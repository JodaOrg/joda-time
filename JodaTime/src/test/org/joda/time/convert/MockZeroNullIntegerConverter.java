/*
 *  Copyright 2001-2006 Stephen Colebourne
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
package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;

/**
 * A basic mock testing class for a converter.
 * This converter returns zero and null for an Integer input.
 *
 * @author Stephen Colebourne
 */
public class MockZeroNullIntegerConverter implements InstantConverter {
    
    public static final InstantConverter INSTANCE = new MockZeroNullIntegerConverter();

    public long getInstantMillis(Object object, Chronology chrono) {
        return 0;
    }

    public long getInstantMillis(Object object, Chronology chrono, DateTimeFormatter parser) {
        return 0;
    }

    public Chronology getChronology(Object object, DateTimeZone zone) {
        return null;
    }

    public Chronology getChronology(Object object, Chronology chrono) {
        return null;
    }

    public Class getSupportedType() {
        return Integer.class;
    }
}
