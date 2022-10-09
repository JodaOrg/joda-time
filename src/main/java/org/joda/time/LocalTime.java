/*
 *  Copyright 2001-2011 Stephen Colebourne
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.base.BaseLocal;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.PartialConverter;
import org.joda.time.field.AbstractReadableInstantFieldProperty;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * LocalTime is an immutable time class representing a time
 * without a time zone.
 * <p>
 * LocalTime implements the {@link ReadablePartial} interface.
 * To do this, the interface methods focus on the key fields -
 * HourOfDay, MinuteOfHour, SecondOfMinute and MillisOfSecond.
 * However, <b>all</b> time fields may in fact be queried.
 * <p>
 * Calculations on LocalTime are performed using a {@link Chronology}.
 * This chronology will be set internally to be in the UTC time zone
 * for all calculations.
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
 * LocalTime is thread-safe and immutable, provided that the Chronology is as well.
 * All standard Chronology classes supplied are thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @since 1.3
 */
public final class LocalTime
        extends BaseLocal
        implements ReadablePartial, Serializable {

    /** Serialization lock */
    private static final long serialVersionUID = -12873158713873L;

    /** Constant for midnight. */
    public static final LocalTime MIDNIGHT = new LocalTime(0, 0, 0, 0);

    /** The index of the hourOfDay field in the field array */
    private static final int HOUR_OF_DAY = 0;
    /** The index of the minuteOfHour field in the field array */
    private static final int MINUTE_OF_HOUR = 1;
    /** The index of the secondOfMinute field in the field array */
    private static final int SECOND_OF_MINUTE = 2;
    /** The index of the millisOfSecond field in the field array */
    private static final int MILLIS_OF_SECOND = 3;
    /** Set of known duration types. */
    private static final Set<DurationFieldType> TIME_DURATION_TYPES = new HashSet<DurationFieldType>();
    static {
        TIME_DURATION_TYPES.add(DurationFieldType.millis());
        TIME_DURATION_TYPES.add(DurationFieldType.seconds());
        TIME_DURATION_TYPES.add(DurationFieldType.minutes());
        TIME_DURATION_TYPES.add(DurationFieldType.hours());
    }

    /** The local millis from 1970-01-01T00:00:00 */
    private final long iLocalMillis;
    /** The chronology to use, in UTC */
    private final Chronology iChronology;

    //-----------------------------------------------------------------------
    /**
     * Obtains a {@code LocalTime} set to the current system millisecond time
     * using <code>ISOChronology</code> in the default time zone.
     * The resulting object does not use the zone.
     * 
     * @return the current time, not null
     * @since 2.0
     */
    public static LocalTime now() {
        return new LocalTime();
    }

    /**
     * Obtains a {@code LocalTime} set to the current system millisecond time
     * using <code>ISOChronology</code> in the specified time zone.
     * The resulting object does not use the zone.
     *
     * @param zone  the time zone, not null
     * @return the current time, not null
     * @since 2.0
     */
    public static LocalTime now(DateTimeZone zone) {
        if (zone == null) {
            throw new NullPointerException("Zone must not be null");
        }
        return new LocalTime(zone);
    }

    /**
     * Obtains a {@code LocalTime} set to the current system millisecond time
     * using the specified chronology.
     * The resulting object does not use the zone.
     *
     * @param chronology  the chronology, not null
     * @return the current time, not null
     * @since 2.0
     */
    public static LocalTime now(Chronology chronology) {
        if (chronology == null) {
            throw new NullPointerException("Chronology must not be null");
        }
        return new LocalTime(chronology);
    }

    //-----------------------------------------------------------------------
    /**
     * Parses a {@code LocalTime} from the specified string.
     * <p>
     * This uses {@link ISODateTimeFormat#localTimeParser()}.
     * 
     * @param str  the string to parse, not null
     * @return the parsed time, not null
     * @since 2.0
     */
    @FromString
    public static LocalTime parse(String str) {
        return parse(str, ISODateTimeFormat.localTimeParser());
    }

    /**
     * Parses a {@code LocalTime} from the specified string using a formatter.
     * 
     * @param str  the string to parse, not null
     * @param formatter  the formatter to use, not null
     * @return the parsed time, not null
     * @since 2.0
     */
    public static LocalTime parse(String str, DateTimeFormatter formatter) {
        return formatter.parseLocalTime(str);
    }

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
     * @return the time, not null
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
     * @return the time, not null
     */
    public static LocalTime fromMillisOfDay(long millisOfDay, Chronology chrono) {
        chrono = DateTimeUtils.getChronology(chrono).withUTC();
        return new LocalTime(millisOfDay, chrono);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs a LocalTime from a <code>java.util.Calendar</code>
     * using exactly the same field values.
     * <p>
     * Each field is queried from the Calendar and assigned to the LocalTime.
     * This is useful if you have been using the Calendar as a local time,
     * ignoring the zone.
     * <p>
     * One advantage of this method is that this method is unaffected if the
     * version of the time zone data differs between the JDK and Joda-Time.
     * That is because the local field values are transferred, calculated using
     * the JDK time zone data and without using the Joda-Time time zone data.
     * <p>
     * This factory method ignores the type of the calendar and always
     * creates a LocalTime with ISO chronology. It is expected that you
     * will only pass in instances of <code>GregorianCalendar</code> however
     * this is not validated.
     *
     * @param calendar  the Calendar to extract fields from
     * @return the created LocalTime
     * @throws IllegalArgumentException if the calendar is null
     * @throws IllegalArgumentException if the date is invalid for the ISO chronology
     */
    public static LocalTime fromCalendarFields(Calendar calendar) {
        if (calendar == null) {
            throw new IllegalArgumentException("The calendar must not be null");
        }
        return new LocalTime(
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND),
            calendar.get(Calendar.MILLISECOND)
        );
    }

    /**
     * Constructs a LocalTime from a <code>java.util.Date</code>
     * using exactly the same field values.
     * <p>
     * Each field is queried from the Date and assigned to the LocalTime.
     * This is useful if you have been using the Date as a local time,
     * ignoring the zone.
     * <p>
     * One advantage of this method is that this method is unaffected if the
     * version of the time zone data differs between the JDK and Joda-Time.
     * That is because the local field values are transferred, calculated using
     * the JDK time zone data and without using the Joda-Time time zone data.
     * <p>
     * This factory method always creates a LocalTime with ISO chronology.
     *
     * @param date  the Date to extract fields from
     * @return the created LocalTime
     * @throws IllegalArgumentException if the calendar is null
     * @throws IllegalArgumentException if the date is invalid for the ISO chronology
     */
    @SuppressWarnings("deprecation")
    public static LocalTime fromDateFields(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return new LocalTime(
            date.getHours(),
            date.getMinutes(),
            date.getSeconds(),
            (((int) (date.getTime() % 1000)) + 1000) % 1000
        );
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance set to the current local time evaluated using
     * ISO chronology in the default zone.
     * <p>
     * Once the constructor is completed, the zone is no longer used.
     * 
     * @see #now()
     */
    public LocalTime() {
        this(DateTimeUtils.currentTimeMillis(), ISOChronology.getInstance());
    }

    /**
     * Constructs an instance set to the current local time evaluated using
     * ISO chronology in the specified zone.
     * <p>
     * If the specified time zone is null, the default zone is used.
     * Once the constructor is completed, the zone is no longer used.
     *
     * @param zone  the time zone, null means default zone
     * @see #now(DateTimeZone)
     */
    public LocalTime(DateTimeZone zone) {
        this(DateTimeUtils.currentTimeMillis(), ISOChronology.getInstance(zone));
    }

    /**
     * Constructs an instance set to the current local time evaluated using
     * specified chronology and zone.
     * <p>
     * If the chronology is null, ISO chronology in the default time zone is used.
     * Once the constructor is completed, the zone is no longer used.
     *
     * @param chronology  the chronology, null means ISOChronology in default zone
     * @see #now(Chronology)
     */
    public LocalTime(Chronology chronology) {
        this(DateTimeUtils.currentTimeMillis(), chronology);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance set to the local time defined by the specified
     * instant evaluated using ISO chronology in the default zone.
     * <p>
     * Once the constructor is completed, the zone is no longer used.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     */
    public LocalTime(long instant) {
        this(instant, ISOChronology.getInstance());
    }

    /**
     * Constructs an instance set to the local time defined by the specified
     * instant evaluated using ISO chronology in the specified zone.
     * <p>
     * If the specified time zone is null, the default zone is used.
     * Once the constructor is completed, the zone is no longer used.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @param zone  the time zone, null means default zone
     */
    public LocalTime(long instant, DateTimeZone zone) {
        this(instant, ISOChronology.getInstance(zone));
    }

    /**
     * Constructs an instance set to the local time defined by the specified
     * instant evaluated using the specified chronology.
     * <p>
     * If the chronology is null, ISO chronology in the default zone is used.
     * Once the constructor is completed, the zone is no longer used.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @param chronology  the chronology, null means ISOChronology in default zone
     */
    public LocalTime(long instant, Chronology chronology) {
        chronology = DateTimeUtils.getChronology(chronology);
        
        long localMillis = chronology.getZone().getMillisKeepLocal(DateTimeZone.UTC, instant);
        chronology = chronology.withUTC();
        iLocalMillis = chronology.millisOfDay().get(localMillis);
        iChronology = chronology;
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance from an Object that represents a datetime.
     * <p>
     * If the object contains no chronology, <code>ISOChronology</code> is used.
     * If the object contains no time zone, the default zone is used.
     * Once the constructor is completed, the zone is no longer used.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadablePartial, ReadableInstant, String, Calendar and Date.
     * The String formats are described by {@link ISODateTimeFormat#localTimeParser()}.
     * The default String converter ignores the zone and only parses the field values.
     *
     * @param instant  the datetime object
     * @throws IllegalArgumentException if the instant is invalid
     */
    public LocalTime(Object instant) {
        this(instant, (Chronology) null);
    }

    /**
     * Constructs an instance from an Object that represents a datetime,
     * forcing the time zone to that specified.
     * <p>
     * If the object contains no chronology, <code>ISOChronology</code> is used.
     * If the specified time zone is null, the default zone is used.
     * Once the constructor is completed, the zone is no longer used.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadablePartial, ReadableInstant, String, Calendar and Date.
     * The String formats are described by {@link ISODateTimeFormat#localTimeParser()}.
     * The default String converter ignores the zone and only parses the field values.
     *
     * @param instant  the datetime object
     * @param zone  the time zone
     * @throws IllegalArgumentException if the instant is invalid
     */
    public LocalTime(Object instant, DateTimeZone zone) {
        PartialConverter converter = ConverterManager.getInstance().getPartialConverter(instant);
        Chronology chronology = converter.getChronology(instant, zone);
        chronology = DateTimeUtils.getChronology(chronology);
        iChronology = chronology.withUTC();
        int[] values = converter.getPartialValues(this, instant, chronology, ISODateTimeFormat.localTimeParser());
        iLocalMillis = iChronology.getDateTimeMillis(0L, values[0], values[1], values[2], values[3]);
    }

    /**
     * Constructs an instance from an Object that represents a datetime,
     * using the specified chronology.
     * <p>
     * If the chronology is null, ISO in the default time zone is used.
     * Once the constructor is completed, the zone is no longer used.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadablePartial, ReadableInstant, String, Calendar and Date.
     * The String formats are described by {@link ISODateTimeFormat#localTimeParser()}.
     * The default String converter ignores the zone and only parses the field values.
     *
     * @param instant  the datetime object
     * @param chronology  the chronology
     * @throws IllegalArgumentException if the instant is invalid
     */
    public LocalTime(Object instant, Chronology chronology) {
        PartialConverter converter = ConverterManager.getInstance().getPartialConverter(instant);
        chronology = converter.getChronology(instant, chronology);
        chronology = DateTimeUtils.getChronology(chronology);
        iChronology = chronology.withUTC();
        int[] values = converter.getPartialValues(this, instant, chronology, ISODateTimeFormat.localTimeParser());
        iLocalMillis = iChronology.getDateTimeMillis(0L, values[0], values[1], values[2], values[3]);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance set to the specified time
     * using <code>ISOChronology</code>.
     *
     * @param hourOfDay  the hour of the day, from 0 to 23
     * @param minuteOfHour  the minute of the hour, from 0 to 59
     */
    public LocalTime(
            int hourOfDay,
            int minuteOfHour) {
        this(hourOfDay, minuteOfHour, 0, 0, ISOChronology.getInstanceUTC());
    }

    /**
     * Constructs an instance set to the specified time
     * using <code>ISOChronology</code>.
     *
     * @param hourOfDay  the hour of the day, from 0 to 23
     * @param minuteOfHour  the minute of the hour, from 0 to 59
     * @param secondOfMinute  the second of the minute, from 0 to 59
     */
    public LocalTime(
            int hourOfDay,
            int minuteOfHour,
            int secondOfMinute) {
        this(hourOfDay, minuteOfHour, secondOfMinute, 0, ISOChronology.getInstanceUTC());
    }

    /**
     * Constructs an instance set to the specified time
     * using <code>ISOChronology</code>.
     *
     * @param hourOfDay  the hour of the day, from 0 to 23
     * @param minuteOfHour  the minute of the hour, from 0 to 59
     * @param secondOfMinute  the second of the minute, from 0 to 59
     * @param millisOfSecond  the millisecond of the second, from 0 to 999
     */
    public LocalTime(
            int hourOfDay,
            int minuteOfHour,
            int secondOfMinute,
            int millisOfSecond) {
        this(hourOfDay, minuteOfHour, secondOfMinute,
                millisOfSecond, ISOChronology.getInstanceUTC());
    }

    /**
     * Constructs an instance set to the specified time
     * using the specified chronology, whose zone is ignored.
     * <p>
     * If the chronology is null, <code>ISOChronology</code> is used.
     *
     * @param hourOfDay  the hour of the day, valid values defined by the chronology
     * @param minuteOfHour  the minute of the hour, valid values defined by the chronology
     * @param secondOfMinute  the second of the minute, valid values defined by the chronology
     * @param millisOfSecond  the millisecond of the second, valid values defined by the chronology
     * @param chronology  the chronology, null means ISOChronology in default zone
     */
    public LocalTime(
            int hourOfDay,
            int minuteOfHour,
            int secondOfMinute,
            int millisOfSecond,
            Chronology chronology) {
        super();
        chronology = DateTimeUtils.getChronology(chronology).withUTC();
        long instant = chronology.getDateTimeMillis(
            0L, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        iChronology = chronology;
        iLocalMillis = instant;
    }

    /**
     * Handle broken serialization from other tools.
     * @return the resolved object, not null
     */
    private Object readResolve() {
        if (iChronology == null) {
            return new LocalTime(iLocalMillis, ISOChronology.getInstanceUTC());
        }
        if (DateTimeZone.UTC.equals(iChronology.getZone()) == false) {
            return new LocalTime(iLocalMillis, iChronology.withUTC());
        }
        return this;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the number of fields in this partial, which is four.
     * The supported fields are HourOfDay, MinuteOfHour, SecondOfMinute
     * and MillisOfSecond.
     *
     * @return the field count, four
     */
    public int size() {
        return 4;
    }

    /**
     * Gets the field for a specific index in the chronology specified.
     * <p>
     * This method must not use any instance variables.
     *
     * @param index  the index to retrieve
     * @param chrono  the chronology to use
     * @return the field
     */
    @Override
    protected DateTimeField getField(int index, Chronology chrono) {
        switch (index) {
            case HOUR_OF_DAY:
                return chrono.hourOfDay();
            case MINUTE_OF_HOUR:
                return chrono.minuteOfHour();
            case SECOND_OF_MINUTE:
                return chrono.secondOfMinute();
            case MILLIS_OF_SECOND:
                return chrono.millisOfSecond();
            default:
                throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
    }

    /**
     * Gets the value of the field at the specified index.
     * <p>
     * This method is required to support the <code>ReadablePartial</code>
     * interface. The supported fields are HourOfDay, MinuteOfHour,
     * SecondOfMinute and MillisOfSecond.
     *
     * @param index  the index, zero to three
     * @return the value
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public int getValue(int index) {
        switch (index) {
            case HOUR_OF_DAY:
                return getChronology().hourOfDay().get(getLocalMillis());
            case MINUTE_OF_HOUR:
                return getChronology().minuteOfHour().get(getLocalMillis());
            case SECOND_OF_MINUTE:
                return getChronology().secondOfMinute().get(getLocalMillis());
            case MILLIS_OF_SECOND:
                return getChronology().millisOfSecond().get(getLocalMillis());
            default:
                throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Get the value of one of the fields of time.
     * <p>
     * This method gets the value of the specified field.
     * For example:
     * <pre>
     * DateTime dt = new DateTime();
     * int hourOfDay = dt.get(DateTimeFieldType.hourOfDay());
     * </pre>
     *
     * @param fieldType  a field type, usually obtained from DateTimeFieldType, not null
     * @return the value of that field
     * @throws IllegalArgumentException if the field type is null
     */
    @Override
    public int get(DateTimeFieldType fieldType) {
        if (fieldType == null) {
            throw new IllegalArgumentException("The DateTimeFieldType must not be null");
        }
        if (isSupported(fieldType) == false) {
            throw new IllegalArgumentException("Field '" + fieldType + "' is not supported");
        }
        return fieldType.getField(getChronology()).get(getLocalMillis());
    }

    /**
     * Checks if the field type specified is supported by this
     * local time and chronology.
     * This can be used to avoid exceptions in {@link #get(DateTimeFieldType)}.
     *
     * @param type  a field type, usually obtained from DateTimeFieldType
     * @return true if the field type is supported
     */
    @Override
    public boolean isSupported(DateTimeFieldType type) {
        if (type == null) {
            return false;
        }
        if (isSupported(type.getDurationType()) == false) {
            return false;
        }
        DurationFieldType range = type.getRangeDurationType();
        return (isSupported(range) || range == DurationFieldType.days());
    }

    /**
     * Checks if the duration type specified is supported by this
     * local time and chronology.
     *
     * @param type  a duration type, usually obtained from DurationFieldType
     * @return true if the field type is supported
     */
    public boolean isSupported(DurationFieldType type) {
        if (type == null) {
            return false;
        }
        DurationField field = type.getField(getChronology());
        if (TIME_DURATION_TYPES.contains(type) ||
            field.getUnitMillis() < getChronology().days().getUnitMillis()) {
            return field.isSupported();
        }
        return false;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the local milliseconds from the Java epoch
     * of 1970-01-01T00:00:00 (not fixed to any specific time zone).
     * 
     * @return the number of milliseconds since 1970-01-01T00:00:00
     * @since 1.5 (previously private)
     */
    @Override
    protected long getLocalMillis() {
        return iLocalMillis;
    }

    /**
     * Gets the chronology of the time.
     * 
     * @return the Chronology that the time is using
     */
    public Chronology getChronology() {
        return iChronology;
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this ReadablePartial with another returning true if the chronology,
     * field types and values are equal.
     *
     * @param partial  an object to check against
     * @return true if fields and values are equal
     */
    @Override
    public boolean equals(Object partial) {
        // override to perform faster
        if (this == partial) {
            return true;
        }
        if (partial instanceof LocalTime) {
            LocalTime other = (LocalTime) partial;
            if (iChronology.equals(other.iChronology)) {
                return iLocalMillis == other.iLocalMillis;
            }
        }
        return super.equals(partial);
    }

    @Override
    public int hashCode() {
        int total = 157;
        total = 23 * total + iChronology.hourOfDay().get(iLocalMillis);
        total = 23 * total + iChronology.hourOfDay().getType().hashCode();
        total = 23 * total + iChronology.minuteOfHour().get(iLocalMillis);
        total = 23 * total + iChronology.minuteOfHour().getType().hashCode();
        total = 23 * total + iChronology.secondOfMinute().get(iLocalMillis);
        total = 23 * total + iChronology.secondOfMinute().getType().hashCode();
        total = 23 * total + iChronology.millisOfSecond().get(iLocalMillis);
        total = 23 * total + iChronology.millisOfSecond().getType().hashCode();
        total += getChronology().hashCode();
        return total;
    }

    /**
     * Compares this partial with another returning an integer
     * indicating the order.
     * <p>
     * The fields are compared in order, from largest to smallest.
     * The first field that is non-equal is used to determine the result.
     * <p>
     * The specified object must be a partial instance whose field types
     * match those of this partial.
     *
     * @param partial  an object to check against
     * @return negative if this is less, zero if equal, positive if greater
     * @throws ClassCastException if the partial is the wrong class
     *  or if it has field types that don't match
     * @throws NullPointerException if the partial is null
     */
    @Override
    public int compareTo(ReadablePartial partial) {
        // override to perform faster
        if (this == partial) {
            return 0;
        }
        if (partial instanceof LocalTime) {
            LocalTime other = (LocalTime) partial;
            if (iChronology.equals(other.iChronology)) {
                return (iLocalMillis < other.iLocalMillis ? -1 :
                            (iLocalMillis == other.iLocalMillis ? 0 : 1));

            }
        }
        return super.compareTo(partial);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this time with different local millis.
     * <p>
     * The returned object will be a new instance of the same type.
     * Only the millis will change, the chronology is kept.
     * The returned object will be either be a new instance or <code>this</code>.
     *
     * @param newMillis  the new millis, from 1970-01-01T00:00:00
     * @return a copy of this time with different millis
     */
    LocalTime withLocalMillis(long newMillis) {
        return (newMillis == getLocalMillis() ? this : new LocalTime(newMillis, getChronology()));
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this time with the partial set of fields replacing
     * those from this instance.
     * <p>
     * For example, if the partial contains an hour and minute then those two
     * fields will be changed in the returned instance.
     * Unsupported fields are ignored.
     * If the partial is null, then <code>this</code> is returned.
     *
     * @param partial  the partial set of fields to apply to this time, null ignored
     * @return a copy of this time with a different set of fields
     * @throws IllegalArgumentException if any value is invalid
     */
    public LocalTime withFields(ReadablePartial partial) {
        if (partial == null) {
            return this;
        }
        return withLocalMillis(getChronology().set(partial, getLocalMillis()));
    }

    /**
     * Returns a copy of this time with the specified field set
     * to a new value.
     * <p>
     * For example, if the field type is <code>hourOfDay</code> then the hour of day
     * field would be changed in the returned instance.
     * If the field type is null, then <code>this</code> is returned.
     * <p>
     * These lines are equivalent:
     * <pre>
     * LocalTime updated = dt.withHourOfDay(6);
     * LocalTime updated = dt.withField(DateTimeFieldType.hourOfDay(), 6);
     * </pre>
     *
     * @param fieldType  the field type to set, not null
     * @param value  the value to set
     * @return a copy of this time with the field set
     * @throws IllegalArgumentException if the value is null or invalid
     */
    public LocalTime withField(DateTimeFieldType fieldType, int value) {
        if (fieldType == null) {
            throw new IllegalArgumentException("Field must not be null");
        }
        if (isSupported(fieldType) == false) {
            throw new IllegalArgumentException("Field '" + fieldType + "' is not supported");
        }
        long instant = fieldType.getField(getChronology()).set(getLocalMillis(), value);
        return withLocalMillis(instant);
    }

    /**
     * Returns a copy of this time with the value of the specified
     * field increased.
     * <p>
     * If the addition is zero or the field is null, then <code>this</code>
     * is returned.
     * <p>
     * If the addition causes the maximum value of the field to be exceeded,
     * then the value will wrap. Thus 23:59 plus two minutes yields 00:01.
     * <p>
     * These lines are equivalent:
     * <pre>
     * LocalTime added = dt.plusHours(6);
     * LocalTime added = dt.withFieldAdded(DurationFieldType.hours(), 6);
     * </pre>
     *
     * @param fieldType  the field type to add to, not null
     * @param amount  the amount to add
     * @return a copy of this time with the field updated
     * @throws IllegalArgumentException if the value is null or invalid
     * @throws ArithmeticException if the result exceeds the internal capacity
     */
    public LocalTime withFieldAdded(DurationFieldType fieldType, int amount) {
        if (fieldType == null) {
            throw new IllegalArgumentException("Field must not be null");
        }
        if (isSupported(fieldType) == false) {
            throw new IllegalArgumentException("Field '" + fieldType + "' is not supported");
        }
        if (amount == 0) {
            return this;
        }
        long instant = fieldType.getField(getChronology()).add(getLocalMillis(), amount);
        return withLocalMillis(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this time with the specified period added.
     * <p>
     * If the addition is zero, then <code>this</code> is returned.
     * <p>
     * This method is typically used to add multiple copies of complex
     * period instances. Adding one field is best achieved using methods
     * like {@link #withFieldAdded(DurationFieldType, int)}
     * or {@link #plusHours(int)}.
     *
     * @param period  the period to add to this one, null means zero
     * @param scalar  the amount of times to add, such as -1 to subtract once
     * @return a copy of this time with the period added
     * @throws ArithmeticException if the result exceeds the internal capacity
     */
    public LocalTime withPeriodAdded(ReadablePeriod period, int scalar) {
        if (period == null || scalar == 0) {
            return this;
        }
        long instant = getChronology().add(period, getLocalMillis(), scalar);
        return withLocalMillis(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this time with the specified period added.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * <p>
     * This method is typically used to add complex period instances.
     * Adding one field is best achieved using methods
     * like {@link #plusHours(int)}.
     * 
     * @param period  the period to add to this one, null means zero
     * @return a copy of this time with the period added
     * @throws ArithmeticException if the result exceeds the internal capacity
     */
    public LocalTime plus(ReadablePeriod period) {
        return withPeriodAdded(period, 1);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this time plus the specified number of hours.
     * <p>
     * This LocalTime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * LocalTime added = dt.plusHours(6);
     * LocalTime added = dt.plus(Period.hours(6));
     * LocalTime added = dt.withFieldAdded(DurationFieldType.hours(), 6);
     * </pre>
     *
     * @param hours  the amount of hours to add, may be negative
     * @return the new LocalTime plus the increased hours
     */
    public LocalTime plusHours(int hours) {
        if (hours == 0) {
            return this;
        }
        long instant = getChronology().hours().add(getLocalMillis(), hours);
        return withLocalMillis(instant);
    }

    /**
     * Returns a copy of this time plus the specified number of minutes.
     * <p>
     * This LocalTime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * LocalTime added = dt.plusMinutes(6);
     * LocalTime added = dt.plus(Period.minutes(6));
     * LocalTime added = dt.withFieldAdded(DurationFieldType.minutes(), 6);
     * </pre>
     *
     * @param minutes  the amount of minutes to add, may be negative
     * @return the new LocalTime plus the increased minutes
     */
    public LocalTime plusMinutes(int minutes) {
        if (minutes == 0) {
            return this;
        }
        long instant = getChronology().minutes().add(getLocalMillis(), minutes);
        return withLocalMillis(instant);
    }

    /**
     * Returns a copy of this time plus the specified number of seconds.
     * <p>
     * This LocalTime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * LocalTime added = dt.plusSeconds(6);
     * LocalTime added = dt.plus(Period.seconds(6));
     * LocalTime added = dt.withFieldAdded(DurationFieldType.seconds(), 6);
     * </pre>
     *
     * @param seconds  the amount of seconds to add, may be negative
     * @return the new LocalTime plus the increased seconds
     */
    public LocalTime plusSeconds(int seconds) {
        if (seconds == 0) {
            return this;
        }
        long instant = getChronology().seconds().add(getLocalMillis(), seconds);
        return withLocalMillis(instant);
    }

    /**
     * Returns a copy of this time plus the specified number of millis.
     * <p>
     * This LocalTime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * LocalTime added = dt.plusMillis(6);
     * LocalTime added = dt.plus(Period.millis(6));
     * LocalTime added = dt.withFieldAdded(DurationFieldType.millis(), 6);
     * </pre>
     *
     * @param millis  the amount of millis to add, may be negative
     * @return the new LocalTime plus the increased millis
     */
    public LocalTime plusMillis(int millis) {
        if (millis == 0) {
            return this;
        }
        long instant = getChronology().millis().add(getLocalMillis(), millis);
        return withLocalMillis(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this time with the specified period taken away.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * <p>
     * This method is typically used to subtract complex period instances.
     * Subtracting one field is best achieved using methods
     * like {@link #minusHours(int)}.
     * 
     * @param period  the period to reduce this instant by
     * @return a copy of this time with the period taken away
     * @throws ArithmeticException if the result exceeds the internal capacity
     */
    public LocalTime minus(ReadablePeriod period) {
        return withPeriodAdded(period, -1);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this time minus the specified number of hours.
     * <p>
     * This LocalTime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * LocalTime subtracted = dt.minusHours(6);
     * LocalTime subtracted = dt.minus(Period.hours(6));
     * LocalTime subtracted = dt.withFieldAdded(DurationFieldType.hours(), -6);
     * </pre>
     *
     * @param hours  the amount of hours to subtract, may be negative
     * @return the new LocalTime minus the increased hours
     */
    public LocalTime minusHours(int hours) {
        if (hours == 0) {
            return this;
        }
        long instant = getChronology().hours().subtract(getLocalMillis(), hours);
        return withLocalMillis(instant);
    }

    /**
     * Returns a copy of this time minus the specified number of minutes.
     * <p>
     * This LocalTime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * LocalTime subtracted = dt.minusMinutes(6);
     * LocalTime subtracted = dt.minus(Period.minutes(6));
     * LocalTime subtracted = dt.withFieldAdded(DurationFieldType.minutes(), -6);
     * </pre>
     *
     * @param minutes  the amount of minutes to subtract, may be negative
     * @return the new LocalTime minus the increased minutes
     */
    public LocalTime minusMinutes(int minutes) {
        if (minutes == 0) {
            return this;
        }
        long instant = getChronology().minutes().subtract(getLocalMillis(), minutes);
        return withLocalMillis(instant);
    }

    /**
     * Returns a copy of this time minus the specified number of seconds.
     * <p>
     * This LocalTime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * LocalTime subtracted = dt.minusSeconds(6);
     * LocalTime subtracted = dt.minus(Period.seconds(6));
     * LocalTime subtracted = dt.withFieldAdded(DurationFieldType.seconds(), -6);
     * </pre>
     *
     * @param seconds  the amount of seconds to subtract, may be negative
     * @return the new LocalTime minus the increased seconds
     */
    public LocalTime minusSeconds(int seconds) {
        if (seconds == 0) {
            return this;
        }
        long instant = getChronology().seconds().subtract(getLocalMillis(), seconds);
        return withLocalMillis(instant);
    }

    /**
     * Returns a copy of this time minus the specified number of millis.
     * <p>
     * This LocalTime instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * LocalTime subtracted = dt.minusMillis(6);
     * LocalTime subtracted = dt.minus(Period.millis(6));
     * LocalTime subtracted = dt.withFieldAdded(DurationFieldType.millis(), -6);
     * </pre>
     *
     * @param millis  the amount of millis to subtract, may be negative
     * @return the new LocalTime minus the increased millis
     */
    public LocalTime minusMillis(int millis) {
        if (millis == 0) {
            return this;
        }
        long instant = getChronology().millis().subtract(getLocalMillis(), millis);
        return withLocalMillis(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the property object for the specified type, which contains
     * many useful methods.
     *
     * @param fieldType  the field type to get the chronology for
     * @return the property object
     * @throws IllegalArgumentException if the field is null or unsupported
     */
    public Property property(DateTimeFieldType fieldType) {
        if (fieldType == null) {
            throw new IllegalArgumentException("The DateTimeFieldType must not be null");
        }
        if (isSupported(fieldType) == false) {
            throw new IllegalArgumentException("Field '" + fieldType + "' is not supported");
        }
        return new Property(this, fieldType.getField(getChronology()));
    }

    //-----------------------------------------------------------------------
    /**
     * Get the hour of day field value.
     *
     * @return the hour of day
     */
    public int getHourOfDay() {
        return getChronology().hourOfDay().get(getLocalMillis());
    }

    /**
     * Get the minute of hour field value.
     *
     * @return the minute of hour
     */
    public int getMinuteOfHour() {
        return getChronology().minuteOfHour().get(getLocalMillis());
    }

    /**
     * Get the second of minute field value.
     *
     * @return the second of minute
     */
    public int getSecondOfMinute() {
        return getChronology().secondOfMinute().get(getLocalMillis());
    }

    /**
     * Get the millis of second field value.
     *
     * @return the millis of second
     */
    public int getMillisOfSecond() {
        return getChronology().millisOfSecond().get(getLocalMillis());
    }

    /**
     * Get the millis of day field value.
     *
     * @return the millis of day
     */
    public int getMillisOfDay() {
        return getChronology().millisOfDay().get(getLocalMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this time with the hour of day field updated.
     * <p>
     * LocalTime is immutable, so there are no set methods.
     * Instead, this method returns a new instance with the value of
     * hour of day changed.
     *
     * @param hour  the hour of day to set
     * @return a copy of this object with the field set
     * @throws IllegalArgumentException if the value is invalid
     */
    public LocalTime withHourOfDay(int hour) {
        return withLocalMillis(getChronology().hourOfDay().set(getLocalMillis(), hour));
    }

    /**
     * Returns a copy of this time with the minute of hour field updated.
     * <p>
     * LocalTime is immutable, so there are no set methods.
     * Instead, this method returns a new instance with the value of
     * minute of hour changed.
     *
     * @param minute  the minute of hour to set
     * @return a copy of this object with the field set
     * @throws IllegalArgumentException if the value is invalid
     */
    public LocalTime withMinuteOfHour(int minute) {
        return withLocalMillis(getChronology().minuteOfHour().set(getLocalMillis(), minute));
    }

    /**
     * Returns a copy of this time with the second of minute field updated.
     * <p>
     * LocalTime is immutable, so there are no set methods.
     * Instead, this method returns a new instance with the value of
     * second of minute changed.
     *
     * @param second  the second of minute to set
     * @return a copy of this object with the field set
     * @throws IllegalArgumentException if the value is invalid
     */
    public LocalTime withSecondOfMinute(int second) {
        return withLocalMillis(getChronology().secondOfMinute().set(getLocalMillis(), second));
    }

    /**
     * Returns a copy of this time with the millis of second field updated.
     * <p>
     * LocalTime is immutable, so there are no set methods.
     * Instead, this method returns a new instance with the value of
     * millis of second changed.
     *
     * @param millis  the millis of second to set
     * @return a copy of this object with the field set
     * @throws IllegalArgumentException if the value is invalid
     */
    public LocalTime withMillisOfSecond(int millis) {
        return withLocalMillis(getChronology().millisOfSecond().set(getLocalMillis(), millis));
    }

    /**
     * Returns a copy of this time with the millis of day field updated.
     * <p>
     * LocalTime is immutable, so there are no set methods.
     * Instead, this method returns a new instance with the value of
     * millis of day changed.
     *
     * @param millis  the millis of day to set
     * @return a copy of this object with the field set
     * @throws IllegalArgumentException if the value is invalid
     */
    public LocalTime withMillisOfDay(int millis) {
        return withLocalMillis(getChronology().millisOfDay().set(getLocalMillis(), millis));
    }

    //-----------------------------------------------------------------------
    /**
     * Get the hour of day field property which provides access to advanced functionality.
     * 
     * @return the hour of day property
     */
    public Property hourOfDay() {
        return new Property(this, getChronology().hourOfDay());
    }

    /**
     * Get the minute of hour field property which provides access to advanced functionality.
     * 
     * @return the minute of hour property
     */
    public Property minuteOfHour() {
        return new Property(this, getChronology().minuteOfHour());
    }

    /**
     * Get the second of minute field property which provides access to advanced functionality.
     * 
     * @return the second of minute property
     */
    public Property secondOfMinute() {
        return new Property(this, getChronology().secondOfMinute());
    }

    /**
     * Get the millis of second property which provides access to advanced functionality.
     * 
     * @return the millis of second property
     */
    public Property millisOfSecond() {
        return new Property(this, getChronology().millisOfSecond());
    }

    /**
     * Get the millis of day property which provides access to advanced functionality.
     * 
     * @return the millis of day property
     */
    public Property millisOfDay() {
        return new Property(this, getChronology().millisOfDay());
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this LocalTime to a full datetime using the default time zone
     * setting the time fields from this instance and the date fields from
     * the current date.
     *
     * @return this time as a datetime using today's date
     */
    public DateTime toDateTimeToday() {
        return toDateTimeToday(null);
    }

    /**
     * Converts this LocalTime to a full datetime using the specified time zone
     * setting the time fields from this instance and the date fields from
     * the current time.
     * <p>
     * This method uses the chronology from this instance plus the time zone
     * specified.
     *
     * @param zone  the zone to use, null means default
     * @return this time as a datetime using today's date
     */
    public DateTime toDateTimeToday(DateTimeZone zone) {
        Chronology chrono = getChronology().withZone(zone);
        long instantMillis = DateTimeUtils.currentTimeMillis();
        long resolved = chrono.set(this, instantMillis);
        return new DateTime(resolved, chrono);
    }

    //-----------------------------------------------------------------------
    /**
     * Output the time in ISO8601 format (HH:mm:ss.SSS).
     * 
     * @return ISO8601 time formatted string.
     */
    @Override
    @ToString
    public String toString() {
        return ISODateTimeFormat.time().print(this);
    }

    /**
     * Output the time using the specified format pattern.
     *
     * @param pattern  the pattern specification, null means use <code>toString</code>
     * @return the formatted output, not null
     * @see org.joda.time.format.DateTimeFormat
     */
    public String toString(String pattern) {
        if (pattern == null) {
            return toString();
        }
        return DateTimeFormat.forPattern(pattern).print(this);
    }

    /**
     * Output the time using the specified format pattern.
     *
     * @param pattern  the pattern specification, null means use <code>toString</code>
     * @param locale  Locale to use, null means default
     * @return the formatted output, not null
     * @throws IllegalArgumentException if the pattern is invalid
     * @see org.joda.time.format.DateTimeFormat
     */
    public String toString(String pattern, Locale locale) throws IllegalArgumentException {
        if (pattern == null) {
            return toString();
        }
        return DateTimeFormat.forPattern(pattern).withLocale(locale).print(this);
    }

    //-----------------------------------------------------------------------
    /**
     * LocalTime.Property binds a LocalTime to a DateTimeField allowing
     * powerful datetime functionality to be easily accessed.
     * <p>
     * The simplest use of this class is as an alternative get method, here used to
     * get the minute '30'.
     * <pre>
     * LocalTime dt = new LocalTime(12, 30);
     * int year = dt.minuteOfHour().get();
     * </pre>
     * <p>
     * Methods are also provided that allow time modification. These return
     * new instances of LocalTime - they do not modify the original. The example
     * below yields two independent immutable date objects 2 hours apart.
     * <pre>
     * LocalTime dt1230 = new LocalTime(12, 30);
     * LocalTime dt1430 = dt1230.hourOfDay().setCopy(14);
     * </pre>
     * <p>
     * LocalTime.Property itself is thread-safe and immutable, as well as the
     * LocalTime being operated on.
     *
     * @author Stephen Colebourne
     * @author Brian S O'Neill
     * @since 1.3
     */
    public static final class Property extends AbstractReadableInstantFieldProperty {
        
        /** Serialization version */
        private static final long serialVersionUID = -325842547277223L;
        
        /** The instant this property is working against */
        private transient LocalTime iInstant;
        /** The field this property is working against */
        private transient DateTimeField iField;
        
        /**
         * Constructor.
         * 
         * @param instant  the instant to set
         * @param field  the field to use
         */
        Property(LocalTime instant, DateTimeField field) {
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
            iInstant = (LocalTime) oos.readObject();
            DateTimeFieldType type = (DateTimeFieldType) oos.readObject();
            iField = type.getField(iInstant.getChronology());
        }
        
        //-----------------------------------------------------------------------
        /**
         * Gets the field being used.
         * 
         * @return the field
         */
        @Override
        public DateTimeField getField() {
            return iField;
        }
        
        /**
         * Gets the milliseconds of the time that this property is linked to.
         * 
         * @return the milliseconds
         */
        @Override
        protected long getMillis() {
            return iInstant.getLocalMillis();
        }
        
        /**
         * Gets the chronology of the datetime that this property is linked to.
         * 
         * @return the chronology
         * @since 1.4
         */
        @Override
        protected Chronology getChronology() {
            return iInstant.getChronology();
        }
        
        /**
         * Gets the LocalTime object linked to this property.
         * 
         * @return the linked LocalTime
         */
        public LocalTime getLocalTime() {
            return iInstant;
        }
        
        //-----------------------------------------------------------------------
        /**
         * Adds to this field in a copy of this LocalTime.
         * <p>
         * The LocalTime attached to this property is unchanged by this call.
         *
         * @param value  the value to add to the field in the copy
         * @return a copy of the LocalTime with the field value changed
         */
        public LocalTime addCopy(int value) {
            return iInstant.withLocalMillis(iField.add(iInstant.getLocalMillis(), value));
        }
        
        /**
         * Adds to this field in a copy of this LocalTime.
         * If the addition exceeds the maximum value (eg. 23:59) it will
         * wrap to the minimum value (eg. 00:00).
         * <p>
         * The LocalTime attached to this property is unchanged by this call.
         *
         * @param value  the value to add to the field in the copy
         * @return a copy of the LocalTime with the field value changed
         */
        public LocalTime addCopy(long value) {
            return iInstant.withLocalMillis(iField.add(iInstant.getLocalMillis(), value));
        }
        
        /**
         * Adds to this field in a copy of this LocalTime.
         * If the addition exceeds the maximum value (eg. 23:59) then
         * an exception will be thrown.
         * Contrast this behaviour to {@link #addCopy(int)}.
         * <p>
         * The LocalTime attached to this property is unchanged by this call.
         *
         * @param value  the value to add to the field in the copy
         * @return a copy of the LocalTime with the field value changed
         * @throws IllegalArgumentException if the result is invalid
         */
        public LocalTime addNoWrapToCopy(int value) {
            long millis = iField.add(iInstant.getLocalMillis(), value);
            long rounded = iInstant.getChronology().millisOfDay().get(millis);
            if (rounded != millis) {
                throw new IllegalArgumentException("The addition exceeded the boundaries of LocalTime");
            }
            return iInstant.withLocalMillis(millis);
        }
        
        /**
         * Adds to this field, possibly wrapped, in a copy of this LocalTime.
         * A field wrapped operation only changes this field.
         * Thus 10:59 plusWrapField one minute goes to 10:00.
         * <p>
         * The LocalTime attached to this property is unchanged by this call.
         *
         * @param value  the value to add to the field in the copy
         * @return a copy of the LocalTime with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public LocalTime addWrapFieldToCopy(int value) {
            return iInstant.withLocalMillis(iField.addWrapField(iInstant.getLocalMillis(), value));
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
            return iInstant.withLocalMillis(iField.set(iInstant.getLocalMillis(), value));
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
            return iInstant.withLocalMillis(iField.set(iInstant.getLocalMillis(), text, locale));
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
         * Returns a new LocalTime with this field set to the maximum value
         * for this field.
         * <p>
         * The LocalTime attached to this property is unchanged by this call.
         *
         * @return a copy of the LocalTime with this field set to its maximum
         */
        public LocalTime withMaximumValue() {
            return setCopy(getMaximumValue());
        }
        
        /**
         * Returns a new LocalTime with this field set to the minimum value
         * for this field.
         * <p>
         * The LocalTime attached to this property is unchanged by this call.
         *
         * @return a copy of the LocalTime with this field set to its minimum
         */
        public LocalTime withMinimumValue() {
            return setCopy(getMinimumValue());
        }
        
        //-----------------------------------------------------------------------
        /**
         * Rounds to the lowest whole unit of this field on a copy of this
         * LocalTime.
         * <p>
         * For example, rounding floor on the hourOfDay field of a LocalTime
         * where the time is 10:30 would result in new LocalTime with the
         * time of 10:00.
         *
         * @return a copy of the LocalTime with the field value changed
         */
        public LocalTime roundFloorCopy() {
            return iInstant.withLocalMillis(iField.roundFloor(iInstant.getLocalMillis()));
        }
        
        /**
         * Rounds to the highest whole unit of this field on a copy of this
         * LocalTime.
         * <p>
         * For example, rounding floor on the hourOfDay field of a LocalTime
         * where the time is 10:30 would result in new LocalTime with the
         * time of 11:00.
         *
         * @return a copy of the LocalTime with the field value changed
         */
        public LocalTime roundCeilingCopy() {
            return iInstant.withLocalMillis(iField.roundCeiling(iInstant.getLocalMillis()));
        }
        
        /**
         * Rounds to the nearest whole unit of this field on a copy of this
         * LocalTime, favoring the floor if halfway.
         *
         * @return a copy of the LocalTime with the field value changed
         */
        public LocalTime roundHalfFloorCopy() {
            return iInstant.withLocalMillis(iField.roundHalfFloor(iInstant.getLocalMillis()));
        }
        
        /**
         * Rounds to the nearest whole unit of this field on a copy of this
         * LocalTime, favoring the ceiling if halfway.
         *
         * @return a copy of the LocalTime with the field value changed
         */
        public LocalTime roundHalfCeilingCopy() {
            return iInstant.withLocalMillis(iField.roundHalfCeiling(iInstant.getLocalMillis()));
        }
        
        /**
         * Rounds to the nearest whole unit of this field on a copy of this
         * LocalTime.  If halfway, the ceiling is favored over the floor
         * only if it makes this field's value even.
         *
         * @return a copy of the LocalTime with the field value changed
         */
        public LocalTime roundHalfEvenCopy() {
            return iInstant.withLocalMillis(iField.roundHalfEven(iInstant.getLocalMillis()));
        }
    }

}
