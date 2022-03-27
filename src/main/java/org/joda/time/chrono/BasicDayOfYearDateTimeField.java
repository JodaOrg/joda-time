/*
 *  Copyright 2001-2014 Stephen Colebourne
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
 * Provides time calculations for the day of the year component of time.
 *
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.1, refactored from GJDayOfYearDateTimeField
 */
final class BasicDayOfYearDateTimeField extends PreciseDurationDateTimeField {

    @SuppressWarnings("unused")
    private static final long serialVersionUID = -6821236822336841037L;

    private final BasicChronology iChronology;

    /**
     * Restricted constructor
     */
    BasicDayOfYearDateTimeField(BasicChronology chronology, DurationField days) {
        super(DateTimeFieldType.dayOfYear(), days);
        iChronology = chronology;
    }

    /**
     * Get the day of the year component of the specified time instant.
     * 
     * @param instant  the time instant in millis to query.
     * @return the day of the year extracted from the input.
     */
    @Override
    public int get(long instant) {
        return iChronology.getDayOfYear(instant);
    }

    @Override
    public DurationField getRangeDurationField() {
        return iChronology.years();
    }

    @Override
    public int getMinimumValue() {
        return 1;
    }

    @Override
    public int getMaximumValue() {
        return iChronology.getDaysInYearMax();
    }

    @Override
    public int getMaximumValue(long instant) {
        int year = iChronology.getYear(instant);
        return iChronology.getDaysInYear(year);
    }

    @Override
    public int getMaximumValue(ReadablePartial partial) {
        if (partial.isSupported(DateTimeFieldType.year())) {
            int year = partial.get(DateTimeFieldType.year());
            return iChronology.getDaysInYear(year);
        }
        return iChronology.getDaysInYearMax();
    }

    @Override
    public int getMaximumValue(ReadablePartial partial, int[] values) {
        int size = partial.size();
        for (int i = 0; i < size; i++) {
            if (partial.getFieldType(i) == DateTimeFieldType.year()) {
                int year = values[i];
                return iChronology.getDaysInYear(year);
            }
        }
        return iChronology.getDaysInYearMax();
    }

    @Override
    protected int getMaximumValueForSet(long instant, int value) {
        int maxLessOne = iChronology.getDaysInYearMax() - 1;
        return (value > maxLessOne || value < 1) ? getMaximumValue(instant) : maxLessOne;
    }

    @Override
    public boolean isLeap(long instant) {
        return iChronology.isLeapDay(instant);
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return iChronology.dayOfYear();
    }
}
