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
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.ISOChronology;

/**
 * This class is a JUnit test for DurationType.
 *
 * @author Stephen Colebourne
 */
public class TestDurationType extends TestCase {
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
        return new TestSuite(TestDurationType.class);
    }

    public TestDurationType(String name) {
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
    private void assertEqualsAfterSerialization(DurationType type) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(type);
        byte[] bytes = baos.toByteArray();
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        DurationType result = (DurationType) ois.readObject();
        ois.close();
        
        assertEquals(type, result);
    }

    private void assertSameAfterSerialization(DurationType type) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(type);
        byte[] bytes = baos.toByteArray();
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        DurationType result = (DurationType) ois.readObject();
        ois.close();
        
        assertSame(type, result);
    }

    //-----------------------------------------------------------------------
    public void testMillisType() throws Exception {
        DurationType type = DurationType.getMillisType();
        assertEquals(false, type.years().isSupported());
        assertEquals(false, type.months().isSupported());
        assertEquals(false, type.weeks().isSupported());
        assertEquals(false, type.days().isSupported());
        assertEquals(false, type.hours().isSupported());
        assertEquals(false, type.minutes().isSupported());
        assertEquals(false, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(true, type.isPrecise());
        assertEquals(null, type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type == DurationType.getMillisType());
        assertEquals(false, type.equals(DurationType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == DurationType.getMillisType().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getAllType().hashCode());
        assertEquals("MillisType", type.getName());
        assertEquals("DurationType[MillisType]", type.toString());
        assertSameAfterSerialization(type);
        assertSame(type, type.withChronology(null));
        assertSame(type, type.withChronology(CopticChronology.getInstanceUTC()));
    }

    //-----------------------------------------------------------------------
    public void testDayHourType() throws Exception {
        DurationType type = DurationType.getDayHourType();
        assertEquals(false, type.years().isSupported());
        assertEquals(false, type.months().isSupported());
        assertEquals(false, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(true, type.isPrecise());
        assertEquals(ISOChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type == DurationType.getDayHourType());
        assertEquals(false, type.equals(DurationType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == DurationType.getDayHourType().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getAllType().hashCode());
        assertEquals("DayHourType", type.getName());
        assertEquals("DurationType[DayHourType]", type.toString());
        assertSameAfterSerialization(type);
        assertSame(type, type.withChronology(null));
        assertSame(type, type.withChronology(ISOChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC(), type.withChronology(CopticChronology.getInstanceUTC()).getChronology());
    }

    //-----------------------------------------------------------------------
    public void testDayHourType_Chronology() throws Exception {
        DurationType type = DurationType.getDayHourType(BuddhistChronology.getInstanceUTC());
        assertEquals(false, type.years().isSupported());
        assertEquals(false, type.months().isSupported());
        assertEquals(false, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(true, type.isPrecise());
        assertEquals(BuddhistChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, DurationType.getDayHourType() == DurationType.getDayHourType(null));
        assertEquals(true, DurationType.getDayHourType() == DurationType.getDayHourType(ISOChronology.getInstanceUTC()));
        assertEquals(true, type.equals(DurationType.getDayHourType(BuddhistChronology.getInstanceUTC())));
        assertEquals(false, type.equals(DurationType.getDayHourType()));
        assertEquals(false, type.equals(DurationType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(false, type.hashCode() == DurationType.getDayHourType().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getAllType().hashCode());
        assertEquals("DayHourType", type.getName());
        assertEquals("DurationType[DayHourType]", type.toString());
        assertEqualsAfterSerialization(type);
        assertSame(DurationType.getDayHourType(), type.withChronology(null));
        assertSame(DurationType.getDayHourType(), type.withChronology(ISOChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC(), type.withChronology(CopticChronology.getInstanceUTC()).getChronology());
    }

    //-----------------------------------------------------------------------
    public void testYearWeekType() throws Exception {
        DurationType type = DurationType.getYearWeekType();
        assertEquals(true, type.years().isSupported());
        assertEquals(false, type.months().isSupported());
        assertEquals(true, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(false, type.isPrecise());
        assertEquals(ISOChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type == DurationType.getYearWeekType());
        assertEquals(false, type.equals(DurationType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == DurationType.getYearWeekType().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getAllType().hashCode());
        assertEquals("YearWeekType", type.getName());
        assertEquals("DurationType[YearWeekType]", type.toString());
        assertSameAfterSerialization(type);
        assertSame(type, type.withChronology(null));
        assertSame(type, type.withChronology(ISOChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC(), type.withChronology(CopticChronology.getInstanceUTC()).getChronology());
    }

    //-----------------------------------------------------------------------
    public void testYearWeekType_Chronology() throws Exception {
        DurationType type = DurationType.getYearWeekType(BuddhistChronology.getInstanceUTC());
        assertEquals(true, type.years().isSupported());
        assertEquals(false, type.months().isSupported());
        assertEquals(true, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(false, type.isPrecise());
        assertEquals(BuddhistChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, DurationType.getYearWeekType() == DurationType.getYearWeekType(null));
        assertEquals(true, DurationType.getYearWeekType() == DurationType.getYearWeekType(ISOChronology.getInstanceUTC()));
        assertEquals(true, type.equals(DurationType.getYearWeekType(BuddhistChronology.getInstanceUTC())));
        assertEquals(false, type.equals(DurationType.getYearWeekType()));
        assertEquals(false, type.equals(DurationType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(false, type.hashCode() == DurationType.getYearWeekType().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getAllType().hashCode());
        assertEquals("YearWeekType", type.getName());
        assertEquals("DurationType[YearWeekType]", type.toString());
        assertEqualsAfterSerialization(type);
        assertSame(DurationType.getYearWeekType(), type.withChronology(null));
        assertSame(DurationType.getYearWeekType(), type.withChronology(ISOChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC(), type.withChronology(CopticChronology.getInstanceUTC()).getChronology());
    }

    //-----------------------------------------------------------------------
    public void testYearMonthType() throws Exception {
        DurationType type = DurationType.getYearMonthType();
        assertEquals(true, type.years().isSupported());
        assertEquals(true, type.months().isSupported());
        assertEquals(false, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(false, type.isPrecise());
        assertEquals(ISOChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type == DurationType.getYearMonthType());
        assertEquals(false, type.equals(DurationType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == DurationType.getYearMonthType().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getAllType().hashCode());
        assertEquals("YearMonthType", type.getName());
        assertEquals("DurationType[YearMonthType]", type.toString());
        assertSameAfterSerialization(type);
        assertSame(type, type.withChronology(null));
        assertSame(type, type.withChronology(ISOChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC(), type.withChronology(CopticChronology.getInstanceUTC()).getChronology());
    }

    //-----------------------------------------------------------------------
    public void testYearMonthType_Chronology() throws Exception {
        DurationType type = DurationType.getYearMonthType(BuddhistChronology.getInstanceUTC());
        assertEquals(true, type.years().isSupported());
        assertEquals(true, type.months().isSupported());
        assertEquals(false, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(false, type.isPrecise());
        assertEquals(BuddhistChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, DurationType.getYearMonthType() == DurationType.getYearMonthType(null));
        assertEquals(true, DurationType.getYearMonthType() == DurationType.getYearMonthType(ISOChronology.getInstanceUTC()));
        assertEquals(true, type.equals(DurationType.getYearMonthType(BuddhistChronology.getInstanceUTC())));
        assertEquals(false, type.equals(DurationType.getYearMonthType()));
        assertEquals(false, type.equals(DurationType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(false, type.hashCode() == DurationType.getYearMonthType().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getAllType().hashCode());
        assertEquals("YearMonthType", type.getName());
        assertEquals("DurationType[YearMonthType]", type.toString());
        assertEqualsAfterSerialization(type);
        assertSame(DurationType.getYearMonthType(), type.withChronology(null));
        assertSame(DurationType.getYearMonthType(), type.withChronology(ISOChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC(), type.withChronology(CopticChronology.getInstanceUTC()).getChronology());
    }

    //-----------------------------------------------------------------------
    public void testAllType() throws Exception {
        DurationType type = DurationType.getAllType();
        assertEquals(true, type.years().isSupported());
        assertEquals(true, type.months().isSupported());
        assertEquals(true, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(false, type.isPrecise());
        assertEquals(ISOChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type == DurationType.getAllType());
        assertEquals(false, type.equals(DurationType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == DurationType.getAllType().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getMillisType().hashCode());
        assertEquals("AllType", type.getName());
        assertEquals("DurationType[AllType]", type.toString());
        assertSameAfterSerialization(type);
        assertSame(type, type.withChronology(null));
        assertSame(type, type.withChronology(ISOChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC(), type.withChronology(CopticChronology.getInstanceUTC()).getChronology());
    }

    //-----------------------------------------------------------------------
    public void testAllType_Chronology() throws Exception {
        DurationType type = DurationType.getAllType(BuddhistChronology.getInstanceUTC());
        assertEquals(true, type.years().isSupported());
        assertEquals(true, type.months().isSupported());
        assertEquals(true, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(false, type.isPrecise());
        assertEquals(BuddhistChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, DurationType.getAllType() == DurationType.getAllType(null));
        assertEquals(true, DurationType.getAllType() == DurationType.getAllType(ISOChronology.getInstanceUTC()));
        assertEquals(true, type.equals(DurationType.getAllType(BuddhistChronology.getInstanceUTC())));
        assertEquals(false, type.equals(DurationType.getAllType()));
        assertEquals(false, type.equals(DurationType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(false, type.hashCode() == DurationType.getAllType().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getMillisType().hashCode());
        assertEquals("AllType", type.getName());
        assertEquals("DurationType[AllType]", type.toString());
        assertEqualsAfterSerialization(type);
        assertSame(DurationType.getAllType(), type.withChronology(null));
        assertSame(DurationType.getAllType(), type.withChronology(ISOChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC(), type.withChronology(CopticChronology.getInstanceUTC()).getChronology());
    }

    //-----------------------------------------------------------------------
    public void testAverageYearMonthType() throws Exception {
        DurationType type = DurationType.getAverageYearMonthType();
        assertEquals(true, type.years().isSupported());
        assertEquals(true, type.months().isSupported());
        assertEquals(false, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(true, type.isPrecise());
        assertEquals(ISOChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type == DurationType.getAverageYearMonthType());
        assertEquals(false, type.equals(DurationType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == DurationType.getAverageYearMonthType().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getAllType().hashCode());
        assertEquals("AverageYearMonthType", type.getName());
        assertEquals("DurationType[AverageYearMonthType]", type.toString());
        assertSameAfterSerialization(type);
        assertSame(type, type.withChronology(null));
        assertSame(type, type.withChronology(ISOChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC(), type.withChronology(CopticChronology.getInstanceUTC()).getChronology());
    }

    //-----------------------------------------------------------------------
    public void testAverageYearMonthType_Chronology() throws Exception {
        DurationType type = DurationType.getAverageYearMonthType(BuddhistChronology.getInstanceUTC());
        assertEquals(true, type.years().isSupported());
        assertEquals(true, type.months().isSupported());
        assertEquals(false, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(true, type.isPrecise());
        assertEquals(BuddhistChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, DurationType.getAverageYearMonthType() == DurationType.getAverageYearMonthType(null));
        assertEquals(true, DurationType.getAverageYearMonthType() == DurationType.getAverageYearMonthType(ISOChronology.getInstanceUTC()));
        assertEquals(true, type.equals(DurationType.getAverageYearMonthType(BuddhistChronology.getInstanceUTC())));
        assertEquals(false, type.equals(DurationType.getAverageYearMonthType()));
        assertEquals(false, type.equals(DurationType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(false, type.hashCode() == DurationType.getAverageYearMonthType().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getAllType().hashCode());
        assertEquals("AverageYearMonthType", type.getName());
        assertEquals("DurationType[AverageYearMonthType]", type.toString());
        assertEqualsAfterSerialization(type);
        assertSame(DurationType.getAverageYearMonthType(), type.withChronology(null));
        assertSame(DurationType.getAverageYearMonthType(), type.withChronology(ISOChronology.getInstanceUTC()));
        assertEquals(CopticChronology.getInstanceUTC(), type.withChronology(CopticChronology.getInstanceUTC()).getChronology());
    }

    //-----------------------------------------------------------------------
    public void testPreciseYearWeekType() throws Exception {
        DurationType type = DurationType.getPreciseYearWeekType();
        assertEquals(true, type.years().isSupported());
        assertEquals(false, type.months().isSupported());
        assertEquals(true, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(true, type.isPrecise());
        assertEquals(ISOChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type == DurationType.getPreciseYearWeekType());
        assertEquals(false, type.equals(DurationType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == DurationType.getPreciseYearWeekType().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getAllType().hashCode());
        assertEquals("PreciseYearWeekType", type.getName());
        assertEquals("DurationType[PreciseYearWeekType]", type.toString());
        assertSameAfterSerialization(type);
        assertSame(type, type.withChronology(null));
        assertSame(type, type.withChronology(ISOChronology.getInstanceUTC()));
        assertSame(type, type.withChronology(CopticChronology.getInstanceUTC()));
    }

    //-----------------------------------------------------------------------
    public void testPreciseYearMonthType() throws Exception {
        DurationType type = DurationType.getPreciseYearMonthType();
        assertEquals(true, type.years().isSupported());
        assertEquals(true, type.months().isSupported());
        assertEquals(false, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(true, type.isPrecise());
        assertEquals(ISOChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type == DurationType.getPreciseYearMonthType());
        assertEquals(false, type.equals(DurationType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == DurationType.getPreciseYearMonthType().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getAllType().hashCode());
        assertEquals("PreciseYearMonthType", type.getName());
        assertEquals("DurationType[PreciseYearMonthType]", type.toString());
        assertSameAfterSerialization(type);
        assertSame(type, type.withChronology(null));
        assertSame(type, type.withChronology(ISOChronology.getInstanceUTC()));
        assertSame(type, type.withChronology(CopticChronology.getInstanceUTC()));
    }

    //-----------------------------------------------------------------------
    public void testPreciseAllType() throws Exception {
        DurationType type = DurationType.getPreciseAllType();
        assertEquals(true, type.years().isSupported());
        assertEquals(true, type.months().isSupported());
        assertEquals(true, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(true, type.isPrecise());
        assertEquals(ISOChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type == DurationType.getPreciseAllType());
        assertEquals(false, type.equals(DurationType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == DurationType.getPreciseAllType().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getMillisType().hashCode());
        assertEquals("PreciseAllType", type.getName());
        assertEquals("DurationType[PreciseAllType]", type.toString());
        assertSameAfterSerialization(type);
        assertSame(type, type.withChronology(null));
        assertSame(type, type.withChronology(ISOChronology.getInstanceUTC()));
        assertSame(type, type.withChronology(CopticChronology.getInstanceUTC()));
    }

    //-----------------------------------------------------------------------
    public void testMaskYears() throws Exception {
        DurationType type = DurationType.getPreciseAllType().withYearsRemoved();
        assertEquals(false, type.years().isSupported());
        assertEquals(true, type.months().isSupported());
        assertEquals(true, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(true, type.isPrecise());
        assertEquals(ISOChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type.equals(DurationType.getPreciseAllType().withYearsRemoved()));
        assertEquals(false, type.equals(DurationType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == DurationType.getPreciseAllType().withYearsRemoved().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getMillisType().hashCode());
        assertEquals("MaskedYears[PreciseAllType]", type.getName());
        assertEquals("DurationType[MaskedYears[PreciseAllType]]", type.toString());
        assertEqualsAfterSerialization(type);
    }

    //-----------------------------------------------------------------------
    public void testMaskMonths() throws Exception {
        DurationType type = DurationType.getPreciseAllType().withMonthsRemoved();
        assertEquals(true, type.years().isSupported());
        assertEquals(false, type.months().isSupported());
        assertEquals(true, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(true, type.isPrecise());
        assertEquals(ISOChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type.equals(DurationType.getPreciseAllType().withMonthsRemoved()));
        assertEquals(false, type.equals(DurationType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == DurationType.getPreciseAllType().withMonthsRemoved().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getMillisType().hashCode());
        assertEquals("MaskedMonths[PreciseAllType]", type.getName());
        assertEquals("DurationType[MaskedMonths[PreciseAllType]]", type.toString());
        assertEqualsAfterSerialization(type);
    }

    //-----------------------------------------------------------------------
    public void testMaskWeeks() throws Exception {
        DurationType type = DurationType.getPreciseAllType().withWeeksRemoved();
        assertEquals(true, type.years().isSupported());
        assertEquals(true, type.months().isSupported());
        assertEquals(false, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(true, type.isPrecise());
        assertEquals(ISOChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type.equals(DurationType.getPreciseAllType().withWeeksRemoved()));
        assertEquals(false, type.equals(DurationType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == DurationType.getPreciseAllType().withWeeksRemoved().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getMillisType().hashCode());
        assertEquals("MaskedWeeks[PreciseAllType]", type.getName());
        assertEquals("DurationType[MaskedWeeks[PreciseAllType]]", type.toString());
        assertEqualsAfterSerialization(type);
    }

    //-----------------------------------------------------------------------
    public void testMaskDays() throws Exception {
        DurationType type = DurationType.getPreciseAllType().withDaysRemoved();
        assertEquals(true, type.years().isSupported());
        assertEquals(true, type.months().isSupported());
        assertEquals(true, type.weeks().isSupported());
        assertEquals(false, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(true, type.isPrecise());
        assertEquals(ISOChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type.equals(DurationType.getPreciseAllType().withDaysRemoved()));
        assertEquals(false, type.equals(DurationType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == DurationType.getPreciseAllType().withDaysRemoved().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getMillisType().hashCode());
        assertEquals("MaskedDays[PreciseAllType]", type.getName());
        assertEquals("DurationType[MaskedDays[PreciseAllType]]", type.toString());
        assertEqualsAfterSerialization(type);
    }

    //-----------------------------------------------------------------------
    public void testMaskHours() throws Exception {
        DurationType type = DurationType.getPreciseAllType().withHoursRemoved();
        assertEquals(true, type.years().isSupported());
        assertEquals(true, type.months().isSupported());
        assertEquals(true, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(false, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(true, type.isPrecise());
        assertEquals(ISOChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type.equals(DurationType.getPreciseAllType().withHoursRemoved()));
        assertEquals(false, type.equals(DurationType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == DurationType.getPreciseAllType().withHoursRemoved().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getMillisType().hashCode());
        assertEquals("MaskedHours[PreciseAllType]", type.getName());
        assertEquals("DurationType[MaskedHours[PreciseAllType]]", type.toString());
        assertEqualsAfterSerialization(type);
    }

    //-----------------------------------------------------------------------
    public void testMaskMinutes() throws Exception {
        DurationType type = DurationType.getPreciseAllType().withMinutesRemoved();
        assertEquals(true, type.years().isSupported());
        assertEquals(true, type.months().isSupported());
        assertEquals(true, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(false, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(true, type.isPrecise());
        assertEquals(ISOChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type.equals(DurationType.getPreciseAllType().withMinutesRemoved()));
        assertEquals(false, type.equals(DurationType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == DurationType.getPreciseAllType().withMinutesRemoved().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getMillisType().hashCode());
        assertEquals("MaskedMinutes[PreciseAllType]", type.getName());
        assertEquals("DurationType[MaskedMinutes[PreciseAllType]]", type.toString());
        assertEqualsAfterSerialization(type);
    }

    //-----------------------------------------------------------------------
    public void testMaskSeconds() throws Exception {
        DurationType type = DurationType.getPreciseAllType().withSecondsRemoved();
        assertEquals(true, type.years().isSupported());
        assertEquals(true, type.months().isSupported());
        assertEquals(true, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(false, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(true, type.isPrecise());
        assertEquals(ISOChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type.equals(DurationType.getPreciseAllType().withSecondsRemoved()));
        assertEquals(false, type.equals(DurationType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == DurationType.getPreciseAllType().withSecondsRemoved().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getMillisType().hashCode());
        assertEquals("MaskedSeconds[PreciseAllType]", type.getName());
        assertEquals("DurationType[MaskedSeconds[PreciseAllType]]", type.toString());
        assertEqualsAfterSerialization(type);
    }

    //-----------------------------------------------------------------------
    public void testMaskMillis() throws Exception {
        DurationType type = DurationType.getPreciseAllType().withMillisRemoved();
        assertEquals(true, type.years().isSupported());
        assertEquals(true, type.months().isSupported());
        assertEquals(true, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(false, type.millis().isSupported());
        assertEquals(true, type.isPrecise());
        assertEquals(ISOChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type.equals(DurationType.getPreciseAllType().withMillisRemoved()));
        assertEquals(false, type.equals(DurationType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == DurationType.getPreciseAllType().withMillisRemoved().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getMillisType().hashCode());
        assertEquals("MaskedMillis[PreciseAllType]", type.getName());
        assertEquals("DurationType[MaskedMillis[PreciseAllType]]", type.toString());
        assertEqualsAfterSerialization(type);
    }

    //-----------------------------------------------------------------------
    public void testMaskHoursMinutesSeconds() throws Exception {
        DurationType type = DurationType.getPreciseAllType().withHoursRemoved().withMinutesRemoved().withSecondsRemoved();
        assertEquals(true, type.years().isSupported());
        assertEquals(true, type.months().isSupported());
        assertEquals(true, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(false, type.hours().isSupported());
        assertEquals(false, type.minutes().isSupported());
        assertEquals(false, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(true, type.isPrecise());
        assertEquals(ISOChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type.equals(DurationType.getPreciseAllType().withHoursRemoved().withMinutesRemoved().withSecondsRemoved()));
        assertEquals(false, type.equals(DurationType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == DurationType.getPreciseAllType().withHoursRemoved().withMinutesRemoved().withSecondsRemoved().hashCode());
        assertEquals(false, type.hashCode() == DurationType.getMillisType().hashCode());
        assertEquals("MaskedHoursMinutesSeconds[PreciseAllType]", type.getName());
        assertEquals("DurationType[MaskedHoursMinutesSeconds[PreciseAllType]]", type.toString());
        assertEqualsAfterSerialization(type);
    }

    //-----------------------------------------------------------------------
    public void testMaskWithChronology() throws Exception {
        DurationType type = DurationType.getAllType().withYearsRemoved();
        assertEquals(type, type.withChronology(null));
        assertEquals(type, type.withChronology(ISOChronology.getInstanceUTC()));
        
        DurationType type2 = type.withChronology(CopticChronology.getInstanceUTC());
        assertEquals(CopticChronology.getInstanceUTC(), type2.getChronology());
        assertEquals(false, type2.years().isSupported());
        assertEquals(true, type2.months().isSupported());
        assertEquals(true, type2.weeks().isSupported());
        assertEquals(true, type2.days().isSupported());
        assertEquals(true, type2.hours().isSupported());
        assertEquals(true, type2.minutes().isSupported());
        assertEquals(true, type2.seconds().isSupported());
        assertEquals(true, type2.millis().isSupported());
    }

    //-----------------------------------------------------------------------
    public void testMaskTwice1() throws Exception {
        DurationType type = DurationType.getPreciseAllType().withYearsRemoved();
        DurationType type2 = type.withYearsRemoved();
        assertEquals(true, type == type2);
        
        type = DurationType.getPreciseAllType().withMonthsRemoved();
        type2 = type.withMonthsRemoved();
        assertEquals(true, type == type2);
        
        type = DurationType.getPreciseAllType().withWeeksRemoved();
        type2 = type.withWeeksRemoved();
        assertEquals(true, type == type2);
        
        type = DurationType.getPreciseAllType().withDaysRemoved();
        type2 = type.withDaysRemoved();
        assertEquals(true, type == type2);
        
        type = DurationType.getPreciseAllType().withHoursRemoved();
        type2 = type.withHoursRemoved();
        assertEquals(true, type == type2);
        
        type = DurationType.getPreciseAllType().withMinutesRemoved();
        type2 = type.withMinutesRemoved();
        assertEquals(true, type == type2);
        
        type = DurationType.getPreciseAllType().withSecondsRemoved();
        type2 = type.withSecondsRemoved();
        assertEquals(true, type == type2);
        
        type = DurationType.getPreciseAllType().withMillisRemoved();
        type2 = type.withMillisRemoved();
        assertEquals(true, type == type2);
    }

    //-----------------------------------------------------------------------
    public void testMaskTwice2() throws Exception {
        DurationType type = DurationType.getDayHourType();
        DurationType type2 = type.withYearsRemoved();
        assertEquals(true, type == type2);
        
        type = DurationType.getDayHourType();
        type2 = type.withMonthsRemoved();
        assertEquals(true, type == type2);
        
        type = DurationType.getDayHourType();
        type2 = type.withWeeksRemoved();
        assertEquals(true, type == type2);
        
        type = DurationType.getMillisType();
        type2 = type.withDaysRemoved();
        assertEquals(true, type == type2);
        
        type = DurationType.getMillisType();
        type2 = type.withHoursRemoved();
        assertEquals(true, type == type2);
        
        type = DurationType.getMillisType();
        type2 = type.withMinutesRemoved();
        assertEquals(true, type == type2);
        
        type = DurationType.getMillisType();
        type2 = type.withSecondsRemoved();
        assertEquals(true, type == type2);
    }

    //-----------------------------------------------------------------------
    public void testMaskNullName() throws Exception {
        DurationType type = new MockDurationTypeWithFields().withYearsRemoved();
        assertEquals("MaskedYears[]", type.getName());
        assertEquals("DurationType[MaskedYears[]]", type.toString());
    }        

    //-----------------------------------------------------------------------
    public void testAbstract() throws Exception {
        assertEquals(true, Modifier.isPublic(DurationType.class.getModifiers()));
        assertEquals(true, Modifier.isAbstract(DurationType.class.getModifiers()));
        
        DurationType type = new MockDurationType();
        assertEquals(false, type.years().isSupported());
        assertEquals(false, type.months().isSupported());
        assertEquals(false, type.weeks().isSupported());
        assertEquals(false, type.days().isSupported());
        assertEquals(false, type.hours().isSupported());
        assertEquals(false, type.minutes().isSupported());
        assertEquals(false, type.seconds().isSupported());
        assertEquals(false, type.millis().isSupported());
        assertEquals(false, type.isPrecise());
        assertEquals(null, type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type.equals(new MockDurationType()));
        assertEquals(false, type.equals(DurationType.getAllType()));
        assertEquals(false, type.equals(null));
        assertEquals(false, type.equals("six"));
        assertEquals(null, type.getName());
        assertEquals("DurationType[]", type.toString());
    }

    static class MockDurationType extends DurationType {
        public String getName() {
            return null;
        }
        public Chronology getChronology() {
            return null;
        }
        public DurationType withChronology(Chronology chrono) {
            return null;
        }
        public boolean isPrecise() {
            return false;
        }
    }

    static class MockDurationTypeWithFields extends DurationType {
        public String getName() {
            return null;
        }
        public Chronology getChronology() {
            return null;
        }
        public DurationField years() {
            return ISOChronology.getInstanceUTC().years();
        }
        public DurationField months() {
            return ISOChronology.getInstanceUTC().months();
        }
        public DurationType withChronology(Chronology chrono) {
            return null;
        }
        public boolean isPrecise() {
            return false;
        }
    }

}
