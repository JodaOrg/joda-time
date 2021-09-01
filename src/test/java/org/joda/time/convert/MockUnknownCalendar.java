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

import java.util.Calendar;
import java.util.TimeZone;

/**
 * A basic mock testing class for an unknown calendar.
 *
 * @author Stephen Colebourne
 */
class MockUnknownCalendar extends Calendar {
    
    private long millis;
    private TimeZone zone;
    
    MockUnknownCalendar(long millis) {
        this.millis = millis;
    }
    MockUnknownCalendar(TimeZone zone) {
        this.zone = zone;
    }
    
    public long getTimeInMillis() {
        return millis;
    }
    public TimeZone getTimeZone() {
        return zone;
    }

    protected void computeTime() {
    }
    protected void computeFields() {
    }
    public void add(int field, int amount) {
    }
    public void roll(int field, boolean up) {
    }
    public int getMinimum(int field) {
        return 0;
    }
    public int getMaximum(int field) {
        return 0;
    }
    public int getGreatestMinimum(int field) {
        return 0;
    }
    public int getLeastMaximum(int field) {
        return 0;
    }

}
