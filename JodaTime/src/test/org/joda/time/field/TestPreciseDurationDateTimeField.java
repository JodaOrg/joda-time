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
package org.joda.time.field;

import java.util.Arrays;
import java.util.Locale;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.ISOChronology;

/**
 * This class is a Junit unit test for PreciseDurationDateTimeField.
 *
 * @author Stephen Colebourne
 */
public class TestPreciseDurationDateTimeField extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestPreciseDurationDateTimeField.class);
    }

    public TestPreciseDurationDateTimeField(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    //-----------------------------------------------------------------------
    public void test_constructor() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(DateTimeFieldType.secondOfMinute(), field.getType());
        try {
            field = new MockPreciseDurationDateTimeField(null, null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            field = new MockPreciseDurationDateTimeField(
                DateTimeFieldType.minuteOfHour(),
                new MockImpreciseDurationField(DurationFieldType.minutes()));
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            field = new MockPreciseDurationDateTimeField(
                DateTimeFieldType.minuteOfHour(),
                new MockZeroDurationField(DurationFieldType.minutes()));
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void test_getType() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField(
            DateTimeFieldType.secondOfDay(), new MockCountingDurationField(DurationFieldType.minutes()));
        assertEquals(DateTimeFieldType.secondOfDay(), field.getType());
    }

    public void test_getName() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField(
            DateTimeFieldType.secondOfDay(), new MockCountingDurationField(DurationFieldType.minutes()));
        assertEquals("secondOfDay", field.getName());
    }

    public void test_toString() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField(
            DateTimeFieldType.secondOfDay(), new MockCountingDurationField(DurationFieldType.minutes()));
        assertEquals("DateTimeField[secondOfDay]", field.toString());
    }

    public void test_isSupported() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(true, field.isSupported());
    }

    public void test_isLenient() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(false, field.isLenient());
    }

    public void test_get() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0, field.get(0));
        assertEquals(1, field.get(60));
        assertEquals(2, field.get(123));
    }

    //-----------------------------------------------------------------------
    public void test_getAsText_long_Locale() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("29", field.getAsText(60L * 29, Locale.ENGLISH));
        assertEquals("29", field.getAsText(60L * 29, null));
    }

    public void test_getAsText_long() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("29", field.getAsText(60L * 29));
    }

    public void test_getAsText_RP_int_Locale() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("20", field.getAsText(new TimeOfDay(12, 30, 40, 50), 20, Locale.ENGLISH));
        assertEquals("20", field.getAsText(new TimeOfDay(12, 30, 40, 50), 20, null));
    }

    public void test_getAsText_RP_Locale() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("40", field.getAsText(new TimeOfDay(12, 30, 40, 50), Locale.ENGLISH));
        assertEquals("40", field.getAsText(new TimeOfDay(12, 30, 40, 50), null));
    }

    public void test_getAsText_int_Locale() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("80", field.getAsText(80, Locale.ENGLISH));
        assertEquals("80", field.getAsText(80, null));
    }

    //-----------------------------------------------------------------------
    public void test_getAsShortText_long_Locale() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("29", field.getAsShortText(60L * 29, Locale.ENGLISH));
        assertEquals("29", field.getAsShortText(60L * 29, null));
    }

    public void test_getAsShortText_long() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("29", field.getAsShortText(60L * 29));
    }

    public void test_getAsShortText_RP_int_Locale() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("20", field.getAsShortText(new TimeOfDay(12, 30, 40, 50), 20, Locale.ENGLISH));
        assertEquals("20", field.getAsShortText(new TimeOfDay(12, 30, 40, 50), 20, null));
    }

    public void test_getAsShortText_RP_Locale() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("40", field.getAsShortText(new TimeOfDay(12, 30, 40, 50), Locale.ENGLISH));
        assertEquals("40", field.getAsShortText(new TimeOfDay(12, 30, 40, 50), null));
    }

    public void test_getAsShortText_int_Locale() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("80", field.getAsShortText(80, Locale.ENGLISH));
        assertEquals("80", field.getAsShortText(80, null));
    }

    //-----------------------------------------------------------------------
    public void test_add_long_int() {
        MockCountingDurationField.add_int = 0;
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(61, field.add(1L, 1));
        assertEquals(1, MockCountingDurationField.add_int);
    }

    public void test_add_long_long() {
        MockCountingDurationField.add_long = 0;
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(61, field.add(1L, 1L));
        assertEquals(1, MockCountingDurationField.add_long);
    }

    public void test_add_RP_int_intarray_int() {
        int[] values = new int[] {10, 20, 30, 40};
        int[] expected = new int[] {10, 20, 30, 40};
        BaseDateTimeField field = new MockStandardBaseDateTimeField();
        int[] result = field.add(new TimeOfDay(), 2, values, 0);
        assertEquals(true, Arrays.equals(expected, result));
        
        values = new int[] {10, 20, 30, 40};
        expected = new int[] {10, 20, 31, 40};
        result = field.add(new TimeOfDay(), 2, values, 1);
        assertEquals(true, Arrays.equals(expected, result));
        
        values = new int[] {10, 20, 30, 40};
        expected = new int[] {10, 21, 0, 40};
        result = field.add(new TimeOfDay(), 2, values, 30);
        assertEquals(true, Arrays.equals(expected, result));
        
        values = new int[] {23, 59, 30, 40};
        try {
            field.add(new TimeOfDay(), 2, values, 30);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        values = new int[] {10, 20, 30, 40};
        expected = new int[] {10, 20, 29, 40};
        result = field.add(new TimeOfDay(), 2, values, -1);
        assertEquals(true, Arrays.equals(expected, result));
        
        values = new int[] {10, 20, 30, 40};
        expected = new int[] {10, 19, 59, 40};
        result = field.add(new TimeOfDay(), 2, values, -31);
        assertEquals(true, Arrays.equals(expected, result));
        
        values = new int[] {0, 0, 30, 40};
        try {
            field.add(new TimeOfDay(), 2, values, -31);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void test_addWrapField_long_int() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(29 * 60L, field.addWrapField(60L * 29, 0));
        assertEquals(59 * 60L, field.addWrapField(60L * 29, 30));
        assertEquals(0 * 60L, field.addWrapField(60L * 29, 31));
    }

    public void test_addWrapField_RP_int_intarray_int() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        int[] values = new int[] {10, 20, 30, 40};
        int[] expected = new int[] {10, 20, 30, 40};
        int[] result = field.addWrapField(new TimeOfDay(), 2, values, 0);
        assertEquals(true, Arrays.equals(result, expected));
        
        values = new int[] {10, 20, 30, 40};
        expected = new int[] {10, 20, 59, 40};
        result = field.addWrapField(new TimeOfDay(), 2, values, 29);
        assertEquals(true, Arrays.equals(result, expected));
        
        values = new int[] {10, 20, 30, 40};
        expected = new int[] {10, 20, 0, 40};
        result = field.addWrapField(new TimeOfDay(), 2, values, 30);
        assertEquals(true, Arrays.equals(result, expected));
        
        values = new int[] {10, 20, 30, 40};
        expected = new int[] {10, 20, 1, 40};
        result = field.addWrapField(new TimeOfDay(), 2, values, 31);
        assertEquals(true, Arrays.equals(result, expected));
    }

    //-----------------------------------------------------------------------
    public void test_getDifference_long_long() {
        MockCountingDurationField.difference_long = 0;
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(30, field.getDifference(0L, 0L));
        assertEquals(1, MockCountingDurationField.difference_long);
    }

    public void test_getDifferenceAsLong_long_long() {
        MockCountingDurationField.difference_long = 0;
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(30, field.getDifferenceAsLong(0L, 0L));
        assertEquals(1, MockCountingDurationField.difference_long);
    }

    //-----------------------------------------------------------------------
    public void test_set_long_int() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0, field.set(120L, 0));
        assertEquals(29 * 60, field.set(120L, 29));
    }

    public void test_set_RP_int_intarray_int() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        int[] values = new int[] {10, 20, 30, 40};
        int[] expected = new int[] {10, 20, 30, 40};
        int[] result = field.set(new TimeOfDay(), 2, values, 30);
        assertEquals(true, Arrays.equals(result, expected));
        
        values = new int[] {10, 20, 30, 40};
        expected = new int[] {10, 20, 29, 40};
        result = field.set(new TimeOfDay(), 2, values, 29);
        assertEquals(true, Arrays.equals(result, expected));
        
        values = new int[] {10, 20, 30, 40};
        expected = new int[] {10, 20, 30, 40};
        try {
            field.set(new TimeOfDay(), 2, values, 60);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals(true, Arrays.equals(values, expected));
        
        values = new int[] {10, 20, 30, 40};
        expected = new int[] {10, 20, 30, 40};
        try {
            field.set(new TimeOfDay(), 2, values, -1);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals(true, Arrays.equals(values, expected));
    }

    public void test_set_long_String_Locale() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0, field.set(0L, "0", null));
        assertEquals(29 * 60, field.set(0L, "29", Locale.ENGLISH));
    }

    public void test_set_long_String() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0, field.set(0L, "0"));
        assertEquals(29 * 60, field.set(0L, "29"));
    }

    public void test_set_RP_int_intarray_String_Locale() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        int[] values = new int[] {10, 20, 30, 40};
        int[] expected = new int[] {10, 20, 30, 40};
        int[] result = field.set(new TimeOfDay(), 2, values, "30", null);
        assertEquals(true, Arrays.equals(result, expected));
        
        values = new int[] {10, 20, 30, 40};
        expected = new int[] {10, 20, 29, 40};
        result = field.set(new TimeOfDay(), 2, values, "29", Locale.ENGLISH);
        assertEquals(true, Arrays.equals(result, expected));
        
        values = new int[] {10, 20, 30, 40};
        expected = new int[] {10, 20, 30, 40};
        try {
            field.set(new TimeOfDay(), 2, values, "60", null);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals(true, Arrays.equals(values, expected));
        
        values = new int[] {10, 20, 30, 40};
        expected = new int[] {10, 20, 30, 40};
        try {
            field.set(new TimeOfDay(), 2, values, "-1", null);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals(true, Arrays.equals(values, expected));
    }

    public void test_convertText() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0, field.convertText("0", null));
        assertEquals(29, field.convertText("29", null));
        try {
            field.convertText("2A", null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            field.convertText(null, null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //------------------------------------------------------------------------
//    public abstract DurationField getDurationField();
//
//    public abstract DurationField getRangeDurationField();

    public void test_isLeap_long() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(false, field.isLeap(0L));
    }

    public void test_getLeapAmount_long() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0, field.getLeapAmount(0L));
    }

    public void test_getLeapDurationField() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(null, field.getLeapDurationField());
    }

    //-----------------------------------------------------------------------
    public void test_getMinimumValue() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0, field.getMinimumValue());
    }

    public void test_getMinimumValue_long() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0, field.getMinimumValue(0L));
    }

    public void test_getMinimumValue_RP() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0, field.getMinimumValue(new TimeOfDay()));
    }

    public void test_getMinimumValue_RP_intarray() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0, field.getMinimumValue(new TimeOfDay(), new int[4]));
    }

    public void test_getMaximumValue() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(59, field.getMaximumValue());
    }

    public void test_getMaximumValue_long() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(59, field.getMaximumValue(0L));
    }

    public void test_getMaximumValue_RP() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(59, field.getMaximumValue(new TimeOfDay()));
    }

    public void test_getMaximumValue_RP_intarray() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(59, field.getMaximumValue(new TimeOfDay(), new int[4]));
    }

    //-----------------------------------------------------------------------
    public void test_getMaximumTextLength_Locale() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(2, field.getMaximumTextLength(Locale.ENGLISH));

        field = new MockPreciseDurationDateTimeField() {
            public int getMaximumValue() {
                return 5;
            }
        };
        assertEquals(1, field.getMaximumTextLength(Locale.ENGLISH));
        
        field = new MockPreciseDurationDateTimeField() {
            public int getMaximumValue() {
                return 555;
            }
        };
        assertEquals(3, field.getMaximumTextLength(Locale.ENGLISH));
        
        field = new MockPreciseDurationDateTimeField() {
            public int getMaximumValue() {
                return 5555;
            }
        };
        assertEquals(4, field.getMaximumTextLength(Locale.ENGLISH));
        
        field = new MockPreciseDurationDateTimeField() {
            public int getMaximumValue() {
                return -1;
            }
        };
        assertEquals(2, field.getMaximumTextLength(Locale.ENGLISH));
    }

    public void test_getMaximumShortTextLength_Locale() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(2, field.getMaximumShortTextLength(Locale.ENGLISH));
    }

    //------------------------------------------------------------------------
    public void test_roundFloor_long() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(-120L, field.roundFloor(-61L));
        assertEquals(-60L, field.roundFloor(-60L));
        assertEquals(-60L, field.roundFloor(-59L));
        assertEquals(-60L, field.roundFloor(-1L));
        assertEquals(0L, field.roundFloor(0L));
        assertEquals(0L, field.roundFloor(1L));
        assertEquals(0L, field.roundFloor(29L));
        assertEquals(0L, field.roundFloor(30L));
        assertEquals(0L, field.roundFloor(31L));
        assertEquals(60L, field.roundFloor(60L));
    }

    public void test_roundCeiling_long() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(-60L, field.roundCeiling(-61L));
        assertEquals(-60L, field.roundCeiling(-60L));
        assertEquals(0L, field.roundCeiling(-59L));
        assertEquals(0L, field.roundCeiling(-1L));
        assertEquals(0L, field.roundCeiling(0L));
        assertEquals(60L, field.roundCeiling(1L));
        assertEquals(60L, field.roundCeiling(29L));
        assertEquals(60L, field.roundCeiling(30L));
        assertEquals(60L, field.roundCeiling(31L));
        assertEquals(60L, field.roundCeiling(60L));
    }

    public void test_roundHalfFloor_long() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0L, field.roundHalfFloor(0L));
        assertEquals(0L, field.roundHalfFloor(29L));
        assertEquals(0L, field.roundHalfFloor(30L));
        assertEquals(60L, field.roundHalfFloor(31L));
        assertEquals(60L, field.roundHalfFloor(60L));
    }

    public void test_roundHalfCeiling_long() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0L, field.roundHalfCeiling(0L));
        assertEquals(0L, field.roundHalfCeiling(29L));
        assertEquals(60L, field.roundHalfCeiling(30L));
        assertEquals(60L, field.roundHalfCeiling(31L));
        assertEquals(60L, field.roundHalfCeiling(60L));
    }

    public void test_roundHalfEven_long() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0L, field.roundHalfEven(0L));
        assertEquals(0L, field.roundHalfEven(29L));
        assertEquals(0L, field.roundHalfEven(30L));
        assertEquals(60L, field.roundHalfEven(31L));
        assertEquals(60L, field.roundHalfEven(60L));
        assertEquals(60L, field.roundHalfEven(89L));
        assertEquals(120L, field.roundHalfEven(90L));
        assertEquals(120L, field.roundHalfEven(91L));
    }

    public void test_remainder_long() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0L, field.remainder(0L));
        assertEquals(29L, field.remainder(29L));
        assertEquals(30L, field.remainder(30L));
        assertEquals(31L, field.remainder(31L));
        assertEquals(0L, field.remainder(60L));
    }

    //-----------------------------------------------------------------------
    static class MockPreciseDurationDateTimeField extends PreciseDurationDateTimeField {
        protected MockPreciseDurationDateTimeField() {
            super(DateTimeFieldType.secondOfMinute(),
                new MockCountingDurationField(DurationFieldType.seconds()));
        }
        protected MockPreciseDurationDateTimeField(DateTimeFieldType type, DurationField dur) {
            super(type, dur);
        }
        public int get(long instant) {
            return (int) (instant / 60L);
        }
        public DurationField getRangeDurationField() {
            return new MockCountingDurationField(DurationFieldType.minutes());
        }
        public int getMaximumValue() {
            return 59;
        }
    }

    static class MockStandardBaseDateTimeField extends MockPreciseDurationDateTimeField {
        protected MockStandardBaseDateTimeField() {
            super();
        }
        public DurationField getDurationField() {
            return ISOChronology.getInstanceUTC().seconds();
        }
        public DurationField getRangeDurationField() {
            return ISOChronology.getInstanceUTC().minutes();
        }
    }

    //-----------------------------------------------------------------------
    static class MockCountingDurationField extends BaseDurationField {
        static int add_int = 0;
        static int add_long = 0;
        static int difference_long = 0;
        
        protected MockCountingDurationField(DurationFieldType type) {
            super(type);
        }
        public boolean isPrecise() {
            return true;
        }
        public long getUnitMillis() {
            return 60;
        }
        public long getValueAsLong(long duration, long instant) {
            return 0;
        }
        public long getMillis(int value, long instant) {
            return 0;
        }
        public long getMillis(long value, long instant) {
            return 0;
        }
        public long add(long instant, int value) {
            add_int++;
            return instant + (value * 60L);
        }
        public long add(long instant, long value) {
            add_long++;
            return instant + (value * 60L);
        }
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            difference_long++;
            return 30;
        }
    }

    //-----------------------------------------------------------------------
    static class MockZeroDurationField extends BaseDurationField {
        protected MockZeroDurationField(DurationFieldType type) {
            super(type);
        }
        public boolean isPrecise() {
            return true;
        }
        public long getUnitMillis() {
            return 0;  // this is zero
        }
        public long getValueAsLong(long duration, long instant) {
            return 0;
        }
        public long getMillis(int value, long instant) {
            return 0;
        }
        public long getMillis(long value, long instant) {
            return 0;
        }
        public long add(long instant, int value) {
            return 0;
        }
        public long add(long instant, long value) {
            return 0;
        }
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            return 0;
        }
    }

    //-----------------------------------------------------------------------
    static class MockImpreciseDurationField extends BaseDurationField {
        protected MockImpreciseDurationField(DurationFieldType type) {
            super(type);
        }
        public boolean isPrecise() {
            return false;  // this is false
        }
        public long getUnitMillis() {
            return 0;
        }
        public long getValueAsLong(long duration, long instant) {
            return 0;
        }
        public long getMillis(int value, long instant) {
            return 0;
        }
        public long getMillis(long value, long instant) {
            return 0;
        }
        public long add(long instant, int value) {
            return 0;
        }
        public long add(long instant, long value) {
            return 0;
        }
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            return 0;
        }
    }

}
