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
package org.joda.time;

import java.io.Serializable;

import org.joda.time.convert.InstantConverter;
import org.joda.time.convert.ConverterManager;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Instant is the standard implementation of a fully immutable instant in time.
 * It holds the instant as milliseconds from the Java Epoch of 1970-01-01T00:00:00Z.
 * <p>
 * There is no concept of a calendar system, chronology or time zone.
 * In a fully internationalized program, you may want to ensure methods accept the
 * ReadableInstant interface as input and return Instant objects.
 * <p>
 * Instant is thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public final class Instant extends AbstractInstant
        implements ReadableInstant, Serializable {

    static final long serialVersionUID = 3299096530934209741L;

    /** The millis from 1970-01-01T00:00:00Z */
    private final long iMillis;

    // Constructors
    //-----------------------------------------------------------------------
    /**
     * Constructs an instance set to the current system millisecond time.
     */
    public Instant() {
        super();
        iMillis = System.currentTimeMillis();
    }

    /**
     * Constructs an instance set to the milliseconds from 1970-01-01T00:00:00Z.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     */
    public Instant(long instant) {
        super();
        iMillis = instant;
    }

    /**
     * Constructs an instance from a <code>ReadableInstant</code>.
     * 
     * @param instant  the ReadableInstant
     * @throws IllegalArgumentException if the instant is null
     */
    public Instant(ReadableInstant instant) {
        super();
        iMillis = instant.getMillis();
    }

    /**
     * Constructs an instance from an Object that represents a datetime.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include String, Calendar and Date.
     *
     * @param instant  the datetime object, must not be null
     * @throws IllegalArgumentException if the instant is null or invalid
     */
    public Instant(Object instant) {
        super();
        InstantConverter converter = ConverterManager.getInstance().getInstantConverter(instant);
        iMillis = converter.getInstantMillis(instant);
    }

    /**
     * Gets a copy of this instant with different millis, as an Instant.
     * <p>
     * The returned object will be either be a new Instant or
     * <code>this</code>.
     *
     * @param newMillis  the new millis, from 1970-01-01T00:00:00Z
     * @return a copy of this instant with different millis
     */
    public ReadableInstant toCopy(long newMillis) {
        return newMillis == iMillis ? this : new Instant(newMillis);
    }
    
    /**
     * Since Instant does not support chronologies, this method always returns
     * <code>this</code>.
     *
     * @param newChronology  ignored
     * @return this
     */
    public ReadableInstant toCopy(Chronology newChronology) {
        return this;
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
     * Gets the milliseconds of the instant.
     *
     * @param base ignored
     * @return the number of milliseconds since 1970-01-01T00:00:00Z
     */
    public final long getMillis(ReadableInstant base) {
        return iMillis;
    }

    /**
     * Gets the milliseconds of the instant.
     *
     * @param base ignored
     * @param zone ignored
     * @return the number of milliseconds since 1970-01-01T00:00:00Z
     */
    public final long getMillis(ReadableInstant base, DateTimeZone zone) {
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
