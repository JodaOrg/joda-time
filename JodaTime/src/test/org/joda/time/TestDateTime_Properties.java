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
    // Summer time:
    // 1968-02-18 to 1971-10-31 - +01:00 all year round!
    // 1972  UK  Mar 19 - Oct 29
    // 1973  UK  Mar 18 - Oct 28

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
        assertSame(test, test.era().getReadableInstant());
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
        assertSame(test, test.yearOfEra().getReadableInstant());
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
        assertSame(test, test.centuryOfEra().getReadableInstant());
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
        assertSame(test, test.yearOfCentury().getReadableInstant());
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
        assertSame(test, test.year().getReadableInstant());
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
        assertEquals(-292275054, test.year().getMinimumValue());
        assertEquals(-292275054, test.year().getMinimumValueOverall());
        assertEquals(292277023, test.year().getMaximumValue());
        assertEquals(292277023, test.year().getMaximumValueOverall());
    }

    public void testPropertyLeapYear() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        assertEquals(true, test.year().isLeap());
        assertEquals(1, test.year().getLeapAmount());
        assertEquals(test.getChronology().days(), test.year().getLeapDurationField());
        test = new DateTime(1971, 6, 9, 0, 0, 0, 0);
        assertEquals(false, test.year().isLeap());
        assertEquals(0, test.year().getLeapAmount());
        assertEquals(test.getChronology().days(), test.year().getLeapDurationField());
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

    public void testPropertyAddWrapFieldYear() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.year().addWrapFieldToCopy(9);
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1981-06-09T00:00:00.000+01:00", copy.toString());
        
        copy = test.year().addWrapFieldToCopy(0);
        assertEquals(1972, copy.getYear());
        
        copy = test.year().addWrapFieldToCopy(292277023 - 1972 + 1);
        assertEquals(-292275054, copy.getYear());
        
        copy = test.year().addWrapFieldToCopy(-292275054 - 1972 - 1);
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
    public void testPropertyGetMonthOfYear() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        assertSame(test.getChronology().monthOfYear(), test.monthOfYear().getField());
        assertEquals("monthOfYear", test.monthOfYear().getName());
        assertEquals("Property[monthOfYear]", test.monthOfYear().toString());
        assertSame(test, test.monthOfYear().getReadableInstant());
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
        assertEquals(1, test.monthOfYear().getMinimumValue());
        assertEquals(1, test.monthOfYear().getMinimumValueOverall());
        assertEquals(12, test.monthOfYear().getMaximumValue());
        assertEquals(12, test.monthOfYear().getMaximumValueOverall());
    }

    public void testPropertyLeapMonthOfYear() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        assertEquals(false, test.monthOfYear().isLeap());
        assertEquals(0, test.monthOfYear().getLeapAmount());
        assertEquals(test.getChronology().days(), test.monthOfYear().getLeapDurationField());
        
        test = new DateTime(1972, 2, 9, 0, 0, 0, 0);
        assertEquals(true, test.monthOfYear().isLeap());
        assertEquals(1, test.monthOfYear().getLeapAmount());
        assertEquals(test.getChronology().days(), test.monthOfYear().getLeapDurationField());
        
        test = new DateTime(1971, 6, 9, 0, 0, 0, 0);
        assertEquals(false, test.monthOfYear().isLeap());
        assertEquals(0, test.monthOfYear().getLeapAmount());
        assertEquals(test.getChronology().days(), test.monthOfYear().getLeapDurationField());
        
        test = new DateTime(1971, 2, 9, 0, 0, 0, 0);
        assertEquals(false, test.monthOfYear().isLeap());
        assertEquals(0, test.monthOfYear().getLeapAmount());
        assertEquals(test.getChronology().days(), test.monthOfYear().getLeapDurationField());
    }

    public void testPropertyAddMonthOfYear() {
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
        assertEquals("1972-03-31T00:00:00.000+01:00", copy.toString());
        
        copy = test.monthOfYear().addToCopy(3);
        assertEquals("1972-04-30T00:00:00.000+01:00", copy.toString());
        
        test = new DateTime(1971, 1, 31, 0, 0, 0, 0);
        copy = test.monthOfYear().addToCopy(1);
        assertEquals("1971-02-28T00:00:00.000+01:00", copy.toString());
    }

    public void testPropertyAddWrapFieldMonthOfYear() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.monthOfYear().addWrapFieldToCopy(4);
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1972-10-09T00:00:00.000+01:00", copy.toString());
        
        copy = test.monthOfYear().addWrapFieldToCopy(8);
        assertEquals("1972-02-09T00:00:00.000Z", copy.toString());
        
        copy = test.monthOfYear().addWrapFieldToCopy(-8);
        assertEquals("1972-10-09T00:00:00.000+01:00", copy.toString());
        
        test = new DateTime(1972, 1, 31, 0, 0, 0, 0);
        copy = test.monthOfYear().addWrapFieldToCopy(1);
        assertEquals("1972-01-31T00:00:00.000Z", test.toString());
        assertEquals("1972-02-29T00:00:00.000Z", copy.toString());
        
        copy = test.monthOfYear().addWrapFieldToCopy(2);
        assertEquals("1972-03-31T00:00:00.000+01:00", copy.toString());
        
        copy = test.monthOfYear().addWrapFieldToCopy(3);
        assertEquals("1972-04-30T00:00:00.000+01:00", copy.toString());
        
        test = new DateTime(1973, 1, 31, 0, 0, 0, 0);
        copy = test.monthOfYear().addWrapFieldToCopy(1);
        assertEquals("1973-01-31T00:00:00.000Z", test.toString());
        assertEquals("1973-02-28T00:00:00.000Z", copy.toString());
    }

    public void testPropertySetMonthOfYear() {
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

    public void testPropertySetTextMonthOfYear() {
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

    public void testPropertyCompareToMonthOfYear() {
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
    public void testPropertyGetDayOfMonth() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        assertSame(test.getChronology().dayOfMonth(), test.dayOfMonth().getField());
        assertEquals("dayOfMonth", test.dayOfMonth().getName());
        assertEquals("Property[dayOfMonth]", test.dayOfMonth().toString());
        assertSame(test, test.dayOfMonth().getReadableInstant());
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
        assertEquals(false, test.dayOfMonth().isLeap());
        assertEquals(0, test.dayOfMonth().getLeapAmount());
        assertEquals(null, test.dayOfMonth().getLeapDurationField());
    }

    public void testPropertyGetMaxMinValuesDayOfMonth() {
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

    public void testPropertyAddDayOfMonth() {
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

    public void testPropertyAddWrapFieldDayOfMonth() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.dayOfMonth().addWrapFieldToCopy(21);
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1972-06-30T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfMonth().addWrapFieldToCopy(22);
        assertEquals("1972-06-01T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfMonth().addWrapFieldToCopy(-12);
        assertEquals("1972-06-27T00:00:00.000+01:00", copy.toString());
        
        test = new DateTime(1972, 7, 9, 0, 0, 0, 0);
        copy = test.dayOfMonth().addWrapFieldToCopy(21);
        assertEquals("1972-07-30T00:00:00.000+01:00", copy.toString());
    
        copy = test.dayOfMonth().addWrapFieldToCopy(22);
        assertEquals("1972-07-31T00:00:00.000+01:00", copy.toString());
    
        copy = test.dayOfMonth().addWrapFieldToCopy(23);
        assertEquals("1972-07-01T00:00:00.000+01:00", copy.toString());
    
        copy = test.dayOfMonth().addWrapFieldToCopy(-12);
        assertEquals("1972-07-28T00:00:00.000+01:00", copy.toString());
    }

    public void testPropertySetDayOfMonth() {
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

    public void testPropertySetTextDayOfMonth() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.dayOfMonth().setCopy("12");
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1972-06-12T00:00:00.000+01:00", copy.toString());
    }

    public void testPropertyCompareToDayOfMonth() {
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

    //-----------------------------------------------------------------------
    public void testPropertyGetDayOfYear() {
        // 31+29+31+30+31+9 = 161
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        assertSame(test.getChronology().dayOfYear(), test.dayOfYear().getField());
        assertEquals("dayOfYear", test.dayOfYear().getName());
        assertEquals("Property[dayOfYear]", test.dayOfYear().toString());
        assertSame(test, test.dayOfYear().getReadableInstant());
        assertSame(test, test.dayOfYear().getDateTime());
        assertEquals(161, test.dayOfYear().get());
        assertEquals("161", test.dayOfYear().getAsText());
        assertEquals("161", test.dayOfYear().getAsText(Locale.FRENCH));
        assertEquals("161", test.dayOfYear().getAsShortText());
        assertEquals("161", test.dayOfYear().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().days(), test.dayOfYear().getDurationField());
        assertEquals(test.getChronology().years(), test.dayOfYear().getRangeDurationField());
        assertEquals(3, test.dayOfYear().getMaximumTextLength(null));
        assertEquals(3, test.dayOfYear().getMaximumShortTextLength(null));
        assertEquals(false, test.dayOfYear().isLeap());
        assertEquals(0, test.dayOfYear().getLeapAmount());
        assertEquals(null, test.dayOfYear().getLeapDurationField());
    }

    public void testPropertyGetMaxMinValuesDayOfYear() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        assertEquals(1, test.dayOfYear().getMinimumValue());
        assertEquals(1, test.dayOfYear().getMinimumValueOverall());
        assertEquals(366, test.dayOfYear().getMaximumValue());
        assertEquals(366, test.dayOfYear().getMaximumValueOverall());
        test = new DateTime(1970, 6, 9, 0, 0, 0, 0);
        assertEquals(365, test.dayOfYear().getMaximumValue());
        assertEquals(366, test.dayOfYear().getMaximumValueOverall());
    }

    public void testPropertyAddDayOfYear() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.dayOfYear().addToCopy(9);
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1972-06-18T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfYear().addToCopy(21);
        assertEquals("1972-06-30T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfYear().addToCopy(22);
        assertEquals("1972-07-01T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfYear().addToCopy(21 + 31 + 31 + 30 + 31 + 30 + 31);
        assertEquals("1972-12-31T00:00:00.000Z", copy.toString());
        
        copy = test.dayOfYear().addToCopy(22 + 31 + 31 + 30 + 31 + 30 + 31);
        assertEquals("1973-01-01T00:00:00.000Z", copy.toString());
        
        copy = test.dayOfYear().addToCopy(-8);
        assertEquals("1972-06-01T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfYear().addToCopy(-9);
        assertEquals("1972-05-31T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfYear().addToCopy(-8 - 31 - 30 - 31 - 29 - 31);
        assertEquals("1972-01-01T00:00:00.000Z", copy.toString());
        
        copy = test.dayOfYear().addToCopy(-9 - 31 - 30 - 31 - 29 - 31);
        assertEquals("1971-12-31T00:00:00.000Z", copy.toString());
    }

    public void testPropertyAddWrapFieldDayOfYear() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.dayOfYear().addWrapFieldToCopy(21);
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1972-06-30T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfYear().addWrapFieldToCopy(22);
        assertEquals("1972-07-01T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfYear().addWrapFieldToCopy(-12);
        assertEquals("1972-05-28T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfYear().addWrapFieldToCopy(205);
        assertEquals("1972-12-31T00:00:00.000Z", copy.toString());
        
        copy = test.dayOfYear().addWrapFieldToCopy(206);
        assertEquals("1972-01-01T00:00:00.000Z", copy.toString());
        
        copy = test.dayOfYear().addWrapFieldToCopy(-160);
        assertEquals("1972-01-01T00:00:00.000Z", copy.toString());
        
        copy = test.dayOfYear().addWrapFieldToCopy(-161);
        assertEquals("1972-12-31T00:00:00.000Z", copy.toString());
    }

    public void testPropertySetDayOfYear() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.dayOfYear().setCopy(12);
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1972-01-12T00:00:00.000Z", copy.toString());
        
        try {
            test.dayOfYear().setCopy(367);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.dayOfYear().setCopy(0);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testPropertySetTextDayOfYear() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.dayOfYear().setCopy("12");
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1972-01-12T00:00:00.000Z", copy.toString());
    }

    public void testPropertyCompareToDayOfYear() {
        DateTime test1 = new DateTime(TEST_TIME1);
        DateTime test2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.dayOfYear().compareTo(test2) < 0);
        assertEquals(true, test2.dayOfYear().compareTo(test1) > 0);
        assertEquals(true, test1.dayOfYear().compareTo(test1) == 0);
        try {
            test1.dayOfYear().compareTo((ReadableInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        DateTime dt1 = new DateTime(TEST_TIME1);
        DateTime dt2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.dayOfYear().compareTo(dt2) < 0);
        assertEquals(true, test2.dayOfYear().compareTo(dt1) > 0);
        assertEquals(true, test1.dayOfYear().compareTo(dt1) == 0);
        try {
            test1.dayOfYear().compareTo((ReadableInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetWeekOfWeekyear() {
        // 1970-01-01 = Thu
        // 1970-12-31 = Thu (+364 days)
        // 1971-12-30 = Thu (+364 days)
        // 1972-01-03 = Mon             W1
        // 1972-01-31 = Mon (+28 days)  W5
        // 1972-02-28 = Mon (+28 days)  W9
        // 1972-03-27 = Mon (+28 days)  W13
        // 1972-04-24 = Mon (+28 days)  W17
        // 1972-05-23 = Mon (+28 days)  W21
        // 1972-06-05 = Mon (+14 days)  W23
        // 1972-06-09 = Fri
        // 1972-12-25 = Mon             W52
        // 1973-01-01 = Mon             W1
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        assertSame(test.getChronology().weekOfWeekyear(), test.weekOfWeekyear().getField());
        assertEquals("weekOfWeekyear", test.weekOfWeekyear().getName());
        assertEquals("Property[weekOfWeekyear]", test.weekOfWeekyear().toString());
        assertSame(test, test.weekOfWeekyear().getReadableInstant());
        assertSame(test, test.weekOfWeekyear().getDateTime());
        assertEquals(23, test.weekOfWeekyear().get());
        assertEquals("23", test.weekOfWeekyear().getAsText());
        assertEquals("23", test.weekOfWeekyear().getAsText(Locale.FRENCH));
        assertEquals("23", test.weekOfWeekyear().getAsShortText());
        assertEquals("23", test.weekOfWeekyear().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().weeks(), test.weekOfWeekyear().getDurationField());
        assertEquals(test.getChronology().weekyears(), test.weekOfWeekyear().getRangeDurationField());
        assertEquals(2, test.weekOfWeekyear().getMaximumTextLength(null));
        assertEquals(2, test.weekOfWeekyear().getMaximumShortTextLength(null));
        assertEquals(false, test.weekOfWeekyear().isLeap());
        assertEquals(0, test.weekOfWeekyear().getLeapAmount());
        assertEquals(null, test.weekOfWeekyear().getLeapDurationField());
    }

    public void testPropertyGetMaxMinValuesWeekOfWeekyear() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        assertEquals(1, test.weekOfWeekyear().getMinimumValue());
        assertEquals(1, test.weekOfWeekyear().getMinimumValueOverall());
        assertEquals(52, test.weekOfWeekyear().getMaximumValue());
        assertEquals(53, test.weekOfWeekyear().getMaximumValueOverall());
        test = new DateTime(1970, 6, 9, 0, 0, 0, 0);
        assertEquals(53, test.weekOfWeekyear().getMaximumValue());
        assertEquals(53, test.weekOfWeekyear().getMaximumValueOverall());
    }

    public void testPropertyAddWeekOfWeekyear() {
        DateTime test = new DateTime(1972, 6, 5, 0, 0, 0, 0);
        DateTime copy = test.weekOfWeekyear().addToCopy(1);
        assertEquals("1972-06-05T00:00:00.000+01:00", test.toString());
        assertEquals("1972-06-12T00:00:00.000+01:00", copy.toString());
        
        copy = test.weekOfWeekyear().addToCopy(29);
        assertEquals("1972-12-25T00:00:00.000Z", copy.toString());
        
        copy = test.weekOfWeekyear().addToCopy(30);
        assertEquals("1973-01-01T00:00:00.000Z", copy.toString());
        
        copy = test.weekOfWeekyear().addToCopy(-22);
        assertEquals("1972-01-03T00:00:00.000Z", copy.toString());
        
        copy = test.weekOfWeekyear().addToCopy(-23);
        assertEquals("1971-12-27T00:00:00.000Z", copy.toString());
    }

    public void testPropertyAddWrapFieldWeekOfWeekyear() {
        DateTime test = new DateTime(1972, 6, 5, 0, 0, 0, 0);
        DateTime copy = test.weekOfWeekyear().addWrapFieldToCopy(1);
        assertEquals("1972-06-05T00:00:00.000+01:00", test.toString());
        assertEquals("1972-06-12T00:00:00.000+01:00", copy.toString());
        
        copy = test.weekOfWeekyear().addWrapFieldToCopy(29);
        assertEquals("1972-12-25T00:00:00.000Z", copy.toString());
        
        copy = test.weekOfWeekyear().addWrapFieldToCopy(30);
        assertEquals("1972-01-03T00:00:00.000Z", copy.toString());
        
        copy = test.weekOfWeekyear().addWrapFieldToCopy(-23);
        assertEquals("1972-12-25T00:00:00.000Z", copy.toString());
    }

    public void testPropertySetWeekOfWeekyear() {
        DateTime test = new DateTime(1972, 6, 5, 0, 0, 0, 0);
        DateTime copy = test.weekOfWeekyear().setCopy(4);
        assertEquals("1972-06-05T00:00:00.000+01:00", test.toString());
        assertEquals("1972-01-24T00:00:00.000Z", copy.toString());
        
        try {
            test.weekOfWeekyear().setCopy(53);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.weekOfWeekyear().setCopy(0);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testPropertySetTextWeekOfWeekyear() {
        DateTime test = new DateTime(1972, 6, 5, 0, 0, 0, 0);
        DateTime copy = test.weekOfWeekyear().setCopy("4");
        assertEquals("1972-06-05T00:00:00.000+01:00", test.toString());
        assertEquals("1972-01-24T00:00:00.000Z", copy.toString());
    }

    public void testPropertyCompareToWeekOfWeekyear() {
        DateTime test1 = new DateTime(TEST_TIME1);
        DateTime test2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.weekOfWeekyear().compareTo(test2) < 0);
        assertEquals(true, test2.weekOfWeekyear().compareTo(test1) > 0);
        assertEquals(true, test1.weekOfWeekyear().compareTo(test1) == 0);
        try {
            test1.weekOfWeekyear().compareTo((ReadableInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        DateTime dt1 = new DateTime(TEST_TIME1);
        DateTime dt2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.weekOfWeekyear().compareTo(dt2) < 0);
        assertEquals(true, test2.weekOfWeekyear().compareTo(dt1) > 0);
        assertEquals(true, test1.weekOfWeekyear().compareTo(dt1) == 0);
        try {
            test1.weekOfWeekyear().compareTo((ReadableInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetDayOfWeek() {
        // 1970-01-01 = Thu
        // 1970-12-31 = Thu (+364 days)
        // 1971-12-30 = Thu (+364 days)
        // 1972-01-03 = Mon
        // 1972-01-31 = Mon (+28 days)
        // 1972-02-28 = Mon (+28 days)
        // 1972-03-27 = Mon (+28 days)
        // 1972-04-24 = Mon (+28 days)
        // 1972-05-23 = Mon (+28 days)
        // 1972-06-05 = Mon (+14 days)
        // 1972-06-09 = Fri
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        assertSame(test.getChronology().dayOfWeek(), test.dayOfWeek().getField());
        assertEquals("dayOfWeek", test.dayOfWeek().getName());
        assertEquals("Property[dayOfWeek]", test.dayOfWeek().toString());
        assertSame(test, test.dayOfWeek().getReadableInstant());
        assertSame(test, test.dayOfWeek().getDateTime());
        assertEquals(5, test.dayOfWeek().get());
        assertEquals("Friday", test.dayOfWeek().getAsText());
        assertEquals("vendredi", test.dayOfWeek().getAsText(Locale.FRENCH));
        assertEquals("Fri", test.dayOfWeek().getAsShortText());
        assertEquals("ven.", test.dayOfWeek().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().days(), test.dayOfWeek().getDurationField());
        assertEquals(test.getChronology().weeks(), test.dayOfWeek().getRangeDurationField());
        assertEquals(9, test.dayOfWeek().getMaximumTextLength(null));
        assertEquals(8, test.dayOfWeek().getMaximumTextLength(Locale.FRENCH));
        assertEquals(3, test.dayOfWeek().getMaximumShortTextLength(null));
        assertEquals(4, test.dayOfWeek().getMaximumShortTextLength(Locale.FRENCH));
        assertEquals(1, test.dayOfWeek().getMinimumValue());
        assertEquals(1, test.dayOfWeek().getMinimumValueOverall());
        assertEquals(7, test.dayOfWeek().getMaximumValue());
        assertEquals(7, test.dayOfWeek().getMaximumValueOverall());
        assertEquals(false, test.dayOfWeek().isLeap());
        assertEquals(0, test.dayOfWeek().getLeapAmount());
        assertEquals(null, test.dayOfWeek().getLeapDurationField());
    }

    public void testPropertyAddDayOfWeek() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.dayOfWeek().addToCopy(1);
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1972-06-10T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfWeek().addToCopy(21);
        assertEquals("1972-06-30T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfWeek().addToCopy(22);
        assertEquals("1972-07-01T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfWeek().addToCopy(21 + 31 + 31 + 30 + 31 + 30 + 31);
        assertEquals("1972-12-31T00:00:00.000Z", copy.toString());
        
        copy = test.dayOfWeek().addToCopy(22 + 31 + 31 + 30 + 31 + 30 + 31);
        assertEquals("1973-01-01T00:00:00.000Z", copy.toString());
        
        copy = test.dayOfWeek().addToCopy(-8);
        assertEquals("1972-06-01T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfWeek().addToCopy(-9);
        assertEquals("1972-05-31T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfWeek().addToCopy(-8 - 31 - 30 - 31 - 29 - 31);
        assertEquals("1972-01-01T00:00:00.000Z", copy.toString());
        
        copy = test.dayOfWeek().addToCopy(-9 - 31 - 30 - 31 - 29 - 31);
        assertEquals("1971-12-31T00:00:00.000Z", copy.toString());
    }

    public void testPropertyAddWrapFieldDayOfWeek() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.dayOfWeek().addWrapFieldToCopy(1);
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1972-06-10T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfWeek().addWrapFieldToCopy(3);
        assertEquals("1972-06-05T00:00:00.000+01:00", copy.toString());
        
        copy = test.dayOfWeek().addWrapFieldToCopy(-12);
        assertEquals("1972-06-11T00:00:00.000+01:00", copy.toString());
        
        test = new DateTime(1972, 6, 2, 0, 0, 0, 0);
        copy = test.dayOfWeek().addWrapFieldToCopy(3);
        assertEquals("1972-06-02T00:00:00.000+01:00", test.toString());
        assertEquals("1972-05-29T00:00:00.000+01:00", copy.toString());
    }

    public void testPropertySetDayOfWeek() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.dayOfWeek().setCopy(4);
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1972-06-08T00:00:00.000+01:00", copy.toString());
        
        try {
            test.dayOfWeek().setCopy(8);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.dayOfWeek().setCopy(0);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testPropertySetTextDayOfWeek() {
        DateTime test = new DateTime(1972, 6, 9, 0, 0, 0, 0);
        DateTime copy = test.dayOfWeek().setCopy("4");
        assertEquals("1972-06-09T00:00:00.000+01:00", test.toString());
        assertEquals("1972-06-08T00:00:00.000+01:00", copy.toString());
        copy = test.dayOfWeek().setCopy("Mon");
        assertEquals("1972-06-05T00:00:00.000+01:00", copy.toString());
        copy = test.dayOfWeek().setCopy("Tuesday");
        assertEquals("1972-06-06T00:00:00.000+01:00", copy.toString());
        copy = test.dayOfWeek().setCopy("lundi", Locale.FRENCH);
        assertEquals("1972-06-05T00:00:00.000+01:00", copy.toString());
    }

    public void testPropertyCompareToDayOfWeek() {
        DateTime test1 = new DateTime(TEST_TIME1);
        DateTime test2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.dayOfWeek().compareTo(test2) < 0);
        assertEquals(true, test2.dayOfWeek().compareTo(test1) > 0);
        assertEquals(true, test1.dayOfWeek().compareTo(test1) == 0);
        try {
            test1.dayOfWeek().compareTo((ReadableInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        DateTime dt1 = new DateTime(TEST_TIME1);
        DateTime dt2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.dayOfWeek().compareTo(dt2) < 0);
        assertEquals(true, test2.dayOfWeek().compareTo(dt1) > 0);
        assertEquals(true, test1.dayOfWeek().compareTo(dt1) == 0);
        try {
            test1.dayOfWeek().compareTo((ReadableInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

}
