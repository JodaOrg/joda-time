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
package org.joda.time.base;

import java.io.Serializable;
import java.util.Locale;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.ReadablePartial;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.PartialConverter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * BasePartial is an abstract implementation of ReadablePartial that stores
 * data in array and <code>Chronology</code> fields.
 * <p>
 * This class should generally not be used directly by API users.
 * The {@link org.joda.time.ReadablePartial} interface should be used when different 
 * kinds of partial objects are to be referenced.
 * <p>
 * BasePartial subclasses may be mutable and not thread-safe.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public abstract class BasePartial
        extends AbstractPartial
        implements ReadablePartial, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 2353678632973660L;

    /** The chronology in use */
    private final Chronology iChronology;
    /** The values of each field in this partial */
    private final int[] iValues;

    //-----------------------------------------------------------------------
    /**
     * Constructs a partial with the current time, using ISOChronology in
     * the default zone to extract the fields.
     * <p>
     * The constructor uses the default time zone, resulting in the local time
     * being initialised. Once the constructor is complete, all further calculations
     * are performed without reference to a timezone (by switching to UTC).
     */
    protected BasePartial() {
        this(DateTimeUtils.currentTimeMillis(), null);
    }

    /**
     * Constructs a partial with the current time, using the specified chronology
     * and zone to extract the fields.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     *
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    protected BasePartial(Chronology chronology) {
        this(DateTimeUtils.currentTimeMillis(), chronology);
    }

    /**
     * Constructs a partial extracting the partial fields from the specified
     * milliseconds using the ISOChronology in the default zone.
     * <p>
     * The constructor uses the default time zone, resulting in the local time
     * being initialised. Once the constructor is complete, all further calculations
     * are performed without reference to a timezone (by switching to UTC).
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     */
    protected BasePartial(long instant) {
        this(instant, null);
    }

    /**
     * Constructs a partial extracting the partial fields from the specified
     * milliseconds using the chronology provided.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    protected BasePartial(long instant, Chronology chronology) {
        super();
        chronology = DateTimeUtils.getChronology(chronology);
        iChronology = chronology.withUTC();
        iValues = chronology.get(this, instant);
    }

    /**
     * Constructs a partial from an Object that represents a time, using the
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
     * @param instant  the datetime object
     * @param chronology  the chronology, null means use converter
     * @throws IllegalArgumentException if the date is invalid
     */
    protected BasePartial(Object instant, Chronology chronology) {
        super();
        PartialConverter converter = ConverterManager.getInstance().getPartialConverter(instant);
        chronology = converter.getChronology(instant, chronology);
        chronology = DateTimeUtils.getChronology(chronology);
        iChronology = chronology.withUTC();
        iValues = converter.getPartialValues(this, instant, chronology);
    }

    /**
     * Constructs a partial from an Object that represents a time, using the
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
     * @param instant  the datetime object
     * @param chronology  the chronology, null means use converter
     * @param parser  if converting from a String, the given parser is preferred
     * @throws IllegalArgumentException if the date is invalid
     * @since 1.3
     */
    protected BasePartial(Object instant, Chronology chronology, DateTimeFormatter parser) {
        super();
        PartialConverter converter = ConverterManager.getInstance().getPartialConverter(instant);
        chronology = converter.getChronology(instant, chronology);
        chronology = DateTimeUtils.getChronology(chronology);
        iChronology = chronology.withUTC();
        iValues = converter.getPartialValues(this, instant, chronology, parser);
    }

    /**
     * Constructs a partial with specified time field values and chronology.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     * <p>
     * The array of values is assigned (not cloned) to the new instance.
     *
     * @param values  the new set of values
     * @param chronology  the chronology, null means ISOChronology in the default zone
     * @throws IllegalArgumentException if the values are invalid
     */
    protected BasePartial(int[] values, Chronology chronology) {
        super();
        chronology = DateTimeUtils.getChronology(chronology);
        iChronology = chronology.withUTC();
        chronology.validate(this, values);
        iValues = values;
    }

    /**
     * Private constructor to be used by subclasses only which performs no validation.
     * <p>
     * Data is assigned (not cloned) to the new instance.
     *
     * @param base  the base partial
     * @param values  the new set of values, not cloned, null means use base
     */
    protected BasePartial(BasePartial base, int[] values) {
        super();
        iChronology = base.iChronology;
        iValues = values;
    }

    /**
     * Private constructor to be used by subclasses only which performs no validation.
     * <p>
     * Data is assigned (not cloned) to the new instance.
     * This should not be used by mutable subclasses.
     *
     * @param base  the base partial
     * @param chrono  the chronology to use, null means use base
     */
    protected BasePartial(BasePartial base, Chronology chrono) {
        super();
        iChronology = chrono.withUTC();
        iValues = base.iValues;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the value of the field at the specifed index.
     * 
     * @param index  the index
     * @return the value
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public int getValue(int index) {
        return iValues[index];
    }

    /**
     * Gets an array of the value of each of the fields that this partial supports.
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
     * @return the chronology, never null
     */
    public Chronology getChronology() {
        return iChronology;
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the value of the field at the specified index.
     * <p>
     * In version 2.0 and later, this method copies the array into the original.
     * This is because the instance variable has been changed to be final to satisfy the Java Memory Model.
     * This only impacts subclasses that are mutable.
     * 
     * @param index  the index
     * @param value  the value to set
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    protected void setValue(int index, int value) {
        DateTimeField field = getField(index);
        int[] values = field.set(this, index, iValues, value);
        System.arraycopy(values, 0, iValues, 0, iValues.length);
    }

    /**
     * Sets the values of all fields.
     * <p>
     * In version 2.0 and later, this method copies the array into the original.
     * This is because the instance variable has been changed to be final to satisfy the Java Memory Model.
     * This only impacts subclasses that are mutable.
     * 
     * @param values  the array of values
     */
    protected void setValues(int[] values) {
        getChronology().validate(this, values);
        System.arraycopy(values, 0, iValues, 0, iValues.length);
    }

    //-----------------------------------------------------------------------
    /**
     * Output the date using the specified format pattern.
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
     * Output the date using the specified format pattern.
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

}
