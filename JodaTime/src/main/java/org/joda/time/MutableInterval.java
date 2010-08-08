/*
 *  Copyright 2001-2005 Stephen Colebourne
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

import org.joda.time.base.BaseInterval;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.ISOPeriodFormat;

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
        extends BaseInterval
        implements ReadWritableInterval, Cloneable, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = -5982824024992428470L;

    //-----------------------------------------------------------------------
    /**
     * Constructs a zero length time interval from 1970-01-01 to 1970-01-01.
     */
    public MutableInterval() {
        super(0L, 0L, null);
    }

    /**
     * Constructs an interval from a start and end instant with the ISO default chronology.
     * 
     * @param startInstant  start of this interval, as milliseconds from 1970-01-01T00:00:00Z.
     * @param endInstant  end of this interval, as milliseconds from 1970-01-01T00:00:00Z.
     * @throws IllegalArgumentException if the end is before the start
     */
    public MutableInterval(long startInstant, long endInstant) {
        super(startInstant, endInstant, null);
    }

    /**
     * Constructs an interval from a start and end instant with a chronology.
     * 
     * @param chronology  the chronology to use, null is ISO default
     * @param startInstant  start of this interval, as milliseconds from 1970-01-01T00:00:00Z.
     * @param endInstant  end of this interval, as milliseconds from 1970-01-01T00:00:00Z.
     * @throws IllegalArgumentException if the end is before the start
     */
    public MutableInterval(long startInstant, long endInstant, Chronology chronology) {
        super(startInstant, endInstant, chronology);
    }

    /**
     * Constructs an interval from a start and end instant.
     * <p>
     * The chronology used is that of the start instant.
     * 
     * @param start  start of this interval, null means now
     * @param end  end of this interval, null means now
     * @throws IllegalArgumentException if the end is before the start
     */
    public MutableInterval(ReadableInstant start, ReadableInstant end) {
        super(start, end);
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
        super(start, duration);
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
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the end instant exceeds the capacity of a long
     */
    public MutableInterval(ReadableInstant start, ReadablePeriod period) {
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
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the start instant exceeds the capacity of a long
     */
    public MutableInterval(ReadablePeriod period, ReadableInstant end) {
        super(period, end);
    }

    /**
     * Constructs a time interval by converting or copying from another object.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableInterval and String.
     * The String formats are described by {@link ISODateTimeFormat#dateTimeParser()}
     * and {@link ISOPeriodFormat#standard()}, and may be 'datetime/datetime',
     * 'datetime/period' or 'period/datetime'.
     * 
     * @param interval  the time interval to copy
     * @throws IllegalArgumentException if the interval is invalid
     */
    public MutableInterval(Object interval) {
        super(interval, null);
    }

    /**
     * Constructs a time interval by converting or copying from another object,
     * overriding the chronology.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableInterval and String.
     * The String formats are described by {@link ISODateTimeFormat#dateTimeParser()}
     * and {@link ISOPeriodFormat#standard()}, and may be 'datetime/datetime',
     * 'datetime/period' or 'period/datetime'.
     * 
     * @param interval  the time interval to copy
     * @param chronology  the chronology to use, null means ISO default
     * @throws IllegalArgumentException if the interval is invalid
     */
    public MutableInterval(Object interval, Chronology chronology) {
        super(interval, chronology);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets this interval from two millisecond instants retaining the chronology.
     *
     * @param startInstant  the start of the time interval
     * @param endInstant  the start of the time interval
     * @throws IllegalArgumentException if the end is before the start
     */
    public void setInterval(long startInstant, long endInstant) {
        super.setInterval(startInstant, endInstant, getChronology());
    }

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
        Chronology chrono = interval.getChronology();
        super.setInterval(startMillis, endMillis, chrono);
    }

    /**
     * Sets this interval from two instants, replacing the chronology with
     * that from the start instant.
     *
     * @param start  the start of the time interval
     * @param end  the start of the time interval
     * @throws IllegalArgumentException if the end is before the start
     */
    public void setInterval(ReadableInstant start, ReadableInstant end) {
        if (start == null && end == null) {
            long now = DateTimeUtils.currentTimeMillis();
            setInterval(now, now);
        } else {
            long startMillis = DateTimeUtils.getInstantMillis(start);
            long endMillis = DateTimeUtils.getInstantMillis(end);
            Chronology chrono = DateTimeUtils.getInstantChronology(start);
            super.setInterval(startMillis, endMillis, chrono);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the chronology of this time interval.
     *
     * @param chrono  the chronology to use, null means ISO default
     */
    public void setChronology(Chronology chrono) {
        super.setInterval(getStartMillis(), getEndMillis(), chrono);
    }

    /**
     * Sets the start of this time interval.
     *
     * @param startInstant  the start of the time interval,
     *  millisecond instant from 1970-01-01T00:00:00Z
     * @throws IllegalArgumentException if the end is before the start
     */
    public void setStartMillis(long startInstant) {
        super.setInterval(startInstant, getEndMillis(), getChronology());
    }

    /**
     * Sets the start of this time interval as an Instant.
     *
     * @param start  the start of the time interval, null means now
     * @throws IllegalArgumentException if the end is before the start
     */
    public void setStart(ReadableInstant start) {
        long startMillis = DateTimeUtils.getInstantMillis(start);
        super.setInterval(startMillis, getEndMillis(), getChronology());
    }

    /** 
     * Sets the end of this time interval.
     *
     * @param endInstant  the end of the time interval,
     *  millisecond instant from 1970-01-01T00:00:00Z
     * @throws IllegalArgumentException if the end is before the start
     */
    public void setEndMillis(long endInstant) {
        super.setInterval(getStartMillis(), endInstant, getChronology());
    }

    /** 
     * Sets the end of this time interval as an Instant.
     *
     * @param end  the end of the time interval, null means now
     * @throws IllegalArgumentException if the end is before the start
     */
    public void setEnd(ReadableInstant end) {
        long endMillis = DateTimeUtils.getInstantMillis(end);
        super.setInterval(getStartMillis(), endMillis, getChronology());
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
        setStartMillis(FieldUtils.safeAdd(getEndMillis(), -durationMillis));
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the period of this time interval, preserving the start instant
     * and using the ISOChronology in the default zone for calculations.
     *
     * @param period  new period for interval, null means zero length
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the end instant exceeds the capacity of a long
     */
    public void setPeriodAfterStart(ReadablePeriod period) {
        if (period == null) {
            setEndMillis(getStartMillis());
        } else {
            setEndMillis(getChronology().add(period, getStartMillis(), 1));
        }
    }

    /**
     * Sets the period of this time interval, preserving the end instant
     * and using the ISOChronology in the default zone for calculations.
     *
     * @param period  new period for interval, null means zero length
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the start instant exceeds the capacity of a long
     */
    public void setPeriodBeforeEnd(ReadablePeriod period) {
        if (period == null) {
            setStartMillis(getEndMillis());
        } else {
            setStartMillis(getChronology().add(period, getEndMillis(), -1));
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Clone this object without having to cast the returned object.
     *
     * @return a clone of the this object.
     */
    public MutableInterval copy() {
        return (MutableInterval) clone();
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
