/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2004 Stephen Colebourne.  
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
package org.joda.time.chrono;

import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.field.ImpreciseDateTimeField;
import org.joda.time.field.FieldUtils;

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

    private final AbstractGJChronology iChronology;

    /**
     * Restricted constructor
     */
    GJYearDateTimeField(AbstractGJChronology chronology) {
        super("year", "years", chronology.getAverageMillisPerYear());
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
