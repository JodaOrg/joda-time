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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.joda.time.chrono.buddhist.BuddhistChronology;
import org.joda.time.chrono.gj.GJChronology;
import org.joda.time.chrono.iso.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;

/**
 * AbstractDateTime provides the common behaviour for immutable datetime
 * classes.
 * <p>
 * This class should generally not be used directly by API users. The 
 * {@link ReadableDateTime} interface should be used when different 
 * kinds of date/time objects are to be referenced.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public abstract class AbstractDateTime extends AbstractInstant
        implements ReadableDateTime {

    // The following package-private static methods are used in the
    // constructors for DateTime, DateOnly, TimeOnly, and MutableDateTime.

    /**
     * Validates the parameters and returns a suitable chronology.
     * 
     * @param chrono  the chronology to use, must not be null
     * @return the passed in Chronology
     * @throws IllegalArgumentException if the chronology is null
     */
    static Chronology selectChronology(Chronology chrono) {
        if (chrono == null) {
            throw new IllegalArgumentException("The Chronology must not be null");
        }
        return chrono;
    }

    /**
     * Validates the parameters and returns a suitable chronology. If the
     * instant's chronology is null, ISOChronology in the default time zone is
     * returned.
     * 
     * @param instant  the instant to use, must not be null
     * @return a suitable Chronology
     * @throws IllegalArgumentException if the instant is null
     */
    static Chronology selectChronology(ReadableInstant instant) {
        if (instant == null) {
            throw new IllegalArgumentException("The ReadableInstant must not be null");
        }
        Chronology chrono = instant.getChronology();
        if (chrono == null) {
            chrono = ISOChronology.getInstance();
        }
        return chrono;
    }

    /**
     * Validates the parameters and returns a suitable chronology. If the
     * instant's chronology is null, ISOChronology in the given time zone is
     * returned.
     * 
     * @param instant  the instant to use, must not be null
     * @param zone  the time zone to use, must not be null
     * @return a suitable Chronology
     * @throws IllegalArgumentException if the instant or zone is null
     */
    static Chronology selectChronology(ReadableInstant instant, DateTimeZone zone) {
        Chronology chrono = selectChronology(instant);
        if (zone == null) {
            throw new IllegalArgumentException("The DateTimeZone must not be null");
        }
        if (chrono.getDateTimeZone() != zone) {
            chrono = chrono.withDateTimeZone(zone);
            if (chrono == null) {
                chrono = ISOChronology.getInstance(zone);
            }
        }
        return chrono;
    }

    /**
     * Validates the parameters and returns a suitable chronology.
     *
     * @param instant  the instant to use, must not be null
     * @param chrono  the chronology to use, must not be null
     * @return the passed in chronology
     * @throws IllegalArgumentException if the instant or chronology is null
     */
    static Chronology selectChronology(ReadableInstant instant, Chronology chrono) {
        if (instant == null) {
            throw new IllegalArgumentException("The ReadableInstant must not be null");
        }
        return selectChronology(chrono);
    }

    /**
     * Validates the parameters and returns a suitable chronology. If the
     * instant's chronology is null, ISOChronology in UTC is returned.
     *
     * <p>Note: This method does not guarantee returning a UTC chronology
     * 
     * @param instant  the instant to use, must not be null
     * @return a suitable Chronology
     * @throws IllegalArgumentException if the instant is null
     */
    static Chronology selectChronologyUTC(ReadableInstant instant) {
        if (instant == null) {
            throw new IllegalArgumentException("The ReadableInstant must not be null");
        }
        Chronology chrono = instant.getChronology();
        if (chrono == null) {
            chrono = ISOChronology.getInstanceUTC();
        }
        return chrono;
    }

    /**
     * Validates the parameters and returns a suitable chronology.
     * 
     * @param date  the date to be validated, must not be null
     * @return a suitable Chronology
     * @throws IllegalArgumentException if the date is null
     */
    static Chronology selectChronology(Date date) {
        return selectChronology(date, ISOChronology.getInstance());
    }

    /**
     * Validates the parameters and returns a suitable chronology.
     * 
     * @param date  the date to be validated, must not be null
     * @param zone  the zone to use, must not be null
     * @return a suitable Chronology
     * @throws IllegalArgumentException if the date or zone is null
     */
    static Chronology selectChronology(Date date, DateTimeZone zone) {
        return selectChronology(date, ISOChronology.getInstance(zone));
    }

    /**
     * Validates the parameters and returns a suitable chronology.
     * 
     * @param date  the date to be validated, must not be null
     * @param chrono  the chronology to use, must not be null
     * @return a suitable Chronology
     * @throws IllegalArgumentException if the date or chronology is null
     */
    static Chronology selectChronology(Date date, Chronology chrono) {
        if (date == null) {
            throw new IllegalArgumentException("The Date must not be null");
        }
        return selectChronology(chrono);
    }

    /**
     * Validates the parameters and returns a suitable chronology. The method
     * is aware of GregorianCalendar and BuddhistCalendar and maps them
     * accordingly. Other calendars map to ISO.
     * 
     * @param calendar  the calendar to be used, must not be null
     * @return a suitable Chronology
     * @throws IllegalArgumentException if the calendar is null
     */
    static Chronology selectChronology(Calendar calendar) {
        if (calendar == null) {
            throw new IllegalArgumentException("The Calendar must not be null");
        }
        DateTimeZone zone = null;
        try {
            zone = DateTimeZone.getInstance(calendar.getTimeZone());
        } catch (IllegalArgumentException ex) {
            return ISOChronology.getInstance();
        }
        return selectChronology(calendar, zone);
    }

    /**
     * Validates the parameters and returns a suitable chronology. The method
     * is aware of GregorianCalendar and BuddhistCalendar and maps them
     * accordingly. Other calendars map to ISO.
     * 
     * @param calendar  the calendar to be used, must not be null
     * @param zone  the time zone to use
     * @return a suitable Chronology
     * @throws IllegalArgumentException if the calendar is null
     */
    static Chronology selectChronology(Calendar calendar, DateTimeZone zone) {
        if (calendar == null) {
            throw new IllegalArgumentException("The Calendar must not be null");
        }
        if (zone == null) {
            throw new IllegalArgumentException("The DateTimeZone must not be null");
        }
        if (calendar instanceof GregorianCalendar) {
            GregorianCalendar gc = (GregorianCalendar) calendar;
            return GJChronology.getInstance(zone, gc.getGregorianChange().getTime(), false);
        } else if (calendar.getClass().getName().endsWith(".BuddhistCalendar")) {
            return BuddhistChronology.getInstance(zone);
        } else {
            return ISOChronology.getInstance(zone);
        }
    }

    /**
     * Validates the parameters and returns a suitable chronology.
     * 
     * @param calendar  the calendar to be used, must not be null
     * @param chrono  the chronology to use, must not be null
     * @return a suitable Chronology
     * @throws IllegalArgumentException if the calendar is null
     */
    static Chronology selectChronology(Calendar calendar, Chronology chrono) {
        if (calendar == null) {
            throw new IllegalArgumentException("The Calendar must not be null");
        }
        return selectChronology(chrono);
    }

    /**
     * Validates the parameters and returns a suitable chronology.
     * 
     * @param string  the string to parse, must not be null
     * @return a suitable Chronology
     * @throws IllegalArgumentException if the string is null
     */
    static Chronology selectChronology(String str) {
        return selectChronology(str, ISOChronology.getInstance());
    }

    /**
     * Validates the parameters and returns a suitable chronology.
     * 
     * @param string  the string to parse, must not be null
     * @param zone  the zone to use, must not be null
     * @return a suitable Chronology
     * @throws IllegalArgumentException if the string or zone is null
     */
    static Chronology selectChronology(String str, DateTimeZone zone) {
        return selectChronology(str, ISOChronology.getInstance(zone));
    }

    /**
     * Validates the parameters and returns a suitable chronology.
     * 
     * @param str  the string to parse, must not be null
     * @param chrono  the chronology to use, must not be null
     * @return a suitable Chronology
     * @throws IllegalArgumentException if the string or chronology is null
     */
    static Chronology selectChronology(String str, Chronology chrono) {
        if (str == null) {
            throw new IllegalArgumentException("The String must not be null");
        }
        return selectChronology(chrono);
    }

    public AbstractDateTime() {
        super();
    }

    // Date field access
    //-----------------------------------------------------------------------
    /**
     * Get the day of week field value.
     * <p>
     * The values for the day of week are defined in {@link DateTimeConstants}.
     * 
     * @return the day of week
     */
    public final int getDayOfWeek() {
        return getChronology().dayOfWeek().get(getMillis());
    }

    /**
     * Get the day of month field value.
     * 
     * @return the day of month
     */
    public final int getDayOfMonth() {
        return getChronology().dayOfMonth().get(getMillis());
    }

    /**
     * Get the day of year field value.
     * 
     * @return the day of year
     */
    public final int getDayOfYear() {
        return getChronology().dayOfYear().get(getMillis());
    }

    /**
     * Get the week of weekyear field value.
     * 
     * @return the week of a week based year
     */
    public final int getWeekOfWeekyear() {
        return getChronology().weekOfWeekyear().get(getMillis());
    }

    /**
     * Get the weekyear field value.
     * 
     * @return the year of a week based year
     */
    public final int getWeekyear() {
        return getChronology().weekyear().get(getMillis());
    }

    /**
     * Get the month of year field value.
     * 
     * @return the month of year
     */
    public final int getMonthOfYear() {
        return getChronology().monthOfYear().get(getMillis());
    }

    /**
     * Get the year field value.
     * 
     * @return the year
     */
    public final int getYear() {
        return getChronology().year().get(getMillis());
    }

    /**
     * Get the year of era field value.
     * 
     * @return the year of era
     */
    public final int getYearOfEra() {
        return getChronology().yearOfEra().get(getMillis());
    }

    /**
     * Get the year of century field value.
     * 
     * @return the year of century
     */
    public final int getYearOfCentury() {
        return getChronology().yearOfCentury().get(getMillis());
    }

    /**
     * Get the year of era field value.
     * 
     * @return the year of era
     */
    public final int getCenturyOfEra() {
        return getChronology().centuryOfEra().get(getMillis());
    }

    /**
     * Get the era field value.
     * 
     * @return the era
     */
    public final int getEra() {
        return getChronology().era().get(getMillis());
    }

    // Time field access
    //-----------------------------------------------------------------------
    /**
     * Get the millis of second field value.
     *
     * @return the millis of second
     */
    public final int getMillisOfSecond() {
        return getChronology().millisOfSecond().get(getMillis());
    }

    /**
     * Get the millis of day field value.
     *
     * @return the millis of day
     */
    public final int getMillisOfDay() {
        return getChronology().millisOfDay().get(getMillis());
    }

    /**
     * Get the second of minute field value.
     *
     * @return the second of minute
     */
    public final int getSecondOfMinute() {
        return getChronology().secondOfMinute().get(getMillis());
    }

    /**
     * Get the second of day field value.
     *
     * @return the second of day
     */
    public final int getSecondOfDay() {
        return getChronology().secondOfDay().get(getMillis());
    }

    /**
     * Get the minute of hour field value.
     *
     * @return the minute of hour
     */
    public final int getMinuteOfHour() {
        return getChronology().minuteOfHour().get(getMillis());
    }

    /**
     * Get the minute of day field value.
     *
     * @return the minute of day
     */
    public final int getMinuteOfDay() {
        return getChronology().minuteOfDay().get(getMillis());
    }

    /**
     * Get the hour of day field value.
     *
     * @return the hour of day
     */
    public final int getHourOfDay() {
        return getChronology().hourOfDay().get(getMillis());
    }

    // Conversion
    //-----------------------------------------------------------------------
    /**
     * Get this object as a DateTime.
     * 
     * @return a DateTime using the same millis
     */
    public final DateTime toDateTime() {
        if (this instanceof DateTime) {
            return (DateTime)this;
        }
        return new DateTime(this);
    }

    /**
     * Get this object as a DateTime.
     * 
     * @param zone time zone to apply
     * @return a DateTime using the same millis
     * @throws IllegalArgumentException if the time zone is null
     */
    public final DateTime toDateTime(DateTimeZone zone) {
        if (zone == null) {
            throw new IllegalArgumentException("The DateTimeZone must not be null");
        }
        if (this instanceof DateTime) {
            DateTime dt = (DateTime)this;
            if (dt.getDateTimeZone() == zone) {
                return dt;
            }
        }
        return new DateTime(this, zone);
    }

    /**
     * Get this object as a DateTime.
     * 
     * @param chronology chronology to apply
     * @return a DateTime using the same millis
     * @throws IllegalArgumentException if the chronology is null
     */
    public final DateTime toDateTime(Chronology chronology) {
        if (chronology == null) {
            throw new IllegalArgumentException("The Chronology must not be null");
        }
        if (this instanceof DateTime) {
            DateTime dt = (DateTime)this;
            if (dt.getChronology() == chronology) {
                return dt;
            }
        }
        return new DateTime(this, chronology);
    }

    /**
     * Get this object as a trusted ISO DateTime.
     * 
     * @return an ISO DateTime using the same millis
     */
    public final DateTime toISODateTime() {
        if (this instanceof DateTime) {
            DateTime dt = (DateTime)this;
            if (dt.getChronology() instanceof ISOChronology) {
                // Verify that the time zone is trusted.
                DateTimeZone tz = dt.getDateTimeZone();
                DateTimeZone trusted = DateTimeZone.getInstance(tz.getID());
                if (tz == trusted) {
                    return dt;
                }
                return new DateTime(this, trusted);
            }
        }
        return new DateTime(this);
    }

    /**
     * Get this object as a trusted ISO DateTime.
     * 
     * @param zone time zone to apply
     * @return an ISO DateTime using the same millis
     * @throws IllegalArgumentException if the time zone is null
     */
    public final DateTime toISODateTime(DateTimeZone zone) {
        if (zone == null) {
            throw new IllegalArgumentException("The DateTimeZone must not be null");
        }
        if (this instanceof DateTime) {
            DateTime dt = (DateTime)this;
            if (dt.getChronology() instanceof ISOChronology) {
                if (dt.getDateTimeZone() == zone) {
                    return dt;
                }
            }
        }
        return new DateTime(this, zone);
    }

    /**
     * Get this object as a MutableDateTime.
     * 
     * @return a MutableDateTime using the same millis
     */
    public final MutableDateTime toMutableDateTime() {
        if (this instanceof MutableDateTime) {
            return (MutableDateTime)this;
        }
        return new MutableDateTime(this);
    }

    /**
     * Get this object as a MutableDateTime.
     * 
     * @param zone time zone to apply
     * @return a MutableDateTime using the same millis
     * @throws IllegalArgumentException if the time zone is null
     */
    public final MutableDateTime toMutableDateTime(DateTimeZone zone) {
        if (zone == null) {
            throw new IllegalArgumentException("The DateTimeZone must not be null");
        }
        if (this instanceof MutableDateTime) {
            MutableDateTime mdt = (MutableDateTime)this;
            if (mdt.getDateTimeZone() == zone) {
                return mdt;
            }
        }
        return new MutableDateTime(this, zone);
    }

    /**
     * Get this object as a MutableDateTime.
     * 
     * @param chronology chronology to apply
     * @return a MutableDateTime using the same millis
     * @throws IllegalArgumentException if the chronology is null
     */
    public final MutableDateTime toMutableDateTime(Chronology chronology) {
        if (chronology == null) {
            throw new IllegalArgumentException("The Chronology must not be null");
        }
        if (this instanceof MutableDateTime) {
            MutableDateTime mdt = (MutableDateTime)this;
            if (mdt.getChronology() == chronology) {
                return mdt;
            }
        }
        return new MutableDateTime(this, chronology);
    }

    /**
     * Get this object as a trusted ISO MutableDateTime.
     * 
     * @return an ISO MutableDateTime using the same millis
     */
    public final MutableDateTime toISOMutableDateTime() {
        if (this instanceof DateTime) {
            MutableDateTime mdt = (MutableDateTime)this;
            if (mdt.getChronology() instanceof ISOChronology) {
                // Verify that the time zone is trusted.
                DateTimeZone tz = mdt.getDateTimeZone();
                DateTimeZone trusted = DateTimeZone.getInstance(tz.getID());
                if (tz == trusted) {
                    return mdt;
                }
                return new MutableDateTime(this, trusted);
            }
        }
        return new MutableDateTime(this);
    }

    /**
     * Get this object as a trusted ISO MutableDateTime.
     * 
     * @param zone time zone to apply
     * @return an ISO MutableDateTime using the same millis
     * @throws IllegalArgumentException if the time zone is null
     */
    public final MutableDateTime toISOMutableDateTime(DateTimeZone zone) {
        if (zone == null) {
            throw new IllegalArgumentException("The DateTimeZone must not be null");
        }
        if (this instanceof MutableDateTime) {
            MutableDateTime mdt = (MutableDateTime)this;
            if (mdt.getChronology() instanceof ISOChronology) {
                if (mdt.getDateTimeZone() == zone) {
                    return mdt;
                }
            }
        }
        return new MutableDateTime(this, zone);
    }

    /**
     * Get this object as a DateOnly.
     * 
     * @return a DateOnly using the same millis
     */
    /*
    public final DateOnly toDateOnly() {
        if (this instanceof DateOnly) {
            return (DateOnly)this;
        }
        return new DateOnly(this);
    }
    /*

    /**
     * Get this object as a DateOnly.
     * 
     * @param chronology chronology to apply
     * @return a DateOnly using the same millis
     * @throws IllegalArgumentException if the chronology is null
     */
    /*
    public final DateOnly toDateOnly(Chronology chronology) {
        if (chronology == null) {
            throw new IllegalArgumentException("The Chronology must not be null");
        }
        chronology = chronology.withUTC();
        if (this instanceof DateOnly) {
            DateOnly d = (DateOnly)this;
            if (d.getChronology() == chronology) {
                return d;
            }
        }
        return new DateOnly(this, chronology);
    }
    */

    /**
     * Get this object as a TimeOnly.
     * 
     * @return a TimeOnly using the same millis
     */
    /*
    public final TimeOnly toTimeOnly() {
        if (this instanceof TimeOnly) {
            return (TimeOnly)this;
        }
        return new TimeOnly(this);
    }
    */

    /**
     * Get this object as a TimeOnly.
     * 
     * @param chronology chronology to apply
     * @return a TimeOnly using the same millis
     * @throws IllegalArgumentException if the chronology is null
     */
    /*
    public final TimeOnly toTimeOnly(Chronology chronology) {
        if (chronology == null) {
            throw new IllegalArgumentException("The Chronology must not be null");
        }
        chronology = chronology.withUTC();
        if (this instanceof TimeOnly) {
            TimeOnly t = (TimeOnly)this;
            if (t.getChronology() == chronology) {
                return t;
            }
        }
        return new TimeOnly(this, chronology);
    }
    */

    // Output
    //-----------------------------------------------------------------------
    /**
     * Output the date time in ISO8601 format (yyyy-MM-ddTHH:mm:ss.SSSZ).
     *
     * @return ISO8601 time formatted string.
     */
    public String toString() {
        return ISODateTimeFormat.getInstance(getChronology()).dateTime().print(this);
    }

    /**
     * Output the instant using the specified format pattern.
     *
     * @param pattern  the pattern specification
     * @see org.joda.time.format.DateTimeFormatterBuilder#appendPattern(java.lang.String)
     */
    public String toString(String pattern) throws IllegalArgumentException {
        Chronology chrono = getChronology();
        if (chrono == null) {
            chrono = ISOChronology.getInstanceUTC();
        }
        return DateTimeFormat.getInstance(chrono).forPattern(pattern).print(this);
    }

    /**
     * Output the instant using the specified format pattern.
     *
     * @param pattern  the pattern specification
     * @param locale  the Locale to use, must not be null
     * @see org.joda.time.format.DateTimeFormatterBuilder#appendPattern(java.lang.String)
     * @throws IllegalArgumentException if the locale is null
     */
    public String toString(String pattern, Locale locale) throws IllegalArgumentException {
        if (locale == null) {
            throw new IllegalArgumentException("The Locale must not be null");
        }
        Chronology chrono = getChronology();
        if (chrono == null) {
            chrono = ISOChronology.getInstanceUTC();
        }
        return DateTimeFormat.getInstance(chrono, locale).forPattern(pattern).print(this);
    }

}
