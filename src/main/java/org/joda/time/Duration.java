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

import java.io.Serializable;
import java.math.RoundingMode;

import org.joda.convert.FromString;
import org.joda.time.base.BaseDuration;
import org.joda.time.field.FieldUtils;

/**
 * An immutable duration specifying a length of time in milliseconds.
 * <p>
 * A duration is defined by a fixed number of milliseconds.
 * There is no concept of fields, such as days or seconds, as these fields can vary in length.
 * <p>
 * A duration may be converted to a {@link Period} to obtain field values.
 * This conversion will typically cause a loss of precision.
 * <p>
 * Duration is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public final class Duration
        extends BaseDuration
        implements ReadableDuration, Serializable {

    /** Constant representing zero millisecond duration */
    public static final Duration ZERO = new Duration(0L);

    /** Serialization version */
    private static final long serialVersionUID = 2471658376918L;

    //-----------------------------------------------------------------------
    /**
     * Parses a {@code Duration} from the specified string.
     * <p>
     * This parses the format {@code PTa.bS}, as per {@link #toString()}.
     * 
     * @param str  the string to parse, not null
     * @since 2.0
     */
    @FromString
    public static Duration parse(String str) {
        return new Duration(str);
    }

    //-----------------------------------------------------------------------
    /**
     * Create a duration with the specified number of days assuming that
     * there are the standard number of milliseconds in a day.
     * <p>
     * This method assumes that there are 24 hours in a day,
     * 60 minutes in an hour, 60 seconds in a minute and 1000 milliseconds in
     * a second. This will be true for most days, however days with Daylight
     * Savings changes will not have 24 hours, so use this method with care.
     * <p>
     * A Duration is a representation of an amount of time. If you want to express
     * the concept of 'days' you should consider using the {@link Days} class.
     *
     * @param days  the number of standard days in this duration
     * @return the duration, never null
     * @throws ArithmeticException if the days value is too large
     * @since 1.6
     */
    public static Duration standardDays(long days) {
        if (days == 0) {
            return ZERO;
        }
        return new Duration(FieldUtils.safeMultiply(days, DateTimeConstants.MILLIS_PER_DAY));
    }

    /**
     * Create a duration with the specified number of hours assuming that
     * there are the standard number of milliseconds in an hour.
     * <p>
     * This method assumes that there are 60 minutes in an hour,
     * 60 seconds in a minute and 1000 milliseconds in a second.
     * All currently supplied chronologies use this definition.
     * <p>
     * A Duration is a representation of an amount of time. If you want to express
     * the concept of 'hours' you should consider using the {@link Hours} class.
     *
     * @param hours  the number of standard hours in this duration
     * @return the duration, never null
     * @throws ArithmeticException if the hours value is too large
     * @since 1.6
     */
    public static Duration standardHours(long hours) {
        if (hours == 0) {
            return ZERO;
        }
        return new Duration(FieldUtils.safeMultiply(hours, DateTimeConstants.MILLIS_PER_HOUR));
    }

    /**
     * Create a duration with the specified number of minutes assuming that
     * there are the standard number of milliseconds in a minute.
     * <p>
     * This method assumes that there are 60 seconds in a minute and
     * 1000 milliseconds in a second.
     * All currently supplied chronologies use this definition.
     * <p>
     * A Duration is a representation of an amount of time. If you want to express
     * the concept of 'minutes' you should consider using the {@link Minutes} class.
     *
     * @param minutes  the number of standard minutes in this duration
     * @return the duration, never null
     * @throws ArithmeticException if the minutes value is too large
     * @since 1.6
     */
    public static Duration standardMinutes(long minutes) {
        if (minutes == 0) {
            return ZERO;
        }
        return new Duration(FieldUtils.safeMultiply(minutes, DateTimeConstants.MILLIS_PER_MINUTE));
    }

    /**
     * Create a duration with the specified number of seconds assuming that
     * there are the standard number of milliseconds in a second.
     * <p>
     * This method assumes that there are 1000 milliseconds in a second.
     * All currently supplied chronologies use this definition.
     * <p>
     * A Duration is a representation of an amount of time. If you want to express
     * the concept of 'seconds' you should consider using the {@link Seconds} class.
     *
     * @param seconds  the number of standard seconds in this duration
     * @return the duration, never null
     * @throws ArithmeticException if the seconds value is too large
     * @since 1.6
     */
    public static Duration standardSeconds(long seconds) {
        if (seconds == 0) {
            return ZERO;
        }
        return new Duration(FieldUtils.safeMultiply(seconds, DateTimeConstants.MILLIS_PER_SECOND));
    }

    /**
     * Create a duration with the specified number of milliseconds.
     *
     * @param millis  the number of standard milliseconds in this duration
     * @return the duration, never null
     * @since 2.0
     */
    public static Duration millis(long millis) {
        if (millis == 0) {
            return ZERO;
        }
        return new Duration(millis);
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a duration from the given millisecond duration.
     *
     * @param duration  the duration, in milliseconds
     */
    public Duration(long duration) {
        super(duration);
    }

    /**
     * Creates a duration from the given interval endpoints.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     * @throws ArithmeticException if the duration exceeds a 64-bit long
     */
    public Duration(long startInstant, long endInstant) {
        super(startInstant, endInstant);
    }

    /**
     * Creates a duration from the given interval endpoints.
     *
     * @param start  interval start, null means now
     * @param end  interval end, null means now
     * @throws ArithmeticException if the duration exceeds a 64-bit long
     */
    public Duration(ReadableInstant start, ReadableInstant end) {
        super(start, end);
    }

    /**
     * Creates a duration from the specified object using the
     * {@link org.joda.time.convert.ConverterManager ConverterManager}.
     *
     * @param duration  duration to convert
     * @throws IllegalArgumentException if duration is invalid
     */
    public Duration(Object duration) {
        super(duration);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the length of this duration in days assuming that there are the
     * standard number of milliseconds in a day.
     * <p>
     * This method assumes that there are 24 hours in a day,
     * 60 minutes in an hour, 60 seconds in a minute and 1000 milliseconds in
     * a second. This will be true for most days, however days with Daylight
     * Savings changes will not have 24 hours, so use this method with care.
     * <p>
     * This returns <code>getMillis() / MILLIS_PER_DAY</code>.
     * The result is an integer division, thus excess milliseconds are truncated.
     *
     * @return the length of the duration in standard days
     * @since 2.0
     */
    public long getStandardDays() {
        return getMillis() / DateTimeConstants.MILLIS_PER_DAY;
    }

    /**
     * Gets the length of this duration in hours assuming that there are the
     * standard number of milliseconds in an hour.
     * <p>
     * This method assumes that there are 60 minutes in an hour,
     * 60 seconds in a minute and 1000 milliseconds in a second.
     * All currently supplied chronologies use this definition.
     * <p>
     * This returns <code>getMillis() / MILLIS_PER_HOUR</code>.
     * The result is an integer division, thus excess milliseconds are truncated.
     *
     * @return the length of the duration in standard hours
     * @since 2.0
     */
    public long getStandardHours() {
        return getMillis() / DateTimeConstants.MILLIS_PER_HOUR;
    }

    /**
     * Gets the length of this duration in minutes assuming that there are the
     * standard number of milliseconds in a minute.
     * <p>
     * This method assumes that there are 60 seconds in a minute and
     * 1000 milliseconds in a second.
     * All currently supplied chronologies use this definition.
     * <p>
     * This returns <code>getMillis() / MILLIS_PER_MINUTE</code>.
     * The result is an integer division, thus excess milliseconds are truncated.
     *
     * @return the length of the duration in standard minutes
     * @since 2.0
     */
    public long getStandardMinutes() {
        return getMillis() / DateTimeConstants.MILLIS_PER_MINUTE;
    }

    /**
     * Gets the length of this duration in seconds assuming that there are the
     * standard number of milliseconds in a second.
     * <p>
     * This method assumes that there are 1000 milliseconds in a second.
     * All currently supplied chronologies use this definition.
     * <p>
     * This returns <code>getMillis() / 1000</code>.
     * The result is an integer division, so 2999 millis returns 2 seconds.
     *
     * @return the length of the duration in standard seconds
     * @since 1.6
     */
    public long getStandardSeconds() {
        return getMillis() / DateTimeConstants.MILLIS_PER_SECOND;
    }

    //-----------------------------------------------------------------------
    /**
     * Get this duration as an immutable <code>Duration</code> object
     * by returning <code>this</code>.
     * 
     * @return <code>this</code>
     */
    public Duration toDuration() {
        return this;
    }

    /**
     * Converts this duration to a period in days assuming that there are the
     * standard number of milliseconds in a day.
     * <p>
     * This method assumes that there are 24 hours in a day,
     * 60 minutes in an hour, 60 seconds in a minute and 1000 milliseconds in
     * a second. This will be true for most days, however days with Daylight
     * Savings changes will not have 24 hours, so use this method with care.
     * 
     * @return a period representing the number of standard days in this period, never null
     * @throws ArithmeticException if the number of days is too large to be represented
     * @since 2.0
     */
    public Days toStandardDays() {
        long days = getStandardDays();
        return Days.days(FieldUtils.safeToInt(days));
    }

    /**
     * Converts this duration to a period in hours assuming that there are the
     * standard number of milliseconds in an hour.
     * <p>
     * This method assumes that there are 60 minutes in an hour,
     * 60 seconds in a minute and 1000 milliseconds in a second.
     * All currently supplied chronologies use this definition.
     * 
     * @return a period representing the number of standard hours in this period, never null
     * @throws ArithmeticException if the number of hours is too large to be represented
     * @since 2.0
     */
    public Hours toStandardHours() {
        long hours = getStandardHours();
        return Hours.hours(FieldUtils.safeToInt(hours));
    }

    /**
     * Converts this duration to a period in minutes assuming that there are the
     * standard number of milliseconds in a minute.
     * <p>
     * This method assumes that there are 60 seconds in a minute and
     * 1000 milliseconds in a second.
     * All currently supplied chronologies use this definition.
     * 
     * @return a period representing the number of standard minutes in this period, never null
     * @throws ArithmeticException if the number of minutes is too large to be represented
     * @since 2.0
     */
    public Minutes toStandardMinutes() {
        long minutes = getStandardMinutes();
        return Minutes.minutes(FieldUtils.safeToInt(minutes));
    }

    /**
     * Converts this duration to a period in seconds assuming that there are the
     * standard number of milliseconds in a second.
     * <p>
     * This method assumes that there are 1000 milliseconds in a second.
     * All currently supplied chronologies use this definition.
     * 
     * @return a period representing the number of standard seconds in this period, never null
     * @throws ArithmeticException if the number of seconds is too large to be represented
     * @since 1.6
     */
    public Seconds toStandardSeconds() {
        long seconds = getStandardSeconds();
        return Seconds.seconds(FieldUtils.safeToInt(seconds));
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a new Duration instance with a different millisecond length.
     * 
     * @param duration  the new length of the duration
     * @return the new duration instance
     */
    public Duration withMillis(long duration) {
        if (duration == getMillis()) {
            return this;
        }
        return new Duration(duration);
    }

    /**
     * Returns a new duration with this length plus that specified multiplied by the scalar.
     * This instance is immutable and is not altered.
     * <p>
     * If the addition is zero, this instance is returned.
     * 
     * @param durationToAdd  the duration to add to this one
     * @param scalar  the amount of times to add, such as -1 to subtract once
     * @return the new duration instance
     */
    public Duration withDurationAdded(long durationToAdd, int scalar) {
        if (durationToAdd == 0 || scalar == 0) {
            return this;
        }
        long add = FieldUtils.safeMultiply(durationToAdd, scalar);
        long duration = FieldUtils.safeAdd(getMillis(), add);
        return new Duration(duration);
    }

    /**
     * Returns a new duration with this length plus that specified multiplied by the scalar.
     * This instance is immutable and is not altered.
     * <p>
     * If the addition is zero, this instance is returned.
     * 
     * @param durationToAdd  the duration to add to this one, null means zero
     * @param scalar  the amount of times to add, such as -1 to subtract once
     * @return the new duration instance
     */
    public Duration withDurationAdded(ReadableDuration durationToAdd, int scalar) {
        if (durationToAdd == null || scalar == 0) {
            return this;
        }
        return withDurationAdded(durationToAdd.getMillis(), scalar);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new duration with this length plus that specified.
     * This instance is immutable and is not altered.
     * <p>
     * If the addition is zero, this instance is returned.
     * 
     * @param amount  the duration to add to this one
     * @return the new duration instance
     */
    public Duration plus(long amount) {
        return withDurationAdded(amount, 1);
    }

    /**
     * Returns a new duration with this length plus that specified.
     * This instance is immutable and is not altered.
     * <p>
     * If the amount is zero, this instance is returned.
     * 
     * @param amount  the duration to add to this one, null means zero
     * @return the new duration instance
     */
    public Duration plus(ReadableDuration amount) {
        if (amount == null) {
            return this;
        }
        return withDurationAdded(amount.getMillis(), 1);
    }

    /**
     * Returns a new duration with this length minus that specified.
     * This instance is immutable and is not altered.
     * <p>
     * If the addition is zero, this instance is returned.
     * 
     * @param amount  the duration to take away from this one
     * @return the new duration instance
     */
    public Duration minus(long amount) {
        return withDurationAdded(amount, -1);
    }

    /**
     * Returns a new duration with this length minus that specified.
     * This instance is immutable and is not altered.
     * <p>
     * If the amount is zero, this instance is returned.
     * 
     * @param amount  the duration to take away from this one, null means zero
     * @return the new duration instance
     */
    public Duration minus(ReadableDuration amount) {
        if (amount == null) {
            return this;
        }
        return withDurationAdded(amount.getMillis(), -1);
    }

    /**
     * Returns a new duration with this length multiplied by the 
     * specified multiplicand.
     * This instance is immutable and is not altered.
     * <p>
     * If the multiplicand is one, this instance is returned.
     * 
     * @param multiplicand  the multiplicand to multiply this one by
     * @return the new duration instance
     */
    public Duration multipliedBy(long multiplicand) {
        if (multiplicand == 1) {
            return this;
        }
        return new Duration(FieldUtils.safeMultiply(getMillis(), multiplicand));
    }

    /**
     * Returns a new duration with this length divided by the 
     * specified divisor.
     * This instance is immutable and is not altered.
     * <p>
     * If the divisor is one, this instance is returned.
     * 
     * @param divisor  the divisor to divide this one by
     * @return the new duration instance
     */
    public Duration dividedBy(long divisor) {
        if (divisor == 1) {
            return this;
        }
        return new Duration(FieldUtils.safeDivide(getMillis(), divisor));
    }

    /**
     * Returns a new duration with its length divided by the
     * specified divisor. {@code RoundingMode} can be specified.
     * This instance is immutable and is not altered.
     * <p>
     * If the divisor is one, this instance is returned.
     *
     * @param divisor  the divisor to divide this one by
     * @param roundingMode  the type of rounding desired
     * @return the new duration instance
     */
    public Duration dividedBy(long divisor, RoundingMode roundingMode) {
        if (divisor == 1) {
            return this;
        }
        return new Duration(FieldUtils.safeDivide(getMillis(), divisor, roundingMode));
    }

    /**
     * Returns a new duration with this length negated.
     * This instance is immutable and is not altered.
     * 
     * @return the new duration instance
     */
    public Duration negated() {
        if (getMillis() == Long.MIN_VALUE) {
            throw new ArithmeticException("Negation of this duration would overflow");
        }
        return new Duration(-getMillis());
    }

}
