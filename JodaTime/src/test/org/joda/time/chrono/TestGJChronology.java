/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2004 Stephen Colebourne.  
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Joda project (http://www.joda.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "Joda" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact licence@joda.org.
 *
 * 5. Products derived from this software may not be called "Joda",
 *    nor may "Joda" appear in their name, without prior written
 *    permission of the Joda project.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE JODA AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Joda project and was originally 
 * created by Stephen Colebourne <scolebourne@joda.org>. For more
 * information on the Joda project, please see <http://www.joda.org/>.
 */
package org.joda.time.chrono;

import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;

/**
 * This class is a Junit unit test for GJChronology.
 *
 * @author Stephen Colebourne
 */
public class TestGJChronology extends TestCase {

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
        return new TestSuite(TestGJChronology.class);
    }

    public TestGJChronology(String name) {
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
        assertEquals(DateTimeZone.UTC, GJChronology.getInstanceUTC().getZone());
        assertSame(GJChronology.class, GJChronology.getInstanceUTC().getClass());
    }

    public void testFactory() {
        assertEquals(LONDON, GJChronology.getInstance().getZone());
        assertSame(GJChronology.class, GJChronology.getInstance().getClass());
    }

    public void testFactory_Zone() {
        assertEquals(TOKYO, GJChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, GJChronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, GJChronology.getInstance(null).getZone());
        assertSame(GJChronology.class, GJChronology.getInstance(TOKYO).getClass());
    }

    public void testFactory_Zone_long_int() {
        GJChronology chrono = GJChronology.getInstance(TOKYO, 0L, 2);
        assertEquals(TOKYO, chrono.getZone());
        assertEquals(new Instant(0L), chrono.getGregorianCutover());
        assertEquals(2, chrono.getMinimumDaysInFirstWeek());
        assertSame(GJChronology.class, GJChronology.getInstance(TOKYO, 0L, 2).getClass());
        
        try {
            GJChronology.getInstance(TOKYO, 0L, 0);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            GJChronology.getInstance(TOKYO, 0L, 8);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testFactory_Zone_RI() {
        GJChronology chrono = GJChronology.getInstance(TOKYO, new Instant(0L));
        assertEquals(TOKYO, chrono.getZone());
        assertEquals(new Instant(0L), chrono.getGregorianCutover());
        assertSame(GJChronology.class, GJChronology.getInstance(TOKYO, new Instant(0L)).getClass());
        
        DateTime cutover = new DateTime(1582, 10, 15, 0, 0, 0, 0, DateTimeZone.UTC);
        chrono = GJChronology.getInstance(TOKYO, null);
        assertEquals(TOKYO, chrono.getZone());
        assertEquals(cutover.toInstant(), chrono.getGregorianCutover());
    }

    public void testFactory_Zone_RI_int() {
        GJChronology chrono = GJChronology.getInstance(TOKYO, new Instant(0L), 2);
        assertEquals(TOKYO, chrono.getZone());
        assertEquals(new Instant(0L), chrono.getGregorianCutover());
        assertEquals(2, chrono.getMinimumDaysInFirstWeek());
        assertSame(GJChronology.class, GJChronology.getInstance(TOKYO, new Instant(0L), 2).getClass());
        
        DateTime cutover = new DateTime(1582, 10, 15, 0, 0, 0, 0, DateTimeZone.UTC);
        chrono = GJChronology.getInstance(TOKYO, null, 2);
        assertEquals(TOKYO, chrono.getZone());
        assertEquals(cutover.toInstant(), chrono.getGregorianCutover());
        assertEquals(2, chrono.getMinimumDaysInFirstWeek());
        
        try {
            GJChronology.getInstance(TOKYO, new Instant(0L), 0);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            GJChronology.getInstance(TOKYO, new Instant(0L), 8);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testEquality() {
        assertSame(GJChronology.getInstance(TOKYO), GJChronology.getInstance(TOKYO));
        assertSame(GJChronology.getInstance(LONDON), GJChronology.getInstance(LONDON));
        assertSame(GJChronology.getInstance(PARIS), GJChronology.getInstance(PARIS));
        assertSame(GJChronology.getInstanceUTC(), GJChronology.getInstanceUTC());
        assertSame(GJChronology.getInstance(), GJChronology.getInstance(LONDON));
    }

    public void testWithUTC() {
        assertSame(GJChronology.getInstanceUTC(), GJChronology.getInstance(LONDON).withUTC());
        assertSame(GJChronology.getInstanceUTC(), GJChronology.getInstance(TOKYO).withUTC());
        assertSame(GJChronology.getInstanceUTC(), GJChronology.getInstanceUTC().withUTC());
        assertSame(GJChronology.getInstanceUTC(), GJChronology.getInstance().withUTC());
    }

    public void testWithZone() {
        assertSame(GJChronology.getInstance(TOKYO), GJChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(GJChronology.getInstance(LONDON), GJChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(GJChronology.getInstance(PARIS), GJChronology.getInstance(TOKYO).withZone(PARIS));
        assertSame(GJChronology.getInstance(LONDON), GJChronology.getInstance(TOKYO).withZone(null));
        assertSame(GJChronology.getInstance(PARIS), GJChronology.getInstance().withZone(PARIS));
        assertSame(GJChronology.getInstance(PARIS), GJChronology.getInstanceUTC().withZone(PARIS));
    }

    public void testToString() {
        assertEquals("GJChronology[Europe/London]", GJChronology.getInstance(LONDON).toString());
        assertEquals("GJChronology[Asia/Tokyo]", GJChronology.getInstance(TOKYO).toString());
        assertEquals("GJChronology[Europe/London]", GJChronology.getInstance().toString());
        assertEquals("GJChronology[UTC]", GJChronology.getInstanceUTC().toString());
        assertEquals("GJChronology[UTC,cutover=1970-01-01]", GJChronology.getInstance(DateTimeZone.UTC, 0L, 4).toString());
        assertEquals("GJChronology[UTC,cutover=1970-01-01T00:00:00.001Z,mdfw=2]", GJChronology.getInstance(DateTimeZone.UTC, 1L, 2).toString());
    }

    //-----------------------------------------------------------------------
    public void testDurationFields() {
        assertEquals("eras", GJChronology.getInstance().eras().getName());
        assertEquals("centuries", GJChronology.getInstance().centuries().getName());
        assertEquals("years", GJChronology.getInstance().years().getName());
        assertEquals("weekyears", GJChronology.getInstance().weekyears().getName());
        assertEquals("months", GJChronology.getInstance().months().getName());
        assertEquals("weeks", GJChronology.getInstance().weeks().getName());
        assertEquals("days", GJChronology.getInstance().days().getName());
        assertEquals("hours", GJChronology.getInstance().hours().getName());
        assertEquals("minutes", GJChronology.getInstance().minutes().getName());
        assertEquals("seconds", GJChronology.getInstance().seconds().getName());
        assertEquals("millis", GJChronology.getInstance().millis().getName());
        
        assertEquals(false, GJChronology.getInstance().eras().isSupported());
        assertEquals(true, GJChronology.getInstance().centuries().isSupported());
        assertEquals(true, GJChronology.getInstance().years().isSupported());
        assertEquals(true, GJChronology.getInstance().weekyears().isSupported());
        assertEquals(true, GJChronology.getInstance().months().isSupported());
        assertEquals(true, GJChronology.getInstance().weeks().isSupported());
        assertEquals(true, GJChronology.getInstance().days().isSupported());
        assertEquals(true, GJChronology.getInstance().hours().isSupported());
        assertEquals(true, GJChronology.getInstance().minutes().isSupported());
        assertEquals(true, GJChronology.getInstance().seconds().isSupported());
        assertEquals(true, GJChronology.getInstance().millis().isSupported());
        
        assertEquals(false, GJChronology.getInstance().centuries().isPrecise());
        assertEquals(false, GJChronology.getInstance().years().isPrecise());
        assertEquals(false, GJChronology.getInstance().weekyears().isPrecise());
        assertEquals(false, GJChronology.getInstance().months().isPrecise());
        assertEquals(false, GJChronology.getInstance().weeks().isPrecise());
        assertEquals(false, GJChronology.getInstance().days().isPrecise());
        assertEquals(true, GJChronology.getInstance().hours().isPrecise());
        assertEquals(true, GJChronology.getInstance().minutes().isPrecise());
        assertEquals(true, GJChronology.getInstance().seconds().isPrecise());
        assertEquals(true, GJChronology.getInstance().millis().isPrecise());
        
        assertEquals(false, GJChronology.getInstanceUTC().centuries().isPrecise());
        assertEquals(false, GJChronology.getInstanceUTC().years().isPrecise());
        assertEquals(false, GJChronology.getInstanceUTC().weekyears().isPrecise());
        assertEquals(false, GJChronology.getInstanceUTC().months().isPrecise());
        assertEquals(true, GJChronology.getInstanceUTC().weeks().isPrecise());
        assertEquals(true, GJChronology.getInstanceUTC().days().isPrecise());
        assertEquals(true, GJChronology.getInstanceUTC().hours().isPrecise());
        assertEquals(true, GJChronology.getInstanceUTC().minutes().isPrecise());
        assertEquals(true, GJChronology.getInstanceUTC().seconds().isPrecise());
        assertEquals(true, GJChronology.getInstanceUTC().millis().isPrecise());
    }

    public void testDateFields() {
        assertEquals("era", GJChronology.getInstance().era().getName());
        assertEquals("centuryOfEra", GJChronology.getInstance().centuryOfEra().getName());
        assertEquals("yearOfCentury", GJChronology.getInstance().yearOfCentury().getName());
        assertEquals("yearOfEra", GJChronology.getInstance().yearOfEra().getName());
        assertEquals("year", GJChronology.getInstance().year().getName());
        assertEquals("monthOfYear", GJChronology.getInstance().monthOfYear().getName());
        assertEquals("weekyear", GJChronology.getInstance().weekyear().getName());
        assertEquals("weekOfWeekyear", GJChronology.getInstance().weekOfWeekyear().getName());
        assertEquals("dayOfYear", GJChronology.getInstance().dayOfYear().getName());
        assertEquals("dayOfMonth", GJChronology.getInstance().dayOfMonth().getName());
        assertEquals("dayOfWeek", GJChronology.getInstance().dayOfWeek().getName());
        
        assertEquals(true, GJChronology.getInstance().era().isSupported());
        assertEquals(true, GJChronology.getInstance().centuryOfEra().isSupported());
        assertEquals(true, GJChronology.getInstance().yearOfCentury().isSupported());
        assertEquals(true, GJChronology.getInstance().yearOfEra().isSupported());
        assertEquals(true, GJChronology.getInstance().year().isSupported());
        assertEquals(true, GJChronology.getInstance().monthOfYear().isSupported());
        assertEquals(true, GJChronology.getInstance().weekyear().isSupported());
        assertEquals(true, GJChronology.getInstance().weekOfWeekyear().isSupported());
        assertEquals(true, GJChronology.getInstance().dayOfYear().isSupported());
        assertEquals(true, GJChronology.getInstance().dayOfMonth().isSupported());
        assertEquals(true, GJChronology.getInstance().dayOfWeek().isSupported());
    }

    public void testTimeFields() {
        assertEquals("halfdayOfDay", GJChronology.getInstance().halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", GJChronology.getInstance().clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", GJChronology.getInstance().hourOfHalfday().getName());
        assertEquals("clockhourOfDay", GJChronology.getInstance().clockhourOfDay().getName());
        assertEquals("hourOfDay", GJChronology.getInstance().hourOfDay().getName());
        assertEquals("minuteOfDay", GJChronology.getInstance().minuteOfDay().getName());
        assertEquals("minuteOfHour", GJChronology.getInstance().minuteOfHour().getName());
        assertEquals("secondOfDay", GJChronology.getInstance().secondOfDay().getName());
        assertEquals("secondOfMinute", GJChronology.getInstance().secondOfMinute().getName());
        assertEquals("millisOfDay", GJChronology.getInstance().millisOfDay().getName());
        assertEquals("millisOfSecond", GJChronology.getInstance().millisOfSecond().getName());
        
        assertEquals(true, GJChronology.getInstance().halfdayOfDay().isSupported());
        assertEquals(true, GJChronology.getInstance().clockhourOfHalfday().isSupported());
        assertEquals(true, GJChronology.getInstance().hourOfHalfday().isSupported());
        assertEquals(true, GJChronology.getInstance().clockhourOfDay().isSupported());
        assertEquals(true, GJChronology.getInstance().hourOfDay().isSupported());
        assertEquals(true, GJChronology.getInstance().minuteOfDay().isSupported());
        assertEquals(true, GJChronology.getInstance().minuteOfHour().isSupported());
        assertEquals(true, GJChronology.getInstance().secondOfDay().isSupported());
        assertEquals(true, GJChronology.getInstance().secondOfMinute().isSupported());
        assertEquals(true, GJChronology.getInstance().millisOfDay().isSupported());
        assertEquals(true, GJChronology.getInstance().millisOfSecond().isSupported());
    }

}
