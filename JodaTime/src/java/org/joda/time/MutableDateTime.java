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

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.chrono.iso.ISOChronology;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.ISODateTimeFormat;

/**
 * MutableDateTime is the standard implementation of a modifiable
 * datetime class.
 * It holds the date/time as milliseconds from the Java epoch of
 * 1970-01-01T00:00:00Z.
 * <p>
 * This class requires a Chronology to be specified. The Chronology determines
 * how the millisecond instant value is converted into the date time fields.
 * <p>
 * Each individual field can be accessed in two ways:
 * <ul>
 * <li><code>getHourOfDay()</code>
 * <li><code>hourOfDay().get()</code>
 * </ul>
 * The second technique also provides access to other useful methods on the
 * field:
 * <ul>
 * <li>get numeric value
 * <li>set numeric value
 * <li>add to numeric value
 * <li>add to numeric value wrapping with the field
 * <li>get text vlaue
 * <li>get short text value
 * <li>set text value
 * <li>field maximum value
 * <li>field minimum value
 * </ul>
 *
 * @author Guy Allard
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public class MutableDateTime extends AbstractDateTime
    implements ReadableDateTime, ReadWritableInstant, Cloneable
{
    /** The millis from 1970-01-01T00:00:00Z. */
    private long iMillis;
    /** The chronology to use */
    private Chronology iChronology;

    // Constructors (same as DateTime)
    //-----------------------------------------------------------

    /**
     * Constructs a MutableDateTime to the current datetime, as reported by the system
     * clock. The chronology used is ISO, in the
     * {@link DateTimeZone#getDefault() default} time zone.
     */
    public MutableDateTime() {
        iChronology = ISOChronology.getInstance();
        iMillis = System.currentTimeMillis();
    }

    /**
     * Constructs a MutableDateTime to the current datetime, as reported by the system
     * clock. The chronology used is ISO, in the supplied time zone.
     *
     * @param zone  the time zone, must not be null
     * @throws IllegalArgumentException if the zone is null
     */
    public MutableDateTime(DateTimeZone zone) {
        iChronology = ISOChronology.getInstance(zone);
        iMillis = System.currentTimeMillis();
    }

    /**
     * Constructs a MutableDateTime to the current datetime, as reported by the system
     * clock.
     *
     * @param chronology  the chronology, must not be null
     * @throws IllegalArgumentException if the chronology is null
     */
    public MutableDateTime(Chronology chronology) {
        iChronology = selectChronology(chronology);
        iMillis = System.currentTimeMillis();
    }

    /**
     * Constructs a MutableDateTime set to the milliseconds from 1970-01-01T00:00:00Z,
     * using the ISO chronology, in the
     * {@link DateTimeZone#getDefault() default} time zone.
     *
     * @param millis  the milliseconds
     */
    public MutableDateTime(long millis) {
        iChronology = ISOChronology.getInstance();
        iMillis = millis;
    }

    /**
     * Constructs a MutableDateTime set to the milliseconds from 1970-01-01T00:00:00Z,
     * using the ISO chronology, in the supplied time zone.
     *
     * @param millis  the milliseconds
     * @param zone  the time zone, must not be null
     * @throws IllegalArgumentException if the zone is null
     */
    public MutableDateTime(long millis, DateTimeZone zone) {
        iChronology = ISOChronology.getInstance(zone);
        iMillis = millis;
    }

    /**
     * Constructs a MutableDateTime set to the milliseconds from 1970-01-01T00:00:00Z,
     * using the supplied chronology.
     *
     * @param millis  the milliseconds
     * @param chronology  the chronology, must not be null
     * @throws IllegalArgumentException if the chronology is null
     */
    public MutableDateTime(long millis, Chronology chronology) {
        iChronology = selectChronology(chronology);
        iMillis = millis;
    }

    /**
     * Constructs a MutableDateTime from a ReadableInstant, using its chronology. If
     * its chronology null, then the chronology is set to ISO, in the
     * {@link DateTimeZone#getDefault() default} time zone.
     *
     * @param instant  the ReadableInstant, must not be null
     * @throws IllegalArgumentException if the instant is null
     */
    public MutableDateTime(ReadableInstant instant) {
        iChronology = selectChronology(instant);
        iMillis = instant.getMillis();
    }

    /**
     * Constructs a MutableDateTime from a ReadableInstant, using its chronology
     * against a different time zone. If its chronology is null, then the
     * chronology is set to ISO. If the selected chronology is not in the
     * supplied time zone, a new chronology is created that is.
     *
     * @param instant  the ReadableInstant, must not be null
     * @param zone  the time zone, must not be null
     * @throws IllegalArgumentException if the instant or zone is null
     */
    public MutableDateTime(ReadableInstant instant, DateTimeZone zone) {
        iChronology = selectChronology(instant, zone);
        iMillis = instant.getMillis();
    }

    /**
     * Constructs a MutableDateTime from a ReadableInstant, using the supplied
     * chronology.
     *
     * @param instant  the ReadableInstant, must not be null
     * @param chronology  the chronology, must not be null
     * @throws IllegalArgumentException if the instant or chronology is null
     */
    public MutableDateTime(ReadableInstant instant, Chronology chronology) {
        iChronology = selectChronology(instant, chronology);
        iMillis = instant.getMillis();
    }

    /**
     * Constructs a MutableDateTime from a Date, using the ISO chronology, in the
     * {@link DateTimeZone#getDefault() default} time zone.
     *
     * @param date  the Date, must not be null
     * @throws IllegalArgumentException if the date is null
     */
    public MutableDateTime(Date date) {
        iChronology = selectChronology(date);
        iMillis = date.getTime();
    }

    /**
     * Constructs a MutableDateTime from a Date, using the ISO chronology, in the
     * supplied time zone.
     *
     * @param date  the Date, must not be null
     * @param zone  the time zone, must not be null
     * @throws IllegalArgumentException if the date or zone is null
     */
    public MutableDateTime(Date date, DateTimeZone zone) {
        iChronology = selectChronology(date, zone);
        iMillis = date.getTime();
    }

    /**
     * Constructs a MutableDateTime from a Date, using the supplied chronology.
     *
     * @param date  the Date, must not be null
     * @param chronology  the chronology, must not be null
     * @throws IllegalArgumentException if the date or chronology is null
     */
    public MutableDateTime(Date date, Chronology chronology) {
        iChronology = selectChronology(date, chronology);
        iMillis = date.getTime();
    }

    /**
     * Constructs a MutableDateTime from a Calendar, using its closest mapped
     * chronology and time zone.
     *
     * <p>When converting calendars to chronologies, the constructor is aware
     * of GregorianCalendar and BuddhistCalendar and maps them to the
     * equivalent chronology. Other calendars map to ISOChronology.
     *
     * @param calendar  the Calendar, must not be null
     * @throws IllegalArgumentException if the calendar is null
     */
    public MutableDateTime(Calendar calendar) {
        iChronology = selectChronology(calendar);
        iMillis = calendar.getTime().getTime();
    }

    /**
     * Constructs a MutableDateTime from a Calendar, using its closest mapped
     * chronology against a different time zone.
     *
     * <p>When converting calendars to chronologies, the constructor is aware
     * of GregorianCalendar and BuddhistCalendar and maps them to the
     * equivalent chronology. Other calendars map to ISOChronology.
     *
     * @param calendar  the Calendar, must not be null
     * @param chronology  the chronology, must not be null
     * @throws IllegalArgumentException if the calendar or zone is null
     */
    public MutableDateTime(Calendar calendar, DateTimeZone zone) {
        iChronology = selectChronology(calendar, zone);
        iMillis = calendar.getTime().getTime();
    }

    /**
     * Constructs a MutableDateTime from a Calendar, using the supplied chronology.
     *
     * @param calendar  the Calendar, must not be null
     * @param chronology  the chronology, must not be null
     * @throws IllegalArgumentException if the calendar or chronology is null
     */
    public MutableDateTime(Calendar calendar, Chronology chronology) {
        iChronology = selectChronology(calendar, chronology);
        iMillis = calendar.getTime().getTime();
    }

    /**
     * Constructs a MutableDateTime from an ISO formatted String, using the ISO
     * chronology, in the {@link DateTimeZone#getDefault() default} time zone.
     *
     * @param str  the string to parse, must not be null
     * @throws IllegalArgumentException if the string is null
     * @throws ParseException if parsing fails
     */
    public MutableDateTime(String str) throws ParseException {
        iChronology = selectChronology(str);
        DateTimeParser p = ISODateTimeFormat.getInstance(iChronology).dateTimeParser();
        iMillis = p.parseMillis(str);
    }

    /**
     * Constructs a MutableDateTime from an ISO formatted String, using the ISO
     * chronology, in the supplied time zone.
     *
     * @param str  the string to parse, must not be null
     * @param zone the time zone, must not be null
     * @throws IllegalArgumentException if the string or zone is null
     * @throws ParseException if parsing fails
     */
    public MutableDateTime(String str, DateTimeZone zone) throws ParseException {
        iChronology = selectChronology(str, zone);
        DateTimeParser p = ISODateTimeFormat.getInstance(iChronology).dateTimeParser();
        iMillis = p.parseMillis(str);
    }

    /**
     * Constructs a MutableDateTime from an ISO formatted String, using the supplied
     * chronology.
     *
     * @param str  the string to parse, must not be null
     * @param chronology  the chronology, must not be null
     * @throws IllegalArgumentException if the string or chronology is null
     * @throws ParseException if parsing fails
     */
    public MutableDateTime(String str, Chronology chronology) throws ParseException {
        iChronology = selectChronology(str, chronology);
        DateTimeParser p = ISODateTimeFormat.getInstance(iChronology).dateTimeParser();
        iMillis = p.parseMillis(str);
    }

    /**
     * Creates a new instance of this class.
     * <p>
     * The returned object will be a new instance of the implementation.
     * Immutable subclasses may return <code>this</code> if appropriate.
     *
     * @param millis  the new millis, from 1970-01-01T00:00:00Z
     * @param chrono  the new chronology
     * @return a new instance of this class
     * @throws IllegalArgumentException if the chronology is null
     */
    protected ReadableInstant create(long millis, Chronology chrono) {
        if (chrono == null) {
            throw new IllegalArgumentException("The Chronology must not be null");
        }
        return new MutableDateTime(millis, chrono);
    }
    
    // Accessor/mutator/adder methods (Accessors same as for DateTime)
    //-----------------------------------------------------------

    /**
     * Gets the milliseconds of the datetime instant from the Java epoch
     * of 1970-01-01T00:00:00Z.
     * 
     * @return the number of milliseconds since 1970-01-01T00:00:00Z
     */
    public long getMillis() {
        return iMillis;
    }

    /**
     * Set the milliseconds of the datetime.
     *
     * @param millis the milliseconds since 1970-01-01T00:00:00Z to set the
     * datetime to
     */
    public void setMillis(long millis) {
        iMillis = millis;
    }

    /**
     * Set the number of milliseconds of the datetime.
     * 
     * @param datetime  a ReadableInstant, Date, Calendar, Long or String
     * @throws IllegalArgumentException if the object is null
     * @throws ClassCastException if the object's type cannot be recognised
     */
    public void setMillis(Object object) {
        iMillis = getDateTimeMillisFromObject(object);
    }

    /**
     * Add an amount of time to the date.
     * 
     * @param duration duration to add.
     */
    /*
    public void add(ReadableDuration duration) {
        duration.addTo(this, 1);
    }
    */

    /**
     * Add an amount of time to the date.
     * 
     * @param duration duration to add.
     * @param scalar direction and amount to add, which may be negative
     */
    /*
    public void add(ReadableDuration duration, int scalar) {
        duration.addTo(this, scalar);
    }
    */

    /**
     * Add an amount of time to the date.
     * 
     * @param object a ReadableDuration, Long or String evaluating to a
     * duration
     */
    public void add(Object object) {
        /*
        if (object instanceof ReadableDuration) {
            add((ReadableDuration)object);
        } else {
            iMillis += getDurationMillisFromObject(object);
        }
        */
        iMillis += getDurationMillisFromObject(object);
    }

    /**
     * Add an amount of time to the datetime.
     * 
     * @param millis  the millis to add
     */
    public void addMillis(long millis) {
        iMillis += millis;
    }

    /**
     * Gets the chronology of the datetime.
     * 
     * @return the Chronology that the datetime is using
     */
    public Chronology getChronology() {
        return iChronology;
    }

    /**
     * Set the chronology of the datetime.
     * 
     * @param chronology  the chronology to use
     * @throws IllegalArgumentException if the chronology is null
     */
    public void setChronology(Chronology chronology) {
        if (chronology == null) {
            throw new IllegalArgumentException("The Chronology must not be null");
        }
        iChronology = chronology;
    }

    /**
     * Sets the time zone of the datetime via the chronology.
     *
     * @param zone  the time zone to use
     * @throws IllegalArgumentException if the time zone is null
     */
    public void setDateTimeZone(DateTimeZone zone) {
        if (zone == null) {
            throw new IllegalArgumentException("The DateTimeZone must not be null");
        }
        if (iChronology.getDateTimeZone() != zone) {
            iChronology = iChronology.withDateTimeZone(zone);
        }
    }

    // public int get(DateTimeField field);  inherited from AbstractInstant

    /**
     * Set a value in the specified field.
     * This could be used to set a field using a different Chronology.
     * For example:
     * <pre>
     * MutableDateTime dt = new MutableDateTime();
     * dt.set(GJChronology.getInstance().year(), 2002);
     * </pre>
     * 
     * @param field  the DateTimeField to use
     * @param value the value
     * @throws NullPointerException if the field is null
     */
    public void set(DateTimeField field, int value) {
        iMillis = field.set(getMillis(), value);
    }

    /**
     * Add a value to the specified field.
     * This could be used to set a field using a different Chronology.
     * For example:
     * <pre>
     * MutableDateTime dt = new MutableDateTime();
     * dt.add(GJChronology.getInstance().year(), 2);
     * </pre>
     * 
     * @param field  the DateTimeField to use
     * @param value the value
     * @throws NullPointerException if the field is null
     */
    public void add(DateTimeField field, int value) {
        iMillis = field.add(getMillis(), value);
    }

    /**
     * Add a value to the specified field, wrapping within that field.
     * This could be used to set a field using a different Chronology.
     * For example:
     * <pre>
     * MutableDateTime dt = new MutableDateTime();
     * dt.addWrapped(GJChronology.getInstance().monthOfYear(), 6);
     * </pre>
     * 
     * @param field  the DateTimeField to use
     * @param value the value
     * @throws NullPointerException if the field is null
     */
    public void addWrapped(DateTimeField field, int value) {
        iMillis = field.addWrapped(getMillis(), value);
    }

    // Date field mutator/adder methods
    //-----------------------------------------------------------

    /**
     * Set the day of week to a value.
     *
     * @param dayOfWeek  the day of the week.
     */
    public void setDayOfWeek(int dayOfWeek) {
        iMillis = getChronology().dayOfWeek().set(iMillis, dayOfWeek);
    }

    /**
     * Set the day of the month to a value.
     *
     * @param dayOfMonth  the day of the month.
     */
    public void setDayOfMonth(int dayOfMonth) {
        iMillis = getChronology().dayOfMonth().set(iMillis, dayOfMonth);
    }

    /**
     * Set the day of year to a value.
     *
     * @param dayOfYear the day of the year.
     */
    public void setDayOfYear(int dayOfYear) {
        iMillis = getChronology().dayOfYear().set(iMillis, dayOfYear);
    }

    /**
     * Add a number of days to the date.
     *
     * @param days  the days to add.
     */
    public void addDays(int days) {
        iMillis = getChronology().dayOfYear().add(iMillis, days);
    }

    /**
     * Set the week of weekyear to a value.
     *
     * @param weekOfWeekyear the week of the weekyear.
     */
    public void setWeekOfWeekyear(int weekOfWeekyear) {
        iMillis = getChronology().weekOfWeekyear().set(iMillis, weekOfWeekyear);
    }

    /**
     * Add a number of weeks to the date.
     *
     * @param weeks  the weeks to add.
     */
    public void addWeeks(int weeks) {
        iMillis = getChronology().weekOfWeekyear().add(iMillis, weeks);
    }

    /**
     * Set the week of the year to a value.
     *
     * @param weekyear  the weekyear.
     */
    public void setWeekyear(int weekyear) {
        iMillis = getChronology().weekyear().set(iMillis, weekyear);
    }

    /**
     * Add a number of weekyears to the date.
     *
     * @param weekyears  the weekyears to add.
     */
    public void addWeekyears(int weekyears) {
        iMillis = getChronology().weekyear().add(iMillis, weekyears);
    }

    /**
     * Set the month of the year to a value.
     *
     * @param month  the month of the year.
     */
    public void setMonthOfYear(int month) {
        iMillis = getChronology().monthOfYear().set(iMillis, month);
    }

    /**
     * Add a number of months to the date.
     *
     * @param months  the months to add.
     */
    public void addMonths(int months) {
        iMillis = getChronology().monthOfYear().add(iMillis, months);
    }

    /**
     * Set the year to a value.
     *
     * @param year  the year.
     */
    public void setYear(int year) {
        iMillis = getChronology().year().set(iMillis, year);
    }

    /**
     * Add a number of years to the date.
     *
     * @param years  the years to add.
     */
    public void addYears(int years) {
        iMillis = getChronology().year().add(iMillis, years);
    }

    // Time field mutator/adder methods
    //-----------------------------------------------------------

    /**
     * Set the millis of the second.
     *
     * @param millis  the millis of second.
     */
    public void setMillisOfSecond(int millis) {
        iMillis = getChronology().millisOfSecond().set(iMillis, millis);
    }

    /**
     * Set the millis of the day.
     *
     * @param millis  the millis of day.
     */
    public void setMillisOfDay(int millis) {
        iMillis = getChronology().millisOfDay().set(iMillis, millis);
    }

    /**
     * Set the second of the minute.
     *
     * @param second  the second of minute.
     */
    public void setSecondOfMinute(int second) {
        iMillis = getChronology().secondOfMinute().set(iMillis, second);
    }

    /**
     * Set the second of the day.
     *
     * @param second  the second of day.
     */
    public void setSecondOfDay(int second) {
        iMillis = getChronology().secondOfDay().set(iMillis, second);
    }

    /**
     * Add a number of seconds to the date.
     *
     * @param seconds  the seconds to add.
     */
    public void addSeconds(int seconds) {
        iMillis = getChronology().secondOfDay().add(iMillis, seconds);
    }

    /**
     * Set the minute of the hour.
     *
     * @param minute  the minute of hour.
     */
    public void setMinuteOfHour(int minute) {
        iMillis = getChronology().minuteOfHour().set(iMillis, minute);
    }

    /**
     * Set the minute of the day.
     *
     * @param minute  the minute of day.
     */
    public void setMinuteOfDay(int minute) {
        iMillis = getChronology().minuteOfDay().set(iMillis, minute);
    }

    /**
     * Add a number of minutes to the date.
     *
     * @param minutes  the minutes to add.
     */
    public void addMinutes(int minutes) {
        iMillis = getChronology().minuteOfDay().add(iMillis, minutes);
    }

    /**
     * Set the hour of the day.
     *
     * @param hour  the hour of day.
     */
    public void setHourOfDay(int hour) {
        iMillis = getChronology().hourOfDay().set(iMillis, hour);
    }

    /**
     * Add a number of hours to the date.
     *
     * @param hours  the hours to add.
     */
    public void addHours(int hours) {
        iMillis = getChronology().hourOfDay().add(iMillis, hours);
    }

    // Convenient helpers
    //----------------------------------------------------
    
    /**
     * Set the date from various different types of object.
     * The time part of the parameter will be ignored.
     * The time part of this object will be unaffected.
     *
     * @param date  a ReadableInstant, Date, Calendar, Long or String
     * @throws IllegalArgumentException if the object is null.
     * @throws ClassCastException if the object's type cannot be recognised
     */
    public void setDate(Object date) {
        setDate(getDateTimeMillisFromObject(date));
    }

    /**
     * Set the date from milliseconds.
     * The time part of the parameter will be ignored.
     * The time part of this object will be unaffected.
     *
     * @param millis milliseconds from 1970-01-01T00:00:00Z, ignoring time of
     * day
     */
    public void setDate(long millis) {
        Chronology c = getChronology();
        iMillis = c.getDateOnlyMillis(millis) + c.getTimeOnlyMillis(iMillis);
    }

    /**
     * Set the date from fields.
     * The time part of this object will be unaffected.
     *
     * @param year  the year
     * @param month  the month of the year
     * @param dayOfMonth  the day of the month
     */
    public void setDate(int year, int monthOfYear, int dayOfMonth) {
        // Do not modify this object until method completion
        Chronology c = getChronology();
        long workVal = c.getTimeOnlyMillis( iMillis );
        workVal = c.year().set( workVal, year );
        workVal = c.monthOfYear().set( workVal, monthOfYear );
        workVal = c.dayOfMonth().set( workVal, dayOfMonth );
        iMillis = workVal;
    }

    /**
     * Set the time from an object.
     * The date part of the parameter will be ignored.
     * The date part of this object will be unaffected.
     *
     * @param time  a ReadableInstant, Date, Calendar, Long or String
     * @throws IllegalArgumentException if the object is null.
     * @throws ClassCastException if the object's type cannot be recognised
     */
    public void setTime(Object time) {
        setTime(getDateTimeMillisFromObject(time));
    }

    /**
     * Set the time from milliseconds.
     * The date part of the parameter will be ignored.
     * The date part of this object will be unaffected.
     *
     * @param millis milliseconds from T00:00:00Z, ignoring date
     */
    public void setTime(long millis) {
        Chronology c = getChronology();
        iMillis = c.getDateOnlyMillis(iMillis) + c.getTimeOnlyMillis(millis);
    }

    /**
     * Set the time from fields.
     * The date part of this object will be unaffected.
     *
     * @param hour  the hour
     * @param minuteOfHour  the minute of the hour
     * @param secondOfMinute  the second of the minute
     * @param millisOfSecond  the milisecond of the second
     */
    public void setTime(int hour, int minuteOfHour, int secondOfMinute, int millisOfSecond) {
        // Do not modify this object until method completion
        Chronology c = getChronology();
        long workVal = c.getDateOnlyMillis( iMillis );
        workVal = c.hourOfDay().set( workVal, hour );
        workVal = c.minuteOfHour().set( workVal, minuteOfHour );
        workVal = c.secondOfMinute().set( workVal, secondOfMinute );
        workVal = c.millisOfSecond().set( workVal, millisOfSecond );
        iMillis = workVal;
    }

    /**
     * Set the date and time from an object.
     *
     * @param datetime  a ReadableInstant, Date, Calendar, Long or String
     * @throws IllegalArgumentException if the object is null.
     * @throws ClassCastException if the object's type cannot be recognised
     */
    public void setDateTime(Object datetime) {
        iMillis = getDateTimeMillisFromObject(datetime);
    }

    /**
     * Set the date and time from milliseconds.
     *
     * @param millis  the millis
     */
    public void setDateTime(long millis) {
        iMillis = millis;
    }

    /**
     * Set the date and time from fields.
     *
     * @param year  the year
     * @param month  the month of the year
     * @param dayOfMonth  the day of the month
     * @param hour  the hour
     * @param minuteOfHour  the minute of the hour
     * @param secondOfMinute  the second of the minute
     * @param millisOfSecond  the milisecond of the second
     */
    public void setDateTime(int year,
                            int monthOfYear,
                            int dayOfMonth,
                            int hourOfDay,
                            int minuteOfHour,
                            int secondOfMinute,
                            int millisOfSecond)
    {
        // Do not modify this object until method completion
        Chronology c = getChronology();
        long workVal = 0;
        workVal = c.year().set( workVal, year );
        workVal = c.monthOfYear().set( workVal, monthOfYear );
        workVal = c.dayOfMonth().set( workVal, dayOfMonth );
        workVal = c.hourOfDay().set( workVal, hourOfDay );
        workVal = c.minuteOfHour().set( workVal, minuteOfHour );
        workVal = c.secondOfMinute().set( workVal, secondOfMinute );
        workVal = c.millisOfSecond().set( workVal, millisOfSecond );
        iMillis = workVal;
    }

    // Date properties
    //-----------------------------------------------------------

    /**
     * Get the day of week property.
     * <p>
     * The values for day of week are defined in {@link DateTimeConstants}.
     * 
     * @return the day of week property
     */
    public MutableDateTimeFieldProperty dayOfWeek() {
        return new MutableDateTimeFieldProperty(this, getChronology().dayOfWeek());
    }

    /**
     * Get the day of month property.
     * 
     * @return the day of month property
     */
    public MutableDateTimeFieldProperty dayOfMonth() {
        return new MutableDateTimeFieldProperty(this, getChronology().dayOfMonth());
    }

    /**
     * Get the day of year property.
     * 
     * @return the day of year property
     */
    public MutableDateTimeFieldProperty dayOfYear() {
        return new MutableDateTimeFieldProperty(this, getChronology().dayOfYear());
    }

    /**
     * Get the week of a week based year property.
     * 
     * @return the week of a week based year property
     */
    public MutableDateTimeFieldProperty weekOfWeekyear() {
        return new MutableDateTimeFieldProperty(this, getChronology().weekOfWeekyear());
    }

    /**
     * Get the year of a week based year property.
     * 
     * @return the year of a week based year property
     */
    public MutableDateTimeFieldProperty weekyear() {
        return new MutableDateTimeFieldProperty(this, getChronology().weekyear());
    }

    /**
     * Get the month of year property.
     * 
     * @return the month of year property
     */
    public MutableDateTimeFieldProperty monthOfYear() {
        return new MutableDateTimeFieldProperty(this, getChronology().monthOfYear());
    }

    /**
     * Get the year property.
     * 
     * @return the year property
     */
    public MutableDateTimeFieldProperty year() {
        return new MutableDateTimeFieldProperty(this, getChronology().year());
    }

    /**
     * Get the year of era property.
     * 
     * @return the year of era property
     */
    public MutableDateTimeFieldProperty yearOfEra() {
        return new MutableDateTimeFieldProperty(this, getChronology().yearOfEra());
    }

    /**
     * Get the year of century property.
     * 
     * @return the year of era property
     */
    public MutableDateTimeFieldProperty yearOfCentury() {
        return new MutableDateTimeFieldProperty(this, getChronology().yearOfCentury());
    }

    /**
     * Get the century of era property.
     * 
     * @return the year of era property
     */
    public MutableDateTimeFieldProperty centuryOfEra() {
        return new MutableDateTimeFieldProperty(this, getChronology().centuryOfEra());
    }

    /**
     * Get the era property.
     * 
     * @return the era property
     */
    public MutableDateTimeFieldProperty era() {
        return new MutableDateTimeFieldProperty(this, getChronology().era());
    }

    // Time properties
    //-----------------------------------------------------------
    
    /**
     * Get the millis of second property
     * 
     * @return the millis of second property
     */
    public MutableDateTimeFieldProperty millisOfSecond() {
        return new MutableDateTimeFieldProperty(this, getChronology().millisOfSecond());
    }

    /**
     * Get the millis of day property
     * 
     * @return the millis of day property
     */
    public MutableDateTimeFieldProperty millisOfDay() {
        return new MutableDateTimeFieldProperty(this, getChronology().millisOfDay());
    }

    /**
     * Get the second of minute field property
     * 
     * @return the second of minute property
     */
    public MutableDateTimeFieldProperty secondOfMinute() {
        return new MutableDateTimeFieldProperty(this, getChronology().secondOfMinute());
    }

    /**
     * Get the second of day property
     * 
     * @return the second of day property
     */
    public MutableDateTimeFieldProperty secondOfDay() {
        return new MutableDateTimeFieldProperty(this, getChronology().secondOfDay());
    }

    /**
     * Get the minute of hour field property
     * 
     * @return the minute of hour property
     */
    public MutableDateTimeFieldProperty minuteOfHour() {
        return new MutableDateTimeFieldProperty(this, getChronology().minuteOfHour());
    }

    /**
     * Get the minute of day property
     * 
     * @return the minute of day property
     */
    public MutableDateTimeFieldProperty minuteOfDay() {
        return new MutableDateTimeFieldProperty(this, getChronology().minuteOfDay());
    }

    /**
     * Get the hour of day field property
     * 
     * @return the hour of day property
     */
    public MutableDateTimeFieldProperty hourOfDay() {
        return new MutableDateTimeFieldProperty(this, getChronology().hourOfDay());
    }

    // Misc
    //-----------------------------------------------------------

    /**
     * Output the date time in ISO8601 format (yyyy-MM-ddTHH:mm:ss.SSSZ).
     * 
     * @return ISO8601 time formatted string.
     */
    public String toString() {
        return ISODateTimeFormat.getInstance(getChronology()).dateTime().print(this);
    }

    /**
     * Clone this object without having to cast the returned object.
     *
     * @return a clone of the this object.
     */
    public MutableDateTime copy() {
        try {
            return (MutableDateTime)super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new InternalError("Clone error");
        }
    }

    /**
     * Clone this object.
     *
     * @return a clone of this object.
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new InternalError("Clone error");
        }
    }

    /**
     * Extracts a long datetime value from an object.
     *
     * @param object  an input object
     * @return a long date-time value
     * @throws IllegalArgumentException if the object is null, or the string invalid
     * @throws ClassCastException if the object type is not supported
     */
    protected long getDateTimeMillisFromObject(Object object) {
        if (object instanceof ReadableInstant) {
            return ((ReadableInstant) object).getMillis();

        } else if (object instanceof Date) {
            return ((Date) object).getTime();

        } else if (object instanceof Calendar) {
            return ((Calendar) object).getTime().getTime();

        } else if (object instanceof Long) {
            return ((Long) object).longValue();

        } else if (object instanceof String) {
            try {
                Instant instant = new Instant((String) object);
                return instant.getMillis();

            } catch (ParseException ex) {
                throw new IllegalArgumentException("String '" + object + "' is an invalid date format");
            }
        } else if (object == null) {
            throw new IllegalArgumentException("<null> cannot be converted to a datetime");
        } else {
            throw new ClassCastException("Class '" + object.getClass().getName() + "' cannot be converted to a datetime");
        }
    }

    /**
     * Extracts a fixed millisecond duration from an object.
     * 
     * @param object  Long or String
     * @throws IllegalArgumentException if the object is null, or the string invalid
     * @throws ClassCastException if the object type is not supported
     */
    protected long getDurationMillisFromObject(Object object) {
        if (object instanceof Long) {
            Long other = (Long) object;
            return other.longValue();
        /* TODO } else if (object instanceof String) {
            try {
                TimePeriod other = new TimePeriod((String) object);
                return other.getMillis();
            } catch (ParseException ex) {
                throw new IllegalArgumentException
                    ("String '" + object + "' is an invalid time period format");
            }*/
        } else {
            String type;
            if (object == null) {
                type = "<null>";
            } else {
                type = "Object of type \"" + object.getClass().getName() + '"';
            }
            throw new IllegalArgumentException(type + " cannot be converted to a duration");
        }
    }
    
}
