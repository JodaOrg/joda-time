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
package org.joda.time.chrono;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.field.PreciseDurationDateTimeField;

/**
 * 
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
final class CopticMonthOfYearDateTimeField extends PreciseDurationDateTimeField {

    private static final long serialVersionUID = 7741038885247700323L;

    private final BaseGJChronology iChronology;

    /**
     * Restricted constructor.
     */
    CopticMonthOfYearDateTimeField(BaseGJChronology chronology, DurationField months) {
        super(DateTimeFieldType.monthOfYear(), months);
        iChronology = chronology;
    }

    public int get(long instant) {
        return (iChronology.getDayOfYear(instant) - 1) / 30 + 1;
    }

    public long set(long instant, int value) {
        instant = super.set(instant, value);
        if (value == 13) {
            int day = iChronology.getDayOfYear(instant);
            if (day < 30) {
                // Move back a few days to the end of the 13th "month".
                instant -= (long)DateTimeConstants.MILLIS_PER_DAY * day;
            }
        }
        return instant;
    }

    public DurationField getRangeDurationField() {
        return iChronology.years();
    }

    public boolean isLeap(long instant) {
        return get(instant) > 12 && iChronology.isLeapYear(iChronology.getYear(instant));
    }

    public int getLeapAmount(long instant) {
        return isLeap(instant) ? 1 : 0;
    }

    public DurationField getLeapDurationField() {
        return iChronology.days();
    }

    public int getMinimumValue() {
        return 1;
    }

    public int getMaximumValue() {
        return 13;
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return iChronology.monthOfYear();
    }

}
