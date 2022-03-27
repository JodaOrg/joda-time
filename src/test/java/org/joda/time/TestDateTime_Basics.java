/*
 *  Copyright 2001-2013 Stephen Colebourne
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
package org.joda.time;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.base.AbstractInstant;
import org.joda.time.chrono.BaseChronology;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.UnsupportedDateTimeField;
import org.joda.time.field.UnsupportedDurationField;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * This class is a Junit unit test for DateTime.
 *
 * @author Stephen Colebourne
 */
public class TestDateTime_Basics extends TestCase {
    // Test in 2002/03 as time zones are more well known
    // (before the late 90's they were all over the place)

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    // the default time zone is set to LONDON in setUp()
    // we have to hard code LONDON here (instead of ISOChronology.getInstance() etc.)
    // as TestAll sets up a different time zone for better all-round testing
    private static final ISOChronology ISO_UTC = ISOChronology.getInstanceUTC();
    private static final ISOChronology ISO_DEFAULT = ISOChronology.getInstance(LONDON);
    private static final ISOChronology ISO_PARIS = ISOChronology.getInstance(PARIS);
    private static final GJChronology GJ_DEFAULT = GJChronology.getInstance(LONDON);
    private static final GregorianChronology GREGORIAN_DEFAULT = GregorianChronology.getInstance(LONDON);
    private static final GregorianChronology GREGORIAN_PARIS = GregorianChronology.getInstance(PARIS);
    private static final BuddhistChronology BUDDHIST_UTC = BuddhistChronology.getInstanceUTC();
    private static final BuddhistChronology BUDDHIST_DEFAULT = BuddhistChronology.getInstance(LONDON);
    private static final CopticChronology COPTIC_DEFAULT = CopticChronology.getInstance(LONDON);
    
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
        return new TestSuite(TestDateTime_Basics.class);
    }

    public TestDateTime_Basics(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @Override
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
    public void testGet_DateTimeField() {
        DateTime test = new DateTime();
        assertEquals(1, test.get(ISO_DEFAULT.era()));
        assertEquals(20, test.get(ISO_DEFAULT.centuryOfEra()));
        assertEquals(2, test.get(ISO_DEFAULT.yearOfCentury()));
        assertEquals(2002, test.get(ISO_DEFAULT.yearOfEra()));
        assertEquals(2002, test.get(ISO_DEFAULT.year()));
        assertEquals(6, test.get(ISO_DEFAULT.monthOfYear()));
        assertEquals(9, test.get(ISO_DEFAULT.dayOfMonth()));
        assertEquals(2002, test.get(ISO_DEFAULT.weekyear()));
        assertEquals(23, test.get(ISO_DEFAULT.weekOfWeekyear()));
        assertEquals(7, test.get(ISO_DEFAULT.dayOfWeek()));
        assertEquals(160, test.get(ISO_DEFAULT.dayOfYear()));
        assertEquals(0, test.get(ISO_DEFAULT.halfdayOfDay()));
        assertEquals(1, test.get(ISO_DEFAULT.hourOfHalfday()));
        assertEquals(1, test.get(ISO_DEFAULT.clockhourOfDay()));
        assertEquals(1, test.get(ISO_DEFAULT.clockhourOfHalfday()));
        assertEquals(1, test.get(ISO_DEFAULT.hourOfDay()));
        assertEquals(0, test.get(ISO_DEFAULT.minuteOfHour()));
        assertEquals(60, test.get(ISO_DEFAULT.minuteOfDay()));
        assertEquals(0, test.get(ISO_DEFAULT.secondOfMinute()));
        assertEquals(60 * 60, test.get(ISO_DEFAULT.secondOfDay()));
        assertEquals(0, test.get(ISO_DEFAULT.millisOfSecond()));
        assertEquals(60 * 60 * 1000, test.get(ISO_DEFAULT.millisOfDay()));
        try {
            test.get((DateTimeField) null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testGet_DateTimeFieldType() {
        DateTime test = new DateTime();
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
        assertEquals(0, test.get(DateTimeFieldType.halfdayOfDay()));
        assertEquals(1, test.get(DateTimeFieldType.hourOfHalfday()));
        assertEquals(1, test.get(DateTimeFieldType.clockhourOfDay()));
        assertEquals(1, test.get(DateTimeFieldType.clockhourOfHalfday()));
        assertEquals(1, test.get(DateTimeFieldType.hourOfDay()));
        assertEquals(0, test.get(DateTimeFieldType.minuteOfHour()));
        assertEquals(60, test.get(DateTimeFieldType.minuteOfDay()));
        assertEquals(0, test.get(DateTimeFieldType.secondOfMinute()));
        assertEquals(60 * 60, test.get(DateTimeFieldType.secondOfDay()));
        assertEquals(0, test.get(DateTimeFieldType.millisOfSecond()));
        assertEquals(60 * 60 * 1000, test.get(DateTimeFieldType.millisOfDay()));
        try {
            test.get((DateTimeFieldType) null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testIsSupported_DateTimeFieldType() {
        DateTime test = new DateTime();
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
        assertEquals(true, test.isSupported(DateTimeFieldType.halfdayOfDay()));
        assertEquals(true, test.isSupported(DateTimeFieldType.hourOfHalfday()));
        assertEquals(true, test.isSupported(DateTimeFieldType.clockhourOfDay()));
        assertEquals(true, test.isSupported(DateTimeFieldType.clockhourOfHalfday()));
        assertEquals(true, test.isSupported(DateTimeFieldType.hourOfDay()));
        assertEquals(true, test.isSupported(DateTimeFieldType.minuteOfHour()));
        assertEquals(true, test.isSupported(DateTimeFieldType.minuteOfDay()));
        assertEquals(true, test.isSupported(DateTimeFieldType.secondOfMinute()));
        assertEquals(true, test.isSupported(DateTimeFieldType.secondOfDay()));
        assertEquals(true, test.isSupported(DateTimeFieldType.millisOfSecond()));
        assertEquals(true, test.isSupported(DateTimeFieldType.millisOfDay()));
        assertEquals(false, test.isSupported(null));
    }

    //-----------------------------------------------------------------------
    public void testGetters() {
        DateTime test = new DateTime();
        
        assertEquals(ISO_DEFAULT, test.getChronology());
        assertEquals(LONDON, test.getZone());
        assertEquals(TEST_TIME_NOW, test.getMillis());
        
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
        assertEquals(1, test.getHourOfDay());
        assertEquals(0, test.getMinuteOfHour());
        assertEquals(60, test.getMinuteOfDay());
        assertEquals(0, test.getSecondOfMinute());
        assertEquals(60 * 60, test.getSecondOfDay());
        assertEquals(0, test.getMillisOfSecond());
        assertEquals(60 * 60 * 1000, test.getMillisOfDay());
    }

    public void testWithers() {
        DateTime test = new DateTime(1970, 6, 9, 10, 20, 30, 40, GJ_DEFAULT);
        check(test.withYear(2000), 2000, 6, 9, 10, 20, 30, 40);
        check(test.withMonthOfYear(2), 1970, 2, 9, 10, 20, 30, 40);
        check(test.withDayOfMonth(2), 1970, 6, 2, 10, 20, 30, 40);
        check(test.withDayOfYear(6), 1970, 1, 6, 10, 20, 30, 40);
        check(test.withDayOfWeek(6), 1970, 6, 13, 10, 20, 30, 40);
        check(test.withWeekOfWeekyear(6), 1970, 2, 3, 10, 20, 30, 40);
        check(test.withWeekyear(1971), 1971, 6, 15, 10, 20, 30, 40);
        check(test.withYearOfCentury(60), 1960, 6, 9, 10, 20, 30, 40);
        check(test.withCenturyOfEra(21), 2070, 6, 9, 10, 20, 30, 40);
        check(test.withYearOfEra(1066), 1066, 6, 9, 10, 20, 30, 40);
        check(test.withEra(DateTimeConstants.BC), -1970, 6, 9, 10, 20, 30, 40);
        check(test.withHourOfDay(6), 1970, 6, 9, 6, 20, 30, 40);
        check(test.withMinuteOfHour(6), 1970, 6, 9, 10, 6, 30, 40);
        check(test.withSecondOfMinute(6), 1970, 6, 9, 10, 20, 6, 40);
        check(test.withMillisOfSecond(6), 1970, 6, 9, 10, 20, 30, 6);
        check(test.withMillisOfDay(61234), 1970, 6, 9, 0, 1, 1, 234);
        
        try {
            test.withMonthOfYear(0);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.withMonthOfYear(13);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testEqualsHashCode() {
        DateTime test1 = new DateTime(TEST_TIME1);
        DateTime test2 = new DateTime(TEST_TIME1);
        assertEquals(true, test1.equals(test2));
        assertEquals(true, test2.equals(test1));
        assertEquals(true, test1.equals(test1));
        assertEquals(true, test2.equals(test2));
        assertEquals(true, test1.hashCode() == test2.hashCode());
        assertEquals(true, test1.hashCode() == test1.hashCode());
        assertEquals(true, test2.hashCode() == test2.hashCode());
        
        DateTime test3 = new DateTime(TEST_TIME2);
        assertEquals(false, test1.equals(test3));
        assertEquals(false, test2.equals(test3));
        assertEquals(false, test3.equals(test1));
        assertEquals(false, test3.equals(test2));
        assertEquals(false, test1.hashCode() == test3.hashCode());
        assertEquals(false, test2.hashCode() == test3.hashCode());
        
        assertEquals(false, test1.equals("Hello"));
        assertEquals(true, test1.equals(new MockInstant()));
        assertEquals(false, test1.equals(new DateTime(TEST_TIME1, GREGORIAN_DEFAULT)));
        assertEquals(true, new DateTime(TEST_TIME1, new MockEqualsChronology()).equals(new DateTime(TEST_TIME1, new MockEqualsChronology())));
        assertEquals(false, new DateTime(TEST_TIME1, new MockEqualsChronology()).equals(new DateTime(TEST_TIME1, ISO_DEFAULT)));
    }
    
    class MockInstant extends AbstractInstant {
        @Override
        public String toString() {
            return null;
        }
        public long getMillis() {
            return TEST_TIME1;
        }
        public Chronology getChronology() {
            return ISO_DEFAULT;
        }
    }

    class MockEqualsChronology extends BaseChronology {
        private static final long serialVersionUID = 1L;
        @Override
        public boolean equals(Object obj) {
            return obj instanceof MockEqualsChronology;
        }
        @Override
        public DateTimeZone getZone() {
            return null;
        }
        @Override
        public Chronology withUTC() {
            return this;
        }
        @Override
        public Chronology withZone(DateTimeZone zone) {
            return this;
        }
        @Override
        public String toString() {
            return "";
        }
    }

    public void testCompareTo() {
        DateTime test1 = new DateTime(TEST_TIME1);
        DateTime test1a = new DateTime(TEST_TIME1);
        assertEquals(0, test1.compareTo(test1a));
        assertEquals(0, test1a.compareTo(test1));
        assertEquals(0, test1.compareTo(test1));
        assertEquals(0, test1a.compareTo(test1a));
        
        DateTime test2 = new DateTime(TEST_TIME2);
        assertEquals(-1, test1.compareTo(test2));
        assertEquals(+1, test2.compareTo(test1));
        
        DateTime test3 = new DateTime(TEST_TIME2, GREGORIAN_PARIS);
        assertEquals(-1, test1.compareTo(test3));
        assertEquals(+1, test3.compareTo(test1));
        assertEquals(0, test3.compareTo(test2));
        
        assertEquals(+1, test2.compareTo(new MockInstant()));
        assertEquals(0, test1.compareTo(new MockInstant()));
        
        try {
            test1.compareTo(null);
            fail();
        } catch (NullPointerException ex) {}
//        try {
//            test1.compareTo(new Date());
//            fail();
//        } catch (ClassCastException ex) {}
    }
    
    //-----------------------------------------------------------------------
    public void testIsEqual_long() {
        assertEquals(false, new DateTime(TEST_TIME1).isEqual(TEST_TIME2));
        assertEquals(true, new DateTime(TEST_TIME1).isEqual(TEST_TIME1));
        assertEquals(false, new DateTime(TEST_TIME2).isEqual(TEST_TIME1));
    }
    
    public void testIsEqualNow() {
        assertEquals(false, new DateTime(TEST_TIME_NOW - 1).isEqualNow());
        assertEquals(true, new DateTime(TEST_TIME_NOW).isEqualNow());
        assertEquals(false, new DateTime(TEST_TIME_NOW + 1).isEqualNow());
    }
    
    public void testIsEqual_RI() {
        DateTime test1 = new DateTime(TEST_TIME1);
        DateTime test1a = new DateTime(TEST_TIME1);
        assertEquals(true, test1.isEqual(test1a));
        assertEquals(true, test1a.isEqual(test1));
        assertEquals(true, test1.isEqual(test1));
        assertEquals(true, test1a.isEqual(test1a));
        
        DateTime test2 = new DateTime(TEST_TIME2);
        assertEquals(false, test1.isEqual(test2));
        assertEquals(false, test2.isEqual(test1));
        
        DateTime test3 = new DateTime(TEST_TIME2, GREGORIAN_PARIS);
        assertEquals(false, test1.isEqual(test3));
        assertEquals(false, test3.isEqual(test1));
        assertEquals(true, test3.isEqual(test2));
        
        assertEquals(false, test2.isEqual(new MockInstant()));
        assertEquals(true, test1.isEqual(new MockInstant()));
        
        assertEquals(false, new DateTime(TEST_TIME_NOW + 1).isEqual(null));
        assertEquals(true, new DateTime(TEST_TIME_NOW).isEqual(null));
        assertEquals(false, new DateTime(TEST_TIME_NOW - 1).isEqual(null));
    }
    
    //-----------------------------------------------------------------------
    public void testIsBefore_long() {
        assertEquals(true, new DateTime(TEST_TIME1).isBefore(TEST_TIME2));
        assertEquals(false, new DateTime(TEST_TIME1).isBefore(TEST_TIME1));
        assertEquals(false, new DateTime(TEST_TIME2).isBefore(TEST_TIME1));
    }
    
    public void testIsBeforeNow() {
        assertEquals(true, new DateTime(TEST_TIME_NOW - 1).isBeforeNow());
        assertEquals(false, new DateTime(TEST_TIME_NOW).isBeforeNow());
        assertEquals(false, new DateTime(TEST_TIME_NOW + 1).isBeforeNow());
    }
    
    public void testIsBefore_RI() {
        DateTime test1 = new DateTime(TEST_TIME1);
        DateTime test1a = new DateTime(TEST_TIME1);
        assertEquals(false, test1.isBefore(test1a));
        assertEquals(false, test1a.isBefore(test1));
        assertEquals(false, test1.isBefore(test1));
        assertEquals(false, test1a.isBefore(test1a));
        
        DateTime test2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.isBefore(test2));
        assertEquals(false, test2.isBefore(test1));
        
        DateTime test3 = new DateTime(TEST_TIME2, GREGORIAN_PARIS);
        assertEquals(true, test1.isBefore(test3));
        assertEquals(false, test3.isBefore(test1));
        assertEquals(false, test3.isBefore(test2));
        
        assertEquals(false, test2.isBefore(new MockInstant()));
        assertEquals(false, test1.isBefore(new MockInstant()));
        
        assertEquals(false, new DateTime(TEST_TIME_NOW + 1).isBefore(null));
        assertEquals(false, new DateTime(TEST_TIME_NOW).isBefore(null));
        assertEquals(true, new DateTime(TEST_TIME_NOW - 1).isBefore(null));
    }
    
    //-----------------------------------------------------------------------
    public void testIsAfter_long() {
        assertEquals(false, new DateTime(TEST_TIME1).isAfter(TEST_TIME2));
        assertEquals(false, new DateTime(TEST_TIME1).isAfter(TEST_TIME1));
        assertEquals(true, new DateTime(TEST_TIME2).isAfter(TEST_TIME1));
    }
    
    public void testIsAfterNow() {
        assertEquals(false, new DateTime(TEST_TIME_NOW - 1).isAfterNow());
        assertEquals(false, new DateTime(TEST_TIME_NOW).isAfterNow());
        assertEquals(true, new DateTime(TEST_TIME_NOW + 1).isAfterNow());
    }
    
    public void testIsAfter_RI() {
        DateTime test1 = new DateTime(TEST_TIME1);
        DateTime test1a = new DateTime(TEST_TIME1);
        assertEquals(false, test1.isAfter(test1a));
        assertEquals(false, test1a.isAfter(test1));
        assertEquals(false, test1.isAfter(test1));
        assertEquals(false, test1a.isAfter(test1a));
        
        DateTime test2 = new DateTime(TEST_TIME2);
        assertEquals(false, test1.isAfter(test2));
        assertEquals(true, test2.isAfter(test1));
        
        DateTime test3 = new DateTime(TEST_TIME2, GREGORIAN_PARIS);
        assertEquals(false, test1.isAfter(test3));
        assertEquals(true, test3.isAfter(test1));
        assertEquals(false, test3.isAfter(test2));
        
        assertEquals(true, test2.isAfter(new MockInstant()));
        assertEquals(false, test1.isAfter(new MockInstant()));
        
        assertEquals(true, new DateTime(TEST_TIME_NOW + 1).isAfter(null));
        assertEquals(false, new DateTime(TEST_TIME_NOW).isAfter(null));
        assertEquals(false, new DateTime(TEST_TIME_NOW - 1).isAfter(null));
    }
    
    //-----------------------------------------------------------------------
    public void testSerialization() throws Exception {
        DateTime test = new DateTime(TEST_TIME_NOW);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(test);
        oos.close();
        byte[] bytes = baos.toByteArray();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        DateTime result = (DateTime) ois.readObject();
        ois.close();
        
        assertEquals(test, result);
    }

    //-----------------------------------------------------------------------
    public void testToString() {
        DateTime test = new DateTime(TEST_TIME_NOW);
        assertEquals("2002-06-09T01:00:00.000+01:00", test.toString());
        
        test = new DateTime(TEST_TIME_NOW, PARIS);
        assertEquals("2002-06-09T02:00:00.000+02:00", test.toString());
    }

    public void testToString_String() {
        DateTime test = new DateTime(TEST_TIME_NOW);
        assertEquals("2002 01", test.toString("yyyy HH"));
        assertEquals("2002-06-09T01:00:00.000+01:00", test.toString((String) null));
    }

    public void testToString_String_Locale() {
        DateTime test = new DateTime(TEST_TIME_NOW);
        assertEquals("Sun 9/6", test.toString("EEE d/M", Locale.ENGLISH));
        assertEquals("dim. 9/6", test.toString("EEE d/M", Locale.FRENCH));
        assertEquals("2002-06-09T01:00:00.000+01:00", test.toString(null, Locale.ENGLISH));
        assertEquals("Sun 9/6", test.toString("EEE d/M", null));
        assertEquals("2002-06-09T01:00:00.000+01:00", test.toString(null, null));
    }

    @SuppressWarnings("deprecation")
    public void testToString_DTFormatter() {
        DateMidnight test = new DateMidnight(TEST_TIME_NOW);
        assertEquals("2002 00", test.toString(DateTimeFormat.forPattern("yyyy HH")));
        assertEquals("2002-06-09T00:00:00.000+01:00", test.toString((DateTimeFormatter) null));
    }

    //-----------------------------------------------------------------------
    public void testToInstant() {
        DateTime test = new DateTime(TEST_TIME1);
        Instant result = test.toInstant();
        assertEquals(TEST_TIME1, result.getMillis());
    }

    public void testToDateTime() {
        DateTime test = new DateTime(TEST_TIME1);
        DateTime result = test.toDateTime();
        assertSame(test, result);
    }

    public void testToDateTimeISO() {
        DateTime test = new DateTime(TEST_TIME1);
        DateTime result = test.toDateTimeISO();
        assertSame(test, result);
        
        test = new DateTime(TEST_TIME1, ISO_PARIS);
        result = test.toDateTimeISO();
        assertSame(DateTime.class, result.getClass());
        assertSame(ISOChronology.class, result.getChronology().getClass());
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISO_PARIS, result.getChronology());
        assertNotSame(test, result);
        
        test = new DateTime(TEST_TIME1, BUDDHIST_DEFAULT);
        result = test.toDateTimeISO();
        assertSame(DateTime.class, result.getClass());
        assertSame(ISOChronology.class, result.getChronology().getClass());
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISO_DEFAULT, result.getChronology());
        assertNotSame(test, result);
        
        test = new DateTime(TEST_TIME1, new MockNullZoneChronology());
        result = test.toDateTimeISO();
        assertSame(DateTime.class, result.getClass());
        assertSame(ISOChronology.class, result.getChronology().getClass());
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISO_DEFAULT, result.getChronology());
        assertNotSame(test, result);
    }

    public void testToDateTime_DateTimeZone() {
        DateTime test = new DateTime(TEST_TIME1);
        DateTime result = test.toDateTime(LONDON);
        assertSame(test, result);

        test = new DateTime(TEST_TIME1);
        result = test.toDateTime(PARIS);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(PARIS, result.getZone());

        test = new DateTime(TEST_TIME1, PARIS);
        result = test.toDateTime((DateTimeZone) null);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(LONDON, result.getZone());

        test = new DateTime(TEST_TIME1);
        result = test.toDateTime((DateTimeZone) null);
        assertSame(test, result);
    }

    public void testToDateTime_Chronology() {
        DateTime test = new DateTime(TEST_TIME1);
        DateTime result = test.toDateTime(ISO_DEFAULT);
        assertSame(test, result);

        test = new DateTime(TEST_TIME1);
        result = test.toDateTime(GREGORIAN_PARIS);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(GREGORIAN_PARIS, result.getChronology());

        test = new DateTime(TEST_TIME1, GREGORIAN_PARIS);
        result = test.toDateTime((Chronology) null);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISO_DEFAULT, result.getChronology());

        test = new DateTime(TEST_TIME1);
        result = test.toDateTime((Chronology) null);
        assertSame(test, result);
    }

    public void testToMutableDateTime() {
        DateTime test = new DateTime(TEST_TIME1, PARIS);
        MutableDateTime result = test.toMutableDateTime();
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISO_PARIS, result.getChronology());
    }

    public void testToMutableDateTimeISO() {
        DateTime test = new DateTime(TEST_TIME1, PARIS);
        MutableDateTime result = test.toMutableDateTimeISO();
        assertSame(MutableDateTime.class, result.getClass());
        assertSame(ISOChronology.class, result.getChronology().getClass());
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISO_PARIS, result.getChronology());
    }

    public void testToMutableDateTime_DateTimeZone() {
        DateTime test = new DateTime(TEST_TIME1);
        MutableDateTime result = test.toMutableDateTime(LONDON);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISO_DEFAULT, result.getChronology());

        test = new DateTime(TEST_TIME1);
        result = test.toMutableDateTime(PARIS);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISO_PARIS, result.getChronology());

        test = new DateTime(TEST_TIME1, PARIS);
        result = test.toMutableDateTime((DateTimeZone) null);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISO_DEFAULT, result.getChronology());

        test = new DateTime(TEST_TIME1);
        result = test.toMutableDateTime((DateTimeZone) null);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISO_DEFAULT, result.getChronology());
    }

    public void testToMutableDateTime_Chronology() {
        DateTime test = new DateTime(TEST_TIME1);
        MutableDateTime result = test.toMutableDateTime(ISO_DEFAULT);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISO_DEFAULT, result.getChronology());

        test = new DateTime(TEST_TIME1);
        result = test.toMutableDateTime(GREGORIAN_PARIS);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(GREGORIAN_PARIS, result.getChronology());

        test = new DateTime(TEST_TIME1, GREGORIAN_PARIS);
        result = test.toMutableDateTime((Chronology) null);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISO_DEFAULT, result.getChronology());

        test = new DateTime(TEST_TIME1);
        result = test.toMutableDateTime((Chronology) null);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISO_DEFAULT, result.getChronology());
    }

    public void testToDate() {
        DateTime test = new DateTime(TEST_TIME1);
        Date result = test.toDate();
        assertEquals(test.getMillis(), result.getTime());
    }

    public void testToCalendar_Locale() {
        DateTime test = new DateTime(TEST_TIME1);
        Calendar result = test.toCalendar(null);
        assertEquals(test.getMillis(), result.getTime().getTime());
        assertEquals(TimeZone.getTimeZone("Europe/London"), result.getTimeZone());

        test = new DateTime(TEST_TIME1, PARIS);
        result = test.toCalendar(null);
        assertEquals(test.getMillis(), result.getTime().getTime());
        assertEquals(TimeZone.getTimeZone("Europe/Paris"), result.getTimeZone());

        test = new DateTime(TEST_TIME1, PARIS);
        result = test.toCalendar(Locale.UK);
        assertEquals(test.getMillis(), result.getTime().getTime());
        assertEquals(TimeZone.getTimeZone("Europe/Paris"), result.getTimeZone());
    }

    public void testToGregorianCalendar() {
        DateTime test = new DateTime(TEST_TIME1);
        GregorianCalendar result = test.toGregorianCalendar();
        assertEquals(test.getMillis(), result.getTime().getTime());
        assertEquals(TimeZone.getTimeZone("Europe/London"), result.getTimeZone());

        test = new DateTime(TEST_TIME1, PARIS);
        result = test.toGregorianCalendar();
        assertEquals(test.getMillis(), result.getTime().getTime());
        assertEquals(TimeZone.getTimeZone("Europe/Paris"), result.getTimeZone());
    }

    //-----------------------------------------------------------------------
    @SuppressWarnings("deprecation")
    public void testToDateMidnight() {
        DateTime base = new DateTime(TEST_TIME1, COPTIC_DEFAULT);
        DateMidnight test = base.toDateMidnight();
        assertEquals(new DateMidnight(base, COPTIC_DEFAULT), test);
    }

    @SuppressWarnings("deprecation")
    public void testToYearMonthDay() {
        DateTime base = new DateTime(TEST_TIME1, COPTIC_DEFAULT);
        YearMonthDay test = base.toYearMonthDay();
        assertEquals(new YearMonthDay(TEST_TIME1, COPTIC_DEFAULT), test);
    }

    @SuppressWarnings("deprecation")
    public void testToTimeOfDay() {
        DateTime base = new DateTime(TEST_TIME1, COPTIC_DEFAULT);
        TimeOfDay test = base.toTimeOfDay();
        assertEquals(new TimeOfDay(TEST_TIME1, COPTIC_DEFAULT), test);
    }

    public void testToLocalDateTime() {
        DateTime base = new DateTime(TEST_TIME1, COPTIC_DEFAULT);
        LocalDateTime test = base.toLocalDateTime();
        assertEquals(new LocalDateTime(TEST_TIME1, COPTIC_DEFAULT), test);
    }

    public void testToLocalDate() {
        DateTime base = new DateTime(TEST_TIME1, COPTIC_DEFAULT);
        LocalDate test = base.toLocalDate();
        assertEquals(new LocalDate(TEST_TIME1, COPTIC_DEFAULT), test);
    }

    public void testToLocalTime() {
        DateTime base = new DateTime(TEST_TIME1, COPTIC_DEFAULT);
        LocalTime test = base.toLocalTime();
        assertEquals(new LocalTime(TEST_TIME1, COPTIC_DEFAULT), test);
    }

    //-----------------------------------------------------------------------
    public void testWithMillis_long() {
        DateTime test = new DateTime(TEST_TIME1);
        DateTime result = test.withMillis(TEST_TIME2);
        assertEquals(TEST_TIME2, result.getMillis());
        assertEquals(test.getChronology(), result.getChronology());
        
        test = new DateTime(TEST_TIME1, GREGORIAN_PARIS);
        result = test.withMillis(TEST_TIME2);
        assertEquals(TEST_TIME2, result.getMillis());
        assertEquals(test.getChronology(), result.getChronology());
        
        test = new DateTime(TEST_TIME1);
        result = test.withMillis(TEST_TIME1);
        assertSame(test, result);
    }

    public void testWithChronology_Chronology() {
        DateTime test = new DateTime(TEST_TIME1);
        DateTime result = test.withChronology(GREGORIAN_PARIS);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(GREGORIAN_PARIS, result.getChronology());
        
        test = new DateTime(TEST_TIME1, GREGORIAN_PARIS);
        result = test.withChronology(null);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISO_DEFAULT, result.getChronology());
        
        test = new DateTime(TEST_TIME1);
        result = test.withChronology(null);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISO_DEFAULT, result.getChronology());
        
        test = new DateTime(TEST_TIME1);
        result = test.withChronology(ISO_DEFAULT);
        assertSame(test, result);
    }

    public void testWithZone_DateTimeZone() {
        DateTime test = new DateTime(TEST_TIME1);
        DateTime result = test.withZone(PARIS);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISO_PARIS, result.getChronology());
        
        test = new DateTime(TEST_TIME1, GREGORIAN_PARIS);
        result = test.withZone(null);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(GREGORIAN_DEFAULT, result.getChronology());
        
        test = new DateTime(TEST_TIME1);
        result = test.withZone(null);
        assertSame(test, result);
    }

    public void testWithZoneRetainFields_DateTimeZone() {
        DateTime test = new DateTime(TEST_TIME1);
        DateTime result = test.withZoneRetainFields(PARIS);
        assertEquals(test.getMillis() - DateTimeConstants.MILLIS_PER_HOUR, result.getMillis());
        assertEquals(ISO_PARIS, result.getChronology());
        
        test = new DateTime(TEST_TIME1);
        result = test.withZoneRetainFields(LONDON);
        assertSame(test, result);
        
        test = new DateTime(TEST_TIME1);
        result = test.withZoneRetainFields(null);
        assertSame(test, result);
        
        test = new DateTime(TEST_TIME1, GREGORIAN_PARIS);
        result = test.withZoneRetainFields(null);
        assertEquals(test.getMillis() + DateTimeConstants.MILLIS_PER_HOUR, result.getMillis());
        assertEquals(GREGORIAN_DEFAULT, result.getChronology());
        
        test = new DateTime(TEST_TIME1, new MockNullZoneChronology());
        result = test.withZoneRetainFields(LONDON);
        assertSame(test, result);
    }
    
    //-----------------------------------------------------------------------
    public void testWithDate_int_int_int() {
        DateTime test = new DateTime(2002, 4, 5, 1, 2, 3, 4, ISO_UTC);
        DateTime result = test.withDate(2003, 5, 6);
        DateTime expected = new DateTime(2003, 5, 6, 1, 2, 3, 4, ISO_UTC);
        assertEquals(expected, result);
        
        test = new DateTime(TEST_TIME1);
        try {
            test.withDate(2003, 13, 1);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    public void testWithDate_int_int_int_toDST1() {
        // 2010-03-28T02:55 is DST time, need to change to 03:55
        DateTime test = new DateTime(2015, 1, 10, 2, 55, 0, 0, ISO_PARIS);
        DateTime result = test.withDate(2010, 3, 28);
        DateTime expected = new DateTime(2010, 3, 28, 3, 55, 0, 0, ISO_PARIS);
        assertEquals(expected, result);
    }
    
    public void testWithDate_int_int_int_toDST2() {
        // 2010-03-28T02:55 is DST time, need to change to 03:55
        DateTime test = new DateTime(2015, 1, 28, 2, 55, 0, 0, ISO_PARIS);
        DateTime result = test.withDate(2010, 3, 28);
        DateTime expected = new DateTime(2010, 3, 28, 3, 55, 0, 0, ISO_PARIS);
        assertEquals(expected, result);
    }
    
    public void testWithDate_int_int_int_affectedByDST() {
        // 2010-03-28T02:55 is DST time, need to avoid time being changed to 03:55
        DateTime test = new DateTime(2015, 1, 28, 2, 55, 0, 0, ISO_PARIS);
        DateTime result = test.withDate(2010, 3, 10);
        DateTime expected = new DateTime(2010, 3, 10, 2, 55, 0, 0, ISO_PARIS);
        assertEquals(expected, result);
    }
    
    public void testWithDate_LocalDate() {
        DateTime test = new DateTime(2002, 4, 5, 1, 2, 3, 4, ISO_UTC);
        DateTime result = test.withDate(new LocalDate(2003, 5, 6));
        DateTime expected = new DateTime(2003, 5, 6, 1, 2, 3, 4, ISO_UTC);
        assertEquals(expected, result);
        
        test = new DateTime(TEST_TIME1);
        try {
            test.withDate(new LocalDate(2003, 13, 1));
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    //-----------------------------------------------------------------------
    public void testWithTime_int_int_int_int() {
        DateTime test = new DateTime(TEST_TIME1 - 12345L, BUDDHIST_UTC);
        DateTime result = test.withTime(12, 24, 0, 0);
        assertEquals(TEST_TIME1, result.getMillis());
        assertEquals(BUDDHIST_UTC, result.getChronology());
        
        test = new DateTime(TEST_TIME1);
        try {
            test.withTime(25, 1, 1, 1);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    public void testWithTime_int_int_int_int_toDST() {
        // 2010-03-28T02:55 is DST time, need to change to 03:55
        DateTime test = new DateTime(2010, 3, 28, 0, 0, 0, 0, ISO_PARIS);
        DateTime result = test.withTime(2, 55, 0, 0);
        DateTime expected = new DateTime(2010, 3, 28, 3, 55, 0, 0, ISO_PARIS);
        assertEquals(expected, result);
    }
    
    public void testWithTime_LocalTime() {
        DateTime test = new DateTime(TEST_TIME1 - 12345L, BUDDHIST_UTC);
        DateTime result = test.withTime(new LocalTime(12, 24, 0, 0));
        assertEquals(TEST_TIME1, result.getMillis());
        assertEquals(BUDDHIST_UTC, result.getChronology());
        
        test = new DateTime(TEST_TIME1);
        try {
            test.withTime(new LocalTime(25, 1, 1, 1));
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    public void testWithTimeAtStartOfDay() {
        DateTime test = new DateTime(2018, 10, 28, 0, 0, DateTimeZone.forID("Atlantic/Azores"));
        DateTime result = test.withTimeAtStartOfDay();
        assertEquals(test, result);
    }
    
    @SuppressWarnings("deprecation")
    public void testWithFields_RPartial() {
        DateTime test = new DateTime(2004, 5, 6, 7, 8, 9, 0);
        DateTime result = test.withFields(new YearMonthDay(2003, 4, 5));
        DateTime expected = new DateTime(2003, 4, 5, 7, 8, 9, 0);
        assertEquals(expected, result);
        
        test = new DateTime(TEST_TIME1);
        result = test.withFields(null);
        assertSame(test, result);
    }
    
    //-----------------------------------------------------------------------
    public void testWithField1() {
        DateTime test = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime result = test.withField(DateTimeFieldType.year(), 2006);
        
        assertEquals(new DateTime(2004, 6, 9, 0, 0, 0, 0), test);
        assertEquals(new DateTime(2006, 6, 9, 0, 0, 0, 0), result);
    }

    public void testWithField2() {
        DateTime test = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        try {
            test.withField(null, 6);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testWithFieldAdded1() {
        DateTime test = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime result = test.withFieldAdded(DurationFieldType.years(), 6);
        
        assertEquals(new DateTime(2004, 6, 9, 0, 0, 0, 0), test);
        assertEquals(new DateTime(2010, 6, 9, 0, 0, 0, 0), result);
    }

    public void testWithFieldAdded2() {
        DateTime test = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        try {
            test.withFieldAdded(null, 0);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testWithFieldAdded3() {
        DateTime test = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        try {
            test.withFieldAdded(null, 6);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testWithFieldAdded4() {
        DateTime test = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime result = test.withFieldAdded(DurationFieldType.years(), 0);
        assertSame(test, result);
    }

    //-----------------------------------------------------------------------
    public void testWithDurationAdded_long_int() {
        DateTime test = new DateTime(TEST_TIME1, BUDDHIST_DEFAULT);
        DateTime result = test.withDurationAdded(123456789L, 1);
        DateTime expected = new DateTime(TEST_TIME1 + 123456789L, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.withDurationAdded(123456789L, 0);
        assertSame(test, result);
        
        result = test.withDurationAdded(123456789L, 2);
        expected = new DateTime(TEST_TIME1 + (2L * 123456789L), BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.withDurationAdded(123456789L, -3);
        expected = new DateTime(TEST_TIME1 - (3L * 123456789L), BUDDHIST_DEFAULT);
        assertEquals(expected, result);
    }
    
    //-----------------------------------------------------------------------
    public void testWithDurationAdded_RD_int() {
        DateTime test = new DateTime(TEST_TIME1, BUDDHIST_DEFAULT);
        DateTime result = test.withDurationAdded(new Duration(123456789L), 1);
        DateTime expected = new DateTime(TEST_TIME1 + 123456789L, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.withDurationAdded(null, 1);
        assertSame(test, result);
        
        result = test.withDurationAdded(new Duration(123456789L), 0);
        assertSame(test, result);
        
        result = test.withDurationAdded(new Duration(123456789L), 2);
        expected = new DateTime(TEST_TIME1 + (2L * 123456789L), BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.withDurationAdded(new Duration(123456789L), -3);
        expected = new DateTime(TEST_TIME1 - (3L * 123456789L), BUDDHIST_DEFAULT);
        assertEquals(expected, result);
    }
    
    //-----------------------------------------------------------------------
    public void testWithDurationAdded_RP_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        DateTime result = test.withPeriodAdded(new Period(1, 2, 3, 4, 5, 6, 7, 8), 1);
        DateTime expected = new DateTime(2003, 7, 28, 6, 8, 10, 12, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.withPeriodAdded(null, 1);
        assertSame(test, result);
        
        result = test.withPeriodAdded(new Period(1, 2, 3, 4, 5, 6, 7, 8), 0);
        assertSame(test, result);
        
        result = test.withPeriodAdded(new Period(1, 2, 0, 4, 5, 6, 7, 8), 3);
        expected = new DateTime(2005, 11, 15, 16, 20, 24, 28, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.withPeriodAdded(new Period(1, 2, 0, 1, 1, 2, 3, 4), -1);
        expected = new DateTime(2001, 3, 2, 0, 0, 0, 0, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
    }

    //-----------------------------------------------------------------------    
    public void testPlus_long() {
        DateTime test = new DateTime(TEST_TIME1, BUDDHIST_DEFAULT);
        DateTime result = test.plus(123456789L);
        DateTime expected = new DateTime(TEST_TIME1 + 123456789L, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
    }
    
    public void testPlus_RD() {
        DateTime test = new DateTime(TEST_TIME1, BUDDHIST_DEFAULT);
        DateTime result = test.plus(new Duration(123456789L));
        DateTime expected = new DateTime(TEST_TIME1 + 123456789L, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.plus((ReadableDuration) null);
        assertSame(test, result);
    }
    
    public void testPlus_RP() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        DateTime result = test.plus(new Period(1, 2, 3, 4, 5, 6, 7, 8));
        DateTime expected = new DateTime(2003, 7, 28, 6, 8, 10, 12, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.plus((ReadablePeriod) null);
        assertSame(test, result);
    }

    public void testPlusYears_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        DateTime result = test.plusYears(1);
        DateTime expected = new DateTime(2003, 5, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.plusYears(0);
        assertSame(test, result);
    }

    public void testPlusMonths_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        DateTime result = test.plusMonths(1);
        DateTime expected = new DateTime(2002, 6, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.plusMonths(0);
        assertSame(test, result);
    }

    public void testPlusMonths_intMax() {
        DateTime test = new DateTime(2016, 2, 20, 1, 2, 3, 4, ISO_UTC);
        assertEquals(new DateTime(178958986, 7, 20, 1, 2, 3, 4, ISO_UTC), test.plusMonths(Integer.MAX_VALUE - 2));
        assertEquals(new DateTime(178958986, 8, 20, 1, 2, 3, 4, ISO_UTC), test.plusMonths(Integer.MAX_VALUE - 1));
        assertEquals(new DateTime(178958986, 9, 20, 1, 2, 3, 4, ISO_UTC), test.plusMonths(Integer.MAX_VALUE));

        assertEquals(new DateTime(178958986, 7, 20, 1, 2, 3, 4, ISO_UTC), test.monthOfYear().addToCopy(Integer.MAX_VALUE - 2));
        assertEquals(new DateTime(178958986, 8, 20, 1, 2, 3, 4, ISO_UTC), test.monthOfYear().addToCopy(Integer.MAX_VALUE - 1));
        assertEquals(new DateTime(178958986, 9, 20, 1, 2, 3, 4, ISO_UTC), test.monthOfYear().addToCopy(Integer.MAX_VALUE));
    }

    public void testPlusMonths_intMin() {
        DateTime test = new DateTime(2016, 2, 20, 1, 2, 3, 4, ISO_UTC);
        assertEquals(new DateTime(-178954955, 8, 20, 1, 2, 3, 4, ISO_UTC), test.plusMonths(Integer.MIN_VALUE + 2));
        assertEquals(new DateTime(-178954955, 7, 20, 1, 2, 3, 4, ISO_UTC), test.plusMonths(Integer.MIN_VALUE + 1));
        assertEquals(new DateTime(-178954955, 6, 20, 1, 2, 3, 4, ISO_UTC), test.plusMonths(Integer.MIN_VALUE));

        assertEquals(new DateTime(-178954955, 8, 20, 1, 2, 3, 4, ISO_UTC), test.monthOfYear().addToCopy(Integer.MIN_VALUE + 2));
        assertEquals(new DateTime(-178954955, 7, 20, 1, 2, 3, 4, ISO_UTC), test.monthOfYear().addToCopy(Integer.MIN_VALUE + 1));
        assertEquals(new DateTime(-178954955, 6, 20, 1, 2, 3, 4, ISO_UTC), test.monthOfYear().addToCopy(Integer.MIN_VALUE));
    }

    public void testPlusWeeks_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        DateTime result = test.plusWeeks(1);
        DateTime expected = new DateTime(2002, 5, 10, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.plusWeeks(0);
        assertSame(test, result);
    }

    public void testPlusDays_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        DateTime result = test.plusDays(1);
        DateTime expected = new DateTime(2002, 5, 4, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.plusDays(0);
        assertSame(test, result);
    }

    public void testPlusHours_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        DateTime result = test.plusHours(1);
        DateTime expected = new DateTime(2002, 5, 3, 2, 2, 3, 4, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.plusHours(0);
        assertSame(test, result);
    }

    public void testPlusMinutes_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        DateTime result = test.plusMinutes(1);
        DateTime expected = new DateTime(2002, 5, 3, 1, 3, 3, 4, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.plusMinutes(0);
        assertSame(test, result);
    }

    public void testPlusSeconds_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        DateTime result = test.plusSeconds(1);
        DateTime expected = new DateTime(2002, 5, 3, 1, 2, 4, 4, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.plusSeconds(0);
        assertSame(test, result);
    }

    public void testPlusMillis_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        DateTime result = test.plusMillis(1);
        DateTime expected = new DateTime(2002, 5, 3, 1, 2, 3, 5, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.plusMillis(0);
        assertSame(test, result);
    }

    //-----------------------------------------------------------------------    
    public void testMinus_long() {
        DateTime test = new DateTime(TEST_TIME1, BUDDHIST_DEFAULT);
        DateTime result = test.minus(123456789L);
        DateTime expected = new DateTime(TEST_TIME1 - 123456789L, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
    }
    
    public void testMinus_RD() {
        DateTime test = new DateTime(TEST_TIME1, BUDDHIST_DEFAULT);
        DateTime result = test.minus(new Duration(123456789L));
        DateTime expected = new DateTime(TEST_TIME1 - 123456789L, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.minus((ReadableDuration) null);
        assertSame(test, result);
    }
    
    public void testMinus_RP() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        DateTime result = test.minus(new Period(1, 1, 1, 1, 1, 1, 1, 1));
        DateTime expected = new DateTime(2001, 3, 26, 0, 1, 2, 3, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.minus((ReadablePeriod) null);
        assertSame(test, result);
    }

    public void testMinusYears_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        DateTime result = test.minusYears(1);
        DateTime expected = new DateTime(2001, 5, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.minusYears(0);
        assertSame(test, result);
    }

    public void testMinusMonths_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        DateTime result = test.minusMonths(1);
        DateTime expected = new DateTime(2002, 4, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.minusMonths(0);
        assertSame(test, result);
    }

    public void testMinusMonths_intMax() {
        DateTime test = new DateTime(2016, 2, 20, 1, 2, 3, 4, ISO_UTC);
        assertEquals(new DateTime(-178954955, 9, 20, 1, 2, 3, 4, ISO_UTC), test.minusMonths(Integer.MAX_VALUE - 2));
        assertEquals(new DateTime(-178954955, 8, 20, 1, 2, 3, 4, ISO_UTC), test.minusMonths(Integer.MAX_VALUE - 1));
        assertEquals(new DateTime(-178954955, 7, 20, 1, 2, 3, 4, ISO_UTC), test.minusMonths(Integer.MAX_VALUE));
    }

    public void testMinusMonths_intMin() {
        DateTime test = new DateTime(2016, 2, 20, 1, 2, 3, 4, ISO_UTC);
        assertEquals(new DateTime(178958986, 8, 20, 1, 2, 3, 4, ISO_UTC), test.minusMonths(Integer.MIN_VALUE + 2));
        assertEquals(new DateTime(178958986, 9, 20, 1, 2, 3, 4, ISO_UTC), test.minusMonths(Integer.MIN_VALUE + 1));
        assertEquals(new DateTime(178958986, 10, 20, 1, 2, 3, 4, ISO_UTC), test.minusMonths(Integer.MIN_VALUE));
    }

    public void testMinusWeeks_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        DateTime result = test.minusWeeks(1);
        DateTime expected = new DateTime(2002, 4, 26, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.minusWeeks(0);
        assertSame(test, result);
    }

    public void testMinusDays_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        DateTime result = test.minusDays(1);
        DateTime expected = new DateTime(2002, 5, 2, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.minusDays(0);
        assertSame(test, result);
    }

    public void testMinusHours_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        DateTime result = test.minusHours(1);
        DateTime expected = new DateTime(2002, 5, 3, 0, 2, 3, 4, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.minusHours(0);
        assertSame(test, result);
    }

    public void testMinusMinutes_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        DateTime result = test.minusMinutes(1);
        DateTime expected = new DateTime(2002, 5, 3, 1, 1, 3, 4, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.minusMinutes(0);
        assertSame(test, result);
    }

    public void testMinusSeconds_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        DateTime result = test.minusSeconds(1);
        DateTime expected = new DateTime(2002, 5, 3, 1, 2, 2, 4, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.minusSeconds(0);
        assertSame(test, result);
    }

    public void testMinusMillis_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4, BUDDHIST_DEFAULT);
        DateTime result = test.minusMillis(1);
        DateTime expected = new DateTime(2002, 5, 3, 1, 2, 3, 3, BUDDHIST_DEFAULT);
        assertEquals(expected, result);
        
        result = test.minusMillis(0);
        assertSame(test, result);
    }

    //-----------------------------------------------------------------------
    public void testProperty() {
        DateTime test = new DateTime();
        assertEquals(test.year(), test.property(DateTimeFieldType.year()));
        assertEquals(test.dayOfWeek(), test.property(DateTimeFieldType.dayOfWeek()));
        assertEquals(test.secondOfMinute(), test.property(DateTimeFieldType.secondOfMinute()));
        assertEquals(test.millisOfSecond(), test.property(DateTimeFieldType.millisOfSecond()));
        DateTimeFieldType bad = new DateTimeFieldType("bad") {
            private static final long serialVersionUID = 1L;
            @Override
            public DurationFieldType getDurationType() {
                return DurationFieldType.weeks();
            }
            @Override
            public DurationFieldType getRangeDurationType() {
                return null;
            }
            @Override
            public DateTimeField getField(Chronology chronology) {
                return UnsupportedDateTimeField.getInstance(this, UnsupportedDurationField.getInstance(getDurationType()));
            }
        };
        try {
            test.property(bad);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.property(null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    private void check(DateTime test, int year, int month, int day, int hour, int min, int sec, int mil) {
        assertEquals(year, test.getYear());
        assertEquals(month, test.getMonthOfYear());
        assertEquals(day, test.getDayOfMonth());
        assertEquals(hour, test.getHourOfDay());
        assertEquals(min, test.getMinuteOfHour());
        assertEquals(sec, test.getSecondOfMinute());
        assertEquals(mil, test.getMillisOfSecond());
    }

}
