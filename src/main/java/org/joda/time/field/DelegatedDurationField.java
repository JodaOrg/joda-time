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

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

/**
 * <code>DelegatedDurationField</code> delegates each method call to the
 * duration field it wraps.
 * <p>
 * DelegatedDurationField is thread-safe and immutable, and its subclasses must
 * be as well.
 *
 * @author Brian S O'Neill
 * @see DecoratedDurationField
 * @since 1.0
 */
public class DelegatedDurationField extends DurationField implements Serializable {

    /** Serialization lock. */
    private static final long serialVersionUID = -5576443481242007829L;

    /** The DurationField being wrapped */
    private final DurationField iField;
    /** The field type */
    private final DurationFieldType iType;

    /**
     * Constructor.
     * 
     * @param field  the base field
     */
    protected DelegatedDurationField(DurationField field) {
        this(field, null);
    }

    /**
     * Constructor.
     * 
     * @param field  the base field
     * @param type  the field type to use
     */
    protected DelegatedDurationField(DurationField field, DurationFieldType type) {
        super();
        if (field == null) {
            throw new IllegalArgumentException("The field must not be null");
        }
        iField = field;
        iType = (type == null ? field.getType() : type);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the wrapped duration field.
     * 
     * @return the wrapped DurationField
     */
    public final DurationField getWrappedField() {
        return iField;
    }

    @Override
    public DurationFieldType getType() {
        return iType;
    }

    @Override
    public String getName() {
        return iType.getName();
    }

    /**
     * Returns true if this field is supported.
     */
    @Override
    public boolean isSupported() {
        return iField.isSupported();
    }

    @Override
    public boolean isPrecise() {
        return iField.isPrecise();
    }
    
    @Override
    public int getValue(long duration) {
        return iField.getValue(duration);
    }

    @Override
    public long getValueAsLong(long duration) {
        return iField.getValueAsLong(duration);
    }

    @Override
    public int getValue(long duration, long instant) {
        return iField.getValue(duration, instant);
    }

    @Override
    public long getValueAsLong(long duration, long instant) {
        return iField.getValueAsLong(duration, instant);
    }

    @Override
    public long getMillis(int value) {
        return iField.getMillis(value);
    }

    @Override
    public long getMillis(long value) {
        return iField.getMillis(value);
    }

    @Override
    public long getMillis(int value, long instant) {
        return iField.getMillis(value, instant);
    }

    @Override
    public long getMillis(long value, long instant) {
        return iField.getMillis(value, instant);
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
    public int getDifference(long minuendInstant, long subtrahendInstant) {
        return iField.getDifference(minuendInstant, subtrahendInstant);
    }

    @Override
    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        return iField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Override
    public long getUnitMillis() {
        return iField.getUnitMillis();
    }

    public int compareTo(DurationField durationField) {
        return iField.compareTo(durationField);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DelegatedDurationField) {
            return iField.equals(((DelegatedDurationField) obj).iField);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return iField.hashCode() ^ iType.hashCode();
    }

    @Override
    public String toString() {
        return (iType == null) ? iField.toString() :
            ("DurationField[" + iType + ']');
    }

}
