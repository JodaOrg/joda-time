/*
 *  Copyright 2001-2006 Stephen Colebourne
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

/**
 * This class is a Junit unit test for TimeOfDay.
 *
 * @author Stephen Colebourne
 */
public class TestLocalTime_Properties extends TestCase {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    private long TEST_TIME_NOW =
            10L * DateTimeConstants.MILLIS_PER_HOUR
            + 20L * DateTimeConstants.MILLIS_PER_MINUTE
            + 30L * DateTimeConstants.MILLIS_PER_SECOND
            + 40L;

    private long TEST_TIME1 =
        1L * DateTimeConstants.MILLIS_PER_HOUR
        + 2L * DateTimeConstants.MILLIS_PER_MINUTE
        + 3L * DateTimeConstants.MILLIS_PER_SECOND
        + 4L;

    private long TEST_TIME2 =
        1L * DateTimeConstants.MILLIS_PER_DAY
        + 5L * DateTimeConstants.MILLIS_PER_HOUR
        + 6L * DateTimeConstants.MILLIS_PER_MINUTE
        + 7L * DateTimeConstants.MILLIS_PER_SECOND
        + 8L;

    private DateTimeZone zone = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestLocalTime_Properties.class);
    }

    public TestLocalTime_Properties(String name) {
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
    public void testPropertyGetHour() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        assertSame(test.getChronology().hourOfDay(), test.hourOfDay().getField());
        assertEquals("hourOfDay", test.hourOfDay().getName());
        assertEquals("Property[hourOfDay]", test.hourOfDay().toString());
        assertSame(test, test.hourOfDay().getLocalTime());
        assertEquals(10, test.hourOfDay().get());
        assertEquals("10", test.hourOfDay().getAsString());
        assertEquals("10", test.hourOfDay().getAsText());
        assertEquals("10", test.hourOfDay().getAsText(Locale.FRENCH));
        assertEquals("10", test.hourOfDay().getAsShortText());
        assertEquals("10", test.hourOfDay().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().hours(), test.hourOfDay().getDurationField());
        assertEquals(test.getChronology().days(), test.hourOfDay().getRangeDurationField());
        assertEquals(2, test.hourOfDay().getMaximumTextLength(null));
        assertEquals(2, test.hourOfDay().getMaximumShortTextLength(null));
    }

    public void testPropertyRoundHour() {
        LocalTime test = new LocalTime(10, 20);
        check(test.hourOfDay().withRoundedCeiling(), 11, 0, 0, 0);
        check(test.hourOfDay().withRoundedFloor(), 10, 0, 0, 0);
        check(test.hourOfDay().withRoundedHalfCeiling(), 10, 0, 0, 0);
        check(test.hourOfDay().withRoundedHalfFloor(), 10, 0, 0, 0);
        check(test.hourOfDay().withRoundedHalfEven(), 10, 0, 0, 0);
        
        test = new LocalTime(10, 40);
        check(test.hourOfDay().withRoundedCeiling(), 11, 0, 0, 0);
        check(test.hourOfDay().withRoundedFloor(), 10, 0, 0, 0);
        check(test.hourOfDay().withRoundedHalfCeiling(), 11, 0, 0, 0);
        check(test.hourOfDay().withRoundedHalfFloor(), 11, 0, 0, 0);
        check(test.hourOfDay().withRoundedHalfEven(), 11, 0, 0, 0);
        
        test = new LocalTime(10, 30);
        check(test.hourOfDay().withRoundedCeiling(), 11, 0, 0, 0);
        check(test.hourOfDay().withRoundedFloor(), 10, 0, 0, 0);
        check(test.hourOfDay().withRoundedHalfCeiling(), 11, 0, 0, 0);
        check(test.hourOfDay().withRoundedHalfFloor(), 10, 0, 0, 0);
        check(test.hourOfDay().withRoundedHalfEven(), 10, 0, 0, 0);
        
        test = new LocalTime(11, 30);
        check(test.hourOfDay().withRoundedCeiling(), 12, 0, 0, 0);
        check(test.hourOfDay().withRoundedFloor(), 11, 0, 0, 0);
        check(test.hourOfDay().withRoundedHalfCeiling(), 12, 0, 0, 0);
        check(test.hourOfDay().withRoundedHalfFloor(), 11, 0, 0, 0);
        check(test.hourOfDay().withRoundedHalfEven(), 12, 0, 0, 0);
    }

    public void testPropertyGetMaxMinValuesHour() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        assertEquals(0, test.hourOfDay().getMinimumValue());
        assertEquals(0, test.hourOfDay().getMinimumValueOverall());
        assertEquals(23, test.hourOfDay().getMaximumValue());
        assertEquals(23, test.hourOfDay().getMaximumValueOverall());
    }

    public void testPropertyWithMaxMinValueHour() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        check(test.hourOfDay().withMaximumValue(), 23, 20, 30, 40);
        check(test.hourOfDay().withMinimumValue(), 0, 20, 30, 40);
    }

    public void testPropertyPlusHour() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.hourOfDay().plus(9);
        check(test, 10, 20, 30, 40);
        check(copy, 19, 20, 30, 40);
        
        copy = test.hourOfDay().plus(0);
        check(copy, 10, 20, 30, 40);
        
        copy = test.hourOfDay().plus(13);
        check(copy, 23, 20, 30, 40);
        
        copy = test.hourOfDay().plus(14);
        check(copy, 0, 20, 30, 40);
        
        copy = test.hourOfDay().plus(-10);
        check(copy, 0, 20, 30, 40);
        
        copy = test.hourOfDay().plus(-11);
        check(copy, 23, 20, 30, 40);
    }

    public void testPropertyPlusNoWrapHour() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.hourOfDay().plusNoWrap(9);
        check(test, 10, 20, 30, 40);
        check(copy, 19, 20, 30, 40);
        
        copy = test.hourOfDay().plusNoWrap(0);
        check(copy, 10, 20, 30, 40);
        
        copy = test.hourOfDay().plusNoWrap(13);
        check(copy, 23, 20, 30, 40);
        
        try {
            test.hourOfDay().plusNoWrap(14);
            fail();
        } catch (IllegalArgumentException ex) {}
        check(test, 10, 20, 30, 40);
        
        copy = test.hourOfDay().plusNoWrap(-10);
        check(copy, 0, 20, 30, 40);
        
        try {
            test.hourOfDay().plusNoWrap(-11);
            fail();
        } catch (IllegalArgumentException ex) {}
        check(test, 10, 20, 30, 40);
    }

    public void testPropertyPlusWrapFieldHour() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.hourOfDay().plusWrapField(9);
        check(test, 10, 20, 30, 40);
        check(copy, 19, 20, 30, 40);
        
        copy = test.hourOfDay().plusWrapField(0);
        check(copy, 10, 20, 30, 40);
        
        copy = test.hourOfDay().plusWrapField(18);
        check(copy, 4, 20, 30, 40);
        
        copy = test.hourOfDay().plusWrapField(-15);
        check(copy, 19, 20, 30, 40);
    }

    public void testPropertySetHour() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.hourOfDay().withValue(12);
        check(test, 10, 20, 30, 40);
        check(copy, 12, 20, 30, 40);
        
        try {
            test.hourOfDay().withValue(24);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.hourOfDay().withValue(-1);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testPropertySetTextHour() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.hourOfDay().withValue("12");
        check(test, 10, 20, 30, 40);
        check(copy, 12, 20, 30, 40);
    }

    public void testPropertyWithMaximumValueHour() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.hourOfDay().withMaximumValue();
        check(test, 10, 20, 30, 40);
        check(copy, 23, 20, 30, 40);
    }

    public void testPropertyWithMinimumValueHour() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.hourOfDay().withMinimumValue();
        check(test, 10, 20, 30, 40);
        check(copy, 0, 20, 30, 40);
    }

    public void testPropertyCompareToHour() {
        LocalTime test1 = LocalTime.forInstantDefaultZone(TEST_TIME1);
        LocalTime test2 = LocalTime.forInstantDefaultZone(TEST_TIME2);
        assertEquals(true, test1.hourOfDay().compareTo(test2) < 0);
        assertEquals(true, test2.hourOfDay().compareTo(test1) > 0);
        assertEquals(true, test1.hourOfDay().compareTo(test1) == 0);
        try {
            test1.hourOfDay().compareTo((ReadablePartial) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        DateTime dt1 = new DateTime(TEST_TIME1);
        DateTime dt2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.hourOfDay().compareTo(dt2) < 0);
        assertEquals(true, test2.hourOfDay().compareTo(dt1) > 0);
        assertEquals(true, test1.hourOfDay().compareTo(dt1) == 0);
        try {
            test1.hourOfDay().compareTo((ReadableInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetMinute() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        assertSame(test.getChronology().minuteOfHour(), test.minuteOfHour().getField());
        assertEquals("minuteOfHour", test.minuteOfHour().getName());
        assertEquals("Property[minuteOfHour]", test.minuteOfHour().toString());
        assertSame(test, test.minuteOfHour().getLocalTime());
        assertEquals(20, test.minuteOfHour().get());
        assertEquals("20", test.minuteOfHour().getAsString());
        assertEquals("20", test.minuteOfHour().getAsText());
        assertEquals("20", test.minuteOfHour().getAsText(Locale.FRENCH));
        assertEquals("20", test.minuteOfHour().getAsShortText());
        assertEquals("20", test.minuteOfHour().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().minutes(), test.minuteOfHour().getDurationField());
        assertEquals(test.getChronology().hours(), test.minuteOfHour().getRangeDurationField());
        assertEquals(2, test.minuteOfHour().getMaximumTextLength(null));
        assertEquals(2, test.minuteOfHour().getMaximumShortTextLength(null));
    }

    public void testPropertyGetMaxMinValuesMinute() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        assertEquals(0, test.minuteOfHour().getMinimumValue());
        assertEquals(0, test.minuteOfHour().getMinimumValueOverall());
        assertEquals(59, test.minuteOfHour().getMaximumValue());
        assertEquals(59, test.minuteOfHour().getMaximumValueOverall());
    }

    public void testPropertyWithMaxMinValueMinute() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        check(test.minuteOfHour().withMaximumValue(), 10, 59, 30, 40);
        check(test.minuteOfHour().withMinimumValue(), 10, 0, 30, 40);
    }

    public void testPropertyPlusMinute() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.minuteOfHour().plus(9);
        check(test, 10, 20, 30, 40);
        check(copy, 10, 29, 30, 40);
        
        copy = test.minuteOfHour().plus(39);
        check(copy, 10, 59, 30, 40);
        
        copy = test.minuteOfHour().plus(40);
        check(copy, 11, 0, 30, 40);
        
        copy = test.minuteOfHour().plus(1 * 60 + 45);
        check(copy, 12, 5, 30, 40);
        
        copy = test.minuteOfHour().plus(13 * 60 + 39);
        check(copy, 23, 59, 30, 40);
        
        copy = test.minuteOfHour().plus(13 * 60 + 40);
        check(copy, 0, 0, 30, 40);
        
        copy = test.minuteOfHour().plus(-9);
        check(copy, 10, 11, 30, 40);
        
        copy = test.minuteOfHour().plus(-19);
        check(copy, 10, 1, 30, 40);
        
        copy = test.minuteOfHour().plus(-20);
        check(copy, 10, 0, 30, 40);
        
        copy = test.minuteOfHour().plus(-21);
        check(copy, 9, 59, 30, 40);
        
        copy = test.minuteOfHour().plus(-(10 * 60 + 20));
        check(copy, 0, 0, 30, 40);
        
        copy = test.minuteOfHour().plus(-(10 * 60 + 21));
        check(copy, 23, 59, 30, 40);
    }

    public void testPropertyPlusNoWrapMinute() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.minuteOfHour().plusNoWrap(9);
        check(test, 10, 20, 30, 40);
        check(copy, 10, 29, 30, 40);
        
        copy = test.minuteOfHour().plusNoWrap(39);
        check(copy, 10, 59, 30, 40);
        
        copy = test.minuteOfHour().plusNoWrap(40);
        check(copy, 11, 0, 30, 40);
        
        copy = test.minuteOfHour().plusNoWrap(1 * 60 + 45);
        check(copy, 12, 5, 30, 40);
        
        copy = test.minuteOfHour().plusNoWrap(13 * 60 + 39);
        check(copy, 23, 59, 30, 40);
        
        try {
            test.minuteOfHour().plusNoWrap(13 * 60 + 40);
            fail();
        } catch (IllegalArgumentException ex) {}
        check(test, 10, 20, 30, 40);
        
        copy = test.minuteOfHour().plusNoWrap(-9);
        check(copy, 10, 11, 30, 40);
        
        copy = test.minuteOfHour().plusNoWrap(-19);
        check(copy, 10, 1, 30, 40);
        
        copy = test.minuteOfHour().plusNoWrap(-20);
        check(copy, 10, 0, 30, 40);
        
        copy = test.minuteOfHour().plusNoWrap(-21);
        check(copy, 9, 59, 30, 40);
        
        copy = test.minuteOfHour().plusNoWrap(-(10 * 60 + 20));
        check(copy, 0, 0, 30, 40);
        
        try {
            test.minuteOfHour().plusNoWrap(-(10 * 60 + 21));
            fail();
        } catch (IllegalArgumentException ex) {}
        check(test, 10, 20, 30, 40);
    }

    public void testPropertyPlusWrapFieldMinute() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.minuteOfHour().plusWrapField(9);
        check(test, 10, 20, 30, 40);
        check(copy, 10, 29, 30, 40);
        
        copy = test.minuteOfHour().plusWrapField(49);
        check(copy, 10, 9, 30, 40);
        
        copy = test.minuteOfHour().plusWrapField(-47);
        check(copy, 10, 33, 30, 40);
    }

    public void testPropertySetMinute() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.minuteOfHour().withValue(12);
        check(test, 10, 20, 30, 40);
        check(copy, 10, 12, 30, 40);
        
        try {
            test.minuteOfHour().withValue(60);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.minuteOfHour().withValue(-1);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testPropertySetTextMinute() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.minuteOfHour().withValue("12");
        check(test, 10, 20, 30, 40);
        check(copy, 10, 12, 30, 40);
    }

    public void testPropertyCompareToMinute() {
        LocalTime test1 = LocalTime.forInstantDefaultZone(TEST_TIME1);
        LocalTime test2 = LocalTime.forInstantDefaultZone(TEST_TIME2);
        assertEquals(true, test1.minuteOfHour().compareTo(test2) < 0);
        assertEquals(true, test2.minuteOfHour().compareTo(test1) > 0);
        assertEquals(true, test1.minuteOfHour().compareTo(test1) == 0);
        try {
            test1.minuteOfHour().compareTo((ReadablePartial) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        DateTime dt1 = new DateTime(TEST_TIME1);
        DateTime dt2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.minuteOfHour().compareTo(dt2) < 0);
        assertEquals(true, test2.minuteOfHour().compareTo(dt1) > 0);
        assertEquals(true, test1.minuteOfHour().compareTo(dt1) == 0);
        try {
            test1.minuteOfHour().compareTo((ReadableInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetSecond() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        assertSame(test.getChronology().secondOfMinute(), test.secondOfMinute().getField());
        assertEquals("secondOfMinute", test.secondOfMinute().getName());
        assertEquals("Property[secondOfMinute]", test.secondOfMinute().toString());
        assertSame(test, test.secondOfMinute().getLocalTime());
        assertEquals(30, test.secondOfMinute().get());
        assertEquals("30", test.secondOfMinute().getAsString());
        assertEquals("30", test.secondOfMinute().getAsText());
        assertEquals("30", test.secondOfMinute().getAsText(Locale.FRENCH));
        assertEquals("30", test.secondOfMinute().getAsShortText());
        assertEquals("30", test.secondOfMinute().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().seconds(), test.secondOfMinute().getDurationField());
        assertEquals(test.getChronology().minutes(), test.secondOfMinute().getRangeDurationField());
        assertEquals(2, test.secondOfMinute().getMaximumTextLength(null));
        assertEquals(2, test.secondOfMinute().getMaximumShortTextLength(null));
    }

    public void testPropertyGetMaxMinValuesSecond() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        assertEquals(0, test.secondOfMinute().getMinimumValue());
        assertEquals(0, test.secondOfMinute().getMinimumValueOverall());
        assertEquals(59, test.secondOfMinute().getMaximumValue());
        assertEquals(59, test.secondOfMinute().getMaximumValueOverall());
    }

    public void testPropertyWithMaxMinValueSecond() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        check(test.secondOfMinute().withMaximumValue(), 10, 20, 59, 40);
        check(test.secondOfMinute().withMinimumValue(), 10, 20, 0, 40);
    }

    public void testPropertyPlusSecond() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.secondOfMinute().plus(9);
        check(test, 10, 20, 30, 40);
        check(copy, 10, 20, 39, 40);
        
        copy = test.secondOfMinute().plus(29);
        check(copy, 10, 20, 59, 40);
        
        copy = test.secondOfMinute().plus(30);
        check(copy, 10, 21, 0, 40);
        
        copy = test.secondOfMinute().plus(39 * 60 + 29);
        check(copy, 10, 59, 59, 40);
        
        copy = test.secondOfMinute().plus(39 * 60 + 30);
        check(copy, 11, 0, 0, 40);
        
        copy = test.secondOfMinute().plus(13 * 60 * 60 + 39 * 60 + 30);
        check(copy, 0, 0, 0, 40);
        
        copy = test.secondOfMinute().plus(-9);
        check(copy, 10, 20, 21, 40);
        
        copy = test.secondOfMinute().plus(-30);
        check(copy, 10, 20, 0, 40);
        
        copy = test.secondOfMinute().plus(-31);
        check(copy, 10, 19, 59, 40);
        
        copy = test.secondOfMinute().plus(-(10 * 60 * 60 + 20 * 60 + 30));
        check(copy, 0, 0, 0, 40);
        
        copy = test.secondOfMinute().plus(-(10 * 60 * 60 + 20 * 60 + 31));
        check(copy, 23, 59, 59, 40);
    }

    public void testPropertyPlusNoWrapSecond() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.secondOfMinute().plusNoWrap(9);
        check(test, 10, 20, 30, 40);
        check(copy, 10, 20, 39, 40);
        
        copy = test.secondOfMinute().plusNoWrap(29);
        check(copy, 10, 20, 59, 40);
        
        copy = test.secondOfMinute().plusNoWrap(30);
        check(copy, 10, 21, 0, 40);
        
        copy = test.secondOfMinute().plusNoWrap(39 * 60 + 29);
        check(copy, 10, 59, 59, 40);
        
        copy = test.secondOfMinute().plusNoWrap(39 * 60 + 30);
        check(copy, 11, 0, 0, 40);
        
        try {
            test.secondOfMinute().plusNoWrap(13 * 60 * 60 + 39 * 60 + 30);
            fail();
        } catch (IllegalArgumentException ex) {}
        check(test, 10, 20, 30, 40);
        
        copy = test.secondOfMinute().plusNoWrap(-9);
        check(copy, 10, 20, 21, 40);
        
        copy = test.secondOfMinute().plusNoWrap(-30);
        check(copy, 10, 20, 0, 40);
        
        copy = test.secondOfMinute().plusNoWrap(-31);
        check(copy, 10, 19, 59, 40);
        
        copy = test.secondOfMinute().plusNoWrap(-(10 * 60 * 60 + 20 * 60 + 30));
        check(copy, 0, 0, 0, 40);
        
        try {
            test.secondOfMinute().plusNoWrap(-(10 * 60 * 60 + 20 * 60 + 31));
            fail();
        } catch (IllegalArgumentException ex) {}
        check(test, 10, 20, 30, 40);
    }

    public void testPropertyPlusWrapFieldSecond() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.secondOfMinute().plusWrapField(9);
        check(test, 10, 20, 30, 40);
        check(copy, 10, 20, 39, 40);
        
        copy = test.secondOfMinute().plusWrapField(49);
        check(copy, 10, 20, 19, 40);
        
        copy = test.secondOfMinute().plusWrapField(-47);
        check(copy, 10, 20, 43, 40);
    }

    public void testPropertySetSecond() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.secondOfMinute().withValue(12);
        check(test, 10, 20, 30, 40);
        check(copy, 10, 20, 12, 40);
        
        try {
            test.secondOfMinute().withValue(60);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.secondOfMinute().withValue(-1);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testPropertySetTextSecond() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.secondOfMinute().withValue("12");
        check(test, 10, 20, 30, 40);
        check(copy, 10, 20, 12, 40);
    }

    public void testPropertyCompareToSecond() {
        LocalTime test1 = LocalTime.forInstantDefaultZone(TEST_TIME1);
        LocalTime test2 = LocalTime.forInstantDefaultZone(TEST_TIME2);
        assertEquals(true, test1.secondOfMinute().compareTo(test2) < 0);
        assertEquals(true, test2.secondOfMinute().compareTo(test1) > 0);
        assertEquals(true, test1.secondOfMinute().compareTo(test1) == 0);
        try {
            test1.secondOfMinute().compareTo((ReadablePartial) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        DateTime dt1 = new DateTime(TEST_TIME1);
        DateTime dt2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.secondOfMinute().compareTo(dt2) < 0);
        assertEquals(true, test2.secondOfMinute().compareTo(dt1) > 0);
        assertEquals(true, test1.secondOfMinute().compareTo(dt1) == 0);
        try {
            test1.secondOfMinute().compareTo((ReadableInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetMilli() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        assertSame(test.getChronology().millisOfSecond(), test.millisOfSecond().getField());
        assertEquals("millisOfSecond", test.millisOfSecond().getName());
        assertEquals("Property[millisOfSecond]", test.millisOfSecond().toString());
        assertSame(test, test.millisOfSecond().getLocalTime());
        assertEquals(40, test.millisOfSecond().get());
        assertEquals("40", test.millisOfSecond().getAsString());
        assertEquals("40", test.millisOfSecond().getAsText());
        assertEquals("40", test.millisOfSecond().getAsText(Locale.FRENCH));
        assertEquals("40", test.millisOfSecond().getAsShortText());
        assertEquals("40", test.millisOfSecond().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().millis(), test.millisOfSecond().getDurationField());
        assertEquals(test.getChronology().seconds(), test.millisOfSecond().getRangeDurationField());
        assertEquals(3, test.millisOfSecond().getMaximumTextLength(null));
        assertEquals(3, test.millisOfSecond().getMaximumShortTextLength(null));
    }

    public void testPropertyGetMaxMinValuesMilli() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        assertEquals(0, test.millisOfSecond().getMinimumValue());
        assertEquals(0, test.millisOfSecond().getMinimumValueOverall());
        assertEquals(999, test.millisOfSecond().getMaximumValue());
        assertEquals(999, test.millisOfSecond().getMaximumValueOverall());
    }

    public void testPropertyWithMaxMinValueMilli() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        check(test.millisOfSecond().withMaximumValue(), 10, 20, 30, 999);
        check(test.millisOfSecond().withMinimumValue(), 10, 20, 30, 0);
    }

    public void testPropertyPlusMilli() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.millisOfSecond().plus(9);
        check(test, 10, 20, 30, 40);
        check(copy, 10, 20, 30, 49);
        
        copy = test.millisOfSecond().plus(959);
        check(copy, 10, 20, 30, 999);
        
        copy = test.millisOfSecond().plus(960);
        check(copy, 10, 20, 31, 0);
        
        copy = test.millisOfSecond().plus(13 * 60 * 60 * 1000 + 39 * 60 * 1000 + 29 * 1000 + 959);
        check(copy, 23, 59, 59, 999);
        
        copy = test.millisOfSecond().plus(13 * 60 * 60 * 1000 + 39 * 60 * 1000 + 29 * 1000 + 960);
        check(copy, 0, 0, 0, 0);
        
        copy = test.millisOfSecond().plus(-9);
        check(copy, 10, 20, 30, 31);
        
        copy = test.millisOfSecond().plus(-40);
        check(copy, 10, 20, 30, 0);
        
        copy = test.millisOfSecond().plus(-41);
        check(copy, 10, 20, 29, 999);
        
        copy = test.millisOfSecond().plus(-(10 * 60 * 60 * 1000 + 20 * 60 * 1000 + 30 * 1000 + 40));
        check(copy, 0, 0, 0, 0);
        
        copy = test.millisOfSecond().plus(-(10 * 60 * 60 * 1000 + 20 * 60 * 1000 + 30 * 1000 + 41));
        check(copy, 23, 59, 59, 999);
    }

    public void testPropertyPlusNoWrapMilli() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.millisOfSecond().plusNoWrap(9);
        check(test, 10, 20, 30, 40);
        check(copy, 10, 20, 30, 49);
        
        copy = test.millisOfSecond().plusNoWrap(959);
        check(copy, 10, 20, 30, 999);
        
        copy = test.millisOfSecond().plusNoWrap(960);
        check(copy, 10, 20, 31, 0);
        
        copy = test.millisOfSecond().plusNoWrap(13 * 60 * 60 * 1000 + 39 * 60 * 1000 + 29 * 1000 + 959);
        check(copy, 23, 59, 59, 999);
        
        try {
            test.millisOfSecond().plusNoWrap(13 * 60 * 60 * 1000 + 39 * 60 * 1000 + 29 * 1000 + 960);
            fail();
        } catch (IllegalArgumentException ex) {}
        check(test, 10, 20, 30, 40);
        
        copy = test.millisOfSecond().plusNoWrap(-9);
        check(copy, 10, 20, 30, 31);
        
        copy = test.millisOfSecond().plusNoWrap(-40);
        check(copy, 10, 20, 30, 0);
        
        copy = test.millisOfSecond().plusNoWrap(-41);
        check(copy, 10, 20, 29, 999);
        
        copy = test.millisOfSecond().plusNoWrap(-(10 * 60 * 60 * 1000 + 20 * 60 * 1000 + 30 * 1000 + 40));
        check(copy, 0, 0, 0, 0);
        
        try {
            test.millisOfSecond().plusNoWrap(-(10 * 60 * 60 * 1000 + 20 * 60 * 1000 + 30 * 1000 + 41));
            fail();
        } catch (IllegalArgumentException ex) {}
        check(test, 10, 20, 30, 40);
    }

    public void testPropertyPlusWrapFieldMilli() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.millisOfSecond().plusWrapField(9);
        check(test, 10, 20, 30, 40);
        check(copy, 10, 20, 30, 49);
        
        copy = test.millisOfSecond().plusWrapField(995);
        check(copy, 10, 20, 30, 35);
        
        copy = test.millisOfSecond().plusWrapField(-47);
        check(copy, 10, 20, 30, 993);
    }

    public void testPropertySetMilli() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.millisOfSecond().withValue(12);
        check(test, 10, 20, 30, 40);
        check(copy, 10, 20, 30, 12);
        
        try {
            test.millisOfSecond().withValue(1000);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.millisOfSecond().withValue(-1);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testPropertySetTextMilli() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime copy = test.millisOfSecond().withValue("12");
        check(test, 10, 20, 30, 40);
        check(copy, 10, 20, 30, 12);
    }

    public void testPropertyCompareToMilli() {
        LocalTime test1 = LocalTime.forInstantDefaultZone(TEST_TIME1);
        LocalTime test2 = LocalTime.forInstantDefaultZone(TEST_TIME2);
        assertEquals(true, test1.millisOfSecond().compareTo(test2) < 0);
        assertEquals(true, test2.millisOfSecond().compareTo(test1) > 0);
        assertEquals(true, test1.millisOfSecond().compareTo(test1) == 0);
        try {
            test1.millisOfSecond().compareTo((ReadablePartial) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        DateTime dt1 = new DateTime(TEST_TIME1);
        DateTime dt2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.millisOfSecond().compareTo(dt2) < 0);
        assertEquals(true, test2.millisOfSecond().compareTo(dt1) > 0);
        assertEquals(true, test1.millisOfSecond().compareTo(dt1) == 0);
        try {
            test1.millisOfSecond().compareTo((ReadableInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    private void check(LocalTime test, int hour, int min, int sec, int milli) {
        assertEquals(hour, test.getHourOfDay());
        assertEquals(min, test.getMinuteOfHour());
        assertEquals(sec, test.getSecondOfMinute());
        assertEquals(milli, test.getMillisOfSecond());
    }
}
