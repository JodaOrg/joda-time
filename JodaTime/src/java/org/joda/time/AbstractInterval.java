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

    /** The duration, which may be lazily set */
    private Duration iDuration;

    /** Cache the start instant */
    private transient Instant iStartInstant;
    /** Cache the end instant */
    private transient Instant iEndInstant;

    /**
     * Constructs a time interval as a copy of another.
     * 
     * @param interval the time interval to convert
     * @throws IllegalArgumentException if the interval is null or invalid
     */
    public AbstractInterval(Object interval) {
        super();
        Period duration;
        if (interval instanceof AbstractInterval) {
            AbstractInterval ri = (AbstractInterval) interval;
            iStartMillis = ri.iStartMillis;
            iStartInstant = ri.iStartInstant;
            iEndMillis = ri.iEndMillis;
            iEndInstant = ri.iEndInstant;
            iDuration = ri.iDuration;
            
        } else if (interval instanceof ReadableInterval) {
            ReadableInterval ri = (ReadableInterval) interval;
            iStartMillis = ri.getStartMillis();
            iEndMillis = ri.getEndMillis();
            
        } else {
            IntervalConverter converter = ConverterManager.getInstance().getIntervalConverter(interval);
            if (this instanceof ReadWritableInterval) {
                converter.setInto((ReadWritableInterval) this, interval);
            } else {
                MutableInterval mi = new MutableInterval();
                converter.setInto(mi, interval);
                iStartMillis = mi.getStartMillis();
                iEndMillis = mi.getEndMillis();
            }
        }
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
            iDuration = duration.toDuration();
        }
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
            iDuration = duration.toDuration();
        }
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
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the start of this interval as the number of milliseconds elapsed
     * since 1970-01-01T00:00:00Z.
     *
     * @return the start of the interval
     */
    public final long getStartMillis() {
        return iStartMillis;
    }

    /**
     * Gets the start of this time interval as an Instant.
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
     * Gets the end of this interval as the number of milliseconds elapsed
     * since 1970-01-01T00:00:00Z.
     *
     * @return the start of the interval
     */
    public final long getEndMillis() {
        return iEndMillis;
    }

    /**
     * Gets the end of this time interval as an Instant.
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
     * The duration returned will always be precise because it is relative to
     * a known date.
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
     * If this interval was constructed using a precise duration then that object will
     * be returned. Otherwise a new Duration instance using the MillisType is returned.
     *
     * @return the precise duration of the time interval
     * @throws ArithmeticException if the duration exceeds the capacity of a long
     */
    public final Duration getDuration() {
        if (iDuration == null) {
            if (iStartMillis == iEndMillis) {
                iDuration = Duration.ZERO;
            } else {
                iDuration = new Duration(iStartMillis, iEndMillis);
            }
        }
        return iDuration;
    }

    //-----------------------------------------------------------------------
    /**
     * Does this time interval contain the specified millisecond instant.
     * 
     * @param millisInstant  the instant to compare to,
     *  millisecond instant from 1970-01-01T00:00:00Z
     * @return true if this time interval contains the millisecond
     */
    public final boolean contains(long millisInstant) {
        return (millisInstant >= getStartMillis() && millisInstant <= getEndMillis());
    }
    
    /**
     * Does this time interval contain the specified instant.
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
     * 
     * @param interval  the time interval to compare to
     * @return true if this interval contains the time interval
     * @throws IllegalArgumentException if the interval is null
     */
    public final boolean contains(ReadableInterval interval) {
        if (interval == null) {
            throw new IllegalArgumentException("The time interval must not be null");
        }
        long otherStart = interval.getStartMillis();
        long otherEnd = interval.getEndMillis();
        return 
            (otherStart >= getStartMillis() && otherStart <= getEndMillis())
            && (otherEnd >= getStartMillis() && otherEnd <= getEndMillis());
    }
    
    /**
     * Does this time interval overlap the specified time interval.
     * <p>
     * The intervals overlap if at least some of the time interval is in common.
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
        return 
            (otherStart >= getStartMillis() && otherStart <= getEndMillis())
            || (otherEnd >= getStartMillis() && otherEnd <= getEndMillis());
    }
    
    //-----------------------------------------------------------------------
    /**
     * Is this time interval before the specified millisecond instant.
     * 
     * @param millisInstant  the instant to compare to,
     *  millisecond instant from 1970-01-01T00:00:00Z
     * @return true if this time interval is before the instant
     */
    public final boolean isBefore(long millisInstant) {
        return (getStartMillis() < millisInstant && getEndMillis() < millisInstant);
    }
    
    /**
     * Is this time interval before the specified instant.
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
     * 
     * @param millisInstant  the instant to compare to,
     *  millisecond instant from 1970-01-01T00:00:00Z
     * @return true if this time interval is after the instant
     */
    public final boolean isAfter(long millisInstant) {
        return (getStartMillis() > millisInstant && getEndMillis() > millisInstant);
    }
    
    /**
     * Is this time interval after the specified instant.
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
     * Get the object as an Interval.
     * 
     * @return an immutable interval object
     */
    public final Interval toInterval() {
        if (this instanceof Interval) {
            return (Interval) this;
        }
        return new Interval(this);
    }

    /**
     * Get the object as a MutableInterval always returning a new instance.
     * 
     * @return a mutable interval object
     */
    public final MutableInterval toMutableInterval() {
        return new MutableInterval(this);
    }

    //-----------------------------------------------------------------------
    /**
     * Converts the duration of the interval to a time period using the
     * All period type.
     * <p>
     * This method should be used to exract the field values describing the
     * difference between the start and end instants.
     * The time period may not be precise - if you want the millisecond duration
     * then you should use {@link #getDuration()}.
     *
     * @param type  the requested type of the duration, null means AllType
     * @return a time period derived from the interval
     */
    public final Period toPeriod() {
        return new Period(getStartMillis(), getEndMillis());
    }

    /**
     * Converts the duration of the interval to a time period using the
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
        int result = 97;
        result = 31 * result + ((int) (getStartMillis() ^ (getStartMillis() >>> 32)));
        result = 31 * result + ((int) (getEndMillis() ^ (getEndMillis() >>> 32)));
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

    /**
     * Stores the duration of this time interval.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     *
     * @param duration  new duration for interval, null means zero length
     */
    protected void storeDuration(Duration duration) {
        // this method exists so that subclasses can block it
        iDuration = duration;
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
            storeDuration(duration.toDuration());
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
            storeDuration(duration.toDuration());
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
