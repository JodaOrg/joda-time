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

/**
 * Standard mutable time period implementation.
 * <p>
 * MutableTimePeriod is mutable and not thread-safe, unless concurrent threads
 * are not invoking mutator methods.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 * @see TimePeriod
 */
public class MutableTimePeriod
        extends AbstractTimePeriod
        implements ReadWritableTimePeriod, Cloneable, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 3436451121567212165L;

    /**
     * Creates a zero-length period using AllType.
     */
    public MutableTimePeriod() {
        super(0L, null);
    }

    /**
     * Creates a zero-length period using the specified duration type.
     *
     * @param type  which set of fields this period supports
     */
    public MutableTimePeriod(DurationType type) {
        super(0L, type);
    }

    /**
     * Creates a period from the given millisecond duration using AllType.
     * <p>
     * The exact impact of this constructor is determined by the duration type.
     * Only precise fields in the duration type will be used and the calculation will use UTC.
     * <p>
     * If the duration is small, less than one day, then this method will perform
     * as you might expect and split the fields evenly. The situation is more complex
     * for larger durations.
     * <p>
     * If this period uses the PreciseAll duration type then all fields can be set.
     * For example, a duration equal to (365 + 60 + 5) days will be converted to
     * 1 year, 2 months and 5 days using the PreciseAll type.
     * <p>
     * If the period uses the All duration type then the years and months fields
     * will remain as zero, with the duration allocated to the weeks field.
     * Normally, the weeks and days fields are imprecise, but this method
     * calculates using the UTC time zone making weeks and days precise.
     * The effect is that a large duration of several years or months will be converted
     * to a period including a large number of weeks and zero years and months.
     * For example, a duration equal to (365 + 60 + 5) days will be converted to
     * 61 weeks and 3 days.
     * <p>
     * For more control over the conversion process, you should convert the duration
     * to an interval by referencing a fixed instant and then obtain the period.
     *
     * @param duration  the duration, in milliseconds
     */
    public MutableTimePeriod(long duration) {
        super(duration, null);
    }

    /**
     * Creates a period from the given millisecond duration.
     * <p>
     * The exact impact of this constructor is determined by the duration type.
     * Only precise fields in the duration type will be used and the calculation will use UTC.
     * <p>
     * If the duration is small, less than one day, then this method will perform
     * as you might expect and split the fields evenly. The situation is more complex
     * for larger durations.
     * <p>
     * If this period uses the PreciseAll duration type then all fields can be set.
     * For example, a duration equal to (365 + 60 + 5) days will be converted to
     * 1 year, 2 months and 5 days using the PreciseAll type.
     * <p>
     * If the period uses the All duration type then the years and months fields
     * will remain as zero, with the duration allocated to the weeks field.
     * Normally, the weeks and days fields are imprecise, but this method
     * calculates using the UTC time zone making weeks and days precise.
     * The effect is that a large duration of several years or months will be converted
     * to a period including a large number of weeks and zero years and months.
     * For example, a duration equal to (365 + 60 + 5) days will be converted to
     * 61 weeks and 3 days.
     * <p>
     * For more control over the conversion process, you should convert the duration
     * to an interval by referencing a fixed instant and then obtain the period.
     *
     * @param duration  the duration, in milliseconds
     * @param type  which set of fields this duration supports
     */
    public MutableTimePeriod(long duration, DurationType type) {
        super(duration, type);
    }

    /**
     * Create a period from a set of field values using AllType.
     * This constructor creates a precise period.
     *
     * @param hours  amount of hours in this period
     * @param minutes  amount of minutes in this period
     * @param seconds  amount of seconds in this period
     * @param millis  amount of milliseconds in this period
     */
    public MutableTimePeriod(int hours, int minutes, int seconds, int millis) {
        super(0, 0, 0, 0, hours, minutes, seconds, millis, null);
    }

    /**
     * Create a period from a set of field values using AllType.
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
    public MutableTimePeriod(int years, int months, int weeks, int days,
                    int hours, int minutes, int seconds, int millis) {
        super(years, months, weeks, days, hours, minutes, seconds, millis, null);
    }

    /**
     * Create a period from a set of field values.
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
    public MutableTimePeriod(int years, int months, int weeks, int days,
                    int hours, int minutes, int seconds, int millis, DurationType type) {
        super(years, months, weeks, days, hours, minutes, seconds, millis, type);
    }

    /**
     * Creates a period from the given interval endpoints using AllType.
     * This constructor creates a precise period.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     */
    public MutableTimePeriod(long startInstant, long endInstant) {
        super(startInstant, endInstant, null);
    }

    /**
     * Creates a period from the given interval endpoints.
     * This constructor creates a precise period.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     * @param type  which set of fields this period supports, null means AllType
     */
    public MutableTimePeriod(long startInstant, long endInstant, DurationType type) {
        super(startInstant, endInstant, type);
    }

    /**
     * Creates a period from the given interval endpoints using AllType.
     * This constructor creates a precise period.
     *
     * @param startInstant  interval start, null means now
     * @param endInstant  interval end, null means now
     */
    public MutableTimePeriod(ReadableInstant startInstant, ReadableInstant endInstant) {
        super(startInstant, endInstant, null);
    }

    /**
     * Creates a period from the given interval endpoints.
     * This constructor creates a precise period.
     *
     * @param startInstant  interval start, null means now
     * @param endInstant  interval end, null means now
     * @param type  which set of fields this period supports, null means AllType
     */
    public MutableTimePeriod(ReadableInstant startInstant, ReadableInstant endInstant, DurationType type) {
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
    public MutableTimePeriod(Object period) {
        super(period, null);
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
    public MutableTimePeriod(Object period, DurationType type) {
        super(period, type);
    }

    //-----------------------------------------------------------------------
    /**
     * Validates a duration type, converting nulls to a default value and
     * checking the type is suitable for this instance.
     * 
     * @param type  the type to check, may be null
     * @return the validated type to use, not null
     * @throws IllegalArgumentException if the duration type is not precise
     */
    protected DurationType checkDurationType(DurationType type) {
        if (type == null) {
            return DurationType.getAllType();
        }
        return type;
    }

    //-----------------------------------------------------------------------
    /**
     * Sets all the fields in one go from another ReadableTimePeriod.
     * 
     * @param period  the period to set, null means zero length period
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    public void setTimePeriod(ReadableTimePeriod period) {
        super.setTimePeriod(period);
    }

    /**
     * Sets all the fields in one go.
     * 
     * @param years  amount of years in this period, which must be zero if unsupported
     * @param months  amount of months in this period, which must be zero if unsupported
     * @param weeks  amount of weeks in this period, which must be zero if unsupported
     * @param days  amount of days in this period, which must be zero if unsupported
     * @param hours  amount of hours in this period, which must be zero if unsupported
     * @param minutes  amount of minutes in this period, which must be zero if unsupported
     * @param seconds  amount of seconds in this period, which must be zero if unsupported
     * @param millis  amount of milliseconds in this period, which must be zero if unsupported
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    public void setTimePeriod(int years, int months, int weeks, int days,
                            int hours, int minutes, int seconds, int millis) {
        super.setTimePeriod(years, months, weeks, days,
                          hours, minutes, seconds, millis);
    }

    /**
     * Sets all the fields in one go from an interval dividing the
     * fields using the duration type.
     * 
     * @param interval  the interval to set, null means zero length
     */
    public void setTimePeriod(ReadableInterval interval) {
        super.setTimePeriod(interval);
    }

    /**
     * Sets all the fields in one go from a millisecond interval dividing the
     * fields using the duration type.
     * 
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     */
    public void setTimePeriod(long startInstant, long endInstant) {
        super.setTimePeriod(startInstant, endInstant);
    }

    /**
     * Sets all the fields in one go from a duration dividing the
     * fields using the duration type.
     * <p>
     * The exact impact of this method is determined by the duration type.
     * Only precise fields in the duration type will be used and the calculation will use UTC.
     * <p>
     * If the duration is small, less than one day, then this method will perform
     * as you might expect and split the fields evenly. The situation is more complex
     * for larger durations.
     * <p>
     * If this period uses the PreciseAll duration type then all fields can be set.
     * For example, a duration equal to (365 + 60 + 5) days will be converted to
     * 1 year, 2 months and 5 days using the PreciseAll type.
     * <p>
     * If the period uses the All duration type then the years and months fields
     * will remain as zero, with the duration allocated to the weeks field.
     * Normally, the weeks and days fields are imprecise, but this method
     * calculates using the UTC time zone making weeks and days precise.
     * The effect is that a large duration of several years or months will be converted
     * to a period including a large number of weeks and zero years and months.
     * For example, a duration equal to (365 + 60 + 5) days will be converted to
     * 61 weeks and 3 days.
     * <p>
     * For more control over the conversion process, you should convert the duration
     * to an interval by referencing a fixed instant and then obtain the period.
     * 
     * @param duration  the duration to set, null means zero length
     */
    public void setTimePeriod(ReadableDuration duration) {
        super.setTimePeriod(duration);
    }

    /**
     * Sets all the fields in one go from a millisecond duration dividing the
     * fields using the duration type.
     * <p>
     * The exact impact of this method is determined by the duration type.
     * Only precise fields in the duration type will be used and the calculation will use UTC.
     * <p>
     * If the duration is small, less than one day, then this method will perform
     * as you might expect and split the fields evenly. The situation is more complex
     * for larger durations.
     * <p>
     * If this period uses the PreciseAll duration type then all fields can be set.
     * For example, a duration equal to (365 + 60 + 5) days will be converted to
     * 1 year, 2 months and 5 days using the PreciseAll type.
     * <p>
     * If the period uses the All duration type then the years and months fields
     * will remain as zero, with the duration allocated to the weeks field.
     * Normally, the weeks and days fields are imprecise, but this method
     * calculates using the UTC time zone making weeks and days precise.
     * The effect is that a large duration of several years or months will be converted
     * to a period including a large number of weeks and zero years and months.
     * For example, a duration equal to (365 + 60 + 5) days will be converted to
     * 61 weeks and 3 days.
     * <p>
     * For more control over the conversion process, you should convert the duration
     * to an interval by referencing a fixed instant and then obtain the period.
     * 
     * @param duration  the duration, in milliseconds
     */
    public void setTimePeriod(long duration) {
        super.setTimePeriod(duration);
    }
    
    //-----------------------------------------------------------------------
    /**
     * Adds a period to this one by adding each field in turn.
     * 
     * @param period  the period to add, null means add nothing
     * @throws IllegalArgumentException if the period being added contains a field
     * not supported by this period
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void add(ReadableTimePeriod period) {
        super.add(period);
    }

    /**
     * Adds to each field of this period.
     * 
     * @param years  amount of years to add to this period, which must be zero if unsupported
     * @param months  amount of months to add to this period, which must be zero if unsupported
     * @param weeks  amount of weeks to add to this period, which must be zero if unsupported
     * @param days  amount of days to add to this period, which must be zero if unsupported
     * @param hours  amount of hours to add to this period, which must be zero if unsupported
     * @param minutes  amount of minutes to add to this period, which must be zero if unsupported
     * @param seconds  amount of seconds to add to this period, which must be zero if unsupported
     * @param millis  amount of milliseconds to add to this period, which must be zero if unsupported
     * @throws IllegalArgumentException if the period being added contains a field
     * not supported by this period
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void add(int years, int months, int weeks, int days,
                    int hours, int minutes, int seconds, int millis) {
        super.add(years, months, weeks, days, hours, minutes, seconds, millis);
    }

    /**
     * Adds an interval to this one by converting it to a period using the same
     * duration type and then adding each field in turn.
     * 
     * @param interval  the interval to add, null means add nothing
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void add(ReadableInterval interval) {
        super.add(interval);
    }

    /**
     * Adds a duration to this one by dividing the duration into
     * fields and then adding each field in turn.
     * <p>
     * The exact impact of this method is determined by the duration type.
     * Only precise fields in the duration type will be used and the calculation will use UTC.
     * <p>
     * If the duration is small, less than one day, then this method will perform
     * as you might expect and split the fields evenly. The situation is more complex
     * for larger durations.
     * <p>
     * If this period uses the PreciseAll duration type then all fields can be set.
     * For example, a duration equal to (365 + 60 + 5) days will be converted to
     * 1 year, 2 months and 5 days using the PreciseAll type.
     * <p>
     * If the period uses the All duration type then the years and months fields
     * will remain as zero, with the duration allocated to the weeks field.
     * Normally, the weeks and days fields are imprecise, but this method
     * calculates using the UTC time zone making weeks and days precise.
     * The effect is that a large duration of several years or months will be converted
     * to a period including a large number of weeks and zero years and months.
     * For example, a duration equal to (365 + 60 + 5) days will be converted to
     * 61 weeks and 3 days.
     * <p>
     * For more control over the conversion process, you should convert the duration
     * to an interval by referencing a fixed instant and then obtain the period.
     * 
     * @param duration  the duration to add, null means add nothing
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void add(ReadableDuration duration) {
        super.add(duration);
    }

    /**
     * Adds a duration to this one by dividing the duration into
     * fields and then adding each field in turn.
     * <p>
     * The exact impact of this method is determined by the duration type.
     * Only precise fields in the duration type will be used and the calculation will use UTC.
     * <p>
     * If the duration is small, less than one day, then this method will perform
     * as you might expect and split the fields evenly. The situation is more complex
     * for larger durations.
     * <p>
     * If this period uses the PreciseAll duration type then all fields can be set.
     * For example, a duration equal to (365 + 60 + 5) days will be converted to
     * 1 year, 2 months and 5 days using the PreciseAll type.
     * <p>
     * If the period uses the All duration type then the years and months fields
     * will remain as zero, with the duration allocated to the weeks field.
     * Normally, the weeks and days fields are imprecise, but this method
     * calculates using the UTC time zone making weeks and days precise.
     * The effect is that a large duration of several years or months will be converted
     * to a period including a large number of weeks and zero years and months.
     * For example, a duration equal to (365 + 60 + 5) days will be converted to
     * 61 weeks and 3 days.
     * <p>
     * For more control over the conversion process, you should convert the duration
     * to an interval by referencing a fixed instant and then obtain the period.
     * 
     * @param duration  the duration to add
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void add(long duration) {
        super.add(duration);
    }

    /**
     * Normalizes all the field values in this period.
     * <p>
     * This method converts to a milliecond duration and back again.
     *
     * @throws IllegalStateException if this period is imprecise
     */
    public void normalize() {
        super.normalize();
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of years of the period.
     * 
     * @param years  the number of years
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setYears(int years) {
        super.setYears(years);
    }

    /**
     * Adds the specified years to the number of years in the period.
     * 
     * @param years  the number of years
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void addYears(int years) {
        super.addYears(years);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of months of the period.
     * 
     * @param months  the number of months
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setMonths(int months) {
        super.setMonths(months);
    }

    /**
     * Adds the specified months to the number of months in the period.
     * 
     * @param months  the number of months
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void addMonths(int months) {
        super.addMonths(months);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of weeks of the period.
     * 
     * @param weeks  the number of weeks
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setWeeks(int weeks) {
        super.setWeeks(weeks);
    }

    /**
     * Adds the specified weeks to the number of weeks in the period.
     * 
     * @param weeks  the number of weeks
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void addWeeks(int weeks) {
        super.addWeeks(weeks);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of days of the period.
     * 
     * @param days  the number of days
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setDays(int days) {
        super.setDays(days);
    }

    /**
     * Adds the specified days to the number of days in the period.
     * 
     * @param days  the number of days
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void addDays(int days) {
        super.addDays(days);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of hours of the period.
     * 
     * @param hours  the number of hours
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setHours(int hours) {
        super.setHours(hours);
    }

    /**
     * Adds the specified hours to the number of hours in the period.
     * 
     * @param hours  the number of hours
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void addHours(int hours) {
        super.addHours(hours);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of minutes of the period.
     * 
     * @param minutes  the number of minutes
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setMinutes(int minutes) {
        super.setMinutes(minutes);
    }

    /**
     * Adds the specified minutes to the number of minutes in the period.
     * 
     * @param minutes  the number of minutes
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void addMinutes(int minutes) {
        super.addMinutes(minutes);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of seconds of the period.
     * 
     * @param seconds  the number of seconds
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setSeconds(int seconds) {
        super.setSeconds(seconds);
    }

    /**
     * Adds the specified seconds to the number of seconds in the period.
     * 
     * @param seconds  the number of seconds
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void addSeconds(int seconds) {
        super.addSeconds(seconds);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of millis of the period.
     * 
     * @param millis  the number of millis
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setMillis(int millis) {
        super.setMillis(millis);
    }

    /**
     * Adds the specified millis to the number of millis in the period.
     * 
     * @param millis  the number of millis
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void addMillis(int millis) {
        super.addMillis(millis);
    }

    // Misc
    //-----------------------------------------------------------------------
    /**
     * Clone this object without having to cast the returned object.
     *
     * @return a clone of the this object.
     */
    public MutableTimePeriod copy() {
        return (MutableTimePeriod)clone();
    }

    /**
     * Clone this object.
     *
     * @return a clone of this object.
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new InternalError("Clone error");
        }
    }

}
