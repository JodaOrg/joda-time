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
public class TestDateMidnight_Properties extends TestCase {
    // Test in 2002/03 as time zones are more well known
    // (before the late 90's they were all over the place)

    private static final DateTimeZone PARIS = DateTimeZone.getInstance("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.getInstance("Europe/London");
    
    long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                     366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                     365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                     366 + 365;
    long y2003days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                     366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                     365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                     366 + 365 + 365;
    
    // 2002-06-09
    private long TEST_TIME_NOW =
            (y2002days + 31L + 28L + 31L + 30L + 31L + 9L -1L) * DateTimeConstants.MILLIS_PER_DAY;
            
    // 2002-04-05 Fri
    private long TEST_TIME1 =
            (y2002days + 31L + 28L + 31L + 5L -1L) * DateTimeConstants.MILLIS_PER_DAY
            + 12L * DateTimeConstants.MILLIS_PER_HOUR
            + 24L * DateTimeConstants.MILLIS_PER_MINUTE;
        
    // 2003-05-06 Tue
    private long TEST_TIME2 =
            (y2003days + 31L + 28L + 31L + 30L + 6L -1L) * DateTimeConstants.MILLIS_PER_DAY
            + 14L * DateTimeConstants.MILLIS_PER_HOUR
            + 28L * DateTimeConstants.MILLIS_PER_MINUTE;
        
    private DateTimeZone zone = null;
    private Locale locale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestDateMidnight_Properties.class);
    }

    public TestDateMidnight_Properties(String name) {
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
    public void testTest() {
        assertEquals("2002-06-09T00:00:00.000Z", new Instant(TEST_TIME_NOW).toString());
        assertEquals("2002-04-05T12:24:00.000Z", new Instant(TEST_TIME1).toString());
        assertEquals("2003-05-06T14:28:00.000Z", new Instant(TEST_TIME2).toString());
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetEra() {
        DateMidnight test = new DateMidnight(2004, 6, 9);
        assertSame(test.getChronology().era(), test.era().getField());
        assertEquals("era", test.era().getName());
        assertEquals("Property[era]", test.era().toString());
        assertSame(test, test.era().getReadableInstant());
        assertSame(test, test.era().getDateMidnight());
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
        DateMidnight test = new DateMidnight(2004, 6, 9);
        assertSame(test.getChronology().yearOfEra(), test.yearOfEra().getField());
        assertEquals("yearOfEra", test.yearOfEra().getName());
        assertEquals("Property[yearOfEra]", test.yearOfEra().toString());
        assertSame(test, test.yearOfEra().getReadableInstant());
        assertSame(test, test.yearOfEra().getDateMidnight());
        assertEquals(2004, test.yearOfEra().get());
        assertEquals("2004", test.yearOfEra().getAsText());
        assertEquals("2004", test.yearOfEra().getAsText(Locale.FRENCH));
        assertEquals("2004", test.yearOfEra().getAsShortText());
        assertEquals("2004", test.yearOfEra().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().years(), test.yearOfEra().getDurationField());
        assertEquals(null, test.yearOfEra().getRangeDurationField());
        assertEquals(9, test.yearOfEra().getMaximumTextLength(null));
        assertEquals(9, test.yearOfEra().getMaximumShortTextLength(null));
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetCenturyOfEra() {
        DateMidnight test = new DateMidnight(2004, 6, 9);
        assertSame(test.getChronology().centuryOfEra(), test.centuryOfEra().getField());
        assertEquals("centuryOfEra", test.centuryOfEra().getName());
        assertEquals("Property[centuryOfEra]", test.centuryOfEra().toString());
        assertSame(test, test.centuryOfEra().getReadableInstant());
        assertSame(test, test.centuryOfEra().getDateMidnight());
        assertEquals(20, test.centuryOfEra().get());
        assertEquals("20", test.centuryOfEra().getAsText());
        assertEquals("20", test.centuryOfEra().getAsText(Locale.FRENCH));
        assertEquals("20", test.centuryOfEra().getAsShortText());
        assertEquals("20", test.centuryOfEra().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().centuries(), test.centuryOfEra().getDurationField());
        assertEquals(null, test.centuryOfEra().getRangeDurationField());
        assertEquals(7, test.centuryOfEra().getMaximumTextLength(null));
        assertEquals(7, test.centuryOfEra().getMaximumShortTextLength(null));
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetYearOfCentury() {
        DateMidnight test = new DateMidnight(2004, 6, 9);
        assertSame(test.getChronology().yearOfCentury(), test.yearOfCentury().getField());
        assertEquals("yearOfCentury", test.yearOfCentury().getName());
        assertEquals("Property[yearOfCentury]", test.yearOfCentury().toString());
        assertSame(test, test.yearOfCentury().getReadableInstant());
        assertSame(test, test.yearOfCentury().getDateMidnight());
        assertEquals(4, test.yearOfCentury().get());
        assertEquals("4", test.yearOfCentury().getAsText());
        assertEquals("4", test.yearOfCentury().getAsText(Locale.FRENCH));
        assertEquals("4", test.yearOfCentury().getAsShortText());
        assertEquals("4", test.yearOfCentury().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().years(), test.yearOfCentury().getDurationField());
        assertEquals(test.getChronology().centuries(), test.yearOfCentury().getRangeDurationField());
        assertEquals(2, test.yearOfCentury().getMaximumTextLength(null));
        assertEquals(2, test.yearOfCentury().getMaximumShortTextLength(null));
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetWeekyear() {
        DateMidnight test = new DateMidnight(2004, 6, 9);
        assertSame(test.getChronology().weekyear(), test.weekyear().getField());
        assertEquals("weekyear", test.weekyear().getName());
        assertEquals("Property[weekyear]", test.weekyear().toString());
        assertSame(test, test.weekyear().getReadableInstant());
        assertSame(test, test.weekyear().getDateMidnight());
        assertEquals(2004, test.weekyear().get());
        assertEquals("2004", test.weekyear().getAsText());
        assertEquals("2004", test.weekyear().getAsText(Locale.FRENCH));
        assertEquals("2004", test.weekyear().getAsShortText());
        assertEquals("2004", test.weekyear().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().weekyears(), test.weekyear().getDurationField());
        assertEquals(null, test.weekyear().getRangeDurationField());
        assertEquals(9, test.weekyear().getMaximumTextLength(null));
        assertEquals(9, test.weekyear().getMaximumShortTextLength(null));
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetYear() {
        DateMidnight test = new DateMidnight(2004, 6, 9);
        assertSame(test.getChronology().year(), test.year().getField());
        assertEquals("year", test.year().getName());
        assertEquals("Property[year]", test.year().toString());
        assertSame(test, test.year().getReadableInstant());
        assertSame(test, test.year().getDateMidnight());
        assertEquals(2004, test.year().get());
        assertEquals("2004", test.year().getAsText());
        assertEquals("2004", test.year().getAsText(Locale.FRENCH));
        assertEquals("2004", test.year().getAsShortText());
        assertEquals("2004", test.year().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().years(), test.year().getDurationField());
        assertEquals(null, test.year().getRangeDurationField());
        assertEquals(9, test.year().getMaximumTextLength(null));
        assertEquals(9, test.year().getMaximumShortTextLength(null));
        assertEquals(-292275054, test.year().getMinimumValue());
        assertEquals(-292275054, test.year().getMinimumValueOverall());
        assertEquals(292277023, test.year().getMaximumValue());
        assertEquals(292277023, test.year().getMaximumValueOverall());
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetMonthOfYear() {
        DateMidnight test = new DateMidnight(2004, 6, 9);
        assertSame(test.getChronology().monthOfYear(), test.monthOfYear().getField());
        assertEquals("monthOfYear", test.monthOfYear().getName());
        assertEquals("Property[monthOfYear]", test.monthOfYear().toString());
        assertSame(test, test.monthOfYear().getReadableInstant());
        assertSame(test, test.monthOfYear().getDateMidnight());
        assertEquals(6, test.monthOfYear().get());
        assertEquals("June", test.monthOfYear().getAsText());
        assertEquals("juin", test.monthOfYear().getAsText(Locale.FRENCH));
        assertEquals("Jun", test.monthOfYear().getAsShortText());
        assertEquals("juin", test.monthOfYear().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().months(), test.monthOfYear().getDurationField());
        assertEquals(test.getChronology().years(), test.monthOfYear().getRangeDurationField());
        assertEquals(9, test.monthOfYear().getMaximumTextLength(null));
        assertEquals(3, test.monthOfYear().getMaximumShortTextLength(null));
        test = new DateMidnight(2004, 7, 9);
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

    public void testPropertySetMonthOfYear() {
        DateMidnight test = new DateMidnight(2004, 6, 9);
        DateMidnight copy = test.monthOfYear().setCopy(8);
        assertEquals(2004, copy.getYear());
        assertEquals(8, copy.getMonthOfYear());
        assertEquals(9, copy.getDayOfMonth());
    }

    public void testPropertySetTextMonthOfYear() {
        DateMidnight test = new DateMidnight(2004, 6, 9);
        DateMidnight copy = test.monthOfYear().setCopy("8");
        assertEquals(2004, copy.getYear());
        assertEquals(8, copy.getMonthOfYear());
        assertEquals(9, copy.getDayOfMonth());
    }

    public void testPropertySetTextLocaleMonthOfYear() {
        DateMidnight test = new DateMidnight(2004, 6, 9);
        DateMidnight copy = test.monthOfYear().setCopy("mars", Locale.FRENCH);
        assertEquals(2004, copy.getYear());
        assertEquals(3, copy.getMonthOfYear());
        assertEquals(9, copy.getDayOfMonth());
    }

    public void testPropertyAddMonthOfYear() {
        DateMidnight test = new DateMidnight(2004, 6, 9);
        DateMidnight copy = test.monthOfYear().addToCopy(8);
        assertEquals(2005, copy.getYear());
        assertEquals(2, copy.getMonthOfYear());
        assertEquals(9, copy.getDayOfMonth());
    }

    public void testPropertyAddLongMonthOfYear() {
        DateMidnight test = new DateMidnight(2004, 6, 9);
        DateMidnight copy = test.monthOfYear().addToCopy(8L);
        assertEquals(2005, copy.getYear());
        assertEquals(2, copy.getMonthOfYear());
        assertEquals(9, copy.getDayOfMonth());
    }

    public void testPropertyAddWrapFieldMonthOfYear() {
        DateMidnight test = new DateMidnight(2004, 6, 9);
        DateMidnight copy = test.monthOfYear().addWrapFieldToCopy(8);
        assertEquals(2004, copy.getYear());
        assertEquals(2, copy.getMonthOfYear());
        assertEquals(9, copy.getDayOfMonth());
    }

    public void testPropertyGetDifferenceMonthOfYear() {
        DateMidnight test1 = new DateMidnight(2004, 6, 9);
        DateMidnight test2 = new DateMidnight(2004, 8, 9);
        assertEquals(-2, test1.monthOfYear().getDifference(test2));
        assertEquals(2, test2.monthOfYear().getDifference(test1));
        assertEquals(-2L, test1.monthOfYear().getDifferenceAsLong(test2));
        assertEquals(2L, test2.monthOfYear().getDifferenceAsLong(test1));
    }

    public void testPropertyRoundFloorMonthOfYear() {
        DateMidnight test = new DateMidnight(2004, 6, 16);
        DateMidnight copy = test.monthOfYear().roundFloorCopy();
        assertEquals("2004-06-01T00:00:00.000+01:00", copy.toString());
    }

    public void testPropertyRoundCeilingMonthOfYear() {
        DateMidnight test = new DateMidnight(2004, 6, 16);
        DateMidnight copy = test.monthOfYear().roundCeilingCopy();
        assertEquals("2004-07-01T00:00:00.000+01:00", copy.toString());
    }

    public void testPropertyRoundHalfFloorMonthOfYear() {
        DateMidnight test = new DateMidnight(2004, 6, 16);
        DateMidnight copy = test.monthOfYear().roundHalfFloorCopy();
        assertEquals("2004-06-01T00:00:00.000+01:00", copy.toString());
        
        test = new DateMidnight(2004, 6, 17);
        copy = test.monthOfYear().roundHalfFloorCopy();
        assertEquals("2004-07-01T00:00:00.000+01:00", copy.toString());
        
        test = new DateMidnight(2004, 6, 15);
        copy = test.monthOfYear().roundHalfFloorCopy();
        assertEquals("2004-06-01T00:00:00.000+01:00", copy.toString());
    }

    public void testPropertyRoundHalfCeilingMonthOfYear() {
        DateMidnight test = new DateMidnight(2004, 6, 16);
        DateMidnight copy = test.monthOfYear().roundHalfCeilingCopy();
        assertEquals("2004-07-01T00:00:00.000+01:00", copy.toString());
        
        test = new DateMidnight(2004, 6, 17);
        copy = test.monthOfYear().roundHalfCeilingCopy();
        assertEquals("2004-07-01T00:00:00.000+01:00", copy.toString());
        
        test = new DateMidnight(2004, 6, 15);
        copy = test.monthOfYear().roundHalfCeilingCopy();
        assertEquals("2004-06-01T00:00:00.000+01:00", copy.toString());
    }

    public void testPropertyRoundHalfEvenMonthOfYear() {
        DateMidnight test = new DateMidnight(2004, 6, 16);
        DateMidnight copy = test.monthOfYear().roundHalfEvenCopy();
        assertEquals("2004-06-01T00:00:00.000+01:00", copy.toString());
        
        test = new DateMidnight(2004, 9, 16);
        copy = test.monthOfYear().roundHalfEvenCopy();
        assertEquals("2004-10-01T00:00:00.000+01:00", copy.toString());
        
        test = new DateMidnight(2004, 6, 17);
        copy = test.monthOfYear().roundHalfEvenCopy();
        assertEquals("2004-07-01T00:00:00.000+01:00", copy.toString());
        
        test = new DateMidnight(2004, 6, 15);
        copy = test.monthOfYear().roundHalfEvenCopy();
        assertEquals("2004-06-01T00:00:00.000+01:00", copy.toString());
    }

    public void testPropertyRemainderMonthOfYear() {
        DateMidnight test = new DateMidnight(2004, 6, 9);
        assertEquals((9L - 1L) * DateTimeConstants.MILLIS_PER_DAY, test.monthOfYear().remainder());
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetDayOfMonth() {
        DateMidnight test = new DateMidnight(2004, 6, 9);
        assertSame(test.getChronology().dayOfMonth(), test.dayOfMonth().getField());
        assertEquals("dayOfMonth", test.dayOfMonth().getName());
        assertEquals("Property[dayOfMonth]", test.dayOfMonth().toString());
        assertSame(test, test.dayOfMonth().getReadableInstant());
        assertSame(test, test.dayOfMonth().getDateMidnight());
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

    //-----------------------------------------------------------------------
    public void testPropertyGetDayOfYear() {
        // 31+29+31+30+31+9 = 161
        DateMidnight test = new DateMidnight(2004, 6, 9);
        assertSame(test.getChronology().dayOfYear(), test.dayOfYear().getField());
        assertEquals("dayOfYear", test.dayOfYear().getName());
        assertEquals("Property[dayOfYear]", test.dayOfYear().toString());
        assertSame(test, test.dayOfYear().getReadableInstant());
        assertSame(test, test.dayOfYear().getDateMidnight());
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

    //-----------------------------------------------------------------------
    public void testPropertyGetWeekOfWeekyear() {
        DateMidnight test = new DateMidnight(2004, 6, 9);
        assertSame(test.getChronology().weekOfWeekyear(), test.weekOfWeekyear().getField());
        assertEquals("weekOfWeekyear", test.weekOfWeekyear().getName());
        assertEquals("Property[weekOfWeekyear]", test.weekOfWeekyear().toString());
        assertSame(test, test.weekOfWeekyear().getReadableInstant());
        assertSame(test, test.weekOfWeekyear().getDateMidnight());
        assertEquals(24, test.weekOfWeekyear().get());
        assertEquals("24", test.weekOfWeekyear().getAsText());
        assertEquals("24", test.weekOfWeekyear().getAsText(Locale.FRENCH));
        assertEquals("24", test.weekOfWeekyear().getAsShortText());
        assertEquals("24", test.weekOfWeekyear().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().weeks(), test.weekOfWeekyear().getDurationField());
        assertEquals(test.getChronology().weekyears(), test.weekOfWeekyear().getRangeDurationField());
        assertEquals(2, test.weekOfWeekyear().getMaximumTextLength(null));
        assertEquals(2, test.weekOfWeekyear().getMaximumShortTextLength(null));
        assertEquals(false, test.weekOfWeekyear().isLeap());
        assertEquals(0, test.weekOfWeekyear().getLeapAmount());
        assertEquals(null, test.weekOfWeekyear().getLeapDurationField());
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetDayOfWeek() {
        DateMidnight test = new DateMidnight(2004, 6, 9);
        assertSame(test.getChronology().dayOfWeek(), test.dayOfWeek().getField());
        assertEquals("dayOfWeek", test.dayOfWeek().getName());
        assertEquals("Property[dayOfWeek]", test.dayOfWeek().toString());
        assertSame(test, test.dayOfWeek().getReadableInstant());
        assertSame(test, test.dayOfWeek().getDateMidnight());
        assertEquals(3, test.dayOfWeek().get());
        assertEquals("Wednesday", test.dayOfWeek().getAsText());
        assertEquals("mercredi", test.dayOfWeek().getAsText(Locale.FRENCH));
        assertEquals("Wed", test.dayOfWeek().getAsShortText());
        assertEquals("mer.", test.dayOfWeek().getAsShortText(Locale.FRENCH));
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

}
