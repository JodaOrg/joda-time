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
import java.util.Locale;

import org.joda.time.base.BasePartial;
import org.joda.time.field.AbstractPartialFieldProperty;
import org.joda.time.format.ISODateTimeFormat;

/**
 * YearMonthDay is an immutable partial supporting the year, monthOfYear
 * and dayOfMonth fields.
 * <p>
 * Calculations on YearMonthDay are performed using a {@link Chronology}.
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
 * YearMonthDay is thread-safe and immutable, provided that the Chronology is as well.
 * All standard Chronology classes supplied are thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public final class YearMonthDay
        extends BasePartial
        implements ReadablePartial, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 797544782896179L;
    /** The singleton set of field types */
    private static final DateTimeFieldType[] FIELD_TYPES = new DateTimeFieldType[] {
        DateTimeFieldType.year(),
        DateTimeFieldType.monthOfYear(),
        DateTimeFieldType.dayOfMonth(),
    };

    /** The index of the year field in the field array */
    public static final int YEAR = 0;
    /** The index of the monthOfYear field in the field array */
    public static final int MONTH_OF_YEAR = 1;
    /** The index of the dayOfMonth field in the field array */
    public static final int DAY_OF_MONTH = 2;

    // Constructors
    //-----------------------------------------------------------------------
    /**
     * Constructs a YearMonthDay with the current time, using ISOChronology in
     * the default zone to extract the fields.
     * <p>
     * The constructor uses the default time zone, resulting in the local time
     * being initialised. Once the constructor is complete, all further calculations
     * are performed without reference to a timezone (by switching to UTC).
     */
    public YearMonthDay() {
        super();
    }

    /**
     * Constructs a YearMonthDay with the current time, using the specified chronology
     * and zone to extract the fields.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     *
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public YearMonthDay(Chronology chronology) {
        super(chronology);
    }

    /**
     * Constructs a YearMonthDay extracting the partial fields from the specified
     * milliseconds using the ISOChronology in the default zone.
     * <p>
     * The constructor uses the default time zone, resulting in the local time
     * being initialised. Once the constructor is complete, all further calculations
     * are performed without reference to a timezone (by switching to UTC).
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     */
    public YearMonthDay(long instant) {
        super(instant);
    }

    /**
     * Constructs a YearMonthDay extracting the partial fields from the specified
     * milliseconds using the chronology provided.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public YearMonthDay(long instant, Chronology chronology) {
        super(instant, chronology);
    }

    /**
     * Constructs a YearMonthDay from an Object that represents a time.
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
    public YearMonthDay(Object instant) {
        super(instant, null);
    }

    /**
     * Constructs a YearMonthDay from an Object that represents a time, using the
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
    public YearMonthDay(Object instant, Chronology chronology) {
        super(instant, DateTimeUtils.getChronology(chronology));
    }

    /**
     * Constructs a YearMonthDay with specified time field values
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
    public YearMonthDay(int year, int monthOfYear, int dayOfMonth) {
        this(year, monthOfYear, dayOfMonth, null);
    }

    /**
     * Constructs a YearMonthDay with specified time field values.
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
    public YearMonthDay(int year, int monthOfYear, int dayOfMonth, Chronology chronology) {
        super(new int[] {year, monthOfYear, dayOfMonth}, chronology);
    }

    /**
     * Constructs a YearMonthDay with chronology from this instance and new values.
     *
     * @param partial  the partial to base this new instance on
     * @param values  the new set of values
     */
    YearMonthDay(YearMonthDay partial, int[] values) {
        super(partial, values);
    }

    /**
     * Constructs a YearMonthDay with values from this instance and a new chronology.
     *
     * @param partial  the partial to base this new instance on
     * @param chrono  the new chronology
     */
    YearMonthDay(YearMonthDay partial, Chronology chrono) {
        super(partial, chrono);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the number of fields in this partial.
     * 
     * @return the field count
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
     * Gets the field type at the specified index.
     *
     * @param index  the index to retrieve
     * @return the field at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public DateTimeFieldType getFieldType(int index) {
        return FIELD_TYPES[index];
    }

    /**
     * Gets an array of the field type of each of the fields that this partial supports.
     * <p>
     * The fields are returned largest to smallest, Year, Month, Day
     *
     * @return the array of field types (cloned), largest to smallest
     */
    public DateTimeFieldType[] getFieldTypes() {
        return (DateTimeFieldType[]) FIELD_TYPES.clone();
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a new YearMonthDay instance with the specified chronology.
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
    public YearMonthDay withChronologyRetainFields(Chronology newChronology) {
        newChronology = DateTimeUtils.getChronology(newChronology);
        newChronology = newChronology.withUTC();
        if (newChronology == getChronology()) {
            return this;
        } else {
            return new YearMonthDay(this, newChronology);
        }
    }

    /**
     * Gets the property object for the specified type, which contains many useful methods.
     *
     * @param type  the field type to get the chronology for
     * @return the property object
     * @throws IllegalArgumentException if the field is null or unsupported
     */
    public Property property(DateTimeFieldType type) {
        return new Property(this, indexOfSupported(type));
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
     * Converts this object to a DateTime using a TimeOfDay to fill in the
     * missing fields and using the default time zone.
     * This instance is immutable and unaffected by this method call.
     * <p>
     * The resulting chronology is determined by the chronology of this
     * YearMonthDay plus the time zone.
     * The chronology of the time is ignored - only the field values are used.
     *
     * @param time  the time of day to use, null means current time
     * @return the DateTime instance
     */
    public DateTime toDateTime(TimeOfDay time) {
        return toDateTime(time, null);
    }

    /**
     * Converts this object to a DateTime using a TimeOfDay to fill in the
     * missing fields.
     * This instance is immutable and unaffected by this method call.
     * <p>
     * The resulting chronology is determined by the chronology of this
     * YearMonthDay plus the time zone.
     * The chronology of the time is ignored - only the field values are used.
     *
     * @param time  the time of day to use, null means current time
     * @param zone  the zone to get the DateTime in, null means default
     * @return the DateTime instance
     */
    public DateTime toDateTime(TimeOfDay time, DateTimeZone zone) {
        Chronology chrono = getChronology().withZone(zone);
        long instant = DateTimeUtils.currentTimeMillis();
        instant = chrono.set(this, instant);
        if (time != null) {
            instant = chrono.set(time, instant);
        }
        return new DateTime(instant, chrono);
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this object to an Interval representing the whole day
     * in the default time zone.
     *
     * @return the DateMidnight instance in the default zone
     */
    public Interval toInterval() {
        return toInterval(null);
    }

    /**
     * Converts this object to an Interval representing the whole day.
     *
     * @param zone  the zone to get the Interval in, null means default
     * @return the DateMidnight instance
     */
    public Interval toInterval(DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        return toDateMidnight(zone).toInterval();
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
     * Get the year field property
     * 
     * @return the year property
     */
    public Property year() {
        return new Property(this, YEAR);
    }

    /**
     * Get the month of year field property
     * 
     * @return the month of year property
     */
    public Property monthOfYear() {
        return new Property(this, MONTH_OF_YEAR);
    }

    /**
     * Get the day of month field property
     * 
     * @return the day of month property
     */
    public Property dayOfMonth() {
        return new Property(this, DAY_OF_MONTH);
    }

    //-----------------------------------------------------------------------
    /**
     * Output the time in the ISO8601 format YYYY-MM-DD.
     * 
     * @return ISO8601 formatted string
     */
    public String toString() {
        return ISODateTimeFormat.getInstanceUTC().yearMonthDay().print(this);
    }

    //-----------------------------------------------------------------------
    /**
     * The property class for <code>YearMonthDay</code>.
     * <p>
     * This class binds a <code>YearMonthDay</code> to a <code>DateTimeField</code>.
     * 
     * @author Stephen Colebourne
     * @since 1.0
     */
    public static class Property extends AbstractPartialFieldProperty implements Serializable {

        /** Serialization version */
        private static final long serialVersionUID = 5727734012190224363L;

        /** The partial */
        private final YearMonthDay iYearMonthDay;
        /** The field index */
        private final int iFieldIndex;

        /**
         * Constructs a property.
         * 
         * @param partial  the partial instance
         * @param fieldIndex  the index in the partial
         */
        Property(YearMonthDay partial, int fieldIndex) {
            super();
            iYearMonthDay = partial;
            iFieldIndex = fieldIndex;
        }

        /**
         * Gets the field that this property uses.
         * 
         * @return the field
         */
        public DateTimeField getField() {
            return iYearMonthDay.getField(iFieldIndex);
        }

        /**
         * Gets the partial that this property belongs to.
         * 
         * @return the partial
         */
        public ReadablePartial getReadablePartial() {
            return iYearMonthDay;
        }

        /**
         * Gets the partial that this property belongs to.
         * 
         * @return the partial
         */
        public YearMonthDay getYearMonthDay() {
            return iYearMonthDay;
        }

        /**
         * Gets the value of this field.
         * 
         * @return the field value
         */
        public int get() {
            return iYearMonthDay.getValue(iFieldIndex);
        }

        //-----------------------------------------------------------------------
        /**
         * Adds to the value of this field in a copy of this YearMonthDay.
         * <p>
         * The value will be added to this field. If the value is too large to be
         * added solely to this field then it will affect larger fields.
         * Smaller fields are unaffected.
         * <p>
         * If the result would be too large, beyond the maximum year, then an
         * IllegalArgumentException is thrown.
         * <p>
         * The YearMonthDay attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param valueToAdd  the value to add to the field in the copy
         * @return a copy of the YearMonthDay with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public YearMonthDay addToCopy(int valueToAdd) {
            int[] newValues = iYearMonthDay.getValues();
            newValues = getField().add(iYearMonthDay, iFieldIndex, newValues, valueToAdd);
            return new YearMonthDay(iYearMonthDay, newValues);
        }

        /**
         * Adds to the value of this field in a copy of this YearMonthDay wrapping
         * within this field if the maximum value is reached.
         * <p>
         * The value will be added to this field. If the value is too large to be
         * added solely to this field then it wraps within this field.
         * Other fields are unaffected.
         * <p>
         * For example,
         * <code>2004-12-20</code> addWrapField one month returns <code>2004-01-20</code>.
         * <p>
         * The YearMonthDay attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param valueToAdd  the value to add to the field in the copy
         * @return a copy of the YearMonthDay with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public YearMonthDay addWrapFieldToCopy(int valueToAdd) {
            int[] newValues = iYearMonthDay.getValues();
            newValues = getField().addWrapField(iYearMonthDay, iFieldIndex, newValues, valueToAdd);
            return new YearMonthDay(iYearMonthDay, newValues);
        }

        //-----------------------------------------------------------------------
        /**
         * Sets this field in a copy of the YearMonthDay.
         * <p>
         * The YearMonthDay attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param value  the value to set the field in the copy to
         * @return a copy of the YearMonthDay with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public YearMonthDay setCopy(int value) {
            int[] newValues = iYearMonthDay.getValues();
            newValues = getField().set(iYearMonthDay, iFieldIndex, newValues, value);
            return new YearMonthDay(iYearMonthDay, newValues);
        }

        /**
         * Sets this field in a copy of the YearMonthDay to a parsed text value.
         * <p>
         * The YearMonthDay attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param text  the text value to set
         * @param locale  optional locale to use for selecting a text symbol
         * @return a copy of the YearMonthDay with the field value changed
         * @throws IllegalArgumentException if the text value isn't valid
         */
        public YearMonthDay setCopy(String text, Locale locale) {
            int[] newValues = iYearMonthDay.getValues();
            newValues = getField().set(iYearMonthDay, iFieldIndex, newValues, text, locale);
            return new YearMonthDay(iYearMonthDay, newValues);
        }

        /**
         * Sets this field in a copy of the YearMonthDay to a parsed text value.
         * <p>
         * The YearMonthDay attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param text  the text value to set
         * @return a copy of the YearMonthDay with the field value changed
         * @throws IllegalArgumentException if the text value isn't valid
         */
        public YearMonthDay setCopy(String text) {
            return setCopy(text, null);
        }
    }

}
