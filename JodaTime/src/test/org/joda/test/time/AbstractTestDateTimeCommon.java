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
package org.joda.test.time;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.ReadableInstant;
import org.joda.time.chrono.gj.GJChronology;
import org.joda.time.chrono.iso.ISOChronology;

/**
 * This class is a Junit unit test base class for
 * DateOnly, TimeOnly, DateTime, and MutableDateTime implementations.
 * <p>This class handles testing of CTORs for the named classes.
 *
 * @author Stephen Colebourne
 * @author Guy Allard
 */
public abstract class AbstractTestDateTimeCommon
       extends AbstractTestAbstractInstant {

    // The class to be tested.
    private Class cls;
    //
    private static final DateTimeZone ZONE = DateTimeZone.getInstance("+02:00");

    /**
     * AbstractTestDateTimeCommon constructor.
     * A constructor with this signature
     * is required for mapping the system inheritance tree to the
     * test class inheritance tree.
     * @param name The human readable name of the class.
     * @param cls A reference to the Class being tested.
     */
    public AbstractTestDateTimeCommon(String name, Class cls) {
        super(name, cls);
        this.cls = cls;
    }

    /**
     * Junit <code>setUp()</code> method.
     */
    protected void setUp() /* throws Exception */ {
        super.setUp();
    }

    /**
     * Junit <code>tearDown()</code> method.
     */
    protected void tearDown() /* throws Exception */ {
        super.tearDown();
    }
    
    //----------------------------------------------------------------------------
    
    /**
     * Test the CTOR with signature: (Chronology).
     */
    public void testCurrentTimeChronologyConstructor() throws Throwable {
        long currentMillis = System.currentTimeMillis();
        ReadableInstant ri = create(cls, 
            new Class[] {Chronology.class}, 
            new Object[] {GJChronology.getInstance(ZONE)});
        currentMillis = round(currentMillis, ZONE.toTimeZone());
        assertTrue(ri.getMillis() - currentMillis < 1000);
        assertEquals(GJChronology.getInstance(ZONE), ri.getChronology());
    }

    /**
     * Test the CTOR with signature: (Chronology), null chronology.
     */
    public void testCurrentTimeChronologyNullConstructor() throws Throwable {
        long currentMillis = System.currentTimeMillis();
        ReadableInstant ri = create(cls, new Class[] {Chronology.class}, new Object[] {null});
        currentMillis = round(currentMillis, DateTimeZone.getDefault().toTimeZone());
        assertTrue(ri.getMillis() - currentMillis < 20);
        assertEquals(ISOChronology.getInstance(DateTimeZone.getDefault()), ri.getChronology());
    }

    //----------------------------------------------------------------------------
    
    /**
     * Test the CTOR with signature: (long, Chronology).
     */
    public void testMillisChronologyConstructor() throws Throwable {
        long currentMillis = System.currentTimeMillis() + 1000;
        ReadableInstant ri = create(cls, 
            new Class[] {Long.TYPE, Chronology.class}, 
            new Object[] {new Long(currentMillis), GJChronology.getInstance(ZONE)});
        currentMillis = round(currentMillis, ZONE.toTimeZone());
        assertEquals(new Instant(currentMillis), new Instant(ri.getMillis()));
        assertEquals(GJChronology.getInstance(ZONE), ri.getChronology());
    }

    /**
     * Test the CTOR with signature: (long, Chronology), null chronology.
     */
    public void testMillisChronologyNullConstructor() throws Throwable {
        long currentMillis = System.currentTimeMillis() + 1000;
        ReadableInstant ri = create(cls, 
            new Class[] {Long.TYPE, Chronology.class}, 
            new Object[] {new Long(currentMillis), null});
        currentMillis = round(currentMillis, DateTimeZone.getDefault().toTimeZone());
        assertEquals(new Instant(currentMillis), new Instant(ri.getMillis()));
        assertEquals(ISOChronology.getInstance(DateTimeZone.getDefault()), ri.getChronology());
    }

    /**
     * Test the CTOR with signature: (long, Chronology). Fixed time 1
     */
    public void testMillisChronologyConstructorTime1() throws Throwable {
        long currentMillis = 20L * 24 * 60 * 60 * 1000 + 5L * 60 * 60 * 1000; // 20 days 5hrs
        ReadableInstant ri = create(cls, 
            new Class[] {Long.TYPE, Chronology.class}, 
            new Object[] {new Long(currentMillis), GJChronology.getInstance(ZONE)});
        currentMillis = round(currentMillis, ZONE.toTimeZone());
        assertEquals(new Instant(currentMillis), new Instant(ri.getMillis()));
        assertEquals(GJChronology.getInstance(ZONE), ri.getChronology());
    }

    /**
     * Test the CTOR with signature: (long, Chronology). Fixed time 2
     */
    public void testMillisChronologyConstructorTime2() throws Throwable {
        long currentMillis = 20L * 24 * 60 * 60 * 1000 + 22L * 60 * 60 * 1000; // 20 days 22hrs
        ReadableInstant ri = create(cls, 
            new Class[] {Long.TYPE, Chronology.class}, 
            new Object[] {new Long(currentMillis), GJChronology.getInstance(ZONE)});
        currentMillis = round(currentMillis, ZONE.toTimeZone());
        assertEquals(new Instant(currentMillis), new Instant(ri.getMillis()));
        assertEquals(GJChronology.getInstance(ZONE), ri.getChronology());
    }

    //----------------------------------------------------------------------------
    
    /**
     * Test the CTOR with signature: (ReadableInstant, Chronology).
     */
    public void testReadableInstantChronologyConstructor() throws Throwable {
        ReadableInstant instant = create(Instant.class, null, null);
        ReadableInstant ri = create(cls, 
            new Class[] {ReadableInstant.class, Chronology.class}, 
            new Object[] {instant, GJChronology.getInstance(ZONE)});
        long currentMillis = round(instant.getMillis(), ZONE.toTimeZone());
        assertEquals(new Instant(currentMillis), new Instant(ri.getMillis()));
        assertEquals(GJChronology.getInstance(ZONE), ri.getChronology());
    }

    /**
     * Test the CTOR with signature: (ReadableInstant, Chronology), null Chronology.
     */
    public void testReadableInstantChronologyNullConstructor() throws Throwable {
        ReadableInstant instant = create(Instant.class, null, null);
        ReadableInstant ri = create(cls, 
            new Class[] {ReadableInstant.class, Chronology.class}, 
            new Object[] {instant, null});
        long currentMillis = round(instant.getMillis(), DateTimeZone.getDefault().toTimeZone());
        assertEquals(new Instant(currentMillis), new Instant(ri.getMillis()));
        assertEquals(ISOChronology.getInstance(DateTimeZone.getDefault()), ri.getChronology());
    }

    /**
     * Test the CTOR with signature: (ReadableInstant, Chronology), null RI.
     */
    public void testReadableInstantNullChronologyConstructor() throws Throwable {
        try {
            create(cls, 
                new Class[] {ReadableInstant.class, Chronology.class}, 
                new Object[] {null, GJChronology.getInstanceUTC()});
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    /**
     * Test the CTOR with signature: (ReadableInstant, Chronology), null RI, chronology.
     */
    public void testReadableInstantNullChronologyNullConstructor() throws Throwable {
        try {
            create(cls, 
                new Class[] {ReadableInstant.class, Chronology.class}, 
                new Object[] {null, null});
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //----------------------------------------------------------------------------
    
    /**
     * Test the CTOR with signature: (Date, Chronology).
     */
    public void testDateChronologyConstructor() throws Throwable {
        Date date = new Date();
        ReadableInstant ri = create(cls, 
            new Class[] {Date.class, Chronology.class}, 
            new Object[] {date, GJChronology.getInstance(ZONE)});
        long currentMillis = round(date.getTime(), ZONE.toTimeZone());
        assertEquals(new Instant(currentMillis), new Instant(ri.getMillis()));
        assertEquals(GJChronology.getInstance(ZONE), ri.getChronology());
    }

    /**
     * Test the CTOR with signature: (Date, Chronology), null Chronology.
     */
    public void testDateChronologyNullConstructor() throws Throwable {
        Date date = new Date();
        ReadableInstant ri = create(cls, 
            new Class[] {Date.class, Chronology.class}, 
            new Object[] {date, null});
        long currentMillis = round(date.getTime(), DateTimeZone.getDefault().toTimeZone());
        assertEquals(new Instant(currentMillis), new Instant(ri.getMillis()));
        assertEquals(ISOChronology.getInstance(DateTimeZone.getDefault()), ri.getChronology());
    }

    /**
     * Test the CTOR with signature: (Date, Chronology), null RI.
     */
    public void testDateNullChronologyConstructor() throws Throwable {
        try {
            create(cls, 
                new Class[] {Date.class, Chronology.class}, 
                new Object[] {null, GJChronology.getInstanceUTC()});
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    /**
     * Test the CTOR with signature: (Date, Chronology), null RI, chronology.
     */
    public void testDateNullChronologyNullConstructor() throws Throwable {
        try {
            create(cls, 
                new Class[] {Date.class, Chronology.class}, 
                new Object[] {null, null});
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //----------------------------------------------------------------------------
    
    /**
     * Test the CTOR with signature: (Calendar, Chronology).
     */
    public void testCalendarChronologyConstructor() throws Throwable {
        Calendar calendar = Calendar.getInstance();
        ReadableInstant ri = create(cls, 
            new Class[] {Calendar.class, Chronology.class}, 
            new Object[] {calendar, GJChronology.getInstance(ZONE)});
        long currentMillis = round(calendar.getTime().getTime(), ZONE.toTimeZone());
        assertEquals(new Instant(currentMillis), new Instant(ri.getMillis()));
        assertEquals(GJChronology.getInstance(ZONE), ri.getChronology());
    }

    /**
     * Test the CTOR with signature: (Calendar, Chronology), null Chronology.
     */
    public void testCalendarChronologyNullConstructor() throws Throwable {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+02:00"));
        ReadableInstant ri = create(cls, 
            new Class[] {Calendar.class, Chronology.class}, 
            new Object[] {calendar, null});
        long currentMillis = round(calendar.getTime().getTime(), DateTimeZone.getInstance("+02:00").toTimeZone());
        assertEquals(new Instant(currentMillis), new Instant(ri.getMillis()));
        assertEquals(GJChronology.getInstance(ZONE), ri.getChronology());
    }

    /**
     * Test the CTOR with signature: (Calendar, Chronology), null RI.
     */
    public void testCalendarNullChronologyConstructor() throws Throwable {
        try {
            create(cls, 
                new Class[] {Calendar.class, Chronology.class}, 
                new Object[] {null, GJChronology.getInstanceUTC()});
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    /**
     * Test the CTOR with signature: (Calendar, Chronology), null RI, chronology.
     */
    public void testCalendarNullChronologyNullConstructor() throws Throwable {
        try {
            create(cls, 
                new Class[] {Calendar.class, Chronology.class}, 
                new Object[] {null, null});
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //----------------------------------------------------------------------------
    
    /**
     * Test the CTOR with signature: (String, Chronology).
     */
    public void testStringChronologyConstructor() throws Throwable {
        String str = "1970-01-01T13:02:03.004Z";
        long millis = 4 + 3*1000 + 2*1000*60 + 13*1000*60*60;
        if (isDateOnly()) {
            str = "1970-01-02TZ";
            millis = 1 * 24 * 60 * 60 * 1000;
        } else if (isTimeOnly()) {
            str = "T13:02:03.004Z";
        }
        ReadableInstant ri = create(cls, 
            new Class[] {String.class, Chronology.class}, 
            new Object[] {str, GJChronology.getInstance(ZONE)});
        assertEquals(new Instant(millis), new Instant(ri.getMillis()));
        assertEquals(GJChronology.getInstance(ZONE), ri.getChronology());
    }

    /**
     * Test the CTOR with signature: (String, Chronology), null Chronology.
     */
    public void testStringChronologyNullConstructor() throws Throwable {
        String str = "1970-01-01T13:02:03.004Z";
        long millis = 4 + 3*1000 + 2*1000*60 + 13*1000*60*60;
        if (isDateOnly()) {
            str = "1970-01-02TZ";
            millis = 1 * 24 * 60 * 60 * 1000;
        } else if (isTimeOnly()) {
            str = "T13:02:03.004Z";
        }
        ReadableInstant ri = create(cls, 
            new Class[] {String.class, Chronology.class}, 
            new Object[] {str, null});
        assertEquals(new Instant(millis), new Instant(ri.getMillis()));
        assertEquals(ISOChronology.getInstance(), ri.getChronology());
    }

    /**
     * Test the CTOR with signature: (String, Chronology), null RI.
     */
    public void testStringNullChronologyConstructor() throws Throwable {
        try {
            create(cls, 
                new Class[] {String.class, Chronology.class}, 
                new Object[] {null, GJChronology.getInstanceUTC()});
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    /**
     * Test the CTOR with signature: (String, Chronology), null RI, chronology.
     */
    public void testStringNullChronologyNullConstructor() throws Throwable {
        try {
            create(cls, 
                new Class[] {String.class, Chronology.class}, 
                new Object[] {null, null});
            fail();
        } catch (IllegalArgumentException ex) {}
    }

}
