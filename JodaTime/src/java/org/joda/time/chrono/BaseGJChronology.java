/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2005 Stephen Colebourne.
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
package org.joda.time.chrono;

import java.util.Locale;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.field.DividedDateTimeField;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.MillisDurationField;
import org.joda.time.field.ZeroIsMaxDateTimeField;
import org.joda.time.field.OffsetDateTimeField;
import org.joda.time.field.PreciseDateTimeField;
import org.joda.time.field.PreciseDurationField;
import org.joda.time.field.RemainderDateTimeField;

/**
 * Abstract Chronology for implementing chronologies based on Gregorian/Julian formulae.
 * Most of the utility methods required by subclasses are package-private,
 * reflecting the intention that they be defined in the same package.
 * <p>
 * AbstractGJChronology is thread-safe and immutable, and all subclasses must
 * be as well.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @author Guy Allard
 * @since 1.0
 */
public abstract class BaseGJChronology extends AssembledChronology {

    /** Serialization lock */
    private static final long serialVersionUID = 8283225332206808863L;

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
            (DurationFieldType.seconds(), DateTimeConstants.MILLIS_PER_SECOND);
        cMinutesField = new PreciseDurationField
            (DurationFieldType.minutes(), DateTimeConstants.MILLIS_PER_MINUTE);
        cHoursField = new PreciseDurationField
            (DurationFieldType.hours(), DateTimeConstants.MILLIS_PER_HOUR);
        cHalfdaysField = new PreciseDurationField
            (DurationFieldType.halfdays(), DateTimeConstants.MILLIS_PER_DAY / 2);
        cDaysField = new PreciseDurationField
            (DurationFieldType.days(), DateTimeConstants.MILLIS_PER_DAY);
        cWeeksField = new PreciseDurationField
            (DurationFieldType.weeks(), DateTimeConstants.MILLIS_PER_WEEK);

        cMillisOfSecondField = new PreciseDateTimeField
            (DateTimeFieldType.millisOfSecond(), cMillisField, cSecondsField);

        cMillisOfDayField = new PreciseDateTimeField
            (DateTimeFieldType.millisOfDay(), cMillisField, cDaysField);
             
        cSecondOfMinuteField = new PreciseDateTimeField
            (DateTimeFieldType.secondOfMinute(), cSecondsField, cMinutesField);

        cSecondOfDayField = new PreciseDateTimeField
            (DateTimeFieldType.secondOfDay(), cSecondsField, cDaysField);

        cMinuteOfHourField = new PreciseDateTimeField
            (DateTimeFieldType.minuteOfHour(), cMinutesField, cHoursField);

        cMinuteOfDayField = new PreciseDateTimeField
            (DateTimeFieldType.minuteOfDay(), cMinutesField, cDaysField);

        cHourOfDayField = new PreciseDateTimeField
            (DateTimeFieldType.hourOfDay(), cHoursField, cDaysField);

        cHourOfHalfdayField = new PreciseDateTimeField
            (DateTimeFieldType.hourOfHalfday(), cHoursField, cHalfdaysField);

        cClockhourOfDayField = new ZeroIsMaxDateTimeField
            (cHourOfDayField, DateTimeFieldType.clockhourOfDay());

        cClockhourOfHalfdayField = new ZeroIsMaxDateTimeField
            (cHourOfHalfdayField, DateTimeFieldType.clockhourOfHalfday());

        cHalfdayOfDayField = new HalfdayField();
    }

    private transient YearInfo[] iYearInfoCache;
    private transient int iYearInfoCacheMask;

    private final int iMinDaysInFirstWeek;

    BaseGJChronology(Chronology base, Object param, int minDaysInFirstWeek) {
        super(base, param);

        if (minDaysInFirstWeek < 1 || minDaysInFirstWeek > 7) {
            throw new IllegalArgumentException
                ("Invalid min days in first week: " + minDaysInFirstWeek);
        }

        iMinDaysInFirstWeek = minDaysInFirstWeek;

        Integer i;
        try {
            i = Integer.getInteger(getClass().getName().concat(".yearInfoCacheSize"));
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
    }

    public DateTimeZone getZone() {
        Chronology base;
        if ((base = getBase()) != null) {
            return base.getZone();
        }
        return DateTimeZone.UTC;
    }

    public final long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                        int millisOfDay)
        throws IllegalArgumentException
    {
        Chronology base;
        if ((base = getBase()) != null) {
            return base.getDateTimeMillis(year, monthOfYear, dayOfMonth, millisOfDay);
        }

        FieldUtils.verifyValueBounds("millisOfDay", millisOfDay, 0, DateTimeConstants.MILLIS_PER_DAY);
        return getDateMidnightMillis(year, monthOfYear, dayOfMonth) + millisOfDay;
    }

    public final long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                        int hourOfDay, int minuteOfHour,
                                        int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        Chronology base;
        if ((base = getBase()) != null) {
            return base.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                          hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        }

        FieldUtils.verifyValueBounds("hourOfDay", hourOfDay, 0, 23);
        FieldUtils.verifyValueBounds("minuteOfHour", minuteOfHour, 0, 59);
        FieldUtils.verifyValueBounds("secondOfMinute", secondOfMinute, 0, 59);
        FieldUtils.verifyValueBounds("millisOfSecond", millisOfSecond, 0, 999);

        return getDateMidnightMillis(year, monthOfYear, dayOfMonth)
            + hourOfDay * DateTimeConstants.MILLIS_PER_HOUR
            + minuteOfHour * DateTimeConstants.MILLIS_PER_MINUTE
            + secondOfMinute * DateTimeConstants.MILLIS_PER_SECOND
            + millisOfSecond;
    }

    public final int getMinimumDaysInFirstWeek() {
        return iMinDaysInFirstWeek;
    }

    // Output
    //-----------------------------------------------------------------------
    /**
     * Gets a debugging toString.
     * 
     * @return a debugging string
     */
    public String toString() {
        StringBuffer sb = new StringBuffer(60);
        String name = getClass().getName();
        int index = name.lastIndexOf('.');
        if (index >= 0) {
            name = name.substring(index + 1);
        }
        sb.append(name);
        sb.append('[');
        DateTimeZone zone = getZone();
        if (zone != null) {
            sb.append(zone.getID());
        }
        if (getMinimumDaysInFirstWeek() != 4) {
            sb.append(",mdfw=");
            sb.append(getMinimumDaysInFirstWeek());
        }
        sb.append(']');
        return sb.toString();
    }

    protected void assemble(Fields fields) {
        // First copy fields that are the same for all Gregorian and Julian
        // chronologies.

        fields.millis = cMillisField;
        fields.seconds = cSecondsField;
        fields.minutes = cMinutesField;
        fields.hours = cHoursField;
        fields.halfdays = cHalfdaysField;
        fields.days = cDaysField;
        fields.weeks = cWeeksField;

        fields.millisOfSecond = cMillisOfSecondField;
        fields.millisOfDay = cMillisOfDayField;
        fields.secondOfMinute = cSecondOfMinuteField;
        fields.secondOfDay = cSecondOfDayField;
        fields.minuteOfHour = cMinuteOfHourField;
        fields.minuteOfDay = cMinuteOfDayField;
        fields.hourOfDay = cHourOfDayField;
        fields.hourOfHalfday = cHourOfHalfdayField;
        fields.clockhourOfDay = cClockhourOfDayField;
        fields.clockhourOfHalfday = cClockhourOfHalfdayField;
        fields.halfdayOfDay = cHalfdayOfDayField;

        // Now create fields that have unique behavior for Gregorian and Julian
        // chronologies.

        fields.year = new GJYearDateTimeField(this);
        fields.yearOfEra = new GJYearOfEraDateTimeField(fields.year, this);

        // Define one-based centuryOfEra and yearOfCentury.
        DateTimeField field = new OffsetDateTimeField(
            fields.yearOfEra, 99);
        fields.centuryOfEra = new DividedDateTimeField(
            field, DateTimeFieldType.centuryOfEra(), 100);
        
        field = new RemainderDateTimeField(
            (DividedDateTimeField) fields.centuryOfEra);
        fields.yearOfCentury = new OffsetDateTimeField(
            field, DateTimeFieldType.yearOfCentury(), 1);

        fields.era = new GJEraDateTimeField(this);
        fields.dayOfWeek = new GJDayOfWeekDateTimeField(this, fields.days);
        fields.dayOfMonth = new GJDayOfMonthDateTimeField(this, fields.days);
        fields.dayOfYear = new GJDayOfYearDateTimeField(this, fields.days);
        fields.monthOfYear = new GJMonthOfYearDateTimeField(this);
        fields.weekyear = new GJWeekyearDateTimeField(this);
        fields.weekOfWeekyear = new GJWeekOfWeekyearDateTimeField(this, fields.weeks);
        
        field = new RemainderDateTimeField(
            fields.weekyear, DateTimeFieldType.weekyearOfCentury(), 100);
        fields.weekyearOfCentury = new OffsetDateTimeField(
            field, DateTimeFieldType.weekyearOfCentury(), 1);
        
        // The remaining (imprecise) durations are available from the newly
        // created datetime fields.

        fields.years = fields.year.getDurationField();
        fields.centuries = fields.centuryOfEra.getDurationField();
        fields.months = fields.monthOfYear.getDurationField();
        fields.weekyears = fields.weekyear.getDurationField();
    }

    /**
     * Get the number of days in the year.
     * @param year The year to use.
     * @return 366 if a leap year, otherwise 365.
     */
    final int getDaysInYear(int year) {
        return isLeapYear(year) ? 366 : 365;
    }

    final int getDaysInYearMonth(int year, int month) {
        if (isLeapYear(year)) {
            return MAX_DAYS_PER_MONTH_ARRAY[month - 1];
        } else {
            return MIN_DAYS_PER_MONTH_ARRAY[month - 1];
        }
    }

    /**
     * Gets the maximum days in the specified month.
     * 
     * @param month  the month
     * @return the max days
     */
    final int getDaysInMonthMax(int month) {
        return MAX_DAYS_PER_MONTH_ARRAY[month - 1];
    }

    /**
     * Returns the total number of milliseconds elapsed in the year, by the end
     * of the month.
     */
    final long getTotalMillisByYearMonth(int year, int month) {
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
    final int getWeeksInYear(int year) {
        long firstWeekMillis1 = getFirstWeekOfYearMillis(year);
        long firstWeekMillis2 = getFirstWeekOfYearMillis(year + 1);
        return (int) ((firstWeekMillis2 - firstWeekMillis1) / DateTimeConstants.MILLIS_PER_WEEK);
    }

    /**
     * Get the millis for the first week of a year.
     * @param year  the year to use.
     * @return millis
     */
    final long getFirstWeekOfYearMillis(int year) {
        long jan1millis = getYearMillis(year);
        int jan1dayOfWeek = getDayOfWeek(jan1millis);
        
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
    final long getYearMillis(int year) {
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
    final long getYearMonthMillis(int year, int month) {
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
    final long getYearMonthDayMillis(int year, int month, int dayOfMonth) {
        long millis = getYearMillis(year);
        // month
        if (month > 1) {
            millis += getTotalMillisByYearMonth(year, month - 1);
        }
        // day
        return millis + (dayOfMonth - 1) * (long)DateTimeConstants.MILLIS_PER_DAY;
    }
    
    /**
     * @param instant millis from 1970-01-01T00:00:00Z
     */
    final int getYear(long instant) {
        // Get an initial estimate of the year, and the millis value that
        // represents the start of that year. Then verify estimate and fix if
        // necessary.

        long unitMillis = getAverageMillisPerYear();
        long i2 = instant + getApproxMillisAtEpoch();
        if (i2 < 0) {
            i2 = i2 - unitMillis + 1;
        }
        int year = (int) (i2 / unitMillis);

        long yearStart = getYearMillis(year);
        long diff = instant - yearStart;

        if (diff < 0) {
            if (diff < -DateTimeConstants.MILLIS_PER_DAY * 2L) {
                // Too much error, assume operation overflowed.
                return getYearOverflow(instant);
            }
            year--;
        } else if (diff >= DateTimeConstants.MILLIS_PER_DAY * 365L) {
            if (diff >= DateTimeConstants.MILLIS_PER_DAY * 367L) {
                // Too much error, assume operation overflowed.
                return getYearOverflow(instant);
            }
            // One year may need to be added to fix estimate.
            long oneYear;
            if (isLeapYear(year)) {
                oneYear = DateTimeConstants.MILLIS_PER_DAY * 366L;
            } else {
                oneYear = DateTimeConstants.MILLIS_PER_DAY * 365L;
            }

            yearStart += oneYear;

            if ((yearStart ^ instant) < 0) {
                // Sign mismatch, operation may have overflowed.
                if ((yearStart <  0 && (yearStart - oneYear) >= 0) ||
                    (yearStart >= 0 && (yearStart - oneYear) <  0)   ) {
                    // It overflowed.
                    return getYearOverflow(instant);
                }
            }

            if (yearStart <= instant) {
                // Didn't go too far, so actually add one year.
                year++;
            }
        }

        return year;
    }

    private final int getYearOverflow(long instant) {
        if (instant > 0) {
            int year = getMaxYear();
            long yearStartMillis = getYearMillis(year);
            if (isLeapYear(year)) {
                yearStartMillis += DateTimeConstants.MILLIS_PER_DAY * 366L;
            } else {
                yearStartMillis += DateTimeConstants.MILLIS_PER_DAY * 365L;
            }
            long yearEndMillis = yearStartMillis - 1;

            if (instant <= yearEndMillis) {
                return year;
            }

            throw new IllegalArgumentException
                ("Instant too large: " + instant + " > " + yearEndMillis);
        } else {
            int year = getMinYear();
            long yearStartMillis = getYearMillis(year);
            if (instant >= yearStartMillis) {
                return year;
            }

            throw new IllegalArgumentException
                ("Instant too small: " + instant + " < " + yearStartMillis);
        }
    }

    /**
     * @param instant millis from 1970-01-01T00:00:00Z
     */
    final long setYear(long instant, int year) {
        int thisYear = getYear(instant);
        int dayOfYear = getDayOfYear(instant, thisYear);
        int millisOfDay = getMillisOfDay(instant);

        if (dayOfYear > (31 + 28)) { // after Feb 28
            if (isLeapYear(thisYear)) {
                // Current date is Feb 29 or later.
                if (!isLeapYear(year)) {
                    // Moving to a non-leap year, Feb 29 does not exist.
                    dayOfYear--;
                }
            } else {
                // Current date is Mar 01 or later.
                if (isLeapYear(year)) {
                    // Moving to a leap year, account for Feb 29.
                    dayOfYear++;
                }
            }
        }

        instant = getYearMonthDayMillis(year, 1, dayOfYear);
        instant += millisOfDay;

        return instant;
    }

    /**
     * @param millis from 1970-01-01T00:00:00Z
     */
    final int getMonthOfYear(long millis) {
        return getMonthOfYear(millis, getYear(millis));
    }

    /**
     * @param millis from 1970-01-01T00:00:00Z
     * @param year precalculated year of millis
     */
    final int getMonthOfYear(long millis, int year) {
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
    final int getDayOfMonth(long millis) {
        int year = getYear(millis);
        int month = getMonthOfYear(millis, year);
        return getDayOfMonth(millis, year, month);
    }

    /**
     * @param millis from 1970-01-01T00:00:00Z
     * @param year precalculated year of millis
     */
    final int getDayOfMonth(long millis, int year) {
        int month = getMonthOfYear(millis, year);
        return getDayOfMonth(millis, year, month);
    }

    /**
     * @param millis from 1970-01-01T00:00:00Z
     * @param year precalculated year of millis
     * @param month precalculated month of millis
     */
    final int getDayOfMonth(long millis, int year, int month) {
        long dateMillis = getYearMillis(year);
        if (month > 1) {
            dateMillis += getTotalMillisByYearMonth(year, month - 1);
        }
        return (int) ((millis - dateMillis) / DateTimeConstants.MILLIS_PER_DAY) + 1;
    }

    /**
     * @param instant millis from 1970-01-01T00:00:00Z
     */
    final int getDayOfYear(long instant) {
        return getDayOfYear(instant, getYear(instant));
    }

    /**
     * @param instant millis from 1970-01-01T00:00:00Z
     * @param year precalculated year of millis
     */
    final int getDayOfYear(long instant, int year) {
        long yearStart = getYearMillis(year);
        return (int) ((instant - yearStart) / DateTimeConstants.MILLIS_PER_DAY) + 1;
    }

    /**
     * @param instant millis from 1970-01-01T00:00:00Z
     */
    final int getWeekyear(long instant) {
        int year = getYear(instant);
        int week = getWeekOfWeekyear(instant, year);
        if (week == 1) {
            return getYear(instant + DateTimeConstants.MILLIS_PER_WEEK);
        } else if (week > 51) {
            return getYear(instant - (2 * DateTimeConstants.MILLIS_PER_WEEK));
        } else {
            return year;
        }
    }

    /**
     * @param instant millis from 1970-01-01T00:00:00Z
     */
    final int getWeekOfWeekyear(long instant) {
        return getWeekOfWeekyear(instant, getYear(instant));
    }

    /**
     * @param instant millis from 1970-01-01T00:00:00Z
     * @param year precalculated year of millis
     */
    final int getWeekOfWeekyear(long instant, int year) {
        long firstWeekMillis1 = getFirstWeekOfYearMillis(year);
        if (instant < firstWeekMillis1) {
            return getWeeksInYear(year - 1);
        }
        long firstWeekMillis2 = getFirstWeekOfYearMillis(year + 1);
        if (instant >= firstWeekMillis2) {
            return 1;
        }
        return (int) ((instant - firstWeekMillis1) / DateTimeConstants.MILLIS_PER_WEEK) + 1;
    }

    /**
     * @param instant millis from 1970-01-01T00:00:00Z
     */
    final int getDayOfWeek(long instant) {
        // 1970-01-01 is day of week 4, Thursday.

        long daysSince19700101;
        if (instant >= 0) {
            daysSince19700101 = instant / DateTimeConstants.MILLIS_PER_DAY;
        } else {
            daysSince19700101 = (instant - (DateTimeConstants.MILLIS_PER_DAY - 1))
                / DateTimeConstants.MILLIS_PER_DAY;
            if (daysSince19700101 < -3) {
                return 7 + (int) ((daysSince19700101 + 4) % 7);
            }
        }

        return 1 + (int) ((daysSince19700101 + 3) % 7);
    }

    /**
     * @param instant millis from 1970-01-01T00:00:00Z
     */
    final int getMillisOfDay(long instant) {
        if (instant >= 0) {
            return (int) (instant % DateTimeConstants.MILLIS_PER_DAY);
        } else {
            return (DateTimeConstants.MILLIS_PER_DAY - 1)
                + (int) ((instant + 1) % DateTimeConstants.MILLIS_PER_DAY);
        }
    }

    long getDateMidnightMillis(int year, int monthOfYear, int dayOfMonth)
        throws IllegalArgumentException
    {
        FieldUtils.verifyValueBounds("year", year, getMinYear(), getMaxYear());
        FieldUtils.verifyValueBounds("monthOfYear", monthOfYear, 1, 12);

        boolean isLeap = isLeapYear(year);

        FieldUtils.verifyValueBounds("dayOfMonth", dayOfMonth, 1,
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

    abstract boolean isLeapYear(int year);

    abstract long calculateFirstDayOfYearMillis(int year);

    abstract int getMinYear();

    abstract int getMaxYear();

    abstract long getAverageMillisPerYear();

    abstract long getAverageMillisPerMonth();

    /**
     * Returns a constant representing the approximate number of milliseconds
     * elapsed from year 0 of this chronology. This constant <em>must</em> be
     * defined as:
     * <pre>
     *    yearAtEpoch * averageMillisPerYear + millisOfYearAtEpoch
     * <pre>
     * where epoch is 1970-01-01 (Gregorian).
     */
    abstract long getApproxMillisAtEpoch();

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
        private static final long serialVersionUID = 581601443656929254L;

        HalfdayField() {
            super(DateTimeFieldType.halfdayOfDay(), cHalfdaysField, cDaysField);
        }

        public String getAsText(int fieldValue, Locale locale) {
            return GJLocaleSymbols.forLocale(locale).halfdayValueToText(fieldValue);
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
