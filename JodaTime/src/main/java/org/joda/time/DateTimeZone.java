/*
 *  Copyright 2001-2011 Stephen Colebourne
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.chrono.BaseChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormat;
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
 * The following definitions of time are generally used:
 * <ul>
 * <li>UTC - The reference time.
 * <li>Standard Time - The local time without a daylight saving time offset.
 * For example, in Paris, standard time is UTC+01:00.
 * <li>Daylight Saving Time - The local time with a daylight saving time 
 * offset. This offset is typically one hour, but not always. It is typically
 * used in most countries away from the equator.  In Paris, daylight saving 
 * time is UTC+02:00.
 * <li>Wall Time - This is what a local clock on the wall reads. This will be
 * either Standard Time or Daylight Saving Time depending on the time of year
 * and whether the location uses Daylight Saving Time.
 * </ul>
 * <p>
 * Unlike the Java TimeZone class, DateTimeZone is immutable. It also only
 * supports long format time zone ids. Thus EST and ECT are not accepted.
 * However, the factory that accepts a TimeZone will attempt to convert from
 * the old short id to a suitable long id.
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
    public static final DateTimeZone UTC = new FixedDateTimeZone("UTC", "UTC", 0, 0);

    /** The instance that is providing time zones. */
    private static Provider cProvider;
    /** The instance that is providing time zone names. */
    private static NameProvider cNameProvider;
    /** The set of ID strings. */
    private static Set<String> cAvailableIDs;
    /** The default time zone. */
    private static volatile DateTimeZone cDefault;
    /** A formatter for printing and parsing zones. */
    private static DateTimeFormatter cOffsetFormatter;

    /** Cache that maps fixed offset strings to softly referenced DateTimeZones */
    private static Map<String, SoftReference<DateTimeZone>> iFixedOffsetCache;

    /** Cache of old zone IDs to new zone IDs */
    private static Map<String, String> cZoneIdConversion;

    static {
        setProvider0(null);
        setNameProvider0(null);
    }

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
        DateTimeZone zone = cDefault;
        if (zone == null) {
            synchronized(DateTimeZone.class) {
                zone = cDefault;
                if (zone == null) {
                    DateTimeZone temp = null;
                    try {
                        try {
                            String id = System.getProperty("user.timezone");
                            if (id != null) {  // null check avoids stack overflow
                                temp = forID(id);
                            }
                        } catch (RuntimeException ex) {
                            // ignored
                        }
                        if (temp == null) {
                            temp = forTimeZone(TimeZone.getDefault());
                        }
                    } catch (IllegalArgumentException ex) {
                        // ignored
                    }
                    if (temp == null) {
                        temp = UTC;
                    }
                    cDefault = zone = temp;
                }
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
        synchronized(DateTimeZone.class) {
            cDefault = zone;
        }
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
        DateTimeZone zone = cProvider.getZone(id);
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
     * @param hoursOffset  the offset in hours from UTC
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
     * The minutes value is always positive and in the range 0 to 59.
     * If constructed with the values (-2, 30), the resulting zone is '-02:30'.
     * 
     * @param hoursOffset  the offset in hours from UTC
     * @param minutesOffset  the offset in minutes from UTC, must be between 0 and 59 inclusive
     * @return the DateTimeZone object for the offset
     * @throws IllegalArgumentException if the offset or minute is too large or too small
     */
    public static DateTimeZone forOffsetHoursMinutes(int hoursOffset, int minutesOffset) throws IllegalArgumentException {
        if (hoursOffset == 0 && minutesOffset == 0) {
            return DateTimeZone.UTC;
        }
        if (minutesOffset < 0 || minutesOffset > 59) {
            throw new IllegalArgumentException("Minutes out of range: " + minutesOffset);
        }
        int offset = 0;
        try {
            int hoursInMinutes = FieldUtils.safeMultiply(hoursOffset, 60);
            if (hoursInMinutes < 0) {
                minutesOffset = FieldUtils.safeAdd(hoursInMinutes, -minutesOffset);
            } else {
                minutesOffset = FieldUtils.safeAdd(hoursInMinutes, minutesOffset);
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
     * @param millisOffset  the offset in millis from UTC
     * @return the DateTimeZone object for the offset
     */
    public static DateTimeZone forOffsetMillis(int millisOffset) {
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
        if (id.equals("UTC")) {
            return DateTimeZone.UTC;
        }

        // Convert from old alias before consulting provider since they may differ.
        DateTimeZone dtz = null;
        String convId = getConvertedId(id);
        if (convId != null) {
            dtz = cProvider.getZone(convId);
        }
        if (dtz == null) {
            dtz = cProvider.getZone(id);
        }
        if (dtz != null) {
            return dtz;
        }

        // Support GMT+/-hh:mm formats
        if (convId == null) {
            convId = zone.getDisplayName();
            if (convId.startsWith("GMT+") || convId.startsWith("GMT-")) {
                convId = convId.substring(3);
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

    //-----------------------------------------------------------------------
    /**
     * Gets the zone using a fixed offset amount.
     * 
     * @param id  the zone id
     * @param offset  the offset in millis
     * @return the zone
     */
    private static synchronized DateTimeZone fixedOffsetZone(String id, int offset) {
        if (offset == 0) {
            return DateTimeZone.UTC;
        }
        if (iFixedOffsetCache == null) {
            iFixedOffsetCache = new HashMap<String, SoftReference<DateTimeZone>>();
        }
        DateTimeZone zone;
        Reference<DateTimeZone> ref = iFixedOffsetCache.get(id);
        if (ref != null) {
            zone = ref.get();
            if (zone != null) {
                return zone;
            }
        }
        zone = new FixedDateTimeZone(id, null, offset, offset);
        iFixedOffsetCache.put(id, new SoftReference<DateTimeZone>(zone));
        return zone;
    }

    /**
     * Gets all the available IDs supported.
     * 
     * @return an unmodifiable Set of String IDs
     */
    public static Set<String> getAvailableIDs() {
        return cAvailableIDs;
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
        return cProvider;
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
        setProvider0(provider);
    }

    /**
     * Sets the zone provider factory without performing the security check.
     * 
     * @param provider  provider to use, or null for default
     * @throws IllegalArgumentException if the provider is invalid
     */
    private static void setProvider0(Provider provider) {
        if (provider == null) {
            provider = getDefaultProvider();
        }
        Set<String> ids = provider.getAvailableIDs();
        if (ids == null || ids.size() == 0) {
            throw new IllegalArgumentException
                ("The provider doesn't have any available ids");
        }
        if (!ids.contains("UTC")) {
            throw new IllegalArgumentException("The provider doesn't support UTC");
        }
        if (!UTC.equals(provider.getZone("UTC"))) {
            throw new IllegalArgumentException("Invalid UTC zone provided");
        }
        cProvider = provider;
        cAvailableIDs = ids;
    }

    /**
     * Gets the default zone provider.
     * <p>
     * Tries the system property <code>org.joda.time.DateTimeZone.Provider</code>.
     * Then tries a <code>ZoneInfoProvider</code> using the data in <code>org/joda/time/tz/data</code>.
     * Then uses <code>UTCProvider</code>.
     * 
     * @return the default name provider
     */
    private static Provider getDefaultProvider() {
        Provider provider = null;

        try {
            String providerClass =
                System.getProperty("org.joda.time.DateTimeZone.Provider");
            if (providerClass != null) {
                try {
                    provider = (Provider) Class.forName(providerClass).newInstance();
                } catch (Exception ex) {
                    Thread thread = Thread.currentThread();
                    thread.getThreadGroup().uncaughtException(thread, ex);
                }
            }
        } catch (SecurityException ex) {
            // ignored
        }

        if (provider == null) {
            try {
                provider = new ZoneInfoProvider("org/joda/time/tz/data");
            } catch (Exception ex) {
                Thread thread = Thread.currentThread();
                thread.getThreadGroup().uncaughtException(thread, ex);
            }
        }

        if (provider == null) {
            provider = new UTCProvider();
        }

        return provider;
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
        return cNameProvider;
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
        setNameProvider0(nameProvider);
    }

    /**
     * Sets the name provider factory without performing the security check.
     * 
     * @param nameProvider  provider to use, or null for default
     * @throws IllegalArgumentException if the provider is invalid
     */
    private static void setNameProvider0(NameProvider nameProvider) {
        if (nameProvider == null) {
            nameProvider = getDefaultNameProvider();
        }
        cNameProvider = nameProvider;
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
                    nameProvider = (NameProvider) Class.forName(providerClass).newInstance();
                } catch (Exception ex) {
                    Thread thread = Thread.currentThread();
                    thread.getThreadGroup().uncaughtException(thread, ex);
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
    private static synchronized String getConvertedId(String id) {
        Map<String, String> map = cZoneIdConversion;
        if (map == null) {
            // Backwards compatibility with TimeZone.
            map = new HashMap<String, String>();
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
            cZoneIdConversion = map;
        }
        return map.get(id);
    }

    private static int parseOffset(String str) {
        // Can't use a real chronology if called during class
        // initialization. Offset parser doesn't need it anyhow.
        Chronology chrono = new BaseChronology() {
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
        return -(int) offsetFormatter().withChronology(chrono).parseMillis(str);
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

    /**
     * Gets a printer/parser for managing the offset id formatting.
     * 
     * @return the formatter
     */
    private static synchronized DateTimeFormatter offsetFormatter() {
        if (cOffsetFormatter == null) {
            cOffsetFormatter = new DateTimeFormatterBuilder()
                .appendTimeZoneOffset(null, true, 2, 4)
                .toFormatter();
        }
        return cOffsetFormatter;
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
        String name = cNameProvider.getShortName(locale, iID, nameKey);
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
        String name = cNameProvider.getName(locale, iID, nameKey);
        if (name != null) {
            return name;
        }
        return printOffset(getOffset(instant));
    }

    /**
     * Gets the millisecond offset to add to UTC to get local time.
     * 
     * @param instant  milliseconds from 1970-01-01T00:00:00Z to get the offset for
     * @return the millisecond offset to add to UTC to get local time
     */
    public abstract int getOffset(long instant);

    /**
     * Gets the millisecond offset to add to UTC to get local time.
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
     * Gets the standard millisecond offset to add to UTC to get local time,
     * when standard time is in effect.
     * 
     * @param instant  milliseconds from 1970-01-01T00:00:00Z to get the offset for
     * @return the millisecond offset to add to UTC to get local time
     */
    public abstract int getStandardOffset(long instant);

    /**
     * Checks whether, at a particular instant, the offset is standard or not.
     * <p>
     * This method can be used to determine whether Summer Time (DST) applies.
     * As a general rule, if the offset at the specified instant is standard,
     * then either Winter time applies, or there is no Summer Time. If the
     * instant is not standard, then Summer Time applies.
     * <p>
     * The implementation of the method is simply whether {@link #getOffset(long)}
     * equals {@link #getStandardOffset(long)} at the specified instant.
     * 
     * @param instant  milliseconds from 1970-01-01T00:00:00Z to get the offset for
     * @return true if the offset at the given instant is the standard offset
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
     * This method will return an offset suitable for calculating an instant
     * after any DST gap. For example, consider a zone with a cutover
     * from 01:00 to 01:59:<br />
     * Input: 00:00  Output: 00:00<br />
     * Input: 00:30  Output: 00:30<br />
     * Input: 01:00  Output: 02:00<br />
     * Input: 01:30  Output: 02:30<br />
     * Input: 02:00  Output: 02:00<br />
     * Input: 02:30  Output: 02:30<br />
     * <p>
     * During a DST overlap (where the local time is ambiguous) this method will return
     * the earlier instant. The combination of these two rules is to always favour
     * daylight (summer) time over standard (winter) time.
     * <p>
     * NOTE: Prior to v2.0, the DST overlap behaviour was not defined and varied by hemisphere.
     * Prior to v1.5, the DST gap behaviour was also not defined.
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
                long nextAdjusted = nextTransition(instantLocal - offsetAdjusted);
                if (nextLocal != nextAdjusted) {
                    return offsetLocal;
                }
            }
        } else if (offsetLocal > 0) {
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
     * Converts a standard UTC instant to a local instant with the same
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
     * Converts a local instant to a standard UTC instant with the same
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
     * Converts a local instant to a standard UTC instant with the same
     * local time. This conversion is used after performing a calculation
     * where the calculation was done using a simple local zone.
     *
     * @param instantLocal  the local instant to convert to UTC
     * @param strict  whether the conversion should reject non-existent local times
     * @return the UTC instant with the same local time, 
     * @throws ArithmeticException if the result overflows a long
     * @throws IllegalArgumentException if the zone has no equivalent local time
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
                        throw new IllegalArgumentException("Illegal instant due to time zone offset transition: " +
                                DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS").print(new Instant(instantLocal)) +
                                " (" + getID() + ")");
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

//    //-----------------------------------------------------------------------
//    /**
//     * Checks if the given {@link LocalDateTime} is within an overlap.
//     * <p>
//     * When switching from Daylight Savings Time to standard time there is
//     * typically an overlap where the same clock hour occurs twice. This
//     * method identifies whether the local datetime refers to such an overlap.
//     * 
//     * @param localDateTime  the time to check, not null
//     * @return true if the given datetime refers to an overlap
//     */
//    public boolean isLocalDateTimeOverlap(LocalDateTime localDateTime) {
//        if (isFixed()) {
//            return false;
//        }
//        long instantLocal = localDateTime.toDateTime(DateTimeZone.UTC).getMillis();
//        // get the offset at instantLocal (first estimate)
//        int offsetLocal = getOffset(instantLocal);
//        // adjust instantLocal using the estimate and recalc the offset
//        int offset = getOffset(instantLocal - offsetLocal);
//        // if the offsets differ, we must be near a DST boundary
//        if (offsetLocal != offset) {
//            long nextLocal = nextTransition(instantLocal - offsetLocal);
//            long nextAdjusted = nextTransition(instantLocal - offset);
//            if (nextLocal != nextAdjusted) {
//                // in DST gap
//                return false;
//            }
//            long diff = Math.abs(offset - offsetLocal);
//            DateTime dateTime = localDateTime.toDateTime(this);
//            DateTime adjusted = dateTime.plus(diff);
//            if (dateTime.getHourOfDay() == adjusted.getHourOfDay() &&
//                    dateTime.getMinuteOfHour() == adjusted.getMinuteOfHour() &&
//                    dateTime.getSecondOfMinute() == adjusted.getSecondOfMinute()) {
//                return true;
//            }
//            adjusted = dateTime.minus(diff);
//            if (dateTime.getHourOfDay() == adjusted.getHourOfDay() &&
//                    dateTime.getMinuteOfHour() == adjusted.getMinuteOfHour() &&
//                    dateTime.getSecondOfMinute() == adjusted.getSecondOfMinute()) {
//                return true;
//            }
//            return false;
//        }
//        return false;
//    }
//        
//        
//        DateTime dateTime = null;
//        try {
//            dateTime = localDateTime.toDateTime(this);
//        } catch (IllegalArgumentException ex) {
//            return false;  // it is a gap, not an overlap
//        }
//        long offset1 = Math.abs(getOffset(dateTime.getMillis() + 1) - getStandardOffset(dateTime.getMillis() + 1));
//        long offset2 = Math.abs(getOffset(dateTime.getMillis() - 1) - getStandardOffset(dateTime.getMillis() - 1));
//        long offset = Math.max(offset1, offset2);
//        if (offset == 0) {
//            return false;
//        }
//        DateTime adjusted = dateTime.plus(offset);
//        if (dateTime.getHourOfDay() == adjusted.getHourOfDay() &&
//                dateTime.getMinuteOfHour() == adjusted.getMinuteOfHour() &&
//                dateTime.getSecondOfMinute() == adjusted.getSecondOfMinute()) {
//            return true;
//        }
//        adjusted = dateTime.minus(offset);
//        if (dateTime.getHourOfDay() == adjusted.getHourOfDay() &&
//                dateTime.getMinuteOfHour() == adjusted.getMinuteOfHour() &&
//                dateTime.getSecondOfMinute() == adjusted.getSecondOfMinute()) {
//            return true;
//        }
//        return false;
        
//        long millis = dateTime.getMillis();
//        long nextTransition = nextTransition(millis);
//        long previousTransition = previousTransition(millis);
//        long deltaToPreviousTransition = millis - previousTransition;
//        long deltaToNextTransition = nextTransition - millis;
//        if (deltaToNextTransition < deltaToPreviousTransition) {
//            int offset = getOffset(nextTransition);
//            int standardOffset = getStandardOffset(nextTransition);
//            if (Math.abs(offset - standardOffset) >= deltaToNextTransition) {
//                return true;
//            }
//        } else  {
//            int offset = getOffset(previousTransition);
//            int standardOffset = getStandardOffset(previousTransition);
//            if (Math.abs(offset - standardOffset) >= deltaToPreviousTransition) {
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * Checks if the given {@link LocalDateTime} is within a gap.
     * <p>
     * When switching from standard time to Daylight Savings Time there is
     * typically a gap where a clock hour is missing. This method identifies
     * whether the local datetime refers to such a gap.
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
        } catch (IllegalArgumentException ex) {
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
        long before = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
        long after = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
        if (before == after) {
            return instant;
        }
        long local = convertUTCToLocal(instant);
        return convertLocalToUTC(local, false, earlierOrLater ? after : before);
    }

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
     * Gets a hash code compatable with equals.
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

}
