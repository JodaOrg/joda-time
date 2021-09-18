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
class TestGJWeekOfWeekyearField extends TestGJDateTimeField {
    public TestGJWeekOfWeekyearField(TestGJChronology chrono) {
        super(DateTimeFieldType.weekOfWeekyear(),
              (long)(TestGJChronology.MILLIS_PER_DAY * 7), chrono);
    }

    public int get(long millis) {
        return iChronology.isoFromMillis(millis)[1];
    }

    public long set(long millis, int value) {
        int[] wwd = iChronology.isoFromMillis(millis);
        return iChronology.getTimeOnlyMillis(millis)
            + iChronology.millisFromISO(wwd[0], value, wwd[2]);
    }

    public long add(long millis, long value) {
        return iChronology.dayOfYear().add(millis, value * 7);
    }

    public DurationField getRangeDurationField() {
        return iChronology.weeks();
    }

    public int getMinimumValue() {
        return 1;
    }

    public int getMaximumValue() {
        return 53;
    }

    public int getMaximumValue(long millis) {
        // Move millis to end of weekyear.
        millis = iChronology.weekyear().roundFloor(millis);
        millis = iChronology.weekyear().add(millis, 1);
        millis = iChronology.dayOfYear().add(millis, -1);
        return get(millis);
    }

    public long roundFloor(long millis) {
        int[] wwd = iChronology.isoFromMillis(millis);
        return iChronology.millisFromISO(wwd[0], wwd[1], 1);
    }
}
