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
 * This class is a Junit unit test for CopticChronology.
 *
 * @author Stephen Colebourne
 */
public class TestCopticChronology extends TestCase {

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
        return new TestSuite(TestCopticChronology.class);
    }

    public TestCopticChronology(String name) {
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
        assertEquals(DateTimeZone.UTC, CopticChronology.getInstanceUTC().getZone());
        assertSame(CopticChronology.class, CopticChronology.getInstanceUTC().getClass());
    }

    public void testFactory() {
        assertEquals(LONDON, CopticChronology.getInstance().getZone());
        assertSame(CopticChronology.class, CopticChronology.getInstance().getClass());
    }

    public void testFactory_Zone() {
        assertEquals(TOKYO, CopticChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, CopticChronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, CopticChronology.getInstance(null).getZone());
        assertSame(CopticChronology.class, CopticChronology.getInstance(TOKYO).getClass());
    }

    //-----------------------------------------------------------------------
    public void testEquality() {
        assertSame(CopticChronology.getInstance(TOKYO), CopticChronology.getInstance(TOKYO));
        assertSame(CopticChronology.getInstance(LONDON), CopticChronology.getInstance(LONDON));
        assertSame(CopticChronology.getInstance(PARIS), CopticChronology.getInstance(PARIS));
        assertSame(CopticChronology.getInstanceUTC(), CopticChronology.getInstanceUTC());
        assertSame(CopticChronology.getInstance(), CopticChronology.getInstance(LONDON));
    }

    public void testWithUTC() {
        assertSame(CopticChronology.getInstanceUTC(), CopticChronology.getInstance(LONDON).withUTC());
        assertSame(CopticChronology.getInstanceUTC(), CopticChronology.getInstance(TOKYO).withUTC());
        assertSame(CopticChronology.getInstanceUTC(), CopticChronology.getInstanceUTC().withUTC());
        assertSame(CopticChronology.getInstanceUTC(), CopticChronology.getInstance().withUTC());
    }

    public void testWithZone() {
        assertSame(CopticChronology.getInstance(TOKYO), CopticChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(CopticChronology.getInstance(LONDON), CopticChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(CopticChronology.getInstance(PARIS), CopticChronology.getInstance(TOKYO).withZone(PARIS));
        assertSame(CopticChronology.getInstance(LONDON), CopticChronology.getInstance(TOKYO).withZone(null));
        assertSame(CopticChronology.getInstance(PARIS), CopticChronology.getInstance().withZone(PARIS));
        assertSame(CopticChronology.getInstance(PARIS), CopticChronology.getInstanceUTC().withZone(PARIS));
    }

    public void testToString() {
        assertEquals("CopticChronology[Europe/London]", CopticChronology.getInstance(LONDON).toString());
        assertEquals("CopticChronology[Asia/Tokyo]", CopticChronology.getInstance(TOKYO).toString());
        assertEquals("CopticChronology[Europe/London]", CopticChronology.getInstance().toString());
        assertEquals("CopticChronology[UTC]", CopticChronology.getInstanceUTC().toString());
    }

    //-----------------------------------------------------------------------
    public void testDurationFields() {
        assertEquals("eras", CopticChronology.getInstance().eras().getName());
        assertEquals("centuries", CopticChronology.getInstance().centuries().getName());
        assertEquals("years", CopticChronology.getInstance().years().getName());
        assertEquals("weekyears", CopticChronology.getInstance().weekyears().getName());
        assertEquals("months", CopticChronology.getInstance().months().getName());
        assertEquals("weeks", CopticChronology.getInstance().weeks().getName());
        assertEquals("days", CopticChronology.getInstance().days().getName());
        assertEquals("hours", CopticChronology.getInstance().hours().getName());
        assertEquals("minutes", CopticChronology.getInstance().minutes().getName());
        assertEquals("seconds", CopticChronology.getInstance().seconds().getName());
        assertEquals("millis", CopticChronology.getInstance().millis().getName());
        
        assertEquals(false, CopticChronology.getInstance().eras().isSupported());
        assertEquals(true, CopticChronology.getInstance().centuries().isSupported());
        assertEquals(true, CopticChronology.getInstance().years().isSupported());
        assertEquals(true, CopticChronology.getInstance().weekyears().isSupported());
        assertEquals(true, CopticChronology.getInstance().months().isSupported());
        assertEquals(true, CopticChronology.getInstance().weeks().isSupported());
        assertEquals(true, CopticChronology.getInstance().days().isSupported());
        assertEquals(true, CopticChronology.getInstance().hours().isSupported());
        assertEquals(true, CopticChronology.getInstance().minutes().isSupported());
        assertEquals(true, CopticChronology.getInstance().seconds().isSupported());
        assertEquals(true, CopticChronology.getInstance().millis().isSupported());
        
        assertEquals(false, CopticChronology.getInstance().centuries().isPrecise());
        assertEquals(false, CopticChronology.getInstance().years().isPrecise());
        assertEquals(false, CopticChronology.getInstance().weekyears().isPrecise());
        assertEquals(false, CopticChronology.getInstance().months().isPrecise());
        assertEquals(false, CopticChronology.getInstance().weeks().isPrecise());
        assertEquals(false, CopticChronology.getInstance().days().isPrecise());
        assertEquals(true, CopticChronology.getInstance().hours().isPrecise());
        assertEquals(true, CopticChronology.getInstance().minutes().isPrecise());
        assertEquals(true, CopticChronology.getInstance().seconds().isPrecise());
        assertEquals(true, CopticChronology.getInstance().millis().isPrecise());
        
        assertEquals(false, CopticChronology.getInstanceUTC().centuries().isPrecise());
        assertEquals(false, CopticChronology.getInstanceUTC().years().isPrecise());
        assertEquals(false, CopticChronology.getInstanceUTC().weekyears().isPrecise());
        assertEquals(true, CopticChronology.getInstanceUTC().months().isPrecise());
        assertEquals(true, CopticChronology.getInstanceUTC().weeks().isPrecise());
        assertEquals(true, CopticChronology.getInstanceUTC().days().isPrecise());
        assertEquals(true, CopticChronology.getInstanceUTC().hours().isPrecise());
        assertEquals(true, CopticChronology.getInstanceUTC().minutes().isPrecise());
        assertEquals(true, CopticChronology.getInstanceUTC().seconds().isPrecise());
        assertEquals(true, CopticChronology.getInstanceUTC().millis().isPrecise());
    }

    public void testDateFields() {
        assertEquals("era", CopticChronology.getInstance().era().getName());
        assertEquals("centuryOfEra", CopticChronology.getInstance().centuryOfEra().getName());
        assertEquals("yearOfCentury", CopticChronology.getInstance().yearOfCentury().getName());
        assertEquals("yearOfEra", CopticChronology.getInstance().yearOfEra().getName());
        assertEquals("year", CopticChronology.getInstance().year().getName());
        assertEquals("monthOfYear", CopticChronology.getInstance().monthOfYear().getName());
        assertEquals("weekyear", CopticChronology.getInstance().weekyear().getName());
        assertEquals("weekOfWeekyear", CopticChronology.getInstance().weekOfWeekyear().getName());
        assertEquals("dayOfYear", CopticChronology.getInstance().dayOfYear().getName());
        assertEquals("dayOfMonth", CopticChronology.getInstance().dayOfMonth().getName());
        assertEquals("dayOfWeek", CopticChronology.getInstance().dayOfWeek().getName());
        
        assertEquals(true, CopticChronology.getInstance().era().isSupported());
        assertEquals(true, CopticChronology.getInstance().centuryOfEra().isSupported());
        assertEquals(true, CopticChronology.getInstance().yearOfCentury().isSupported());
        assertEquals(true, CopticChronology.getInstance().yearOfEra().isSupported());
        assertEquals(true, CopticChronology.getInstance().year().isSupported());
        assertEquals(true, CopticChronology.getInstance().monthOfYear().isSupported());
        assertEquals(true, CopticChronology.getInstance().weekyear().isSupported());
        assertEquals(true, CopticChronology.getInstance().weekOfWeekyear().isSupported());
        assertEquals(true, CopticChronology.getInstance().dayOfYear().isSupported());
        assertEquals(true, CopticChronology.getInstance().dayOfMonth().isSupported());
        assertEquals(true, CopticChronology.getInstance().dayOfWeek().isSupported());
    }

    public void testTimeFields() {
        assertEquals("halfdayOfDay", CopticChronology.getInstance().halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", CopticChronology.getInstance().clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", CopticChronology.getInstance().hourOfHalfday().getName());
        assertEquals("clockhourOfDay", CopticChronology.getInstance().clockhourOfDay().getName());
        assertEquals("hourOfDay", CopticChronology.getInstance().hourOfDay().getName());
        assertEquals("minuteOfDay", CopticChronology.getInstance().minuteOfDay().getName());
        assertEquals("minuteOfHour", CopticChronology.getInstance().minuteOfHour().getName());
        assertEquals("secondOfDay", CopticChronology.getInstance().secondOfDay().getName());
        assertEquals("secondOfMinute", CopticChronology.getInstance().secondOfMinute().getName());
        assertEquals("millisOfDay", CopticChronology.getInstance().millisOfDay().getName());
        assertEquals("millisOfSecond", CopticChronology.getInstance().millisOfSecond().getName());
        
        assertEquals(true, CopticChronology.getInstance().halfdayOfDay().isSupported());
        assertEquals(true, CopticChronology.getInstance().clockhourOfHalfday().isSupported());
        assertEquals(true, CopticChronology.getInstance().hourOfHalfday().isSupported());
        assertEquals(true, CopticChronology.getInstance().clockhourOfDay().isSupported());
        assertEquals(true, CopticChronology.getInstance().hourOfDay().isSupported());
        assertEquals(true, CopticChronology.getInstance().minuteOfDay().isSupported());
        assertEquals(true, CopticChronology.getInstance().minuteOfHour().isSupported());
        assertEquals(true, CopticChronology.getInstance().secondOfDay().isSupported());
        assertEquals(true, CopticChronology.getInstance().secondOfMinute().isSupported());
        assertEquals(true, CopticChronology.getInstance().millisOfDay().isSupported());
        assertEquals(true, CopticChronology.getInstance().millisOfSecond().isSupported());
    }

}
