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
public class TestISOPeriodFormat extends TestCase {
    
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
