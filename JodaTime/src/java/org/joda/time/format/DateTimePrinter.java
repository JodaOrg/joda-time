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
package org.joda.time.format;

import java.io.IOException;
import java.io.Writer;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableInstant;

/**
 * Converts datetimes into a sequence of human-readable characters.
 *
 * @author Brian S O'Neill
 * @see DateTimeFormatter
 * @see DateTimeFormatterBuilder
 * @see DateTimeFormat
 */
public interface DateTimePrinter {
    
    /**
     * Returns the Chronology being used by the printer, or null if none.
     */
    Chronology getChronology();

    /**
     * Returns the expected maximum number of characters produced. The actual
     * amount should rarely exceed this estimate.
     */
    int estimatePrintedLength();

    /**
     * Prints a ReadableInstant, attempting to use the DateTimeZone supplied by
     * the instant.
     *
     * @param buf formatted instant is appended to this buffer
     * @param instant instant to format
     */
    void printTo(StringBuffer buf, ReadableInstant instant);

    /**
     * Prints a ReadableInstant, attempting to use the DateTimeZone supplied by
     * the instant.
     *
     * @param out formatted instant is written out
     * @param instant instant to format
     */
    void printTo(Writer out, ReadableInstant instant) throws IOException;

    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * using the formatter's DateTimeZone.
     *
     * @param buf formatted instant is appended to this buffer
     * @param millisUTC millis since 1970-01-01T00:00:00Z
     */
    void printTo(StringBuffer buf, long millisUTC);

    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * using the formatter's DateTimeZone.
     *
     * @param out formatted instant is written out
     * @param millisUTC millis since 1970-01-01T00:00:00Z
     */
    void printTo(Writer out, long millisUTC) throws IOException;

    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * attempting to use the given DateTimeZone.
     *
     * @param buf formatted instant is appended to this buffer
     * @param millisUTC millis since 1970-01-01T00:00:00Z
     * @param zone DateTimeZone to use, overriding the formatter's own zone if
     * not null
     */
    void printTo(StringBuffer buf, long millisUTC, DateTimeZone zone);

    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * attempting to use the given DateTimeZone.
     *
     * @param out formatted instant is written out
     * @param millisUTC millis since 1970-01-01T00:00:00Z
     * @param zone DateTimeZone to use, overriding the formatter's own zone if
     * not null
     */
    void printTo(Writer out, long millisUTC, DateTimeZone zone) throws IOException;

    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * attempting to use the given DateTimeZone.
     *
     * @param buf formatted instant is appended to this buffer
     * @param millisUTC millis since 1970-01-01T00:00:00Z, used by time zone
     * printers
     * @param zone DateTimeZone to use, overriding the formatter's own zone if
     * not null
     * @param millisLocal pre-calculated millis since 1970-01-01T00:00:00,
     * local time
     */
    void printTo(StringBuffer buf, long millisUTC,
                 DateTimeZone zone, long millisLocal);

    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * attempting to use the given DateTimeZone.
     *
     * @param out formatted instant is written out
     * @param millisUTC millis since 1970-01-01T00:00:00Z, used by time zone
     * printers
     * @param zone DateTimeZone to use, overriding the formatter's own zone if
     * not null
     * @param millisLocal pre-calculated millis since 1970-01-01T00:00:00,
     * local time
     */
    void printTo(Writer out, long millisUTC,
                 DateTimeZone zone, long millisLocal) throws IOException;

    /**
     * Prints a ReadableInstant to a new String, attempting to use the
     * DateTimeZone supplied by the instant.
     *
     * @param instant instant to format
     * @return the printed result
     */
    String print(ReadableInstant instant);

    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * using the formatter's DateTimeZone.
     *
     * @param millisUTC millis since 1970-01-01T00:00:00Z
     * @return the printed result
     */
    String print(long millisUTC);

    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * attempting to use the given DateTimeZone.
     *
     * @param millisUTC millis since 1970-01-01T00:00:00Z
     * @param zone DateTimeZone to use, overriding the formatter's own zone if
     * not null
     * @return the printed result
     */
    String print(long millisUTC, DateTimeZone zone);

    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * attempting to use the given DateTimeZone.
     *
     * @param millisUTC millis since 1970-01-01T00:00:00Z
     * @param zone DateTimeZone to use, overriding the formatter's own zone if
     * not null
     * @param millisLocal pre-calculated millis since 1970-01-01T00:00:00,
     * local time
     * @return the printed result
     */
    String print(long millisUTC, DateTimeZone zone, long millisLocal);
}
