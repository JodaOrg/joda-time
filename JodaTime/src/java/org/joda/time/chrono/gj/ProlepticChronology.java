/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2004 Stephen Colebourne.  
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

import java.util.Locale;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DurationField;
import org.joda.time.chrono.DividedDateTimeField;
import org.joda.time.chrono.MillisDurationField;
import org.joda.time.chrono.NonZeroDateTimeField;
import org.joda.time.chrono.RemainderDateTimeField;
import org.joda.time.chrono.PreciseDateTimeField;
import org.joda.time.chrono.PreciseDurationField;
import org.joda.time.chrono.Utils;

/**
 * ProlepticChronology uses a consistent set of rules for all dates and
 * times. Year zero is included.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
abstract class ProlepticChronology extends GJChronology {

    static final long serialVersionUID = 541866437970475456L;

    static final long MILLIS_1970_TO_2000 = 946684800000L;

    // These arrays are NOT public. We trust ourselves not to alter the array.
    // They use zero-based array indexes so the that valid range of months is
    // automatically checked.

    private static final int[] MIN_DAYS_PER_MONTH_ARRAY = {
        31,28,31,30,31,30,31,31,30,31,30,31
    };

    private static final int[] MAX_DAYS_PER_MONTH_ARRAY = {
        31,29,31,30,31,30,31,31,30,31,30,31
    };

    private static final long[] MIN_TOTAL_MILLIS_BY_MONTH_ARRAY;
    private static final long[] MAX_TOTAL_MILLIS_BY_MONTH_ARRAY;

    private static final DurationField cMillisField;
    private static final DurationField cSecondsField;
    private static final DurationField cMinutesField;
    private static final DurationField cHoursField;
    private static final DurationField cHalfdaysField;
    private static final DurationField cDaysField;
    private static final DurationField cWeeksField;

    private static final DateTimeField cMillisOfSecondField;
    private static final DateTimeField cMillisOfDayField;
    private static final DateTimeField cSecondOfMinuteField;
    private static final DateTimeField cSecondOfDayField;
    private static final DateTimeField cMinuteOfHourField;
    private static final DateTimeField cMinuteOfDayField;
    private static final DateTimeField cHourOfDayField;
    private static final DateTimeField cHourOfHalfdayField;
    private static final DateTimeField cClockhourOfDayField;
    private static final DateTimeField cClockhourOfHalfdayField;
    private static final DateTimeField cHalfdayOfDayField;

    static {
        MIN_TOTAL_MILLIS_BY_MONTH_ARRAY = new long[12];
        MAX_TOTAL_MILLIS_BY_MONTH_ARRAY = new long[12];

        long minSum = 0;
        long maxSum = 0;
        for (int i=0; i<12; i++) {
            long millis = MIN_DAYS_PER_MONTH_ARRAY[i]
                * (long)DateTimeConstants.MILLIS_PER_DAY;
            minSum += millis;
            MIN_TOTAL_MILLIS_BY_MONTH_ARRAY[i] = minSum;

            millis = MAX_DAYS_PER_MONTH_ARRAY[i]
                * (long)DateTimeConstants.MILLIS_PER_DAY;
            maxSum += millis;
            MAX_TOTAL_MILLIS_BY_MONTH_ARRAY[i] = maxSum;
        }

        cMillisField = MillisDurationField.INSTANCE;
        cSecondsField = new PreciseDurationField
            ("seconds", DateTimeConstants.MILLIS_PER_SECOND);
        cMinutesField = new PreciseDurationField
            ("minutes", DateTimeConstants.MILLIS_PER_MINUTE);
        cHoursField = new PreciseDurationField
            ("hours", DateTimeConstants.MILLIS_PER_HOUR);
        cHalfdaysField = new PreciseDurationField
            ("halfdays", DateTimeConstants.MILLIS_PER_DAY / 2);
        cDaysField = new PreciseDurationField
            ("days", DateTimeConstants.MILLIS_PER_DAY);
        cWeeksField = new PreciseDurationField
            ("weeks", DateTimeConstants.MILLIS_PER_WEEK);

        cMillisOfSecondField = new PreciseDateTimeField
            ("millisOfSecond", cMillisField, cSecondsField);

        cMillisOfDayField = new PreciseDateTimeField
            ("millisOfDay", cMillisField, cDaysField);
             
        cSecondOfMinuteField = new PreciseDateTimeField
            ("secondOfMinute", cSecondsField, cMinutesField);

        cSecondOfDayField = new PreciseDateTimeField
            ("secondOfDay", cSecondsField, cDaysField);

        cMinuteOfHourField = new PreciseDateTimeField
            ("minuteOfHour", cMinutesField, cHoursField);

        cMinuteOfDayField = new PreciseDateTimeField
            ("minuteOfDay", cMinutesField, cDaysField);

        cHourOfDayField = new PreciseDateTimeField
            ("hourOfDay", cHoursField, cDaysField);

        cHourOfHalfdayField = new PreciseDateTimeField
            ("hourOfHalfday", cHoursField, cHalfdaysField);

        cClockhourOfDayField = new NonZeroDateTimeField
            (cHourOfDayField, "clockhourOfDay");

        cClockhourOfHalfdayField = new NonZeroDateTimeField
            (cHourOfHalfdayField, "clockhourOfHalfday");

        cHalfdayOfDayField = new HalfdayField();
    }

    private transient YearInfo[] iYearInfoCache;
    private transient int iYearInfoCacheMask;

    private final int iMinDaysInFirstWeek;

    ProlepticChronology(int minDaysInFirstWeek) {
        super();
        iMinDaysInFirstWeek = minDaysInFirstWeek;

        Integer i;
        try {
            i = Integer.getInteger("org.joda.time.gj.ProlepticChronology.yearInfoCacheSize");
        } catch (SecurityException e) {
            i = null;
        }

        int cacheSize;
        if (i == null) {
            cacheSize = 1024; // (1 << 10)
        } else {
            cacheSize = i.intValue();
            // Ensure cache size is even power of 2.
            cacheSize--;
            int shift = 0;
            while (cacheSize > 0) {
                shift++;
                cacheSize >>= 1;
            }
            cacheSize = 1 << shift;
        }

        iYearInfoCache = new YearInfo[cacheSize];
        iYearInfoCacheMask = cacheSize - 1;

        // First copy fields that are the same for all Gregorian and Julian
        // chronologies.

        iMillisField = cMillisField;
        iSecondsField = cSecondsField;
        iMinutesField = cMinutesField;
        iHoursField = cHoursField;
        //iHalfdaysField = cHalfdaysField;  Doesn't exist in public interface
        iDaysField = cDaysField;
        iWeeksField = cWeeksField;

        iMillisOfSecondField = cMillisOfSecondField;
        iMillisOfDayField = cMillisOfDayField;
        iSecondOfMinuteField = cSecondOfMinuteField;
        iSecondOfDayField = cSecondOfDayField;
        iMinuteOfHourField = cMinuteOfHourField;
        iMinuteOfDayField = cMinuteOfDayField;
        iHourOfDayField = cHourOfDayField;
        iHourOfHalfdayField = cHourOfHalfdayField;
        iClockhourOfDayField = cClockhourOfDayField;
        iClockhourOfHalfdayField = cClockhourOfHalfdayField;
        iHalfdayOfDayField = cHalfdayOfDayField;

        // Now create fields that have unique behavior for Gregorian and Julian
        // chronologies.

        iYearField = new GJYearDateTimeField(this);
        iYearOfEraField = new GJYearOfEraDateTimeField(iYearField, this);

        iCenturyOfEraField = new DividedDateTimeField
            (iYearOfEraField, "centuryOfEra", "centuries", 100);
        iYearOfCenturyField = new RemainderDateTimeField
            ((DividedDateTimeField)iCenturyOfEraField, "yearOfCentury");

        iEraField = new GJEraDateTimeField(this);
        iDayOfWeekField = new GJDayOfWeekDateTimeField(this, iDaysField);
        iDayOfMonthField = new GJDayOfMonthDateTimeField(this, iDaysField);
        iDayOfYearField = new GJDayOfYearDateTimeField(this, iDaysField);
        iMonthOfYearField = new GJMonthOfYearDateTimeField(this);
        iWeekyearField = new GJWeekyearDateTimeField(this);
        iWeekOfWeekyearField = new GJWeekOfWeekyearDateTimeField(this, iWeeksField);

        // The remaining (imprecise) durations are available from the newly
        // created datetime fields.

        iYearsField = iYearField.getDurationField();
        iCenturiesField = iCenturyOfEraField.getDurationField();
        iMonthsField = iMonthOfYearField.getDurationField();
        iWeekyearsField = iWeekyearField.getDurationField();
    }

    public Chronology withUTC() {
        return this;
    }

    /**
     * Override the default implementation
     */
    public final long getDateOnlyMillis(int year, int monthOfYear, int dayOfMonth)
        throws IllegalArgumentException
    {
        boolean isLeap = isLeapYear(year);

        Utils.verifyValueBounds("monthOfYear", monthOfYear, 1, 12);
        Utils.verifyValueBounds("dayOfMonth", dayOfMonth, 1,
                                (isLeap ? MAX_DAYS_PER_MONTH_ARRAY : MIN_DAYS_PER_MONTH_ARRAY)
                                [monthOfYear - 1]);

        long instant = getYearMillis(year);

        if (monthOfYear > 1) {
            instant += 
                (isLeap ? MAX_TOTAL_MILLIS_BY_MONTH_ARRAY : MIN_TOTAL_MILLIS_BY_MONTH_ARRAY)
                [monthOfYear - 2];
        }

        if (dayOfMonth != 1) {
            instant += (dayOfMonth - 1) * (long)DateTimeConstants.MILLIS_PER_DAY;
        }

        return instant;
    }

    /**
     * Override the default implementation
     */
    public final long getTimeOnlyMillis(int hourOfDay, int minuteOfHour,
                                        int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {

        Utils.verifyValueBounds("hourOfDay", hourOfDay, 0, 23);
        Utils.verifyValueBounds("minuteOfHour", minuteOfHour, 0, 59);
        Utils.verifyValueBounds("secondOfMinute", secondOfMinute, 0, 59);
        Utils.verifyValueBounds("millisOfSecond", millisOfSecond, 0, 999);

        return hourOfDay * DateTimeConstants.MILLIS_PER_HOUR
            + minuteOfHour * DateTimeConstants.MILLIS_PER_MINUTE
            + secondOfMinute * DateTimeConstants.MILLIS_PER_SECOND
            + millisOfSecond;
    }

    /**
     * Override the default implementation
     */
    public final long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                        int millisOfDay)
        throws IllegalArgumentException
    {
        Utils.verifyValueBounds("millisOfDay", millisOfDay, 0, DateTimeConstants.MILLIS_PER_DAY);
        return getDateOnlyMillis(year, monthOfYear, dayOfMonth) + millisOfDay;
    }

    /**
     * Override the default implementation
     */
    public final long getDateTimeMillis(long instant,
                                        int hourOfDay, int minuteOfHour,
                                        int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        return getDateOnlyMillis(instant)
            + getTimeOnlyMillis(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    /**
     * Override the default implementation
     */
    public final long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                        int hourOfDay, int minuteOfHour,
                                        int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        return getDateOnlyMillis(year, monthOfYear, dayOfMonth)
            + getTimeOnlyMillis(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    public final boolean isCenturyISO() {
        return true;
    }

    public final int getMinimumDaysInFirstWeek() {
        return iMinDaysInFirstWeek;
    }

    /**
     * Get the number of days in the year.
     * @param year The year to use.
     * @return 366 if a leap year, otherwise 365.
     */
    public final int getDaysInYear(int year) {
        return isLeapYear(year) ? 366 : 365;
    }

    public final int getDaysInYearMonth(int year, int month) {
        if (isLeapYear(year)) {
            return MAX_DAYS_PER_MONTH_ARRAY[month - 1];
        } else {
            return MIN_DAYS_PER_MONTH_ARRAY[month - 1];
        }
    }

    /**
     * Returns the total number of milliseconds elapsed in the year, by the end
     * of the month.
     */
    public final long getTotalMillisByYearMonth(int year, int month) {
        if (isLeapYear(year)) {
            return MAX_TOTAL_MILLIS_BY_MONTH_ARRAY[month - 1];
        } else {
            return MIN_TOTAL_MILLIS_BY_MONTH_ARRAY[month - 1];
        }
    }

    /**
     * Get the number of weeks in the year.
     * @param year  the year to use.
     * @return number of weeks in the year.
     */
    public final int getWeeksInYear(int year) {
        long firstWeekMillis1 = getFirstWeekOfYearMillis(year);
        long firstWeekMillis2 = getFirstWeekOfYearMillis(year + 1);
        return (int) ((firstWeekMillis2 - firstWeekMillis1) / DateTimeConstants.MILLIS_PER_WEEK);
    }

    /**
     * Get the millis for the first week of a year.
     * @param year  the year to use.
     * @return millis
     */
    public final long getFirstWeekOfYearMillis(int year) {
        long jan1millis = getYearMillis(year);
        int jan1dayOfWeek = dayOfWeek().get(jan1millis);
        
        if (jan1dayOfWeek > (8 - iMinDaysInFirstWeek)) {
            // First week is end of previous year because it doesn't have enough days.
            return jan1millis + (8 - jan1dayOfWeek)
                * (long)DateTimeConstants.MILLIS_PER_DAY;
        } else {
            // First week is start of this year because it has enough days.
            return jan1millis - (jan1dayOfWeek - 1)
                * (long)DateTimeConstants.MILLIS_PER_DAY;
        }
    }

    /**
     * Get the milliseconds for the start of a year.
     *
     * @param year The year to use.
     * @return millis from 1970-01-01T00:00:00Z
     */
    public final long getYearMillis(int year) {
        return getYearInfo(year).iFirstDayMillis;
        //return calculateFirstDayOfYearMillis(year);
    }

    /**
     * Get the milliseconds for the start of a month.
     *
     * @param year The year to use.
     * @param month The month to use
     * @return millis from 1970-01-01T00:00:00Z
     */
    public final long getYearMonthMillis(int year, int month) {
        long millis = getYearMillis(year);
        // month
        if (month > 1) {
            millis += getTotalMillisByYearMonth(year, month - 1);
        }
        return millis;
    }

    /**
     * Get the milliseconds for a particular date.
     *
     * @param year The year to use.
     * @param month The month to use
     * @param dayOfMonth The day of the month to use
     * @return millis from 1970-01-01T00:00:00Z
     */
    public final long getYearMonthDayMillis(int year, int month, int dayOfMonth) {
        long millis = getYearMillis(year);
        // month
        if (month > 1) {
            millis += getTotalMillisByYearMonth(year, month - 1);
        }
        // day
        return millis + (dayOfMonth - 1) * (long)DateTimeConstants.MILLIS_PER_DAY;
    }
    
    /**
     * @param millis from 1970-01-01T00:00:00Z
     */
    public final int getMonthOfYear(long millis) {
        return getMonthOfYear(millis, year().get(millis));
    }

    /**
     * @param millis from 1970-01-01T00:00:00Z
     * @param year precalculated year of millis
     */
    public final int getMonthOfYear(long millis, int year) {
        // Perform a binary search to get the month. To make it go even faster,
        // compare using ints instead of longs. The number of milliseconds per
        // year exceeds the limit of a 32-bit int's capacity, so divide by
        // 1024. No precision is lost (except time of day) since the number of
        // milliseconds per day contains 1024 as a factor. After the division,
        // the instant isn't measured in milliseconds, but in units of
        // (128/125)seconds.

        int i = (int)((millis - getYearMillis(year)) >> 10);

        // There are 86400000 milliseconds per day, but divided by 1024 is
        // 84375. There are 84375 (128/125)seconds per day.

        return
            (isLeapYear(year))
            ? ((i < 182 * 84375)
               ? ((i < 91 * 84375)
                  ? ((i < 31 * 84375) ? 1 : (i < 60 * 84375) ? 2 : 3)
                  : ((i < 121 * 84375) ? 4 : (i < 152 * 84375) ? 5 : 6))
               : ((i < 274 * 84375)
                  ? ((i < 213 * 84375) ? 7 : (i < 244 * 84375) ? 8 : 9)
                  : ((i < 305 * 84375) ? 10 : (i < 335 * 84375) ? 11 : 12)))
            : ((i < 181 * 84375)
               ? ((i < 90 * 84375)
                  ? ((i < 31 * 84375) ? 1 : (i < 59 * 84375) ? 2 : 3)
                  : ((i < 120 * 84375) ? 4 : (i < 151 * 84375) ? 5 : 6))
               : ((i < 273 * 84375)
                  ? ((i < 212 * 84375) ? 7 : (i < 243 * 84375) ? 8 : 9)
                  : ((i < 304 * 84375) ? 10 : (i < 334 * 84375) ? 11 : 12)));
    }

    /**
     * @param millis from 1970-01-01T00:00:00Z
     */
    public final int getDayOfMonth(long millis) {
        int year = year().get(millis);
        int month = getMonthOfYear(millis, year);
        return getDayOfMonth(millis, year, month);
    }

    /**
     * @param millis from 1970-01-01T00:00:00Z
     * @param year precalculated year of millis
     */
    public final int getDayOfMonth(long millis, int year) {
        int month = getMonthOfYear(millis, year);
        return getDayOfMonth(millis, year, month);
    }

    /**
     * @param millis from 1970-01-01T00:00:00Z
     * @param year precalculated year of millis
     * @param month precalculated month of millis
     */
    public final int getDayOfMonth(long millis, int year, int month) {
        long dateMillis = getYearMillis(year);
        if (month > 1) {
            dateMillis += getTotalMillisByYearMonth(year, month - 1);
        }
        return (int) ((millis - dateMillis) / DateTimeConstants.MILLIS_PER_DAY) + 1;
    }

    public abstract boolean isLeapYear(int year);

    protected abstract long calculateFirstDayOfYearMillis(int year);

    protected abstract int getMinYear();

    protected abstract int getMaxYear();

    protected abstract long getRoughMillisPerYear();

    protected abstract long getRoughMillisPerMonth();

    // Although accessed by multiple threads, this method doesn't need to be synchronized.
    private YearInfo getYearInfo(int year) {
        YearInfo[] cache = iYearInfoCache;
        int index = year & iYearInfoCacheMask;
        YearInfo info = cache[index];
        if (info == null || info.iYear != year) {
            info = new YearInfo(year, calculateFirstDayOfYearMillis(year));
            cache[index] = info;
        }
        return info;
    }

    private static class HalfdayField extends PreciseDateTimeField {
        static final long serialVersionUID = 581601443656929254L;

        HalfdayField() {
            super("halfdayOfDay", cHalfdaysField, cDaysField);
        }

        public String getAsText(long millis, Locale locale) {
            return GJLocaleSymbols.forLocale(locale).halfdayValueToText(get(millis));
        }

        public long set(long millis, String text, Locale locale) {
            return set(millis, GJLocaleSymbols.forLocale(locale).halfdayTextToValue(text));
        }

        public int getMaximumTextLength(Locale locale) {
            return GJLocaleSymbols.forLocale(locale).getHalfdayMaxTextLength();
        }
    }

    private static class YearInfo {
        public final int iYear;
        public final long iFirstDayMillis;

        YearInfo(int year, long firstDayMillis) {
            iYear = year;
            iFirstDayMillis = firstDayMillis;
        }
    }
}
