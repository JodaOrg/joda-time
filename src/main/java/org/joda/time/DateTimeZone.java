/*
 *  Copyright 2001-2014 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.chrono.BaseChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.FormatUtils;
import org.joda.time.tz.DefaultNameProvider;
import org.joda.time.tz.FixedDateTimeZone;
import org.joda.time.tz.NameProvider;
import org.joda.time.tz.Provider;
import org.joda.time.tz.UTCProvider;
import org.joda.time.tz.ZoneInfoProvider;

/**
 * DateTimeZone represents a time zone.
 * <p>
 * A time zone is a system of rules to convert time from one geographic 
 * location to another. For example, Paris, France is one hour ahead of
 * London, England. Thus when it is 10:00 in London, it is 11:00 in Paris.
 * <p>
 * All time zone rules are expressed, for historical reasons, relative to
 * Greenwich, London. Local time in Greenwich is referred to as Greenwich Mean
 * Time (GMT).  This is similar, but not precisely identical, to Universal 
 * Coordinated Time, or UTC. This library only uses the term UTC.
 * <p>
 * Using this system, America/Los_Angeles is expressed as UTC-08:00, or UTC-07:00
 * in the summer. The offset -08:00 indicates that America/Los_Angeles time is
 * obtained from UTC by adding -08:00, that is, by subtracting 8 hours.
 * <p>
 * The offset differs in the summer because of daylight saving time, or DST.
 * The following definitions are helpful:
 * <ul>
 * <li>UTC - The reference time.
 * <li>Offset - The amount of time a zone differs from UTC. This can vary during the year.
 * <li>Daylight Saving - The process of having two offsets each year, one in winter and one in summer.
 * <li>Raw offset - The base offset of the zone.
 * <li>Additional offset - The additional offset on top of the raw offset.
 *   This is typically zero in winter and one hour in summer in zones that apply DST.
 * <li>Actual offset - The actual offset that applies, which is the combination of the raw offset and additional offset.
 * </ul>
 * <p>
 * For example, in 2018 Greece applied daylight saving.
 * Throughout the whole year, the raw offset was +02:00.
 * In winter, the additional offset was zero, while in summer the additional offset was one hour.
 * Thus, the actual offset was +02:00 in winter and +03:00 in summer.
 * <p>
 * Note: Some governments, most notably Ireland, define daylight saving by describing
 * a "standard" time in summer and a <i>negative</i> DST offset in winter.
 * Joda-Time, like the JDK, follows a model for time-zone data where there is a
 * raw offset all year round and a <i>positive</i> additional offset.
 * As such, callers cannot assume that the raw offset is that defined by law for the zone.
 * <p>
 * Note: Some governments define a daylight saving time that applies for two separate periods.
 * For example, the year might be winter time, then summer time, then a special time equal
 * to winter time, then back to summer time before finally dropping back to winter time.
 * As such, callers cannot assume that the raw and DST offsets directly correlate to summer and winter.
 * <p>
 * Unlike the Java TimeZone class, DateTimeZone is immutable. It also only
 * supports long format time zone ids. Thus PST and ECT are not accepted.
 * However, the factory that accepts a TimeZone will attempt to convert from
 * the old short id to a suitable long id.
 * <p>
 * There are four approaches to loading time-zone data, which are tried in this order:
 * <ol>
 * <li>load the specific {@link Provider} specified by the system property
 *   {@code org.joda.time.DateTimeZone.Provider}.
 * <li>load {@link ZoneInfoProvider} using the data in the filing system folder
 *   pointed to by system property {@code org.joda.time.DateTimeZone.Folder}.
 * <li>load {@link ZoneInfoProvider} using the data in the classpath location
 *   {@code org/joda/time/tz/data}.
 * <li>load {@link UTCProvider}
 * </ol>
 * <p>
 * Unless you override the standard behaviour, the default if the third approach.
 * <p>
 * DateTimeZone is thread-safe and immutable, and all subclasses must be as
 * well.
 * 
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public abstract class DateTimeZone implements Serializable {
    
    /** Serialization version. */
    private static final long serialVersionUID = 5546345482340108586L;

    /** The time zone for Universal Coordinated Time */
    public static final DateTimeZone UTC = UTCDateTimeZone.INSTANCE;
    /** Maximum offset. */
    private static final int MAX_MILLIS = (86400 * 1000) - 1;

    /**
     * The instance that is providing time zones.
     * This is lazily initialized to reduce risks of race conditions at startup.
     */
    private static final AtomicReference<Provider> cProvider =
                    new AtomicReference<Provider>();
    /**
     * The instance that is providing time zone names.
     * This is lazily initialized to reduce risks of race conditions at startup.
     */
    private static final AtomicReference<NameProvider> cNameProvider =
                    new AtomicReference<NameProvider>();
    /**
     * The default time zone.
     * This is lazily initialized to reduce risks of race conditions at startup.
     */
    private static final AtomicReference<DateTimeZone> cDefault =
                    new AtomicReference<DateTimeZone>();
    /**
     * The default TZ data path
     * This is the default classpath location containing the compiled data files.
     */
    public static final String DEFAULT_TZ_DATA_PATH = "org/joda/time/tz/data";

    //-----------------------------------------------------------------------
    /**
     * Gets the default time zone.
     * <p>
     * The default time zone is derived from the system property {@code user.timezone}.
     * If that is {@code null} or is not a valid identifier, then the value of the
     * JDK {@code TimeZone} default is converted. If that fails, {@code UTC} is used.
     * <p>
     * NOTE: If the {@code java.util.TimeZone} default is updated <i>after</i> calling this
     * method, then the change will not be picked up here.
     * 
     * @return the default datetime zone object
     */
    public static DateTimeZone getDefault() {
        DateTimeZone zone = cDefault.get();
        if (zone == null) {
            try {
                try {
                    String id = System.getProperty("user.timezone");
                    if (id != null) {  // null check avoids stack overflow
                        zone = forID(id);
                    }
                } catch (RuntimeException ex) {
                    // ignored
                }
                if (zone == null) {
                    zone = forTimeZone(TimeZone.getDefault());
                }
            } catch (IllegalArgumentException ex) {
                // ignored
            }
            if (zone == null) {
                zone = UTC;
            }
            if (!cDefault.compareAndSet(null, zone)) {
                zone = cDefault.get();
            }
        }
        return zone;
    }

    /**
     * Sets the default time zone.
     * <p>
     * NOTE: Calling this method does <i>not</i> set the {@code java.util.TimeZone} default.
     * 
     * @param zone  the default datetime zone object, must not be null
     * @throws IllegalArgumentException if the zone is null
     * @throws SecurityException if the application has insufficient security rights
     */
    public static void setDefault(DateTimeZone zone) throws SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new JodaTimePermission("DateTimeZone.setDefault"));
        }
        if (zone == null) {
            throw new IllegalArgumentException("The datetime zone must not be null");
        }
        cDefault.set(zone);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a time zone instance for the specified time zone id.
     * <p>
     * The time zone id may be one of those returned by getAvailableIDs.
     * Short ids, as accepted by {@link java.util.TimeZone}, are not accepted.
     * All IDs must be specified in the long format.
     * The exception is UTC, which is an acceptable id.
     * <p>
     * Alternatively a locale independent, fixed offset, datetime zone can
     * be specified. The form <code>[+-]hh:mm</code> can be used.
     * 
     * @param id  the ID of the datetime zone, null means default
     * @return the DateTimeZone object for the ID
     * @throws IllegalArgumentException if the ID is not recognised
     */
    @FromString
    public static DateTimeZone forID(String id) {
        if (id == null) {
            return getDefault();
        }
        if (id.equals("UTC")) {
            return DateTimeZone.UTC;
        }
        DateTimeZone zone = getProvider().getZone(id);
        if (zone != null) {
            return zone;
        }
        if (id.startsWith("+") || id.startsWith("-")) {
            int offset = parseOffset(id);
            if (offset == 0L) {
                return DateTimeZone.UTC;
            } else {
                id = printOffset(offset);
                return fixedOffsetZone(id, offset);
            }
        }
        throw new IllegalArgumentException("The datetime zone id '" + id + "' is not recognised");
    }

    /**
     * Gets a time zone instance for the specified offset to UTC in hours.
     * This method assumes standard length hours.
     * <p>
     * This factory is a convenient way of constructing zones with a fixed offset.
     * 
     * @param hoursOffset  the offset in hours from UTC, from -23 to +23
     * @return the DateTimeZone object for the offset
     * @throws IllegalArgumentException if the offset is too large or too small
     */
    public static DateTimeZone forOffsetHours(int hoursOffset) throws IllegalArgumentException {
        return forOffsetHoursMinutes(hoursOffset, 0);
    }

    /**
     * Gets a time zone instance for the specified offset to UTC in hours and minutes.
     * This method assumes 60 minutes in an hour, and standard length minutes.
     * <p>
     * This factory is a convenient way of constructing zones with a fixed offset.
     * The hours value must be in the range -23 to +23.
     * The minutes value must be in the range -59 to +59.
     * The following combinations of sign for the hour and minute are possible:
     * <pre>
     *  Hour    Minute    Example    Result
     * 
     *  +ve     +ve       (2, 15)    +02:15
     *  +ve     zero      (2, 0)     +02:00
     *  +ve     -ve       (2, -15)   IllegalArgumentException
     * 
     *  zero    +ve       (0, 15)    +00:15
     *  zero    zero      (0, 0)     +00:00
     *  zero    -ve       (0, -15)   -00:15
     * 
     *  -ve     +ve       (-2, 15)   -02:15
     *  -ve     zero      (-2, 0)    -02:00
     *  -ve     -ve       (-2, -15)  -02:15
     * </pre>
     * Note that in versions before 2.3, the minutes had to be zero or positive.
     * 
     * @param hoursOffset  the offset in hours from UTC, from -23 to +23
     * @param minutesOffset  the offset in minutes from UTC, from -59 to +59
     * @return the DateTimeZone object for the offset
     * @throws IllegalArgumentException if any value is out of range, the minutes are negative
     *  when the hours are positive, or the resulting offset exceeds +/- 23:59:59.000
     */
    public static DateTimeZone forOffsetHoursMinutes(int hoursOffset, int minutesOffset) throws IllegalArgumentException {
        if (hoursOffset == 0 && minutesOffset == 0) {
            return DateTimeZone.UTC;
        }
        if (hoursOffset < -23 || hoursOffset > 23) {
            throw new IllegalArgumentException("Hours out of range: " + hoursOffset);
        }
        if (minutesOffset < -59 || minutesOffset > 59) {
            throw new IllegalArgumentException("Minutes out of range: " + minutesOffset);
        }
        if (hoursOffset > 0 && minutesOffset < 0) {
            throw new IllegalArgumentException("Positive hours must not have negative minutes: " + minutesOffset);
        }
        int offset = 0;
        try {
            int hoursInMinutes = hoursOffset * 60;
            if (hoursInMinutes < 0) {
                minutesOffset = hoursInMinutes - Math.abs(minutesOffset);
            } else {
                minutesOffset = hoursInMinutes + minutesOffset;
            }
            offset = FieldUtils.safeMultiply(minutesOffset, DateTimeConstants.MILLIS_PER_MINUTE);
        } catch (ArithmeticException ex) {
            throw new IllegalArgumentException("Offset is too large");
        }
        return forOffsetMillis(offset);
    }

    /**
     * Gets a time zone instance for the specified offset to UTC in milliseconds.
     *
     * @param millisOffset  the offset in millis from UTC, from -23:59:59.999 to +23:59:59.999
     * @return the DateTimeZone object for the offset
     */
    public static DateTimeZone forOffsetMillis(int millisOffset) {
        if (millisOffset < -MAX_MILLIS || millisOffset > MAX_MILLIS) {
            throw new IllegalArgumentException("Millis out of range: " + millisOffset);
        }
        String id = printOffset(millisOffset);
        return fixedOffsetZone(id, millisOffset);
    }

    /**
     * Gets a time zone instance for a JDK TimeZone.
     * <p>
     * DateTimeZone only accepts a subset of the IDs from TimeZone. The
     * excluded IDs are the short three letter form (except UTC). This 
     * method will attempt to convert between time zones created using the
     * short IDs and the full version.
     * <p>
     * This method is not designed to parse time zones with rules created by
     * applications using <code>SimpleTimeZone</code> directly.
     * 
     * @param zone  the zone to convert, null means default
     * @return the DateTimeZone object for the zone
     * @throws IllegalArgumentException if the zone is not recognised
     */
    public static DateTimeZone forTimeZone(TimeZone zone) {
        if (zone == null) {
            return getDefault();
        }
        final String id = zone.getID();
        if (id == null) {
            throw new IllegalArgumentException("The TimeZone id must not be null");
        }
        if (id.equals("UTC")) {
            return DateTimeZone.UTC;
        }

        // Convert from old alias before consulting provider since they may differ.
        DateTimeZone dtz = null;
        String convId = getConvertedId(id);
        Provider provider = getProvider();
        if (convId != null) {
            dtz = provider.getZone(convId);
        }
        if (dtz == null) {
            dtz = provider.getZone(id);
        }
        if (dtz != null) {
            return dtz;
        }

        // Support GMT+/-hh:mm formats
        if (convId == null) {
            convId = id;
            if (convId.startsWith("GMT+") || convId.startsWith("GMT-")) {
                convId = convId.substring(3);
                if (convId.length() > 2) {
                    char firstDigit = convId.charAt(1);
                    if (firstDigit > '9' && Character.isDigit(firstDigit)) {
                        convId = convertToAsciiNumber(convId);
                    }
                }
                int offset = parseOffset(convId);
                if (offset == 0L) {
                    return DateTimeZone.UTC;
                } else {
                    convId = printOffset(offset);
                    return fixedOffsetZone(convId, offset);
                }
            }
        }
        throw new IllegalArgumentException("The datetime zone id '" + id + "' is not recognised");
    }

    private static String convertToAsciiNumber(String convId) {
        StringBuilder buf = new StringBuilder(convId);
        for (int i = 0; i < buf.length(); i++) {
            char ch = buf.charAt(i);
            int digit = Character.digit(ch, 10);
            if (digit >= 0) {
                buf.setCharAt(i, (char) ('0' + digit));
            }
        }
        return buf.toString();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the zone using a fixed offset amount.
     * 
     * @param id  the zone id
     * @param offset  the offset in millis
     * @return the zone
     */
    private static DateTimeZone fixedOffsetZone(String id, int offset) {
        if (offset == 0) {
            return DateTimeZone.UTC;
        }
        return new FixedDateTimeZone(id, null, offset, offset);
    }

    /**
     * Gets all the available IDs supported.
     * 
     * @return an unmodifiable Set of String IDs
     */
    public static Set<String> getAvailableIDs() {
        return getProvider().getAvailableIDs();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the zone provider factory.
     * <p>
     * The zone provider is a pluggable instance factory that supplies the
     * actual instances of DateTimeZone.
     * 
     * @return the provider
     */
    public static Provider getProvider() {
        Provider provider = cProvider.get();
        if (provider == null) {
            provider = getDefaultProvider();
            if (!cProvider.compareAndSet(null, provider)) {
                provider = cProvider.get();
            }
        }
        return provider;
    }

    /**
     * Sets the zone provider factory.
     * <p>
     * The zone provider is a pluggable instance factory that supplies the
     * actual instances of DateTimeZone.
     * 
     * @param provider  provider to use, or null for default
     * @throws SecurityException if you do not have the permission DateTimeZone.setProvider
     * @throws IllegalArgumentException if the provider is invalid
     */
    public static void setProvider(Provider provider) throws SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new JodaTimePermission("DateTimeZone.setProvider"));
        }
        if (provider == null) {
            provider = getDefaultProvider();
        } else {
            validateProvider(provider);
        }
        cProvider.set(provider);
    }

    /**
     * Sets the zone provider factory without performing the security check.
     * 
     * @param provider  provider to use, or null for default
     * @return the provider
     * @throws IllegalArgumentException if the provider is invalid
     */
    private static Provider validateProvider(Provider provider) {
        Set<String> ids = provider.getAvailableIDs();
        if (ids == null || ids.size() == 0) {
            throw new IllegalArgumentException("The provider doesn't have any available ids");
        }
        if (!ids.contains("UTC")) {
            throw new IllegalArgumentException("The provider doesn't support UTC");
        }
        if (!UTC.equals(provider.getZone("UTC"))) {
            throw new IllegalArgumentException("Invalid UTC zone provided");
        }
        return provider;
    }

    /**
     * Gets the default zone provider.
     * <p>
     * This tries four approaches to loading data:
     * <ol>
     * <li>loads the provider identifier by the system property
     *   <code>org.joda.time.DateTimeZone.Provider</code>.
     * <li>load <code>ZoneInfoProvider</code> using the data in the filing system folder
     *   pointed to by system property <code>org.joda.time.DateTimeZone.Folder</code>.
     * <li>loads <code>ZoneInfoProvider</code> using the data in the classpath location
     *   <code>org/joda/time/tz/data</code>.
     * <li>loads <code>UTCProvider</code>.
     * </ol>
     * <p>
     * Unless you override the standard behaviour, the default if the third approach.
     * 
     * @return the default name provider
     */
    private static Provider getDefaultProvider() {
        // approach 1
        try {
            String providerClass = System.getProperty("org.joda.time.DateTimeZone.Provider");
            if (providerClass != null) {
                try {
                    // do not initialize the class until the type has been checked
                    Class<?> cls = Class.forName(providerClass, false, DateTimeZone.class.getClassLoader());
                    if (!Provider.class.isAssignableFrom(cls)) {
                        throw new IllegalArgumentException("System property referred to class that does not implement " + Provider.class);
                    }
                    Provider provider = cls.asSubclass(Provider.class).getConstructor().newInstance();
                    return validateProvider(provider);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        } catch (SecurityException ex) {
            // ignored
        }
        // approach 2
        try {
            String dataFolder = System.getProperty("org.joda.time.DateTimeZone.Folder");
            if (dataFolder != null) {
                try {
                    Provider provider = new ZoneInfoProvider(new File(dataFolder));
                    return validateProvider(provider);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        } catch (SecurityException ex) {
            // ignored
        }
        // approach 3
        try {
            Provider provider = new ZoneInfoProvider(DEFAULT_TZ_DATA_PATH);
            return validateProvider(provider);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // approach 4
        return new UTCProvider();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the name provider factory.
     * <p>
     * The name provider is a pluggable instance factory that supplies the
     * names of each DateTimeZone.
     * 
     * @return the provider
     */
    public static NameProvider getNameProvider() {
        NameProvider nameProvider = cNameProvider.get();
        if (nameProvider == null) {
            nameProvider = getDefaultNameProvider();
            if (!cNameProvider.compareAndSet(null, nameProvider)) {
                nameProvider = cNameProvider.get();
            }
        }
        return nameProvider;
    }

    /**
     * Sets the name provider factory.
     * <p>
     * The name provider is a pluggable instance factory that supplies the
     * names of each DateTimeZone.
     * 
     * @param nameProvider  provider to use, or null for default
     * @throws SecurityException if you do not have the permission DateTimeZone.setNameProvider
     * @throws IllegalArgumentException if the provider is invalid
     */
    public static void setNameProvider(NameProvider nameProvider) throws SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new JodaTimePermission("DateTimeZone.setNameProvider"));
        }
        if (nameProvider == null) {
            nameProvider = getDefaultNameProvider();
        }
        cNameProvider.set(nameProvider);
    }

    /**
     * Gets the default name provider.
     * <p>
     * Tries the system property <code>org.joda.time.DateTimeZone.NameProvider</code>.
     * Then uses <code>DefaultNameProvider</code>.
     * 
     * @return the default name provider
     */
    private static NameProvider getDefaultNameProvider() {
        NameProvider nameProvider = null;
        try {
            String providerClass = System.getProperty("org.joda.time.DateTimeZone.NameProvider");
            if (providerClass != null) {
                try {
                    // do not initialize the class until the type has been checked
                    Class<?> cls = Class.forName(providerClass, false, DateTimeZone.class.getClassLoader());
                    if (!NameProvider.class.isAssignableFrom(cls)) {
                        throw new IllegalArgumentException("System property referred to class that does not implement " + NameProvider.class);
                    }
                    nameProvider = cls.asSubclass(NameProvider.class).getConstructor().newInstance();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        } catch (SecurityException ex) {
            // ignore
        }

        if (nameProvider == null) {
            nameProvider = new DefaultNameProvider();
        }

        return nameProvider;
    }

    //-----------------------------------------------------------------------
    /**
     * Converts an old style id to a new style id.
     * 
     * @param id  the old style id
     * @return the new style id, null if not found
     */
    private static String getConvertedId(String id) {
        return LazyInit.CONVERSION_MAP.get(id);
    }

    /**
     * Parses an offset from the string.
     * 
     * @param str  the string
     * @return the offset millis
     */
    private static int parseOffset(String str) {
        return -(int) LazyInit.OFFSET_FORMATTER.parseMillis(str);
    }

    /**
     * Formats a timezone offset string.
     * <p>
     * This method is kept separate from the formatting classes to speed and
     * simplify startup and classloading.
     * 
     * @param offset  the offset in milliseconds
     * @return the time zone string
     */
    private static String printOffset(int offset) {
        StringBuffer buf = new StringBuffer();
        if (offset >= 0) {
            buf.append('+');
        } else {
            buf.append('-');
            offset = -offset;
        }

        int hours = offset / DateTimeConstants.MILLIS_PER_HOUR;
        FormatUtils.appendPaddedInteger(buf, hours, 2);
        offset -= hours * (int) DateTimeConstants.MILLIS_PER_HOUR;

        int minutes = offset / DateTimeConstants.MILLIS_PER_MINUTE;
        buf.append(':');
        FormatUtils.appendPaddedInteger(buf, minutes, 2);
        offset -= minutes * DateTimeConstants.MILLIS_PER_MINUTE;
        if (offset == 0) {
            return buf.toString();
        }

        int seconds = offset / DateTimeConstants.MILLIS_PER_SECOND;
        buf.append(':');
        FormatUtils.appendPaddedInteger(buf, seconds, 2);
        offset -= seconds * DateTimeConstants.MILLIS_PER_SECOND;
        if (offset == 0) {
            return buf.toString();
        }

        buf.append('.');
        FormatUtils.appendPaddedInteger(buf, offset, 3);
        return buf.toString();
    }

    // Instance fields and methods
    //--------------------------------------------------------------------

    private final String iID;

    /**
     * Constructor.
     * 
     * @param id  the id to use
     * @throws IllegalArgumentException if the id is null
     */
    protected DateTimeZone(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        iID = id;
    }

    // Principal methods
    //--------------------------------------------------------------------

    /**
     * Gets the ID of this datetime zone.
     * 
     * @return the ID of this datetime zone
     */
    @ToString
    public final String getID() {
        return iID;
    }

    /**
     * Returns a non-localized name that is unique to this time zone. It can be
     * combined with id to form a unique key for fetching localized names.
     *
     * @param instant  milliseconds from 1970-01-01T00:00:00Z to get the name for
     * @return name key or null if id should be used for names
     */
    public abstract String getNameKey(long instant);

    /**
     * Gets the short name of this datetime zone suitable for display using
     * the default locale.
     * <p>
     * If the name is not available for the locale, then this method returns a
     * string in the format <code>[+-]hh:mm</code>.
     * 
     * @param instant  milliseconds from 1970-01-01T00:00:00Z to get the name for
     * @return the human-readable short name in the default locale
     */
    public final String getShortName(long instant) {
        return getShortName(instant, null);
    }

    /**
     * Gets the short name of this datetime zone suitable for display using
     * the specified locale.
     * <p>
     * If the name is not available for the locale, then this method returns a
     * string in the format <code>[+-]hh:mm</code>.
     * 
     * @param instant  milliseconds from 1970-01-01T00:00:00Z to get the name for
     * @param locale  the locale to get the name for
     * @return the human-readable short name in the specified locale
     */
    public String getShortName(long instant, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        String nameKey = getNameKey(instant);
        if (nameKey == null) {
            return iID;
        }
        String name;
        NameProvider np = getNameProvider();
        if (np instanceof DefaultNameProvider) {
            name = ((DefaultNameProvider) np).getShortName(locale, iID, nameKey, isStandardOffset(instant));
        } else {
            name = np.getShortName(locale, iID, nameKey);
        }
        if (name != null) {
            return name;
        }
        return printOffset(getOffset(instant));
    }

    /**
     * Gets the long name of this datetime zone suitable for display using
     * the default locale.
     * <p>
     * If the name is not available for the locale, then this method returns a
     * string in the format <code>[+-]hh:mm</code>.
     * 
     * @param instant  milliseconds from 1970-01-01T00:00:00Z to get the name for
     * @return the human-readable long name in the default locale
     */
    public final String getName(long instant) {
        return getName(instant, null);
    }

    /**
     * Gets the long name of this datetime zone suitable for display using
     * the specified locale.
     * <p>
     * If the name is not available for the locale, then this method returns a
     * string in the format <code>[+-]hh:mm</code>.
     * 
     * @param instant  milliseconds from 1970-01-01T00:00:00Z to get the name for
     * @param locale  the locale to get the name for
     * @return the human-readable long name in the specified locale
     */
    public String getName(long instant, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        String nameKey = getNameKey(instant);
        if (nameKey == null) {
            return iID;
        }
        String name;
        NameProvider np = getNameProvider();
        if (np instanceof DefaultNameProvider) {
            name = ((DefaultNameProvider) np).getName(locale, iID, nameKey, isStandardOffset(instant));
        } else {
            name = np.getName(locale, iID, nameKey);
        }
        if (name != null) {
            return name;
        }
        return printOffset(getOffset(instant));
    }

    /**
     * Gets the millisecond offset to add to UTC to get local time.
     * <p>
     * This returns the actual offset from UTC for the zone at the specified instant.
     * If the method is called with a different instant, the offset returned may be different
     * as a result of daylight saving or other government rule changes.
     * 
     * @param instant  milliseconds from 1970-01-01T00:00:00Z to get the offset for
     * @return the millisecond offset to add to UTC to get local time
     */
    public abstract int getOffset(long instant);

    /**
     * Gets the millisecond offset to add to UTC to get local time.
     * <p>
     * This returns the actual offset from UTC for the zone at the specified instant.
     * If the method is called with a different instant, the offset returned may be different
     * as a result of daylight saving or other government rule changes.
     * 
     * @param instant  instant to get the offset for, null means now
     * @return the millisecond offset to add to UTC to get local time
     */
    public final int getOffset(ReadableInstant instant) {
        if (instant == null) {
            return getOffset(DateTimeUtils.currentTimeMillis());
        }
        return getOffset(instant.getMillis());
    }

    /**
     * Gets the raw millisecond offset to add to UTC.
     * <p>
     * This should be treated as an implementation detail.
     * End-users should use {@link #getOffset(long)}.
     * <p>
     * This returns the raw offset from UTC for the zone at the specified instant, effectively ignoring DST.
     * If the method is called with a different instant, the offset returned may be different
     * as a result of government rule changes.
     * <p>
     * This method should be named {@code getRawOffset()} but cannot be renamed for compatibility reasons.
     * 
     * @param instant  milliseconds from 1970-01-01T00:00:00Z to get the offset for
     * @return the millisecond offset to add to UTC to get local time
     */
    public abstract int getStandardOffset(long instant);

    /**
     * Checks whether, at a particular instant, the offset is raw or not.
     * <p>
     * This method can be used to estimate whether Summer Time (DST) applies at the specified instant.
     * As a general rule, if the actual offset equals the raw offset at the specified instant
     * then either winter time applies or the zone does not have DST rules.
     * If the actual offset does not equal the raw offset, then some form of Summer Time applies.
     * <p>
     * The implementation of the method is simply whether {@link #getOffset(long)}
     * equals {@link #getStandardOffset(long)} at the specified instant.
     * <p>
     * This method should be named {@code isRawOffsetInUse()} but cannot be renamed for compatibility reasons.
     * 
     * @param instant  milliseconds from 1970-01-01T00:00:00Z to get the offset for
     * @return true if the offset at the given instant is the same as the raw offset
     * @since 1.5
     */
    public boolean isStandardOffset(long instant) {
        return getOffset(instant) == getStandardOffset(instant);
    }

    /**
     * Gets the millisecond offset to subtract from local time to get UTC time.
     * This offset can be used to undo adding the offset obtained by getOffset.
     *
     * <pre>
     * millisLocal == millisUTC   + getOffset(millisUTC)
     * millisUTC   == millisLocal - getOffsetFromLocal(millisLocal)
     * </pre>
     *
     * NOTE: After calculating millisLocal, some error may be introduced. At
     * offset transitions (due to DST or other historical changes), ranges of
     * local times may map to different UTC times.
     * <p>
     * For overlaps (where the local time is ambiguous), this method returns the
     * offset applicable before the gap. The effect of this is that any instant
     * calculated using the offset from an overlap will be in "summer" time.
     * <p>
     * For gaps, this method returns the offset applicable before the gap, ie "winter" offset.
     * However, the effect of this is that any instant calculated using the offset
     * from a gap will be after the gap, in "summer" time.
     * <p>
     * For example, consider a zone with a gap from 01:00 to 01:59:<br />
     * Input: 00:00 (before gap) Output: Offset applicable before gap  DateTime: 00:00<br />
     * Input: 00:30 (before gap) Output: Offset applicable before gap  DateTime: 00:30<br />
     * Input: 01:00 (in gap)     Output: Offset applicable before gap  DateTime: 02:00<br />
     * Input: 01:30 (in gap)     Output: Offset applicable before gap  DateTime: 02:30<br />
     * Input: 02:00 (after gap)  Output: Offset applicable after gap   DateTime: 02:00<br />
     * Input: 02:30 (after gap)  Output: Offset applicable after gap   DateTime: 02:30<br />
     * <p>
     * NOTE: Prior to v2.0, the DST overlap behaviour was not defined and varied by hemisphere.
     * Prior to v1.5, the DST gap behaviour was also not defined.
     * In v2.4, the documentation was clarified again.
     *
     * @param instantLocal  the millisecond instant, relative to this time zone, to get the offset for
     * @return the millisecond offset to subtract from local time to get UTC time
     */
    public int getOffsetFromLocal(long instantLocal) {
        // get the offset at instantLocal (first estimate)
        final int offsetLocal = getOffset(instantLocal);
        // adjust instantLocal using the estimate and recalc the offset
        final long instantAdjusted = instantLocal - offsetLocal;
        final int offsetAdjusted = getOffset(instantAdjusted);
        // if the offsets differ, we must be near a DST boundary
        if (offsetLocal != offsetAdjusted) {
            // we need to ensure that time is always after the DST gap
            // this happens naturally for positive offsets, but not for negative
            if ((offsetLocal - offsetAdjusted) < 0) {
                // if we just return offsetAdjusted then the time is pushed
                // back before the transition, whereas it should be
                // on or after the transition
                long nextLocal = nextTransition(instantAdjusted);
                if (nextLocal == (instantLocal - offsetLocal)) {
                    nextLocal = Long.MAX_VALUE;
                }
                long nextAdjusted = nextTransition(instantLocal - offsetAdjusted);
                if (nextAdjusted == (instantLocal - offsetAdjusted)) {
                    nextAdjusted = Long.MAX_VALUE;
                }
                if (nextLocal != nextAdjusted) {
                    return offsetLocal;
                }
            }
        } else if (offsetLocal >= 0) {
            long prev = previousTransition(instantAdjusted);
            if (prev < instantAdjusted) {
                int offsetPrev = getOffset(prev);
                int diff = offsetPrev - offsetLocal;
                if (instantAdjusted - prev <= diff) {
                    return offsetPrev;
                }
            }
        }
        return offsetAdjusted;
    }

    /**
     * Converts an actual UTC instant to a local instant with the same
     * local time. This conversion is used before performing a calculation
     * so that the calculation can be done using a simple local zone.
     *
     * @param instantUTC  the UTC instant to convert to local
     * @return the local instant with the same local time
     * @throws ArithmeticException if the result overflows a long
     * @since 1.5
     */
    public long convertUTCToLocal(long instantUTC) {
        int offset = getOffset(instantUTC);
        long instantLocal = instantUTC + offset;
        // If there is a sign change, but the two values have the same sign...
        if ((instantUTC ^ instantLocal) < 0 && (instantUTC ^ offset) >= 0) {
            throw new ArithmeticException("Adding time zone offset caused overflow");
        }
        return instantLocal;
    }

    /**
     * Converts a local instant to an actual UTC instant with the same
     * local time attempting to use the same offset as the original.
     * <p>
     * This conversion is used after performing a calculation
     * where the calculation was done using a simple local zone.
     * Whenever possible, the same offset as the original offset will be used.
     * This is most significant during a daylight savings overlap.
     *
     * @param instantLocal  the local instant to convert to UTC
     * @param strict  whether the conversion should reject non-existent local times
     * @param originalInstantUTC  the original instant that the calculation is based on
     * @return the UTC instant with the same local time, 
     * @throws ArithmeticException if the result overflows a long
     * @throws IllegalArgumentException if the zone has no equivalent local time
     * @since 2.0
     */
    public long convertLocalToUTC(long instantLocal, boolean strict, long originalInstantUTC) {
        int offsetOriginal = getOffset(originalInstantUTC);
        long instantUTC = instantLocal - offsetOriginal;
        int offsetLocalFromOriginal = getOffset(instantUTC);
        if (offsetLocalFromOriginal == offsetOriginal) {
            return instantUTC;
        }
        return convertLocalToUTC(instantLocal, strict);
    }

    /**
     * Converts a local instant to an actual UTC instant with the same
     * local time. This conversion is used after performing a calculation
     * where the calculation was done using a simple local zone.
     *
     * @param instantLocal  the local instant to convert to UTC
     * @param strict  whether the conversion should reject non-existent local times
     * @return the UTC instant with the same local time, 
     * @throws ArithmeticException if the result overflows a long
     * @throws IllegalInstantException if the zone has no equivalent local time
     * @since 1.5
     */
    public long convertLocalToUTC(long instantLocal, boolean strict) {
        // get the offset at instantLocal (first estimate)
        int offsetLocal = getOffset(instantLocal);
        // adjust instantLocal using the estimate and recalc the offset
        int offset = getOffset(instantLocal - offsetLocal);
        // if the offsets differ, we must be near a DST boundary
        if (offsetLocal != offset) {
            // if strict then always check if in DST gap
            // otherwise only check if zone in Western hemisphere (as the
            // value of offset is already correct for Eastern hemisphere)
            if (strict || offsetLocal < 0) {
                // determine if we are in the DST gap
                long nextLocal = nextTransition(instantLocal - offsetLocal);
                if (nextLocal == (instantLocal - offsetLocal)) {
                    nextLocal = Long.MAX_VALUE;
                }
                long nextAdjusted = nextTransition(instantLocal - offset);
                if (nextAdjusted == (instantLocal - offset)) {
                    nextAdjusted = Long.MAX_VALUE;
                }
                if (nextLocal != nextAdjusted) {
                    // yes we are in the DST gap
                    if (strict) {
                        // DST gap is not acceptable
                        throw new IllegalInstantException(instantLocal, getID());
                    } else {
                        // DST gap is acceptable, but for the Western hemisphere
                        // the offset is wrong and will result in local times
                        // before the cutover so use the offsetLocal instead
                        offset = offsetLocal;
                    }
                }
            }
        }
        // check for overflow
        long instantUTC = instantLocal - offset;
        // If there is a sign change, but the two values have different signs...
        if ((instantLocal ^ instantUTC) < 0 && (instantLocal ^ offset) < 0) {
            throw new ArithmeticException("Subtracting time zone offset caused overflow");
        }
        return instantUTC;
    }

    /**
     * Gets the millisecond instant in another zone keeping the same local time.
     * <p>
     * The conversion is performed by converting the specified UTC millis to local
     * millis in this zone, then converting back to UTC millis in the new zone.
     *
     * @param newZone  the new zone, null means default
     * @param oldInstant  the UTC millisecond instant to convert
     * @return the UTC millisecond instant with the same local time in the new zone
     */
    public long getMillisKeepLocal(DateTimeZone newZone, long oldInstant) {
        if (newZone == null) {
            newZone = DateTimeZone.getDefault();
        }
        if (newZone == this) {
            return oldInstant;
        }
        long instantLocal = convertUTCToLocal(oldInstant);
        return newZone.convertLocalToUTC(instantLocal, false, oldInstant);
    }

    /**
     * Checks if the given {@link LocalDateTime} is within a gap.
     * <p>
     * When switching into Daylight Savings Time there is typically a gap where a clock hour is missing.
     * This method identifies whether the local datetime refers to such a gap.
     * 
     * @param localDateTime  the time to check, not null
     * @return true if the given datetime refers to a gap
     * @since 1.6
     */
    public boolean isLocalDateTimeGap(LocalDateTime localDateTime) {
        if (isFixed()) {
            return false;
        }
        try {
            localDateTime.toDateTime(this);
            return false;
        } catch (IllegalInstantException ex) {
            return true;
        }
    }

    /**
     * Adjusts the offset to be the earlier or later one during an overlap.
     * 
     * @param instant  the instant to adjust
     * @param earlierOrLater  false for earlier, true for later
     * @return the adjusted instant millis
     */
    public long adjustOffset(long instant, boolean earlierOrLater) {
        // a bit messy, but will work in all non-pathological cases
        
        // evaluate 3 hours before and after to work out if anything is happening
        long instantBefore = instant - 3 * DateTimeConstants.MILLIS_PER_HOUR;
        long instantAfter = instant + 3 * DateTimeConstants.MILLIS_PER_HOUR;
        long offsetBefore = getOffset(instantBefore);
        long offsetAfter = getOffset(instantAfter);
        if (offsetBefore <= offsetAfter) {
            return instant;  // not an overlap (less than is a gap, equal is normal case)
        }
        
        // work out range of instants that have duplicate local times
        long diff = offsetBefore - offsetAfter;
        long transition = nextTransition(instantBefore);
        long overlapStart = transition - diff;
        long overlapEnd = transition + diff;
        if (instant < overlapStart || instant >= overlapEnd) {
          return instant;  // not an overlap
        }
        
        // calculate result
        long afterStart = instant - overlapStart;
        if (afterStart >= diff) {
          // currently in later offset
          return earlierOrLater ? instant : instant - diff;
        } else {
          // currently in earlier offset
          return earlierOrLater ? instant + diff : instant;
        }
    }
//    System.out.println(new DateTime(transitionStart, DateTimeZone.UTC) + " " + new DateTime(transitionStart, this));

    //-----------------------------------------------------------------------
    /**
     * Returns true if this time zone has no transitions.
     *
     * @return true if no transitions
     */
    public abstract boolean isFixed();

    /**
     * Advances the given instant to where the time zone offset or name changes.
     * If the instant returned is exactly the same as passed in, then
     * no changes occur after the given instant.
     *
     * @param instant  milliseconds from 1970-01-01T00:00:00Z
     * @return milliseconds from 1970-01-01T00:00:00Z
     */
    public abstract long nextTransition(long instant);

    /**
     * Retreats the given instant to where the time zone offset or name changes.
     * If the instant returned is exactly the same as passed in, then
     * no changes occur before the given instant.
     *
     * @param instant  milliseconds from 1970-01-01T00:00:00Z
     * @return milliseconds from 1970-01-01T00:00:00Z
     */
    public abstract long previousTransition(long instant);

    // Basic methods
    //--------------------------------------------------------------------

    /**
     * Get the datetime zone as a {@link java.util.TimeZone}.
     * 
     * @return the closest matching TimeZone object
     */
    public java.util.TimeZone toTimeZone() {
        return java.util.TimeZone.getTimeZone(iID);
    }

    /**
     * Compare this datetime zone with another.
     * 
     * @param object the object to compare with
     * @return true if equal, based on the ID and all internal rules
     */
    public abstract boolean equals(Object object);

    /**
     * Gets a hash code compatible with equals.
     * 
     * @return suitable hashcode
     */
    public int hashCode() {
        return 57 + getID().hashCode();
    }

    /**
     * Gets the datetime zone as a string, which is simply its ID.
     * @return the id of the zone
     */
    public String toString() {
        return getID();
    }

    /**
     * By default, when DateTimeZones are serialized, only a "stub" object
     * referring to the id is written out. When the stub is read in, it
     * replaces itself with a DateTimeZone object.
     * @return a stub object to go in the stream
     */
    protected Object writeReplace() throws ObjectStreamException {
        return new Stub(iID);
    }

    /**
     * Used to serialize DateTimeZones by id.
     */
    private static final class Stub implements Serializable {
        /** Serialization lock. */
        private static final long serialVersionUID = -6471952376487863581L;
        /** The ID of the zone. */
        private transient String iID;

        /**
         * Constructor.
         * @param id  the id of the zone
         */
        Stub(String id) {
            iID = id;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeUTF(iID);
        }

        private void readObject(ObjectInputStream in) throws IOException {
            iID = in.readUTF();
        }

        private Object readResolve() throws ObjectStreamException {
            return forID(iID);
        }
    }

    //-------------------------------------------------------------------------
    /**
     * Lazy initialization to avoid a synchronization lock.
     */
    static final class LazyInit {

        /** Cache of old zone IDs to new zone IDs */
        static final Map<String, String> CONVERSION_MAP = buildMap();
        /** Time zone offset formatter. */
        static final DateTimeFormatter OFFSET_FORMATTER = buildFormatter();

        private static DateTimeFormatter buildFormatter() {
            // Can't use a real chronology if called during class
            // initialization. Offset parser doesn't need it anyhow.
            Chronology chrono = new BaseChronology() {
                private static final long serialVersionUID = -3128740902654445468L;
                public DateTimeZone getZone() {
                    return null;
                }
                public Chronology withUTC() {
                    return this;
                }
                public Chronology withZone(DateTimeZone zone) {
                    return this;
                }
                public String toString() {
                    return getClass().getName();
                }
            };
            return new DateTimeFormatterBuilder()
                .appendTimeZoneOffset(null, true, 2, 4)
                .toFormatter()
                .withChronology(chrono);
        }

        private static Map<String, String> buildMap() {
            // Backwards compatibility with TimeZone.
            Map<String, String> map = new HashMap<String, String>();
            map.put("GMT", "UTC");
            map.put("WET", "WET");
            map.put("CET", "CET");
            map.put("MET", "CET");
            map.put("ECT", "CET");
            map.put("EET", "EET");
            map.put("MIT", "Pacific/Apia");
            map.put("HST", "Pacific/Honolulu");  // JDK 1.1 compatible
            map.put("AST", "America/Anchorage");
            map.put("PST", "America/Los_Angeles");
            map.put("MST", "America/Denver");  // JDK 1.1 compatible
            map.put("PNT", "America/Phoenix");
            map.put("CST", "America/Chicago");
            map.put("EST", "America/New_York");  // JDK 1.1 compatible
            map.put("IET", "America/Indiana/Indianapolis");
            map.put("PRT", "America/Puerto_Rico");
            map.put("CNT", "America/St_Johns");
            map.put("AGT", "America/Argentina/Buenos_Aires");
            map.put("BET", "America/Sao_Paulo");
            map.put("ART", "Africa/Cairo");
            map.put("CAT", "Africa/Harare");
            map.put("EAT", "Africa/Addis_Ababa");
            map.put("NET", "Asia/Yerevan");
            map.put("PLT", "Asia/Karachi");
            map.put("IST", "Asia/Kolkata");
            map.put("BST", "Asia/Dhaka");
            map.put("VST", "Asia/Ho_Chi_Minh");
            map.put("CTT", "Asia/Shanghai");
            map.put("JST", "Asia/Tokyo");
            map.put("ACT", "Australia/Darwin");
            map.put("AET", "Australia/Sydney");
            map.put("SST", "Pacific/Guadalcanal");
            map.put("NST", "Pacific/Auckland");
            return Collections.unmodifiableMap(map);
        }
    }

}
