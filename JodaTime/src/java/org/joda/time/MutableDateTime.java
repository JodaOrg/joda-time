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

import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.DurationConverter;
import org.joda.time.convert.InstantConverter;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.property.ReadWritableInstantFieldProperty;

/**
 * MutableDateTime is the standard implementation of a modifiable datetime class.
 * It holds the datetime as milliseconds from the Java epoch of 1970-01-01T00:00:00Z.
 * <p>
 * This class uses a Chronology internally. The Chronology determines how the
 * millisecond instant value is converted into the date time fields.
 * The default Chronology is <code>ISOChronology</code> which is the agreed
 * international standard and compatable with the modern Gregorian calendar.
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
 * MutableDateTime is mutable and not thread-safe, unless concurrent threads
 * are not invoking mutator methods.
 *
 * @author Guy Allard
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 * @see DateTime
 */
public class MutableDateTime extends AbstractDateTime
        implements ReadWritableDateTime, Cloneable, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 2852608688135209575L;

    /** Rounding is disabled */
    public static final int ROUND_NONE = 0;
    /** Rounding mode as described by {@link DateTimeField#roundFloor} */
    public static final int ROUND_FLOOR = 1;
    /** Rounding mode as described by {@link DateTimeField#roundCeiling} */
    public static final int ROUND_CEILING = 2;
    /** Rounding mode as described by {@link DateTimeField#roundHalfFloor} */
    public static final int ROUND_HALF_FLOOR = 3;
    /** Rounding mode as described by {@link DateTimeField#roundHalfCeiling} */
    public static final int ROUND_HALF_CEILING = 4;
    /** Rounding mode as described by {@link DateTimeField#roundHalfEven} */
    public static final int ROUND_HALF_EVEN = 5;

    /** The field to round on */
    private DateTimeField iRoundingField;
    /** The mode of rounding */
    private int iRoundingMode;

    // Constructors
    //-----------------------------------------------------------------------
    /**
     * Constructs an instance set to the current system millisecond time
     * using <code>ISOChronology</code> in the default time zone.
     */
    public MutableDateTime() {
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
    public MutableDateTime(DateTimeZone zone) {
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
    public MutableDateTime(Chronology chronology) {
        super(chronology);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance set to the milliseconds from 1970-01-01T00:00:00Z
     * using <code>ISOChronology</code> in the default time zone.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     */
    public MutableDateTime(long instant) {
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
    public MutableDateTime(long instant, DateTimeZone zone) {
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
    public MutableDateTime(long instant, Chronology chronology) {
        super(instant, chronology);
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
     * @throws IllegalArgumentException if the instant is invalid
     */
    public MutableDateTime(Object instant) {
        super(instant);
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
     * @throws IllegalArgumentException if the instant is invalid
     */
    public MutableDateTime(Object instant, DateTimeZone zone) {
        super(instant, zone);
    }

    /**
     * Constructs an instance from an Object that represents a datetime,
     * using the specifed chronology.
     * <p>
     * If the chronology is null, ISOChronology in the default time zone is used.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  the datetime object, must not be null
     * @param chronology  the chronology, null means ISOChronology in default zone
     * @throws IllegalArgumentException if the instant is invalid
     */
    public MutableDateTime(Object instant, Chronology chronology) {
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
    public MutableDateTime(
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
    public MutableDateTime(
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
    public MutableDateTime(
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
    
    /**
     * Gets the field used for rounding this instant, returning null if rounding
     * is not enabled.
     * 
     * @return the rounding field
     */
    public DateTimeField getRoundingField() {
        return iRoundingField;
    }

    /**
     * Gets the rounding mode for this instant, returning ROUND_NONE if rounding
     * is not enabled.
     * 
     * @return the rounding mode constant
     */
    public int getRoundingMode() {
        return iRoundingMode;
    }

    /**
     * Sets the status of rounding to use the specified field and ROUND_FLOOR mode.
     * A null field will disable rounding.
     * Once set, the instant is then rounded using the new field and mode.
     * <p>
     * Enabling rounding will cause all subsequent calls to {@link #setMillis(long)}
     * to be rounded. This can be used to control the precision of the instant,
     * for example by setting a rounding field of minuteOfDay, the seconds and
     * milliseconds will always be zero.
     *
     * @param field rounding field or null to disable
     */
    public void setRounding(DateTimeField field) {
        setRounding(field, MutableDateTime.ROUND_FLOOR);
    }

    /**
     * Sets the status of rounding to use the specified field and mode.
     * A null field or mode of ROUND_NONE will disable rounding.
     * Once set, the instant is then rounded using the new field and mode.
     * <p>
     * Enabling rounding will cause all subsequent calls to {@link #setMillis(long)}
     * to be rounded. This can be used to control the precision of the instant,
     * for example by setting a rounding field of minuteOfDay, the seconds and
     * milliseconds will always be zero.
     *
     * @param field  rounding field or null to disable
     * @param mode  rounding mode or ROUND_NONE to disable
     * @throws IllegalArgumentException if mode is unknown, no exception if field is null
     */
    public void setRounding(DateTimeField field, int mode) {
        if (field != null && (mode < ROUND_NONE || mode > ROUND_HALF_EVEN)) {
            throw new IllegalArgumentException("Illegal rounding mode: " + mode);
        }
        iRoundingField = (mode == ROUND_NONE ? null : field);
        iRoundingMode = (field == null ? ROUND_NONE : mode);
        setMillis(getMillis());
    }

    // Millis
    //-----------------------------------------------------------------------
    /**
     * Set the milliseconds of the datetime.
     *
     * @param instant  the milliseconds since 1970-01-01T00:00:00Z to set the
     * datetime to
     * @see #setDateTime(long)
     */
    public void setMillis(long instant) {
        switch (iRoundingMode) {
        case ROUND_NONE:
            break;
        case ROUND_FLOOR:
            instant = iRoundingField.roundFloor(instant);
            break;
        case ROUND_CEILING:
            instant = iRoundingField.roundCeiling(instant);
            break;
        case ROUND_HALF_FLOOR:
            instant = iRoundingField.roundHalfFloor(instant);
            break;
        case ROUND_HALF_CEILING:
            instant = iRoundingField.roundHalfCeiling(instant);
            break;
        case ROUND_HALF_EVEN:
            instant = iRoundingField.roundHalfEven(instant);
            break;
        }

        super.setMillis(instant);
    }

    /**
     * Set the date and time from an object representing an instant.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  an object representing an instant
     * @throws IllegalArgumentException if the object is invalid
     * @see #setDateTime(Object)
     */
    public void setMillis(Object instant) {
        super.setMillis(instant);
    }

    // Add
    //-----------------------------------------------------------------------
    /**
     * Add an amount of time to the datetime.
     * 
     * @param duration  the millis to add
     */
    public void add(final long duration) {
        setMillis(getMillis() + duration);
    }

    /**
     * Adds an amount of time to this instant.
     * <p>
     * If the resulting value is too large for the implementation,
     * an exception is thrown.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableDuration, String and Long.
     *
     * @param duration  an object representing a duration
     * @throws IllegalArgumentException if the duration is invalid
     * @throws ArithmeticException if the result exceeds the capacity of the instant
     */
    public void add(final Object duration) {
        add(duration, 1);
    }

    /**
     * Adds an amount of time to this instant specifying how many times to add.
     * <p>
     * If the resulting value is too large for the implementation,
     * an exception is thrown.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableDuration, String and Long.
     *
     * @param duration  duration to add.
     * @param scalar  direction and amount to add, which may be negative
     * @throws IllegalArgumentException if the duration is invalid
     * @throws ArithmeticException if the result exceeds the capacity of the instant
     */
    public void add(final Object duration, final int scalar) {
        if (duration instanceof ReadableDuration) {
            ReadableDuration d = (ReadableDuration) duration;
            d.addInto(this, scalar);
        } else {
            DurationConverter converter = ConverterManager.getInstance().getDurationConverter(duration);
            add(FieldUtils.safeMultiply(converter.getDurationMillis(duration), scalar));
        }
    }

    // Chronology
    //-----------------------------------------------------------------------
    /**
     * Set the chronology of the datetime.
     * 
     * @param chronology  the chronology to use, null means ISOChronology in default zone
     */
    public void setChronology(Chronology chronology) {
        super.setChronology(chronology);
    }

    // Time zone
    //-----------------------------------------------------------------------
    /**
     * Sets the time zone of the datetime, changing the chronology and field values.
     * <p>
     * Changing the zone using this method retains the millisecond instant.
     * The millisecond instant is adjusted in the new zone to compensate.
     * 
     * chronology. Setting the time zone does not affect the millisecond value
     * of this instant.
     * <p>
     * If the chronology already has this time zone, no change occurs.
     *
     * @param zone  the time zone to use, null means default zone
     * @see #setZoneRetainFields
     */
    public void setZone(DateTimeZone zone) {
        super.setZone(zone);
    }

    /**
     * Sets the time zone of the datetime, changing the chronology and millisecond.
     * <p>
     * Changing the zone using this method retains the field values.
     * The millisecond instant is adjusted in the new zone to compensate.
     * <p>
     * If the chronology already has this time zone, no change occurs.
     *
     * @param zone  the time zone to use, null means default zone
     * @see #setZone
     */
    public void setZoneRetainFields(DateTimeZone zone) {
        super.setZoneRetainFields(zone);
    }

    // Field based
    //-----------------------------------------------------------------------
    /**
     * Sets the value of the specified field.
     * It is permitted to use a field from another Chronology.
     * For example:
     * <pre>
     * MutableDateTime dt = new MutableDateTime();
     * dt.set(GJChronology.getInstance().year(), 2002);
     * </pre>
     * 
     * @param field  the DateTimeField to use
     * @param value the value
     * @throws IllegalArgumentException if the field is null
     * @throws IllegalArgumentException if the value is invalid
     */
    public void set(final DateTimeField field, final int value) {
        if (field == null) {
            throw new IllegalArgumentException("The DateTimeField must not be null");
        }
        setMillis(field.set(getMillis(), value));
    }

    /**
     * Adds to the value to the specified field.
     * It is permitted to use a field from another Chronology.
     * For example:
     * <pre>
     * MutableDateTime dt = new MutableDateTime();
     * dt.add(GJChronology.getInstance().year(), 2);
     * </pre>
     * Where possible the {@link #add(DurationField, int)} is a better choice as
     * it is more explicit about what is being added.
     * 
     * @param field  the DateTimeField to use
     * @param value the value
     * @throws IllegalArgumentException if the field is null
     * @throws IllegalArgumentException if the value is invalid
     */
    public void add(final DateTimeField field, final int value) {
        if (field == null) {
            throw new IllegalArgumentException("The DateTimeField must not be null");
        }
        setMillis(field.add(getMillis(), value));
    }

    /**
     * Adds the to the datetime the amount represented by the duration multiplied by the value.
     * It is permitted to use a field from another Chronology.
     * For example:
     * <pre>
     * MutableDateTime dt = new MutableDateTime();
     * dt.add(GJChronology.getInstance().years(), 2);
     * </pre>
     * 
     * @param field  the DurationField to use
     * @param value the value
     * @throws IllegalArgumentException if the field is null
     * @throws IllegalArgumentException if the value is invalid
     */
    public void add(final DurationField field, final int value) {
        if (field == null) {
            throw new IllegalArgumentException("The DurationField must not be null");
        }
        setMillis(field.add(getMillis(), value));
    }

    /**
     * Add a value to the specified field, wrapping within that field.
     * It is permitted to use a field from another Chronology.
     * For example:
     * <pre>
     * MutableDateTime dt = new MutableDateTime();
     * addWrapField(GJChronology.getInstance().monthOfYear(), 6);
     * </pre>
     * 
     * @param field  the DateTimeField to use
     * @param value the value
     * @throws IllegalArgumentException if the field is null
     * @throws IllegalArgumentException if the value is invalid
     */
    public void addWrapField(final DateTimeField field, final int value) {
        if (field == null) {
            throw new IllegalArgumentException("The DateTimeField must not be null");
        }
        setMillis(field.addWrapField(getMillis(), value));
    }

    // Date methods
    //-----------------------------------------------------------------------
    /**
     * Set the year to the specified value.
     *
     * @param year  the year
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setYear(final int year) {
        setMillis(getChronology().year().set(getMillis(), year));
    }

    /**
     * Add a number of years to the date.
     *
     * @param years  the years to add
     * @throws IllegalArgumentException if the value is invalid
     */
    public void addYears(final int years) {
        setMillis(getChronology().years().add(getMillis(), years));
    }

    //-----------------------------------------------------------------------
    /**
     * Set the weekyear to the specified value.
     *
     * @param weekyear  the weekyear
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setWeekyear(final int weekyear) {
        setMillis(getChronology().weekyear().set(getMillis(), weekyear));
    }

    /**
     * Add a number of weekyears to the date.
     *
     * @param weekyears  the weekyears to add
     * @throws IllegalArgumentException if the value is invalid
     */
    public void addWeekyears(final int weekyears) {
        setMillis(getChronology().weekyears().add(getMillis(), weekyears));
    }

    //-----------------------------------------------------------------------
    /**
     * Set the month of the year to the specified value.
     *
     * @param monthOfYear  the month of the year
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setMonthOfYear(final int monthOfYear) {
        setMillis(getChronology().monthOfYear().set(getMillis(), monthOfYear));
    }

    /**
     * Add a number of months to the date.
     *
     * @param months  the months to add
     * @throws IllegalArgumentException if the value is invalid
     */
    public void addMonths(final int months) {
        setMillis(getChronology().months().add(getMillis(), months));
    }

    //-----------------------------------------------------------------------
    /**
     * Set the week of weekyear to the specified value.
     *
     * @param weekOfWeekyear the week of the weekyear
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setWeekOfWeekyear(final int weekOfWeekyear) {
        setMillis(getChronology().weekOfWeekyear().set(getMillis(), weekOfWeekyear));
    }

    /**
     * Add a number of weeks to the date.
     *
     * @param weeks  the weeks to add
     * @throws IllegalArgumentException if the value is invalid
     */
    public void addWeeks(final int weeks) {
        setMillis(getChronology().weeks().add(getMillis(), weeks));
    }

    //-----------------------------------------------------------------------
    /**
     * Set the day of year to the specified value.
     *
     * @param dayOfYear the day of the year
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setDayOfYear(final int dayOfYear) {
        setMillis(getChronology().dayOfYear().set(getMillis(), dayOfYear));
    }

    /**
     * Set the day of the month to the specified value.
     *
     * @param dayOfMonth  the day of the month
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setDayOfMonth(final int dayOfMonth) {
        setMillis(getChronology().dayOfMonth().set(getMillis(), dayOfMonth));
    }

    /**
     * Set the day of week to the specified value.
     *
     * @param dayOfWeek  the day of the week
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setDayOfWeek(final int dayOfWeek) {
        setMillis(getChronology().dayOfWeek().set(getMillis(), dayOfWeek));
    }

    /**
     * Add a number of days to the date.
     *
     * @param days  the days to add
     * @throws IllegalArgumentException if the value is invalid
     */
    public void addDays(final int days) {
        setMillis(getChronology().days().add(getMillis(), days));
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

    // Setters
    //-----------------------------------------------------------------------
    /**
     * Set the date from milliseconds.
     * The time part of this object will be unaffected.
     *
     * @param instant  milliseconds from 1970-01-01T00:00:00Z, time part ignored
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setDate(final long instant) {
        setMillis(getChronology().millisOfDay().set(instant, getMillisOfDay()));
    }

    /**
     * Set the date from an object representing an instant.
     * The time part of this object will be unaffected.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  an object representing an instant, time part ignored
     * @throws IllegalArgumentException if the object is invalid
     */
    public void setDate(final Object instant) {
        // TODO: Does time zone need to be considered? See setTime(Object)
        InstantConverter converter = ConverterManager.getInstance().getInstantConverter(instant);
        setDate(converter.getInstantMillis(instant));
    }

    /**
     * Set the date from fields.
     * The time part of this object will be unaffected.
     *
     * @param year  the year
     * @param monthOfYear  the month of the year
     * @param dayOfMonth  the day of the month
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setDate(
            final int year,
            final int monthOfYear,
            final int dayOfMonth) {
        Chronology c = getChronology();
        long instantMidnight = c.getDateTimeMillis(year, monthOfYear, dayOfMonth, 0);
        setDate(instantMidnight);
    }

    //-----------------------------------------------------------------------
    /**
     * Set the time from milliseconds.
     * The date part of this object will be unaffected.
     *
     * @param millis milliseconds from T00:00:00Z, date part ignored
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setTime(final long millis) {
        int millisOfDay = ISOChronology.getInstanceUTC().millisOfDay().get(millis);
        setMillis(getChronology().millisOfDay().set(getMillis(), millisOfDay));
    }

    /**
     * Set the time from an object representing an instant.
     * The date part of this object will be unaffected.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  an object representing an instant, date part ignored
     * @throws IllegalArgumentException if the object is invalid
     */
    public void setTime(final Object instant) {
        InstantConverter converter = ConverterManager.getInstance().getInstantConverter(instant);
        long millis = converter.getInstantMillis(instant);
        DateTimeZone zone = converter.getChronology(instant).getZone();
        if (zone != null) {
            millis = zone.getMillisKeepLocal(DateTimeZone.UTC, millis);
        }
        setTime(millis);
    }

    /**
     * Set the time from fields.
     * The date part of this object will be unaffected.
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
        long instant = getChronology().getDateTimeMillis(
            getMillis(), hour, minuteOfHour, secondOfMinute, millisOfSecond);
        setMillis(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Set the date and time from milliseconds.
     * This method is a synonym for {@link #setMillis(long)}.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setDateTime(final long instant) {
        setMillis(instant);
    }

    /**
     * Set the date and time from an object representing an instant.
     * This method is a synonym for {@link #setMillis(Object)}.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  an object representing an instant
     * @throws IllegalArgumentException if the object is invalid
     */
    public void setDateTime(final Object instant) {
        setMillis(instant);
    }

    /**
     * Set the date and time from fields.
     *
     * @param year  the year
     * @param monthOfYear  the month of the year
     * @param dayOfMonth  the day of the month
     * @param hourOfDay  the hour of the day
     * @param minuteOfHour  the minute of the hour
     * @param secondOfMinute  the second of the minute
     * @param millisOfSecond  the millisecond of the second
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setDateTime(
            final int year,
            final int monthOfYear,
            final int dayOfMonth,
            final int hourOfDay,
            final int minuteOfHour,
            final int secondOfMinute,
            final int millisOfSecond) {
        long instant = getChronology().getDateTimeMillis(
            year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        setDateTime(instant);
    }

    // Date properties
    //-----------------------------------------------------------------------
    /**
     * Get the era property.
     * 
     * @return the era property
     */
    public ReadWritableInstantFieldProperty era() {
        return new ReadWritableInstantFieldProperty(this, getChronology().era());
    }

    /**
     * Get the century of era property.
     * 
     * @return the year of era property
     */
    public ReadWritableInstantFieldProperty centuryOfEra() {
        return new ReadWritableInstantFieldProperty(this, getChronology().centuryOfEra());
    }

    /**
     * Get the year of century property.
     * 
     * @return the year of era property
     */
    public ReadWritableInstantFieldProperty yearOfCentury() {
        return new ReadWritableInstantFieldProperty(this, getChronology().yearOfCentury());
    }

    /**
     * Get the year of era property.
     * 
     * @return the year of era property
     */
    public ReadWritableInstantFieldProperty yearOfEra() {
        return new ReadWritableInstantFieldProperty(this, getChronology().yearOfEra());
    }

    /**
     * Get the year property.
     * 
     * @return the year property
     */
    public ReadWritableInstantFieldProperty year() {
        return new ReadWritableInstantFieldProperty(this, getChronology().year());
    }

    /**
     * Get the year of a week based year property.
     * 
     * @return the year of a week based year property
     */
    public ReadWritableInstantFieldProperty weekyear() {
        return new ReadWritableInstantFieldProperty(this, getChronology().weekyear());
    }

    /**
     * Get the month of year property.
     * 
     * @return the month of year property
     */
    public ReadWritableInstantFieldProperty monthOfYear() {
        return new ReadWritableInstantFieldProperty(this, getChronology().monthOfYear());
    }

    /**
     * Get the week of a week based year property.
     * 
     * @return the week of a week based year property
     */
    public ReadWritableInstantFieldProperty weekOfWeekyear() {
        return new ReadWritableInstantFieldProperty(this, getChronology().weekOfWeekyear());
    }

    /**
     * Get the day of year property.
     * 
     * @return the day of year property
     */
    public ReadWritableInstantFieldProperty dayOfYear() {
        return new ReadWritableInstantFieldProperty(this, getChronology().dayOfYear());
    }

    /**
     * Get the day of month property.
     * <p>
     * The values for day of month are defined in {@link DateTimeConstants}.
     * 
     * @return the day of month property
     */
    public ReadWritableInstantFieldProperty dayOfMonth() {
        return new ReadWritableInstantFieldProperty(this, getChronology().dayOfMonth());
    }

    /**
     * Get the day of week property.
     * <p>
     * The values for day of week are defined in {@link DateTimeConstants}.
     * 
     * @return the day of week property
     */
    public ReadWritableInstantFieldProperty dayOfWeek() {
        return new ReadWritableInstantFieldProperty(this, getChronology().dayOfWeek());
    }

    // Time properties
    //-----------------------------------------------------------------------
    /**
     * Get the hour of day field property
     * 
     * @return the hour of day property
     */
    public ReadWritableInstantFieldProperty hourOfDay() {
        return new ReadWritableInstantFieldProperty(this, getChronology().hourOfDay());
    }

    /**
     * Get the minute of day property
     * 
     * @return the minute of day property
     */
    public ReadWritableInstantFieldProperty minuteOfDay() {
        return new ReadWritableInstantFieldProperty(this, getChronology().minuteOfDay());
    }

    /**
     * Get the minute of hour field property
     * 
     * @return the minute of hour property
     */
    public ReadWritableInstantFieldProperty minuteOfHour() {
        return new ReadWritableInstantFieldProperty(this, getChronology().minuteOfHour());
    }

    /**
     * Get the second of day property
     * 
     * @return the second of day property
     */
    public ReadWritableInstantFieldProperty secondOfDay() {
        return new ReadWritableInstantFieldProperty(this, getChronology().secondOfDay());
    }

    /**
     * Get the second of minute field property
     * 
     * @return the second of minute property
     */
    public ReadWritableInstantFieldProperty secondOfMinute() {
        return new ReadWritableInstantFieldProperty(this, getChronology().secondOfMinute());
    }

    /**
     * Get the millis of day property
     * 
     * @return the millis of day property
     */
    public ReadWritableInstantFieldProperty millisOfDay() {
        return new ReadWritableInstantFieldProperty(this, getChronology().millisOfDay());
    }

    /**
     * Get the millis of second property
     * 
     * @return the millis of second property
     */
    public ReadWritableInstantFieldProperty millisOfSecond() {
        return new ReadWritableInstantFieldProperty(this, getChronology().millisOfSecond());
    }

    // Misc
    //-----------------------------------------------------------------------
    /**
     * Clone this object without having to cast the returned object.
     *
     * @return a clone of the this object.
     */
    public MutableDateTime copy() {
        return (MutableDateTime)clone();
    }

    // Basics
    //-----------------------------------------------------------------------
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
     * Output the date time in ISO8601 format (yyyy-MM-ddTHH:mm:ss.SSSZ).
     * 
     * @return ISO8601 time formatted string.
     */
    public String toString() {
        return ISODateTimeFormat.getInstance(getChronology()).dateTime().print(this);
    }

}
