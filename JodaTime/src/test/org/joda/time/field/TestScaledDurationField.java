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
package org.joda.time.field;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

/**
 * This class is a Junit unit test for PreciseDurationField.
 *
 * @author Stephen Colebourne
 */
public class TestScaledDurationField extends TestCase {
    
    private static final long LONG_INTEGER_MAX = Integer.MAX_VALUE;
    private static final int INTEGER_MAX = Integer.MAX_VALUE;
    private static final long LONG_MAX = Long.MAX_VALUE;
    
    private ScaledDurationField iField;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestScaledDurationField.class);
    }

    public TestScaledDurationField(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        DurationField base = MillisDurationField.INSTANCE;
        iField = new ScaledDurationField(base, DurationFieldType.minutes(), 90);
    }

    protected void tearDown() throws Exception {
        iField = null;
    }

    //-----------------------------------------------------------------------
    public void test_constructor() {
        try {
            new ScaledDurationField(null, DurationFieldType.minutes(), 10);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new ScaledDurationField(MillisDurationField.INSTANCE, null, 10);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new ScaledDurationField(MillisDurationField.INSTANCE, DurationFieldType.minutes(), 0);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new ScaledDurationField(MillisDurationField.INSTANCE, DurationFieldType.minutes(), 1);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void test_getScalar() {
        assertEquals(90, iField.getScalar());
    }

    //-----------------------------------------------------------------------
    public void test_getType() {
        assertEquals(DurationFieldType.minutes(), iField.getType());
    }

    public void test_getName() {
        assertEquals("minutes", iField.getName());
    }
    
    public void test_isSupported() {
        assertEquals(true, iField.isSupported());
    }

    public void test_isPrecise() {
        assertEquals(true, iField.isPrecise());
    }

    public void test_getUnitMillis() {
        assertEquals(90, iField.getUnitMillis());
    }

    public void test_toString() {
        assertEquals("DurationField[minutes]", iField.toString());
    }

    //-----------------------------------------------------------------------
    public void test_getValue_long() {
        assertEquals(0, iField.getValue(0L));
        assertEquals(12345678 / 90, iField.getValue(12345678L));
        assertEquals(-1234 / 90, iField.getValue(-1234L));
        assertEquals(INTEGER_MAX / 90, iField.getValue(LONG_INTEGER_MAX));
        try {
            iField.getValue(LONG_INTEGER_MAX + 1L);
            fail();
        } catch (ArithmeticException ex) {}
    }

    public void test_getValueAsLong_long() {
        assertEquals(0L, iField.getValueAsLong(0L));
        assertEquals(12345678L / 90, iField.getValueAsLong(12345678L));
        assertEquals(-1234 / 90L, iField.getValueAsLong(-1234L));
        assertEquals(LONG_INTEGER_MAX + 1L, iField.getValueAsLong(LONG_INTEGER_MAX * 90L + 90L));
    }

    public void test_getValue_long_long() {
        assertEquals(0, iField.getValue(0L, 567L));
        assertEquals(12345678 / 90, iField.getValue(12345678L, 567L));
        assertEquals(-1234 / 90, iField.getValue(-1234L, 567L));
        assertEquals(INTEGER_MAX / 90, iField.getValue(LONG_INTEGER_MAX, 567L));
        try {
            iField.getValue(LONG_INTEGER_MAX + 1L, 567L);
            fail();
        } catch (ArithmeticException ex) {}
    }

    public void test_getValueAsLong_long_long() {
        assertEquals(0L, iField.getValueAsLong(0L, 567L));
        assertEquals(12345678 / 90L, iField.getValueAsLong(12345678L, 567L));
        assertEquals(-1234 / 90L, iField.getValueAsLong(-1234L, 567L));
        assertEquals(LONG_INTEGER_MAX + 1L, iField.getValueAsLong(LONG_INTEGER_MAX * 90L + 90L, 567L));
    }

    //-----------------------------------------------------------------------
    public void test_getMillis_int() {
        assertEquals(0, iField.getMillis(0));
        assertEquals(1234L * 90L, iField.getMillis(1234));
        assertEquals(-1234L * 90L, iField.getMillis(-1234));
        assertEquals(LONG_INTEGER_MAX * 90L, iField.getMillis(INTEGER_MAX));
    }

    public void test_getMillis_long() {
        assertEquals(0L, iField.getMillis(0L));
        assertEquals(1234L * 90L, iField.getMillis(1234L));
        assertEquals(-1234L * 90L, iField.getMillis(-1234L));
        try {
            iField.getMillis(LONG_MAX);
            fail();
        } catch (ArithmeticException ex) {}
    }

    public void test_getMillis_int_long() {
        assertEquals(0L, iField.getMillis(0, 567L));
        assertEquals(1234L * 90L, iField.getMillis(1234, 567L));
        assertEquals(-1234L * 90L, iField.getMillis(-1234, 567L));
        assertEquals(LONG_INTEGER_MAX * 90L, iField.getMillis(INTEGER_MAX, 567L));
    }

    public void test_getMillis_long_long() {
        assertEquals(0L, iField.getMillis(0L, 567L));
        assertEquals(1234L * 90L, iField.getMillis(1234L, 567L));
        assertEquals(-1234L * 90L, iField.getMillis(-1234L, 567L));
        try {
            iField.getMillis(LONG_MAX, 567L);
            fail();
        } catch (ArithmeticException ex) {}
    }

    //-----------------------------------------------------------------------
    public void test_add_long_int() {
        assertEquals(567L, iField.add(567L, 0));
        assertEquals(567L + 1234L * 90L, iField.add(567L, 1234));
        assertEquals(567L - 1234L * 90L, iField.add(567L, -1234));
        try {
            iField.add(LONG_MAX, 1);
            fail();
        } catch (ArithmeticException ex) {}
    }

    public void test_add_long_long() {
        assertEquals(567L, iField.add(567L, 0L));
        assertEquals(567L + 1234L * 90L, iField.add(567L, 1234L));
        assertEquals(567L - 1234L * 90L, iField.add(567L, -1234L));
        try {
            iField.add(LONG_MAX, 1L);
            fail();
        } catch (ArithmeticException ex) {}
        try {
            iField.add(1L, LONG_MAX);
            fail();
        } catch (ArithmeticException ex) {}
    }

    //-----------------------------------------------------------------------
    public void test_getDifference_long_int() {
        assertEquals(0, iField.getDifference(1L, 0L));
        assertEquals(567, iField.getDifference(567L * 90L, 0L));
        assertEquals(567 - 1234, iField.getDifference(567L * 90L, 1234L * 90L));
        assertEquals(567 + 1234, iField.getDifference(567L * 90L, -1234L * 90L));
        try {
            iField.getDifference(LONG_MAX, -1L);
            fail();
        } catch (ArithmeticException ex) {}
    }

    public void test_getDifferenceAsLong_long_long() {
        assertEquals(0L, iField.getDifferenceAsLong(1L, 0L));
        assertEquals(567L, iField.getDifferenceAsLong(567L * 90L, 0L));
        assertEquals(567L - 1234L, iField.getDifferenceAsLong(567L * 90L, 1234L * 90L));
        assertEquals(567L + 1234L, iField.getDifferenceAsLong(567L * 90L, -1234L * 90L));
        try {
            iField.getDifferenceAsLong(LONG_MAX, -1L);
            fail();
        } catch (ArithmeticException ex) {}
    }

    //-----------------------------------------------------------------------
    public void test_equals() {
        assertEquals(true, iField.equals(iField));
        assertEquals(false, iField.equals(Chronology.getISO().minutes()));
        DurationField dummy = new ScaledDurationField(MillisDurationField.INSTANCE, DurationFieldType.minutes(), 2);
        assertEquals(false, iField.equals(dummy));
        dummy = new ScaledDurationField(MillisDurationField.INSTANCE, DurationFieldType.minutes(), 90);
        assertEquals(true, iField.equals(dummy));
        dummy = new ScaledDurationField(MillisDurationField.INSTANCE, DurationFieldType.millis(), 90);
        assertEquals(false, iField.equals(dummy));
        assertEquals(false, iField.equals(""));
        assertEquals(false, iField.equals(null));
    }

    public void test_hashCode() {
        assertEquals(iField.hashCode(), iField.hashCode());
        assertEquals(false, iField.hashCode() == Chronology.getISO().minutes().hashCode());
        DurationField dummy = new ScaledDurationField(MillisDurationField.INSTANCE, DurationFieldType.minutes(), 2);
        assertEquals(false, iField.hashCode() == dummy.hashCode());
        dummy = new ScaledDurationField(MillisDurationField.INSTANCE, DurationFieldType.minutes(), 90);
        assertEquals(true, iField.hashCode() == dummy.hashCode());
        dummy = new ScaledDurationField(MillisDurationField.INSTANCE, DurationFieldType.millis(), 90);
        assertEquals(false, iField.hashCode() == dummy.hashCode());
    }

    //-----------------------------------------------------------------------
    public void test_compareTo() {
        assertEquals(0, iField.compareTo(iField));
        assertEquals(-1, iField.compareTo(Chronology.getISO().minutes()));
        DurationField dummy = new PreciseDurationField(DurationFieldType.minutes(), 0);
        assertEquals(1, iField.compareTo(dummy));
        try {
            iField.compareTo("");
            fail();
        } catch (ClassCastException ex) {}
        try {
            iField.compareTo(null);
            fail();
        } catch (NullPointerException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testSerialization() throws Exception {
        DurationField test = iField;
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(test);
        byte[] bytes = baos.toByteArray();
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        DurationField result = (DurationField) ois.readObject();
        ois.close();
        
        assertEquals(test, result);
    }

}
