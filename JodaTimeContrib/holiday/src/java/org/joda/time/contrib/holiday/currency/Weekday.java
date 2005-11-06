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
 * This enumeration is for the weekdays of a week.
 *
 * @author Scott R. Duchin
 */
public enum Weekday {

///////////////////////////////////////////////////////////////////////////////////////////////////
// Static Variables & Methods

    /**
     * Defines the seven weekdays.
     */
    SUN(1, true), MON(2, false), TUE(3, false), WED(4, false), THU(5, false), FRI(6, false), SAT(7, true);

    // static variables
    private static Weekday[]            _weekdays = new Weekday[] { null, SUN, MON, TUE, WED, THU, FRI, SAT };
    private static Map<String, Weekday> _names = new HashMap<String, Weekday>(7); // lower case names to weekdays

    /**
     * Numeric value representing number of days in the week.
     */
    public static int DAYS_PER_WEEK = 7;

    /**
     * Array of weekday full names.
     */
    private static final String[] FULL_NAME = {
        null,
        "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"
    };

    static {
        _names.put("Sunday",    Weekday.SUN);
        _names.put("Monday",    Weekday.MON);
        _names.put("Tuesday",   Weekday.TUE);
        _names.put("Wednesday", Weekday.WED);
        _names.put("Thursday",  Weekday.THU);
        _names.put("Friday",    Weekday.FRI);
        _names.put("Saturday",  Weekday.SAT);
    }

    /**
     * Returns the weekday associated with the index.
     * @param index Index of the weekday (1...7).
     * @return Associated weekday.
     */
    public static Weekday weekday(int index) {
        return _weekdays[index];
    }

    /**
     * Returns the weekday associated with the name.
     * @param str Weekday name.
     * @return Associated weekday; <code>null</code> if invalid string.
     */
    public static Weekday weekday(String str) {
        Weekday weekday;
        if (str.length() == 3) {
            try {
                weekday = Enum.valueOf(Weekday.class, str.toUpperCase());
            } catch (IllegalArgumentException iae) {
                weekday = null;
            }
        } else {
            weekday = _names.get(str.toLowerCase());
        }
        return weekday;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
// Field Variables & Constructor

    // private field variables
    private final boolean               _isWeekend;         // true if weekend; false otherwise
    private final int                   _weekday;           // index of the weekday

    /**
     * Constructor.
     * @param   weekday     Weekday index.
     * @param   isWeekend   <code>true</code> if weekend; <code>false</code> otherwise.
     */
    private Weekday(int weekday, boolean isWeekend) {
        _weekday = weekday;
        _isWeekend = isWeekend;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
// Business Logic

    /**
     * Indicates if day is a weekday.
     * @return  <code>true</code> if weekday; <code>false</code> if weekend.
     */
    public boolean isWeekday() {
        return ! _isWeekend;
    }

    /**
     * Indicates if day is a weekend.
     * @return  <code>true</code> if weekend; <code>false</code> if weekday.
     */
    public boolean isWeekend() {
        return _isWeekend;
    }

    /**
     * Returns a string representation of the weekday.
     * @return Weekday abbreviation.
     */
    public String toString() {
        return name();
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
// Get & Set Methods

    /**
     * Returns the weekday abbreviated name.
     * @return Weekday abbreviation (SUN, MON, ..., SAT).
     */
    public String getAbbrev() {
        return name();
    }

    /**
     * Returns the weekday full name.
     * @return Weekday name (Sunday, Monday, ..., Saturday).
     */
    public String getFullName() {
        return FULL_NAME[_weekday];
    }

    /**
     * Returns the weekday index.
     * @return Index of the weekday (1...7).
     */
    public int getValue() {
        return _weekday;
    }
}
