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
 * Provides time calculations for the year component of time.
 *
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
final class GJYearDateTimeField extends DateTimeField {

    private static final long FEB_29 = (31L + 29 - 1) * DateTimeConstants.MILLIS_PER_DAY;

    private final ProlepticChronology iChronology;
    private final transient long iRoughMillisPerYear;

    /**
     * Restricted constructor
     */
    GJYearDateTimeField(ProlepticChronology chronology) {
        super("year");
        iChronology = chronology;
        iRoughMillisPerYear = chronology.getRoughMillisPerYear();
    }

    /**
     * Get the Year component of the specified time instant.
     * 
     * @param millis  the time instant in millis to query.
     * @return the year extracted from the input.
     */
    public int get(long millis) {
        // Get an initial estimate of the year, and the millis value
        // that represents the start of that year.
        int year = 1970 + (int) (millis / iRoughMillisPerYear);
        long yearStartMillis = iChronology.getYearMillis(year);

        if (millis > yearStartMillis) {
            for (;;) {
                // Actual year may be greater than what we estimated. Check if
                // year should advance.
                if (iChronology.isLeapYear(year)) {
                    yearStartMillis += DateTimeConstants.MILLIS_PER_DAY * 366L;
                } else {
                    yearStartMillis += DateTimeConstants.MILLIS_PER_DAY * 365L;
                }
                if (millis < yearStartMillis) {
                    // Year was correct, no need to advance.
                    break;
                }
                year++;
                if (millis == yearStartMillis) {
                    // Millis is at start of year; year is now correct, so no
                    // need to check anymore.
                    break;
                }
                if ((millis ^ yearStartMillis) < 0) {
                    // Sign mismatch, operation overflowed.
                    return getOverflow(millis);
                }
            }
        } else if (millis < yearStartMillis) {
            for (;;) {
                // Actual year less than what we estimated. Go to previous year
                // and check.
                year--;
                if (iChronology.isLeapYear(year)) {
                    yearStartMillis -= DateTimeConstants.MILLIS_PER_DAY * 366L;
                } else {
                    yearStartMillis -= DateTimeConstants.MILLIS_PER_DAY * 365L;
                }
                if (millis >= yearStartMillis) {
                    // Year is now correct.
                    break;
                }
                if ((millis ^ yearStartMillis) < 0) {
                    // Sign mismatch, operation overflowed.
                    return getOverflow(millis);
                }
            }
        }

        return year;
    }

    private int getOverflow(long millis) {
        if (millis > 0) {
            int year = iChronology.getMaxYear();
            long yearStartMillis = iChronology.getYearMillis(year);
            if (iChronology.isLeapYear(year)) {
                yearStartMillis += DateTimeConstants.MILLIS_PER_DAY * 366L;
            } else {
                yearStartMillis += DateTimeConstants.MILLIS_PER_DAY * 365L;
            }
            long yearEndMillis = yearStartMillis - 1;

            if (millis <= yearEndMillis) {
                return year;
            }

            throw new IllegalArgumentException
                ("Instant too large: " + millis + " > " + yearEndMillis);
        } else {
            int year = iChronology.getMinYear();
            long yearStartMillis = iChronology.getYearMillis(year);
            if (millis >= yearStartMillis) {
                return year;
            }

            throw new IllegalArgumentException
                ("Instant too small: " + millis + " < " + yearStartMillis);
        }
    }

    /**
     * Add the specified year to the specified time instant.
     * The amount added may be negative.
     * 
     * @param millis  the time instant in millis to update.
     * @param years  the years to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long millis, int years) {
        if (years == 0) {
            return millis;
        }
        int thisYear = get(millis);
        int newYear = thisYear + years;
        return set(millis, newYear);
    }

    public long add(long millis, long years) {
        return addLong(millis, years);
    }

    /**
     * Add to the Year component of the specified time instant
     * wrapping around within that component if necessary.
     * 
     * @param millis  the time instant in millis to update.
     * @param years  the years to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapped(long millis, int years) {
        if (years == 0) {
            return millis;
        }
        // Return newly calculated millis value
        int thisYear = iChronology.year().get(millis);
        int wrappedYear = getWrappedValue
            (thisYear, years, iChronology.getMinYear(), iChronology.getMaxYear());
        return set(millis, wrappedYear);
    }

    public long getDifference(long minuendMillis, long subtrahendMillis) {
        if (minuendMillis < subtrahendMillis) {
            return -getDifference(subtrahendMillis, minuendMillis);
        }

        int minuendYear = get(minuendMillis);
        int subtrahendYear = get(subtrahendMillis);

        // Inlined remainder method to avoid duplicate calls to get.
        long minuendRem = minuendMillis - iChronology.getYearMillis(minuendYear);
        long subtrahendRem = subtrahendMillis - iChronology.getYearMillis(subtrahendYear);

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
     * @param millis  the time instant in millis to update.
     * @param year  the year (-292269055,292278994) to update the time to.
     * @return the updated time instant.
     * @throws IllegalArgumentException  if year is invalid.
     */
    public long set(long millis, int year) {
        super.verifyValueBounds(year, iChronology.getMinYear(), iChronology.getMaxYear());

        int dayOfYear = iChronology.dayOfYear().get(millis);
        int millisOfDay = iChronology.millisOfDay().get(millis);

        if (dayOfYear > (31 + 28)) { // after Feb 28
            if (isLeap(millis)) {
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

        millis = iChronology.getYearMonthDayMillis(year, 1, dayOfYear);
        millis += millisOfDay;

        return millis;
    }

    public boolean isLeap(long millis) {
        return iChronology.isLeapYear(get(millis));
    }

    public int getLeapAmount(long millis) {
        if (iChronology.isLeapYear(get(millis))) {
            return 1;
        } else {
            return 0;
        }
    }

    public long getUnitMillis() {
        return iRoughMillisPerYear;
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
        return iChronology.getYearMillis(get(millis));
    }

    public long roundCeiling(long millis) {
        int year = get(millis);
        long yearStartMillis = iChronology.getYearMillis(year);
        if (millis != yearStartMillis) {
            // Bump up to start of next year.
            millis = iChronology.getYearMillis(year + 1);
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
        return iChronology.year();
    }
}
