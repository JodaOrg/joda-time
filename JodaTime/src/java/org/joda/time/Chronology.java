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
 * Chronology provides access to the individual date time fields for a
 * chronological calendar system. Various chronologies are supported by
 * subclasses including ISO and GregorianJulian.
 * <p>
 * This interface defines a number of fields with names from the ISO8601
 * standard. Chronology does not 'strongly' define these fields however, thus
 * implementations are free to interpret the field names as they wish. For
 * example, a week could be defined as 10 days and a month as 40 days in a
 * special WeirdChronology implementation. Clearly the GJ and ISO
 * implementations provided use the field names as you would expect.
 * 
 * @see org.joda.time.chrono.ISOChronology
 * @see org.joda.time.chrono.GJChronology
 * @see org.joda.time.chrono.GregorianChronology
 * @see org.joda.time.chrono.JulianChronology
 * @see org.joda.time.chrono.CopticChronology
 * @see org.joda.time.chrono.BuddhistChronology
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public interface Chronology {
    
    /**
     * Returns the DateTimeZone that this Chronology operates in, or null if
     * unspecified.
     *
     * @return DateTimeZone null if unspecified
     */
    DateTimeZone getZone();

    /**
     * Returns an instance of this Chronology that operates in the UTC time
     * zone. Chronologies that do not operate in a time zone or are already
     * UTC must return themself.
     *
     * @return a version of this chronology that ignores time zones
     */
    Chronology withUTC();
    
    /**
     * Returns an instance of this Chronology that operates in any time zone.
     *
     * @return a version of this chronology with a specific time zone
     * @param zone to use, or default if null
     * @see org.joda.time.chrono.ZonedChronology
     */
    Chronology withZone(DateTimeZone zone);

    /**
     * Returns a date-only millisecond instant, by clearing the time fields
     * from the given instant.
     * <p>
     * The default implementation simply returns
     * <code>dayOfYear().roundFloor(instant)</code>.
     * 
     * @param instant the milliseconds from 1970-01-01T00:00:00Z
     * @return millisecond instant from 1970-01-01T00:00:00Z with the time part
     * cleared
     */
    long getDateOnlyMillis(long instant);

    /**
     * Returns a date-only millisecond instant, formed from the given year,
     * month, and day values. The set of given values must refer to a valid
     * date, or else an IllegalArgumentException is thrown.
     * <p>
     * The default implementation simply returns
     * <code>getDateTimeMillis(year, monthOfYear, dayOfMonth, 0)</code>.
     *
     * @param year year to use
     * @param monthOfYear month to use
     * @param dayOfMonth day of month to use
     * @return millisecond instant from 1970-01-01T00:00:00Z without any time
     * part
     */
    long getDateOnlyMillis(int year, int monthOfYear, int dayOfMonth)
        throws IllegalArgumentException;

    /**
     * Returns a time-only millisecond instant, by clearing the date fields
     * from the given instant.
     * <p>
     * The default implementation simply returns
     * <code>dayOfYear().remainder(instant)</code>.
     * 
     * @param instant the milliseconds from 1970-01-01T00:00:00Z
     * @return millisecond instant from 1970-01-01T00:00:00Z with the date part
     * cleared
     */
    long getTimeOnlyMillis(long instant);

    /**
     * Returns a time-only millisecond instant, formed from the given hour,
     * minute, second, and millisecond values. The set of given values must
     * refer to a valid time, or else an IllegalArgumentException is thrown.
     * <p>
     * The default implementation calls upon separate DateTimeFields to
     * determine the result. Subclasses are encouraged to provide a more
     * efficient implementation.
     *
     * @param hourOfDay hour to use
     * @param minuteOfHour minute to use
     * @param secondOfMinute second to use
     * @param millisOfSecond millisecond to use
     * @return millisecond instant from 1970-01-01T00:00:00Z without any date
     * part
     */
    long getTimeOnlyMillis(int hourOfDay, int minuteOfHour,
                           int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException;

    /**
     * Returns a datetime millisecond instant, formed from the given year,
     * month, day, and millisecond values. The set of given values must refer
     * to a valid datetime, or else an IllegalArgumentException is thrown.
     * <p>
     * The default implementation calls upon separate DateTimeFields to
     * determine the result. Subclasses are encouraged to provide a more
     * efficient implementation.
     *
     * @param year year to use
     * @param monthOfYear month to use
     * @param dayOfMonth day of month to use
     * @param millisOfDay millisecond to use
     * @return millisecond instant from 1970-01-01T00:00:00Z
     */
    long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth, int millisOfDay)
        throws IllegalArgumentException;

    /**
     * Returns a datetime millisecond instant, from from the given instant,
     * hour, minute, second, and millisecond values. The set of given values
     * must refer to a valid datetime, or else an IllegalArgumentException is
     * thrown.
     * <p>
     * The default implementation calls upon separate DateTimeFields to
     * determine the result. Subclasses are encouraged to provide a more
     * efficient implementation.
     *
     * @param instant instant to start from
     * @param hourOfDay hour to use
     * @param minuteOfHour minute to use
     * @param secondOfMinute second to use
     * @param millisOfSecond millisecond to use
     * @return millisecond instant from 1970-01-01T00:00:00Z
     */
    long getDateTimeMillis(long instant,
                           int hourOfDay, int minuteOfHour,
                           int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException;

    /**
     * Returns a datetime millisecond instant, formed from the given year,
     * month, day, hour, minute, second, and millisecond values. The set of
     * given values must refer to a valid datetime, or else an
     * IllegalArgumentException is thrown.
     * <p>
     * The default implementation calls upon separate DateTimeFields to
     * determine the result. Subclasses are encouraged to provide a more
     * efficient implementation.
     *
     * @param year year to use
     * @param monthOfYear month to use
     * @param dayOfMonth day of month to use
     * @param hourOfDay hour to use
     * @param minuteOfHour minute to use
     * @param secondOfMinute second to use
     * @param millisOfSecond millisecond to use
     * @return millisecond instant from 1970-01-01T00:00:00Z
     */
    long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                           int hourOfDay, int minuteOfHour,
                           int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException;

    /**
     * Validates whether the fields stored in a partial instant are valid.
     *
     * @param instant  the partial instant to validate
     * @throws IllegalArgumentException if the instant is invalid
     */
    void validate(ReadablePartial instant);

    // Millis
    //-----------------------------------------------------------------------
    /**
     * Get the millis duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    DurationField millis();

    /**
     * Get the millis of second field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField millisOfSecond();

    /**
     * Get the millis of day field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField millisOfDay();

    // Second
    //-----------------------------------------------------------------------
    /**
     * Get the seconds duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    DurationField seconds();

    /**
     * Get the second of minute field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField secondOfMinute();

    /**
     * Get the second of day field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField secondOfDay();

    // Minute
    //-----------------------------------------------------------------------
    /**
     * Get the minutes duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    DurationField minutes();

    /**
     * Get the minute of hour field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField minuteOfHour();

    /**
     * Get the minute of day field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField minuteOfDay();

    // Hour
    //-----------------------------------------------------------------------
    /**
     * Get the hours duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    DurationField hours();

    /**
     * Get the hour of day (0-23) field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField hourOfDay();

    /**
     * Get the hour of day (offset to 1-24) field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField clockhourOfDay();

    /**
     * Get the hour of am/pm (0-11) field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField hourOfHalfday();

    /**
     * Get the hour of am/pm (offset to 1-12) field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField clockhourOfHalfday();

    /**
     * Get the AM(0) PM(1) field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField halfdayOfDay();

    // Day
    //-----------------------------------------------------------------------
    /**
     * Get the days duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    DurationField days();

    /**
     * Get the day of week field for this chronology.
     *
     * <p>DayOfWeek values are defined in {@link DateTimeConstants}.
     * They use the ISO definitions, where 1 is Monday and 7 is Sunday.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField dayOfWeek();

    /**
     * Get the day of month field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField dayOfMonth();

    /**
     * Get the day of year field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField dayOfYear();

    // Week
    //-----------------------------------------------------------------------
    /**
     * Get the weeks duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    DurationField weeks();

    /**
     * Get the week of a week based year field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField weekOfWeekyear();

    /**
     * Get the weekyears duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    DurationField weekyears();

    /**
     * Get the year of a week based year field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField weekyear();

    // Month
    //-----------------------------------------------------------------------
    /**
     * Get the months duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    DurationField months();

    /**
     * Get the month of year field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField monthOfYear();

    // Year
    //-----------------------------------------------------------------------
    /**
     * Get the years duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    DurationField years();

    /**
     * Get the year field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField year();

    /**
     * Get the year of era field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField yearOfEra();

    /**
     * Get the year of century field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField yearOfCentury();

    /**
     * Get the centuries duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    DurationField centuries();

    /**
     * Get the century of era field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField centuryOfEra();

    /**
     * Get the eras duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    DurationField eras();

    /**
     * Get the era field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    DateTimeField era();

    /**
     * Gets a debugging toString.
     * 
     * @return a debugging string
     */
    String toString();

}
