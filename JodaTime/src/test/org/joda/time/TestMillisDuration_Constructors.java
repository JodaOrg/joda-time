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
package org.joda.time;

import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This class is a JUnit test for Duration.
 *
 * @author Stephen Colebourne
 */
public class TestMillisDuration_Constructors extends TestCase {
    // Test in 2002/03 as time zones are more well known
    // (before the late 90's they were all over the place)

    private static final DateTimeZone PARIS = DateTimeZone.getInstance("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.getInstance("Europe/London");
    
    long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                     366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                     365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                     366 + 365;
    long y2003days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                     366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                     365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                     366 + 365 + 365;
    
    // 2002-06-09
    private long TEST_TIME_NOW =
            (y2002days + 31L + 28L + 31L + 30L + 31L + 9L -1L) * DateTimeConstants.MILLIS_PER_DAY;
            
    // 2002-04-05
    private long TEST_TIME1 =
            (y2002days + 31L + 28L + 31L + 5L -1L) * DateTimeConstants.MILLIS_PER_DAY
            + 12L * DateTimeConstants.MILLIS_PER_HOUR
            + 24L * DateTimeConstants.MILLIS_PER_MINUTE;
        
    // 2003-05-06
    private long TEST_TIME2 =
            (y2003days + 31L + 28L + 31L + 30L + 6L -1L) * DateTimeConstants.MILLIS_PER_DAY
            + 14L * DateTimeConstants.MILLIS_PER_HOUR
            + 28L * DateTimeConstants.MILLIS_PER_MINUTE;
    
    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestMillisDuration_Constructors.class);
    }

    public TestMillisDuration_Constructors(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor ()
     */
    public void testZERO() throws Throwable {
        MillisDuration test = MillisDuration.ZERO;
        assertEquals(DurationType.getPreciseAllType(), test.getDurationType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(0, test.getMinutes());
        assertEquals(0, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(0, test.getTotalMillis());
    }

    //-----------------------------------------------------------------------
    public void testConstructor_long1() throws Throwable {
        long length = 4 * DateTimeConstants.MILLIS_PER_DAY +
                5 * DateTimeConstants.MILLIS_PER_HOUR +
                6 * DateTimeConstants.MILLIS_PER_MINUTE +
                7 * DateTimeConstants.MILLIS_PER_SECOND + 8;
        MillisDuration test = new MillisDuration(length);
        assertEquals(DurationType.getPreciseAllType(), test.getDurationType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(4, test.getDays());
        assertEquals(5, test.getHours());
        assertEquals(6, test.getMinutes());
        assertEquals(7, test.getSeconds());
        assertEquals(8, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(length, test.getTotalMillis());
    }

    //-----------------------------------------------------------------------
    public void testConstructor_long_DurationType1() throws Throwable {
        long length = 4 * DateTimeConstants.MILLIS_PER_DAY +
                5 * DateTimeConstants.MILLIS_PER_HOUR +
                6 * DateTimeConstants.MILLIS_PER_MINUTE +
                7 * DateTimeConstants.MILLIS_PER_SECOND + 8;
        MillisDuration test = new MillisDuration(length, null);
        assertEquals(DurationType.getPreciseAllType(), test.getDurationType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(4, test.getDays());
        assertEquals(5, test.getHours());
        assertEquals(6, test.getMinutes());
        assertEquals(7, test.getSeconds());
        assertEquals(8, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(length, test.getTotalMillis());
    }

    public void testConstructor_long_DurationType2() throws Throwable {
        long length = 4 * DateTimeConstants.MILLIS_PER_DAY +
                5 * DateTimeConstants.MILLIS_PER_HOUR +
                6 * DateTimeConstants.MILLIS_PER_MINUTE +
                7 * DateTimeConstants.MILLIS_PER_SECOND + 8;
        MillisDuration test = new MillisDuration(length, DurationType.getMillisType());
        assertEquals(DurationType.getMillisType(), test.getDurationType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(0, test.getMinutes());
        assertEquals(0, test.getSeconds());
        assertEquals(length, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(length, test.getTotalMillis());
    }

    public void testConstructor_long_DurationType3() throws Throwable {
        long length = 4 * DateTimeConstants.MILLIS_PER_DAY +
                5 * DateTimeConstants.MILLIS_PER_HOUR +
                6 * DateTimeConstants.MILLIS_PER_MINUTE +
                7 * DateTimeConstants.MILLIS_PER_SECOND + 8;
        MillisDuration test = new MillisDuration(length, DurationType.getPreciseDayHourType());
        assertEquals(DurationType.getPreciseDayHourType(), test.getDurationType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(4, test.getDays());
        assertEquals(5, test.getHours());
        assertEquals(6, test.getMinutes());
        assertEquals(7, test.getSeconds());
        assertEquals(8, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(length, test.getTotalMillis());
    }

    public void testConstructor_long_DurationType4() throws Throwable {
        long length = 4 * DateTimeConstants.MILLIS_PER_DAY +
                5 * DateTimeConstants.MILLIS_PER_HOUR +
                6 * DateTimeConstants.MILLIS_PER_MINUTE +
                7 * DateTimeConstants.MILLIS_PER_SECOND + 8;
        MillisDuration test = new MillisDuration(length, DurationType.getPreciseAllType().withMillisRemoved());
        assertEquals(DurationType.getPreciseAllType().withMillisRemoved(), test.getDurationType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(4, test.getDays());
        assertEquals(5, test.getHours());
        assertEquals(6, test.getMinutes());
        assertEquals(7, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(length - 8, test.getTotalMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor (4ints)
     */
    public void testConstructor_4int1() throws Throwable {
        MillisDuration test = new MillisDuration(5, 6, 7, 8);
        assertEquals(DurationType.getPreciseAllType(), test.getDurationType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(5, test.getHours());
        assertEquals(6, test.getMinutes());
        assertEquals(7, test.getSeconds());
        assertEquals(8, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(
            5 * DateTimeConstants.MILLIS_PER_HOUR + 6 * DateTimeConstants.MILLIS_PER_MINUTE +
            7 * DateTimeConstants.MILLIS_PER_SECOND + 8, test.getTotalMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor (8ints)
     */
    public void testConstructor_8int1() throws Throwable {
        MillisDuration test = new MillisDuration(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(DurationType.getPreciseAllType(), test.getDurationType());
        assertEquals(1, test.getYears());
        assertEquals(2, test.getMonths());
        assertEquals(3, test.getWeeks());
        assertEquals(4, test.getDays());
        assertEquals(5, test.getHours());
        assertEquals(6, test.getMinutes());
        assertEquals(7, test.getSeconds());
        assertEquals(8, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(
            (365L + 2L * 30L + 3L * 7L + 4L) * DateTimeConstants.MILLIS_PER_DAY +
            5 * DateTimeConstants.MILLIS_PER_HOUR + 6 * DateTimeConstants.MILLIS_PER_MINUTE +
            7 * DateTimeConstants.MILLIS_PER_SECOND + 8, test.getTotalMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor (8ints)
     */
    public void testConstructor_8int__DurationType1() throws Throwable {
        MillisDuration test = new MillisDuration(1, 2, 3, 4, 5, 6, 7, 8, null);
        assertEquals(DurationType.getPreciseAllType(), test.getDurationType());
        assertEquals(1, test.getYears());
        assertEquals(2, test.getMonths());
        assertEquals(3, test.getWeeks());
        assertEquals(4, test.getDays());
        assertEquals(5, test.getHours());
        assertEquals(6, test.getMinutes());
        assertEquals(7, test.getSeconds());
        assertEquals(8, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(
            (365L + 2L * 30L + 3L * 7L + 4L) * DateTimeConstants.MILLIS_PER_DAY +
            5 * DateTimeConstants.MILLIS_PER_HOUR + 6 * DateTimeConstants.MILLIS_PER_MINUTE +
            7 * DateTimeConstants.MILLIS_PER_SECOND + 8, test.getTotalMillis());
    }

    public void testConstructor_8int__DurationType2() throws Throwable {
        MillisDuration test = new MillisDuration(0, 0, 0, 0, 5, 6, 7, 8, DurationType.getPreciseDayHourType());
        assertEquals(DurationType.getPreciseDayHourType(), test.getDurationType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(5, test.getHours());
        assertEquals(6, test.getMinutes());
        assertEquals(7, test.getSeconds());
        assertEquals(8, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(
            5 * DateTimeConstants.MILLIS_PER_HOUR + 6 * DateTimeConstants.MILLIS_PER_MINUTE +
            7 * DateTimeConstants.MILLIS_PER_SECOND + 8, test.getTotalMillis());
    }

    public void testConstructor_8int__DurationType3() throws Throwable {
        try {
            new MillisDuration(1, 2, 3, 4, 5, 6, 7, 8, DurationType.getDayHourType());
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testConstructor_long_long1() throws Throwable {
        DateTime dt1 = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2005, 7, 10, 1, 1, 1, 1);
        MillisDuration test = new MillisDuration(dt1.getMillis(), dt2.getMillis());
        assertEquals(DurationType.getPreciseAllType(), test.getDurationType());
        assertEquals(1, test.getYears());
        assertEquals(1, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(1, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(dt2.getMillis() - dt1.getMillis(), test.getTotalMillis());
    }

    public void testConstructor_long_long2() throws Throwable {
        DateTime dt1 = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2005, 7, 17, 1, 1, 1, 1);
        MillisDuration test = new MillisDuration(dt1.getMillis(), dt2.getMillis());
        assertEquals(DurationType.getPreciseAllType(), test.getDurationType());
        assertEquals(1, test.getYears());
        assertEquals(1, test.getMonths());
        assertEquals(1, test.getWeeks());
        assertEquals(1, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(dt2.getMillis() - dt1.getMillis(), test.getTotalMillis());
    }

    //-----------------------------------------------------------------------
    public void testConstructor_long_long_DurationType1() throws Throwable {
        DateTime dt1 = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2005, 7, 10, 1, 1, 1, 1);
        MillisDuration test = new MillisDuration(dt1.getMillis(), dt2.getMillis(), null);
        assertEquals(DurationType.getPreciseAllType(), test.getDurationType());
        assertEquals(1, test.getYears());
        assertEquals(1, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(1, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(dt2.getMillis() - dt1.getMillis(), test.getTotalMillis());
    }

    public void testConstructor_long_long_DurationType2() throws Throwable {
        DateTime dt1 = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2004, 7, 10, 1, 1, 1, 1);
        MillisDuration test = new MillisDuration(dt1.getMillis(), dt2.getMillis(), DurationType.getPreciseDayHourType());
        assertEquals(DurationType.getPreciseDayHourType(), test.getDurationType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(31, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(dt2.getMillis() - dt1.getMillis(), test.getTotalMillis());
    }

    public void testConstructor_long_long_DurationType3() throws Throwable {
        DateTime dt1 = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2005, 7, 17, 1, 1, 1, 1);
        MillisDuration test = new MillisDuration(dt1.getMillis(), dt2.getMillis(), DurationType.getPreciseAllType().withMillisRemoved());
        assertEquals(DurationType.getPreciseAllType().withMillisRemoved(), test.getDurationType());
        assertEquals(1, test.getYears());
        assertEquals(1, test.getMonths());
        assertEquals(1, test.getWeeks());
        assertEquals(1, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(dt2.getMillis() - dt1.getMillis() - 1, test.getTotalMillis());
    }

    //-----------------------------------------------------------------------
    public void testConstructor_RI_RI1() throws Throwable {
        DateTime dt1 = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2005, 7, 10, 1, 1, 1, 1);
        MillisDuration test = new MillisDuration(dt1, dt2);
        assertEquals(DurationType.getPreciseAllType(), test.getDurationType());
        assertEquals(1, test.getYears());
        assertEquals(1, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(1, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(dt2.getMillis() - dt1.getMillis(), test.getTotalMillis());
    }

    public void testConstructor_RI_RI2() throws Throwable {
        DateTime dt1 = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2005, 7, 17, 1, 1, 1, 1);
        MillisDuration test = new MillisDuration(dt1, dt2);
        assertEquals(DurationType.getPreciseAllType(), test.getDurationType());
        assertEquals(1, test.getYears());
        assertEquals(1, test.getMonths());
        assertEquals(1, test.getWeeks());
        assertEquals(1, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(dt2.getMillis() - dt1.getMillis(), test.getTotalMillis());
    }

    public void testConstructor_RI_RI3() throws Throwable {
        DateTime dt1 = null;  // 2002-06-09T01:00+01:00
        DateTime dt2 = new DateTime(2005, 7, 17, 1, 1, 1, 1);
        MillisDuration test = new MillisDuration(dt1, dt2);
        assertEquals(DurationType.getPreciseAllType(), test.getDurationType());
        assertEquals(3, test.getYears());
        assertEquals(1, test.getMonths());
        assertEquals(1, test.getWeeks());
        assertEquals(2, test.getDays());  // one more due to 2004 leap year and fixed 365 day years
        assertEquals(0, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(dt2.getMillis() - TEST_TIME_NOW, test.getTotalMillis());
    }

    public void testConstructor_RI_RI4() throws Throwable {
        DateTime dt1 = new DateTime(2005, 7, 17, 1, 1, 1, 1);
        DateTime dt2 = null;  // 2002-06-09T01:00+01:00
        MillisDuration test = new MillisDuration(dt1, dt2);
        assertEquals(DurationType.getPreciseAllType(), test.getDurationType());
        assertEquals(-3, test.getYears());
        assertEquals(-1, test.getMonths());
        assertEquals(-1, test.getWeeks());
        assertEquals(-2, test.getDays());  // one more due to 2004 leap year and fixed 365 day years
        assertEquals(0, test.getHours());
        assertEquals(-1, test.getMinutes());
        assertEquals(-1, test.getSeconds());
        assertEquals(-1, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(TEST_TIME_NOW - dt1.getMillis(), test.getTotalMillis());
    }

    public void testConstructor_RI_RI5() throws Throwable {
        DateTime dt1 = null;  // 2002-06-09T01:00+01:00
        DateTime dt2 = null;  // 2002-06-09T01:00+01:00
        MillisDuration test = new MillisDuration(dt1, dt2);
        assertEquals(DurationType.getPreciseAllType(), test.getDurationType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(0, test.getMinutes());
        assertEquals(0, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(0L, test.getTotalMillis());
    }

    //-----------------------------------------------------------------------
    public void testConstructor_RI_RI_DurationType1() throws Throwable {
        DateTime dt1 = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2005, 7, 10, 1, 1, 1, 1);
        MillisDuration test = new MillisDuration(dt1, dt2, null);
        assertEquals(DurationType.getPreciseAllType(), test.getDurationType());
        assertEquals(1, test.getYears());
        assertEquals(1, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(1, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(dt2.getMillis() - dt1.getMillis(), test.getTotalMillis());
    }

    public void testConstructor_RI_RI_DurationType2() throws Throwable {
        DateTime dt1 = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2004, 7, 10, 1, 1, 1, 1);
        MillisDuration test = new MillisDuration(dt1, dt2, DurationType.getPreciseDayHourType());
        assertEquals(DurationType.getPreciseDayHourType(), test.getDurationType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(31, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(dt2.getMillis() - dt1.getMillis(), test.getTotalMillis());
    }

    public void testConstructor_RI_RI_DurationType3() throws Throwable {
        DateTime dt1 = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2005, 7, 17, 1, 1, 1, 1);
        MillisDuration test = new MillisDuration(dt1, dt2, DurationType.getPreciseAllType().withMillisRemoved());
        assertEquals(DurationType.getPreciseAllType().withMillisRemoved(), test.getDurationType());
        assertEquals(1, test.getYears());
        assertEquals(1, test.getMonths());
        assertEquals(1, test.getWeeks());
        assertEquals(1, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(dt2.getMillis() - dt1.getMillis() - 1, test.getTotalMillis());
    }

    public void testConstructor_RI_RI_DurationType4() throws Throwable {
        DateTime dt1 = null;  // 2002-06-09T01:00+01:00
        DateTime dt2 = new DateTime(2005, 7, 17, 1, 1, 1, 1);
        MillisDuration test = new MillisDuration(dt1, dt2, DurationType.getPreciseAllType());
        assertEquals(DurationType.getPreciseAllType(), test.getDurationType());
        assertEquals(3, test.getYears());
        assertEquals(1, test.getMonths());
        assertEquals(1, test.getWeeks());
        assertEquals(2, test.getDays());  // one more due to 2004 leap year and fixed 365 day years
        assertEquals(0, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(dt2.getMillis() - TEST_TIME_NOW, test.getTotalMillis());
    }

    public void testConstructor_RI_RI_DurationType5() throws Throwable {
        DateTime dt1 = null;  // 2002-06-09T01:00+01:00
        DateTime dt2 = null;  // 2002-06-09T01:00+01:00
        MillisDuration test = new MillisDuration(dt1, dt2, DurationType.getPreciseAllType());
        assertEquals(DurationType.getPreciseAllType(), test.getDurationType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(0, test.getMinutes());
        assertEquals(0, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(0L, test.getTotalMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor (Object)
     */
    public void testConstructor_Object1() throws Throwable {
        MillisDuration test = new MillisDuration("P1Y2M3D");
        assertEquals(DurationType.getPreciseAllType(), test.getDurationType());
        assertEquals(1, test.getYears());
        assertEquals(2, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(3, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(0, test.getMinutes());
        assertEquals(0, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(
            (365L + 2L * 30L + 3L) * DateTimeConstants.MILLIS_PER_DAY, test.getTotalMillis());
    }

    public void testConstructor_Object2() throws Throwable {
        MillisDuration test = new MillisDuration((Object) null);
        assertEquals(DurationType.getPreciseAllType(), test.getDurationType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(0, test.getMinutes());
        assertEquals(0, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(0, test.getTotalMillis());
    }

    public void testConstructor_Object3() throws Throwable {
        MillisDuration base = new MillisDuration(0, 0, 0, 0, 1, 2, 3, 4, DurationType.getPreciseDayHourType());
        MillisDuration test = new MillisDuration(base);
        assertEquals(DurationType.getPreciseDayHourType(), test.getDurationType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(2, test.getMinutes());
        assertEquals(3, test.getSeconds());
        assertEquals(4, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(DateTimeConstants.MILLIS_PER_HOUR + 2 * DateTimeConstants.MILLIS_PER_MINUTE +
            3 * DateTimeConstants.MILLIS_PER_SECOND + 4, test.getTotalMillis());
    }

    public void testConstructor_Object4() throws Throwable {
        DateTime dt1 = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2005, 7, 10, 1, 1, 1, 1);
        MillisDuration base = new MillisDuration(dt1, dt2);  // AllType and precise
        MillisDuration test = new MillisDuration(base);
        assertEquals(DurationType.getPreciseAllType(), test.getDurationType());
        assertEquals(1, test.getYears());
        assertEquals(1, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(1, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(dt2.getMillis() - dt1.getMillis(), test.getTotalMillis());
    }

    public void testConstructor_Object5() throws Throwable {
        MillisDuration base = new MillisDuration(0, 0, 0, 0, 1, 2, 3, 4, DurationType.getDayHourType());
        try {
            new MillisDuration(base);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor (Object)
     */
    public void testConstructor_Object_DurationType1() throws Throwable {
        MillisDuration test = new MillisDuration("P1Y2M3D", DurationType.getPreciseYearMonthType());
        assertEquals(DurationType.getPreciseYearMonthType(), test.getDurationType());
        assertEquals(1, test.getYears());
        assertEquals(2, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(3, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(0, test.getMinutes());
        assertEquals(0, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(
            (365L + 2L * 30L + 3L) * DateTimeConstants.MILLIS_PER_DAY, test.getTotalMillis());
    }

    public void testConstructor_Object_DurationType2() throws Throwable {
        MillisDuration test = new MillisDuration((Object) null, DurationType.getPreciseYearMonthType());
        assertEquals(DurationType.getPreciseYearMonthType(), test.getDurationType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(0, test.getMinutes());
        assertEquals(0, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(0, test.getTotalMillis());
    }

    public void testConstructor_Object_DurationType3() throws Throwable {
        MillisDuration test = new MillisDuration(
            new MillisDuration(0, 0, 0, 0, 1, 2, 3, 4, DurationType.getPreciseDayHourType()), DurationType.getPreciseYearMonthType());
        assertEquals(DurationType.getPreciseYearMonthType(), test.getDurationType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(2, test.getMinutes());
        assertEquals(3, test.getSeconds());
        assertEquals(4, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(DateTimeConstants.MILLIS_PER_HOUR + 2 * DateTimeConstants.MILLIS_PER_MINUTE +
            3 * DateTimeConstants.MILLIS_PER_SECOND + 4, test.getTotalMillis());
    }

    public void testConstructor_Object_DurationType4() throws Throwable {
        MillisDuration test = new MillisDuration(new MillisDuration(0, 0, 0, 0, 1, 2, 3, 4, DurationType.getPreciseDayHourType()), null);
        assertEquals(DurationType.getPreciseDayHourType(), test.getDurationType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(2, test.getMinutes());
        assertEquals(3, test.getSeconds());
        assertEquals(4, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(DateTimeConstants.MILLIS_PER_HOUR + 2 * DateTimeConstants.MILLIS_PER_MINUTE +
            3 * DateTimeConstants.MILLIS_PER_SECOND + 4, test.getTotalMillis());
    }

}
