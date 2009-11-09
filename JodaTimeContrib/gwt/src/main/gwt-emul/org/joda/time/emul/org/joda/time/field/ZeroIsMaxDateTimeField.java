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

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;

/**
 * Wraps another field such that zero values are replaced with one more than
 * it's maximum. This is particularly useful for implementing an clockhourOfDay
 * field, where the midnight value of 0 is replaced with 24.
 * <p>
 * ZeroIsMaxDateTimeField is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class ZeroIsMaxDateTimeField extends DecoratedDateTimeField {

    private static final long serialVersionUID = 961749798233026866L;

    /**
     * Constructor.
     * 
     * @param field  the base field
     * @param type  the field type this field will actually use
     * @throws IllegalArgumentException if wrapped field's minimum value is not zero
     */
    public ZeroIsMaxDateTimeField(DateTimeField field, DateTimeFieldType type) {
        super(field, type);
        if (field.getMinimumValue() != 0) {
            throw new IllegalArgumentException("Wrapped field's minumum value must be zero");
        }
    }

    public int get(long instant) {
        int value = getWrappedField().get(instant);
        if (value == 0) {
            value = getMaximumValue();
        }
        return value;
    }

    public long add(long instant, int value) {
        return getWrappedField().add(instant, value);
    }

    public long add(long instant, long value) {
        return getWrappedField().add(instant, value);
    }

    public long addWrapField(long instant, int value) {
        return getWrappedField().addWrapField(instant, value);
    }

    public int[] addWrapField(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
        return getWrappedField().addWrapField(instant, fieldIndex, values, valueToAdd);
    }

    public int getDifference(long minuendInstant, long subtrahendInstant) {
        return getWrappedField().getDifference(minuendInstant, subtrahendInstant);
    }

    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        return getWrappedField().getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    public long set(long instant, int value) {
        int max = getMaximumValue();
        FieldUtils.verifyValueBounds(this, value, 1, max);
        if (value == max) {
            value = 0;
        }
        return getWrappedField().set(instant, value);
    }

    public boolean isLeap(long instant) {
        return getWrappedField().isLeap(instant);
    }

    public int getLeapAmount(long instant) {
        return getWrappedField().getLeapAmount(instant);
    }

    public DurationField getLeapDurationField() {
        return getWrappedField().getLeapDurationField();
    }

    /**
     * Always returns 1.
     * 
     * @return the minimum value of 1
     */
    public int getMinimumValue() {
        return 1;
    }

    /**
     * Always returns 1.
     * 
     * @return the minimum value of 1
     */
    public int getMinimumValue(long instant) {
        return 1;
    }

    /**
     * Always returns 1.
     * 
     * @return the minimum value of 1
     */
    public int getMinimumValue(ReadablePartial instant) {
        return 1;
    }

    /**
     * Always returns 1.
     * 
     * @return the minimum value of 1
     */
    public int getMinimumValue(ReadablePartial instant, int[] values) {
        return 1;
    }

    /**
     * Get the maximum value for the field, which is one more than the wrapped
     * field's maximum value.
     * 
     * @return the maximum value
     */
    public int getMaximumValue() {
        return getWrappedField().getMaximumValue() + 1;
    }

    /**
     * Get the maximum value for the field, which is one more than the wrapped
     * field's maximum value.
     * 
     * @return the maximum value
     */
    public int getMaximumValue(long instant) {
        return getWrappedField().getMaximumValue(instant) + 1;
    }

    /**
     * Get the maximum value for the field, which is one more than the wrapped
     * field's maximum value.
     * 
     * @return the maximum value
     */
    public int getMaximumValue(ReadablePartial instant) {
        return getWrappedField().getMaximumValue(instant) + 1;
    }

    /**
     * Get the maximum value for the field, which is one more than the wrapped
     * field's maximum value.
     * 
     * @return the maximum value
     */
    public int getMaximumValue(ReadablePartial instant, int[] values) {
        return getWrappedField().getMaximumValue(instant, values) + 1;
    }

    public long roundFloor(long instant) {
        return getWrappedField().roundFloor(instant);
    }

    public long roundCeiling(long instant) {
        return getWrappedField().roundCeiling(instant);
    }

    public long roundHalfFloor(long instant) {
        return getWrappedField().roundHalfFloor(instant);
    }

    public long roundHalfCeiling(long instant) {
        return getWrappedField().roundHalfCeiling(instant);
    }

    public long roundHalfEven(long instant) {
        return getWrappedField().roundHalfEven(instant);
    }

    public long remainder(long instant) {
        return getWrappedField().remainder(instant);
    }

}
