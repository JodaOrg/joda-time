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

public class TestCommonAnniversaries extends TestCase {

    /**
     * NB: tests for good friday are automatically tests for Easter and Easter
     * Monday.
     */
    public void testGoodFriday0() {
        DateTime ymd = CommonAnniversaries.GOOD_FRIDAY.create(2005);
        assertEquals(ymd, new DateMidnight(2005, DateTimeConstants.MARCH, 25));
    }

    public void testGoodFriday1() {
        DateTime ymd = CommonAnniversaries.GOOD_FRIDAY.create(1993);
        assertEquals(ymd, new DateMidnight(1993, DateTimeConstants.APRIL, 9));
    }

    public void testGoodFriday2() {
        DateTime ymd = CommonAnniversaries.GOOD_FRIDAY.create(1997);
        assertEquals(ymd, new DateMidnight(1997, DateTimeConstants.MARCH, 28));
    }

}
