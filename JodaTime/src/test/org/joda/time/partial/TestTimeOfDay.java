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
package org.joda.time.partial;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.MockZeroNullIntegerConverter;

/**
 * This class is a Junit unit test for TimeOfDay.
 *
 * @author Stephen Colebourne
 */
public class TestTimeOfDay extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.getInstance("Europe/Paris");
    
    private long TEST_TIME_NOW =
            10L * DateTimeConstants.MILLIS_PER_HOUR
            + 20L * DateTimeConstants.MILLIS_PER_MINUTE
            + 30L * DateTimeConstants.MILLIS_PER_SECOND
            + 40L;
            
    private long TEST_TIME1 =
        1L * DateTimeConstants.MILLIS_PER_HOUR
        + 2L * DateTimeConstants.MILLIS_PER_MINUTE
        + 3L * DateTimeConstants.MILLIS_PER_SECOND
        + 4L;
        
    private long TEST_TIME2 =
        1L * DateTimeConstants.MILLIS_PER_DAY
        + 5L * DateTimeConstants.MILLIS_PER_HOUR
        + 6L * DateTimeConstants.MILLIS_PER_MINUTE
        + 7L * DateTimeConstants.MILLIS_PER_SECOND
        + 8L;
        
    private DateTimeZone zone = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestTimeOfDay.class);
    }

    public TestTimeOfDay(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        zone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(zone);
        zone = null;
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor ()
     */
    public void testConstructor() throws Throwable {
        TimeOfDay test = new TimeOfDay();
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

    /**
     * Test constructor (Chronology)
     */
    public void testConstructor_Chronology() throws Throwable {
        TimeOfDay test = new TimeOfDay(JulianChronology.getInstance());
        assertEquals(JulianChronology.getInstance(), test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

    /**
     * Test constructor (Chronology=null)
     */
    public void testConstructor_nullChronology() throws Throwable {
        TimeOfDay test = new TimeOfDay((Chronology) null);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor (long)
     */
    public void testConstructor_long1() throws Throwable {
        TimeOfDay test = new TimeOfDay(TEST_TIME1);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(1, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    /**
     * Test constructor (long)
     */
    public void testConstructor_long2() throws Throwable {
        TimeOfDay test = new TimeOfDay(TEST_TIME2);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(5, test.getHourOfDay());
        assertEquals(6, test.getMinuteOfHour());
        assertEquals(7, test.getSecondOfMinute());
        assertEquals(8, test.getMillisOfSecond());
    }

    /**
     * Test constructor (long, Chronology)
     */
    public void testConstructor_long1_Chronology() throws Throwable {
        TimeOfDay test = new TimeOfDay(TEST_TIME1, JulianChronology.getInstance());
        assertEquals(JulianChronology.getInstance(), test.getChronology());
        assertEquals(1, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    /**
     * Test constructor (long, Chronology)
     */
    public void testConstructor_long2_Chronology() throws Throwable {
        TimeOfDay test = new TimeOfDay(TEST_TIME2, JulianChronology.getInstance());
        assertEquals(JulianChronology.getInstance(), test.getChronology());
        assertEquals(5, test.getHourOfDay());
        assertEquals(6, test.getMinuteOfHour());
        assertEquals(7, test.getSecondOfMinute());
        assertEquals(8, test.getMillisOfSecond());
    }

    /**
     * Test constructor (long, Chronology=null)
     */
    public void testConstructor_long_nullChronology() throws Throwable {
        TimeOfDay test = new TimeOfDay(TEST_TIME1, null);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(1, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor (Object)
     */
    public void testConstructor_Object() throws Throwable {
        Date date = new Date(TEST_TIME1);
        TimeOfDay test = new TimeOfDay(date);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(1, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    /**
     * Test constructor (Object=null)
     */
    public void testConstructor_nullObject() throws Throwable {
        TimeOfDay test = new TimeOfDay(null);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

    /**
     * Test constructor (Object=null)
     */
    public void testConstructor_badconverterObject() throws Throwable {
        try {
            ConverterManager.getInstance().addInstantConverter(MockZeroNullIntegerConverter.INSTANCE);
            TimeOfDay test = new TimeOfDay(new Integer(0));
            assertEquals(ISOChronology.getInstance(), test.getChronology());
            assertEquals(0, test.getHourOfDay());
            assertEquals(0, test.getMinuteOfHour());
            assertEquals(0, test.getSecondOfMinute());
            assertEquals(0, test.getMillisOfSecond());
        } finally {
            ConverterManager.getInstance().removeInstantConverter(MockZeroNullIntegerConverter.INSTANCE);
        }
    }

    /**
     * Test constructor (Object, Chronology)
     */
    public void testConstructor_Object_Chronology() throws Throwable {
        Date date = new Date(TEST_TIME1);
        TimeOfDay test = new TimeOfDay(date, JulianChronology.getInstance());
        assertEquals(JulianChronology.getInstance(), test.getChronology());
        assertEquals(1, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    /**
     * Test constructor (Object=null, Chronology)
     */
    public void testConstructor_nullObject_Chronology() throws Throwable {
        TimeOfDay test = new TimeOfDay((Object) null, JulianChronology.getInstance());
        assertEquals(JulianChronology.getInstance(), test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

    /**
     * Test constructor (Object, Chronology=null)
     */
    public void testConstructor_Object_nullChronology() throws Throwable {
        Date date = new Date(TEST_TIME1);
        TimeOfDay test = new TimeOfDay(date, null);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(1, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    /**
     * Test constructor (Object=null, Chronology=null)
     */
    public void testConstructor_nullObject_nullChronology() throws Throwable {
        TimeOfDay test = new TimeOfDay((Object) null, null);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

    /**
     * Test constructor (Object=null)
     */
    public void testConstructor_badconverterObject_Chronology() throws Throwable {
        try {
            ConverterManager.getInstance().addInstantConverter(MockZeroNullIntegerConverter.INSTANCE);
            TimeOfDay test = new TimeOfDay(new Integer(0), JulianChronology.getInstance());
            assertEquals(ISOChronology.getInstance(), test.getChronology());
            assertEquals(0, test.getHourOfDay());
            assertEquals(0, test.getMinuteOfHour());
            assertEquals(0, test.getSecondOfMinute());
            assertEquals(0, test.getMillisOfSecond());
        } finally {
            ConverterManager.getInstance().removeInstantConverter(MockZeroNullIntegerConverter.INSTANCE);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor (int, int)
     */
    public void testConstructor_int_int() throws Throwable {
        TimeOfDay test = new TimeOfDay(10, 20);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(0, test.getSecondOfMinute());
        assertEquals(0, test.getMillisOfSecond());
        try {
            new TimeOfDay(-1, 20);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(24, 20);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, -1);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, 60);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    /**
     * Test constructor (int, int, int, Chronology)
     */
    public void testConstructor_int_int_Chronology() throws Throwable {
        TimeOfDay test = new TimeOfDay(10, 20, JulianChronology.getInstance());
        assertEquals(JulianChronology.getInstance(), test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(0, test.getSecondOfMinute());
        assertEquals(0, test.getMillisOfSecond());
        try {
            new TimeOfDay(-1, 20, JulianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(24, 20, JulianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, -1, JulianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, 60, JulianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    /**
     * Test constructor (int, int, int, Chronology=null)
     */
    public void testConstructor_int_int_nullChronology() throws Throwable {
        TimeOfDay test = new TimeOfDay(10, 20, null);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(0, test.getSecondOfMinute());
        assertEquals(0, test.getMillisOfSecond());
    }

    /**
     * Test constructor (int, int, int)
     */
    public void testConstructor_int_int_int() throws Throwable {
        TimeOfDay test = new TimeOfDay(10, 20, 30);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(0, test.getMillisOfSecond());
        try {
            new TimeOfDay(-1, 20, 30);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(24, 20, 30);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, -1, 30);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, 60, 30);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, 20, -1);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, 20, 60);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    /**
     * Test constructor (int, int, int, Chronology)
     */
    public void testConstructor_int_int_int_Chronology() throws Throwable {
        TimeOfDay test = new TimeOfDay(10, 20, 30, JulianChronology.getInstance());
        assertEquals(JulianChronology.getInstance(), test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(0, test.getMillisOfSecond());
        try {
            new TimeOfDay(-1, 20, 30, JulianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(24, 20, 30, JulianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, -1, 30, JulianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, 60, 30, JulianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, 20, -1, JulianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, 20, 60, JulianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    /**
     * Test constructor (int, int, int, Chronology=null)
     */
    public void testConstructor_int_int_int_nullChronology() throws Throwable {
        TimeOfDay test = new TimeOfDay(10, 20, 30, null);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(0, test.getMillisOfSecond());
    }

    /**
     * Test constructor (int, int, int, int)
     */
    public void testConstructor_int_int_int_int() throws Throwable {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
        try {
            new TimeOfDay(-1, 20, 30, 40);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(24, 20, 30, 40);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, -1, 30, 40);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, 60, 30, 40);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, 20, -1, 40);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, 20, 60, 40);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, 20, 30, -1);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, 20, 30, 1000);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    /**
     * Test constructor (int, int, int, int, Chronology)
     */
    public void testConstructor_int_int_int_int_Chronology() throws Throwable {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40, JulianChronology.getInstance());
        assertEquals(JulianChronology.getInstance(), test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
        try {
            new TimeOfDay(-1, 20, 30, 40, JulianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(24, 20, 30, 40, JulianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, -1, 30, 40, JulianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, 60, 30, 40, JulianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, 20, -1, 40, JulianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, 20, 60, 40, JulianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, 20, 30, -1, JulianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new TimeOfDay(10, 20, 30, 1000, JulianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    /**
     * Test constructor (int, int, int, int, Chronology=null)
     */
    public void testConstructor_int_int_int_int_nullChronology() throws Throwable {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40, null);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    public void testGet() {
        TimeOfDay test = new TimeOfDay();
        assertEquals(10, test.get(ISOChronology.getInstance().hourOfDay()));
        assertEquals(20, test.get(ISOChronology.getInstance().minuteOfHour()));
        assertEquals(30, test.get(ISOChronology.getInstance().secondOfMinute()));
        assertEquals(40, test.get(ISOChronology.getInstance().millisOfSecond()));
        try {
            test.get(null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.get(ISOChronology.getInstance().dayOfMonth());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.get(ISOChronology.getInstance(PARIS).hourOfDay());
            fail();
        } catch (IllegalArgumentException ex) {}
        // TODO: Should this fail or suceed - by succeeding it exposes out implementation
//        try {
//            test.get(JulianChronology.getInstance().hourOfDay());
//            fail();
//        } catch (IllegalArgumentException ex) {}
    }

    public void testGetFieldSize() {
        TimeOfDay test = new TimeOfDay();
        assertEquals(4, test.getFieldSize());
    }

    public void testGetField() {
        TimeOfDay test = new TimeOfDay();
        assertSame(ISOChronology.getInstance().hourOfDay(), test.getField(0));
        assertSame(ISOChronology.getInstance().minuteOfHour(), test.getField(1));
        assertSame(ISOChronology.getInstance().secondOfMinute(), test.getField(2));
        assertSame(ISOChronology.getInstance().millisOfSecond(), test.getField(3));
        try {
            test.getField(-1);
        } catch (IllegalArgumentException ex) {}
        try {
            test.getField(5);
        } catch (IllegalArgumentException ex) {}
    }

    public void testGetFields() {
        TimeOfDay test = new TimeOfDay();
        DateTimeField[] fields = test.getFields();
        assertSame(ISOChronology.getInstance().hourOfDay(), fields[0]);
        assertSame(ISOChronology.getInstance().minuteOfHour(), fields[1]);
        assertSame(ISOChronology.getInstance().secondOfMinute(), fields[2]);
        assertSame(ISOChronology.getInstance().millisOfSecond(), fields[3]);
    }

    public void testGetValue() {
        TimeOfDay test = new TimeOfDay();
        assertEquals(10, test.getValue(0));
        assertEquals(20, test.getValue(1));
        assertEquals(30, test.getValue(2));
        assertEquals(40, test.getValue(3));
        try {
            test.getValue(-1);
        } catch (IllegalArgumentException ex) {}
        try {
            test.getValue(5);
        } catch (IllegalArgumentException ex) {}
    }

    public void testGetValues() {
        TimeOfDay test = new TimeOfDay();
        int[] values = test.getValues();
        assertEquals(10, values[0]);
        assertEquals(20, values[1]);
        assertEquals(30, values[2]);
        assertEquals(40, values[3]);
    }

    public void testIsSupported() {
        TimeOfDay test = new TimeOfDay();
        assertEquals(true, test.isSupported(ISOChronology.getInstance().hourOfDay()));
        assertEquals(true, test.isSupported(ISOChronology.getInstance().minuteOfHour()));
        assertEquals(true, test.isSupported(ISOChronology.getInstance().secondOfMinute()));
        assertEquals(true, test.isSupported(ISOChronology.getInstance().millisOfSecond()));
        assertEquals(false, test.isSupported(ISOChronology.getInstance().dayOfMonth()));
        assertEquals(false, test.isSupported(ISOChronology.getInstance(PARIS).hourOfDay()));
    }

    public void testEqualsHashCode() {
        TimeOfDay test1 = new TimeOfDay(10, 20, 30, 40);
        TimeOfDay test2 = new TimeOfDay(10, 20, 30, 40);
        assertEquals(true, test1.equals(test2));
        assertEquals(true, test2.equals(test1));
        assertEquals(true, test1.equals(test1));
        assertEquals(true, test2.equals(test2));
        assertEquals(true, test1.hashCode() == test2.hashCode());
        assertEquals(true, test1.hashCode() == test1.hashCode());
        assertEquals(true, test2.hashCode() == test2.hashCode());
        
        TimeOfDay test3 = new TimeOfDay(11, 20, 30, 40);
        assertEquals(false, test1.equals(test3));
        assertEquals(false, test2.equals(test3));
        assertEquals(false, test3.equals(test1));
        assertEquals(false, test3.equals(test2));
        assertEquals(false, test1.hashCode() == test3.hashCode());
        assertEquals(false, test2.hashCode() == test3.hashCode());
        
        assertEquals(false, test1.equals("Hello"));
        assertEquals(true, test1.equals(new MockInstant()));
        assertEquals(false, test1.equals(MockPartial.EMPTY_INSTANCE));
    }
    
    class MockInstant extends MockPartial {
        public DateTimeField[] getFields() {
            return new DateTimeField[] {
                ISOChronology.getInstance().hourOfDay(),
                ISOChronology.getInstance().minuteOfHour(),
                ISOChronology.getInstance().secondOfMinute(),
                ISOChronology.getInstance().millisOfSecond(),
            };
        }
        public int[] getValues() {
            return new int[] {10, 20, 30, 40};
        }
    }

    //-----------------------------------------------------------------------
    public void testResolve_long() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        DateTime dt = new DateTime(0L);
        assertEquals("1970-01-01T00:00:00.000Z", dt.toString());
        
        DateTime result = new DateTime(test.resolve(dt.getMillis(), DateTimeZone.UTC));
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
        assertEquals("1970-01-01T00:00:00.000Z", dt.toString());
        assertEquals("1970-01-01T10:20:30.040Z", result.toString());
    }

    public void testResolveDateTime_RI() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        DateTime dt = new DateTime(0L);
        assertEquals("1970-01-01T00:00:00.000Z", dt.toString());
        
        DateTime result = test.resolveDateTime(dt);
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
        assertEquals("1970-01-01T00:00:00.000Z", dt.toString());
        assertEquals("1970-01-01T10:20:30.040Z", result.toString());
    }

    public void testResolveDateTime_nullRI() {
        TimeOfDay test = new TimeOfDay(1, 2, 3, 4);
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME2);
        
        DateTime result = test.resolveDateTime(null);
        assertEquals(1, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
        assertEquals("1970-01-02T01:02:03.004Z", result.toString());
    }

    public void testResolveInto_RWI() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        MutableDateTime mdt = new MutableDateTime(0L);
        assertEquals("1970-01-01T00:00:00.000Z", mdt.toString());
        
        test.resolveInto(mdt);
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
        assertEquals("1970-01-01T10:20:30.040Z", mdt.toString());
    }

    public void testResolveInto_nullRWI() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        
        try {
            test.resolveInto(null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testSerialization() throws Exception {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(test);
        byte[] bytes = baos.toByteArray();
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        TimeOfDay result = (TimeOfDay) ois.readObject();
        ois.close();
        
        assertEquals(test, result);
        assertTrue(Arrays.equals(test.getValues(), result.getValues()));
        assertTrue(Arrays.equals(test.getFields(), result.getFields()));
        assertEquals(test.getChronology(), result.getChronology());
    }

    //-----------------------------------------------------------------------
    public void testToString() {
        // TODO
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetHour() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        assertSame(test.getChronology().hourOfDay(), test.hourOfDay().getField());
        assertEquals("hourOfDay", test.hourOfDay().getName());
        assertEquals("Property[hourOfDay]", test.hourOfDay().toString());
        assertSame(test, test.hourOfDay().getReadablePartial());
        assertSame(test, test.hourOfDay().getTimeOfDay());
        assertEquals(10, test.hourOfDay().get());
        assertEquals("10", test.hourOfDay().getAsText());
        assertEquals("10", test.hourOfDay().getAsText(Locale.FRENCH));
        assertEquals("10", test.hourOfDay().getAsShortText());
        assertEquals("10", test.hourOfDay().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().hours(), test.hourOfDay().getDurationField());
        assertEquals(test.getChronology().days(), test.hourOfDay().getRangeDurationField());
        assertEquals(2, test.hourOfDay().getMaximumTextLength(null));
        assertEquals(2, test.hourOfDay().getMaximumShortTextLength(null));
    }

    public void testPropertyGetMaxMinValuesHour() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        assertEquals(0, test.hourOfDay().getMinimumValue());
        assertEquals(0, test.hourOfDay().getMinimumValueOverall());
        assertEquals(23, test.hourOfDay().getMaximumValue());
        assertEquals(23, test.hourOfDay().getMaximumValueOverall());
    }

    public void testPropertyAddHour() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        TimeOfDay copy = test.hourOfDay().addCopy(9);
        check(test, 10, 20, 30, 40);
        check(copy, 19, 20, 30, 40);
        
        copy = test.hourOfDay().addCopy(0);
        check(copy, 10, 20, 30, 40);
        
        copy = test.hourOfDay().addCopy(13);
        check(copy, 23, 20, 30, 40);
        
        try {
            test.hourOfDay().addCopy(14);
            fail();
        } catch (IllegalArgumentException ex) {}
        check(test, 10, 20, 30, 40);
        
        copy = test.hourOfDay().addCopy(-10);
        check(copy, 0, 20, 30, 40);
        
        try {
            test.hourOfDay().addCopy(-11);
            fail();
        } catch (IllegalArgumentException ex) {}
        check(test, 10, 20, 30, 40);
    }

    public void testPropertyAddInFieldHour() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        TimeOfDay copy = test.hourOfDay().addInFieldCopy(9);
        check(test, 10, 20, 30, 40);
        check(copy, 19, 20, 30, 40);
        
        copy = test.hourOfDay().addInFieldCopy(0);
        check(copy, 10, 20, 30, 40);
        
        copy = test.hourOfDay().addInFieldCopy(18);
        check(copy, 4, 20, 30, 40);
        
        copy = test.hourOfDay().addInFieldCopy(-15);
        check(copy, 19, 20, 30, 40);
    }

    public void testPropertySetHour() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        TimeOfDay copy = test.hourOfDay().setCopy(12);
        check(test, 10, 20, 30, 40);
        check(copy, 12, 20, 30, 40);
        
        try {
            test.hourOfDay().setCopy(24);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.hourOfDay().setCopy(-1);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testPropertySetTextHour() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        TimeOfDay copy = test.hourOfDay().setCopy("12");
        check(test, 10, 20, 30, 40);
        check(copy, 12, 20, 30, 40);
    }

    public void testPropertyCompareToHour() {
        TimeOfDay test1 = new TimeOfDay(TEST_TIME1);
        TimeOfDay test2 = new TimeOfDay(TEST_TIME2);
        assertEquals(true, test1.hourOfDay().compareTo(test2) < 0);
        assertEquals(true, test2.hourOfDay().compareTo(test1) > 0);
        assertEquals(true, test1.hourOfDay().compareTo(test1) == 0);
        try {
            test1.hourOfDay().compareTo((ReadablePartial) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        DateTime dt1 = new DateTime(TEST_TIME1);
        DateTime dt2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.hourOfDay().compareTo(dt2) < 0);
        assertEquals(true, test2.hourOfDay().compareTo(dt1) > 0);
        assertEquals(true, test1.hourOfDay().compareTo(dt1) == 0);
        try {
            test1.hourOfDay().compareTo((ReadableInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetMinute() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        assertSame(test.getChronology().minuteOfHour(), test.minuteOfHour().getField());
        assertEquals("minuteOfHour", test.minuteOfHour().getName());
        assertEquals("Property[minuteOfHour]", test.minuteOfHour().toString());
        assertSame(test, test.minuteOfHour().getReadablePartial());
        assertSame(test, test.minuteOfHour().getTimeOfDay());
        assertEquals(20, test.minuteOfHour().get());
        assertEquals("20", test.minuteOfHour().getAsText());
        assertEquals("20", test.minuteOfHour().getAsText(Locale.FRENCH));
        assertEquals("20", test.minuteOfHour().getAsShortText());
        assertEquals("20", test.minuteOfHour().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().minutes(), test.minuteOfHour().getDurationField());
        assertEquals(test.getChronology().hours(), test.minuteOfHour().getRangeDurationField());
        assertEquals(2, test.minuteOfHour().getMaximumTextLength(null));
        assertEquals(2, test.minuteOfHour().getMaximumShortTextLength(null));
    }

    public void testPropertyGetMaxMinValuesMinute() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        assertEquals(0, test.minuteOfHour().getMinimumValue());
        assertEquals(0, test.minuteOfHour().getMinimumValueOverall());
        assertEquals(59, test.minuteOfHour().getMaximumValue());
        assertEquals(59, test.minuteOfHour().getMaximumValueOverall());
    }

    public void testPropertyAddMinute() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        TimeOfDay copy = test.minuteOfHour().addCopy(9);
        check(test, 10, 20, 30, 40);
        check(copy, 10, 29, 30, 40);
        
        copy = test.minuteOfHour().addCopy(39);
        check(copy, 10, 59, 30, 40);
        
        copy = test.minuteOfHour().addCopy(40);
        check(copy, 11, 0, 30, 40);
        
        copy = test.minuteOfHour().addCopy(1 * 60 + 45);
        check(copy, 12, 5, 30, 40);
        
        copy = test.minuteOfHour().addCopy(13 * 60 + 39);
        check(copy, 23, 59, 30, 40);
        
        try {
            test.minuteOfHour().addCopy(13 * 60 + 40);
            fail();
        } catch (IllegalArgumentException ex) {}
        check(test, 10, 20, 30, 40);
        
        copy = test.minuteOfHour().addCopy(-9);
        check(copy, 10, 11, 30, 40);
        
        copy = test.minuteOfHour().addCopy(-19);
        check(copy, 10, 1, 30, 40);
        
        copy = test.minuteOfHour().addCopy(-20);
        check(copy, 10, 0, 30, 40);
        
        copy = test.minuteOfHour().addCopy(-21);
        check(copy, 9, 59, 30, 40);
        
        copy = test.minuteOfHour().addCopy(-(10 * 60 + 20));
        check(copy, 0, 0, 30, 40);
        
        try {
            test.minuteOfHour().addCopy(-(10 * 60 + 21));
            fail();
        } catch (IllegalArgumentException ex) {}
        check(test, 10, 20, 30, 40);
    }

    public void testPropertyAddInFieldMinute() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        TimeOfDay copy = test.minuteOfHour().addInFieldCopy(9);
        check(test, 10, 20, 30, 40);
        check(copy, 10, 29, 30, 40);
        
        copy = test.minuteOfHour().addInFieldCopy(49);
        check(copy, 10, 9, 30, 40);
        
        copy = test.minuteOfHour().addInFieldCopy(-47);
        check(copy, 10, 33, 30, 40);
    }

    public void testPropertySetMinute() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        TimeOfDay copy = test.minuteOfHour().setCopy(12);
        check(test, 10, 20, 30, 40);
        check(copy, 10, 12, 30, 40);
        
        try {
            test.minuteOfHour().setCopy(60);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.minuteOfHour().setCopy(-1);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testPropertySetTextMinute() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        TimeOfDay copy = test.minuteOfHour().setCopy("12");
        check(test, 10, 20, 30, 40);
        check(copy, 10, 12, 30, 40);
    }

    public void testPropertyCompareToMinute() {
        TimeOfDay test1 = new TimeOfDay(TEST_TIME1);
        TimeOfDay test2 = new TimeOfDay(TEST_TIME2);
        assertEquals(true, test1.minuteOfHour().compareTo(test2) < 0);
        assertEquals(true, test2.minuteOfHour().compareTo(test1) > 0);
        assertEquals(true, test1.minuteOfHour().compareTo(test1) == 0);
        try {
            test1.minuteOfHour().compareTo((ReadablePartial) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        DateTime dt1 = new DateTime(TEST_TIME1);
        DateTime dt2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.minuteOfHour().compareTo(dt2) < 0);
        assertEquals(true, test2.minuteOfHour().compareTo(dt1) > 0);
        assertEquals(true, test1.minuteOfHour().compareTo(dt1) == 0);
        try {
            test1.minuteOfHour().compareTo((ReadableInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetSecond() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        assertSame(test.getChronology().secondOfMinute(), test.secondOfMinute().getField());
        assertEquals("secondOfMinute", test.secondOfMinute().getName());
        assertEquals("Property[secondOfMinute]", test.secondOfMinute().toString());
        assertSame(test, test.secondOfMinute().getReadablePartial());
        assertSame(test, test.secondOfMinute().getTimeOfDay());
        assertEquals(30, test.secondOfMinute().get());
        assertEquals("30", test.secondOfMinute().getAsText());
        assertEquals("30", test.secondOfMinute().getAsText(Locale.FRENCH));
        assertEquals("30", test.secondOfMinute().getAsShortText());
        assertEquals("30", test.secondOfMinute().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().seconds(), test.secondOfMinute().getDurationField());
        assertEquals(test.getChronology().minutes(), test.secondOfMinute().getRangeDurationField());
        assertEquals(2, test.secondOfMinute().getMaximumTextLength(null));
        assertEquals(2, test.secondOfMinute().getMaximumShortTextLength(null));
    }

    public void testPropertyGetMaxMinValuesSecond() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        assertEquals(0, test.secondOfMinute().getMinimumValue());
        assertEquals(0, test.secondOfMinute().getMinimumValueOverall());
        assertEquals(59, test.secondOfMinute().getMaximumValue());
        assertEquals(59, test.secondOfMinute().getMaximumValueOverall());
    }

    public void testPropertyAddSecond() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        TimeOfDay copy = test.secondOfMinute().addCopy(9);
        check(test, 10, 20, 30, 40);
        check(copy, 10, 20, 39, 40);
        
        copy = test.secondOfMinute().addCopy(29);
        check(copy, 10, 20, 59, 40);
        
        copy = test.secondOfMinute().addCopy(30);
        check(copy, 10, 21, 0, 40);
        
        copy = test.secondOfMinute().addCopy(39 * 60 + 29);
        check(copy, 10, 59, 59, 40);
        
        copy = test.secondOfMinute().addCopy(39 * 60 + 30);
        check(copy, 11, 0, 0, 40);
        
        try {
            test.secondOfMinute().addCopy(13 * 60 * 60 + 39 * 60 + 30);
            fail();
        } catch (IllegalArgumentException ex) {}
        check(test, 10, 20, 30, 40);
        
        copy = test.secondOfMinute().addCopy(-9);
        check(copy, 10, 20, 21, 40);
        
        copy = test.secondOfMinute().addCopy(-30);
        check(copy, 10, 20, 0, 40);
        
        copy = test.secondOfMinute().addCopy(-31);
        check(copy, 10, 19, 59, 40);
        
        copy = test.secondOfMinute().addCopy(-(10 * 60 * 60 + 20 * 60 + 30));
        check(copy, 0, 0, 0, 40);
        
        try {
            test.secondOfMinute().addCopy(-(10 * 60 * 60 + 20 * 60 + 31));
            fail();
        } catch (IllegalArgumentException ex) {}
        check(test, 10, 20, 30, 40);
    }

    public void testPropertyAddInFieldSecond() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        TimeOfDay copy = test.secondOfMinute().addInFieldCopy(9);
        check(test, 10, 20, 30, 40);
        check(copy, 10, 20, 39, 40);
        
        copy = test.secondOfMinute().addInFieldCopy(49);
        check(copy, 10, 20, 19, 40);
        
        copy = test.secondOfMinute().addInFieldCopy(-47);
        check(copy, 10, 20, 43, 40);
    }

    public void testPropertySetSecond() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        TimeOfDay copy = test.secondOfMinute().setCopy(12);
        check(test, 10, 20, 30, 40);
        check(copy, 10, 20, 12, 40);
        
        try {
            test.secondOfMinute().setCopy(60);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.secondOfMinute().setCopy(-1);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testPropertySetTextSecond() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        TimeOfDay copy = test.secondOfMinute().setCopy("12");
        check(test, 10, 20, 30, 40);
        check(copy, 10, 20, 12, 40);
    }

    public void testPropertyCompareToSecond() {
        TimeOfDay test1 = new TimeOfDay(TEST_TIME1);
        TimeOfDay test2 = new TimeOfDay(TEST_TIME2);
        assertEquals(true, test1.secondOfMinute().compareTo(test2) < 0);
        assertEquals(true, test2.secondOfMinute().compareTo(test1) > 0);
        assertEquals(true, test1.secondOfMinute().compareTo(test1) == 0);
        try {
            test1.secondOfMinute().compareTo((ReadablePartial) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        DateTime dt1 = new DateTime(TEST_TIME1);
        DateTime dt2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.secondOfMinute().compareTo(dt2) < 0);
        assertEquals(true, test2.secondOfMinute().compareTo(dt1) > 0);
        assertEquals(true, test1.secondOfMinute().compareTo(dt1) == 0);
        try {
            test1.secondOfMinute().compareTo((ReadableInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetMilli() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        assertSame(test.getChronology().millisOfSecond(), test.millisOfSecond().getField());
        assertEquals("millisOfSecond", test.millisOfSecond().getName());
        assertEquals("Property[millisOfSecond]", test.millisOfSecond().toString());
        assertSame(test, test.millisOfSecond().getReadablePartial());
        assertSame(test, test.millisOfSecond().getTimeOfDay());
        assertEquals(40, test.millisOfSecond().get());
        assertEquals("40", test.millisOfSecond().getAsText());
        assertEquals("40", test.millisOfSecond().getAsText(Locale.FRENCH));
        assertEquals("40", test.millisOfSecond().getAsShortText());
        assertEquals("40", test.millisOfSecond().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().millis(), test.millisOfSecond().getDurationField());
        assertEquals(test.getChronology().seconds(), test.millisOfSecond().getRangeDurationField());
        assertEquals(3, test.millisOfSecond().getMaximumTextLength(null));
        assertEquals(3, test.millisOfSecond().getMaximumShortTextLength(null));
    }

    public void testPropertyGetMaxMinValuesMilli() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        assertEquals(0, test.millisOfSecond().getMinimumValue());
        assertEquals(0, test.millisOfSecond().getMinimumValueOverall());
        assertEquals(999, test.millisOfSecond().getMaximumValue());
        assertEquals(999, test.millisOfSecond().getMaximumValueOverall());
    }

    public void testPropertyAddMilli() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        TimeOfDay copy = test.millisOfSecond().addCopy(9);
        check(test, 10, 20, 30, 40);
        check(copy, 10, 20, 30, 49);
        
        copy = test.millisOfSecond().addCopy(959);
        check(copy, 10, 20, 30, 999);
        
        copy = test.millisOfSecond().addCopy(960);
        check(copy, 10, 20, 31, 0);
        
        copy = test.millisOfSecond().addCopy(13 * 60 * 60 * 1000 + 39 * 60 * 1000 + 29 * 1000 + 959);
        check(copy, 23, 59, 59, 999);
        
        try {
            test.millisOfSecond().addCopy(13 * 60 * 60 * 1000 + 39 * 60 * 1000 + 29 * 1000 + 960);
            fail();
        } catch (IllegalArgumentException ex) {}
        check(test, 10, 20, 30, 40);
        
        copy = test.millisOfSecond().addCopy(-9);
        check(copy, 10, 20, 30, 31);
        
        copy = test.millisOfSecond().addCopy(-40);
        check(copy, 10, 20, 30, 0);
        
        copy = test.millisOfSecond().addCopy(-41);
        check(copy, 10, 20, 29, 999);
        
        copy = test.millisOfSecond().addCopy(-(10 * 60 * 60 * 1000 + 20 * 60 * 1000 + 30 * 1000 + 40));
        check(copy, 0, 0, 0, 0);
        
        try {
            test.millisOfSecond().addCopy(-(10 * 60 * 60 * 1000 + 20 * 60 * 1000 + 30 * 1000 + 41));
            fail();
        } catch (IllegalArgumentException ex) {}
        check(test, 10, 20, 30, 40);
    }

    public void testPropertyAddInFieldMilli() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        TimeOfDay copy = test.millisOfSecond().addInFieldCopy(9);
        check(test, 10, 20, 30, 40);
        check(copy, 10, 20, 30, 49);
        
        copy = test.millisOfSecond().addInFieldCopy(995);
        check(copy, 10, 20, 30, 35);
        
        copy = test.millisOfSecond().addInFieldCopy(-47);
        check(copy, 10, 20, 30, 993);
    }

    public void testPropertySetMilli() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        TimeOfDay copy = test.millisOfSecond().setCopy(12);
        check(test, 10, 20, 30, 40);
        check(copy, 10, 20, 30, 12);
        
        try {
            test.millisOfSecond().setCopy(1000);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.millisOfSecond().setCopy(-1);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testPropertySetTextMilli() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        TimeOfDay copy = test.millisOfSecond().setCopy("12");
        check(test, 10, 20, 30, 40);
        check(copy, 10, 20, 30, 12);
    }

    public void testPropertyCompareToMilli() {
        TimeOfDay test1 = new TimeOfDay(TEST_TIME1);
        TimeOfDay test2 = new TimeOfDay(TEST_TIME2);
        assertEquals(true, test1.millisOfSecond().compareTo(test2) < 0);
        assertEquals(true, test2.millisOfSecond().compareTo(test1) > 0);
        assertEquals(true, test1.millisOfSecond().compareTo(test1) == 0);
        try {
            test1.millisOfSecond().compareTo((ReadablePartial) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        DateTime dt1 = new DateTime(TEST_TIME1);
        DateTime dt2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.millisOfSecond().compareTo(dt2) < 0);
        assertEquals(true, test2.millisOfSecond().compareTo(dt1) > 0);
        assertEquals(true, test1.millisOfSecond().compareTo(dt1) == 0);
        try {
            test1.millisOfSecond().compareTo((ReadableInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    private void check(TimeOfDay test, int hour, int min, int sec, int milli) {
        assertEquals(hour, test.getHourOfDay());
        assertEquals(min, test.getMinuteOfHour());
        assertEquals(sec, test.getSecondOfMinute());
        assertEquals(milli, test.getMillisOfSecond());
    }
}
