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

import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.IntervalConverter;
import org.joda.time.field.FieldUtils;

/**
 * MutableInterval is the standard implementation of a mutable time interval.
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
 * If performing significant calculations on an interval, it may be faster to
 * convert an Interval object to a MutableInterval one.
 * <p>
 * MutableInterval is mutable and not thread-safe, unless concurrent threads
 * are not invoking mutator methods.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public class MutableInterval
        extends AbstractInterval
        implements ReadWritableInterval, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = -5982824024992428470L;

    /** The start of the period */
    private long iStartMillis;
    /** The end of the period */
    private long iEndMillis;

    /**
     * Constructs a zero length time interval from 1970-01-01 to 1970-01-01.
     */
    public MutableInterval() {
        super();
    }

    /**
     * Constructs an interval from a start and end instant.
     * 
     * @param startInstant  start of this interval, as milliseconds from 1970-01-01T00:00:00Z.
     * @param endInstant  end of this interval, as milliseconds from 1970-01-01T00:00:00Z.
     * @throws IllegalArgumentException if the end is before the start
     */
    public MutableInterval(long startInstant, long endInstant) {
        super();
        checkInterval(startInstant, endInstant);
        iStartMillis = startInstant;
        iEndMillis = endInstant;
    }

    /**
     * Constructs an interval from a start and end instant.
     * 
     * @param start  start of this interval, null means now
     * @param end  end of this interval, null means now
     * @throws IllegalArgumentException if the end is before the start
     */
    public MutableInterval(ReadableInstant start, ReadableInstant end) {
        super();
        iStartMillis = DateTimeUtils.getInstantMillis(start);
        iEndMillis = getEndInstantMillis(end, start, iStartMillis);
        checkInterval(iStartMillis, iEndMillis);
    }

    /**
     * Constructs an interval from a start instant and a duration.
     * 
     * @param start  start of this interval, null means now
     * @param duration  the duration of this interval, null means zero length
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the end instant exceeds the capacity of a long
     */
    public MutableInterval(ReadableInstant start, ReadableDuration duration) {
        super();
        iStartMillis = DateTimeUtils.getInstantMillis(start);
        long durationMillis = DateTimeUtils.getDurationMillis(duration);
        iEndMillis = FieldUtils.safeAdd(iStartMillis, durationMillis);
        checkInterval(iStartMillis, iEndMillis);
    }

    /**
     * Constructs an interval from a millisecond duration and an end instant.
     * 
     * @param duration  the duration of this interval, null means zero length
     * @param end  end of this interval, null means now
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the start instant exceeds the capacity of a long
     */
    public MutableInterval(ReadableDuration duration, ReadableInstant end) {
        super();
        iEndMillis = DateTimeUtils.getInstantMillis(end);
        long durationMillis = DateTimeUtils.getDurationMillis(duration);
        iStartMillis = FieldUtils.safeAdd(iEndMillis, -durationMillis);
        checkInterval(iStartMillis, iEndMillis);
    }

    /**
     * Constructs an interval from a start instant and a time period.
     * <p>
     * When forming the interval, the chronology from the instant is used
     * if present, otherwise the chronology of the period is used.
     * 
     * @param start  start of this interval, null means now
     * @param period  the period of this interval, null means zero length
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the end instant exceeds the capacity of a long
     */
    public MutableInterval(ReadableInstant start, ReadablePeriod period) {
        super();
        iStartMillis = DateTimeUtils.getInstantMillis(start);
        if (period == null) {
            iEndMillis = iStartMillis;
        } else {
            Chronology chrono = DateTimeUtils.getInstantChronology(start, null);
            iEndMillis = period.addTo(iStartMillis, 1, chrono);
        }
        checkInterval(iStartMillis, iEndMillis);
    }

    /**
     * Constructs an interval from a time period and an end instant.
     * <p>
     * When forming the interval, the chronology from the instant is used
     * if present, otherwise the chronology of the period is used.
     * 
     * @param period  the period of this interval, null means zero length
     * @param end  end of this interval, null means now
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the start instant exceeds the capacity of a long
     */
    public MutableInterval(ReadablePeriod period, ReadableInstant end) {
        super();
        iEndMillis = DateTimeUtils.getInstantMillis(end);
        if (period == null) {
            iStartMillis = iEndMillis;
        } else {
            Chronology chrono = DateTimeUtils.getInstantChronology(end, null);
            iStartMillis = period.addTo(iEndMillis, -1, chrono);
        }
        checkInterval(iStartMillis, iEndMillis);
    }

    /**
     * Constructs a time interval as a copy of another.
     * 
     * @param interval  the time interval to copy
     * @throws IllegalArgumentException if the interval is null or invalid
     */
    public MutableInterval(Object interval) {
        super();
        IntervalConverter converter = ConverterManager.getInstance().getIntervalConverter(interval);
        converter.setInto(this, interval);
        checkInterval(iStartMillis, iEndMillis);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the start of this time interval which is inclusive.
     *
     * @return the start of the time interval,
     *  millisecond instant from 1970-01-01T00:00:00Z
     */
    public long getStartMillis() {
        return iStartMillis;
    }

    /** 
     * Gets the end of this time interval which is exclusive.
     *
     * @return the end of the time interval,
     *  millisecond instant from 1970-01-01T00:00:00Z
     */
    public long getEndMillis() {
        return iEndMillis;
    }

    //-----------------------------------------------------------------------
    /**
     * Sets this interval from two millisecond instants.
     * <p>
     * All updates ocurr via this method (exclusing the constructors).
     *
     * @param startInstant  the start of the time interval
     * @param endInstant  the start of the time interval
     * @throws IllegalArgumentException if the end is before the start
     */
    public void setInterval(long startInstant, long endInstant) {
        if (startInstant != iStartMillis || endInstant != iEndMillis) {
            checkInterval(startInstant, endInstant);
            iStartMillis = startInstant;
            iEndMillis = endInstant;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets this interval to be the same as another.
     *
     * @param interval  the interval to copy
     * @throws IllegalArgumentException if the interval is null
     */
    public void setInterval(ReadableInterval interval) {
        if (interval == null) {
            throw new IllegalArgumentException("Interval must not be null");
        }
        long startMillis = interval.getStartMillis();
        long endMillis = interval.getEndMillis();
        setInterval(startMillis, endMillis);
    }

    /**
     * Sets this interval from two instants.
     *
     * @param start  the start of the time interval
     * @param end  the start of the time interval
     * @throws IllegalArgumentException if the end is before the start
     */
    public void setInterval(ReadableInstant start, ReadableInstant end) {
        long startMillis = DateTimeUtils.getInstantMillis(start);
        long endMillis = getEndInstantMillis(end, start, iStartMillis);
        setInterval(startMillis, endMillis);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the start of this time interval.
     *
     * @param startInstant  the start of the time interval,
     *  millisecond instant from 1970-01-01T00:00:00Z
     * @throws IllegalArgumentException if the end is before the start
     */
    public void setStartMillis(long startInstant) {
        setInterval(startInstant, iEndMillis);
    }

    /**
     * Sets the start of this time interval as an Instant.
     *
     * @param start  the start of the time interval, null means now
     * @throws IllegalArgumentException if the end is before the start
     */
    public void setStartInstant(ReadableInstant start) {
        long startMillis = DateTimeUtils.getInstantMillis(start);
        setInterval(startMillis, iEndMillis);
    }

    //-----------------------------------------------------------------------
    /** 
     * Sets the end of this time interval.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is public and final. This also ensures that
     * all lower subclasses are also immutable.
     *
     * @param endInstant  the end of the time interval,
     *  millisecond instant from 1970-01-01T00:00:00Z
     * @throws IllegalArgumentException if the end is before the start
     */
    public void setEndMillis(long endInstant) {
        setInterval(iStartMillis, endInstant);
    }

    /** 
     * Sets the end of this time interval as an Instant.
     *
     * @param instant  the end of the time interval, null means now
     * @throws IllegalArgumentException if the end is before the start
     */
    public void setEndInstant(ReadableInstant end) {
        long endMillis = DateTimeUtils.getInstantMillis(end);
        setInterval(iStartMillis, endMillis);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the duration of this time interval, preserving the start instant.
     *
     * @param duration  new duration for interval
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the end instant exceeds the capacity of a long
     */
    public void setDurationAfterStart(long duration) {
        setEndMillis(FieldUtils.safeAdd(getStartMillis(), duration));
    }

    /**
     * Sets the duration of this time interval, preserving the end instant.
     *
     * @param duration  new duration for interval
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the start instant exceeds the capacity of a long
     */
    public void setDurationBeforeEnd(long duration) {
        setStartMillis(FieldUtils.safeAdd(getEndMillis(), -duration));
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the duration of this time interval, preserving the start instant.
     *
     * @param duration  new duration for interval, null means zero length
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the end instant exceeds the capacity of a long
     */
    public void setDurationAfterStart(ReadableDuration duration) {
        long durationMillis = DateTimeUtils.getDurationMillis(duration);
        setEndMillis(FieldUtils.safeAdd(getStartMillis(), durationMillis));
    }

    /**
     * Sets the duration of this time interval, preserving the end instant.
     *
     * @param duration  new duration for interval, null means zero length
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the start instant exceeds the capacity of a long
     */
    public void setDurationBeforeEnd(ReadableDuration duration) {
        long durationMillis = DateTimeUtils.getDurationMillis(duration);
        setStartMillis(FieldUtils.safeAdd(getStartMillis(), -durationMillis));
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the period of this time interval, preserving the start instant.
     *
     * @param period  new period for interval, null means zero length
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the end instant exceeds the capacity of a long
     */
    public void setPeriodAfterStart(ReadablePeriod period) {
        if (period == null) {
            setEndMillis(getStartMillis());
        } else {
            setEndMillis(period.addTo(getStartMillis(), 1));
        }
    }

    /**
     * Sets the period of this time interval, preserving the end instant.
     *
     * @param period  new period for interval, null means zero length
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the start instant exceeds the capacity of a long
     */
    public void setPeriodBeforeEnd(ReadablePeriod period) {
        if (period == null) {
            setStartMillis(getEndMillis());
        } else {
            setStartMillis(period.addTo(getEndMillis(), -1));
        }
    }

}
