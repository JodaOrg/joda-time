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
 * Standard immutable duration implementation split on any set of fields.
 * <p>
 * A duration can be divided into a number of fields, such as hours and seconds.
 * The way in which that divide occurs is controlled by the DurationType class.
 * Commonly use types are MillisType, which assigns all values to the millis field,
 * and AllType, which spreads values across all fields.
 * <p>
 * A duration has a concept of being <i>precise</i>.
 * A precise duration is a fixed number of milliseconds long.
 * Whether a particular duration instance is precise depends on the constructor
 * used and the duration type specified.
 * <p>
 * Duration is thread-safe and immutable, provided that the DurationType is as well.
 * All standard DurationType classes supplied are thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 * @see MutableDuration
 */
public class Duration extends AbstractDuration implements ReadableDuration, Serializable {

    /** Constant representing zero millisecond duration */
    public static final Duration ZERO = new Duration();

    /** Serialization version */
    private static final long serialVersionUID = 741052353876488155L;

    /**
     * Creates a zero length millisecond duration using MillisType.
     * This constructor creates a precise duration because
     * MillisType in ISOChronology UTC is precise.
     */
    public Duration() {
        super((DurationType) null);
    }

    /**
     * Creates a zero length duration.
     * This constructor creates a precise duration.
     *
     * @param type  which set of fields this duration supports, null means millis type
     */
    public Duration(DurationType type) {
        super(type);
    }

    /**
     * Creates a duration from the specified object using the
     * {@link org.joda.time.convert.ConverterManager ConverterManager}.
     *
     * @param duration  duration to convert
     * @throws IllegalArgumentException if duration is invalid
     * @throws UnsupportedOperationException if an unsupported field's value is non-zero
     */
    public Duration(Object duration) {
        super(duration, null);
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
    public Duration(Object duration, DurationType type) {
        super(duration, type);
    }

    /**
     * Create a duration from a set of field values using DayHourType.
     * This constructor creates a precise duration because
     * DayHourType in ISOChronology UTC is precise.
     *
     * @param days  amount of days in this duration
     * @param hours  amount of hours in this duration
     * @param minutes  amount of minutes in this duration
     * @param seconds  amount of seconds in this duration
     * @param millis  amount of milliseconds in this duration
     */
    public Duration(int days, int hours, int minutes, int seconds, int millis) {
        super(0, 0, 0, days, hours, minutes, seconds, millis, DurationType.getDayHourType());
    }

    /**
     * Create a duration from a set of field values using AllType.
     * AllType using ISOChronology in UTC is an imprecise duration type
     * unless the year, month and week fields are zero.
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
    public Duration(int years, int months, int weeks, int days,
                    int hours, int minutes, int seconds, int millis) {
        super(years, months, weeks, days, hours, minutes, seconds, millis, null);
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
     * @throws UnsupportedOperationException if an unsupported field's value is non-zero
     */
    public Duration(int years, int months, int weeks, int days,
                    int hours, int minutes, int seconds, int millis, DurationType type) {
        super(years, months, weeks, days, hours, minutes, seconds, millis, type);
    }

    /**
     * Creates a duration from the given interval endpoints using AllType.
     * This constructor creates a precise duration.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     */
    public Duration(long startInstant, long endInstant) {
        super(startInstant, endInstant, null);
    }

    /**
     * Creates a duration from the given interval endpoints.
     * This constructor creates a precise duration.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     * @param type  which set of fields this duration supports, null means AllType
     */
    public Duration(long startInstant, long endInstant, DurationType type) {
        super(startInstant, endInstant, type);
    }

    /**
     * Creates a duration from the given interval endpoints using AllType.
     * This constructor creates a precise duration.
     *
     * @param startInstant  interval start, null means now
     * @param endInstant  interval end, null means now
     */
    public Duration(ReadableInstant startInstant, ReadableInstant endInstant) {
        super(startInstant, endInstant, null);
    }

    /**
     * Creates a duration from the given interval endpoints.
     * This constructor creates a precise duration.
     *
     * @param startInstant  interval start, null means now
     * @param endInstant  interval end, null means now
     * @param type  which set of fields this duration supports, null means AllType
     */
    public Duration(ReadableInstant startInstant, ReadableInstant endInstant, DurationType type) {
        super(startInstant, endInstant, type);
    }

    /**
     * Creates a duration from the given millisecond duration using MillisType.
     * This constructor creates a precise duration because
     * MillisType in ISOChronology UTC is precise.
     *
     * @param duration  the duration, in milliseconds
     */
    public Duration(long duration) {
        super(duration, null);
    }

    /**
     * Creates a duration from the given millisecond duration.
     * This constructor creates a precise duration.
     *
     * @param duration  the duration, in milliseconds
     * @param type  which set of fields this duration supports
     * @throws UnsupportedOperationException if any fields are imprecise
     */
    public Duration(long duration, DurationType type) {
        super(duration, type);
    }

    //-----------------------------------------------------------------------
    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    protected final void setDuration(ReadableDuration duration) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    protected final void setDuration(int years, int months, int weeks, int days,
                                     int hours, int minutes, int seconds, int millis) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    protected final void setTotalMillis(long startInstant, long endInstant) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    protected final void setTotalMillis(long duration) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    protected final void normalize() {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    protected final void setYears(int years) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    protected final void setMonths(int months) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    protected final void setWeeks(int weeks) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    protected final void setDays(int days) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    protected final void setHours(int hours) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    protected final void setMinutes(int minutes) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    protected final void setSeconds(int seconds) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    protected final void setMillis(int millis) {
    }

}
