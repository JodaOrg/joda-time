/*
 *  Copyright 2001-2005 Stephen Colebourne
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
