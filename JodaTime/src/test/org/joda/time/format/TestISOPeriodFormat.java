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
public class TestISOPeriodFormat extends TestCase {
    
    private static final Period PERIOD = new Period(1, 2, 3, 4, 5, 6, 7, 8);
    private static final Period EMPTY_PERIOD = new Period(0, 0, 0, 0, 0, 0, 0, 0);
    private static final Period YEAR_DAY_PERIOD = new Period(1, 0, 0, 4, 5, 6, 7, 8, PeriodType.yearDayTime());
    private static final Period EMPTY_YEAR_DAY_PERIOD = new Period(0, 0, 0, 0, 0, 0, 0, 0, PeriodType.yearDayTime());
    private static final Period TIME_PERIOD = new Period(0, 0, 0, 0, 5, 6, 7, 8);
    private static final Period DATE_PERIOD = new Period(1, 2, 3, 4, 0, 0, 0, 0);

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

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

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestISOPeriodFormat.class);
    }

    public TestISOPeriodFormat(String name) {
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
    public void testFormatStandard() {
        Period p = new Period(1, 2, 3, 4, 5, 6 ,7, 8);
        assertEquals("P1Y2M3W4DT5H6M7.008S", ISOPeriodFormat.getInstance().standard().print(p));
        p = new Period(1, 2, 3, 4, 5, 6 ,7, 0);
        assertEquals("P1Y2M3W4DT5H6M7S", ISOPeriodFormat.getInstance().standard().print(p));
        
        p = new Period(0);
        assertEquals("PT0S", ISOPeriodFormat.getInstance().standard().print(p));
        p = new Period(0, PeriodType.standard().withMillisRemoved().withSecondsRemoved());
        assertEquals("PT0M", ISOPeriodFormat.getInstance().standard().print(p));
        
        assertEquals("P1Y4DT5H6M7.008S", ISOPeriodFormat.getInstance().standard().print(YEAR_DAY_PERIOD));
        assertEquals("PT0S", ISOPeriodFormat.getInstance().standard().print(EMPTY_YEAR_DAY_PERIOD));
        assertEquals("P1Y2M3W4D", ISOPeriodFormat.getInstance().standard().print(DATE_PERIOD));
        assertEquals("PT5H6M7.008S", ISOPeriodFormat.getInstance().standard().print(TIME_PERIOD));
    }

    //-----------------------------------------------------------------------
    public void testFormatAlternate() {
        Period p = new Period(1, 2, 3, 4, 5, 6 ,7, 8);
        assertEquals("P00010204T050607.008", ISOPeriodFormat.getInstance().alternate().print(p));
        p = new Period(1, 2, 3, 4, 5, 6 ,7, 0);
        assertEquals("P00010204T050607", ISOPeriodFormat.getInstance().alternate().print(p));
        
        p = new Period(0);
        assertEquals("P00000000T000000", ISOPeriodFormat.getInstance().alternate().print(p));
        p = new Period(0, PeriodType.standard().withMillisRemoved().withSecondsRemoved());
        assertEquals("P00000000T000000", ISOPeriodFormat.getInstance().alternate().print(p));
        
        assertEquals("P00010004T050607.008", ISOPeriodFormat.getInstance().alternate().print(YEAR_DAY_PERIOD));
        assertEquals("P00000000T000000", ISOPeriodFormat.getInstance().alternate().print(EMPTY_YEAR_DAY_PERIOD));
        assertEquals("P00010204T000000", ISOPeriodFormat.getInstance().alternate().print(DATE_PERIOD));
        assertEquals("P00000000T050607.008", ISOPeriodFormat.getInstance().alternate().print(TIME_PERIOD));
    }

    //-----------------------------------------------------------------------
    public void testFormatAlternateExtended() {
        Period p = new Period(1, 2, 3, 4, 5, 6 ,7, 8);
        assertEquals("P0001-02-04T05:06:07.008", ISOPeriodFormat.getInstance().alternateExtended().print(p));
        p = new Period(1, 2, 3, 4, 5, 6 ,7, 0);
        assertEquals("P0001-02-04T05:06:07", ISOPeriodFormat.getInstance().alternateExtended().print(p));
        
        p = new Period(0);
        assertEquals("P0000-00-00T00:00:00", ISOPeriodFormat.getInstance().alternateExtended().print(p));
        p = new Period(0, PeriodType.standard().withMillisRemoved().withSecondsRemoved());
        assertEquals("P0000-00-00T00:00:00", ISOPeriodFormat.getInstance().alternateExtended().print(p));
        
        assertEquals("P0001-00-04T05:06:07.008", ISOPeriodFormat.getInstance().alternateExtended().print(YEAR_DAY_PERIOD));
        assertEquals("P0000-00-00T00:00:00", ISOPeriodFormat.getInstance().alternateExtended().print(EMPTY_YEAR_DAY_PERIOD));
        assertEquals("P0001-02-04T00:00:00", ISOPeriodFormat.getInstance().alternateExtended().print(DATE_PERIOD));
        assertEquals("P0000-00-00T05:06:07.008", ISOPeriodFormat.getInstance().alternateExtended().print(TIME_PERIOD));
    }

    //-----------------------------------------------------------------------
    public void testFormatAlternateWithWeeks() {
        Period p = new Period(1, 2, 3, 4, 5, 6 ,7, 8);
        assertEquals("P0001W0304T050607.008", ISOPeriodFormat.getInstance().alternateWithWeeks().print(p));
        p = new Period(1, 2, 3, 4, 5, 6 ,7, 0);
        assertEquals("P0001W0304T050607", ISOPeriodFormat.getInstance().alternateWithWeeks().print(p));
        
        p = new Period(0);
        assertEquals("P0000W0000T000000", ISOPeriodFormat.getInstance().alternateWithWeeks().print(p));
        p = new Period(0, PeriodType.standard().withMillisRemoved().withSecondsRemoved());
        assertEquals("P0000W0000T000000", ISOPeriodFormat.getInstance().alternateWithWeeks().print(p));
        
        assertEquals("P0001W0004T050607.008", ISOPeriodFormat.getInstance().alternateWithWeeks().print(YEAR_DAY_PERIOD));
        assertEquals("P0000W0000T000000", ISOPeriodFormat.getInstance().alternateWithWeeks().print(EMPTY_YEAR_DAY_PERIOD));
        assertEquals("P0001W0304T000000", ISOPeriodFormat.getInstance().alternateWithWeeks().print(DATE_PERIOD));
        assertEquals("P0000W0000T050607.008", ISOPeriodFormat.getInstance().alternateWithWeeks().print(TIME_PERIOD));
    }

    //-----------------------------------------------------------------------
    public void testFormatAlternateExtendedWithWeeks() {
        Period p = new Period(1, 2, 3, 4, 5, 6 ,7, 8);
        assertEquals("P0001-W03-04T05:06:07.008", ISOPeriodFormat.getInstance().alternateExtendedWithWeeks().print(p));
        p = new Period(1, 2, 3, 4, 5, 6 ,7, 0);
        assertEquals("P0001-W03-04T05:06:07", ISOPeriodFormat.getInstance().alternateExtendedWithWeeks().print(p));
        
        p = new Period(0);
        assertEquals("P0000-W00-00T00:00:00", ISOPeriodFormat.getInstance().alternateExtendedWithWeeks().print(p));
        p = new Period(0, PeriodType.standard().withMillisRemoved().withSecondsRemoved());
        assertEquals("P0000-W00-00T00:00:00", ISOPeriodFormat.getInstance().alternateExtendedWithWeeks().print(p));
        
        assertEquals("P0001-W00-04T05:06:07.008", ISOPeriodFormat.getInstance().alternateExtendedWithWeeks().print(YEAR_DAY_PERIOD));
        assertEquals("P0000-W00-00T00:00:00", ISOPeriodFormat.getInstance().alternateExtendedWithWeeks().print(EMPTY_YEAR_DAY_PERIOD));
        assertEquals("P0001-W03-04T00:00:00", ISOPeriodFormat.getInstance().alternateExtendedWithWeeks().print(DATE_PERIOD));
        assertEquals("P0000-W00-00T05:06:07.008", ISOPeriodFormat.getInstance().alternateExtendedWithWeeks().print(TIME_PERIOD));
    }

}
