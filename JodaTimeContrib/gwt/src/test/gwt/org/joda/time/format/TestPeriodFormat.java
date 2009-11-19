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

import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.gwt.JodaGwtTestCase;
import static org.joda.time.gwt.TestConstants.*;
//import junit.framework.TestSuite;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.PeriodType;

/**
 * This class is a Junit unit test for PeriodFormat.
 *
 * @author Stephen Colebourne
 */
public class TestPeriodFormat extends JodaGwtTestCase {
    
    private static final Period PERIOD = new Period(1, 2, 3, 4, 5, 6, 7, 8);
    private static final Period EMPTY_PERIOD = new Period(0, 0, 0, 0, 0, 0, 0, 0);
    private static final Period YEAR_DAY_PERIOD = new Period(1, 0, 0, 4, 5, 6, 7, 8, PeriodType.yearDayTime());
    private static final Period EMPTY_YEAR_DAY_PERIOD = new Period(0, 0, 0, 0, 0, 0, 0, 0, PeriodType.yearDayTime());
    private static final Period TIME_PERIOD = new Period(0, 0, 0, 0, 5, 6, 7, 8);
    private static final Period DATE_PERIOD = new Period(1, 2, 3, 4, 0, 0, 0, 0);

    // Removed for GWT private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    // Removed for GWT private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    // Removed for GWT private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

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

    /* Removed for GWT public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    } */

    /* Removed for GWT public static TestSuite suite() {
        return new TestSuite(TestPeriodFormat.class);
    } */

    /* Removed for GWT public TestPeriodFormat(String name) {
        super(name);
    } */

    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        /* //BEGIN GWT IGNORE
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        //END GWT IGNORE */
        DateTimeZone.setDefault(LONDON);
        /* //BEGIN GWT IGNORE
        //TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        //Locale.setDefault(Locale.UK);
        TimeZone.setDefault(DateTimeZone.forID("Asia/Tokyo").toTimeZone());
        Locale.setDefault(Locale.JAPAN);
        //END GWT IGNORE */
     }

    protected void gwtTearDown() throws Exception {
        super.gwtTearDown();
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        /* //BEGIN GWT IGNORE
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        //END GWT IGNORE */
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    //-----------------------------------------------------------------------
    public void testSubclassableConstructor() {
        PeriodFormat f = new PeriodFormat() {
            // test constructor is protected
        };
        assertNotNull(f);
    }

    //-----------------------------------------------------------------------
    public void testFormatStandard() {
        Period p = new Period(0, 0, 0, 1, 5, 6 ,7, 8);
        assertEquals("1 day, 5 hours, 6 minutes, 7 seconds and 8 milliseconds", PeriodFormat.getDefault().print(p));
    }

    //-----------------------------------------------------------------------
    public void testFormatOneField() {
        Period p = Period.days(2);
        assertEquals("2 days", PeriodFormat.getDefault().print(p));
    }

    //-----------------------------------------------------------------------
    public void testFormatTwoFields() {
        Period p = Period.days(2).withHours(5);
        assertEquals("2 days and 5 hours", PeriodFormat.getDefault().print(p));
    }

    //-----------------------------------------------------------------------
    public void testParseOneField() {
        Period p = Period.days(2);
        assertEquals(p, PeriodFormat.getDefault().parsePeriod("2 days"));
    }

    //-----------------------------------------------------------------------
    public void testParseTwoFields() {
        Period p = Period.days(2).withHours(5);
        assertEquals(p, PeriodFormat.getDefault().parsePeriod("2 days and 5 hours"));
    }

}
