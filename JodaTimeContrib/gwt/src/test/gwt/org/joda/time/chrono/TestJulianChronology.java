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

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;

/**
 * This class is a Junit unit test for JulianChronology.
 *
 * @author Stephen Colebourne
 */
public class TestJulianChronology extends TestCase {

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
        return new TestSuite(TestJulianChronology.class);
    }

    public TestJulianChronology(String name) {
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
        assertEquals(DateTimeZone.UTC, JulianChronology.getInstanceUTC().getZone());
        assertSame(JulianChronology.class, JulianChronology.getInstanceUTC().getClass());
    }

    public void testFactory() {
        assertEquals(LONDON, JulianChronology.getInstance().getZone());
        assertSame(JulianChronology.class, JulianChronology.getInstance().getClass());
    }

    public void testFactory_Zone() {
        assertEquals(TOKYO, JulianChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, JulianChronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, JulianChronology.getInstance(null).getZone());
        assertSame(JulianChronology.class, JulianChronology.getInstance(TOKYO).getClass());
    }

    public void testFactory_Zone_int() {
        JulianChronology chrono = JulianChronology.getInstance(TOKYO, 2);
        assertEquals(TOKYO, chrono.getZone());
        assertEquals(2, chrono.getMinimumDaysInFirstWeek());
        
        try {
            JulianChronology.getInstance(TOKYO, 0);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            JulianChronology.getInstance(TOKYO, 8);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testEquality() {
        assertSame(JulianChronology.getInstance(TOKYO), JulianChronology.getInstance(TOKYO));
        assertSame(JulianChronology.getInstance(LONDON), JulianChronology.getInstance(LONDON));
        assertSame(JulianChronology.getInstance(PARIS), JulianChronology.getInstance(PARIS));
        assertSame(JulianChronology.getInstanceUTC(), JulianChronology.getInstanceUTC());
        assertSame(JulianChronology.getInstance(), JulianChronology.getInstance(LONDON));
    }

    public void testWithUTC() {
        assertSame(JulianChronology.getInstanceUTC(), JulianChronology.getInstance(LONDON).withUTC());
        assertSame(JulianChronology.getInstanceUTC(), JulianChronology.getInstance(TOKYO).withUTC());
        assertSame(JulianChronology.getInstanceUTC(), JulianChronology.getInstanceUTC().withUTC());
        assertSame(JulianChronology.getInstanceUTC(), JulianChronology.getInstance().withUTC());
    }

    public void testWithZone() {
        assertSame(JulianChronology.getInstance(TOKYO), JulianChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(JulianChronology.getInstance(LONDON), JulianChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(JulianChronology.getInstance(PARIS), JulianChronology.getInstance(TOKYO).withZone(PARIS));
        assertSame(JulianChronology.getInstance(LONDON), JulianChronology.getInstance(TOKYO).withZone(null));
        assertSame(JulianChronology.getInstance(PARIS), JulianChronology.getInstance().withZone(PARIS));
        assertSame(JulianChronology.getInstance(PARIS), JulianChronology.getInstanceUTC().withZone(PARIS));
    }

    public void testToString() {
        assertEquals("JulianChronology[Europe/London]", JulianChronology.getInstance(LONDON).toString());
        assertEquals("JulianChronology[Asia/Tokyo]", JulianChronology.getInstance(TOKYO).toString());
        assertEquals("JulianChronology[Europe/London]", JulianChronology.getInstance().toString());
        assertEquals("JulianChronology[UTC]", JulianChronology.getInstanceUTC().toString());
        assertEquals("JulianChronology[UTC,mdfw=2]", JulianChronology.getInstance(DateTimeZone.UTC, 2).toString());
    }

    //-----------------------------------------------------------------------
    public void testDurationFields() {
        assertEquals("eras", JulianChronology.getInstance().eras().getName());
        assertEquals("centuries", JulianChronology.getInstance().centuries().getName());
        assertEquals("years", JulianChronology.getInstance().years().getName());
        assertEquals("weekyears", JulianChronology.getInstance().weekyears().getName());
        assertEquals("months", JulianChronology.getInstance().months().getName());
        assertEquals("weeks", JulianChronology.getInstance().weeks().getName());
        assertEquals("days", JulianChronology.getInstance().days().getName());
        assertEquals("halfdays", JulianChronology.getInstance().halfdays().getName());
        assertEquals("hours", JulianChronology.getInstance().hours().getName());
        assertEquals("minutes", JulianChronology.getInstance().minutes().getName());
        assertEquals("seconds", JulianChronology.getInstance().seconds().getName());
        assertEquals("millis", JulianChronology.getInstance().millis().getName());
        
        assertEquals(false, JulianChronology.getInstance().eras().isSupported());
        assertEquals(true, JulianChronology.getInstance().centuries().isSupported());
        assertEquals(true, JulianChronology.getInstance().years().isSupported());
        assertEquals(true, JulianChronology.getInstance().weekyears().isSupported());
        assertEquals(true, JulianChronology.getInstance().months().isSupported());
        assertEquals(true, JulianChronology.getInstance().weeks().isSupported());
        assertEquals(true, JulianChronology.getInstance().days().isSupported());
        assertEquals(true, JulianChronology.getInstance().halfdays().isSupported());
        assertEquals(true, JulianChronology.getInstance().hours().isSupported());
        assertEquals(true, JulianChronology.getInstance().minutes().isSupported());
        assertEquals(true, JulianChronology.getInstance().seconds().isSupported());
        assertEquals(true, JulianChronology.getInstance().millis().isSupported());
        
        assertEquals(false, JulianChronology.getInstance().centuries().isPrecise());
        assertEquals(false, JulianChronology.getInstance().years().isPrecise());
        assertEquals(false, JulianChronology.getInstance().weekyears().isPrecise());
        assertEquals(false, JulianChronology.getInstance().months().isPrecise());
        assertEquals(false, JulianChronology.getInstance().weeks().isPrecise());
        assertEquals(false, JulianChronology.getInstance().days().isPrecise());
        assertEquals(false, JulianChronology.getInstance().halfdays().isPrecise());
        assertEquals(true, JulianChronology.getInstance().hours().isPrecise());
        assertEquals(true, JulianChronology.getInstance().minutes().isPrecise());
        assertEquals(true, JulianChronology.getInstance().seconds().isPrecise());
        assertEquals(true, JulianChronology.getInstance().millis().isPrecise());
        
        assertEquals(false, JulianChronology.getInstanceUTC().centuries().isPrecise());
        assertEquals(false, JulianChronology.getInstanceUTC().years().isPrecise());
        assertEquals(false, JulianChronology.getInstanceUTC().weekyears().isPrecise());
        assertEquals(false, JulianChronology.getInstanceUTC().months().isPrecise());
        assertEquals(true, JulianChronology.getInstanceUTC().weeks().isPrecise());
        assertEquals(true, JulianChronology.getInstanceUTC().days().isPrecise());
        assertEquals(true, JulianChronology.getInstanceUTC().halfdays().isPrecise());
        assertEquals(true, JulianChronology.getInstanceUTC().hours().isPrecise());
        assertEquals(true, JulianChronology.getInstanceUTC().minutes().isPrecise());
        assertEquals(true, JulianChronology.getInstanceUTC().seconds().isPrecise());
        assertEquals(true, JulianChronology.getInstanceUTC().millis().isPrecise());
        
        DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        assertEquals(false, JulianChronology.getInstance(gmt).centuries().isPrecise());
        assertEquals(false, JulianChronology.getInstance(gmt).years().isPrecise());
        assertEquals(false, JulianChronology.getInstance(gmt).weekyears().isPrecise());
        assertEquals(false, JulianChronology.getInstance(gmt).months().isPrecise());
        assertEquals(true, JulianChronology.getInstance(gmt).weeks().isPrecise());
        assertEquals(true, JulianChronology.getInstance(gmt).days().isPrecise());
        assertEquals(true, JulianChronology.getInstance(gmt).halfdays().isPrecise());
        assertEquals(true, JulianChronology.getInstance(gmt).hours().isPrecise());
        assertEquals(true, JulianChronology.getInstance(gmt).minutes().isPrecise());
        assertEquals(true, JulianChronology.getInstance(gmt).seconds().isPrecise());
        assertEquals(true, JulianChronology.getInstance(gmt).millis().isPrecise());
    }

    public void testDateFields() {
        assertEquals("era", JulianChronology.getInstance().era().getName());
        assertEquals("centuryOfEra", JulianChronology.getInstance().centuryOfEra().getName());
        assertEquals("yearOfCentury", JulianChronology.getInstance().yearOfCentury().getName());
        assertEquals("yearOfEra", JulianChronology.getInstance().yearOfEra().getName());
        assertEquals("year", JulianChronology.getInstance().year().getName());
        assertEquals("monthOfYear", JulianChronology.getInstance().monthOfYear().getName());
        assertEquals("weekyearOfCentury", JulianChronology.getInstance().weekyearOfCentury().getName());
        assertEquals("weekyear", JulianChronology.getInstance().weekyear().getName());
        assertEquals("weekOfWeekyear", JulianChronology.getInstance().weekOfWeekyear().getName());
        assertEquals("dayOfYear", JulianChronology.getInstance().dayOfYear().getName());
        assertEquals("dayOfMonth", JulianChronology.getInstance().dayOfMonth().getName());
        assertEquals("dayOfWeek", JulianChronology.getInstance().dayOfWeek().getName());
        
        assertEquals(true, JulianChronology.getInstance().era().isSupported());
        assertEquals(true, JulianChronology.getInstance().centuryOfEra().isSupported());
        assertEquals(true, JulianChronology.getInstance().yearOfCentury().isSupported());
        assertEquals(true, JulianChronology.getInstance().yearOfEra().isSupported());
        assertEquals(true, JulianChronology.getInstance().year().isSupported());
        assertEquals(true, JulianChronology.getInstance().monthOfYear().isSupported());
        assertEquals(true, JulianChronology.getInstance().weekyearOfCentury().isSupported());
        assertEquals(true, JulianChronology.getInstance().weekyear().isSupported());
        assertEquals(true, JulianChronology.getInstance().weekOfWeekyear().isSupported());
        assertEquals(true, JulianChronology.getInstance().dayOfYear().isSupported());
        assertEquals(true, JulianChronology.getInstance().dayOfMonth().isSupported());
        assertEquals(true, JulianChronology.getInstance().dayOfWeek().isSupported());
    }

    public void testTimeFields() {
        assertEquals("halfdayOfDay", JulianChronology.getInstance().halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", JulianChronology.getInstance().clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", JulianChronology.getInstance().hourOfHalfday().getName());
        assertEquals("clockhourOfDay", JulianChronology.getInstance().clockhourOfDay().getName());
        assertEquals("hourOfDay", JulianChronology.getInstance().hourOfDay().getName());
        assertEquals("minuteOfDay", JulianChronology.getInstance().minuteOfDay().getName());
        assertEquals("minuteOfHour", JulianChronology.getInstance().minuteOfHour().getName());
        assertEquals("secondOfDay", JulianChronology.getInstance().secondOfDay().getName());
        assertEquals("secondOfMinute", JulianChronology.getInstance().secondOfMinute().getName());
        assertEquals("millisOfDay", JulianChronology.getInstance().millisOfDay().getName());
        assertEquals("millisOfSecond", JulianChronology.getInstance().millisOfSecond().getName());
        
        assertEquals(true, JulianChronology.getInstance().halfdayOfDay().isSupported());
        assertEquals(true, JulianChronology.getInstance().clockhourOfHalfday().isSupported());
        assertEquals(true, JulianChronology.getInstance().hourOfHalfday().isSupported());
        assertEquals(true, JulianChronology.getInstance().clockhourOfDay().isSupported());
        assertEquals(true, JulianChronology.getInstance().hourOfDay().isSupported());
        assertEquals(true, JulianChronology.getInstance().minuteOfDay().isSupported());
        assertEquals(true, JulianChronology.getInstance().minuteOfHour().isSupported());
        assertEquals(true, JulianChronology.getInstance().secondOfDay().isSupported());
        assertEquals(true, JulianChronology.getInstance().secondOfMinute().isSupported());
        assertEquals(true, JulianChronology.getInstance().millisOfDay().isSupported());
        assertEquals(true, JulianChronology.getInstance().millisOfSecond().isSupported());
    }

}
