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
package org.joda.test.time.chrono.gj;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;

/**
 * A reference Julian chronology implementation, intended for testing purposes
 * only. Correctness is favored over performance. The key functions for date
 * calculations are based on ones provided in "Calendrical Calculations", ISBN
 * 0-521-77752-6.
 *
 * @author Brian S O'Neill
 */
public final class TestJulianChronology extends TestGJChronology {

    private static final long JULIAN_EPOCH;

    static {
        // Constant as defined in book.
        JULIAN_EPOCH = new TestGregorianChronology().fixedFromGJ(0, 12, 30);
    }

    /**
     * Constructs with an epoch of 1969-12-19.
     */
    public TestJulianChronology() {
        super(1969, 12, 19);
    }

    public TestJulianChronology(int epochYear, int epochMonth, int epochDay) {
        super(epochYear, epochMonth, epochDay);
    }

    public DateTimeField dayOfMonth() {
        return new TestJulianDayOfMonthField(this); 
    }

    public DateTimeField weekyear() {
        return new TestJulianWeekyearField(this);
    }

    public DateTimeField monthOfYear() {
        return new TestJulianMonthOfYearField(this);
    }

    public DateTimeField year() {
        return new TestJulianYearField(this);
    }

    public String toString() {
        return "TestJulianChronology";
    }

    long millisPerYear() {
        return (long)(365.25 * MILLIS_PER_DAY);
    }

    long millisPerMonth() {
        return (long)(365.25 * MILLIS_PER_DAY / 12);
    }

    boolean isLeapYear(int year) {
        if (year == 0) {
            throw new IllegalArgumentException("Illegal year: " + year);
        }
        return mod(year, 4) == (year > 0 ? 0 : 3);
    }

    /**
     * @return days from 0001-01-01
     */
    long fixedFromGJ(int year, int monthOfYear, int dayOfMonth) {
        if (year == 0) {
            throw new IllegalArgumentException("Illegal year: " + year);
        }
        int y = (year < 0) ? year + 1 : year;
        long y_m1 = y - 1;
        long f = JULIAN_EPOCH - 1 + 365 * y_m1 + div(y_m1, 4)
            + div(367 * monthOfYear - 362, 12) + dayOfMonth;
        if (monthOfYear > 2) {
            f += isLeapYear(year) ? -1 : -2;
        }
        return f;
    }

    /**
     * @param date days from 0001-01-01
     * @return gj year
     */
    int gjYearFromFixed(long date) {
        return gjFromFixed(date)[0];
    }

    /**
     * @param date days from 0001-01-01
     * @return gj year, monthOfYear, dayOfMonth
     */
    int[] gjFromFixed(long date) {
        long approx = div(4 * (date - JULIAN_EPOCH) + 1464, 1461);
        long year = (approx <= 0) ? approx - 1 : approx;
        int year_i = (int)year;
        if (year_i != year) {
            throw new RuntimeException("year cannot be cast to an int: " + year);
        }
        long priorDays = date - fixedFromGJ(year_i, 1, 1);
        long correction;
        if (date < fixedFromGJ(year_i, 3, 1)) {
            correction = 0;
        } else if (isLeapYear(year_i)) {
            correction = 1;
        } else {
            correction = 2;
        }
        int monthOfYear = (int)div(12 * (priorDays + correction) + 373, 367);
        int day = (int)(date - fixedFromGJ(year_i, monthOfYear, 1) + 1);

        return new int[]{year_i, monthOfYear, day};
    }

    long fixedFromISO(int weekyear, int weekOfWeekyear, int dayOfWeek) {
        if (weekyear == 0) {
            throw new IllegalArgumentException("Illegal weekyear: " + weekyear);
        }
        if (weekyear == 1) {
            weekyear = -1;
        } else {
            weekyear--;
        }
        return nthWeekday(weekOfWeekyear, 0, weekyear, 12, 28) + dayOfWeek;
    }

    /**
     * @param date days from 0001-01-01
     * @return iso weekyear, weekOfWeekyear, dayOfWeek (1=Monday to 7)
     */
    int[] isoFromFixed(long date) {
        int weekyear = gjYearFromFixed(date - 3);
        int nextWeekyear;
        if (weekyear == -1) {
            nextWeekyear = 1;
        } else {
            nextWeekyear = weekyear + 1;
        }
        if (date >= fixedFromISO(nextWeekyear, 1, 1)) {
            weekyear = nextWeekyear;
        }
        int weekOfWeekyear = (int)(div(date - fixedFromISO(weekyear, 1, 1), 7) + 1);
        int dayOfWeek = (int)amod(date, 7);
        return new int[]{weekyear, weekOfWeekyear, dayOfWeek};
    }
}
