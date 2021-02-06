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
package org.joda.time;

import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

/**
 * An immutable time period representing a number of years.
 * <p>
 * <code>Years</code> is an immutable period that can only store years.
 * It does not store months, days or hours for example. As such it is a
 * type-safe way of representing a number of years in an application.
 * <p>
 * The number of years is set in the constructor, and may be queried using
 * <code>getYears()</code>. Basic mathematical operations are provided -
 * <code>plus()</code>, <code>minus()</code>, <code>multipliedBy()</code> and
 * <code>dividedBy()</code>.
 * <p>
 * <code>Years</code> is thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @since 1.4
 */
public final class Years extends BaseSingleFieldPeriod {

    /** Constant representing zero years. */
    public static final Years ZERO = new Years(0);
    /** Constant representing one year. */
    public static final Years ONE = new Years(1);
    /** Constant representing two years. */
    public static final Years TWO = new Years(2);
    /** Constant representing three years. */
    public static final Years THREE = new Years(3);
    /** Constant representing the maximum number of years that can be stored in this object. */
    public static final Years MAX_VALUE = new Years(Integer.MAX_VALUE);
    /** Constant representing the minimum number of years that can be stored in this object. */
    public static final Years MIN_VALUE = new Years(Integer.MIN_VALUE);

    /** The parser to use for this class. */
    private static final PeriodFormatter PARSER = ISOPeriodFormat.standard().withParseType(PeriodType.years());
    /** Serialization version. */
    private static final long serialVersionUID = 87525275727380868L;

    //-----------------------------------------------------------------------
    /**
     * Obtains an instance of <code>Years</code> that may be cached.
     * <code>Years</code> is immutable, so instances can be cached and shared.
     * This factory method provides access to shared instances.
     *
     * @param years  the number of years to obtain an instance for
     * @return the instance of Years
     */
    public static Years years(int years) {
        switch (years) {
            case 0:
                return ZERO;
            case 1:
                return ONE;
            case 2:
                return TWO;
            case 3:
                return THREE;
            case Integer.MAX_VALUE:
                return MAX_VALUE;
            case Integer.MIN_VALUE:
                return MIN_VALUE;
            default:
                return new Years(years);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a <code>Years</code> representing the number of whole years
     * between the two specified datetimes. This method correctly handles
     * any daylight savings time changes that may occur during the interval.
     *
     * @param start  the start instant, must not be null
     * @param end  the end instant, must not be null
     * @return the period in years
     * @throws IllegalArgumentException if the instants are null or invalid
     */
    public static Years yearsBetween(ReadableInstant start, ReadableInstant end) {
        int amount = BaseSingleFieldPeriod.between(start, end, DurationFieldType.years());
        return Years.years(amount);
    }

    /**
     * Creates a <code>Years</code> representing the number of whole years
     * between the two specified partial datetimes.
     * <p>
     * The two partials must contain the same fields, for example you can specify
     * two <code>LocalDate</code> objects.
     *
     * @param start  the start partial date, must not be null
     * @param end  the end partial date, must not be null
     * @return the period in years
     * @throws IllegalArgumentException if the partials are null or invalid
     */
    public static Years yearsBetween(ReadablePartial start, ReadablePartial end) {
        if (start instanceof LocalDate && end instanceof LocalDate)   {
            Chronology chrono = DateTimeUtils.getChronology(start.getChronology());
            int years = chrono.years().getDifference(
                    ((LocalDate) end).getLocalMillis(), ((LocalDate) start).getLocalMillis());
            return Years.years(years);
        }
        int amount = BaseSingleFieldPeriod.between(start, end, ZERO);
        return Years.years(amount);
    }

    /**
     * Creates a <code>Years</code> representing the number of whole years
     * in the specified interval. This method correctly handles any daylight
     * savings time changes that may occur during the interval.
     *
     * @param interval  the interval to extract years from, null returns zero
     * @return the period in years
     * @throws IllegalArgumentException if the partials are null or invalid
     */
    public static Years yearsIn(ReadableInterval interval) {
        if (interval == null)   {
            return Years.ZERO;
        }
        int amount = BaseSingleFieldPeriod.between(interval.getStart(), interval.getEnd(), DurationFieldType.years());
        return Years.years(amount);
    }

    /**
     * Creates a new <code>Years</code> by parsing a string in the ISO8601 format 'PnY'.
     * <p>
     * The parse will accept the full ISO syntax of PnYnMnWnDTnHnMnS however only the
     * years component may be non-zero. If any other component is non-zero, an exception
     * will be thrown.
     *
     * @param periodStr  the period string, null returns zero
     * @return the period in years
     * @throws IllegalArgumentException if the string format is invalid
     */
    @FromString
    public static Years parseYears(String periodStr) {
        if (periodStr == null) {
            return Years.ZERO;
        }
        Period p = PARSER.parsePeriod(periodStr);
        return Years.years(p.getYears());
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a new instance representing a number of years.
     * You should consider using the factory method {@link #years(int)}
     * instead of the constructor.
     *
     * @param years  the number of years to represent
     */
    private Years(int years) {
        super(years);
    }

    /**
     * Resolves singletons.
     * 
     * @return the singleton instance
     */
    private Object readResolve() {
        return Years.years(getValue());
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the duration field type, which is <code>years</code>.
     *
     * @return the period type
     */
    public DurationFieldType getFieldType() {
        return DurationFieldType.years();
    }

    /**
     * Gets the period type, which is <code>years</code>.
     *
     * @return the period type
     */
    public PeriodType getPeriodType() {
        return PeriodType.years();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the number of years that this period represents.
     *
     * @return the number of years in the period
     */
    public int getYears() {
        return getValue();
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new instance with the specified number of years added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param years  the amount of years to add, may be negative
     * @return the new period plus the specified number of years
     * @throws ArithmeticException if the result overflows an int
     */
    public Years plus(int years) {
        if (years == 0) {
            return this;
        }
        return Years.years(FieldUtils.safeAdd(getValue(), years));
    }

    /**
     * Returns a new instance with the specified number of years added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param years  the amount of years to add, may be negative, null means zero
     * @return the new period plus the specified number of years
     * @throws ArithmeticException if the result overflows an int
     */
    public Years plus(Years years) {
        if (years == null) {
            return this;
        }
        return plus(years.getValue());
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new instance with the specified number of years taken away.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param years  the amount of years to take away, may be negative
     * @return the new period minus the specified number of years
     * @throws ArithmeticException if the result overflows an int
     */
    public Years minus(int years) {
        return plus(FieldUtils.safeNegate(years));
    }

    /**
     * Returns a new instance with the specified number of years taken away.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param years  the amount of years to take away, may be negative, null means zero
     * @return the new period minus the specified number of years
     * @throws ArithmeticException if the result overflows an int
     */
    public Years minus(Years years) {
        if (years == null) {
            return this;
        }
        return minus(years.getValue());
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new instance with the years multiplied by the specified scalar.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param scalar  the amount to multiply by, may be negative
     * @return the new period multiplied by the specified scalar
     * @throws ArithmeticException if the result overflows an int
     */
    public Years multipliedBy(int scalar) {
        return Years.years(FieldUtils.safeMultiply(getValue(), scalar));
    }

    /**
     * Returns a new instance with the years divided by the specified divisor.
     * The calculation uses integer division, thus 3 divided by 2 is 1.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param divisor  the amount to divide by, may be negative
     * @return the new period divided by the specified divisor
     * @throws ArithmeticException if the divisor is zero
     */
    public Years dividedBy(int divisor) {
        if (divisor == 1) {
            return this;
        }
        return Years.years(getValue() / divisor);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new instance with the years value negated.
     *
     * @return the new period with a negated value
     * @throws ArithmeticException if the result overflows an int
     */
    public Years negated() {
        return Years.years(FieldUtils.safeNegate(getValue()));
    }

    //-----------------------------------------------------------------------
    /**
     * Is this years instance greater than the specified number of years.
     *
     * @param other  the other period, null means zero
     * @return true if this years instance is greater than the specified one
     */
    public boolean isGreaterThan(Years other) {
        if (other == null) {
            return getValue() > 0;
        }
        return getValue() > other.getValue();
    }

    /**
     * Is this years instance less than the specified number of years.
     *
     * @param other  the other period, null means zero
     * @return true if this years instance is less than the specified one
     */
    public boolean isLessThan(Years other) {
        if (other == null) {
            return getValue() < 0;
        }
        return getValue() < other.getValue();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets this instance as a String in the ISO8601 duration format.
     * <p>
     * For example, "P4Y" represents 4 years.
     *
     * @return the value as an ISO8601 string
     */
    @ToString
    public String toString() {
        return "P" + String.valueOf(getValue()) + "Y";
    }

}
