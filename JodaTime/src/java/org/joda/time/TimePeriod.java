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
 * An immutable time period specifying a set of duration field values.
 * <p>
 * A time period is divided into a number of fields, such as hours and seconds.
 * The way in which that divide occurs is controlled by the DurationType class.
 * <p>
 * <code>TimePeriod</code> can use any duration type to split the milliseconds into fields.
 * The {@link DurationType#getAllType() All} type is used by default.
 * <code>All</code> uses the ISO chronology and divides a duration into years, months,
 * weeks, days, hours, minutes, seconds and milliseconds as best it can.
 * <p>
 * This class performs calculations using the individual fields.
 * It <i>may</i> be possible to convert a <code>TimePeriod</code> to a <code>Duration</code>.
 * The conversion will succeed if the time period is precise.
 * A time period is precise if all of the populated fields have a fixed known duration.
 * <p>
 * When this time period is added to an instant, the effect is of adding each field in turn.
 * As a result, this takes into account daylight savings time.
 * Adding a time period of 1 day to the day before daylight savings starts will only add
 * 23 hours rather than 24 to ensure that the time remains the same.
 * If this is not the behaviour you want, then see {@link Duration}.
 * <p>
 * TimePeriod is thread-safe and immutable, provided that the DurationType is as well.
 * All standard DurationType classes supplied are thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 * @see MutableTimePeriod
 */
public class TimePeriod
        extends AbstractTimePeriod
        implements ReadableTimePeriod, Serializable {

    /** Constant representing zero millisecond duration */
    public static final TimePeriod ZERO = new TimePeriod(0L);

    /** Serialization version */
    private static final long serialVersionUID = 741052353876488155L;

    /**
     * Creates a duration from the given millisecond duration using AllType.
     * <p>
     * The millisecond duration will be split to fields using a UTC version of
     * the duration type. This ensures that there are no odd effects caused by
     * time zones. The add methods will still use the time zone specific version
     * of the duration type.
     *
     * @param duration  the duration, in milliseconds
     */
    public TimePeriod(long duration) {
        super(duration, null);
    }

    /**
     * Creates a duration from the given millisecond duration.
     * <p>
     * The millisecond duration will be split to fields using a UTC version of
     * the duration type. This ensures that there are no odd effects caused by
     * time zones. The add methods will still use the time zone specific version
     * of the duration type.
     *
     * @param duration  the duration, in milliseconds
     * @param type  which set of fields this duration supports
     */
    public TimePeriod(long duration, DurationType type) {
        super(duration, type);
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
    public TimePeriod(int hours, int minutes, int seconds, int millis) {
        super(0, 0, 0, 0, hours, minutes, seconds, millis, null);
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
    public TimePeriod(int years, int months, int weeks, int days,
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
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    public TimePeriod(int years, int months, int weeks, int days,
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
    public TimePeriod(long startInstant, long endInstant) {
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
    public TimePeriod(long startInstant, long endInstant, DurationType type) {
        super(startInstant, endInstant, type);
    }

    /**
     * Creates a duration from the given interval endpoints using AllType.
     * This constructor creates a precise duration.
     *
     * @param startInstant  interval start, null means now
     * @param endInstant  interval end, null means now
     */
    public TimePeriod(ReadableInstant startInstant, ReadableInstant endInstant) {
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
    public TimePeriod(ReadableInstant startInstant, ReadableInstant endInstant, DurationType type) {
        super(startInstant, endInstant, type);
    }

    /**
     * Creates a duration from the specified object using the
     * {@link org.joda.time.convert.ConverterManager ConverterManager}.
     *
     * @param duration  duration to convert
     * @throws IllegalArgumentException if duration is invalid
     * @throws UnsupportedOperationException if an unsupported field's value is non-zero
     */
    public TimePeriod(Object duration) {
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
    public TimePeriod(Object duration, DurationType type) {
        super(duration, type);
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
            return DurationType.getAllType();
        }
        return type;
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a new TimePeriod instance with the same field values but
     * different DurationType.
     * 
     * @param type  the duration type to use, null means AllType
     * @return the new duration instance
     * @throws IllegalArgumentException if the new duration won't accept all of the current fields
     */
    public TimePeriod withDurationType(DurationType type) {
        if (type == null) {
            type = DurationType.getAllType();
        }
        if (type.equals(getDurationType())) {
            return this;
        }
        return new TimePeriod(getYears(), getMonths(), getWeeks(), getDays(),
                    getHours(), getMinutes(), getSeconds(), getMillis(), type);
    }

    /**
     * Creates a new TimePeriod instance with the same millisecond duration but
     * different DurationType.
     * 
     * @param type  the duration type to use, null means AllType
     * @return the new duration instance
     * @throws IllegalStateException if this duration is imprecise
     */
    public TimePeriod withDurationTypeRetainDuration(DurationType type) {
        if (type == null) {
            type = DurationType.getAllType();
        }
        if (type.equals(getDurationType())) {
            return this;
        }
        return new TimePeriod(toDurationMillis(), type);
    }

    /**
     * Creates a new TimePeriod instance with the same millisecond duration but
     * all the fields normalized to be within their standard ranges.
     * 
     * @return the new duration instance
     * @throws IllegalStateException if this duration is imprecise
     */
    public TimePeriod withFieldsNormalized() {
        return new TimePeriod(toDurationMillis(), getDurationType());
    }

    //-----------------------------------------------------------------------
    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    protected final void setTimePeriod(ReadableTimePeriod period) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    protected final void setTimePeriod(int years, int months, int weeks, int days,
                                       int hours, int minutes, int seconds, int millis) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    protected final void setTimePeriod(long startInstant, long endInstant) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    protected final void setTimePeriod(long duration) {
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
