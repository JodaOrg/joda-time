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
package org.joda.time.contrib.holiday.anniversary;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

/**
 * TODO: There may be a need for localizable labels for the names of the holidays.
 * would have to be loaded from an appropriate properties file.
 * TODO: at some point it may make sense to generate either the holiday computation
 * code from a definition file or to run an interpreter on the file to generate
 * the holiday directly.
 */
/**
 * Except when specifically noted otherwise, dates for holidays are based on
 * information from Wikipedia.
 */
public class USAnniversaries {

    public static final AnniversaryFactory NEW_YEAR_HOLIDAY = new AnniversaryFactory(
            "NewYearHoliday") {
        public DateTime create(int iYear) {
            return TimeUtility
                    .AdjustAmericanHolidayForWeekend(CommonAnniversaries.NEW_YEAR
                            .create(iYear));
        }
    };

    public static final AnniversaryFactory MARTIN_LUTHER_KING_DAY = new AnniversaryFactory(
            "MartinLutherKingDay") {
        public DateTime create(int iYear) {
            // Third Monday in January
            return TimeUtility.GetFirstXWeekdayOfMonth(
                    DateTimeConstants.MONDAY, iYear, DateTimeConstants.JANUARY)
                    .plusWeeks(2);
        }
    };

    /**
     * Also called "President's Day"
     */
    public static final AnniversaryFactory WASHINGTONS_BIRTHDAY_HOLIDAY = new AnniversaryFactory(
            "WashingtonsBirthdayHoliday") {
        public DateTime create(int iYear) {
            // Third Monday in February
            return TimeUtility
                    .GetFirstXWeekdayOfMonth(DateTimeConstants.MONDAY, iYear,
                            DateTimeConstants.FEBRUARY).plusWeeks(2);
        }
    };

    public static final AnniversaryFactory MEMORIAL_DAY = new AnniversaryFactory(
            "MemorialDay") {
        public DateTime create(int iYear) {
            // Last Monday in May
            return TimeUtility.GetLastXWeekdayOfMonth(DateTimeConstants.MONDAY,
                    iYear, DateTimeConstants.MAY);
        }
    };

    public static final AnniversaryFactory INDEPENDENCE_DAY = new AnniversaryFactory(
            "IndependenceDay") {
        public DateTime create(int iYear) {
            return new DateMidnight(iYear, DateTimeConstants.JULY, 4)
                    .toDateTime();
        }
    };

    public static final AnniversaryFactory INDEPENDENCE_DAY_HOLIDAY = new AnniversaryFactory(
            "IndependenceDayHoliday") {
        public DateTime create(int iYear) {
            return TimeUtility.AdjustAmericanHolidayForWeekend(INDEPENDENCE_DAY
                    .create(iYear));
        }
    };

    public static final AnniversaryFactory VETERANS_DAY_HOLIDAY = new AnniversaryFactory(
            "VeteransDayHoliday") {
        public DateTime create(int iYear) {
            return TimeUtility
                    .AdjustAmericanHolidayForWeekend(CommonAnniversaries.ARMISTICE_DAY
                            .create(iYear));
        }
    };

    public static final AnniversaryFactory COLUMBUS_DAY = new AnniversaryFactory(
            "ColumbusDay") {
        public DateTime create(int iYear) {
            // Second Monday in October
            return TimeUtility.GetFirstXWeekdayOfMonth(
                    DateTimeConstants.MONDAY, iYear, DateTimeConstants.OCTOBER)
                    .plusWeeks(1);
        }
    };

    public static final AnniversaryFactory THANKSGIVING = new AnniversaryFactory(
            "ThanksGiving") {
        public DateTime create(int iYear) {
            // 4th Thursday in November
            return TimeUtility.GetFirstXWeekdayOfMonth(
                    DateTimeConstants.THURSDAY, iYear,
                    DateTimeConstants.NOVEMBER).plusWeeks(3);
        }
    };

    public static final AnniversaryFactory CHRISTMAS_HOLIDAY = new AnniversaryFactory(
            "ChristmasHoliday") {
        public DateTime create(int iYear) {
            return TimeUtility
                    .AdjustAmericanHolidayForWeekend(CommonAnniversaries.CHRISTMAS
                            .create(iYear));
        }
    };

    /**
     * This was the holiday prior to 1968-1971.
     */
    public static final AnniversaryFactory WASHINGTONS_BIRTHDAY = new AnniversaryFactory(
            "WashingtonsBirthday") {
        public DateTime create(int iYear) {
            return new DateMidnight(iYear, DateTimeConstants.FEBRUARY, 22)
                    .toDateTime();
        }
    };

    public static final AnniversaryFactory LINCOLNS_BIRTHDAY = new AnniversaryFactory(
            "LincolnsBirthday") {
        public DateTime create(int iYear) {
            return new DateMidnight(iYear, DateTimeConstants.FEBRUARY, 12)
                    .toDateTime();
        }
    };

    public static final AnniversaryFactory CINCO_DE_MAYO = new AnniversaryFactory(
            "CincoDeMayo") {
        public DateTime create(int iYear) {
            return new DateMidnight(iYear, DateTimeConstants.MAY, 5)
                    .toDateTime();
        }
    };

    public static final AnniversaryFactory HALLOWEEN = new AnniversaryFactory(
            "Halloween") {
        public DateTime create(int iYear) {
            return new DateMidnight(iYear, DateTimeConstants.OCTOBER, 31)
                    .toDateTime();
        }
    };

    /**
     * Source: the example given in RFC 2445
     */
    public static final AnniversaryFactory ELECTION_DAY = new AnniversaryFactory(
            "ElectionDay") {
        public DateTime create(int iYear) {
            // not really an anniversary, happens once in 4 years
            if (iYear % 4 != 0) {
                return null;
            }
            // First Tuesday in November which follows a Monday in November
            return TimeUtility.GetFirstXWeekdayOfMonthAfterYMonthday(
                    DateTimeConstants.TUESDAY, 1, iYear,
                    DateTimeConstants.NOVEMBER);
        }
    };

    public static final AnniversaryFactory PRE_FDR_THANKSGIVING = new AnniversaryFactory(
            "PreFDRThanksGiving") {
        public DateTime create(int iYear) {
            // Last Thursday in November
            return TimeUtility.GetLastXWeekdayOfMonth(
                    DateTimeConstants.THURSDAY, iYear,
                    DateTimeConstants.NOVEMBER);
        }
    };
}
