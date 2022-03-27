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

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A basic mock testing class for an unknown time zone.
 *
 * @author Stephen Colebourne
 */
class MockUnknownTimeZone extends TimeZone {
    
    MockUnknownTimeZone() {
        super();
    }
    
    @Override
    public String getID() {
        return "!!!";
    }
    @Override
    public String getDisplayName(boolean daylight, int style, Locale locale) {
        return "!!!";
    }

    @Override
    public int getOffset(int era, int year, int month, int day, int dayOfWeek, int milliseconds) {
        return 0;
    }
    @Override
    public void setRawOffset(int offsetMillis) {
    }
    @Override
    public int getRawOffset() {
        return 0;
    }
    @Override
    public boolean useDaylightTime() {
        return false;
    }
    @Override
    public boolean inDaylightTime(Date date) {
        return false;
    }
    
}
