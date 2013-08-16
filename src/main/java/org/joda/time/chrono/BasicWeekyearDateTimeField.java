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
import org.joda.time.DurationField;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;

/**
 * Provides time calculations for the week of the weekyear component of time.
 *
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.1, refactored from GJWeekyearDateTimeField
 */
final class BasicWeekyearDateTimeField extends ImpreciseDateTimeField {
    
    @SuppressWarnings("unused")
    private static final long serialVersionUID = 6215066916806820644L;

    private static final long WEEK_53 = (53L - 1) * DateTimeConstants.MILLIS_PER_WEEK;

    private final BasicChronology iChronology;

    /**
     * Restricted constructor
     */
    BasicWeekyearDateTimeField(BasicChronology chronology) {
        super(DateTimeFieldType.weekyear(), chronology.getAverageMillisPerYear());
        iChronology = chronology;
    }

    public boolean isLenient() {
        return false;
    }

    /**
     * Get the Year of a week based year component of the specified time instant.
     * 
     * @see org.joda.time.DateTimeField#get
     * @param instant  the time instant in millis to query.
     * @return the year extracted from the input.
     */
    public int get(long instant) {
        return iChronology.getWeekyear(instant);
    }

    /**
     * Add the specified years to the specified time instant.
     * 
     * @see org.joda.time.DateTimeField#add
     * @param instant  the time instant in millis to update.
     * @param years  the years to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long instant, int years) {
        if (years == 0) {
            return instant;
        }
        return set(instant, get(instant) + years);
    }

    public long add(long instant, long value) {
        return add(instant, FieldUtils.safeToInt(value));
    }

    /**
     * Add to the year component of the specified time instant
     * wrapping around within that component if necessary.
     * 
     * @see org.joda.time.DateTimeField#addWrapField
     * @param instant  the time instant in millis to update.
     * @param years  the years to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapField(long instant, int years) {
        return add(instant, years);
    }

    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        if (minuendInstant < subtrahendInstant) {
            return -getDifference(subtrahendInstant, minuendInstant);
        }

        int minuendWeekyear = get(minuendInstant);
        int subtrahendWeekyear = get(subtrahendInstant);

        long minuendRem = remainder(minuendInstant);
        long subtrahendRem = remainder(subtrahendInstant);

        // Balance leap weekyear differences on remainders.
        if (subtrahendRem >= WEEK_53 && iChronology.getWeeksInYear(minuendWeekyear) <= 52) {
            subtrahendRem -= DateTimeConstants.MILLIS_PER_WEEK;
        }

        int difference = minuendWeekyear - subtrahendWeekyear;
        if (minuendRem < subtrahendRem) {
            difference--;
        }
        return difference;
    }

    /**
     * Set the Year of a week based year component of the specified time instant.
     *
     * @see org.joda.time.DateTimeField#set
     * @param instant  the time instant in millis to update.
     * @param year  the year (-9999,9999) to set the date to.
     * @return the updated DateTime.
     * @throws IllegalArgumentException  if year is invalid.
     */
    public long set(long instant, int year) {
        FieldUtils.verifyValueBounds(this, Math.abs(year),
                                     iChronology.getMinYear(), iChronology.getMaxYear());
        //
        // Do nothing if no real change is requested.
        //
        int thisWeekyear = get( instant );
        if ( thisWeekyear == year ) {
            return instant;
        }
        //
        // Calculate the DayOfWeek (to be preserved).
        //
        int thisDow = iChronology.getDayOfWeek(instant);
        //
        // Calculate the maximum weeks in the target year.
        //
        int weeksInFromYear = iChronology.getWeeksInYear( thisWeekyear );
        int weeksInToYear = iChronology.getWeeksInYear( year );
        int maxOutWeeks = (weeksInToYear < weeksInFromYear) ?
            weeksInToYear : weeksInFromYear;
        //
        // Get the current week of the year. This will be preserved in
        // the output unless it is greater than the maximum possible
        // for the target weekyear.  In that case it is adjusted
        // to the maximum possible.
        //
        int setToWeek = iChronology.getWeekOfWeekyear(instant);
        if ( setToWeek > maxOutWeeks ) {
            setToWeek = maxOutWeeks;
        }
        //
        // Get a wroking copy of the current date-time.
        // This can be a convenience for debugging.
        //
        long workInstant = instant; // Get a copy
        //
        // Attempt to get close to the proper weekyear.
        // Note - we cannot currently call ourself, so we just call
        // set for the year.  This at least gets us close.
        //
        workInstant = iChronology.setYear( workInstant, year );
        //
        // Calculate the weekyear number for the get close to value
        // (which might not be equal to the year just set).
        //
        int workWoyYear = get( workInstant );

        //
        // At most we are off by one year, which can be "fixed" by
        // adding/subtracting a week.
        //
        if ( workWoyYear < year ) {
            workInstant += DateTimeConstants.MILLIS_PER_WEEK;
        } else if ( workWoyYear > year ) {
            workInstant -= DateTimeConstants.MILLIS_PER_WEEK;
        }
        //
        // Set the proper week in the current weekyear.
        //

        // BEGIN: possible set WeekOfWeekyear logic.
        int currentWoyWeek = iChronology.getWeekOfWeekyear(workInstant);
        // No range check required (we already know it is OK).
        workInstant = workInstant + (setToWeek - currentWoyWeek)
            * (long)DateTimeConstants.MILLIS_PER_WEEK;
        // END: possible set WeekOfWeekyear logic.

        //
        // Reset DayOfWeek to previous value.
        //
        // Note: This works fine, but it ideally shouldn't invoke other
        // fields from within a field.
        workInstant = iChronology.dayOfWeek().set( workInstant, thisDow );
        //
        // Return result.
        //
        return workInstant;
    }

    public DurationField getRangeDurationField() {
        return null;
    }

    public boolean isLeap(long instant) {
        return iChronology.getWeeksInYear(iChronology.getWeekyear(instant)) > 52;
    }

    public int getLeapAmount(long instant) {
        return iChronology.getWeeksInYear(iChronology.getWeekyear(instant)) - 52;
    }

    public DurationField getLeapDurationField() {
        return iChronology.weeks();
    }

    public int getMinimumValue() {
        return iChronology.getMinYear();
    }

    public int getMaximumValue() {
        return iChronology.getMaxYear();
    }

    public long roundFloor(long instant) {
        // Note: This works fine, but it ideally shouldn't invoke other
        // fields from within a field.
        instant = iChronology.weekOfWeekyear().roundFloor(instant);
        int wow = iChronology.getWeekOfWeekyear(instant);
        if (wow > 1) {
            instant -= ((long) DateTimeConstants.MILLIS_PER_WEEK) * (wow - 1);
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
        return iChronology.weekyear();
    }
}
