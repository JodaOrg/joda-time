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

import java.util.Locale;

/**
 * Defines an instant in time that can be queried using datetime fields.
 * <p>
 * The implementation of this interface may be mutable or immutable.
 * This interface only gives access to retrieve data, never to change it.
 * <p>
 * Methods in your application should be defined using <code>ReadableDateTime</code>
 * as a parameter if the method only wants to read the datetime, and not perform
 * any advanced manipulations.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public interface ReadableDateTime extends ReadableInstant {

    /**
     * Get the day of week field value.
     * <p>
     * The values for the day of week are defined in {@link DateTimeConstants}.
     * 
     * @return the day of week
     */
    int getDayOfWeek();

    /**
     * Get the day of month field value.
     * 
     * @return the day of month
     */
    int getDayOfMonth();

    /**
     * Get the day of year field value.
     * 
     * @return the day of year
     */
    int getDayOfYear();

    /**
     * Get the week of weekyear field value.
     * 
     * @return the week of a week based year
     */
    int getWeekOfWeekyear();

    /**
     * Get the weekyear field value.
     * 
     * @return the year of a week based year
     */
    int getWeekyear();

    /**
     * Get the month of year field value.
     * 
     * @return the month of year
     */
    int getMonthOfYear();

    /**
     * Get the year field value.
     * 
     * @return the year
     */
    int getYear();

    /**
     * Get the year of era field value.
     * 
     * @return the year of era
     */
    int getYearOfEra();

    /**
     * Get the year of century field value.
     * 
     * @return the year of century
     */
    int getYearOfCentury();

    /**
     * Get the year of era field value.
     * 
     * @return the year of era
     */
    int getCenturyOfEra();

    /**
     * Get the era field value.
     * 
     * @return the era
     */
    int getEra();

    // Time field access methods
    //-----------------------------------------------------------

    /**
     * Get the millis of second field value.
     *
     * @return the millis of second
     */
    int getMillisOfSecond();

    /**
     * Get the millis of day field value.
     *
     * @return the millis of day
     */
    int getMillisOfDay();

    /**
     * Get the second of minute field value.
     *
     * @return the second of minute
     */
    int getSecondOfMinute();

    /**
     * Get the second of day field value.
     *
     * @return the second of day
     */
    int getSecondOfDay();

    /**
     * Get the minute of hour field value.
     *
     * @return the minute of hour
     */
    int getMinuteOfHour();

    /**
     * Get the minute of day field value.
     *
     * @return the minute of day
     */
    int getMinuteOfDay();

    /**
     * Get the hour of day field value.
     *
     * @return the hour of day
     */
    int getHourOfDay();

    /**
     * Get this object as a DateTime.
     * <p>
     * If the implementation of the interface is a DateTime, it is returned directly.
     * 
     * @return a DateTime using the same millis
     */
    DateTime toDateTime();

    /**
     * Get this object as a MutableDateTime, always returning a new instance.
     * 
     * @return a MutableDateTime using the same millis
     */
    MutableDateTime toMutableDateTime();

    /**
     * Output the instant using the specified format pattern.
     *
     * @param pattern  pattern specification
     * @throws IllegalArgumentException  if pattern is invalid
     * @see  org.joda.time.format.DateTimeFormat
     */
    String toString(String pattern) throws IllegalArgumentException;

    /**
     * Output the instant using the specified format pattern.
     *
     * @param pattern  pattern specification
     * @param locale  Locale to use, or null for default
     * @throws IllegalArgumentException  if pattern is invalid
     * @see  org.joda.time.format.DateTimeFormat
     */
    String toString(String pattern, Locale locale) throws IllegalArgumentException;
    
}
