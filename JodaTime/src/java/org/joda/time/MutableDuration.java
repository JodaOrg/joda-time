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
 * Standard mutable duration implementation.
 * <p>
 * MutableDuration is mutable and not thread-safe, unless concurrent threads
 * are not invoking mutator methods.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 * @see Duration
 */
public class MutableDuration
        extends AbstractDuration
        implements ReadWritableDuration, Cloneable, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 3436451121567212165L;

    /**
     * Creates a zero-length duration using AllType.
     */
    public MutableDuration() {
        super(0L, null, false);
    }

    /**
     * Creates a zero-length duration using the specified duration type.
     *
     * @param type  which set of fields this duration supports
     */
    public MutableDuration(DurationType type) {
        super(0L, type, false);
    }

    /**
     * Creates a zero-length duration using the specified duration type.
     * <p>
     * This constructor enables the created object to be based on total miliseconds
     * rather than the more normal fields. A total millisecond based duration
     * performs all calculations using the total millis and is always precise.
     *
     * @param type  which set of fields this duration supports
     * @param totalMillisBased  true if duration treats the total millis as the master field
     * @throws IllegalArgumentException if the duration type is imprecise and totalMillisBased is true
     */
    public MutableDuration(DurationType type, boolean totalMillisBased) {
        super(0L, type, totalMillisBased);
    }

    /**
     * Creates a duration from the given millisecond duration using AllType.
     *
     * @param duration  the duration, in milliseconds
     */
    public MutableDuration(long duration) {
        super(duration, null, false);
    }

    /**
     * Creates a duration from the given millisecond duration.
     *
     * @param duration  the duration, in milliseconds
     * @param type  which set of fields this duration supports
     */
    public MutableDuration(long duration, DurationType type) {
        super(duration, type, false);
    }

    /**
     * Create a duration from a set of field values using AllType.
     * This constructor creates a precise duration.
     *
     * @param hours  amount of hours in this duration
     * @param minutes  amount of minutes in this duration
     * @param seconds  amount of seconds in this duration
     * @param millis  amount of milliseconds in this duration
     */
    public MutableDuration(int hours, int minutes, int seconds, int millis) {
        super(0, 0, 0, 0, hours, minutes, seconds, millis, null, false);
    }

    /**
     * Create a duration from a set of field values using AllType.
     *
     * @param years  amount of years in this duration
     * @param months  amount of months in this duration
     * @param weeks  amount of weeks in this duration
     * @param days  amount of days in this duration
     * @param hours  amount of hours in this duration
     * @param minutes  amount of minutes in this duration
     * @param seconds  amount of seconds in this duration
     * @param millis  amount of milliseconds in this duration
     */
    public MutableDuration(int years, int months, int weeks, int days,
                    int hours, int minutes, int seconds, int millis) {
        super(years, months, weeks, days, hours, minutes, seconds, millis, null, false);
    }

    /**
     * Create a duration from a set of field values.
     *
     * @param years  amount of years in this duration, which must be zero if unsupported
     * @param months  amount of months in this duration, which must be zero if unsupported
     * @param weeks  amount of weeks in this duration, which must be zero if unsupported
     * @param days  amount of days in this duration, which must be zero if unsupported
     * @param hours  amount of hours in this duration, which must be zero if unsupported
     * @param minutes  amount of minutes in this duration, which must be zero if unsupported
     * @param seconds  amount of seconds in this duration, which must be zero if unsupported
     * @param millis  amount of milliseconds in this duration, which must be zero if unsupported
     * @param type  which set of fields this duration supports, null means AllType
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    public MutableDuration(int years, int months, int weeks, int days,
                    int hours, int minutes, int seconds, int millis, DurationType type) {
        super(years, months, weeks, days, hours, minutes, seconds, millis, type, false);
    }

    /**
     * Creates a duration from the given interval endpoints using AllType.
     * This constructor creates a precise duration.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     */
    public MutableDuration(long startInstant, long endInstant) {
        super(startInstant, endInstant, null, false);
    }

    /**
     * Creates a duration from the given interval endpoints.
     * This constructor creates a precise duration.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     * @param type  which set of fields this duration supports, null means AllType
     */
    public MutableDuration(long startInstant, long endInstant, DurationType type) {
        super(startInstant, endInstant, type, false);
    }

    /**
     * Creates a duration from the given interval endpoints using AllType.
     * This constructor creates a precise duration.
     *
     * @param startInstant  interval start, null means now
     * @param endInstant  interval end, null means now
     */
    public MutableDuration(ReadableInstant startInstant, ReadableInstant endInstant) {
        super(startInstant, endInstant, null, false);
    }

    /**
     * Creates a duration from the given interval endpoints.
     * This constructor creates a precise duration.
     *
     * @param startInstant  interval start, null means now
     * @param endInstant  interval end, null means now
     * @param type  which set of fields this duration supports, null means AllType
     */
    public MutableDuration(ReadableInstant startInstant, ReadableInstant endInstant, DurationType type) {
        super(startInstant, endInstant, type, false);
    }

    /**
     * Creates a duration from the specified object using the
     * {@link org.joda.time.convert.ConverterManager ConverterManager}.
     *
     * @param duration  duration to convert
     * @throws IllegalArgumentException if duration is invalid
     * @throws UnsupportedOperationException if an unsupported field's value is non-zero
     */
    public MutableDuration(Object duration) {
        super(duration, null, false);
    }

    /**
     * Creates a duration from the specified object using the
     * {@link org.joda.time.convert.ConverterManager ConverterManager}.
     *
     * @param duration  duration to convert
     * @param type  which set of fields this duration supports, null means use converter
     * @throws IllegalArgumentException if duration is invalid
     * @throws UnsupportedOperationException if an unsupported field's value is non-zero
     */
    public MutableDuration(Object duration, DurationType type) {
        super(duration, type, false);
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
            if (isTotalMillisBased()) {
                return DurationType.getPreciseAllType();
            } else {
                return DurationType.getAllType();
            }
        }
        return type;
    }

    //-----------------------------------------------------------------------
    /**
     * Sets all the fields in one go from another ReadableDuration.
     * 
     * @param duration  the duration to set, null means zero length duration
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    public void setDuration(ReadableDuration duration) {
        super.setDuration(duration);
    }

    /**
     * Sets all the fields in one go.
     * 
     * @param years  amount of years in this duration, which must be zero if unsupported
     * @param months  amount of months in this duration, which must be zero if unsupported
     * @param weeks  amount of weeks in this duration, which must be zero if unsupported
     * @param days  amount of days in this duration, which must be zero if unsupported
     * @param hours  amount of hours in this duration, which must be zero if unsupported
     * @param minutes  amount of minutes in this duration, which must be zero if unsupported
     * @param seconds  amount of seconds in this duration, which must be zero if unsupported
     * @param millis  amount of milliseconds in this duration, which must be zero if unsupported
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    public void setDuration(int years, int months, int weeks, int days,
                            int hours, int minutes, int seconds, int millis) {
        super.setDuration(years, months, weeks, days,
                          hours, minutes, seconds, millis);
    }

    /**
     * Sets all the fields in one go from a millisecond interval.
     * 
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     */
    public void setTotalMillis(long startInstant, long endInstant) {
        super.setTotalMillis(startInstant, endInstant);
    }

    /**
     * Sets all the fields in one go from a millisecond duration.
     * Only fields that are supported and precise will be set.
     * 
     * @param duration  the duration, in milliseconds
     */
    public void setTotalMillis(long duration) {
        super.setTotalMillis(duration);
    }
    
    //-----------------------------------------------------------------------
    /**
     * Adds a millisecond duration to this one.
     * 
     * @param duration  the duration to add, in milliseconds
     * @throws IllegalStateException if the duration is imprecise
     */
    public void add(long duration) {
        super.add(duration);
    }
    
    /**
     * Adds a duration to this one.
     * 
     * @param duration  the duration to add, mulls means add nothing
     * @throws IllegalStateException if the duration is imprecise
     */
    public void add(ReadableDuration duration) {
        super.add(duration);
    }
    
    /**
     * Normalizes all the field values in this duration.
     *
     * @throws IllegalStateException if this duration is imprecise
     */
    public void normalize() {
        super.normalize();
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of years of the duration.
     * 
     * @param years  the number of years
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setYears(int years) {
        super.setYears(years);
    }

    /**
     * Adds the specified years to the number of years in the duration.
     * 
     * @param years  the number of years
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void addYears(int years) {
        super.addYears(years);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of months of the duration.
     * 
     * @param months  the number of months
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setMonths(int months) {
        super.setMonths(months);
    }

    /**
     * Adds the specified months to the number of months in the duration.
     * 
     * @param months  the number of months
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void addMonths(int months) {
        super.addMonths(months);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of weeks of the duration.
     * 
     * @param weeks  the number of weeks
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setWeeks(int weeks) {
        super.setWeeks(weeks);
    }

    /**
     * Adds the specified weeks to the number of weeks in the duration.
     * 
     * @param weeks  the number of weeks
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void addWeeks(int weeks) {
        super.addWeeks(weeks);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of days of the duration.
     * 
     * @param days  the number of days
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setDays(int days) {
        super.setDays(days);
    }

    /**
     * Adds the specified days to the number of days in the duration.
     * 
     * @param days  the number of days
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void addDays(int days) {
        super.addDays(days);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of hours of the duration.
     * 
     * @param hours  the number of hours
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setHours(int hours) {
        super.setHours(hours);
    }

    /**
     * Adds the specified hours to the number of hours in the duration.
     * 
     * @param hours  the number of hours
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void addHours(int hours) {
        super.addHours(hours);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of minutes of the duration.
     * 
     * @param minutes  the number of minutes
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setMinutes(int minutes) {
        super.setMinutes(minutes);
    }

    /**
     * Adds the specified minutes to the number of minutes in the duration.
     * 
     * @param minutes  the number of minutes
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void addMinutes(int minutes) {
        super.addMinutes(minutes);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of seconds of the duration.
     * 
     * @param seconds  the number of seconds
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setSeconds(int seconds) {
        super.setSeconds(seconds);
    }

    /**
     * Adds the specified seconds to the number of seconds in the duration.
     * 
     * @param seconds  the number of seconds
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void addSeconds(int seconds) {
        super.addSeconds(seconds);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of millis of the duration.
     * 
     * @param millis  the number of millis
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setMillis(int millis) {
        super.setMillis(millis);
    }

    /**
     * Adds the specified millis to the number of millis in the duration.
     * 
     * @param millis  the number of millis
     * @throws UnsupportedOperationException if field is not supported.
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
    public MutableDuration copy() {
        return (MutableDuration)clone();
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
