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

import junit.framework.TestCase;
import junit.framework.TestSuite;

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
        assertEquals(Chronology.getCopticUTC().era(), DateTimeFieldType.era().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().era().isSupported(), DateTimeFieldType.era().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.era());
    }

    public void test_centuryOfEra() throws Exception {
        assertEquals(DateTimeFieldType.centuryOfEra(), DateTimeFieldType.centuryOfEra());
        assertEquals("centuryOfEra", DateTimeFieldType.centuryOfEra().getName());
        assertEquals(DurationFieldType.centuries(), DateTimeFieldType.centuryOfEra().getDurationType());
        assertEquals(DurationFieldType.eras(), DateTimeFieldType.centuryOfEra().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().centuryOfEra(), DateTimeFieldType.centuryOfEra().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().centuryOfEra().isSupported(), DateTimeFieldType.centuryOfEra().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.centuryOfEra());
    }

    public void test_yearOfCentury() throws Exception {
        assertEquals(DateTimeFieldType.yearOfCentury(), DateTimeFieldType.yearOfCentury());
        assertEquals("yearOfCentury", DateTimeFieldType.yearOfCentury().getName());
        assertEquals(DurationFieldType.years(), DateTimeFieldType.yearOfCentury().getDurationType());
        assertEquals(DurationFieldType.centuries(), DateTimeFieldType.yearOfCentury().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().yearOfCentury(), DateTimeFieldType.yearOfCentury().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().yearOfCentury().isSupported(), DateTimeFieldType.yearOfCentury().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.yearOfCentury());
    }

    public void test_yearOfEra() throws Exception {
        assertEquals(DateTimeFieldType.yearOfEra(), DateTimeFieldType.yearOfEra());
        assertEquals("yearOfEra", DateTimeFieldType.yearOfEra().getName());
        assertEquals(DurationFieldType.years(), DateTimeFieldType.yearOfEra().getDurationType());
        assertEquals(DurationFieldType.eras(), DateTimeFieldType.yearOfEra().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().yearOfEra(), DateTimeFieldType.yearOfEra().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().yearOfEra().isSupported(), DateTimeFieldType.yearOfEra().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.yearOfEra());
    }

    public void test_year() throws Exception {
        assertEquals(DateTimeFieldType.year(), DateTimeFieldType.year());
        assertEquals("year", DateTimeFieldType.year().getName());
        assertEquals(DurationFieldType.years(), DateTimeFieldType.year().getDurationType());
        assertEquals(null, DateTimeFieldType.year().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().year(), DateTimeFieldType.year().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().year().isSupported(), DateTimeFieldType.year().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.year());
    }

    public void test_monthOfYear() throws Exception {
        assertEquals(DateTimeFieldType.monthOfYear(), DateTimeFieldType.monthOfYear());
        assertEquals("monthOfYear", DateTimeFieldType.monthOfYear().getName());
        assertEquals(DurationFieldType.months(), DateTimeFieldType.monthOfYear().getDurationType());
        assertEquals(DurationFieldType.years(), DateTimeFieldType.monthOfYear().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().monthOfYear(), DateTimeFieldType.monthOfYear().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().monthOfYear().isSupported(), DateTimeFieldType.monthOfYear().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.monthOfYear());
    }

    public void test_weekyearOfCentury() throws Exception {
        assertEquals(DateTimeFieldType.weekyearOfCentury(), DateTimeFieldType.weekyearOfCentury());
        assertEquals("weekyearOfCentury", DateTimeFieldType.weekyearOfCentury().getName());
        assertEquals(DurationFieldType.weekyears(), DateTimeFieldType.weekyearOfCentury().getDurationType());
        assertEquals(DurationFieldType.centuries(), DateTimeFieldType.weekyearOfCentury().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().weekyearOfCentury(), DateTimeFieldType.weekyearOfCentury().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().weekyearOfCentury().isSupported(), DateTimeFieldType.weekyearOfCentury().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.weekyearOfCentury());
    }

    public void test_weekyear() throws Exception {
        assertEquals(DateTimeFieldType.weekyear(), DateTimeFieldType.weekyear());
        assertEquals("weekyear", DateTimeFieldType.weekyear().getName());
        assertEquals(DurationFieldType.weekyears(), DateTimeFieldType.weekyear().getDurationType());
        assertEquals(null, DateTimeFieldType.weekyear().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().weekyear(), DateTimeFieldType.weekyear().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().weekyear().isSupported(), DateTimeFieldType.weekyear().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.weekyear());
    }

    public void test_weekOfWeekyear() throws Exception {
        assertEquals(DateTimeFieldType.weekOfWeekyear(), DateTimeFieldType.weekOfWeekyear());
        assertEquals("weekOfWeekyear", DateTimeFieldType.weekOfWeekyear().getName());
        assertEquals(DurationFieldType.weeks(), DateTimeFieldType.weekOfWeekyear().getDurationType());
        assertEquals(DurationFieldType.weekyears(), DateTimeFieldType.weekOfWeekyear().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().weekOfWeekyear(), DateTimeFieldType.weekOfWeekyear().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().weekOfWeekyear().isSupported(), DateTimeFieldType.weekOfWeekyear().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.weekOfWeekyear());
    }

    public void test_dayOfYear() throws Exception {
        assertEquals(DateTimeFieldType.dayOfYear(), DateTimeFieldType.dayOfYear());
        assertEquals("dayOfYear", DateTimeFieldType.dayOfYear().getName());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.dayOfYear().getDurationType());
        assertEquals(DurationFieldType.years(), DateTimeFieldType.dayOfYear().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().dayOfYear(), DateTimeFieldType.dayOfYear().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().dayOfYear().isSupported(), DateTimeFieldType.dayOfYear().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.dayOfYear());
    }

    public void test_dayOfMonth() throws Exception {
        assertEquals(DateTimeFieldType.dayOfMonth(), DateTimeFieldType.dayOfMonth());
        assertEquals("dayOfMonth", DateTimeFieldType.dayOfMonth().getName());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.dayOfMonth().getDurationType());
        assertEquals(DurationFieldType.months(), DateTimeFieldType.dayOfMonth().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().dayOfMonth(), DateTimeFieldType.dayOfMonth().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().dayOfMonth().isSupported(), DateTimeFieldType.dayOfMonth().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.dayOfMonth());
    }

    public void test_dayOfWeek() throws Exception {
        assertEquals(DateTimeFieldType.dayOfWeek(), DateTimeFieldType.dayOfWeek());
        assertEquals("dayOfWeek", DateTimeFieldType.dayOfWeek().getName());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.dayOfWeek().getDurationType());
        assertEquals(DurationFieldType.weeks(), DateTimeFieldType.dayOfWeek().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().dayOfWeek(), DateTimeFieldType.dayOfWeek().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().dayOfWeek().isSupported(), DateTimeFieldType.dayOfWeek().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.dayOfWeek());
    }

    public void test_halfdayOfDay() throws Exception {
        assertEquals(DateTimeFieldType.halfdayOfDay(), DateTimeFieldType.halfdayOfDay());
        assertEquals("halfdayOfDay", DateTimeFieldType.halfdayOfDay().getName());
        assertEquals(DurationFieldType.halfdays(), DateTimeFieldType.halfdayOfDay().getDurationType());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.halfdayOfDay().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().halfdayOfDay(), DateTimeFieldType.halfdayOfDay().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().halfdayOfDay().isSupported(), DateTimeFieldType.halfdayOfDay().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.halfdayOfDay());
    }

    public void test_clockhourOfDay() throws Exception {
        assertEquals(DateTimeFieldType.clockhourOfDay(), DateTimeFieldType.clockhourOfDay());
        assertEquals("clockhourOfDay", DateTimeFieldType.clockhourOfDay().getName());
        assertEquals(DurationFieldType.hours(), DateTimeFieldType.clockhourOfDay().getDurationType());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.clockhourOfDay().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().clockhourOfDay(), DateTimeFieldType.clockhourOfDay().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().clockhourOfDay().isSupported(), DateTimeFieldType.clockhourOfDay().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.clockhourOfDay());
    }

    public void test_clockhourOfHalfday() throws Exception {
        assertEquals(DateTimeFieldType.clockhourOfHalfday(), DateTimeFieldType.clockhourOfHalfday());
        assertEquals("clockhourOfHalfday", DateTimeFieldType.clockhourOfHalfday().getName());
        assertEquals(DurationFieldType.hours(), DateTimeFieldType.clockhourOfHalfday().getDurationType());
        assertEquals(DurationFieldType.halfdays(), DateTimeFieldType.clockhourOfHalfday().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().clockhourOfHalfday(), DateTimeFieldType.clockhourOfHalfday().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().clockhourOfHalfday().isSupported(), DateTimeFieldType.clockhourOfHalfday().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.clockhourOfHalfday());
    }

    public void test_hourOfHalfday() throws Exception {
        assertEquals(DateTimeFieldType.hourOfHalfday(), DateTimeFieldType.hourOfHalfday());
        assertEquals("hourOfHalfday", DateTimeFieldType.hourOfHalfday().getName());
        assertEquals(DurationFieldType.hours(), DateTimeFieldType.hourOfHalfday().getDurationType());
        assertEquals(DurationFieldType.halfdays(), DateTimeFieldType.hourOfHalfday().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().hourOfHalfday(), DateTimeFieldType.hourOfHalfday().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().hourOfHalfday().isSupported(), DateTimeFieldType.hourOfHalfday().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.hourOfHalfday());
    }

    public void test_hourOfDay() throws Exception {
        assertEquals(DateTimeFieldType.hourOfDay(), DateTimeFieldType.hourOfDay());
        assertEquals("hourOfDay", DateTimeFieldType.hourOfDay().getName());
        assertEquals(DurationFieldType.hours(), DateTimeFieldType.hourOfDay().getDurationType());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.hourOfDay().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().hourOfDay(), DateTimeFieldType.hourOfDay().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().hourOfDay().isSupported(), DateTimeFieldType.hourOfDay().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.hourOfDay());
    }

    public void test_minuteOfDay() throws Exception {
        assertEquals(DateTimeFieldType.minuteOfDay(), DateTimeFieldType.minuteOfDay());
        assertEquals("minuteOfDay", DateTimeFieldType.minuteOfDay().getName());
        assertEquals(DurationFieldType.minutes(), DateTimeFieldType.minuteOfDay().getDurationType());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.minuteOfDay().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().minuteOfDay(), DateTimeFieldType.minuteOfDay().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().minuteOfDay().isSupported(), DateTimeFieldType.minuteOfDay().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.minuteOfDay());
    }

    public void test_minuteOfHour() throws Exception {
        assertEquals(DateTimeFieldType.minuteOfHour(), DateTimeFieldType.minuteOfHour());
        assertEquals("minuteOfHour", DateTimeFieldType.minuteOfHour().getName());
        assertEquals(DurationFieldType.minutes(), DateTimeFieldType.minuteOfHour().getDurationType());
        assertEquals(DurationFieldType.hours(), DateTimeFieldType.minuteOfHour().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().minuteOfHour(), DateTimeFieldType.minuteOfHour().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().minuteOfHour().isSupported(), DateTimeFieldType.minuteOfHour().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.minuteOfHour());
    }

    public void test_secondOfDay() throws Exception {
        assertEquals(DateTimeFieldType.secondOfDay(), DateTimeFieldType.secondOfDay());
        assertEquals("secondOfDay", DateTimeFieldType.secondOfDay().getName());
        assertEquals(DurationFieldType.seconds(), DateTimeFieldType.secondOfDay().getDurationType());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.secondOfDay().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().secondOfDay(), DateTimeFieldType.secondOfDay().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().secondOfDay().isSupported(), DateTimeFieldType.secondOfDay().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.secondOfDay());
    }

    public void test_secondOfMinute() throws Exception {
        assertEquals(DateTimeFieldType.secondOfMinute(), DateTimeFieldType.secondOfMinute());
        assertEquals("secondOfMinute", DateTimeFieldType.secondOfMinute().getName());
        assertEquals(DurationFieldType.seconds(), DateTimeFieldType.secondOfMinute().getDurationType());
        assertEquals(DurationFieldType.minutes(), DateTimeFieldType.secondOfMinute().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().secondOfMinute(), DateTimeFieldType.secondOfMinute().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().secondOfMinute().isSupported(), DateTimeFieldType.secondOfMinute().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.secondOfMinute());
    }

    public void test_millisOfDay() throws Exception {
        assertEquals(DateTimeFieldType.millisOfDay(), DateTimeFieldType.millisOfDay());
        assertEquals("millisOfDay", DateTimeFieldType.millisOfDay().getName());
        assertEquals(DurationFieldType.millis(), DateTimeFieldType.millisOfDay().getDurationType());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.millisOfDay().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().millisOfDay(), DateTimeFieldType.millisOfDay().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().millisOfDay().isSupported(), DateTimeFieldType.millisOfDay().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.millisOfDay());
    }

    public void test_millisOfSecond() throws Exception {
        assertEquals(DateTimeFieldType.millisOfSecond(), DateTimeFieldType.millisOfSecond());
        assertEquals("millisOfSecond", DateTimeFieldType.millisOfSecond().getName());
        assertEquals(DurationFieldType.millis(), DateTimeFieldType.millisOfSecond().getDurationType());
        assertEquals(DurationFieldType.seconds(), DateTimeFieldType.millisOfSecond().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().millisOfSecond(), DateTimeFieldType.millisOfSecond().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().millisOfSecond().isSupported(), DateTimeFieldType.millisOfSecond().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DateTimeFieldType.millisOfSecond());
    }

    //-----------------------------------------------------------------------
    public void assertSerialization(DateTimeFieldType type) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(type);
        byte[] bytes = baos.toByteArray();
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        DateTimeFieldType result = (DateTimeFieldType) ois.readObject();
        ois.close();
        
        assertSame(type, result);
    }

}
