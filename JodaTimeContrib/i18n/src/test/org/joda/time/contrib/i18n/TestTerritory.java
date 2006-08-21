/*
 *  Copyright 2006 Stephen Colebourne
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
package org.joda.time.contrib.i18n;

import junit.framework.TestCase;

/**
 * Test Territory.
 */
public class TestTerritory extends TestCase {

    //-----------------------------------------------------------------------
    public void testGB() {
        Territory t = Territory.forID("GB");
        assertEquals("GB", t.getID());
        assertEquals(1, t.getZones().length);
        assertEquals("Europe/London", t.getZones()[0].getID());
        assertEquals("Europe/London", t.getZone().getID());
        assertEquals(1, t.getFirstDayOfWeek());
        assertEquals(1, t.getBusinessWeekStart());
        assertEquals(5, t.getBusinessWeekEnd());
        assertEquals(6, t.getWeekendStart());
        assertEquals(7, t.getWeekendEnd());
    }

}
