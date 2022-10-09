/*
 *  Copyright 2001-2009 Stephen Colebourne
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
import java.util.HashMap;
import java.util.Locale;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;

/**
 * A placeholder implementation to use when a datetime field is not supported.
 * <p>
 * UnsupportedDateTimeField is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class UnsupportedDateTimeField extends DateTimeField implements Serializable {

    /** Serialization version */
    private static final long serialVersionUID = -1934618396111902255L;

    /** The cache of unsupported datetime field instances */
    private static HashMap<DateTimeFieldType, UnsupportedDateTimeField> cCache;

    /**
     * Gets an instance of UnsupportedDateTimeField for a specific named field.
     * Names should be of standard format, such as 'monthOfYear' or 'hourOfDay'.
     * The returned instance is cached.
     * 
     * @param type  the type to obtain
     * @param durationField  the duration field
     * @return the instance
     * @throws IllegalArgumentException if durationField is null
     */
    public static synchronized UnsupportedDateTimeField getInstance(
            DateTimeFieldType type, DurationField durationField) {

        UnsupportedDateTimeField field;
        if (cCache == null) {
            cCache = new HashMap<DateTimeFieldType, UnsupportedDateTimeField>(7);
            field = null;
        } else {
            field = cCache.get(type);
            if (field != null && field.getDurationField() != durationField) {
                field = null;
            }
        }
        if (field == null) {
            field = new UnsupportedDateTimeField(type, durationField);
            cCache.put(type, field);
        }
        return field;
    }

    /** The field type */
    private final DateTimeFieldType iType;
    /** The duration of the datetime field */
    private final DurationField iDurationField;

    /**
     * Constructor.
     * 
     * @param type  the field type
     * @param durationField  the duration to use
     */
    private UnsupportedDateTimeField(DateTimeFieldType type, DurationField durationField) {
        if (type == null || durationField == null) {
            throw new IllegalArgumentException();
        }
        iType = type;
        iDurationField = durationField;
    }

    //-----------------------------------------------------------------------
    // Design note: Simple accessors return a suitable value, but methods
    // intended to perform calculations throw an UnsupportedOperationException.

    @Override
    public DateTimeFieldType getType() {
        return iType;
    }

    @Override
    public String getName() {
        return iType.getName();
    }

    /**
     * This field is not supported.
     *
     * @return false always
     */
    @Override
    public boolean isSupported() {
        return false;
    }

    /**
     * This field is not lenient.
     *
     * @return false always
     */
    @Override
    public boolean isLenient() {
        return false;
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public int get(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public String getAsText(long instant, Locale locale) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public String getAsText(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public String getAsText(ReadablePartial partial, int fieldValue, Locale locale) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public String getAsText(ReadablePartial partial, Locale locale) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public String getAsText(int fieldValue, Locale locale) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public String getAsShortText(long instant, Locale locale) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public String getAsShortText(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public String getAsShortText(ReadablePartial partial, int fieldValue, Locale locale) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public String getAsShortText(ReadablePartial partial, Locale locale) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public String getAsShortText(int fieldValue, Locale locale) {
        throw unsupported();
    }

    /**
     * Delegates to the duration field.
     *
     * @throws UnsupportedOperationException if the duration is unsupported
     */
    @Override
    public long add(long instant, int value) {
        return getDurationField().add(instant, value);
    }

    /**
     * Delegates to the duration field.
     *
     * @throws UnsupportedOperationException if the duration is unsupported
     */
    @Override
    public long add(long instant, long value) {
        return getDurationField().add(instant, value);
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public int[] add(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public int[] addWrapPartial(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public long addWrapField(long instant, int value) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public int[] addWrapField(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
        throw unsupported();
    }

    /**
     * Delegates to the duration field.
     *
     * @throws UnsupportedOperationException if the duration is unsupported
     */
    @Override
    public int getDifference(long minuendInstant, long subtrahendInstant) {
        return getDurationField().getDifference(minuendInstant, subtrahendInstant);
    }

    /**
     * Delegates to the duration field.
     *
     * @throws UnsupportedOperationException if the duration is unsupported
     */
    @Override
    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        return getDurationField().getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public long set(long instant, int value) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public int[] set(ReadablePartial instant, int fieldIndex, int[] values, int newValue) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public long set(long instant, String text, Locale locale) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public long set(long instant, String text) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public int[] set(ReadablePartial instant, int fieldIndex, int[] values, String text, Locale locale) {
        throw unsupported();
    }

    /**
     * Even though this DateTimeField is unsupported, the duration field might
     * be supported.
     *
     * @return a possibly supported DurationField
     */
    @Override
    public DurationField getDurationField() {
        return iDurationField;
    }

    /**
     * Always returns null.
     *
     * @return null always
     */
    @Override
    public DurationField getRangeDurationField() {
        return null;
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public boolean isLeap(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public int getLeapAmount(long instant) {
        throw unsupported();
    }

    /**
     * Always returns null.
     *
     * @return null always
     */
    @Override
    public DurationField getLeapDurationField() {
        return null;
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public int getMinimumValue() {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public int getMinimumValue(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public int getMinimumValue(ReadablePartial instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public int getMinimumValue(ReadablePartial instant, int[] values) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public int getMaximumValue() {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public int getMaximumValue(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public int getMaximumValue(ReadablePartial instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public int getMaximumValue(ReadablePartial instant, int[] values) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public int getMaximumTextLength(Locale locale) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public int getMaximumShortTextLength(Locale locale) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public long roundFloor(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public long roundCeiling(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public long roundHalfFloor(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public long roundHalfCeiling(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public long roundHalfEven(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException as this is not supported
     */
    @Override
    public long remainder(long instant) {
        throw unsupported();
    }

    //------------------------------------------------------------------------
    /**
     * Get a suitable debug string.
     * 
     * @return debug string
     */
    @Override
    public String toString() {
        return "UnsupportedDateTimeField";
    }

    /**
     * Ensure proper singleton serialization
     */
    private Object readResolve() {
        return getInstance(iType, iDurationField);
    }

    private UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException(iType + " field is unsupported");
    }

}
