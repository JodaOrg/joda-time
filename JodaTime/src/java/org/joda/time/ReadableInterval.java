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

/** 
 * Readable interface for a time interval.
 * <p>
 * A time interval represents a period of time between two instants.
 * This interval has a duration, represented separately by ReadableDuration.
 *
 * @author Sean Geoghegan
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public interface ReadableInterval {

    /**
     * Gets the start of this time interval.
     *
     * @return the start of the time interval,
     *  millisecond instant from 1970-01-01T00:00:00Z
     */
    long getStartMillis();

    /**
     * Gets the start of this time interval as an Instant.
     *
     * @return the start of the time interval
     */
    Instant getStartInstant();

    /** 
     * Gets the end of this time interval.
     *
     * @return the end of the time interval,
     *  millisecond instant from 1970-01-01T00:00:00Z
     */
    long getEndMillis();

    /** 
     * Gets the end of this time interval as an Instant.
     *
     * @return the end of the time interval
     */
    Instant getEndInstant();

    /** 
     * Gets the duration of this time interval in milliseconds.
     * <p>
     * The duration returned will always be precise because it is relative to
     * a known date.
     *
     * @return the duration of the time interval in milliseconds
     */
    long getDurationMillis();

    /** 
     * Gets the duration of this time interval.
     * <p>
     * The duration returned will always be precise because it is relative to
     * a known date.
     *
     * @return the duration of the time interval
     */
    Duration getDuration();

    /** 
     * Gets the duration of this time interval.
     *
     * @param type the requested type of the duration
     * @return the duration of the time interval
     */
    Duration getDuration(DurationType type);

    //-----------------------------------------------------------------------
    /**
     * Does this time interval contain the specified millisecond instant.
     * 
     * @param millisInstant  the instant to compare to,
     *  millisecond instant from 1970-01-01T00:00:00Z
     * @return true if this time interval contains the millisecond
     */
    public boolean contains(long millisInstant);
    
    /**
     * Does this time interval contain the specified instant.
     * 
     * @param instant  the instant
     * @return true if this time interval contains the instant
     * @throws IllegalArgumentException if the instant is null
     */
    public boolean contains(ReadableInstant instant);
    
    /**
     * Does this time interval contain the specified time interval completely.
     * 
     * @param interval  the time interval to compare to
     * @return true if this time interval contains the time interval
     * @throws IllegalArgumentException if the interval is null
     */
    public boolean contains(ReadableInterval interval);
    
    /**
     * Does this time interval overlap the specified time interval.
     * <p>
     * The intervals overlap if at least some of the time interval is in common.
     * 
     * @param interval  the time interval to compare to
     * @return true if the time intervals overlap
     * @throws IllegalArgumentException if the interval is null
     */
    public boolean overlaps(ReadableInterval interval);
    
    //-----------------------------------------------------------------------
    /**
     * Is this time interval before the specified millisecond instant.
     * 
     * @param millisInstant  the instant to compare to,
     *  millisecond instant from 1970-01-01T00:00:00Z
     * @return true if this time interval is before the instant
     */
    public boolean isBefore(long millisInstant);
    
    /**
     * Is this time interval before the specified instant.
     * 
     * @param instant  the instant to compare to
     * @return true if this time interval is before the instant
     * @throws IllegalArgumentException if the instant is null
     */
    public boolean isBefore(ReadableInstant instant);
    
    /**
     * Is this time interval after the specified millisecond instant.
     * 
     * @param millisInstant  the instant to compare to,
     *  millisecond instant from 1970-01-01T00:00:00Z
     * @return true if this time interval is after the instant
     */
    public boolean isAfter(long millisInstant);
    
    /**
     * Is this time interval after the specified instant.
     * 
     * @param instant  the instant to compare to
     * @return true if this time interval is after the instant
     * @throws IllegalArgumentException if the instant is null
     */
    public boolean isAfter(ReadableInstant instant);
    
    //-----------------------------------------------------------------------
    /**
     * Get the value as a simple immutable object. This can be useful if you
     * don't trust the implementation of the interface to be well-behaved, or
     * to get a guaranteed immutable object.
     *
     * @return the value as an Interval object
     */
    Interval toInterval();

    /**
     * Get this time interval as a MutableInterval object.
     *
     * @return the time interval as a MutableInterval object
     */
    MutableInterval toMutableInterval();

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
    boolean equals(Object readableInterval);

    /**
     * Gets a hash code for the time interval that is compatable with the 
     * equals method.
     * <p>
     * The formula used must be as follows:
     * <pre>int result = 97;
     * result = 31 * result + ((int) (getStartMillis() ^ (getStartMillis() >>> 32)));
     * result = 31 * result + ((int) (getEndMillis() ^ (getEndMillis() >>> 32)));
     * return result;</pre>
     *
     * @return a hash code
     */
    int hashCode();

    //-----------------------------------------------------------------------
    /**
     * Get the value as a String in the ISO8601 interval format.
     *
     * @return the value as an ISO8601 string
     */
    String toString();

}
