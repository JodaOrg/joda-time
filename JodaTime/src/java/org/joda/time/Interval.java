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

    /** The start of the period */
    private final long iStartMillis;
    /** The end of the period */
    private final long iEndMillis;

    /** The cached duration */
    private transient Duration iDuration;

    /**
     * Constructs an interval from a start and end instant.
     * 
     * @param startInstant  start of this interval, as milliseconds from 1970-01-01T00:00:00Z.
     * @param endInstant  end of this interval, as milliseconds from 1970-01-01T00:00:00Z.
     * @throws IllegalArgumentException if the end is before the start
     */
    public Interval(long startInstant, long endInstant) {
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
    public Interval(ReadableInstant start, ReadableInstant end) {
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
    public Interval(ReadableInstant start, ReadableDuration duration) {
        super();
        iStartMillis = DateTimeUtils.getInstantMillis(start);
        long durationMillis = DateTimeUtils.getDurationMillis(duration);
        iEndMillis = FieldUtils.safeAdd(iStartMillis, durationMillis);
        if (duration instanceof Duration) {
            iDuration = (Duration) duration;
        }
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
    public Interval(ReadableDuration duration, ReadableInstant end) {
        super();
        iEndMillis = DateTimeUtils.getInstantMillis(end);
        long durationMillis = DateTimeUtils.getDurationMillis(duration);
        iStartMillis = FieldUtils.safeAdd(iEndMillis, -durationMillis);
        if (duration instanceof Duration) {
            iDuration = (Duration) duration;
        }
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
    public Interval(ReadableInstant start, ReadablePeriod period) {
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
    public Interval(ReadablePeriod period, ReadableInstant end) {
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
    public Interval(Object interval) {
        super();
        IntervalConverter converter = ConverterManager.getInstance().getIntervalConverter(interval);
        long[] millis = converter.getIntervalMillis(interval);
        iStartMillis = millis[0];
        iEndMillis = millis[1];
        checkInterval(iStartMillis, iEndMillis);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the start of this time interval which is inclusive.
     *
     * @return the start of the time interval,
     *  millisecond instant from 1970-01-01T00:00:00Z
     */
    public final long getStartMillis() {
        return iStartMillis;
    }

    /**
     * Gets the end of this time interval which is exclusive.
     *
     * @return the end of the time interval,
     *  millisecond instant from 1970-01-01T00:00:00Z
     */
    public final long getEndMillis() {
        return iEndMillis;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the duration of this interval.
     * <p>
     * Where possible, this implementation reuses the same <code>Duration</code> object.
     *
     * @return the duration of the interval
     */
    public final Duration getDuration() {
        if (iDuration == null) {
            iDuration = super.getDuration();
        }
        return iDuration;
    }

    //-----------------------------------------------------------------------
    // Protect against malicious subclasses, ensuring this class is immutable
    //-----------------------------------------------------------------------
    protected final void checkInterval(long start, long end) {
        super.checkInterval(start, end);
    }

    protected final long getEndInstantMillis(ReadableInstant end, ReadableInstant start, long startMillis) {
        return super.getEndInstantMillis(end, start, startMillis);
    }

    //-----------------------------------------------------------------------
    public final long getDurationMillis() {
        return super.getDurationMillis();
    }

    public final Instant getStartInstant() {
        return super.getStartInstant();
    }

    public final Instant getEndInstant() {
        return super.getEndInstant();
    }

    //-----------------------------------------------------------------------
    public final boolean contains(long millisInstant) {
        return super.contains(millisInstant);
    }

    public final boolean containsNow() {
        return super.containsNow();
    }

    public final boolean contains(ReadableInstant instant) {
        return super.contains(instant);
    }

    public final boolean contains(ReadableInterval interval) {
        return super.contains(interval);
    }

    public final boolean overlaps(ReadableInterval interval) {
        return super.overlaps(interval);
    }

    //-----------------------------------------------------------------------
    public final boolean equals(Object readableInterval) {
        return super.equals(readableInterval);
    }

    public final int hashCode() {
        return super.hashCode();
    }

    //-----------------------------------------------------------------------
    public final boolean isAfter(long millisInstant) {
        return super.isAfter(millisInstant);
    }

    public final boolean isAfterNow() {
        return super.isAfterNow();
    }

    public final boolean isAfter(ReadableInstant instant) {
        return super.isAfter(instant);
    }

    public final boolean isBefore(long millisInstant) {
        return super.isBefore(millisInstant);
    }

    public final boolean isBeforeNow() {
        return super.isBeforeNow();
    }

    public final boolean isBefore(ReadableInstant instant) {
        return super.isBefore(instant);
    }

    //-----------------------------------------------------------------------
    public final Interval toInterval() {
        return super.toInterval();
    }

    public final MutableInterval toMutableInterval() {
        return super.toMutableInterval();
    }

    //-----------------------------------------------------------------------
    public final Period toPeriod() {
        return super.toPeriod();
    }

    public final Period toPeriod(PeriodType type) {
        return super.toPeriod(type);
    }

    //-----------------------------------------------------------------------
    public final String toString() {
        return super.toString();
    }

}
