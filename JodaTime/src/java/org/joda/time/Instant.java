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

import org.joda.time.base.AbstractInstant;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.InstantConverter;

/**
 * Instant is the standard implementation of a fully immutable instant in time.
 * It holds the instant as milliseconds from the Java Epoch of 1970-01-01T00:00:00Z.
 * <p>
 * The chronology used is always ISO in the UTC time zone.
 * This corresponds to the definition of the Java Epoch.
 * <p>
 * An Instant can be used to compare two <code>DateTime</code> objects:
 * <pre>
 * boolean sameInstant = dt1.toInstant().equals(dt2.toInstant());
 * </pre>
 * This code will return true if the two <code>DateTime</code> objects represent
 * the same instant regardless of chronology or time zone.
 * <p>
 * Note that the following code will also perform the same check:
 * <pre>
 * boolean sameInstant = dt1.isEqual(dt2);
 * </pre>
 * <p>
 * Instant is thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public final class Instant
        extends AbstractInstant
        implements ReadableInstant, Serializable {

    /** Serialization lock */
    private static final long serialVersionUID = 3299096530934209741L;

    /** The millis from 1970-01-01T00:00:00Z */
    private final long iMillis;

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance set to the current system millisecond time.
     */
    public Instant() {
        super();
        iMillis = DateTimeUtils.currentTimeMillis();
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
     * Constructs an instance from an Object that represents a datetime.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include String, Calendar and Date.
     *
     * @param instant  the datetime object, null means now
     * @throws IllegalArgumentException if the instant is invalid
     */
    public Instant(Object instant) {
        super();
        InstantConverter converter = ConverterManager.getInstance().getInstantConverter(instant);
        iMillis = converter.getInstantMillis(instant, Chronology.getISOUTC());
    }

    //-----------------------------------------------------------------------
    /**
     * Get this object as an Instant by returning <code>this</code>.
     * 
     * @return <code>this</code>
     */
    public Instant toInstant() {
        return this;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this instant with different millis.
     * <p>
     * The returned object will be either be a new Instant or <code>this</code>.
     *
     * @param newMillis  the new millis, from 1970-01-01T00:00:00Z
     * @return a copy of this instant with different millis
     */
    public Instant withMillis(long newMillis) {
        return (newMillis == iMillis ? this : new Instant(newMillis));
    }

    /**
     * Gets a copy of this instant with the specified duration added.
     * <p>
     * If the addition is zero, then <code>this</code> is returned.
     * 
     * @param durationToAdd  the duration to add to this one
     * @param scalar  the amount of times to add, such as -1 to subtract once
     * @return a copy of this instant with the duration added
     * @throws ArithmeticException if the new instant exceeds the capacity of a long
     */
    public Instant withDurationAdded(long durationToAdd, int scalar) {
        if (durationToAdd == 0 || scalar == 0) {
            return this;
        }
        long instant = getChronology().add(getMillis(), durationToAdd, scalar);
        return withMillis(instant);
    }

    /**
     * Gets a copy of this instant with the specified duration added.
     * <p>
     * If the addition is zero, then <code>this</code> is returned.
     * 
     * @param durationToAdd  the duration to add to this one, null means zero
     * @param scalar  the amount of times to add, such as -1 to subtract once
     * @return a copy of this instant with the duration added
     * @throws ArithmeticException if the new instant exceeds the capacity of a long
     */
    public Instant withDurationAdded(ReadableDuration durationToAdd, int scalar) {
        if (durationToAdd == null || scalar == 0) {
            return this;
        }
        return withDurationAdded(durationToAdd.getMillis(), scalar);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this instant with the specified duration added.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * 
     * @param duration  the duration to add to this one
     * @return a copy of this instant with the duration added
     * @throws ArithmeticException if the new instant exceeds the capacity of a long
     */
    public Instant plus(long duration) {
        return withDurationAdded(duration, 1);
    }

    /**
     * Gets a copy of this instant with the specified duration added.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * 
     * @param durationToAdd  the duration to add to this one, null means zero
     * @return a copy of this instant with the duration added
     * @throws ArithmeticException if the new instant exceeds the capacity of a long
     */
    public Instant plus(ReadableDuration duration) {
        return withDurationAdded(duration, 1);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this instant with the specified duration taken away.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * 
     * @param duration  the duration to reduce this instant by
     * @return a copy of this instant with the duration taken away
     * @throws ArithmeticException if the new instant exceeds the capacity of a long
     */
    public Instant minus(long duration) {
        return withDurationAdded(duration, -1);
    }

    /**
     * Gets a copy of this instant with the specified duration taken away.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * 
     * @param duration  the duration to reduce this instant by
     * @return a copy of this instant with the duration taken away
     * @throws ArithmeticException if the new instant exceeds the capacity of a long
     */
    public Instant minus(ReadableDuration durationToAdd) {
        return withDurationAdded(durationToAdd, -1);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the milliseconds of the instant.
     * 
     * @return the number of milliseconds since 1970-01-01T00:00:00Z
     */
    public long getMillis() {
        return iMillis;
    }

    /**
     * Gets the chronology of the instant, which is ISO in the UTC zone.
     * 
     * @return ISO in the UTC zone
     */
    public Chronology getChronology() {
        return Chronology.getISOUTC();
    }

}
