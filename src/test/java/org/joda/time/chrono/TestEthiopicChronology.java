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

import org.joda.time.*;
import org.joda.time.DateTime.Property;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;



/**
 * This class is a Junit unit test for EthiopicChronology.
 *
 * @author Stephen Colebourne
 */
public class TestEthiopicChronology  {

    private static final int MILLIS_PER_DAY = DateTimeConstants.MILLIS_PER_DAY;

    private static long SKIP = 1 * MILLIS_PER_DAY;

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
    private static final Chronology ETHIOPIC_UTC = EthiopicChronology.getInstanceUTC();
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                     366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                     365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                     366 + 365;
    // 2002-06-09
    private long TEST_TIME_NOW =
            (y2002days + 31L + 28L + 31L + 30L + 31L + 9L -1L) * MILLIS_PER_DAY;

    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

   @Before
   public void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

   @After
   public void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    //-----------------------------------------------------------------------
   @Test
    public void testFactoryUTC() {
        assertEquals(DateTimeZone.UTC, EthiopicChronology.getInstanceUTC().getZone());
        assertSame(EthiopicChronology.class, EthiopicChronology.getInstanceUTC().getClass());
    }

   @Test
    public void testFactory() {
        assertEquals(LONDON, EthiopicChronology.getInstance().getZone());
        assertSame(EthiopicChronology.class, EthiopicChronology.getInstance().getClass());
    }

   @Test
    public void testFactory_Zone() {
        assertEquals(TOKYO, EthiopicChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, EthiopicChronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, EthiopicChronology.getInstance(null).getZone());
        assertSame(EthiopicChronology.class, EthiopicChronology.getInstance(TOKYO).getClass());
    }

    //-----------------------------------------------------------------------
   @Test
    public void testEquality() {
        assertSame(EthiopicChronology.getInstance(TOKYO), EthiopicChronology.getInstance(TOKYO));
        assertSame(EthiopicChronology.getInstance(LONDON), EthiopicChronology.getInstance(LONDON));
        assertSame(EthiopicChronology.getInstance(PARIS), EthiopicChronology.getInstance(PARIS));
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstanceUTC());
        assertSame(EthiopicChronology.getInstance(), EthiopicChronology.getInstance(LONDON));
    }

   @Test
    public void testWithUTC() {
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstance(LONDON).withUTC());
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstance(TOKYO).withUTC());
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstanceUTC().withUTC());
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstance().withUTC());
    }

   @Test
    public void testWithZone() {
        assertSame(EthiopicChronology.getInstance(TOKYO), EthiopicChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(EthiopicChronology.getInstance(LONDON), EthiopicChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(EthiopicChronology.getInstance(PARIS), EthiopicChronology.getInstance(TOKYO).withZone(PARIS));
        assertSame(EthiopicChronology.getInstance(LONDON), EthiopicChronology.getInstance(TOKYO).withZone(null));
        assertSame(EthiopicChronology.getInstance(PARIS), EthiopicChronology.getInstance().withZone(PARIS));
        assertSame(EthiopicChronology.getInstance(PARIS), EthiopicChronology.getInstanceUTC().withZone(PARIS));
    }

   @Test
    public void testToString() {
        assertEquals("EthiopicChronology[Europe/London]", EthiopicChronology.getInstance(LONDON).toString());
        assertEquals("EthiopicChronology[Asia/Tokyo]", EthiopicChronology.getInstance(TOKYO).toString());
        assertEquals("EthiopicChronology[Europe/London]", EthiopicChronology.getInstance().toString());
        assertEquals("EthiopicChronology[UTC]", EthiopicChronology.getInstanceUTC().toString());
    }

    //-----------------------------------------------------------------------
   @Test
    public void testDurationFields() {
        assertEquals("eras", EthiopicChronology.getInstance().eras().getName());
        assertEquals("centuries", EthiopicChronology.getInstance().centuries().getName());
        assertEquals("years", EthiopicChronology.getInstance().years().getName());
        assertEquals("weekyears", EthiopicChronology.getInstance().weekyears().getName());
        assertEquals("months", EthiopicChronology.getInstance().months().getName());
        assertEquals("weeks", EthiopicChronology.getInstance().weeks().getName());
        assertEquals("days", EthiopicChronology.getInstance().days().getName());
        assertEquals("halfdays", EthiopicChronology.getInstance().halfdays().getName());
        assertEquals("hours", EthiopicChronology.getInstance().hours().getName());
        assertEquals("minutes", EthiopicChronology.getInstance().minutes().getName());
        assertEquals("seconds", EthiopicChronology.getInstance().seconds().getName());
        assertEquals("millis", EthiopicChronology.getInstance().millis().getName());
        
        assertEquals(false, EthiopicChronology.getInstance().eras().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().centuries().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().years().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().weekyears().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().months().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().weeks().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().days().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().halfdays().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().hours().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().minutes().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().seconds().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().millis().isSupported());
        
        assertEquals(false, EthiopicChronology.getInstance().centuries().isPrecise());
        assertEquals(false, EthiopicChronology.getInstance().years().isPrecise());
        assertEquals(false, EthiopicChronology.getInstance().weekyears().isPrecise());
        assertEquals(false, EthiopicChronology.getInstance().months().isPrecise());
        assertEquals(false, EthiopicChronology.getInstance().weeks().isPrecise());
        assertEquals(false, EthiopicChronology.getInstance().days().isPrecise());
        assertEquals(false, EthiopicChronology.getInstance().halfdays().isPrecise());
        assertEquals(true, EthiopicChronology.getInstance().hours().isPrecise());
        assertEquals(true, EthiopicChronology.getInstance().minutes().isPrecise());
        assertEquals(true, EthiopicChronology.getInstance().seconds().isPrecise());
        assertEquals(true, EthiopicChronology.getInstance().millis().isPrecise());
        
        assertEquals(false, EthiopicChronology.getInstanceUTC().centuries().isPrecise());
        assertEquals(false, EthiopicChronology.getInstanceUTC().years().isPrecise());
        assertEquals(false, EthiopicChronology.getInstanceUTC().weekyears().isPrecise());
        assertEquals(false, EthiopicChronology.getInstanceUTC().months().isPrecise());
        assertEquals(true, EthiopicChronology.getInstanceUTC().weeks().isPrecise());
        assertEquals(true, EthiopicChronology.getInstanceUTC().days().isPrecise());
        assertEquals(true, EthiopicChronology.getInstanceUTC().halfdays().isPrecise());
        assertEquals(true, EthiopicChronology.getInstanceUTC().hours().isPrecise());
        assertEquals(true, EthiopicChronology.getInstanceUTC().minutes().isPrecise());
        assertEquals(true, EthiopicChronology.getInstanceUTC().seconds().isPrecise());
        assertEquals(true, EthiopicChronology.getInstanceUTC().millis().isPrecise());
        
        DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        assertEquals(false, EthiopicChronology.getInstance(gmt).centuries().isPrecise());
        assertEquals(false, EthiopicChronology.getInstance(gmt).years().isPrecise());
        assertEquals(false, EthiopicChronology.getInstance(gmt).weekyears().isPrecise());
        assertEquals(false, EthiopicChronology.getInstance(gmt).months().isPrecise());
        assertEquals(true, EthiopicChronology.getInstance(gmt).weeks().isPrecise());
        assertEquals(true, EthiopicChronology.getInstance(gmt).days().isPrecise());
        assertEquals(true, EthiopicChronology.getInstance(gmt).halfdays().isPrecise());
        assertEquals(true, EthiopicChronology.getInstance(gmt).hours().isPrecise());
        assertEquals(true, EthiopicChronology.getInstance(gmt).minutes().isPrecise());
        assertEquals(true, EthiopicChronology.getInstance(gmt).seconds().isPrecise());
        assertEquals(true, EthiopicChronology.getInstance(gmt).millis().isPrecise());
    }

   @Test
    public void testDateFields() {
        assertEquals("era", EthiopicChronology.getInstance().era().getName());
        assertEquals("centuryOfEra", EthiopicChronology.getInstance().centuryOfEra().getName());
        assertEquals("yearOfCentury", EthiopicChronology.getInstance().yearOfCentury().getName());
        assertEquals("yearOfEra", EthiopicChronology.getInstance().yearOfEra().getName());
        assertEquals("year", EthiopicChronology.getInstance().year().getName());
        assertEquals("monthOfYear", EthiopicChronology.getInstance().monthOfYear().getName());
        assertEquals("weekyearOfCentury", EthiopicChronology.getInstance().weekyearOfCentury().getName());
        assertEquals("weekyear", EthiopicChronology.getInstance().weekyear().getName());
        assertEquals("weekOfWeekyear", EthiopicChronology.getInstance().weekOfWeekyear().getName());
        assertEquals("dayOfYear", EthiopicChronology.getInstance().dayOfYear().getName());
        assertEquals("dayOfMonth", EthiopicChronology.getInstance().dayOfMonth().getName());
        assertEquals("dayOfWeek", EthiopicChronology.getInstance().dayOfWeek().getName());
        
        assertEquals(true, EthiopicChronology.getInstance().era().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().centuryOfEra().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().yearOfCentury().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().yearOfEra().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().year().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().monthOfYear().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().weekyearOfCentury().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().weekyear().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().weekOfWeekyear().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().dayOfYear().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().dayOfMonth().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().dayOfWeek().isSupported());
    }

   @Test
    public void testTimeFields() {
        assertEquals("halfdayOfDay", EthiopicChronology.getInstance().halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", EthiopicChronology.getInstance().clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", EthiopicChronology.getInstance().hourOfHalfday().getName());
        assertEquals("clockhourOfDay", EthiopicChronology.getInstance().clockhourOfDay().getName());
        assertEquals("hourOfDay", EthiopicChronology.getInstance().hourOfDay().getName());
        assertEquals("minuteOfDay", EthiopicChronology.getInstance().minuteOfDay().getName());
        assertEquals("minuteOfHour", EthiopicChronology.getInstance().minuteOfHour().getName());
        assertEquals("secondOfDay", EthiopicChronology.getInstance().secondOfDay().getName());
        assertEquals("secondOfMinute", EthiopicChronology.getInstance().secondOfMinute().getName());
        assertEquals("millisOfDay", EthiopicChronology.getInstance().millisOfDay().getName());
        assertEquals("millisOfSecond", EthiopicChronology.getInstance().millisOfSecond().getName());
        
        assertEquals(true, EthiopicChronology.getInstance().halfdayOfDay().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().clockhourOfHalfday().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().hourOfHalfday().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().clockhourOfDay().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().hourOfDay().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().minuteOfDay().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().minuteOfHour().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().secondOfDay().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().secondOfMinute().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().millisOfDay().isSupported());
        assertEquals(true, EthiopicChronology.getInstance().millisOfSecond().isSupported());
    }

    //-----------------------------------------------------------------------
   @Test
    public void testEpoch() {
        DateTime epoch = new DateTime(1, 1, 1, 0, 0, 0, 0, ETHIOPIC_UTC);
        assertEquals(new DateTime(8, 8, 29, 0, 0, 0, 0, JULIAN_UTC), epoch.withChronology(JULIAN_UTC));
    }

   @Test
    public void testEra() {
        assertEquals(1, EthiopicChronology.EE);
        try {
            new DateTime(-1, 13, 5, 0, 0, 0, 0, ETHIOPIC_UTC);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    /**
     * Tests era, year, monthOfYear, dayOfMonth and dayOfWeek.
     */
   @Test
    public void testCalendar() {
        if (TestAll.FAST) {
            return;
        }
        System.out.println("\nTestEthiopicChronology.testCalendar");
        DateTime epoch = new DateTime(1, 1, 1, 0, 0, 0, 0, ETHIOPIC_UTC);
        long millis = epoch.getMillis();
        long end = new DateTime(3000, 1, 1, 0, 0, 0, 0, ISO_UTC).getMillis();
        DateTimeField dayOfWeek = ETHIOPIC_UTC.dayOfWeek();
        DateTimeField dayOfYear = ETHIOPIC_UTC.dayOfYear();
        DateTimeField dayOfMonth = ETHIOPIC_UTC.dayOfMonth();
        DateTimeField monthOfYear = ETHIOPIC_UTC.monthOfYear();
        DateTimeField year = ETHIOPIC_UTC.year();
        DateTimeField yearOfEra = ETHIOPIC_UTC.yearOfEra();
        DateTimeField era = ETHIOPIC_UTC.era();
        int expectedDOW = new DateTime(8, 8, 29, 0, 0, 0, 0, JULIAN_UTC).getDayOfWeek();
        int expectedDOY = 1;
        int expectedDay = 1;
        int expectedMonth = 1;
        int expectedYear = 1;
        while (millis < end) {
            int dowValue = dayOfWeek.get(millis);
            int doyValue = dayOfYear.get(millis);
            int dayValue = dayOfMonth.get(millis);
            int monthValue = monthOfYear.get(millis);
            int yearValue = year.get(millis);
            int yearOfEraValue = yearOfEra.get(millis);
            int monthLen = dayOfMonth.getMaximumValue(millis);
            if (monthValue < 1 || monthValue > 13) {
                fail("Bad month: " + millis);
            }
            
            // test era
            assertEquals(1, era.get(millis));
            assertEquals("EE", era.getAsText(millis));
            assertEquals("EE", era.getAsShortText(millis));
            
            // test date
            assertEquals(expectedYear, yearValue);
            assertEquals(expectedYear, yearOfEraValue);
            assertEquals(expectedMonth, monthValue);
            assertEquals(expectedDay, dayValue);
            assertEquals(expectedDOW, dowValue);
            assertEquals(expectedDOY, doyValue);
            
            // test leap year
            assertEquals(yearValue % 4 == 3, year.isLeap(millis));
            
            // test month length
            if (monthValue == 13) {
                assertEquals(yearValue % 4 == 3, monthOfYear.isLeap(millis));
                if (yearValue % 4 == 3) {
                    assertEquals(6, monthLen);
                } else {
                    assertEquals(5, monthLen);
                }
            } else {
                assertEquals(30, monthLen);
            }
            
            // recalculate date
            expectedDOW = (((expectedDOW + 1) - 1) % 7) + 1;
            expectedDay++;
            expectedDOY++;
            if (expectedDay == 31 && expectedMonth < 13) {
                expectedDay = 1;
                expectedMonth++;
            } else if (expectedMonth == 13) {
                if (expectedYear % 4 == 3 && expectedDay == 7) {
                    expectedDay = 1;
                    expectedMonth = 1;
                    expectedYear++;
                    expectedDOY = 1;
                } else if (expectedYear % 4 != 3 && expectedDay == 6) {
                    expectedDay = 1;
                    expectedMonth = 1;
                    expectedYear++;
                    expectedDOY = 1;
                }
            }
            millis += SKIP;
        }
    }

   @Test
    public void testSampleDate() {
        DateTime dt = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(ETHIOPIC_UTC);
        assertEquals(EthiopicChronology.EE, dt.getEra());
        assertEquals(20, dt.getCenturyOfEra());  // TODO confirm
        assertEquals(96, dt.getYearOfCentury());
        assertEquals(1996, dt.getYearOfEra());
        
        assertEquals(1996, dt.getYear());
        Property fld = dt.year();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        assertEquals(new DateTime(1997, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC), fld.addToCopy(1));
        
        assertEquals(10, dt.getMonthOfYear());
        fld = dt.monthOfYear();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(13, fld.getMaximumValue());
        assertEquals(13, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1997, 1, 2, 0, 0, 0, 0, ETHIOPIC_UTC), fld.addToCopy(4));
        assertEquals(new DateTime(1996, 1, 2, 0, 0, 0, 0, ETHIOPIC_UTC), fld.addWrapFieldToCopy(4));
        
        assertEquals(2, dt.getDayOfMonth());
        fld = dt.dayOfMonth();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(30, fld.getMaximumValue());
        assertEquals(30, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1996, 10, 3, 0, 0, 0, 0, ETHIOPIC_UTC), fld.addToCopy(1));
        
        assertEquals(DateTimeConstants.WEDNESDAY, dt.getDayOfWeek());
        fld = dt.dayOfWeek();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(7, fld.getMaximumValue());
        assertEquals(7, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1996, 10, 3, 0, 0, 0, 0, ETHIOPIC_UTC), fld.addToCopy(1));
        
        assertEquals(9 * 30 + 2, dt.getDayOfYear());
        fld = dt.dayOfYear();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(365, fld.getMaximumValue());
        assertEquals(366, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1996, 10, 3, 0, 0, 0, 0, ETHIOPIC_UTC), fld.addToCopy(1));
        
        assertEquals(0, dt.getHourOfDay());
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

   @Test
    public void testSampleDateWithZone() {
        DateTime dt = new DateTime(2004, 6, 9, 12, 0, 0, 0, PARIS).withChronology(ETHIOPIC_UTC);
        assertEquals(EthiopicChronology.EE, dt.getEra());
        assertEquals(1996, dt.getYear());
        assertEquals(1996, dt.getYearOfEra());
        assertEquals(10, dt.getMonthOfYear());
        assertEquals(2, dt.getDayOfMonth());
        assertEquals(10, dt.getHourOfDay());  // PARIS is UTC+2 in summer (12-2=10)
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

   @Test
    public void testDurationYear() {
        // Leap 1999, NotLeap 1996,97,98
        DateTime dt96 = new DateTime(1996, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt97 = new DateTime(1997, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt98 = new DateTime(1998, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt99 = new DateTime(1999, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt00 = new DateTime(2000, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        
        DurationField fld = dt96.year().getDurationField();
        assertEquals(ETHIOPIC_UTC.years(), fld);
        assertEquals(1L * 365L * MILLIS_PER_DAY, fld.getMillis(1, dt96.getMillis()));
        assertEquals(2L * 365L * MILLIS_PER_DAY, fld.getMillis(2, dt96.getMillis()));
        assertEquals(3L * 365L * MILLIS_PER_DAY, fld.getMillis(3, dt96.getMillis()));
        assertEquals((4L * 365L + 1L) * MILLIS_PER_DAY, fld.getMillis(4, dt96.getMillis()));
        
        assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 4, fld.getMillis(1));
        assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 2, fld.getMillis(2));
        
        assertEquals(1L * 365L * MILLIS_PER_DAY, fld.getMillis(1L, dt96.getMillis()));
        assertEquals(2L * 365L * MILLIS_PER_DAY, fld.getMillis(2L, dt96.getMillis()));
        assertEquals(3L * 365L * MILLIS_PER_DAY, fld.getMillis(3L, dt96.getMillis()));
        assertEquals((4L * 365L + 1L) * MILLIS_PER_DAY, fld.getMillis(4L, dt96.getMillis()));
        
        assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 4, fld.getMillis(1L));
        assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 2, fld.getMillis(2L));
        
        assertEquals(((4L * 365L + 1L) * MILLIS_PER_DAY) / 4, fld.getUnitMillis());
        
        assertEquals(0, fld.getValue(1L * 365L * MILLIS_PER_DAY - 1L, dt96.getMillis()));
        assertEquals(1, fld.getValue(1L * 365L * MILLIS_PER_DAY, dt96.getMillis()));
        assertEquals(1, fld.getValue(1L * 365L * MILLIS_PER_DAY + 1L, dt96.getMillis()));
        assertEquals(1, fld.getValue(2L * 365L * MILLIS_PER_DAY - 1L, dt96.getMillis()));
        assertEquals(2, fld.getValue(2L * 365L * MILLIS_PER_DAY, dt96.getMillis()));
        assertEquals(2, fld.getValue(2L * 365L * MILLIS_PER_DAY + 1L, dt96.getMillis()));
        assertEquals(2, fld.getValue(3L * 365L * MILLIS_PER_DAY - 1L, dt96.getMillis()));
        assertEquals(3, fld.getValue(3L * 365L * MILLIS_PER_DAY, dt96.getMillis()));
        assertEquals(3, fld.getValue(3L * 365L * MILLIS_PER_DAY + 1L, dt96.getMillis()));
        assertEquals(3, fld.getValue((4L * 365L + 1L) * MILLIS_PER_DAY - 1L, dt96.getMillis()));
        assertEquals(4, fld.getValue((4L * 365L + 1L) * MILLIS_PER_DAY, dt96.getMillis()));
        assertEquals(4, fld.getValue((4L * 365L + 1L) * MILLIS_PER_DAY + 1L, dt96.getMillis()));
        
        assertEquals(dt97.getMillis(), fld.add(dt96.getMillis(), 1));
        assertEquals(dt98.getMillis(), fld.add(dt96.getMillis(), 2));
        assertEquals(dt99.getMillis(), fld.add(dt96.getMillis(), 3));
        assertEquals(dt00.getMillis(), fld.add(dt96.getMillis(), 4));
        
        assertEquals(dt97.getMillis(), fld.add(dt96.getMillis(), 1L));
        assertEquals(dt98.getMillis(), fld.add(dt96.getMillis(), 2L));
        assertEquals(dt99.getMillis(), fld.add(dt96.getMillis(), 3L));
        assertEquals(dt00.getMillis(), fld.add(dt96.getMillis(), 4L));
    }

   @Test
    public void testDurationMonth() {
        // Leap 1999, NotLeap 1996,97,98
        DateTime dt11 = new DateTime(1999, 11, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt12 = new DateTime(1999, 12, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt13 = new DateTime(1999, 13, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt01 = new DateTime(2000, 1, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        
        DurationField fld = dt11.monthOfYear().getDurationField();
        assertEquals(ETHIOPIC_UTC.months(), fld);
        assertEquals(1L * 30L * MILLIS_PER_DAY, fld.getMillis(1, dt11.getMillis()));
        assertEquals(2L * 30L * MILLIS_PER_DAY, fld.getMillis(2, dt11.getMillis()));
        assertEquals((2L * 30L + 6L) * MILLIS_PER_DAY, fld.getMillis(3, dt11.getMillis()));
        assertEquals((3L * 30L + 6L) * MILLIS_PER_DAY, fld.getMillis(4, dt11.getMillis()));
        
        assertEquals(1L * 30L * MILLIS_PER_DAY, fld.getMillis(1));
        assertEquals(2L * 30L * MILLIS_PER_DAY, fld.getMillis(2));
        assertEquals(13L * 30L * MILLIS_PER_DAY, fld.getMillis(13));
        
        assertEquals(1L * 30L * MILLIS_PER_DAY, fld.getMillis(1L, dt11.getMillis()));
        assertEquals(2L * 30L * MILLIS_PER_DAY, fld.getMillis(2L, dt11.getMillis()));
        assertEquals((2L * 30L + 6L) * MILLIS_PER_DAY, fld.getMillis(3L, dt11.getMillis()));
        assertEquals((3L * 30L + 6L) * MILLIS_PER_DAY, fld.getMillis(4L, dt11.getMillis()));
        
        assertEquals(1L * 30L * MILLIS_PER_DAY, fld.getMillis(1L));
        assertEquals(2L * 30L * MILLIS_PER_DAY, fld.getMillis(2L));
        assertEquals(13L * 30L * MILLIS_PER_DAY, fld.getMillis(13L));
        
        assertEquals(0, fld.getValue(1L * 30L * MILLIS_PER_DAY - 1L, dt11.getMillis()));
        assertEquals(1, fld.getValue(1L * 30L * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(1, fld.getValue(1L * 30L * MILLIS_PER_DAY + 1L, dt11.getMillis()));
        assertEquals(1, fld.getValue(2L * 30L * MILLIS_PER_DAY - 1L, dt11.getMillis()));
        assertEquals(2, fld.getValue(2L * 30L * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(2, fld.getValue(2L * 30L * MILLIS_PER_DAY + 1L, dt11.getMillis()));
        assertEquals(2, fld.getValue((2L * 30L + 6L) * MILLIS_PER_DAY - 1L, dt11.getMillis()));
        assertEquals(3, fld.getValue((2L * 30L + 6L) * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(3, fld.getValue((2L * 30L + 6L) * MILLIS_PER_DAY + 1L, dt11.getMillis()));
        assertEquals(3, fld.getValue((3L * 30L + 6L) * MILLIS_PER_DAY - 1L, dt11.getMillis()));
        assertEquals(4, fld.getValue((3L * 30L + 6L) * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(4, fld.getValue((3L * 30L + 6L) * MILLIS_PER_DAY + 1L, dt11.getMillis()));
        
        assertEquals(dt12.getMillis(), fld.add(dt11.getMillis(), 1));
        assertEquals(dt13.getMillis(), fld.add(dt11.getMillis(), 2));
        assertEquals(dt01.getMillis(), fld.add(dt11.getMillis(), 3));
        
        assertEquals(dt12.getMillis(), fld.add(dt11.getMillis(), 1L));
        assertEquals(dt13.getMillis(), fld.add(dt11.getMillis(), 2L));
        assertEquals(dt01.getMillis(), fld.add(dt11.getMillis(), 3L));
    }

}
