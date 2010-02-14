/*
 *  Copyright 2001-2010 Stephen Colebourne
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
import java.util.Arrays;
import java.util.Locale;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * This class is a Junit unit test for MonthDay. Based on {@link TestYearMonth_Basics} 
 */
public class TestMonthDay_Basics extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
    private static final Chronology COPTIC_PARIS = CopticChronology.getInstance(PARIS);
//    private static final Chronology COPTIC_LONDON = CopticChronology.getInstance(LONDON);
    private static final Chronology COPTIC_TOKYO = CopticChronology.getInstance(TOKYO);
    private static final Chronology COPTIC_UTC = CopticChronology.getInstanceUTC();
//    private static final Chronology ISO_PARIS = ISOChronology.getInstance(PARIS);
//    private static final Chronology ISO_LONDON = ISOChronology.getInstance(LONDON);
//    private static final Chronology ISO_TOKYO = ISOChronology.getInstance(TOKYO);
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();
//    private static final Chronology BUDDHIST_PARIS = BuddhistChronology.getInstance(PARIS);
//    private static final Chronology BUDDHIST_LONDON = BuddhistChronology.getInstance(LONDON);
    private static final Chronology BUDDHIST_TOKYO = BuddhistChronology.getInstance(TOKYO);
    private static final Chronology BUDDHIST_UTC = BuddhistChronology.getInstanceUTC();
    
    private long TEST_TIME_NOW =
            (31L + 28L + 31L + 30L + 31L + 9L -1L) * DateTimeConstants.MILLIS_PER_DAY;

    private DateTimeZone zone = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestMonthDay_Basics.class);
    }

    public TestMonthDay_Basics(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        zone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(LONDON);
    }

    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(zone);
        zone = null;
    }

    //-----------------------------------------------------------------------
    public void testGet() {
        MonthDay test = new MonthDay();
        assertEquals(6, test.get(DateTimeFieldType.monthOfYear()));
        assertEquals(9, test.get(DateTimeFieldType.dayOfMonth()));
        try {
            test.get(null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.get(DateTimeFieldType.year());
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testSize() {
        MonthDay test = new MonthDay();
        assertEquals(2, test.size());
    }

    public void testGetFieldType() {
        MonthDay test = new MonthDay(COPTIC_PARIS);
        assertSame(DateTimeFieldType.monthOfYear(), test.getFieldType(0));
        assertSame(DateTimeFieldType.dayOfMonth(), test.getFieldType(1));

        try {
            test.getFieldType(-1);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            test.getFieldType(2);
        } catch (IndexOutOfBoundsException ex) {}
    }

    public void testGetFieldTypes() {
        MonthDay test = new MonthDay(COPTIC_PARIS);
        DateTimeFieldType[] fields = test.getFieldTypes();
        assertEquals(2, fields.length);
        assertSame(DateTimeFieldType.monthOfYear(), fields[0]);
        assertSame(DateTimeFieldType.dayOfMonth(), fields[1]);
        assertNotSame(test.getFieldTypes(), test.getFieldTypes());
    }

    public void testGetField() {
        MonthDay test = new MonthDay(COPTIC_PARIS);
        assertSame(COPTIC_UTC.monthOfYear(), test.getField(0));
        assertSame(COPTIC_UTC.dayOfMonth(), test.getField(1));
        try {
            test.getField(-1);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            test.getField(2);
        } catch (IndexOutOfBoundsException ex) {}
    }

    public void testGetFields() {
        MonthDay test = new MonthDay(COPTIC_PARIS);
        DateTimeField[] fields = test.getFields();
        assertEquals(2, fields.length);
        assertSame(COPTIC_UTC.monthOfYear(), fields[0]);
        assertSame(COPTIC_UTC.dayOfMonth(), fields[1]);
        assertNotSame(test.getFields(), test.getFields());
    }

    public void testGetValue() {
        MonthDay test = new MonthDay();
        assertEquals(6, test.getValue(0));
        assertEquals(9, test.getValue(1));
        try {
            test.getValue(-1);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            test.getValue(2);
        } catch (IndexOutOfBoundsException ex) {}
    }

    public void testGetValues() {
        MonthDay test = new MonthDay();
        int[] values = test.getValues();
        assertEquals(2, values.length);
        assertEquals(6, values[0]);
        assertEquals(9, values[1]);
        assertNotSame(test.getValues(), test.getValues());
    }

    public void testIsSupported() {
        MonthDay test = new MonthDay(COPTIC_PARIS);
        assertEquals(false, test.isSupported(DateTimeFieldType.year()));
        assertEquals(true, test.isSupported(DateTimeFieldType.monthOfYear()));
        assertEquals(true, test.isSupported(DateTimeFieldType.dayOfMonth()));
        assertEquals(false, test.isSupported(DateTimeFieldType.hourOfDay()));
    }

    public void testEqualsHashCode() {
        MonthDay test1 = new MonthDay(10, 6, COPTIC_PARIS);
        MonthDay test2 = new MonthDay(10, 6, COPTIC_PARIS);
        assertEquals(true, test1.equals(test2));
        assertEquals(true, test2.equals(test1));
        assertEquals(true, test1.equals(test1));
        assertEquals(true, test2.equals(test2));
        assertEquals(true, test1.hashCode() == test2.hashCode());
        assertEquals(true, test1.hashCode() == test1.hashCode());
        assertEquals(true, test2.hashCode() == test2.hashCode());
        
        MonthDay test3 = new MonthDay(10, 6);
        assertEquals(false, test1.equals(test3));
        assertEquals(false, test2.equals(test3));
        assertEquals(false, test3.equals(test1));
        assertEquals(false, test3.equals(test2));
        assertEquals(false, test1.hashCode() == test3.hashCode());
        assertEquals(false, test2.hashCode() == test3.hashCode());
        
        assertEquals(false, test1.equals("Hello"));
        assertEquals(true, test1.equals(new MockMD()));
        assertEquals(false, test1.equals(MockPartial.EMPTY_INSTANCE));
    }
    
    class MockMD extends MockPartial {
        
        @Override
        public Chronology getChronology() {
            return COPTIC_UTC;
        }
        
        @Override
        public DateTimeField[] getFields() {
            return new DateTimeField[] {
                COPTIC_UTC.monthOfYear(),
                COPTIC_UTC.dayOfMonth()
            };
        }
        
        @Override
        public int[] getValues() {
            return new int[] {10, 6};
        }
    }

    //-----------------------------------------------------------------------
    public void testCompareTo() {
        MonthDay test1 = new MonthDay(6, 6);
        MonthDay test1a = new MonthDay(6, 6);
        assertEquals(0, test1.compareTo(test1a));
        assertEquals(0, test1a.compareTo(test1));
        assertEquals(0, test1.compareTo(test1));
        assertEquals(0, test1a.compareTo(test1a));
        
        MonthDay test2 = new MonthDay(6, 7);
        assertEquals(-1, test1.compareTo(test2));
        assertEquals(+1, test2.compareTo(test1));
        
        MonthDay test3 = new MonthDay(6, 7, GregorianChronology.getInstanceUTC());
        assertEquals(-1, test1.compareTo(test3));
        assertEquals(+1, test3.compareTo(test1));
        assertEquals(0, test3.compareTo(test2));
        
        DateTimeFieldType[] types = new DateTimeFieldType[] {
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth()
        };
        int[] values = new int[] {6, 6};
        Partial p = new Partial(types, values);
        assertEquals(0, test1.compareTo(p));
        try {
            test1.compareTo(null);
            fail();
        } catch (NullPointerException ex) {}
        try {
            test1.compareTo(new LocalTime());
            fail();
        } catch (ClassCastException ex) {}
        Partial partial = new Partial()
            .with(DateTimeFieldType.centuryOfEra(), 1)
            .with(DateTimeFieldType.halfdayOfDay(), 0)
            .with(DateTimeFieldType.dayOfMonth(), 9);
        try {
            new MonthDay(10, 6).compareTo(partial);
            fail();
        } catch (ClassCastException ex) {}
    }
    
    //-----------------------------------------------------------------------
    public void testIsEqual_MD() {
        MonthDay test1 = new MonthDay(6, 6);
        MonthDay test1a = new MonthDay(6, 6);
        assertEquals(true, test1.isEqual(test1a));
        assertEquals(true, test1a.isEqual(test1));
        assertEquals(true, test1.isEqual(test1));
        assertEquals(true, test1a.isEqual(test1a));
        
        MonthDay test2 = new MonthDay(6, 7);
        assertEquals(false, test1.isEqual(test2));
        assertEquals(false, test2.isEqual(test1));
        
        MonthDay test3 = new MonthDay(6, 7, GregorianChronology.getInstanceUTC());
        assertEquals(false, test1.isEqual(test3));
        assertEquals(false, test3.isEqual(test1));
        assertEquals(true, test3.isEqual(test2));
        
        try {
            new MonthDay(6, 7).isEqual(null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    //-----------------------------------------------------------------------
    public void testIsBefore_MD() {
        MonthDay test1 = new MonthDay(6, 6);
        MonthDay test1a = new MonthDay(6, 6);
        assertEquals(false, test1.isBefore(test1a));
        assertEquals(false, test1a.isBefore(test1));
        assertEquals(false, test1.isBefore(test1));
        assertEquals(false, test1a.isBefore(test1a));
        
        MonthDay test2 = new MonthDay(6, 7);
        assertEquals(true, test1.isBefore(test2));
        assertEquals(false, test2.isBefore(test1));
        
        MonthDay test3 = new MonthDay(6, 7, GregorianChronology.getInstanceUTC());
        assertEquals(true, test1.isBefore(test3));
        assertEquals(false, test3.isBefore(test1));
        assertEquals(false, test3.isBefore(test2));
        
        try {
            new MonthDay(6, 7).isBefore(null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    //-----------------------------------------------------------------------
    public void testIsAfter_MD() {
        MonthDay test1 = new MonthDay(6, 6);
        MonthDay test1a = new MonthDay(6, 6);
        assertEquals(false, test1.isAfter(test1a));
        assertEquals(false, test1a.isAfter(test1));
        assertEquals(false, test1.isAfter(test1));
        assertEquals(false, test1a.isAfter(test1a));
        
        MonthDay test2 = new MonthDay(6, 7);
        assertEquals(false, test1.isAfter(test2));
        assertEquals(true, test2.isAfter(test1));
        
        MonthDay test3 = new MonthDay(6, 7, GregorianChronology.getInstanceUTC());
        assertEquals(false, test1.isAfter(test3));
        assertEquals(true, test3.isAfter(test1));
        assertEquals(false, test3.isAfter(test2));
        
        try {
            new MonthDay(6, 7).isAfter(null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    //-----------------------------------------------------------------------
    public void testWithChronologyRetainFields_Chrono() {
        MonthDay base = new MonthDay(6, 6, COPTIC_PARIS);
        MonthDay test = base.withChronologyRetainFields(BUDDHIST_TOKYO);
        check(base, 6, 6);
        assertEquals(COPTIC_UTC, base.getChronology());
        check(test, 6, 6);
        assertEquals(BUDDHIST_UTC, test.getChronology());
    }

    public void testWithChronologyRetainFields_sameChrono() {
        MonthDay base = new MonthDay(6, 6, COPTIC_PARIS);
        MonthDay test = base.withChronologyRetainFields(COPTIC_TOKYO);
        assertSame(base, test);
    }

    public void testWithChronologyRetainFields_nullChrono() {
        MonthDay base = new MonthDay(6, 6, COPTIC_PARIS);
        MonthDay test = base.withChronologyRetainFields(null);
        check(base, 6, 6);
        assertEquals(COPTIC_UTC, base.getChronology());
        check(test, 6, 6);
        assertEquals(ISO_UTC, test.getChronology());
    }

    //-----------------------------------------------------------------------
    public void testWithField() {
        MonthDay test = new MonthDay(9, 6);
        MonthDay result = test.withField(DateTimeFieldType.monthOfYear(), 10);
        
        assertEquals(new MonthDay(9, 6), test);
        assertEquals(new MonthDay(10, 6), result);
    }

    public void testWithField_nullField() {
        MonthDay test = new MonthDay(9, 6);
        try {
            test.withField(null, 6);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testWithField_unknownField() {
        MonthDay test = new MonthDay(9, 6);
        try {
            test.withField(DateTimeFieldType.hourOfDay(), 6);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testWithField_same() {
        MonthDay test = new MonthDay(9, 6);
        MonthDay result = test.withField(DateTimeFieldType.monthOfYear(), 9);
        assertEquals(new MonthDay(9, 6), test);
        assertSame(test, result);
    }

    //-----------------------------------------------------------------------
    public void testWithFieldAdded() {
        MonthDay test = new MonthDay(9, 6);
        MonthDay result = test.withFieldAdded(DurationFieldType.months(), 1);
        
        assertEquals(new MonthDay(9, 6), test);
        assertEquals(new MonthDay(10, 6), result);
    }

    public void testWithFieldAdded_nullField_zero() {
        MonthDay test = new MonthDay(9, 6);
        try {
            test.withFieldAdded(null, 0);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testWithFieldAdded_nullField_nonZero() {
        MonthDay test = new MonthDay(9, 6);
        try {
            test.withFieldAdded(null, 6);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testWithFieldAdded_zero() {
        MonthDay test = new MonthDay(9, 6);
        MonthDay result = test.withFieldAdded(DurationFieldType.months(), 0);
        assertSame(test, result);
    }

    public void testWithFieldAdded_unknownField() {
        MonthDay test = new MonthDay(9, 6);
        try {
            test.withFieldAdded(DurationFieldType.hours(), 6);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testPlus_RP() {
        MonthDay test = new MonthDay(6, 5, BuddhistChronology.getInstance());
        MonthDay result = test.plus(new Period(1, 2, 3, 4, 5, 6, 7, 8));
        MonthDay expected = new MonthDay(8, 9, BuddhistChronology.getInstance());
        assertEquals(expected, result);
        
        result = test.plus((ReadablePeriod) null);
        assertSame(test, result);
    }

    public void testPlusMonths_int() {
        MonthDay test = new MonthDay(6, 5, BuddhistChronology.getInstance());
        MonthDay result = test.plusMonths(1);
        MonthDay expected = new MonthDay(7, 5, BuddhistChronology.getInstance());
        assertEquals(expected, result);
    }

    public void testPlusMonths_int_same() {
        MonthDay test = new MonthDay(6, 5, ISO_UTC);
        MonthDay result = test.plusMonths(0);
        assertSame(test, result);
    }

    public void testPlusMonths_int_wrap() {
        MonthDay test = new MonthDay(6, 5, ISO_UTC);
        MonthDay result = test.plusMonths(10);
        MonthDay expected = new MonthDay(4, 5, ISO_UTC);
        assertEquals(expected, result);
    }

    public void testPlusMonths_int_adjust() {
        MonthDay test = new MonthDay(7, 31, ISO_UTC);
        MonthDay result = test.plusMonths(2);
        MonthDay expected = new MonthDay(9, 30, ISO_UTC);
        assertEquals(expected, result);
    }

    public void testPlusDays_int() {
        MonthDay test = new MonthDay(5, 10, BuddhistChronology.getInstance());
        MonthDay result = test.plusDays(1);
        MonthDay expected = new MonthDay(5, 11, BuddhistChronology.getInstance());
        assertEquals(expected, result);
    }

    public void testPlusDays_same() {
        MonthDay test = new MonthDay(5, 10, BuddhistChronology.getInstance());
        MonthDay result = test.plusDays(0);
        assertSame(test, result);
    }
    
    //-----------------------------------------------------------------------
    public void testMinus_RP() {
        MonthDay test = new MonthDay(6, 5, BuddhistChronology.getInstance());
        MonthDay result = test.minus(new Period(1, 1, 1, 1, 1, 1, 1, 1));
        MonthDay expected = new MonthDay(5, 4, BuddhistChronology.getInstance());
        assertEquals(expected, result);
        
        result = test.minus((ReadablePeriod) null);
        assertSame(test, result);
    }

    public void testMinusMonths_int() {
        MonthDay test = new MonthDay(6, 5, BuddhistChronology.getInstance());
        MonthDay result = test.minusMonths(1);
        MonthDay expected = new MonthDay(5, 5, BuddhistChronology.getInstance());
        assertEquals(expected, result);
    }

    public void testMinusMonths_int_same() {
        MonthDay test = new MonthDay(6, 5, ISO_UTC);
        MonthDay result = test.minusMonths(0);
        assertSame(test, result);
    }

    public void testMinusMonths_int_wrap() {
        MonthDay test = new MonthDay(6, 5, ISO_UTC);
        MonthDay result = test.minusMonths(10);
        MonthDay expected = new MonthDay(8, 5, ISO_UTC);
        assertEquals(expected, result);
    }

    public void testMinusMonths_int_adjust() {
        MonthDay test = new MonthDay(7, 31, ISO_UTC);
        MonthDay result = test.minusMonths(3);
        MonthDay expected = new MonthDay(4, 30, ISO_UTC);
        assertEquals(expected, result);
    }

    public void testMinusDays_int() {
        MonthDay test = new MonthDay(5, 11, BuddhistChronology.getInstance());
        MonthDay result = test.minusDays(1);
        MonthDay expected = new MonthDay(5, 10, BuddhistChronology.getInstance());
        assertEquals(expected, result);
    }

    public void testMinusDays_same() {
        MonthDay test = new MonthDay(5, 11, BuddhistChronology.getInstance());
        MonthDay result = test.minusDays(0);
        assertSame(test, result);
    }
    
    //-----------------------------------------------------------------------
    public void testToLocalDate() {
        MonthDay base = new MonthDay(6, 6, COPTIC_UTC);
        LocalDate test = base.toLocalDate(2009);
        assertEquals(new LocalDate(2009, 6, 6, COPTIC_UTC), test);
        try {
            base.toLocalDate(0);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testToDateTime_RI() {
        MonthDay base = new MonthDay(6, 6, COPTIC_PARIS);
        DateTime dt = new DateTime(2002, 1, 3, 4, 5, 6, 7);
        
        DateTime test = base.toDateTime(dt);
        check(base, 6, 6);
        DateTime expected = dt;
        expected = expected.monthOfYear().setCopy(6);
        expected = expected.dayOfMonth().setCopy(6);
        assertEquals(expected, test);
    }

    public void testToDateTime_nullRI() {
        MonthDay base = new MonthDay(6, 6);
        DateTime dt = new DateTime(2002, 1, 3, 4, 5, 6, 7);
        DateTimeUtils.setCurrentMillisFixed(dt.getMillis());
        
        DateTime test = base.toDateTime((ReadableInstant) null);
        check(base, 6, 6);
        DateTime expected = dt;
        expected = expected.monthOfYear().setCopy(6);
        expected = expected.dayOfMonth().setCopy(6);
        assertEquals(expected, test);
    }

    //-----------------------------------------------------------------------
    public void testWithers() {
        MonthDay test = new MonthDay(10, 6);
        check(test.withMonthOfYear(5), 5, 6);
        check(test.withDayOfMonth(2), 10, 2);
        try {
            test.withMonthOfYear(0);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.withMonthOfYear(13);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testProperty() {
        MonthDay test = new MonthDay(6, 6);
        assertEquals(test.monthOfYear(), test.property(DateTimeFieldType.monthOfYear()));
        assertEquals(test.dayOfMonth(), test.property(DateTimeFieldType.dayOfMonth()));
        try {
            test.property(DateTimeFieldType.millisOfDay());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.property(null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testSerialization() throws Exception {
        MonthDay test = new MonthDay(5, 6, COPTIC_PARIS);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(test);
        byte[] bytes = baos.toByteArray();
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        MonthDay result = (MonthDay) ois.readObject();
        ois.close();
        
        assertEquals(test, result);
        assertTrue(Arrays.equals(test.getValues(), result.getValues()));
        assertTrue(Arrays.equals(test.getFields(), result.getFields()));
        assertEquals(test.getChronology(), result.getChronology());
    }

    //-----------------------------------------------------------------------
    public void testToString() {
        MonthDay test = new MonthDay(5, 6);
        assertEquals("--05-06", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testToString_String() {
        MonthDay test = new MonthDay(5, 6);
        assertEquals("05 \ufffd\ufffd", test.toString("MM HH"));
        assertEquals("--05-06", test.toString((String) null));
    }

    //-----------------------------------------------------------------------
    public void testToString_String_Locale() {
        MonthDay test = new MonthDay(5, 6);
        assertEquals("\ufffd 6/5", test.toString("EEE d/M", Locale.ENGLISH));
        assertEquals("\ufffd 6/5", test.toString("EEE d/M", Locale.FRENCH));
        assertEquals("--05-06", test.toString(null, Locale.ENGLISH));
        assertEquals("\ufffd 6/5", test.toString("EEE d/M", null));
        assertEquals("--05-06", test.toString(null, null));
    }

    //-----------------------------------------------------------------------
    public void testToString_DTFormatter() {
        MonthDay test = new MonthDay(5, 6);
        assertEquals("05 \ufffd\ufffd", test.toString(DateTimeFormat.forPattern("MM HH")));
        assertEquals("--05-06", test.toString((DateTimeFormatter) null));
    }

    //-----------------------------------------------------------------------
    private void check(MonthDay test, int month, int day) {
        assertEquals(month, test.getMonthOfYear());
        assertEquals(day, test.getDayOfMonth());
    }
}
