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

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This class is a Junit unit test for DurationFieldType.
 *
 * @author Stephen Colebourne
 */
public class TestDurationFieldType extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestDurationFieldType.class);
    }

    public TestDurationFieldType(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    //-----------------------------------------------------------------------
    public void test_eras() throws Exception {
        assertEquals(DurationFieldType.eras(), DurationFieldType.eras());
        assertEquals("eras", DurationFieldType.eras().getName());
        assertEquals(Chronology.getCopticUTC().eras(), DurationFieldType.eras().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().eras().isSupported(), DurationFieldType.eras().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DurationFieldType.eras());
    }

    public void test_centuries() throws Exception {
        assertEquals(DurationFieldType.centuries(), DurationFieldType.centuries());
        assertEquals("centuries", DurationFieldType.centuries().getName());
        assertEquals(Chronology.getCopticUTC().centuries(), DurationFieldType.centuries().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().centuries().isSupported(), DurationFieldType.centuries().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DurationFieldType.centuries());
    }

    public void test_years() throws Exception {
        assertEquals(DurationFieldType.years(), DurationFieldType.years());
        assertEquals("years", DurationFieldType.years().getName());
        assertEquals(Chronology.getCopticUTC().years(), DurationFieldType.years().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().years().isSupported(), DurationFieldType.years().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DurationFieldType.years());
    }

    public void test_months() throws Exception {
        assertEquals(DurationFieldType.months(), DurationFieldType.months());
        assertEquals("months", DurationFieldType.months().getName());
        assertEquals(Chronology.getCopticUTC().months(), DurationFieldType.months().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().months().isSupported(), DurationFieldType.months().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DurationFieldType.months());
    }

    public void test_weekyears() throws Exception {
        assertEquals(DurationFieldType.weekyears(), DurationFieldType.weekyears());
        assertEquals("weekyears", DurationFieldType.weekyears().getName());
        assertEquals(Chronology.getCopticUTC().weekyears(), DurationFieldType.weekyears().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().weekyears().isSupported(), DurationFieldType.weekyears().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DurationFieldType.weekyears());
    }

    public void test_weeks() throws Exception {
        assertEquals(DurationFieldType.weeks(), DurationFieldType.weeks());
        assertEquals("weeks", DurationFieldType.weeks().getName());
        assertEquals(Chronology.getCopticUTC().weeks(), DurationFieldType.weeks().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().weeks().isSupported(), DurationFieldType.weeks().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DurationFieldType.weeks());
    }

    public void test_days() throws Exception {
        assertEquals(DurationFieldType.days(), DurationFieldType.days());
        assertEquals("days", DurationFieldType.days().getName());
        assertEquals(Chronology.getCopticUTC().days(), DurationFieldType.days().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().days().isSupported(), DurationFieldType.days().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DurationFieldType.days());
    }

    public void test_halfdays() throws Exception {
        assertEquals(DurationFieldType.halfdays(), DurationFieldType.halfdays());
        assertEquals("halfdays", DurationFieldType.halfdays().getName());
        assertEquals(Chronology.getCopticUTC().halfdays(), DurationFieldType.halfdays().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().halfdays().isSupported(), DurationFieldType.halfdays().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DurationFieldType.halfdays());
    }

    public void test_hours() throws Exception {
        assertEquals(DurationFieldType.hours(), DurationFieldType.hours());
        assertEquals("hours", DurationFieldType.hours().getName());
        assertEquals(Chronology.getCopticUTC().hours(), DurationFieldType.hours().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().hours().isSupported(), DurationFieldType.hours().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DurationFieldType.hours());
    }

    public void test_minutes() throws Exception {
        assertEquals(DurationFieldType.minutes(), DurationFieldType.minutes());
        assertEquals("minutes", DurationFieldType.minutes().getName());
        assertEquals(Chronology.getCopticUTC().minutes(), DurationFieldType.minutes().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().minutes().isSupported(), DurationFieldType.minutes().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DurationFieldType.minutes());
    }

    public void test_seconds() throws Exception {
        assertEquals(DurationFieldType.seconds(), DurationFieldType.seconds());
        assertEquals("seconds", DurationFieldType.seconds().getName());
        assertEquals(Chronology.getCopticUTC().seconds(), DurationFieldType.seconds().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().seconds().isSupported(), DurationFieldType.seconds().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DurationFieldType.seconds());
    }

    public void test_millis() throws Exception {
        assertEquals(DurationFieldType.millis(), DurationFieldType.millis());
        assertEquals("millis", DurationFieldType.millis().getName());
        assertEquals(Chronology.getCopticUTC().millis(), DurationFieldType.millis().getField(Chronology.getCopticUTC()));
        assertEquals(Chronology.getCopticUTC().millis().isSupported(), DurationFieldType.millis().isSupported(Chronology.getCopticUTC()));
        assertSerialization(DurationFieldType.millis());
    }

    //-----------------------------------------------------------------------
    public void assertSerialization(DurationFieldType type) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(type);
        byte[] bytes = baos.toByteArray();
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        DurationFieldType result = (DurationFieldType) ois.readObject();
        ois.close();
        
        assertSame(type, result);
    }

}
