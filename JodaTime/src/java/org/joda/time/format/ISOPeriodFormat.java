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
 * ISOPeriodFormat provides factory methods for the ISO8601 standard.
 * <p>
 * ISOPeriodFormat is thread-safe and immutable, and the formatters it
 * returns are as well.
 *
 * @author Brian S O'Neill
 * @see PeriodFormat
 * @see PeriodFormatterBuilder
 */
public class ISOPeriodFormat {
    private static final ISOPeriodFormat INSTANCE = new ISOPeriodFormat();

    /**
     * Returns a singleton instance of ISOPeriodFormat.
     */
    public static ISOPeriodFormat getInstance() {
        return INSTANCE;
    }

    private transient PeriodFormatter
        iStandard,
        iAlternate,
        iAlternateExtended,
        iAlternateWithWeeks,
        iAlternateExtendedWihWeeks;

    private ISOPeriodFormat() {
    }

    /**
     * The standard ISO format - PyYmMwWdDThHmMsS.
     * Milliseconds are not output.
     * Note that the ISO8601 standard actually indicates weeks should not
     * be shown if any other field is present and vice versa.
     */
    public PeriodFormatter standard() {
        if (iStandard == null) {
            iStandard = new PeriodFormatterBuilder()
                .appendLiteral("P")
                .appendYears()
                .appendSuffix("Y")
                .appendMonths()
                .appendSuffix("M")
                .appendWeeks()
                .appendSuffix("W")
                .appendDays()
                .appendSuffix("D")
                .appendSeparatorIfFieldsAfter("T")
                .appendHours()
                .appendSuffix("H")
                .appendMinutes()
                .appendSuffix("M")
                .appendSecondsWithOptionalMillis()
                .appendSuffix("S")
                .toFormatter();
        }
        return iStandard;
    }

    /**
     * PyyyymmddThhmmss
     */
    public PeriodFormatter alternate() {
        if (iAlternate == null) {
            iAlternate = new PeriodFormatterBuilder()
                .appendLiteral("P")
                .printZeroAlways()
                .minimumPrintedDigits(4)
                .appendYears()
                .minimumPrintedDigits(2)
                .appendMonths()
                .appendDays()
                .appendSeparatorIfFieldsAfter("T")
                .appendHours()
                .appendMinutes()
                .appendSecondsWithOptionalMillis()
                .toFormatter();
        }
        return iAlternate;
    }

    /**
     * Pyyyy-mm-ddThh:mm:ss
     */
    public PeriodFormatter alternateExtended() {
        if (iAlternateExtended == null) {
            iAlternateExtended = new PeriodFormatterBuilder()
                .appendLiteral("P")
                .printZeroAlways()
                .minimumPrintedDigits(4)
                .appendYears()
                .appendSeparator("-")
                .minimumPrintedDigits(2)
                .appendMonths()
                .appendSeparator("-")
                .appendDays()
                .appendSeparatorIfFieldsAfter("T")
                .appendHours()
                .appendSeparator(":")
                .appendMinutes()
                .appendSeparator(":")
                .appendSecondsWithOptionalMillis()
                .toFormatter();
        }
        return iAlternateExtended;
    }

    /**
     * PyyyyWwwddThhmmss
     */
    public PeriodFormatter alternateWithWeeks() {
        if (iAlternateWithWeeks == null) {
            iAlternateWithWeeks = new PeriodFormatterBuilder()
                .appendLiteral("P")
                .printZeroAlways()
                .minimumPrintedDigits(4)
                .appendYears()
                .minimumPrintedDigits(2)
                .appendPrefix("W")
                .appendWeeks()
                .appendDays()
                .appendSeparatorIfFieldsAfter("T")
                .appendHours()
                .appendMinutes()
                .appendSecondsWithOptionalMillis()
                .toFormatter();
        }
        return iAlternateWithWeeks;
    }

    /**
     * Pyyyy-Www-ddThh:mm:ss
     */
    public PeriodFormatter alternateExtendedWithWeeks() {
        if (iAlternateExtendedWihWeeks == null) {
            iAlternateExtendedWihWeeks = new PeriodFormatterBuilder()
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
                .appendSeparatorIfFieldsAfter("T")
                .appendHours()
                .appendSeparator(":")
                .appendMinutes()
                .appendSeparator(":")
                .appendSecondsWithOptionalMillis()
                .toFormatter();
        }
        return iAlternateExtendedWihWeeks;
    }

}
