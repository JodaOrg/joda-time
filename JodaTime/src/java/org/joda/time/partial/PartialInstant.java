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
import org.joda.time.ReadableInstant;

/**
 * Defines an instant that does not support every datetime field.
 * <p>
 * A PartialInstant supports a set of fields and cannot be compared to a
 * full complete instant. Methods are provided to resolve the partial instant
 * into a full instant by 'filling in the gaps'.
 *
 * @author Stephen Colebourne
 */
public interface PartialInstant {

    /**
     * Gets an array of the fields that this partial instant supports.
     * <p>
     * The fields are returned largest to smallest, for example Hour, Minute, Second.
     *
     * @return the fields supported, largest to smallest
     */
    DateTimeField[] getSupportedFields();

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
     * The field specified must be one of those that is supported by the partial instant.
     *
     * @param field  a DateTimeField instance that is supported by this partial
     * @return the value of that field
     * @throws IllegalArgumentException if the field is null or not supported
     */
    int get(DateTimeField field);

    /**
     * Checks whether the field specified is supported by this partial instant.
     *
     * @param field  the field to check, may be null which returns false
     * @return true if the field is supported
     */
    boolean isSupported(DateTimeField field);

    /**
     * Resolves this partial against another complete instant to create a new
     * full instant specifying values as milliseconds since 1970-01-01T00:00:00Z.
     * <p>
     * For example, if this partial represents a time, then the result of this method
     * will be the date from the specified base plus the time from this instant.
     *
     * @param baseMillis  source of missing fields
     * @return the combined instant in milliseconds
     */
    long resolve(long baseMillis);

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
    DateTime resolveDateTime(ReadableInstant base);

//    /**
//     * Compares this object with the specified object for equality based
//     * on the millisecond instant, the Chronology, and the limiting fields.
//     * <p>
//     * To compare two instants for absolute time (ie. UTC milliseconds ignoring
//     * the chronology), use {@link #isEqual(ReadableInstant)} or
//     * {@link #compareTo(Object)}.
//     *
//     * @param readableInstant  a readable instant to check against
//     * @return true if millisecond and chronology are equal, false if
//     *  not or the instant is null or of an incorrect type
//     */
//    boolean equals(Object readableInstant);
//
//    /**
//     * Gets a hash code for the instant that is compatable with the 
//     * equals method.
//     *
//     * @return a suitable hash code
//     */
//    int hashCode();

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
