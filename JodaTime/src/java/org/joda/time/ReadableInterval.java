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

/** 
 * Readable interface for an interval of time between two instants.
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
 * Methods that are passed an interval as a parameter will treat <code>null</code>
 * as a zero length interval at the current instant in time.
 *
 * @author Sean Geoghegan
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public interface ReadableInterval {

    /**
     * Gets the chronology of the interval, which is the chronology of the first datetime.
     *
     * @return the chronology of the interval
     */
    Chronology getChronology();

    /**
     * Gets the start of this time interval which is inclusive.
     *
     * @return the start of the time interval,
     *  millisecond instant from 1970-01-01T00:00:00Z
     */
    long getStartMillis();

    /**
     * Gets the start of this time interval, which is inclusive, as a DateTime.
     *
     * @return the start of the time interval
     */
    DateTime getStart();

    /** 
     * Gets the end of this time interval which is exclusive.
     *
     * @return the end of the time interval,
     *  millisecond instant from 1970-01-01T00:00:00Z
     */
    long getEndMillis();

    /** 
     * Gets the end of this time interval, which is exclusive, as a DateTime.
     *
     * @return the end of the time interval
     */
    DateTime getEnd();

    //-----------------------------------------------------------------------
    /**
     * Does this time interval contain the specified instant.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @param instant  the instant, null means now
     * @return true if this time interval contains the instant
     */
    boolean contains(ReadableInstant instant);
    
    /**
     * Does this time interval contain the specified time interval completely.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @param interval  the time interval to compare to, null means now
     * @return true if this time interval contains the time interval
     */
    boolean contains(ReadableInterval interval);
    
    /**
     * Does this time interval overlap the specified time interval.
     * <p>
     * The intervals overlap if at least some of the time interval is in common.
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @param interval  the time interval to compare to, null means now
     * @return true if the time intervals overlap
     */
    boolean overlaps(ReadableInterval interval);
    
    //-----------------------------------------------------------------------
    /**
     * Is this time interval after the specified instant.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @param instant  the instant to compare to, null means now
     * @return true if this time interval is after the instant
     */
    boolean isAfter(ReadableInstant instant);
    
    /**
     * Is this time interval entirely after the specified interval.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @param interval  the interval to compare to, null means now
     * @return true if this time interval is after the interval specified
     */
    boolean isAfter(ReadableInterval interval);
    
    /**
     * Is this time interval before the specified instant.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @param instant  the instant to compare to, null means now
     * @return true if this time interval is before the instant
     */
    boolean isBefore(ReadableInstant instant);
    
    /**
     * Is this time interval entirely before the specified interval.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * 
     * @param interval  the interval to compare to, null means now
     * @return true if this time interval is before the interval specified
     */
    boolean isBefore(ReadableInterval interval);
    
    //-----------------------------------------------------------------------
    /**
     * Get this interval as an immutable <code>Interval</code> object.
     * <p>
     * This will either typecast this instance, or create a new <code>Interval</code>.
     *
     * @return the interval as an Interval object
     */
    Interval toInterval();

    /**
     * Get this time interval as a <code>MutableInterval</code>.
     * <p>
     * This will always return a new <code>MutableInterval</code> with the same interval.
     *
     * @return the time interval as a MutableInterval object
     */
    MutableInterval toMutableInterval();

    //-----------------------------------------------------------------------
    /**
     * Gets the millisecond duration of this time interval.
     *
     * @return the millisecond duration of the time interval
     * @throws ArithmeticException if the duration exceeds the capacity of a long
     */
    Duration toDuration();

    /**
     * Gets the millisecond duration of this time interval.
     *
     * @return the millisecond duration of the time interval
     * @throws ArithmeticException if the duration exceeds the capacity of a long
     */
    long toDurationMillis();

    /**
     * Converts the duration of the interval to a period using the
     * standard period type.
     * <p>
     * This method should be used to exract the field values describing the
     * difference between the start and end instants.
     *
     * @return a time period derived from the interval
     */
    Period toPeriod();

    /**
     * Converts the duration of the interval to a period using the
     * specified period type.
     * <p>
     * This method should be used to exract the field values describing the
     * difference between the start and end instants.
     *
     * @param type  the requested type of the duration, null means standard
     * @return a time period derived from the interval
     */
    Period toPeriod(PeriodType type);

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
    boolean equals(Object readableInterval);

    /**
     * Gets a hash code for the time interval that is compatable with the 
     * equals method.
     * <p>
     * The formula used must be as follows:
     * <pre>int result = 97;
     * result = 31 * result + ((int) (getStartMillis() ^ (getStartMillis() >>> 32)));
     * result = 31 * result + ((int) (getEndMillis() ^ (getEndMillis() >>> 32)));
     * result = 31 * result + getChronology().hashCode();
     * return result;</pre>
     *
     * @return a hash code
     */
    int hashCode();

    //-----------------------------------------------------------------------
    /**
     * Get the value as a String in the ISO8601 interval format.
     * <p>
     * For example, "2004-06-09T12:30:00.000/2004-07-10T13:30:00.000".
     *
     * @return the value as an ISO8601 string
     */
    String toString();

}
