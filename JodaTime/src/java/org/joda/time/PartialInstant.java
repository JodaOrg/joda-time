/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-03 Stephen Colebourne.
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
 * Defines an instant that does not support every datetime field.
 * A PartialInstant supports a range of fields, specified by lower and
 * upper bounding fields.
 *
 * @author Brian S O'Neill
 */
public interface PartialInstant extends ReadableInstant {

    /**
     * Get the value as the number of milliseconds since the epoch,
     * 1970-01-01T00:00:00Z, with unsupported field values reset.
     *
     * @return the value as milliseconds
     */
    long getMillis();

    /**
     * Get the value as the number of milliseconds since the epoch,
     * 1970-01-01T00:00:00Z, with unsupported field values and time zone filled
     * in by the given base instant.
     *
     * @param base  source of missing fields
     * @return the value as milliseconds
     */
    long getMillis(ReadableInstant base);

    /**
     * Get the value as the number of milliseconds since the epoch,
     * 1970-01-01T00:00:00Z, with unsupported field values filled in by the
     * given base instant.
     *
     * @param base  source of missing fields
     * @param zone  override the base time zone, null implies override with no
     * time zone
     * @return the value as milliseconds
     */
    long getMillis(ReadableInstant base, DateTimeZone zone);

    /**
     * Gets the chronology of the instant, null if not applicable. The time
     * zone of the chronology is either null or UTC.
     * <p>
     * The {@link Chronology} provides conversion from the millisecond
     * value to meaningful fields in a particular calendar system.
     * 
     * @return the Chronology
     */
    Chronology getChronology();

    /**
     * Returns the lower limiting field, where the lower limit field itself is
     * supported. In other words, for the range described by the lower and
     * upper limits, the lower limit is inclusive.
     *
     * @return lower limit or null if none
     */
    DateTimeField getLowerLimit();

    /**
     * Returns the upper limiting field, where the upper limit field itself is
     * not supported. In other words, for the range described by the lower and
     * upper limits, the upper limit is exclusive.
     *
     * @return upper limit or null if none
     */
    DateTimeField getUpperLimit();

    /**
     * Returns the given instant, except with lower and upper limits
     * applied. Field values are reset below the lower limit and at or above
     * the upper limit.
     *
     * @param instant milliseconds from 1970-01-01T00:00:00
     * @return the adjusted millisecond instant
     */
    long resetUnsupportedFields(long instant);

    /**
     * Returns the given instant, except with lower and upper limits
     * applied. Field values are reset below the upper limit and at or above
     * the lower limit.
     *
     * @param instant milliseconds from 1970-01-01T00:00:00
     * @return the adjusted millisecond instant
     */
    long resetSupportedFields(long instant);

    /**
     * Compares this object with the specified object for equality based
     * on the millisecond instant, the Chronology, and the limiting fields.
     * <p>
     * To compare two instants for absolute time (ie. UTC milliseconds ignoring
     * the chronology), use {@link #isEqual(ReadableInstant)} or
     * {@link #compareTo(Object)}.
     *
     * @param readableInstant  a readable instant to check against
     * @return true if millisecond and chronology are equal, false if
     *  not or the instant is null or of an incorrect type
     */
    boolean equals(Object readableInstant);

    /**
     * Gets a hash code for the instant that is compatable with the 
     * equals method.
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
