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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;

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
 * AbstractPartial provides a standard base implementation of most methods
 * in the ReadablePartial interface.
 * <p>
 * Calculations on are performed using a {@link Chronology}.
 * This chronology is set to be in the UTC time zone for all calculations.
 * <p>
 * AbstractPartial allows subclasses may be mutable and not thread-safe.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public abstract class AbstractPartial implements ReadablePartial, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 2353678632973660L;

    /** The chronology in use */
    protected Chronology iChronology;
    /** The values of each field in this partial */
    protected int[] iValues;
    /** The values of each field in this partial */
    protected transient DateTimeField[] iFields;

    // Constructors
    //-----------------------------------------------------------------------
    /**
     * Constructs a AbstractPartial with the current time, using ISOChronology in
     * the default zone to extract the fields.
     * <p>
     * The constructor uses the default time zone, resulting in the local time
     * being initialised. Once the constructor is complete, all further calculations
     * are performed without reference to a timezone (by switching to UTC).
     */
    public AbstractPartial() {
        this(DateTimeUtils.currentTimeMillis(), null);
    }

    /**
     * Constructs a AbstractPartial with the current time, using the specified chronology
     * and zone to extract the fields.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     *
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public AbstractPartial(Chronology chronology) {
        this(DateTimeUtils.currentTimeMillis(), chronology);
    }

    /**
     * Constructs a AbstractPartial extracting the partial fields from the specified
     * milliseconds using the ISOChronology in the default zone.
     * <p>
     * The constructor uses the default time zone, resulting in the local time
     * being initialised. Once the constructor is complete, all further calculations
     * are performed without reference to a timezone (by switching to UTC).
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     */
    public AbstractPartial(long instant) {
        this(instant, null);
    }

    /**
     * Constructs a AbstractPartial extracting the partial fields from the specified
     * milliseconds using the chronology provided.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public AbstractPartial(long instant, Chronology chronology) {
        super();
        if (chronology == null) {
            chronology = ISOChronology.getInstance();
        }
        iChronology = chronology.withUTC();
        iFields = initFields(iChronology);
        iValues = initValues(instant, chronology);
    }

    /**
     * Constructs a AbstractPartial from an Object that represents a time.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  the datetime object, must not be null
     * @throws IllegalArgumentException if the date is null
     */
    public AbstractPartial(Object instant) {
        super();
        InstantConverter converter = ConverterManager.getInstance().getInstantConverter(instant);
        long millis = converter.getInstantMillis(instant);
        Chronology chronology = converter.getChronology(instant);
        if (chronology == null) {
            chronology = ISOChronology.getInstance();
        }
        iChronology = chronology.withUTC();
        iFields = initFields(iChronology);
        iValues = initValues(millis, chronology);
    }

    /**
     * Constructs a AbstractPartial from an Object that represents a time, using the
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
    public AbstractPartial(Object instant, Chronology chronology) {
        super();
        InstantConverter converter = ConverterManager.getInstance().getInstantConverter(instant);
        long millis = converter.getInstantMillis(instant, chronology);
        chronology = converter.getChronology(instant, chronology);
        if (chronology == null) {
            chronology = ISOChronology.getInstance();
        }
        iChronology = chronology.withUTC();
        iFields = initFields(iChronology);
        iValues = initValues(millis, chronology);
    }

    /**
     * Constructs a AbstractPartial with specified time field values and chronology.
     * <p>
     * The constructor uses the time zone of the chronology specified.
     * Once the constructor is complete, all further calculations are performed
     * without reference to a timezone (by switching to UTC).
     *
     * @param values  the new set of values
     * @param chronology  the chronology, null means ISOChronology in the default zone
     */
    public AbstractPartial(int[] values, Chronology chronology) {
        super();
        if (chronology == null) {
            chronology = ISOChronology.getInstance();
        }
        iChronology = chronology.withUTC();
        iFields = initFields(iChronology);
        iValues = values;
        chronology.validate(this);
    }

    //-----------------------------------------------------------------------
    /**
     * Recreates the state of this object after deserialization.
     * 
     * @param in  the input stream
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        iFields = initFields(iChronology);
    }

    /**
     * Initialize the array of fields.
     * The field and value arrays must match.
     * 
     * @param chrono  the chronology to use
     */
    protected abstract DateTimeField[] initFields(Chronology chrono);

    /**
     * Initialize the array of values.
     * The field and value arrays must match.
     * 
     * @param instant  the instant to use
     * @param chrono  the chronology to use
     */
    protected abstract int[] initValues(long instant, Chronology chrono);

    //-----------------------------------------------------------------------
    /**
     * Gets the number of fields in this partial.
     * 
     * @return the field count
     */
    public int getFieldSize() {
        return iFields.length;
    }

    /**
     * Gets the field at the specifed index.
     * 
     * @param index  the index
     * @return the field
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public DateTimeField getField(int index) {
        if (index < 0 || index >= iFields.length) {
            throw new IllegalArgumentException(Integer.toString(index));
        }
        return iFields[index];
    }

    /**
     * Gets the value of the field at the specifed index.
     * 
     * @param index  the index
     * @return the value
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public int getValue(int index) {
        if (index < 0 || index >= iValues.length) {
            throw new IllegalArgumentException(Integer.toString(index));
        }
        return iValues[index];
    }

    //-----------------------------------------------------------------------
    /**
     * Gets an array of the fields that this partial supports.
     * <p>
     * The fields are returned largest to smallest, for example Hour, Minute, Second.
     *
     * @return the fields supported (cloned), largest to smallest
     */
    public DateTimeField[] getFields() {
        return (DateTimeField[]) iFields.clone();
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
     * @return the chronology
     */
    public Chronology getChronology() {
        return iChronology;
    }

    //-----------------------------------------------------------------------
    /**
     * Get the value of one of the fields of a datetime.
     * <p>
     * The field specified must be one of those that is supported by the partial.
     *
     * @param field  a DateTimeField instance that is supported by this partial
     * @return the value of that field
     * @throws IllegalArgumentException if the field is null or not supported
     */
    public int get(DateTimeField field) {
        for (int i = 0; i < iFields.length; i++) {
            if (iFields[i] == field) {
                return iValues[i];
            }
        }
        throw new IllegalArgumentException("Field '" + field + "' is not supported");
    }

    /**
     * Checks whether the field specified is supported by this partial.
     *
     * @param field  the field to check, may be null which returns false
     * @return true if the field is supported
     */
    public boolean isSupported(DateTimeField field) {
        for (int i = 0; i < iFields.length; i++) {
            if (iFields[i] == field) {
                return true;
            }
        }
        return false;
    }

    //-----------------------------------------------------------------------
    /**
     * Resolves this partial against another complete millisecond instant to
     * create a new full instant specifying the time zone to resolve with.
     * <p>
     * For example, if this partial represents a time, then the result of this
     * method will be the datetime from the specified base instant plus the
     * time from this partial set using the time zone specified.
     *
     * @param baseInstant  source of missing fields
     * @param zone  the time zone to use, null means default
     * @return the combined instant in milliseconds
     */
    public long resolve(long baseInstant, DateTimeZone zone) {
        Chronology chrono = iChronology.withZone(zone);
        return resolve(baseInstant, chrono);
    }

    /**
     * Resolves this partial against another complete instant to create a new
     * full instant. The combination is performed using the chronology of the
     * specified instant.
     * <p>
     * For example, if this partial represents a time, then the result of this
     * method will be the datetime from the specified base instant plus the
     * time from this partial.
     *
     * @param baseInstant  the instant that provides the missing fields, null means now
     * @return the combined datetime
     */
    public DateTime resolveDateTime(ReadableInstant baseInstant) {
        long resolved;
        Chronology chrono;
        if (baseInstant == null) {
            chrono = ISOChronology.getInstance();
            resolved = resolve(DateTimeUtils.currentTimeMillis(), chrono);
        } else {
            chrono = baseInstant.getChronology();
            resolved = resolve(baseInstant.getMillis(), chrono);
        }
        return new DateTime(resolved, chrono);
    }

    /**
     * Resolves this partial into another complete instant setting the relevant
     * fields on the writable instant. The combination is performed using the
     * chronology of the specified instant.
     * <p>
     * For example, if this partial represents a time, then the input writable
     * instant will be updated with the time from this partial.
     *
     * @param baseInstant  the instant to set into, must not be null
     * @throws IllegalArgumentException if the base instant is null
     */
    public void resolveInto(ReadWritableInstant baseInstant) {
        if (baseInstant == null) {
            throw new IllegalArgumentException("The instant must not be null");
        }
        Chronology chrono = baseInstant.getChronology();
        long resolved = resolve(baseInstant.getMillis(), chrono);
        baseInstant.setMillis(resolved);
    }

    /**
     * Resolve this partial into the base millis using the specified chronology.
     * 
     * @param baseInstant  the base millisecond instant
     * @param chrono  the chronology
     * @return the new resolved millis
     */
    protected long resolve(long baseInstant, Chronology chrono) {
        long millis = baseInstant;
        for (int i = 0; i < iFields.length; i++) {
            millis = iFields[i].set(millis, iValues[i]);
        }
        return millis;
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this ReadablePartial with another returning true if the chronology,
     * fields and values are equal.
     *
     * @param partial  an object to check against
     * @return true if fields and values are equal
     */
    public boolean equals(Object partial) {
        if (partial instanceof AbstractPartial) {
            AbstractPartial other = (AbstractPartial) partial;
            return Arrays.equals(iValues, other.iValues) &&
                   Arrays.equals(iFields, other.iFields) &&
                   iChronology == other.iChronology;
        } else if (partial instanceof ReadablePartial) {
            ReadablePartial other = (ReadablePartial) partial;
            return Arrays.equals(iValues, other.getValues()) &&
                   Arrays.equals(iFields, other.getFields()) &&
                   iChronology == other.getChronology();
        }
        return false;
    }

    /**
     * Gets a hash code for the ReadablePartial that is compatible with the 
     * equals method.
     *
     * @return a suitable hash code
     */
    public int hashCode() {
        int total = 157;
        for (int i = 0; i < iFields.length; i++) {
            total = 23 * total + iValues[i];
            total = 23 * total + iFields[i].hashCode();
        }
        total += iChronology.hashCode();
        return total;
    }

}
