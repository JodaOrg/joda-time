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

import org.joda.time.DurationFieldType;

/**
 * Duration field class representing a field with a fixed unit length.
 * <p>
 * PreciseDurationField is thread-safe and immutable.
 * 
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public class PreciseDurationField extends BaseDurationField {
    
    private static final long serialVersionUID = -8346152187724495365L;

    /** The size of the unit */
    private final long iUnitMillis;

    /**
     * Constructor.
     * 
     * @param type  the field type
     * @param unitMillis  the unit milliseconds
     */    
    public PreciseDurationField(DurationFieldType type, long unitMillis) {
        super(type);
        iUnitMillis = unitMillis;
    }
    
    //------------------------------------------------------------------------
    /**
     * This field is precise.
     * 
     * @return true always
     */
    @Override
    public final boolean isPrecise() {
        return true;
    }
    
    /**
     * Returns the amount of milliseconds per unit value of this field.
     *
     * @return the unit size of this field, in milliseconds
     */
    @Override
    public final long getUnitMillis() {
        return iUnitMillis;
    }

    //------------------------------------------------------------------------
    /**
     * Get the value of this field from the milliseconds.
     * 
     * @param duration  the milliseconds to query, which may be negative
     * @param instant  ignored
     * @return the value of the field, in the units of the field, which may be
     * negative
     */
    @Override
    public long getValueAsLong(long duration, long instant) {
        return duration / iUnitMillis;  // safe
    }

    /**
     * Get the millisecond duration of this field from its value.
     * 
     * @param value  the value of the field, which may be negative
     * @param instant  ignored
     * @return the milliseconds that the field represents, which may be
     * negative
     */
    @Override
    public long getMillis(int value, long instant) {
        return value * iUnitMillis;  // safe
    }

    /**
     * Get the millisecond duration of this field from its value.
     * 
     * @param value  the value of the field, which may be negative
     * @param instant  ignored
     * @return the milliseconds that the field represents, which may be
     * negative
     */
    @Override
    public long getMillis(long value, long instant) {
        return FieldUtils.safeMultiply(value, iUnitMillis);
    }

    @Override
    public long add(long instant, int value) {
        long addition = value * iUnitMillis;  // safe
        return FieldUtils.safeAdd(instant, addition);
    }

    @Override
    public long add(long instant, long value) {
        long addition = FieldUtils.safeMultiply(value, iUnitMillis);
        return FieldUtils.safeAdd(instant, addition);
    }

    @Override
    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        long difference = FieldUtils.safeSubtract(minuendInstant, subtrahendInstant);
        return difference / iUnitMillis;
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this duration field to another.
     * Two fields are equal if of the same type and duration.
     * 
     * @param obj  the object to compare to
     * @return if equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof PreciseDurationField) {
            PreciseDurationField other = (PreciseDurationField) obj;
            return (getType() == other.getType()) && (iUnitMillis == other.iUnitMillis);
        }
        return false;
    }

    /**
     * Gets a hash code for this instance.
     * 
     * @return a suitable hashcode
     */
    @Override
    public int hashCode() {
        long millis = iUnitMillis;
        int hash = (int) (millis ^ (millis >>> 32));
        hash += getType().hashCode();
        return hash;
    }

}
