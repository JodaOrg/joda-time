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

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.field.UnsupportedDateTimeField;
import org.joda.time.field.UnsupportedDurationField;

/**
 * This class is a Junit unit test for LocalTime.
 *
 * @author Stephen Colebourne
 */
public class TestLocalTime_Basics extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.getInstance("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.getInstance("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.getInstance("Asia/Tokyo");
    private static final int OFFSET = 1;
    private static final Chronology COPTIC_PARIS = Chronology.getCoptic(PARIS);
    private static final Chronology COPTIC_LONDON = Chronology.getCoptic(LONDON);
    private static final Chronology COPTIC_TOKYO = Chronology.getCoptic(TOKYO);
    private static final Chronology COPTIC_UTC = Chronology.getCopticUTC();
    private static final Chronology ISO_PARIS = Chronology.getISO(PARIS);
    private static final Chronology ISO_LONDON = Chronology.getISO(LONDON);
    private static final Chronology ISO_TOKYO = Chronology.getISO(TOKYO);
    private static final Chronology ISO_UTC = Chronology.getISOUTC();
    private static final Chronology BUDDHIST_PARIS = Chronology.getBuddhist(PARIS);
    private static final Chronology BUDDHIST_LONDON = Chronology.getBuddhist(LONDON);
    private static final Chronology BUDDHIST_TOKYO = Chronology.getBuddhist(TOKYO);
    private static final Chronology BUDDHIST_UTC = Chronology.getBuddhistUTC();
    
    private long TEST_TIME_NOW =
            10L * DateTimeConstants.MILLIS_PER_HOUR
            + 20L * DateTimeConstants.MILLIS_PER_MINUTE
            + 30L * DateTimeConstants.MILLIS_PER_SECOND
            + 40L;
            
    private long TEST_TIME1 =
        1L * DateTimeConstants.MILLIS_PER_HOUR
        + 2L * DateTimeConstants.MILLIS_PER_MINUTE
        + 3L * DateTimeConstants.MILLIS_PER_SECOND
        + 4L;
        
    private long TEST_TIME2 =
        1L * DateTimeConstants.MILLIS_PER_DAY
        + 5L * DateTimeConstants.MILLIS_PER_HOUR
        + 6L * DateTimeConstants.MILLIS_PER_MINUTE
        + 7L * DateTimeConstants.MILLIS_PER_SECOND
        + 8L;
        
    private DateTimeZone zone = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestLocalTime_Basics.class);
    }

    public TestLocalTime_Basics(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        zone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(LONDON);
    }

    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(zone);
        zone = null;
    }

    //-----------------------------------------------------------------------
    public void testGetMethods() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(20 + (10 * 60), test.getMinuteOfDay());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(30 + ((20 + (10 * 60)) * 60), test.getSecondOfDay());
        assertEquals(40, test.getMillisOfSecond());
        assertEquals(40 + ((30 + ((20 + (10 * 60)) * 60)) * 1000), test.getMillisOfDay());
    }

    //-----------------------------------------------------------------------
    public void testGet_DateTimeFieldType() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        assertEquals(10, test.get(DateTimeFieldType.hourOfDay()));
        assertEquals(20, test.get(DateTimeFieldType.minuteOfHour()));
        assertEquals(30, test.get(DateTimeFieldType.secondOfMinute()));
        assertEquals(40, test.get(DateTimeFieldType.millisOfSecond()));
        try {
            test.get(null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.get(DateTimeFieldType.dayOfMonth());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.get(BAD_DATETIMETYPE);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testProperty_DateTimeFieldType() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        assertEquals(test.hourOfDay(), test.property(DateTimeFieldType.hourOfDay()));
        assertEquals(test.minuteOfHour(), test.property(DateTimeFieldType.minuteOfHour()));
        assertEquals(test.secondOfMinute(), test.property(DateTimeFieldType.secondOfMinute()));
        assertEquals(test.millisOfSecond(), test.property(DateTimeFieldType.millisOfSecond()));
        try {
            test.property(null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.property(DateTimeFieldType.dayOfMonth());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.property(BAD_DATETIMETYPE);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testIsSupported_DateTimeFieldType() {
        LocalTime test = new LocalTime(COPTIC_PARIS);
        assertEquals(true, test.isSupported(DateTimeFieldType.hourOfDay()));
        assertEquals(true, test.isSupported(DateTimeFieldType.minuteOfHour()));
        assertEquals(true, test.isSupported(DateTimeFieldType.secondOfMinute()));
        assertEquals(true, test.isSupported(DateTimeFieldType.millisOfSecond()));
        assertEquals(false, test.isSupported(DateTimeFieldType.dayOfMonth()));
        assertEquals(false, test.isSupported(BAD_DATETIMETYPE));
    }

    //-----------------------------------------------------------------------
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
    public void testEqualsHashCode() {
        LocalTime test1 = new LocalTime(10, 20, 30, 40, COPTIC_PARIS);
        LocalTime test2 = new LocalTime(10, 20, 30, 40, COPTIC_PARIS);
        assertEquals(true, test1.equals(test2));
        assertEquals(true, test2.equals(test1));
        assertEquals(true, test1.equals(test1));
        assertEquals(true, test2.equals(test2));
        assertEquals(true, test1.hashCode() == test2.hashCode());
        assertEquals(true, test1.hashCode() == test1.hashCode());
        assertEquals(true, test2.hashCode() == test2.hashCode());
        
        LocalTime test3 = new LocalTime(15, 20, 30, 40);
        assertEquals(false, test1.equals(test3));
        assertEquals(false, test2.equals(test3));
        assertEquals(false, test3.equals(test1));
        assertEquals(false, test3.equals(test2));
        assertEquals(false, test1.hashCode() == test3.hashCode());
        assertEquals(false, test2.hashCode() == test3.hashCode());
        
        assertEquals(false, test1.equals("Hello"));
        assertEquals(false, test1.equals(new LocalDate()));
    }

    //-----------------------------------------------------------------------
    public void testSerialization() throws Exception {
        LocalTime test = new LocalTime(10, 20, 30, 40, COPTIC_PARIS);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(test);
        byte[] bytes = baos.toByteArray();
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        LocalTime result = (LocalTime) ois.readObject();
        ois.close();
        
        assertEquals(test, result);
        assertEquals(test.getLocalMillis(), result.getLocalMillis());
        assertEquals(test.getChronology(), result.getChronology());
    }

    //-----------------------------------------------------------------------
    public void testToString() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        assertEquals("T10:20:30.040", test.toString());
    }

    public void testToString_String() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        assertEquals("10,20", test.toString("hh,mm"));
        assertEquals("T10:20:30.040", test.toString(null));
    }

    public void testToString_String_String() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        assertEquals("10,20", test.toString("hh,mm", Locale.ENGLISH));
        assertEquals("10,20", test.toString("hh,mm", Locale.FRENCH));
        assertEquals("T10:20:30.040", test.toString(null, Locale.ENGLISH));
        assertEquals("10,20", test.toString("hh,mm", null));
        assertEquals("T10:20:30.040", test.toString(null, null));
    }

    //-----------------------------------------------------------------------
    public void testToDateTimeToday() {
        LocalTime base = new LocalTime(10, 20, 30, 40, COPTIC_PARIS); // PARIS irrelevant
        DateTime dt = new DateTime(2004, 6, 9, 6, 7, 8, 9);
        DateTimeUtils.setCurrentMillisFixed(dt.getMillis());
        
        DateTime test = base.toDateTimeToday();
        check(base, 10, 20, 30, 40);
        DateTime expected = new DateTime(dt.getMillis(), COPTIC_LONDON);
        expected = expected.hourOfDay().setCopy(10);
        expected = expected.minuteOfHour().setCopy(20);
        expected = expected.secondOfMinute().setCopy(30);
        expected = expected.millisOfSecond().setCopy(40);
        assertEquals(expected, test);
    }

    //-----------------------------------------------------------------------
    public void testToDateTimeToday_Zone() {
        LocalTime base = new LocalTime(10, 20, 30, 40, COPTIC_PARIS); // PARIS irrelevant
        DateTime dt = new DateTime(2004, 6, 9, 6, 7, 8, 9);
        DateTimeUtils.setCurrentMillisFixed(dt.getMillis());
        
        DateTime test = base.toDateTimeToday(TOKYO);
        check(base, 10, 20, 30, 40);
        DateTime expected = new DateTime(dt.getMillis(), COPTIC_TOKYO);
        expected = expected.hourOfDay().setCopy(10);
        expected = expected.minuteOfHour().setCopy(20);
        expected = expected.secondOfMinute().setCopy(30);
        expected = expected.millisOfSecond().setCopy(40);
        assertEquals(expected, test);
    }

    public void testToDateTimeToday_nullZone() {
        LocalTime base = new LocalTime(10, 20, 30, 40, COPTIC_PARIS); // PARIS irrelevant
        DateTime dt = new DateTime(2004, 6, 9, 6, 7, 8, 9);
        DateTimeUtils.setCurrentMillisFixed(dt.getMillis());
        
        DateTime test = base.toDateTimeToday((DateTimeZone) null);
        check(base, 10, 20, 30, 40);
        DateTime expected = new DateTime(dt.getMillis(), COPTIC_LONDON);
        expected = expected.hourOfDay().setCopy(10);
        expected = expected.minuteOfHour().setCopy(20);
        expected = expected.secondOfMinute().setCopy(30);
        expected = expected.millisOfSecond().setCopy(40);
        assertEquals(expected, test);
    }

    //-----------------------------------------------------------------------
    public void testToDateTime_RI() {
        LocalTime base = new LocalTime(10, 20, 30, 40, COPTIC_PARIS);
        DateTime dt = new DateTime(0L); // LONDON zone
        assertEquals("1970-01-01T01:00:00.000+01:00", dt.toString());
        
        DateTime test = base.toDateTime(dt);
        check(base, 10, 20, 30, 40);
        assertEquals("1970-01-01T01:00:00.000+01:00", dt.toString());
        assertEquals("1970-01-01T10:20:30.040+01:00", test.toString());
    }

    public void testToDateTime_nullRI() {
        LocalTime base = new LocalTime(1, 2, 3, 4);
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME2);
        
        DateTime test = base.toDateTime((ReadableInstant) null);
        check(base, 1, 2, 3, 4);
        assertEquals("1970-01-02T01:02:03.004+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testToDateTime_LocalDate() {
        LocalTime base = new LocalTime(10, 20, 30, 40, COPTIC_PARIS); // PARIS irrelevant
        LocalDate date = new LocalDate(2004, 6, 9, BUDDHIST_TOKYO);

        DateTime test = base.toDateTime(date);
        check(base, 10, 20, 30, 40);
        DateTime expected = new DateTime(2004, 6, 9, 10, 20, 30, 40, COPTIC_LONDON);
        assertEquals(expected, test);
    }

    public void testToDateTime_nullLocalDate() {
        LocalTime base = new LocalTime(10, 20, 30, 40, COPTIC_PARIS); // PARIS irrelevant

        DateTime test = base.toDateTime((LocalDate) null);
        check(base, 10, 20, 30, 40);
        DateTime expected = new DateTime(COPTIC_LONDON);
        expected = expected.withTime(10, 20, 30, 40);
        assertEquals(expected, test);
    }

    //-----------------------------------------------------------------------
    public void testToDateTime_LocalDate_Zone() {
        LocalTime base = new LocalTime(10, 20, 30, 40, COPTIC_PARIS); // PARIS irrelevant
        LocalDate date = new LocalDate(2004, 6, 9, BUDDHIST_LONDON);

        DateTime test = base.toDateTime(date, TOKYO);
        check(base, 10, 20, 30, 40);
        DateTime expected = new DateTime(2004, 6, 9, 10, 20, 30, 40, COPTIC_TOKYO);
        assertEquals(expected, test);
    }

    public void testToDateTime_LocalDate_nullZone() {
        LocalTime base = new LocalTime(10, 20, 30, 40, COPTIC_PARIS); // PARIS irrelevant
        LocalDate date = new LocalDate(2004, 6, 9, BUDDHIST_LONDON);

        DateTime test = base.toDateTime(date, null);
        check(base, 10, 20, 30, 40);
        DateTime expected = new DateTime(2004, 6, 9, 10, 20, 30, 40, COPTIC_LONDON);
        assertEquals(expected, test);
    }

    public void testToDateTime_nullLocalDate_Zone() {
        LocalTime base = new LocalTime(10, 20, 30, 40, COPTIC_PARIS); // PARIS irrelevant

        DateTime test = base.toDateTime((LocalDate) null, TOKYO);
        check(base, 10, 20, 30, 40);
        DateTime expected = new DateTime(COPTIC_TOKYO);
        expected = expected.withTime(10, 20, 30, 40);
        assertEquals(expected, test);
    }

    //-----------------------------------------------------------------------
    public void testWithChronologyRetainFields_Chrono() {
        LocalTime base = new LocalTime(10, 20, 30, 40, COPTIC_PARIS);
        LocalTime test = base.withChronologyRetainFields(BUDDHIST_TOKYO);
        check(base, 10, 20, 30, 40);
        assertEquals(COPTIC_UTC, base.getChronology());
        check(test, 10, 20, 30, 40);
        assertEquals(BUDDHIST_UTC, test.getChronology());
    }

    public void testWithChronologyRetainFields_sameChrono() {
        LocalTime base = new LocalTime(10, 20, 30, 40, COPTIC_PARIS);
        LocalTime test = base.withChronologyRetainFields(COPTIC_TOKYO);
        assertSame(base, test);
    }

    public void testWithChronologyRetainFields_nullChrono() {
        LocalTime base = new LocalTime(10, 20, 30, 40, COPTIC_PARIS);
        LocalTime test = base.withChronologyRetainFields(null);
        check(base, 10, 20, 30, 40);
        assertEquals(COPTIC_UTC, base.getChronology());
        check(test, 10, 20, 30, 40);
        assertEquals(ISO_UTC, test.getChronology());
    }

    //-----------------------------------------------------------------------
    public void testWithField1() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime result = test.withField(DateTimeFieldType.hourOfDay(), 15);
        
        assertEquals(new LocalTime(10, 20, 30, 40), test);
        assertEquals(new LocalTime(15, 20, 30, 40), result);
    }

    public void testWithField2() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        try {
            test.withField(null, 6);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testWithField3() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        try {
            test.withField(DateTimeFieldType.dayOfMonth(), 6);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testWithFieldAdded1() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime result = test.withFieldAdded(DurationFieldType.hours(), 6);
        
        assertEquals(new LocalTime(10, 20, 30, 40), test);
        assertEquals(new LocalTime(16, 20, 30, 40), result);
    }

    public void testWithFieldAdded2() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        try {
            test.withFieldAdded(null, 0);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testWithFieldAdded3() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        try {
            test.withFieldAdded(null, 6);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testWithFieldAdded4() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime result = test.withFieldAdded(DurationFieldType.hours(), 0);
        assertSame(test, result);
    }

    public void testWithFieldAdded5() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        try {
            test.withFieldAdded(DurationFieldType.days(), 6);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testWithFieldAdded6() {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        LocalTime result = test.withFieldAdded(DurationFieldType.hours(), 16);
        
        assertEquals(new LocalTime(10, 20, 30, 40), test);
        assertEquals(new LocalTime(2, 20, 30, 40), result);
    }

    public void testPlus_RP() {
        LocalTime test = new LocalTime(10, 20, 30, 40, BuddhistChronology.getInstance());
        LocalTime result = test.plus(new Period(1, 2, 3, 4, 5, 6, 7, 8));
        LocalTime expected = new LocalTime(15, 26, 37, 48, BuddhistChronology.getInstance());
        assertEquals(expected, result);
        
        result = test.plus((ReadablePeriod) null);
        assertSame(test, result);
    }

    public void testMinus_RP() {
        LocalTime test = new LocalTime(10, 20, 30, 40, BuddhistChronology.getInstance());
        LocalTime result = test.minus(new Period(1, 1, 1, 1, 1, 1, 1, 1));
        LocalTime expected = new LocalTime(9, 19, 29, 39, BuddhistChronology.getInstance());
        assertEquals(expected, result);
        
        result = test.minus((ReadablePeriod) null);
        assertSame(test, result);
    }

    //-----------------------------------------------------------------------
    public void testCompareTo_Object() {
        LocalTime test = new LocalTime();
        try {
            test.compareTo(null);
            fail();
        } catch (NullPointerException ex) {}
        try {
            test.compareTo(new DateTime());
            fail();
        } catch (ClassCastException ex) {}
        try {
            test.compareTo(new LocalTime(Chronology.getBuddhistUTC()));
            fail();
        } catch (IllegalArgumentException ex) {}
        
        test = new LocalTime(10, 20, 30, 40);
        assertEquals(0, test.compareTo(new LocalTime(10, 20, 30, 40)));
        
        test = new LocalTime(10, 20, 30, 41);
        assertEquals(1, test.compareTo(new LocalTime(10, 20, 30, 40)));
        
        test = new LocalTime(10, 20, 30, 39);
        assertEquals(-1, test.compareTo(new LocalTime(10, 20, 30, 40)));
    }

    //-----------------------------------------------------------------------
    public void testIsAfterNow() {
        LocalTime test = new LocalTime();  // 11, 20, 30, 40
        assertEquals(false, test.isAfterNow());
        
        test = new LocalTime(11, 20, 30, 39);
        assertEquals(false, test.isAfterNow());
        
        test = new LocalTime(11, 20, 30, 41);
        assertEquals(true, test.isAfterNow());
    }

    //-----------------------------------------------------------------------
    public void testIsAfter_RI() {
        LocalTime test = new LocalTime();
        assertEquals(false, test.isAfter(new LocalTime()));
        
        test = new LocalTime();
        assertEquals(false, test.isAfter(null));
        
        test = new LocalTime(11, 20, 30, 39);
        assertEquals(false, test.isAfter(new LocalTime()));
        
        test = new LocalTime(11, 20, 30, 41);
        assertEquals(true, test.isAfter(new LocalTime()));
    }

    //-----------------------------------------------------------------------
    public void testIsBeforeNow() {
        LocalTime test = new LocalTime();  // 11, 20, 30, 40
        assertEquals(false, test.isBeforeNow());
        
        test = new LocalTime(11, 20, 30, 39);
        assertEquals(true, test.isBeforeNow());
        
        test = new LocalTime(11, 20, 30, 41);
        assertEquals(false, test.isBeforeNow());
    }

    //-----------------------------------------------------------------------
    public void testIsBefore_RI() {
        LocalTime test = new LocalTime();
        assertEquals(false, test.isBefore(new LocalTime()));
        
        test = new LocalTime();
        assertEquals(false, test.isBefore(null));
        
        test = new LocalTime(11, 20, 30, 39);
        assertEquals(true, test.isBefore(new LocalTime()));
        
        test = new LocalTime(11, 20, 30, 41);
        assertEquals(false, test.isBefore(new LocalTime()));
    }

    //-----------------------------------------------------------------------
    private void check(LocalTime test, int hour, int min, int sec, int milli) {
        assertEquals(hour, test.getHourOfDay());
        assertEquals(min, test.getMinuteOfHour());
        assertEquals(sec, test.getSecondOfMinute());
        assertEquals(milli, test.getMillisOfSecond());
    }
}
