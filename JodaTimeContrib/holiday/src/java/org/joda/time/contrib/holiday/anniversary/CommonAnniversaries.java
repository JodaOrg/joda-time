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

public class CommonAnniversaries {

    public static final AnniversaryFactory NEW_YEAR = new AnniversaryFactory(
            "NewYear") {
        public DateTime create(int iYear) {
            return new DateMidnight(iYear, DateTimeConstants.JANUARY, 1)
                    .toDateTime();
        }
    };

    public static final AnniversaryFactory ST_VALENTINES_DAY = new AnniversaryFactory(
            "ValentinesDay") {
        public DateTime create(int iYear) {
            return new DateMidnight(iYear, DateTimeConstants.FEBRUARY, 14)
                    .toDateTime();
        }
    };

    public static final AnniversaryFactory ST_PATRICKS_DAY = new AnniversaryFactory(
            "StPatricksDay") {
        public DateTime create(int iYear) {
            return new DateMidnight(iYear, DateTimeConstants.MARCH, 17)
                    .toDateTime();
        }
    };

    public static final AnniversaryFactory GOOD_FRIDAY = new AnniversaryFactory(
            "GoodFriday") {
        public DateTime create(int iYear) {
            return EASTER_SUNDAY.create(iYear).minusDays(2);
        }
    };

    /**
     * Source: http://www.tondering.dk/claus/calendar.html This is a calculation
     * for the <em>Gregorian</em> calendar only.
     */
    public static final AnniversaryFactory EASTER_SUNDAY = new AnniversaryFactory(
            "EasterSunday") {
        public DateTime create(int iYear) {
            int iG = iYear % 19;
            int iC = iYear / 100;
            int iH = (iC - iC / 4 - (8 * iC + 13) / 25 + 19 * iG + 15) % 30;
            int iI = iH - (iH / 28) * (1 - (29 / (iH + 1)) * ((21 - iG) / 11));
            int iJ = (iYear + iYear / 4 + iI + 2 - iC + iC / 4) % 7;
            int iL = iI - iJ;
            int iMonth = 3 + (iL + 40) / 44;
            int iDay = iL + 28 - 31 * (iMonth / 4);
            return new DateMidnight(iYear, iMonth, iDay).toDateTime();
        }
    };

    public static final AnniversaryFactory EASTER_MONDAY = new AnniversaryFactory(
            "EasterMonday") {
        public DateTime create(int iYear) {
            return EASTER_SUNDAY.create(iYear).plusDays(1);
        }
    };

    /**
     * same as Veterans Day and Remembrance Day.
     */
    public static final AnniversaryFactory ARMISTICE_DAY = new AnniversaryFactory(
            "ArmisticeDay") {
        public DateTime create(int iYear) {
            return new DateMidnight(iYear, DateTimeConstants.NOVEMBER, 11)
                    .toDateTime();
        }
    };

    public static final AnniversaryFactory CHRISTMAS = new AnniversaryFactory(
            "Christmas") {
        public DateTime create(int iYear) {
            return new DateMidnight(iYear, DateTimeConstants.DECEMBER, 25)
                    .toDateTime();
        }
    };

    public static final AnniversaryFactory BOXING_DAY = new AnniversaryFactory(
            "BoxingDay") {
        public DateTime create(int iYear) {
            return new DateMidnight(iYear, DateTimeConstants.DECEMBER, 26)
                    .toDateTime();
        }
    };

    public static final AnniversaryFactory LABOR_DAY = new AnniversaryFactory(
            "LaborDay") {
        public DateTime create(int iYear) {
            // First Monday in September
            return TimeUtility.GetFirstXWeekdayOfMonth(
                    DateTimeConstants.MONDAY, iYear,
                    DateTimeConstants.SEPTEMBER);
        }
    };

}
