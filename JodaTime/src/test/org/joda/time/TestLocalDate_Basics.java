/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2005 Stephen Colebourne.  
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.UnsupportedDateTimeField;
import org.joda.time.field.UnsupportedDurationField;

/**
 * This class is a Junit unit test for LocalDate.
 *
 * @author Stephen Colebourne
 */
public class TestLocalDate_Basics extends TestCase {
    // Test in 2002/03 as time zones are more well known
    // (before the late 90's they were all over the place)

    private static final DateTimeZone PARIS = DateTimeZone.getInstance("Europe/Paris");
    private static final Chronology COPTIC_PARIS = Chronology.getCoptic(PARIS);
    private static final DateTimeZone LONDON = DateTimeZone.getInstance("Europe/London");
    private static final Chronology COPTIC_LONDON = Chronology.getCoptic(LONDON);
    private static final DateTimeZone NEWYORK = DateTimeZone.getInstance("America/New_York");
    private static final Chronology COPTIC_NEWYORK = Chronology.getCoptic(NEWYORK);
    private static final Chronology BUDDHIST_NEWYORK = Chronology.getBuddhist(NEWYORK);
    
    long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                     366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                     365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                     366 + 365;
    long y2003days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                     366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                     365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                     366 + 365 + 365;
    
    // 2002-06-09
    private long TEST_TIME_NOW_UTC =
            (y2002days + 31L + 28L + 31L + 30L + 31L + 9L -1L) * DateTimeConstants.MILLIS_PER_DAY;
    private long TEST_TIME_NOW_LONDON =
            TEST_TIME_NOW_UTC - DateTimeConstants.MILLIS_PER_HOUR;
    private long TEST_TIME_NOW_PARIS =
            TEST_TIME_NOW_UTC - 2*DateTimeConstants.MILLIS_PER_HOUR;
            
    // 2002-04-05
    private long TEST_TIME1_UTC =
            (y2002days + 31L + 28L + 31L + 5L -1L) * DateTimeConstants.MILLIS_PER_DAY
            + 12L * DateTimeConstants.MILLIS_PER_HOUR
            + 24L * DateTimeConstants.MILLIS_PER_MINUTE;
    private long TEST_TIME1_LONDON =
            (y2002days + 31L + 28L + 31L + 5L -1L) * DateTimeConstants.MILLIS_PER_DAY
            - DateTimeConstants.MILLIS_PER_HOUR;
    private long TEST_TIME1_PARIS =
            (y2002days + 31L + 28L + 31L + 5L -1L) * DateTimeConstants.MILLIS_PER_DAY
            - 2*DateTimeConstants.MILLIS_PER_HOUR;
        
    // 2003-05-06
    private long TEST_TIME2_UTC =
            (y2003days + 31L + 28L + 31L + 30L + 6L -1L) * DateTimeConstants.MILLIS_PER_DAY
            + 14L * DateTimeConstants.MILLIS_PER_HOUR
            + 28L * DateTimeConstants.MILLIS_PER_MINUTE;
    private long TEST_TIME2_LONDON =
            (y2003days + 31L + 28L + 31L + 30L + 6L -1L) * DateTimeConstants.MILLIS_PER_DAY
             - DateTimeConstants.MILLIS_PER_HOUR;
    private long TEST_TIME2_PARIS =
            (y2003days + 31L + 28L + 31L + 30L + 6L -1L) * DateTimeConstants.MILLIS_PER_DAY
             - 2*DateTimeConstants.MILLIS_PER_HOUR;
    
    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestLocalDate_Basics.class);
    }

    public TestLocalDate_Basics(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW_UTC);
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
        assertEquals("2002-06-09T00:00:00.000Z", new Instant(TEST_TIME_NOW_UTC).toString());
        assertEquals("2002-04-05T12:24:00.000Z", new Instant(TEST_TIME1_UTC).toString());
        assertEquals("2003-05-06T14:28:00.000Z", new Instant(TEST_TIME2_UTC).toString());
    }

    public void testGet_DateTimeFieldType() {
        LocalDate test = new LocalDate();
        assertEquals(1, test.get(DateTimeFieldType.era()));
        assertEquals(20, test.get(DateTimeFieldType.centuryOfEra()));
        assertEquals(2, test.get(DateTimeFieldType.yearOfCentury()));
        assertEquals(2002, test.get(DateTimeFieldType.yearOfEra()));
        assertEquals(2002, test.get(DateTimeFieldType.year()));
        assertEquals(6, test.get(DateTimeFieldType.monthOfYear()));
        assertEquals(9, test.get(DateTimeFieldType.dayOfMonth()));
        assertEquals(2002, test.get(DateTimeFieldType.weekyear()));
        assertEquals(23, test.get(DateTimeFieldType.weekOfWeekyear()));
        assertEquals(7, test.get(DateTimeFieldType.dayOfWeek()));
        assertEquals(160, test.get(DateTimeFieldType.dayOfYear()));
        try {
            test.get((DateTimeFieldType) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.get(DateTimeFieldType.halfdayOfDay());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.get(DateTimeFieldType.hourOfHalfday());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.get(DateTimeFieldType.clockhourOfDay());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.get(DateTimeFieldType.clockhourOfHalfday());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.get(DateTimeFieldType.hourOfDay());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.get(BAD_DATETIMETYPE);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testGetMethods() {
        LocalDate test = new LocalDate();
        
        assertEquals(Chronology.getISOUTC(), test.getChronology());
        
        assertEquals(1, test.getEra());
        assertEquals(20, test.getCenturyOfEra());
        assertEquals(2, test.getYearOfCentury());
        assertEquals(2002, test.getYearOfEra());
        assertEquals(2002, test.getYear());
        assertEquals(6, test.getMonthOfYear());
        assertEquals(9, test.getDayOfMonth());
        assertEquals(2002, test.getWeekyear());
        assertEquals(23, test.getWeekOfWeekyear());
        assertEquals(7, test.getDayOfWeek());
        assertEquals(160, test.getDayOfYear());
    }

    public void testEqualsHashCode() {
        LocalDate test1 = new LocalDate(TEST_TIME1_UTC);
        LocalDate test2 = new LocalDate(TEST_TIME1_UTC);
        assertEquals(true, test1.equals(test2));
        assertEquals(true, test2.equals(test1));
        assertEquals(true, test1.equals(test1));
        assertEquals(true, test2.equals(test2));
        assertEquals(true, test1.hashCode() == test2.hashCode());
        assertEquals(true, test1.hashCode() == test1.hashCode());
        assertEquals(true, test2.hashCode() == test2.hashCode());
        
        LocalDate test3 = new LocalDate(TEST_TIME2_UTC);
        assertEquals(false, test1.equals(test3));
        assertEquals(false, test2.equals(test3));
        assertEquals(false, test3.equals(test1));
        assertEquals(false, test3.equals(test2));
        assertEquals(false, test1.hashCode() == test3.hashCode());
        assertEquals(false, test2.hashCode() == test3.hashCode());
        
        assertEquals(false, test1.equals("Hello"));
        assertEquals(false, test1.equals(new DateTime()));
        assertEquals(false, test1.equals(new LocalDate(TEST_TIME1_UTC, GregorianChronology.getInstance())));
    }

    //-----------------------------------------------------------------------
    public void testSerialization() throws Exception {
        LocalDate test = new LocalDate(TEST_TIME_NOW_UTC);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(test);
        byte[] bytes = baos.toByteArray();
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        LocalDate result = (LocalDate) ois.readObject();
        ois.close();
        
        assertEquals(test, result);
    }

    //-----------------------------------------------------------------------
    public void testToString() {
        LocalDate test = new LocalDate(TEST_TIME_NOW_UTC);
        assertEquals("2002-06-09", test.toString());
        
        test = new LocalDate(TEST_TIME_NOW_UTC, Chronology.getISO(PARIS));
        assertEquals("2002-06-09", test.toString());
        
        test = new LocalDate(TEST_TIME_NOW_UTC, Chronology.getISO(NEWYORK));
        assertEquals("2002-06-08", test.toString());  // the 8th
    }

    public void testToString_String() {
        LocalDate test = new LocalDate(TEST_TIME_NOW_UTC);
        assertEquals("2002 00", test.toString("yyyy HH"));
        assertEquals("2002-06-09", test.toString(null));
    }

    public void testToString_String_String() {
        LocalDate test = new LocalDate(TEST_TIME_NOW_UTC);
        assertEquals("Sun 9/6", test.toString("EEE d/M", Locale.ENGLISH));
        assertEquals("dim. 9/6", test.toString("EEE d/M", Locale.FRENCH));
        assertEquals("2002-06-09", test.toString(null, Locale.ENGLISH));
        assertEquals("Sun 9/6", test.toString("EEE d/M", null));
        assertEquals("2002-06-09", test.toString(null, null));
    }

    //-----------------------------------------------------------------------
    public void testToDateTimeAtMidnight() {
        LocalDate test = new LocalDate(TEST_TIME1_UTC);
        DateTime result = test.toDateTimeAtMidnight();
        assertEquals(test.getYear(), result.getYear());
        assertEquals(test.getMonthOfYear(), result.getMonthOfYear());
        assertEquals(test.getDayOfMonth(), result.getDayOfMonth());
        assertEquals(0, result.getMillisOfDay());
        assertEquals(LONDON, result.getZone());
    }

    public void testToDateTimeAtMidnight_DateTimeZone() {
        LocalDate test = new LocalDate(TEST_TIME1_UTC);
        DateTime result = test.toDateTimeAtMidnight(LONDON);
        assertEquals(test.getYear(), result.getYear());
        assertEquals(test.getMonthOfYear(), result.getMonthOfYear());
        assertEquals(test.getDayOfMonth(), result.getDayOfMonth());
        assertEquals(0, result.getMillisOfDay());
        assertEquals(LONDON, result.getZone());

        test = new LocalDate(TEST_TIME1_UTC);
        result = test.toDateTimeAtMidnight(PARIS);
        assertEquals(test.getYear(), result.getYear());
        assertEquals(test.getMonthOfYear(), result.getMonthOfYear());
        assertEquals(test.getDayOfMonth(), result.getDayOfMonth());
        assertEquals(0, result.getMillisOfDay());
        assertEquals(PARIS, result.getZone());

        test = new LocalDate(TEST_TIME1_UTC);
        result = test.toDateTimeAtMidnight((DateTimeZone) null);
        assertEquals(test.getYear(), result.getYear());
        assertEquals(test.getMonthOfYear(), result.getMonthOfYear());
        assertEquals(test.getDayOfMonth(), result.getDayOfMonth());
        assertEquals(0, result.getMillisOfDay());
        assertEquals(LONDON, result.getZone());
    }

    //-----------------------------------------------------------------------
    public void testToDateTime_RI() {
        LocalDate base = new LocalDate(2005, 6, 9, COPTIC_PARIS);
        DateTime dt = new DateTime(2002, 1, 3, 4, 5, 6, 7);
        
        DateTime test = base.toDateTime(dt);
        DateTime expected = dt;
        expected = expected.year().setCopy(2005);
        expected = expected.monthOfYear().setCopy(6);
        expected = expected.dayOfMonth().setCopy(9);
        assertEquals(expected, test);
    }

    public void testToDateTime_nullRI() {
        LocalDate base = new LocalDate(2005, 6, 9);
        DateTime dt = new DateTime(2002, 1, 3, 4, 5, 6, 7);
        DateTimeUtils.setCurrentMillisFixed(dt.getMillis());
        
        DateTime test = base.toDateTime((ReadableInstant) null);
        DateTime expected = dt;
        expected = expected.year().setCopy(2005);
        expected = expected.monthOfYear().setCopy(6);
        expected = expected.dayOfMonth().setCopy(9);
        assertEquals(expected, test);
    }

    //-----------------------------------------------------------------------
    public void testToDateTime_LocalTime() {
        LocalDate base = new LocalDate(2005, 6, 9, COPTIC_PARIS); // PARIS irrelevant
        LocalTime tod = new LocalTime(12, 13, 14, 15, BUDDHIST_NEWYORK);
        
        DateTime test = base.toDateTime(tod);
        DateTime expected = new DateTime(2005, 6, 9, 12, 13, 14, 15, COPTIC_LONDON);
        assertEquals(expected, test);
    }

    public void testToDateTime_nullLocalTime() {
        LocalDate base = new LocalDate(2005, 6, 9, COPTIC_PARIS); // PARIS irrelevant
        long now = new DateTime(2004, 5, 8, 12, 13, 14, 15, COPTIC_LONDON).getMillis();
        DateTimeUtils.setCurrentMillisFixed(now);
        
        DateTime test = base.toDateTime((LocalTime) null);
        DateTime expected = new DateTime(2005, 6, 9, 12, 13, 14, 15, COPTIC_LONDON);
        assertEquals(expected, test);
    }

    //-----------------------------------------------------------------------
    public void testToDateTime_LocalTime_Zone() {
        LocalDate base = new LocalDate(2005, 6, 9, COPTIC_PARIS); // PARIS irrelevant
        LocalTime tod = new LocalTime(12, 13, 14, 15, BUDDHIST_NEWYORK);
        
        DateTime test = base.toDateTime(tod, NEWYORK);
        DateTime expected = new DateTime(2005, 6, 9, 12, 13, 14, 15, COPTIC_NEWYORK);
        assertEquals(expected, test);
    }

    public void testToDateTime_LocalTime_nullZone() {
        LocalDate base = new LocalDate(2005, 6, 9, COPTIC_PARIS); // PARIS irrelevant
        LocalTime tod = new LocalTime(12, 13, 14, 15, BUDDHIST_NEWYORK);
        
        DateTime test = base.toDateTime(tod, null);
        DateTime expected = new DateTime(2005, 6, 9, 12, 13, 14, 15, COPTIC_LONDON);
        assertEquals(expected, test);
    }

    public void testToDateTime_nullLocalTime_Zone() {
        LocalDate base = new LocalDate(2005, 6, 9, COPTIC_PARIS); // PARIS irrelevant
        long now = new DateTime(2004, 5, 8, 12, 13, 14, 15, COPTIC_NEWYORK).getMillis();
        DateTimeUtils.setCurrentMillisFixed(now);
        
        DateTime test = base.toDateTime((LocalTime) null, NEWYORK);
        DateTime expected = new DateTime(2005, 6, 9, 12, 13, 14, 15, COPTIC_NEWYORK);
        assertEquals(expected, test);
    }

    //-----------------------------------------------------------------------
    public void testToDateMidnight() {
        LocalDate test = new LocalDate(TEST_TIME1_UTC);
        DateMidnight result = test.toDateMidnight();
        assertEquals(test.getYear(), result.getYear());
        assertEquals(test.getMonthOfYear(), result.getMonthOfYear());
        assertEquals(test.getDayOfMonth(), result.getDayOfMonth());
        assertEquals(0, result.getMillisOfDay());
        assertEquals(LONDON, result.getZone());
    }

    public void testToDateMidnight_DateTimeZone() {
        LocalDate test = new LocalDate(TEST_TIME1_UTC);
        DateMidnight result = test.toDateMidnight(LONDON);
        assertEquals(test.getYear(), result.getYear());
        assertEquals(test.getMonthOfYear(), result.getMonthOfYear());
        assertEquals(test.getDayOfMonth(), result.getDayOfMonth());
        assertEquals(0, result.getMillisOfDay());
        assertEquals(LONDON, result.getZone());

        test = new LocalDate(TEST_TIME1_UTC);
        result = test.toDateMidnight(PARIS);
        assertEquals(test.getYear(), result.getYear());
        assertEquals(test.getMonthOfYear(), result.getMonthOfYear());
        assertEquals(test.getDayOfMonth(), result.getDayOfMonth());
        assertEquals(0, result.getMillisOfDay());
        assertEquals(PARIS, result.getZone());

        test = new LocalDate(TEST_TIME1_UTC);
        result = test.toDateMidnight((DateTimeZone) null);
        assertEquals(test.getYear(), result.getYear());
        assertEquals(test.getMonthOfYear(), result.getMonthOfYear());
        assertEquals(test.getDayOfMonth(), result.getDayOfMonth());
        assertEquals(0, result.getMillisOfDay());
        assertEquals(LONDON, result.getZone());
    }

    //-----------------------------------------------------------------------
    public void testToInterval() {
        LocalDate base = new LocalDate(2005, 6, 9, COPTIC_PARIS); // PARIS irrelevant
        Interval test = base.toInterval();
        DateTime start = base.toDateTimeAtMidnight();
        DateTime end = start.plus(Period.days(1));
        Interval expected = new Interval(start, end);
        assertEquals(expected, test);
    }

    //-----------------------------------------------------------------------
    public void testToInterval_Zone() {
        LocalDate base = new LocalDate(2005, 6, 9, COPTIC_PARIS); // PARIS irrelevant
        Interval test = base.toInterval(NEWYORK);
        DateTime start = base.toDateTimeAtMidnight(NEWYORK);
        DateTime end = start.plus(Period.days(1));
        Interval expected = new Interval(start, end);
        assertEquals(expected, test);
    }

    public void testToInterval_nullZone() {
        LocalDate base = new LocalDate(2005, 6, 9, COPTIC_PARIS); // PARIS irrelevant
        Interval test = base.toInterval(null);
        DateTime start = base.toDateTimeAtMidnight(LONDON);
        DateTime end = start.plus(Period.days(1));
        Interval expected = new Interval(start, end);
        assertEquals(expected, test);
    }

    //-----------------------------------------------------------------------
    public void testWithLocalMillis_long() {
        LocalDate test = new LocalDate(TEST_TIME1_UTC);
        LocalDate result = test.withLocalMillis(test.getLocalMillis() - 1);
        assertEquals(test.getYear(), result.getYear());
        assertEquals(test.getMonthOfYear(), result.getMonthOfYear());
        assertEquals(test.getDayOfMonth() - 1, result.getDayOfMonth());
        assertEquals(test.getChronology(), result.getChronology());
        
        test = new LocalDate(TEST_TIME1_UTC);
        result = test.withLocalMillis(test.getLocalMillis());
        assertSame(test, result);
    }

    public void testWithChronologyRetainFields_Chronology() {
        LocalDate test = new LocalDate(TEST_TIME1_UTC);
        LocalDate result = test.withChronologyRetainFields(Chronology.getGregorian(PARIS));
        assertEquals(test.toString(), result.toString());
        assertEquals(Chronology.getGregorianUTC(), result.getChronology());
        
        test = new LocalDate(TEST_TIME1_UTC, GregorianChronology.getInstance(PARIS));
        result = test.withChronologyRetainFields(null);
        assertEquals(test.toString(), result.toString());
        assertEquals(Chronology.getISOUTC(), result.getChronology());
        
        test = new LocalDate(TEST_TIME1_UTC);
        result = test.withChronologyRetainFields(ISOChronology.getInstance());
        assertSame(test, result);
    }

//    //-----------------------------------------------------------------------
//    public void testWithFields_RPartial() {
//        LocalDate test = new LocalDate(2004, 5, 6);
//        LocalDate result = test.withFields(new YearMonthDay(2003, 4, 5));
//        LocalDate expected = new LocalDate(2003, 4, 5);
//        assertEquals(expected, result);
//        
//        test = new LocalDate(TEST_TIME1_UTC);
//        result = test.withFields(null);
//        assertSame(test, result);
//    }
//
    //-----------------------------------------------------------------------
    public void testWithField1() {
        LocalDate test = new LocalDate(2004, 6, 9);
        LocalDate result = test.withField(DateTimeFieldType.year(), 2006);
        
        assertEquals(new LocalDate(2004, 6, 9), test);
        assertEquals(new LocalDate(2006, 6, 9), result);
    }

    public void testWithField2() {
        LocalDate test = new LocalDate(2004, 6, 9);
        try {
            test.withField(null, 6);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testWithField3() {
        LocalDate test = new LocalDate(2004, 6, 9);
        try {
            test.withField(DateTimeFieldType.hourOfDay(), 6);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testWithFieldAdded1() {
        LocalDate test = new LocalDate(2004, 6, 9);
        LocalDate result = test.withFieldAdded(DurationFieldType.years(), 6);
        
        assertEquals(new LocalDate(2004, 6, 9), test);
        assertEquals(new LocalDate(2010, 6, 9), result);
    }

    public void testWithFieldAdded2() {
        LocalDate test = new LocalDate(2004, 6, 9);
        try {
            test.withFieldAdded(null, 0);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testWithFieldAdded3() {
        LocalDate test = new LocalDate(2004, 6, 9);
        try {
            test.withFieldAdded(null, 6);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testWithFieldAdded4() {
        LocalDate test = new LocalDate(2004, 6, 9);
        LocalDate result = test.withFieldAdded(DurationFieldType.years(), 0);
        assertSame(test, result);
    }

    public void testWithFieldAdded5() {
        LocalDate test = new LocalDate(2004, 6, 9);
        try {
            test.withFieldAdded(DurationFieldType.hours(), 6);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testPlus_RP() {
        LocalDate test = new LocalDate(2002, 5, 3, BuddhistChronology.getInstance());
        LocalDate result = test.plus(new Period(1, 2, 3, 4, 5, 6, 7, 8));
        LocalDate expected = new LocalDate(2003, 7, 28, BuddhistChronology.getInstance());
        assertEquals(expected, result);
        
        result = test.plus((ReadablePeriod) null);
        assertSame(test, result);
    }

    public void testMinus_RP() {
        LocalDate test = new LocalDate(2002, 5, 3, BuddhistChronology.getInstance());
        LocalDate result = test.minus(new Period(1, 1, 1, 1, 1, 1, 1, 1));
        LocalDate expected = new LocalDate(2001, 3, 25, BuddhistChronology.getInstance());
        assertEquals(expected, result);
        
        result = test.minus((ReadablePeriod) null);
        assertSame(test, result);
    }

    //-----------------------------------------------------------------------
    public void testProperty() {
        LocalDate test = new LocalDate();
        assertEquals(test.year(), test.property(DateTimeFieldType.year()));
        assertEquals(test.dayOfWeek(), test.property(DateTimeFieldType.dayOfWeek()));
        assertEquals(test.weekOfWeekyear(), test.property(DateTimeFieldType.weekOfWeekyear()));
        assertEquals(test.property(DateTimeFieldType.dayOfMonth()), test.property(DateTimeFieldType.dayOfMonth()));
        try {
            test.property((DateTimeFieldType) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.property(DateTimeFieldType.halfdayOfDay());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.property(DateTimeFieldType.hourOfHalfday());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.property(DateTimeFieldType.clockhourOfDay());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.property(DateTimeFieldType.clockhourOfHalfday());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.property(DateTimeFieldType.hourOfDay());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.property(BAD_DATETIMETYPE);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testIsSupported_DateTimeFieldType() {
        LocalDate test = new LocalDate();
        assertEquals(true, test.isSupported(DateTimeFieldType.era()));
        assertEquals(true, test.isSupported(DateTimeFieldType.centuryOfEra()));
        assertEquals(true, test.isSupported(DateTimeFieldType.yearOfCentury()));
        assertEquals(true, test.isSupported(DateTimeFieldType.yearOfEra()));
        assertEquals(true, test.isSupported(DateTimeFieldType.year()));
        assertEquals(true, test.isSupported(DateTimeFieldType.monthOfYear()));
        assertEquals(true, test.isSupported(DateTimeFieldType.dayOfMonth()));
        assertEquals(true, test.isSupported(DateTimeFieldType.weekyear()));
        assertEquals(true, test.isSupported(DateTimeFieldType.weekOfWeekyear()));
        assertEquals(true, test.isSupported(DateTimeFieldType.dayOfWeek()));
        assertEquals(true, test.isSupported(DateTimeFieldType.dayOfYear()));
        
        assertEquals(false, test.isSupported(null));
        assertEquals(false, test.isSupported(DateTimeFieldType.halfdayOfDay()));
        assertEquals(false, test.isSupported(DateTimeFieldType.hourOfHalfday()));
        assertEquals(false, test.isSupported(DateTimeFieldType.clockhourOfDay()));
        assertEquals(false, test.isSupported(DateTimeFieldType.clockhourOfHalfday()));
        assertEquals(false, test.isSupported(DateTimeFieldType.hourOfDay()));
        assertEquals(false, test.isSupported(BAD_DATETIMETYPE));
    }

    DateTimeFieldType BAD_DATETIMETYPE = new DateTimeFieldType("bad") {
        public DurationFieldType getDurationType() {
            return DurationFieldType.weeks();
        }
        public DurationFieldType getRangeDurationType() {
            return null;
        }
        public DateTimeField getField(Chronology chronology) {
            return UnsupportedDateTimeField.getInstance(this, UnsupportedDurationField.getInstance(getDurationType()));
        }
    };

    //-----------------------------------------------------------------------
    public void testCompareTo_Object() {
        LocalDate test = new LocalDate();
        try {
            test.compareTo(null);
            fail();
        } catch (NullPointerException ex) {}
        try {
            test.compareTo(new DateTime());
            fail();
        } catch (ClassCastException ex) {}
        try {
            test.compareTo(new LocalDate(Chronology.getBuddhistUTC()));
            fail();
        } catch (IllegalArgumentException ex) {}
        
        test = new LocalDate(TEST_TIME_NOW_UTC);
        assertEquals(0, test.compareTo(new LocalDate(TEST_TIME_NOW_UTC)));
        
        test = new LocalDate(TEST_TIME_NOW_UTC);
        assertEquals(1, test.compareTo(new LocalDate(TEST_TIME1_UTC)));
        
        test = new LocalDate(TEST_TIME_NOW_UTC);
        assertEquals(-1, test.compareTo(new LocalDate(TEST_TIME2_UTC)));
    }

    //-----------------------------------------------------------------------
    public void testIsToday() {
        LocalDate test = new LocalDate();
        assertEquals(true, test.isToday());
        
        test = new LocalDate(TEST_TIME1_UTC);
        assertEquals(false, test.isToday());
    }

    //-----------------------------------------------------------------------
    public void testIsAfterToday() {
        LocalDate test = new LocalDate();
        assertEquals(false, test.isAfterToday());
        
        test = new LocalDate(TEST_TIME1_UTC);
        assertEquals(false, test.isAfterToday());
        
        test = new LocalDate(TEST_TIME2_UTC);
        assertEquals(true, test.isAfterToday());
    }

    //-----------------------------------------------------------------------
    public void testIsAfter_RI() {
        LocalDate test = new LocalDate();
        assertEquals(false, test.isAfter(new LocalDate()));
        
        test = new LocalDate(TEST_TIME1_UTC);
        assertEquals(false, test.isAfter(null));
        
        test = new LocalDate(TEST_TIME1_UTC);
        assertEquals(false, test.isAfter(new LocalDate()));
        
        test = new LocalDate(TEST_TIME2_UTC);
        assertEquals(true, test.isAfter(new LocalDate()));
    }

    //-----------------------------------------------------------------------
    public void testIsBeforeToday() {
        LocalDate test = new LocalDate();
        assertEquals(false, test.isBeforeToday());
        
        test = new LocalDate(TEST_TIME1_UTC);
        assertEquals(true, test.isBeforeToday());
        
        test = new LocalDate(TEST_TIME2_UTC);
        assertEquals(false, test.isBeforeToday());
    }

    //-----------------------------------------------------------------------
    public void testIsBefore_RI() {
        LocalDate test = new LocalDate();
        assertEquals(false, test.isBefore(new LocalDate()));
        
        test = new LocalDate(TEST_TIME1_UTC);
        assertEquals(true, test.isBefore(null));
        
        test = new LocalDate(TEST_TIME1_UTC);
        assertEquals(true, test.isBefore(new LocalDate()));
        
        test = new LocalDate(TEST_TIME2_UTC);
        assertEquals(false, test.isBefore(new LocalDate()));
    }

}
