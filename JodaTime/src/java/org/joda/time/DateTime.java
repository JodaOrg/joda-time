/*
 *  Copyright 2001-2005 Stephen Colebourne
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

import java.io.Serializable;
import java.util.Locale;

import org.joda.time.base.BaseDateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.AbstractReadableInstantFieldProperty;

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
 * <li>maximum/minimum values
 * <li>add/subtract
 * <li>set
 * <li>rounding
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
 */
public final class DateTime
        extends BaseDateTime
        implements ReadableDateTime, Serializable {

    /** Serialization lock */
    private static final long serialVersionUID = -5171125899451703815L;

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
    public DateTime(Object instant) {
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
    public DateTime(Object instant, DateTimeZone zone) {
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
     * @param chronology  the chronology, null means ISO in default zone
     * @throws IllegalArgumentException if the instant is invalid
     */
    public DateTime(Object instant, Chronology chronology) {
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
     * Get this object as a DateTime by returning <code>this</code>.
     * 
     * @return <code>this</code>
     */
    public DateTime toDateTime() {
        return this;
    }

    /**
     * Get this object as a DateTime using ISOChronology in the default zone,
     * returning <code>this</code> if possible.
     * 
     * @return a DateTime using the same millis
     */
    public DateTime toDateTimeISO() {
        if (getChronology() == ISOChronology.getInstance()) {
            return this;
        }
        return super.toDateTimeISO();
    }

    /**
     * Get this object as a DateTime, returning <code>this</code> if possible.
     * 
     * @param zone time zone to apply, or default if null
     * @return a DateTime using the same millis
     */
    public DateTime toDateTime(DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        if (getZone() == zone) {
            return this;
        }
        return super.toDateTime(zone);
    }

    /**
     * Get this object as a DateTime, returning <code>this</code> if possible.
     * 
     * @param chronology chronology to apply, or ISOChronology if null
     * @return a DateTime using the same millis
     */
    public DateTime toDateTime(Chronology chronology) {
        chronology = DateTimeUtils.getChronology(chronology);
        if (getChronology() == chronology) {
            return this;
        }
        return super.toDateTime(chronology);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this datetime with different millis.
     * <p>
     * The returned object will be a new instance of the same implementation type.
     * Only the millis will change, the chronology and time zone are kept.
     * The returned object will be either be a new instance or <code>this</code>.
     *
     * @param newMillis  the new millis, from 1970-01-01T00:00:00Z
     * @return a copy of this datetime with different millis
     */
    public DateTime withMillis(long newMillis) {
        return (newMillis == getMillis() ? this : new DateTime(newMillis, getChronology()));
    }

    /**
     * Gets a copy of this datetime with a different chronology.
     * <p>
     * The returned object will be a new instance of the same implementation type.
     * Only the chronology will change, the millis are kept.
     * The returned object will be either be a new instance or <code>this</code>.
     *
     * @param newChronology  the new chronology, null means ISO default
     * @return a copy of this datetime with a different chronology
     */
    public DateTime withChronology(Chronology newChronology) {
        newChronology = DateTimeUtils.getChronology(newChronology);
        return (newChronology == getChronology() ? this : new DateTime(getMillis(), newChronology));
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this datetime with a different time zone, preserving the
     * millisecond instant.
     * <p>
     * This method is useful for finding the local time in another timezone.
     * For example, if this instant holds 12:30 in Europe/London, the result
     * from this method with Europe/Paris would be 13:30.
     * <p>
     * The returned object will be a new instance of the same implementation type.
     * This method changes the time zone, and does not change the
     * millisecond instant, with the effect that the field values usually change.
     * The returned object will be either be a new instance or <code>this</code>.
     *
     * @param newZone  the new time zone
     * @return a copy of this datetime with a different time zone
     * @see #withZoneRetainFields
     */
    public DateTime withZone(DateTimeZone newZone) {
        return withChronology(getChronology().withZone(newZone));
    }

    /**
     * Gets a copy of this datetime with a different time zone, preserving the
     * field values.
     * <p>
     * This method is useful for finding the millisecond time in another timezone.
     * For example, if this instant holds 12:30 in Europe/London (ie. 12:30Z),
     * the result from this method with Europe/Paris would be 12:30 (ie. 11:30Z).
     * <p>
     * The returned object will be a new instance of the same implementation type.
     * This method changes the time zone and the millisecond instant to keep
     * the field values the same.
     * The returned object will be either be a new instance or <code>this</code>.
     *
     * @param newZone  the new time zone, null means default
     * @return a copy of this datetime with a different time zone
     * @see #withZone
     */
    public DateTime withZoneRetainFields(DateTimeZone newZone) {
        newZone = DateTimeUtils.getZone(newZone);
        DateTimeZone originalZone = DateTimeUtils.getZone(getZone());
        if (newZone == originalZone) {
            return this;
        }
        
        long millis = originalZone.getMillisKeepLocal(newZone, getMillis());
        return new DateTime(millis, getChronology().withZone(newZone));
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this datetime with the specified date, retaining the time fields.
     * <p>
     * If the date is already the date passed in, then <code>this</code> is returned.
     * <p>
     * To set a single field use the properties, for example:
     * <pre>
     * DateTime set = monthOfYear().setCopy(6);
     * </pre>
     *
     * @param year  the new year value
     * @param monthOfYear  the new monthOfYear value
     * @param dayOfMonth  the new dayOfMonth value
     * @return a copy of this datetime with a different date
     * @throws IllegalArgumentException if any value if invalid
     */
    public DateTime withDate(int year, int monthOfYear, int dayOfMonth) {
        Chronology chrono = getChronology();
        long instant = getMillis();
        instant = chrono.year().set(instant, year);
        instant = chrono.monthOfYear().set(instant, monthOfYear);
        instant = chrono.dayOfMonth().set(instant, dayOfMonth);
        return withMillis(instant);
    }

    /**
     * Gets a copy of this datetime with the specified time, retaining the date fields.
     * <p>
     * If the time is already the time passed in, then <code>this</code> is returned.
     * <p>
     * To set a single field use the properties, for example:
     * <pre>
     * DateTime set = dt.hourOfDay().setCopy(6);
     * </pre>
     *
     * @param hourOfDay  the hour of the day
     * @param minuteOfHour  the minute of the hour
     * @param secondOfMinute  the second of the minute
     * @param millisOfSecond  the millisecond of the second
     * @return a copy of this datetime with a different time
     * @throws IllegalArgumentException if any value if invalid
     */
    public DateTime withTime(int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond) {
        Chronology chrono = getChronology();
        long instant = getMillis();
        instant = chrono.hourOfDay().set(instant, hourOfDay);
        instant = chrono.minuteOfHour().set(instant, minuteOfHour);
        instant = chrono.secondOfMinute().set(instant, secondOfMinute);
        instant = chrono.millisOfSecond().set(instant, millisOfSecond);
        return withMillis(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this datetime with the partial set of fields replacing those
     * from this instance.
     * <p>
     * For example, if the partial is a <code>TimeOfDay</code> then the time fields
     * would be changed in the returned instance.
     * If the partial is null, then <code>this</code> is returned.
     *
     * @param partial  the partial set of fields to apply to this datetime, null ignored
     * @return a copy of this datetime with a different set of fields
     * @throws IllegalArgumentException if any value is invalid
     */
    public DateTime withFields(ReadablePartial partial) {
        if (partial == null) {
            return this;
        }
        return withMillis(getChronology().set(partial, getMillis()));
    }

    /**
     * Gets a copy of this datetime with the specified field set to a new value.
     * <p>
     * For example, if the field type is <code>hourOfDay</code> then the hour of day
     * field would be changed in the returned instance.
     * If the field type is null, then <code>this</code> is returned.
     * <p>
     * These three lines are equivalent:
     * <pre>
     * DateTime updated = dt.withField(DateTimeFieldType.dayOfMonth(), 6);
     * DateTime updated = dt.dayOfMonth().setCopy(6);
     * DateTime updated = dt.property(DateTimeFieldType.dayOfMonth()).setCopy(6);
     * </pre>
     *
     * @param fieldType  the field type to set, not null
     * @param value  the value to set
     * @return a copy of this datetime with the field set
     * @throws IllegalArgumentException if the value is null or invalid
     */
    public DateTime withField(DateTimeFieldType fieldType, int value) {
        if (fieldType == null) {
            throw new IllegalArgumentException("Field must not be null");
        }
        long instant = fieldType.getField(getChronology()).set(getMillis(), value);
        return withMillis(instant);
    }

    /**
     * Gets a copy of this datetime with the value of the specified field increased.
     * <p>
     * If the addition is zero or the field is null, then <code>this</code> is returned.
     * <p>
     * These three lines are equivalent:
     * <pre>
     * DateTime added = dt.withFieldAdded(DurationFieldType.years(), 6);
     * DateTime added = dt.plusYears(6);
     * DateTime added = dt.plus(Period.years(6));
     * </pre>
     * 
     * @param fieldType  the field type to add to, not null
     * @param amount  the amount to add
     * @return a copy of this datetime with the field updated
     * @throws IllegalArgumentException if the value is null or invalid
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public DateTime withFieldAdded(DurationFieldType fieldType, int amount) {
        if (fieldType == null) {
            throw new IllegalArgumentException("Field must not be null");
        }
        if (amount == 0) {
            return this;
        }
        long instant = fieldType.getField(getChronology()).add(getMillis(), amount);
        return withMillis(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this datetime with the specified duration added.
     * <p>
     * If the addition is zero, then <code>this</code> is returned.
     * 
     * @param durationToAdd  the duration to add to this one
     * @param scalar  the amount of times to add, such as -1 to subtract once
     * @return a copy of this datetime with the duration added
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public DateTime withDurationAdded(long durationToAdd, int scalar) {
        if (durationToAdd == 0 || scalar == 0) {
            return this;
        }
        long instant = getChronology().add(getMillis(), durationToAdd, scalar);
        return withMillis(instant);
    }

    /**
     * Gets a copy of this datetime with the specified duration added.
     * <p>
     * If the addition is zero, then <code>this</code> is returned.
     * 
     * @param durationToAdd  the duration to add to this one, null means zero
     * @param scalar  the amount of times to add, such as -1 to subtract once
     * @return a copy of this datetime with the duration added
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public DateTime withDurationAdded(ReadableDuration durationToAdd, int scalar) {
        if (durationToAdd == null || scalar == 0) {
            return this;
        }
        return withDurationAdded(durationToAdd.getMillis(), scalar);
    }

    /**
     * Gets a copy of this datetime with the specified period added.
     * <p>
     * If the addition is zero, then <code>this</code> is returned.
     * <p>
     * This method is typically used to add multiple copies of complex
     * period instances. Adding one field is best achieved using methods
     * like {@link #withFieldAdded(DurationFieldType, int)}
     * or {@link #plusYears(int)}.
     * 
     * @param period  the period to add to this one, null means zero
     * @param scalar  the amount of times to add, such as -1 to subtract once
     * @return a copy of this datetime with the period added
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public DateTime withPeriodAdded(ReadablePeriod period, int scalar) {
        if (period == null || scalar == 0) {
            return this;
        }
        long instant = getChronology().add(period, getMillis(), scalar);
        return withMillis(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this datetime with the specified duration added.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * 
     * @param duration  the duration, in millis, to add to this one
     * @return a copy of this datetime with the duration added
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public DateTime plus(long duration) {
        return withDurationAdded(duration, 1);
    }

    /**
     * Gets a copy of this datetime with the specified duration added.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * 
     * @param duration  the duration to add to this one, null means zero
     * @return a copy of this datetime with the duration added
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public DateTime plus(ReadableDuration duration) {
        return withDurationAdded(duration, 1);
    }

    /**
     * Gets a copy of this datetime with the specified period added.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * <p>
     * This method is typically used to add complex period instances.
     * Adding one field is best achieved using methods
     * like {@link #plusYears(int)}.
     * 
     * @param period  the duration to add to this one, null means zero
     * @return a copy of this datetime with the period added
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public DateTime plus(ReadablePeriod period) {
        return withPeriodAdded(period, 1);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new datetime plus the specified number of years.
     * <p>
     * This datetime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * DateTime added = dt.plusYears(6);
     * DateTime added = dt.plus(Period.years(6));
     * DateTime added = dt.withFieldAdded(DurationFieldType.years(), 6);
     * </pre>
     *
     * @param years  the amount of years to add, may be negative
     * @return the new datetime plus the increased years
     * @since 1.1
     */
    public DateTime plusYears(int years) {
        if (years == 0) {
            return this;
        }
        long instant = getChronology().years().add(getMillis(), years);
        return withMillis(instant);
    }

    /**
     * Returns a new datetime plus the specified number of months.
     * <p>
     * This datetime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * DateTime added = dt.plusMonths(6);
     * DateTime added = dt.plus(Period.months(6));
     * DateTime added = dt.withFieldAdded(DurationFieldType.months(), 6);
     * </pre>
     *
     * @param months  the amount of months to add, may be negative
     * @return the new datetime plus the increased months
     * @since 1.1
     */
    public DateTime plusMonths(int months) {
        if (months == 0) {
            return this;
        }
        long instant = getChronology().months().add(getMillis(), months);
        return withMillis(instant);
    }

    /**
     * Returns a new datetime plus the specified number of weeks.
     * <p>
     * This datetime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * DateTime added = dt.plusWeeks(6);
     * DateTime added = dt.plus(Period.weeks(6));
     * DateTime added = dt.withFieldAdded(DurationFieldType.weeks(), 6);
     * </pre>
     *
     * @param weeks  the amount of weeks to add, may be negative
     * @return the new datetime plus the increased weeks
     * @since 1.1
     */
    public DateTime plusWeeks(int weeks) {
        if (weeks == 0) {
            return this;
        }
        long instant = getChronology().weeks().add(getMillis(), weeks);
        return withMillis(instant);
    }

    /**
     * Returns a new datetime plus the specified number of days.
     * <p>
     * This datetime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * DateTime added = dt.plusDays(6);
     * DateTime added = dt.plus(Period.days(6));
     * DateTime added = dt.withFieldAdded(DurationFieldType.days(), 6);
     * </pre>
     *
     * @param days  the amount of days to add, may be negative
     * @return the new datetime plus the increased days
     * @since 1.1
     */
    public DateTime plusDays(int days) {
        if (days == 0) {
            return this;
        }
        long instant = getChronology().days().add(getMillis(), days);
        return withMillis(instant);
    }

    /**
     * Returns a new datetime plus the specified number of hours.
     * <p>
     * This datetime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * DateTime added = dt.plusHours(6);
     * DateTime added = dt.plus(Period.hours(6));
     * DateTime added = dt.withFieldAdded(DurationFieldType.hours(), 6);
     * </pre>
     *
     * @param hours  the amount of hours to add, may be negative
     * @return the new datetime plus the increased hours
     * @since 1.1
     */
    public DateTime plusHours(int hours) {
        if (hours == 0) {
            return this;
        }
        long instant = getChronology().hours().add(getMillis(), hours);
        return withMillis(instant);
    }

    /**
     * Returns a new datetime plus the specified number of minutes.
     * <p>
     * This datetime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * DateTime added = dt.plusMinutes(6);
     * DateTime added = dt.plus(Period.minutes(6));
     * DateTime added = dt.withFieldAdded(DurationFieldType.minutes(), 6);
     * </pre>
     *
     * @param minutes  the amount of minutes to add, may be negative
     * @return the new datetime plus the increased minutes
     * @since 1.1
     */
    public DateTime plusMinutes(int minutes) {
        if (minutes == 0) {
            return this;
        }
        long instant = getChronology().minutes().add(getMillis(), minutes);
        return withMillis(instant);
    }

    /**
     * Returns a new datetime plus the specified number of seconds.
     * <p>
     * This datetime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * DateTime added = dt.plusSeconds(6);
     * DateTime added = dt.plus(Period.seconds(6));
     * DateTime added = dt.withFieldAdded(DurationFieldType.seconds(), 6);
     * </pre>
     *
     * @param seconds  the amount of seconds to add, may be negative
     * @return the new datetime plus the increased seconds
     * @since 1.1
     */
    public DateTime plusSeconds(int seconds) {
        if (seconds == 0) {
            return this;
        }
        long instant = getChronology().seconds().add(getMillis(), seconds);
        return withMillis(instant);
    }

    /**
     * Returns a new datetime plus the specified number of millis.
     * <p>
     * This datetime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * DateTime added = dt.plusMillis(6);
     * DateTime added = dt.plus(Period.millis(6));
     * DateTime added = dt.withFieldAdded(DurationFieldType.millis(), 6);
     * </pre>
     *
     * @param millis  the amount of millis to add, may be negative
     * @return the new datetime plus the increased millis
     * @since 1.1
     */
    public DateTime plusMillis(int millis) {
        if (millis == 0) {
            return this;
        }
        long instant = getChronology().millis().add(getMillis(), millis);
        return withMillis(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this datetime with the specified duration take away.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * 
     * @param duration  the duration, in millis, to reduce this instant by
     * @return a copy of this datetime with the duration taken away
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public DateTime minus(long duration) {
        return withDurationAdded(duration, -1);
    }

    /**
     * Gets a copy of this datetime with the specified duration take away.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * 
     * @param duration  the duration to reduce this instant by
     * @return a copy of this datetime with the duration taken away
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public DateTime minus(ReadableDuration duration) {
        return withDurationAdded(duration, -1);
    }

    /**
     * Gets a copy of this datetime with the specified period take away.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * <p>
     * This method is typically used to subtract complex period instances.
     * Subtracting one field is best achieved using methods
     * like {@link #minusYears(int)}.
     * 
     * @param period  the period to reduce this instant by
     * @return a copy of this datetime with the period taken away
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public DateTime minus(ReadablePeriod period) {
        return withPeriodAdded(period, -1);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new datetime minus the specified number of years.
     * <p>
     * This datetime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * DateTime subtracted = dt.minusYears(6);
     * DateTime subtracted = dt.minus(Period.years(6));
     * DateTime subtracted = dt.withFieldAdded(DurationFieldType.years(), -6);
     * </pre>
     *
     * @param years  the amount of years to subtract, may be negative
     * @return the new datetime minus the increased years
     * @since 1.1
     */
    public DateTime minusYears(int years) {
        if (years == 0) {
            return this;
        }
        long instant = getChronology().years().subtract(getMillis(), years);
        return withMillis(instant);
    }

    /**
     * Returns a new datetime minus the specified number of months.
     * <p>
     * This datetime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * DateTime subtracted = dt.minusMonths(6);
     * DateTime subtracted = dt.minus(Period.months(6));
     * DateTime subtracted = dt.withFieldAdded(DurationFieldType.months(), -6);
     * </pre>
     *
     * @param months  the amount of months to subtract, may be negative
     * @return the new datetime minus the increased months
     * @since 1.1
     */
    public DateTime minusMonths(int months) {
        if (months == 0) {
            return this;
        }
        long instant = getChronology().months().subtract(getMillis(), months);
        return withMillis(instant);
    }

    /**
     * Returns a new datetime minus the specified number of weeks.
     * <p>
     * This datetime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * DateTime subtracted = dt.minusWeeks(6);
     * DateTime subtracted = dt.minus(Period.weeks(6));
     * DateTime subtracted = dt.withFieldAdded(DurationFieldType.weeks(), -6);
     * </pre>
     *
     * @param weeks  the amount of weeks to subtract, may be negative
     * @return the new datetime minus the increased weeks
     * @since 1.1
     */
    public DateTime minusWeeks(int weeks) {
        if (weeks == 0) {
            return this;
        }
        long instant = getChronology().weeks().subtract(getMillis(), weeks);
        return withMillis(instant);
    }

    /**
     * Returns a new datetime minus the specified number of days.
     * <p>
     * This datetime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * DateTime subtracted = dt.minusDays(6);
     * DateTime subtracted = dt.minus(Period.days(6));
     * DateTime subtracted = dt.withFieldAdded(DurationFieldType.days(), -6);
     * </pre>
     *
     * @param days  the amount of days to subtract, may be negative
     * @return the new datetime minus the increased days
     * @since 1.1
     */
    public DateTime minusDays(int days) {
        if (days == 0) {
            return this;
        }
        long instant = getChronology().days().subtract(getMillis(), days);
        return withMillis(instant);
    }

    /**
     * Returns a new datetime minus the specified number of hours.
     * <p>
     * This datetime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * DateTime subtracted = dt.minusHours(6);
     * DateTime subtracted = dt.minus(Period.hours(6));
     * DateTime subtracted = dt.withFieldAdded(DurationFieldType.hours(), -6);
     * </pre>
     *
     * @param hours  the amount of hours to subtract, may be negative
     * @return the new datetime minus the increased hours
     * @since 1.1
     */
    public DateTime minusHours(int hours) {
        if (hours == 0) {
            return this;
        }
        long instant = getChronology().hours().subtract(getMillis(), hours);
        return withMillis(instant);
    }

    /**
     * Returns a new datetime minus the specified number of minutes.
     * <p>
     * This datetime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * DateTime subtracted = dt.minusMinutes(6);
     * DateTime subtracted = dt.minus(Period.minutes(6));
     * DateTime subtracted = dt.withFieldAdded(DurationFieldType.minutes(), -6);
     * </pre>
     *
     * @param minutes  the amount of minutes to subtract, may be negative
     * @return the new datetime minus the increased minutes
     * @since 1.1
     */
    public DateTime minusMinutes(int minutes) {
        if (minutes == 0) {
            return this;
        }
        long instant = getChronology().minutes().subtract(getMillis(), minutes);
        return withMillis(instant);
    }

    /**
     * Returns a new datetime minus the specified number of seconds.
     * <p>
     * This datetime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * DateTime subtracted = dt.minusSeconds(6);
     * DateTime subtracted = dt.minus(Period.seconds(6));
     * DateTime subtracted = dt.withFieldAdded(DurationFieldType.seconds(), -6);
     * </pre>
     *
     * @param seconds  the amount of seconds to subtract, may be negative
     * @return the new datetime minus the increased seconds
     * @since 1.1
     */
    public DateTime minusSeconds(int seconds) {
        if (seconds == 0) {
            return this;
        }
        long instant = getChronology().seconds().subtract(getMillis(), seconds);
        return withMillis(instant);
    }

    /**
     * Returns a new datetime minus the specified number of millis.
     * <p>
     * This datetime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * DateTime subtracted = dt.minusMillis(6);
     * DateTime subtracted = dt.minus(Period.millis(6));
     * DateTime subtracted = dt.withFieldAdded(DurationFieldType.millis(), -6);
     * </pre>
     *
     * @param millis  the amount of millis to subtract, may be negative
     * @return the new datetime minus the increased millis
     * @since 1.1
     */
    public DateTime minusMillis(int millis) {
        if (millis == 0) {
            return this;
        }
        long instant = getChronology().millis().subtract(getMillis(), millis);
        return withMillis(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the property object for the specified type, which contains many useful methods.
     *
     * @param type  the field type to get the chronology for
     * @return the property object
     * @throws IllegalArgumentException if the field is null or unsupported
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

    //-----------------------------------------------------------------------
    /**
     * Converts this object to a DateMidnight using the same millis and chronology.
     * 
     * @return a DateMidnight using the same millis and chronology
     */
    public DateMidnight toDateMidnight() {
        return new DateMidnight(getMillis(), getChronology());
    }

    /**
     * Converts this object to a YearMonthDay using the same millis and chronology.
     * 
     * @return a YearMonthDay using the same millis and chronology
     */
    public YearMonthDay toYearMonthDay() {
        return new YearMonthDay(getMillis(), getChronology());
    }

    /**
     * Converts this object to a TimeOfDay using the same millis and chronology.
     * 
     * @return a TimeOfDay using the same millis and chronology
     */
    public TimeOfDay toTimeOfDay() {
        return new TimeOfDay(getMillis(), getChronology());
    }

    // Date properties
    //-----------------------------------------------------------------------
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
     * 
     * @return the day of month property
     */
    public Property dayOfMonth() {
        return new Property(this, getChronology().dayOfMonth());
    }

    /**
     * Get the day of week property.
     * 
     * @return the day of week property
     */
    public Property dayOfWeek() {
        return new Property(this, getChronology().dayOfWeek());
    }

    // Time properties
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
     * DateTime.Property binds a DateTime to a DateTimeField allowing powerful
     * datetime functionality to be easily accessed.
     * <p>
     * The simplest use of this class is as an alternative get method, here used to
     * get the year '1972' (as an int) and the month 'December' (as a String).
     * <pre>
     * DateTime dt = new DateTime(1972, 12, 3, 0, 0, 0, 0);
     * int year = dt.year().get();
     * String monthStr = dt.month().getAsText();
     * </pre>
     * <p>
     * Methods are also provided that allow date modification. These return new instances
     * of DateTime - they do not modify the original. The example below yields two
     * independent immutable date objects 20 years apart.
     * <pre>
     * DateTime dt = new DateTime(1972, 12, 3, 0, 0, 0, 0);
     * DateTime dt20 = dt.year().addToCopy(20);
     * </pre>
     * Serious modification of dates (ie. more than just changing one or two fields)
     * should use the {@link org.joda.time.MutableDateTime MutableDateTime} class.
     * <p>
     * DateTime.Propery itself is thread-safe and immutable, as well as the
     * DateTime being operated on.
     *
     * @author Stephen Colebourne
     * @author Brian S O'Neill
     * @since 1.0
     */
    public static final class Property extends AbstractReadableInstantFieldProperty {
        
        /** Serialization version */
        private static final long serialVersionUID = -6983323811635733510L;
        
        /** The instant this property is working against */
        private final DateTime iInstant;
        /** The field this property is working against */
        private final DateTimeField iField;
        
        /**
         * Constructor.
         * 
         * @param instant  the instant to set
         * @param field  the field to use
         */
        Property(DateTime instant, DateTimeField field) {
            super();
            iInstant = instant;
            iField = field;
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
         * Gets the datetime being used.
         * 
         * @return the datetime
         */
        public DateTime getDateTime() {
            return iInstant;
        }
        
        //-----------------------------------------------------------------------
        /**
         * Adds to this field in a copy of this DateTime.
         * <p>
         * The DateTime attached to this property is unchanged by this call.
         * This operation is faster than converting a DateTime to a MutableDateTime
         * and back again when setting one field. When setting multiple fields,
         * it is generally quicker to make the conversion to MutableDateTime.
         * 
         * @param value  the value to add to the field in the copy
         * @return a copy of the DateTime with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public DateTime addToCopy(int value) {
            return iInstant.withMillis(iField.add(iInstant.getMillis(), value));
        }
        
        /**
         * Adds to this field in a copy of this DateTime.
         * <p>
         * The DateTime attached to this property is unchanged by this call.
         * This operation is faster than converting a DateTime to a MutableDateTime
         * and back again when setting one field. When setting multiple fields,
         * it is generally quicker to make the conversion to MutableDateTime.
         * 
         * @param value  the value to add to the field in the copy
         * @return a copy of the DateTime with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public DateTime addToCopy(long value) {
            return iInstant.withMillis(iField.add(iInstant.getMillis(), value));
        }
        
        /**
         * Adds to this field, possibly wrapped, in a copy of this DateTime.
         * A wrapped operation only changes this field.
         * Thus 31st January addWrapField one day goes to the 1st January.
         * <p>
         * The DateTime attached to this property is unchanged by this call.
         * This operation is faster than converting a DateTime to a MutableDateTime
         * and back again when setting one field. When setting multiple fields,
         * it is generally quicker to make the conversion to MutableDateTime.
         * 
         * @param value  the value to add to the field in the copy
         * @return a copy of the DateTime with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public DateTime addWrapFieldToCopy(int value) {
            return iInstant.withMillis(iField.addWrapField(iInstant.getMillis(), value));
        }
        
        //-----------------------------------------------------------------------
        /**
         * Sets this field in a copy of the DateTime.
         * <p>
         * The DateTime attached to this property is unchanged by this call.
         * This operation is faster than converting a DateTime to a MutableDateTime
         * and back again when setting one field. When setting multiple fields,
         * it is generally quicker to make the conversion to MutableDateTime.
         * 
         * @param value  the value to set the field in the copy to
         * @return a copy of the DateTime with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public DateTime setCopy(int value) {
            return iInstant.withMillis(iField.set(iInstant.getMillis(), value));
        }
        
        /**
         * Sets this field in a copy of the DateTime to a parsed text value.
         * <p>
         * The DateTime attached to this property is unchanged by this call.
         * This operation is faster than converting a DateTime to a MutableDateTime
         * and back again when setting one field. When setting multiple fields,
         * it is generally quicker to make the conversion to MutableDateTime.
         * 
         * @param text  the text value to set
         * @param locale  optional locale to use for selecting a text symbol
         * @return a copy of the DateTime with the field value changed
         * @throws IllegalArgumentException if the text value isn't valid
         */
        public DateTime setCopy(String text, Locale locale) {
            return iInstant.withMillis(iField.set(iInstant.getMillis(), text, locale));
        }
        
        /**
         * Sets this field in a copy of the DateTime to a parsed text value.
         * <p>
         * The DateTime attached to this property is unchanged by this call.
         * This operation is faster than converting a DateTime to a MutableDateTime
         * and back again when setting one field. When setting multiple fields,
         * it is generally quicker to make the conversion to MutableDateTime.
         * 
         * @param text  the text value to set
         * @return a copy of the DateTime with the field value changed
         * @throws IllegalArgumentException if the text value isn't valid
         */
        public DateTime setCopy(String text) {
            return setCopy(text, null);
        }
        
        //-----------------------------------------------------------------------
        /**
         * Rounds to the lowest whole unit of this field on a copy of this DateTime.
         *
         * @return a copy of the DateTime with the field value changed
         */
        public DateTime roundFloorCopy() {
            return iInstant.withMillis(iField.roundFloor(iInstant.getMillis()));
        }
        
        /**
         * Rounds to the highest whole unit of this field on a copy of this DateTime.
         *
         * @return a copy of the DateTime with the field value changed
         */
        public DateTime roundCeilingCopy() {
            return iInstant.withMillis(iField.roundCeiling(iInstant.getMillis()));
        }
        
        /**
         * Rounds to the nearest whole unit of this field on a copy of this DateTime,
         * favoring the floor if halfway.
         *
         * @return a copy of the DateTime with the field value changed
         */
        public DateTime roundHalfFloorCopy() {
            return iInstant.withMillis(iField.roundHalfFloor(iInstant.getMillis()));
        }
        
        /**
         * Rounds to the nearest whole unit of this field on a copy of this DateTime,
         * favoring the ceiling if halfway.
         *
         * @return a copy of the DateTime with the field value changed
         */
        public DateTime roundHalfCeilingCopy() {
            return iInstant.withMillis(iField.roundHalfCeiling(iInstant.getMillis()));
        }
        
        /**
         * Rounds to the nearest whole unit of this field on a copy of this
         * DateTime.  If halfway, the ceiling is favored over the floor only if
         * it makes this field's value even.
         *
         * @return a copy of the DateTime with the field value changed
         */
        public DateTime roundHalfEvenCopy() {
            return iInstant.withMillis(iField.roundHalfEven(iInstant.getMillis()));
        }
    }

}
