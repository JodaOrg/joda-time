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
 * Implements the Ethiopic calendar system, which defines every fourth year as
 * leap, much like the Julian calendar. The year is broken down into 12 months,
 * each 30 days in length. An extra period at the end of the year is either 5
 * or 6 days in length. In this implementation, it is considered a 13th month.
 * <p>
 * Year 1 in the Ethiopic calendar began on August 29, 8 CE (Julian), thus
 * Ethiopic years do not begin at the same time as Julian years. This chronology
 * is not proleptic, as it does not allow dates before the first Ethiopic year.
 * <p>
 * This implementation defines a day as midnight to midnight exactly as per
 * the ISO chronology. Some references indicate that a coptic day starts at
 * sunset on the previous ISO day, but this has not been confirmed and is not
 * implemented.
 * <p>
 * EthiopicChronology is thread-safe and immutable.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Ethiopian_calendar">Wikipedia</a>
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.2
 */
public final class EthiopicChronology extends BasicFixedMonthChronology {

    /** Serialization lock */
    private static final long serialVersionUID = -5972804258688333942L;

    /**
     * Constant value for 'Ethiopean Era', equivalent
     * to the value returned for AD/CE.
     */
    public static final int EE = DateTimeConstants.CE;

    /** A singleton era field. */
    private static final DateTimeField ERA_FIELD = new BasicSingleEraDateTimeField("EE");

    /** The lowest year that can be fully supported. */
    private static final int MIN_YEAR = -292269337;

    /** The highest year that can be fully supported. */
    private static final int MAX_YEAR = 292272984;

    /** Cache of zone to chronology arrays */
    private static final ConcurrentHashMap<DateTimeZone, EthiopicChronology[]> cCache = new ConcurrentHashMap<DateTimeZone, EthiopicChronology[]>();

    /** Singleton instance of a UTC EthiopicChronology */
    private static final EthiopicChronology INSTANCE_UTC;
    static {
        // init after static fields
        INSTANCE_UTC = getInstance(DateTimeZone.UTC);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets an instance of the EthiopicChronology.
     * The time zone of the returned instance is UTC.
     * 
     * @return a singleton UTC instance of the chronology
     */
    public static EthiopicChronology getInstanceUTC() {
        return INSTANCE_UTC;
    }

    /**
     * Gets an instance of the EthiopicChronology in the default time zone.
     * 
     * @return a chronology in the default time zone
     */
    public static EthiopicChronology getInstance() {
        return getInstance(DateTimeZone.getDefault(), 4);
    }

    /**
     * Gets an instance of the EthiopicChronology in the given time zone.
     * 
     * @param zone  the time zone to get the chronology in, null is default
     * @return a chronology in the specified time zone
     */
    public static EthiopicChronology getInstance(DateTimeZone zone) {
        return getInstance(zone, 4);
    }

    /**
     * Gets an instance of the EthiopicChronology in the given time zone.
     * 
     * @param zone  the time zone to get the chronology in, null is default
     * @param minDaysInFirstWeek  minimum number of days in first week of the year; default is 4
     * @return a chronology in the specified time zone
     */
    public static EthiopicChronology getInstance(DateTimeZone zone, int minDaysInFirstWeek) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        EthiopicChronology chrono;
        EthiopicChronology[] chronos = cCache.get(zone);
        if (chronos == null) {
            chronos = new EthiopicChronology[7];
            EthiopicChronology[] oldChronos = cCache.putIfAbsent(zone, chronos);
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
                        chrono = new EthiopicChronology(null, null, minDaysInFirstWeek);
                        // Impose lower limit and make another EthiopicChronology.
                        DateTime lowerLimit = new DateTime(1, 1, 1, 0, 0, 0, 0, chrono);
                        chrono = new EthiopicChronology
                            (LimitChronology.getInstance(chrono, lowerLimit, null),
                             null, minDaysInFirstWeek);
                    } else {
                        chrono = getInstance(DateTimeZone.UTC, minDaysInFirstWeek);
                        chrono = new EthiopicChronology
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
    EthiopicChronology(Chronology base, Object param, int minDaysInFirstWeek) {
        super(base, param, minDaysInFirstWeek);
    }

    /**
     * Serialization singleton.
     */
    private Object readResolve() {
        Chronology base = getBase();
        return base == null ?
                getInstance(DateTimeZone.UTC, getMinimumDaysInFirstWeek()) :
                    getInstance(base.getZone(), getMinimumDaysInFirstWeek());
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
        // Java epoch is 1970-01-01 Gregorian which is 1962-04-23 Ethiopic.
        // Calculate relative to the nearest leap year and account for the
        // difference later.

        int relativeYear = year - 1963;
        int leapYears;
        if (relativeYear <= 0) {
            // Add 3 before shifting right since /4 and >>2 behave differently
            // on negative numbers.
            leapYears = (relativeYear + 3) >> 2;
        } else {
            leapYears = relativeYear >> 2;
            // For post 1963 an adjustment is needed as jan1st is before leap day
            if (!isLeapYear(year)) {
                leapYears++;
            }
        }
        
        long millis = (relativeYear * 365L + leapYears)
            * (long)DateTimeConstants.MILLIS_PER_DAY;

        // Adjust to account for difference between 1963-01-01 and 1962-04-23.

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
        return (1962L * MILLIS_PER_YEAR + 112L * DateTimeConstants.MILLIS_PER_DAY) / 2;
    }

    //-----------------------------------------------------------------------
    @Override
    protected void assemble(Fields fields) {
        if (getBase() == null) {
            super.assemble(fields);

            // Ethiopic, like Julian, has no year zero.
            fields.year = new SkipDateTimeField(this, fields.year);
            fields.weekyear = new SkipDateTimeField(this, fields.weekyear);
            
            fields.era = ERA_FIELD;
            fields.monthOfYear = new BasicMonthOfYearDateTimeField(this, 13);
            fields.months = fields.monthOfYear.getDurationField();
        }
    }

}
