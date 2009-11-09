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
 * Factory that creates instances of PeriodFormatter.
 * <p>
 * Period formatting is performed by the {@link PeriodFormatter} class.
 * Three classes provide factory methods to create formatters, and this is one.
 * The others are {@link ISOPeriodFormat} and {@link PeriodFormatterBuilder}.
 * <p>
 * PeriodFormat is thread-safe and immutable, and the formatters it returns
 * are as well.
 *
 * @author Brian S O'Neill
 * @since 1.0
 * @see ISOPeriodFormat
 * @see PeriodFormatterBuilder
 */
public class PeriodFormat {

    /** An english words based formatter. */
    private static PeriodFormatter cEnglishWords;

    /**
     * Constructor.
     *
     * @since 1.1 (previously private)
     */
    protected PeriodFormat() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the default PeriodFormatter.
     * <p>
     * This currently returns a word based formatter using English only.
     * Hopefully future release will support localized period formatting.
     * 
     * @return the formatter
     */
    public static PeriodFormatter getDefault() {
        if (cEnglishWords == null) {
            String[] variants = {" ", ",", ",and ", ", and "};
            cEnglishWords = new PeriodFormatterBuilder()
                .appendYears()
                .appendSuffix(" year", " years")
                .appendSeparator(", ", " and ", variants)
                .appendMonths()
                .appendSuffix(" month", " months")
                .appendSeparator(", ", " and ", variants)
                .appendWeeks()
                .appendSuffix(" week", " weeks")
                .appendSeparator(", ", " and ", variants)
                .appendDays()
                .appendSuffix(" day", " days")
                .appendSeparator(", ", " and ", variants)
                .appendHours()
                .appendSuffix(" hour", " hours")
                .appendSeparator(", ", " and ", variants)
                .appendMinutes()
                .appendSuffix(" minute", " minutes")
                .appendSeparator(", ", " and ", variants)
                .appendSeconds()
                .appendSuffix(" second", " seconds")
                .appendSeparator(", ", " and ", variants)
                .appendMillis()
                .appendSuffix(" millisecond", " milliseconds")
                .toFormatter();
        }
        return cEnglishWords;
    }

}
