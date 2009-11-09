/*
 *  Copyright 2001-2007 Stephen Colebourne
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

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;

/**
 * Converts a strict DateTimeField into a lenient one. By being lenient, the
 * set method accepts out of bounds values, performing an addition instead.
 * <p>
 * LenientDateTimeField is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @see org.joda.time.chrono.LenientChronology
 * @see StrictDateTimeField
 * @since 1.0
 */
public class LenientDateTimeField extends DelegatedDateTimeField {

    private static final long serialVersionUID = 8714085824173290599L;

    private final Chronology iBase;

    /**
     * Returns a lenient version of the given field. If it is already lenient,
     * then it is returned as-is. Otherwise, a new LenientDateTimeField is
     * returned.
     */
    public static DateTimeField getInstance(DateTimeField field, Chronology base) {
        if (field == null) {
            return null;
        }
        if (field instanceof StrictDateTimeField) {
            field = ((StrictDateTimeField)field).getWrappedField();
        }
        if (field.isLenient()) {
            return field;
        }
        return new LenientDateTimeField(field, base);
    }

    protected LenientDateTimeField(DateTimeField field, Chronology base) {
        super(field);
        iBase = base;
    }

    public final boolean isLenient() {
        return true;
    }

    /**
     * Set values which may be out of bounds by adding the difference between
     * the new value and the current value.
     */
    public long set(long instant, int value) {
        // lenient needs to handle time zone chronologies
        // so we do the calculation using local milliseconds
        long localInstant = iBase.getZone().convertUTCToLocal(instant);
        long difference = FieldUtils.safeSubtract(value, get(instant));
        localInstant = getType().getField(iBase.withUTC()).add(localInstant, difference);
        return iBase.getZone().convertLocalToUTC(localInstant, false);
    }
}
