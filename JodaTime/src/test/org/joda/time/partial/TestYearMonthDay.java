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
package org.joda.time.partial;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.MockZeroNullIntegerConverter;

/**
 * This class is a Junit unit test for YearMonthDay.
 *
 * @author Stephen Colebourne
 */
public class TestYearMonthDay extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.getInstance("Europe/Paris");
    
    private long TEST_TIME_NOW =
            (31L + 28L + 31L + 30L + 31L + 9L -1L) * DateTimeConstants.MILLIS_PER_DAY;
            
    private long TEST_TIME1 =
        (31L + 28L + 31L + 6L -1L) * DateTimeConstants.MILLIS_PER_DAY
        + 12L * DateTimeConstants.MILLIS_PER_HOUR
        + 24L * DateTimeConstants.MILLIS_PER_MINUTE;
        
    private long TEST_TIME2 =
        (365L + 31L + 28L + 31L + 30L + 7L -1L) * DateTimeConstants.MILLIS_PER_DAY
        + 14L * DateTimeConstants.MILLIS_PER_HOUR
        + 28L * DateTimeConstants.MILLIS_PER_MINUTE;
        
    private DateTimeZone zone = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestYearMonthDay.class);
    }

    public TestYearMonthDay(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        zone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(zone);
        zone = null;
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor ()
     */
    public void testConstructor() throws Throwable {
        YearMonthDay test = new YearMonthDay();
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(1970, test.getYear());
        assertEquals(6, test.getMonthOfYear());
        assertEquals(9, test.getDayOfMonth());
    }

    /**
     * Test constructor (Chronology)
     */
    public void testConstructor_Chronology() throws Throwable {
        YearMonthDay test = new YearMonthDay(GregorianChronology.getInstance());
        assertEquals(GregorianChronology.getInstance(), test.getChronology());
        assertEquals(1970, test.getYear());
        assertEquals(6, test.getMonthOfYear());
        assertEquals(9, test.getDayOfMonth());
    }

    /**
     * Test constructor (Chronology=null)
     */
    public void testConstructor_nullChronology() throws Throwable {
        YearMonthDay test = new YearMonthDay((Chronology) null);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(1970, test.getYear());
        assertEquals(6, test.getMonthOfYear());
        assertEquals(9, test.getDayOfMonth());
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor (long)
     */
    public void testConstructor_long1() throws Throwable {
        YearMonthDay test = new YearMonthDay(TEST_TIME1);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(1970, test.getYear());
        assertEquals(4, test.getMonthOfYear());
        assertEquals(6, test.getDayOfMonth());
    }

    /**
     * Test constructor (long)
     */
    public void testConstructor_long2() throws Throwable {
        YearMonthDay test = new YearMonthDay(TEST_TIME2);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(1971, test.getYear());
        assertEquals(5, test.getMonthOfYear());
        assertEquals(7, test.getDayOfMonth());
    }

    /**
     * Test constructor (long, Chronology)
     */
    public void testConstructor_long1_Chronology() throws Throwable {
        YearMonthDay test = new YearMonthDay(TEST_TIME1, GregorianChronology.getInstance());
        assertEquals(GregorianChronology.getInstance(), test.getChronology());
        assertEquals(1970, test.getYear());
        assertEquals(4, test.getMonthOfYear());
        assertEquals(6, test.getDayOfMonth());
    }

    /**
     * Test constructor (long, Chronology)
     */
    public void testConstructor_long2_Chronology() throws Throwable {
        YearMonthDay test = new YearMonthDay(TEST_TIME2, GregorianChronology.getInstance());
        assertEquals(GregorianChronology.getInstance(), test.getChronology());
        assertEquals(1971, test.getYear());
        assertEquals(5, test.getMonthOfYear());
        assertEquals(7, test.getDayOfMonth());
    }

    /**
     * Test constructor (long, Chronology=null)
     */
    public void testConstructor_long_nullChronology() throws Throwable {
        YearMonthDay test = new YearMonthDay(TEST_TIME1, null);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(1970, test.getYear());
        assertEquals(4, test.getMonthOfYear());
        assertEquals(6, test.getDayOfMonth());
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor (Object)
     */
    public void testConstructor_Object() throws Throwable {
        Date date = new Date(TEST_TIME1);
        YearMonthDay test = new YearMonthDay(date);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(1970, test.getYear());
        assertEquals(4, test.getMonthOfYear());
        assertEquals(6, test.getDayOfMonth());
    }

    /**
     * Test constructor (Object=null)
     */
    public void testConstructor_nullObject() throws Throwable {
        YearMonthDay test = new YearMonthDay(null);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(1970, test.getYear());
        assertEquals(6, test.getMonthOfYear());
        assertEquals(9, test.getDayOfMonth());
    }

    /**
     * Test constructor (Object=null)
     */
    public void testConstructor_badconverterObject() throws Throwable {
        try {
            ConverterManager.getInstance().addInstantConverter(MockZeroNullIntegerConverter.INSTANCE);
            YearMonthDay test = new YearMonthDay(new Integer(0));
            assertEquals(ISOChronology.getInstance(), test.getChronology());
            assertEquals(1970, test.getYear());
            assertEquals(1, test.getMonthOfYear());
            assertEquals(1, test.getDayOfMonth());
        } finally {
            ConverterManager.getInstance().removeInstantConverter(MockZeroNullIntegerConverter.INSTANCE);
        }
    }

    /**
     * Test constructor (Object, Chronology)
     */
    public void testConstructor_Object_Chronology() throws Throwable {
        Date date = new Date(TEST_TIME1);
        YearMonthDay test = new YearMonthDay(date, GregorianChronology.getInstance());
        assertEquals(GregorianChronology.getInstance(), test.getChronology());
        assertEquals(1970, test.getYear());
        assertEquals(4, test.getMonthOfYear());
        assertEquals(6, test.getDayOfMonth());
    }

    /**
     * Test constructor (Object=null, Chronology)
     */
    public void testConstructor_nullObject_Chronology() throws Throwable {
        YearMonthDay test = new YearMonthDay((Object) null, GregorianChronology.getInstance());
        assertEquals(GregorianChronology.getInstance(), test.getChronology());
        assertEquals(1970, test.getYear());
        assertEquals(6, test.getMonthOfYear());
        assertEquals(9, test.getDayOfMonth());
    }

    /**
     * Test constructor (Object, Chronology=null)
     */
    public void testConstructor_Object_nullChronology() throws Throwable {
        Date date = new Date(TEST_TIME1);
        YearMonthDay test = new YearMonthDay(date, null);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(1970, test.getYear());
        assertEquals(4, test.getMonthOfYear());
        assertEquals(6, test.getDayOfMonth());
    }

    /**
     * Test constructor (Object=null, Chronology=null)
     */
    public void testConstructor_nullObject_nullChronology() throws Throwable {
        YearMonthDay test = new YearMonthDay((Object) null, null);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(1970, test.getYear());
        assertEquals(6, test.getMonthOfYear());
        assertEquals(9, test.getDayOfMonth());
    }

    /**
     * Test constructor (Object=null)
     */
    public void testConstructor_badconverterObject_Chronology() throws Throwable {
        try {
            ConverterManager.getInstance().addInstantConverter(MockZeroNullIntegerConverter.INSTANCE);
            YearMonthDay test = new YearMonthDay(new Integer(0), GregorianChronology.getInstance());
            assertEquals(ISOChronology.getInstance(), test.getChronology());
            assertEquals(1970, test.getYear());
            assertEquals(1, test.getMonthOfYear());
            assertEquals(1, test.getDayOfMonth());
        } finally {
            ConverterManager.getInstance().removeInstantConverter(MockZeroNullIntegerConverter.INSTANCE);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor (int, int, int)
     */
    public void testConstructor_int_int_int() throws Throwable {
        YearMonthDay test = new YearMonthDay(1970, 6, 9);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(1970, test.getYear());
        assertEquals(6, test.getMonthOfYear());
        assertEquals(9, test.getDayOfMonth());
        try {
            new YearMonthDay(Integer.MIN_VALUE, 6, 9);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new YearMonthDay(Integer.MAX_VALUE, 6, 9);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new YearMonthDay(1970, 0, 9);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new YearMonthDay(1970, 13, 9);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new YearMonthDay(1970, 6, 0);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new YearMonthDay(1970, 6, 31);
            fail();
        } catch (IllegalArgumentException ex) {}
        new YearMonthDay(1970, 7, 31);
        try {
            new YearMonthDay(1970, 7, 32);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    /**
     * Test constructor (int, int, int, Chronology)
     */
    public void testConstructor_int_int_int_Chronology() throws Throwable {
        YearMonthDay test = new YearMonthDay(1970, 6, 9, GregorianChronology.getInstance());
        assertEquals(GregorianChronology.getInstance(), test.getChronology());
        assertEquals(1970, test.getYear());
        assertEquals(6, test.getMonthOfYear());
        assertEquals(9, test.getDayOfMonth());
        try {
            new YearMonthDay(Integer.MIN_VALUE, 6, 9, GregorianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new YearMonthDay(Integer.MAX_VALUE, 6, 9, GregorianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new YearMonthDay(1970, 0, 9, GregorianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new YearMonthDay(1970, 13, 9, GregorianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new YearMonthDay(1970, 6, 0, GregorianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new YearMonthDay(1970, 6, 31, GregorianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
        new YearMonthDay(1970, 7, 31, GregorianChronology.getInstance());
        try {
            new YearMonthDay(1970, 7, 32, GregorianChronology.getInstance());
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    /**
     * Test constructor (int, int, int, Chronology=null)
     */
    public void testConstructor_int_int_int_nullChronology() throws Throwable {
        YearMonthDay test = new YearMonthDay(1970, 6, 9, null);
        assertEquals(ISOChronology.getInstance(), test.getChronology());
        assertEquals(1970, test.getYear());
        assertEquals(6, test.getMonthOfYear());
        assertEquals(9, test.getDayOfMonth());
    }

    //-----------------------------------------------------------------------
    public void testGet() {
        YearMonthDay test = new YearMonthDay();
        assertEquals(1970, test.get(ISOChronology.getInstance().year()));
        assertEquals(6, test.get(ISOChronology.getInstance().monthOfYear()));
        assertEquals(9, test.get(ISOChronology.getInstance().dayOfMonth()));
        try {
            test.get(null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.get(ISOChronology.getInstance().hourOfDay());
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.get(ISOChronology.getInstance(PARIS).year());
            fail();
        } catch (IllegalArgumentException ex) {}
        // TODO: Should this fail or suceed - by succeeding it exposes out implementation
//        try {
//            test.get(GregorianChronology.getInstance().year());
//            fail();
//        } catch (IllegalArgumentException ex) {}
    }

    public void testGetFieldSize() {
        YearMonthDay test = new YearMonthDay();
        assertEquals(3, test.getFieldSize());
    }

    public void testGetField() {
        YearMonthDay test = new YearMonthDay();
        assertSame(ISOChronology.getInstance().year(), test.getField(0));
        assertSame(ISOChronology.getInstance().monthOfYear(), test.getField(1));
        assertSame(ISOChronology.getInstance().dayOfMonth(), test.getField(2));
        try {
            test.getField(-1);
        } catch (IllegalArgumentException ex) {}
        try {
            test.getField(3);
        } catch (IllegalArgumentException ex) {}
    }

    public void testGetFields() {
        YearMonthDay test = new YearMonthDay();
        DateTimeField[] fields = test.getFields();
        assertSame(ISOChronology.getInstance().year(), fields[0]);
        assertSame(ISOChronology.getInstance().monthOfYear(), fields[1]);
        assertSame(ISOChronology.getInstance().dayOfMonth(), fields[2]);
    }

    public void testGetValue() {
        YearMonthDay test = new YearMonthDay();
        assertEquals(1970, test.getValue(0));
        assertEquals(6, test.getValue(1));
        assertEquals(9, test.getValue(2));
        try {
            test.getValue(-1);
        } catch (IllegalArgumentException ex) {}
        try {
            test.getValue(3);
        } catch (IllegalArgumentException ex) {}
    }

    public void testGetValues() {
        YearMonthDay test = new YearMonthDay();
        int[] values = test.getValues();
        assertEquals(1970, values[0]);
        assertEquals(6, values[1]);
        assertEquals(9, values[2]);
    }

    public void testIsSupported() {
        YearMonthDay test = new YearMonthDay();
        assertEquals(true, test.isSupported(ISOChronology.getInstance().year()));
        assertEquals(true, test.isSupported(ISOChronology.getInstance().monthOfYear()));
        assertEquals(true, test.isSupported(ISOChronology.getInstance().dayOfMonth()));
        assertEquals(false, test.isSupported(ISOChronology.getInstance().hourOfDay()));
        assertEquals(false, test.isSupported(ISOChronology.getInstance(PARIS).year()));
    }

    public void testEqualsHashCode() {
        YearMonthDay test1 = new YearMonthDay(1970, 6, 9);
        YearMonthDay test2 = new YearMonthDay(1970, 6, 9);
        assertEquals(true, test1.equals(test2));
        assertEquals(true, test2.equals(test1));
        assertEquals(true, test1.equals(test1));
        assertEquals(true, test2.equals(test2));
        assertEquals(true, test1.hashCode() == test2.hashCode());
        assertEquals(true, test1.hashCode() == test1.hashCode());
        assertEquals(true, test2.hashCode() == test2.hashCode());
        
        YearMonthDay test3 = new YearMonthDay(1971, 6, 9);
        assertEquals(false, test1.equals(test3));
        assertEquals(false, test2.equals(test3));
        assertEquals(false, test3.equals(test1));
        assertEquals(false, test3.equals(test2));
        assertEquals(false, test1.hashCode() == test3.hashCode());
        assertEquals(false, test2.hashCode() == test3.hashCode());
        
        assertEquals(false, test1.equals("Hello"));
        assertEquals(true, test1.equals(new MockInstant()));
        assertEquals(false, test1.equals(MockPartialInstant.EMPTY_INSTANCE));
    }
    
    class MockInstant extends MockPartialInstant {
        public DateTimeField[] getFields() {
            return new DateTimeField[] {
                ISOChronology.getInstance().year(),
                ISOChronology.getInstance().monthOfYear(),
                ISOChronology.getInstance().dayOfMonth(),
            };
        }
        public int[] getValues() {
            return new int[] {1970, 6, 9};
        }
    }

    //-----------------------------------------------------------------------
    public void testResolve_long() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        DateTime dt = new DateTime(TEST_TIME1);
        assertEquals("1970-04-06T12:24:00.000Z", dt.toString());
        
        DateTime result = new DateTime(test.resolve(dt.getMillis(), DateTimeZone.UTC));
        check(test, 1972, 6, 9);
        assertEquals("1970-04-06T12:24:00.000Z", dt.toString());
        assertEquals("1972-06-09T12:24:00.000Z", result.toString());
    }

    public void testResolveDateTime_RI() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        DateTime dt = new DateTime(TEST_TIME1);
        assertEquals("1970-04-06T12:24:00.000Z", dt.toString());
        
        DateTime result = test.resolveDateTime(dt);
        check(test, 1972, 6, 9);
        assertEquals("1970-04-06T12:24:00.000Z", dt.toString());
        assertEquals("1972-06-09T12:24:00.000Z", result.toString());
    }

    public void testResolveDateTime_nullRI() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME1);
        
        DateTime result = test.resolveDateTime(null);
        check(test, 1972, 6, 9);
        assertEquals("1972-06-09T12:24:00.000Z", result.toString());
    }

    public void testResolveInto_RWI() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        MutableDateTime mdt = new MutableDateTime(TEST_TIME1);
        assertEquals("1970-04-06T12:24:00.000Z", mdt.toString());
        
        test.resolveInto(mdt);
        check(test, 1972, 6, 9);
        assertEquals("1972-06-09T12:24:00.000Z", mdt.toString());
    }

    public void testResolveInto_nullRWI() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        
        try {
            test.resolveInto(null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testSerialization() throws Exception {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(test);
        byte[] bytes = baos.toByteArray();
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        YearMonthDay result = (YearMonthDay) ois.readObject();
        ois.close();
        
        assertEquals(test, result);
        assertTrue(Arrays.equals(test.getValues(), result.getValues()));
        assertTrue(Arrays.equals(test.getFields(), result.getFields()));
        assertEquals(test.getChronology(), result.getChronology());
    }

    //-----------------------------------------------------------------------
    public void testToString() {
        // TODO
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetYear() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        assertSame(test.getChronology().year(), test.year().getField());
        assertEquals("year", test.year().getName());
        assertEquals("Property[year]", test.year().toString());
        assertSame(test, test.year().getPartialInstant());
        assertSame(test, test.year().getYearMonthDay());
        assertEquals(1972, test.year().get());
        assertEquals("1972", test.year().getAsText());
        assertEquals("1972", test.year().getAsText(Locale.FRENCH));
        assertEquals("1972", test.year().getAsShortText());
        assertEquals("1972", test.year().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().years(), test.year().getDurationField());
        assertEquals(null, test.year().getRangeDurationField());
        assertEquals(9, test.year().getMaximumTextLength(null));
        assertEquals(9, test.year().getMaximumShortTextLength(null));
    }

    public void testPropertyGetMaxMinValuesYear() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        assertEquals(-292275054, test.year().getMinimumValue());
        assertEquals(-292275054, test.year().getMinimumValueOverall());
        assertEquals(292277023, test.year().getMaximumValue());
        assertEquals(292277023, test.year().getMaximumValueOverall());
    }

    public void testPropertyAddYear() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        YearMonthDay copy = test.year().addCopy(9);
        check(test, 1972, 6, 9);
        check(copy, 1981, 6, 9);
        
        copy = test.year().addCopy(0);
        check(copy, 1972, 6, 9);
        
        copy = test.year().addCopy(292277023 - 1972);
        check(copy, 292277023, 6, 9);
        
        try {
            test.year().addCopy(292277023 - 1972 + 1);
            fail();
        } catch (IllegalArgumentException ex) {}
        check(test, 1972, 6, 9);
        
        copy = test.year().addCopy(-1972);
        check(copy, 0, 6, 9);
        
        copy = test.year().addCopy(-1973);
        check(copy, -1, 6, 9);
        
        try {
            test.year().addCopy(-292275054 - 1972 - 1);
            fail();
        } catch (IllegalArgumentException ex) {}
        check(test, 1972, 6, 9);
    }

    public void testPropertyAddInFieldYear() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        YearMonthDay copy = test.year().addInFieldCopy(9);
        check(test, 1972, 6, 9);
        check(copy, 1981, 6, 9);
        
        copy = test.year().addInFieldCopy(0);
        check(copy, 1972, 6, 9);
        
        copy = test.year().addInFieldCopy(292277023 - 1972 + 1);
        check(copy, -292275054, 6, 9);
        
        copy = test.year().addInFieldCopy(-292275054 - 1972 - 1);
        check(copy, 292277023, 6, 9);
    }

    public void testPropertySetYear() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        YearMonthDay copy = test.year().setCopy(12);
        check(test, 1972, 6, 9);
        check(copy, 12, 6, 9);
    }

    public void testPropertySetTextYear() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        YearMonthDay copy = test.year().setCopy("12");
        check(test, 1972, 6, 9);
        check(copy, 12, 6, 9);
    }

    public void testPropertyCompareToYear() {
        YearMonthDay test1 = new YearMonthDay(TEST_TIME1);
        YearMonthDay test2 = new YearMonthDay(TEST_TIME2);
        assertEquals(true, test1.year().compareTo(test2) < 0);
        assertEquals(true, test2.year().compareTo(test1) > 0);
        assertEquals(true, test1.year().compareTo(test1) == 0);
        try {
            test1.year().compareTo((PartialInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        DateTime dt1 = new DateTime(TEST_TIME1);
        DateTime dt2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.year().compareTo(dt2) < 0);
        assertEquals(true, test2.year().compareTo(dt1) > 0);
        assertEquals(true, test1.year().compareTo(dt1) == 0);
        try {
            test1.year().compareTo((ReadableInstant) null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testPropertyGetMonth() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        assertSame(test.getChronology().monthOfYear(), test.monthOfYear().getField());
        assertEquals("monthOfYear", test.monthOfYear().getName());
        assertEquals("Property[monthOfYear]", test.monthOfYear().toString());
        assertSame(test, test.monthOfYear().getPartialInstant());
        assertSame(test, test.monthOfYear().getYearMonthDay());
        assertEquals(6, test.monthOfYear().get());
        assertEquals("June", test.monthOfYear().getAsText());
        assertEquals("juin", test.monthOfYear().getAsText(Locale.FRENCH));
        assertEquals("Jun", test.monthOfYear().getAsShortText());
        assertEquals("juin", test.monthOfYear().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().months(), test.monthOfYear().getDurationField());
        assertEquals(test.getChronology().years(), test.monthOfYear().getRangeDurationField());
        assertEquals(9, test.monthOfYear().getMaximumTextLength(null));
        assertEquals(3, test.monthOfYear().getMaximumShortTextLength(null));
        test = new YearMonthDay(1972, 7, 9);
        assertEquals("juillet", test.monthOfYear().getAsText(Locale.FRENCH));
        assertEquals("juil.", test.monthOfYear().getAsShortText(Locale.FRENCH));
    }

    public void testPropertyGetMaxMinValuesMonth() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        assertEquals(1, test.monthOfYear().getMinimumValue());
        assertEquals(1, test.monthOfYear().getMinimumValueOverall());
        assertEquals(12, test.monthOfYear().getMaximumValue());
        assertEquals(12, test.monthOfYear().getMaximumValueOverall());
    }

    public void testPropertyAddMonth() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        YearMonthDay copy = test.monthOfYear().addCopy(6);
        check(test, 1972, 6, 9);
        check(copy, 1972, 12, 9);
        
        copy = test.monthOfYear().addCopy(7);
        check(copy, 1973, 1, 9);
        
        copy = test.monthOfYear().addCopy(-5);
        check(copy, 1972, 1, 9);
        
        copy = test.monthOfYear().addCopy(-6);
        check(copy, 1971, 12, 9);
        
        test = new YearMonthDay(1972, 1, 31);
        copy = test.monthOfYear().addCopy(1);
        check(copy, 1972, 2, 29);
        
        copy = test.monthOfYear().addCopy(2);
        check(copy, 1972, 3, 31);
        
        copy = test.monthOfYear().addCopy(3);
        check(copy, 1972, 4, 30);
        
        test = new YearMonthDay(1971, 1, 31);
        copy = test.monthOfYear().addCopy(1);
        check(copy, 1971, 2, 28);
    }

    public void testPropertyAddInFieldMonth() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        YearMonthDay copy = test.monthOfYear().addInFieldCopy(4);
        check(test, 1972, 6, 9);
        check(copy, 1972, 10, 9);
        
        copy = test.monthOfYear().addInFieldCopy(8);
        check(copy, 1972, 2, 9);
        
        copy = test.monthOfYear().addInFieldCopy(-8);
        check(copy, 1972, 10, 9);
        
        test = new YearMonthDay(1972, 1, 31);
        copy = test.monthOfYear().addInFieldCopy(1);
        check(copy, 1972, 2, 29);
        
        copy = test.monthOfYear().addInFieldCopy(2);
        check(copy, 1972, 3, 31);
        
        copy = test.monthOfYear().addInFieldCopy(3);
        check(copy, 1972, 4, 30);
        
        test = new YearMonthDay(1971, 1, 31);
        copy = test.monthOfYear().addInFieldCopy(1);
        check(copy, 1971, 2, 28);
    }

    public void testPropertySetMonth() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        YearMonthDay copy = test.monthOfYear().setCopy(12);
        check(test, 1972, 6, 9);
        check(copy, 1972, 12, 9);
        
        test = new YearMonthDay(1972, 1, 31);
        copy = test.monthOfYear().setCopy(2);
        check(copy, 1972, 2, 29);
        
        try {
            test.monthOfYear().setCopy(13);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.monthOfYear().setCopy(0);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testPropertySetTextMonth() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        YearMonthDay copy = test.monthOfYear().setCopy("12");
        check(test, 1972, 6, 9);
        check(copy, 1972, 12, 9);
        
        copy = test.monthOfYear().setCopy("December");
        check(test, 1972, 6, 9);
        check(copy, 1972, 12, 9);
        
        copy = test.monthOfYear().setCopy("Dec");
        check(test, 1972, 6, 9);
        check(copy, 1972, 12, 9);
    }

    public void testPropertyCompareToMonth() {
        YearMonthDay test1 = new YearMonthDay(TEST_TIME1);
        YearMonthDay test2 = new YearMonthDay(TEST_TIME2);
        assertEquals(true, test1.monthOfYear().compareTo(test2) < 0);
        assertEquals(true, test2.monthOfYear().compareTo(test1) > 0);
        assertEquals(true, test1.monthOfYear().compareTo(test1) == 0);
        try {
            test1.monthOfYear().compareTo((PartialInstant) null);
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
    public void testPropertyGetDay() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        assertSame(test.getChronology().dayOfMonth(), test.dayOfMonth().getField());
        assertEquals("dayOfMonth", test.dayOfMonth().getName());
        assertEquals("Property[dayOfMonth]", test.dayOfMonth().toString());
        assertSame(test, test.dayOfMonth().getPartialInstant());
        assertSame(test, test.dayOfMonth().getYearMonthDay());
        assertEquals(9, test.dayOfMonth().get());
        assertEquals("9", test.dayOfMonth().getAsText());
        assertEquals("9", test.dayOfMonth().getAsText(Locale.FRENCH));
        assertEquals("9", test.dayOfMonth().getAsShortText());
        assertEquals("9", test.dayOfMonth().getAsShortText(Locale.FRENCH));
        assertEquals(test.getChronology().days(), test.dayOfMonth().getDurationField());
        assertEquals(test.getChronology().months(), test.dayOfMonth().getRangeDurationField());
        assertEquals(2, test.dayOfMonth().getMaximumTextLength(null));
        assertEquals(2, test.dayOfMonth().getMaximumShortTextLength(null));
    }

    public void testPropertyGetMaxMinValuesDay() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        assertEquals(1, test.dayOfMonth().getMinimumValue());
        assertEquals(1, test.dayOfMonth().getMinimumValueOverall());
        assertEquals(30, test.dayOfMonth().getMaximumValue());
        assertEquals(31, test.dayOfMonth().getMaximumValueOverall());
        test = new YearMonthDay(1972, 7, 9);
        assertEquals(31, test.dayOfMonth().getMaximumValue());
        test = new YearMonthDay(1972, 2, 9);
        assertEquals(29, test.dayOfMonth().getMaximumValue());
        test = new YearMonthDay(1971, 2, 9);
        assertEquals(28, test.dayOfMonth().getMaximumValue());
    }

    public void testPropertyAddDay() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        YearMonthDay copy = test.dayOfMonth().addCopy(9);
        check(test, 1972, 6, 9);
        check(copy, 1972, 6, 18);
        
        copy = test.dayOfMonth().addCopy(21);
        check(copy, 1972, 6, 30);
        
        copy = test.dayOfMonth().addCopy(22);
        check(copy, 1972, 7, 1);
        
        copy = test.dayOfMonth().addCopy(22 + 30);
        check(copy, 1972, 7, 31);
        
        copy = test.dayOfMonth().addCopy(22 + 31);
        check(copy, 1972, 8, 1);

        copy = test.dayOfMonth().addCopy(21 + 31 + 31 + 30 + 31 + 30 + 31);
        check(copy, 1972, 12, 31);
        
        copy = test.dayOfMonth().addCopy(22 + 31 + 31 + 30 + 31 + 30 + 31);
        check(copy, 1973, 1, 1);
        
        copy = test.dayOfMonth().addCopy(-8);
        check(copy, 1972, 6, 1);
        
        copy = test.dayOfMonth().addCopy(-9);
        check(copy, 1972, 5, 31);
        
        copy = test.dayOfMonth().addCopy(-8 - 31 - 30 - 31 - 29 - 31);
        check(copy, 1972, 1, 1);
        
        copy = test.dayOfMonth().addCopy(-9 - 31 - 30 - 31 - 29 - 31);
        check(copy, 1971, 12, 31);
    }

    public void testPropertyAddInFieldDay() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        YearMonthDay copy = test.dayOfMonth().addInFieldCopy(21);
        check(test, 1972, 6, 9);
        check(copy, 1972, 6, 30);
        
        copy = test.dayOfMonth().addInFieldCopy(22);
        check(copy, 1972, 6, 1);
        
        copy = test.dayOfMonth().addInFieldCopy(-12);
        check(copy, 1972, 6, 27);
        
        test = new YearMonthDay(1972, 7, 9);
        copy = test.dayOfMonth().addInFieldCopy(21);
        check(copy, 1972, 7, 30);
    
        copy = test.dayOfMonth().addInFieldCopy(22);
        check(copy, 1972, 7, 31);
    
        copy = test.dayOfMonth().addInFieldCopy(23);
        check(copy, 1972, 7, 1);
    
        copy = test.dayOfMonth().addInFieldCopy(-12);
        check(copy, 1972, 7, 28);
    }

    public void testPropertySetDay() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        YearMonthDay copy = test.dayOfMonth().setCopy(12);
        check(test, 1972, 6, 9);
        check(copy, 1972, 6, 12);
        
        try {
            test.dayOfMonth().setCopy(31);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            test.dayOfMonth().setCopy(0);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testPropertySetTextDay() {
        YearMonthDay test = new YearMonthDay(1972, 6, 9);
        YearMonthDay copy = test.dayOfMonth().setCopy("12");
        check(test, 1972, 6, 9);
        check(copy, 1972, 6, 12);
    }

    public void testPropertyCompareToDay() {
        YearMonthDay test1 = new YearMonthDay(TEST_TIME1);
        YearMonthDay test2 = new YearMonthDay(TEST_TIME2);
        assertEquals(true, test1.dayOfMonth().compareTo(test2) < 0);
        assertEquals(true, test2.dayOfMonth().compareTo(test1) > 0);
        assertEquals(true, test1.dayOfMonth().compareTo(test1) == 0);
        try {
            test1.dayOfMonth().compareTo((PartialInstant) null);
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
    private void check(YearMonthDay test, int hour, int min, int sec) {
        assertEquals(hour, test.getYear());
        assertEquals(min, test.getMonthOfYear());
        assertEquals(sec, test.getDayOfMonth());
    }
}
