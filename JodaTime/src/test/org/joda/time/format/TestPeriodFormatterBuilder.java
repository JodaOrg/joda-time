/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2004 Stephen Colebourne.  
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Joda project (http://www.joda.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "Joda" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact licence@joda.org.
 *
 * 5. Products derived from this software may not be called "Joda",
 *    nor may "Joda" appear in their name, without prior written
 *    permission of the Joda project.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE JODA AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Joda project and was originally 
 * created by Stephen Colebourne <scolebourne@joda.org>. For more
 * information on the Joda project, please see <http://www.joda.org/>.
 */
package org.joda.time.format;

import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.PeriodType;

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

    private static final DateTimeZone PARIS = DateTimeZone.getInstance("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.getInstance("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.getInstance("Asia/Tokyo");

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
    public void testFormatYears() {
        PeriodFormatter f = builder.appendYears().toFormatter();
        assertEquals("1", f.print(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
    }

    public void testFormatMonths() {
        PeriodFormatter f = builder.appendMonths().toFormatter();
        assertEquals("2", f.print(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
    }

    public void testFormatWeeks() {
        PeriodFormatter f = builder.appendWeeks().toFormatter();
        assertEquals("3", f.print(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
    }

    public void testFormatDays() {
        PeriodFormatter f = builder.appendDays().toFormatter();
        assertEquals("4", f.print(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
    }

    public void testFormatHours() {
        PeriodFormatter f = builder.appendHours().toFormatter();
        assertEquals("5", f.print(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
    }

    public void testFormatMinutes() {
        PeriodFormatter f = builder.appendMinutes().toFormatter();
        assertEquals("6", f.print(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
    }

    public void testFormatSeconds() {
        PeriodFormatter f = builder.appendSeconds().toFormatter();
        assertEquals("7", f.print(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
    }

    public void testFormatSecondsWithMillis() {
        PeriodFormatter f = builder.appendSecondsWithMillis().toFormatter();
        Period p = new Period(0, 0, 0, 0, 0, 0, 7, 0);
        assertEquals("7.000", f.print(p));
        assertEquals(5, ((BasePeriodFormatter) f).calculatePrintedLength(p));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(p));
        
        p = new Period(0, 0, 0, 0, 0, 0, 7, 1);
        assertEquals("7.001", f.print(p));
        assertEquals(5, ((BasePeriodFormatter) f).calculatePrintedLength(p));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(p));
        
        p = new Period(0, 0, 0, 0, 0, 0, 7, 999);
        assertEquals("7.999", f.print(p));
        assertEquals(5, ((BasePeriodFormatter) f).calculatePrintedLength(p));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(p));
        
        p = new Period(0, 0, 0, 0, 0, 0, 7, 1000);
        assertEquals("8.000", f.print(p));
        assertEquals(5, ((BasePeriodFormatter) f).calculatePrintedLength(p));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(p));
        
        p = new Period(0, 0, 0, 0, 0, 0, 7, 1001);
        assertEquals("8.001", f.print(p));
        assertEquals(5, ((BasePeriodFormatter) f).calculatePrintedLength(p));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(p));
        
        p = new Period(0, 0, 0, 0, 0, 0, 7, -1);
        assertEquals("6.999", f.print(p));
        assertEquals(5, ((BasePeriodFormatter) f).calculatePrintedLength(p));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(p));
        
        p = new Period(0, 0, 0, 0, 0, 0, -7, 1);
        assertEquals("-6.999", f.print(p));
        assertEquals(6, ((BasePeriodFormatter) f).calculatePrintedLength(p));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(p));
        
        p = new Period(0, 0, 0, 0, 0, 0, -7, -1);
        assertEquals("-7.001", f.print(p));
        assertEquals(6, ((BasePeriodFormatter) f).calculatePrintedLength(p));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(p));
    }

    public void testFormatSecondsWithOptionalMillis() {
        PeriodFormatter f = builder.appendSecondsWithOptionalMillis().toFormatter();
        Period p = new Period(0, 0, 0, 0, 0, 0, 7, 0);
        assertEquals("7", f.print(p));
        assertEquals(1, ((BasePeriodFormatter) f).calculatePrintedLength(p));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(p));
        
        p = new Period(0, 0, 0, 0, 0, 0, 7, 1);
        assertEquals("7.001", f.print(p));
        assertEquals(5, ((BasePeriodFormatter) f).calculatePrintedLength(p));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(p));
        
        p = new Period(0, 0, 0, 0, 0, 0, 7, 999);
        assertEquals("7.999", f.print(p));
        assertEquals(5, ((BasePeriodFormatter) f).calculatePrintedLength(p));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(p));
        
        p = new Period(0, 0, 0, 0, 0, 0, 7, 1000);
        assertEquals("8", f.print(p));
        assertEquals(1, ((BasePeriodFormatter) f).calculatePrintedLength(p));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(p));
        
        p = new Period(0, 0, 0, 0, 0, 0, 7, 1001);
        assertEquals("8.001", f.print(p));
        assertEquals(5, ((BasePeriodFormatter) f).calculatePrintedLength(p));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(p));
        
        p = new Period(0, 0, 0, 0, 0, 0, 7, -1);
        assertEquals("6.999", f.print(p));
        assertEquals(5, ((BasePeriodFormatter) f).calculatePrintedLength(p));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(p));
        
        p = new Period(0, 0, 0, 0, 0, 0, -7, 1);
        assertEquals("-6.999", f.print(p));
        assertEquals(6, ((BasePeriodFormatter) f).calculatePrintedLength(p));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(p));
        
        p = new Period(0, 0, 0, 0, 0, 0, -7, -1);
        assertEquals("-7.001", f.print(p));
        assertEquals(6, ((BasePeriodFormatter) f).calculatePrintedLength(p));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(p));
    }

    public void testFormatMillis() {
        PeriodFormatter f = builder.appendMillis().toFormatter();
        assertEquals("8", f.print(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
    }

    public void testFormatMillis3Digit() {
        PeriodFormatter f = builder.appendMillis3Digit().toFormatter();
        assertEquals("008", f.print(PERIOD));
        assertEquals(3, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
    }

    //-----------------------------------------------------------------------
    public void testFormatPrefixSimple1() {
        PeriodFormatter f = builder.appendPrefix("Years:").appendYears().toFormatter();
        assertEquals("Years:1", f.print(PERIOD));
        assertEquals(7, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
    }

    public void testFormatPrefixSimple2() {
        PeriodFormatter f = builder.appendPrefix("Hours:").appendHours().toFormatter();
        assertEquals("Hours:5", f.print(PERIOD));
        assertEquals(7, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
    }

    public void testFormatPrefixSimple3() {
        try {
            builder.appendPrefix(null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testFormatPrefixPlural1() {
        PeriodFormatter f = builder.appendPrefix("Year:", "Years:").appendYears().toFormatter();
        assertEquals("Year:1", f.print(PERIOD));
        assertEquals(6, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
    }

    public void testFormatPrefixPlural2() {
        PeriodFormatter f = builder.appendPrefix("Hour:", "Hours:").appendHours().toFormatter();
        assertEquals("Hours:5", f.print(PERIOD));
        assertEquals(7, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
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
            builder.appendPrefix(null, null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testFormatSuffixSimple1() {
        PeriodFormatter f = builder.appendYears().appendSuffix(" years").toFormatter();
        assertEquals("1 years", f.print(PERIOD));
        assertEquals(7, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
    }

    public void testFormatSuffixSimple2() {
        PeriodFormatter f = builder.appendHours().appendSuffix(" hours").toFormatter();
        assertEquals("5 hours", f.print(PERIOD));
        assertEquals(7, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
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

    public void testFormatSuffixPlural1() {
        PeriodFormatter f = builder.appendYears().appendSuffix(" year", " years").toFormatter();
        assertEquals("1 year", f.print(PERIOD));
        assertEquals(6, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
    }

    public void testFormatSuffixPlural2() {
        PeriodFormatter f = builder.appendHours().appendSuffix(" hour", " hours").toFormatter();
        assertEquals("5 hours", f.print(PERIOD));
        assertEquals(7, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
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
            builder.appendSuffix(null, null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testFormatSuffixPlural4() {
        try {
            builder.appendSuffix(" hour", " hours");
            fail();
        } catch (IllegalStateException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testFormatPrefixSuffix() {
        PeriodFormatter f = builder.appendPrefix("P").appendYears().appendSuffix("Y").toFormatter();
        assertEquals("P1Y", f.print(PERIOD));
        assertEquals(3, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
    }

    //-----------------------------------------------------------------------
    public void testFormatSeparatorSimple() {
        PeriodFormatter f = builder.appendYears().appendSeparator("T").appendHours().toFormatter();
        assertEquals("1T5", f.print(PERIOD));
        assertEquals(3, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(2, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
        
        assertEquals("5", f.print(TIME_PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).calculatePrintedLength(TIME_PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(TIME_PERIOD));
        
        assertEquals("1", f.print(DATE_PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).calculatePrintedLength(DATE_PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(DATE_PERIOD));
    }

    public void testFormatSeparatorComplex() {
        PeriodFormatter f = builder
            .appendYears().appendSeparator(", ", " and ")
            .appendHours().appendSeparator(", ", " and ")
            .appendMinutes().appendSeparator(", ", " and ")
            .toFormatter();
        assertEquals("1, 5 and 6", f.print(PERIOD));
        assertEquals(10, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(3, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
        
        assertEquals("5 and 6", f.print(TIME_PERIOD));
        assertEquals(7, ((BasePeriodFormatter) f).calculatePrintedLength(TIME_PERIOD));
        assertEquals(2, ((BasePeriodFormatter) f).countFieldsToPrint(TIME_PERIOD));
        
        assertEquals("1", f.print(DATE_PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).calculatePrintedLength(DATE_PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(DATE_PERIOD));
    }

    public void testFormatSeparatorIfFieldsAfter() {
        PeriodFormatter f = builder.appendYears().appendSeparatorIfFieldsAfter("T").appendHours().toFormatter();
        assertEquals("1T5", f.print(PERIOD));
        assertEquals(3, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(2, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
        
        assertEquals("T5", f.print(TIME_PERIOD));
        assertEquals(2, ((BasePeriodFormatter) f).calculatePrintedLength(TIME_PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(TIME_PERIOD));
        
        assertEquals("1", f.print(DATE_PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).calculatePrintedLength(DATE_PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(DATE_PERIOD));
    }

    public void testFormatSeparatorIfFieldsBefore() {
        PeriodFormatter f = builder.appendYears().appendSeparatorIfFieldsBefore("T").appendHours().toFormatter();
        assertEquals("1T5", f.print(PERIOD));
        assertEquals(3, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(2, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
        
        assertEquals("5", f.print(TIME_PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).calculatePrintedLength(TIME_PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(TIME_PERIOD));
        
        assertEquals("1T", f.print(DATE_PERIOD));
        assertEquals(2, ((BasePeriodFormatter) f).calculatePrintedLength(DATE_PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(DATE_PERIOD));
    }

    //-----------------------------------------------------------------------
    public void testFormatLiteral() {
        PeriodFormatter f = builder.appendLiteral("HELLO").toFormatter();
        assertEquals("HELLO", f.print(PERIOD));
        assertEquals(5, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(0, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
    }

    public void testFormatAppendFormatter() {
        PeriodFormatter base = builder.appendYears().appendLiteral("-").toFormatter();
        PeriodFormatter f = new PeriodFormatterBuilder().append(base).appendYears().toFormatter();
        assertEquals("1-1", f.print(PERIOD));
        assertEquals(3, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(2, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
    }

    public void testFormatMinDigits() {
        PeriodFormatter f = new PeriodFormatterBuilder().minimumPrintedDigits(4).appendYears().toFormatter();
        assertEquals("0001", f.print(PERIOD));
        assertEquals(4, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
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
        assertEquals(7, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(4, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
        
        assertEquals("---0", f.print(EMPTY_YEAR_DAY_PERIOD));
        assertEquals(4, ((BasePeriodFormatter) f).calculatePrintedLength(EMPTY_YEAR_DAY_PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(EMPTY_YEAR_DAY_PERIOD));
        
        assertEquals("1---4", f.print(YEAR_DAY_PERIOD));
        assertEquals(5, ((BasePeriodFormatter) f).calculatePrintedLength(YEAR_DAY_PERIOD));
        assertEquals(2, ((BasePeriodFormatter) f).countFieldsToPrint(YEAR_DAY_PERIOD));
        
        assertEquals("---0", f.print(EMPTY_PERIOD));
        assertEquals(4, ((BasePeriodFormatter) f).calculatePrintedLength(EMPTY_PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(EMPTY_PERIOD));
        
        // test only last instance of same field is output
        f = new PeriodFormatterBuilder()
                .appendYears().appendLiteral("-")
                .appendYears().toFormatter();
        assertEquals("-0", f.print(EMPTY_PERIOD));
        assertEquals(2, ((BasePeriodFormatter) f).calculatePrintedLength(EMPTY_PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(EMPTY_PERIOD));
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
        assertEquals(7, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(4, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
        
        assertEquals("---0", f.print(EMPTY_YEAR_DAY_PERIOD));
        assertEquals(4, ((BasePeriodFormatter) f).calculatePrintedLength(EMPTY_YEAR_DAY_PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(EMPTY_YEAR_DAY_PERIOD));
        
        assertEquals("1---4", f.print(YEAR_DAY_PERIOD));
        assertEquals(5, ((BasePeriodFormatter) f).calculatePrintedLength(YEAR_DAY_PERIOD));
        assertEquals(2, ((BasePeriodFormatter) f).countFieldsToPrint(YEAR_DAY_PERIOD));
        
        assertEquals("---0", f.print(EMPTY_PERIOD));
        assertEquals(4, ((BasePeriodFormatter) f).calculatePrintedLength(EMPTY_PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(EMPTY_PERIOD));
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
        assertEquals(7, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(4, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
        
        assertEquals("0---", f.print(EMPTY_YEAR_DAY_PERIOD));
        assertEquals(4, ((BasePeriodFormatter) f).calculatePrintedLength(EMPTY_YEAR_DAY_PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(EMPTY_YEAR_DAY_PERIOD));
        
        assertEquals("1---4", f.print(YEAR_DAY_PERIOD));
        assertEquals(5, ((BasePeriodFormatter) f).calculatePrintedLength(YEAR_DAY_PERIOD));
        assertEquals(2, ((BasePeriodFormatter) f).countFieldsToPrint(YEAR_DAY_PERIOD));
        
        assertEquals("0---", f.print(EMPTY_PERIOD));
        assertEquals(4, ((BasePeriodFormatter) f).calculatePrintedLength(EMPTY_PERIOD));
        assertEquals(1, ((BasePeriodFormatter) f).countFieldsToPrint(EMPTY_PERIOD));
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
        assertEquals(7, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(4, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
        
        assertEquals("0---0", f.print(EMPTY_YEAR_DAY_PERIOD));
        assertEquals(5, ((BasePeriodFormatter) f).calculatePrintedLength(EMPTY_YEAR_DAY_PERIOD));
        assertEquals(2, ((BasePeriodFormatter) f).countFieldsToPrint(EMPTY_YEAR_DAY_PERIOD));
        
        assertEquals("1---4", f.print(YEAR_DAY_PERIOD));
        assertEquals(5, ((BasePeriodFormatter) f).calculatePrintedLength(YEAR_DAY_PERIOD));
        assertEquals(2, ((BasePeriodFormatter) f).countFieldsToPrint(YEAR_DAY_PERIOD));
        
        assertEquals("0-0-0-0", f.print(EMPTY_PERIOD));
        assertEquals(7, ((BasePeriodFormatter) f).calculatePrintedLength(EMPTY_PERIOD));
        assertEquals(4, ((BasePeriodFormatter) f).countFieldsToPrint(EMPTY_PERIOD));
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
        assertEquals(7, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(4, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
        
        assertEquals("0-0-0-0", f.print(EMPTY_YEAR_DAY_PERIOD));
        assertEquals(7, ((BasePeriodFormatter) f).calculatePrintedLength(EMPTY_YEAR_DAY_PERIOD));
        assertEquals(4, ((BasePeriodFormatter) f).countFieldsToPrint(EMPTY_YEAR_DAY_PERIOD));
        
        assertEquals("1-0-0-4", f.print(YEAR_DAY_PERIOD));
        assertEquals(7, ((BasePeriodFormatter) f).calculatePrintedLength(YEAR_DAY_PERIOD));
        assertEquals(4, ((BasePeriodFormatter) f).countFieldsToPrint(YEAR_DAY_PERIOD));
        
        assertEquals("0-0-0-0", f.print(EMPTY_PERIOD));
        assertEquals(7, ((BasePeriodFormatter) f).calculatePrintedLength(EMPTY_PERIOD));
        assertEquals(4, ((BasePeriodFormatter) f).countFieldsToPrint(EMPTY_PERIOD));
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
        assertEquals(7, ((BasePeriodFormatter) f).calculatePrintedLength(PERIOD));
        assertEquals(4, ((BasePeriodFormatter) f).countFieldsToPrint(PERIOD));
        
        assertEquals("---", f.print(EMPTY_YEAR_DAY_PERIOD));
        assertEquals(3, ((BasePeriodFormatter) f).calculatePrintedLength(EMPTY_YEAR_DAY_PERIOD));
        assertEquals(0, ((BasePeriodFormatter) f).countFieldsToPrint(EMPTY_YEAR_DAY_PERIOD));
        
        assertEquals("1---4", f.print(YEAR_DAY_PERIOD));
        assertEquals(5, ((BasePeriodFormatter) f).calculatePrintedLength(YEAR_DAY_PERIOD));
        assertEquals(2, ((BasePeriodFormatter) f).countFieldsToPrint(YEAR_DAY_PERIOD));
        
        assertEquals("---", f.print(EMPTY_PERIOD));
        assertEquals(3, ((BasePeriodFormatter) f).calculatePrintedLength(EMPTY_PERIOD));
        assertEquals(0, ((BasePeriodFormatter) f).countFieldsToPrint(EMPTY_PERIOD));
    }

}
