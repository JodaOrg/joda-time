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
 * Defines a duration of time that can be queried and modified using datetime fields.
 * <p>
 * The implementation of this interface will be mutable.
 * It may provide more advanced methods than those in the interface.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public interface ReadWritableDuration extends ReadableDuration {

    /**
     * Sets all the fields in one go from another ReadableDuration.
     * 
     * @param duration  the duration to set
     * @throws IllegalArgumentException if duration is null
     * @throws UnsupportedOperationException if an unsupported field's value is
     * non-zero
     */
    void setDuration(ReadableDuration duration);

    /**
     * Sets all the fields in one go.
     * 
     * @param years amount of years in this duration, which must be zero if
     * unsupported.
     * @param months amount of months in this duration, which must be zero if
     * unsupported.
     * @param weeks amount of weeks in this duration, which must be zero if
     * unsupported.
     * @param days amount of days in this duration, which must be zero if
     * unsupported.
     * @param hours amount of hours in this duration, which must be zero if
     * unsupported.
     * @param minutes amount of minutes in this duration, which must be zero if
     * unsupported.
     * @param seconds amount of seconds in this duration, which must be zero if
     * unsupported.
     * @param millis amount of milliseconds in this duration, which must be
     * zero if unsupported.
     * @throws UnsupportedOperationException if an unsupported field's value is
     * non-zero
     */
    void setDuration(int years, int months, int weeks, int days,
                     int hours, int minutes, int seconds, int millis);

    /**
     * Sets all the fields in one go from a millisecond interval.
     * 
     * @param startInstant interval start, in milliseconds
     * @param endInstant interval end, in milliseconds
     */
    void setTotalMillis(long startInstant, long endInstant);

    /**
     * Sets all the fields in one go from a millisecond duration.
     * 
     * @param duration  the duration, in milliseconds
     * @throws UnsupportedOperationException if any fields are imprecise
     */
    void setTotalMillis(long duration);
    
    //-----------------------------------------------------------------------
    /**
     * Adds a millisecond duration to this one.
     * 
     * @param duration  the duration to add, in milliseconds
     * @throws IllegalStateException if the duration is imprecise
     */
    void add(long duration);
    
    /**
     * Adds a duration to this one.
     * 
     * @param duration  the duration to add
     * @throws IllegalArgumentException if the duration is null
     * @throws IllegalStateException if the duration is imprecise
     */
    void add(ReadableDuration duration);
    
    /**
     * Normalizes all the field values in this duration.
     *
     * @throws IllegalStateException if this duration is imprecise
     */
    void normalize();

    //-----------------------------------------------------------------------
    /**
     * Sets the number of years of the duration.
     * 
     * @param years  the number of years
     * @throws UnsupportedOperationException if field is not supported.
     */
    void setYears(int years);

    /**
     * Adds the specified years to the number of years in the duration.
     * 
     * @param years  the number of years
     * @throws UnsupportedOperationException if field is not supported.
     */
    void addYears(int years);

    //-----------------------------------------------------------------------
    /**
     * Sets the number of months of the duration.
     * 
     * @param months  the number of months
     * @throws UnsupportedOperationException if field is not supported.
     */
    void setMonths(int months);

    /**
     * Adds the specified months to the number of months in the duration.
     * 
     * @param months  the number of months
     * @throws UnsupportedOperationException if field is not supported.
     */
    void addMonths(int months);

    //-----------------------------------------------------------------------
    /**
     * Sets the number of weeks of the duration.
     * 
     * @param weeks  the number of weeks
     * @throws UnsupportedOperationException if field is not supported.
     */
    void setWeeks(int weeks);

    /**
     * Adds the specified weeks to the number of weeks in the duration.
     * 
     * @param weeks  the number of weeks
     * @throws UnsupportedOperationException if field is not supported.
     */
    void addWeeks(int weeks);

    //-----------------------------------------------------------------------
    /**
     * Sets the number of days of the duration.
     * 
     * @param days  the number of days
     * @throws UnsupportedOperationException if field is not supported.
     */
    void setDays(int days);

    /**
     * Adds the specified days to the number of days in the duration.
     * 
     * @param days  the number of days
     * @throws UnsupportedOperationException if field is not supported.
     */
    void addDays(int days);

    //-----------------------------------------------------------------------
    /**
     * Sets the number of hours of the duration.
     * 
     * @param hours  the number of hours
     * @throws UnsupportedOperationException if field is not supported.
     */
    void setHours(int hours);

    /**
     * Adds the specified hours to the number of hours in the duration.
     * 
     * @param hours  the number of hours
     * @throws UnsupportedOperationException if field is not supported.
     */
    void addHours(int hours);

    //-----------------------------------------------------------------------
    /**
     * Sets the number of minutes of the duration.
     * 
     * @param minutes  the number of minutes
     * @throws UnsupportedOperationException if field is not supported.
     */
    void setMinutes(int minutes);

    /**
     * Adds the specified minutes to the number of minutes in the duration.
     * 
     * @param minutes  the number of minutes
     * @throws UnsupportedOperationException if field is not supported.
     */
    void addMinutes(int minutes);

    //-----------------------------------------------------------------------
    /**
     * Sets the number of seconds of the duration.
     * 
     * @param seconds  the number of seconds
     * @throws UnsupportedOperationException if field is not supported.
     */
    void setSeconds(int seconds);

    /**
     * Adds the specified seconds to the number of seconds in the duration.
     * 
     * @param seconds  the number of seconds
     * @throws UnsupportedOperationException if field is not supported.
     */
    void addSeconds(int seconds);

    //-----------------------------------------------------------------------
    /**
     * Sets the number of millis of the duration.
     * 
     * @param millis  the number of millis
     * @throws UnsupportedOperationException if field is not supported.
     */
    void setMillis(int millis);

    /**
     * Adds the specified millis to the number of millis in the duration.
     * 
     * @param millis  the number of millis
     * @throws UnsupportedOperationException if field is not supported.
     */
    void addMillis(int millis);

}
