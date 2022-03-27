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

/**
 * A reference Gregorian chronology implementation, intended for testing
 * purposes only. Correctness is favored over performance. The key functions
 * for date calculations are based on ones provided in "Calendrical
 * Calculations", ISBN 0-521-77752-6.
 *
 * @author Brian S O'Neill
 */
public final class TestGregorianChronology extends TestGJChronology {
    /**
     * Constructs with an epoch of 1970-01-01.
     */
    public TestGregorianChronology() {
        super(1970, 1, 1);
    }

    public TestGregorianChronology(int epochYear, int epochMonth, int epochDay) {
        super(epochYear, epochMonth, epochDay);
    }

    @Override
    public String toString() {
        return "TestGregorianChronology";
    }

    @Override
    long millisPerYear() {
        return (long)(365.2425 * MILLIS_PER_DAY);
    }

    @Override
    long millisPerMonth() {
        return (long)(365.2425 * MILLIS_PER_DAY / 12);
    }

    @Override
    boolean isLeapYear(int year) {
        if (mod(year, 4) == 0) {
            int t = (int)mod(year, 400);
            if (t != 100 && t != 200 && t != 300) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return days from 0001-01-01
     */
    @Override
    long fixedFromGJ(int year, int monthOfYear, int dayOfMonth) {
        long year_m1 = year - 1;
        long f = 365 * year_m1 + div(year_m1, 4) - div(year_m1, 100)
            + div(year_m1, 400) + div(367 * monthOfYear - 362, 12) + dayOfMonth;
        if (monthOfYear > 2) {
            f += isLeapYear(year) ? -1 : -2;
        }
        return f;
    }

    /**
     * @param date days from 0001-01-01
     * @return gj year
     */
    @Override
    int gjYearFromFixed(long date) {
        long d0 = date - 1;
        long n400 = div(d0, 146097);
        long d1 = mod(d0, 146097);
        long n100 = div(d1, 36524);
        long d2 = mod(d1, 36524);
        long n4 = div(d2, 1461);
        long d3 = mod(d2, 1461);
        long n1 = div(d3, 365);
        long year = 400 * n400 + 100 * n100 + 4 * n4 + n1;
        if (!(n100 == 4 || n1 == 4)) {
            year += 1;
        }

        int year_i = (int)year;
        if (year_i == year) {
            return year_i;
        } else {
            throw new RuntimeException("year cannot be cast to an int: " + year);
        }
    }

    /**
     * @param date days from 0001-01-01
     * @return gj year, monthOfYear, dayOfMonth
     */
    @Override
    int[] gjFromFixed(long date) {
        int year = gjYearFromFixed(date);
        long priorDays = date - fixedFromGJ(year, 1, 1);
        long correction;
        if (date < fixedFromGJ(year, 3, 1)) {
            correction = 0;
        } else if (isLeapYear(year)) {
            correction = 1;
        } else {
            correction = 2;
        }
        int monthOfYear = (int)div(12 * (priorDays + correction) + 373, 367);
        int day = (int)(date - fixedFromGJ(year, monthOfYear, 1) + 1);

        return new int[]{year, monthOfYear, day};
    }

    @Override
    long fixedFromISO(int weekyear, int weekOfWeekyear, int dayOfWeek) {
        return nthWeekday(weekOfWeekyear, 0, weekyear - 1, 12, 28) + dayOfWeek;
    }

    /**
     * @param date days from 0001-01-01
     * @return iso weekyear, weekOfWeekyear, dayOfWeek (1=Monday to 7)
     */
    @Override
    int[] isoFromFixed(long date) {
        int weekyear = gjYearFromFixed(date - 3);
        if (date >= fixedFromISO(weekyear + 1, 1, 1)) {
            weekyear += 1;
        }
        int weekOfWeekyear = (int)(div(date - fixedFromISO(weekyear, 1, 1), 7) + 1);
        int dayOfWeek = (int)amod(date, 7);
        return new int[]{weekyear, weekOfWeekyear, dayOfWeek};
    }
}
