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

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

/**
 * Scales a DurationField such that it's unit millis becomes larger in
 * magnitude.
 * <p>
 * ScaledDurationField is thread-safe and immutable.
 *
 * @see PreciseDurationField
 *
 * @author Brian S O'Neill
 */
public class ScaledDurationField extends DecoratedDurationField {

    private static final long serialVersionUID = -3205227092378684157L;

    private final int iScalar;

    /**
     * Constructor
     * 
     * @param field  the field to wrap, like "year()".
     * @param type  the type this field will actually use
     * @param scalar  scalar, such as 100 years in a century
     * @throws IllegalArgumentException if scalar is zero or one.
     */
    public ScaledDurationField(DurationField field, DurationFieldType type, int scalar) {
        super(field, type);
        if (scalar == 0 || scalar == 1) {
            throw new IllegalArgumentException("The scalar must not be 0 or 1");
        }
        iScalar = scalar;
    }

    public int getValue(long duration) {
        return getWrappedField().getValue(duration) / iScalar;
    }

    public long getValueAsLong(long duration) {
        return getWrappedField().getValueAsLong(duration) / iScalar;
    }

    public int getValue(long duration, long instant) {
        return getWrappedField().getValue(duration, instant) / iScalar;
    }

    public long getValueAsLong(long duration, long instant) {
        return getWrappedField().getValueAsLong(duration, instant) / iScalar;
    }

    public long getMillis(int value) {
        long scaled = ((long) value) * ((long) iScalar);
        return getWrappedField().getMillis(scaled);
    }

    public long getMillis(long value) {
        long scaled = FieldUtils.safeMultiply(value, iScalar);
        return getWrappedField().getMillis(scaled);
    }

    public long getMillis(int value, long instant) {
        long scaled = ((long) value) * ((long) iScalar);
        return getWrappedField().getMillis(scaled, instant);
    }

    public long getMillis(long value, long instant) {
        long scaled = FieldUtils.safeMultiply(value, iScalar);
        return getWrappedField().getMillis(scaled, instant);
    }

    public long add(long instant, int value) {
        long scaled = ((long) value) * ((long) iScalar);
        return getWrappedField().add(instant, scaled);
    }

    public long add(long instant, long value) {
        long scaled = FieldUtils.safeMultiply(value, iScalar);
        return getWrappedField().add(instant, scaled);
    }

    public int getDifference(long minuendInstant, long subtrahendInstant) {
        return getWrappedField().getDifference(minuendInstant, subtrahendInstant) / iScalar;
    }

    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        return getWrappedField().getDifferenceAsLong(minuendInstant, subtrahendInstant) / iScalar;
    }

    public long getUnitMillis() {
        return getWrappedField().getUnitMillis() * iScalar;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the scalar applied, in the field's units.
     * 
     * @return the scalar
     */
    public int getScalar() {
        return iScalar;
    }

    /**
     * Compares this duration field to another.
     * Two fields are equal if of the same type and duration.
     * 
     * @param obj  the object to compare to
     * @return if equal
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof ScaledDurationField) {
            ScaledDurationField other = (ScaledDurationField) obj;
            return (getWrappedField().equals(other.getWrappedField())) &&
                   (getType() == other.getType()) &&
                   (iScalar == other.iScalar);
        }
        return false;
    }

    /**
     * Gets a hash code for this instance.
     * 
     * @return a suitable hashcode
     */
    public int hashCode() {
        long scalar = iScalar;
        int hash = (int) (scalar ^ (scalar >>> 32));
        hash += getType().hashCode();
        hash += getWrappedField().hashCode();
        return hash;
    }

}
