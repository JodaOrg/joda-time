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

import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;

/**
 * This class is a Junit unit test for Chronology.
 *
 * @author Stephen Colebourne
 */
public class TestChronology extends TestCase {
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
        return new TestSuite(TestChronology.class);
    }

    public TestChronology(String name) {
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
    public void testGetISO() {
        assertEquals(ISOChronology.getInstance(), Chronology.getISO());
    }

    public void testGetISOUTC() {
        assertEquals(ISOChronology.getInstanceUTC(), Chronology.getISOUTC());
    }

    public void testGetISO_Zone() {
        assertEquals(ISOChronology.getInstance(PARIS), Chronology.getISO(PARIS));
        assertEquals(ISOChronology.getInstance(), Chronology.getISO(null));
    }

    //-----------------------------------------------------------------------
    public void testGetGJ() {
        assertEquals(GJChronology.getInstance(), Chronology.getGJ());
    }

    public void testGetGJUTC() {
        assertEquals(GJChronology.getInstanceUTC(), Chronology.getGJUTC());
    }

    public void testGetGJ_Zone() {
        assertEquals(GJChronology.getInstance(PARIS), Chronology.getGJ(PARIS));
        assertEquals(GJChronology.getInstance(), Chronology.getGJ(null));
    }

    //-----------------------------------------------------------------------
    public void testGetGregorian() {
        assertEquals(GregorianChronology.getInstance(), Chronology.getGregorian());
    }

    public void testGetGregorianUTC() {
        assertEquals(GregorianChronology.getInstanceUTC(), Chronology.getGregorianUTC());
    }

    public void testGetGregorian_Zone() {
        assertEquals(GregorianChronology.getInstance(PARIS), Chronology.getGregorian(PARIS));
        assertEquals(GregorianChronology.getInstance(), Chronology.getGregorian(null));
    }

    //-----------------------------------------------------------------------
    public void testGetJulian() {
        assertEquals(JulianChronology.getInstance(), Chronology.getJulian());
    }

    public void testGetJulianUTC() {
        assertEquals(JulianChronology.getInstanceUTC(), Chronology.getJulianUTC());
    }

    public void testGetJulian_Zone() {
        assertEquals(JulianChronology.getInstance(PARIS), Chronology.getJulian(PARIS));
        assertEquals(JulianChronology.getInstance(), Chronology.getJulian(null));
    }

    //-----------------------------------------------------------------------
    public void testGetBuddhist() {
        assertEquals(BuddhistChronology.getInstance(), Chronology.getBuddhist());
    }

    public void testGetBuddhistUTC() {
        assertEquals(BuddhistChronology.getInstanceUTC(), Chronology.getBuddhistUTC());
    }

    public void testGetBuddhist_Zone() {
        assertEquals(BuddhistChronology.getInstance(PARIS), Chronology.getBuddhist(PARIS));
        assertEquals(BuddhistChronology.getInstance(), Chronology.getBuddhist(null));
    }

    //-----------------------------------------------------------------------
    public void testGetCoptic() {
        assertEquals(CopticChronology.getInstance(), Chronology.getCoptic());
    }

    public void testGetCopticUTC() {
        assertEquals(CopticChronology.getInstanceUTC(), Chronology.getCopticUTC());
    }

    public void testGetCoptic_Zone() {
        assertEquals(CopticChronology.getInstance(PARIS), Chronology.getCoptic(PARIS));
        assertEquals(CopticChronology.getInstance(), Chronology.getCoptic(null));
    }

}
