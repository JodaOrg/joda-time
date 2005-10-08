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
 * Caveat: According to the description on Wikipedia, the rule states that the
 * holiday is moved if it would fall on _another holiday_ not just a weekend.
 * The only standard holidays for which this could be a problem is Boxing Day
 * and Jan 2, which are explicitly corrected.
 * 
 * It's not clear how the rule interacts with non statutory holidays.
 * 
 */
public class CanadaAnniversaries {

    public static final AnniversaryFactory NEW_YEAR_HOLIDAY = new AnniversaryFactory(
            "CanadaDayHoliday") {
        public DateTime create(int iYear) {
            return TimeUtility
                    .AdjustCanadianHolidayForWeekend(CanadaAnniversaries.CANADA_DAY
                            .create(iYear));
        }
    };

    public static final AnniversaryFactory VICTORIA_DAY = new AnniversaryFactory(
            "VictoriaDay") {
        public DateTime create(int iYear) {
            return TimeUtility.GetLastXWeekdayOfMonthBeforeYMonthday(
                    DateTimeConstants.MONDAY, 25, iYear, DateTimeConstants.MAY);
        }
    };

    public static final AnniversaryFactory CANADA_DAY = new AnniversaryFactory(
            "CanadaDay") {
        public DateTime create(int iYear) {
            return new DateMidnight(iYear, DateTimeConstants.JULY, 1)
                    .toDateTime();
        }
    };

    public static final AnniversaryFactory CANADA_DAY_HOLIDAY = new AnniversaryFactory(
            "CanadaDayHoliday") {
        public DateTime create(int iYear) {
            return TimeUtility
                    .AdjustCanadianHolidayForWeekend(CanadaAnniversaries.CANADA_DAY
                            .create(iYear));
        }
    };

    public static final AnniversaryFactory THANKSGIVING = new AnniversaryFactory(
            "Thanksgiving") {
        public DateTime create(int iYear) {
            // Second Monday in October
            return TimeUtility.GetFirstXWeekdayOfMonth(
                    DateTimeConstants.MONDAY, iYear, DateTimeConstants.OCTOBER)
                    .plusWeeks(1);
        }
    };

    public static final AnniversaryFactory REMEMBRANCE_DAY_HOLIDAY = new AnniversaryFactory(
            "RemembranceDayHoliday") {
        public DateTime create(int iYear) {
            return TimeUtility
                    .AdjustCanadianHolidayForWeekend(CommonAnniversaries.ARMISTICE_DAY
                            .create(iYear));
        }
    };

    public static final AnniversaryFactory CHRISTMAS_HOLIDAY = new AnniversaryFactory(
            "ChristmasHoliday") {
        public DateTime create(int iYear) {
            return TimeUtility
                    .AdjustCanadianHolidayForWeekend(CommonAnniversaries.CHRISTMAS
                            .create(iYear));
        }
    };

    public static final AnniversaryFactory BOXING_DAY_HOLIDAY = new AnniversaryFactory(
            "BoxingDayHoliday") {
        // special case. since they two dates are together and both have to be on a weekday
        public DateTime create(int iYear) {
            return TimeUtility
                    .AdjustCanadianHolidayForWeekend(CHRISTMAS_HOLIDAY.create(
                            iYear).plusDays(1));
        }
    };

}
