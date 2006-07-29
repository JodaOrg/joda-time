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
package org.joda.time.format;

import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTimeZone;

/**
 * This class is a Junit unit test for ISODateTimeFormat parsing.
 *
 * @author Stephen Colebourne
 */
public class TestISODateTimeFormatParsing extends TestCase {

    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestISODateTimeFormatParsing.class);
    }

    public TestISODateTimeFormatParsing(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(DateTimeZone.forID("Europe/London"));
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    protected void tearDown() throws Exception {
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    //-----------------------------------------------------------------------
    public void test_dateParser() {
        DateTimeFormatter parser = ISODateTimeFormat.dateParser();
        assertParse(parser, true, "2006-06-09");
        assertParse(parser, true, "2006-W27-3");
        assertParse(parser, true, "2006-123");
        assertParse(parser, true, "2006-06-09T+02:00");
        assertParse(parser, true, "2006-W27-3T+02:00");
        assertParse(parser, true, "2006-123T+02:00");
        
        assertParse(parser, false, "2006-06-09T10:20:30.040");
        assertParse(parser, false, "2006-W27-3T10:20:30.040");
        assertParse(parser, false, "2006-123T10:20:30.040");
        assertParse(parser, false, "2006-06-09T10:20:30.040+02:00");
        assertParse(parser, false, "2006-W27-3T10:20:30.040+02:00");
        assertParse(parser, false, "2006-123T10:20:30.040+02:00");
        
        assertParse(parser, false, "T10:20:30.040");
        assertParse(parser, false, "T10.5");
        assertParse(parser, false, "T10:20:30.040+02:00");
        assertParse(parser, false, "T10.5+02:00");
        
        assertParse(parser, false, "10:20:30.040");
        assertParse(parser, false, "10.5");
        assertParse(parser, false, "10:20:30.040+02:00");
        assertParse(parser, false, "10.5+02:00");
    }

    //-----------------------------------------------------------------------
    public void test_localDateParser() {
        DateTimeFormatter parser = ISODateTimeFormat.localDateParser();
        assertEquals(DateTimeZone.UTC, parser.getZone());
        assertParse(parser, true, "2006-06-09");
        assertParse(parser, true, "2006-W27-3");
        assertParse(parser, true, "2006-123");
        assertParse(parser, false, "2006-06-09T+02:00");
        assertParse(parser, false, "2006-W27-3T+02:00");
        assertParse(parser, false, "2006-123T+02:00");
        
        assertParse(parser, false, "2006-06-09T10:20:30.040");
        assertParse(parser, false, "2006-W27-3T10:20:30.040");
        assertParse(parser, false, "2006-123T10:20:30.040");
        assertParse(parser, false, "2006-06-09T10:20:30.040+02:00");
        assertParse(parser, false, "2006-W27-3T10:20:30.040+02:00");
        assertParse(parser, false, "2006-123T10:20:30.040+02:00");
        
        assertParse(parser, false, "T10:20:30.040");
        assertParse(parser, false, "T10.5");
        assertParse(parser, false, "T10:20:30.040+02:00");
        assertParse(parser, false, "T10.5+02:00");
        
        assertParse(parser, false, "10:20:30.040");
        assertParse(parser, false, "10.5");
        assertParse(parser, false, "10:20:30.040+02:00");
        assertParse(parser, false, "10.5+02:00");
    }

    //-----------------------------------------------------------------------
    public void test_dateElementParser() {
        DateTimeFormatter parser = ISODateTimeFormat.dateElementParser();
        assertParse(parser, true, "2006-06-09");
        assertParse(parser, true, "2006-W27-3");
        assertParse(parser, true, "2006-123");
        assertParse(parser, false, "2006-06-09T+02:00");
        assertParse(parser, false, "2006-W27-3T+02:00");
        assertParse(parser, false, "2006-123T+02:00");
        
        assertParse(parser, false, "2006-06-09T10:20:30.040");
        assertParse(parser, false, "2006-W27-3T10:20:30.040");
        assertParse(parser, false, "2006-123T10:20:30.040");
        assertParse(parser, false, "2006-06-09T10:20:30.040+02:00");
        assertParse(parser, false, "2006-W27-3T10:20:30.040+02:00");
        assertParse(parser, false, "2006-123T10:20:30.040+02:00");
        
        assertParse(parser, false, "T10:20:30.040");
        assertParse(parser, false, "T10.5");
        assertParse(parser, false, "T10:20:30.040+02:00");
        assertParse(parser, false, "T10.5+02:00");
        
        assertParse(parser, false, "10:20:30.040");
        assertParse(parser, false, "10.5");
        assertParse(parser, false, "10:20:30.040+02:00");
        assertParse(parser, false, "10.5+02:00");
    }

    //-----------------------------------------------------------------------
    public void test_timeParser() {
        DateTimeFormatter parser = ISODateTimeFormat.timeParser();
        assertParse(parser, false, "2006-06-09");
        assertParse(parser, false, "2006-W27-3");
        assertParse(parser, false, "2006-123");
        assertParse(parser, false, "2006-06-09T+02:00");
        assertParse(parser, false, "2006-W27-3T+02:00");
        assertParse(parser, false, "2006-123T+02:00");
        
        assertParse(parser, false, "2006-06-09T10:20:30.040");
        assertParse(parser, false, "2006-W27-3T10:20:30.040");
        assertParse(parser, false, "2006-123T10:20:30.040");
        assertParse(parser, false, "2006-06-09T10:20:30.040+02:00");
        assertParse(parser, false, "2006-W27-3T10:20:30.040+02:00");
        assertParse(parser, false, "2006-123T10:20:30.040+02:00");
        
        assertParse(parser, true, "T10:20:30.040");
        assertParse(parser, true, "T10.5");
        assertParse(parser, true, "T10:20:30.040+02:00");
        assertParse(parser, true, "T10.5+02:00");
        
        assertParse(parser, true, "10:20:30.040");
        assertParse(parser, true, "10.5");
        assertParse(parser, true, "10:20:30.040+02:00");
        assertParse(parser, true, "10.5+02:00");
    }

    //-----------------------------------------------------------------------
    public void test_localTimeParser() {
        DateTimeFormatter parser = ISODateTimeFormat.localTimeParser();
        assertEquals(DateTimeZone.UTC, parser.getZone());
        assertParse(parser, false, "2006-06-09");
        assertParse(parser, false, "2006-W27-3");
        assertParse(parser, false, "2006-123");
        assertParse(parser, false, "2006-06-09T+02:00");
        assertParse(parser, false, "2006-W27-3T+02:00");
        assertParse(parser, false, "2006-123T+02:00");
        
        assertParse(parser, false, "2006-06-09T10:20:30.040");
        assertParse(parser, false, "2006-W27-3T10:20:30.040");
        assertParse(parser, false, "2006-123T10:20:30.040");
        assertParse(parser, false, "2006-06-09T10:20:30.040+02:00");
        assertParse(parser, false, "2006-W27-3T10:20:30.040+02:00");
        assertParse(parser, false, "2006-123T10:20:30.040+02:00");
        
        assertParse(parser, true, "T10:20:30.040");
        assertParse(parser, true, "T10.5");
        assertParse(parser, false, "T10:20:30.040+02:00");
        assertParse(parser, false, "T10.5+02:00");
        
        assertParse(parser, true, "10:20:30.040");
        assertParse(parser, true, "10.5");
        assertParse(parser, false, "10:20:30.040+02:00");
        assertParse(parser, false, "10.5+02:00");
        
        assertParse(parser, true, "00:00:10.512345678");
        assertEquals(10512, parser.parseMillis("00:00:10.512345678"));
    }

    //-----------------------------------------------------------------------
    public void test_timeElementParser() {
        DateTimeFormatter parser = ISODateTimeFormat.timeElementParser();
        assertParse(parser, false, "2006-06-09");
        assertParse(parser, false, "2006-W27-3");
        assertParse(parser, false, "2006-123");
        assertParse(parser, false, "2006-06-09T+02:00");
        assertParse(parser, false, "2006-W27-3T+02:00");
        assertParse(parser, false, "2006-123T+02:00");
        
        assertParse(parser, false, "2006-06-09T10:20:30.040");
        assertParse(parser, false, "2006-W27-3T10:20:30.040");
        assertParse(parser, false, "2006-123T10:20:30.040");
        assertParse(parser, false, "2006-06-09T10:20:30.040+02:00");
        assertParse(parser, false, "2006-W27-3T10:20:30.040+02:00");
        assertParse(parser, false, "2006-123T10:20:30.040+02:00");
        
        assertParse(parser, false, "T10:20:30.040");
        assertParse(parser, false, "T10.5");
        assertParse(parser, false, "T10:20:30.040+02:00");
        assertParse(parser, false, "T10.5+02:00");
        
        assertParse(parser, true, "10:20:30.040");
        assertParse(parser, true, "10.5");
        assertParse(parser, false, "10:20:30.040+02:00");
        assertParse(parser, false, "10.5+02:00");
        
        assertParse(parser, true, "00:00:10.512345678");
        // result is offset by London DST in 1970-01-01
        assertEquals(10512, parser.parseMillis("00:00:10.512345678") + DateTimeZone.getDefault().getOffset(0L));
    }

    //-----------------------------------------------------------------------
    public void test_dateTimeParser() {
        DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();
        assertParse(parser, true, "2006-06-09");
        assertParse(parser, true, "2006-W27-3");
        assertParse(parser, true, "2006-123");
        assertParse(parser, true, "2006-06-09T+02:00");
        assertParse(parser, true, "2006-W27-3T+02:00");
        assertParse(parser, true, "2006-123T+02:00");
        
        assertParse(parser, true, "2006-06-09T10:20:30.040");
        assertParse(parser, true, "2006-W27-3T10:20:30.040");
        assertParse(parser, true, "2006-123T10:20:30.040");
        assertParse(parser, true, "2006-06-09T10:20:30.040+02:00");
        assertParse(parser, true, "2006-W27-3T10:20:30.040+02:00");
        assertParse(parser, true, "2006-123T10:20:30.040+02:00");
        
        assertParse(parser, true, "T10:20:30.040");
        assertParse(parser, true, "T10.5");
        assertParse(parser, true, "T10:20:30.040+02:00");
        assertParse(parser, true, "T10.5+02:00");
        
        assertParse(parser, false, "10:20:30.040");
        assertParse(parser, false, "10.5");
        assertParse(parser, false, "10:20:30.040+02:00");
        assertParse(parser, false, "10.5+02:00");
    }

    //-----------------------------------------------------------------------
    public void test_dateOptionalTimeParser() {
        DateTimeFormatter parser = ISODateTimeFormat.dateOptionalTimeParser();
        assertParse(parser, true, "2006-06-09");
        assertParse(parser, true, "2006-W27-3");
        assertParse(parser, true, "2006-123");
        assertParse(parser, true, "2006-06-09T+02:00");
        assertParse(parser, true, "2006-W27-3T+02:00");
        assertParse(parser, true, "2006-123T+02:00");
        
        assertParse(parser, true, "2006-06-09T10:20:30.040");
        assertParse(parser, true, "2006-W27-3T10:20:30.040");
        assertParse(parser, true, "2006-123T10:20:30.040");
        assertParse(parser, true, "2006-06-09T10:20:30.040+02:00");
        assertParse(parser, true, "2006-W27-3T10:20:30.040+02:00");
        assertParse(parser, true, "2006-123T10:20:30.040+02:00");
        
        assertParse(parser, false, "T10:20:30.040");
        assertParse(parser, false, "T10.5");
        assertParse(parser, false, "T10:20:30.040+02:00");
        assertParse(parser, false, "T10.5+02:00");
        
        assertParse(parser, false, "10:20:30.040");
        assertParse(parser, false, "10.5");
        assertParse(parser, false, "10:20:30.040+02:00");
        assertParse(parser, false, "10.5+02:00");
    }

    //-----------------------------------------------------------------------
    public void test_localDateOptionalTimeParser() {
        DateTimeFormatter parser = ISODateTimeFormat.localDateOptionalTimeParser();
        assertEquals(DateTimeZone.UTC, parser.getZone());
        assertParse(parser, true, "2006-06-09");
        assertParse(parser, true, "2006-W27-3");
        assertParse(parser, true, "2006-123");
        assertParse(parser, false, "2006-06-09T+02:00");
        assertParse(parser, false, "2006-W27-3T+02:00");
        assertParse(parser, false, "2006-123T+02:00");
        
        assertParse(parser, true, "2006-06-09T10:20:30.040");
        assertParse(parser, true, "2006-W27-3T10:20:30.040");
        assertParse(parser, true, "2006-123T10:20:30.040");
        assertParse(parser, false, "2006-06-09T10:20:30.040+02:00");
        assertParse(parser, false, "2006-W27-3T10:20:30.040+02:00");
        assertParse(parser, false, "2006-123T10:20:30.040+02:00");
        
        assertParse(parser, false, "T10:20:30.040");
        assertParse(parser, false, "T10.5");
        assertParse(parser, false, "T10:20:30.040+02:00");
        assertParse(parser, false, "T10.5+02:00");
        
        assertParse(parser, false, "10:20:30.040");
        assertParse(parser, false, "10.5");
        assertParse(parser, false, "10:20:30.040+02:00");
        assertParse(parser, false, "10.5+02:00");
    }

    //-----------------------------------------------------------------------
    private void assertParse(DateTimeFormatter parser, boolean expected, String str) {
        if (expected) {
            parser.parseMillis(str);
        } else {
            try {
                parser.parseMillis(str);
                fail();
            } catch (IllegalArgumentException ex) {
                // expected
            }
        }
    }

}
