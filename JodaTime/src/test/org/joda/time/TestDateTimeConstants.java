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
package org.joda.time;

import junit.framework.TestSuite;

import org.joda.test.time.BulkTest;

/**
 * Test case.
 *
 * @author Stephen Colebourne
 */
public class TestDateTimeConstants extends BulkTest {

    /**
     * The main method for this test program.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * TestSuite is a junit required method.
     */
    public static TestSuite suite() {
        return BulkTest.makeSuite(TestDateTimeConstants.class);
    }

    /**
     * TestDateTimeComparator constructor.
     * @param name
     */
    public TestDateTimeConstants(String name) {
        super(name);
    }

    //-----------------------------------------------------------------------
    public void testConstructor() {
        DateTimeConstants c = new DateTimeConstants() {
        };
        c.toString();
    }

    public void testHalfdaysOfDay() {
        assertEquals(0, DateTimeConstants.AM);
        assertEquals(1, DateTimeConstants.PM);
    }

    public void testDaysOfWeek() {
        assertEquals(1, DateTimeConstants.MONDAY);
        assertEquals(2, DateTimeConstants.TUESDAY);
        assertEquals(3, DateTimeConstants.WEDNESDAY);
        assertEquals(4, DateTimeConstants.THURSDAY);
        assertEquals(5, DateTimeConstants.FRIDAY);
        assertEquals(6, DateTimeConstants.SATURDAY);
        assertEquals(7, DateTimeConstants.SUNDAY);
    }

    public void testMonthsOfYear() {
        assertEquals(1, DateTimeConstants.JANUARY);
        assertEquals(2, DateTimeConstants.FEBRUARY);
        assertEquals(3, DateTimeConstants.MARCH);
        assertEquals(4, DateTimeConstants.APRIL);
        assertEquals(5, DateTimeConstants.MAY);
        assertEquals(6, DateTimeConstants.JUNE);
        assertEquals(7, DateTimeConstants.JULY);
        assertEquals(8, DateTimeConstants.AUGUST);
        assertEquals(9, DateTimeConstants.SEPTEMBER);
        assertEquals(10, DateTimeConstants.OCTOBER);
        assertEquals(11, DateTimeConstants.NOVEMBER);
        assertEquals(12, DateTimeConstants.DECEMBER);
    }

    public void testEras() {
        assertEquals(0, DateTimeConstants.BC);
        assertEquals(0, DateTimeConstants.BCE);
        assertEquals(1, DateTimeConstants.AD);
        assertEquals(1, DateTimeConstants.CE);
    }

    public void testMaths() {
        assertEquals(1000, DateTimeConstants.MILLIS_PER_SECOND);
        assertEquals(60 * 1000, DateTimeConstants.MILLIS_PER_MINUTE);
        assertEquals(60 * 60 * 1000, DateTimeConstants.MILLIS_PER_HOUR);
        assertEquals(24 * 60 * 60 * 1000, DateTimeConstants.MILLIS_PER_DAY);
        assertEquals(7 * 24 * 60 * 60 * 1000, DateTimeConstants.MILLIS_PER_WEEK);
        
        assertEquals(60, DateTimeConstants.SECONDS_PER_MINUTE);
        assertEquals(60 * 60, DateTimeConstants.SECONDS_PER_HOUR);
        assertEquals(24 * 60 * 60, DateTimeConstants.SECONDS_PER_DAY);
        assertEquals(7 * 24 * 60 * 60, DateTimeConstants.SECONDS_PER_WEEK);
        
        assertEquals(60, DateTimeConstants.MINUTES_PER_HOUR);
        assertEquals(24 * 60, DateTimeConstants.MINUTES_PER_DAY);
        assertEquals(7 * 24 * 60, DateTimeConstants.MINUTES_PER_WEEK);
        
        assertEquals(24, DateTimeConstants.HOURS_PER_DAY);
        assertEquals(7 * 24, DateTimeConstants.HOURS_PER_WEEK);
        
        assertEquals(7, DateTimeConstants.DAYS_PER_WEEK);
    }

}
