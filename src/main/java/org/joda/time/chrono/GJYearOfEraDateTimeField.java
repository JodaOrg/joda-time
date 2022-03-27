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

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;
import org.joda.time.field.DecoratedDateTimeField;
import org.joda.time.field.FieldUtils;

/**
 * Provides time calculations for the year of era component of time.
 * 
 * @author Brian S O'Neill
 * @since 1.0
 */
final class GJYearOfEraDateTimeField extends DecoratedDateTimeField {

    @SuppressWarnings("unused")
    private static final long serialVersionUID = -5961050944769862059L;

    private final BasicChronology iChronology;

    /**
     * Restricted constructor.
     */
    GJYearOfEraDateTimeField(DateTimeField yearField, BasicChronology chronology) {
        super(yearField, DateTimeFieldType.yearOfEra());
        iChronology = chronology;
    }

    @Override
    public DurationField getRangeDurationField() {
        return iChronology.eras();
    }

    @Override
    public int get(long instant) {
        int year = getWrappedField().get(instant);
        if (year <= 0) {
            year = 1 - year;
        }
        return year;
    }

    @Override
    public long add(long instant, int years) {
        return getWrappedField().add(instant, years);
    }

    @Override
    public long add(long instant, long years) {
        return getWrappedField().add(instant, years);
    }

    @Override
    public long addWrapField(long instant, int years) {
        return getWrappedField().addWrapField(instant, years);
    }

    @Override
    public int[] addWrapField(ReadablePartial instant, int fieldIndex, int[] values, int years) {
        return getWrappedField().addWrapField(instant, fieldIndex, values, years);
    }

    @Override
    public int getDifference(long minuendInstant, long subtrahendInstant) {
        return getWrappedField().getDifference(minuendInstant, subtrahendInstant);
    }

    @Override
    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        return getWrappedField().getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    /**
     * Set the year component of the specified time instant.
     * 
     * @param instant  the time instant in millis to update.
     * @param year  the year (0,292278994) to update the time to.
     * @return the updated time instant.
     * @throws IllegalArgumentException  if year is invalid.
     */
    @Override
    public long set(long instant, int year) {
        FieldUtils.verifyValueBounds(this, year, 1, getMaximumValue());
        if (iChronology.getYear(instant) <= 0) {
            year = 1 - year;
        }
        return super.set(instant, year);
    }

    @Override
    public int getMinimumValue() {
        return 1;
    }

    @Override
    public int getMaximumValue() {
        return getWrappedField().getMaximumValue();
    }

    @Override
    public long roundFloor(long instant) {
        return getWrappedField().roundFloor(instant);
    }

    @Override
    public long roundCeiling(long instant) {
        return getWrappedField().roundCeiling(instant);
    }

    @Override
    public long remainder(long instant) {
        return getWrappedField().remainder(instant);
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return iChronology.yearOfEra();
    }
}
