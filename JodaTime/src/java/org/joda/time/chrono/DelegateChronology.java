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
package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;

/**
 * <code>DelegateChronology</code> delegates each method call to the
 * chronology it wraps.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public abstract class DelegateChronology extends Chronology {
    
    /** The Chonology being wrapped */
    private final Chronology iChronology;
    
    /**
     * Create a DelegateChronology for any chronology.
     *
     * @param chrono the chronology
     * @throws IllegalArgumentException if chronology is null
     */
    protected DelegateChronology(Chronology chrono) {
        if (chrono == null) {
            throw new IllegalArgumentException("The Chronology must not be null");
        }
        iChronology = chrono;
    }
    
    /**
     * Gets the wrapped chronology.
     * 
     * @return the wrapped Chronology
     */
    protected Chronology getChronology() {
        return iChronology;
    }

    /**
     * Get the Chronology in the UTC time zone.
     * 
     * @return Chronology in UTC
     */
    public abstract Chronology withUTC();

    /**
     * Get the Chronology in the any time zone.
     * 
     * @return Chronology in ant time zone
     */
    public abstract Chronology withDateTimeZone(DateTimeZone zone);

    /**
     * Gets the time zone that this chronolog is using.
     * 
     * @return the DateTimeZone
     */
    public DateTimeZone getDateTimeZone() {
        return iChronology.getDateTimeZone();
    }

    // Millis
    //------------------------------------------------------------

    /**
     * Get the millis of second field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField millisOfSecond() {
        return iChronology.millisOfSecond();
    }

    /**
     * Get the millis of day field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField millisOfDay() {
        return iChronology.millisOfDay();
    }

    // Seconds
    //------------------------------------------------------------

    /**
     * Get the second of minute field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField secondOfMinute() {
        return iChronology.secondOfMinute();
    }

    /**
     * Get the second of day field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField secondOfDay() {
        return iChronology.secondOfDay();
    }

    // Minutes
    //------------------------------------------------------------

    /**
     * Get the minute of hour field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField minuteOfHour() {
        return iChronology.minuteOfHour();
    }

    /**
     * Get the minute of day field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField minuteOfDay() {
        return iChronology.minuteOfDay();
    }

    // Hours
    //------------------------------------------------------------

    /**
     * Get the hour of day (0-23) field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField hourOfDay() {
        return iChronology.hourOfDay();
    }

    /**
     * Get the hour of day (offset to 1-24) field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField clockhourOfDay() {
        return iChronology.clockhourOfDay();
    }

    /**
     * Get the hour of am/pm (0-11) field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField hourOfHalfday() {
        return iChronology.hourOfHalfday();
    }

    /**
     * Get the hour of am/pm (offset to 1-12) field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField clockhourOfHalfday() {
        return iChronology.clockhourOfHalfday();
    }

    /**
     * Get the AM(0) PM(1) field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField halfdayOfDay() {
        return iChronology.halfdayOfDay();
    }

    // Day
    //------------------------------------------------------------

    /**
     * Get the day of week field for this chronology.
     *
     * @return DateTimeField
     */
    public DateTimeField dayOfWeek() {
        return iChronology.dayOfWeek();
    }

    /**
     * Get the day of month field for this chronology.
     *
     * @return DateTimeField
     */
    public DateTimeField dayOfMonth() {
        return iChronology.dayOfMonth();
    }

    /**
     * Get the day of year field for this chronology.
     *
     * @return DateTimeField
     */
    public DateTimeField dayOfYear() {
        return iChronology.dayOfYear();
    }

    // Week
    //------------------------------------------------------------

    /**
     * Get the week of a week based year field for this chronology.
     *
     * @return DateTimeField
     */
    public DateTimeField weekOfWeekyear() {
        return iChronology.weekOfWeekyear();
    }

    /**
     * Get the year of a week based year field for this chronology.
     *
     * @return DateTimeField
     */
    public DateTimeField weekyear() {
        return iChronology.weekyear();
    }

    // Month
    //------------------------------------------------------------

    /**
     * Get the month of year field for this chronology.
     *
     * @return DateTimeField
     */
    public DateTimeField monthOfYear() {
        return iChronology.monthOfYear();
    }

    // Year
    //------------------------------------------------------------

    /**
     * Get the year field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField year() {
        return iChronology.year();
    }

    /**
     * Get the year of era field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField yearOfEra() {
        return iChronology.yearOfEra();
    }

    /**
     * Get the year field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField yearOfCentury() {
        return iChronology.yearOfCentury();
    }

    /**
     * Get the century of era field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField centuryOfEra() {
        return iChronology.centuryOfEra();
    }

    /**
     * Get the era field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField era() {
        return iChronology.era();
    }
    
}
