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
 * Defines an instant in the datetime continuum.
 * This interface expresses the datetime as milliseconds from 1970-01-01T00:00:00Z.
 * <p>
 * The implementation of this interface may be mutable or immutable.
 * This interface only gives access to retrieve data, never to change it.
 * <p>
 * Methods in your application should be defined using <code>ReadableInstant</code>
 * as a parameter if the method only wants to read the instant without needing to know
 * the specific datetime fields.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public interface ReadableInstant extends Comparable {

    /**
     * Get the value as the number of milliseconds since
     * the epoch, 1970-01-01T00:00:00Z.
     *
     * @return the value as milliseconds
     */
    long getMillis();

    /**
     * Gets the chronology of the instant.
     * <p>
     * The {@link Chronology} provides conversion from the millisecond
     * value to meaningful fields in a particular calendar system.
     * 
     * @return the Chronology, never null
     */
    Chronology getChronology();

    /**
     * Gets the time zone of the instant from the chronology.
     * 
     * @return the DateTimeZone that the instant is using, never null
     */
    DateTimeZone getZone();

    /**
     * Gets a value of the specified field, which is obtained from a Chronology.
     * <p>
     * Field instances are generally obtained from a {@link Chronology}.
     * However, an application can create its own fields to manipulate the
     * datetime millis in new ways.
     *
     * @param field  a field, usually obtained from a Chronology
     * @return the value of that field for the millis set in the implementation
     * @throws IllegalArgumentException if the field is null
     */
    int get(DateTimeField field);

    //-----------------------------------------------------------------------
    /**
     * Get the value as a simple immutable <code>Instant</code> object.
     * <p>
     * This can be useful if you don't trust the implementation
     * of the interface to be well-behaved, or to get a guaranteed
     * immutable object.
     *
     * @return the value as an <code>Instant</code> object
     */
    Instant toInstant();

    //-----------------------------------------------------------------------
    /**
     * Compares this object with the specified object for ascending
     * millisecond instant order. This ordering is inconsistent with
     * equals, as it ignores the Chronology.
     * <p>
     * All ReadableInstant instances are accepted.
     *
     * @param readableInstant  a readable instant to check against
     * @return negative value if this is less, 0 if equal, or positive value if greater
     * @throws NullPointerException if the object is null
     * @throws ClassCastException if the object type is not supported
     */
    int compareTo(Object readableInstant);

    //-----------------------------------------------------------------------
    /**
     * Is this instant equal to the instant passed in
     * comparing solely by millisecond.
     *
     * @param instant  an instant to check against, null means now
     * @return true if the instant is equal to the instant passed in
     */
    boolean isEqual(ReadableInstant instant);

    /**
     * Is this instant after the instant passed in
     * comparing solely by millisecond.
     *
     * @param instant  an instant to check against, null means now
     * @return true if the instant is after the instant passed in
     */
    boolean isAfter(ReadableInstant instant);

    /**
     * Is this instant before the instant passed in
     * comparing solely by millisecond.
     *
     * @param instant  an instant to check against, null means now
     * @return true if the instant is before the instant passed in
     */
    boolean isBefore(ReadableInstant instant);

    //-----------------------------------------------------------------------
    /**
     * Compares this object with the specified object for equality based
     * on the millisecond instant and the Chronology. All ReadableInstant
     * instances are accepted.
     * <p>
     * To compare two instants for absolute time (ie. UTC milliseconds 
     * ignoring the chronology), use {@link #isEqual(ReadableInstant)} or
     * {@link #compareTo(Object)}.
     *
     * @param readableInstant  a readable instant to check against
     * @return true if millisecond and chronology are equal, false if
     *  not or the instant is null or of an incorrect type
     */
    boolean equals(Object readableInstant);

    /**
     * Gets a hash code for the instant that is compatible with the 
     * equals method.
     * <p>
     * The formula used must be as follows:
     * <pre>
     * ((int) (getMillis() ^ (getMillis() >>> 32))) +
     * (getChronology().hashCode())
     * </pre>
     *
     * @return a hash code as defined above
     */
    int hashCode();

    //-----------------------------------------------------------------------
    /**
     * Get the value as a String in a recognisable ISO8601 format.
     * <p>
     * The string output is in ISO8601 format to enable the String
     * constructor to correctly parse it.
     *
     * @return the value as an ISO8601 string
     */
    String toString();

}
