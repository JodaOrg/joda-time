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

import org.joda.time.convert.DurationConverter;
import org.joda.time.convert.ConverterManager;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.ISODurationFormat;

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
public abstract class AbstractDuration implements ReadableDuration, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = -2110953284060001145L;

    /**
     * Checks whether the field is supported.
     */
    private static void checkArgument(DurationField field, String name) {
        if (!field.isSupported()) {
            throw new IllegalArgumentException
                ("Duration does not support field \"" + name + '"');
        }
    }

    /**
     * Checks whether the field is supported.
     */
    private static void checkSupport(DurationField field, String name) {
        if (!field.isSupported()) {
            throw new UnsupportedOperationException
                ("Duration does not support field \"" + name + '"');
        }
    }

    /** The duration type that allocates the duration to fields */
    private final DurationType iType;
    /** The total milliseconds, if known */
    private long iTotalMillis;
    /** The milliseoond status, 0=unknown, 1=imprecise, 2=precise */
    private int iTotalMillisState;

    private int iYears;
    private int iMonths;
    private int iWeeks;
    private int iDays;
    private int iHours;
    private int iMinutes;
    private int iSeconds;
    private int iMillis;

    /**
     * Creates a zero length duration of the specified type.
     *
     * @param type  which set of fields this duration supports, null means millis type
     */
    public AbstractDuration(DurationType type) {
        super();
        type = (type == null ? DurationType.getMillisType() : type);
        iType = type;
        // Only call a private method
        setTotalMillis(type, 0L);
    }

    /**
     * Creates a new duration based on another using the {@link ConverterManager}.
     *
     * @param duration  duration to convert
     * @param type  which set of fields this duration supports, null means use type from object
     * @throws IllegalArgumentException if duration is invalid
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    public AbstractDuration(Object duration, DurationType type) {
        super();
        DurationConverter converter = ConverterManager.getInstance().getDurationConverter(duration);
        type = (type == null ? converter.getDurationType(duration) : type);
        iType = type;
        if (type.isPrecise() && converter.isPrecise(duration)) {
            // Only call a private method
            setTotalMillis(type, converter.getDurationMillis(duration));
        } else if (this instanceof ReadWritableDuration) {
            converter.setInto((ReadWritableDuration) this, duration);
        } else {
            // Only call a private method
            setDuration(type, new MutableDuration(duration, type));
        }
    }

    /**
     * Creates a duration from a set of field values.
     *
     * @param years  amount of years in this duration, which must be zero if unsupported
     * @param months  amount of months in this duration, which must be zero if unsupported
     * @param weeks  amount of weeks in this duration, which must be zero if unsupported
     * @param days  amount of days in this duration, which must be zero if unsupported
     * @param hours  amount of hours in this duration, which must be zero if unsupported
     * @param minutes  amount of minutes in this duration, which must be zero if unsupported
     * @param seconds  amount of seconds in this duration, which must be zero if unsupported
     * @param millis  amount of milliseconds in this duration, which must be zero if unsupported
     * @param type  which set of fields this duration supports, null means AllType
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    public AbstractDuration(int years, int months, int weeks, int days,
                            int hours, int minutes, int seconds, int millis,
                            DurationType type) {
        super();
        type = (type == null ? DurationType.getAllType() : type);
        iType = type;
        // Only call a private method
        setDuration(type, years, months, weeks, days, hours, minutes, seconds, millis);
    }

    /**
     * Creates a duration from the given interval endpoints.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     * @param type  which set of fields this duration supports, null means AllType
     */
    public AbstractDuration(long startInstant, long endInstant, DurationType type) {
        super();
        type = (type == null ? DurationType.getAllType() : type);
        iType = type;
        // Only call a private method
        setTotalMillis(type, startInstant, endInstant);
    }

    /**
     * Creates a duration from the given interval endpoints.
     *
     * @param startInstant  interval start, null means now
     * @param endInstant  interval end, null means now
     * @param type  which set of fields this duration supports, null means AllType
     */
    public AbstractDuration(
            ReadableInstant startInstant, ReadableInstant  endInstant, DurationType type) {
        super();
        type = (type == null ? DurationType.getAllType() : type);
        if (startInstant == null && endInstant == null) {
            iType = type;
        } else {
            long start = (startInstant == null ? DateTimeUtils.currentTimeMillis() : startInstant.getMillis());
            long end = (endInstant == null ? DateTimeUtils.currentTimeMillis() : endInstant.getMillis());
            iType = type;
            // Only call a private method
            setTotalMillis(type, start, end);
        }
    }

    /**
     * Creates a duration from the given millisecond duration. If any supported
     * fields are imprecise, an UnsupportedOperationException is thrown. The
     * exception to this is when the specified duration is zero.
     *
     * @param duration  the duration, in milliseconds
     * @param type  which set of fields this duration supports, null means MillisType
     */
    public AbstractDuration(long duration, DurationType type) {
        super();
        type = (type == null ? DurationType.getMillisType() : type);
        iType = type;
        setTotalMillis(type, duration); // Only call a private method
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the object which defines which fields this duration supports.
     */
    public final DurationType getDurationType() {
        return iType;
    }

    /**
     * Gets the total length of this duration in milliseconds, 
     * failing if the duration is imprecise.
     *
     * @return the total length of the duration in milliseconds.
     * @throws IllegalStateException if the duration is imprecise
     */
    public final long getTotalMillis() {
        int state = iTotalMillisState;
        if (state == 0) {
            state = updateTotalMillis();
        }
        if (state != 2) {
            throw new IllegalStateException("Duration is imprecise");
        }
        return iTotalMillis;
    }

    /**
     * Is this duration a precise length of time, or descriptive.
     * <p>
     * A precise duration could include millis, seconds, minutes or hours.
     * However, days, weeks, months and years can vary in length, resulting in
     * an imprecise duration.
     * <p>
     * An imprecise duration can be made precise by pairing it with a
     * date in a {@link ReadableInterval}.
     *
     * @return true if the duration is precise
     */
    public final boolean isPrecise() {
        int state = iTotalMillisState;
        if (state == 0) {
            state = updateTotalMillis();
        }
        return state == 2;
    }

    /**
     * Walks through the field values, determining total millis and whether
     * this duration is precise.
     *
     * @return new state
     */
    private int updateTotalMillis() {
        final DurationType type = iType;

        boolean isPrecise = true;
        long totalMillis = 0;

        DurationField field;
        int value; // used to lock fields against threading issues
        value = iYears;
        if (value != 0) {
            field = type.years();
            if (isPrecise &= field.isPrecise()) {
                totalMillis += field.getMillis(value);
            }
        }
        value = iMonths;
        if (value != 0) {
            field = type.months();
            if (isPrecise &= field.isPrecise()) {
                totalMillis += field.getMillis(value);
            }
        }
        value = iWeeks;
        if (value != 0) {
            field = type.weeks();
            if (isPrecise &= field.isPrecise()) {
                totalMillis += field.getMillis(value);
            }
        }
        value = iDays;
        if (value != 0) {
            field = type.days();
            if (isPrecise &= field.isPrecise()) {
                totalMillis += field.getMillis(value);
            }
        }
        value = iHours;
        if (value != 0) {
            field = type.hours();
            if (isPrecise &= field.isPrecise()) {
                totalMillis += field.getMillis(value);
            }
        }
        value = iMinutes;
        if (value != 0) {
            field = type.minutes();
            if (isPrecise &= field.isPrecise()) {
                totalMillis += field.getMillis(value);
            }
        }
        value = iSeconds;
        if (value != 0) {
            field = type.seconds();
            if (isPrecise &= field.isPrecise()) {
                totalMillis += field.getMillis(value);
            }
        }
        value = iMillis;
        if (value != 0) {
            field = type.millis();
            if (isPrecise &= field.isPrecise()) {
                totalMillis += field.getMillis(value);
            }
        }

        if (isPrecise) {
            iTotalMillis = totalMillis;
            return iTotalMillisState = 2;
        } else {
            iTotalMillis = totalMillis;
            return iTotalMillisState = 1;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Adds this duration to the given instant, returning a new value.
     * <p>
     * To add just once, pass in a scalar of one. To subtract once, pass
     * in a scalar of minus one.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to add the
     * duration to
     * @param scalar  the number of times to add the duration, negative to subtract
     * @return milliseconds value plus this duration times scalar
     * @throws ArithmeticException if the result of the calculation is too large
     */
    public final long addTo(long instant, int scalar) {
        return addTo(instant, scalar, null);
    }

    /**
     * Adds this duration to the given instant, returning a new value.
     * <p>
     * To add just once, pass in a scalar of one. To subtract once, pass
     * in a scalar of minus one.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to add the
     * duration to
     * @param scalar  the number of times to add the duration, negative to subtract
     * @param chrono  override the duration's chronology, unless null is passed in
     * @return milliseconds value plus this duration times scalar
     * @throws ArithmeticException if the result of the calculation is too large
     */
    public final long addTo(long instant, int scalar, Chronology chrono) {
        if (isPrecise()) {
            return FieldUtils.safeAdd(instant, getTotalMillis() * scalar);
        }
        
        DurationType type = iType;
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
     * Adds this duration to the given instant, returning a new Instant.
     * <p>
     * To add just once, pass in a scalar of one. To subtract once, pass
     * in a scalar of minus one.
     *
     * @param instant  the instant to add the duration to, null means now
     * @param scalar  the number of times to add the duration, negative to subtract
     * @return instant with the original value plus this duration times scalar
     * @throws ArithmeticException if the result of the calculation is too large
     */
    public final Instant addTo(ReadableInstant instant, int scalar) {
        if (instant == null) {
            return new Instant(addTo(DateTimeUtils.currentTimeMillis(), scalar));
        }
        return new Instant(addTo(instant.getMillis(), scalar));
    }

    /**
     * Adds this duration into the given mutable instant.
     * <p>
     * To add just once, pass in a scalar of one. To subtract once, pass
     * in a scalar of minus one.
     *
     * @param instant  the instant to update with the added duration, must not be null
     * @param scalar  the number of times to add the duration, negative to subtract
     * @throws IllegalArgumentException if the instant is null
     * @throws ArithmeticException if the result of the calculation is too large
     */
    public final void addInto(ReadWritableInstant instant, int scalar) {
        if (instant == null) {
            throw new IllegalArgumentException("The instant must not be null");
        }
        instant.setMillis(addTo(instant.getMillis(), scalar));
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the years field part of the duration.
     * 
     * @return the number of years in the duration, zero if unsupported
     */
    public final int getYears() {
        return iYears;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the months field part of the duration.
     * 
     * @return the number of months in the duration, zero if unsupported
     */
    public final int getMonths() {
        return iMonths;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the weeks field part of the duration.
     * 
     * @return the number of weeks in the duration, zero if unsupported
     */
    public final int getWeeks() {
        return iWeeks;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the days field part of the duration.
     * 
     * @return the number of days in the duration, zero if unsupported
     */
    public final int getDays() {
        return iDays;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the hours field part of the duration.
     * 
     * @return the number of hours in the duration, zero if unsupported
     */
    public final int getHours() {
        return iHours;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the minutes field part of the duration.
     * 
     * @return the number of minutes in the duration, zero if unsupported
     */
    public final int getMinutes() {
        return iMinutes;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the seconds field part of the duration.
     * 
     * @return the number of seconds in the duration, zero if unsupported
     */
    public final int getSeconds() {
        return iSeconds;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the millis field part of the duration.
     * 
     * @return the number of millis in the duration, zero if unsupported
     */
    public final int getMillis() {
        return iMillis;
    }

    //-----------------------------------------------------------------------
    /**
     * Get this object as an immutable Duration. This can be useful if you
     * don't trust the implementation of the interface to be well-behaved, or
     * to get a guaranteed immutable object.
     * 
     * @return a Duration using the same field set and values
     */
    public final Duration toDuration() {
        if (this instanceof Duration) {
            return (Duration) this;
        }
        return new Duration(this);
    }

    /**
     * Get this object as a MutableDuration.
     * 
     * @return a MutableDuration using the same field set and values
     */
    public final MutableDuration toMutableDuration() {
        return new MutableDuration(this);
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this duration with the specified duration, which can only be
     * performed if both are precise.
     *
     * @param obj  a precise duration to check against
     * @return negative value if this is less, 0 if equal, or positive value if greater
     * @throws NullPointerException if the object is null
     * @throws ClassCastException if the given object is not supported
     * @throws IllegalStateException if either duration is imprecise
     */
    public int compareTo(Object obj) {
        // Comparable contract means we cannot handle null or other types gracefully
        ReadableDuration thisDuration = (ReadableDuration) this;
        ReadableDuration otherDuration = (ReadableDuration) obj;
        
        long thisMillis = thisDuration.getTotalMillis();
        long otherMillis = otherDuration.getTotalMillis();
        
        // cannot do (thisMillis - otherMillis) as it can overflow
        if (thisMillis < otherMillis) {
            return -1;
        }
        if (thisMillis > otherMillis) {
            return 1;
        }
        return 0;
    }

    /**
     * Is the length of this duration equal to the duration passed in.
     * Both durations must be precise.
     *
     * @param duration  another duration to compare to, null means zero milliseconds
     * @return true if this duration is equal to than the duration passed in
     * @throws IllegalStateException if either duration is imprecise
     */
    public boolean isEqual(ReadableDuration duration) {
        if (duration == null) {
            duration = Duration.ZERO;
        }
        return compareTo(duration) == 0;
    }

    /**
     * Is the length of this duration longer than the duration passed in.
     * Both durations must be precise.
     *
     * @param duration  another duration to compare to, null means zero milliseconds
     * @return true if this duration is equal to than the duration passed in
     * @throws IllegalStateException if either duration is imprecise
     */
    public boolean isLongerThan(ReadableDuration duration) {
        if (duration == null) {
            duration = Duration.ZERO;
        }
        return compareTo(duration) > 0;
    }

    /**
     * Is the length of this duration shorter than the duration passed in.
     * Both durations must be precise.
     *
     * @param duration  another duration to compare to, null means zero milliseconds
     * @return true if this duration is equal to than the duration passed in
     * @throws IllegalStateException if either duration is imprecise
     */
    public boolean isShorterThan(ReadableDuration duration) {
        if (duration == null) {
            duration = Duration.ZERO;
        }
        return compareTo(duration) < 0;
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this object with the specified object for equality based
     * on the value of each field. All ReadableDuration instances are accepted.
     * <p>
     * To compare two durations for absolute duration (ie. millisecond duration
     * ignoring the fields), use {@link #isEqual(ReadableDuration)} or
     * {@link #compareTo(Object)}.
     *
     * @param readableDuration  a readable duration to check against
     * @return true if all the field values are equal, false if
     *  not or the duration is null or of an incorrect type
     */
    public boolean equals(Object readableDuration) {
        if (this == readableDuration) {
            return true;
        }
        if (readableDuration instanceof ReadableDuration == false) {
            return false;
        }
        ReadableDuration other = (ReadableDuration) readableDuration;
        DurationType type = getDurationType();
        if (type.equals(other.getDurationType()) == false) {
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
     * Gets a hash code for the duration that is compatable with the 
     * equals method.
     *
     * @return a hash code
     */
    public int hashCode() {
        int hash = getDurationType().hashCode();
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
        return ISODurationFormat.getInstance().standard().print(this);
    }

    /**
     * Sets all the fields in one go from another ReadableDuration.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     * 
     * @param duration  the duration to set, null means zero length duration
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    protected void setDuration(ReadableDuration duration) {
        if (duration == null) {
            setTotalMillis(iType, 0L);
        } else {
            setDuration(iType, duration);
        }
    }

    /**
     * This method is private to prevent subclasses from overriding.
     */
    private void setDuration(DurationType type, ReadableDuration duration) {
        setDuration(type,
                    duration.getYears(), duration.getMonths(),
                    duration.getWeeks(), duration.getDays(),
                    duration.getHours(), duration.getMinutes(),
                    duration.getSeconds(), duration.getMillis());
        if (type.equals(duration.getDurationType()) && duration.isPrecise()) {
            iTotalMillis = duration.getTotalMillis();
            iTotalMillisState = 2;
        }
    }

    /**
     * Sets all the fields in one go.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     * 
     * @param years  amount of years in this duration, which must be zero if unsupported
     * @param months  amount of months in this duration, which must be zero if unsupported
     * @param weeks  amount of weeks in this duration, which must be zero if unsupported
     * @param days  amount of days in this duration, which must be zero if unsupported
     * @param hours  amount of hours in this duration, which must be zero if unsupported
     * @param minutes  amount of minutes in this duration, which must be zero if unsupported
     * @param seconds  amount of seconds in this duration, which must be zero if unsupported
     * @param millis  amount of milliseconds in this duration, which must be zero if unsupported
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    protected void setDuration(int years, int months, int weeks, int days,
                               int hours, int minutes, int seconds, int millis) {
        setDuration(iType, years, months, weeks, days, hours, minutes, seconds, millis);
    }

    /**
     * This method is private to prevent subclasses from overriding.
     */
    private void setDuration(DurationType type,
                             int years, int months, int weeks, int days,
                             int hours, int minutes, int seconds, int millis) {
        if (years != 0) {
            checkArgument(type.years(), "years");
        }
        if (months != 0) {
            checkArgument(type.months(), "months");
        }
        if (weeks != 0) {
            checkArgument(type.weeks(), "weeks");
        }
        if (days != 0) {
            checkArgument(type.days(), "days");
        }
        if (hours != 0) {
            checkArgument(type.hours(), "hours");
        }
        if (minutes != 0) {
            checkArgument(type.minutes(), "minutes");
        }
        if (seconds != 0) {
            checkArgument(type.seconds(), "seconds");
        }
        if (millis != 0) {
            checkArgument(type.millis(), "millis");
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

        iTotalMillisState = 0;
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
    protected void setTotalMillis(long startInstant, long endInstant) {
        setTotalMillis(iType, startInstant, endInstant);
    }

    /**
     * This method is private to prevent subclasses from overriding.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     */
    private void setTotalMillis(DurationType type, long startInstant, long endInstant) {
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
        // (end - start) is excess to be discarded
        iTotalMillis = baseTotalMillis - (endInstant - startInstant);
        iTotalMillisState = 2;
    }

    /**
     * Sets all the fields in one go from a millisecond duration. If any
     * supported fields are imprecise, an UnsupportedOperationException is
     * thrown. The exception to this is when the specified duration is zero.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     * 
     * @param duration  the duration, in milliseconds
     */
    protected void setTotalMillis(long duration) {
        setTotalMillis(iType, duration);
    }

    /**
     * This method is private to prevent subclasses from overriding.
     *
     * @param duration  the duration, in milliseconds
     */
    private void setTotalMillis(DurationType type, long duration) {
        if (duration == 0) {
            iTotalMillis = duration;
            iTotalMillisState = 2;

            iYears = 0;
            iMonths = 0;
            iWeeks = 0;
            iDays = 0;
            iHours = 0;
            iMinutes = 0;
            iSeconds = 0;
            iMillis = 0;

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
        // (end - start) is excess to be discarded
        iTotalMillis = duration - (duration - startInstant);
        iTotalMillisState = 2;
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a millisecond duration to this one. As a side-effect, all field
     * values are normalized.
     * 
     * @param duration  the duration, in milliseconds
     * @throws IllegalStateException if the duration is imprecise
     */
    protected void add(long duration) {
        setTotalMillis(getTotalMillis() + duration);
    }
    
    /**
     * Adds a duration to this one.
     * 
     * @param duration  the duration to add, mulls means add nothing
     * @throws IllegalStateException if the duration is imprecise
     */
    protected void add(ReadableDuration duration) {
        if (duration != null) {
            add(duration.getTotalMillis());
        }
    }
    
    /**
     * Normalizes all the field values in this duration.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     *
     * @throws IllegalStateException if this duration is imprecise
     */
    protected void normalize() {
        setTotalMillis(getTotalMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of years of the duration.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     * 
     * @param years  the number of years
     * @throws UnsupportedOperationException if field is not supported.
     */
    protected void setYears(int years) {
        if (years != iYears) {
            if (years != 0) {
                checkSupport(iType.years(), "years");
            }
            iYears = years;
            iTotalMillisState = 0;
        }
    }

    /**
     * Adds the specified years to the number of years in the duration.
     * 
     * @param years  the number of years
     * @throws UnsupportedOperationException if field is not supported.
     */
    protected void addYears(int years) {
        setYears(getYears() + years);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of months of the duration.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     * 
     * @param months  the number of months
     * @throws UnsupportedOperationException if field is not supported.
     */
    protected void setMonths(int months) {
        if (months != iMonths) {
            if (months != 0) {
                checkSupport(iType.months(), "months");
            }
            iMonths = months;
            iTotalMillisState = 0;
        }
    }

    /**
     * Adds the specified months to the number of months in the duration.
     * 
     * @param months  the number of months
     * @throws UnsupportedOperationException if field is not supported.
     */
    protected void addMonths(int months) {
        setMonths(getMonths() + months);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of weeks of the duration.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     * 
     * @param weeks  the number of weeks
     * @throws UnsupportedOperationException if field is not supported.
     */
    protected void setWeeks(int weeks) {
        if (weeks != iWeeks) {
            if (weeks != 0) {
                checkSupport(iType.weeks(), "weeks");
            }
            iWeeks = weeks;
            iTotalMillisState = 0;
        }
    }

    /**
     * Adds the specified weeks to the number of weeks in the duration.
     * 
     * @param weeks  the number of weeks
     * @throws UnsupportedOperationException if field is not supported.
     */
    protected void addWeeks(int weeks) {
        setWeeks(getWeeks() + weeks);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of days of the duration.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     * 
     * @param days  the number of days
     * @throws UnsupportedOperationException if field is not supported.
     */
    protected void setDays(int days) {
        if (days != iDays) {
            if (days != 0) {
                checkSupport(iType.days(), "days");
            }
            iDays = days;
            iTotalMillisState = 0;
        }
    }

    /**
     * Adds the specified days to the number of days in the duration.
     * 
     * @param days  the number of days
     * @throws UnsupportedOperationException if field is not supported.
     */
    protected void addDays(int days) {
        setDays(getDays() + days);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of hours of the duration.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     * 
     * @param hours  the number of hours
     * @throws UnsupportedOperationException if field is not supported.
     */
    protected void setHours(int hours) {
        if (hours != iHours) {
            if (hours != 0) {
                checkSupport(iType.hours(), "hours");
            }
            iHours = hours;
            iTotalMillisState = 0;
        }
    }

    /**
     * Adds the specified hours to the number of hours in the duration.
     * 
     * @param hours  the number of hours
     * @throws UnsupportedOperationException if field is not supported.
     */
    protected void addHours(int hours) {
        setHours(getHours() + hours);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of minutes of the duration.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     * 
     * @param minutes  the number of minutes
     * @throws UnsupportedOperationException if field is not supported.
     */
    protected void setMinutes(int minutes) {
        if (minutes != iMinutes) {
            if (minutes != 0) {
                checkSupport(iType.minutes(), "minutes");
            }
            iMinutes = minutes;
            iTotalMillisState = 0;
        }
    }

    /**
     * Adds the specified minutes to the number of minutes in the duration.
     * 
     * @param minutes  the number of minutes
     * @throws UnsupportedOperationException if field is not supported.
     */
    protected void addMinutes(int minutes) {
        setMinutes(getMinutes() + minutes);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of seconds of the duration.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     * 
     * @param seconds  the number of seconds
     * @throws UnsupportedOperationException if field is not supported.
     */
    protected void setSeconds(int seconds) {
        if (seconds != iSeconds) {
            if (seconds != 0) {
                checkSupport(iType.seconds(), "seconds");
            }
            iSeconds = seconds;
            iTotalMillisState = 0;
        }
    }

    /**
     * Adds the specified seconds to the number of seconds in the duration.
     * 
     * @param seconds  the number of seconds
     * @throws UnsupportedOperationException if field is not supported.
     */
    protected void addSeconds(int seconds) {
        setSeconds(getSeconds() + seconds);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of millis of the duration.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     * 
     * @param millis  the number of millis
     * @throws UnsupportedOperationException if field is not supported.
     */
    protected void setMillis(int millis) {
        if (millis != iMillis) {
            if (millis != 0) {
                checkSupport(iType.millis(), "millis");
            }
            iMillis = millis;
            iTotalMillisState = 0;
        }
    }

    /**
     * Adds the specified millis to the number of millis in the duration.
     * 
     * @param millis  the number of millis
     * @throws UnsupportedOperationException if field is not supported.
     */
    protected void addMillis(int millis) {
        setMillis(getMillis() + millis);
    }

}
