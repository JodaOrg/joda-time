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
package org.joda.time.chrono;

import java.util.concurrent.ConcurrentHashMap;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.field.SkipDateTimeField;

/**
 * Implements the Coptic calendar system, which defines every fourth year as
 * leap, much like the Julian calendar. The year is broken down into 12 months,
 * each 30 days in length. An extra period at the end of the year is either 5
 * or 6 days in length. In this implementation, it is considered a 13th month.
 * <p>
 * Year 1 in the Coptic calendar began on August 29, 284 CE (Julian), thus
 * Coptic years do not begin at the same time as Julian years. This chronology
 * is not proleptic, as it does not allow dates before the first Coptic year.
 * <p>
 * This implementation defines a day as midnight to midnight exactly as per
 * the ISO chronology. Some references indicate that a coptic day starts at
 * sunset on the previous ISO day, but this has not been confirmed and is not
 * implemented.
 * <p>
 * CopticChronology is thread-safe and immutable.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Coptic_calendar">Wikipedia</a>
 * @see JulianChronology
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class CopticChronology extends BasicFixedMonthChronology {

    /** Serialization lock */
    private static final long serialVersionUID = -5972804258688333942L;

    /**
     * Constant value for 'Anno Martyrum' or 'Era of the Martyrs', equivalent
     * to the value returned for AD/CE.
     */
    public static final int AM = DateTimeConstants.CE;

    /** A singleton era field. */
    private static final DateTimeField ERA_FIELD = new BasicSingleEraDateTimeField("AM");

    /** The lowest year that can be fully supported. */
    private static final int MIN_YEAR = -292269337;

    /** The highest year that can be fully supported. */
    private static final int MAX_YEAR = 292272708;

    /** Cache of zone to chronology arrays */
    private static final ConcurrentHashMap<DateTimeZone, CopticChronology[]> cCache = new ConcurrentHashMap<DateTimeZone, CopticChronology[]>();

    /** Singleton instance of a UTC CopticChronology */
    private static final CopticChronology INSTANCE_UTC;
    static {
        // init after static fields
        INSTANCE_UTC = getInstance(DateTimeZone.UTC);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets an instance of the CopticChronology.
     * The time zone of the returned instance is UTC.
     * 
     * @return a singleton UTC instance of the chronology
     */
    public static CopticChronology getInstanceUTC() {
        return INSTANCE_UTC;
    }

    /**
     * Gets an instance of the CopticChronology in the default time zone.
     * 
     * @return a chronology in the default time zone
     */
    public static CopticChronology getInstance() {
        return getInstance(DateTimeZone.getDefault(), 4);
    }

    /**
     * Gets an instance of the CopticChronology in the given time zone.
     * 
     * @param zone  the time zone to get the chronology in, null is default
     * @return a chronology in the specified time zone
     */
    public static CopticChronology getInstance(DateTimeZone zone) {
        return getInstance(zone, 4);
    }

    /**
     * Gets an instance of the CopticChronology in the given time zone.
     * 
     * @param zone  the time zone to get the chronology in, null is default
     * @param minDaysInFirstWeek  minimum number of days in first week of the year; default is 4
     * @return a chronology in the specified time zone
     */
    public static CopticChronology getInstance(DateTimeZone zone, int minDaysInFirstWeek) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        CopticChronology chrono;
        CopticChronology[] chronos = cCache.get(zone);
        if (chronos == null) {
            chronos = new CopticChronology[7];
            CopticChronology[] oldChronos = cCache.putIfAbsent(zone, chronos);
            if (oldChronos != null) {
                chronos = oldChronos;
            }
        }
        try {
            chrono = chronos[minDaysInFirstWeek - 1];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException
                ("Invalid min days in first week: " + minDaysInFirstWeek);
        }
        if (chrono == null) {
            synchronized (chronos) {
                chrono = chronos[minDaysInFirstWeek - 1];
                if (chrono == null) {
                    if (zone == DateTimeZone.UTC) {
                        // First create without a lower limit.
                        chrono = new CopticChronology(null, null, minDaysInFirstWeek);
                        // Impose lower limit and make another CopticChronology.
                        DateTime lowerLimit = new DateTime(1, 1, 1, 0, 0, 0, 0, chrono);
                        chrono = new CopticChronology
                            (LimitChronology.getInstance(chrono, lowerLimit, null),
                             null, minDaysInFirstWeek);
                    } else {
                        chrono = getInstance(DateTimeZone.UTC, minDaysInFirstWeek);
                        chrono = new CopticChronology
                            (ZonedChronology.getInstance(chrono, zone), null, minDaysInFirstWeek);
                    }
                    chronos[minDaysInFirstWeek - 1] = chrono;
                }
            }
        }
        return chrono;
    }

    // Constructors and instance variables
    //-----------------------------------------------------------------------
    /**
     * Restricted constructor.
     */
    CopticChronology(Chronology base, Object param, int minDaysInFirstWeek) {
        super(base, param, minDaysInFirstWeek);
    }

    /**
     * Serialization singleton.
     */
    private Object readResolve() {
        Chronology base = getBase();
        int minDays = getMinimumDaysInFirstWeek();
        minDays = (minDays == 0 ? 4 : minDays);  // handle rename of BaseGJChronology
        return base == null ?
                getInstance(DateTimeZone.UTC, minDays) :
                    getInstance(base.getZone(), minDays);
    }

    // Conversion
    //-----------------------------------------------------------------------
    /**
     * Gets the Chronology in the UTC time zone.
     * 
     * @return the chronology in UTC
     */
    @Override
    public Chronology withUTC() {
        return INSTANCE_UTC;
    }

    /**
     * Gets the Chronology in a specific time zone.
     * 
     * @param zone  the zone to get the chronology in, null is default
     * @return the chronology
     */
    @Override
    public Chronology withZone(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        if (zone == getZone()) {
            return this;
        }
        return getInstance(zone);
    }

    //-----------------------------------------------------------------------
    @Override
    boolean isLeapDay(long instant) {
        return dayOfMonth().get(instant) == 6 && monthOfYear().isLeap(instant);
    }

    //-----------------------------------------------------------------------
    @Override
    long calculateFirstDayOfYearMillis(int year) {
        // Java epoch is 1970-01-01 Gregorian which is 1686-04-23 Coptic.
        // Calculate relative to the nearest leap year and account for the
        // difference later.

        int relativeYear = year - 1687;
        int leapYears;
        if (relativeYear <= 0) {
            // Add 3 before shifting right since /4 and >>2 behave differently
            // on negative numbers.
            leapYears = (relativeYear + 3) >> 2;
        } else {
            leapYears = relativeYear >> 2;
            // For post 1687 an adjustment is needed as jan1st is before leap day
            if (!isLeapYear(year)) {
                leapYears++;
            }
        }
        
        long millis = (relativeYear * 365L + leapYears)
            * (long)DateTimeConstants.MILLIS_PER_DAY;

        // Adjust to account for difference between 1687-01-01 and 1686-04-23.

        return millis + (365L - 112) * DateTimeConstants.MILLIS_PER_DAY;
    }

    //-----------------------------------------------------------------------
    @Override
    int getMinYear() {
        return MIN_YEAR;
    }

    //-----------------------------------------------------------------------
    @Override
    int getMaxYear() {
        return MAX_YEAR;
    }

    //-----------------------------------------------------------------------
    @Override
    long getApproxMillisAtEpochDividedByTwo() {
        return (1686L * MILLIS_PER_YEAR + 112L * DateTimeConstants.MILLIS_PER_DAY) / 2;
    }

    //-----------------------------------------------------------------------
    @Override
    protected void assemble(Fields fields) {
        if (getBase() == null) {
            super.assemble(fields);

            // Coptic, like Julian, has no year zero.
            fields.year = new SkipDateTimeField(this, fields.year);
            fields.weekyear = new SkipDateTimeField(this, fields.weekyear);
            
            fields.era = ERA_FIELD;
            fields.monthOfYear = new BasicMonthOfYearDateTimeField(this, 13);
            fields.months = fields.monthOfYear.getDurationField();
        }
    }

}
