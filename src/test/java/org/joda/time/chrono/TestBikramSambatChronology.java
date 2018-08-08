/*
 * Copyright 2018 Nafundi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.joda.time.chrono;

import junit.framework.TestSuite;
import org.joda.time.Chronology;
import org.joda.time.DateTime;

import static junit.framework.Assert.assertEquals;

public class TestBikramSambatChronology {

    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();
    private static final Chronology BIKRAM_SAMBAT_UTC = BikramSambatChronology.getInstanceUTC();

    // Test results confirmed using online converters:
    // http://nepalicalendar.rat32.com/index.php?dowhat=nepali-calendar-horoscope-game-download-unicode-new-year-mobile-calendar&view=851ddf5058cf22df63d3344ad89919cf#date-conversion
    // http://dateconverter.appspot.com/
    public void testSampleDate() {
        DateTime dt = new DateTime(1943, 4, 14, 0, 0, 0, 0, ISO_UTC).withChronology(BIKRAM_SAMBAT_UTC);
        assertEquals(new DateTime(2000, 1, 1, 0, 0, 0, 0, BIKRAM_SAMBAT_UTC), dt);

        dt = new DateTime(1959, 11, 10, 0, 0, 0, 0, ISO_UTC).withChronology(BIKRAM_SAMBAT_UTC);
        assertEquals(new DateTime(2016, 7, 24, 0, 0, 0, 0, BIKRAM_SAMBAT_UTC), dt);

        dt = new DateTime(1967, 5, 7, 0, 0, 0, 0, ISO_UTC).withChronology(BIKRAM_SAMBAT_UTC);
        assertEquals(new DateTime(2024, 1, 24, 0, 0, 0, 0, BIKRAM_SAMBAT_UTC), dt);

        dt = new DateTime(1970, 7, 23, 0, 0, 0, 0, ISO_UTC).withChronology(BIKRAM_SAMBAT_UTC);
        assertEquals(new DateTime(2027, 4, 8, 0, 0, 0, 0, BIKRAM_SAMBAT_UTC), dt);

        dt = new DateTime(1982, 1, 25, 0, 0, 0, 0, ISO_UTC).withChronology(BIKRAM_SAMBAT_UTC);
        assertEquals(new DateTime(2038, 10, 12, 0, 0, 0, 0, BIKRAM_SAMBAT_UTC), dt);

        dt = new DateTime(1991, 9, 30, 0, 0, 0, 0, ISO_UTC).withChronology(BIKRAM_SAMBAT_UTC);
        assertEquals(new DateTime(2048, 6, 14, 0, 0, 0, 0, BIKRAM_SAMBAT_UTC), dt);

        dt = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(BIKRAM_SAMBAT_UTC);
        assertEquals(new DateTime(2061, 2, 27, 0, 0, 0, 0, BIKRAM_SAMBAT_UTC), dt);

        dt = new DateTime(2015, 1, 7, 0, 0, 0, 0, ISO_UTC).withChronology(BIKRAM_SAMBAT_UTC);
        assertEquals(new DateTime(2071, 9, 23, 0, 0, 0, 0, BIKRAM_SAMBAT_UTC), dt);

        dt = new DateTime(2021, 12, 17, 0, 0, 0, 0, ISO_UTC).withChronology(BIKRAM_SAMBAT_UTC);
        assertEquals(new DateTime(2078, 9, 2, 0, 0, 0, 0, BIKRAM_SAMBAT_UTC), dt);

        dt = new DateTime(2033, 4, 14, 0, 0, 0, 0, ISO_UTC).withChronology(BIKRAM_SAMBAT_UTC);
        assertEquals(new DateTime(2090, 1, 1, 0, 0, 0, 0, BIKRAM_SAMBAT_UTC), dt);
    }

    public static TestSuite suite() {
        return new TestSuite(TestBikramSambatChronology.class);
    }
}
