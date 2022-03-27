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
class TestGJYearField extends TestGJDateTimeField {
    public TestGJYearField(TestGJChronology chrono) {
        super(DateTimeFieldType.year(), chrono.millisPerYear(), chrono);
    }

    @Override
    public int get(long millis) {
        return iChronology.gjYearFromMillis(millis);
    }

    @Override
    public long set(long millis, int value) {
        int[] ymd = iChronology.gjFromMillis(millis);
        millis = iChronology.getTimeOnlyMillis(millis)
            + iChronology.millisFromGJ(value, ymd[1], ymd[2]);
        if (ymd[1] == 2 && ymd[2] == 29 && !iChronology.isLeapYear(value)) {
            millis = iChronology.dayOfYear().add(millis, -1);
        }
        return millis;
    }

    @Override
    public long add(long millis, long value) {
        return set(millis, (int)(get(millis) + value));
    }

    @Override
    public boolean isLeap(long millis) {
        return iChronology.isLeapYear(get(millis));
    }

    @Override
    public int getLeapAmount(long millis) {
        return isLeap(millis) ? 1 : 0;
    }

    @Override
    public DurationField getLeapDurationField() {
        return iChronology.days();
    }

    @Override
    public DurationField getRangeDurationField() {
        return null;
    }

    @Override
    public int getMinimumValue() {
        return -100000000;
    }

    @Override
    public int getMaximumValue() {
        return 100000000;
    }

    @Override
    public long roundFloor(long millis) {
        return iChronology.millisFromGJ(get(millis), 1, 1);
    }
}
