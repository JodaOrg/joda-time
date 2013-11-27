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
import org.joda.time.DateMidnight;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.YearMonthDay;

/**
 * This class is a Junit unit test for GregorianChronology.
 *
 * @author Stephen Colebourne
 */
@SuppressWarnings("deprecation")
public class TestGregorianChronology extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

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
        return new TestSuite(TestGregorianChronology.class);
    }

    public TestGregorianChronology(String name) {
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
        assertEquals(DateTimeZone.UTC, GregorianChronology.getInstanceUTC().getZone());
        assertSame(GregorianChronology.class, GregorianChronology.getInstanceUTC().getClass());
    }

    public void testFactory() {
        assertEquals(LONDON, GregorianChronology.getInstance().getZone());
        assertSame(GregorianChronology.class, GregorianChronology.getInstance().getClass());
    }

    public void testFactory_Zone() {
        assertEquals(TOKYO, GregorianChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, GregorianChronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, GregorianChronology.getInstance(null).getZone());
        assertSame(GregorianChronology.class, GregorianChronology.getInstance(TOKYO).getClass());
    }

    public void testFactory_Zone_int() {
        GregorianChronology chrono = GregorianChronology.getInstance(TOKYO, 2);
        assertEquals(TOKYO, chrono.getZone());
        assertEquals(2, chrono.getMinimumDaysInFirstWeek());
        
        try {
            GregorianChronology.getInstance(TOKYO, 0);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            GregorianChronology.getInstance(TOKYO, 8);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testEquality() {
        assertSame(GregorianChronology.getInstance(TOKYO), GregorianChronology.getInstance(TOKYO));
        assertSame(GregorianChronology.getInstance(LONDON), GregorianChronology.getInstance(LONDON));
        assertSame(GregorianChronology.getInstance(PARIS), GregorianChronology.getInstance(PARIS));
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstanceUTC());
        assertSame(GregorianChronology.getInstance(), GregorianChronology.getInstance(LONDON));
    }

    public void testWithUTC() {
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstance(LONDON).withUTC());
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstance(TOKYO).withUTC());
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstanceUTC().withUTC());
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstance().withUTC());
    }

    public void testWithZone() {
        assertSame(GregorianChronology.getInstance(TOKYO), GregorianChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(GregorianChronology.getInstance(LONDON), GregorianChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(GregorianChronology.getInstance(PARIS), GregorianChronology.getInstance(TOKYO).withZone(PARIS));
        assertSame(GregorianChronology.getInstance(LONDON), GregorianChronology.getInstance(TOKYO).withZone(null));
        assertSame(GregorianChronology.getInstance(PARIS), GregorianChronology.getInstance().withZone(PARIS));
        assertSame(GregorianChronology.getInstance(PARIS), GregorianChronology.getInstanceUTC().withZone(PARIS));
    }

    public void testToString() {
        assertEquals("GregorianChronology[Europe/London]", GregorianChronology.getInstance(LONDON).toString());
        assertEquals("GregorianChronology[Asia/Tokyo]", GregorianChronology.getInstance(TOKYO).toString());
        assertEquals("GregorianChronology[Europe/London]", GregorianChronology.getInstance().toString());
        assertEquals("GregorianChronology[UTC]", GregorianChronology.getInstanceUTC().toString());
        assertEquals("GregorianChronology[UTC,mdfw=2]", GregorianChronology.getInstance(DateTimeZone.UTC, 2).toString());
    }

    //-----------------------------------------------------------------------
    public void testDurationFields() {
        final GregorianChronology greg = GregorianChronology.getInstance();
        assertEquals("eras", greg.eras().getName());
        assertEquals("centuries", greg.centuries().getName());
        assertEquals("years", greg.years().getName());
        assertEquals("weekyears", greg.weekyears().getName());
        assertEquals("months", greg.months().getName());
        assertEquals("weeks", greg.weeks().getName());
        assertEquals("days", greg.days().getName());
        assertEquals("halfdays", greg.halfdays().getName());
        assertEquals("hours", greg.hours().getName());
        assertEquals("minutes", greg.minutes().getName());
        assertEquals("seconds", greg.seconds().getName());
        assertEquals("millis", greg.millis().getName());
        
        assertEquals(false, greg.eras().isSupported());
        assertEquals(true, greg.centuries().isSupported());
        assertEquals(true, greg.years().isSupported());
        assertEquals(true, greg.weekyears().isSupported());
        assertEquals(true, greg.months().isSupported());
        assertEquals(true, greg.weeks().isSupported());
        assertEquals(true, greg.days().isSupported());
        assertEquals(true, greg.halfdays().isSupported());
        assertEquals(true, greg.hours().isSupported());
        assertEquals(true, greg.minutes().isSupported());
        assertEquals(true, greg.seconds().isSupported());
        assertEquals(true, greg.millis().isSupported());
        
        assertEquals(false, greg.centuries().isPrecise());
        assertEquals(false, greg.years().isPrecise());
        assertEquals(false, greg.weekyears().isPrecise());
        assertEquals(false, greg.months().isPrecise());
        assertEquals(false, greg.weeks().isPrecise());
        assertEquals(false, greg.days().isPrecise());
        assertEquals(false, greg.halfdays().isPrecise());
        assertEquals(true, greg.hours().isPrecise());
        assertEquals(true, greg.minutes().isPrecise());
        assertEquals(true, greg.seconds().isPrecise());
        assertEquals(true, greg.millis().isPrecise());
        
        final GregorianChronology gregUTC = GregorianChronology.getInstanceUTC();
        assertEquals(false, gregUTC.centuries().isPrecise());
        assertEquals(false, gregUTC.years().isPrecise());
        assertEquals(false, gregUTC.weekyears().isPrecise());
        assertEquals(false, gregUTC.months().isPrecise());
        assertEquals(true, gregUTC.weeks().isPrecise());
        assertEquals(true, gregUTC.days().isPrecise());
        assertEquals(true, gregUTC.halfdays().isPrecise());
        assertEquals(true, gregUTC.hours().isPrecise());
        assertEquals(true, gregUTC.minutes().isPrecise());
        assertEquals(true, gregUTC.seconds().isPrecise());
        assertEquals(true, gregUTC.millis().isPrecise());
        
        final DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        final GregorianChronology gregGMT = GregorianChronology.getInstance(gmt);
        assertEquals(false, gregGMT.centuries().isPrecise());
        assertEquals(false, gregGMT.years().isPrecise());
        assertEquals(false, gregGMT.weekyears().isPrecise());
        assertEquals(false, gregGMT.months().isPrecise());
        assertEquals(true, gregGMT.weeks().isPrecise());
        assertEquals(true, gregGMT.days().isPrecise());
        assertEquals(true, gregGMT.halfdays().isPrecise());
        assertEquals(true, gregGMT.hours().isPrecise());
        assertEquals(true, gregGMT.minutes().isPrecise());
        assertEquals(true, gregGMT.seconds().isPrecise());
        assertEquals(true, gregGMT.millis().isPrecise());
    }

    public void testDateFields() {
        final GregorianChronology greg = GregorianChronology.getInstance();
        assertEquals("era", greg.era().getName());
        assertEquals("centuryOfEra", greg.centuryOfEra().getName());
        assertEquals("yearOfCentury", greg.yearOfCentury().getName());
        assertEquals("yearOfEra", greg.yearOfEra().getName());
        assertEquals("year", greg.year().getName());
        assertEquals("monthOfYear", greg.monthOfYear().getName());
        assertEquals("weekyearOfCentury", greg.weekyearOfCentury().getName());
        assertEquals("weekyear", greg.weekyear().getName());
        assertEquals("weekOfWeekyear", greg.weekOfWeekyear().getName());
        assertEquals("dayOfYear", greg.dayOfYear().getName());
        assertEquals("dayOfMonth", greg.dayOfMonth().getName());
        assertEquals("dayOfWeek", greg.dayOfWeek().getName());
        
        assertEquals(true, greg.era().isSupported());
        assertEquals(true, greg.centuryOfEra().isSupported());
        assertEquals(true, greg.yearOfCentury().isSupported());
        assertEquals(true, greg.yearOfEra().isSupported());
        assertEquals(true, greg.year().isSupported());
        assertEquals(true, greg.monthOfYear().isSupported());
        assertEquals(true, greg.weekyearOfCentury().isSupported());
        assertEquals(true, greg.weekyear().isSupported());
        assertEquals(true, greg.weekOfWeekyear().isSupported());
        assertEquals(true, greg.dayOfYear().isSupported());
        assertEquals(true, greg.dayOfMonth().isSupported());
        assertEquals(true, greg.dayOfWeek().isSupported());
        
        assertEquals(greg.eras(), greg.era().getDurationField());
        assertEquals(greg.centuries(), greg.centuryOfEra().getDurationField());
        assertEquals(greg.years(), greg.yearOfCentury().getDurationField());
        assertEquals(greg.years(), greg.yearOfEra().getDurationField());
        assertEquals(greg.years(), greg.year().getDurationField());
        assertEquals(greg.months(), greg.monthOfYear().getDurationField());
        assertEquals(greg.weekyears(), greg.weekyearOfCentury().getDurationField());
        assertEquals(greg.weekyears(), greg.weekyear().getDurationField());
        assertEquals(greg.weeks(), greg.weekOfWeekyear().getDurationField());
        assertEquals(greg.days(), greg.dayOfYear().getDurationField());
        assertEquals(greg.days(), greg.dayOfMonth().getDurationField());
        assertEquals(greg.days(), greg.dayOfWeek().getDurationField());
        
        assertEquals(null, greg.era().getRangeDurationField());
        assertEquals(greg.eras(), greg.centuryOfEra().getRangeDurationField());
        assertEquals(greg.centuries(), greg.yearOfCentury().getRangeDurationField());
        assertEquals(greg.eras(), greg.yearOfEra().getRangeDurationField());
        assertEquals(null, greg.year().getRangeDurationField());
        assertEquals(greg.years(), greg.monthOfYear().getRangeDurationField());
        assertEquals(greg.centuries(), greg.weekyearOfCentury().getRangeDurationField());
        assertEquals(null, greg.weekyear().getRangeDurationField());
        assertEquals(greg.weekyears(), greg.weekOfWeekyear().getRangeDurationField());
        assertEquals(greg.years(), greg.dayOfYear().getRangeDurationField());
        assertEquals(greg.months(), greg.dayOfMonth().getRangeDurationField());
        assertEquals(greg.weeks(), greg.dayOfWeek().getRangeDurationField());
    }

    public void testTimeFields() {
        final GregorianChronology greg = GregorianChronology.getInstance();
        assertEquals("halfdayOfDay", greg.halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", greg.clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", greg.hourOfHalfday().getName());
        assertEquals("clockhourOfDay", greg.clockhourOfDay().getName());
        assertEquals("hourOfDay", greg.hourOfDay().getName());
        assertEquals("minuteOfDay", greg.minuteOfDay().getName());
        assertEquals("minuteOfHour", greg.minuteOfHour().getName());
        assertEquals("secondOfDay", greg.secondOfDay().getName());
        assertEquals("secondOfMinute", greg.secondOfMinute().getName());
        assertEquals("millisOfDay", greg.millisOfDay().getName());
        assertEquals("millisOfSecond", greg.millisOfSecond().getName());
        
        assertEquals(true, greg.halfdayOfDay().isSupported());
        assertEquals(true, greg.clockhourOfHalfday().isSupported());
        assertEquals(true, greg.hourOfHalfday().isSupported());
        assertEquals(true, greg.clockhourOfDay().isSupported());
        assertEquals(true, greg.hourOfDay().isSupported());
        assertEquals(true, greg.minuteOfDay().isSupported());
        assertEquals(true, greg.minuteOfHour().isSupported());
        assertEquals(true, greg.secondOfDay().isSupported());
        assertEquals(true, greg.secondOfMinute().isSupported());
        assertEquals(true, greg.millisOfDay().isSupported());
        assertEquals(true, greg.millisOfSecond().isSupported());
    }

    public void testMaximumValue() {
        YearMonthDay ymd1 = new YearMonthDay(1999, DateTimeConstants.FEBRUARY, 1);
        DateMidnight dm1 = new DateMidnight(1999, DateTimeConstants.FEBRUARY, 1);
        Chronology chrono = GregorianChronology.getInstance();
        assertEquals(28, chrono.dayOfMonth().getMaximumValue(ymd1));
        assertEquals(28, chrono.dayOfMonth().getMaximumValue(dm1.getMillis()));
    }

}
