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

import org.joda.time.chrono.iso.ISOChronology;
// Import for @link support
import org.joda.time.convert.ConverterManager;
import org.joda.time.format.ISODateTimeFormat;

/*
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
 */

/**
 * TimeOnly is the basic implementation of a time only class supporting
 * chronologies. It holds the time as milliseconds from T00:00:00. The date
 * component and time zone is fixed at 1970-01-01TZ.
 * <p>
 * TimeOnly is thread-safe and immutable, provided that the Chronology is as
 * well. All standard Chronology classes supplied are thread-safe and
 * immutable.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 * @see DateOnly
 * @see DateTime
 */
public class TimeOnly extends AbstractPartialInstant implements Serializable {

    static final long serialVersionUID = -8414446947366046476L;

    // Constructors
    //-----------------------------------------------------------------------
    /**
     * Constructs a TimeOnly to the current time in the default time zone.
     */
    public TimeOnly() {
        super();
    }

    /**
     * Constructs a TimeOnly to the current time in the given time zone.
     *
     * @param zone  the time zone, null means default zone
     */
    public TimeOnly(DateTimeZone zone) {
        super(zone);
    }

    /**
     * Constructs a TimeOnly to the current time in the time zone of the given
     * chronology.
     *
     * @param chronology  the chronology, null means ISOChronology in default zone
     */
    public TimeOnly(Chronology chronology) {
        super(chronology);
    }

    /**
     * Constructs a TimeOnly set to the milliseconds from 1970-01-01T00:00:00Z.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     */
    public TimeOnly(long instant) {
        super(instant);
    }

    /**
     * Constructs a TimeOnly set to the milliseconds from
     * 1970-01-01T00:00:00Z. If the time zone of the given chronology is not
     * null or UTC, then the instant is converted to local time.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @param chronology  the chronology, null means ISOChronology
     */
    public TimeOnly(long instant, Chronology chronology) {
        super(instant, chronology);
    }

    /**
     * Constructs an instance from an Object that represents a time.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  the datetime object, must not be null
     * @throws IllegalArgumentException if the date is null
     */
    public TimeOnly(Object instant) {
        super(instant);
    }

    /**
     * Constructs an instance from an Object that represents a time, using the
     * specified chronology.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  the datetime object, must not be null
     * @param chronology  the chronology, must not be null
     * @throws IllegalArgumentException if the date or chronology is null
     */
    public TimeOnly(Object instant, Chronology chronology) {
        super(instant, chronology);
    }

    /**
     * Constructs an instance from time field values using
     * <code>ISOChronology</code>.
     *
     * @param hourOfDay  the hour of the day
     * @param minuteOfHour  the minute of the hour
     * @param secondOfMinute  the second of the minute
     * @param millisOfSecond  the milisecond of the second
     */
    public TimeOnly(
            final int hourOfDay,
            final int minuteOfHour,
            final int secondOfMinute,
            final int millisOfSecond) {

        super(ISOChronology.getInstanceUTC()
              .getTimeOnlyMillis(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond),
              ISOChronology.getInstanceUTC());
    }

    /**
     * Constructs an instance from time field values
     * using the specified chronology.
     * <p>
     * If the chronology is null, <code>ISOChronology</code>
     * is used.
     *
     * @param hourOfDay  the hour of the day
     * @param minuteOfHour  the minute of the hour
     * @param secondOfMinute  the second of the minute
     * @param millisOfSecond  the milisecond of the second
     * @param chronology  the chronology, null means ISOChronology
     */
    public TimeOnly(
            final int hourOfDay,
            final int minuteOfHour,
            final int secondOfMinute,
            final int millisOfSecond,
            Chronology chronology) {

        super((chronology == null ? (chronology = ISOChronology.getInstanceUTC()) : chronology)
              .getTimeOnlyMillis(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond),
              chronology);
    }

    /**
     * Returns the lower limiting field, null.
     *
     * @return null.
     */
    public final DateTimeField getLowerLimit() {
        return null;
    }

    /**
     * Returns the upper limiting field, dayOfYear.
     *
     * @return dayOfYear field
     */
    public final DateTimeField getUpperLimit() {
        return getChronology().dayOfYear();
    }

    public final boolean isMatchingType(ReadableInstant instant) {
        return instant instanceof TimeOnly;
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

    // TODO: DateTimeFieldProperty cannot be constructed with anything but
    // DateTime.

    // Properties
    //-----------------------------------------------------------------------
    /**
     * Get the hour of day field property
     * 
     * @return the hour of day property
     * /
    public final DateTimeFieldProperty hourOfDay() {
        return new DateTimeFieldProperty(this, getChronology().hourOfDay());
    }

    /**
     * Get the minute of day property
     * 
     * @return the minute of day property
     * /
    public final DateTimeFieldProperty minuteOfDay() {
        return new DateTimeFieldProperty(this, getChronology().minuteOfDay());
    }

    /**
     * Get the minute of hour field property
     * 
     * @return the minute of hour property
     * /
    public final DateTimeFieldProperty minuteOfHour() {
        return new DateTimeFieldProperty(this, getChronology().minuteOfHour());
    }

    /**
     * Get the second of day property
     * 
     * @return the second of day property
     * /
    public final DateTimeFieldProperty secondOfDay() {
        return new DateTimeFieldProperty(this, getChronology().secondOfDay());
    }

    /**
     * Get the second of minute field property
     * 
     * @return the second of minute property
     * /
    public final DateTimeFieldProperty secondOfMinute() {
        return new DateTimeFieldProperty(this, getChronology().secondOfMinute());
    }

    /**
     * Get the millis of day property
     * 
     * @return the millis of day property
     * /
    public final DateTimeFieldProperty millisOfDay() {
        return new DateTimeFieldProperty(this, getChronology().millisOfDay());
    }

    /**
     * Get the millis of second property
     * 
     * @return the millis of second property
     * /
    public final DateTimeFieldProperty millisOfSecond() {
        return new DateTimeFieldProperty(this, getChronology().millisOfSecond());
    }
    */

    // Output
    //-----------------------------------------------------------------------
    /**
     * Output the time in ISO8601 date only format (hh:mm:ss.SSS).
     * 
     * @return ISO8601 date formatted string
     */
    public final String toString() {
        return ISODateTimeFormat.getInstance(getChronology())
            .hourMinuteSecondFraction().print(getMillis());
    }

    protected ReadableInstant create(long millis, Chronology chronology) {
        if (chronology == null) {
            throw new IllegalArgumentException("The Chronology must not be null");
        }
        if (millis == getMillis() && chronology == getChronology()) {
            return this;
        }
        return new TimeOnly(millis, chronology);
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
