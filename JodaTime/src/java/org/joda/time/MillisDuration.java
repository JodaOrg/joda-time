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
 * A precise immutable duration that defines and adds durations based on milliseconds.
 * <p>
 * A precise duration is one that is defined primarily by a fixed number of milliseconds.
 * The fields, such as hours and seconds, are provided for convenience.
 * The {@link ReadableDuration#isTotalMillisBased} method will always return true.
 * <p>
 * <code>MillisDuration</code> uses any precise duration type to split the milliseconds
 * into fields.
 * The {@link DurationType#getPreciseAllType() PreciseAll} type is used by default.
 * <code>PreciseAll</code> uses the ISO chronology and fixes
 * days at 24 hours, weeks at 7 days, months at 30 days and years at 365 days.
 * <p>
 * When a precise duration is added to an instant the millisecond value of the instant
 * is added. The field values are not used. If the addition to the instant crosses a
 * daylight savings boundary the effect may be unexpected.
 * <p>
 * For example, consider a <code>MillisDuration</code> of 1 day.
 * This actually represents <code>24 * 60 * 60 * 1000</code> milliseconds.
 * When you add this to a <code>DateTime</code> just before daylight savings changes
 * the result will be to add the milliseconds. Thus the result will be one hour
 * different on the following day.
 * <pre>
 * MillisDuration dur = new MillisDuration(0, 0, 0, 1, 0, 0, 0, 0); // 1 'day'
 * DateTime dt = new DateTime(2004, 3, 27, 12, 0, 0, 0); // before DST
 * DateTime result = new DateTime(dur.addTo(dt, 1)); // after DST
 * // result:  2004-03-27T12:00:00 -> 2004-03-28T13:00:00 
 * // note: result time is 13:00, as 1 day is always 24 hours in MillisDuration
 * </pre>
 * If this behaviour is not what you want then you should use {@link Duration}.
 * <p>
 * MillisDuration is thread-safe and immutable, provided that the DurationType is as well.
 * All standard DurationType classes supplied are thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 * @see Duration
 * @see MutableDuration
 */
public class MillisDuration
        extends AbstractDuration
        implements ReadableDuration, Serializable {

    /** Constant representing zero millisecond duration */
    public static final MillisDuration ZERO = new MillisDuration(0L);

    /** Serialization version */
    private static final long serialVersionUID = 5727916780257544L;

    /**
     * Creates a duration from the given millisecond duration using PreciseAllType.
     * <p>
     * The duration created using this constructor will always have normalized fields.
     *
     * @param duration  the duration, in milliseconds
     */
    public MillisDuration(long duration) {
        super(duration, null, true);
    }

    /**
     * Creates a duration from the given millisecond duration.
     * <p>
     * The duration created using this constructor will always have normalized fields.
     *
     * @param duration  the duration, in milliseconds
     * @param type  which set of fields this duration supports, null means PreciseAllType
     * @throws IllegalArgumentException if the duration type is not precise
     */
    public MillisDuration(long duration, DurationType type) {
        super(duration, type, true);
    }

    /**
     * Create a duration from a set of field values using PreciseAllType.
     *
     * @param hours  amount of hours in this duration
     * @param minutes  amount of minutes in this duration
     * @param seconds  amount of seconds in this duration
     * @param millis  amount of milliseconds in this duration
     */
    public MillisDuration(int hours, int minutes, int seconds, int millis) {
        super(0, 0, 0, 0, hours, minutes, seconds, millis, null, true);
    }

    /**
     * Create a duration from a set of field values using PreciseAllType.
     *
     * @param years  amount of years in this duration
     * @param months  amount of months in this duration
     * @param weeks  amount of weeks in this duration
     * @param days  amount of days in this duration
     * @param hours  amount of hours in this duration
     * @param minutes  amount of minutes in this duration
     * @param seconds  amount of seconds in this duration
     * @param millis  amount of milliseconds in this duration
     * @throws ArithmeticException if the total millis is too large for a <code>long</code>
     */
    public MillisDuration(int years, int months, int weeks, int days,
                    int hours, int minutes, int seconds, int millis) {
        super(years, months, weeks, days, hours, minutes, seconds, millis, null, true);
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
     * @param type  which set of fields this duration supports, null means PreciseAllType
     * @throws IllegalArgumentException if the duration type is not precise
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     * @throws ArithmeticException if the total millis is too large for a <code>long</code>
     */
    public MillisDuration(int years, int months, int weeks, int days,
                    int hours, int minutes, int seconds, int millis, DurationType type) {
        super(years, months, weeks, days, hours, minutes, seconds, millis, type, true);
    }

    /**
     * Creates a duration from the given interval endpoints using PreciseAllType.
     * <p>
     * This constructor is a convenience for the single <code>long</code> constructor.
     * The start and end instant play non role in determining the field values.
     * The duration created using this constructor will always have normalized fields.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     */
    public MillisDuration(long startInstant, long endInstant) {
        super(startInstant, endInstant, null, true);
    }

    /**
     * Creates a duration from the given interval endpoints.
     * <p>
     * This constructor is a convenience for the single <code>long</code> constructor.
     * The start and end instant play non role in determining the field values.
     * The duration created using this constructor will always have normalized fields.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     * @param type  which set of fields this duration supports, null means PreciseAllType
     * @throws IllegalArgumentException if the duration type is not precise
     */
    public MillisDuration(long startInstant, long endInstant, DurationType type) {
        super(startInstant, endInstant, type, true);
    }

    /**
     * Creates a duration from the given interval endpoints using PreciseAllType.
     * <p>
     * The duration created using this constructor will always have normalized fields.
     *
     * @param startInstant  interval start, null means now
     * @param endInstant  interval end, null means now
     */
    public MillisDuration(ReadableInstant startInstant, ReadableInstant endInstant) {
        super(startInstant, endInstant, null, true);
    }

    /**
     * Creates a duration from the given interval endpoints.
     * <p>
     * This constructor is a convenience for the single <code>long</code> constructor.
     * The start and end instant play non role in determining the field values.
     * The duration created using this constructor will always have normalized fields.
     *
     * @param startInstant  interval start, null means now
     * @param endInstant  interval end, null means now
     * @param type  which set of fields this duration supports, null means PreciseAllType
     * @throws IllegalArgumentException if the duration type is not precise
     */
    public MillisDuration(ReadableInstant startInstant, ReadableInstant endInstant, DurationType type) {
        super(startInstant, endInstant, type, true);
    }

    /**
     * Creates a duration from the specified object using the
     * {@link org.joda.time.convert.ConverterManager ConverterManager}.
     * <p>
     * This constructor is a convenience for the single <code>long</code> constructor.
     * The start and end instant play non role in determining the field values.
     * The duration created using this constructor will always have normalized fields.
     *
     * @param duration  duration to convert
     * @throws IllegalArgumentException if duration is invalid
     * @throws UnsupportedOperationException if an unsupported field's value is non-zero
     */
    public MillisDuration(Object duration) {
        super(duration, null, true);
    }

    /**
     * Creates a duration from the specified object using the
     * {@link org.joda.time.convert.ConverterManager ConverterManager}.
     * <p>
     * The duration created using this constructor will always have normalized fields.
     *
     * @param duration  duration to convert
     * @param type  which set of fields this duration supports, null means use converter
     * @throws IllegalArgumentException if duration is invalid
     * @throws IllegalArgumentException if the duration type is not precise
     * @throws UnsupportedOperationException if an unsupported field's value is non-zero
     */
    public MillisDuration(Object duration, DurationType type) {
        super(duration, type, true);
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
    protected final DurationType checkDurationType(DurationType type) {
        if (type == null) {
            return DurationType.getPreciseAllType();
        }
        if (type.isPrecise() == false) {
            throw new IllegalArgumentException("The duration type must be precise: " + type);
        }
        return type;
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a new MillisDuration instance with the same total milliseconds but
     * different DurationType, which must be precise.
     * 
     * @param type  the duration type to use, null means PreciseAllType
     * @return the new duration instance
     * @throws IllegalArgumentException if the duration type is not precise
     */
    public MillisDuration withDurationType(DurationType type) {
        type = checkDurationType(type);
        if (type.equals(getDurationType())) {
            return this;
        }
        return new MillisDuration(getTotalMillis(), type);
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
