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

/**
 * Defines a partial time that does not support every datetime field, and is
 * thus a local time.
 * <p>
 * A <code>ReadablePartial</code> supports a subset of those fields on the chronology.
 * It cannot be compared to a <code>ReadableInstant</code>, as it does not fully
 * specify an instant in time. The time it does specify is a local time, and does
 * not include a time zone.
 * <p>
 * A <code>ReadablePartial</code> can be converted to a <code>ReadableInstant</code>
 * using one of the <code>resolve</code> methods. These work by providing a full base
 * instant that can be used to 'fill in the gaps' and specify a time zone.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public interface ReadablePartial {

    /**
     * Gets the number of fields that this partial supports.
     *
     * @return the number of fields supported
     */
    int size();

    /**
     * Gets the field type at the specified index.
     *
     * @param index  the index to retrieve
     * @return the field at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    DateTimeFieldType getFieldType(int index);

    /**
     * Gets the field at the specified index.
     *
     * @param index  the index to retrieve
     * @return the field at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    DateTimeField getField(int index);

    /**
     * Gets the value at the specified index.
     *
     * @param index  the index to retrieve
     * @return the value of the field at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    int getValue(int index);

    /**
     * Gets the chronology of the partial which is never null.
     * <p>
     * The {@link Chronology} is the calculation engine behind the partial and
     * provides conversion and validation of the fields in a particular calendar system.
     * 
     * @return the chronology, never null
     */
    Chronology getChronology();

    /**
     * Gets the value of one of the fields.
     * <p>
     * The field type specified must be one of those that is supported by the partial.
     *
     * @param field  a DateTimeFieldType instance that is supported by this partial
     * @return the value of that field
     * @throws IllegalArgumentException if the field is null or not supported
     */
    int get(DateTimeFieldType field);

    /**
     * Checks whether the field type specified is supported by this partial.
     *
     * @param field  the field to check, may be null which returns false
     * @return true if the field is supported
     */
    boolean isSupported(DateTimeFieldType field);

    /**
     * Converts this partial to a full datetime using the specified time zone and
     * filing in any gaps using the current datetime.
     * <p>
     * This method obtains the current datetime, creates a chronology from that
     * on this instance plus the time zone specified, and then sets the fields
     * from this instant on top.
     * <p>
     * For example, if this partial represents a time, then the result of this
     * method will be the datetime from the specified base instant plus the
     * time from this partial.
     *
     * @param zone  the zone to use, null means default
     * @return the combined datetime
     */
    DateTime toDateTime(DateTimeZone zone);

    /**
     * Converts this partial to a full datetime by resolving it against another
     * datetime.
     * <p>
     * This method takes the specified datetime and sets the fields from this
     * instant on top. The chronology from the base instant is used.
     * <p>
     * For example, if this partial represents a time, then the result of this
     * method will be the datetime from the specified base instant plus the
     * time from this partial.
     *
     * @param baseInstant  the instant that provides the missing fields, null means now
     * @return the combined datetime
     */
    DateTime toDateTime(ReadableInstant baseInstant);

    //-----------------------------------------------------------------------
    /**
     * Compares this partial with the specified object for equality based
     * on the supported fields, chronology and values.
     * <p>
     * Two instances of ReadablePartial are equal if they have the same
     * chronology, same field types (in same order) and same values.
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
     *      total = 23 * total + fieldTypes[i].hashCode();
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
