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
 * DateTime is the basic implementation of a datetime class supporting
 * chronologies and time zones. It holds the time as milliseconds from the Java
 * epoch of 1970-01-01T00:00:00Z.
 *
 * <p>Each individual field can be queried in two ways:
 * <ul>
 * <li><code>getHourOfDay()</code>
 * <li><code>hourOfDay().get()</code>
 * </ul>
 * The second technique also provides access to other useful methods on the
 * field:
 * <ul>
 * <li>numeric value
 * <li>text value
 * <li>short text value
 * <li>maximum value
 * <li>minimum value
 * </ul>
 *
 * <p>This class is immutable provided that the Chronology is immutable. All 
 * Chronology classes supplied are immutable.
 *
 * @author Stephen Colebourne
 * @author Kandarp Shah
 * @author Brian S O'Neill
 * @since 1.0
 */
public class DateTime extends AbstractDateTime implements ReadableDateTime {
    
    /** The millis from 1970-01-01T00:00:00Z */
    private final long iMillis;
    /** The chronology to use */
    private final Chronology iChronology;

    // Constructors
    //-----------------------------------------------------------------------
    /**
     * Constructs a DateTime to the current datetime, as reported by the system
     * clock. The chronology used is ISO, in the
     * {@link DateTimeZone#getDefault() default} time zone.
     */
    public DateTime() {
        iChronology = ISOChronology.getInstance();
        iMillis = System.currentTimeMillis();
    }

    /**
     * Constructs a DateTime to the current datetime, as reported by the system
     * clock. The chronology used is ISO, in the supplied time zone.
     *
     * @param zone  the time zone, must not be null
     * @throws IllegalArgumentException if the zone is null
     */
    public DateTime(DateTimeZone zone) {
        iChronology = ISOChronology.getInstance(zone);
        iMillis = System.currentTimeMillis();
    }

    /**
     * Constructs a DateTime to the current datetime, as reported by the system
     * clock.
     *
     * @param chronology  the chronology, must not be null
     * @throws IllegalArgumentException if the chronology is null
     */
    public DateTime(Chronology chronology) {
        iChronology = selectChronology(chronology);
        iMillis = System.currentTimeMillis();
    }

    /**
     * Constructs a DateTime set to the milliseconds from 1970-01-01T00:00:00Z,
     * using the ISO chronology, in the
     * {@link DateTimeZone#getDefault() default} time zone.
     *
     * @param millis  the milliseconds
     */
    public DateTime(long millis) {
        iChronology = ISOChronology.getInstance();
        iMillis = millis;
    }

    /**
     * Constructs a DateTime set to the milliseconds from 1970-01-01T00:00:00Z,
     * using the ISO chronology, in the supplied time zone.
     *
     * @param millis  the milliseconds
     * @param zone  the time zone, must not be null
     * @throws IllegalArgumentException if the zone is null
     */
    public DateTime(long millis, DateTimeZone zone) {
        iChronology = ISOChronology.getInstance(zone);
        iMillis = millis;
    }

    /**
     * Constructs a DateTime set to the milliseconds from 1970-01-01T00:00:00Z,
     * using the supplied chronology.
     *
     * @param millis  the milliseconds
     * @param chronology  the chronology, must not be null
     * @throws IllegalArgumentException if the chronology is null
     */
    public DateTime(long millis, Chronology chronology) {
        iChronology = selectChronology(chronology);
        iMillis = millis;
    }

    /**
     * Constructs a DateTime from a ReadableInstant, using its chronology. If
     * its chronology null, then the chronology is set to ISO, in the
     * {@link DateTimeZone#getDefault() default} time zone.
     *
     * @param instant  the ReadableInstant, must not be null
     * @throws IllegalArgumentException if the instant is null
     */
    public DateTime(ReadableInstant instant) {
        iChronology = selectChronology(instant);
        iMillis = instant.getMillis();
    }

    /**
     * Constructs a DateTime from a ReadableInstant, using its chronology
     * against a different time zone. If its chronology is null, then the
     * chronology is set to ISO. If the selected chronology is not in the
     * supplied time zone, a new chronology is created that is.
     *
     * @param instant  the ReadableInstant, must not be null
     * @param zone  the time zone, must not be null
     * @throws IllegalArgumentException if the instant or zone is null
     */
    public DateTime(ReadableInstant instant, DateTimeZone zone) {
        iChronology = selectChronology(instant, zone);
        iMillis = instant.getMillis();
    }

    /**
     * Constructs a DateTime from a ReadableInstant, using the supplied
     * chronology.
     *
     * @param instant  the ReadableInstant, must not be null
     * @param chronology  the chronology, must not be null
     * @throws IllegalArgumentException if the instant or chronology is null
     */
    public DateTime(ReadableInstant instant, Chronology chronology) {
        iChronology = selectChronology(instant, chronology);
        iMillis = instant.getMillis();
    }

    /**
     * Constructs a DateTime from a Date, using the ISO chronology, in the
     * {@link DateTimeZone#getDefault() default} time zone.
     *
     * @param date  the Date, must not be null
     * @throws IllegalArgumentException if the date is null
     */
    public DateTime(Date date) {
        iChronology = selectChronology(date);
        iMillis = date.getTime();
    }

    /**
     * Constructs a DateTime from a Date, using the ISO chronology, in the
     * supplied time zone.
     *
     * @param date  the Date, must not be null
     * @param zone  the time zone, must not be null
     * @throws IllegalArgumentException if the date or zone is null
     */
    public DateTime(Date date, DateTimeZone zone) {
        iChronology = selectChronology(date, zone);
        iMillis = date.getTime();
    }

    /**
     * Constructs a DateTime from a Date, using the supplied chronology.
     *
     * @param date  the Date, must not be null
     * @param chronology  the chronology, must not be null
     * @throws IllegalArgumentException if the date or chronology is null
     */
    public DateTime(Date date, Chronology chronology) {
        iChronology = selectChronology(date, chronology);
        iMillis = date.getTime();
    }

    /**
     * Constructs a DateTime from a Calendar, using its closest mapped
     * chronology and time zone.
     *
     * <p>When converting calendars to chronologies, the constructor is aware
     * of GregorianCalendar and BuddhistCalendar and maps them to the
     * equivalent chronology. Other calendars map to ISOChronology.
     *
     * @param calendar  the Calendar, must not be null
     * @throws IllegalArgumentException if the calendar is null
     */
    public DateTime(Calendar calendar) {
        iChronology = selectChronology(calendar);
        iMillis = calendar.getTime().getTime();
    }

    /**
     * Constructs a DateTime from a Calendar, using its closest mapped
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
    public DateTime(Calendar calendar, DateTimeZone zone) {
        iChronology = selectChronology(calendar, zone);
        iMillis = calendar.getTime().getTime();
    }

    /**
     * Constructs a DateTime from a Calendar, using the supplied chronology.
     *
     * @param calendar  the Calendar, must not be null
     * @param chronology  the chronology, must not be null
     * @throws IllegalArgumentException if the calendar or chronology is null
     */
    public DateTime(Calendar calendar, Chronology chronology) {
        iChronology = selectChronology(calendar, chronology);
        iMillis = calendar.getTime().getTime();
    }

    /**
     * Constructs a DateTime from an ISO formatted String, using the ISO
     * chronology, in the {@link DateTimeZone#getDefault() default} time zone.
     *
     * @param str  the string to parse, must not be null
     * @throws IllegalArgumentException if the string is null
     * @throws ParseException if parsing fails
     */
    public DateTime(String str) throws ParseException {
        iChronology = selectChronology(str);
        DateTimeParser p = ISODateTimeFormat.getInstance(iChronology).dateTimeParser();
        iMillis = p.parseMillis(str);
    }

    /**
     * Constructs a DateTime from an ISO formatted String, using the ISO
     * chronology, in the supplied time zone.
     *
     * @param str  the string to parse, must not be null
     * @param zone the time zone, must not be null
     * @throws IllegalArgumentException if the string or zone is null
     * @throws ParseException if parsing fails
     */
    public DateTime(String str, DateTimeZone zone) throws ParseException {
        iChronology = selectChronology(str, zone);
        DateTimeParser p = ISODateTimeFormat.getInstance(iChronology).dateTimeParser();
        iMillis = p.parseMillis(str);
    }

    /**
     * Constructs a DateTime from an ISO formatted String, using the supplied
     * chronology.
     *
     * @param str  the string to parse, must not be null
     * @param chronology  the chronology, must not be null
     * @throws IllegalArgumentException if the string or chronology is null
     * @throws ParseException if parsing fails
     */
    public DateTime(String str, Chronology chronology) throws ParseException {
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
        if (millis == getMillis() && chrono == getChronology()) {
            return this;
        }
        return new DateTime(millis, chrono);
    }
    
    // Accessors
    //-----------------------------------------------------------------------
    /**
     * Gets the milliseconds of the datetime instant from the Java epoch
     * of 1970-01-01T00:00:00Z.
     * 
     * @return the number of milliseconds since 1970-01-01T00:00:00Z
     */
    public final long getMillis() {
        return iMillis;
    }

    /**
     * Gets the chronology of the datetime.
     * 
     * @return the Chronology that the datetime is using
     */
    public final Chronology getChronology() {
        return iChronology;
    }

    // Date properties
    //-----------------------------------------------------------------------
    /**
     * Get the day of week property.
     * <p>
     * The values for day of week are defined in {@link DateTimeConstants}.
     * 
     * @return the day of week property
     */
    public final DateTimeFieldProperty dayOfWeek() {
        return new DateTimeFieldProperty(this, getChronology().dayOfWeek());
    }

    /**
     * Get the day of month property.
     * 
     * @return the day of month property
     */
    public final DateTimeFieldProperty dayOfMonth() {
        return new DateTimeFieldProperty(this, getChronology().dayOfMonth());
    }

    /**
     * Get the day of year property.
     * 
     * @return the day of year property
     */
    public final DateTimeFieldProperty dayOfYear() {
        return new DateTimeFieldProperty(this, getChronology().dayOfYear());
    }

    /**
     * Get the week of a week based year property.
     * 
     * @return the week of a week based year property
     */
    public final DateTimeFieldProperty weekOfWeekyear() {
        return new DateTimeFieldProperty(this, getChronology().weekOfWeekyear());
    }

    /**
     * Get the year of a week based year property.
     * 
     * @return the year of a week based year property
     */
    public final DateTimeFieldProperty weekyear() {
        return new DateTimeFieldProperty(this, getChronology().weekyear());
    }

    /**
     * Get the month of year property.
     * 
     * @return the month of year property
     */
    public final DateTimeFieldProperty monthOfYear() {
        return new DateTimeFieldProperty(this, getChronology().monthOfYear());
    }

    /**
     * Get the year property.
     * 
     * @return the year property
     */
    public final DateTimeFieldProperty year() {
        return new DateTimeFieldProperty(this, getChronology().year());
    }

    /**
     * Get the year of era property.
     * 
     * @return the year of era property
     */
    public final DateTimeFieldProperty yearOfEra() {
        return new DateTimeFieldProperty(this, getChronology().yearOfEra());
    }

    /**
     * Get the year of century property.
     * 
     * @return the year of era property
     */
    public final DateTimeFieldProperty yearOfCentury() {
        return new DateTimeFieldProperty(this, getChronology().yearOfCentury());
    }

    /**
     * Get the century of era property.
     * 
     * @return the year of era property
     */
    public final DateTimeFieldProperty centuryOfEra() {
        return new DateTimeFieldProperty(this, getChronology().centuryOfEra());
    }

    /**
     * Get the era property.
     * 
     * @return the era property
     */
    public final DateTimeFieldProperty era() {
        return new DateTimeFieldProperty(this, getChronology().era());
    }

    // Time properties
    //-----------------------------------------------------------------------
    /**
     * Get the millis of second property.
     * 
     * @return the millis of second property
     */
    public final DateTimeFieldProperty millisOfSecond() {
        return new DateTimeFieldProperty(this, getChronology().millisOfSecond());
    }

    /**
     * Get the millis of day property.
     * 
     * @return the millis of day property
     */
    public final DateTimeFieldProperty millisOfDay() {
        return new DateTimeFieldProperty(this, getChronology().millisOfDay());
    }

    /**
     * Get the second of minute field property.
     * 
     * @return the second of minute property
     */
    public final DateTimeFieldProperty secondOfMinute() {
        return new DateTimeFieldProperty(this, getChronology().secondOfMinute());
    }

    /**
     * Get the second of day property.
     * 
     * @return the second of day property
     */
    public final DateTimeFieldProperty secondOfDay() {
        return new DateTimeFieldProperty(this, getChronology().secondOfDay());
    }

    /**
     * Get the minute of hour field property.
     * 
     * @return the minute of hour property
     */
    public final DateTimeFieldProperty minuteOfHour() {
        return new DateTimeFieldProperty(this, getChronology().minuteOfHour());
    }

    /**
     * Get the minute of day property.
     * 
     * @return the minute of day property
     */
    public final DateTimeFieldProperty minuteOfDay() {
        return new DateTimeFieldProperty(this, getChronology().minuteOfDay());
    }

    /**
     * Get the hour of day field property.
     * 
     * @return the hour of day property
     */
    public final DateTimeFieldProperty hourOfDay() {
        return new DateTimeFieldProperty(this, getChronology().hourOfDay());
    }

    // Output
    //-----------------------------------------------------------------------
    /**
     * Output the date time in ISO8601 format (yyyy-MM-ddTHH:mm:ss.SSSZ).
     * 
     * @return ISO8601 time formatted string.
     */
    public final String toString() {
        return ISODateTimeFormat.getInstance(getChronology()).dateTime().print(this);
    }

}
