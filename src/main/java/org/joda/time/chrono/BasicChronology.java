/*
 *  Copyright 2001-2013 Stephen Colebourne
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
import org.joda.time.field.OffsetDateTimeField;
import org.joda.time.field.PreciseDateTimeField;
import org.joda.time.field.PreciseDurationField;
import org.joda.time.field.RemainderDateTimeField;
import org.joda.time.field.ZeroIsMaxDateTimeField;

/**
 * Abstract implementation for calendar systems that use a typical
 * day/month/year/leapYear model.
 * Most of the utility methods required by subclasses are package-private,
 * reflecting the intention that they be defined in the same package.
 * <p>
 * BasicChronology is thread-safe and immutable, and all subclasses must
 * be as well.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @author Guy Allard
 * @since 1.2, renamed from BaseGJChronology
 */
abstract class BasicChronology extends AssembledChronology {

    /** Serialization lock */
    private static final long serialVersionUID = 8283225332206808863L;

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

    private static final int CACHE_SIZE = 1 << 10;
    private static final int CACHE_MASK = CACHE_SIZE - 1;

    private transient final YearInfo[] iYearInfoCache = new YearInfo[CACHE_SIZE];

    private final int iMinDaysInFirstWeek;

    BasicChronology(Chronology base, Object param, int minDaysInFirstWeek) {
        super(base, param);

        if (minDaysInFirstWeek < 1 || minDaysInFirstWeek > 7) {
            throw new IllegalArgumentException
                ("Invalid min days in first week: " + minDaysInFirstWeek);
        }

        iMinDaysInFirstWeek = minDaysInFirstWeek;
    }

    public DateTimeZone getZone() {
        Chronology base;
        if ((base = getBase()) != null) {
            return base.getZone();
        }
        return DateTimeZone.UTC;
    }

    public long getDateTimeMillis(
            int year, int monthOfYear, int dayOfMonth, int millisOfDay)
            throws IllegalArgumentException {
        Chronology base;
        if ((base = getBase()) != null) {
            return base.getDateTimeMillis(year, monthOfYear, dayOfMonth, millisOfDay);
        }

        FieldUtils.verifyValueBounds
            (DateTimeFieldType.millisOfDay(), millisOfDay, 0, DateTimeConstants.MILLIS_PER_DAY - 1);
        return getDateMidnightMillis(year, monthOfYear, dayOfMonth) + millisOfDay;
    }

    public long getDateTimeMillis(
            int year, int monthOfYear, int dayOfMonth,
            int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond)
            throws IllegalArgumentException {
        Chronology base;
        if ((base = getBase()) != null) {
            return base.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                          hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        }

        FieldUtils.verifyValueBounds(DateTimeFieldType.hourOfDay(), hourOfDay, 0, 23);
        FieldUtils.verifyValueBounds(DateTimeFieldType.minuteOfHour(), minuteOfHour, 0, 59);
        FieldUtils.verifyValueBounds(DateTimeFieldType.secondOfMinute(), secondOfMinute, 0, 59);
        FieldUtils.verifyValueBounds(DateTimeFieldType.millisOfSecond(), millisOfSecond, 0, 999);

        return getDateMidnightMillis(year, monthOfYear, dayOfMonth)
            + hourOfDay * DateTimeConstants.MILLIS_PER_HOUR
            + minuteOfHour * DateTimeConstants.MILLIS_PER_MINUTE
            + secondOfMinute * DateTimeConstants.MILLIS_PER_SECOND
            + millisOfSecond;
    }

    public int getMinimumDaysInFirstWeek() {
        return iMinDaysInFirstWeek;
    }

    //-----------------------------------------------------------------------
    /**
     * Checks if this chronology instance equals another.
     * 
     * @param obj  the object to compare to
     * @return true if equal
     * @since 1.6
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            BasicChronology chrono = (BasicChronology) obj;
            return getMinimumDaysInFirstWeek() == chrono.getMinimumDaysInFirstWeek() &&
                    getZone().equals(chrono.getZone());
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
        return getClass().getName().hashCode() * 11 + getZone().hashCode() + getMinimumDaysInFirstWeek();
    }

    // Output
    //-----------------------------------------------------------------------
    /**
     * Gets a debugging toString.
     * 
     * @return a debugging string
     */
    public String toString() {
        StringBuilder sb = new StringBuilder(60);
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

        fields.year = new BasicYearDateTimeField(this);
        fields.yearOfEra = new GJYearOfEraDateTimeField(fields.year, this);

        // Define one-based centuryOfEra and yearOfCentury.
        DateTimeField field = new OffsetDateTimeField(
            fields.yearOfEra, 99);
        fields.centuryOfEra = new DividedDateTimeField(
            field, DateTimeFieldType.centuryOfEra(), 100);
        fields.centuries = fields.centuryOfEra.getDurationField();
        
        field = new RemainderDateTimeField(
            (DividedDateTimeField) fields.centuryOfEra);
        fields.yearOfCentury = new OffsetDateTimeField(
            field, DateTimeFieldType.yearOfCentury(), 1);

        fields.era = new GJEraDateTimeField(this);
        fields.dayOfWeek = new GJDayOfWeekDateTimeField(this, fields.days);
        fields.dayOfMonth = new BasicDayOfMonthDateTimeField(this, fields.days);
        fields.dayOfYear = new BasicDayOfYearDateTimeField(this, fields.days);
        fields.monthOfYear = new GJMonthOfYearDateTimeField(this);
        fields.weekyear = new BasicWeekyearDateTimeField(this);
        fields.weekOfWeekyear = new BasicWeekOfWeekyearDateTimeField(this, fields.weeks);
        
        field = new RemainderDateTimeField(
            fields.weekyear, fields.centuries, DateTimeFieldType.weekyearOfCentury(), 100);
        fields.weekyearOfCentury = new OffsetDateTimeField(
            field, DateTimeFieldType.weekyearOfCentury(), 1);
        
        // The remaining (imprecise) durations are available from the newly
        // created datetime fields.
        fields.years = fields.year.getDurationField();
        fields.months = fields.monthOfYear.getDurationField();
        fields.weekyears = fields.weekyear.getDurationField();
    }

    //-----------------------------------------------------------------------
    /**
     * Get the number of days in the year.
     *
     * @return 366
     */
    int getDaysInYearMax() {
        return 366;
    }

    /**
     * Get the number of days in the year.
     *
     * @param year  the year to use
     * @return 366 if a leap year, otherwise 365
     */
    int getDaysInYear(int year) {
        return isLeapYear(year) ? 366 : 365;
    }

    /**
     * Get the number of weeks in the year.
     *
     * @param year  the year to use
     * @return number of weeks in the year
     */
    int getWeeksInYear(int year) {
        long firstWeekMillis1 = getFirstWeekOfYearMillis(year);
        long firstWeekMillis2 = getFirstWeekOfYearMillis(year + 1);
        return (int) ((firstWeekMillis2 - firstWeekMillis1) / DateTimeConstants.MILLIS_PER_WEEK);
    }

    /**
     * Get the millis for the first week of a year.
     *
     * @param year  the year to use
     * @return millis
     */
    long getFirstWeekOfYearMillis(int year) {
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
    long getYearMillis(int year) {
        return getYearInfo(year).iFirstDayMillis;
    }

    /**
     * Get the milliseconds for the start of a month.
     *
     * @param year The year to use.
     * @param month The month to use
     * @return millis from 1970-01-01T00:00:00Z
     */
    long getYearMonthMillis(int year, int month) {
        long millis = getYearMillis(year);
        millis += getTotalMillisByYearMonth(year, month);
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
    long getYearMonthDayMillis(int year, int month, int dayOfMonth) {
        long millis = getYearMillis(year);
        millis += getTotalMillisByYearMonth(year, month);
        return millis + (dayOfMonth - 1) * (long)DateTimeConstants.MILLIS_PER_DAY;
    }
    
    /**
     * @param instant millis from 1970-01-01T00:00:00Z
     */
    int getYear(long instant) {
        // Get an initial estimate of the year, and the millis value that
        // represents the start of that year. Then verify estimate and fix if
        // necessary.

        // Initial estimate uses values divided by two to avoid overflow.
        long unitMillis = getAverageMillisPerYearDividedByTwo();
        long i2 = (instant >> 1) + getApproxMillisAtEpochDividedByTwo();
        if (i2 < 0) {
            i2 = i2 - unitMillis + 1;
        }
        int year = (int) (i2 / unitMillis);

        long yearStart = getYearMillis(year);
        long diff = instant - yearStart;

        if (diff < 0) {
            year--;
        } else if (diff >= DateTimeConstants.MILLIS_PER_DAY * 365L) {
            // One year may need to be added to fix estimate.
            long oneYear;
            if (isLeapYear(year)) {
                oneYear = DateTimeConstants.MILLIS_PER_DAY * 366L;
            } else {
                oneYear = DateTimeConstants.MILLIS_PER_DAY * 365L;
            }

            yearStart += oneYear;

            if (yearStart <= instant) {
                // Didn't go too far, so actually add one year.
                year++;
            }
        }

        return year;
    }

    /**
     * @param millis from 1970-01-01T00:00:00Z
     */
    int getMonthOfYear(long millis) {
        return getMonthOfYear(millis, getYear(millis));
    }

    /**
     * @param millis from 1970-01-01T00:00:00Z
     * @param year precalculated year of millis
     */
    abstract int getMonthOfYear(long millis, int year);

    /**
     * @param millis from 1970-01-01T00:00:00Z
     */
    int getDayOfMonth(long millis) {
        int year = getYear(millis);
        int month = getMonthOfYear(millis, year);
        return getDayOfMonth(millis, year, month);
    }

    /**
     * @param millis from 1970-01-01T00:00:00Z
     * @param year precalculated year of millis
     */
    int getDayOfMonth(long millis, int year) {
        int month = getMonthOfYear(millis, year);
        return getDayOfMonth(millis, year, month);
    }

    /**
     * @param millis from 1970-01-01T00:00:00Z
     * @param year precalculated year of millis
     * @param month precalculated month of millis
     */
    int getDayOfMonth(long millis, int year, int month) {
        long dateMillis = getYearMillis(year);
        dateMillis += getTotalMillisByYearMonth(year, month);
        return (int) ((millis - dateMillis) / DateTimeConstants.MILLIS_PER_DAY) + 1;
    }

    /**
     * @param instant millis from 1970-01-01T00:00:00Z
     */
    int getDayOfYear(long instant) {
        return getDayOfYear(instant, getYear(instant));
    }

    /**
     * @param instant millis from 1970-01-01T00:00:00Z
     * @param year precalculated year of millis
     */
    int getDayOfYear(long instant, int year) {
        long yearStart = getYearMillis(year);
        return (int) ((instant - yearStart) / DateTimeConstants.MILLIS_PER_DAY) + 1;
    }

    /**
     * @param instant millis from 1970-01-01T00:00:00Z
     */
    int getWeekyear(long instant) {
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
    int getWeekOfWeekyear(long instant) {
        return getWeekOfWeekyear(instant, getYear(instant));
    }

    /**
     * @param instant millis from 1970-01-01T00:00:00Z
     * @param year precalculated year of millis
     */
    int getWeekOfWeekyear(long instant, int year) {
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
    int getDayOfWeek(long instant) {
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
    int getMillisOfDay(long instant) {
        if (instant >= 0) {
            return (int) (instant % DateTimeConstants.MILLIS_PER_DAY);
        } else {
            return (DateTimeConstants.MILLIS_PER_DAY - 1)
                + (int) ((instant + 1) % DateTimeConstants.MILLIS_PER_DAY);
        }
    }

    /**
     * Gets the maximum number of days in any month.
     * 
     * @return 31
     */
    int getDaysInMonthMax() {
        return 31;
    }

    /**
     * Gets the maximum number of days in the month specified by the instant.
     * 
     * @param instant  millis from 1970-01-01T00:00:00Z
     * @return the maximum number of days in the month
     */
    int getDaysInMonthMax(long instant) {
        int thisYear = getYear(instant);
        int thisMonth = getMonthOfYear(instant, thisYear);
        return getDaysInYearMonth(thisYear, thisMonth);
    }

    /**
     * Gets the maximum number of days in the month specified by the instant.
     * The value represents what the user is trying to set, and can be
     * used to optimise this method.
     * 
     * @param instant  millis from 1970-01-01T00:00:00Z
     * @param value  the value being set
     * @return the maximum number of days in the month
     */
    int getDaysInMonthMaxForSet(long instant, int value) {
        return getDaysInMonthMax(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the milliseconds for a date at midnight.
     * 
     * @param year  the year
     * @param monthOfYear  the month
     * @param dayOfMonth  the day
     * @return the milliseconds
     */
    long getDateMidnightMillis(int year, int monthOfYear, int dayOfMonth) {
        FieldUtils.verifyValueBounds(DateTimeFieldType.year(), year, getMinYear(), getMaxYear());
        FieldUtils.verifyValueBounds(DateTimeFieldType.monthOfYear(), monthOfYear, 1, getMaxMonth(year));
        FieldUtils.verifyValueBounds(DateTimeFieldType.dayOfMonth(), dayOfMonth, 1, getDaysInYearMonth(year, monthOfYear));
        return getYearMonthDayMillis(year, monthOfYear, dayOfMonth);
    }

    /**
     * Gets the difference between the two instants in years.
     * 
     * @param minuendInstant  the first instant
     * @param subtrahendInstant  the second instant
     * @return the difference
     */
    abstract long getYearDifference(long minuendInstant, long subtrahendInstant);

    /**
     * Is the specified year a leap year?
     * 
     * @param year  the year to test
     * @return true if leap
     */
    abstract boolean isLeapYear(int year);

    /**
     * Gets the number of days in the specified month and year.
     * 
     * @param year  the year
     * @param month  the month
     * @return the number of days
     */
    abstract int getDaysInYearMonth(int year, int month);

    /**
     * Gets the maximum days in the specified month.
     * 
     * @param month  the month
     * @return the max days
     */
    abstract int getDaysInMonthMax(int month);

    /**
     * Gets the total number of millis elapsed in this year at the start
     * of the specified month, such as zero for month 1.
     * 
     * @param year  the year
     * @param month  the month
     * @return the elapsed millis at the start of the month
     */
    abstract long getTotalMillisByYearMonth(int year, int month);

    /**
     * Gets the millisecond value of the first day of the year.
     * 
     * @return the milliseconds for the first of the year
     */
    abstract long calculateFirstDayOfYearMillis(int year);

    /**
     * Gets the minimum supported year.
     * 
     * @return the year
     */
    abstract int getMinYear();

    /**
     * Gets the maximum supported year.
     * 
     * @return the year
     */
    abstract int getMaxYear();

    /**
     * Gets the maximum month for the specified year.
     * This implementation calls getMaxMonth().
     * 
     * @param year  the year
     * @return the maximum month value
     */
    int getMaxMonth(int year) {
        return getMaxMonth();
    }

    /**
     * Gets the maximum number of months.
     * 
     * @return 12
     */
    int getMaxMonth() {
        return 12;
    }

    /**
     * Gets an average value for the milliseconds per year.
     * 
     * @return the millis per year
     */
    abstract long getAverageMillisPerYear();

    /**
     * Gets an average value for the milliseconds per year, divided by two.
     * 
     * @return the millis per year divided by two
     */
    abstract long getAverageMillisPerYearDividedByTwo();

    /**
     * Gets an average value for the milliseconds per month.
     * 
     * @return the millis per month
     */
    abstract long getAverageMillisPerMonth();

    /**
     * Returns a constant representing the approximate number of milliseconds
     * elapsed from year 0 of this chronology, divided by two. This constant
     * <em>must</em> be defined as:
     * <pre>
     *    (yearAtEpoch * averageMillisPerYear + millisOfYearAtEpoch) / 2
     * </pre>
     * where epoch is 1970-01-01 (Gregorian).
     */
    abstract long getApproxMillisAtEpochDividedByTwo();

    /**
     * Sets the year from an instant and year.
     * 
     * @param instant  millis from 1970-01-01T00:00:00Z
     * @param year  the year to set
     * @return the updated millis
     */
    abstract long setYear(long instant, int year);

    //-----------------------------------------------------------------------
    // Although accessed by multiple threads, this method doesn't need to be synchronized.
    private YearInfo getYearInfo(int year) {
        YearInfo info = iYearInfoCache[year & CACHE_MASK];
        if (info == null || info.iYear != year) {
            info = new YearInfo(year, calculateFirstDayOfYearMillis(year));
            iYearInfoCache[year & CACHE_MASK] = info;
        }
        return info;
    }

    private static class HalfdayField extends PreciseDateTimeField {
        @SuppressWarnings("unused")
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
