/*
 *  Copyright 2001-2013 Stephen Colebourne
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

import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.SkipDateTimeField;

/**
 * Tests IllegalFieldValueException by triggering it from other methods.
 *
 * @author Brian S O'Neill
 */
public class TestIllegalFieldValueException extends TestCase {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestIllegalFieldValueException.class);
    }

    public TestIllegalFieldValueException(String name) {
        super(name);
    }

    public void testVerifyValueBounds() {
        try {
            FieldUtils.verifyValueBounds(ISOChronology.getInstance().monthOfYear(), -5, 1, 31);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.monthOfYear(), e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("monthOfYear", e.getFieldName());
            assertEquals(new Integer(-5), e.getIllegalNumberValue());
            assertEquals(null, e.getIllegalStringValue());
            assertEquals("-5", e.getIllegalValueAsString());
            assertEquals(new Integer(1), e.getLowerBound());
            assertEquals(new Integer(31), e.getUpperBound());
        }

        try {
            FieldUtils.verifyValueBounds(DateTimeFieldType.hourOfDay(), 27, 0, 23);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.hourOfDay(), e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("hourOfDay", e.getFieldName());
            assertEquals(new Integer(27), e.getIllegalNumberValue());
            assertEquals(null, e.getIllegalStringValue());
            assertEquals("27", e.getIllegalValueAsString());
            assertEquals(new Integer(0), e.getLowerBound());
            assertEquals(new Integer(23), e.getUpperBound());
        }

        try {
            FieldUtils.verifyValueBounds("foo", 1, 2, 3);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(null, e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("foo", e.getFieldName());
            assertEquals(new Integer(1), e.getIllegalNumberValue());
            assertEquals(null, e.getIllegalStringValue());
            assertEquals("1", e.getIllegalValueAsString());
            assertEquals(new Integer(2), e.getLowerBound());
            assertEquals(new Integer(3), e.getUpperBound());
        }
    }

    public void testSkipDateTimeField() {
        DateTimeField field = new SkipDateTimeField
            (ISOChronology.getInstanceUTC(), ISOChronology.getInstanceUTC().year(), 1970);
        try {
            field.set(0, 1970);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.year(), e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("year", e.getFieldName());
            assertEquals(new Integer(1970), e.getIllegalNumberValue());
            assertEquals(null, e.getIllegalStringValue());
            assertEquals("1970", e.getIllegalValueAsString());
            assertEquals(null, e.getLowerBound());
            assertEquals(null, e.getUpperBound());
        }
    }

    public void testSetText() {
        try {
            ISOChronology.getInstanceUTC().year().set(0, null, java.util.Locale.US);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.year(), e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("year", e.getFieldName());
            assertEquals(null, e.getIllegalNumberValue());
            assertEquals(null, e.getIllegalStringValue());
            assertEquals("null", e.getIllegalValueAsString());
            assertEquals(null, e.getLowerBound());
            assertEquals(null, e.getUpperBound());
        }

        try {
            ISOChronology.getInstanceUTC().year().set(0, "nineteen seventy", java.util.Locale.US);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.year(), e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("year", e.getFieldName());
            assertEquals(null, e.getIllegalNumberValue());
            assertEquals("nineteen seventy", e.getIllegalStringValue());
            assertEquals("nineteen seventy", e.getIllegalValueAsString());
            assertEquals(null, e.getLowerBound());
            assertEquals(null, e.getUpperBound());
        }

        try {
            ISOChronology.getInstanceUTC().era().set(0, "long ago", java.util.Locale.US);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.era(), e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("era", e.getFieldName());
            assertEquals(null, e.getIllegalNumberValue());
            assertEquals("long ago", e.getIllegalStringValue());
            assertEquals("long ago", e.getIllegalValueAsString());
            assertEquals(null, e.getLowerBound());
            assertEquals(null, e.getUpperBound());
        }

        try {
            ISOChronology.getInstanceUTC().monthOfYear().set(0, "spring", java.util.Locale.US);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.monthOfYear(), e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("monthOfYear", e.getFieldName());
            assertEquals(null, e.getIllegalNumberValue());
            assertEquals("spring", e.getIllegalStringValue());
            assertEquals("spring", e.getIllegalValueAsString());
            assertEquals(null, e.getLowerBound());
            assertEquals(null, e.getUpperBound());
        }

        try {
            ISOChronology.getInstanceUTC().dayOfWeek().set(0, "yesterday", java.util.Locale.US);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.dayOfWeek(), e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("dayOfWeek", e.getFieldName());
            assertEquals(null, e.getIllegalNumberValue());
            assertEquals("yesterday", e.getIllegalStringValue());
            assertEquals("yesterday", e.getIllegalValueAsString());
            assertEquals(null, e.getLowerBound());
            assertEquals(null, e.getUpperBound());
        }

        try {
            ISOChronology.getInstanceUTC().halfdayOfDay().set(0, "morning", java.util.Locale.US);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.halfdayOfDay(), e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("halfdayOfDay", e.getFieldName());
            assertEquals(null, e.getIllegalNumberValue());
            assertEquals("morning", e.getIllegalStringValue());
            assertEquals("morning", e.getIllegalValueAsString());
            assertEquals(null, e.getLowerBound());
            assertEquals(null, e.getUpperBound());
        }
    }

    public void testZoneTransition() {
        DateTime dt = new DateTime
            (2005, 4, 3, 1, 0, 0, 0, DateTimeZone.forID("America/Los_Angeles"));
        try {
            dt.hourOfDay().setCopy(2);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.hourOfDay(), e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("hourOfDay", e.getFieldName());
            assertEquals(new Integer(2), e.getIllegalNumberValue());
            assertEquals(null, e.getIllegalStringValue());
            assertEquals("2", e.getIllegalValueAsString());
            assertEquals(null, e.getLowerBound());
            assertEquals(null, e.getUpperBound());
        }
    }

    public void testJulianYearZero() {
        DateTime dt = new DateTime(JulianChronology.getInstanceUTC());
        try {
            dt.year().setCopy(0);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.year(), e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("year", e.getFieldName());
            assertEquals(new Integer(0), e.getIllegalNumberValue());
            assertEquals(null, e.getIllegalStringValue());
            assertEquals("0", e.getIllegalValueAsString());
            assertEquals(null, e.getLowerBound());
            assertEquals(null, e.getUpperBound());
        }
    }

    public void testGJCutover() {
        DateTime dt = new DateTime("1582-10-04", GJChronology.getInstanceUTC());
        try {
            dt.dayOfMonth().setCopy(5);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.dayOfMonth(), e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("dayOfMonth", e.getFieldName());
            assertEquals(new Integer(5), e.getIllegalNumberValue());
            assertEquals(null, e.getIllegalStringValue());
            assertEquals("5", e.getIllegalValueAsString());
            assertEquals(null, e.getLowerBound());
            assertEquals(null, e.getUpperBound());
        }

        dt = new DateTime("1582-10-15", GJChronology.getInstanceUTC());
        try {
            dt.dayOfMonth().setCopy(14);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.dayOfMonth(), e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("dayOfMonth", e.getFieldName());
            assertEquals(new Integer(14), e.getIllegalNumberValue());
            assertEquals(null, e.getIllegalStringValue());
            assertEquals("14", e.getIllegalValueAsString());
            assertEquals(null, e.getLowerBound());
            assertEquals(null, e.getUpperBound());
        }
    }

    @SuppressWarnings("deprecation")
    public void testReadablePartialValidate() {
        try {
            new YearMonthDay(1970, -5, 1);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.monthOfYear(), e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("monthOfYear", e.getFieldName());
            assertEquals(new Integer(-5), e.getIllegalNumberValue());
            assertEquals(null, e.getIllegalStringValue());
            assertEquals("-5", e.getIllegalValueAsString());
            assertEquals(new Integer(1), e.getLowerBound());
            assertEquals(null, e.getUpperBound());
        }

        try {
            new YearMonthDay(1970, 500, 1);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.monthOfYear(), e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("monthOfYear", e.getFieldName());
            assertEquals(new Integer(500), e.getIllegalNumberValue());
            assertEquals(null, e.getIllegalStringValue());
            assertEquals("500", e.getIllegalValueAsString());
            assertEquals(null, e.getLowerBound());
            assertEquals(new Integer(12), e.getUpperBound());
        }

        try {
            new YearMonthDay(1970, 2, 30);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.dayOfMonth(), e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("dayOfMonth", e.getFieldName());
            assertEquals(new Integer(30), e.getIllegalNumberValue());
            assertEquals(null, e.getIllegalStringValue());
            assertEquals("30", e.getIllegalValueAsString());
            assertEquals(null, e.getLowerBound());
            assertEquals(new Integer(28), e.getUpperBound());
        }
    }

    // Test extra constructors not currently called by anything
    public void testOtherConstructors() {
        IllegalFieldValueException e = new IllegalFieldValueException
            (DurationFieldType.days(), new Integer(1), new Integer(2), new Integer(3));
        assertEquals(null, e.getDateTimeFieldType());
        assertEquals(DurationFieldType.days(), e.getDurationFieldType());
        assertEquals("days", e.getFieldName());
        assertEquals(new Integer(1), e.getIllegalNumberValue());
        assertEquals(null, e.getIllegalStringValue());
        assertEquals("1", e.getIllegalValueAsString());
        assertEquals(new Integer(2), e.getLowerBound());
        assertEquals(new Integer(3), e.getUpperBound());

        e = new IllegalFieldValueException(DurationFieldType.months(), "five");
        assertEquals(null, e.getDateTimeFieldType());
        assertEquals(DurationFieldType.months(), e.getDurationFieldType());
        assertEquals("months", e.getFieldName());
        assertEquals(null, e.getIllegalNumberValue());
        assertEquals("five", e.getIllegalStringValue());
        assertEquals("five", e.getIllegalValueAsString());
        assertEquals(null, e.getLowerBound());
        assertEquals(null, e.getUpperBound());

        e = new IllegalFieldValueException("months", "five");
        assertEquals(null, e.getDateTimeFieldType());
        assertEquals(null, e.getDurationFieldType());
        assertEquals("months", e.getFieldName());
        assertEquals(null, e.getIllegalNumberValue());
        assertEquals("five", e.getIllegalStringValue());
        assertEquals("five", e.getIllegalValueAsString());
        assertEquals(null, e.getLowerBound());
        assertEquals(null, e.getUpperBound());
    }
}
