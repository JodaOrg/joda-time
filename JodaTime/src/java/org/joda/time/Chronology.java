/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-03 Stephen Colebourne.
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

import java.io.Serializable;

/**
 * Chronology provides access to the individual date time fields for
 * a chronological calendar system.
 * <p>
 * Chronology subclasses <em>must</em> be immutable.
 * <p>
 * Various chronologies are supported by subclasses including ISO and 
 * GregorianJulian. 
 * <p>
 * This class does not strongly define each field. Subclasses may interpret
 * the fields differently.
 * 
 * @see org.joda.time.chrono.iso.ISOChronology
 * @see org.joda.time.chrono.gj.GJChronology
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public abstract class Chronology implements Serializable {
    
    /**
     * Restricted constructor
     */
    protected Chronology() {
        super();
    }

    /**
     * Returns the DateTimeZone that this Chronology operates in, or null if
     * unspecified.
     *
     * @return DateTimeZone null if unspecified
     */
    public abstract DateTimeZone getDateTimeZone();

    /**
     * Returns an instance of this Chronology that operates in the UTC time
     * zone. Chronologies that do not operate in a time zone or are already
     * UTC must return themself.
     *
     * @return a version of this chronology that ignores time zones
     */
    public abstract Chronology withUTC();
    
    /**
     * Returns an instance of this Chronology that operates in any time zone.
     *
     * @return a version of this chronology with a specific time zone
     * @throws IllegalArgumentException if zone is null
     * @see org.joda.time.chrono.ZonedChronology
     */
    public abstract Chronology withDateTimeZone(DateTimeZone zone);

    /**
     * Get the millis for a time only field. The default implementation simply
     * returns <code>dayOfYear().remainder(millis)</code>.
     * 
     * @param millis  the millis to convert to time only
     * @return millis with the date part stripped
     */
    public long getTimeOnlyMillis(long millis) {
        return dayOfYear().remainder(millis);
    }

    /**
     * Get the millis for a date only field. The default implementation simply
     * returns <code>dayOfYear().roundFloor(millis)</code>.
     * 
     * @param millis  the millis to convert to date only
     * @return millis with the time part stripped
     */
    public long getDateOnlyMillis(long millis) {
        return dayOfYear().roundFloor(millis);
    }

    // Millis
    //-----------------------------------------------------------------------
    /**
     * Get the millis of second field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField millisOfSecond() {
        throw new UnsupportedOperationException("millisOfSecond is unsupported for " + getClass().getName());
    }

    /**
     * Get the millis of day field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField millisOfDay() {
        throw new UnsupportedOperationException("millisOfDay is unsupported for " + getClass().getName());
    }

    // Second
    //-----------------------------------------------------------------------
    /**
     * Get the second of minute field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField secondOfMinute() {
        throw new UnsupportedOperationException("secondOfMinute is unsupported for " + getClass().getName());
    }

    /**
     * Get the second of day field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField secondOfDay() {
        throw new UnsupportedOperationException("secondOfDay is unsupported for " + getClass().getName());
    }

    // Minute
    //-----------------------------------------------------------------------
    /**
     * Get the minute of hour field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField minuteOfHour() {
        throw new UnsupportedOperationException("minuteOfHour is unsupported for " + getClass().getName());
    }

    /**
     * Get the minute of day field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField minuteOfDay() {
        throw new UnsupportedOperationException("minuteOfDay is unsupported for " + getClass().getName());
    }

    // Hour
    //-----------------------------------------------------------------------
    /**
     * Get the hour of day (0-23) field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField hourOfDay() {
        throw new UnsupportedOperationException("hourOfDay is unsupported for " + getClass().getName());
    }

    /**
     * Get the hour of day (offset to 1-24) field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField clockhourOfDay() {
        throw new UnsupportedOperationException("clockhourOfDay is unsupported for " + getClass().getName());
    }

    /**
     * Get the hour of am/pm (0-11) field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField hourOfHalfday() {
        throw new UnsupportedOperationException("hourOfHalfday is unsupported for " + getClass().getName());
    }

    /**
     * Get the hour of am/pm (offset to 1-12) field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField clockhourOfHalfday() {
        throw new UnsupportedOperationException("clockhourOfHalfday is unsupported for " + getClass().getName());
    }

    /**
     * Get the AM(0) PM(1) field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField halfdayOfDay() {
        throw new UnsupportedOperationException("halfdayOfDay is unsupported for " + getClass().getName());
    }

    // Day
    //-----------------------------------------------------------------------
    /**
     * Get the day of week field for this chronology.
     *
     * <p>DayOfWeek values are defined in {@link DateTimeConstants}.
     * They use the ISO definitions, where 1 is Monday and 7 is Sunday.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField dayOfWeek() {
        throw new UnsupportedOperationException("dayOfWeek is unsupported for " + getClass().getName());
    }

    /**
     * Get the day of month field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField dayOfMonth() {
        throw new UnsupportedOperationException("dayOfMonth is unsupported for " + getClass().getName());
    }

    /**
     * Get the day of year field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField dayOfYear() {
        throw new UnsupportedOperationException("dayOfYear is unsupported for " + getClass().getName());
    }

    // Week
    //-----------------------------------------------------------------------
    /**
     * Get the week of a week based year field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField weekOfWeekyear() {
        throw new UnsupportedOperationException("weekOfWeekyear is unsupported for " + getClass().getName());
    }

    /**
     * Get the year of a week based year field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField weekyear() {
        throw new UnsupportedOperationException("weekyear is unsupported for " + getClass().getName());
    }

    // Month
    //-----------------------------------------------------------------------
    /**
     * Get the month of year field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField monthOfYear() {
        throw new UnsupportedOperationException("monthOfYear is unsupported for " + getClass().getName());
    }

    // Year
    //-----------------------------------------------------------------------
    /**
     * Get the year field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField year() {
        throw new UnsupportedOperationException("year is unsupported for " + getClass().getName());
    }

    /**
     * Get the year of era field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField yearOfEra() {
        throw new UnsupportedOperationException("yearOfEra is unsupported for " + getClass().getName());
    }

    /**
     * Get the year of century field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField yearOfCentury() {
        throw new UnsupportedOperationException("yearOfCentury is unsupported for " + getClass().getName());
    }

    /**
     * Get the century of era field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField centuryOfEra() {
        throw new UnsupportedOperationException("centuryOfEra is unsupported for " + getClass().getName());
    }

    /**
     * Get the era field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException if unsupported
     */
    public DateTimeField era() {
        throw new UnsupportedOperationException("era is unsupported for " + getClass().getName());
    }

}
