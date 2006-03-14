/*
 *  Copyright 2001-2006 Stephen Colebourne
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
package org.joda.time.base;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.MutableInterval;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadableInterval;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;
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

    /**
     * Constructor.
     */
    protected AbstractInterval() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * Validates an interval.
     * 
     * @param start  the start instant in milliseconds
     * @param end  the end instant in milliseconds
     * @throws IllegalArgumentException if the interval is invalid
     */
    protected void checkInterval(long start, long end) {
        if (end < start) {
            throw new IllegalArgumentException("The end instant must be greater or equal to the start");
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the start of this time interval, which is inclusive, as a DateTime.
     *
     * @return the start of the time interval
     */
    public DateTime getStart() {
        return new DateTime(getStartMillis(), getChronology());
    }

    /** 
     * Gets the end of this time interval, which is exclusive, as a DateTime.
     *
     * @return the end of the time interval
     */
    public DateTime getEnd() {
        return new DateTime(getEndMillis(), getChronology());
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
    public boolean contains(long millisInstant) {
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
    public boolean containsNow() {
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
    public boolean contains(ReadableInstant instant) {
        if (instant == null) {
            return containsNow();
        }
        return contains(instant.getMillis());
    }

    /**
     * Does this time interval contain the specified time interval completely.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @param interval  the time interval to compare to, null means now
     * @return true if this time interval contains the time interval
     */
    public boolean contains(ReadableInterval interval) {
        if (interval == null) {
            return containsNow();
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
     * @param interval  the time interval to compare to, null means now
     * @return true if the time intervals overlap
     */
    public boolean overlaps(ReadableInterval interval) {
        if (interval == null) {
            return containsNow();
        }
        long otherStart = interval.getStartMillis();
        long otherEnd = interval.getEndMillis();
        long thisStart = getStartMillis();
        long thisEnd = getEndMillis();
        
        return (thisStart < otherEnd && otherStart < thisEnd) ||
            (thisStart == otherStart &&
                    (thisStart == thisEnd || otherStart == otherEnd));
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
    public boolean isBefore(long millisInstant) {
        return (getEndMillis() <= millisInstant);
    }

    /**
     * Is this time interval before the current instant.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @return true if this time interval is before the current instant
     */
    public boolean isBeforeNow() {
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
    public boolean isBefore(ReadableInstant instant) {
        if (instant == null) {
            return isBeforeNow();
        }
        return isBefore(instant.getMillis());
    }

    /**
     * Is this time interval entirely before the specified instant.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @param interval  the interval to compare to, null means now
     * @return true if this time interval is before the interval specified
     */
    public boolean isBefore(ReadableInterval interval) {
        if (interval == null) {
            return isBeforeNow();
        }
        return isBefore(interval.getStartMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Is this time interval after the specified millisecond instant.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @param millisInstant  the instant to compare to,
     *  millisecond instant from 1970-01-01T00:00:00Z
     * @return true if this time interval is after the instant
     */
    public boolean isAfter(long millisInstant) {
        return (getStartMillis() > millisInstant);
    }

    /**
     * Is this time interval after the current instant.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @return true if this time interval is after the current instant
     */
    public boolean isAfterNow() {
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
    public boolean isAfter(ReadableInstant instant) {
        if (instant == null) {
            return isAfterNow();
        }
        return isAfter(instant.getMillis());
    }

    /**
     * Is this time interval entirely after the specified interval.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * Only the end time of the specified interval is used in the comparison.
     * 
     * @param interval  the interval to compare to, null means now
     * @return true if this time interval is after the interval specified
     */
    public boolean isAfter(ReadableInterval interval) {
        long endMillis;
        if (interval == null) {
            endMillis = DateTimeUtils.currentTimeMillis();
        } else {
            endMillis = interval.getEndMillis();
        }
        return (getStartMillis() >= endMillis);
    }

    //-----------------------------------------------------------------------
    /**
     * Get this interval as an immutable <code>Interval</code> object.
     *
     * @return the interval as an Interval object
     */
    public Interval toInterval() {
        return new Interval(getStartMillis(), getEndMillis(), getChronology());
    }

    /**
     * Get this time interval as a <code>MutableInterval</code>.
     * <p>
     * This will always return a new <code>MutableInterval</code> with the same interval.
     *
     * @return the time interval as a MutableInterval object
     */
    public MutableInterval toMutableInterval() {
        return new MutableInterval(getStartMillis(), getEndMillis(), getChronology());
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
    public long toDurationMillis() {
        return FieldUtils.safeAdd(getEndMillis(), -getStartMillis());
    }

    /**
     * Gets the duration of this time interval.
     * <p>
     * The duration is equal to the end millis minus the start millis.
     *
     * @return the duration of the time interval
     * @throws ArithmeticException if the duration exceeds the capacity of a long
     */
    public Duration toDuration() {
        long durMillis = toDurationMillis();
        if (durMillis == 0) {
            return Duration.ZERO;
        } else {
            return new Duration(durMillis);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Converts the duration of the interval to a <code>Period</code> using the
     * All period type.
     * <p>
     * This method should be used to exract the field values describing the
     * difference between the start and end instants.
     *
     * @return a time period derived from the interval
     */
    public Period toPeriod() {
        return new Period(getStartMillis(), getEndMillis(), getChronology());
    }

    /**
     * Converts the duration of the interval to a <code>Period</code> using the
     * specified period type.
     * <p>
     * This method should be used to exract the field values describing the
     * difference between the start and end instants.
     *
     * @param type  the requested type of the duration, null means AllType
     * @return a time period derived from the interval
     */
    public Period toPeriod(PeriodType type) {
        return new Period(getStartMillis(), getEndMillis(), type, getChronology());
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this object with the specified object for equality based
     * on start and end millis plus the chronology.
     * All ReadableInterval instances are accepted.
     * <p>
     * To compare the duration of two time intervals, use {@link #toDuration()}
     * to get the durations and compare those.
     *
     * @param readableInterval  a readable interval to check against
     * @return true if the start and end millis are equal
     */
    public boolean equals(Object readableInterval) {
        if (this == readableInterval) {
            return true;
        }
        if (readableInterval instanceof ReadableInterval == false) {
            return false;
        }
        ReadableInterval other = (ReadableInterval) readableInterval;
        return (getStartMillis() == other.getStartMillis() &&
                getEndMillis() == other.getEndMillis() &&
                getChronology() == other.getChronology());
    }

    /**
     * Hashcode compatible with equals method.
     *
     * @return suitable hashcode
     */
    public int hashCode() {
        long start = getStartMillis();
        long end = getEndMillis();
        int result = 97;
        result = 31 * result + ((int) (start ^ (start >>> 32)));
        result = 31 * result + ((int) (end ^ (end >>> 32)));
        result = 31 * result + getChronology().hashCode();
        return result;
    }

    /**
     * Output a string in ISO8601 interval format.
     *
     * @return re-parsable string
     */
    public String toString() {
        DateTimeFormatter printer = ISODateTimeFormat.dateHourMinuteSecondFraction();
        printer = printer.withChronology(getChronology());
        StringBuffer buf = new StringBuffer(48);
        printer.printTo(buf, getStartMillis());
        buf.append('/');
        printer.printTo(buf, getEndMillis());
        return buf.toString();
    }

}
