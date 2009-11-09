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
import org.joda.time.DurationFieldType;
import org.joda.time.DateTime.Property;

/**
 * This class is a Junit unit test for IslamicChronology.
 *
 * @author Stephen Colebourne
 */
public class TestIslamicChronology extends TestCase {

    private static long SKIP = 1 * DateTimeConstants.MILLIS_PER_DAY;

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
    private static final Chronology ISLAMIC_UTC = IslamicChronology.getInstanceUTC();
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();
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
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        SKIP = 1 * DateTimeConstants.MILLIS_PER_DAY;
        return new TestSuite(TestIslamicChronology.class);
    }

    public TestIslamicChronology(String name) {
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
        assertEquals(DateTimeZone.UTC, IslamicChronology.getInstanceUTC().getZone());
        assertSame(IslamicChronology.class, IslamicChronology.getInstanceUTC().getClass());
    }

    public void testFactory() {
        assertEquals(LONDON, IslamicChronology.getInstance().getZone());
        assertSame(IslamicChronology.class, IslamicChronology.getInstance().getClass());
    }

    public void testFactory_Zone() {
        assertEquals(TOKYO, IslamicChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, IslamicChronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, IslamicChronology.getInstance(null).getZone());
        assertSame(IslamicChronology.class, IslamicChronology.getInstance(TOKYO).getClass());
    }

    //-----------------------------------------------------------------------
    public void testEquality() {
        assertSame(IslamicChronology.getInstance(TOKYO), IslamicChronology.getInstance(TOKYO));
        assertSame(IslamicChronology.getInstance(LONDON), IslamicChronology.getInstance(LONDON));
        assertSame(IslamicChronology.getInstance(PARIS), IslamicChronology.getInstance(PARIS));
        assertSame(IslamicChronology.getInstanceUTC(), IslamicChronology.getInstanceUTC());
        assertSame(IslamicChronology.getInstance(), IslamicChronology.getInstance(LONDON));
    }

    public void testWithUTC() {
        assertSame(IslamicChronology.getInstanceUTC(), IslamicChronology.getInstance(LONDON).withUTC());
        assertSame(IslamicChronology.getInstanceUTC(), IslamicChronology.getInstance(TOKYO).withUTC());
        assertSame(IslamicChronology.getInstanceUTC(), IslamicChronology.getInstanceUTC().withUTC());
        assertSame(IslamicChronology.getInstanceUTC(), IslamicChronology.getInstance().withUTC());
    }

    public void testWithZone() {
        assertSame(IslamicChronology.getInstance(TOKYO), IslamicChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(IslamicChronology.getInstance(LONDON), IslamicChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(IslamicChronology.getInstance(PARIS), IslamicChronology.getInstance(TOKYO).withZone(PARIS));
        assertSame(IslamicChronology.getInstance(LONDON), IslamicChronology.getInstance(TOKYO).withZone(null));
        assertSame(IslamicChronology.getInstance(PARIS), IslamicChronology.getInstance().withZone(PARIS));
        assertSame(IslamicChronology.getInstance(PARIS), IslamicChronology.getInstanceUTC().withZone(PARIS));
    }

    public void testToString() {
        assertEquals("IslamicChronology[Europe/London]", IslamicChronology.getInstance(LONDON).toString());
        assertEquals("IslamicChronology[Asia/Tokyo]", IslamicChronology.getInstance(TOKYO).toString());
        assertEquals("IslamicChronology[Europe/London]", IslamicChronology.getInstance().toString());
        assertEquals("IslamicChronology[UTC]", IslamicChronology.getInstanceUTC().toString());
    }

    //-----------------------------------------------------------------------
    public void testDurationFields() {
        assertEquals("eras", IslamicChronology.getInstance().eras().getName());
        assertEquals("centuries", IslamicChronology.getInstance().centuries().getName());
        assertEquals("years", IslamicChronology.getInstance().years().getName());
        assertEquals("weekyears", IslamicChronology.getInstance().weekyears().getName());
        assertEquals("months", IslamicChronology.getInstance().months().getName());
        assertEquals("weeks", IslamicChronology.getInstance().weeks().getName());
        assertEquals("days", IslamicChronology.getInstance().days().getName());
        assertEquals("halfdays", IslamicChronology.getInstance().halfdays().getName());
        assertEquals("hours", IslamicChronology.getInstance().hours().getName());
        assertEquals("minutes", IslamicChronology.getInstance().minutes().getName());
        assertEquals("seconds", IslamicChronology.getInstance().seconds().getName());
        assertEquals("millis", IslamicChronology.getInstance().millis().getName());
        
        assertEquals(false, IslamicChronology.getInstance().eras().isSupported());
        assertEquals(true, IslamicChronology.getInstance().centuries().isSupported());
        assertEquals(true, IslamicChronology.getInstance().years().isSupported());
        assertEquals(true, IslamicChronology.getInstance().weekyears().isSupported());
        assertEquals(true, IslamicChronology.getInstance().months().isSupported());
        assertEquals(true, IslamicChronology.getInstance().weeks().isSupported());
        assertEquals(true, IslamicChronology.getInstance().days().isSupported());
        assertEquals(true, IslamicChronology.getInstance().halfdays().isSupported());
        assertEquals(true, IslamicChronology.getInstance().hours().isSupported());
        assertEquals(true, IslamicChronology.getInstance().minutes().isSupported());
        assertEquals(true, IslamicChronology.getInstance().seconds().isSupported());
        assertEquals(true, IslamicChronology.getInstance().millis().isSupported());
        
        assertEquals(false, IslamicChronology.getInstance().centuries().isPrecise());
        assertEquals(false, IslamicChronology.getInstance().years().isPrecise());
        assertEquals(false, IslamicChronology.getInstance().weekyears().isPrecise());
        assertEquals(false, IslamicChronology.getInstance().months().isPrecise());
        assertEquals(false, IslamicChronology.getInstance().weeks().isPrecise());
        assertEquals(false, IslamicChronology.getInstance().days().isPrecise());
        assertEquals(false, IslamicChronology.getInstance().halfdays().isPrecise());
        assertEquals(true, IslamicChronology.getInstance().hours().isPrecise());
        assertEquals(true, IslamicChronology.getInstance().minutes().isPrecise());
        assertEquals(true, IslamicChronology.getInstance().seconds().isPrecise());
        assertEquals(true, IslamicChronology.getInstance().millis().isPrecise());
        
        assertEquals(false, IslamicChronology.getInstanceUTC().centuries().isPrecise());
        assertEquals(false, IslamicChronology.getInstanceUTC().years().isPrecise());
        assertEquals(false, IslamicChronology.getInstanceUTC().weekyears().isPrecise());
        assertEquals(false, IslamicChronology.getInstanceUTC().months().isPrecise());
        assertEquals(true, IslamicChronology.getInstanceUTC().weeks().isPrecise());
        assertEquals(true, IslamicChronology.getInstanceUTC().days().isPrecise());
        assertEquals(true, IslamicChronology.getInstanceUTC().halfdays().isPrecise());
        assertEquals(true, IslamicChronology.getInstanceUTC().hours().isPrecise());
        assertEquals(true, IslamicChronology.getInstanceUTC().minutes().isPrecise());
        assertEquals(true, IslamicChronology.getInstanceUTC().seconds().isPrecise());
        assertEquals(true, IslamicChronology.getInstanceUTC().millis().isPrecise());
        
        DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        assertEquals(false, IslamicChronology.getInstance(gmt).centuries().isPrecise());
        assertEquals(false, IslamicChronology.getInstance(gmt).years().isPrecise());
        assertEquals(false, IslamicChronology.getInstance(gmt).weekyears().isPrecise());
        assertEquals(false, IslamicChronology.getInstance(gmt).months().isPrecise());
        assertEquals(true, IslamicChronology.getInstance(gmt).weeks().isPrecise());
        assertEquals(true, IslamicChronology.getInstance(gmt).days().isPrecise());
        assertEquals(true, IslamicChronology.getInstance(gmt).halfdays().isPrecise());
        assertEquals(true, IslamicChronology.getInstance(gmt).hours().isPrecise());
        assertEquals(true, IslamicChronology.getInstance(gmt).minutes().isPrecise());
        assertEquals(true, IslamicChronology.getInstance(gmt).seconds().isPrecise());
        assertEquals(true, IslamicChronology.getInstance(gmt).millis().isPrecise());
    }

    public void testDateFields() {
        assertEquals("era", IslamicChronology.getInstance().era().getName());
        assertEquals("centuryOfEra", IslamicChronology.getInstance().centuryOfEra().getName());
        assertEquals("yearOfCentury", IslamicChronology.getInstance().yearOfCentury().getName());
        assertEquals("yearOfEra", IslamicChronology.getInstance().yearOfEra().getName());
        assertEquals("year", IslamicChronology.getInstance().year().getName());
        assertEquals("monthOfYear", IslamicChronology.getInstance().monthOfYear().getName());
        assertEquals("weekyearOfCentury", IslamicChronology.getInstance().weekyearOfCentury().getName());
        assertEquals("weekyear", IslamicChronology.getInstance().weekyear().getName());
        assertEquals("weekOfWeekyear", IslamicChronology.getInstance().weekOfWeekyear().getName());
        assertEquals("dayOfYear", IslamicChronology.getInstance().dayOfYear().getName());
        assertEquals("dayOfMonth", IslamicChronology.getInstance().dayOfMonth().getName());
        assertEquals("dayOfWeek", IslamicChronology.getInstance().dayOfWeek().getName());
        
        assertEquals(true, IslamicChronology.getInstance().era().isSupported());
        assertEquals(true, IslamicChronology.getInstance().centuryOfEra().isSupported());
        assertEquals(true, IslamicChronology.getInstance().yearOfCentury().isSupported());
        assertEquals(true, IslamicChronology.getInstance().yearOfEra().isSupported());
        assertEquals(true, IslamicChronology.getInstance().year().isSupported());
        assertEquals(true, IslamicChronology.getInstance().monthOfYear().isSupported());
        assertEquals(true, IslamicChronology.getInstance().weekyearOfCentury().isSupported());
        assertEquals(true, IslamicChronology.getInstance().weekyear().isSupported());
        assertEquals(true, IslamicChronology.getInstance().weekOfWeekyear().isSupported());
        assertEquals(true, IslamicChronology.getInstance().dayOfYear().isSupported());
        assertEquals(true, IslamicChronology.getInstance().dayOfMonth().isSupported());
        assertEquals(true, IslamicChronology.getInstance().dayOfWeek().isSupported());
    }

    public void testTimeFields() {
        assertEquals("halfdayOfDay", IslamicChronology.getInstance().halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", IslamicChronology.getInstance().clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", IslamicChronology.getInstance().hourOfHalfday().getName());
        assertEquals("clockhourOfDay", IslamicChronology.getInstance().clockhourOfDay().getName());
        assertEquals("hourOfDay", IslamicChronology.getInstance().hourOfDay().getName());
        assertEquals("minuteOfDay", IslamicChronology.getInstance().minuteOfDay().getName());
        assertEquals("minuteOfHour", IslamicChronology.getInstance().minuteOfHour().getName());
        assertEquals("secondOfDay", IslamicChronology.getInstance().secondOfDay().getName());
        assertEquals("secondOfMinute", IslamicChronology.getInstance().secondOfMinute().getName());
        assertEquals("millisOfDay", IslamicChronology.getInstance().millisOfDay().getName());
        assertEquals("millisOfSecond", IslamicChronology.getInstance().millisOfSecond().getName());
        
        assertEquals(true, IslamicChronology.getInstance().halfdayOfDay().isSupported());
        assertEquals(true, IslamicChronology.getInstance().clockhourOfHalfday().isSupported());
        assertEquals(true, IslamicChronology.getInstance().hourOfHalfday().isSupported());
        assertEquals(true, IslamicChronology.getInstance().clockhourOfDay().isSupported());
        assertEquals(true, IslamicChronology.getInstance().hourOfDay().isSupported());
        assertEquals(true, IslamicChronology.getInstance().minuteOfDay().isSupported());
        assertEquals(true, IslamicChronology.getInstance().minuteOfHour().isSupported());
        assertEquals(true, IslamicChronology.getInstance().secondOfDay().isSupported());
        assertEquals(true, IslamicChronology.getInstance().secondOfMinute().isSupported());
        assertEquals(true, IslamicChronology.getInstance().millisOfDay().isSupported());
        assertEquals(true, IslamicChronology.getInstance().millisOfSecond().isSupported());
    }

    //-----------------------------------------------------------------------
    public void testEpoch() {
        DateTime epoch = new DateTime(1, 1, 1, 0, 0, 0, 0, ISLAMIC_UTC);
        DateTime expectedEpoch = new DateTime(622, 7, 16, 0, 0, 0, 0, JULIAN_UTC);
        assertEquals(expectedEpoch.getMillis(), epoch.getMillis());
    }

    public void testEra() {
        assertEquals(1, IslamicChronology.AH);
        try {
            new DateTime(-1, 13, 5, 0, 0, 0, 0, ISLAMIC_UTC);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testFieldConstructor() {
        DateTime date = new DateTime(1364, 12, 6, 0, 0, 0, 0, ISLAMIC_UTC);
        DateTime expectedDate = new DateTime(1945, 11, 12, 0, 0, 0, 0, ISO_UTC);
        assertEquals(expectedDate.getMillis(), date.getMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Tests era, year, monthOfYear, dayOfMonth and dayOfWeek.
     */
    public void testCalendar() {
        if (TestAll.FAST) {
            return;
        }
        System.out.println("\nTestIslamicChronology.testCalendar");
        DateTime epoch = new DateTime(1, 1, 1, 0, 0, 0, 0, ISLAMIC_UTC);
        long millis = epoch.getMillis();
        long end = new DateTime(3000, 1, 1, 0, 0, 0, 0, ISO_UTC).getMillis();
        DateTimeField dayOfWeek = ISLAMIC_UTC.dayOfWeek();
        DateTimeField dayOfYear = ISLAMIC_UTC.dayOfYear();
        DateTimeField dayOfMonth = ISLAMIC_UTC.dayOfMonth();
        DateTimeField monthOfYear = ISLAMIC_UTC.monthOfYear();
        DateTimeField year = ISLAMIC_UTC.year();
        DateTimeField yearOfEra = ISLAMIC_UTC.yearOfEra();
        DateTimeField era = ISLAMIC_UTC.era();
        int expectedDOW = new DateTime(622, 7, 16, 0, 0, 0, 0, JULIAN_UTC).getDayOfWeek();
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
            int dayOfYearLen = dayOfYear.getMaximumValue(millis);
            int monthLen = dayOfMonth.getMaximumValue(millis);
            if (monthValue < 1 || monthValue > 12) {
                fail("Bad month: " + millis);
            }
            
            // test era
            assertEquals(1, era.get(millis));
            assertEquals("AH", era.getAsText(millis));
            assertEquals("AH", era.getAsShortText(millis));
            
            // test date
            assertEquals(expectedDOY, doyValue);
            assertEquals(expectedMonth, monthValue);
            assertEquals(expectedDay, dayValue);
            assertEquals(expectedDOW, dowValue);
            assertEquals(expectedYear, yearValue);
            assertEquals(expectedYear, yearOfEraValue);
            
            // test leap year
            boolean leap = ((11 * yearValue + 14) % 30) < 11;
            assertEquals(leap, year.isLeap(millis));
            
            // test month length
            switch (monthValue) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 9:
                case 11:
                    assertEquals(30, monthLen);
                    break;
                case 2:
                case 4:
                case 6:
                case 8:
                case 10:
                    assertEquals(29, monthLen);
                    break;
                case 12:
                    assertEquals((leap ? 30 : 29), monthLen);
                    break;
            }
            
            // test year length
            assertEquals((leap ? 355 : 354), dayOfYearLen);
            
            // recalculate date
            expectedDOW = (((expectedDOW + 1) - 1) % 7) + 1;
            expectedDay++;
            expectedDOY++;
            if (expectedDay > monthLen) {
                expectedDay = 1;
                expectedMonth++;
                if (expectedMonth == 13) {
                    expectedMonth = 1;
                    expectedDOY = 1;
                    expectedYear++;
                }
            }
            millis += SKIP;
        }
    }

    public void testSampleDate1() {
        DateTime dt = new DateTime(1945, 11, 12, 0, 0, 0, 0, ISO_UTC);
        dt = dt.withChronology(ISLAMIC_UTC);
        assertEquals(IslamicChronology.AH, dt.getEra());
        assertEquals(14, dt.getCenturyOfEra());  // TODO confirm
        assertEquals(64, dt.getYearOfCentury());
        assertEquals(1364, dt.getYearOfEra());
        
        assertEquals(1364, dt.getYear());
        Property fld = dt.year();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        assertEquals(new DateTime(1365, 12, 6, 0, 0, 0, 0, ISLAMIC_UTC), fld.addToCopy(1));
        
        assertEquals(12, dt.getMonthOfYear());
        fld = dt.monthOfYear();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(12, fld.getMaximumValue());
        assertEquals(12, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1365, 1, 6, 0, 0, 0, 0, ISLAMIC_UTC), fld.addToCopy(1));
        assertEquals(new DateTime(1364, 1, 6, 0, 0, 0, 0, ISLAMIC_UTC), fld.addWrapFieldToCopy(1));
        
        assertEquals(6, dt.getDayOfMonth());
        fld = dt.dayOfMonth();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(29, fld.getMaximumValue());
        assertEquals(30, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1364, 12, 7, 0, 0, 0, 0, ISLAMIC_UTC), fld.addToCopy(1));
        
        assertEquals(DateTimeConstants.MONDAY, dt.getDayOfWeek());
        fld = dt.dayOfWeek();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(7, fld.getMaximumValue());
        assertEquals(7, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1364, 12, 7, 0, 0, 0, 0, ISLAMIC_UTC), fld.addToCopy(1));
        
        assertEquals(6 * 30 + 5 * 29 + 6, dt.getDayOfYear());
        fld = dt.dayOfYear();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(354, fld.getMaximumValue());
        assertEquals(355, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1364, 12, 7, 0, 0, 0, 0, ISLAMIC_UTC), fld.addToCopy(1));
        
        assertEquals(0, dt.getHourOfDay());
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    public void testSampleDate2() {
        DateTime dt = new DateTime(2005, 11, 26, 0, 0, 0, 0, ISO_UTC);
        dt = dt.withChronology(ISLAMIC_UTC);
        assertEquals(IslamicChronology.AH, dt.getEra());
        assertEquals(15, dt.getCenturyOfEra());  // TODO confirm
        assertEquals(26, dt.getYearOfCentury());
        assertEquals(1426, dt.getYearOfEra());
        
        assertEquals(1426, dt.getYear());
        Property fld = dt.year();
        assertEquals(true, fld.isLeap());
        assertEquals(1, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        
        assertEquals(10, dt.getMonthOfYear());
        fld = dt.monthOfYear();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(12, fld.getMaximumValue());
        assertEquals(12, fld.getMaximumValueOverall());
        
        assertEquals(24, dt.getDayOfMonth());
        fld = dt.dayOfMonth();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(29, fld.getMaximumValue());
        assertEquals(30, fld.getMaximumValueOverall());
        
        assertEquals(DateTimeConstants.SATURDAY, dt.getDayOfWeek());
        fld = dt.dayOfWeek();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(7, fld.getMaximumValue());
        assertEquals(7, fld.getMaximumValueOverall());
        
        assertEquals(5 * 30 + 4 * 29 + 24, dt.getDayOfYear());
        fld = dt.dayOfYear();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(355, fld.getMaximumValue());
        assertEquals(355, fld.getMaximumValueOverall());
        
        assertEquals(0, dt.getHourOfDay());
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    public void testSampleDate3() {
        DateTime dt = new DateTime(1426, 12, 24, 0, 0, 0, 0, ISLAMIC_UTC);
        assertEquals(IslamicChronology.AH, dt.getEra());
        
        assertEquals(1426, dt.getYear());
        Property fld = dt.year();
        assertEquals(true, fld.isLeap());
        assertEquals(1, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        
        assertEquals(12, dt.getMonthOfYear());
        fld = dt.monthOfYear();
        assertEquals(true, fld.isLeap());
        assertEquals(1, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(12, fld.getMaximumValue());
        assertEquals(12, fld.getMaximumValueOverall());
        
        assertEquals(24, dt.getDayOfMonth());
        fld = dt.dayOfMonth();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(30, fld.getMaximumValue());
        assertEquals(30, fld.getMaximumValueOverall());
        
        assertEquals(DateTimeConstants.TUESDAY, dt.getDayOfWeek());
        fld = dt.dayOfWeek();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(7, fld.getMaximumValue());
        assertEquals(7, fld.getMaximumValueOverall());
        
        assertEquals(6 * 30 + 5 * 29 + 24, dt.getDayOfYear());
        fld = dt.dayOfYear();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(355, fld.getMaximumValue());
        assertEquals(355, fld.getMaximumValueOverall());
        
        assertEquals(0, dt.getHourOfDay());
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    public void testSampleDateWithZone() {
        DateTime dt = new DateTime(2005, 11, 26, 12, 0, 0, 0, PARIS).withChronology(ISLAMIC_UTC);
        assertEquals(IslamicChronology.AH, dt.getEra());
        assertEquals(1426, dt.getYear());
        assertEquals(10, dt.getMonthOfYear());
        assertEquals(24, dt.getDayOfMonth());
        assertEquals(11, dt.getHourOfDay());  // PARIS is UTC+1 in summer (12-1=11)
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    public void test15BasedLeapYear() {
        assertEquals(false, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(1));
        assertEquals(true, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(2));
        assertEquals(false, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(3));
        assertEquals(false, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(4));
        assertEquals(true, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(5));
        assertEquals(false, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(6));
        assertEquals(true, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(7));
        assertEquals(false, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(8));
        assertEquals(false, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(9));
        assertEquals(true, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(10));
        assertEquals(false, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(11));
        assertEquals(false, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(12));
        assertEquals(true, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(13));
        assertEquals(false, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(14));
        assertEquals(true, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(15));
        assertEquals(false, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(16));
        assertEquals(false, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(17));
        assertEquals(true, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(18));
        assertEquals(false, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(19));
        assertEquals(false, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(20));
        assertEquals(true, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(21));
        assertEquals(false, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(22));
        assertEquals(false, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(23));
        assertEquals(true, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(24));
        assertEquals(false, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(25));
        assertEquals(true, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(26));
        assertEquals(false, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(27));
        assertEquals(false, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(28));
        assertEquals(true, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(29));
        assertEquals(false, IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(30));
    }

    public void test16BasedLeapYear() {
        assertEquals(false, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(1));
        assertEquals(true, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(2));
        assertEquals(false, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(3));
        assertEquals(false, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(4));
        assertEquals(true, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(5));
        assertEquals(false, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(6));
        assertEquals(true, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(7));
        assertEquals(false, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(8));
        assertEquals(false, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(9));
        assertEquals(true, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(10));
        assertEquals(false, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(11));
        assertEquals(false, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(12));
        assertEquals(true, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(13));
        assertEquals(false, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(14));
        assertEquals(false, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(15));
        assertEquals(true, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(16));
        assertEquals(false, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(17));
        assertEquals(true, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(18));
        assertEquals(false, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(19));
        assertEquals(false, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(20));
        assertEquals(true, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(21));
        assertEquals(false, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(22));
        assertEquals(false, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(23));
        assertEquals(true, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(24));
        assertEquals(false, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(25));
        assertEquals(true, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(26));
        assertEquals(false, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(27));
        assertEquals(false, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(28));
        assertEquals(true, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(29));
        assertEquals(false, IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(30));
    }

    public void testIndianBasedLeapYear() {
        assertEquals(false, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(1));
        assertEquals(true, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(2));
        assertEquals(false, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(3));
        assertEquals(false, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(4));
        assertEquals(true, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(5));
        assertEquals(false, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(6));
        assertEquals(false, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(7));
        assertEquals(true, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(8));
        assertEquals(false, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(9));
        assertEquals(true, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(10));
        assertEquals(false, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(11));
        assertEquals(false, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(12));
        assertEquals(true, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(13));
        assertEquals(false, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(14));
        assertEquals(false, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(15));
        assertEquals(true, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(16));
        assertEquals(false, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(17));
        assertEquals(false, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(18));
        assertEquals(true, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(19));
        assertEquals(false, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(20));
        assertEquals(true, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(21));
        assertEquals(false, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(22));
        assertEquals(false, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(23));
        assertEquals(true, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(24));
        assertEquals(false, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(25));
        assertEquals(false, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(26));
        assertEquals(true, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(27));
        assertEquals(false, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(28));
        assertEquals(true, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(29));
        assertEquals(false, IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(30));
    }

    public void testHabashAlHasibBasedLeapYear() {
        assertEquals(false, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(1));
        assertEquals(true, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(2));
        assertEquals(false, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(3));
        assertEquals(false, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(4));
        assertEquals(true, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(5));
        assertEquals(false, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(6));
        assertEquals(false, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(7));
        assertEquals(true, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(8));
        assertEquals(false, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(9));
        assertEquals(false, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(10));
        assertEquals(true, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(11));
        assertEquals(false, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(12));
        assertEquals(true, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(13));
        assertEquals(false, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(14));
        assertEquals(false, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(15));
        assertEquals(true, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(16));
        assertEquals(false, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(17));
        assertEquals(false, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(18));
        assertEquals(true, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(19));
        assertEquals(false, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(20));
        assertEquals(true, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(21));
        assertEquals(false, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(22));
        assertEquals(false, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(23));
        assertEquals(true, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(24));
        assertEquals(false, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(25));
        assertEquals(false, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(26));
        assertEquals(true, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(27));
        assertEquals(false, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(28));
        assertEquals(false, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(29));
        assertEquals(true, IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(30));
    }

}
