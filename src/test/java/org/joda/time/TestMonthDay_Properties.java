/*
 *  Copyright 2001-2011 Stephen Colebourne
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

import java.util.Locale;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.LenientChronology;
import org.joda.time.chrono.StrictChronology;

/**
 * This class is a Junit unit test for MonthDay. Based on {@link TestYearMonth_Propeties} 
 */
public class TestMonthDay_Properties extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final Chronology COPTIC_PARIS = CopticChronology.getInstance(PARIS);

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
    private Locale locale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestMonthDay_Properties.class);
    }

    public TestMonthDay_Properties(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        zone = DateTimeZone.getDefault();
        locale = Locale.getDefault();
        Locale.setDefault(Locale.UK);
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    @Override
    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(zone);
        zone = null;
        Locale.setDefault(locale);
        locale = null;
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetMonthOfYear() {
        MonthDay test = new MonthDay(9, 6);
        assertSame(test.getChronology().monthOfYear(), test.monthOfYear().getField());
        assertEquals("monthOfYear", test.monthOfYear().getName());
        assertEquals("Property[monthOfYear]", test.monthOfYear().toString());
        assertSame(test, test.monthOfYear().getReadablePartial());
        assertSame(test, test.monthOfYear().getMonthDay());
        assertEquals(9, test.monthOfYear().get());
        assertEquals("9", test.monthOfYear().getAsString());
        assertEquals("September", test.monthOfYear().getAsText());
        assertEquals("septembre", test.monthOfYear().getAsText(Locale.FRENCH));
        assertEquals("Sep", test.monthOfYear().getAsShortText());
        assertEquals("sept.", test.monthOfYear().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().months(), test.monthOfYear().getDurationField());
        // assertEquals(test.getChronology().days(), test.dayOfMonth().getRangeDurationField());
        assertEquals(9, test.monthOfYear().getMaximumTextLength(null));
        assertEquals(3, test.monthOfYear().getMaximumShortTextLength(null));
    }

    public void testPropertyGetMaxMinValuesMonthOfYear() {
        MonthDay test = new MonthDay(10, 6);
        assertEquals(1, test.monthOfYear().getMinimumValue());
        assertEquals(1, test.monthOfYear().getMinimumValueOverall());
        assertEquals(12, test.monthOfYear().getMaximumValue());
        assertEquals(12, test.monthOfYear().getMaximumValueOverall());
    }

    public void testPropertyAddMonthOfYear() {
        MonthDay test = new MonthDay(3, 6);
        MonthDay copy = test.monthOfYear().addToCopy(9);
        check(test, 3, 6);
        check(copy, 12, 6);
        
        copy = test.monthOfYear().addToCopy(0);
        check(copy, 3, 6);

        check(test, 3, 6);
        
        copy = test.monthOfYear().addToCopy(-3);
        check(copy, 12, 6);
        check(test, 3, 6);
    }

    public void testPropertyAddWrapFieldMonthOfYear() {
        MonthDay test = new MonthDay(5, 6);
        MonthDay copy = test.monthOfYear().addWrapFieldToCopy(2);
        check(test, 5, 6);
        check(copy, 7, 6);
        
        copy = test.monthOfYear().addWrapFieldToCopy(2);
        check(copy, 7, 6);
        
        copy = test.monthOfYear().addWrapFieldToCopy(292278993 - 4 + 1);
        check(copy, 11, 6);
        
        copy = test.monthOfYear().addWrapFieldToCopy(-292275054 - 4 - 1);
        check(copy, 6, 6);
    }

    public void testPropertySetMonthOfYear() {
        MonthDay test = new MonthDay(10, 6);
        MonthDay copy = test.monthOfYear().setCopy(12);
        check(test, 10, 6);
        check(copy, 12, 6);
    }

    public void testPropertySetTextMonthOfYear() {
        MonthDay test = new MonthDay(10, 6);
        MonthDay copy = test.monthOfYear().setCopy("12");
        check(test, 10, 6);
        check(copy, 12, 6);
    }

    public void testPropertyCompareToMonthOfYear() {
        MonthDay test1 = new MonthDay(TEST_TIME1);
        MonthDay test2 = new MonthDay(TEST_TIME2);
        assertEquals(true, test1.monthOfYear().compareTo(test2) < 0);
        assertEquals(true, test2.monthOfYear().compareTo(test1) > 0);
        assertEquals(true, test1.monthOfYear().compareTo(test1) == 0);
        try {
            test1.monthOfYear().compareTo((ReadablePartial) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        DateTime dt1 = new DateTime(TEST_TIME1);
        DateTime dt2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.monthOfYear().compareTo(dt2) < 0);
        assertEquals(true, test2.monthOfYear().compareTo(dt1) > 0);
        assertEquals(true, test1.monthOfYear().compareTo(dt1) == 0);
        try {
            test1.monthOfYear().compareTo((ReadableInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetDayOfMonth() {
        MonthDay test = new MonthDay(4, 6);
        assertSame(test.getChronology().dayOfMonth(), test.dayOfMonth().getField());
        assertEquals("dayOfMonth", test.dayOfMonth().getName());
        assertEquals("Property[dayOfMonth]", test.dayOfMonth().toString());
        assertSame(test, test.dayOfMonth().getReadablePartial());
        assertSame(test, test.dayOfMonth().getMonthDay());
        assertEquals(6, test.dayOfMonth().get());
        assertEquals("6", test.dayOfMonth().getAsString());
        assertEquals("6", test.dayOfMonth().getAsText());
        assertEquals("6", test.dayOfMonth().getAsText(Locale.FRENCH));
        assertEquals("6", test.dayOfMonth().getAsShortText());
        assertEquals("6", test.dayOfMonth().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().days(), test.dayOfMonth().getDurationField());
        assertEquals(test.getChronology().months(), test.dayOfMonth().getRangeDurationField());
        assertEquals(2, test.dayOfMonth().getMaximumTextLength(null));
        assertEquals(2, test.dayOfMonth().getMaximumShortTextLength(null));
        test = new MonthDay(4, 7);
        assertEquals("7", test.dayOfMonth().getAsText(Locale.FRENCH));
        assertEquals("7", test.dayOfMonth().getAsShortText(Locale.FRENCH));
    }

    public void testPropertyGetMaxMinValuesDayOfMonth() {
        MonthDay test = new MonthDay(4, 6);
        assertEquals(1, test.dayOfMonth().getMinimumValue());
        assertEquals(1, test.dayOfMonth().getMinimumValueOverall());
        assertEquals(30, test.dayOfMonth().getMaximumValue());
        assertEquals(31, test.dayOfMonth().getMaximumValueOverall());
    }

    public void testPropertyAddDayOfMonth() {
        MonthDay test = new MonthDay(4, 6);
        MonthDay copy = test.dayOfMonth().addToCopy(6);
        check(test, 4, 6);
        check(copy, 4, 12);
        
        copy = test.dayOfMonth().addToCopy(7);
        check(copy, 4, 13);
        
        copy = test.dayOfMonth().addToCopy(-5);
        check(copy, 4, 1);
        
        copy = test.dayOfMonth().addToCopy(-6);
        check(copy, 3, 31);
    }

    public void testPropertyAddWrapFieldDayOfMonth() {
        MonthDay test = new MonthDay(4, 6);
        MonthDay copy = test.dayOfMonth().addWrapFieldToCopy(4);
        check(test, 4, 6);
        check(copy, 4, 10);
        
        copy = test.dayOfMonth().addWrapFieldToCopy(8);
        check(copy, 4, 14);
        
        copy = test.dayOfMonth().addWrapFieldToCopy(-8);
        check(copy, 4, 28);
    }

    public void testPropertySetDayOfMonth() {
        MonthDay test = new MonthDay(4, 6);
        MonthDay copy = test.dayOfMonth().setCopy(12);
        check(test, 4, 6);
        check(copy, 4, 12);
        
        try {
            test.dayOfMonth().setCopy(33);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.dayOfMonth().setCopy(0);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testPropertySetTextDayOfMonth() {
        MonthDay test = new MonthDay(4, 6);
        MonthDay copy = test.dayOfMonth().setCopy("12");
        check(test, 4, 6);
        check(copy, 4, 12);
        
        copy = test.dayOfMonth().setCopy("2");
        check(test, 4, 6);
        check(copy, 4, 2);
        
        copy = test.dayOfMonth().setCopy("4");
        check(test, 4, 6);
        check(copy, 4, 4);
    }

    public void testPropertyCompareToDayOfMonth() {
        MonthDay test1 = new MonthDay(TEST_TIME1);
        MonthDay test2 = new MonthDay(TEST_TIME2);
        assertEquals(true, test1.dayOfMonth().compareTo(test2) < 0);
        assertEquals(true, test2.dayOfMonth().compareTo(test1) > 0);
        assertEquals(true, test1.dayOfMonth().compareTo(test1) == 0);
        try {
            test1.dayOfMonth().compareTo((ReadablePartial) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        DateTime dt1 = new DateTime(TEST_TIME1);
        DateTime dt2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.dayOfMonth().compareTo(dt2) < 0);
        assertEquals(true, test2.dayOfMonth().compareTo(dt1) > 0);
        assertEquals(true, test1.dayOfMonth().compareTo(dt1) == 0);
        try {
            test1.dayOfMonth().compareTo((ReadableInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testPropertyEquals() {
        MonthDay test1 = new MonthDay(11, 11);
        MonthDay test2 = new MonthDay(11, 12);
        MonthDay test3 = new MonthDay(11, 11, CopticChronology.getInstanceUTC());
        assertEquals(true, test1.dayOfMonth().equals(test1.dayOfMonth()));
        assertEquals(false, test1.dayOfMonth().equals(test1.monthOfYear()));
        assertEquals(false, test1.dayOfMonth().equals(test2.dayOfMonth()));
        assertEquals(false, test1.dayOfMonth().equals(test2.monthOfYear()));
        
        assertEquals(false, test1.monthOfYear().equals(test1.dayOfMonth()));
        assertEquals(true, test1.monthOfYear().equals(test1.monthOfYear()));
        assertEquals(false, test1.monthOfYear().equals(test2.dayOfMonth()));
        assertEquals(true, test1.monthOfYear().equals(test2.monthOfYear()));
        
        assertEquals(false, test1.dayOfMonth().equals(null));
        assertEquals(false, test1.dayOfMonth().equals("any"));
        
        // chrono
        assertEquals(false, test1.dayOfMonth().equals(test3.dayOfMonth()));
    }

    public void testPropertyHashCode() {
        MonthDay test1 = new MonthDay(5, 11);
        MonthDay test2 = new MonthDay(5, 12);
        assertEquals(true, test1.dayOfMonth().hashCode() == test1.dayOfMonth().hashCode());
        assertEquals(false, test1.dayOfMonth().hashCode() == test2.dayOfMonth().hashCode());
        assertEquals(true, test1.monthOfYear().hashCode() == test1.monthOfYear().hashCode());
        assertEquals(true, test1.monthOfYear().hashCode() == test2.monthOfYear().hashCode());
    }

    public void testPropertyEqualsHashCodeLenient() {
        MonthDay test1 = new MonthDay(5, 6, LenientChronology.getInstance(COPTIC_PARIS));
        MonthDay test2 = new MonthDay(5, 6, LenientChronology.getInstance(COPTIC_PARIS));
        assertEquals(true, test1.dayOfMonth().equals(test2.dayOfMonth()));
        assertEquals(true, test2.dayOfMonth().equals(test1.dayOfMonth()));
        assertEquals(true, test1.dayOfMonth().equals(test1.dayOfMonth()));
        assertEquals(true, test2.dayOfMonth().equals(test2.dayOfMonth()));
        assertEquals(true, test1.dayOfMonth().hashCode() == test2.dayOfMonth().hashCode());
        assertEquals(true, test1.dayOfMonth().hashCode() == test1.dayOfMonth().hashCode());
        assertEquals(true, test2.dayOfMonth().hashCode() == test2.dayOfMonth().hashCode());
    }

    public void testPropertyEqualsHashCodeStrict() {
        MonthDay test1 = new MonthDay(5, 6, StrictChronology.getInstance(COPTIC_PARIS));
        MonthDay test2 = new MonthDay(5, 6, StrictChronology.getInstance(COPTIC_PARIS));
        assertEquals(true, test1.dayOfMonth().equals(test2.dayOfMonth()));
        assertEquals(true, test2.dayOfMonth().equals(test1.dayOfMonth()));
        assertEquals(true, test1.dayOfMonth().equals(test1.dayOfMonth()));
        assertEquals(true, test2.dayOfMonth().equals(test2.dayOfMonth()));
        assertEquals(true, test1.dayOfMonth().hashCode() == test2.dayOfMonth().hashCode());
        assertEquals(true, test1.dayOfMonth().hashCode() == test1.dayOfMonth().hashCode());
        assertEquals(true, test2.dayOfMonth().hashCode() == test2.dayOfMonth().hashCode());
    }

    //-----------------------------------------------------------------------
    private void check(MonthDay test, int monthOfYear, int dayOfMonth) {
        assertEquals(monthOfYear, test.getMonthOfYear());
        assertEquals(dayOfMonth, test.getDayOfMonth());
    }
}
