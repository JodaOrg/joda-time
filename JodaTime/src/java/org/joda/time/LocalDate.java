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
 * LocalDate represents an immutable local date, without time zone or time fields.
 * A good use of this class would be to store a birthdate, where typically you
 * don't care about the timezone of the birth.
 * <p>
 * The time fields are not present and cannot be queried on a LocalDate.
 * Calculations on LocalDate are performed using a {@link Chronology}.
 * This chronology is set to be in the UTC time zone for all calculations.
 * <p>
 * Each individual field can be queried in two ways:
 * <ul>
 * <li><code>getMonthOfYear()</code>
 * <li><code>monthOfYear().get()</code>
 * </ul>
 * The second technique also provides access to other useful methods on the
 * field:
 * <ul>
 * <li>numeric value - <code>monthOfYear().get()</code>
 * <li>text value - <code>monthOfYear().getAsText()</code>
 * <li>short text value - <code>monthOfYear().getAsShortText()</code>
 * <li>maximum/minimum values - <code>monthOfYear().getMaximumValue()</code>
 * <li>add/subtract - <code>monthOfYear().addToCopy()</code>
 * <li>set - <code>monthOfYear().setCopy()</code>
 * </ul>
 * <p>
 * LocalDate is thread-safe and immutable, provided that the Chronology is as well.
 * All standard Chronology classes supplied are thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public final class LocalDate
        extends AbstractLocal
        implements ReadableLocal, Serializable, Comparable {

    /** Serialization version */
    private static final long serialVersionUID = 36131981638918L;

    /** The local millis from 1970-01-01T00:00:00Z */
    private final long iLocalMillis;
    /** The chronology to use */
    private final Chronology iChronology;

    // Constructors
    //-----------------------------------------------------------------------
    /**
     * Constructs a LocalDate with the current time, using ISOChronology in
     * the default zone to extract the fields.
     * <p>
     * The constructor uses the default time zone, resulting in the local time
     * being initialised. Once the constructor is complete, all further calculations
     * are performed without reference to a timezone (by switching to UTC).
     */
    public LocalDate() {
        this(DateTimeUtils.currentTimeMillis(), null);
    }

    /**
     * Constructs a LocalDate with the current time, using the specified chronology
     * and zone to extract the fields.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     *
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public LocalDate(Chronology chronology) {
        this(DateTimeUtils.currentTimeMillis(), chronology);
    }

    /**
     * Constructs a LocalDate extracting the partial fields from the specified
     * milliseconds using the ISOChronology in the default zone.
     * <p>
     * The constructor uses the default time zone, resulting in the local time
     * being initialised. Once the constructor is complete, all further calculations
     * are performed without reference to a timezone (by switching to UTC).
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     */
    public LocalDate(long instant) {
        this(instant, null);
    }

    /**
     * Constructs a LocalDate extracting the partial fields from the specified
     * milliseconds using the chronology provided.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public LocalDate(long instant, Chronology chronology) {
        super();
        chronology = DateTimeUtils.getChronology(chronology);
        iChronology = chronology.withUTC();
        iLocalMillis = adjustInstant(instant, chronology);
    }

    /**
     * Constructs a LocalDate from an Object that represents a time.
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
    public LocalDate(Object instant) {
        this(instant, null);
    }

    /**
     * Constructs a LocalDate from an Object that represents a time, using the
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
    public LocalDate(Object instant, Chronology chronology) {
        super();
        InstantConverter converter = ConverterManager.getInstance().getInstantConverter(instant);
        Chronology chrono = DateTimeUtils.getChronology(converter.getChronology(instant, chronology));
        long millis = converter.getInstantMillis(instant, chronology);
        iChronology = chrono.withUTC();
        iLocalMillis = adjustInstant(millis, chrono);
    }

    /**
     * Constructs a LocalDate with specified time field values
     * using <code>ISOChronology</code> in the default zone.
     * <p>
     * The constructor uses the no time zone initialising the fields as provided.
     * Once the constructor is complete, all further calculations
     * are performed without reference to a timezone (by switching to UTC).
     *
     * @param year  the year
     * @param monthOfYear  the month of the year
     * @param dayOfMonth  the day of the month
     */
    public LocalDate(int year, int monthOfYear, int dayOfMonth) {
        this(year, monthOfYear, dayOfMonth, null);
    }

    /**
     * Constructs a LocalDate with specified time field values.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     *
     * @param year  the year
     * @param monthOfYear  the month of the year
     * @param dayOfMonth  the day of the month
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public LocalDate(int year, int monthOfYear, int dayOfMonth, Chronology chronology) {
        super();
        iChronology = DateTimeUtils.getChronology(chronology).withUTC();
        iLocalMillis = iChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth, 0);
    }

    /**
     * Checks the input instant and adusts it to be local and date only.
     * 
     * @param instant  the instant to adjust
     * @param chronology  the chronology that the instant is currently in
     * @return the adjusted local millis
     */
    private long adjustInstant(long instant, Chronology chronology) {
        instant = chronology.getZone().getMillisKeepLocal(DateTimeZone.UTC, instant);
        return chronology.withUTC().dayOfMonth().roundFloor(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the <i>local</i> millisecond instant of this instance.
     * <p>
     * The local millisecond corresponds to the number of milliseconds from
     * 1970-01-01T00:00 ignoring the notion of time zone. For a LocalDate,
     * the time is always set to midnight.
     * <p>
     * API users are not expected to directly use this field.
     *
     * @return the local milliseconds
     */
    public long getLocalMillis() {
        return iLocalMillis;
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
        return field.get(getLocalMillis());
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
     * Checks whether the field specified is supported by this date.
     *
     * @param field  the field to check, may be null which returns false
     * @return true if the field is supported
     */
    private boolean isSupported(DateTimeField field) {
        if (field.isSupported() == false) {
            return false;
        }
        if (field.getType() == DateTimeFieldType.era()) {
            return true;
        }
        return isSupported(field.getDurationField());
    }

    /**
     * Checks whether the field specified is supported by this date.
     *
     * @param field  the field to check, may be null which returns false
     * @return true if the field is supported
     */
    private boolean isSupported(DurationField field) {
        if (field.isSupported() == false) {
            return false;
        }
        return field.getUnitMillis() >= getChronology().days().getUnitMillis();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this datetime with different millis.
     * <p>
     * The returned object will be a new instance of LocalDate.
     * Only the millis will change, the chronology is kept.
     * The returned object will be either be a new instance or <code>this</code>.
     *
     * @param newMillis  the new millis, from 1970-01-01T00:00:00Z
     * @return a copy of this datetime with different millis
     */
    LocalDate withLocalMillis(long newMillis) {
        return (newMillis == getLocalMillis() ? this : new LocalDate(newMillis, getChronology()));
    }

    /**
     * Creates a new LocalDate instance with the specified chronology.
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
    public LocalDate withChronologyRetainFields(Chronology newChronology) {
        newChronology = DateTimeUtils.getChronology(newChronology);
        newChronology = newChronology.withUTC();
        if (newChronology == getChronology()) {
            return this;
        } else {
            return new LocalDate(getYear(), getMonthOfYear(), getDayOfMonth(), newChronology);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this date with the specified field set to a new value.
     * <p>
     * For example, if the field type is <code>dayOfWeek</code> then the day of week
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
     * @return a copy of this instance with the field set
     * @throws IllegalArgumentException if the value is null or invalid
     */
    public LocalDate withField(DateTimeFieldType fieldType, int value) {
        DateTimeField field = getDateTimeField(fieldType);
        long instant = field.set(getLocalMillis(), value);
        return withLocalMillis(instant);
    }

    /**
     * Gets a copy of this date with the value of the specified field increased.
     * <p>
     * If the addition is zero or the field is null, then <code>this</code> is returned.
     * <p>
     * These three lines are equivalent:
     * <pre>
     * DateTime added = dt.withField(DateTimeFieldType.dayOfMonth(), 6);
     * DateTime added = dt.dayOfMonth().addToCopy(6);
     * DateTime added = dt.property(DateTimeFieldType.dayOfMonth()).addToCopy(6);
     * </pre>
     * 
     * @param fieldType  the field type to add to, not null
     * @param amount  the amount to add
     * @return a copy of this instance with the field updated
     * @throws IllegalArgumentException if the value is null or invalid
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public LocalDate withFieldAdded(DurationFieldType fieldType, int amount) {
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
        long instant = field.add(getLocalMillis(), amount);
        return withLocalMillis(instant);
    }

    /**
     * Gets a copy of this date with the specified period added.
     * <p>
     * If the addition is zero, then <code>this</code> is returned.
     * <p>
     * To add or subtract on a single field use the properties, for example:
     * <pre>
     * DateTime added = dt.dayOfMonth().addToCopy(6);
     * </pre>
     * 
     * @param period  the period to add to this one, null means zero
     * @param scalar  the amount of times to add, such as -1 to subtract once
     * @return a copy of this instance with the period added
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public LocalDate withPeriodAdded(ReadablePeriod period, int scalar) {
        if (period == null || scalar == 0) {
            return this;
        }
        long instant = getChronology().add(period, getLocalMillis(), scalar);
        instant = getChronology().dayOfMonth().roundFloor(instant);
        return withLocalMillis(instant);
    }

    /**
     * Gets a copy of this instance with the specified period added.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * <p>
     * The following two lines are identical in effect:
     * <pre>
     * DateTime added = dt.dayOfMonth().addToCopy(6);
     * DateTime added = dt.plus(Period.days(6));
     * </pre>
     * 
     * @param period  the duration to add to this one, null means zero
     * @return a copy of this instance with the period added
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public LocalDate plus(ReadablePeriod period) {
        return withPeriodAdded(period, 1);
    }

    /**
     * Gets a copy of this instance with the specified period take away.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * <p>
     * The following two lines are identical in effect:
     * <pre>
     * DateTime added = dt.dayOfMonth().addToCopy(-6);
     * DateTime added = dt.minus(Period.days(6));
     * </pre>
     * 
     * @param period  the period to reduce this instant by
     * @return a copy of this instance with the period taken away
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public LocalDate minus(ReadablePeriod period) {
        return withPeriodAdded(period, -1);
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this LocalDate to a full datetime at midnight using the
     * default time zone.
     *
     * @return this local date as a datetime at midnight
     */
    public DateTime toDateTimeAtMidnight() {
        return toDateTimeAtMidnight(null);
    }

    /**
     * Converts this LocalDate to a full datetime at midnight using the
     * specified time zone.
     * <p>
     * This method uses the chronology from this instance plus the time zone
     * specified.
     *
     * @param zone  the zone to use, null means default
     * @return the combined datetime
     */
    public DateTime toDateTimeAtMidnight(DateTimeZone zone) {
        Chronology chrono = getChronology().withZone(zone);
        return new DateTime(getYear(), getMonthOfYear(), getDayOfMonth(), 0, 0, 0, 0, chrono);
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this ReadableLocal to a full datetime by resolving it against
     * another datetime.
     * <p>
     * This method takes the specified datetime and sets the fields from this
     * ReadableLocal on top. The chronology (including timezone) from the base
     * instant is used.
     * <p>
     * Thus, the result will use the date from this LocalDate, with the time and
     * zone from the specified instant.
     *
     * @param baseInstant  the instant that provides the missing fields, null means now
     * @return the combined datetime
     */
    public DateTime toDateTime(ReadableInstant baseInstant) {
        Chronology chrono = DateTimeUtils.getInstantChronology(baseInstant);
        long instant = DateTimeUtils.getInstantMillis(baseInstant);
        instant = chrono.year().set(instant, getYear());
        instant = chrono.monthOfYear().set(instant, getMonthOfYear());
        instant = chrono.dayOfMonth().set(instant, getDayOfMonth());
        return new DateTime(instant, chrono);
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this object to a DateTime using a LocalTime to fill in the
     * missing fields and using the default time zone.
     * This instance is immutable and unaffected by this method call.
     * <p>
     * The resulting chronology is determined by the chronology of this
     * LocalDate plus the time zone.
     * The chronology of the time is ignored - only the field values are used.
     *
     * @param time  the time to use, null means current time
     * @return the DateTime instance
     */
    public DateTime toDateTime(LocalTime time) {
        return toDateTime(time, null);
    }

    /**
     * Converts this object to a DateTime using a LocalTime to fill in the
     * missing fields.
     * This instance is immutable and unaffected by this method call.
     * <p>
     * The resulting chronology is determined by the chronology of this
     * LocalDate plus the time zone.
     * The chronology of the time is ignored - only the field values are used.
     *
     * @param time  the time to use, null means current time
     * @param zone  the zone to get the DateTime in, null means default
     * @return the DateTime instance
     */
    public DateTime toDateTime(LocalTime time, DateTimeZone zone) {
        Chronology chrono = getChronology().withZone(zone);
        if (time == null) {
            time = new LocalTime(chrono);
        }
        return new DateTime(
                getYear(), getMonthOfYear(), getDayOfMonth(),
                time.getHourOfDay(), time.getMinuteOfHour(),
                time.getSecondOfMinute(), time.getMillisOfSecond(),
                chrono);
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this object to a DateMidnight in the default time zone.
     *
     * @return the DateMidnight instance in the default zone
     */
    public DateMidnight toDateMidnight() {
        return toDateMidnight(null);
    }

    /**
     * Converts this object to a DateMidnight.
     *
     * @param zone  the zone to get the DateMidnight in, null means default
     * @return the DateMidnight instance
     */
    public DateMidnight toDateMidnight(DateTimeZone zone) {
        Chronology chrono = getChronology().withZone(zone);
        return new DateMidnight(getYear(), getMonthOfYear(), getDayOfMonth(), chrono);
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this object to an Interval representing the whole day
     * in the default time zone.
     *
     * @return the Interval instance in the default zone
     */
    public Interval toInterval() {
        return toInterval(null);
    }

    /**
     * Converts this object to an Interval representing the whole day.
     *
     * @param zone  the zone to get the Interval in, null means default
     * @return the Interval instance
     */
    public Interval toInterval(DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        return toDateMidnight(zone).toInterval();
    }

    //-----------------------------------------------------------------------
    /**
     * Get the era field value.
     * 
     * @return the era
     */
    public int getEra() {
        return getChronology().era().get(getLocalMillis());
    }

    /**
     * Get the year of era field value.
     * 
     * @return the year of era
     */
    public int getCenturyOfEra() {
        return getChronology().centuryOfEra().get(getLocalMillis());
    }

    /**
     * Get the year of era field value.
     * 
     * @return the year of era
     */
    public int getYearOfEra() {
        return getChronology().yearOfEra().get(getLocalMillis());
    }

    /**
     * Get the year of century field value.
     * 
     * @return the year of century
     */
    public int getYearOfCentury() {
        return getChronology().yearOfCentury().get(getLocalMillis());
    }

    /**
     * Get the year field value.
     * 
     * @return the year
     */
    public int getYear() {
        return getChronology().year().get(getLocalMillis());
    }

    /**
     * Get the weekyear field value.
     * 
     * @return the year of a week based year
     */
    public int getWeekyear() {
        return getChronology().weekyear().get(getLocalMillis());
    }

    /**
     * Get the month of year field value.
     * 
     * @return the month of year
     */
    public int getMonthOfYear() {
        return getChronology().monthOfYear().get(getLocalMillis());
    }

    /**
     * Get the week of weekyear field value.
     * 
     * @return the week of a week based year
     */
    public int getWeekOfWeekyear() {
        return getChronology().weekOfWeekyear().get(getLocalMillis());
    }

    /**
     * Get the day of year field value.
     * 
     * @return the day of year
     */
    public int getDayOfYear() {
        return getChronology().dayOfYear().get(getLocalMillis());
    }

    /**
     * Get the day of month field value.
     * <p>
     * The values for the day of month are defined in {@link org.joda.time.DateTimeConstants}.
     * 
     * @return the day of month
     */
    public int getDayOfMonth() {
        return getChronology().dayOfMonth().get(getLocalMillis());
    }

    /**
     * Get the day of week field value.
     * <p>
     * The values for the day of week are defined in {@link org.joda.time.DateTimeConstants}.
     * 
     * @return the day of week
     */
    public int getDayOfWeek() {
        return getChronology().dayOfWeek().get(getLocalMillis());
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

    //-----------------------------------------------------------------------
    /**
     * Compares this instance with the specified object for ascending
     * time order.
     * <p>
     * The comparison will only succeed if the object is a LocalDate with
     * the same Chronology.
     *
     * @param localDate  a local date to check against
     * @return negative value if this is less, 0 if equal, or positive value if greater
     * @throws NullPointerException if the object is null
     * @throws ClassCastException if the object type is not supported
     * @throws IllegalArgumentException if the object cannot be compared
     */
    public int compareTo(Object localDate) {
        LocalDate other = (LocalDate) localDate;
        if (other.getChronology() != getChronology()) {
            throw new IllegalArgumentException("The LocalDate cannot be compared");
        }
        long thisAmount = getLocalMillis();
        long otherAmount = other.getLocalMillis();
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
     * Is this instance after the current date.
     * <p>
     * The comparison is date based, and returns true if the current time
     * belongs to a previous day.
     *
     * @return true if this instance is after the current date
     */
    public boolean isToday() {
        long now = DateTimeUtils.currentTimeMillis();
        now = adjustInstant(now, getChronology());
        return (getLocalMillis() == now);
    }

    /**
     * Is this instance after the current date.
     * <p>
     * The comparison is date based, and returns true if the current time
     * belongs to a previous day.
     * The current date is obtained in the default time zone.
     *
     * @return true if this instance is after the current date
     */
    public boolean isAfterToday() {
        long now = DateTimeUtils.currentTimeMillis();
        now = adjustInstant(now, Chronology.getISO());
        return (getLocalMillis() > now);
    }

    /**
     * Is this instance after the date passed in.
     * <p>
     * The comparison will only succeed if the object is a LocalDate with
     * the same Chronology.
     *
     * @param local  a local to check against, null means now
     * @return true if this instance is after the one passed in
     * @throws IllegalArgumentException if the object cannot be compared
     */
    public boolean isAfter(LocalDate local) {
        if (local == null) {
            return isAfterToday();
        }
        if (local.getChronology() != getChronology()) {
            throw new IllegalArgumentException("The LocalDate cannot be compared");
        }
        return (getLocalMillis() > local.getLocalMillis());
    }

    /**
     * Is this instance before the current date.
     * <p>
     * The comparison is date based, and returns true if the current time
     * belongs to a previous day.
     * The current date is obtained in the default time zone.
     *
     * @return true if this instance is before the current date
     */
    public boolean isBeforeToday() {
        long now = DateTimeUtils.currentTimeMillis();
        now = adjustInstant(now, Chronology.getISO());
        return (getLocalMillis() < now);
    }

    /**
     * Is this instance before the date passed in.
     * <p>
     * The comparison will only succeed if the object is a LocalDate with
     * the same Chronology.
     *
     * @param local  a local to check against, null means now
     * @return true if this instance is before the one passed in
     * @throws IllegalArgumentException if the object cannot be compared
     */
    public boolean isBefore(LocalDate local) {
        if (local == null) {
            return isBeforeToday();
        }
        if (local.getChronology() != getChronology()) {
            throw new IllegalArgumentException("The LocalDate cannot be compared");
        }
        return (getLocalMillis() < local.getLocalMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this object with the specified object for equality based
     * on the local time and the Chronology.
     * <p>
     * Only LocalDate instances are accepted.
     *
     * @param localDate  a readable instant to check against
     * @return true if local time and chronology are equal, false if
     *  not or the localDate is null or of an incorrect type
     */
    public boolean equals(Object localDate) {
        if (this == localDate) {
            return true;
        }
        if (localDate instanceof LocalDate) {
            LocalDate otherDate = (LocalDate) localDate;
            if (getLocalMillis() == otherDate.getLocalMillis()) {
                Chronology chrono = getChronology();
                if (chrono == otherDate.getChronology()) {
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
            ((int) (getLocalMillis() ^ (getLocalMillis() >>> 32))) +
            (getChronology().hashCode());
    }

    //-----------------------------------------------------------------------
    /**
     * Output the time in the ISO8601 format YYYY-MM-DD.
     * 
     * @return ISO8601 formatted string
     */
    public String toString() {
        return ISODateTimeFormat.getInstance().yearMonthDay().print(this);
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
        return DateTimeFormat.getInstance().forPattern(pattern).print(this);
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
        return DateTimeFormat.getInstance(locale).forPattern(pattern).print(this);
    }

    //-----------------------------------------------------------------------
    /**
     * LocalDate.Property binds a LocalDate to a DateTimeField allowing powerful
     * datetime functionality to be easily accessed.
     * <p>
     * The simplest use of this class is as an alternative get method, here used to
     * get the year '1972' (as an int) and the month 'December' (as a String).
     * <pre>
     * LocalDate dt = new LocalDate(1972, 12, 3, 0, 0, 0, 0);
     * int year = dt.year().get();
     * String monthStr = dt.month().getAsText();
     * </pre>
     * <p>
     * Methods are also provided that allow date modification. These return
     * new instances of LocalDate - they do not modify the original. The example
     * below yields two independent immutable date objects 20 years apart.
     * <pre>
     * LocalDate dt = new LocalDate(1972, 12, 3);
     * LocalDate dt20 = dt.year().addToCopy(20);
     * </pre>
     * LocalDate.Propery itself is thread-safe and immutable, as well as the
     * LocalDate being operated on.
     *
     * @author Stephen Colebourne
     * @author Brian S O'Neill
     * @since 1.0
     */
    public static final class Property extends AbstractMillisFieldProperty {
        
        /** Serialization version */
        private static final long serialVersionUID = -5853619638173L;
        
        /** The date this property is working against */
        private final LocalDate iDate;
        /** The field this property is working against */
        private final DateTimeField iField;
        
        /**
         * Constructor.
         * 
         * @param date  the date to use
         * @param field  the field to use
         */
        Property(LocalDate date, DateTimeField field) {
            super();
            iDate = date;
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
         * Gets the milliseconds of the local date that this property is linked to.
         * 
         * @return the milliseconds
         */
        protected long getMillis() {
            return iDate.getLocalMillis();
        }

        /**
         * Gets the date being used.
         * 
         * @return the date
         */
        public LocalDate getLocalDate() {
            return iDate;
        }

        //-----------------------------------------------------------------------
        /**
         * Adds to this field in a copy of this LocalDate.
         * <p>
         * The LocalDate attached to this property is unchanged by this call.
         * 
         * @param value  the value to add to the field in the copy
         * @return a copy of the LocalDate with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public LocalDate addToCopy(int value) {
            return iDate.withLocalMillis(iField.add(iDate.getLocalMillis(), value));
        }
        
        /**
         * Adds to this field in a copy of this LocalDate.
         * <p>
         * The LocalDate attached to this property is unchanged by this call.
         * 
         * @param value  the value to add to the field in the copy
         * @return a copy of the LocalDate with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public LocalDate addToCopy(long value) {
            return iDate.withLocalMillis(iField.add(iDate.getLocalMillis(), value));
        }
        
        /**
         * Adds to this field, possibly wrapped, in a copy of this LocalDate.
         * A wrapped operation only changes this field.
         * Thus 31st January addWrapField one day goes to the 1st January.
         * <p>
         * The LocalDate attached to this property is unchanged by this call.
         * 
         * @param value  the value to add to the field in the copy
         * @return a copy of the LocalDate with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public LocalDate addWrapFieldToCopy(int value) {
            return iDate.withLocalMillis(iField.addWrapField(iDate.getLocalMillis(), value));
        }
        
        //-----------------------------------------------------------------------
        /**
         * Sets this field in a copy of the LocalDate.
         * <p>
         * The LocalDate attached to this property is unchanged by this call.
         * 
         * @param value  the value to set the field in the copy to
         * @return a copy of the LocalDate with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public LocalDate setCopy(int value) {
            return iDate.withLocalMillis(iField.set(iDate.getLocalMillis(), value));
        }
        
        /**
         * Sets this field in a copy of the LocalDate to a parsed text value.
         * <p>
         * The LocalDate attached to this property is unchanged by this call.
         * 
         * @param text  the text value to set
         * @param locale  optional locale to use for selecting a text symbol
         * @return a copy of the LocalDate with the field value changed
         * @throws IllegalArgumentException if the text value isn't valid
         */
        public LocalDate setCopy(String text, Locale locale) {
            return iDate.withLocalMillis(iField.set(iDate.getLocalMillis(), text, locale));
        }
        
        /**
         * Sets this field in a copy of the LocalDate to a parsed text value.
         * <p>
         * The LocalDate attached to this property is unchanged by this call.
         * 
         * @param text  the text value to set
         * @return a copy of the LocalDate with the field value changed
         * @throws IllegalArgumentException if the text value isn't valid
         */
        public LocalDate setCopy(String text) {
            return setCopy(text, null);
        }
        
        //-----------------------------------------------------------------------
        /**
         * Rounds to the lowest whole unit of this field on a copy of this LocalDate.
         *
         * @return a copy of the LocalDate with the field value changed
         */
        public LocalDate roundFloorCopy() {
            return iDate.withLocalMillis(iField.roundFloor(iDate.getLocalMillis()));
        }
        
        /**
         * Rounds to the highest whole unit of this field on a copy of this LocalDate.
         *
         * @return a copy of the LocalDate with the field value changed
         */
        public LocalDate roundCeilingCopy() {
            return iDate.withLocalMillis(iField.roundCeiling(iDate.getLocalMillis()));
        }
        
        /**
         * Rounds to the nearest whole unit of this field on a copy of this LocalDate,
         * favoring the floor if halfway.
         *
         * @return a copy of the LocalDate with the field value changed
         */
        public LocalDate roundHalfFloorCopy() {
            return iDate.withLocalMillis(iField.roundHalfFloor(iDate.getLocalMillis()));
        }
        
        /**
         * Rounds to the nearest whole unit of this field on a copy of this LocalDate,
         * favoring the ceiling if halfway.
         *
         * @return a copy of the LocalDate with the field value changed
         */
        public LocalDate roundHalfCeilingCopy() {
            return iDate.withLocalMillis(iField.roundHalfCeiling(iDate.getLocalMillis()));
        }
        
        /**
         * Rounds to the nearest whole unit of this field on a copy of this
         * LocalDate.  If halfway, the ceiling is favored over the floor only if
         * it makes this field's value even.
         *
         * @return a copy of the LocalDate with the field value changed
         */
        public LocalDate roundHalfEvenCopy() {
            return iDate.withLocalMillis(iField.roundHalfEven(iDate.getLocalMillis()));
        }
    }

}
