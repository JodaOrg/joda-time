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
 * This class is a JUnit test for PeriodType.
 *
 * @author Stephen Colebourne
 */
public class TestPeriodType extends TestCase {
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
        return new TestSuite(TestPeriodType.class);
    }

    public TestPeriodType(String name) {
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
    private void assertEqualsAfterSerialization(PeriodType type) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(type);
        byte[] bytes = baos.toByteArray();
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        PeriodType result = (PeriodType) ois.readObject();
        ois.close();
        
        assertEquals(type, result);
    }

    private void assertSameAfterSerialization(PeriodType type) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(type);
        byte[] bytes = baos.toByteArray();
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        PeriodType result = (PeriodType) ois.readObject();
        ois.close();
        
        assertSame(type, result);
    }

    //-----------------------------------------------------------------------
    public void testMillisType() throws Exception {
        PeriodType type = PeriodType.getMillisType();
        assertEquals(false, type.years().isSupported());
        assertEquals(false, type.months().isSupported());
        assertEquals(false, type.weeks().isSupported());
        assertEquals(false, type.days().isSupported());
        assertEquals(false, type.hours().isSupported());
        assertEquals(false, type.minutes().isSupported());
        assertEquals(false, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(true, type.isPrecise());
        assertEquals(ISOChronology.getInstanceUTC(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type == PeriodType.getMillisType());
        assertEquals(false, type.equals(PeriodType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == PeriodType.getMillisType().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getAllType().hashCode());
        assertEquals("MillisType", type.getName());
        assertEquals("PeriodType[MillisType]", type.toString());
        assertSameAfterSerialization(type);
        assertSame(type, type.withChronology(null));
        assertSame(type, type.withChronology(CopticChronology.getInstanceUTC()));
    }

    //-----------------------------------------------------------------------
    public void testDayHourType() throws Exception {
        PeriodType type = PeriodType.getDayHourType();
        assertEquals(false, type.years().isSupported());
        assertEquals(false, type.months().isSupported());
        assertEquals(false, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(false, type.isPrecise());
        assertEquals(ISOChronology.getInstance(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type == PeriodType.getDayHourType());
        assertEquals(false, type.equals(PeriodType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == PeriodType.getDayHourType().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getAllType().hashCode());
        assertEquals("DayHourType", type.getName());
        assertEquals("PeriodType[DayHourType]", type.toString());
        assertSameAfterSerialization(type);
        assertSame(type, type.withChronology(null));
        assertSame(type, type.withChronology(ISOChronology.getInstance()));
        assertEquals(CopticChronology.getInstanceUTC(), type.withChronology(CopticChronology.getInstanceUTC()).getChronology());
    }

    //-----------------------------------------------------------------------
    public void testDayHourType_Chronology() throws Exception {
        PeriodType type = PeriodType.getDayHourType(BuddhistChronology.getInstanceUTC());
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
        assertEquals(true, PeriodType.getDayHourType() == PeriodType.getDayHourType(null));
        assertEquals(true, PeriodType.getDayHourType() == PeriodType.getDayHourType(ISOChronology.getInstance()));
        assertEquals(true, type.equals(PeriodType.getDayHourType(BuddhistChronology.getInstanceUTC())));
        assertEquals(false, type.equals(PeriodType.getDayHourType()));
        assertEquals(false, type.equals(PeriodType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getDayHourType().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getAllType().hashCode());
        assertEquals("DayHourType", type.getName());
        assertEquals("PeriodType[DayHourType]", type.toString());
        assertEqualsAfterSerialization(type);
        assertSame(PeriodType.getDayHourType(), type.withChronology(null));
        assertSame(PeriodType.getDayHourType(), type.withChronology(ISOChronology.getInstance()));
        assertEquals(CopticChronology.getInstanceUTC(), type.withChronology(CopticChronology.getInstanceUTC()).getChronology());
    }

    //-----------------------------------------------------------------------
    public void testYearWeekType() throws Exception {
        PeriodType type = PeriodType.getYearWeekType();
        assertEquals(true, type.years().isSupported());
        assertEquals(false, type.months().isSupported());
        assertEquals(true, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(false, type.isPrecise());
        assertEquals(ISOChronology.getInstance(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type == PeriodType.getYearWeekType());
        assertEquals(false, type.equals(PeriodType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == PeriodType.getYearWeekType().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getAllType().hashCode());
        assertEquals("YearWeekType", type.getName());
        assertEquals("PeriodType[YearWeekType]", type.toString());
        assertSameAfterSerialization(type);
        assertSame(type, type.withChronology(null));
        assertSame(type, type.withChronology(ISOChronology.getInstance()));
        assertEquals(CopticChronology.getInstanceUTC(), type.withChronology(CopticChronology.getInstanceUTC()).getChronology());
    }

    //-----------------------------------------------------------------------
    public void testYearWeekType_Chronology() throws Exception {
        PeriodType type = PeriodType.getYearWeekType(BuddhistChronology.getInstanceUTC());
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
        assertEquals(true, PeriodType.getYearWeekType() == PeriodType.getYearWeekType(null));
        assertEquals(true, PeriodType.getYearWeekType() == PeriodType.getYearWeekType(ISOChronology.getInstance()));
        assertEquals(true, type.equals(PeriodType.getYearWeekType(BuddhistChronology.getInstanceUTC())));
        assertEquals(false, type.equals(PeriodType.getYearWeekType()));
        assertEquals(false, type.equals(PeriodType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getYearWeekType().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getAllType().hashCode());
        assertEquals("YearWeekType", type.getName());
        assertEquals("PeriodType[YearWeekType]", type.toString());
        assertEqualsAfterSerialization(type);
        assertSame(PeriodType.getYearWeekType(), type.withChronology(null));
        assertSame(PeriodType.getYearWeekType(), type.withChronology(ISOChronology.getInstance()));
        assertEquals(CopticChronology.getInstanceUTC(), type.withChronology(CopticChronology.getInstanceUTC()).getChronology());
    }

    //-----------------------------------------------------------------------
    public void testYearMonthType() throws Exception {
        PeriodType type = PeriodType.getYearMonthType();
        assertEquals(true, type.years().isSupported());
        assertEquals(true, type.months().isSupported());
        assertEquals(false, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(false, type.isPrecise());
        assertEquals(ISOChronology.getInstance(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type == PeriodType.getYearMonthType());
        assertEquals(false, type.equals(PeriodType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == PeriodType.getYearMonthType().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getAllType().hashCode());
        assertEquals("YearMonthType", type.getName());
        assertEquals("PeriodType[YearMonthType]", type.toString());
        assertSameAfterSerialization(type);
        assertSame(type, type.withChronology(null));
        assertSame(type, type.withChronology(ISOChronology.getInstance()));
        assertEquals(CopticChronology.getInstanceUTC(), type.withChronology(CopticChronology.getInstanceUTC()).getChronology());
    }

    //-----------------------------------------------------------------------
    public void testYearMonthType_Chronology() throws Exception {
        PeriodType type = PeriodType.getYearMonthType(BuddhistChronology.getInstanceUTC());
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
        assertEquals(true, PeriodType.getYearMonthType() == PeriodType.getYearMonthType(null));
        assertEquals(true, PeriodType.getYearMonthType() == PeriodType.getYearMonthType(ISOChronology.getInstance()));
        assertEquals(true, type.equals(PeriodType.getYearMonthType(BuddhistChronology.getInstanceUTC())));
        assertEquals(false, type.equals(PeriodType.getYearMonthType()));
        assertEquals(false, type.equals(PeriodType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getYearMonthType().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getAllType().hashCode());
        assertEquals("YearMonthType", type.getName());
        assertEquals("PeriodType[YearMonthType]", type.toString());
        assertEqualsAfterSerialization(type);
        assertSame(PeriodType.getYearMonthType(), type.withChronology(null));
        assertSame(PeriodType.getYearMonthType(), type.withChronology(ISOChronology.getInstance()));
        assertEquals(CopticChronology.getInstanceUTC(), type.withChronology(CopticChronology.getInstanceUTC()).getChronology());
    }

    //-----------------------------------------------------------------------
    public void testAllType() throws Exception {
        PeriodType type = PeriodType.getAllType();
        assertEquals(true, type.years().isSupported());
        assertEquals(true, type.months().isSupported());
        assertEquals(true, type.weeks().isSupported());
        assertEquals(true, type.days().isSupported());
        assertEquals(true, type.hours().isSupported());
        assertEquals(true, type.minutes().isSupported());
        assertEquals(true, type.seconds().isSupported());
        assertEquals(true, type.millis().isSupported());
        assertEquals(false, type.isPrecise());
        assertEquals(ISOChronology.getInstance(), type.getChronology());
        assertEquals(true, type.equals(type));
        assertEquals(true, type == PeriodType.getAllType());
        assertEquals(false, type.equals(PeriodType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == PeriodType.getAllType().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getMillisType().hashCode());
        assertEquals("AllType", type.getName());
        assertEquals("PeriodType[AllType]", type.toString());
        assertSameAfterSerialization(type);
        assertSame(type, type.withChronology(null));
        assertSame(type, type.withChronology(ISOChronology.getInstance()));
        assertEquals(CopticChronology.getInstanceUTC(), type.withChronology(CopticChronology.getInstanceUTC()).getChronology());
    }

    //-----------------------------------------------------------------------
    public void testAllType_Chronology() throws Exception {
        PeriodType type = PeriodType.getAllType(BuddhistChronology.getInstanceUTC());
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
        assertEquals(true, PeriodType.getAllType() == PeriodType.getAllType(null));
        assertEquals(true, PeriodType.getAllType() == PeriodType.getAllType(ISOChronology.getInstance()));
        assertEquals(true, type.equals(PeriodType.getAllType(BuddhistChronology.getInstanceUTC())));
        assertEquals(false, type.equals(PeriodType.getAllType()));
        assertEquals(false, type.equals(PeriodType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getAllType().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getMillisType().hashCode());
        assertEquals("AllType", type.getName());
        assertEquals("PeriodType[AllType]", type.toString());
        assertEqualsAfterSerialization(type);
        assertSame(PeriodType.getAllType(), type.withChronology(null));
        assertSame(PeriodType.getAllType(), type.withChronology(ISOChronology.getInstance()));
        assertEquals(CopticChronology.getInstanceUTC(), type.withChronology(CopticChronology.getInstanceUTC()).getChronology());
    }

    //-----------------------------------------------------------------------
    public void testPreciseDayHourType() throws Exception {
        PeriodType type = PeriodType.getPreciseDayHourType();
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
        assertEquals(true, type == PeriodType.getPreciseDayHourType());
        assertEquals(false, type.equals(PeriodType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == PeriodType.getPreciseDayHourType().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getAllType().hashCode());
        assertEquals("PreciseDayHourType", type.getName());
        assertEquals("PeriodType[PreciseDayHourType]", type.toString());
        assertSameAfterSerialization(type);
        assertSame(type, type.withChronology(null));
        assertSame(type, type.withChronology(ISOChronology.getInstanceUTC()));
        assertSame(type, type.withChronology(CopticChronology.getInstanceUTC()));
    }

    //-----------------------------------------------------------------------
    public void testPreciseYearDayType() throws Exception {
        PeriodType type = PeriodType.getPreciseYearDayType();
        assertEquals(true, type.years().isSupported());
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
        assertEquals(true, type == PeriodType.getPreciseYearDayType());
        assertEquals(false, type.equals(PeriodType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == PeriodType.getPreciseYearDayType().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getAllType().hashCode());
        assertEquals("PreciseYearDayType", type.getName());
        assertEquals("PeriodType[PreciseYearDayType]", type.toString());
        assertSameAfterSerialization(type);
        assertSame(type, type.withChronology(null));
        assertSame(type, type.withChronology(ISOChronology.getInstanceUTC()));
        assertSame(type, type.withChronology(CopticChronology.getInstanceUTC()));
    }

    //-----------------------------------------------------------------------
    public void testPreciseYearWeekType() throws Exception {
        PeriodType type = PeriodType.getPreciseYearWeekType();
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
        assertEquals(true, type == PeriodType.getPreciseYearWeekType());
        assertEquals(false, type.equals(PeriodType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == PeriodType.getPreciseYearWeekType().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getAllType().hashCode());
        assertEquals("PreciseYearWeekType", type.getName());
        assertEquals("PeriodType[PreciseYearWeekType]", type.toString());
        assertSameAfterSerialization(type);
        assertSame(type, type.withChronology(null));
        assertSame(type, type.withChronology(ISOChronology.getInstanceUTC()));
        assertSame(type, type.withChronology(CopticChronology.getInstanceUTC()));
    }

    //-----------------------------------------------------------------------
    public void testPreciseYearMonthType() throws Exception {
        PeriodType type = PeriodType.getPreciseYearMonthType();
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
        assertEquals(true, type == PeriodType.getPreciseYearMonthType());
        assertEquals(false, type.equals(PeriodType.getAllType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == PeriodType.getPreciseYearMonthType().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getAllType().hashCode());
        assertEquals("PreciseYearMonthType", type.getName());
        assertEquals("PeriodType[PreciseYearMonthType]", type.toString());
        assertSameAfterSerialization(type);
        assertSame(type, type.withChronology(null));
        assertSame(type, type.withChronology(ISOChronology.getInstanceUTC()));
        assertSame(type, type.withChronology(CopticChronology.getInstanceUTC()));
    }

    //-----------------------------------------------------------------------
    public void testPreciseAllType() throws Exception {
        PeriodType type = PeriodType.getPreciseAllType();
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
        assertEquals(true, type == PeriodType.getPreciseAllType());
        assertEquals(false, type.equals(PeriodType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == PeriodType.getPreciseAllType().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getMillisType().hashCode());
        assertEquals("PreciseAllType", type.getName());
        assertEquals("PeriodType[PreciseAllType]", type.toString());
        assertSameAfterSerialization(type);
        assertSame(type, type.withChronology(null));
        assertSame(type, type.withChronology(ISOChronology.getInstanceUTC()));
        assertSame(type, type.withChronology(CopticChronology.getInstanceUTC()));
    }

    //-----------------------------------------------------------------------
    public void testMaskYears() throws Exception {
        PeriodType type = PeriodType.getPreciseAllType().withYearsRemoved();
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
        assertEquals(true, type.equals(PeriodType.getPreciseAllType().withYearsRemoved()));
        assertEquals(false, type.equals(PeriodType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == PeriodType.getPreciseAllType().withYearsRemoved().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getMillisType().hashCode());
        assertEquals("MaskedYears[PreciseAllType]", type.getName());
        assertEquals("PeriodType[MaskedYears[PreciseAllType]]", type.toString());
        assertEqualsAfterSerialization(type);
    }

    //-----------------------------------------------------------------------
    public void testMaskMonths() throws Exception {
        PeriodType type = PeriodType.getPreciseAllType().withMonthsRemoved();
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
        assertEquals(true, type.equals(PeriodType.getPreciseAllType().withMonthsRemoved()));
        assertEquals(false, type.equals(PeriodType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == PeriodType.getPreciseAllType().withMonthsRemoved().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getMillisType().hashCode());
        assertEquals("MaskedMonths[PreciseAllType]", type.getName());
        assertEquals("PeriodType[MaskedMonths[PreciseAllType]]", type.toString());
        assertEqualsAfterSerialization(type);
    }

    //-----------------------------------------------------------------------
    public void testMaskWeeks() throws Exception {
        PeriodType type = PeriodType.getPreciseAllType().withWeeksRemoved();
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
        assertEquals(true, type.equals(PeriodType.getPreciseAllType().withWeeksRemoved()));
        assertEquals(false, type.equals(PeriodType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == PeriodType.getPreciseAllType().withWeeksRemoved().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getMillisType().hashCode());
        assertEquals("MaskedWeeks[PreciseAllType]", type.getName());
        assertEquals("PeriodType[MaskedWeeks[PreciseAllType]]", type.toString());
        assertEqualsAfterSerialization(type);
    }

    //-----------------------------------------------------------------------
    public void testMaskDays() throws Exception {
        PeriodType type = PeriodType.getPreciseAllType().withDaysRemoved();
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
        assertEquals(true, type.equals(PeriodType.getPreciseAllType().withDaysRemoved()));
        assertEquals(false, type.equals(PeriodType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == PeriodType.getPreciseAllType().withDaysRemoved().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getMillisType().hashCode());
        assertEquals("MaskedDays[PreciseAllType]", type.getName());
        assertEquals("PeriodType[MaskedDays[PreciseAllType]]", type.toString());
        assertEqualsAfterSerialization(type);
    }

    //-----------------------------------------------------------------------
    public void testMaskHours() throws Exception {
        PeriodType type = PeriodType.getPreciseAllType().withHoursRemoved();
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
        assertEquals(true, type.equals(PeriodType.getPreciseAllType().withHoursRemoved()));
        assertEquals(false, type.equals(PeriodType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == PeriodType.getPreciseAllType().withHoursRemoved().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getMillisType().hashCode());
        assertEquals("MaskedHours[PreciseAllType]", type.getName());
        assertEquals("PeriodType[MaskedHours[PreciseAllType]]", type.toString());
        assertEqualsAfterSerialization(type);
    }

    //-----------------------------------------------------------------------
    public void testMaskMinutes() throws Exception {
        PeriodType type = PeriodType.getPreciseAllType().withMinutesRemoved();
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
        assertEquals(true, type.equals(PeriodType.getPreciseAllType().withMinutesRemoved()));
        assertEquals(false, type.equals(PeriodType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == PeriodType.getPreciseAllType().withMinutesRemoved().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getMillisType().hashCode());
        assertEquals("MaskedMinutes[PreciseAllType]", type.getName());
        assertEquals("PeriodType[MaskedMinutes[PreciseAllType]]", type.toString());
        assertEqualsAfterSerialization(type);
    }

    //-----------------------------------------------------------------------
    public void testMaskSeconds() throws Exception {
        PeriodType type = PeriodType.getPreciseAllType().withSecondsRemoved();
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
        assertEquals(true, type.equals(PeriodType.getPreciseAllType().withSecondsRemoved()));
        assertEquals(false, type.equals(PeriodType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == PeriodType.getPreciseAllType().withSecondsRemoved().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getMillisType().hashCode());
        assertEquals("MaskedSeconds[PreciseAllType]", type.getName());
        assertEquals("PeriodType[MaskedSeconds[PreciseAllType]]", type.toString());
        assertEqualsAfterSerialization(type);
    }

    //-----------------------------------------------------------------------
    public void testMaskMillis() throws Exception {
        PeriodType type = PeriodType.getPreciseAllType().withMillisRemoved();
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
        assertEquals(true, type.equals(PeriodType.getPreciseAllType().withMillisRemoved()));
        assertEquals(false, type.equals(PeriodType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == PeriodType.getPreciseAllType().withMillisRemoved().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getMillisType().hashCode());
        assertEquals("MaskedMillis[PreciseAllType]", type.getName());
        assertEquals("PeriodType[MaskedMillis[PreciseAllType]]", type.toString());
        assertEqualsAfterSerialization(type);
    }

    //-----------------------------------------------------------------------
    public void testMaskHoursMinutesSeconds() throws Exception {
        PeriodType type = PeriodType.getPreciseAllType().withHoursRemoved().withMinutesRemoved().withSecondsRemoved();
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
        assertEquals(true, type.equals(PeriodType.getPreciseAllType().withHoursRemoved().withMinutesRemoved().withSecondsRemoved()));
        assertEquals(false, type.equals(PeriodType.getMillisType()));
        assertEquals(true, type.hashCode() == type.hashCode());
        assertEquals(true, type.hashCode() == PeriodType.getPreciseAllType().withHoursRemoved().withMinutesRemoved().withSecondsRemoved().hashCode());
        assertEquals(false, type.hashCode() == PeriodType.getMillisType().hashCode());
        assertEquals("MaskedHoursMinutesSeconds[PreciseAllType]", type.getName());
        assertEquals("PeriodType[MaskedHoursMinutesSeconds[PreciseAllType]]", type.toString());
        assertEqualsAfterSerialization(type);
    }

    //-----------------------------------------------------------------------
    public void testMaskWithChronology() throws Exception {
        PeriodType type = PeriodType.getAllType().withYearsRemoved();
        assertEquals(type, type.withChronology(null));
        assertEquals(type, type.withChronology(ISOChronology.getInstance()));
        
        PeriodType type2 = type.withChronology(CopticChronology.getInstanceUTC());
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
        PeriodType type = PeriodType.getPreciseAllType().withYearsRemoved();
        PeriodType type2 = type.withYearsRemoved();
        assertEquals(true, type == type2);
        
        type = PeriodType.getPreciseAllType().withMonthsRemoved();
        type2 = type.withMonthsRemoved();
        assertEquals(true, type == type2);
        
        type = PeriodType.getPreciseAllType().withWeeksRemoved();
        type2 = type.withWeeksRemoved();
        assertEquals(true, type == type2);
        
        type = PeriodType.getPreciseAllType().withDaysRemoved();
        type2 = type.withDaysRemoved();
        assertEquals(true, type == type2);
        
        type = PeriodType.getPreciseAllType().withHoursRemoved();
        type2 = type.withHoursRemoved();
        assertEquals(true, type == type2);
        
        type = PeriodType.getPreciseAllType().withMinutesRemoved();
        type2 = type.withMinutesRemoved();
        assertEquals(true, type == type2);
        
        type = PeriodType.getPreciseAllType().withSecondsRemoved();
        type2 = type.withSecondsRemoved();
        assertEquals(true, type == type2);
        
        type = PeriodType.getPreciseAllType().withMillisRemoved();
        type2 = type.withMillisRemoved();
        assertEquals(true, type == type2);
    }

    //-----------------------------------------------------------------------
    public void testMaskTwice2() throws Exception {
        PeriodType type = PeriodType.getDayHourType();
        PeriodType type2 = type.withYearsRemoved();
        assertEquals(true, type == type2);
        
        type = PeriodType.getDayHourType();
        type2 = type.withMonthsRemoved();
        assertEquals(true, type == type2);
        
        type = PeriodType.getDayHourType();
        type2 = type.withWeeksRemoved();
        assertEquals(true, type == type2);
        
        type = PeriodType.getMillisType();
        type2 = type.withDaysRemoved();
        assertEquals(true, type == type2);
        
        type = PeriodType.getMillisType();
        type2 = type.withHoursRemoved();
        assertEquals(true, type == type2);
        
        type = PeriodType.getMillisType();
        type2 = type.withMinutesRemoved();
        assertEquals(true, type == type2);
        
        type = PeriodType.getMillisType();
        type2 = type.withSecondsRemoved();
        assertEquals(true, type == type2);
    }

    //-----------------------------------------------------------------------
    public void testMaskNullName() throws Exception {
        PeriodType type = new MockPeriodTypeWithFields().withYearsRemoved();
        assertEquals("MaskedYears[]", type.getName());
        assertEquals("PeriodType[MaskedYears[]]", type.toString());
    }        

    //-----------------------------------------------------------------------
    public void testAbstract() throws Exception {
        assertEquals(true, Modifier.isPublic(PeriodType.class.getModifiers()));
        assertEquals(true, Modifier.isAbstract(PeriodType.class.getModifiers()));
        
        PeriodType type = new MockPeriodType();
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
        assertEquals(true, type.equals(new MockPeriodType()));
        assertEquals(false, type.equals(PeriodType.getAllType()));
        assertEquals(false, type.equals(null));
        assertEquals(false, type.equals("six"));
        assertEquals(null, type.getName());
        assertEquals("PeriodType[]", type.toString());
    }

    static class MockPeriodType extends PeriodType {
        public String getName() {
            return null;
        }
        public Chronology getChronology() {
            return null;
        }
        public PeriodType withChronology(Chronology chrono) {
            return null;
        }
        public boolean isPrecise() {
            return false;
        }
    }

    static class MockPeriodTypeWithFields extends PeriodType {
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
        public PeriodType withChronology(Chronology chrono) {
            return null;
        }
        public boolean isPrecise() {
            return false;
        }
    }

}
