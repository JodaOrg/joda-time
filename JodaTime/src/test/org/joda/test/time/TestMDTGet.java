/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2004 Stephen Colebourne.  
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Joda project (http://www.joda.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "Joda" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact licence@joda.org.
 *
 * 5. Products derived from this software may not be called "Joda",
 *    nor may "Joda" appear in their name, without prior written
 *    permission of the Joda project.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE JODA AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Joda project and was originally 
 * created by Stephen Colebourne <scolebourne@joda.org>. For more
 * information on the Joda project, please see <http://www.joda.org/>.
 */
package org.joda.test.time;

import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import junit.framework.TestSuite;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.MutableDateTime;
import org.joda.time.chrono.iso.ISOChronology;
/**
 * This class is a Junit unit test for the
 * org.joda.time.MutableDateTime class.
 * This currently forces tests using GMT only.
 *
 * @author Guy Allard
 */
public class TestMDTGet extends BulkTest {
    /**
     * main
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    /**
     * TestSuite
     */
    public static TestSuite suite() {
        return BulkTest.makeSuite(TestMDTGet.class);
    }
    /**
     * TestMDTGet constructor.
     * @param name
     */
    public TestMDTGet(String name) {
        super(name);
    }
    // Class Name: org.joda.time.MutableDateTime
    /**
     * Junit <code>setUp()</code> method.
     */
    protected void setUp() /* throws Exception */ {
        // super.setUp();
        ++testNum;
        TimeZone.setDefault( new SimpleTimeZone(0, "UTC") );
        gcals = gcals0;
        // gcals = gcalsMiscBunch;
    }
    /**
     * Junit <code>tearDown()</code> method.
     */
    protected void tearDown() /* throws Exception */ {
        // super.tearDown();
    }

    private boolean debug = true;
    private static int testNum = 0;
    private PrintStream ewtr = System.err;

    /**
     *
     */
    boolean failed;
    // Developers/Testers Note:
    //
    // 1) Construct any valid array of Gregorian calendar objects,
    //    e.g. 'gcals1' below.
    // 2) Change the assignment statement in the setUp() method.
    // 3) Recompile+jar.
    //
    // The new array will be used in all the tests.
    //
    /**
     *
     */
    GregorianCalendar[] gcals0 = {
        new GregorianCalendar(1999, Calendar.JANUARY, 1, 0, 0, 0),
    };
    GregorianCalendar[] gcals1 = {
        new GregorianCalendar(1999, Calendar.JANUARY, 1, 0, 0, 0),
        new GregorianCalendar(1999, 1, 1, 0, 0, 0),
        new GregorianCalendar(1999, 2, 1, 0, 0, 0),
        new GregorianCalendar(2000, 0, 1, 0, 0, 0),
        new GregorianCalendar(2000, 1, 1, 0, 0, 0),
        new GregorianCalendar(2000, 2, 1, 0, 0, 0),
    };
    GregorianCalendar[] gcalsGJCutOver = {
        new GregorianCalendar(1582, Calendar.OCTOBER, 4, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 5, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 6, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 7, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 8, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 9, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 10, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 11, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 12, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 13, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 14, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 15, 0, 0, 0),
    };
    GregorianCalendar[] gcalsPreCut = {
        new GregorianCalendar(1, Calendar.JANUARY, 1, 0, 0, 0),
        new GregorianCalendar(400, Calendar.FEBRUARY, 28, 0, 0, 0),
        new GregorianCalendar(403, Calendar.FEBRUARY, 28, 0, 0, 0),
        new GregorianCalendar(404, Calendar.FEBRUARY, 29, 0, 0, 0),
        new GregorianCalendar(1066, Calendar.DECEMBER, 25, 0, 0, 0),
        new GregorianCalendar(1492, Calendar.MAY, 5, 0, 0, 0),
        new GregorianCalendar(1385, Calendar.MARCH, 23, 0, 0, 0),
    };

    GregorianCalendar[] gcalsYear0 = {
        new GregorianCalendar(0, Calendar.JANUARY, 1, 0, 0, 0),
        new GregorianCalendar(0, Calendar.DECEMBER, 31, 23, 59, 59),
    };
    GregorianCalendar[] gcalsYearM1 = {
        new GregorianCalendar(-1, Calendar.JANUARY, 1, 0, 0, 0),
    };
    GregorianCalendar[] gcalsEndPoints = {
        new GregorianCalendar(-9999, Calendar.JANUARY, 1, 0, 0, 0),
        new GregorianCalendar(+9999, Calendar.DECEMBER, 31, 23, 59, 59),
    };
    // This don't work.  ParseException from the CTOR.
    GregorianCalendar[] gcalsOutSide = {
        new GregorianCalendar(+76542, Calendar.DECEMBER, 31, 23, 59, 59),
        new GregorianCalendar(-12345, Calendar.JANUARY, 1, 0, 0, 0),
    };
    //
    GregorianCalendar[] gcalsMiscBunch = {
        new GregorianCalendar(-9999, Calendar.JANUARY, 1, 0, 0, 0),
        new GregorianCalendar(+9999, Calendar.DECEMBER, 31, 23, 59, 59),
        new GregorianCalendar(-1, Calendar.JANUARY, 1, 0, 0, 0),
        new GregorianCalendar(0, Calendar.JANUARY, 1, 0, 0, 0),
        new GregorianCalendar(0, Calendar.DECEMBER, 31, 23, 59, 59),
        new GregorianCalendar(1, Calendar.JANUARY, 1, 0, 0, 0),
        new GregorianCalendar(400, Calendar.FEBRUARY, 28, 0, 0, 0),
        new GregorianCalendar(403, Calendar.FEBRUARY, 28, 0, 0, 0),
        new GregorianCalendar(404, Calendar.FEBRUARY, 29, 0, 0, 0),
        new GregorianCalendar(1066, Calendar.DECEMBER, 25, 0, 0, 0),
        new GregorianCalendar(1492, Calendar.MAY, 5, 0, 0, 0),
        new GregorianCalendar(1385, Calendar.MARCH, 23, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 4, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 5, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 6, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 7, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 8, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 9, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 10, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 11, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 12, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 13, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 14, 0, 0, 0),
        new GregorianCalendar(1582, Calendar.OCTOBER, 15, 0, 0, 0),
        new GregorianCalendar(1999, Calendar.JANUARY, 1, 0, 0, 0),
        new GregorianCalendar(1999, 1, 1, 0, 0, 0),
        new GregorianCalendar(1999, 2, 1, 0, 0, 0),
        new GregorianCalendar(2000, 0, 1, 0, 0, 0),
        new GregorianCalendar(2000, 1, 1, 0, 0, 0),
        new GregorianCalendar(2000, 2, 1, 0, 0, 0),
    };
    //
    /**
     *
     */
    GregorianCalendar[] gcals = null;
    /**
     *
     */
    final int bumpValue = 385;
    // final int bumpValue = 0;
    /**
     *
     */
    // GregorianCalendar gcal = null;
    /**
     *
     */
    String isoString = null;
    /**
     *
     */
    MutableDateTime mdt = null;
    /**
     *
     */
    int expected_year = -1;         // Calculated in setup
    /**
     *
     */
    int expected_mon = -1;
    /**
     *
     */
    int expected_day = -1;
    /**
     *
     */
    int expected_hour = -1;
    /**
     *
     */
    int expected_minute = -1;
    /**
     *
     */
    int expected_second = -1;
    /**
     *
     */
    int expected_doy = -1;
    /**
     *
     */
    int expected_woyw = -1;
    /**
     *
     */
    int expected_woyy = -1;
    /**
     *
     */
    int expected_dow = -1;
    /**
     *
     */
    long expected_gcmillis = -1L;
    /**
     *
     */
    long expected_millisofday = -1L;
    /**
     *
     */
    long expected_secondofday = -1L;
    /**
     *
     */
    long expected_minuteofday = -1L;
    /**
     *
     */
    Chronology chrona = null;
    //
    // Test 'get' methods - 1st.
    //
    /**
     * Test the <code>getChronology()</code> method.
     * @see org.joda.time.MutableDateTime#getChronology()
     */
    public void testGetChronology() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            chrona = mdt.getChronology();
            assertNotNull("GC1:"+isoString, chrona);
            // Commented out until ?
            // assertTrue("GC2:"+isoString, chrona instanceof ISOChronology);
        }
    }
    /**
     * Test the <code>getMillis()</code> method.
     * @see org.joda.time.MutableDateTime#getMillis()
     */
    public void testGetMillis() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("GM1:"+isoString, expected_gcmillis, mdt.getMillis());
        }
    }
    /**
     * Test the <code>getDayOfWeek()</code> method.
     * @see org.joda.time.MutableDateTime#getDayOfWeek()
     */
    public void testGetDayOfWeek() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("GDOW1:"+isoString, expected_dow, mdt.getDayOfWeek() );
        }
    }
    /**
     * Test the <code>getDayOfMonth()</code> method.
     * @see org.joda.time.MutableDateTime#getDayOfMonth()
     */
    public void testGetDayOfMonth() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("GDOM1:"+isoString, expected_day, mdt.getDayOfMonth());
        }
    }
    /**
     * Test the <code>getDayOfYear()</code> method.
     * @see org.joda.time.MutableDateTime#getDayOfYear()
     */
    public void testGetDayOfYear() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("GDOY1:"+isoString, expected_doy, mdt.getDayOfYear());
        }
    }
    /**
     * Test the <code>getWeekOfYearWeek()</code> method.
     * @see org.joda.time.MutableDateTime#getWeekOfWeekyear()
     */
    public void testGetWeekOfWeekyear() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("GWOYW1:"+isoString, expected_woyw, mdt.getWeekOfWeekyear());
        }
    }
    /**
     * Test the <code>getWeekOfYearYear()</code> method.
     * @see org.joda.time.MutableDateTime#getWeekOfWeekyear()
     */
    public void testGetYearOfWeekyear() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("GWOYY1:"+isoString, expected_woyy, mdt.getWeekyear());
        }
    }
    /**
     * Test the <code>getMonthOfYear()</code> method.
     * @see org.joda.time.MutableDateTime#getMonthOfYear()
     */
    public void testGetMonthOfYear() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("GMOY1:"+isoString, expected_mon, mdt.getMonthOfYear());
        }
    }
    /**
     * Test the <code>getYear()</code> method.
     * @see org.joda.time.MutableDateTime#getYear()
     */
    public void testGetYear() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("GY1:"+isoString, expected_year, mdt.getYear());
        }
    }
    /**
     * Test the <code>getYearOfEra()</code> method.
     * @see org.joda.time.MutableDateTime#getYearOfEra()
     */
    /*
    public void testGetYearOfEra() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("GYOE1:"+isoString, Math.abs(expected_year), mdt.getYearOfEra());
        }
    }
    */
    /**
     * Test the <code>getEra()</code> method.
     * @see org.joda.time.MutableDateTime#getEra()
     */
    /*
    public void testGetEra() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            if ( gcals[ngc].get(Calendar.ERA) == GregorianCalendar.AD) {
                assertEquals("GERA1:"+isoString,
                    DateTimeConstants.AD, mdt.getEra());
            } else {
                assertEquals("GERA1:"+isoString,
                    DateTimeConstants.BC, mdt.getEra());
            }
        }
    }
    */
    /**
     * Test the <code>getMillisOfSecond()</code> method.
     * @see org.joda.time.MutableDateTime#getMillisOfSecond()
     */
    public void testGetMillisOfSecond() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("GMOS1:"+isoString, bumpValue, mdt.getMillisOfSecond());
        }
    }
    /**
     * Test the <code>getMillisOfDay()</code> method.
     * @see org.joda.time.MutableDateTime#getMillisOfDay()
     */
    public void testGetMillisOfDay() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("GMLSOD1:"+isoString, expected_millisofday, mdt.getMillisOfDay());
        }
    }
    /**
     * Test the <code>getSecondOfMinute()</code> method.
     * @see org.joda.time.MutableDateTime#getSecondOfMinute()
     */
    public void testGetSecondOfMinute() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("GSOM1:"+isoString, expected_second, mdt.getSecondOfMinute());
        }
    }
    /**
     * Test the <code>getSecondOfDay()</code> method.
     * @see org.joda.time.MutableDateTime#getSecondOfDay()
     */
    public void testGetSecondOfDay() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("GSOD1:"+isoString, expected_secondofday, mdt.getSecondOfDay());
        }
    }
    /**
     * Test the <code>getMinuteOfHour()</code> method.
     * @see org.joda.time.MutableDateTime#getMinuteOfHour()
     */
    public void testGetMinuteOfHour() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("GMOH1:"+isoString, expected_minute, mdt.getMinuteOfHour());
        }
    }
    /**
     * Test the <code>getMinuteOfDay()</code> method.
     * @see org.joda.time.MutableDateTime#getMinuteOfDay()
     */
    public void testGetMinuteOfDay() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("GMINOD1:"+isoString,
                expected_minuteofday, mdt.getMinuteOfDay());
        }
    }
    /**
     * Test the <code>getHourOfDay()</code> method.
     * @see org.joda.time.MutableDateTime#getHourOfDay()
     */
    public void testGetHourOfDay() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("GHOD1:"+isoString, expected_hour,
                mdt.getHourOfDay());
        }
    }
    /**
     * Test the <code>dayOfWeek()</code> method.
     * @see org.joda.time.MutableDateTime#dayOfWeek()
     */
    public void testDayOfWeek() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("DOW1:"+isoString, expected_dow,
                mdt.dayOfWeek().get() );
            assertEquals("DOW2:"+isoString, expected_dow,
                mdt.dayOfWeek().getField().get(mdt.getMillis()));
            //
            assertEquals("DOW3:"+isoString,
                dowShort[mdt.dayOfWeek().get()],
                mdt.dayOfWeek().getAsShortText() );
            assertEquals("DOW4:"+isoString,
                dowLong[mdt.dayOfWeek().get()],
                mdt.dayOfWeek().getAsText() );
        }
    }
    /**
     * Test the <code>dayOfMonth()</code> method.
     * @see org.joda.time.MutableDateTime#dayOfMonth()
     */
    public void testDayOfMonth() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("DOM1:"+isoString, expected_day,
                mdt.dayOfMonth().getField().get(mdt.getMillis()));
            assertEquals("DOM2:"+isoString, expected_day,
                mdt.dayOfMonth().getField().get(mdt.getMillis()));
            assertEquals("DOM3:"+isoString,
                ""+expected_day, mdt.dayOfMonth().getAsShortText() );
            assertEquals("DOM4:"+isoString,
                ""+expected_day, mdt.dayOfMonth().getAsText() );
        }
    }
    /**
     * Test the <code>dayOfYear()</code> method.
     * @see org.joda.time.MutableDateTime#dayOfYear()
     */
    public void testDayOfYear() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("DOY1:"+isoString, expected_doy,
                mdt.dayOfYear().getField().get(mdt.getMillis()));
            assertEquals("DOY2:"+isoString, expected_doy,
                mdt.dayOfYear().getField().get(mdt.getMillis()));
            assertEquals("DOY3:"+isoString,
                ""+expected_doy, mdt.dayOfYear().getAsShortText() );
            assertEquals("DOY4:"+isoString,
                ""+expected_doy, mdt.dayOfYear().getAsText() );
        }
    }
    /**
     * Test the <code>weekOfYearWeek()</code> method.
     * @see org.joda.time.MutableDateTime#weekOfWeekyear()
     */
    public void testWeekOfWeekyear() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("DWOYW1:"+isoString, expected_woyw,
                mdt.weekOfWeekyear().getField().get(mdt.getMillis()));
            assertEquals("DWOYW2:"+isoString, expected_woyw,
                mdt.weekOfWeekyear().getField().get(mdt.getMillis()));
            assertEquals("DWOYW3:"+isoString,
                ""+expected_woyw, mdt.weekOfWeekyear().getAsShortText() );
            assertEquals("DWOYW4:"+isoString,
                ""+expected_woyw, mdt.weekOfWeekyear().getAsText() );
        }
    }
    /**
     * Test the <code>weekOfYearYear()</code> method.
     * @see org.joda.time.MutableDateTime#weekyear()
     */
    public void testWeekyear() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("DWOYY1:"+isoString, expected_woyy,
                mdt.weekyear().getField().get(mdt.getMillis()));
            assertEquals("DWOYY2:"+isoString, expected_woyy,
                mdt.weekyear().getField().get(mdt.getMillis()));
            assertEquals("DWOYY3:"+isoString,
                ""+expected_woyy, mdt.weekyear().getAsShortText() );
            assertEquals("DWOYY4:"+isoString,
                ""+expected_woyy, mdt.weekyear().getAsText() );
        }
    }
    /**
     * Test the <code>monthOfYear()</code> method.
     * @see org.joda.time.MutableDateTime#monthOfYear()
     */
    public void testMonthOfYear() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("MOY1:"+isoString, expected_mon,
                mdt.monthOfYear().getField().get(mdt.getMillis()));
            assertEquals("MOY2:"+isoString, expected_mon,
                mdt.monthOfYear().getField().get(mdt.getMillis()));
            assertEquals("MOY3:"+isoString,
                moyShort[mdt.monthOfYear().getField().get(mdt.getMillis())],
                mdt.monthOfYear().getAsShortText() );
            assertEquals("MOY4:"+isoString,
                moyLong[mdt.monthOfYear().getField().get(mdt.getMillis())],
                mdt.monthOfYear().getAsText() );
        }
    }
    /**
     * Test the <code>year()</code> method.
     * @see org.joda.time.MutableDateTime#year()
     */
    public void testYear() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("Y1:"+isoString, expected_year,
                mdt.year().getField().get(mdt.getMillis()));
            assertEquals("Y2:"+isoString, expected_year,
                mdt.year().getField().get(mdt.getMillis()));
            assertEquals("Y3:"+isoString, ""+expected_year, mdt.year().getAsShortText() );
            assertEquals("Y4:"+isoString, ""+expected_year, mdt.year().getAsText() );
        }
    }
    /**
     * Test the <code>yearOfEra()</code> method.
     * @see org.joda.time.MutableDateTime#yearOfEra()
     */
    /*
    public void testYearOfEra() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            int lty = Math.abs( expected_year );
            assertEquals("YOE1:"+isoString, lty,
                mdt.yearOfEra().getField().get(mdt.getMillis()));
            assertEquals("YOE2:"+isoString, lty,
                mdt.yearOfEra().getField().get(mdt.getMillis()));
            assertEquals("YOE3:"+isoString, ""+lty, mdt.yearOfEra().getAsShortText() );
            assertEquals("YOE4:"+isoString, ""+lty, mdt.yearOfEra().getAsText() );
        }
    }
    */
    /**
     * Test the <code>era()</code> method.
     * @see org.joda.time.MutableDateTime#era()
     */
    /*
    public void testEra() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            if ( gcals[ngc].get(Calendar.ERA) == GregorianCalendar.AD) {
                assertEquals("ERA1:"+isoString, DateTimeConstants.AD,
                    mdt.era().getField().get(mdt.getMillis()));
                assertEquals("ERA2:"+isoString, DateTimeConstants.AD,
                    mdt.era().getField().get(mdt.getMillis()));
                assertEquals("ERA3:"+isoString, "AD", mdt.era().getAsShortText() );
                assertEquals("ERA4:"+isoString, "AD", mdt.era().getAsText() );
            } else {
                assertEquals("ERA1:"+isoString, DateTimeConstants.BC,
                    mdt.era().getField().get(mdt.getMillis()));
                assertEquals("ERA2:"+isoString, DateTimeConstants.BC,
                    mdt.era().getField().get(mdt.getMillis()));
                assertEquals("ERA3:"+isoString, "BC", mdt.era().getAsShortText() );
                assertEquals("ERA4:"+isoString, "BC", mdt.era().getAsText() );
            }
        }
    }
    */
    /**
     * Test the <code>millisOfSecond()</code> method.
     * @see org.joda.time.MutableDateTime#millisOfSecond()
     */
    public void testMillisOfSecond() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("MOS1:"+isoString,
                bumpValue, mdt.millisOfSecond().getField().get(mdt.getMillis()));
            assertEquals("MOS2:"+isoString,
                bumpValue, mdt.millisOfSecond().getField().get(mdt.getMillis()));
            assertEquals("MOS3:"+isoString,
                ""+bumpValue, mdt.millisOfSecond().getAsShortText() );
            assertEquals("MOS4:"+isoString,
                ""+bumpValue, mdt.millisOfSecond().getAsText() );
        }
    }
    /**
     * Test the <code>millisOfDay()</code> method.
     * @see org.joda.time.MutableDateTime#millisOfDay()
     */
    public void testMillisOfDay() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("MLSOD1:"+isoString,
                expected_millisofday,
                mdt.millisOfDay().getField().get(mdt.getMillis()));
            assertEquals("MLSOD2:"+isoString,
                expected_millisofday,
                mdt.millisOfDay().getField().get(mdt.getMillis()));
            assertEquals("MLSOD3:"+isoString,
                ""+expected_millisofday, mdt.millisOfDay().getAsShortText() );
            assertEquals("MLSOD4:"+isoString,
                ""+expected_millisofday, mdt.millisOfDay().getAsText() );
        }
    }
    /**
     * Test the <code>secondOfMinute()</code> method.
     * @see org.joda.time.MutableDateTime#secondOfMinute()
     */
    public void testSecondOfMinute() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("SOM1:"+isoString, expected_second,
                mdt.secondOfMinute().getField().get(mdt.getMillis()));
            assertEquals("SOM2:"+isoString, expected_second,
                mdt.secondOfMinute().getField().get(mdt.getMillis()));
            assertEquals("SOM3:"+isoString,
                ""+expected_second, mdt.secondOfMinute().getAsShortText() );
            assertEquals("SOM4:"+isoString,
                ""+expected_second, mdt.secondOfMinute().getAsText() );
        }
    }
    /**
     * Test the <code>secondOfDay()</code> method.
     * @see org.joda.time.MutableDateTime#secondOfDay()
     */
    public void testSecondOfDay() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("SOD1:"+isoString, expected_secondofday,
                mdt.secondOfDay().getField().get(mdt.getMillis()));
            assertEquals("SOD2:"+isoString, expected_secondofday,
                mdt.secondOfDay().getField().get(mdt.getMillis()));
            assertEquals("SOD3:"+isoString, ""+expected_secondofday,
                mdt.secondOfDay().getAsShortText() );
            assertEquals("SOD4:"+isoString, ""+expected_secondofday,
                mdt.secondOfDay().getAsText() );
        }
    }
    /**
     * Test the <code>minuteOfHour()</code> method.
     * @see org.joda.time.MutableDateTime#minuteOfHour()
     */
    public void testMinuteOfHour() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("MOH1:"+isoString, expected_minute,
                mdt.minuteOfHour().getField().get(mdt.getMillis()));
            assertEquals("MOH2:"+isoString, expected_minute,
                mdt.minuteOfHour().getField().get(mdt.getMillis()));
            assertEquals("MOH3:"+isoString,
                ""+expected_minute, mdt.minuteOfHour().getAsShortText() );
            assertEquals("MOH4:"+isoString,
                ""+expected_minute, mdt.minuteOfHour().getAsText() );
        }
    }
    /**
     * Test the <code>minuteOfDay()</code> method.
     * @see org.joda.time.MutableDateTime#minuteOfDay()
     */
    public void testMinuteOfDay() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("MOD1:"+isoString, expected_minuteofday,
                mdt.minuteOfDay().getField().get(mdt.getMillis()));
            assertEquals("MOD2:"+isoString, expected_minuteofday,
                mdt.minuteOfDay().getField().get(mdt.getMillis()));
            assertEquals("MOD3:"+isoString, ""+expected_minuteofday,
                mdt.minuteOfDay().getAsShortText() );
            assertEquals("MOD4:"+isoString, ""+expected_minuteofday,
                mdt.minuteOfDay().getAsText() );
        }
    }
    /**
     * Test the <code>hourOfDay()</code> method.
     * @see org.joda.time.MutableDateTime#hourOfDay()
     */
    public void testHourOfDay() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            assertEquals("HOD1:"+isoString, expected_hour,
                mdt.hourOfDay().getField().get(mdt.getMillis()));
            assertEquals("HOD2:"+isoString, expected_hour,
                mdt.hourOfDay().getField().get(mdt.getMillis()));
            assertEquals("HOD3:"+isoString,
                ""+expected_hour, mdt.hourOfDay().getAsShortText() );
            assertEquals("HOD4:"+isoString,
                ""+expected_hour, mdt.hourOfDay().getAsText() );
        }
    }
    private static boolean getsDone = false;
    /**
     * Test the <code>toString()</code> method.
     * @see org.joda.time.MutableDateTime#toString()
     */
    public void testToString() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );

            assertEquals("TOS1:"+isoString,
                // isoString + "." + bumpValue + "Z",
                isoString,
                mdt.toString());
            /*
            assertEquals("TOS1:"+isoString,
                isoString + "." + "000" + "Z",
                mdt.toString());
            */
        }
    }

    public void testLast() {
        ewtr.println("TestMDTGet completes");
        ewtr.flush();
    }

    //-----------------------------------------------------------------
    //
    // Private methods.
    //
    //
    private  void prepTest(int item) {
        GregorianCalendar wrkgcal = new GregorianCalendar(
            gcals[item].get( Calendar.YEAR ),
            gcals[item].get( Calendar.MONTH ),
            gcals[item].get( Calendar.DATE ),
            gcals[item].get( Calendar.HOUR ),
            gcals[item].get( Calendar.MINUTE ),
            gcals[item].get( Calendar.SECOND )
        );
        wrkgcal.setGregorianChange(new Date(Long.MIN_VALUE));
        wrkgcal.set(Calendar.ERA, gcals[item].get(Calendar.ERA));
        //
        wrkgcal.add(Calendar.MILLISECOND, bumpValue);
        wrkgcal.setMinimalDaysInFirstWeek(4);
        wrkgcal.setFirstDayOfWeek(Calendar.MONDAY);
        //
        // Force the GregorianCalendar to recalculate all internal
        // values.
        //
        wrkgcal.setTime( wrkgcal.getTime() );

        if ( debug ) {
            ewtr.println("====Test Number: " + testNum);
            ewtr.println("  ==Item Number: " + item);
            ewtr.flush();
        }
        isoString = getDateTimeString( wrkgcal );
        mdt = null;
        try
        {
            mdt = new MutableDateTime( isoString, ISOChronology.getInstanceUTC() );
        }
        catch(IllegalArgumentException pe)
        {
            ewtr.println("IllegalArgumentException Detected: " + isoString);
            ewtr.println( pe.getMessage() );
            ewtr.flush();
        }
        if ( debug ) {
            ewtr.println("    =ISO String: " + isoString);
            ewtr.flush();
        }
        //
        // When it is easy to do, we locally calculate test comparison
        // values rather than depend on Java Calendar's.
        // If it is not fairly convenient, we call the Calendar's
        // get method.
        //
        // However, the following is very ugly code and needs to be
        // cleaned up.
        //
        int pb = 0;
        if ( isoString.substring(0,1).equals("-") ) {
            pb = 1;
        }
        //
        // Calculate expected values.
        //
        expected_gcmillis = wrkgcal.getTime().getTime();
        //
        expected_year = getPartValue( isoString.substring(0+pb,4+pb) );
        if ( pb == 1 ) {
            expected_year = -expected_year;
        }
        //
        expected_mon = getPartValue( isoString.substring(5+pb,7+pb) );
        expected_day = getPartValue( isoString.substring(8+pb,10+pb) );
        expected_hour = getPartValue( isoString.substring(11+pb,13+pb) );
        expected_minute = getPartValue( isoString.substring(14+pb,16+pb) );
        expected_second = getPartValue( isoString.substring(17+pb,19+pb) );
        //
        expected_doy = wrkgcal.get(Calendar.DAY_OF_YEAR);
        expected_dow = wrkgcal.get(Calendar.DAY_OF_WEEK) - 1;
        expected_dow = (expected_dow <= 0 ? 7 : expected_dow);
        //
        expected_woyw = wrkgcal.get(Calendar.WEEK_OF_YEAR);
        if (expected_woyw == 1) {
            wrkgcal.add(Calendar.DATE, +7);
        } else if (expected_woyw > 51) {
            wrkgcal.add(Calendar.DATE, -14);
        }
        expected_woyy = wrkgcal.get(Calendar.YEAR);
        if ( wrkgcal.get(Calendar.ERA) == GregorianCalendar.BC ) {
            expected_woyy = -(expected_woyy-1);
        }
        //
        expected_millisofday = expected_hour * DateTimeConstants.MILLIS_PER_HOUR
            + expected_minute * DateTimeConstants.MILLIS_PER_MINUTE
            + expected_second * DateTimeConstants.MILLIS_PER_SECOND
            + bumpValue;
        expected_secondofday = expected_hour * DateTimeConstants.SECONDS_PER_HOUR
            + expected_minute * DateTimeConstants.SECONDS_PER_MINUTE
            + expected_second;
        expected_minuteofday = expected_hour * DateTimeConstants.MINUTES_PER_HOUR
            + expected_minute;
        return;
    }
} // end of class TestMDTGet
