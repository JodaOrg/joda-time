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
package org.joda.time.base;

import java.io.Serializable;

import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.PeriodConverter;
import org.joda.time.field.FieldUtils;

/**
 * BasePeriod is an abstract implementation of ReadablePeriod that stores
 * data in a <code>int</code> and <code>PeriodType</code> fields.
 * <p>
 * This class should generally not be used directly by API users.
 * The {@link ReadablePeriod} interface should be used when different 
 * kinds of period objects are to be referenced.
 * <p>
 * BasePeriod subclasses may be mutable and not thread-safe.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public abstract class BasePeriod
        extends AbstractPeriod
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
    private transient int iState;
    /** The duration, if known */
    private transient long iDuration;
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

    //-----------------------------------------------------------------------
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
    protected BasePeriod(long duration, PeriodType type) {
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
    protected BasePeriod(int years, int months, int weeks, int days,
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
    protected BasePeriod(long startInstant, long endInstant, PeriodType type) {
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
    protected BasePeriod(
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
    protected BasePeriod(Object period, PeriodType type) {
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
    protected PeriodType checkPeriodType(PeriodType type) {
        return DateTimeUtils.getPeriodType(type);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the object which defines which fields this period supports.
     */
    public PeriodType getPeriodType() {
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
     * date in a {@link org.joda.time.ReadableInterval}.
     *
     * @return true if the period is precise
     */
    public boolean isPrecise() {
        int state = iState;
        if (state == STATE_UNKNOWN) {
            state = updateTotalMillis();
        }
        return (state == STATE_CALCULATED);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the years field part of the period.
     * 
     * @return the number of years in the period, zero if unsupported
     */
    public int getYears() {
        return iYears;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the months field part of the period.
     * 
     * @return the number of months in the period, zero if unsupported
     */
    public int getMonths() {
        return iMonths;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the weeks field part of the period.
     * 
     * @return the number of weeks in the period, zero if unsupported
     */
    public int getWeeks() {
        return iWeeks;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the days field part of the period.
     * 
     * @return the number of days in the period, zero if unsupported
     */
    public int getDays() {
        return iDays;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the hours field part of the period.
     * 
     * @return the number of hours in the period, zero if unsupported
     */
    public int getHours() {
        return iHours;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the minutes field part of the period.
     * 
     * @return the number of minutes in the period, zero if unsupported
     */
    public int getMinutes() {
        return iMinutes;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the seconds field part of the period.
     * 
     * @return the number of seconds in the period, zero if unsupported
     */
    public int getSeconds() {
        return iSeconds;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the millis field part of the period.
     * 
     * @return the number of millis in the period, zero if unsupported
     */
    public int getMillis() {
        return iMillis;
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
    public long toDurationMillis() {
        int state = iState;
        if (state == STATE_UNKNOWN) {
            state = updateTotalMillis();
        }
        if (state != STATE_CALCULATED) {
            throw new IllegalStateException("Duration is imprecise");
        }
        return iDuration;
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

    //-----------------------------------------------------------------------
    /**
     * Walks through the field values, determining total millis and whether
     * this period is precise.
     *
     * @return new state
     * @throws ArithmeticException if the millis exceeds the capacity of the period
     */
    private int updateTotalMillis() {
        PeriodType type = iType;

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

}
