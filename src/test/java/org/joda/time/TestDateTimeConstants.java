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
package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test case.
 *
 * @author Stephen Colebourne
 */
public class TestDateTimeConstants extends TestCase {

    /**
     * The main method for this test program.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * TestSuite is a junit required method.
     */
    public static TestSuite suite() {
        return new TestSuite(TestDateTimeConstants.class);
    }

    /**
     * TestDateTimeComparator constructor.
     * @param name
     */
    public TestDateTimeConstants(String name) {
        super(name);
    }

    //-----------------------------------------------------------------------
    public void testConstructor() {
        DateTimeConstants c = new DateTimeConstants() {
        };
        c.toString();
    }

    public void testHalfdaysOfDay() {
        assertEquals(0, DateTimeConstants.AM);
        assertEquals(1, DateTimeConstants.PM);
    }

    public void testDaysOfWeek() {
        assertEquals(1, DateTimeConstants.MONDAY);
        assertEquals(2, DateTimeConstants.TUESDAY);
        assertEquals(3, DateTimeConstants.WEDNESDAY);
        assertEquals(4, DateTimeConstants.THURSDAY);
        assertEquals(5, DateTimeConstants.FRIDAY);
        assertEquals(6, DateTimeConstants.SATURDAY);
        assertEquals(7, DateTimeConstants.SUNDAY);
    }

    public void testMonthsOfYear() {
        assertEquals(1, DateTimeConstants.JANUARY);
        assertEquals(2, DateTimeConstants.FEBRUARY);
        assertEquals(3, DateTimeConstants.MARCH);
        assertEquals(4, DateTimeConstants.APRIL);
        assertEquals(5, DateTimeConstants.MAY);
        assertEquals(6, DateTimeConstants.JUNE);
        assertEquals(7, DateTimeConstants.JULY);
        assertEquals(8, DateTimeConstants.AUGUST);
        assertEquals(9, DateTimeConstants.SEPTEMBER);
        assertEquals(10, DateTimeConstants.OCTOBER);
        assertEquals(11, DateTimeConstants.NOVEMBER);
        assertEquals(12, DateTimeConstants.DECEMBER);
    }

    public void testEras() {
        assertEquals(0, DateTimeConstants.BC);
        assertEquals(0, DateTimeConstants.BCE);
        assertEquals(1, DateTimeConstants.AD);
        assertEquals(1, DateTimeConstants.CE);
    }

    public void testMaths() {
        assertEquals(1000, DateTimeConstants.MILLIS_PER_SECOND);
        assertEquals(60 * 1000, DateTimeConstants.MILLIS_PER_MINUTE);
        assertEquals(60 * 60 * 1000, DateTimeConstants.MILLIS_PER_HOUR);
        assertEquals(24 * 60 * 60 * 1000, DateTimeConstants.MILLIS_PER_DAY);
        assertEquals(7 * 24 * 60 * 60 * 1000, DateTimeConstants.MILLIS_PER_WEEK);
        
        assertEquals(60, DateTimeConstants.SECONDS_PER_MINUTE);
        assertEquals(60 * 60, DateTimeConstants.SECONDS_PER_HOUR);
        assertEquals(24 * 60 * 60, DateTimeConstants.SECONDS_PER_DAY);
        assertEquals(7 * 24 * 60 * 60, DateTimeConstants.SECONDS_PER_WEEK);
        
        assertEquals(60, DateTimeConstants.MINUTES_PER_HOUR);
        assertEquals(24 * 60, DateTimeConstants.MINUTES_PER_DAY);
        assertEquals(7 * 24 * 60, DateTimeConstants.MINUTES_PER_WEEK);
        
        assertEquals(24, DateTimeConstants.HOURS_PER_DAY);
        assertEquals(7 * 24, DateTimeConstants.HOURS_PER_WEEK);
        
        assertEquals(7, DateTimeConstants.DAYS_PER_WEEK);
    }

}
