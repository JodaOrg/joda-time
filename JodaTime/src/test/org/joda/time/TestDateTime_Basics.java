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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.chrono.AbstractChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;

/**
 * This class is a Junit unit test for DateTime.
 *
 * @author Stephen Colebourne
 */
public class TestDateTime_Basics extends TestCase {
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
        return new TestSuite(TestDateTime_Basics.class);
    }

    public TestDateTime_Basics(String name) {
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
    public void testGet() {
        DateTime test = new DateTime();
        assertEquals(1, test.get(ISOChronology.getInstance().era()));
        assertEquals(20, test.get(ISOChronology.getInstance().centuryOfEra()));
        assertEquals(2, test.get(ISOChronology.getInstance().yearOfCentury()));
        assertEquals(2002, test.get(ISOChronology.getInstance().yearOfEra()));
        assertEquals(2002, test.get(ISOChronology.getInstance().year()));
        assertEquals(6, test.get(ISOChronology.getInstance().monthOfYear()));
        assertEquals(9, test.get(ISOChronology.getInstance().dayOfMonth()));
        assertEquals(2002, test.get(ISOChronology.getInstance().weekyear()));
        assertEquals(23, test.get(ISOChronology.getInstance().weekOfWeekyear()));
        assertEquals(7, test.get(ISOChronology.getInstance().dayOfWeek()));
        assertEquals(160, test.get(ISOChronology.getInstance().dayOfYear()));
        assertEquals(0, test.get(ISOChronology.getInstance().halfdayOfDay()));
        assertEquals(1, test.get(ISOChronology.getInstance().hourOfHalfday()));
        assertEquals(1, test.get(ISOChronology.getInstance().clockhourOfDay()));
        assertEquals(1, test.get(ISOChronology.getInstance().clockhourOfHalfday()));
        assertEquals(1, test.get(ISOChronology.getInstance().hourOfDay()));
        assertEquals(0, test.get(ISOChronology.getInstance().minuteOfHour()));
        assertEquals(60, test.get(ISOChronology.getInstance().minuteOfDay()));
        assertEquals(0, test.get(ISOChronology.getInstance().secondOfMinute()));
        assertEquals(60 * 60, test.get(ISOChronology.getInstance().secondOfDay()));
        assertEquals(0, test.get(ISOChronology.getInstance().millisOfSecond()));
        assertEquals(60 * 60 * 1000, test.get(ISOChronology.getInstance().millisOfDay()));
        try {
            test.get(null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testGetMethods() {
        DateTime test = new DateTime();
        
        assertEquals(ISOChronology.getInstance(), test.getChronology());
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
        assertEquals(false, test1.equals(new DateTime(TEST_TIME1, GregorianChronology.getInstance())));
        assertEquals(true, new DateTime(TEST_TIME1, new MockEqualsChronology()).equals(new DateTime(TEST_TIME1, new MockEqualsChronology())));
        assertEquals(false, new DateTime(TEST_TIME1, new MockEqualsChronology()).equals(new DateTime(TEST_TIME1, ISOChronology.getInstance())));
    }
    
    class MockInstant extends AbstractInstant {
        public String toString() {
            return null;
        }
        public long getMillis() {
            return TEST_TIME1;
        }
        public Chronology getChronology() {
            return ISOChronology.getInstance();
        }
    }

    class MockEqualsChronology extends AbstractChronology {
        public boolean equals(Object obj) {
            return obj instanceof MockEqualsChronology;
        }
        public DateTimeZone getZone() {
            return null;
        }
        public Chronology withUTC() {
            return this;
        }
        public Chronology withZone(DateTimeZone zone) {
            return this;
        }
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
        
        DateTime test3 = new DateTime(TEST_TIME2, GregorianChronology.getInstance(PARIS));
        assertEquals(-1, test1.compareTo(test3));
        assertEquals(+1, test3.compareTo(test1));
        assertEquals(0, test3.compareTo(test2));
        
        assertEquals(+1, test2.compareTo(new MockInstant()));
        assertEquals(0, test1.compareTo(new MockInstant()));
        
        try {
            test1.compareTo(null);
            fail();
        } catch (NullPointerException ex) {}
        try {
            test1.compareTo(new Date());
            fail();
        } catch (ClassCastException ex) {}
    }
    
    public void testIsEqual() {
        DateTime test1 = new DateTime(TEST_TIME1);
        DateTime test1a = new DateTime(TEST_TIME1);
        assertEquals(true, test1.isEqual(test1a));
        assertEquals(true, test1a.isEqual(test1));
        assertEquals(true, test1.isEqual(test1));
        assertEquals(true, test1a.isEqual(test1a));
        
        DateTime test2 = new DateTime(TEST_TIME2);
        assertEquals(false, test1.isEqual(test2));
        assertEquals(false, test2.isEqual(test1));
        
        DateTime test3 = new DateTime(TEST_TIME2, GregorianChronology.getInstance(PARIS));
        assertEquals(false, test1.isEqual(test3));
        assertEquals(false, test3.isEqual(test1));
        assertEquals(true, test3.isEqual(test2));
        
        assertEquals(false, test2.isEqual(new MockInstant()));
        assertEquals(true, test1.isEqual(new MockInstant()));
        assertEquals(false, test1.isEqual(null));
    }
    
    public void testIsBefore() {
        DateTime test1 = new DateTime(TEST_TIME1);
        DateTime test1a = new DateTime(TEST_TIME1);
        assertEquals(false, test1.isBefore(test1a));
        assertEquals(false, test1a.isBefore(test1));
        assertEquals(false, test1.isBefore(test1));
        assertEquals(false, test1a.isBefore(test1a));
        
        DateTime test2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.isBefore(test2));
        assertEquals(false, test2.isBefore(test1));
        
        DateTime test3 = new DateTime(TEST_TIME2, GregorianChronology.getInstance(PARIS));
        assertEquals(true, test1.isBefore(test3));
        assertEquals(false, test3.isBefore(test1));
        assertEquals(false, test3.isBefore(test2));
        
        assertEquals(false, test2.isBefore(new MockInstant()));
        assertEquals(false, test1.isBefore(new MockInstant()));
        assertEquals(false, test1.isBefore(null));
    }
    
    public void testIsAfter() {
        DateTime test1 = new DateTime(TEST_TIME1);
        DateTime test1a = new DateTime(TEST_TIME1);
        assertEquals(false, test1.isAfter(test1a));
        assertEquals(false, test1a.isAfter(test1));
        assertEquals(false, test1.isAfter(test1));
        assertEquals(false, test1a.isAfter(test1a));
        
        DateTime test2 = new DateTime(TEST_TIME2);
        assertEquals(false, test1.isAfter(test2));
        assertEquals(true, test2.isAfter(test1));
        
        DateTime test3 = new DateTime(TEST_TIME2, GregorianChronology.getInstance(PARIS));
        assertEquals(false, test1.isAfter(test3));
        assertEquals(true, test3.isAfter(test1));
        assertEquals(false, test3.isAfter(test2));
        
        assertEquals(true, test2.isAfter(new MockInstant()));
        assertEquals(false, test1.isAfter(new MockInstant()));
        assertEquals(false, test1.isAfter(null));
    }
    
    //-----------------------------------------------------------------------
    public void testSerialization() throws Exception {
        DateTime test = new DateTime(TEST_TIME_NOW);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(test);
        byte[] bytes = baos.toByteArray();
        oos.close();
        
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
        assertEquals("2002-06-09T01:00:00.000+01:00", test.toString(null));
    }

    public void testToString_String_String() {
        DateTime test = new DateTime(TEST_TIME_NOW);
        assertEquals("Sun 9/6", test.toString("EEE d/M", Locale.ENGLISH));
        assertEquals("dim. 9/6", test.toString("EEE d/M", Locale.FRENCH));
        assertEquals("2002-06-09T01:00:00.000+01:00", test.toString(null, Locale.ENGLISH));
        assertEquals("Sun 9/6", test.toString("EEE d/M", null));
        assertEquals("2002-06-09T01:00:00.000+01:00", test.toString(null, null));
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
        DateTime result = test.toDateTime(ISOChronology.getInstance());
        assertSame(test, result);

        test = new DateTime(TEST_TIME1);
        result = test.toDateTime(GregorianChronology.getInstance(PARIS));
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(GregorianChronology.getInstance(PARIS), result.getChronology());

        test = new DateTime(TEST_TIME1, GregorianChronology.getInstance(PARIS));
        result = test.toDateTime((Chronology) null);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISOChronology.getInstance(), result.getChronology());

        test = new DateTime(TEST_TIME1);
        result = test.toDateTime((Chronology) null);
        assertSame(test, result);
    }

    public void testToTrustedISODateTime() {
        DateTime test = new DateTime(TEST_TIME1);
        DateTime result = test.toTrustedISODateTime();
        assertSame(test, result);
        assertSame(DateTime.class, result.getClass());
        assertSame(ISOChronology.class, result.getChronology().getClass());

        test = new MockUntrustedDateTime(TEST_TIME1);
        result = test.toTrustedISODateTime();
        assertSame(DateTime.class, result.getClass());
        assertSame(ISOChronology.class, result.getChronology().getClass());
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISOChronology.getInstance(), result.getChronology());

        test = new DateTime(TEST_TIME1, new MockUntrustedZone("Europe/Paris"));
        result = test.toTrustedISODateTime();
        assertSame(DateTime.class, result.getClass());
        assertSame(ISOChronology.class, result.getChronology().getClass());
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISOChronology.getInstance(PARIS), result.getChronology());
    }

    static class MockUntrustedDateTime extends DateTime {
        MockUntrustedDateTime(long millis) {
            super(millis);
        }
    }

    static class MockUntrustedZone extends DateTimeZone {
        MockUntrustedZone(String id) {
            super(id);
        }
        public String getNameKey(long instant) {
            return null;
        }
        public int getOffset(long instant) {
            return 60 * 60 * 1000;
        }
        public int getStandardOffset(long instant) {
            return 60 * 60 * 1000;
        }
        public boolean isFixed() {
            return true;
        }
        public long nextTransition(long instant) {
            return 0;
        }
        public long previousTransition(long instant) {
            return 0;
        }
        public boolean equals(Object object) {
            return false;
        }
    }

    public void testToMutableDateTime() {
        DateTime test = new DateTime(TEST_TIME1);
        MutableDateTime result = test.toMutableDateTime();
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISOChronology.getInstance(), result.getChronology());
    }

    public void testToMutableDateTime_DateTimeZone() {
        DateTime test = new DateTime(TEST_TIME1);
        MutableDateTime result = test.toMutableDateTime(LONDON);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISOChronology.getInstance(), result.getChronology());

        test = new DateTime(TEST_TIME1);
        result = test.toMutableDateTime(PARIS);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISOChronology.getInstance(PARIS), result.getChronology());

        test = new DateTime(TEST_TIME1, PARIS);
        result = test.toMutableDateTime((DateTimeZone) null);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISOChronology.getInstance(), result.getChronology());

        test = new DateTime(TEST_TIME1);
        result = test.toMutableDateTime((DateTimeZone) null);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISOChronology.getInstance(), result.getChronology());
    }

    public void testToMutableDateTime_Chronology() {
        DateTime test = new DateTime(TEST_TIME1);
        MutableDateTime result = test.toMutableDateTime(ISOChronology.getInstance());
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISOChronology.getInstance(), result.getChronology());

        test = new DateTime(TEST_TIME1);
        result = test.toMutableDateTime(GregorianChronology.getInstance(PARIS));
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(GregorianChronology.getInstance(PARIS), result.getChronology());

        test = new DateTime(TEST_TIME1, GregorianChronology.getInstance(PARIS));
        result = test.toMutableDateTime((Chronology) null);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISOChronology.getInstance(), result.getChronology());

        test = new DateTime(TEST_TIME1);
        result = test.toMutableDateTime((Chronology) null);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISOChronology.getInstance(), result.getChronology());
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
    public void testWithMillis_long() {
        DateTime test = new DateTime(TEST_TIME1);
        DateTime result = test.withMillis(TEST_TIME2);
        assertEquals(TEST_TIME2, result.getMillis());
        assertEquals(test.getChronology(), result.getChronology());
        
        test = new DateTime(TEST_TIME1, GregorianChronology.getInstance(PARIS));
        result = test.withMillis(TEST_TIME2);
        assertEquals(TEST_TIME2, result.getMillis());
        assertEquals(test.getChronology(), result.getChronology());
        
        test = new DateTime(TEST_TIME1);
        result = test.withMillis(TEST_TIME1);
        assertSame(test, result);
    }

    public void testWithChronology_Chronology() {
        DateTime test = new DateTime(TEST_TIME1);
        DateTime result = test.withChronology(GregorianChronology.getInstance(PARIS));
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(GregorianChronology.getInstance(PARIS), result.getChronology());
        
        test = new DateTime(TEST_TIME1, GregorianChronology.getInstance(PARIS));
        result = test.withChronology(null);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISOChronology.getInstance(), result.getChronology());
        
        test = new DateTime(TEST_TIME1);
        result = test.withChronology(null);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISOChronology.getInstance(), result.getChronology());
        
        test = new DateTime(TEST_TIME1);
        result = test.withChronology(ISOChronology.getInstance());
        assertSame(test, result);
    }

    public void testWithZone_DateTimeZone() {
        DateTime test = new DateTime(TEST_TIME1);
        DateTime result = test.withZone(PARIS);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(ISOChronology.getInstance(PARIS), result.getChronology());
        
        test = new DateTime(TEST_TIME1, GregorianChronology.getInstance(PARIS));
        result = test.withZone(null);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(GregorianChronology.getInstance(), result.getChronology());
        
        test = new DateTime(TEST_TIME1);
        result = test.withZone(null);
        assertSame(test, result);
    }

    public void testWithZoneRetainFields_DateTimeZone() {
        DateTime test = new DateTime(TEST_TIME1);
        DateTime result = test.withZoneRetainFields(PARIS);
        assertEquals(test.getMillis() - DateTimeConstants.MILLIS_PER_HOUR, result.getMillis());
        assertEquals(ISOChronology.getInstance(PARIS), result.getChronology());
        
        test = new DateTime(TEST_TIME1);
        result = test.withZoneRetainFields(LONDON);
        assertSame(test, result);
        
        test = new DateTime(TEST_TIME1);
        result = test.withZoneRetainFields(null);
        assertSame(test, result);
        
        test = new DateTime(TEST_TIME1, GregorianChronology.getInstance(PARIS));
        result = test.withZoneRetainFields(null);
        assertEquals(test.getMillis() + DateTimeConstants.MILLIS_PER_HOUR, result.getMillis());
        assertEquals(GregorianChronology.getInstance(), result.getChronology());
        
        test = new DateTime(TEST_TIME1, new MockNullZoneChronology());
        result = test.withZoneRetainFields(LONDON);
        assertSame(test, result);
    }
    
    public void testImmutable() {
        MockChangeDateTime test = new MockChangeDateTime(TEST_TIME_NOW);
        assertEquals(TEST_TIME_NOW, test.getMillis());
        test.testSetMillis();
        assertEquals(TEST_TIME_NOW, test.getMillis());
        
        test = new MockChangeDateTime(TEST_TIME_NOW);
        assertEquals(TEST_TIME_NOW, test.getMillis());
        test.testSetMillisObject();
        assertEquals(TEST_TIME_NOW, test.getMillis());
        
        test = new MockChangeDateTime(TEST_TIME_NOW);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        test.testSetChronology();
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        
        test = new MockChangeDateTime(TEST_TIME_NOW);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        test.testSetZone();
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        
        test = new MockChangeDateTime(TEST_TIME_NOW);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        test.testSetZoneRetainFields();
        assertEquals(ISOChronology.getInstance(), test.getChronology());
    }
    
    static class MockChangeDateTime extends DateTime {
        MockChangeDateTime(long instant) {
            super(instant);
        }
        public void testSetMillis() {
            setMillis(0L);
        }
        public void testSetMillisObject() {
            setMillis(new Date(0L));
        }
        public void testSetChronology() {
            setChronology(GregorianChronology.getInstance(PARIS));
        }
        public void testSetZone() {
            setZone(PARIS);
        }
        public void testSetZoneRetainFields() {
            setZoneRetainFields(PARIS);
        }
    }

}
