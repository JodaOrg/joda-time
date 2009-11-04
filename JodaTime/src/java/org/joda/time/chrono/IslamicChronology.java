/*
 *  Copyright 2001-2009 Stephen Colebourne
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;

/**
 * Implements the Islamic, or Hijri, calendar system using arithmetic rules.
 * <p>
 * This calendar is a lunar calendar with a shorter year than ISO.
 * Year 1 in the Islamic calendar began on July 16, 622 CE (Julian), thus
 * Islamic years do not begin at the same time as Julian years. This chronology
 * is not proleptic, as it does not allow dates before the first Islamic year.
 * <p>
 * There are two basic forms of the Islamic calendar, the tabular and the
 * observed. The observed form cannot easily be used by computers as it
 * relies on human observation of the new moon.
 * The tabular calendar, implemented here, is an arithmetical approximation
 * of the observed form that follows relatively simple rules.
 * <p>
 * The tabular form of the calendar defines 12 months of alternately
 * 30 and 29 days. The last month is extended to 30 days in a leap year.
 * Leap years occur according to a 30 year cycle. There are four recognised
 * patterns of leap years in the 30 year cycle:
 * <pre>
 * Years 2, 5, 7, 10, 13, 15, 18, 21, 24, 26 & 29 - 15-based, used by Microsoft
 * Years 2, 5, 7, 10, 13, 16, 18, 21, 24, 26 & 29 - 16-based, most commonly used
 * Years 2, 5, 8, 10, 13, 16, 19, 21, 24, 27 & 29 - Indian
 * Years 2, 5, 8, 11, 13, 16, 19, 21, 24, 27 & 30 - Habash al-Hasib
 * </pre>
 * You can select which pattern to use via the factory methods, or use the
 * default (16-based).
 * <p>
 * This implementation defines a day as midnight to midnight exactly as per
 * the ISO chronology. This correct start of day is at sunset on the previous
 * day, however this cannot readily be modelled and has been ignored.
 * <p>
 * IslamicChronology is thread-safe and immutable.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Islamic_calendar">Wikipedia</a>
 *
 * @author Stephen Colebourne
 * @since 1.2
 */
public final class IslamicChronology extends BasicChronology {

    /** Serialization lock */
    private static final long serialVersionUID = -3663823829888L;

    /**
     * Constant value for 'Anno Hegirae', equivalent
     * to the value returned for AD/CE.
     */
    public static final int AH = DateTimeConstants.CE;

    /** A singleton era field. */
    private static final DateTimeField ERA_FIELD = new BasicSingleEraDateTimeField("AH");

    /** Leap year 15-based pattern. */
    public static final LeapYearPatternType LEAP_YEAR_15_BASED = new LeapYearPatternType(0, 623158436);
    /** Leap year 16-based pattern. */
    public static final LeapYearPatternType LEAP_YEAR_16_BASED = new LeapYearPatternType(1, 623191204);
    /** Leap year Indian pattern. */
    public static final LeapYearPatternType LEAP_YEAR_INDIAN = new LeapYearPatternType(2, 690562340);
    /** Leap year Habash al-Hasib pattern. */
    public static final LeapYearPatternType LEAP_YEAR_HABASH_AL_HASIB = new LeapYearPatternType(3, 153692453);

    /** The lowest year that can be fully supported. */
    private static final int MIN_YEAR = -292269337;

    /**
     * The highest year that can be fully supported.
     * Although calculateFirstDayOfYearMillis can go higher without
     * overflowing, the getYear method overflows when it adds the
     * approximate millis at the epoch.
     */
    private static final int MAX_YEAR = 292271022;

    /** The days in a pair of months. */
    private static final int MONTH_PAIR_LENGTH = 59;

    /** The length of the long month. */
    private static final int LONG_MONTH_LENGTH = 30;

    /** The length of the short month. */
    private static final int SHORT_MONTH_LENGTH = 29;

    /** The length of the long month in millis. */
    private static final long MILLIS_PER_MONTH_PAIR = 59L * DateTimeConstants.MILLIS_PER_DAY;

    /** The length of the long month in millis. */
    private static final long MILLIS_PER_MONTH = (long) (29.53056 * DateTimeConstants.MILLIS_PER_DAY);

    /** The length of the long month in millis. */
    private static final long MILLIS_PER_LONG_MONTH = 30L * DateTimeConstants.MILLIS_PER_DAY;

    /** The typical millis per year. */
    private static final long MILLIS_PER_YEAR = (long) (354.36667 * DateTimeConstants.MILLIS_PER_DAY);

    /** The typical millis per year. */
    private static final long MILLIS_PER_SHORT_YEAR = 354L * DateTimeConstants.MILLIS_PER_DAY;

    /** The typical millis per year. */
    private static final long MILLIS_PER_LONG_YEAR = 355L * DateTimeConstants.MILLIS_PER_DAY;

    /** The millis of 0001-01-01. */
    private static final long MILLIS_YEAR_1 = -42521587200000L;
                                    //        -42520809600000L;
//    long start = 0L - 278L * DateTimeConstants.MILLIS_PER_DAY;
//    long cy = 46L * MILLIS_PER_CYCLE;  // 1381-01-01
//    long rem = 5L * MILLIS_PER_SHORT_YEAR +
//            3L * MILLIS_PER_LONG_YEAR;  // 1389-01-01

    /** The length of the cycle of leap years. */
    private static final int CYCLE = 30;

    /** The millis of a 30 year cycle. */
    private static final long MILLIS_PER_CYCLE = ((19L * 354L + 11L * 355L) * DateTimeConstants.MILLIS_PER_DAY);

    /** Cache of zone to chronology arrays */
    private static final Map<DateTimeZone, IslamicChronology[]> cCache = new HashMap<DateTimeZone, IslamicChronology[]>();

    /** Singleton instance of a UTC IslamicChronology */
    private static final IslamicChronology INSTANCE_UTC;
    static {
        // init after static fields
        INSTANCE_UTC = getInstance(DateTimeZone.UTC);
    }

    /** The leap years to use. */
    private final LeapYearPatternType iLeapYears;

    //-----------------------------------------------------------------------
    /**
     * Gets an instance of the IslamicChronology.
     * The time zone of the returned instance is UTC.
     * 
     * @return a singleton UTC instance of the chronology
     */
    public static IslamicChronology getInstanceUTC() {
        return INSTANCE_UTC;
    }

    /**
     * Gets an instance of the IslamicChronology in the default time zone.
     * 
     * @return a chronology in the default time zone
     */
    public static IslamicChronology getInstance() {
        return getInstance(DateTimeZone.getDefault(), LEAP_YEAR_16_BASED);
    }

    /**
     * Gets an instance of the IslamicChronology in the given time zone.
     * 
     * @param zone  the time zone to get the chronology in, null is default
     * @return a chronology in the specified time zone
     */
    public static IslamicChronology getInstance(DateTimeZone zone) {
        return getInstance(zone, LEAP_YEAR_16_BASED);
    }

    /**
     * Gets an instance of the IslamicChronology in the given time zone.
     * 
     * @param zone  the time zone to get the chronology in, null is default
     * @param leapYears  the type defining the leap year pattern
     * @return a chronology in the specified time zone
     */
    public static IslamicChronology getInstance(DateTimeZone zone, LeapYearPatternType leapYears) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        IslamicChronology chrono;
        synchronized (cCache) {
            IslamicChronology[] chronos = cCache.get(zone);
            if (chronos == null) {
                chronos = new IslamicChronology[4];
                cCache.put(zone, chronos);
            }
            chrono = chronos[leapYears.index];
            if (chrono == null) {
                if (zone == DateTimeZone.UTC) {
                    // First create without a lower limit.
                    chrono = new IslamicChronology(null, null, leapYears);
                    // Impose lower limit and make another IslamicChronology.
                    DateTime lowerLimit = new DateTime(1, 1, 1, 0, 0, 0, 0, chrono);
                    chrono = new IslamicChronology(
                        LimitChronology.getInstance(chrono, lowerLimit, null),
                         null, leapYears);
                } else {
                    chrono = getInstance(DateTimeZone.UTC, leapYears);
                    chrono = new IslamicChronology
                        (ZonedChronology.getInstance(chrono, zone), null, leapYears);
                }
                chronos[leapYears.index] = chrono;
            }
        }
        return chrono;
    }

    // Constructors and instance variables
    //-----------------------------------------------------------------------
    /**
     * Restricted constructor.
     */
    IslamicChronology(Chronology base, Object param, LeapYearPatternType leapYears) {
        super(base, param, 4);
        this.iLeapYears = leapYears;
    }

    /**
     * Serialization singleton.
     */
    private Object readResolve() {
        Chronology base = getBase();
        return base == null ? getInstanceUTC() : getInstance(base.getZone());
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the leap year pattern type.
     *
     * @return the pattern type
     */
    public LeapYearPatternType getLeapYearPatternType() {
        return iLeapYears;
    }

    // Conversion
    //-----------------------------------------------------------------------
    /**
     * Gets the Chronology in the UTC time zone.
     * 
     * @return the chronology in UTC
     */
    public Chronology withUTC() {
        return INSTANCE_UTC;
    }

    /**
     * Gets the Chronology in a specific time zone.
     * 
     * @param zone  the zone to get the chronology in, null is default
     * @return the chronology
     */
    public Chronology withZone(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        if (zone == getZone()) {
            return this;
        }
        return getInstance(zone);
    }

    /**
     * A suitable hash code for the chronology.
     * 
     * @return the hash code
     * @since 1.6
     */
    public int hashCode() {
        return super.hashCode() * 13 + getLeapYearPatternType().hashCode();
    }

    //-----------------------------------------------------------------------
    int getYear(long instant) {
        long millisIslamic = instant - MILLIS_YEAR_1;
        long cycles = millisIslamic / MILLIS_PER_CYCLE;
        long cycleRemainder = millisIslamic % MILLIS_PER_CYCLE;
        
        int year = (int) ((cycles * CYCLE) + 1L);
        long yearMillis = (isLeapYear(year) ? MILLIS_PER_LONG_YEAR : MILLIS_PER_SHORT_YEAR);
        while (cycleRemainder >= yearMillis) {
            cycleRemainder -= yearMillis;
            yearMillis = (isLeapYear(++year) ? MILLIS_PER_LONG_YEAR : MILLIS_PER_SHORT_YEAR);
        }
        return year;
    }

    long setYear(long instant, int year) {
        // optimsed implementation of set, due to fixed months
        int thisYear = getYear(instant);
        int dayOfYear = getDayOfYear(instant, thisYear);
        int millisOfDay = getMillisOfDay(instant);

        if (dayOfYear > 354) {
            // Current year is leap, and day is leap.
            if (!isLeapYear(year)) {
                // Moving to a non-leap year, leap day doesn't exist.
                dayOfYear--;
            }
        }

        instant = getYearMonthDayMillis(year, 1, dayOfYear);
        instant += millisOfDay;
        return instant;
    }

    //-----------------------------------------------------------------------
    long getYearDifference(long minuendInstant, long subtrahendInstant) {
        // optimsed implementation of getDifference, due to fixed months
        int minuendYear = getYear(minuendInstant);
        int subtrahendYear = getYear(subtrahendInstant);

        // Inlined remainder method to avoid duplicate calls to get.
        long minuendRem = minuendInstant - getYearMillis(minuendYear);
        long subtrahendRem = subtrahendInstant - getYearMillis(subtrahendYear);

        int difference = minuendYear - subtrahendYear;
        if (minuendRem < subtrahendRem) {
            difference--;
        }
        return difference;
    }

    //-----------------------------------------------------------------------
    long getTotalMillisByYearMonth(int year, int month) {
        if (--month % 2 == 1) {
            month /= 2;
            return month * MILLIS_PER_MONTH_PAIR + MILLIS_PER_LONG_MONTH;
        } else {
            month /= 2;
            return month * MILLIS_PER_MONTH_PAIR;
        }
    }

    //-----------------------------------------------------------------------
    int getDayOfMonth(long millis) {
        // optimised for simple months
        int doy = getDayOfYear(millis) - 1;
        if (doy == 354) {
            return 30;
        }
        return (doy % MONTH_PAIR_LENGTH) % LONG_MONTH_LENGTH + 1;
    }

    //-----------------------------------------------------------------------
    boolean isLeapYear(int year) {
        return iLeapYears.isLeapYear(year);
    }

    //-----------------------------------------------------------------------
    int getDaysInYearMax() {
        return 355;
    }

    //-----------------------------------------------------------------------
    int getDaysInYear(int year) {
        return isLeapYear(year) ? 355 : 354;
    }

    //-----------------------------------------------------------------------
    int getDaysInYearMonth(int year, int month) {
        if (month == 12 && isLeapYear(year)) {
            return LONG_MONTH_LENGTH;
        }
        return (--month % 2 == 0 ? LONG_MONTH_LENGTH : SHORT_MONTH_LENGTH);
    }

    //-----------------------------------------------------------------------
    int getDaysInMonthMax() {
        return LONG_MONTH_LENGTH;
    }

    //-----------------------------------------------------------------------
    int getDaysInMonthMax(int month) {
        if (month == 12) {
            return LONG_MONTH_LENGTH;
        }
        return (--month % 2 == 0 ? LONG_MONTH_LENGTH : SHORT_MONTH_LENGTH);
    }

    //-----------------------------------------------------------------------
    int getMonthOfYear(long millis, int year) {
        int doyZeroBased = (int) ((millis - getYearMillis(year)) / DateTimeConstants.MILLIS_PER_DAY);
        if (doyZeroBased == 354) {
            return 12;
        }
        return ((doyZeroBased * 2) / MONTH_PAIR_LENGTH) + 1;
//        return (int) (doyZeroBased / 29.9f) + 1;
//        
//        int monthPairZeroBased = doyZeroBased / MONTH_PAIR_LENGTH;
//        int monthPairRemainder = doyZeroBased % MONTH_PAIR_LENGTH;
//        return (monthPairZeroBased * 2) + 1 + (monthPairRemainder >= LONG_MONTH_LENGTH ? 1 : 0);
    }

    //-----------------------------------------------------------------------
    long getAverageMillisPerYear() {
        return MILLIS_PER_YEAR;
    }

    //-----------------------------------------------------------------------
    long getAverageMillisPerYearDividedByTwo() {
        return MILLIS_PER_YEAR / 2;
    }

    //-----------------------------------------------------------------------
    long getAverageMillisPerMonth() {
        return MILLIS_PER_MONTH;
    }

    //-----------------------------------------------------------------------
    long calculateFirstDayOfYearMillis(int year) {
        if (year > MAX_YEAR) {
            throw new ArithmeticException("Year is too large: " + year + " > " + MAX_YEAR);
        }
        if (year < MIN_YEAR) {
            throw new ArithmeticException("Year is too small: " + year + " < " + MIN_YEAR);
        }

        // Java epoch is 1970-01-01 Gregorian which is 0622-07-16 Islamic.
        // 0001-01-01 Islamic is -42520809600000L
        // would prefer to calculate against year zero, but leap year
        // can be in that year so it doesn't work
        year--;
        long cycle = year / CYCLE;
        long millis = MILLIS_YEAR_1 + cycle * MILLIS_PER_CYCLE;
        int cycleRemainder = (year % CYCLE) + 1;
        
        for (int i = 1; i < cycleRemainder; i++) {
            millis += (isLeapYear(i) ? MILLIS_PER_LONG_YEAR : MILLIS_PER_SHORT_YEAR);
        }
        
        return millis;
    }

    //-----------------------------------------------------------------------
    int getMinYear() {
        return 1; //MIN_YEAR;
    }

    //-----------------------------------------------------------------------
    int getMaxYear() {
        return MAX_YEAR;
    }

    //-----------------------------------------------------------------------
    long getApproxMillisAtEpochDividedByTwo() {
        // Epoch 1970-01-01 ISO = 1389-10-22 Islamic
        return (-MILLIS_YEAR_1) / 2;
    }

    //-----------------------------------------------------------------------
    protected void assemble(Fields fields) {
        if (getBase() == null) {
            super.assemble(fields);

            fields.era = ERA_FIELD;
            fields.monthOfYear = new BasicMonthOfYearDateTimeField(this, 12);
            fields.months = fields.monthOfYear.getDurationField();
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Opaque object describing a leap year pattern for the Islamic Chronology.
     *
     * @since 1.2
     */
    public static class LeapYearPatternType implements Serializable {
        /** Serialization lock */
        private static final long serialVersionUID = 26581275372698L;
//        /** Leap year raw data encoded into bits. */
//        private static final int[][] LEAP_YEARS = {
//            {2, 5, 7, 10, 13, 15, 18, 21, 24, 26, 29},  // 623158436
//            {2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29},  // 623191204
//            {2, 5, 8, 10, 13, 16, 19, 21, 24, 27, 29},  // 690562340
//            {0, 2, 5, 8, 11, 13, 16, 19, 21, 24, 27},   // 153692453
//        };
        
        /** The index. */
        final byte index;
        /** The leap year pattern, a bit-based 1=true pattern. */
        final int pattern;
        
        /**
         * Constructor.
         * This constructor takes a bit pattern where bits 0-29 correspond
         * to years 0-29 in the 30 year Islamic cycle of years. This allows
         * a highly efficient lookup by bit-matching.
         *
         * @param index  the index
         * @param pattern  the bit pattern
         */
        LeapYearPatternType(int index, int pattern) {
            super();
            this.index = (byte) index;
            this.pattern = pattern;
        }
        
        /**
         * Is the year a leap year.
         * @param year  the year to query
         * @return true if leap
         */
        boolean isLeapYear(int year) {
            int key = 1 << (year % 30);
            return ((pattern & key) > 0);
        }
        
        /**
         * Ensure a singleton is returned if possible.
         * @return the singleton instance
         */
        private Object readResolve() {
            switch (index) {
                case 0:
                    return LEAP_YEAR_15_BASED;
                case 1:
                    return LEAP_YEAR_16_BASED;
                case 2:
                    return LEAP_YEAR_INDIAN;
                case 3:
                    return LEAP_YEAR_HABASH_AL_HASIB;
                default:
                    return this;
            }
        }
    }
}
