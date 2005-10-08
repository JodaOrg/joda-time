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

public class GBAnniversaries {
    /*
     * TODO: in 1995 this was moved to second monday in may. fix?
     */
    public static final AnniversaryFactory MAY_DAY_BANK_HOLIDAY = new AnniversaryFactory(
            "MayDayBankHoliday") {
        public DateTime create(int iYear) {
            // First Monday in May
            return TimeUtility.GetFirstXWeekdayOfMonth(
                    DateTimeConstants.MONDAY, iYear, DateTimeConstants.MAY);
        }
    };

    public static final AnniversaryFactory SPRING_BANK_HOLIDAY = new AnniversaryFactory(
            "SpringBankHoliday") {
        public DateTime create(int iYear) {
            // Last Monday in May
            return TimeUtility.GetLastXWeekdayOfMonth(DateTimeConstants.MONDAY,
                    iYear, DateTimeConstants.MAY);
        }
    };

    public static final AnniversaryFactory JULY_12 = new AnniversaryFactory(
            "July12") {
        public DateTime create(int iYear) {
            return new DateMidnight(iYear, DateTimeConstants.JULY, 12)
                    .toDateTime();
        }
    };

    public static final AnniversaryFactory SUMMER_BANK_HOLIDAY = new AnniversaryFactory(
            "SummerBankHoliday") {
        public DateTime create(int iYear) {
            // Last Monday in August
            return TimeUtility.GetLastXWeekdayOfMonth(DateTimeConstants.MONDAY,
                    iYear, DateTimeConstants.AUGUST);
        }
    };

    public static final AnniversaryFactory SCOTTISH_SUMMER_BANK_HOLIDAY = new AnniversaryFactory(
            "MayDayBankHoliday") {
        public DateTime create(int iYear) {
            // First Monday in August
            return TimeUtility.GetFirstXWeekdayOfMonth(
                    DateTimeConstants.MONDAY, iYear, DateTimeConstants.AUGUST);
        }
    };
}
