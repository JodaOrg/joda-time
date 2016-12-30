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

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;

import java.util.concurrent.ConcurrentHashMap;

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
public final class BISChronology extends BasicChronology {

    /** Serialization lock */
    private static final long serialVersionUID = -3663823829888L;

    /** A singleton era field. */
    private static final DateTimeField ERA_FIELD =  new BasicSingleEraDateTimeField("BS");

    /** The lowest year that can be fully supported. */
    private static final int MIN_YEAR = 2000;

    /**
     * The highest year that can be fully supported.
     * Although calculateFirstDayOfYearMillis can go higher without
     * overflowing, the getYear method overflows when it adds the
     * approximate millis at the epoch.
     */
    private static final int MAX_YEAR = 2090;

    /** The length of the long month. */
    private static final int LONG_MONTH_LENGTH = 32;

    /** The length of the long month in millis. */
    private static final long MILLIS_PER_MONTH = (long) (30.4166666667 * DateTimeConstants.MILLIS_PER_DAY);

    /** The typical millis per year. */
    private static final long MILLIS_PER_YEAR = (long) (365.36667 * DateTimeConstants.MILLIS_PER_DAY);

    /** The millis of 0001-01-01. */
    private static final long MILLIS_YEAR_1 = -843177600000L;

    /** Cache of zone to chronology arrays */
    private static final ConcurrentHashMap<DateTimeZone, BISChronology> cCache = new ConcurrentHashMap<DateTimeZone, BISChronology>();

    /** Singleton instance of a UTC IslamicChronology */
    private static final BISChronology INSTANCE_UTC;
    static {
        // init after static fields
        INSTANCE_UTC = getInstance(DateTimeZone.UTC);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets an instance of the IslamicChronology.
     * The time zone of the returned instance is UTC.
     *
     * @return a singleton UTC instance of the chronology
     */
    public static BISChronology getInstanceUTC() {
        return INSTANCE_UTC;
    }

    /**
     * Gets an instance of the IslamicChronology in the default time zone.
     *
     * @return a chronology in the default time zone
     */
    public static BISChronology getInstance() {
        return getInstance(DateTimeZone.getDefault());
    }

    /**
     * Gets an instance of the IslamicChronology in the given time zone.
     *
     * @param zone  the time zone to get the chronology in, null is default
     * @return a chronology in the specified time zone
     */
    public static BISChronology getInstance(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        BISChronology chrono = cCache.get(zone);
        if (chrono == null) {
//            if (zone == DateTimeZone.UTC) {
                        //First create without a lower limit.
                        chrono = new BISChronology(null, null);
                        // Impose lower limit and make another IslamicChronology.
                        DateTime lowerLimit = new DateTime(MIN_YEAR, 1, 1, 0, 0, 0, 0, chrono);
                        chrono = new BISChronology(
                                LimitChronology.getInstance(chrono, lowerLimit, null), null);
//                    } else {
//                        chrono = getInstance(DateTimeZone.UTC);
//                        chrono = new BISChronology
//                                (ZonedChronology.getInstance(chrono, zone), null);
//                    }
//            if (oldChrono != null) {
//                chrono = chrono;
//            }
        }
        return chrono;
    }

    // Constructors and instance variables
    //-----------------------------------------------------------------------
    /**
     * Restricted constructor.
     */
    private BISChronology(Chronology base, Object param) {
        super(base, param, 2);
    }

    /**
     * Serialization singleton.
     */
    private Object readResolve() {
        Chronology base = getBase();
        return base == null ? getInstanceUTC() : getInstance(base.getZone());
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

    //-----------------------------------------------------------------------
    /**
     * Checks if this chronology instance equals another.
     *
     * @param obj  the object to compare to
     * @return true if equal
     * @since 2.3
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return false;
    }

    /**
     * A suitable hash code for the chronology.
     *
     * @return the hash code
     * @since 1.6
     */
    public int hashCode() {
        return super.hashCode() * 13;
    }

    //-----------------------------------------------------------------------
    int getYear(long instant) {
        long millisNepali = instant - MILLIS_YEAR_1;
        int year = MIN_YEAR;
        for (int i = MIN_YEAR; i <= MAX_YEAR; i++) {
            long millisInYear = (long) BISDateUtils.getTotalDaysInYear(i) * DateTimeConstants.MILLIS_PER_DAY;
            if (millisNepali > millisInYear){
                millisNepali -= millisInYear;
                year++;
            }else{
                break;
            }
        }
        return year ;
    }

    long setYear(long instant, int year) {
        // optimsed implementation of set, due to fixed months
        int thisYear = getYear(instant);
        int dayOfYear = getDayOfYear(instant, thisYear);
        int millisOfDay = getMillisOfDay(instant);

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
        long totalMillis =  (long) BISDateUtils.getTotalDaysInYearTillMonth(year, month) * DateTimeConstants.MILLIS_PER_DAY;
        return totalMillis;
    }

    //-----------------------------------------------------------------------
    int getDayOfMonth(long millis) {
        // optimised for simple months
        int year = getYear(millis);
        int doy = getDayOfYear(millis);

        return BISDateUtils.getDayOfMonth(year, doy);
    }

    //-----------------------------------------------------------------------
    boolean isLeapYear(int year) {
//        boolean isLeap = false;
//        if (DateUtils.getTotalDaysInYear(year) == 366){
//            isLeap = true;
//        }
        return false;
    }

    //-----------------------------------------------------------------------
    int getDaysInYearMax() {
        return 366;
    }

    //-----------------------------------------------------------------------
    int getDaysInYear(int year) {
        return BISDateUtils.getTotalDaysInYear(year);
    }

    //-----------------------------------------------------------------------
    int getDaysInYearMonth(int year, int month) {
//        System.out.println("getTotalDaysInYearsMonth m> "+ month+" y> " +year+" result > "+DateUtils.getTotalDaysInYearsMonth(year, month));
        return BISDateUtils.getTotalDaysInYearsMonth(year, month);
    }

    //-----------------------------------------------------------------------
    int getDaysInMonthMax() {
        return LONG_MONTH_LENGTH;
    }

    //-----------------------------------------------------------------------
    int getDaysInMonthMax(int month) {
        return LONG_MONTH_LENGTH;
    }

    //-----------------------------------------------------------------------
    int getMonthOfYear(long millis, int year) {
        int doyZeroBased = (int) ((millis - getYearMillis(year)) / DateTimeConstants.MILLIS_PER_DAY);
        return BISDateUtils.getMonthByYearDays(year, doyZeroBased);
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
        long millis = MILLIS_YEAR_1;
        for (int i = MIN_YEAR; i <= year; i++) {
            millis += (long) BISDateUtils.getTotalDaysInYear(i) * DateTimeConstants.MILLIS_PER_DAY;
        }

        return millis;
    }

    //-----------------------------------------------------------------------
    int getMinYear() {
        return MIN_YEAR; //MIN_YEAR;
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
//            fields.monthOfYear = new BISDateTimeField(this, 0);
//            fields.months = fields.monthOfYear.getDurationField();
        }
    }
}
