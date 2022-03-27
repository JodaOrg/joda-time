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

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;

/**
 * Provides time calculations for the month of the year component of time.
 *
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.2, refactored from GJMonthOfYearDateTimeField
 */
class BasicMonthOfYearDateTimeField extends ImpreciseDateTimeField {

    /** Serialization version */
    @SuppressWarnings("unused")
    private static final long serialVersionUID = -8258715387168736L;

    private static final int MIN = DateTimeConstants.JANUARY;

    private final BasicChronology iChronology;
    private final int iMax;
    private final int iLeapMonth;

    /**
     * Restricted constructor.
     * 
     * @param leapMonth the month of year that leaps
     */
    BasicMonthOfYearDateTimeField(BasicChronology chronology, int leapMonth) {
        super(DateTimeFieldType.monthOfYear(), chronology.getAverageMillisPerMonth());
        iChronology = chronology;
        iMax = iChronology.getMaxMonth();
        iLeapMonth = leapMonth;
    }

    //-----------------------------------------------------------------------
    @Override
    public boolean isLenient() {
        return false;
    }

    //-----------------------------------------------------------------------
    /**
     * Get the Month component of the specified time instant.
     *
     * @see org.joda.time.DateTimeField#get(long)
     * @see org.joda.time.ReadableDateTime#getMonthOfYear()
     * @param instant  the time instant in millis to query.
     * @return the month extracted from the input.
     */
    @Override
    public int get(long instant) {
        return iChronology.getMonthOfYear(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Add the specified month to the specified time instant.
     * The amount added may be negative.<p>
     * If the new month has less total days than the specified
     * day of the month, this value is coerced to the nearest
     * sane value. e.g.<p>
     * 07-31 - (1 month) = 06-30<p>
     * 03-31 - (1 month) = 02-28 or 02-29 depending<p>
     * 
     * @see org.joda.time.DateTimeField#add
     * @see org.joda.time.ReadWritableDateTime#addMonths(int)
     * @param instant  the time instant in millis to update.
     * @param months  the months to add (can be negative).
     * @return the updated time instant.
     */
    @Override
    public long add(long instant, int months) {
        if (months == 0) {
            return instant; // the easy case
        }
        //
        // Save time part first.
        //
        long timePart = iChronology.getMillisOfDay(instant);
        //
        //
        // Get this year and month.
        //
        int thisYear = iChronology.getYear(instant);
        int thisMonth = iChronology.getMonthOfYear(instant, thisYear);
        // ----------------------------------------------------------
        //
        // Do not refactor without careful consideration.
        // Order of calculation is important.
        //
        int yearToUse = thisYear;;
        // Initially, monthToUse is zero-based
        int monthToUse = thisMonth - 1 + months;
        if (thisMonth > 0 && monthToUse < 0) {
            if (Math.signum(months + iMax) == Math.signum(months)) {
                yearToUse--;
                months += iMax;
            } else {
                yearToUse++;
                months -= iMax;
            }
            monthToUse = thisMonth - 1 + months;
        }
        if (monthToUse >= 0) {
            yearToUse = yearToUse + (monthToUse / iMax);
            monthToUse = (monthToUse % iMax) + 1;
        } else {
            yearToUse = yearToUse + (monthToUse / iMax) - 1;
            monthToUse = Math.abs(monthToUse);
            int remMonthToUse = monthToUse % iMax;
            // Take care of the boundary condition
            if (remMonthToUse == 0) {
                remMonthToUse = iMax;
            }
            monthToUse = iMax - remMonthToUse + 1;
            // Take care of the boundary condition
            if (monthToUse == 1) {
                yearToUse += 1;
            }
        }
        // End of do not refactor.
        // ----------------------------------------------------------

        //
        // Quietly force DOM to nearest sane value.
        //
        int dayToUse = iChronology.getDayOfMonth(instant, thisYear, thisMonth);
        int maxDay = iChronology.getDaysInYearMonth(yearToUse, monthToUse);
        if (dayToUse > maxDay) {
            dayToUse = maxDay;
        }
        //
        // get proper date part, and return result
        //
        long datePart =
            iChronology.getYearMonthDayMillis(yearToUse, monthToUse, dayToUse);
        return datePart + timePart;
    }

    //-----------------------------------------------------------------------
    @Override
    public long add(long instant, long months) {
        int i_months = (int)months;
        if (i_months == months) {
            return add(instant, i_months);
        }

        // Copied from add(long, int) and modified slightly:

        long timePart = iChronology.getMillisOfDay(instant);

        int thisYear = iChronology.getYear(instant);
        int thisMonth = iChronology.getMonthOfYear(instant, thisYear);

        long yearToUse;
        long monthToUse = thisMonth - 1 + months;
        if (monthToUse >= 0) {
            yearToUse = thisYear + (monthToUse / iMax);
            monthToUse = (monthToUse % iMax) + 1;
        } else {
            yearToUse = thisYear + (monthToUse / iMax) - 1;
            monthToUse = Math.abs(monthToUse);
            int remMonthToUse = (int)(monthToUse % iMax);
            if (remMonthToUse == 0) {
                remMonthToUse = iMax;
            }
            monthToUse = iMax - remMonthToUse + 1;
            if (monthToUse == 1) {
                yearToUse += 1;
            }
        }

        if (yearToUse < iChronology.getMinYear() ||
            yearToUse > iChronology.getMaxYear()) {

            throw new IllegalArgumentException
                ("Magnitude of add amount is too large: " + months);
        }

        int i_yearToUse = (int)yearToUse;
        int i_monthToUse = (int)monthToUse;

        int dayToUse = iChronology.getDayOfMonth(instant, thisYear, thisMonth);
        int maxDay = iChronology.getDaysInYearMonth(i_yearToUse, i_monthToUse);
        if (dayToUse > maxDay) {
            dayToUse = maxDay;
        }

        long datePart =
            iChronology.getYearMonthDayMillis(i_yearToUse, i_monthToUse, dayToUse);
        return datePart + timePart;
    }

    //-----------------------------------------------------------------------
    @Override
    public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
        // overridden as superclass algorithm can't handle
        // 2004-02-29 + 48 months -> 2008-02-29 type dates
        if (valueToAdd == 0) {
            return values;
        }
        if (partial.size() > 0 && partial.getFieldType(0).equals(DateTimeFieldType.monthOfYear()) && fieldIndex == 0) {
            // month is largest field and being added to, such as month-day
            int curMonth0 = values[0] - 1;
            int newMonth = ((curMonth0 + (valueToAdd % 12) + 12) % 12) + 1;
            return set(partial, 0, values, newMonth);
        }
        if (DateTimeUtils.isContiguous(partial)) {
            long instant = 0L;
            for (int i = 0, isize = partial.size(); i < isize; i++) {
                instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
            }
            instant = add(instant, valueToAdd);
            return iChronology.get(partial, instant);
        } else {
            return super.add(partial, fieldIndex, values, valueToAdd);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Add to the Month component of the specified time instant
     * wrapping around within that component if necessary.
     * 
     * @see org.joda.time.DateTimeField#addWrapField
     * @param instant  the time instant in millis to update.
     * @param months  the months to add (can be negative).
     * @return the updated time instant.
     */
    @Override
    public long addWrapField(long instant, int months) {
        return set(instant, FieldUtils.getWrappedValue(get(instant), months, MIN, iMax));
    }

    //-----------------------------------------------------------------------
    @Override
    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        if (minuendInstant < subtrahendInstant) {
            return -getDifference(subtrahendInstant, minuendInstant);
        }

        int minuendYear = iChronology.getYear(minuendInstant);
        int minuendMonth = iChronology.getMonthOfYear(minuendInstant, minuendYear);
        int subtrahendYear = iChronology.getYear(subtrahendInstant);
        int subtrahendMonth = iChronology.getMonthOfYear(subtrahendInstant, subtrahendYear);

        long difference = (minuendYear - subtrahendYear) * ((long) iMax) + minuendMonth - subtrahendMonth;

        // Before adjusting for remainder, account for special case of add
        // where the day-of-month is forced to the nearest sane value.
        int minuendDom = iChronology.getDayOfMonth
            (minuendInstant, minuendYear, minuendMonth);
        if (minuendDom == iChronology.getDaysInYearMonth(minuendYear, minuendMonth)) {
            // Last day of the minuend month...
            int subtrahendDom = iChronology.getDayOfMonth
                (subtrahendInstant, subtrahendYear, subtrahendMonth);
            if (subtrahendDom > minuendDom) {
                // ...and day of subtrahend month is larger.
                // Note: This works fine, but it ideally shouldn't invoke other
                // fields from within a field.
                subtrahendInstant = iChronology.dayOfMonth().set(subtrahendInstant, minuendDom);
            }
        }

        // Inlined remainder method to avoid duplicate calls.
        long minuendRem = minuendInstant
            - iChronology.getYearMonthMillis(minuendYear, minuendMonth);
        long subtrahendRem = subtrahendInstant
            - iChronology.getYearMonthMillis(subtrahendYear, subtrahendMonth);

        if (minuendRem < subtrahendRem) {
            difference--;
        }

        return difference;
    }

    //-----------------------------------------------------------------------
    /**
     * Set the Month component of the specified time instant.<p>
     * If the new month has less total days than the specified
     * day of the month, this value is coerced to the nearest
     * sane value. e.g.<p>
     * 07-31 to month 6 = 06-30<p>
     * 03-31 to month 2 = 02-28 or 02-29 depending<p>
     * 
     * @param instant  the time instant in millis to update.
     * @param month  the month (1,12) to update the time to.
     * @return the updated time instant.
     * @throws IllegalArgumentException  if month is invalid
     */
    @Override
    public long set(long instant, int month) {
        FieldUtils.verifyValueBounds(this, month, MIN, iMax);
        //
        int thisYear = iChronology.getYear(instant);
        //
        int thisDom = iChronology.getDayOfMonth(instant, thisYear);
        int maxDom = iChronology.getDaysInYearMonth(thisYear, month);
        if (thisDom > maxDom) {
            // Quietly force DOM to nearest sane value.
            thisDom = maxDom;
        }
        // Return newly calculated millis value
        return iChronology.getYearMonthDayMillis(thisYear, month, thisDom) +
            iChronology.getMillisOfDay(instant);
    }

    //-----------------------------------------------------------------------
    @Override
    public DurationField getRangeDurationField() {
        return iChronology.years();
    }

    //-----------------------------------------------------------------------
    @Override
    public boolean isLeap(long instant) {
        int thisYear = iChronology.getYear(instant);
        if (iChronology.isLeapYear(thisYear)) {
            return (iChronology.getMonthOfYear(instant, thisYear) == iLeapMonth);
        }
        return false;
    }

    //-----------------------------------------------------------------------
    @Override
    public int getLeapAmount(long instant) {
        return isLeap(instant) ? 1 : 0;
    }

    //-----------------------------------------------------------------------
    @Override
    public DurationField getLeapDurationField() {
        return iChronology.days();
    }

    //-----------------------------------------------------------------------
    @Override
    public int getMinimumValue() {
        return MIN;
    }

    //-----------------------------------------------------------------------
    @Override
    public int getMaximumValue() {
        return iMax;
    }

    //-----------------------------------------------------------------------
    @Override
    public long roundFloor(long instant) {
        int year = iChronology.getYear(instant);
        int month = iChronology.getMonthOfYear(instant, year);
        return iChronology.getYearMonthMillis(year, month);
    }

    //-----------------------------------------------------------------------
    @Override
    public long remainder(long instant) {
        return instant - roundFloor(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return iChronology.monthOfYear();
    }
}
