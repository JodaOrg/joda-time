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
package org.joda.time.base;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableDateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * AbstractDateTime provides the common behaviour for datetime classes.
 * <p>
 * This class should generally not be used directly by API users.
 * The {@link ReadableDateTime} interface should be used when different 
 * kinds of date/time objects are to be referenced.
 * <p>
 * Whenever you want to implement <code>ReadableDateTime</code> you should
 * extend this class.
 * <p>
 * AbstractDateTime subclasses may be mutable and not thread-safe.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public abstract class AbstractDateTime
        extends AbstractInstant
        implements ReadableDateTime {

    //-----------------------------------------------------------------------
    /**
     * Constructor.
     */
    protected AbstractDateTime() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * Get the value of one of the fields of a datetime.
     * <p>
     * This method uses the chronology of the datetime to obtain the value.
     * It is essentially a generic way of calling one of the get methods.
     *
     * @param type  a field type, usually obtained from DateTimeFieldType
     * @return the value of that field
     * @throws IllegalArgumentException if the field type is null
     */
    public int get(DateTimeFieldType type) {
        if (type == null) {
            throw new IllegalArgumentException("The DateTimeFieldType must not be null");
        }
        return type.getField(getChronology()).get(getMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Get the era field value.
     * 
     * @return the era
     */
    public int getEra() {
        return getChronology().era().get(getMillis());
    }

    /**
     * Get the year of era field value.
     * 
     * @return the year of era
     */
    public int getCenturyOfEra() {
        return getChronology().centuryOfEra().get(getMillis());
    }

    /**
     * Get the year of era field value.
     * 
     * @return the year of era
     */
    public int getYearOfEra() {
        return getChronology().yearOfEra().get(getMillis());
    }

    /**
     * Get the year of century field value.
     * 
     * @return the year of century
     */
    public int getYearOfCentury() {
        return getChronology().yearOfCentury().get(getMillis());
    }

    /**
     * Get the year field value.
     * 
     * @return the year
     */
    public int getYear() {
        return getChronology().year().get(getMillis());
    }

    /**
     * Get the weekyear field value.
     * 
     * @return the year of a week based year
     */
    public int getWeekyear() {
        return getChronology().weekyear().get(getMillis());
    }

    /**
     * Get the month of year field value.
     * 
     * @return the month of year
     */
    public int getMonthOfYear() {
        return getChronology().monthOfYear().get(getMillis());
    }

    /**
     * Get the week of weekyear field value.
     * 
     * @return the week of a week based year
     */
    public int getWeekOfWeekyear() {
        return getChronology().weekOfWeekyear().get(getMillis());
    }

    /**
     * Get the day of year field value.
     * 
     * @return the day of year
     */
    public int getDayOfYear() {
        return getChronology().dayOfYear().get(getMillis());
    }

    /**
     * Get the day of month field value.
     * <p>
     * The values for the day of month are defined in {@link org.joda.time.DateTimeConstants}.
     * 
     * @return the day of month
     */
    public int getDayOfMonth() {
        return getChronology().dayOfMonth().get(getMillis());
    }

    /**
     * Get the day of week field value.
     * <p>
     * The values for the day of week are defined in {@link org.joda.time.DateTimeConstants}.
     * 
     * @return the day of week
     */
    public int getDayOfWeek() {
        return getChronology().dayOfWeek().get(getMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Get the hour of day field value.
     *
     * @return the hour of day
     */
    public int getHourOfDay() {
        return getChronology().hourOfDay().get(getMillis());
    }

    /**
     * Get the minute of day field value.
     *
     * @return the minute of day
     */
    public int getMinuteOfDay() {
        return getChronology().minuteOfDay().get(getMillis());
    }

    /**
     * Get the minute of hour field value.
     *
     * @return the minute of hour
     */
    public int getMinuteOfHour() {
        return getChronology().minuteOfHour().get(getMillis());
    }

    /**
     * Get the second of day field value.
     *
     * @return the second of day
     */
    public int getSecondOfDay() {
        return getChronology().secondOfDay().get(getMillis());
    }

    /**
     * Get the second of minute field value.
     *
     * @return the second of minute
     */
    public int getSecondOfMinute() {
        return getChronology().secondOfMinute().get(getMillis());
    }

    /**
     * Get the millis of day field value.
     *
     * @return the millis of day
     */
    public int getMillisOfDay() {
        return getChronology().millisOfDay().get(getMillis());
    }

    /**
     * Get the millis of second field value.
     *
     * @return the millis of second
     */
    public int getMillisOfSecond() {
        return getChronology().millisOfSecond().get(getMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Get the date time as a <code>java.util.Calendar</code>.
     * The locale is passed in, enabling Calendar to select the correct
     * localized subclass.
     * 
     * @param locale  the locale to get the Calendar for, or default if null
     * @return a localized Calendar initialised with this datetime
     */
    public Calendar toCalendar(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        DateTimeZone zone = getZone();
        Calendar cal;
        if (zone == null) {
            cal = Calendar.getInstance(locale);
        } else {
            cal = Calendar.getInstance(zone.toTimeZone(), locale);
        }
        cal.setTime(toDate());
        return cal;
    }

    /**
     * Get the date time as a <code>java.util.GregorianCalendar</code>.
     * 
     * @return a GregorianCalendar initialised with this datetime
     */
    public GregorianCalendar toGregorianCalendar() {
        DateTimeZone zone = getZone();
        GregorianCalendar cal;
        if (zone == null) {
            cal = new GregorianCalendar();
        } else {
            cal = new GregorianCalendar(zone.toTimeZone());
        }
        cal.setTime(toDate());
        return cal;
    }

    //-----------------------------------------------------------------------
    /**
     * Output the instant using the specified format pattern.
     *
     * @param pattern  the pattern specification, null means use <code>toString</code>
     * @see  org.joda.time.format.DateTimeFormat
     */
    public String toString(String pattern) {
        if (pattern == null) {
            return toString();
        }
        return DateTimeFormat.getInstance(getChronology()).forPattern(pattern).print(this);
    }

    /**
     * Output the instant using the specified format pattern.
     *
     * @param pattern  the pattern specification, null means use <code>toString</code>
     * @param locale  Locale to use, null means default
     * @see  org.joda.time.format.DateTimeFormat
     */
    public String toString(String pattern, Locale locale) throws IllegalArgumentException {
        if (pattern == null) {
            return toString();
        }
        return DateTimeFormat.getInstance(getChronology(), locale).forPattern(pattern).print(this);
    }

}
