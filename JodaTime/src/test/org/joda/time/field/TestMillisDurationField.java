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
 * This class is a Junit unit test for PeriodFormatterBuilder.
 *
 * @author Stephen Colebourne
 */
public class TestMillisDurationField extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestMillisDurationField.class);
    }

    public TestMillisDurationField(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    //-----------------------------------------------------------------------
    public void test_getType() {
        assertEquals(DurationFieldType.millis(), MillisDurationField.INSTANCE.getType());
    }

    public void test_getName() {
        assertEquals("millis", MillisDurationField.INSTANCE.getName());
    }
    
    public void test_isSupported() {
        assertEquals(true, MillisDurationField.INSTANCE.isSupported());
    }

    public void test_isPrecise() {
        assertEquals(true, MillisDurationField.INSTANCE.isPrecise());
    }

    public void test_getUnitMillis() {
        assertEquals(1, MillisDurationField.INSTANCE.getUnitMillis());
    }

    public void test_toString() {
        assertEquals("DurationField[millis]", MillisDurationField.INSTANCE.toString());
    }
    
    //-----------------------------------------------------------------------
    public void test_getValue_long() {
        assertEquals(0, MillisDurationField.INSTANCE.getValue(0L));
        assertEquals(1234, MillisDurationField.INSTANCE.getValue(1234L));
        assertEquals(-1234, MillisDurationField.INSTANCE.getValue(-1234L));
        try {
            MillisDurationField.INSTANCE.getValue(((long) (Integer.MAX_VALUE)) + 1L);
            fail();
        } catch (ArithmeticException ex) {}
    }

    public void test_getValueAsLong_long() {
        assertEquals(0L, MillisDurationField.INSTANCE.getValueAsLong(0L));
        assertEquals(1234L, MillisDurationField.INSTANCE.getValueAsLong(1234L));
        assertEquals(-1234L, MillisDurationField.INSTANCE.getValueAsLong(-1234L));
        assertEquals(((long) (Integer.MAX_VALUE)) + 1L, MillisDurationField.INSTANCE.getValueAsLong(((long) (Integer.MAX_VALUE)) + 1L));
    }

    public void test_getValue_long_long() {
        assertEquals(0, MillisDurationField.INSTANCE.getValue(0L, 567L));
        assertEquals(1234, MillisDurationField.INSTANCE.getValue(1234L, 567L));
        assertEquals(-1234, MillisDurationField.INSTANCE.getValue(-1234L, 567L));
        try {
            MillisDurationField.INSTANCE.getValue(((long) (Integer.MAX_VALUE)) + 1L, 567L);
            fail();
        } catch (ArithmeticException ex) {}
    }

    public void test_getValueAsLong_long_long() {
        assertEquals(0L, MillisDurationField.INSTANCE.getValueAsLong(0L, 567L));
        assertEquals(1234L, MillisDurationField.INSTANCE.getValueAsLong(1234L, 567L));
        assertEquals(-1234L, MillisDurationField.INSTANCE.getValueAsLong(-1234L, 567L));
        assertEquals(((long) (Integer.MAX_VALUE)) + 1L, MillisDurationField.INSTANCE.getValueAsLong(((long) (Integer.MAX_VALUE)) + 1L, 567L));
    }

    //-----------------------------------------------------------------------
    public void test_getMillis_int() {
        assertEquals(0, MillisDurationField.INSTANCE.getMillis(0));
        assertEquals(1234, MillisDurationField.INSTANCE.getMillis(1234));
        assertEquals(-1234, MillisDurationField.INSTANCE.getMillis(-1234));
    }

    public void test_getMillis_long() {
        assertEquals(0L, MillisDurationField.INSTANCE.getMillis(0L));
        assertEquals(1234L, MillisDurationField.INSTANCE.getMillis(1234L));
        assertEquals(-1234L, MillisDurationField.INSTANCE.getMillis(-1234L));
    }

    public void test_getMillis_int_long() {
        assertEquals(0, MillisDurationField.INSTANCE.getMillis(0, 567L));
        assertEquals(1234, MillisDurationField.INSTANCE.getMillis(1234, 567L));
        assertEquals(-1234, MillisDurationField.INSTANCE.getMillis(-1234, 567L));
    }

    public void test_getMillis_long_long() {
        assertEquals(0L, MillisDurationField.INSTANCE.getMillis(0L, 567L));
        assertEquals(1234L, MillisDurationField.INSTANCE.getMillis(1234L, 567L));
        assertEquals(-1234L, MillisDurationField.INSTANCE.getMillis(-1234L, 567L));
    }

    //-----------------------------------------------------------------------
    public void test_add_long_int() {
        assertEquals(567L, MillisDurationField.INSTANCE.add(567L, 0));
        assertEquals(567L + 1234L, MillisDurationField.INSTANCE.add(567L, 1234));
        assertEquals(567L - 1234L, MillisDurationField.INSTANCE.add(567L, -1234));
        try {
            MillisDurationField.INSTANCE.add(Long.MAX_VALUE, 1);
            fail();
        } catch (ArithmeticException ex) {}
    }

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
    public void test_getDifference_long_int() {
        assertEquals(567, MillisDurationField.INSTANCE.getDifference(567L, 0L));
        assertEquals(567 - 1234, MillisDurationField.INSTANCE.getDifference(567L, 1234L));
        assertEquals(567 + 1234, MillisDurationField.INSTANCE.getDifference(567L, -1234L));
        try {
            MillisDurationField.INSTANCE.getDifference(Long.MAX_VALUE, 1L);
            fail();
        } catch (ArithmeticException ex) {}
    }

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
    public void test_compareTo() {
        assertEquals(0, MillisDurationField.INSTANCE.compareTo(MillisDurationField.INSTANCE));
        assertEquals(-1, MillisDurationField.INSTANCE.compareTo(Chronology.getISO().seconds()));
        DurationField dummy = new PreciseDurationField(DurationFieldType.seconds(), 0);
        assertEquals(1, MillisDurationField.INSTANCE.compareTo(dummy));
        try {
            MillisDurationField.INSTANCE.compareTo("");
            fail();
        } catch (ClassCastException ex) {}
        try {
            MillisDurationField.INSTANCE.compareTo(null);
            fail();
        } catch (NullPointerException ex) {}
    }

    //-----------------------------------------------------------------------
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
