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
 * This field is not publicly exposed by ISOChronology, but rather it is used to
 * build the yearOfCentury and centuryOfEra fields. It merely drops the sign of
 * the year.
 *
 * @author Brian S O'Neill
 * @see GJYearOfEraDateTimeField
 * @since 1.0
 */
class ISOYearOfEraDateTimeField extends DecoratedDateTimeField {

    @SuppressWarnings("unused")
    private static final long serialVersionUID = 7037524068969447317L;

    /**
     * Singleton instance
     */
    static final DateTimeField INSTANCE = new ISOYearOfEraDateTimeField();

    /**
     * Restricted constructor.
     */
    private ISOYearOfEraDateTimeField() {
        super(GregorianChronology.getInstanceUTC().year(), DateTimeFieldType.yearOfEra());
    }

    @Override
    public DurationField getRangeDurationField() {
        return GregorianChronology.getInstanceUTC().eras();
    }

    @Override
    public int get(long instant) {
        int year = getWrappedField().get(instant);
        return year < 0 ? -year : year;
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

    @Override
    public long set(long instant, int year) {
        FieldUtils.verifyValueBounds(this, year, 0, getMaximumValue());
        if (getWrappedField().get(instant) < 0) {
            year = -year;
        }
        return super.set(instant, year);
    }

    @Override
    public int getMinimumValue() {
        return 0;
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
        return INSTANCE;
    }
}
