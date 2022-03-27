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
class TestGJWeekyearField extends TestGJDateTimeField {
    public TestGJWeekyearField(TestGJChronology chrono) {
        super(DateTimeFieldType.weekyear(), chrono.millisPerYear(), chrono);
    }

    @Override
    public int get(long millis) {
        return iChronology.isoFromMillis(millis)[0];
    }

    @Override
    public long set(long millis, int value) {
        int[] wwd = iChronology.isoFromMillis(millis);
        millis = iChronology.getTimeOnlyMillis(millis)
            + iChronology.millisFromISO(value, wwd[1], wwd[2]);
        if (wwd[1] == 53) {
            int[] wwd2 = iChronology.isoFromMillis(millis);
            if (wwd2[0] != value) {
                // Set year doesn't have 53 weeks, so back off a week.
                millis = iChronology.dayOfYear().add(millis, -7);
            }
        }
        return millis;
    }

    @Override
    public long add(long millis, long value) {
        return set(millis, (int)(get(millis) + value));
    }

    @Override
    public boolean isLeap(long millis) {
        return iChronology.weekOfWeekyear().getMaximumValue(millis) > 52;
    }

    @Override
    public int getLeapAmount(long millis) {
        return iChronology.weekOfWeekyear().getMaximumValue(millis) - 52;
    } 

    @Override
    public DurationField getLeapDurationField() {
        return iChronology.weeks();
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
        return iChronology.millisFromISO(get(millis), 1, 1);
    }
}
