/*
 *  Copyright 2001-2013 Stephen Colebourne
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
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadablePartial;

/**
 * Factory that creates instances of DateTimeFormatter from patterns and styles.
 * <p>
 * Datetime formatting is performed by the {@link DateTimeFormatter} class.
 * Three classes provide factory methods to create formatters, and this is one.
 * The others are {@link ISODateTimeFormat} and {@link DateTimeFormatterBuilder}.
 * <p>
 * This class provides two types of factory:
 * <ul>
 * <li>{@link #forPattern(String) Pattern} provides a DateTimeFormatter based on
 * a pattern string that is mostly compatible with the JDK date patterns.
 * <li>{@link #forStyle(String) Style} provides a DateTimeFormatter based on a
 * two character style, representing short, medium, long and full.
 * </ul>
 * <p>
 * For example, to use a patterm:
 * <pre>
 * DateTime dt = new DateTime();
 * DateTimeFormatter fmt = DateTimeFormat.forPattern("MMMM, yyyy");
 * String str = fmt.print(dt);
 * </pre>
 *
 * The pattern syntax is mostly compatible with java.text.SimpleDateFormat -
 * time zone names cannot be parsed and a few more symbols are supported.
 * All ASCII letters are reserved as pattern letters, which are defined as follows:
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
 * Z       time zone offset/id          zone          -0800; -08:00; America/Los_Angeles
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
 * <strong>Zone</strong>: 'Z' outputs offset without a colon, 'ZZ' outputs
 * the offset with a colon, 'ZZZ' or more outputs the zone id.
 * <p>
 * <strong>Zone names</strong>: Time zone names ('z') cannot be parsed.
 * <p>
 * Any characters in the pattern that are not in the ranges of ['a'..'z']
 * and ['A'..'Z'] will be treated as quoted text. For instance, characters
 * like ':', '.', ' ', '#' and '?' will appear in the resulting time text
 * even they are not embraced within single quotes.
 * <p>
 * DateTimeFormat is thread-safe and immutable, and the formatters it returns
 * are as well.
 *
 * @author Brian S O'Neill
 * @author Maxim Zhao
 * @since 1.0
 * @see ISODateTimeFormat
 * @see DateTimeFormatterBuilder
 */
public class DateTimeFormat {

    /** Style constant for FULL. */
    static final int FULL = 0;  // DateFormat.FULL
    /** Style constant for LONG. */
    static final int LONG = 1;  // DateFormat.LONG
    /** Style constant for MEDIUM. */
    static final int MEDIUM = 2;  // DateFormat.MEDIUM
    /** Style constant for SHORT. */
    static final int SHORT = 3;  // DateFormat.SHORT
    /** Style constant for NONE. */
    static final int NONE = 4;

    /** Type constant for DATE only. */
    static final int DATE = 0;
    /** Type constant for TIME only. */
    static final int TIME = 1;
    /** Type constant for DATETIME. */
    static final int DATETIME = 2;

    /** Maximum size of the pattern cache. */
    private static final int PATTERN_CACHE_SIZE = 500;

    /** Maps patterns to formatters via LRU, patterns don't vary by locale. */
    private static final Map<String, DateTimeFormatter> PATTERN_CACHE = new LinkedHashMap<String, DateTimeFormatter>(7) {
        private static final long serialVersionUID = 23L;
        @Override
        protected boolean removeEldestEntry(final Map.Entry<String, DateTimeFormatter> eldest) {
            return size() > PATTERN_CACHE_SIZE;
        };
    };

    /** Maps patterns to formatters, patterns don't vary by locale. */
    private static final DateTimeFormatter[] STYLE_CACHE = new DateTimeFormatter[25];

    //-----------------------------------------------------------------------
    /**
     * Factory to create a formatter from a pattern string.
     * The pattern string is described above in the class level javadoc.
     * It is very similar to SimpleDateFormat patterns.
     * <p>
     * The format may contain locale specific output, and this will change as
     * you change the locale of the formatter.
     * Call {@link DateTimeFormatter#withLocale(Locale)} to switch the locale.
     * For example:
     * <pre>
     * DateTimeFormat.forPattern(pattern).withLocale(Locale.FRANCE).print(dt);
     * </pre>
     *
     * @param pattern  pattern specification
     * @return the formatter
     * @throws IllegalArgumentException if the pattern is invalid
     */
    public static DateTimeFormatter forPattern(String pattern) {
        return createFormatterForPattern(pattern);
    }

    /**
     * Factory to create a format from a two character style pattern.
     * <p>
     * The first character is the date style, and the second character is the
     * time style. Specify a character of 'S' for short style, 'M' for medium,
     * 'L' for long, and 'F' for full.
     * A date or time may be ommitted by specifying a style character '-'.
     * <p>
     * The returned formatter will dynamically adjust to the locale that
     * the print/parse takes place in. Thus you just call
     * {@link DateTimeFormatter#withLocale(Locale)} and the Short/Medium/Long/Full
     * style for that locale will be output. For example:
     * <pre>
     * DateTimeFormat.forStyle(style).withLocale(Locale.FRANCE).print(dt);
     * </pre>
     *
     * @param style  two characters from the set {"S", "M", "L", "F", "-"}
     * @return the formatter
     * @throws IllegalArgumentException if the style is invalid
     */
    public static DateTimeFormatter forStyle(String style) {
        return createFormatterForStyle(style);
    }

    /**
     * Returns the pattern used by a particular style and locale.
     * <p>
     * The first character is the date style, and the second character is the
     * time style. Specify a character of 'S' for short style, 'M' for medium,
     * 'L' for long, and 'F' for full.
     * A date or time may be ommitted by specifying a style character '-'.
     *
     * @param style  two characters from the set {"S", "M", "L", "F", "-"}
     * @param locale  locale to use, null means default
     * @return the formatter
     * @throws IllegalArgumentException if the style is invalid
     * @since 1.3
     */
    public static String patternForStyle(String style, Locale locale) {
        DateTimeFormatter formatter = createFormatterForStyle(style);
        if (locale == null) {
            locale = Locale.getDefault();
        }
        // Not pretty, but it works.
        return ((StyleFormatter) formatter.getPrinter()).getPattern(locale);
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a format that outputs a short date format.
     * <p>
     * The format will change as you change the locale of the formatter.
     * Call {@link DateTimeFormatter#withLocale(Locale)} to switch the locale.
     * 
     * @return the formatter
     */
    public static DateTimeFormatter shortDate() {
        return createFormatterForStyleIndex(SHORT, NONE);
    }

    /**
     * Creates a format that outputs a short time format.
     * <p>
     * The format will change as you change the locale of the formatter.
     * Call {@link DateTimeFormatter#withLocale(Locale)} to switch the locale.
     * 
     * @return the formatter
     */
    public static DateTimeFormatter shortTime() {
        return createFormatterForStyleIndex(NONE, SHORT);
    }

    /**
     * Creates a format that outputs a short datetime format.
     * <p>
     * The format will change as you change the locale of the formatter.
     * Call {@link DateTimeFormatter#withLocale(Locale)} to switch the locale.
     * 
     * @return the formatter
     */
    public static DateTimeFormatter shortDateTime() {
        return createFormatterForStyleIndex(SHORT, SHORT);
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a format that outputs a medium date format.
     * <p>
     * The format will change as you change the locale of the formatter.
     * Call {@link DateTimeFormatter#withLocale(Locale)} to switch the locale.
     * 
     * @return the formatter
     */
    public static DateTimeFormatter mediumDate() {
        return createFormatterForStyleIndex(MEDIUM, NONE);
    }

    /**
     * Creates a format that outputs a medium time format.
     * <p>
     * The format will change as you change the locale of the formatter.
     * Call {@link DateTimeFormatter#withLocale(Locale)} to switch the locale.
     * 
     * @return the formatter
     */
    public static DateTimeFormatter mediumTime() {
        return createFormatterForStyleIndex(NONE, MEDIUM);
    }

    /**
     * Creates a format that outputs a medium datetime format.
     * <p>
     * The format will change as you change the locale of the formatter.
     * Call {@link DateTimeFormatter#withLocale(Locale)} to switch the locale.
     * 
     * @return the formatter
     */
    public static DateTimeFormatter mediumDateTime() {
        return createFormatterForStyleIndex(MEDIUM, MEDIUM);
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a format that outputs a long date format.
     * <p>
     * The format will change as you change the locale of the formatter.
     * Call {@link DateTimeFormatter#withLocale(Locale)} to switch the locale.
     * 
     * @return the formatter
     */
    public static DateTimeFormatter longDate() {
        return createFormatterForStyleIndex(LONG, NONE);
    }

    /**
     * Creates a format that outputs a long time format.
     * <p>
     * The format will change as you change the locale of the formatter.
     * Call {@link DateTimeFormatter#withLocale(Locale)} to switch the locale.
     * 
     * @return the formatter
     */
    public static DateTimeFormatter longTime() {
        return createFormatterForStyleIndex(NONE, LONG);
    }

    /**
     * Creates a format that outputs a long datetime format.
     * <p>
     * The format will change as you change the locale of the formatter.
     * Call {@link DateTimeFormatter#withLocale(Locale)} to switch the locale.
     * 
     * @return the formatter
     */
    public static DateTimeFormatter longDateTime() {
        return createFormatterForStyleIndex(LONG, LONG);
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a format that outputs a full date format.
     * <p>
     * The format will change as you change the locale of the formatter.
     * Call {@link DateTimeFormatter#withLocale(Locale)} to switch the locale.
     * 
     * @return the formatter
     */
    public static DateTimeFormatter fullDate() {
        return createFormatterForStyleIndex(FULL, NONE);
    }

    /**
     * Creates a format that outputs a full time format.
     * <p>
     * The format will change as you change the locale of the formatter.
     * Call {@link DateTimeFormatter#withLocale(Locale)} to switch the locale.
     * 
     * @return the formatter
     */
    public static DateTimeFormatter fullTime() {
        return createFormatterForStyleIndex(NONE, FULL);
    }

    /**
     * Creates a format that outputs a full datetime format.
     * <p>
     * The format will change as you change the locale of the formatter.
     * Call {@link DateTimeFormatter#withLocale(Locale)} to switch the locale.
     * 
     * @return the formatter
     */
    public static DateTimeFormatter fullDateTime() {
        return createFormatterForStyleIndex(FULL, FULL);
    }

    //-----------------------------------------------------------------------
    /**
     * Parses the given pattern and appends the rules to the given
     * DateTimeFormatterBuilder.
     *
     * @param pattern  pattern specification
     * @throws IllegalArgumentException if the pattern is invalid
     */
    static void appendPatternTo(DateTimeFormatterBuilder builder, String pattern) {
        parsePatternTo(builder, pattern);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor.
     *
     * @since 1.1 (previously private)
     */
    protected DateTimeFormat() {
        super();
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
    private static void parsePatternTo(DateTimeFormatterBuilder builder, String pattern) {
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
                    boolean lenientParse = true;

                    // Peek ahead to next token.
                    if (i + 1 < length) {
                        indexRef[0]++;
                        if (isNumericToken(parseToken(pattern, indexRef))) {
                            // If next token is a number, cannot support
                            // lenient parse, because it will consume digits
                            // that it should not.
                            lenientParse = false;
                        }
                        indexRef[0]--;
                    }

                    // Use pivots which are compatible with SimpleDateFormat.
                    switch (c) {
                    case 'x':
                        builder.appendTwoDigitWeekyear
                            (new DateTime().getWeekyear() - 30, lenientParse);
                        break;
                    case 'y':
                    case 'Y':
                    default:
                        builder.appendTwoDigitYear(new DateTime().getYear() - 30, lenientParse);
                        break;
                    }
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
            case 'a': // am/pm marker (text)
                builder.appendHalfdayOfDayText();
                break;
            case 'h': // clockhour of halfday (number, 1..12)
                builder.appendClockhourOfHalfday(tokenLen);
                break;
            case 'H': // hour of day (number, 0..23)
                builder.appendHourOfDay(tokenLen);
                break;
            case 'k': // clockhour of day (1..24)
                builder.appendClockhourOfDay(tokenLen);
                break;
            case 'K': // hour of halfday (0..11)
                builder.appendHourOfHalfday(tokenLen);
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
            case 'z': // time zone (text)
                if (tokenLen >= 4) {
                    builder.appendTimeZoneName();
                } else {
                    builder.appendTimeZoneShortName(null);
                }
                break;
            case 'Z': // time zone offset
                if (tokenLen == 1) {
                    builder.appendTimeZoneOffset(null, "Z", false, 2, 2);
                } else if (tokenLen == 2) {
                    builder.appendTimeZoneOffset(null, "Z", true, 2, 2);
                } else {
                    builder.appendTimeZoneId();
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

    /**
     * Parses an individual token.
     * 
     * @param pattern  the pattern string
     * @param indexRef  a single element array, where the input is the start
     *  location and the output is the location after parsing the token
     * @return the parsed token
     */
    private static String parseToken(String pattern, int[] indexRef) {
        StringBuilder buf = new StringBuilder();

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

    /**
     * Returns true if token should be parsed as a numeric field.
     * 
     * @param token  the token to parse
     * @return true if numeric field
     */
    private static boolean isNumericToken(String token) {
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
    /**
     * Select a format from a custom pattern.
     *
     * @param pattern  pattern specification
     * @throws IllegalArgumentException if the pattern is invalid
     * @see #appendPatternTo
     */
    private static DateTimeFormatter createFormatterForPattern(String pattern) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("Invalid pattern specification");
        }
        DateTimeFormatter formatter = null;
        synchronized (PATTERN_CACHE) {
            formatter = PATTERN_CACHE.get(pattern);
            if (formatter == null) {
                DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
                parsePatternTo(builder, pattern);
                formatter = builder.toFormatter();

                PATTERN_CACHE.put(pattern, formatter);
            }
        }
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
    private static DateTimeFormatter createFormatterForStyle(String style) {
        if (style == null || style.length() != 2) {
            throw new IllegalArgumentException("Invalid style specification: " + style);
        }
        int dateStyle = selectStyle(style.charAt(0));
        int timeStyle = selectStyle(style.charAt(1));
        if (dateStyle == NONE && timeStyle == NONE) {
            throw new IllegalArgumentException("Style '--' is invalid");
        }
        return createFormatterForStyleIndex(dateStyle, timeStyle);
    }

    /**
     * Gets the formatter for the specified style.
     * 
     * @param dateStyle  the date style
     * @param timeStyle  the time style
     * @return the formatter
     */
    private static DateTimeFormatter createFormatterForStyleIndex(int dateStyle, int timeStyle) {
        int index = ((dateStyle << 2) + dateStyle) + timeStyle;
        // Should never happen but do a double check...
        if (index >= STYLE_CACHE.length) {
            return createDateTimeFormatter(dateStyle, timeStyle);
        }
        DateTimeFormatter f = null;
        synchronized (STYLE_CACHE) {
            f = STYLE_CACHE[index];
            if (f == null) {
                f = createDateTimeFormatter(dateStyle, timeStyle);
                STYLE_CACHE[index] = f;
            }
        }
        return f;
    }
    
    /**
     * Creates a formatter for the specified style.
     * @param dateStyle  the date style
     * @param timeStyle  the time style
     * @return the formatter
     */
    private static DateTimeFormatter createDateTimeFormatter(int dateStyle, int timeStyle){
        int type = DATETIME;
        if (dateStyle == NONE) {
            type = TIME;
        } else if (timeStyle == NONE) {
            type = DATE;
        }
        StyleFormatter llf = new StyleFormatter(dateStyle, timeStyle, type);
        return new DateTimeFormatter(llf, llf);
    }

    /**
     * Gets the JDK style code from the Joda code.
     * 
     * @param ch  the Joda style code
     * @return the JDK style code
     */
    private static int selectStyle(char ch) {
        switch (ch) {
        case 'S':
            return SHORT;
        case 'M':
            return MEDIUM;
        case 'L':
            return LONG;
        case 'F':
            return FULL;
        case '-':
            return NONE;
        default:
            throw new IllegalArgumentException("Invalid style character: " + ch);
        }
    }

    //-----------------------------------------------------------------------
    static class StyleFormatter
            implements DateTimePrinter, DateTimeParser {

        private static final Map<String, DateTimeFormatter> cCache = new HashMap<String, DateTimeFormatter>();  // manual sync
        
        private final int iDateStyle;
        private final int iTimeStyle;
        private final int iType;

        StyleFormatter(int dateStyle, int timeStyle, int type) {
            super();
            iDateStyle = dateStyle;
            iTimeStyle = timeStyle;
            iType = type;
        }

        public int estimatePrintedLength() {
            return 40;  // guess
        }

        public void printTo(
                StringBuffer buf, long instant, Chronology chrono,
                int displayOffset, DateTimeZone displayZone, Locale locale) {
            DateTimePrinter p = getFormatter(locale).getPrinter();
            p.printTo(buf, instant, chrono, displayOffset, displayZone, locale);
        }

        public void printTo(
                Writer out, long instant, Chronology chrono,
                int displayOffset, DateTimeZone displayZone, Locale locale) throws IOException {
            DateTimePrinter p = getFormatter(locale).getPrinter();
            p.printTo(out, instant, chrono, displayOffset, displayZone, locale);
        }

        public void printTo(StringBuffer buf, ReadablePartial partial, Locale locale) {
            DateTimePrinter p = getFormatter(locale).getPrinter();
            p.printTo(buf, partial, locale);
        }

        public void printTo(Writer out, ReadablePartial partial, Locale locale) throws IOException {
            DateTimePrinter p = getFormatter(locale).getPrinter();
            p.printTo(out, partial, locale);
        }

        public int estimateParsedLength() {
            return 40;  // guess
        }

        public int parseInto(DateTimeParserBucket bucket, String text, int position) {
            DateTimeParser p = getFormatter(bucket.getLocale()).getParser();
            return p.parseInto(bucket, text, position);
        }

        private DateTimeFormatter getFormatter(Locale locale) {
            locale = (locale == null ? Locale.getDefault() : locale);
            String key = Integer.toString(iType + (iDateStyle << 4) + (iTimeStyle << 8)) + locale.toString();
            DateTimeFormatter f = null;
            synchronized (cCache) {
                f = cCache.get(key);
                if (f == null) {
                    String pattern = getPattern(locale);
                    f = DateTimeFormat.forPattern(pattern);
                    cCache.put(key, f);
                }
            }
            return f;
        }

        String getPattern(Locale locale) {
            DateFormat f = null;
            switch (iType) {
                case DATE:
                    f = DateFormat.getDateInstance(iDateStyle, locale);
                    break;
                case TIME:
                    f = DateFormat.getTimeInstance(iTimeStyle, locale);
                    break;
                case DATETIME:
                    f = DateFormat.getDateTimeInstance(iDateStyle, iTimeStyle, locale);
                    break;
            }
            if (f instanceof SimpleDateFormat == false) {
                throw new IllegalArgumentException("No datetime pattern for locale: " + locale);
            }
            return ((SimpleDateFormat) f).toPattern();
        }
    }

}
