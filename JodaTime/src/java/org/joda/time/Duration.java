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

import org.joda.time.base.BaseDuration;
import org.joda.time.field.FieldUtils;

/**
 * An immutable duration specifying a length of time in milliseconds.
 * <p>
 * A duration is defined by a fixed number of milliseconds.
 * There is no concept of fields, such as days or seconds, as these fields can vary in length.
 * A duration may be converted to a {@link Period} to obtain field values.
 * This conversion will typically cause a loss of precision however.
 * <p>
 * Duration is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public final class Duration
        extends BaseDuration
        implements ReadableDuration, Serializable {

    /** Constant representing zero millisecond duration */
    public static final Duration ZERO = new Duration(0L);

    /** Serialization version */
    private static final long serialVersionUID = 2471658376918L;

    /**
     * Creates a duration from the given millisecond duration.
     *
     * @param duration  the duration, in milliseconds
     */
    public Duration(long duration) {
        super(duration);
    }

    /**
     * Creates a duration from the given interval endpoints.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     * @throws ArithmeticException if the duration exceeds a 64 bit long
     */
    public Duration(long startInstant, long endInstant) {
        super(startInstant, endInstant);
    }

    /**
     * Creates a duration from the given interval endpoints.
     *
     * @param start  interval start, null means now
     * @param end  interval end, null means now
     * @throws ArithmeticException if the duration exceeds a 64 bit long
     */
    public Duration(ReadableInstant start, ReadableInstant end) {
        super(start, end);
    }

    /**
     * Creates a duration from the specified object using the
     * {@link org.joda.time.convert.ConverterManager ConverterManager}.
     *
     * @param duration  duration to convert
     * @throws IllegalArgumentException if duration is invalid
     */
    public Duration(Object duration) {
        super(duration);
    }

    //-----------------------------------------------------------------------
    /**
     * Get this duration as an immutable <code>Duration</code> object
     * by returning <code>this</code>.
     * 
     * @return <code>this</code>
     */
    public Duration toDuration() {
        return this;
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a new Duration instance with a different milisecond length.
     * 
     * @param duration  the new length of the duration
     * @return the new duration instance
     */
    public Duration withMillis(long duration) {
        if (duration == getMillis()) {
            return this;
        }
        return new Duration(duration);
    }

    /**
     * Adds to this duration returning a new Duration instance.
     * <p>
     * If the addition is zero, this instance is returned.
     * 
     * @param durationToAdd  the duration to add to this one
     * @return the new duration instance
     */
    public Duration withDurationAdded(long durationToAdd) {
        return withDurationAdded(durationToAdd, 1);
    }

    /**
     * Adds to this duration returning a new Duration instance.
     * <p>
     * If the addition is zero, this instance is returned.
     * 
     * @param durationToAdd  the duration to add to this one
     * @param scalar  the amount of times to add, such as -1 to subtract once
     * @return the new duration instance
     */
    public Duration withDurationAdded(long durationToAdd, int scalar) {
        if (durationToAdd == 0 || scalar == 0) {
            return this;
        }
        long add = FieldUtils.safeMultiply(durationToAdd, scalar);
        long duration = FieldUtils.safeAdd(getMillis(), add);
        return new Duration(duration);
    }

    /**
     * Adds to this duration returning a new Duration instance.
     * <p>
     * If the addition is zero, this instance is returned.
     * 
     * @param durationToAdd  the duration to add to this one, null means zero
     * @return the new duration instance
     */
    public Duration withDurationAdded(ReadableDuration durationToAdd) {
        if (durationToAdd == null) {
            return this;
        }
        return withDurationAdded(durationToAdd.getMillis(), 1);
    }

    /**
     * Adds to this duration returning a new Duration instance.
     * <p>
     * If the addition is zero, this instance is returned.
     * 
     * @param durationToAdd  the duration to add to this one, null means zero
     * @param scalar  the amount of times to add, such as -1 to subtract once
     * @return the new duration instance
     */
    public Duration withDurationAdded(ReadableDuration durationToAdd, int scalar) {
        if (durationToAdd == null || scalar == 0) {
            return this;
        }
        return withDurationAdded(durationToAdd.getMillis(), scalar);
    }

}
