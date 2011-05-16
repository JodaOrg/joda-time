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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.chrono.CopticChronology;

/**
 * This class is a Junit unit test for DurationFieldType.
 *
 * @author Stephen Colebourne
 */
public class TestDurationFieldType extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestDurationFieldType.class);
    }

    public TestDurationFieldType(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    //-----------------------------------------------------------------------
    public void test_eras() throws Exception {
        assertEquals(DurationFieldType.eras(), DurationFieldType.eras());
        assertEquals("eras", DurationFieldType.eras().getName());
        assertEquals(CopticChronology.getInstanceUTC().eras(), DurationFieldType.eras().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().eras().isSupported(), DurationFieldType.eras().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DurationFieldType.eras());
    }

    public void test_centuries() throws Exception {
        assertEquals(DurationFieldType.centuries(), DurationFieldType.centuries());
        assertEquals("centuries", DurationFieldType.centuries().getName());
        assertEquals(CopticChronology.getInstanceUTC().centuries(), DurationFieldType.centuries().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().centuries().isSupported(), DurationFieldType.centuries().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DurationFieldType.centuries());
    }

    public void test_years() throws Exception {
        assertEquals(DurationFieldType.years(), DurationFieldType.years());
        assertEquals("years", DurationFieldType.years().getName());
        assertEquals(CopticChronology.getInstanceUTC().years(), DurationFieldType.years().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().years().isSupported(), DurationFieldType.years().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DurationFieldType.years());
    }

    public void test_months() throws Exception {
        assertEquals(DurationFieldType.months(), DurationFieldType.months());
        assertEquals("months", DurationFieldType.months().getName());
        assertEquals(CopticChronology.getInstanceUTC().months(), DurationFieldType.months().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().months().isSupported(), DurationFieldType.months().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DurationFieldType.months());
    }

    public void test_weekyears() throws Exception {
        assertEquals(DurationFieldType.weekyears(), DurationFieldType.weekyears());
        assertEquals("weekyears", DurationFieldType.weekyears().getName());
        assertEquals(CopticChronology.getInstanceUTC().weekyears(), DurationFieldType.weekyears().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().weekyears().isSupported(), DurationFieldType.weekyears().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DurationFieldType.weekyears());
    }

    public void test_weeks() throws Exception {
        assertEquals(DurationFieldType.weeks(), DurationFieldType.weeks());
        assertEquals("weeks", DurationFieldType.weeks().getName());
        assertEquals(CopticChronology.getInstanceUTC().weeks(), DurationFieldType.weeks().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().weeks().isSupported(), DurationFieldType.weeks().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DurationFieldType.weeks());
    }

    public void test_days() throws Exception {
        assertEquals(DurationFieldType.days(), DurationFieldType.days());
        assertEquals("days", DurationFieldType.days().getName());
        assertEquals(CopticChronology.getInstanceUTC().days(), DurationFieldType.days().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().days().isSupported(), DurationFieldType.days().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DurationFieldType.days());
    }

    public void test_halfdays() throws Exception {
        assertEquals(DurationFieldType.halfdays(), DurationFieldType.halfdays());
        assertEquals("halfdays", DurationFieldType.halfdays().getName());
        assertEquals(CopticChronology.getInstanceUTC().halfdays(), DurationFieldType.halfdays().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().halfdays().isSupported(), DurationFieldType.halfdays().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DurationFieldType.halfdays());
    }

    public void test_hours() throws Exception {
        assertEquals(DurationFieldType.hours(), DurationFieldType.hours());
        assertEquals("hours", DurationFieldType.hours().getName());
        assertEquals(CopticChronology.getInstanceUTC().hours(), DurationFieldType.hours().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().hours().isSupported(), DurationFieldType.hours().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DurationFieldType.hours());
    }

    public void test_minutes() throws Exception {
        assertEquals(DurationFieldType.minutes(), DurationFieldType.minutes());
        assertEquals("minutes", DurationFieldType.minutes().getName());
        assertEquals(CopticChronology.getInstanceUTC().minutes(), DurationFieldType.minutes().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().minutes().isSupported(), DurationFieldType.minutes().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DurationFieldType.minutes());
    }

    public void test_seconds() throws Exception {
        assertEquals(DurationFieldType.seconds(), DurationFieldType.seconds());
        assertEquals("seconds", DurationFieldType.seconds().getName());
        assertEquals(CopticChronology.getInstanceUTC().seconds(), DurationFieldType.seconds().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().seconds().isSupported(), DurationFieldType.seconds().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DurationFieldType.seconds());
    }

    public void test_millis() throws Exception {
        assertEquals(DurationFieldType.millis(), DurationFieldType.millis());
        assertEquals("millis", DurationFieldType.millis().getName());
        assertEquals(CopticChronology.getInstanceUTC().millis(), DurationFieldType.millis().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().millis().isSupported(), DurationFieldType.millis().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DurationFieldType.millis());
    }

    public void test_other() throws Exception {
        assertEquals(1, DurationFieldType.class.getDeclaredClasses().length);
        Class cls = DurationFieldType.class.getDeclaredClasses()[0];
        assertEquals(1, cls.getDeclaredConstructors().length);
        Constructor con = cls.getDeclaredConstructors()[0];
        Object[] params = new Object[] {"other", new Byte((byte) 128)};
        DurationFieldType type = (DurationFieldType) con.newInstance(params);
        
        assertEquals("other", type.getName());
        try {
            type.getField(CopticChronology.getInstanceUTC());
            fail();
        } catch (InternalError ex) {}
        DurationFieldType result = doSerialization(type);
        assertEquals(type.getName(), result.getName());
        assertNotSame(type, result);
    }

    //-----------------------------------------------------------------------
    private void assertSerialization(DurationFieldType type) throws Exception {
        DurationFieldType result = doSerialization(type);
        assertSame(type, result);
    }

    private DurationFieldType doSerialization(DurationFieldType type) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(type);
        byte[] bytes = baos.toByteArray();
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        DurationFieldType result = (DurationFieldType) ois.readObject();
        ois.close();
        return result;
    }

}
