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
 * This class is a JUnit test for MutableDuration.
 *
 * @author Stephen Colebourne
 */
public class TestMutablePeriod_Constructors extends TestCase {
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
        return new TestSuite(TestMutablePeriod_Constructors.class);
    }

    public TestMutablePeriod_Constructors(String name) {
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
    public void testConstructor1() throws Throwable {
        MutableTimePeriod test = new MutableTimePeriod();
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(0, test.getMinutes());
        assertEquals(0, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(0, test.toDurationMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor (PeriodType)
     */
    public void testConstructor_PeriodType1() throws Throwable {
        MutableTimePeriod test = new MutableTimePeriod(PeriodType.getYearMonthType());
        assertEquals(PeriodType.getYearMonthType(), test.getPeriodType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(0, test.getMinutes());
        assertEquals(0, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(0, test.toDurationMillis());
    }

    public void testConstructor_PeriodType2() throws Throwable {
        MutableTimePeriod test = new MutableTimePeriod((PeriodType) null);
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(0, test.getMinutes());
        assertEquals(0, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(0, test.toDurationMillis());
    }

    //-----------------------------------------------------------------------
    public void testConstructor_long1() throws Throwable {
        long length = 4 * DateTimeConstants.MILLIS_PER_DAY +
                5 * DateTimeConstants.MILLIS_PER_HOUR +
                6 * DateTimeConstants.MILLIS_PER_MINUTE +
                7 * DateTimeConstants.MILLIS_PER_SECOND + 8;
        MutableTimePeriod test = new MutableTimePeriod(length);
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(4, test.getDays());
        assertEquals(5, test.getHours());
        assertEquals(6, test.getMinutes());
        assertEquals(7, test.getSeconds());
        assertEquals(8, test.getMillis());
        assertEquals(false, test.isPrecise());
        try {
            test.toDurationMillis();
            fail();
        } catch (IllegalStateException ex) {}
    }

    public void testConstructor_long2() throws Throwable {
        long length =
                5 * DateTimeConstants.MILLIS_PER_HOUR +
                6 * DateTimeConstants.MILLIS_PER_MINUTE +
                7 * DateTimeConstants.MILLIS_PER_SECOND + 8;
        MutableTimePeriod test = new MutableTimePeriod(length);
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(5, test.getHours());
        assertEquals(6, test.getMinutes());
        assertEquals(7, test.getSeconds());
        assertEquals(8, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(length, test.toDurationMillis());
    }

    //-----------------------------------------------------------------------
    public void testConstructor_long_PeriodType1() throws Throwable {
        long length = 4 * DateTimeConstants.MILLIS_PER_DAY +
                5 * DateTimeConstants.MILLIS_PER_HOUR +
                6 * DateTimeConstants.MILLIS_PER_MINUTE +
                7 * DateTimeConstants.MILLIS_PER_SECOND + 8;
        MutableTimePeriod test = new MutableTimePeriod(length, null);
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(4, test.getDays());
        assertEquals(5, test.getHours());
        assertEquals(6, test.getMinutes());
        assertEquals(7, test.getSeconds());
        assertEquals(8, test.getMillis());
        assertEquals(false, test.isPrecise());
        try {
            test.toDurationMillis();
            fail();
        } catch (IllegalStateException ex) {}
    }

    public void testConstructor_long_PeriodType2() throws Throwable {
        long length = 4 * DateTimeConstants.MILLIS_PER_DAY +
                5 * DateTimeConstants.MILLIS_PER_HOUR +
                6 * DateTimeConstants.MILLIS_PER_MINUTE +
                7 * DateTimeConstants.MILLIS_PER_SECOND + 8;
        MutableTimePeriod test = new MutableTimePeriod(length, PeriodType.getMillisType());
        assertEquals(PeriodType.getMillisType(), test.getPeriodType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(0, test.getMinutes());
        assertEquals(0, test.getSeconds());
        assertEquals(length, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(length, test.toDurationMillis());
    }

    public void testConstructor_long_PeriodType3() throws Throwable {
        long length = 4 * DateTimeConstants.MILLIS_PER_DAY +
                5 * DateTimeConstants.MILLIS_PER_HOUR +
                6 * DateTimeConstants.MILLIS_PER_MINUTE +
                7 * DateTimeConstants.MILLIS_PER_SECOND + 8;
        MutableTimePeriod test = new MutableTimePeriod(length, PeriodType.getAllType());
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(4, test.getDays());
        assertEquals(5, test.getHours());
        assertEquals(6, test.getMinutes());
        assertEquals(7, test.getSeconds());
        assertEquals(8, test.getMillis());
        assertEquals(false, test.isPrecise());
        try {
            test.toDurationMillis();
            fail();
        } catch (IllegalStateException ex) {}
    }

    public void testConstructor_long_PeriodType4() throws Throwable {
        long length =
                5 * DateTimeConstants.MILLIS_PER_HOUR +
                6 * DateTimeConstants.MILLIS_PER_MINUTE +
                7 * DateTimeConstants.MILLIS_PER_SECOND + 8;
        MutableTimePeriod test = new MutableTimePeriod(length, PeriodType.getAllType().withMillisRemoved());
        assertEquals(PeriodType.getAllType().withMillisRemoved(), test.getPeriodType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(5, test.getHours());
        assertEquals(6, test.getMinutes());
        assertEquals(7, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(length - 8, test.toDurationMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor (4ints)
     */
    public void testConstructor_4int1() throws Throwable {
        MutableTimePeriod test = new MutableTimePeriod(5, 6, 7, 8);
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
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
            7 * DateTimeConstants.MILLIS_PER_SECOND + 8, test.toDurationMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor (8ints)
     */
    public void testConstructor_8int1() throws Throwable {
        MutableTimePeriod test = new MutableTimePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(1, test.getYears());
        assertEquals(2, test.getMonths());
        assertEquals(3, test.getWeeks());
        assertEquals(4, test.getDays());
        assertEquals(5, test.getHours());
        assertEquals(6, test.getMinutes());
        assertEquals(7, test.getSeconds());
        assertEquals(8, test.getMillis());
        assertEquals(false, test.isPrecise());
        try {
            test.toDurationMillis();
            fail();
        } catch (IllegalStateException ex) {}
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor (8ints)
     */
    public void testConstructor_8int__PeriodType1() throws Throwable {
        MutableTimePeriod test = new MutableTimePeriod(1, 2, 3, 4, 5, 6, 7, 8, null);
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(1, test.getYears());
        assertEquals(2, test.getMonths());
        assertEquals(3, test.getWeeks());
        assertEquals(4, test.getDays());
        assertEquals(5, test.getHours());
        assertEquals(6, test.getMinutes());
        assertEquals(7, test.getSeconds());
        assertEquals(8, test.getMillis());
        assertEquals(false, test.isPrecise());
        try {
            test.toDurationMillis();
            fail();
        } catch (IllegalStateException ex) {}
    }

    public void testConstructor_8int__PeriodType2() throws Throwable {
        MutableTimePeriod test = new MutableTimePeriod(0, 0, 0, 0, 5, 6, 7, 8, PeriodType.getDayHourType());
        assertEquals(PeriodType.getDayHourType(), test.getPeriodType());
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
            7 * DateTimeConstants.MILLIS_PER_SECOND + 8, test.toDurationMillis());
    }

    public void testConstructor_8int__PeriodType3() throws Throwable {
        try {
            new MutableTimePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.getDayHourType());
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testConstructor_long_long1() throws Throwable {
        DateTime dt1 = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2005, 7, 10, 1, 1, 1, 1);
        MutableTimePeriod test = new MutableTimePeriod(dt1.getMillis(), dt2.getMillis());
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(1, test.getYears());
        assertEquals(1, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(1, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(false, test.isPrecise());
        try {
            test.toDurationMillis();
            fail();
        } catch (IllegalStateException ex) {}
    }

    public void testConstructor_long_long2() throws Throwable {
        DateTime dt1 = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2005, 7, 17, 1, 1, 1, 1);
        MutableTimePeriod test = new MutableTimePeriod(dt1.getMillis(), dt2.getMillis());
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(1, test.getYears());
        assertEquals(1, test.getMonths());
        assertEquals(1, test.getWeeks());
        assertEquals(1, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(false, test.isPrecise());
        try {
            test.toDurationMillis();
            fail();
        } catch (IllegalStateException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testConstructor_long_long_PeriodType1() throws Throwable {
        DateTime dt1 = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2005, 7, 10, 1, 1, 1, 1);
        MutableTimePeriod test = new MutableTimePeriod(dt1.getMillis(), dt2.getMillis(), null);
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(1, test.getYears());
        assertEquals(1, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(1, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(false, test.isPrecise());
        try {
            test.toDurationMillis();
            fail();
        } catch (IllegalStateException ex) {}
    }

    public void testConstructor_long_long_PeriodType2() throws Throwable {
        DateTime dt1 = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2004, 7, 10, 1, 1, 1, 1);
        MutableTimePeriod test = new MutableTimePeriod(dt1.getMillis(), dt2.getMillis(), PeriodType.getDayHourType());
        assertEquals(PeriodType.getDayHourType(), test.getPeriodType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(31, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(false, test.isPrecise());
        try {
            test.toDurationMillis();
            fail();
        } catch (IllegalStateException ex) {}
    }

    public void testConstructor_long_long_PeriodType3() throws Throwable {
        DateTime dt1 = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2004, 6, 9, 1, 1, 1, 1);
        MutableTimePeriod test = new MutableTimePeriod(dt1.getMillis(), dt2.getMillis(), PeriodType.getAllType().withMillisRemoved());
        assertEquals(PeriodType.getAllType().withMillisRemoved(), test.getPeriodType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(dt2.getMillis() - dt1.getMillis() - 1, test.toDurationMillis());
    }

    //-----------------------------------------------------------------------
    public void testConstructor_RI_RI1() throws Throwable {
        DateTime dt1 = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2005, 7, 10, 1, 1, 1, 1);
        MutableTimePeriod test = new MutableTimePeriod(dt1, dt2);
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(1, test.getYears());
        assertEquals(1, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(1, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(false, test.isPrecise());
        try {
            test.toDurationMillis();
            fail();
        } catch (IllegalStateException ex) {}
    }

    public void testConstructor_RI_RI2() throws Throwable {
        DateTime dt1 = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2005, 7, 17, 1, 1, 1, 1);
        MutableTimePeriod test = new MutableTimePeriod(dt1, dt2);
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(1, test.getYears());
        assertEquals(1, test.getMonths());
        assertEquals(1, test.getWeeks());
        assertEquals(1, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(false, test.isPrecise());
        try {
            test.toDurationMillis();
            fail();
        } catch (IllegalStateException ex) {}
    }

    public void testConstructor_RI_RI3() throws Throwable {
        DateTime dt1 = null;  // 2002-06-09T01:00+01:00
        DateTime dt2 = new DateTime(2005, 7, 17, 1, 1, 1, 1);
        MutableTimePeriod test = new MutableTimePeriod(dt1, dt2);
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(3, test.getYears());
        assertEquals(1, test.getMonths());
        assertEquals(1, test.getWeeks());
        assertEquals(1, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(false, test.isPrecise());
        try {
            test.toDurationMillis();
            fail();
        } catch (IllegalStateException ex) {}
    }

    public void testConstructor_RI_RI4() throws Throwable {
        DateTime dt1 = new DateTime(2005, 7, 17, 1, 1, 1, 1);
        DateTime dt2 = null;  // 2002-06-09T01:00+01:00
        MutableTimePeriod test = new MutableTimePeriod(dt1, dt2);
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(-3, test.getYears());
        assertEquals(-1, test.getMonths());
        assertEquals(-1, test.getWeeks());
        assertEquals(-1, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(-1, test.getMinutes());
        assertEquals(-1, test.getSeconds());
        assertEquals(-1, test.getMillis());
        assertEquals(false, test.isPrecise());
        try {
            test.toDurationMillis();
            fail();
        } catch (IllegalStateException ex) {}
    }

    public void testConstructor_RI_RI5() throws Throwable {
        DateTime dt1 = null;  // 2002-06-09T01:00+01:00
        DateTime dt2 = null;  // 2002-06-09T01:00+01:00
        MutableTimePeriod test = new MutableTimePeriod(dt1, dt2);
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(0, test.getMinutes());
        assertEquals(0, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(0L, test.toDurationMillis());
    }

    //-----------------------------------------------------------------------
    public void testConstructor_RI_RI_PeriodType1() throws Throwable {
        DateTime dt1 = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2005, 7, 10, 1, 1, 1, 1);
        MutableTimePeriod test = new MutableTimePeriod(dt1, dt2, null);
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(1, test.getYears());
        assertEquals(1, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(1, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(false, test.isPrecise());
        try {
            test.toDurationMillis();
            fail();
        } catch (IllegalStateException ex) {}
    }

    public void testConstructor_RI_RI_PeriodType2() throws Throwable {
        DateTime dt1 = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2004, 7, 10, 1, 1, 1, 1);
        MutableTimePeriod test = new MutableTimePeriod(dt1, dt2, PeriodType.getDayHourType());
        assertEquals(PeriodType.getDayHourType(), test.getPeriodType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(31, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(false, test.isPrecise());
        try {
            test.toDurationMillis();
            fail();
        } catch (IllegalStateException ex) {}
    }

    public void testConstructor_RI_RI_PeriodType3() throws Throwable {
        DateTime dt1 = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2004, 6, 9, 1, 1, 1, 1);
        MutableTimePeriod test = new MutableTimePeriod(dt1, dt2, PeriodType.getAllType().withMillisRemoved());
        assertEquals(PeriodType.getAllType().withMillisRemoved(), test.getPeriodType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(dt2.getMillis() - dt1.getMillis() - 1, test.toDurationMillis());
    }

    public void testConstructor_RI_RI_PeriodType4() throws Throwable {
        DateTime dt1 = null;  // 2002-06-09T01:00+01:00
        DateTime dt2 = new DateTime(2005, 7, 17, 1, 1, 1, 1);
        MutableTimePeriod test = new MutableTimePeriod(dt1, dt2, PeriodType.getAllType());
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(3, test.getYears());
        assertEquals(1, test.getMonths());
        assertEquals(1, test.getWeeks());
        assertEquals(1, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(false, test.isPrecise());
        try {
            test.toDurationMillis();
            fail();
        } catch (IllegalStateException ex) {}
    }

    public void testConstructor_RI_RI_PeriodType5() throws Throwable {
        DateTime dt1 = null;  // 2002-06-09T01:00+01:00
        DateTime dt2 = null;  // 2002-06-09T01:00+01:00
        MutableTimePeriod test = new MutableTimePeriod(dt1, dt2, PeriodType.getAllType());
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(0, test.getMinutes());
        assertEquals(0, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(0L, test.toDurationMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor (Object)
     */
    public void testConstructor_Object1() throws Throwable {
        MutableTimePeriod test = new MutableTimePeriod("P1Y2M3D");
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(1, test.getYears());
        assertEquals(2, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(3, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(0, test.getMinutes());
        assertEquals(0, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(false, test.isPrecise());
        try {
            test.toDurationMillis();
            fail();
        } catch (IllegalStateException ex) {}
    }

    public void testConstructor_Object2() throws Throwable {
        MutableTimePeriod test = new MutableTimePeriod((Object) null);
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(0, test.getMinutes());
        assertEquals(0, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(0, test.toDurationMillis());
    }

    public void testConstructor_Object3() throws Throwable {
        MutableTimePeriod test = new MutableTimePeriod(new TimePeriod(0, 0, 0, 0, 1, 2, 3, 4, PeriodType.getDayHourType()));
        assertEquals(PeriodType.getDayHourType(), test.getPeriodType());
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
            3 * DateTimeConstants.MILLIS_PER_SECOND + 4, test.toDurationMillis());
    }

    public void testConstructor_Object4() throws Throwable {
        TimePeriod base = new TimePeriod(1, 1, 0, 1, 1, 1, 1, 1, PeriodType.getAllType());
        MutableTimePeriod test = new MutableTimePeriod(base);
        assertEquals(PeriodType.getAllType(), test.getPeriodType());
        assertEquals(1, test.getYears());
        assertEquals(1, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(1, test.getDays());
        assertEquals(1, test.getHours());
        assertEquals(1, test.getMinutes());
        assertEquals(1, test.getSeconds());
        assertEquals(1, test.getMillis());
        assertEquals(false, test.isPrecise());
        try {
            test.toDurationMillis();
            fail();
        } catch (IllegalStateException ex) {}
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor (Object,PeriodType)
     */
    public void testConstructor_Object_PeriodType1() throws Throwable {
        MutableTimePeriod test = new MutableTimePeriod("P1Y2M3D", PeriodType.getYearMonthType());
        assertEquals(PeriodType.getYearMonthType(), test.getPeriodType());
        assertEquals(1, test.getYears());
        assertEquals(2, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(3, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(0, test.getMinutes());
        assertEquals(0, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(false, test.isPrecise());
        try {
            test.toDurationMillis();
            fail();
        } catch (IllegalStateException ex) {}
    }

    public void testConstructor_Object_PeriodType2() throws Throwable {
        MutableTimePeriod test = new MutableTimePeriod((Object) null, PeriodType.getYearMonthType());
        assertEquals(PeriodType.getYearMonthType(), test.getPeriodType());
        assertEquals(0, test.getYears());
        assertEquals(0, test.getMonths());
        assertEquals(0, test.getWeeks());
        assertEquals(0, test.getDays());
        assertEquals(0, test.getHours());
        assertEquals(0, test.getMinutes());
        assertEquals(0, test.getSeconds());
        assertEquals(0, test.getMillis());
        assertEquals(true, test.isPrecise());
        assertEquals(0, test.toDurationMillis());
    }

    public void testConstructor_Object_PeriodType3() throws Throwable {
        MutableTimePeriod test = new MutableTimePeriod(new TimePeriod(0, 0, 0, 0, 1, 2, 3, 4, PeriodType.getDayHourType()), PeriodType.getYearMonthType());
        assertEquals(PeriodType.getYearMonthType(), test.getPeriodType());
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
            3 * DateTimeConstants.MILLIS_PER_SECOND + 4, test.toDurationMillis());
    }

    public void testConstructor_Object_PeriodType4() throws Throwable {
        MutableTimePeriod test = new MutableTimePeriod(new TimePeriod(0, 0, 0, 0, 1, 2, 3, 4, PeriodType.getDayHourType()), null);
        assertEquals(PeriodType.getDayHourType(), test.getPeriodType());
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
            3 * DateTimeConstants.MILLIS_PER_SECOND + 4, test.toDurationMillis());
    }

}
