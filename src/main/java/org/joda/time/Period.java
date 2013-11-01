/*
 *  Copyright 2001-2010 Stephen Colebourne
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
package org.joda.time;

import java.io.Serializable;

import org.joda.convert.FromString;
import org.joda.time.base.BasePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

/**
 * An immutable time period specifying a set of duration field values.
 * <p>
 * A time period is divided into a number of fields, such as hours and seconds.
 * Which fields are supported is defined by the PeriodType class.
 * The default is the standard period type, which supports years, months, weeks, days,
 * hours, minutes, seconds and millis.
 * <p>
 * When this time period is added to an instant, the effect is of adding each field in turn.
 * As a result, this takes into account daylight savings time.
 * Adding a time period of 1 day to the day before daylight savings starts will only add
 * 23 hours rather than 24 to ensure that the time remains the same.
 * If this is not the behaviour you want, then see {@link Duration}.
 * <p>
 * The definition of a period also affects the equals method. A period of 1
 * day is not equal to a period of 24 hours, nor 1 hour equal to 60 minutes.
 * This is because periods represent an abstracted definition of a time period
 * (eg. a day may not actually be 24 hours, it might be 23 or 25 at daylight
 * savings boundary). To compare the actual duration of two periods, convert
 * both to durations using toDuration, an operation that emphasises that the
 * result may differ according to the date you choose.
 * <p>
 * Period is thread-safe and immutable, provided that the PeriodType is as well.
 * All standard PeriodType classes supplied are thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 * @see MutablePeriod
 */
public final class Period
        extends BasePeriod
        implements ReadablePeriod, Serializable {

    /**
     * A period of zero length and standard period type.
     * @since 1.4
     */
    public static final Period ZERO = new Period();

    /** Serialization version */
    private static final long serialVersionUID = 741052353876488155L;

    //-----------------------------------------------------------------------
    /**
     * Parses a {@code Period} from the specified string.
     * <p>
     * This uses {@link ISOPeriodFormat#standard()}.
     * 
     * @param str  the string to parse, not null
     * @since 2.0
     */
    @FromString
    public static Period parse(String str) {
        return parse(str, ISOPeriodFormat.standard());
    }

    /**
     * Parses a {@code Period} from the specified string using a formatter.
     * 
     * @param str  the string to parse, not null
     * @param formatter  the formatter to use, not null
     * @since 2.0
     */
    public static Period parse(String str, PeriodFormatter formatter) {
        return formatter.parsePeriod(str);
    }

    //-----------------------------------------------------------------------
    /**
     * Create a period with a specified number of years.
     * <p>
     * The standard period type is used, thus you can add other fields such
     * as months or days using the <code>withXxx()</code> methods.
     * For example, <code>Period.years(2).withMonths(6);</code>
     * <p>
     * If you want a year-based period that cannot have other fields added,
     * then you should consider using {@link Years}.
     *
     * @param years  the amount of years in this period
     * @return the period
     */
    public static Period years(int years) {
        return new Period(new int[] {years, 0, 0, 0, 0, 0, 0, 0, 0}, PeriodType.standard());
    }

    /**
     * Create a period with a specified number of months.
     * <p>
     * The standard period type is used, thus you can add other fields such
     * as years or days using the <code>withXxx()</code> methods.
     * For example, <code>Period.months(2).withDays(6);</code>
     * <p>
     * If you want a month-based period that cannot have other fields added,
     * then you should consider using {@link Months}.
     *
     * @param months  the amount of months in this period
     * @return the period
     */
    public static Period months(int months) {
        return new Period(new int[] {0, months, 0, 0, 0, 0, 0, 0}, PeriodType.standard());
    }

    /**
     * Create a period with a specified number of weeks.
     * <p>
     * The standard period type is used, thus you can add other fields such
     * as months or days using the <code>withXxx()</code> methods.
     * For example, <code>Period.weeks(2).withDays(6);</code>
     * <p>
     * If you want a week-based period that cannot have other fields added,
     * then you should consider using {@link Weeks}.
     *
     * @param weeks  the amount of weeks in this period
     * @return the period
     */
    public static Period weeks(int weeks) {
        return new Period(new int[] {0, 0, weeks, 0, 0, 0, 0, 0}, PeriodType.standard());
    }

    /**
     * Create a period with a specified number of days.
     * <p>
     * The standard period type is used, thus you can add other fields such
     * as months or weeks using the <code>withXxx()</code> methods.
     * For example, <code>Period.days(2).withHours(6);</code>
     * <p>
     * If you want a day-based period that cannot have other fields added,
     * then you should consider using {@link Days}.
     *
     * @param days  the amount of days in this period
     * @return the period
     */
    public static Period days(int days) {
        return new Period(new int[] {0, 0, 0, days, 0, 0, 0, 0}, PeriodType.standard());
    }

    /**
     * Create a period with a specified number of hours.
     * <p>
     * The standard period type is used, thus you can add other fields such
     * as months or days using the <code>withXxx()</code> methods.
     * For example, <code>Period.hours(2).withMinutes(30);</code>
     * <p>
     * If you want a hour-based period that cannot have other fields added,
     * then you should consider using {@link Hours}.
     *
     * @param hours  the amount of hours in this period
     * @return the period
     */
    public static Period hours(int hours) {
        return new Period(new int[] {0, 0, 0, 0, hours, 0, 0, 0}, PeriodType.standard());
    }

    /**
     * Create a period with a specified number of minutes.
     * <p>
     * The standard period type is used, thus you can add other fields such
     * as days or hours using the <code>withXxx()</code> methods.
     * For example, <code>Period.minutes(2).withSeconds(30);</code>
     * <p>
     * If you want a minute-based period that cannot have other fields added,
     * then you should consider using {@link Minutes}.
     *
     * @param minutes  the amount of minutes in this period
     * @return the period
     */
    public static Period minutes(int minutes) {
        return new Period(new int[] {0, 0, 0, 0, 0, minutes, 0, 0}, PeriodType.standard());
    }

    /**
     * Create a period with a specified number of seconds.
     * <p>
     * The standard period type is used, thus you can add other fields such
     * as days or hours using the <code>withXxx()</code> methods.
     * For example, <code>Period.seconds(2).withMillis(30);</code>
     * <p>
     * If you want a second-based period that cannot have other fields added,
     * then you should consider using {@link Seconds}.
     *
     * @param seconds  the amount of seconds in this period
     * @return the period
     */
    public static Period seconds(int seconds) {
        return new Period(new int[] {0, 0, 0, 0, 0, 0, seconds, 0}, PeriodType.standard());
    }

    /**
     * Create a period with a specified number of millis.
     * <p>
     * The standard period type is used, thus you can add other fields such
     * as days or hours using the <code>withXxx()</code> methods.
     * For example, <code>Period.millis(20).withSeconds(30);</code>
     *
     * @param millis  the amount of millis in this period
     * @return the period
     */
    public static Period millis(int millis) {
        return new Period(new int[] {0, 0, 0, 0, 0, 0, 0, millis}, PeriodType.standard());
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a period from two partially specified times, calculating
     * by field difference.
     * <p>
     * The two partials must contain the same fields, thus you can specify
     * two <code>LocalDate</code> objects, or two <code>LocalTime</code> objects,
     * but not one of each. Also, the partial may not contain overlapping
     * fields, such as dayOfWeek and dayOfMonth.
     * <p>
     * Calculation by field difference works by extracting the difference
     * one field at a time and not wrapping into other fields.
     * Thus 2005-06-09/2007-04-12 will yield P1Y-2M3D.
     * <p>
     * For example, you have an event that always runs from the 27th of
     * each month to the 2nd of the next month. If you calculate this
     * period using a standard constructor, then you will get between
     * P3D and P6D depending on the month. If you use this method, then
     * you will get P1M-25D. This field-difference based period can
     * be successfully applied to each month of the year to obtain the
     * correct end date for a given start date.
     *
     * @param start  the start of the period, must not be null
     * @param end  the end of the period, must not be null
     * @throws IllegalArgumentException if the partials are null or invalid
     * @since 1.1
     */
    public static Period fieldDifference(ReadablePartial start, ReadablePartial end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("ReadablePartial objects must not be null");
        }
        if (start.size() != end.size()) {
            throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
        }
        DurationFieldType[] types = new DurationFieldType[start.size()];
        int[] values = new int[start.size()];
        for (int i = 0, isize = start.size(); i < isize; i++) {
            if (start.getFieldType(i) != end.getFieldType(i)) {
                throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
            }
            types[i] = start.getFieldType(i).getDurationType();
            if (i > 0 && types[i - 1] == types[i]) {
                throw new IllegalArgumentException("ReadablePartial objects must not have overlapping fields");
            }
            values[i] = end.getValue(i) - start.getValue(i);
        }
        return new Period(values, PeriodType.forFields(types));
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a new empty period with the standard set of fields.
     * <p>
     * One way to initialise a period is as follows:
     * <pre>
     * Period = new Period().withYears(6).withMonths(3).withSeconds(23);
     * </pre>
     * Bear in mind that this creates four period instances in total, three of
     * which are immediately discarded.
     * The alterative is more efficient, but less readable:
     * <pre>
     * Period = new Period(6, 3, 0, 0, 0, 0, 23, 0);
     * </pre>
     * The following is also slightly less wasteful:
     * <pre>
     * Period = Period.years(6).withMonths(3).withSeconds(23);
     * </pre>
     */
    public Period() {
        super(0L, null, null);
    }

    /**
     * Create a period from a set of field values using the standard set of fields.
     * Note that the parameters specify the time fields hours, minutes,
     * seconds and millis, not the date fields.
     *
     * @param hours  amount of hours in this period
     * @param minutes  amount of minutes in this period
     * @param seconds  amount of seconds in this period
     * @param millis  amount of milliseconds in this period
     */
    public Period(int hours, int minutes, int seconds, int millis) {
        super(0, 0, 0, 0, hours, minutes, seconds, millis, PeriodType.standard());
    }

    /**
     * Create a period from a set of field values using the standard set of fields.
     *
     * @param years  amount of years in this period
     * @param months  amount of months in this period
     * @param weeks  amount of weeks in this period
     * @param days  amount of days in this period
     * @param hours  amount of hours in this period
     * @param minutes  amount of minutes in this period
     * @param seconds  amount of seconds in this period
     * @param millis  amount of milliseconds in this period
     */
    public Period(int years, int months, int weeks, int days,
                  int hours, int minutes, int seconds, int millis) {
        super(years, months, weeks, days, hours, minutes, seconds, millis, PeriodType.standard());
    }

    /**
     * Create a period from a set of field values.
     * <p>
     * There is usually little need to use this constructor.
     * The period type is used primarily to define how to split an interval into a period.
     * As this constructor already is split, the period type does no real work.
     *
     * @param years  amount of years in this period, which must be zero if unsupported
     * @param months  amount of months in this period, which must be zero if unsupported
     * @param weeks  amount of weeks in this period, which must be zero if unsupported
     * @param days  amount of days in this period, which must be zero if unsupported
     * @param hours  amount of hours in this period, which must be zero if unsupported
     * @param minutes  amount of minutes in this period, which must be zero if unsupported
     * @param seconds  amount of seconds in this period, which must be zero if unsupported
     * @param millis  amount of milliseconds in this period, which must be zero if unsupported
     * @param type  which set of fields this period supports, null means AllType
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    public Period(int years, int months, int weeks, int days,
                    int hours, int minutes, int seconds, int millis, PeriodType type) {
        super(years, months, weeks, days, hours, minutes, seconds, millis, type);
    }

    /**
     * Creates a period from the given millisecond duration using the standard
     * set of fields.
     * <p>
     * Only precise fields in the period type will be used.
     * For the standard period type this is the time fields only.
     * Thus the year, month, week and day fields will not be populated.
     * <p>
     * If the duration is small, less than one day, then this method will perform
     * as you might expect and split the fields evenly.
     * <p>
     * If the duration is larger than one day then all the remaining duration will
     * be stored in the largest available precise field, hours in this case.
     * <p>
     * For example, a duration equal to (365 + 60 + 5) days will be converted to
     * ((365 + 60 + 5) * 24) hours by this constructor.
     * <p>
     * For more control over the conversion process, you have two options:
     * <ul>
     * <li>convert the duration to an {@link Interval}, and from there obtain the period
     * <li>specify a period type that contains precise definitions of the day and larger
     * fields, such as UTC
     * </ul>
     *
     * @param duration  the duration, in milliseconds
     */
    public Period(long duration) {
        super(duration);
    }

    /**
     * Creates a period from the given millisecond duration.
     * <p>
     * Only precise fields in the period type will be used.
     * Imprecise fields will not be populated.
     * <p>
     * If the duration is small then this method will perform
     * as you might expect and split the fields evenly.
     * <p>
     * If the duration is large then all the remaining duration will
     * be stored in the largest available precise field.
     * For details as to which fields are precise, review the period type javadoc.
     *
     * @param duration  the duration, in milliseconds
     * @param type  which set of fields this period supports, null means standard
     */
    public Period(long duration, PeriodType type) {
        super(duration, type, null);
    }

    /**
     * Creates a period from the given millisecond duration using the standard
     * set of fields.
     * <p>
     * Only precise fields in the period type will be used.
     * Imprecise fields will not be populated.
     * <p>
     * If the duration is small then this method will perform
     * as you might expect and split the fields evenly.
     * <p>
     * If the duration is large then all the remaining duration will
     * be stored in the largest available precise field.
     * For details as to which fields are precise, review the period type javadoc.
     *
     * @param duration  the duration, in milliseconds
     * @param chronology  the chronology to use to split the duration, null means ISO default
     */
    public Period(long duration, Chronology chronology) {
        super(duration, null, chronology);
    }

    /**
     * Creates a period from the given millisecond duration.
     * <p>
     * Only precise fields in the period type will be used.
     * Imprecise fields will not be populated.
     * <p>
     * If the duration is small then this method will perform
     * as you might expect and split the fields evenly.
     * <p>
     * If the duration is large then all the remaining duration will
     * be stored in the largest available precise field.
     * For details as to which fields are precise, review the period type javadoc.
     *
     * @param duration  the duration, in milliseconds
     * @param type  which set of fields this period supports, null means standard
     * @param chronology  the chronology to use to split the duration, null means ISO default
     */
    public Period(long duration, PeriodType type, Chronology chronology) {
        super(duration, type, chronology);
    }

    /**
     * Creates a period from the given interval endpoints using the standard
     * set of fields.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     */
    public Period(long startInstant, long endInstant) {
        super(startInstant, endInstant, null, null);
    }

    /**
     * Creates a period from the given interval endpoints.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     * @param type  which set of fields this period supports, null means standard
     */
    public Period(long startInstant, long endInstant, PeriodType type) {
        super(startInstant, endInstant, type, null);
    }

    /**
     * Creates a period from the given interval endpoints using the standard
     * set of fields.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     * @param chrono  the chronology to use, null means ISO in default zone
     */
    public Period(long startInstant, long endInstant, Chronology chrono) {
        super(startInstant, endInstant, null, chrono);
    }

    /**
     * Creates a period from the given interval endpoints.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     * @param type  which set of fields this period supports, null means standard
     * @param chrono  the chronology to use, null means ISO in default zone
     */
    public Period(long startInstant, long endInstant, PeriodType type, Chronology chrono) {
        super(startInstant, endInstant, type, chrono);
    }

    /**
     * Creates a period between the given instants using the standard set of fields.
     * <p>
     * Most calculations performed by this method have obvious results.
     * The special case is where the calculation is from a "long" month to a "short" month.
     * Here, the result favours increasing the months field rather than the days.
     * For example, 2013-01-31 to 2013-02-28 is treated as one whole month.
     * By contrast, 2013-01-31 to 2013-03-30 is treated as one month and 30 days
     * (exposed as 4 weeks and 2 days).
     * The results are explained by considering that the start date plus the
     * calculated period result in the end date.
     *
     * @param startInstant  interval start, null means now
     * @param endInstant  interval end, null means now
     */
    public Period(ReadableInstant startInstant, ReadableInstant endInstant) {
        super(startInstant, endInstant, null);
    }

    /**
     * Creates a period between the given instants.
     * <p>
     * Most calculations performed by this method have obvious results.
     * The special case is where the calculation is from a "long" month to a "short" month.
     * Here, the result favours increasing the months field rather than the days.
     * For example, 2013-01-31 to 2013-02-28 is treated as one whole month.
     * By contrast, 2013-01-31 to 2013-03-30 is treated as one month and 30 days.
     * The results are explained by considering that the start date plus the
     * calculated period result in the end date.
     *
     * @param startInstant  interval start, null means now
     * @param endInstant  interval end, null means now
     * @param type  which set of fields this period supports, null means standard
     */
    public Period(ReadableInstant startInstant, ReadableInstant endInstant, PeriodType type) {
        super(startInstant, endInstant, type);
    }

    /**
     * Creates a period from two partially specified times.
     * <p>
     * The two partials must contain the same fields, thus you can specify
     * two <code>LocalDate</code> objects, or two <code>LocalTime</code> objects,
     * but not one of each.
     * As these are Partial objects, time zones have no effect on the result.
     * <p>
     * The two partials must also both be contiguous - see
     * {@link DateTimeUtils#isContiguous(ReadablePartial)} for a definition.
     * Both <code>LocalDate</code> and <code>LocalTime</code> are contiguous.
     * <p>
     * Most calculations performed by this method have obvious results.
     * The special case is where the calculation is from a "long" month to a "short" month.
     * Here, the result favours increasing the months field rather than the days.
     * For example, 2013-01-31 to 2013-02-28 is treated as one whole month.
     * By contrast, 2013-01-31 to 2013-03-30 is treated as one month and 30 days
     * (exposed as 4 weeks and 2 days).
     * The results are explained by considering that the start date plus the
     * calculated period result in the end date.
     * <p>
     * An alternative way of constructing a Period from two Partials
     * is {@link #fieldDifference(ReadablePartial, ReadablePartial)}.
     * That method handles all kinds of partials.
     *
     * @param start  the start of the period, must not be null
     * @param end  the end of the period, must not be null
     * @throws IllegalArgumentException if the partials are null or invalid
     * @since 1.1
     */
    public Period(ReadablePartial start, ReadablePartial end) {
        super(start, end, null);
    }

    /**
     * Creates a period from two partially specified times.
     * <p>
     * The two partials must contain the same fields, thus you can specify
     * two <code>LocalDate</code> objects, or two <code>LocalTime</code> objects,
     * but not one of each.
     * As these are Partial objects, time zones have no effect on the result.
     * <p>
     * The two partials must also both be contiguous - see
     * {@link DateTimeUtils#isContiguous(ReadablePartial)} for a definition.
     * Both <code>LocalDate</code> and <code>LocalTime</code> are contiguous.
     * <p>
     * Most calculations performed by this method have obvious results.
     * The special case is where the calculation is from a "long" month to a "short" month.
     * Here, the result favours increasing the months field rather than the days.
     * For example, 2013-01-31 to 2013-02-28 is treated as one whole month.
     * By contrast, 2013-01-31 to 2013-03-30 is treated as one month and 30 days.
     * The results are explained by considering that the start date plus the
     * calculated period result in the end date.
     * <p>
     * An alternative way of constructing a Period from two Partials
     * is {@link #fieldDifference(ReadablePartial, ReadablePartial)}.
     * That method handles all kinds of partials.
     *
     * @param start  the start of the period, must not be null
     * @param end  the end of the period, must not be null
     * @param type  which set of fields this period supports, null means standard
     * @throws IllegalArgumentException if the partials are null or invalid
     * @since 1.1
     */
    public Period(ReadablePartial start, ReadablePartial end, PeriodType type) {
        super(start, end, type);
    }

    /**
     * Creates a period from the given start point and the duration.
     *
     * @param startInstant  the interval start, null means now
     * @param duration  the duration of the interval, null means zero-length
     */
    public Period(ReadableInstant startInstant, ReadableDuration duration) {
        super(startInstant, duration, null);
    }

    /**
     * Creates a period from the given start point and the duration.
     *
     * @param startInstant  the interval start, null means now
     * @param duration  the duration of the interval, null means zero-length
     * @param type  which set of fields this period supports, null means standard
     */
    public Period(ReadableInstant startInstant, ReadableDuration duration, PeriodType type) {
        super(startInstant, duration, type);
    }

    /**
     * Creates a period from the given duration and end point.
     *
     * @param duration  the duration of the interval, null means zero-length
     * @param endInstant  the interval end, null means now
     */
    public Period(ReadableDuration duration, ReadableInstant endInstant) {
        super(duration, endInstant, null);
    }

    /**
     * Creates a period from the given duration and end point.
     *
     * @param duration  the duration of the interval, null means zero-length
     * @param endInstant  the interval end, null means now
     * @param type  which set of fields this period supports, null means standard
     */
    public Period(ReadableDuration duration, ReadableInstant endInstant, PeriodType type) {
        super(duration, endInstant, type);
    }

    /**
     * Creates a period by converting or copying from another object.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadablePeriod, ReadableInterval and String.
     * The String formats are described by {@link ISOPeriodFormat#standard()}.
     *
     * @param period  period to convert
     * @throws IllegalArgumentException if period is invalid
     * @throws UnsupportedOperationException if an unsupported field's value is non-zero
     */
    public Period(Object period) {
        super(period, null, null);
    }

    /**
     * Creates a period by converting or copying from another object.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadablePeriod, ReadableInterval and String.
     * The String formats are described by {@link ISOPeriodFormat#standard()}.
     *
     * @param period  period to convert
     * @param type  which set of fields this period supports, null means use converter
     * @throws IllegalArgumentException if period is invalid
     * @throws UnsupportedOperationException if an unsupported field's value is non-zero
     */
    public Period(Object period, PeriodType type) {
        super(period, type, null);
    }

    /**
     * Creates a period by converting or copying from another object.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadablePeriod, ReadableInterval and String.
     * The String formats are described by {@link ISOPeriodFormat#standard()}.
     *
     * @param period  period to convert
     * @param chrono  the chronology to use, null means ISO in default zone
     * @throws IllegalArgumentException if period is invalid
     * @throws UnsupportedOperationException if an unsupported field's value is non-zero
     */
    public Period(Object period, Chronology chrono) {
        super(period, null, chrono);
    }

    /**
     * Creates a period by converting or copying from another object.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadablePeriod, ReadableInterval and String.
     * The String formats are described by {@link ISOPeriodFormat#standard()}.
     *
     * @param period  period to convert
     * @param type  which set of fields this period supports, null means use converter
     * @param chrono  the chronology to use, null means ISO in default zone
     * @throws IllegalArgumentException if period is invalid
     * @throws UnsupportedOperationException if an unsupported field's value is non-zero
     */
    public Period(Object period, PeriodType type, Chronology chrono) {
        super(period, type, chrono);
    }

    /**
     * Constructor used when we trust ourselves.
     *
     * @param values  the values to use, not null, not cloned
     * @param type  which set of fields this period supports, not null
     */
    private Period(int[] values, PeriodType type) {
        super(values, type);
    }

    //-----------------------------------------------------------------------
    /**
     * Get this period as an immutable <code>Period</code> object
     * by returning <code>this</code>.
     * 
     * @return <code>this</code>
     */
    public Period toPeriod() {
        return this;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the years field part of the period.
     * 
     * @return the number of years in the period, zero if unsupported
     */
    public int getYears() {
        return getPeriodType().getIndexedField(this, PeriodType.YEAR_INDEX);
    }

    /**
     * Gets the months field part of the period.
     * 
     * @return the number of months in the period, zero if unsupported
     */
    public int getMonths() {
        return getPeriodType().getIndexedField(this, PeriodType.MONTH_INDEX);
    }

    /**
     * Gets the weeks field part of the period.
     * 
     * @return the number of weeks in the period, zero if unsupported
     */
    public int getWeeks() {
        return getPeriodType().getIndexedField(this, PeriodType.WEEK_INDEX);
    }

    /**
     * Gets the days field part of the period.
     * 
     * @return the number of days in the period, zero if unsupported
     */
    public int getDays() {
        return getPeriodType().getIndexedField(this, PeriodType.DAY_INDEX);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the hours field part of the period.
     * 
     * @return the number of hours in the period, zero if unsupported
     */
    public int getHours() {
        return getPeriodType().getIndexedField(this, PeriodType.HOUR_INDEX);
    }

    /**
     * Gets the minutes field part of the period.
     * 
     * @return the number of minutes in the period, zero if unsupported
     */
    public int getMinutes() {
        return getPeriodType().getIndexedField(this, PeriodType.MINUTE_INDEX);
    }

    /**
     * Gets the seconds field part of the period.
     * 
     * @return the number of seconds in the period, zero if unsupported
     */
    public int getSeconds() {
        return getPeriodType().getIndexedField(this, PeriodType.SECOND_INDEX);
    }

    /**
     * Gets the millis field part of the period.
     * 
     * @return the number of millis in the period, zero if unsupported
     */
    public int getMillis() {
        return getPeriodType().getIndexedField(this, PeriodType.MILLI_INDEX);
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a new Period instance with the same field values but
     * different PeriodType.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     * 
     * @param type  the period type to use, null means standard
     * @return the new period instance
     * @throws IllegalArgumentException if the new period won't accept all of the current fields
     */
    public Period withPeriodType(PeriodType type) {
        type = DateTimeUtils.getPeriodType(type);
        if (type.equals(getPeriodType())) {
            return this;
        }
        return new Period(this, type);
    }

    /**
     * Creates a new Period instance with the fields from the specified period
     * copied on top of those from this period.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     * 
     * @param period  the period to copy from, null ignored
     * @return the new period instance
     * @throws IllegalArgumentException if a field type is unsupported
     */
    public Period withFields(ReadablePeriod period) {
        if (period == null) {
            return this;
        }
        int[] newValues = getValues();  // cloned
        newValues = super.mergePeriodInto(newValues, period);
        return new Period(newValues, getPeriodType());
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a new Period instance with the specified field set to a new value.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     * 
     * @param field  the field to set, not null
     * @param value  the value to set to
     * @return the new period instance
     * @throws IllegalArgumentException if the field type is null or unsupported
     */
    public Period withField(DurationFieldType field, int value) {
        if (field == null) {
            throw new IllegalArgumentException("Field must not be null");
        }
        int[] newValues = getValues();  // cloned
        super.setFieldInto(newValues, field, value);
        return new Period(newValues, getPeriodType());
    }

    /**
     * Creates a new Period instance with the valueToAdd added to the specified field.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     * 
     * @param field  the field to set, not null
     * @param value  the value to add
     * @return the new period instance
     * @throws IllegalArgumentException if the field type is null or unsupported
     */
    public Period withFieldAdded(DurationFieldType field, int value) {
        if (field == null) {
            throw new IllegalArgumentException("Field must not be null");
        }
        if (value == 0) {
            return this;
        }
        int[] newValues = getValues();  // cloned
        super.addFieldInto(newValues, field, value);
        return new Period(newValues, getPeriodType());
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new period with the specified number of years.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param years  the amount of years to add, may be negative
     * @return the new period with the increased years
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period withYears(int years) {
        int[] values = getValues();  // cloned
        getPeriodType().setIndexedField(this, PeriodType.YEAR_INDEX, values, years);
        return new Period(values, getPeriodType());
    }

    /**
     * Returns a new period with the specified number of months.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param months  the amount of months to add, may be negative
     * @return the new period with the increased months
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period withMonths(int months) {
        int[] values = getValues();  // cloned
        getPeriodType().setIndexedField(this, PeriodType.MONTH_INDEX, values, months);
        return new Period(values, getPeriodType());
    }

    /**
     * Returns a new period with the specified number of weeks.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param weeks  the amount of weeks to add, may be negative
     * @return the new period with the increased weeks
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period withWeeks(int weeks) {
        int[] values = getValues();  // cloned
        getPeriodType().setIndexedField(this, PeriodType.WEEK_INDEX, values, weeks);
        return new Period(values, getPeriodType());
    }

    /**
     * Returns a new period with the specified number of days.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param days  the amount of days to add, may be negative
     * @return the new period with the increased days
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period withDays(int days) {
        int[] values = getValues();  // cloned
        getPeriodType().setIndexedField(this, PeriodType.DAY_INDEX, values, days);
        return new Period(values, getPeriodType());
    }

    /**
     * Returns a new period with the specified number of hours.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param hours  the amount of hours to add, may be negative
     * @return the new period with the increased hours
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period withHours(int hours) {
        int[] values = getValues();  // cloned
        getPeriodType().setIndexedField(this, PeriodType.HOUR_INDEX, values, hours);
        return new Period(values, getPeriodType());
    }

    /**
     * Returns a new period with the specified number of minutes.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param minutes  the amount of minutes to add, may be negative
     * @return the new period with the increased minutes
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period withMinutes(int minutes) {
        int[] values = getValues();  // cloned
        getPeriodType().setIndexedField(this, PeriodType.MINUTE_INDEX, values, minutes);
        return new Period(values, getPeriodType());
    }

    /**
     * Returns a new period with the specified number of seconds.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param seconds  the amount of seconds to add, may be negative
     * @return the new period with the increased seconds
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period withSeconds(int seconds) {
        int[] values = getValues();  // cloned
        getPeriodType().setIndexedField(this, PeriodType.SECOND_INDEX, values, seconds);
        return new Period(values, getPeriodType());
    }

    /**
     * Returns a new period with the specified number of millis.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param millis  the amount of millis to add, may be negative
     * @return the new period with the increased millis
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period withMillis(int millis) {
        int[] values = getValues();  // cloned
        getPeriodType().setIndexedField(this, PeriodType.MILLI_INDEX, values, millis);
        return new Period(values, getPeriodType());
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new period with the specified period added.
     * <p>
     * Each field of the period is added separately. Thus a period of
     * 2 hours 30 minutes plus 3 hours 40 minutes will produce a result
     * of 5 hours 70 minutes - see {@link #normalizedStandard()}.
     * <p>
     * If the period being added contains a non-zero amount for a field that
     * is not supported in this period then an exception is thrown.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param period  the period to add, null adds zero and returns this
     * @return the new updated period
     * @throws UnsupportedOperationException if any field is not supported
     * @since 1.5
     */
    public Period plus(ReadablePeriod period) {
        if (period == null) {
            return this;
        }
        int[] values = getValues();  // cloned
        getPeriodType().addIndexedField(this, PeriodType.YEAR_INDEX, values, period.get(DurationFieldType.YEARS_TYPE));
        getPeriodType().addIndexedField(this, PeriodType.MONTH_INDEX, values, period.get(DurationFieldType.MONTHS_TYPE));
        getPeriodType().addIndexedField(this, PeriodType.WEEK_INDEX, values, period.get(DurationFieldType.WEEKS_TYPE));
        getPeriodType().addIndexedField(this, PeriodType.DAY_INDEX, values, period.get(DurationFieldType.DAYS_TYPE));
        getPeriodType().addIndexedField(this, PeriodType.HOUR_INDEX, values, period.get(DurationFieldType.HOURS_TYPE));
        getPeriodType().addIndexedField(this, PeriodType.MINUTE_INDEX, values, period.get(DurationFieldType.MINUTES_TYPE));
        getPeriodType().addIndexedField(this, PeriodType.SECOND_INDEX, values, period.get(DurationFieldType.SECONDS_TYPE));
        getPeriodType().addIndexedField(this, PeriodType.MILLI_INDEX, values, period.get(DurationFieldType.MILLIS_TYPE));
        return new Period(values, getPeriodType());
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new period with the specified number of years added.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param years  the amount of years to add, may be negative
     * @return the new period with the increased years
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period plusYears(int years) {
        if (years == 0) {
            return this;
        }
        int[] values = getValues();  // cloned
        getPeriodType().addIndexedField(this, PeriodType.YEAR_INDEX, values, years);
        return new Period(values, getPeriodType());
    }

    /**
     * Returns a new period plus the specified number of months added.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param months  the amount of months to add, may be negative
     * @return the new period plus the increased months
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period plusMonths(int months) {
        if (months == 0) {
            return this;
        }
        int[] values = getValues();  // cloned
        getPeriodType().addIndexedField(this, PeriodType.MONTH_INDEX, values, months);
        return new Period(values, getPeriodType());
    }

    /**
     * Returns a new period plus the specified number of weeks added.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param weeks  the amount of weeks to add, may be negative
     * @return the new period plus the increased weeks
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period plusWeeks(int weeks) {
        if (weeks == 0) {
            return this;
        }
        int[] values = getValues();  // cloned
        getPeriodType().addIndexedField(this, PeriodType.WEEK_INDEX, values, weeks);
        return new Period(values, getPeriodType());
    }

    /**
     * Returns a new period plus the specified number of days added.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param days  the amount of days to add, may be negative
     * @return the new period plus the increased days
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period plusDays(int days) {
        if (days == 0) {
            return this;
        }
        int[] values = getValues();  // cloned
        getPeriodType().addIndexedField(this, PeriodType.DAY_INDEX, values, days);
        return new Period(values, getPeriodType());
    }

    /**
     * Returns a new period plus the specified number of hours added.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param hours  the amount of hours to add, may be negative
     * @return the new period plus the increased hours
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period plusHours(int hours) {
        if (hours == 0) {
            return this;
        }
        int[] values = getValues();  // cloned
        getPeriodType().addIndexedField(this, PeriodType.HOUR_INDEX, values, hours);
        return new Period(values, getPeriodType());
    }

    /**
     * Returns a new period plus the specified number of minutes added.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param minutes  the amount of minutes to add, may be negative
     * @return the new period plus the increased minutes
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period plusMinutes(int minutes) {
        if (minutes == 0) {
            return this;
        }
        int[] values = getValues();  // cloned
        getPeriodType().addIndexedField(this, PeriodType.MINUTE_INDEX, values, minutes);
        return new Period(values, getPeriodType());
    }

    /**
     * Returns a new period plus the specified number of seconds added.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param seconds  the amount of seconds to add, may be negative
     * @return the new period plus the increased seconds
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period plusSeconds(int seconds) {
        if (seconds == 0) {
            return this;
        }
        int[] values = getValues();  // cloned
        getPeriodType().addIndexedField(this, PeriodType.SECOND_INDEX, values, seconds);
        return new Period(values, getPeriodType());
    }

    /**
     * Returns a new period plus the specified number of millis added.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param millis  the amount of millis to add, may be negative
     * @return the new period plus the increased millis
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period plusMillis(int millis) {
        if (millis == 0) {
            return this;
        }
        int[] values = getValues();  // cloned
        getPeriodType().addIndexedField(this, PeriodType.MILLI_INDEX, values, millis);
        return new Period(values, getPeriodType());
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new period with the specified period subtracted.
     * <p>
     * Each field of the period is subtracted separately. Thus a period of
     * 3 hours 30 minutes minus 2 hours 40 minutes will produce a result
     * of 1 hour and -10 minutes - see {@link #normalizedStandard()}.
     * <p>
     * If the period being added contains a non-zero amount for a field that
     * is not supported in this period then an exception is thrown.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param period  the period to add, null adds zero and returns this
     * @return the new updated period
     * @throws UnsupportedOperationException if any field is not supported
     * @since 1.5
     */
    public Period minus(ReadablePeriod period) {
        if (period == null) {
            return this;
        }
        int[] values = getValues();  // cloned
        getPeriodType().addIndexedField(this, PeriodType.YEAR_INDEX, values, -period.get(DurationFieldType.YEARS_TYPE));
        getPeriodType().addIndexedField(this, PeriodType.MONTH_INDEX, values, -period.get(DurationFieldType.MONTHS_TYPE));
        getPeriodType().addIndexedField(this, PeriodType.WEEK_INDEX, values, -period.get(DurationFieldType.WEEKS_TYPE));
        getPeriodType().addIndexedField(this, PeriodType.DAY_INDEX, values, -period.get(DurationFieldType.DAYS_TYPE));
        getPeriodType().addIndexedField(this, PeriodType.HOUR_INDEX, values, -period.get(DurationFieldType.HOURS_TYPE));
        getPeriodType().addIndexedField(this, PeriodType.MINUTE_INDEX, values, -period.get(DurationFieldType.MINUTES_TYPE));
        getPeriodType().addIndexedField(this, PeriodType.SECOND_INDEX, values, -period.get(DurationFieldType.SECONDS_TYPE));
        getPeriodType().addIndexedField(this, PeriodType.MILLI_INDEX, values, -period.get(DurationFieldType.MILLIS_TYPE));
        return new Period(values, getPeriodType());
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new period with the specified number of years taken away.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param years  the amount of years to take away, may be negative
     * @return the new period with the increased years
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period minusYears(int years) {
        return plusYears(-years);
    }

    /**
     * Returns a new period minus the specified number of months taken away.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param months  the amount of months to take away, may be negative
     * @return the new period minus the increased months
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period minusMonths(int months) {
        return plusMonths(-months);
    }

    /**
     * Returns a new period minus the specified number of weeks taken away.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param weeks  the amount of weeks to take away, may be negative
     * @return the new period minus the increased weeks
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period minusWeeks(int weeks) {
        return plusWeeks(-weeks);
    }

    /**
     * Returns a new period minus the specified number of days taken away.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param days  the amount of days to take away, may be negative
     * @return the new period minus the increased days
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period minusDays(int days) {
        return plusDays(-days);
    }

    /**
     * Returns a new period minus the specified number of hours taken away.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param hours  the amount of hours to take away, may be negative
     * @return the new period minus the increased hours
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period minusHours(int hours) {
        return plusHours(-hours);
    }

    /**
     * Returns a new period minus the specified number of minutes taken away.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param minutes  the amount of minutes to take away, may be negative
     * @return the new period minus the increased minutes
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period minusMinutes(int minutes) {
        return plusMinutes(-minutes);
    }

    /**
     * Returns a new period minus the specified number of seconds taken away.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param seconds  the amount of seconds to take away, may be negative
     * @return the new period minus the increased seconds
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period minusSeconds(int seconds) {
        return plusSeconds(-seconds);
    }

    /**
     * Returns a new period minus the specified number of millis taken away.
     * <p>
     * This period instance is immutable and unaffected by this method call.
     *
     * @param millis  the amount of millis to take away, may be negative
     * @return the new period minus the increased millis
     * @throws UnsupportedOperationException if the field is not supported
     */
    public Period minusMillis(int millis) {
        return plusMillis(-millis);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new instance with each element in this period multiplied
     * by the specified scalar.
     *
     * @param scalar  the scalar to multiply by, not null
     * @return a {@code Period} based on this period with the amounts multiplied by the scalar, never null
     * @throws ArithmeticException if the capacity of any field is exceeded
     * @since 2.1
     */
    public Period multipliedBy(int scalar) {
        if (this == ZERO || scalar == 1) {
            return this;
        }
        int[] values = getValues();  // cloned
        for (int i = 0; i < values.length; i++) {
            values[i] = FieldUtils.safeMultiply(values[i], scalar);
        }
        return new Period(values, getPeriodType());
    }

    /**
     * Returns a new instance with each amount in this period negated.
     *
     * @return a {@code Period} based on this period with the amounts negated, never null
     * @throws ArithmeticException if any field has the minimum value
     * @since 2.1
     */
    public Period negated() {
        return multipliedBy(-1);
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this period to a period in weeks assuming a
     * 7 day week, 24 hour day, 60 minute hour and 60 second minute.
     * <p>
     * This method allows you to convert between different types of period.
     * However to achieve this it makes the assumption that all
     * weeks are 7 days, all days are 24 hours, all hours are 60 minutes and
     * all minutes are 60 seconds. This is not true when daylight savings time
     * is considered, and may also not be true for some unusual chronologies.
     * However, it is included as it is a useful operation for many
     * applications and business rules.
     * <p>
     * If the period contains years or months, an exception will be thrown.
     * 
     * @return a period representing the number of standard weeks in this period
     * @throws UnsupportedOperationException if the period contains years or months
     * @throws ArithmeticException if the number of weeks is too large to be represented
     * @since 1.5
     */
    public Weeks toStandardWeeks() {
        checkYearsAndMonths("Weeks");
        long millis = getMillis();  // assign to a long
        millis += ((long) getSeconds()) * DateTimeConstants.MILLIS_PER_SECOND;
        millis += ((long) getMinutes()) * DateTimeConstants.MILLIS_PER_MINUTE;
        millis += ((long) getHours()) * DateTimeConstants.MILLIS_PER_HOUR;
        millis += ((long) getDays()) * DateTimeConstants.MILLIS_PER_DAY;
        long weeks = ((long) getWeeks()) + millis / DateTimeConstants.MILLIS_PER_WEEK;
        return Weeks.weeks(FieldUtils.safeToInt(weeks));
    }

    /**
     * Converts this period to a period in days assuming a
     * 7 day week, 24 hour day, 60 minute hour and 60 second minute.
     * <p>
     * This method allows you to convert between different types of period.
     * However to achieve this it makes the assumption that all
     * weeks are 7 days, all days are 24 hours, all hours are 60 minutes and
     * all minutes are 60 seconds. This is not true when daylight savings time
     * is considered, and may also not be true for some unusual chronologies.
     * However, it is included as it is a useful operation for many
     * applications and business rules.
     * <p>
     * If the period contains years or months, an exception will be thrown.
     * 
     * @return a period representing the number of standard days in this period
     * @throws UnsupportedOperationException if the period contains years or months
     * @throws ArithmeticException if the number of days is too large to be represented
     * @since 1.5
     */
    public Days toStandardDays() {
        checkYearsAndMonths("Days");
        long millis = getMillis();  // assign to a long
        millis += ((long) getSeconds()) * DateTimeConstants.MILLIS_PER_SECOND;
        millis += ((long) getMinutes()) * DateTimeConstants.MILLIS_PER_MINUTE;
        millis += ((long) getHours()) * DateTimeConstants.MILLIS_PER_HOUR;
        long days = millis / DateTimeConstants.MILLIS_PER_DAY;
        days = FieldUtils.safeAdd(days, getDays());
        days = FieldUtils.safeAdd(days, ((long) getWeeks()) * ((long) DateTimeConstants.DAYS_PER_WEEK));
        return Days.days(FieldUtils.safeToInt(days));
    }

    /**
     * Converts this period to a period in hours assuming a
     * 7 day week, 24 hour day, 60 minute hour and 60 second minute.
     * <p>
     * This method allows you to convert between different types of period.
     * However to achieve this it makes the assumption that all
     * weeks are 7 days, all days are 24 hours, all hours are 60 minutes and
     * all minutes are 60 seconds. This is not true when daylight savings time
     * is considered, and may also not be true for some unusual chronologies.
     * However, it is included as it is a useful operation for many
     * applications and business rules.
     * <p>
     * If the period contains years or months, an exception will be thrown.
     * 
     * @return a period representing the number of standard hours in this period
     * @throws UnsupportedOperationException if the period contains years or months
     * @throws ArithmeticException if the number of hours is too large to be represented
     * @since 1.5
     */
    public Hours toStandardHours() {
        checkYearsAndMonths("Hours");
        long millis = getMillis();  // assign to a long
        millis += ((long) getSeconds()) * DateTimeConstants.MILLIS_PER_SECOND;
        millis += ((long) getMinutes()) * DateTimeConstants.MILLIS_PER_MINUTE;
        long hours = millis / DateTimeConstants.MILLIS_PER_HOUR;
        hours = FieldUtils.safeAdd(hours, getHours());
        hours = FieldUtils.safeAdd(hours, ((long) getDays()) * ((long) DateTimeConstants.HOURS_PER_DAY));
        hours = FieldUtils.safeAdd(hours, ((long) getWeeks()) * ((long) DateTimeConstants.HOURS_PER_WEEK));
        return Hours.hours(FieldUtils.safeToInt(hours));
    }

    /**
     * Converts this period to a period in minutes assuming a
     * 7 day week, 24 hour day, 60 minute hour and 60 second minute.
     * <p>
     * This method allows you to convert between different types of period.
     * However to achieve this it makes the assumption that all
     * weeks are 7 days, all days are 24 hours, all hours are 60 minutes and
     * all minutes are 60 seconds. This is not true when daylight savings time
     * is considered, and may also not be true for some unusual chronologies.
     * However, it is included as it is a useful operation for many
     * applications and business rules.
     * <p>
     * If the period contains years or months, an exception will be thrown.
     * 
     * @return a period representing the number of standard minutes in this period
     * @throws UnsupportedOperationException if the period contains years or months
     * @throws ArithmeticException if the number of minutes is too large to be represented
     * @since 1.5
     */
    public Minutes toStandardMinutes() {
        checkYearsAndMonths("Minutes");
        long millis = getMillis();  // assign to a long
        millis += ((long) getSeconds()) * DateTimeConstants.MILLIS_PER_SECOND;
        long minutes = millis / DateTimeConstants.MILLIS_PER_MINUTE;
        minutes = FieldUtils.safeAdd(minutes, getMinutes());
        minutes = FieldUtils.safeAdd(minutes, ((long) getHours()) * ((long) DateTimeConstants.MINUTES_PER_HOUR));
        minutes = FieldUtils.safeAdd(minutes, ((long) getDays()) * ((long) DateTimeConstants.MINUTES_PER_DAY));
        minutes = FieldUtils.safeAdd(minutes, ((long) getWeeks()) * ((long) DateTimeConstants.MINUTES_PER_WEEK));
        return Minutes.minutes(FieldUtils.safeToInt(minutes));
    }

    /**
     * Converts this period to a period in seconds assuming a
     * 7 day week, 24 hour day, 60 minute hour and 60 second minute.
     * <p>
     * This method allows you to convert between different types of period.
     * However to achieve this it makes the assumption that all
     * weeks are 7 days, all days are 24 hours, all hours are 60 minutes and
     * all minutes are 60 seconds. This is not true when daylight savings time
     * is considered, and may also not be true for some unusual chronologies.
     * However, it is included as it is a useful operation for many
     * applications and business rules.
     * <p>
     * If the period contains years or months, an exception will be thrown.
     * 
     * @return a period representing the number of standard seconds in this period
     * @throws UnsupportedOperationException if the period contains years or months
     * @throws ArithmeticException if the number of seconds is too large to be represented
     * @since 1.5
     */
    public Seconds toStandardSeconds() {
        checkYearsAndMonths("Seconds");
        long seconds = getMillis() / DateTimeConstants.MILLIS_PER_SECOND;
        seconds = FieldUtils.safeAdd(seconds, getSeconds());
        seconds = FieldUtils.safeAdd(seconds, ((long) getMinutes()) * ((long) DateTimeConstants.SECONDS_PER_MINUTE));
        seconds = FieldUtils.safeAdd(seconds, ((long) getHours()) * ((long) DateTimeConstants.SECONDS_PER_HOUR));
        seconds = FieldUtils.safeAdd(seconds, ((long) getDays()) * ((long) DateTimeConstants.SECONDS_PER_DAY));
        seconds = FieldUtils.safeAdd(seconds, ((long) getWeeks()) * ((long) DateTimeConstants.SECONDS_PER_WEEK));
        return Seconds.seconds(FieldUtils.safeToInt(seconds));
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this period to a duration assuming a
     * 7 day week, 24 hour day, 60 minute hour and 60 second minute.
     * <p>
     * This method allows you to convert from a period to a duration.
     * However to achieve this it makes the assumption that all
     * weeks are 7 days, all days are 24 hours, all hours are 60 minutes and
     * all minutes are 60 seconds. This is not true when daylight savings time
     * is considered, and may also not be true for some unusual chronologies.
     * However, it is included as it is a useful operation for many
     * applications and business rules.
     * <p>
     * If the period contains years or months, an exception will be thrown.
     * 
     * @return a duration equivalent to this period
     * @throws UnsupportedOperationException if the period contains years or months
     * @since 1.5
     */
    public Duration toStandardDuration() {
        checkYearsAndMonths("Duration");
        long millis = getMillis();  // no overflow can happen, even with Integer.MAX_VALUEs
        millis += (((long) getSeconds()) * ((long) DateTimeConstants.MILLIS_PER_SECOND));
        millis += (((long) getMinutes()) * ((long) DateTimeConstants.MILLIS_PER_MINUTE));
        millis += (((long) getHours()) * ((long) DateTimeConstants.MILLIS_PER_HOUR));
        millis += (((long) getDays()) * ((long) DateTimeConstants.MILLIS_PER_DAY));
        millis += (((long) getWeeks()) * ((long) DateTimeConstants.MILLIS_PER_WEEK));
        return new Duration(millis);
    }

    /**
     * Check that there are no years or months in the period.
     * 
     * @param destintionType  the destination type, not null
     * @throws UnsupportedOperationException if the period contains years or months
     */
    private void checkYearsAndMonths(String destintionType) {
        if (getMonths() != 0) {
            throw new UnsupportedOperationException("Cannot convert to " + destintionType + " as this period contains months and months vary in length");
        }
        if (getYears() != 0) {
            throw new UnsupportedOperationException("Cannot convert to " + destintionType + " as this period contains years and years vary in length");
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Normalizes this period using standard rules, assuming a 12 month year,
     * 7 day week, 24 hour day, 60 minute hour and 60 second minute.
     * <p>
     * This method allows you to normalize a period.
     * However to achieve this it makes the assumption that all years are
     * 12 months, all weeks are 7 days, all days are 24 hours,
     * all hours are 60 minutes and all minutes are 60 seconds. This is not
     * true when daylight savings time is considered, and may also not be true
     * for some chronologies. However, it is included as it is a useful operation
     * for many applications and business rules.
     * <p>
     * If the period contains years or months, then the months will be
     * normalized to be between 0 and 11. The days field and below will be
     * normalized as necessary, however this will not overflow into the months
     * field. Thus a period of 1 year 15 months will normalize to 2 years 3 months.
     * But a period of 1 month 40 days will remain as 1 month 40 days.
     * <p>
     * The result will always have a <code>PeriodType</code> of standard, thus
     * days will be grouped into weeks.
     * 
     * @return a normalized period equivalent to this period
     * @throws ArithmeticException if any field is too large to be represented
     * @since 1.5
     */
    public Period normalizedStandard() {
        return normalizedStandard(PeriodType.standard());
    }

    //-----------------------------------------------------------------------
    /**
     * Normalizes this period using standard rules, assuming a 12 month year,
     * 7 day week, 24 hour day, 60 minute hour and 60 second minute,
     * providing control over how the result is split into fields.
     * <p>
     * This method allows you to normalize a period.
     * However to achieve this it makes the assumption that all years are
     * 12 months, all weeks are 7 days, all days are 24 hours,
     * all hours are 60 minutes and all minutes are 60 seconds. This is not
     * true when daylight savings time is considered, and may also not be true
     * for some chronologies. However, it is included as it is a useful operation
     * for many applications and business rules.
     * <p>
     * If the period contains years or months, then the months will be
     * normalized to be between 0 and 11. The days field and below will be
     * normalized as necessary, however this will not overflow into the months
     * field. Thus a period of 1 year 15 months will normalize to 2 years 3 months.
     * But a period of 1 month 40 days will remain as 1 month 40 days.
     * <p>
     * The PeriodType parameter controls how the result is created. It allows
     * you to omit certain fields from the result if desired. For example,
     * you may not want the result to include weeks, in which case you pass
     * in <code>PeriodType.yearMonthDayTime()</code>.
     * 
     * @param type  the period type of the new period, null means standard type
     * @return a normalized period equivalent to this period
     * @throws ArithmeticException if any field is too large to be represented
     * @throws UnsupportedOperationException if this period contains non-zero
     *  years or months but the specified period type does not support them
     * @since 1.5
     */
    public Period normalizedStandard(PeriodType type) {
        type = DateTimeUtils.getPeriodType(type);
        long millis = getMillis();  // no overflow can happen, even with Integer.MAX_VALUEs
        millis += (((long) getSeconds()) * ((long) DateTimeConstants.MILLIS_PER_SECOND));
        millis += (((long) getMinutes()) * ((long) DateTimeConstants.MILLIS_PER_MINUTE));
        millis += (((long) getHours()) * ((long) DateTimeConstants.MILLIS_PER_HOUR));
        millis += (((long) getDays()) * ((long) DateTimeConstants.MILLIS_PER_DAY));
        millis += (((long) getWeeks()) * ((long) DateTimeConstants.MILLIS_PER_WEEK));
        Period result = new Period(millis, type, ISOChronology.getInstanceUTC());
        int years = getYears();
        int months = getMonths();
        if (years != 0 || months != 0) {
            long totalMonths = years * 12L + months;
            if (type.isSupported(DurationFieldType.YEARS_TYPE)) {
                int normalizedYears = FieldUtils.safeToInt(totalMonths / 12);
                result = result.withYears(normalizedYears);
                totalMonths = totalMonths - (normalizedYears * 12);
            }
            if (type.isSupported(DurationFieldType.MONTHS_TYPE)) {
                int normalizedMonths = FieldUtils.safeToInt(totalMonths);
                result = result.withMonths(normalizedMonths);
                totalMonths = totalMonths - normalizedMonths;
            }
            if (totalMonths != 0) {
                throw new UnsupportedOperationException("Unable to normalize as PeriodType is missing either years or months but period has a month/year amount: " + toString());
            }
        }
        return result;
    }

}
