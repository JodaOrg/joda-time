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
 * @author Brian S O'Neill
 * @since 1.0
 */
final class CopticDayOfMonthDateTimeField extends PreciseDurationDateTimeField {

    private static final long serialVersionUID = -5441610762799659434L;

    private final BaseGJChronology iChronology;

    /**
     * Restricted constructor.
     */
    CopticDayOfMonthDateTimeField(BaseGJChronology chronology, DurationField days) {
        super(DateTimeFieldType.dayOfMonth(), days);
        iChronology = chronology;
    }

    public int get(long instant) {
        return (iChronology.getDayOfYear(instant) - 1) % 30 + 1;
    }

    public DurationField getRangeDurationField() {
        return iChronology.months();
    }

    public int getMinimumValue() {
        return 1;
    }

    public int getMaximumValue() {
        return 30;
    }

    public int getMaximumValue(long instant) {
		if (((iChronology.getDayOfYear(instant) - 1) / 30) < 12) {
			return 30;
		}
		return iChronology.isLeapYear(iChronology.getYear(instant)) ? 6 : 5;
    }

    public int getMaximumValue(ReadablePartial partial) {
        if (partial.isSupported(DateTimeFieldType.monthOfYear())) {
            // find month
            int month = partial.get(DateTimeFieldType.monthOfYear());
            if (month <= 12) {
                return 30;
            }
            // 13th month, so check year
            if (partial.isSupported(DateTimeFieldType.year())) {
                int year = partial.get(DateTimeFieldType.year());
                return iChronology.isLeapYear(year) ? 6 : 5;
            }
            return 6;
        }
        return 30;
    }

    public int getMaximumValue(ReadablePartial partial, int[] values) {
        int size = partial.size();
        for (int i = 0; i < size; i++) {
            // find month
            if (partial.getFieldType(i) == DateTimeFieldType.monthOfYear()) {
                int month = values[i];
                if (month <= 12) {
                    return 30;
                }
                // 13th month, so check year
                for (int j = 0; j < size; j++) {
                    if (partial.getFieldType(j) == DateTimeFieldType.year()) {
                        int year = values[j];
                        return iChronology.isLeapYear(year) ? 6 : 5;
                    }
                }
                return 6;
            }
        }
        return 30;
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return iChronology.dayOfMonth();
    }
}
