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
package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.base.AbstractPartial;
import org.joda.time.field.AbstractPartialFieldProperty;

/**
 * This class is a Junit unit test for YearMonthDay.
 *
 * @author Stephen Colebourne
 */
public class TestAbstractPartial extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.getInstance("Europe/Paris");
    
    private long TEST_TIME_NOW =
            (31L + 28L + 31L + 30L + 31L + 9L -1L) * DateTimeConstants.MILLIS_PER_DAY;
            
    private long TEST_TIME1 =
        (31L + 28L + 31L + 6L -1L) * DateTimeConstants.MILLIS_PER_DAY
        + 12L * DateTimeConstants.MILLIS_PER_HOUR
        + 24L * DateTimeConstants.MILLIS_PER_MINUTE;
        
    private long TEST_TIME2 =
        (365L + 31L + 28L + 31L + 30L + 7L -1L) * DateTimeConstants.MILLIS_PER_DAY
        + 14L * DateTimeConstants.MILLIS_PER_HOUR
        + 28L * DateTimeConstants.MILLIS_PER_MINUTE;
        
    private DateTimeZone zone = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestAbstractPartial.class);
    }

    public TestAbstractPartial(String name) {
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
    public void testGetValue() throws Throwable {
        MockPartial mock = new MockPartial();
        assertEquals(1970, mock.getValue(0));
        assertEquals(1, mock.getValue(1));
        
        try {
            mock.getValue(-1);
            fail();
        } catch (IndexOutOfBoundsException ex) {}
        try {
            mock.getValue(2);
            fail();
        } catch (IndexOutOfBoundsException ex) {}
    }

    public void testGetValues() throws Throwable {
        MockPartial mock = new MockPartial();
        int[] vals = mock.getValues();
        assertEquals(2, vals.length);
        assertEquals(1970, vals[0]);
        assertEquals(1, vals[1]);
    }

    public void testGetField() throws Throwable {
        MockPartial mock = new MockPartial();
        assertEquals(Chronology.getBuddhistUTC().year(), mock.getField(0));
        assertEquals(Chronology.getBuddhistUTC().monthOfYear(), mock.getField(1));
        
        try {
            mock.getField(-1);
            fail();
        } catch (IndexOutOfBoundsException ex) {}
        try {
            mock.getField(2);
            fail();
        } catch (IndexOutOfBoundsException ex) {}
    }

    public void testGetFieldType() throws Throwable {
        MockPartial mock = new MockPartial();
        assertEquals(DateTimeFieldType.year(), mock.getFieldType(0));
        assertEquals(DateTimeFieldType.monthOfYear(), mock.getFieldType(1));
        
        try {
            mock.getFieldType(-1);
            fail();
        } catch (IndexOutOfBoundsException ex) {}
        try {
            mock.getFieldType(2);
            fail();
        } catch (IndexOutOfBoundsException ex) {}
    }

    public void testGetFieldTypes() throws Throwable {
        MockPartial mock = new MockPartial();
        DateTimeFieldType[] vals = mock.getFieldTypes();
        assertEquals(2, vals.length);
        assertEquals(DateTimeFieldType.year(), vals[0]);
        assertEquals(DateTimeFieldType.monthOfYear(), vals[1]);
    }

    public void testGetPropertyEquals() throws Throwable {
        MockPartial mock = new MockPartial();
        YearMonthDay ymd = new YearMonthDay(1970, 2, 1, Chronology.getBuddhist());
        
        MockProperty0 prop0 = new MockProperty0();
        assertEquals(true, prop0.equals(prop0));
        assertEquals(true, prop0.equals(new MockProperty0()));
        assertEquals(false, prop0.equals(new MockProperty1()));
        assertEquals(false, prop0.equals(new MockProperty0Val()));
        assertEquals(false, prop0.equals(new MockProperty0Field()));
        assertEquals(false, prop0.equals(new MockProperty0Chrono()));
        assertEquals(false, prop0.equals(""));
        assertEquals(false, prop0.equals(null));
    }

    //-----------------------------------------------------------------------
    static class MockPartial extends AbstractPartial {
        
        int[] val = new int[] {1970, 1};
        
        MockPartial() {
            super();
        }

        protected DateTimeField getField(int index, Chronology chrono) {
            switch (index) {
                case 0:
                    return chrono.year();
                case 1:
                    return chrono.monthOfYear();
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        public int size() {
            return 2;
        }
        
        public int getValue(int index) {
            return val[index];
        }

        public void setValue(int index, int value) {
            val[index] = value;
        }

        public Chronology getChronology() {
            return Chronology.getBuddhistUTC();
        }
    }
    
    static class MockProperty0 extends AbstractPartialFieldProperty {
        MockPartial partial = new MockPartial();
        public DateTimeField getField() {
            return partial.getField(0);
        }
        public ReadablePartial getReadablePartial() {
            return partial;
        }
        public int get() {
            return partial.getValue(0);
        }
    }
    static class MockProperty1 extends AbstractPartialFieldProperty {
        MockPartial partial = new MockPartial();
        public DateTimeField getField() {
            return partial.getField(1);
        }
        public ReadablePartial getReadablePartial() {
            return partial;
        }
        public int get() {
            return partial.getValue(1);
        }
    }
    static class MockProperty0Field extends MockProperty0 {
        public DateTimeField getField() {
            return Chronology.getBuddhistUTC().hourOfDay();
        }
    }
    static class MockProperty0Val extends MockProperty0 {
        public int get() {
            return 99;
        }
    }
    static class MockProperty0Chrono extends MockProperty0 {
        public ReadablePartial getReadablePartial() {
            return new MockPartial() {
                public Chronology getChronology() {
                    return Chronology.getISOUTC();
                }
            };
        }
    }
}
