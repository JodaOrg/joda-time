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
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This class is a Junit unit test for Chronology.
 *
 * @author Stephen Colebourne
 */
public class TestDateTimeFieldType extends TestCase {
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
            
    // 2002-04-05
    private long TEST_TIME1 =
            (y2002days + 31L + 28L + 31L + 5L -1L) * DateTimeConstants.MILLIS_PER_DAY
            + 12L * DateTimeConstants.MILLIS_PER_HOUR
            + 24L * DateTimeConstants.MILLIS_PER_MINUTE;
        
    // 2003-05-06
    private long TEST_TIME2 =
            (y2003days + 31L + 28L + 31L + 30L + 6L -1L) * DateTimeConstants.MILLIS_PER_DAY
            + 14L * DateTimeConstants.MILLIS_PER_HOUR
            + 28L * DateTimeConstants.MILLIS_PER_MINUTE;
    
    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestDateTimeFieldType.class);
    }

    public TestDateTimeFieldType(String name) {
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
    public void testTest() {
        assertEquals("2002-06-09T00:00:00.000Z", new Instant(TEST_TIME_NOW).toString());
        assertEquals("2002-04-05T12:24:00.000Z", new Instant(TEST_TIME1).toString());
        assertEquals("2003-05-06T14:28:00.000Z", new Instant(TEST_TIME2).toString());
    }

    //-----------------------------------------------------------------------
    public void test_era() {
        assertEquals(DateTimeFieldType.era(), DateTimeFieldType.era());
        assertEquals("era", DateTimeFieldType.era().getName());
        assertEquals(DurationFieldType.eras(), DateTimeFieldType.era().getDurationType());
        assertEquals(null, DateTimeFieldType.era().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().era(), DateTimeFieldType.era().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().era().isSupported(), DateTimeFieldType.era().isSupported(Chronology.getCopticUTC()));
    }

    public void test_centuryOfEra() {
        assertEquals(DateTimeFieldType.centuryOfEra(), DateTimeFieldType.centuryOfEra());
        assertEquals("centuryOfEra", DateTimeFieldType.centuryOfEra().getName());
        assertEquals(DurationFieldType.centuries(), DateTimeFieldType.centuryOfEra().getDurationType());
        assertEquals(DurationFieldType.eras(), DateTimeFieldType.centuryOfEra().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().centuryOfEra(), DateTimeFieldType.centuryOfEra().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().centuryOfEra().isSupported(), DateTimeFieldType.centuryOfEra().isSupported(Chronology.getCopticUTC()));
    }

    public void test_yearOfCentury() {
        assertEquals(DateTimeFieldType.yearOfCentury(), DateTimeFieldType.yearOfCentury());
        assertEquals("yearOfCentury", DateTimeFieldType.yearOfCentury().getName());
        assertEquals(DurationFieldType.years(), DateTimeFieldType.yearOfCentury().getDurationType());
        assertEquals(DurationFieldType.centuries(), DateTimeFieldType.yearOfCentury().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().yearOfCentury(), DateTimeFieldType.yearOfCentury().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().yearOfCentury().isSupported(), DateTimeFieldType.yearOfCentury().isSupported(Chronology.getCopticUTC()));
    }

    public void test_yearOfEra() {
        assertEquals(DateTimeFieldType.yearOfEra(), DateTimeFieldType.yearOfEra());
        assertEquals("yearOfEra", DateTimeFieldType.yearOfEra().getName());
        assertEquals(DurationFieldType.years(), DateTimeFieldType.yearOfEra().getDurationType());
        assertEquals(DurationFieldType.eras(), DateTimeFieldType.yearOfEra().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().yearOfEra(), DateTimeFieldType.yearOfEra().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().yearOfEra().isSupported(), DateTimeFieldType.yearOfEra().isSupported(Chronology.getCopticUTC()));
    }

    public void test_year() {
        assertEquals(DateTimeFieldType.year(), DateTimeFieldType.year());
        assertEquals("year", DateTimeFieldType.year().getName());
        assertEquals(DurationFieldType.years(), DateTimeFieldType.year().getDurationType());
        assertEquals(null, DateTimeFieldType.year().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().year(), DateTimeFieldType.year().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().year().isSupported(), DateTimeFieldType.year().isSupported(Chronology.getCopticUTC()));
    }

    public void test_monthOfYear() {
        assertEquals(DateTimeFieldType.monthOfYear(), DateTimeFieldType.monthOfYear());
        assertEquals("monthOfYear", DateTimeFieldType.monthOfYear().getName());
        assertEquals(DurationFieldType.months(), DateTimeFieldType.monthOfYear().getDurationType());
        assertEquals(DurationFieldType.years(), DateTimeFieldType.monthOfYear().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().monthOfYear(), DateTimeFieldType.monthOfYear().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().monthOfYear().isSupported(), DateTimeFieldType.monthOfYear().isSupported(Chronology.getCopticUTC()));
    }

    public void test_weekyearOfCentury() {
        assertEquals(DateTimeFieldType.weekyearOfCentury(), DateTimeFieldType.weekyearOfCentury());
        assertEquals("weekyearOfCentury", DateTimeFieldType.weekyearOfCentury().getName());
        assertEquals(DurationFieldType.weekyears(), DateTimeFieldType.weekyearOfCentury().getDurationType());
        assertEquals(DurationFieldType.centuries(), DateTimeFieldType.weekyearOfCentury().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().weekyearOfCentury(), DateTimeFieldType.weekyearOfCentury().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().weekyearOfCentury().isSupported(), DateTimeFieldType.weekyearOfCentury().isSupported(Chronology.getCopticUTC()));
    }

    public void test_weekyear() {
        assertEquals(DateTimeFieldType.weekyear(), DateTimeFieldType.weekyear());
        assertEquals("weekyear", DateTimeFieldType.weekyear().getName());
        assertEquals(DurationFieldType.weekyears(), DateTimeFieldType.weekyear().getDurationType());
        assertEquals(null, DateTimeFieldType.weekyear().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().weekyear(), DateTimeFieldType.weekyear().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().weekyear().isSupported(), DateTimeFieldType.weekyear().isSupported(Chronology.getCopticUTC()));
    }

    public void test_weekOfWeekyear() {
        assertEquals(DateTimeFieldType.weekOfWeekyear(), DateTimeFieldType.weekOfWeekyear());
        assertEquals("weekOfWeekyear", DateTimeFieldType.weekOfWeekyear().getName());
        assertEquals(DurationFieldType.weeks(), DateTimeFieldType.weekOfWeekyear().getDurationType());
        assertEquals(DurationFieldType.weekyears(), DateTimeFieldType.weekOfWeekyear().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().weekOfWeekyear(), DateTimeFieldType.weekOfWeekyear().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().weekOfWeekyear().isSupported(), DateTimeFieldType.weekOfWeekyear().isSupported(Chronology.getCopticUTC()));
    }

    public void test_dayOfYear() {
        assertEquals(DateTimeFieldType.dayOfYear(), DateTimeFieldType.dayOfYear());
        assertEquals("dayOfYear", DateTimeFieldType.dayOfYear().getName());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.dayOfYear().getDurationType());
        assertEquals(DurationFieldType.years(), DateTimeFieldType.dayOfYear().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().dayOfYear(), DateTimeFieldType.dayOfYear().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().dayOfYear().isSupported(), DateTimeFieldType.dayOfYear().isSupported(Chronology.getCopticUTC()));
    }

    public void test_dayOfMonth() {
        assertEquals(DateTimeFieldType.dayOfMonth(), DateTimeFieldType.dayOfMonth());
        assertEquals("dayOfMonth", DateTimeFieldType.dayOfMonth().getName());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.dayOfMonth().getDurationType());
        assertEquals(DurationFieldType.months(), DateTimeFieldType.dayOfMonth().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().dayOfMonth(), DateTimeFieldType.dayOfMonth().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().dayOfMonth().isSupported(), DateTimeFieldType.dayOfMonth().isSupported(Chronology.getCopticUTC()));
    }

    public void test_dayOfWeek() {
        assertEquals(DateTimeFieldType.dayOfWeek(), DateTimeFieldType.dayOfWeek());
        assertEquals("dayOfWeek", DateTimeFieldType.dayOfWeek().getName());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.dayOfWeek().getDurationType());
        assertEquals(DurationFieldType.weeks(), DateTimeFieldType.dayOfWeek().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().dayOfWeek(), DateTimeFieldType.dayOfWeek().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().dayOfWeek().isSupported(), DateTimeFieldType.dayOfWeek().isSupported(Chronology.getCopticUTC()));
    }

    public void test_halfdayOfDay() {
        assertEquals(DateTimeFieldType.halfdayOfDay(), DateTimeFieldType.halfdayOfDay());
        assertEquals("halfdayOfDay", DateTimeFieldType.halfdayOfDay().getName());
        assertEquals(DurationFieldType.halfdays(), DateTimeFieldType.halfdayOfDay().getDurationType());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.halfdayOfDay().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().halfdayOfDay(), DateTimeFieldType.halfdayOfDay().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().halfdayOfDay().isSupported(), DateTimeFieldType.halfdayOfDay().isSupported(Chronology.getCopticUTC()));
    }

    public void test_clockhourOfDay() {
        assertEquals(DateTimeFieldType.clockhourOfDay(), DateTimeFieldType.clockhourOfDay());
        assertEquals("clockhourOfDay", DateTimeFieldType.clockhourOfDay().getName());
        assertEquals(DurationFieldType.hours(), DateTimeFieldType.clockhourOfDay().getDurationType());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.clockhourOfDay().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().clockhourOfDay(), DateTimeFieldType.clockhourOfDay().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().clockhourOfDay().isSupported(), DateTimeFieldType.clockhourOfDay().isSupported(Chronology.getCopticUTC()));
    }

    public void test_clockhourOfHalfday() {
        assertEquals(DateTimeFieldType.clockhourOfHalfday(), DateTimeFieldType.clockhourOfHalfday());
        assertEquals("clockhourOfHalfday", DateTimeFieldType.clockhourOfHalfday().getName());
        assertEquals(DurationFieldType.hours(), DateTimeFieldType.clockhourOfHalfday().getDurationType());
        assertEquals(DurationFieldType.halfdays(), DateTimeFieldType.clockhourOfHalfday().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().clockhourOfHalfday(), DateTimeFieldType.clockhourOfHalfday().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().clockhourOfHalfday().isSupported(), DateTimeFieldType.clockhourOfHalfday().isSupported(Chronology.getCopticUTC()));
    }

    public void test_hourOfHalfday() {
        assertEquals(DateTimeFieldType.hourOfHalfday(), DateTimeFieldType.hourOfHalfday());
        assertEquals("hourOfHalfday", DateTimeFieldType.hourOfHalfday().getName());
        assertEquals(DurationFieldType.hours(), DateTimeFieldType.hourOfHalfday().getDurationType());
        assertEquals(DurationFieldType.halfdays(), DateTimeFieldType.hourOfHalfday().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().hourOfHalfday(), DateTimeFieldType.hourOfHalfday().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().hourOfHalfday().isSupported(), DateTimeFieldType.hourOfHalfday().isSupported(Chronology.getCopticUTC()));
    }

    public void test_hourOfDay() {
        assertEquals(DateTimeFieldType.hourOfDay(), DateTimeFieldType.hourOfDay());
        assertEquals("hourOfDay", DateTimeFieldType.hourOfDay().getName());
        assertEquals(DurationFieldType.hours(), DateTimeFieldType.hourOfDay().getDurationType());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.hourOfDay().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().hourOfDay(), DateTimeFieldType.hourOfDay().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().hourOfDay().isSupported(), DateTimeFieldType.hourOfDay().isSupported(Chronology.getCopticUTC()));
    }

    public void test_minuteOfDay() {
        assertEquals(DateTimeFieldType.minuteOfDay(), DateTimeFieldType.minuteOfDay());
        assertEquals("minuteOfDay", DateTimeFieldType.minuteOfDay().getName());
        assertEquals(DurationFieldType.minutes(), DateTimeFieldType.minuteOfDay().getDurationType());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.minuteOfDay().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().minuteOfDay(), DateTimeFieldType.minuteOfDay().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().minuteOfDay().isSupported(), DateTimeFieldType.minuteOfDay().isSupported(Chronology.getCopticUTC()));
    }

    public void test_minuteOfHour() {
        assertEquals(DateTimeFieldType.minuteOfHour(), DateTimeFieldType.minuteOfHour());
        assertEquals("minuteOfHour", DateTimeFieldType.minuteOfHour().getName());
        assertEquals(DurationFieldType.minutes(), DateTimeFieldType.minuteOfHour().getDurationType());
        assertEquals(DurationFieldType.hours(), DateTimeFieldType.minuteOfHour().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().minuteOfHour(), DateTimeFieldType.minuteOfHour().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().minuteOfHour().isSupported(), DateTimeFieldType.minuteOfHour().isSupported(Chronology.getCopticUTC()));
    }

    public void test_secondOfDay() {
        assertEquals(DateTimeFieldType.secondOfDay(), DateTimeFieldType.secondOfDay());
        assertEquals("secondOfDay", DateTimeFieldType.secondOfDay().getName());
        assertEquals(DurationFieldType.seconds(), DateTimeFieldType.secondOfDay().getDurationType());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.secondOfDay().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().secondOfDay(), DateTimeFieldType.secondOfDay().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().secondOfDay().isSupported(), DateTimeFieldType.secondOfDay().isSupported(Chronology.getCopticUTC()));
    }

    public void test_secondOfMinute() {
        assertEquals(DateTimeFieldType.secondOfMinute(), DateTimeFieldType.secondOfMinute());
        assertEquals("secondOfMinute", DateTimeFieldType.secondOfMinute().getName());
        assertEquals(DurationFieldType.seconds(), DateTimeFieldType.secondOfMinute().getDurationType());
        assertEquals(DurationFieldType.minutes(), DateTimeFieldType.secondOfMinute().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().secondOfMinute(), DateTimeFieldType.secondOfMinute().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().secondOfMinute().isSupported(), DateTimeFieldType.secondOfMinute().isSupported(Chronology.getCopticUTC()));
    }

    public void test_millisOfDay() {
        assertEquals(DateTimeFieldType.millisOfDay(), DateTimeFieldType.millisOfDay());
        assertEquals("millisOfDay", DateTimeFieldType.millisOfDay().getName());
        assertEquals(DurationFieldType.millis(), DateTimeFieldType.millisOfDay().getDurationType());
        assertEquals(DurationFieldType.days(), DateTimeFieldType.millisOfDay().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().millisOfDay(), DateTimeFieldType.millisOfDay().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().millisOfDay().isSupported(), DateTimeFieldType.millisOfDay().isSupported(Chronology.getCopticUTC()));
    }

    public void test_millisOfSecond() {
        assertEquals(DateTimeFieldType.millisOfSecond(), DateTimeFieldType.millisOfSecond());
        assertEquals("millisOfSecond", DateTimeFieldType.millisOfSecond().getName());
        assertEquals(DurationFieldType.millis(), DateTimeFieldType.millisOfSecond().getDurationType());
        assertEquals(DurationFieldType.seconds(), DateTimeFieldType.millisOfSecond().getRangeDurationType());
        assertEquals(Chronology.getCopticUTC().millisOfSecond(), DateTimeFieldType.millisOfSecond().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().millisOfSecond().isSupported(), DateTimeFieldType.millisOfSecond().isSupported(Chronology.getCopticUTC()));
    }

}
