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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadablePartial;
import org.joda.time.field.MillisDurationField;
import org.joda.time.field.PreciseDateTimeField;

/**
 * DateTimeFormatterBuilder is used for constructing {@link DateTimeFormatter}s.
 * DateTimeFormatters can be built by appending specific fields or other
 * formatters. All formatters must extend {@link BaseDateTimeFormatter}.
 *
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
 * @since 1.0
 * @see DateTimeFormat
 * @see ISODateTimeFormat
 */
public class DateTimeFormatterBuilder {

    /** The locale the builder uses. */
    private final Locale iLocale;

    // Array contents alternate between printers and parsers.
    private ArrayList iElementPairs;
    private Object iFormatter;

    //-----------------------------------------------------------------------
    /**
     * Creates a DateTimeFormatterBuilder for the default locale.
     */
    public DateTimeFormatterBuilder() {
        this(Locale.getDefault());
    }

    /**
     * Creates a DateTimeFormatterBuilder for the specified locale.
     * 
     * @param locale Locale to use, or null for default
     */
    public DateTimeFormatterBuilder(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        iLocale = locale;
        iElementPairs = new ArrayList();
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the locale being used by the formatter builder, never null.
     */
    public Locale getLocale() {
        return iLocale;
    }

    //-----------------------------------------------------------------------
    /**
     * Converts to a DateTimePrinter that prints using all the appended
     * elements. Subsequent changes to this builder do not affect the returned
     * printer.
     *
     * @throws UnsupportedOperationException if any formatter element doesn't support
     * printing
     */
    public DateTimePrinter toPrinter() throws UnsupportedOperationException {
        Object f = getFormatter();
        if (isPrinter(f)) {
            return (DateTimePrinter)f;
        }
        throw new UnsupportedOperationException("Printing not supported");
    }

    /**
     * Converts to a DateTimeParser that parses using all the appended
     * elements. Subsequent changes to this builder do not affect the returned
     * parser.
     *
     * @throws UnsupportedOperationException if any formatter element doesn't support
     * parsing
     */
    public DateTimeParser toParser() throws UnsupportedOperationException {
        Object f = getFormatter();
        if (isParser(f)) {
            return (DateTimeParser)f;
        }
        throw new UnsupportedOperationException("Parsing not supported");
    }

    /**
     * Converts to a DateTimeFormatter that prints and parses using all the
     * appended elements. Subsequent changes to this builder do not affect the
     * returned formatter.
     *
     * @throws UnsupportedOperationException if any formatter element doesn't support
     * both printing and parsing
     */
    public DateTimeFormatter toFormatter() throws UnsupportedOperationException {
        Object f = getFormatter();
        if (isFormatter(f)) {
            return (DateTimeFormatter)f;
        }
        throw new UnsupportedOperationException("Both printing and parsing not supported");
    }

    //-----------------------------------------------------------------------
    /**
     * Returns true if toPrinter can be called without throwing an
     * UnsupportedOperationException.
     */
    public boolean canBuildPrinter() {
        return isPrinter(getFormatter());
    }

    /**
     * Returns true if toParser can be called without throwing an
     * UnsupportedOperationException.
     */
    public boolean canBuildParser() {
        return isParser(getFormatter());
    }

    /**
     * Returns true if toFormatter can be called without throwing an
     * UnsupportedOperationException.
     */
    public boolean canBuildFormatter() {
        return isFormatter(getFormatter());
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
     * The formatter must extend <code>DateTimeFormatterProvider</code>.
     * This is an internal class, which all supplied format classes extend.
     *
     * @param formatter  the formatter to add
     * @return this DateTimeFormatterBuilder
     * @throws IllegalArgumentException if formatter is null or of an invalid type
     */
    public DateTimeFormatterBuilder append(DateTimeFormatter formatter) {
        if (formatter == null) {
            throw new IllegalArgumentException("No formatter supplied");
        }
        if (formatter instanceof BaseDateTimeFormatter == false) {
            throw new IllegalArgumentException("Formatter must extend BaseDateTimeFormatter");
        }
        return append0(formatter);
    }

    /**
     * Appends just a printer. With no matching parser, a parser cannot be
     * built from this DateTimeFormatterBuilder.
     * <p>
     * The printer added must extend <code>BaseDateTimeFormatter</code>.
     * This is an internal class, which all supplied format classes extend.
     *
     * @param printer  the printer to add
     * @return this DateTimeFormatterBuilder
     * @throws IllegalArgumentException if printer is null or of an invalid type
     */
    public DateTimeFormatterBuilder append(DateTimePrinter printer) {
        checkPrinter(printer);
        return append0(printer, null);
    }

    /**
     * Appends just a parser. With no matching printer, a printer cannot be
     * built from this builder.
     * <p>
     * The parser added must extend <code>BaseDateTimeFormatter</code>.
     * This is an internal class, which all supplied format classes extend.
     *
     * @param parser  the parser to add
     * @return this DateTimeFormatterBuilder
     * @throws IllegalArgumentException if parser is null or of an invalid type
     */
    public DateTimeFormatterBuilder append(DateTimeParser parser) {
        checkParser(parser);
        return append0(null, parser);
    }

    /**
     * Appends a printer/parser pair.
     * <p>
     * The printer and parser must extend <code>BaseDateTimeFormatter</code>.
     * This is an internal class, which all supplied format classes extend.
     *
     * @param printer  the printer to add
     * @param parser  the parser to add
     * @return this DateTimeFormatterBuilder
     * @throws IllegalArgumentException if printer or parser is null or of an invalid type
     */
    public DateTimeFormatterBuilder append(DateTimePrinter printer, DateTimeParser parser) {
        checkPrinter(printer);
        checkParser(parser);
        return append0(printer, parser);
    }

    /**
     * Appends a printer and a set of matching parsers. When parsing, the first
     * parser in the list is selected for parsing. If it fails, the next is
     * chosen, and so on. If none of these parsers succeeds, then the failed
     * position of the parser that made the greatest progress is returned.
     * <p>
     * Only the printer is optional. In addtion, it is illegal for any but the
     * last of the parser array elements to be null. If the last element is
     * null, this represents the empty parser. The presence of an empty parser
     * indicates that the entire array of parse formats is optional.
     * <p>
     * The printer and parsers must extend <code>BaseDateTimeFormatter</code>.
     * This is an internal class, which all supplied format classes extend.
     *
     * @param printer  the printer to add
     * @param parsers  the parsers to add
     * @return this DateTimeFormatterBuilder
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
        BaseDateTimeFormatter[] copyOfParsers = new BaseDateTimeFormatter[length];
        for (int i = 0; i < length; i++) {
            DateTimeParser parser = parsers[i];
            if (i == length - 1 && parser == null) {
                // ok
            } else {
                if (parser == null) {
                    throw new IllegalArgumentException("Incomplete parser array");
                } else if (parser instanceof BaseDateTimeFormatter == false) {
                    throw new IllegalArgumentException("Parser must extend BaseDateTimeFormatter");
                }
                copyOfParsers[i] = (BaseDateTimeFormatter) parser;
            }
        }
        
        return append0(printer, new MatchingParser(copyOfParsers));
    }

    /**
     * Appends just a parser element which is optional. With no matching
     * printer, a printer cannot be built from this DateTimeFormatterBuilder.
     * <p>
     * The parser must implement <code>BaseDateTimeFormatter</code>.
     * This is an internal interface, which all supplied format classes implement.
     *
     * @return this DateTimeFormatterBuilder
     * @throws IllegalArgumentException if parser is null or of an invalid type
     */
    public DateTimeFormatterBuilder appendOptional(DateTimeParser parser) {
        checkParser(parser);
        BaseDateTimeFormatter[] parsers = new BaseDateTimeFormatter[] {
            (BaseDateTimeFormatter) parser, null};
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
        if (parser instanceof BaseDateTimeFormatter == false) {
            throw new IllegalArgumentException("Parser must extend BaseDateTimeFormatter");
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
        if (printer instanceof BaseDateTimeFormatter == false) {
            throw new IllegalArgumentException("Printer must extend BaseDateTimeFormatter");
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
            DateTimePrinter printer, DateTimeParser parser) {
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
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendLiteral(char c) {
        return append0(new CharacterLiteral(c));
    }

    /**
     * Instructs the printer to emit specific text, and the parser to expect
     * it. The parser is case-insensitive.
     *
     * @return this DateTimeFormatterBuilder
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
     * @param fieldType type of field to append
     * @param minDigits minumum number of digits to <i>print</i>
     * @param maxDigits maximum number of digits to <i>parse</i>, or the estimated
     * maximum number of digits to print
     * @return this DateTimeFormatterBuilder
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
     * Instructs the printer to emit a field value as a decimal number, and the
     * parser to expect a signed decimal number.
     *
     * @param fieldType type of field to append
     * @param minDigits minumum number of digits to <i>print</i>
     * @param maxDigits maximum number of digits to <i>parse</i>, or the estimated
     * maximum number of digits to print
     * @return this DateTimeFormatterBuilder
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
     * Instructs the printer to emit a field value as text, and the
     * parser to expect text.
     *
     * @param fieldType type of field to append
     * @return this DateTimeFormatterBuilder
     * @throws IllegalArgumentException if field type is null
     */
    public DateTimeFormatterBuilder appendText(DateTimeFieldType fieldType) {
        if (fieldType == null) {
            throw new IllegalArgumentException("Field type must not be null");
        }
        return append0(new TextField(fieldType, iLocale, false));
    }

    /**
     * Instructs the printer to emit a field value as short text, and the
     * parser to expect text.
     *
     * @param fieldType type of field to append
     * @return this DateTimeFormatterBuilder
     * @throws IllegalArgumentException if field type is null
     */
    public DateTimeFormatterBuilder appendShortText(DateTimeFieldType fieldType) {
        if (fieldType == null) {
            throw new IllegalArgumentException("Field type must not be null");
        }
        return append0(new TextField(fieldType, iLocale, true));
    }

    /**
     * Instructs the printer to emit a remainder of time as a decimal fraction,
     * sans decimal point. For example, if the field is specified as
     * minuteOfHour and the time is 12:30:45, the value printed is 75. A
     * decimal point is implied, so the fraction is 0.75, or three-quarters of
     * a minute.
     *
     * @param fieldType type of field to append
     * @param minDigits minumum number of digits to print.
     * @param maxDigits maximum number of digits to print or parse.
     * @return this DateTimeFormatterBuilder
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
     * @param minDigits minumum number of digits to print
     * @param maxDigits maximum number of digits to print or parse
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendFractionOfSecond(int minDigits, int maxDigits) {
        return appendFraction(DateTimeFieldType.secondOfDay(), minDigits, maxDigits);
    }

    /**
     * @param minDigits minumum number of digits to print
     * @param maxDigits maximum number of digits to print or parse
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendFractionOfMinute(int minDigits, int maxDigits) {
        return appendFraction(DateTimeFieldType.minuteOfDay(), minDigits, maxDigits);
    }

    /**
     * @param minDigits minumum number of digits to print
     * @param maxDigits maximum number of digits to print or parse
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendFractionOfHour(int minDigits, int maxDigits) {
        return appendFraction(DateTimeFieldType.hourOfDay(), minDigits, maxDigits);
    }

    /**
     * @param minDigits minumum number of digits to print
     * @param maxDigits maximum number of digits to print or parse
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendFractionOfDay(int minDigits, int maxDigits) {
        return appendFraction(DateTimeFieldType.dayOfYear(), minDigits, maxDigits);
    }

    /**
     * Instructs the printer to emit a numeric millisOfSecond field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendMillisOfSecond(int minDigits) {
        return appendDecimal(DateTimeFieldType.millisOfSecond(), minDigits, 3);
    }

    /**
     * Instructs the printer to emit a numeric millisOfDay field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendMillisOfDay(int minDigits) {
        return appendDecimal(DateTimeFieldType.millisOfDay(), minDigits, 8);
    }

    /**
     * Instructs the printer to emit a numeric secondOfMinute field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendSecondOfMinute(int minDigits) {
        return appendDecimal(DateTimeFieldType.secondOfMinute(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric secondOfDay field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendSecondOfDay(int minDigits) {
        return appendDecimal(DateTimeFieldType.secondOfDay(), minDigits, 5);
    }

    /**
     * Instructs the printer to emit a numeric minuteOfHour field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendMinuteOfHour(int minDigits) {
        return appendDecimal(DateTimeFieldType.minuteOfHour(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric minuteOfDay field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendMinuteOfDay(int minDigits) {
        return appendDecimal(DateTimeFieldType.minuteOfDay(), minDigits, 4);
    }

    /**
     * Instructs the printer to emit a numeric hourOfDay field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendHourOfDay(int minDigits) {
        return appendDecimal(DateTimeFieldType.hourOfDay(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric clockhourOfDay field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendClockhourOfDay(int minDigits) {
        return appendDecimal(DateTimeFieldType.clockhourOfDay(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric hourOfHalfday field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendHourOfHalfday(int minDigits) {
        return appendDecimal(DateTimeFieldType.hourOfHalfday(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric clockhourOfHalfday field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendClockhourOfHalfday(int minDigits) {
        return appendDecimal(DateTimeFieldType.clockhourOfHalfday(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric dayOfWeek field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendDayOfWeek(int minDigits) {
        return appendDecimal(DateTimeFieldType.dayOfWeek(), minDigits, 1);
    }

    /**
     * Instructs the printer to emit a numeric dayOfMonth field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendDayOfMonth(int minDigits) {
        return appendDecimal(DateTimeFieldType.dayOfMonth(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric dayOfYear field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendDayOfYear(int minDigits) {
        return appendDecimal(DateTimeFieldType.dayOfYear(), minDigits, 3);
    }

    /**
     * Instructs the printer to emit a numeric weekOfWeekyear field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendWeekOfWeekyear(int minDigits) {
        return appendDecimal(DateTimeFieldType.weekOfWeekyear(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric weekyear field.
     *
     * @param minDigits minumum number of digits to <i>print</i>
     * @param maxDigits maximum number of digits to <i>parse</i>, or the estimated
     * maximum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendWeekyear(int minDigits, int maxDigits) {
        return appendSignedDecimal(DateTimeFieldType.weekyear(), minDigits, maxDigits);
    }

    /**
     * Instructs the printer to emit a numeric monthOfYear field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendMonthOfYear(int minDigits) {
        return appendDecimal(DateTimeFieldType.monthOfYear(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric year field.
     *
     * @param minDigits minumum number of digits to <i>print</i>
     * @param maxDigits maximum number of digits to <i>parse</i>, or the estimated
     * maximum number of digits to print
     * @return this DateTimeFormatterBuilder
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
     * @param pivot pivot year to use when parsing
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendTwoDigitYear(int pivot) {
        return append0(new TwoDigitYear(pivot));
    }

    /**
     * Instructs the printer to emit a numeric yearOfEra field.
     *
     * @param minDigits minumum number of digits to <i>print</i>
     * @param maxDigits maximum number of digits to <i>parse</i>, or the estimated
     * maximum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendYearOfEra(int minDigits, int maxDigits) {
        return appendDecimal(DateTimeFieldType.yearOfEra(), minDigits, maxDigits);
    }

    /**
     * Instructs the printer to emit a numeric year of century field.
     *
     * @param minDigits minumum number of digits to print
     * @param maxDigits maximum number of digits to <i>parse</i>, or the estimated
     * maximum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendYearOfCentury(int minDigits, int maxDigits) {
        return appendDecimal(DateTimeFieldType.yearOfCentury(), minDigits, maxDigits);
    }

    /**
     * Instructs the printer to emit a numeric century of era field.
     *
     * @param minDigits minumum number of digits to print
     * @param maxDigits maximum number of digits to <i>parse</i>, or the estimated
     * maximum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendCenturyOfEra(int minDigits, int maxDigits) {
        return appendSignedDecimal(DateTimeFieldType.centuryOfEra(), minDigits, maxDigits);
    }

    /**
     * Instructs the printer to emit a locale-specific AM/PM text, and the
     * parser to expect it. The parser is case-insensitive.
     *
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendHalfdayOfDayText() {
        return appendText(DateTimeFieldType.halfdayOfDay());
    }

    /**
     * Instructs the printer to emit a locale-specific dayOfWeek text. The
     * parser will accept a long or short dayOfWeek text, case-insensitive.
     *
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendDayOfWeekText() {
        return appendText(DateTimeFieldType.dayOfWeek());
    }

    /**
     * Instructs the printer to emit a short locale-specific dayOfWeek
     * text. The parser will accept a long or short dayOfWeek text,
     * case-insensitive.
     *
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendDayOfWeekShortText() {
        return appendShortText(DateTimeFieldType.dayOfWeek());
    }

    /**
     * Instructs the printer to emit a short locale-specific monthOfYear
     * text. The parser will accept a long or short monthOfYear text,
     * case-insensitive.
     *
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendMonthOfYearText() { 
        return appendText(DateTimeFieldType.monthOfYear());
    }

    /**
     * Instructs the printer to emit a locale-specific monthOfYear text. The
     * parser will accept a long or short monthOfYear text, case-insensitive.
     *
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendMonthOfYearShortText() {
        return appendShortText(DateTimeFieldType.monthOfYear());
    }

    /**
     * Instructs the printer to emit a locale-specific era text (BC/AD), and
     * the parser to expect it. The parser is case-insensitive.
     *
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendEraText() {
        return appendText(DateTimeFieldType.era());
    }

    /**
     * Instructs the printer to emit a locale-specific time zone name. A
     * parser cannot be created from this builder if a time zone name is
     * appended.
     *
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendTimeZoneName() {
        return append0(new TimeZonePrinter( iLocale, false), null);
    }

    /**
     * Instructs the printer to emit a short locale-specific time zone
     * name. A parser cannot be created from this builder if time zone
     * name is appended.
     *
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendTimeZoneShortName() {
        return append0(new TimeZonePrinter( iLocale, true), null);
    }

    /**
     * Instructs the printer to emit text and numbers to display time zone
     * offset from UTC. A parser will use the parsed time zone offset to adjust
     * the datetime.
     *
     * @param zeroOffsetText Text to use if time zone offset is zero. If
     * null, offset is always shown.
     * @param showSeparators If true, prints ':' separator before minute and
     * second field and prints '.' separator before fraction field.
     * @param minFields minimum number of fields to print, stopping when no
     * more precision is required. 1=hours, 2=minutes, 3=seconds, 4=fraction
     * @param maxFields maximum number of fields to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendTimeZoneOffset(
            String zeroOffsetText, boolean showSeparators,
            int minFields, int maxFields) {
        return append0(new TimeZoneOffsetFormatter
                       (zeroOffsetText, showSeparators, minFields, maxFields));
    }

    /**
     * Calls upon {@link DateTimeFormat} to parse the pattern and append the
     * results into this builder.
     *
     * @param pattern  pattern specification
     * @throws IllegalArgumentException if the pattern is invalid
     * @see DateTimeFormat#appendPatternTo(DateTimeFormatterBuilder,String)
     */
    public DateTimeFormatterBuilder appendPattern(String pattern) {
        DateTimeFormat.appendPatternTo(this, pattern);
        return this;
    }

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
        if (f instanceof DateTimePrinter) {
            if (f instanceof Composite) {
                return ((Composite)f).isPrinter();
            }
            return true;
        }
        return false;
    }

    private boolean isParser(Object f) {
        if (f instanceof DateTimeParser) {
            if (f instanceof Composite) {
                return ((Composite)f).isParser();
            }
            return true;
        }
        return false;
    }

    private boolean isFormatter(Object f) {
        if (f instanceof DateTimeFormatter) {
            if (f instanceof Composite) {
                return ((Composite)f).isPrinter()
                    && ((Composite)f).isParser();
            }
            return true;
        }
        return false;
    }

    static void appendUnknownString(StringBuffer buf, int len) {
        for (int i = len; --i >= 0;) {
            buf.append('\ufffd');
        }
    }

    static void printUnknownString(Writer out, int len) throws IOException {
        for (int i = len; --i >= 0;) {
            out.write('\ufffd');
        }
    }

    //-----------------------------------------------------------------------
    static class CharacterLiteral
            extends BaseDateTimeFormatter
            implements DateTimeFormatter, BoundDateTimePrinter {

        private final char iValue;

        CharacterLiteral(char value) {
            super();
            iValue = value;
        }

        public int estimatePrintedLength() {
            return 1;
        }

        protected void printTo(StringBuffer buf,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) {
            buf.append(iValue);
        }

        protected void printTo(Writer out,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) throws IOException {
            out.write(iValue);
        }

        public void printTo(StringBuffer buf, ReadablePartial partial) {
            buf.append(iValue);
        }

        public void printTo(Writer out, ReadablePartial partial) throws IOException {
            out.write(iValue);
        }

        protected String print(long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) {
            return String.valueOf(iValue);
        }

        public String print(ReadablePartial partial) {
            return String.valueOf(iValue);
        }

        protected int estimateParsedLength() {
            return 1;
        }

        protected int parseInto(DateTimeParserBucket bucket, String text, int position) {
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
            extends BaseDateTimeFormatter
            implements DateTimeFormatter, BoundDateTimePrinter {

        private final String iValue;

        StringLiteral(String value) {
            super();
            iValue = value;
        }

        public int estimatePrintedLength() {
            return iValue.length();
        }

        protected void printTo(StringBuffer buf,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) {
            buf.append(iValue);
        }

        protected void printTo(Writer out,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) throws IOException {
            out.write(iValue);
        }

        public void printTo(StringBuffer buf, ReadablePartial partial) {
            buf.append(iValue);
        }

        public void printTo(Writer out, ReadablePartial partial) throws IOException {
            out.write(iValue);
        }

        protected String print(long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) {
            return iValue;
        }

        public String print(ReadablePartial partial) {
            return iValue;
        }

        public int estimateParsedLength() {
            return iValue.length();
        }

        public int parseInto(DateTimeParserBucket bucket, String text, int position) {
            if (text.regionMatches(true, position, iValue, 0, iValue.length())) {
                return position + iValue.length();
            }
            return ~position;
        }
    }

    //-----------------------------------------------------------------------
    static abstract class NumberFormatter
            extends BaseDateTimeFormatter
            implements DateTimeFormatter {
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

        protected int estimateParsedLength() {
            return iMaxParsedDigits;
        }

        protected int parseInto(DateTimeParserBucket bucket, String text, int position) {
            int limit = Math.min(iMaxParsedDigits, text.length() - position);

            boolean negative = false;
            int length = 0;
            while (length < limit) {
                char c = text.charAt(position + length);
                if (length == 0 && (c == '-' || c == '+') && iSigned) {
                    negative = c == '-';
                    if (negative) {
                        length++;
                    } else {
                        // Skip the '+' for parseInt to succeed.
                        position++;
                    }
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
                value = Integer.parseInt
                    (text.substring(position, position += length));
            } else {
                int i = position;
                if (negative) {
                    i++;
                }
                value = text.charAt(i++) - '0';
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

        public BoundDateTimePrinter bindPrinter(Chronology chrono) {
            chrono = DateTimeUtils.getChronology(chrono);
            DateTimeField field = iFieldType.getField(chrono.withUTC());
            return new BoundUnpaddedNumber(field, iMaxParsedDigits, iSigned);
        }

        protected int estimatePrintedLength() {
            return iMaxParsedDigits;
        }

        protected void printTo(StringBuffer buf,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) {
            try {
                DateTimeField field = iFieldType.getField(chronoLocal);
                FormatUtils.appendUnpaddedInteger(buf, field.get(instantLocal));
            } catch (RuntimeException e) {
                buf.append('\ufffd');
            }
        }

        protected void printTo(Writer out,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) throws IOException {
            try {
                DateTimeField field = iFieldType.getField(chronoLocal);
                FormatUtils.writeUnpaddedInteger(out, field.get(instantLocal));
            } catch (RuntimeException e) {
                out.write('\ufffd');
            }
        }

        public void printTo(StringBuffer buf, ReadablePartial partial) {
            if (partial.isSupported(iFieldType)) {
                try {
                    FormatUtils.appendUnpaddedInteger(buf, partial.get(iFieldType));
                } catch (RuntimeException e) {
                    buf.append('\ufffd');
                }
            } else {
                buf.append('\ufffd');
            }
        }

        public void printTo(Writer out, ReadablePartial partial) throws IOException {
            if (partial.isSupported(iFieldType)) {
                try {
                    FormatUtils.writeUnpaddedInteger(out, partial.get(iFieldType));
                } catch (RuntimeException e) {
                    out.write('\ufffd');
                }
            } else {
                out.write('\ufffd');
            }
        }
    }

    //-----------------------------------------------------------------------
    static class BoundUnpaddedNumber
            extends UnpaddedNumber
            implements BoundDateTimePrinter {

        private final DateTimeField iField;

        BoundUnpaddedNumber(DateTimeField field, int maxParsedDigits, boolean signed) {
            super(field.getType(), maxParsedDigits, signed);
            iField = field;
        }

        protected void printTo(StringBuffer buf,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) {
            try {
                FormatUtils.appendUnpaddedInteger(buf, iField.get(instantLocal));
            } catch (RuntimeException e) {
                buf.append('\ufffd');
            }
        }

        protected void printTo(Writer out,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) throws IOException {
            try {
                FormatUtils.writeUnpaddedInteger(out, iField.get(instantLocal));
            } catch (RuntimeException e) {
                out.write('\ufffd');
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

        public BoundDateTimePrinter bindPrinter(Chronology chrono) {
            chrono = DateTimeUtils.getChronology(chrono);
            DateTimeField field = iFieldType.getField(chrono.withUTC());
            return new BoundPaddedNumber(field, iMaxParsedDigits, iSigned, iMinPrintedDigits);
        }

        protected int estimatePrintedLength() {
            return iMaxParsedDigits;
        }

        protected void printTo(StringBuffer buf,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) {
            try {
                DateTimeField field = iFieldType.getField(chronoLocal);
                FormatUtils.appendPaddedInteger(buf, field.get(instantLocal), iMinPrintedDigits);
            } catch (RuntimeException e) {
                appendUnknownString(buf, iMinPrintedDigits);
            }
        }

        protected void printTo(Writer out,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) throws IOException {
            try {
                DateTimeField field = iFieldType.getField(chronoLocal);
                FormatUtils.writePaddedInteger(out, field.get(instantLocal), iMinPrintedDigits);
            } catch (RuntimeException e) {
                printUnknownString(out, iMinPrintedDigits);
            }
        }

        public void printTo(StringBuffer buf, ReadablePartial partial) {
            if (partial.isSupported(iFieldType)) {
                try {
                    FormatUtils.appendPaddedInteger(buf, partial.get(iFieldType), iMinPrintedDigits);
                } catch (RuntimeException e) {
                    appendUnknownString(buf, iMinPrintedDigits);
                }
            } else {
                appendUnknownString(buf, iMinPrintedDigits);
            }
        }

        public void printTo(Writer out, ReadablePartial partial) throws IOException {
            if (partial.isSupported(iFieldType)) {
                try {
                    FormatUtils.writePaddedInteger(out, partial.get(iFieldType), iMinPrintedDigits);
                } catch (RuntimeException e) {
                    printUnknownString(out, iMinPrintedDigits);
                }
            } else {
                printUnknownString(out, iMinPrintedDigits);
            }
        }
    }

    //-----------------------------------------------------------------------
    static class BoundPaddedNumber
            extends PaddedNumber
            implements BoundDateTimePrinter {

        private final DateTimeField iField;

        BoundPaddedNumber(DateTimeField field, int maxParsedDigits,
                          boolean signed, int minPrintedDigits)
        {
            super(field.getType(), maxParsedDigits, signed, minPrintedDigits);
            iField = field;
        }

        protected void printTo(StringBuffer buf,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) {
            try {
                FormatUtils.appendPaddedInteger(buf, iField.get(instantLocal), iMinPrintedDigits);
            } catch (RuntimeException e) {
                appendUnknownString(buf, iMinPrintedDigits);
            }
        }

        protected void printTo(Writer out,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) throws IOException {
            try {
                FormatUtils.writePaddedInteger(out, iField.get(instantLocal), iMinPrintedDigits);
            } catch (RuntimeException e) {
                printUnknownString(out, iMinPrintedDigits);
            }
        }
    }

    //-----------------------------------------------------------------------
    static class TwoDigitYear
            extends BaseDateTimeFormatter
            implements DateTimeFormatter, BoundDateTimePrinter {

        private final int iPivot;

        TwoDigitYear(int pivot) {
            super();
            iPivot = pivot;
        }

        protected int estimateParsedLength() {
            return 2;
        }

        protected int parseInto(DateTimeParserBucket bucket, String text, int position) {
            int limit = Math.min(2, text.length() - position);
            if (limit < 2) {
                return ~position;
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

            int low = iPivot - 50;

            int t;
            if (low >= 0) {
                t = low % 100;
            } else {
                t = 99 + ((low + 1) % 100);
            }

            year += low + ((year < t) ? 100 : 0) - t;

            bucket.saveField(DateTimeFieldType.year(), year);
            return position + 2;
        }
        
        protected int estimatePrintedLength() {
            return 2;
        }

        protected void printTo(StringBuffer buf,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) {
            int year = getTwoDigitYear(instantLocal, chronoLocal);
            if (year < 0) {
                buf.append('\ufffd');
                buf.append('\ufffd');
            } else {
                FormatUtils.appendPaddedInteger(buf, year, 2);
            }
        }

        protected void printTo(Writer out,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) throws IOException {
            int year = getTwoDigitYear(instantLocal, chronoLocal);
            if (year < 0) {
                out.write('\ufffd');
                out.write('\ufffd');
            } else {
                FormatUtils.writePaddedInteger(out, year, 2);
            }
        }

        private int getTwoDigitYear(long instantLocal, Chronology chronoLocal) {
            try {
                int year = chronoLocal.year().get(instantLocal);
                if (year < 0) {
                    year = -year;
                }
                return year % 100;
            } catch (RuntimeException e) {
                return -1;
            }
        }

        public void printTo(StringBuffer buf, ReadablePartial partial) {
            int year = getTwoDigitYear(partial);
            if (year < 0) {
                buf.append('\ufffd');
                buf.append('\ufffd');
            } else {
                FormatUtils.appendPaddedInteger(buf, year, 2);
            }
        }

        public void printTo(Writer out, ReadablePartial partial) throws IOException {
            int year = getTwoDigitYear(partial);
            if (year < 0) {
                out.write('\ufffd');
                out.write('\ufffd');
            } else {
                FormatUtils.writePaddedInteger(out, year, 2);
            }
        }

        private int getTwoDigitYear(ReadablePartial partial) {
            if (partial.isSupported(DateTimeFieldType.year())) {
                try {
                    int year = partial.get(DateTimeFieldType.year());
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
            extends BaseDateTimeFormatter
            implements DateTimeFormatter, BoundDateTimePrinter {

        private final DateTimeFieldType iFieldType;
        private final Locale iLocale;
        private final boolean iShort;

        TextField(DateTimeFieldType fieldType, Locale locale, boolean isShort) {
            super();
            iFieldType = fieldType;
            iLocale = locale;
            iShort = isShort;
        }

        protected int estimatePrintedLength() {
            return iShort ? 6 : 20;
        }

        protected void printTo(StringBuffer buf,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) {
            try {
                buf.append(print(instantLocal, chronoLocal, instant, chrono));
            } catch (RuntimeException e) {
                buf.append('\ufffd');
            }
        }

        protected void printTo(Writer out,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) throws IOException {
            try {
                out.write(print(instantLocal, chronoLocal, instant, chrono));
            } catch (RuntimeException e) {
                out.write('\ufffd');
            }
        }

        public void printTo(StringBuffer buf, ReadablePartial partial) {
            try {
                buf.append(print(partial));
            } catch (RuntimeException e) {
                buf.append('\ufffd');
            }
        }

        public void printTo(Writer out, ReadablePartial partial) throws IOException {
            try {
                out.write(print(partial));
            } catch (RuntimeException e) {
                out.write('\ufffd');
            }
        }

        protected String print(long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) {
            DateTimeField field = iFieldType.getField(chrono);
            if (iShort) {
                return field.getAsShortText(instantLocal, iLocale);
            } else {
                return field.getAsText(instantLocal, iLocale);
            }
        }

        public String print(ReadablePartial partial) {
            if (partial.isSupported(iFieldType)) {
                DateTimeField field = iFieldType.getField(partial.getChronology());
                if (iShort) {
                    return field.getAsShortText(partial, iLocale);
                } else {
                    return field.getAsText(partial, iLocale);
                }
            } else {
                return "\ufffd";
            }
        }

        protected int estimateParsedLength() {
            return estimatePrintedLength();
        }

        protected int parseInto(DateTimeParserBucket bucket, String text, int position) {
            int limit = text.length();
            int i = position;
            for (; i<limit; i++) {
                char c = text.charAt(i);
                if (c < 'A') {
                    break;
                }
                if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || Character.isLetter(c)) {
                    continue;
                }
                break;
            }

            if (i == position) {
                return ~position;
            }

            bucket.saveField(iFieldType, text.substring(position, i), iLocale);

            return i;
        }
    }

    //-----------------------------------------------------------------------
    static class Fraction
            extends BaseDateTimeFormatter
            implements DateTimeFormatter {

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

        public BoundDateTimePrinter bindPrinter(Chronology chrono) {
            chrono = DateTimeUtils.getChronology(chrono);
            DateTimeField field = iFieldType.getField(chrono.withUTC());
            return new BoundFraction(field, iMinDigits, iMaxDigits);
        }

        protected int estimatePrintedLength() {
            return iMaxDigits;
        }

        protected void printTo(StringBuffer buf,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) {
            try {
                printTo(buf, null, instantLocal, chronoLocal);
            } catch (IOException e) {
                // Not gonna happen.
            }
        }

        protected void printTo(Writer out,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) throws IOException {
            printTo(null, out, instantLocal, chronoLocal);
        }

        public void printTo(StringBuffer buf, ReadablePartial partial) {
            if (partial.isSupported(iFieldType)) {
                long millis = partial.getChronology().set(partial, 0L);
                try {
                    printTo(buf, null, millis, partial.getChronology());
                } catch (IOException e) {
                    // Not gonna happen.
                }
            } else {
                buf.append('\ufffd');
            }
        }

        public void printTo(Writer out, ReadablePartial partial) throws IOException {
            if (partial.isSupported(iFieldType)) {
                long millis = partial.getChronology().set(partial, 0L);
                printTo(null, out, millis, partial.getChronology());
            } else {
                out.write('\ufffd');
            }
        }

        protected void printTo(StringBuffer buf, Writer out, long instantLocal, Chronology chronoLocal)
            throws IOException
        {
            DateTimeField field = iFieldType.getField(chronoLocal);
            int minDigits = iMinDigits;

            long fraction;
            try {
                fraction = field.remainder(instantLocal);
            } catch (RuntimeException e) {
                if (buf != null) {
                    appendUnknownString(buf, minDigits);
                } else {
                    printUnknownString(out, minDigits);
                }
                return;
            }

            if (fraction == 0) {
                if (buf != null) {
                    while (--minDigits >= 0) {
                        buf.append('0');
                    }
                } else {
                    while (--minDigits >= 0) {
                        out.write('0');
                    }
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
                if (buf != null) {
                    buf.append('0');
                } else {
                    out.write('0');
                }
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
                    if (buf != null) {
                        for (int i=0; i<length; i++) {
                            buf.append(str.charAt(i));
                        }
                    } else {
                        for (int i=0; i<length; i++) {
                            out.write(str.charAt(i));
                        }
                    }
                    return;
                }
            }

            if (buf != null) {
                buf.append(str);
            } else {
                out.write(str);
            }
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

        protected int estimateParsedLength() {
            return iMaxDigits;
        }

        protected int parseInto(DateTimeParserBucket bucket, String text, int position) {
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
    static class BoundFraction
            extends Fraction
            implements BoundDateTimePrinter {

        private final DateTimeField iField;
        private final long iScalar;
        private final long iRangeMillis;

        BoundFraction(DateTimeField field, int minDigits, int maxDigits) {
            super(field.getType(), minDigits, maxDigits);
            iField = field;
            iMinDigits = minDigits;
            
            long rangeMillis = field.getDurationField().getUnitMillis();
            long scalar;
            while (true) {
                switch (maxDigits) {  // know this is 18 or less
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
            iMaxDigits = maxDigits;
            iScalar = scalar;
            iRangeMillis = rangeMillis;
        }

        protected void printTo(StringBuffer buf, Writer out, long instantLocal, Chronology chronoLocal)
            throws IOException
        {
            DateTimeField field = iField;
            int minDigits = iMinDigits;

            long fraction;
            try {
                fraction = field.remainder(instantLocal);
            } catch (RuntimeException e) {
                if (buf != null) {
                    appendUnknownString(buf, minDigits);
                } else {
                    printUnknownString(out, minDigits);
                }
                return;
            }

            if (fraction == 0) {
                if (buf != null) {
                    while (--minDigits >= 0) {
                        buf.append('0');
                    }
                } else {
                    while (--minDigits >= 0) {
                        out.write('0');
                    }
                }
                return;
            }

            String str;
            long scaled = fraction * iScalar / iRangeMillis;
            int maxDigits = iMaxDigits;
            
            if ((scaled & 0x7fffffff) == scaled) {
                str = Integer.toString((int) scaled);
            } else {
                str = Long.toString(scaled);
            }

            int length = str.length();
            int digits = maxDigits;
            while (length < digits) {
                if (buf != null) {
                    buf.append('0');
                } else {
                    out.write('0');
                }
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
                    if (buf != null) {
                        for (int i=0; i<length; i++) {
                            buf.append(str.charAt(i));
                        }
                    } else {
                        for (int i=0; i<length; i++) {
                            out.write(str.charAt(i));
                        }
                    }
                    return;
                }
            }

            if (buf != null) {
                buf.append(str);
            } else {
                out.write(str);
            }
        }
    }

    //-----------------------------------------------------------------------
    static class TimeZoneOffsetFormatter
            extends BaseDateTimeFormatter
            implements DateTimeFormatter, BoundDateTimePrinter {

        private final String iZeroOffsetText;
        private final boolean iShowSeparators;
        private final int iMinFields;
        private final int iMaxFields;

        TimeZoneOffsetFormatter(String zeroOffsetText,
                                boolean showSeparators,
                                int minFields, int maxFields)
        {
            super();
            iZeroOffsetText = zeroOffsetText;
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
            
        protected int estimatePrintedLength() {
            int est = 1 + iMinFields << 1;
            if (iShowSeparators) {
                est += iMinFields - 1;
            }
            if (iZeroOffsetText != null && iZeroOffsetText.length() > est) {
                est = iZeroOffsetText.length();
            }
            return est;
        }
        
        protected void printTo(StringBuffer buf,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) {
            int offset = (int)(instantLocal - instant);

            if (offset == 0 && iZeroOffsetText != null) {
                buf.append(iZeroOffsetText);
                return;
            }
            if (offset >= 0) {
                buf.append('+');
            } else {
                buf.append('-');
                offset = -offset;
            }

            int hours = offset / DateTimeConstants.MILLIS_PER_HOUR;
            FormatUtils.appendPaddedInteger(buf, hours, 2);
            if (iMaxFields == 1) {
                return;
            }
            offset -= hours * (int)DateTimeConstants.MILLIS_PER_HOUR;
            if (offset == 0 && iMinFields <= 1) {
                return;
            }

            int minutes = offset / DateTimeConstants.MILLIS_PER_MINUTE;
            if (iShowSeparators) {
                buf.append(':');
            }
            FormatUtils.appendPaddedInteger(buf, minutes, 2);
            if (iMaxFields == 2) {
                return;
            }
            offset -= minutes * DateTimeConstants.MILLIS_PER_MINUTE;
            if (offset == 0 && iMinFields <= 2) {
                return;
            }

            int seconds = offset / DateTimeConstants.MILLIS_PER_SECOND;
            if (iShowSeparators) {
                buf.append(':');
            }
            FormatUtils.appendPaddedInteger(buf, seconds, 2);
            if (iMaxFields == 3) {
                return;
            }
            offset -= seconds * DateTimeConstants.MILLIS_PER_SECOND;
            if (offset == 0 && iMinFields <= 3) {
                return;
            }

            if (iShowSeparators) {
                buf.append('.');
            }
            FormatUtils.appendPaddedInteger(buf, offset, 3);
        }
        
        protected void printTo(Writer out,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) throws IOException {
            int offset = (int)(instantLocal - instant);

            if (offset == 0 && iZeroOffsetText != null) {
                out.write(iZeroOffsetText);
                return;
            }
            if (offset >= 0) {
                out.write('+');
            } else {
                out.write('-');
                offset = -offset;
            }

            int hours = offset / DateTimeConstants.MILLIS_PER_HOUR;
            FormatUtils.writePaddedInteger(out, hours, 2);
            if (iMaxFields == 1) {
                return;
            }
            offset -= hours * (int)DateTimeConstants.MILLIS_PER_HOUR;
            if (offset == 0 && iMinFields == 1) {
                return;
            }

            int minutes = offset / DateTimeConstants.MILLIS_PER_MINUTE;
            if (iShowSeparators) {
                out.write(':');
            }
            FormatUtils.writePaddedInteger(out, minutes, 2);
            if (iMaxFields == 2) {
                return;
            }
            offset -= minutes * DateTimeConstants.MILLIS_PER_MINUTE;
            if (offset == 0 && iMinFields == 2) {
                return;
            }

            int seconds = offset / DateTimeConstants.MILLIS_PER_SECOND;
            if (iShowSeparators) {
                out.write(':');
            }
            FormatUtils.writePaddedInteger(out, seconds, 2);
            if (iMaxFields == 3) {
                return;
            }
            offset -= seconds * DateTimeConstants.MILLIS_PER_SECOND;
            if (offset == 0 && iMinFields == 3) {
                return;
            }

            if (iShowSeparators) {
                out.write('.');
            }
            FormatUtils.writePaddedInteger(out, offset, 3);
        }

        public void printTo(StringBuffer buf, ReadablePartial partial) {
            // no zone info
        }

        public void printTo(Writer out, ReadablePartial partial) throws IOException {
            // no zone info
        }

        protected int estimateParsedLength() {
            return estimatePrintedLength();
        }

        protected int parseInto(DateTimeParserBucket bucket, String text, int position) {
            int limit = text.length() - position;

            zeroOffset:
            if (iZeroOffsetText != null) {
                if (iZeroOffsetText.length() == 0) {
                    // Peek ahead, looking for sign character.
                    if (limit > 0) {
                        char c = text.charAt(position);
                        if (c == '-' || c == '+') {
                            break zeroOffset;
                        }
                    }
                    bucket.setOffset(0);
                    return position;
                }
                if (text.regionMatches(true, position, iZeroOffsetText, 0,
                                       iZeroOffsetText.length())) {
                    bucket.setOffset(0);
                    return position + iZeroOffsetText.length();
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

            bucket.setOffset(negative ? -offset : offset);
            return position;
        }

        /**
         * Returns actual amount of digits to parse, but no more than original
         * 'amount' parameter.
         */
        private int digitCount(String text, int position, int amount) {
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
    static class TimeZonePrinter
            extends BaseDateTimeFormatter
            implements DateTimePrinter, BoundDateTimePrinter {

        private final Locale iLocale;
        private final boolean iShortFormat;

        TimeZonePrinter(Locale locale, boolean shortFormat) {
            super();
            iLocale = locale;
            iShortFormat = shortFormat;
        }

        protected int estimatePrintedLength() {
            return iShortFormat ? 4 : 20;
        }

        protected void printTo(StringBuffer buf,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) {
            buf.append(print(instantLocal, chronoLocal, instant, chrono));
        }

        protected void printTo(Writer out,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) throws IOException {
            out.write(print(instantLocal, chronoLocal, instant, chrono));
        }

        protected String print(long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) {
            DateTimeZone zone = chrono.getZone();
            if (iShortFormat) {
                return zone.getShortName(instant, this.iLocale);
            } else {
                return zone.getName(instant, this.iLocale);
            }
        }

        public void printTo(StringBuffer buf, ReadablePartial partial) {
            // no zone info
        }

        public void printTo(Writer out, ReadablePartial partial) throws IOException {
            // no zone info
        }
    }

    //-----------------------------------------------------------------------
    static class Composite
            extends BaseDateTimeFormatter
            implements DateTimeFormatter, BoundDateTimePrinter {

        private final BaseDateTimeFormatter[] iPrinters;
        private final BaseDateTimeFormatter[] iParsers;

        private final int iPrintedLengthEstimate;
        private final int iParsedLengthEstimate;

        Composite(List elementPairs) {
            super();

            List printerList = new ArrayList();
            List parserList = new ArrayList();

            decompose(elementPairs, printerList, parserList);

            if (printerList.size() <= 0) {
                iPrinters = null;
                iPrintedLengthEstimate = 0;
            } else {
                int size = printerList.size();
                iPrinters = new BaseDateTimeFormatter[size];
                int printEst = 0;
                for (int i=0; i<size; i++) {
                    BaseDateTimeFormatter printer = (BaseDateTimeFormatter) printerList.get(i);
                    printEst += printer.estimatePrintedLength();
                    iPrinters[i] = printer;
                }
                iPrintedLengthEstimate = printEst;
            }

            if (parserList.size() <= 0) {
                iParsers = null;
                iParsedLengthEstimate = 0;
            } else {
                int size = parserList.size();
                iParsers = new BaseDateTimeFormatter[size];
                int parseEst = 0;
                for (int i=0; i<size; i++) {
                    BaseDateTimeFormatter parser = (BaseDateTimeFormatter) parserList.get(i);
                    parseEst += parser.estimateParsedLength();
                    iParsers[i] = parser;
                }
                iParsedLengthEstimate = parseEst;
            }
        }

        private Composite(Composite base, BaseDateTimeFormatter[] printers) {
            iPrinters = printers;
            iParsers = base.iParsers;
            iPrintedLengthEstimate = base.iPrintedLengthEstimate;
            iParsedLengthEstimate = base.iParsedLengthEstimate;
        }

        public BoundDateTimePrinter bindPrinter(Chronology chrono) {
            BaseDateTimeFormatter[] elements = iPrinters;
            if (elements == null) {
                throw new UnsupportedOperationException();
            }

            int len = elements.length;
            BaseDateTimeFormatter[] array = new BaseDateTimeFormatter[len];
            for (int i = 0; i < len; i++) {
                BoundDateTimePrinter bound = ((DateTimePrinter) elements[i]).bindPrinter(chrono);
                array[i] = (BaseDateTimeFormatter) bound;
            }
            return new Composite(this, array);
        }

        protected int estimatePrintedLength() {
            return iPrintedLengthEstimate;
        }
    
        protected void printTo(StringBuffer buf,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) {
            BaseDateTimeFormatter[] elements = iPrinters;
            if (elements == null) {
                throw new UnsupportedOperationException();
            }

            int len = elements.length;
            for (int i = 0; i < len; i++) {
                elements[i].printTo(buf, instantLocal, chronoLocal, instant, chrono);
            }
        }

        protected void printTo(Writer out,
                               long instantLocal, Chronology chronoLocal,
                               long instant, Chronology chrono) throws IOException {
            BaseDateTimeFormatter[] elements = iPrinters;
            if (elements == null) {
                throw new UnsupportedOperationException();
            }

            int len = elements.length;
            for (int i = 0; i < len; i++) {
                elements[i].printTo(out, instantLocal, chronoLocal, instant, chrono);
            }
        }

        public void printTo(StringBuffer buf, ReadablePartial partial) {
            BaseDateTimeFormatter[] elements = iPrinters;
            if (elements == null) {
                throw new UnsupportedOperationException();
            }

            int len = elements.length;
            for (int i=0; i<len; i++) {
                elements[i].printTo(buf, partial);
            }
        }

        public void printTo(Writer out, ReadablePartial partial) throws IOException {
            BaseDateTimeFormatter[] elements = iPrinters;
            if (elements == null) {
                throw new UnsupportedOperationException();
            }

            int len = elements.length;
            for (int i=0; i<len; i++) {
                elements[i].printTo(out, partial);
            }
        }

        protected int estimateParsedLength() {
            return iParsedLengthEstimate;
        }

        protected int parseInto(DateTimeParserBucket bucket, String text, int position) {
            BaseDateTimeFormatter[] elements = iParsers;
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
        private void decompose(List elementPairs, List printerList, List parserList) {
            int size = elementPairs.size();
            for (int i=0; i<size; i+=2) {
                Object element = elementPairs.get(i);
                if (element != null && element instanceof DateTimePrinter) {
                    if (element instanceof Composite) {
                        addArrayToList(printerList, ((Composite)element).iPrinters);
                    } else {
                        printerList.add(element);
                    }
                }

                element = elementPairs.get(i + 1);
                if (element != null && element instanceof DateTimeParser) {
                    if (element instanceof Composite) {
                        addArrayToList(parserList, ((Composite)element).iParsers);
                    } else {
                        parserList.add(element);
                    }
                }
            }
        }

        private void addArrayToList(List list, Object[] array) {
            if (array != null) {
                for (int i=0; i<array.length; i++) {
                    list.add(array[i]);
                }
            }
        }
    }

    //-----------------------------------------------------------------------
    static class MatchingParser
            extends BaseDateTimeFormatter
            implements DateTimeParser {

        private final BaseDateTimeFormatter[] iParsers;
        private final int iParsedLengthEstimate;

        MatchingParser(BaseDateTimeFormatter[] parsers) {
            super();
            iParsers = parsers;
            int est = 0;
            for (int i=parsers.length; --i>=0 ;) {
                BaseDateTimeFormatter parser = parsers[i];
                if (parser != null) {
                    int len = parser.estimateParsedLength();
                    if (len > est) {
                        len = est;
                    }
                }
            }
            iParsedLengthEstimate = est;
        }

        protected int estimateParsedLength() {
            return iParsedLengthEstimate;
        }

        protected int parseInto(DateTimeParserBucket bucket, String text, int position) {
            BaseDateTimeFormatter[] parsers = iParsers;
            int length = parsers.length;

            final Object originalState = bucket.saveState();
            boolean isOptional = false;

            int bestValidPos = position;
            Object bestValidState = null;

            int bestInvalidPos = position;

            for (int i=0; i<length; i++) {
                BaseDateTimeFormatter parser = parsers[i];
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
}
