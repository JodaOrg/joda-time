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
public class TestMDTAdd extends BulkTest {
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
        return BulkTest.makeSuite(TestMDTAdd.class);
    }
    /**
     * TestMDTAdd constructor.
     * @param name
     */
    public TestMDTAdd(String name) {
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

    GregorianCalendar[] gcals0 = {
        new GregorianCalendar(1999, Calendar.JANUARY, 1, 0, 0, 0),
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
    /**
     *
     */
    Long longVal = null;
    /**
     *
     */
    boolean failed;

    /**
     * Test the <code>addMillis(long)</code> method.
     * @see org.joda.time.MutableDateTime#add(long)
     */
    public void testAddMillis() {
        fail("TBD");
    }

    public void testLast() {
        ewtr.println("TestMDTAdd completes");
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

} // end of class TestMDTAdd
