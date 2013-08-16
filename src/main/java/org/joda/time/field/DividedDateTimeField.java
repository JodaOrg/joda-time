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
 * Divides a DateTimeField such that the retrieved values are reduced by a
 * fixed divisor. The field's unit duration is scaled accordingly, but the
 * range duration is unchanged.
 * <p>
 * DividedDateTimeField is thread-safe and immutable.
 *
 * @see RemainderDateTimeField
 * 
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public class DividedDateTimeField extends DecoratedDateTimeField {

    @SuppressWarnings("unused")
    private static final long serialVersionUID = 8318475124230605365L;

    // Shared with RemainderDateTimeField.
    final int iDivisor;
    final DurationField iDurationField;

    private final int iMin;
    private final int iMax;

    /**
     * Constructor.
     * 
     * @param field  the field to wrap, like "year()".
     * @param type  the field type this field will actually use
     * @param divisor  divisor, such as 100 years in a century
     * @throws IllegalArgumentException if divisor is less than two
     */
    public DividedDateTimeField(DateTimeField field,
                                DateTimeFieldType type, int divisor) {
        super(field, type);
                
        if (divisor < 2) {
            throw new IllegalArgumentException("The divisor must be at least 2");
        }

        DurationField unitField = field.getDurationField();
        if (unitField == null) {
            iDurationField = null;
        } else {
            iDurationField = new ScaledDurationField(
                unitField, type.getDurationType(), divisor);
        }

        iDivisor = divisor;

        int i = field.getMinimumValue();
        int min = (i >= 0) ? i / divisor : ((i + 1) / divisor - 1);

        int j = field.getMaximumValue();
        int max = (j >= 0) ? j / divisor : ((j + 1) / divisor - 1);

        iMin = min;
        iMax = max;
    }

    /**
     * Construct a DividedDateTimeField that compliments the given
     * RemainderDateTimeField.
     *
     * @param remainderField  complimentary remainder field, like "yearOfCentury()".
     * @param type  the field type this field will actually use
     */
    public DividedDateTimeField(RemainderDateTimeField remainderField, DateTimeFieldType type) {
        super(remainderField.getWrappedField(), type);
        int divisor = iDivisor = remainderField.iDivisor;
        iDurationField = remainderField.iRangeField;

        DateTimeField field = getWrappedField();
        int i = field.getMinimumValue();
        int min = (i >= 0) ? i / divisor : ((i + 1) / divisor - 1);

        int j = field.getMaximumValue();
        int max = (j >= 0) ? j / divisor : ((j + 1) / divisor - 1);

        iMin = min;
        iMax = max;
    }

    /**
     * Get the amount of scaled units from the specified time instant.
     * 
     * @param instant  the time instant in millis to query.
     * @return the amount of scaled units extracted from the input.
     */
    public int get(long instant) {
        int value = getWrappedField().get(instant);
        if (value >= 0) {
            return value / iDivisor;
        } else {
            return ((value + 1) / iDivisor) - 1;
        }
    }

    /**
     * Add the specified amount of scaled units to the specified time
     * instant. The amount added may be negative.
     * 
     * @param instant  the time instant in millis to update.
     * @param amount  the amount of scaled units to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long instant, int amount) {
        return getWrappedField().add(instant, amount * iDivisor);
    }

    /**
     * Add the specified amount of scaled units to the specified time
     * instant. The amount added may be negative.
     * 
     * @param instant  the time instant in millis to update.
     * @param amount  the amount of scaled units to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long instant, long amount) {
        return getWrappedField().add(instant, amount * iDivisor);
    }

    /**
     * Add to the scaled component of the specified time instant,
     * wrapping around within that component if necessary.
     * 
     * @param instant  the time instant in millis to update.
     * @param amount  the amount of scaled units to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapField(long instant, int amount) {
        return set(instant, FieldUtils.getWrappedValue(get(instant), amount, iMin, iMax));
    }

    public int getDifference(long minuendInstant, long subtrahendInstant) {
        return getWrappedField().getDifference(minuendInstant, subtrahendInstant) / iDivisor;
    }

    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        return getWrappedField().getDifferenceAsLong(minuendInstant, subtrahendInstant) / iDivisor;
    }

    /**
     * Set the specified amount of scaled units to the specified time instant.
     * 
     * @param instant  the time instant in millis to update.
     * @param value  value of scaled units to set.
     * @return the updated time instant.
     * @throws IllegalArgumentException if value is too large or too small.
     */
    public long set(long instant, int value) {
        FieldUtils.verifyValueBounds(this, value, iMin, iMax);
        int remainder = getRemainder(getWrappedField().get(instant));
        return getWrappedField().set(instant, value * iDivisor + remainder);
    }

    /**
     * Returns a scaled version of the wrapped field's unit duration field.
     */
    public DurationField getDurationField() {
        return iDurationField;
    }

    /**
     * Get the minimum value for the field.
     * 
     * @return the minimum value
     */
    public int getMinimumValue() {
        return iMin;
    }

    /**
     * Get the maximum value for the field.
     * 
     * @return the maximum value
     */
    public int getMaximumValue() {
        return iMax;
    }

    public long roundFloor(long instant) {
        DateTimeField field = getWrappedField();
        return field.roundFloor(field.set(instant, get(instant) * iDivisor));
    }

    public long remainder(long instant) {
        return set(instant, get(getWrappedField().remainder(instant)));
    }

    /**
     * Returns the divisor applied, in the field's units.
     * 
     * @return the divisor
     */
    public int getDivisor() {
        return iDivisor;
    }

    private int getRemainder(int value) {
        if (value >= 0) {
            return value % iDivisor;
        } else {
            return (iDivisor - 1) + ((value + 1) % iDivisor);
        }
    }

}
