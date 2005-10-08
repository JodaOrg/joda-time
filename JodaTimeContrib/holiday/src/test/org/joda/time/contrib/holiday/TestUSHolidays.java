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

import java.util.List;

import junit.framework.TestCase;

import org.joda.time.contrib.holiday.USGovtHolidays;
import org.joda.time.contrib.holiday.anniversary.CommonAnniversaries;
import org.joda.time.contrib.holiday.anniversary.USAnniversaries;
import org.joda.time.contrib.holiday.anniversary.NamedAnniversaryFactory.NamedAnniversary;

/* 
 * This is basically a test to make sure that the holiday list mechanism
 * works correctly. individual dates are tested in the anniversary tests.
 */
public class TestUSHolidays extends TestCase {
    public void testFederalHolidays() {
        List<NamedAnniversary> holidays = USGovtHolidays.FEDERAL.getHolidaysForYear(1995);

        assertEquals(holidays.size(), 10);
        assertEquals(holidays.get(0).getDate(),
                USAnniversaries.NEW_YEAR_HOLIDAY.create(1995));
        assertEquals(holidays.get(1).getDate(),
                USAnniversaries.MARTIN_LUTHER_KING_DAY.create(1995));
        assertEquals(holidays.get(2).getDate(),
                USAnniversaries.WASHINGTONS_BIRTHDAY_HOLIDAY.create(1995));
        assertEquals(holidays.get(3).getDate(),
                USAnniversaries.MEMORIAL_DAY.create(1995));
        assertEquals(holidays.get(4).getDate(),
                USAnniversaries.INDEPENDENCE_DAY_HOLIDAY.create(1995));
        assertEquals(holidays.get(5).getDate(),
                CommonAnniversaries.LABOR_DAY.create(1995));
        assertEquals(holidays.get(6).getDate(),
                USAnniversaries.COLUMBUS_DAY.create(1995));
        assertEquals(holidays.get(7).getDate(),
                USAnniversaries.VETERANS_DAY_HOLIDAY.create(1995));
        assertEquals(holidays.get(8).getDate(),
                USAnniversaries.THANKSGIVING.create(1995));
        assertEquals(holidays.get(9).getDate(),
                USAnniversaries.CHRISTMAS_HOLIDAY.create(1995));
    }
}
