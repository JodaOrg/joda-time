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

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;

/**
 * Wraps another field such that a certain value is added back into
 * the sequence of numbers.
 * <p>
 * This reverses the effect of SkipDateTimeField. This isn't very
 * elegant.
 * <p>
 * SkipUndoDateTimeField is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public final class SkipUndoDateTimeField extends DelegatedDateTimeField {

    /** Serialization version. */
    private static final long serialVersionUID = -5875876968979L;

    /** The chronology to wrap. */
    private final Chronology iChronology;
    /** The value to skip. */
    private final int iSkip;
    /** The calculated minimum value. */
    private transient int iMinValue;

    /**
     * Constructor that reinserts zero.
     * 
     * @param chronology  the chronoogy to use
     * @param field  the field to skip zero on
     */
    public SkipUndoDateTimeField(Chronology chronology, DateTimeField field) {
        this(chronology, field, 0);
    }

    /**
     * Constructor.
     * 
     * @param chronology  the chronoogy to use
     * @param field  the field to skip zero on
     * @param skip  the value to skip
     */
    public SkipUndoDateTimeField(Chronology chronology, DateTimeField field, int skip) {
        super(field);
        iChronology = chronology;
        int min = super.getMinimumValue();
        if (min < skip) {
            iMinValue = min + 1;
        } else if (min == skip + 1) {
            iMinValue = skip;
        } else {
            iMinValue = min;
        }
        iSkip = skip;
    }

    //-----------------------------------------------------------------------
    public int get(long millis) {
        int value = super.get(millis);
        if (value < iSkip) {
            value++;
        }
        return value;
    }

    public long set(long millis, int value) {
        FieldUtils.verifyValueBounds(this, value, iMinValue, getMaximumValue());
        if (value <= iSkip) {
            value--;
        }
        return super.set(millis, value);
    }

    public int getMinimumValue() {
        return iMinValue;
    }

    private Object readResolve() {
        return getType().getField(iChronology);
    }

}
