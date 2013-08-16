/*
 *  Copyright 2001-2013 Stephen Colebourne
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
import org.joda.time.ReadablePartial;
import org.joda.time.field.PreciseDurationDateTimeField;

/**
 * Provides time calculations for the week of a week based year component of time.
 *
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.1, refactored from GJWeekOfWeekyearDateTimeField
 */
final class BasicWeekOfWeekyearDateTimeField extends PreciseDurationDateTimeField {

    @SuppressWarnings("unused")
    private static final long serialVersionUID = -1587436826395135328L;

    private final BasicChronology iChronology;

    /**
     * Restricted constructor
     */
    BasicWeekOfWeekyearDateTimeField(BasicChronology chronology, DurationField weeks) {
        super(DateTimeFieldType.weekOfWeekyear(), weeks);
        iChronology = chronology;
    }

    /**
     * Get the week of a week based year component of the specified time instant.
     * 
     * @see org.joda.time.DateTimeField#get(long)
     * @param instant  the time instant in millis to query.
     * @return the week of the year extracted from the input.
     */
    public int get(long instant) {
        return iChronology.getWeekOfWeekyear(instant);
    }

    public DurationField getRangeDurationField() {
        return iChronology.weekyears();
    }

    // 1970-01-01 is day of week 4, Thursday. The rounding methods need to
    // apply a corrective alignment since weeks begin on day of week 1, Monday.

    public long roundFloor(long instant) {
        return super.roundFloor(instant + 3 * DateTimeConstants.MILLIS_PER_DAY)
            - 3 * DateTimeConstants.MILLIS_PER_DAY;
    }

    public long roundCeiling(long instant) {
        return super.roundCeiling(instant + 3 * DateTimeConstants.MILLIS_PER_DAY)
            - 3 * DateTimeConstants.MILLIS_PER_DAY;
    }

    public long remainder(long instant) {
        return super.remainder(instant + 3 * DateTimeConstants.MILLIS_PER_DAY);
    }

    public int getMinimumValue() {
        return 1;
    }

    public int getMaximumValue() {
        return 53;
    }

    public int getMaximumValue(long instant) {
        int weekyear = iChronology.getWeekyear(instant);
        return iChronology.getWeeksInYear(weekyear);
    }

    public int getMaximumValue(ReadablePartial partial) {
        if (partial.isSupported(DateTimeFieldType.weekyear())) {
            int weekyear = partial.get(DateTimeFieldType.weekyear());
            return iChronology.getWeeksInYear(weekyear);
        }
        return 53;
    }

    public int getMaximumValue(ReadablePartial partial, int[] values) {
        int size = partial.size();
        for (int i = 0; i < size; i++) {
            if (partial.getFieldType(i) == DateTimeFieldType.weekyear()) {
                int weekyear = values[i];
                return iChronology.getWeeksInYear(weekyear);
            }
        }
        return 53;
    }

    protected int getMaximumValueForSet(long instant, int value) {
        return value > 52 ? getMaximumValue(instant) : 52;
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return iChronology.weekOfWeekyear();
    }
}
