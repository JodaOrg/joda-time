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

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationFieldType;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadWritableInstant;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.field.RemainderDateTimeField;

/**
 * DateTimeFormat provides localized printing and parsing capabilities for all
 * dates and times.
 * <p>
 * This class provides access to the actual DateTimeFormatter instances in two ways:
 * <ul>
 * <li>{@link #forPattern(String) Pattern} provides a DateTimeFormatter based on
 * a pattern string that is compatible with the JDK date patterns.
 * <li>{@link #forStyle(String) Style} provides a DateTimeFormatter based on a
 * two character style, representing short, medium, long and full.
 * </ul>
 * <p>
 * For example, to use a patterm:
 * <pre>
 * DateTime dt = new DateTime();
 * DateTimeFormatter fmt = DateTimeFormat.getInstance().forPattern("MMMM, yyyy");
 * String str = fmt.print(dt);
 * </pre>
 *
 * The pattern syntax is compatible with java.text.SimpleDateFormat, but a few
 * more symbols are also supported. All ASCII letters are reserved as pattern
 * letters, which are defined as the following:
 * <blockquote>
 * <pre>
 * Symbol  Meaning                      Presentation  Examples
 * ------  -------                      ------------  -------
 * G       era                          text          AD
 * C       century of era (&gt;=0)         number        20
 * Y       year of era (&gt;=0)            year          1996
 *
 * x       weekyear                     year          1996
 * w       week of weekyear             number        27
 * e       day of week                  number        2
 * E       day of week                  text          Tuesday; Tue
 *
 * y       year                         year          1996
 * D       day of year                  number        189
 * M       month of year                month         July; Jul; 07
 * d       day of month                 number        10
 *
 * a       halfday of day               text          PM
 * K       hour of halfday (0~11)       number        0
 * h       clockhour of halfday (1~12)  number        12
 *
 * H       hour of day (0~23)           number        0
 * k       clockhour of day (1~24)      number        24
 * m       minute of hour               number        30
 * s       second of minute             number        55
 * S       fraction of second           number        978
 *
 * z       time zone                    text          Pacific Standard Time; PST
 * Z       time zone offset             text          -08:00; -0800
 *
 * '       escape for text              delimiter
 * ''      single quote                 literal       '
 * </pre>
 * </blockquote>
 * The count of pattern letters determine the format.
 * <p>
 * <strong>Text</strong>: If the number of pattern letters is 4 or more,
 * the full form is used; otherwise a short or abbreviated form is used if
 * available.
 * <p>
 * <strong>Number</strong>: The minimum number of digits. Shorter numbers
 * are zero-padded to this amount.
 * <p>
 * <strong>Year</strong>: Numeric presentation for year and weekyear fields
 * are handled specially. For example, if the count of 'y' is 2, the year
 * will be displayed as the zero-based year of the century, which is two
 * digits.
 * <p>
 * <strong>Month</strong>: 3 or over, use text, otherwise use number.
 * <p>
 * Any characters in the pattern that are not in the ranges of ['a'..'z']
 * and ['A'..'Z'] will be treated as quoted text. For instance, characters
 * like ':', '.', ' ', '#' and '@' will appear in the resulting time text
 * even they are not embraced within single quotes.
 * <p>
 * DateTimeFormat is thread-safe and immutable, and the formatters it returns
 * are as well.
 *
 * @author Brian S O'Neill
 * @since 1.0
 * @see ISODateTimeFormat
 * @see DateTimeFormatterBuilder
 */
public class DateTimeFormat {

    /**
     * Cache that maps Chronology instances to maps that map
     * Locales to DateTimeFormat instances.
     */
    private static Map cInstanceCache = new HashMap(7);

    //-----------------------------------------------------------------------
    /**
     * Gets an instance of the formatter provider that works with the default locale.
     * 
     * @return a format provider
     */
    public static DateTimeFormat getInstance() {
        return getInstance(Locale.getDefault());
    }

    /**
     * Gets an instance of the formatter provider that works with the given locale.
     * 
     * @param locale  the Locale to use, null for default locale
     * @return a format provider
     */
    public synchronized static DateTimeFormat getInstance(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        DateTimeFormat dtf = (DateTimeFormat) cInstanceCache.get(locale);
        if (dtf == null) {
            dtf = new DateTimeFormat(locale);
            cInstanceCache.put(locale, dtf);
        }
        return dtf;
    }

    //-----------------------------------------------------------------------
    /**
     * Parses the given pattern and appends the rules to the given
     * DateTimeFormatterBuilder.
     *
     * @param pattern  pattern specification
     * @throws IllegalArgumentException if the pattern is invalid
     * @see #forPattern
     */
    public static void appendPatternTo(DateTimeFormatterBuilder builder, String pattern) {
        int length = pattern.length();
        int[] indexRef = new int[1];

        for (int i=0; i<length; i++) {
            indexRef[0] = i;
            String token = parseToken(pattern, indexRef);
            i = indexRef[0];

            int tokenLen = token.length();
            if (tokenLen == 0) {
                break;
            }
            char c = token.charAt(0);

            switch (c) {
            case 'G': // era designator (text)
                builder.appendEraText();
                break;
            case 'C': // century of era (number)
                builder.appendCenturyOfEra(tokenLen, tokenLen);
                break;
            case 'x': // weekyear (number)
            case 'y': // year (number)
            case 'Y': // year of era (number)
                if (tokenLen == 2) {
                    // Use a new remainder type to ensure that the year of
                    // century is zero-based.
                    DateTimeFieldType type;
                    switch (c) {
                    case 'x':
                        type = new RemainderType(DateTimeFieldType.weekyear(),
                                                 DateTimeFieldType.weekyearOfCentury(), 100);
                        break;
                    case 'y': default:
                        type = new RemainderType(DateTimeFieldType.year(),
                                                 DateTimeFieldType.yearOfCentury(), 100);
                        break;
                    case 'Y':
                        type = new RemainderType(DateTimeFieldType.yearOfEra(),
                                                 DateTimeFieldType.yearOfCentury(), 100);
                        break;
                    }
                    builder.appendDecimal(type, 2, 2);
                } else {
                    // Try to support long year values.
                    int maxDigits = 9;

                    // Peek ahead to next token.
                    if (i + 1 < length) {
                        indexRef[0]++;
                        if (isNumericToken(parseToken(pattern, indexRef))) {
                            // If next token is a number, cannot support long years.
                            maxDigits = tokenLen;
                        }
                        indexRef[0]--;
                    }

                    switch (c) {
                    case 'x':
                        builder.appendWeekyear(tokenLen, maxDigits);
                        break;
                    case 'y':
                        builder.appendYear(tokenLen, maxDigits);
                        break;
                    case 'Y':
                        builder.appendYearOfEra(tokenLen, maxDigits);
                        break;
                    }
                }
                break;
            case 'M': // month of year (text and number)
                if (tokenLen >= 3) {
                    if (tokenLen >= 4) {
                        builder.appendMonthOfYearText();
                    } else {
                        builder.appendMonthOfYearShortText();
                    }
                } else {
                    builder.appendMonthOfYear(tokenLen);
                }
                break;
            case 'd': // day of month (number)
                builder.appendDayOfMonth(tokenLen);
                break;
            case 'h': // hour of day (number, 1..12)
                builder.appendClockhourOfHalfday(tokenLen);
                break;
            case 'H': // hour of day (number, 0..23)
                builder.appendHourOfDay(tokenLen);
                break;
            case 'm': // minute of hour (number)
                builder.appendMinuteOfHour(tokenLen);
                break;
            case 's': // second of minute (number)
                builder.appendSecondOfMinute(tokenLen);
                break;
            case 'S': // fraction of second (number)
                builder.appendFractionOfSecond(tokenLen, tokenLen);
                break;
            case 'e': // day of week (number)
                builder.appendDayOfWeek(tokenLen);
                break;
            case 'E': // dayOfWeek (text)
                if (tokenLen >= 4) {
                    builder.appendDayOfWeekText();
                } else {
                    builder.appendDayOfWeekShortText();
                }
                break;
            case 'D': // day of year (number)
                builder.appendDayOfYear(tokenLen);
                break;
            case 'w': // week of weekyear (number)
                builder.appendWeekOfWeekyear(tokenLen);
                break;
            case 'a': // am/pm marker (text)
                builder.appendHalfdayOfDayText();
                break;
            case 'k': // hour of day (1..24)
                builder.appendClockhourOfDay(tokenLen);
                break;
            case 'K': // hour of day (0..11)
                builder.appendClockhourOfHalfday(tokenLen);
                break;
            case 'z': // time zone (text)
                if (tokenLen >= 4) {
                    builder.appendTimeZoneName();
                } else {
                    builder.appendTimeZoneShortName();
                }
                break;
            case 'Z': // time zone offset
                if (tokenLen >= 4) {
                    builder.appendTimeZoneOffset(null, true, 2, 2);
                } else {
                    builder.appendTimeZoneOffset(null, false, 2, 2);
                }
                break;
            case '\'': // literal text
                String sub = token.substring(1);
                if (sub.length() == 1) {
                    builder.appendLiteral(sub.charAt(0));
                } else {
                    // Create copy of sub since otherwise the temporary quoted
                    // string would still be referenced internally.
                    builder.appendLiteral(new String(sub));
                }
                break;
            default:
                throw new IllegalArgumentException
                    ("Illegal pattern component: " + token);
            }
        }
    }

    private static String parseToken(final String pattern, final int[] indexRef) {
        StringBuffer buf = new StringBuffer();

        int i = indexRef[0];
        int length = pattern.length();

        char c = pattern.charAt(i);
        if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z') {
            // Scan a run of the same character, which indicates a time
            // pattern.
            buf.append(c);

            while (i + 1 < length) {
                char peek = pattern.charAt(i + 1);
                if (peek == c) {
                    buf.append(c);
                    i++;
                } else {
                    break;
                }
            }
        } else {
            // This will identify token as text.
            buf.append('\'');

            boolean inLiteral = false;

            for (; i < length; i++) {
                c = pattern.charAt(i);
                
                if (c == '\'') {
                    if (i + 1 < length && pattern.charAt(i + 1) == '\'') {
                        // '' is treated as escaped '
                        i++;
                        buf.append(c);
                    } else {
                        inLiteral = !inLiteral;
                    }
                } else if (!inLiteral &&
                           (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z')) {
                    i--;
                    break;
                } else {
                    buf.append(c);
                }
            }
        }

        indexRef[0] = i;
        return buf.toString();
    }

    // Returns true if token should be parsed as a numeric field.
    private static boolean isNumericToken(final String token) {
        int tokenLen = token.length();
        if (tokenLen > 0) {
            char c = token.charAt(0);
            switch (c) {
            case 'c': // century (number)
            case 'C': // century of era (number)
            case 'x': // weekyear (number)
            case 'y': // year (number)
            case 'Y': // year of era (number)
            case 'd': // day of month (number)
            case 'h': // hour of day (number, 1..12)
            case 'H': // hour of day (number, 0..23)
            case 'm': // minute of hour (number)
            case 's': // second of minute (number)
            case 'S': // fraction of second (number)
            case 'e': // day of week (number)
            case 'D': // day of year (number)
            case 'F': // day of week in month (number)
            case 'w': // week of year (number)
            case 'W': // week of month (number)
            case 'k': // hour of day (1..24)
            case 'K': // hour of day (0..11)
                return true;
            case 'M': // month of year (text and number)
                if (tokenLen <= 2) {
                    return true;
                }
            }
        }
            
        return false;
    }

    //-----------------------------------------------------------------------
    /** The locale to use */
    private final Locale iLocale;

    /** Maps patterns to formatters */
    private transient Map iPatternedCache = new HashMap(7);

    /** Maps styles to formatters */
    private transient Map iStyledCache = new HashMap(7);

    /**
     * Constructor.
     * 
     * @param locale  the locale to use, must not be null
     */
    private DateTimeFormat(final Locale locale) {
        super();
        iLocale = locale;
    }

    //-----------------------------------------------------------------------
    /**
     * Select a format from a custom pattern.
     *
     * @param pattern  pattern specification
     * @throws IllegalArgumentException if the pattern is invalid
     * @see #appendPatternTo
     */
    public synchronized DateTimeFormatter forPattern(final String pattern) {
        DateTimeFormatter formatter = (DateTimeFormatter) iPatternedCache.get(pattern);
        if (formatter != null) {
            return formatter;
        }

        if (pattern == null) {
            throw new IllegalArgumentException("Invalid pattern specification");
        }

        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder(iLocale);
        appendPatternTo(builder, pattern);

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
     * @param style  two characters from the set {"S", "M", "L", "F", "-"}
     * @throws IllegalArgumentException if the style is invalid
     */
    public synchronized DateTimeFormatter forStyle(final String style) {
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
     * @param style  two characters from the set {"S", "M", "L", "F", "-"}
     * @throws IllegalArgumentException if the style is invalid
     */
    public String getPatternForStyle(final String style) {
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

    private String getDatePattern(final char style) {
        int istyle = selectStyle(style);
        try {
            return ((SimpleDateFormat)DateFormat.getDateInstance(istyle, iLocale)).toPattern();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("No date pattern for locale: " + iLocale);
        }
    }

    private String getTimePattern(final char style) {
        int istyle = selectStyle(style);
        try {
            return ((SimpleDateFormat)DateFormat.getTimeInstance(istyle, iLocale)).toPattern();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("No time pattern for locale: " + iLocale);
        }
    }

    private String getDateTimePattern(final char dateStyle, final char timeStyle) {
        int idateStyle = selectStyle(dateStyle);
        int itimeStyle = selectStyle(timeStyle);
        try {
            return ((SimpleDateFormat)DateFormat.getDateTimeInstance
                    (idateStyle, itimeStyle, iLocale)).toPattern();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("No datetime pattern for locale: " + iLocale);
        }
    }

    private int selectStyle(final char c) {
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

    //-----------------------------------------------------------------------
    /**
     * Special field type which derives a new field as a remainder.
     */
    static class RemainderType extends DateTimeFieldType {
        private final DateTimeFieldType iWrappedType;
        private final DateTimeFieldType iType;
        private final int iDivisor;

        private transient RemainderDateTimeField iRecent;

        RemainderType(DateTimeFieldType wrappedType, DateTimeFieldType type, int divisor) {
            super(type.getName());
            iWrappedType = wrappedType;
            iType = type;
            iDivisor = divisor;
        }

        public DurationFieldType getDurationType() {
            return iType.getDurationType();
        }

        public DurationFieldType getRangeDurationType() {
            return iType.getRangeDurationType();
        }

        public DateTimeField getField(Chronology chrono) {
            DateTimeField wrappedField = iWrappedType.getField(chrono);
            RemainderDateTimeField field = iRecent;
            if (field != null && field.getWrappedField() == wrappedField) {
                return field;
            }
            field = new RemainderDateTimeField(wrappedField, iType, iDivisor);
            iRecent = field;
            return field;
        }
    }

    /**
     * A fake formatter that can only print.
     */
    static class FPrinter implements DateTimeFormatter {
        private final DateTimePrinter iPrinter;

        FPrinter(DateTimePrinter printer) {
            super();
            iPrinter = printer;
        }

        public int estimatePrintedLength() {
            return iPrinter.estimatePrintedLength();
        }

        public void printTo(StringBuffer buf, ReadableInstant instant) {
            iPrinter.printTo(buf, instant);
        }

        public void printTo(Writer out, ReadableInstant instant) throws IOException {
            iPrinter.printTo(out, instant);
        }

        public void printTo(StringBuffer buf, long instant) {
            iPrinter.printTo(buf, instant);
        }

        public void printTo(Writer out, long instant) throws IOException {
            iPrinter.printTo(out, instant);
        }

        public void printTo(StringBuffer buf, long instant, DateTimeZone zone) {
            iPrinter.printTo(buf, instant, zone);
        }

        public void printTo(Writer out, long instant, DateTimeZone zone)
            throws IOException {
            iPrinter.printTo(out, instant, zone);
        }

        public void printTo(StringBuffer buf, long instant, Chronology chrono) {
            iPrinter.printTo(buf, instant, chrono);
        }

        public void printTo(Writer out, long instant, Chronology chrono) throws IOException {
            iPrinter.printTo(out, instant, chrono);
        }

        public void printTo(StringBuffer buf, long instant, Chronology chrono,
                            int displayOffset, DateTimeZone displayZone) {
            iPrinter.printTo(buf, instant, chrono, displayOffset, displayZone);
        }

        public void printTo(Writer out, long instant, Chronology chrono,
                            int displayOffset, DateTimeZone displayZone) throws IOException {
            iPrinter.printTo(out, instant, chrono, displayOffset, displayZone);
        }

        public void printTo(StringBuffer buf, ReadablePartial instant) {
            iPrinter.printTo(buf, instant);
        }

        public void printTo(Writer out, ReadablePartial instant) throws IOException {
            iPrinter.printTo(out, instant);
        }

        public String print(ReadableInstant instant) {
            return iPrinter.print(instant);
        }

        public String print(long instant) {
            return iPrinter.print(instant);
        }

        public String print(long instant, DateTimeZone zone) {
            return iPrinter.print(instant, zone);
        }

        public String print(long instant, Chronology chrono) {
            return iPrinter.print(instant, chrono);
        }

        public String print(long instant, Chronology chrono,
                            int displayOffset, DateTimeZone displayZone) {
            return iPrinter.print(instant, chrono, displayOffset, displayZone);
        }

        public String print(ReadablePartial partial) {
            return iPrinter.print(partial);
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

        public long parseMillis(String text) {
            throw unsupported();
        }

        public long parseMillis(String text, Chronology chrono) {
            throw unsupported();
        }

        public long parseMillis(String text, long instantLocal) {
            throw unsupported();
        }

        public long parseMillis(String text, long instant, Chronology chrono) {
            throw unsupported();
        }

        public DateTime parseDateTime(String text) {
            throw unsupported();
        }

        public DateTime parseDateTime(String text, Chronology chrono) {
            throw unsupported();
        }

        public DateTime parseDateTime(String text, ReadableInstant instant) {
            throw unsupported();
        }

        public MutableDateTime parseMutableDateTime(String text) {
            throw unsupported();
        }

        public MutableDateTime parseMutableDateTime(String text, Chronology chrono) {
            throw unsupported();
        }

        public MutableDateTime parseMutableDateTime(String text,
                                                    ReadableInstant instant) {
            throw unsupported();
        }

        private UnsupportedOperationException unsupported() {
            return new UnsupportedOperationException("Parsing not supported");
        }
    }

    //-----------------------------------------------------------------------
    /**
     * A fake formatter that can only parse.
     */
    static class FParser implements DateTimeFormatter {
        private final DateTimeParser iParser;

        FParser(DateTimeParser parser) {
            super();
            iParser = parser;
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

        public void printTo(StringBuffer buf, long instant) {
            throw unsupported();
        }

        public void printTo(Writer out, long instant) throws IOException {
            throw unsupported();
        }

        public void printTo(StringBuffer buf, long instant, DateTimeZone zone) {
            throw unsupported();
        }

        public void printTo(Writer out, long instant, DateTimeZone zone) {
            throw unsupported();
        }

        public void printTo(StringBuffer buf, long instant, Chronology chrono) {
            throw unsupported();
        }

        public void printTo(Writer out, long instant, Chronology chrono) throws IOException {
            throw unsupported();
        }

        public void printTo(StringBuffer buf, long instant, Chronology chrono,
                            int displayOffset, DateTimeZone displayZone) {
            throw unsupported();
        }

        public void printTo(Writer out, long instant, Chronology chrono,
                            int displayOffset, DateTimeZone displayZone) throws IOException {
            throw unsupported();
        }

        public void printTo(StringBuffer buf, ReadablePartial instant) {
            throw unsupported();
        }

        public void printTo(Writer out, ReadablePartial instant) throws IOException {
            throw unsupported();
        }

        public String print(ReadableInstant instant) {
            throw unsupported();
        }

        public String print(long instant) {
            throw unsupported();
        }

        public String print(long instant, DateTimeZone zone) {
            throw unsupported();
        }

        public String print(long instant, Chronology chrono) {
            throw unsupported();
        }

        public String print(long instant, Chronology chrono,
                            int displayOffset, DateTimeZone displayZone) {
            throw unsupported();
        }

        public String print(ReadablePartial partial) {
            throw unsupported();
        }

        public int estimateParsedLength() {
            return iParser.estimateParsedLength();
        }

        public int parseInto(DateTimeParserBucket bucket, String text, int position) {
            return iParser.parseInto(bucket, text, position);
        }

        public int parseInto(ReadWritableInstant instant, String text, int position) {
            return iParser.parseInto(instant, text, position);
        }

        public long parseMillis(String text) {
            return iParser.parseMillis(text);
        }

        public long parseMillis(String text, Chronology chrono) {
            return iParser.parseMillis(text, chrono);
        }

        public long parseMillis(String text, long instant) {
            return iParser.parseMillis(text, instant);
        }

        public long parseMillis(String text, long instant, Chronology chrono) {
            return iParser.parseMillis(text, instant, chrono);
        }

        public DateTime parseDateTime(String text) {
            return iParser.parseDateTime(text);
        }

        public DateTime parseDateTime(String text, Chronology chrono) {
            return iParser.parseDateTime(text, chrono);
        }

        public DateTime parseDateTime(String text, ReadableInstant instant) {
            return iParser.parseDateTime(text, instant);
        }

        public MutableDateTime parseMutableDateTime(String text) {
            return iParser.parseMutableDateTime(text);
        }

        public MutableDateTime parseMutableDateTime(String text, Chronology chrono) {
            return iParser.parseMutableDateTime(text, chrono);
        }

        public MutableDateTime parseMutableDateTime(String text, ReadableInstant instant) {
            return iParser.parseMutableDateTime(text, instant);
        }

        private UnsupportedOperationException unsupported() {
            return new UnsupportedOperationException("Printing not supported");
        }
    }
}
