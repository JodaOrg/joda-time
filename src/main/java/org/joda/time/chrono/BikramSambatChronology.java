/*
 *  Copyright 2001-2018 Bishwash Adhikari
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
 * Implements the Bikram Sambat (Bikram Samwat or Vikram Samvat) calendar system used in Nepal and India.
 *
 * Bikram Sambat calendar is a lunisideral calendar because it uses lunar months and sidereal years for timekeeping.
 * The year is broken down into 12 months, ach 29-32 days in length.
 * This calendar is 56.7 years ahead of the solar Gregorian calendar.
 *
 * Conversion between Bikram Sambat dates and Gregorian Dates is not simple. It cannot be done by a formula and needs
 * database mappings since months have different amount of days in different years.
 *
 * This implementation defines a day as midnight to midnight exactly as per
 * the ISO chronology. This correct start of day is at sunset on the previous
 * day, however this cannot readily be modelled and has been ignored.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Vikram_Samvat">Wikipedia</a>
 */
public final class BikramSambatChronology extends BasicChronology {

    /** Serialization lock */
    private static final long serialVersionUID = -3663823829888L;

    /** A singleton era field. */
    private static final DateTimeField ERA_FIELD = new BasicSingleEraDateTimeField("BS");

    /** The lowest year that can be fully supported. */
    private static final int MIN_YEAR = 2000;

    /** The highest year that can be fully supported. */
    private static final int MAX_YEAR = 2090;

    /** The length of the long month. */
    private static final int LONG_MONTH_LENGTH = 32;

    /** The length of the long month in millis. */
    private static final long MILLIS_PER_MONTH = (long) (30.4166666667 * DateTimeConstants.MILLIS_PER_DAY);

    /** The typical millis per year. */
    private static final long MILLIS_PER_YEAR = (long) (365.36667 * DateTimeConstants.MILLIS_PER_DAY);

    /** The millis of 1943-04-14. */
    private static final long MILLIS_YEAR_1 = -843177600000L;

    /** Cache of zone to chronology arrays */
    private static final ConcurrentHashMap<DateTimeZone, BikramSambatChronology> cCache = new ConcurrentHashMap<DateTimeZone, BikramSambatChronology>();

    /** Singleton instance of a UTC BikramSambatChronology */
    private static final BikramSambatChronology INSTANCE_UTC;
    static {
        // init after static fields
        INSTANCE_UTC = getInstance(DateTimeZone.UTC);
    }

    /**
     * Gets an instance of the BikramSambatChronology.
     * The time zone of the returned instance is UTC.
     *
     * @return a singleton UTC instance of the chronology
     */
    public static BikramSambatChronology getInstanceUTC() {
        return INSTANCE_UTC;
    }

    /**
     * Gets an instance of the BikramSambatChronology in the given time zone.
     *
     * @param zone the time zone to get the chronology in, null is default
     * @return a chronology in the specified time zone
     */
    public static BikramSambatChronology getInstance(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        BikramSambatChronology chrono = cCache.get(zone);
        if (chrono == null) {
            if (zone == DateTimeZone.UTC) {
                //First create without a lower limit.
                chrono = new BikramSambatChronology(null, null);
                cCache.putIfAbsent(zone, chrono);
                // Impose lower limit and make another BikramSambatChronology.
                DateTime lowerLimit = new DateTime(MIN_YEAR, 1, 1, 0, 0, 0, 0, chrono);
                chrono = new BikramSambatChronology(LimitChronology.getInstance(chrono, lowerLimit, null), null);
            } else {
                chrono = getInstance(DateTimeZone.UTC);
                chrono = new BikramSambatChronology(ZonedChronology.getInstance(chrono, zone), null);
            }
        }
        return chrono;
    }

    /**
     * Restricted constructor.
     */
    private BikramSambatChronology(Chronology base, Object param) {
        super(base, param, 2);
    }

    /**
     * Serialization singleton.
     */
    private Object readResolve() {
        Chronology base = getBase();
        return base == null ? getInstanceUTC() : getInstance(base.getZone());
    }

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
     * @param zone the zone to get the chronology in, null is default
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

    int getYear(long instant) {
        long millisNepali = instant - MILLIS_YEAR_1;
        int year = MIN_YEAR;
        for (int i = MIN_YEAR; i <= MAX_YEAR; i++) {
            long millisInYear = (long) BikramSambatDateUtils.getTotalDaysInYear(i) * DateTimeConstants.MILLIS_PER_DAY;
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

    long getTotalMillisByYearMonth(int year, int month) {
        return (long) BikramSambatDateUtils.getTotalDaysInYearTillMonth(year, month) * DateTimeConstants.MILLIS_PER_DAY;
    }

    int getDayOfMonth(long millis) {
        // optimised for simple months
        int year = getYear(millis);
        int doy = getDayOfYear(millis);

        return BikramSambatDateUtils.getDayOfMonth(year, doy);
    }

    boolean isLeapYear(int year) {
        return false;
    }

    int getDaysInYearMax() {
        return 366;
    }

    int getDaysInYear(int year) {
        return BikramSambatDateUtils.getTotalDaysInYear(year);
    }

    int getDaysInYearMonth(int year, int month) {
        return BikramSambatDateUtils.getTotalDaysInYearsMonth(year, month);
    }

    int getDaysInMonthMax() {
        return LONG_MONTH_LENGTH;
    }

    int getDaysInMonthMax(int month) {
        return LONG_MONTH_LENGTH;
    }

    int getMonthOfYear(long millis, int year) {
        int doyZeroBased = (int) ((millis - getYearMillis(year)) / DateTimeConstants.MILLIS_PER_DAY);
        return BikramSambatDateUtils.getMonthByYearDays(year, doyZeroBased);
    }

    long getAverageMillisPerYear() {
        return MILLIS_PER_YEAR;
    }

    long getAverageMillisPerYearDividedByTwo() {
        return MILLIS_PER_YEAR / 2;
    }

    long getAverageMillisPerMonth() {
        return MILLIS_PER_MONTH;
    }

    long calculateFirstDayOfYearMillis(int year) {
        if (year > MAX_YEAR) {
            throw new ArithmeticException("Year is too large: " + year + " > " + MAX_YEAR);
        }
        if (year < MIN_YEAR) {
            throw new ArithmeticException("Year is too small: " + year + " < " + MIN_YEAR);
        }

        long millis = MILLIS_YEAR_1;
        for (int i = MIN_YEAR; i < year; i++) {
            millis += (long) BikramSambatDateUtils.getTotalDaysInYear(i) * DateTimeConstants.MILLIS_PER_DAY;
        }

        return millis;
    }

    int getMinYear() {
        return MIN_YEAR;
    }

    int getMaxYear() {
        return MAX_YEAR;
    }

    long getApproxMillisAtEpochDividedByTwo() {
        // Epoch 1970-01-01 ISO = 2026-09-17 Bikram Sambat
        return (-MILLIS_YEAR_1) / 2;
    }

    protected void assemble(Fields fields) {
        if (getBase() == null) {
            super.assemble(fields);

            fields.era = ERA_FIELD;
        }
    }
}
