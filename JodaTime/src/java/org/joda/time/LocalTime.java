/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2005 Stephen Colebourne.  
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

import org.joda.time.base.AbstractLocal;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.InstantConverter;
import org.joda.time.field.AbstractMillisFieldProperty;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;

/**
 * LocalTime represents an immutable local time, without time zone or date.
 * A good use of this class would be to store the time at which you start work,
 * where typically you don't care about the time zone or date.
 * <p>
 * The date fields are not present and cannot be queried on a LocalTime.
 * Calculations on LocalTime are performed using a {@link Chronology}.
 * This chronology is set to be in the UTC time zone for all calculations.
 * <p>
 * Each individual field can be queried in two ways:
 * <ul>
 * <li><code>getMinuteOfHour()</code>
 * <li><code>minuteOfHour().get()</code>
 * </ul>
 * The second technique also provides access to other useful methods on the
 * field:
 * <ul>
 * <li>numeric value - <code>minuteOfHour().get()</code>
 * <li>text value - <code>minuteOfHour().getAsText()</code>
 * <li>short text value - <code>minuteOfHour().getAsShortText()</code>
 * <li>maximum/minimum values - <code>minuteOfHour().getMaximumValue()</code>
 * <li>add/subtract - <code>minuteOfHour().addToCopy()</code>
 * <li>set - <code>minuteOfHour().setCopy()</code>
 * </ul>
 * <p>
 * LocalTime is thread-safe and immutable, provided that the Chronology is as well.
 * All standard Chronology classes supplied are thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public final class LocalTime
        extends AbstractLocal
        implements ReadableLocal, Serializable, Comparable {

    /** Serialization version */
    private static final long serialVersionUID = 36131981638918L;

    /** The local millis from 1970-01-01T00:00:00Z */
    private final long iLocalMillis;
    /** The chronology to use */
    private final Chronology iChronology;

    //-----------------------------------------------------------------------
    /**
     * Constructs a LocalTime from the specified millis of day using the
     * ISO chronology.
     * <p>
     * The millisOfDay value may exceed the number of millis in one day,
     * but additional days will be ignored.
     * This method uses the UTC time zone internally.
     *
     * @param millisOfDay  the number of milliseconds into a day to convert
     */
    public static LocalTime fromMillisOfDay(long millisOfDay) {
        return fromMillisOfDay(millisOfDay, null);
    }

    /**
     * Constructs a LocalTime from the specified millis of day using the
     * specified chronology.
     * <p>
     * The millisOfDay value may exceed the number of millis in one day,
     * but additional days will be ignored.
     * This method uses the UTC time zone internally.
     *
     * @param millisOfDay  the number of milliseconds into a day to convert
     * @param chrono  the chronology, null means ISO chronology
     */
    public static LocalTime fromMillisOfDay(long millisOfDay, Chronology chrono) {
        chrono = DateTimeUtils.getChronology(chrono);
        chrono = chrono.withUTC();
        return new LocalTime(millisOfDay, chrono);
    }

    // Constructors
    //-----------------------------------------------------------------------
    /**
     * Constructs a LocalTime with the current time, using ISOChronology in
     * the default zone to extract the fields.
     * <p>
     * The constructor uses the default time zone, resulting in the local time
     * being initialised. Once the constructor is complete, all further calculations
     * are performed without reference to a time zone (by switching to UTC).
     */
    public LocalTime() {
        this(DateTimeUtils.currentTimeMillis(), null);
    }

    /**
     * Constructs a LocalTime with the current time, using the specified chronology
     * and zone to extract the fields.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a time zone (by switching to UTC).
     *
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public LocalTime(Chronology chronology) {
        this(DateTimeUtils.currentTimeMillis(), chronology);
    }

    /**
     * Constructs a LocalTime extracting the partial fields from the specified
     * milliseconds using the ISOChronology in the default zone.
     * <p>
     * The constructor uses the default time zone, resulting in the local time
     * being initialised. Once the constructor is complete, all further calculations
     * are performed without reference to a time zone (by switching to UTC).
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     */
    public LocalTime(long instant) {
        this(instant, null);
    }

    /**
     * Constructs a LocalTime extracting the partial fields from the specified
     * milliseconds using the chronology provided.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a time zone (by switching to UTC).
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public LocalTime(long instant, Chronology chronology) {
        super();
        chronology = DateTimeUtils.getChronology(chronology);
        iChronology = chronology.withUTC();
        iLocalMillis = adjustInstant(instant, chronology);
    }

    /**
     * Constructs a LocalTime from an Object that represents a time.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     * <p>
     * The chronology used will be derived from the object, defaulting to ISO.
     *
     * @param instant  the datetime object, null means now
     * @throws IllegalArgumentException if the instant is invalid
     */
    public LocalTime(Object instant) {
        this(instant, null);
    }

    /**
     * Constructs a LocalTime from an Object that represents a time, using the
     * specified chronology.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     * The specified chronology overrides that of the object.
     *
     * @param instant  the datetime object, null means now
     * @param chronology  the chronology, null means ISO default
     * @throws IllegalArgumentException if the instant is invalid
     */
    public LocalTime(Object instant, Chronology chronology) {
        super();
        InstantConverter converter = ConverterManager.getInstance().getInstantConverter(instant);
        Chronology chrono = DateTimeUtils.getChronology(converter.getChronology(instant, chronology));
        long millis = converter.getInstantMillis(instant, chronology);
        iChronology = chrono.withUTC();
        iLocalMillis = adjustInstant(millis, chrono);
    }

    /**
     * Constructs a LocalTime with specified time field values
     * using <code>ISOChronology</code> in the default zone.
     * <p>
     * The constructor uses the chronology specified, but ignores any time zone
     * initialising the fields as provided.
     * Once the constructor is complete, all further calculations
     * are performed without reference to a timezone (by switching to UTC).
     *
     * @param hourOfDay  the hour of the day
     * @param minuteOfHour  the minute of the hour
     */
    public LocalTime(int hourOfDay, int minuteOfHour) {
        this(hourOfDay, minuteOfHour, 0, 0, null);
    }

    /**
     * Constructs a LocalTime with specified time field values.
     * <p>
     * The constructor uses the chronology specified, but ignores any time zone
     * initialising the fields as provided.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     *
     * @param hourOfDay  the hour of the day
     * @param minuteOfHour  the minute of the hour
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public LocalTime(int hourOfDay, int minuteOfHour, Chronology chronology) {
        this(hourOfDay, minuteOfHour, 0, 0, chronology);
    }

    /**
     * Constructs a LocalTime with specified time field values
     * using <code>ISOChronology</code> in the default zone.
     * <p>
     * The constructor uses the chronology specified, but ignores any time zone
     * initialising the fields as provided.
     * Once the constructor is complete, all further calculations
     * are performed without reference to a timezone (by switching to UTC).
     *
     * @param hourOfDay  the hour of the day
     * @param minuteOfHour  the minute of the hour
     * @param secondOfMinute  the second of the minute
     */
    public LocalTime(int hourOfDay, int minuteOfHour, int secondOfMinute) {
        this(hourOfDay, minuteOfHour, secondOfMinute, 0, null);
    }

    /**
     * Constructs a LocalTime with specified time field values.
     * <p>
     * The constructor uses the chronology specified, but ignores any time zone
     * initialising the fields as provided.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     *
     * @param hourOfDay  the hour of the day
     * @param minuteOfHour  the minute of the hour
     * @param secondOfMinute  the second of the minute
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public LocalTime(int hourOfDay, int minuteOfHour, int secondOfMinute, Chronology chronology) {
        this(hourOfDay, minuteOfHour, secondOfMinute, 0, chronology);
    }

    /**
     * Constructs a LocalTime with specified time field values
     * using <code>ISOChronology</code> in the default zone.
     * <p>
     * The constructor uses the chronology specified, but ignores any time zone
     * initialising the fields as provided.
     * Once the constructor is complete, all further calculations
     * are performed without reference to a timezone (by switching to UTC).
     *
     * @param hourOfDay  the hour of the day
     * @param minuteOfHour  the minute of the hour
     * @param secondOfMinute  the second of the minute
     * @param millisOfSecond  the millisecond of the second
     */
    public LocalTime(int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond) {
        this(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond, null);
    }

    /**
     * Constructs a LocalTime with specified time field values.
     * <p>
     * The constructor uses the chronology specified, but ignores any time zone
     * initialising the fields as provided.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     *
     * @param hourOfDay  the hour of the day
     * @param minuteOfHour  the minute of the hour
     * @param secondOfMinute  the second of the minute
     * @param millisOfSecond  the millisecond of the second
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public LocalTime(int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond, Chronology chronology) {
        super();
        iChronology = DateTimeUtils.getChronology(chronology).withUTC();
        iLocalMillis = iChronology.getDateTimeMillis(0L, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    /**
     * Checks the input instant and adusts it to be local and time only.
     * 
     * @param instant  the instant to adjust
     * @param chronology  the chronology that the instant is currently in
     * @return the adjusted local millis
     */
    private long adjustInstant(long instant, Chronology chronology) {
        instant = chronology.getZone().getMillisKeepLocal(DateTimeZone.UTC, instant);
        return chronology.withUTC().millisOfDay().get(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the amount of time this instance represents.
     * <p>
     * The value returned from this method will be relative to the epoch
     * returned by {@link #getEpoch()} and in the units of {@link #getUnitDurationType()}.
     * <p>
     * Currently, this is a UTC milliseconds value with the date set to 1970-01-01.
     * However, you should not rely on that remaining true and always refer to
     * this method with respect to the Epoch and Unit duration.
     *
     * @return the amount of time this local represents
     */
    public long getAmount() {
        return iLocalMillis;
    }

    /**
     * Gets the length of each unit that this instance uses for the amount.
     * <p>
     * A <code>ReadableLocal</code> measures time in units defined by this method.
     * The result of {@link #getAmount()} must be interpretted in terms of these units.
     * <p>
     * Currently, this returns milliseconds.
     *
     * @return the duration of each unit of time this local represents
     */
    public DurationFieldType getUnitDurationType() {
        return DurationFieldType.millis();
    }

    /**
     * Gets the epoch that this instance uses.
     * <p>
     * A <code>ReadableLocal</code> measures time in units whose duration is
     * defined by {@link #getUnitDurationType()}. This method returns the zero point,
     * allowing conversion between a <code>ReadableLocal</code> and a
     * <code>ReadableInstant</code>.
     *
     * @return the epoch that this instance measures against
     */
    public DateTime getEpoch() {
        return DateTime.EPOCH;
    }

    /**
     * Gets the chronology of the datetime.
     * <p>
     * This will always return a chronology in UTC, as UTC is used to represent
     * local times.
     * 
     * @return the Chronology that the datetime is using
     */
    public Chronology getChronology() {
        return iChronology;
    }

    //-----------------------------------------------------------------------
    /**
     * Checks whether the field specified is supported by this date.
     *
     * @param type  the type to check, may be null which returns false
     * @return true if the field is supported
     */
    public boolean isSupported(DateTimeFieldType type) {
        if (type == null) {
            return false;
        }
        DateTimeField field = type.getField(getChronology());
        return isSupported(field);
    }

    /**
     * Get the value of one of the fields of a datetime.
     * <p>
     * The field specified must be one of those that is supported by the date.
     *
     * @param type  a DateTimeFieldType instance that is supported by this date
     * @return the value of that field
     * @throws IllegalArgumentException if the field is null or not supported
     */
    public int get(DateTimeFieldType type) {
        DateTimeField field = getDateTimeField(type);
        return field.get(getAmount());
    }

    /**
     * Gets the property object for the specified type, which contains many useful methods.
     *
     * @param type  the field type to get the chronology for
     * @return the property object
     * @throws IllegalArgumentException if the field is null or unsupported
     */
    public Property property(DateTimeFieldType type) {
        DateTimeField field = getDateTimeField(type);
        return new Property(this, field);
    }

    /**
     * Gets a DateTimeField for the specified type.
     * 
     * @param type  the type to query
     * @return the field
     * @throws IllegalArgumentException if the type is not supported
     */
    private DateTimeField getDateTimeField(DateTimeFieldType type) {
        if (type == null) {
            throw new IllegalArgumentException("The DateTimeFieldType must not be null");
        }
        DateTimeField field = type.getField(getChronology());
        if (isSupported(field) == false) {
            throw new IllegalArgumentException("Field '" + type + "' is not supported");
        }
        return field;
    }

    /**
     * Checks whether the field specified is supported by this instance.
     *
     * @param field  the field to check, may be null which returns false
     * @return true if the field is supported
     */
    private boolean isSupported(DateTimeField field) {
        if (field.isSupported() == false) {
            return false;
        }
        return isSupported(field.getDurationField());
    }

    /**
     * Checks whether the field specified is supported by this instance.
     *
     * @param field  the field to check, may be null which returns false
     * @return true if the field is supported
     */
    private boolean isSupported(DurationField field) {
        if (field.isSupported() == false) {
            return false;
        }
        return field.getUnitMillis() < getChronology().days().getUnitMillis();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this instance with a different time amount.
     * <p>
     * The returned object will be a new instance of LocalTime.
     * Only the amount will change, the chronology is kept.
     * The returned object will be either be a new instance or <code>this</code>.
     *
     * @param newMillis  the new millis, from 1970-01-01T00:00:00Z
     * @return a copy of this datetime with different millis
     */
    LocalTime withAmount(long newMillis) {
        return (newMillis == getAmount() ? this : new LocalTime(newMillis, getChronology()));
    }

    /**
     * Creates a new LocalTime instance with the specified chronology.
     * This instance is immutable and unaffected by this method call.
     * <p>
     * This method retains the values of the fields, thus the result will
     * typically refer to a different instant.
     * <p>
     * The time zone of the specified chronology is ignored, as TimeOfDay
     * operates without a time zone.
     *
     * @param newChronology  the new chronology, null means ISO
     * @return a copy of this datetime with a different chronology
     */
    public LocalTime withChronologyRetainFields(Chronology newChronology) {
        newChronology = DateTimeUtils.getChronology(newChronology);
        newChronology = newChronology.withUTC();
        if (newChronology == getChronology()) {
            return this;
        } else {
            return new LocalTime(getHourOfDay(), getMinuteOfHour(), getSecondOfMinute(), getMillisOfSecond(), newChronology);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this instance with the specified field set to a new value.
     * <p>
     * For example, if the field type is <code>hourOfDay</code> then the hour of day
     * field would be changed in the returned instance.
     * If the field type is null, then <code>this</code> is returned.
     * <p>
     * These three lines are equivalent:
     * <pre>
     * DateTime updated = dt.withField(DateTimeFieldType.hourOfDay(), 6);
     * DateTime updated = dt.hourOfDay().setCopy(6);
     * DateTime updated = dt.property(DateTimeFieldType.hourOfDay()).setCopy(6);
     * </pre>
     *
     * @param fieldType  the field type to set, not null
     * @param value  the value to set
     * @return a copy of this instance with the field set
     * @throws IllegalArgumentException if the value is null or invalid
     */
    public LocalTime withField(DateTimeFieldType fieldType, int value) {
        DateTimeField field = getDateTimeField(fieldType);
        long instant = field.set(getAmount(), value);
        return withAmount(instant);
    }

    /**
     * Gets a copy of this date with the value of the specified field increased.
     * <p>
     * If the addition is zero or the field is null, then <code>this</code> is returned.
     * If the addition exceeds the end of the day (normally 23:59), then it will
     * wrap around. Thus 15:30 + 10 hours = 01:30.
     * <p>
     * These three lines are equivalent:
     * <pre>
     * DateTime added = dt.withField(DateTimeFieldType.hourOfDay(), 6);
     * DateTime added = dt.hourOfDay().addToCopy(6);
     * DateTime added = dt.property(DateTimeFieldType.hourOfDay()).addToCopy(6);
     * </pre>
     * 
     * @param fieldType  the field type to add to, not null
     * @param amount  the amount to add
     * @return a copy of this instance with the field updated
     * @throws IllegalArgumentException if the value is null or invalid
     */
    public LocalTime withFieldAdded(DurationFieldType fieldType, int amount) {
        if (fieldType == null) {
            throw new IllegalArgumentException("Field must not be null");
        }
        DurationField field = fieldType.getField(getChronology());
        if (isSupported(field) == false) {
            throw new IllegalArgumentException("Field '" + fieldType + "' is not supported");
        }
        if (amount == 0) {
            return this;
        }
        long instant = field.add(getAmount(), amount);
        return withAmount(instant);
    }

    /**
     * Gets a copy of this date with the specified period added.
     * <p>
     * If the addition is zero, then <code>this</code> is returned.
     * If the addition exceeds the end of the day (normally 23:59), then it will
     * wrap around. Thus 15:30 + 10 hours = 01:30.
     * <p>
     * To add or subtract on a single field use the properties, for example:
     * <pre>
     * DateTime added = dt.hourOfDay().addToCopy(6);
     * </pre>
     * 
     * @param period  the period to add to this one, null means zero
     * @param scalar  the amount of times to add, such as -1 to subtract once
     * @return a copy of this instance with the period added
     */
    public LocalTime withPeriodAdded(ReadablePeriod period, int scalar) {
        if (period == null || scalar == 0) {
            return this;
        }
        long instant = getChronology().add(period, getAmount(), scalar);
        return withAmount(instant);
    }

    /**
     * Gets a copy of this instance with the specified period added.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * If the addition exceeds the end of the day (normally 23:59), then it will
     * wrap around. Thus 15:30 + 10 hours = 01:30.
     * <p>
     * The following two lines are identical in effect:
     * <pre>
     * DateTime added = dt.hourOfDay().addToCopy(6);
     * DateTime added = dt.plus(Period.days(6));
     * </pre>
     * 
     * @param period  the duration to add to this one, null means zero
     * @return a copy of this instance with the period added
     */
    public LocalTime plus(ReadablePeriod period) {
        return withPeriodAdded(period, 1);
    }

    /**
     * Gets a copy of this instance with the specified period take away.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * If the addition exceeds the end of the day (normally 23:59), then it will
     * wrap around. Thus 01:30 - 10 hours = 15:30.
     * <p>
     * The following two lines are identical in effect:
     * <pre>
     * DateTime added = dt.hourOfDay().addToCopy(-6);
     * DateTime added = dt.minus(Period.days(6));
     * </pre>
     * 
     * @param period  the period to reduce this instant by
     * @return a copy of this instance with the period taken away
     */
    public LocalTime minus(ReadablePeriod period) {
        return withPeriodAdded(period, -1);
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this LocalTime to a full DateTime using todays date and the
     * default time zone.
     *
     * @return the combination of todays date and this local time
     */
    public DateTime toDateTimeToday() {
        return toDateTimeToday(null);
    }

    /**
     * Converts this LocalTime to a full DateTime using todays date and the
     * specified time zone.
     * <p>
     * This method uses the chronology from this instance plus the time zone
     * specified.
     *
     * @param zone  the zone to use, null means default
     * @return the combination of todays date and this local time
     */
    public DateTime toDateTimeToday(DateTimeZone zone) {
        Chronology chrono = getChronology().withZone(zone);
        DateTime now = new DateTime(chrono);
        return now.withTime(getHourOfDay(), getMinuteOfHour(), getSecondOfMinute(), getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this ReadableLocal to a full DateTime by resolving it against
     * another datetime.
     * <p>
     * This method takes the specified datetime and sets the fields from this
     * LocalTime on top. The chronology (including timezone) from the base
     * instant is used.
     * <p>
     * Thus the result will be the date and zone from the specified instant
     * combined with the time from this LocalTime.
     *
     * @param baseInstant  the instant that provides the missing fields, null means now
     * @return the combined datetime
     */
    public DateTime toDateTime(ReadableInstant baseInstant) {
        Chronology chrono = DateTimeUtils.getInstantChronology(baseInstant);
        long instant = DateTimeUtils.getInstantMillis(baseInstant);
        instant = chrono.hourOfDay().set(instant, getHourOfDay());
        instant = chrono.minuteOfHour().set(instant, getMinuteOfHour());
        instant = chrono.secondOfMinute().set(instant, getSecondOfMinute());
        instant = chrono.millisOfSecond().set(instant, getMillisOfSecond());
        return new DateTime(instant, chrono);
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this object to a DateTime using a LocalDate to fill in the
     * missing fields and using the default time zone.
     * This instance is immutable and unaffected by this method call.
     * <p>
     * The resulting chronology is determined by the chronology of this
     * LocalTime plus the default time zone.
     * The chronology of the date is ignored - only the field values are used.
     *
     * @param date  the date to use, null means current date
     * @return the DateTime instance
     */
    public DateTime toDateTime(LocalDate date) {
        return toDateTime(date, null);
    }

    /**
     * Converts this object to a DateTime using a LocalDate to fill in the
     * missing fields.
     * This instance is immutable and unaffected by this method call.
     * <p>
     * The resulting chronology is determined by the chronology of this
     * LocalTime plus the time zone.
     * The chronology of the date is ignored - only the field values are used.
     *
     * @param date  the date to use, null means current date
     * @param zone  the zone to get the DateTime in, null means default
     * @return the DateTime instance
     */
    public DateTime toDateTime(LocalDate date, DateTimeZone zone) {
        Chronology chrono = getChronology().withZone(zone);
        if (date == null) {
            date = new LocalDate(chrono);
        }
        return new DateTime(
                date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(),
                getHourOfDay(), getMinuteOfHour(), getSecondOfMinute(), getMillisOfSecond(),
                chrono);
    }

    //-----------------------------------------------------------------------
    /**
     * Get the hour of day field value.
     *
     * @return the hour of day
     */
    public int getHourOfDay() {
        return getChronology().hourOfDay().get(getAmount());
    }

    /**
     * Get the minute of day field value.
     *
     * @return the minute of day
     */
    public int getMinuteOfDay() {
        return getChronology().minuteOfDay().get(getAmount());
    }

    /**
     * Get the minute of hour field value.
     *
     * @return the minute of hour
     */
    public int getMinuteOfHour() {
        return getChronology().minuteOfHour().get(getAmount());
    }

    /**
     * Get the second of day field value.
     *
     * @return the second of day
     */
    public int getSecondOfDay() {
        return getChronology().secondOfDay().get(getAmount());
    }

    /**
     * Get the second of minute field value.
     *
     * @return the second of minute
     */
    public int getSecondOfMinute() {
        return getChronology().secondOfMinute().get(getAmount());
    }

    /**
     * Get the millis of day field value.
     *
     * @return the millis of day
     */
    public int getMillisOfDay() {
        return getChronology().millisOfDay().get(getAmount());
    }

    /**
     * Get the millis of second field value.
     *
     * @return the millis of second
     */
    public int getMillisOfSecond() {
        return getChronology().millisOfSecond().get(getAmount());
    }

    //-----------------------------------------------------------------------
    /**
     * Get the hour of day property
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
     * Compares this instance with the specified object for ascending
     * time order.
     * <p>
     * The comparison will only succeed if the object is a LocalTime with
     * the same Chronology.
     *
     * @param localTime  a local time to check against
     * @return negative value if this is less, 0 if equal, or positive value if greater
     * @throws NullPointerException if the object is null
     * @throws ClassCastException if the object type is not supported
     * @throws IllegalArgumentException if the object cannot be compared
     */
    public int compareTo(Object localTime) {
        LocalTime other = (LocalTime) localTime;
        if (other.getChronology() != getChronology()) {
            throw new IllegalArgumentException("The LocalTime cannot be compared");
        }
        long thisAmount = getAmount();
        long otherAmount = other.getAmount();
        if (thisAmount > otherAmount) {
            return 1;
        } else if (thisAmount < otherAmount) {
            return -1;
        } else {
            return 0;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Is this instance after the current time (ignoring the date).
     * <p>
     * The comparison is time based and ignores the date aspect of the
     * current time. The current time is obtained in the default time zone.
     *
     * @return true if this instance is after the current time
     */
    public boolean isAfterNow() {
        long now = DateTimeUtils.currentTimeMillis();
        now = adjustInstant(now, Chronology.getISO());
        return (getAmount() > now);
    }

    /**
     * Is this instance after the time passed in.
     * <p>
     * The comparison will only succeed if the object is a LocalTime with
     * the same Chronology.
     *
     * @param local  a local to check against, null means now
     * @return true if this instance is after the one passed in
     * @throws IllegalArgumentException if the object cannot be compared
     */
    public boolean isAfter(LocalTime local) {
        if (local == null) {
            return isAfterNow();
        }
        if (local.getChronology() != getChronology()) {
            throw new IllegalArgumentException("The LocalTime cannot be compared");
        }
        return (getAmount() > local.getAmount());
    }

    /**
     * Is this instance before the current time (ignoring the date).
     * <p>
     * The comparison is time based and ignores the date aspect of the
     * current time. The current time is obtained in the default time zone.
     *
     * @return true if this instance is before the current time
     */
    public boolean isBeforeNow() {
        long now = DateTimeUtils.currentTimeMillis();
        now = adjustInstant(now, Chronology.getISO());
        return (getAmount() < now);
    }

    /**
     * Is this instance before the time passed in.
     * <p>
     * The comparison will only succeed if the object is a LocalTime with
     * the same Chronology.
     *
     * @param local  a local to check against, null means now
     * @return true if this instance is before the one passed in
     * @throws IllegalArgumentException if the object cannot be compared
     */
    public boolean isBefore(LocalTime local) {
        if (local == null) {
            return isBeforeNow();
        }
        if (local.getChronology() != getChronology()) {
            throw new IllegalArgumentException("The LocalTime cannot be compared");
        }
        return (getAmount() < local.getAmount());
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this object with the specified object for equality based
     * on the local time and the Chronology.
     * <p>
     * Only LocalTime instances are accepted.
     *
     * @param localTime  a readable instant to check against
     * @return true if local time and chronology are equal, false if
     *  not or the localDate is null or of an incorrect type
     */
    public boolean equals(Object localTime) {
        if (this == localTime) {
            return true;
        }
        if (localTime instanceof LocalTime) {
            LocalTime otherTime = (LocalTime) localTime;
            if (getAmount() == otherTime.getAmount()) {
                Chronology chrono = getChronology();
                if (chrono == otherTime.getChronology()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets a hash code for the this date object.
     *
     * @return a suitable hash code
     */
    public int hashCode() {
        return
            ((int) (getAmount() ^ (getAmount() >>> 32))) +
            (getChronology().hashCode());
    }

    //-----------------------------------------------------------------------
    /**
     * Output the time in the ISO8601 format THH:mm:ss.SSS.
     * 
     * @return ISO8601 formatted string
     */
    public String toString() {
        return ISODateTimeFormat.getInstance().tTime().print(
            getAmount(), getChronology());
    }

    /**
     * Output the instant using the specified format pattern.
     *
     * @param pattern  the pattern specification, null means use <code>toString</code>
     * @see  org.joda.time.format.DateTimeFormat
     */
    public String toString(String pattern) {
        if (pattern == null) {
            return toString();
        }
        return DateTimeFormat.getInstance().forPattern(pattern).print(
            getAmount(), getChronology());
    }

    /**
     * Output the instant using the specified format pattern.
     *
     * @param pattern  the pattern specification, null means use <code>toString</code>
     * @param locale  Locale to use, null means default
     * @see  org.joda.time.format.DateTimeFormat
     */
    public String toString(String pattern, Locale locale) throws IllegalArgumentException {
        if (pattern == null) {
            return toString();
        }
        return DateTimeFormat.getInstance(locale).forPattern(pattern).print(
            getAmount(), getChronology());
    }

    //-----------------------------------------------------------------------
    /**
     * LocalTime.Property binds a LocalTime to a DateTimeField allowing powerful
     * datetime functionality to be easily accessed.
     * <p>
     * The simplest use of this class is as an alternative get method, here used to
     * get the year '1972' (as an int) and the month 'December' (as a String).
     * <pre>
     * LocalTime dt = new LocalTime(1972, 12, 3, 0, 0, 0, 0);
     * int year = dt.hourOfDay().get();
     * String minuteStr = dt.minuteOfHour().getAsText();
     * </pre>
     * <p>
     * Methods are also provided that allow date modification. These return
     * new instances of LocalTime - they do not modify the original. The example
     * below yields two independent immutable date objects 20 years apart.
     * <pre>
     * LocalTime dt = new LocalTime(1972, 12, 3);
     * LocalTime dt20 = dt.hourOfDay().addToCopy(20);
     * </pre>
     * LocalTime.Propery itself is thread-safe and immutable, as well as the
     * LocalTime being operated on.
     *
     * @author Stephen Colebourne
     * @author Brian S O'Neill
     * @since 1.0
     */
    public static final class Property extends AbstractMillisFieldProperty {
        
        /** Serialization version */
        private static final long serialVersionUID = -12591766889L;
        
        /** The time this property is working against */
        private final LocalTime iTime;
        /** The field this property is working against */
        private final DateTimeField iField;
        
        /**
         * Constructor.
         * 
         * @param time  the time to use
         * @param field  the field to use
         */
        Property(LocalTime time, DateTimeField field) {
            super();
            iTime = time;
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
         * Gets the milliseconds of the local time that this property is linked to.
         * 
         * @return the milliseconds
         */
        protected long getMillis() {
            return iTime.getAmount();
        }

        /**
         * Gets the time being used.
         * 
         * @return the date
         */
        public LocalTime getLocalTime() {
            return iTime;
        }

        //-----------------------------------------------------------------------
        /**
         * Adds to this field in a copy of this LocalTime.
         * <p>
         * The LocalTime attached to this property is unchanged by this call.
         * 
         * @param value  the value to add to the field in the copy
         * @return a copy of the LocalTime with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public LocalTime addToCopy(int value) {
            return iTime.withAmount(iField.add(iTime.getAmount(), value));
        }
        
        /**
         * Adds to this field in a copy of this LocalTime.
         * If necessary, the add wraps within the day.
         * <p>
         * The LocalTime attached to this property is unchanged by this call.
         * 
         * @param value  the value to add to the field in the copy
         * @return a copy of the LocalTime with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public LocalTime addToCopy(long value) {
            return iTime.withAmount(iField.add(iTime.getAmount(), value));
        }
        
        /**
         * Adds to this field, possibly wrapped, in a copy of this LocalTime.
         * A wrapped operation only changes this field.
         * Thus 31st January addWrapField one day goes to the 1st January.
         * <p>
         * The LocalTime attached to this property is unchanged by this call.
         * 
         * @param value  the value to add to the field in the copy
         * @return a copy of the LocalTime with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public LocalTime addWrapFieldToCopy(int value) {
            return iTime.withAmount(iField.addWrapField(iTime.getAmount(), value));
        }
        
        //-----------------------------------------------------------------------
        /**
         * Sets this field in a copy of the LocalTime.
         * <p>
         * The LocalTime attached to this property is unchanged by this call.
         * 
         * @param value  the value to set the field in the copy to
         * @return a copy of the LocalTime with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public LocalTime setCopy(int value) {
            return iTime.withAmount(iField.set(iTime.getAmount(), value));
        }
        
        /**
         * Sets this field in a copy of the LocalTime to a parsed text value.
         * <p>
         * The LocalTime attached to this property is unchanged by this call.
         * 
         * @param text  the text value to set
         * @param locale  optional locale to use for selecting a text symbol
         * @return a copy of the LocalTime with the field value changed
         * @throws IllegalArgumentException if the text value isn't valid
         */
        public LocalTime setCopy(String text, Locale locale) {
            return iTime.withAmount(iField.set(iTime.getAmount(), text, locale));
        }
        
        /**
         * Sets this field in a copy of the LocalTime to a parsed text value.
         * <p>
         * The LocalTime attached to this property is unchanged by this call.
         * 
         * @param text  the text value to set
         * @return a copy of the LocalTime with the field value changed
         * @throws IllegalArgumentException if the text value isn't valid
         */
        public LocalTime setCopy(String text) {
            return setCopy(text, null);
        }
        
        //-----------------------------------------------------------------------
        /**
         * Rounds to the lowest whole unit of this field on a copy of this LocalTime.
         *
         * @return a copy of the LocalTime with the field value changed
         */
        public LocalTime roundFloorCopy() {
            return iTime.withAmount(iField.roundFloor(iTime.getAmount()));
        }
        
        /**
         * Rounds to the highest whole unit of this field on a copy of this LocalTime.
         *
         * @return a copy of the LocalTime with the field value changed
         */
        public LocalTime roundCeilingCopy() {
            return iTime.withAmount(iField.roundCeiling(iTime.getAmount()));
        }
        
        /**
         * Rounds to the nearest whole unit of this field on a copy of this LocalTime,
         * favoring the floor if halfway.
         *
         * @return a copy of the LocalTime with the field value changed
         */
        public LocalTime roundHalfFloorCopy() {
            return iTime.withAmount(iField.roundHalfFloor(iTime.getAmount()));
        }
        
        /**
         * Rounds to the nearest whole unit of this field on a copy of this LocalTime,
         * favoring the ceiling if halfway.
         *
         * @return a copy of the LocalTime with the field value changed
         */
        public LocalTime roundHalfCeilingCopy() {
            return iTime.withAmount(iField.roundHalfCeiling(iTime.getAmount()));
        }
        
        /**
         * Rounds to the nearest whole unit of this field on a copy of this
         * LocalTime.  If halfway, the ceiling is favored over the floor only if
         * it makes this field's value even.
         *
         * @return a copy of the LocalTime with the field value changed
         */
        public LocalTime roundHalfEvenCopy() {
            return iTime.withAmount(iField.roundHalfEven(iTime.getAmount()));
        }
    }

}
