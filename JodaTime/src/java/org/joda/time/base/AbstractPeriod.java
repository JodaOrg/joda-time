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

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.Duration;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadWritableInstant;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.ISOPeriodFormat;

/**
 * AbstractPeriod provides the common behaviour for period classes.
 * <p>
 * This class should generally not be used directly by API users. The 
 * {@link ReadablePeriod} interface should be used when different 
 * kinds of periods are to be referenced.
 * <p>
 * AbstractPeriod subclasses may be mutable and not thread-safe.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public abstract class AbstractPeriod
        implements ReadablePeriod, Serializable {

    /**
     * Constructor.
     */
    protected AbstractPeriod() {
        super();
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
    public long addTo(long instant, int scalar) {
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
    public long addTo(long instant, int scalar, Chronology chrono) {
        if (isPrecise()) {
            return FieldUtils.safeAdd(instant, toDurationMillis() * scalar);
        }
        
        PeriodType type = getPeriodType();
        if (chrono != null) {
            type = type.withChronology(chrono);
        }
        
        long value; // used to lock fields against threading issues
        value = scaleValue(getYears(), scalar);
        if (value != 0) {
            instant = type.years().add(instant, value);
        }
        value = scaleValue(getMonths(), scalar);
        if (value != 0) {
            instant = type.months().add(instant, value);
        }
        value = scaleValue(getWeeks(), scalar);
        if (value != 0) {
            instant = type.weeks().add(instant, value);
        }
        value = scaleValue(getDays(), scalar);
        if (value != 0) {
            instant = type.days().add(instant, value);
        }
        value = scaleValue(getHours(), scalar);
        if (value != 0) {
            instant = type.hours().add(instant, value);
        }
        value = scaleValue(getMinutes(), scalar);
        if (value != 0) {
            instant = type.minutes().add(instant, value);
        }
        value = scaleValue(getSeconds(), scalar);
        if (value != 0) {
            instant = type.seconds().add(instant, value);
        }
        value = scaleValue(getMillis(), scalar);
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
     * instant (if present), returning a new DateTime.
     * <p>
     * To add just once, pass in a scalar of one. To subtract once, pass
     * in a scalar of minus one.
     *
     * @param instant  the instant to add the period to, null means now
     * @param scalar  the number of times to add the period, negative to subtract
     * @return datetime with the original value plus this period times scalar
     * @throws ArithmeticException if the result of the calculation is too large
     */
    public DateTime addTo(ReadableInstant instant, int scalar) {
        long instantMillis = DateTimeUtils.getInstantMillis(instant);
        Chronology chrono = DateTimeUtils.getInstantChronology(instant, null);
        return new DateTime(addTo(instantMillis, scalar, chrono), chrono);
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
    public void addInto(ReadWritableInstant instant, int scalar) {
        if (instant == null) {
            throw new IllegalArgumentException("The instant must not be null");
        }
        instant.setMillis(addTo(instant.getMillis(), scalar, instant.getChronology()));
    }

    //-----------------------------------------------------------------------
    /**
     * Get this period as an immutable <code>Period</code> object.
     * 
     * @return a Period using the same field set and values
     */
    public Period toPeriod() {
        return new Period(this);
    }

    /**
     * Get this object as a <code>MutablePeriod</code>.
     * <p>
     * This will always return a new <code>MutablePeriod</code> with the same fields.
     * 
     * @return a MutablePeriod using the same field set and values
     */
    public MutablePeriod toMutablePeriod() {
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
    public Duration toDuration() {
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
    public boolean equals(Object readablePeriod) {
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
     * Gets a hash code for the period that is compatible with the 
     * equals method. The hashcode is the period type hashcode plus
     * each period value from largest to smallest calculated as follows:
     *
     * @return a hash code
     */
    public int hashCode() {
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
     * <p>
     * For more control over the output, see
     * {@link org.joda.time.format.PeriodFormatterBuilder PeriodFormatterBuilder}.
     *
     * @return the value as an ISO8601 string
     */
    public String toString() {
        return ISOPeriodFormat.getInstance().standard().print(this);
    }

}
