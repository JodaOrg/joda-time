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
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;

/**
 * Provides time calculations for the month of the year component of time.
 *
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.2, refactored from GJMonthOfYearDateTimeField
 */
class BasicQuarterOfYearDateTimeField extends BasicMonthOfYearDateTimeField {

    private static final int MIN = 1;

    /**
     * Restricted constructor.
     *
     * @param leapMonth the month of year that leaps
     */
    BasicQuarterOfYearDateTimeField(BasicChronology chronology, int leapMonth) {
        super(chronology, leapMonth);
    }

    //-----------------------------------------------------------------------
    /**
     * Get the Quarter component of the specified time instant.
     *
     * @see org.joda.time.DateTimeField#get(long)
     * @see org.joda.time.ReadableDateTime#getMonthOfYear()
     * @param instant  the time instant in millis to query.
     * @return the quarter extracted from the input.
     */
    public int get(long instant) {
        return (super.get(instant) - 1) / 3 + 1;
    }

    //-----------------------------------------------------------------------
    /**
     * Add the specified month to the specified time instant.
     * The amount added may be negative.<p>
     * If the new month has less total days than the specified
     * day of the month, this value is coerced to the nearest
     * sane value. e.g.<p>
     * 07-31 - (1 month) = 06-30<p>
     * 03-31 - (1 month) = 02-28 or 02-29 depending<p>
     *
     * @see org.joda.time.DateTimeField#add
     * @see org.joda.time.ReadWritableDateTime#addMonths(int)
     * @param instant  the time instant in millis to update.
     * @param months  the months to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long instant, int quarters) {
        return super.add(instant, quarters * 3);
    }

    //-----------------------------------------------------------------------
    public long add(long instant, long quarters) {
        return super.add(instant, quarters * 3);
    }

    //-----------------------------------------------------------------------
    public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
        if (valueToAdd == 0) {
            return values;
        }
        if (partial.size() > 0 && partial.getFieldType(0).equals(DateTimeFieldType.quarterOfYear()) && fieldIndex == 2) {
            return super.add(partial, fieldIndex, values, valueToAdd * 3);
        }
        return super.add(partial, fieldIndex, values, valueToAdd);
    }

    //-----------------------------------------------------------------------
    /**
     * Add to the Month component of the specified time instant
     * wrapping around within that component if necessary.
     *
     * @see org.joda.time.DateTimeField#addWrapField
     * @param instant  the time instant in millis to update.
     * @param months  the months to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapField(long instant, int quarters) {
        return super.addWrapField(instant, quarters * 3);
    }

    //-----------------------------------------------------------------------
    /**
     * Set the month component of the specified time instant.<p>
     * If the new month has less total days than the specified
     * day of the month, this value is coerced to the nearest
     * sane value. e.g.<p>
     * 07-31 to month 6 = 06-30<p>
     * 03-31 to month 2 = 02-28 or 02-29 depending<p>
     *
     * @param instant  the time instant in millis to update.
     * @param month  the month (1,12) to update the time to.
     * @return the updated time instant.
     * @throws IllegalArgumentException  if month is invalid
     */
    public long set(long instant, int quarter) {
        return super.set(instant, quarter * 3);
    }

    //-----------------------------------------------------------------------
    public int getMinimumValue() {
        return 1;
    }

    //-----------------------------------------------------------------------
    public int getMaximumValue() {
        return 4;
    }
}
