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
package org.joda.time.contrib.holiday;

import org.joda.time.DateTimeConstants;
import org.joda.time.MutableDateTime;

/**
 * HolidayRule defines the rules to create a specially named day.
 * The named day may be a holiday, anniversary or other day with
 * particular meaning.
 *
 * @author Al Major
 * @author Stephen Colebourne
 * @version $Id$
 */
public class HolidayRule {

    /** The from year, when the rule begins, inclusive. */
    private int iFromYear;
    /** The to year, when the rule ends, inclusive. */
    private int iToYear;
    /** The month. */
    private int iMonthOfYear;
    /** The day of month. */
    private int iDayOfMonth;
    /** The day of week. */
    private int iDayOfWeek;
    /** The field week of month. */
    private int iWeekOfMonth;
    /** The relative days. */
    private int iRelativeDays;
    /** The weekend adjust. */
    private int iWeekendAdjust;

    /** The weekend adjust value, to move to the next weekday. */
    private static final int WEEKEND_ADJUST_NEXT_WEEKDAY = 0;
    /** The weekend adjust value, to move to the next weekday. */
    private static final int WEEKEND_ADJUST_PREVIOUS_WEEKDAY = 1;
    /** The weekend adjust value, to move to the next weekday. */
    private static final int WEEKEND_ADJUST_NEAREST_WEEKDAY = 2;

    /**
     * Constructor.
     */
    HolidayRule() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the year that the rule commences (inclusive).
     *
     * @return the from year, Integer.MIN_VALUE if not used
     */
    public int getFromYear() {
        return iFromYear;
    }

    /**
     * Sets the year that the rule commences (inclusive).
     *
     * @param year  the from year, Integer.MIN_VALUE if not used
     */
    void setFromYear(int year) {
        iFromYear = year;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the year that the rule ends (inclusive).
     *
     * @return the to year, Integer.MAX_VALUE if not used
     */
    public int getToYear() {
        return iToYear;
    }

    /**
     * Sets the year that the rule ends (inclusive).
     *
     * @param year  the to year, Integer.MIN_VALUE if not used
     */
    void setToYear(int year) {
        iToYear = year;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the month of the year of the rule.
     *
     * @return the month of year, Integer.MIN_VALUE if not used
     */
    public int getMonthOfYear() {
        return iMonthOfYear;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the day of the month of the rule.
     *
     * @return the day of month, Integer.MIN_VALUE if not used
     */
    public int getDayOfMonth() {
        return iDayOfMonth;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the day of the week of the rule.
     *
     * @return the day of week, Integer.MIN_VALUE if not used
     */
    public int getDayOfWeek() {
        return iDayOfWeek;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the week of the month of the rule.
     * -1 indicates the last.
     *
     * @return the week of month, Integer.MIN_VALUE if not used
     */
    public int getWeekOfMonth() {
        return iWeekOfMonth;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the relative days of the rule.
     *
     * @return the relative days, Integer.MIN_VALUE if not used
     */
    public int getRelativeDays() {
        return iRelativeDays;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the day of the month of the rule.
     *
     * @return the weekend adjustment rule, Integer.MIN_VALUE if not used
     */
    public int getWeekendAdjust() {
        return iWeekendAdjust;
    }

    //-----------------------------------------------------------------------
    /**
     * Does this rule apply for the specified year.
     *
     * @param mdt  working copy MutableDateTime, may be altered, not null
     * @param year  the year
     * @return true if this rule applies for the specified year
     */
    boolean appliesForYear(MutableDateTime mdt, int year) {
        return (iFromYear <= year && iToYear >= year);
    }

    //-----------------------------------------------------------------------
    /**
     * Applies the rules for the specified year.
     *
     * @param mdt  working copy MutableDateTime, may be altered, not null
     * @param year  the year
     * @return the MutableDateTime
     */
    MutableDateTime applyForYear(MutableDateTime mdt, int year) {
        mdt.setDate(year, 1, 1);
        if (iMonthOfYear != Integer.MIN_VALUE) {
            mdt.setMonthOfYear(iMonthOfYear);
        }
        if (iDayOfMonth != Integer.MIN_VALUE) {
            mdt.setDayOfMonth(iDayOfMonth);
        }
        if (iDayOfWeek != Integer.MIN_VALUE) {
            mdt.setDayOfWeek(iDayOfWeek);
        }
        if (iWeekOfMonth != Integer.MIN_VALUE) {
            calculateWeekOfMonth(mdt, iMonthOfYear, iWeekOfMonth);
        }
        if (iRelativeDays != Integer.MIN_VALUE) {
            mdt.addDays(iRelativeDays);
        }
        if (iWeekendAdjust != Integer.MIN_VALUE) {
            calculateWeekendAdjust(mdt, iWeekendAdjust);
        }
        return mdt;
    }

    //-----------------------------------------------------------------------
    /**
     * Perform the weekk of month calculation.
     * TODO, move this to a DateTimeField.
     *
     * @param mdt  the datetime to update
     * @param month  the month it should be
     * @param week  the week of month
     */
    private void calculateWeekOfMonth(MutableDateTime mdt, int month, int week) {
        if (week > 0) {
            while (mdt.getMonthOfYear() >= month) {
                mdt.addWeeks(-1);
            }
            mdt.addWeeks(week);
        } else {
            while (mdt.getMonthOfYear() <= month) {
                mdt.addWeeks(1);
            }
            mdt.addWeeks(-week);
        }
    }

    /**
     * Calculates the weekend adjust field.
     *
     * @param mdt  the datetime to update
     * @param adjust  the adjust value
     */
    private void calculateWeekendAdjust(MutableDateTime mdt, int adjust) {
        // TODO, use proper weekend calculation, not hard coded SatSun
        if (mdt.getDayOfWeek() == DateTimeConstants.SATURDAY) {
            switch (adjust) {
                case WEEKEND_ADJUST_NEXT_WEEKDAY:
                    mdt.addDays(2);
                    break;
                case WEEKEND_ADJUST_PREVIOUS_WEEKDAY:
                case WEEKEND_ADJUST_NEAREST_WEEKDAY:
                    mdt.addDays(-1);
                    break;
            }
        } else if (mdt.getDayOfWeek() == DateTimeConstants.SUNDAY) {
            switch (adjust) {
                case WEEKEND_ADJUST_NEXT_WEEKDAY:
                case WEEKEND_ADJUST_NEAREST_WEEKDAY:
                    mdt.addDays(1);
                    break;
                case WEEKEND_ADJUST_PREVIOUS_WEEKDAY:
                    mdt.addDays(-2);
                    break;
            }
        }
    }

}
