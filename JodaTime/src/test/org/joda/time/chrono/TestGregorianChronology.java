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

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;

/**
 * This class is a Junit unit test for GregorianChronology.
 *
 * @author Stephen Colebourne
 */
public class TestGregorianChronology extends TestCase {

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
        assertEquals("eras", GregorianChronology.getInstance().eras().getName());
        assertEquals("centuries", GregorianChronology.getInstance().centuries().getName());
        assertEquals("years", GregorianChronology.getInstance().years().getName());
        assertEquals("weekyears", GregorianChronology.getInstance().weekyears().getName());
        assertEquals("months", GregorianChronology.getInstance().months().getName());
        assertEquals("weeks", GregorianChronology.getInstance().weeks().getName());
        assertEquals("days", GregorianChronology.getInstance().days().getName());
        assertEquals("hours", GregorianChronology.getInstance().hours().getName());
        assertEquals("minutes", GregorianChronology.getInstance().minutes().getName());
        assertEquals("seconds", GregorianChronology.getInstance().seconds().getName());
        assertEquals("millis", GregorianChronology.getInstance().millis().getName());
        
        assertEquals(false, GregorianChronology.getInstance().eras().isSupported());
        assertEquals(true, GregorianChronology.getInstance().centuries().isSupported());
        assertEquals(true, GregorianChronology.getInstance().years().isSupported());
        assertEquals(true, GregorianChronology.getInstance().weekyears().isSupported());
        assertEquals(true, GregorianChronology.getInstance().months().isSupported());
        assertEquals(true, GregorianChronology.getInstance().weeks().isSupported());
        assertEquals(true, GregorianChronology.getInstance().days().isSupported());
        assertEquals(true, GregorianChronology.getInstance().hours().isSupported());
        assertEquals(true, GregorianChronology.getInstance().minutes().isSupported());
        assertEquals(true, GregorianChronology.getInstance().seconds().isSupported());
        assertEquals(true, GregorianChronology.getInstance().millis().isSupported());
        
        assertEquals(false, GregorianChronology.getInstance().centuries().isPrecise());
        assertEquals(false, GregorianChronology.getInstance().years().isPrecise());
        assertEquals(false, GregorianChronology.getInstance().weekyears().isPrecise());
        assertEquals(false, GregorianChronology.getInstance().months().isPrecise());
        assertEquals(false, GregorianChronology.getInstance().weeks().isPrecise());
        assertEquals(false, GregorianChronology.getInstance().days().isPrecise());
        assertEquals(true, GregorianChronology.getInstance().hours().isPrecise());
        assertEquals(true, GregorianChronology.getInstance().minutes().isPrecise());
        assertEquals(true, GregorianChronology.getInstance().seconds().isPrecise());
        assertEquals(true, GregorianChronology.getInstance().millis().isPrecise());
        
        assertEquals(false, GregorianChronology.getInstanceUTC().centuries().isPrecise());
        assertEquals(false, GregorianChronology.getInstanceUTC().years().isPrecise());
        assertEquals(false, GregorianChronology.getInstanceUTC().weekyears().isPrecise());
        assertEquals(false, GregorianChronology.getInstanceUTC().months().isPrecise());
        assertEquals(true, GregorianChronology.getInstanceUTC().weeks().isPrecise());
        assertEquals(true, GregorianChronology.getInstanceUTC().days().isPrecise());
        assertEquals(true, GregorianChronology.getInstanceUTC().hours().isPrecise());
        assertEquals(true, GregorianChronology.getInstanceUTC().minutes().isPrecise());
        assertEquals(true, GregorianChronology.getInstanceUTC().seconds().isPrecise());
        assertEquals(true, GregorianChronology.getInstanceUTC().millis().isPrecise());
    }

    public void testDateFields() {
        assertEquals("era", GregorianChronology.getInstance().era().getName());
        assertEquals("centuryOfEra", GregorianChronology.getInstance().centuryOfEra().getName());
        assertEquals("yearOfCentury", GregorianChronology.getInstance().yearOfCentury().getName());
        assertEquals("yearOfEra", GregorianChronology.getInstance().yearOfEra().getName());
        assertEquals("year", GregorianChronology.getInstance().year().getName());
        assertEquals("monthOfYear", GregorianChronology.getInstance().monthOfYear().getName());
        assertEquals("weekyear", GregorianChronology.getInstance().weekyear().getName());
        assertEquals("weekOfWeekyear", GregorianChronology.getInstance().weekOfWeekyear().getName());
        assertEquals("dayOfYear", GregorianChronology.getInstance().dayOfYear().getName());
        assertEquals("dayOfMonth", GregorianChronology.getInstance().dayOfMonth().getName());
        assertEquals("dayOfWeek", GregorianChronology.getInstance().dayOfWeek().getName());
        
        assertEquals(true, GregorianChronology.getInstance().era().isSupported());
        assertEquals(true, GregorianChronology.getInstance().centuryOfEra().isSupported());
        assertEquals(true, GregorianChronology.getInstance().yearOfCentury().isSupported());
        assertEquals(true, GregorianChronology.getInstance().yearOfEra().isSupported());
        assertEquals(true, GregorianChronology.getInstance().year().isSupported());
        assertEquals(true, GregorianChronology.getInstance().monthOfYear().isSupported());
        assertEquals(true, GregorianChronology.getInstance().weekyear().isSupported());
        assertEquals(true, GregorianChronology.getInstance().weekOfWeekyear().isSupported());
        assertEquals(true, GregorianChronology.getInstance().dayOfYear().isSupported());
        assertEquals(true, GregorianChronology.getInstance().dayOfMonth().isSupported());
        assertEquals(true, GregorianChronology.getInstance().dayOfWeek().isSupported());
    }

    public void testTimeFields() {
        assertEquals("halfdayOfDay", GregorianChronology.getInstance().halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", GregorianChronology.getInstance().clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", GregorianChronology.getInstance().hourOfHalfday().getName());
        assertEquals("clockhourOfDay", GregorianChronology.getInstance().clockhourOfDay().getName());
        assertEquals("hourOfDay", GregorianChronology.getInstance().hourOfDay().getName());
        assertEquals("minuteOfDay", GregorianChronology.getInstance().minuteOfDay().getName());
        assertEquals("minuteOfHour", GregorianChronology.getInstance().minuteOfHour().getName());
        assertEquals("secondOfDay", GregorianChronology.getInstance().secondOfDay().getName());
        assertEquals("secondOfMinute", GregorianChronology.getInstance().secondOfMinute().getName());
        assertEquals("millisOfDay", GregorianChronology.getInstance().millisOfDay().getName());
        assertEquals("millisOfSecond", GregorianChronology.getInstance().millisOfSecond().getName());
        
        assertEquals(true, GregorianChronology.getInstance().halfdayOfDay().isSupported());
        assertEquals(true, GregorianChronology.getInstance().clockhourOfHalfday().isSupported());
        assertEquals(true, GregorianChronology.getInstance().hourOfHalfday().isSupported());
        assertEquals(true, GregorianChronology.getInstance().clockhourOfDay().isSupported());
        assertEquals(true, GregorianChronology.getInstance().hourOfDay().isSupported());
        assertEquals(true, GregorianChronology.getInstance().minuteOfDay().isSupported());
        assertEquals(true, GregorianChronology.getInstance().minuteOfHour().isSupported());
        assertEquals(true, GregorianChronology.getInstance().secondOfDay().isSupported());
        assertEquals(true, GregorianChronology.getInstance().secondOfMinute().isSupported());
        assertEquals(true, GregorianChronology.getInstance().millisOfDay().isSupported());
        assertEquals(true, GregorianChronology.getInstance().millisOfSecond().isSupported());
    }

}
