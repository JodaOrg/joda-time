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
 * This class is a Junit unit test for BuddhistChronology.
 *
 * @author Stephen Colebourne
 */
public class TestBuddhistChronology extends TestCase {

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
        assertEquals(true, BuddhistChronology.getInstanceUTC().hours().isPrecise());
        assertEquals(true, BuddhistChronology.getInstanceUTC().minutes().isPrecise());
        assertEquals(true, BuddhistChronology.getInstanceUTC().seconds().isPrecise());
        assertEquals(true, BuddhistChronology.getInstanceUTC().millis().isPrecise());
    }

    public void testDateFields() {
        assertEquals("era", BuddhistChronology.getInstance().era().getName());
        assertEquals("centuryOfEra", BuddhistChronology.getInstance().centuryOfEra().getName());
        assertEquals("yearOfCentury", BuddhistChronology.getInstance().yearOfCentury().getName());
        assertEquals("yearOfEra", BuddhistChronology.getInstance().yearOfEra().getName());
        assertEquals("year", BuddhistChronology.getInstance().year().getName());
        assertEquals("monthOfYear", BuddhistChronology.getInstance().monthOfYear().getName());
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

}
