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

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;

/**
 * 
 * @author Brian S O'Neill
 */
class TestGJDayOfWeekField extends TestGJDateTimeField {
    public TestGJDayOfWeekField(TestGJChronology chrono) {
        super(DateTimeFieldType.dayOfWeek(), TestGJChronology.MILLIS_PER_DAY, chrono);
    }

    @Override
    public int get(long millis) {
        int dayOfWeek = (int) TestGJChronology.mod(iChronology.fixedFromMillis(millis), 7);
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        return dayOfWeek;
    }

    @Override
    public long set(long millis, int value) {
        return add(millis, (long) value - get(millis));
    }

    @Override
    public long add(long millis, long value) {
        return millis + value * TestGJChronology.MILLIS_PER_DAY;
    }

    @Override
    public DurationField getRangeDurationField() {
        return iChronology.weeks();
    }

    @Override
    public int getMinimumValue() {
        return 1;
    }

    @Override
    public int getMaximumValue() {
        return 7;
    }

    @Override
    public long roundFloor(long millis) {
        return iChronology.getDateOnlyMillis(millis);
    }
}
