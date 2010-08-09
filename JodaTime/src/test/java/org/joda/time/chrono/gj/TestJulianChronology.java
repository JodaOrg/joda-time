/*
 *  Copyright 2001-2005 Stephen Colebourne
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
package org.joda.time.chrono.gj;

import org.joda.time.DateTimeField;

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
