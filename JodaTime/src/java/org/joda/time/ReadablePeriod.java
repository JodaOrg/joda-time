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
 * Defines a time period specified in terms of individual duration fields
 * such as years and days.
 * <p>
 * The implementation of this interface may be mutable or immutable. This
 * interface only gives access to retrieve data, never to change it.
 * <p>
 * Periods are split up into multiple fields, for example days and seconds.
 * Implementations are not required to evenly distribute the values across the fields.
 * The value for each field may be positive or negative.
 * The {@link PeriodType} defines the rules for dividing the fields and which fields
 * are supported. Unsupported fields always have a value of zero.
 * <p>
 * When a time period is added to an instant, the effect is to add each field in turn.
 * For example, a time period could be defined as 3 months, 2 days and -1 hours.
 * In most circumstances this would be the same as 3 months, 1 day, and 23 hours.
 * However, when adding across a daylight savings boundary, a day may be 23 or 25 hours long.
 * Thus, the time period is always added field by field to the datetime.
 *
 * @see ReadableDuration
 * @see ReadableInterval
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public interface ReadablePeriod {

    /**
     * Returns the period type which defines which fields this period supports.
     * 
     * @return the period type
     */
    PeriodType getPeriodType();

    /**
     * Is this period a precise length of time, or descriptive.
     * <p>
     * A precise period could include millis, seconds, minutes or hours.
     * However, days, weeks, months and years can vary in length, resulting in
     * an imprecise period.
     * <p>
     * An imprecise period can be made precise by pairing it with a
     * date in a {@link ReadableInterval}.
     *
     * @return true if the period is precise
     */
    boolean isPrecise();

    //-----------------------------------------------------------------------
    /**
     * Adds this period to the given instant, returning a new value.
     * <p>
     * The addition uses the chronology of the PeriodType.
     * To add just once, pass in a scalar of one. To subtract once, pass
     * in a scalar of minus one.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to add the
     * period to
     * @param scalar  the number of times to add the period, negative to subtract
     * @return milliseconds value plus this period times scalar
     * @throws ArithmeticException if the result of the calculation is too large
     */
    long addTo(long instant, int scalar);

    /**
     * Adds this period to the given instant, returning a new value.
     * <p>
     * The addition uses the chronology specified.
     * To add just once, pass in a scalar of one. To subtract once, pass
     * in a scalar of minus one.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to add the
     * period to
     * @param scalar  the number of times to add the period, negative to subtract
     * @param chrono  override the chronology of the period type, unless null is passed in
     * @return milliseconds value plus this period times scalar
     * @throws ArithmeticException if the result of the calculation is too large
     */
    long addTo(long instant, int scalar, Chronology chrono);

    /**
     * Adds this period to the given instant, returning a new Instant.
     * <p>
     * To add just once, pass in a scalar of one. To subtract once, pass
     * in a scalar of minus one.
     *
     * @param instant  the instant to add the period to
     * @param scalar  the number of times to add the period, negative to subtract
     * @return instant with the original value plus this period times scalar
     * @throws IllegalArgumentException if the instant is null
     * @throws ArithmeticException if the result of the calculation is too large
     */
    Instant addTo(ReadableInstant instant, int scalar);

    /**
     * Adds this period into the given mutable instant.
     * <p>
     * To add just once, pass in a scalar of one. To subtract once, pass
     * in a scalar of minus one.
     *
     * @param instant  the instant to update with the added period
     * @param scalar  the number of times to add the period, negative to subtract
     * @throws IllegalArgumentException if the instant is null
     * @throws ArithmeticException if the result of the calculation is too large
     */
    void addInto(ReadWritableInstant instant, int scalar);

    //-----------------------------------------------------------------------
    /**
     * Gets the years field part of the period.
     * 
     * @return the number of years in the period, zero if unsupported
     */
    int getYears();

    /**
     * Gets the months field part of the period.
     * 
     * @return the number of months in the period, zero if unsupported
     */
    int getMonths();

    /**
     * Gets the weeks field part of the period.
     * 
     * @return the number of weeks in the period, zero if unsupported
     */
    int getWeeks();

    /**
     * Gets the days field part of the period.
     * 
     * @return the number of days in the period, zero if unsupported
     */
    int getDays();

    /**
     * Gets the hours field part of the period.
     * 
     * @return the number of hours in the period, zero if unsupported
     */
    int getHours();

    /**
     * Gets the minutes field part of the period.
     * 
     * @return the number of minutes in the period, zero if unsupported
     */
    int getMinutes();

    /**
     * Gets the seconds field part of the period.
     * 
     * @return the number of seconds in the period, zero if unsupported
     */
    int getSeconds();

    /**
     * Gets the millis field part of the period.
     * 
     * @return the number of millis in the period, zero if unsupported
     */
    int getMillis();

    //-----------------------------------------------------------------------
    /**
     * Gets this object as an immutable Period. This can be useful if you
     * don't trust the implementation of the interface to be well-behaved, or
     * to get a guaranteed immutable object.
     * 
     * @return a Duration using the same field set and values
     */
    Period toPeriod();

    /**
     * Get this object as a MutablePeriod.
     * 
     * @return a MutablePeriod using the same field set and values
     */
    MutablePeriod toMutablePeriod();

    /**
     * Gets the total length of this time period in milliseconds, 
     * failing if the period is imprecise.
     *
     * @return the total length of the time period in milliseconds.
     * @throws IllegalStateException if this time period is imprecise
     */
    long toDurationMillis();

    /**
     * Gets the total length of this time period,
     * failing if the period is imprecise.
     *
     * @return the total length of the time period in milliseconds.
     * @throws IllegalStateException if this time period is imprecise
     */
    Duration toDuration();

    //-----------------------------------------------------------------------
    /**
     * Compares this object with the specified object for equality based
     * on the value of each field. All ReadablePeriod instances are accepted.
     * <p>
     * To compare two periods for absolute duration (ie. millisecond duration
     * ignoring the fields), use {@link #toDurationMillis()} or {@link #toDuration()}.
     *
     * @param readablePeriod  a readable period to check against
     * @return true if all the field values are equal, false if
     *  not or the period is null or of an incorrect type
     */
    boolean equals(Object readablePeriod);

    /**
     * Gets a hash code for the period that is compatible with the 
     * equals method. The hashcode is the period type hashcode plus
     * each period value from largest to smallest calculated as follows:
     * <pre>
     *   int hash = getPeriodType().hashCode();
     *   hash = 53 * hash + getYears();
     *   hash = 53 * hash + getMonths();
     *   hash = 53 * hash + getWeeks();
     *   hash = 53 * hash + getDays();
     *   hash = 53 * hash + getHours();
     *   hash = 53 * hash + getMinutes();
     *   hash = 53 * hash + getSeconds();
     *   hash = 53 * hash + getMillis();
     *   return hash;
     * </pre>
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
