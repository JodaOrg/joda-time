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
 * Counterpart remainder datetime field to {@link DividedDateTimeField}. The
 * field's unit duration is unchanged, but the range duration is scaled
 * accordingly.
 * <p>
 * RemainderDateTimeField is thread-safe and immutable.
 *
 * @see DividedDateTimeField
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public class RemainderDateTimeField extends DecoratedDateTimeField {

    @SuppressWarnings("unused")
    private static final long serialVersionUID = 5708241235177666790L;

    // Shared with DividedDateTimeField.
    final int iDivisor;
    final DurationField iRangeField;

    /**
     * Constructor.
     * 
     * @param field  the field to wrap, like "year()".
     * @param type  the field type this field actually uses
     * @param divisor  divisor, such as 100 years in a century
     * @throws IllegalArgumentException if divisor is less than two
     */
    public RemainderDateTimeField(DateTimeField field,
                                  DateTimeFieldType type, int divisor) {
        super(field, type);

        if (divisor < 2) {
            throw new IllegalArgumentException("The divisor must be at least 2");
        }

        DurationField rangeField = field.getDurationField();
        if (rangeField == null) {
            iRangeField = null;
        } else {
            iRangeField = new ScaledDurationField(
                rangeField, type.getRangeDurationType(), divisor);
        }

        iDivisor = divisor;
    }

    /**
     * Construct a RemainderDateTimeField that compliments the given
     * DividedDateTimeField.
     *
     * @param dividedField  complimentary divided field, like "century()".
     */
    public RemainderDateTimeField(DividedDateTimeField dividedField) {
        this(dividedField, dividedField.getType());
    }

    /**
     * Construct a RemainderDateTimeField that compliments the given
     * DividedDateTimeField.
     *
     * @param dividedField  complimentary divided field, like "century()".
     * @param type  the field type this field actually uses
     */
    public RemainderDateTimeField(DividedDateTimeField dividedField, DateTimeFieldType type) {
        super(dividedField.getWrappedField(), type);
        iDivisor = dividedField.iDivisor;
        iRangeField = dividedField.iDurationField;
    }

    //-----------------------------------------------------------------------
    /**
     * Get the remainder from the specified time instant.
     * 
     * @param instant  the time instant in millis to query.
     * @return the remainder extracted from the input.
     */
    public int get(long instant) {
        int value = getWrappedField().get(instant);
        if (value >= 0) {
            return value % iDivisor;
        } else {
            return (iDivisor - 1) + ((value + 1) % iDivisor);
        }
    }

    /**
     * Add the specified amount to the specified time instant, wrapping around
     * within the remainder range if necessary. The amount added may be
     * negative.
     * 
     * @param instant  the time instant in millis to update.
     * @param amount  the amount to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapField(long instant, int amount) {
        return set(instant, FieldUtils.getWrappedValue(get(instant), amount, 0, iDivisor - 1));
    }

    /**
     * Set the specified amount of remainder units to the specified time instant.
     * 
     * @param instant  the time instant in millis to update.
     * @param value  value of remainder units to set.
     * @return the updated time instant.
     * @throws IllegalArgumentException if value is too large or too small.
     */
    public long set(long instant, int value) {
        FieldUtils.verifyValueBounds(this, value, 0, iDivisor - 1);
        int divided = getDivided(getWrappedField().get(instant));
        return getWrappedField().set(instant, divided * iDivisor + value);
    }

    /**
     * Returns a scaled version of the wrapped field's unit duration field.
     */
    public DurationField getRangeDurationField() {
        return iRangeField;
    }

    /**
     * Get the minimum value for the field, which is always zero.
     * 
     * @return the minimum value of zero.
     */
    public int getMinimumValue() {
        return 0;
    }

    /**
     * Get the maximum value for the field, which is always one less than the
     * divisor.
     * 
     * @return the maximum value
     */
    public int getMaximumValue() {
        return iDivisor - 1;
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

    /**
     * Returns the divisor applied, in the field's units.
     * 
     * @return the divisor
     */
    public int getDivisor() {
        return iDivisor;
    }

    private int getDivided(int value) {
        if (value >= 0) {
            return value / iDivisor;
        } else {
            return ((value + 1) / iDivisor) - 1;
        }
    }

}
