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

import org.joda.time.chrono.ISOChronology;

/**
 * This class is a JUnit test for MutableDateTime.
 *
 * @author Stephen Colebourne
 */
public class TestMutableDateTime_Adds extends TestCase {
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
        return new TestSuite(TestMutableDateTime_Adds.class);
    }

    public TestMutableDateTime_Adds(String name) {
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
    public void testTest() {
        assertEquals("2002-06-09T00:00:00.000Z", new Instant(TEST_TIME_NOW).toString());
        assertEquals("2002-04-05T12:24:00.000Z", new Instant(TEST_TIME1).toString());
        assertEquals("2003-05-06T14:28:00.000Z", new Instant(TEST_TIME2).toString());
    }

    //-----------------------------------------------------------------------
    public void testAdd_long1() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.add(123456L);
        assertEquals(TEST_TIME1 + 123456L, test.getMillis());
        assertEquals(ISOChronology.getInstance(), test.getChronology());
    }

    //-----------------------------------------------------------------------
    public void testAdd_Object1() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.add(new Long(123456L));
        assertEquals(TEST_TIME1 + 123456L, test.getMillis());
    }

    public void testAdd_Object2() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.add((Object) null);
        assertEquals(TEST_TIME1, test.getMillis());
    }

    public void testAdd_Object3() {
        Duration d = new Duration(DurationType.getYearMonthType(), 1, 1, 0, 1, 1, 1, 1, 1);
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        assertEquals("2002-06-09T05:06:07.008+01:00", test.toString());
        test.add(d);
        assertEquals("2003-07-10T06:07:08.009+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testAdd_Object_int1() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.add(new Long(123L), -2);
        assertEquals(TEST_TIME1 + (-2 * 123L), test.getMillis());
    }

    public void testAdd_Object_int2() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.add((ReadableDuration) null, 1);
        assertEquals(TEST_TIME1, test.getMillis());
        assertEquals(ISOChronology.getInstance(), test.getChronology());
    }

    public void testAdd_Object_int3() {
        Duration d = new Duration(DurationType.getYearMonthType(), 1, 1, 0, 1, 1, 1, 1, 1);
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        assertEquals("2002-06-09T05:06:07.008+01:00", test.toString());
        test.add(d, -2);
        assertEquals("2000-04-07T03:04:05.006+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testAdd_DateTimeField_int1() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.add(ISOChronology.getInstance().year(), 8);
        assertEquals(2010, test.getYear());
    }

    public void testAdd_DateTimeField_int2() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        try {
            test.add((DateTimeField) null, 2010);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals(TEST_TIME1, test.getMillis());
    }

    //-----------------------------------------------------------------------
    public void testAdd_DurationField_int1() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.add(ISOChronology.getInstance().years(), 8);
        assertEquals(2010, test.getYear());
    }

    public void testAdd_DurationField_int2() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        try {
            test.add((DurationField) null, 2010);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals(TEST_TIME1, test.getMillis());
    }

    //-----------------------------------------------------------------------
    public void testAddYears_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.addYears(8);
        assertEquals("2010-06-09T05:06:07.008+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testAddMonths_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.addMonths(6);
        assertEquals("2002-12-09T05:06:07.008Z", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testAddDays_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.addDays(17);
        assertEquals("2002-06-26T05:06:07.008+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testAddWeekyears_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.addWeekyears(-1);
        assertEquals("2001-06-10T05:06:07.008+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testAddWeeks_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.addWeeks(-21);
        assertEquals("2002-01-13T05:06:07.008Z", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testAddHours_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.addHours(13);
        assertEquals("2002-06-09T18:06:07.008+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testAddMinutes_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.addMinutes(13);
        assertEquals("2002-06-09T05:19:07.008+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testAddSeconds_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.addSeconds(13);
        assertEquals("2002-06-09T05:06:20.008+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testAddMillis_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.addMillis(13);
        assertEquals("2002-06-09T05:06:07.021+01:00", test.toString());
    }

}
