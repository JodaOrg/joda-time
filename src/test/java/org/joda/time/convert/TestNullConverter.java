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
package org.joda.time.convert;

import org.joda.time.*;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;



/**
 * This class is a Junit unit test for NullConverter.
 *
 * @author Stephen Colebourne
 */
public class TestNullConverter  {

    private long TEST_TIME_NOW =
            20 * DateTimeConstants.MILLIS_PER_DAY
            + 10L * DateTimeConstants.MILLIS_PER_HOUR
            + 20L * DateTimeConstants.MILLIS_PER_MINUTE
            + 30L * DateTimeConstants.MILLIS_PER_SECOND
            + 40L;
            
    private static final DateTimeZone UTC = DateTimeZone.UTC;
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final Chronology ISO_PARIS = ISOChronology.getInstance(PARIS);
    private static Chronology ISO;
    private static Chronology JULIAN;
    
    private DateTimeZone zone = null;
    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

   @Before
   public void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(DateTimeZone.forID("Europe/London"));
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
        
        ISO = ISOChronology.getInstance();
        JULIAN = JulianChronology.getInstance();
    }

   @After
   public void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    //-----------------------------------------------------------------------
   @Test
    public void testSingleton() throws Exception {
        Class cls = NullConverter.class;
        assertEquals(false, Modifier.isPublic(cls.getModifiers()));
        assertEquals(false, Modifier.isProtected(cls.getModifiers()));
        assertEquals(false, Modifier.isPrivate(cls.getModifiers()));
        
        Constructor con = cls.getDeclaredConstructor((Class[]) null);
        assertEquals(1, cls.getDeclaredConstructors().length);
        assertEquals(true, Modifier.isProtected(con.getModifiers()));
        
        Field fld = cls.getDeclaredField("INSTANCE");
        assertEquals(false, Modifier.isPublic(fld.getModifiers()));
        assertEquals(false, Modifier.isProtected(fld.getModifiers()));
        assertEquals(false, Modifier.isPrivate(fld.getModifiers()));
    }

    //-----------------------------------------------------------------------
   @Test
    public void testSupportedType() throws Exception {
        assertEquals(null, NullConverter.INSTANCE.getSupportedType());
    }

    //-----------------------------------------------------------------------
   @Test
    public void testGetInstantMillis_Object_Chronology() throws Exception {
        assertEquals(TEST_TIME_NOW, NullConverter.INSTANCE.getInstantMillis(null, JULIAN));
        assertEquals(TEST_TIME_NOW, NullConverter.INSTANCE.getInstantMillis(null, (Chronology) null));
    }

    //-----------------------------------------------------------------------
   @Test
    public void testGetChronology_Object_Zone() throws Exception {
        assertEquals(ISO_PARIS, NullConverter.INSTANCE.getChronology(null, PARIS));
        assertEquals(ISO, NullConverter.INSTANCE.getChronology(null, (DateTimeZone) null));
    }

   @Test
    public void testGetChronology_Object_Chronology() throws Exception {
        assertEquals(JULIAN, NullConverter.INSTANCE.getChronology(null, JULIAN));
        assertEquals(ISO, NullConverter.INSTANCE.getChronology(null, (Chronology) null));
    }

    //-----------------------------------------------------------------------
   @Test
    public void testGetPartialValues() throws Exception {
        TimeOfDay tod = new TimeOfDay();
        int[] expected = new int[] {10 + 1, 20, 30, 40}; // now
        int[] actual = NullConverter.INSTANCE.getPartialValues(tod, null, ISOChronology.getInstance());
        assertEquals(true, Arrays.equals(expected, actual));
    }

    //-----------------------------------------------------------------------
   @Test
    public void testGetDurationMillis_Object() throws Exception {
        assertEquals(0L, NullConverter.INSTANCE.getDurationMillis(null));
    }

    //-----------------------------------------------------------------------
   @Test
    public void testGetPeriodType_Object() throws Exception {
        assertEquals(PeriodType.standard(),
            NullConverter.INSTANCE.getPeriodType(null));
    }

   @Test
    public void testSetInto_Object() throws Exception {
        MutablePeriod m = new MutablePeriod(PeriodType.millis());
        NullConverter.INSTANCE.setInto(m, null, null);
        assertEquals(0L, m.getMillis());
    }

    //-----------------------------------------------------------------------
   @Test
    public void testIsReadableInterval_Object_Chronology() throws Exception {
        assertEquals(false, NullConverter.INSTANCE.isReadableInterval(null, null));
    }

   @Test
    public void testSetInto_Object_Chronology1() throws Exception {
        MutableInterval m = new MutableInterval(1000L, 2000L, GJChronology.getInstance());
        NullConverter.INSTANCE.setInto(m, null, null);
        assertEquals(TEST_TIME_NOW, m.getStartMillis());
        assertEquals(TEST_TIME_NOW, m.getEndMillis());
        assertEquals(ISOChronology.getInstance(), m.getChronology());
    }

   @Test
    public void testSetInto_Object_Chronology2() throws Exception {
        MutableInterval m = new MutableInterval(1000L, 2000L, GJChronology.getInstance());
        NullConverter.INSTANCE.setInto(m, null, CopticChronology.getInstance());
        assertEquals(TEST_TIME_NOW, m.getStartMillis());
        assertEquals(TEST_TIME_NOW, m.getEndMillis());
        assertEquals(CopticChronology.getInstance(), m.getChronology());
    }

    //-----------------------------------------------------------------------
   @Test
    public void testToString() {
        assertEquals("Converter[null]", NullConverter.INSTANCE.toString());
    }

}
