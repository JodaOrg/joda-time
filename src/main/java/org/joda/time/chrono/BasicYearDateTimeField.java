/*
 *  Copyright 2001-2015 Stephen Colebourne
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
 * A year field suitable for many calendars.
 *
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.1, refactored from GJYearDateTimeField
 */
class BasicYearDateTimeField extends ImpreciseDateTimeField {

    /** Serialization version. */
    @SuppressWarnings("unused")
    private static final long serialVersionUID = -98628754872287L;

    /** The underlying basic chronology. */
    protected final BasicChronology iChronology;

    /**
     * Restricted constructor.
     * 
     * @param chronology  the chronology this field belongs to
     */
    BasicYearDateTimeField(BasicChronology chronology) {
        super(DateTimeFieldType.year(), chronology.getAverageMillisPerYear());
        iChronology = chronology;
    }

    @Override
    public boolean isLenient() {
        return false;
    }

    @Override
    public int get(long instant) {
        return iChronology.getYear(instant);
    }

    @Override
    public long add(long instant, int years) {
        if (years == 0) {
            return instant;
        }
        int thisYear = get(instant);
        int newYear = FieldUtils.safeAdd(thisYear, years);
        return set(instant, newYear);
    }

    @Override
    public long add(long instant, long years) {
        return add(instant, FieldUtils.safeToInt(years));
    }

    @Override
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

    @Override
    public long set(long instant, int year) {
        FieldUtils.verifyValueBounds
            (this, year, iChronology.getMinYear(), iChronology.getMaxYear());
        return iChronology.setYear(instant, year);
    }

    @Override
    public long setExtended(long instant, int year) {
        FieldUtils.verifyValueBounds(
                this, year, iChronology.getMinYear() - 1, iChronology.getMaxYear() + 1);
        return iChronology.setYear(instant, year);
    }

    @Override
    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        if (minuendInstant < subtrahendInstant) {
            return -iChronology.getYearDifference(subtrahendInstant, minuendInstant);
        }
        return iChronology.getYearDifference(minuendInstant, subtrahendInstant);
    }

    @Override
    public DurationField getRangeDurationField() {
        return null;
    }

    @Override
    public boolean isLeap(long instant) {
        return iChronology.isLeapYear(get(instant));
    }

    @Override
    public int getLeapAmount(long instant) {
        if (iChronology.isLeapYear(get(instant))) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public DurationField getLeapDurationField() {
        return iChronology.days();
    }

    @Override
    public int getMinimumValue() {
        return iChronology.getMinYear();
    }

    @Override
    public int getMaximumValue() {
        return iChronology.getMaxYear();
    }

    @Override
    public long roundFloor(long instant) {
        return iChronology.getYearMillis(get(instant));
    }

    @Override
    public long roundCeiling(long instant) {
        int year = get(instant);
        long yearStartMillis = iChronology.getYearMillis(year);
        if (instant != yearStartMillis) {
            // Bump up to start of next year.
            instant = iChronology.getYearMillis(year + 1);
        }
        return instant;
    }

    @Override
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
