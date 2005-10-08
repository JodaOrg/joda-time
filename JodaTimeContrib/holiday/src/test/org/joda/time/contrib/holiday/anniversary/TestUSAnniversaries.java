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

import junit.framework.TestCase;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.contrib.holiday.anniversary.CommonAnniversaries;
import org.joda.time.contrib.holiday.anniversary.USAnniversaries;

public class TestUSAnniversaries extends TestCase {
    public void testNewYearHoliday0() {
        DateTime ymd = USAnniversaries.NEW_YEAR_HOLIDAY.create(2005);
        assertEquals(ymd,
                new DateMidnight(2004, DateTimeConstants.DECEMBER, 31));
    }

    public void testNewYearHoliday1() {
        DateTime ymd = USAnniversaries.NEW_YEAR_HOLIDAY.create(1993);
        assertEquals(ymd, new DateMidnight(1993, DateTimeConstants.JANUARY, 1));
    }

    public void testMartinLutherKingDay() {
        DateTime ymd = USAnniversaries.MARTIN_LUTHER_KING_DAY.create(2005);
        assertEquals(ymd, new DateMidnight(2005, DateTimeConstants.JANUARY, 17));
    }

    public void testWashingtonsBirthdayHoliday() {
        DateTime ymd = USAnniversaries.WASHINGTONS_BIRTHDAY_HOLIDAY
                .create(2005);
        assertEquals(ymd,
                new DateMidnight(2005, DateTimeConstants.FEBRUARY, 21));
    }

    public void testMemorialDay0() {
        DateTime ymd = USAnniversaries.MEMORIAL_DAY.create(2005);
        assertEquals(ymd, new DateMidnight(2005, DateTimeConstants.MAY, 30));
    }

    public void testMemorialDay1() {
        DateTime ymd = USAnniversaries.MEMORIAL_DAY.create(1995);
        assertEquals(ymd, new DateMidnight(1995, DateTimeConstants.MAY, 29));
    }

    public void testMemorialDay2() {
        DateTime ymd = USAnniversaries.MEMORIAL_DAY.create(1997);
        assertEquals(ymd, new DateMidnight(1997, DateTimeConstants.MAY, 26));
    }

    public void testIndependenceDayHoliday0() {
        DateTime ymd = USAnniversaries.INDEPENDENCE_DAY_HOLIDAY.create(2005);
        assertEquals(ymd, new DateMidnight(2005, DateTimeConstants.JULY, 4));
    }

    public void testIndependenceDayHoliday1() {
        DateTime ymd = USAnniversaries.INDEPENDENCE_DAY_HOLIDAY.create(1993);
        assertEquals(ymd, new DateMidnight(1993, DateTimeConstants.JULY, 5));
    }

    public void testLaborDay() {
        DateTime ymd = CommonAnniversaries.LABOR_DAY.create(2005);
        assertEquals(ymd,
                new DateMidnight(2005, DateTimeConstants.SEPTEMBER, 5));
    }

    public void testThanksgiving0() {
        DateTime ymd = USAnniversaries.THANKSGIVING.create(2005);
        assertEquals(ymd,
                new DateMidnight(2005, DateTimeConstants.NOVEMBER, 24));
    }

    public void testThanksgiving1() {
        DateTime ymd = USAnniversaries.THANKSGIVING.create(1995);
        assertEquals(ymd,
                new DateMidnight(1995, DateTimeConstants.NOVEMBER, 23));
    }

    public void testColumbusDay0() {
        DateTime ymd = USAnniversaries.COLUMBUS_DAY.create(2005);
        assertEquals(ymd, new DateMidnight(2005, DateTimeConstants.OCTOBER, 10));
    }

    public void testColumbusDay1() {
        DateTime ymd = USAnniversaries.COLUMBUS_DAY.create(1998);
        assertEquals(ymd, new DateMidnight(1998, DateTimeConstants.OCTOBER, 12));
    }

    public void testChristmasHoliday0() {
        DateTime ymd = USAnniversaries.CHRISTMAS_HOLIDAY.create(2005);
        assertEquals(ymd,
                new DateMidnight(2005, DateTimeConstants.DECEMBER, 26));
    }

    public void testChristmasHoliday1() {
        DateTime ymd = USAnniversaries.CHRISTMAS_HOLIDAY.create(1995);
        assertEquals(ymd,
                new DateMidnight(1995, DateTimeConstants.DECEMBER, 25));
    }

    public void testElectionDay0() {
        DateTime ymd = USAnniversaries.ELECTION_DAY.create(1996);
        assertEquals(ymd, new DateMidnight(1996, DateTimeConstants.NOVEMBER, 5));
    }

    public void testElectionDay1() {
        // no election in 1997
        DateTime ymd = USAnniversaries.ELECTION_DAY.create(1997);
        assertEquals(ymd, null);
    }

    public void testElectionDay2() {
        DateTime ymd = USAnniversaries.ELECTION_DAY.create(2000);
        assertEquals(ymd, new DateMidnight(2000, DateTimeConstants.NOVEMBER, 7));
    }

    public void testElectionDay3() {
        DateTime ymd = USAnniversaries.ELECTION_DAY.create(2004);
        assertEquals(ymd, new DateMidnight(2004, DateTimeConstants.NOVEMBER, 2));
    }
}
