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
package org.joda.time;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.DateTimeParser;

/**
 * Instant is the standard implementation of a fully immutable instant in 
 * time. It holds the instant as milliseconds from the Java Epoch of 
 * 1970-01-01T00:00:00Z.
 * <p>
 * There is no concept of a calendar system, chronology or time zone. In 
 * a fully internationalized program, methods should accept the ReadableInstant 
 * interface as input and return Instant objects.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public final class Instant extends AbstractInstant implements ReadableInstant {

    /** The millis from 1970-01-01T00:00:00Z */
    private final long iMillis;

    // Constructors
    //-----------------------------------------------------------------------
    /**
     * Constructor that sets the time to be the current time from the
     * system clock.
     */
    public Instant() {
        super();
        iMillis = System.currentTimeMillis();
    }

    /**
     * Constructor that takes milliseconds from 1970-01-01T00:00:00Z.
     * 
     * @param millis  the milliseconds
     */
    public Instant(long millis) {
        super();
        iMillis = millis;
    }

    /**
     * Constructor that takes a ReadableInstant.
     * 
     * @param instant  the ReadableInstant
     * @throws IllegalArgumentException if the instant is null
     */
    public Instant(ReadableInstant instant) {
        super();
        if (instant == null) {
            throw new IllegalArgumentException("The ReadableInstant must not be null");
        }
        iMillis = instant.getMillis();
    }

    /**
     * Constructor that takes a Date.
     * 
     * @param date  the Date
     * @throws IllegalArgumentException if the date is null
     */
    public Instant(Date date) {
        super();
        if (date == null) {
            throw new IllegalArgumentException("The Date must not be null");
        }
        iMillis = date.getTime();
    }

    /**
     * Constructor that takes a Calendar.
     * 
     * @param calendar  the Calendar
     * @throws IllegalArgumentException if the calendar is null
     */
    public Instant(Calendar calendar) {
        super();
        if (calendar == null) {
            throw new IllegalArgumentException("The Calendar must not be null");
        }
        iMillis = calendar.getTime().getTime();
    }

    /**
     * Constructor that parses an ISO formatted string.
     * 
     * @param str  the string
     * @throws IllegalArgumentException if the string is null
     * @throws ParseException if the string is incorrectly formatted
     */
    public Instant(String str) throws ParseException {
        super();
        if (str == null) {
            throw new IllegalArgumentException("The String must not be null");
        }
        DateTimeParser p = ISODateTimeFormat.getInstanceUTC().dateTimeParser();
        iMillis = p.parseMillis(str);
    }

    /**
     * Creates a new instance of this class.
     * <p>
     * The returned object will be a new instance of the implementation.
     * Immutable subclasses may return <code>this</code> if appropriate.
     *
     * @param millis  the new millis, from 1970-01-01T00:00:00Z
     * @param chrono  the new chronology
     * @return a new instance of this class
     * @throws IllegalArgumentException if the chronology is null
     */
    protected ReadableInstant create(long millis, Chronology chrono) {
        // ignore chrono
        if (millis == getMillis()) {
            return this;
        }
        return new Instant(millis);
    }
    
    // Accessors
    //-----------------------------------------------------------------------
    /**
     * Gets the milliseconds of the instant.
     * 
     * @return the number of milliseconds since 1970-01-01T00:00:00Z
     */
    public final long getMillis() {
        return iMillis;
    }

    /**
     * Gets the chronology of the instant, which is null.
     * <p>
     * The {@link Chronology} provides conversion from the millisecond
     * value to meaningful fields in a particular calendar system. This
     * class represents a chronology free view of time, so this method
     * returns null.
     * 
     * @return null
     */
    public final Chronology getChronology() {
        return null;
    }

    // Output
    //-----------------------------------------------------------------------
    /**
     * Output the date time in ISO8601 format.
     * <p>
     * ISO8601 is deliberately used here so that the resulting string can be
     * re-parsed by the constructor.
     * 
     * @return ISO8601 date formatted string
     */
    public final String toString() {
        return ISODateTimeFormat.getInstanceUTC().dateTime().print(this);
    }

}
