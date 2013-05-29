/*
 *  Copyright 2001-2009 Stephen Colebourne
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
package org.joda.time.field;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.chrono.ISOChronology;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



/**
 * This class is a Junit unit test for PeriodFormatterBuilder.
 *
 * @author Stephen Colebourne
 */
public class TestMillisDurationField extends Assert {
    //-----------------------------------------------------------------------
   @Test
    public void test_getType() {
        assertEquals(DurationFieldType.millis(), MillisDurationField.INSTANCE.getType());
    }

   @Test
    public void test_getName() {
        assertEquals("millis", MillisDurationField.INSTANCE.getName());
    }
    
   @Test
    public void test_isSupported() {
        assertEquals(true, MillisDurationField.INSTANCE.isSupported());
    }

   @Test
    public void test_isPrecise() {
        assertEquals(true, MillisDurationField.INSTANCE.isPrecise());
    }

   @Test
    public void test_getUnitMillis() {
        assertEquals(1, MillisDurationField.INSTANCE.getUnitMillis());
    }

   @Test
    public void test_toString() {
        assertEquals("DurationField[millis]", MillisDurationField.INSTANCE.toString());
    }
    
    //-----------------------------------------------------------------------
   @Test
    public void test_getValue_long() {
        assertEquals(0, MillisDurationField.INSTANCE.getValue(0L));
        assertEquals(1234, MillisDurationField.INSTANCE.getValue(1234L));
        assertEquals(-1234, MillisDurationField.INSTANCE.getValue(-1234L));
        try {
            MillisDurationField.INSTANCE.getValue(((long) (Integer.MAX_VALUE)) + 1L);
            fail();
        } catch (ArithmeticException ex) {}
    }

   @Test
    public void test_getValueAsLong_long() {
        assertEquals(0L, MillisDurationField.INSTANCE.getValueAsLong(0L));
        assertEquals(1234L, MillisDurationField.INSTANCE.getValueAsLong(1234L));
        assertEquals(-1234L, MillisDurationField.INSTANCE.getValueAsLong(-1234L));
        assertEquals(((long) (Integer.MAX_VALUE)) + 1L, MillisDurationField.INSTANCE.getValueAsLong(((long) (Integer.MAX_VALUE)) + 1L));
    }

   @Test
    public void test_getValue_long_long() {
        assertEquals(0, MillisDurationField.INSTANCE.getValue(0L, 567L));
        assertEquals(1234, MillisDurationField.INSTANCE.getValue(1234L, 567L));
        assertEquals(-1234, MillisDurationField.INSTANCE.getValue(-1234L, 567L));
        try {
            MillisDurationField.INSTANCE.getValue(((long) (Integer.MAX_VALUE)) + 1L, 567L);
            fail();
        } catch (ArithmeticException ex) {}
    }

   @Test
    public void test_getValueAsLong_long_long() {
        assertEquals(0L, MillisDurationField.INSTANCE.getValueAsLong(0L, 567L));
        assertEquals(1234L, MillisDurationField.INSTANCE.getValueAsLong(1234L, 567L));
        assertEquals(-1234L, MillisDurationField.INSTANCE.getValueAsLong(-1234L, 567L));
        assertEquals(((long) (Integer.MAX_VALUE)) + 1L, MillisDurationField.INSTANCE.getValueAsLong(((long) (Integer.MAX_VALUE)) + 1L, 567L));
    }

    //-----------------------------------------------------------------------
   @Test
    public void test_getMillis_int() {
        assertEquals(0, MillisDurationField.INSTANCE.getMillis(0));
        assertEquals(1234, MillisDurationField.INSTANCE.getMillis(1234));
        assertEquals(-1234, MillisDurationField.INSTANCE.getMillis(-1234));
    }

   @Test
    public void test_getMillis_long() {
        assertEquals(0L, MillisDurationField.INSTANCE.getMillis(0L));
        assertEquals(1234L, MillisDurationField.INSTANCE.getMillis(1234L));
        assertEquals(-1234L, MillisDurationField.INSTANCE.getMillis(-1234L));
    }

   @Test
    public void test_getMillis_int_long() {
        assertEquals(0, MillisDurationField.INSTANCE.getMillis(0, 567L));
        assertEquals(1234, MillisDurationField.INSTANCE.getMillis(1234, 567L));
        assertEquals(-1234, MillisDurationField.INSTANCE.getMillis(-1234, 567L));
    }

   @Test
    public void test_getMillis_long_long() {
        assertEquals(0L, MillisDurationField.INSTANCE.getMillis(0L, 567L));
        assertEquals(1234L, MillisDurationField.INSTANCE.getMillis(1234L, 567L));
        assertEquals(-1234L, MillisDurationField.INSTANCE.getMillis(-1234L, 567L));
    }

    //-----------------------------------------------------------------------
   @Test
    public void test_add_long_int() {
        assertEquals(567L, MillisDurationField.INSTANCE.add(567L, 0));
        assertEquals(567L + 1234L, MillisDurationField.INSTANCE.add(567L, 1234));
        assertEquals(567L - 1234L, MillisDurationField.INSTANCE.add(567L, -1234));
        try {
            MillisDurationField.INSTANCE.add(Long.MAX_VALUE, 1);
            fail();
        } catch (ArithmeticException ex) {}
    }

   @Test
    public void test_add_long_long() {
        assertEquals(567L, MillisDurationField.INSTANCE.add(567L, 0L));
        assertEquals(567L + 1234L, MillisDurationField.INSTANCE.add(567L, 1234L));
        assertEquals(567L - 1234L, MillisDurationField.INSTANCE.add(567L, -1234L));
        try {
            MillisDurationField.INSTANCE.add(Long.MAX_VALUE, 1L);
            fail();
        } catch (ArithmeticException ex) {}
    }

    //-----------------------------------------------------------------------
   @Test
    public void test_getDifference_long_int() {
        assertEquals(567, MillisDurationField.INSTANCE.getDifference(567L, 0L));
        assertEquals(567 - 1234, MillisDurationField.INSTANCE.getDifference(567L, 1234L));
        assertEquals(567 + 1234, MillisDurationField.INSTANCE.getDifference(567L, -1234L));
        try {
            MillisDurationField.INSTANCE.getDifference(Long.MAX_VALUE, 1L);
            fail();
        } catch (ArithmeticException ex) {}
    }

   @Test
    public void test_getDifferenceAsLong_long_long() {
        assertEquals(567L, MillisDurationField.INSTANCE.getDifferenceAsLong(567L, 0L));
        assertEquals(567L - 1234L, MillisDurationField.INSTANCE.getDifferenceAsLong(567L, 1234L));
        assertEquals(567L + 1234L, MillisDurationField.INSTANCE.getDifferenceAsLong(567L, -1234L));
        try {
            MillisDurationField.INSTANCE.getDifferenceAsLong(Long.MAX_VALUE, -1L);
            fail();
        } catch (ArithmeticException ex) {}
    }

    //-----------------------------------------------------------------------
   @Test
    public void test_compareTo() {
        assertEquals(0, MillisDurationField.INSTANCE.compareTo(MillisDurationField.INSTANCE));
        assertEquals(-1, MillisDurationField.INSTANCE.compareTo(ISOChronology.getInstance().seconds()));
        DurationField dummy = new PreciseDurationField(DurationFieldType.seconds(), 0);
        assertEquals(1, MillisDurationField.INSTANCE.compareTo(dummy));
//        try {
//            MillisDurationField.INSTANCE.compareTo("");
//            fail();
//        } catch (ClassCastException ex) {}
        try {
            MillisDurationField.INSTANCE.compareTo(null);
            fail();
        } catch (NullPointerException ex) {}
    }

    //-----------------------------------------------------------------------
   @Test
    public void testSerialization() throws Exception {
        DurationField test = MillisDurationField.INSTANCE;
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(test);
        byte[] bytes = baos.toByteArray();
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        DurationField result = (DurationField) ois.readObject();
        ois.close();
        
        assertSame(test, result);
    }

}
