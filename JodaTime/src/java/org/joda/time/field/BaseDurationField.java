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
package org.joda.time.field;

import java.io.Serializable;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

/**
 * BaseDurationField provides the common behaviour for DurationField
 * implementations.
 * <p>
 * This class should generally not be used directly by API users. The
 * DurationField class should be used when different kinds of DurationField
 * objects are to be referenced.
 * <p>
 * BaseDurationField is thread-safe and immutable, and its subclasses must
 * be as well.
 *
 * @author Brian S O'Neill
 * @see DecoratedDurationField
 * @since 1.0
 */
public abstract class BaseDurationField extends DurationField implements Serializable {

    /** Serialization lock. */
    private static final long serialVersionUID = -2554245107589433218L;

    /** A desriptive name for the field. */
    private final DurationFieldType iType;

    protected BaseDurationField(DurationFieldType type) {
        super();
        if (type == null) {
            throw new IllegalArgumentException("The type must not be null");
        }
        iType = type;
    }

    public final DurationFieldType getType() {
        return iType;
    }

    public final String getName() {
        return iType.getName();
    }

    /**
     * @return true always
     */
    public final boolean isSupported() {
        return true;
    }

    //------------------------------------------------------------------------
    /**
     * Get the value of this field from the milliseconds, which is approximate
     * if this field is imprecise.
     *
     * @param duration  the milliseconds to query, which may be negative
     * @return the value of the field, in the units of the field, which may be
     * negative
     */
    public int getValue(long duration) {
        return FieldUtils.safeToInt(getValueAsLong(duration));
    }

    /**
     * Get the value of this field from the milliseconds, which is approximate
     * if this field is imprecise.
     *
     * @param duration  the milliseconds to query, which may be negative
     * @return the value of the field, in the units of the field, which may be
     * negative
     */
    public long getValueAsLong(long duration) {
        return duration / getUnitMillis();
    }

    /**
     * Get the value of this field from the milliseconds relative to an
     * instant.
     *
     * <p>If the milliseconds is positive, then the instant is treated as a
     * "start instant". If negative, the instant is treated as an "end
     * instant".
     *
     * <p>The default implementation returns
     * <code>Utils.safeToInt(getAsLong(millisDuration, instant))</code>.
     * 
     * @param duration  the milliseconds to query, which may be negative
     * @param instant  the start instant to calculate relative to
     * @return the value of the field, in the units of the field, which may be
     * negative
     */
    public int getValue(long duration, long instant) {
        return FieldUtils.safeToInt(getValueAsLong(duration, instant));
    }

    /**
     * Get the millisecond duration of this field from its value, which is
     * approximate if this field is imprecise.
     * 
     * @param value  the value of the field, which may be negative
     * @return the milliseconds that the field represents, which may be
     * negative
     */
    public long getMillis(int value) {
        return value * getUnitMillis();  // safe
    }

    /**
     * Get the millisecond duration of this field from its value, which is
     * approximate if this field is imprecise.
     * 
     * @param value  the value of the field, which may be negative
     * @return the milliseconds that the field represents, which may be
     * negative
     */
    public long getMillis(long value) {
        return FieldUtils.safeMultiply(value, getUnitMillis());
    }

    // Calculation API
    //------------------------------------------------------------------------
    public int getDifference(long minuendInstant, long subtrahendInstant) {
        return FieldUtils.safeToInt(getDifferenceAsLong(minuendInstant, subtrahendInstant));
    }

    //------------------------------------------------------------------------
    public int compareTo(Object durationField) {
        DurationField otherField = (DurationField) durationField;
        long otherMillis = otherField.getUnitMillis();
        long thisMillis = getUnitMillis();
        // cannot do (thisMillis - otherMillis) as can overflow
        if (thisMillis == otherMillis) {
            return 0;
        }
        if (thisMillis < otherMillis) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * Get a suitable debug string.
     * 
     * @return debug string
     */
    public String toString() {
        return "DurationField[" + getName() + ']';
    }

}
