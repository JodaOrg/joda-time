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
import java.util.Locale;

import org.joda.time.chrono.iso.ISOChronology;
import org.joda.time.convert.InstantConverter;
import org.joda.time.convert.ConverterManager;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;

/**
 * AbstractDateTime provides the common behaviour for datetime classes.
 * <p>
 * This class should generally not be used directly by API users.
 * The {@link ReadableDateTime} interface should be used when different 
 * kinds of date/time objects are to be referenced.
 * <p>
 * AbstractDateTime subclasses may be mutable and not thread-safe.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public abstract class AbstractDateTime extends AbstractInstant
    implements ReadableDateTime, Serializable {

    static final long serialVersionUID = 597501475466447837L;

    /**
     * Selects the correct chronology to use for the chronology based constructors
     * on subclasses.
     * 
     * @param chrono  the chronology to use, must not be null
     * @return the passed in Chronology
     */
    private static Chronology selectChronology(Chronology chrono) {
        if (chrono == null) {
            return ISOChronology.getInstance();
        }
        return chrono;
    }

    /** The millis from 1970-01-01T00:00:00Z */
    private long iMillis;
    /** The chronology to use */
    private Chronology iChronology;

    // Constructors
    //-----------------------------------------------------------------------
    /**
     * Constructs an instance set to the current system millisecond time
     * using <code>ISOChronology</code> in the default time zone.
     */
    protected AbstractDateTime() {
        super();
        iChronology = ISOChronology.getInstance();
        iMillis = System.currentTimeMillis();
    }

    /**
     * Constructs an instance set to the current system millisecond time
     * using <code>ISOChronology</code> in the specified time zone.
     * <p>
     * If the specified time zone is null, the default zone is used.
     *
     * @param zone  the time zone, null means default zone
     */
    protected AbstractDateTime(final DateTimeZone zone) {
        super();
        iChronology = ISOChronology.getInstance(zone);
        iMillis = System.currentTimeMillis();
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
    protected AbstractDateTime(final Chronology chronology) {
        super();
        iChronology = selectChronology(chronology);
        iMillis = System.currentTimeMillis();
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance set to the milliseconds from 1970-01-01T00:00:00Z
     * using <code>ISOChronology</code> in the default time zone.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     */
    protected AbstractDateTime(final long instant) {
        super();
        iChronology = ISOChronology.getInstance();
        iMillis = instant;
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
    protected AbstractDateTime(final long instant, final DateTimeZone zone) {
        super();
        iChronology = ISOChronology.getInstance(zone);
        iMillis = instant;
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
    protected AbstractDateTime(final long instant, final Chronology chronology) {
        super();
        iChronology = selectChronology(chronology);
        iMillis = instant;
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance from an Object that represents a datetime.
     * <p>
     * If the object contains no chronology, <code>ISOChronology</code>
     * in the default time zone is used.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  the datetime object, must not be null
     * @throws IllegalArgumentException if the instant is null or invalid
     */
    protected AbstractDateTime(final Object instant) {
        super();
        InstantConverter converter = ConverterManager.getInstance().getInstantConverter(instant);
        iChronology = converter.getChronology(instant);
        iMillis = converter.getInstantMillis(instant);
    }

    /**
     * Constructs an instance from an Object that represents a datetime,
     * forcing the time zone to that specified.
     * <p>
     * If the object contains no chronology, <code>ISOChronology</code> is used.
     * If the specified time zone is null, the default zone is used.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  the datetime object, must not be null
     * @param zone  the time zone, null means default time zone
     * @throws IllegalArgumentException if the instant is null or invalid
     */
    protected AbstractDateTime(final Object instant, final DateTimeZone zone) {
        super();
        InstantConverter converter = ConverterManager.getInstance().getInstantConverter(instant);
        iChronology = converter.getChronology(instant, zone);
        iMillis = converter.getInstantMillis(instant, zone);
    }

    /**
     * Constructs an instance from an Object that represents a datetime,
     * using the specified chronology.
     * <p>
     * If the chronology is null, ISOChronology in the default time zone is used.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  the datetime object, must not be null
     * @param chronology  the chronology, null means ISOChronology in default zone
     * @throws IllegalArgumentException if the instant is null or invalid
     */
    protected AbstractDateTime(final Object instant, final Chronology chronology) {
        super();
        InstantConverter converter = ConverterManager.getInstance().getInstantConverter(instant);
        iChronology = converter.getChronology(instant, chronology);
        iMillis = converter.getInstantMillis(instant, chronology);
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
     * @param millisOfSecond  the milisecond of the second
     */
    protected AbstractDateTime(
            final int year,
            final int monthOfYear,
            final int dayOfMonth,
            final int hourOfDay,
            final int minuteOfHour,
            final int secondOfMinute,
            final int millisOfSecond) {
        super();
        iChronology = ISOChronology.getInstance();
        iMillis = iChronology.getDateTimeMillis(
            year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
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
     * @param millisOfSecond  the milisecond of the second
     * @param zone  the time zone, null means default time zone
     */
    protected AbstractDateTime(
            final int year,
            final int monthOfYear,
            final int dayOfMonth,
            final int hourOfDay,
            final int minuteOfHour,
            final int secondOfMinute,
            final int millisOfSecond,
            final DateTimeZone zone) {
        super();
        iChronology = ISOChronology.getInstance(zone);
        iMillis = iChronology.getDateTimeMillis(
            year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
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
     * @param millisOfSecond  the milisecond of the second
     * @param chronology  the chronology, null means ISOChronology in default zone
     */
    protected AbstractDateTime(
            final int year,
            final int monthOfYear,
            final int dayOfMonth,
            final int hourOfDay,
            final int minuteOfHour,
            final int secondOfMinute,
            final int millisOfSecond,
            final Chronology chronology) {
        super();
        iChronology = selectChronology(chronology);
        iMillis = iChronology.getDateTimeMillis(
            year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
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
     * Gets the milliseconds of the datetime instant from the Java epoch
     * of 1970-01-01T00:00:00Z.
     * 
     * @param base ignored
     * @return the number of milliseconds since 1970-01-01T00:00:00Z
     */
    public final long getMillis(ReadableInstant base) {
        return iMillis;
    }

    /**
     * Gets the milliseconds of the datetime instant from the Java epoch
     * of 1970-01-01T00:00:00Z.
     * 
     * @param base ignored
     * @param zone ignored
     * @return the number of milliseconds since 1970-01-01T00:00:00Z
     */
    public final long getMillis(ReadableInstant base, DateTimeZone zone) {
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

    // Date field access
    //-----------------------------------------------------------------------
    /**
     * Get the era field value.
     * 
     * @return the era
     */
    public final int getEra() {
        return getChronology().era().get(getMillis());
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
     * Get the year field value.
     * 
     * @return the year
     */
    public final int getYear() {
        return getChronology().year().get(getMillis());
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
     * Get the week of weekyear field value.
     * 
     * @return the week of a week based year
     */
    public final int getWeekOfWeekyear() {
        return getChronology().weekOfWeekyear().get(getMillis());
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
     * Get the day of month field value.
     * <p>
     * The values for the day of month are defined in {@link DateTimeConstants}.
     * 
     * @return the day of month
     */
    public final int getDayOfMonth() {
        return getChronology().dayOfMonth().get(getMillis());
    }

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

    // Time field access
    //-----------------------------------------------------------------------
    /**
     * Get the hour of day field value.
     *
     * @return the hour of day
     */
    public final int getHourOfDay() {
        return getChronology().hourOfDay().get(getMillis());
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
     * Get the minute of hour field value.
     *
     * @return the minute of hour
     */
    public final int getMinuteOfHour() {
        return getChronology().minuteOfHour().get(getMillis());
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
     * Get the second of minute field value.
     *
     * @return the second of minute
     */
    public final int getSecondOfMinute() {
        return getChronology().secondOfMinute().get(getMillis());
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
     * Get the millis of second field value.
     *
     * @return the millis of second
     */
    public final int getMillisOfSecond() {
        return getChronology().millisOfSecond().get(getMillis());
    }

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
     * @throws IllegalArgumentException  if pattern is invalid
     * @see  org.joda.time.format.DateTimeFormat
     */
    public String toString(String pattern) throws IllegalArgumentException {
        return DateTimeFormat.getInstance(getChronology()).forPattern(pattern).print(this);
    }

    /**
     * Output the instant using the specified format pattern.
     *
     * @param pattern  the pattern specification
     * @param locale  Locale to use, or default if null
     * @throws IllegalArgumentException  if pattern is invalid
     * @see  org.joda.time.format.DateTimeFormat
     */
    public String toString(String pattern, Locale locale) throws IllegalArgumentException {
        return DateTimeFormat.getInstance(getChronology(), locale).forPattern(pattern).print(this);
    }

    /**
     * Set the value as the number of miliseconds since the epoch,
     * 1970-01-01T00:00:00Z.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     *
     * @param instant  the milliseconds since 1970-01-01T00:00:00Z to set the
     * instant to
     * @throws IllegalArgumentException if the value is invalid
     */
    protected void setMillis(long instant) {
        iMillis = instant;
    }

    /**
     * Set the value from an Object representing an instant.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     * 
     * @param instant  an object representing an instant
     * @throws IllegalArgumentException if the value is invalid
     */
    protected void setMillis(Object instant) {
        // Don't set iMillis directly, as it may provide a backdoor to
        // immutable subclasses.
        if (instant instanceof ReadableInstant) {
            setMillis(((ReadableInstant) instant).getMillis());
        } else {
            InstantConverter converter = ConverterManager.getInstance().getInstantConverter(instant);
            setMillis(converter.getInstantMillis(instant));
        }
    }

    /**
     * Set the chronology of the datetime.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     * 
     * @param chronology  the chronology to use, null means ISOChronology in default zone
     * @throws IllegalArgumentException if the value is invalid
     */
    protected void setChronology(Chronology chronology) {
        iChronology = selectChronology(chronology);
    }

    /**
     * Sets the time zone of the datetime via the chronology. Setting the time
     * zone does not affect the millisecond value of this instant.
     *
     * @param zone  the time zone to use, null means default zone
     * @throws IllegalArgumentException if the value is invalid
     * @see #moveDateTimeZone
     */
    protected void setDateTimeZone(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        if (iChronology.getDateTimeZone() != zone) {
            // Don't set iChronology directly, as it may provide a backdoor to
            // immutable subclasses.
            setChronology(iChronology.withDateTimeZone(zone));
        }
    }

    /**
     * Moves the time zone of the datetime via the chronology. Moving the time
     * zone alters the millisecond value of this instant such that it is
     * relative to the new time zone.
     *
     * @param zone  the time zone to use, null means default zone
     * @throws IllegalArgumentException if the value is invalid
     * @see #setDateTimeZone
     */
    protected void moveDateTimeZone(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        DateTimeZone currentZone = iChronology.getDateTimeZone();
        if (currentZone != zone) {
            long millis = iMillis;
            millis += currentZone.getOffset(millis);
            millis -= zone.getOffsetFromLocal(millis);
            // Don't set iChronology and iMillis directly, as it may provide a
            // backdoor to immutable subclasses.
            setChronology(iChronology.withDateTimeZone(zone));
            setMillis(millis);
        }
    }

}
