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

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
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
public abstract class AbstractPeriod implements ReadablePeriod {

    /**
     * Constructor.
     */
    protected AbstractPeriod() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets an array of the field types that this period supports.
     * <p>
     * The fields are returned largest to smallest, for example Hours, Minutes, Seconds.
     *
     * @return the fields supported in an array that may be altered, largest to smallest
     */
    public DurationFieldType[] getFieldTypes() {
        DurationFieldType[] result = new DurationFieldType[size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = getFieldType(i);
        }
        return result;
    }

    /**
     * Gets an array of the value of each of the fields that this period supports.
     * <p>
     * The fields are returned largest to smallest, for example Hours, Minutes, Seconds.
     * Each value corresponds to the same array index as <code>getFields()</code>
     *
     * @return the current values of each field in an array that may be altered, largest to smallest
     */
    public int[] getValues() {
        int[] result = new int[size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = getValue(i);
        }
        return result;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the value of one of the fields.
     * <p>
     * If the field type specified is not supported by the period then zero
     * is returned.
     *
     * @param field  the field type to query, null returns zero
     * @return the value of that field, zero if field not supported
     */
    public int get(DurationFieldType type) {
        int index = indexOf(type);
        if (index == -1) {
            return 0;
        }
        return getValue(index);
    }

    /**
     * Checks whether the field specified is supported by this period.
     *
     * @param type  the type to check, may be null which returns false
     * @return true if the field is supported
     */
    public boolean isSupported(DurationFieldType type) {
        return getPeriodType().isSupported(type);
    }

    /**
     * Gets the index of the field in this period.
     *
     * @param type  the type to check, may be null which returns -1
     * @return the index of -1 if not supported
     */
    public int indexOf(DurationFieldType type) {
        return getPeriodType().indexOf(type);
    }

    //-----------------------------------------------------------------------
    /**
     * Adds this period to the given instant, returning a new value.
     * <p>
     * The addition uses ISOChronology in the default zone.
     * To add just once, pass in a scalar of one. To subtract once, pass
     * in a scalar of minus one.
     *
     * @param instant  the millisecond instant to add the period to
     * @param scalar  the number of times to add the period, negative to subtract
     * @return milliseconds value plus this period times scalar
     * @throws ArithmeticException if the result of the calculation is too large
     */
    public long addTo(long instant, int scalar) {
        return addTo(instant, scalar, null);
    }

    /**
     * Adds this period to the given instant, returning a new value.
     * <p>
     * The addition uses the chronology specified, or ISOChronology
     * in the default zone if it is null.
     * To add just once, pass in a scalar of one. To subtract once, pass
     * in a scalar of minus one.
     *
     * @param instant  the millisecond instant to add the period to
     * @param scalar  the number of times to add the period, negative to subtract
     * @param chrono  the chronology to use, null means ISO in the default zone
     * @return milliseconds value plus this period times scalar
     * @throws ArithmeticException if the result of the calculation is too large
     */
    public long addTo(long instant, int scalar, Chronology chrono) {
        if (scalar != 0) {
            chrono = DateTimeUtils.getChronology(chrono);
            for (int i = 0, isize = size(); i < isize; i++) {
                long value = getValue(i); // use long to allow for multiplication (fits OK)
                if (value != 0) {
                    instant = getFieldType(i).getField(chrono).add(instant, value * scalar);
                }
            }
        }
        return instant;
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
     * Gets the total millisecond duration of this period relative to a start
     * instant and chronology.
     * <p>
     * This method adds the period to the specifed instant.
     * The difference between the start instant and the result of the add is the duration
     *
     * @param startInstant  the instant to add the period to, thus obtaining the duration
     * @param chrono  the chronology to use
     * @return the total length of the period in milliseconds relative to the start instant
     * @throws ArithmeticException if the millis exceeds the capacity of the duration
     */
    public long toDurationMillisFrom(long startInstant, Chronology chrono) {
        long endInstant = addTo(startInstant, 1, chrono);
        return FieldUtils.safeAdd(endInstant, -startInstant);
    }

    /**
     * Gets the total millisecond duration of this period relative to a start
     * instant and chronology.
     * <p>
     * This method adds the period to the specifed instant.
     * The difference between the start instant and the result of the add is the duration
     *
     * @param startInstant  the instant to add the period to, thus obtaining the duration
     * @return the total length of the period in milliseconds relative to the start instant
     * @throws ArithmeticException if the millis exceeds the capacity of the duration
     */
    public Duration toDurationFrom(ReadableInstant startInstant) {
        long millis = DateTimeUtils.getInstantMillis(startInstant);
        Chronology chrono = DateTimeUtils.getInstantChronology(startInstant);
        return new Duration(toDurationMillisFrom(millis, chrono));
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this object with the specified object for equality based
     * on the value of each field. All ReadablePeriod instances are accepted.
     *
     * @param readablePeriod  a readable period to check against
     * @return true if all the field values are equal, false if
     *  not or the period is null or of an incorrect type
     */
    public boolean equals(Object period) {
        if (this == period) {
            return true;
        }
        if (period instanceof ReadablePeriod == false) {
            return false;
        }
        ReadablePeriod other = (ReadablePeriod) period;
        if (size() != other.size()) {
            return false;
        }
        for (int i = 0, isize = size(); i < isize; i++) {
            if (getValue(i) != other.getValue(i) || getFieldType(i) != other.getFieldType(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets a hash code for the period as defined by ReadablePeriod.
     *
     * @return a hash code
     */
    public int hashCode() {
        int total = 17;
        for (int i = 0, isize = size(); i < isize; i++) {
            total = 27 * total + getValue(i);
            total = 27 * total + getFieldType(i).hashCode();
        }
        return total;
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
