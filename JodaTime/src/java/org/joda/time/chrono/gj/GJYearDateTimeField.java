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
import org.joda.time.DurationField;
import org.joda.time.chrono.ImpreciseDateTimeField;
import org.joda.time.chrono.Utils;

/**
 * Provides time calculations for the year component of time.
 *
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
final class GJYearDateTimeField extends ImpreciseDateTimeField {

    static final long serialVersionUID = -679076949530018869L;

    private static final long FEB_29 = (31L + 29 - 1) * DateTimeConstants.MILLIS_PER_DAY;

    private final ProlepticChronology iChronology;

    /**
     * Restricted constructor
     */
    GJYearDateTimeField(ProlepticChronology chronology) {
        super("year", "years", chronology.getRoughMillisPerYear());
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
        // Get an initial estimate of the year, and the millis value that
        // represents the start of that year. Then verify estimate and fix if
        // necessary.

        int year;

        long unitMillis = getDurationUnitMillis();
        if (instant >= 0) {
            year = 1970 + (int) (instant / unitMillis);
        } else {
            year = 1970 + (int) ((instant - unitMillis + 1) / unitMillis);
        }

        long yearStart = iChronology.getYearMillis(year);
        if ((yearStart ^ instant) < 0) {
            // Sign mismatch, operation overflowed.
            return getOverflow(instant);
        }

        long diff = instant - yearStart;

        if (diff < 0) {
            // Subtract one year to fix estimate.
            year--;
        } else if (diff >= DateTimeConstants.MILLIS_PER_DAY * 365L) {
            // One year may need to be added to fix estimate.
            long oneYear;
            if (iChronology.isLeapYear(year)) {
                oneYear = DateTimeConstants.MILLIS_PER_DAY * 366L;
            } else {
                oneYear = DateTimeConstants.MILLIS_PER_DAY * 365L;
            }

            yearStart += oneYear;
            if ((yearStart ^ instant) < 0) {
                // Sign mismatch, operation overflowed.
                return getOverflow(instant);
            }

            if (yearStart <= instant) {
                // Didn't go too far, so actually add one year.
                year++;
            }
        }

        return year;
    }

    private int getOverflow(long instant) {
        if (instant > 0) {
            int year = iChronology.getMaxYear();
            long yearStartMillis = iChronology.getYearMillis(year);
            if (iChronology.isLeapYear(year)) {
                yearStartMillis += DateTimeConstants.MILLIS_PER_DAY * 366L;
            } else {
                yearStartMillis += DateTimeConstants.MILLIS_PER_DAY * 365L;
            }
            long yearEndMillis = yearStartMillis - 1;

            if (instant <= yearEndMillis) {
                return year;
            }

            throw new IllegalArgumentException
                ("Instant too large: " + instant + " > " + yearEndMillis);
        } else {
            int year = iChronology.getMinYear();
            long yearStartMillis = iChronology.getYearMillis(year);
            if (instant >= yearStartMillis) {
                return year;
            }

            throw new IllegalArgumentException
                ("Instant too small: " + instant + " < " + yearStartMillis);
        }
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
        return add(instant, Utils.safeToInt(years));
    }

    /**
     * Add to the Year component of the specified time instant
     * wrapping around within that component if necessary.
     * 
     * @param instant  the time instant in millis to update.
     * @param years  the years to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapped(long instant, int years) {
        if (years == 0) {
            return instant;
        }
        // Return newly calculated millis value
        int thisYear = iChronology.year().get(instant);
        int wrappedYear = Utils.getWrappedValue
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
        Utils.verifyValueBounds
            (this, year, iChronology.getMinYear(), iChronology.getMaxYear());

        int dayOfYear = iChronology.dayOfYear().get(instant);
        int millisOfDay = iChronology.millisOfDay().get(instant);

        if (dayOfYear > (31 + 28)) { // after Feb 28
            if (isLeap(instant)) {
                // Old date is Feb 29 or later.
                if (!iChronology.isLeapYear(year)) {
                    // Moving to a non-leap year, Feb 29 does not exist.
                    dayOfYear--;
                }
            } else {
                // Old date is Mar 01 or later.
                if (iChronology.isLeapYear(year)) {
                    // Moving to a leap year, account for Feb 29.
                    dayOfYear++;
                }
            }
        }

        instant = iChronology.getYearMonthDayMillis(year, 1, dayOfYear);
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
