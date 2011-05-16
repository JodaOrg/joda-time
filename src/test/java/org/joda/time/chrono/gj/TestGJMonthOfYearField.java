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
class TestGJMonthOfYearField extends TestGJDateTimeField {
    public TestGJMonthOfYearField(TestGJChronology chrono) {
        super(DateTimeFieldType.monthOfYear(), chrono.millisPerMonth(), chrono);
    }

    public int get(long millis) {
        return iChronology.gjFromMillis(millis)[1];
    }

    public long set(long millis, int value) {
        long timeOnlyMillis = iChronology.getTimeOnlyMillis(millis);
        int[] ymd = iChronology.gjFromMillis(millis);
        // First set to start of month...
        millis = iChronology.millisFromGJ(ymd[0], value, 1);
        // ...and use dayOfMonth field to check range.
        int maxDay = iChronology.dayOfMonth().getMaximumValue(millis);
        if (ymd[2] > maxDay) {
            ymd[2] = maxDay;
        }
        return timeOnlyMillis + iChronology.millisFromGJ(ymd[0], value, ymd[2]);
    }

    public long add(long millis, long value) {
        int newYear = iChronology.year().get(millis)
            + (int)TestGJChronology.div(value, 12);
        int newMonth = get(millis) + (int)TestGJChronology.mod(value, 12);
        if (newMonth > 12) {
            newYear++;
            newMonth -= 12;
        }
        int newDay = iChronology.dayOfMonth().get(millis);
        millis = iChronology.getTimeOnlyMillis(millis) 
            + iChronology.millisFromGJ(newYear, newMonth, newDay);
        while (get(millis) != newMonth) {
            millis = iChronology.dayOfYear().add(millis, -1);
        }
        return millis;
    }

    public boolean isLeap(long millis) {
        int[] ymd = iChronology.gjFromMillis(millis);
        return ymd[1] == 2 && iChronology.isLeapYear(ymd[0]);
    }

    public int getLeapAmount(long millis) {
        return isLeap(millis) ? 1 : 0;
    }

    public DurationField getLeapDurationField() {
        return iChronology.days();
    }

    public DurationField getRangeDurationField() {
        return iChronology.years();
    }

    public int getMinimumValue() {
        return 1;
    }

    public int getMaximumValue() {
        return 12;
    }

    public long roundFloor(long millis) {
        int[] ymd = iChronology.gjFromMillis(millis);
        return iChronology.millisFromGJ(ymd[0], ymd[1], 1);
    }
}
