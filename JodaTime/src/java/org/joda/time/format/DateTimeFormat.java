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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadWritableInstant;
import org.joda.time.ReadableInstant;
import org.joda.time.chrono.iso.ISOChronology;

/**
 * DateTimeFormat provides localized printing and parsing capabilities for all
 * dates and times.
 *
 * @author Brian S O'Neill
 * @see ISODateTimeFormat
 * @see DateTimeFormatterBuilder
 */
public class DateTimeFormat {

    // Maps Chronology instances to maps that map Locales to DateTimeFormat instances.
    private static Map cInstanceCache = new HashMap(7);

    public static DateTimeFormat getInstanceUTC() {
        return getInstance(ISOChronology.getInstanceUTC(), Locale.getDefault());
    }

    public static DateTimeFormat getInstance() {
        return getInstance(ISOChronology.getInstance(), Locale.getDefault());
    }

    public static DateTimeFormat getInstance(DateTimeZone zone) {
        return getInstance(ISOChronology.getInstance(zone), Locale.getDefault());
    }

    public static DateTimeFormat getInstance(DateTimeZone zone, Locale locale) {
        return getInstance(ISOChronology.getInstance(zone), locale);
    }

    /**
     * @param chrono Chronology to use
     */
    public static DateTimeFormat getInstance(Chronology chrono) {
        return getInstance(chrono, Locale.getDefault());
    }

    /**
     * @param chrono Chronology to use
     * @param locale Locale to use
     */
    public static synchronized DateTimeFormat getInstance(Chronology chrono, Locale locale) {
        if (chrono == null) {
            throw new IllegalArgumentException("The Chronology must not be null");
        }
        if (locale == null) {
            throw new IllegalArgumentException("The Locale must not be null");
        }
        Map map = (Map)cInstanceCache.get(chrono);
        if (map == null) {
            map = new HashMap(7);
            cInstanceCache.put(chrono, map);
        }
        DateTimeFormat dtf = (DateTimeFormat)map.get(locale);
        if (dtf == null) {
            dtf = new DateTimeFormat(chrono, locale);
            map.put(locale, dtf);
        }
        return dtf;
    }

    private final Chronology iChrono;
    private final Locale iLocale;

    // Maps patterns to formatters.
    private Map iPatternedCache = new HashMap(7);

    // Maps styles to formatters.
    private Map iStyledCache = new HashMap(7);

    private DateTimeFormat(Chronology chrono, Locale locale) {
        iChrono = chrono;
        iLocale = locale;
    }

    /**
     * Select a format from a custom {@link DateTimeFormatterBuilder#appendPattern pattern}.
     *
     * @param pattern pattern specification
     * @throws IllegalArgumentException
     * @see DateTimeFormatterBuilder#appendPattern
     */
    public synchronized DateTimeFormatter forPattern(String pattern) {
        DateTimeFormatter formatter = (DateTimeFormatter)iPatternedCache.get(pattern);
        if (formatter != null) {
            return formatter;
        }

        if (pattern == null) {
            throw new IllegalArgumentException("Invalid pattern specification");
        }

        DateTimeFormatterBuilder builder = 
            new DateTimeFormatterBuilder(iChrono, iLocale).appendPattern(pattern);

        if (builder.canBuildFormatter()) {
            formatter = builder.toFormatter();
        } else if (builder.canBuildPrinter()) {
            formatter = new FPrinter(builder.toPrinter());
        } else if (builder.canBuildParser()) {
            // I don't expect this case to ever occur.
            formatter = new FParser(builder.toParser());
        } else {
            throw new UnsupportedOperationException("Pattern unsupported: " + pattern);
        }

        iPatternedCache.put(pattern, formatter);
        return formatter;
    }

    /**
     * Select a format from a two character style pattern. The first character
     * is the date style, and the second character is the time style. Specify a
     * character of 'S' for short style, 'M' for medium, 'L' for long, and 'F'
     * for full. A date or time may be ommitted by specifying a style character '-'.
     *
     * @param style two characters from the set {"S", "M", "L", "F", "-"}
     * @throws IllegalArgumentException
     */
    public synchronized DateTimeFormatter forStyle(String style) {
        DateTimeFormatter formatter = (DateTimeFormatter)iStyledCache.get(style);
        if (formatter == null) {
            formatter = forPattern(getPatternForStyle(style));
            iStyledCache.put(style, formatter);
        }
        return formatter;
    }

    /**
     * Returns a pattern specification from a two character style. The first
     * character is the date style, and the second character is the time
     * style. Specify a character of 'S' for short style, 'M' for medium, 'L'
     * for long, and 'F' for full. A date or time may be ommitted by specifying
     * a style character '-'.
     *
     * @param style two characters from the set {"S", "M", "L", "F", "-"}
     * @throws IllegalArgumentException
     */
    public String getPatternForStyle(String style) {
        if (style == null || style.length() != 2) {
            throw new IllegalArgumentException("Invalid style specification: " + style);
        }

        if (style.charAt(1) == '-') {
            // date only
            return getDatePattern(style.charAt(0));
        } else if (style.charAt(0) == '-') {
            // time only
            return getTimePattern(style.charAt(1));
        } else {
            // datetime
            return getDateTimePattern(style.charAt(0), style.charAt(1));
        }
    }

    private String getDatePattern(char style) {
        int istyle = selectStyle(style);
        try {
            return ((SimpleDateFormat)DateFormat.getDateInstance(istyle, iLocale)).toPattern();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("No date pattern for locale: " + iLocale);
        }
    }

    private String getTimePattern(char style) {
        int istyle = selectStyle(style);
        try {
            return ((SimpleDateFormat)DateFormat.getTimeInstance(istyle, iLocale)).toPattern();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("No time pattern for locale: " + iLocale);
        }
    }

    private String getDateTimePattern(char dateStyle, char timeStyle) {
        int idateStyle = selectStyle(dateStyle);
        int itimeStyle = selectStyle(dateStyle);
        try {
            return ((SimpleDateFormat)DateFormat.getDateTimeInstance
                    (idateStyle, itimeStyle, iLocale)).toPattern();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("No datetime pattern for locale: " + iLocale);
        }
    }

    private int selectStyle(char c) {
        switch (c) {
        case 'S':
            return DateFormat.SHORT;
        case 'M':
            return DateFormat.MEDIUM;
        case 'L':
            return DateFormat.LONG;
        case 'F':
            return DateFormat.FULL;
        default:
            throw new IllegalArgumentException("Invalid style character: " + c);
        }
    }

    /**
     * A fake formatter that can only print.
     */
    private static class FPrinter implements DateTimeFormatter {
        private final DateTimePrinter mPrinter;

        FPrinter(DateTimePrinter printer) {
            mPrinter = printer;
        }

        public Chronology getChronology() {
            return mPrinter.getChronology();
        }

        public int estimatePrintedLength() {
            return mPrinter.estimatePrintedLength();
        }

        public void printTo(StringBuffer buf, ReadableInstant instant) {
            mPrinter.printTo(buf, instant);
        }

        public void printTo(Writer out, ReadableInstant instant) throws IOException {
            mPrinter.printTo(out, instant);
        }

        public void printTo(StringBuffer buf, long millisUTC) {
            mPrinter.printTo(buf, millisUTC);
        }

        public void printTo(Writer out, long millisUTC) throws IOException {
            mPrinter.printTo(out, millisUTC);
        }

        public void printTo(StringBuffer buf, long millisUTC, DateTimeZone zone) {
            mPrinter.printTo(buf, millisUTC, zone);
        }

        public void printTo(Writer out, long millisUTC, DateTimeZone zone)
            throws IOException {
            mPrinter.printTo(out, millisUTC, zone);
        }

        public void printTo(StringBuffer buf, long millisUTC,
                            DateTimeZone zone, long millisLocal) {
            mPrinter.printTo(buf, millisUTC, zone, millisLocal);
        }

        public void printTo(Writer out, long millisUTC,
                            DateTimeZone zone, long millisLocal)
            throws IOException {
            mPrinter.printTo(out, millisUTC, zone, millisLocal);
        }

        public String print(ReadableInstant instant) {
            return mPrinter.print(instant);
        }

        public String print(long millisUTC) {
            return mPrinter.print(millisUTC);
        }

        public String print(long millisUTC, DateTimeZone zone) {
            return mPrinter.print(millisUTC, zone);
        }

        public String print(long millisUTC, DateTimeZone zone, long millisLocal) {
            return mPrinter.print(millisUTC, zone, millisLocal);
        }

        public int estimateParsedLength() {
            return 0;
        }

        public int parseInto(DateTimeParserBucket bucket, String text, int position) {
            throw unsupported();
        }

        public int parseInto(ReadWritableInstant instant, String text, int position) {
            throw unsupported();
        }

        public long parseMillis(String text) throws ParseException {
            throw unsupported();
        }

        public long parseMillis(String text, long millis) throws ParseException {
            throw unsupported();
        }

        public DateTime parseDateTime(String text) throws ParseException {
            throw unsupported();
        }

        public MutableDateTime parseMutableDateTime(String text) throws ParseException {
            throw unsupported();
        }

        private UnsupportedOperationException unsupported() {
            return new UnsupportedOperationException("Parsing not supported");
        }
    }

    /**
     * A fake formatter that can only parse.
     */
    private static class FParser implements DateTimeFormatter {
        private final DateTimeParser mParser;

        FParser(DateTimeParser parser) {
            mParser = parser;
        }

        public Chronology getChronology() {
            return mParser.getChronology();
        }

        public int estimatePrintedLength() {
            return 0;
        }

        public void printTo(StringBuffer buf, ReadableInstant instant) {
            throw unsupported();
        }

        public void printTo(Writer out, ReadableInstant instant) throws IOException {
            throw unsupported();
        }

        public void printTo(StringBuffer buf, long millisUTC) {
            throw unsupported();
        }

        public void printTo(Writer out, long millisUTC) throws IOException {
            throw unsupported();
        }

        public void printTo(StringBuffer buf, long millisUTC, DateTimeZone zone) {
            throw unsupported();
        }

        public void printTo(Writer out, long millisUTC, DateTimeZone zone) {
            throw unsupported();
        }

        public void printTo(StringBuffer buf, long millisUTC,
                            DateTimeZone zone, long millisLocal) {
            throw unsupported();
        }

        public void printTo(Writer out, long millisUTC,
                            DateTimeZone zone, long millisLocal) {
            throw unsupported();
        }

        public String print(ReadableInstant instant) {
            throw unsupported();
        }

        public String print(long millisUTC) {
            throw unsupported();
        }

        public String print(long millisUTC, DateTimeZone zone) {
            throw unsupported();
        }

        public String print(long millisUTC, DateTimeZone zone, long millisLocal) {
            throw unsupported();
        }

        public int estimateParsedLength() {
            return mParser.estimateParsedLength();
        }

        public int parseInto(DateTimeParserBucket bucket, String text, int position) {
            return mParser.parseInto(bucket, text, position);
        }

        public int parseInto(ReadWritableInstant instant, String text, int position) {
            return mParser.parseInto(instant, text, position);
        }

        public long parseMillis(String text) throws ParseException {
            return mParser.parseMillis(text);
        }

        public long parseMillis(String text, long millis) throws ParseException {
            return mParser.parseMillis(text, millis);
        }

        public DateTime parseDateTime(String text) throws ParseException {
            return mParser.parseDateTime(text);
        }

        public MutableDateTime parseMutableDateTime(String text) throws ParseException {
            return mParser.parseMutableDateTime(text);
        }

        private UnsupportedOperationException unsupported() {
            return new UnsupportedOperationException("Printing not supported");
        }
    }
}
