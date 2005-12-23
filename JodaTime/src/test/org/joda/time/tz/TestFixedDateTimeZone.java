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

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTimeZone;

/**
 * Test cases for FixedDateTimeZone.
 *
 * @author Stephen Colebourne
 */
public class TestFixedDateTimeZone extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestFixedDateTimeZone.class);
    }

    private DateTimeZone originalDateTimeZone = null;

    public TestFixedDateTimeZone(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        originalDateTimeZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    protected void tearDown() throws Exception {
        DateTimeZone.setDefault(originalDateTimeZone);
    }

    public void testEquals() throws Exception {
        FixedDateTimeZone zone1 = new FixedDateTimeZone("A", "B", 1, 5);
        FixedDateTimeZone zone1b = new FixedDateTimeZone("A", "B", 1, 5);
        FixedDateTimeZone zone2 = new FixedDateTimeZone("A", "C", 1, 5);
        FixedDateTimeZone zone3 = new FixedDateTimeZone("A", "B", 2, 5);
        FixedDateTimeZone zone4 = new FixedDateTimeZone("A", "B", 1, 6);
        
        assertEquals(true, zone1.equals(zone1));
        assertEquals(true, zone1.equals(zone1b));
        assertEquals(true, zone1.equals(zone2));  // second arg ignored
        assertEquals(false, zone1.equals(zone3));
        assertEquals(false, zone1.equals(zone4));
    }

    public void testHashCode() throws Exception {
        FixedDateTimeZone zone1 = new FixedDateTimeZone("A", "B", 1, 5);
        FixedDateTimeZone zone1b = new FixedDateTimeZone("A", "B", 1, 5);
        FixedDateTimeZone zone2 = new FixedDateTimeZone("A", "C", 1, 5);
        FixedDateTimeZone zone3 = new FixedDateTimeZone("A", "B", 2, 5);
        FixedDateTimeZone zone4 = new FixedDateTimeZone("A", "B", 1, 6);
        
        assertEquals(true, zone1.hashCode() == zone1.hashCode());
        assertEquals(true, zone1.hashCode() == zone1b.hashCode());
        assertEquals(true, zone1.hashCode() == zone2.hashCode());  // second arg ignored
        assertEquals(false, zone1.hashCode() == zone3.hashCode());
        assertEquals(false, zone1.hashCode() == zone4.hashCode());
    }

}
