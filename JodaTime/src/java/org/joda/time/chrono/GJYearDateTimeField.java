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

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;

/**
 * Provides time calculations for the year component of time.
 *
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
final class GJYearDateTimeField extends ImpreciseDateTimeField {

    private static final long serialVersionUID = -679076949530018869L;

    private static final long FEB_29 = (31L + 29 - 1) * DateTimeConstants.MILLIS_PER_DAY;

    private final BaseGJChronology iChronology;

    /**
     * Restricted constructor
     */
    GJYearDateTimeField(BaseGJChronology chronology) {
        super(DateTimeFieldType.year(), chronology.getAverageMillisPerYear());
        iChronology = chronology;
    }

    public boolean isLenient() {
        return false;
    }

    /**
     * Get the Year component of the specified time instant.
     * 
     * @param instant  the time instant in millis to query.
     * @return the year extracted from the input.
     */
    public int get(long instant) {
        return iChronology.getYear(instant);
    }

    /**
     * Add the specified year to the specified time instant.
     * The amount added may be negative.
     * 
     * @param instant  the time instant in millis to update.
     * @param years  the years to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long instant, int years) {
        if (years == 0) {
            return instant;
        }
        int thisYear = get(instant);
        int newYear = thisYear + years;
        return set(instant, newYear);
    }

    public long add(long instant, long years) {
        return add(instant, FieldUtils.safeToInt(years));
    }

    /**
     * Add to the Year component of the specified time instant
     * wrapping around within that component if necessary.
     * 
     * @param instant  the time instant in millis to update.
     * @param years  the years to add (can be negative).
     * @return the updated time instant.
     */
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

        // Balance leap year differences on remainders.
        if (subtrahendRem >= FEB_29) {
            if (iChronology.isLeapYear(subtrahendYear)) {
                if (!iChronology.isLeapYear(minuendYear)) {
                    subtrahendRem -= DateTimeConstants.MILLIS_PER_DAY;
                }
            } else if (minuendRem >= FEB_29 && iChronology.isLeapYear(minuendYear)) {
                minuendRem -= DateTimeConstants.MILLIS_PER_DAY;
            }
        }

        int difference = minuendYear - subtrahendYear;
        if (minuendRem < subtrahendRem) {
            difference--;
        }
        return difference;
    }

    /**
     * Set the Year component of the specified time instant.
     * 
     * @param instant  the time instant in millis to update.
     * @param year  the year (-292269055,292278994) to update the time to.
     * @return the updated time instant.
     * @throws IllegalArgumentException  if year is invalid.
     */
    public long set(long instant, int year) {
        FieldUtils.verifyValueBounds
            (this, year, iChronology.getMinYear(), iChronology.getMaxYear());
        return iChronology.setYear(instant, year);
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
