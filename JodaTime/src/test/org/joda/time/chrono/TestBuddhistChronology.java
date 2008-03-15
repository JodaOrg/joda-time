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
    private static final Chronology COPTIC_UTC = CopticChronology.getInstanceUTC();
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
        assertEquals("eras", BuddhistChronology.getInstance().eras().getName());
        assertEquals("centuries", BuddhistChronology.getInstance().centuries().getName());
        assertEquals("years", BuddhistChronology.getInstance().years().getName());
        assertEquals("weekyears", BuddhistChronology.getInstance().weekyears().getName());
        assertEquals("months", BuddhistChronology.getInstance().months().getName());
        assertEquals("weeks", BuddhistChronology.getInstance().weeks().getName());
        assertEquals("days", BuddhistChronology.getInstance().days().getName());
        assertEquals("halfdays", GregorianChronology.getInstance().halfdays().getName());
        assertEquals("hours", BuddhistChronology.getInstance().hours().getName());
        assertEquals("minutes", BuddhistChronology.getInstance().minutes().getName());
        assertEquals("seconds", BuddhistChronology.getInstance().seconds().getName());
        assertEquals("millis", BuddhistChronology.getInstance().millis().getName());
        
        assertEquals(false, BuddhistChronology.getInstance().eras().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().centuries().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().years().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().weekyears().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().months().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().weeks().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().days().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().halfdays().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().hours().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().minutes().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().seconds().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().millis().isSupported());
        
        assertEquals(false, BuddhistChronology.getInstance().centuries().isPrecise());
        assertEquals(false, BuddhistChronology.getInstance().years().isPrecise());
        assertEquals(false, BuddhistChronology.getInstance().weekyears().isPrecise());
        assertEquals(false, BuddhistChronology.getInstance().months().isPrecise());
        assertEquals(false, BuddhistChronology.getInstance().weeks().isPrecise());
        assertEquals(false, BuddhistChronology.getInstance().days().isPrecise());
        assertEquals(false, BuddhistChronology.getInstance().halfdays().isPrecise());
        assertEquals(true, BuddhistChronology.getInstance().hours().isPrecise());
        assertEquals(true, BuddhistChronology.getInstance().minutes().isPrecise());
        assertEquals(true, BuddhistChronology.getInstance().seconds().isPrecise());
        assertEquals(true, BuddhistChronology.getInstance().millis().isPrecise());
        
        assertEquals(false, BuddhistChronology.getInstanceUTC().centuries().isPrecise());
        assertEquals(false, BuddhistChronology.getInstanceUTC().years().isPrecise());
        assertEquals(false, BuddhistChronology.getInstanceUTC().weekyears().isPrecise());
        assertEquals(false, BuddhistChronology.getInstanceUTC().months().isPrecise());
        assertEquals(true, BuddhistChronology.getInstanceUTC().weeks().isPrecise());
        assertEquals(true, BuddhistChronology.getInstanceUTC().days().isPrecise());
        assertEquals(true, BuddhistChronology.getInstanceUTC().halfdays().isPrecise());
        assertEquals(true, BuddhistChronology.getInstanceUTC().hours().isPrecise());
        assertEquals(true, BuddhistChronology.getInstanceUTC().minutes().isPrecise());
        assertEquals(true, BuddhistChronology.getInstanceUTC().seconds().isPrecise());
        assertEquals(true, BuddhistChronology.getInstanceUTC().millis().isPrecise());
        
        DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        assertEquals(false, BuddhistChronology.getInstance(gmt).centuries().isPrecise());
        assertEquals(false, BuddhistChronology.getInstance(gmt).years().isPrecise());
        assertEquals(false, BuddhistChronology.getInstance(gmt).weekyears().isPrecise());
        assertEquals(false, BuddhistChronology.getInstance(gmt).months().isPrecise());
        assertEquals(true, BuddhistChronology.getInstance(gmt).weeks().isPrecise());
        assertEquals(true, BuddhistChronology.getInstance(gmt).days().isPrecise());
        assertEquals(true, BuddhistChronology.getInstance(gmt).halfdays().isPrecise());
        assertEquals(true, BuddhistChronology.getInstance(gmt).hours().isPrecise());
        assertEquals(true, BuddhistChronology.getInstance(gmt).minutes().isPrecise());
        assertEquals(true, BuddhistChronology.getInstance(gmt).seconds().isPrecise());
        assertEquals(true, BuddhistChronology.getInstance(gmt).millis().isPrecise());
    }

    public void testDateFields() {
        assertEquals("era", BuddhistChronology.getInstance().era().getName());
        assertEquals("centuryOfEra", BuddhistChronology.getInstance().centuryOfEra().getName());
        assertEquals("yearOfCentury", BuddhistChronology.getInstance().yearOfCentury().getName());
        assertEquals("yearOfEra", BuddhistChronology.getInstance().yearOfEra().getName());
        assertEquals("year", BuddhistChronology.getInstance().year().getName());
        assertEquals("monthOfYear", BuddhistChronology.getInstance().monthOfYear().getName());
        assertEquals("weekyearOfCentury", BuddhistChronology.getInstance().weekyearOfCentury().getName());
        assertEquals("weekyear", BuddhistChronology.getInstance().weekyear().getName());
        assertEquals("weekOfWeekyear", BuddhistChronology.getInstance().weekOfWeekyear().getName());
        assertEquals("dayOfYear", BuddhistChronology.getInstance().dayOfYear().getName());
        assertEquals("dayOfMonth", BuddhistChronology.getInstance().dayOfMonth().getName());
        assertEquals("dayOfWeek", BuddhistChronology.getInstance().dayOfWeek().getName());
        
        assertEquals(true, BuddhistChronology.getInstance().era().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().centuryOfEra().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().yearOfCentury().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().yearOfEra().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().year().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().monthOfYear().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().weekyearOfCentury().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().weekyear().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().weekOfWeekyear().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().dayOfYear().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().dayOfMonth().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().dayOfWeek().isSupported());
    }

    public void testTimeFields() {
        assertEquals("halfdayOfDay", BuddhistChronology.getInstance().halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", BuddhistChronology.getInstance().clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", BuddhistChronology.getInstance().hourOfHalfday().getName());
        assertEquals("clockhourOfDay", BuddhistChronology.getInstance().clockhourOfDay().getName());
        assertEquals("hourOfDay", BuddhistChronology.getInstance().hourOfDay().getName());
        assertEquals("minuteOfDay", BuddhistChronology.getInstance().minuteOfDay().getName());
        assertEquals("minuteOfHour", BuddhistChronology.getInstance().minuteOfHour().getName());
        assertEquals("secondOfDay", BuddhistChronology.getInstance().secondOfDay().getName());
        assertEquals("secondOfMinute", BuddhistChronology.getInstance().secondOfMinute().getName());
        assertEquals("millisOfDay", BuddhistChronology.getInstance().millisOfDay().getName());
        assertEquals("millisOfSecond", BuddhistChronology.getInstance().millisOfSecond().getName());
        
        assertEquals(true, BuddhistChronology.getInstance().halfdayOfDay().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().clockhourOfHalfday().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().hourOfHalfday().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().clockhourOfDay().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().hourOfDay().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().minuteOfDay().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().minuteOfHour().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().secondOfDay().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().secondOfMinute().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().millisOfDay().isSupported());
        assertEquals(true, BuddhistChronology.getInstance().millisOfSecond().isSupported());
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
        DateTimeField gjYearOfEra = GJ_UTC.yearOfEra();
        DateTimeField gjEra = GJ_UTC.era();
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
