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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import junit.framework.TestSuite;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.Instant;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.chrono.ISOChronology;
/**
 * This class is a Junit unit test for the
 * org.joda.time.MutableDateTime class.
 * This currently forces tests using GMT only.
 *
 * @author Guy Allard
 */
public class TestMDTSet extends BulkTest {
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
        return BulkTest.makeSuite(TestMDTSet.class);
    }
    /**
     * TestMDTSet constructor.
     * @param name
     */
    public TestMDTSet(String name) {
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
    Long longVal = null;
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
    /**
     *
     */
    ReadableInstant rica = null;
    /**
     *
     */
    Chronology cronca = null;
    /**
     *
     */
    Date jdtca = null;
    /**
     *
     */
    Calendar calca = null;

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
    Chronology chrona = null;
    /**
     *
     */
    long setMillis = -1L;
    /**
     *
     */
    int setYear = -1;
    int setMonth = -1;
    int setDay = -1;
    int setHour = -1;
    int setMinute = -1;
    int setSecond = -1;
    int setMillisecond = -1;
    String setString = null;
    String yearStr = null;
    String monthStr = null;
    String dayStr = null;
    String hourStr = null;
    String minuteStr = null;
    String secondStr = null;
    String millisecondStr = null;
    //
    // Long longVal = null;
    //
    boolean getsDone = true;
    //
    // Test 'set' methods - 2nd.
    //
    //----------------------------------------------------------------
    //
    // Sets of Millis value.
    //
    /**
     * Test the <code>setMillis(long)</code> method.
     * @see org.joda.time.MutableDateTime#setMillis(long)
     */
    public void testSetMillisL() {
        getsDone = true;
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            mdt.setMillis( setMillis );
            assertEquals("SML1:"+isoString+":"+setString,
                mdt.getMillis(), setMillis);
        }
    }
    /**
     * Test the <code>setMillis(java.lang.Object)</code> method.
     * @see org.joda.time.MutableDateTime#setMillis(java.lang.Object)
     */
    public void testSetMillisO() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            //ewtr.println("getsDone: " + getsDone);
            //ewtr.println("rica: " + rica);
            //ewtr.flush();
            mdt.setMillis( rica );
            assertEquals("SMO1:"+isoString+":"+setString,
                setString, ""+mdt);
            //
            mdt.setMillis( jdtca );
            assertEquals("SMO2:"+isoString+":"+setString,
                setString, ""+mdt);
            //
            mdt.setMillis( calca );
            assertEquals("SMO3:"+isoString+":"+setString,
                setString, ""+mdt);
            //
            mdt.setMillis( longVal );
            assertEquals("SMO4:"+isoString+":"+setString,
                setString, ""+mdt);
            //
            mdt.setMillis( setString );
            assertEquals("SMO5:"+isoString+":"+setString,
                setString, ""+mdt);
            //
            failed = false;
            rica = null;
            try
            {
                mdt.setMillis( rica );
            }
            catch(IllegalArgumentException iae)
            {
                failed = true;
            }
            if ( !failed ) {
                fail("SMO6:null");
            }
        }
    }
    //
    // Set of individual fields through individual DateTe=imeFields.
    //
    /**
     * Test the <code>set(org.joda.time.DateTimeField, int)</code> method.
     * @see org.joda.time.MutableDateTime#set(org.joda.time.DateTimeField, int)
     */
    public void testSetDTFI() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            mdt.set( mdt.year().getField(), setYear );
            mdt.set( mdt.monthOfYear().getField(), setMonth );
            mdt.set( mdt.dayOfMonth().getField(), setDay );
            mdt.set( mdt.hourOfDay().getField(), setHour );
            mdt.set( mdt.minuteOfHour().getField(), setMinute );
            mdt.set( mdt.secondOfMinute().getField(), setSecond );
            mdt.set( mdt.millisOfSecond().getField(), setMillisecond );
            //ewtr.println("setString: " + setString);
            //ewtr.println("mdt: " + mdt);
            assertEquals("DTFI1:"+isoString+":"+setString,
                setString, ""+mdt);
        }
    }
    //
    // Sets of the date portion.
    //
    /**
     * Test the <code>setDate(long)</code> method.
     * @see org.joda.time.MutableDateTime#setDate(long)
     */
    public void testSetDateL() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            String before = "" + mdt;
            mdt.setDate( setMillis );
            String after = "" + mdt;
            assertEquals("SDL1:",
                rhsDT(before), rhsDT(after) );
            assertEquals("SDL2:",
                lhsDT(setString), lhsDT(after) );
        }
    }
    /**
     * Test the <code>setDate(java.lang.Object)</code> method.
     * @see org.joda.time.MutableDateTime#setDate(java.lang.Object)
     */
    public void testSetDateO() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            //
            String before = "" + mdt;
            String rhsBefore = rhsDT( before );
            //
            mdt.setDate( rica );
            String after = "" + mdt;
            String rhsAfter = rhsDT( after );
            assertEquals("SDO1:",
                rhsBefore, rhsAfter);
            assertEquals("SDO1A:",
                lhsDT(setString), lhsDT(after));
            //
            mdt.setDate( jdtca );
            after = "" + mdt;
            rhsAfter = rhsDT( after );
            assertEquals("SDO2:",
                rhsBefore, rhsAfter);
            assertEquals("SDO2A:",
                lhsDT(setString), lhsDT(after));
            //
            mdt.setDate( calca );
            after = "" + mdt;
            rhsAfter = rhsDT( after );
            assertEquals("SDO3:",
                rhsBefore, rhsAfter);
            assertEquals("SDO3A:",
                lhsDT(setString), lhsDT(after));
            //
            mdt.setDate( longVal );
            after = "" + mdt;
            rhsAfter = rhsDT( after );
            assertEquals("SDO4:",
                rhsBefore, rhsAfter);
            assertEquals("SDO4A:",
                lhsDT(setString), lhsDT(after));
            //
            mdt.setDate( setString );
            after = "" + mdt;
            rhsAfter = rhsDT( after );
            assertEquals("SDO5:",
                rhsBefore, rhsAfter);
            assertEquals("SDO5A:",
                lhsDT(setString), lhsDT(after));
            //
            failed = false;
            rica = null;
            try
            {
                mdt.setDate( rica );
            }
            catch(IllegalArgumentException iae)
            {
                failed = true;
            }
            if ( !failed ) {
                fail("SDO6:null");
            }

        }
    }
    /**
     * Test the <code>setDate(int, int, int)</code> method.
     * @see org.joda.time.MutableDateTime#setDate(int, int, int)
     */
    public void testSetDateIII() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            //
            String before = "" + mdt;
            String rhsBefore = rhsDT( before );
            //
            mdt.setDate( setYear, setMonth, setDay );
            //
            String after = "" + mdt;
            String rhsAfter = rhsDT( after );
            assertEquals("DIII1:",
                rhsBefore, rhsAfter);
            assertEquals("DIII1A:",
                lhsDT(setString), lhsDT(after));
        }
    }
    //
    // Sets of the time portion of the DT.
    //
    /**
     * Test the <code>setTime(long)</code> method.
     * @see org.joda.time.MutableDateTime#setTime(long)
     */
    public void testSetTimeL() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            String before = "" + mdt;
            mdt.setTime( setMillis );
            String after = "" + mdt;
            assertEquals("SDL1:",
                lhsDT(before), lhsDT(after) );
            assertEquals("SDL2:",
                rhsDT(setString), rhsDT(after) );
        }
    }
    /**
     * Test the <code>setTime(java.lang.Object)</code> method.
     * @see org.joda.time.MutableDateTime#setTime(java.lang.Object)
     */
    public void testSetTimeO() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            String before = "" + mdt;
            String lhsBefore = lhsDT( before );
            //
            mdt.setTime( rica );
            String after = "" + mdt;
            String lhsAfter = lhsDT( after );

            // ewtr.println("before: " + before);
            // ewtr.println("after: " + after);
            // ewtr.println("setString: " + setString);
            // ewtr.flush();

            assertEquals("STO1:",
                lhsBefore, lhsAfter);
            assertEquals("STO1A:",
                rhsDT(setString), rhsDT(after));
            //
            mdt.setTime( jdtca );
            after = "" + mdt;
            lhsAfter = lhsDT( after );
            assertEquals("STO2:",
                lhsBefore, lhsAfter);
            assertEquals("STO2A:",
                rhsDT(setString), rhsDT(after));
            //
            mdt.setTime( calca );
            after = "" + mdt;
            lhsAfter = lhsDT( after );
            assertEquals("STO3:",
                lhsBefore, lhsAfter);
            assertEquals("STO3A:",
                rhsDT(setString), rhsDT(after));
            //
            mdt.setTime( longVal );
            after = "" + mdt;
            lhsAfter = lhsDT( after );
            assertEquals("STO4:",
                lhsBefore, lhsAfter);
            assertEquals("STO4A:",
                rhsDT(setString), rhsDT(after));
            //
            mdt.setTime( setString );
            after = "" + mdt;
            lhsAfter = lhsDT( after );
            assertEquals("STO5:",
                lhsBefore, lhsAfter);
            assertEquals("STO5A:",
                rhsDT(setString), rhsDT(after));
            //
            failed = false;
            rica = null;
            try
            {
                mdt.setTime( rica );
            }
            catch(IllegalArgumentException iae)
            {
                failed = true;
            }
            if ( !failed ) {
                fail("STO6:null");
            }

        }
    }
    /**
     * Test the <code>setTime(int, int, int, int)</code> method.
     * @see org.joda.time.MutableDateTime#setTime(int, int, int, int)
     */
    public void testSetTimeIIII() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            String before = "" + mdt;
            mdt.setTime( setHour, setMinute, setSecond, setMillisecond );
            String after = "" + mdt;
            assertEquals("TIII1:",
                lhsDT(before), lhsDT(after) );
            assertEquals("TIII2:",
                rhsDT(setString), rhsDT(after) );
        }
    }
    //
    // Sets of both the Date and Time portions.
    //
    /**
     * Test the <code>setDateTime(long)</code> method.
     * @see org.joda.time.MutableDateTime#setDateTime(long)
     */
    public void testSetDateTimeL() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            mdt.setDateTime( setMillis );
            assertEquals("SDT1:",
                setString, ""+mdt );
        }
    }
    /**
     * Test the <code>setDateTime(java.lang.Object)</code> method.
     * @see org.joda.time.MutableDateTime#setDateTime(java.lang.Object)
     */
    public void testSetDateTimeO() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            //
            mdt.setDateTime( rica );
            assertEquals("SDTO1:",
                setString, ""+mdt );
            //
            mdt.setDateTime( jdtca );
            assertEquals("SDTO2:",
                setString, ""+mdt );
            //
            mdt.setDateTime( calca );
            assertEquals("SDTO3:",
                setString, ""+mdt );
            //
            mdt.setDateTime( longVal );
            assertEquals("SDTO4:",
                setString, ""+mdt );
            //
            mdt.setDateTime( setString );
            assertEquals("SDTO5:",
                setString, ""+mdt );
            //
            failed = false;
            rica = null;
            try
            {
                mdt.setDateTime( rica );
            }
            catch(IllegalArgumentException iae)
            {
                failed = true;
            }
            if ( !failed ) {
                fail("SDTO6:null");
            }
        }
    }
    /**
     * Test the <code>setDateTime(int, int, int, int, int, int, int)</code> method.
     * @see org.joda.time.MutableDateTime#setDateTime(int, int, int, int, int, int, int)
     */
    public void testSetDateTimeIs() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );
            mdt.setDateTime( setYear, setMonth, setDay,
                setHour, setMinute, setSecond, setMillisecond );
            assertEquals("SDTIIs",
                setString, ""+mdt );
        }
    }
    //
    // Individual field sets.
    //
    /**
     * Test the <code>setYear(int)</code> method.
     * @see org.joda.time.MutableDateTime#setYear(int)
     */
    public void testSetYear() {

        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );

            ArrayList before = getDTA( ""+mdt );
            mdt.setYear( setYear );
            ArrayList after = getDTA( ""+mdt );

            int[] mc = {3,5,7,9,11,13};
            for (int i = 0; i < mc.length; ++i) {
                assertEquals("TSYR1",
                    ""+(String)before.get(mc[i]),
                    ""+(String)after.get(mc[i])
                );
            }
            String e = (String)after.get(1);
            assertEquals("TSYR2",
                e, padNumberToLen(setYear,e.length())
            );
        }

        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );

            ArrayList before = getDTA( ""+mdt );
            mdt.year().set( setYear );
            ArrayList after = getDTA( ""+mdt );

            int[] mc = {3,5,7,9,11,13};
            for (int i = 0; i < mc.length; ++i) {
                assertEquals("TSYR1",
                    ""+(String)before.get(mc[i]),
                    ""+(String)after.get(mc[i])
                );
            }
            String e = (String)after.get(1);
            assertEquals("TSYR2",
                e, padNumberToLen(setYear,e.length())
            );
        }
    }
    /**
     * Test the <code>setMonthOfYear(int)</code> method.
     * @see org.joda.time.MutableDateTime#setMonthOfYear(int)
     */
    public void testSetMonthOfYear() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );

            ArrayList before = getDTA( ""+mdt );
            mdt.setMonthOfYear( setMonth );
            ArrayList after = getDTA( ""+mdt );

            int[] mc = {1,5,7,9,11,13};
            for (int i = 0; i < mc.length; ++i) {
                assertEquals("TSMO1",
                    ""+(String)before.get(mc[i]),
                    ""+(String)after.get(mc[i])
                );
            }
            String e = (String)after.get(3);
            assertEquals("TSMO2",
                e, padNumberToLen(setMonth,e.length())
            );
        }
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );

            ArrayList before = getDTA( ""+mdt );
            mdt.monthOfYear().set( setMonth );
            ArrayList after = getDTA( ""+mdt );

            int[] mc = {1,5,7,9,11,13};
            for (int i = 0; i < mc.length; ++i) {
                assertEquals("TSMO1",
                    ""+(String)before.get(mc[i]),
                    ""+(String)after.get(mc[i])
                );
            }
            String e = (String)after.get(3);
            assertEquals("TSMO2",
                e, padNumberToLen(setMonth,e.length())
            );
        }
    }
    /**
     * Test the <code>setDayOfMonth(int)</code> method.
     * @see org.joda.time.MutableDateTime#setDayOfMonth(int)
     */
    public void testSetDayOfMonth() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );

            ArrayList before = getDTA( ""+mdt );
            mdt.setDayOfMonth( setDay );
            ArrayList after = getDTA( ""+mdt );

            int[] mc = {1,3,7,9,11,13};
            for (int i = 0; i < mc.length; ++i) {
                assertEquals("TSDA1",
                    ""+(String)before.get(mc[i]),
                    ""+(String)after.get(mc[i])
                );
            }
            String e = (String)after.get(5);
            assertEquals("TSDA2",
                e, padNumberToLen(setDay,e.length())
            );
        }
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );

            ArrayList before = getDTA( ""+mdt );
            mdt.dayOfMonth().set( setDay );
            ArrayList after = getDTA( ""+mdt );

            int[] mc = {1,3,7,9,11,13};
            for (int i = 0; i < mc.length; ++i) {
                assertEquals("TSDA1",
                    ""+(String)before.get(mc[i]),
                    ""+(String)after.get(mc[i])
                );
            }
            String e = (String)after.get(5);
            assertEquals("TSDA2",
                e, padNumberToLen(setDay,e.length())
            );
        }
    }
    /**
     * Test the <code>setHourOfDay(int)</code> method.
     * @see org.joda.time.MutableDateTime#setHourOfDay(int)
     */
    public void testSetHourOfDay() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );

            ArrayList before = getDTA( ""+mdt );
            mdt.setHourOfDay( setHour );
            ArrayList after = getDTA( ""+mdt );

            int[] mc = {1,3,5,9,11,13};
            for (int i = 0; i < mc.length; ++i) {
                assertEquals("TSHD1",
                    ""+(String)before.get(mc[i]),
                    ""+(String)after.get(mc[i])
                );
            }
            String e = (String)after.get(7);
            assertEquals("TSHD2",
                e, padNumberToLen(setHour,e.length())
            );
        }
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );

            ArrayList before = getDTA( ""+mdt );
            mdt.hourOfDay().set( setHour );
            ArrayList after = getDTA( ""+mdt );

            int[] mc = {1,3,5,9,11,13};
            for (int i = 0; i < mc.length; ++i) {
                assertEquals("TSHD1",
                    ""+(String)before.get(mc[i]),
                    ""+(String)after.get(mc[i])
                );
            }
            String e = (String)after.get(7);
            assertEquals("TSHD2",
                e, padNumberToLen(setHour,e.length())
            );
        }
    }
    /**
     * Test the <code>setMinuteOfHour(int)</code> method.
     * @see org.joda.time.MutableDateTime#setMinuteOfHour(int)
     */
    public void testSetMinuteOfHour() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );

            ArrayList before = getDTA( ""+mdt );
            mdt.setMinuteOfHour( setHour );
            ArrayList after = getDTA( ""+mdt );

            int[] mc = {1,3,5,7,11,13};
            for (int i = 0; i < mc.length; ++i) {
                assertEquals("TSMH1",
                    ""+(String)before.get(mc[i]),
                    ""+(String)after.get(mc[i])
                );
            }
            String e = (String)after.get(9);
            assertEquals("TSMH2",
                e, padNumberToLen(setHour,e.length())
            );
        }
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );

            ArrayList before = getDTA( ""+mdt );
            mdt.minuteOfHour().set( setHour );
            ArrayList after = getDTA( ""+mdt );

            int[] mc = {1,3,5,7,11,13};
            for (int i = 0; i < mc.length; ++i) {
                assertEquals("TSMH1",
                    ""+(String)before.get(mc[i]),
                    ""+(String)after.get(mc[i])
                );
            }
            String e = (String)after.get(9);
            assertEquals("TSMH2",
                e, padNumberToLen(setHour,e.length())
            );
        }
    }
    /**
     * Test the <code>setSecondOfMinute(int)</code> method.
     * @see org.joda.time.MutableDateTime#setSecondOfMinute(int)
     */
    public void testSetSecondOfMinute() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );

            ArrayList before = getDTA( ""+mdt );
            mdt.setSecondOfMinute( setSecond );
            ArrayList after = getDTA( ""+mdt );

            int[] mc = {1,3,5,7,9,13};
            for (int i = 0; i < mc.length; ++i) {
                assertEquals("TSSM1",
                    ""+(String)before.get(mc[i]),
                    ""+(String)after.get(mc[i])
                );
            }
            String e = (String)after.get(11);
            assertEquals("TSSM2",
                e, padNumberToLen(setSecond,e.length())
            );
        }
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );

            ArrayList before = getDTA( ""+mdt );
            mdt.secondOfMinute().set( setSecond );
            ArrayList after = getDTA( ""+mdt );

            int[] mc = {1,3,5,7,9,13};
            for (int i = 0; i < mc.length; ++i) {
                assertEquals("TSSM1",
                    ""+(String)before.get(mc[i]),
                    ""+(String)after.get(mc[i])
                );
            }
            String e = (String)after.get(11);
            assertEquals("TSSM2",
                e, padNumberToLen(setSecond,e.length())
            );
        }
    }
    /**
     * Test the <code>setMillisOfSecond(int)</code> method.
     * @see org.joda.time.MutableDateTime#setMillisOfSecond(int)
     */
    public void testSetMillisOfSecond() {
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );

            ArrayList before = getDTA( ""+mdt );
            mdt.setMillisOfSecond( setMillisecond );
            ArrayList after = getDTA( ""+mdt );

            int[] mc = {1,3,5,7,9,11};
            for (int i = 0; i < mc.length; ++i) {
                assertEquals("TSMS1",
                    ""+(String)before.get(mc[i]),
                    ""+(String)after.get(mc[i])
                );
            }
            String e = (String)after.get(13);
            assertEquals("TSMS2",
                e, padNumberToLen(setMillisecond,e.length())
            );
        }
        for (int ngc = 0; ngc < gcals.length; ++ngc) {
            prepTest( ngc );

            ArrayList before = getDTA( ""+mdt );
            mdt.millisOfSecond().set( setMillisecond );
            ArrayList after = getDTA( ""+mdt );

            int[] mc = {1,3,5,7,9,11};
            for (int i = 0; i < mc.length; ++i) {
                assertEquals("TSMS1",
                    ""+(String)before.get(mc[i]),
                    ""+(String)after.get(mc[i])
                );
            }
            String e = (String)after.get(13);
            assertEquals("TSMS2",
                e, padNumberToLen(setMillisecond,e.length())
            );
        }
    }
    /**
     * Test the <code>setDayOfYear(int)</code> method.
     * @see org.joda.time.MutableDateTime#setDayOfYear(int)
     */
    public void testSetDayOfYear() {
        //
        ewtr.println("testSetDayOfYear needs enhancement");
        ewtr.flush();
        //
        String ts = "1776-07-04T11:22:33.000Z";
        //
        mdt = getMDTFromString( ts );
        mdt.setDayOfYear( 8 );
        assertEquals("SDOY1",
            "1776-01-08T11:22:33.000Z",
            mdt.toString() );

        mdt = getMDTFromString( ts );
        mdt.setDayOfYear( 60 );
        assertEquals("SDOY2",
            "1776-02-29T11:22:33.000Z",
            mdt.toString() );

        mdt = getMDTFromString( ts );
        mdt.setDayOfYear( 366 );
        assertEquals("SDOY3",
            "1776-12-31T11:22:33.000Z",
            mdt.toString() );

        mdt = getMDTFromString( ts );
        failed = false;
        try
        {
            mdt.setDayOfYear( 367 );
        }
        catch(IllegalArgumentException iae)
        {
            failed = true;
        }
        if ( !failed ) {
            fail("SDOY4");
        }

        mdt = getMDTFromString( "1777-12-31T11:22:33.000Z" );
        failed = false;
        try
        {
            mdt.setDayOfYear( 366 );
        }
        catch(IllegalArgumentException iae)
        {
            failed = true;
        }
        if ( !failed ) {
            fail("SDOY4");
        }
    }
    /**
     * Test the <code>setDayOfWeek(int)</code> method.
     * @see org.joda.time.MutableDateTime#setDayOfWeek(int)
     */
    public void testSetDayOfWeek() {
        //
        ewtr.println("testSetDayOfWeek needs enhancement");
        ewtr.flush();
        String ts = "1776-07-04T11:22:33.000Z";
        //
        mdt = getMDTFromString( ts );

        mdt.setDayOfWeek( DateTimeConstants.MONDAY );
        assertEquals("SDOW1",
            "1776-07-01T11:22:33.000Z",
            mdt.toString() );

        mdt.setDayOfWeek( DateTimeConstants.SUNDAY );
        assertEquals("SDOW2",
            "1776-07-07T11:22:33.000Z",
            mdt.toString() );
        //
        failed = false;
        try
        {
            mdt.setDayOfWeek( 8 );
        }
        catch(IllegalArgumentException iae)
        {
            failed = true;
        }
        if ( !failed ) {
            fail("SWOY4");
        }


    }
    /**
     * Test the <code>setWeekOfYear(int, int)</code> method.
     * @see org.joda.time.MutableDateTime#setWeekOfWeekyear(int)
     */
    public void testSetWeekOfYear() {
        ewtr.println("testSetWeekOfYear needs enhancement");
        ewtr.flush();
        String ts = "1776-07-04T11:22:33.000Z";
        //
        mdt = getMDTFromString( ts );

        mdt.setWeekyear( 1944 );
        mdt.setWeekOfWeekyear( 2 );
        assertEquals("SWOY1", 2, mdt.getWeekOfWeekyear());
        assertEquals("SWOY2", 1944, mdt.getWeekyear());
        assertEquals("SWOY3",
            "1944-01-13T11:22:33.000Z",
            mdt.toString() );
        //
        failed = false;
        try
        {
            mdt.setWeekyear( 2003 );
            mdt.setWeekOfWeekyear( 53 ); // 03 has 52 weeks
        }
        catch(IllegalArgumentException iae)
        {
            failed = true;
        }
        if ( !failed ) {
            fail("SWOY4");
        }
        //
        failed = false;
        try
        {
            mdt.setWeekyear( 2004 );
            mdt.setWeekOfWeekyear( 53 ); // 03 has 52 weeks
        }
        catch(IllegalArgumentException iae)
        {
            failed = true;
        }
        if ( failed ) {
            fail("SWOY5");
        }
    }

    public void testLast() {
        ewtr.println("TestMDTSet completes");
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

        setMillis = System.currentTimeMillis();
        GregorianCalendar gx = new GregorianCalendar();
        gx.setTime( new Date( setMillis ) );
        //
        setYear = gx.get(Calendar.YEAR);
        setMonth = gx.get(Calendar.MONTH) + 1;
        setDay = gx.get(Calendar.DATE);
        setHour = gx.get(Calendar.HOUR_OF_DAY);
        setMinute = gx.get(Calendar.MINUTE);
        setSecond = gx.get(Calendar.SECOND);
        setMillisecond = gx.get(Calendar.MILLISECOND);
        //
        rica = new Instant( setMillis );
        jdtca = new Date( setMillis );
        calca = Calendar.getInstance();
        calca.setTime( jdtca );
        longVal = new Long( setMillis );
        //
        MutableDateTime temp = new MutableDateTime( setMillis,
            ISOChronology.getInstanceUTC() );
        setString = "" + temp;
    }
} // end of class TestMDTSet
