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

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadWritableInstant;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;

/**
 * Controls the printing and parsing of a datetime to and from a string.
 * <p>
 * This class is the main API for printing and parsing used by most applications.
 * Instances of this class are created via one of three factory classes:
 * <ul>
 * <li>{@link DateTimeFormat} - formats by pattern and style</li>
 * <li>{@link ISODateTimeFormat} - ISO8601 formats</li>
 * <li>{@link DateTimeFormatterBuilder} - complex formats created via method calls</li>
 * </ul>
 * <p>
 * An instance of this class holds a reference internally to one printer and
 * one parser. It is possible that one of these may be null, in which case the
 * formatter cannot print/parse. This can be checked via the {@link #isPrinter()}
 * and {@link #isParser()} methods.
 * <p>
 * The underlying printer/parser can be altered to behave exactly as required
 * by using one of the decorator modifiers:
 * <ul>
 * <li>{@link #withLocale(Locale)} - returns a new formatter that uses the specified locale</li>
 * <li>{@link #withZone(DateTimeZone)} - returns a new formatter that uses the specified time zone</li>
 * <li>{@link #withChronology(Chronology)} - returns a new formatter that uses the specified chronology</li>
 * <li>{@link #withOffsetParsed()} - returns a new formatter that returns the parsed time zone offset</li>
 * </ul>
 * Each of these returns a new formatter (instances of this class are immutable).
 * <p>
 * The main methods of the class are the <code>printXxx</code> and
 * <code>parseXxx</code> methods. These are used as follows:
 * <pre>
 * // print using the defaults (default locale, chronology/zone of the datetime)
 * String dateStr = formatter.print(dt);
 * // print using the French locale
 * String dateStr = formatter.withLocale(Locale.FRENCH).print(dt);
 * // print using the UTC zone
 * String dateStr = formatter.withZone(DateTimeZone.UTC).print(dt);
 * 
 * // parse using the Paris zone
 * DateTime date = formatter.withZone(DateTimeZone.forID("Europe/Paris")).parseDateTime(str);
 * </pre>
 * 
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public class DateTimeFormatter {

    /** The internal printer used to output the datetime. */
    private final DateTimePrinter iPrinter;
    /** The internal parser used to output the datetime. */
    private final DateTimeParser iParser;
    /** The locale to use for printing and parsing. */
    private final Locale iLocale;
    /** Whether the offset is parsed. */
    private final boolean iOffsetParsed;
    /** The chronology to use as an override. */
    private final Chronology iChrono;
    /** The zone to use as an override. */
    private final DateTimeZone iZone;

    /**
     * Creates a new formatter, however you will normally use the factory
     * or the builder.
     * 
     * @param printer  the internal printer, null if cannot print
     * @param parser  the internal parser, null if cannot parse
     */
    public DateTimeFormatter(
            DateTimePrinter printer, DateTimeParser parser) {
        super();
        iPrinter = printer;
        iParser = parser;
        iLocale = null;
        iOffsetParsed = false;
        iChrono = null;
        iZone = null;
    }

    /**
     * Constructor.
     */
    private DateTimeFormatter(
            DateTimePrinter printer, DateTimeParser parser,
            Locale locale, boolean offsetParsed,
            Chronology chrono, DateTimeZone zone) {
        super();
        iPrinter = printer;
        iParser = parser;
        iLocale = locale;
        iOffsetParsed = offsetParsed;
        iChrono = chrono;
        iZone = zone;
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
    public DateTimePrinter getPrinter() {
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
    public DateTimeParser getParser() {
        return iParser;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new formatter with a different locale that will be used
     * for printing and parsing.
     * <p>
     * A DateTimeFormatter is immutable, so a new instance is returned,
     * and the original is unaltered and still usable.
     * 
     * @param locale  the locale to use
     * @return the new formatter
     */
    public DateTimeFormatter withLocale(Locale locale) {
        locale = (locale == null ? Locale.getDefault() : locale);
        if (locale.equals(getLocale())) {
            return this;
        }
        return new DateTimeFormatter(iPrinter, iParser, locale, iOffsetParsed, iChrono, iZone);
    }

    /**
     * Gets the locale that will be used for printing and parsing.
     * 
     * @return the locale to use
     */
    public Locale getLocale() {
        return iLocale;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new formatter that will create a datetime with a time zone
     * equal to that of the offset of the parsed string.
     * <p>
     * After calling this method, a string '2004-06-09T10:20:30-08:00' will
     * create a datetime with a zone of -08:00 (a fixed zone, with no daylight
     * savings rules). If the parsed string represents a local time (no zone
     * offset) the parsed datetime will be in the default zone.
     * <p>
     * Calling this method sets the override zone to null.
     * Calling the override zone method sets this flag off.
     * 
     * @return the new formatter
     */
    public DateTimeFormatter withOffsetParsed() {
        if (iOffsetParsed == true) {
            return this;
        }
        return new DateTimeFormatter(iPrinter, iParser, iLocale, true, iChrono, null);
    }

    /**
     * Checks whether the offset from the string is used as the zone of
     * the parsed datetime.
     * 
     * @return true if the offset from the string is used as the zone
     */
    public boolean isOffsetParsed() {
        return iOffsetParsed;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new formatter that will use the specified chronology in
     * preference to that of the printed object, or ISO on a parse.
     * <p>
     * When printing, this chronolgy will be used in preference to the chronology
     * from the datetime that would otherwise be used.
     * <p>
     * When parsing, this chronology will be set on the parsed datetime.
     * <p>
     * A null chronology means of no-override.
     * If both an override chronology and an override zone are set, the
     * override zone will take precedence over the zone in the chronology.
     * 
     * @param chrono  the chronology to use as an override
     * @return the new formatter
     */
    public DateTimeFormatter withChronology(Chronology chrono) {
        if (iChrono == chrono) {
            return this;
        }
        return new DateTimeFormatter(iPrinter, iParser, iLocale, iOffsetParsed, chrono, iZone);
    }

    /**
     * Gets the chronology to use as an override.
     * 
     * @return the chronology to use as an override
     */
    public Chronology getChronolgy() {
        return iChrono;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new formatter that will use the specified zone in preference
     * to the zone of the printed object, or default zone on a parse.
     * <p>
     * When printing, this zone will be used in preference to the zone
     * from the datetime that would otherwise be used.
     * <p>
     * When parsing, this zone will be set on the parsed datetime.
     * <p>
     * A null zone means of no-override.
     * If both an override chronology and an override zone are set, the
     * override zone will take precedence over the zone in the chronology.
     * 
     * @param zone  the zone to use as an override
     * @return the new formatter
     */
    public DateTimeFormatter withZone(DateTimeZone zone) {
        if (iZone == zone) {
            return this;
        }
        return new DateTimeFormatter(iPrinter, iParser, iLocale, false, iChrono, zone);
    }

    /**
     * Gets the zone to use as an override.
     * 
     * @return the zone to use as an override
     */
    public DateTimeZone getZone() {
        return iZone;
    }

    //-----------------------------------------------------------------------
    /**
     * Prints a ReadableInstant, using the chronology supplied by the instant.
     *
     * @param buf  formatted instant is appended to this buffer
     * @param instant  instant to format, null means now
     */
    public void printTo(StringBuffer buf, ReadableInstant instant) {
        checkPrinter();
        
        long millis = DateTimeUtils.getInstantMillis(instant);
        Chronology chrono = DateTimeUtils.getInstantChronology(instant);
        printTo(buf, millis, chrono);
    }

    /**
     * Prints a ReadableInstant, using the chronology supplied by the instant.
     *
     * @param out  formatted instant is written out
     * @param instant  instant to format, null means now
     */
    public void printTo(Writer out, ReadableInstant instant) throws IOException {
        checkPrinter();
        
        long millis = DateTimeUtils.getInstantMillis(instant);
        Chronology chrono = DateTimeUtils.getInstantChronology(instant);
        printTo(out, millis, chrono);
    }

    //-----------------------------------------------------------------------
    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * using ISO chronology in the default DateTimeZone.
     *
     * @param buf  formatted instant is appended to this buffer
     * @param instant  millis since 1970-01-01T00:00:00Z
     */
    public void printTo(StringBuffer buf, long instant) {
        checkPrinter();
        
        printTo(buf, instant, null);
    }

    /**
     * Prints an instant from milliseconds since 1970-01-01T00:00:00Z,
     * using ISO chronology in the default DateTimeZone.
     *
     * @param out  formatted instant is written out
     * @param instant  millis since 1970-01-01T00:00:00Z
     */
    public void printTo(Writer out, long instant) throws IOException {
        checkPrinter();
        
        printTo(out, instant, null);
    }

    //-----------------------------------------------------------------------
    /**
     * Prints a ReadablePartial.
     * <p>
     * Neither the override chronology nor the override zone are used
     * by this method.
     *
     * @param buf  formatted partial is appended to this buffer
     * @param partial  partial to format
     */
    public void printTo(StringBuffer buf, ReadablePartial partial) {
        checkPrinter();
        if (partial == null) {
            throw new IllegalArgumentException("The partial must not be null");
        }
        
        iPrinter.printTo(buf, partial, iLocale);
    }

    /**
     * Prints a ReadablePartial.
     * <p>
     * Neither the override chronology nor the override zone are used
     * by this method.
     *
     * @param out  formatted partial is written out
     * @param partial  partial to format
     */
    public void printTo(Writer out, ReadablePartial partial) throws IOException {
        checkPrinter();
        if (partial == null) {
            throw new IllegalArgumentException("The partial must not be null");
        }
        
        iPrinter.printTo(out, partial, iLocale);
    }

    //-----------------------------------------------------------------------
    /**
     * Prints a ReadableInstant to a String.
     * <p>
     * This method will use the override zone and the override chronololgy if
     * they are set. Otherwise it will use the chronology and zone of the instant.
     *
     * @param instant  instant to format, null means now
     * @return the printed result
     */
    public String print(ReadableInstant instant) {
        checkPrinter();
        
        StringBuffer buf = new StringBuffer(iPrinter.estimatePrintedLength());
        printTo(buf, instant);
        return buf.toString();
    }

    /**
     * Prints a millisecond instant to a String.
     * <p>
     * This method will use the override zone and the override chronololgy if
     * they are set. Otherwise it will use the ISO chronology and default zone.
     *
     * @param instant  millis since 1970-01-01T00:00:00Z
     * @return the printed result
     */
    public String print(long instant) {
        checkPrinter();
        
        StringBuffer buf = new StringBuffer(iPrinter.estimatePrintedLength());
        printTo(buf, instant);
        return buf.toString();
    }

    /**
     * Prints a ReadablePartial to a new String.
     * <p>
     * Neither the override chronology nor the override zone are used
     * by this method.
     *
     * @param partial  partial to format
     * @return the printed result
     */
    public String print(ReadablePartial partial) {
        checkPrinter();
        
        StringBuffer buf = new StringBuffer(iPrinter.estimatePrintedLength());
        printTo(buf, partial);
        return buf.toString();
    }

    private void printTo(StringBuffer buf, long instant, Chronology chrono) {
        chrono = selectChronology(chrono);
        // Shift instant into local time (UTC) to avoid excessive offset
        // calculations when printing multiple fields in a composite printer.
        DateTimeZone zone = chrono.getZone();
        int offset = zone.getOffset(instant);
        iPrinter.printTo(buf, instant + offset, chrono.withUTC(), offset, zone, iLocale);
    }

    private void printTo(Writer buf, long instant, Chronology chrono) throws IOException {
        chrono = selectChronology(chrono);
        // Shift instant into local time (UTC) to avoid excessive offset
        // calculations when printing multiple fields in a composite printer.
        DateTimeZone zone = chrono.getZone();
        int offset = zone.getOffset(instant);
        iPrinter.printTo(buf, instant + offset, chrono.withUTC(), offset, zone, iLocale);
    }

    /**
     * Method called when there is nothing to parse.
     * 
     * @return the exception
     */
    private void checkPrinter() {
        if (iPrinter == null) {
            throw new UnsupportedOperationException("Printing not supported");
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Parses a datetime from the given text, at the given position, saving the
     * result into the fields of the given ReadWritableInstant. If the parse
     * succeeds, the return value is the new text position. Note that the parse
     * may succeed without fully reading the text and in this case those fields
     * that were read will be set.
     * <p>
     * Only those fields present in the string will be changed in the specified
     * instant. All other fields will remain unaltered. Thus if the string only
     * contains a year and a month, then the day and time will be retained from
     * the input instant. If this is not the behaviour you want, then reset the
     * fields before calling this method, or use {@link #parseDateTime(String)}
     * or {@link #parseMutableDateTime(String)}.
     * <p>
     * If it fails, the return value is negative, but the instant may still be
     * modified. To determine the position where the parse failed, apply the
     * one's complement operator (~) on the return value.
     * <p>
     * The parse will use the chronology of the instant.
     *
     * @param instant  an instant that will be modified, not null
     * @param text  the text to parse
     * @param position  position to start parsing from
     * @return new position, negative value means parse failed -
     *  apply complement operator (~) to get position of failure
     * @throws UnsupportedOperationException if parsing is not supported
     * @throws IllegalArgumentException if the instant is null
     * @throws IllegalArgumentException if any field is out of range
     */
    public int parseInto(ReadWritableInstant instant, String text, int position) {
        checkParser();
        if (instant == null) {
            throw new IllegalArgumentException("Instant must not be null");
        }
        
        long instantMillis = instant.getMillis();
        Chronology chrono = instant.getChronology();
        long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);
        chrono = selectChronology(chrono);
        
        DateTimeParserBucket bucket = new DateTimeParserBucket(instantLocal, chrono, iLocale);
        int newPos = iParser.parseInto(bucket, text, position);
        instant.setMillis(bucket.computeMillis());
        if (iOffsetParsed && bucket.getZone() == null) {
            int parsedOffset = bucket.getOffset();
            DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
            chrono = chrono.withZone(parsedZone);
        }
        instant.setChronology(chrono);
        return newPos;
    }

    /**
     * Parses a datetime from the given text, returning the number of
     * milliseconds since the epoch, 1970-01-01T00:00:00Z.
     * <p>
     * The parse will use the ISO chronology, and the default time zone.
     * If the text contains a time zone string then that will be taken into account.
     *
     * @param text  text to parse
     * @return parsed value expressed in milliseconds since the epoch
     * @throws UnsupportedOperationException if parsing is not supported
     * @throws IllegalArgumentException if the text to parse is invalid
     */
    public long parseMillis(String text) {
        checkParser();
        
        Chronology chrono = selectChronology(iChrono);
        DateTimeParserBucket bucket = new DateTimeParserBucket(0, chrono, iLocale);
        int newPos = iParser.parseInto(bucket, text, 0);
        if (newPos >= 0) {
            if (newPos >= text.length()) {
                return bucket.computeMillis(true);
            }
        } else {
            newPos = ~newPos;
        }
        throw new IllegalArgumentException(FormatUtils.createErrorMessage(text, newPos));
    }

    /**
     * Parses a datetime from the given text, returning a new DateTime.
     * <p>
     * The parse will use the zone and chronology specified on this formatter.
     * <p>
     * If the text contains a time zone string then that will be taken into
     * account in adjusting the time of day as follows.
     * If the {@link #withOffsetParsed()} has been called, then the resulting
     * DateTime will have a fixed offset based on the parsed time zone.
     * Otherwise the resulting DateTime will have the zone of this formatter,
     * but the parsed zone may have caused the time to be adjusted.
     *
     * @param text  the text to parse
     * @return parsed value in a DateTime object
     * @throws UnsupportedOperationException if parsing is not supported
     * @throws IllegalArgumentException if the text to parse is invalid
     */
    public DateTime parseDateTime(String text) {
        checkParser();
        
        Chronology chrono = selectChronology(null);
        DateTimeParserBucket bucket = new DateTimeParserBucket(0, chrono, iLocale);
        int newPos = iParser.parseInto(bucket, text, 0);
        if (newPos >= 0) {
            if (newPos >= text.length()) {
                long millis = bucket.computeMillis(true);
                if (iOffsetParsed && bucket.getZone() == null) {
                    int parsedOffset = bucket.getOffset();
                    DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
                    chrono = chrono.withZone(parsedZone);
                }
                return new DateTime(millis, chrono);
            }
        } else {
            newPos = ~newPos;
        }
        throw new IllegalArgumentException(FormatUtils.createErrorMessage(text, newPos));
    }

    /**
     * Parses a datetime from the given text, returning a new MutableDateTime.
     * <p>
     * The parse will use the zone and chronology specified on this formatter.
     * <p>
     * If the text contains a time zone string then that will be taken into
     * account in adjusting the time of day as follows.
     * If the {@link #withOffsetParsed()} has been called, then the resulting
     * DateTime will have a fixed offset based on the parsed time zone.
     * Otherwise the resulting DateTime will have the zone of this formatter,
     * but the parsed zone may have caused the time to be adjusted.
     *
     * @param text  the text to parse
     * @return parsed value in a MutableDateTime object
     * @throws UnsupportedOperationException if parsing is not supported
     * @throws IllegalArgumentException if the text to parse is invalid
     */
    public MutableDateTime parseMutableDateTime(String text) {
        checkParser();
        
        Chronology chrono = selectChronology(null);
        DateTimeParserBucket bucket = new DateTimeParserBucket(0, chrono, iLocale);
        int newPos = iParser.parseInto(bucket, text, 0);
        if (newPos >= 0) {
            if (newPos >= text.length()) {
                long millis = bucket.computeMillis(true);
                if (iOffsetParsed && bucket.getZone() == null) {
                    int parsedOffset = bucket.getOffset();
                    DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
                    chrono = chrono.withZone(parsedZone);
                }
                return new MutableDateTime(millis, chrono);
            }
        } else {
            newPos = ~newPos;
        }
        throw new IllegalArgumentException(FormatUtils.createErrorMessage(text, newPos));
    }

    /**
     * Method called when there is nothing to parse.
     * 
     * @return the exception
     */
    private void checkParser() {
        if (iParser == null) {
            throw new UnsupportedOperationException("Parsing not supported");
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Determines the correct chronology to use.
     *
     * @param chrono  the proposed chronology
     * @return the actual chronology
     */
    private Chronology selectChronology(Chronology chrono) {
        chrono = DateTimeUtils.getChronology(chrono);
        if (iChrono != null) {
            chrono = iChrono;
        }
        if (iZone != null) {
            chrono = chrono.withZone(iZone);
        }
        return chrono;
    }

}
