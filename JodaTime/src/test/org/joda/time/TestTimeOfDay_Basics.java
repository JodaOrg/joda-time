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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Locale;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This class is a Junit unit test for TimeOfDay.
 *
 * @author Stephen Colebourne
 */
public class TestTimeOfDay_Basics extends TestCase {

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
        return new TestSuite(TestTimeOfDay_Basics.class);
    }

    public TestTimeOfDay_Basics(String name) {
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
    public void testGet() {
        TimeOfDay test = new TimeOfDay();
        assertEquals(10 + OFFSET, test.get(DateTimeFieldType.hourOfDay()));
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
    }

    public void testSize() {
        TimeOfDay test = new TimeOfDay();
        assertEquals(4, test.size());
    }

    public void testGetFieldType() {
        TimeOfDay test = new TimeOfDay(COPTIC_PARIS);
        assertSame(DateTimeFieldType.hourOfDay(), test.getFieldType(0));
        assertSame(DateTimeFieldType.minuteOfHour(), test.getFieldType(1));
        assertSame(DateTimeFieldType.secondOfMinute(), test.getFieldType(2));
        assertSame(DateTimeFieldType.millisOfSecond(), test.getFieldType(3));
        try {
            test.getFieldType(-1);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            test.getFieldType(5);
        } catch (IndexOutOfBoundsException ex) {}
    }

    public void testGetFieldTypes() {
        TimeOfDay test = new TimeOfDay(COPTIC_PARIS);
        DateTimeFieldType[] fields = test.getFieldTypes();
        assertSame(DateTimeFieldType.hourOfDay(), fields[0]);
        assertSame(DateTimeFieldType.minuteOfHour(), fields[1]);
        assertSame(DateTimeFieldType.secondOfMinute(), fields[2]);
        assertSame(DateTimeFieldType.millisOfSecond(), fields[3]);
        assertNotSame(test.getFieldTypes(), test.getFieldTypes());
    }

    public void testGetField() {
        TimeOfDay test = new TimeOfDay(COPTIC_PARIS);
        assertSame(Chronology.getCopticUTC().hourOfDay(), test.getField(0));
        assertSame(Chronology.getCopticUTC().minuteOfHour(), test.getField(1));
        assertSame(Chronology.getCopticUTC().secondOfMinute(), test.getField(2));
        assertSame(Chronology.getCopticUTC().millisOfSecond(), test.getField(3));
        try {
            test.getField(-1);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            test.getField(5);
        } catch (IndexOutOfBoundsException ex) {}
    }

    public void testGetFields() {
        TimeOfDay test = new TimeOfDay(COPTIC_PARIS);
        DateTimeField[] fields = test.getFields();
        assertSame(Chronology.getCopticUTC().hourOfDay(), fields[0]);
        assertSame(Chronology.getCopticUTC().minuteOfHour(), fields[1]);
        assertSame(Chronology.getCopticUTC().secondOfMinute(), fields[2]);
        assertSame(Chronology.getCopticUTC().millisOfSecond(), fields[3]);
        assertNotSame(test.getFields(), test.getFields());
    }

    public void testGetValue() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40, COPTIC_PARIS);
        assertEquals(10, test.getValue(0));
        assertEquals(20, test.getValue(1));
        assertEquals(30, test.getValue(2));
        assertEquals(40, test.getValue(3));
        try {
            test.getValue(-1);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            test.getValue(5);
        } catch (IndexOutOfBoundsException ex) {}
    }

    public void testGetValues() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40, COPTIC_PARIS);
        int[] values = test.getValues();
        assertEquals(10, values[0]);
        assertEquals(20, values[1]);
        assertEquals(30, values[2]);
        assertEquals(40, values[3]);
        assertNotSame(test.getValues(), test.getValues());
    }

    public void testIsSupported() {
        TimeOfDay test = new TimeOfDay(COPTIC_PARIS);
        assertEquals(true, test.isSupported(DateTimeFieldType.hourOfDay()));
        assertEquals(true, test.isSupported(DateTimeFieldType.minuteOfHour()));
        assertEquals(true, test.isSupported(DateTimeFieldType.secondOfMinute()));
        assertEquals(true, test.isSupported(DateTimeFieldType.millisOfSecond()));
        assertEquals(false, test.isSupported(DateTimeFieldType.dayOfMonth()));
    }

    public void testEqualsHashCode() {
        TimeOfDay test1 = new TimeOfDay(10, 20, 30, 40, COPTIC_PARIS);
        TimeOfDay test2 = new TimeOfDay(10, 20, 30, 40, COPTIC_PARIS);
        assertEquals(true, test1.equals(test2));
        assertEquals(true, test2.equals(test1));
        assertEquals(true, test1.equals(test1));
        assertEquals(true, test2.equals(test2));
        assertEquals(true, test1.hashCode() == test2.hashCode());
        assertEquals(true, test1.hashCode() == test1.hashCode());
        assertEquals(true, test2.hashCode() == test2.hashCode());
        
        TimeOfDay test3 = new TimeOfDay(15, 20, 30, 40);
        assertEquals(false, test1.equals(test3));
        assertEquals(false, test2.equals(test3));
        assertEquals(false, test3.equals(test1));
        assertEquals(false, test3.equals(test2));
        assertEquals(false, test1.hashCode() == test3.hashCode());
        assertEquals(false, test2.hashCode() == test3.hashCode());
        
        assertEquals(false, test1.equals("Hello"));
        assertEquals(true, test1.equals(new MockInstant()));
        assertEquals(false, test1.equals(MockPartial.EMPTY_INSTANCE));
    }
    
    class MockInstant extends MockPartial {
        public Chronology getChronology() {
            return Chronology.getCopticUTC();
        }
        public DateTimeField[] getFields() {
            return new DateTimeField[] {
                Chronology.getCopticUTC().hourOfDay(),
                Chronology.getCopticUTC().minuteOfHour(),
                Chronology.getCopticUTC().secondOfMinute(),
                Chronology.getCopticUTC().millisOfSecond(),
            };
        }
        public int[] getValues() {
            return new int[] {10, 20, 30, 40};
        }
    }

    //-----------------------------------------------------------------------
    public void testWithChronologyRetainFields_Chrono() {
        TimeOfDay base = new TimeOfDay(10, 20, 30, 40, COPTIC_PARIS);
        TimeOfDay test = base.withChronologyRetainFields(BUDDHIST_TOKYO);
        check(base, 10, 20, 30, 40);
        assertEquals(COPTIC_UTC, base.getChronology());
        check(test, 10, 20, 30, 40);
        assertEquals(BUDDHIST_UTC, test.getChronology());
    }

    public void testWithChronologyRetainFields_sameChrono() {
        TimeOfDay base = new TimeOfDay(10, 20, 30, 40, COPTIC_PARIS);
        TimeOfDay test = base.withChronologyRetainFields(COPTIC_TOKYO);
        assertSame(base, test);
    }

    public void testWithChronologyRetainFields_nullChrono() {
        TimeOfDay base = new TimeOfDay(10, 20, 30, 40, COPTIC_PARIS);
        TimeOfDay test = base.withChronologyRetainFields(null);
        check(base, 10, 20, 30, 40);
        assertEquals(COPTIC_UTC, base.getChronology());
        check(test, 10, 20, 30, 40);
        assertEquals(ISO_UTC, test.getChronology());
    }

    //-----------------------------------------------------------------------
    public void testToDateTimeToday() {
        TimeOfDay base = new TimeOfDay(10, 20, 30, 40, COPTIC_PARIS); // PARIS irrelevant
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
        TimeOfDay base = new TimeOfDay(10, 20, 30, 40, COPTIC_PARIS); // PARIS irrelevant
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
        TimeOfDay base = new TimeOfDay(10, 20, 30, 40, COPTIC_PARIS); // PARIS irrelevant
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

    // Removed as too complex
//    /**
//     * Merges two partial together, taking account of the different chronologies.
//     *
//     * @param main  the main partial
//     * @param base  the partial to use as a base to merge on top of
//     * @param instant  the instant to start from and to use for missing fields
//     * @return the merged instant
//     */
//    public long merge(ReadablePartial main, ReadablePartial base, long instant) {
//        DateTimeZone zone = main.getChronology().getZone();
//        instant = base.getChronology().withZone(zone).set(base, instant);
//        return set(main, instant);
//    }
//
//    //-----------------------------------------------------------------------
//    /**
//     * Converts this object to a DateTime using a YearMonthDay to fill in the
//     * missing fields and using the default time zone.
//     * This instance is immutable and unaffected by this method call.
//     * <p>
//     * The resulting chronology is determined by the chronology of this
//     * TimeOfDay plus the time zone.
//     * <p>
//     * This method makes use of the chronology of the specified YearMonthDay
//     * in the calculation. This can be significant when mixing chronologies.
//     * If the YearMonthDay is in the same chronology as this instance the
//     * method will perform exactly as you might expect.
//     * <p>
//     * If the chronologies differ, then both this TimeOfDay and the YearMonthDay
//     * are converted to the destination chronology and then merged. As a result
//     * it may be the case that the year, monthOfYear and dayOfMonth fields on
//     * the result are different from the values returned by the methods on the
//     * YearMonthDay.
//     * <p>
//     * See {@link DateTime#withFields(ReadablePartial)} for an algorithm that
//     * ignores the chronology.
//     *
//     * @param date  the date to use, null means today
//     * @return the DateTime instance
//     */
//    public DateTime toDateTime(YearMonthDay date) {
//        return toDateTime(date, null);
//    }
//
//    /**
//     * Converts this object to a DateTime using a YearMonthDay to fill in the
//     * missing fields.
//     * This instance is immutable and unaffected by this method call.
//     * <p>
//     * The resulting chronology is determined by the chronology of this
//     * TimeOfDay plus the time zone.
//     * <p>
//     * This method makes use of the chronology of the specified YearMonthDay
//     * in the calculation. This can be significant when mixing chronologies.
//     * If the YearMonthDay is in the same chronology as this instance the
//     * method will perform exactly as you might expect.
//     * <p>
//     * If the chronologies differ, then both this TimeOfDay and the YearMonthDay
//     * are converted to the destination chronology and then merged. As a result
//     * it may be the case that the year, monthOfYear and dayOfMonth fields on
//     * the result are different from the values returned by the methods on the
//     * YearMonthDay.
//     * <p>
//     * See {@link DateTime#withFields(ReadablePartial)} for an algorithm that
//     * ignores the chronology and just assigns the fields.
//     *
//     * @param date  the date to use, null means today
//     * @param zone  the zone to get the DateTime in, null means default
//     * @return the DateTime instance
//     */
//    public DateTime toDateTime(YearMonthDay date, DateTimeZone zone) {
//        Chronology chrono = getChronology().withZone(zone);
//        if (date == null) {
//            DateTime dt = new DateTime(chrono);
//            return dt.withFields(this);
//        } else {
//            long millis = chrono.merge(this, date, DateTimeUtils.currentTimeMillis());
//            return new DateTime(millis, chrono);
//        }
//    }
//
//    //-----------------------------------------------------------------------
//    public void testToDateTime_YMD() {
//        TimeOfDay base = new TimeOfDay(10, 20, 30, 40, COPTIC_PARIS); // PARIS irrelevant
//        YearMonthDay ymd = new YearMonthDay(new DateMidnight(2004, 6, 9), BUDDHIST_TOKYO);
//        
//        DateTime test = base.toDateTime(ymd);
//        check(base, 10, 20, 30, 40);
//        DateTime expected = new DateTime(ymd.toDateMidnight(LONDON), COPTIC_LONDON);
//        expected = expected.hourOfDay().setCopy(10);
//        expected = expected.minuteOfHour().setCopy(20);
//        expected = expected.secondOfMinute().setCopy(30);
//        expected = expected.millisOfSecond().setCopy(40);
//        assertEquals(expected, test);
//    }
//
//    public void testToDateTime_nullYMD() {
//        TimeOfDay base = new TimeOfDay(10, 20, 30, 40, COPTIC_PARIS); // PARIS irrelevant
//        
//        DateTime test = base.toDateTime((YearMonthDay) null);
//        check(base, 10, 20, 30, 40);
//        DateTime expected = new DateTime(COPTIC_LONDON);
//        expected = expected.hourOfDay().setCopy(10);
//        expected = expected.minuteOfHour().setCopy(20);
//        expected = expected.secondOfMinute().setCopy(30);
//        expected = expected.millisOfSecond().setCopy(40);
//        assertEquals(expected, test);
//    }
//
//    //-----------------------------------------------------------------------
//    public void testToDateTime_YMD_Zone() {
//        TimeOfDay base = new TimeOfDay(10, 20, 30, 40, COPTIC_PARIS); // PARIS irrelevant
//        YearMonthDay ymd = new YearMonthDay(new DateMidnight(2004, 6, 9), BUDDHIST_LONDON);
//        
//        DateTime test = base.toDateTime(ymd, TOKYO);
//        check(base, 10, 20, 30, 40);
//        DateTime expected = new DateTime(ymd.toDateMidnight(TOKYO), COPTIC_TOKYO);
//        expected = expected.hourOfDay().setCopy(10);
//        expected = expected.minuteOfHour().setCopy(20);
//        expected = expected.secondOfMinute().setCopy(30);
//        expected = expected.millisOfSecond().setCopy(40);
//        assertEquals(expected, test);
//    }
//
//    public void testToDateTime_YMD_nullZone() {
//        TimeOfDay base = new TimeOfDay(10, 20, 30, 40, COPTIC_PARIS); // PARIS irrelevant
//        YearMonthDay ymd = new YearMonthDay(new DateMidnight(2004, 6, 9), BUDDHIST_LONDON);
//        
//        DateTime test = base.toDateTime(ymd, null);
//        check(base, 10, 20, 30, 40);
//        DateTime expected = new DateTime(ymd.toDateMidnight(LONDON), COPTIC_LONDON);
//        expected = expected.hourOfDay().setCopy(10);
//        expected = expected.minuteOfHour().setCopy(20);
//        expected = expected.secondOfMinute().setCopy(30);
//        expected = expected.millisOfSecond().setCopy(40);
//        assertEquals(expected, test);
//    }
//
//    public void testToDateTime_nullYMD_Zone() {
//        TimeOfDay base = new TimeOfDay(10, 20, 30, 40, COPTIC_PARIS); // PARIS irrelevant
//        
//        DateTime test = base.toDateTime((YearMonthDay) null, TOKYO);
//        check(base, 10, 20, 30, 40);
//        DateTime expected = new DateTime(COPTIC_TOKYO);
//        expected = expected.hourOfDay().setCopy(10);
//        expected = expected.minuteOfHour().setCopy(20);
//        expected = expected.secondOfMinute().setCopy(30);
//        expected = expected.millisOfSecond().setCopy(40);
//        assertEquals(expected, test);
//    }

    //-----------------------------------------------------------------------
    public void testToDateTime_RI() {
        TimeOfDay base = new TimeOfDay(10, 20, 30, 40, COPTIC_PARIS);
        DateTime dt = new DateTime(0L); // LONDON zone
        assertEquals("1970-01-01T01:00:00.000+01:00", dt.toString());
        
        DateTime test = base.toDateTime(dt);
        check(base, 10, 20, 30, 40);
        assertEquals("1970-01-01T01:00:00.000+01:00", dt.toString());
        assertEquals("1970-01-01T10:20:30.040+01:00", test.toString());
    }

    public void testToDateTime_nullRI() {
        TimeOfDay base = new TimeOfDay(1, 2, 3, 4);
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME2);
        
        DateTime test = base.toDateTime((ReadableInstant) null);
        check(base, 1, 2, 3, 4);
        assertEquals("1970-01-02T01:02:03.004+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testProperty() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        assertEquals(test.hourOfDay(), test.property(DateTimeFieldType.hourOfDay()));
        assertEquals(test.minuteOfHour(), test.property(DateTimeFieldType.minuteOfHour()));
        assertEquals(test.secondOfMinute(), test.property(DateTimeFieldType.secondOfMinute()));
        assertEquals(test.millisOfSecond(), test.property(DateTimeFieldType.millisOfSecond()));
        try {
            test.property(DateTimeFieldType.millisOfDay());
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testSerialization() throws Exception {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40, COPTIC_PARIS);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(test);
        byte[] bytes = baos.toByteArray();
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        TimeOfDay result = (TimeOfDay) ois.readObject();
        ois.close();
        
        assertEquals(test, result);
        assertTrue(Arrays.equals(test.getValues(), result.getValues()));
        assertTrue(Arrays.equals(test.getFields(), result.getFields()));
        assertEquals(test.getChronology(), result.getChronology());
    }

    //-----------------------------------------------------------------------
    public void testToString() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        assertEquals("T10:20:30.040", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testToString_String() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        assertEquals(" 10", test.toString("yyyy HH"));
        assertEquals("T10:20:30.040", test.toString(null));
    }

    //-----------------------------------------------------------------------
    public void testToString_String_Locale() {
        TimeOfDay test = new TimeOfDay(10, 20, 30, 40);
        assertEquals("10 20", test.toString("H m", Locale.ENGLISH));
        assertEquals("T10:20:30.040", test.toString(null, Locale.ENGLISH));
        assertEquals("10 20", test.toString("H m", null));
        assertEquals("T10:20:30.040", test.toString(null, null));
    }

    //-----------------------------------------------------------------------
    private void check(TimeOfDay test, int hour, int min, int sec, int milli) {
        assertEquals(hour, test.getHourOfDay());
        assertEquals(min, test.getMinuteOfHour());
        assertEquals(sec, test.getSecondOfMinute());
        assertEquals(milli, test.getMillisOfSecond());
    }
}
