/*
 *  Copyright 2001-2005 Stephen Colebourne
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.field.DelegatedDurationField;
import org.joda.time.field.MillisDurationField;
import org.joda.time.field.UnsupportedDateTimeField;
import org.joda.time.field.UnsupportedDurationField;

/**
 * This class is a Junit unit test for serialization.
 *
 * @author Stephen Colebourne
 */
public class TestSerialization extends TestCase {
    // Test in 2002/03 as time zones are more well known
    // (before the late 90's they were all over the place)

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
    
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

    private static class MockDelegatedDurationField extends DelegatedDurationField implements Serializable {
        private static final long serialVersionUID = 1878496002811998493L;        
        public MockDelegatedDurationField() {
            super(MillisDurationField.INSTANCE);
        }
    }

    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestSerialization.class);
    }

    public TestSerialization(String name) {
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
    public void testSerializedInstant() throws Exception {
        Instant test = new Instant();
        loadAndCompare(test, "Instant.dat", false);
        inlineCompare(test, false);
    }

    public void testSerializedDateTime() throws Exception {
        DateTime test = new DateTime();
        loadAndCompare(test, "DateTime.dat", false);
        inlineCompare(test, false);
    }

    public void testSerializedDateTimeProperty() throws Exception {
        DateTime.Property test = new DateTime().hourOfDay();
        loadAndCompare(test, "DateTimeProperty.dat", false);
        inlineCompare(test, false);
    }

    public void testSerializedMutableDateTime() throws Exception {
        MutableDateTime test = new MutableDateTime();
        loadAndCompare(test, "MutableDateTime.dat", false);
        inlineCompare(test, false);
    }

    public void testSerializedMutableDateTimeProperty() throws Exception {
        MutableDateTime.Property test = new MutableDateTime().hourOfDay();
        loadAndCompare(test, "MutableDateTimeProperty.dat", false);
        inlineCompare(test, false);
    }

    public void testSerializedDateMidnight() throws Exception {
        DateMidnight test = new DateMidnight();
        loadAndCompare(test, "DateMidnight.dat", false);
        inlineCompare(test, false);
    }

    public void testSerializedDateMidnightProperty() throws Exception {
        DateMidnight.Property test = new DateMidnight().monthOfYear();
        loadAndCompare(test, "DateMidnightProperty.dat", false);
        inlineCompare(test, false);
    }

    public void testSerializedYearMonthDay() throws Exception {
        YearMonthDay test = new YearMonthDay();
        loadAndCompare(test, "YearMonthDay.dat", false);
        inlineCompare(test, false);
    }

    public void testSerializedTimeOfDay() throws Exception {
        TimeOfDay test = new TimeOfDay();
        loadAndCompare(test, "TimeOfDay.dat", false);
        inlineCompare(test, false);
    }

    public void testSerializedDateTimeZoneUTC() throws Exception {
        DateTimeZone test = DateTimeZone.UTC;
        loadAndCompare(test, "DateTimeZoneUTC.dat", true);
        inlineCompare(test, true);
    }

    public void testSerializedDateTimeZone() throws Exception {
        // have to re-get the zone, as TestDateTimeZone may have
        // changed the cache, or a SoftReference may have got cleared
        DateTimeZone test = DateTimeZone.forID("Europe/Paris");
        loadAndCompare(test, "DateTimeZone.dat", true);
        inlineCompare(test, true);
    }

    public void testSerializedCopticChronology() throws Exception {
        CopticChronology test = CopticChronology.getInstance(LONDON);
        loadAndCompare(test, "CopticChronology.dat", true);
        inlineCompare(test, true);
    }

    public void testSerializedISOChronology() throws Exception {
        ISOChronology test = ISOChronology.getInstance(PARIS);
        loadAndCompare(test, "ISOChronology.dat", true);
        inlineCompare(test, true);
    }

    public void testSerializedGJChronology() throws Exception {
        GJChronology test = GJChronology.getInstance(TOKYO);
        loadAndCompare(test, "GJChronology.dat", true);
        inlineCompare(test, true);
    }

    public void testSerializedGJChronologyChangedInternals() throws Exception {
        GJChronology test = GJChronology.getInstance(PARIS, 123L, 2);
        loadAndCompare(test, "GJChronologyChangedInternals.dat", true);
        inlineCompare(test, true);
    }

    public void testSerializedGregorianChronology() throws Exception {
        GregorianChronology test = GregorianChronology.getInstance(PARIS);
        loadAndCompare(test, "GregorianChronology.dat", true);
        inlineCompare(test, true);
    }

    public void testSerializedJulianChronology() throws Exception {
        JulianChronology test = JulianChronology.getInstance(PARIS);
        loadAndCompare(test, "JulianChronology.dat", true);
        inlineCompare(test, true);
    }

    public void testSerializedBuddhistChronology() throws Exception {
        BuddhistChronology test = BuddhistChronology.getInstance(PARIS);
        loadAndCompare(test, "BuddhistChronology.dat", true);
        inlineCompare(test, true);
    }

    public void testSerializedPeriodType() throws Exception {
        PeriodType test = PeriodType.dayTime();
        loadAndCompare(test, "PeriodType.dat", false);
        inlineCompare(test, false);
    }

    public void testSerializedDateTimeFieldType() throws Exception {
        DateTimeFieldType test = DateTimeFieldType.clockhourOfDay();
        loadAndCompare(test, "DateTimeFieldType.dat", true);
        inlineCompare(test, true);
    }

    public void testSerializedUnsupportedDateTimeField() throws Exception {
        UnsupportedDateTimeField test = UnsupportedDateTimeField.getInstance(
                DateTimeFieldType.year(),
                UnsupportedDurationField.getInstance(DurationFieldType.years()));
        loadAndCompare(test, "UnsupportedDateTimeField.dat", true);
        inlineCompare(test, true);
    }

    private void loadAndCompare(Serializable test, String filename, boolean same) throws Exception {
        FileInputStream fis = new FileInputStream("src/test/resources/" + filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object obj = ois.readObject();
        ois.close();
        if (same) {
            assertSame(test, obj);
        } else {
            assertEquals(test, obj);
        }
    }

    public void inlineCompare(Serializable test, boolean same) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(test);
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object obj = ois.readObject();
        ois.close();
        
        if (same) {
            assertSame(test, obj);
        } else {
            assertEquals(test, obj);
        }
    }

//    //-----------------------------------------------------------------------
//    public void testStoreSerializedInstant() throws Exception {
//        Instant test = new Instant();
//        store(test, "Instant.dat");
//    }
//
//    public void testStoreSerializedDateTime() throws Exception {
//        DateTime test = new DateTime();
//        store(test, "DateTime.dat");
//    }
//
//    public void testStoreSerializedMutableDateTime() throws Exception {
//        MutableDateTime test = new MutableDateTime();
//        store(test, "MutableDateTime.dat");
//    }
//
//    public void testStoreSerializedDateMidnight() throws Exception {
//        DateMidnight test = new DateMidnight();
//        store(test, "DateMidnight.dat");
//    }
//
//    public void testStoreSerializedYearMonthDay() throws Exception {
//        YearMonthDay test = new YearMonthDay();
//        store(test, "YearMonthDay.dat");
//    }
//
//    public void testStoreSerializedYearMonthDayProperty() throws Exception {
//        YearMonthDay.Property test = new YearMonthDay().monthOfYear();
//        store(test, "YearMonthDayProperty.dat");
//    }
//
//    public void testStoreSerializedTimeOfDay() throws Exception {
//        TimeOfDay test = new TimeOfDay();
//        store(test, "TimeOfDay.dat");
//    }
//
//    public void testStoreSerializedTimeOfDayProperty() throws Exception {
//        TimeOfDay.Property test = new TimeOfDay().hourOfDay();
//        store(test, "TimeOfDayProperty.dat");
//    }
//
//    public void testStoreSerializedDateTimeZoneUTC() throws Exception {
//        DateTimeZone test = DateTimeZone.UTC;
//        store(test, "DateTimeZoneUTC.dat");
//    }
//
//    public void testStoreSerializedDateTimeZone() throws Exception {
//        DateTimeZone test = PARIS;
//        store(test, "DateTimeZone.dat");
//    }
//
//    public void testStoreSerializedCopticChronology() throws Exception {
//        CopticChronology test = CopticChronology.getInstance(LONDON);
//        store(test, "CopticChronology.dat");
//    }
//
//    public void testStoreSerializedISOChronology() throws Exception {
//        ISOChronology test = ISOChronology.getInstance(PARIS);
//        store(test, "ISOChronology.dat");
//    }
//
//    public void testStoreSerializedGJChronology() throws Exception {
//        GJChronology test = GJChronology.getInstance(TOKYO);
//        store(test, "GJChronology.dat");
//    }
//
//    // Format changed in v1.2 - min days in first week not deserialized in v1.0/1.1
//    public void testStoreSerializedGJChronologyChangedInternals() throws Exception {
//        GJChronology test = GJChronology.getInstance(PARIS, 123L, 2);
//        store(test, "GJChronologyChangedInternals.dat");
//    }
//
//    public void testStoreSerializedGregorianChronology() throws Exception {
//        GregorianChronology test = GregorianChronology.getInstance(PARIS);
//        store(test, "GregorianChronology.dat");
//    }
//
//    public void testStoreSerializedJulianChronology() throws Exception {
//        JulianChronology test = JulianChronology.getInstance(PARIS);
//        store(test, "JulianChronology.dat");
//    }
//
//    public void testStoreSerializedBuddhistChronology() throws Exception {
//        BuddhistChronology test = BuddhistChronology.getInstance(PARIS);
//        store(test, "BuddhistChronology.dat");
//    }
//
//    public void testStoreSerializedPeriodType() throws Exception {
//        PeriodType test = PeriodType.dayTime();
//        store(test, "PeriodType.dat");
//    }
//
//    public void testStoreSerializedDateTimeFieldType() throws Exception {
//        DateTimeFieldType test = DateTimeFieldType.clockhourOfDay();
//        store(test, "DateTimeFieldType.dat");
//    }
//
//    public void testStoreSerializedUnsupportedDateTimeField() throws Exception {
//        UnsupportedDateTimeField test = UnsupportedDateTimeField.getInstance(
//                DateTimeFieldType.year(),
//                UnsupportedDurationField.getInstance(DurationFieldType.years()));
//        store(test, "UnsupportedDateTimeField.dat");
//    }
//
//    public void testStoreSerializedDurationFieldType() throws Exception {
//        DurationFieldType test = DurationFieldType.MINUTES_TYPE;
//        store(test, "DurationFieldType.dat");
//    }
//
//    public void testStoreSerializedMillisDurationField() throws Exception {
//        MillisDurationField test = (MillisDurationField) MillisDurationField.INSTANCE;
//        store(test, "MillisDurationField.dat");
//    }
//
//    public void testStoreSerializedDelegatedDurationField() throws Exception {
//        DelegatedDurationField test = new MockDelegatedDurationField();
//        store(test, "DelegatedDurationField.dat");
//    }
//
//    public void testStoreSerializedUnsupportedDurationField() throws Exception {
//        UnsupportedDurationField test = UnsupportedDurationField.getInstance(DurationFieldType.eras());
//        store(test, "UnsupportedDurationField.dat");
//    }
//
    // format changed (properly defined) in v1.1
//    public void testStoreSerializedDateTimeProperty() throws Exception {
//        DateTime.Property test = new DateTime().hourOfDay();
//        store(test, "DateTimeProperty.dat");
//    }
//
//    public void testStoreSerializedMutableDateTimeProperty() throws Exception {
//        MutableDateTime.Property test = new MutableDateTime().hourOfDay();
//        store(test, "MutableDateTimeProperty.dat");
//    }
//
//    public void testStoreSerializedDateMidnightProperty() throws Exception {
//        DateMidnight.Property test = new DateMidnight().monthOfYear();
//        store(test, "DateMidnightProperty.dat");
//    }

    private void store(Serializable test, String filename) throws Exception {
        FileOutputStream fos = new FileOutputStream("src/test/resources/" + filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        try {
            oos.writeObject(test);
        } finally {
            oos.close();
        }
        oos.close();
    }

}
