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

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.base.BasePartial;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.AbstractPartialFieldProperty;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * YearMonth is an immutable partial supporting the year and monthOfYear fields.
 * <p>
 * NOTE: This class only supports the two fields listed above.
 * It is impossible to query any other fields, such as dayOfWeek or centuryOfEra.
 * <p>
 * Calculations on YearMonth are performed using a {@link Chronology}.
 * This chronology is set to be in the UTC time zone for all calculations.
 * <p>
 * One use case for this class is to store a credit card expiry date, as that only
 * references the year and month.
 * This class can be used as the gYearMonth type in XML Schema.
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
 * YearMonth is thread-safe and immutable, provided that the Chronology is as well.
 * All standard Chronology classes supplied are thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @since 2.0
 */
public final class YearMonth
        extends BasePartial
        implements ReadablePartial, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 797544782896179L;
    /** The singleton set of field types */
    private static final DateTimeFieldType[] FIELD_TYPES = new DateTimeFieldType[] {
        DateTimeFieldType.year(),
        DateTimeFieldType.monthOfYear(),
    };

    /** The index of the year field in the field array */
    public static final int YEAR = 0;
    /** The index of the monthOfYear field in the field array */
    public static final int MONTH_OF_YEAR = 1;

    //-----------------------------------------------------------------------
    /**
     * Obtains a {@code YearMonth} set to the current system millisecond time
     * using <code>ISOChronology</code> in the default time zone.
     * The resulting object does not use the zone.
     * 
     * @return the current year-month, not null
     * @since 2.0
     */
    public static YearMonth now() {
        return new YearMonth();
    }

    /**
     * Obtains a {@code YearMonth} set to the current system millisecond time
     * using <code>ISOChronology</code> in the specified time zone.
     * The resulting object does not use the zone.
     *
     * @param zone  the time zone, not null
     * @return the current year-month, not null
     * @since 2.0
     */
    public static YearMonth now(DateTimeZone zone) {
        if (zone == null) {
            throw new NullPointerException("Zone must not be null");
        }
        return new YearMonth(zone);
    }

    /**
     * Obtains a {@code YearMonth} set to the current system millisecond time
     * using the specified chronology.
     * The resulting object does not use the zone.
     *
     * @param chronology  the chronology, not null
     * @return the current year-month, not null
     * @since 2.0
     */
    public static YearMonth now(Chronology chronology) {
        if (chronology == null) {
            throw new NullPointerException("Chronology must not be null");
        }
        return new YearMonth(chronology);
    }

    //-----------------------------------------------------------------------
    /**
     * Parses a {@code YearMonth} from the specified string.
     * <p>
     * This uses {@link ISODateTimeFormat#localDateParser()}.
     * 
     * @param str  the string to parse, not null
     * @since 2.0
     */
    @FromString
    public static YearMonth parse(String str) {
        return parse(str, ISODateTimeFormat.localDateParser());
    }

    /**
     * Parses a {@code YearMonth} from the specified string using a formatter.
     * 
     * @param str  the string to parse, not null
     * @param formatter  the formatter to use, not null
     * @since 2.0
     */
    public static YearMonth parse(String str, DateTimeFormatter formatter) {
        LocalDate date = formatter.parseLocalDate(str);
        return new YearMonth(date.getYear(), date.getMonthOfYear());
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs a YearMonth from a <code>java.util.Calendar</code>
     * using exactly the same field values avoiding any time zone effects.
     * <p>
     * Each field is queried from the Calendar and assigned to the YearMonth.
     * <p>
     * This factory method ignores the type of the calendar and always
     * creates a YearMonth with ISO chronology. It is expected that you
     * will only pass in instances of <code>GregorianCalendar</code> however
     * this is not validated.
     *
     * @param calendar  the Calendar to extract fields from
     * @return the created YearMonth, never null
     * @throws IllegalArgumentException if the calendar is null
     * @throws IllegalArgumentException if the year or month is invalid for the ISO chronology
     */
    public static YearMonth fromCalendarFields(Calendar calendar) {
        if (calendar == null) {
            throw new IllegalArgumentException("The calendar must not be null");
        }
        return new YearMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
    }

    /**
     * Constructs a YearMonth from a <code>java.util.Date</code>
     * using exactly the same field values avoiding any time zone effects.
     * <p>
     * Each field is queried from the Date and assigned to the YearMonth.
     * <p>
     * This factory method always creates a YearMonth with ISO chronology.
     *
     * @param date  the Date to extract fields from
     * @return the created YearMonth, never null
     * @throws IllegalArgumentException if the calendar is null
     * @throws IllegalArgumentException if the year or month is invalid for the ISO chronology
     */
    @SuppressWarnings("deprecation")
    public static YearMonth fromDateFields(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return new YearMonth(date.getYear() + 1900, date.getMonth() + 1);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs a YearMonth with the current year-month, using ISOChronology in
     * the default zone to extract the fields.
     * <p>
     * The constructor uses the default time zone, resulting in the local time
     * being initialised. Once the constructor is complete, all further calculations
     * are performed without reference to a time-zone (by switching to UTC).
     * 
     * @see #now()
     */
    public YearMonth() {
        super();
    }

    /**
     * Constructs a YearMonth with the current year-month, using ISOChronology in
     * the specified zone to extract the fields.
     * <p>
     * The constructor uses the specified time zone to obtain the current year-month.
     * Once the constructor is complete, all further calculations
     * are performed without reference to a time-zone (by switching to UTC).
     * 
     * @param zone  the zone to use, null means default zone
     * @see #now(DateTimeZone)
     */
    public YearMonth(DateTimeZone zone) {
        super(ISOChronology.getInstance(zone));
    }

    /**
     * Constructs a YearMonth with the current year-month, using the specified chronology
     * and zone to extract the fields.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a time-zone (by switching to UTC).
     *
     * @param chronology  the chronology, null means ISOChronology in the default zone
     * @see #now(Chronology)
     */
    public YearMonth(Chronology chronology) {
        super(chronology);
    }

    /**
     * Constructs a YearMonth extracting the partial fields from the specified
     * milliseconds using the ISOChronology in the default zone.
     * <p>
     * The constructor uses the default time zone, resulting in the local time
     * being initialised. Once the constructor is complete, all further calculations
     * are performed without reference to a time-zone (by switching to UTC).
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     */
    public YearMonth(long instant) {
        super(instant);
    }

    /**
     * Constructs a YearMonth extracting the partial fields from the specified
     * milliseconds using the chronology provided.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a time-zone (by switching to UTC).
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public YearMonth(long instant, Chronology chronology) {
        super(instant, chronology);
    }

    /**
     * Constructs a YearMonth from an Object that represents some form of time.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     * The String formats are described by {@link ISODateTimeFormat#localDateParser()}.
     * <p>
     * The chronology used will be derived from the object, defaulting to ISO.
     *
     * @param instant  the date-time object, null means now
     * @throws IllegalArgumentException if the instant is invalid
     */
    public YearMonth(Object instant) {
        super(instant, null, ISODateTimeFormat.localDateParser());
    }

    /**
     * Constructs a YearMonth from an Object that represents some form of time,
     * using the specified chronology.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     * The String formats are described by {@link ISODateTimeFormat#localDateParser()}.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a time-zone (by switching to UTC).
     * The specified chronology overrides that of the object.
     *
     * @param instant  the date-time object, null means now
     * @param chronology  the chronology, null means ISO default
     * @throws IllegalArgumentException if the instant is invalid
     */
    public YearMonth(Object instant, Chronology chronology) {
        super(instant, DateTimeUtils.getChronology(chronology), ISODateTimeFormat.localDateParser());
    }

    /**
     * Constructs a YearMonth with specified year and month
     * using <code>ISOChronology</code>.
     * <p>
     * The constructor uses the no time zone initialising the fields as provided.
     * Once the constructor is complete, all further calculations
     * are performed without reference to a time-zone (by switching to UTC).
     *
     * @param year  the year
     * @param monthOfYear  the month of the year
     */
    public YearMonth(int year, int monthOfYear) {
        this(year, monthOfYear, null);
    }

    /**
     * Constructs an instance set to the specified year and month
     * using the specified chronology, whose zone is ignored.
     * <p>
     * If the chronology is null, <code>ISOChronology</code> is used.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a time-zone (by switching to UTC).
     *
     * @param year  the year
     * @param monthOfYear  the month of the year
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public YearMonth(int year, int monthOfYear, Chronology chronology) {
        super(new int[] {year, monthOfYear}, chronology);
    }

    /**
     * Constructs a YearMonth with chronology from this instance and new values.
     *
     * @param partial  the partial to base this new instance on
     * @param values  the new set of values
     */
    YearMonth(YearMonth partial, int[] values) {
        super(partial, values);
    }

    /**
     * Constructs a YearMonth with values from this instance and a new chronology.
     *
     * @param partial  the partial to base this new instance on
     * @param chrono  the new chronology
     */
    YearMonth(YearMonth partial, Chronology chrono) {
        super(partial, chrono);
    }

    /**
     * Handle broken serialization from other tools.
     * @return the resolved object, not null
     */
    private Object readResolve() {
        if (DateTimeZone.UTC.equals(getChronology().getZone()) == false) {
            return new YearMonth(this, getChronology().withUTC());
        }
        return this;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the number of fields in this partial, which is two.
     * The supported fields are Year and MonthOfYear.
     * Note that only these fields may be queried.
     *
     * @return the field count, two
     */
    public int size() {
        return 2;
    }

    /**
     * Gets the field for a specific index in the chronology specified.
     * <p>
     * This method must not use any instance variables.
     * 
     * @param index  the index to retrieve
     * @param chrono  the chronology to use
     * @return the field, never null
     */
    protected DateTimeField getField(int index, Chronology chrono) {
        switch (index) {
            case YEAR:
                return chrono.year();
            case MONTH_OF_YEAR:
                return chrono.monthOfYear();
            default:
                throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
    }

    /**
     * Gets the field type at the specified index.
     *
     * @param index  the index to retrieve
     * @return the field at the specified index, never null
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public DateTimeFieldType getFieldType(int index) {
        return FIELD_TYPES[index];
    }

    /**
     * Gets an array of the field type of each of the fields that this partial supports.
     * <p>
     * The fields are returned largest to smallest, Year, Month.
     *
     * @return the array of field types (cloned), largest to smallest, never null
     */
    public DateTimeFieldType[] getFieldTypes() {
        return (DateTimeFieldType[]) FIELD_TYPES.clone();
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this year-month with the specified chronology.
     * This instance is immutable and unaffected by this method call.
     * <p>
     * This method retains the values of the fields, thus the result will
     * typically refer to a different instant.
     * <p>
     * The time zone of the specified chronology is ignored, as YearMonth
     * operates without a time zone.
     *
     * @param newChronology  the new chronology, null means ISO
     * @return a copy of this year-month with a different chronology, never null
     * @throws IllegalArgumentException if the values are invalid for the new chronology
     */
    public YearMonth withChronologyRetainFields(Chronology newChronology) {
        newChronology = DateTimeUtils.getChronology(newChronology);
        newChronology = newChronology.withUTC();
        if (newChronology == getChronology()) {
            return this;
        } else {
            YearMonth newYearMonth = new YearMonth(this, newChronology);
            newChronology.validate(newYearMonth, getValues());
            return newYearMonth;
        }
    }

    /**
     * Returns a copy of this year-month with the specified field set to a new value.
     * <p>
     * For example, if the field type is <code>monthOfYear</code> then the month
     * would be changed in the returned instance.
     * <p>
     * These three lines are equivalent:
     * <pre>
     * YearMonth updated = ym.withField(DateTimeFieldType.monthOfYear(), 6);
     * YearMonth updated = ym.monthOfYear().setCopy(6);
     * YearMonth updated = ym.property(DateTimeFieldType.monthOfYear()).setCopy(6);
     * </pre>
     *
     * @param fieldType  the field type to set, not null
     * @param value  the value to set
     * @return a copy of this instance with the field set, never null
     * @throws IllegalArgumentException if the value is null or invalid
     */
    public YearMonth withField(DateTimeFieldType fieldType, int value) {
        int index = indexOfSupported(fieldType);
        if (value == getValue(index)) {
            return this;
        }
        int[] newValues = getValues();
        newValues = getField(index).set(this, index, newValues, value);
        return new YearMonth(this, newValues);
    }

    /**
     * Returns a copy of this year-month with the value of the specified field increased.
     * <p>
     * If the addition is zero, then <code>this</code> is returned.
     * <p>
     * These three lines are equivalent:
     * <pre>
     * YearMonth added = ym.withFieldAdded(DurationFieldType.months(), 6);
     * YearMonth added = ym.plusMonths(6);
     * YearMonth added = ym.monthOfYear().addToCopy(6);
     * </pre>
     * 
     * @param fieldType  the field type to add to, not null
     * @param amount  the amount to add
     * @return a copy of this instance with the field updated, never null
     * @throws IllegalArgumentException if the value is null or invalid
     * @throws ArithmeticException if the new date-time exceeds the capacity
     */
    public YearMonth withFieldAdded(DurationFieldType fieldType, int amount) {
        int index = indexOfSupported(fieldType);
        if (amount == 0) {
            return this;
        }
        int[] newValues = getValues();
        newValues = getField(index).add(this, index, newValues, amount);
        return new YearMonth(this, newValues);
    }

    /**
     * Returns a copy of this year-month with the specified period added.
     * <p>
     * If the addition is zero, then <code>this</code> is returned.
     * Fields in the period that aren't present in the partial are ignored.
     * <p>
     * This method is typically used to add multiple copies of complex
     * period instances. Adding one field is best achieved using methods
     * like {@link #withFieldAdded(DurationFieldType, int)}
     * or {@link #plusYears(int)}.
     * 
     * @param period  the period to add to this one, null means zero
     * @param scalar  the amount of times to add, such as -1 to subtract once
     * @return a copy of this instance with the period added, never null
     * @throws ArithmeticException if the new date-time exceeds the capacity
     */
    public YearMonth withPeriodAdded(ReadablePeriod period, int scalar) {
        if (period == null || scalar == 0) {
            return this;
        }
        int[] newValues = getValues();
        for (int i = 0; i < period.size(); i++) {
            DurationFieldType fieldType = period.getFieldType(i);
            int index = indexOf(fieldType);
            if (index >= 0) {
                newValues = getField(index).add(this, index, newValues,
                        FieldUtils.safeMultiply(period.getValue(i), scalar));
            }
        }
        return new YearMonth(this, newValues);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this year-month with the specified period added.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * <p>
     * This method is typically used to add complex period instances.
     * Adding one field is best achieved using methods
     * like {@link #plusYears(int)}.
     * 
     * @param period  the duration to add to this one, null means zero
     * @return a copy of this instance with the period added, never null
     * @throws ArithmeticException if the new year-month exceeds the capacity
     */
    public YearMonth plus(ReadablePeriod period) {
        return withPeriodAdded(period, 1);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this year-month plus the specified number of years.
     * <p>
     * This year-month instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * YearMonth added = ym.plusYears(6);
     * YearMonth added = ym.plus(Period.years(6));
     * YearMonth added = ym.withFieldAdded(DurationFieldType.years(), 6);
     * </pre>
     *
     * @param years  the amount of years to add, may be negative
     * @return the new year-month plus the increased years, never null
     */
    public YearMonth plusYears(int years) {
        return withFieldAdded(DurationFieldType.years(), years);
    }

    /**
     * Returns a copy of this year-month plus the specified number of months.
     * <p>
     * This year-month instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * YearMonth added = ym.plusMonths(6);
     * YearMonth added = ym.plus(Period.months(6));
     * YearMonth added = ym.withFieldAdded(DurationFieldType.months(), 6);
     * </pre>
     *
     * @param months  the amount of months to add, may be negative
     * @return the new year-month plus the increased months, never null
     */
    public YearMonth plusMonths(int months) {
        return withFieldAdded(DurationFieldType.months(), months);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this year-month with the specified period taken away.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * <p>
     * This method is typically used to subtract complex period instances.
     * Subtracting one field is best achieved using methods
     * like {@link #minusYears(int)}.
     * 
     * @param period  the period to reduce this instant by
     * @return a copy of this instance with the period taken away, never null
     * @throws ArithmeticException if the new year-month exceeds the capacity
     */
    public YearMonth minus(ReadablePeriod period) {
        return withPeriodAdded(period, -1);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this year-month minus the specified number of years.
     * <p>
     * This year-month instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * YearMonth subtracted = ym.minusYears(6);
     * YearMonth subtracted = ym.minus(Period.years(6));
     * YearMonth subtracted = ym.withFieldAdded(DurationFieldType.years(), -6);
     * </pre>
     *
     * @param years  the amount of years to subtract, may be negative
     * @return the new year-month minus the increased years, never null
     */
    public YearMonth minusYears(int years) {
        return withFieldAdded(DurationFieldType.years(), FieldUtils.safeNegate(years));
    }

    /**
     * Returns a copy of this year-month minus the specified number of months.
     * <p>
     * This year-month instance is immutable and unaffected by this method call.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * YearMonth subtracted = ym.minusMonths(6);
     * YearMonth subtracted = ym.minus(Period.months(6));
     * YearMonth subtracted = ym.withFieldAdded(DurationFieldType.months(), -6);
     * </pre>
     *
     * @param months  the amount of months to subtract, may be negative
     * @return the new year-month minus the increased months, never null
     */
    public YearMonth minusMonths(int months) {
        return withFieldAdded(DurationFieldType.months(), FieldUtils.safeNegate(months));
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this object to a LocalDate with the same year-month and chronology.
     *
     * @param dayOfMonth the day of month to use, valid for chronology, such as 1-31 for ISO
     * @return a LocalDate with the same year-month and chronology, never null
     */
    public LocalDate toLocalDate(int dayOfMonth) {
        return new LocalDate(getYear(), getMonthOfYear(), dayOfMonth, getChronology());
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this object to an Interval representing the whole month.
     * <p>
     * The interval will use the chronology of the year-month in the default zone.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @return an interval over the month, never null
     */
    public Interval toInterval() {
        return toInterval(null);
    }

    /**
     * Converts this object to an Interval representing the whole month.
     * <p>
     * The interval will use the chronology of the year-month in the specified zone.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param zone  the zone to get the Interval in, null means default
     * @return an interval over the month, never null
     */
    public Interval toInterval(DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        DateTime start = toLocalDate(1).toDateTimeAtStartOfDay(zone);
        DateTime end = plusMonths(1).toLocalDate(1).toDateTimeAtStartOfDay(zone);
        return new Interval(start, end);
    }

    //-----------------------------------------------------------------------
    /**
     * Get the year field value.
     *
     * @return the year
     */
    public int getYear() {
        return getValue(YEAR);
    }

    /**
     * Get the month of year field value.
     *
     * @return the month of year
     */
    public int getMonthOfYear() {
        return getValue(MONTH_OF_YEAR);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this year-month with the year field updated.
     * <p>
     * YearMonth is immutable, so there are no set methods.
     * Instead, this method returns a new instance with the value of
     * year changed.
     *
     * @param year  the year to set
     * @return a copy of this object with the field set, never null
     * @throws IllegalArgumentException if the value is invalid
     */
    public YearMonth withYear(int year) {
        int[] newValues = getValues();
        newValues = getChronology().year().set(this, YEAR, newValues, year);
        return new YearMonth(this, newValues);
    }

    /**
     * Returns a copy of this year-month with the month of year field updated.
     * <p>
     * YearMonth is immutable, so there are no set methods.
     * Instead, this method returns a new instance with the value of
     * month of year changed.
     *
     * @param monthOfYear  the month of year to set
     * @return a copy of this object with the field set, never null
     * @throws IllegalArgumentException if the value is invalid
     */
    public YearMonth withMonthOfYear(int monthOfYear) {
        int[] newValues = getValues();
        newValues = getChronology().monthOfYear().set(this, MONTH_OF_YEAR, newValues, monthOfYear);
        return new YearMonth(this, newValues);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the property object for the specified type, which contains
     * many useful methods.
     *
     * @param type  the field type to get the property for
     * @return the property object
     * @throws IllegalArgumentException if the field is null or unsupported
     */
    public Property property(DateTimeFieldType type) {
        return new Property(this, indexOfSupported(type));
    }

    //-----------------------------------------------------------------------
    /**
     * Get the year field property which provides access to advanced functionality.
     * 
     * @return the year property
     */
    public Property year() {
        return new Property(this, YEAR);
    }

    /**
     * Get the month of year field property which provides access to advanced functionality.
     * 
     * @return the month of year property
     */
    public Property monthOfYear() {
        return new Property(this, MONTH_OF_YEAR);
    }

    //-----------------------------------------------------------------------
    /**
     * Output the year-month in ISO8601 format (yyyy-MM).
     *
     * @return ISO8601 time formatted string.
     */
    @ToString
    public String toString() {
        return ISODateTimeFormat.yearMonth().print(this);
    }

    /**
     * Output the year-month using the specified format pattern.
     *
     * @param pattern  the pattern specification, null means use <code>toString</code>
     * @see org.joda.time.format.DateTimeFormat
     */
    public String toString(String pattern) {
        if (pattern == null) {
            return toString();
        }
        return DateTimeFormat.forPattern(pattern).print(this);
    }

    /**
     * Output the year-month using the specified format pattern.
     *
     * @param pattern  the pattern specification, null means use <code>toString</code>
     * @param locale  Locale to use, null means default
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
     * The property class for <code>YearMonth</code>.
     * <p>
     * This class binds a <code>YearMonth</code> to a <code>DateTimeField</code>.
     * 
     * @author Stephen Colebourne
     * @since 2.0
     */
    public static class Property extends AbstractPartialFieldProperty implements Serializable {

        /** Serialization version */
        private static final long serialVersionUID = 5727734012190224363L;

        /** The partial */
        private final YearMonth iBase;
        /** The field index */
        private final int iFieldIndex;

        /**
         * Constructs a property.
         * 
         * @param partial  the partial instance
         * @param fieldIndex  the index in the partial
         */
        Property(YearMonth partial, int fieldIndex) {
            super();
            iBase = partial;
            iFieldIndex = fieldIndex;
        }

        /**
         * Gets the field that this property uses.
         * 
         * @return the field
         */
        public DateTimeField getField() {
            return iBase.getField(iFieldIndex);
        }

        /**
         * Gets the partial that this property belongs to.
         * 
         * @return the partial
         */
        protected ReadablePartial getReadablePartial() {
            return iBase;
        }

        /**
         * Gets the partial that this property belongs to.
         * 
         * @return the partial
         */
        public YearMonth getYearMonth() {
            return iBase;
        }

        /**
         * Gets the value of this field.
         * 
         * @return the field value
         */
        public int get() {
            return iBase.getValue(iFieldIndex);
        }

        //-----------------------------------------------------------------------
        /**
         * Adds to the value of this field in a copy of this YearMonth.
         * <p>
         * The value will be added to this field. If the value is too large to be
         * added solely to this field then it will affect larger fields.
         * Smaller fields are unaffected.
         * <p>
         * If the result would be too large, beyond the maximum year, then an
         * IllegalArgumentException is thrown.
         * <p>
         * The YearMonth attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param valueToAdd  the value to add to the field in the copy
         * @return a copy of the YearMonth with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public YearMonth addToCopy(int valueToAdd) {
            int[] newValues = iBase.getValues();
            newValues = getField().add(iBase, iFieldIndex, newValues, valueToAdd);
            return new YearMonth(iBase, newValues);
        }

        /**
         * Adds to the value of this field in a copy of this YearMonth wrapping
         * within this field if the maximum value is reached.
         * <p>
         * The value will be added to this field. If the value is too large to be
         * added solely to this field then it wraps within this field.
         * Other fields are unaffected.
         * <p>
         * For example,
         * <code>2004-12</code> addWrapField one month returns <code>2004-01</code>.
         * <p>
         * The YearMonth attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param valueToAdd  the value to add to the field in the copy
         * @return a copy of the YearMonth with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public YearMonth addWrapFieldToCopy(int valueToAdd) {
            int[] newValues = iBase.getValues();
            newValues = getField().addWrapField(iBase, iFieldIndex, newValues, valueToAdd);
            return new YearMonth(iBase, newValues);
        }

        //-----------------------------------------------------------------------
        /**
         * Sets this field in a copy of the YearMonth.
         * <p>
         * The YearMonth attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param value  the value to set the field in the copy to
         * @return a copy of the YearMonth with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public YearMonth setCopy(int value) {
            int[] newValues = iBase.getValues();
            newValues = getField().set(iBase, iFieldIndex, newValues, value);
            return new YearMonth(iBase, newValues);
        }

        /**
         * Sets this field in a copy of the YearMonth to a parsed text value.
         * <p>
         * The YearMonth attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param text  the text value to set
         * @param locale  optional locale to use for selecting a text symbol
         * @return a copy of the YearMonth with the field value changed
         * @throws IllegalArgumentException if the text value isn't valid
         */
        public YearMonth setCopy(String text, Locale locale) {
            int[] newValues = iBase.getValues();
            newValues = getField().set(iBase, iFieldIndex, newValues, text, locale);
            return new YearMonth(iBase, newValues);
        }

        /**
         * Sets this field in a copy of the YearMonth to a parsed text value.
         * <p>
         * The YearMonth attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param text  the text value to set
         * @return a copy of the YearMonth with the field value changed
         * @throws IllegalArgumentException if the text value isn't valid
         */
        public YearMonth setCopy(String text) {
            return setCopy(text, null);
        }
    }

}
