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

import org.joda.time.chrono.iso.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.DurationConverter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.property.ReadWritableInstantFieldProperty;

/**
 * MutableTimeOnly is the basic implementation of a modifiable time only class.
 * It holds the time as milliseconds from T00:00:00. The date component and
 * time zone is fixed at 1970-01-01TZ.
 * <p>
 * This class uses a Chronology internally. The Chronology determines how the
 * millisecond instant value is converted into the date time fields.
 * The default Chronology is <code>ISOChronology</code> which is the agreed
 * international standard and compatable with the modern Gregorian calendar.
 *
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
 * <p>
 * MutableTimeOnly is mutable and not thread-safe, unless concurrent threads
 * are not invoking mutator methods.
 *
 * @author Brian S O'Neill
 * @since 1.0
 * @see TimeOnly
 */
public class MutableTimeOnly extends AbstractPartialInstant
    implements ReadWritableInstant, Cloneable, Serializable {

    static final long serialVersionUID = -1438532408790831231L;

    // Constructors
    //-----------------------------------------------------------------------
    /**
     * Constructs a MutableTimeOnly to the current time in the default time zone.
     */
    public MutableTimeOnly() {
        super();
    }

    /**
     * Constructs a MutableTimeOnly to the current time in the given time zone.
     *
     * @param zone  the time zone, null means default zone
     */
    public MutableTimeOnly(DateTimeZone zone) {
        super(zone);
    }

    /**
     * Constructs a MutableTimeOnly to the current time in the time zone of the given
     * chronology.
     *
     * @param chronology  the chronology, null means ISOChronology in default zone
     */
    public MutableTimeOnly(Chronology chronology) {
        super(chronology);
    }

    /**
     * Constructs a MutableTimeOnly set to the milliseconds from 1970-01-01T00:00:00Z.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     */
    public MutableTimeOnly(long instant) {
        super(instant);
    }

    /**
     * Constructs a MutableTimeOnly set to the milliseconds from
     * 1970-01-01T00:00:00Z. If the time zone of the given chronology is not
     * null or UTC, then the instant is converted to local time.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @param chronology  the chronology, null means ISOChronology
     */
    public MutableTimeOnly(long instant, Chronology chronology) {
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
    public MutableTimeOnly(Object instant) {
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
    public MutableTimeOnly(Object instant, Chronology chronology) {
        super(instant, chronology);
    }

    /**
     * Constructs an instance from time field values using
     * <code>ISOChronology</code>.
     *
     * @param hourOfDay  the hour of the day
     * @param minuteOfHour  the minute of the hour
     * @param secondOfMinute  the second of the minute
     * @param millisOfSecond  the millisecond of the second
     */
    public MutableTimeOnly(
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
     * @param millisOfSecond  the millisecond of the second
     * @param chronology  the chronology, null means ISOChronology
     */
    public MutableTimeOnly(
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
     * Gets a copy of this instant with different millis.
     * <p>
     * The returned object will be a new instance of the same implementation type.
     * Only the millis will change, the chronology is kept.
     * Immutable subclasses may return <code>this</code> if appropriate.
     *
     * @param newMillis  the new millis, from 1970-01-01T00:00:00Z
     * @return a copy of this instant with different millis
     */
    public ReadableInstant toCopy(long newMillis) {
        return new MutableTimeOnly(newMillis, getChronology());
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
     * @throws IllegalArgumentException if the chronology is null
     */
    public ReadableInstant toCopy(Chronology newChronology) {
        if (newChronology == null) {
            throw new IllegalArgumentException("The Chronology must not be null");
        }
        return new MutableTimeOnly(getMillis(), newChronology);
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

    /**
     * Set the value as the number of milliseconds since the epoch,
     * 1970-01-01T00:00:00Z.
     *
     * @param instant  the milliseconds since 1970-01-01T00:00:00Z to set the
     * instant to
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setMillis(long instant) {
        super.setMillis(instant);
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
    public void setMillis(Object instant) {
        super.setMillis(instant);
    }

    /**
     * Set the chronology of the instant.
     * 
     * @param chronology  the chronology to use, null means ISOChronology/UTC
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setChronology(Chronology chronology) {
        super.setChronology(chronology);
    }
    
    /**
     * Since time zones are ignored, this method does nothing.
     *
     * @param zone  ignored
     */
    public void setDateTimeZone(DateTimeZone zone) {
    }

    /**
     * Since time zones are ignored, this method does nothing.
     *
     * @param zone  ignored
     */
    public void moveDateTimeZone(DateTimeZone zone) {
    }

    // Add
    //-----------------------------------------------------------------------
    /**
     * Add an amount of time to the time.
     * 
     * @param duration  the millis to add
     */
    public void add(final long duration) {
        setMillis(getMillis() + duration);
    }

    /**
     * Add an amount of time to the time.
     * 
     * @param duration  duration to add.
     */
    public void add(final ReadableDuration duration) {
        duration.addInto(this, 1);
    }

    /**
     * Add an amount of time to the time.
     * 
     * @param duration  duration to add.
     * @param scalar  direction and amount to add, which may be negative
     */
    public void add(final ReadableDuration duration, final int scalar) {
        duration.addInto(this, scalar);
    }

    /**
     * Add an amount of time to the time.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableDuration, and Long.
     * 
     * @param duration  an object representing a duration
     */
    public void add(final Object duration) {
        if (duration instanceof ReadableDuration) {
            add((ReadableDuration) duration, 1);
        } else {
            DurationConverter converter = ConverterManager.getInstance().getDurationConverter(duration);
            add(converter.getDurationMillis(duration));
        }
    }

    // Field based
    //-----------------------------------------------------------------------
    /**
     * Set a value in the specified field.
     * This could be used to set a field using a different Chronology.
     * For example:
     * <pre>
     * MutableTimeOnly time = new MutableTimeOnly();
     * time.set(GJChronology.getInstance().hourOfDay(), 12);
     * </pre>
     * 
     * @param field  the DateTimeField to use
     * @param value the value
     * @throws NullPointerException if the field is null
     */
    public void set(final DateTimeField field, final int value) {
        setMillis(field.set(getMillis(), value));
    }

    /**
     * Add a value to the specified field.
     * This could be used to set a field using a different Chronology.
     * For example:
     * <pre>
     * MutableTimeOnly time = new MutableTimeOnly();
     * time.add(GJChronology.getInstance().hourOfDay(), 2);
     * </pre>
     * 
     * @param field  the DateTimeField to use
     * @param value the value
     * @throws NullPointerException if the field is null
     */
    public void add(final DateTimeField field, final int value) {
        setMillis(field.add(getMillis(), value));
    }

    /**
     * Add a value to the specified field, wrapping within that field.
     * This could be used to set a field using a different Chronology.
     * For example:
     * <pre>
     * MutableTimeOnly time = new MutableTimeOnly();
     * time.addWrapped(GJChronology.getInstance().minuteOfHour(), 30);
     * </pre>
     * 
     * @param field  the DateTimeField to use
     * @param value the value
     * @throws NullPointerException if the field is null
     */
    public void addWrapped(final DateTimeField field, final int value) {
        setMillis(field.addWrapped(getMillis(), value));
    }

    // Time methods
    //-----------------------------------------------------------------------
    /**
     * Set the hour of the day to the specified value.
     *
     * @param hourOfDay  the hour of day
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setHourOfDay(final int hourOfDay) {
        setMillis(getChronology().hourOfDay().set(getMillis(), hourOfDay));
    }

    /**
     * Set the hour of day (offset to 1-24) to the specified value.
     * 
     * @param clockhourOfDay  the clockhour of day
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setClockhourOfDay(final int clockhourOfDay) {
        setMillis(getChronology().clockhourOfDay().set(getMillis(), clockhourOfDay));
    }

    /**
     * Set the hour of am/pm (0-11) to the specified value.
     * 
     * @param hourOfHalfday  the hour of halfday
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setHourOfHalfday(final int hourOfHalfday) {
        setMillis(getChronology().hourOfHalfday().set(getMillis(), hourOfHalfday));
    }

    /**
     * Set the hour of am/pm (offset to 1-12) to the specified value.
     * 
     * @param clockhourOfHalfday  the clockhour of halfday
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setClockhourOfHalfday(final int clockhourOfHalfday) {
        setMillis(getChronology().clockhourOfHalfday().set(getMillis(), clockhourOfHalfday));
    }

    /**
     * Set the AM(0) PM(1) field value.
     * 
     * @param halfdayOfDay  the halfday of day
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setHalfdayOfDay(final int halfdayOfDay) {
        setMillis(getChronology().halfdayOfDay().set(getMillis(), halfdayOfDay));
    }

    /**
     * Add a number of hours to the date.
     *
     * @param hours  the hours to add
     * @throws IllegalArgumentException if the value is invalid
     */
    public void addHours(final int hours) {
        setMillis(getChronology().hours().add(getMillis(), hours));
    }
    
    //-----------------------------------------------------------------------
    /**
     * Set the minute of the day to the specified value.
     *
     * @param minuteOfDay  the minute of day
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setMinuteOfDay(final int minuteOfDay) {
        setMillis(getChronology().minuteOfDay().set(getMillis(), minuteOfDay));
    }

    /**
     * Set the minute of the hour to the specified value.
     *
     * @param minuteOfHour  the minute of hour
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setMinuteOfHour(final int minuteOfHour) {
        setMillis(getChronology().minuteOfHour().set(getMillis(), minuteOfHour));
    }

    /**
     * Add a number of minutes to the date.
     *
     * @param minutes  the minutes to add
     * @throws IllegalArgumentException if the value is invalid
     */
    public void addMinutes(final int minutes) {
        setMillis(getChronology().minutes().add(getMillis(), minutes));
    }

    //-----------------------------------------------------------------------
    /**
     * Set the second of the day to the specified value.
     *
     * @param secondOfDay  the second of day
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setSecondOfDay(final int secondOfDay) {
        setMillis(getChronology().secondOfDay().set(getMillis(), secondOfDay));
    }

    /**
     * Set the second of the minute to the specified value.
     *
     * @param secondOfMinute  the second of minute
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setSecondOfMinute(final int secondOfMinute) {
        setMillis(getChronology().secondOfMinute().set(getMillis(), secondOfMinute));
    }

    /**
     * Add a number of seconds to the date.
     *
     * @param seconds  the seconds to add
     * @throws IllegalArgumentException if the value is invalid
     */
    public void addSeconds(final int seconds) {
        setMillis(getChronology().seconds().add(getMillis(), seconds));
    }

    //-----------------------------------------------------------------------
    /**
     * Set the millis of the day to the specified value.
     *
     * @param millisOfDay  the millis of day
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setMillisOfDay(final int millisOfDay) {
        setMillis(getChronology().millisOfDay().set(getMillis(), millisOfDay));
    }

    /**
     * Set the millis of the second to the specified value.
     *
     * @param millisOfSecond  the millis of second
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setMillisOfSecond(final int millisOfSecond) {
        setMillis(getChronology().millisOfSecond().set(getMillis(), millisOfSecond));
    }

    /**
     * Add a number of milliseconds to the date. The implementation of this
     * method differs from the {@link #add(long)} method in that a
     * DateTimeField performs the addition.
     *
     * @param millis  the milliseconds to add
     * @throws IllegalArgumentException if the value is invalid
     */
    public void addMillis(final int millis) {
        setMillis(getChronology().millis().add(getMillis(), millis));
    }

    // Time field access
    //-----------------------------------------------------------------------
    /**
     * Get the hour of day (0-23) field value.
     *
     * @return the hour of day
     */
    public final int getHourOfDay() {
        return getChronology().hourOfDay().get(getMillis());
    }

    /**
     * Get the hour of day (offset to 1-24) field value.
     * 
     * @return the clockhour of day
     */
    public final int getClockhourOfDay() {
        return getChronology().clockhourOfDay().get(getMillis());
    }

    /**
     * Get the hour of am/pm (0-11) field value.
     * 
     * @return the hour of halfday
     */
    public final int getHourOfHalfday() {
        return getChronology().hourOfHalfday().get(getMillis());
    }

    /**
     * Get the hour of am/pm (offset to 1-12) field value.
     * 
     * @return the clockhour of halfday
     */
    public final int getClockhourOfHalfday() {
        return getChronology().clockhourOfHalfday().get(getMillis());
    }

    /**
     * Get the AM(0) PM(1) field value.
     * 
     * @return the halfday of day
     */
    public final int getHalfdayOfDay() {
        return getChronology().halfdayOfDay().get(getMillis());
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

    // Setters
    //-----------------------------------------------------------------------
    /**
     * Set the time from milliseconds.
     *
     * @param millis milliseconds from T00:00:00Z, date part ignored
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setTime(final long millis) {
        setMillis(millis);
    }

    /**
     * Set the date from an object representing an instant.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  an object representing an instant, date part ignored
     * @throws IllegalArgumentException if the object is null or invalid
     */
    public void setTime(final Object instant) {
        setMillis(instant);
    }

    /**
     * Set the time from fields.
     *
     * @param hour  the hour
     * @param minuteOfHour  the minute of the hour
     * @param secondOfMinute  the second of the minute
     * @param millisOfSecond  the millisecond of the second
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setTime(
            final int hour,
            final int minuteOfHour,
            final int secondOfMinute,
            final int millisOfSecond) {
        setMillis(getChronology().getTimeOnlyMillis(hour, minuteOfHour, secondOfMinute, millisOfSecond));
    }

    // Properties
    //-----------------------------------------------------------------------
    /**
     * Get the hour of day (0-23) field property
     * 
     * @return the hour of day property
     */
    public final ReadWritableInstantFieldProperty hourOfDay() {
        return new ReadWritableInstantFieldProperty(this, getChronology().hourOfDay());
    }

    /**
     * Get the hour of day (offset to 1-24) field property
     * 
     * @return the clockhour of day property
     */
    public final ReadWritableInstantFieldProperty clockhourOfDay() {
        return new ReadWritableInstantFieldProperty(this, getChronology().clockhourOfDay());
    }

    /**
     * Get the hour of am/pm (0-11) field property
     * 
     * @return the hour of halfday property
     */
    public final ReadWritableInstantFieldProperty hourOfHalfday() {
        return new ReadWritableInstantFieldProperty(this, getChronology().hourOfHalfday());
    }

    /**
     * Get the hour of am/pm (offset to 1-12) field property
     * 
     * @return the clockhour of halfday property
     */
    public final ReadWritableInstantFieldProperty clockhourOfHalfday() {
        return new ReadWritableInstantFieldProperty(this, getChronology().clockhourOfHalfday());
    }

    /**
     * Get the AM(0) PM(1) field property
     * 
     * @return the halfday of day property
     */
    public final ReadWritableInstantFieldProperty halfdayOfDay() {
        return new ReadWritableInstantFieldProperty(this, getChronology().halfdayOfDay());
    }

    /**
     * Get the minute of day property
     * 
     * @return the minute of day property
     */
    public final ReadWritableInstantFieldProperty minuteOfDay() {
        return new ReadWritableInstantFieldProperty(this, getChronology().minuteOfDay());
    }

    /**
     * Get the minute of hour field property
     * 
     * @return the minute of hour property
     */
    public final ReadWritableInstantFieldProperty minuteOfHour() {
        return new ReadWritableInstantFieldProperty(this, getChronology().minuteOfHour());
    }

    /**
     * Get the second of day property
     * 
     * @return the second of day property
     */
    public final ReadWritableInstantFieldProperty secondOfDay() {
        return new ReadWritableInstantFieldProperty(this, getChronology().secondOfDay());
    }

    /**
     * Get the second of minute field property
     * 
     * @return the second of minute property
     */
    public final ReadWritableInstantFieldProperty secondOfMinute() {
        return new ReadWritableInstantFieldProperty(this, getChronology().secondOfMinute());
    }

    /**
     * Get the millis of day property
     * 
     * @return the millis of day property
     */
    public final ReadWritableInstantFieldProperty millisOfDay() {
        return new ReadWritableInstantFieldProperty(this, getChronology().millisOfDay());
    }

    /**
     * Get the millis of second property
     * 
     * @return the millis of second property
     */
    public final ReadWritableInstantFieldProperty millisOfSecond() {
        return new ReadWritableInstantFieldProperty(this, getChronology().millisOfSecond());
    }

    // Misc
    //-----------------------------------------------------------------------
    /**
     * Clone this object without having to cast the returned object.
     *
     * @return a clone of the this object.
     */
    public MutableTimeOnly copy() {
        return (MutableTimeOnly)clone();
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

}
