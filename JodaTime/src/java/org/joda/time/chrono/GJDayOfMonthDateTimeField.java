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
 * @since 1.0
 */
final class GJDayOfMonthDateTimeField extends PreciseDurationDateTimeField {

    private static final long serialVersionUID = -4677223814028011723L;

    private final BaseGJChronology iChronology;

    /**
     * Restricted constructor.
     */
    GJDayOfMonthDateTimeField(BaseGJChronology chronology, DurationField days) {
        super(DateTimeFieldType.dayOfMonth(), days);
        iChronology = chronology;
    }

    /**
     * Get the day of the month component of the specified time instant.
     * 
     * @param instant  the time instant in millis to query.
     * @return the day of the month extracted from the input.
     */
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
        return 31;
    }

    public int getMaximumValue(long instant) {
        int thisYear = iChronology.getYear(instant);
        int thisMonth = iChronology.getMonthOfYear(instant, thisYear);
        return iChronology.getDaysInYearMonth(thisYear, thisMonth);
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
        return 31;
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
        return 31;
    }

    protected int getMaximumValueForSet(long instant, int value) {
        return value > 28 ? getMaximumValue(instant) : 28;
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return iChronology.dayOfMonth();
    }
}
