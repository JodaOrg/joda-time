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

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    
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
    public void testAdd_RD1() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.add(new Duration(123456L));
        assertEquals(TEST_TIME1 + 123456L, test.getMillis());
    }

    public void testAdd_RD2() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.add((ReadableDuration) null);
        assertEquals(TEST_TIME1, test.getMillis());
    }

    //-----------------------------------------------------------------------
    public void testAdd_RD_int1() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.add(new Duration(123456L), -2);
        assertEquals(TEST_TIME1 - (2L * 123456L), test.getMillis());
    }

    public void testAdd_RD_int2() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.add((ReadableDuration) null, 1);
        assertEquals(TEST_TIME1, test.getMillis());
    }

    //-----------------------------------------------------------------------
    public void testAdd_RP1() {
        Period d = new Period(1, 1, 0, 1, 1, 1, 1, 1);
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        assertEquals("2002-06-09T05:06:07.008+01:00", test.toString());
        test.add(d);
        assertEquals("2003-07-10T06:07:08.009+01:00", test.toString());
    }

    public void testAdd_RP2() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.add((ReadablePeriod) null);
        assertEquals(TEST_TIME1, test.getMillis());
    }

    //-----------------------------------------------------------------------
    public void testAdd_RP_int1() {
        Period d = new Period(0, 0, 0, 0, 0, 0, 1, 2);
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.add(d, -2);
        assertEquals(TEST_TIME1 - (2L * 1002L), test.getMillis());
    }

    public void testAdd_RP_int2() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.add((ReadablePeriod) null, 1);
        assertEquals(TEST_TIME1, test.getMillis());
    }

    //-----------------------------------------------------------------------
    public void testAdd_DurationFieldType_int1() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.add(DurationFieldType.years(), 8);
        assertEquals(2010, test.getYear());
    }

    public void testAdd_DurationFieldType_int2() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        try {
            test.add((DurationFieldType) null, 0);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals(TEST_TIME1, test.getMillis());
    }

    public void testAdd_DurationFieldType_int3() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        try {
            test.add((DurationFieldType) null, 6);
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
