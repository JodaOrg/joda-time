/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-03 Stephen Colebourne.
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
package org.joda.test.time;

import java.util.GregorianCalendar;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.buddhist.BuddhistChronology;

import junit.framework.TestSuite;
/**
 * This class is a Junit unit test for the
 * BuddhistChronology date time class.
 *
 * @author Stephen Colebourne
 */
public class TestBuddhistChronology extends BulkTest {
    // TODO: These are not TimeZone safe
    private static final long SMALL_MILLIS = new GregorianCalendar(-20000, 0, 1).getTime().getTime();
    private static final long LARGE_MILLIS = new GregorianCalendar(20000, 0, 1).getTime().getTime();
    private static final long MILLIS_1971 = new GregorianCalendar(1971, 0, 1).getTime().getTime();
    private static final long MILLIS_1970 = new GregorianCalendar(1970, 0, 1).getTime().getTime();
    private static final long MILLIS_1969 = new GregorianCalendar(1969, 0, 1).getTime().getTime();
    
    private BuddhistChronology iChrono = null;

    /**
     * This is the main class for this test suite.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    /**
     * TestSuite is a junit required method.
     */
    public static TestSuite suite() {
        return BulkTest.makeSuite(TestBuddhistChronology.class);
    }
    /**
     * TestDateTimeField constructor.
     * @param name
     */
    public TestBuddhistChronology(String name) {
        super(name);
    }

    /**
     * Junit <code>setUp()</code> method.
     */
    protected void setUp() throws Exception {
        super.setUp();
        iChrono = BuddhistChronology.getInstanceUTC();
    }
    /**
     * Junit <code>tearDown()</code> method.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testEraConstant() throws Exception {
        assertEquals(DateTimeConstants.CE, BuddhistChronology.BE);
    }
    public void testGetInstanceUTC() throws Exception {
        assertNotNull(BuddhistChronology.getInstanceUTC());
        assertTrue(BuddhistChronology.getInstanceUTC() instanceof BuddhistChronology);
        assertSame(BuddhistChronology.getInstanceUTC(), BuddhistChronology.getInstanceUTC());
    }
    public void testGetInstanceNull() throws Exception {
        DateTimeZone zone = DateTimeZone.getDefault();
        assertNotNull(BuddhistChronology.getInstance());
        assertTrue(BuddhistChronology.getInstance() instanceof BuddhistChronology);
        assertSame(zone, BuddhistChronology.getInstance().getDateTimeZone());
    }
    public void testGetInstanceZone() throws Exception {
        DateTimeZone zone = DateTimeZone.getInstance("+01:00");
        assertNotNull(BuddhistChronology.getInstance(zone));
        assertTrue(BuddhistChronology.getInstance(zone) instanceof BuddhistChronology);
        assertSame(zone, BuddhistChronology.getInstance(zone).getDateTimeZone());
    }
    public void testGetInstanceZoneUTC() throws Exception {
        assertSame(BuddhistChronology.getInstanceUTC(), BuddhistChronology.getInstance(DateTimeZone.UTC));
    }
    
    public void testMillisOfSecond() {
        // TODO: Its the same as GJChronology, so checking it isn't that important
    }
    public void testMillisOfDay() {
        // TODO: Its the same as GJChronology, so checking it isn't that important
    }
    public void testSecondOfMinute() {
        // TODO: Its the same as GJChronology, so checking it isn't that important
    }
    public void testSecondOfDay() {
        // TODO: Its the same as GJChronology, so checking it isn't that important
    }
    public void testMinuteOfHour() {
        // TODO: Its the same as GJChronology, so checking it isn't that important
    }
    public void testMinuteOfDay() {
        // TODO: Its the same as GJChronology, so checking it isn't that important
    }
    public void testHourOfDay() {
        // TODO: Its the same as GJChronology, so checking it isn't that important
    }
    public void testClockhourOfDay() {
        // TODO: Its the same as GJChronology, so checking it isn't that important
    }
    public void testHourOfHalfday() {
        // TODO: Its the same as GJChronology, so checking it isn't that important
    }
    public void testClockhourOfHalfday() {
        // TODO: Its the same as GJChronology, so checking it isn't that important
    }
    public void testAmPm() {
        // TODO: Its the same as GJChronology, so checking it isn't that important
    }
    public void testDayOfWeek() {
        // TODO: Its the same as GJChronology, so checking it isn't that important
    }
    public void testDayOfMonth() {
        // TODO: Its the same as GJChronology, so checking it isn't that important
    }
    public void testDayOfYear() {
        // TODO: Its the same as GJChronology, so checking it isn't that important
    }
    public void testWeekOfYearWeek() {
        // TODO: Its the same as GJChronology, so checking it isn't that important
    }
    public void testMonthOfYear() {
        // TODO: Its the same as GJChronology, so checking it isn't that important
    }
    public void testLeapYear() {
        // TODO: Its the same as GJChronology, so checking it isn't that important
    }
    
    public void testWeekOfYearYear() {
        fail("TBD");
    }
    public void testYear() {
        assertEquals("year", iChrono.year().getName());
        assertSame(iChrono.year(), iChrono.year());
        
        assertEquals(1970 + 543, iChrono.year().get(MILLIS_1970));
        assertEquals(20000 + 543, iChrono.year().get(LARGE_MILLIS));
        
        assertEquals(MILLIS_1970, iChrono.year().set(LARGE_MILLIS, 1970 + 543));
        
        assertEquals(MILLIS_1971, iChrono.year().add(MILLIS_1970, 1));
        assertEquals(MILLIS_1969, iChrono.year().add(MILLIS_1970, -1));
        
        assertEquals(MILLIS_1971, iChrono.year().addWrapped(MILLIS_1970, 1));
        assertEquals(MILLIS_1969, iChrono.year().addWrapped(MILLIS_1970, -1));
        
        assertEquals("2513", iChrono.year().getAsShortText(MILLIS_1970));
        assertEquals(9, iChrono.year().getMaximumShortTextLength(null));
        assertEquals("2513", iChrono.year().getAsText(MILLIS_1970));
        assertEquals(9, iChrono.year().getMaximumTextLength(null));
        
        assertEquals(1, iChrono.year().getMinimumValue());
        assertEquals(250000000 + 543, iChrono.year().getMaximumValue());
        
        assertEquals(MILLIS_1970, iChrono.year().set(MILLIS_1970, "2513"));
        assertEquals(MILLIS_1971, iChrono.year().set(MILLIS_1970, "2514"));
        assertEquals(LARGE_MILLIS, iChrono.year().set(MILLIS_1970, "20543"));
        try {
            iChrono.year().set(MILLIS_1970, null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            iChrono.year().set(MILLIS_1970, "AD");
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    public void testYearOfCentury() {
        try {
            iChrono.yearOfCentury();
            fail();
        } catch (UnsupportedOperationException ex) {}
    }
    public void testCenturyOfEra() {
        try {
            iChrono.centuryOfEra();
            fail();
        } catch (UnsupportedOperationException ex) {}
    }
    public void testEra() {
        assertEquals("era", iChrono.era().getName());
        assertSame(iChrono.era(), iChrono.era());
        
        assertEquals(BuddhistChronology.BE, iChrono.era().get(SMALL_MILLIS));
        assertEquals(BuddhistChronology.BE, iChrono.era().get(MILLIS_1970));
        assertEquals(BuddhistChronology.BE, iChrono.era().get(LARGE_MILLIS));
        
        assertEquals(SMALL_MILLIS, iChrono.era().set(SMALL_MILLIS, BuddhistChronology.BE));
        try {
            iChrono.era().set(SMALL_MILLIS, DateTimeConstants.BCE);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        try {
            iChrono.era().add(SMALL_MILLIS, 1);
            fail();
        } catch (UnsupportedOperationException ex) {}
        
        try {
            iChrono.era().addWrapped(SMALL_MILLIS, 1);
            fail();
        } catch (UnsupportedOperationException ex) {}
        
        assertEquals("BE", iChrono.era().getAsShortText(MILLIS_1970));
        assertEquals(2, iChrono.era().getMaximumShortTextLength(null));
        assertEquals("BE", iChrono.era().getAsText(MILLIS_1970));
        assertEquals(2, iChrono.era().getMaximumTextLength(null));
        
        assertEquals(BuddhistChronology.BE, iChrono.era().getMinimumValue());
        assertEquals(BuddhistChronology.BE, iChrono.era().getMaximumValue());
        
        assertEquals(MILLIS_1970, iChrono.era().set(MILLIS_1970, "BE"));
        try {
            iChrono.era().set(SMALL_MILLIS, null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            iChrono.era().set(SMALL_MILLIS, "AD");
            fail();
        } catch (IllegalArgumentException ex) {}
    }

}
