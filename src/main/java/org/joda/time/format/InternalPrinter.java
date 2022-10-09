/*
 *  Copyright 2001-2014 Stephen Colebourne
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
import java.util.Locale;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadablePartial;

/**
 * Internal interface for creating textual representations of datetimes.
 * <p>
 * This has been separated from {@link DateTimePrinter} to avoid code duplication.
 *
 * @author Stephen Colebourne
 * @since 2.4
 */
interface InternalPrinter {

    /**
     * Returns the expected maximum number of characters produced.
     * The actual amount should rarely exceed this estimate.
     * 
     * @return the estimated length
     */
    int estimatePrintedLength();

    //-----------------------------------------------------------------------
    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * using the given Chronology.
     *
     * @param appendable  formatted instant is appended to, not null
     * @param instant  millis since 1970-01-01T00:00:00Z
     * @param chrono  the chronology to use, not null
     * @param displayOffset  if a time zone offset is printed, force it to use
     * this millisecond value
     * @param displayZone  the time zone to use, null means local time
     * @param locale  the locale to use, null means default locale
     * @throws IOException if an IO error occurs
     */
    void printTo(Appendable appendable, long instant, Chronology chrono,
                 int displayOffset, DateTimeZone displayZone, Locale locale) throws IOException;

    /**
     * Prints a ReadablePartial.
     *
     * @param appendable  formatted instant is appended to, not null
     * @param partial  partial to format, not null
     * @param locale  the locale to use, null means default locale
     * @throws IOException if an IO error occurs
     */
    void printTo(Appendable appendable, ReadablePartial partial, Locale locale) throws IOException;

}
