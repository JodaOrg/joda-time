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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.chrono.ISOChronology;
/**
 * This class is a Junit unit test for the
 * org.joda.time.DateTimeComparator class.
 *
 * @author Guy Allard
 */
public class TestDateTimeComparator extends TestCase {

    private static final Chronology ISO = ISOChronology.getInstance();
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestDateTimeComparator.class);
    }

    public TestDateTimeComparator(String name) {
        super(name);
    }

    /**
     * A reference to a DateTime object.
     */
    DateTime aDateTime = null;
    /**
     * A reference to a DateTime object.
     */
    DateTime bDateTime = null;
    /**
     * A reference to a DateTimeComparator object
     * (a Comparator) for millis of seconds.
     */
    Comparator cMillis = null;
    /**
     * A reference to a DateTimeComparator object
     * (a Comparator) for seconds.
     */
    Comparator cSecond = null;
    /**
     * A reference to a DateTimeComparator object
     * (a Comparator) for minutes.
     */
    Comparator cMinute = null;
    /**
     * A reference to a DateTimeComparator object
     * (a Comparator) for hours.
     */
    Comparator cHour = null;
    /**
     * A reference to a DateTimeComparator object
     * (a Comparator) for day of the week.
     */
    Comparator cDayOfWeek = null;
    /**
     * A reference to a DateTimeComparator object
     * (a Comparator) for day of the month.
     */
    Comparator cDayOfMonth = null;
    /**
     * A reference to a DateTimeComparator object
     * (a Comparator) for day of the year.
     */
    Comparator cDayOfYear = null;
    /**
     * A reference to a DateTimeComparator object
     * (a Comparator) for week of the weekyear.
     */
    Comparator cWeekOfWeekyear = null;
    /**
     * A reference to a DateTimeComparator object
     * (a Comparator) for year given a week of the year.
     */
    Comparator cWeekyear = null;
    /**
     * A reference to a DateTimeComparator object
     * (a Comparator) for months.
     */
    Comparator cMonth = null;
    /**
     * A reference to a DateTimeComparator object
     * (a Comparator) for year.
     */
    Comparator cYear = null;
    /**
     * A reference to a DateTimeComparator object
     * (a Comparator) for the date portion of an
     * object.
     */
    Comparator cDate = null;
    /**
     * A reference to a DateTimeComparator object
     * (a Comparator) for the time portion of an
     * object.
     */
    Comparator cTime = null;
    /**
     * Junit <code>setUp()</code> method.
     */
    @Override
    public void setUp() /* throws Exception */ {
        Chronology chrono = ISOChronology.getInstanceUTC();

        // super.setUp();
        // Obtain comparator's
        cMillis = DateTimeComparator.getInstance(null, DateTimeFieldType.secondOfMinute());
        cSecond = DateTimeComparator.getInstance(DateTimeFieldType.secondOfMinute(), DateTimeFieldType.minuteOfHour());
        cMinute = DateTimeComparator.getInstance(DateTimeFieldType.minuteOfHour(), DateTimeFieldType.hourOfDay());
        cHour = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay(), DateTimeFieldType.dayOfYear());
        cDayOfWeek = DateTimeComparator.getInstance(DateTimeFieldType.dayOfWeek(), DateTimeFieldType.weekOfWeekyear());
        cDayOfMonth = DateTimeComparator.getInstance(DateTimeFieldType.dayOfMonth(), DateTimeFieldType.monthOfYear());
        cDayOfYear = DateTimeComparator.getInstance(DateTimeFieldType.dayOfYear(), DateTimeFieldType.year());
        cWeekOfWeekyear = DateTimeComparator.getInstance(DateTimeFieldType.weekOfWeekyear(), DateTimeFieldType.weekyear());
        cWeekyear = DateTimeComparator.getInstance(DateTimeFieldType.weekyear());
        cMonth = DateTimeComparator.getInstance(DateTimeFieldType.monthOfYear(), DateTimeFieldType.year());
        cYear = DateTimeComparator.getInstance(DateTimeFieldType.year());
        cDate = DateTimeComparator.getDateOnlyInstance();
        cTime = DateTimeComparator.getTimeOnlyInstance();
    }

    /**
     * Junit <code>tearDown()</code> method.
     */
    @Override
    protected void tearDown() /* throws Exception */ {
        // super.tearDown();
        aDateTime = null;
        bDateTime = null;
        //
        cMillis = null;
        cSecond = null;
        cMinute = null;
        cHour = null;
        cDayOfWeek = null;
        cDayOfMonth = null;
        cDayOfYear = null;
        cWeekOfWeekyear = null;
        cWeekyear = null;
        cMonth = null;
        cYear = null;
        cDate = null;
        cTime = null;
    }

    //-----------------------------------------------------------------------
    public void testClass() {
        assertEquals(true, Modifier.isPublic(DateTimeComparator.class.getModifiers()));
        assertEquals(false, Modifier.isFinal(DateTimeComparator.class.getModifiers()));
        assertEquals(1, DateTimeComparator.class.getDeclaredConstructors().length);
        assertEquals(true, Modifier.isProtected(DateTimeComparator.class.getDeclaredConstructors()[0].getModifiers()));
    }
    
    //-----------------------------------------------------------------------
    public void testStaticGetInstance() {
        DateTimeComparator c = DateTimeComparator.getInstance();
        assertEquals(null, c.getLowerLimit());
        assertEquals(null, c.getUpperLimit());
        assertEquals("DateTimeComparator[]", c.toString());
    }        
    public void testStaticGetDateOnlyInstance() {
        DateTimeComparator c = DateTimeComparator.getDateOnlyInstance();
        assertEquals(DateTimeFieldType.dayOfYear(), c.getLowerLimit());
        assertEquals(null, c.getUpperLimit());
        assertEquals("DateTimeComparator[dayOfYear-]", c.toString());
        
        assertSame(DateTimeComparator.getDateOnlyInstance(), DateTimeComparator.getDateOnlyInstance());
    }
    public void testStaticGetTimeOnlyInstance() {
        DateTimeComparator c = DateTimeComparator.getTimeOnlyInstance();
        assertEquals(null, c.getLowerLimit());
        assertEquals(DateTimeFieldType.dayOfYear(), c.getUpperLimit());
        assertEquals("DateTimeComparator[-dayOfYear]", c.toString());
        
        assertSame(DateTimeComparator.getTimeOnlyInstance(), DateTimeComparator.getTimeOnlyInstance());
    }
    public void testStaticGetInstanceLower() {
        DateTimeComparator c = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay());
        assertEquals(DateTimeFieldType.hourOfDay(), c.getLowerLimit());
        assertEquals(null, c.getUpperLimit());
        assertEquals("DateTimeComparator[hourOfDay-]", c.toString());
        
        c = DateTimeComparator.getInstance(null);
        assertSame(DateTimeComparator.getInstance(), c);
    }

    public void testStaticGetInstanceLowerUpper() {
        DateTimeComparator c = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay(), DateTimeFieldType.dayOfYear());
        assertEquals(DateTimeFieldType.hourOfDay(), c.getLowerLimit());
        assertEquals(DateTimeFieldType.dayOfYear(), c.getUpperLimit());
        assertEquals("DateTimeComparator[hourOfDay-dayOfYear]", c.toString());
        
        c = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay(), DateTimeFieldType.hourOfDay());
        assertEquals(DateTimeFieldType.hourOfDay(), c.getLowerLimit());
        assertEquals(DateTimeFieldType.hourOfDay(), c.getUpperLimit());
        assertEquals("DateTimeComparator[hourOfDay]", c.toString());
        
        c = DateTimeComparator.getInstance(null, null);
        assertSame(DateTimeComparator.getInstance(), c);
        
        c = DateTimeComparator.getInstance(DateTimeFieldType.dayOfYear(), null);
        assertSame(DateTimeComparator.getDateOnlyInstance(), c);
        
        c = DateTimeComparator.getInstance(null, DateTimeFieldType.dayOfYear());
        assertSame(DateTimeComparator.getTimeOnlyInstance(), c);
    }
    
    public void testNullNowCheckedOnce() {
        // checks a race condition against the system clock, issue #404
        for (int i = 0; i < 10000; i++) {
            if (DateTimeComparator.getInstance().compare(null, null) != 0) {
                fail("Comparing (null, null) should always return 0");
            }
        }
    }
    
    //-----------------------------------------------------------------------
    public void testEqualsHashCode() {
        DateTimeComparator c1 = DateTimeComparator.getInstance();
        assertEquals(true, c1.equals(c1));
        assertEquals(false, c1.equals(null));
        assertEquals(true, c1.hashCode() == c1.hashCode());
        
        DateTimeComparator c2 = DateTimeComparator.getTimeOnlyInstance();
        assertEquals(true, c2.equals(c2));
        assertEquals(false, c2.equals(c1));
        assertEquals(false, c1.equals(c2));
        assertEquals(false, c2.equals(null));
        assertEquals(false, c1.hashCode() == c2.hashCode());
        
        DateTimeComparator c3 = DateTimeComparator.getTimeOnlyInstance();
        assertEquals(true, c3.equals(c3));
        assertEquals(false, c3.equals(c1));
        assertEquals(true, c3.equals(c2));
        assertEquals(false, c1.equals(c3));
        assertEquals(true, c2.equals(c3));
        assertEquals(false, c1.hashCode() == c3.hashCode());
        assertEquals(true, c2.hashCode() == c3.hashCode());
        
        DateTimeComparator c4 = DateTimeComparator.getDateOnlyInstance();
        assertEquals(false, c4.hashCode() == c3.hashCode());
    }
    
    //-----------------------------------------------------------------------
    public void testSerialization1() throws Exception {
        DateTimeField f = ISO.dayOfYear();
        f.toString();
        DateTimeComparator c = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay(), DateTimeFieldType.dayOfYear());
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(c);
        oos.close();
        byte[] bytes = baos.toByteArray();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        DateTimeComparator result = (DateTimeComparator) ois.readObject();
        ois.close();
        
        assertEquals(c, result);
    }

    //-----------------------------------------------------------------------
    public void testSerialization2() throws Exception {
        DateTimeComparator c = DateTimeComparator.getInstance();
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(c);
        oos.close();
        byte[] bytes = baos.toByteArray();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        DateTimeComparator result = (DateTimeComparator) ois.readObject();
        ois.close();
        
        assertSame(c, result);
    }

    //-----------------------------------------------------------------------
    /**
     * Test all basic comparator operation with DateTime objects.
     */
    public void testBasicComps1() {
        aDateTime = new DateTime( System.currentTimeMillis(), DateTimeZone.UTC );
        bDateTime = new DateTime( aDateTime.getMillis(), DateTimeZone.UTC );
        assertEquals( "getMillis", aDateTime.getMillis(),
            bDateTime.getMillis() );
        assertEquals( "MILLIS", 0, cMillis.compare( aDateTime, bDateTime ) );
        assertEquals( "SECOND", 0, cSecond.compare( aDateTime, bDateTime ) );
        assertEquals( "MINUTE", 0, cMinute.compare( aDateTime, bDateTime ) );
        assertEquals( "HOUR", 0, cHour.compare( aDateTime, bDateTime ) );
        assertEquals( "DOW", 0, cDayOfWeek.compare( aDateTime, bDateTime ) );
        assertEquals( "DOM", 0, cDayOfMonth.compare( aDateTime, bDateTime ) );
        assertEquals( "DOY", 0, cDayOfYear.compare( aDateTime, bDateTime ) );
        assertEquals( "WOW", 0, cWeekOfWeekyear.compare( aDateTime, bDateTime ) );
        assertEquals( "WY", 0, cWeekyear.compare( aDateTime, bDateTime ) );
        assertEquals( "MONTH", 0, cMonth.compare( aDateTime, bDateTime ) );
        assertEquals( "YEAR", 0, cYear.compare( aDateTime, bDateTime ) );
        assertEquals( "DATE", 0, cDate.compare( aDateTime, bDateTime ) );
        assertEquals( "TIME", 0, cTime.compare( aDateTime, bDateTime ) );
    }   // end of testBasicComps


    /**
     * Test all basic comparator operation with ReadableInstant objects.
     */
    public void testBasicComps2() {
        ReadableInstant aDateTime = new DateTime( System.currentTimeMillis(), DateTimeZone.UTC );
        ReadableInstant bDateTime = new DateTime( aDateTime.getMillis(), DateTimeZone.UTC );
        assertEquals( "getMillis", aDateTime.getMillis(),
            bDateTime.getMillis() );
        assertEquals( "MILLIS", 0, cMillis.compare( aDateTime, bDateTime ) );
        assertEquals( "SECOND", 0, cSecond.compare( aDateTime, bDateTime ) );
        assertEquals( "MINUTE", 0, cMinute.compare( aDateTime, bDateTime ) );
        assertEquals( "HOUR", 0, cHour.compare( aDateTime, bDateTime ) );
        assertEquals( "DOW", 0, cDayOfWeek.compare( aDateTime, bDateTime ) );
        assertEquals( "DOM", 0, cDayOfMonth.compare( aDateTime, bDateTime ) );
        assertEquals( "DOY", 0, cDayOfYear.compare( aDateTime, bDateTime ) );
        assertEquals( "WOW", 0, cWeekOfWeekyear.compare( aDateTime, bDateTime ) );
        assertEquals( "WY", 0, cWeekyear.compare( aDateTime, bDateTime ) );
        assertEquals( "MONTH", 0, cMonth.compare( aDateTime, bDateTime ) );
        assertEquals( "YEAR", 0, cYear.compare( aDateTime, bDateTime ) );
        assertEquals( "DATE", 0, cDate.compare( aDateTime, bDateTime ) );
        assertEquals( "TIME", 0, cTime.compare( aDateTime, bDateTime ) );
    }   // end of testBasicComps

    /**
     * Test all basic comparator operation with java Date objects.
     */
    public void testBasicComps3() {
        Date aDateTime
            = new Date( System.currentTimeMillis() );
        Date bDateTime
            = new Date( aDateTime.getTime() );
        assertEquals( "MILLIS", 0, cMillis.compare( aDateTime, bDateTime ) );
        assertEquals( "SECOND", 0, cSecond.compare( aDateTime, bDateTime ) );
        assertEquals( "MINUTE", 0, cMinute.compare( aDateTime, bDateTime ) );
        assertEquals( "HOUR", 0, cHour.compare( aDateTime, bDateTime ) );
        assertEquals( "DOW", 0, cDayOfWeek.compare( aDateTime, bDateTime ) );
        assertEquals( "DOM", 0, cDayOfMonth.compare( aDateTime, bDateTime ) );
        assertEquals( "DOY", 0, cDayOfYear.compare( aDateTime, bDateTime ) );
        assertEquals( "WOW", 0, cWeekOfWeekyear.compare( aDateTime, bDateTime ) );
        assertEquals( "WY", 0, cWeekyear.compare( aDateTime, bDateTime ) );
        assertEquals( "MONTH", 0, cMonth.compare( aDateTime, bDateTime ) );
        assertEquals( "YEAR", 0, cYear.compare( aDateTime, bDateTime ) );
        assertEquals( "DATE", 0, cDate.compare( aDateTime, bDateTime ) );
        assertEquals( "TIME", 0, cTime.compare( aDateTime, bDateTime ) );
    }   // end of testBasicComps

    /**
     * Test all basic comparator operation with Long objects.
     */
    public void testBasicComps4() {
        Long aDateTime
            = new Long( System.currentTimeMillis() );
        Long bDateTime
            = new Long( aDateTime.longValue() );
        assertEquals( "MILLIS", 0, cMillis.compare( aDateTime, bDateTime ) );
        assertEquals( "SECOND", 0, cSecond.compare( aDateTime, bDateTime ) );
        assertEquals( "MINUTE", 0, cMinute.compare( aDateTime, bDateTime ) );
        assertEquals( "HOUR", 0, cHour.compare( aDateTime, bDateTime ) );
        assertEquals( "DOW", 0, cDayOfWeek.compare( aDateTime, bDateTime ) );
        assertEquals( "DOM", 0, cDayOfMonth.compare( aDateTime, bDateTime ) );
        assertEquals( "DOY", 0, cDayOfYear.compare( aDateTime, bDateTime ) );
        assertEquals( "WOW", 0, cWeekOfWeekyear.compare( aDateTime, bDateTime ) );
        assertEquals( "WY", 0, cWeekyear.compare( aDateTime, bDateTime ) );
        assertEquals( "MONTH", 0, cMonth.compare( aDateTime, bDateTime ) );
        assertEquals( "YEAR", 0, cYear.compare( aDateTime, bDateTime ) );
        assertEquals( "DATE", 0, cDate.compare( aDateTime, bDateTime ) );
        assertEquals( "TIME", 0, cTime.compare( aDateTime, bDateTime ) );
    }   // end of testBasicComps

    /**
     * Test all basic comparator operation with Calendar objects.
     */
    public void testBasicComps5() {
        Calendar aDateTime
            = Calendar.getInstance();   // right now
        Calendar bDateTime = aDateTime;
        assertEquals( "MILLIS", 0, cMillis.compare( aDateTime, bDateTime ) );
        assertEquals( "SECOND", 0, cSecond.compare( aDateTime, bDateTime ) );
        assertEquals( "MINUTE", 0, cMinute.compare( aDateTime, bDateTime ) );
        assertEquals( "HOUR", 0, cHour.compare( aDateTime, bDateTime ) );
        assertEquals( "DOW", 0, cDayOfWeek.compare( aDateTime, bDateTime ) );
        assertEquals( "DOM", 0, cDayOfMonth.compare( aDateTime, bDateTime ) );
        assertEquals( "DOY", 0, cDayOfYear.compare( aDateTime, bDateTime ) );
        assertEquals( "WOW", 0, cWeekOfWeekyear.compare( aDateTime, bDateTime ) );
        assertEquals( "WY", 0, cWeekyear.compare( aDateTime, bDateTime ) );
        assertEquals( "MONTH", 0, cMonth.compare( aDateTime, bDateTime ) );
        assertEquals( "YEAR", 0, cYear.compare( aDateTime, bDateTime ) );
        assertEquals( "DATE", 0, cDate.compare( aDateTime, bDateTime ) );
        assertEquals( "TIME", 0, cTime.compare( aDateTime, bDateTime ) );
    }   // end of testBasicComps


    /**
     * Test unequal comparisons with millis of second comparators.
     */
    public void testMillis() {
        aDateTime = new DateTime( System.currentTimeMillis(), DateTimeZone.UTC );
        bDateTime = new DateTime( aDateTime.getMillis() + 1, DateTimeZone.UTC );
        assertEquals( "MillisM1", -1, cMillis.compare( aDateTime, bDateTime ) );
        assertEquals( "MillisP1", 1, cMillis.compare( bDateTime, aDateTime ) );
    }   // end of testMillis

    /**
     * Test unequal comparisons with second comparators.
     */
    public void testSecond() {
        aDateTime = getADate( "1969-12-31T23:59:58" );
        bDateTime = getADate( "1969-12-31T23:50:59" );
        assertEquals( "SecondM1a", -1, cSecond.compare( aDateTime, bDateTime ) );
        assertEquals( "SecondP1a", 1, cSecond.compare( bDateTime, aDateTime ) );
        aDateTime = getADate( "1970-01-01T00:00:00" );
        bDateTime = getADate( "1970-01-01T00:00:01" );
        assertEquals( "SecondM1b", -1, cSecond.compare( aDateTime, bDateTime ) );
        assertEquals( "SecondP1b", 1, cSecond.compare( bDateTime, aDateTime ) );
    }   // end of testSecond

    /**
     * Test unequal comparisons with minute comparators.
     */
    public void testMinute() {
        aDateTime = getADate( "1969-12-31T23:58:00" );
        bDateTime = getADate( "1969-12-31T23:59:00" );
        assertEquals( "MinuteM1a", -1, cMinute.compare( aDateTime, bDateTime ) );
        assertEquals( "MinuteP1a", 1, cMinute.compare( bDateTime, aDateTime ) );
        aDateTime = getADate( "1970-01-01T00:00:00" );
        bDateTime = getADate( "1970-01-01T00:01:00" );
        assertEquals( "MinuteM1b", -1, cMinute.compare( aDateTime, bDateTime ) );
        assertEquals( "MinuteP1b", 1, cMinute.compare( bDateTime, aDateTime ) );
    }   // end of testMinute

    /**
     * Test unequal comparisons with hour comparators.
     */
    public void testHour() {
        aDateTime = getADate( "1969-12-31T22:00:00" );
        bDateTime = getADate( "1969-12-31T23:00:00" );
        assertEquals( "HourM1a", -1, cHour.compare( aDateTime, bDateTime ) );
        assertEquals( "HourP1a", 1, cHour.compare( bDateTime, aDateTime ) );
        aDateTime = getADate( "1970-01-01T00:00:00" );
        bDateTime = getADate( "1970-01-01T01:00:00" );
        assertEquals( "HourM1b", -1, cHour.compare( aDateTime, bDateTime ) );
        assertEquals( "HourP1b", 1, cHour.compare( bDateTime, aDateTime ) );
        aDateTime = getADate( "1969-12-31T23:59:59" );
        bDateTime = getADate( "1970-01-01T00:00:00" );
        assertEquals( "HourP1c", 1, cHour.compare( aDateTime, bDateTime ) );
        assertEquals( "HourM1c", -1, cHour.compare( bDateTime, aDateTime ) );
    }   // end of testHour

    /**
     * Test unequal comparisons with day of week comparators.
     */
    public void testDOW() {
        /*
         * Dates chosen when I wrote the code, so I know what day of
         * the week it is.
         */
        aDateTime = getADate( "2002-04-12T00:00:00" );
        bDateTime = getADate( "2002-04-13T00:00:00" );
        assertEquals( "DOWM1a", -1, cDayOfWeek.compare( aDateTime, bDateTime ) );
        assertEquals( "DOWP1a", 1, cDayOfWeek.compare( bDateTime, aDateTime ) );
    }   // end of testDOW

    /**
     * Test unequal comparisons with day of month comparators.
     */
    public void testDOM() {
        aDateTime = getADate( "2002-04-12T00:00:00" );
        bDateTime = getADate( "2002-04-13T00:00:00" );
        assertEquals( "DOMM1a", -1, cDayOfMonth.compare( aDateTime, bDateTime ) );
        assertEquals( "DOMP1a", 1, cDayOfMonth.compare( bDateTime, aDateTime ) );
        aDateTime = getADate( "2000-12-01T00:00:00" );
        bDateTime = getADate( "1814-04-30T00:00:00" );
        assertEquals( "DOMM1b", -1, cDayOfMonth.compare( aDateTime, bDateTime ) );
        assertEquals( "DOMP1b", 1, cDayOfMonth.compare( bDateTime, aDateTime ) );
    }   // end of testDOM

    /**
     * Test unequal comparisons with day of year comparators.
     */
    public void testDOY() {
        aDateTime = getADate( "2002-04-12T00:00:00" );
        bDateTime = getADate( "2002-04-13T00:00:00" );
        assertEquals( "DOYM1a", -1, cDayOfYear.compare( aDateTime, bDateTime ) );
        assertEquals( "DOYP1a", 1, cDayOfYear.compare( bDateTime, aDateTime ) );
        aDateTime = getADate( "2000-02-29T00:00:00" );
        bDateTime = getADate( "1814-11-30T00:00:00" );
        assertEquals( "DOYM1b", -1, cDayOfYear.compare( aDateTime, bDateTime ) );
        assertEquals( "DOYP1b", 1, cDayOfYear.compare( bDateTime, aDateTime ) );
    }   // end of testDOY

    /**
     * Test unequal comparisons with week of weekyear comparators.
     */
    public void testWOW() {
        // 1st week of year contains Jan 04.
        aDateTime = getADate( "2000-01-04T00:00:00" );
        bDateTime = getADate( "2000-01-11T00:00:00" );
        assertEquals( "WOWM1a", -1,
            cWeekOfWeekyear.compare( aDateTime, bDateTime ) );
        assertEquals( "WOWP1a", 1,
            cWeekOfWeekyear.compare( bDateTime, aDateTime ) );
        aDateTime = getADate( "2000-01-04T00:00:00" );
        bDateTime = getADate( "1999-12-31T00:00:00" );
        assertEquals( "WOWM1b", -1,
            cWeekOfWeekyear.compare( aDateTime, bDateTime ) );
        assertEquals( "WOWP1b", 1,
            cWeekOfWeekyear.compare( bDateTime, aDateTime ) );
    }   // end of testMillis

    /**
     * Test unequal comparisons with year given the week comparators.
     */
    public void testWOYY() {
        // How do I test the end conditions of this?
        // Don't understand ......
        aDateTime = getADate( "1998-12-31T23:59:59" );
        bDateTime = getADate( "1999-01-01T00:00:00" );
        assertEquals( "YOYYZ", 0, cWeekyear.compare( aDateTime, bDateTime ) );
        bDateTime = getADate( "1999-01-04T00:00:00" );
        assertEquals( "YOYYM1", -1, cWeekyear.compare( aDateTime, bDateTime ) );
        assertEquals( "YOYYP1", 1, cWeekyear.compare( bDateTime, aDateTime ) );
    }   // end of testWOYY

    /**
     * Test unequal comparisons with month comparators.
     */
    public void testMonth() {
        aDateTime = getADate( "2002-04-30T00:00:00" );
        bDateTime = getADate( "2002-05-01T00:00:00" );
        assertEquals( "MONTHM1a", -1, cMonth.compare( aDateTime, bDateTime ) );
        assertEquals( "MONTHP1a", 1, cMonth.compare( bDateTime, aDateTime ) );
        aDateTime = getADate( "1900-01-01T00:00:00" );
        bDateTime = getADate( "1899-12-31T00:00:00" );
        assertEquals( "MONTHM1b", -1, cMonth.compare( aDateTime, bDateTime ) );
        assertEquals( "MONTHP1b", 1, cMonth.compare( bDateTime, aDateTime ) );
    }   // end of testMonth

    /**
     * Test unequal comparisons with year comparators.
     */
    public void testYear() {
        aDateTime = getADate( "2000-01-01T00:00:00" );
        bDateTime = getADate( "2001-01-01T00:00:00" );
        assertEquals( "YEARM1a", -1, cYear.compare( aDateTime, bDateTime ) );
        assertEquals( "YEARP1a", 1, cYear.compare( bDateTime, aDateTime ) );
        aDateTime = getADate( "1968-12-31T23:59:59" );
        bDateTime = getADate( "1970-01-01T00:00:00" );
        assertEquals( "YEARM1b", -1, cYear.compare( aDateTime, bDateTime ) );
        assertEquals( "YEARP1b", 1, cYear.compare( bDateTime, aDateTime ) );
        aDateTime = getADate( "1969-12-31T23:59:59" );
        bDateTime = getADate( "1970-01-01T00:00:00" );
        assertEquals( "YEARM1c", -1, cYear.compare( aDateTime, bDateTime ) );
        assertEquals( "YEARP1c", 1, cYear.compare( bDateTime, aDateTime ) );
    }   // end of testYear

    /*
     * 'List' processing tests follow.
     */

     /**
      * Test sorting with full default comparator.
      */
     public void testListBasic() {
        String[] dtStrs = {
            "1999-02-01T00:00:00",
            "1998-01-20T00:00:00"
        };
        //
        List sl = loadAList( dtStrs );
        boolean isSorted1 = isListSorted( sl );
        Collections.sort( sl );
        boolean isSorted2 = isListSorted( sl );
        assertEquals("ListBasic", !isSorted1, isSorted2);
     } // end of testListBasic

     /**
      * Test sorting with millis of second comparator.
      */
    public void testListMillis() {
        //
        List sl = new ArrayList();
        long base = 12345L * 1000L;
        sl.add( new DateTime( base + 999L, DateTimeZone.UTC ) );
        sl.add( new DateTime( base + 222L, DateTimeZone.UTC ) );
        sl.add( new DateTime( base + 456L, DateTimeZone.UTC ) );
        sl.add( new DateTime( base + 888L, DateTimeZone.UTC ) );
        sl.add( new DateTime( base + 123L, DateTimeZone.UTC ) );
        sl.add( new DateTime( base + 000L, DateTimeZone.UTC ) );
        //
        boolean isSorted1 = isListSorted( sl );
        Collections.sort( sl, cMillis );
        boolean isSorted2 = isListSorted( sl );
        assertEquals("ListLillis", !isSorted1, isSorted2);
    } // end of testListSecond


     /**
      * Test sorting with second comparator.
      */
    public void testListSecond() {
        String[] dtStrs = {
            "1999-02-01T00:00:10",
            "1999-02-01T00:00:30",
            "1999-02-01T00:00:25",
            "1999-02-01T00:00:18",
            "1999-02-01T00:00:01",
            "1999-02-01T00:00:59",
            "1999-02-01T00:00:22"
        };
        //
        List sl = loadAList( dtStrs );
        boolean isSorted1 = isListSorted( sl );
        Collections.sort( sl, cSecond );
        boolean isSorted2 = isListSorted( sl );
        assertEquals("ListSecond", !isSorted1, isSorted2);
    } // end of testListSecond

     /**
      * Test sorting with minute comparator.
      */
    public void testListMinute() {
        String[] dtStrs = {
            "1999-02-01T00:10:00",
            "1999-02-01T00:30:00",
            "1999-02-01T00:25:00",
            "1999-02-01T00:18:00",
            "1999-02-01T00:01:00",
            "1999-02-01T00:59:00",
            "1999-02-01T00:22:00"
        };
        //
        List sl = loadAList( dtStrs );
        boolean isSorted1 = isListSorted( sl );
        Collections.sort( sl, cMinute );
        boolean isSorted2 = isListSorted( sl );
        assertEquals("ListMinute", !isSorted1, isSorted2);
    } // end of testListMinute

     /**
      * Test sorting with hour comparator.
      */
    public void testListHour() {
        String[] dtStrs = {
            "1999-02-01T10:00:00",
            "1999-02-01T23:00:00",
            "1999-02-01T01:00:00",
            "1999-02-01T15:00:00",
            "1999-02-01T05:00:00",
            "1999-02-01T20:00:00",
            "1999-02-01T17:00:00"
        };
        //
        List sl = loadAList( dtStrs );
        boolean isSorted1 = isListSorted( sl );
        Collections.sort( sl, cHour );
        boolean isSorted2 = isListSorted( sl );
        assertEquals("ListHour", !isSorted1, isSorted2);
    } // end of testListHour


     /**
      * Test sorting with day of week comparator.
      */
    public void testListDOW() {
        String[] dtStrs = {
            /* 2002-04-15 = Monday */
            "2002-04-21T10:00:00",
            "2002-04-16T10:00:00",
            "2002-04-15T10:00:00",
            "2002-04-17T10:00:00",
            "2002-04-19T10:00:00",
            "2002-04-18T10:00:00",
            "2002-04-20T10:00:00"
        };
        //
        List sl = loadAList( dtStrs );
        boolean isSorted1 = isListSorted( sl );
        Collections.sort( sl, cDayOfWeek );
        boolean isSorted2 = isListSorted( sl );
        assertEquals("ListDOW", !isSorted1, isSorted2);
    } // end of testListDOW

     /**
      * Test sorting with day of month comparator.
      */
    public void testListDOM() {
        String[] dtStrs = {
            /* 2002-04-14 = Sunday */
            "2002-04-20T10:00:00",
            "2002-04-16T10:00:00",
            "2002-04-15T10:00:00",
            "2002-04-17T10:00:00",
            "2002-04-19T10:00:00",
            "2002-04-18T10:00:00",
            "2002-04-14T10:00:00"
        };
        //
        List sl = loadAList( dtStrs );
        boolean isSorted1 = isListSorted( sl );
        Collections.sort( sl, cDayOfMonth );
        boolean isSorted2 = isListSorted( sl );
        assertEquals("ListDOM", !isSorted1, isSorted2);
    } // end of testListDOM

     /**
      * Test sorting with day of year comparator.
      */
    public void testListDOY() {
        String[] dtStrs = {
            "2002-04-20T10:00:00",
            "2002-01-16T10:00:00",
            "2002-12-31T10:00:00",
            "2002-09-14T10:00:00",
            "2002-09-19T10:00:00",
            "2002-02-14T10:00:00",
            "2002-10-30T10:00:00"
        };
        //
        List sl = loadAList( dtStrs );
        boolean isSorted1 = isListSorted( sl );
        Collections.sort( sl, cDayOfYear );
        boolean isSorted2 = isListSorted( sl );
        assertEquals("ListDOY", !isSorted1, isSorted2);
    } // end of testListDOY

     /**
      * Test sorting with week of weekyear comparator.
      */
    public void testListWOW() {
        String[] dtStrs = {
            "2002-04-01T10:00:00",
            "2002-01-01T10:00:00",
            "2002-12-01T10:00:00",
            "2002-09-01T10:00:00",
            "2002-09-01T10:00:00",
            "2002-02-01T10:00:00",
            "2002-10-01T10:00:00"
        };
        //
        List sl = loadAList( dtStrs );
        boolean isSorted1 = isListSorted( sl );
        Collections.sort( sl, cWeekOfWeekyear );
        boolean isSorted2 = isListSorted( sl );
        assertEquals("ListWOW", !isSorted1, isSorted2);
    } // end of testListWOW

     /**
      * Test sorting with year (given week) comparator.
      */
    public void testListYOYY() {
        // ?? How to catch end conditions ??
        String[] dtStrs = {
            "2010-04-01T10:00:00",
            "2002-01-01T10:00:00"
        };
        //
        List sl = loadAList( dtStrs );
        boolean isSorted1 = isListSorted( sl );
        Collections.sort( sl, cWeekyear );
        boolean isSorted2 = isListSorted( sl );
        assertEquals("ListYOYY", !isSorted1, isSorted2);
    } // end of testListYOYY


     /**
      * Test sorting with month comparator.
      */
    public void testListMonth() {
        String[] dtStrs = {
            "2002-04-01T10:00:00",
            "2002-01-01T10:00:00",
            "2002-12-01T10:00:00",
            "2002-09-01T10:00:00",
            "2002-09-01T10:00:00",
            "2002-02-01T10:00:00",
            "2002-10-01T10:00:00"
        };
        //
        List sl = loadAList( dtStrs );
        boolean isSorted1 = isListSorted( sl );
        Collections.sort( sl, cMonth );
        boolean isSorted2 = isListSorted( sl );
        assertEquals("ListMonth", !isSorted1, isSorted2);
    } // end of testListMonth

     /**
      * Test sorting with year comparator.
      */
     public void testListYear() {
        String[] dtStrs = {
            "1999-02-01T00:00:00",
            "1998-02-01T00:00:00",
            "2525-02-01T00:00:00",
            "1776-02-01T00:00:00",
            "1863-02-01T00:00:00",
            "1066-02-01T00:00:00",
            "2100-02-01T00:00:00"
        };
        //
        List sl = loadAList( dtStrs );
        boolean isSorted1 = isListSorted( sl );
        Collections.sort( sl, cYear );
        boolean isSorted2 = isListSorted( sl );
        assertEquals("ListYear", !isSorted1, isSorted2);
     } // end of testListYear

     /**
      * Test sorting with date only comparator.
      */
    public void testListDate() {
        String[] dtStrs = {
            "1999-02-01T00:00:00",
            "1998-10-03T00:00:00",
            "2525-05-20T00:00:00",
            "1776-12-25T00:00:00",
            "1863-01-31T00:00:00",
            "1066-09-22T00:00:00",
            "2100-07-04T00:00:00"
        };
        //
        List sl = loadAList( dtStrs );
        boolean isSorted1 = isListSorted( sl );
        Collections.sort( sl, cDate );
        boolean isSorted2 = isListSorted( sl );
        assertEquals("ListDate", !isSorted1, isSorted2);
    } // end of testListDate

     /**
      * Test sorting with time only comparator.
      */
    public void testListTime() {
        String[] dtStrs = {
            "1999-02-01T01:02:05",
            "1999-02-01T22:22:22",
            "1999-02-01T05:30:45",
            "1999-02-01T09:17:59",
            "1999-02-01T09:17:58",
            "1999-02-01T15:30:00",
            "1999-02-01T17:00:44"
        };
        //
        List sl = loadAList( dtStrs );
        boolean isSorted1 = isListSorted( sl );
        Collections.sort( sl, cTime );
        boolean isSorted2 = isListSorted( sl );
        assertEquals("ListTime", !isSorted1, isSorted2);
    } // end of testListTime


    /**
     * Test comparator operation with null object(s).
     */
    public void testNullDT() {
        // null means now
        aDateTime = getADate("2000-01-01T00:00:00");
        assertTrue(cYear.compare(null, aDateTime) > 0);
        assertTrue(cYear.compare(aDateTime, null) < 0);
    }

    /**
     * Test comparator operation with an invalid object type.
     */
    public void testInvalidObj() {
        aDateTime = getADate("2000-01-01T00:00:00");
        try {
            cYear.compare("FreeBird", aDateTime);
            fail("Invalid object failed");
        } catch (IllegalArgumentException cce) {}
    }

    // private convenience methods
    //-----------------------------------------------------------------------
    /**
     * Creates a date to test with.
     */
    private DateTime getADate(String s) {
        DateTime retDT = null;
        try {
            retDT = new DateTime(s, DateTimeZone.UTC);
        } catch (IllegalArgumentException pe) {
            pe.printStackTrace();
        }
        return retDT;
    }

    /**
     * Load a string array.
     */
    private List loadAList(String[] someStrs) {
        List newList = new ArrayList();
        try {
            for (int i = 0; i < someStrs.length; ++i) {
                newList.add(new DateTime(someStrs[i], DateTimeZone.UTC));
            } // end of the for
        } catch (IllegalArgumentException pe) {
            pe.printStackTrace();
        }
        return newList;
    }

    /**
     * Check if the list is sorted.
     */
    private boolean isListSorted(List tl) {
        // tl must be populated with DateTime objects.
        DateTime lhDT = (DateTime)tl.get(0);
        DateTime rhDT = null;
        Long lhVal = new Long( lhDT.getMillis() );
        Long rhVal = null;
        for (int i = 1; i < tl.size(); ++i) {
            rhDT = (DateTime)tl.get(i);
            rhVal = new Long( rhDT.getMillis() );
            if ( lhVal.compareTo( rhVal) > 0 ) return false;
            //
            lhVal = rhVal;  // swap for next iteration
            lhDT = rhDT;    // swap for next iteration
        }
        return true;
    }

}
