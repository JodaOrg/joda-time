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
package org.joda.test.time;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestSuite;

import org.joda.time.AbstractInstant;
import org.joda.time.DateTimeZone;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.ReadableInstant;
/**
 * This class is a Junit unit test for the
 * Instant date time class.
 *
 * @author Stephen Colebourne
 * @author Guy Allard
 */
public class TestInstant extends AbstractTestAbstractInstant {

    private static Class cls = Instant.class;
    
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
        return BulkTest.makeSuite(TestInstant.class);
    }
    /**
     * TestDateTimeField constructor.
     * @param name
     */
    public TestInstant(String name) {
        super(name, Instant.class);
    }
    
    /**
     * Test the CTOR with signature: ().
     */
    public void testCurrentTimeConstructor() throws Throwable {
        long currentMillis = System.currentTimeMillis();
        ReadableInstant ri = create(cls, null, null);
        assertTrue(ri.getMillis() - currentMillis < 100);
    }

    /**
     * Test the CTOR with signature: (long).
     */
    public void testMillisConstructor() throws Throwable {
        long currentMillis = System.currentTimeMillis() + 1000;
        ReadableInstant ri = create(cls, new Class[] {Long.TYPE}, new Object[] {new Long(currentMillis)});
        assertEquals(currentMillis, ri.getMillis());
    }

    /**
     * Test the CTOR with signature: (ReadableInstant).
     */
    public void testReadableInstantConstructor() throws Throwable {
        ReadableInstant instant = create(Instant.class, null, null);
        ReadableInstant ri = create(cls, new Class[] {ReadableInstant.class}, new Object[] {instant});
        assertEquals(instant.getMillis(), ri.getMillis());
    }

    /**
     * Test the CTOR with signature: (ReadableInstant), null RI.
     */
    public void testReadableInstantConstructorEx() throws Throwable {
        try {
            create(cls, new Class[] {ReadableInstant.class}, new Object[] {null});
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    /**
     * Test the CTOR with signature: (Date).
     */
    public void testDateConstructor() throws Throwable {
        Date date = new Date();
        ReadableInstant ri = create(cls, new Class[] {Date.class}, new Object[] {date});
        assertEquals(date.getTime(), ri.getMillis());
    }

    /**
     * Test the CTOR with signature: (Date), null date.
     */
    public void testDateConstructorEx() throws Throwable {
        try {
            create(cls, new Class[] {Date.class}, new Object[] {null});
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    /**
     * Test the CTOR with signature: (Calendar).
     */
    public void testCalendarConstructor() throws Throwable {
        Calendar cal = Calendar.getInstance();
        ReadableInstant ri = create(cls, new Class[] {Calendar.class}, new Object[] {cal});
        assertEquals(cal.getTime().getTime(), ri.getMillis());
    }

    /**
     * Test the CTOR with signature: (Calendar), null calendar.
     */
    public void testCalendarConstructorEx() throws Throwable {
        try {
            create(cls, new Class[] {Calendar.class}, new Object[] {null});
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    /**
     * Test the CTOR with signature: (String).
     */
    public void testStringConstructor() throws Throwable {
        ReadableInstant ri = create(cls, new Class[] {String.class}, new Object[] {"1970-01-01T13:02:03.004Z"});
        assertEquals(4 + 3*1000 + 2*1000*60 + 13*1000*60*60, ri.getMillis());
        // TODO: Merge in proper ISO testing from TestConstructors
    }

    /**
     * Test the CTOR with signature: (String), null string.
     */
    public void testStringConstructorEx() throws Throwable {
        try {
            create(cls, new Class[] {String.class}, new Object[] {null});
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }
    
    //----------------------------------------------------------------------------

    /**
     * Test getChronology
     */
    public void testGetChronology() throws Throwable {
        ReadableInstant ri1 = create(cls, new Class[] {Long.TYPE}, new Object[] {new Long(-300)});
        assertNull(ri1.getChronology());
    }

    //----------------------------------------------------------------------------

    /**
     * Test toInstant
     */
    public void testToInstant() throws Throwable {
        ReadableInstant ri = create(cls, null, null);
        Instant instant = ri.toInstant();
        assertSame(instant, ri);
    }

    /**
     * Test toString
     */
    public void testToString() throws Throwable {
        ReadableInstant ri = create(cls, new Class[] {Long.TYPE}, new Object[] {new Long(4 + 3*1000 + 2*1000*60 + 13*1000*60*60)});
        assertEquals("1970-01-01T13:02:03.004Z", ri.toString());
    }

    /**
     * @see org.joda.test.time.AbstractTestReadableInstant#createLarge(boolean)
     */
    protected ReadableInstant createLarge(boolean ofAnotherClass) {
        if (ofAnotherClass) {
            return new DateTime(400, DateTimeZone.UTC);
        }
        return new Instant(400);
    }

    /**
     * @see org.joda.test.time.AbstractTestReadableInstant#createMid(boolean)
     */
    protected ReadableInstant createMid(boolean ofAnotherClass) {
        if (ofAnotherClass) {
            return new DateTime(100, DateTimeZone.UTC);
        }
        return new Instant(100);
    }

    /**
     * @see org.joda.test.time.AbstractTestReadableInstant#createSmall(boolean)
     */
    protected ReadableInstant createSmall(boolean ofAnotherClass) {
        if (ofAnotherClass) {
            return new DateTime(-300, DateTimeZone.UTC);
        }
        return new Instant(-300);
    }

    protected ReadableInstant createUTC(long millis) {
        return new Instant(millis);
    }

}
