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
package org.joda.time.format;

import java.io.IOException;
import java.io.Writer;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;

/**
 * Defines an interface for creating textual representations of datetimes.
 * <p>
 * Instances of this interface are provided by the various builder classes.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @see DateTimeFormatterBuilder
 * @see DateTimeFormat
 * @see ISODateTimeFormat
 * @since 1.0
 */
public interface DateTimePrinter {

    /**
     * Returns the expected maximum number of characters produced. The actual
     * amount should rarely exceed this estimate.
     * 
     * @return the estimated length
     */
    int estimatePrintedLength();

    /**
     * Prints a ReadableInstant, using the chronology supplied by the instant.
     *
     * @param buf  formatted instant is appended to this buffer
     * @param instant  instant to format, null means now
     */
    void printTo(StringBuffer buf, ReadableInstant instant);

    /**
     * Prints a ReadableInstant, using the chronology supplied by the instant.
     *
     * @param out  formatted instant is written out
     * @param instant  instant to format, null means now
     */
    void printTo(Writer out, ReadableInstant instant) throws IOException;

    //-----------------------------------------------------------------------
    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * using ISO chronology in the default DateTimeZone.
     *
     * @param buf  formatted instant is appended to this buffer
     * @param instant  millis since 1970-01-01T00:00:00Z
     */
    void printTo(StringBuffer buf, long instant);

    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * using ISO chronology in the default DateTimeZone.
     *
     * @param out  formatted instant is written out
     * @param instant  millis since 1970-01-01T00:00:00Z
     */
    void printTo(Writer out, long instant) throws IOException;

    //-----------------------------------------------------------------------
    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * using ISO chronology in the given DateTimeZone.
     *
     * @param buf  formatted instant is appended to this buffer
     * @param instant  millis since 1970-01-01T00:00:00Z
     * @param zone  the zone to use, null means default
     */
    void printTo(StringBuffer buf, long instant, DateTimeZone zone);

    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * using ISO chronology in the given DateTimeZone.
     *
     * @param out  formatted instant is written out
     * @param instant  millis since 1970-01-01T00:00:00Z
     * @param zone  the zone to use, null means default
     */
    void printTo(Writer out, long instant, DateTimeZone zone) throws IOException;

    //-----------------------------------------------------------------------
    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * using the given Chronology.
     *
     * @param buf  formatted instant is appended to this buffer
     * @param instant  millis since 1970-01-01T00:00:00Z
     * @param chrono  the chronology to use, null means ISO default
     */
    void printTo(StringBuffer buf, long instant, Chronology chrono);

    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * using the given Chronology.
     *
     * @param out  formatted instant is written out
     * @param instant  millis since 1970-01-01T00:00:00Z
     * @param chrono  the chronology to use, null means ISO default
     */
    void printTo(Writer out, long instant, Chronology chrono) throws IOException;

    //-----------------------------------------------------------------------
    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * using the given Chronology.
     *
     * @param buf  formatted instant is appended to this buffer
     * @param instant  millis since 1970-01-01T00:00:00Z
     * @param chrono  the chronology to use, null means ISO default
     * @param displayOffset  if a time zone offset is printed, force it to use
     * this millisecond value
     * @param displayZone  if a time zone is printed, force it to use this one
     */
    void printTo(StringBuffer buf, long instant, Chronology chrono,
                 int displayOffset, DateTimeZone displayZone);

    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * using the given Chronology.
     *
     * @param out  formatted instant is written out
     * @param instant  millis since 1970-01-01T00:00:00Z
     * @param chrono  the chronology to use, null means ISO default
     * @param displayOffset  if a time zone offset is printed, force it to use
     * this millisecond value
     * @param displayZone  if a time zone is printed, force it to use this one
     */
    void printTo(Writer out, long instant, Chronology chrono,
                 int displayOffset, DateTimeZone displayZone) throws IOException;

    //-----------------------------------------------------------------------
    /**
     * Prints a ReadablePartial.
     *
     * @param buf  formatted partial is appended to this buffer
     * @param partial  partial to format
     */
    void printTo(StringBuffer buf, ReadablePartial partial);

    /**
     * Prints a ReadablePartial.
     *
     * @param out  formatted partial is written out
     * @param partial  partial to format
     */
    void printTo(Writer out, ReadablePartial partial) throws IOException;

    //-----------------------------------------------------------------------
    /**
     * Prints a ReadableInstant to a new String, using the chronology of the instant.
     *
     * @param instant  instant to format, null means now
     * @return the printed result
     */
    String print(ReadableInstant instant);

    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * using ISO chronology in the default zone.
     *
     * @param instant  millis since 1970-01-01T00:00:00Z
     * @return the printed result
     */
    String print(long instant);

    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * using ISO chronology in the given zone.
     *
     * @param instant  millis since 1970-01-01T00:00:00Z
     * @param zone  the zone to use, null means default
     * @return the printed result
     */
    String print(long instant, DateTimeZone zone);

    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * using the given chronology.
     *
     * @param instant  millis since 1970-01-01T00:00:00Z
     * @param chrono  the chronoogy to use
     * @return the printed result
     */
    String print(long instant, Chronology chrono);

    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * using the given chronology.
     *
     * @param instant  millis since 1970-01-01T00:00:00Z
     * @param chrono  the chronoogy to use
     * @param displayOffset  if a time zone offset is printed, force it to use
     * this millisecond value
     * @param displayZone  if a time zone is printed, force it to use this one
     * @return the printed result
     */
    String print(long instant, Chronology chrono, int displayOffset, DateTimeZone displayZone);

    /**
     * Prints a ReadablePartial to a new String.
     *
     * @param partial  partial to format
     * @return the printed result
     */
    String print(ReadablePartial partial);

}
