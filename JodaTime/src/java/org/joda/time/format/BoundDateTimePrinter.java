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
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;

/**
 * Defines an interface, bound to a single chronology, for creating textual
 * representations of datetimes.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public interface BoundDateTimePrinter {
    
    /**
     * Returns another bound printer that uses the specified chronology.
     * <p>
     * It is the callers resposibility to ensure that the printer is then only
     * used with instances of the correct chronology.
     * 
     * @param chrono  the chronology to use, null means ISO
     * @return the chronology specific printer
     */
    BoundDateTimePrinter bindPrinter(Chronology chrono);

    //-----------------------------------------------------------------------
    /**
     * Prints a ReadableInstant, using the chronology of this printer.
     *
     * @param buf  formatted instant is appended to this buffer
     * @param instant  instant to format, null means now
     */
    void printTo(StringBuffer buf, ReadableInstant instant);

    /**
     * Prints a ReadableInstant, using the chronology of this printer.
     *
     * @param out  formatted instant is written out
     * @param instant  instant to format, null means now
     */
    void printTo(Writer out, ReadableInstant instant) throws IOException;

    //-----------------------------------------------------------------------
    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * using the chronology of this printer.
     *
     * @param buf  formatted instant is appended to this buffer
     * @param instant  millis since 1970-01-01T00:00:00Z
     */
    void printTo(StringBuffer buf, long instant);

    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * using the chronology of this printer.
     *
     * @param out  formatted instant is written out
     * @param instant  millis since 1970-01-01T00:00:00Z
     */
    void printTo(Writer out, long instant) throws IOException;

    //-----------------------------------------------------------------------
    /**
     * Prints a ReadablePartial using the chronology of this printer.
     *
     * @param buf  formatted partial is appended to this buffer
     * @param partial  partial to format
     */
    void printTo(StringBuffer buf, ReadablePartial partial);

    /**
     * Prints a ReadablePartial using the chronology of this printer.
     *
     * @param out  formatted partial is written out
     * @param partial  partial to format
     */
    void printTo(Writer out, ReadablePartial partial) throws IOException;

    //-----------------------------------------------------------------------
    /**
     * Prints a ReadableInstant to a new String, using the chronology of this printer.
     *
     * @param instant  instant to format, null means now
     * @return the printed result
     */
    String print(ReadableInstant instant);

    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * using the chronology of this printer.
     *
     * @param instant  millis since 1970-01-01T00:00:00Z
     * @return the printed result
     */
    String print(long instant);

    /**
     * Prints a ReadablePartial to a new String using the chronology of this printer.
     *
     * @param partial  partial to format
     * @return the printed result
     */
    String print(ReadablePartial partial);

}
