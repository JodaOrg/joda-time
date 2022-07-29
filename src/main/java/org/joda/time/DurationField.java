/*
 *  Copyright 2001-2009 Stephen Colebourne
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
package org.joda.time;

/**
 * Defines the calculation engine for duration fields.
 * The interface defines a set of methods that manipulate a millisecond duration
 * with regards to a single field, such as months or seconds.
 * <p>
 * This design is extensible so, if you wish, you can extract a different field from
 * the millisecond duration. A number of standard implementations are provided to assist.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public abstract class DurationField implements Comparable<DurationField> {

    /**
     * Get the type of the field.
     * 
     * @return field type
     */
    public abstract DurationFieldType getType();

    /**
     * Get the name of the field.
     * <p>
     * By convention, names are plural.
     * 
     * @return field name
     */
    public abstract String getName();

    /**
     * Returns true if this field is supported.
     * 
     * @return true if this field is supported
     */
    public abstract boolean isSupported();

    /**
     * Is this field precise. A precise field can calculate its value from
     * milliseconds without needing a reference date. Put another way, a
     * precise field's unit size is not variable.
     * 
     * @return true if precise
     * @see #getUnitMillis()
     */
    public abstract boolean isPrecise();
    
    /**
     * Returns the amount of milliseconds per unit value of this field. For
     * example, if this field represents "seconds", then this returns the
     * milliseconds in one second.
     * <p>
     * For imprecise fields, the unit size is variable, and so this method
     * returns a suitable average value.
     *
     * @return the unit size of this field, in milliseconds
     * @see #isPrecise()
     */
    public abstract long getUnitMillis();

    //------------------------------------------------------------------------
    /**
     * Get the value of this field from the milliseconds, which is approximate
     * if this field is imprecise.
     *
     * @param duration  the milliseconds to query, which may be negative
     * @return the value of the field, in the units of the field, which may be
     * negative
     * @throws ArithmeticException if the value is too large for an int
     */
    public abstract int getValue(long duration);

    /**
     * Get the value of this field from the milliseconds, which is approximate
     * if this field is imprecise.
     *
     * @param duration  the milliseconds to query, which may be negative
     * @return the value of the field, in the units of the field, which may be
     * negative
     */
    public abstract long getValueAsLong(long duration);

    /**
     * Get the value of this field from the milliseconds relative to an
     * instant. For precise fields this method produces the same result as for
     * the single argument get method.
     * <p>
     * If the millisecond duration is positive, then the instant is treated as a
     * "start instant". If negative, the instant is treated as an "end instant".
     * 
     * @param duration  the milliseconds to query, which may be negative
     * @param instant  the start instant to calculate relative to
     * @return the value of the field, in the units of the field, which may be
     * negative
     * @throws ArithmeticException if the value is too large for an int
     */
    public abstract int getValue(long duration, long instant);

    /**
     * Get the value of this field from the milliseconds relative to an
     * instant. For precise fields this method produces the same result as for
     * the single argument get method.
     * <p>
     * If the millisecond duration is positive, then the instant is treated as a
     * "start instant". If negative, the instant is treated as an "end instant".
     * 
     * @param duration  the milliseconds to query, which may be negative
     * @param instant  the start instant to calculate relative to
     * @return the value of the field, in the units of the field, which may be
     * negative
     */
    public abstract long getValueAsLong(long duration, long instant);

    //------------------------------------------------------------------------
    /**
     * Get the millisecond duration of this field from its value, which is
     * approximate if this field is imprecise.
     * 
     * @param value  the value of the field, which may be negative
     * @return the milliseconds that the field represents, which may be
     * negative
     */
    public abstract long getMillis(int value);

    /**
     * Get the millisecond duration of this field from its value, which is
     * approximate if this field is imprecise.
     * 
     * @param value  the value of the field, which may be negative
     * @return the milliseconds that the field represents, which may be
     * negative
     */
    public abstract long getMillis(long value);

    /**
     * Get the millisecond duration of this field from its value relative to an
     * instant. For precise fields this method produces the same result as for
     * the single argument getMillis method.
     * <p>
     * If the value is positive, then the instant is treated as a "start
     * instant". If negative, the instant is treated as an "end instant".
     *
     * @param value  the value of the field, which may be negative
     * @param instant  the instant to calculate relative to
     * @return the millisecond duration that the field represents, which may be
     * negative
     */
    public abstract long getMillis(int value, long instant);

    /**
     * Get the millisecond duration of this field from its value relative to an
     * instant. For precise fields this method produces the same result as for
     * the single argument getMillis method.
     * <p>
     * If the value is positive, then the instant is treated as a "start
     * instant". If negative, the instant is treated as an "end instant".
     *
     * @param value  the value of the field, which may be negative
     * @param instant  the instant to calculate relative to
     * @return the millisecond duration that the field represents, which may be
     * negative
     */
    public abstract long getMillis(long value, long instant);

    /**
     * Adds a duration value (which may be negative) to the instant.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to add to
     * @param value  the value to add, in the units of the field
     * @return the updated milliseconds
     */
    public abstract long add(long instant, int value);

    /**
     * Adds a duration value (which may be negative) to the instant.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to add to
     * @param value  the value to add, in the units of the field
     * @return the updated milliseconds
     */
    public abstract long add(long instant, long value);

    /**
     * Subtracts a duration value (which may be negative) from the instant.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to subtract from
     * @param value  the value to subtract, in the units of the field
     * @return the updated milliseconds
     * @since 1.1
     */
    public long subtract(long instant, int value) {
        if (value == Integer.MIN_VALUE) {
            return subtract(instant, (long) value);
        }
        return add(instant, -value);
    }

    /**
     * Subtracts a duration value (which may be negative) from the instant.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to subtract from
     * @param value  the value to subtract, in the units of the field
     * @return the updated milliseconds
     * @since 1.1
     */
    public long subtract(long instant, long value) {
        if (value == Long.MIN_VALUE) {
            throw new ArithmeticException("Long.MIN_VALUE cannot be negated");
        }
        return add(instant, -value);
    }

    /**
     * Computes the difference between two instants, as measured in the units
     * of this field. Any fractional units are dropped from the result. Calling
     * getDifference reverses the effect of calling add. In the following code:
     *
     * <pre>
     * long instant = ...
     * int v = ...
     * int age = getDifference(add(instant, v), instant);
     * </pre>
     *
     * The value 'age' is the same as the value 'v'.
     *
     * @param minuendInstant the milliseconds from 1970-01-01T00:00:00Z to
     * subtract from
     * @param subtrahendInstant the milliseconds from 1970-01-01T00:00:00Z to
     * subtract off the minuend
     * @return the difference in the units of this field
     */
    public abstract int getDifference(long minuendInstant, long subtrahendInstant);

    /**
     * Computes the difference between two instants, as measured in the units
     * of this field. Any fractional units are dropped from the result. Calling
     * getDifference reverses the effect of calling add. In the following code:
     *
     * <pre>
     * long instant = ...
     * long v = ...
     * long age = getDifferenceAsLong(add(instant, v), instant);
     * </pre>
     *
     * The value 'age' is the same as the value 'v'.
     *
     * @param minuendInstant the milliseconds from 1970-01-01T00:00:00Z to
     * subtract from
     * @param subtrahendInstant the milliseconds from 1970-01-01T00:00:00Z to
     * subtract off the minuend
     * @return the difference in the units of this field
     */
    public abstract long getDifferenceAsLong(long minuendInstant, long subtrahendInstant);

    // Adding this definition would be backwards incompatible with earlier subclasses
    // This definition of compareTo was present in previous versions, and still applies
//    /**
//     * Compares this duration field with another duration field for ascending
//     * unit millisecond order. This ordering is inconsistent with equals, as it
//     * ignores name and precision.
//     *
//     * @param durationField  a duration field to check against
//     * @return negative value if this is less, 0 if equal, or positive value if greater
//     * @throws NullPointerException if the object is null
//     * @throws ClassCastException if the object type is not supported
//     */
//    public abstract int compareTo(DurationField durationField);

//    /**
//     * Returns a localized unit name of this field, using the given value as an
//     * aid. For example, the unit name may differ if it is plural.
//     *
//     * @param value the duration value to use for selecting a unit name
//     * @param locale the locale to use for selecting a name, null for default
//     */
//    //String getUnitName(long value, Locale locale);
//
//    /**
//     * Returns a localized unit name of this field, using the given value as an
//     * aid. For example, the unit name may differ if it is plural.
//     *
//     * @param value the duration value to use for selecting a unit name
//     */
//    //String getUnitName(long value);
//
//    /**
//     * Get the maximum length string returned by getUnitName.
//     * 
//     * @param locale the locale to use for selecting a unit name, null for
//     * default
//     * @return the maximum name length
//     */
//    //int getMaximumUnitNameLength(Locale locale);

    //------------------------------------------------------------------------
    /**
     * Get a suitable debug string.
     * 
     * @return debug string
     */
    @Override
    public abstract String toString();
    
}
