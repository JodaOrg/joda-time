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
package org.joda.time;

import java.util.Date;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;

/**
 * This class is a Junit unit test for TimeOfDay.
 *
 * @author Stephen Colebourne
 */
public class TestTimeOfDay_Constructors extends TestCase {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final int OFFSET = 1;
    
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
        return new TestSuite(TestTimeOfDay_Constructors.class);
    }

    public TestTimeOfDay_Constructors(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        zone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(LONDON);
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
    public void testConstantMidnight() throws Throwable {
        TimeOfDay test = TimeOfDay.MIDNIGHT;
        assertEquals(ISOChronology.getInstanceUTC(), test.getChronology());
        assertEquals(0, test.getHourOfDay());
        assertEquals(0, test.getMinuteOfHour());
        assertEquals(0, test.getSecondOfMinute());
        assertEquals(0, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    /**
     * Test factory (long)
     */
    public void testFactoryMillisOfDay_long1() throws Throwable {
        TimeOfDay test = TimeOfDay.fromMillisOfDay(TEST_TIME1);
        assertEquals(ISOChronology.getInstanceUTC(), test.getChronology());
        assertEquals(1, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    /**
     * Test factory (long, Chronology)
     */
    public void testFactoryMillisOfDay_long1_Chronology() throws Throwable {
        TimeOfDay test = TimeOfDay.fromMillisOfDay(TEST_TIME1, JulianChronology.getInstance());
        assertEquals(JulianChronology.getInstanceUTC(), test.getChronology());
        assertEquals(1, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    /**
     * Test factory (long, Chronology=null)
     */
    public void testFactoryMillisOfDay_long_nullChronology() throws Throwable {
        TimeOfDay test = TimeOfDay.fromMillisOfDay(TEST_TIME1, null);
        assertEquals(ISOChronology.getInstanceUTC(), test.getChronology());
        assertEquals(1, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor ()
     */
    public void testConstructor() throws Throwable {
        TimeOfDay test = new TimeOfDay();
        assertEquals(ISOChronology.getInstanceUTC(), test.getChronology());
        assertEquals(10 + OFFSET, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

    /**
     * Test constructor (Chronology)
     */
    public void testConstructor_Chronology() throws Throwable {
        TimeOfDay test = new TimeOfDay(JulianChronology.getInstance());
        assertEquals(JulianChronology.getInstanceUTC(), test.getChronology());
        assertEquals(10 + OFFSET, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

    /**
     * Test constructor (Chronology=null)
     */
    public void testConstructor_nullChronology() throws Throwable {
        TimeOfDay test = new TimeOfDay((Chronology) null);
        assertEquals(ISOChronology.getInstanceUTC(), test.getChronology());
        assertEquals(10 + OFFSET, test.getHourOfDay());
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
        assertEquals(ISOChronology.getInstanceUTC(), test.getChronology());
        assertEquals(1 + OFFSET, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    /**
     * Test constructor (long)
     */
    public void testConstructor_long2() throws Throwable {
        TimeOfDay test = new TimeOfDay(TEST_TIME2);
        assertEquals(ISOChronology.getInstanceUTC(), test.getChronology());
        assertEquals(5 + OFFSET, test.getHourOfDay());
        assertEquals(6, test.getMinuteOfHour());
        assertEquals(7, test.getSecondOfMinute());
        assertEquals(8, test.getMillisOfSecond());
    }

    /**
     * Test constructor (long, Chronology)
     */
    public void testConstructor_long1_Chronology() throws Throwable {
        TimeOfDay test = new TimeOfDay(TEST_TIME1, JulianChronology.getInstance());
        assertEquals(JulianChronology.getInstanceUTC(), test.getChronology());
        assertEquals(1 + OFFSET, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    /**
     * Test constructor (long, Chronology)
     */
    public void testConstructor_long2_Chronology() throws Throwable {
        TimeOfDay test = new TimeOfDay(TEST_TIME2, JulianChronology.getInstance());
        assertEquals(JulianChronology.getInstanceUTC(), test.getChronology());
        assertEquals(5 + OFFSET, test.getHourOfDay());
        assertEquals(6, test.getMinuteOfHour());
        assertEquals(7, test.getSecondOfMinute());
        assertEquals(8, test.getMillisOfSecond());
    }

    /**
     * Test constructor (long, Chronology=null)
     */
    public void testConstructor_long_nullChronology() throws Throwable {
        TimeOfDay test = new TimeOfDay(TEST_TIME1, null);
        assertEquals(ISOChronology.getInstanceUTC(), test.getChronology());
        assertEquals(1 + OFFSET, test.getHourOfDay());
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
        assertEquals(ISOChronology.getInstanceUTC(), test.getChronology());
        assertEquals(1 + OFFSET, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    /**
     * Test constructor (Object=null)
     */
    public void testConstructor_nullObject() throws Throwable {
        TimeOfDay test = new TimeOfDay(null);
        assertEquals(ISOChronology.getInstanceUTC(), test.getChronology());
        assertEquals(10 + OFFSET, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

    /**
     * Test constructor (Object)
     */
    public void testConstructor_todObject() throws Throwable {
        TimeOfDay base = new TimeOfDay(10, 20, 30, 40, Chronology.getCoptic(PARIS));
        TimeOfDay test = new TimeOfDay(base);
        assertEquals(Chronology.getCopticUTC(), test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor (Object, Chronology)
     */
    public void testConstructor_Object_Chronology() throws Throwable {
        Date date = new Date(TEST_TIME1);
        TimeOfDay test = new TimeOfDay(date, JulianChronology.getInstance());
        assertEquals(JulianChronology.getInstanceUTC(), test.getChronology());
        assertEquals(1 + OFFSET, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    /**
     * Test constructor (Object=null, Chronology)
     */
    public void testConstructor_nullObject_Chronology() throws Throwable {
        TimeOfDay test = new TimeOfDay((Object) null, JulianChronology.getInstance());
        assertEquals(JulianChronology.getInstanceUTC(), test.getChronology());
        assertEquals(10 + OFFSET, test.getHourOfDay());
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
        assertEquals(ISOChronology.getInstanceUTC(), test.getChronology());
        assertEquals(1 + OFFSET, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    /**
     * Test constructor (Object=null, Chronology=null)
     */
    public void testConstructor_nullObject_nullChronology() throws Throwable {
        TimeOfDay test = new TimeOfDay((Object) null, null);
        assertEquals(ISOChronology.getInstanceUTC(), test.getChronology());
        assertEquals(10 + OFFSET, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor (int, int)
     */
    public void testConstructor_int_int() throws Throwable {
        TimeOfDay test = new TimeOfDay(10, 20);
        assertEquals(ISOChronology.getInstanceUTC(), test.getChronology());
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
        assertEquals(JulianChronology.getInstanceUTC(), test.getChronology());
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
        assertEquals(ISOChronology.getInstanceUTC(), test.getChronology());
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
        assertEquals(ISOChronology.getInstanceUTC(), test.getChronology());
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
        assertEquals(JulianChronology.getInstanceUTC(), test.getChronology());
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
        assertEquals(ISOChronology.getInstanceUTC(), test.getChronology());
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
        assertEquals(ISOChronology.getInstanceUTC(), test.getChronology());
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
        assertEquals(JulianChronology.getInstanceUTC(), test.getChronology());
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
        assertEquals(ISOChronology.getInstanceUTC(), test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

}
