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

import java.util.Locale;

import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;

/**
 * Provides time calculations for the month of the year component of time.
 *
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @version 1.0
 * @since 1.0
 */
final class GJMonthOfYearDateTimeField extends ImpreciseDateTimeField {

    /** Serialization version */
    private static final long serialVersionUID = -4748157875845286249L;

    private static final int MIN = DateTimeConstants.JANUARY;
    private static final int MAX = DateTimeConstants.DECEMBER;

    private final AbstractGJChronology iChronology;

    /**
     * Restricted constructor
     */
    GJMonthOfYearDateTimeField(AbstractGJChronology chronology) {
        super("monthOfYear", "months", chronology.getAverageMillisPerMonth());
        iChronology = chronology;
    }

    public boolean isLenient() {
        return false;
    }

    /**
     * Get the Month component of the specified time instant.
     *
     * @see org.joda.time.DateTimeField#get(long)
     * @see org.joda.time.ReadableDateTime#getMonthOfYear()
     * @param instant  the time instant in millis to query.
     * @return the month extracted from the input.
     */
    public int get(long instant) {
        return iChronology.getMonthOfYear(instant);
    }

    protected String getAsText(int fieldValue, Locale locale) {
        return GJLocaleSymbols.forLocale(locale).monthOfYearValueToText(fieldValue);
    }

    protected String getAsShortText(int fieldValue, Locale locale) {
        return GJLocaleSymbols.forLocale(locale).monthOfYearValueToShortText(fieldValue);
    }

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
        int yearToUse;
        // Initially, monthToUse is zero-based
        int monthToUse = thisMonth - 1 + months;
        if (monthToUse >= 0) {
            yearToUse = thisYear + (monthToUse / MAX);
            monthToUse = (monthToUse % MAX) + 1;
        } else {
            yearToUse = thisYear + (monthToUse / MAX) - 1;
            monthToUse = Math.abs(monthToUse);
            int remMonthToUse = monthToUse % MAX;
            // Take care of the boundary condition
            if (remMonthToUse == 0) {
                remMonthToUse = MAX;
            }
            monthToUse = MAX - remMonthToUse + 1;
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
            yearToUse = thisYear + (monthToUse / MAX);
            monthToUse = (monthToUse % MAX) + 1;
        } else {
            yearToUse = thisYear + (monthToUse / MAX) - 1;
            monthToUse = Math.abs(monthToUse);
            int remMonthToUse = (int)(monthToUse % MAX);
            if (remMonthToUse == 0) {
                remMonthToUse = MAX;
            }
            monthToUse = MAX - remMonthToUse + 1;
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

    /**
     * Add to the Month component of the specified time instant
     * wrapping around within that component if necessary.
     * 
     * @see org.joda.time.DateTimeField#addWrapped
     * @see org.joda.time.ReadWritableDateTime#addWrappedMonthOfYear(int)
     * @param instant  the time instant in millis to update.
     * @param months  the months to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapped(long instant, int months) {
        return set(instant, FieldUtils.getWrappedValue(get(instant), months, MIN, MAX));
    }

    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        if (minuendInstant < subtrahendInstant) {
            return -getDifference(subtrahendInstant, minuendInstant);
        }

        int minuendYear = iChronology.getYear(minuendInstant);
        int minuendMonth = iChronology.getMonthOfYear(minuendInstant, minuendYear);
        int subtrahendYear = iChronology.getYear(subtrahendInstant);
        int subtrahendMonth = iChronology.getMonthOfYear(subtrahendInstant, subtrahendYear);

        long difference = (minuendYear - subtrahendYear) * 12L + minuendMonth - subtrahendMonth;

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
                // TODO: Don't depend on other fields
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
    public long set(long instant, int month) {
        FieldUtils.verifyValueBounds(this, month, MIN, MAX);
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

    public long set(long instant, String text, Locale locale) {
        return set(instant, GJLocaleSymbols.forLocale(locale).monthOfYearTextToValue(text));
    }

    public DurationField getRangeDurationField() {
        return iChronology.years();
    }

    public boolean isLeap(long instant) {
        int thisYear = iChronology.getYear(instant);
        int thisMonth = iChronology.getMonthOfYear(instant, thisYear);
        if (thisMonth != 2) {
            return false;
        } else {
            return 29 == iChronology.getDaysInYearMonth(thisYear, thisMonth);
        }
    }

    public int getLeapAmount(long instant) {
        return isLeap(instant) ? 1 : 0;
    }

    public DurationField getLeapDurationField() {
        return iChronology.days();
    }

    public int getMinimumValue() {
        return MIN;
    }

    public int getMaximumValue() {
        return MAX;
    }

    public int getMaximumTextLength(Locale locale) {
        return GJLocaleSymbols.forLocale(locale).getMonthMaxTextLength();
    }

    public int getMaximumShortTextLength(Locale locale) {
        return GJLocaleSymbols.forLocale(locale).getMonthMaxShortTextLength();
    }

    public long roundFloor(long instant) {
        int year = iChronology.getYear(instant);
        int month = iChronology.getMonthOfYear(instant, year);
        return iChronology.getYearMonthMillis(year, month);
    }

    public long remainder(long instant) {
        return instant - roundFloor(instant);
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return iChronology.monthOfYear();
    }
}
