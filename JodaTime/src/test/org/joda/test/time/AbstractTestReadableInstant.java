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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.ReadableInstant;
import org.joda.time.chrono.iso.ISOChronology;
/**
 * This class is a Junit unit test base class for
 * ReadableInstant implementations.
 *
 * @author Stephen Colebourne
 * @author Guy Allard
 */
public abstract class AbstractTestReadableInstant extends TestCase {

    private SimpleDateFormat dateTimeFormat;
    private final Class cls;
    private final Class otherClass;
    
    private TimeZone storeTimeZone = null;

    /**
     * Constructor.
     * @param name
     */
    public AbstractTestReadableInstant(String name, Class cls) {
        super(name);
        this.cls = cls;
        if (cls == Instant.class) {
            otherClass = DateTime.class;
        } else {
            otherClass = Instant.class;
        }
    }
    /**
     * Junit <code>setUp()</code> method.
     */
    protected void setUp() /* throws Exception */ {
        dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
        storeTimeZone = TimeZone.getDefault();
        // this way we don't hide time zone problems during UK winter
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+04:00"));
    }

    /**
     * Junit <code>tearDown()</code> method.
     */
    protected void tearDown() /* throws Exception */ {
        dateTimeFormat = null;
        TimeZone.setDefault(storeTimeZone);
    }

    /**
     * Create a ReadableInstant by reflection
     */
    protected ReadableInstant create(Class reflectClass, Class[] types, Object[] args) throws Throwable {
        try {
            Constructor con = reflectClass.getConstructor(types);
            return (ReadableInstant) con.newInstance(args);

        } catch (InvocationTargetException ex) {
            throw ex.getTargetException();
        } catch (NoSuchMethodException ex) {
            if (types == null || types.length == 0) {
                types = new Class[] {Chronology.class};
                args = new Object[] {ISOChronology.getInstance(DateTimeZone.getDefault())};
            }
            if (types.length == 1 && types[0] == Long.TYPE) {
                types = new Class[] {Long.TYPE, Chronology.class};
                args = new Object[] {args[0], ISOChronology.getInstance(DateTimeZone.getDefault())};
            }
            try {
                Constructor con = reflectClass.getConstructor(types);
                return (ReadableInstant) con.newInstance(args);
    
            } catch (InvocationTargetException ex2) {
                throw ex2.getTargetException();
            }
        }
    }
    
    protected abstract ReadableInstant createSmall(boolean ofAnotherClass);
    protected abstract ReadableInstant createMid(boolean ofAnotherClass);
    protected abstract ReadableInstant createLarge(boolean ofAnotherClass);
    protected abstract ReadableInstant createUTC(long millis);
    
    protected boolean isDateOnly() {
        return false;
    }

    protected boolean isTimeOnly() {
        return false;
    }

    /**
     * Round the millis
     * @param currentMillis
     * @return long
     */
    protected long round(long currentMillis, TimeZone zone) {
        if (zone == null) {
            zone = TimeZone.getDefault();
        }
        if (isDateOnly()) {
            GregorianCalendar cal = new GregorianCalendar(zone);
            cal.setTime(new Date(currentMillis));
            cal.set(GregorianCalendar.AM_PM, GregorianCalendar.AM);
            cal.set(GregorianCalendar.HOUR, 0);
            cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
            cal.set(GregorianCalendar.MINUTE, 0);
            cal.set(GregorianCalendar.SECOND, 0);
            cal.set(GregorianCalendar.MILLISECOND, 0);
            return cal.getTime().getTime();
        } else if (isTimeOnly()) {
            GregorianCalendar cal = new GregorianCalendar(zone);
            cal.setTime(new Date(currentMillis));
            cal.set(GregorianCalendar.YEAR, 1970);
            cal.set(GregorianCalendar.MONTH, 0);
            cal.set(GregorianCalendar.DAY_OF_MONTH, 1);
            return cal.getTime().getTime();
        }
        return currentMillis;
    }

    //----------------------------------------------------------------------------

    /**
     * Test getMillis
     */
    public void testGetMillis() throws Throwable {
        ReadableInstant ri1 = createUTC(0);
        assertEquals(0, ri1.getMillis());
    }

    //----------------------------------------------------------------------------

    /**
     * Test getChronology
     */
    public void testGetChronology() throws Throwable {
        ReadableInstant ri1 = create(cls, new Class[] {Long.TYPE}, new Object[] {new Long(-300)});
        assertNotNull(ri1.getChronology());
    }

    //----------------------------------------------------------------------------

    /**
     * Test get
     */
    public void testGetField() throws Throwable {
        ReadableInstant ri1 = create(cls, new Class[] {Long.TYPE}, new Object[] {new Long(1000)});
        if (isDateOnly()) {
            assertEquals(0, ri1.get(ISOChronology.getInstance(DateTimeZone.getDefault()).secondOfMinute()));
        } else {
            assertEquals(1, ri1.get(ISOChronology.getInstance(DateTimeZone.getDefault()).secondOfMinute()));
        }
        assertEquals(0, ri1.get(ISOChronology.getInstance(DateTimeZone.getDefault()).minuteOfHour()));
    }

    /**
     * Test get
     */
    public void testGetFieldEx() throws Throwable {
        ReadableInstant ri1 = create(cls, new Class[] {Long.TYPE}, new Object[] {new Long(1000)});
        try {
            ri1.get(null);
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }
    
    //----------------------------------------------------------------------------
        
    /**
     * Test equals
     */
    public void testEquals() throws Throwable {
        ReadableInstant ri1 = createSmall(false);
        ReadableInstant ri2 = createMid(false);
        ReadableInstant ri3 = createLarge(false);
        assertTrue(ri1.equals(ri1) == true);
        assertTrue(ri1.equals(ri2) == false);
        assertTrue(ri1.equals(ri3) == false);
        
        assertTrue(ri2.equals(ri1) == false);
        assertTrue(ri2.equals(ri2) == true);
        assertTrue(ri2.equals(ri3) == false);
        
        assertTrue(ri3.equals(ri1) == false);
        assertTrue(ri3.equals(ri2) == false);
        assertTrue(ri3.equals(ri3) == true);
        
        ReadableInstant ri4 = createSmall(true);
        assertTrue(ri2.equals(ri4) == false);
        ri4 = createMid(true);
        assertTrue(ri2.equals(ri4) == false);  // different chronology
        ri4 = createLarge(true);
        assertTrue(ri2.equals(ri4) == false);

        assertTrue(ri2.equals(null) == false);
        assertTrue(ri2.equals(new Integer(8)) == false);
    }
    
    //----------------------------------------------------------------------------
        
    /**
     * Test hashCode
     */
    public void testHashCode() throws Throwable {
        ReadableInstant ri1 = createSmall(false);
        ReadableInstant ri2 = createMid(false);
        ReadableInstant ri3 = create(cls, new Class[] {Long.TYPE}, new Object[] {new Long(200000000L)});
        assertEquals(ri1.hashCode(), ri1.hashCode());
        ReadableInstant ri4 = createSmall(false);
        assertEquals(ri1.hashCode(), ri4.hashCode());
    }
    
    //----------------------------------------------------------------------------
        
    /**
     * Test compareTo
     */
    public void testCompareTo() throws Throwable {
        ReadableInstant ri1 = createSmall(false);
        ReadableInstant ri2 = createMid(false);
        ReadableInstant ri3 = createLarge(false);
        assertTrue(ri1.compareTo(ri1) == 0);
        assertTrue(ri1.compareTo(ri2) < 0);
        assertTrue(ri1.compareTo(ri3) < 0);
        
        assertTrue(ri2.compareTo(ri1) > 0);
        assertTrue(ri2.compareTo(ri2) == 0);
        assertTrue(ri2.compareTo(ri3) < 0);
        
        assertTrue(ri3.compareTo(ri1) > 0);
        assertTrue(ri3.compareTo(ri2) > 0);
        assertTrue(ri3.compareTo(ri3) == 0);
        
        ReadableInstant ri4 = createSmall(true);
        assertTrue(ri2.compareTo(ri4) > 0);
        ri4 = createMid(true);
        assertTrue(ri2.compareTo(ri4) == 0);
        ri4 = createLarge(true);
        assertTrue(ri2.compareTo(ri4) < 0);
    }
    
    /**
     * Test compareTo
     */
    public void testCompareToEx1() throws Throwable {
        ReadableInstant ri = createSmall(false);
        try {
            ri.compareTo(null);
        } catch (NullPointerException ex) {
            return;
        }
        fail();
    }

    /**
     * Test compareTo
     */
    public void testCompareToEx2() throws Throwable {
        ReadableInstant ri = createSmall(false);
        try {
            ri.compareTo(new Integer(2));
        } catch (ClassCastException ex) {
            return;
        }
        fail();
    }
    
    //----------------------------------------------------------------------------
        
    /**
     * Test IsAfter
     */
    public void testIsAfter() throws Throwable {
        ReadableInstant ri1 = createSmall(false);
        ReadableInstant ri2 = createMid(false);
        ReadableInstant ri3 = createLarge(false);
        assertTrue(ri1.isAfter(ri1) == false);
        assertTrue(ri1.isAfter(ri2) == false);
        assertTrue(ri1.isAfter(ri3) == false);
        
        assertTrue(ri2.isAfter(ri1) == true);
        assertTrue(ri2.isAfter(ri2) == false);
        assertTrue(ri2.isAfter(ri3) == false);

        assertTrue(ri3.isAfter(ri1) == true);
        assertTrue(ri3.isAfter(ri2) == true);
        assertTrue(ri3.isAfter(ri3) == false);

        ReadableInstant ri4 = createSmall(true);
        assertTrue(ri2.isAfter(ri4) == true);
        ri4 = createMid(true);
        assertTrue(ri2.isAfter(ri4) == false);
        ri4 = createLarge(true);
        assertTrue(ri2.isAfter(ri4) == false);
    }

    /**
     * Test IsAfter
     */
    public void testIsAfterEx() throws Throwable {
        ReadableInstant ri = createSmall(false);
        try {
            ri.isAfter(null);
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    //----------------------------------------------------------------------------

    /**
     * Test IsBefore
     */
    public void testIsBefore() throws Throwable {
        ReadableInstant ri1 = createSmall(false);
        ReadableInstant ri2 = createMid(false);
        ReadableInstant ri3 = createLarge(false);
        assertTrue(ri1.isBefore(ri1) == false);
        assertTrue(ri1.isBefore(ri2) == true);
        assertTrue(ri1.isBefore(ri3) == true);

        assertTrue(ri2.isBefore(ri1) == false);
        assertTrue(ri2.isBefore(ri2) == false);
        assertTrue(ri2.isBefore(ri3) == true);

        assertTrue(ri3.isBefore(ri1) == false);
        assertTrue(ri3.isBefore(ri2) == false);
        assertTrue(ri3.isBefore(ri3) == false);

        ReadableInstant ri4 = createSmall(true);
        assertTrue(ri2.isBefore(ri4)  == false);
        ri4 = createMid(true);
        assertTrue(ri2.isBefore(ri4) == false);
        ri4 = createLarge(true);
        assertTrue(ri2.isBefore(ri4) == true);
    }

    /**
     * Test IsBefore
     */
    public void testIsBeforeEx() throws Throwable {
        ReadableInstant ri = createSmall(false);
        try {
            ri.isBefore(null);
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    //----------------------------------------------------------------------------

    /**
     * Test isEqual
     */
    public void testIsEqual() throws Throwable {
        ReadableInstant ri1 = createSmall(false);
        ReadableInstant ri2 = createMid(false);
        ReadableInstant ri3 = createLarge(false);
        assertTrue(ri1.isEqual(ri1) == true);
        assertTrue(ri1.isEqual(ri2) == false);
        assertTrue(ri1.isEqual(ri3) == false);

        assertTrue(ri2.isEqual(ri1) == false);
        assertTrue(ri2.isEqual(ri2) == true);
        assertTrue(ri2.isEqual(ri3) == false);

        assertTrue(ri3.isEqual(ri1) == false);
        assertTrue(ri3.isEqual(ri2) == false);
        assertTrue(ri3.isEqual(ri3) == true);

        ReadableInstant ri4 = createSmall(true);
        assertTrue(ri2.isEqual(ri4) == false);
        ri4 = createMid(true);
        assertTrue(ri2.isEqual(ri4) == true);
        ri4 = createLarge(true);
        assertTrue(ri2.isEqual(ri4) == false);
    }

    /**
     * Test isEqual
     */
    public void testIsEqualEx() throws Throwable {
        ReadableInstant ri = createSmall(false);
        try {
            ri.isEqual(null);
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    //----------------------------------------------------------------------------

    /**
     * Test toInstant
     */
    public void testToInstant() throws Throwable {
        ReadableInstant ri = create(cls, null, null);
        Instant instant = ri.toInstant();
        assertEquals(instant.getMillis(), ri.getMillis());
    }


}
