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

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadWritableInstant;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;

/**
 * AbstractPartial provides a standard base implementation of most methods
 * in the ReadablePartial interface.
 * <p>
 * Calculations on are performed using a {@link Chronology}.
 * This chronology is set to be in the UTC time zone for all calculations.
 * <p>
 * The methods on this class uses {@link ReadablePartial#getFieldSize()},
 * {@link ReadablePartial#getField(int)} and {@link ReadablePartial#getValue(int)}
 * to calculate their results. Subclasses may have a better implementation.
 * <p>
 * AbstractPartial allows subclasses may be mutable and not thread-safe.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public abstract class AbstractPartial implements ReadablePartial {

    //-----------------------------------------------------------------------
    /**
     * Constructor.
     */
    protected AbstractPartial() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the field for a specific index in the chronology specified.
     * <p>
     * This method must not use any instance variables.
     * 
     * @param index  the index to retrieve
     * @param chrono  the chronology to use
     * @return the field
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    protected abstract DateTimeField getField(int index, Chronology chrono);

    //-----------------------------------------------------------------------
    /**
     * Gets the field type at the specifed index.
     * 
     * @param index  the index
     * @return the field type
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public DateTimeFieldType getFieldType(int index) {
        return getField(index, getChronology()).getType();
    }

    /**
     * Gets an array of the field types that this partial supports.
     * <p>
     * The fields are returned largest to smallest, for example Hour, Minute, Second.
     *
     * @return the fields supported in an array that may be altered, largest to smallest
     */
    public DateTimeFieldType[] getFieldTypes() {
        DateTimeFieldType[] result = new DateTimeFieldType[size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = getFieldType(i);
        }
        return result;
    }

    /**
     * Gets the field at the specifed index.
     * 
     * @param index  the index
     * @return the field
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public DateTimeField getField(int index) {
        return getField(index, getChronology());
    }

    /**
     * Gets an array of the fields that this partial supports.
     * <p>
     * The fields are returned largest to smallest, for example Hour, Minute, Second.
     *
     * @return the fields supported in an array that may be altered, largest to smallest
     */
    public DateTimeField[] getFields() {
        DateTimeField[] result = new DateTimeField[size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = getField(i);
        }
        return result;
    }

    /**
     * Gets an array of the value of each of the fields that this partial supports.
     * <p>
     * The fields are returned largest to smallest, for example Hour, Minute, Second.
     * Each value corresponds to the same array index as <code>getFields()</code>
     *
     * @return the current values of each field in an array that may be altered, largest to smallest
     */
    public int[] getValues() {
        int[] result = new int[size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = getValue(i);
        }
        return result;
    }

    //-----------------------------------------------------------------------
    /**
     * Get the value of one of the fields of a datetime.
     * <p>
     * The field specified must be one of those that is supported by the partial.
     *
     * @param type  a DateTimeFieldType instance that is supported by this partial
     * @return the value of that field
     * @throws IllegalArgumentException if the field is null or not supported
     */
    public int get(DateTimeFieldType type) {
        for (int i = 0, isize = size(); i < isize; i++) {
            if (getFieldType(i) == type) {
                return getValue(i);
            }
        }
        throw new IllegalArgumentException("Field '" + type + "' is not supported");
    }

    /**
     * Checks whether the field specified is supported by this partial.
     *
     * @param type  the type to check, may be null which returns false
     * @return true if the field is supported
     */
    public boolean isSupported(DateTimeFieldType type) {
        for (int i = 0, isize = size(); i < isize; i++) {
            if (getFieldType(i) == type) {
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
        Chronology chrono = getChronology().withZone(zone);
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
        Chronology chrono = DateTimeUtils.getInstantChronology(baseInstant);
        long instantMillis = DateTimeUtils.getInstantMillis(baseInstant);
        long resolved = resolve(instantMillis, chrono);
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
        for (int i = 0, isize = size(); i < isize; i++) {
            millis = getField(i, chrono).set(millis, getValue(i));
        }
        return millis;
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this ReadablePartial with another returning true if the chronology,
     * field types and values are equal.
     *
     * @param partial  an object to check against
     * @return true if fields and values are equal
     */
    public boolean equals(Object partial) {
        if (this == partial) {
            return true;
        }
        if (partial instanceof ReadablePartial == false) {
            return false;
        }
        ReadablePartial other = (ReadablePartial) partial;
        if (size() != other.size()) {
            return false;
        }
        for (int i = 0, isize = size(); i < isize; i++) {
            if (getValue(i) != other.getValue(i) || getFieldType(i) != other.getFieldType(i)) {
                return false;
            }
        }
        return (getChronology() == other.getChronology());
    }

    /**
     * Gets a hash code for the ReadablePartial that is compatible with the 
     * equals method.
     *
     * @return a suitable hash code
     */
    public int hashCode() {
        int total = 157;
        for (int i = 0, isize = size(); i < isize; i++) {
            total = 23 * total + getValue(i);
            total = 23 * total + getFieldType(i).hashCode();
        }
        total += getChronology().hashCode();
        return total;
    }

}
