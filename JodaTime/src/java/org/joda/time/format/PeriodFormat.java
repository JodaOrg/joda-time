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

import java.util.Locale;

/**
 * PeriodFormat provides basic printing and parsing capabilities for
 * periods. Eventually, this class will also support localization.
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

    private static final PeriodFormat INSTANCE = new PeriodFormat();

    /**
     * Gets a formatter provider that works using the default locale.
     * 
     * @return a format provider
     */
    public static PeriodFormat getInstance() {
        return INSTANCE;
    }

    /**
     * Gets a formatter provider that works using the given locale.
     * 
     * @param locale  the Locale to use, null for default locale
     * @return a format provider
     */
    public static PeriodFormat getInstance(Locale locale) {
        return INSTANCE;
    }

    private final PeriodFormatter iDefault;

    private PeriodFormat() {
        String[] variants = {" ", ",", ",and ", ", and "};
        iDefault = new PeriodFormatterBuilder()
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

    /**
     * Returns the default PeriodFormatter.
     */
    public PeriodFormatter getDefault() {
        return iDefault;
    }
}
