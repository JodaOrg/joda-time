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
 * @since 1.0
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
