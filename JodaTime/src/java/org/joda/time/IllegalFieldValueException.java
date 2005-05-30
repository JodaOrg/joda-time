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
package org.joda.time;

/**
 * Exception thrown when attempting to set a field outisde its supported range.
 *
 * @author Brian S O'Neill
 * @since 1.1
 */
public class IllegalFieldValueException extends IllegalArgumentException {
    private static String createMessage(String fieldName, Number value,
                                        Number lowerBound, Number upperBound) {
        StringBuffer buf = new StringBuffer()
            .append("Value ").append(value).append(" for ").append(fieldName).append(' ');

        if (lowerBound == null) {
            if (upperBound == null) {
                buf.append("is not supported");
            } else {
                buf.append("must not be larger than ").append(upperBound);
            }
        } else if (upperBound == null) {
            buf.append("must not be smaller than ").append(lowerBound);
        } else {
            buf.append("must be in the range [")
                .append(lowerBound)
                .append(',')
                .append(upperBound)
                .append(']');
        }

        return buf.toString();
    }

    private static String createMessage(String fieldName, String value) {
        StringBuffer buf = new StringBuffer().append("Value ");

        if (value == null) {
            buf.append("null");
        } else {
            buf.append('"');
            buf.append(value);
            buf.append('"');
        }

        buf.append(" for ").append(fieldName).append(' ').append("is not supported");
        
        return buf.toString();
    }
    
    private final DateTimeFieldType iDateTimeFieldType;
    private final DurationFieldType iDurationFieldType;
    private final String iFieldName;
    private final Number iNumberValue;
    private final String iStringValue;
    private final Number iLowerBound;
    private final Number iUpperBound;

    /**
     * @param fieldType type of field being set
     * @param value illegal value being set
     * @param lowerBound lower legal field value, or null if not applicable
     * @param upperBound upper legal field value, or null if not applicable
     */
    public IllegalFieldValueException(DateTimeFieldType fieldType,
                                      Number value, Number lowerBound, Number upperBound) {
        super(createMessage(fieldType.getName(), value, lowerBound, upperBound));
        iDateTimeFieldType = fieldType;
        iDurationFieldType = null;
        iFieldName = fieldType.getName();
        iNumberValue = value;
        iStringValue = null;
        iLowerBound = lowerBound;
        iUpperBound = upperBound;
    }

    /**
     * @param fieldType type of field being set
     * @param value illegal value being set
     * @param lowerBound lower legal field value, or null if not applicable
     * @param upperBound upper legal field value, or null if not applicable
     */
    public IllegalFieldValueException(DurationFieldType fieldType,
                                      Number value, Number lowerBound, Number upperBound) {
        super(createMessage(fieldType.getName(), value, lowerBound, upperBound));
        iDateTimeFieldType = null;
        iDurationFieldType = fieldType;
        iFieldName = fieldType.getName();
        iNumberValue = value;
        iStringValue = null;
        iLowerBound = lowerBound;
        iUpperBound = upperBound;
    }

    /**
     * @param fieldName name of field being set
     * @param value illegal value being set
     * @param lowerBound lower legal field value, or null if not applicable
     * @param upperBound upper legal field value, or null if not applicable
     */
    public IllegalFieldValueException(String fieldName,
                                      Number value, Number lowerBound, Number upperBound) {
        super(createMessage(fieldName, value, lowerBound, upperBound));
        iDateTimeFieldType = null;
        iDurationFieldType = null;
        iFieldName = fieldName;
        iNumberValue = value;
        iStringValue = null;
        iLowerBound = lowerBound;
        iUpperBound = upperBound;
    }

    /**
     * @param fieldType type of field being set
     * @param value illegal value being set
     */
    public IllegalFieldValueException(DateTimeFieldType fieldType, String value) {
        super(createMessage(fieldType.getName(), value));
        iDateTimeFieldType = fieldType;
        iDurationFieldType = null;
        iFieldName = fieldType.getName();
        iStringValue = value;
        iNumberValue = null;
        iLowerBound = null;
        iUpperBound = null;
    }

    /**
     * @param fieldType type of field being set
     * @param value illegal value being set
     */
    public IllegalFieldValueException(DurationFieldType fieldType, String value) {
        super(createMessage(fieldType.getName(), value));
        iDateTimeFieldType = null;
        iDurationFieldType = fieldType;
        iFieldName = fieldType.getName();
        iStringValue = value;
        iNumberValue = null;
        iLowerBound = null;
        iUpperBound = null;
    }

    /**
     * @param fieldName name of field being set
     * @param value illegal value being set
     */
    public IllegalFieldValueException(String fieldName, String value) {
        super(createMessage(fieldName, value));
        iDateTimeFieldType = null;
        iDurationFieldType = null;
        iFieldName = fieldName;
        iStringValue = value;
        iNumberValue = null;
        iLowerBound = null;
        iUpperBound = null;
    }

    /**
     * Returns the DateTimeFieldType whose value was invalid, or null if not applicable.
     */
    public DateTimeFieldType getDateTimeFieldType() {
        return iDateTimeFieldType;
    }

    /**
     * Returns the DurationFieldType whose value was invalid, or null if not applicable.
     */
    public DurationFieldType getDurationFieldType() {
        return iDurationFieldType;
    }

    /**
     * Returns the name of the field whose value was invalid.
     */
    public String getFieldName() {
        return iFieldName;
    }

    /**
     * Returns the illegal integer value assigned to the field, or null if not applicable.
     */
    public Number getIllegalNumberValue() {
        return iNumberValue;
    }

    /**
     * Returns the illegal string value assigned to the field, or null if not applicable.
     */
    public String getIllegalStringValue() {
        return iStringValue;
    }

    /**
     * Returns the illegal value assigned to the field as a non-null string.
     */
    public String getIllegalValueAsString() {
        String value = iStringValue;
        if (value == null) {
            value = String.valueOf(iNumberValue);
        }
        return value;
    }

    /**
     * Returns the lower bound of the legal value range, or null if not applicable.
     */
    public Number getLowerBound() {
        return iLowerBound;
    }

    /**
     * Returns the upper bound of the legal value range, or null if not applicable.
     */
    public Number getUpperBound() {
        return iUpperBound;
    }
}
