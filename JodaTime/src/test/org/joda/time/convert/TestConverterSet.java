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
package org.joda.time.convert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.GregorianCalendar;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.ReadWritableDateTime;
import org.joda.time.ReadWritableInstant;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadableInstant;

/**
 * This class is a JUnit test for ConverterSet.
 * Mostly for coverage.
 *
 * @author Stephen Colebourne
 */
public class TestConverterSet extends TestCase {

    private static final Converter c1 = new Converter() {
        public Class getSupportedType() {return Boolean.class;}
    };
    private static final Converter c2 = new Converter() {
        public Class getSupportedType() {return Character.class;}
    };
    private static final Converter c3 = new Converter() {
        public Class getSupportedType() {return Byte.class;}
    };
    private static final Converter c4 = new Converter() {
        public Class getSupportedType() {return Short.class;}
    };
    private static final Converter c4a = new Converter() {
        public Class getSupportedType() {return Short.class;}
    };
    private static final Converter c5 = new Converter() {
        public Class getSupportedType() {return Integer.class;}
    };
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestConverterSet.class);
    }

    public TestConverterSet(String name) {
        super(name);
    }

    //-----------------------------------------------------------------------
    public void testClass() throws Exception {
        Class cls = ConverterSet.class;
        assertEquals(false, Modifier.isPublic(cls.getModifiers()));
        assertEquals(false, Modifier.isProtected(cls.getModifiers()));
        assertEquals(false, Modifier.isPrivate(cls.getModifiers()));
        
        assertEquals(1, cls.getDeclaredConstructors().length);
        Constructor con = cls.getDeclaredConstructors()[0];
        assertEquals(false, Modifier.isPublic(con.getModifiers()));
        assertEquals(false, Modifier.isProtected(con.getModifiers()));
        assertEquals(false, Modifier.isPrivate(con.getModifiers()));
    }

    //-----------------------------------------------------------------------
    public void testBigHashtable() {
        Converter[] array = new Converter[] {
            c1, c2, c3, c4,
        };
        ConverterSet set = new ConverterSet(array);
        set.select(Boolean.class);
        set.select(Character.class);
        set.select(Byte.class);
        set.select(Short.class);
        set.select(Integer.class);
        set.select(Long.class);
        set.select(Float.class);
        set.select(Double.class);
        set.select(null);
        set.select(Calendar.class);
        set.select(GregorianCalendar.class);
        set.select(DateTime.class);
        set.select(DateMidnight.class);
        set.select(ReadableInstant.class);
        set.select(ReadableDateTime.class);
        set.select(ReadWritableInstant.class);  // 16
        set.select(ReadWritableDateTime.class);
        set.select(DateTime.class);
        assertEquals(4, set.size());
    }

    //-----------------------------------------------------------------------
    public void testAddNullRemoved1() {
        Converter[] array = new Converter[] {
            c1, c2, c3, c4,
        };
        ConverterSet set = new ConverterSet(array);
        ConverterSet result = set.add(c5, null);
        assertEquals(4, set.size());
        assertEquals(5, result.size());
    }

    public void testAddNullRemoved2() {
        Converter[] array = new Converter[] {
            c1, c2, c3, c4,
        };
        ConverterSet set = new ConverterSet(array);
        ConverterSet result = set.add(c4, null);
        assertSame(set, result);
    }

    public void testAddNullRemoved3() {
        Converter[] array = new Converter[] {
            c1, c2, c3, c4,
        };
        ConverterSet set = new ConverterSet(array);
        ConverterSet result = set.add(c4a, null);
        assertTrue(set != result);
        assertEquals(4, set.size());
        assertEquals(4, result.size());
    }

    //-----------------------------------------------------------------------
    public void testRemoveNullRemoved1() {
        Converter[] array = new Converter[] {
            c1, c2, c3, c4,
        };
        ConverterSet set = new ConverterSet(array);
        ConverterSet result = set.remove(c3, null);
        assertEquals(4, set.size());
        assertEquals(3, result.size());
    }

    public void testRemoveNullRemoved2() {
        Converter[] array = new Converter[] {
            c1, c2, c3, c4,
        };
        ConverterSet set = new ConverterSet(array);
        ConverterSet result = set.remove(c5, null);
        assertSame(set, result);
    }

    //-----------------------------------------------------------------------
    public void testRemoveBadIndex1() {
        Converter[] array = new Converter[] {
            c1, c2, c3, c4,
        };
        ConverterSet set = new ConverterSet(array);
        try {
            set.remove(200, null);
            fail();
        } catch (IndexOutOfBoundsException ex) {}
        assertEquals(4, set.size());
    }

    public void testRemoveBadIndex2() {
        Converter[] array = new Converter[] {
            c1, c2, c3, c4,
        };
        ConverterSet set = new ConverterSet(array);
        try {
            set.remove(-1, null);
            fail();
        } catch (IndexOutOfBoundsException ex) {}
        assertEquals(4, set.size());
    }

}
