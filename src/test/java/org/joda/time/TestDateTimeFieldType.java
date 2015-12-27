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
 * This class is a Junit unit test for Chronology.
 *
 * @author Stephen Colebourne
 */
public class TestDateTimeFieldType extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestDateTimeFieldType.class);
    }

    public TestDateTimeFieldType(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    //-----------------------------------------------------------------------
    public void test_era() throws Exception {
        assertEquals(DateTimeFieldType.era(), DateTimeFieldType.era());
        assertEquals("era", DateTimeFieldType.era().getName());
        assertEquals(DurationFieldType.eras(), DateTimeFieldType.era().getDurationType());
        assertEquals(null, DateTimeFieldType.era().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().era(), DateTimeFieldType.era().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().era().isSupported(), DateTimeFieldType.era().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.era());
    }

    public void test_centuryOfEra() throws Exception {
        assertEquals(DateTimeFieldType.centuryOfEra(), DateTimeFieldType.centuryOfEra());
        assertEquals("centuryOfEra", DateTimeFieldType.centuryOfEra().getName());
        assertEquals(DurationFieldType.centuries(), DateTimeFieldType.centuryOfEra().getDurationType());
        assertEquals(DurationFieldType.eras(), DateTimeFieldType.centuryOfEra().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().centuryOfEra(), DateTimeFieldType.centuryOfEra().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().centuryOfEra().isSupported(), DateTimeFieldType.centuryOfEra().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.centuryOfEra());
    }

    public void test_yearOfCentury() throws Exception {
        assertEquals(DateTimeFieldType.yearOfCentury(), DateTimeFieldType.yearOfCentury());
        assertEquals("yearOfCentury", DateTimeFieldType.yearOfCentury().getName());
        assertEquals(DurationFieldType.years(), DateTimeFieldType.yearOfCentury().getDurationType());
        assertEquals(DurationFieldType.centuries(), DateTimeFieldType.yearOfCentury().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().yearOfCentury(), DateTimeFieldType.yearOfCentury().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().yearOfCentury().isSupported(), DateTimeFieldType.yearOfCentury().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.yearOfCentury());
    }

    public void test_yearOfEra() throws Exception {
        assertEquals(DateTimeFieldType.yearOfEra(), DateTimeFieldType.yearOfEra());
        assertEquals("yearOfEra", DateTimeFieldType.yearOfEra().getName());
        assertEquals(DurationFieldType.years(), DateTimeFieldType.yearOfEra().getDurationType());
        assertEquals(DurationFieldType.eras(), DateTimeFieldType.yearOfEra().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().yearOfEra(), DateTimeFieldType.yearOfEra().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().yearOfEra().isSupported(), DateTimeFieldType.yearOfEra().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.yearOfEra());
    }

    public void test_year() throws Exception {
        assertEquals(DateTimeFieldType.year(), DateTimeFieldType.year());
        assertEquals("year", DateTimeFieldType.year().getName());
        assertEquals(DurationFieldType.years(), DateTimeFieldType.year().getDurationType());
        assertEquals(null, DateTimeFieldType.year().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().year(), DateTimeFieldType.year().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().year().isSupported(), DateTimeFieldType.year().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.year());
    }

    public void test_monthOfYear() throws Exception {
        assertEquals(DateTimeFieldType.monthOfYear(), DateTimeFieldType.monthOfYear());
        assertEquals("monthOfYear", DateTimeFieldType.monthOfYear().getName());
        assertEquals(DurationFieldType.months(), DateTimeFieldType.monthOfYear().getDurationType());
        assertEquals(DurationFieldType.years(), DateTimeFieldType.monthOfYear().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().monthOfYear(), DateTimeFieldType.monthOfYear().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().monthOfYear().isSupported(), DateTimeFieldType.monthOfYear().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.monthOfYear());
    }

    public void test_weekyearOfCentury() throws Exception {
        assertEquals(DateTimeFieldType.weekyearOfCentury(), DateTimeFieldType.weekyearOfCentury());
        assertEquals("weekyearOfCentury", DateTimeFieldType.weekyearOfCentury().getName());
        assertEquals(DurationFieldType.weekyears(), DateTimeFieldType.weekyearOfCentury().getDurationType());
        assertEquals(DurationFieldType.centuries(), DateTimeFieldType.weekyearOfCentury().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().weekyearOfCentury(), DateTimeFieldType.weekyearOfCentury().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().weekyearOfCentury().isSupported(), DateTimeFieldType.weekyearOfCentury().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.weekyearOfCentury());
    }

    public void test_weekyear() throws Exception {
        assertEquals(DateTimeFieldType.weekyear(), DateTimeFieldType.weekyear());
        assertEquals("weekyear", DateTimeFieldType.weekyear().getName());
        assertEquals(DurationFieldType.weekyears(), DateTimeFieldType.weekyear().getDurationType());
        assertEquals(null, DateTimeFieldType.weekyear().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().weekyear(), DateTimeFieldType.weekyear().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().weekyear().isSupported(), DateTimeFieldType.weekyear().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.weekyear());
    }

    public void test_weekOfWeekyear() throws Exception {
        assertEquals(DateTimeFieldType.weekOfWeekyear(), DateTimeFieldType.weekOfWeekyear());
        assertEquals("weekOfWeekyear", DateTimeFieldType.weekOfWeekyear().getName());
        assertEquals(DurationFieldType.weeks(), DateTimeFieldType.weekOfWeekyear().getDurationType());
        assertEquals(DurationFieldType.weekyears(), DateTimeFieldType.weekOfWeekyear().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().weekOfWeekyear(), DateTimeFieldType.weekOfWeekyear().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().weekOfWeekyear().isSupported(), DateTimeFieldType.weekOfWeekyear().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.weekOfWeekyear());
    }

    public void test_dayOfYear() throws Exception {
        assertEquals(DateTimeFieldType.dayOfYear(), DateTimeFieldType.dayOfYear());
        assertEquals("dayOfYear", DateTimeFieldType.dayOfYear().getName());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.dayOfYear().getDurationType());
        assertEquals(DurationFieldType.years(), DateTimeFieldType.dayOfYear().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().dayOfYear(), DateTimeFieldType.dayOfYear().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().dayOfYear().isSupported(), DateTimeFieldType.dayOfYear().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.dayOfYear());
    }

    public void test_dayOfMonth() throws Exception {
        assertEquals(DateTimeFieldType.dayOfMonth(), DateTimeFieldType.dayOfMonth());
        assertEquals("dayOfMonth", DateTimeFieldType.dayOfMonth().getName());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.dayOfMonth().getDurationType());
        assertEquals(DurationFieldType.months(), DateTimeFieldType.dayOfMonth().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().dayOfMonth(), DateTimeFieldType.dayOfMonth().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().dayOfMonth().isSupported(), DateTimeFieldType.dayOfMonth().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.dayOfMonth());
    }

    public void test_dayOfWeek() throws Exception {
        assertEquals(DateTimeFieldType.dayOfWeek(), DateTimeFieldType.dayOfWeek());
        assertEquals("dayOfWeek", DateTimeFieldType.dayOfWeek().getName());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.dayOfWeek().getDurationType());
        assertEquals(DurationFieldType.weeks(), DateTimeFieldType.dayOfWeek().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().dayOfWeek(), DateTimeFieldType.dayOfWeek().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().dayOfWeek().isSupported(), DateTimeFieldType.dayOfWeek().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.dayOfWeek());
    }

    public void test_halfdayOfDay() throws Exception {
        assertEquals(DateTimeFieldType.halfdayOfDay(), DateTimeFieldType.halfdayOfDay());
        assertEquals("halfdayOfDay", DateTimeFieldType.halfdayOfDay().getName());
        assertEquals(DurationFieldType.halfdays(), DateTimeFieldType.halfdayOfDay().getDurationType());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.halfdayOfDay().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().halfdayOfDay(), DateTimeFieldType.halfdayOfDay().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().halfdayOfDay().isSupported(), DateTimeFieldType.halfdayOfDay().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.halfdayOfDay());
    }

    public void test_clockhourOfDay() throws Exception {
        assertEquals(DateTimeFieldType.clockhourOfDay(), DateTimeFieldType.clockhourOfDay());
        assertEquals("clockhourOfDay", DateTimeFieldType.clockhourOfDay().getName());
        assertEquals(DurationFieldType.hours(), DateTimeFieldType.clockhourOfDay().getDurationType());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.clockhourOfDay().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().clockhourOfDay(), DateTimeFieldType.clockhourOfDay().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().clockhourOfDay().isSupported(), DateTimeFieldType.clockhourOfDay().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.clockhourOfDay());
    }

    public void test_clockhourOfHalfday() throws Exception {
        assertEquals(DateTimeFieldType.clockhourOfHalfday(), DateTimeFieldType.clockhourOfHalfday());
        assertEquals("clockhourOfHalfday", DateTimeFieldType.clockhourOfHalfday().getName());
        assertEquals(DurationFieldType.hours(), DateTimeFieldType.clockhourOfHalfday().getDurationType());
        assertEquals(DurationFieldType.halfdays(), DateTimeFieldType.clockhourOfHalfday().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().clockhourOfHalfday(), DateTimeFieldType.clockhourOfHalfday().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().clockhourOfHalfday().isSupported(), DateTimeFieldType.clockhourOfHalfday().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.clockhourOfHalfday());
    }

    public void test_hourOfHalfday() throws Exception {
        assertEquals(DateTimeFieldType.hourOfHalfday(), DateTimeFieldType.hourOfHalfday());
        assertEquals("hourOfHalfday", DateTimeFieldType.hourOfHalfday().getName());
        assertEquals(DurationFieldType.hours(), DateTimeFieldType.hourOfHalfday().getDurationType());
        assertEquals(DurationFieldType.halfdays(), DateTimeFieldType.hourOfHalfday().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().hourOfHalfday(), DateTimeFieldType.hourOfHalfday().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().hourOfHalfday().isSupported(), DateTimeFieldType.hourOfHalfday().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.hourOfHalfday());
    }

    public void test_hourOfDay() throws Exception {
        assertEquals(DateTimeFieldType.hourOfDay(), DateTimeFieldType.hourOfDay());
        assertEquals("hourOfDay", DateTimeFieldType.hourOfDay().getName());
        assertEquals(DurationFieldType.hours(), DateTimeFieldType.hourOfDay().getDurationType());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.hourOfDay().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().hourOfDay(), DateTimeFieldType.hourOfDay().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().hourOfDay().isSupported(), DateTimeFieldType.hourOfDay().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.hourOfDay());
    }

    public void test_minuteOfDay() throws Exception {
        assertEquals(DateTimeFieldType.minuteOfDay(), DateTimeFieldType.minuteOfDay());
        assertEquals("minuteOfDay", DateTimeFieldType.minuteOfDay().getName());
        assertEquals(DurationFieldType.minutes(), DateTimeFieldType.minuteOfDay().getDurationType());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.minuteOfDay().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().minuteOfDay(), DateTimeFieldType.minuteOfDay().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().minuteOfDay().isSupported(), DateTimeFieldType.minuteOfDay().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.minuteOfDay());
    }

    public void test_minuteOfHour() throws Exception {
        assertEquals(DateTimeFieldType.minuteOfHour(), DateTimeFieldType.minuteOfHour());
        assertEquals("minuteOfHour", DateTimeFieldType.minuteOfHour().getName());
        assertEquals(DurationFieldType.minutes(), DateTimeFieldType.minuteOfHour().getDurationType());
        assertEquals(DurationFieldType.hours(), DateTimeFieldType.minuteOfHour().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().minuteOfHour(), DateTimeFieldType.minuteOfHour().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().minuteOfHour().isSupported(), DateTimeFieldType.minuteOfHour().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.minuteOfHour());
    }

    public void test_secondOfDay() throws Exception {
        assertEquals(DateTimeFieldType.secondOfDay(), DateTimeFieldType.secondOfDay());
        assertEquals("secondOfDay", DateTimeFieldType.secondOfDay().getName());
        assertEquals(DurationFieldType.seconds(), DateTimeFieldType.secondOfDay().getDurationType());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.secondOfDay().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().secondOfDay(), DateTimeFieldType.secondOfDay().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().secondOfDay().isSupported(), DateTimeFieldType.secondOfDay().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.secondOfDay());
    }

    public void test_secondOfMinute() throws Exception {
        assertEquals(DateTimeFieldType.secondOfMinute(), DateTimeFieldType.secondOfMinute());
        assertEquals("secondOfMinute", DateTimeFieldType.secondOfMinute().getName());
        assertEquals(DurationFieldType.seconds(), DateTimeFieldType.secondOfMinute().getDurationType());
        assertEquals(DurationFieldType.minutes(), DateTimeFieldType.secondOfMinute().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().secondOfMinute(), DateTimeFieldType.secondOfMinute().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().secondOfMinute().isSupported(), DateTimeFieldType.secondOfMinute().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.secondOfMinute());
    }

    public void test_millisOfDay() throws Exception {
        assertEquals(DateTimeFieldType.millisOfDay(), DateTimeFieldType.millisOfDay());
        assertEquals("millisOfDay", DateTimeFieldType.millisOfDay().getName());
        assertEquals(DurationFieldType.millis(), DateTimeFieldType.millisOfDay().getDurationType());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.millisOfDay().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().millisOfDay(), DateTimeFieldType.millisOfDay().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().millisOfDay().isSupported(), DateTimeFieldType.millisOfDay().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.millisOfDay());
    }

    public void test_millisOfSecond() throws Exception {
        assertEquals(DateTimeFieldType.millisOfSecond(), DateTimeFieldType.millisOfSecond());
        assertEquals("millisOfSecond", DateTimeFieldType.millisOfSecond().getName());
        assertEquals(DurationFieldType.millis(), DateTimeFieldType.millisOfSecond().getDurationType());
        assertEquals(DurationFieldType.seconds(), DateTimeFieldType.millisOfSecond().getRangeDurationType());
        assertEquals(CopticChronology.getInstanceUTC().millisOfSecond(), DateTimeFieldType.millisOfSecond().getField(CopticChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC().millisOfSecond().isSupported(), DateTimeFieldType.millisOfSecond().isSupported(CopticChronology.getInstanceUTC()));
        assertSerialization(DateTimeFieldType.millisOfSecond());
    }

    public void test_other() throws Exception {
        assertEquals(1, DateTimeFieldType.class.getDeclaredClasses().length);
        Class cls = DateTimeFieldType.class.getDeclaredClasses()[0];
        assertEquals(1, cls.getDeclaredConstructors().length);
        Constructor con = cls.getDeclaredConstructors()[0];
        Object[] params = new Object[] {
            "other", new Byte((byte) 128), DurationFieldType.hours(), DurationFieldType.months()};
        con.setAccessible(true);  // for Apache Harmony JVM
        DateTimeFieldType type = (DateTimeFieldType) con.newInstance(params);
        
        assertEquals("other", type.getName());
        assertSame(DurationFieldType.hours(), type.getDurationType());
        assertSame(DurationFieldType.months(), type.getRangeDurationType());
        try {
            type.getField(CopticChronology.getInstanceUTC());
            fail();
        } catch (InternalError ex) {}
        DateTimeFieldType result = doSerialization(type);
        assertEquals(type.getName(), result.getName());
        assertNotSame(type, result);
    }

    //-----------------------------------------------------------------------
    private void assertSerialization(DateTimeFieldType type) throws Exception {
        DateTimeFieldType result = doSerialization(type);
        assertSame(type, result);
    }

    private DateTimeFieldType doSerialization(DateTimeFieldType type) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(type);
        oos.close();
        byte[] bytes = baos.toByteArray();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        DateTimeFieldType result = (DateTimeFieldType) ois.readObject();
        ois.close();
        return result;
    }

}
