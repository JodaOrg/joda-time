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
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DurationField;
import org.joda.time.DurationType;
import org.joda.time.ReadableDuration;

/**
 * 
 *
 * @author Brian S O'Neill
 */
public class DurationFormatterBuilder {
    private static final int PRINT_ZERO_NEVER = 0;
    private static final int PRINT_ZERO_MAYBE = 1;
    private static final int PRINT_ZERO_ALWAYS = 2;

    private int iMinPrintedDigits = 1;
    private int iPrintZeroSetting;

    private DurationFieldAffix iPrefix;

    // List of separate DurationFormatters.
    private List iFormatters;

    public DurationFormatterBuilder() {
       iFormatters = new ArrayList();
    }

    /**
     * Converts to a DurationPrinter that prints using all the appended
     * elements. Subsequent changes to this builder do not affect the returned
     * printer.
     */
    public DurationPrinter toPrinter() {
        return toPrinter(iFormatters);
    }

    /**
     * Converts to a DurationPrinter that prints using all the appended
     * elements. Subsequent changes to this builder do not affect the returned
     * printer.
     *
     * @param alternate alternate text to print when printer emits no fields
     */
    public DurationPrinter toPrinter(String alternate) {
        DurationPrinter printer = toPrinter();
        if (alternate != null) {
            return new AlternateSelector(printer, new Literal(alternate));
        }
        return printer;
    }

    /**
     * Converts to a DurationPrinter that prints using all the appended
     * elements. Subsequent changes to this builder do not affect the returned
     * printer.
     *
     * @param alternate alternate printer to use when printer emits no fields
     */
    public DurationPrinter toPrinter(DurationPrinter alternate) {
        DurationPrinter printer = toPrinter();
        if (alternate != null) {
            return new AlternateSelector(printer, alternate);
        }
        return printer;
    }

    private static DurationPrinter toPrinter(List formatters) {
        int size = formatters.size();
        if (size >= 2 && formatters.get(1) instanceof Separator) {
            DurationPrinter before = (DurationPrinter) formatters.get(0);
            if (size == 2) {
                // Separator at the end would never print anything.
                return before;
            }
            return ((Separator) formatters.get(1)).finish
                (before, toPrinter(formatters.subList(2, size)));
        }
        return createComposite(formatters);
    }

    /**
     * Depending on what rules are applied, a parser may not be buildable due
     * to ambiguities that may arise during parsing.
     */
    /*
    public boolean canBuildParser() {
        // TODO
        return true;
    }
    */

    /**
     * Clears out all the appended elements, allowing this builder to be
     * reused.
     */
    public void clear() {
        iMinPrintedDigits = 1;
        iPrintZeroSetting = PRINT_ZERO_NEVER;
        iPrefix = null;
        iFormatters.clear();
    }

    /**
     * Appends just a printer. With no matching parser, a parser cannot be
     * built from this DurationFormatterBuilder.
     */
    public DurationFormatterBuilder append(DurationPrinter printer)
        throws IllegalArgumentException
    {
        if (printer == null) {
            throw new IllegalArgumentException("No printer supplied");
        }
        clearPrefix();
        iFormatters.add(printer);
        return this;
    }

    /**
     * Instructs the printer to emit specific text, and the parser to expect
     * it. The parser is case-insensitive.
     *
     * @throws IllegalArgumentException if text is null
     */
    public DurationFormatterBuilder appendLiteral(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Literal must not be null");
        }
        clearPrefix();
        iFormatters.add(new Literal(text));
        return this;
    }

    /**
     * Set the minimum digits printed for the next and following appended
     * fields. By default, the minimum digits printed is one. If the field value
     * is zero, it is not printed unless a printZero rule is applied.
     */
    public DurationFormatterBuilder minimumPrintedDigits(int minDigits) {
        iMinPrintedDigits = minDigits;
        return this;
    }

    /**
     * Set the maximum digits parsed for the next and following appended
     * fields. By default, the maximum digits parsed is ten.
     */
    public DurationFormatterBuilder maximumParsedDigits(int maxDigits) {
        // TODO
        return this;
    }

    /**
     * Reject signed values when parsing the next and following appended fields.
     */
    public DurationFormatterBuilder rejectSignedValues() {
        // TODO
        return this;
    }

    /**
     * Never print zero values for the next and following appended fields. This
     * is the default setting.
     */
    public DurationFormatterBuilder printZeroNever() {
        iPrintZeroSetting = PRINT_ZERO_NEVER;
        return this;
    }

    /**
     * Print zero values for the next and following appened fields only if the
     * duration supports it.
     */
    public DurationFormatterBuilder printZeroMaybe() {
        iPrintZeroSetting = PRINT_ZERO_MAYBE;
        return this;
    }

    /**
     * Always print zero values for the next and following appended fields,
     * even if the duration doesn't support it.
     */
    public DurationFormatterBuilder printZeroAlways() {
        iPrintZeroSetting = PRINT_ZERO_ALWAYS;
        return this;
    }

    /**
     * Append a field prefix which applies only to the next appended field. If
     * the field is not printed, neither is the prefix.
     *
     * @param text text to print before field only if field is printed
     * @see #appendSuffix
     */
    public DurationFormatterBuilder appendPrefix(String text) {
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
     * @see #appendSuffix
     */
    public DurationFormatterBuilder appendPrefix(String singularText,
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
     * @see #appendSuffix
     */
    public DurationFormatterBuilder appendPrefix(DurationFieldAffix prefix) {
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
     * Instruct the printer to emit a numeric years field, if supported.
     */
    public DurationFormatterBuilder appendYears() {
        appendField(1);
        return this;
    }

    /**
     * Instruct the printer to emit a numeric years field, if supported.
     */
    public DurationFormatterBuilder appendMonths() {
        appendField(2);
        return this;
    }

    /**
     * Instruct the printer to emit a numeric weeks field, if supported.
     */
    public DurationFormatterBuilder appendWeeks() {
        appendField(3);
        return this;
    }

    /**
     * Instruct the printer to emit a numeric days field, if supported.
     */
    public DurationFormatterBuilder appendDays() {
        appendField(4);
        return this;
    }

    /**
     * Instruct the printer to emit a numeric hours field, if supported.
     */
    public DurationFormatterBuilder appendHours() {
        appendField(5);
        return this;
    }

    /**
     * Instruct the printer to emit a numeric minutes field, if supported.
     */
    public DurationFormatterBuilder appendMinutes() {
        appendField(6);
        return this;
    }

    /**
     * Instruct the printer to emit a numeric seconds field, if supported.
     */
    public DurationFormatterBuilder appendSeconds() {
        appendField(7);
        return this;
    }

    /**
     * Instruct the printer to emit a numeric millis field, if supported.
     */
    public DurationFormatterBuilder appendMillis() {
        appendField(8);
        return this;
    }

    private void appendField(int type) {
        iFormatters.add(new FieldFormatter(iMinPrintedDigits, iPrintZeroSetting,
                                           type, iPrefix, null));
        iPrefix = null;
    }

    /**
     * Append a field suffix which applies only to the last appended field. If
     * the field is not printed, neither is the suffix.
     *
     * @param text text to print after field only if field is printed
     * @throws IllegalStateException if no field exists to append to
     * @see #appendPrefix
     */
    public DurationFormatterBuilder appendSuffix(String text) {
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
     * @throws IllegalStateException if no field exists to append to
     * @see #appendPrefix
     */
    public DurationFormatterBuilder appendSuffix(String singularText,
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
     * @throws IllegalStateException if no field exists to append to
     * @see #appendPrefix
     */
    public DurationFormatterBuilder appendSuffix(DurationFieldAffix suffix) {
        Object f = null;
        if (iFormatters.size() > 0) {
            f = iFormatters.get(iFormatters.size() - 1);
        }
        if (!(f instanceof FieldFormatter)) {
            throw new IllegalStateException("No field to apply suffix to");
        }
        clearPrefix();
        f = new FieldFormatter((FieldFormatter) f, suffix);
        iFormatters.set(iFormatters.size() - 1, f);
        return this;
    }

    /**
     * During printing, separators are only printed if fields are printed
     * following the latest one.
     * <p>
     * Note: appending a separator discontinues any further work on the latest
     * appended field.
     */
    public DurationFormatterBuilder appendSeparator(String text) {
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
     */
    public DurationFormatterBuilder appendSeparator(String text,
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
            DurationPrinter composite = createComposite(formatters);
            formatters.clear();
            formatters.add(composite);
            
            // The separator will be finished later.
            formatters.add(new Separator(text, finalText));
        }
            
        return this;
    }

    private void clearPrefix() throws IllegalStateException {
        if (iPrefix != null) {
            throw new IllegalStateException("Prefix not followed by field");
        }
        iPrefix = null;
    }

    private static DurationPrinter createComposite(List formatters) {
        if (formatters.size() == 1) {
            return (DurationPrinter)formatters.get(0);
        } else {
            return new Composite(formatters);
        }
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
    }

    private static final class FieldFormatter extends AbstractDurationFormatter
        implements DurationPrinter
    {
        private final int iMinPrintedDigits;
        private final int iPrintZeroSetting;

        private final int iFieldType;

        private final DurationFieldAffix iPrefix;
        private final DurationFieldAffix iSuffix;

        FieldFormatter(int minPrintedDigits, int printZeroSetting,
                       int fieldType, DurationFieldAffix prefix, DurationFieldAffix suffix) {
            iMinPrintedDigits = minPrintedDigits;
            iPrintZeroSetting = printZeroSetting;
            iFieldType = fieldType;
            iPrefix = prefix;
            iSuffix = suffix;
        }

        FieldFormatter(FieldFormatter field, DurationFieldAffix suffix) {
            iMinPrintedDigits = field.iMinPrintedDigits;
            iPrintZeroSetting = field.iPrintZeroSetting;
            iFieldType = field.iFieldType;
            iPrefix = field.iPrefix;
            if (field.iSuffix != null) {
                suffix = new CompositeAffix(field.iSuffix, suffix);
            }
            iSuffix = suffix;
        }

        public int countFieldsToPrint(ReadableDuration duration) {
            if (iPrintZeroSetting == PRINT_ZERO_ALWAYS || getFieldValue(duration) >= 0) {
                return 1;
            }
            return 0;
        }

        public int countFieldsToPrint(ReadableDuration duration, int stopAt) {
            return stopAt <= 0 ? 0 : countFieldsToPrint(duration);
        }

        public int calculatePrintedLength(ReadableDuration duration) {
            long valueLong = getFieldValue(duration);
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
        
        public void printTo(StringBuffer buf, ReadableDuration duration) {
            long valueLong = getFieldValue(duration);
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

        public void printTo(Writer out, ReadableDuration duration) throws IOException {
            long valueLong = getFieldValue(duration);
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

        /**
         * @return negative value if nothing to print, otherwise lower 32 bits
         * is signed int value.
         */
        long getFieldValue(ReadableDuration duration) {
            DurationType type;
            if (iPrintZeroSetting == PRINT_ZERO_ALWAYS) {
                type = null; // Don't need to check if supported.
            } else {
                type = duration.getDurationType();
            }

            int value;

            switch (iFieldType) {
            default:
                return -1;
            case 1:
                if (type != null && type.years().isSupported() == false) {
                    return -1;
                }
                value = duration.getYears();
                break;
            case 2:
                if (type != null && type.months().isSupported() == false) {
                    return -1;
                }
                value = duration.getMonths();
                break;
            case 3:
                if (type != null && type.weeks().isSupported() == false) {
                    return -1;
                }
                value = duration.getWeeks();
                break;
            case 4:
                if (type != null && type.days().isSupported() == false) {
                    return -1;
                }
                value = duration.getDays();
                break;
            case 5:
                if (type != null && type.hours().isSupported() == false) {
                    return -1;
                }
                value = duration.getHours();
                break;
            case 6:
                if (type != null && type.minutes().isSupported() == false) {
                    return -1;
                }
                value = duration.getMinutes();
                break;
            case 7:
                if (type != null && type.seconds().isSupported() == false) {
                    return -1;
                }
                value = duration.getSeconds();
                break;
            case 8:
                if (type != null && type.millis().isSupported() == false) {
                    return -1;
                }
                value = duration.getMillis();
                break;
            }

            if (value == 0 && iPrintZeroSetting == PRINT_ZERO_NEVER) {
                return -1;
            }

            return value & 0xffffffffL;
        }
    }

    private static final class Literal extends AbstractDurationFormatter
        implements DurationPrinter
    {
        private final String iText;

        Literal(String text) {
            iText = text;
        }

        public int countFieldsToPrint(ReadableDuration duration, int stopAt) {
            return 0;
        }

        public int calculatePrintedLength(ReadableDuration duration) {
            return iText.length();
        }

        public void printTo(StringBuffer buf, ReadableDuration duration) {
            buf.append(iText);
        }

        public void printTo(Writer out, ReadableDuration duration) throws IOException {
            out.write(iText);
        }
    }

    private static final class Separator extends AbstractDurationFormatter
        implements DurationPrinter
    {
        private final String iText;
        private final String iFinalText;

        private final DurationPrinter iBefore;
        private final DurationPrinter iAfter;

        Separator(String text, String finalText) {
            this(text, finalText, null, null);
        }

        Separator(String text, String finalText,
                  DurationPrinter before, DurationPrinter after) {
            iText = text;
            iFinalText = finalText;
            iBefore = before;
            iAfter = after;
        }

        public int countFieldsToPrint(ReadableDuration duration, int stopAt) {
            int sum = iBefore.countFieldsToPrint(duration, stopAt);
            if (sum < stopAt) {
                sum += iAfter.countFieldsToPrint(duration, stopAt);
            }
            return sum;
        }

        public int calculatePrintedLength(ReadableDuration duration) {
            int sum = iBefore.calculatePrintedLength(duration)
                + iAfter.calculatePrintedLength(duration);

            if (iBefore.countFieldsToPrint(duration, 1) > 0) {
                int afterCount = iAfter.countFieldsToPrint(duration, 2);
                if (afterCount > 0) {
                    sum += (afterCount > 1 ? iText : iFinalText).length();
                }
            }

            return sum;
        }

        public void printTo(StringBuffer buf, ReadableDuration duration) {
            DurationPrinter before = iBefore;
            DurationPrinter after = iAfter;

            before.printTo(buf, duration);

            if (before.countFieldsToPrint(duration, 1) > 0) {
                int afterCount = after.countFieldsToPrint(duration, 2);
                if (afterCount > 0) {
                    buf.append(afterCount > 1 ? iText : iFinalText);
                }
            }

            after.printTo(buf, duration);
        }

        public void printTo(Writer out, ReadableDuration duration) throws IOException {
            DurationPrinter before = iBefore;
            DurationPrinter after = iAfter;

            before.printTo(out, duration);

            if (before.countFieldsToPrint(duration, 1) > 0) {
                int afterCount = after.countFieldsToPrint(duration, 2);
                if (afterCount > 0) {
                    out.write(afterCount > 1 ? iText : iFinalText);
                }
            }

            after.printTo(out, duration);
        }

        Separator merge(String text, String finalText) {
            return new Separator(iText + text, iFinalText + finalText, iBefore, iAfter);
        }

        Separator finish(DurationPrinter before, DurationPrinter after) {
            return new Separator(iText, iFinalText, before, after);
        }
    }

    private static final class Composite extends AbstractDurationFormatter
        implements DurationPrinter
    {
        private final DurationPrinter[] iFormatters;

        Composite(List formatters) {
            iFormatters = (DurationPrinter[])formatters.toArray
                (new DurationPrinter[formatters.size()]);
        }

        public int countFieldsToPrint(ReadableDuration duration, int stopAt) {
            int sum = 0;
            DurationPrinter[] printers = iFormatters;
            for (int i=printers.length; sum < stopAt && --i>=0; ) {
                sum += printers[i].countFieldsToPrint(duration);
            }
            return sum;
        }

        public int calculatePrintedLength(ReadableDuration duration) {
            int sum = 0;
            DurationPrinter[] printers = iFormatters;
            for (int i=printers.length; --i>=0; ) {
                sum += printers[i].calculatePrintedLength(duration);
            }
            return sum;
        }

        public void printTo(StringBuffer buf, ReadableDuration duration) {
            DurationPrinter[] printers = iFormatters;
            int len = printers.length;
            for (int i=0; i<len; i++) {
                printers[i].printTo(buf, duration);
            }
        }

        public void printTo(Writer out, ReadableDuration duration) throws IOException {
            DurationPrinter[] printers = iFormatters;
            int len = printers.length;
            for (int i=0; i<len; i++) {
                printers[i].printTo(out, duration);
            }
        }
    }

    private static final class AlternateSelector extends AbstractDurationFormatter
        implements DurationPrinter
    {
        private final DurationPrinter iPrimary;
        private final DurationPrinter iAlternate;

        AlternateSelector(DurationPrinter primary, DurationPrinter alternate) {
            iPrimary = primary;
            iAlternate = alternate;
        }

        public int countFieldsToPrint(ReadableDuration duration, int stopAt) {
            int count = iPrimary.countFieldsToPrint(duration, stopAt);
            if (count <= 0) {
                count = iAlternate.countFieldsToPrint(duration, stopAt);
            }
            return count;
        }

        public int calculatePrintedLength(ReadableDuration duration) {
            if (iPrimary.countFieldsToPrint(duration, 1) > 0) {
                return iPrimary.calculatePrintedLength(duration);
            } else {
                return iAlternate.calculatePrintedLength(duration);
            }
        }

        public void printTo(StringBuffer buf, ReadableDuration duration) {
            if (iPrimary.countFieldsToPrint(duration, 1) > 0) {
                iPrimary.printTo(buf, duration);
            } else {
                iAlternate.printTo(buf, duration);
            }
        }

        public void printTo(Writer out, ReadableDuration duration) throws IOException {
            if (iPrimary.countFieldsToPrint(duration, 1) > 0) {
                iPrimary.printTo(out, duration);
            } else {
                iAlternate.printTo(out, duration);
            }
        }
    }

}
