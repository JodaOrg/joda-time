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

/**
 * ISOPeriodFormat provides factory methods for the ISO8601 standard.
 * <p>
 * ISOPeriodFormat is thread-safe and immutable, and the formatters it
 * returns are as well.
 *
 * @author Brian S O'Neill
 * @since 1.0
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
     * 
     * @return the formatter
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
     * The alternate ISO format, PyyyymmddThhmmss, which excludes weeks.
     * <p>
     * Even if weeks are present in the period, they are not output.
     * Fractional seconds (milliseconds) will appear if required.
     * 
     * @return the formatter
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
     * The alternate ISO format, Pyyyy-mm-ddThh:mm:ss, which excludes weeks.
     * <p>
     * Even if weeks are present in the period, they are not output.
     * Fractional seconds (milliseconds) will appear if required.
     * 
     * @return the formatter
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
     * The alternate ISO format, PyyyyWwwddThhmmss, which excludes months.
     * <p>
     * Even if months are present in the period, they are not output.
     * Fractional seconds (milliseconds) will appear if required.
     * 
     * @return the formatter
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
     * The alternate ISO format, Pyyyy-Www-ddThh:mm:ss, which excludes months.
     * <p>
     * Even if months are present in the period, they are not output.
     * Fractional seconds (milliseconds) will appear if required.
     * 
     * @return the formatter
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
