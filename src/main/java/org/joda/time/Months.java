/*
 *  Copyright 2001-2010 Stephen Colebourne
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
 * An immutable time period representing a number of months.
 * <p>
 * <code>Months</code> is an immutable period that can only store months.
 * It does not store years, days or hours for example. As such it is a
 * type-safe way of representing a number of months in an application.
 * <p>
 * The number of months is set in the constructor, and may be queried using
 * <code>getMonths()</code>. Basic mathematical operations are provided -
 * <code>plus()</code>, <code>minus()</code>, <code>multipliedBy()</code> and
 * <code>dividedBy()</code>.
 * <p>
 * <code>Months</code> is thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @since 1.4
 */
public final class Months extends BaseSingleFieldPeriod {

    /** Constant representing zero months. */
    public static final Months ZERO = new Months(0);
    /** Constant representing one month. */
    public static final Months ONE = new Months(1);
    /** Constant representing two months. */
    public static final Months TWO = new Months(2);
    /** Constant representing three months. */
    public static final Months THREE = new Months(3);
    /** Constant representing four months. */
    public static final Months FOUR = new Months(4);
    /** Constant representing five months. */
    public static final Months FIVE = new Months(5);
    /** Constant representing six months. */
    public static final Months SIX = new Months(6);
    /** Constant representing seven months. */
    public static final Months SEVEN = new Months(7);
    /** Constant representing eight months. */
    public static final Months EIGHT = new Months(8);
    /** Constant representing nine months. */
    public static final Months NINE = new Months(9);
    /** Constant representing ten months. */
    public static final Months TEN = new Months(10);
    /** Constant representing eleven months. */
    public static final Months ELEVEN = new Months(11);
    /** Constant representing twelve months. */
    public static final Months TWELVE = new Months(12);
    /** Constant representing the maximum number of months that can be stored in this object. */
    public static final Months MAX_VALUE = new Months(Integer.MAX_VALUE);
    /** Constant representing the minimum number of months that can be stored in this object. */
    public static final Months MIN_VALUE = new Months(Integer.MIN_VALUE);

    /** The parser to use for this class. */
    private static final PeriodFormatter PARSER = ISOPeriodFormat.standard().withParseType(PeriodType.months());
    /** Serialization version. */
    private static final long serialVersionUID = 87525275727380867L;

    //-----------------------------------------------------------------------
    /**
     * Obtains an instance of <code>Months</code> that may be cached.
     * <code>Months</code> is immutable, so instances can be cached and shared.
     * This factory method provides access to shared instances.
     *
     * @param months  the number of months to obtain an instance for
     * @return the instance of Months
     */
    public static Months months(int months) {
        switch (months) {
            case 0:
                return ZERO;
            case 1:
                return ONE;
            case 2:
                return TWO;
            case 3:
                return THREE;
            case 4:
                return FOUR;
            case 5:
                return FIVE;
            case 6:
                return SIX;
            case 7:
                return SEVEN;
            case 8:
                return EIGHT;
            case 9:
                return NINE;
            case 10:
                return TEN;
            case 11:
                return ELEVEN;
            case 12:
                return TWELVE;
            case Integer.MAX_VALUE:
                return MAX_VALUE;
            case Integer.MIN_VALUE:
                return MIN_VALUE;
            default:
                return new Months(months);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a <code>Months</code> representing the number of whole months
     * between the two specified datetimes. This method correctly handles
     * any daylight savings time changes that may occur during the interval.
     * <p>
     * This method calculates by adding months to the start date until the result
     * is past the end date. As such, a period from the end of a "long" month to
     * the end of a "short" month is counted as a whole month.
     *
     * @param start  the start instant, must not be null
     * @param end  the end instant, must not be null
     * @return the period in months
     * @throws IllegalArgumentException if the instants are null or invalid
     */
    public static Months monthsBetween(ReadableInstant start, ReadableInstant end) {
        int amount = BaseSingleFieldPeriod.between(start, end, DurationFieldType.months());
        return Months.months(amount);
    }

    /**
     * Creates a <code>Months</code> representing the number of whole months
     * between the two specified partial datetimes.
     * <p>
     * The two partials must contain the same fields, for example you can specify
     * two <code>LocalDate</code> objects.
     * <p>
     * This method calculates by adding months to the start date until the result
     * is past the end date. As such, a period from the end of a "long" month to
     * the end of a "short" month is counted as a whole month.
     *
     * @param start  the start partial date, must not be null
     * @param end  the end partial date, must not be null
     * @return the period in months
     * @throws IllegalArgumentException if the partials are null or invalid
     */
    public static Months monthsBetween(ReadablePartial start, ReadablePartial end) {
        if (start instanceof LocalDate && end instanceof LocalDate)   {
            Chronology chrono = DateTimeUtils.getChronology(start.getChronology());
            int months = chrono.months().getDifference(
                    ((LocalDate) end).getLocalMillis(), ((LocalDate) start).getLocalMillis());
            return Months.months(months);
        }
        int amount = BaseSingleFieldPeriod.between(start, end, ZERO);
        return Months.months(amount);
    }

    /**
     * Creates a <code>Months</code> representing the number of whole months
     * in the specified interval. This method correctly handles any daylight
     * savings time changes that may occur during the interval.
     *
     * @param interval  the interval to extract months from, null returns zero
     * @return the period in months
     * @throws IllegalArgumentException if the partials are null or invalid
     */
    public static Months monthsIn(ReadableInterval interval) {
        if (interval == null)   {
            return Months.ZERO;
        }
        int amount = BaseSingleFieldPeriod.between(interval.getStart(), interval.getEnd(), DurationFieldType.months());
        return Months.months(amount);
    }

    /**
     * Creates a new <code>Months</code> by parsing a string in the ISO8601 format 'PnM'.
     * <p>
     * The parse will accept the full ISO syntax of PnYnMnWnDTnHnMnS however only the
     * months component may be non-zero. If any other component is non-zero, an exception
     * will be thrown.
     *
     * @param periodStr  the period string, null returns zero
     * @return the period in months
     * @throws IllegalArgumentException if the string format is invalid
     */
    @FromString
    public static Months parseMonths(String periodStr) {
        if (periodStr == null) {
            return Months.ZERO;
        }
        Period p = PARSER.parsePeriod(periodStr);
        return Months.months(p.getMonths());
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a new instance representing a number of months.
     * You should consider using the factory method {@link #months(int)}
     * instead of the constructor.
     *
     * @param months  the number of months to represent
     */
    private Months(int months) {
        super(months);
    }

    /**
     * Resolves singletons.
     * 
     * @return the singleton instance
     */
    private Object readResolve() {
        return Months.months(getValue());
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the duration field type, which is <code>months</code>.
     *
     * @return the period type
     */
    public DurationFieldType getFieldType() {
        return DurationFieldType.months();
    }

    /**
     * Gets the period type, which is <code>months</code>.
     *
     * @return the period type
     */
    public PeriodType getPeriodType() {
        return PeriodType.months();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the number of months that this period represents.
     *
     * @return the number of months in the period
     */
    public int getMonths() {
        return getValue();
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new instance with the specified number of months added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param months  the amount of months to add, may be negative
     * @return the new period plus the specified number of months
     * @throws ArithmeticException if the result overflows an int
     */
    public Months plus(int months) {
        if (months == 0) {
            return this;
        }
        return Months.months(FieldUtils.safeAdd(getValue(), months));
    }

    /**
     * Returns a new instance with the specified number of months added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param months  the amount of months to add, may be negative, null means zero
     * @return the new period plus the specified number of months
     * @throws ArithmeticException if the result overflows an int
     */
    public Months plus(Months months) {
        if (months == null) {
            return this;
        }
        return plus(months.getValue());
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new instance with the specified number of months taken away.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param months  the amount of months to take away, may be negative
     * @return the new period minus the specified number of months
     * @throws ArithmeticException if the result overflows an int
     */
    public Months minus(int months) {
        return plus(FieldUtils.safeNegate(months));
    }

    /**
     * Returns a new instance with the specified number of months taken away.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param months  the amount of months to take away, may be negative, null means zero
     * @return the new period minus the specified number of months
     * @throws ArithmeticException if the result overflows an int
     */
    public Months minus(Months months) {
        if (months == null) {
            return this;
        }
        return minus(months.getValue());
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new instance with the months multiplied by the specified scalar.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param scalar  the amount to multiply by, may be negative
     * @return the new period multiplied by the specified scalar
     * @throws ArithmeticException if the result overflows an int
     */
    public Months multipliedBy(int scalar) {
        return Months.months(FieldUtils.safeMultiply(getValue(), scalar));
    }

    /**
     * Returns a new instance with the months divided by the specified divisor.
     * The calculation uses integer division, thus 3 divided by 2 is 1.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param divisor  the amount to divide by, may be negative
     * @return the new period divided by the specified divisor
     * @throws ArithmeticException if the divisor is zero
     */
    public Months dividedBy(int divisor) {
        if (divisor == 1) {
            return this;
        }
        return Months.months(getValue() / divisor);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new instance with the months value negated.
     *
     * @return the new period with a negated value
     * @throws ArithmeticException if the result overflows an int
     */
    public Months negated() {
        return Months.months(FieldUtils.safeNegate(getValue()));
    }

    //-----------------------------------------------------------------------
    /**
     * Is this months instance greater than the specified number of months.
     *
     * @param other  the other period, null means zero
     * @return true if this months instance is greater than the specified one
     */
    public boolean isGreaterThan(Months other) {
        if (other == null) {
            return getValue() > 0;
        }
        return getValue() > other.getValue();
    }

    /**
     * Is this months instance less than the specified number of months.
     *
     * @param other  the other period, null means zero
     * @return true if this months instance is less than the specified one
     */
    public boolean isLessThan(Months other) {
        if (other == null) {
            return getValue() < 0;
        }
        return getValue() < other.getValue();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets this instance as a String in the ISO8601 duration format.
     * <p>
     * For example, "P4M" represents 4 months.
     *
     * @return the value as an ISO8601 string
     */
    @ToString
    public String toString() {
        return "P" + String.valueOf(getValue()) + "M";
    }

}
