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
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.chrono.LenientChronology;
import org.joda.time.chrono.LimitChronology;
import org.joda.time.chrono.StrictChronology;
import org.joda.time.chrono.ZonedChronology;

/**
 * This class is a Junit unit test for Chronology.
 *
 * @author Stephen Colebourne
 */
public class TestChronology extends TestCase {
    // Test in 2002/03 as time zones are more well known
    // (before the late 90's they were all over the place)

//    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
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
    public void testEqualsHashCode_ISO() {
        Chronology chrono1 = ISOChronology.getInstanceUTC();
        Chronology chrono2 = ISOChronology.getInstanceUTC();
        Chronology chrono3 = ISOChronology.getInstance();
        
        assertEquals(true, chrono1.equals(chrono2));
        assertEquals(false, chrono1.equals(chrono3));
        
        DateTime dt1 = new DateTime(0L, chrono1);
        DateTime dt2 = new DateTime(0L, chrono2);
        DateTime dt3 = new DateTime(0L, chrono3);
        
        assertEquals(true, dt1.equals(dt2));
        assertEquals(false, dt1.equals(dt3));
        
        assertEquals(true, chrono1.hashCode() == chrono2.hashCode());
        assertEquals(false, chrono1.hashCode() == chrono3.hashCode());
    }

    //-----------------------------------------------------------------------
    public void testEqualsHashCode_Lenient() {
        Chronology chrono1 = LenientChronology.getInstance(ISOChronology.getInstanceUTC());
        Chronology chrono2 = LenientChronology.getInstance(ISOChronology.getInstanceUTC());
        Chronology chrono3 = LenientChronology.getInstance(ISOChronology.getInstance());
        
        assertEquals(true, chrono1.equals(chrono2));
        assertEquals(false, chrono1.equals(chrono3));
        
        DateTime dt1 = new DateTime(0L, chrono1);
        DateTime dt2 = new DateTime(0L, chrono2);
        DateTime dt3 = new DateTime(0L, chrono3);
        
        assertEquals(true, dt1.equals(dt2));
        assertEquals(false, dt1.equals(dt3));
        
        assertEquals(true, chrono1.hashCode() == chrono2.hashCode());
        assertEquals(false, chrono1.hashCode() == chrono3.hashCode());
    }

    //-----------------------------------------------------------------------
    public void testEqualsHashCode_Strict() {
        Chronology chrono1 = StrictChronology.getInstance(ISOChronology.getInstanceUTC());
        Chronology chrono2 = StrictChronology.getInstance(ISOChronology.getInstanceUTC());
        Chronology chrono3 = StrictChronology.getInstance(ISOChronology.getInstance());
        
        assertEquals(true, chrono1.equals(chrono2));
        assertEquals(false, chrono1.equals(chrono3));
        
        DateTime dt1 = new DateTime(0L, chrono1);
        DateTime dt2 = new DateTime(0L, chrono2);
        DateTime dt3 = new DateTime(0L, chrono3);
        
        assertEquals(true, dt1.equals(dt2));
        assertEquals(false, dt1.equals(dt3));
        
        assertEquals(true, chrono1.hashCode() == chrono2.hashCode());
        assertEquals(false, chrono1.hashCode() == chrono3.hashCode());
    }

    //-----------------------------------------------------------------------
    public void testEqualsHashCode_Limit() {
        DateTime lower = new DateTime(0L);
        DateTime higherA = new DateTime(1000000L);
        DateTime higherB = new DateTime(2000000L);
        
        Chronology chrono1 = LimitChronology.getInstance(ISOChronology.getInstanceUTC(), lower, higherA);
        Chronology chrono2A = LimitChronology.getInstance(ISOChronology.getInstanceUTC(), lower, higherA);
        Chronology chrono2B = LimitChronology.getInstance(ISOChronology.getInstanceUTC(), lower, higherB);
        Chronology chrono3 = LimitChronology.getInstance(ISOChronology.getInstance(), lower, higherA);
        
        assertEquals(true, chrono1.equals(chrono2A));
        assertEquals(false, chrono1.equals(chrono2B));
        assertEquals(false, chrono1.equals(chrono3));
        
        DateTime dt1 = new DateTime(0L, chrono1);
        DateTime dt2A = new DateTime(0L, chrono2A);
        DateTime dt2B = new DateTime(0L, chrono2B);
        DateTime dt3 = new DateTime(0L, chrono3);
        
        assertEquals(true, dt1.equals(dt2A));
        assertEquals(false, dt1.equals(dt2B));
        assertEquals(false, dt1.equals(dt3));
        
        assertEquals(true, chrono1.hashCode() == chrono2A.hashCode());
        assertEquals(false, chrono1.hashCode() == chrono2B.hashCode());
        assertEquals(false, chrono1.hashCode() == chrono3.hashCode());
    }

    //-----------------------------------------------------------------------
    public void testEqualsHashCode_Zoned() {
        DateTimeZone zoneA = DateTimeZone.forID("Europe/Paris");
        DateTimeZone zoneB = DateTimeZone.forID("Asia/Tokyo");
        
        Chronology chrono1 = ZonedChronology.getInstance(ISOChronology.getInstanceUTC(), zoneA);
        Chronology chrono2 = ZonedChronology.getInstance(ISOChronology.getInstanceUTC(), zoneA);
        Chronology chrono3 = ZonedChronology.getInstance(ISOChronology.getInstanceUTC(), zoneB);
        
        assertEquals(true, chrono1.equals(chrono2));
        assertEquals(false, chrono1.equals(chrono3));
        
        DateTime dt1 = new DateTime(0L, chrono1);
        DateTime dt2 = new DateTime(0L, chrono2);
        DateTime dt3 = new DateTime(0L, chrono3);
        
        assertEquals(true, dt1.equals(dt2));
        assertEquals(false, dt1.equals(dt3));
        
        assertEquals(true, chrono1.hashCode() == chrono2.hashCode());
        assertEquals(false, chrono1.hashCode() == chrono3.hashCode());
    }

    //-----------------------------------------------------------------------
    public void testToString() {
        DateTimeZone paris = DateTimeZone.forID("Europe/Paris");
        ISOChronology isoParis = ISOChronology.getInstance(paris);
        
        assertEquals("ISOChronology[Europe/Paris]", isoParis.toString());
        assertEquals("GJChronology[Europe/Paris]", GJChronology.getInstance(paris).toString());
        assertEquals("GregorianChronology[Europe/Paris]", GregorianChronology.getInstance(paris).toString());
        assertEquals("JulianChronology[Europe/Paris]", JulianChronology.getInstance(paris).toString());
        assertEquals("BuddhistChronology[Europe/Paris]", BuddhistChronology.getInstance(paris).toString());
        assertEquals("CopticChronology[Europe/Paris]", CopticChronology.getInstance(paris).toString());
        assertEquals("EthiopicChronology[Europe/Paris]", EthiopicChronology.getInstance(paris).toString());
        assertEquals("IslamicChronology[Europe/Paris]", IslamicChronology.getInstance(paris).toString());
        
        assertEquals("LenientChronology[ISOChronology[Europe/Paris]]", LenientChronology.getInstance(isoParis).toString());
        assertEquals("StrictChronology[ISOChronology[Europe/Paris]]", StrictChronology.getInstance(isoParis).toString());
        assertEquals("LimitChronology[ISOChronology[Europe/Paris], NoLimit, NoLimit]", LimitChronology.getInstance(isoParis, null, null).toString());
        assertEquals("ZonedChronology[ISOChronology[UTC], Europe/Paris]", ZonedChronology.getInstance(isoParis, paris).toString());
    }

}
