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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableDateTime.Property;
import org.joda.time.ReadablePartial;
import org.joda.time.field.MillisDurationField;
import org.joda.time.field.PreciseDateTimeField;

/**
 * Factory that creates complex instances of DateTimeFormatter via method calls.
 * <p>
 * Datetime formatting is performed by the {@link DateTimeFormatter} class.
 * Three classes provide factory methods to create formatters, and this is one.
 * The others are {@link DateTimeFormat} and {@link ISODateTimeFormat}.
 * <p>
 * DateTimeFormatterBuilder is used for constructing formatters which are then
 * used to print or parse. The formatters are built by appending specific fields
 * or other formatters to an instance of this builder.
 * <p>
 * For example, a formatter that prints month and year, like "January 1970",
 * can be constructed as follows:
 * <p>
 * <pre>
 * DateTimeFormatter monthAndYear = new DateTimeFormatterBuilder()
 *     .appendMonthOfYearText()
 *     .appendLiteral(' ')
 *     .appendYear(4, 4)
 *     .toFormatter();
 * </pre>
 * <p>
 * DateTimeFormatterBuilder itself is mutable and not thread-safe, but the
 * formatters that it builds are thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @author Fredrik Borgh
 * @since 1.0
 * @see DateTimeFormat
 * @see ISODateTimeFormat
 */
public class DateTimeFormatterBuilder {

    /** Array of printers and parsers (alternating). */
    private ArrayList<Object> iElementPairs;
    /** Cache of the last returned formatter. */
    private Object iFormatter;

    //-----------------------------------------------------------------------
    /**
     * Creates a DateTimeFormatterBuilder.
     */
    public DateTimeFormatterBuilder() {
        super();
        iElementPairs = new ArrayList<Object>();
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs a DateTimeFormatter using all the appended elements.
     * <p>
     * This is the main method used by applications at the end of the build
     * process to create a usable formatter.
     * <p>
     * Subsequent changes to this builder do not affect the returned formatter.
     * <p>
     * The returned formatter may not support both printing and parsing.
     * The methods {@link DateTimeFormatter#isPrinter()} and
     * {@link DateTimeFormatter#isParser()} will help you determine the state
     * of the formatter.
     *
     * @throws UnsupportedOperationException if neither printing nor parsing is supported
     */
    public DateTimeFormatter toFormatter() {
        Object f = getFormatter();
        InternalPrinter printer = null;
        if (isPrinter(f)) {
            printer = (InternalPrinter) f;
        }
        InternalParser parser = null;
        if (isParser(f)) {
            parser = (InternalParser) f;
        }
        if (printer != null || parser != null) {
            return new DateTimeFormatter(printer, parser);
        }
        throw new UnsupportedOperationException("Both printing and parsing not supported");
    }

    /**
     * Internal method to create a DateTimePrinter instance using all the
     * appended elements.
     * <p>
     * Most applications will not use this method.
     * If you want a printer in an application, call {@link #toFormatter()}
     * and just use the printing API.
     * <p>
     * Subsequent changes to this builder do not affect the returned printer.
     *
     * @throws UnsupportedOperationException if printing is not supported
     */
    public DateTimePrinter toPrinter() {
        Object f = getFormatter();
        if (isPrinter(f)) {
            InternalPrinter ip = (InternalPrinter) f;
            return InternalPrinterDateTimePrinter.of(ip);
        }
        throw new UnsupportedOperationException("Printing is not supported");
    }

    /**
     * Internal method to create a DateTimeParser instance using all the
     * appended elements.
     * <p>
     * Most applications will not use this method.
     * If you want a parser in an application, call {@link #toFormatter()}
     * and just use the parsing API.
     * <p>
     * Subsequent changes to this builder do not affect the returned parser.
     *
     * @throws UnsupportedOperationException if parsing is not supported
     */
    public DateTimeParser toParser() {
        Object f = getFormatter();
        if (isParser(f)) {
            InternalParser ip = (InternalParser) f;
            return InternalParserDateTimeParser.of(ip);
        }
        throw new UnsupportedOperationException("Parsing is not supported");
    }

    //-----------------------------------------------------------------------
    /**
     * Returns true if toFormatter can be called without throwing an
     * UnsupportedOperationException.
     * 
     * @return true if a formatter can be built
     */
    public boolean canBuildFormatter() {
        return isFormatter(getFormatter());
    }

    /**
     * Returns true if toPrinter can be called without throwing an
     * UnsupportedOperationException.
     * 
     * @return true if a printer can be built
     */
    public boolean canBuildPrinter() {
        return isPrinter(getFormatter());
    }

    /**
     * Returns true if toParser can be called without throwing an
     * UnsupportedOperationException.
     * 
     * @return true if a parser can be built
     */
    public boolean canBuildParser() {
        return isParser(getFormatter());
    }

    //-----------------------------------------------------------------------
    /**
     * Clears out all the appended elements, allowing this builder to be
     * reused.
     */
    public void clear() {
        iFormatter = null;
        iElementPairs.clear();
    }

    //-----------------------------------------------------------------------
    /**
     * Appends another formatter.
     * <p>
     * This extracts the underlying printer and parser and appends them
     * The printer and parser interfaces are the low-level part of the formatting API.
     * Normally, instances are extracted from another formatter.
     * Note however that any formatter specific information, such as the locale,
     * time-zone, chronology, offset parsing or pivot/default year, will not be
     * extracted by this method.
     *
     * @param formatter  the formatter to add
     * @return this DateTimeFormatterBuilder, for chaining
     * @throws IllegalArgumentException if formatter is null or of an invalid type
     */
    public DateTimeFormatterBuilder append(DateTimeFormatter formatter) {
        if (formatter == null) {
            throw new IllegalArgumentException("No formatter supplied");
        }
        return append0(formatter.getPrinter0(), formatter.getParser0());
    }

    /**
     * Appends just a printer. With no matching parser, a parser cannot be
     * built from this DateTimeFormatterBuilder.
     * <p>
     * The printer interface is part of the low-level part of the formatting API.
     * Normally, instances are extracted from another formatter.
     * Note however that any formatter specific information, such as the locale,
     * time-zone, chronology, offset parsing or pivot/default year, will not be
     * extracted by this method.
     *
     * @param printer  the printer to add, not null
     * @return this DateTimeFormatterBuilder, for chaining
     * @throws IllegalArgumentException if printer is null or of an invalid type
     */
    public DateTimeFormatterBuilder append(DateTimePrinter printer) {
        checkPrinter(printer);
        return append0(DateTimePrinterInternalPrinter.of(printer), null);
    }

    /**
     * Appends just a parser. With no matching printer, a printer cannot be
     * built from this builder.
     * <p>
     * The parser interface is part of the low-level part of the formatting API.
     * Normally, instances are extracted from another formatter.
     * Note however that any formatter specific information, such as the locale,
     * time-zone, chronology, offset parsing or pivot/default year, will not be
     * extracted by this method.
     *
     * @param parser  the parser to add, not null
     * @return this DateTimeFormatterBuilder, for chaining
     * @throws IllegalArgumentException if parser is null or of an invalid type
     */
    public DateTimeFormatterBuilder append(DateTimeParser parser) {
        checkParser(parser);
        return append0(null, DateTimeParserInternalParser.of(parser));
    }

    /**
     * Appends a printer/parser pair.
     * <p>
     * The printer and parser interfaces are the low-level part of the formatting API.
     * Normally, instances are extracted from another formatter.
     * Note however that any formatter specific information, such as the locale,
     * time-zone, chronology, offset parsing or pivot/default year, will not be
     * extracted by this method.
     *
     * @param printer  the printer to add, not null
     * @param parser  the parser to add, not null
     * @return this DateTimeFormatterBuilder, for chaining
     * @throws IllegalArgumentException if printer or parser is null or of an invalid type
     */
    public DateTimeFormatterBuilder append(DateTimePrinter printer, DateTimeParser parser) {
        checkPrinter(printer);
        checkParser(parser);
        return append0(DateTimePrinterInternalPrinter.of(printer), DateTimeParserInternalParser.of(parser));
    }

    /**
     * Appends a printer and a set of matching parsers. When parsing, the first
     * parser in the list is selected for parsing. If it fails, the next is
     * chosen, and so on. If none of these parsers succeeds, then the failed
     * position of the parser that made the greatest progress is returned.
     * <p>
     * Only the printer is optional. In addition, it is illegal for any but the
     * last of the parser array elements to be null. If the last element is
     * null, this represents the empty parser. The presence of an empty parser
     * indicates that the entire array of parse formats is optional.
     * <p>
     * The printer and parser interfaces are the low-level part of the formatting API.
     * Normally, instances are extracted from another formatter.
     * Note however that any formatter specific information, such as the locale,
     * time-zone, chronology, offset parsing or pivot/default year, will not be
     * extracted by this method.
     *
     * @param printer  the printer to add
     * @param parsers  the parsers to add
     * @return this DateTimeFormatterBuilder, for chaining
     * @throws IllegalArgumentException if any printer or parser is of an invalid type
     * @throws IllegalArgumentException if any parser element but the last is null
     */
    public DateTimeFormatterBuilder append(DateTimePrinter printer, DateTimeParser[] parsers) {
        if (printer != null) {
            checkPrinter(printer);
        }
        if (parsers == null) {
            throw new IllegalArgumentException("No parsers supplied");
        }
        int length = parsers.length;
        if (length == 1) {
            if (parsers[0] == null) {
                throw new IllegalArgumentException("No parser supplied");
            }
            return append0(DateTimePrinterInternalPrinter.of(printer), DateTimeParserInternalParser.of(parsers[0]));
        }

        InternalParser[] copyOfParsers = new InternalParser[length];
        int i;
        for (i = 0; i < length - 1; i++) {
            if ((copyOfParsers[i] = DateTimeParserInternalParser.of(parsers[i])) == null) {
                throw new IllegalArgumentException("Incomplete parser array");
            }
        }
        copyOfParsers[i] = DateTimeParserInternalParser.of(parsers[i]);

        return append0(DateTimePrinterInternalPrinter.of(printer), new MatchingParser(copyOfParsers));
    }

    /**
     * Appends just a parser element which is optional. With no matching
     * printer, a printer cannot be built from this DateTimeFormatterBuilder.
     * <p>
     * The parser interface is part of the low-level part of the formatting API.
     * Normally, instances are extracted from another formatter.
     * Note however that any formatter specific information, such as the locale,
     * time-zone, chronology, offset parsing or pivot/default year, will not be
     * extracted by this method.
     *
     * @return this DateTimeFormatterBuilder, for chaining
     * @throws IllegalArgumentException if parser is null or of an invalid type
     */
    public DateTimeFormatterBuilder appendOptional(DateTimeParser parser) {
        checkParser(parser);
        InternalParser[] parsers = new InternalParser[] {DateTimeParserInternalParser.of(parser), null};
        return append0(null, new MatchingParser(parsers));
    }

    //-----------------------------------------------------------------------
    /**
     * Checks if the parser is non null and a provider.
     * 
     * @param parser  the parser to check
     */
    private void checkParser(DateTimeParser parser) {
        if (parser == null) {
            throw new IllegalArgumentException("No parser supplied");
        }
    }

    /**
     * Checks if the printer is non null and a provider.
     * 
     * @param printer  the printer to check
     */
    private void checkPrinter(DateTimePrinter printer) {
        if (printer == null) {
            throw new IllegalArgumentException("No printer supplied");
        }
    }

    private DateTimeFormatterBuilder append0(Object element) {
        iFormatter = null;
        // Add the element as both a printer and parser.
        iElementPairs.add(element);
        iElementPairs.add(element);
        return this;
    }

    private DateTimeFormatterBuilder append0(
            InternalPrinter printer, InternalParser parser) {
        iFormatter = null;
        iElementPairs.add(printer);
        iElementPairs.add(parser);
        return this;
    }

    //-----------------------------------------------------------------------
    /**
     * Instructs the printer to emit a specific character, and the parser to
     * expect it. The parser is case-insensitive.
     *
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendLiteral(char c) {
        return append0(new CharacterLiteral(c));
    }

    /**
     * Instructs the printer to emit specific text, and the parser to expect
     * it. The parser is case-insensitive.
     *
     * @return this DateTimeFormatterBuilder, for chaining
     * @throws IllegalArgumentException if text is null
     */
    public DateTimeFormatterBuilder appendLiteral(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Literal must not be null");
        }
        switch (text.length()) {
            case 0:
                return this;
            case 1:
                return append0(new CharacterLiteral(text.charAt(0)));
            default:
                return append0(new StringLiteral(text));
        }
    }

    /**
     * Instructs the printer to emit a field value as a decimal number, and the
     * parser to expect an unsigned decimal number.
     *
     * @param fieldType  type of field to append
     * @param minDigits  minimum number of digits to <i>print</i>
     * @param maxDigits  maximum number of digits to <i>parse</i>, or the estimated
     * maximum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     * @throws IllegalArgumentException if field type is null
     */
    public DateTimeFormatterBuilder appendDecimal(
            DateTimeFieldType fieldType, int minDigits, int maxDigits) {
        if (fieldType == null) {
            throw new IllegalArgumentException("Field type must not be null");
        }
        if (maxDigits < minDigits) {
            maxDigits = minDigits;
        }
        if (minDigits < 0 || maxDigits <= 0) {
            throw new IllegalArgumentException();
        }
        if (minDigits <= 1) {
            return append0(new UnpaddedNumber(fieldType, maxDigits, false));
        } else {
            return append0(new PaddedNumber(fieldType, maxDigits, false, minDigits));
        }
    }

    /**
     * Instructs the printer to emit a field value as a fixed-width decimal
     * number (smaller numbers will be left-padded with zeros), and the parser
     * to expect an unsigned decimal number with the same fixed width.
     * 
     * @param fieldType  type of field to append
     * @param numDigits  the exact number of digits to parse or print, except if
     * printed value requires more digits
     * @return this DateTimeFormatterBuilder, for chaining
     * @throws IllegalArgumentException if field type is null or if <code>numDigits <= 0</code>
     * @since 1.5
     */
    public DateTimeFormatterBuilder appendFixedDecimal(
            DateTimeFieldType fieldType, int numDigits) {
        if (fieldType == null) {
            throw new IllegalArgumentException("Field type must not be null");
        }
        if (numDigits <= 0) {
            throw new IllegalArgumentException("Illegal number of digits: " + numDigits);
        }
        return append0(new FixedNumber(fieldType, numDigits, false));
    }

    /**
     * Instructs the printer to emit a field value as a decimal number, and the
     * parser to expect a signed decimal number.
     *
     * @param fieldType  type of field to append
     * @param minDigits  minimum number of digits to <i>print</i>
     * @param maxDigits  maximum number of digits to <i>parse</i>, or the estimated
     * maximum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     * @throws IllegalArgumentException if field type is null
     */
    public DateTimeFormatterBuilder appendSignedDecimal(
            DateTimeFieldType fieldType, int minDigits, int maxDigits) {
        if (fieldType == null) {
            throw new IllegalArgumentException("Field type must not be null");
        }
        if (maxDigits < minDigits) {
            maxDigits = minDigits;
        }
        if (minDigits < 0 || maxDigits <= 0) {
            throw new IllegalArgumentException();
        }
        if (minDigits <= 1) {
            return append0(new UnpaddedNumber(fieldType, maxDigits, true));
        } else {
            return append0(new PaddedNumber(fieldType, maxDigits, true, minDigits));
        }
    }

    /**
     * Instructs the printer to emit a field value as a fixed-width decimal
     * number (smaller numbers will be left-padded with zeros), and the parser
     * to expect an signed decimal number with the same fixed width.
     * 
     * @param fieldType  type of field to append
     * @param numDigits  the exact number of digits to parse or print, except if
     * printed value requires more digits
     * @return this DateTimeFormatterBuilder, for chaining
     * @throws IllegalArgumentException if field type is null or if <code>numDigits <= 0</code>
     * @since 1.5
     */
    public DateTimeFormatterBuilder appendFixedSignedDecimal(
            DateTimeFieldType fieldType, int numDigits) {
        if (fieldType == null) {
            throw new IllegalArgumentException("Field type must not be null");
        }
        if (numDigits <= 0) {
            throw new IllegalArgumentException("Illegal number of digits: " + numDigits);
        }
        return append0(new FixedNumber(fieldType, numDigits, true));
    }

    /**
     * Instructs the printer to emit a field value as text, and the
     * parser to expect text.
     *
     * @param fieldType  type of field to append
     * @return this DateTimeFormatterBuilder, for chaining
     * @throws IllegalArgumentException if field type is null
     */
    public DateTimeFormatterBuilder appendText(DateTimeFieldType fieldType) {
        if (fieldType == null) {
            throw new IllegalArgumentException("Field type must not be null");
        }
        return append0(new TextField(fieldType, false));
    }

    /**
     * Instructs the printer to emit a field value as short text, and the
     * parser to expect text.
     *
     * @param fieldType  type of field to append
     * @return this DateTimeFormatterBuilder, for chaining
     * @throws IllegalArgumentException if field type is null
     */
    public DateTimeFormatterBuilder appendShortText(DateTimeFieldType fieldType) {
        if (fieldType == null) {
            throw new IllegalArgumentException("Field type must not be null");
        }
        return append0(new TextField(fieldType, true));
    }

    /**
     * Instructs the printer to emit a remainder of time as a decimal fraction,
     * without decimal point. For example, if the field is specified as
     * minuteOfHour and the time is 12:30:45, the value printed is 75. A
     * decimal point is implied, so the fraction is 0.75, or three-quarters of
     * a minute.
     *
     * @param fieldType  type of field to append
     * @param minDigits  minimum number of digits to print.
     * @param maxDigits  maximum number of digits to print or parse.
     * @return this DateTimeFormatterBuilder, for chaining
     * @throws IllegalArgumentException if field type is null
     */
    public DateTimeFormatterBuilder appendFraction(
            DateTimeFieldType fieldType, int minDigits, int maxDigits) {
        if (fieldType == null) {
            throw new IllegalArgumentException("Field type must not be null");
        }
        if (maxDigits < minDigits) {
            maxDigits = minDigits;
        }
        if (minDigits < 0 || maxDigits <= 0) {
            throw new IllegalArgumentException();
        }
        return append0(new Fraction(fieldType, minDigits, maxDigits));
    }

    /**
     * Appends the print/parse of a fractional second.
     * <p>
     * This reliably handles the case where fractional digits are being handled
     * beyond a visible decimal point. The digits parsed will always be treated
     * as the most significant (numerically largest) digits.
     * Thus '23' will be parsed as 230 milliseconds.
     * Contrast this behaviour to {@link #appendMillisOfSecond}.
     * This method does not print or parse the decimal point itself.
     * 
     * @param minDigits  minimum number of digits to print
     * @param maxDigits  maximum number of digits to print or parse
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendFractionOfSecond(int minDigits, int maxDigits) {
        return appendFraction(DateTimeFieldType.secondOfDay(), minDigits, maxDigits);
    }

    /**
     * Appends the print/parse of a fractional minute.
     * <p>
     * This reliably handles the case where fractional digits are being handled
     * beyond a visible decimal point. The digits parsed will always be treated
     * as the most significant (numerically largest) digits.
     * Thus '23' will be parsed as 0.23 minutes (converted to milliseconds).
     * This method does not print or parse the decimal point itself.
     * 
     * @param minDigits  minimum number of digits to print
     * @param maxDigits  maximum number of digits to print or parse
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendFractionOfMinute(int minDigits, int maxDigits) {
        return appendFraction(DateTimeFieldType.minuteOfDay(), minDigits, maxDigits);
    }

    /**
     * Appends the print/parse of a fractional hour.
     * <p>
     * This reliably handles the case where fractional digits are being handled
     * beyond a visible decimal point. The digits parsed will always be treated
     * as the most significant (numerically largest) digits.
     * Thus '23' will be parsed as 0.23 hours (converted to milliseconds).
     * This method does not print or parse the decimal point itself.
     * 
     * @param minDigits  minimum number of digits to print
     * @param maxDigits  maximum number of digits to print or parse
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendFractionOfHour(int minDigits, int maxDigits) {
        return appendFraction(DateTimeFieldType.hourOfDay(), minDigits, maxDigits);
    }

    /**
     * Appends the print/parse of a fractional day.
     * <p>
     * This reliably handles the case where fractional digits are being handled
     * beyond a visible decimal point. The digits parsed will always be treated
     * as the most significant (numerically largest) digits.
     * Thus '23' will be parsed as 0.23 days (converted to milliseconds).
     * This method does not print or parse the decimal point itself.
     * 
     * @param minDigits  minimum number of digits to print
     * @param maxDigits  maximum number of digits to print or parse
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendFractionOfDay(int minDigits, int maxDigits) {
        return appendFraction(DateTimeFieldType.dayOfYear(), minDigits, maxDigits);
    }

    /**
     * Instructs the printer to emit a numeric millisOfSecond field.
     * <p>
     * This method will append a field that prints a three digit value.
     * During parsing the value that is parsed is assumed to be three digits.
     * If less than three digits are present then they will be counted as the
     * smallest parts of the millisecond. This is probably not what you want
     * if you are using the field as a fraction. Instead, a fractional
     * millisecond should be produced using {@link #appendFractionOfSecond}.
     *
     * @param minDigits  minimum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendMillisOfSecond(int minDigits) {
        return appendDecimal(DateTimeFieldType.millisOfSecond(), minDigits, 3);
    }

    /**
     * Instructs the printer to emit a numeric millisOfDay field.
     *
     * @param minDigits  minimum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendMillisOfDay(int minDigits) {
        return appendDecimal(DateTimeFieldType.millisOfDay(), minDigits, 8);
    }

    /**
     * Instructs the printer to emit a numeric secondOfMinute field.
     *
     * @param minDigits  minimum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendSecondOfMinute(int minDigits) {
        return appendDecimal(DateTimeFieldType.secondOfMinute(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric secondOfDay field.
     *
     * @param minDigits  minimum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendSecondOfDay(int minDigits) {
        return appendDecimal(DateTimeFieldType.secondOfDay(), minDigits, 5);
    }

    /**
     * Instructs the printer to emit a numeric minuteOfHour field.
     *
     * @param minDigits  minimum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendMinuteOfHour(int minDigits) {
        return appendDecimal(DateTimeFieldType.minuteOfHour(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric minuteOfDay field.
     *
     * @param minDigits  minimum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendMinuteOfDay(int minDigits) {
        return appendDecimal(DateTimeFieldType.minuteOfDay(), minDigits, 4);
    }

    /**
     * Instructs the printer to emit a numeric hourOfDay field.
     *
     * @param minDigits  minimum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendHourOfDay(int minDigits) {
        return appendDecimal(DateTimeFieldType.hourOfDay(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric clockhourOfDay field.
     *
     * @param minDigits minimum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendClockhourOfDay(int minDigits) {
        return appendDecimal(DateTimeFieldType.clockhourOfDay(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric hourOfHalfday field.
     *
     * @param minDigits  minimum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendHourOfHalfday(int minDigits) {
        return appendDecimal(DateTimeFieldType.hourOfHalfday(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric clockhourOfHalfday field.
     *
     * @param minDigits  minimum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendClockhourOfHalfday(int minDigits) {
        return appendDecimal(DateTimeFieldType.clockhourOfHalfday(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric dayOfWeek field.
     *
     * @param minDigits  minimum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendDayOfWeek(int minDigits) {
        return appendDecimal(DateTimeFieldType.dayOfWeek(), minDigits, 1);
    }

    /**
     * Instructs the printer to emit a numeric dayOfMonth field.
     *
     * @param minDigits  minimum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendDayOfMonth(int minDigits) {
        return appendDecimal(DateTimeFieldType.dayOfMonth(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric dayOfYear field.
     *
     * @param minDigits  minimum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendDayOfYear(int minDigits) {
        return appendDecimal(DateTimeFieldType.dayOfYear(), minDigits, 3);
    }

    /**
     * Instructs the printer to emit a numeric weekOfWeekyear field.
     *
     * @param minDigits  minimum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendWeekOfWeekyear(int minDigits) {
        return appendDecimal(DateTimeFieldType.weekOfWeekyear(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric weekyear field.
     *
     * @param minDigits  minimum number of digits to <i>print</i>
     * @param maxDigits  maximum number of digits to <i>parse</i>, or the estimated
     * maximum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendWeekyear(int minDigits, int maxDigits) {
        return appendSignedDecimal(DateTimeFieldType.weekyear(), minDigits, maxDigits);
    }

    /**
     * Instructs the printer to emit a numeric monthOfYear field.
     *
     * @param minDigits  minimum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendMonthOfYear(int minDigits) {
        return appendDecimal(DateTimeFieldType.monthOfYear(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric year field.
     *
     * @param minDigits  minimum number of digits to <i>print</i>
     * @param maxDigits  maximum number of digits to <i>parse</i>, or the estimated
     * maximum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendYear(int minDigits, int maxDigits) {
        return appendSignedDecimal(DateTimeFieldType.year(), minDigits, maxDigits);
    }

    /**
     * Instructs the printer to emit a numeric year field which always prints
     * and parses two digits. A pivot year is used during parsing to determine
     * the range of supported years as <code>(pivot - 50) .. (pivot + 49)</code>.
     *
     * <pre>
     * pivot   supported range   00 is   20 is   40 is   60 is   80 is
     * ---------------------------------------------------------------
     * 1950      1900..1999      1900    1920    1940    1960    1980
     * 1975      1925..2024      2000    2020    1940    1960    1980
     * 2000      1950..2049      2000    2020    2040    1960    1980
     * 2025      1975..2074      2000    2020    2040    2060    1980
     * 2050      2000..2099      2000    2020    2040    2060    2080
     * </pre>
     *
     * @param pivot  pivot year to use when parsing
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendTwoDigitYear(int pivot) {
        return appendTwoDigitYear(pivot, false);
    }

    /**
     * Instructs the printer to emit a numeric year field which always prints
     * two digits. A pivot year is used during parsing to determine the range
     * of supported years as <code>(pivot - 50) .. (pivot + 49)</code>. If
     * parse is instructed to be lenient and the digit count is not two, it is
     * treated as an absolute year. With lenient parsing, specifying a positive
     * or negative sign before the year also makes it absolute.
     *
     * @param pivot  pivot year to use when parsing
     * @param lenientParse  when true, if digit count is not two, it is treated
     * as an absolute year
     * @return this DateTimeFormatterBuilder, for chaining
     * @since 1.1
     */
    public DateTimeFormatterBuilder appendTwoDigitYear(int pivot, boolean lenientParse) {
        return append0(new TwoDigitYear(DateTimeFieldType.year(), pivot, lenientParse));
    }

    /**
     * Instructs the printer to emit a numeric weekyear field which always prints
     * and parses two digits. A pivot year is used during parsing to determine
     * the range of supported years as <code>(pivot - 50) .. (pivot + 49)</code>.
     *
     * <pre>
     * pivot   supported range   00 is   20 is   40 is   60 is   80 is
     * ---------------------------------------------------------------
     * 1950      1900..1999      1900    1920    1940    1960    1980
     * 1975      1925..2024      2000    2020    1940    1960    1980
     * 2000      1950..2049      2000    2020    2040    1960    1980
     * 2025      1975..2074      2000    2020    2040    2060    1980
     * 2050      2000..2099      2000    2020    2040    2060    2080
     * </pre>
     *
     * @param pivot  pivot weekyear to use when parsing
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendTwoDigitWeekyear(int pivot) {
        return appendTwoDigitWeekyear(pivot, false);
    }

    /**
     * Instructs the printer to emit a numeric weekyear field which always prints
     * two digits. A pivot year is used during parsing to determine the range
     * of supported years as <code>(pivot - 50) .. (pivot + 49)</code>. If
     * parse is instructed to be lenient and the digit count is not two, it is
     * treated as an absolute weekyear. With lenient parsing, specifying a positive
     * or negative sign before the weekyear also makes it absolute.
     *
     * @param pivot  pivot weekyear to use when parsing
     * @param lenientParse  when true, if digit count is not two, it is treated
     * as an absolute weekyear
     * @return this DateTimeFormatterBuilder, for chaining
     * @since 1.1
     */
    public DateTimeFormatterBuilder appendTwoDigitWeekyear(int pivot, boolean lenientParse) {
        return append0(new TwoDigitYear(DateTimeFieldType.weekyear(), pivot, lenientParse));
    }

    /**
     * Instructs the printer to emit a numeric yearOfEra field.
     *
     * @param minDigits  minimum number of digits to <i>print</i>
     * @param maxDigits  maximum number of digits to <i>parse</i>, or the estimated
     * maximum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendYearOfEra(int minDigits, int maxDigits) {
        return appendDecimal(DateTimeFieldType.yearOfEra(), minDigits, maxDigits);
    }

    /**
     * Instructs the printer to emit a numeric year of century field.
     *
     * @param minDigits  minimum number of digits to print
     * @param maxDigits  maximum number of digits to <i>parse</i>, or the estimated
     * maximum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendYearOfCentury(int minDigits, int maxDigits) {
        return appendDecimal(DateTimeFieldType.yearOfCentury(), minDigits, maxDigits);
    }

    /**
     * Instructs the printer to emit a numeric century of era field.
     *
     * @param minDigits  minimum number of digits to print
     * @param maxDigits  maximum number of digits to <i>parse</i>, or the estimated
     * maximum number of digits to print
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendCenturyOfEra(int minDigits, int maxDigits) {
        return appendSignedDecimal(DateTimeFieldType.centuryOfEra(), minDigits, maxDigits);
    }

    /**
     * Instructs the printer to emit a locale-specific AM/PM text, and the
     * parser to expect it. The parser is case-insensitive.
     *
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendHalfdayOfDayText() {
        return appendText(DateTimeFieldType.halfdayOfDay());
    }

    /**
     * Instructs the printer to emit a locale-specific dayOfWeek text. The
     * parser will accept a long or short dayOfWeek text, case-insensitive.
     *
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendDayOfWeekText() {
        return appendText(DateTimeFieldType.dayOfWeek());
    }

    /**
     * Instructs the printer to emit a short locale-specific dayOfWeek
     * text. The parser will accept a long or short dayOfWeek text,
     * case-insensitive.
     *
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendDayOfWeekShortText() {
        return appendShortText(DateTimeFieldType.dayOfWeek());
    }

    /**
     * Instructs the printer to emit a short locale-specific monthOfYear
     * text. The parser will accept a long or short monthOfYear text,
     * case-insensitive.
     *
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendMonthOfYearText() { 
        return appendText(DateTimeFieldType.monthOfYear());
    }

    /**
     * Instructs the printer to emit a locale-specific monthOfYear text. The
     * parser will accept a long or short monthOfYear text, case-insensitive.
     *
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendMonthOfYearShortText() {
        return appendShortText(DateTimeFieldType.monthOfYear());
    }

    /**
     * Instructs the printer to emit a locale-specific era text (BC/AD), and
     * the parser to expect it. The parser is case-insensitive.
     *
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendEraText() {
        return appendText(DateTimeFieldType.era());
    }

    /**
     * Instructs the printer to emit a locale-specific time zone name.
     * Using this method prevents parsing, because time zone names are not unique.
     * See {@link #appendTimeZoneName(Map)}.
     *
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendTimeZoneName() {
        return append0(new TimeZoneName(TimeZoneName.LONG_NAME, null), null);
    }

    /**
     * Instructs the printer to emit a locale-specific time zone name, providing a lookup for parsing.
     * Time zone names are not unique, thus the API forces you to supply the lookup.
     * The names are searched in the order of the map, thus it is strongly recommended
     * to use a {@code LinkedHashMap} or similar.
     *
     * @param parseLookup  the table of names, not null
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendTimeZoneName(Map<String, DateTimeZone> parseLookup) {
        TimeZoneName pp = new TimeZoneName(TimeZoneName.LONG_NAME, parseLookup);
        return append0(pp, pp);
    }

    /**
     * Instructs the printer to emit a short locale-specific time zone name.
     * Using this method prevents parsing, because time zone names are not unique.
     * See {@link #appendTimeZoneShortName(Map)}.
     *
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendTimeZoneShortName() {
        return append0(new TimeZoneName(TimeZoneName.SHORT_NAME, null), null);
    }

    /**
     * Instructs the printer to emit a short locale-specific time zone
     * name, providing a lookup for parsing.
     * Time zone names are not unique, thus the API forces you to supply the lookup.
     * The names are searched in the order of the map, thus it is strongly recommended
     * to use a {@code LinkedHashMap} or similar.
     *
     * @param parseLookup  the table of names, null to use the {@link DateTimeUtils#getDefaultTimeZoneNames() default names}
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendTimeZoneShortName(Map<String, DateTimeZone> parseLookup) {
        TimeZoneName pp = new TimeZoneName(TimeZoneName.SHORT_NAME, parseLookup);
        return append0(pp, pp);
    }

    /**
     * Instructs the printer to emit the identifier of the time zone.
     * From version 2.0, this field can be parsed.
     *
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendTimeZoneId() {
        return append0(TimeZoneId.INSTANCE, TimeZoneId.INSTANCE);
    }

    /**
     * Instructs the printer to emit text and numbers to display time zone
     * offset from UTC. A parser will use the parsed time zone offset to adjust
     * the datetime.
     * <p>
     * If zero offset text is supplied, then it will be printed when the zone is zero.
     * During parsing, either the zero offset text, or the offset will be parsed.
     *
     * @param zeroOffsetText  the text to use if time zone offset is zero. If
     * null, offset is always shown.
     * @param showSeparators  if true, prints ':' separator before minute and
     * second field and prints '.' separator before fraction field.
     * @param minFields  minimum number of fields to print, stopping when no
     * more precision is required. 1=hours, 2=minutes, 3=seconds, 4=fraction
     * @param maxFields  maximum number of fields to print
     * @return this DateTimeFormatterBuilder, for chaining
     */
    public DateTimeFormatterBuilder appendTimeZoneOffset(
            String zeroOffsetText, boolean showSeparators,
            int minFields, int maxFields) {
        return append0(new TimeZoneOffset
                       (zeroOffsetText, zeroOffsetText, showSeparators, minFields, maxFields));
    }

    /**
     * Instructs the printer to emit text and numbers to display time zone
     * offset from UTC. A parser will use the parsed time zone offset to adjust
     * the datetime.
     * <p>
     * If zero offset print text is supplied, then it will be printed when the zone is zero.
     * If zero offset parse text is supplied, then either it or the offset will be parsed.
     *
     * @param zeroOffsetPrintText  the text to print if time zone offset is zero. If
     * null, offset is always shown.
     * @param zeroOffsetParseText  the text to optionally parse to indicate that the time
     * zone offset is zero. If null, then always use the offset.
     * @param showSeparators  if true, prints ':' separator before minute and
     * second field and prints '.' separator before fraction field.
     * @param minFields  minimum number of fields to print, stopping when no
     * more precision is required. 1=hours, 2=minutes, 3=seconds, 4=fraction
     * @param maxFields  maximum number of fields to print
     * @return this DateTimeFormatterBuilder, for chaining
     * @since 2.0
     */
    public DateTimeFormatterBuilder appendTimeZoneOffset(
            String zeroOffsetPrintText, String zeroOffsetParseText, boolean showSeparators,
            int minFields, int maxFields) {
        return append0(new TimeZoneOffset
                       (zeroOffsetPrintText, zeroOffsetParseText, showSeparators, minFields, maxFields));
    }

    //-----------------------------------------------------------------------
    /**
     * Calls upon {@link DateTimeFormat} to parse the pattern and append the
     * results into this builder.
     *
     * @param pattern  pattern specification
     * @throws IllegalArgumentException if the pattern is invalid
     * @see DateTimeFormat
     */
    public DateTimeFormatterBuilder appendPattern(String pattern) {
        DateTimeFormat.appendPatternTo(this, pattern);
        return this;
    }

    //-----------------------------------------------------------------------
    private Object getFormatter() {
        Object f = iFormatter;

        if (f == null) {
            if (iElementPairs.size() == 2) {
                Object printer = iElementPairs.get(0);
                Object parser = iElementPairs.get(1);

                if (printer != null) {
                    if (printer == parser || parser == null) {
                        f = printer;
                    }
                } else {
                    f = parser;
                }
            }

            if (f == null) {
                f = new Composite(iElementPairs);
            }

            iFormatter = f;
        }

        return f;
    }

    private boolean isPrinter(Object f) {
        if (f instanceof InternalPrinter) {
            if (f instanceof Composite) {
                return ((Composite)f).isPrinter();
            }
            return true;
        }
        return false;
    }

    private boolean isParser(Object f) {
        if (f instanceof InternalParser) {
            if (f instanceof Composite) {
                return ((Composite)f).isParser();
            }
            return true;
        }
        return false;
    }

    private boolean isFormatter(Object f) {
        return (isPrinter(f) || isParser(f));
    }

    static void appendUnknownString(Appendable appendable, int len) throws IOException {
        for (int i = len; --i >= 0;) {
            appendable.append('\ufffd');
        }
    }

    //-----------------------------------------------------------------------
    static class CharacterLiteral
            implements InternalPrinter, InternalParser {

        private final char iValue;

        CharacterLiteral(char value) {
            super();
            iValue = value;
        }

        public int estimatePrintedLength() {
            return 1;
        }

        public void printTo(
                Appendable appendable, long instant, Chronology chrono,
                int displayOffset, DateTimeZone displayZone, Locale locale) throws IOException {
            appendable.append(iValue);
        }

        public void printTo(Appendable appendable, ReadablePartial partial, Locale locale) throws IOException {
            appendable.append(iValue);
        }

        public int estimateParsedLength() {
            return 1;
        }

        public int parseInto(DateTimeParserBucket bucket, CharSequence text, int position) {
            if (position >= text.length()) {
                return ~position;
            }

            char a = text.charAt(position);
            char b = iValue;

            if (a != b) {
                a = Character.toUpperCase(a);
                b = Character.toUpperCase(b);
                if (a != b) {
                    a = Character.toLowerCase(a);
                    b = Character.toLowerCase(b);
                    if (a != b) {
                        return ~position;
                    }
                }
            }

            return position + 1;
        }
    }

    //-----------------------------------------------------------------------
    static class StringLiteral
            implements InternalPrinter, InternalParser {

        private final String iValue;

        StringLiteral(String value) {
            super();
            iValue = value;
        }

        public int estimatePrintedLength() {
            return iValue.length();
        }

        public void printTo(
                Appendable appendable, long instant, Chronology chrono,
                int displayOffset, DateTimeZone displayZone, Locale locale) throws IOException {
            appendable.append(iValue);
        }

        public void printTo(Appendable appendable, ReadablePartial partial, Locale locale) throws IOException {
            appendable.append(iValue);
        }

        public int estimateParsedLength() {
            return iValue.length();
        }

        public int parseInto(DateTimeParserBucket bucket, CharSequence text, int position) {
            if (csStartsWithIgnoreCase(text, position, iValue)) {
                return position + iValue.length();
            }
            return ~position;
        }
    }

    //-----------------------------------------------------------------------
    static abstract class NumberFormatter
            implements InternalPrinter, InternalParser {
        protected final DateTimeFieldType iFieldType;
        protected final int iMaxParsedDigits;
        protected final boolean iSigned;

        NumberFormatter(DateTimeFieldType fieldType,
                int maxParsedDigits, boolean signed) {
            super();
            iFieldType = fieldType;
            iMaxParsedDigits = maxParsedDigits;
            iSigned = signed;
        }

        public int estimateParsedLength() {
            return iMaxParsedDigits;
        }

        public int parseInto(DateTimeParserBucket bucket, CharSequence text, int position) {
            int limit = Math.min(iMaxParsedDigits, text.length() - position);

            boolean negative = false;
            boolean positive = false;
            int length = 0;
            while (length < limit) {
                char c = text.charAt(position + length);
                if (length == 0 && (c == '-' || c == '+') && iSigned) {
                    negative = c == '-';
                    positive = c == '+';

                    // Next character must be a digit.
                    if (length + 1 >= limit || 
                        (c = text.charAt(position + length + 1)) < '0' || c > '9') {
                        break;
                    }
                    length++;

                    // Expand the limit to disregard the sign character.
                    limit = Math.min(limit + 1, text.length() - position);
                    continue;
                }
                if (c < '0' || c > '9') {
                    break;
                }
                length++;
            }

            if (length == 0) {
                return ~position;
            }

            int value;
            if (length >= 9) {
                // Since value may exceed integer limits, use stock parser
                // which checks for this.
                if (positive) {
                    value = Integer.parseInt(text.subSequence(position + 1, position += length).toString());
                } else {
                    value = Integer.parseInt(text.subSequence(position, position += length).toString());
                }
//                value = Integer.parseInt(text.subSequence(position, position += length).toString());
            } else {
                int i = position;
                if (negative || positive) {
                    i++;
                }
                try {
                    value = text.charAt(i++) - '0';
                } catch (StringIndexOutOfBoundsException e) {
                    return ~position;
                }
                position += length;
                while (i < position) {
                    value = ((value << 3) + (value << 1)) + text.charAt(i++) - '0';
                }
                if (negative) {
                    value = -value;
                }
            }

            bucket.saveField(iFieldType, value);
            return position;
        }
    }

    //-----------------------------------------------------------------------
    static class UnpaddedNumber extends NumberFormatter {

        protected UnpaddedNumber(DateTimeFieldType fieldType,
                       int maxParsedDigits, boolean signed)
        {
            super(fieldType, maxParsedDigits, signed);
        }

        public int estimatePrintedLength() {
            return iMaxParsedDigits;
        }

        public void printTo(
                Appendable appendable, long instant, Chronology chrono,
                int displayOffset, DateTimeZone displayZone, Locale locale) throws IOException {
            try {
                DateTimeField field = iFieldType.getField(chrono);
                FormatUtils.appendUnpaddedInteger(appendable, field.get(instant));
            } catch (RuntimeException e) {
                appendable.append('\ufffd');
            }
        }

        public void printTo(Appendable appendable, ReadablePartial partial, Locale locale) throws IOException {
            if (partial.isSupported(iFieldType)) {
                try {
                    FormatUtils.appendUnpaddedInteger(appendable, partial.get(iFieldType));
                } catch (RuntimeException e) {
                    appendable.append('\ufffd');
                }
            } else {
                appendable.append('\ufffd');
            }
        }
    }

    //-----------------------------------------------------------------------
    static class PaddedNumber extends NumberFormatter {

        protected final int iMinPrintedDigits;

        protected PaddedNumber(DateTimeFieldType fieldType, int maxParsedDigits,
                     boolean signed, int minPrintedDigits)
        {
            super(fieldType, maxParsedDigits, signed);
            iMinPrintedDigits = minPrintedDigits;
        }

        public int estimatePrintedLength() {
            return iMaxParsedDigits;
        }

        public void printTo(
                Appendable appendable, long instant, Chronology chrono,
                int displayOffset, DateTimeZone displayZone, Locale locale) throws IOException {
            try {
                DateTimeField field = iFieldType.getField(chrono);
                FormatUtils.appendPaddedInteger(appendable, field.get(instant), iMinPrintedDigits);
            } catch (RuntimeException e) {
                appendUnknownString(appendable, iMinPrintedDigits);
            }
        }

        public void printTo(Appendable appendable, ReadablePartial partial, Locale locale) throws IOException {
            if (partial.isSupported(iFieldType)) {
                try {
                    FormatUtils.appendPaddedInteger(appendable, partial.get(iFieldType), iMinPrintedDigits);
                } catch (RuntimeException e) {
                    appendUnknownString(appendable, iMinPrintedDigits);
                }
            } else {
                appendUnknownString(appendable, iMinPrintedDigits);
            }
        }
    }

    //-----------------------------------------------------------------------
    static class FixedNumber extends PaddedNumber {

        protected FixedNumber(DateTimeFieldType fieldType, int numDigits, boolean signed) {
            super(fieldType, numDigits, signed, numDigits);
        }

        @Override
        public int parseInto(DateTimeParserBucket bucket, CharSequence text, int position) {
            int newPos = super.parseInto(bucket, text, position);
            if (newPos < 0) {
                return newPos;
            }
            int expectedPos = position + iMaxParsedDigits;
            if (newPos != expectedPos) {
                if (iSigned) {
                    char c = text.charAt(position);
                    if (c == '-' || c == '+') {
                        expectedPos++;
                    }
                }
                if (newPos > expectedPos) {
                    // The failure is at the position of the first extra digit.
                    return ~(expectedPos + 1);
                } else if (newPos < expectedPos) {
                    // The failure is at the position where the next digit should be.
                    return ~newPos;
                }
            }
            return newPos;
        }
    }

    //-----------------------------------------------------------------------
    static class TwoDigitYear
            implements InternalPrinter, InternalParser {

        /** The field to print/parse. */
        private final DateTimeFieldType iType;
        /** The pivot year. */
        private final int iPivot;
        private final boolean iLenientParse;

        TwoDigitYear(DateTimeFieldType type, int pivot, boolean lenientParse) {
            super();
            iType = type;
            iPivot = pivot;
            iLenientParse = lenientParse;
        }

        public int estimateParsedLength() {
            return iLenientParse ? 4 : 2;
        }

        public int parseInto(DateTimeParserBucket bucket, CharSequence text, int position) {
            int limit = text.length() - position;

            if (!iLenientParse) {
                limit = Math.min(2, limit);
                if (limit < 2) {
                    return ~position;
                }
            } else {
                boolean hasSignChar = false;
                boolean negative = false;
                int length = 0;
                while (length < limit) {
                    char c = text.charAt(position + length);
                    if (length == 0 && (c == '-' || c == '+')) {
                        hasSignChar = true;
                        negative = c == '-';
                        if (negative) {
                            length++;
                        } else {
                            // Skip the '+' for parseInt to succeed.
                            position++;
                            limit--;
                        }
                        continue;
                    }
                    if (c < '0' || c > '9') {
                        break;
                    }
                    length++;
                }
                
                if (length == 0) {
                    return ~position;
                }

                if (hasSignChar || length != 2) {
                    int value;
                    if (length >= 9) {
                        // Since value may exceed integer limits, use stock
                        // parser which checks for this.
                        value = Integer.parseInt(text.subSequence(position, position += length).toString());
                    } else {
                        int i = position;
                        if (negative) {
                            i++;
                        }
                        try {
                            value = text.charAt(i++) - '0';
                        } catch (StringIndexOutOfBoundsException e) {
                            return ~position;
                        }
                        position += length;
                        while (i < position) {
                            value = ((value << 3) + (value << 1)) + text.charAt(i++) - '0';
                        }
                        if (negative) {
                            value = -value;
                        }
                    }
                    
                    bucket.saveField(iType, value);
                    return position;
                }
            }

            int year;
            char c = text.charAt(position);
            if (c < '0' || c > '9') {
                return ~position;
            }
            year = c - '0';
            c = text.charAt(position + 1);
            if (c < '0' || c > '9') {
                return ~position;
            }
            year = ((year << 3) + (year << 1)) + c - '0';

            int pivot = iPivot;
            // If the bucket pivot year is non-null, use that when parsing
            if (bucket.getPivotYear() != null) {
                pivot = bucket.getPivotYear().intValue();
            }

            int low = pivot - 50;

            int t;
            if (low >= 0) {
                t = low % 100;
            } else {
                t = 99 + ((low + 1) % 100);
            }

            year += low + ((year < t) ? 100 : 0) - t;

            bucket.saveField(iType, year);
            return position + 2;
        }
        
        public int estimatePrintedLength() {
            return 2;
        }

        public void printTo(
                Appendable appendable, long instant, Chronology chrono,
                int displayOffset, DateTimeZone displayZone, Locale locale) throws IOException {
            int year = getTwoDigitYear(instant, chrono);
            if (year < 0) {
                appendable.append('\ufffd');
                appendable.append('\ufffd');
            } else {
                FormatUtils.appendPaddedInteger(appendable, year, 2);
            }
        }

        private int getTwoDigitYear(long instant, Chronology chrono) {
            try {
                int year = iType.getField(chrono).get(instant);
                if (year < 0) {
                    year = -year;
                }
                return year % 100;
            } catch (RuntimeException e) {
                return -1;
            }
        }

        public void printTo(Appendable appendable, ReadablePartial partial, Locale locale) throws IOException {
            int year = getTwoDigitYear(partial);
            if (year < 0) {
                appendable.append('\ufffd');
                appendable.append('\ufffd');
            } else {
                FormatUtils.appendPaddedInteger(appendable, year, 2);
            }
        }

        private int getTwoDigitYear(ReadablePartial partial) {
            if (partial.isSupported(iType)) {
                try {
                    int year = partial.get(iType);
                    if (year < 0) {
                        year = -year;
                    }
                    return year % 100;
                } catch (RuntimeException e) {}
            } 
            return -1;
        }
    }

    //-----------------------------------------------------------------------
    static class TextField
            implements InternalPrinter, InternalParser {

        private static Map<Locale, Map<DateTimeFieldType, Object[]>> cParseCache =
                    new ConcurrentHashMap<Locale, Map<DateTimeFieldType, Object[]>>();
        private final DateTimeFieldType iFieldType;
        private final boolean iShort;

        TextField(DateTimeFieldType fieldType, boolean isShort) {
            super();
            iFieldType = fieldType;
            iShort = isShort;
        }

        public int estimatePrintedLength() {
            return iShort ? 6 : 20;
        }

        public void printTo(
                Appendable appendable, long instant, Chronology chrono,
                int displayOffset, DateTimeZone displayZone, Locale locale) throws IOException {
            try {
                appendable.append(print(instant, chrono, locale));
            } catch (RuntimeException e) {
                appendable.append('\ufffd');
            }
        }

        public void printTo(Appendable appendable, ReadablePartial partial, Locale locale) throws IOException {
            try {
                appendable.append(print(partial, locale));
            } catch (RuntimeException e) {
                appendable.append('\ufffd');
            }
        }

        private String print(long instant, Chronology chrono, Locale locale) {
            DateTimeField field = iFieldType.getField(chrono);
            if (iShort) {
                return field.getAsShortText(instant, locale);
            } else {
                return field.getAsText(instant, locale);
            }
        }

        private String print(ReadablePartial partial, Locale locale) {
            if (partial.isSupported(iFieldType)) {
                DateTimeField field = iFieldType.getField(partial.getChronology());
                if (iShort) {
                    return field.getAsShortText(partial, locale);
                } else {
                    return field.getAsText(partial, locale);
                }
            } else {
                return "\ufffd";
            }
        }

        public int estimateParsedLength() {
            return estimatePrintedLength();
        }

        @SuppressWarnings("unchecked")
        public int parseInto(DateTimeParserBucket bucket, CharSequence text, int position) {
            Locale locale = bucket.getLocale();
            // handle languages which might have non ASCII A-Z or punctuation
            // bug 1788282
            Map<String, Boolean> validValues = null;
            int maxLength = 0;
            Map<DateTimeFieldType, Object[]> innerMap = cParseCache.get(locale);
            if (innerMap == null) {
                innerMap = new ConcurrentHashMap<DateTimeFieldType, Object[]>();
                cParseCache.put(locale, innerMap);
            }
            Object[] array = innerMap.get(iFieldType);
            if (array == null) {
                validValues = new ConcurrentHashMap<String, Boolean>(32);  // use map as no concurrent Set
                MutableDateTime dt = new MutableDateTime(0L, DateTimeZone.UTC);
                Property property = dt.property(iFieldType);
                int min = property.getMinimumValueOverall();
                int max = property.getMaximumValueOverall();
                if (max - min > 32) {  // protect against invalid fields
                    return ~position;
                }
                maxLength = property.getMaximumTextLength(locale);
                for (int i = min; i <= max; i++) {
                    property.set(i);
                    validValues.put(property.getAsShortText(locale), Boolean.TRUE);
                    validValues.put(property.getAsShortText(locale).toLowerCase(locale), Boolean.TRUE);
                    validValues.put(property.getAsShortText(locale).toUpperCase(locale), Boolean.TRUE);
                    validValues.put(property.getAsText(locale), Boolean.TRUE);
                    validValues.put(property.getAsText(locale).toLowerCase(locale), Boolean.TRUE);
                    validValues.put(property.getAsText(locale).toUpperCase(locale), Boolean.TRUE);
                }
                if ("en".equals(locale.getLanguage()) && iFieldType == DateTimeFieldType.era()) {
                    // hack to support for parsing "BCE" and "CE" if the language is English
                    validValues.put("BCE", Boolean.TRUE);
                    validValues.put("bce", Boolean.TRUE);
                    validValues.put("CE", Boolean.TRUE);
                    validValues.put("ce", Boolean.TRUE);
                    maxLength = 3;
                }
                array = new Object[] {validValues, Integer.valueOf(maxLength)};
                innerMap.put(iFieldType, array);
            } else {
                validValues = (Map<String, Boolean>) array[0];
                maxLength = ((Integer) array[1]).intValue();
            }
            // match the longest string first using our knowledge of the max length
            int limit = Math.min(text.length(), position + maxLength);
            for (int i = limit; i > position; i--) {
                String match = text.subSequence(position, i).toString();
                if (validValues.containsKey(match)) {
                    bucket.saveField(iFieldType, match, locale);
                    return i;
                }
            }
            return ~position;
        }
    }

    //-----------------------------------------------------------------------
    static class Fraction
            implements InternalPrinter, InternalParser {

        private final DateTimeFieldType iFieldType;
        protected int iMinDigits;
        protected int iMaxDigits;

        protected Fraction(DateTimeFieldType fieldType, int minDigits, int maxDigits) {
            super();
            iFieldType = fieldType;
            // Limit the precision requirements.
            if (maxDigits > 18) {
                maxDigits = 18;
            }
            iMinDigits = minDigits;
            iMaxDigits = maxDigits;
        }

        public int estimatePrintedLength() {
            return iMaxDigits;
        }

        public void printTo(
                Appendable appendable, long instant, Chronology chrono,
                int displayOffset, DateTimeZone displayZone, Locale locale) throws IOException {
            printTo(appendable, instant, chrono);
        }

        public void printTo(Appendable appendable, ReadablePartial partial, Locale locale) throws IOException {
            // removed check whether field is supported, as input field is typically
            // secondOfDay which is unsupported by TimeOfDay
            long millis = partial.getChronology().set(partial, 0L);
            printTo(appendable, millis, partial.getChronology());
        }

        protected void printTo(Appendable appendable, long instant, Chronology chrono)
            throws IOException
        {
            DateTimeField field = iFieldType.getField(chrono);
            int minDigits = iMinDigits;

            long fraction;
            try {
                fraction = field.remainder(instant);
            } catch (RuntimeException e) {
                appendUnknownString(appendable, minDigits);
                return;
            }

            if (fraction == 0) {
                while (--minDigits >= 0) {
                    appendable.append('0');
                }
                return;
            }

            String str;
            long[] fractionData = getFractionData(fraction, field);
            long scaled = fractionData[0];
            int maxDigits = (int) fractionData[1];
            
            if ((scaled & 0x7fffffff) == scaled) {
                str = Integer.toString((int) scaled);
            } else {
                str = Long.toString(scaled);
            }

            int length = str.length();
            int digits = maxDigits;
            while (length < digits) {
                appendable.append('0');
                minDigits--;
                digits--;
            }

            if (minDigits < digits) {
                // Chop off as many trailing zero digits as necessary.
                while (minDigits < digits) {
                    if (length <= 1 || str.charAt(length - 1) != '0') {
                        break;
                    }
                    digits--;
                    length--;
                }
                if (length < str.length()) {
                    for (int i=0; i<length; i++) {
                        appendable.append(str.charAt(i));
                    }
                    return;
                }
            }

            appendable.append(str);
        }
        
        private long[] getFractionData(long fraction, DateTimeField field) {
            long rangeMillis = field.getDurationField().getUnitMillis();
            long scalar;
            int maxDigits = iMaxDigits;
            while (true) {
                switch (maxDigits) {
                default: scalar = 1L; break;
                case 1:  scalar = 10L; break;
                case 2:  scalar = 100L; break;
                case 3:  scalar = 1000L; break;
                case 4:  scalar = 10000L; break;
                case 5:  scalar = 100000L; break;
                case 6:  scalar = 1000000L; break;
                case 7:  scalar = 10000000L; break;
                case 8:  scalar = 100000000L; break;
                case 9:  scalar = 1000000000L; break;
                case 10: scalar = 10000000000L; break;
                case 11: scalar = 100000000000L; break;
                case 12: scalar = 1000000000000L; break;
                case 13: scalar = 10000000000000L; break;
                case 14: scalar = 100000000000000L; break;
                case 15: scalar = 1000000000000000L; break;
                case 16: scalar = 10000000000000000L; break;
                case 17: scalar = 100000000000000000L; break;
                case 18: scalar = 1000000000000000000L; break;
                }
                if (((rangeMillis * scalar) / scalar) == rangeMillis) {
                    break;
                }
                // Overflowed: scale down.
                maxDigits--;
            }
            
            return new long[] {fraction * scalar / rangeMillis, maxDigits};
        }

        public int estimateParsedLength() {
            return iMaxDigits;
        }

        public int parseInto(DateTimeParserBucket bucket, CharSequence text, int position) {
            DateTimeField field = iFieldType.getField(bucket.getChronology());
            
            int limit = Math.min(iMaxDigits, text.length() - position);

            long value = 0;
            long n = field.getDurationField().getUnitMillis() * 10;
            int length = 0;
            while (length < limit) {
                char c = text.charAt(position + length);
                if (c < '0' || c > '9') {
                    break;
                }
                length++;
                long nn = n / 10;
                value += (c - '0') * nn;
                n = nn;
            }

            value /= 10;

            if (length == 0) {
                return ~position;
            }

            if (value > Integer.MAX_VALUE) {
                return ~position;
            }

            DateTimeField parseField = new PreciseDateTimeField(
                DateTimeFieldType.millisOfSecond(),
                MillisDurationField.INSTANCE,
                field.getDurationField());

            bucket.saveField(parseField, (int) value);

            return position + length;
        }
    }

    //-----------------------------------------------------------------------
    static class TimeZoneOffset
            implements InternalPrinter, InternalParser {

        private final String iZeroOffsetPrintText;
        private final String iZeroOffsetParseText;
        private final boolean iShowSeparators;
        private final int iMinFields;
        private final int iMaxFields;

        TimeZoneOffset(String zeroOffsetPrintText, String zeroOffsetParseText,
                                boolean showSeparators,
                                int minFields, int maxFields)
        {
            super();
            iZeroOffsetPrintText = zeroOffsetPrintText;
            iZeroOffsetParseText = zeroOffsetParseText;
            iShowSeparators = showSeparators;
            if (minFields <= 0 || maxFields < minFields) {
                throw new IllegalArgumentException();
            }
            if (minFields > 4) {
                minFields = 4;
                maxFields = 4;
            }
            iMinFields = minFields;
            iMaxFields = maxFields;
        }
            
        public int estimatePrintedLength() {
            int est = 1 + iMinFields << 1;
            if (iShowSeparators) {
                est += iMinFields - 1;
            }
            if (iZeroOffsetPrintText != null && iZeroOffsetPrintText.length() > est) {
                est = iZeroOffsetPrintText.length();
            }
            return est;
        }

        public void printTo(
                Appendable buf, long instant, Chronology chrono,
                int displayOffset, DateTimeZone displayZone, Locale locale) throws IOException {
            if (displayZone == null) {
                return;  // no zone
            }
            if (displayOffset == 0 && iZeroOffsetPrintText != null) {
                buf.append(iZeroOffsetPrintText);
                return;
            }
            if (displayOffset >= 0) {
                buf.append('+');
            } else {
                buf.append('-');
                displayOffset = -displayOffset;
            }

            int hours = displayOffset / DateTimeConstants.MILLIS_PER_HOUR;
            FormatUtils.appendPaddedInteger(buf, hours, 2);
            if (iMaxFields == 1) {
                return;
            }
            displayOffset -= hours * (int)DateTimeConstants.MILLIS_PER_HOUR;
            if (displayOffset == 0 && iMinFields <= 1) {
                return;
            }

            int minutes = displayOffset / DateTimeConstants.MILLIS_PER_MINUTE;
            if (iShowSeparators) {
                buf.append(':');
            }
            FormatUtils.appendPaddedInteger(buf, minutes, 2);
            if (iMaxFields == 2) {
                return;
            }
            displayOffset -= minutes * DateTimeConstants.MILLIS_PER_MINUTE;
            if (displayOffset == 0 && iMinFields <= 2) {
                return;
            }

            int seconds = displayOffset / DateTimeConstants.MILLIS_PER_SECOND;
            if (iShowSeparators) {
                buf.append(':');
            }
            FormatUtils.appendPaddedInteger(buf, seconds, 2);
            if (iMaxFields == 3) {
                return;
            }
            displayOffset -= seconds * DateTimeConstants.MILLIS_PER_SECOND;
            if (displayOffset == 0 && iMinFields <= 3) {
                return;
            }

            if (iShowSeparators) {
                buf.append('.');
            }
            FormatUtils.appendPaddedInteger(buf, displayOffset, 3);
        }

        public void printTo(Appendable appendable, ReadablePartial partial, Locale locale) throws IOException {
            // no zone info
        }

        public int estimateParsedLength() {
            return estimatePrintedLength();
        }

        public int parseInto(DateTimeParserBucket bucket, CharSequence text, int position) {
            int limit = text.length() - position;

            zeroOffset:
            if (iZeroOffsetParseText != null) {
                if (iZeroOffsetParseText.length() == 0) {
                    // Peek ahead, looking for sign character.
                    if (limit > 0) {
                        char c = text.charAt(position);
                        if (c == '-' || c == '+') {
                            break zeroOffset;
                        }
                    }
                    bucket.setOffset(Integer.valueOf(0));
                    return position;
                }
                if (csStartsWithIgnoreCase(text, position, iZeroOffsetParseText)) {
                    bucket.setOffset(Integer.valueOf(0));
                    return position + iZeroOffsetParseText.length();
                }
            }

            // Format to expect is sign character followed by at least one digit.

            if (limit <= 1) {
                return ~position;
            }

            boolean negative;
            char c = text.charAt(position);
            if (c == '-') {
                negative = true;
            } else if (c == '+') {
                negative = false;
            } else {
                return ~position;
            }

            limit--;
            position++;

            // Format following sign is one of:
            //
            // hh
            // hhmm
            // hhmmss
            // hhmmssSSS
            // hh:mm
            // hh:mm:ss
            // hh:mm:ss.SSS

            // First parse hours.

            if (digitCount(text, position, 2) < 2) {
                // Need two digits for hour.
                return ~position;
            }

            int offset;

            int hours = FormatUtils.parseTwoDigits(text, position);
            if (hours > 23) {
                return ~position;
            }
            offset = hours * DateTimeConstants.MILLIS_PER_HOUR;
            limit -= 2;
            position += 2;

            parse: {
                // Need to decide now if separators are expected or parsing
                // stops at hour field.

                if (limit <= 0) {
                    break parse;
                }

                boolean expectSeparators;
                c = text.charAt(position);
                if (c == ':') {
                    expectSeparators = true;
                    limit--;
                    position++;
                } else if (c >= '0' && c <= '9') {
                    expectSeparators = false;
                } else {
                    break parse;
                }

                // Proceed to parse minutes.

                int count = digitCount(text, position, 2);
                if (count == 0 && !expectSeparators) {
                    break parse;
                } else if (count < 2) {
                    // Need two digits for minute.
                    return ~position;
                }

                int minutes = FormatUtils.parseTwoDigits(text, position);
                if (minutes > 59) {
                    return ~position;
                }
                offset += minutes * DateTimeConstants.MILLIS_PER_MINUTE;
                limit -= 2;
                position += 2;

                // Proceed to parse seconds.

                if (limit <= 0) {
                    break parse;
                }

                if (expectSeparators) {
                    if (text.charAt(position) != ':') {
                        break parse;
                    }
                    limit--;
                    position++;
                }

                count = digitCount(text, position, 2);
                if (count == 0 && !expectSeparators) {
                    break parse;
                } else if (count < 2) {
                    // Need two digits for second.
                    return ~position;
                }

                int seconds = FormatUtils.parseTwoDigits(text, position);
                if (seconds > 59) {
                    return ~position;
                }
                offset += seconds * DateTimeConstants.MILLIS_PER_SECOND;
                limit -= 2;
                position += 2;

                // Proceed to parse fraction of second.

                if (limit <= 0) {
                    break parse;
                }

                if (expectSeparators) {
                    if (text.charAt(position) != '.' && text.charAt(position) != ',') {
                        break parse;
                    }
                    limit--;
                    position++;
                }
                
                count = digitCount(text, position, 3);
                if (count == 0 && !expectSeparators) {
                    break parse;
                } else if (count < 1) {
                    // Need at least one digit for fraction of second.
                    return ~position;
                }

                offset += (text.charAt(position++) - '0') * 100;
                if (count > 1) {
                    offset += (text.charAt(position++) - '0') * 10;
                    if (count > 2) {
                        offset += text.charAt(position++) - '0';
                    }
                }
            }

            bucket.setOffset(Integer.valueOf(negative ? -offset : offset));
            return position;
        }

        /**
         * Returns actual amount of digits to parse, but no more than original
         * 'amount' parameter.
         */
        private int digitCount(CharSequence text, int position, int amount) {
            int limit = Math.min(text.length() - position, amount);
            amount = 0;
            for (; limit > 0; limit--) {
                char c = text.charAt(position + amount);
                if (c < '0' || c > '9') {
                    break;
                }
                amount++;
            }
            return amount;
        }
    }

    //-----------------------------------------------------------------------
    static class TimeZoneName
            implements InternalPrinter, InternalParser {

        static final int LONG_NAME = 0;
        static final int SHORT_NAME = 1;

        private final Map<String, DateTimeZone> iParseLookup;
        private final int iType;

        TimeZoneName(int type, Map<String, DateTimeZone> parseLookup) {
            super();
            iType = type;
            iParseLookup = parseLookup;
        }

        public int estimatePrintedLength() {
            return (iType == SHORT_NAME ? 4 : 20);
        }

        public void printTo(
                Appendable appendable, long instant, Chronology chrono,
                int displayOffset, DateTimeZone displayZone, Locale locale) throws IOException {
            appendable.append(print(instant - displayOffset, displayZone, locale));
        }

        private String print(long instant, DateTimeZone displayZone, Locale locale) {
            if (displayZone == null) {
                return "";  // no zone
            }
            switch (iType) {
                case LONG_NAME:
                    return displayZone.getName(instant, locale);
                case SHORT_NAME:
                    return displayZone.getShortName(instant, locale);
            }
            return "";
        }

        public void printTo(Appendable appendable, ReadablePartial partial, Locale locale) throws IOException {
            // no zone info
        }

        public int estimateParsedLength() {
            return (iType == SHORT_NAME ? 4 : 20);
        }

        public int parseInto(DateTimeParserBucket bucket, CharSequence text, int position) {
            Map<String, DateTimeZone> parseLookup = iParseLookup;
            parseLookup = (parseLookup != null ? parseLookup : DateTimeUtils.getDefaultTimeZoneNames());
            String matched = null;
            for (String name : parseLookup.keySet()) {
                if (csStartsWith(text, position, name)) {
                    if (matched == null || name.length() > matched.length()) {
                        matched = name;
                    }
                }
            }
            if (matched != null) {
                bucket.setZone(parseLookup.get(matched));
                return position + matched.length();
            }
            return ~position;
        }
    }

    //-----------------------------------------------------------------------
    static enum TimeZoneId
            implements InternalPrinter, InternalParser {

        INSTANCE;
        private static final List<String> ALL_IDS;
        // groups are "Europe/A", "Europe/B", "Europe/C", etc
        // group of "" is for zones that do not have a "/" in the name
        private static final Map<String, List<String>> GROUPED_IDS;
        private static final List<String> BASE_GROUPED_IDS = new ArrayList<String>();
        static final int MAX_LENGTH;
        static final int MAX_PREFIX_LENGTH;
        static {
            ALL_IDS = new ArrayList<String>(DateTimeZone.getAvailableIDs());
            Collections.sort(ALL_IDS);
            GROUPED_IDS = new HashMap<String, List<String>>();
            int max = 0;
            int maxPrefix = 0;
            for (String id : ALL_IDS) {
                int pos = id.indexOf('/');
                if (pos >= 0) {
                    if (pos < id.length()) {
                        pos++;
                    }
                    maxPrefix = Math.max(maxPrefix, pos);
                    String prefix = id.substring(0, pos + 1);
                    String suffix = id.substring(pos);
                    if (!GROUPED_IDS.containsKey(prefix)) {
                        GROUPED_IDS.put(prefix, new ArrayList<String>());
                    }
                    GROUPED_IDS.get(prefix).add(suffix);
                } else {
                    BASE_GROUPED_IDS.add(id);
                }
                max = Math.max(max, id.length());
            }
            MAX_LENGTH = max;
            MAX_PREFIX_LENGTH = maxPrefix;
        }

        public int estimatePrintedLength() {
            return MAX_LENGTH;
        }

        public void printTo(
                Appendable appendable, long instant, Chronology chrono,
                int displayOffset, DateTimeZone displayZone, Locale locale) throws IOException {
            appendable.append(displayZone != null ? displayZone.getID() : "");
        }

        public void printTo(Appendable appendable, ReadablePartial partial, Locale locale) throws IOException {
            // no zone info
        }

        public int estimateParsedLength() {
            return MAX_LENGTH;
        }

        public int parseInto(DateTimeParserBucket bucket, CharSequence text, int position) {
            // select the base set of identifiers that do not have a slash
            List<String> suffixSet = BASE_GROUPED_IDS;
            // hunt for a slash only as far as the max prefix length
            int textLen = text.length();
            int matchLen = Math.min(textLen, position + MAX_PREFIX_LENGTH);
            int pos = position;
            String prefix = "";
            for (int i = pos; i < matchLen; i++) {
                if (text.charAt(i) == '/') {
                    // when a slash is found, determine the prefix, such as "Europe/A" and lookup to get suffixes
                    prefix = text.subSequence(pos, i + 1).toString();
                    pos += prefix.length();
                    String prefixLookup = prefix;
                    if (i < textLen) {
                        prefixLookup += text.charAt(i + 1);
                    }
                    suffixSet = GROUPED_IDS.get(prefixLookup);
                    if (suffixSet == null) {
                        return ~position;
                    }
                    break;
                }
            }
            // search all suffixes, hopefully a relatively small number due to prefix search
            String best = null;
            for (int i = 0; i < suffixSet.size(); i++) {
                String suffix = suffixSet.get(i);
                if (csStartsWith(text, pos, suffix)) {
                    if (best == null || suffix.length() > best.length()) {
                        best = suffix;
                    }
                }
            }
            // if found then store, else fail
            if (best != null) {
                bucket.setZone(DateTimeZone.forID(prefix + best));
                return pos + best.length();
            }
            return ~position;
        }

    }

    //-----------------------------------------------------------------------
    static class Composite
            implements InternalPrinter, InternalParser {

        private final InternalPrinter[] iPrinters;
        private final InternalParser[] iParsers;

        private final int iPrintedLengthEstimate;
        private final int iParsedLengthEstimate;

        Composite(List<Object> elementPairs) {
            super();

            List<Object> printerList = new ArrayList<Object>();
            List<Object> parserList = new ArrayList<Object>();

            decompose(elementPairs, printerList, parserList);

            if (printerList.contains(null) || printerList.isEmpty()) {
                iPrinters = null;
                iPrintedLengthEstimate = 0;
            } else {
                int size = printerList.size();
                iPrinters = new InternalPrinter[size];
                int printEst = 0;
                for (int i=0; i<size; i++) {
                    InternalPrinter printer = (InternalPrinter) printerList.get(i);
                    printEst += printer.estimatePrintedLength();
                    iPrinters[i] = printer;
                }
                iPrintedLengthEstimate = printEst;
            }

            if (parserList.contains(null) || parserList.isEmpty()) {
                iParsers = null;
                iParsedLengthEstimate = 0;
            } else {
                int size = parserList.size();
                iParsers = new InternalParser[size];
                int parseEst = 0;
                for (int i=0; i<size; i++) {
                    InternalParser parser = (InternalParser) parserList.get(i);
                    parseEst += parser.estimateParsedLength();
                    iParsers[i] = parser;
                }
                iParsedLengthEstimate = parseEst;
            }
        }

        public int estimatePrintedLength() {
            return iPrintedLengthEstimate;
        }

        public void printTo(
                Appendable appendable, long instant, Chronology chrono,
                int displayOffset, DateTimeZone displayZone, Locale locale) throws IOException {
            InternalPrinter[] elements = iPrinters;
            if (elements == null) {
                throw new UnsupportedOperationException();
            }

            if (locale == null) {
                // Guard against default locale changing concurrently.
                locale = Locale.getDefault();
            }

            int len = elements.length;
            for (int i = 0; i < len; i++) {
                elements[i].printTo(appendable, instant, chrono, displayOffset, displayZone, locale);
            }
        }

        public void printTo(Appendable appendable, ReadablePartial partial, Locale locale) throws IOException {
            InternalPrinter[] elements = iPrinters;
            if (elements == null) {
                throw new UnsupportedOperationException();
            }

            if (locale == null) {
                // Guard against default locale changing concurrently.
                locale = Locale.getDefault();
            }

            int len = elements.length;
            for (int i=0; i<len; i++) {
                elements[i].printTo(appendable, partial, locale);
            }
        }

        public int estimateParsedLength() {
            return iParsedLengthEstimate;
        }

        public int parseInto(DateTimeParserBucket bucket, CharSequence text, int position) {
            InternalParser[] elements = iParsers;
            if (elements == null) {
                throw new UnsupportedOperationException();
            }

            int len = elements.length;
            for (int i=0; i<len && position >= 0; i++) {
                position = elements[i].parseInto(bucket, text, position);
            }
            return position;
        }

        boolean isPrinter() {
            return iPrinters != null;
        }

        boolean isParser() {
            return iParsers != null;
        }

        /**
         * Processes the element pairs, putting results into the given printer
         * and parser lists.
         */
        private void decompose(List<Object> elementPairs, List<Object> printerList, List<Object> parserList) {
            int size = elementPairs.size();
            for (int i=0; i<size; i+=2) {
                Object element = elementPairs.get(i);
                if (element instanceof Composite) {
                    addArrayToList(printerList, ((Composite)element).iPrinters);
                } else {
                    printerList.add(element);
                }

                element = elementPairs.get(i + 1);
                if (element instanceof Composite) {
                    addArrayToList(parserList, ((Composite)element).iParsers);
                } else {
                    parserList.add(element);
                }
            }
        }

        private void addArrayToList(List<Object> list, Object[] array) {
            if (array != null) {
                for (int i=0; i<array.length; i++) {
                    list.add(array[i]);
                }
            }
        }
    }

    //-----------------------------------------------------------------------
    static class MatchingParser
            implements InternalParser {

        private final InternalParser[] iParsers;
        private final int iParsedLengthEstimate;

        MatchingParser(InternalParser[] parsers) {
            super();
            iParsers = parsers;
            int est = 0;
            for (int i=parsers.length; --i>=0 ;) {
                InternalParser parser = parsers[i];
                if (parser != null) {
                    int len = parser.estimateParsedLength();
                    if (len > est) {
                        est = len;
                    }
                }
            }
            iParsedLengthEstimate = est;
        }

        public int estimateParsedLength() {
            return iParsedLengthEstimate;
        }

        public int parseInto(DateTimeParserBucket bucket, CharSequence text, int position) {
            InternalParser[] parsers = iParsers;
            int length = parsers.length;

            final Object originalState = bucket.saveState();
            boolean isOptional = false;

            int bestValidPos = position;
            Object bestValidState = null;

            int bestInvalidPos = position;

            for (int i=0; i<length; i++) {
                InternalParser parser = parsers[i];
                if (parser == null) {
                    // The empty parser wins only if nothing is better.
                    if (bestValidPos <= position) {
                        return position;
                    }
                    isOptional = true;
                    break;
                }
                int parsePos = parser.parseInto(bucket, text, position);
                if (parsePos >= position) {
                    if (parsePos > bestValidPos) {
                        if (parsePos >= text.length() ||
                            (i + 1) >= length || parsers[i + 1] == null) {

                            // Completely parsed text or no more parsers to
                            // check. Skip the rest.
                            return parsePos;
                        }
                        bestValidPos = parsePos;
                        bestValidState = bucket.saveState();
                    }
                } else {
                    if (parsePos < 0) {
                        parsePos = ~parsePos;
                        if (parsePos > bestInvalidPos) {
                            bestInvalidPos = parsePos;
                        }
                    }
                }
                bucket.restoreState(originalState);
            }

            if (bestValidPos > position || (bestValidPos == position && isOptional)) {
                // Restore the state to the best valid parse.
                if (bestValidState != null) {
                    bucket.restoreState(bestValidState);
                }
                return bestValidPos;
            }

            return ~bestInvalidPos;
        }
    }

    static boolean csStartsWith(CharSequence text, int position, String search) {
        int searchLen = search.length();
        if ((text.length() - position) < searchLen) {
            return false;
        }
        for (int i = 0; i < searchLen; i++) {
            if (text.charAt(position + i) != search.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    static boolean csStartsWithIgnoreCase(CharSequence text, int position, String search) {
        int searchLen = search.length();
        if ((text.length() - position) < searchLen) {
            return false;
        }
        for (int i = 0; i < searchLen; i++) {
            char ch1 = text.charAt(position + i);
            char ch2 = search.charAt(i);
            if (ch1 != ch2) {
                char u1 = Character.toUpperCase(ch1);
                char u2 = Character.toUpperCase(ch2);
                if (u1 != u2 && Character.toLowerCase(u1) != Character.toLowerCase(u2)) {
                    return false;
                }
            }
        }
        return true;
    }

}
