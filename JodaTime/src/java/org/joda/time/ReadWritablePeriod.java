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

/**
 * Defines a duration of time that can be queried and modified using datetime fields.
 * <p>
 * The implementation of this interface will be mutable.
 * It may provide more advanced methods than those in the interface.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public interface ReadWritablePeriod extends ReadablePeriod {

    //-----------------------------------------------------------------------
    /**
     * Sets all the fields in one go from another ReadablePeriod.
     * 
     * @param period  the period to set, null means zero length period
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    void setPeriod(ReadablePeriod period);

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
    void setPeriod(int years, int months, int weeks, int days,
                       int hours, int minutes, int seconds, int millis);

    /**
     * Sets all the fields in one go from an interval dividing the
     * fields using the period type.
     * 
     * @param interval  the interval to set, null means zero length
     */
    void setPeriod(ReadableInterval interval);

    /**
     * Sets all the fields in one go from a millisecond interval dividing the
     * fields using the period type.
     * 
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     */
    void setPeriod(long startInstant, long endInstant);

    /**
     * Sets all the fields in one go from a duration dividing the
     * fields using the period type.
     * <p>
     * When dividing the duration, only precise fields in the period type will be used.
     * For large durations, all the remaining duration will be stored in the largest
     * available precise field.
     * 
     * @param duration  the duration to set, null means zero length
     */
    void setPeriod(ReadableDuration duration);

    /**
     * Sets all the fields in one go from a millisecond duration dividing the
     * fields using the period type.
     * <p>
     * When dividing the duration, only precise fields in the period type will be used.
     * For large durations, all the remaining duration will be stored in the largest
     * available precise field.
     * 
     * @param duration  the duration, in milliseconds
     */
    void setPeriod(long duration);

    //-----------------------------------------------------------------------
    /**
     * Adds a period to this one by adding each field in turn.
     * 
     * @param period  the period to add, null means add nothing
     * @throws IllegalArgumentException if the period being added contains a field
     * not supported by this period
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    void add(ReadablePeriod period);

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
    void add(int years, int months, int weeks, int days,
                    int hours, int minutes, int seconds, int millis);

    /**
     * Adds an interval to this one by dividing the interval into
     * fields and then adding each field in turn.
     * 
     * @param interval  the interval to add, null means add nothing
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    void add(ReadableInterval interval);

    /**
     * Adds a duration to this one by dividing the duration into
     * fields and then adding each field in turn.
     * <p>
     * When dividing the duration, only precise fields in the period type will be used.
     * For large durations, all the remaining duration will be added to the largest
     * available precise field.
     * 
     * @param duration  the duration to add, null means add nothing
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    void add(ReadableDuration duration);

    /**
     * Adds a duration to this one by dividing the duration into
     * fields and then adding each field in turn.
     * <p>
     * When dividing the duration, only precise fields in the period type will be used.
     * For large durations, all the remaining duration will be added to the largest
     * available precise field.
     * 
     * @param duration  the duration to add
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    void add(long duration);

    /**
     * Normalizes all the field values in this period.
     * <p>
     * This method converts to a milliecond duration and back again.
     *
     * @throws IllegalStateException if this period is imprecise
     */
    void normalize();

    //-----------------------------------------------------------------------
    /**
     * Sets the number of years of the period.
     * 
     * @param years  the number of years
     * @throws UnsupportedOperationException if field is not supported.
     */
    void setYears(int years);

    /**
     * Adds the specified years to the number of years in the period.
     * 
     * @param years  the number of years
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    void addYears(int years);

    //-----------------------------------------------------------------------
    /**
     * Sets the number of months of the period.
     * 
     * @param months  the number of months
     * @throws UnsupportedOperationException if field is not supported.
     */
    void setMonths(int months);

    /**
     * Adds the specified months to the number of months in the period.
     * 
     * @param months  the number of months
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    void addMonths(int months);

    //-----------------------------------------------------------------------
    /**
     * Sets the number of weeks of the period.
     * 
     * @param weeks  the number of weeks
     * @throws UnsupportedOperationException if field is not supported.
     */
    void setWeeks(int weeks);

    /**
     * Adds the specified weeks to the number of weeks in the period.
     * 
     * @param weeks  the number of weeks
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    void addWeeks(int weeks);

    //-----------------------------------------------------------------------
    /**
     * Sets the number of days of the period.
     * 
     * @param days  the number of days
     * @throws UnsupportedOperationException if field is not supported.
     */
    void setDays(int days);

    /**
     * Adds the specified days to the number of days in the period.
     * 
     * @param days  the number of days
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    void addDays(int days);

    //-----------------------------------------------------------------------
    /**
     * Sets the number of hours of the period.
     * 
     * @param hours  the number of hours
     * @throws UnsupportedOperationException if field is not supported.
     */
    void setHours(int hours);

    /**
     * Adds the specified hours to the number of hours in the period.
     * 
     * @param hours  the number of hours
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    void addHours(int hours);

    //-----------------------------------------------------------------------
    /**
     * Sets the number of minutes of the period.
     * 
     * @param minutes  the number of minutes
     * @throws UnsupportedOperationException if field is not supported.
     */
    void setMinutes(int minutes);

    /**
     * Adds the specified minutes to the number of minutes in the period.
     * 
     * @param minutes  the number of minutes
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    void addMinutes(int minutes);

    //-----------------------------------------------------------------------
    /**
     * Sets the number of seconds of the period.
     * 
     * @param seconds  the number of seconds
     * @throws UnsupportedOperationException if field is not supported.
     */
    void setSeconds(int seconds);

    /**
     * Adds the specified seconds to the number of seconds in the period.
     * 
     * @param seconds  the number of seconds
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    void addSeconds(int seconds);

    //-----------------------------------------------------------------------
    /**
     * Sets the number of millis of the period.
     * 
     * @param millis  the number of millis
     * @throws UnsupportedOperationException if field is not supported.
     */
    void setMillis(int millis);

    /**
     * Adds the specified millis to the number of millis in the period.
     * 
     * @param millis  the number of millis
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    void addMillis(int millis);

}
