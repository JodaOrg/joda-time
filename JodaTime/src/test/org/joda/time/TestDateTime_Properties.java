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
package org.joda.time;

import java.util.Locale;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This class is a Junit unit test for DateTime.
 *
 * @author Stephen Colebourne
 */
public class TestDateTime_Properties extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.getInstance("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.getInstance("Europe/London");
    
    // 1970-06-09
    private long TEST_TIME_NOW =
            (31L + 28L + 31L + 30L + 31L + 9L -1L) * DateTimeConstants.MILLIS_PER_DAY;
            
    // 1970-04-05
    private long TEST_TIME1 =
        (31L + 28L + 31L + 6L -1L) * DateTimeConstants.MILLIS_PER_DAY
        + 12L * DateTimeConstants.MILLIS_PER_HOUR
        + 24L * DateTimeConstants.MILLIS_PER_MINUTE;
        
    // 1971-05-06
    private long TEST_TIME2 =
        (365L + 31L + 28L + 31L + 30L + 7L -1L) * DateTimeConstants.MILLIS_PER_DAY
        + 14L * DateTimeConstants.MILLIS_PER_HOUR
        + 28L * DateTimeConstants.MILLIS_PER_MINUTE;
        
    private DateTimeZone zone = null;
    private Locale locale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestDateTime_Properties.class);
    }

    public TestDateTime_Properties(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        zone = DateTimeZone.getDefault();
        locale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        Locale.setDefault(Locale.UK);
    }

    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(zone);
        Locale.setDefault(locale);
        zone = null;
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetEra() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        assertSame(test.getChronology().era(), test.era().getField());
        assertEquals("era", test.era().getName());
        assertEquals("Property[era]", test.era().toString());
        assertSame(test, test.era().getInstant());
        assertSame(test, test.era().getDateTime());
        assertEquals(1, test.era().get());
        assertEquals("AD", test.era().getAsText());
        assertEquals("ap. J.-C.", test.era().getAsText(Locale.FRENCH));
        assertEquals("AD", test.era().getAsShortText());
        assertEquals("ap. J.-C.", test.era().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().eras(), test.era().getDurationField());
        assertEquals(null, test.era().getRangeDurationField());
        assertEquals(2, test.era().getMaximumTextLength(null));
        assertEquals(9, test.era().getMaximumTextLength(Locale.FRENCH));
        assertEquals(2, test.era().getMaximumShortTextLength(null));
        assertEquals(9, test.era().getMaximumShortTextLength(Locale.FRENCH));
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetYearOfEra() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        assertSame(test.getChronology().yearOfEra(), test.yearOfEra().getField());
        assertEquals("yearOfEra", test.yearOfEra().getName());
        assertEquals("Property[yearOfEra]", test.yearOfEra().toString());
        assertSame(test, test.yearOfEra().getInstant());
        assertSame(test, test.yearOfEra().getDateTime());
        assertEquals(1972, test.yearOfEra().get());
        assertEquals("1972", test.yearOfEra().getAsText());
        assertEquals("1972", test.yearOfEra().getAsText(Locale.FRENCH));
        assertEquals("1972", test.yearOfEra().getAsShortText());
        assertEquals("1972", test.yearOfEra().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().years(), test.yearOfEra().getDurationField());
        assertEquals(null, test.yearOfEra().getRangeDurationField());
        assertEquals(9, test.yearOfEra().getMaximumTextLength(null));
        assertEquals(9, test.yearOfEra().getMaximumShortTextLength(null));
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetCenturyOfEra() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        assertSame(test.getChronology().centuryOfEra(), test.centuryOfEra().getField());
        assertEquals("centuryOfEra", test.centuryOfEra().getName());
        assertEquals("Property[centuryOfEra]", test.centuryOfEra().toString());
        assertSame(test, test.centuryOfEra().getInstant());
        assertSame(test, test.centuryOfEra().getDateTime());
        assertEquals(19, test.centuryOfEra().get());
        assertEquals("19", test.centuryOfEra().getAsText());
        assertEquals("19", test.centuryOfEra().getAsText(Locale.FRENCH));
        assertEquals("19", test.centuryOfEra().getAsShortText());
        assertEquals("19", test.centuryOfEra().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().centuries(), test.centuryOfEra().getDurationField());
        assertEquals(null, test.centuryOfEra().getRangeDurationField());
        assertEquals(7, test.centuryOfEra().getMaximumTextLength(null));
        assertEquals(7, test.centuryOfEra().getMaximumShortTextLength(null));
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetYearOfCentury() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        assertSame(test.getChronology().yearOfCentury(), test.yearOfCentury().getField());
        assertEquals("yearOfCentury", test.yearOfCentury().getName());
        assertEquals("Property[yearOfCentury]", test.yearOfCentury().toString());
        assertSame(test, test.yearOfCentury().getInstant());
        assertSame(test, test.yearOfCentury().getDateTime());
        assertEquals(72, test.yearOfCentury().get());
        assertEquals("72", test.yearOfCentury().getAsText());
        assertEquals("72", test.yearOfCentury().getAsText(Locale.FRENCH));
        assertEquals("72", test.yearOfCentury().getAsShortText());
        assertEquals("72", test.yearOfCentury().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().years(), test.yearOfCentury().getDurationField());
        assertEquals(test.getChronology().centuries(), test.yearOfCentury().getRangeDurationField());
        assertEquals(2, test.yearOfCentury().getMaximumTextLength(null));
        assertEquals(2, test.yearOfCentury().getMaximumShortTextLength(null));
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetYear() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        assertSame(test.getChronology().year(), test.year().getField());
        assertEquals("year", test.year().getName());
        assertEquals("Property[year]", test.year().toString());
        assertSame(test, test.year().getInstant());
        assertSame(test, test.year().getDateTime());
        assertEquals(1972, test.year().get());
        assertEquals("1972", test.year().getAsText());
        assertEquals("1972", test.year().getAsText(Locale.FRENCH));
        assertEquals("1972", test.year().getAsShortText());
        assertEquals("1972", test.year().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().years(), test.year().getDurationField());
        assertEquals(null, test.year().getRangeDurationField());
        assertEquals(9, test.year().getMaximumTextLength(null));
        assertEquals(9, test.year().getMaximumShortTextLength(null));
    }

    public void testPropertyGetMaxMinValuesYear() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        assertEquals(-292275054, test.year().getMinimumValue());
        assertEquals(-292275054, test.year().getMinimumValueOverall());
        assertEquals(292277023, test.year().getMaximumValue());
        assertEquals(292277023, test.year().getMaximumValueOverall());
    }

    public void testPropertyAddYear() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.year().addToCopy(9);
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1981-06-09T00:00:00.000+01:00", copy.toString());
        
        copy = test.year().addToCopy(0);
        assertEquals("1972-06-09T00:00:00.000+01:00", copy.toString());
        
        copy = test.year().addToCopy(292277023 - 1972);
        assertEquals(292277023, copy.getYear());
        
        try {
            test.year().addToCopy(292277023 - 1972 + 1);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        copy = test.year().addToCopy(-1972);
        assertEquals(0, copy.getYear());
        
        copy = test.year().addToCopy(-1973);
        assertEquals(-1, copy.getYear());
        
        try {
            test.year().addToCopy(-292275054 - 1972 - 1);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testPropertyAddInFieldYear() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.year().addWrappedToCopy(9);
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1981-06-09T00:00:00.000+01:00", copy.toString());
        
        copy = test.year().addWrappedToCopy(0);
        assertEquals(1972, copy.getYear());
        
        copy = test.year().addWrappedToCopy(292277023 - 1972 + 1);
        assertEquals(-292275054, copy.getYear());
        
        copy = test.year().addWrappedToCopy(-292275054 - 1972 - 1);
        assertEquals(292277023, copy.getYear());
    }

    public void testPropertySetYear() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.year().setCopy(1960);
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1960-06-09T00:00:00.000+01:00", copy.toString());
    }

    public void testPropertySetTextYear() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.year().setCopy("1960");
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1960-06-09T00:00:00.000+01:00", copy.toString());
    }

    public void testPropertyCompareToYear() {
        DateTime test1 = new DateTime(TEST_TIME1);
        DateTime test2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.year().compareTo(test2) < 0);
        assertEquals(true, test2.year().compareTo(test1) > 0);
        assertEquals(true, test1.year().compareTo(test1) == 0);
        try {
            test1.year().compareTo(null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetMonth() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        assertSame(test.getChronology().monthOfYear(), test.monthOfYear().getField());
        assertEquals("monthOfYear", test.monthOfYear().getName());
        assertEquals("Property[monthOfYear]", test.monthOfYear().toString());
        assertSame(test, test.monthOfYear().getInstant());
        assertSame(test, test.monthOfYear().getDateTime());
        assertEquals(6, test.monthOfYear().get());
        assertEquals("June", test.monthOfYear().getAsText());
        assertEquals("juin", test.monthOfYear().getAsText(Locale.FRENCH));
        assertEquals("Jun", test.monthOfYear().getAsShortText());
        assertEquals("juin", test.monthOfYear().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().months(), test.monthOfYear().getDurationField());
        assertEquals(test.getChronology().years(), test.monthOfYear().getRangeDurationField());
        assertEquals(9, test.monthOfYear().getMaximumTextLength(null));
        assertEquals(3, test.monthOfYear().getMaximumShortTextLength(null));
        test = new DateTime(1972, 7, 9, 0, 0, 0, 0);
        assertEquals("juillet", test.monthOfYear().getAsText(Locale.FRENCH));
        assertEquals("juil.", test.monthOfYear().getAsShortText(Locale.FRENCH));
        assertEquals(1, test.monthOfYear().getMinimumValue());
        assertEquals(1, test.monthOfYear().getMinimumValueOverall());
        assertEquals(12, test.monthOfYear().getMaximumValue());
        assertEquals(12, test.monthOfYear().getMaximumValueOverall());
    }

    public void testPropertyGetMaxMinValuesMonth() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        assertEquals(1, test.monthOfYear().getMinimumValue());
        assertEquals(1, test.monthOfYear().getMinimumValueOverall());
        assertEquals(12, test.monthOfYear().getMaximumValue());
        assertEquals(12, test.monthOfYear().getMaximumValueOverall());
    }

    public void testPropertyAddMonth() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.monthOfYear().addToCopy(6);
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1972-12-09T00:00:00.000Z", copy.toString());
        
        copy = test.monthOfYear().addToCopy(7);
        assertEquals("1973-01-09T00:00:00.000Z", copy.toString());
        
        copy = test.monthOfYear().addToCopy(-5);
        assertEquals("1972-01-09T00:00:00.000Z", copy.toString());
        
        copy = test.monthOfYear().addToCopy(-6);
        assertEquals("1971-12-09T00:00:00.000Z", copy.toString());
        
        test = new DateTime(1972, 1, 31, 0, 0, 0, 0);
        copy = test.monthOfYear().addToCopy(1);
        assertEquals("1972-01-31T00:00:00.000Z", test.toString());
        assertEquals("1972-02-29T00:00:00.000Z", copy.toString());
        
        copy = test.monthOfYear().addToCopy(2);
        assertEquals("1972-03-31T00:00:00.000Z", copy.toString());
        
        copy = test.monthOfYear().addToCopy(3);
        assertEquals("1972-04-30T00:00:00.000+01:00", copy.toString());
        
        test = new DateTime(1971, 1, 31, 0, 0, 0, 0);
        copy = test.monthOfYear().addToCopy(1);
        assertEquals("1971-02-28T00:00:00.000Z", copy.toString());
    }

    public void testPropertyAddInFieldMonth() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.monthOfYear().addWrappedToCopy(4);
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1972-10-09T00:00:00.000+01:00", copy.toString());
        
        copy = test.monthOfYear().addWrappedToCopy(8);
        assertEquals("1972-02-09T00:00:00.000Z", copy.toString());
        
        copy = test.monthOfYear().addWrappedToCopy(-8);
        assertEquals("1972-10-09T00:00:00.000+01:00", copy.toString());
        
        test = new DateTime(1972, 1, 31, 0, 0, 0, 0);
        copy = test.monthOfYear().addWrappedToCopy(1);
        assertEquals("1972-01-31T00:00:00.000Z", test.toString());
        assertEquals("1972-02-29T00:00:00.000Z", copy.toString());
        
        copy = test.monthOfYear().addWrappedToCopy(2);
        assertEquals("1972-03-31T00:00:00.000+01:00", copy.toString());
        
        copy = test.monthOfYear().addWrappedToCopy(3);
        assertEquals("1972-04-30T00:00:00.000+01:00", copy.toString());
        
        test = new DateTime(1971, 1, 31, 0, 0, 0, 0);
        copy = test.monthOfYear().addWrappedToCopy(1);
        assertEquals("1971-01-31T00:00:00.000Z", test.toString());
        assertEquals("1971-02-28T00:00:00.000Z", copy.toString());
    }

    public void testPropertySetMonth() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.monthOfYear().setCopy(12);
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1972-12-09T00:00:00.000Z", copy.toString());
        
        test = new DateTime(1972, 1, 31, 0, 0, 0, 0);
        copy = test.monthOfYear().setCopy(2);
        assertEquals("1972-02-29T00:00:00.000Z", copy.toString());
        
        try {
            test.monthOfYear().setCopy(13);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.monthOfYear().setCopy(0);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testPropertySetTextMonth() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.monthOfYear().setCopy("12");
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1972-12-09T00:00:00.000Z", copy.toString());
        
        copy = test.monthOfYear().setCopy("December");
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1972-12-09T00:00:00.000Z", copy.toString());
        
        copy = test.monthOfYear().setCopy("Dec");
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1972-12-09T00:00:00.000Z", copy.toString());
    }

    public void testPropertyCompareToMonth() {
        DateTime test1 = new DateTime(TEST_TIME1);
        DateTime test2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.monthOfYear().compareTo(test2) < 0);
        assertEquals(true, test2.monthOfYear().compareTo(test1) > 0);
        assertEquals(true, test1.monthOfYear().compareTo(test1) == 0);
        try {
            test1.monthOfYear().compareTo((ReadableInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        DateTime dt1 = new DateTime(TEST_TIME1);
        DateTime dt2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.monthOfYear().compareTo(dt2) < 0);
        assertEquals(true, test2.monthOfYear().compareTo(dt1) > 0);
        assertEquals(true, test1.monthOfYear().compareTo(dt1) == 0);
        try {
            test1.monthOfYear().compareTo((ReadableInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetDay() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        assertSame(test.getChronology().dayOfMonth(), test.dayOfMonth().getField());
        assertEquals("dayOfMonth", test.dayOfMonth().getName());
        assertEquals("Property[dayOfMonth]", test.dayOfMonth().toString());
        assertSame(test, test.dayOfMonth().getInstant());
        assertSame(test, test.dayOfMonth().getDateTime());
        assertEquals(9, test.dayOfMonth().get());
        assertEquals("9", test.dayOfMonth().getAsText());
        assertEquals("9", test.dayOfMonth().getAsText(Locale.FRENCH));
        assertEquals("9", test.dayOfMonth().getAsShortText());
        assertEquals("9", test.dayOfMonth().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().days(), test.dayOfMonth().getDurationField());
        assertEquals(test.getChronology().months(), test.dayOfMonth().getRangeDurationField());
        assertEquals(2, test.dayOfMonth().getMaximumTextLength(null));
        assertEquals(2, test.dayOfMonth().getMaximumShortTextLength(null));
        assertEquals(1, test.dayOfMonth().getMinimumValue());
        assertEquals(1, test.dayOfMonth().getMinimumValueOverall());
        assertEquals(30, test.dayOfMonth().getMaximumValue());
        assertEquals(31, test.dayOfMonth().getMaximumValueOverall());
    }

    public void testPropertyGetMaxMinValuesDay() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        assertEquals(1, test.dayOfMonth().getMinimumValue());
        assertEquals(1, test.dayOfMonth().getMinimumValueOverall());
        assertEquals(30, test.dayOfMonth().getMaximumValue());
        assertEquals(31, test.dayOfMonth().getMaximumValueOverall());
        test = new DateTime(1972, 7, 9, 0, 0, 0, 0);
        assertEquals(31, test.dayOfMonth().getMaximumValue());
        test = new DateTime(1972, 2, 9, 0, 0, 0, 0);
        assertEquals(29, test.dayOfMonth().getMaximumValue());
        test = new DateTime(1971, 2, 9, 0, 0, 0, 0);
        assertEquals(28, test.dayOfMonth().getMaximumValue());
    }

    public void testPropertyAddDay() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.dayOfMonth().addToCopy(9);
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1972-06-18T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfMonth().addToCopy(21);
        assertEquals("1972-06-30T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfMonth().addToCopy(22);
        assertEquals("1972-07-01T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfMonth().addToCopy(22 + 30);
        assertEquals("1972-07-31T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfMonth().addToCopy(22 + 31);
        assertEquals("1972-08-01T00:00:00.000+01:00", copy.toString());

        copy = test.dayOfMonth().addToCopy(21 + 31 + 31 + 30 + 31 + 30 + 31);
        assertEquals("1972-12-31T00:00:00.000Z", copy.toString());
        
        copy = test.dayOfMonth().addToCopy(22 + 31 + 31 + 30 + 31 + 30 + 31);
        assertEquals("1973-01-01T00:00:00.000Z", copy.toString());
        
        copy = test.dayOfMonth().addToCopy(-8);
        assertEquals("1972-06-01T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfMonth().addToCopy(-9);
        assertEquals("1972-05-31T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfMonth().addToCopy(-8 - 31 - 30 - 31 - 29 - 31);
        assertEquals("1972-01-01T00:00:00.000Z", copy.toString());
        
        copy = test.dayOfMonth().addToCopy(-9 - 31 - 30 - 31 - 29 - 31);
        assertEquals("1971-12-31T00:00:00.000Z", copy.toString());
    }

    public void testPropertyAddInFieldDay() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.dayOfMonth().addWrappedToCopy(21);
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1972-06-30T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfMonth().addWrappedToCopy(22);
        assertEquals("1972-06-01T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfMonth().addWrappedToCopy(-12);
        assertEquals("1972-06-27T00:00:00.000+01:00", copy.toString());
        
        test = new DateTime(1972, 7, 9, 0, 0, 0, 0);
        copy = test.dayOfMonth().addWrappedToCopy(21);
        assertEquals("1972-07-30T00:00:00.000+01:00", copy.toString());
    
        copy = test.dayOfMonth().addWrappedToCopy(22);
        assertEquals("1972-07-31T00:00:00.000+01:00", copy.toString());
    
        copy = test.dayOfMonth().addWrappedToCopy(23);
        assertEquals("1972-07-01T00:00:00.000+01:00", copy.toString());
    
        copy = test.dayOfMonth().addWrappedToCopy(-12);
        assertEquals("1972-07-28T00:00:00.000+01:00", copy.toString());
    }

    public void testPropertySetDay() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.dayOfMonth().setCopy(12);
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1972-06-12T00:00:00.000+01:00", copy.toString());
        
        try {
            test.dayOfMonth().setCopy(31);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.dayOfMonth().setCopy(0);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testPropertySetTextDay() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.dayOfMonth().setCopy("12");
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1972-06-12T00:00:00.000+01:00", copy.toString());
    }

    public void testPropertyCompareToDay() {
        DateTime test1 = new DateTime(TEST_TIME1);
        DateTime test2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.dayOfMonth().compareTo(test2) < 0);
        assertEquals(true, test2.dayOfMonth().compareTo(test1) > 0);
        assertEquals(true, test1.dayOfMonth().compareTo(test1) == 0);
        try {
            test1.dayOfMonth().compareTo((ReadableInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        DateTime dt1 = new DateTime(TEST_TIME1);
        DateTime dt2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.dayOfMonth().compareTo(dt2) < 0);
        assertEquals(true, test2.dayOfMonth().compareTo(dt1) > 0);
        assertEquals(true, test1.dayOfMonth().compareTo(dt1) == 0);
        try {
            test1.dayOfMonth().compareTo((ReadableInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

}
