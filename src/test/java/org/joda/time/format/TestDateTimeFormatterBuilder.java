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
package org.joda.time.format;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

/**
 * This class is a Junit unit test for DateTimeFormatterBuilder.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 */
public class TestDateTimeFormatterBuilder extends TestCase {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
    private static final DateTimeZone NEW_YORK = DateTimeZone.forID("America/New_York");
    private static final DateTimeZone LOS_ANGELES = DateTimeZone.forID("America/Los_Angeles");

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
        DateTimeFormatter f = bld2.toFormatter();
        assertEquals(true, f.isPrinter());
        assertEquals(false, f.isParser());
        assertEquals("XYZ", f.print(0L));
    }

    public void test_append_nullPrinter() {
        try {
            DateTimeFormatterBuilder bld2 = new DateTimeFormatterBuilder();
            bld2.append((DateTimePrinter) null);
            fail();
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    //-----------------------------------------------------------------------
    public void test_append_Parser() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder();
        bld.appendLiteral('Y');
        DateTimeParser p = bld.toParser();
        
        DateTimeFormatterBuilder bld2 = new DateTimeFormatterBuilder();
        bld2.appendLiteral('X');
        bld2.append(p);
        bld2.appendLiteral('Z');
        DateTimeFormatter f = bld2.toFormatter();
        assertEquals(false, f.isPrinter());
        assertEquals(true, f.isParser());
        assertEquals(0, f.withZoneUTC().parseMillis("XYZ"));
    }

    public void test_append_nullParser() {
        try {
            DateTimeFormatterBuilder bld2 = new DateTimeFormatterBuilder();
            bld2.append((DateTimeParser) null);
            fail();
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    //-----------------------------------------------------------------------
    public void test_append_Printer_nullParser() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder();
        bld.appendLiteral('Y');
        DateTimePrinter p = bld.toPrinter();
        
        try {
            DateTimeFormatterBuilder bld2 = new DateTimeFormatterBuilder();
            bld2.append(p, (DateTimeParser) null);
            fail();
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    public void test_append_nullPrinter_Parser() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder();
        bld.appendLiteral('Y');
        DateTimeParser p = bld.toParser();
        
        try {
            DateTimeFormatterBuilder bld2 = new DateTimeFormatterBuilder();
            bld2.append((DateTimePrinter) null, p);
            fail();
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    //-----------------------------------------------------------------------
    public void test_appendOptional_Parser() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder();
        bld.appendLiteral('Y');
        DateTimeParser p = bld.toParser();
        
        DateTimeFormatterBuilder bld2 = new DateTimeFormatterBuilder();
        bld2.appendLiteral('X');
        bld2.appendOptional(p);
        bld2.appendLiteral('Z');
        DateTimeFormatter f = bld2.toFormatter();
        assertEquals(false, f.isPrinter());
        assertEquals(true, f.isParser());
        assertEquals(0, f.withZoneUTC().parseMillis("XYZ"));
    }

    public void test_appendOptional_nullParser() {
        try {
            DateTimeFormatterBuilder bld2 = new DateTimeFormatterBuilder();
            bld2.appendOptional((DateTimeParser) null);
            fail();
        } catch (IllegalArgumentException ex) {
            // expected
        }
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

    //-----------------------------------------------------------------------
    public void test_appendTimeZoneId() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder();
        bld.appendTimeZoneId();
        DateTimeFormatter f = bld.toFormatter();
        
        assertEquals("Asia/Tokyo", f.print(new DateTime(2007, 3, 4, 0, 0, 0, TOKYO)));
        assertEquals(TOKYO, f.parseDateTime("Asia/Tokyo").getZone());
        try {
            f.parseDateTime("Nonsense");
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    public void test_printParseZoneTokyo() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm ").appendTimeZoneId();
        DateTimeFormatter f = bld.toFormatter();
        
        DateTime dt = new DateTime(2007, 3, 4, 12, 30, 0, TOKYO);
        assertEquals("2007-03-04 12:30 Asia/Tokyo", f.print(dt));
        assertEquals(dt, f.parseDateTime("2007-03-04 12:30 Asia/Tokyo"));
    }

    public void test_printParseZoneParis() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm ").appendTimeZoneId();
        DateTimeFormatter f = bld.toFormatter();
        
        DateTime dt = new DateTime(2007, 3, 4, 12, 30, 0, PARIS);
        assertEquals("2007-03-04 12:30 Europe/Paris", f.print(dt));
        assertEquals(dt, f.parseDateTime("2007-03-04 12:30 Europe/Paris"));
        assertEquals(dt, f.withOffsetParsed().parseDateTime("2007-03-04 12:30 Europe/Paris"));
    }

    public void test_printParseZoneDawsonCreek() {  // clashes with shorter Dawson
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm ").appendTimeZoneId();
        DateTimeFormatter f = bld.toFormatter();
        
        DateTime dt = new DateTime(2007, 3, 4, 12, 30, 0, DateTimeZone.forID("America/Dawson_Creek"));
        assertEquals("2007-03-04 12:30 America/Dawson_Creek", f.print(dt));
        assertEquals(dt, f.parseDateTime("2007-03-04 12:30 America/Dawson_Creek"));
    }

    public void test_printParseOffset() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm ").appendTimeZoneOffset("Z", true, 2, 2);
        DateTimeFormatter f = bld.toFormatter();
        
        DateTime dt = new DateTime(2007, 3, 4, 12, 30, 0, TOKYO);
        assertEquals("2007-03-04 12:30 +09:00", f.print(dt));
        assertEquals(dt.withZone(DateTimeZone.getDefault()), f.parseDateTime("2007-03-04 12:30 +09:00"));
        assertEquals(dt, f.withZone(TOKYO).parseDateTime("2007-03-04 12:30 +09:00"));
        assertEquals(dt.withZone(DateTimeZone.forOffsetHours(9)), f.withOffsetParsed().parseDateTime("2007-03-04 12:30 +09:00"));
    }

    public void test_printParseOffsetAndZone() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm ").appendTimeZoneOffset("Z", true, 2, 2).appendLiteral(' ').appendTimeZoneId();
        DateTimeFormatter f = bld.toFormatter();
        
        DateTime dt = new DateTime(2007, 3, 4, 12, 30, 0, TOKYO);
        assertEquals("2007-03-04 12:30 +09:00 Asia/Tokyo", f.print(dt));
        assertEquals(dt, f.withZone(TOKYO).parseDateTime("2007-03-04 12:30 +09:00 Asia/Tokyo"));
        assertEquals(dt.withZone(PARIS), f.withZone(PARIS).parseDateTime("2007-03-04 12:30 +09:00 Asia/Tokyo"));
        assertEquals(dt.withZone(DateTimeZone.forOffsetHours(9)), f.withOffsetParsed().parseDateTime("2007-03-04 12:30 +09:00 Asia/Tokyo"));
    }

    public void test_parseWrongOffset() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm ").appendTimeZoneOffset("Z", true, 2, 2);
        DateTimeFormatter f = bld.toFormatter();
        
        DateTime expected = new DateTime(2007, 3, 4, 12, 30, 0, DateTimeZone.forOffsetHours(7));
        // parses offset time then adjusts to requested zone
        assertEquals(expected.withZone(TOKYO), f.withZone(TOKYO).parseDateTime("2007-03-04 12:30 +07:00"));
        // parses offset time returning offset zone
        assertEquals(expected, f.withOffsetParsed().parseDateTime("2007-03-04 12:30 +07:00"));
        // parses offset time then converts to default zone
        assertEquals(expected.withZone(DateTimeZone.getDefault()), f.parseDateTime("2007-03-04 12:30 +07:00"));
    }

    public void test_parseWrongOffsetAndZone() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm ").appendTimeZoneOffset("Z", true, 2, 2).appendLiteral(' ').appendTimeZoneId();
        DateTimeFormatter f = bld.toFormatter();
        
        DateTime expected = new DateTime(2007, 3, 4, 12, 30, 0, DateTimeZone.forOffsetHours(7));
        // parses offset time then adjusts to parsed zone
        assertEquals(expected.withZone(TOKYO), f.parseDateTime("2007-03-04 12:30 +07:00 Asia/Tokyo"));
        // parses offset time then adjusts to requested zone
        assertEquals(expected.withZone(TOKYO), f.withZone(TOKYO).parseDateTime("2007-03-04 12:30 +07:00 Asia/Tokyo"));
        // parses offset time returning offset zone (ignores zone)
        assertEquals(expected, f.withOffsetParsed().parseDateTime("2007-03-04 12:30 +07:00 Asia/Tokyo"));
    }

    //-----------------------------------------------------------------------
    public void test_localPrintParseZoneTokyo() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm ").appendTimeZoneId();
        DateTimeFormatter f = bld.toFormatter();
        
        DateTime dt = new DateTime(2007, 3, 4, 12, 30, 0, TOKYO);
        assertEquals("2007-03-04 12:30 Asia/Tokyo", f.print(dt));
        
        LocalDateTime expected = new LocalDateTime(2007, 3, 4, 12, 30);
        assertEquals(expected, f.parseLocalDateTime("2007-03-04 12:30 Asia/Tokyo"));
    }

    public void test_localPrintParseOffset() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm ").appendTimeZoneOffset("Z", true, 2, 2);
        DateTimeFormatter f = bld.toFormatter();
        
        DateTime dt = new DateTime(2007, 3, 4, 12, 30, 0, TOKYO);
        assertEquals("2007-03-04 12:30 +09:00", f.print(dt));
        
        LocalDateTime expected = new LocalDateTime(2007, 3, 4, 12, 30);
        assertEquals(expected, f.parseLocalDateTime("2007-03-04 12:30 +09:00"));
        assertEquals(expected, f.withZone(TOKYO).parseLocalDateTime("2007-03-04 12:30 +09:00"));
        assertEquals(expected, f.withOffsetParsed().parseLocalDateTime("2007-03-04 12:30 +09:00"));
    }

    public void test_localPrintParseOffsetAndZone() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm ").appendTimeZoneOffset("Z", true, 2, 2).appendLiteral(' ').appendTimeZoneId();
        DateTimeFormatter f = bld.toFormatter();
        
        DateTime dt = new DateTime(2007, 3, 4, 12, 30, 0, TOKYO);
        assertEquals("2007-03-04 12:30 +09:00 Asia/Tokyo", f.print(dt));
        
        LocalDateTime expected = new LocalDateTime(2007, 3, 4, 12, 30);
        assertEquals(expected, f.withZone(TOKYO).parseLocalDateTime("2007-03-04 12:30 +09:00 Asia/Tokyo"));
        assertEquals(expected, f.withZone(PARIS).parseLocalDateTime("2007-03-04 12:30 +09:00 Asia/Tokyo"));
    }

    public void test_localParseWrongOffsetAndZone() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm ").appendTimeZoneOffset("Z", true, 2, 2).appendLiteral(' ').appendTimeZoneId();
        DateTimeFormatter f = bld.toFormatter();
        
        LocalDateTime expected = new LocalDateTime(2007, 3, 4, 12, 30);
        // parses offset time then adjusts to parsed zone
        assertEquals(expected, f.parseLocalDateTime("2007-03-04 12:30 +07:00 Asia/Tokyo"));
        // parses offset time then adjusts to requested zone
        assertEquals(expected, f.withZone(TOKYO).parseLocalDateTime("2007-03-04 12:30 +07:00 Asia/Tokyo"));
        // parses offset time returning offset zone (ignores zone)
        assertEquals(expected, f.withOffsetParsed().parseLocalDateTime("2007-03-04 12:30 +07:00 Asia/Tokyo"));
    }

    //-----------------------------------------------------------------------
    public void test_printParseShortName() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm ").appendTimeZoneShortName();
        DateTimeFormatter f = bld.toFormatter().withLocale(Locale.ENGLISH);
        
        assertEquals(true, f.isPrinter());
        assertEquals(false, f.isParser());
        DateTime dt1 = new DateTime(2011, 1, 4, 12, 30, 0, LONDON);
        assertEquals("2011-01-04 12:30 GMT", f.print(dt1));
        DateTime dt2 = new DateTime(2011, 7, 4, 12, 30, 0, LONDON);
        assertEquals("2011-07-04 12:30 BST", f.print(dt2));
        try {
            f.parseDateTime("2007-03-04 12:30 GMT");
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    public void test_printParseShortNameWithLookup() {
        Map<String, DateTimeZone> lookup = new LinkedHashMap<String, DateTimeZone>();
        lookup.put("GMT", LONDON);
        lookup.put("BST", LONDON);
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm ").appendTimeZoneShortName(lookup);
        DateTimeFormatter f = bld.toFormatter().withLocale(Locale.ENGLISH);
        
        assertEquals(true, f.isPrinter());
        assertEquals(true, f.isParser());
        DateTime dt1 = new DateTime(2011, 1, 4, 12, 30, 0, LONDON);
        assertEquals("2011-01-04 12:30 GMT", f.print(dt1));
        DateTime dt2 = new DateTime(2011, 7, 4, 12, 30, 0, LONDON);
        assertEquals("2011-07-04 12:30 BST", f.print(dt2));
        
        assertEquals(dt1, f.parseDateTime("2011-01-04 12:30 GMT"));
        assertEquals(dt2, f.parseDateTime("2011-07-04 12:30 BST"));
        try {
            f.parseDateTime("2007-03-04 12:30 EST");
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    public void test_printParseShortNameWithAutoLookup() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm ").appendTimeZoneShortName(null);
        DateTimeFormatter f = bld.toFormatter().withLocale(Locale.ENGLISH);
        
        assertEquals(true, f.isPrinter());
        assertEquals(true, f.isParser());
        DateTime dt1 = new DateTime(2011, 1, 4, 12, 30, 0, NEW_YORK);
        assertEquals("2011-01-04 12:30 EST", f.print(dt1));
        DateTime dt2 = new DateTime(2011, 7, 4, 12, 30, 0, NEW_YORK);
        assertEquals("2011-07-04 12:30 EDT", f.print(dt2));
        DateTime dt3 = new DateTime(2011, 1, 4, 12, 30, 0, LOS_ANGELES);
        assertEquals("2011-01-04 12:30 PST", f.print(dt3));
        DateTime dt4 = new DateTime(2011, 7, 4, 12, 30, 0, LOS_ANGELES);
        assertEquals("2011-07-04 12:30 PDT", f.print(dt4));
        
        assertEquals(dt1.getZone() + " " + f.parseDateTime("2011-01-04 12:30 EST").getZone(), dt1, f.parseDateTime("2011-01-04 12:30 EST"));
        assertEquals(dt2, f.parseDateTime("2011-07-04 12:30 EDT"));
        assertEquals(dt3, f.parseDateTime("2011-01-04 12:30 PST"));
        assertEquals(dt4, f.parseDateTime("2011-07-04 12:30 PDT"));
        try {
            f.parseDateTime("2007-03-04 12:30 PPP");
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    //-----------------------------------------------------------------------
    public void test_printParseLongName() {
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm ").appendTimeZoneName();
        DateTimeFormatter f = bld.toFormatter().withLocale(Locale.ENGLISH);
        
        assertEquals(true, f.isPrinter());
        assertEquals(false, f.isParser());
        DateTime dt1 = new DateTime(2011, 1, 4, 12, 30, 0, LONDON);
        assertEquals("2011-01-04 12:30 Greenwich Mean Time", f.print(dt1));
        DateTime dt2 = new DateTime(2011, 7, 4, 12, 30, 0, LONDON);
        assertEquals("2011-07-04 12:30 British Summer Time", f.print(dt2));
        try {
            f.parseDateTime("2007-03-04 12:30 GMT");
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    public void test_printParseLongNameWithLookup() {
        Map<String, DateTimeZone> lookup = new LinkedHashMap<String, DateTimeZone>();
        lookup.put("Greenwich Mean Time", LONDON);
        lookup.put("British Summer Time", LONDON);
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm ").appendTimeZoneName(lookup);
        DateTimeFormatter f = bld.toFormatter().withLocale(Locale.ENGLISH);
        
        assertEquals(true, f.isPrinter());
        assertEquals(true, f.isParser());
        DateTime dt1 = new DateTime(2011, 1, 4, 12, 30, 0, LONDON);
        assertEquals("2011-01-04 12:30 Greenwich Mean Time", f.print(dt1));
        DateTime dt2 = new DateTime(2011, 7, 4, 12, 30, 0, LONDON);
        assertEquals("2011-07-04 12:30 British Summer Time", f.print(dt2));
        
        assertEquals(dt1, f.parseDateTime("2011-01-04 12:30 Greenwich Mean Time"));
        assertEquals(dt2, f.parseDateTime("2011-07-04 12:30 British Summer Time"));
        try {
            f.parseDateTime("2007-03-04 12:30 EST");
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

}
