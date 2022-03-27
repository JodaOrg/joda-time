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

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.base.AbstractPartial;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.AbstractPartialFieldProperty;

/**
 * This class is a Junit unit test for YearMonthDay.
 *
 * @author Stephen Colebourne
 */
public class TestAbstractPartial extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    
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

    @Override
    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        zone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    @Override
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
        assertEquals(BuddhistChronology.getInstanceUTC().year(), mock.getField(0));
        assertEquals(BuddhistChronology.getInstanceUTC().monthOfYear(), mock.getField(1));
        
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

        @Override
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
            return BuddhistChronology.getInstanceUTC();
        }
    }
    
    static class MockProperty0 extends AbstractPartialFieldProperty {
        MockPartial partial = new MockPartial();
        @Override
        public DateTimeField getField() {
            return partial.getField(0);
        }
        @Override
        public ReadablePartial getReadablePartial() {
            return partial;
        }
        @Override
        public int get() {
            return partial.getValue(0);
        }
    }
    static class MockProperty1 extends AbstractPartialFieldProperty {
        MockPartial partial = new MockPartial();
        @Override
        public DateTimeField getField() {
            return partial.getField(1);
        }
        @Override
        public ReadablePartial getReadablePartial() {
            return partial;
        }
        @Override
        public int get() {
            return partial.getValue(1);
        }
    }
    static class MockProperty0Field extends MockProperty0 {
        @Override
        public DateTimeField getField() {
            return BuddhistChronology.getInstanceUTC().hourOfDay();
        }
    }
    static class MockProperty0Val extends MockProperty0 {
        @Override
        public int get() {
            return 99;
        }
    }
    static class MockProperty0Chrono extends MockProperty0 {
        @Override
        public ReadablePartial getReadablePartial() {
            return new MockPartial() {
                @Override
                public Chronology getChronology() {
                    return ISOChronology.getInstanceUTC();
                }
            };
        }
    }
}
