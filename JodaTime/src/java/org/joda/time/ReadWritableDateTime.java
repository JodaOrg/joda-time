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

// Import for @link support
import org.joda.time.convert.ConverterManager;

/**
 * Defines an instant in time that can be queried and modified using datetime fields.
 * <p>
 * The implementation of this interface will be mutable.
 * It may provide more advanced methods than those in the interface.
 * <p>
 * Methods in your application should be defined using <code>ReadWritableDateTime</code>
 * as a parameter if the method wants to manipulate and change a date in simple ways.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 */
public interface ReadWritableDateTime extends ReadableDateTime, ReadWritableInstant {
    
    //-----------------------------------------------------------------------
    /**
     * Set the year to the specified value.
     *
     * @param year  the year
     * @throws IllegalArgumentException if the value is invalid
     */
    void setYear(int year);

    /**
     * Add a number of years to the date.
     *
     * @param years  the years to add
     * @throws IllegalArgumentException if the value is invalid
     */
    void addYears(int years);

    //-----------------------------------------------------------------------
    /**
     * Set the weekyear to the specified value.
     *
     * @param weekyear  the weekyear
     * @throws IllegalArgumentException if the value is invalid
     */
    void setWeekyear(int weekyear);

    /**
     * Add a number of weekyears to the date.
     *
     * @param weekyears  the weekyears to add
     * @throws IllegalArgumentException if the value is invalid
     */
    void addWeekyears(int weekyears);

    //-----------------------------------------------------------------------
    /**
     * Set the month of the year to the specified value.
     *
     * @param monthOfYear  the month of the year
     * @throws IllegalArgumentException if the value is invalid
     */
    void setMonthOfYear(int monthOfYear);

    /**
     * Add a number of months to the date.
     *
     * @param months  the months to add
     * @throws IllegalArgumentException if the value is invalid
     */
    void addMonths(int months);

    //-----------------------------------------------------------------------
    /**
     * Set the week of weekyear to the specified value.
     *
     * @param weekOfWeekyear the week of the weekyear
     * @throws IllegalArgumentException if the value is invalid
     */
    void setWeekOfWeekyear(int weekOfWeekyear);

    /**
     * Add a number of weeks to the date.
     *
     * @param weeks  the weeks to add
     * @throws IllegalArgumentException if the value is invalid
     */
    void addWeeks(int weeks);

    //-----------------------------------------------------------------------
    /**
     * Set the day of year to the specified value.
     *
     * @param dayOfYear the day of the year
     * @throws IllegalArgumentException if the value is invalid
     */
    void setDayOfYear(int dayOfYear);

    /**
     * Set the day of the month to the specified value.
     *
     * @param dayOfMonth  the day of the month
     * @throws IllegalArgumentException if the value is invalid
     */
    void setDayOfMonth(int dayOfMonth);

    /**
     * Set the day of week to the specified value.
     *
     * @param dayOfWeek  the day of the week
     * @throws IllegalArgumentException if the value is invalid
     */
    void setDayOfWeek(int dayOfWeek);

    /**
     * Add a number of days to the date.
     *
     * @param days  the days to add
     * @throws IllegalArgumentException if the value is invalid
     */
    void addDays(int days);

    //-----------------------------------------------------------------------
    /**
     * Set the hour of the day to the specified value.
     *
     * @param hourOfDay  the hour of day
     * @throws IllegalArgumentException if the value is invalid
     */
    void setHourOfDay(int hourOfDay);

    /**
     * Add a number of hours to the date.
     *
     * @param hours  the hours to add
     * @throws IllegalArgumentException if the value is invalid
     */
    void addHours(int hours);
    
    //-----------------------------------------------------------------------
    /**
     * Set the minute of the day to the specified value.
     *
     * @param minuteOfDay  the minute of day
     * @throws IllegalArgumentException if the value is invalid
     */
    void setMinuteOfDay(int minuteOfDay);

    /**
     * Set the minute of the hour to the specified value.
     *
     * @param minuteOfHour  the minute of hour
     * @throws IllegalArgumentException if the value is invalid
     */
    void setMinuteOfHour(int minuteOfHour);

    /**
     * Add a number of minutes to the date.
     *
     * @param minutes  the minutes to add
     * @throws IllegalArgumentException if the value is invalid
     */
    void addMinutes(int minutes);

    //-----------------------------------------------------------------------
    /**
     * Set the second of the day to the specified value.
     *
     * @param secondOfDay  the second of day
     * @throws IllegalArgumentException if the value is invalid
     */
    void setSecondOfDay(int secondOfDay);

    /**
     * Set the second of the minute to the specified value.
     *
     * @param secondOfMinute  the second of minute
     * @throws IllegalArgumentException if the value is invalid
     */
    void setSecondOfMinute(int secondOfMinute);

    /**
     * Add a number of seconds to the date.
     *
     * @param seconds  the seconds to add
     * @throws IllegalArgumentException if the value is invalid
     */
    void addSeconds(int seconds);

    //-----------------------------------------------------------------------
    /**
     * Set the millis of the day to the specified value.
     *
     * @param millisOfDay  the millis of day
     * @throws IllegalArgumentException if the value is invalid
     */
    void setMillisOfDay(int millisOfDay);

    /**
     * Set the millis of the second to the specified value.
     *
     * @param millisOfSecond  the millis of second
     * @throws IllegalArgumentException if the value is invalid
     */
    void setMillisOfSecond(int millisOfSecond);

    /**
     * Add a number of milliseconds to the date. The implementation of this
     * method differs from the {@link #add(long)} method in that a
     * DateTimeField performs the addition.
     *
     * @param millis  the milliseconds to add
     * @throws IllegalArgumentException if the value is invalid
     */
    void addMillis(int millis);

    //-----------------------------------------------------------------------
    /**
     * Set the date from milliseconds.
     * The time part of this object will be unaffected.
     *
     * @param instant  milliseconds from 1970-01-01T00:00:00Z, time part ignored
     * @throws IllegalArgumentException if the value is invalid
     */
    void setDate(long instant);

    /**
     * Set the date from an object representing an instant.
     * The time part of this object will be unaffected.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  an object representing an instant, time part ignored
     * @throws IllegalArgumentException if the object is null or invalid
     */
    void setDate(Object instant);

    /**
     * Set the date from fields.
     * The time part of this object will be unaffected.
     *
     * @param year  the year
     * @param monthOfYear  the month of the year
     * @param dayOfMonth  the day of the month
     * @throws IllegalArgumentException if the value is invalid
     */
    void setDate(int year, int monthOfYear, int dayOfMonth);

    //-----------------------------------------------------------------------
    /**
     * Set the time from milliseconds.
     * The date part of this object will be unaffected.
     *
     * @param millis milliseconds from T00:00:00Z, date part ignored
     * @throws IllegalArgumentException if the value is invalid
     */
    void setTime(long millis);

    /**
     * Set the date from an object representing an instant.
     * The date part of this object will be unaffected.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  an object representing an instant, date part ignored
     * @throws IllegalArgumentException if the object is null or invalid
     */
    void setTime(Object instant);

    /**
     * Set the time from fields.
     * The date part of this object will be unaffected.
     *
     * @param hour  the hour
     * @param minuteOfHour  the minute of the hour
     * @param secondOfMinute  the second of the minute
     * @param millisOfSecond  the millisecond of the second
     * @throws IllegalArgumentException if the value is invalid
     */
    void setTime(int hour, int minuteOfHour, int secondOfMinute, int millisOfSecond);

    //-----------------------------------------------------------------------
    /**
     * Set the date and time from milliseconds.
     * This method is a synonym for {@link #setMillis(long)}.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @throws IllegalArgumentException if the value is invalid
     */
    void setDateTime(long instant);

    /**
     * Set the date and time from an object representing an instant.
     * This method is a synonym for {@link #setMillis(Object)}.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  an object representing an instant
     * @throws IllegalArgumentException if the object is null or invalid
     */
    void setDateTime(Object instant);

    /**
     * Set the date and time from fields.
     *
     * @param year  the year
     * @param monthOfYear  the month of the year
     * @param dayOfMonth  the day of the month
     * @param hourOfDay  the hour of the day
     * @param minuteOfHour  the minute of the hour
     * @param secondOfMinute  the second of the minute
     * @param millisOfSecond  the millisecond of the second
     * @throws IllegalArgumentException if the value is invalid
     */
    void setDateTime(
        int year, int monthOfYear, int dayOfMonth,
        int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond);

}
