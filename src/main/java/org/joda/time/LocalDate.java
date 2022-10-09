/*
 *  Copyright 2001-2013 Stephen Colebourne
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
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.base.BaseLocal;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.PartialConverter;
import org.joda.time.field.AbstractReadableInstantFieldProperty;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * LocalDate is an immutable datetime class representing a date
 * without a time zone.
 * <p>
 * LocalDate implements the {@link ReadablePartial} interface.
 * To do this, the interface methods focus on the key fields -
 * Year, MonthOfYear and DayOfMonth.
 * However, <b>all</b> date fields may in fact be queried.
 * <p>
 * LocalDate differs from DateMidnight in that this class does not
 * have a time zone and does not represent a single instant in time.
 * <p>
 * Calculations on LocalDate are performed using a {@link Chronology}.
 * This chronology will be set internally to be in the UTC time zone
 * for all calculations.
 *
 * <p>Each individual field can be queried in two ways:
 * <ul>
 * <li><code>getMonthOfYear()</code>
 * <li><code>monthOfYear().get()</code>
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
 * LocalDate is thread-safe and immutable, provided that the Chronology is as well.
 * All standard Chronology classes supplied are thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @since 1.3
 */
public final class LocalDate
        extends BaseLocal
        implements ReadablePartial, Serializable {

    /** Serialization lock */
    private static final long serialVersionUID = -8775358157899L;

    /** The index of the year field in the field array */
    private static final int YEAR = 0;
    /** The index of the monthOfYear field in the field array */
    private static final int MONTH_OF_YEAR = 1;
    /** The index of the dayOfMonth field in the field array */
    private static final int DAY_OF_MONTH = 2;
    /** Set of known duration types. */
    private static final Set<DurationFieldType> DATE_DURATION_TYPES = new HashSet<DurationFieldType>();
    static {
        DATE_DURATION_TYPES.add(DurationFieldType.days());
        DATE_DURATION_TYPES.add(DurationFieldType.weeks());
        DATE_DURATION_TYPES.add(DurationFieldType.months());
        DATE_DURATION_TYPES.add(DurationFieldType.weekyears());
        DATE_DURATION_TYPES.add(DurationFieldType.years());
        DATE_DURATION_TYPES.add(DurationFieldType.centuries());
        // eras are supported, although the DurationField generally isn't
        DATE_DURATION_TYPES.add(DurationFieldType.eras());
    }

    /** The local millis from 1970-01-01T00:00:00 */
    private final long iLocalMillis;
    /** The chronology to use in UTC. */
    private final Chronology iChronology;
    /** The cached hash code. */
    private transient int iHash;

    //-----------------------------------------------------------------------
    /**
     * Obtains a {@code LocalDate} set to the current system millisecond time
     * using <code>ISOChronology</code> in the default time zone.
     * 
     * @return the current date-time, not null
     * @since 2.0
     */
    public static LocalDate now() {
        return new LocalDate();
    }

    /**
     * Obtains a {@code LocalDate} set to the current system millisecond time
     * using <code>ISOChronology</code> in the specified time zone.
     *
     * @param zone  the time zone, not null
     * @return the current date-time, not null
     * @since 2.0
     */
    public static LocalDate now(DateTimeZone zone) {
        if (zone == null) {
            throw new NullPointerException("Zone must not be null");
        }
        return new LocalDate(zone);
    }

    /**
     * Obtains a {@code LocalDate} set to the current system millisecond time
     * using the specified chronology.
     *
     * @param chronology  the chronology, not null
     * @return the current date-time, not null
     * @since 2.0
     */
    public static LocalDate now(Chronology chronology) {
        if (chronology == null) {
            throw new NullPointerException("Chronology must not be null");
        }
        return new LocalDate(chronology);
    }

    //-----------------------------------------------------------------------
    /**
     * Parses a {@code LocalDate} from the specified string.
     * <p>
     * This uses {@link ISODateTimeFormat#localDateParser()}.
     * 
     * @param str  the string to parse, not null
     * @return the parsed date, not null
     * @since 2.0
     */
    @FromString
    public static LocalDate parse(String str) {
        return parse(str, ISODateTimeFormat.localDateParser());
    }

    /**
     * Parses a {@code LocalDate} from the specified string using a formatter.
     * 
     * @param str  the string to parse, not null
     * @param formatter  the formatter to use, not null
     * @return the parsed date, not null
     * @since 2.0
     */
    public static LocalDate parse(String str, DateTimeFormatter formatter) {
        return formatter.parseLocalDate(str);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs a LocalDate from a <code>java.util.Calendar</code>
     * using exactly the same field values.
     * <p>
     * Each field is queried from the Calendar and assigned to the LocalDate.
     * This is useful if you have been using the Calendar as a local date,
     * ignoring the zone.
     * <p>
     * One advantage of this method is that this method is unaffected if the
     * version of the time zone data differs between the JDK and Joda-Time.
     * That is because the local field values are transferred, calculated using
     * the JDK time zone data and without using the Joda-Time time zone data.
     * <p>
     * This factory method ignores the type of the calendar and always
     * creates a LocalDate with ISO chronology. It is expected that you
     * will only pass in instances of <code>GregorianCalendar</code> however
     * this is not validated.
     *
     * @param calendar  the Calendar to extract fields from, not null
     * @return the created local date, not null
     * @throws IllegalArgumentException if the calendar is null
     * @throws IllegalArgumentException if the date is invalid for the ISO chronology
     */
    public static LocalDate fromCalendarFields(Calendar calendar) {
        if (calendar == null) {
            throw new IllegalArgumentException("The calendar must not be null");
        }
        int era = calendar.get(Calendar.ERA);
        int yearOfEra = calendar.get(Calendar.YEAR);
        return new LocalDate(
            (era == GregorianCalendar.AD ? yearOfEra : 1 - yearOfEra),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        );
    }

    /**
     * Constructs a LocalDate from a <code>java.util.Date</code>
     * using exactly the same field values.
     * <p>
     * Each field is queried from the Date and assigned to the LocalDate.
     * This is useful if you have been using the Date as a local date,
     * ignoring the zone.
     * <p>
     * One advantage of this method is that this method is unaffected if the
     * version of the time zone data differs between the JDK and Joda-Time.
     * That is because the local field values are transferred, calculated using
     * the JDK time zone data and without using the Joda-Time time zone data.
     * <p>
     * This factory method always creates a LocalDate with ISO chronology.
     *
     * @param date  the Date to extract fields from, not null
     * @return the created local date, not null
     * @throws IllegalArgumentException if the calendar is null
     * @throws IllegalArgumentException if the date is invalid for the ISO chronology
     */
    @SuppressWarnings("deprecation")
    public static LocalDate fromDateFields(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        if (date.getTime() < 0) {
            // handle years in era BC
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            return fromCalendarFields(cal);
        }
        return new LocalDate(
            date.getYear() + 1900,
            date.getMonth() + 1,
            date.getDate()
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
    public LocalDate() {
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
    public LocalDate(DateTimeZone zone) {
        this(DateTimeUtils.currentTimeMillis(), ISOChronology.getInstance(zone));
    }

    /**
     * Constructs an instance set to the current local time evaluated using
     * specified chronology.
     * <p>
     * If the chronology is null, ISO chronology in the default time zone is used.
     * Once the constructor is completed, the zone is no longer used.
     *
     * @param chronology  the chronology, null means ISOChronology in default zone
     * @see #now(Chronology)
     */
    public LocalDate(Chronology chronology) {
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
    public LocalDate(long instant) {
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
    public LocalDate(long instant, DateTimeZone zone) {
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
    public LocalDate(long instant, Chronology chronology) {
        chronology = DateTimeUtils.getChronology(chronology);
        
        long localMillis = chronology.getZone().getMillisKeepLocal(DateTimeZone.UTC, instant);
        chronology = chronology.withUTC();
        iLocalMillis = chronology.dayOfMonth().roundFloor(localMillis);
        iChronology = chronology;
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance from an Object that represents a datetime.
     * The time zone will be retrieved from the object if possible,
     * otherwise the default time zone will be used.
     * <p>
     * If the object contains no chronology, <code>ISOChronology</code> is used.
     * Once the constructor is completed, the zone is no longer used.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadablePartial, ReadableInstant, String, Calendar and Date.
     * The String formats are described by {@link ISODateTimeFormat#localDateParser()}.
     * The default String converter ignores the zone and only parses the field values.
     *
     * @param instant  the datetime object
     * @throws IllegalArgumentException if the instant is invalid
     */
    public LocalDate(Object instant) {
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
     * The String formats are described by {@link ISODateTimeFormat#localDateParser()}.
     * The default String converter ignores the zone and only parses the field values.
     *
     * @param instant  the datetime object
     * @param zone  the time zone
     * @throws IllegalArgumentException if the instant is invalid
     */
    public LocalDate(Object instant, DateTimeZone zone) {
        PartialConverter converter = ConverterManager.getInstance().getPartialConverter(instant);
        Chronology chronology = converter.getChronology(instant, zone);
        chronology = DateTimeUtils.getChronology(chronology);
        iChronology = chronology.withUTC();
        int[] values = converter.getPartialValues(this, instant, chronology, ISODateTimeFormat.localDateParser());
        iLocalMillis = iChronology.getDateTimeMillis(values[0], values[1], values[2], 0);
    }

    /**
     * Constructs an instance from an Object that represents a datetime,
     * using the specified chronology.
     * <p>
     * If the chronology is null, ISO in the default time zone is used.
     * Once the constructor is completed, the zone is no longer used.
     * If the instant contains a chronology, it will be ignored.
     * For example, passing a {@code LocalDate} and a different chronology
     * will return a date with the year/month/day from the date applied
     * unaltered to the specified chronology.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadablePartial, ReadableInstant, String, Calendar and Date.
     * The String formats are described by {@link ISODateTimeFormat#localDateParser()}.
     * The default String converter ignores the zone and only parses the field values.
     *
     * @param instant  the datetime object
     * @param chronology  the chronology
     * @throws IllegalArgumentException if the instant is invalid
     */
    public LocalDate(Object instant, Chronology chronology) {
        PartialConverter converter = ConverterManager.getInstance().getPartialConverter(instant);
        chronology = converter.getChronology(instant, chronology);
        chronology = DateTimeUtils.getChronology(chronology);
        iChronology = chronology.withUTC();
        int[] values = converter.getPartialValues(this, instant, chronology, ISODateTimeFormat.localDateParser());
        iLocalMillis = iChronology.getDateTimeMillis(values[0], values[1], values[2], 0);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance set to the specified date and time
     * using <code>ISOChronology</code>.
     *
     * @param year  the year
     * @param monthOfYear  the month of the year, from 1 to 12
     * @param dayOfMonth  the day of the month, from 1 to 31
     */
    public LocalDate(
            int year,
            int monthOfYear,
            int dayOfMonth) {
        this(year, monthOfYear, dayOfMonth, ISOChronology.getInstanceUTC());
    }

    /**
     * Constructs an instance set to the specified date and time
     * using the specified chronology, whose zone is ignored.
     * <p>
     * If the chronology is null, <code>ISOChronology</code> is used.
     *
     * @param year  the year, valid values defined by the chronology
     * @param monthOfYear  the month of the year, valid values defined by the chronology
     * @param dayOfMonth  the day of the month, valid values defined by the chronology
     * @param chronology  the chronology, null means ISOChronology in default zone
     */
    public LocalDate(
            int year,
            int monthOfYear,
            int dayOfMonth,
            Chronology chronology) {
        super();
        chronology = DateTimeUtils.getChronology(chronology).withUTC();
        long instant = chronology.getDateTimeMillis(year, monthOfYear, dayOfMonth, 0);
        iChronology = chronology;
        iLocalMillis = instant;
    }

    /**
     * Handle broken serialization from other tools.
     * @return the resolved object, not null
     */
    private Object readResolve() {
        if (iChronology == null) {
            return new LocalDate(iLocalMillis, ISOChronology.getInstanceUTC());
        }
        if (DateTimeZone.UTC.equals(iChronology.getZone()) == false) {
            return new LocalDate(iLocalMillis, iChronology.withUTC());
        }
        return this;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the number of fields in this partial, which is three.
     * The supported fields are Year, MonthOfYear and DayOfMonth.
     * Note that all fields from day and above may in fact be queried via
     * other methods.
     *
     * @return the field count, three
     */
    public int size() {
        return 3;
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
            case YEAR:
                return chrono.year();
            case MONTH_OF_YEAR:
                return chrono.monthOfYear();
            case DAY_OF_MONTH:
                return chrono.dayOfMonth();
            default:
                throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
    }

    /**
     * Gets the value of the field at the specified index.
     * <p>
     * This method is required to support the <code>ReadablePartial</code>
     * interface. The supported fields are Year, MonthOfYear and DayOfMonth.
     * Note that all fields from day and above may in fact be queried via
     * other methods.
     *
     * @param index  the index, zero to two
     * @return the value
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public int getValue(int index) {
        switch (index) {
            case YEAR:
                return getChronology().year().get(getLocalMillis());
            case MONTH_OF_YEAR:
                return getChronology().monthOfYear().get(getLocalMillis());
            case DAY_OF_MONTH:
                return getChronology().dayOfMonth().get(getLocalMillis());
            default:
                throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Get the value of one of the fields of a datetime.
     * <p>
     * This method gets the value of the specified field.
     * For example:
     * <pre>
     * LocalDate dt = LocalDate.nowDefaultZone();
     * int year = dt.get(DateTimeFieldType.year());
     * </pre>
     *
     * @param fieldType  a field type, usually obtained from DateTimeFieldType, not null
     * @return the value of that field
     * @throws IllegalArgumentException if the field type is null or unsupported
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
     * local date and chronology.
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
        DurationFieldType durType = type.getDurationType();
        if (DATE_DURATION_TYPES.contains(durType) ||
                durType.getField(getChronology()).getUnitMillis() >=
                    getChronology().days().getUnitMillis()) {
            return type.getField(getChronology()).isSupported();
        }
        return false;
    }

    /**
     * Checks if the duration type specified is supported by this
     * local date and chronology.
     *
     * @param type  a duration type, usually obtained from DurationFieldType
     * @return true if the field type is supported
     */
    public boolean isSupported(DurationFieldType type) {
        if (type == null) {
            return false;
        }
        DurationField field = type.getField(getChronology());
        if (DATE_DURATION_TYPES.contains(type) ||
            field.getUnitMillis() >= getChronology().days().getUnitMillis()) {
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
     * Gets the chronology of the date.
     * 
     * @return the Chronology that the date is using
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
        if (partial instanceof LocalDate) {
            LocalDate other = (LocalDate) partial;
            if (iChronology.equals(other.iChronology)) {
                return iLocalMillis == other.iLocalMillis;
            }
        }
        return super.equals(partial);
    }

    /**
     * Gets a hash code for the instant as defined in <code>ReadablePartial</code>.
     *
     * @return a suitable hash code
     */
    @Override
    public int hashCode() {
        // override for performance
        int hash = iHash;
        if (hash == 0) {
            hash = iHash = super.hashCode();
        }
        return hash;
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
        if (partial instanceof LocalDate) {
            LocalDate other = (LocalDate) partial;
            if (iChronology.equals(other.iChronology)) {
                return (iLocalMillis < other.iLocalMillis ? -1 :
                            (iLocalMillis == other.iLocalMillis ? 0 : 1));

            }
        }
        return super.compareTo(partial);
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this LocalDate to a full datetime at the earliest valid time
     * for the date using the default time zone.
     * <p>
     * The time will normally be midnight, as that is the earliest time on
     * any given day. However, in some time zones when Daylight Savings Time
     * starts, there is no midnight because time jumps from 11:59 to 01:00.
     * This method handles that situation by returning 01:00 on that date.
     * <p>
     * This instance is immutable and unaffected by this method call.
     * 
     * @return this date as a datetime at the start of the day
     * @since 1.5
     */
    public DateTime toDateTimeAtStartOfDay() {
        return toDateTimeAtStartOfDay(null);
    }

    /**
     * Converts this LocalDate to a full datetime at the earliest valid time
     * for the date using the specified time zone.
     * <p>
     * The time will normally be midnight, as that is the earliest time on
     * any given day. However, in some time zones when Daylight Savings Time
     * starts, there is no midnight because time jumps from 11:59 to 01:00.
     * This method handles that situation by returning 01:00 on that date.
     * <p>
     * This method uses the chronology from this instance plus the time zone
     * specified.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param zone  the zone to use, null means default zone
     * @return this date as a datetime at the start of the day
     * @since 1.5
     */
    public DateTime toDateTimeAtStartOfDay(DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        Chronology chrono = getChronology().withZone(zone);
        long localMillis = getLocalMillis() + 6L * DateTimeConstants.MILLIS_PER_HOUR;
        long instant = zone.convertLocalToUTC(localMillis, false);
        instant = chrono.dayOfMonth().roundFloor(instant);
        return new DateTime(instant, chrono).withEarlierOffsetAtOverlap();
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this LocalDate to a full datetime at midnight using the default
     * time zone.
     * <p>
     * This method will throw an exception if the default time zone switches
     * to Daylight Savings Time at midnight and this LocalDate represents
     * that switchover date. The problem is that there is no such time as
     * midnight on the required date, and as such an exception is thrown.
     * <p>
     * This instance is immutable and unaffected by this method call.
     * 
     * @return this date as a datetime at midnight
     * @deprecated Use {@link #toDateTimeAtStartOfDay()} which won't throw an exception
     */
    @Deprecated
    public DateTime toDateTimeAtMidnight() {
        return toDateTimeAtMidnight(null);
    }

    /**
     * Converts this LocalDate to a full datetime at midnight using the
     * specified time zone.
     * <p>
     * This method will throw an exception if the time zone switches
     * to Daylight Savings Time at midnight and this LocalDate represents
     * that switchover date. The problem is that there is no such time as
     * midnight on the required date, and as such an exception is thrown.
     * <p>
     * This method uses the chronology from this instance plus the time zone
     * specified.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param zone  the zone to use, null means default zone
     * @return this date as a datetime at midnight
     * @deprecated Use {@link #toDateTimeAtStartOfDay(DateTimeZone)} which won't throw an exception
     */
    @Deprecated
    public DateTime toDateTimeAtMidnight(DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        Chronology chrono = getChronology().withZone(zone);
        return new DateTime(getYear(), getMonthOfYear(), getDayOfMonth(), 0, 0, 0, 0, chrono);
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this LocalDate to a full datetime using the default time zone
     * setting the date fields from this instance and the time fields from
     * the current time.
     * <p>
     * This method will throw an exception if the datetime that would be
     * created does not exist when the time zone is taken into account.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @return this date as a datetime with the time as the current time
     */
    public DateTime toDateTimeAtCurrentTime() {
        return toDateTimeAtCurrentTime(null);
    }

    /**
     * Converts this LocalDate to a full datetime using the specified time zone
     * setting the date fields from this instance and the time fields from
     * the current time.
     * <p>
     * This method uses the chronology from this instance plus the time zone
     * specified.
     * <p>
     * This method will throw an exception if the datetime that would be
     * created does not exist when the time zone is taken into account.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param zone  the zone to use, null means default zone
     * @return this date as a datetime with the time as the current time
     */
    public DateTime toDateTimeAtCurrentTime(DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        Chronology chrono = getChronology().withZone(zone);
        long instantMillis = DateTimeUtils.currentTimeMillis();
        long resolved = chrono.set(this, instantMillis);
        return new DateTime(resolved, chrono);
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this LocalDate to a DateMidnight in the default time zone.
     * <p>
     * As from v1.5, you are recommended to avoid DateMidnight and use
     * {@link #toDateTimeAtStartOfDay()} instead because of the exception
     * detailed below.
     * <p>
     * This method will throw an exception if the default time zone switches
     * to Daylight Savings Time at midnight and this LocalDate represents
     * that switchover date. The problem is that there is no such time as
     * midnight on the required date, and as such an exception is thrown.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @return the DateMidnight instance in the default zone
     * @deprecated DateMidnight is deprecated
     */
    @Deprecated
    public DateMidnight toDateMidnight() {
        return toDateMidnight(null);
    }

    /**
     * Converts this LocalDate to a DateMidnight.
     * <p>
     * As from v1.5, you are recommended to avoid DateMidnight and use
     * {@link #toDateTimeAtStartOfDay()} instead because of the exception
     * detailed below.
     * <p>
     * This method will throw an exception if the time zone switches
     * to Daylight Savings Time at midnight and this LocalDate represents
     * that switchover date. The problem is that there is no such time as
     * midnight on the required date, and as such an exception is thrown.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param zone  the zone to get the DateMidnight in, null means default zone
     * @return the DateMidnight instance
     * @deprecated DateMidnight is deprecated
     */
    @Deprecated
    public DateMidnight toDateMidnight(DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        Chronology chrono = getChronology().withZone(zone);
        return new DateMidnight(getYear(), getMonthOfYear(), getDayOfMonth(), chrono);
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this object to a LocalDateTime using a LocalTime to fill in
     * the missing fields.
     * <p>
     * The resulting chronology is determined by the chronology of this
     * LocalDate. The chronology of the time must also match.
     * If the time is null an exception is thrown.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param time  the time of day to use, must not be null
     * @return the LocalDateTime instance
     * @throws IllegalArgumentException if the time is null
     * @throws IllegalArgumentException if the chronology of the time does not match
     * @since 1.5
     */
    public LocalDateTime toLocalDateTime(LocalTime time) {
        if (time == null) {
            throw new IllegalArgumentException("The time must not be null");
        }
        if (getChronology() != time.getChronology()) {
            throw new IllegalArgumentException("The chronology of the time does not match");
        }
        long localMillis = getLocalMillis() + time.getLocalMillis();
        return new LocalDateTime(localMillis, getChronology());
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this object to a DateTime using a LocalTime to fill in the
     * missing fields and using the default time zone.
     * <p>
     * The resulting chronology is determined by the chronology of this
     * LocalDate. The chronology of the time must match.
     * <p>
     * If the time is null, this method delegates to {@link #toDateTimeAtCurrentTime(DateTimeZone)}
     * and the following documentation does not apply.
     * <p>
     * When the time zone is applied, the local date-time may be affected by daylight saving.
     * In a daylight saving gap, when the local time does not exist,
     * this method will throw an exception.
     * In a daylight saving overlap, when the same local time occurs twice,
     * this method returns the first occurrence of the local time.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param time  the time of day to use, null uses current time
     * @return the DateTime instance
     * @throws IllegalArgumentException if the chronology of the time does not match
     * @throws IllegalInstantException if the local time does not exist when the time zone is applied
     */
    public DateTime toDateTime(LocalTime time) {
        return toDateTime(time, null);
    }

    /**
     * Converts this object to a DateTime using a LocalTime to fill in the
     * missing fields.
     * <p>
     * The resulting chronology is determined by the chronology of this
     * LocalDate plus the time zone. The chronology of the time must match.
     * <p>
     * If the time is null, this method delegates to {@link #toDateTimeAtCurrentTime(DateTimeZone)}
     * and the following documentation does not apply.
     * <p>
     * When the time zone is applied, the local date-time may be affected by daylight saving.
     * In a daylight saving gap, when the local time does not exist,
     * this method will throw an exception.
     * In a daylight saving overlap, when the same local time occurs twice,
     * this method returns the first occurrence of the local time.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param time  the time of day to use, null uses current time
     * @param zone  the zone to get the DateTime in, null means default
     * @return the DateTime instance
     * @throws IllegalArgumentException if the chronology of the time does not match
     * @throws IllegalInstantException if the local time does not exist when the time zone is applied
     */
    public DateTime toDateTime(LocalTime time, DateTimeZone zone) {
        if (time == null) {
            return toDateTimeAtCurrentTime(zone);
        }
        if (getChronology() != time.getChronology()) {
            throw new IllegalArgumentException("The chronology of the time does not match");
        }
        Chronology chrono = getChronology().withZone(zone);
        return new DateTime(
                        getYear(), getMonthOfYear(), getDayOfMonth(),
                        time.getHourOfDay(), time.getMinuteOfHour(),
                        time.getSecondOfMinute(), time.getMillisOfSecond(), chrono);
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this object to an Interval representing the whole day
     * in the default time zone.
     * <p>
     * The interval may have more or less than 24 hours if this is a daylight
     * savings cutover date.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @return a interval over the day
     */
    public Interval toInterval() {
        return toInterval(null);
    }

    /**
     * Converts this object to an Interval representing the whole day.
     * <p>
     * The interval may have more or less than 24 hours if this is a daylight
     * savings cutover date.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param zone  the zone to get the Interval in, null means default
     * @return a interval over the day
     */
    public Interval toInterval(DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        DateTime start = toDateTimeAtStartOfDay(zone);
        DateTime end = plusDays(1).toDateTimeAtStartOfDay(zone);
        return new Interval(start, end);
    }

    //-----------------------------------------------------------------------
    /**
     * Get the date time as a <code>java.util.Date</code>.
     * <p>
     * The <code>Date</code> object created has exactly the same year, month and day
     * as this date. The time will be set to the earliest valid time for that date.
     * <p>
     * Converting to a JDK Date is full of complications as the JDK Date constructor
     * doesn't behave as you might expect around DST transitions. This method works
     * by taking a first guess and then adjusting the JDK date until it has the
     * earliest valid instant. This also handles the situation where the JDK time
     * zone data differs from the Joda-Time time zone data.
     *
     * @return a Date initialised with this date, never null
     * @since 2.0
     */
    @SuppressWarnings("deprecation")
    public Date toDate() {
        int dom = getDayOfMonth();
        Date date = new Date(getYear() - 1900, getMonthOfYear() - 1, dom);
        LocalDate check = LocalDate.fromDateFields(date);
        if (check.isBefore(this)) {
            // DST gap (no midnight)
            // move forward in units of one hour until date correct
            while (check.equals(this) == false) {
                date.setTime(date.getTime() + 3600000);
                check = LocalDate.fromDateFields(date);
            }
            // move back in units of one second until date wrong
            while (date.getDate() == dom) {
                date.setTime(date.getTime() - 1000);
            }
            // fix result
            date.setTime(date.getTime() + 1000);
        } else if (check.equals(this)) {
            // check for DST overlap (two midnights)
            Date earlier = new Date(date.getTime() - TimeZone.getDefault().getDSTSavings());
            if (earlier.getDate() == dom) {
                date = earlier;
            }
        }
        return date;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this date with different local millis.
     * <p>
     * The returned object will be a new instance of the same type.
     * Only the millis will change, the chronology is kept.
     * The returned object will be either be a new instance or <code>this</code>.
     *
     * @param newMillis  the new millis, from 1970-01-01T00:00:00
     * @return a copy of this date with different millis
     */
    LocalDate withLocalMillis(long newMillis) {
        newMillis = iChronology.dayOfMonth().roundFloor(newMillis);
        return (newMillis == getLocalMillis() ? this : new LocalDate(newMillis, getChronology()));
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this date with the partial set of fields replacing
     * those from this instance.
     * <p>
     * For example, if the partial contains a year and a month then those two
     * fields will be changed in the returned instance.
     * Unsupported fields are ignored.
     * If the partial is null, then <code>this</code> is returned.
     *
     * @param partial  the partial set of fields to apply to this date, null ignored
     * @return a copy of this date with a different set of fields
     * @throws IllegalArgumentException if any value is invalid
     */
    public LocalDate withFields(ReadablePartial partial) {
        if (partial == null) {
            return this;
        }
        return withLocalMillis(getChronology().set(partial, getLocalMillis()));
    }

    /**
     * Returns a copy of this date with the specified field set to a new value.
     * <p>
     * For example, if the field type is <code>monthOfYear</code> then the
     * month of year field will be changed in the returned instance.
     * If the field type is null, then <code>this</code> is returned.
     * <p>
     * These two lines are equivalent:
     * <pre>
     * LocalDate updated = dt.withDayOfMonth(6);
     * LocalDate updated = dt.withField(DateTimeFieldType.dayOfMonth(), 6);
     * </pre>
     *
     * @param fieldType  the field type to set, not null
     * @param value  the value to set
     * @return a copy of this date with the field set
     * @throws IllegalArgumentException if the field is null or unsupported
     */
    public LocalDate withField(DateTimeFieldType fieldType, int value) {
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
     * Returns a copy of this date with the value of the specified field increased.
     * <p>
     * If the addition is zero or the field is null, then <code>this</code> is returned.
     * <p>
     * These three lines are equivalent:
     * <pre>
     * LocalDate added = dt.withFieldAdded(DurationFieldType.years(), 6);
     * LocalDate added = dt.plusYears(6);
     * LocalDate added = dt.plus(Period.years(6));
     * </pre>
     *
     * @param fieldType  the field type to add to, not null
     * @param amount  the amount to add
     * @return a copy of this date with the field updated
     * @throws IllegalArgumentException if the field is null or unsupported
     * @throws ArithmeticException if the result exceeds the internal capacity
     */
    public LocalDate withFieldAdded(DurationFieldType fieldType, int amount) {
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
     * Returns a copy of this date with the specified period added.
     * <p>
     * If the addition is zero, then <code>this</code> is returned.
     * <p>
     * This method is typically used to add multiple copies of complex
     * period instances. Adding one field is best achieved using methods
     * like {@link #withFieldAdded(DurationFieldType, int)}
     * or {@link #plusYears(int)}.
     * <p>
     * Unsupported time fields are ignored, thus adding a period of 24 hours
     * will not have any effect.
     *
     * @param period  the period to add to this one, null means zero
     * @param scalar  the amount of times to add, such as -1 to subtract once
     * @return a copy of this date with the period added
     * @throws ArithmeticException if the result exceeds the internal capacity
     */
    public LocalDate withPeriodAdded(ReadablePeriod period, int scalar) {
        if (period == null || scalar == 0) {
            return this;
        }
        long instant = getLocalMillis();
        Chronology chrono = getChronology();
        for (int i = 0; i < period.size(); i++) {
            long value = FieldUtils.safeMultiply(period.getValue(i), scalar);
            DurationFieldType type = period.getFieldType(i);
            if (isSupported(type)) {
                instant = type.getField(chrono).add(instant, value);
            }
        }
        return withLocalMillis(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this date with the specified period added.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * <p>
     * This method is typically used to add complex period instances.
     * Adding one field is best achieved using methods
     * like {@link #plusYears(int)}.
     * <p>
     * Unsupported time fields are ignored, thus adding a period of 24 hours
     * will not have any effect.
     *
     * @param period  the period to add to this one, null means zero
     * @return a copy of this date with the period added
     * @throws ArithmeticException if the result exceeds the internal capacity
     */
    public LocalDate plus(ReadablePeriod period) {
        return withPeriodAdded(period, 1);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this date plus the specified number of years.
     * <p>
     * This adds the specified number of years to the date.
     * If adding years makes the day-of-month invalid, it is adjusted to the last valid day in the month.
     * This LocalDate instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * LocalDate added = dt.plusYears(6);
     * LocalDate added = dt.plus(Period.years(6));
     * LocalDate added = dt.withFieldAdded(DurationFieldType.years(), 6);
     * </pre>
     *
     * @param years  the amount of years to add, may be negative
     * @return the new LocalDate plus the increased years
     */
    public LocalDate plusYears(int years) {
        if (years == 0) {
            return this;
        }
        long instant = getChronology().years().add(getLocalMillis(), years);
        return withLocalMillis(instant);
    }

    /**
     * Returns a copy of this date plus the specified number of months.
     * <p>
     * This adds the specified number of months to the date.
     * The addition may change the year, but the day-of-month is normally unchanged.
     * If adding months makes the day-of-month invalid, it is adjusted to the last valid day in the month.
     * This LocalDate instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * LocalDate added = dt.plusMonths(6);
     * LocalDate added = dt.plus(Period.months(6));
     * LocalDate added = dt.withFieldAdded(DurationFieldType.months(), 6);
     * </pre>
     *
     * @param months  the amount of months to add, may be negative
     * @return the new LocalDate plus the increased months
     */
    public LocalDate plusMonths(int months) {
        if (months == 0) {
            return this;
        }
        long instant = getChronology().months().add(getLocalMillis(), months);
        return withLocalMillis(instant);
    }

    /**
     * Returns a copy of this date plus the specified number of weeks.
     * <p>
     * This LocalDate instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * LocalDate added = dt.plusWeeks(6);
     * LocalDate added = dt.plus(Period.weeks(6));
     * LocalDate added = dt.withFieldAdded(DurationFieldType.weeks(), 6);
     * </pre>
     *
     * @param weeks  the amount of weeks to add, may be negative
     * @return the new LocalDate plus the increased weeks
     */
    public LocalDate plusWeeks(int weeks) {
        if (weeks == 0) {
            return this;
        }
        long instant = getChronology().weeks().add(getLocalMillis(), weeks);
        return withLocalMillis(instant);
    }

    /**
     * Returns a copy of this date plus the specified number of days.
     * <p>
     * This LocalDate instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * LocalDate added = dt.plusDays(6);
     * LocalDate added = dt.plus(Period.days(6));
     * LocalDate added = dt.withFieldAdded(DurationFieldType.days(), 6);
     * </pre>
     *
     * @param days  the amount of days to add, may be negative
     * @return the new LocalDate plus the increased days
     */
    public LocalDate plusDays(int days) {
        if (days == 0) {
            return this;
        }
        long instant = getChronology().days().add(getLocalMillis(), days);
        return withLocalMillis(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this date with the specified period taken away.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * <p>
     * This method is typically used to subtract complex period instances.
     * Subtracting one field is best achieved using methods
     * like {@link #minusYears(int)}.
     * <p>
     * Unsupported time fields are ignored, thus subtracting a period of 24 hours
     * will not have any effect.
     *
     * @param period  the period to reduce this instant by
     * @return a copy of this LocalDate with the period taken away
     * @throws ArithmeticException if the result exceeds the internal capacity
     */
    public LocalDate minus(ReadablePeriod period) {
        return withPeriodAdded(period, -1);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this date minus the specified number of years.
     * <p>
     * This subtracts the specified number of years from the date.
     * If subtracting years makes the day-of-month invalid, it is adjusted to the last valid day in the month.
     * This LocalDate instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * LocalDate subtracted = dt.minusYears(6);
     * LocalDate subtracted = dt.minus(Period.years(6));
     * LocalDate subtracted = dt.withFieldAdded(DurationFieldType.years(), -6);
     * </pre>
     *
     * @param years  the amount of years to subtract, may be negative
     * @return the new LocalDate minus the increased years
     */
    public LocalDate minusYears(int years) {
        if (years == 0) {
            return this;
        }
        long instant = getChronology().years().subtract(getLocalMillis(), years);
        return withLocalMillis(instant);
    }

    /**
     * Returns a copy of this date minus the specified number of months.
     * <p>
     * This subtracts the specified number of months from the date.
     * The subtraction may change the year, but the day-of-month is normally unchanged.
     * If subtracting months makes the day-of-month invalid, it is adjusted to the last valid day in the month.
     * This LocalDate instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * LocalDate subtracted = dt.minusMonths(6);
     * LocalDate subtracted = dt.minus(Period.months(6));
     * LocalDate subtracted = dt.withFieldAdded(DurationFieldType.months(), -6);
     * </pre>
     *
     * @param months  the amount of months to subtract, may be negative
     * @return the new LocalDate minus the increased months
     */
    public LocalDate minusMonths(int months) {
        if (months == 0) {
            return this;
        }
        long instant = getChronology().months().subtract(getLocalMillis(), months);
        return withLocalMillis(instant);
    }

    /**
     * Returns a copy of this date minus the specified number of weeks.
     * <p>
     * This LocalDate instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * LocalDate subtracted = dt.minusWeeks(6);
     * LocalDate subtracted = dt.minus(Period.weeks(6));
     * LocalDate subtracted = dt.withFieldAdded(DurationFieldType.weeks(), -6);
     * </pre>
     *
     * @param weeks  the amount of weeks to subtract, may be negative
     * @return the new LocalDate minus the increased weeks
     */
    public LocalDate minusWeeks(int weeks) {
        if (weeks == 0) {
            return this;
        }
        long instant = getChronology().weeks().subtract(getLocalMillis(), weeks);
        return withLocalMillis(instant);
    }

    /**
     * Returns a copy of this date minus the specified number of days.
     * <p>
     * This LocalDate instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * LocalDate subtracted = dt.minusDays(6);
     * LocalDate subtracted = dt.minus(Period.days(6));
     * LocalDate subtracted = dt.withFieldAdded(DurationFieldType.days(), -6);
     * </pre>
     *
     * @param days  the amount of days to subtract, may be negative
     * @return the new LocalDate minus the increased days
     */
    public LocalDate minusDays(int days) {
        if (days == 0) {
            return this;
        }
        long instant = getChronology().days().subtract(getLocalMillis(), days);
        return withLocalMillis(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the property object for the specified type, which contains many
     * useful methods.
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
     * <p>
     * The weekyear is the year that matches with the weekOfWeekyear field.
     * In the standard ISO8601 week algorithm, the first week of the year
     * is that in which at least 4 days are in the year. As a result of this
     * definition, day 1 of the first week may be in the previous year.
     * The weekyear allows you to query the effective year for that day.
     *
     * @return the weekyear
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
     * <p>
     * This field is associated with the "weekyear" via {@link #getWeekyear()}.
     * In the standard ISO8601 week algorithm, the first week of the year
     * is that in which at least 4 days are in the year. As a result of this
     * definition, day 1 of the first week may be in the previous year.
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

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this date with the era field updated.
     * <p>
     * LocalDate is immutable, so there are no set methods.
     * Instead, this method returns a new instance with the value of
     * era changed.
     *
     * @param era  the era to set
     * @return a copy of this object with the field set
     * @throws IllegalArgumentException if the value is invalid
     */
    public LocalDate withEra(int era) {
        return withLocalMillis(getChronology().era().set(getLocalMillis(), era));
    }

    /**
     * Returns a copy of this date with the century of era field updated.
     * <p>
     * LocalDate is immutable, so there are no set methods.
     * Instead, this method returns a new instance with the value of
     * century of era changed.
     *
     * @param centuryOfEra  the century of era to set
     * @return a copy of this object with the field set
     * @throws IllegalArgumentException if the value is invalid
     */
    public LocalDate withCenturyOfEra(int centuryOfEra) {
        return withLocalMillis(getChronology().centuryOfEra().set(getLocalMillis(), centuryOfEra));
    }

    /**
     * Returns a copy of this date with the year of era field updated.
     * <p>
     * LocalDate is immutable, so there are no set methods.
     * Instead, this method returns a new instance with the value of
     * year of era changed.
     *
     * @param yearOfEra  the year of era to set
     * @return a copy of this object with the field set
     * @throws IllegalArgumentException if the value is invalid
     */
    public LocalDate withYearOfEra(int yearOfEra) {
        return withLocalMillis(getChronology().yearOfEra().set(getLocalMillis(), yearOfEra));
    }

    /**
     * Returns a copy of this date with the year of century field updated.
     * <p>
     * LocalDate is immutable, so there are no set methods.
     * Instead, this method returns a new instance with the value of
     * year of century changed.
     *
     * @param yearOfCentury  the year of century to set
     * @return a copy of this object with the field set
     * @throws IllegalArgumentException if the value is invalid
     */
    public LocalDate withYearOfCentury(int yearOfCentury) {
        return withLocalMillis(getChronology().yearOfCentury().set(getLocalMillis(), yearOfCentury));
    }

    /**
     * Returns a copy of this date with the year field updated.
     * <p>
     * LocalDate is immutable, so there are no set methods.
     * Instead, this method returns a new instance with the value of
     * year changed.
     *
     * @param year  the year to set
     * @return a copy of this object with the field set
     * @throws IllegalArgumentException if the value is invalid
     */
    public LocalDate withYear(int year) {
        return withLocalMillis(getChronology().year().set(getLocalMillis(), year));
    }

    /**
     * Returns a copy of this date with the weekyear field updated.
     * <p>
     * The weekyear is the year that matches with the weekOfWeekyear field.
     * In the standard ISO8601 week algorithm, the first week of the year
     * is that in which at least 4 days are in the year. As a result of this
     * definition, day 1 of the first week may be in the previous year.
     * The weekyear allows you to query the effective year for that day.
     * <p>
     * LocalDate is immutable, so there are no set methods.
     * Instead, this method returns a new instance with the value of
     * weekyear changed.
     *
     * @param weekyear  the weekyear to set
     * @return a copy of this object with the field set
     * @throws IllegalArgumentException if the value is invalid
     */
    public LocalDate withWeekyear(int weekyear) {
        return withLocalMillis(getChronology().weekyear().set(getLocalMillis(), weekyear));
    }

    /**
     * Returns a copy of this date with the month of year field updated.
     * <p>
     * LocalDate is immutable, so there are no set methods.
     * Instead, this method returns a new instance with the value of
     * month of year changed.
     *
     * @param monthOfYear  the month of year to set
     * @return a copy of this object with the field set
     * @throws IllegalArgumentException if the value is invalid
     */
    public LocalDate withMonthOfYear(int monthOfYear) {
        return withLocalMillis(getChronology().monthOfYear().set(getLocalMillis(), monthOfYear));
    }

    /**
     * Returns a copy of this date with the week of weekyear field updated.
     * <p>
     * This field is associated with the "weekyear" via {@link #withWeekyear(int)}.
     * In the standard ISO8601 week algorithm, the first week of the year
     * is that in which at least 4 days are in the year. As a result of this
     * definition, day 1 of the first week may be in the previous year.
     * <p>
     * LocalDate is immutable, so there are no set methods.
     * Instead, this method returns a new instance with the value of
     * week of weekyear changed.
     *
     * @param weekOfWeekyear  the week of weekyear to set
     * @return a copy of this object with the field set
     * @throws IllegalArgumentException if the value is invalid
     */
    public LocalDate withWeekOfWeekyear(int weekOfWeekyear) {
        return withLocalMillis(getChronology().weekOfWeekyear().set(getLocalMillis(), weekOfWeekyear));
    }

    /**
     * Returns a copy of this date with the day of year field updated.
     * <p>
     * LocalDate is immutable, so there are no set methods.
     * Instead, this method returns a new instance with the value of
     * day of year changed.
     *
     * @param dayOfYear  the day of year to set
     * @return a copy of this object with the field set
     * @throws IllegalArgumentException if the value is invalid
     */
    public LocalDate withDayOfYear(int dayOfYear) {
        return withLocalMillis(getChronology().dayOfYear().set(getLocalMillis(), dayOfYear));
    }

    /**
     * Returns a copy of this date with the day of month field updated.
     * <p>
     * LocalDate is immutable, so there are no set methods.
     * Instead, this method returns a new instance with the value of
     * day of month changed.
     *
     * @param dayOfMonth  the day of month to set
     * @return a copy of this object with the field set
     * @throws IllegalArgumentException if the value is invalid
     */
    public LocalDate withDayOfMonth(int dayOfMonth) {
        return withLocalMillis(getChronology().dayOfMonth().set(getLocalMillis(), dayOfMonth));
    }

    /**
     * Returns a copy of this date with the day of week field updated.
     * <p>
     * LocalDate is immutable, so there are no set methods.
     * Instead, this method returns a new instance with the value of
     * day of week changed.
     *
     * @param dayOfWeek  the day of week to set
     * @return a copy of this object with the field set
     * @throws IllegalArgumentException if the value is invalid
     */
    public LocalDate withDayOfWeek(int dayOfWeek) {
        return withLocalMillis(getChronology().dayOfWeek().set(getLocalMillis(), dayOfWeek));
    }

    //-----------------------------------------------------------------------
    /**
     * Get the era property which provides access to advanced functionality.
     *
     * @return the era property
     */
    public Property era() {
        return new Property(this, getChronology().era());
    }

    /**
     * Get the century of era property which provides access to advanced functionality.
     *
     * @return the year of era property
     */
    public Property centuryOfEra() {
        return new Property(this, getChronology().centuryOfEra());
    }

    /**
     * Get the year of century property which provides access to advanced functionality.
     *
     * @return the year of era property
     */
    public Property yearOfCentury() {
        return new Property(this, getChronology().yearOfCentury());
    }

    /**
     * Get the year of era property which provides access to advanced functionality.
     *
     * @return the year of era property
     */
    public Property yearOfEra() {
        return new Property(this, getChronology().yearOfEra());
    }

    /**
     * Get the year property which provides access to advanced functionality.
     *
     * @return the year property
     */
    public Property year() {
        return new Property(this, getChronology().year());
    }

    /**
     * Get the weekyear property which provides access to advanced functionality.
     *
     * @return the weekyear property
     */
    public Property weekyear() {
        return new Property(this, getChronology().weekyear());
    }

    /**
     * Get the month of year property which provides access to advanced functionality.
     *
     * @return the month of year property
     */
    public Property monthOfYear() {
        return new Property(this, getChronology().monthOfYear());
    }

    /**
     * Get the week of a week based year property which provides access to advanced functionality.
     *
     * @return the week of a week based year property
     */
    public Property weekOfWeekyear() {
        return new Property(this, getChronology().weekOfWeekyear());
    }

    /**
     * Get the day of year property which provides access to advanced functionality.
     *
     * @return the day of year property
     */
    public Property dayOfYear() {
        return new Property(this, getChronology().dayOfYear());
    }

    /**
     * Get the day of month property which provides access to advanced functionality.
     *
     * @return the day of month property
     */
    public Property dayOfMonth() {
        return new Property(this, getChronology().dayOfMonth());
    }

    /**
     * Get the day of week property which provides access to advanced functionality.
     *
     * @return the day of week property
     */
    public Property dayOfWeek() {
        return new Property(this, getChronology().dayOfWeek());
    }

    //-----------------------------------------------------------------------
    /**
     * Output the date time in ISO8601 format (yyyy-MM-dd).
     *
     * @return ISO8601 time formatted string.
     */
    @Override
    @ToString
    public String toString() {
        return ISODateTimeFormat.date().print(this);
    }

    /**
     * Output the date using the specified format pattern.
     *
     * @param pattern  the pattern specification, null means use <code>toString</code>
     * @return the formatted output
     * @see org.joda.time.format.DateTimeFormat
     */
    public String toString(String pattern) {
        if (pattern == null) {
            return toString();
        }
        return DateTimeFormat.forPattern(pattern).print(this);
    }

    /**
     * Output the date using the specified format pattern.
     *
     * @param pattern  the pattern specification, null means use <code>toString</code>
     * @param locale  Locale to use, null means default
     * @return the formatted output
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
     * LocalDate.Property binds a LocalDate to a DateTimeField allowing
     * powerful datetime functionality to be easily accessed.
     * <p>
     * The simplest use of this class is as an alternative get method, here used to
     * get the year '1972' (as an int) and the month 'December' (as a String).
     * <pre>
     * LocalDate dt = new LocalDate(1972, 12, 3, 0, 0);
     * int year = dt.year().get();
     * String monthStr = dt.month().getAsText();
     * </pre>
     * <p>
     * Methods are also provided that allow date modification. These return
     * new instances of LocalDate - they do not modify the original. The example
     * below yields two independent immutable date objects 20 years apart.
     * <pre>
     * LocalDate dt = new LocalDate(1972, 12, 3);
     * LocalDate dt1920 = dt.year().setCopy(1920);
     * </pre>
     * <p>
     * LocalDate.Property itself is thread-safe and immutable, as well as the
     * LocalDate being operated on.
     *
     * @author Stephen Colebourne
     * @author Brian S O'Neill
     * @since 1.3
     */
    public static final class Property extends AbstractReadableInstantFieldProperty {
        
        /** Serialization version */
        private static final long serialVersionUID = -3193829732634L;
        
        /** The instant this property is working against */
        private transient LocalDate iInstant;
        /** The field this property is working against */
        private transient DateTimeField iField;
        
        /**
         * Constructor.
         * 
         * @param instant  the instant to set
         * @param field  the field to use
         */
        Property(LocalDate instant, DateTimeField field) {
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
            iInstant = (LocalDate) oos.readObject();
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
         * Gets the milliseconds of the date that this property is linked to.
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
         * Gets the LocalDate object linked to this property.
         * 
         * @return the linked LocalDate
         */
        public LocalDate getLocalDate() {
            return iInstant;
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
            return iInstant.withLocalMillis(iField.add(iInstant.getLocalMillis(), value));
        }
        
        /**
         * Adds to this field, possibly wrapped, in a copy of this LocalDate.
         * A field wrapped operation only changes this field.
         * Thus 31st January addWrapField one day goes to the 1st January.
         * <p>
         * The LocalDate attached to this property is unchanged by this call.
         *
         * @param value  the value to add to the field in the copy
         * @return a copy of the LocalDate with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public LocalDate addWrapFieldToCopy(int value) {
            return iInstant.withLocalMillis(iField.addWrapField(iInstant.getLocalMillis(), value));
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
            return iInstant.withLocalMillis(iField.set(iInstant.getLocalMillis(), value));
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
            return iInstant.withLocalMillis(iField.set(iInstant.getLocalMillis(), text, locale));
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
         * Returns a new LocalDate with this field set to the maximum value
         * for this field.
         * <p>
         * This operation is useful for obtaining a LocalDate on the last day
         * of the month, as month lengths vary.
         * <pre>
         * LocalDate lastDayOfMonth = dt.dayOfMonth().withMaximumValue();
         * </pre>
         * <p>
         * The LocalDate attached to this property is unchanged by this call.
         *
         * @return a copy of the LocalDate with this field set to its maximum
         */
        public LocalDate withMaximumValue() {
            return setCopy(getMaximumValue());
        }
        
        /**
         * Returns a new LocalDate with this field set to the minimum value
         * for this field.
         * <p>
         * The LocalDate attached to this property is unchanged by this call.
         *
         * @return a copy of the LocalDate with this field set to its minimum
         */
        public LocalDate withMinimumValue() {
            return setCopy(getMinimumValue());
        }
        
        //-----------------------------------------------------------------------
        /**
         * Rounds to the lowest whole unit of this field on a copy of this
         * LocalDate.
         * <p>
         * For example, rounding floor on the hourOfDay field of a LocalDate
         * where the time is 10:30 would result in new LocalDate with the
         * time of 10:00.
         *
         * @return a copy of the LocalDate with the field value changed
         */
        public LocalDate roundFloorCopy() {
            return iInstant.withLocalMillis(iField.roundFloor(iInstant.getLocalMillis()));
        }
        
        /**
         * Rounds to the highest whole unit of this field on a copy of this
         * LocalDate.
         * <p>
         * For example, rounding floor on the hourOfDay field of a LocalDate
         * where the time is 10:30 would result in new LocalDate with the
         * time of 11:00.
         *
         * @return a copy of the LocalDate with the field value changed
         */
        public LocalDate roundCeilingCopy() {
            return iInstant.withLocalMillis(iField.roundCeiling(iInstant.getLocalMillis()));
        }
        
        /**
         * Rounds to the nearest whole unit of this field on a copy of this
         * LocalDate, favoring the floor if halfway.
         *
         * @return a copy of the LocalDate with the field value changed
         */
        public LocalDate roundHalfFloorCopy() {
            return iInstant.withLocalMillis(iField.roundHalfFloor(iInstant.getLocalMillis()));
        }
        
        /**
         * Rounds to the nearest whole unit of this field on a copy of this
         * LocalDate, favoring the ceiling if halfway.
         *
         * @return a copy of the LocalDate with the field value changed
         */
        public LocalDate roundHalfCeilingCopy() {
            return iInstant.withLocalMillis(iField.roundHalfCeiling(iInstant.getLocalMillis()));
        }
        
        /**
         * Rounds to the nearest whole unit of this field on a copy of this
         * LocalDate.  If halfway, the ceiling is favored over the floor
         * only if it makes this field's value even.
         *
         * @return a copy of the LocalDate with the field value changed
         */
        public LocalDate roundHalfEvenCopy() {
            return iInstant.withLocalMillis(iField.roundHalfEven(iInstant.getLocalMillis()));
        }
    }

}
