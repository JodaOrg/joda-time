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
 * An immutable time period representing a number of weeks.
 * <p>
 * <code>Weeks</code> is an immutable period that can only store weeks.
 * It does not store years, months or hours for example. As such it is a
 * type-safe way of representing a number of weeks in an application.
 * <p>
 * The number of weeks is set in the constructor, and may be queried using
 * <code>getWeeks()</code>. Basic mathematical operations are provided -
 * <code>plus()</code>, <code>minus()</code>, <code>multipliedBy()</code> and
 * <code>dividedBy()</code>.
 * <p>
 * <code>Weeks</code> is thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @since 1.4
 */
public final class Weeks extends BaseSingleFieldPeriod {

    /** Constant representing zero weeks. */
    public static final Weeks ZERO = new Weeks(0);
    /** Constant representing one week. */
    public static final Weeks ONE = new Weeks(1);
    /** Constant representing two weeks. */
    public static final Weeks TWO = new Weeks(2);
    /** Constant representing three weeks. */
    public static final Weeks THREE = new Weeks(3);
    /** Constant representing the maximum number of weeks that can be stored in this object. */
    public static final Weeks MAX_VALUE = new Weeks(Integer.MAX_VALUE);
    /** Constant representing the minimum number of weeks that can be stored in this object. */
    public static final Weeks MIN_VALUE = new Weeks(Integer.MIN_VALUE);

    /** The parser to use for this class. */
    private static final PeriodFormatter PARSER = ISOPeriodFormat.standard().withParseType(PeriodType.weeks());
    /** Serialization version. */
    private static final long serialVersionUID = 87525275727380866L;

    //-----------------------------------------------------------------------
    /**
     * Obtains an instance of <code>Weeks</code> that may be cached.
     * <code>Weeks</code> is immutable, so instances can be cached and shared.
     * This factory method provides access to shared instances.
     *
     * @param weeks  the number of weeks to obtain an instance for
     * @return the instance of Weeks
     */
    public static Weeks weeks(int weeks) {
        switch (weeks) {
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
                return new Weeks(weeks);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a <code>Weeks</code> representing the number of whole weeks
     * between the two specified datetimes.
     *
     * @param start  the start instant, must not be null
     * @param end  the end instant, must not be null
     * @return the period in weeks
     * @throws IllegalArgumentException if the instants are null or invalid
     */
    public static Weeks weeksBetween(ReadableInstant start, ReadableInstant end) {
        int amount = BaseSingleFieldPeriod.between(start, end, DurationFieldType.weeks());
        return Weeks.weeks(amount);
    }

    /**
     * Creates a <code>Weeks</code> representing the number of whole weeks
     * between the two specified partial datetimes.
     * <p>
     * The two partials must contain the same fields, for example you can specify
     * two <code>LocalDate</code> objects.
     *
     * @param start  the start partial date, must not be null
     * @param end  the end partial date, must not be null
     * @return the period in weeks
     * @throws IllegalArgumentException if the partials are null or invalid
     */
    public static Weeks weeksBetween(ReadablePartial start, ReadablePartial end) {
        if (start instanceof LocalDate && end instanceof LocalDate)   {
            Chronology chrono = DateTimeUtils.getChronology(start.getChronology());
            int weeks = chrono.weeks().getDifference(
                    ((LocalDate) end).getLocalMillis(), ((LocalDate) start).getLocalMillis());
            return Weeks.weeks(weeks);
        }
        int amount = BaseSingleFieldPeriod.between(start, end, ZERO);
        return Weeks.weeks(amount);
    }

    /**
     * Creates a <code>Weeks</code> representing the number of whole weeks
     * in the specified interval.
     *
     * @param interval  the interval to extract weeks from, null returns zero
     * @return the period in weeks
     * @throws IllegalArgumentException if the partials are null or invalid
     */
    public static Weeks weeksIn(ReadableInterval interval) {
        if (interval == null)   {
            return Weeks.ZERO;
        }
        int amount = BaseSingleFieldPeriod.between(interval.getStart(), interval.getEnd(), DurationFieldType.weeks());
        return Weeks.weeks(amount);
    }

    /**
     * Creates a new <code>Weeks</code> representing the number of complete
     * standard length weeks in the specified period.
     * <p>
     * This factory method converts all fields from the period to hours using standardised
     * durations for each field. Only those fields which have a precise duration in
     * the ISO UTC chronology can be converted.
     * <ul>
     * <li>One week consists of 7 days.
     * <li>One day consists of 24 hours.
     * <li>One hour consists of 60 minutes.
     * <li>One minute consists of 60 weeks.
     * <li>One second consists of 1000 milliseconds.
     * </ul>
     * Months and Years are imprecise and periods containing these values cannot be converted.
     *
     * @param period  the period to get the number of hours from, null returns zero
     * @return the period in weeks
     * @throws IllegalArgumentException if the period contains imprecise duration values
     */
    public static Weeks standardWeeksIn(ReadablePeriod period) {
        int amount = BaseSingleFieldPeriod.standardPeriodIn(period, DateTimeConstants.MILLIS_PER_WEEK);
        return Weeks.weeks(amount);
    }

    /**
     * Creates a new <code>Weeks</code> by parsing a string in the ISO8601 format 'PnW'.
     * <p>
     * The parse will accept the full ISO syntax of PnYnMnWnDTnHnMnS however only the
     * weeks component may be non-zero. If any other component is non-zero, an exception
     * will be thrown.
     *
     * @param periodStr  the period string, null returns zero
     * @return the period in weeks
     * @throws IllegalArgumentException if the string format is invalid
     */
    @FromString
    public static Weeks parseWeeks(String periodStr) {
        if (periodStr == null) {
            return Weeks.ZERO;
        }
        Period p = PARSER.parsePeriod(periodStr);
        return Weeks.weeks(p.getWeeks());
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a new instance representing a number of weeks.
     * You should consider using the factory method {@link #weeks(int)}
     * instead of the constructor.
     *
     * @param weeks  the number of weeks to represent
     */
    private Weeks(int weeks) {
        super(weeks);
    }

    /**
     * Resolves singletons.
     * 
     * @return the singleton instance
     */
    private Object readResolve() {
        return Weeks.weeks(getValue());
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the duration field type, which is <code>weeks</code>.
     *
     * @return the period type
     */
    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.weeks();
    }

    /**
     * Gets the period type, which is <code>weeks</code>.
     *
     * @return the period type
     */
    @Override
    public PeriodType getPeriodType() {
        return PeriodType.weeks();
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this period in weeks to a period in days assuming a
     * 7 day week.
     * <p>
     * This method allows you to convert between different types of period.
     * However to achieve this it makes the assumption that all weeks are
     * 7 days long.
     * This may not be true for some unusual chronologies. However, it is included
     * as it is a useful operation for many applications and business rules.
     * 
     * @return a period representing the number of days for this number of weeks
     * @throws ArithmeticException if the number of days is too large to be represented
     */
    public Days toStandardDays() {
        return Days.days(FieldUtils.safeMultiply(getValue(), DateTimeConstants.DAYS_PER_WEEK));
    }

    /**
     * Converts this period in weeks to a period in hours assuming a
     * 7 day week and 24 hour day.
     * <p>
     * This method allows you to convert between different types of period.
     * However to achieve this it makes the assumption that all weeks are
     * 7 days long and all days are 24 hours long.
     * This is not true when daylight savings is considered and may also not
     * be true for some unusual chronologies. However, it is included
     * as it is a useful operation for many applications and business rules.
     * 
     * @return a period representing the number of hours for this number of weeks
     * @throws ArithmeticException if the number of hours is too large to be represented
     */
    public Hours toStandardHours() {
        return Hours.hours(FieldUtils.safeMultiply(getValue(), DateTimeConstants.HOURS_PER_WEEK));
    }

    /**
     * Converts this period in weeks to a period in minutes assuming a
     * 7 day week, 24 hour day and 60 minute hour.
     * <p>
     * This method allows you to convert between different types of period.
     * However to achieve this it makes the assumption that all weeks are
     * 7 days long, all days are 24 hours long and all hours are 60 minutes long.
     * This is not true when daylight savings is considered and may also not
     * be true for some unusual chronologies. However, it is included
     * as it is a useful operation for many applications and business rules.
     * 
     * @return a period representing the number of minutes for this number of weeks
     * @throws ArithmeticException if the number of minutes is too large to be represented
     */
    public Minutes toStandardMinutes() {
        return Minutes.minutes(FieldUtils.safeMultiply(getValue(), DateTimeConstants.MINUTES_PER_WEEK));
    }

    /**
     * Converts this period in weeks to a period in seconds assuming a
     * 7 day week, 24 hour day, 60 minute hour and 60 second minute.
     * <p>
     * This method allows you to convert between different types of period.
     * However to achieve this it makes the assumption that all weeks are
     * 7 days long, all days are 24 hours long, all hours are 60 minutes long
     * and all minutes are 60 seconds long.
     * This is not true when daylight savings is considered and may also not
     * be true for some unusual chronologies. However, it is included
     * as it is a useful operation for many applications and business rules.
     * 
     * @return a period representing the number of seconds for this number of weeks
     * @throws ArithmeticException if the number of seconds is too large to be represented
     */
    public Seconds toStandardSeconds() {
        return Seconds.seconds(FieldUtils.safeMultiply(getValue(), DateTimeConstants.SECONDS_PER_WEEK));
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this period in weeks to a duration in milliweeks assuming a
     * 7 day week, 24 hour day, 60 minute hour and 60 second minute.
     * <p>
     * This method allows you to convert from a period to a duration.
     * However to achieve this it makes the assumption that all weeks are
     * 7 days long, all days are 24 hours long, all hours are 60 minutes long
     * and all minutes are 60 seconds long.
     * This is not true when daylight savings time is considered, and may also
     * not be true for some unusual chronologies. However, it is included as it
     * is a useful operation for many applications and business rules.
     * 
     * @return a duration equivalent to this number of weeks
     */
    public Duration toStandardDuration() {
        long weeks = getValue();  // assign to a long
        return new Duration(weeks * DateTimeConstants.MILLIS_PER_WEEK);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the number of weeks that this period represents.
     *
     * @return the number of weeks in the period
     */
    public int getWeeks() {
        return getValue();
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new instance with the specified number of weeks added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param weeks  the amount of weeks to add, may be negative
     * @return the new period plus the specified number of weeks
     * @throws ArithmeticException if the result overflows an int
     */
    public Weeks plus(int weeks) {
        if (weeks == 0) {
            return this;
        }
        return Weeks.weeks(FieldUtils.safeAdd(getValue(), weeks));
    }

    /**
     * Returns a new instance with the specified number of weeks added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param weeks  the amount of weeks to add, may be negative, null means zero
     * @return the new period plus the specified number of weeks
     * @throws ArithmeticException if the result overflows an int
     */
    public Weeks plus(Weeks weeks) {
        if (weeks == null) {
            return this;
        }
        return plus(weeks.getValue());
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new instance with the specified number of weeks taken away.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param weeks  the amount of weeks to take away, may be negative
     * @return the new period minus the specified number of weeks
     * @throws ArithmeticException if the result overflows an int
     */
    public Weeks minus(int weeks) {
        return plus(FieldUtils.safeNegate(weeks));
    }

    /**
     * Returns a new instance with the specified number of weeks taken away.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param weeks  the amount of weeks to take away, may be negative, null means zero
     * @return the new period minus the specified number of weeks
     * @throws ArithmeticException if the result overflows an int
     */
    public Weeks minus(Weeks weeks) {
        if (weeks == null) {
            return this;
        }
        return minus(weeks.getValue());
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new instance with the weeks multiplied by the specified scalar.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param scalar  the amount to multiply by, may be negative
     * @return the new period multiplied by the specified scalar
     * @throws ArithmeticException if the result overflows an int
     */
    public Weeks multipliedBy(int scalar) {
        return Weeks.weeks(FieldUtils.safeMultiply(getValue(), scalar));
    }

    /**
     * Returns a new instance with the weeks divided by the specified divisor.
     * The calculation uses integer division, thus 3 divided by 2 is 1.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param divisor  the amount to divide by, may be negative
     * @return the new period divided by the specified divisor
     * @throws ArithmeticException if the divisor is zero
     */
    public Weeks dividedBy(int divisor) {
        if (divisor == 1) {
            return this;
        }
        return Weeks.weeks(getValue() / divisor);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new instance with the weeks value negated.
     *
     * @return the new period with a negated value
     * @throws ArithmeticException if the result overflows an int
     */
    public Weeks negated() {
        return Weeks.weeks(FieldUtils.safeNegate(getValue()));
    }

    //-----------------------------------------------------------------------
    /**
     * Is this weeks instance greater than the specified number of weeks.
     *
     * @param other  the other period, null means zero
     * @return true if this weeks instance is greater than the specified one
     */
    public boolean isGreaterThan(Weeks other) {
        if (other == null) {
            return getValue() > 0;
        }
        return getValue() > other.getValue();
    }

    /**
     * Is this weeks instance less than the specified number of weeks.
     *
     * @param other  the other period, null means zero
     * @return true if this weeks instance is less than the specified one
     */
    public boolean isLessThan(Weeks other) {
        if (other == null) {
            return getValue() < 0;
        }
        return getValue() < other.getValue();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets this instance as a String in the ISO8601 duration format.
     * <p>
     * For example, "P4W" represents 4 weeks.
     *
     * @return the value as an ISO8601 string
     */
    @Override
    @ToString
    public String toString() {
        return "P" + String.valueOf(getValue()) + "W";
    }

}
