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

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadWritableInstant;
import org.joda.time.ReadableInstant;

/**
 * Defines an partial time that does not support every datetime field.
 * <p>
 * A <code>ReadablePartial</code> supports a set of fields which be be a
 * subset of those on the chronology.
 * A <code>ReadablePartial</code> cannot be compared to a <code>ReadableInstant</code>.
 * <p>
 * A <code>ReadablePartial</code> can be converted to a <code>ReadableInstant</code>
 * using one of the <code>resolve</code> methods. These work by providing a full base
 * instant that can be used to 'fill in the gaps'.
 *
 * @author Stephen Colebourne
 */
public interface ReadablePartial {

    /**
     * Gets the number of fields that this partial supports.
     *
     * @return the number of fields supported
     */
    int getFieldSize();

    /**
     * Gets the field at the specified index.
     *
     * @param index  the index to retrieve
     * @return the field at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    DateTimeField getField(int index);

    /**
     * Gets an array of the fields that this partial supports.
     * <p>
     * The fields are returned largest to smallest, for example Hour, Minute, Second.
     *
     * @return the fields supported (cloned), largest to smallest
     */
    DateTimeField[] getFields();

    /**
     * Gets the value at the specified index.
     *
     * @param index  the index to retrieve
     * @return the value of the field at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    int getValue(int index);

    /**
     * Gets an array of the value of each of the fields that this partial supports.
     * <p>
     * The fields are returned largest to smallest, for example Hour, Minute, Second.
     * Each value corresponds to the same array index as <code>getFields()</code>
     *
     * @return the current values of each field (cloned), largest to smallest
     */
    int[] getValues();

    /**
     * Gets the chronology of the partial which is never null.
     * <p>
     * The {@link Chronology} is the calculation engine behind the partial and
     * provides conversion and validation of the fields in a particular calendar system.
     * 
     * @return the chronology
     */
    Chronology getChronology();

    /**
     * Get the value of one of the fields of a datetime.
     * <p>
     * The field specified must be one of those that is supported by the partial.
     *
     * @param field  a DateTimeField instance that is supported by this partial
     * @return the value of that field
     * @throws IllegalArgumentException if the field is null or not supported
     */
    int get(DateTimeField field);

    /**
     * Checks whether the field specified is supported by this partial.
     *
     * @param field  the field to check, may be null which returns false
     * @return true if the field is supported
     */
    boolean isSupported(DateTimeField field);

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
    long resolve(long baseInstant, DateTimeZone zone);

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
    DateTime resolveDateTime(ReadableInstant baseInstant);

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
    void resolveInto(ReadWritableInstant baseInstant);

    //-----------------------------------------------------------------------
    /**
     * Compares this partial with the specified object for equality based
     * on the supported fields, chronology and values.
     * <p>
     * Two instances of ReadablePartial are equal if they have the same
     * chronology, same fields in same order and same values.
     *
     * @param partial  the object to compare to
     * @return true if equal
     */
    boolean equals(Object partial);

    /**
     * Gets a hash code for the partial that is compatible with the 
     * equals method.
     * <p>
     * The formula used must be:
     * <pre>
     *  int total = 157;
     *  for (int i = 0; i < fields.length; i++) {
     *      total = 23 * total + values[i];
     *      total = 23 * total + fields[i].hashCode();
     *  }
     *  total += chronology.hashCode();
     *  return total;
     * </pre>
     *
     * @return a suitable hash code
     */
    int hashCode();

    //-----------------------------------------------------------------------
    /**
     * Get the value as a String in a recognisable ISO8601 format, only
     * displaying supported fields.
     * <p>
     * The string output is in ISO8601 format to enable the String
     * constructor to correctly parse it.
     *
     * @return the value as an ISO8601 string
     */
    String toString();

}
