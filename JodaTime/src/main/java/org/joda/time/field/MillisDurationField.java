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

import java.io.Serializable;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

/**
 * Duration field class representing a field with a fixed unit length of one
 * millisecond.
 * <p>
 * MillisDurationField is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class MillisDurationField extends DurationField implements Serializable {

    /** Serialization lock. */
    private static final long serialVersionUID = 2656707858124633367L;

    /** Singleton instance. */
    public static final DurationField INSTANCE = new MillisDurationField();

    /**
     * Restricted constructor.
     */
    private MillisDurationField() {
        super();
    }
    
    //------------------------------------------------------------------------
    public DurationFieldType getType() {
        return DurationFieldType.millis();
    }

    public String getName() {
        return "millis";
    }

    /**
     * Returns true as this field is supported.
     * 
     * @return true always
     */
    public boolean isSupported() {
        return true;
    }

    /**
     * Returns true as this field is precise.
     * 
     * @return true always
     */
    public final boolean isPrecise() {
        return true;
    }

    /**
     * Returns the amount of milliseconds per unit value of this field.
     *
     * @return one always
     */
    public final long getUnitMillis() {
        return 1;
    }

    //------------------------------------------------------------------------
    public int getValue(long duration) {
        return FieldUtils.safeToInt(duration);
    }

    public long getValueAsLong(long duration) {
        return duration;
    }

    public int getValue(long duration, long instant) {
        return FieldUtils.safeToInt(duration);
    }

    public long getValueAsLong(long duration, long instant) {
        return duration;
    }

    public long getMillis(int value) {
        return value;
    }

    public long getMillis(long value) {
        return value;
    }

    public long getMillis(int value, long instant) {
        return value;
    }

    public long getMillis(long value, long instant) {
        return value;
    }

    public long add(long instant, int value) {
        return FieldUtils.safeAdd(instant, value);
    }

    public long add(long instant, long value) {
        return FieldUtils.safeAdd(instant, value);
    }

    public int getDifference(long minuendInstant, long subtrahendInstant) {
        return FieldUtils.safeToInt(FieldUtils.safeSubtract(minuendInstant, subtrahendInstant));
    }

    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        return FieldUtils.safeSubtract(minuendInstant, subtrahendInstant);
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
        return "DurationField[millis]";
    }

    /**
     * Deserialize to the singleton.
     */
    private Object readResolve() {
        return INSTANCE;
    }

}
