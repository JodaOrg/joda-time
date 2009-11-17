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
class TestGJDayOfMonthField extends TestGJDateTimeField {
    public TestGJDayOfMonthField(TestGJChronology chrono) {
        super(DateTimeFieldType.dayOfMonth(), TestGJChronology.MILLIS_PER_DAY, chrono);
    }

    public int get(long millis) {
        return iChronology.gjFromMillis(millis)[2];
    }

    public long set(long millis, int value) {
        int[] ymd = iChronology.gjFromMillis(millis);
        return iChronology.getTimeOnlyMillis(millis)
            + iChronology.millisFromGJ(ymd[0], ymd[1], value);
    }

    public long add(long millis, long value) {
        return millis + value * TestGJChronology.MILLIS_PER_DAY;
    }

    public DurationField getRangeDurationField() {
        return iChronology.months();
    }

    public int getMinimumValue() {
        return 1;
    }

    public int getMaximumValue() {
        return 31;
    }

    public int getMaximumValue(long millis) {
        int[] lengths = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (iChronology.year().isLeap(millis)) {
            lengths[2] = 29;
        }
        return lengths[iChronology.monthOfYear().get(millis)];
    }

    public long roundFloor(long millis) {
        return iChronology.getDateOnlyMillis(millis);
    }
}
