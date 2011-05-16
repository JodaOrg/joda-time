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
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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

    /**
     * The resource bundle name.
     */
    private static final String BUNDLE_NAME = "org.joda.time.format.messages";
    /**
     * The created formatters.
     */
    private static final ConcurrentMap<Locale, PeriodFormatter> FORMATTERS = new ConcurrentHashMap<Locale, PeriodFormatter>();

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
     * Gets the default formatter that outputs words in English.
     * <p>
     * This calls {@link #wordBased(Locale)} using a locale of {@code ENGLISH}.
     * 
     * @return the formatter, not null
     */
    public static PeriodFormatter getDefault() {
        return wordBased(Locale.ENGLISH);
    }

    /**
     * Returns a word based formatter for the JDK default locale.
     * <p>
     * This calls {@link #wordBased(Locale)} using the {@link Locale#getDefault() default locale}.
     * 
     * @return the formatter, not null
     * @since 2.0
     */
    public static PeriodFormatter wordBased() {
        return wordBased(Locale.getDefault());
    }

    /**
     * Returns a word based formatter for the specified locale.
     * <p>
     * The words are configured in a resource bundle text file -
     * {@code org.joda.time.format.messages}.
     * This can be added to via the normal classpath resource bundle mechanisms.
     * <p>
     * Available languages are English, German, Dutch, French, Spanish and Portuguese.
     * 
     * @return the formatter, not null
     * @since 2.0
     */
    public static PeriodFormatter wordBased(Locale locale) {
        PeriodFormatter pf = FORMATTERS.get(locale);
        if (pf == null) {
            ResourceBundle b = ResourceBundle.getBundle(BUNDLE_NAME, locale);
            String[] variants = {
                    b.getString("PeriodFormat.space"), b.getString("PeriodFormat.comma"),
                    b.getString("PeriodFormat.commandand"), b.getString("PeriodFormat.commaspaceand")};
            pf = new PeriodFormatterBuilder()
                .appendYears()
                .appendSuffix(b.getString("PeriodFormat.year"), b.getString("PeriodFormat.years"))
                .appendSeparator(b.getString("PeriodFormat.commaspace"), b.getString("PeriodFormat.spaceandspace"), variants)
                .appendMonths()
                .appendSuffix(b.getString("PeriodFormat.month"), b.getString("PeriodFormat.months"))
                .appendSeparator(b.getString("PeriodFormat.commaspace"), b.getString("PeriodFormat.spaceandspace"), variants)
                .appendWeeks()
                .appendSuffix(b.getString("PeriodFormat.week"), b.getString("PeriodFormat.weeks"))
                .appendSeparator(b.getString("PeriodFormat.commaspace"), b.getString("PeriodFormat.spaceandspace"), variants)
                .appendDays()
                .appendSuffix(b.getString("PeriodFormat.day"), b.getString("PeriodFormat.days"))
                .appendSeparator(b.getString("PeriodFormat.commaspace"), b.getString("PeriodFormat.spaceandspace"), variants)
                .appendHours()
                .appendSuffix(b.getString("PeriodFormat.hour"), b.getString("PeriodFormat.hours"))
                .appendSeparator(b.getString("PeriodFormat.commaspace"), b.getString("PeriodFormat.spaceandspace"), variants)
                .appendMinutes()
                .appendSuffix(b.getString("PeriodFormat.minute"), b.getString("PeriodFormat.minutes"))
                .appendSeparator(b.getString("PeriodFormat.commaspace"), b.getString("PeriodFormat.spaceandspace"), variants)
                .appendSeconds()
                .appendSuffix(b.getString("PeriodFormat.second"), b.getString("PeriodFormat.seconds"))
                .appendSeparator(b.getString("PeriodFormat.commaspace"), b.getString("PeriodFormat.spaceandspace"), variants)
                .appendMillis()
                .appendSuffix(b.getString("PeriodFormat.millisecond"), b.getString("PeriodFormat.milliseconds"))
                .toFormatter();
            FORMATTERS.putIfAbsent(locale, pf);
        }
        return pf;
    }

}
