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
import java.util.Locale;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadWritableInstant;
import org.joda.time.ReadableInstant;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.InstantConverter;

/**
 * DayOfWeek is an immutable partial instant supporting the dayOfWeek field.
 * <p>
 * The day of week field is restricted to seven values for the ISO chronology.
 * These are provided as constants.
 * <p>
 * Calculations on DayOfWeek are performed using a {@link Chronology}.
 * This chronology is set to be in the UTC time zone for all calculations.
 * <p>
 * The day of week field can be queried in two ways:
 * <ul>
 * <li><code>getDayOfWeek()</code>
 * <li><code>dayOfWeek().get()</code>
 * </ul>
 * The second technique also provides access to other useful methods on the field:
 * <ul>
 * <li>numeric value - <code>dayOfWeek().get()</code>
 * <li>text value - <code>dayOfWeek().getAsText()</code>
 * <li>short text value - <code>dayOfWeek().getAsShortText()</code>
 * <li>maximum/minimum values - <code>dayOfWeek().getMaximumValue()</code>
 * <li>add/subtract - <code>dayOfWeek().addToCopy()</code>
 * <li>set - <code>dayOfWeek().setCopy()</code>
 * </ul>
 * <p>
 * DayOfWeek is thread-safe and immutable, provided that the Chronology is as well.
 * All standard Chronology classes supplied are thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public final class DayOfWeek implements PartialInstant, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 65294869236483L;

    /** The index of the dayOfWeek field in the field array. */
    public static final int DAY_OF_WEEK = 0;

    /** A day of week constant for Monday in the ISOChronology. */
    public static final DayOfWeek MONDAY = new DayOfWeek(1, null);
    /** A day of week constant for Monday in the ISOChronology. */
    public static final DayOfWeek TUESDAY = new DayOfWeek(2, null);
    /** A day of week constant for Monday in the ISOChronology. */
    public static final DayOfWeek WEDNESDAY = new DayOfWeek(3, null);
    /** A day of week constant for Monday in the ISOChronology. */
    public static final DayOfWeek THURSDAY = new DayOfWeek(4, null);
    /** A day of week constant for Monday in the ISOChronology. */
    public static final DayOfWeek FRIDAY = new DayOfWeek(5, null);
    /** A day of week constant for Monday in the ISOChronology. */
    public static final DayOfWeek SATURDAY = new DayOfWeek(6, null);
    /** A day of week constant for Monday in the ISOChronology. */
    public static final DayOfWeek SUNDAY = new DayOfWeek(7, null);
    
    /** The chronology. */
    private final Chronology iChronology;
    /** The value. */
    private final int iValue;

    // Factories
    //-----------------------------------------------------------------------
    /**
     * Obtains a DayOfWeek by day value in the <code>ISOChronology</code>.
     *
     * @param dayOfWeek  the ISO day of the week, 1 (Monday) - 7 (Sunday)
     * @return a DayOfWeek constant
     */
    public static DayOfWeek getInstance(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
            return MONDAY;
            case 2:
            return TUESDAY;
            case 3:
            return WEDNESDAY;
            case 4:
            return THURSDAY;
            case 5:
            return FRIDAY;
            case 6:
            return SATURDAY;
            case 7:
            return SUNDAY;
        }
        throw new IllegalArgumentException("Invalid day of week: " + dayOfWeek);
    }

    /**
     * Constructs a DayOfWeek with specified day value and chronology.
     *
     * @param dayOfWeek  the ISO day of the week, 1 (Monday) - 7 (Sunday)
     * @param chronology  the chronology, null means ISOChronology
     * @return a DayOfWeek
     */
    public static DayOfWeek getInstance(int dayOfWeek, Chronology chronology) {
        if (chronology == null || chronology instanceof ISOChronology) {
            return getInstance(dayOfWeek);
        }
        return new DayOfWeek(dayOfWeek, chronology);
    }

    /**
     * Obtains a DayOfWeek with the current time, using ISOChronology in
     * the default zone to extract the fields.
     * <p>
     * The constructor uses the default time zone, resulting in the local time
     * being initialised. Once the constructor is complete, all further calculations
     * are performed without reference to a timezone (by switching to UTC).
     * 
     * @return a DayOfWeek constant
     */
    public static DayOfWeek getInstanceNow() {
        return getInstance(DateTimeUtils.currentTimeMillis());
    }

    /**
     * Obtains a DayOfWeek with the current time, using the specified chronology
     * and zone to extract the fields.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     *
     * @param chronology  the chronology, null means ISOChronology in the default zone
     * @return a DayOfWeek
     */
    public static DayOfWeek getInstanceNow(Chronology chronology) {
        return getInstance(DateTimeUtils.currentTimeMillis(), chronology);
    }

    /**
     * Obtains a DayOfWeek by millisecond in the <code>ISOChronology</code>.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @return a DayOfWeek constant
     */
    public static DayOfWeek getInstance(long instant) {
        int dayOfWeek = ISOChronology.getInstance().dayOfWeek().get(instant);
        return getInstance(dayOfWeek);
    }

    /**
     * Obtains a DayOfWeek extracting the partial fields from the specified
     * milliseconds using the chronology provided.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @param chronology  the chronology, null means ISOChronology in the default zone
     * @return a DayOfWeek
     */
    public static DayOfWeek getInstance(long instant, Chronology chronology) {
        if (chronology == null) {
            chronology = ISOChronology.getInstance();
        }
        int dayOfWeek = chronology.dayOfWeek().get(instant);
        return getInstance(dayOfWeek, chronology);
    }

    /**
     * Obtains a DayOfWeek from an Object that represents a time.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  the datetime object, null means now
     * @throws IllegalArgumentException if the date is invalid
     * @return a DayOfWeek
     */
    public static DayOfWeek getInstance(Object instant) {
        InstantConverter converter = ConverterManager.getInstance().getInstantConverter(instant);
        long millis = converter.getInstantMillis(instant);
        Chronology chronology = converter.getChronology(instant);
        if (chronology == null) {
            chronology = ISOChronology.getInstance();
        }
        return getInstance(millis, chronology);
    }

    /**
     * Obtains a DayOfWeek from an Object that represents a time, using the
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
     * @param instant  the datetime object, null means now
     * @param chronology  the chronology, null means ISOChronology
     * @throws IllegalArgumentException if the date is invalid
     * @return a DayOfWeek
     */
    public static DayOfWeek getInstance(Object instant, Chronology chronology) {
        InstantConverter converter = ConverterManager.getInstance().getInstantConverter(instant);
        long millis = converter.getInstantMillis(instant, chronology);
        chronology = converter.getChronology(instant, chronology);
        if (chronology == null) {
            chronology = ISOChronology.getInstance();
        }
        return getInstance(millis, chronology);
    }

    // Constructors
    //-----------------------------------------------------------------------
    /**
     * Constructs a DayOfWeek with specified day value and chronology.
     *
     * @param dayOfWeek  the ISO day of the week, 1 (Monday) - 7 (Sunday)
     * @param chronology  the chronology, null means ISOChronology
     */
    private DayOfWeek(int dayOfWeek, Chronology chronology) {
        super();
        if (chronology == null) {
            chronology = ISOChronology.getInstance();
        }
        iChronology = chronology.withUTC();
        iValue = dayOfWeek;
        chronology.validate(this);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the number of fields in this instant.
     * 
     * @return the field count
     */
    public int getFieldSize() {
        return 1;
    }

    /**
     * Gets the field at the specifed index.
     * 
     * @param index  the index
     * @return the field
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public DateTimeField getField(int index) {
        if (index != 0) {
            throw new IllegalArgumentException(Integer.toString(index));
        }
        return iChronology.dayOfWeek();
    }

    /**
     * Gets the value of the field at the specifed index.
     * 
     * @param index  the index
     * @return the value
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public int getValue(int index) {
        if (index != 0) {
            throw new IllegalArgumentException(Integer.toString(index));
        }
        return iValue;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets an array containing the day of week field.
     *
     * @return the fields supported (cloned)
     */
    public DateTimeField[] getFields() {
        return new DateTimeField[] {iChronology.dayOfWeek()};
    }

    /**
     * Gets an array containing the day of week value.
     *
     * @return the current values of each field (cloned)
     */
    public int[] getValues() {
        return new int[] {iValue};
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

    //-----------------------------------------------------------------------
    /**
     * Get the value of one of the fields of this partial.
     * <p>
     * The field specified must be one of those that is supported by the partial instant.
     *
     * @param field  a DateTimeField instance that is supported by this partial
     * @return the value of that field
     * @throws IllegalArgumentException if the field is null or not supported
     */
    public int get(DateTimeField field) {
        if (iChronology.dayOfWeek() == field) {
            return iValue;
        }
        throw new IllegalArgumentException("Field '" + field + "' is not supported");
    }

    /**
     * Checks whether the field specified is supported by this partial instant.
     *
     * @param field  the field to check, may be null which returns false
     * @return true if the field is supported
     */
    public boolean isSupported(DateTimeField field) {
        if (iChronology.dayOfWeek() == field) {
            return true;
        }
        return false;
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
        return resolve(baseMillis, chrono);
    }

    /**
     * Resolves this partial into another complete instant setting the relevant fields
     * on the writable instant. The combination is performed using the chronology of the
     * specified instant.
     * <p>
     * For example, if this partial represents a time, then the input writable instant
     * will be updated with the time from this instant.
     *
     * @param base  the instant to set into, must not be null
     * @throws IllegalArgumentException if the base instant is null
     */
    public void resolveInto(ReadWritableInstant base) {
        if (base == null) {
            throw new IllegalArgumentException("The instant must not be null");
        }
        Chronology chrono = base.getChronology();
        long resolved = resolve(base.getMillis(), chrono);
        base.setMillis(resolved);
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
        long resolved;
        Chronology chrono;
        if (base == null) {
            chrono = ISOChronology.getInstance();
            resolved = resolve(DateTimeUtils.currentTimeMillis(), chrono);
        } else {
            chrono = base.getChronology();
            resolved = resolve(base.getMillis(), chrono);
        }
        return new DateTime(resolved, chrono);
    }

    /**
     * Resolve this partial instant into the base millis using the specified chronology.
     * 
     * @param baseMillis  the base millis
     * @param chrono  the chronology
     * @return the new resolved millis
     */
    protected long resolve(long baseMillis, Chronology chrono) {
        return iChronology.dayOfWeek().set(baseMillis, iValue);
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this PartialInstant with another returning true if the chronology,
     * fields and values are equal.
     *
     * @param instant  an object to check against
     * @return true if fields and values are equal
     */
    public boolean equals(Object instant) {
        if (instant instanceof PartialInstant) {
            PartialInstant other = (PartialInstant) instant;
            return other.getFieldSize() == 1 &&
                   other.isSupported(iChronology.dayOfWeek()) &&
                   iValue == other.get(iChronology.dayOfWeek()) &&
                   iChronology == other.getChronology();
        }
        return false;
    }

    /**
     * Gets a hash code for the PartialInstant that is compatible with the 
     * equals method.
     *
     * @return a suitable hash code
     */
    public int hashCode() {
        int total = 157;
        total = 23 * total + iValue;
        total = 23 * total + iChronology.dayOfWeek().hashCode();
        total += iChronology.hashCode();
        return total;
    }

    /**
     * Output the time in an ISO8601 format.
     * 
     * @return ISO8601 formatted string
     */
    public String toString() {
        // TODO
        return "";
    }

    //-----------------------------------------------------------------------
    /**
     * Get the ISO day of week (1-7) field value.
     *
     * @return the ISO day of the week, 1 (Monday) - 7 (Sunday)
     */
    public int getDayOfWeek() {
        return iValue;
    }

    //-----------------------------------------------------------------------
    /**
     * Get the ISO day of week (1-7) field property
     * 
     * @return the ISO day of week property
     */
    public Property dayOfWeek() {
        return new Property(this);
    }

    //-----------------------------------------------------------------------
    /**
     * The property class for DayOfWeek.
     */
    public static class Property extends AbstractPartialFieldProperty {

        /** The instant */
        private final DayOfWeek iInstant;

        /**
         * Constructs a property.
         * 
         * @param instant  the partial instant
         */
        Property(DayOfWeek instant) {
            super();
            iInstant = instant;
        }

        /**
         * Gets the field that this property uses.
         * 
         * @return the field
         */
        public DateTimeField getField() {
            return iInstant.iChronology.dayOfWeek();
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
        public DayOfWeek getDayOfWeek() {
            return iInstant;
        }

        /**
         * Gets the value of the field that the partial instant is set to.
         * 
         * @return the field value
         */
        public int get() {
            return iInstant.iValue;
        }

        //-----------------------------------------------------------------------
        /**
         * Adds to the value of this field in a copy of this DayOfWeek.
         * <p>
         * The value will be added to this field. If the result is too large
         * (more than 7) or too small (less than 1) then an exception is thrown.
         * <p>
         * The DayOfWeek attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param valueToAdd  the value to add to the field in the copy
         * @return a copy of the DayOfWeek with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public DayOfWeek addCopy(int valueToAdd) {
            int[] newValues = iInstant.getValues();
            getField().add(iInstant, 0, newValues, valueToAdd);
            return DayOfWeek.getInstance(newValues[0], iInstant.getChronology());
        }

        /**
         * Adds to the value of this field in a copy of this DayOfWeek wrapping
         * within this field if the maximum value is reached.
         * <p>
         * The value will be added to this field. If the result is too large
         * (more than 7) or too small (less than 1) then it will wrap.
         * <p>
         * The DayOfWeek attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param valueToAdd  the value to add to the field in the copy
         * @return a copy of the DayOfWeek with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public DayOfWeek addInFieldCopy(int valueToAdd) {
            int[] newValues = iInstant.getValues();
            getField().addInField(iInstant, 0, newValues, valueToAdd);
            return DayOfWeek.getInstance(newValues[0], iInstant.getChronology());
        }

        //-----------------------------------------------------------------------
        /**
         * Sets this field in a copy of the DayOfWeek.
         * <p>
         * The DayOfWeek attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param value  the value to set the field in the copy to
         * @return a copy of the DayOfWeek with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public DayOfWeek setCopy(int value) {
            int[] newValues = iInstant.getValues();
            getField().set(iInstant, 0, newValues, value);
            return DayOfWeek.getInstance(newValues[0], iInstant.getChronology());
        }

        /**
         * Sets this field in a copy of the DayOfWeek to a parsed text value.
         * <p>
         * The DayOfWeek attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param text  the text value to set
         * @param locale  optional locale to use for selecting a text symbol
         * @return a copy of the DayOfWeek with the field value changed
         * @throws IllegalArgumentException if the text value isn't valid
         */
        public DayOfWeek setCopy(String text, Locale locale) {
            int[] newValues = iInstant.getValues();
            getField().set(iInstant, 0, newValues, text, locale);
            return DayOfWeek.getInstance(newValues[0], iInstant.getChronology());
        }

        /**
         * Sets this field in a copy of the DayOfWeek to a parsed text value.
         * <p>
         * The DayOfWeek attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param text  the text value to set
         * @return a copy of the DayOfWeek with the field value changed
         * @throws IllegalArgumentException if the text value isn't valid
         */
        public DayOfWeek setCopy(String text) {
            return setCopy(text, null);
        }
    }

}
