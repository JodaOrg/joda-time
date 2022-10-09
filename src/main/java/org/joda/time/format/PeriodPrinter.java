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
import java.util.Locale;

import org.joda.time.ReadablePeriod;

/**
 * Internal interface for printing textual representations of time periods.
 * <p>
 * Application users will rarely use this class directly. Instead, you
 * will use one of the factory classes to create a {@link PeriodFormatter}.
 * <p>
 * The factory classes are:<br />
 * - {@link PeriodFormatterBuilder}<br />
 * - {@link PeriodFormat}<br />
 * - {@link ISOPeriodFormat}<br />
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 * @see PeriodFormatter
 * @see PeriodFormatterBuilder
 * @see PeriodFormat
 */
public interface PeriodPrinter {

    /**
     * Returns the exact number of characters produced for the given period.
     * 
     * @param period  the period to use
     * @param locale  the locale to use
     * @return the estimated length
     */
    int calculatePrintedLength(ReadablePeriod period, Locale locale);

    /**
     * Returns the amount of fields from the given period that this printer
     * will print.
     * 
     * @param period  the period to use
     * @param stopAt stop counting at this value, enter a number &ge; 256 to count all
     * @param locale  the locale to use
     * @return amount of fields printed
     */
    int countFieldsToPrint(ReadablePeriod period, int stopAt, Locale locale);

    //-----------------------------------------------------------------------
    /**
     * Prints a ReadablePeriod to a StringBuffer.
     *
     * @param buf  the formatted period is appended to this buffer
     * @param period  the period to format
     * @param locale  the locale to use
     */
    void printTo(StringBuffer buf, ReadablePeriod period, Locale locale);

    /**
     * Prints a ReadablePeriod to a Writer.
     *
     * @param out  the formatted period is written out
     * @param period  the period to format
     * @param locale  the locale to use
     * @throws IOException if an IO error occurs
     */
    void printTo(Writer out, ReadablePeriod period, Locale locale) throws IOException;

}
