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

/**
 * 
 * @author Brian S O'Neill
 */
class TestJulianMonthOfYearField extends TestGJMonthOfYearField {
    public TestJulianMonthOfYearField(TestJulianChronology chrono) {
        super(chrono);
    }

    @Override
    public int get(long millis) {
        return iChronology.gjFromMillis(millis)[1];
    }

    @Override
    public long add(long millis, long value) {
        int year = iChronology.year().get(millis);
        int newYear = year + (int)TestGJChronology.div(value, 12);
        if (year < 0) {
            if (newYear >= 0) {
                newYear++;
            }
        } else {
            if (newYear <= 0) {
                newYear--;
            }
        }
        int newMonth = get(millis) + (int)TestGJChronology.mod(value, 12);
        if (newMonth > 12) {
            if (newYear == -1) {
                newYear = 1;
            } else {
                newYear++;
            }
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
}
