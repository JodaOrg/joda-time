/*
 *  Copyright 2001-2006 Stephen Colebourne
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
package org.joda.time.base;

import java.io.Serializable;

import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;

/**
 * BaseSingleFieldPeriod is an abstract implementation of ReadablePeriod that
 * manages a single duration field, such as days or minutes.
 * <p>
 * This class should generally not be used directly by API users.
 * The {@link ReadablePeriod} interface should be used when different 
 * kinds of period objects are to be referenced.
 * <p>
 * BaseSingleFieldPeriod subclasses may be mutable and not thread-safe.
 *
 * @author Stephen Colebourne
 * @since 1.4
 */
public abstract class BaseSingleFieldPeriod
        implements ReadablePeriod, Comparable, Serializable {

    /** Serialization version. */
    private static final long serialVersionUID = 9386874258972L;

    /** The period in the units of this period. */
    private int iPeriod;

    //-----------------------------------------------------------------------
    /**
     * Calculates the number of whole units between the two specified datetimes.
     *
     * @param start  the start instant, validated to not be null
     * @param end  the end instant, validated to not be null
     * @param field  the field type to use, must not be null
     * @return the period
     * @throws IllegalArgumentException if the instants are null or invalid
     */
    protected static int between(ReadableInstant start, ReadableInstant end, DurationFieldType field) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("ReadableInstant objects must not be null");
        }
        Chronology chrono = DateTimeUtils.getInstantChronology(start);
        int amount = field.getField(chrono).getDifference(end.getMillis(), start.getMillis());
        return amount;
    }

    //-----------------------------------------------------------------------
    /**
     * Calculates the number of whole units between the two specified partial datetimes.
     * <p>
     * The two partials must contain the same fields, for example you can specify
     * two <code>LocalDate</code> objects.
     *
     * @param start  the start partial date, validated to not be null
     * @param end  the end partial date, validated to not be null
     * @param zeroInstance  the zero instance constant, must not be null
     * @return the period
     * @throws IllegalArgumentException if the partials are null or invalid
     */
    protected static int between(ReadablePartial start, ReadablePartial end, ReadablePeriod zeroInstance) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("ReadablePartial objects must not be null");
        }
        if (start.size() != end.size()) {
            throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
        }
        for (int i = 0, isize = start.size(); i < isize; i++) {
            if (start.getFieldType(i) != end.getFieldType(i)) {
                throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
            }
        }
        if (DateTimeUtils.isContiguous(start) == false) {
            throw new IllegalArgumentException("ReadablePartial objects must be contiguous");
        }
        Chronology chrono = DateTimeUtils.getChronology(start.getChronology()).withUTC();
        int[] values = chrono.get(zeroInstance, chrono.set(start, 0L), chrono.set(end, 0L));
        return values[0];
    }

    /**
     * Creates a new instance representing the number of complete standard length units
     * in the specified period.
     * <p>
     * This factory method converts all fields from the period to hours using standardised
     * durations for each field. Only those fields which have a precise duration in
     * the ISO UTC chronology can be converted.
     * <ul>
     * <li>One week consists of 7 days.
     * <li>One day consists of 24 hours.
     * <li>One hour consists of 60 minutes.
     * <li>One minute consists of 60 seconds.
     * <li>One second consists of 1000 milliseconds.
     * </ul>
     * Months and Years are imprecise and periods containing these values cannot be converted.
     *
     * @param period  the period to get the number of hours from, must not be null
     * @param millisPerUnit  the number of milliseconds in one standard unit of this period
     * @throws IllegalArgumentException if the period contains imprecise duration values
     */
    protected static int standardPeriodIn(ReadablePeriod period, long millisPerUnit) {
        if (period == null) {
            return 0;
        }
        Chronology iso = ISOChronology.getInstanceUTC();
        long duration = 0L;
        for (int i = 0; i < period.size(); i++) {
            int value = period.getValue(i);
            if (value != 0) {
                DurationField field = period.getFieldType(i).getField(iso);
                if (field.isPrecise() == false) {
                    throw new IllegalArgumentException(
                            "Cannot convert period to duration as " + field.getName() +
                            " is not precise in the period " + period);
                }
                duration = FieldUtils.safeAdd(duration, FieldUtils.safeMultiply(field.getUnitMillis(), value));
            }
        }
        return FieldUtils.safeToInt(duration / millisPerUnit);
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a new instance representing the specified period.
     *
     * @param period  the period to represent
     */
    protected BaseSingleFieldPeriod(int period) {
        super();
        iPeriod = period;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the amount of this period.
     *
     * @return the period value
     */
    protected int getValue() {
        return iPeriod;
    }

    /**
     * Sets the amount of this period.
     * To make a subclass immutable you must declare it final, or block this method.
     *
     * @param value  the period value
     */
    protected void setValue(int value) {
        iPeriod = value;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the single duration field type.
     *
     * @return the duration field type, not null
     */
    public abstract DurationFieldType getFieldType();

    /**
     * Gets the period type which matches the duration field type.
     *
     * @return the period type, not null
     */
    public abstract PeriodType getPeriodType();

    //-----------------------------------------------------------------------
    /**
     * Gets the number of fields that this period supports, which is one.
     *
     * @return the number of fields supported, which is one
     */
    public int size() {
        return 1;
    }

    /**
     * Gets the field type at the specified index.
     * <p>
     * The only index supported by this period is zero which returns the
     * field type of this class.
     *
     * @param index  the index to retrieve, which must be zero
     * @return the field at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public DurationFieldType getFieldType(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        return getFieldType();
    }

    /**
     * Gets the value at the specified index.
     * <p>
     * The only index supported by this period is zero.
     *
     * @param index  the index to retrieve, which must be zero
     * @return the value of the field at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public int getValue(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        return getValue();
    }

    /**
     * Gets the value of a duration field represented by this period.
     * <p>
     * If the field type specified does not match the type used by this class
     * then zero is returned.
     *
     * @param type  the field type to query, null returns zero
     * @return the value of that field, zero if field not supported
     */
    public int get(DurationFieldType type) {
        if (type == getFieldType()) {
            return getValue();
        }
        return 0;
    }

    /**
     * Checks whether the duration field specified is supported by this period.
     *
     * @param type  the type to check, may be null which returns false
     * @return true if the field is supported
     */
    public boolean isSupported(DurationFieldType type) {
        return (type == getFieldType());
    }

    //-----------------------------------------------------------------------
    /**
     * Get this period as an immutable <code>Period</code> object.
     * The period will use <code>PeriodType.standard()</code>.
     *
     * @return a <code>Period</code> representing the same number of days
     */
    public Period toPeriod() {
        return Period.ZERO.withFields(this);
    }

    /**
     * Get this object as a <code>MutablePeriod</code>.
     * <p>
     * This will always return a new <code>MutablePeriod</code> with the same fields.
     * The period will use <code>PeriodType.standard()</code>.
     * 
     * @return a MutablePeriod using the same field set and values
     */
    public MutablePeriod toMutablePeriod() {
        MutablePeriod period = new MutablePeriod();
        period.add(this);
        return period;
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this object with the specified object for equality based on the
     * value of each field. All ReadablePeriod instances are accepted, but only
     * those with a matching <code>PeriodType</code> can return true.
     *
     * @param period  a readable period to check against
     * @return true if all the field values are equal, false if
     *  not or the period is null or of an incorrect type
     */
    public boolean equals(Object period) {
        if (this == period) {
            return true;
        }
        if (period instanceof ReadablePeriod == false) {
            return false;
        }
        ReadablePeriod other = (ReadablePeriod) period;
        return (other.getPeriodType() == getPeriodType() && other.getValue(0) == getValue());
    }

    /**
     * Gets a hash code for the period as defined by ReadablePeriod.
     *
     * @return a hash code
     */
    public int hashCode() {
        int total = 17;
        total = 27 * total + getValue();
        total = 27 * total + getFieldType().hashCode();
        return total;
    }

    /**
     * Compares this period to another object of the same class.
     *
     * @param other  the other period, must not be null
     * @return zero if equal, positive if greater, negative if less
     * @throws NullPointerException if the other period is null
     * @throws ClassCastException if the other period is of a different type
     */
    public int compareTo(Object other) {
        if (other.getClass() != getClass()) {
            throw new ClassCastException(getClass() + " cannot be compared to " + other.getClass());
        }
        int otherValue = ((BaseSingleFieldPeriod) other).getValue();
        int thisValue = getValue();
        if (thisValue > otherValue) {
            return 1;
        }
        if (thisValue < otherValue) {
            return -1;
        }
        return 0;
    }

}
