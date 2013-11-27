/*
 *  Copyright 2001-2013 Stephen Colebourne
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
package org.joda.time.chrono;

import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;

/**
 * This class is a Junit unit test for BuddhistChronology.
 *
 * @author Stephen Colebourne
 */
public class TestBuddhistChronology extends TestCase {

    private static int SKIP = 1 * DateTimeConstants.MILLIS_PER_DAY;
    
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
    private static final Chronology BUDDHIST_UTC = BuddhistChronology.getInstanceUTC();
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();
    private static final Chronology GJ_UTC = GJChronology.getInstanceUTC();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                     366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                     365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                     366 + 365;
    // 2002-06-09
    private long TEST_TIME_NOW =
            (y2002days + 31L + 28L + 31L + 30L + 31L + 9L -1L) * DateTimeConstants.MILLIS_PER_DAY;

    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    public static void main(String[] args) {
        SKIP = 1 * DateTimeConstants.MILLIS_PER_DAY;
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestBuddhistChronology.class);
    }

    public TestBuddhistChronology(String name) {
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
    public void testFactoryUTC() {
        assertEquals(DateTimeZone.UTC, BuddhistChronology.getInstanceUTC().getZone());
        assertSame(BuddhistChronology.class, BuddhistChronology.getInstanceUTC().getClass());
    }

    public void testFactory() {
        assertEquals(LONDON, BuddhistChronology.getInstance().getZone());
        assertSame(BuddhistChronology.class, BuddhistChronology.getInstance().getClass());
    }

    public void testFactory_Zone() {
        assertEquals(TOKYO, BuddhistChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, BuddhistChronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, BuddhistChronology.getInstance(null).getZone());
        assertSame(BuddhistChronology.class, BuddhistChronology.getInstance(TOKYO).getClass());
    }

    //-----------------------------------------------------------------------
    public void testEquality() {
        assertSame(BuddhistChronology.getInstance(TOKYO), BuddhistChronology.getInstance(TOKYO));
        assertSame(BuddhistChronology.getInstance(LONDON), BuddhistChronology.getInstance(LONDON));
        assertSame(BuddhistChronology.getInstance(PARIS), BuddhistChronology.getInstance(PARIS));
        assertSame(BuddhistChronology.getInstanceUTC(), BuddhistChronology.getInstanceUTC());
        assertSame(BuddhistChronology.getInstance(), BuddhistChronology.getInstance(LONDON));
    }

    public void testWithUTC() {
        assertSame(BuddhistChronology.getInstanceUTC(), BuddhistChronology.getInstance(LONDON).withUTC());
        assertSame(BuddhistChronology.getInstanceUTC(), BuddhistChronology.getInstance(TOKYO).withUTC());
        assertSame(BuddhistChronology.getInstanceUTC(), BuddhistChronology.getInstanceUTC().withUTC());
        assertSame(BuddhistChronology.getInstanceUTC(), BuddhistChronology.getInstance().withUTC());
    }

    public void testWithZone() {
        assertSame(BuddhistChronology.getInstance(TOKYO), BuddhistChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(BuddhistChronology.getInstance(LONDON), BuddhistChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(BuddhistChronology.getInstance(PARIS), BuddhistChronology.getInstance(TOKYO).withZone(PARIS));
        assertSame(BuddhistChronology.getInstance(LONDON), BuddhistChronology.getInstance(TOKYO).withZone(null));
        assertSame(BuddhistChronology.getInstance(PARIS), BuddhistChronology.getInstance().withZone(PARIS));
        assertSame(BuddhistChronology.getInstance(PARIS), BuddhistChronology.getInstanceUTC().withZone(PARIS));
    }

    public void testToString() {
        assertEquals("BuddhistChronology[Europe/London]", BuddhistChronology.getInstance(LONDON).toString());
        assertEquals("BuddhistChronology[Asia/Tokyo]", BuddhistChronology.getInstance(TOKYO).toString());
        assertEquals("BuddhistChronology[Europe/London]", BuddhistChronology.getInstance().toString());
        assertEquals("BuddhistChronology[UTC]", BuddhistChronology.getInstanceUTC().toString());
    }

    //-----------------------------------------------------------------------
    public void testDurationFields() {
        final BuddhistChronology buddhist = BuddhistChronology.getInstance();
        assertEquals("eras", buddhist.eras().getName());
        assertEquals("centuries", buddhist.centuries().getName());
        assertEquals("years", buddhist.years().getName());
        assertEquals("weekyears", buddhist.weekyears().getName());
        assertEquals("months", buddhist.months().getName());
        assertEquals("weeks", buddhist.weeks().getName());
        assertEquals("days", buddhist.days().getName());
        assertEquals("halfdays", GregorianChronology.getInstance().halfdays().getName());
        assertEquals("hours", buddhist.hours().getName());
        assertEquals("minutes", buddhist.minutes().getName());
        assertEquals("seconds", buddhist.seconds().getName());
        assertEquals("millis", buddhist.millis().getName());
        
        assertEquals(false, buddhist.eras().isSupported());
        assertEquals(true, buddhist.centuries().isSupported());
        assertEquals(true, buddhist.years().isSupported());
        assertEquals(true, buddhist.weekyears().isSupported());
        assertEquals(true, buddhist.months().isSupported());
        assertEquals(true, buddhist.weeks().isSupported());
        assertEquals(true, buddhist.days().isSupported());
        assertEquals(true, buddhist.halfdays().isSupported());
        assertEquals(true, buddhist.hours().isSupported());
        assertEquals(true, buddhist.minutes().isSupported());
        assertEquals(true, buddhist.seconds().isSupported());
        assertEquals(true, buddhist.millis().isSupported());
        
        assertEquals(false, buddhist.centuries().isPrecise());
        assertEquals(false, buddhist.years().isPrecise());
        assertEquals(false, buddhist.weekyears().isPrecise());
        assertEquals(false, buddhist.months().isPrecise());
        assertEquals(false, buddhist.weeks().isPrecise());
        assertEquals(false, buddhist.days().isPrecise());
        assertEquals(false, buddhist.halfdays().isPrecise());
        assertEquals(true, buddhist.hours().isPrecise());
        assertEquals(true, buddhist.minutes().isPrecise());
        assertEquals(true, buddhist.seconds().isPrecise());
        assertEquals(true, buddhist.millis().isPrecise());
        
        final BuddhistChronology buddhistUTC = BuddhistChronology.getInstanceUTC();
        assertEquals(false, buddhistUTC.centuries().isPrecise());
        assertEquals(false, buddhistUTC.years().isPrecise());
        assertEquals(false, buddhistUTC.weekyears().isPrecise());
        assertEquals(false, buddhistUTC.months().isPrecise());
        assertEquals(true, buddhistUTC.weeks().isPrecise());
        assertEquals(true, buddhistUTC.days().isPrecise());
        assertEquals(true, buddhistUTC.halfdays().isPrecise());
        assertEquals(true, buddhistUTC.hours().isPrecise());
        assertEquals(true, buddhistUTC.minutes().isPrecise());
        assertEquals(true, buddhistUTC.seconds().isPrecise());
        assertEquals(true, buddhistUTC.millis().isPrecise());
        
        final DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        final BuddhistChronology buddhistGMT = BuddhistChronology.getInstance(gmt);
        assertEquals(false, buddhistGMT.centuries().isPrecise());
        assertEquals(false, buddhistGMT.years().isPrecise());
        assertEquals(false, buddhistGMT.weekyears().isPrecise());
        assertEquals(false, buddhistGMT.months().isPrecise());
        assertEquals(true, buddhistGMT.weeks().isPrecise());
        assertEquals(true, buddhistGMT.days().isPrecise());
        assertEquals(true, buddhistGMT.halfdays().isPrecise());
        assertEquals(true, buddhistGMT.hours().isPrecise());
        assertEquals(true, buddhistGMT.minutes().isPrecise());
        assertEquals(true, buddhistGMT.seconds().isPrecise());
        assertEquals(true, buddhistGMT.millis().isPrecise());
    }

    public void testDateFields() {
        final BuddhistChronology buddhist = BuddhistChronology.getInstance();
        assertEquals("era", buddhist.era().getName());
        assertEquals("centuryOfEra", buddhist.centuryOfEra().getName());
        assertEquals("yearOfCentury", buddhist.yearOfCentury().getName());
        assertEquals("yearOfEra", buddhist.yearOfEra().getName());
        assertEquals("year", buddhist.year().getName());
        assertEquals("monthOfYear", buddhist.monthOfYear().getName());
        assertEquals("weekyearOfCentury", buddhist.weekyearOfCentury().getName());
        assertEquals("weekyear", buddhist.weekyear().getName());
        assertEquals("weekOfWeekyear", buddhist.weekOfWeekyear().getName());
        assertEquals("dayOfYear", buddhist.dayOfYear().getName());
        assertEquals("dayOfMonth", buddhist.dayOfMonth().getName());
        assertEquals("dayOfWeek", buddhist.dayOfWeek().getName());
        
        assertEquals(true, buddhist.era().isSupported());
        assertEquals(true, buddhist.centuryOfEra().isSupported());
        assertEquals(true, buddhist.yearOfCentury().isSupported());
        assertEquals(true, buddhist.yearOfEra().isSupported());
        assertEquals(true, buddhist.year().isSupported());
        assertEquals(true, buddhist.monthOfYear().isSupported());
        assertEquals(true, buddhist.weekyearOfCentury().isSupported());
        assertEquals(true, buddhist.weekyear().isSupported());
        assertEquals(true, buddhist.weekOfWeekyear().isSupported());
        assertEquals(true, buddhist.dayOfYear().isSupported());
        assertEquals(true, buddhist.dayOfMonth().isSupported());
        assertEquals(true, buddhist.dayOfWeek().isSupported());
    }

    public void testTimeFields() {
        final BuddhistChronology buddhist = BuddhistChronology.getInstance();
        assertEquals("halfdayOfDay", buddhist.halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", buddhist.clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", buddhist.hourOfHalfday().getName());
        assertEquals("clockhourOfDay", buddhist.clockhourOfDay().getName());
        assertEquals("hourOfDay", buddhist.hourOfDay().getName());
        assertEquals("minuteOfDay", buddhist.minuteOfDay().getName());
        assertEquals("minuteOfHour", buddhist.minuteOfHour().getName());
        assertEquals("secondOfDay", buddhist.secondOfDay().getName());
        assertEquals("secondOfMinute", buddhist.secondOfMinute().getName());
        assertEquals("millisOfDay", buddhist.millisOfDay().getName());
        assertEquals("millisOfSecond", buddhist.millisOfSecond().getName());
        
        assertEquals(true, buddhist.halfdayOfDay().isSupported());
        assertEquals(true, buddhist.clockhourOfHalfday().isSupported());
        assertEquals(true, buddhist.hourOfHalfday().isSupported());
        assertEquals(true, buddhist.clockhourOfDay().isSupported());
        assertEquals(true, buddhist.hourOfDay().isSupported());
        assertEquals(true, buddhist.minuteOfDay().isSupported());
        assertEquals(true, buddhist.minuteOfHour().isSupported());
        assertEquals(true, buddhist.secondOfDay().isSupported());
        assertEquals(true, buddhist.secondOfMinute().isSupported());
        assertEquals(true, buddhist.millisOfDay().isSupported());
        assertEquals(true, buddhist.millisOfSecond().isSupported());
    }

    //-----------------------------------------------------------------------
    public void testEpoch() {
        DateTime epoch = new DateTime(1, 1, 1, 0, 0, 0, 0, BUDDHIST_UTC);
        assertEquals(new DateTime(-543, 1, 1, 0, 0, 0, 0, JULIAN_UTC), epoch.withChronology(JULIAN_UTC));
    }

    public void testEra() {
        assertEquals(1, BuddhistChronology.BE);
        try {
            new DateTime(-1, 13, 5, 0, 0, 0, 0, BUDDHIST_UTC);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testKeyYears() {
        DateTime bd = new DateTime(2513, 1, 1, 0, 0, 0, 0, BUDDHIST_UTC);
        DateTime jd = new DateTime(1970, 1, 1, 0, 0, 0, 0, GJ_UTC);
        assertEquals(jd, bd.withChronology(GJ_UTC));
        assertEquals(2513, bd.getYear());
        assertEquals(2513, bd.getYearOfEra());
        assertEquals(2513, bd.plus(Period.weeks(1)).getWeekyear());
        
        bd = new DateTime(2126, 1, 1, 0, 0, 0, 0, BUDDHIST_UTC);
        jd = new DateTime(1583, 1, 1, 0, 0, 0, 0, GJ_UTC);
        assertEquals(jd, bd.withChronology(GJ_UTC));
        assertEquals(2126, bd.getYear());
        assertEquals(2126, bd.getYearOfEra());
        assertEquals(2126, bd.plus(Period.weeks(1)).getWeekyear());
        
        bd = new DateTime(2125, 1, 1, 0, 0, 0, 0, BUDDHIST_UTC);
        jd = new DateTime(1582, 1, 1, 0, 0, 0, 0, GJ_UTC);
        assertEquals(jd, bd.withChronology(GJ_UTC));
        assertEquals(2125, bd.getYear());
        assertEquals(2125, bd.getYearOfEra());
        assertEquals(2125, bd.plus(Period.weeks(1)).getWeekyear());
        
        bd = new DateTime(544, 1, 1, 0, 0, 0, 0, BUDDHIST_UTC);
        jd = new DateTime(1, 1, 1, 0, 0, 0, 0, GJ_UTC);
        assertEquals(jd, bd.withChronology(GJ_UTC));
        assertEquals(544, bd.getYear());
        assertEquals(544, bd.getYearOfEra());
        assertEquals(544, bd.plus(Period.weeks(1)).getWeekyear());
        
        bd = new DateTime(543, 1, 1, 0, 0, 0, 0, BUDDHIST_UTC);
        jd = new DateTime(-1, 1, 1, 0, 0, 0, 0, GJ_UTC);
        assertEquals(jd, bd.withChronology(GJ_UTC));
        assertEquals(543, bd.getYear());
        assertEquals(543, bd.getYearOfEra());
        assertEquals(543, bd.plus(Period.weeks(1)).getWeekyear());
        
        bd = new DateTime(1, 1, 1, 0, 0, 0, 0, BUDDHIST_UTC);
        jd = new DateTime(-543, 1, 1, 0, 0, 0, 0, GJ_UTC);
        assertEquals(jd, bd.withChronology(GJ_UTC));
        assertEquals(1, bd.getYear());
        assertEquals(1, bd.getYearOfEra());
        assertEquals(1, bd.plus(Period.weeks(1)).getWeekyear());
    }

    public void testCalendar() {
        if (TestAll.FAST) {
            return;
        }
        System.out.println("\nTestBuddhistChronology.testCalendar");
        DateTime epoch = new DateTime(1, 1, 1, 0, 0, 0, 0, BUDDHIST_UTC);
        long millis = epoch.getMillis();
        long end = new DateTime(3000, 1, 1, 0, 0, 0, 0, ISO_UTC).getMillis();
        DateTimeField dayOfWeek = BUDDHIST_UTC.dayOfWeek();
        DateTimeField weekOfWeekyear = GJ_UTC.weekOfWeekyear();
        DateTimeField dayOfYear = BUDDHIST_UTC.dayOfYear();
        DateTimeField dayOfMonth = BUDDHIST_UTC.dayOfMonth();
        DateTimeField monthOfYear = BUDDHIST_UTC.monthOfYear();
        DateTimeField year = BUDDHIST_UTC.year();
        DateTimeField yearOfEra = BUDDHIST_UTC.yearOfEra();
        DateTimeField era = BUDDHIST_UTC.era();
        DateTimeField gjDayOfWeek = GJ_UTC.dayOfWeek();
        DateTimeField gjWeekOfWeekyear = GJ_UTC.weekOfWeekyear();
        DateTimeField gjDayOfYear = GJ_UTC.dayOfYear();
        DateTimeField gjDayOfMonth = GJ_UTC.dayOfMonth();
        DateTimeField gjMonthOfYear = GJ_UTC.monthOfYear();
        DateTimeField gjYear = GJ_UTC.year();
        while (millis < end) {
            assertEquals(gjDayOfWeek.get(millis), dayOfWeek.get(millis));
            assertEquals(gjDayOfYear.get(millis), dayOfYear.get(millis));
            assertEquals(gjDayOfMonth.get(millis), dayOfMonth.get(millis));
            assertEquals(gjMonthOfYear.get(millis), monthOfYear.get(millis));
            assertEquals(gjWeekOfWeekyear.get(millis), weekOfWeekyear.get(millis));
            assertEquals(1, era.get(millis));
            int yearValue = gjYear.get(millis);
            if (yearValue <= 0) {
                yearValue++;
            }
            yearValue += 543;
            assertEquals(yearValue, year.get(millis));
            assertEquals(yearValue, yearOfEra.get(millis));
            millis += SKIP;
        }
    }

}
