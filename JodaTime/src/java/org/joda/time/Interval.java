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
 * Interval is the standard implementation of an immutable time interval.
 * <p>
 * A time interval represents a period of time between two instants.
 * Intervals are inclusive of the start instant and exclusive of the end.
 * The end instant is always greater than or equal to the start instant.
 * <p>
 * Intervals have a fixed millisecond duration.
 * This is the difference between the start and end instants.
 * The duration is represented separately by {@link ReadableDuration}.
 * As a result, intervals are not comparable.
 * To compare the length of two intervals, you should compare their durations.
 * <p>
 * An interval can also be converted to a {@link ReadablePeriod}.
 * This represents the difference between the start and end points in terms of fields
 * such as years and days.
 * <p>
 * Interval is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @author Sean Geoghegan
 * @author Stephen Colebourne
 * @since 1.0
 */
public class Interval
        extends AbstractInterval
        implements ReadableInterval, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 4922451897541386752L;

    /**
     * Constructs an interval from a start and end instant.
     * 
     * @param startInstant  start of this interval, as milliseconds from 1970-01-01T00:00:00Z.
     * @param endInstant  end of this interval, as milliseconds from 1970-01-01T00:00:00Z.
     */
    public Interval(long startInstant, long endInstant) {
        super(startInstant, endInstant);
    }

    /**
     * Constructs a time interval as a copy of another.
     * 
     * @param interval  the time interval to copy
     * @throws IllegalArgumentException if the interval is null or invalid
     */
    public Interval(Object interval) {
        super(interval);
    }

    /**
     * Constructs an interval from a start and end instant.
     * 
     * @param start  start of this interval, null means now
     * @param end  end of this interval, null means now
     */
    public Interval(ReadableInstant start, ReadableInstant end) {
        super(start, end);
    }

    /**
     * Constructs an interval from a start instant and a millisecond duration.
     * 
     * @param start  start of this interval, null means now
     * @param duration  the duration of this interval, null means zero length
     * @throws ArithmeticException if the end instant exceeds the capacity of a long
     */
    public Interval(ReadableInstant start, ReadableDuration duration) {
        super(start, duration);
    }

    /**
     * Constructs an interval from a millisecond duration and an end instant.
     * 
     * @param duration  the duration of this interval, null means zero length
     * @param end  end of this interval, null means now
     * @throws ArithmeticException if the start instant exceeds the capacity of a long
     */
    public Interval(ReadableDuration duration, ReadableInstant end) {
        super(duration, end);
    }

    /**
     * Constructs an interval from a start instant and a time period.
     * <p>
     * When forming the interval, the chronology from the instant is used
     * if present, otherwise the chronology of the period is used.
     * 
     * @param start  start of this interval, null means now
     * @param period  the period of this interval, null means zero length
     * @throws ArithmeticException if the end instant exceeds the capacity of a long
     */
    public Interval(ReadableInstant start, ReadablePeriod period) {
        super(start, period);
    }

    /**
     * Constructs an interval from a time period and an end instant.
     * <p>
     * When forming the interval, the chronology from the instant is used
     * if present, otherwise the chronology of the period is used.
     * 
     * @param period  the period of this interval, null means zero length
     * @param end  end of this interval, null means now
     * @throws ArithmeticException if the start instant exceeds the capacity of a long
     */
    public Interval(ReadablePeriod period, ReadableInstant end) {
        super(period, end);
    }

    //-----------------------------------------------------------------------
    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    public void setInterval(ReadableInterval interval) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    public void setInterval(long startInstant, long endInstant) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    protected final void setStartMillis(long millisInstant) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    protected final void setEndMillis(long millisInstant) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    protected void setDurationAfterStart(ReadableDuration duration) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are immutable.
     */
    protected void setDurationBeforeEnd(ReadableDuration duration) {
    }

}
