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
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;

/**
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
final class CopticYearDateTimeField extends ImpreciseDateTimeField {

    private static final long serialVersionUID = 8990199361773280783L;

    private final BaseGJChronology iChronology;

    /**
     * Restricted constructor
     */
    CopticYearDateTimeField(BaseGJChronology chronology) {
        super(DateTimeFieldType.year(), chronology.getAverageMillisPerYear());
        iChronology = chronology;
    }

    public boolean isLenient() {
        return false;
    }

    public int get(long instant) {
        return iChronology.getYear(instant);
    }

    public long add(long instant, int years) {
        if (years == 0) {
            return instant;
        }
        return set(instant, get(instant) + years);
    }

    public long add(long instant, long years) {
        return add(instant, FieldUtils.safeToInt(years));
    }

    public long addWrapField(long instant, int years) {
        if (years == 0) {
            return instant;
        }
        // Return newly calculated millis value
        int thisYear = iChronology.getYear(instant);
        int wrappedYear = FieldUtils.getWrappedValue
            (thisYear, years, iChronology.getMinYear(), iChronology.getMaxYear());
        return set(instant, wrappedYear);
    }

    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        if (minuendInstant < subtrahendInstant) {
            return -getDifference(subtrahendInstant, minuendInstant);
        }

        int minuendYear = get(minuendInstant);
        int subtrahendYear = get(subtrahendInstant);

        // Inlined remainder method to avoid duplicate calls to get.
        long minuendRem = minuendInstant - iChronology.getYearMillis(minuendYear);
        long subtrahendRem = subtrahendInstant - iChronology.getYearMillis(subtrahendYear);

        int difference = minuendYear - subtrahendYear;
        if (minuendRem < subtrahendRem) {
            difference--;
        }
        return difference;
    }

    public long set(long instant, int year) {
        FieldUtils.verifyValueBounds
            (this, year, iChronology.getMinYear(), iChronology.getMaxYear());

		BaseGJChronology chrono = iChronology;

        int thisYear = chrono.getYear(instant);
        int dayOfYear = chrono.getDayOfYear(instant, thisYear);
        int millisOfDay = chrono.getMillisOfDay(instant);

        if (dayOfYear > 365) {
			// Current year is leap, and day is leap.
			if (!chrono.isLeapYear(year)) {
				// Moving to a non-leap year, leap day doesn't exist.
				dayOfYear--;
			}
        }

        instant = chrono.getYearMonthDayMillis(year, 1, dayOfYear);
        instant += millisOfDay;

        return instant;
    }

    public DurationField getRangeDurationField() {
        return null;
    }

    public boolean isLeap(long instant) {
        return iChronology.isLeapYear(get(instant));
    }

    public int getLeapAmount(long instant) {
        if (iChronology.isLeapYear(get(instant))) {
            return 1;
        } else {
            return 0;
        }
    }

    public DurationField getLeapDurationField() {
        return iChronology.days();
    }

    public int getMinimumValue() {
        return iChronology.getMinYear();
    }

    public int getMaximumValue() {
        return iChronology.getMaxYear();
    }

    public long roundFloor(long instant) {
        return iChronology.getYearMillis(get(instant));
    }

    public long roundCeiling(long instant) {
        int year = get(instant);
        long yearStartMillis = iChronology.getYearMillis(year);
        if (instant != yearStartMillis) {
            // Bump up to start of next year.
            instant = iChronology.getYearMillis(year + 1);
        }
        return instant;
    }

    public long remainder(long instant) {
        return instant - roundFloor(instant);
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return iChronology.year();
    }
}
