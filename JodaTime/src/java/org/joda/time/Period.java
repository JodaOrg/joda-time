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
package org.joda.time;

import java.io.Serializable;

import org.joda.time.base.BasePeriod;

/**
 * An immutable time period specifying a set of duration field values.
 * <p>
 * A time period is divided into a number of fields, such as hours and seconds.
 * Which fields are supported is defined by the PeriodType class.
 * <p>
 * When this time period is added to an instant, the effect is of adding each field in turn.
 * As a result, this takes into account daylight savings time.
 * Adding a time period of 1 day to the day before daylight savings starts will only add
 * 23 hours rather than 24 to ensure that the time remains the same.
 * If this is not the behaviour you want, then see {@link Duration}.
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

    /** Serialization version */
    private static final long serialVersionUID = 741052353876488155L;

    //-----------------------------------------------------------------------
    /**
     * Create a period with a specified number of years.
     * The standard period type is used.
     *
     * @param years  the amount of years in this period
     * @return the period
     */
    public static Period years(int years) {
        return new Period(new int[] {years}, PeriodType.standard());
    }

    /**
     * Create a period with a specified number of months.
     * The standard period type is used.
     *
     * @param months  the amount of months in this period
     * @return the period
     */
    public static Period months(int months) {
        return new Period(new int[] {months}, PeriodType.standard());
    }

    /**
     * Create a period with a specified number of weeks.
     * The standard period type is used.
     *
     * @param weeks  the amount of weeks in this period
     * @return the period
     */
    public static Period weeks(int weeks) {
        return new Period(new int[] {weeks}, PeriodType.standard());
    }

    /**
     * Create a period with a specified number of days.
     * The standard period type is used.
     *
     * @param days  the amount of days in this period
     * @return the period
     */
    public static Period days(int days) {
        return new Period(new int[] {days}, PeriodType.standard());
    }

    /**
     * Create a period with a specified number of hours.
     * The standard period type is used.
     *
     * @param hours  the amount of hours in this period
     * @return the period
     */
    public static Period hours(int hours) {
        return new Period(new int[] {hours}, PeriodType.standard());
    }

    /**
     * Create a period with a specified number of minutes.
     * The standard period type is used.
     *
     * @param minutes  the amount of minutes in this period
     * @return the period
     */
    public static Period minutes(int minutes) {
        return new Period(new int[] {minutes}, PeriodType.standard());
    }

    /**
     * Create a period with a specified number of seconds.
     * The standard period type is used.
     *
     * @param seconds  the amount of seconds in this period
     * @return the period
     */
    public static Period seconds(int seconds) {
        return new Period(new int[] {seconds}, PeriodType.standard());
    }

    /**
     * Create a period with a specified number of millis.
     * The standard period type is used.
     *
     * @param millis  the amount of millis in this period
     * @return the period
     */
    public static Period millis(int millis) {
        return new Period(new int[] {millis}, PeriodType.standard());
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
        super(duration, null, null);
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
     * Creates a period from the given interval endpoints using the standard
     * set of fields.
     *
     * @param startInstant  interval start, null means now
     * @param endInstant  interval end, null means now
     */
    public Period(ReadableInstant startInstant, ReadableInstant endInstant) {
        super(startInstant, endInstant, null);
    }

    /**
     * Creates a period from the given interval endpoints.
     *
     * @param startInstant  interval start, null means now
     * @param endInstant  interval end, null means now
     * @param type  which set of fields this period supports, null means AllType
     */
    public Period(ReadableInstant startInstant, ReadableInstant endInstant, PeriodType type) {
        super(startInstant, endInstant, type);
    }

    /**
     * Creates a period from the specified object using the
     * {@link org.joda.time.convert.ConverterManager ConverterManager}.
     *
     * @param period  period to convert
     * @throws IllegalArgumentException if period is invalid
     * @throws UnsupportedOperationException if an unsupported field's value is non-zero
     */
    public Period(Object period) {
        super(period, null, null);
    }

    /**
     * Creates a period from the specified object using the
     * {@link org.joda.time.convert.ConverterManager ConverterManager}.
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
     * Creates a period from the specified object using the
     * {@link org.joda.time.convert.ConverterManager ConverterManager}.
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
     * Creates a period from the specified object using the
     * {@link org.joda.time.convert.ConverterManager ConverterManager}.
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
     * 
     * @param type  the period type to use, null means AllType
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

}
