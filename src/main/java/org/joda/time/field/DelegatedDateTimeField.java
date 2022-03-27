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

import java.io.Serializable;
import java.util.Locale;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;

/**
 * <code>DelegatedDateTimeField</code> delegates each method call to the
 * date time field it wraps.
 * <p>
 * DelegatedDateTimeField is thread-safe and immutable, and its subclasses must
 * be as well.
 *
 * @author Brian S O'Neill
 * @since 1.0
 * @see DecoratedDateTimeField
 */
public class DelegatedDateTimeField extends DateTimeField implements Serializable {

    /** Serialization version */
    private static final long serialVersionUID = -4730164440214502503L;

    /** The DateTimeField being wrapped. */
    private final DateTimeField iField;
    /** The range duration. */
    private final DurationField iRangeDurationField;
    /** The override field type. */
    private final DateTimeFieldType iType;

    /**
     * Constructor.
     * 
     * @param field  the field being decorated
     */
    public DelegatedDateTimeField(DateTimeField field) {
        this(field, null);
    }

    /**
     * Constructor.
     * 
     * @param field  the field being decorated
     * @param type  the field type override
     */
    public DelegatedDateTimeField(DateTimeField field, DateTimeFieldType type) {
        this(field, null, type);
    }

    /**
     * Constructor.
     * 
     * @param field  the field being decorated
     * @param rangeField  the range field, null to derive
     * @param type  the field type override
     */
    public DelegatedDateTimeField(DateTimeField field, DurationField rangeField, DateTimeFieldType type) {
        super();
        if (field == null) {
            throw new IllegalArgumentException("The field must not be null");
        }
        iField = field;
        iRangeDurationField = rangeField;
        iType = (type == null ? field.getType() : type);
    }

    /**
     * Gets the wrapped date time field.
     * 
     * @return the wrapped DateTimeField
     */
    public final DateTimeField getWrappedField() {
        return iField;
    }

    @Override
    public DateTimeFieldType getType() {
        return iType;
    }

    @Override
    public String getName() {
        return iType.getName();
    }

    @Override
    public boolean isSupported() {
        return iField.isSupported();
    }

    @Override
    public boolean isLenient() {
        return iField.isLenient();
    }

    @Override
    public int get(long instant) {
        return iField.get(instant);
    }

    @Override
    public String getAsText(long instant, Locale locale) {
        return iField.getAsText(instant, locale);
    }

    @Override
    public String getAsText(long instant) {
        return iField.getAsText(instant);
    }

    @Override
    public String getAsText(ReadablePartial partial, int fieldValue, Locale locale) {
        return iField.getAsText(partial, fieldValue, locale);
    }

    @Override
    public String getAsText(ReadablePartial partial, Locale locale) {
        return iField.getAsText(partial, locale);
    }

    @Override
    public String getAsText(int fieldValue, Locale locale) {
        return iField.getAsText(fieldValue, locale);
    }

    @Override
    public String getAsShortText(long instant, Locale locale) {
        return iField.getAsShortText(instant, locale);
    }

    @Override
    public String getAsShortText(long instant) {
        return iField.getAsShortText(instant);
    }

    @Override
    public String getAsShortText(ReadablePartial partial, int fieldValue, Locale locale) {
        return iField.getAsShortText(partial, fieldValue, locale);
    }

    @Override
    public String getAsShortText(ReadablePartial partial, Locale locale) {
        return iField.getAsShortText(partial, locale);
    }

    @Override
    public String getAsShortText(int fieldValue, Locale locale) {
        return iField.getAsShortText(fieldValue, locale);
    }

    @Override
    public long add(long instant, int value) {
        return iField.add(instant, value);
    }

    @Override
    public long add(long instant, long value) {
        return iField.add(instant, value);
    }

    @Override
    public int[] add(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
        return iField.add(instant, fieldIndex, values, valueToAdd);
    }

    @Override
    public int[] addWrapPartial(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
        return iField.addWrapPartial(instant, fieldIndex, values, valueToAdd);
    }

    @Override
    public long addWrapField(long instant, int value) {
        return iField.addWrapField(instant, value);
    }

    @Override
    public int[] addWrapField(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
        return iField.addWrapField(instant, fieldIndex, values, valueToAdd);
    }

    @Override
    public int getDifference(long minuendInstant, long subtrahendInstant) {
        return iField.getDifference(minuendInstant, subtrahendInstant);
    }

    @Override
    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        return iField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Override
    public long set(long instant, int value) {
        return iField.set(instant, value);
    }

    @Override
    public long set(long instant, String text, Locale locale) {
        return iField.set(instant, text, locale);
    }

    @Override
    public long set(long instant, String text) {
        return iField.set(instant, text);
    }

    @Override
    public int[] set(ReadablePartial instant, int fieldIndex, int[] values, int newValue) {
        return iField.set(instant, fieldIndex, values, newValue);
    }

    @Override
    public int[] set(ReadablePartial instant, int fieldIndex, int[] values, String text, Locale locale) {
        return iField.set(instant, fieldIndex, values, text, locale);
    }

    @Override
    public DurationField getDurationField() {
        return iField.getDurationField();
    }

    @Override
    public DurationField getRangeDurationField() {
        if (iRangeDurationField != null) {
            return iRangeDurationField;
        }
        return iField.getRangeDurationField();
    }

    @Override
    public boolean isLeap(long instant) {
        return iField.isLeap(instant);
    }

    @Override
    public int getLeapAmount(long instant) {
        return iField.getLeapAmount(instant);
    }

    @Override
    public DurationField getLeapDurationField() {
        return iField.getLeapDurationField();
    }

    @Override
    public int getMinimumValue() {
        return iField.getMinimumValue();
    }

    @Override
    public int getMinimumValue(long instant) {
        return iField.getMinimumValue(instant);
    }

    @Override
    public int getMinimumValue(ReadablePartial instant) {
        return iField.getMinimumValue(instant);
    }

    @Override
    public int getMinimumValue(ReadablePartial instant, int[] values) {
        return iField.getMinimumValue(instant, values);
    }

    @Override
    public int getMaximumValue() {
        return iField.getMaximumValue();
    }

    @Override
    public int getMaximumValue(long instant) {
        return iField.getMaximumValue(instant);
    }

    @Override
    public int getMaximumValue(ReadablePartial instant) {
        return iField.getMaximumValue(instant);
    }

    @Override
    public int getMaximumValue(ReadablePartial instant, int[] values) {
        return iField.getMaximumValue(instant, values);
    }

    @Override
    public int getMaximumTextLength(Locale locale) {
        return iField.getMaximumTextLength(locale);
    }

    @Override
    public int getMaximumShortTextLength(Locale locale) {
        return iField.getMaximumShortTextLength(locale);
    }

    @Override
    public long roundFloor(long instant) {
        return iField.roundFloor(instant);
    }

    @Override
    public long roundCeiling(long instant) {
        return iField.roundCeiling(instant);
    }

    @Override
    public long roundHalfFloor(long instant) {
        return iField.roundHalfFloor(instant);
    }

    @Override
    public long roundHalfCeiling(long instant) {
        return iField.roundHalfCeiling(instant);
    }

    @Override
    public long roundHalfEven(long instant) {
        return iField.roundHalfEven(instant);
    }

    @Override
    public long remainder(long instant) {
        return iField.remainder(instant);
    }

    @Override
    public String toString() {
        return ("DateTimeField[" + getName() + ']');
    }

}
