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
package org.joda.time.chrono.gj;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.ReadableInstant;

/**
 * GJChronology provides access to the individual date time fields 
 * for the Gregorian/Julian defined chronological calendar system.
 * <p>
 * The Gregorian calendar replaced the Julian calendar, and the point in time
 * when this chronology switches can be controlled using the second parameter
 * of the getInstance method. By default this cutover is set to the date the
 * Gregorian calendar was first instituted, October 15, 1582.
 * <p>
 * Before this date, this chronology uses the proleptic Julian calendar
 * (proleptic means extending indefinitely). The Julian calendar has leap 
 * years every four years, whereas the Gregorian has special rules for 100 
 * and 400 years. A meaningful result will thus be obtained for all input 
 * values. However before March 1, 4 CE, Julian leap years were irregular,
 * and before 45 BCE there was no Julian calendar.
 * <p>
 * This chronology differs from {@link java.util.GregorianCalendar
 * java.util.GregorianCalendar} in that years in BCE are returned
 * correctly. Thus year 1 BCE is returned as -1 instead of 1. The yearOfEra
 * field produces results compatible with GregorianCalendar.
 * <p>
 * The Julian calendar does not have a year zero, and so year -1 is followed by
 * year 1. If the Gregorian cutover date is specified at or before year -1
 * (Julian), year zero is defined. In other words, the proleptic Gregorian
 * chronology implemented by this class has a year zero.
 * <p>
 * A pure proleptic Gregorian chronology is obtained by specifying a cutover of
 * Long.MIN_VALUE. Likewise, a pure Julian chronology is obtained with a
 * cutover of Long.MAX_VALUE.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public abstract class GJChronology extends Chronology {
    /**
     * The default GregorianJulian cutover point
     */
    static final long DEFAULT_CUTOVER = -12219292800000L;

    // Cache that maps DateTimeZones to Factory instances.
    private static HashMap cZonesToFactories = new HashMap();

    transient DateTimeField iYearField;
    transient DateTimeField iYearOfEraField;
    transient DateTimeField iYearOfCenturyField;
    transient DateTimeField iCenturyOfEraField;
    transient DateTimeField iEraField;
    transient DateTimeField iDayOfWeekField;
    transient DateTimeField iDayOfMonthField;
    transient DateTimeField iDayOfYearField;
    transient DateTimeField iMonthOfYearField;
    transient DateTimeField iWeekOfWeekyearField;
    transient DateTimeField iWeekyearField;

    transient DateTimeField iMillisOfSecondField;
    transient DateTimeField iMillisOfDayField;
    transient DateTimeField iSecondOfMinuteField;
    transient DateTimeField iSecondOfDayField;
    transient DateTimeField iMinuteOfHourField;
    transient DateTimeField iMinuteOfDayField;
    transient DateTimeField iHourOfDayField;
    transient DateTimeField iHourOfHalfdayField;
    transient DateTimeField iClockhourOfDayField;
    transient DateTimeField iClockhourOfHalfdayField;
    transient DateTimeField iHalfdayOfDayField;

    /**
     * Factory method returns instances of the default GJ chronology. This uses
     * a cutover date of October 15, 1582 (Gregorian) 00:00:00 UTC. For this
     * value, October 4, 1582 (Julian) is followed by October 15, 1582
     * (Gregorian).
     *
     * <p>The first day of the week is designated to be
     * {@link DateTimeConstants#MONDAY Monday}, and the minimum days in the
     * first week of the year is 4.
     *
     * <p>The time zone of the returned instance is UTC.
     */
    public static GJChronology getInstanceUTC() {
        return getInstance(DateTimeZone.UTC, DEFAULT_CUTOVER, false);
    }

    /**
     * Factory method returns instances of the default GJ chronology. This uses
     * a cutover date of October 15, 1582 (Gregorian) 00:00:00 UTC. For this
     * value, October 4, 1582 (Julian) is followed by October 15, 1582
     * (Gregorian).
     *
     * <p>The first day of the week is designated to be
     * {@link DateTimeConstants#MONDAY Monday}, and the minimum days in the
     * first week of the year is 4.
     *
     * <p>The returned chronology is in the default time zone.
     */
    public static GJChronology getInstance() {
        return getInstance(DateTimeZone.getDefault(), DEFAULT_CUTOVER, false);
    }

    /**
     * Factory method returns instances of the default GJ chronology. This uses
     * a cutover date of October 15, 1582 (Gregorian) 00:00:00 UTC. For this
     * value, October 4, 1582 (Julian) is followed by October 15, 1582
     * (Gregorian).
     *
     * <p>The first day of the week is designated to be
     * {@link DateTimeConstants#MONDAY Monday}, and the minimum days in the
     * first week of the year is 4.
     *
     * @param zone  the time zone to use, null is default
     */
    public static GJChronology getInstance(DateTimeZone zone) {
        return getInstance(zone, DEFAULT_CUTOVER, false);
    }

    /**
     * Factory method allowing the Gregorian cutover point and year zero
     * handling to be set. If the cutover millis is Long.MIN_VALUE, a proleptic
     * Gregorian calendar is returned. If the cutover millis is Long.MAX_VALUE,
     * a proleptic Julian calendar is returned.
     *
     * <p>The first day of the week is designated to be
     * {@link DateTimeConstants#MONDAY Monday}, and the minimum days in the
     * first week of the year is 4.
     *
     * @param zone  the time zone to use, null is default
     * @param gregorianCutover  the cutover as a ReadableInstant, null means default
     * @param centuryISO  when true, century related fields follow ISO rules
     */
    public static GJChronology getInstance(DateTimeZone zone,
                                           ReadableInstant gregorianCutover,
                                           boolean centuryISO)
    {
        return getInstance(zone, gregorianCutover, centuryISO, 4);
    }
    
    /**
     * Factory method allowing the Gregorian cutover point and year zero
     * handling to be set.  If the cutover is Long.MIN_VALUE, a proleptic
     * Gregorian calendar is returned. If the cutover is Long.MAX_VALUE, a
     * proleptic Julian calendar is returned.
     *
     * <p>The first day of the week is designated to be
     * {@link DateTimeConstants#MONDAY Monday}, and the minimum days in the
     * first week of the year is 4.
     *
     * @param zone  the time zone to use, null is default
     * @param gregorianCutover  the cutover as milliseconds from 1970-01-01T00:00:00Z
     * @param centuryISO  when true, century related fields follow ISO rules
     */
    public static synchronized GJChronology getInstance(DateTimeZone zone,
                                                        long gregorianCutover,
                                                        boolean centuryISO)
    {
        return getInstance(zone, gregorianCutover, centuryISO, 4);
    }

    /**
     * Factory method allowing the Gregorian cutover point and year zero
     * handling to be set.  If the cutover is Long.MIN_VALUE, a proleptic
     * Gregorian calendar is returned. If the cutover is Long.MAX_VALUE, a
     * proleptic Julian calendar is returned.
     *
     * @param zone  the time zone to use, null is default
     * @param gregorianCutover  the cutover as a ReadableInstant, null means default
     * @param centuryISO  when true, century related fields follow ISO rules
     * @param minDaysInFirstWeek  minimum number of days in first week of the year; ISO is 4
     */
    public static synchronized GJChronology getInstance(DateTimeZone zone,
                                                        ReadableInstant gregorianCutover,
                                                        boolean centuryISO,
                                                        int minDaysInFirstWeek)
    {
        long millis;
        if (gregorianCutover != null) {
            millis = gregorianCutover.getMillis();
        } else {
            millis = DEFAULT_CUTOVER;
        }
        return getInstance(zone, millis, centuryISO, minDaysInFirstWeek);
    }

    /**
     * Factory method allowing the Gregorian cutover point and year zero
     * handling to be set.  If the cutover is Long.MIN_VALUE, a proleptic
     * Gregorian calendar is returned. If the cutover is Long.MAX_VALUE, a
     * proleptic Julian calendar is returned.
     *
     * @param zone  the time zone to use, null is default
     * @param gregorianCutover  the cutover as milliseconds from 1970-01-01T00:00:00Z
     * @param centuryISO  when true, century related fields follow ISO rules
     * @param minDaysInFirstWeek  minimum number of days in first week of the year; ISO is 4
     */
    public static synchronized GJChronology getInstance(DateTimeZone zone,
                                                        long gregorianCutover,
                                                        boolean centuryISO,
                                                        int minDaysInFirstWeek)
    {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }

        if (minDaysInFirstWeek < 1 || minDaysInFirstWeek > 7) {
            throw new IllegalArgumentException("Invalid min days in first week: " + minDaysInFirstWeek);
        }

        Factory factory;
        getFactory: {
            SoftReference ref = (SoftReference)cZonesToFactories.get(zone);
            if (ref != null) {
                factory = (Factory)ref.get();
                if (factory != null) {
                    break getFactory;
                }
            }
            factory = new Factory(zone);
            cZonesToFactories.put(zone, new SoftReference(factory));
        }

        return factory.getInstance(gregorianCutover, centuryISO, minDaysInFirstWeek);
    }

    GJChronology() {
        super();
    }

    public Chronology withDateTimeZone(DateTimeZone zone) {
        if (zone == null) {
            throw new IllegalArgumentException("The DateTimeZone must not be null");
        }
        if (zone == getDateTimeZone()) {
            return this;
        }
        if (zone == DateTimeZone.UTC) {
            return withUTC();
        }
        return getInstance(zone, getGregorianJulianCutoverMillis(),
                           isCenturyISO(), getMinimumDaysInFirstWeek());
    }

    // This method is overridden by GJZonedChronology.
    public DateTimeZone getDateTimeZone() {
        return DateTimeZone.UTC;
    }

    /**
     * Gets the cutover instant between Gregorian and Julian chronologies.
     * @return the cutover instant
     */
    public Instant getGregorianJulianCutover() {
        return new Instant(getGregorianJulianCutoverMillis());
    }
    
    /**
     * Gets the cutover millis between Gregorian and Julian chronologies.
     * @return the cutover millis
     */
    public abstract long getGregorianJulianCutoverMillis();
    
    /**
     * Returns true when century fields follow ISO rules. In ISO rules, the
     * century is simply the year divided by 100, and the year of century is
     * the remainder.
     * <p>
     * When false is returned, the first century is defined to start on year
     * one, and the year of century can range from 1 to 100.
     */
    public abstract boolean isCenturyISO();

    public abstract int getMinimumDaysInFirstWeek();
    
    // Milliseconds
    //------------------------------------------------------------

    public DateTimeField millisOfSecond() {
        return iMillisOfSecondField;
    }

    public DateTimeField millisOfDay() {
        return iMillisOfDayField;
    }

    // Seconds
    //------------------------------------------------------------

    public DateTimeField secondOfMinute() {
        return iSecondOfMinuteField;
    }

    public DateTimeField secondOfDay() {
        return iSecondOfDayField;
    }

    public DateTimeField minuteOfHour() {
        return iMinuteOfHourField;
    }

    public DateTimeField minuteOfDay() {
        return iMinuteOfDayField;
    }

    // Hours
    //------------------------------------------------------------

    public DateTimeField hourOfDay() {
        return iHourOfDayField;
    }

    public DateTimeField clockhourOfDay() {
        return iClockhourOfDayField;
    }

    public DateTimeField hourOfHalfday() {
        return iHourOfHalfdayField;
    }

    public DateTimeField clockhourOfHalfday() {
        return iClockhourOfHalfdayField;
    }

    public DateTimeField halfdayOfDay() {
        return iHalfdayOfDayField;
    }

    // Day
    //------------------------------------------------------------
    
    public DateTimeField dayOfWeek() {
        return iDayOfWeekField;
    }

    public DateTimeField dayOfMonth() {
        return iDayOfMonthField;
    }

    public DateTimeField dayOfYear() {
        return iDayOfYearField;
    }

    // Week
    //------------------------------------------------------------
    
    public DateTimeField weekOfWeekyear() {
        return iWeekOfWeekyearField;
    }

    public DateTimeField weekyear() {
        return iWeekyearField;
    }

    // Month
    //------------------------------------------------------------
    
    public DateTimeField monthOfYear() {
        return iMonthOfYearField;
    }

    // Year
    //------------------------------------------------------------
    
    public DateTimeField year() {
        return iYearField;
    }

    public DateTimeField yearOfEra() {
        return iYearOfEraField;
    }

    public DateTimeField yearOfCentury() {
        return iYearOfCenturyField;
    }

    public DateTimeField centuryOfEra() {
        return iCenturyOfEraField;
    }

    public DateTimeField era() {
        return iEraField;
    }

    /**
     * Serialize GJChronology instances using a small stub. This reduces the
     * serialized size, and deserialized instances come from the cache.
     *
     * <p>This method is intentionally package-private in order for sub-classes
     * to inherit this method.
     */
    Object writeReplace() {
        return new Stub(getDateTimeZone(),
                        getGregorianJulianCutoverMillis(),
                        isCenturyISO(),
                        getMinimumDaysInFirstWeek());
    }

    private static final class Stub implements Serializable {
        private transient DateTimeZone iZone;
        private transient long iCutover;
        private transient boolean iCenturyISO;
        private transient byte iMinDaysInFirstWeek;

        Stub(DateTimeZone zone, long gregorianCutover,
             boolean centuryISO, int minDaysInFirstWeek)
        {
            iZone = zone;
            iCutover = gregorianCutover;
            iCenturyISO = centuryISO;
            iMinDaysInFirstWeek = (byte)minDaysInFirstWeek;
        }

        Object readResolve() {
            return GJChronology.getInstance(iZone,
                                            iCutover,
                                            iCenturyISO,
                                            iMinDaysInFirstWeek);
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeBoolean(iCenturyISO);
            out.writeObject(iZone);
            out.writeLong(iCutover);
            out.writeByte(iMinDaysInFirstWeek);
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            iCenturyISO = in.readBoolean();
            iZone = (DateTimeZone)in.readObject();
            iCutover = in.readLong();
            iMinDaysInFirstWeek = in.readByte();
        }
    }

    /**
     * Creates instances for a specific time zone.
     */
    private static final class Factory {
        private final DateTimeZone iZone;

        // A simple cache that performs in O(n) time.
        private ArrayList iCache;

        Factory(DateTimeZone zone) {
            iZone = zone;
            iCache = new ArrayList();
        }

        public synchronized GJChronology getInstance(long gregorianCutover,
                                                     boolean centuryISO,
                                                     int minDaysInFirstWeek)
        {
            GJChronology chrono;
            ArrayList cache = iCache;
            
            for (int i=cache.size(); --i>=0; ) {
                chrono = (GJChronology)((SoftReference)cache.get(i)).get();
                if (chrono == null) {
                    cache.remove(i);
                    continue;
                }
                
                if (chrono.getGregorianJulianCutoverMillis() == gregorianCutover &&
                    chrono.isCenturyISO() == centuryISO &&
                    chrono.getMinimumDaysInFirstWeek() == minDaysInFirstWeek) {
                    
                    return chrono;
                }
            }

            if (gregorianCutover == Long.MAX_VALUE) {
                chrono = new JulianChronology(minDaysInFirstWeek);
            } else if (gregorianCutover == Long.MIN_VALUE) {
                chrono = new GregorianChronology(minDaysInFirstWeek);
            } else {
                JulianChronology julian_utc = (JulianChronology)GJChronology.getInstance
                    (DateTimeZone.UTC, Long.MAX_VALUE, true, minDaysInFirstWeek);
                GregorianChronology gregorian_utc = (GregorianChronology)GJChronology.getInstance
                    (DateTimeZone.UTC, Long.MIN_VALUE, true, minDaysInFirstWeek);

                chrono = new CutoverChronology(julian_utc, gregorian_utc, gregorianCutover);
            }

            if (!centuryISO) {
                chrono = new GJCenturyChronology(chrono);
            }
            
            if (iZone != DateTimeZone.UTC) {
                chrono = new GJZonedChronology(chrono, iZone);
            }

            cache.add(new SoftReference(chrono));
            return chrono;
        }
    }
}
