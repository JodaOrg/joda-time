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

/**
 * ISODurationFormat provides factory methods for the ISO8601 standard.
 * <p>
 * ISODurationFormat is thread-safe and immutable, and the formatters it
 * returns are as well.
 *
 * @author Brian S O'Neill
 * @see DurationFormat
 * @see DurationFormatterBuilder
 */
public class ISODurationFormat {
    private static final ISODurationFormat INSTANCE = new ISODurationFormat();

    /**
     * Returns a singleton instance of ISODurationFormat.
     */
    public static ISODurationFormat getInstance() {
        return INSTANCE;
    }

    private transient DurationFormatter
        iStandard,
        iAlternate,
        iAlternateExtended,
        iAlternateWithWeeks,
        iAlternateExtendedWihWeeks;

    private ISODurationFormat() {
    }

    /**
     * PyYmMwWdDThHmMsS
     */
    public DurationFormatter standard() {
        if (iStandard == null) {
            iStandard = new DurationFormatterBuilder()
                .appendLiteral("P")
                .printZeroIfSupported()
                .appendYears()
                .appendSuffix("Y")
                .appendMonths()
                .appendSuffix("M")
                .appendWeeks()
                .appendSuffix("W")
                .appendDays()
                .appendSuffix("D")
                .appendSeparator("T")
                .appendHours()
                .appendSuffix("H")
                .appendMinutes()
                .appendSuffix("M")
                .appendSeconds()
                .appendSuffix("S")
                .toFormatter();
        }
        return iStandard;
    }

    /**
     * PyyyymmddThhmmss
     */
    public DurationFormatter alternate() {
        if (iAlternate == null) {
            iAlternate = new DurationFormatterBuilder()
                .appendLiteral("P")
                .printZeroAlways()
                .minimumPrintedDigits(4)
                .appendYears()
                .minimumPrintedDigits(2)
                .appendMonths()
                .appendDays()
                .appendSeparator("T")
                .appendHours()
                .appendMinutes()
                .appendSeconds()
                .toFormatter();
        }
        return iAlternate;
    }

    /**
     * Pyyyy-mm-ddThh:mm:ss
     */
    public DurationFormatter alternateExtended() {
        if (iAlternateExtended == null) {
            iAlternateExtended = new DurationFormatterBuilder()
                .appendLiteral("P")
                .printZeroAlways()
                .minimumPrintedDigits(4)
                .appendYears()
                .appendSeparator("-")
                .minimumPrintedDigits(2)
                .appendMonths()
                .appendSeparator("-")
                .appendDays()
                .appendSeparator("T")
                .appendHours()
                .appendSeparator(":")
                .appendMinutes()
                .appendSeparator(":")
                .appendSeconds()
                .toFormatter();
        }
        return iAlternateExtended;
    }

    /**
     * PyyyyWwwddThhmmss
     */
    public DurationFormatter alternateWithWeeks() {
        if (iAlternateWithWeeks == null) {
            iAlternateWithWeeks = new DurationFormatterBuilder()
                .appendLiteral("P")
                .printZeroAlways()
                .minimumPrintedDigits(4)
                .appendYears()
                .minimumPrintedDigits(2)
                .appendPrefix("W")
                .appendWeeks()
                .appendDays()
                .appendSeparator("T")
                .appendHours()
                .appendMinutes()
                .appendSeconds()
                .toFormatter();
        }
        return iAlternateWithWeeks;
    }

    /**
     * Pyyyy-Www-ddThh:mm:ss
     */
    public DurationFormatter alternateExtendedWithWeeks() {
        if (iAlternateExtendedWihWeeks == null) {
            iAlternateExtendedWihWeeks = new DurationFormatterBuilder()
                .appendLiteral("P")
                .printZeroAlways()
                .minimumPrintedDigits(4)
                .appendYears()
                .appendSeparator("-")
                .minimumPrintedDigits(2)
                .appendPrefix("W")
                .appendWeeks()
                .appendSeparator("-")
                .appendDays()
                .appendSeparator("T")
                .appendHours()
                .appendSeparator(":")
                .appendMinutes()
                .appendSeparator(":")
                .appendSeconds()
                .toFormatter();
        }
        return iAlternateExtendedWihWeeks;
    }

}
