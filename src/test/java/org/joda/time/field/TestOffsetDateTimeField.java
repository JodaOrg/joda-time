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
 * This class is a Junit unit test for PreciseDateTimeField.
 *
 * @author Stephen Colebourne
 */
public class TestOffsetDateTimeField extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestOffsetDateTimeField.class);
    }

    public TestOffsetDateTimeField(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    //-----------------------------------------------------------------------
    public void test_constructor1() {
        OffsetDateTimeField field = new OffsetDateTimeField(
            ISOChronology.getInstance().secondOfMinute(), 3
        );
        assertEquals(DateTimeFieldType.secondOfMinute(), field.getType());
        assertEquals(3, field.getOffset());
        
        try {
            field = new OffsetDateTimeField(null, 3);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        try {
            field = new OffsetDateTimeField(ISOChronology.getInstance().secondOfMinute(), 0);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        try {
            field = new OffsetDateTimeField(UnsupportedDateTimeField.getInstance(
                DateTimeFieldType.secondOfMinute(), UnsupportedDurationField.getInstance(DurationFieldType.seconds())), 0);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void test_constructor2() {
        OffsetDateTimeField field = new OffsetDateTimeField(
            ISOChronology.getInstance().secondOfMinute(), DateTimeFieldType.secondOfDay(), 3
        );
        assertEquals(DateTimeFieldType.secondOfDay(), field.getType());
        assertEquals(3, field.getOffset());
        
        try {
            field = new OffsetDateTimeField(null, DateTimeFieldType.secondOfDay(), 3);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        try {
            field = new OffsetDateTimeField(ISOChronology.getInstance().secondOfMinute(), null, 3);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        try {
            field = new OffsetDateTimeField(ISOChronology.getInstance().secondOfMinute(), DateTimeFieldType.secondOfDay(), 0);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void test_getType() {
        OffsetDateTimeField field = new OffsetDateTimeField(
            ISOChronology.getInstance().secondOfMinute(), 3
        );
        assertEquals(DateTimeFieldType.secondOfMinute(), field.getType());
    }

    public void test_getName() {
        OffsetDateTimeField field = new OffsetDateTimeField(
            ISOChronology.getInstance().secondOfMinute(), 3
        );
        assertEquals("secondOfMinute", field.getName());
    }

    public void test_toString() {
        OffsetDateTimeField field = new OffsetDateTimeField(
            ISOChronology.getInstance().secondOfMinute(), 3
        );
        assertEquals("DateTimeField[secondOfMinute]", field.toString());
    }

    public void test_isSupported() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(true, field.isSupported());
    }

    public void test_isLenient() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(false, field.isLenient());
    }

    public void test_getOffset() {
        OffsetDateTimeField field = new OffsetDateTimeField(
            ISOChronology.getInstance().secondOfMinute(), 5
        );
        assertEquals(5, field.getOffset());
    }

    public void test_get() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(0 + 3, field.get(0));
        assertEquals(6 + 3, field.get(6000));
    }

    //-----------------------------------------------------------------------
    public void test_getAsText_long_Locale() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals("32", field.getAsText(1000L * 29, Locale.ENGLISH));
        assertEquals("32", field.getAsText(1000L * 29, null));
    }

    public void test_getAsText_long() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals("32", field.getAsText(1000L * 29));
    }

    public void test_getAsText_RP_int_Locale() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals("20", field.getAsText(new TimeOfDay(12, 30, 40, 50), 20, Locale.ENGLISH));
        assertEquals("20", field.getAsText(new TimeOfDay(12, 30, 40, 50), 20, null));
    }

    public void test_getAsText_RP_Locale() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals("40", field.getAsText(new TimeOfDay(12, 30, 40, 50), Locale.ENGLISH));
        assertEquals("40", field.getAsText(new TimeOfDay(12, 30, 40, 50), null));
    }

    public void test_getAsText_int_Locale() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals("80", field.getAsText(80, Locale.ENGLISH));
        assertEquals("80", field.getAsText(80, null));
    }

    //-----------------------------------------------------------------------
    public void test_getAsShortText_long_Locale() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals("32", field.getAsShortText(1000L * 29, Locale.ENGLISH));
        assertEquals("32", field.getAsShortText(1000L * 29, null));
    }

    public void test_getAsShortText_long() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals("32", field.getAsShortText(1000L * 29));
    }

    public void test_getAsShortText_RP_int_Locale() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals("20", field.getAsShortText(new TimeOfDay(12, 30, 40, 50), 20, Locale.ENGLISH));
        assertEquals("20", field.getAsShortText(new TimeOfDay(12, 30, 40, 50), 20, null));
    }

    public void test_getAsShortText_RP_Locale() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals("40", field.getAsShortText(new TimeOfDay(12, 30, 40, 50), Locale.ENGLISH));
        assertEquals("40", field.getAsShortText(new TimeOfDay(12, 30, 40, 50), null));
    }

    public void test_getAsShortText_int_Locale() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals("80", field.getAsShortText(80, Locale.ENGLISH));
        assertEquals("80", field.getAsShortText(80, null));
    }

    //-----------------------------------------------------------------------
    public void test_add_long_int() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(1001, field.add(1L, 1));
    }

    public void test_add_long_long() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(1001, field.add(1L, 1L));
    }

    public void test_add_RP_int_intarray_int() {
        int[] values = new int[] {10, 20, 30, 40};
        int[] expected = new int[] {10, 20, 30, 40};
        OffsetDateTimeField field = new MockStandardDateTimeField();
        int[] result = field.add(new TimeOfDay(), 2, values, 0);
        assertEquals(true, Arrays.equals(expected, result));
        
        values = new int[] {10, 20, 30, 40};
        expected = new int[] {10, 20, 31, 40};
        result = field.add(new TimeOfDay(), 2, values, 1);
        assertEquals(true, Arrays.equals(expected, result));
        
        values = new int[] {10, 20, 30, 40};
        expected = new int[] {10, 20, 62, 40};
        result = field.add(new TimeOfDay(), 2, values, 32);
        assertEquals(true, Arrays.equals(expected, result));
        
        values = new int[] {10, 20, 30, 40};
        expected = new int[] {10, 21, 3, 40};
        result = field.add(new TimeOfDay(), 2, values, 33);
        assertEquals(true, Arrays.equals(expected, result));
        
        values = new int[] {23, 59, 30, 40};
        try {
            field.add(new TimeOfDay(), 2, values, 33);
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
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(29 * 1000L, field.addWrapField(1000L * 29, 0));
        assertEquals(59 * 1000L, field.addWrapField(1000L * 29, 30));
        assertEquals(0L, field.addWrapField(1000L * 29, 31));
    }

    public void test_addWrapField_RP_int_intarray_int() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        int[] values = new int[] {10, 20, 30, 40};
        int[] expected = new int[] {10, 20, 30, 40};
        int[] result = field.addWrapField(new TimeOfDay(), 2, values, 0);
        assertEquals(true, Arrays.equals(result, expected));
        
        values = new int[] {10, 20, 30, 40};
        expected = new int[] {10, 20, 59, 40};
        result = field.addWrapField(new TimeOfDay(), 2, values, 29);
        assertEquals(true, Arrays.equals(result, expected));
        
        values = new int[] {10, 20, 30, 40};
        expected = new int[] {10, 20, 3, 40};
        result = field.addWrapField(new TimeOfDay(), 2, values, 33);
        assertEquals(true, Arrays.equals(result, expected));
    }

    //-----------------------------------------------------------------------
    public void test_getDifference_long_long() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(-21, field.getDifference(20000L, 41000L));
    }

    public void test_getDifferenceAsLong_long_long() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(-21L, field.getDifferenceAsLong(20000L, 41000L));
    }

    //-----------------------------------------------------------------------
    public void test_set_long_int() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(3120L, field.set(2120L, 6));
        assertEquals(26120L, field.set(120L, 29));
        assertEquals(57120L, field.set(2120L, 60));
    }

    public void test_set_RP_int_intarray_int() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
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
            field.set(new TimeOfDay(), 2, values, 63);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals(true, Arrays.equals(values, expected));
        
        values = new int[] {10, 20, 30, 40};
        expected = new int[] {10, 20, 30, 40};
        try {
            field.set(new TimeOfDay(), 2, values, 2);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals(true, Arrays.equals(values, expected));
    }

    public void test_set_long_String_Locale() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(3050L, field.set(50L, "6", null));
        assertEquals(26050L, field.set(50L, "29", Locale.ENGLISH));
    }

    public void test_set_long_String() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(3050L, field.set(50L, "6"));
        assertEquals(26050L, field.set(50L, "29"));
    }

    public void test_set_RP_int_intarray_String_Locale() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
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
            field.set(new TimeOfDay(), 2, values, "63", null);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals(true, Arrays.equals(values, expected));
        
        values = new int[] {10, 20, 30, 40};
        expected = new int[] {10, 20, 30, 40};
        try {
            field.set(new TimeOfDay(), 2, values, "2", null);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals(true, Arrays.equals(values, expected));
    }

    public void test_convertText() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
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
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(false, field.isLeap(0L));
    }

    public void test_getLeapAmount_long() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(0, field.getLeapAmount(0L));
    }

    public void test_getLeapDurationField() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(null, field.getLeapDurationField());
    }

    //-----------------------------------------------------------------------
    public void test_getMinimumValue() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(3, field.getMinimumValue());
    }

    public void test_getMinimumValue_long() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(3, field.getMinimumValue(0L));
    }

    public void test_getMinimumValue_RP() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(3, field.getMinimumValue(new TimeOfDay()));
    }

    public void test_getMinimumValue_RP_intarray() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(3, field.getMinimumValue(new TimeOfDay(), new int[4]));
    }

    public void test_getMaximumValue() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(62, field.getMaximumValue());
    }

    public void test_getMaximumValue_long() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(62, field.getMaximumValue(0L));
    }

    public void test_getMaximumValue_RP() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(62, field.getMaximumValue(new TimeOfDay()));
    }

    public void test_getMaximumValue_RP_intarray() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(62, field.getMaximumValue(new TimeOfDay(), new int[4]));
    }

    //-----------------------------------------------------------------------
    public void test_getMaximumTextLength_Locale() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(2, field.getMaximumTextLength(Locale.ENGLISH));
    }

    public void test_getMaximumShortTextLength_Locale() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(2, field.getMaximumShortTextLength(Locale.ENGLISH));
    }

    //------------------------------------------------------------------------
    public void test_roundFloor_long() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(-2000L, field.roundFloor(-1001L));
        assertEquals(-1000L, field.roundFloor(-1000L));
        assertEquals(-1000L, field.roundFloor(-999L));
        assertEquals(-1000L, field.roundFloor(-1L));
        assertEquals(0L, field.roundFloor(0L));
        assertEquals(0L, field.roundFloor(1L));
        assertEquals(0L, field.roundFloor(499L));
        assertEquals(0L, field.roundFloor(500L));
        assertEquals(0L, field.roundFloor(501L));
        assertEquals(1000L, field.roundFloor(1000L));
    }

    public void test_roundCeiling_long() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(-1000L, field.roundCeiling(-1001L));
        assertEquals(-1000L, field.roundCeiling(-1000L));
        assertEquals(0L, field.roundCeiling(-999L));
        assertEquals(0L, field.roundCeiling(-1L));
        assertEquals(0L, field.roundCeiling(0L));
        assertEquals(1000L, field.roundCeiling(1L));
        assertEquals(1000L, field.roundCeiling(499L));
        assertEquals(1000L, field.roundCeiling(500L));
        assertEquals(1000L, field.roundCeiling(501L));
        assertEquals(1000L, field.roundCeiling(1000L));
    }

    public void test_roundHalfFloor_long() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(0L, field.roundHalfFloor(0L));
        assertEquals(0L, field.roundHalfFloor(499L));
        assertEquals(0L, field.roundHalfFloor(500L));
        assertEquals(1000L, field.roundHalfFloor(501L));
        assertEquals(1000L, field.roundHalfFloor(1000L));
    }

    public void test_roundHalfCeiling_long() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(0L, field.roundHalfCeiling(0L));
        assertEquals(0L, field.roundHalfCeiling(499L));
        assertEquals(1000L, field.roundHalfCeiling(500L));
        assertEquals(1000L, field.roundHalfCeiling(501L));
        assertEquals(1000L, field.roundHalfCeiling(1000L));
    }

    public void test_roundHalfEven_long() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(0L, field.roundHalfEven(0L));
        assertEquals(0L, field.roundHalfEven(499L));
        assertEquals(0L, field.roundHalfEven(500L));
        assertEquals(1000L, field.roundHalfEven(501L));
        assertEquals(1000L, field.roundHalfEven(1000L));
        assertEquals(1000L, field.roundHalfEven(1499L));
        assertEquals(2000L, field.roundHalfEven(1500L));
        assertEquals(2000L, field.roundHalfEven(1501L));
    }

    public void test_remainder_long() {
        OffsetDateTimeField field = new MockOffsetDateTimeField();
        assertEquals(0L, field.remainder(0L));
        assertEquals(499L, field.remainder(499L));
        assertEquals(500L, field.remainder(500L));
        assertEquals(501L, field.remainder(501L));
        assertEquals(0L, field.remainder(1000L));
    }

    //-----------------------------------------------------------------------
    static class MockOffsetDateTimeField extends OffsetDateTimeField {
        protected MockOffsetDateTimeField() {
            super(ISOChronology.getInstance().secondOfMinute(), 3);
        }
    }

    static class MockStandardDateTimeField extends MockOffsetDateTimeField {
        protected MockStandardDateTimeField() {
            super();
        }
        public DurationField getDurationField() {
            return ISOChronology.getInstanceUTC().seconds();
        }
        public DurationField getRangeDurationField() {
            return ISOChronology.getInstanceUTC().minutes();
        }
    }

}
