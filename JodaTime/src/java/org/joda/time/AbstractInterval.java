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

import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.IntervalConverter;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimePrinter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * AbstractInterval provides the common behaviour for time intervals.
 * <p>
 * This class should generally not be used directly by API users. The 
 * {@link ReadableInterval} interface should be used when different 
 * kinds of intervals are to be referenced.
 * <p>
 * AbstractInterval subclasses may be mutable and not thread-safe.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public abstract class AbstractInterval implements ReadableInterval {

    /** The start of the period */
    private long iStartMillis;
    /** The end of the period */
    private long iEndMillis;

    /** Cache the duration */
    private transient Duration iDuration;
    /** Cache the start instant */
    private transient Instant iStartInstant;
    /** Cache the end instant */
    private transient Instant iEndInstant;

    /**
     * Constructs a time interval as a copy of another.
     * 
     * @param interval  the time interval to convert
     * @throws IllegalArgumentException if the interval is null or invalid
     */
    public AbstractInterval(Object interval) {
        super();
        IntervalConverter converter = ConverterManager.getInstance().getIntervalConverter(interval);
        if (this instanceof ReadWritableInterval) {
            converter.setInto((ReadWritableInterval) this, interval);
        } else {
            long[] millis = converter.getIntervalMillis(interval);
            iStartMillis = millis[0];
            iEndMillis = millis[1];
        }
        if (interval instanceof AbstractInterval) {
            AbstractInterval ri = (AbstractInterval) interval;
            if (iStartMillis == ri.iStartMillis && iEndMillis == ri.iEndMillis) {
                // this double checks against weird converters
                iStartInstant = ri.iStartInstant;
                iEndInstant = ri.iEndInstant;
                iDuration = ri.iDuration;
            }
        }
        checkInterval(iStartMillis, iEndMillis);
    }

    /**
     * Constructs an interval from a start and end instant.
     * 
     * @param startInstant  start of this interval, as milliseconds from
     *  1970-01-01T00:00:00Z.
     * @param endInstant  end of this interval, as milliseconds from
     *  1970-01-01T00:00:00Z.
     */
    public AbstractInterval(long startInstant, long endInstant) {
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
     */
    public AbstractInterval(ReadableInstant start, ReadableInstant end) {
        super();
        if (start == null && end == null) {
            iStartMillis = DateTimeUtils.currentTimeMillis();
            iEndMillis = iStartMillis;
        } else {
            if (start == null) {
                iStartMillis = DateTimeUtils.currentTimeMillis();
            } else {
                iStartMillis = start.getMillis();
                if (start instanceof Instant) {
                    iStartInstant = (Instant) start;
                }
            }
            if (end == null) {
                iEndMillis = DateTimeUtils.currentTimeMillis();
            } else {
                iEndMillis = end.getMillis();
                if (end instanceof Instant) {
                    iEndInstant = (Instant) end;
                }
            }
        }
        checkInterval(iStartMillis, iEndMillis);
    }

    /**
     * Constructs an interval from a start instant and a millisecond duration.
     * 
     * @param start  start of this interval, null means now
     * @param duration  the duration of this interval, null means zero length
     * @throws ArithmeticException if the end instant exceeds the capacity of a long
     */
    public AbstractInterval(ReadableInstant start, ReadableDuration duration) {
        super();
        Chronology chrono = null;
        if (start == null) {
            iStartMillis = DateTimeUtils.currentTimeMillis();
        } else {
            iStartMillis = start.getMillis();
            chrono = start.getChronology();
            if (start instanceof Instant) {
                iStartInstant = (Instant) start;
            }
        }
        if (duration == null) {
            iEndMillis = iStartMillis;
        } else {
            iEndMillis = FieldUtils.safeAdd(iStartMillis, duration.getMillis());
            if (duration instanceof Duration) {
                iDuration = (Duration) duration;
            }
        }
        checkInterval(iStartMillis, iEndMillis);
    }

    /**
     * Constructs an interval from a millisecond duration and an end instant.
     * 
     * @param duration  the duration of this interval, null means zero length
     * @param end  end of this interval, null means now
     * @throws ArithmeticException if the start instant exceeds the capacity of a long
     */
    public AbstractInterval(ReadableDuration duration, ReadableInstant end) {
        super();
        Chronology chrono = null;
        if (end == null) {
            iEndMillis = DateTimeUtils.currentTimeMillis();
        } else {
            iEndMillis = end.getMillis();
            chrono = end.getChronology();
            if (end instanceof Instant) {
                iEndInstant = (Instant) end;
            }
        }
        if (duration == null) {
            iStartMillis = iEndMillis;
        } else {
            iStartMillis = FieldUtils.safeAdd(iEndMillis, -duration.getMillis());
            if (duration instanceof Duration) {
                iDuration = (Duration) duration;
            }
        }
        checkInterval(iStartMillis, iEndMillis);
    }

    /**
     * Constructs an interval from a start instant and a period.
     * <p>
     * When forming the interval, the chronology from the instant is used
     * if present, otherwise the chronology of the period is used.
     * 
     * @param start  start of this interval, null means now
     * @param period  the period of this interval, null means zero length
     * @throws ArithmeticException if the end instant exceeds the capacity of a long
     */
    public AbstractInterval(ReadableInstant start, ReadablePeriod period) {
        super();
        Chronology chrono = null;
        if (start == null) {
            iStartMillis = DateTimeUtils.currentTimeMillis();
        } else {
            iStartMillis = start.getMillis();
            chrono = start.getChronology();
            if (start instanceof Instant) {
                iStartInstant = (Instant) start;
            }
        }
        if (period == null) {
            iEndMillis = iStartMillis;
        } else {
            iEndMillis = period.addTo(iStartMillis, 1, chrono);
        }
        checkInterval(iStartMillis, iEndMillis);
    }

    /**
     * Constructs an interval from a period and an end instant.
     * <p>
     * When forming the interval, the chronology from the instant is used
     * if present, otherwise the chronology of the period is used.
     * 
     * @param period  the period of this interval, null means zero length
     * @param end  end of this interval, null means now
     * @throws ArithmeticException if the start instant exceeds the capacity of a long
     */
    public AbstractInterval(ReadablePeriod period, ReadableInstant end) {
        super();
        Chronology chrono = null;
        if (end == null) {
            iEndMillis = DateTimeUtils.currentTimeMillis();
        } else {
            iEndMillis = end.getMillis();
            chrono = end.getChronology();
            if (end instanceof Instant) {
                iEndInstant = (Instant) end;
            }
        }
        if (period == null) {
            iStartMillis = iEndMillis;
        } else {
            iStartMillis = period.addTo(iEndMillis, -1, chrono);
        }
        checkInterval(iStartMillis, iEndMillis);
    }

    //-----------------------------------------------------------------------
    /**
     * Validates an interval.
     * 
     * @param start  the start instant in milliseconds
     * @param end  the end instant in milliseconds
     * @throws IllegalArgumentException if the interval is invalid
     */
    private void checkInterval(long start, long end) {
        if (end < start) {
            throw new IllegalArgumentException("The end instant must be greater or equal to the start");
        }
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
     * Gets the start of this time interval, which is inclusive, as an Instant.
     *
     * @return the start of the time interval
     */
    public final Instant getStartInstant() {
        if (iStartInstant == null) {
            iStartInstant = new Instant(getStartMillis());
        }
        return iStartInstant;
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

    /** 
     * Gets the end of this time interval, which is exclusive, as an Instant.
     *
     * @return the end of the time interval
     */
    public final Instant getEndInstant() {
        if (iEndInstant == null) {
            iEndInstant = new Instant(getEndMillis());
        }
        return iEndInstant;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the duration of this time interval in milliseconds.
     * <p>
     * The duration is equal to the end millis minus the start millis.
     *
     * @return the duration of the time interval in milliseconds
     * @throws ArithmeticException if the duration exceeds the capacity of a long
     */
    public final long getDurationMillis() {
        return FieldUtils.safeAdd(getEndMillis(), -getStartMillis());
    }

    /**
     * Gets the millisecond duration of this time interval.
     * <p>
     * If this interval was constructed using a Duration then that object will
     * be returned. Otherwise a new Duration instance is returned.
     *
     * @return the millisecond duration of the time interval
     * @throws ArithmeticException if the duration exceeds the capacity of a long
     */
    public final Duration getDuration() {
        if (iDuration == null) {
            long durMillis = getDurationMillis();
            if (durMillis == 0) {
                iDuration = Duration.ZERO;
            } else {
                iDuration = new Duration(durMillis);
            }
        }
        return iDuration;
    }

    //-----------------------------------------------------------------------
    /**
     * Does this time interval contain the specified millisecond instant.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @param millisInstant  the instant to compare to,
     *  millisecond instant from 1970-01-01T00:00:00Z
     * @return true if this time interval contains the millisecond
     */
    public final boolean contains(long millisInstant) {
        long thisStart = getStartMillis();
        long thisEnd = getEndMillis();
        return (millisInstant >= thisStart && millisInstant < thisEnd);
    }

    /**
     * Does this time interval contain the current instant.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @return true if this time interval contains the current instant
     */
    public final boolean containsNow() {
        return contains(DateTimeUtils.currentTimeMillis());
    }

    /**
     * Does this time interval contain the specified instant.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @param instant  the instant, null means now
     * @return true if this time interval contains the instant
     */
    public final boolean contains(ReadableInstant instant) {
        if (instant == null) {
            return contains(DateTimeUtils.currentTimeMillis());
        }
        return contains(instant.getMillis());
    }

    /**
     * Does this time interval contain the specified time interval completely.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @param interval  the time interval to compare to
     * @return true if this time interval contains the time interval
     * @throws IllegalArgumentException if the interval is null
     */
    public final boolean contains(ReadableInterval interval) {
        if (interval == null) {
            throw new IllegalArgumentException("The time interval must not be null");
        }
        long otherStart = interval.getStartMillis();
        long otherEnd = interval.getEndMillis();
        long thisStart = getStartMillis();
        long thisEnd = getEndMillis();
        return (otherStart >= thisStart && otherStart < thisEnd && otherEnd <= thisEnd);
    }

    /**
     * Does this time interval overlap the specified time interval.
     * <p>
     * The intervals overlap if at least some of the time interval is in common.
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @param interval  the time interval to compare to
     * @return true if the time intervals overlap
     * @throws IllegalArgumentException if the interval is null
     */
    public final boolean overlaps(ReadableInterval interval) {
        if (interval == null) {
            throw new IllegalArgumentException("The time interval must not be null");
        }
        long otherStart = interval.getStartMillis();
        long otherEnd = interval.getEndMillis();
        long thisStart = getStartMillis();
        long thisEnd = getEndMillis();
        return (thisStart < otherEnd && otherStart < thisEnd);
    }

    //-----------------------------------------------------------------------
    /**
     * Is this time interval before the specified millisecond instant.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @param millisInstant  the instant to compare to,
     *  millisecond instant from 1970-01-01T00:00:00Z
     * @return true if this time interval is before the instant
     */
    public final boolean isBefore(long millisInstant) {
        return (getEndMillis() <= millisInstant);
    }

    /**
     * Is this time interval before the current instant.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @return true if this time interval is before the current instant
     */
    public final boolean isBeforeNow() {
        return isBefore(DateTimeUtils.currentTimeMillis());
    }

    /**
     * Is this time interval before the specified instant.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @param instant  the instant to compare to, null means now
     * @return true if this time interval is before the instant
     */
    public final boolean isBefore(ReadableInstant instant) {
        if (instant == null) {
            return isBefore(DateTimeUtils.currentTimeMillis());
        }
        return isBefore(instant.getMillis());
    }

    /**
     * Is this time interval after the specified millisecond instant.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @param millisInstant  the instant to compare to,
     *  millisecond instant from 1970-01-01T00:00:00Z
     * @return true if this time interval is after the instant
     */
    public final boolean isAfter(long millisInstant) {
        return (getStartMillis() > millisInstant);
    }

    /**
     * Is this time interval after the current instant.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @return true if this time interval is after the current instant
     */
    public final boolean isAfterNow() {
        return isAfter(DateTimeUtils.currentTimeMillis());
    }

    /**
     * Is this time interval after the specified instant.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @param instant  the instant to compare to, null means now
     * @return true if this time interval is after the instant
     */
    public final boolean isAfter(ReadableInstant instant) {
        if (instant == null) {
            return isAfter(DateTimeUtils.currentTimeMillis());
        }
        return isAfter(instant.getMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Get this interval as an immutable <code>Interval</code> object.
     * <p>
     * This will either typecast this instance, or create a new <code>Interval</code>.
     *
     * @return the interval as an Interval object
     */
    public final Interval toInterval() {
        if (this instanceof Interval) {
            return (Interval) this;
        }
        return new Interval(this);
    }

    /**
     * Get this time interval as a <code>MutableInterval</code>.
     * <p>
     * This will always return a new <code>MutableInterval</code> with the same interval.
     *
     * @return the time interval as a MutableInterval object
     */
    public final MutableInterval toMutableInterval() {
        return new MutableInterval(this);
    }

    //-----------------------------------------------------------------------
    /**
     * Converts the duration of the interval to a <code>Period</code> using the
     * All period type.
     * <p>
     * This method should be used to exract the field values describing the
     * difference between the start and end instants.
     * The time period may not be precise - if you want the millisecond duration
     * then you should use {@link #getDuration()}.
     *
     * @return a time period derived from the interval
     */
    public final Period toPeriod() {
        return new Period(getStartMillis(), getEndMillis());
    }

    /**
     * Converts the duration of the interval to a <code>Period</code> using the
     * specified period type.
     * <p>
     * This method should be used to exract the field values describing the
     * difference between the start and end instants.
     * The time period may not be precise - if you want the millisecond duration
     * then you should use {@link #getDuration()}.
     *
     * @param type  the requested type of the duration, null means AllType
     * @return a time period derived from the interval
     */
    public final Period toPeriod(PeriodType type) {
        return new Period(getStartMillis(), getEndMillis(), type);
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this object with the specified object for equality based
     * on start and end millis. All ReadableInterval instances are accepted.
     * <p>
     * To compare the duration of two time intervals, use {@link #getDuration()}
     * to get the durations and compare those.
     *
     * @param readableInterval  a readable interval to check against
     * @return true if the start and end millis are equal
     */
    public final boolean equals(Object readableInterval) {
        if (this == readableInterval) {
            return true;
        }
        if (readableInterval instanceof ReadableInterval == false) {
            return false;
        }
        ReadableInterval other = (ReadableInterval) readableInterval;
        return (getStartMillis() == other.getStartMillis() &&
                getEndMillis() == other.getEndMillis());
    }

    /**
     * Hashcode compatible with equals method.
     *
     * @return suitable hashcode
     */
    public final int hashCode() {
        long start = getStartMillis();
        long end = getEndMillis();
        int result = 97;
        result = 31 * result + ((int) (start ^ (start >>> 32)));
        result = 31 * result + ((int) (end ^ (end >>> 32)));
        return result;
    }

    /**
     * Output a string in ISO8601 interval format.
     *
     * @return re-parsable string
     */
    public String toString() {
        DateTimePrinter printer =
            ISODateTimeFormat.getInstance(ISOChronology.getInstanceUTC())
            .dateHourMinuteSecondFraction();
        StringBuffer buf = new StringBuffer(48);
        printer.printTo(buf, getStartMillis());
        buf.append('/');
        printer.printTo(buf, getEndMillis());
        return buf.toString();
    }

    //-----------------------------------------------------------------------
    /**
     * Sets this interval to be the same as another.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     *
     * @param interval  the interval to copy
     */
    protected void setInterval(ReadableInterval interval) {
        if (interval instanceof AbstractInterval) {
            AbstractInterval other = (AbstractInterval) interval;
            iStartMillis = other.iStartMillis;
            iStartInstant = other.iStartInstant;
            iEndMillis = other.iEndMillis;
            iEndInstant = other.iEndInstant;
            iDuration = other.iDuration;
        } else {
            iStartMillis = interval.getStartMillis();
            iEndMillis = interval.getEndMillis();
        }
    }

    /**
     * Sets this interval from two millisecond instants.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     *
     * @param startInstant  the start of the time interval
     * @param endInstant  the start of the time interval
     */
    protected void setInterval(long startInstant, long endInstant) {
        if (startInstant != iStartMillis || endInstant != iEndMillis) {
            iStartMillis = startInstant;
            iStartInstant = null;
            iEndMillis = endInstant;
            iEndInstant = null;
            iDuration = null;
        }
    }

    /**
     * Sets this interval from two instants.
     *
     * @param startInstant  the start of the time interval
     * @param endInstant  the start of the time interval
     */
    protected void setInterval(ReadableInstant startInstant, ReadableInstant endInstant) {
        if (startInstant == null && endInstant == null) {
            long now = DateTimeUtils.currentTimeMillis();
            setInterval(now, now);
        } else if (startInstant == null) {
            long now = DateTimeUtils.currentTimeMillis();
            setInterval(now, endInstant.getMillis());
        } else if (startInstant == null) {
            long now = DateTimeUtils.currentTimeMillis();
            setInterval(startInstant.getMillis(), now);
        } else {
            setInterval(startInstant.getMillis(), endInstant.getMillis());
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the start of this time interval.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     *
     * @param millisInstant  the start of the time interval,
     *  millisecond instant from 1970-01-01T00:00:00Z
     */
    protected void setStartMillis(long millisInstant) {
        if (millisInstant != iStartMillis) {
            iStartMillis = millisInstant;
            iStartInstant = null;
            iDuration = null;
        }
    }

    /** 
     * Sets the end of this time interval.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     *
     * @param millisInstant  the end of the time interval,
     *  millisecond instant from 1970-01-01T00:00:00Z
     */
    protected void setEndMillis(long millisInstant) {
        if (millisInstant != iEndMillis) {
            iEndMillis = millisInstant;
            iEndInstant = null;
            iDuration = null;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the duration of this time interval, preserving the start instant.
     *
     * @param duration  new duration for interval
     * @throws ArithmeticException if the end instant exceeds the capacity of a long
     */
    protected void setDurationAfterStart(long duration) {
        setEndMillis(FieldUtils.safeAdd(getStartMillis(), duration));
    }

    /**
     * Sets the duration of this time interval, preserving the end instant.
     *
     * @param duration  new duration for interval
     * @throws ArithmeticException if the start instant exceeds the capacity of a long
     */
    protected void setDurationBeforeEnd(long duration) {
        setStartMillis(FieldUtils.safeAdd(getEndMillis(), -duration));
    }

    /**
     * Sets the duration of this time interval, preserving the start instant.
     *
     * @param duration  new duration for interval, null means zero length
     * @throws ArithmeticException if the end instant exceeds the capacity of a long
     */
    protected void setDurationAfterStart(ReadableDuration duration) {
        if (duration == null) {
            setEndMillis(getStartMillis());
        } else {
            setEndMillis(FieldUtils.safeAdd(getStartMillis(), duration.getMillis()));
            if (duration instanceof Duration) {
                iDuration = (Duration) duration;
            }
        }
    }

    /**
     * Sets the duration of this time interval, preserving the end instant.
     *
     * @param duration  new duration for interval, null means zero length
     * @throws ArithmeticException if the start instant exceeds the capacity of a long
     */
    protected void setDurationBeforeEnd(ReadableDuration duration) {
        if (duration == null) {
            setStartMillis(getEndMillis());
        } else {
            setStartMillis(FieldUtils.safeAdd(getEndMillis(), -duration.getMillis()));
            if (duration instanceof Duration) {
                iDuration = (Duration) duration;
            }
        }
    }

    /**
     * Sets the period of this time interval, preserving the start instant.
     *
     * @param period  new period for interval, null means zero length
     * @throws ArithmeticException if the end instant exceeds the capacity of a long
     */
    protected void setPeriodAfterStart(ReadablePeriod period) {
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
     * @throws ArithmeticException if the start instant exceeds the capacity of a long
     */
    protected void setPeriodBeforeEnd(ReadablePeriod period) {
        if (period == null) {
            setStartMillis(getEndMillis());
        } else {
            setStartMillis(period.addTo(getEndMillis(), -1));
        }
    }

}
