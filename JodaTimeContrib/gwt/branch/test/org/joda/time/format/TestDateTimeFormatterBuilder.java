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
package org.joda.time.format;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

/**
 * This class is a Junit unit test for DateTimeFormatterBuilder.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 */
public class TestDateTimeFormatterBuilder extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestDateTimeFormatterBuilder.class);
    }

    public TestDateTimeFormatterBuilder(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    //-----------------------------------------------------------------------
    public void test_toFormatter() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder();
        try {
            bld.toFormatter();
            fail();
        } catch (UnsupportedOperationException ex) {}
        bld.appendLiteral('X');
        assertNotNull(bld.toFormatter());
    }

    public void test_toPrinter() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder();
        try {
            bld.toPrinter();
            fail();
        } catch (UnsupportedOperationException ex) {}
        bld.appendLiteral('X');
        assertNotNull(bld.toPrinter());
    }

    public void test_toParser() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder();
        try {
            bld.toParser();
            fail();
        } catch (UnsupportedOperationException ex) {}
        bld.appendLiteral('X');
        assertNotNull(bld.toParser());
    }

    //-----------------------------------------------------------------------
    public void test_canBuildFormatter() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder();
        assertEquals(false, bld.canBuildFormatter());
        bld.appendLiteral('X');
        assertEquals(true, bld.canBuildFormatter());
    }

    public void test_canBuildPrinter() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder();
        assertEquals(false, bld.canBuildPrinter());
        bld.appendLiteral('X');
        assertEquals(true, bld.canBuildPrinter());
    }

    public void test_canBuildParser() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder();
        assertEquals(false, bld.canBuildParser());
        bld.appendLiteral('X');
        assertEquals(true, bld.canBuildParser());
    }

    //-----------------------------------------------------------------------
    public void test_append_Formatter() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder();
        bld.appendLiteral('Y');
        DateTimeFormatter f = bld.toFormatter();
        
        DateTimeFormatterBuilder bld2 = new DateTimeFormatterBuilder();
        bld2.appendLiteral('X');
        bld2.append(f);
        bld2.appendLiteral('Z');
        assertEquals("XYZ", bld2.toFormatter().print(0L));
    }

    //-----------------------------------------------------------------------
    public void test_append_Printer() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder();
        bld.appendLiteral('Y');
        DateTimePrinter p = bld.toPrinter();
        
        DateTimeFormatterBuilder bld2 = new DateTimeFormatterBuilder();
        bld2.appendLiteral('X');
        bld2.append(p);
        bld2.appendLiteral('Z');
        assertEquals("XYZ", bld2.toFormatter().print(0L));
    }

    //-----------------------------------------------------------------------
    public void test_appendFixedDecimal() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder();
        bld.appendFixedDecimal(DateTimeFieldType.year(), 4);
        DateTimeFormatter f = bld.toFormatter();

        assertEquals("2007", f.print(new DateTime("2007-01-01")));
        assertEquals("0123", f.print(new DateTime("123-01-01")));
        assertEquals("0001", f.print(new DateTime("1-2-3")));
        assertEquals("99999", f.print(new DateTime("99999-2-3")));
        assertEquals("-0099", f.print(new DateTime("-99-2-3")));
        assertEquals("0000", f.print(new DateTime("0-2-3")));

        assertEquals(2001, f.parseDateTime("2001").getYear());
        try {
            f.parseDateTime("-2001");
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            f.parseDateTime("200");
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            f.parseDateTime("20016");
            fail();
        } catch (IllegalArgumentException e) {
        }

        bld = new DateTimeFormatterBuilder();
        bld.appendFixedDecimal(DateTimeFieldType.hourOfDay(), 2);
        bld.appendLiteral(':');
        bld.appendFixedDecimal(DateTimeFieldType.minuteOfHour(), 2);
        bld.appendLiteral(':');
        bld.appendFixedDecimal(DateTimeFieldType.secondOfMinute(), 2);
        f = bld.toFormatter();

        assertEquals("01:02:34", f.print(new DateTime("T1:2:34")));

        DateTime dt = f.parseDateTime("01:02:34");
        assertEquals(1, dt.getHourOfDay());
        assertEquals(2, dt.getMinuteOfHour());
        assertEquals(34, dt.getSecondOfMinute());

        try {
            f.parseDateTime("0145:02:34");
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            f.parseDateTime("01:0:34");
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    //-----------------------------------------------------------------------
    public void test_appendFixedSignedDecimal() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder();
        bld.appendFixedSignedDecimal(DateTimeFieldType.year(), 4);
        DateTimeFormatter f = bld.toFormatter();

        assertEquals("2007", f.print(new DateTime("2007-01-01")));
        assertEquals("0123", f.print(new DateTime("123-01-01")));
        assertEquals("0001", f.print(new DateTime("1-2-3")));
        assertEquals("99999", f.print(new DateTime("99999-2-3")));
        assertEquals("-0099", f.print(new DateTime("-99-2-3")));
        assertEquals("0000", f.print(new DateTime("0-2-3")));

        assertEquals(2001, f.parseDateTime("2001").getYear());
        assertEquals(-2001, f.parseDateTime("-2001").getYear());
        assertEquals(2001, f.parseDateTime("+2001").getYear());
        try {
            f.parseDateTime("20016");
            fail();
        } catch (IllegalArgumentException e) {
        }
    }
}
