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

import org.joda.time.ReadablePeriod;

/**
 * Defines an interface for creating textual representations of time periods.
 *
 * @author Brian S O'Neill
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
     * @return the estimated length
     */
    int calculatePrintedLength(ReadablePeriod period);

    /**
     * Returns the amount of fields from the given period that this printer
     * will print.
     * 
     * @param period  the period to use
     * @return amount of fields printed
     */
    int countFieldsToPrint(ReadablePeriod period);

    /**
     * Returns the amount of fields from the given period that this printer
     * will print.
     * 
     * @param period  the period to use
     * @param stopAt stop counting at this value
     * @return amount of fields printed
     */
    int countFieldsToPrint(ReadablePeriod period, int stopAt);

    //-----------------------------------------------------------------------
    /**
     * Prints a ReadablePeriod to a StringBuffer.
     *
     * @param buf  the formatted period is appended to this buffer
     * @param period  the period to format
     */
    void printTo(StringBuffer buf, ReadablePeriod period);

    /**
     * Prints a ReadablePeriod to a Writer.
     *
     * @param out  the formatted period is written out
     * @param period  the period to format
     */
    void printTo(Writer out, ReadablePeriod period) throws IOException;

    /**
     * Prints a ReadablePeriod to a new String.
     *
     * @param period  the period to format
     * @return the printed result
     */
    String print(ReadablePeriod period);

}
