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

import org.joda.time.PeriodType;
import org.joda.time.ReadWritableTimePeriod;
import org.joda.time.ReadableTimePeriod;

/**
 * TimePeriodFormatterBuilder is used for constructing {@link TimePeriodFormatter}s.
 * TimePeriodFormatters are built by appending specific fields and separators.
 *
 * <p>
 * For example, a formatter that prints years and months, like "15 years and 8 months",
 * can be constructed as follows:
 * <p>
 * <pre>
 * TimePeriodFormatter yearsAndMonths = new TimePeriodFormatterBuilder()
 *     .printZeroAlways()
 *     .appendYears()
 *     .appendSuffix(" year", " years")
 *     .appendSeparator(" and ")
 *     .printZeroRarely()
 *     .appendMonths()
 *     .appendSuffix(" month", " months")
 *     .toFormatter();
 * </pre>
 * <p>
 * TimePeriodFormatterBuilder itself is mutable and not thread-safe, but the
 * formatters that it builds are thread-safe and immutable.
 *
 * @see TimePeriodFormat
 * @author Brian S O'Neill
 */
public class TimePeriodFormatterBuilder {
    private static final int PRINT_ZERO_RARELY = 1;
    private static final int PRINT_ZERO_IF_SUPPORTED = 2;
    private static final int PRINT_ZERO_ALWAYS = 3;

    private boolean iFavorFirstFieldForZero;

    private int iMinPrintedDigits;
    private int iPrintZeroSetting;
    private int iMaxParsedDigits;
    private boolean iRejectSignedValues;

    private DurationFieldAffix iPrefix;

    // List of TimePeriodFormatters used to build a final formatter.
    private List iFormatters;

    // List of TimePeriodFormatters used to build an alternate formatter. The
    // alternate is chosen if no other fields are printed.
    private List iAlternateFormatters;

    public TimePeriodFormatterBuilder() {
        clear();
    }

    /**
     * Converts to a TimePeriodPrinter that prints using all the appended
     * elements. Subsequent changes to this builder do not affect the returned
     * printer.
     */
    public TimePeriodPrinter toPrinter() {
        return toFormatter();
    }

    /**
     * Converts to a TimePeriodParser that parses using all the appended
     * elements. Subsequent changes to this builder do not affect the returned
     * parser.
     */
    public TimePeriodParser toParser() {
        return toFormatter();
    }

    /**
     * Converts to a TimePeriodFormatter that formats using all the appended
     * elements. Subsequent changes to this builder do not affect the returned
     * formatter.
     */
    public TimePeriodFormatter toFormatter() {
        TimePeriodFormatter formatter = toFormatter(iFormatters);
        List altFormatters = iAlternateFormatters;
        if (altFormatters.size() > 0) {
            // Alternate is needed only if field formatters were
            // appended. Literals may have been appended as well.
            for (int i=altFormatters.size(); --i>=0; ) {
                if (altFormatters.get(i) instanceof FieldFormatter) {
                    formatter = new AlternateSelector
                        (formatter, altFormatters, iFavorFirstFieldForZero);
                    break;
                }
            }
        }
        return formatter;
    }

    private static TimePeriodFormatter toFormatter(List formatters) {
        int size = formatters.size();
        if (size >= 2 && formatters.get(1) instanceof Separator) {
            TimePeriodFormatter before = (TimePeriodFormatter) formatters.get(0);
            if (size == 2) {
                // Separator at the end would never format anything.
                return before;
            }
            return ((Separator) formatters.get(1)).finish
                (before, toFormatter(formatters.subList(2, size)));
        }
        return createComposite(formatters);
    }

    /**
     * Clears out all the appended elements, allowing this builder to be
     * reused.
     */
    public void clear() {
        iFavorFirstFieldForZero = false;
        iMinPrintedDigits = 1;
        iPrintZeroSetting = PRINT_ZERO_RARELY;
        iMaxParsedDigits = 10;
        iRejectSignedValues = false;
        iPrefix = null;
        if (iFormatters == null) {
            iFormatters = new ArrayList();
        } else {
            iFormatters.clear();
        }
        if (iAlternateFormatters == null) {
            iAlternateFormatters = new ArrayList();
        } else {
            iAlternateFormatters.clear();
        }
    }

    /**
     * Appends another formatter.
     *
     * @return this TimePeriodFormatterBuilder
     */
    public TimePeriodFormatterBuilder append(TimePeriodFormatter formatter)
        throws IllegalArgumentException
    {
        if (formatter == null) {
            throw new IllegalArgumentException("No formatter supplied");
        }
        clearPrefix();
        iFormatters.add(formatter);
        return this;
    }

    /**
     * Instructs the printer to emit specific text, and the parser to expect
     * it. The parser is case-insensitive.
     *
     * @return this TimePeriodFormatterBuilder
     * @throws IllegalArgumentException if text is null
     */
    public TimePeriodFormatterBuilder appendLiteral(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Literal must not be null");
        }
        clearPrefix();
        Literal literal = new Literal(text);
        iFormatters.add(literal);
        iAlternateFormatters.add(literal);
        return this;
    }

    /**
     * Set the minimum digits printed for the next and following appended
     * fields. By default, the minimum digits printed is one. If the field value
     * is zero, it is not printed unless a printZero rule is applied.
     *
     * @return this TimePeriodFormatterBuilder
     */
    public TimePeriodFormatterBuilder minimumPrintedDigits(int minDigits) {
        iMinPrintedDigits = minDigits;
        return this;
    }

    /**
     * Set the maximum digits parsed for the next and following appended
     * fields. By default, the maximum digits parsed is ten.
     *
     * @return this TimePeriodFormatterBuilder
     */
    public TimePeriodFormatterBuilder maximumParsedDigits(int maxDigits) {
        iMaxParsedDigits = maxDigits;
        return this;
    }

    /**
     * Reject signed values when parsing the next and following appended fields.
     *
     * @return this TimePeriodFormatterBuilder
     */
    public TimePeriodFormatterBuilder rejectSignedValues(boolean v) {
        iRejectSignedValues = v;
        return this;
    }

    /**
     * Never print zero values for the next and following appended fields,
     * unless no fields would be printed. If no fields are printed, the printer
     * forces at most one "printZeroRarely" field to print a zero.
     * <p>
     * This field setting is the default.
     *
     * @return this TimePeriodFormatterBuilder
     * @see #favorLastFieldForZero()
     * @see #favorFirstFieldForZero()
     */
    public TimePeriodFormatterBuilder printZeroRarely() {
        iPrintZeroSetting = PRINT_ZERO_RARELY;
        return this;
    }

    /**
     * Print zero values for the next and following appened fields only if the
     * duration supports it.
     *
     * @return this TimePeriodFormatterBuilder
     */
    public TimePeriodFormatterBuilder printZeroIfSupported() {
        iPrintZeroSetting = PRINT_ZERO_IF_SUPPORTED;
        return this;
    }

    /**
     * Always print zero values for the next and following appended fields,
     * even if the duration doesn't support it. The parser requires values for
     * fields that always print zero.
     *
     * @return this TimePeriodFormatterBuilder
     */
    public TimePeriodFormatterBuilder printZeroAlways() {
        iPrintZeroSetting = PRINT_ZERO_ALWAYS;
        return this;
    }

    /**
     * Append a field prefix which applies only to the next appended field. If
     * the field is not printed, neither is the prefix.
     *
     * @param text text to print before field only if field is printed
     * @return this TimePeriodFormatterBuilder
     * @see #appendSuffix
     */
    public TimePeriodFormatterBuilder appendPrefix(String text) {
        if (text == null) {
            throw new IllegalArgumentException();
        }
        return appendPrefix(new SingularAffix(text));
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
     * @return this TimePeriodFormatterBuilder
     * @see #appendSuffix
     */
    public TimePeriodFormatterBuilder appendPrefix(String singularText,
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
     * @return this TimePeriodFormatterBuilder
     * @see #appendSuffix
     */
    private TimePeriodFormatterBuilder appendPrefix(DurationFieldAffix prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        if (iPrefix != null) {
            prefix = new CompositeAffix(iPrefix, prefix);
        }
        iPrefix = prefix;
        return this;
    }

    /**
     * Instruct the printer to emit an integer years field, if supported.
     *
     * @return this TimePeriodFormatterBuilder
     */
    public TimePeriodFormatterBuilder appendYears() {
        appendField(1);
        return this;
    }

    /**
     * Instruct the printer to emit an integer years field, if supported.
     *
     * @return this TimePeriodFormatterBuilder
     */
    public TimePeriodFormatterBuilder appendMonths() {
        appendField(2);
        return this;
    }

    /**
     * Instruct the printer to emit an integer weeks field, if supported.
     *
     * @return this TimePeriodFormatterBuilder
     */
    public TimePeriodFormatterBuilder appendWeeks() {
        appendField(3);
        return this;
    }

    /**
     * Instruct the printer to emit an integer days field, if supported.
     *
     * @return this TimePeriodFormatterBuilder
     */
    public TimePeriodFormatterBuilder appendDays() {
        appendField(4);
        return this;
    }

    /**
     * Instruct the printer to emit an integer hours field, if supported.
     *
     * @return this TimePeriodFormatterBuilder
     */
    public TimePeriodFormatterBuilder appendHours() {
        appendField(5);
        return this;
    }

    /**
     * Instruct the printer to emit an integer minutes field, if supported.
     *
     * @return this TimePeriodFormatterBuilder
     */
    public TimePeriodFormatterBuilder appendMinutes() {
        appendField(6);
        return this;
    }

    /**
     * Instruct the printer to emit an integer seconds field, if supported.
     *
     * @return this TimePeriodFormatterBuilder
     */
    public TimePeriodFormatterBuilder appendSeconds() {
        appendField(7);
        return this;
    }

    /**
     * Instruct the printer to emit an integer millis field, if supported.
     *
     * @return this TimePeriodFormatterBuilder
     */
    public TimePeriodFormatterBuilder appendMillis() {
        appendField(8);
        return this;
    }

    /**
     * Instruct the printer to emit an integer millis field, if supported.
     *
     * @return this TimePeriodFormatterBuilder
     */
    public TimePeriodFormatterBuilder appendMillis3Digit() {
        appendField(8, 3);
        return this;
    }

    private void appendField(int type) {
        appendField(type, iMinPrintedDigits);
    }

    private void appendField(int type, int minPrinted) {
        FieldFormatter field = new FieldFormatter(minPrinted, iPrintZeroSetting,
            iMaxParsedDigits, iRejectSignedValues, type, iPrefix, null);
        iFormatters.add(field);
        if (iPrintZeroSetting == PRINT_ZERO_RARELY) {
            iAlternateFormatters.add(field);
        }
        iPrefix = null;
    }

    /**
     * Append a field suffix which applies only to the last appended field. If
     * the field is not printed, neither is the suffix.
     *
     * @param text text to print after field only if field is printed
     * @return this TimePeriodFormatterBuilder
     * @throws IllegalStateException if no field exists to append to
     * @see #appendPrefix
     */
    public TimePeriodFormatterBuilder appendSuffix(String text) {
        if (text == null) {
            throw new IllegalArgumentException();
        }
        return appendSuffix(new SingularAffix(text));
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
     * @return this TimePeriodFormatterBuilder
     * @throws IllegalStateException if no field exists to append to
     * @see #appendPrefix
     */
    public TimePeriodFormatterBuilder appendSuffix(String singularText,
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
     * @return this TimePeriodFormatterBuilder
     * @throws IllegalStateException if no field exists to append to
     * @see #appendPrefix
     */
    private TimePeriodFormatterBuilder appendSuffix(DurationFieldAffix suffix) {
        final Object originalField;
        if (iFormatters.size() > 0) {
            originalField = iFormatters.get(iFormatters.size() - 1);
        } else {
            originalField = null;
        }

        if (originalField == null || !(originalField instanceof FieldFormatter)) {
            throw new IllegalStateException("No field to apply suffix to");
        }

        clearPrefix();
        Object newField = new FieldFormatter((FieldFormatter) originalField, suffix);
        iFormatters.set(iFormatters.size() - 1, newField);

        int index = iAlternateFormatters.lastIndexOf(originalField);
        if (index >= 0) {
            iAlternateFormatters.set(index, newField);
        }

        return this;
    }

    /**
     * During printing, separators are only printed if fields are printed
     * following the latest one.
     * <p>
     * Note: appending a separator discontinues any further work on the latest
     * appended field.
     *
     * @return this TimePeriodFormatterBuilder
     */
    public TimePeriodFormatterBuilder appendSeparator(String text) {
        return appendSeparator(text, text);
    }

    /**
     * During printing, separators are only printed if fields are printed
     * following the latest one.
     * <p>
     * During parsing, either text parameter is accepted, and is
     * case-insensitive.
     * <p>
     * Note: appending a separator discontinues any further work on the latest
     * appended field.
     *
     * @param finalText alternate used if this is the final separator
     * printed
     * @return this TimePeriodFormatterBuilder
     */
    public TimePeriodFormatterBuilder appendSeparator(String text,
                                                    String finalText) {
        if (text == null || finalText == null) {
            throw new IllegalArgumentException();
        }

        clearPrefix();

        List formatters = iFormatters;

        if (formatters.size() == 0) {
            // Separator at the beginning would never print anything.
            return this;
        }

        // Create a composite over all the fields between separators.
        int i;
        Separator lastSeparator = null;
        for (i=formatters.size(); --i>=0; ) {
            if (formatters.get(i) instanceof Separator) {
                lastSeparator = (Separator) formatters.get(i);
                formatters = formatters.subList(i + 1, formatters.size());
                break;
            }
        }

        if (lastSeparator != null && formatters.size() == 0) {
            // Merge two adjacent separators together.
            iFormatters.set(i, lastSeparator.merge(text, finalText));
        } else {
            TimePeriodFormatter composite = createComposite(formatters);
            formatters.clear();
            formatters.add(composite);
            
            // The separator will be finished later.
            formatters.add(new Separator(text, finalText));
        }
            
        return this;
    }

    /**
     * If the printer doesn't print any field values, it forces a
     * "printZeroRarely" field to print. This setting controls which field is
     * selected.
     * <p>
     * It starts from the last appended field, and moves towards the first,
     * stopping until it finds a field that is supported by the duration being
     * printed. If no supported fields are found, then no fields are printed.
     * <p>
     * This setting is the default.
     *
     * @return this TimePeriodFormatterBuilder
     * @see #printZeroRarely()
     */
    public TimePeriodFormatterBuilder favorLastFieldForZero() {
        iFavorFirstFieldForZero = false;
        return this;
    }

    /**
     * If the printer doesn't print any field values, it forces a
     * "printZeroRarely" field to print. This setting controls which field is
     * selected.
     * <p>
     * It starts from the first appended field, and moves towards the last,
     * stopping until it finds a field that is supported by the duration being
     * printed. If no supported fields are found, then no fields are printed.
     *
     * @return this TimePeriodFormatterBuilder
     * @see #printZeroRarely()
     */
    public TimePeriodFormatterBuilder favorFirstFieldForZero() {
        iFavorFirstFieldForZero = true;
        return this;
    }

    private void clearPrefix() throws IllegalStateException {
        if (iPrefix != null) {
            throw new IllegalStateException("Prefix not followed by field");
        }
        iPrefix = null;
    }

    private static TimePeriodFormatter createComposite(List formatters) {
        if (formatters.size() == 1) {
            return (TimePeriodFormatter)formatters.get(0);
        } else {
            return new Composite(formatters);
        }
    }

    /**
     * Defines a formatted field's prefix or suffix text.
     */
    private static interface DurationFieldAffix {
        int calculatePrintedLength(int value);
        
        void printTo(StringBuffer buf, int value);
        
        void printTo(Writer out, int value) throws IOException;
        
        /**
         * @return new position after parsing affix, or ~position of failure
         */
        int parse(String durationStr, int position);

        /**
         * @return position where affix starts, or original ~position if not found
         */
        int scan(String durationStr, int position);
    }

    private static final class SingularAffix implements DurationFieldAffix {
        private final String iText;

        SingularAffix(String text) {
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

        public int parse(String durationStr, int position) {
            String text = iText;
            int textLength = text.length();
            if (durationStr.regionMatches(true, position, text, 0, textLength)) {
                return position + textLength;
            }
            return ~position;
        }

        public int scan(String durationStr, final int position) {
            String text = iText;
            int textLength = text.length();
            int sourceLength = durationStr.length();
            for (int pos = position; pos < sourceLength; pos++) {
                if (durationStr.regionMatches(true, pos, text, 0, textLength)) {
                    return pos;
                }
            }
            return ~position;
        }
    }

    private static final class PluralAffix implements DurationFieldAffix {
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

        public int parse(String durationStr, int position) {
            String text1 = iPluralText;
            String text2 = iSingularText; 

            if (text1.length() < text2.length()) {
                // Swap in order to match longer one first.
                String temp = text1;
                text1 = text2;
                text2 = temp;
            }

            if (durationStr.regionMatches
                (true, position, text1, 0, text1.length())) {
                return position + text1.length();
            }
            if (durationStr.regionMatches
                (true, position, text2, 0, text2.length())) {
                return position + text2.length();
            }

            return ~position;
        }

        public int scan(String durationStr, final int position) {
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

            int sourceLength = durationStr.length();
            for (int pos = position; pos < sourceLength; pos++) {
                if (durationStr.regionMatches(true, pos, text1, 0, textLength1)) {
                    return pos;
                }
                if (durationStr.regionMatches(true, pos, text2, 0, textLength2)) {
                    return pos;
                }
            }
            return ~position;
        }
    }

    private static final class CompositeAffix implements DurationFieldAffix {
        private final DurationFieldAffix iLeft;
        private final DurationFieldAffix iRight;

        CompositeAffix(DurationFieldAffix left, DurationFieldAffix right) {
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

        public int parse(String durationStr, int position) {
            position = iLeft.parse(durationStr, position);
            if (position >= 0) {
                position = iRight.parse(durationStr, position);
            }
            return position;
        }

        public int scan(String durationStr, final int position) {
            int pos = iLeft.scan(durationStr, position);
            if (pos >= 0) {
                return iRight.scan(durationStr, pos);
            }
            return ~position;
        }
    }

    private static final class FieldFormatter extends AbstractTimePeriodFormatter
        implements TimePeriodFormatter
    {
        private final int iMinPrintedDigits;
        private final int iPrintZeroSetting;
        private final int iMaxParsedDigits;
        private final boolean iRejectSignedValues;

        private final int iFieldType;

        private final DurationFieldAffix iPrefix;
        private final DurationFieldAffix iSuffix;

        FieldFormatter(int minPrintedDigits, int printZeroSetting,
                       int maxParsedDigits, boolean rejectSignedValues,
                       int fieldType, DurationFieldAffix prefix, DurationFieldAffix suffix) {
            iMinPrintedDigits = minPrintedDigits;
            iPrintZeroSetting = printZeroSetting;
            iMaxParsedDigits = maxParsedDigits;
            iRejectSignedValues = rejectSignedValues;
            iFieldType = fieldType;
            iPrefix = prefix;
            iSuffix = suffix;
        }

        FieldFormatter(FieldFormatter field, DurationFieldAffix suffix) {
            iMinPrintedDigits = field.iMinPrintedDigits;
            iPrintZeroSetting = field.iPrintZeroSetting;
            iMaxParsedDigits = field.iMaxParsedDigits;
            iRejectSignedValues = field.iRejectSignedValues;
            iFieldType = field.iFieldType;
            iPrefix = field.iPrefix;
            if (field.iSuffix != null) {
                suffix = new CompositeAffix(field.iSuffix, suffix);
            }
            iSuffix = suffix;
        }

        FieldFormatter(FieldFormatter field, int printZeroSetting) {
            iMinPrintedDigits = field.iMinPrintedDigits;
            iPrintZeroSetting = printZeroSetting;
            iMaxParsedDigits = field.iMaxParsedDigits;
            iRejectSignedValues = field.iRejectSignedValues;
            iFieldType = field.iFieldType;
            iPrefix = field.iPrefix;
            iSuffix = field.iSuffix;
        }

        public int countFieldsToPrint(ReadableTimePeriod period) {
            if (iPrintZeroSetting == PRINT_ZERO_ALWAYS || getFieldValue(period) >= 0) {
                return 1;
            }
            return 0;
        }

        public int countFieldsToPrint(ReadableTimePeriod period, int stopAt) {
            return stopAt <= 0 ? 0 : countFieldsToPrint(period);
        }

        public int calculatePrintedLength(ReadableTimePeriod period) {
            long valueLong = getFieldValue(period);
            if (valueLong < 0) {
                return 0;
            }

            int value = (int)valueLong;

            int sum = Math.max
                (FormatUtils.calculateDigitCount(value), iMinPrintedDigits);
            if (value < 0) {
                // Account for sign character
                sum++;
            }

            DurationFieldAffix affix;
            if ((affix = iPrefix) != null) {
                sum += affix.calculatePrintedLength(value);
            }
            if ((affix = iSuffix) != null) {
                sum += affix.calculatePrintedLength(value);
            }

            return sum;
        }
        
        public void printTo(StringBuffer buf, ReadableTimePeriod period) {
            long valueLong = getFieldValue(period);
            if (valueLong < 0) {
                return;
            }
            int value = (int)valueLong;

            DurationFieldAffix affix;
            if ((affix = iPrefix) != null) {
                affix.printTo(buf, value);
            }
            int minDigits = iMinPrintedDigits;
            if (minDigits <= 1) {
                FormatUtils.appendUnpaddedInteger(buf, value);
            } else {
                FormatUtils.appendPaddedInteger(buf, value, minDigits);
            }
            if ((affix = iSuffix) != null) {
                affix.printTo(buf, value);
            }
        }

        public void printTo(Writer out, ReadableTimePeriod period) throws IOException {
            long valueLong = getFieldValue(period);
            if (valueLong < 0) {
                return;
            }
            int value = (int)valueLong;

            DurationFieldAffix affix;
            if ((affix = iPrefix) != null) {
                affix.printTo(out, value);
            }
            int minDigits = iMinPrintedDigits;
            if (minDigits <= 1) {
                FormatUtils.writeUnpaddedInteger(out, value);
            } else {
                FormatUtils.writePaddedInteger(out, value, minDigits);
            }
            if ((affix = iSuffix) != null) {
                affix.printTo(out, value);
            }
        }

        public int parseInto(ReadWritableTimePeriod period,
                             String text, int position) {

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

            if (!mustParse && !isSupported(period.getPeriodType())) {
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

            boolean negative = false;
            int length = 0;
            while (length < limit) {
                char c = text.charAt(position + length);
                if (length == 0 && (c == '-' || c == '+') && !iRejectSignedValues) {
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
                // Since value may exceed max, use stock parser which checks
                // for this.
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
            
            setFieldValue(period, value);
                
            if (position >= 0 && iSuffix != null) {
                position = iSuffix.parse(text, position);
            }
                
            return position;
        }

        /**
         * @return negative value if nothing to print, otherwise lower 32 bits
         * is signed int value.
         */
        long getFieldValue(ReadableTimePeriod period) {
            PeriodType type;
            if (iPrintZeroSetting == PRINT_ZERO_ALWAYS) {
                type = null; // Don't need to check if supported.
            } else {
                type = period.getPeriodType();
            }

            int value;

            switch (iFieldType) {
            default:
                return -1;
            case 1:
                if (type != null && type.years().isSupported() == false) {
                    return -1;
                }
                value = period.getYears();
                break;
            case 2:
                if (type != null && type.months().isSupported() == false) {
                    return -1;
                }
                value = period.getMonths();
                break;
            case 3:
                if (type != null && type.weeks().isSupported() == false) {
                    return -1;
                }
                value = period.getWeeks();
                break;
            case 4:
                if (type != null && type.days().isSupported() == false) {
                    return -1;
                }
                value = period.getDays();
                break;
            case 5:
                if (type != null && type.hours().isSupported() == false) {
                    return -1;
                }
                value = period.getHours();
                break;
            case 6:
                if (type != null && type.minutes().isSupported() == false) {
                    return -1;
                }
                value = period.getMinutes();
                break;
            case 7:
                if (type != null && type.seconds().isSupported() == false) {
                    return -1;
                }
                value = period.getSeconds();
                break;
            case 8:
                if (type != null && type.millis().isSupported() == false) {
                    return -1;
                }
                value = period.getMillis();
                break;
            }

            if (value == 0 && iPrintZeroSetting == PRINT_ZERO_RARELY) {
                return -1;
            }

            return value & 0xffffffffL;
        }

        boolean isSupported(PeriodType type) {
            switch (iFieldType) {
            default:
                return false;
            case 1:
                return type.years().isSupported();
            case 2:
                return type.months().isSupported();
            case 3:
                return type.weeks().isSupported();
            case 4:
                return type.days().isSupported();
            case 5:
                return type.hours().isSupported();
            case 6:
                return type.minutes().isSupported();
            case 7:
                return type.seconds().isSupported();
            case 8:
                return type.millis().isSupported();
            }
        }

        void setFieldValue(ReadWritableTimePeriod period, int value) {
            switch (iFieldType) {
            default:
                break;
            case 1:
                period.setYears(value);
                break;
            case 2:
                period.setMonths(value);
                break;
            case 3:
                period.setWeeks(value);
                break;
            case 4:
                period.setDays(value);
                break;
            case 5:
                period.setHours(value);
                break;
            case 6:
                period.setMinutes(value);
                break;
            case 7:
                period.setSeconds(value);
                break;
            case 8:
                period.setMillis(value);
                break;
            }
        }

        int getPrintZeroSetting() {
            return iPrintZeroSetting;
        }
    }

    private static final class Literal extends AbstractTimePeriodFormatter
        implements TimePeriodFormatter
    {
        private final String iText;

        Literal(String text) {
            iText = text;
        }

        public int countFieldsToPrint(ReadableTimePeriod period, int stopAt) {
            return 0;
        }

        public int calculatePrintedLength(ReadableTimePeriod period) {
            return iText.length();
        }

        public void printTo(StringBuffer buf, ReadableTimePeriod period) {
            buf.append(iText);
        }

        public void printTo(Writer out, ReadableTimePeriod period) throws IOException {
            out.write(iText);
        }

        public int parseInto(ReadWritableTimePeriod period,
                             String periodStr, int position) {
            if (periodStr.regionMatches(true, position, iText, 0, iText.length())) {
                return position + iText.length();
            }
            return ~position;
        }
    }

    private static final class Separator extends AbstractTimePeriodFormatter
        implements TimePeriodFormatter
    {
        private final String iText;
        private final String iFinalText;

        private final TimePeriodFormatter iBefore;
        private final TimePeriodFormatter iAfter;

        Separator(String text, String finalText) {
            this(text, finalText, null, null);
        }

        Separator(String text, String finalText,
                  TimePeriodFormatter before, TimePeriodFormatter after) {
            iText = text;
            iFinalText = finalText;
            iBefore = before;
            iAfter = after;
        }

        public int countFieldsToPrint(ReadableTimePeriod period, int stopAt) {
            int sum = iBefore.countFieldsToPrint(period, stopAt);
            if (sum < stopAt) {
                sum += iAfter.countFieldsToPrint(period, stopAt);
            }
            return sum;
        }

        public int calculatePrintedLength(ReadableTimePeriod period) {
            int sum = iBefore.calculatePrintedLength(period)
                + iAfter.calculatePrintedLength(period);

            if (iBefore.countFieldsToPrint(period, 1) > 0) {
                int afterCount = iAfter.countFieldsToPrint(period, 2);
                if (afterCount > 0) {
                    sum += (afterCount > 1 ? iText : iFinalText).length();
                }
            }

            return sum;
        }

        public void printTo(StringBuffer buf, ReadableTimePeriod period) {
            TimePeriodPrinter before = iBefore;
            TimePeriodPrinter after = iAfter;

            before.printTo(buf, period);

            if (before.countFieldsToPrint(period, 1) > 0) {
                int afterCount = after.countFieldsToPrint(period, 2);
                if (afterCount > 0) {
                    buf.append(afterCount > 1 ? iText : iFinalText);
                }
            }

            after.printTo(buf, period);
        }

        public void printTo(Writer out, ReadableTimePeriod period) throws IOException {
            TimePeriodPrinter before = iBefore;
            TimePeriodPrinter after = iAfter;

            before.printTo(out, period);

            if (before.countFieldsToPrint(period, 1) > 0) {
                int afterCount = after.countFieldsToPrint(period, 2);
                if (afterCount > 0) {
                    out.write(afterCount > 1 ? iText : iFinalText);
                }
            }

            after.printTo(out, period);
        }

        public int parseInto(ReadWritableTimePeriod period,
                             String periodStr, int position) {
            final int oldPos = position;

            position = iBefore.parseInto(period, periodStr, position);

            if (position < 0) {
                return position;
            }

            if (position > oldPos) {
                // Since position advanced, this separator is
                // allowed. Optionally parse it.
                if (periodStr.regionMatches(true, position, iText, 0, iText.length())) {
                    position += iText.length();
                } else if (iText != iFinalText && periodStr.regionMatches
                           (true, position, iFinalText, 0, iFinalText.length())) {
                    position += iFinalText.length();
                }
            }

            return iAfter.parseInto(period, periodStr, position);
        }

        Separator merge(String text, String finalText) {
            return new Separator(iText + text, iFinalText + finalText, iBefore, iAfter);
        }

        Separator finish(TimePeriodFormatter before, TimePeriodFormatter after) {
            return new Separator(iText, iFinalText, before, after);
        }
    }

    private static final class Composite extends AbstractTimePeriodFormatter
        implements TimePeriodFormatter
    {
        private final TimePeriodFormatter[] iFormatters;

        Composite(List formatters) {
            iFormatters = (TimePeriodFormatter[])formatters.toArray
                (new TimePeriodFormatter[formatters.size()]);
        }

        public int countFieldsToPrint(ReadableTimePeriod period, int stopAt) {
            int sum = 0;
            TimePeriodPrinter[] printers = iFormatters;
            for (int i=printers.length; sum < stopAt && --i>=0; ) {
                sum += printers[i].countFieldsToPrint(period);
            }
            return sum;
        }

        public int calculatePrintedLength(ReadableTimePeriod period) {
            int sum = 0;
            TimePeriodPrinter[] printers = iFormatters;
            for (int i=printers.length; --i>=0; ) {
                sum += printers[i].calculatePrintedLength(period);
            }
            return sum;
        }

        public void printTo(StringBuffer buf, ReadableTimePeriod period) {
            TimePeriodPrinter[] printers = iFormatters;
            int len = printers.length;
            for (int i=0; i<len; i++) {
                printers[i].printTo(buf, period);
            }
        }

        public void printTo(Writer out, ReadableTimePeriod period) throws IOException {
            TimePeriodPrinter[] printers = iFormatters;
            int len = printers.length;
            for (int i=0; i<len; i++) {
                printers[i].printTo(out, period);
            }
        }

        public int parseInto(ReadWritableTimePeriod period,
                             String periodStr, int position) {
            TimePeriodParser[] parsers = iFormatters;

            if (parsers == null) {
                throw new UnsupportedOperationException();
            }

            int len = parsers.length;
            for (int i=0; i<len && position >= 0; i++) {
                position = parsers[i].parseInto(period, periodStr, position);
            }
            return position;
        }
    }

    private static final class AlternateSelector extends AbstractTimePeriodFormatter
        implements TimePeriodFormatter
    {
        private final TimePeriodFormatter iPrimaryFormatter;
        private final TimePeriodPrinter[] iAlternatePrinters;
        private final boolean iFavorFirstFieldForZero;

        AlternateSelector(TimePeriodFormatter primaryFormatter,
                          List alternatePrinters,
                          boolean favorFirstFieldForZero) {
            iPrimaryFormatter = primaryFormatter;
            iAlternatePrinters = (TimePeriodPrinter[])alternatePrinters.toArray
                (new TimePeriodPrinter[alternatePrinters.size()]);
            iFavorFirstFieldForZero = favorFirstFieldForZero;
        }

        public int countFieldsToPrint(ReadableTimePeriod period, int stopAt) {
            int count = iPrimaryFormatter.countFieldsToPrint(period, stopAt);
            if (count < 1 && stopAt >= 1) {
                if (chooseFieldToPrint(period) != null) {
                    return 1;
                }
            }
            return count;
        }

        public int calculatePrintedLength(ReadableTimePeriod period) {
            if (iPrimaryFormatter.countFieldsToPrint(period, 1) > 0) {
                return iPrimaryFormatter.calculatePrintedLength(period);
            }

            Object chosenOne = chooseFieldToPrint(period);

            int sum = 0;
            TimePeriodPrinter[] printers = iAlternatePrinters;
            for (int i=printers.length; --i>=0; ) {
                TimePeriodPrinter dp = printers[i];
                if (dp == chosenOne || !(dp instanceof FieldFormatter)) {
                    sum += dp.calculatePrintedLength(period);
                }
            }
            return sum;
        }

        public void printTo(StringBuffer buf, ReadableTimePeriod period) {
            if (iPrimaryFormatter.countFieldsToPrint(period, 1) > 0) {
                iPrimaryFormatter.printTo(buf, period);
                return;
            }

            Object chosenOne = chooseFieldToPrint(period);
            
            TimePeriodPrinter[] printers = iAlternatePrinters;
            int len = printers.length;
            for (int i=0; i<len; i++) {
                TimePeriodPrinter dp = printers[i];
                if (dp == chosenOne || !(dp instanceof FieldFormatter)) {
                    dp.printTo(buf, period);
                }
            }
        }

        public void printTo(Writer out, ReadableTimePeriod period) throws IOException {
            if (iPrimaryFormatter.countFieldsToPrint(period, 1) > 0) {
                iPrimaryFormatter.printTo(out, period);
                return;
            }
            
            Object chosenOne = chooseFieldToPrint(period);

            TimePeriodPrinter[] printers = iAlternatePrinters;
            int len = printers.length;
            for (int i=0; i<len; i++) {
                TimePeriodPrinter dp = printers[i];
                if (dp == chosenOne || !(dp instanceof FieldFormatter)) {
                    dp.printTo(out, period);
                }
            }
        }

        public int parseInto(ReadWritableTimePeriod period,
                             String periodStr, int position) {
            return iPrimaryFormatter.parseInto(period, periodStr, position);
        }

        private FieldFormatter chooseFieldToPrint(ReadableTimePeriod period) {
            PeriodType type = period.getPeriodType();
            TimePeriodPrinter[] printers = iAlternatePrinters;
            if (iFavorFirstFieldForZero) {
                int len = printers.length;
                for (int i=0; i<len; i++) {
                    TimePeriodPrinter dp = printers[i];
                    if (dp instanceof FieldFormatter) {
                        FieldFormatter ff = (FieldFormatter) dp;
                        if (ff.isSupported(type)) {
                            if (ff.getPrintZeroSetting() == PRINT_ZERO_RARELY) {
                                ff = new FieldFormatter(ff, PRINT_ZERO_IF_SUPPORTED);
                                printers[i] = ff;
                            }
                            return ff;
                        }
                    }
                }
            } else {
                for (int i=printers.length; --i>=0; ) {
                    TimePeriodPrinter dp = printers[i];
                    if (dp instanceof FieldFormatter) {
                        FieldFormatter ff = (FieldFormatter) dp;
                        if (ff.isSupported(type)) {
                            if (ff.getPrintZeroSetting() == PRINT_ZERO_RARELY) {
                                ff = new FieldFormatter(ff, PRINT_ZERO_IF_SUPPORTED);
                                printers[i] = ff;
                            }
                            return ff;
                        }
                    }
                }
            }
            return null;
        }
    }

}
