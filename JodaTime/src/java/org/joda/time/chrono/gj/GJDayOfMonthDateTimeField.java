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
 * Provides time calculations for the day of the month component of time.
 *
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
final class GJDayOfMonthDateTimeField extends DateTimeField {

    private final ProlepticChronology iChronology;

    /**
     * Restricted constructor
     */
    GJDayOfMonthDateTimeField(ProlepticChronology chronology) {
        super("dayOfMonth");
        iChronology = chronology;
    }

    /**
     * Get the day of the month component of the specified time instant.
     * 
     * @param millis  the time instant in millis to query.
     * @return the day of the month extracted from the input.
     */
    public int get(long millis) {
        return iChronology.getDayOfMonth(millis);
    }

    /**
     * Add the specified day of the month to the specified time instant.
     * The amount added may be negative.
     * 
     * @param millis  the time instant in millis to update.
     * @param days  the days to add to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long millis, int days) {
        return millis + days * (long)DateTimeConstants.MILLIS_PER_DAY;
    }

    public long add(long millis, long days) {
        return millis + days * DateTimeConstants.MILLIS_PER_DAY;
    }

    /**
     * Add to the day of the month component of the specified time instant
     * wrapping around within that component if necessary.
     * The amount added may be negative.
     * 
     * @param millis  the time instant in millis to update.
     * @param days  the days to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapped(long millis, int days) {
        // This method deviates from the normal logic found in
        // concrete subclasses of DateTimeField.
        // This is because the maximum allowed day for a given
        // month must be calculated at run time.
        int thisYear = iChronology.year().get(millis);
        int thisMonth = iChronology.getMonthOfYear(millis, thisYear);
        int thisDom = iChronology.getDayOfMonth(millis, thisYear, thisMonth);
        int wrappedDom = getWrappedValue
            (thisDom, days, 1, iChronology.getDaysInYearMonth(thisYear, thisMonth));
        return set(millis, wrappedDom);
    }

    public long getDifference(long minuendMillis, long subtrahendMillis) {
        return (minuendMillis - subtrahendMillis) / DateTimeConstants.MILLIS_PER_DAY;
    }

    /**
     * Set the day of the month component of the specified time instant.
     * 
     * @param millis  the time instant in millis to update.
     * @param year  the day (1,28-31) to update the time to.
     * @return the updated time instant.
     * @throws IllegalArgumentException  if day is is invalid for this year and month.
     */
    public long set(long millis, int day) {
        int thisYear = iChronology.year().get(millis);
        int thisMonth = iChronology.getMonthOfYear(millis, thisYear);
        verifyValueBounds(day, 1, iChronology.getDaysInYearMonth(thisYear, thisMonth));
        int thisDom = iChronology.getDayOfMonth(millis, thisYear, thisMonth);
        return millis + (day - thisDom) * (long)DateTimeConstants.MILLIS_PER_DAY;
    }

    public long getUnitMillis() {
        return DateTimeConstants.MILLIS_PER_DAY;
    }

    public long getRangeMillis() {
        return iChronology.getRoughMillisPerMonth();
    }

    public int getMinimumValue() {
        return 1;
    }

    public int getMaximumValue() {
        return 31;
    }

    public int getMaximumValue(long millis) {
        int thisYear = iChronology.year().get(millis);
        int thisMonth = iChronology.getMonthOfYear(millis, thisYear);
        return iChronology.getDaysInYearMonth(thisYear, thisMonth);
    }

    public long roundFloor(long millis) {
        if (millis >= 0) {
            return millis - millis % DateTimeConstants.MILLIS_PER_DAY;
        } else {
            millis += 1;
            return millis - millis % DateTimeConstants.MILLIS_PER_DAY - DateTimeConstants.MILLIS_PER_DAY;
        }
    }

    public long roundCeiling(long millis) {
        if (millis >= 0) {
            millis -= 1;
            return millis - millis % DateTimeConstants.MILLIS_PER_DAY + DateTimeConstants.MILLIS_PER_DAY;
        } else {
            return millis - millis % DateTimeConstants.MILLIS_PER_DAY;
        }
    }

    public long remainder(long millis) {
        if (millis >= 0) {
            return millis % DateTimeConstants.MILLIS_PER_DAY;
        } else {
            return (millis + 1) % DateTimeConstants.MILLIS_PER_DAY + DateTimeConstants.MILLIS_PER_DAY - 1;
        }
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return iChronology.dayOfMonth();
    }
}
