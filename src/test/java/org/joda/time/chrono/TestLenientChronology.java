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
package org.joda.time.chrono;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MockZone;

/**
 *
 * @author Brian S O'Neill
 * @author Blair Martin
 */
public class TestLenientChronology extends TestCase {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestLenientChronology.class);
    }

    public TestLenientChronology(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
    }

    @Override
    protected void tearDown() throws Exception {
    }

    //-----------------------------------------------------------------------
    public void test_setYear() {
        Chronology zone = LenientChronology.getInstance(ISOChronology.getInstanceUTC());
        DateTime dt = new DateTime(2007, 1, 1, 0, 0 ,0, 0, zone);
        assertEquals("2007-01-01T00:00:00.000Z", dt.toString());
        dt = dt.withYear(2008);
        assertEquals("2008-01-01T00:00:00.000Z", dt.toString());
    }

    //-----------------------------------------------------------------------
    public void test_setMonthOfYear() {
        Chronology zone = LenientChronology.getInstance(ISOChronology.getInstanceUTC());
        DateTime dt = new DateTime(2007, 1, 1, 0, 0 ,0, 0, zone);
        assertEquals("2007-01-01T00:00:00.000Z", dt.toString());
        dt = dt.withMonthOfYear(13);
        assertEquals("2008-01-01T00:00:00.000Z", dt.toString());
        dt = dt.withMonthOfYear(0);
        assertEquals("2007-12-01T00:00:00.000Z", dt.toString());
    }

    //-----------------------------------------------------------------------
    public void test_setDayOfMonth() {
        Chronology zone = LenientChronology.getInstance(ISOChronology.getInstanceUTC());
        DateTime dt = new DateTime(2007, 1, 1, 0, 0 ,0, 0, zone);
        assertEquals("2007-01-01T00:00:00.000Z", dt.toString());
        dt = dt.withDayOfMonth(32);
        assertEquals("2007-02-01T00:00:00.000Z", dt.toString());
        dt = dt.withDayOfMonth(0);
        assertEquals("2007-01-31T00:00:00.000Z", dt.toString());
    }

    //-----------------------------------------------------------------------
    public void test_setHourOfDay() {
        Chronology zone = LenientChronology.getInstance(ISOChronology.getInstanceUTC());
        DateTime dt = new DateTime(2007, 1, 1, 0, 0 ,0, 0, zone);
        assertEquals("2007-01-01T00:00:00.000Z", dt.toString());
        dt = dt.withHourOfDay(24);
        assertEquals("2007-01-02T00:00:00.000Z", dt.toString());
        dt = dt.withHourOfDay(-1);
        assertEquals("2007-01-01T23:00:00.000Z", dt.toString());
    }

    //-----------------------------------------------------------------------
    //------------------------ Bug ------------------------------------------
    //-----------------------------------------------------------------------
    public void testNearDstTransition() {
        // This is just a regression test. Test case provided by Blair Martin.

        int hour = 23;
        DateTime dt;

        dt = new DateTime(2006, 10, 29, hour, 0, 0, 0,
                          ISOChronology.getInstance(DateTimeZone.forID("America/Los_Angeles")));
        assertEquals(hour, dt.getHourOfDay()); // OK - no LenientChronology

        dt = new DateTime(2006, 10, 29, hour, 0, 0, 0,
                          LenientChronology.getInstance
                          (ISOChronology.getInstance(DateTimeZone.forOffsetHours(-8))));
        assertEquals(hour, dt.getHourOfDay()); // OK - no TZ ID

        dt = new DateTime(2006, 10, 29, hour, 0, 0, 0,
                          LenientChronology.getInstance
                          (ISOChronology.getInstance(DateTimeZone.forID("America/Los_Angeles"))));

        assertEquals(hour, dt.getHourOfDay()); // Used to fail - hour was 22
    }

    //-----------------------------------------------------------------------
    //------------------------ Bug [1755161] --------------------------------
    //-----------------------------------------------------------------------
    /** Mock zone simulating America/Grand_Turk cutover at midnight 2007-04-01 */
    private static long CUTOVER_TURK = 1175403600000L;
    private static int OFFSET_TURK = -18000000;  // -05:00
    private static final DateTimeZone MOCK_TURK = new MockZone(CUTOVER_TURK, OFFSET_TURK, 3600);

    //-----------------------------------------------------------------------
    public void test_MockTurkIsCorrect() {
        DateTime pre = new DateTime(CUTOVER_TURK - 1L, MOCK_TURK);
        assertEquals("2007-03-31T23:59:59.999-05:00", pre.toString());
        DateTime at = new DateTime(CUTOVER_TURK, MOCK_TURK);
        assertEquals("2007-04-01T01:00:00.000-04:00", at.toString());
        DateTime post = new DateTime(CUTOVER_TURK + 1L, MOCK_TURK);
        assertEquals("2007-04-01T01:00:00.001-04:00", post.toString());
    }

    public void test_lenientChrononolgy_Chicago() {
        DateTimeZone zone = DateTimeZone.forID("America/Chicago");
        Chronology lenient = LenientChronology.getInstance(ISOChronology.getInstance(zone));
        DateTime dt = new DateTime(2007, 3, 11, 2, 30, 0, 0, lenient);
        assertEquals("2007-03-11T03:30:00.000-05:00", dt.toString());
    }

    public void test_lenientChrononolgy_Turk() {
        Chronology lenient = LenientChronology.getInstance(ISOChronology.getInstance(MOCK_TURK));
        DateTime dt = new DateTime(2007, 4, 1, 0, 30, 0, 0, lenient);
        assertEquals("2007-04-01T01:30:00.000-04:00", dt.toString());
    }

    public void test_strictChrononolgy_Chicago() {
        DateTimeZone zone = DateTimeZone.forID("America/Chicago");
        Chronology lenient = StrictChronology.getInstance(ISOChronology.getInstance(zone));
        try {
            new DateTime(2007, 3, 11, 2, 30, 0, 0, lenient);
            fail();
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    public void test_isoChrononolgy_Chicago() {
        DateTimeZone zone = DateTimeZone.forID("America/Chicago");
        Chronology lenient = ISOChronology.getInstance(zone);
        try {
            new DateTime(2007, 3, 11, 2, 30, 0, 0, lenient);
            fail();
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

}
