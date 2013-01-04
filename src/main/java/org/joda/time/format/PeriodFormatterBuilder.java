/*
 *  Copyright 2001-2009 Stephen Colebourne
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.ReadablePeriod;

/**
 * Factory that creates complex instances of PeriodFormatter via method calls.
 * <p>
 * Period formatting is performed by the {@link PeriodFormatter} class.
 * Three classes provide factory methods to create formatters, and this is one.
 * The others are {@link PeriodFormat} and {@link ISOPeriodFormat}.
 * <p>
 * PeriodFormatterBuilder is used for constructing formatters which are then
 * used to print or parse. The formatters are built by appending specific fields
 * or other formatters to an instance of this builder.
 * <p>
 * For example, a formatter that prints years and months, like "15 years and 8 months",
 * can be constructed as follows:
 * <p>
 * <pre>
 * PeriodFormatter yearsAndMonths = new PeriodFormatterBuilder()
 *     .printZeroAlways()
 *     .appendYears()
 *     .appendSuffix(" year", " years")
 *     .appendSeparator(" and ")
 *     .printZeroRarelyLast()
 *     .appendMonths()
 *     .appendSuffix(" month", " months")
 *     .toFormatter();
 * </pre>
 * <p>
 * PeriodFormatterBuilder itself is mutable and not thread-safe, but the
 * formatters that it builds are thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @since 1.0
 * @see PeriodFormat
 */
public class PeriodFormatterBuilder {
    private static final int PRINT_ZERO_RARELY_FIRST = 1;
    private static final int PRINT_ZERO_RARELY_LAST = 2;
    private static final int PRINT_ZERO_IF_SUPPORTED = 3;
    private static final int PRINT_ZERO_ALWAYS = 4;
    private static final int PRINT_ZERO_NEVER = 5;
    
    private static final int YEARS = 0;
    private static final int MONTHS = 1;
    private static final int WEEKS = 2;
    private static final int DAYS = 3;
    private static final int HOURS = 4;
    private static final int MINUTES = 5;
    private static final int SECONDS = 6;
    private static final int MILLIS = 7;
    private static final int SECONDS_MILLIS = 8;
    private static final int SECONDS_OPTIONAL_MILLIS = 9;
    private static final int MAX_FIELD = SECONDS_OPTIONAL_MILLIS;

    private int iMinPrintedDigits;
    private int iPrintZeroSetting;
    private int iMaxParsedDigits;
    private boolean iRejectSignedValues;

    private PeriodFieldAffix iPrefix;

    // List of Printers and Parsers used to build a final formatter.
    private List<Object> iElementPairs;
    /** Set to true if the formatter is not a printer. */
    private boolean iNotPrinter;
    /** Set to true if the formatter is not a parser. */
    private boolean iNotParser;

    // Last PeriodFormatter appended of each field type.
    private FieldFormatter[] iFieldFormatters;

    public PeriodFormatterBuilder() {
        clear();
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs a PeriodFormatter using all the appended elements.
     * <p>
     * This is the main method used by applications at the end of the build
     * process to create a usable formatter.
     * <p>
     * Subsequent changes to this builder do not affect the returned formatter.
     * <p>
     * The returned formatter may not support both printing and parsing.
     * The methods {@link PeriodFormatter#isPrinter()} and
     * {@link PeriodFormatter#isParser()} will help you determine the state
     * of the formatter.
     * 
     * @return the newly created formatter
     * @throws IllegalStateException if the builder can produce neither a printer nor a parser
     */
    public PeriodFormatter toFormatter() {
        PeriodFormatter formatter = toFormatter(iElementPairs, iNotPrinter, iNotParser);
        iFieldFormatters = (FieldFormatter[]) iFieldFormatters.clone();
        return formatter;
    }

    /**
     * Internal method to create a PeriodPrinter instance using all the
     * appended elements.
     * <p>
     * Most applications will not use this method.
     * If you want a printer in an application, call {@link #toFormatter()}
     * and just use the printing API.
     * <p>
     * Subsequent changes to this builder do not affect the returned printer.
     * 
     * @return the newly created printer, null if builder cannot create a printer
     */
    public PeriodPrinter toPrinter() {
        if (iNotPrinter) {
            return null;
        }
        return toFormatter().getPrinter();
    }

    /**
     * Internal method to create a PeriodParser instance using all the
     * appended elements.
     * <p>
     * Most applications will not use this method.
     * If you want a printer in an application, call {@link #toFormatter()}
     * and just use the printing API.
     * <p>
     * Subsequent changes to this builder do not affect the returned parser.
     * 
     * @return the newly created parser, null if builder cannot create a parser
     */
    public PeriodParser toParser() {
        if (iNotParser) {
            return null;
        }
        return toFormatter().getParser();
    }

    //-----------------------------------------------------------------------
    /**
     * Clears out all the appended elements, allowing this builder to be reused.
     */
    public void clear() {
        iMinPrintedDigits = 1;
        iPrintZeroSetting = PRINT_ZERO_RARELY_LAST;
        iMaxParsedDigits = 10;
        iRejectSignedValues = false;
        iPrefix = null;
        if (iElementPairs == null) {
            iElementPairs = new ArrayList<Object>();
        } else {
            iElementPairs.clear();
        }
        iNotPrinter = false;
        iNotParser = false;
        iFieldFormatters = new FieldFormatter[10];
    }

    /**
     * Appends another formatter.
     *
     * @return this PeriodFormatterBuilder
     */
    public PeriodFormatterBuilder append(PeriodFormatter formatter) {
        if (formatter == null) {
            throw new IllegalArgumentException("No formatter supplied");
        }
        clearPrefix();
        append0(formatter.getPrinter(), formatter.getParser());
        return this;
    }

    /**
     * Appends a printer parser pair.
     * <p>
     * Either the printer or the parser may be null, in which case the builder will
     * be unable to produce a parser or printer repectively.
     *
     * @param printer  appends a printer to the builder, null if printing is not supported
     * @param parser  appends a parser to the builder, null if parsing is not supported
     * @return this PeriodFormatterBuilder
     * @throws IllegalArgumentException if both the printer and parser are null
     */
    public PeriodFormatterBuilder append(PeriodPrinter printer, PeriodParser parser) {
        if (printer == null && parser == null) {
            throw new IllegalArgumentException("No printer or parser supplied");
        }
        clearPrefix();
        append0(printer, parser);
        return this;
    }

    /**
     * Instructs the printer to emit specific text, and the parser to expect it.
     * The parser is case-insensitive.
     *
     * @return this PeriodFormatterBuilder
     * @throws IllegalArgumentException if text is null
     */
    public PeriodFormatterBuilder appendLiteral(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Literal must not be null");
        }
        clearPrefix();
        Literal literal = new Literal(text);
        append0(literal, literal);
        return this;
    }

    /**
     * Set the minimum digits printed for the next and following appended
     * fields. By default, the minimum digits printed is one. If the field value
     * is zero, it is not printed unless a printZero rule is applied.
     *
     * @return this PeriodFormatterBuilder
     */
    public PeriodFormatterBuilder minimumPrintedDigits(int minDigits) {
        iMinPrintedDigits = minDigits;
        return this;
    }

    /**
     * Set the maximum digits parsed for the next and following appended
     * fields. By default, the maximum digits parsed is ten.
     *
     * @return this PeriodFormatterBuilder
     */
    public PeriodFormatterBuilder maximumParsedDigits(int maxDigits) {
        iMaxParsedDigits = maxDigits;
        return this;
    }

    /**
     * Reject signed values when parsing the next and following appended fields.
     *
     * @return this PeriodFormatterBuilder
     */
    public PeriodFormatterBuilder rejectSignedValues(boolean v) {
        iRejectSignedValues = v;
        return this;
    }

    /**
     * Never print zero values for the next and following appended fields,
     * unless no fields would be printed. If no fields are printed, the printer
     * forces the last "printZeroRarely" field to print a zero.
     * <p>
     * This field setting is the default.
     *
     * @return this PeriodFormatterBuilder
     */
    public PeriodFormatterBuilder printZeroRarelyLast() {
        iPrintZeroSetting = PRINT_ZERO_RARELY_LAST;
        return this;
    }

    /**
     * Never print zero values for the next and following appended fields,
     * unless no fields would be printed. If no fields are printed, the printer
     * forces the first "printZeroRarely" field to print a zero.
     *
     * @return this PeriodFormatterBuilder
     */
    public PeriodFormatterBuilder printZeroRarelyFirst() {
        iPrintZeroSetting = PRINT_ZERO_RARELY_FIRST;
        return this;
    }

    /**
     * Print zero values for the next and following appened fields only if the
     * period supports it.
     *
     * @return this PeriodFormatterBuilder
     */
    public PeriodFormatterBuilder printZeroIfSupported() {
        iPrintZeroSetting = PRINT_ZERO_IF_SUPPORTED;
        return this;
    }

    /**
     * Always print zero values for the next and following appended fields,
     * even if the period doesn't support it. The parser requires values for
     * fields that always print zero.
     *
     * @return this PeriodFormatterBuilder
     */
    public PeriodFormatterBuilder printZeroAlways() {
        iPrintZeroSetting = PRINT_ZERO_ALWAYS;
        return this;
    }

    /**
     * Never print zero values for the next and following appended fields,
     * unless no fields would be printed. If no fields are printed, the printer
     * forces the last "printZeroRarely" field to print a zero.
     * <p>
     * This field setting is the default.
     *
     * @return this PeriodFormatterBuilder
     */
    public PeriodFormatterBuilder printZeroNever() {
        iPrintZeroSetting = PRINT_ZERO_NEVER;
        return this;
    }

    //-----------------------------------------------------------------------
    /**
     * Append a field prefix which applies only to the next appended field. If
     * the field is not printed, neither is the prefix.
     *
     * @param text text to print before field only if field is printed
     * @return this PeriodFormatterBuilder
     * @see #appendSuffix
     */
    public PeriodFormatterBuilder appendPrefix(String text) {
        if (text == null) {
            throw new IllegalArgumentException();
        }
        return appendPrefix(new SimpleAffix(text));
    }

    /**
     * Append a field prefix which applies only to the next appended field. If
     * the field is not printed, neither is the prefix.
     * <p>
     * During parsing, the singular and plural versions are accepted whether
     * or not the actual value matches plurality.
     *
     * @param singularText text to print if field value is one
     * @param pluralText text to print if field value is not one
     * @return this PeriodFormatterBuilder
     * @see #appendSuffix
     */
    public PeriodFormatterBuilder appendPrefix(String singularText,
                                                 String pluralText) {
        if (singularText == null || pluralText == null) {
            throw new IllegalArgumentException();
        }
        return appendPrefix(new PluralAffix(singularText, pluralText));
    }

    /**
     * Append a field prefix which applies only to the next appended field. If
     * the field is not printed, neither is the prefix.
     *
     * @param prefix custom prefix
     * @return this PeriodFormatterBuilder
     * @see #appendSuffix
     */
    private PeriodFormatterBuilder appendPrefix(PeriodFieldAffix prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        if (iPrefix != null) {
            prefix = new CompositeAffix(iPrefix, prefix);
        }
        iPrefix = prefix;
        return this;
    }

    //-----------------------------------------------------------------------
    /**
     * Instruct the printer to emit an integer years field, if supported.
     * <p>
     * The number of printed and parsed digits can be controlled using
     * {@link #minimumPrintedDigits(int)} and {@link #maximumParsedDigits(int)}.
     *
     * @return this PeriodFormatterBuilder
     */
    public PeriodFormatterBuilder appendYears() {
        appendField(YEARS);
        return this;
    }

    /**
     * Instruct the printer to emit an integer months field, if supported.
     * <p>
     * The number of printed and parsed digits can be controlled using
     * {@link #minimumPrintedDigits(int)} and {@link #maximumParsedDigits(int)}.
     *
     * @return this PeriodFormatterBuilder
     */
    public PeriodFormatterBuilder appendMonths() {
        appendField(MONTHS);
        return this;
    }

    /**
     * Instruct the printer to emit an integer weeks field, if supported.
     * <p>
     * The number of printed and parsed digits can be controlled using
     * {@link #minimumPrintedDigits(int)} and {@link #maximumParsedDigits(int)}.
     *
     * @return this PeriodFormatterBuilder
     */
    public PeriodFormatterBuilder appendWeeks() {
        appendField(WEEKS);
        return this;
    }

    /**
     * Instruct the printer to emit an integer days field, if supported.
     * <p>
     * The number of printed and parsed digits can be controlled using
     * {@link #minimumPrintedDigits(int)} and {@link #maximumParsedDigits(int)}.
     *
     * @return this PeriodFormatterBuilder
     */
    public PeriodFormatterBuilder appendDays() {
        appendField(DAYS);
        return this;
    }

    /**
     * Instruct the printer to emit an integer hours field, if supported.
     * <p>
     * The number of printed and parsed digits can be controlled using
     * {@link #minimumPrintedDigits(int)} and {@link #maximumParsedDigits(int)}.
     *
     * @return this PeriodFormatterBuilder
     */
    public PeriodFormatterBuilder appendHours() {
        appendField(HOURS);
        return this;
    }

    /**
     * Instruct the printer to emit an integer minutes field, if supported.
     * <p>
     * The number of printed and parsed digits can be controlled using
     * {@link #minimumPrintedDigits(int)} and {@link #maximumParsedDigits(int)}.
     *
     * @return this PeriodFormatterBuilder
     */
    public PeriodFormatterBuilder appendMinutes() {
        appendField(MINUTES);
        return this;
    }

    /**
     * Instruct the printer to emit an integer seconds field, if supported.
     * <p>
     * The number of printed and parsed digits can be controlled using
     * {@link #minimumPrintedDigits(int)} and {@link #maximumParsedDigits(int)}.
     *
     * @return this PeriodFormatterBuilder
     */
    public PeriodFormatterBuilder appendSeconds() {
        appendField(SECONDS);
        return this;
    }

    /**
     * Instruct the printer to emit a combined seconds and millis field, if supported.
     * The millis will overflow into the seconds if necessary.
     * The millis are always output.
     *
     * @return this PeriodFormatterBuilder
     */
    public PeriodFormatterBuilder appendSecondsWithMillis() {
        appendField(SECONDS_MILLIS);
        return this;
    }

    /**
     * Instruct the printer to emit a combined seconds and millis field, if supported.
     * The millis will overflow into the seconds if necessary.
     * The millis are only output if non-zero.
     *
     * @return this PeriodFormatterBuilder
     */
    public PeriodFormatterBuilder appendSecondsWithOptionalMillis() {
        appendField(SECONDS_OPTIONAL_MILLIS);
        return this;
    }

    /**
     * Instruct the printer to emit an integer millis field, if supported.
     * <p>
     * The number of printed and parsed digits can be controlled using
     * {@link #minimumPrintedDigits(int)} and {@link #maximumParsedDigits(int)}.
     *
     * @return this PeriodFormatterBuilder
     */
    public PeriodFormatterBuilder appendMillis() {
        appendField(MILLIS);
        return this;
    }

    /**
     * Instruct the printer to emit an integer millis field, if supported.
     * <p>
     * The number of arsed digits can be controlled using {@link #maximumParsedDigits(int)}.
     *
     * @return this PeriodFormatterBuilder
     */
    public PeriodFormatterBuilder appendMillis3Digit() {
        appendField(7, 3);
        return this;
    }

    private void appendField(int type) {
        appendField(type, iMinPrintedDigits);
    }

    private void appendField(int type, int minPrinted) {
        FieldFormatter field = new FieldFormatter(minPrinted, iPrintZeroSetting,
            iMaxParsedDigits, iRejectSignedValues, type, iFieldFormatters, iPrefix, null);
        append0(field, field);
        iFieldFormatters[type] = field;
        iPrefix = null;
    }

    //-----------------------------------------------------------------------
    /**
     * Append a field suffix which applies only to the last appended field. If
     * the field is not printed, neither is the suffix.
     *
     * @param text text to print after field only if field is printed
     * @return this PeriodFormatterBuilder
     * @throws IllegalStateException if no field exists to append to
     * @see #appendPrefix
     */
    public PeriodFormatterBuilder appendSuffix(String text) {
        if (text == null) {
            throw new IllegalArgumentException();
        }
        return appendSuffix(new SimpleAffix(text));
    }

    /**
     * Append a field suffix which applies only to the last appended field. If
     * the field is not printed, neither is the suffix.
     * <p>
     * During parsing, the singular and plural versions are accepted whether or
     * not the actual value matches plurality.
     *
     * @param singularText text to print if field value is one
     * @param pluralText text to print if field value is not one
     * @return this PeriodFormatterBuilder
     * @throws IllegalStateException if no field exists to append to
     * @see #appendPrefix
     */
    public PeriodFormatterBuilder appendSuffix(String singularText,
                                               String pluralText) {
        if (singularText == null || pluralText == null) {
            throw new IllegalArgumentException();
        }
        return appendSuffix(new PluralAffix(singularText, pluralText));
    }

    /**
     * Append a field suffix which applies only to the last appended field. If
     * the field is not printed, neither is the suffix.
     *
     * @param suffix custom suffix
     * @return this PeriodFormatterBuilder
     * @throws IllegalStateException if no field exists to append to
     * @see #appendPrefix
     */
    private PeriodFormatterBuilder appendSuffix(PeriodFieldAffix suffix) {
        final Object originalPrinter;
        final Object originalParser;
        if (iElementPairs.size() > 0) {
            originalPrinter = iElementPairs.get(iElementPairs.size() - 2);
            originalParser = iElementPairs.get(iElementPairs.size() - 1);
        } else {
            originalPrinter = null;
            originalParser = null;
        }

        if (originalPrinter == null || originalParser == null ||
                originalPrinter != originalParser ||
                !(originalPrinter instanceof FieldFormatter)) {
            throw new IllegalStateException("No field to apply suffix to");
        }

        clearPrefix();
        FieldFormatter newField = new FieldFormatter((FieldFormatter) originalPrinter, suffix);
        iElementPairs.set(iElementPairs.size() - 2, newField);
        iElementPairs.set(iElementPairs.size() - 1, newField);
        iFieldFormatters[newField.getFieldType()] = newField;
        
        return this;
    }

    //-----------------------------------------------------------------------
    /**
     * Append a separator, which is output if fields are printed both before
     * and after the separator.
     * <p>
     * For example, <code>builder.appendDays().appendSeparator(",").appendHours()</code>
     * will only output the comma if both the days and hours fields are output.
     * <p>
     * The text will be parsed case-insensitively.
     * <p>
     * Note: appending a separator discontinues any further work on the latest
     * appended field.
     *
     * @param text  the text to use as a separator
     * @return this PeriodFormatterBuilder
     * @throws IllegalStateException if this separator follows a previous one
     */
    public PeriodFormatterBuilder appendSeparator(String text) {
        return appendSeparator(text, text, null, true, true);
    }

    /**
     * Append a separator, which is output only if fields are printed after the separator.
     * <p>
     * For example,
     * <code>builder.appendDays().appendSeparatorIfFieldsAfter(",").appendHours()</code>
     * will only output the comma if the hours fields is output.
     * <p>
     * The text will be parsed case-insensitively.
     * <p>
     * Note: appending a separator discontinues any further work on the latest
     * appended field.
     *
     * @param text  the text to use as a separator
     * @return this PeriodFormatterBuilder
     * @throws IllegalStateException if this separator follows a previous one
     */
    public PeriodFormatterBuilder appendSeparatorIfFieldsAfter(String text) {
        return appendSeparator(text, text, null, false, true);
    }

    /**
     * Append a separator, which is output only if fields are printed before the separator.
     * <p>
     * For example,
     * <code>builder.appendDays().appendSeparatorIfFieldsBefore(",").appendHours()</code>
     * will only output the comma if the days fields is output.
     * <p>
     * The text will be parsed case-insensitively.
     * <p>
     * Note: appending a separator discontinues any further work on the latest
     * appended field.
     *
     * @param text  the text to use as a separator
     * @return this PeriodFormatterBuilder
     * @throws IllegalStateException if this separator follows a previous one
     */
    public PeriodFormatterBuilder appendSeparatorIfFieldsBefore(String text) {
        return appendSeparator(text, text, null, true, false);
    }

    /**
     * Append a separator, which is output if fields are printed both before
     * and after the separator.
     * <p>
     * This method changes the separator depending on whether it is the last separator
     * to be output.
     * <p>
     * For example, <code>builder.appendDays().appendSeparator(",", "&").appendHours().appendSeparator(",", "&").appendMinutes()</code>
     * will output '1,2&3' if all three fields are output, '1&2' if two fields are output
     * and '1' if just one field is output.
     * <p>
     * The text will be parsed case-insensitively.
     * <p>
     * Note: appending a separator discontinues any further work on the latest
     * appended field.
     *
     * @param text  the text to use as a separator
     * @param finalText  the text used used if this is the final separator to be printed
     * @return this PeriodFormatterBuilder
     * @throws IllegalStateException if this separator follows a previous one
     */
    public PeriodFormatterBuilder appendSeparator(String text, String finalText) {
        return appendSeparator(text, finalText, null, true, true);
    }

    /**
     * Append a separator, which is output if fields are printed both before
     * and after the separator.
     * <p>
     * This method changes the separator depending on whether it is the last separator
     * to be output.
     * <p>
     * For example, <code>builder.appendDays().appendSeparator(",", "&").appendHours().appendSeparator(",", "&").appendMinutes()</code>
     * will output '1,2&3' if all three fields are output, '1&2' if two fields are output
     * and '1' if just one field is output.
     * <p>
     * The text will be parsed case-insensitively.
     * <p>
     * Note: appending a separator discontinues any further work on the latest
     * appended field.
     *
     * @param text  the text to use as a separator
     * @param finalText  the text used used if this is the final separator to be printed
     * @param variants  set of text values which are also acceptable when parsed
     * @return this PeriodFormatterBuilder
     * @throws IllegalStateException if this separator follows a previous one
     */
    public PeriodFormatterBuilder appendSeparator(String text, String finalText,
                                                  String[] variants) {
        return appendSeparator(text, finalText, variants, true, true);
    }

    private PeriodFormatterBuilder appendSeparator(String text, String finalText,
                                                   String[] variants,
                                                   boolean useBefore, boolean useAfter) {
        if (text == null || finalText == null) {
            throw new IllegalArgumentException();
        }

        clearPrefix();
        
        // optimise zero formatter case
        List<Object> pairs = iElementPairs;
        if (pairs.size() == 0) {
            if (useAfter && useBefore == false) {
                Separator separator = new Separator(
                        text, finalText, variants,
                        Literal.EMPTY, Literal.EMPTY, useBefore, useAfter);
                append0(separator, separator);
            }
            return this;
        }
        
        // find the last separator added
        int i;
        Separator lastSeparator = null;
        for (i=pairs.size(); --i>=0; ) {
            if (pairs.get(i) instanceof Separator) {
                lastSeparator = (Separator) pairs.get(i);
                pairs = pairs.subList(i + 1, pairs.size());
                break;
            }
            i--;  // element pairs
        }
        
        // merge formatters
        if (lastSeparator != null && pairs.size() == 0) {
            throw new IllegalStateException("Cannot have two adjacent separators");
        } else {
            Object[] comp = createComposite(pairs);
            pairs.clear();
            Separator separator = new Separator(
                    text, finalText, variants,
                    (PeriodPrinter) comp[0], (PeriodParser) comp[1],
                    useBefore, useAfter);
            pairs.add(separator);
            pairs.add(separator);
        }
        
        return this;
    }

    //-----------------------------------------------------------------------
    private void clearPrefix() throws IllegalStateException {
        if (iPrefix != null) {
            throw new IllegalStateException("Prefix not followed by field");
        }
        iPrefix = null;
    }

    private PeriodFormatterBuilder append0(PeriodPrinter printer, PeriodParser parser) {
        iElementPairs.add(printer);
        iElementPairs.add(parser);
        iNotPrinter |= (printer == null);
        iNotParser |= (parser == null);
        return this;
    }

    //-----------------------------------------------------------------------
    private static PeriodFormatter toFormatter(List<Object> elementPairs, boolean notPrinter, boolean notParser) {
        if (notPrinter && notParser) {
            throw new IllegalStateException("Builder has created neither a printer nor a parser");
        }
        int size = elementPairs.size();
        if (size >= 2 && elementPairs.get(0) instanceof Separator) {
            Separator sep = (Separator) elementPairs.get(0);
            if (sep.iAfterParser == null && sep.iAfterPrinter == null) {
                PeriodFormatter f = toFormatter(elementPairs.subList(2, size), notPrinter, notParser);
                sep = sep.finish(f.getPrinter(), f.getParser());
                return new PeriodFormatter(sep, sep);
            }
        }
        Object[] comp = createComposite(elementPairs);
        if (notPrinter) {
            return new PeriodFormatter(null, (PeriodParser) comp[1]);
        } else if (notParser) {
            return new PeriodFormatter((PeriodPrinter) comp[0], null);
        } else {
            return new PeriodFormatter((PeriodPrinter) comp[0], (PeriodParser) comp[1]);
        }
    }

    private static Object[] createComposite(List<Object> elementPairs) {
        switch (elementPairs.size()) {
            case 0:
                return new Object[] {Literal.EMPTY, Literal.EMPTY};
            case 1:
                return new Object[] {elementPairs.get(0), elementPairs.get(1)};
            default:
                Composite comp = new Composite(elementPairs);
                return new Object[] {comp, comp};
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Defines a formatted field's prefix or suffix text.
     * This can be used for fields such as 'n hours' or 'nH' or 'Hour:n'.
     */
    static interface PeriodFieldAffix {
        int calculatePrintedLength(int value);
        
        void printTo(StringBuffer buf, int value);
        
        void printTo(Writer out, int value) throws IOException;
        
        /**
         * @return new position after parsing affix, or ~position of failure
         */
        int parse(String periodStr, int position);

        /**
         * @return position where affix starts, or original ~position if not found
         */
        int scan(String periodStr, int position);
    }

    //-----------------------------------------------------------------------
    /**
     * Implements an affix where the text does not vary by the amount.
     */
    static class SimpleAffix implements PeriodFieldAffix {
        private final String iText;

        SimpleAffix(String text) {
            iText = text;
        }

        public int calculatePrintedLength(int value) {
            return iText.length();
        }

        public void printTo(StringBuffer buf, int value) {
            buf.append(iText);
        }

        public void printTo(Writer out, int value) throws IOException {
            out.write(iText);
        }

        public int parse(String periodStr, int position) {
            String text = iText;
            int textLength = text.length();
            if (periodStr.regionMatches(true, position, text, 0, textLength)) {
                return position + textLength;
            }
            return ~position;
        }

        public int scan(String periodStr, final int position) {
            String text = iText;
            int textLength = text.length();
            int sourceLength = periodStr.length();
            search:
            for (int pos = position; pos < sourceLength; pos++) {
                if (periodStr.regionMatches(true, pos, text, 0, textLength)) {
                    return pos;
                }
                // Only allow number characters to be skipped in search of suffix.
                switch (periodStr.charAt(pos)) {
                case '0': case '1': case '2': case '3': case '4':
                case '5': case '6': case '7': case '8': case '9':
                case '.': case ',': case '+': case '-':
                    break;
                default:
                    break search;
                }
            }
            return ~position;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Implements an affix where the text varies by the amount of the field.
     * Only singular (1) and plural (not 1) are supported.
     */
    static class PluralAffix implements PeriodFieldAffix {
        private final String iSingularText;
        private final String iPluralText;

        PluralAffix(String singularText, String pluralText) {
            iSingularText = singularText;
            iPluralText = pluralText;
        }

        public int calculatePrintedLength(int value) {
            return (value == 1 ? iSingularText : iPluralText).length();
        }

        public void printTo(StringBuffer buf, int value) {
            buf.append(value == 1 ? iSingularText : iPluralText);
        }

        public void printTo(Writer out, int value) throws IOException {
            out.write(value == 1 ? iSingularText : iPluralText);
        }

        public int parse(String periodStr, int position) {
            String text1 = iPluralText;
            String text2 = iSingularText; 

            if (text1.length() < text2.length()) {
                // Swap in order to match longer one first.
                String temp = text1;
                text1 = text2;
                text2 = temp;
            }

            if (periodStr.regionMatches
                (true, position, text1, 0, text1.length())) {
                return position + text1.length();
            }
            if (periodStr.regionMatches
                (true, position, text2, 0, text2.length())) {
                return position + text2.length();
            }

            return ~position;
        }

        public int scan(String periodStr, final int position) {
            String text1 = iPluralText;
            String text2 = iSingularText; 

            if (text1.length() < text2.length()) {
                // Swap in order to match longer one first.
                String temp = text1;
                text1 = text2;
                text2 = temp;
            }

            int textLength1 = text1.length();
            int textLength2 = text2.length();

            int sourceLength = periodStr.length();
            for (int pos = position; pos < sourceLength; pos++) {
                if (periodStr.regionMatches(true, pos, text1, 0, textLength1)) {
                    return pos;
                }
                if (periodStr.regionMatches(true, pos, text2, 0, textLength2)) {
                    return pos;
                }
            }
            return ~position;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Builds a composite affix by merging two other affix implementations.
     */
    static class CompositeAffix implements PeriodFieldAffix {
        private final PeriodFieldAffix iLeft;
        private final PeriodFieldAffix iRight;

        CompositeAffix(PeriodFieldAffix left, PeriodFieldAffix right) {
            iLeft = left;
            iRight = right;
        }

        public int calculatePrintedLength(int value) {
            return iLeft.calculatePrintedLength(value)
                + iRight.calculatePrintedLength(value);
        }

        public void printTo(StringBuffer buf, int value) {
            iLeft.printTo(buf, value);
            iRight.printTo(buf, value);
        }

        public void printTo(Writer out, int value) throws IOException {
            iLeft.printTo(out, value);
            iRight.printTo(out, value);
        }

        public int parse(String periodStr, int position) {
            position = iLeft.parse(periodStr, position);
            if (position >= 0) {
                position = iRight.parse(periodStr, position);
            }
            return position;
        }

        public int scan(String periodStr, final int position) {
            int pos = iLeft.scan(periodStr, position);
            if (pos >= 0) {
                return iRight.scan(periodStr, pos);
            }
            return ~position;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Formats the numeric value of a field, potentially with prefix/suffix.
     */
    static class FieldFormatter
            implements PeriodPrinter, PeriodParser {
        private final int iMinPrintedDigits;
        private final int iPrintZeroSetting;
        private final int iMaxParsedDigits;
        private final boolean iRejectSignedValues;
        
        /** The index of the field type, 0=year, etc. */
        private final int iFieldType;
        /**
         * The array of the latest formatter added for each type.
         * This is shared between all the field formatters in a formatter.
         */
        private final FieldFormatter[] iFieldFormatters;
        
        private final PeriodFieldAffix iPrefix;
        private final PeriodFieldAffix iSuffix;

        FieldFormatter(int minPrintedDigits, int printZeroSetting,
                       int maxParsedDigits, boolean rejectSignedValues,
                       int fieldType, FieldFormatter[] fieldFormatters,
                       PeriodFieldAffix prefix, PeriodFieldAffix suffix) {
            iMinPrintedDigits = minPrintedDigits;
            iPrintZeroSetting = printZeroSetting;
            iMaxParsedDigits = maxParsedDigits;
            iRejectSignedValues = rejectSignedValues;
            iFieldType = fieldType;
            iFieldFormatters = fieldFormatters;
            iPrefix = prefix;
            iSuffix = suffix;
        }

        FieldFormatter(FieldFormatter field, PeriodFieldAffix suffix) {
            iMinPrintedDigits = field.iMinPrintedDigits;
            iPrintZeroSetting = field.iPrintZeroSetting;
            iMaxParsedDigits = field.iMaxParsedDigits;
            iRejectSignedValues = field.iRejectSignedValues;
            iFieldType = field.iFieldType;
            iFieldFormatters = field.iFieldFormatters;
            iPrefix = field.iPrefix;
            if (field.iSuffix != null) {
                suffix = new CompositeAffix(field.iSuffix, suffix);
            }
            iSuffix = suffix;
        }

        public int countFieldsToPrint(ReadablePeriod period, int stopAt, Locale locale) {
            if (stopAt <= 0) {
                return 0;
            }
            if (iPrintZeroSetting == PRINT_ZERO_ALWAYS || getFieldValue(period) != Long.MAX_VALUE) {
                return 1;
            }
            return 0;
        }

        public int calculatePrintedLength(ReadablePeriod period, Locale locale) {
            long valueLong = getFieldValue(period);
            if (valueLong == Long.MAX_VALUE) {
                return 0;
            }

            int sum = Math.max(FormatUtils.calculateDigitCount(valueLong), iMinPrintedDigits);
            if (iFieldType >= SECONDS_MILLIS) {
                // valueLong contains the seconds and millis fields
                // the minimum output is 0.000, which is 4 or 5 digits with a negative
                sum = (valueLong < 0 ? Math.max(sum, 5) : Math.max(sum, 4));
                // plus one for the decimal point
                sum++;
                if (iFieldType == SECONDS_OPTIONAL_MILLIS &&
                        (Math.abs(valueLong) % DateTimeConstants.MILLIS_PER_SECOND) == 0) {
                    sum -= 4; // remove three digits and decimal point
                }
                // reset valueLong to refer to the seconds part for the prefic/suffix calculation
                valueLong = valueLong / DateTimeConstants.MILLIS_PER_SECOND;
            }
            int value = (int) valueLong;

            if (iPrefix != null) {
                sum += iPrefix.calculatePrintedLength(value);
            }
            if (iSuffix != null) {
                sum += iSuffix.calculatePrintedLength(value);
            }

            return sum;
        }
        
        public void printTo(StringBuffer buf, ReadablePeriod period, Locale locale) {
            long valueLong = getFieldValue(period);
            if (valueLong == Long.MAX_VALUE) {
                return;
            }
            int value = (int) valueLong;
            if (iFieldType >= SECONDS_MILLIS) {
                value = (int) (valueLong / DateTimeConstants.MILLIS_PER_SECOND);
            }

            if (iPrefix != null) {
                iPrefix.printTo(buf, value);
            }
            int bufLen = buf.length();
            int minDigits = iMinPrintedDigits;
            if (minDigits <= 1) {
                FormatUtils.appendUnpaddedInteger(buf, value);
            } else {
                FormatUtils.appendPaddedInteger(buf, value, minDigits);
            }
            if (iFieldType >= SECONDS_MILLIS) {
                int dp = (int) (Math.abs(valueLong) % DateTimeConstants.MILLIS_PER_SECOND);
                if (iFieldType == SECONDS_MILLIS || dp > 0) {
                    if (valueLong < 0 && valueLong > -DateTimeConstants.MILLIS_PER_SECOND) {
                        buf.insert(bufLen, '-');
                    }
                    buf.append('.');
                    FormatUtils.appendPaddedInteger(buf, dp, 3);
                }
            }
            if (iSuffix != null) {
                iSuffix.printTo(buf, value);
            }
        }

        public void printTo(Writer out, ReadablePeriod period, Locale locale) throws IOException {
            long valueLong = getFieldValue(period);
            if (valueLong == Long.MAX_VALUE) {
                return;
            }
            int value = (int) valueLong;
            if (iFieldType >= SECONDS_MILLIS) {
                value = (int) (valueLong / DateTimeConstants.MILLIS_PER_SECOND);
            }

            if (iPrefix != null) {
                iPrefix.printTo(out, value);
            }
            int minDigits = iMinPrintedDigits;
            if (minDigits <= 1) {
                FormatUtils.writeUnpaddedInteger(out, value);
            } else {
                FormatUtils.writePaddedInteger(out, value, minDigits);
            }
            if (iFieldType >= SECONDS_MILLIS) {
                int dp = (int) (Math.abs(valueLong) % DateTimeConstants.MILLIS_PER_SECOND);
                if (iFieldType == SECONDS_MILLIS || dp > 0) {
                    out.write('.');
                    FormatUtils.writePaddedInteger(out, dp, 3);
                }
            }
            if (iSuffix != null) {
                iSuffix.printTo(out, value);
            }
        }

        public int parseInto(
                ReadWritablePeriod period, String text, 
                int position, Locale locale) {

            boolean mustParse = (iPrintZeroSetting == PRINT_ZERO_ALWAYS);

            // Shortcut test.
            if (position >= text.length()) {
                return mustParse ? ~position : position;
            }

            if (iPrefix != null) {
                position = iPrefix.parse(text, position);
                if (position >= 0) {
                    // If prefix is found, then the parse must finish.
                    mustParse = true;
                } else {
                    // Prefix not found, so bail.
                    if (!mustParse) {
                        // It's okay because parsing of this field is not
                        // required. Don't return an error. Fields down the
                        // chain can continue on, trying to parse.
                        return ~position;
                    }
                    return position;
                }
            }

            int suffixPos = -1;
            if (iSuffix != null && !mustParse) {
                // Pre-scan the suffix, to help determine if this field must be
                // parsed.
                suffixPos = iSuffix.scan(text, position);
                if (suffixPos >= 0) {
                    // If suffix is found, then parse must finish.
                    mustParse = true;
                } else {
                    // Suffix not found, so bail.
                    if (!mustParse) {
                        // It's okay because parsing of this field is not
                        // required. Don't return an error. Fields down the
                        // chain can continue on, trying to parse.
                        return ~suffixPos;
                    }
                    return suffixPos;
                }
            }

            if (!mustParse && !isSupported(period.getPeriodType(), iFieldType)) {
                // If parsing is not required and the field is not supported,
                // exit gracefully so that another parser can continue on.
                return position;
            }

            int limit;
            if (suffixPos > 0) {
                limit = Math.min(iMaxParsedDigits, suffixPos - position);
            } else {
                limit = Math.min(iMaxParsedDigits, text.length() - position);
            }

            // validate input number
            int length = 0;
            int fractPos = -1;
            boolean hasDigits = false;
            while (length < limit) {
                char c = text.charAt(position + length);
                // leading sign
                if (length == 0 && (c == '-' || c == '+') && !iRejectSignedValues) {
                    boolean negative = c == '-';

                    // Next character must be a digit.
                    if (length + 1 >= limit || 
                        (c = text.charAt(position + length + 1)) < '0' || c > '9')
                    {
                        break;
                    }

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
                // main number
                if (c >= '0' && c <= '9') {
                    hasDigits = true;
                } else {
                    if ((c == '.' || c == ',')
                         && (iFieldType == SECONDS_MILLIS || iFieldType == SECONDS_OPTIONAL_MILLIS)) {
                        if (fractPos >= 0) {
                            // can't have two decimals
                            break;
                        }
                        fractPos = position + length + 1;
                        // Expand the limit to disregard the decimal point.
                        limit = Math.min(limit + 1, text.length() - position);
                    } else {
                        break;
                    }
                }
                length++;
            }

            if (!hasDigits) {
                return ~position;
            }

            if (suffixPos >= 0 && position + length != suffixPos) {
                // If there are additional non-digit characters before the
                // suffix is reached, then assume that the suffix found belongs
                // to a field not yet reached. Return original position so that
                // another parser can continue on.
                return position;
            }

            if (iFieldType != SECONDS_MILLIS && iFieldType != SECONDS_OPTIONAL_MILLIS) {
                // Handle common case.
                setFieldValue(period, iFieldType, parseInt(text, position, length));
            } else if (fractPos < 0) {
                setFieldValue(period, SECONDS, parseInt(text, position, length));
                setFieldValue(period, MILLIS, 0);
            } else {
                int wholeValue = parseInt(text, position, fractPos - position - 1);
                setFieldValue(period, SECONDS, wholeValue);

                int fractLen = position + length - fractPos;
                int fractValue;
                if (fractLen <= 0) {
                    fractValue = 0;
                } else {
                    if (fractLen >= 3) {
                        fractValue = parseInt(text, fractPos, 3);
                    } else {
                        fractValue = parseInt(text, fractPos, fractLen);
                        if (fractLen == 1) {
                            fractValue *= 100;
                        } else {
                            fractValue *= 10;
                        }
                    }
                    if (wholeValue < 0) {
                        fractValue = -fractValue;
                    }
                }

                setFieldValue(period, MILLIS, fractValue);
            }
                
            position += length;

            if (position >= 0 && iSuffix != null) {
                position = iSuffix.parse(text, position);
            }
                
            return position;
        }

        /**
         * @param text text to parse
         * @param position position in text
         * @param length exact count of characters to parse
         * @return parsed int value
         */
        private int parseInt(String text, int position, int length) {
            if (length >= 10) {
                // Since value may exceed max, use stock parser which checks for this.
                return Integer.parseInt(text.substring(position, position + length));
            }
            if (length <= 0) {
                return 0;
            }
            int value = text.charAt(position++);
            length--;
            boolean negative;
            if (value == '-') {
                if (--length < 0) {
                    return 0;
                }
                negative = true;
                value = text.charAt(position++);
            } else {
                negative = false;
            }
            value -= '0';
            while (length-- > 0) {
                value = ((value << 3) + (value << 1)) + text.charAt(position++) - '0';
            }
            return negative ? -value : value;
        }

        /**
         * @return Long.MAX_VALUE if nothing to print, otherwise value
         */
        long getFieldValue(ReadablePeriod period) {
            PeriodType type;
            if (iPrintZeroSetting == PRINT_ZERO_ALWAYS) {
                type = null; // Don't need to check if supported.
            } else {
                type = period.getPeriodType();
            }
            if (type != null && isSupported(type, iFieldType) == false) {
                return Long.MAX_VALUE;
            }

            long value;

            switch (iFieldType) {
            default:
                return Long.MAX_VALUE;
            case YEARS:
                value = period.get(DurationFieldType.years());
                break;
            case MONTHS:
                value = period.get(DurationFieldType.months());
                break;
            case WEEKS:
                value = period.get(DurationFieldType.weeks());
                break;
            case DAYS:
                value = period.get(DurationFieldType.days());
                break;
            case HOURS:
                value = period.get(DurationFieldType.hours());
                break;
            case MINUTES:
                value = period.get(DurationFieldType.minutes());
                break;
            case SECONDS:
                value = period.get(DurationFieldType.seconds());
                break;
            case MILLIS:
                value = period.get(DurationFieldType.millis());
                break;
            case SECONDS_MILLIS: // drop through
            case SECONDS_OPTIONAL_MILLIS:
                int seconds = period.get(DurationFieldType.seconds());
                int millis = period.get(DurationFieldType.millis());
                value = (seconds * (long) DateTimeConstants.MILLIS_PER_SECOND) + millis;
                break;
            }

            // determine if period is zero and this is the last field
            if (value == 0) {
                switch (iPrintZeroSetting) {
                case PRINT_ZERO_NEVER:
                    return Long.MAX_VALUE;
                case PRINT_ZERO_RARELY_LAST:
                    if (isZero(period) && iFieldFormatters[iFieldType] == this) {
                        for (int i = iFieldType + 1; i <= MAX_FIELD; i++) {
                            if (isSupported(type, i) && iFieldFormatters[i] != null) {
                                return Long.MAX_VALUE;
                            }
                        }
                    } else {
                        return Long.MAX_VALUE;
                    }
                    break;
                case PRINT_ZERO_RARELY_FIRST:
                    if (isZero(period) && iFieldFormatters[iFieldType] == this) {
                        int i = Math.min(iFieldType, 8);  // line split out for IBM JDK
                        i--;                              // see bug 1660490
                        for (; i >= 0 && i <= MAX_FIELD; i--) {
                            if (isSupported(type, i) && iFieldFormatters[i] != null) {
                                return Long.MAX_VALUE;
                            }
                        }
                    } else {
                        return Long.MAX_VALUE;
                    }
                    break;
                }
            }

            return value;
        }

        boolean isZero(ReadablePeriod period) {
            for (int i = 0, isize = period.size(); i < isize; i++) {
                if (period.getValue(i) != 0) {
                    return false;
                }
            }
            return true;
        }

        boolean isSupported(PeriodType type, int field) {
            switch (field) {
            default:
                return false;
            case YEARS:
                return type.isSupported(DurationFieldType.years());
            case MONTHS:
                return type.isSupported(DurationFieldType.months());
            case WEEKS:
                return type.isSupported(DurationFieldType.weeks());
            case DAYS:
                return type.isSupported(DurationFieldType.days());
            case HOURS:
                return type.isSupported(DurationFieldType.hours());
            case MINUTES:
                return type.isSupported(DurationFieldType.minutes());
            case SECONDS:
                return type.isSupported(DurationFieldType.seconds());
            case MILLIS:
                return type.isSupported(DurationFieldType.millis());
            case SECONDS_MILLIS: // drop through
            case SECONDS_OPTIONAL_MILLIS:
                return type.isSupported(DurationFieldType.seconds()) ||
                       type.isSupported(DurationFieldType.millis());
            }
        }

        void setFieldValue(ReadWritablePeriod period, int field, int value) {
            switch (field) {
            default:
                break;
            case YEARS:
                period.setYears(value);
                break;
            case MONTHS:
                period.setMonths(value);
                break;
            case WEEKS:
                period.setWeeks(value);
                break;
            case DAYS:
                period.setDays(value);
                break;
            case HOURS:
                period.setHours(value);
                break;
            case MINUTES:
                period.setMinutes(value);
                break;
            case SECONDS:
                period.setSeconds(value);
                break;
            case MILLIS:
                period.setMillis(value);
                break;
            }
        }

        int getFieldType() {
            return iFieldType;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Handles a simple literal piece of text.
     */
    static class Literal
            implements PeriodPrinter, PeriodParser {
        static final Literal EMPTY = new Literal("");
        private final String iText;

        Literal(String text) {
            iText = text;
        }

        public int countFieldsToPrint(ReadablePeriod period, int stopAt, Locale locale) {
            return 0;
        }

        public int calculatePrintedLength(ReadablePeriod period, Locale locale) {
            return iText.length();
        }

        public void printTo(StringBuffer buf, ReadablePeriod period, Locale locale) {
            buf.append(iText);
        }

        public void printTo(Writer out, ReadablePeriod period, Locale locale) throws IOException {
            out.write(iText);
        }

        public int parseInto(
                ReadWritablePeriod period, String periodStr,
                int position, Locale locale) {
            if (periodStr.regionMatches(true, position, iText, 0, iText.length())) {
                return position + iText.length();
            }
            return ~position;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Handles a separator, that splits the fields into multiple parts.
     * For example, the 'T' in the ISO8601 standard.
     */
    static class Separator
            implements PeriodPrinter, PeriodParser {
        private final String iText;
        private final String iFinalText;
        private final String[] iParsedForms;

        private final boolean iUseBefore;
        private final boolean iUseAfter;

        private final PeriodPrinter iBeforePrinter;
        private volatile PeriodPrinter iAfterPrinter;
        private final PeriodParser iBeforeParser;
        private volatile PeriodParser iAfterParser;

        Separator(String text, String finalText, String[] variants,
                PeriodPrinter beforePrinter, PeriodParser beforeParser,
                boolean useBefore, boolean useAfter) {
            iText = text;
            iFinalText = finalText;

            if ((finalText == null || text.equals(finalText)) &&
                (variants == null || variants.length == 0)) {

                iParsedForms = new String[] {text};
            } else {
                // Filter and reverse sort the parsed forms.
                TreeSet<String> parsedSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
                parsedSet.add(text);
                parsedSet.add(finalText);
                if (variants != null) {
                    for (int i=variants.length; --i>=0; ) {
                        parsedSet.add(variants[i]);
                    }
                }
                ArrayList<String> parsedList = new ArrayList<String>(parsedSet);
                Collections.reverse(parsedList);
                iParsedForms = parsedList.toArray(new String[parsedList.size()]);
            }

            iBeforePrinter = beforePrinter;
            iBeforeParser = beforeParser;
            iUseBefore = useBefore;
            iUseAfter = useAfter;
        }

        public int countFieldsToPrint(ReadablePeriod period, int stopAt, Locale locale) {
            int sum = iBeforePrinter.countFieldsToPrint(period, stopAt, locale);
            if (sum < stopAt) {
                sum += iAfterPrinter.countFieldsToPrint(period, stopAt, locale);
            }
            return sum;
        }

        public int calculatePrintedLength(ReadablePeriod period, Locale locale) {
            PeriodPrinter before = iBeforePrinter;
            PeriodPrinter after = iAfterPrinter;
            
            int sum = before.calculatePrintedLength(period, locale)
                    + after.calculatePrintedLength(period, locale);
            
            if (iUseBefore) {
                if (before.countFieldsToPrint(period, 1, locale) > 0) {
                    if (iUseAfter) {
                        int afterCount = after.countFieldsToPrint(period, 2, locale);
                        if (afterCount > 0) {
                            sum += (afterCount > 1 ? iText : iFinalText).length();
                        }
                    } else {
                        sum += iText.length();
                    }
                }
            } else if (iUseAfter && after.countFieldsToPrint(period, 1, locale) > 0) {
                sum += iText.length();
            }
            
            return sum;
        }

        public void printTo(StringBuffer buf, ReadablePeriod period, Locale locale) {
            PeriodPrinter before = iBeforePrinter;
            PeriodPrinter after = iAfterPrinter;
            
            before.printTo(buf, period, locale);
            if (iUseBefore) {
                if (before.countFieldsToPrint(period, 1, locale) > 0) {
                    if (iUseAfter) {
                        int afterCount = after.countFieldsToPrint(period, 2, locale);
                        if (afterCount > 0) {
                            buf.append(afterCount > 1 ? iText : iFinalText);
                        }
                    } else {
                        buf.append(iText);
                    }
                }
            } else if (iUseAfter && after.countFieldsToPrint(period, 1, locale) > 0) {
                buf.append(iText);
            }
            after.printTo(buf, period, locale);
        }

        public void printTo(Writer out, ReadablePeriod period, Locale locale) throws IOException {
            PeriodPrinter before = iBeforePrinter;
            PeriodPrinter after = iAfterPrinter;
            
            before.printTo(out, period, locale);
            if (iUseBefore) {
                if (before.countFieldsToPrint(period, 1, locale) > 0) {
                    if (iUseAfter) {
                        int afterCount = after.countFieldsToPrint(period, 2, locale);
                        if (afterCount > 0) {
                            out.write(afterCount > 1 ? iText : iFinalText);
                        }
                    } else {
                        out.write(iText);
                    }
                }
            } else if (iUseAfter && after.countFieldsToPrint(period, 1, locale) > 0) {
                out.write(iText);
            }
            after.printTo(out, period, locale);
        }

        public int parseInto(
                ReadWritablePeriod period, String periodStr,
                int position, Locale locale) {
            int oldPos = position;
            position = iBeforeParser.parseInto(period, periodStr, position, locale);

            if (position < 0) {
                return position;
            }

            boolean found = false;
            int parsedFormLength = -1;
            if (position > oldPos) {
                // Consume this separator.
                String[] parsedForms = iParsedForms;
                int length = parsedForms.length;
                for (int i=0; i < length; i++) {
                    String parsedForm = parsedForms[i];
                    if ((parsedForm == null || parsedForm.length() == 0) ||
                        periodStr.regionMatches
                        (true, position, parsedForm, 0, parsedForm.length())) {
                        
                        parsedFormLength = (parsedForm == null ? 0 : parsedForm.length());
                        position += parsedFormLength;
                        found = true;
                        break;
                    }
                }
            }

            oldPos = position;
            position = iAfterParser.parseInto(period, periodStr, position, locale);

            if (position < 0) {
                return position;
            }

            if (found && position == oldPos && parsedFormLength > 0) {
                // Separator should not have been supplied.
                return ~oldPos;
            }

            if (position > oldPos && !found && !iUseBefore) {
                // Separator was required.
                return ~oldPos;
            }

            return position;
        }

        Separator finish(PeriodPrinter afterPrinter, PeriodParser afterParser) {
            iAfterPrinter = afterPrinter;
            iAfterParser = afterParser;
            return this;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Composite implementation that merges other fields to create a full pattern.
     */
    static class Composite
            implements PeriodPrinter, PeriodParser {
        
        private final PeriodPrinter[] iPrinters;
        private final PeriodParser[] iParsers;

        Composite(List<Object> elementPairs) {
            List<Object> printerList = new ArrayList<Object>();
            List<Object> parserList = new ArrayList<Object>();

            decompose(elementPairs, printerList, parserList);

            if (printerList.size() <= 0) {
                iPrinters = null;
            } else {
                iPrinters = printerList.toArray(
                        new PeriodPrinter[printerList.size()]);
            }

            if (parserList.size() <= 0) {
                iParsers = null;
            } else {
                iParsers = parserList.toArray(
                        new PeriodParser[parserList.size()]);
            }
        }

        public int countFieldsToPrint(ReadablePeriod period, int stopAt, Locale locale) {
            int sum = 0;
            PeriodPrinter[] printers = iPrinters;
            for (int i=printers.length; sum < stopAt && --i>=0; ) {
                sum += printers[i].countFieldsToPrint(period, Integer.MAX_VALUE, locale);
            }
            return sum;
        }

        public int calculatePrintedLength(ReadablePeriod period, Locale locale) {
            int sum = 0;
            PeriodPrinter[] printers = iPrinters;
            for (int i=printers.length; --i>=0; ) {
                sum += printers[i].calculatePrintedLength(period, locale);
            }
            return sum;
        }

        public void printTo(StringBuffer buf, ReadablePeriod period, Locale locale) {
            PeriodPrinter[] printers = iPrinters;
            int len = printers.length;
            for (int i=0; i<len; i++) {
                printers[i].printTo(buf, period, locale);
            }
        }

        public void printTo(Writer out, ReadablePeriod period, Locale locale) throws IOException {
            PeriodPrinter[] printers = iPrinters;
            int len = printers.length;
            for (int i=0; i<len; i++) {
                printers[i].printTo(out, period, locale);
            }
        }

        public int parseInto(
                ReadWritablePeriod period, String periodStr,
                int position, Locale locale) {
            PeriodParser[] parsers = iParsers;
            if (parsers == null) {
                throw new UnsupportedOperationException();
            }

            int len = parsers.length;
            for (int i=0; i<len && position >= 0; i++) {
                position = parsers[i].parseInto(period, periodStr, position, locale);
            }
            return position;
        }

        private void decompose(List<Object> elementPairs, List<Object> printerList, List<Object> parserList) {
            int size = elementPairs.size();
            for (int i=0; i<size; i+=2) {
                Object element = elementPairs.get(i);
                if (element instanceof PeriodPrinter) {
                    if (element instanceof Composite) {
                        addArrayToList(printerList, ((Composite) element).iPrinters);
                    } else {
                        printerList.add(element);
                    }
                }

                element = elementPairs.get(i + 1);
                if (element instanceof PeriodParser) {
                    if (element instanceof Composite) {
                        addArrayToList(parserList, ((Composite) element).iParsers);
                    } else {
                        parserList.add(element);
                    }
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

}
