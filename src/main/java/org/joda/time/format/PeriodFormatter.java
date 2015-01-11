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
import java.util.Locale;

import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.ReadablePeriod;

/**
 * Controls the printing and parsing of a time period to and from a string.
 * <p>
 * This class is the main API for printing and parsing used by most applications.
 * Instances of this class are created via one of three factory classes:
 * <ul>
 * <li>{@link PeriodFormat} - formats by pattern and style</li>
 * <li>{@link ISOPeriodFormat} - ISO8601 formats</li>
 * <li>{@link PeriodFormatterBuilder} - complex formats created via method calls</li>
 * </ul>
 * <p>
 * An instance of this class holds a reference internally to one printer and
 * one parser. It is possible that one of these may be null, in which case the
 * formatter cannot print/parse. This can be checked via the {@link #isPrinter()}
 * and {@link #isParser()} methods.
 * <p>
 * The underlying printer/parser can be altered to behave exactly as required
 * by using a decorator modifier:
 * <ul>
 * <li>{@link #withLocale(Locale)} - returns a new formatter that uses the specified locale</li>
 * </ul>
 * This returns a new formatter (instances of this class are immutable).
 * <p>
 * The main methods of the class are the <code>printXxx</code> and
 * <code>parseXxx</code> methods. These are used as follows:
 * <pre>
 * // print using the default locale
 * String periodStr = formatter.print(period);
 * // print using the French locale
 * String periodStr = formatter.withLocale(Locale.FRENCH).print(period);
 * 
 * // parse using the French locale
 * Period date = formatter.withLocale(Locale.FRENCH).parsePeriod(str);
 * </pre>
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public class PeriodFormatter {

    /** The internal printer used to output the datetime. */
    private final PeriodPrinter iPrinter;
    /** The internal parser used to output the datetime. */
    private final PeriodParser iParser;
    /** The locale to use for printing and parsing. */
    private final Locale iLocale;
    /** The period type used in parsing. */
    private final PeriodType iParseType;

    /**
     * Creates a new formatter, however you will normally use the factory
     * or the builder.
     * 
     * @param printer  the internal printer, null if cannot print
     * @param parser  the internal parser, null if cannot parse
     */
    public PeriodFormatter(
            PeriodPrinter printer, PeriodParser parser) {
        super();
        iPrinter = printer;
        iParser = parser;
        iLocale = null;
        iParseType = null;
    }

    /**
     * Constructor.
     * 
     * @param printer  the internal printer, null if cannot print
     * @param parser  the internal parser, null if cannot parse
     * @param locale  the locale to use
     * @param type  the parse period type
     */
    PeriodFormatter(
            PeriodPrinter printer, PeriodParser parser,
            Locale locale, PeriodType type) {
        super();
        iPrinter = printer;
        iParser = parser;
        iLocale = locale;
        iParseType = type;
    }

    //-----------------------------------------------------------------------
    /**
     * Is this formatter capable of printing.
     * 
     * @return true if this is a printer
     */
    public boolean isPrinter() {
        return (iPrinter != null);
    }

    /**
     * Gets the internal printer object that performs the real printing work.
     * 
     * @return the internal printer
     */
    public PeriodPrinter getPrinter() {
        return iPrinter;
    }

    /**
     * Is this formatter capable of parsing.
     * 
     * @return true if this is a parser
     */
    public boolean isParser() {
        return (iParser != null);
    }

    /**
     * Gets the internal parser object that performs the real parsing work.
     * 
     * @return the internal parser
     */
    public PeriodParser getParser() {
        return iParser;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new formatter with a different locale that will be used
     * for printing and parsing.
     * <p>
     * A PeriodFormatter is immutable, so a new instance is returned,
     * and the original is unaltered and still usable.
     * <p>
     * A null locale indicates that no specific locale override is in use.
     * 
     * @param locale  the locale to use
     * @return the new formatter
     */
    public PeriodFormatter withLocale(Locale locale) {
        if (locale == getLocale() || (locale != null && locale.equals(getLocale()))) {
            return this;
        }
        return new PeriodFormatter(iPrinter, iParser, locale, iParseType);
    }

    /**
     * Gets the locale that will be used for printing and parsing.
     * <p>
     * A null locale indicates that no specific locale override is in use.
     * 
     * @return the locale to use
     */
    public Locale getLocale() {
        return iLocale;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new formatter with a different PeriodType for parsing.
     * <p>
     * A PeriodFormatter is immutable, so a new instance is returned,
     * and the original is unaltered and still usable.
     * 
     * @param type  the type to use in parsing
     * @return the new formatter
     */
    public PeriodFormatter withParseType(PeriodType type) {
        if (type == iParseType) {
            return this;
        }
        return new PeriodFormatter(iPrinter, iParser, iLocale, type);
    }

    /**
     * Gets the PeriodType that will be used for parsing.
     * 
     * @return the parse type to use
     */
    public PeriodType getParseType() {
        return iParseType;
    }

    //-----------------------------------------------------------------------
    /**
     * Prints a ReadablePeriod to a StringBuffer.
     *
     * @param buf  the formatted period is appended to this buffer
     * @param period  the period to format, not null
     */
    public void printTo(StringBuffer buf, ReadablePeriod period) {
        checkPrinter();
        checkPeriod(period);
        
        getPrinter().printTo(buf, period, iLocale);
    }

    /**
     * Prints a ReadablePeriod to a Writer.
     *
     * @param out  the formatted period is written out
     * @param period  the period to format, not null
     */
    public void printTo(Writer out, ReadablePeriod period) throws IOException {
        checkPrinter();
        checkPeriod(period);
        
        getPrinter().printTo(out, period, iLocale);
    }

    /**
     * Prints a ReadablePeriod to a new String.
     *
     * @param period  the period to format, not null
     * @return the printed result
     */
    public String print(ReadablePeriod period) {
        checkPrinter();
        checkPeriod(period);
        
        PeriodPrinter printer = getPrinter();
        StringBuffer buf = new StringBuffer(printer.calculatePrintedLength(period, iLocale));
        printer.printTo(buf, period, iLocale);
        return buf.toString();
    }

    /**
     * Checks whether printing is supported.
     * 
     * @throws UnsupportedOperationException if printing is not supported
     */
    private void checkPrinter() {
        if (iPrinter == null) {
            throw new UnsupportedOperationException("Printing not supported");
        }
    }

    /**
     * Checks whether the period is non-null.
     * 
     * @throws IllegalArgumentException if the period is null
     */
    private void checkPeriod(ReadablePeriod period) {
        if (period == null) {
            throw new IllegalArgumentException("Period must not be null");
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Parses a period from the given text, at the given position, saving the
     * result into the fields of the given ReadWritablePeriod. If the parse
     * succeeds, the return value is the new text position. Note that the parse
     * may succeed without fully reading the text.
     * <p>
     * The parse type of the formatter is not used by this method.
     * <p>
     * If it fails, the return value is negative, but the period may still be
     * modified. To determine the position where the parse failed, apply the
     * one's complement operator (~) on the return value.
     *
     * @param period  a period that will be modified
     * @param text  text to parse
     * @param position position to start parsing from
     * @return new position, if negative, parse failed. Apply complement
     * operator (~) to get position of failure
     * @throws IllegalArgumentException if any field is out of range
     */
    public int parseInto(ReadWritablePeriod period, String text, int position) {
        checkParser();
        checkPeriod(period);
        
        return getParser().parseInto(period, text, position, iLocale);
    }

    /**
     * Parses a period from the given text, returning a new Period.
     *
     * @param text  text to parse
     * @return parsed value in a Period object
     * @throws IllegalArgumentException if any field is out of range
     */
    public Period parsePeriod(String text) {
        checkParser();
        
        return parseMutablePeriod(text).toPeriod();
    }

    /**
     * Parses a period from the given text, returning a new MutablePeriod.
     *
     * @param text  text to parse
     * @return parsed value in a MutablePeriod object
     * @throws IllegalArgumentException if any field is out of range
     */
    public MutablePeriod parseMutablePeriod(String text) {
        checkParser();
        
        MutablePeriod period = new MutablePeriod(0, iParseType);
        int newPos = getParser().parseInto(period, text, 0, iLocale);
        if (newPos >= 0) {
            if (newPos >= text.length()) {
                return period;
            }
        } else {
            newPos = ~newPos;
        }
        throw new IllegalArgumentException(FormatUtils.createErrorMessage(text, newPos));
    }

    /**
     * Checks whether parsing is supported.
     * 
     * @throws UnsupportedOperationException if parsing is not supported
     */
    private void checkParser() {
        if (iParser == null) {
            throw new UnsupportedOperationException("Parsing not supported");
        }
    }

}
