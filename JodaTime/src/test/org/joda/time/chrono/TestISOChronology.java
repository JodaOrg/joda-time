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
 * This class is a Junit unit test for ISOChronology.
 *
 * @author Stephen Colebourne
 */
public class TestISOChronology extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.getInstance("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.getInstance("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.getInstance("Asia/Tokyo");

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
        return new TestSuite(TestISOChronology.class);
    }

    public TestISOChronology(String name) {
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
        assertEquals(DateTimeZone.UTC, ISOChronology.getInstanceUTC().getZone());
        assertSame(ISOChronology.class, ISOChronology.getInstanceUTC().getClass());
    }

    public void testFactory() {
        assertEquals(LONDON, ISOChronology.getInstance().getZone());
        assertSame(ISOChronology.class, ISOChronology.getInstance().getClass());
    }

    public void testFactory_Zone() {
        assertEquals(TOKYO, ISOChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, ISOChronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, ISOChronology.getInstance(null).getZone());
        assertSame(ISOChronology.class, ISOChronology.getInstance(TOKYO).getClass());
    }

    //-----------------------------------------------------------------------
    public void testEquality() {
        assertSame(ISOChronology.getInstance(TOKYO), ISOChronology.getInstance(TOKYO));
        assertSame(ISOChronology.getInstance(LONDON), ISOChronology.getInstance(LONDON));
        assertSame(ISOChronology.getInstance(PARIS), ISOChronology.getInstance(PARIS));
        assertSame(ISOChronology.getInstanceUTC(), ISOChronology.getInstanceUTC());
        assertSame(ISOChronology.getInstance(), ISOChronology.getInstance(LONDON));
    }

    public void testWithUTC() {
        assertSame(ISOChronology.getInstanceUTC(), ISOChronology.getInstance(LONDON).withUTC());
        assertSame(ISOChronology.getInstanceUTC(), ISOChronology.getInstance(TOKYO).withUTC());
        assertSame(ISOChronology.getInstanceUTC(), ISOChronology.getInstanceUTC().withUTC());
        assertSame(ISOChronology.getInstanceUTC(), ISOChronology.getInstance().withUTC());
    }

    public void testWithZone() {
        assertSame(ISOChronology.getInstance(TOKYO), ISOChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(ISOChronology.getInstance(LONDON), ISOChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(ISOChronology.getInstance(PARIS), ISOChronology.getInstance(TOKYO).withZone(PARIS));
        assertSame(ISOChronology.getInstance(LONDON), ISOChronology.getInstance(TOKYO).withZone(null));
        assertSame(ISOChronology.getInstance(PARIS), ISOChronology.getInstance().withZone(PARIS));
        assertSame(ISOChronology.getInstance(PARIS), ISOChronology.getInstanceUTC().withZone(PARIS));
    }

    public void testToString() {
        assertEquals("ISOChronology[Europe/London]", ISOChronology.getInstance(LONDON).toString());
        assertEquals("ISOChronology[Asia/Tokyo]", ISOChronology.getInstance(TOKYO).toString());
        assertEquals("ISOChronology[Europe/London]", ISOChronology.getInstance().toString());
        assertEquals("ISOChronology[UTC]", ISOChronology.getInstanceUTC().toString());
    }

    //-----------------------------------------------------------------------
    public void testDurationFields() {
        assertEquals("eras", ISOChronology.getInstance().eras().getName());
        assertEquals("centuries", ISOChronology.getInstance().centuries().getName());
        assertEquals("years", ISOChronology.getInstance().years().getName());
        assertEquals("weekyears", ISOChronology.getInstance().weekyears().getName());
        assertEquals("months", ISOChronology.getInstance().months().getName());
        assertEquals("weeks", ISOChronology.getInstance().weeks().getName());
        assertEquals("days", ISOChronology.getInstance().days().getName());
        assertEquals("halfdays", ISOChronology.getInstance().halfdays().getName());
        assertEquals("hours", ISOChronology.getInstance().hours().getName());
        assertEquals("minutes", ISOChronology.getInstance().minutes().getName());
        assertEquals("seconds", ISOChronology.getInstance().seconds().getName());
        assertEquals("millis", ISOChronology.getInstance().millis().getName());
        
        assertEquals(false, ISOChronology.getInstance().eras().isSupported());
        assertEquals(true, ISOChronology.getInstance().centuries().isSupported());
        assertEquals(true, ISOChronology.getInstance().years().isSupported());
        assertEquals(true, ISOChronology.getInstance().weekyears().isSupported());
        assertEquals(true, ISOChronology.getInstance().months().isSupported());
        assertEquals(true, ISOChronology.getInstance().weeks().isSupported());
        assertEquals(true, ISOChronology.getInstance().days().isSupported());
        assertEquals(true, ISOChronology.getInstance().halfdays().isSupported());
        assertEquals(true, ISOChronology.getInstance().hours().isSupported());
        assertEquals(true, ISOChronology.getInstance().minutes().isSupported());
        assertEquals(true, ISOChronology.getInstance().seconds().isSupported());
        assertEquals(true, ISOChronology.getInstance().millis().isSupported());
        
        assertEquals(false, ISOChronology.getInstance().centuries().isPrecise());
        assertEquals(false, ISOChronology.getInstance().years().isPrecise());
        assertEquals(false, ISOChronology.getInstance().weekyears().isPrecise());
        assertEquals(false, ISOChronology.getInstance().months().isPrecise());
        assertEquals(false, ISOChronology.getInstance().weeks().isPrecise());
        assertEquals(false, ISOChronology.getInstance().days().isPrecise());
        assertEquals(false, ISOChronology.getInstance().halfdays().isPrecise());
        assertEquals(true, ISOChronology.getInstance().hours().isPrecise());
        assertEquals(true, ISOChronology.getInstance().minutes().isPrecise());
        assertEquals(true, ISOChronology.getInstance().seconds().isPrecise());
        assertEquals(true, ISOChronology.getInstance().millis().isPrecise());
        
        assertEquals(false, ISOChronology.getInstanceUTC().centuries().isPrecise());
        assertEquals(false, ISOChronology.getInstanceUTC().years().isPrecise());
        assertEquals(false, ISOChronology.getInstanceUTC().weekyears().isPrecise());
        assertEquals(false, ISOChronology.getInstanceUTC().months().isPrecise());
        assertEquals(true, ISOChronology.getInstanceUTC().weeks().isPrecise());
        assertEquals(true, ISOChronology.getInstanceUTC().days().isPrecise());
        assertEquals(true, ISOChronology.getInstanceUTC().halfdays().isPrecise());
        assertEquals(true, ISOChronology.getInstanceUTC().hours().isPrecise());
        assertEquals(true, ISOChronology.getInstanceUTC().minutes().isPrecise());
        assertEquals(true, ISOChronology.getInstanceUTC().seconds().isPrecise());
        assertEquals(true, ISOChronology.getInstanceUTC().millis().isPrecise());
    }

    public void testDateFields() {
        assertEquals("era", ISOChronology.getInstance().era().getName());
        assertEquals("centuryOfEra", ISOChronology.getInstance().centuryOfEra().getName());
        assertEquals("yearOfCentury", ISOChronology.getInstance().yearOfCentury().getName());
        assertEquals("yearOfEra", ISOChronology.getInstance().yearOfEra().getName());
        assertEquals("year", ISOChronology.getInstance().year().getName());
        assertEquals("monthOfYear", ISOChronology.getInstance().monthOfYear().getName());
        assertEquals("weekyearOfCentury", ISOChronology.getInstance().weekyearOfCentury().getName());
        assertEquals("weekyear", ISOChronology.getInstance().weekyear().getName());
        assertEquals("weekOfWeekyear", ISOChronology.getInstance().weekOfWeekyear().getName());
        assertEquals("dayOfYear", ISOChronology.getInstance().dayOfYear().getName());
        assertEquals("dayOfMonth", ISOChronology.getInstance().dayOfMonth().getName());
        assertEquals("dayOfWeek", ISOChronology.getInstance().dayOfWeek().getName());
        
        assertEquals(true, ISOChronology.getInstance().era().isSupported());
        assertEquals(true, ISOChronology.getInstance().centuryOfEra().isSupported());
        assertEquals(true, ISOChronology.getInstance().yearOfCentury().isSupported());
        assertEquals(true, ISOChronology.getInstance().yearOfEra().isSupported());
        assertEquals(true, ISOChronology.getInstance().year().isSupported());
        assertEquals(true, ISOChronology.getInstance().monthOfYear().isSupported());
        assertEquals(true, ISOChronology.getInstance().weekyearOfCentury().isSupported());
        assertEquals(true, ISOChronology.getInstance().weekyear().isSupported());
        assertEquals(true, ISOChronology.getInstance().weekOfWeekyear().isSupported());
        assertEquals(true, ISOChronology.getInstance().dayOfYear().isSupported());
        assertEquals(true, ISOChronology.getInstance().dayOfMonth().isSupported());
        assertEquals(true, ISOChronology.getInstance().dayOfWeek().isSupported());
    }

    public void testTimeFields() {
        assertEquals("halfdayOfDay", ISOChronology.getInstance().halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", ISOChronology.getInstance().clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", ISOChronology.getInstance().hourOfHalfday().getName());
        assertEquals("clockhourOfDay", ISOChronology.getInstance().clockhourOfDay().getName());
        assertEquals("hourOfDay", ISOChronology.getInstance().hourOfDay().getName());
        assertEquals("minuteOfDay", ISOChronology.getInstance().minuteOfDay().getName());
        assertEquals("minuteOfHour", ISOChronology.getInstance().minuteOfHour().getName());
        assertEquals("secondOfDay", ISOChronology.getInstance().secondOfDay().getName());
        assertEquals("secondOfMinute", ISOChronology.getInstance().secondOfMinute().getName());
        assertEquals("millisOfDay", ISOChronology.getInstance().millisOfDay().getName());
        assertEquals("millisOfSecond", ISOChronology.getInstance().millisOfSecond().getName());
        
        assertEquals(true, ISOChronology.getInstance().halfdayOfDay().isSupported());
        assertEquals(true, ISOChronology.getInstance().clockhourOfHalfday().isSupported());
        assertEquals(true, ISOChronology.getInstance().hourOfHalfday().isSupported());
        assertEquals(true, ISOChronology.getInstance().clockhourOfDay().isSupported());
        assertEquals(true, ISOChronology.getInstance().hourOfDay().isSupported());
        assertEquals(true, ISOChronology.getInstance().minuteOfDay().isSupported());
        assertEquals(true, ISOChronology.getInstance().minuteOfHour().isSupported());
        assertEquals(true, ISOChronology.getInstance().secondOfDay().isSupported());
        assertEquals(true, ISOChronology.getInstance().secondOfMinute().isSupported());
        assertEquals(true, ISOChronology.getInstance().millisOfDay().isSupported());
        assertEquals(true, ISOChronology.getInstance().millisOfSecond().isSupported());
    }

}
