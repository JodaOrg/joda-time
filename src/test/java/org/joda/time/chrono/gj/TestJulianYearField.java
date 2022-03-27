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
package org.joda.time.chrono.gj;

import org.joda.time.field.FieldUtils;

/**
 * 
 * @author Brian S O'Neill
 */
class TestJulianYearField extends TestGJYearField {
    public TestJulianYearField(TestJulianChronology chrono) {
        super(chrono);
    }

    @Override
    public long addWrapField(long millis, int value) {
        int year = get(millis);
        int wrapped = FieldUtils.getWrappedValue
            (year, value, getMinimumValue(), getMaximumValue());
        return add(millis, (long) wrapped - year);
    }

    @Override
    public long add(long millis, long value) {
        int year = get(millis);
        int newYear = year + FieldUtils.safeToInt(value);
        if (year < 0) {
            if (newYear >= 0) {
                newYear++;
            }
        } else {
            if (newYear <= 0) {
                newYear--;
            }
        }
        return set(millis, newYear);
    }

    @Override
    public int getMinimumValue() {
        return -100000000;
    }

    @Override
    public int getMaximumValue() {
        return 100000000;
    }
}
