/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2004 Stephen Colebourne.  
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Joda project (http://www.joda.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "Joda" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact licence@joda.org.
 *
 * 5. Products derived from this software may not be called "Joda",
 *    nor may "Joda" appear in their name, without prior written
 *    permission of the Joda project.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE JODA AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Joda project and was originally 
 * created by Stephen Colebourne <scolebourne@joda.org>. For more
 * information on the Joda project, please see <http://www.joda.org/>.
 */
package org.joda.time;

import java.io.Serializable;

import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.PeriodConverter;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.ISOPeriodFormat;

/**
 * AbstractDuration provides the common behaviour for duration classes.
 * <p>
 * This class should generally not be used directly by API users. The 
 * {@link ReadableDuration} interface should be used when different 
 * kinds of durations are to be referenced.
 * <p>
 * AbstractDuration subclasses may be mutable and not thread-safe.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public abstract class AbstractPeriod
        implements ReadablePeriod, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = -2110953284060001145L;
    /** Millis cache is currently unknown */
    private static final int STATE_UNKNOWN = 0;
    /** Millis cache is not calculable */
    private static final int STATE_NOT_CALCULABLE = 1;
    /** Millis cache has been calculated and is valid */
    private static final int STATE_CALCULATED = 2;

    /** The period type that allocates the duration to fields */
    private final PeriodType iType;
    /** The object state */
    private int iState;
    /** The duration, if known */
    private long iDuration;
    /** Value for years */
    private int iYears;
    /** Value for months */
    private int iMonths;
    /** Value for weeks */
    private int iWeeks;
    /** Value for days */
    private int iDays;
    /** Value for hours */
    private int iHours;
    /** Value for minutes */
    private int iMinutes;
    /** Value for seconds */
    private int iSeconds;
    /** Value for millis */
    private int iMillis;

    /**
     * Creates a period from the given millisecond duration.
     * <p>
     * The millisecond duration will be split to fields using a UTC version of
     * the period type.
     *
     * @param duration  the duration, in milliseconds
     * @param type  which set of fields this period supports
     * @throws IllegalArgumentException if period type is invalid
     */
    public AbstractPeriod(long duration, PeriodType type) {
        super();
        type = checkPeriodType(type);
        iType = type;
        // Only call a private method
        setPeriod(type, duration);
    }

    /**
     * Creates a period from a set of field values.
     *
     * @param years  amount of years in this period, which must be zero if unsupported
     * @param months  amount of months in this period, which must be zero if unsupported
     * @param weeks  amount of weeks in this period, which must be zero if unsupported
     * @param days  amount of days in this period, which must be zero if unsupported
     * @param hours  amount of hours in this period, which must be zero if unsupported
     * @param minutes  amount of minutes in this period, which must be zero if unsupported
     * @param seconds  amount of seconds in this period, which must be zero if unsupported
     * @param millis  amount of milliseconds in this period, which must be zero if unsupported
     * @param type  which set of fields this period supports
     * @throws IllegalArgumentException if period type is invalid
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    public AbstractPeriod(int years, int months, int weeks, int days,
                            int hours, int minutes, int seconds, int millis,
                            PeriodType type) {
        super();
        type = checkPeriodType(type);
        iType = type;
        // Only call a private method
        setPeriod(type, years, months, weeks, days, hours, minutes, seconds, millis);
    }

    /**
     * Creates a period from the given interval endpoints.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     * @param type  which set of fields this period supports
     * @throws IllegalArgumentException if period type is invalid
     */
    public AbstractPeriod(long startInstant, long endInstant, PeriodType type) {
        super();
        type = checkPeriodType(type);
        iType = type;
        // Only call a private method
        setPeriod(type, startInstant, endInstant);
    }

    /**
     * Creates a period from the given interval endpoints.
     *
     * @param startInstant  interval start, null means now
     * @param endInstant  interval end, null means now
     * @param type  which set of fields this period supports
     * @throws IllegalArgumentException if period type is invalid
     */
    public AbstractPeriod(
            ReadableInstant startInstant, ReadableInstant  endInstant, PeriodType type) {
        super();
        type = checkPeriodType(type);
        if (startInstant == null && endInstant == null) {
            iType = type;
        } else {
            long start = (startInstant == null ? DateTimeUtils.currentTimeMillis() : startInstant.getMillis());
            long end = (endInstant == null ? DateTimeUtils.currentTimeMillis() : endInstant.getMillis());
            iType = type;
            // Only call a private method
            setPeriod(type, start, end);
        }
    }

    /**
     * Creates a new period based on another using the {@link ConverterManager}.
     *
     * @param period  the period to convert
     * @param type  which set of fields this period supports, null means use type from object
     * @throws IllegalArgumentException if period is invalid
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    public AbstractPeriod(Object period, PeriodType type) {
        super();
        PeriodConverter converter = ConverterManager.getInstance().getPeriodConverter(period);
        type = (type == null ? converter.getPeriodType(period, false) : type);
        type = checkPeriodType(type);
        iType = type;
        if (this instanceof ReadWritablePeriod) {
            converter.setInto((ReadWritablePeriod) this, period);
        } else {
            // Only call a private method
            setPeriod(type, new MutablePeriod(period, type));
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Validates a period type, converting nulls to a default value and
     * checking the type is suitable for this instance.
     * 
     * @param type  the type to check, may be null
     * @return the validated type to use, not null
     * @throws IllegalArgumentException if the period type is invalid
     */
    protected abstract PeriodType checkPeriodType(PeriodType type);

    //-----------------------------------------------------------------------
    /**
     * Returns the object which defines which fields this period supports.
     */
    public final PeriodType getPeriodType() {
        return iType;
    }

    /**
     * Is this period a precise length of time, or descriptive.
     * <p>
     * A typical precise period could include millis, seconds, minutes or hours,
     * but days, weeks, months and years usually vary in length, resulting in
     * an imprecise period.
     * <p>
     * An imprecise period can be made precise by pairing it with a
     * date in a {@link ReadableInterval}.
     *
     * @return true if the period is precise
     */
    public final boolean isPrecise() {
        int state = iState;
        if (state == STATE_UNKNOWN) {
            state = updateTotalMillis();
        }
        return (state == STATE_CALCULATED);
    }

    //-----------------------------------------------------------------------
    /**
     * Adds this period to the given instant using the chronology of the period
     * which typically ignores time zones.
     * <p>
     * To add just once, pass in a scalar of one. To subtract once, pass
     * in a scalar of minus one.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to add the
     * period to
     * @param scalar  the number of times to add the period, negative to subtract
     * @return milliseconds value plus this period times scalar
     * @throws ArithmeticException if the result of the calculation is too large
     */
    public final long addTo(long instant, int scalar) {
        return addTo(instant, scalar, null);
    }

    /**
     * Adds this period to the given instant using a specific chronology.
     * <p>
     * To add just once, pass in a scalar of one. To subtract once, pass
     * in a scalar of minus one.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to add the
     * period to
     * @param scalar  the number of times to add the period, negative to subtract
     * @param chrono  override the period's chronology, unless null is passed in
     * @return milliseconds value plus this period times scalar
     * @throws ArithmeticException if the result of the calculation is too large
     */
    public final long addTo(long instant, int scalar, Chronology chrono) {
        if (isPrecise()) {
            return FieldUtils.safeAdd(instant, toDurationMillis() * scalar);
        }
        
        PeriodType type = iType;
        if (chrono != null) {
            type = type.withChronology(chrono);
        }
        
        long value; // used to lock fields against threading issues
        value = scaleValue(iYears, scalar);
        if (value != 0) {
            instant = type.years().add(instant, value);
        }
        value = scaleValue(iMonths, scalar);
        if (value != 0) {
            instant = type.months().add(instant, value);
        }
        value = scaleValue(iWeeks, scalar);
        if (value != 0) {
            instant = type.weeks().add(instant, value);
        }
        value = scaleValue(iDays, scalar);
        if (value != 0) {
            instant = type.days().add(instant, value);
        }
        value = scaleValue(iHours, scalar);
        if (value != 0) {
            instant = type.hours().add(instant, value);
        }
        value = scaleValue(iMinutes, scalar);
        if (value != 0) {
            instant = type.minutes().add(instant, value);
        }
        value = scaleValue(iSeconds, scalar);
        if (value != 0) {
            instant = type.seconds().add(instant, value);
        }
        value = scaleValue(iMillis, scalar);
        if (value != 0) {
            instant = type.millis().add(instant, value);
        }

        return instant;
    }

    /**
     * Convert the scalar to a multiple efficiently.
     * 
     * @param value  the value
     * @param scalar  the scalar
     * @return the converted value
     */
    private static long scaleValue(int value, int scalar) {
        long val = value;  // use long to avoid truncation
        switch (scalar) {
        case -1:
            return -val;
        case 0:
            return 0;
        case 1:
            return val;
        default:
            return val * scalar;
        }
    }

    /**
     * Adds this period to the given instant using the chronology of the specified
     * instant (if present), returning a new Instant.
     * <p>
     * To add just once, pass in a scalar of one. To subtract once, pass
     * in a scalar of minus one.
     *
     * @param instant  the instant to add the period to, null means now
     * @param scalar  the number of times to add the period, negative to subtract
     * @return instant with the original value plus this period times scalar
     * @throws ArithmeticException if the result of the calculation is too large
     */
    public final Instant addTo(ReadableInstant instant, int scalar) {
        if (instant == null) {
            return new Instant(addTo(DateTimeUtils.currentTimeMillis(), scalar));
        }
        return new Instant(addTo(instant.getMillis(), scalar, instant.getChronology()));
    }

    /**
     * Adds this period into the given mutable instant using the chronology of
     * the specified mutable instant (if present).
     * <p>
     * To add just once, pass in a scalar of one. To subtract once, pass
     * in a scalar of minus one.
     *
     * @param instant  the instant to update with the added period, must not be null
     * @param scalar  the number of times to add the period, negative to subtract
     * @throws IllegalArgumentException if the instant is null
     * @throws ArithmeticException if the result of the calculation is too large
     */
    public final void addInto(ReadWritableInstant instant, int scalar) {
        if (instant == null) {
            throw new IllegalArgumentException("The instant must not be null");
        }
        instant.setMillis(addTo(instant.getMillis(), scalar, instant.getChronology()));
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the years field part of the period.
     * 
     * @return the number of years in the period, zero if unsupported
     */
    public final int getYears() {
        return iYears;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the months field part of the period.
     * 
     * @return the number of months in the period, zero if unsupported
     */
    public final int getMonths() {
        return iMonths;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the weeks field part of the period.
     * 
     * @return the number of weeks in the period, zero if unsupported
     */
    public final int getWeeks() {
        return iWeeks;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the days field part of the period.
     * 
     * @return the number of days in the period, zero if unsupported
     */
    public final int getDays() {
        return iDays;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the hours field part of the period.
     * 
     * @return the number of hours in the period, zero if unsupported
     */
    public final int getHours() {
        return iHours;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the minutes field part of the period.
     * 
     * @return the number of minutes in the period, zero if unsupported
     */
    public final int getMinutes() {
        return iMinutes;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the seconds field part of the period.
     * 
     * @return the number of seconds in the period, zero if unsupported
     */
    public final int getSeconds() {
        return iSeconds;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the millis field part of the period.
     * 
     * @return the number of millis in the period, zero if unsupported
     */
    public final int getMillis() {
        return iMillis;
    }

    //-----------------------------------------------------------------------
    /**
     * Get this object as an immutable Period. This can be useful if you
     * don't trust the implementation of the interface to be well-behaved, or
     * to get a guaranteed immutable object.
     * 
     * @return a Duration using the same field set and values
     */
    public final Period toPeriod() {
        if (this instanceof Period) {
            return (Period) this;
        }
        return new Period(this);
    }

    /**
     * Get this object as a MutablePeriod.
     * 
     * @return a MutablePeriod using the same field set and values
     */
    public final MutablePeriod toMutablePeriod() {
        return new MutablePeriod(this);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the total millisecond duration of this period,
     * failing if the period is imprecise.
     *
     * @return the total length of the period in milliseconds.
     * @throws IllegalStateException if the period is imprecise
     * @throws ArithmeticException if the millis exceeds the capacity of the duration
     */
    public final long toDurationMillis() {
        int state = iState;
        if (state == STATE_UNKNOWN) {
            state = updateTotalMillis();
        }
        if (state != STATE_CALCULATED) {
            throw new IllegalStateException("Duration is imprecise");
        }
        return iDuration;
    }

    /**
     * Gets the total millisecond duration of this period,
     * failing if the period is imprecise.
     *
     * @return the total length of the period in milliseconds.
     * @throws IllegalStateException if the period is imprecise
     * @throws ArithmeticException if the millis exceeds the capacity of the duration
     */
    public final Duration toDuration() {
        return new Duration(toDurationMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this object with the specified object for equality based
     * on the value of each field. All ReadablePeriod instances are accepted.
     * <p>
     * To compare two periods for absolute duration (ie. millisecond duration
     * ignoring the fields), use {@link #toDurationMillis()} or {@link #toDuration()}.
     *
     * @param readablePeriod  a readable period to check against
     * @return true if all the field values are equal, false if
     *  not or the period is null or of an incorrect type
     */
    public final boolean equals(Object readablePeriod) {
        if (this == readablePeriod) {
            return true;
        }
        if (readablePeriod instanceof ReadablePeriod == false) {
            return false;
        }
        ReadablePeriod other = (ReadablePeriod) readablePeriod;
        PeriodType type = getPeriodType();
        if (type.equals(other.getPeriodType()) == false) {
            return false;
        }
        return getYears() == other.getYears()
            && getMonths() == other.getMonths()
            && getWeeks() == other.getWeeks()
            && getDays() == other.getDays()
            && getHours() == other.getHours()
            && getMinutes() == other.getMinutes()
            && getSeconds() == other.getSeconds()
            && getMillis() == other.getMillis();
    }

    /**
     * Gets a hash code for the period that is compatable with the 
     * equals method.
     *
     * @return a hash code
     */
    public final int hashCode() {
        int hash = getPeriodType().hashCode();
        hash = 53 * hash + getYears();
        hash = 53 * hash + getMonths();
        hash = 53 * hash + getWeeks();
        hash = 53 * hash + getDays();
        hash = 53 * hash + getHours();
        hash = 53 * hash + getMinutes();
        hash = 53 * hash + getSeconds();
        hash = 53 * hash + getMillis();
        return hash;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the value as a String in the ISO8601 duration format.
     * <p>
     * For example, "P6H3M7S" represents 6 hours, 3 minutes, 7 seconds.
     *
     * @return the value as an ISO8601 string
     */
    public String toString() {
        return ISOPeriodFormat.getInstance().standard().print(this);
    }

    //-----------------------------------------------------------------------
    /**
     * Checks whether the field is supported.
     */
    private static void checkArgument(DurationField field) {
        if (!field.isSupported()) {
            throw new IllegalArgumentException
                ("Time period does not support field '" + field.getName() + "'");
        }
    }

    /**
     * Checks whether the field is supported.
     */
    private static void checkSupport(DurationField field) {
        if (!field.isSupported()) {
            throw new UnsupportedOperationException
                ("Time period does not support field '" + field.getName() + "'");
        }
    }

    /**
     * Sets all the fields in one go from another ReadablePeriod.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     * 
     * @param period  the period to set, null means zero length period
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    protected void setPeriod(ReadablePeriod period) {
        if (period == null) {
            setPeriod(iType, 0L);
        } else {
            setPeriod(iType, period);
        }
    }

    /**
     * This method is private to prevent subclasses from overriding.
     */
    private void setPeriod(PeriodType type, ReadablePeriod period) {
        setPeriod(type,
            period.getYears(), period.getMonths(),
            period.getWeeks(), period.getDays(),
            period.getHours(), period.getMinutes(),
            period.getSeconds(), period.getMillis());
    }

    /**
     * Sets all the fields in one go.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     * 
     * @param years  amount of years in this period, which must be zero if unsupported
     * @param months  amount of months in this period, which must be zero if unsupported
     * @param weeks  amount of weeks in this period, which must be zero if unsupported
     * @param days  amount of days in this period, which must be zero if unsupported
     * @param hours  amount of hours in this period, which must be zero if unsupported
     * @param minutes  amount of minutes in this period, which must be zero if unsupported
     * @param seconds  amount of seconds in this period, which must be zero if unsupported
     * @param millis  amount of milliseconds in this period, which must be zero if unsupported
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    protected void setPeriod(int years, int months, int weeks, int days,
                               int hours, int minutes, int seconds, int millis) {
        setPeriod(iType, years, months, weeks, days, hours, minutes, seconds, millis);
    }

    /**
     * This method is private to prevent subclasses from overriding.
     */
    private void setPeriod(PeriodType type,
                             int years, int months, int weeks, int days,
                             int hours, int minutes, int seconds, int millis) {
        if (years != 0) {
            checkArgument(type.years());
        }
        if (months != 0) {
            checkArgument(type.months());
        }
        if (weeks != 0) {
            checkArgument(type.weeks());
        }
        if (days != 0) {
            checkArgument(type.days());
        }
        if (hours != 0) {
            checkArgument(type.hours());
        }
        if (minutes != 0) {
            checkArgument(type.minutes());
        }
        if (seconds != 0) {
            checkArgument(type.seconds());
        }
        if (millis != 0) {
            checkArgument(type.millis());
        }
        
        // assign fields in one block to reduce threading issues
        iYears = years;
        iMonths = months;
        iWeeks = weeks;
        iDays = days;
        iHours = hours;
        iMinutes = minutes;
        iSeconds = seconds;
        iMillis = millis;
        iState = STATE_UNKNOWN;
    }

    /**
     * Sets all the fields in one go from a millisecond interval.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     * 
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     */
    protected void setPeriod(long startInstant, long endInstant) {
        setPeriod(iType, startInstant, endInstant);
    }

    /**
     * This method is private to prevent subclasses from overriding.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     */
    private void setPeriod(PeriodType type, long startInstant, long endInstant) {
        long baseTotalMillis = (endInstant - startInstant);
        int years = 0, months = 0, weeks = 0, days = 0;
        int hours = 0, minutes = 0, seconds = 0, millis = 0;
        DurationField field;
        field = type.years();
        if (field.isSupported()) {
            years = field.getDifference(endInstant, startInstant);
            startInstant = field.add(startInstant, years);
        }
        field = type.months();
        if (field.isSupported()) {
            months = field.getDifference(endInstant, startInstant);
            startInstant = field.add(startInstant, months);
        }
        field = type.weeks();
        if (field.isSupported()) {
            weeks = field.getDifference(endInstant, startInstant);
            startInstant = field.add(startInstant, weeks);
        }
        field = type.days();
        if (field.isSupported()) {
            days = field.getDifference(endInstant, startInstant);
            startInstant = field.add(startInstant, days);
        }
        field = type.hours();
        if (field.isSupported()) {
            hours = field.getDifference(endInstant, startInstant);
            startInstant = field.add(startInstant, hours);
        }
        field = type.minutes();
        if (field.isSupported()) {
            minutes = field.getDifference(endInstant, startInstant);
            startInstant = field.add(startInstant, minutes);
        }
        field = type.seconds();
        if (field.isSupported()) {
            seconds = field.getDifference(endInstant, startInstant);
            startInstant = field.add(startInstant, seconds);
        }
        field = type.millis();
        if (field.isSupported()) {
            millis = field.getDifference(endInstant, startInstant);
            startInstant = field.add(startInstant, millis);
        }
        
        // assign fields in one block to reduce threading issues
        iYears = years;
        iMonths = months;
        iWeeks = weeks;
        iDays = days;
        iHours = hours;
        iMinutes = minutes;
        iSeconds = seconds;
        iMillis = millis;
        iState = STATE_UNKNOWN;
    }

    /**
     * Sets all the fields in one go from a millisecond duration.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     * 
     * @param duration  the duration, in milliseconds
     * @throws ArithmeticException if the set exceeds the capacity of the period
     */
    protected void setPeriod(long duration) {
        setPeriod(iType, duration);
    }

    /**
     * This method is private to prevent subclasses from overriding.
     *
     * @param duration  the duration, in milliseconds
     */
    private void setPeriod(PeriodType type, long duration) {
        if (duration == 0) {
            iDuration = duration;
            iYears = 0;
            iMonths = 0;
            iWeeks = 0;
            iDays = 0;
            iHours = 0;
            iMinutes = 0;
            iSeconds = 0;
            iMillis = 0;
            iState = STATE_CALCULATED;
            return;
        }
        
        long startInstant = 0;
        int years = 0, months = 0, weeks = 0, days = 0;
        int hours = 0, minutes = 0, seconds = 0, millis = 0;
        DurationField field;
        
        field = type.years();
        if (field.isSupported() && field.isPrecise()) {
            years = field.getDifference(duration, startInstant);
            startInstant = field.add(startInstant, years);
        }
        field = type.months();
        if (field.isSupported() && field.isPrecise()) {
            months = field.getDifference(duration, startInstant);
            startInstant = field.add(startInstant, months);
        }
        field = type.weeks();
        if (field.isSupported() && field.isPrecise()) {
            weeks = field.getDifference(duration, startInstant);
            startInstant = field.add(startInstant, weeks);
        }
        field = type.days();
        if (field.isSupported() && field.isPrecise()) {
            days = field.getDifference(duration, startInstant);
            startInstant = field.add(startInstant, days);
        }
        field = type.hours();
        if (field.isSupported() && field.isPrecise()) {
            hours = field.getDifference(duration, startInstant);
            startInstant = field.add(startInstant, hours);
        }
        field = type.minutes();
        if (field.isSupported() && field.isPrecise()) {
            minutes = field.getDifference(duration, startInstant);
            startInstant = field.add(startInstant, minutes);
        }
        field = type.seconds();
        if (field.isSupported() && field.isPrecise()) {
            seconds = field.getDifference(duration, startInstant);
            startInstant = field.add(startInstant, seconds);
        }
        field = type.millis();
        if (field.isSupported() && field.isPrecise()) {
            millis = field.getDifference(duration, startInstant);
            startInstant = field.add(startInstant, millis);
        }
        
        // assign fields in one block to reduce threading issues
        iYears = years;
        iMonths = months;
        iWeeks = weeks;
        iDays = days;
        iHours = hours;
        iMinutes = minutes;
        iSeconds = seconds;
        iMillis = millis;
        iState = STATE_UNKNOWN;
    }

    /**
     * Sets all the fields in one go from an interval dividing the
     * fields using the period type.
     * 
     * @param interval  the interval to set, null means zero length
     */
    protected void setPeriod(ReadableInterval interval) {
        if (interval != null) {
            setPeriod(interval.getStartMillis(), interval.getEndMillis());
        } else {
            setPeriod(0L);
        }
    }

    /**
     * Sets all the fields in one go from a duration dividing the
     * fields using the period type.
     * 
     * @param duration  the duration to set, null means zero length
     */
    protected void setPeriod(ReadableDuration duration) {
        if (duration != null) {
            setPeriod(duration.getMillis());
        } else {
            setPeriod(0L);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Walks through the field values, determining total millis and whether
     * this period is precise.
     *
     * @return new state
     * @throws ArithmeticException if the millis exceeds the capacity of the period
     */
    private int updateTotalMillis() {
        final PeriodType type = iType;

        boolean isPrecise = true;
        long totalMillis = 0;

        DurationField field;
        int years = iYears, months = iMonths, weeks = iWeeks, days = iDays;
        int hours = iHours, minutes = iMinutes, seconds = iSeconds, millis = iMillis;
        if (years != 0) {
            field = type.years();
            if (isPrecise &= field.isPrecise()) {
                totalMillis = FieldUtils.safeAdd(totalMillis, field.getMillis(years));
            }
        }
        if (months != 0) {
            field = type.months();
            if (isPrecise &= field.isPrecise()) {
                totalMillis = FieldUtils.safeAdd(totalMillis, field.getMillis(months));
            }
        }
        if (weeks != 0) {
            field = type.weeks();
            if (isPrecise &= field.isPrecise()) {
                totalMillis = FieldUtils.safeAdd(totalMillis, field.getMillis(weeks));
            }
        }
        if (days != 0) {
            field = type.days();
            if (isPrecise &= field.isPrecise()) {
                totalMillis = FieldUtils.safeAdd(totalMillis, field.getMillis(days));
            }
        }
        if (hours != 0) {
            field = type.hours();
            if (isPrecise &= field.isPrecise()) {
                totalMillis = FieldUtils.safeAdd(totalMillis, field.getMillis(hours));
            }
        }
        if (minutes != 0) {
            field = type.minutes();
            if (isPrecise &= field.isPrecise()) {
                totalMillis = FieldUtils.safeAdd(totalMillis, field.getMillis(minutes));
            }
        }
        if (seconds != 0) {
            field = type.seconds();
            if (isPrecise &= field.isPrecise()) {
                totalMillis = FieldUtils.safeAdd(totalMillis, field.getMillis(seconds));
            }
        }
        if (millis != 0) {
            field = type.millis();
            if (isPrecise &= field.isPrecise()) {
                totalMillis = FieldUtils.safeAdd(totalMillis, field.getMillis(millis));
            }
        }
        
        iDuration = totalMillis;
        if (isPrecise) {
            return iState = STATE_CALCULATED;
        } else {
            return iState = STATE_NOT_CALCULABLE;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a period to this one by adding each field in turn.
     * 
     * @param period  the period to add, null means add nothing
     * @throws IllegalArgumentException if the period being added contains a field
     * not supported by this period
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    protected void add(ReadablePeriod period) {
        if (period != null) {
            setPeriod(
                FieldUtils.safeAdd(getYears(), period.getYears()),
                FieldUtils.safeAdd(getMonths(), period.getMonths()),
                FieldUtils.safeAdd(getWeeks(), period.getWeeks()),
                FieldUtils.safeAdd(getDays(), period.getDays()),
                FieldUtils.safeAdd(getHours(), period.getHours()),
                FieldUtils.safeAdd(getMinutes(), period.getMinutes()),
                FieldUtils.safeAdd(getSeconds(), period.getSeconds()),
                FieldUtils.safeAdd(getMillis(), period.getMillis())
            );
        }
    }

    /**
     * Adds to each field of this period.
     * 
     * @param years  amount of years to add to this period, which must be zero if unsupported
     * @param months  amount of months to add to this period, which must be zero if unsupported
     * @param weeks  amount of weeks to add to this period, which must be zero if unsupported
     * @param days  amount of days to add to this period, which must be zero if unsupported
     * @param hours  amount of hours to add to this period, which must be zero if unsupported
     * @param minutes  amount of minutes to add to this period, which must be zero if unsupported
     * @param seconds  amount of seconds to add to this period, which must be zero if unsupported
     * @param millis  amount of milliseconds to add to this period, which must be zero if unsupported
     * @throws IllegalArgumentException if the period being added contains a field
     * not supported by this period
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    protected void add(int years, int months, int weeks, int days,
                       int hours, int minutes, int seconds, int millis) {
        setPeriod(
            FieldUtils.safeAdd(getYears(), years),
            FieldUtils.safeAdd(getMonths(), months),
            FieldUtils.safeAdd(getWeeks(), weeks),
            FieldUtils.safeAdd(getDays(), days),
            FieldUtils.safeAdd(getHours(), hours),
            FieldUtils.safeAdd(getMinutes(), minutes),
            FieldUtils.safeAdd(getSeconds(), seconds),
            FieldUtils.safeAdd(getMillis(), millis)
        );
    }

    /**
     * Adds an interval to this one by dividing the interval into
     * fields and calling {@link #add(ReadablePeriod)}.
     * 
     * @param interval  the interval to add, null means add nothing
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    protected void add(ReadableInterval interval) {
        if (interval != null) {
            add(interval.toPeriod(getPeriodType()));
        }
    }

    /**
     * Adds a duration to this one by dividing the duration into
     * fields and calling {@link #add(ReadablePeriod)}.
     * 
     * @param duration  the duration to add, null means add nothing
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    protected void add(ReadableDuration duration) {
        if (duration != null) {
            add(new Period(duration.getMillis(), getPeriodType()));
        }
    }

    /**
     * Adds a millisecond duration to this one by dividing the duration into
     * fields and calling {@link #add(ReadablePeriod)}.
     * 
     * @param duration  the duration, in milliseconds
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    protected void add(long duration) {
        add(new Period(duration, getPeriodType()));
    }

    /**
     * Normalizes all the field values in this period.
     * <p>
     * This method converts to a milliecond duration and back again.
     *
     * @throws IllegalStateException if this period is imprecise
     */
    protected void normalize() {
        setPeriod(toDurationMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of years of the period.
     * 
     * @param years  the number of years
     * @throws UnsupportedOperationException if field is not supported.
     */
    protected void setYears(int years) {
        if (years != iYears) {
            if (years != 0) {
                checkSupport(iType.years());
            }
            iYears = years;
            iState = STATE_UNKNOWN;
        }
    }

    /**
     * Adds the specified years to the number of years in the period.
     * 
     * @param years  the number of years
     * @throws UnsupportedOperationException if field is not supported
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    protected void addYears(int years) {
        if (years != 0) {
            setYears(FieldUtils.safeAdd(getYears(), years));
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of months of the period.
     * 
     * @param months  the number of months
     * @throws UnsupportedOperationException if field is not supported
     */
    protected void setMonths(int months) {
        if (months != iMonths) {
            if (months != 0) {
                checkSupport(iType.months());
            }
            iMonths = months;
            iState = STATE_UNKNOWN;
        }
    }

    /**
     * Adds the specified months to the number of months in the period.
     * 
     * @param months  the number of months
     * @throws UnsupportedOperationException if field is not supported
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    protected void addMonths(int months) {
        if (months != 0) {
            setMonths(FieldUtils.safeAdd(getMonths(), months));
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of weeks of the period.
     * 
     * @param weeks  the number of weeks
     * @throws UnsupportedOperationException if field is not supported
     */
    protected void setWeeks(int weeks) {
        if (weeks != iWeeks) {
            if (weeks != 0) {
                checkSupport(iType.weeks());
            }
            iWeeks = weeks;
            iState = STATE_UNKNOWN;
        }
    }

    /**
     * Adds the specified weeks to the number of weeks in the period.
     * 
     * @param weeks  the number of weeks
     * @throws UnsupportedOperationException if field is not supported
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    protected void addWeeks(int weeks) {
        if (weeks != 0) {
            setWeeks(FieldUtils.safeAdd(getWeeks(), weeks));
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of days of the period.
     * 
     * @param days  the number of days
     * @throws UnsupportedOperationException if field is not supported
     */
    protected void setDays(int days) {
        if (days != iDays) {
            if (days != 0) {
                checkSupport(iType.days());
            }
            iDays = days;
            iState = STATE_UNKNOWN;
        }
    }

    /**
     * Adds the specified days to the number of days in the period.
     * 
     * @param days  the number of days
     * @throws UnsupportedOperationException if field is not supported
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    protected void addDays(int days) {
        if (days != 0) {
            setDays(FieldUtils.safeAdd(getDays(), days));
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of hours of the period.
     * 
     * @param hours  the number of hours
     * @throws UnsupportedOperationException if field is not supported
     */
    protected void setHours(int hours) {
        if (hours != iHours) {
            if (hours != 0) {
                checkSupport(iType.hours());
            }
            iHours = hours;
            iState = STATE_UNKNOWN;
        }
    }

    /**
     * Adds the specified hours to the number of hours in the period.
     * 
     * @param hours  the number of hours
     * @throws UnsupportedOperationException if field is not supported
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    protected void addHours(int hours) {
        if (hours != 0) {
            setHours(FieldUtils.safeAdd(getHours(), hours));
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of minutes of the period.
     * 
     * @param minutes  the number of minutes
     * @throws UnsupportedOperationException if field is not supported
     */
    protected void setMinutes(int minutes) {
        if (minutes != iMinutes) {
            if (minutes != 0) {
                checkSupport(iType.minutes());
            }
            iMinutes = minutes;
            iState = STATE_UNKNOWN;
        }
    }

    /**
     * Adds the specified minutes to the number of minutes in the period.
     * 
     * @param minutes  the number of minutes
     * @throws UnsupportedOperationException if field is not supported
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    protected void addMinutes(int minutes) {
        if (minutes != 0) {
            setMinutes(FieldUtils.safeAdd(getMinutes(), minutes));
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of seconds of the period.
     * 
     * @param seconds  the number of seconds
     * @throws UnsupportedOperationException if field is not supported
     */
    protected void setSeconds(int seconds) {
        if (seconds != iSeconds) {
            if (seconds != 0) {
                checkSupport(iType.seconds());
            }
            iSeconds = seconds;
            iState = STATE_UNKNOWN;
        }
    }

    /**
     * Adds the specified seconds to the number of seconds in the period.
     * 
     * @param seconds  the number of seconds
     * @throws UnsupportedOperationException if field is not supported
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    protected void addSeconds(int seconds) {
        if (seconds != 0) {
            setSeconds(FieldUtils.safeAdd(getSeconds(), seconds));
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of millis of the period.
     * 
     * @param millis  the number of millis
     * @throws UnsupportedOperationException if field is not supported
     */
    protected void setMillis(int millis) {
        if (millis != iMillis) {
            if (millis != 0) {
                checkSupport(iType.millis());
            }
            iMillis = millis;
            iState = STATE_UNKNOWN;
        }
    }

    /**
     * Adds the specified millis to the number of millis in the period.
     * 
     * @param millis  the number of millis
     * @throws UnsupportedOperationException if field is not supported
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    protected void addMillis(int millis) {
        if (millis != 0) {
            setMillis(FieldUtils.safeAdd(getMillis(), millis));
        }
    }

}
