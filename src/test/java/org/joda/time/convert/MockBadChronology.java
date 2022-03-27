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
package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.BaseChronology;

/**
 * A basic mock testing class for an unknown calendar.
 *
 * @author Stephen Colebourne
 */
class MockBadChronology extends BaseChronology {
    
    MockBadChronology() {
        super();
    }

    @Override
    public Chronology withZone(DateTimeZone zone) {
        return null;
    }
    
    @Override
    public DateTimeZone getZone() {
        return null;
    }
    @Override
    public Chronology withUTC() {
        return null;
    }
    @Override
    public String toString() {
        return null;
    }
    
}
