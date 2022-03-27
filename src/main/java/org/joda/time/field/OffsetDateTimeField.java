/*
 *  Copyright 2001-2013 Stephen Colebourne
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

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;

/**
 * Generic offset adjusting datetime field.
 * <p>
 * OffsetDateTimeField is thread-safe and immutable.
 * 
 * @author Brian S O'Neill
 * @since 1.0
 */
public class OffsetDateTimeField extends DecoratedDateTimeField {
    @SuppressWarnings("unused")
    private static final long serialVersionUID = 3145790132623583142L;

    private final int iOffset;

    private final int iMin;
    private final int iMax;

    /**
     * Constructor.
     * 
     * @param field  the field to wrap, like "year()".
     * @param offset  offset to add to field values
     * @throws IllegalArgumentException if offset is zero
     */
    public OffsetDateTimeField(DateTimeField field, int offset) {
        this(field, (field == null ? null : field.getType()), offset, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Constructor.
     * 
     * @param field  the field to wrap, like "year()".
     * @param type  the field type this field actually uses
     * @param offset  offset to add to field values
     * @throws IllegalArgumentException if offset is zero
     */
    public OffsetDateTimeField(DateTimeField field, DateTimeFieldType type, int offset) {
        this(field, type, offset, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Constructor.
     * 
     * @param field  the field to wrap, like "year()".
     * @param type  the field type this field actually uses
     * @param offset  offset to add to field values
     * @param minValue  minimum allowed value
     * @param maxValue  maximum allowed value
     * @throws IllegalArgumentException if offset is zero
     */
    public OffsetDateTimeField(DateTimeField field, DateTimeFieldType type, int offset,
                               int minValue, int maxValue) {
        super(field, type);
                
        if (offset == 0) {
            throw new IllegalArgumentException("The offset cannot be zero");
        }

        iOffset = offset;

        if (minValue < (field.getMinimumValue() + offset)) {
            iMin = field.getMinimumValue() + offset;
        } else {
            iMin = minValue;
        }
        if (maxValue > (field.getMaximumValue() + offset)) {
            iMax = field.getMaximumValue() + offset;
        } else {
            iMax = maxValue;
        }
    }

    /**
     * Get the amount of offset units from the specified time instant.
     * 
     * @param instant  the time instant in millis to query.
     * @return the amount of units extracted from the input.
     */
    @Override
    public int get(long instant) {
        return super.get(instant) + iOffset;
    }

    /**
     * Add the specified amount of offset units to the specified time
     * instant. The amount added may be negative.
     * 
     * @param instant  the time instant in millis to update.
     * @param amount  the amount of units to add (can be negative).
     * @return the updated time instant.
     */
    @Override
    public long add(long instant, int amount) {
        instant = super.add(instant, amount);
        FieldUtils.verifyValueBounds(this, get(instant), iMin, iMax);
        return instant;
    }

    /**
     * Add the specified amount of offset units to the specified time
     * instant. The amount added may be negative.
     * 
     * @param instant  the time instant in millis to update.
     * @param amount  the amount of units to add (can be negative).
     * @return the updated time instant.
     */
    @Override
    public long add(long instant, long amount) {
        instant = super.add(instant, amount);
        FieldUtils.verifyValueBounds(this, get(instant), iMin, iMax);
        return instant;
    }

    /**
     * Add to the offset component of the specified time instant,
     * wrapping around within that component if necessary.
     * 
     * @param instant  the time instant in millis to update.
     * @param amount  the amount of units to add (can be negative).
     * @return the updated time instant.
     */
    @Override
    public long addWrapField(long instant, int amount) {
        return set(instant, FieldUtils.getWrappedValue(get(instant), amount, iMin, iMax));
    }

    /**
     * Set the specified amount of offset units to the specified time instant.
     * 
     * @param instant  the time instant in millis to update.
     * @param value  value of units to set.
     * @return the updated time instant.
     * @throws IllegalArgumentException if value is too large or too small.
     */
    @Override
    public long set(long instant, int value) {
        FieldUtils.verifyValueBounds(this, value, iMin, iMax);
        return super.set(instant, value - iOffset);
    }

    @Override
    public boolean isLeap(long instant) {
        return getWrappedField().isLeap(instant);
    }

    @Override
    public int getLeapAmount(long instant) {
        return getWrappedField().getLeapAmount(instant);
    }

    @Override
    public DurationField getLeapDurationField() {
        return getWrappedField().getLeapDurationField();
    }

    /**
     * Get the minimum value for the field.
     * 
     * @return the minimum value
     */
    @Override
    public int getMinimumValue() {
        return iMin;
    }

    /**
     * Get the maximum value for the field.
     * 
     * @return the maximum value
     */
    @Override
    public int getMaximumValue() {
        return iMax;
    }
    
    @Override
    public long roundFloor(long instant) {
        return getWrappedField().roundFloor(instant);
    }

    @Override
    public long roundCeiling(long instant) {
        return getWrappedField().roundCeiling(instant);
    }

    @Override
    public long roundHalfFloor(long instant) {
        return getWrappedField().roundHalfFloor(instant);
    }

    @Override
    public long roundHalfCeiling(long instant) {
        return getWrappedField().roundHalfCeiling(instant);
    }

    @Override
    public long roundHalfEven(long instant) {
        return getWrappedField().roundHalfEven(instant);
    }

    @Override
    public long remainder(long instant) {
        return getWrappedField().remainder(instant);
    }

    /**
     * Returns the offset added to the field values.
     * 
     * @return the offset
     */
    public int getOffset() {
        return iOffset;
    }
}
