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

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadWritableInstant;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;

/**
 * Abstract base class for implementing {@link DateTimePrinter}s,
 * {@link DateTimeParser}s, and {@link DateTimeFormatter}s. This class
 * intentionally does not implement any of those interfaces. You can subclass
 * and implement only the interfaces that you need to.
 * <p>
 * The print methods assume that your subclass has implemented DateTimePrinter or
 * DateTimeFormatter. If not, a ClassCastException is thrown when calling those
 * methods.
 * <p>
 * Likewise, the parse methods assume that your subclass has implemented
 * DateTimeParser or DateTimeFormatter. If not, a ClassCastException is thrown
 * when calling the parse methods.
 * <p>
 * BaseDateTimeFormatter is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public abstract class BaseDateTimeFormatter {

    // Since this class does not implement any interfaces, perform manual
    // virtual binding on all delegating calls. That is, if printTo calls
    // printTo with different arguments, it will bind to this class instead of
    // a virtual method on the DateTimePrinter interface. To accomplish this,
    // cast this to proper interface class first.

    public void printTo(StringBuffer buf, ReadableInstant instant) {
        long millis = DateTimeUtils.getInstantMillis(instant);
        Chronology chrono = DateTimeUtils.getInstantChronology(instant);
        ((DateTimePrinter) this).printTo(buf, millis, chrono);
    }

    public void printTo(Writer out, ReadableInstant instant) throws IOException {
        long millis = DateTimeUtils.getInstantMillis(instant);
        Chronology chrono = DateTimeUtils.getInstantChronology(instant);
        ((DateTimePrinter) this).printTo(out, millis, chrono);
    }

    public void printTo(StringBuffer buf, long instant) {
        ((DateTimePrinter) this).printTo(buf, instant, ISOChronology.getInstance());
    }

    public void printTo(Writer out, long instant) throws IOException {
        ((DateTimePrinter) this).printTo(out, instant, ISOChronology.getInstance());
    }

    public void printTo(StringBuffer buf, long instant, DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        ((DateTimePrinter) this).printTo(buf, instant, ISOChronology.getInstance(zone));
    }

    public void printTo(Writer out, long instant, DateTimeZone zone) throws IOException {
        zone = DateTimeUtils.getZone(zone);
        ((DateTimePrinter) this).printTo(out, instant, ISOChronology.getInstance(zone));
    }

    public void printTo(StringBuffer buf, long instant, Chronology chrono) {
        chrono = DateTimeUtils.getChronology(chrono);
        // Shift instant into local time (UTC) to avoid excessive offset
        // calculations when printing multiple fields in a composite printer.
        DateTimeZone zone = chrono.getZone();
        int offset = zone.getOffset(instant);
        ((DateTimePrinter) this).printTo(buf, instant + offset, chrono.withUTC(), offset, zone);
    }

    public void printTo(Writer out, long instant, Chronology chrono) throws IOException {
        chrono = DateTimeUtils.getChronology(chrono);
        // Shift instant into local time (UTC) to avoid excessive offset
        // calculations when printing multiple fields in a composite printer.
        DateTimeZone zone = chrono.getZone();
        int offset = zone.getOffset(instant);
        ((DateTimePrinter) this).printTo(out, instant + offset, chrono.withUTC(), offset, zone);
    }

    public String print(ReadableInstant instant) {
        long millis = DateTimeUtils.getInstantMillis(instant);
        Chronology chrono = DateTimeUtils.getInstantChronology(instant);
        return ((DateTimePrinter) this).print(millis, chrono);
    }

    public String print(long instant) {
        return ((DateTimePrinter) this).print(instant, ISOChronology.getInstance());
    }

    public String print(long instant, DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        return ((DateTimePrinter) this).print(instant, ISOChronology.getInstance(zone));
    }

    public String print(long instant, Chronology chrono) {
        chrono = DateTimeUtils.getChronology(chrono);
        // Shift instant into local time (UTC) to avoid excessive offset
        // calculations when printing multiple fields in a composite printer.
        DateTimeZone zone = chrono.getZone();
        int offset = zone.getOffset(instant);
        return ((DateTimePrinter) this).print(instant + offset, chrono.withUTC(), offset, zone);
    }

    public String print(long instant, Chronology chrono,
                        int displayOffset, DateTimeZone displayZone) {
        DateTimePrinter printer = (DateTimePrinter) this;
        StringBuffer buf = new StringBuffer(printer.estimatePrintedLength());
        printer.printTo(buf, instant, chrono, displayOffset, displayZone);
        return buf.toString();
    }

    public String print(ReadablePartial partial) {
        DateTimePrinter printer = (DateTimePrinter) this;
        StringBuffer buf = new StringBuffer(printer.estimatePrintedLength());
        printer.printTo(buf, partial);
        return buf.toString();
    }

    //-----------------------------------------------------------------------
    public int parseInto(ReadWritableInstant instant, String text, int position) {
        if (instant == null) {
            throw new IllegalArgumentException("Instant must not be null");
        }

        long millis = instant.getMillis();
        Chronology chrono = instant.getChronology();
        long instantLocal = millis + chrono.getZone().getOffset(millis);

        DateTimeParserBucket bucket = new DateTimeParserBucket(instantLocal, chrono);
        int resultPos = ((DateTimeParser) this).parseInto(bucket, text, position);
        instant.setMillis(bucket.computeMillis());
        return resultPos;
    }

    public long parseMillis(String text) {
        return parseMillis(text, ISOChronology.getInstance());
    }

    public long parseMillis(String text, Chronology chrono) {
        DateTimeParserBucket bucket = new DateTimeParserBucket(0, chrono);

        int newPos = ((DateTimeParser) this).parseInto(bucket, text, 0);
        if (newPos >= 0) {
            if (newPos >= text.length()) {
                return bucket.computeMillis(true);
            }
        } else {
            newPos = ~newPos;
        }

        throw new IllegalArgumentException(FormatUtils.createErrorMessage(text, newPos));
    }

    public long parseMillis(String text, long instant) {
        return ((DateTimeParser) this).parseMillis(text, instant, ISOChronology.getInstance());
    }

    public long parseMillis(String text, long instant, Chronology chrono) {
        chrono = DateTimeUtils.getChronology(chrono);
        long instantLocal = instant + chrono.getZone().getOffset(instant);
        DateTimeParserBucket bucket = new DateTimeParserBucket(instantLocal, chrono);

        int newPos = ((DateTimeParser) this).parseInto(bucket, text, 0);
        if (newPos >= 0) {
            if (newPos >= text.length()) {
                return bucket.computeMillis();
            }
        } else {
            newPos = ~newPos;
        }

        throw new IllegalArgumentException(FormatUtils.createErrorMessage(text, newPos));
    }

    public DateTime parseDateTime(String text) {
        return ((DateTimeParser) this).parseDateTime(text, ISOChronology.getInstance());
    }

    public DateTime parseDateTime(String text, Chronology chrono) {
        return new DateTime(((DateTimeParser) this).parseMillis(text, chrono), chrono);
    }

    public DateTime parseDateTime(String text, ReadableInstant instant) {
        Chronology chrono = DateTimeUtils.getInstantChronology(instant);
        long millis = DateTimeUtils.getInstantMillis(instant);
        return new DateTime(((DateTimeParser) this).parseMillis(text, millis, chrono), chrono);
    }

    public MutableDateTime parseMutableDateTime(String text) {
        return ((DateTimeParser) this).parseMutableDateTime(text, ISOChronology.getInstance());
    }

    public MutableDateTime parseMutableDateTime(String text, Chronology chrono) {
        return new MutableDateTime(((DateTimeParser) this).parseMillis(text, chrono), chrono);
    }

    public MutableDateTime parseMutableDateTime(String text, ReadableInstant instant) {
        Chronology chrono = DateTimeUtils.getInstantChronology(instant);
        long millis = DateTimeUtils.getInstantMillis(instant);
        return new MutableDateTime
            (((DateTimeParser) this).parseMillis(text, millis, chrono), chrono);
    }
}
