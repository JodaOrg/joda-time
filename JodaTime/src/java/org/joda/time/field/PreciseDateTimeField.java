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

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;

/**
 * Precise datetime field, composed of two precise duration fields.
 * <p>
 * This DateTimeField is useful for defining DateTimeFields that are composed
 * of precise durations, like time of day fields. If either duration field is
 * imprecise, then an {@link ImpreciseDateTimeField} may be used instead.
 * <p>
 * PreciseDateTimeField is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 * @see ImpreciseDateTimeField
 */
public class PreciseDateTimeField extends PreciseDurationDateTimeField {

    private static final long serialVersionUID = -5586801265774496376L;

    /** The maximum range in the correct units */
    private final int iRange;

    private final DurationField iRangeField;

    /**
     * Constructor.
     * 
     * @param type  the field type this field uses
     * @param unit  precise unit duration, like "seconds()".
     * @param range precise range duration, preferably a multiple of the unit,
     * like "minutes()".
     * @throws IllegalArgumentException if either duration field is imprecise
     * @throws IllegalArgumentException if unit milliseconds is less than one
     * or effective value range is less than two.
     */
    public PreciseDateTimeField(DateTimeFieldType type,
                                DurationField unit, DurationField range) {
        super(type, unit);

        if (!range.isPrecise()) {
            throw new IllegalArgumentException("Range duration field must be precise");
        }

        long rangeMillis = range.getUnitMillis();
        iRange = (int)(rangeMillis / getUnitMillis());
        if (iRange < 2) {
            throw new IllegalArgumentException("The effective range must be at least 2");
        }

        iRangeField = range;
    }

    /**
     * Get the amount of fractional units from the specified time instant.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to query
     * @return the amount of fractional units extracted from the input.
     */
    public int get(long instant) {
        if (instant >= 0) {
            return (int) ((instant / getUnitMillis()) % iRange);
        } else {
            return iRange - 1 + (int) (((instant + 1) / getUnitMillis()) % iRange);
        }
    }

    /**
     * Add to the component of the specified time instant, wrapping around
     * within that component if necessary.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to add to
     * @param amount  the amount of units to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapField(long instant, int amount) {
        int thisValue = get(instant);
        int wrappedValue = FieldUtils.getWrappedValue
            (thisValue, amount, getMinimumValue(), getMaximumValue());
        // copy code from set() to avoid repeat call to get()
        return instant + (wrappedValue - thisValue) * getUnitMillis();
    }

    /**
     * Set the specified amount of units to the specified time instant.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to set in
     * @param value  value of units to set.
     * @return the updated time instant.
     * @throws IllegalArgumentException if value is too large or too small.
     */
    public long set(long instant, int value) {
        FieldUtils.verifyValueBounds(this, value, getMinimumValue(), getMaximumValue());
        return instant + (value - get(instant)) * iUnitMillis;
    }

    /**
     * Returns the range duration of this field. For example, if this field
     * represents "minute of hour", then the range duration field is an hours.
     *
     * @return the range duration of this field, or null if field has no range
     */
    public DurationField getRangeDurationField() {
        return iRangeField;
    }

    /**
     * Get the maximum value for the field.
     * 
     * @return the maximum value
     */
    public int getMaximumValue() {
        return iRange - 1;
    }
    
    /**
     * Returns the range of the field in the field's units.
     * <p>
     * For example, 60 for seconds per minute. The field is allowed values
     * from 0 to range - 1.
     * 
     * @return unit range
     */
    public int getRange() {
        return iRange;
    }

}
