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
package org.joda.test.time;
//
import java.io.PrintStream;

import junit.framework.TestSuite;

import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
//
/**
 * This class is a Junit unit test for the
 * constructors of various DateTime, DateOnly, and
 * TimeOnly objects.
 *
 * @author Guy Allard
 *
 */
public class TestConstructors extends BulkTest {

    /**
     * This is the main class for this test suite.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    /**
     * TestSuite suite() is a junit required method.
     * @see org.joda.test.time.BulkTest
     */
    public static TestSuite suite() {
        return BulkTest.makeSuite(TestConstructors.class);
    }
    /**
     * TestConstructors constructor.
     * @param name
     */
    public TestConstructors(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    /**
     * Test the <code>DateOnly</code> constructors.
     * @see org.joda.time.DateOnly
     */
    public void testDateOnlyConstructors() {
        dateOnly = true;
        //
        // Tests for Spec Section 5.2.1
        //
        new SectionRunner( new SpecSection( "5.2.1.1", dates5211 ),
            "", "" ).run();
        //
        new SectionRunner( new SpecSection( "5.2.1.2", dates5212 ),
            "", "" ).run();
        //
        new SectionRunner( new SpecSection( "5.2.1.3", dates5213 ),
            "", "" ).run();
        //
        new SectionRunner( new SpecSection( "5.2.1.4", dates5214 ),
            "", "" ).run();
        //
        // Tests for Spec Section 5.2.2.1
        //
        new SectionRunner( new SpecSection( "5.2.2.1", dates5221 ),
            "", "" ).run();
        //
        // Tests for Spec Section 5.2.2.2
        //
        new SectionRunner( new SpecSection( "5.2.2.2", dates5222 ),
            "", "" ).run();
        //
        // Tests for Spec Section 5.2.2.3
        //
        new SectionRunner( new SpecSection( "5.2.2.3", dates5223 ),
            "", "" ).run();
        //
        // Tests for Spec Section 5.2.3.1
        //
        new SectionRunner( new SpecSection( "5.2.3.1", dates5231 ),
            "", "" ).run();
        //
        // Tests for Spec Section 5.2.3.2
        //
        new SectionRunner( new SpecSection( "5.2.3.2", dates5232 ),
            "", "" ).run();
        //
        // Tests for Spec Section 5.2.3.3
        //
        new SectionRunner( new SpecSection( "5.2.3.3", dates5233 ),
            "", "" ).run();
        //
        // Tests for Spec Section 5.2.3.4
        //
        new SectionRunner( new SpecSection( "5.2.3.4", dates5234 ),
            "", "" ).run();
        //
        wtr.println("Running Totals:");
        tt.showResults();
    }
    /**
     * Test the <code>TimeOnly</code> constructors.
     * @see org.joda.time.TimeOnly
     */
    public void testTimeOnlyConstructors() {
        dateOnly = false;
        //
        // Tests for Spec Section 5.3.1 - No Leading 'T', no 'Z' suffix
        //
        new SectionRunner( new SpecSection( "5.3.1.1", times5311 ),
            "", "" ).run();
        //
        new SectionRunner( new SpecSection( "5.3.1.2", times5312 ),
            "", "" ).run();
        //
        new SectionRunner( new SpecSection( "5.3.1.3", times5313 ),
            "", "" ).run();
        //
        new SectionRunner( new SpecSection( "5.3.1.4", times5314 ),
            "", "" ).run();
        //
        // Tests for Spec Section 5.3.1 - Leading 'T', no 'Z' suffix
        //
        new SectionRunner( new SpecSection( "5.3.1.1", times5311 ),
            "T", "" ).run();
        //
        new SectionRunner( new SpecSection( "5.3.1.2", times5312 ),
            "T", "" ).run();
        //
        new SectionRunner( new SpecSection( "5.3.1.3", times5313 ),
            "T", "" ).run();
        //
        new SectionRunner( new SpecSection( "5.3.1.4", times5314 ),
            "T", "" ).run();
        //
        // Tests for Spec Section 5.3.1 - No leading 'T", 'Z' suffix
        //
        new SectionRunner( new SpecSection( "5.3.1.1", times5311 ),
            "", "Z" ).run();
        //
        new SectionRunner( new SpecSection( "5.3.1.2", times5312 ),
            "", "Z" ).run();
        //
        new SectionRunner( new SpecSection( "5.3.1.3", times5313 ),
            "", "Z" ).run();
        //
        new SectionRunner( new SpecSection( "5.3.1.4", times5314 ),
            "", "Z" ).run();
        //
        // Tests for Spec Section 5.3.1 - Leading 'T" and 'Z' suffix
        //
        new SectionRunner( new SpecSection( "5.3.1.1", times5311 ),
            "T", "Z" ).run();
        //
        new SectionRunner( new SpecSection( "5.3.1.2", times5312 ),
            "T", "Z" ).run();
        //
        new SectionRunner( new SpecSection( "5.3.1.3", times5313 ),
            "T", "Z" ).run();
        //
        new SectionRunner( new SpecSection( "5.3.1.4", times5314 ),
            "T", "Z" ).run();
        //
        // Tests for Spec Section 5.3.2
        //
        new SectionRunner( new SpecSection( "5.3.2", times532 ),
            "", "" ).run();
        //
        // Tests for Spec Section 5.3.4.2
        //
        new SectionRunner( new SpecSection( "5.3.4.2", times5342 ),
            "", "" ).run();
        //
        wtr.println("Running Totals:");
        tt.showResults();
    }
    /**
     * Test the <code>DateTime</code> constructors.
     * @see org.joda.time.DateTime
     */
    public void testDateTimeConstructors() {
        dateTimes = true;
        //
        // Tests for Spec Section 5.4.1
        //
        new SectionRunner( new SpecSection( "5.4.1", datetimes541 ),
            "", "" ).run();
        new SectionRunner( new SpecSection( "5.4.1", datetimes541 ),
            "", "Z" ).run();
        //
        // Tests for Spec Section 5.4.2
        //
        new SectionRunner( new SpecSection( "5.4.2", datetimes542 ),
            "", "" ).run();
        //
        wtr.println("Running Totals:");
        tt.showResults();
    }
//
// ------------------------------------------------------------
// Private data and methods
// ------------------------------------------------------------
//
    private final TotalsTracker tt = new TotalsTracker();
//
    private boolean dateOnly = true;
    private boolean dateTimes = false;
    protected  PrintStream wtr = System.err;
    //
    // Spec Section 5.2 - Dates
    //
    // Spec Section 5.2.1 - Calendar Dates
    //
    // Spec Section 5.2.1.1 - Complete Representation
    //
    private final String[][] dates5211 = {
            {"19990101",
                "Complete~Basic~YYYYMMDD~Spec 5.2.1.1"},
            {"1999-01-01",
                "Complete~Extended~YYYY-MM-DD~Spec 5.2.1.1"},
    };
    //
    // Spec Section 5.2.1.2 - Representations with reduced precision
    //
    private final String[][] dates5212 = {
            // A specific month
            {"1999-01",
                "Reduced~Basic~YYYY-MM~Spec 5.2.1.2(a)"},
            // A specific year
            {"1999",
                "Reduced~Basic~YYYY~Spec 5.2.1.2(b)"},
            // A specific century
            {"19",
                "Reduced~Basic~YY~Spec 5.2.1.2(c)"},
        };
    //
    // Spec Section 5.2.1.3 - Truncated representations
    //
    private final String[][] dates5213 = {
            // A specific date in the implied century
            {"990101",
                "Truncated~Basic~YYMMDD~Spec 5.2.1.3(a)"},
            {"99-01-01",
                "Truncated~Extended~YY-MM-DD~Spec 5.2.1.3(a)"},
            // A specific year and month in the implied century
            {"-9901",
                "Truncated~Basic~-YYMM~Spec 5.2.1.3(b)"},
            {"-99-01",
                "Truncated~Extended~YYMMDD~Spec 5.2.1.3(b)"},
            // A specific year in the implied century
            {"-99",
                "Truncated~Basic~-YY~Spec 5.2.1.3(c)"},
            // A specific day of a month in the implied year
            {"--0101",
                "Truncated~Basic~--MMDD~Spec 5.2.1.3(d)"},
            {"--01-01",
                "Truncated~Basic~--MM-DD~Spec 5.2.1.3(d)"},
            // A specific month in the implied year
            {"--01",
                "Truncated~Basic~--MM~Spec 5.2.1.3(e)"},
            // A specific day in the implied month
            {"---01",
                "Truncated~Basic~---DD~Spec 5.2.1.3(f)"},
    };
    //
    // Spec Section 5.2.1.4 - Expanded Representations
    //
    private final String[][] dates5214 = {
            // A specific day
            {"-19990101",
                "Expanded~Basic~-YYYYMMDD~Spec 5.2.1.4(a)"},
            {"+19990101",
                "Expanded~Basic~+YYYYMMDD~Spec 5.2.1.4(a)"},
            {"-1999-01-01",
                "Expanded~Extended~-YYYY-MM-DD~Spec 5.2.1.4(a)"},
            {"+1999-01-01",
                "Expanded~Extended~+YYYY-MM-DD~Spec 5.2.1.4(a)"},
            // A specific month
            {"-1999-01",
                "Expanded~Basic~-YYYY-MM~Spec 5.2.1.4(b)"},
            {"+1999-01",
                "Expanded~Basic~+YYYY-MM~Spec 5.2.1.4(b)"},
            // A specific year
            {"-1999",
                "Expanded~Basic~-YYYY~Spec 5.2.1.4(c)"},
            {"+1999",
                "Expanded~Basic~+YYYY~Spec 5.2.1.4(c)"},
            // A specific century
            {"-19",
                "Expanded~Basic~-YY~Spec 5.2.1.4(d)"},
            {"+19",
                "Expanded~Basic~+YY~Spec 5.2.1.4(d)"},
    };
    //
    // Spec Section 5.2.2 - Ordinal Dates
    //
    // Spec Section 5.2.2.1 - Complete Representation
    //
    private final String[][] dates5221 = {
            {"1999123",
                "Complete~Basic~YYYYDDD~Spec 5.2.2.1"},
            {"1999-123",
                "Complete~Extended~YYYY-DDD~Spec 5.2.2.1"},
    };
    //
    // Spec Section 5.2.2.2 - Truncated Representation
    //
    private final String[][] dates5222 = {
            // A specific year and day in the implied century
            {"99123",
                "Truncated~Basic~YYDDD~Spec 5.2.2.2(a)"},
            {"99-123",
                "Truncated~Extended~YY-DDD~Spec 5.2.2.2(a)"},
            // Day only in the implied year
            {"-123",
                "Truncated~Basic~-DDD~Spec 5.2.2.2(b)"},
    };
    //
    // Spec Section 5.2.2.3 - Expanded Representation
    //
    private final String[][] dates5223 = {
        // A specific day
            {"-1999123",
                "Expanded~Basic~-YYYYDDD~Spec 5.2.2.3(a)"},
            {"-1999-123",
                "Expanded~Extended~-YYYY-DDD~Spec 5.2.2.3(a)"},
    };
    //
    // Spec Section 5.2.3 - Week Date
    //
    // Spec Section 5.2.3.1 - Complete representation
    //
    private final String[][] dates5231 = {
            {"1999W236",
                "Complete~Basic~YYYYWwwD~Spec 5.2.3.1"},
            {"1999-W23-6",
                "Complete~Extended~YYYY-Www-D~Spec 5.2.3.1"},
    };
    //
    // Spec Section 5.2.3.2 - Representations with reduced precision
    //
    private final String[][] dates5232 = {
        // A specific week
            {"1999W23",
                "Reduced~Basic~YYYYWww~Spec 5.2.3.2(a)"},
            {"1999-W23",
                "Reduced~Extended~YYYY-Www~Spec 5.2.3.2(a)"},
    };
    //
    // Spec Section 5.2.3.3 - Truncated representations
    //
    private final String[][] dates5233 = {
        // Year, week, and day in the implied century
            {"99W236",
                "Truncated~Basic~YYWwwD~Spec 5.2.3.3(a)"},
            {"99-W23-6",
                "Truncated~Extended~YYWwwD~Spec 5.2.3.3(a)"},
        // Year and week only in the implied century
            {"99W23",
                "Truncated~Basic~YYWww~Spec 5.2.3.3(b)"},
            {"99-W23",
                "Truncated~Extended~YY-Www~Spec 5.2.3.3(b)"},
        // Year of the implied decade, week and day only
            {"-5W236",
                "Truncated~Basic~-YWwwD~Spec 5.2.3.3(c)"},
            {"-5-W23-6",
                "Truncated~Extended~-Y-Www-D~Spec 5.2.3.3(c)"},
        // Year of the implied decade and week only
            {"-5W23",
                "Truncated~Basic~-YWww~Spec 5.2.3.3(d)"},
            {"-5-W23",
                "Truncated~Extended~-Y-Www~Spec 5.2.3.3(d)"},
        // Week and day only of the implied year
            {"-W236",
                "Truncated~Basic~-WwwD~Spec 5.2.3.3(e)"},
            {"-W23-6",
                "Truncated~Extended~-Www-D~Spec 5.2.3.3(e)"},
        // Week only of the implied year
            {"-W23",
                "Truncated~Basic~-Www~Spec 5.2.3.3(f)"},
        // Day only of the implied week
            {"-W-6",
                "Truncated~Basic~-W-D~Spec 5.2.3.3(g)"},
    };
    //
    // Spec Section 5.2.3.4 - Expanded representations
    //
    private final String[][] dates5234 = {
        // A specific day
            {"-1999W236",
                "Truncated~Basic~-YYYYWwwd~Spec 5.2.3.4(a)"},
            {"+1999W236",
                "Truncated~Basic~+YYYYWwwd~Spec 5.2.3.4(a)"},
            {"-1999-W23-6",
                "Truncated~Extended~-YYYY-Www-d~Spec 5.2.3.4(a)"},
            {"+1999-W23-6",
                "Truncated~Extended~+YYYY-Www-d~Spec 5.2.3.4(a)"},
        // A specific week
            {"-1999W23",
                "Truncated~Basic~-YYYYWww~Spec 5.2.3.4(b)"},
            {"+1999W23",
                "Truncated~Basic~+YYYYWww~Spec 5.2.3.4(b)"},
            {"-1999-W23",
                "Truncated~Extended~-YYYY-Www~Spec 5.2.3.4(b)"},
            {"+1999-W23",
                "Truncated~Extended~+YYYY-Www~Spec 5.2.3.4(b)"},
    };
    //
    // Spec Section 5.3 - Time of day
    //
    // Spec Section 5.3.1.1 - Complete representation
    //
    private final String[][] times5311 = {
            {"232050",
                "Complete~Basic~hhmmss~Spec 5.3.1.1"},
            {"23:20:50",
                "Complete~Extended~hh:mm:ss~Spec 5.3.1.1"},
    };
    //
    // Spec Section 5.3.1.2 - Representations with reduced precision
    //
    private final String[][] times5312 = {
            // A specific hour and minute
            {"2320",
                "Reduced~Basic~hhmm~Spec 5.3.1.2(a)"},
            {"23:20",
                "Reduced~Extended~hh:mm~Spec 5.3.1.2(a)"},
            // A specific hour
            {"23",
                "Reduced~Basic~hh~Spec 5.3.1.2(b)"},
    };
    //
    // Spec Section 5.3.1.3 - Representation of decimal fractions
    //
    private final String[][] times5313 = {
            // A specific hour, minute, second, and decimal fraction of
            // a second
            {"232050,5",
                "Decimal~Basic~hhmmss,ss~Spec 5.3.1.3(a)"},
            {"23:20:50,5",
                "Decimal~Extended~hh:mm:ss,ss~Spec 5.3.1.3(a)"},
            // A specific hour, minute, and decimal fraction of a minute
            {"2320,8",
                "Decimal~Basic~hhmm,mm~Spec 5.3.1.3(b)"},
            {"23:20,8",
                "Decimal~Extended~hh:mm,mm~Spec 5.3.1.3(b)"},
            // A specific hour and decimal fraction of an hour
            {"23,8",
                "Decimal~Basic~hh,hh~Spec 5.3.1.3(c)"},
    };
    //
    // Spec Section 5.3.1.4 - Truncated representations
    //
    private final String[][] times5314 = {
        // A specific minute and second of the implied hour
            {"-2050",
                "Truncated~Basic~-mmss~Spec 5.3.1.4(a)"},
            {"-20:50",
                "Truncated~Extended~-mm:ss~Spec 5.3.1.4(a)"},
        // A specific minute of the implied hour
            {"-20",
                "Truncated~Basic~-mm~Spec 5.3.1.4(b)"},
        // A specific second of the implied minute
            {"--50",
                "Truncated~Basic~--ss~Spec 5.3.1.4(c)"},
        // A specific minute and second of the implied hour and
        // a decimal fraction of a second
            {"-2050,5",
                "Truncated~Basic~-mmss,s~Spec 5.3.1.4(d)"},
            {"-20:50,5",
                "Truncated~Extended~-mm:ss,s~Spec 5.3.1.4(d)"},
        // A specific minute of the implied hour and a decimal fraction
        // of the minute
            {"-20,8",
                "Truncated~Basic~-mm,m~Spec 5.3.1.4(e)"},
        // A specific second of the implied minute and a decimal fraction
        // of the second
            {"--50,5",
                "Truncated~Basic~--ss,s~Spec 5.3.1.4(f)"},
    };
    //
    // Spec Section 5.3.2 - Midnight
    //
    private final String[][] times532 = {
            {"000000",
                "Midnight~Basic~000000~Spec 5.3.2"},
            {"00:00:00",
                "Midnight~Extended~00:00:00~Spec 5.3.2"},
            {"240000",
                "Midnight~Basic~240000~Spec 5.3.2"},
            {"24:00:00",
                "Midnight~Extended~24:00:00~Spec 5.3.2"},
            {"0000",
                "Midnight~Basic~0000~Spec 5.3.2 Note 1"},
            {"2400",
                "Midnight~Basic~2400~Spec 5.3.2 Note 1"},
    };
    //
    // Spec Section 5.3.4.2 - Local time and the difference with UTC
    //
    private final String[][] times5342 = {
            {"152746+0100",
                "Difference~Basic~hhmmss(+/-)hhmm~Spec 5.3.4.2"},
            {"15:27:46+01:00",
                "Difference~Extended~hh:mm:ss(+/-)hh:mm~Spec 5.3.4.2"},
            {"152746-0500",
                "Difference~Basic~hhmmss(+/-)hhmm~Spec 5.3.4.2"},
            {"15:27:46-05:00",
                "Difference~Extended~hh:mm:ss(+/-)hh:mm~Spec 5.3.4.2"},
    };
    //
    // Spec Section 5.4 - Combinations of date and time of day
    //
    // Spec Section 5.4.1 - Complete representation
    //
    private final String[][] datetimes541 = {
            {"19990101T112233",
                "Combination~Basic~YYYYMMDDThhmmss~Spec 5.4.1(a)"},
            {"1999-01-01T11:22:33",
                "Combination~Extended~YYYY-MM-DDThh:mm:ss~Spec 5.4.1(a)"},
            {"1999123T112233",
                "Combination~Basic~YYYYDDDThhmmss~Spec 5.4.1(b)"},
            {"1999-123T11:22:33",
                "Combination~Extended~YYYY-DDDThh:mm:ss~Spec 5.4.1(b)"},
            {"1999W176T112233",
                "Combination~Basic~YYYYWwwDThhmmss~Spec 5.4.1(c)"},
            {"1999-W17-6T11:22:33",
                "Combination~Extended~YYYY-Www-DThh:mm:ss~Spec 5.4.1(c)"},
    };
    //
    // Spec Section 5.4.2 - Representations other than complete
    //
    private final String[][] datetimes542 = {
            {"19990101T1516",
                "Reduced-Combo~Basic~YYYYMMDDThhmm~Spec 5.4.2(a)"},
            {"1999-01-01T15:16",
                "Reduced-Combo~Extended~YYYY-MM-DDThh:mm~Spec 5.4.2(a)"},
            {"1999123T1516",
                "Reduced-Combo~Basic~YYYYDDDThhmm~Spec 5.4.2(b)"},
            {"1999-123T15:16",
                "Reduced-Combo~Extended~YYYY-DDDThh:mm~Spec 5.4.2(b)"},
            {"1985W155T1015+0400",
                "Reduced-Combo~Basic~YYYYWwwDThhmm+hhmm~Spec 5.4.2(b)"},
            {"1985W155T1015-0400",
                "Reduced-Combo~Basic~YYYYWwwDThhmm-hhmm~Spec 5.4.2(b)"},
            {"1985-W15-5T10:15+04:00",
                "Reduced-Combo~Extended~YYYY-Www-DThh:mm+hh:mm~Spec 5.4.2(b)"},
            {"1985-W15-5T10:15-04:00",
                "Reduced-Combo~Extended~YYYY-Www-DThh:mm-hh:mm~Spec 5.4.2(b)"},
    };
    //
    // A Specification Section
    //
    private class SpecSection {
        private final String name;
        private final String[][] testData;
        private final int numTests;
        private int failedTests = 0;
        SpecSection(final String name, final String[][] testData) {
            this.name = name;
            this.testData = testData;
            this.numTests = this.testData.length;
        }
        public String getName() {
            return name;
        }
        public String[][] getTestData() {
            return testData;
        }
        public int getNumTests() {
            return numTests;
        }
        public int getFailedTests() {
            return failedTests;
        }
        public void bumpFailedTests() {
            ++failedTests;
        }
        public void showResults() {
            wtr.println("Section Results For: " + name );
            wtr.println("\tSection Number of CTOR tests: " + numTests );
            wtr.println("\tSection Failed CTOR tests: " + failedTests );
        }

    }
    //
    // A Specification Section Test Runner
    //
    private class SectionRunner {
        private final SpecSection ss;
        private final String prepend;
        private final String append;
        private ReadableInstant ri = null;
        SectionRunner(final SpecSection ss, final String prepend,
                      final String append) {
            this.ss = ss;
            this.prepend = prepend;
            this.append = append;
        }
        public final void run() {
            wtr.println(" ");
            wtr.println("-> Start Section: " + ss.getName());
            String[][] testData = ss.getTestData();
            String sDT = null;
            boolean raised = false;
            for(int nextDate = 0; nextDate < testData.length; ++nextDate) {
                sDT = prepend + testData[nextDate][0] + append;
                wtr.println(" ");
                wtr.println( "The Next String Is: " + sDT );
                wtr.println( "The Spec Reference Is: "
                    + testData[nextDate][1] );
                tt.bumpNumTests();
                try
                {
                    if ( dateTimes ) {
                        ri = new DateTime( sDT );
                    }
                    /*
                    else
                    {
                        if ( dateOnly ) {
                            ri = new DateOnly( sDT );
                        }
                        else
                        {
                            ri = new TimeOnly( sDT );
                        }
                    }
                    */
                }
                catch(IllegalArgumentException pe)
                {
                    ss.bumpFailedTests();
                    tt.bumpFailedTests();
                    wtr.println("Parse Exception Detected");
                    pe.printStackTrace( wtr );
                    raised = true;
                }
                if ( !raised ) {
                    wtr.print("Construction Complete, ");
                    wtr.println("As String: " + this.ri);
                }
                raised = false;
            } // end of the for
            ss.showResults();
            tt.showResults();
        } // end of run
    }
    //
    // Running Totals Tracker
    //
    private class TotalsTracker {
        private int numTests = 0;
        private int failedTests = 0;
        public final int getNumTests() { return numTests; }
        public final int getFailedTests() { return failedTests; }
        public final void bumpNumTests() { ++numTests; }
        public final void bumpFailedTests() { ++failedTests; }
        public final void showResults() {
            // wtr.println(" ");
            wtr.println("\tCurrent Total of CTOR tests: " + numTests );
            wtr.println("\tCurrent # of Failed CTOR tests: " + failedTests );
        }

    }
}
