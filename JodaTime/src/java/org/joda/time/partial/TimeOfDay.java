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
package org.joda.time.partial;

import java.io.Serializable;
import java.util.Arrays;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableInstant;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.InstantConverter;

/**
 * TimeOfDay is an immutable partial instant supporting the hour, minute, second
 * and millisecond fields.
 * <p>
 * Calculations on TimeOfDay are performed using a {@link Chronology}.
 * This chronology is set to be in the UTC time zone for all calculations.
 * <p>
 * Each individual field can be queried in two ways:
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
 * <li>maximum value
 * <li>minimum value
 * </ul>
 * <p>
 * TimeOfDay is thread-safe and immutable, provided that the Chronology is as well.
 * All standard Chronology classes supplied are thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class TimeOfDay implements PartialInstant, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 3633353405803318660L;

    /** The index of the hourOfDay field in the field array */
    public static final int HOUR_OF_DAY = 0;
    /** The index of the minuteOfHour field in the field array */
    public static final int MINUTE_OF_HOUR = 1;
    /** The index of the secondOfMinute field in the field array */
    public static final int SECOND_OF_MINUTE = 2;
    /** The index of the millisOfSecond field in the field array */
    public static final int MILLIS_OF_SECOND = 3;

    /** The chronology in use */
    private final Chronology iChronology;
    /** The values of each field in this partial instant */
    private final int[] iValues;

    // Constructors
    //-----------------------------------------------------------------------
    /**
     * Constructs a TimeOfDay with the current time, using ISOChronology in
     * the default zone to extract the fields.
     * <p>
     * The constructor uses the default time zone, resulting in the local time
     * being initialised. Once the constructor is complete, all further calculations
     * are performed without reference to a timezone (by switching to UTC).
     */
    public TimeOfDay() {
        this(DateTimeUtils.currentTimeMillis(), null);
    }

    /**
     * Constructs a TimeOfDay with the current time, using the specified chronology
     * and zone to extract the fields.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     *
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public TimeOfDay(Chronology chronology) {
        this(DateTimeUtils.currentTimeMillis(), chronology);
    }

    /**
     * Constructs a TimeOfDay extracting the partial fields from the specified
     * milliseconds using the ISOChronology in the default zone.
     * <p>
     * The constructor uses the default time zone, resulting in the local time
     * being initialised. Once the constructor is complete, all further calculations
     * are performed without reference to a timezone (by switching to UTC).
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     */
    public TimeOfDay(long instant) {
        this(instant, null);
    }

    /**
     * Constructs a TimeOfDay extracting the partial fields from the specified
     * milliseconds using the chronology provided.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public TimeOfDay(long instant, Chronology chronology) {
        super();
        if (chronology == null) {
            chronology = ISOChronology.getInstance();
        }
        iValues = initValues(instant, chronology);
        iChronology = chronology.withUTC();
    }

    /**
     * Constructs a TimeOfDay from an Object that represents a time.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  the datetime object, must not be null
     * @throws IllegalArgumentException if the date is null
     */
    public TimeOfDay(Object instant) {
        super();
        InstantConverter converter = ConverterManager.getInstance().getInstantConverter(instant);
        Chronology chronology = converter.getChronology(instant);
        if (chronology == null) {
            chronology = ISOChronology.getInstance();
        }
        iValues = initValues(converter.getInstantMillis(instant), chronology);
        iChronology = chronology.withUTC();
    }

    /**
     * Constructs a TimeOfDay from an Object that represents a time, using the
     * specified chronology.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     *
     * @param instant  the datetime object, must not be null
     * @param chronology  the chronology, null means ISOChronology
     * @throws IllegalArgumentException if the date is null
     */
    public TimeOfDay(Object instant, Chronology chronology) {
        super();
        InstantConverter converter = ConverterManager.getInstance().getInstantConverter(instant);
        if (chronology == null) {
            chronology = ISOChronology.getInstance();
        }
        iValues = initValues(converter.getInstantMillis(instant, chronology), chronology);
        iChronology = chronology.withUTC();
    }

    /**
     * Constructs a TimeOfDay with specified hour and minute and zero seconds and milliseconds
     * using <code>ISOChronology</code> in the default zone.
     * <p>
     * The constructor uses the no time zone initialising the fields as provided.
     * Once the constructor is complete, all further calculations
     * are performed without reference to a timezone (by switching to UTC).
     *
     * @param hourOfDay  the hour of the day
     * @param minuteOfHour  the minute of the hour
     */
    public TimeOfDay(int hourOfDay, int minuteOfHour) {
        this(hourOfDay, minuteOfHour, 0, 0, null);
    }

    /**
     * Constructs a TimeOfDay with specified hour and minute and zero seconds and milliseconds.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     *
     * @param hourOfDay  the hour of the day
     * @param minuteOfHour  the minute of the hour
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public TimeOfDay(int hourOfDay, int minuteOfHour, Chronology chronology) {
        this(hourOfDay, minuteOfHour, 0, 0, chronology);
    }

    /**
     * Constructs a TimeOfDay with specified time field values and zero milliseconds
     * using <code>ISOChronology</code> in the default zone.
     * <p>
     * The constructor uses the no time zone initialising the fields as provided.
     * Once the constructor is complete, all further calculations
     * are performed without reference to a timezone (by switching to UTC).
     *
     * @param hourOfDay  the hour of the day
     * @param minuteOfHour  the minute of the hour
     * @param secondOfMinute  the second of the minute
     */
    public TimeOfDay(int hourOfDay, int minuteOfHour, int secondOfMinute) {
        this(hourOfDay, minuteOfHour, secondOfMinute, 0, null);
    }

    /**
     * Constructs a TimeOfDay with specified time field values and zero milliseconds.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     *
     * @param hourOfDay  the hour of the day
     * @param minuteOfHour  the minute of the hour
     * @param secondOfMinute  the second of the minute
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public TimeOfDay(int hourOfDay, int minuteOfHour, int secondOfMinute, Chronology chronology) {
        this(hourOfDay, minuteOfHour, secondOfMinute, 0, chronology);
    }

    /**
     * Constructs a TimeOfDay with specified time field values using
     * <code>ISOChronology</code> in the default zone.
     * <p>
     * The constructor uses the no time zone initialising the fields as provided.
     * Once the constructor is complete, all further calculations
     * are performed without reference to a timezone (by switching to UTC).
     *
     * @param hourOfDay  the hour of the day
     * @param minuteOfHour  the minute of the hour
     * @param secondOfMinute  the second of the minute
     * @param millisOfSecond  the millisecond of the second
     */
    public TimeOfDay(int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond) {
        this(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond, null);
    }

    /**
     * Constructs a TimeOfDay with specified time field values and chronology.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     *
     * @param hourOfDay  the hour of the day
     * @param minuteOfHour  the minute of the hour
     * @param secondOfMinute  the second of the minute
     * @param millisOfSecond  the millisecond of the second
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public TimeOfDay(int hourOfDay, int minuteOfHour,
            int secondOfMinute, int millisOfSecond, Chronology chronology) {
        super();
        if (chronology == null) {
            chronology = ISOChronology.getInstance();
        }
        iValues = new int[] {hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond};
        iChronology = chronology.withUTC();
        chronology.validate(this);
    }

    /**
     * Constructs a TimeOfDay with specified fields, values and chronology.
     *
     * @param partial  the partial to base this new instance on
     * @param values  the new set of values
     */
    TimeOfDay(TimeOfDay partial, int[] values) {
        super();
        iChronology = partial.iChronology;
        iValues = values;
    }

    /**
     * Initialize the array of values.
     * 
     * @param instant  the instant to use
     * @param chrono  the chronology to use
     */
    private int[] initValues(long instant, Chronology chrono) {
        return new int[] {
            chrono.hourOfDay().get(instant),
            chrono.minuteOfHour().get(instant),
            chrono.secondOfMinute().get(instant),
            chrono.millisOfSecond().get(instant),
        };
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the number of fields in TimeOfDay.
     * 
     * @return the field count
     */
    public int getFieldSize() {
        return 4;
    }

    /**
     * Gets the field at the specifed index.
     * 
     * @param index  the index
     * @return the field
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public DateTimeField getField(int index) {
        switch (index) {
            case HOUR_OF_DAY:
                return iChronology.hourOfDay();
            case MINUTE_OF_HOUR:
                return iChronology.minuteOfHour();
            case SECOND_OF_MINUTE:
                return iChronology.secondOfMinute();
            case MILLIS_OF_SECOND:
                return iChronology.millisOfSecond();
            default:
                throw new IllegalArgumentException(Integer.toString(index));
        }
    }

    /**
     * Gets the value of the field at the specifed index.
     * 
     * @param index  the index
     * @return the value
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public int getValue(int index) {
        if (index < 0 || index > 4) {
            throw new IllegalArgumentException(Integer.toString(index));
        }
        return iValues[index];
    }

    //-----------------------------------------------------------------------
    /**
     * Gets an array of the fields that this partial instant supports.
     * <p>
     * The fields are returned largest to smallest, for example Hour, Minute, Second.
     *
     * @return the fields supported (cloned), largest to smallest
     */
    public DateTimeField[] getFields() {
        return new DateTimeField[] {
            iChronology.hourOfDay(),
            iChronology.minuteOfHour(),
            iChronology.secondOfMinute(),
            iChronology.millisOfSecond(),
        };
    }

    /**
     * Gets an array of the value of each of the fields that this partial instant supports.
     * <p>
     * The fields are returned largest to smallest, for example Hour, Minute, Second.
     * Each value corresponds to the same array index as <code>getFields()</code>
     *
     * @return the current values of each field (cloned), largest to smallest
     */
    public int[] getValues() {
        return (int[]) iValues.clone();
    }

    /**
     * Gets the chronology of the partial which is never null.
     * <p>
     * The {@link Chronology} is the calculation engine behind the partial and
     * provides conversion and validation of the fields in a particular calendar system.
     * 
     * @return the chronology
     */
    public Chronology getChronology() {
        return iChronology;
    }

    /**
     * Get the value of one of the fields of a datetime.
     * <p>
     * The field specified must be one of those that is supported by the partial instant.
     *
     * @param field  a DateTimeField instance that is supported by this partial
     * @return the value of that field
     * @throws IllegalArgumentException if the field is null or not supported
     */
    public int get(DateTimeField field) {
        if (iChronology.hourOfDay() == field) {
            return getValue(HOUR_OF_DAY);
        }
        if (iChronology.minuteOfHour() == field) {
            return getValue(MINUTE_OF_HOUR);
        }
        if (iChronology.secondOfMinute() == field) {
            return getValue(SECOND_OF_MINUTE);
        }
        if (iChronology.millisOfSecond() == field) {
            return getValue(MILLIS_OF_SECOND);
        }
        throw new IllegalArgumentException("Field '" + field + "' is not supported by TimeOfDay");
    }

    /**
     * Checks whether the field specified is supported by this partial instant.
     *
     * @param field  the field to check, may be null which returns false
     * @return true if the field is supported
     */
    public boolean isSupported(DateTimeField field) {
        return 
            iChronology.hourOfDay() == field ||
            iChronology.minuteOfHour() == field ||
            iChronology.secondOfMinute() == field ||
            iChronology.millisOfSecond() == field;
    }

    //-----------------------------------------------------------------------
    /**
     * Resolves this partial against another complete instant to create a new
     * full instant specifying values as milliseconds since 1970-01-01T00:00:00Z.
     * <p>
     * For example, if this partial represents a time, then the result of this method
     * will be the date from the specified base plus the time from this instant.
     *
     * @param baseMillis  source of missing fields
     * @param zone  the zone to use, null means default
     * @return the combined instant in milliseconds
     */
    public long resolve(long baseMillis, DateTimeZone zone) {
        Chronology chrono = iChronology.withZone(zone);
        long millis = baseMillis;
        millis = chrono.hourOfDay().set(millis, getValue(HOUR_OF_DAY));
        millis = chrono.minuteOfHour().set(millis, getValue(MINUTE_OF_HOUR));
        millis = chrono.secondOfMinute().set(millis, getValue(SECOND_OF_MINUTE));
        millis = chrono.millisOfSecond().set(millis, getValue(MILLIS_OF_SECOND));
        return millis;
    }

    /**
     * Resolves this partial against another complete instant to create a new
     * full instant. The combination is performed using the chronology of the
     * specified instant.
     * <p>
     * For example, if this partial represents a time, then the result of this method
     * will be the date from the specified base plus the time from this instant.
     *
     * @param base  the instant that provides the missing fields, null means now
     * @return the combined datetime
     */
    public DateTime resolveDateTime(ReadableInstant base) {
        if (base == null) {
            base = new DateTime();
        }
        Chronology chrono = base.getChronology();
        long millis = base.getMillis();
        millis = chrono.hourOfDay().set(millis, getValue(HOUR_OF_DAY));
        millis = chrono.minuteOfHour().set(millis, getValue(MINUTE_OF_HOUR));
        millis = chrono.secondOfMinute().set(millis, getValue(SECOND_OF_MINUTE));
        millis = chrono.millisOfSecond().set(millis, getValue(MILLIS_OF_SECOND));
        return new DateTime(millis, chrono);
    }

    //-----------------------------------------------------------------------
    /**
     * Get the hour of day (0-23) field value.
     *
     * @return the hour of day
     */
    public int getHourOfDay() {
        return getValue(HOUR_OF_DAY);
    }

    /**
     * Get the minute of hour field value.
     *
     * @return the minute of hour
     */
    public int getMinuteOfHour() {
        return getValue(MINUTE_OF_HOUR);
    }

    /**
     * Get the second of minute field value.
     *
     * @return the second of minute
     */
    public int getSecondOfMinute() {
        return getValue(SECOND_OF_MINUTE);
    }

    /**
     * Get the millis of second field value.
     *
     * @return the millis of second
     */
    public int getMillisOfSecond() {
        return getValue(MILLIS_OF_SECOND);
    }

    //-----------------------------------------------------------------------
    /**
     * Get the hour of day (0-23) field property
     * 
     * @return the hour of day property
     */
    public Property hourOfDay() {
        return new Property(this, HOUR_OF_DAY);
    }

    /**
     * Get the minute of hour field property
     * 
     * @return the minute of hour property
     */
    public Property minuteOfHour() {
        return new Property(this, MINUTE_OF_HOUR);
    }

    /**
     * Get the second of minute field property
     * 
     * @return the second of minute property
     */
    public Property secondOfMinute() {
        return new Property(this, SECOND_OF_MINUTE);
    }

    /**
     * Get the millis of second property
     * 
     * @return the millis of second property
     */
    public Property millisOfSecond() {
        return new Property(this, MILLIS_OF_SECOND);
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this TimeOfDay with another returning true if the fields and
     * values are equal.
     *
     * @param timeOfDay  an object to check against
     * @return true if fields and values are equal
     */
    public boolean equals(Object timeOfDay) {
        if (timeOfDay instanceof TimeOfDay == false) {
            return false;
        }
        TimeOfDay other = (TimeOfDay) timeOfDay;
        return Arrays.equals(iValues, other.iValues) &&
               iChronology == other.iChronology;
    }

    /**
     * Gets a hash code for the TimeOfDay that is compatible with the 
     * equals method.
     *
     * @return a suitable hash code
     */
    public int hashCode() {
        int total = 157;
        for (int i = 0; i < iValues.length; i++) {
            total = 23 * total + getValue(i);
        }
        total += iChronology.hashCode();
        return total;
    }

    /**
     * Output the time in ISO8601 time only format (hh:mm:ss.SSS).
     * 
     * @return ISO8601 time formatted string
     */
    public String toString() {
        // TODO
        return "";
    }
    
    //-----------------------------------------------------------------------
    /**
     * The property class for TimeOfDay.
     */
    public static class Property extends AbstractPartialFieldProperty {

        /** The instant */
        private final TimeOfDay iInstant;
        /** The field index */
        private final int iFieldIndex;

        /**
         * Constructs a property.
         * 
         * @param instant  the partial instant
         * @param field  the field
         * @param fieldIndex  the index in the instant
         */
        Property(TimeOfDay instant, int fieldIndex) {
            super();
            iInstant = instant;
            iFieldIndex = fieldIndex;
        }

        /**
         * Gets the field that this property uses.
         * 
         * @return the field
         */
        public DateTimeField getField() {
            return iInstant.getField(iFieldIndex);
        }

        /**
         * Gets the instant that this property belongs to.
         * 
         * @return the partial instant
         */
        public PartialInstant getPartialInstant() {
            return iInstant;
        }

        /**
         * Gets the instant that this property belongs to.
         * 
         * @return the partial instant
         */
        public TimeOfDay getTimeOfDay() {
            return iInstant;
        }

        /**
         * Gets the value of the field that the partial instant is set to.
         * 
         * @return the field value
         */
        public int get() {
            return iInstant.getValue(iFieldIndex);
        }

        //-----------------------------------------------------------------------
// TODO
//        /**
//         * Adds to this field in a copy of this TimeOfDay.
//         * <p>
//         * The TimeOnly attached to this property is unchanged by this call.
//         * 
//         * @param value  the value to add to the field in the copy
//         * @return a copy of the TimeOnly with the field value changed
//         * @throws IllegalArgumentException if the value isn't valid
//         */
//        public TimeOfDay addToCopy(int value) {
//            int[] newValues = getField().add(getInstant(), value);
//            return new TimeOfDay(getInstant(), newValues);
//        }
//
//        /**
//         * Adds to this field, possibly wrapped, in a copy of this TimeOfDay.
//         * A wrapped operation only changes this field.
//         * Thus 12:59:00 addWrapped one minute goes to 12:00:00.
//         * <p>
//         * The TimeOfDay attached to this property is unchanged by this call.
//         * 
//         * @param value  the value to add to the field in the copy
//         * @return a copy of the TimeOfDay with the field value changed
//         * @throws IllegalArgumentException if the value isn't valid
//         */
//        public TimeOfDay addWrappedToCopy(int value) {
//            int[] newValues = getField().addWrapped(getInstant(), value);
//            return new TimeOfDay(getInstant(), newValues);
//        }
//
        //-----------------------------------------------------------------------
        /**
         * Sets this field in a copy of the TimeOfDay.
         * <p>
         * The TimeOfDay attached to this property is unchanged by this call.
         * 
         * @param value  the value to set the field in the copy to
         * @return a copy of the TimeOfDay with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public TimeOfDay setCopy(int value) {
            int[] newValues = getField().set(iInstant, iFieldIndex, value);
            return new TimeOfDay(iInstant, newValues);
        }

//        /**
//         * Sets this field in a copy of the TimeOfDay to a parsed text value.
//         * <p>
//         * The TimeOfDay attached to this property is unchanged by this call.
//         * 
//         * @param text  the text value to set
//         * @param locale  optional locale to use for selecting a text symbol
//         * @return a copy of the TimeOfDay with the field value changed
//         * @throws IllegalArgumentException if the text value isn't valid
//         */
//        public TimeOfDay setCopy(String text, Locale locale) {
//            int[] newValues = getField().set(getInstant(), text, locale);
//            return new TimeOfDay(getInstant(), newValues);
//        }
//
//        /**
//         * Sets this field in a copy of the TimeOfDay to a parsed text value.
//         * <p>
//         * The TimeOfDay attached to this property is unchanged by this call.
//         * This operation is faster than converting a TimeOfDay to a MutableTimeOfDay
//         * and back again when setting one field. When setting multiple fields,
//         * it is generally quicker to make the conversion to MutableTimeOfDay.
//         * 
//         * @param text  the text value to set
//         * @return a copy of the TimeOfDay with the field value changed
//         * @throws IllegalArgumentException if the text value isn't valid
//         */
//        public final TimeOfDay setCopy(String text) {
//            return setCopy(text, null);
//        }

//        //-----------------------------------------------------------------------
//        /**
//         * Rounds to the lowest whole unit of this field on a copy of this TimeOfDay.
//         *
//         * @return a copy of the TimeOfDay with the field value changed
//         */
//        public TimeOfDay roundFloorCopy() {
//            TimeOfDay instant = iInstant;
//            return (TimeOfDay) instant.withMillis(iField.roundFloor(instant.getMillis()));
//        }
//
//        /**
//         * Rounds to the highest whole unit of this field on a copy of this TimeOfDay.
//         *
//         * @return a copy of the TimeOfDay with the field value changed
//         */
//        public TimeOfDay roundCeilingCopy() {
//            TimeOfDay instant = iInstant;
//            return (TimeOfDay) instant.withMillis(iField.roundCeiling(instant.getMillis()));
//        }
//
//        /**
//         * Rounds to the nearest whole unit of this field on a copy of this TimeOfDay,
//         * favoring the floor if halfway.
//         *
//         * @return a copy of the TimeOfDay with the field value changed
//         */
//        public TimeOfDay roundHalfFloorCopy() {
//            TimeOfDay instant = iInstant;
//            return (TimeOfDay) instant.withMillis(iField.roundHalfFloor(instant.getMillis()));
//        }
//
//        /**
//         * Rounds to the nearest whole unit of this field on a copy of this TimeOfDay,
//         * favoring the ceiling if halfway.
//         *
//         * @return a copy of the TimeOfDay with the field value changed
//         */
//        public TimeOfDay roundHalfCeilingCopy() {
//            TimeOfDay instant = iInstant;
//            return (TimeOfDay) instant.withMillis(iField.roundHalfCeiling(instant.getMillis()));
//        }
//
//        /**
//         * Rounds to the nearest whole unit of this field on a copy of this TimeOfDay.
//         * If halfway, the ceiling is favored over the floor only if it makes this field's value even.
//         *
//         * @return a copy of the TimeOfDay with the field value changed
//         */
//        public TimeOfDay roundHalfEvenCopy() {
//            TimeOfDay instant = iInstant;
//            return (TimeOfDay) instant.withMillis(iField.roundHalfEven(instant.getMillis()));
//        }
    }

}
