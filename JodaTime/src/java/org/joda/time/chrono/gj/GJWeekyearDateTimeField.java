/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-03 Stephen Colebourne.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Joda project (http://www.joda.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "Joda" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact licence@joda.org.
 *
 * 5. Products derived from this software may not be called "Joda",
 *    nor may "Joda" appear in their name, without prior written
 *    permission of the Joda project.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE JODA AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Joda project and was originally
 * created by Stephen Colebourne <scolebourne@joda.org>. For more
 * information on the Joda project, please see <http://www.joda.org/>.
 */
package org.joda.time.chrono.gj;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;

/**
 * Provides time calculations for the week of the weekyear component of time.
 *
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @version 1.0
 * @since 1.0
 * @see org.joda.time.DateTimeField
 */
final class GJWeekyearDateTimeField extends DateTimeField {
    
    private static final long WEEK_53 = (53L - 1) * DateTimeConstants.MILLIS_PER_WEEK;

    private final ProlepticChronology iChronology;

    /**
     * Restricted constructor
     */
    GJWeekyearDateTimeField(ProlepticChronology chronology) {
        super("weekyear");
        iChronology = chronology;
    }

    /**
     * Get the Year of a week based year component of the specified time instant.
     * 
     * @see org.joda.time.DateTimeField#get
     * @param millis  the time instant in millis to query.
     * @return the year extracted from the input.
     */
    public int get(long millis) {
        int week = iChronology.weekOfWeekyear().get(millis);
        if (week == 1) {
            return iChronology.year().get(millis + DateTimeConstants.MILLIS_PER_WEEK);
        } else if (week > 51) {
            return iChronology.year().get(millis - (2 * DateTimeConstants.MILLIS_PER_WEEK));
        } else {
            return iChronology.year().get(millis);
        }
    }

    /**
     * Add the specified years to the specified time instant.
     * 
     * @see org.joda.time.DateTimeField#add
     * @param millis  the time instant in millis to update.
     * @param years  the years to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long millis, int years) {
        if (years == 0) {
            return millis;
        }
        return set(millis, get(millis) + years);
    }

    public long add(long millis, long value) {
        return addLong(millis, value);
    }

    /**
     * Add to the year component of the specified time instant
     * wrapping around within that component if necessary.
     * 
     * @see org.joda.time.DateTimeField#addWrapped
     * @param millis  the time instant in millis to update.
     * @param years  the years to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapped(long millis, int years) {
        return add(millis, years);
    }

    public long getDifference(long minuendMillis, long subtrahendMillis) {
        if (minuendMillis < subtrahendMillis) {
            return -getDifference(subtrahendMillis, minuendMillis);
        }

        int minuendWeekyear = get(minuendMillis);
        int subtrahendWeekyear = get(subtrahendMillis);

        long minuendRem = remainder(minuendMillis);
        long subtrahendRem = remainder(subtrahendMillis);

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
     * @param millis  the time instant in millis to update.
     * @param year  the year (-9999,9999) to set the date to.
     * @return the updated DateTime.
     * @throws IllegalArgumentException  if year is invalid.
     */
    public long set(long millis, int year) {
        super.verifyValueBounds(Math.abs(year),
                                iChronology.getMinYear(), iChronology.getMaxYear());
        //
        // Do nothing if no real change is requested.
        //
        int thisWeekyear = get( millis );
        if ( thisWeekyear == year ) {
            return millis;
        }
        //
        // Calculate the DayOfWeek (to be preserved).
        //
        int thisDow = iChronology.dayOfWeek().get( millis );
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
        int setToWeek = iChronology.weekOfWeekyear().get( millis );
        if ( setToWeek > maxOutWeeks ) {
            setToWeek = maxOutWeeks;
        }
        //
        // Get a wroking copy of the current date-time.
        // This can be a convenience for debugging.
        //
        long workMillis = millis; // Get a copy
        //
        // Attempt to get close to the proper weekyear.
        // Note - we cannot currently call ourself, so we just call
        // set for the year.  This at least gets us close.
        //
        workMillis = iChronology.year().set( workMillis, year );
        //
        // Calculate the weekyear number for the get close to value
        // (which might not be equal to the year just set).
        //
        int workWoyYear = iChronology.weekyear().get( workMillis );

        // *TEMP Debugging
        /*
        MutableDateTime temp = new MutableDateTime(workMillis,
            ISOChronology.getInstance());
        System.out.println("Current mdt value 01: "
            + temp
            + " " + workWoyYear
        );
        System.out.println("->Temp: " + temp.toString()
            + " WOYYr=" + temp.getWeekyear()
            + " WOYWk=" + temp.getWeekOfWeekyear()
            + " DoW=" + temp.getDayOfWeek()
        );
        */

        //
        // At most we are off by one year, which can be "fixed" by
        // adding/subtracting a week.
        //
        if ( workWoyYear < year ) {
            // System.out.println("Year: Adding "+workWoyYear+" "+year);
            workMillis += DateTimeConstants.MILLIS_PER_WEEK;
        } else if ( workWoyYear > year ) {
            // System.out.println("Year: Subing "+workWoyYear+" "+year);
            workMillis -= DateTimeConstants.MILLIS_PER_WEEK;
        }
        //
        // Set the proper week in the current weekyear.
        //

        // BEGIN: possible set WeekOfWeekyear logic.
        int currentWoyWeek = iChronology.weekOfWeekyear().get( workMillis );
        // No range check required (we already know it is OK).
        workMillis = workMillis + (setToWeek - currentWoyWeek)
            * (long)DateTimeConstants.MILLIS_PER_WEEK;
        // END: possible set WeekOfWeekyear logic.

        //
        // Reset DayOfWeek to previous value.
        //
        workMillis = iChronology.dayOfWeek().set( workMillis, thisDow );
        //
        // Return result.
        //
        return workMillis;
    }

    public long getUnitMillis() {
        return iChronology.getRoughMillisPerYear();
    }

    public long getRangeMillis() {
        // Should actually be double this, but that is not possible since Java
        // doesn't support unsigned types.
        return Long.MAX_VALUE;
    }

    public int getMinimumValue() {
        return iChronology.getMinYear();
    }

    public int getMaximumValue() {
        return iChronology.getMaxYear();
    }

    public long roundFloor(long millis) {
        DateTimeField wowField = iChronology.weekOfWeekyear();
        millis = wowField.roundFloor(millis);
        int wow = wowField.get(millis);
        if (wow > 1) {
            millis = wowField.add(millis, 1 - wow);
        }
        return millis;
    }

    public long remainder(long millis) {
        return millis - roundFloor(millis);
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return iChronology.weekyear();
    }
}
