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
package org.joda.time;

import junit.framework.Assert;
import org.joda.time.gwt.JodaGwtTestCase;
import static org.joda.time.gwt.TestConstants.*;
//import junit.framework.TestSuite;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Unit test the parsing of ISO format datetimes
 *
 * @author Guy Allard
 * @author Stephen Colebourne
 */
public class TestParseISO extends JodaGwtTestCase {
    
    private static final int DEFAULT = 99999;

    /**
     * This is the main class for this test suite.
     * @param args command line arguments.
     */
    /* Removed for GWT public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    } */
    
    /**
     * TestSuite suite() is a junit required method.
     * @see org.joda.test.time.BulkTest
     */
    /* Removed for GWT public static TestSuite suite() {
        return new TestSuite(TestParseISO.class);
    } */
    
    /**
     * Constructor.
     * @param name
     */
    /* Removed for GWT public TestParseISO(String name) {
        super(name);
    } */

    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();
        super.setUp();
    }
    
    protected void gwtTearDown() throws Exception {
        super.gwtTearDown();
        super.tearDown();
    }
    
    //-----------------------------------------------------------------------
    // Dates
    //-----------------------------------------------------------------------
    public void testSpecCompleteDate() {
        new DMatcher("5.2.1.1", "1999-10-20", "19991020",
            19, 99, 10, 20, DEFAULT).run();
    }        
    //-----------------------------------------------------------------------
    public void testSpecReducedPrecisionCYM() {
        new DMatcher("5.2.1.2", "1999-10", "199910",
            19, 99, 10, DEFAULT, DEFAULT).run();
    }
    public void testSpecReducedPrecisionCY() {
        new DMatcher("5.2.1.2", "1999", "1999",
            19, 99, DEFAULT, DEFAULT, DEFAULT).run();
    }
    public void testSpecReducedPrecisionC() {
        new DMatcher("5.2.1.2", "20", "20",
            20, DEFAULT, DEFAULT, DEFAULT, DEFAULT).run();
        new DMatcher("5.2.1.2", "19", "19",
            19, DEFAULT, DEFAULT, DEFAULT, DEFAULT).run();
    }
    //-----------------------------------------------------------------------
    public void testSpecTruncatedYMD() {
        new DMatcher("5.2.1.3", "85-04-11", "850411",
            DEFAULT, 85, 4, 11, DEFAULT).run();
    }
    public void testSpecTruncatedYM() {
        new DMatcher("5.2.1.3", "-85-04", "-8504",
            DEFAULT, 85, 4, DEFAULT, DEFAULT).run();
    }
    public void testSpecTruncatedY() {
        new DMatcher("5.2.1.3", "-85", "-85",
            DEFAULT, 85, DEFAULT, DEFAULT, DEFAULT).run();
    }
    public void testSpecTruncatedMD() {
        new DMatcher("5.2.1.3", "--04-11", "--0411",
            DEFAULT, DEFAULT, 4, 11, DEFAULT).run();
    }
    public void testSpecTruncatedM() {
        new DMatcher("5.2.1.3", "--04", "--04",
            DEFAULT, DEFAULT, 4, DEFAULT, DEFAULT).run();
    }
    public void testSpecTruncatedD() {
        new DMatcher("5.2.1.3", "---11", "---11",
            DEFAULT, DEFAULT, DEFAULT, 11, DEFAULT).run();
    }
    //-----------------------------------------------------------------------
    public void testSpecExpandedCYMD() {
        new DMatcher("5.2.1.4", "+001985-04-11", "+0019850411",
            19, 85, 4, 11, DEFAULT).run();
    }
    public void testSpecExpandedCYM() {
        new DMatcher("5.2.1.4", "+001985-04", "+00198504",
            19, 85, 4, DEFAULT, DEFAULT).run();
    }
    public void testSpecExpandedCY() {
        new DMatcher("5.2.1.4", "+001985", "+001985",
            19, 85, DEFAULT, DEFAULT, DEFAULT).run();
    }
    public void testSpecExpandedC() {
        // Not supported - could only tell difference from CY if you knew
        // number of digits representing year
//        new DMatcher("5.2.1.4", "+0019", "+0019",
//            19, DEFAULT, DEFAULT, DEFAULT, DEFAULT).assert();
    }
    
    //-----------------------------------------------------------------------
    // Ordinal based date
    //-----------------------------------------------------------------------
    public void testSpecOrdinalComplete() {
        new DMatcher("5.2.2.1", "1985-101", "1985101",
            19, 85, 4, 11, DEFAULT).run();
        new DMatcher("5.2.2.1", "1985-021", "1985021",
            19, 85, 1, 21, DEFAULT).run();
        new DMatcher("5.2.2.1", "1985-006", "1985006",
            19, 85, 1, 6, DEFAULT).run();
    }
    //-----------------------------------------------------------------------
    public void testSpecOrdinalTruncatedYD() {
        new DMatcher("5.2.2.2", "85-101", "85101",
            DEFAULT, 85, 4, 11, DEFAULT).run();
    }
    public void testSpecOrdinalTruncatedD() {
        new DMatcher("5.2.2.2", "-101", "-101",
            DEFAULT, DEFAULT, 4, 11, DEFAULT).run();
    }
    //-----------------------------------------------------------------------
    public void testSpecOrdinalExpandedYD() {
        new DMatcher("5.2.2.3", "+001985-101", "+001985101",
            19, 85, 4, 11, DEFAULT).run();
    }
    //-----------------------------------------------------------------------
    // Week based date
    //-----------------------------------------------------------------------
    public void testSpecWeekComplete() {
        new DMatcher("5.2.3.1", "1985-W15-1", "1985W151",
            19, 85, 4, 8, DEFAULT).run();
        new DMatcher("5.2.3.1", "1985-W15-2", "1985W152",
            19, 85, 4, 9, DEFAULT).run();
        new DMatcher("5.2.3.1", "1985-W15-3", "1985W153",
            19, 85, 4, 10, DEFAULT).run();
        new DMatcher("5.2.3.1", "1985-W15-4", "1985W154",
            19, 85, 4, 11, DEFAULT).run();
        new DMatcher("5.2.3.1", "1985-W15-5", "1985W155",
            19, 85, 4, 12, DEFAULT).run();
        new DMatcher("5.2.3.1", "1985-W15-6", "1985W156",
            19, 85, 4, 13, DEFAULT).run();
        new DMatcher("5.2.3.1", "1985-W15-7", "1985W157",
            19, 85, 4, 14, DEFAULT).run();
    }
    //-----------------------------------------------------------------------
    public void testSpecWeekReducedPrecision() {
        // test date is Sunday, which should be left alone
        new DMatcher("5.2.3.2", "1985-W15", "1985W15",
            19, 85, 4, 14, DEFAULT).run();
    }
    //-----------------------------------------------------------------------
    public void testSpecWeekTruncatedYWD() {
        new DMatcher("5.2.3.2", "85-W154", "85W154",
            DEFAULT, 85, 4, 11, DEFAULT).run();
    }
    public void testSpecWeekTruncatedYW() {
        // test date is Sunday, which should be left alone
        new DMatcher("5.2.3.2", "85-W15", "85W15",
            DEFAULT, 85, 4, 14, DEFAULT).run();
    }
    public void testSpecWeekTruncatedDWD() {
        // decade not supported
    }
    public void testSpecWeekTruncatedDW() {
        // decade not supported
    }
    public void testSpecWeekTruncatedWD() {
        new DMatcher("5.2.3.2", "-W154", "-W154",
            DEFAULT, DEFAULT, 4, 11, DEFAULT).run();
    }
    public void testSpecWeekTruncatedW() {
        // test date is Sunday, which should be left alone
        new DMatcher("5.2.3.2", "-W15", "-W15",
            DEFAULT, DEFAULT, 4, 14, DEFAULT).run();
    }
    public void testSpecWeekTruncatedD() {
        // test date is Sunday 3rd Dec, thus relative Thursday is 30th Nov
        new DMatcher("5.2.3.3", "-W-4", "-W-4",
            DEFAULT, DEFAULT, 11, 30, DEFAULT).run();
    }
    public void testSpecWeekExpandedYWD() {
        // test date is Sunday 3rd Dec, thus relative Thursday is 30th Nov
        new DMatcher("5.2.3.4", "+001985-W15-4", "+001985W154",
            19, 85, 4, 11, DEFAULT).run();
    }

    //-----------------------------------------------------------------------
    // Times
    //-----------------------------------------------------------------------
    public void testSpecTimeComplete() {
        new TMatcher("5.3.1.1", "23:20:50", "232050",
            23, 20, 50, 0, DEFAULT).run();
    }
    //-----------------------------------------------------------------------
    public void testSpecTimeReducedPrecisionHM() {
        new TMatcher("5.3.1.2", "23:20", "2320",
            23, 20, DEFAULT, DEFAULT, DEFAULT).run();
    }
    public void testSpecTimeReducedPrecisionH() {
        new TMatcher("5.3.1.2", "23", "23",
            23, DEFAULT, DEFAULT, DEFAULT, DEFAULT).run();
    }
    //-----------------------------------------------------------------------
    public void testSpecTimeFractionalHMS() {
        new TMatcher("5.3.1.3", "23:20:50.607", "232050.607",
            23, 20, 50, 607, DEFAULT).run();
        new TMatcher("5.3.1.3", "23:20:50,607", "232050,607",
            23, 20, 50, 607, DEFAULT).run();
    }
    public void testSpecTimeFractionalHM() {
        new TMatcher("5.3.1.3", "23:20.4", "2320.4",
            23, 20, 24, 0, DEFAULT).run();
        new TMatcher("5.3.1.3", "23:20,4", "2320,4",
            23, 20, 24, 0, DEFAULT).run();
    }
    public void testSpecTimeFractionalH() {
        new TMatcher("5.3.1.3", "23.25", "23.25",
            23, 15, 0, 0, DEFAULT).run();
        new TMatcher("5.3.1.3", "23.25", "23,25",
            23, 15, 0, 0, DEFAULT).run();
    }
    //-----------------------------------------------------------------------
    public void testSpecTimeTruncatedMS() {
        new TMatcher("5.3.1.4", "-20:50", "-2050",
            DEFAULT, 20, 50, 0, DEFAULT).run();
    }
    public void testSpecTimeTruncatedM() {
        new TMatcher("5.3.1.4", "-20", "-20",
            DEFAULT, 20, DEFAULT, DEFAULT, DEFAULT).run();
    }
    public void testSpecTimeTruncatedS() {
        new TMatcher("5.3.1.4", "--50", "--50",
            DEFAULT, DEFAULT, 50, 0, DEFAULT).run();
    }
    public void testSpecTimeTruncatedFractionMS() {
        new TMatcher("5.3.1.4", "-20:50.607", "-2050.607",
            DEFAULT, 20, 50, 607, DEFAULT).run();
    }
    public void testSpecTimeTruncatedFractionM() {
        new TMatcher("5.3.1.4", "-20.4", "-20.4",
            DEFAULT, 20, 24, 0, DEFAULT).run();
    }
    public void testSpecTimeTruncatedFractionS() {
        new TMatcher("5.3.1.4", "--50.607", "--50.607",
            DEFAULT, DEFAULT, 50, 607, DEFAULT).run();
    }
    
    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
    /**
     * Perform test.
     */        
    protected static abstract class Matcher extends Assert {
        String spec, extended, basic;
        int century, yearOfCentury, monthOfYear, dayOfMonth, hour, min, sec, milli, zone;
        MutableDateTime dt;
        
        protected Matcher(String spec, String extended, String basic) {
            this.spec = spec;
            this.extended = extended;
            this.basic = basic;
        }
        
        protected abstract void run();
        protected void assertDate() {
            String msg = "\nSpec:   " + spec + "\nParsed: " + extended + "\nTo:     " + dt;
            assertEquals(msg + "\nCentury: ", century, dt.getCenturyOfEra());
            assertEquals(msg + "\nYear: ", yearOfCentury, dt.getYearOfCentury());
            assertEquals(msg + "\nMonth: ", monthOfYear, dt.getMonthOfYear());
            assertEquals(msg + "\nDay: ", dayOfMonth, dt.getDayOfMonth());
            assertEquals(msg + "\nHour: ", hour, dt.getHourOfDay());
            assertEquals(msg + "\nMinute: ", min, dt.getMinuteOfHour());
            assertEquals(msg + "\nSecond: ", sec, dt.getSecondOfMinute());
            assertEquals(msg + "\nMilli: ", milli, dt.getMillisOfSecond());
            DateTimeZone z;
            if (zone == DEFAULT) {
                z = DateTimeZone.getDefault();
            } else if (zone == 0) {
                    z = DateTimeZone.UTC;
            } else {
                String str = "0" + Math.abs(zone) + ":00";
                str = str.substring(str.length() - 4);
                str = (zone < 0 ? "-" : "+") + str;
                z = DateTimeZone.forID(str);
            }
            assertEquals(msg + "\nZone: ", z, dt.getZone());
        }
        protected void parse(DateTimeFormatter p) {
            int result = p.parseInto(dt, extended, 0);
            assertTrue("\nSpec:   " + spec + "\nParsed: " + extended + "\nTo:     "
                + dt + "\nParse failed at: " + ~result,
                result >= 0);
        }
    }
    protected static class DTMatcher extends Matcher {
        protected DTMatcher(String spec, String extended, String basic,
                int century, int yearOfCentury, int monthOfYear, int dayOfMonth,
                int hour, int min, int sec, int milli, int zone) {
            super(spec, extended, basic);
            this.century = (century == DEFAULT ? 19 : century);
            this.yearOfCentury = (yearOfCentury == DEFAULT ? 72 : yearOfCentury);
            this.monthOfYear = (monthOfYear == DEFAULT ? 12 : monthOfYear);
            this.dayOfMonth = (dayOfMonth == DEFAULT ? 3 : dayOfMonth);
            this.hour = (hour == DEFAULT ? 10 : hour);
            this.min = (min == DEFAULT ? 32 : min);
            this.sec = (sec == DEFAULT ? 40 : sec);
            this.milli = (milli == DEFAULT ? 205 : milli);
            this.zone = zone;
        }            
        protected void run() {
            dt = new MutableDateTime(1972, 12, 3, 10, 32, 40, 205);
            parse(ISODateTimeFormat.dateTimeParser());
            super.assertDate();
        }
    }
    protected static class DMatcher extends Matcher {
        protected DMatcher(String spec, String extended, String basic,
                int century, int yearOfCentury, int monthOfYear, int dayOfMonth, int zone) {
            super(spec, extended, basic);
            this.century = (century == DEFAULT ? 19 : century);
            this.yearOfCentury = (yearOfCentury == DEFAULT ? 72 : yearOfCentury);
            this.monthOfYear = (monthOfYear == DEFAULT ? 12 : monthOfYear);
            this.dayOfMonth = (dayOfMonth == DEFAULT ? 3 : dayOfMonth);
            this.hour = 10;
            this.min = 32;
            this.sec = 40;
            this.milli = 205;
            this.zone = zone;
        }
        protected void run() {
            dt = new MutableDateTime(1972, 12, 3, 10, 32, 40, 205);
            parse(ISODateTimeFormat.dateParser());
            super.assertDate();
        
            dt = new MutableDateTime(1972, 12, 3, 10, 32, 40, 205);
            parse(ISODateTimeFormat.dateTimeParser());
            super.assertDate();
        }
    }
    protected static class TMatcher extends Matcher {
        protected TMatcher(String spec, String extended, String basic,
                int hour, int min, int sec, int milli, int zone) {
            super(spec, extended, basic);
            this.century = 19;
            this.yearOfCentury = 72;
            this.monthOfYear = 12;
            this.dayOfMonth = 3;
            this.hour = (hour == DEFAULT ? 10 : hour);
            this.min = (min == DEFAULT ? 32 : min);
            this.sec = (sec == DEFAULT ? 40 : sec);
            this.milli = (milli == DEFAULT ? 205 : milli);
            this.zone = zone;
        }
        protected void run() {
            dt = new MutableDateTime(1972, 12, 3, 10, 32, 40, 205);
            parse(ISODateTimeFormat.timeParser());
            super.assertDate();
            
            extended = "T" + extended;
            dt = new MutableDateTime(1972, 12, 3, 10, 32, 40, 205);
            parse(ISODateTimeFormat.timeParser());
            super.assertDate();
            
            dt = new MutableDateTime(1972, 12, 3, 10, 32, 40, 205);
            parse(ISODateTimeFormat.dateTimeParser());
            super.assertDate();
        }
    }
}
