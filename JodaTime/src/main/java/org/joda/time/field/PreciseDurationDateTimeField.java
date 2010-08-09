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
 * Precise datetime field, which has a precise unit duration field.
 * <p>
 * PreciseDurationDateTimeField is thread-safe and immutable, and its
 * subclasses must be as well.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public abstract class PreciseDurationDateTimeField extends BaseDateTimeField {

    private static final long serialVersionUID = 5004523158306266035L;

    /** The fractional unit in millis */
    final long iUnitMillis;

    private final DurationField iUnitField;

    /**
     * Constructor.
     * 
     * @param type  the field type
     * @param unit  precise unit duration, like "days()".
     * @throws IllegalArgumentException if duration field is imprecise
     * @throws IllegalArgumentException if unit milliseconds is less than one
     */
    public PreciseDurationDateTimeField(DateTimeFieldType type, DurationField unit) {
        super(type);

        if (!unit.isPrecise()) {
            throw new IllegalArgumentException("Unit duration field must be precise");
        }

        iUnitMillis = unit.getUnitMillis();
        if (iUnitMillis < 1) {
            throw new IllegalArgumentException("The unit milliseconds must be at least 1");
        }

        iUnitField = unit;
    }

    /**
     * Returns false by default.
     */
    public boolean isLenient() {
        return false;
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
        FieldUtils.verifyValueBounds(this, value, getMinimumValue(),
                                     getMaximumValueForSet(instant, value));
        return instant + (value - get(instant)) * iUnitMillis;
    }

    /**
     * This method assumes that this field is properly rounded on
     * 1970-01-01T00:00:00. If the rounding alignment differs, override this
     * method as follows:
     * <pre>
     * return super.roundFloor(instant + ALIGNMENT_MILLIS) - ALIGNMENT_MILLIS;
     * </pre>
     */
    public long roundFloor(long instant) {
        if (instant >= 0) {
            return instant - instant % iUnitMillis;
        } else {
            instant += 1;
            return instant - instant % iUnitMillis - iUnitMillis;
        }
    }

    /**
     * This method assumes that this field is properly rounded on
     * 1970-01-01T00:00:00. If the rounding alignment differs, override this
     * method as follows:
     * <pre>
     * return super.roundCeiling(instant + ALIGNMENT_MILLIS) - ALIGNMENT_MILLIS;
     * </pre>
     */
    public long roundCeiling(long instant) {
        if (instant > 0) {
            instant -= 1;
            return instant - instant % iUnitMillis + iUnitMillis;
        } else {
            return instant - instant % iUnitMillis;
        }
    }

    /**
     * This method assumes that this field is properly rounded on
     * 1970-01-01T00:00:00. If the rounding alignment differs, override this
     * method as follows:
     * <pre>
     * return super.remainder(instant + ALIGNMENT_MILLIS);
     * </pre>
     */
    public long remainder(long instant) {
        if (instant >= 0) {
            return instant % iUnitMillis;
        } else {
            return (instant + 1) % iUnitMillis + iUnitMillis - 1;
        }
    }

    /**
     * Returns the duration per unit value of this field. For example, if this
     * field represents "minute of hour", then the duration field is minutes.
     *
     * @return the duration of this field, or UnsupportedDurationField if field
     * has no duration
     */
    public DurationField getDurationField() {
        return iUnitField;
    }

    /**
     * Get the minimum value for the field.
     * 
     * @return the minimum value
     */
    public int getMinimumValue() {
        return 0;
    }

    public final long getUnitMillis() {
        return iUnitMillis;
    }

    /**
     * Called by the set method to get the maximum allowed value. By default,
     * returns getMaximumValue(instant). Override to provide a faster
     * implementation.
     */
    protected int getMaximumValueForSet(long instant, int value) {
        return getMaximumValue(instant);
    }

}
