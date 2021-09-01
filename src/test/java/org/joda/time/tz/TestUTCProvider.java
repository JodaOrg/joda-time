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
package org.joda.time.tz;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Set;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTimeZone;

/**
 * This class is a JUnit test for UTCProvider.
 *
 * @author Stephen Colebourne
 */
public class TestUTCProvider extends TestCase {

    private DateTimeZone zone = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestUTCProvider.class);
    }

    public TestUTCProvider(String name) {
        super(name);
    }

    //-----------------------------------------------------------------------
    public void testClass() throws Exception {
        Class cls = UTCProvider.class;
        assertEquals(true, Modifier.isPublic(cls.getModifiers()));
        
        Constructor con = cls.getDeclaredConstructor((Class[]) null);
        assertEquals(1, cls.getDeclaredConstructors().length);
        assertEquals(true, Modifier.isPublic(con.getModifiers()));
    }

    //-----------------------------------------------------------------------
    public void testGetAvailableIDs() throws Exception {
        Provider p = new UTCProvider();
        Set set = p.getAvailableIDs();
        assertEquals(1, set.size());
        assertEquals("UTC", set.iterator().next());
    }

    //-----------------------------------------------------------------------
    public void testGetZone_String() throws Exception {
        Provider p = new UTCProvider();
        assertSame(DateTimeZone.UTC, p.getZone("UTC"));
        assertEquals(null, p.getZone(null));
        assertEquals(null, p.getZone("Europe/London"));
        assertEquals(null, p.getZone("Blah"));
    }

}
