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

import org.joda.time.ReadableDuration;

/**
 * Defines an interface for creating textual representations of durations.
 *
 * @author Brian S O'Neill
 * @see DurationFormatter
 * @see DurationFormatterBuilder
 * @see DurationFormat
 * @since 1.0
 */
public interface DurationPrinter {

    /**
     * Returns the amount of fields from the given duration that this printer
     * will print.
     * 
     * @param duration duration to use
     * @return amount of fields printed
     */
    int countFieldsToPrint(ReadableDuration duration);

    /**
     * Returns the amount of fields from the given duration that this printer
     * will print.
     * 
     * @param duration duration to use
     * @param stopAt stop counting at this value
     * @return amount of fields printed
     */
    int countFieldsToPrint(ReadableDuration duration, int stopAt);

    /**
     * Returns the exact number of characters produced for the given duration.
     * 
     * @param duration duration to use
     * @return the estimated length
     */
    int calculatePrintedLength(ReadableDuration duration);

    //-----------------------------------------------------------------------
    /**
     * Prints a ReadableDuration to a StringBuffer.
     *
     * @param buf  the formatted duration is appended to this buffer
     * @param duration  duration to format
     */
    void printTo(StringBuffer buf, ReadableDuration duration);

    /**
     * Prints a ReadableDuration to a Writer.
     *
     * @param out  the formatted duration is written out
     * @param duration  duration to format
     */
    void printTo(Writer out, ReadableDuration duration) throws IOException;

    /**
     * Prints a ReadableDuration to a new String.
     *
     * @param duration  duration to format
     * @return the printed result
     */
    String print(ReadableDuration duration);

}
