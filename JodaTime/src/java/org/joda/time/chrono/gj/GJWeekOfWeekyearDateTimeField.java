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
 * Provides time calculations for the week of a week based year component of time.
 *
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @version 1.0
 * @since 1.0
 */
final class GJWeekOfWeekyearDateTimeField extends DateTimeField {

    private static final int MIN = 1;
    private static final int MAX = 53;

    private final ProlepticChronology iChronology;

    /**
     * Restricted constructor
     */
    GJWeekOfWeekyearDateTimeField(ProlepticChronology chronology) {
        super("weekOfWeekyear");
        iChronology = chronology;
    }

    /**
     * Get the week of a week based year component of the specified time instant.
     * 
     * @see org.joda.time.DateTimeField#get(long)
     * @param millis  the time instant in millis to query.
     * @return the week of the year extracted from the input.
     */
    public int get(long millis) {
        int year = iChronology.year().get(millis);
        //
        long firstWeekMillis1 = iChronology.getFirstWeekOfYearMillis(year);
        if (millis < firstWeekMillis1) {
            return iChronology.getWeeksInYear(year - 1);
        }
        long firstWeekMillis2 = iChronology.getFirstWeekOfYearMillis(year + 1);
        if (millis >= firstWeekMillis2) {
            return 1;
        }
        return (int) ((millis - firstWeekMillis1) / DateTimeConstants.MILLIS_PER_WEEK) + 1;
    }

    /**
     * Add the specified weeks to the specified time instant.
     * <p>
     * The amount added may be negative.
     * 
     * @see org.joda.time.DateTimeField#add
     * @param millis  the time instant in millis to update.
     * @param weeks  the weeks to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long millis, int weeks) {
        // Return newly calculated millis value
        return millis + (weeks * (long)DateTimeConstants.MILLIS_PER_WEEK);
    }

    public long add(long millis, long weeks) {
        // Return newly calculated millis value
        return millis + (weeks * DateTimeConstants.MILLIS_PER_WEEK);
    }

    /**
     * Add to the week component of the specified time instant
     * wrapping around within that component if necessary.
     * 
     * @see org.joda.time.DateTimeField#addWrapped
     * @param millis  the time instant in millis to update.
     * @param years  the years to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapped(long millis, int weeks) {
        int thisWeek = get(millis);
        int wrappedWeek = getWrappedValue(thisWeek, weeks, 1, getMaximumValue(millis));
        return millis + (wrappedWeek - thisWeek) * (long)DateTimeConstants.MILLIS_PER_WEEK;
    }

    public long getDifference(long minuendMillis, long subtrahendMillis) {
        return (minuendMillis - subtrahendMillis) / DateTimeConstants.MILLIS_PER_WEEK;
    }

    /**
     * Set the week of a week based year component of the specified time instant.
     * 
     * @see org.joda.time.DateTimeField#set
     * @param millis  the time instant in millis to update.
     * @param week  the week (1,53) to update the time to.
     * @return the updated time instant.
     * @throws IllegalArgumentException  if week is invalid.
     */
    public long set(long millis, int week) {
        verifyValueBounds(week, 1, getMaximumValue(millis));
        int thisWeek = get(millis);
        return millis + (week - thisWeek) * (long)DateTimeConstants.MILLIS_PER_WEEK;
    }

    public long getUnitMillis() {
        return DateTimeConstants.MILLIS_PER_WEEK;
    }

    public long getRangeMillis() {
        return iChronology.getRoughMillisPerYear();
    }

    public int getMinimumValue() {
        return MIN;
    }

    public int getMaximumValue() {
        return MAX;
    }

    public int getMaximumValue(long millis) {
        int thisYear = iChronology.weekyear().get(millis);
        return iChronology.getWeeksInYear(thisYear);
    }

    public long roundFloor(long millis) {
        DateTimeField dowField = iChronology.dayOfWeek();
        millis = dowField.roundFloor(millis);
        int dow = dowField.get(millis);
        if (dow > 1) {
            millis = dowField.add(millis, 1 - dow);
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
        return iChronology.weekOfWeekyear();
    }
}
