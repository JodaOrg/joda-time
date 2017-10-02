/*
 *  Copyright 2001-2014 Stephen Colebourne
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

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This class is a Junit unit test for PeriodFormatterBuilder.
 *
 * @author Stephen Colebourne
 */
public class TestPeriodFormatterBuilder extends TestCase {
    
    private static final Period PERIOD = new Period(1, 2, 3, 4, 5, 6, 7, 8);
    private static final Period EMPTY_PERIOD = new Period(0, 0, 0, 0, 0, 0, 0, 0);
    private static final Period YEAR_DAY_PERIOD = new Period(1, 0, 0, 4, 5, 6, 7, 8, PeriodType.yearDayTime());
    private static final Period EMPTY_YEAR_DAY_PERIOD = new Period(0, 0, 0, 0, 0, 0, 0, 0, PeriodType.yearDayTime());
    private static final Period TIME_PERIOD = new Period(0, 0, 0, 0, 5, 6, 7, 8);
    private static final Period DATE_PERIOD = new Period(1, 2, 3, 4, 0, 0, 0, 0);
    private static final String NULL_STRING = null;
    private static final String[] NULL_STRING_ARRAY = null;

    //private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    //private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                     366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                     365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                     366 + 365;
    // 2002-06-09
    private long TEST_TIME_NOW =
            (y2002days + 31L + 28L + 31L + 30L + 31L + 9L -1L) * DateTimeConstants.MILLIS_PER_DAY;

    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;
    
    private PeriodFormatterBuilder builder;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestPeriodFormatterBuilder.class);
    }

    public TestPeriodFormatterBuilder(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
        
        builder = new PeriodFormatterBuilder();
    }

    @Override
    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    //-----------------------------------------------------------------------
    public void testToFormatterPrinterParser() {
        builder.appendYears();
        assertNotNull(builder.toFormatter());
        assertNotNull(builder.toPrinter());
        assertNotNull(builder.toParser());
    }

    //-----------------------------------------------------------------------
    public void testFormatYears() {
        PeriodFormatter f = builder.appendYears().toFormatter();
        assertEquals("1", f.print(PERIOD));
        assertEquals(1, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("0", f.print(p));
        assertEquals(1, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    public void testFormatMonths() {
        PeriodFormatter f = builder.appendMonths().toFormatter();
        assertEquals("2", f.print(PERIOD));
        assertEquals(1, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("0", f.print(p));
        assertEquals(1, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    public void testFormatWeeks() {
        PeriodFormatter f = builder.appendWeeks().toFormatter();
        assertEquals("3", f.print(PERIOD));
        assertEquals(1, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("0", f.print(p));
        assertEquals(1, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    public void testFormatDays() {
        PeriodFormatter f = builder.appendDays().toFormatter();
        assertEquals("4", f.print(PERIOD));
        assertEquals(1, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("0", f.print(p));
        assertEquals(1, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    public void testFormatHours() {
        PeriodFormatter f = builder.appendHours().toFormatter();
        assertEquals("5", f.print(PERIOD));
        assertEquals(1, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("0", f.print(p));
        assertEquals(1, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    public void testFormatMinutes() {
        PeriodFormatter f = builder.appendMinutes().toFormatter();
        assertEquals("6", f.print(PERIOD));
        assertEquals(1, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("0", f.print(p));
        assertEquals(1, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    public void testFormatSeconds() {
        PeriodFormatter f = builder.appendSeconds().toFormatter();
        assertEquals("7", f.print(PERIOD));
        assertEquals(1, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("0", f.print(p));
        assertEquals(1, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    public void testFormatSecondsWithMillis() {
        PeriodFormatter f = builder.appendSecondsWithMillis().toFormatter();
        Period p = new Period(0, 0, 0, 0, 0, 0, 7, 0);
        assertEquals("7.000", f.print(p));
        assertEquals(5, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
        
        p = new Period(0, 0, 0, 0, 0, 0, 7, 1);
        assertEquals("7.001", f.print(p));
        assertEquals(5, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
        
        p = new Period(0, 0, 0, 0, 0, 0, 7, 999);
        assertEquals("7.999", f.print(p));
        assertEquals(5, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
        
        p = new Period(0, 0, 0, 0, 0, 0, 7, 1000);
        assertEquals("8.000", f.print(p));
        assertEquals(5, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
        
        p = new Period(0, 0, 0, 0, 0, 0, 7, 1001);
        assertEquals("8.001", f.print(p));
        assertEquals(5, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
        
        p = new Period(0, 0, 0, 0, 0, 0, 7, -1);
        assertEquals("6.999", f.print(p));
        assertEquals(5, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
        
        p = new Period(0, 0, 0, 0, 0, 0, -7, 1);
        assertEquals("-6.999", f.print(p));
        assertEquals(6, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
        
        p = new Period(0, 0, 0, 0, 0, 0, -7, -1);
        assertEquals("-7.001", f.print(p));
        assertEquals(6, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
        
        p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("0.000", f.print(p));
        assertEquals(5, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    public void testFormatSecondsWithOptionalMillis() {
        PeriodFormatter f = builder.appendSecondsWithOptionalMillis().toFormatter();
        Period p = new Period(0, 0, 0, 0, 0, 0, 7, 0);
        assertEquals("7", f.print(p));
        assertEquals(1, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
        
        p = new Period(0, 0, 0, 0, 0, 0, 7, 1);
        assertEquals("7.001", f.print(p));
        assertEquals(5, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
        
        p = new Period(0, 0, 0, 0, 0, 0, 7, 999);
        assertEquals("7.999", f.print(p));
        assertEquals(5, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
        
        p = new Period(0, 0, 0, 0, 0, 0, 7, 1000);
        assertEquals("8", f.print(p));
        assertEquals(1, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
        
        p = new Period(0, 0, 0, 0, 0, 0, 7, 1001);
        assertEquals("8.001", f.print(p));
        assertEquals(5, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
        
        p = new Period(0, 0, 0, 0, 0, 0, 7, -1);
        assertEquals("6.999", f.print(p));
        assertEquals(5, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
        
        p = new Period(0, 0, 0, 0, 0, 0, -7, 1);
        assertEquals("-6.999", f.print(p));
        assertEquals(6, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
        
        p = new Period(0, 0, 0, 0, 0, 0, -7, -1);
        assertEquals("-7.001", f.print(p));
        assertEquals(6, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
        
        p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("0", f.print(p));
        assertEquals(1, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    public void testFormatMillis() {
        PeriodFormatter f = builder.appendMillis().toFormatter();
        assertEquals("8", f.print(PERIOD));
        assertEquals(1, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("0", f.print(p));
        assertEquals(1, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    public void testFormatMillis3Digit() {
        PeriodFormatter f = builder.appendMillis3Digit().toFormatter();
        assertEquals("008", f.print(PERIOD));
        assertEquals(3, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("000", f.print(p));
        assertEquals(3, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    //-----------------------------------------------------------------------
    public void testFormatPrefixSimple1() {
        PeriodFormatter f = builder.appendPrefix("Years:").appendYears().toFormatter();
        assertEquals("Years:1", f.print(PERIOD));
        assertEquals(7, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("Years:0", f.print(p));
        assertEquals(7, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    public void testFormatPrefixSimple2() {
        PeriodFormatter f = builder.appendPrefix("Hours:").appendHours().toFormatter();
        assertEquals("Hours:5", f.print(PERIOD));
        assertEquals(7, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("Hours:0", f.print(p));
        assertEquals(7, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    public void testFormatPrefixSimple3() {
        try {
            builder.appendPrefix(null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testFormatPrefixSimple4IgnoringPrefix() {
        PeriodFormatter f = builder.appendPrefix("m").appendMinutes()
                .appendSeparator(" ").appendPrefix("ms").appendMillis().toFormatter();
        String oneMS = Period.millis(1).toString(f);
        assertEquals("ms1", oneMS);
        Period period = f.parsePeriod(oneMS);
        assertEquals(Period.millis(1), period);
        period = f.parsePeriod(oneMS.toUpperCase(Locale.ENGLISH));
        assertEquals(Period.millis(1), period);
    }

    public void testPluralAffixParseOrder() {
        PeriodFormatter f = builder.appendDays().appendSuffix("day", "days").toFormatter();
        String twoDays = Period.days(2).toString(f);
        Period period = f.parsePeriod(twoDays);
        assertEquals(Period.days(2), period);
        period = f.parsePeriod(twoDays.toUpperCase(Locale.ENGLISH));
        assertEquals(Period.days(2), period);
    }

    public void testFormatPrefixPlural1() {
        PeriodFormatter f = builder.appendPrefix("Year:", "Years:").appendYears().toFormatter();
        assertEquals("Year:1", f.print(PERIOD));
        assertEquals(6, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("Years:0", f.print(p));
        assertEquals(7, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    public void testFormatPrefixPlural2() {
        PeriodFormatter f = builder.appendPrefix("Hour:", "Hours:").appendHours().toFormatter();
        assertEquals("Hours:5", f.print(PERIOD));
        assertEquals(7, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("Hours:0", f.print(p));
        assertEquals(7, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    public void testFormatPrefixPlural3() {
        try {
            builder.appendPrefix(null, "");
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            builder.appendPrefix("", null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            builder.appendPrefix(NULL_STRING, null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    public void testFormatPrefixPlural4IgnoringPrefix() {
        PeriodFormatter f = builder.appendPrefix("m", "ms").appendMinutes()
                .appendSeparator(" ").appendPrefix("mss", "msss").appendMillis().toFormatter();
        String oneMS = Period.millis(1).toString(f);
        assertEquals("mss1", oneMS);
        Period period = f.parsePeriod(oneMS);
        assertEquals(Period.millis(1), period);
        period = f.parsePeriod(oneMS.toUpperCase(Locale.ENGLISH));
        assertEquals(Period.millis(1), period);
        
        String twoMS = Period.millis(2).toString(f);
        assertEquals("msss2", twoMS);
        Period period2 = f.parsePeriod(twoMS);
        assertEquals(Period.millis(2), period2);
        period = f.parsePeriod(twoMS.toUpperCase(Locale.ENGLISH));
        assertEquals(Period.millis(2), period2);
    }
    
    public void testRegExAffixParseOrder() {
        PeriodFormatter f = builder.appendDays()
                .appendSuffix(new String[]{"^1$","[0-9]*"}, new String[]{"day", "days"}).toFormatter();
        String twoDays = Period.days(2).toString(f);
        Period period = f.parsePeriod(twoDays);
        assertEquals(Period.days(2), period);
        period = f.parsePeriod(twoDays.toUpperCase(Locale.ENGLISH));
        assertEquals(Period.days(2), period);
    }

    public void testFormatPrefixRegEx1() {
        PeriodFormatter f = builder.appendPrefix(new String[] { "^1$", "^.*$" }, new String[] { "Year:", "Years:" }).appendYears()
                .toFormatter();
        assertEquals("Year:1", f.print(PERIOD));
        assertEquals(6, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));

        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("Years:0", f.print(p));
        assertEquals(7, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    public void testFormatPrefixRegEx2() {
        PeriodFormatter f = builder.appendPrefix(new String[] { "^1$", "^.*$" }, new String[] { "Hour:", "Hours:" }).appendHours()
                .toFormatter();
        assertEquals("Hours:5", f.print(PERIOD));
        assertEquals(7, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));

        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("Hours:0", f.print(p));
        assertEquals(7, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    public void testFormatPrefixRegEx3() {
        try {
            builder.appendPrefix(null, new String[0]);
            fail();
        } catch (IllegalArgumentException ex) {
        }
        try {
            builder.appendPrefix(new String[0], null);
            fail();
        } catch (IllegalArgumentException ex) {
        }
        try {
            builder.appendPrefix(NULL_STRING_ARRAY, null);
            fail();
        } catch (IllegalArgumentException ex) {
        }
        try {
            builder.appendPrefix(new String[0], new String[0]);
            fail();
        } catch (IllegalArgumentException ex) {
        }
        try {
            builder.appendPrefix(new String[1], new String[2]);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }

    public void testFormatPrefixRegEx4IgnoringPrefix() {
        PeriodFormatter f = builder
                .appendPrefix(new String[]{"^1$","[0-9]*"}, new String[]{"m", "ms"}).appendMinutes()
                .appendSeparator(" ")
                .appendPrefix(new String[]{"^1$","[0-9]*"}, new String[]{"mss", "msss"}).appendMillis()
                .toFormatter();
        String oneMS = Period.millis(1).toString(f);
        assertEquals("mss1", oneMS);
        Period period = f.parsePeriod(oneMS);
        assertEquals(Period.millis(1), period);
        period = f.parsePeriod(oneMS.toUpperCase(Locale.ENGLISH));
        assertEquals(Period.millis(1), period);
        
        String twoMS = Period.millis(2).toString(f);
        assertEquals("msss2", twoMS);
        Period period2 = f.parsePeriod(twoMS);
        assertEquals(Period.millis(2), period2);
        period = f.parsePeriod(twoMS.toUpperCase(Locale.ENGLISH));
        assertEquals(Period.millis(2), period2);
    }

    //-----------------------------------------------------------------------
    public void testFormatPrefixComposite1() {
        PeriodFormatter f = builder.appendPrefix("d")
                .appendPrefix("a", "ay")
                .appendPrefix(new String[] { "^1$", "^.*$" }, new String[] { "y:", "s:" })
                .appendDays().toFormatter();
        String oneMS = Period.days(2).toString(f);
        assertEquals("days:2", oneMS);
        Period period = f.parsePeriod(oneMS);
        assertEquals(Period.days(2), period);
        period = f.parsePeriod(oneMS.toUpperCase(Locale.ENGLISH));
        assertEquals(Period.days(2), period);
    }

    //-----------------------------------------------------------------------
    public void testFormatSuffixSimple1() {
        PeriodFormatter f = builder.appendYears().appendSuffix(" years").toFormatter();
        assertEquals("1 years", f.print(PERIOD));
        assertEquals(7, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("0 years", f.print(p));
        assertEquals(7, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    public void testFormatSuffixSimple2() {
        PeriodFormatter f = builder.appendHours().appendSuffix(" hours").toFormatter();
        assertEquals("5 hours", f.print(PERIOD));
        assertEquals(7, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("0 hours", f.print(p));
        assertEquals(7, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    public void testFormatSuffixSimple3() {
        try {
            builder.appendSuffix(null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testFormatSuffixSimple4() {
        try {
            builder.appendSuffix(" hours");
            fail();
        } catch (IllegalStateException ex) {}
    }
    
    public void testFormatPrefixSimple5IgnoringPrefix() {
        PeriodFormatter f = builder.appendMinutes().appendSuffix("m")
                .appendSeparator(" ").appendMillis().appendSuffix("ms").toFormatter();
        String oneMS = Period.millis(1).toString(f);
        assertEquals("1ms", oneMS);
        Period period = f.parsePeriod(oneMS);
        assertEquals(Period.millis(1), period);
        period = f.parsePeriod(oneMS.toUpperCase(Locale.ENGLISH));
        assertEquals(Period.millis(1), period);
    }

    public void testFormatPrefixSimple6IgnoringPrefix() {
        PeriodFormatter f = builder.appendMinutes().appendSuffix("M")
                .appendSeparator(" ").appendMillis().appendSuffix("ms").toFormatter();
        String oneMS = Period.millis(1).toString(f);
        assertEquals("1ms", oneMS);
        Period period = f.parsePeriod(oneMS);
        assertEquals(Period.millis(1), period);
        period = f.parsePeriod(oneMS.toUpperCase(Locale.ENGLISH));
        assertEquals(Period.millis(1), period);
    }

    public void testFormatSuffixPlural1() {
        PeriodFormatter f = builder.appendYears().appendSuffix(" year", " years").toFormatter();
        assertEquals("1 year", f.print(PERIOD));
        assertEquals(6, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("0 years", f.print(p));
        assertEquals(7, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    public void testFormatSuffixPlural2() {
        PeriodFormatter f = builder.appendHours().appendSuffix(" hour", " hours").toFormatter();
        assertEquals("5 hours", f.print(PERIOD));
        assertEquals(7, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("0 hours", f.print(p));
        assertEquals(7, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    public void testFormatSuffixPlural3() {
        try {
            builder.appendSuffix(null, "");
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            builder.appendSuffix("", null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            builder.appendSuffix(NULL_STRING, null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testFormatSuffixPlural4() {
        try {
            builder.appendSuffix(" hour", " hours");
            fail();
        } catch (IllegalStateException ex) {}
    }
    
    public void testFormatSuffixPlural4IgnoringPrefix() {
        PeriodFormatter f = builder.appendMinutes().appendSuffix("m", "ms")
                .appendSeparator(" ").appendMillis().appendSuffix("mss", "msss").toFormatter();
        String oneMS = Period.millis(1).toString(f);
        assertEquals("1mss", oneMS);
        Period period = f.parsePeriod(oneMS);
        assertEquals(Period.millis(1), period);
        period = f.parsePeriod(oneMS.toUpperCase(Locale.ENGLISH));
        assertEquals(Period.millis(1), period);
        
        String twoMS = Period.millis(2).toString(f);
        assertEquals("2msss", twoMS);
        Period period2 = f.parsePeriod(twoMS);
        assertEquals(Period.millis(2), period2);
        period = f.parsePeriod(twoMS.toUpperCase(Locale.ENGLISH));
        assertEquals(Period.millis(2), period);
    }

    public void testFormatSuffixRegEx1() {
        PeriodFormatter f = builder.appendYears().appendSuffix(new String[] { "^1$", "^2$" }, new String[] { " year", " years" })
                .toFormatter();
        assertEquals("1 year", f.print(PERIOD));
        assertEquals(6, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        Period p2 = new Period(2, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("2 years", f.print(p2));
        assertEquals(7, f.getPrinter().calculatePrintedLength(p2, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p2, Integer.MAX_VALUE, null));
        
        Period p0 = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("0 years", f.print(p0));
        assertEquals(7, f.getPrinter().calculatePrintedLength(p0, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p0, Integer.MAX_VALUE, null));
    }

    public void testFormatSuffixRegEx2() {
        PeriodFormatter f = builder.appendHours().appendSuffix(new String[] { "^1$", "^2$" }, new String[] { " hour", " hours" }).toFormatter();
        assertEquals("5 hours", f.print(PERIOD));
        assertEquals(7, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));

        Period p2 = new Period(0, 0, 0, 0, 2, 0, 0, 0);
        assertEquals("2 hours", f.print(p2));
        assertEquals(7, f.getPrinter().calculatePrintedLength(p2, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p2, Integer.MAX_VALUE, null));

        Period p0 = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("0 hours", f.print(p0));
        assertEquals(7, f.getPrinter().calculatePrintedLength(p0, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p0, Integer.MAX_VALUE, null));
    }

    public void testFormatSuffixRegEx3() {
        try {
            builder.appendSuffix(null, new String[0]);
            fail();
        } catch (IllegalArgumentException ex) {
        }
        try {
            builder.appendSuffix(new String[0], null);
            fail();
        } catch (IllegalArgumentException ex) {
        }
        try {
            builder.appendSuffix(NULL_STRING_ARRAY, null);
            fail();
        } catch (IllegalArgumentException ex) {
        }
        try {
            builder.appendSuffix(new String[0], new String[0]);
            fail();
        } catch (IllegalArgumentException ex) {
        }
        try {
            builder.appendSuffix(new String[1], new String[2]);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }

    public void testFormatSuffixRegEx4() {
        try {
            builder.appendSuffix(new String[] { "^1$", "^.*$" }, new String[] { " hour", " hours" });
            fail();
        } catch (IllegalStateException ex) {
        }
    }

    public void testFormatSuffixRegEx5IgnoringAffix() {
        PeriodFormatter f = builder
                .appendMinutes().appendSuffix(new String[]{"^1$","[0-9]*"}, new String[]{"m", "ms"})
                .appendSeparator(" ")
                .appendMillis().appendSuffix(new String[]{"^1$","[0-9]*"}, new String[]{"mss", "msss"})
                .toFormatter();
        String oneMS = Period.millis(1).toString(f);
        assertEquals("1mss", oneMS);
        Period period = f.parsePeriod(oneMS);
        assertEquals(Period.millis(1), period);
        period = f.parsePeriod(oneMS.toUpperCase(Locale.ENGLISH));
        assertEquals(Period.millis(1), period);
        
        String twoMS = Period.millis(2).toString(f);
        assertEquals("2msss", twoMS);
        Period period2 = f.parsePeriod(twoMS);
        assertEquals(Period.millis(2), period2);
        period = f.parsePeriod(twoMS.toUpperCase(Locale.ENGLISH));
        assertEquals(Period.millis(2), period);
    }
    //-----------------------------------------------------------------------
    public void testFormatSuffixComposite1() {
        PeriodFormatter f = builder.appendDays().appendSuffix("d")
                .appendSuffix("a", "ay")
                .appendSuffix(new String[] { "^1$", "^.*$" }, new String[] { "y", "s" })
                .toFormatter();
        String oneMS = Period.days(2).toString(f);
        assertEquals("2days", oneMS);
        Period period = f.parsePeriod(oneMS);
        assertEquals(Period.days(2), period);
        period = f.parsePeriod(oneMS.toUpperCase(Locale.ENGLISH));
        assertEquals(Period.days(2), period);
    }

    public void testFormatSuffixComposite5IgnoringAffix() {
        PeriodFormatter f = builder
                .appendMinutes().appendSuffix("m")
                .appendSeparator(" ")
                .appendMillis().appendSuffix("m").appendSuffix("s")
                .toFormatter();
        String oneMS = Period.millis(1).toString(f);
        assertEquals("1ms", oneMS);
        Period period = f.parsePeriod(oneMS);
        assertEquals(Period.millis(1), period);
        period = f.parsePeriod(oneMS.toUpperCase(Locale.ENGLISH));
        assertEquals(Period.millis(1), period);
    }
    
    //-----------------------------------------------------------------------
    public void testFormatPrefixSuffix() {
        PeriodFormatter f = builder.appendPrefix("P").appendYears().appendSuffix("Y").toFormatter();
        assertEquals("P1Y", f.print(PERIOD));
        assertEquals(3, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals("P0Y", f.print(p));
        assertEquals(3, f.getPrinter().calculatePrintedLength(p, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(p, Integer.MAX_VALUE, null));
    }

    //-----------------------------------------------------------------------
    public void testFormatSeparatorSimple() {
        PeriodFormatter f = builder.appendYears().appendSeparator("T").appendHours().toFormatter();
        assertEquals("1T5", f.print(PERIOD));
        assertEquals(3, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(2, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("5", f.print(TIME_PERIOD));
        assertEquals(1, f.getPrinter().calculatePrintedLength(TIME_PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(TIME_PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("1", f.print(DATE_PERIOD));
        assertEquals(1, f.getPrinter().calculatePrintedLength(DATE_PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(DATE_PERIOD, Integer.MAX_VALUE, null));
    }

    public void testFormatSeparatorComplex() {
        PeriodFormatter f = builder
            .appendYears().appendSeparator(", ", " and ")
            .appendHours().appendSeparator(", ", " and ")
            .appendMinutes().appendSeparator(", ", " and ")
            .toFormatter();
        assertEquals("1, 5 and 6", f.print(PERIOD));
        assertEquals(10, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(3, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("5 and 6", f.print(TIME_PERIOD));
        assertEquals(7, f.getPrinter().calculatePrintedLength(TIME_PERIOD, null));
        assertEquals(2, f.getPrinter().countFieldsToPrint(TIME_PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("1", f.print(DATE_PERIOD));
        assertEquals(1, f.getPrinter().calculatePrintedLength(DATE_PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(DATE_PERIOD, Integer.MAX_VALUE, null));
    }

    public void testFormatSeparatorIfFieldsAfter() {
        PeriodFormatter f = builder.appendYears().appendSeparatorIfFieldsAfter("T").appendHours().toFormatter();
        assertEquals("1T5", f.print(PERIOD));
        assertEquals(3, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(2, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("T5", f.print(TIME_PERIOD));
        assertEquals(2, f.getPrinter().calculatePrintedLength(TIME_PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(TIME_PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("1", f.print(DATE_PERIOD));
        assertEquals(1, f.getPrinter().calculatePrintedLength(DATE_PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(DATE_PERIOD, Integer.MAX_VALUE, null));
    }

    public void testFormatSeparatorIfFieldsBefore() {
        PeriodFormatter f = builder.appendYears().appendSeparatorIfFieldsBefore("T").appendHours().toFormatter();
        assertEquals("1T5", f.print(PERIOD));
        assertEquals(3, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(2, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("5", f.print(TIME_PERIOD));
        assertEquals(1, f.getPrinter().calculatePrintedLength(TIME_PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(TIME_PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("1T", f.print(DATE_PERIOD));
        assertEquals(2, f.getPrinter().calculatePrintedLength(DATE_PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(DATE_PERIOD, Integer.MAX_VALUE, null));
    }

    //-----------------------------------------------------------------------
    public void testFormatLiteral() {
        PeriodFormatter f = builder.appendLiteral("HELLO").toFormatter();
        assertEquals("HELLO", f.print(PERIOD));
        assertEquals(5, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(0, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
    }

    public void testFormatAppendFormatter() {
        PeriodFormatter base = builder.appendYears().appendLiteral("-").toFormatter();
        PeriodFormatter f = new PeriodFormatterBuilder().append(base).appendYears().toFormatter();
        assertEquals("1-1", f.print(PERIOD));
        assertEquals(3, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(2, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
    }

    public void testFormatMinDigits() {
        PeriodFormatter f = new PeriodFormatterBuilder().minimumPrintedDigits(4).appendYears().toFormatter();
        assertEquals("0001", f.print(PERIOD));
        assertEquals(4, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
    }

    //-----------------------------------------------------------------------
    public void testFormatPrintZeroDefault() {
        PeriodFormatter f =
            new PeriodFormatterBuilder()
                .appendYears().appendLiteral("-")
                .appendMonths().appendLiteral("-")
                .appendWeeks().appendLiteral("-")
                .appendDays().toFormatter();
        assertEquals("1-2-3-4", f.print(PERIOD));
        assertEquals(7, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(4, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("---0", f.print(EMPTY_YEAR_DAY_PERIOD));
        assertEquals(4, f.getPrinter().calculatePrintedLength(EMPTY_YEAR_DAY_PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(EMPTY_YEAR_DAY_PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("1---4", f.print(YEAR_DAY_PERIOD));
        assertEquals(5, f.getPrinter().calculatePrintedLength(YEAR_DAY_PERIOD, null));
        assertEquals(2, f.getPrinter().countFieldsToPrint(YEAR_DAY_PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("---0", f.print(EMPTY_PERIOD));
        assertEquals(4, f.getPrinter().calculatePrintedLength(EMPTY_PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(EMPTY_PERIOD, Integer.MAX_VALUE, null));
        
        // test only last instance of same field is output
        f = new PeriodFormatterBuilder()
                .appendYears().appendLiteral("-")
                .appendYears().toFormatter();
        assertEquals("-0", f.print(EMPTY_PERIOD));
        assertEquals(2, f.getPrinter().calculatePrintedLength(EMPTY_PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(EMPTY_PERIOD, Integer.MAX_VALUE, null));
    }

    public void testFormatPrintZeroRarelyLast() {
        PeriodFormatter f =
            new PeriodFormatterBuilder()
                .printZeroRarelyLast()
                .appendYears().appendLiteral("-")
                .appendMonths().appendLiteral("-")
                .appendWeeks().appendLiteral("-")
                .appendDays().toFormatter();
        assertEquals("1-2-3-4", f.print(PERIOD));
        assertEquals(7, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(4, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("---0", f.print(EMPTY_YEAR_DAY_PERIOD));
        assertEquals(4, f.getPrinter().calculatePrintedLength(EMPTY_YEAR_DAY_PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(EMPTY_YEAR_DAY_PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("1---4", f.print(YEAR_DAY_PERIOD));
        assertEquals(5, f.getPrinter().calculatePrintedLength(YEAR_DAY_PERIOD, null));
        assertEquals(2, f.getPrinter().countFieldsToPrint(YEAR_DAY_PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("---0", f.print(EMPTY_PERIOD));
        assertEquals(4, f.getPrinter().calculatePrintedLength(EMPTY_PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(EMPTY_PERIOD, Integer.MAX_VALUE, null));
    }

    public void testFormatPrintZeroRarelyFirst() {
        PeriodFormatter f =
            new PeriodFormatterBuilder()
                .printZeroRarelyFirst()
                .appendYears().appendLiteral("-")
                .appendMonths().appendLiteral("-")
                .appendWeeks().appendLiteral("-")
                .appendDays().toFormatter();
        assertEquals("1-2-3-4", f.print(PERIOD));
        assertEquals(7, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(4, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("0---", f.print(EMPTY_YEAR_DAY_PERIOD));
        assertEquals(4, f.getPrinter().calculatePrintedLength(EMPTY_YEAR_DAY_PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(EMPTY_YEAR_DAY_PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("1---4", f.print(YEAR_DAY_PERIOD));
        assertEquals(5, f.getPrinter().calculatePrintedLength(YEAR_DAY_PERIOD, null));
        assertEquals(2, f.getPrinter().countFieldsToPrint(YEAR_DAY_PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("0---", f.print(EMPTY_PERIOD));
        assertEquals(4, f.getPrinter().calculatePrintedLength(EMPTY_PERIOD, null));
        assertEquals(1, f.getPrinter().countFieldsToPrint(EMPTY_PERIOD, Integer.MAX_VALUE, null));
    }

    public void testFormatPrintZeroRarelyFirstYears() {
        PeriodFormatter f = new PeriodFormatterBuilder()
            .printZeroRarelyFirst()
            .appendYears().toFormatter();
        assertEquals("0", f.print(EMPTY_PERIOD));
    }

    public void testFormatPrintZeroRarelyFirstMonths() {
        PeriodFormatter f = new PeriodFormatterBuilder()
            .printZeroRarelyFirst()
            .appendMonths().toFormatter();
        assertEquals("0", f.print(EMPTY_PERIOD));
    }

    public void testFormatPrintZeroRarelyFirstWeeks() {
        PeriodFormatter f = new PeriodFormatterBuilder()
            .printZeroRarelyFirst()
            .appendWeeks().toFormatter();
        assertEquals("0", f.print(EMPTY_PERIOD));
    }

    public void testFormatPrintZeroRarelyFirstDays() {
        PeriodFormatter f = new PeriodFormatterBuilder()
            .printZeroRarelyFirst()
            .appendDays().toFormatter();
        assertEquals("0", f.print(EMPTY_PERIOD));
    }

    public void testFormatPrintZeroRarelyFirstHours() {
        PeriodFormatter f = new PeriodFormatterBuilder()
            .printZeroRarelyFirst()
            .appendHours().toFormatter();
        assertEquals("0", f.print(EMPTY_PERIOD));
    }

    public void testFormatPrintZeroRarelyFirstMinutes() {
        PeriodFormatter f = new PeriodFormatterBuilder()
            .printZeroRarelyFirst()
            .appendMinutes().toFormatter();
        assertEquals("0", f.print(EMPTY_PERIOD));
    }

    public void testFormatPrintZeroRarelyFirstSeconds() {
        PeriodFormatter f = new PeriodFormatterBuilder()
            .printZeroRarelyFirst()
            .appendSeconds().toFormatter();
        assertEquals("0", f.print(EMPTY_PERIOD));
    }

    public void testFormatPrintZeroIfSupported() {
        PeriodFormatter f =
            new PeriodFormatterBuilder()
                .printZeroIfSupported()
                .appendYears().appendLiteral("-")
                .appendMonths().appendLiteral("-")
                .appendWeeks().appendLiteral("-")
                .appendDays().toFormatter();
        assertEquals("1-2-3-4", f.print(PERIOD));
        assertEquals(7, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(4, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("0---0", f.print(EMPTY_YEAR_DAY_PERIOD));
        assertEquals(5, f.getPrinter().calculatePrintedLength(EMPTY_YEAR_DAY_PERIOD, null));
        assertEquals(2, f.getPrinter().countFieldsToPrint(EMPTY_YEAR_DAY_PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("1---4", f.print(YEAR_DAY_PERIOD));
        assertEquals(5, f.getPrinter().calculatePrintedLength(YEAR_DAY_PERIOD, null));
        assertEquals(2, f.getPrinter().countFieldsToPrint(YEAR_DAY_PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("0-0-0-0", f.print(EMPTY_PERIOD));
        assertEquals(7, f.getPrinter().calculatePrintedLength(EMPTY_PERIOD, null));
        assertEquals(4, f.getPrinter().countFieldsToPrint(EMPTY_PERIOD, Integer.MAX_VALUE, null));
    }

    public void testFormatPrintZeroAlways() {
        PeriodFormatter f =
            new PeriodFormatterBuilder()
                .printZeroAlways()
                .appendYears().appendLiteral("-")
                .appendMonths().appendLiteral("-")
                .appendWeeks().appendLiteral("-")
                .appendDays().toFormatter();
        assertEquals("1-2-3-4", f.print(PERIOD));
        assertEquals(7, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(4, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("0-0-0-0", f.print(EMPTY_YEAR_DAY_PERIOD));
        assertEquals(7, f.getPrinter().calculatePrintedLength(EMPTY_YEAR_DAY_PERIOD, null));
        assertEquals(4, f.getPrinter().countFieldsToPrint(EMPTY_YEAR_DAY_PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("1-0-0-4", f.print(YEAR_DAY_PERIOD));
        assertEquals(7, f.getPrinter().calculatePrintedLength(YEAR_DAY_PERIOD, null));
        assertEquals(4, f.getPrinter().countFieldsToPrint(YEAR_DAY_PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("0-0-0-0", f.print(EMPTY_PERIOD));
        assertEquals(7, f.getPrinter().calculatePrintedLength(EMPTY_PERIOD, null));
        assertEquals(4, f.getPrinter().countFieldsToPrint(EMPTY_PERIOD, Integer.MAX_VALUE, null));
    }

    public void testFormatPrintZeroNever() {
        PeriodFormatter f =
            new PeriodFormatterBuilder()
                .printZeroNever()
                .appendYears().appendLiteral("-")
                .appendMonths().appendLiteral("-")
                .appendWeeks().appendLiteral("-")
                .appendDays().toFormatter();
        assertEquals("1-2-3-4", f.print(PERIOD));
        assertEquals(7, f.getPrinter().calculatePrintedLength(PERIOD, null));
        assertEquals(4, f.getPrinter().countFieldsToPrint(PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("---", f.print(EMPTY_YEAR_DAY_PERIOD));
        assertEquals(3, f.getPrinter().calculatePrintedLength(EMPTY_YEAR_DAY_PERIOD, null));
        assertEquals(0, f.getPrinter().countFieldsToPrint(EMPTY_YEAR_DAY_PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("1---4", f.print(YEAR_DAY_PERIOD));
        assertEquals(5, f.getPrinter().calculatePrintedLength(YEAR_DAY_PERIOD, null));
        assertEquals(2, f.getPrinter().countFieldsToPrint(YEAR_DAY_PERIOD, Integer.MAX_VALUE, null));
        
        assertEquals("---", f.print(EMPTY_PERIOD));
        assertEquals(3, f.getPrinter().calculatePrintedLength(EMPTY_PERIOD, null));
        assertEquals(0, f.getPrinter().countFieldsToPrint(EMPTY_PERIOD, Integer.MAX_VALUE, null));
    }

    //-----------------------------------------------------------------------
    public void testFormatAppend_PrinterParser_null_null() {
        try {
            new PeriodFormatterBuilder().append(null, null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testFormatAppend_PrinterParser_Printer_null() {
        PeriodPrinter printer = new PeriodFormatterBuilder().appendYears().appendLiteral("-").toPrinter();
        PeriodFormatterBuilder bld = new PeriodFormatterBuilder().append(printer, null).appendMonths();
        assertNotNull(bld.toPrinter());
        assertNull(bld.toParser());
        
        PeriodFormatter f = bld.toFormatter();
        assertEquals("1-2", f.print(PERIOD));
        try {
            f.parsePeriod("1-2");
            fail();
        } catch (UnsupportedOperationException ex) {}
    }

    public void testFormatAppend_PrinterParser_null_Parser() {
        PeriodParser parser = new PeriodFormatterBuilder().appendWeeks().appendLiteral("-").toParser();
        PeriodFormatterBuilder bld = new PeriodFormatterBuilder().append(null, parser).appendMonths();
        assertNull(bld.toPrinter());
        assertNotNull(bld.toParser());
        
        PeriodFormatter f = bld.toFormatter();
        try {
            f.print(PERIOD);
            fail();
        } catch (UnsupportedOperationException ex) {}
        assertEquals(new Period(0, 2, 1, 0, 0, 0, 0, 0), f.parsePeriod("1-2"));
    }

    public void testFormatAppend_PrinterParser_PrinterParser() {
        PeriodPrinter printer = new PeriodFormatterBuilder().appendYears().appendLiteral("-").toPrinter();
        PeriodParser parser = new PeriodFormatterBuilder().appendWeeks().appendLiteral("-").toParser();
        PeriodFormatterBuilder bld = new PeriodFormatterBuilder().append(printer, parser).appendMonths();
        assertNotNull(bld.toPrinter());
        assertNotNull(bld.toParser());
        
        PeriodFormatter f = bld.toFormatter();
        assertEquals("1-2", f.print(PERIOD));
        assertEquals(new Period(0, 2, 1, 0, 0, 0, 0, 0), f.parsePeriod("1-2"));
    }

    public void testFormatAppend_PrinterParser_Printer_null_null_Parser() {
        PeriodPrinter printer = new PeriodFormatterBuilder().appendYears().appendLiteral("-").toPrinter();
        PeriodParser parser = new PeriodFormatterBuilder().appendWeeks().appendLiteral("-").toParser();
        PeriodFormatterBuilder bld = new PeriodFormatterBuilder().append(printer, null).append(null, parser);
        assertNull(bld.toPrinter());
        assertNull(bld.toParser());
        
        try {
            bld.toFormatter();
            fail();
        } catch (IllegalStateException ex) {}
    }

    public void testFormatAppend_PrinterParserThenClear() {
        PeriodPrinter printer = new PeriodFormatterBuilder().appendYears().appendLiteral("-").toPrinter();
        PeriodParser parser = new PeriodFormatterBuilder().appendWeeks().appendLiteral("-").toParser();
        PeriodFormatterBuilder bld = new PeriodFormatterBuilder().append(printer, null).append(null, parser);
        assertNull(bld.toPrinter());
        assertNull(bld.toParser());
        bld.clear();
        bld.appendMonths();
        assertNotNull(bld.toPrinter());
        assertNotNull(bld.toParser());
    }

    public void testBug2495455() {
        PeriodFormatter pfmt1 = new PeriodFormatterBuilder()
            .appendLiteral("P")
            .appendYears()
            .appendSuffix("Y")
            .appendMonths()
            .appendSuffix("M")
            .appendWeeks()
            .appendSuffix("W")
            .appendDays()
            .appendSuffix("D")
            .appendSeparatorIfFieldsAfter("T")
            .appendHours()
            .appendSuffix("H")
            .appendMinutes()
            .appendSuffix("M")
            .appendSecondsWithOptionalMillis()
            .appendSuffix("S")
            .toFormatter();
        PeriodFormatter pfmt2 = new PeriodFormatterBuilder()
            .append(ISOPeriodFormat.standard())
            .toFormatter();
        pfmt1.parsePeriod("PT1003199059S");
        pfmt2.parsePeriod("PT1003199059S");
        pfmt2.parsePeriod("pt1003199059S");
    }

    public void testMonthsAndMinutesAreConsideredSeparateAndCaseIsNotIgnored() {
        PeriodFormatter formatter = builder
                .appendMonths().appendSuffix("M").appendSeparator(" ")
                .appendMinutes().appendSuffix("m").appendSeparator(" ")
                .toFormatter();

        String oneMonth = Period.months(1).toString(formatter);
        assertEquals("1M", oneMonth);
        Period period = formatter.parsePeriod(oneMonth);
        assertEquals(Period.months(1), period);
        String oneMinute = Period.minutes(1).toString(formatter);
        assertEquals("1m", oneMinute);
        period = formatter.parsePeriod(oneMinute);
        assertEquals(Period.minutes(1), period);
    }

    public void testAppendSeparatorIfFieldsBeforeThrowsIllegalStateExceptionAndAppendPrefixTakingString() {
        PeriodFormatterBuilder periodFormatterBuilder = new PeriodFormatterBuilder();
        periodFormatterBuilder.appendPrefix("=9Z/])WG");

        try {
            periodFormatterBuilder.appendSeparatorIfFieldsBefore("=9Z/])WG");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals(PeriodFormatterBuilder.class.getName(), e.getStackTrace()[0].getClassName());
        }
    }

    public void testAppendSeparatorIfFieldsBeforeThrowsIllegalStateExceptionAndAppendSeparatorIfFieldsAfter() {
        PeriodFormatterBuilder periodFormatterBuilder = new PeriodFormatterBuilder();
        periodFormatterBuilder.appendSeparatorIfFieldsAfter("3xmZ\"*'Q={=");

        try {
            periodFormatterBuilder.appendSeparatorIfFieldsBefore("3xmZ\"*'Q={=");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals(PeriodFormatterBuilder.class.getName(), e.getStackTrace()[0].getClassName());
        }
    }

    public void testAppendSeparatorTaking3ArgumentsWithEmptyStringAndNull() {
        PeriodFormatterBuilder periodFormatterBuilder = new PeriodFormatterBuilder();

        try {
            periodFormatterBuilder.appendSeparator("", null, null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(PeriodFormatterBuilder.class.getName(), e.getStackTrace()[0].getClassName());
        }
    }

    public void testAppendSeparatorTaking3ArgumentsWithNullAndNonEmptyArray() {
        PeriodFormatterBuilder periodFormatterBuilder = new PeriodFormatterBuilder();
        String[] stringArray = new String[3];

        try {
            periodFormatterBuilder.appendSeparator(null, null, stringArray);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(PeriodFormatterBuilder.class.getName(), e.getStackTrace()[0].getClassName());
        }
    }

    public void testAppendSuffixTaking2StringArraysThrowsIllegalStateException() {
        PeriodFormatterBuilder periodFormatterBuilder = new PeriodFormatterBuilder();
        String[] stringArray = new String[1];
        stringArray[0] = "8io`#&*f6&";
        periodFormatterBuilder.appendSecondsWithMillis();
        periodFormatterBuilder.appendSeparator("8io`#&*f6&", "NW7");

        try {
            periodFormatterBuilder.appendSuffix(stringArray, stringArray);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals(PeriodFormatterBuilder.class.getName(), e.getStackTrace()[0].getClassName());
        }
    }

    public void testAppendLiteralThrowsIllegalArgumentException() {
        PeriodFormatterBuilder periodFormatterBuilder = new PeriodFormatterBuilder();

        try {
            periodFormatterBuilder.appendLiteral(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(PeriodFormatterBuilder.class.getName(), e.getStackTrace()[0].getClassName());
        }
    }

    public void testAppendTakingPeriodFormatterThrowsIllegalArgumentException() {
        PeriodFormatterBuilder periodFormatterBuilder = new PeriodFormatterBuilder();

        try {
            periodFormatterBuilder.append(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(PeriodFormatterBuilder.class.getName(), e.getStackTrace()[0].getClassName());
        }
    }

}
