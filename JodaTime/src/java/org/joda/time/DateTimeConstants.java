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
 * DateTimeConstants is a non-instantiable class of constants used in
 * the date time system. These are the ISO8601 constants, but should be
 * used by all chronologies.
 * <p>
 * DateTimeConstants is thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class DateTimeConstants {

    // These are ints not enumerations as they represent genuine int values
    /** Constant (1) representing January, the first month (ISO) */
    public static final int JANUARY = 1;

    /** Constant (2) representing February, the second month (ISO) */
    public static final int FEBRUARY = 2;

    /** Constant (3) representing March, the third month (ISO) */
    public static final int MARCH = 3;

    /** Constant (4) representing April, the fourth month (ISO) */
    public static final int APRIL = 4;

    /** Constant (5) representing May, the fifth month (ISO) */
    public static final int MAY = 5;

    /** Constant (6) representing June, the sixth month (ISO) */
    public static final int JUNE = 6;

    /** Constant (7) representing July, the seventh month (ISO) */
    public static final int JULY = 7;

    /** Constant (8) representing August, the eighth month (ISO) */
    public static final int AUGUST = 8;

    /** Constant (9) representing September, the nineth month (ISO) */
    public static final int SEPTEMBER = 9;

    /** Constant (10) representing October, the tenth month (ISO) */
    public static final int OCTOBER = 10;

    /** Constant (11) representing November, the eleventh month (ISO) */
    public static final int NOVEMBER = 11;

    /** Constant (12) representing December, the twelfth month (ISO) */
    public static final int DECEMBER = 12;

    // These are ints not enumerations as they represent genuine int values
    /** Constant (1) representing Monday, the first day of the week (ISO) */
    public static final int MONDAY = 1;

    /** Constant (2) representing Monday, the second day of the week (ISO) */
    public static final int TUESDAY = 2;

    /** Constant (3) representing Monday, the third day of the week (ISO) */
    public static final int WEDNESDAY = 3;

    /** Constant (4) representing Monday, the fourth day of the week (ISO) */
    public static final int THURSDAY = 4;

    /** Constant (5) representing Monday, the fifth day of the week (ISO) */
    public static final int FRIDAY = 5;

    /** Constant (6) representing Monday, the sixth day of the week (ISO) */
    public static final int SATURDAY = 6;

    /** Constant (7) representing Monday, the seventh day of the week (ISO) */
    public static final int SUNDAY = 7;


    /** Constant (0) representing AM, the morning (from Calendar) */
    public static final int AM = 0;

    /** Constant (1) representing PM, the afternoon (from Calendar) */
    public static final int PM = 1;


    /** Constant (0) representing BC, years before zero (from Calendar) */
    public static final int BC = 0;
    /** Alternative constant (0) representing BCE, Before Common Era (secular) */
    public static final int BCE = 0;

    /** Constant (1) representing AD, years after zero (from Calendar) */
    public static final int AD = 1;
    /** Alternative constant (1) representing CE, Common Era (secular) */
    public static final int CE = 1;


    /** Milliseconds in one second (1000) (ISO) */
    public static final int MILLIS_PER_SECOND = 1000;

    /** Seconds in one minute (60) (ISO) */
    public static final int SECONDS_PER_MINUTE = 60;
    /** Milliseconds in one minute (ISO) */
    public static final int MILLIS_PER_MINUTE = MILLIS_PER_SECOND * SECONDS_PER_MINUTE;

    /** Minutes in one hour (ISO) */
    public static final int MINUTES_PER_HOUR = 60;
    /** Milliseconds in one hour (ISO) */
    public static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
    /** Minutes in one hour (60) (ISO) */
    public static final int MILLIS_PER_HOUR = MILLIS_PER_MINUTE * MINUTES_PER_HOUR;

    /** Hours in a typical day (24) (ISO). Due to time zone offset changes, the
     * number of hours per day can vary. */
    public static final int HOURS_PER_DAY = 24;
    /** Minutes in a typical day (ISO). Due to time zone offset changes, the number
     * of minutes per day can vary. */
    public static final int MINUTES_PER_DAY = MINUTES_PER_HOUR * HOURS_PER_DAY;
    /** Seconds in a typical day (ISO). Due to time zone offset changes, the number
     * of seconds per day can vary. */
    public static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * HOURS_PER_DAY;
    /** Milliseconds in a typical day (ISO). Due to time zone offset changes, the
     * number of milliseconds per day can vary. */
    public static final int MILLIS_PER_DAY = MILLIS_PER_HOUR * HOURS_PER_DAY;

    /** Days in one week (7) (ISO) */
    public static final int DAYS_PER_WEEK = 7;
    /** Hours in a typical week. Due to time zone offset changes, the number of
     * hours per week can vary. */
    public static final int HOURS_PER_WEEK = HOURS_PER_DAY * DAYS_PER_WEEK;
    /** Minutes in a typical week (ISO). Due to time zone offset changes, the number
     * of minutes per week can vary. */
    public static final int MINUTES_PER_WEEK = MINUTES_PER_DAY * DAYS_PER_WEEK;
    /** Seconds in a typical week (ISO). Due to time zone offset changes, the number
     * of seconds per week can vary. */
    public static final int SECONDS_PER_WEEK = SECONDS_PER_DAY * DAYS_PER_WEEK;
    /** Milliseconds in a typical week (ISO). Due to time zone offset changes, the
     * number of milliseconds per week can vary. */
    public static final int MILLIS_PER_WEEK = MILLIS_PER_DAY * DAYS_PER_WEEK;

    /**
     * Restrictive constructor
     */
    protected DateTimeConstants() {
    }

}
