/*
 *  Copyright 2001-2014 Stephen Colebourne
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
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.joda.time.ReadWritablePeriod;
import org.joda.time.ReadablePeriod;

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
     * You can add your own translation by creating messages_<locale>.properties file
     * and adding it to the {@code org.joda.time.format.messages} path.
     * <p>
     * Simple example (1 -> singular suffix, not 1 -> plural suffix):
     * 
     * <pre>
     * PeriodFormat.space=\ 
     * PeriodFormat.comma=,
     * PeriodFormat.commandand=,and 
     * PeriodFormat.commaspaceand=, and 
     * PeriodFormat.commaspace=, 
     * PeriodFormat.spaceandspace=\ and 
     * PeriodFormat.year=\ year
     * PeriodFormat.years=\ years
     * PeriodFormat.month=\ month
     * PeriodFormat.months=\ months
     * PeriodFormat.week=\ week
     * PeriodFormat.weeks=\ weeks
     * PeriodFormat.day=\ day
     * PeriodFormat.days=\ days
     * PeriodFormat.hour=\ hour
     * PeriodFormat.hours=\ hours
     * PeriodFormat.minute=\ minute
     * PeriodFormat.minutes=\ minutes
     * PeriodFormat.second=\ second
     * PeriodFormat.seconds=\ seconds
     * PeriodFormat.millisecond=\ millisecond
     * PeriodFormat.milliseconds=\ milliseconds
     * </pre>
     * 
     * <p>
     * Some languages contain more than two suffixes. You can use regular expressions
     * for them. Here's an example using regular expression for English:
     * 
     * <pre>
     * PeriodFormat.space=\ 
     * PeriodFormat.comma=,
     * PeriodFormat.commandand=,and 
     * PeriodFormat.commaspaceand=, and 
     * PeriodFormat.commaspace=, 
     * PeriodFormat.spaceandspace=\ and 
     * PeriodFormat.regex.separator=%
     * PeriodFormat.years.regex=1$%.*
     * PeriodFormat.years.list=\ year%\ years
     * PeriodFormat.months.regex=1$%.*
     * PeriodFormat.months.list=\ month%\ months
     * PeriodFormat.weeks.regex=1$%.*
     * PeriodFormat.weeks.list=\ week%\ weeks
     * PeriodFormat.days.regex=1$%.*
     * PeriodFormat.days.list=\ day%\ days
     * PeriodFormat.hours.regex=1$%.*
     * PeriodFormat.hours.list=\ hour%\ hours
     * PeriodFormat.minutes.regex=1$%.*
     * PeriodFormat.minutes.list=\ minute%\ minutes
     * PeriodFormat.seconds.regex=1$%.*
     * PeriodFormat.seconds.list=\ second%\ seconds
     * PeriodFormat.milliseconds.regex=1$%.*
     * PeriodFormat.milliseconds.list=\ millisecond%\ milliseconds
     * </pre>
     * 
     * <p>
     * You can mix both approaches. Here's example for Polish (
     * "1 year, 2 years, 5 years, 12 years, 15 years, 21 years, 22 years, 25 years"
     * translates to
     * "1 rok, 2 lata, 5 lat, 12 lat, 15 lat, 21 lat, 22 lata, 25 lat"). Notice that
     * PeriodFormat.day and PeriodFormat.days is used for day suffixes as there is no
     * need for regular expressions:
     * 
     * <pre>
     * PeriodFormat.space=\ 
     * PeriodFormat.comma=,
     * PeriodFormat.commandand=,i 
     * PeriodFormat.commaspaceand=, i 
     * PeriodFormat.commaspace=, 
     * PeriodFormat.spaceandspace=\ i 
     * PeriodFormat.regex.separator=%
     * PeriodFormat.years.regex=^1$%[0-9]*(?&lt;!1)[2-4]$%[0-9]*
     * PeriodFormat.years.list=\ rok%\ lata%\ lat
     * PeriodFormat.months.regex=^1$%[0-9]*(?&lt;!1)[2-4]$%[0-9]*
     * PeriodFormat.months.list=\ miesi\u0105c%\ miesi\u0105ce%\ miesi\u0119cy
     * PeriodFormat.weeks.regex=^1$%[0-9]*(?&lt;!1)[2-4]$%[0-9]*
     * PeriodFormat.weeks.list=\ tydzie\u0144%\ tygodnie%\ tygodni
     * PeriodFormat.day=\ dzie\u0144
     * PeriodFormat.days=\ dni
     * PeriodFormat.hours.regex=^1$%[0-9]*(?&lt;!1)[2-4]$%[0-9]*
     * PeriodFormat.hours.list=\ godzina%\ godziny%\ godzin
     * PeriodFormat.minutes.regex=^1$%[0-9]*(?&lt;!1)[2-4]$%[0-9]*
     * PeriodFormat.minutes.list=\ minuta%\ minuty%\ minut
     * PeriodFormat.seconds.regex=^1$%[0-9]*(?&lt;!1)[2-4]$%[0-9]*
     * PeriodFormat.seconds.list=\ sekunda%\ sekundy%\ sekund
     * PeriodFormat.milliseconds.regex=^1$%[0-9]*(?&lt;!1)[2-4]$%[0-9]*
     * PeriodFormat.milliseconds.list=\ milisekunda%\ milisekundy%\ milisekund
     * </pre>
     * 
     * <p>
     * Each PeriodFormat.&lt;duration_field_type&gt;.regex property stands for an array of
     * regular expressions and is followed by a property
     * PeriodFormat.&lt;duration_field_type&gt;.list holding an array of suffixes.
     * PeriodFormat.regex.separator is used for splitting. See
     * {@link PeriodFormatterBuilder#appendSuffix(String[], String[])} for details.
     * <p>
     * Available languages are English, Danish, Dutch, French, German, Japanese,
     * Polish, Portuguese and Spanish.
     * 
     * @param locale the locale
     * @return the formatter, not null
     * @since 2.0, regex since 2.5
     */
    public static PeriodFormatter wordBased(Locale locale) {
        PeriodFormatter pf = FORMATTERS.get(locale);
        if (pf == null) {
            DynamicWordBased dynamic = new DynamicWordBased(buildWordBased(locale));
            pf = new PeriodFormatter(dynamic, dynamic, locale, null);
            PeriodFormatter existing = FORMATTERS.putIfAbsent(locale, pf);
            if (existing != null) {
                pf = existing;
            }
        }
        return pf;
    }

    //-----------------------------------------------------------------------
    private static PeriodFormatter buildWordBased(Locale locale) {
        ResourceBundle b = ResourceBundle.getBundle(BUNDLE_NAME, locale);
        if (containsKey(b, "PeriodFormat.regex.separator")) {
            return buildRegExFormatter(b, locale);
        } else {
            return buildNonRegExFormatter(b, locale);
        }
    }

    private static PeriodFormatter buildRegExFormatter(ResourceBundle b, Locale locale) {
        String[] variants = retrieveVariants(b);
        String regExSeparator = b.getString("PeriodFormat.regex.separator");
        
        PeriodFormatterBuilder builder = new PeriodFormatterBuilder();
        builder.appendYears();
        if (containsKey(b, "PeriodFormat.years.regex")) {
            builder.appendSuffix(
                b.getString("PeriodFormat.years.regex").split(regExSeparator),
                b.getString("PeriodFormat.years.list").split(regExSeparator));
        } else {
            builder.appendSuffix(b.getString("PeriodFormat.year"), b.getString("PeriodFormat.years"));
        }

        builder.appendSeparator(b.getString("PeriodFormat.commaspace"), b.getString("PeriodFormat.spaceandspace"), variants);
        builder.appendMonths();
        if (containsKey(b, "PeriodFormat.months.regex")) {
            builder.appendSuffix(
                    b.getString("PeriodFormat.months.regex").split(regExSeparator),
                    b.getString("PeriodFormat.months.list").split(regExSeparator));
        } else {
            builder.appendSuffix(b.getString("PeriodFormat.month"), b.getString("PeriodFormat.months"));
        }        

        builder.appendSeparator(b.getString("PeriodFormat.commaspace"), b.getString("PeriodFormat.spaceandspace"), variants);
        builder.appendWeeks();
        if (containsKey(b, "PeriodFormat.weeks.regex")) {
            builder.appendSuffix(
                    b.getString("PeriodFormat.weeks.regex").split(regExSeparator),
                    b.getString("PeriodFormat.weeks.list").split(regExSeparator));
        } else {
            builder.appendSuffix(b.getString("PeriodFormat.week"), b.getString("PeriodFormat.weeks"));
        }            

        builder.appendSeparator(b.getString("PeriodFormat.commaspace"), b.getString("PeriodFormat.spaceandspace"), variants);
        builder.appendDays();
        if (containsKey(b, "PeriodFormat.days.regex")) {
            builder.appendSuffix(
                    b.getString("PeriodFormat.days.regex").split(regExSeparator),
                    b.getString("PeriodFormat.days.list").split(regExSeparator));
        } else {
            builder.appendSuffix(b.getString("PeriodFormat.day"), b.getString("PeriodFormat.days"));
        }            

        builder.appendSeparator(b.getString("PeriodFormat.commaspace"), b.getString("PeriodFormat.spaceandspace"), variants);
        builder.appendHours();
        if (containsKey(b, "PeriodFormat.hours.regex")) {
            builder.appendSuffix(
                    b.getString("PeriodFormat.hours.regex").split(regExSeparator),
                    b.getString("PeriodFormat.hours.list").split(regExSeparator));
        } else {
            builder.appendSuffix(b.getString("PeriodFormat.hour"), b.getString("PeriodFormat.hours"));
        }            

        builder.appendSeparator(b.getString("PeriodFormat.commaspace"), b.getString("PeriodFormat.spaceandspace"), variants);
        builder.appendMinutes();
        if (containsKey(b, "PeriodFormat.minutes.regex")) {
            builder.appendSuffix(
                    b.getString("PeriodFormat.minutes.regex").split(regExSeparator),
                    b.getString("PeriodFormat.minutes.list").split(regExSeparator));
        } else {
            builder.appendSuffix(b.getString("PeriodFormat.minute"), b.getString("PeriodFormat.minutes"));
        }    

        builder.appendSeparator(b.getString("PeriodFormat.commaspace"), b.getString("PeriodFormat.spaceandspace"), variants);
        builder.appendSeconds();
        if (containsKey(b, "PeriodFormat.seconds.regex")) {
            builder.appendSuffix(
                    b.getString("PeriodFormat.seconds.regex").split(regExSeparator),
                    b.getString("PeriodFormat.seconds.list").split(regExSeparator));
        } else {
            builder.appendSuffix(b.getString("PeriodFormat.second"), b.getString("PeriodFormat.seconds"));
        }

        builder.appendSeparator(b.getString("PeriodFormat.commaspace"), b.getString("PeriodFormat.spaceandspace"), variants);
        builder.appendMillis();
        if (containsKey(b, "PeriodFormat.milliseconds.regex")) {
            builder.appendSuffix(
                    b.getString("PeriodFormat.milliseconds.regex").split(regExSeparator),
                    b.getString("PeriodFormat.milliseconds.list").split(regExSeparator));
        } else {
            builder.appendSuffix(b.getString("PeriodFormat.millisecond"), b.getString("PeriodFormat.milliseconds"));
        }
        return builder.toFormatter().withLocale(locale);
    }

    private static PeriodFormatter buildNonRegExFormatter(ResourceBundle b, Locale locale) {
        String[] variants = retrieveVariants(b);
        return new PeriodFormatterBuilder()
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
            .toFormatter().withLocale(locale);
    }

    private static String[] retrieveVariants(ResourceBundle b) {
        return new String[] { b.getString("PeriodFormat.space"), b.getString("PeriodFormat.comma"),
                b.getString("PeriodFormat.commandand"), b.getString("PeriodFormat.commaspaceand") };
    }

    // simulate ResourceBundle.containsKey()
    private static boolean containsKey(ResourceBundle bundle, String key) {
        for (Enumeration<String> en = bundle.getKeys(); en.hasMoreElements(); ) {
            if (en.nextElement().equals(key)) {
                return true;
            }
        }
        return false;
    }

    //-----------------------------------------------------------------------
    /**
     * Printer/parser that reacts to the locale and changes the word-based
     * pattern if necessary.
     */
    static class DynamicWordBased
            implements PeriodPrinter, PeriodParser {

        /** The formatter with the locale selected at construction time. */
        private final PeriodFormatter iFormatter;

        DynamicWordBased(PeriodFormatter formatter) {
            iFormatter = formatter;
        }

        public int countFieldsToPrint(ReadablePeriod period, int stopAt, Locale locale) {
            return getPrinter(locale).countFieldsToPrint(period, stopAt, locale);
        }

        public int calculatePrintedLength(ReadablePeriod period, Locale locale) {
            return getPrinter(locale).calculatePrintedLength(period, locale);
        }

        public void printTo(StringBuffer buf, ReadablePeriod period, Locale locale) {
            getPrinter(locale).printTo(buf, period, locale);
        }

        public void printTo(Writer out, ReadablePeriod period, Locale locale) throws IOException {
            getPrinter(locale).printTo(out, period, locale);
        }

        private PeriodPrinter getPrinter(Locale locale) {
            if (locale != null && !locale.equals(iFormatter.getLocale())) {
                return wordBased(locale).getPrinter();
            }
            return iFormatter.getPrinter();
        }

        public int parseInto(
                ReadWritablePeriod period, String periodStr,
                int position, Locale locale) {
            return getParser(locale).parseInto(period, periodStr, position, locale);
        }

        private PeriodParser getParser(Locale locale) {
            if (locale != null && !locale.equals(iFormatter.getLocale())) {
                return wordBased(locale).getParser();
            }
            return iFormatter.getParser();
        }
    }

}
