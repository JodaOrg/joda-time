/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-03 Stephen Colebourne.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Joda project (http://www.joda.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "Joda" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact licence@joda.org.
 *
 * 5. Products derived from this software may not be called "Joda",
 *    nor may "Joda" appear in their name, without prior written
 *    permission of the Joda project.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE JODA AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Joda project and was originally
 * created by Stephen Colebourne <scolebourne@joda.org>. For more
 * information on the Joda project, please see <http://www.joda.org/>.
 */

package org.joda.time;

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

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.tz.DateTimeZonePermission;
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
 * Using this system, Pacific Time, USA is expressed as UTC-08:00, or UTC-07:00
 * in the summer. The offset -08:00 indicates that Pacific Time is obtained 
 * from UTC by adding -08:00, that is, by subtracting 8 hours.
 * <p>
 * The offset differs in the summer because of daylight saving time, or DST.
 * The folowing definitions of time are generally used:
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
    
    static final long serialVersionUID = 5546345482340108586L;

    /** The time zone for Universal Coordinated Time */
    public static final DateTimeZone UTC = new FixedDateTimeZone("UTC", "UTC", 0, 0);

    private static Provider cProvider;
    private static NameProvider cNameProvider;
    private static Set cAvailableIDs;

    private static DateTimeZone cDefault;

    private static DateTimeFormatter cOffsetFormatter;

    /**
     * Cache of old zone IDs to new zone IDs
     */
    private static Map cZoneIdConversion;

    static {
        setProvider0(null);
        setNameProvider0(null);

        try {
            try {
                cDefault = getInstance(System.getProperty("user.timezone"));
            } catch (RuntimeException e) {
            }
            cDefault = getInstance(java.util.TimeZone.getDefault());
        } catch (IllegalArgumentException e) {
        }

        if (cDefault == null) {
            cDefault = UTC;
        }
    }

    /**
     * Gets the default time zone.
     * 
     * @return the default datetime zone object
     */
    public static DateTimeZone getDefault() {
        return cDefault;
    }

    /**
     * Sets the default time zone.
     * 
     * @param zone  the default datetime zone object, must not be null
     * @throws IllegalArgumentException if the zone is null
     */
    public static void setDefault(DateTimeZone zone) throws SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new DateTimeZonePermission("setDefault"));
        }
        if (zone == null) {
            throw new IllegalArgumentException("The datetime zone must not be null");
        }
        cDefault = zone;
    }

    /**
     * Get the time zone by id.
     * <p>
     * The time zone id may be one of those returned by getAvailableIDs. Short
     * ids, as accepted by {@link java.util.TimeZone}, are not accepted. All
     * IDs must be specified in the long format. The exception is UTC, which is
     * an acceptable id.
     * <p>
     * Alternatively a locale independent, fixed offset, datetime zone can
     * be specified. The form <code>[+-]hh:mm</code> can be used.
     * 
     * @param id  the ID of the datetime zone
     * @return the DateTimeZone object for the ID
     * @throws IllegalArgumentException if the ID is null or not recognised
     */
    public static DateTimeZone getInstance(String id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("The datetime zone id must not be null");
        }
        if (id.equals("UTC")) {
            return DateTimeZone.UTC;
        }
        DateTimeZone zone = cProvider.getDateTimeZone(id);
        if (zone != null) {
            return zone;
        }
        if (id.startsWith("+") || id.startsWith("-")) {
            int offset = -(int)offsetFormatter().parseMillis(id);
            // Canonicalize the id.
            id = offsetFormatter().print(0, UTC, offset);
            return new FixedDateTimeZone(id, null, offset, offset);
        }
        throw new IllegalArgumentException("The datetime zone id is not recognised: " + id);
    }

    /**
     * Get the time zone by Java TimeZone.
     * <p>
     * DateTimeZone only accepts a subset of the IDs from TimeZone. The
     * excluded IDs are the short three letter form (except UTC). This 
     * method will attempt to convert between time zones created using the
     * short IDs and the full version.
     * 
     * @param zone the zone to convert
     * @return the DateTimeZone object for the zone
     * @throws IllegalArgumentException if the zone is null or not recognised
     */
    public static DateTimeZone getInstance(java.util.TimeZone zone) {
        if (zone == null) {
            throw new IllegalArgumentException("The TimeZone must not be null");
        }
        final String id = zone.getID();
        if (id.equals("UTC")) {
            return DateTimeZone.UTC;
        }

        DateTimeZone dtz;
        // Convert from old alias before consulting provider since they may differ.
        String convId = getConvertedId(id);
        if (convId == null || (dtz = cProvider.getDateTimeZone(convId)) == null) {
            dtz = cProvider.getDateTimeZone(id);
        }
        if (dtz != null) {
            return dtz;
        }

        // Support GMT+/-hh:mm formats
        if (convId == null) {
            convId = zone.getDisplayName();
            if (convId.startsWith("GMT+") || convId.startsWith("GMT-")) {
                convId = convId.substring(3);
                int offset = -(int)offsetFormatter().parseMillis(convId);
                // Canonicalize the id.
                convId = offsetFormatter().print(0, UTC, offset);
                return new FixedDateTimeZone(convId, null, offset, offset);
            }
        }

        throw new IllegalArgumentException("The datetime zone id is not recognised: " + id);
    }

    /**
     * Gets the available IDs according to the given millisecond time
     * zone offset.
     * <p>
     * For example, "America/Phoenix" and "America/Denver"
     * both have GMT-07:00, but differ in daylight saving behavior.
     * 
     * @param rawOffset  the given millisecond time zone GMT offset
     * @return an unmodifiable Set of String IDs
     */
    /*
    public static Set getAvailableIDs(int rawOffset) {
        String[] ids = TimeZone.getAvailableIDs(rawOffset);
        Set set = new HashSet();
        for (int i = 0; i < ids.length; i++) {
            if (cZones.containsKey(ids[i])) {
                set.add(ids[i]);
            }
        }
        return Collections.unmodifiableSet(set);
    }
    */

    /**
     * Gets all the available IDs supported.
     * 
     * @return an unmodifiable Set of String IDs
     */
    public static Set getAvailableIDs() {
        return cAvailableIDs;
    }

    public static Provider getProvider() {
        return cProvider;
    }

    /**
     * @param provider provider to use, or null for default
     */
    public static void setProvider(Provider provider) throws SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new DateTimeZonePermission("setProvider"));
        }
        setProvider0(provider);
    }

    /**
     * Doesn't perform a security check.
     */
    private static void setProvider0(Provider provider) {
        if (provider == null) {
            provider = getDefaultProvider();
        }
        Set ids = provider.getAvailableIDs();
        if (ids == null || ids.size() == 0) {
            throw new IllegalArgumentException
                ("The provider doesn't have any available ids");
        }
        if (!ids.contains("UTC")) {
            throw new IllegalArgumentException("The provider doesn't support UTC");
        }
        if (!UTC.equals(provider.getDateTimeZone("UTC"))) {
            throw new IllegalArgumentException("Invalid UTC zone provided");
        }
        cProvider = provider;
        cAvailableIDs = ids;
    }

    public static NameProvider getNameProvider() {
        return cNameProvider;
    }

    /**
     * @param nameProvider provider to use, or null for default
     */
    public static void setNameProvider(NameProvider nameProvider) throws SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new DateTimeZonePermission("setNameProvider"));
        }
        setNameProvider0(nameProvider);
    }

    /**
     * Doesn't perform a security check.
     */
    private static void setNameProvider0(NameProvider nameProvider) {
        if (nameProvider == null) {
            nameProvider = getDefaultNameProvider();
        }
        cNameProvider = nameProvider;
    }

    private static synchronized String getConvertedId(String id) {
        Map map = cZoneIdConversion;
        if (map == null) {
            // Backwards compatibility with TimeZone.
            map = new HashMap();
            map.put("GMT", "UTC");
            map.put("MIT", "Pacific/Apia");
            map.put("HST", "Pacific/Honolulu");
            map.put("AST", "America/Anchorage");
            map.put("PST", "America/Los_Angeles");
            map.put("MST", "America/Denver");
            map.put("PNT", "America/Phoenix");
            map.put("CST", "America/Chicago");
            map.put("EST", "America/New_York");
            map.put("IET", "America/Indianapolis");
            map.put("PRT", "America/Puerto_Rico");
            map.put("CNT", "America/St_Johns");
            map.put("AGT", "America/Buenos_Aires");
            map.put("BET", "America/Sao_Paulo");
            map.put("WET", "Europe/London");
            map.put("ECT", "Europe/Paris");
            map.put("ART", "Africa/Cairo");
            map.put("CAT", "Africa/Harare");
            map.put("EET", "Europe/Bucharest");
            map.put("EAT", "Africa/Addis_Ababa");
            map.put("MET", "Asia/Tehran");
            map.put("NET", "Asia/Yerevan");
            map.put("PLT", "Asia/Karachi");
            map.put("IST", "Asia/Calcutta");
            map.put("BST", "Asia/Dhaka");
            map.put("VST", "Asia/Saigon");
            map.put("CTT", "Asia/Shanghai");
            map.put("JST", "Asia/Tokyo");
            map.put("ACT", "Australia/Darwin");
            map.put("AET", "Australia/Sydney");
            map.put("SST", "Pacific/Guadalcanal");
            map.put("NST", "Pacific/Auckland");
            cZoneIdConversion = map;
        }
        return (String)map.get(id);
    }

    private static synchronized DateTimeFormatter offsetFormatter() {
        if (cOffsetFormatter == null) {
            cOffsetFormatter = new DateTimeFormatterBuilder(UTC)
                .appendTimeZoneOffset(null, true, 2, 4)
                .toFormatter();
        }
        return cOffsetFormatter;
    }

    private static Provider getDefaultProvider() {
        Provider provider = null;

        try {
            String providerClass =
                System.getProperty("org.joda.time.DateTimeZone.Provider");
            if (providerClass != null) {
                try {
                    provider = (Provider)Class.forName(providerClass).newInstance();
                }
                catch (Exception e) {
                    Thread t = Thread.currentThread();
                    t.getThreadGroup().uncaughtException(t, e);
                }
            }
        } catch (SecurityException e) {
        }

        if (provider == null) {
            try {
                provider = new ZoneInfoProvider("org/joda/time/tz/data");
            } catch (Exception e) {
                Thread t = Thread.currentThread();
                t.getThreadGroup().uncaughtException(t, e);
            }
        }

        if (provider == null) {
            provider = new UTCProvider();
        }

        return provider;
    }

    private static NameProvider getDefaultNameProvider() {
        NameProvider nameProvider = null;

        try {
            String providerClass =
                System.getProperty("org.joda.time.DateTimeZone.NameProvider");
            if (providerClass != null) {
                try {
                    nameProvider = (NameProvider)Class.forName(providerClass).newInstance();
                }
                catch (Exception e) {
                    Thread t = Thread.currentThread();
                    t.getThreadGroup().uncaughtException(t, e);
                }
            }
        } catch (SecurityException e) {
        }

        if (nameProvider == null) {
            nameProvider = new DefaultNameProvider();
        }

        return nameProvider;
    }

    // Instance fields and methods
    //--------------------------------------------------------------------

    private final String iID;

    public DateTimeZone(String id) {
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
    public final String getID() {
        return iID;
    }

    /**
     * Returns a non-localized name that is unique to this time zone. It can be
     * combined with id to form a unique key for fetching localized names.
     *
     * @param instant milliseconds from 1970-01-01T00:00:00Z to get the name for
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
     * @param instant milliseconds from 1970-01-01T00:00:00Z to get the name for
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
     * @param instant milliseconds from 1970-01-01T00:00:00Z to get the name for
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
        return offsetFormatter().print(instant, this);
    }

    /**
     * Gets the long name of this datetime zone suitable for display using
     * the default locale.
     * <p>
     * If the name is not available for the locale, then this method returns a
     * string in the format <code>[+-]hh:mm</code>.
     * 
     * @param instant milliseconds from 1970-01-01T00:00:00Z to get the name for
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
     * @param instant milliseconds from 1970-01-01T00:00:00Z to get the name for
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
        return offsetFormatter().print(instant, this);
    }

    /**
     * Gets the millisecond offset to add to UTC to get local time.
     * 
     * @param instant milliseconds from 1970-01-01T00:00:00Z to get the offset for
     * @return the millisecond offset to add to UTC to get local time
     */
    public abstract int getOffset(long instant);

    /**
     * Gets the millisecond offset to add to UTC to get local time.
     * 
     * @param instant instant to get the offset for
     * @return the millisecond offset to add to UTC to get local time
     * @throws IllegalArgumentException if the instant is null
     */
    public final int getOffset(ReadableInstant instant) {
        if (instant == null) {
            throw new IllegalArgumentException("The instant must not be null");
        }
        return getOffset(instant.getMillis());
    }

    /**
     * Gets the standard millisecond offset to add to UTC to get local time,
     * when standard time is in effect.
     * 
     * @param instant milliseconds from 1970-01-01T00:00:00Z to get the offset for
     * @return the millisecond offset to add to UTC to get local time
     */
    public abstract int getStandardOffset(long instant);

    /**
     * Gets the millisecond offset to subtract from local time to get UTC time.
     * This offset can be used to undo adding the offset obtained by getOffset.
     *
     * <pre>
     * millisLocal == millisUTC   + getOffset(millisUTC)
     * millisUTC   == millisLocal - getOffsetFromLocal(millisLocal)
     * </pre>
     *
     * Note: After calculating millisLocal, some error may be introduced. At
     * offset transitions (due to DST or other historical changes), ranges of
     * local times may map to different UTC times.
     *
     * @param instantLocal the millisecond instant, relative to this time zone, to
     * get the offset for
     * @return the millisceond offset to subtract from local time to get UTC time.
     */
    public int getOffsetFromLocal(long instantLocal) {
        return getOffset(instantLocal - getOffset(instantLocal));
    }

    /**
     * Returns true if this time zone has no transitions.
     *
     * @return true if no transitions
     */
    public abstract boolean isFixed();

    /**
     * Advances the given instant to where the time zone offset or name
     * changes. If the instant returned is exactly the same as passed in, then
     * no changes occur after the given instant.
     *
     * @param instant milliseconds from 1970-01-01T00:00:00Z
     * @return milliseconds from 1970-01-01T00:00:00Z
     */
    public abstract long nextTransition(long instant);

    /**
     * Retreats the given instant to where the time zone offset or name
     * changes. If the instant returned is exactly the same as passed in, then
     * no changes occur before the given instant.
     *
     * @param instant milliseconds from 1970-01-01T00:00:00Z
     * @return milliseconds from 1970-01-01T00:00:00Z
     */
    public abstract long previousTransition(long instant);

    // Basic methods
    //--------------------------------------------------------------------

    /**
     * Get the datetime zone as a {@link java.util.TimeZone}.
     * 
     * @return the equivalent TimeZone object
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
     */
    public String toString() {
        return getID();
    }

    /**
     * By default, when DateTimeZones are serialized, only a "stub" object
     * referring to the id is written out. When the stub is read in, it
     * replaces itself with a DateTimeZone object.
     */
    protected Object writeReplace() throws ObjectStreamException {
        return new Stub(iID);
    }

    /**
     * Used to serialize DateTimeZones by id.
     */
    private static final class Stub implements Serializable {
        static final long serialVersionUID = -6471952376487863581L;

        private transient String iID;

        Stub(String id) {
            iID = id;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeUTF(iID);
        }

        private void readObject(ObjectInputStream in) throws IOException {
            iID = in.readUTF();
        }

        protected Object readResolve() throws ObjectStreamException {
            return getInstance(iID);
        }
    }
}
