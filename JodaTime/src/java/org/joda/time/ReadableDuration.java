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
 * Defines a duration of time that can be queried using fields.
 * <p>
 * The implementation of this interface may be mutable or immutable. This
 * interface only gives access to retrieve data, never to change it.
 * <p>
 * Durations can be split up into multiple fields, but implementations are not
 * required to evenly distribute the values across the fields. Nor are they
 * required to normalize the fields nor match the signs.
 * <p>
 * For example, an implementation can represent a duration of "4 days, 6 hours"
 * as "102 hours", "1 day, 78 hours", "367200000 milliseconds", or even
 * "3 days, -8 hours, 2275 minutes, 298 seconds, 2000 milliseconds".
 *
 * @see ReadableInterval
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public interface ReadableDuration extends Comparable {

    /**
     * Returns the object which defines which fields this duration supports.
     */
    DurationType getDurationType();

    /**
     * Gets the total length of this duration in milliseconds, 
     * failing if the duration is imprecise.
     *
     * @return the total length of the time duration in milliseconds.
     * @throws IllegalStateException if the duration is imprecise
     */
    long getTotalMillis();

    /**
     * Is this duration a precise length of time, or descriptive.
     * <p>
     * A precise duration could include millis, seconds, minutes or hours.
     * However, days, weeks, months and years can vary in length, resulting in
     * an imprecise duration.
     * <p>
     * An imprecise duration can be made precise by pairing it with a
     * date in a {@link ReadableInterval}.
     *
     * @return true if the duration is precise
     */
    boolean isPrecise();

    //-----------------------------------------------------------------------
    /**
     * Adds this duration to the given instant, returning a new value.
     * <p>
     * To add just once, pass in a scalar of one. To subtract once, pass
     * in a scalar of minus one.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to add the
     * duration to
     * @param scalar  the number of times to add the duration, negative to subtract
     * @return milliseconds value plus this duration times scalar
     * @throws ArithmeticException if the result of the calculation is too large
     */
    long addTo(long instant, int scalar);

    /**
     * Adds this duration to the given instant, returning a new value.
     * <p>
     * To add just once, pass in a scalar of one. To subtract once, pass
     * in a scalar of minus one.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to add the
     * duration to
     * @param scalar  the number of times to add the duration, negative to subtract
     * @param chrono  override the duration's chronology, unless null is passed in
     * @return milliseconds value plus this duration times scalar
     * @throws ArithmeticException if the result of the calculation is too large
     */
    long addTo(long instant, int scalar, Chronology chrono);

    /**
     * Adds this duration to the given instant, returning a new Instant.
     * <p>
     * To add just once, pass in a scalar of one. To subtract once, pass
     * in a scalar of minus one.
     *
     * @param instant  the instant to add the duration to
     * @param scalar  the number of times to add the duration, negative to subtract
     * @return instant with the original value plus this duration times scalar
     * @throws IllegalArgumentException if the instant is null
     * @throws ArithmeticException if the result of the calculation is too large
     */
    ReadableInstant addTo(ReadableInstant instant, int scalar);

    /**
     * Adds this duration into the given mutable instant.
     * <p>
     * To add just once, pass in a scalar of one. To subtract once, pass
     * in a scalar of minus one.
     *
     * @param instant  the instant to update with the added duration
     * @param scalar  the number of times to add the duration, negative to subtract
     * @throws IllegalArgumentException if the instant is null
     * @throws ArithmeticException if the result of the calculation is too large
     */
    void addInto(ReadWritableInstant instant, int scalar);

    //-----------------------------------------------------------------------
    /**
     * Gets the years field part of the duration.
     * 
     * @return the number of years in the duration, zero if unsupported
     */
    int getYears();

    /**
     * Gets the months field part of the duration.
     * 
     * @return the number of months in the duration, zero if unsupported
     */
    int getMonths();

    /**
     * Gets the weeks field part of the duration.
     * 
     * @return the number of weeks in the duration, zero if unsupported
     */
    int getWeeks();

    /**
     * Gets the days field part of the duration.
     * 
     * @return the number of days in the duration, zero if unsupported
     */
    int getDays();

    /**
     * Gets the hours field part of the duration.
     * 
     * @return the number of hours in the duration, zero if unsupported
     */
    int getHours();

    /**
     * Gets the minutes field part of the duration.
     * 
     * @return the number of minutes in the duration, zero if unsupported
     */
    int getMinutes();

    /**
     * Gets the seconds field part of the duration.
     * 
     * @return the number of seconds in the duration, zero if unsupported
     */
    int getSeconds();

    /**
     * Gets the millis field part of the duration.
     * 
     * @return the number of millis in the duration, zero if unsupported
     */
    int getMillis();

    /**
     * Get this object as an immutable Duration. This can be useful if you
     * don't trust the implementation of the interface to be well-behaved, or
     * to get a guaranteed immutable object.
     * 
     * @return a Duration using the same field set and values
     */
    Duration toDuration();

    /**
     * Get this object as a MutableDuration.
     * 
     * @return a MutableDuration using the same field set and values
     */
    MutableDuration toMutableDuration();

    //-----------------------------------------------------------------------
    /**
     * Compares this duration with the specified duration, which can only be
     * performed if both are precise.
     *
     * @param obj  a precise duration to check against
     * @return negative value if this is less, 0 if equal, or positive value if greater
     * @throws NullPointerException if the object is null
     * @throws ClassCastException if the given object is not supported
     * @throws IllegalStateException if either duration is imprecise
     */
    int compareTo(Object obj);

    /**
     * Is the length of this duration equal to the duration passed in.
     * Both durations must be precise.
     *
     * @param duration  another duration to compare to
     * @return true if this duration is equal to than the duration passed in
     * @throws IllegalArgumentException if the duration is null
     * @throws IllegalStateException if either duration is imprecise
     */
    boolean isEqual(ReadableDuration duration);

    /**
     * Is the length of this duration longer than the duration passed in.
     * Both durations must be precise.
     *
     * @param duration  another duration to compare to
     * @return true if this duration is equal to than the duration passed in
     * @throws IllegalArgumentException if the duration is null
     * @throws IllegalStateException if either duration is imprecise
     */
    boolean isLongerThan(ReadableDuration duration);

    /**
     * Is the length of this duration shorter than the duration passed in.
     * Both durations must be precise.
     *
     * @param duration  another duration to compare to
     * @return true if this duration is equal to than the duration passed in
     * @throws IllegalArgumentException if the duration is null
     * @throws IllegalStateException if either duration is imprecise
     */
    boolean isShorterThan(ReadableDuration duration);

    //-----------------------------------------------------------------------
    /**
     * Compares this object with the specified object for equality based
     * on the value of each field. All ReadableDuration instances are accepted.
     * <p>
     * To compare two durations for absolute duration (ie. millisecond duration
     * ignoring the fields), use {@link #isEqual(ReadableDuration)} or
     * {@link #compareTo(Object)}.
     *
     * @param readableDuration  a readable duration to check against
     * @return true if all the field values are equal, false if
     *  not or the duration is null or of an incorrect type
     */
    boolean equals(Object readableDuration);

    /**
     * Gets a hash code for the duration that is compatable with the 
     * equals method.
     *
     * @return a hash code
     */
    int hashCode();

    //-----------------------------------------------------------------------
    /**
     * Gets the value as a String in the ISO8601 duration format.
     * <p>
     * For example, "P6H3M5S" represents 6 hours, 3 minutes, 5 seconds.
     *
     * @return the value as an ISO8601 string
     */
    String toString();

}
