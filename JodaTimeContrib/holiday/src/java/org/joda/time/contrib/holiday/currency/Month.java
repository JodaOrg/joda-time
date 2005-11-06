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
package org.joda.time.contrib.holiday.currency;

import java.util.HashMap;
import java.util.Map;

/**
 * This enumeration is for the months of a year.
 *
 * @author Scott R. Duchin
 */
public enum Month {

///////////////////////////////////////////////////////////////////////////////////////////////////
// Static Variables & Methods

    /**
     * Defines the twelve months.
     */
    JAN(1), FEB(2), MAR(3), APR(4), MAY(5), JUN(6), JUL(7), AUG(8), SEP(9), OCT(10), NOV(11), DEC(12);

    // static variables
    private static Month[]              _months = new Month[] { null, JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC };
    private static Map<String, Month>   _names = new HashMap<String, Month>(12); // lower case names to months

    /**
     * Numeric value representing number of months in the year.
     */
    public static int MONTHS_PER_YEAR = 12;

    /**
     * Array of month full names.
     */
    private static final String[] FULL_NAME = {
        null,
        "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    };

    /**
     * Array of days per month in non-leap year.
     */
    private static final /*day*/int[] MAX_DAYS = {
        0,
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };

    /**
     * Array of days per month in year.
     */
    private static final int[] START_WITHIN_YEAR = {
        0,
        0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334
    };
    private static final int[] START_WITHIN_LEAP_YEAR = {
        0,
        0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335
    };

    static {
        _names.put("january",   Month.JAN);
        _names.put("february",  Month.FEB);
        _names.put("march",     Month.MAR);
        _names.put("april",     Month.APR);
        _names.put("may",       Month.MAY);
        _names.put("june",      Month.JUN);
        _names.put("july",      Month.JUL);
        _names.put("august",    Month.AUG);
        _names.put("september", Month.SEP);
        _names.put("october",   Month.OCT);
        _names.put("november",  Month.NOV);
        _names.put("december",  Month.DEC);
    }

    /**
     * Returns the month associated with the index.
     * @param index Index of the month (1...12).
     * @return Associated month.
     */
    public static Month month(int index) {
        return _months[index];
    }

    /**
     * Returns the month associated with the name.
     * @param str Month name.
     * @return Associated month; <code>null</code> if invalid string.
     */
    public static Month month(String str) {
        Month month;
        if (str.length() == 3) {
            try {
                month = Enum.valueOf(Month.class, str.toUpperCase());
            } catch (IllegalArgumentException iae) {
                month = null;
            }
        } else {
            month = _names.get(str.toLowerCase());
        }
        return month;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
// Field Variables & Constructor

    // private field variables
    private final int                   _month;             // index of the month

    /**
     * Constructor.
     * @param   month   Month index.
     */
    private Month(int month) {
        _month = month;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
// Business Logic

    /**
     * Returns the number of days in the year.
     * @param leapYear <code>true</code> if leap year; <code>false</code> otherwise.
     * @return Number of days in month for year; inclusive range of 28 thru 31.
     */
    public /*day*/int maxDays(boolean leapYear) {
        return (this == FEB && leapYear) ? 29 : MAX_DAYS[_month];
    }

    /**
     * Returns the number of days in the year.
     * @param year  Year (1...3000).
     * @return Number of days in month for year; inclusive range of 28 thru 31.
     */
    public /*day*/int maxDays(/*year*/int year) {
        return maxDays(DateUtil.isLeapYear(year));
    }

    /**
     * Calculates the starting day of the year.
     * @param leapYear <code>true</code> if leap year; <code>false</code> otherwise.
     * @return Day number (1...366) of the year.
     */
    public int startDayInYear(boolean leapYear) {
        return leapYear ? START_WITHIN_LEAP_YEAR[_month] : START_WITHIN_YEAR[_month];
    }

    /**
     * Returns a string representation of the month.
     * @return Month abbreviation.
     */
    public String toString() {
        return name();
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
// Get & Set Methods

    /**
     * Returns the month abbreviated name.
     * @return Month abbreviation (JAN, FEB, ..., DEC).
     */
    public String getAbbrev() {
        return name();
    }

    /**
     * Returns the month full name.
     * @return Month name (January, February, ..., December).
     */
    public String getFullName() {
        return FULL_NAME[_month];
    }

    /**
     * Returns the month index.
     * @return Index of the month (1...12).
     */
    public int getValue() {
        return _month;
    }
}
