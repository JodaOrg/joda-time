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

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadWritableInstant;
import org.joda.time.ReadableInstant;

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
 * AbstractDateTimeFormatter is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public abstract class AbstractDateTimeFormatter {

    // Accessed also by AbstractDurationFormatter.
    static String createErrorMessage(final String text, final int errorPos) {
        int sampleLen = errorPos + 20;
        String sampleText;
        if (text.length() <= sampleLen) {
            sampleText = text;
        } else {
            sampleText = text.substring(0, sampleLen).concat("...");
        }
        
        if (errorPos <= 0) {
            return "Invalid format: \"" + sampleText + '"';
        }
        
        if (errorPos >= text.length()) {
            return "Invalid format: \"" + sampleText + "\" is too short";
        }
        
        return "Invalid format: \"" + sampleText + "\" is malformed at \"" +
            sampleText.substring(errorPos) + '"';
    }

    /**
     * Returns the Chronology being used by the formatter, or null if none.
     */
    public abstract Chronology getChronology();

    /**
     * Returns the DateTimeZone from the formatter's Chronology, defaulting to
     * UTC if the Chronology or its DateTimeZone is null.
     */
    public DateTimeZone getDateTimeZone() {
        Chronology chrono = getChronology();
        if (chrono == null) {
            return DateTimeZone.UTC;
        }
        DateTimeZone zone = chrono.getDateTimeZone();
        return zone == null ? DateTimeZone.UTC : zone;
    }

    public void printTo(final StringBuffer buf, final ReadableInstant instant) {
        long millisUTC = instant.getMillis();
        Chronology chrono;
        if ((chrono = instant.getChronology()) != null) {
            printTo(buf, millisUTC, chrono.getDateTimeZone());
        } else {
            ((DateTimePrinter)this).printTo(buf, millisUTC, null);
        }
    }

    public void printTo(final Writer out, final ReadableInstant instant) throws IOException {
        long millisUTC = instant.getMillis();
        Chronology chrono;
        if ((chrono = instant.getChronology()) != null) {
            printTo(out, millisUTC, chrono.getDateTimeZone());
        } else {
            ((DateTimePrinter)this).printTo(out, millisUTC, null);
        }
    }

    public void printTo(final StringBuffer buf, final long instant) {
        printTo(buf, instant, null);
    }

    public void printTo(final Writer out, final long instant) throws IOException {
        printTo(out, instant, null);
    }

    public void printTo(final StringBuffer buf, final long instant, DateTimeZone zone) {
        if (zone == null) {
            zone = getDateTimeZone();
        }
        ((DateTimePrinter) this).printTo
            (buf, instant, zone, instant + zone.getOffset(instant));
    }

    public void printTo(final Writer out, final long instant, DateTimeZone zone) throws IOException {
        if (zone == null) {
            zone = getDateTimeZone();
        }
        ((DateTimePrinter) this).printTo
            (out, instant, zone, instant + zone.getOffset(instant));
    }

    public String print(final ReadableInstant instant) {
        long millisUTC = instant.getMillis();
        Chronology chrono;
        if ((chrono = instant.getChronology()) != null) {
            return print(millisUTC, chrono.getDateTimeZone());
        } else {
            return print(millisUTC, null);
        }
    }

    public String print(final long instant) {
        return print(instant, null);
    }

    public String print(final long instant, DateTimeZone zone) {
        if (zone == null) {
            zone = getDateTimeZone();
        }
        return print(instant, zone, instant + zone.getOffset(instant));
    }

    public String print(final long instant, final DateTimeZone zone, final long instantLocal) {
        DateTimePrinter p = (DateTimePrinter)this;
        StringBuffer buf = new StringBuffer(p.estimatePrintedLength());
        p.printTo(buf, instant, zone, instantLocal);
        return buf.toString();
    }

    public int parseInto(final ReadWritableInstant instant, final String text, final int position) {
        DateTimeParser p = (DateTimeParser)this;

        long millis = instant.getMillis();
        Chronology chrono = instant.getChronology();
        if (chrono != null) {
            DateTimeZone zone = chrono.getDateTimeZone();
            if (zone != null) {
                // Move millis to local time.
                millis += zone.getOffset(millis);
            }
        }

        DateTimeParserBucket bucket = createBucket(millis);
        int resultPos = p.parseInto(bucket, text, position);
        instant.setMillis(bucket.computeMillis());
        return resultPos;
    }
    
    public long parseMillis(final String text) {
        return parseMillis(text, 0);
    }

    public long parseMillis(final String text, final long instantLocal) {
        DateTimeParser p = (DateTimeParser)this;
        DateTimeParserBucket bucket = createBucket(instantLocal);

        int newPos = p.parseInto(bucket, text, 0);
        if (newPos >= 0) {
            if (newPos >= text.length()) {
                return bucket.computeMillis();
            }
        } else {
            newPos = ~newPos;
        }

        throw new IllegalArgumentException(createErrorMessage(text, newPos));
    }

    public DateTime parseDateTime(final String text) {
        return new DateTime(parseMillis(text), getChronology());
    }

    public DateTime parseDateTime(final String text, final ReadableInstant instant) {
        return new DateTime(parseMillis(text, getInstantLocal(instant)), getChronology());
    }

    public MutableDateTime parseMutableDateTime(final String text) {
        return new MutableDateTime(parseMillis(text), getChronology());
    }

    public MutableDateTime parseMutableDateTime(final String text, final ReadableInstant instant) {
        return new MutableDateTime(parseMillis(text, getInstantLocal(instant)), getChronology());
    }

    private long getInstantLocal(ReadableInstant instant) {
        long instantLocal;
        if (instant == null) {
            instantLocal = 0;
        } else {
            instantLocal = instant.getMillis();
            DateTimeZone zone = instant.getDateTimeZone();
            if (zone != null) {
                instantLocal += zone.getOffset(instantLocal);
            }
        }
        return instantLocal;
    }

    private DateTimeParserBucket createBucket(final long millis) {
        DateTimeParserBucket bucket = new DateTimeParserBucket(millis);
        Chronology chrono = getChronology();
        if (chrono != null) {
            DateTimeZone zone = chrono.getDateTimeZone();
            if (zone != null) {
                bucket.setDateTimeZone(zone);
            }
        }
        return bucket;
    }

}
