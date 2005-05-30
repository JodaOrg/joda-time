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
package org.joda.time.chrono;

import java.util.Locale;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.field.BaseDateTimeField;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.UnsupportedDurationField;

/**
 * Provides time calculations for the coptic era component of time.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
final class CopticEraDateTimeField extends BaseDateTimeField {
    
    /** Serialization version */
    private static final long serialVersionUID = 4090856468123006167L;

    /**
     * Singleton instance
     */
    static final DateTimeField INSTANCE = new CopticEraDateTimeField();

    /**
     * Restricted constructor
     */
    private CopticEraDateTimeField() {
        super(DateTimeFieldType.era());
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return INSTANCE;
    }

    public boolean isLenient() {
        return false;
    }

    /**
     * Get the Era component of the specified time instant.
     * 
     * @param instant  the time instant in millis to query.
     * @return the era extracted from the input.
     */
    public int get(long instant) {
        return CopticChronology.AM;
    }

    /**
     * Set the Era component of the specified time instant.
     * 
     * @param instant  the time instant in millis to update.
     * @param era  the era (CopticChronology.AM) to update the time to.
     * @return the updated time instant.
     * @throws IllegalArgumentException  if era is invalid.
     */
    public long set(long instant, int era) {
        FieldUtils.verifyValueBounds(this, era, getMinimumValue(), getMaximumValue());

        return instant;
    }

    /**
     * @see org.joda.time.DateTimeField#set(long, String, Locale)
     */
    public long set(long instant, String text, Locale locale) {
        if ("AM".equals(text) == false) {
            throw new IllegalFieldValueException(DateTimeFieldType.era(), text);
        }
        return instant;
    }

    public long roundFloor(long instant) {
        return Long.MIN_VALUE;
    }

    public long roundCeiling(long instant) {
        return Long.MAX_VALUE;
    }

    public long roundHalfFloor(long instant) {
        return Long.MIN_VALUE;
    }

    public long roundHalfCeiling(long instant) {
        return Long.MIN_VALUE;
    }

    public long roundHalfEven(long instant) {
        return Long.MIN_VALUE;
    }

    public DurationField getDurationField() {
        return UnsupportedDurationField.getInstance(DurationFieldType.eras());
    }

    public DurationField getRangeDurationField() {
        return null;
    }

    public int getMinimumValue() {
        return CopticChronology.AM;
    }

    public int getMaximumValue() {
        return CopticChronology.AM;
    }
    
    public String getAsText(int fieldValue, Locale locale) {
        return "AM";
    }

    public int getMaximumTextLength(Locale locale) {
        return 2;
    }

}
