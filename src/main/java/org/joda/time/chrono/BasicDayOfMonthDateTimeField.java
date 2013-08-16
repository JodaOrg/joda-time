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
package org.joda.time.chrono;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;
import org.joda.time.field.PreciseDurationDateTimeField;

/**
 * Provides time calculations for the day of the month component of time.
 *
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.1, refactored from GJDayOfMonthDateTimeField
 */
final class BasicDayOfMonthDateTimeField extends PreciseDurationDateTimeField {

    @SuppressWarnings("unused")
    private static final long serialVersionUID = -4677223814028011723L;

    private final BasicChronology iChronology;

    /**
     * Restricted constructor.
     */
    BasicDayOfMonthDateTimeField(BasicChronology chronology, DurationField days) {
        super(DateTimeFieldType.dayOfMonth(), days);
        iChronology = chronology;
    }

    //-----------------------------------------------------------------------
    public int get(long instant) {
        return iChronology.getDayOfMonth(instant);
    }

    public DurationField getRangeDurationField() {
        return iChronology.months();
    }

    public int getMinimumValue() {
        return 1;
    }

    public int getMaximumValue() {
        return iChronology.getDaysInMonthMax();
    }

    public int getMaximumValue(long instant) {
        return iChronology.getDaysInMonthMax(instant);
    }

    public int getMaximumValue(ReadablePartial partial) {
        if (partial.isSupported(DateTimeFieldType.monthOfYear())) {
            int month = partial.get(DateTimeFieldType.monthOfYear());
            if (partial.isSupported(DateTimeFieldType.year())) {
                int year = partial.get(DateTimeFieldType.year());
                return iChronology.getDaysInYearMonth(year, month);
            }
            return iChronology.getDaysInMonthMax(month);
        }
        return getMaximumValue();
    }

    public int getMaximumValue(ReadablePartial partial, int[] values) {
        int size = partial.size();
        for (int i = 0; i < size; i++) {
            if (partial.getFieldType(i) == DateTimeFieldType.monthOfYear()) {
                int month = values[i];
                for (int j = 0; j < size; j++) {
                    if (partial.getFieldType(j) == DateTimeFieldType.year()) {
                        int year = values[j];
                        return iChronology.getDaysInYearMonth(year, month);
                    }
                }
                return iChronology.getDaysInMonthMax(month);
            }
        }
        return getMaximumValue();
    }

    protected int getMaximumValueForSet(long instant, int value) {
        return iChronology.getDaysInMonthMaxForSet(instant, value);
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return iChronology.dayOfMonth();
    }
}
