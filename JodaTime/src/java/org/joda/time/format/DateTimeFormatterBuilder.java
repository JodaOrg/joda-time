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
import java.util.Locale;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.MillisDurationField;
import org.joda.time.chrono.PreciseDateTimeField;
import org.joda.time.chrono.iso.ISOChronology;

/**
 * DateTimeFormatterBuilder is used for constructing {@link DateTimeFormatter}s.
 * DateTimeFormatters can be built by appending specific fields or other
 * formatters.
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
 * @see DateTimeFormat
 * @author Brian S O'Neill
 */
public class DateTimeFormatterBuilder {

    private final Chronology iChrono;
    private final Chronology iChronoUTC;
    private final Locale iLocale;

    // Array contents alternate between printers and parsers.
    private ArrayList iElementPairs;
    private Object iFormatter;

    /**
     * Creates a DateTimeFormatterBuilder with {@link ISOChronology}, in the
     * default time zone and locale.
     */
    public DateTimeFormatterBuilder() {
        this(ISOChronology.getInstance());
    }

    /**
     * Creates a DateTimeFormatterBuilder with {@link ISOChronology}, in the
     * given time zone, with the default locale.
     */
    public DateTimeFormatterBuilder(final DateTimeZone zone) {
        this(ISOChronology.getInstance(zone));
    }

    /**
     * Creates a DateTimeFormatterBuilder with {@link ISOChronology}, in the
     * given time zone, with any locale.
     */
    public DateTimeFormatterBuilder(final DateTimeZone zone, final Locale locale) {
        this(ISOChronology.getInstance(zone), locale);
    }

    /**
     * Creates a DateTimeFormatterBuilder with any chronology and the default
     * locale.
     *
     * @param chrono Chronology to use
     */
    public DateTimeFormatterBuilder(final Chronology chrono) {
        this(chrono, Locale.getDefault());
    }

    /**
     * Creates a DateTimeFormatterBuilder with any chronology and locale.
     *
     * @param chrono Chronology to use, or null for default iso
     * @param locale Locale to use, or null for default
     */
    public DateTimeFormatterBuilder(Chronology chrono, Locale locale) {
        if (chrono == null) {
            chrono = ISOChronology.getInstance();
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        iChrono = chrono;
        iChronoUTC = chrono.withUTC();
        DateTimeZone zone = chrono.getDateTimeZone();
        iLocale = locale;
        iElementPairs = new ArrayList();
    }

    /**
     * Returns the chronology being used by the formatter builder.
     */
    public Chronology getChronology() {
        return iChrono;
    }

    /**
     * Returns the locale being used the formatter builder.
     */
    public Locale getLocale() {
        return iLocale;
    }

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

    /**
     * Clears out all the appended elements, allowing this builder to be
     * reused.
     */
    public void clear() {
        iFormatter = null;
        iElementPairs.clear();
    }

    /**
     * Appends another formatter.
     *
     * @return this DateTimeFormatterBuilder
     * @throws IllegalArgumentException if formatter is null
     */
    public DateTimeFormatterBuilder append(final DateTimeFormatter formatter)
        throws IllegalArgumentException
    {
        if (formatter == null) {
            throw new IllegalArgumentException("No formatter supplied");
        }
        return append0(formatter);
    }

    /**
     * Appends just a printer. With no matching parser, a parser cannot be
     * built from this DateTimeFormatterBuilder.
     *
     * @return this DateTimeFormatterBuilder
     * @throws IllegalArgumentException if printer is null
     */
    public DateTimeFormatterBuilder append(final DateTimePrinter printer)
        throws IllegalArgumentException
    {
        if (printer == null) {
            throw new IllegalArgumentException("No printer supplied");
        }
        return append0(printer, null);
    }

    /**
     * Appends just a parser. With no matching printer, a printer cannot be
     * built from this builder.
     *
     * @return this DateTimeFormatterBuilder
     * @throws IllegalArgumentException if parser is null
     */
    public DateTimeFormatterBuilder append(final DateTimeParser parser) {
        if (parser == null) {
            throw new IllegalArgumentException("No parser supplied");
        }
        return append0(null, parser);
    }

    /**
     * Appends a printer/parser pair.
     *
     * @return this DateTimeFormatterBuilder
     * @throws IllegalArgumentException if printer or parser is null
     */
    public DateTimeFormatterBuilder append(final DateTimePrinter printer,
                                           final DateTimeParser parser)
        throws IllegalArgumentException
    {
        if (printer == null) {
            throw new IllegalArgumentException("No printer supplied");
        }
        if (parser == null) {
            throw new IllegalArgumentException("No parser supplied");
        }
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
     *
     * @return this DateTimeFormatterBuilder
     * @throws IllegalArgumentException if any parser element but the last is null
     */
    public DateTimeFormatterBuilder append(final DateTimePrinter printer,
                                           final DateTimeParser[] parsers)
        throws IllegalArgumentException
    {
        if (parsers == null) {
            throw new IllegalArgumentException("No parsers supplied");
        }
        int length = parsers.length;
        if (length == 1) {
            // If the last element is null, an exception is still thrown.
            return append(printer, parsers[0]);
        }

        DateTimeParser[] copyOfParsers = new DateTimeParser[length];
        int i;
        for (i = 0; i < length - 1; i++) {
            if ((copyOfParsers[i] = parsers[i]) == null) {
                throw new IllegalArgumentException("Incomplete parser array");
            }
        }
        copyOfParsers[i] = parsers[i];

        return append0(printer, new MatchingParser(iChrono, copyOfParsers));
    }

    /**
     * Appends just a parser element which is optional. With no matching
     * printer, a printer cannot be built from this DateTimeFormatterBuilder.
     *
     * @return this DateTimeFormatterBuilder
     * @throws IllegalArgumentException if parser is null
     */
    public DateTimeFormatterBuilder appendOptional(final DateTimeParser parser) {
        if (parser == null) {
            throw new IllegalArgumentException("No parser supplied");
        }
        return append0(null, new MatchingParser(iChrono, new DateTimeParser[] {parser, null}));
    }

    private DateTimeFormatterBuilder append0(final Object element) {
        iFormatter = null;
        // Add the element as both a printer and parser.
        iElementPairs.add(element);
        iElementPairs.add(element);
        return this;
    }

    private DateTimeFormatterBuilder append0(
            final DateTimePrinter printer, final DateTimeParser parser) {
        iFormatter = null;
        iElementPairs.add(printer);
        iElementPairs.add(parser);
        return this;
    }

    /**
     * Instructs the printer to emit a specific character, and the parser to
     * expect it. The parser is case-insensitive.
     *
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendLiteral(final char c) {
        return append0(new CharacterLiteral(iChrono, c));
    }

    /**
     * Instructs the printer to emit specific text, and the parser to expect
     * it. The parser is case-insensitive.
     *
     * @return this DateTimeFormatterBuilder
     * @throws IllegalArgumentException if text is null
     */
    public DateTimeFormatterBuilder appendLiteral(final String text) {
        if (text == null) {
            throw new IllegalArgumentException("Literal must not be null");
        }
        return append0(new StringLiteral(iChrono, text));
    }

    /**
     * Instructs the printer to emit a field value as a decimal number, and the
     * parser to expect an unsigned decimal number.
     *
     * @param field field should operate in UTC or be time zone agnostic
     * @param minDigits minumum number of digits to <i>print</i>
     * @param maxDigits maximum number of digits to <i>parse</i>, or the estimated
     * maximum number of digits to print
     * @return this DateTimeFormatterBuilder
     * @throws IllegalArgumentException if field is null
     */
    public DateTimeFormatterBuilder appendDecimal(
            DateTimeField field, int minDigits, int maxDigits) {
        if (field == null) {
            throw new IllegalArgumentException("Field must not be null");
        }
        if (maxDigits < minDigits) {
            maxDigits = minDigits;
        }
        if (minDigits < 0 || maxDigits <= 0) {
            throw new IllegalArgumentException();
        }
        if (minDigits <= 1) {
            return append0(new UnpaddedNumber(iChrono, field, maxDigits, false));
        } else {
            return append0(new PaddedNumber(iChrono, field, maxDigits, false, minDigits));
        }
    }

    /**
     * Instructs the printer to emit a field value as a decimal number, and the
     * parser to expect a signed decimal number.
     *
     * @param field field should operate in UTC or be time zone agnostic
     * @param minDigits minumum number of digits to <i>print</i>
     * @param maxDigits maximum number of digits to <i>parse</i>, or the estimated
     * maximum number of digits to print
     * @return this DateTimeFormatterBuilder
     * @throws IllegalArgumentException if field is null
     */
    public DateTimeFormatterBuilder appendSignedDecimal(
            DateTimeField field, int minDigits, int maxDigits) {
        if (field == null) {
            throw new IllegalArgumentException("Field must not be null");
        }
        if (maxDigits < minDigits) {
            maxDigits = minDigits;
        }
        if (minDigits < 0 || maxDigits <= 0) {
            throw new IllegalArgumentException();
        }
        if (minDigits <= 1) {
            return append0(new UnpaddedNumber(iChrono, field, maxDigits, true));
        } else {
            return append0(new PaddedNumber(iChrono, field, maxDigits, true, minDigits));
        }
    }

    /**
     * Instructs the printer to emit a field value as text, and the
     * parser to expect text.
     *
     * @param field field should operate in UTC or be time zone agnostic
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendText(final DateTimeField field) {
        return append0(new TextField(iChrono, field, iLocale, false));
    }

    /**
     * Instructs the printer to emit a field value as short text, and the
     * parser to expect text.
     *
     * @param field field should operate in UTC or be time zone agnostic
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendShortText(final DateTimeField field) {
        return append0(new TextField(iChrono, field, iLocale, true));
    }

    /**
     * Instructs the printer to emit a remainder of time as a decimal fraction,
     * sans decimal point. For example, if the field is specified as
     * minuteOfHour and the time is 12:30:45, the value printed is 75. A
     * decimal point is implied, so the fraction is 0.75, or three-quarters of
     * a minute.
     *
     * @param field field should operate in UTC or be time zone agnostic
     * @param minDigits minumum number of digits to print.
     * @param maxDigits maximum number of digits to print or parse.
     * @return this DateTimeFormatterBuilder
     * @throws IllegalArgumentException if field's duration is not precise
     */
    public DateTimeFormatterBuilder appendFraction(
            DateTimeField field, int minDigits, int maxDigits) {
        if (field.getDurationField().isPrecise() == false) {
            throw new IllegalArgumentException("Field duration must be precise");
        }
        if (maxDigits < minDigits) {
            maxDigits = minDigits;
        }
        if (minDigits < 0 || maxDigits <= 0) {
            throw new IllegalArgumentException();
        }
        return append0(new Fraction(iChrono, field, minDigits, maxDigits));
    }

    /**
     * @param minDigits minumum number of digits to print
     * @param maxDigits maximum number of digits to print or parse
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendFractionOfSecond(final int minDigits, final int maxDigits) {
        return appendFraction(iChronoUTC.secondOfDay(), minDigits, maxDigits);
    }

    /**
     * @param minDigits minumum number of digits to print
     * @param maxDigits maximum number of digits to print or parse
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendFractionOfMinute(final int minDigits, final int maxDigits) {
        return appendFraction(iChronoUTC.minuteOfDay(), minDigits, maxDigits);
    }

    /**
     * @param minDigits minumum number of digits to print
     * @param maxDigits maximum number of digits to print or parse
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendFractionOfHour(final int minDigits, final int maxDigits) {
        return appendFraction(iChronoUTC.hourOfDay(), minDigits, maxDigits);
    }

    /**
     * @param minDigits minumum number of digits to print
     * @param maxDigits maximum number of digits to print or parse
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendFractionOfDay(final int minDigits, final int maxDigits) {
        return appendFraction(iChronoUTC.dayOfYear(), minDigits, maxDigits);
    }

    /**
     * Instructs the printer to emit a numeric millisOfSecond field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendMillisOfSecond(final int minDigits) {
        return appendDecimal(iChronoUTC.millisOfSecond(), minDigits, 3);
    }

    /**
     * Instructs the printer to emit a numeric millisOfDay field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendMillisOfDay(final int minDigits) {
        return appendDecimal(iChronoUTC.millisOfDay(), minDigits, 8);
    }

    /**
     * Instructs the printer to emit a numeric secondOfMinute field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendSecondOfMinute(final int minDigits) {
        return appendDecimal(iChronoUTC.secondOfMinute(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric secondOfDay field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendSecondOfDay(final int minDigits) {
        return appendDecimal(iChronoUTC.secondOfDay(), minDigits, 5);
    }

    /**
     * Instructs the printer to emit a numeric minuteOfHour field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendMinuteOfHour(final int minDigits) {
        return appendDecimal(iChronoUTC.minuteOfHour(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric minuteOfDay field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendMinuteOfDay(final int minDigits) {
        return appendDecimal(iChronoUTC.minuteOfDay(), minDigits, 4);
    }

    /**
     * Instructs the printer to emit a numeric hourOfDay field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendHourOfDay(final int minDigits) {
        return appendDecimal(iChronoUTC.hourOfDay(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric clockhourOfDay field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendClockhourOfDay(final int minDigits) {
        return appendDecimal(iChronoUTC.clockhourOfDay(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric hourOfHalfday field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendHourOfHalfday(final int minDigits) {
        return appendDecimal(iChronoUTC.hourOfHalfday(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric clockhourOfHalfday field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendClockhourOfHalfday(final int minDigits) {
        return appendDecimal(iChronoUTC.clockhourOfHalfday(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric dayOfWeek field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendDayOfWeek(final int minDigits) {
        return appendDecimal(iChronoUTC.dayOfWeek(), minDigits, 1);
    }

    /**
     * Instructs the printer to emit a numeric dayOfMonth field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendDayOfMonth(final int minDigits) {
        return appendDecimal(iChronoUTC.dayOfMonth(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric dayOfYear field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendDayOfYear(final int minDigits) {
        return appendDecimal(iChronoUTC.dayOfYear(), minDigits, 3);
    }

    /**
     * Instructs the printer to emit a numeric weekOfWeekyear field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendWeekOfWeekyear(final int minDigits) {
        return appendDecimal(iChronoUTC.weekOfWeekyear(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric weekyear field.
     *
     * @param minDigits minumum number of digits to <i>print</i>
     * @param maxDigits maximum number of digits to <i>parse</i>, or the estimated
     * maximum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendWeekyear(final int minDigits, final int maxDigits) {
        return appendDecimal
            (iChronoUTC.weekyear(), minDigits, maxDigits);
    }

    /**
     * Instructs the printer to emit a numeric monthOfYear field.
     *
     * @param minDigits minumum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendMonthOfYear(final int minDigits) {
        return appendDecimal(iChronoUTC.monthOfYear(), minDigits, 2);
    }

    /**
     * Instructs the printer to emit a numeric year field.
     *
     * @param minDigits minumum number of digits to <i>print</i>
     * @param maxDigits maximum number of digits to <i>parse</i>, or the estimated
     * maximum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendYear(final int minDigits, final int maxDigits) {
        return appendSignedDecimal(iChronoUTC.year(), minDigits, maxDigits);
    }

    /**
     * Instructs the printer to emit a numeric yearOfEra field.
     *
     * @param minDigits minumum number of digits to <i>print</i>
     * @param maxDigits maximum number of digits to <i>parse</i>, or the estimated
     * maximum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendYearOfEra(final int minDigits, final int maxDigits) {
        return appendDecimal(iChronoUTC.yearOfEra(), minDigits, maxDigits);
    }

    /**
     * Instructs the printer to emit a numeric year of century field.
     *
     * @param minDigits minumum number of digits to print
     * @param maxDigits maximum number of digits to <i>parse</i>, or the estimated
     * maximum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendYearOfCentury(final int minDigits, final int maxDigits) {
        return appendDecimal(iChronoUTC.yearOfCentury(), minDigits, maxDigits);
    }

    /**
     * Instructs the printer to emit a numeric century of era field.
     *
     * @param minDigits minumum number of digits to print
     * @param maxDigits maximum number of digits to <i>parse</i>, or the estimated
     * maximum number of digits to print
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendCenturyOfEra(final int minDigits, final int maxDigits) {
        return appendSignedDecimal(iChronoUTC.centuryOfEra(), minDigits, maxDigits);
    }

    /**
     * Instructs the printer to emit a locale-specific AM/PM text, and the
     * parser to expect it. The parser is case-insensitive.
     *
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendHalfdayOfDayText() {
        return appendText(iChronoUTC.halfdayOfDay());
    }

    /**
     * Instructs the printer to emit a locale-specific dayOfWeek text. The
     * parser will accept a long or short dayOfWeek text, case-insensitive.
     *
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendDayOfWeekText() {
        return appendText(iChronoUTC.dayOfWeek());
    }

    /**
     * Instructs the printer to emit a short locale-specific dayOfWeek
     * text. The parser will accept a long or short dayOfWeek text,
     * case-insensitive.
     *
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendDayOfWeekShortText() {
        return appendShortText(iChronoUTC.dayOfWeek());
    }

    /**
     * Instructs the printer to emit a short locale-specific monthOfYear
     * text. The parser will accept a long or short monthOfYear text,
     * case-insensitive.
     *
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendMonthOfYearText() { 
        return appendText(iChronoUTC.monthOfYear());
    }

    /**
     * Instructs the printer to emit a locale-specific monthOfYear text. The
     * parser will accept a long or short monthOfYear text, case-insensitive.
     *
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendMonthOfYearShortText() {
        return appendShortText(iChronoUTC.monthOfYear());
    }

    /**
     * Instructs the printer to emit a locale-specific era text (BC/AD), and
     * the parser to expect it. The parser is case-insensitive.
     *
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendEraText() {
        return appendText(iChronoUTC.era());
    }

    /**
     * Instructs the printer to emit a locale-specific time zone name. A
     * parser cannot be created from this builder if a time zone name is
     * appended.
     *
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendTimeZoneName() {
        return append0(new TimeZonePrinter(iChrono, iLocale, false), null);
    }

    /**
     * Instructs the printer to emit a short locale-specific time zone
     * name. A parser cannot be created from this builder if time zone
     * name is appended.
     *
     * @return this DateTimeFormatterBuilder
     */
    public DateTimeFormatterBuilder appendTimeZoneShortName() {
        return append0(new TimeZonePrinter(iChrono, iLocale, true), null);
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
            final String zeroOffsetText, final boolean showSeparators,
            final int minFields, final int maxFields) {
        return append0(new TimeZoneOffsetFormatter
                       (iChrono, zeroOffsetText, showSeparators, minFields, maxFields));
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
                f = new Composite(iChrono, iElementPairs);
            }

            iFormatter = f;
        }

        return f;
    }

    private boolean isPrinter(final Object f) {
        if (f instanceof DateTimePrinter) {
            if (f instanceof Composite) {
                return ((Composite)f).isPrinter();
            }
            return true;
        }
        return false;
    }

    private boolean isParser(final Object f) {
        if (f instanceof DateTimeParser) {
            if (f instanceof Composite) {
                return ((Composite)f).isParser();
            }
            return true;
        }
        return false;
    }

    private boolean isFormatter(final Object f) {
        if (f instanceof DateTimeFormatter) {
            if (f instanceof Composite) {
                return ((Composite)f).isPrinter()
                    && ((Composite)f).isParser();
            }
            return true;
        }
        return false;
    }

    private static abstract class AbstractFormatter extends AbstractDateTimeFormatter {
        protected final Chronology iChrono;

        AbstractFormatter(Chronology chrono) {
            iChrono = chrono;
        }

        public Chronology getChronology() {
            return iChrono;
        }
    }

    private static class CharacterLiteral extends AbstractFormatter
        implements DateTimeFormatter
    {
        private final char iValue;

        CharacterLiteral(Chronology chrono, char value) {
            super(chrono);
            iValue = value;
        }

        public int estimatePrintedLength() {
            return 1;
        }

        public void printTo(StringBuffer buf, long instant,
                            DateTimeZone zone, long instantLocal) {
            buf.append(iValue);
        }

        public void printTo(Writer out, long instant,
                            DateTimeZone zone, long instantLocal) throws IOException {
            out.write(iValue);
        }

        public String print(long instant, DateTimeZone zone, long instantLocal) {
            return String.valueOf(iValue);
        }

        public int estimateParsedLength() {
            return 1;
        }

        public int parseInto(DateTimeParserBucket bucket, String text, int position) {
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

    private static class StringLiteral extends AbstractFormatter
        implements DateTimeFormatter
    {
        private final String iValue;

        StringLiteral(Chronology chrono, String value) {
            super(chrono);
            iValue = value;
        }

        public int estimatePrintedLength() {
            return iValue.length();
        }

        public void printTo(StringBuffer buf, long instant,
                            DateTimeZone zone, long instantLocal) {
            buf.append(iValue);
        }

        public void printTo(Writer out, long instant,
                            DateTimeZone zone, long instantLocal) throws IOException {
            out.write(iValue);
        }

        public String print(long instant, DateTimeZone zone, long instantLocal) {
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

    private abstract static class NumberFormatter extends AbstractFormatter
        implements DateTimeFormatter
    {
        protected final DateTimeField iField;
        protected final int iMaxParsedDigits;
        protected final boolean iSigned;

        NumberFormatter(
                Chronology chrono, DateTimeField field,
                int maxParsedDigits, boolean signed) {
            super(chrono);
            iField = field;
            iMaxParsedDigits = maxParsedDigits;
            iSigned = signed;
        }

        public int estimateParsedLength() {
            return iMaxParsedDigits;
        }

        public int parseInto(DateTimeParserBucket bucket, String text, int position) {
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
            if (length == 3 && negative) {
                value = -FormatUtils.parseTwoDigits(text, position + 1);
            } else if (length == 2) {
                if (negative) {
                    value = text.charAt(position + 1) - '0';
                    value = -value;
                } else {
                    value = FormatUtils.parseTwoDigits(text, position);
                }
            } else if (length == 1 && !negative) {
                value = text.charAt(position) - '0';
            } else {
                String sub = text.substring(position, position + length);
                try {
                    value = Integer.parseInt(sub);
                } catch (NumberFormatException e) {
                    return ~position;
                }
            }

            bucket.saveField(iField, value);

            return position + length;
        }
    }

    private static class UnpaddedNumber extends NumberFormatter {
        UnpaddedNumber(Chronology chrono, DateTimeField field,
                       int maxParsedDigits, boolean signed)
        {
            super(chrono, field, maxParsedDigits, signed);
        }

        public int estimatePrintedLength() {
            return iMaxParsedDigits;
        }

        public void printTo(StringBuffer buf, long instant,
                            DateTimeZone zone, long instantLocal) {
            try {
                FormatUtils.appendUnpaddedInteger(buf, iField.get(instantLocal));
            } catch (RuntimeException e) {
                buf.append('\ufffd');
            }
        }

        public void printTo(Writer out, long instant,
                            DateTimeZone zone, long instantLocal) throws IOException {
            try {
                FormatUtils.writeUnpaddedInteger(out, iField.get(instantLocal));
            } catch (RuntimeException e) {
                out.write('\ufffd');
            }
        }
    }

    private static class PaddedNumber extends NumberFormatter {
        private final int iMinPrintedDigits;

        PaddedNumber(Chronology chrono,
                     DateTimeField field, int maxParsedDigits,
                     boolean signed, int minPrintedDigits)
        {
            super(chrono, field, maxParsedDigits, signed);
            iMinPrintedDigits = minPrintedDigits;
        }

        public int estimatePrintedLength() {
            return iMaxParsedDigits;
        }

        public void printTo(StringBuffer buf, long instant,
                            DateTimeZone zone, long instantLocal) {
            try {
                FormatUtils.appendPaddedInteger
                    (buf, iField.get(instantLocal), iMinPrintedDigits);
            } catch (RuntimeException e) {
                for (int i=iMinPrintedDigits; --i>=0; ) {
                    buf.append('\ufffd');
                }
            }
        }

        public void printTo(Writer out, long instant,
                            DateTimeZone zone, long instantLocal) throws IOException {
            try {
                FormatUtils.writePaddedInteger
                    (out, iField.get(instantLocal), iMinPrintedDigits);
            } catch (RuntimeException e) {
                for (int i=iMinPrintedDigits; --i>=0; ) {
                    out.write('\ufffd');
                }
            }
        }
    }

    private static class TextField extends AbstractFormatter
        implements DateTimeFormatter
    {
        private final DateTimeField iField;
        private final Locale iLocale;
        private final boolean iShort;

        TextField(Chronology chrono, DateTimeField field,
                  Locale locale, boolean isShort) {
            super(chrono);
            iField = field;
            iLocale = locale;
            iShort = isShort;
        }

        public int estimatePrintedLength() {
            try {
                if (iShort) {
                    return iField.getMaximumShortTextLength(iLocale);
                } else {
                    return iField.getMaximumTextLength(iLocale);
                }
            } catch (RuntimeException e) {
                return 1;
            }
        }

        public void printTo(StringBuffer buf, long instant,
                            DateTimeZone zone, long instantLocal) {
            try {
                buf.append(print(instant, zone, instantLocal));
            } catch (RuntimeException e) {
                buf.append('\ufffd');
            }
        }
    
        public void printTo(Writer out, long instant,
                            DateTimeZone zone, long instantLocal) throws IOException {
            try {
                out.write(print(instant, zone, instantLocal));
            } catch (RuntimeException e) {
                out.write('\ufffd');
            }
        }

        public final String print(long instant, DateTimeZone zone, long instantLocal) {
            if (iShort) {
                return iField.getAsShortText(instantLocal, iLocale);
            } else {
                return iField.getAsText(instantLocal, iLocale);
            }
        }

        public int estimateParsedLength() {
            return estimatePrintedLength();
        }

        public int parseInto(DateTimeParserBucket bucket, String text, int position) {
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

            bucket.saveField(iField, text.substring(position, i), iLocale);

            return i;
        }
    }

    private static class Fraction extends AbstractFormatter
        implements DateTimeFormatter
    {
        private final DateTimeField iField;
        private final long iRangeMillis;
        private final int iMinDigits;
        private final int iMaxDigits;

        private final long iScalar;

        private transient DateTimeField iParseField;

        Fraction(Chronology chrono, DateTimeField field,
                 int minDigits, int maxDigits) {
            super(chrono);
            iField = field;
            iRangeMillis = field.getDurationField().getUnitMillis();

            // Limit the precision requirements.
            if (maxDigits > 18) {
                maxDigits = 18;
            }

            iMinDigits = minDigits;

            long scalar;
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
                if (((iRangeMillis * scalar) / scalar) == iRangeMillis) {
                    break;
                }
                // Overflowed: scale down.
                maxDigits--;
            }

            iMaxDigits = maxDigits;
            iScalar = scalar;
        }

        public int estimatePrintedLength() {
            return iMaxDigits;
        }

        public void printTo(StringBuffer buf, long instant,
                            DateTimeZone zone, long instantLocal) {
            try {
                printTo(buf, null, instantLocal);
            } catch (IOException e) {
                // Not gonna happen.
            }
        }

        public void printTo(Writer out, long instant,
                            DateTimeZone zone, long instantLocal) throws IOException {
            printTo(null, out, instantLocal);
        }

        private void printTo(StringBuffer buf, Writer out, long instantLocal)
            throws IOException
        {
            int minDigits = iMinDigits;

            long fraction;
            try {
                fraction = iField.remainder(instantLocal);
            } catch (RuntimeException e) {
                if (buf != null) {
                    while (--minDigits >= 0) {
                        buf.append('\ufffd');
                    }
                } else {
                    while (--minDigits >= 0) {
                        out.write('\ufffd');
                    }
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
            if ((scaled & 0x7fffffff) == scaled) {
                str = Integer.toString((int)scaled);
            } else {
                str = Long.toString(scaled);
            }

            int length = str.length();
            int digits = iMaxDigits;

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

        public int estimateParsedLength() {
            return iMaxDigits;
        }

        public int parseInto(DateTimeParserBucket bucket, String text, int position) {
            int limit = Math.min(iMaxDigits, text.length() - position);

            long value = 0;
            long n = iRangeMillis * 10;
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

            if (iParseField == null) {
                iParseField = new PreciseDateTimeField
                    ("", MillisDurationField.INSTANCE, iField.getDurationField());
            }

            bucket.saveField(iParseField, (int)value);

            return position + length;
        }
    }

    private static class TimeZoneOffsetFormatter extends AbstractFormatter
        implements DateTimeFormatter
    {
        private final String iZeroOffsetText;
        private final boolean iShowSeparators;
        private final int iMinFields;
        private final int iMaxFields;

        TimeZoneOffsetFormatter(Chronology chrono,
                                String zeroOffsetText,
                                boolean showSeparators,
                                int minFields, int maxFields)
        {
            super(chrono);
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
            
        public int estimatePrintedLength() {
            int est = 1 + iMinFields << 1;
            if (iShowSeparators) {
                est += iMinFields - 1;
            }
            if (iZeroOffsetText != null && iZeroOffsetText.length() > est) {
                est = iZeroOffsetText.length();
            }
            return est;
        }
        
        public void printTo(StringBuffer buf, long instant,
                            DateTimeZone zone, long instantLocal) {
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
        
        public void printTo(Writer out, long instant,
                            DateTimeZone zone, long instantLocal) throws IOException {
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

        public int estimateParsedLength() {
            return estimatePrintedLength();
        }

        public int parseInto(DateTimeParserBucket bucket, String text, int position) {
            if (iZeroOffsetText != null) {
                if (text.regionMatches(true, position, iZeroOffsetText, 0,
                                       iZeroOffsetText.length())) {
                    bucket.setOffset(0);
                    return position + iZeroOffsetText.length();
                }
            }

            // Format to expect is sign character followed by at least one digit.

            int limit = text.length() - position;
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
                    if (text.charAt(position) != '.') {
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

    private static class TimeZonePrinter extends AbstractFormatter
        implements DateTimePrinter 
    {
        private final Locale iLocale;
        private final boolean iShortFormat;

        TimeZonePrinter(Chronology chrono, Locale locale, boolean shortFormat) {
            super(chrono);
            iLocale = locale;
            iShortFormat = shortFormat;
        }

        public int estimatePrintedLength() {
            return iShortFormat ? 4 : 20;
        }
        
        public void printTo(StringBuffer buf, long instant,
                            DateTimeZone zone, long instantLocal) {
            if (zone == null) {
                zone = getDateTimeZone();
            }
            if (iShortFormat) {
                buf.append(zone.getShortName(instant, this.iLocale));
            } else {
                buf.append(zone.getName(instant, this.iLocale));
            }
        }
        
        public void printTo(Writer out, long instant,
                            DateTimeZone zone, long instantLocal) throws IOException {
            if (zone == null) {
                zone = getDateTimeZone();
            }
            if (iShortFormat) {
                out.write(zone.getShortName(instant, this.iLocale));
            } else {
                out.write(zone.getName(instant, this.iLocale));
            }
        }

        public String print(long instant, DateTimeZone zone, long instantLocal) {
            if (zone == null) {
                zone = getDateTimeZone();
            }
            if (iShortFormat) {
                return zone.getShortName(instant, this.iLocale);
            } else {
                return zone.getName(instant, this.iLocale);
            }
        }
    }

    private static final class Composite extends AbstractFormatter
        implements DateTimeFormatter
    {
        private final DateTimePrinter[] iPrinters;
        private final DateTimeParser[] iParsers;

        private final int iPrintedLengthEstimate;
        private final int iParsedLengthEstimate;

        Composite(Chronology chrono, ArrayList elementPairs) {
            super(chrono);

            int len = elementPairs.size() / 2;

            boolean isPrinter = true;
            boolean isParser = true;

            int printEst = 0;
            int parseEst = 0;

            DateTimePrinter[] printers = new DateTimePrinter[len];
            DateTimeParser[] parsers = new DateTimeParser[len];
            for (int i=0; i<len; i++) {
                Object element = elementPairs.get(i * 2);
                if (element == null || !(element instanceof DateTimePrinter)) {
                    isPrinter = false;
                } else {
                    DateTimePrinter printer = (DateTimePrinter)element;
                    printEst += printer.estimatePrintedLength();
                    printers[i] = printer;
                }

                element = elementPairs.get(i * 2 + 1);
                if (element == null || !(element instanceof DateTimeParser)) {
                    isParser = false;
                } else {
                    DateTimeParser parser = (DateTimeParser)element;
                    parseEst += parser.estimateParsedLength();
                    parsers[i] = parser;
                }
            }

            if (!isPrinter) {
                printers = null;
            }
            if (!isParser) {
                parsers = null;
            }

            iPrinters = printers;
            iParsers = parsers;
            iPrintedLengthEstimate = printEst;
            iParsedLengthEstimate = parseEst;
        }

        public int estimatePrintedLength() {
            return iPrintedLengthEstimate;
        }
    
        public void printTo(StringBuffer buf, long instant,
                            DateTimeZone zone, long instantLocal) {
            DateTimePrinter[] elements = iPrinters;

            if (elements == null) {
                throw new UnsupportedOperationException();
            }

            int len = elements.length;
            for (int i=0; i<len; i++) {
                elements[i].printTo(buf, instant, zone, instantLocal);
            }
        }

        public void printTo(Writer out, long instant,
                            DateTimeZone zone, long instantLocal) throws IOException {
            DateTimePrinter[] elements = iPrinters;

            if (elements == null) {
                throw new UnsupportedOperationException();
            }

            int len = elements.length;
            for (int i=0; i<len; i++) {
                elements[i].printTo(out, instant, zone, instantLocal);
            }
        }

        public int estimateParsedLength() {
            return iParsedLengthEstimate;
        }

        public int parseInto(DateTimeParserBucket bucket, String text, int position) {
            DateTimeParser[] elements = iParsers;

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
    }

    private static final class MatchingParser extends AbstractFormatter
        implements DateTimeParser
    {
        private final DateTimeParser[] iParsers;
        private final int iParsedLengthEstimate;

        MatchingParser(Chronology chrono, DateTimeParser[] parsers) {
            super(chrono);
            iParsers = parsers;
            int est = 0;
            for (int i=parsers.length; --i>=0 ;) {
                DateTimeParser parser = parsers[i];
                if (parser != null) {
                    int len = parser.estimateParsedLength();
                    if (len > est) {
                        len = est;
                    }
                }
            }
            iParsedLengthEstimate = est;
        }

        public int estimateParsedLength() {
            return iParsedLengthEstimate;
        }

        public int parseInto(DateTimeParserBucket bucket, String text, int position) {
            DateTimeParser[] parsers = iParsers;
            int length = parsers.length;

            Object state = bucket.saveState();
            
            int bestInvalidPos = position;
            int bestInvalidParser = 0;
            int bestValidPos = position;
            int bestValidParser = 0;

            for (int i=0; i<length; i++) {
                if (i != 0) {
                    bucket.undoChanges(state);
                }

                DateTimeParser parser = parsers[i];
                if (parser == null) {
                    // The empty parser wins only if nothing is better.
                    if (bestValidPos > position) {
                        break;
                    }
                    return position;
                }

                int parsePos = parser.parseInto(bucket, text, position);
                if (parsePos >= position) {
                    if (parsePos >= text.length()) {
                        return parsePos;
                    }
                    if (parsePos > bestValidPos) {
                        bestValidPos = parsePos;
                        bestValidParser = i;
                    }
                } else {
                    parsePos = ~parsePos;
                    if (parsePos > bestInvalidPos) {
                        bestInvalidPos = parsePos;
                        bestInvalidParser = i;
                    }
                }
            }

            if (bestValidPos > position) {
                if (bestValidParser == length - 1) {
                    // The best valid parser was the last one, so the bucket is
                    // already in the best state.
                    return bestValidPos;
                }
                bucket.undoChanges(state);
                // Call best valid parser again to restore bucket state.
                return parsers[bestValidParser].parseInto(bucket, text, position);
            }

            if (bestInvalidParser == length - 1) {
                // The best invalid parser was the last one, so the bucket is
                // already in the best state.
                return ~bestInvalidPos;
            }

            bucket.undoChanges(state);
            // Call best invalid parser again to restore bucket state.
            return parsers[bestInvalidParser].parseInto(bucket, text, position);
        }
    }
}
