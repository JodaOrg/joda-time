/*
 *  Copyright 2001-2010 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Locale;

import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.base.BaseDateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.AbstractReadableInstantFieldProperty;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * MutableDateTime is the standard implementation of a modifiable datetime class.
 * It holds the datetime as milliseconds from the Java epoch of 1970-01-01T00:00:00Z.
 * <p>
 * This class uses a Chronology internally. The Chronology determines how the
 * millisecond instant value is converted into the date time fields.
 * The default Chronology is <code>ISOChronology</code> which is the agreed
 * international standard and compatible with the modern Gregorian calendar.
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
 * <li>get text value
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
 * @author Mike Schrag
 * @since 1.0
 * @see DateTime
 */
public class MutableDateTime
        extends BaseDateTime
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

    //-----------------------------------------------------------------------
    /**
     * Obtains a {@code MutableDateTime} set to the current system millisecond time
     * using <code>ISOChronology</code> in the default time zone.
     * 
     * @return the current date-time, not null
     * @since 2.0
     */
    public static MutableDateTime now() {
        return new MutableDateTime();
    }

    /**
     * Obtains a {@code MutableDateTime} set to the current system millisecond time
     * using <code>ISOChronology</code> in the specified time zone.
     *
     * @param zone  the time zone, not null
     * @return the current date-time, not null
     * @since 2.0
     */
    public static MutableDateTime now(DateTimeZone zone) {
        if (zone == null) {
            throw new NullPointerException("Zone must not be null");
        }
        return new MutableDateTime(zone);
    }

    /**
     * Obtains a {@code MutableDateTime} set to the current system millisecond time
     * using the specified chronology.
     *
     * @param chronology  the chronology, not null
     * @return the current date-time, not null
     * @since 2.0
     */
    public static MutableDateTime now(Chronology chronology) {
        if (chronology == null) {
            throw new NullPointerException("Chronology must not be null");
        }
        return new MutableDateTime(chronology);
    }

    //-----------------------------------------------------------------------
    /**
     * Parses a {@code MutableDateTime} from the specified string.
     * <p>
     * This uses {@link ISODateTimeFormat#dateTimeParser()}.
     * 
     * @param str  the string to parse, not null
     * @since 2.0
     */
    @FromString
    public static MutableDateTime parse(String str) {
        return parse(str, ISODateTimeFormat.dateTimeParser().withOffsetParsed());
    }

    /**
     * Parses a {@code MutableDateTime} from the specified string using a formatter.
     * 
     * @param str  the string to parse, not null
     * @param formatter  the formatter to use, not null
     * @since 2.0
     */
    public static MutableDateTime parse(String str, DateTimeFormatter formatter) {
        return formatter.parseDateTime(str).toMutableDateTime();
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance set to the current system millisecond time
     * using <code>ISOChronology</code> in the default time zone.
     * 
     * @see #now()
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
     * @see #now(DateTimeZone)
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
     * @see #now(Chronology)
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
     * If the object implies a chronology (such as GregorianCalendar does),
     * then that chronology will be used. Otherwise, ISO default is used.
     * Thus if a GregorianCalendar is passed in, the chronology used will
     * be GJ, but if a Date is passed in the chronology will be ISO.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  the datetime object, null means now
     * @throws IllegalArgumentException if the instant is invalid
     */
    public MutableDateTime(Object instant) {
        super(instant, (Chronology) null);
    }

    /**
     * Constructs an instance from an Object that represents a datetime,
     * forcing the time zone to that specified.
     * <p>
     * If the object implies a chronology (such as GregorianCalendar does),
     * then that chronology will be used, but with the time zone adjusted.
     * Otherwise, ISO is used in the specified time zone.
     * If the specified time zone is null, the default zone is used.
     * Thus if a GregorianCalendar is passed in, the chronology used will
     * be GJ, but if a Date is passed in the chronology will be ISO.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  the datetime object, null means now
     * @param zone  the time zone, null means default time zone
     * @throws IllegalArgumentException if the instant is invalid
     */
    public MutableDateTime(Object instant, DateTimeZone zone) {
        super(instant, zone);
    }

    /**
     * Constructs an instance from an Object that represents a datetime,
     * using the specified chronology.
     * <p>
     * If the chronology is null, ISO in the default time zone is used.
     * Any chronology implied by the object (such as GregorianCalendar does)
     * is ignored.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  the datetime object, null means now
     * @param chronology  the chronology, null means ISOChronology in default zone
     * @throws IllegalArgumentException if the instant is invalid
     */
    public MutableDateTime(Object instant, Chronology chronology) {
        super(instant, DateTimeUtils.getChronology(chronology));
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

    //-----------------------------------------------------------------------
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

    //-----------------------------------------------------------------------
    /**
     * Set the milliseconds of the datetime.
     * <p>
     * All changes to the millisecond field occurs via this method.
     *
     * @param instant  the milliseconds since 1970-01-01T00:00:00Z to set the
     * datetime to
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
     * Sets the millisecond instant of this instant from another.
     * <p>
     * This method does not change the chronology of this instant, just the
     * millisecond instant.
     * 
     * @param instant  the instant to use, null means now
     */
    public void setMillis(ReadableInstant instant) {
        long instantMillis = DateTimeUtils.getInstantMillis(instant);
        setMillis(instantMillis);  // set via this class not super
    }

    //-----------------------------------------------------------------------
    /**
     * Add an amount of time to the datetime.
     * 
     * @param duration  the millis to add
     * @throws ArithmeticException if the result exceeds the capacity of the instant
     */
    public void add(long duration) {
        setMillis(FieldUtils.safeAdd(getMillis(), duration));  // set via this class not super
    }

    /**
     * Adds a duration to this instant.
     * <p>
     * This will typically change the value of most fields.
     *
     * @param duration  the duration to add, null means add zero
     * @throws ArithmeticException if the result exceeds the capacity of the instant
     */
    public void add(ReadableDuration duration) {
        add(duration, 1);
    }

    /**
     * Adds a duration to this instant specifying how many times to add.
     * <p>
     * This will typically change the value of most fields.
     *
     * @param duration  the duration to add, null means add zero
     * @param scalar  direction and amount to add, which may be negative
     * @throws ArithmeticException if the result exceeds the capacity of the instant
     */
    public void add(ReadableDuration duration, int scalar) {
        if (duration != null) {
            add(FieldUtils.safeMultiply(duration.getMillis(), scalar));
        }
    }

    /**
     * Adds a period to this instant.
     * <p>
     * This will typically change the value of most fields.
     *
     * @param period  the period to add, null means add zero
     * @throws ArithmeticException if the result exceeds the capacity of the instant
     */
    public void add(ReadablePeriod period) {
        add(period, 1);
    }

    /**
     * Adds a period to this instant specifying how many times to add.
     * <p>
     * This will typically change the value of most fields.
     *
     * @param period  the period to add, null means add zero
     * @param scalar  direction and amount to add, which may be negative
     * @throws ArithmeticException if the result exceeds the capacity of the instant
     */
    public void add(ReadablePeriod period, int scalar) {
        if (period != null) {
            setMillis(getChronology().add(period, getMillis(), scalar));  // set via this class not super
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Set the chronology of the datetime.
     * <p>
     * All changes to the chronology occur via this method.
     * 
     * @param chronology  the chronology to use, null means ISOChronology in default zone
     */
    public void setChronology(Chronology chronology) {
        super.setChronology(chronology);
    }

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
     * @param newZone  the time zone to use, null means default zone
     * @see #setZoneRetainFields
     */
    public void setZone(DateTimeZone newZone) {
        newZone = DateTimeUtils.getZone(newZone);
        Chronology chrono = getChronology();
        if (chrono.getZone() != newZone) {
            setChronology(chrono.withZone(newZone));  // set via this class not super
        }
    }

    /**
     * Sets the time zone of the datetime, changing the chronology and millisecond.
     * <p>
     * Changing the zone using this method retains the field values.
     * The millisecond instant is adjusted in the new zone to compensate.
     * <p>
     * If the chronology already has this time zone, no change occurs.
     *
     * @param newZone  the time zone to use, null means default zone
     * @see #setZone
     */
    public void setZoneRetainFields(DateTimeZone newZone) {
        newZone = DateTimeUtils.getZone(newZone);
        DateTimeZone originalZone = DateTimeUtils.getZone(getZone());
        if (newZone == originalZone) {
            return;
        }
        
        long millis = originalZone.getMillisKeepLocal(newZone, getMillis());
        setChronology(getChronology().withZone(newZone));  // set via this class not super
        setMillis(millis);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the value of one of the fields of the instant, such as hourOfDay.
     *
     * @param type  a field type, usually obtained from DateTimeFieldType, not null
     * @param value  the value to set the field to
     * @throws IllegalArgumentException if the value is null or invalid
     */
    public void set(DateTimeFieldType type, int value) {
        if (type == null) {
            throw new IllegalArgumentException("Field must not be null");
        }
        setMillis(type.getField(getChronology()).set(getMillis(), value));
    }

    /**
     * Adds to the instant specifying the duration and multiple to add.
     *
     * @param type  a field type, usually obtained from DateTimeFieldType, not null
     * @param amount  the amount to add of this duration
     * @throws IllegalArgumentException if the value is null or invalid
     * @throws ArithmeticException if the result exceeds the capacity of the instant
     */
    public void add(DurationFieldType type, int amount) {
        if (type == null) {
            throw new IllegalArgumentException("Field must not be null");
        }
        setMillis(type.getField(getChronology()).add(getMillis(), amount));
    }

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

    //-----------------------------------------------------------------------
    /**
     * Set the date from milliseconds.
     * The time part of this object will be unaffected.
     *
     * @param instant  an instant to copy the date from, time part ignored
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setDate(final long instant) {
        setMillis(getChronology().millisOfDay().set(instant, getMillisOfDay()));
    }

    /**
     * Set the date from another instant.
     * The time part of this object will be unaffected.
     *
     * @param instant  an instant to copy the date from, time part ignored
     * @throws IllegalArgumentException if the object is invalid
     */
    public void setDate(final ReadableInstant instant) {
        long instantMillis = DateTimeUtils.getInstantMillis(instant);
        Chronology instantChrono = DateTimeUtils.getInstantChronology(instant);
        DateTimeZone zone = instantChrono.getZone();
        if (zone != null) {
            instantMillis = zone.getMillisKeepLocal(DateTimeZone.UTC, instantMillis);
        }
        setDate(instantMillis);
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
     * @param millis  an instant to copy the time from, date part ignored
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setTime(final long millis) {
        int millisOfDay = ISOChronology.getInstanceUTC().millisOfDay().get(millis);
        setMillis(getChronology().millisOfDay().set(getMillis(), millisOfDay));
    }

    /**
     * Set the time from another instant.
     * The date part of this object will be unaffected.
     *
     * @param instant  an instant to copy the time from, date part ignored
     * @throws IllegalArgumentException if the object is invalid
     */
    public void setTime(final ReadableInstant instant) {
        long instantMillis = DateTimeUtils.getInstantMillis(instant);
        Chronology instantChrono = DateTimeUtils.getInstantChronology(instant);
        DateTimeZone zone = instantChrono.getZone();
        if (zone != null) {
            instantMillis = zone.getMillisKeepLocal(DateTimeZone.UTC, instantMillis);
        }
        setTime(instantMillis);
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
        setMillis(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the property object for the specified type, which contains many useful methods.
     *
     * @param type  the field type to get the chronology for
     * @return the property object
     * @throws IllegalArgumentException if the field is null or unsupported
     * @since 1.2
     */
    public Property property(DateTimeFieldType type) {
        if (type == null) {
            throw new IllegalArgumentException("The DateTimeFieldType must not be null");
        }
        DateTimeField field = type.getField(getChronology());
        if (field.isSupported() == false) {
            throw new IllegalArgumentException("Field '" + type + "' is not supported");
        }
        return new Property(this, field);
    }

    /**
     * Get the era property.
     * 
     * @return the era property
     */
    public Property era() {
        return new Property(this, getChronology().era());
    }

    /**
     * Get the century of era property.
     * 
     * @return the year of era property
     */
    public Property centuryOfEra() {
        return new Property(this, getChronology().centuryOfEra());
    }

    /**
     * Get the year of century property.
     * 
     * @return the year of era property
     */
    public Property yearOfCentury() {
        return new Property(this, getChronology().yearOfCentury());
    }

    /**
     * Get the year of era property.
     * 
     * @return the year of era property
     */
    public Property yearOfEra() {
        return new Property(this, getChronology().yearOfEra());
    }

    /**
     * Get the year property.
     * 
     * @return the year property
     */
    public Property year() {
        return new Property(this, getChronology().year());
    }

    /**
     * Get the year of a week based year property.
     * 
     * @return the year of a week based year property
     */
    public Property weekyear() {
        return new Property(this, getChronology().weekyear());
    }

    /**
     * Get the month of year property.
     * 
     * @return the month of year property
     */
    public Property monthOfYear() {
        return new Property(this, getChronology().monthOfYear());
    }

    /**
     * Get the week of a week based year property.
     * 
     * @return the week of a week based year property
     */
    public Property weekOfWeekyear() {
        return new Property(this, getChronology().weekOfWeekyear());
    }

    /**
     * Get the day of year property.
     * 
     * @return the day of year property
     */
    public Property dayOfYear() {
        return new Property(this, getChronology().dayOfYear());
    }

    /**
     * Get the day of month property.
     * <p>
     * The values for day of month are defined in {@link DateTimeConstants}.
     * 
     * @return the day of month property
     */
    public Property dayOfMonth() {
        return new Property(this, getChronology().dayOfMonth());
    }

    /**
     * Get the day of week property.
     * <p>
     * The values for day of week are defined in {@link DateTimeConstants}.
     * 
     * @return the day of week property
     */
    public Property dayOfWeek() {
        return new Property(this, getChronology().dayOfWeek());
    }

    //-----------------------------------------------------------------------
    /**
     * Get the hour of day field property
     * 
     * @return the hour of day property
     */
    public Property hourOfDay() {
        return new Property(this, getChronology().hourOfDay());
    }

    /**
     * Get the minute of day property
     * 
     * @return the minute of day property
     */
    public Property minuteOfDay() {
        return new Property(this, getChronology().minuteOfDay());
    }

    /**
     * Get the minute of hour field property
     * 
     * @return the minute of hour property
     */
    public Property minuteOfHour() {
        return new Property(this, getChronology().minuteOfHour());
    }

    /**
     * Get the second of day property
     * 
     * @return the second of day property
     */
    public Property secondOfDay() {
        return new Property(this, getChronology().secondOfDay());
    }

    /**
     * Get the second of minute field property
     * 
     * @return the second of minute property
     */
    public Property secondOfMinute() {
        return new Property(this, getChronology().secondOfMinute());
    }

    /**
     * Get the millis of day property
     * 
     * @return the millis of day property
     */
    public Property millisOfDay() {
        return new Property(this, getChronology().millisOfDay());
    }

    /**
     * Get the millis of second property
     * 
     * @return the millis of second property
     */
    public Property millisOfSecond() {
        return new Property(this, getChronology().millisOfSecond());
    }

    //-----------------------------------------------------------------------
    /**
     * Clone this object without having to cast the returned object.
     *
     * @return a clone of the this object.
     */
    public MutableDateTime copy() {
        return (MutableDateTime) clone();
    }

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
     * Output the date time in ISO8601 format (yyyy-MM-ddTHH:mm:ss.SSSZZ).
     * 
     * @return ISO8601 time formatted string.
     */
    @ToString
    public String toString() {
        return ISODateTimeFormat.dateTime().print(this);
    }

    /**
     * MutableDateTime.Property binds a MutableDateTime to a
     * DateTimeField allowing powerful datetime functionality to be easily
     * accessed.
     * <p>
     * The example below shows how to use the property to change the value of a
     * MutableDateTime object.
     * <pre>
     * MutableDateTime dt = new MutableDateTime(1972, 12, 3, 13, 32, 19, 123);
     * dt.year().add(20);
     * dt.second().roundFloor().minute().set(10);
     * </pre>
     * <p>
     * MutableDateTime.Propery itself is thread-safe and immutable, but the
     * MutableDateTime being operated on is not.
     *
     * @author Stephen Colebourne
     * @author Brian S O'Neill
     * @since 1.0
     */
    public static final class Property extends AbstractReadableInstantFieldProperty {
        
        /** Serialization version */
        private static final long serialVersionUID = -4481126543819298617L;
        
        /** The instant this property is working against */
        private MutableDateTime iInstant;
        /** The field this property is working against */
        private DateTimeField iField;
        
        /**
         * Constructor.
         * 
         * @param instant  the instant to set
         * @param field  the field to use
         */
        Property(MutableDateTime instant, DateTimeField field) {
            super();
            iInstant = instant;
            iField = field;
        }
        
        /**
         * Writes the property in a safe serialization format.
         */
        private void writeObject(ObjectOutputStream oos) throws IOException {
            oos.writeObject(iInstant);
            oos.writeObject(iField.getType());
        }

        /**
         * Reads the property from a safe serialization format.
         */
        private void readObject(ObjectInputStream oos) throws IOException, ClassNotFoundException {
            iInstant = (MutableDateTime) oos.readObject();
            DateTimeFieldType type = (DateTimeFieldType) oos.readObject();
            iField = type.getField(iInstant.getChronology());
        }

        //-----------------------------------------------------------------------
        /**
         * Gets the field being used.
         * 
         * @return the field
         */
        public DateTimeField getField() {
            return iField;
        }
        
        /**
         * Gets the milliseconds of the datetime that this property is linked to.
         * 
         * @return the milliseconds
         */
        protected long getMillis() {
            return iInstant.getMillis();
        }
        
        /**
         * Gets the chronology of the datetime that this property is linked to.
         * 
         * @return the chronology
         * @since 1.4
         */
        protected Chronology getChronology() {
            return iInstant.getChronology();
        }
        
        /**
         * Gets the mutable datetime being used.
         * 
         * @return the mutable datetime
         */
        public MutableDateTime getMutableDateTime() {
            return iInstant;
        }
        
        //-----------------------------------------------------------------------
        /**
         * Adds a value to the millis value.
         * 
         * @param value  the value to add
         * @return the mutable datetime being used, so calls can be chained
         * @see DateTimeField#add(long,int)
         */
        public MutableDateTime add(int value) {
            iInstant.setMillis(getField().add(iInstant.getMillis(), value));
            return iInstant;
        }
        
        /**
         * Adds a value to the millis value.
         * 
         * @param value  the value to add
         * @return the mutable datetime being used, so calls can be chained
         * @see DateTimeField#add(long,long)
         */
        public MutableDateTime add(long value) {
            iInstant.setMillis(getField().add(iInstant.getMillis(), value));
            return iInstant;
        }
        
        /**
         * Adds a value, possibly wrapped, to the millis value.
         * 
         * @param value  the value to add
         * @return the mutable datetime being used, so calls can be chained
         * @see DateTimeField#addWrapField
         */
        public MutableDateTime addWrapField(int value) {
            iInstant.setMillis(getField().addWrapField(iInstant.getMillis(), value));
            return iInstant;
        }
        
        //-----------------------------------------------------------------------
        /**
         * Sets a value.
         * 
         * @param value  the value to set.
         * @return the mutable datetime being used, so calls can be chained
         * @see DateTimeField#set(long,int)
         */
        public MutableDateTime set(int value) {
            iInstant.setMillis(getField().set(iInstant.getMillis(), value));
            return iInstant;
        }
        
        /**
         * Sets a text value.
         * 
         * @param text  the text value to set
         * @param locale  optional locale to use for selecting a text symbol
         * @return the mutable datetime being used, so calls can be chained
         * @throws IllegalArgumentException if the text value isn't valid
         * @see DateTimeField#set(long,java.lang.String,java.util.Locale)
         */
        public MutableDateTime set(String text, Locale locale) {
            iInstant.setMillis(getField().set(iInstant.getMillis(), text, locale));
            return iInstant;
        }
        
        /**
         * Sets a text value.
         * 
         * @param text  the text value to set
         * @return the mutable datetime being used, so calls can be chained
         * @throws IllegalArgumentException if the text value isn't valid
         * @see DateTimeField#set(long,java.lang.String)
         */
        public MutableDateTime set(String text) {
            set(text, null);
            return iInstant;
        }
        
        //-----------------------------------------------------------------------
        /**
         * Round to the lowest whole unit of this field.
         *
         * @return the mutable datetime being used, so calls can be chained
         * @see DateTimeField#roundFloor
         */
        public MutableDateTime roundFloor() {
            iInstant.setMillis(getField().roundFloor(iInstant.getMillis()));
            return iInstant;
        }

        /**
         * Round to the highest whole unit of this field.
         *
         * @return the mutable datetime being used, so calls can be chained
         * @see DateTimeField#roundCeiling
         */
        public MutableDateTime roundCeiling() {
            iInstant.setMillis(getField().roundCeiling(iInstant.getMillis()));
            return iInstant;
        }
        
        /**
         * Round to the nearest whole unit of this field, favoring the floor if
         * halfway.
         *
         * @return the mutable datetime being used, so calls can be chained
         * @see DateTimeField#roundHalfFloor
         */
        public MutableDateTime roundHalfFloor() {
            iInstant.setMillis(getField().roundHalfFloor(iInstant.getMillis()));
            return iInstant;
        }
        
        /**
         * Round to the nearest whole unit of this field, favoring the ceiling if
         * halfway.
         *
         * @return the mutable datetime being used, so calls can be chained
         * @see DateTimeField#roundHalfCeiling
         */
        public MutableDateTime roundHalfCeiling() {
            iInstant.setMillis(getField().roundHalfCeiling(iInstant.getMillis()));
            return iInstant;
        }

        /**
         * Round to the nearest whole unit of this field. If halfway, the ceiling
         * is favored over the floor only if it makes this field's value even.
         *
         * @return the mutable datetime being used, so calls can be chained
         * @see DateTimeField#roundHalfEven
         */
        public MutableDateTime roundHalfEven() {
            iInstant.setMillis(getField().roundHalfEven(iInstant.getMillis()));
            return iInstant;
        }
    }

}
