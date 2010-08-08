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
package org.joda.time;

import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

/**
 * An immutable time period representing a number of seconds.
 * <p>
 * <code>Seconds</code> is an immutable period that can only store seconds.
 * It does not store years, months or hours for example. As such it is a
 * type-safe way of representing a number of seconds in an application.
 * <p>
 * The number of seconds is set in the constructor, and may be queried using
 * <code>getSeconds()</code>. Basic mathematical operations are provided -
 * <code>plus()</code>, <code>minus()</code>, <code>multipliedBy()</code> and
 * <code>dividedBy()</code>.
 * <p>
 * <code>Seconds</code> is thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @since 1.4
 */
public final class Seconds extends BaseSingleFieldPeriod {

    /** Constant representing zero seconds. */
    public static final Seconds ZERO = new Seconds(0);
    /** Constant representing one second. */
    public static final Seconds ONE = new Seconds(1);
    /** Constant representing two seconds. */
    public static final Seconds TWO = new Seconds(2);
    /** Constant representing three seconds. */
    public static final Seconds THREE = new Seconds(3);
    /** Constant representing the maximum number of seconds that can be stored in this object. */
    public static final Seconds MAX_VALUE = new Seconds(Integer.MAX_VALUE);
    /** Constant representing the minimum number of seconds that can be stored in this object. */
    public static final Seconds MIN_VALUE = new Seconds(Integer.MIN_VALUE);

    /** The paser to use for this class. */
    private static final PeriodFormatter PARSER = ISOPeriodFormat.standard().withParseType(PeriodType.seconds());
    /** Serialization version. */
    private static final long serialVersionUID = 87525275727380862L;

    //-----------------------------------------------------------------------
    /**
     * Obtains an instance of <code>Seconds</code> that may be cached.
     * <code>Seconds</code> is immutable, so instances can be cached and shared.
     * This factory method provides access to shared instances.
     *
     * @param seconds  the number of seconds to obtain an instance for
     * @return the instance of Seconds
     */
    public static Seconds seconds(int seconds) {
        switch (seconds) {
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
                return new Seconds(seconds);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a <code>Seconds</code> representing the number of whole seconds
     * between the two specified datetimes.
     *
     * @param start  the start instant, must not be null
     * @param end  the end instant, must not be null
     * @return the period in seconds
     * @throws IllegalArgumentException if the instants are null or invalid
     */
    public static Seconds secondsBetween(ReadableInstant start, ReadableInstant end) {
        int amount = BaseSingleFieldPeriod.between(start, end, DurationFieldType.seconds());
        return Seconds.seconds(amount);
    }

    /**
     * Creates a <code>Seconds</code> representing the number of whole seconds
     * between the two specified partial datetimes.
     * <p>
     * The two partials must contain the same fields, for example you can specify
     * two <code>LocalTime</code> objects.
     *
     * @param start  the start partial date, must not be null
     * @param end  the end partial date, must not be null
     * @return the period in seconds
     * @throws IllegalArgumentException if the partials are null or invalid
     */
    public static Seconds secondsBetween(ReadablePartial start, ReadablePartial end) {
        if (start instanceof LocalTime && end instanceof LocalTime)   {
            Chronology chrono = DateTimeUtils.getChronology(start.getChronology());
            int seconds = chrono.seconds().getDifference(
                    ((LocalTime) end).getLocalMillis(), ((LocalTime) start).getLocalMillis());
            return Seconds.seconds(seconds);
        }
        int amount = BaseSingleFieldPeriod.between(start, end, ZERO);
        return Seconds.seconds(amount);
    }

    /**
     * Creates a <code>Seconds</code> representing the number of whole seconds
     * in the specified interval.
     *
     * @param interval  the interval to extract seconds from, null returns zero
     * @return the period in seconds
     * @throws IllegalArgumentException if the partials are null or invalid
     */
    public static Seconds secondsIn(ReadableInterval interval) {
        if (interval == null)   {
            return Seconds.ZERO;
        }
        int amount = BaseSingleFieldPeriod.between(interval.getStart(), interval.getEnd(), DurationFieldType.seconds());
        return Seconds.seconds(amount);
    }

    /**
     * Creates a new <code>Seconds</code> representing the number of complete
     * standard length seconds in the specified period.
     * <p>
     * This factory method converts all fields from the period to hours using standardised
     * durations for each field. Only those fields which have a precise duration in
     * the ISO UTC chronology can be converted.
     * <ul>
     * <li>One week consists of 7 seconds.
     * <li>One day consists of 24 hours.
     * <li>One hour consists of 60 minutes.
     * <li>One minute consists of 60 seconds.
     * <li>One second consists of 1000 milliseconds.
     * </ul>
     * Months and Years are imprecise and periods containing these values cannot be converted.
     *
     * @param period  the period to get the number of hours from, null returns zero
     * @return the period in seconds
     * @throws IllegalArgumentException if the period contains imprecise duration values
     */
    public static Seconds standardSecondsIn(ReadablePeriod period) {
        int amount = BaseSingleFieldPeriod.standardPeriodIn(period, DateTimeConstants.MILLIS_PER_SECOND);
        return Seconds.seconds(amount);
    }

    /**
     * Creates a new <code>Seconds</code> by parsing a string in the ISO8601 format 'PTnS'.
     * <p>
     * The parse will accept the full ISO syntax of PnYnMnWnDTnHnMnS however only the
     * seconds component may be non-zero. If any other component is non-zero, an exception
     * will be thrown.
     *
     * @param periodStr  the period string, null returns zero
     * @return the period in seconds
     * @throws IllegalArgumentException if the string format is invalid
     */
    public static Seconds parseSeconds(String periodStr) {
        if (periodStr == null) {
            return Seconds.ZERO;
        }
        Period p = PARSER.parsePeriod(periodStr);
        return Seconds.seconds(p.getSeconds());
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a new instance representing a number of seconds.
     * You should consider using the factory method {@link #seconds(int)}
     * instead of the constructor.
     *
     * @param seconds  the number of seconds to represent
     */
    private Seconds(int seconds) {
        super(seconds);
    }

    /**
     * Resolves singletons.
     * 
     * @return the singleton instance
     */
    private Object readResolve() {
        return Seconds.seconds(getValue());
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the duration field type, which is <code>seconds</code>.
     *
     * @return the period type
     */
    public DurationFieldType getFieldType() {
        return DurationFieldType.seconds();
    }

    /**
     * Gets the period type, which is <code>seconds</code>.
     *
     * @return the period type
     */
    public PeriodType getPeriodType() {
        return PeriodType.seconds();
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this period in seconds to a period in weeks assuming a
     * 7 day week, 24 hour day, 60 minute hour and 60 second minute.
     * <p>
     * This method allows you to convert between different types of period.
     * However to achieve this it makes the assumption that all weeks are 7 days
     * long, all days are 24 hours long, all hours are 60 minutes long and
     * all minutes are 60 seconds long.
     * This is not true when daylight savings time is considered, and may also
     * not be true for some unusual chronologies. However, it is included as it
     * is a useful operation for many applications and business rules.
     * 
     * @return a period representing the number of whole weeks for this number of seconds
     */
    public Weeks toStandardWeeks() {
        return Weeks.weeks(getValue() / DateTimeConstants.SECONDS_PER_WEEK);
    }

    /**
     * Converts this period in seconds to a period in days assuming a
     * 24 hour day, 60 minute hour and 60 second minute.
     * <p>
     * This method allows you to convert between different types of period.
     * However to achieve this it makes the assumption that all days are 24 hours
     * long, all hours are 60 minutes long and all minutes are 60 seconds long.
     * This is not true when daylight savings is considered and may also not
     * be true for some unusual chronologies. However, it is included
     * as it is a useful operation for many applications and business rules.
     * 
     * @return a period representing the number of days for this number of seconds
     */
    public Days toStandardDays() {
        return Days.days(getValue() / DateTimeConstants.SECONDS_PER_DAY);
    }

    /**
     * Converts this period in seconds to a period in hours assuming a
     * 60 minute hour and 60 second minute.
     * <p>
     * This method allows you to convert between different types of period.
     * However to achieve this it makes the assumption that all hours are
     * 60 minutes long and all minutes are 60 seconds long.
     * This may not be true for some unusual chronologies. However, it is included
     * as it is a useful operation for many applications and business rules.
     * 
     * @return a period representing the number of hours for this number of seconds
     */
    public Hours toStandardHours() {
        return Hours.hours(getValue() / DateTimeConstants.SECONDS_PER_HOUR);
    }

    /**
     * Converts this period in seconds to a period in minutes assuming a
     * 60 second minute.
     * <p>
     * This method allows you to convert between different types of period.
     * However to achieve this it makes the assumption that all minutes are
     * 60 seconds long.
     * This may not be true for some unusual chronologies. However, it is included
     * as it is a useful operation for many applications and business rules.
     * 
     * @return a period representing the number of minutes for this number of seconds
     */
    public Minutes toStandardMinutes() {
        return Minutes.minutes(getValue() / DateTimeConstants.SECONDS_PER_MINUTE);
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this period in seconds to a duration in milliseconds assuming a
     * 24 hour day, 60 minute hour and 60 second minute.
     * <p>
     * This method allows you to convert from a period to a duration.
     * However to achieve this it makes the assumption that all seconds are 24 hours
     * long, all hours are 60 minutes and all minutes are 60 seconds.
     * This is not true when daylight savings time is considered, and may also
     * not be true for some unusual chronologies. However, it is included as it
     * is a useful operation for many applications and business rules.
     * 
     * @return a duration equivalent to this number of seconds
     */
    public Duration toStandardDuration() {
        long seconds = getValue();  // assign to a long
        return new Duration(seconds * DateTimeConstants.MILLIS_PER_SECOND);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the number of seconds that this period represents.
     *
     * @return the number of seconds in the period
     */
    public int getSeconds() {
        return getValue();
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new instance with the specified number of seconds added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param seconds  the amount of seconds to add, may be negative
     * @return the new period plus the specified number of seconds
     * @throws ArithmeticException if the result overflows an int
     */
    public Seconds plus(int seconds) {
        if (seconds == 0) {
            return this;
        }
        return Seconds.seconds(FieldUtils.safeAdd(getValue(), seconds));
    }

    /**
     * Returns a new instance with the specified number of seconds added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param seconds  the amount of seconds to add, may be negative, null means zero
     * @return the new period plus the specified number of seconds
     * @throws ArithmeticException if the result overflows an int
     */
    public Seconds plus(Seconds seconds) {
        if (seconds == null) {
            return this;
        }
        return plus(seconds.getValue());
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new instance with the specified number of seconds taken away.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param seconds  the amount of seconds to take away, may be negative
     * @return the new period minus the specified number of seconds
     * @throws ArithmeticException if the result overflows an int
     */
    public Seconds minus(int seconds) {
        return plus(FieldUtils.safeNegate(seconds));
    }

    /**
     * Returns a new instance with the specified number of seconds taken away.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param seconds  the amount of seconds to take away, may be negative, null means zero
     * @return the new period minus the specified number of seconds
     * @throws ArithmeticException if the result overflows an int
     */
    public Seconds minus(Seconds seconds) {
        if (seconds == null) {
            return this;
        }
        return minus(seconds.getValue());
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new instance with the seconds multiplied by the specified scalar.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param scalar  the amount to multiply by, may be negative
     * @return the new period multiplied by the specified scalar
     * @throws ArithmeticException if the result overflows an int
     */
    public Seconds multipliedBy(int scalar) {
        return Seconds.seconds(FieldUtils.safeMultiply(getValue(), scalar));
    }

    /**
     * Returns a new instance with the seconds divided by the specified divisor.
     * The calculation uses integer division, thus 3 divided by 2 is 1.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param divisor  the amount to divide by, may be negative
     * @return the new period divided by the specified divisor
     * @throws ArithmeticException if the divisor is zero
     */
    public Seconds dividedBy(int divisor) {
        if (divisor == 1) {
            return this;
        }
        return Seconds.seconds(getValue() / divisor);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new instance with the seconds value negated.
     *
     * @return the new period with a negated value
     * @throws ArithmeticException if the result overflows an int
     */
    public Seconds negated() {
        return Seconds.seconds(FieldUtils.safeNegate(getValue()));
    }

    //-----------------------------------------------------------------------
    /**
     * Is this seconds instance greater than the specified number of seconds.
     *
     * @param other  the other period, null means zero
     * @return true if this seconds instance is greater than the specified one
     */
    public boolean isGreaterThan(Seconds other) {
        if (other == null) {
            return getValue() > 0;
        }
        return getValue() > other.getValue();
    }

    /**
     * Is this seconds instance less than the specified number of seconds.
     *
     * @param other  the other period, null means zero
     * @return true if this seconds instance is less than the specified one
     */
    public boolean isLessThan(Seconds other) {
        if (other == null) {
            return getValue() < 0;
        }
        return getValue() < other.getValue();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets this instance as a String in the ISO8601 duration format.
     * <p>
     * For example, "PT4S" represents 4 seconds.
     *
     * @return the value as an ISO8601 string
     */
    public String toString() {
        return "PT" + String.valueOf(getValue()) + "S";
    }

}
