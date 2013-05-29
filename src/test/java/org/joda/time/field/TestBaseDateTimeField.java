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

import org.joda.time.*;
import org.joda.time.base.BasePartial;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import java.util.Arrays;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;



/**
 * This class is a Junit unit test for BaseDateTimeField.
 *
 * @author Stephen Colebourne
 */
public class TestBaseDateTimeField  {
    //-----------------------------------------------------------------------
   @Test
    public void test_constructor() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(DateTimeFieldType.secondOfMinute(), field.getType());
        try {
            field = new MockBaseDateTimeField(null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

   @Test
    public void test_getType() {
        BaseDateTimeField field = new MockBaseDateTimeField(DateTimeFieldType.secondOfDay());
        assertEquals(DateTimeFieldType.secondOfDay(), field.getType());
    }

   @Test
    public void test_getName() {
        BaseDateTimeField field = new MockBaseDateTimeField(DateTimeFieldType.secondOfDay());
        assertEquals("secondOfDay", field.getName());
    }

   @Test
    public void test_toString() {
        BaseDateTimeField field = new MockBaseDateTimeField(DateTimeFieldType.secondOfDay());
        assertEquals("DateTimeField[secondOfDay]", field.toString());
    }

   @Test
    public void test_isSupported() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(true, field.isSupported());
    }

   @Test
    public void test_get() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(0, field.get(0));
        assertEquals(1, field.get(60));
        assertEquals(2, field.get(123));
    }

    //-----------------------------------------------------------------------
   @Test
    public void test_getAsText_long_Locale() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals("29", field.getAsText(60L * 29, Locale.ENGLISH));
        assertEquals("29", field.getAsText(60L * 29, null));
    }

   @Test
    public void test_getAsText_long() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals("29", field.getAsText(60L * 29));
    }

   @Test
    public void test_getAsText_RP_int_Locale() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals("20", field.getAsText(new TimeOfDay(12, 30, 40, 50), 20, Locale.ENGLISH));
        assertEquals("20", field.getAsText(new TimeOfDay(12, 30, 40, 50), 20, null));
    }

   @Test
    public void test_getAsText_RP_Locale() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals("40", field.getAsText(new TimeOfDay(12, 30, 40, 50), Locale.ENGLISH));
        assertEquals("40", field.getAsText(new TimeOfDay(12, 30, 40, 50), null));
    }

   @Test
    public void test_getAsText_int_Locale() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals("80", field.getAsText(80, Locale.ENGLISH));
        assertEquals("80", field.getAsText(80, null));
    }

    //-----------------------------------------------------------------------
   @Test
    public void test_getAsShortText_long_Locale() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals("29", field.getAsShortText(60L * 29, Locale.ENGLISH));
        assertEquals("29", field.getAsShortText(60L * 29, null));
    }

   @Test
    public void test_getAsShortText_long() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals("29", field.getAsShortText(60L * 29));
    }

   @Test
    public void test_getAsShortText_RP_int_Locale() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals("20", field.getAsShortText(new TimeOfDay(12, 30, 40, 50), 20, Locale.ENGLISH));
        assertEquals("20", field.getAsShortText(new TimeOfDay(12, 30, 40, 50), 20, null));
    }

   @Test
    public void test_getAsShortText_RP_Locale() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals("40", field.getAsShortText(new TimeOfDay(12, 30, 40, 50), Locale.ENGLISH));
        assertEquals("40", field.getAsShortText(new TimeOfDay(12, 30, 40, 50), null));
    }

   @Test
    public void test_getAsShortText_int_Locale() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals("80", field.getAsShortText(80, Locale.ENGLISH));
        assertEquals("80", field.getAsShortText(80, null));
    }

    //-----------------------------------------------------------------------
   @Test
    public void test_add_long_int() {
        MockCountingDurationField.add_int = 0;
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(61, field.add(1L, 1));
        assertEquals(1, MockCountingDurationField.add_int);
    }

   @Test
    public void test_add_long_long() {
        MockCountingDurationField.add_long = 0;
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(61, field.add(1L, 1L));
        assertEquals(1, MockCountingDurationField.add_long);
    }

   @Test
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
        
        values = new int[] {0, 0};
        try {
            field.add(new MockPartial(), 0, values, 1000);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        values = new int[] {1, 0};
        try {
            field.add(new MockPartial(), 0, values, -1000);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
   @Test
    public void test_addWrapField_long_int() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(1029, field.addWrapField(60L * 29, 0));
        assertEquals(1059, field.addWrapField(60L * 29, 30));
        assertEquals(1000, field.addWrapField(60L * 29, 31));
    }

   @Test
    public void test_addWrapField_RP_int_intarray_int() {
        BaseDateTimeField field = new MockBaseDateTimeField();
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
   @Test
    public void test_getDifference_long_long() {
        MockCountingDurationField.difference_long = 0;
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(30, field.getDifference(0L, 0L));
        assertEquals(1, MockCountingDurationField.difference_long);
    }

   @Test
    public void test_getDifferenceAsLong_long_long() {
        MockCountingDurationField.difference_long = 0;
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(30, field.getDifferenceAsLong(0L, 0L));
        assertEquals(1, MockCountingDurationField.difference_long);
    }

    //-----------------------------------------------------------------------
   @Test
    public void test_set_long_int() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(1000, field.set(0L, 0));
        assertEquals(1029, field.set(0L, 29));
    }

   @Test
    public void test_set_RP_int_intarray_int() {
        BaseDateTimeField field = new MockBaseDateTimeField();
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

   @Test
    public void test_set_long_String_Locale() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(1000, field.set(0L, "0", null));
        assertEquals(1029, field.set(0L, "29", Locale.ENGLISH));
    }

   @Test
    public void test_set_long_String() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(1000, field.set(0L, "0"));
        assertEquals(1029, field.set(0L, "29"));
    }

   @Test
    public void test_set_RP_int_intarray_String_Locale() {
        BaseDateTimeField field = new MockBaseDateTimeField();
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

   @Test
    public void test_convertText() {
        BaseDateTimeField field = new MockBaseDateTimeField();
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

   @Test
    public void test_isLeap_long() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(false, field.isLeap(0L));
    }

   @Test
    public void test_getLeapAmount_long() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(0, field.getLeapAmount(0L));
    }

   @Test
    public void test_getLeapDurationField() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(null, field.getLeapDurationField());
    }

    //-----------------------------------------------------------------------
   @Test
    public void test_getMinimumValue() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(0, field.getMinimumValue());
    }

   @Test
    public void test_getMinimumValue_long() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(0, field.getMinimumValue(0L));
    }

   @Test
    public void test_getMinimumValue_RP() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(0, field.getMinimumValue(new TimeOfDay()));
    }

   @Test
    public void test_getMinimumValue_RP_intarray() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(0, field.getMinimumValue(new TimeOfDay(), new int[4]));
    }

   @Test
    public void test_getMaximumValue() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(59, field.getMaximumValue());
    }

   @Test
    public void test_getMaximumValue_long() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(59, field.getMaximumValue(0L));
    }

   @Test
    public void test_getMaximumValue_RP() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(59, field.getMaximumValue(new TimeOfDay()));
    }

   @Test
    public void test_getMaximumValue_RP_intarray() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(59, field.getMaximumValue(new TimeOfDay(), new int[4]));
    }

    //-----------------------------------------------------------------------
   @Test
    public void test_getMaximumTextLength_Locale() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(2, field.getMaximumTextLength(Locale.ENGLISH));

        field = new MockBaseDateTimeField() {
            public int getMaximumValue() {
                return 5;
            }
        };
        assertEquals(1, field.getMaximumTextLength(Locale.ENGLISH));
        
        field = new MockBaseDateTimeField() {
            public int getMaximumValue() {
                return 555;
            }
        };
        assertEquals(3, field.getMaximumTextLength(Locale.ENGLISH));
        
        field = new MockBaseDateTimeField() {
            public int getMaximumValue() {
                return 5555;
            }
        };
        assertEquals(4, field.getMaximumTextLength(Locale.ENGLISH));
        
        field = new MockBaseDateTimeField() {
            public int getMaximumValue() {
                return -1;
            }
        };
        assertEquals(2, field.getMaximumTextLength(Locale.ENGLISH));
    }

   @Test
    public void test_getMaximumShortTextLength_Locale() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(2, field.getMaximumShortTextLength(Locale.ENGLISH));
    }

    //------------------------------------------------------------------------
   @Test
    public void test_roundFloor_long() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(0L, field.roundFloor(0L));
        assertEquals(0L, field.roundFloor(29L));
        assertEquals(0L, field.roundFloor(30L));
        assertEquals(0L, field.roundFloor(31L));
        assertEquals(60L, field.roundFloor(60L));
    }

   @Test
    public void test_roundCeiling_long() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(0L, field.roundCeiling(0L));
        assertEquals(60L, field.roundCeiling(29L));
        assertEquals(60L, field.roundCeiling(30L));
        assertEquals(60L, field.roundCeiling(31L));
        assertEquals(60L, field.roundCeiling(60L));
    }

   @Test
    public void test_roundHalfFloor_long() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(0L, field.roundHalfFloor(0L));
        assertEquals(0L, field.roundHalfFloor(29L));
        assertEquals(0L, field.roundHalfFloor(30L));
        assertEquals(60L, field.roundHalfFloor(31L));
        assertEquals(60L, field.roundHalfFloor(60L));
    }

   @Test
    public void test_roundHalfCeiling_long() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(0L, field.roundHalfCeiling(0L));
        assertEquals(0L, field.roundHalfCeiling(29L));
        assertEquals(60L, field.roundHalfCeiling(30L));
        assertEquals(60L, field.roundHalfCeiling(31L));
        assertEquals(60L, field.roundHalfCeiling(60L));
    }

   @Test
    public void test_roundHalfEven_long() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(0L, field.roundHalfEven(0L));
        assertEquals(0L, field.roundHalfEven(29L));
        assertEquals(0L, field.roundHalfEven(30L));
        assertEquals(60L, field.roundHalfEven(31L));
        assertEquals(60L, field.roundHalfEven(60L));
        assertEquals(60L, field.roundHalfEven(89L));
        assertEquals(120L, field.roundHalfEven(90L));
        assertEquals(120L, field.roundHalfEven(91L));
    }

   @Test
    public void test_remainder_long() {
        BaseDateTimeField field = new MockBaseDateTimeField();
        assertEquals(0L, field.remainder(0L));
        assertEquals(29L, field.remainder(29L));
        assertEquals(30L, field.remainder(30L));
        assertEquals(31L, field.remainder(31L));
        assertEquals(0L, field.remainder(60L));
    }

    //-----------------------------------------------------------------------
    static class MockBaseDateTimeField extends BaseDateTimeField {
        protected MockBaseDateTimeField() {
            super(DateTimeFieldType.secondOfMinute());
        }
        protected MockBaseDateTimeField(DateTimeFieldType type) {
            super( type );
        }
        public int get(long instant) {
            return (int) (instant / 60L);
        }
        public long set(long instant, int value) {
            return 1000 + value;
        }
        public DurationField getDurationField() {
            return new MockCountingDurationField(DurationFieldType.seconds());
        }
        public DurationField getRangeDurationField() {
            return new MockCountingDurationField(DurationFieldType.minutes());
        }
        public int getMinimumValue() {
            return 0;
        }
        public int getMaximumValue() {
            return 59;
        }
        public long roundFloor(long instant) {
            return (instant / 60L) * 60L;
        }
        public boolean isLenient() {
            return false;
        }
    }

    static class MockStandardBaseDateTimeField extends MockBaseDateTimeField {
        protected MockStandardBaseDateTimeField() {

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
            return false;
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

    static class MockPartial extends BasePartial {
        protected DateTimeField getField(int index, Chronology chrono) {
            if (index == 0) {
                return ISOChronology.getInstanceUTC().minuteOfHour();
            }
            if (index == 1) {
                return ISOChronology.getInstanceUTC().millisOfSecond();
            }
            return null;
        }
        public int size() {
            return 2;
        }
        
    }
}
