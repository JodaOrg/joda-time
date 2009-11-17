/*
 *  Copyright 2001-2007 Stephen Colebourne
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
package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This class is a JUnit test for DateTimeZone.
 *
 * @author Stephen Colebourne
 */
public class TestDateTimeZoneCutover extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestDateTimeZoneCutover.class);
    }

    public TestDateTimeZoneCutover(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    //-----------------------------------------------------------------------
    //------------------------ Bug [1710316] --------------------------------
    //-----------------------------------------------------------------------
    // The behaviour of getOffsetFromLocal is defined in its javadoc
    // However, this definition doesn't work for all DateTimeField operations
    
    /** Mock zone simulating Asia/Gaza cutover at midnight 2007-04-01 */
    private static long CUTOVER_GAZA = 1175378400000L;
    private static int OFFSET_GAZA = 7200000;  // +02:00
    private static final DateTimeZone MOCK_GAZA = new MockZone(CUTOVER_GAZA, OFFSET_GAZA);

    //-----------------------------------------------------------------------
    public void test_MockGazaIsCorrect() {
        DateTime pre = new DateTime(CUTOVER_GAZA - 1L, MOCK_GAZA);
        assertEquals("2007-03-31T23:59:59.999+02:00", pre.toString());
        DateTime at = new DateTime(CUTOVER_GAZA, MOCK_GAZA);
        assertEquals("2007-04-01T01:00:00.000+03:00", at.toString());
        DateTime post = new DateTime(CUTOVER_GAZA + 1L, MOCK_GAZA);
        assertEquals("2007-04-01T01:00:00.001+03:00", post.toString());
    }

    public void test_getOffsetFromLocal_Gaza() {
        doTest_getOffsetFromLocal_Gaza(-1, 23, 0, "2007-03-31T23:00:00.000+02:00");
        doTest_getOffsetFromLocal_Gaza(-1, 23, 30, "2007-03-31T23:30:00.000+02:00");
        doTest_getOffsetFromLocal_Gaza(0, 0, 0, "2007-04-01T01:00:00.000+03:00");
        doTest_getOffsetFromLocal_Gaza(0, 0, 30, "2007-04-01T01:30:00.000+03:00");
        doTest_getOffsetFromLocal_Gaza(0, 1, 0, "2007-04-01T01:00:00.000+03:00");
        doTest_getOffsetFromLocal_Gaza(0, 1, 30, "2007-04-01T01:30:00.000+03:00");
        doTest_getOffsetFromLocal_Gaza(0, 2, 0, "2007-04-01T02:00:00.000+03:00");
        doTest_getOffsetFromLocal_Gaza(0, 3, 0, "2007-04-01T03:00:00.000+03:00");
        doTest_getOffsetFromLocal_Gaza(0, 4, 0, "2007-04-01T04:00:00.000+03:00");
        doTest_getOffsetFromLocal_Gaza(0, 5, 0, "2007-04-01T05:00:00.000+03:00");
        doTest_getOffsetFromLocal_Gaza(0, 6, 0, "2007-04-01T06:00:00.000+03:00");
    }

    private void doTest_getOffsetFromLocal_Gaza(int days, int hour, int min, String expected) {
        DateTime dt = new DateTime(2007, 4, 1, hour, min, 0, 0, DateTimeZone.UTC).plusDays(days);
        int offset = MOCK_GAZA.getOffsetFromLocal(dt.getMillis());
        DateTime res = new DateTime(dt.getMillis() - offset, MOCK_GAZA);
        assertEquals(res.toString(), expected, res.toString());
    }

    public void test_DateTime_roundFloor_Gaza() {
        DateTime dt = new DateTime(2007, 4, 1, 8, 0, 0, 0, MOCK_GAZA);
        assertEquals("2007-04-01T08:00:00.000+03:00", dt.toString());
        DateTime rounded = dt.dayOfMonth().roundFloorCopy();
        assertEquals("2007-04-01T01:00:00.000+03:00", rounded.toString());
    }

    public void test_DateTime_roundCeiling_Gaza() {
        DateTime dt = new DateTime(2007, 3, 31, 20, 0, 0, 0, MOCK_GAZA);
        assertEquals("2007-03-31T20:00:00.000+02:00", dt.toString());
        DateTime rounded = dt.dayOfMonth().roundCeilingCopy();
        assertEquals("2007-04-01T01:00:00.000+03:00", rounded.toString());
    }

    public void test_DateTime_setHourZero_Gaza() {
        DateTime dt = new DateTime(2007, 4, 1, 8, 0, 0, 0, MOCK_GAZA);
        assertEquals("2007-04-01T08:00:00.000+03:00", dt.toString());
        try {
            dt.hourOfDay().setCopy(0);
            fail();
        } catch (IllegalFieldValueException ex) {
            // expected
        }
    }

    public void test_DateTime_withHourZero_Gaza() {
        DateTime dt = new DateTime(2007, 4, 1, 8, 0, 0, 0, MOCK_GAZA);
        assertEquals("2007-04-01T08:00:00.000+03:00", dt.toString());
        try {
            dt.withHourOfDay(0);
            fail();
        } catch (IllegalFieldValueException ex) {
            // expected
        }
    }

    public void test_DateTime_withDay_Gaza() {
        DateTime dt = new DateTime(2007, 4, 2, 0, 0, 0, 0, MOCK_GAZA);
        assertEquals("2007-04-02T00:00:00.000+03:00", dt.toString());
        DateTime res = dt.withDayOfMonth(1);
        assertEquals("2007-04-01T01:00:00.000+03:00", res.toString());
    }

    public void test_DateTime_minusHour_Gaza() {
        DateTime dt = new DateTime(2007, 4, 1, 8, 0, 0, 0, MOCK_GAZA);
        assertEquals("2007-04-01T08:00:00.000+03:00", dt.toString());
        
        DateTime minus7 = dt.minusHours(7);
        assertEquals("2007-04-01T01:00:00.000+03:00", minus7.toString());
        DateTime minus8 = dt.minusHours(8);
        assertEquals("2007-03-31T23:00:00.000+02:00", minus8.toString());
        DateTime minus9 = dt.minusHours(9);
        assertEquals("2007-03-31T22:00:00.000+02:00", minus9.toString());
    }

    public void test_DateTime_plusHour_Gaza() {
        DateTime dt = new DateTime(2007, 3, 31, 16, 0, 0, 0, MOCK_GAZA);
        assertEquals("2007-03-31T16:00:00.000+02:00", dt.toString());
        
        DateTime plus7 = dt.plusHours(7);
        assertEquals("2007-03-31T23:00:00.000+02:00", plus7.toString());
        DateTime plus8 = dt.plusHours(8);
        assertEquals("2007-04-01T01:00:00.000+03:00", plus8.toString());
        DateTime plus9 = dt.plusHours(9);
        assertEquals("2007-04-01T02:00:00.000+03:00", plus9.toString());
    }

    public void test_DateTime_minusDay_Gaza() {
        DateTime dt = new DateTime(2007, 4, 2, 0, 0, 0, 0, MOCK_GAZA);
        assertEquals("2007-04-02T00:00:00.000+03:00", dt.toString());
        
        DateTime minus1 = dt.minusDays(1);
        assertEquals("2007-04-01T01:00:00.000+03:00", minus1.toString());
        DateTime minus2 = dt.minusDays(2);
        assertEquals("2007-03-31T00:00:00.000+02:00", minus2.toString());
    }

    public void test_DateTime_plusDay_Gaza() {
        DateTime dt = new DateTime(2007, 3, 31, 0, 0, 0, 0, MOCK_GAZA);
        assertEquals("2007-03-31T00:00:00.000+02:00", dt.toString());
        
        DateTime plus1 = dt.plusDays(1);
        assertEquals("2007-04-01T01:00:00.000+03:00", plus1.toString());
        DateTime plus2 = dt.plusDays(2);
        assertEquals("2007-04-02T00:00:00.000+03:00", plus2.toString());
    }

    public void test_DateTime_plusDayMidGap_Gaza() {
        DateTime dt = new DateTime(2007, 3, 31, 0, 30, 0, 0, MOCK_GAZA);
        assertEquals("2007-03-31T00:30:00.000+02:00", dt.toString());
        
        DateTime plus1 = dt.plusDays(1);
        assertEquals("2007-04-01T01:30:00.000+03:00", plus1.toString());
        DateTime plus2 = dt.plusDays(2);
        assertEquals("2007-04-02T00:30:00.000+03:00", plus2.toString());
    }

    public void test_DateTime_addWrapFieldDay_Gaza() {
        DateTime dt = new DateTime(2007, 4, 30, 0, 0, 0, 0, MOCK_GAZA);
        assertEquals("2007-04-30T00:00:00.000+03:00", dt.toString());
        
        DateTime plus1 = dt.dayOfMonth().addWrapFieldToCopy(1);
        assertEquals("2007-04-01T01:00:00.000+03:00", plus1.toString());
        DateTime plus2 = dt.dayOfMonth().addWrapFieldToCopy(2);
        assertEquals("2007-04-02T00:00:00.000+03:00", plus2.toString());
    }

    public void test_DateTime_withZoneRetainFields_Gaza() {
        DateTime dt = new DateTime(2007, 4, 1, 0, 0, 0, 0, DateTimeZone.UTC);
        assertEquals("2007-04-01T00:00:00.000Z", dt.toString());
        
        DateTime res = dt.withZoneRetainFields(MOCK_GAZA);
        assertEquals("2007-04-01T01:00:00.000+03:00", res.toString());
    }

    public void test_MutableDateTime_withZoneRetainFields_Gaza() {
        MutableDateTime dt = new MutableDateTime(2007, 4, 1, 0, 0, 0, 0, DateTimeZone.UTC);
        assertEquals("2007-04-01T00:00:00.000Z", dt.toString());
        
        dt.setZoneRetainFields(MOCK_GAZA);
        assertEquals("2007-04-01T01:00:00.000+03:00", dt.toString());
    }

    public void test_LocalDate_new_Gaza() {
        LocalDate date1 = new LocalDate(CUTOVER_GAZA, MOCK_GAZA);
        assertEquals("2007-04-01", date1.toString());
        
        LocalDate date2 = new LocalDate(CUTOVER_GAZA - 1, MOCK_GAZA);
        assertEquals("2007-03-31", date2.toString());
    }

    public void test_LocalDate_toDateTimeAtMidnight_Gaza() {
        LocalDate date = new LocalDate(2007, 4, 1);
        try {
            date.toDateTimeAtMidnight(MOCK_GAZA);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(true, ex.getMessage().startsWith("Illegal instant due to time zone offset transition"));
        }
    }

    public void test_LocalDate_toDateMidnight_Gaza() {
        LocalDate date = new LocalDate(2007, 4, 1);
        try {
            date.toDateMidnight(MOCK_GAZA);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(true, ex.getMessage().startsWith("Illegal instant due to time zone offset transition"));
        }
    }

    public void test_DateTime_new_Gaza() {
        try {
            new DateTime(2007, 4, 1, 0, 0, 0, 0, MOCK_GAZA);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(true, ex.getMessage().indexOf("Illegal instant due to time zone offset transition") >= 0);
        }
    }

    public void test_DateTime_newValid_Gaza() {
        new DateTime(2007, 3, 31, 19, 0, 0, 0, MOCK_GAZA);
        new DateTime(2007, 3, 31, 20, 0, 0, 0, MOCK_GAZA);
        new DateTime(2007, 3, 31, 21, 0, 0, 0, MOCK_GAZA);
        new DateTime(2007, 3, 31, 22, 0, 0, 0, MOCK_GAZA);
        new DateTime(2007, 3, 31, 23, 0, 0, 0, MOCK_GAZA);
        new DateTime(2007, 4, 1, 1, 0, 0, 0, MOCK_GAZA);
        new DateTime(2007, 4, 1, 2, 0, 0, 0, MOCK_GAZA);
        new DateTime(2007, 4, 1, 3, 0, 0, 0, MOCK_GAZA);
    }

    public void test_DateTime_parse_Gaza() {
        try {
            new DateTime("2007-04-01T00:00", MOCK_GAZA);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(true, ex.getMessage().indexOf("Illegal instant due to time zone offset transition") >= 0);
        }
    }

    //-----------------------------------------------------------------------
    //------------------------ Bug [1710316] --------------------------------
    //-----------------------------------------------------------------------
    /** Mock zone simulating America/Grand_Turk cutover at midnight 2007-04-01 */
    private static long CUTOVER_TURK = 1175403600000L;
    private static int OFFSET_TURK = -18000000;  // -05:00
    private static final DateTimeZone MOCK_TURK = new MockZone(CUTOVER_TURK, OFFSET_TURK);

    //-----------------------------------------------------------------------
    public void test_MockTurkIsCorrect() {
        DateTime pre = new DateTime(CUTOVER_TURK - 1L, MOCK_TURK);
        assertEquals("2007-03-31T23:59:59.999-05:00", pre.toString());
        DateTime at = new DateTime(CUTOVER_TURK, MOCK_TURK);
        assertEquals("2007-04-01T01:00:00.000-04:00", at.toString());
        DateTime post = new DateTime(CUTOVER_TURK + 1L, MOCK_TURK);
        assertEquals("2007-04-01T01:00:00.001-04:00", post.toString());
    }

    public void test_getOffsetFromLocal_Turk() {
        doTest_getOffsetFromLocal_Turk(-1, 23, 0, "2007-03-31T23:00:00.000-05:00");
        doTest_getOffsetFromLocal_Turk(-1, 23, 30, "2007-03-31T23:30:00.000-05:00");
        doTest_getOffsetFromLocal_Turk(0, 0, 0, "2007-04-01T01:00:00.000-04:00");
        doTest_getOffsetFromLocal_Turk(0, 0, 30, "2007-04-01T01:30:00.000-04:00");
        doTest_getOffsetFromLocal_Turk(0, 1, 0, "2007-04-01T01:00:00.000-04:00");
        doTest_getOffsetFromLocal_Turk(0, 1, 30, "2007-04-01T01:30:00.000-04:00");
        doTest_getOffsetFromLocal_Turk(0, 2, 0, "2007-04-01T02:00:00.000-04:00");
        doTest_getOffsetFromLocal_Turk(0, 3, 0, "2007-04-01T03:00:00.000-04:00");
        doTest_getOffsetFromLocal_Turk(0, 4, 0, "2007-04-01T04:00:00.000-04:00");
        doTest_getOffsetFromLocal_Turk(0, 5, 0, "2007-04-01T05:00:00.000-04:00");
        doTest_getOffsetFromLocal_Turk(0, 6, 0, "2007-04-01T06:00:00.000-04:00");
    }

    private void doTest_getOffsetFromLocal_Turk(int days, int hour, int min, String expected) {
        DateTime dt = new DateTime(2007, 4, 1, hour, min, 0, 0, DateTimeZone.UTC).plusDays(days);
        int offset = MOCK_TURK.getOffsetFromLocal(dt.getMillis());
        DateTime res = new DateTime(dt.getMillis() - offset, MOCK_TURK);
        assertEquals(res.toString(), expected, res.toString());
    }

    public void test_DateTime_roundFloor_Turk() {
        DateTime dt = new DateTime(2007, 4, 1, 8, 0, 0, 0, MOCK_TURK);
        assertEquals("2007-04-01T08:00:00.000-04:00", dt.toString());
        DateTime rounded = dt.dayOfMonth().roundFloorCopy();
        assertEquals("2007-04-01T01:00:00.000-04:00", rounded.toString());
    }

    public void test_DateTime_roundFloorNotDST_Turk() {
        DateTime dt = new DateTime(2007, 4, 2, 8, 0, 0, 0, MOCK_TURK);
        assertEquals("2007-04-02T08:00:00.000-04:00", dt.toString());
        DateTime rounded = dt.dayOfMonth().roundFloorCopy();
        assertEquals("2007-04-02T00:00:00.000-04:00", rounded.toString());
    }

    public void test_DateTime_roundCeiling_Turk() {
        DateTime dt = new DateTime(2007, 3, 31, 20, 0, 0, 0, MOCK_TURK);
        assertEquals("2007-03-31T20:00:00.000-05:00", dt.toString());
        DateTime rounded = dt.dayOfMonth().roundCeilingCopy();
        assertEquals("2007-04-01T01:00:00.000-04:00", rounded.toString());
    }

    public void test_DateTime_setHourZero_Turk() {
        DateTime dt = new DateTime(2007, 4, 1, 8, 0, 0, 0, MOCK_TURK);
        assertEquals("2007-04-01T08:00:00.000-04:00", dt.toString());
        try {
            dt.hourOfDay().setCopy(0);
            fail();
        } catch (IllegalFieldValueException ex) {
            // expected
        }
    }

    public void test_DateTime_withHourZero_Turk() {
        DateTime dt = new DateTime(2007, 4, 1, 8, 0, 0, 0, MOCK_TURK);
        assertEquals("2007-04-01T08:00:00.000-04:00", dt.toString());
        try {
            dt.withHourOfDay(0);
            fail();
        } catch (IllegalFieldValueException ex) {
            // expected
        }
    }

    public void test_DateTime_withDay_Turk() {
        DateTime dt = new DateTime(2007, 4, 2, 0, 0, 0, 0, MOCK_TURK);
        assertEquals("2007-04-02T00:00:00.000-04:00", dt.toString());
        DateTime res = dt.withDayOfMonth(1);
        assertEquals("2007-04-01T01:00:00.000-04:00", res.toString());
    }

    public void test_DateTime_minusHour_Turk() {
        DateTime dt = new DateTime(2007, 4, 1, 8, 0, 0, 0, MOCK_TURK);
        assertEquals("2007-04-01T08:00:00.000-04:00", dt.toString());
        
        DateTime minus7 = dt.minusHours(7);
        assertEquals("2007-04-01T01:00:00.000-04:00", minus7.toString());
        DateTime minus8 = dt.minusHours(8);
        assertEquals("2007-03-31T23:00:00.000-05:00", minus8.toString());
        DateTime minus9 = dt.minusHours(9);
        assertEquals("2007-03-31T22:00:00.000-05:00", minus9.toString());
    }

    public void test_DateTime_plusHour_Turk() {
        DateTime dt = new DateTime(2007, 3, 31, 16, 0, 0, 0, MOCK_TURK);
        assertEquals("2007-03-31T16:00:00.000-05:00", dt.toString());
        
        DateTime plus7 = dt.plusHours(7);
        assertEquals("2007-03-31T23:00:00.000-05:00", plus7.toString());
        DateTime plus8 = dt.plusHours(8);
        assertEquals("2007-04-01T01:00:00.000-04:00", plus8.toString());
        DateTime plus9 = dt.plusHours(9);
        assertEquals("2007-04-01T02:00:00.000-04:00", plus9.toString());
    }

    public void test_DateTime_minusDay_Turk() {
        DateTime dt = new DateTime(2007, 4, 2, 0, 0, 0, 0, MOCK_TURK);
        assertEquals("2007-04-02T00:00:00.000-04:00", dt.toString());
        
        DateTime minus1 = dt.minusDays(1);
        assertEquals("2007-04-01T01:00:00.000-04:00", minus1.toString());
        DateTime minus2 = dt.minusDays(2);
        assertEquals("2007-03-31T00:00:00.000-05:00", minus2.toString());
    }

    public void test_DateTime_plusDay_Turk() {
        DateTime dt = new DateTime(2007, 3, 31, 0, 0, 0, 0, MOCK_TURK);
        assertEquals("2007-03-31T00:00:00.000-05:00", dt.toString());
        
        DateTime plus1 = dt.plusDays(1);
        assertEquals("2007-04-01T01:00:00.000-04:00", plus1.toString());
        DateTime plus2 = dt.plusDays(2);
        assertEquals("2007-04-02T00:00:00.000-04:00", plus2.toString());
    }

    public void test_DateTime_plusDayMidGap_Turk() {
        DateTime dt = new DateTime(2007, 3, 31, 0, 30, 0, 0, MOCK_TURK);
        assertEquals("2007-03-31T00:30:00.000-05:00", dt.toString());
        
        DateTime plus1 = dt.plusDays(1);
        assertEquals("2007-04-01T01:30:00.000-04:00", plus1.toString());
        DateTime plus2 = dt.plusDays(2);
        assertEquals("2007-04-02T00:30:00.000-04:00", plus2.toString());
    }

    public void test_DateTime_addWrapFieldDay_Turk() {
        DateTime dt = new DateTime(2007, 4, 30, 0, 0, 0, 0, MOCK_TURK);
        assertEquals("2007-04-30T00:00:00.000-04:00", dt.toString());
        
        DateTime plus1 = dt.dayOfMonth().addWrapFieldToCopy(1);
        assertEquals("2007-04-01T01:00:00.000-04:00", plus1.toString());
        DateTime plus2 = dt.dayOfMonth().addWrapFieldToCopy(2);
        assertEquals("2007-04-02T00:00:00.000-04:00", plus2.toString());
    }

    public void test_DateTime_withZoneRetainFields_Turk() {
        DateTime dt = new DateTime(2007, 4, 1, 0, 0, 0, 0, DateTimeZone.UTC);
        assertEquals("2007-04-01T00:00:00.000Z", dt.toString());
        
        DateTime res = dt.withZoneRetainFields(MOCK_TURK);
        assertEquals("2007-04-01T01:00:00.000-04:00", res.toString());
    }

    public void test_MutableDateTime_setZoneRetainFields_Turk() {
        MutableDateTime dt = new MutableDateTime(2007, 4, 1, 0, 0, 0, 0, DateTimeZone.UTC);
        assertEquals("2007-04-01T00:00:00.000Z", dt.toString());
        
        dt.setZoneRetainFields(MOCK_TURK);
        assertEquals("2007-04-01T01:00:00.000-04:00", dt.toString());
    }

    public void test_LocalDate_new_Turk() {
        LocalDate date1 = new LocalDate(CUTOVER_TURK, MOCK_TURK);
        assertEquals("2007-04-01", date1.toString());
        
        LocalDate date2 = new LocalDate(CUTOVER_TURK - 1, MOCK_TURK);
        assertEquals("2007-03-31", date2.toString());
    }

    public void test_LocalDate_toDateTimeAtMidnight_Turk() {
        LocalDate date = new LocalDate(2007, 4, 1);
        try {
            date.toDateTimeAtMidnight(MOCK_TURK);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(true, ex.getMessage().startsWith("Illegal instant due to time zone offset transition"));
        }
    }

    public void test_LocalDate_toDateMidnight_Turk() {
        LocalDate date = new LocalDate(2007, 4, 1);
        try {
            date.toDateMidnight(MOCK_TURK);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(true, ex.getMessage().startsWith("Illegal instant due to time zone offset transition"));
        }
    }

    public void test_DateTime_new_Turk() {
        try {
            new DateTime(2007, 4, 1, 0, 0, 0, 0, MOCK_TURK);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(true, ex.getMessage().indexOf("Illegal instant due to time zone offset transition") >= 0);
        }
    }

    public void test_DateTime_newValid_Turk() {
        new DateTime(2007, 3, 31, 23, 0, 0, 0, MOCK_TURK);
        new DateTime(2007, 4, 1, 1, 0, 0, 0, MOCK_TURK);
        new DateTime(2007, 4, 1, 2, 0, 0, 0, MOCK_TURK);
        new DateTime(2007, 4, 1, 3, 0, 0, 0, MOCK_TURK);
        new DateTime(2007, 4, 1, 4, 0, 0, 0, MOCK_TURK);
        new DateTime(2007, 4, 1, 5, 0, 0, 0, MOCK_TURK);
        new DateTime(2007, 4, 1, 6, 0, 0, 0, MOCK_TURK);
    }

    public void test_DateTime_parse_Turk() {
        try {
            new DateTime("2007-04-01T00:00", MOCK_TURK);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(true, ex.getMessage().indexOf("Illegal instant due to time zone offset transition") >= 0);
        }
    }

    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
    /** America/New_York cutover from 01:59 to 03:00 on 2007-03-11 */
    private static long CUTOVER_NEW_YORK_SPRING = 1173596400000L;  // 2007-03-11T03:00:00.000-04:00
    private static final DateTimeZone ZONE_NEW_YORK = DateTimeZone.forID("America/New_York");
//  DateTime x = new DateTime(2007, 1, 1, 0, 0, 0, 0, ZONE_NEW_YORK);
//  System.out.println(ZONE_NEW_YORK.nextTransition(x.getMillis()));
//  DateTime y = new DateTime(ZONE_NEW_YORK.nextTransition(x.getMillis()), ZONE_NEW_YORK);
//  System.out.println(y);

    //-----------------------------------------------------------------------
    public void test_NewYorkIsCorrect_Spring() {
        DateTime pre = new DateTime(CUTOVER_NEW_YORK_SPRING - 1L, ZONE_NEW_YORK);
        assertEquals("2007-03-11T01:59:59.999-05:00", pre.toString());
        DateTime at = new DateTime(CUTOVER_NEW_YORK_SPRING, ZONE_NEW_YORK);
        assertEquals("2007-03-11T03:00:00.000-04:00", at.toString());
        DateTime post = new DateTime(CUTOVER_NEW_YORK_SPRING + 1L, ZONE_NEW_YORK);
        assertEquals("2007-03-11T03:00:00.001-04:00", post.toString());
    }

    public void test_getOffsetFromLocal_NewYork_Spring() {
        doTest_getOffsetFromLocal(3, 11, 1, 0, "2007-03-11T01:00:00.000-05:00", ZONE_NEW_YORK);
        doTest_getOffsetFromLocal(3, 11, 1,30, "2007-03-11T01:30:00.000-05:00", ZONE_NEW_YORK);
        
        doTest_getOffsetFromLocal(3, 11, 2, 0, "2007-03-11T03:00:00.000-04:00", ZONE_NEW_YORK);
        doTest_getOffsetFromLocal(3, 11, 2,30, "2007-03-11T03:30:00.000-04:00", ZONE_NEW_YORK);
        
        doTest_getOffsetFromLocal(3, 11, 3, 0, "2007-03-11T03:00:00.000-04:00", ZONE_NEW_YORK);
        doTest_getOffsetFromLocal(3, 11, 3,30, "2007-03-11T03:30:00.000-04:00", ZONE_NEW_YORK);
        doTest_getOffsetFromLocal(3, 11, 4, 0, "2007-03-11T04:00:00.000-04:00", ZONE_NEW_YORK);
        doTest_getOffsetFromLocal(3, 11, 5, 0, "2007-03-11T05:00:00.000-04:00", ZONE_NEW_YORK);
        doTest_getOffsetFromLocal(3, 11, 6, 0, "2007-03-11T06:00:00.000-04:00", ZONE_NEW_YORK);
        doTest_getOffsetFromLocal(3, 11, 7, 0, "2007-03-11T07:00:00.000-04:00", ZONE_NEW_YORK);
        doTest_getOffsetFromLocal(3, 11, 8, 0, "2007-03-11T08:00:00.000-04:00", ZONE_NEW_YORK);
    }

    public void test_DateTime_setHourAcross_NewYork_Spring() {
        DateTime dt = new DateTime(2007, 3, 11, 0, 0, 0, 0, ZONE_NEW_YORK);
        assertEquals("2007-03-11T00:00:00.000-05:00", dt.toString());
        DateTime res = dt.hourOfDay().setCopy(4);
        assertEquals("2007-03-11T04:00:00.000-04:00", res.toString());
    }

    public void test_DateTime_setHourForward_NewYork_Spring() {
        DateTime dt = new DateTime(2007, 3, 11, 0, 0, 0, 0, ZONE_NEW_YORK);
        assertEquals("2007-03-11T00:00:00.000-05:00", dt.toString());
        
        try {
            dt.hourOfDay().setCopy(2);
            fail();
        } catch (IllegalFieldValueException ex) {
            // expected
        }
    }

    public void test_DateTime_setHourBack_NewYork_Spring() {
        DateTime dt = new DateTime(2007, 3, 11, 8, 0, 0, 0, ZONE_NEW_YORK);
        assertEquals("2007-03-11T08:00:00.000-04:00", dt.toString());
        
        try {
            dt.hourOfDay().setCopy(2);
            fail();
        } catch (IllegalFieldValueException ex) {
            // expected
        }
    }

    //-----------------------------------------------------------------------
    public void test_DateTime_roundFloor_day_NewYork_Spring_preCutover() {
        DateTime dt = new DateTime(2007, 3, 11, 1, 30, 0, 0, ZONE_NEW_YORK);
        assertEquals("2007-03-11T01:30:00.000-05:00", dt.toString());
        DateTime rounded = dt.dayOfMonth().roundFloorCopy();
        assertEquals("2007-03-11T00:00:00.000-05:00", rounded.toString());
    }

    public void test_DateTime_roundFloor_day_NewYork_Spring_postCutover() {
        DateTime dt = new DateTime(2007, 3, 11, 3, 30, 0, 0, ZONE_NEW_YORK);
        assertEquals("2007-03-11T03:30:00.000-04:00", dt.toString());
        DateTime rounded = dt.dayOfMonth().roundFloorCopy();
        assertEquals("2007-03-11T00:00:00.000-05:00", rounded.toString());
    }

    public void test_DateTime_roundFloor_hour_NewYork_Spring_preCutover() {
        DateTime dt = new DateTime(2007, 3, 11, 1, 30, 0, 0, ZONE_NEW_YORK);
        assertEquals("2007-03-11T01:30:00.000-05:00", dt.toString());
        DateTime rounded = dt.hourOfDay().roundFloorCopy();
        assertEquals("2007-03-11T01:00:00.000-05:00", rounded.toString());
    }

    public void test_DateTime_roundFloor_hour_NewYork_Spring_postCutover() {
        DateTime dt = new DateTime(2007, 3, 11, 3, 30, 0, 0, ZONE_NEW_YORK);
        assertEquals("2007-03-11T03:30:00.000-04:00", dt.toString());
        DateTime rounded = dt.hourOfDay().roundFloorCopy();
        assertEquals("2007-03-11T03:00:00.000-04:00", rounded.toString());
    }

    public void test_DateTime_roundFloor_minute_NewYork_Spring_preCutover() {
        DateTime dt = new DateTime(2007, 3, 11, 1, 30, 40, 0, ZONE_NEW_YORK);
        assertEquals("2007-03-11T01:30:40.000-05:00", dt.toString());
        DateTime rounded = dt.minuteOfHour().roundFloorCopy();
        assertEquals("2007-03-11T01:30:00.000-05:00", rounded.toString());
    }

    public void test_DateTime_roundFloor_minute_NewYork_Spring_postCutover() {
        DateTime dt = new DateTime(2007, 3, 11, 3, 30, 40, 0, ZONE_NEW_YORK);
        assertEquals("2007-03-11T03:30:40.000-04:00", dt.toString());
        DateTime rounded = dt.minuteOfHour().roundFloorCopy();
        assertEquals("2007-03-11T03:30:00.000-04:00", rounded.toString());
    }

    //-----------------------------------------------------------------------
    public void test_DateTime_roundCeiling_day_NewYork_Spring_preCutover() {
        DateTime dt = new DateTime(2007, 3, 11, 1, 30, 0, 0, ZONE_NEW_YORK);
        assertEquals("2007-03-11T01:30:00.000-05:00", dt.toString());
        DateTime rounded = dt.dayOfMonth().roundCeilingCopy();
        assertEquals("2007-03-12T00:00:00.000-04:00", rounded.toString());
    }

    public void test_DateTime_roundCeiling_day_NewYork_Spring_postCutover() {
        DateTime dt = new DateTime(2007, 3, 11, 3, 30, 0, 0, ZONE_NEW_YORK);
        assertEquals("2007-03-11T03:30:00.000-04:00", dt.toString());
        DateTime rounded = dt.dayOfMonth().roundCeilingCopy();
        assertEquals("2007-03-12T00:00:00.000-04:00", rounded.toString());
    }

    public void test_DateTime_roundCeiling_hour_NewYork_Spring_preCutover() {
        DateTime dt = new DateTime(2007, 3, 11, 1, 30, 0, 0, ZONE_NEW_YORK);
        assertEquals("2007-03-11T01:30:00.000-05:00", dt.toString());
        DateTime rounded = dt.hourOfDay().roundCeilingCopy();
        assertEquals("2007-03-11T03:00:00.000-04:00", rounded.toString());
    }

    public void test_DateTime_roundCeiling_hour_NewYork_Spring_postCutover() {
        DateTime dt = new DateTime(2007, 3, 11, 3, 30, 0, 0, ZONE_NEW_YORK);
        assertEquals("2007-03-11T03:30:00.000-04:00", dt.toString());
        DateTime rounded = dt.hourOfDay().roundCeilingCopy();
        assertEquals("2007-03-11T04:00:00.000-04:00", rounded.toString());
    }

    public void test_DateTime_roundCeiling_minute_NewYork_Spring_preCutover() {
        DateTime dt = new DateTime(2007, 3, 11, 1, 30, 40, 0, ZONE_NEW_YORK);
        assertEquals("2007-03-11T01:30:40.000-05:00", dt.toString());
        DateTime rounded = dt.minuteOfHour().roundCeilingCopy();
        assertEquals("2007-03-11T01:31:00.000-05:00", rounded.toString());
    }

    public void test_DateTime_roundCeiling_minute_NewYork_Spring_postCutover() {
        DateTime dt = new DateTime(2007, 3, 11, 3, 30, 40, 0, ZONE_NEW_YORK);
        assertEquals("2007-03-11T03:30:40.000-04:00", dt.toString());
        DateTime rounded = dt.minuteOfHour().roundCeilingCopy();
        assertEquals("2007-03-11T03:31:00.000-04:00", rounded.toString());
    }

    //-----------------------------------------------------------------------
    /** America/New_York cutover from 01:59 to 01:00 on 2007-11-04 */
    private static long CUTOVER_NEW_YORK_AUTUMN = 1194156000000L;  // 2007-11-04T01:00:00.000-05:00

    //-----------------------------------------------------------------------
    public void test_NewYorkIsCorrect_Autumn() {
        DateTime pre = new DateTime(CUTOVER_NEW_YORK_AUTUMN - 1L, ZONE_NEW_YORK);
        assertEquals("2007-11-04T01:59:59.999-04:00", pre.toString());
        DateTime at = new DateTime(CUTOVER_NEW_YORK_AUTUMN, ZONE_NEW_YORK);
        assertEquals("2007-11-04T01:00:00.000-05:00", at.toString());
        DateTime post = new DateTime(CUTOVER_NEW_YORK_AUTUMN + 1L, ZONE_NEW_YORK);
        assertEquals("2007-11-04T01:00:00.001-05:00", post.toString());
    }

    public void test_getOffsetFromLocal_NewYork_Autumn() {
        doTest_getOffsetFromLocal(11, 4, 0, 0, "2007-11-04T00:00:00.000-04:00", ZONE_NEW_YORK);
        doTest_getOffsetFromLocal(11, 4, 0,30, "2007-11-04T00:30:00.000-04:00", ZONE_NEW_YORK);
        
        doTest_getOffsetFromLocal(11, 4, 1, 0, "2007-11-04T01:00:00.000-04:00", ZONE_NEW_YORK);
        doTest_getOffsetFromLocal(11, 4, 1,30, "2007-11-04T01:30:00.000-04:00", ZONE_NEW_YORK);
        
        doTest_getOffsetFromLocal(11, 4, 2, 0, "2007-11-04T02:00:00.000-05:00", ZONE_NEW_YORK);
        doTest_getOffsetFromLocal(11, 4, 2,30, "2007-11-04T02:30:00.000-05:00", ZONE_NEW_YORK);
        doTest_getOffsetFromLocal(11, 4, 3, 0, "2007-11-04T03:00:00.000-05:00", ZONE_NEW_YORK);
        doTest_getOffsetFromLocal(11, 4, 3,30, "2007-11-04T03:30:00.000-05:00", ZONE_NEW_YORK);
        doTest_getOffsetFromLocal(11, 4, 4, 0, "2007-11-04T04:00:00.000-05:00", ZONE_NEW_YORK);
        doTest_getOffsetFromLocal(11, 4, 5, 0, "2007-11-04T05:00:00.000-05:00", ZONE_NEW_YORK);
        doTest_getOffsetFromLocal(11, 4, 6, 0, "2007-11-04T06:00:00.000-05:00", ZONE_NEW_YORK);
        doTest_getOffsetFromLocal(11, 4, 7, 0, "2007-11-04T07:00:00.000-05:00", ZONE_NEW_YORK);
        doTest_getOffsetFromLocal(11, 4, 8, 0, "2007-11-04T08:00:00.000-05:00", ZONE_NEW_YORK);
    }

    public void test_DateTime_plusHour_NewYork_Autumn() {
        DateTime dt = new DateTime(2007, 11, 3, 18, 0, 0, 0, ZONE_NEW_YORK);
        assertEquals("2007-11-03T18:00:00.000-04:00", dt.toString());
        
        DateTime plus6 = dt.plusHours(6);
        assertEquals("2007-11-04T00:00:00.000-04:00", plus6.toString());
        DateTime plus7 = dt.plusHours(7);
        assertEquals("2007-11-04T01:00:00.000-04:00", plus7.toString());
        DateTime plus8 = dt.plusHours(8);
        assertEquals("2007-11-04T01:00:00.000-05:00", plus8.toString());
        DateTime plus9 = dt.plusHours(9);
        assertEquals("2007-11-04T02:00:00.000-05:00", plus9.toString());
    }

    public void test_DateTime_minusHour_NewYork_Autumn() {
        DateTime dt = new DateTime(2007, 11, 4, 8, 0, 0, 0, ZONE_NEW_YORK);
        assertEquals("2007-11-04T08:00:00.000-05:00", dt.toString());
        
        DateTime minus6 = dt.minusHours(6);
        assertEquals("2007-11-04T02:00:00.000-05:00", minus6.toString());
        DateTime minus7 = dt.minusHours(7);
        assertEquals("2007-11-04T01:00:00.000-05:00", minus7.toString());
        DateTime minus8 = dt.minusHours(8);
        assertEquals("2007-11-04T01:00:00.000-04:00", minus8.toString());
        DateTime minus9 = dt.minusHours(9);
        assertEquals("2007-11-04T00:00:00.000-04:00", minus9.toString());
    }

    //-----------------------------------------------------------------------
    public void test_DateTime_roundFloor_day_NewYork_Autumn_preCutover() {
        DateTime dt = new DateTime(2007, 11, 4, 1, 30, 0, 0, ZONE_NEW_YORK);
        assertEquals("2007-11-04T01:30:00.000-04:00", dt.toString());
        DateTime rounded = dt.dayOfMonth().roundFloorCopy();
        assertEquals("2007-11-04T00:00:00.000-04:00", rounded.toString());
    }

    public void test_DateTime_roundFloor_day_NewYork_Autumn_postCutover() {
        DateTime dt = new DateTime(2007, 11, 4, 1, 30, 0, 0, ZONE_NEW_YORK).plusHours(1);
        assertEquals("2007-11-04T01:30:00.000-05:00", dt.toString());
        DateTime rounded = dt.dayOfMonth().roundFloorCopy();
        assertEquals("2007-11-04T00:00:00.000-04:00", rounded.toString());
    }

    public void test_DateTime_roundFloor_hourOfDay_NewYork_Autumn_preCutover() {
        DateTime dt = new DateTime(2007, 11, 4, 1, 30, 0, 0, ZONE_NEW_YORK);
        assertEquals("2007-11-04T01:30:00.000-04:00", dt.toString());
        DateTime rounded = dt.hourOfDay().roundFloorCopy();
        assertEquals("2007-11-04T01:00:00.000-04:00", rounded.toString());
    }

    public void test_DateTime_roundFloor_hourOfDay_NewYork_Autumn_postCutover() {
        DateTime dt = new DateTime(2007, 11, 4, 1, 30, 0, 0, ZONE_NEW_YORK).plusHours(1);
        assertEquals("2007-11-04T01:30:00.000-05:00", dt.toString());
        DateTime rounded = dt.hourOfDay().roundFloorCopy();
        assertEquals("2007-11-04T01:00:00.000-05:00", rounded.toString());
    }

    public void test_DateTime_roundFloor_minuteOfHour_NewYork_Autumn_preCutover() {
        DateTime dt = new DateTime(2007, 11, 4, 1, 30, 40, 0, ZONE_NEW_YORK);
        assertEquals("2007-11-04T01:30:40.000-04:00", dt.toString());
        DateTime rounded = dt.minuteOfHour().roundFloorCopy();
        assertEquals("2007-11-04T01:30:00.000-04:00", rounded.toString());
    }

    public void test_DateTime_roundFloor_minuteOfHour_NewYork_Autumn_postCutover() {
        DateTime dt = new DateTime(2007, 11, 4, 1, 30, 40, 0, ZONE_NEW_YORK).plusHours(1);
        assertEquals("2007-11-04T01:30:40.000-05:00", dt.toString());
        DateTime rounded = dt.minuteOfHour().roundFloorCopy();
        assertEquals("2007-11-04T01:30:00.000-05:00", rounded.toString());
    }

    public void test_DateTime_roundFloor_secondOfMinute_NewYork_Autumn_preCutover() {
        DateTime dt = new DateTime(2007, 11, 4, 1, 30, 40, 500, ZONE_NEW_YORK);
        assertEquals("2007-11-04T01:30:40.500-04:00", dt.toString());
        DateTime rounded = dt.secondOfMinute().roundFloorCopy();
        assertEquals("2007-11-04T01:30:40.000-04:00", rounded.toString());
    }

    public void test_DateTime_roundFloor_secondOfMinute_NewYork_Autumn_postCutover() {
        DateTime dt = new DateTime(2007, 11, 4, 1, 30, 40, 500, ZONE_NEW_YORK).plusHours(1);
        assertEquals("2007-11-04T01:30:40.500-05:00", dt.toString());
        DateTime rounded = dt.secondOfMinute().roundFloorCopy();
        assertEquals("2007-11-04T01:30:40.000-05:00", rounded.toString());
    }

    //-----------------------------------------------------------------------
    public void test_DateTime_roundCeiling_day_NewYork_Autumn_preCutover() {
        DateTime dt = new DateTime(2007, 11, 4, 1, 30, 0, 0, ZONE_NEW_YORK);
        assertEquals("2007-11-04T01:30:00.000-04:00", dt.toString());
        DateTime rounded = dt.dayOfMonth().roundCeilingCopy();
        assertEquals("2007-11-05T00:00:00.000-05:00", rounded.toString());
    }

    public void test_DateTime_roundCeiling_day_NewYork_Autumn_postCutover() {
        DateTime dt = new DateTime(2007, 11, 4, 1, 30, 0, 0, ZONE_NEW_YORK).plusHours(1);
        assertEquals("2007-11-04T01:30:00.000-05:00", dt.toString());
        DateTime rounded = dt.dayOfMonth().roundCeilingCopy();
        assertEquals("2007-11-05T00:00:00.000-05:00", rounded.toString());
    }

    public void test_DateTime_roundCeiling_hourOfDay_NewYork_Autumn_preCutover() {
        DateTime dt = new DateTime(2007, 11, 4, 1, 30, 0, 0, ZONE_NEW_YORK);
        assertEquals("2007-11-04T01:30:00.000-04:00", dt.toString());
        DateTime rounded = dt.hourOfDay().roundCeilingCopy();
        assertEquals("2007-11-04T01:00:00.000-05:00", rounded.toString());
    }

    public void test_DateTime_roundCeiling_hourOfDay_NewYork_Autumn_postCutover() {
        DateTime dt = new DateTime(2007, 11, 4, 1, 30, 0, 0, ZONE_NEW_YORK).plusHours(1);
        assertEquals("2007-11-04T01:30:00.000-05:00", dt.toString());
        DateTime rounded = dt.hourOfDay().roundCeilingCopy();
        assertEquals("2007-11-04T02:00:00.000-05:00", rounded.toString());
    }

    public void test_DateTime_roundCeiling_minuteOfHour_NewYork_Autumn_preCutover() {
        DateTime dt = new DateTime(2007, 11, 4, 1, 30, 40, 0, ZONE_NEW_YORK);
        assertEquals("2007-11-04T01:30:40.000-04:00", dt.toString());
        DateTime rounded = dt.minuteOfHour().roundCeilingCopy();
        assertEquals("2007-11-04T01:31:00.000-04:00", rounded.toString());
    }

    public void test_DateTime_roundCeiling_minuteOfHour_NewYork_Autumn_postCutover() {
        DateTime dt = new DateTime(2007, 11, 4, 1, 30, 40, 0, ZONE_NEW_YORK).plusHours(1);
        assertEquals("2007-11-04T01:30:40.000-05:00", dt.toString());
        DateTime rounded = dt.minuteOfHour().roundCeilingCopy();
        assertEquals("2007-11-04T01:31:00.000-05:00", rounded.toString());
    }

    public void test_DateTime_roundCeiling_secondOfMinute_NewYork_Autumn_preCutover() {
        DateTime dt = new DateTime(2007, 11, 4, 1, 30, 40, 500, ZONE_NEW_YORK);
        assertEquals("2007-11-04T01:30:40.500-04:00", dt.toString());
        DateTime rounded = dt.secondOfMinute().roundCeilingCopy();
        assertEquals("2007-11-04T01:30:41.000-04:00", rounded.toString());
    }

    public void test_DateTime_roundCeiling_secondOfMinute_NewYork_Autumn_postCutover() {
        DateTime dt = new DateTime(2007, 11, 4, 1, 30, 40, 500, ZONE_NEW_YORK).plusHours(1);
        assertEquals("2007-11-04T01:30:40.500-05:00", dt.toString());
        DateTime rounded = dt.secondOfMinute().roundCeilingCopy();
        assertEquals("2007-11-04T01:30:41.000-05:00", rounded.toString());
    }

    //-----------------------------------------------------------------------
    /** Europe/Moscow cutover from 01:59 to 03:00 on 2007-03-25 */
    private static long CUTOVER_MOSCOW_SPRING = 1174777200000L;  // 2007-03-25T03:00:00.000+04:00
    private static final DateTimeZone ZONE_MOSCOW = DateTimeZone.forID("Europe/Moscow");

    //-----------------------------------------------------------------------
    public void test_MoscowIsCorrect_Spring() {
//      DateTime x = new DateTime(2007, 7, 1, 0, 0, 0, 0, ZONE_MOSCOW);
//      System.out.println(ZONE_MOSCOW.nextTransition(x.getMillis()));
//      DateTime y = new DateTime(ZONE_MOSCOW.nextTransition(x.getMillis()), ZONE_MOSCOW);
//      System.out.println(y);
        DateTime pre = new DateTime(CUTOVER_MOSCOW_SPRING - 1L, ZONE_MOSCOW);
        assertEquals("2007-03-25T01:59:59.999+03:00", pre.toString());
        DateTime at = new DateTime(CUTOVER_MOSCOW_SPRING, ZONE_MOSCOW);
        assertEquals("2007-03-25T03:00:00.000+04:00", at.toString());
        DateTime post = new DateTime(CUTOVER_MOSCOW_SPRING + 1L, ZONE_MOSCOW);
        assertEquals("2007-03-25T03:00:00.001+04:00", post.toString());
    }

    public void test_getOffsetFromLocal_Moscow_Spring() {
        doTest_getOffsetFromLocal(3, 25, 1, 0, "2007-03-25T01:00:00.000+03:00", ZONE_MOSCOW);
        doTest_getOffsetFromLocal(3, 25, 1,30, "2007-03-25T01:30:00.000+03:00", ZONE_MOSCOW);
        
        doTest_getOffsetFromLocal(3, 25, 2, 0, "2007-03-25T03:00:00.000+04:00", ZONE_MOSCOW);
        doTest_getOffsetFromLocal(3, 25, 2,30, "2007-03-25T03:30:00.000+04:00", ZONE_MOSCOW);
        
        doTest_getOffsetFromLocal(3, 25, 3, 0, "2007-03-25T03:00:00.000+04:00", ZONE_MOSCOW);
        doTest_getOffsetFromLocal(3, 25, 3,30, "2007-03-25T03:30:00.000+04:00", ZONE_MOSCOW);
        doTest_getOffsetFromLocal(3, 25, 4, 0, "2007-03-25T04:00:00.000+04:00", ZONE_MOSCOW);
        doTest_getOffsetFromLocal(3, 25, 5, 0, "2007-03-25T05:00:00.000+04:00", ZONE_MOSCOW);
        doTest_getOffsetFromLocal(3, 25, 6, 0, "2007-03-25T06:00:00.000+04:00", ZONE_MOSCOW);
        doTest_getOffsetFromLocal(3, 25, 7, 0, "2007-03-25T07:00:00.000+04:00", ZONE_MOSCOW);
        doTest_getOffsetFromLocal(3, 25, 8, 0, "2007-03-25T08:00:00.000+04:00", ZONE_MOSCOW);
    }

    public void test_DateTime_setHourAcross_Moscow_Spring() {
        DateTime dt = new DateTime(2007, 3, 25, 0, 0, 0, 0, ZONE_MOSCOW);
        assertEquals("2007-03-25T00:00:00.000+03:00", dt.toString());
        DateTime res = dt.hourOfDay().setCopy(4);
        assertEquals("2007-03-25T04:00:00.000+04:00", res.toString());
    }

    public void test_DateTime_setHourForward_Moscow_Spring() {
        DateTime dt = new DateTime(2007, 3, 25, 0, 0, 0, 0, ZONE_MOSCOW);
        assertEquals("2007-03-25T00:00:00.000+03:00", dt.toString());
        
        try {
            dt.hourOfDay().setCopy(2);
            fail();
        } catch (IllegalFieldValueException ex) {
            // expected
        }
    }

    public void test_DateTime_setHourBack_Moscow_Spring() {
        DateTime dt = new DateTime(2007, 3, 25, 8, 0, 0, 0, ZONE_MOSCOW);
        assertEquals("2007-03-25T08:00:00.000+04:00", dt.toString());
        
        try {
            dt.hourOfDay().setCopy(2);
            fail();
        } catch (IllegalFieldValueException ex) {
            // expected
        }
    }

    //-----------------------------------------------------------------------
    /** America/New_York cutover from 02:59 to 02:00 on 2007-10-28 */
    private static long CUTOVER_MOSCOW_AUTUMN = 1193526000000L;  // 2007-10-28T02:00:00.000+03:00

    //-----------------------------------------------------------------------
    public void test_MoscowIsCorrect_Autumn() {
        DateTime pre = new DateTime(CUTOVER_MOSCOW_AUTUMN - 1L, ZONE_MOSCOW);
        assertEquals("2007-10-28T02:59:59.999+04:00", pre.toString());
        DateTime at = new DateTime(CUTOVER_MOSCOW_AUTUMN, ZONE_MOSCOW);
        assertEquals("2007-10-28T02:00:00.000+03:00", at.toString());
        DateTime post = new DateTime(CUTOVER_MOSCOW_AUTUMN + 1L, ZONE_MOSCOW);
        assertEquals("2007-10-28T02:00:00.001+03:00", post.toString());
    }

    // broken, and getOffsetFromLocal has no obvious way to determine which is correct
//    public void test_getOffsetFromLocal_Moscow_Autumn() {
//        doTest_getOffsetFromLocal(10, 28, 0, 0, "2007-10-28T00:00:00.000+04:00", ZONE_MOSCOW);
//        doTest_getOffsetFromLocal(10, 28, 0,30, "2007-10-28T00:30:00.000+04:00", ZONE_MOSCOW);
//        doTest_getOffsetFromLocal(10, 28, 1, 0, "2007-10-28T01:00:00.000+04:00", ZONE_MOSCOW);
//        doTest_getOffsetFromLocal(10, 28, 1,30, "2007-10-28T01:30:00.000+04:00", ZONE_MOSCOW);
//        
//        doTest_getOffsetFromLocal(10, 28, 2, 0, "2007-10-28T02:00:00.000+04:00", ZONE_MOSCOW);
//        doTest_getOffsetFromLocal(10, 28, 2,30, "2007-10-28T02:30:00.000+04:00", ZONE_MOSCOW);
//        
//        doTest_getOffsetFromLocal(10, 28, 3, 0, "2007-10-28T03:00:00.000+03:00", ZONE_MOSCOW);
//        doTest_getOffsetFromLocal(10, 28, 3,30, "2007-10-28T03:30:00.000+03:00", ZONE_MOSCOW);
//        doTest_getOffsetFromLocal(10, 28, 4, 0, "2007-10-28T04:00:00.000+03:00", ZONE_MOSCOW);
//        doTest_getOffsetFromLocal(10, 28, 5, 0, "2007-10-28T05:00:00.000+03:00", ZONE_MOSCOW);
//        doTest_getOffsetFromLocal(10, 28, 6, 0, "2007-10-28T06:00:00.000+03:00", ZONE_MOSCOW);
//        doTest_getOffsetFromLocal(10, 28, 7, 0, "2007-10-28T07:00:00.000+03:00", ZONE_MOSCOW);
//        doTest_getOffsetFromLocal(10, 28, 8, 0, "2007-10-28T08:00:00.000+03:00", ZONE_MOSCOW);
//    }

    public void test_DateTime_plusHour_Moscow_Autumn() {
        DateTime dt = new DateTime(2007, 10, 27, 19, 0, 0, 0, ZONE_MOSCOW);
        assertEquals("2007-10-27T19:00:00.000+04:00", dt.toString());
        
        DateTime plus6 = dt.plusHours(6);
        assertEquals("2007-10-28T01:00:00.000+04:00", plus6.toString());
        DateTime plus7 = dt.plusHours(7);
        assertEquals("2007-10-28T02:00:00.000+04:00", plus7.toString());
        DateTime plus8 = dt.plusHours(8);
        assertEquals("2007-10-28T02:00:00.000+03:00", plus8.toString());
        DateTime plus9 = dt.plusHours(9);
        assertEquals("2007-10-28T03:00:00.000+03:00", plus9.toString());
    }

    public void test_DateTime_minusHour_Moscow_Autumn() {
        DateTime dt = new DateTime(2007, 10, 28, 9, 0, 0, 0, ZONE_MOSCOW);
        assertEquals("2007-10-28T09:00:00.000+03:00", dt.toString());
        
        DateTime minus6 = dt.minusHours(6);
        assertEquals("2007-10-28T03:00:00.000+03:00", minus6.toString());
        DateTime minus7 = dt.minusHours(7);
        assertEquals("2007-10-28T02:00:00.000+03:00", minus7.toString());
        DateTime minus8 = dt.minusHours(8);
        assertEquals("2007-10-28T02:00:00.000+04:00", minus8.toString());
        DateTime minus9 = dt.minusHours(9);
        assertEquals("2007-10-28T01:00:00.000+04:00", minus9.toString());
    }

    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
    /** America/Guatemala cutover from 23:59 to 23:00 on 2006-09-30 */
    private static long CUTOVER_GUATEMALA_AUTUMN = 1159678800000L; // 2006-09-30T23:00:00.000-06:00
    private static final DateTimeZone ZONE_GUATEMALA = DateTimeZone.forID("America/Guatemala");

    //-----------------------------------------------------------------------
    public void test_GuatemataIsCorrect_Autumn() {
        DateTime pre = new DateTime(CUTOVER_GUATEMALA_AUTUMN - 1L, ZONE_GUATEMALA);
        assertEquals("2006-09-30T23:59:59.999-05:00", pre.toString());
        DateTime at = new DateTime(CUTOVER_GUATEMALA_AUTUMN, ZONE_GUATEMALA);
        assertEquals("2006-09-30T23:00:00.000-06:00", at.toString());
        DateTime post = new DateTime(CUTOVER_GUATEMALA_AUTUMN + 1L, ZONE_GUATEMALA);
        assertEquals("2006-09-30T23:00:00.001-06:00", post.toString());
    }

    public void test_getOffsetFromLocal_Guatemata_Autumn() {
        doTest_getOffsetFromLocal( 2006, 9,30,23, 0,
                                  "2006-09-30T23:00:00.000-05:00", ZONE_GUATEMALA);
        doTest_getOffsetFromLocal( 2006, 9,30,23,30,
                                  "2006-09-30T23:30:00.000-05:00", ZONE_GUATEMALA);
        
        doTest_getOffsetFromLocal( 2006, 9,30,23, 0,
                                  "2006-09-30T23:00:00.000-05:00", ZONE_GUATEMALA);
        doTest_getOffsetFromLocal( 2006, 9,30,23,30,
                                  "2006-09-30T23:30:00.000-05:00", ZONE_GUATEMALA);
        
        doTest_getOffsetFromLocal( 2006,10, 1, 0, 0,
                                  "2006-10-01T00:00:00.000-06:00", ZONE_GUATEMALA);
        doTest_getOffsetFromLocal( 2006,10, 1, 0,30,
                                  "2006-10-01T00:30:00.000-06:00", ZONE_GUATEMALA);
        doTest_getOffsetFromLocal( 2006,10, 1, 1, 0,
                                  "2006-10-01T01:00:00.000-06:00", ZONE_GUATEMALA);
        doTest_getOffsetFromLocal( 2006,10, 1, 1,30,
                                  "2006-10-01T01:30:00.000-06:00", ZONE_GUATEMALA);
        doTest_getOffsetFromLocal( 2006,10, 1, 2, 0,
                                  "2006-10-01T02:00:00.000-06:00", ZONE_GUATEMALA);
        doTest_getOffsetFromLocal( 2006,10, 1, 2,30,
                                  "2006-10-01T02:30:00.000-06:00", ZONE_GUATEMALA);
        doTest_getOffsetFromLocal( 2006,10, 1, 3, 0,
                                  "2006-10-01T03:00:00.000-06:00", ZONE_GUATEMALA);
        doTest_getOffsetFromLocal( 2006,10, 1, 3,30,
                                  "2006-10-01T03:30:00.000-06:00", ZONE_GUATEMALA);
        doTest_getOffsetFromLocal( 2006,10, 1, 4, 0,
                                  "2006-10-01T04:00:00.000-06:00", ZONE_GUATEMALA);
        doTest_getOffsetFromLocal( 2006,10, 1, 4,30,
                                  "2006-10-01T04:30:00.000-06:00", ZONE_GUATEMALA);
        doTest_getOffsetFromLocal( 2006,10, 1, 5, 0,
                                  "2006-10-01T05:00:00.000-06:00", ZONE_GUATEMALA);
        doTest_getOffsetFromLocal( 2006,10, 1, 5,30,
                                  "2006-10-01T05:30:00.000-06:00", ZONE_GUATEMALA);
        doTest_getOffsetFromLocal( 2006,10, 1, 6, 0,
                                  "2006-10-01T06:00:00.000-06:00", ZONE_GUATEMALA);
        doTest_getOffsetFromLocal( 2006,10, 1, 6,30,
                                  "2006-10-01T06:30:00.000-06:00", ZONE_GUATEMALA);
    }

    public void test_DateTime_plusHour_Guatemata_Autumn() {
        DateTime dt = new DateTime(2006, 9, 30, 20, 0, 0, 0, ZONE_GUATEMALA);
        assertEquals("2006-09-30T20:00:00.000-05:00", dt.toString());
        
        DateTime plus1 = dt.plusHours(1);
        assertEquals("2006-09-30T21:00:00.000-05:00", plus1.toString());
        DateTime plus2 = dt.plusHours(2);
        assertEquals("2006-09-30T22:00:00.000-05:00", plus2.toString());
        DateTime plus3 = dt.plusHours(3);
        assertEquals("2006-09-30T23:00:00.000-05:00", plus3.toString());
        DateTime plus4 = dt.plusHours(4);
        assertEquals("2006-09-30T23:00:00.000-06:00", plus4.toString());
        DateTime plus5 = dt.plusHours(5);
        assertEquals("2006-10-01T00:00:00.000-06:00", plus5.toString());
        DateTime plus6 = dt.plusHours(6);
        assertEquals("2006-10-01T01:00:00.000-06:00", plus6.toString());
        DateTime plus7 = dt.plusHours(7);
        assertEquals("2006-10-01T02:00:00.000-06:00", plus7.toString());
    }

    public void test_DateTime_minusHour_Guatemata_Autumn() {
        DateTime dt = new DateTime(2006, 10, 1, 2, 0, 0, 0, ZONE_GUATEMALA);
        assertEquals("2006-10-01T02:00:00.000-06:00", dt.toString());
        
        DateTime minus1 = dt.minusHours(1);
        assertEquals("2006-10-01T01:00:00.000-06:00", minus1.toString());
        DateTime minus2 = dt.minusHours(2);
        assertEquals("2006-10-01T00:00:00.000-06:00", minus2.toString());
        DateTime minus3 = dt.minusHours(3);
        assertEquals("2006-09-30T23:00:00.000-06:00", minus3.toString());
        DateTime minus4 = dt.minusHours(4);
        assertEquals("2006-09-30T23:00:00.000-05:00", minus4.toString());
        DateTime minus5 = dt.minusHours(5);
        assertEquals("2006-09-30T22:00:00.000-05:00", minus5.toString());
        DateTime minus6 = dt.minusHours(6);
        assertEquals("2006-09-30T21:00:00.000-05:00", minus6.toString());
        DateTime minus7 = dt.minusHours(7);
        assertEquals("2006-09-30T20:00:00.000-05:00", minus7.toString());
    }

    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------

//    public void test_toDateMidnight_SaoPaolo() {
//        // RFE: 1684259
//        DateTimeZone zone = DateTimeZone.forID("America/Sao_Paulo");
//        LocalDate baseDate = new LocalDate(2006, 11, 5);
//        DateMidnight dm = baseDate.toDateMidnight(zone);
//        assertEquals("2006-11-05T00:00:00.000-03:00", dm.toString());
//        DateTime dt = baseDate.toDateTimeAtMidnight(zone);
//        assertEquals("2006-11-05T00:00:00.000-03:00", dt.toString());
//    }

    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
    private void doTest_getOffsetFromLocal(int month, int day, int hour, int min,
                                           String expected, DateTimeZone zone)
    {
        doTest_getOffsetFromLocal(2007, month, day, hour, min, expected, zone);
    }

    private void doTest_getOffsetFromLocal(int year, int month, int day, int hour, int min,
                                           String expected, DateTimeZone zone)
    {
        DateTime dt = new DateTime(year, month, day, hour, min, 0, 0, DateTimeZone.UTC);
        int offset = zone.getOffsetFromLocal(dt.getMillis());
        DateTime res = new DateTime(dt.getMillis() - offset, zone);
        assertEquals(res.toString(), expected, res.toString());
    }

}
