/*
 *  Copyright 2001-2009 Stephen Colebourne
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
 * <p>
 * The {@code compareTo} method is no longer defined in this class in version 2.0.
 * Instead, the definition is simply inherited from the {@code Comparable} interface.
 * This approach is necessary to preserve binary compatibility.
 * The definition of the comparison is ascending order by millisecond instant.
 * Implementors are recommended to extend {@code AbstractInstant} instead of this interface.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public interface ReadableInstant extends Comparable<ReadableInstant> {

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
     * Get the value of one of the fields of a datetime.
     * <p>
     * This method uses the chronology of the instant to obtain the value.
     *
     * @param type  a field type, usually obtained from DateTimeFieldType, not null
     * @return the value of that field
     * @throws IllegalArgumentException if the field type is null
     */
    int get(DateTimeFieldType type);

    /**
     * Checks whether the field type specified is supported by this implementation.
     *
     * @param field  the field type to check, may be null which returns false
     * @return true if the field is supported
     */
    boolean isSupported(DateTimeFieldType field);

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
    // Method is no longer defined here as that would break generic backwards compatibility
//    /**
//     * Compares this object with the specified object for ascending
//     * millisecond instant order. This ordering is inconsistent with
//     * equals, as it ignores the Chronology.
//     * <p>
//     * All ReadableInstant instances are accepted.
//     *
//     * @param readableInstant  a readable instant to check against
//     * @return negative value if this is less, 0 if equal, or positive value if greater
//     * @throws NullPointerException if the object is null
//     * @throws ClassCastException if the object type is not supported
//     */
//    int compareTo(ReadableInstant readableInstant);

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
