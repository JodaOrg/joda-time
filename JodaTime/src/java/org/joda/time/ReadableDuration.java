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
 * Defines an exact duration of time in milliseconds.
 * <p>
 * The implementation of this interface may be mutable or immutable. This
 * interface only gives access to retrieve data, never to change it.
 *
 * @see ReadableInterval
 * @see ReadableTimePeriod
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public interface ReadableDuration extends Comparable {

    /**
     * Gets the total length of this duration in milliseconds.
     *
     * @return the total length of the time duration in milliseconds.
     */
    long getMillis();

    //-----------------------------------------------------------------------
    /**
     * Converts this duration to a Duration instance.
     * This can be useful if you don't trust the implementation of the interface
     * to be well-behaved, or to get a guaranteed immutable object.
     * 
     * @return a Duration created using the millisecond duration from this instance
     */
    Duration toDuration();

    //-----------------------------------------------------------------------
    /**
     * Converts this duration to a TimePeriod instance using the PreciseAll type.
     * <p>
     * The PreciseAll type fixes days at 24 hours, months ay 30 days and years at 365 days
     * thus the time period will be precise. As a result there is no loss of precision
     * with regards the length of the duration and the following code will work:
     * <pre>
     * Duration dur = new Duration(123456789L);
     * TimePeriod period = d.toTimePeriod();
     * Duration dur2 = period.toDuration();
     * // dur.getMillis() == dur2.getMillis()
     * </pre>
     * 
     * @return a TimePeriod created using the millisecond duration from this instance
     */
    TimePeriod toTimePeriod();

    /**
     * Converts this duration to a TimePeriod instance specifying a duration type
     * to control how the duration is split into fields.
     * <p>
     * If a non-precise duration type is used, the resulting time period will only
     * represent an approximation of the duration. As a result it will not be
     * possible to call {@link TimePeriod#toDuration()} to get the duration back.
     * 
     * @param type  the duration type determining how to split the duration into fields
     * @return a TimePeriod created using the millisecond duration from this instance
     */
    TimePeriod toTimePeriod(DurationType type);

    //-----------------------------------------------------------------------
    /**
     * Compares this duration with the specified duration based on length.
     *
     * @param obj  a duration to check against
     * @return negative value if this is less, 0 if equal, or positive value if greater
     * @throws NullPointerException if the object is null
     * @throws ClassCastException if the given object is not supported
     */
    int compareTo(Object obj);

    /**
     * Is the length of this duration equal to the duration passed in.
     *
     * @param duration  another duration to compare to, null means zero milliseconds
     * @return true if this duration is equal to than the duration passed in
     */
    boolean isEqual(ReadableDuration duration);

    /**
     * Is the length of this duration longer than the duration passed in.
     *
     * @param duration  another duration to compare to, null means zero milliseconds
     * @return true if this duration is equal to than the duration passed in
     */
    boolean isLongerThan(ReadableDuration duration);

    /**
     * Is the length of this duration shorter than the duration passed in.
     *
     * @param duration  another duration to compare to, null means zero milliseconds
     * @return true if this duration is equal to than the duration passed in
     */
    boolean isShorterThan(ReadableDuration duration);

    //-----------------------------------------------------------------------
    /**
     * Compares this object with the specified object for equality based
     * on the millisecond length. All ReadableDuration instances are accepted.
     *
     * @param readableDuration  a readable duration to check against
     * @return true if the length of the duration is equal
     */
    boolean equals(Object readableDuration);

    /**
     * Gets a hash code for the duration that is compatable with the 
     * equals method.
     * The following formula must be used:
     * <pre>
     *  long len = getMillis();
     *  return (int) (len ^ (len >>> 32));
     * </pre>
     *
     * @return a hash code
     */
    int hashCode();

    //-----------------------------------------------------------------------
    /**
     * Gets the value as a String in the ISO8601 duration format.
     * <p>
     * For example, "P6H3M7S" represents 6 hours, 3 minutes, 7 seconds.
     * The field values are determined using the PreciseAll duration type.
     *
     * @return the value as an ISO8601 string
     */
    String toString();

}
