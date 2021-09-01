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
class TestJulianWeekyearField extends TestGJWeekyearField {
    public TestJulianWeekyearField(TestJulianChronology chrono) {
        super(chrono);
    }

    public long addWrapField(long millis, int value) {
        int weekyear = get(millis);
        int wrapped = FieldUtils.getWrappedValue
            (weekyear, value, getMinimumValue(), getMaximumValue());
        return add(millis, (long) wrapped - weekyear);
    }

    public long add(long millis, long value) {
        int weekyear = get(millis);
        int newWeekyear = weekyear + FieldUtils.safeToInt(value);
        if (weekyear < 0) {
            if (newWeekyear >= 0) {
                newWeekyear++;
            }
        } else {
            if (newWeekyear <= 0) {
                newWeekyear--;
            }
        }
        return set(millis, newWeekyear);
    }

    public int getMinimumValue() {
        return -100000000;
    }

    public int getMaximumValue() {
        return 100000000;
    }
}
