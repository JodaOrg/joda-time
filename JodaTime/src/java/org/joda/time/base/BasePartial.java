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
package org.joda.time.base;

import java.io.Serializable;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.ReadablePartial;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.InstantConverter;

/**
 * BasePartial is an abstract implementation of ReadablePartial that stores
 * data in array and <code>Chronology</code> fields.
 * <p>
 * This class should generally not be used directly by API users.
 * The {@link org.joda.time.ReadablePeriod} interface should be used when different 
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
    private Chronology iChronology;
    /** The values of each field in this partial */
    private int[] iValues;

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
        iValues = initValues(instant, chronology);
    }

    /**
     * Constructs a partial from an Object that represents a time.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  the datetime object, must not be null
     * @throws IllegalArgumentException if the date is null
     */
    protected BasePartial(Object instant) {
        super();
        InstantConverter converter = ConverterManager.getInstance().getInstantConverter(instant);
        long millis = converter.getInstantMillis(instant);
        Chronology chronology = converter.getChronology(instant);
        chronology = DateTimeUtils.getChronology(chronology);
        iChronology = chronology.withUTC();
        iValues = initValues(millis, chronology);
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
     * @param instant  the datetime object, must not be null
     * @param chronology  the chronology, null means ISOChronology
     * @throws IllegalArgumentException if the date is null
     */
    protected BasePartial(Object instant, Chronology chronology) {
        super();
        InstantConverter converter = ConverterManager.getInstance().getInstantConverter(instant);
        long millis = converter.getInstantMillis(instant, chronology);
        chronology = converter.getChronology(instant, chronology);
        chronology = DateTimeUtils.getChronology(chronology);
        iChronology = chronology.withUTC();
        iValues = initValues(millis, chronology);
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
     * @param other  the other partial to use to extract the fields and chronology
     * @param values  the new set of values, not cloned
     */
    protected BasePartial(BasePartial other, int[] values) {
        super();
        iChronology = other.iChronology;
        iValues = values;
    }

    //-----------------------------------------------------------------------
    /**
     * Initialize the array of values.
     * The field and value arrays must match.
     * 
     * @param instant  the instant to use
     * @param chrono  the chronology to use
     */
    protected int[] initValues(long instant, Chronology chrono) {
        int[] values = new int[size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = getField(i, chrono).get(instant);
        }
        return values;
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
     * Sets the value of the field at the specifed index.
     * 
     * @param index  the index
     * @param value  the value to set
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    protected void setValue(int index, int value) {
        DateTimeField field = getField(index);
        iValues = field.set(this, index, iValues, value);
    }

    /**
     * Sets the values of all fields.
     * 
     * @param values  the array of values
     */
    protected void setValues(int[] values) {
        getChronology().validate(this, values);
        iValues = values;
    }

}
