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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.base.BasePartial;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.AbstractPartialFieldProperty;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;

/**
 * MonthDay is an immutable partial supporting the monthOfYear and dayOfMonth fields.
 * <p>
 * NOTE: This class only supports the two fields listed above.
 * It is impossible to query any other fields, such as dayOfWeek or centuryOfEra.
 * <p>
 * Calculations on MonthDay are performed using a {@link Chronology}.
 * This chronology is set to be in the UTC time zone for all calculations.
 * <p>
 * One use case for this class is to store a birthday without the year (to avoid
 * storing the age of the person).
 * This class can be used as the gMonthDay type in XML Schema.
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
 * MonthDay is thread-safe and immutable, provided that the Chronology is as well.
 * All standard Chronology classes supplied are thread-safe and immutable.
 *
 * @author Chris Pheby
 * @since 2.0
 */
public final class MonthDay
        extends BasePartial
        implements ReadablePartial, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 2954560699050434609L;

    /** The singleton set of field types */
    private static final DateTimeFieldType[] FIELD_TYPES = new DateTimeFieldType[] {
        DateTimeFieldType.monthOfYear(),
        DateTimeFieldType.dayOfMonth(), };

    /** The singleton set of field types */
    private static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder()
        .appendOptional(ISODateTimeFormat.localDateParser().getParser())
        .appendOptional(DateTimeFormat.forPattern("--MM-dd").getParser()).toFormatter();

    /** The index of the monthOfYear field in the field array */
    public static final int MONTH_OF_YEAR = 0;
    /** The index of the day field in the field array */
    public static final int DAY_OF_MONTH = 1;

    //-----------------------------------------------------------------------
    /**
     * Obtains a {@code MonthDay} set to the current system millisecond time
     * using <code>ISOChronology</code> in the default time zone.
     * The resulting object does not use the zone.
     * 
     * @return the current month-day, not null
     * @since 2.0
     */
    public static MonthDay now() {
        return new MonthDay();
    }

    /**
     * Obtains a {@code MonthDay} set to the current system millisecond time
     * using <code>ISOChronology</code> in the specified time zone.
     * The resulting object does not use the zone.
     *
     * @param zone  the time zone, not null
     * @return the current month-day, not null
     * @since 2.0
     */
    public static MonthDay now(DateTimeZone zone) {
        if (zone == null) {
            throw new NullPointerException("Zone must not be null");
        }
        return new MonthDay(zone);
    }

    /**
     * Obtains a {@code MonthDay} set to the current system millisecond time
     * using the specified chronology.
     * The resulting object does not use the zone.
     *
     * @param chronology  the chronology, not null
     * @return the current month-day, not null
     * @since 2.0
     */
    public static MonthDay now(Chronology chronology) {
        if (chronology == null) {
            throw new NullPointerException("Chronology must not be null");
        }
        return new MonthDay(chronology);
    }

    //-----------------------------------------------------------------------
    /**
     * Parses a {@code MonthDay} from the specified string.
     * <p>
     * This uses {@link ISODateTimeFormat#localDateParser()} or the format {@code --MM-dd}.
     * 
     * @param str  the string to parse, not null
     * @return the parsed month-day, not null
     * @since 2.0
     */
    @FromString
    public static MonthDay parse(String str) {
        return parse(str, PARSER);
    }

    /**
     * Parses a {@code MonthDay} from the specified string using a formatter.
     * 
     * @param str  the string to parse, not null
     * @param formatter  the formatter to use, not null
     * @return the parsed month-day, not null
     * @since 2.0
     */
    public static MonthDay parse(String str, DateTimeFormatter formatter) {
        LocalDate date = formatter.parseLocalDate(str);
        return new MonthDay(date.getMonthOfYear(), date.getDayOfMonth());
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs a MonthDay from a <code>java.util.Calendar</code>
     * using exactly the same field values avoiding any time zone effects.
     * <p>
     * Each field is queried from the Calendar and assigned to the MonthDay.
     * <p>
     * This factory method ignores the type of the calendar and always
     * creates a MonthDay with ISO chronology. It is expected that you
     * will only pass in instances of <code>GregorianCalendar</code> however
     * this is not validated.
     *
     * @param calendar  the Calendar to extract fields from
     * @return the created MonthDay, never null
     * @throws IllegalArgumentException if the calendar is null
     * @throws IllegalArgumentException if the monthOfYear or dayOfMonth is invalid for the ISO chronology
     */
    public static MonthDay fromCalendarFields(Calendar calendar) {
        if (calendar == null) {
            throw new IllegalArgumentException("The calendar must not be null");
        }
        return new MonthDay(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Constructs a MonthDay from a <code>java.util.Date</code>
     * using exactly the same field values avoiding any time zone effects.
     * <p>
     * Each field is queried from the Date and assigned to the MonthDay.
     * <p>
     * This factory method always creates a MonthDay with ISO chronology.
     *
     * @param date  the Date to extract fields from
     * @return the created MonthDay, never null
     * @throws IllegalArgumentException if the calendar is null
     * @throws IllegalArgumentException if the monthOfYear or dayOfMonth is invalid for the ISO chronology
     */
    @SuppressWarnings("deprecation")
    public static MonthDay fromDateFields(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return new MonthDay(date.getMonth() + 1, date.getDate());
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs a MonthDay with the current monthOfYear, using ISOChronology in
     * the default zone to extract the fields.
     * <p>
     * The constructor uses the default time zone, resulting in the local time
     * being initialised. Once the constructor is complete, all further calculations
     * are performed without reference to a time-zone (by switching to UTC).
     * 
     * @see #now()
     */
    public MonthDay() {
        super();
    }

    /**
     * Constructs a MonthDay with the current month-day, using ISOChronology in
     * the specified zone to extract the fields.
     * <p>
     * The constructor uses the specified time zone to obtain the current month-day.
     * Once the constructor is complete, all further calculations
     * are performed without reference to a time-zone (by switching to UTC).
     * 
     * @param zone  the zone to use, null means default zone
     * @see #now(DateTimeZone)
     */
    public MonthDay(DateTimeZone zone) {
        super(ISOChronology.getInstance(zone));
    }

    /**
     * Constructs a MonthDay with the current month-day, using the specified chronology
     * and zone to extract the fields.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a time-zone (by switching to UTC).
     *
     * @param chronology  the chronology, null means ISOChronology in the default zone
     * @see #now(Chronology)
     */
    public MonthDay(Chronology chronology) {
        super(chronology);
    }

    /**
     * Constructs a MonthDay extracting the partial fields from the specified
     * milliseconds using the ISOChronology in the default zone.
     * <p>
     * The constructor uses the default time zone, resulting in the local time
     * being initialised. Once the constructor is complete, all further calculations
     * are performed without reference to a time-zone (by switching to UTC).
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     */
    public MonthDay(long instant) {
        super(instant);
    }

    /**
     * Constructs a MonthDay extracting the partial fields from the specified
     * milliseconds using the chronology provided.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a time-zone (by switching to UTC).
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public MonthDay(long instant, Chronology chronology) {
        super(instant, chronology);
    }

    /**
     * Constructs a MonthDay from an Object that represents some form of time.
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
    public MonthDay(Object instant) {
        super(instant, null, ISODateTimeFormat.localDateParser());
    }

    /**
     * Constructs a MonthDay from an Object that represents some form of time,
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
    public MonthDay(Object instant, Chronology chronology) {
        super(instant, DateTimeUtils.getChronology(chronology), ISODateTimeFormat.localDateParser());
    }

    /**
     * Constructs a MonthDay with specified year and month
     * using <code>ISOChronology</code>.
     * <p>
     * The constructor uses the no time zone initialising the fields as provided.
     * Once the constructor is complete, all further calculations
     * are performed without reference to a time-zone (by switching to UTC).
     *
     * @param monthOfYear  the month of the year
     * @param dayOfMonth  the day of the month
     */
    public MonthDay(int monthOfYear, int dayOfMonth) {
        this(monthOfYear, dayOfMonth, null);
    }

    /**
     * Constructs an instance set to the specified monthOfYear and dayOfMonth
     * using the specified chronology, whose zone is ignored.
     * <p>
     * If the chronology is null, <code>ISOChronology</code> is used.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a time-zone (by switching to UTC).
     *
     * @param monthOfYear  the month of the year
     * @param dayOfMonth  the day of the month
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public MonthDay(int monthOfYear, int dayOfMonth, Chronology chronology) {
        super(new int[] {monthOfYear, dayOfMonth}, chronology);
    }

    /**
     * Constructs a MonthDay with chronology from this instance and new values.
     *
     * @param partial  the partial to base this new instance on
     * @param values  the new set of values
     */
    MonthDay(MonthDay partial, int[] values) {
        super(partial, values);
    }

    /**
     * Constructs a MonthDay with values from this instance and a new chronology.
     *
     * @param partial  the partial to base this new instance on
     * @param chrono  the new chronology
     */
    MonthDay(MonthDay partial, Chronology chrono) {
        super(partial, chrono);
    }

    /**
     * Handle broken serialization from other tools.
     * @return the resolved object, not null
     */
    private Object readResolve() {
        if (DateTimeZone.UTC.equals(getChronology().getZone()) == false) {
            return new MonthDay(this, getChronology().withUTC());
        }
        return this;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the number of fields in this partial, which is two.
     * The supported fields are MonthOfYear and DayOfMonth.
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
    @Override
    protected DateTimeField getField(int index, Chronology chrono) {
        switch (index) {
        case MONTH_OF_YEAR:
            return chrono.monthOfYear();
        case DAY_OF_MONTH:
            return chrono.dayOfMonth();
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
    @Override
    public DateTimeFieldType getFieldType(int index) {
        return FIELD_TYPES[index];
    }

    /**
     * Gets an array of the field type of each of the fields that this partial supports.
     * <p>
     * The fields are returned largest to smallest, Month, Day.
     *
     * @return the array of field types (cloned), largest to smallest, never null
     */
    @Override
    public DateTimeFieldType[] getFieldTypes() {
        return (DateTimeFieldType[]) FIELD_TYPES.clone();
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this month-day with the specified chronology.
     * This instance is immutable and unaffected by this method call.
     * <p>
     * This method retains the values of the fields, thus the result will
     * typically refer to a different instant.
     * <p>
     * The time zone of the specified chronology is ignored, as MonthDay
     * operates without a time zone.
     *
     * @param newChronology  the new chronology, null means ISO
     * @return a copy of this month-day with a different chronology, never null
     * @throws IllegalArgumentException if the values are invalid for the new chronology
     */
    public MonthDay withChronologyRetainFields(Chronology newChronology) {
        newChronology = DateTimeUtils.getChronology(newChronology);
        newChronology = newChronology.withUTC();
        if (newChronology == getChronology()) {
            return this;
        } else {
            MonthDay newMonthDay = new MonthDay(this, newChronology);
            newChronology.validate(newMonthDay, getValues());
            return newMonthDay;
        }
    }

    /**
     * Returns a copy of this month-day with the specified field set to a new value.
     * <p>
     * For example, if the field type is <code>dayOfMonth</code> then the day
     * would be changed in the returned instance.
     * <p>
     * These three lines are equivalent:
     * <pre>
     * MonthDay updated = md.withField(DateTimeFieldType.dayOfMonth(), 6);
     * MonthDay updated = md.dayOfMonth().setCopy(6);
     * MonthDay updated = md.property(DateTimeFieldType.dayOfMonth()).setCopy(6);
     * </pre>
     *
     * @param fieldType  the field type to set, not null
     * @param value  the value to set
     * @return a copy of this instance with the field set, never null
     * @throws IllegalArgumentException if the value is null or invalid
     */
    public MonthDay withField(DateTimeFieldType fieldType, int value) {
        int index = indexOfSupported(fieldType);
        if (value == getValue(index)) {
            return this;
        }
        int[] newValues = getValues();
        newValues = getField(index).set(this, index, newValues, value);
        return new MonthDay(this, newValues);
    }

    /**
     * Returns a copy of this month-day with the value of the specified field increased.
     * <p>
     * If the addition is zero, then <code>this</code> is returned.
     * <p>
     * These three lines are equivalent:
     * <pre>
     * MonthDay added = md.withFieldAdded(DurationFieldType.days(), 6);
     * MonthDay added = md.plusDays(6);
     * MonthDay added = md.dayOfMonth().addToCopy(6);
     * </pre>
     * 
     * @param fieldType  the field type to add to, not null
     * @param amount  the amount to add
     * @return a copy of this instance with the field updated, never null
     * @throws IllegalArgumentException if the value is null or invalid
     * @throws ArithmeticException if the new date-time exceeds the capacity
     */
    public MonthDay withFieldAdded(DurationFieldType fieldType, int amount) {
        int index = indexOfSupported(fieldType);
        if (amount == 0) {
            return this;
        }
        int[] newValues = getValues();
        newValues = getField(index).add(this, index, newValues, amount);
        return new MonthDay(this, newValues);
    }

    /**
     * Returns a copy of this month-day with the specified period added.
     * <p>
     * If the addition is zero, then <code>this</code> is returned.
     * Fields in the period that aren't present in the partial are ignored.
     * <p>
     * This method is typically used to add multiple copies of complex
     * period instances. Adding one field is best achieved using methods
     * like {@link #withFieldAdded(DurationFieldType, int)}
     * or {@link #plusMonths(int)}.
     * 
     * @param period  the period to add to this one, null means zero
     * @param scalar  the amount of times to add, such as -1 to subtract once
     * @return a copy of this instance with the period added, never null
     * @throws ArithmeticException if the new date-time exceeds the capacity
     */
    public MonthDay withPeriodAdded(ReadablePeriod period, int scalar) {
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
        return new MonthDay(this, newValues);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this month-day with the specified period added.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * <p>
     * This method is typically used to add complex period instances.
     * Adding one field is best achieved using methods
     * like {@link #plusMonths(int)}.
     * 
     * @param period  the duration to add to this one, null means zero
     * @return a copy of this instance with the period added, never null
     * @throws ArithmeticException if the new month-day exceeds the capacity
     */
    public MonthDay plus(ReadablePeriod period) {
        return withPeriodAdded(period, 1);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this month-day plus the specified number of months.
     * <p>
     * This month-day instance is immutable and unaffected by this method call.
     * The month will wrap at the end of the year from December to January.
     * The day will be adjusted to the last valid value if necessary.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * MonthDay added = md.plusMonths(6);
     * MonthDay added = md.plus(Period.months(6));
     * MonthDay added = md.withFieldAdded(DurationFieldType.months(), 6);
     * </pre>
     *
     * @param months  the amount of months to add, may be negative
     * @return the new month-day plus the increased months, never null
     */
    public MonthDay plusMonths(int months) {
        return withFieldAdded(DurationFieldType.months(), months);
    }

    /**
     * Returns a copy of this month-day plus the specified number of days.
     * <p>
     * This month-day instance is immutable and unaffected by this method call.
     * The month will wrap at the end of the year from December to January.
     * <p>
     * If the number of days added requires wrapping past the end of February,
     * the wrapping will be calculated assuming February has 29 days. 
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * MonthDay added = md.plusDays(6);
     * MonthDay added = md.plus(Period.days(6));
     * MonthDay added = md.withFieldAdded(DurationFieldType.days(), 6);
     * </pre>
     *
     * @param days  the amount of days to add, may be negative
     * @return the new month-day plus the increased days, never null
     */
    public MonthDay plusDays(int days) {
        return withFieldAdded(DurationFieldType.days(), days);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this month-day with the specified period taken away.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * <p>
     * This method is typically used to subtract complex period instances.
     * Subtracting one field is best achieved using methods
     * like {@link #minusMonths(int)}.
     * 
     * @param period  the period to reduce this instant by
     * @return a copy of this instance with the period taken away, never null
     * @throws ArithmeticException if the new month-day exceeds the capacity
     */
    public MonthDay minus(ReadablePeriod period) {
        return withPeriodAdded(period, -1);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this month-day minus the specified number of months.
     * <p>
     * This MonthDay instance is immutable and unaffected by this method call.
     * The month will wrap at the end of the year from January to December.
     * The day will be adjusted to the last valid value if necessary.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * MonthDay subtracted = md.minusMonths(6);
     * MonthDay subtracted = md.minus(Period.months(6));
     * MonthDay subtracted = md.withFieldAdded(DurationFieldType.months(), -6);
     * </pre>
     *
     * @param months  the amount of months to subtract, may be negative
     * @return the new month-day minus the increased months, never null
     */
    public MonthDay minusMonths(int months) {
        return withFieldAdded(DurationFieldType.months(), FieldUtils.safeNegate(months));
    }

    /**
     * Returns a copy of this month-day minus the specified number of months.
     * <p>
     * This month-day instance is immutable and unaffected by this method call.
     * The month will wrap at the end of the year from January to December.
     * <p>
     * The following three lines are identical in effect:
     * <pre>
     * MonthDay subtracted = md.minusDays(6);
     * MonthDay subtracted = md.minus(Period.days(6));
     * MonthDay subtracted = md.withFieldAdded(DurationFieldType.days(), -6);
     * </pre>
     *
     * @param days  the amount of days to subtract, may be negative
     * @return the new month-day minus the increased days, never null
     */
    public MonthDay minusDays(int days) {
        return withFieldAdded(DurationFieldType.days(), FieldUtils.safeNegate(days));
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this object to a LocalDate with the same month-day and chronology.
     *
     * @param year  the year to use, valid for chronology
     * @return a LocalDate with the same month-day and chronology, never null
     */
    public LocalDate toLocalDate(int year) {
        return new LocalDate(year, getMonthOfYear(), getDayOfMonth(), getChronology());
    }

    //-----------------------------------------------------------------------
    /**
     * Get the month of year field value.
     *
     * @return the month of year
     */
    public int getMonthOfYear() {
        return getValue(MONTH_OF_YEAR);
    }

    /**
     * Get the day of month field value.
     *
     * @return the day of month
     */
    public int getDayOfMonth() {
        return getValue(DAY_OF_MONTH);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this month-day with the month of year field updated.
     * <p>
     * MonthDay is immutable, so there are no set methods.
     * Instead, this method returns a new instance with the value of
     * month of year changed.
     *
     * @param monthOfYear  the month of year to set
     * @return a copy of this object with the field set, never null
     * @throws IllegalArgumentException if the value is invalid
     */
    public MonthDay withMonthOfYear(int monthOfYear) {
        int[] newValues = getValues();
        newValues = getChronology().monthOfYear().set(this, MONTH_OF_YEAR, newValues, monthOfYear);
        return new MonthDay(this, newValues);
    }

    /**
     * Returns a copy of this month-day with the day of month field updated.
     * <p>
     * MonthDay is immutable, so there are no set methods.
     * Instead, this method returns a new instance with the value of
     * day of month changed.
     *
     * @param dayOfMonth  the day of month to set
     * @return a copy of this object with the field set, never null
     * @throws IllegalArgumentException if the value is invalid
     */
    public MonthDay withDayOfMonth(int dayOfMonth) {
        int[] newValues = getValues();
        newValues = getChronology().dayOfMonth().set(this, DAY_OF_MONTH, newValues, dayOfMonth);
        return new MonthDay(this, newValues);
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
     * Get the month of year field property which provides access to advanced functionality.
     * 
     * @return the month of year property
     */
    public Property monthOfYear() {
        return new Property(this, MONTH_OF_YEAR);
    }

    /**
     * Get the day of month field property which provides access to advanced functionality.
     * 
     * @return the day of month property
     */
    public Property dayOfMonth() {
        return new Property(this, DAY_OF_MONTH);
    }

    //-----------------------------------------------------------------------
    /**
     * Output the month-day in ISO8601 format (--MM-dd).
     *
     * @return ISO8601 time formatted string.
     */
    @Override
    @ToString
    public String toString() {
        List<DateTimeFieldType> fields = new ArrayList<DateTimeFieldType>();
        fields.add(DateTimeFieldType.monthOfYear());
        fields.add(DateTimeFieldType.dayOfMonth());
        return ISODateTimeFormat.forFields(fields, true, true).print(this);
    }

    /**
     * Output the month-day using the specified format pattern.
     *
     * @param pattern  the pattern specification, null means use <code>toString</code>
     * @see org.joda.time.format.DateTimeFormat
     */
    @Override
    public String toString(String pattern) {
        if (pattern == null) {
            return toString();
        }
        return DateTimeFormat.forPattern(pattern).print(this);
    }

    /**
     * Output the month-day using the specified format pattern.
     *
     * @param pattern  the pattern specification, null means use <code>toString</code>
     * @param locale  Locale to use, null means default
     * @see org.joda.time.format.DateTimeFormat
     */
    @Override
    public String toString(String pattern, Locale locale) throws IllegalArgumentException {
        if (pattern == null) {
            return toString();
        }
        return DateTimeFormat.forPattern(pattern).withLocale(locale).print(this);
    }

    //-----------------------------------------------------------------------
    /**
     * The property class for <code>MonthDay</code>.
     * <p>
     * This class binds a <code>YearMonth</code> to a <code>DateTimeField</code>.
     * 
     * @author Chris Pheby
     * @since 2.0
     */
    public static class Property extends AbstractPartialFieldProperty implements Serializable {

        /** Serialization version */
        private static final long serialVersionUID = 5727734012190224363L;

        /** The partial */
        private final MonthDay iBase;
        /** The field index */
        private final int iFieldIndex;

        /**
         * Constructs a property.
         * 
         * @param partial  the partial instance
         * @param fieldIndex  the index in the partial
         */
        Property(MonthDay partial, int fieldIndex) {
            super();
            iBase = partial;
            iFieldIndex = fieldIndex;
        }

        /**
         * Gets the field that this property uses.
         * 
         * @return the field
         */
        @Override
        public DateTimeField getField() {
            return iBase.getField(iFieldIndex);
        }

        /**
         * Gets the partial that this property belongs to.
         * 
         * @return the partial
         */
        @Override
        protected ReadablePartial getReadablePartial() {
            return iBase;
        }

        /**
         * Gets the partial that this property belongs to.
         * 
         * @return the partial
         */
        public MonthDay getMonthDay() {
            return iBase;
        }

        /**
         * Gets the value of this field.
         * 
         * @return the field value
         */
        @Override
        public int get() {
            return iBase.getValue(iFieldIndex);
        }

        //-----------------------------------------------------------------------
        /**
         * Adds to the value of this field in a copy of this MonthDay.
         * <p>
         * The value will be added to this field. If the value is too large to be
         * added solely to this field then it will affect larger fields.
         * Smaller fields are unaffected.
         * <p>
         * The MonthDay attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param valueToAdd  the value to add to the field in the copy
         * @return a copy of the MonthDay with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public MonthDay addToCopy(int valueToAdd) {
            int[] newValues = iBase.getValues();
            newValues = getField().add(iBase, iFieldIndex, newValues, valueToAdd);
            return new MonthDay(iBase, newValues);
        }

        /**
         * Adds to the value of this field in a copy of this MonthDay wrapping
         * within this field if the maximum value is reached.
         * <p>
         * The value will be added to this field. If the value is too large to be
         * added solely to this field then it wraps within this field.
         * Other fields are unaffected.
         * <p>
         * For example,
         * <code>--12-30</code> addWrapField one month returns <code>--01-30</code>.
         * <p>
         * The MonthDay attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param valueToAdd  the value to add to the field in the copy
         * @return a copy of the MonthDay with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public MonthDay addWrapFieldToCopy(int valueToAdd) {
            int[] newValues = iBase.getValues();
            newValues = getField().addWrapField(iBase, iFieldIndex, newValues, valueToAdd);
            return new MonthDay(iBase, newValues);
        }

        //-----------------------------------------------------------------------
        /**
         * Sets this field in a copy of the MonthDay.
         * <p>
         * The MonthDay attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param value  the value to set the field in the copy to
         * @return a copy of the MonthDay with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public MonthDay setCopy(int value) {
            int[] newValues = iBase.getValues();
            newValues = getField().set(iBase, iFieldIndex, newValues, value);
            return new MonthDay(iBase, newValues);
        }

        /**
         * Sets this field in a copy of the MonthDay to a parsed text value.
         * <p>
         * The MonthDay attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param text  the text value to set
         * @param locale  optional locale to use for selecting a text symbol
         * @return a copy of the MonthDay with the field value changed
         * @throws IllegalArgumentException if the text value isn't valid
         */
        public MonthDay setCopy(String text, Locale locale) {
            int[] newValues = iBase.getValues();
            newValues = getField().set(iBase, iFieldIndex, newValues, text, locale);
            return new MonthDay(iBase, newValues);
        }

        /**
         * Sets this field in a copy of the MonthDay to a parsed text value.
         * <p>
         * The MonthDay attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param text  the text value to set
         * @return a copy of the MonthDay with the field value changed
         * @throws IllegalArgumentException if the text value isn't valid
         */
        public MonthDay setCopy(String text) {
            return setCopy(text, null);
        }
    }

}
