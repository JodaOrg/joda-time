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

import java.io.Serializable;

import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.property.DateTimeFieldProperty;

/**
 * DateTime is the standard implementation of an unmodifiable datetime class.
 * It holds the datetime as milliseconds from the Java epoch of 1970-01-01T00:00:00Z.
 * <p>
 * This class uses a Chronology internally. The Chronology determines how the
 * millisecond instant value is converted into the date time fields.
 * The default Chronology is <code>ISOChronology</code> which is the agreed
 * international standard and compatable with the modern Gregorian calendar.
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
 * <p>
 * DateTime is thread-safe and immutable, provided that the Chronology is as well.
 * All standard Chronology classes supplied are thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @author Kandarp Shah
 * @author Brian S O'Neill
 * @since 1.0
 * @see MutableDateTime
 * @see DateOnly
 * @see TimeOnly
 */
public class DateTime extends AbstractDateTime
        implements ReadableDateTime, Serializable {
    
    /** Serialization lock */
    private static final long serialVersionUID = -5171125899451703815L;

    // Constructors
    //-----------------------------------------------------------------------
    /**
     * Constructs an instance set to the current system millisecond time
     * using <code>ISOChronology</code> in the default time zone.
     */
    public DateTime() {
        super();
    }

    /**
     * Constructs an instance set to the current system millisecond time
     * using <code>ISOChronology</code> in the specified time zone.
     * <p>
     * If the specified time zone is null, the default zone is used.
     *
     * @param zone  the time zone, null means default zone
     */
    public DateTime(DateTimeZone zone) {
        super(zone);
    }

    /**
     * Constructs an instance set to the current system millisecond time
     * using the specified chronology.
     * <p>
     * If the chronology is null, <code>ISOChronology</code>
     * in the default time zone is used.
     *
     * @param chronology  the chronology, null means ISOChronology in default zone
     */
    public DateTime(Chronology chronology) {
        super(chronology);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance set to the milliseconds from 1970-01-01T00:00:00Z
     * using <code>ISOChronology</code> in the default time zone.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     */
    public DateTime(long instant) {
        super(instant);
    }

    /**
     * Constructs an instance set to the milliseconds from 1970-01-01T00:00:00Z
     * using <code>ISOChronology</code> in the specified time zone.
     * <p>
     * If the specified time zone is null, the default zone is used.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @param zone  the time zone, null means default zone
     */
    public DateTime(long instant, DateTimeZone zone) {
        super(instant, zone);
    }

    /**
     * Constructs an instance set to the milliseconds from 1970-01-01T00:00:00Z
     * using the specified chronology.
     * <p>
     * If the chronology is null, <code>ISOChronology</code>
     * in the default time zone is used.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @param chronology  the chronology, null means ISOChronology in default zone
     */
    public DateTime(long instant, Chronology chronology) {
        super(instant, chronology);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance from an Object that represents a datetime.
     * <p>
     * If the object contains no chronology, <code>ISOChronology</code>
     * in the default time zone is used.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  the datetime object, must not be null
     * @throws IllegalArgumentException if the instant is null or invalid
     */
    public DateTime(Object instant) {
        super(instant);
    }

    /**
     * Constructs an instance from an Object that represents a datetime,
     * forcing the time zone to that specified.
     * <p>
     * If the object contains no chronology, <code>ISOChronology</code> is used.
     * If the specified time zone is null, the default zone is used.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  the datetime object, must not be null
     * @param zone  the time zone, null means default time zone
     * @throws IllegalArgumentException if the instant is null or invalid
     */
    public DateTime(Object instant, DateTimeZone zone) {
        super(instant, zone);
    }

    /**
     * Constructs an instance from an Object that represents a datetime,
     * using the specified chronology.
     * <p>
     * If the chronology is null, ISOChronology in the default time zone is used.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  the datetime object, must not be null
     * @param chronology  the chronology, null means ISOChronology in default zone
     * @throws IllegalArgumentException if the instant is null or invalid
     */
    public DateTime(Object instant, Chronology chronology) {
        super(instant, chronology);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance from datetime field values
     * using <code>ISOChronology</code> in the default time zone.
     *
     * @param year  the year
     * @param monthOfYear  the month of the year
     * @param dayOfMonth  the day of the month
     * @param hourOfDay  the hour of the day
     * @param minuteOfHour  the minute of the hour
     * @param secondOfMinute  the second of the minute
     * @param millisOfSecond  the millisecond of the second
     */
    public DateTime(
            int year,
            int monthOfYear,
            int dayOfMonth,
            int hourOfDay,
            int minuteOfHour,
            int secondOfMinute,
            int millisOfSecond) {
        super(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    /**
     * Constructs an instance from datetime field values
     * using <code>ISOChronology</code> in the specified time zone.
     * <p>
     * If the specified time zone is null, the default zone is used.
     *
     * @param year  the year
     * @param monthOfYear  the month of the year
     * @param dayOfMonth  the day of the month
     * @param hourOfDay  the hour of the day
     * @param minuteOfHour  the minute of the hour
     * @param secondOfMinute  the second of the minute
     * @param millisOfSecond  the millisecond of the second
     * @param zone  the time zone, null means default time zone
     */
    public DateTime(
            int year,
            int monthOfYear,
            int dayOfMonth,
            int hourOfDay,
            int minuteOfHour,
            int secondOfMinute,
            int millisOfSecond,
            DateTimeZone zone) {
        super(year, monthOfYear, dayOfMonth,
              hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond, zone);
    }

    /**
     * Constructs an instance from datetime field values
     * using the specified chronology.
     * <p>
     * If the chronology is null, <code>ISOChronology</code>
     * in the default time zone is used.
     *
     * @param year  the year
     * @param monthOfYear  the month of the year
     * @param dayOfMonth  the day of the month
     * @param hourOfDay  the hour of the day
     * @param minuteOfHour  the minute of the hour
     * @param secondOfMinute  the second of the minute
     * @param millisOfSecond  the millisecond of the second
     * @param chronology  the chronology, null means ISOChronology in default zone
     */
    public DateTime(
            int year,
            int monthOfYear,
            int dayOfMonth,
            int hourOfDay,
            int minuteOfHour,
            int secondOfMinute,
            int millisOfSecond,
            Chronology chronology) {
        super(year, monthOfYear, dayOfMonth,
              hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond, chronology);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this instant with different millis.
     * <p>
     * The returned object will be a new instance of the same implementation type.
     * Only the millis will change, the chronology and time zone are kept.
     * Immutable subclasses may return <code>this</code> if appropriate.
     *
     * @param newMillis  the new millis, from 1970-01-01T00:00:00Z
     * @return a copy of this instant with different millis
     */
    public final DateTime withMillis(long newMillis) {
        return (newMillis == getMillis() ? this : new DateTime(newMillis, getChronology()));
    }

    /**
     * Gets a copy of this instant with a different chronology.
     * <p>
     * The returned object will be a new instance of the same implementation type.
     * Only the chronology will change, the millis are kept.
     * Immutable subclasses may return <code>this</code> if appropriate.
     *
     * @param newChronology  the new chronology
     * @return a copy of this instant with a different chronology
     */
    public final DateTime withChronology(Chronology newChronology) {
        return (newChronology == getChronology() ? this : new DateTime(getMillis(), newChronology));
    }

    /**
     * Gets a copy of this instant with a different time zone, preserving the
     * millisecond instant.
     * <p>
     * This method is useful for finding the local time in another timezone.
     * For example, if this instant holds 12:30 in Europe/London, the result
     * from this method with Europe/Paris would be 13:30.
     * <p>
     * The returned object will be a new instance of the same implementation type.
     * This method changes alters the time zone, and does not change the
     * millisecond instant, with the effect that the field values usually change.
     * Immutable implementations may return <code>this</code> if appropriate.
     *
     * @param newDateTimeZone  the new time zone
     * @return a copy of this instant with a different time zone
     * @see #withZoneRetainFields
     */
    public final DateTime withZone(DateTimeZone newDateTimeZone) {
        return withChronology(getChronology().withZone(newDateTimeZone));
    }

    /**
     * Gets a copy of this instant with a different time zone, preserving the
     * field values.
     * <p>
     * This method is useful for finding the millisecond time in another timezone.
     * For example, if this instant holds 12:30 in Europe/London (ie. 12:30Z),
     * the result from this method with Europe/Paris would be 12:30 (ie. 11:30Z).
     * <p>
     * The returned object will be a new instance of the same implementation type.
     * This method changes alters the time zone and the millisecond instant to keep
     * the field values the same.
     * Immutable implementations may return <code>this</code> if appropriate.
     *
     * @param newDateTimeZone  the new time zone
     * @return a copy of this instant with a different time zone
     * @see #withZone
     */
    public final DateTime withZoneRetainFields(DateTimeZone newDateTimeZone) {
        final long originalMillis = getMillis();
        final Chronology originalChrono = getChronology();
        final DateTimeZone originalZone;
        if (originalChrono == null || (originalZone = originalChrono.getZone()) == null) {
            // Without an original chronology or time zone, no new time zone
            // can be set. Call withMillis to allow subclass to decide if a
            // clone should be made or not.
            return withMillis(originalMillis);
        }

        DateTime newInstant = withChronology(originalChrono.withZone(newDateTimeZone));
        newDateTimeZone = newInstant.getZone();

        if (newDateTimeZone == null || newDateTimeZone == originalZone) {
            // New time zone didn't stick or didn't change. Skip millis adjustment.
            return newInstant;
        }

        long newMillis = originalMillis + originalZone.getOffset(originalMillis);
        newMillis -= newDateTimeZone.getOffsetFromLocal(newMillis);

        return newInstant.withMillis(newMillis);
    }

    // Date properties
    //-----------------------------------------------------------------------
    /**
     * Get the era property.
     * 
     * @return the era property
     */
    public final DateTimeFieldProperty era() {
        return new DateTimeFieldProperty(this, getChronology().era());
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
     * Get the year of century property.
     * 
     * @return the year of era property
     */
    public final DateTimeFieldProperty yearOfCentury() {
        return new DateTimeFieldProperty(this, getChronology().yearOfCentury());
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
     * Get the year property.
     * 
     * @return the year property
     */
    public final DateTimeFieldProperty year() {
        return new DateTimeFieldProperty(this, getChronology().year());
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
     * Get the week of a week based year property.
     * 
     * @return the week of a week based year property
     */
    public final DateTimeFieldProperty weekOfWeekyear() {
        return new DateTimeFieldProperty(this, getChronology().weekOfWeekyear());
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
     * Get the day of month property.
     * <p>
     * The values for day of month are defined in {@link DateTimeConstants}.
     * 
     * @return the day of month property
     */
    public final DateTimeFieldProperty dayOfMonth() {
        return new DateTimeFieldProperty(this, getChronology().dayOfMonth());
    }

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

    // Time properties
    //-----------------------------------------------------------------------
    /**
     * Get the hour of day field property
     * 
     * @return the hour of day property
     */
    public final DateTimeFieldProperty hourOfDay() {
        return new DateTimeFieldProperty(this, getChronology().hourOfDay());
    }

    /**
     * Get the minute of day property
     * 
     * @return the minute of day property
     */
    public final DateTimeFieldProperty minuteOfDay() {
        return new DateTimeFieldProperty(this, getChronology().minuteOfDay());
    }

    /**
     * Get the minute of hour field property
     * 
     * @return the minute of hour property
     */
    public final DateTimeFieldProperty minuteOfHour() {
        return new DateTimeFieldProperty(this, getChronology().minuteOfHour());
    }

    /**
     * Get the second of day property
     * 
     * @return the second of day property
     */
    public final DateTimeFieldProperty secondOfDay() {
        return new DateTimeFieldProperty(this, getChronology().secondOfDay());
    }

    /**
     * Get the second of minute field property
     * 
     * @return the second of minute property
     */
    public final DateTimeFieldProperty secondOfMinute() {
        return new DateTimeFieldProperty(this, getChronology().secondOfMinute());
    }

    /**
     * Get the millis of day property
     * 
     * @return the millis of day property
     */
    public final DateTimeFieldProperty millisOfDay() {
        return new DateTimeFieldProperty(this, getChronology().millisOfDay());
    }

    /**
     * Get the millis of second property
     * 
     * @return the millis of second property
     */
    public final DateTimeFieldProperty millisOfSecond() {
        return new DateTimeFieldProperty(this, getChronology().millisOfSecond());
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

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are
     * immutable.
     */
    protected final void setMillis(long millis) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are
     * immutable.
     */
    protected final void setChronology(Chronology chronology) {
    }

}
