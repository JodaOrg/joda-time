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
import org.joda.time.DateTimeUtils;
import org.joda.time.Duration;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
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
 * data in a <code>PeriodType</code> and an <code>int[]</code>.
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

    /** The type of period */
    private PeriodType iType;
    /** The values */
    private int[] iValues;

    //-----------------------------------------------------------------------
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
        setPeriodInternal(years, months, weeks, days, hours, minutes, seconds, millis); // internal method
    }

    /**
     * Creates a period from the given interval endpoints.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     * @param type  which set of fields this period supports, null means standard
     * @param chrono  the chronology to use, null means ISO default
     * @throws IllegalArgumentException if period type is invalid
     */
    protected BasePeriod(long startInstant, long endInstant, PeriodType type, Chronology chrono) {
        super();
        type = checkPeriodType(type);
        iType = type;
        setPeriodInternal(startInstant, endInstant, chrono); // internal method
    }

    /**
     * Creates a period from the given interval endpoints.
     *
     * @param startInstant  interval start, null means now
     * @param endInstant  interval end, null means now
     * @param type  which set of fields this period supports, null means standard
     * @throws IllegalArgumentException if period type is invalid
     */
    protected BasePeriod(ReadableInstant startInstant, ReadableInstant  endInstant, PeriodType type) {
        super();
        type = checkPeriodType(type);
        if (startInstant == null && endInstant == null) {
            iType = type;
            iValues = new int[size()];
        } else {
            long start = DateTimeUtils.getInstantMillis(startInstant);
            long end = DateTimeUtils.getInstantMillis(endInstant);
            Chronology chrono = (startInstant != null ? startInstant.getChronology() : endInstant.getChronology());
            iType = type;
            setPeriodInternal(start, end, chrono); // internal method
        }
    }

    /**
     * Creates a period from the given millisecond duration, which is only really
     * suitable for durations less than one day.
     * <p>
     * Only fields that are precise will be used.
     * Thus the largest precise field may have a large value.
     *
     * @param duration  the duration, in milliseconds
     * @param type  which set of fields this period supports, null means standard
     * @param chrono  the chronology to use, null means ISO default
     * @throws IllegalArgumentException if period type is invalid
     */
    protected BasePeriod(long duration, PeriodType type, Chronology chrono) {
        super();
        type = checkPeriodType(type);
        iType = type;
        setPeriodInternal(duration, chrono); // internal method
    }

    /**
     * Creates a new period based on another using the {@link ConverterManager}.
     *
     * @param period  the period to convert
     * @param type  which set of fields this period supports, null means use type from object
     * @param chrono  the chronology to use, null means ISO default
     * @throws IllegalArgumentException if period is invalid
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    protected BasePeriod(Object period, PeriodType type, Chronology chrono) {
        super();
        PeriodConverter converter = ConverterManager.getInstance().getPeriodConverter(period);
        type = (type == null ? converter.getPeriodType(period) : type);
        type = checkPeriodType(type);
        iType = type;
        if (this instanceof ReadWritablePeriod) {
            iValues = new int[size()];
            chrono = DateTimeUtils.getChronology(chrono);
            converter.setInto((ReadWritablePeriod) this, period, chrono);
        } else {
            setPeriodInternal(new MutablePeriod(period, type, chrono));
        }
    }

    /**
     * Constructor used when we trust ourselves.
     * Do not expose publically.
     *
     * @param values  the values to use, not null, not cloned
     * @param type  which set of fields this period supports, not null
     */
    protected BasePeriod(int[] values, PeriodType type) {
        super();
        iType = type;
        iValues = values;
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
     * Gets the period type.
     *
     * @return the period type
     */
    public PeriodType getPeriodType() {
        return iType;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the number of fields that this period supports.
     *
     * @return the number of fields supported
     */
    public int size() {
        return iType.size();
    }

    /**
     * Gets the field type at the specified index.
     *
     * @param index  the index to retrieve
     * @return the field at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public DurationFieldType getFieldType(int index) {
        return iType.getFieldType(index);
    }

    /**
     * Gets the value at the specified index.
     *
     * @param index  the index to retrieve
     * @return the value of the field at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public int getValue(int index) {
        return iValues[index];
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the total millisecond duration of this period relative to a start instant.
     * <p>
     * This method adds the period to the specifed instant.
     * The difference between the start instant and the result of the add is the duration
     *
     * @param startInstant  the instant to add the period to, thus obtaining the duration
     * @return the total length of the period as a duration relative to the start instant
     * @throws ArithmeticException if the millis exceeds the capacity of the duration
     */
    public Duration toDurationFrom(ReadableInstant startInstant) {
        long startMillis = DateTimeUtils.getInstantMillis(startInstant);
        Chronology chrono = DateTimeUtils.getInstantChronology(startInstant);
        long endMillis = chrono.add(startMillis, this, 1);
        return new Duration(startMillis, endMillis);
    }

    //-----------------------------------------------------------------------
    /**
     * Checks whether a field type is supported, and if so adds the new value
     * to the relevent index in the specified array.
     * 
     * @param type  the field type
     * @param values  the array to update
     * @param newValue  the new value to store if successful
     */
    private void checkAndUpdate(DurationFieldType type, int[] values, int newValue) {
        int index = indexOf(type);
        if (index == -1) {
            if (newValue != 0) {
                throw new IllegalArgumentException(
                    "Period does not support field '" + type.getName() + "'");
            }
        } else {
            values[index] = newValue;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets all the fields of this period from another.
     * 
     * @param period  the period to copy from, not null
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    protected void setPeriod(ReadablePeriod period) {
        if (period == null) {
            setPeriodInternal(0L, null);
        } else {
            setPeriodInternal(period);
        }
    }

    /**
     * Private method called from constructor.
     */
    private void setPeriodInternal(ReadablePeriod period) {
        int[] newValues = new int[size()];
        for (int i = 0, isize = period.size(); i < isize; i++) {
            DurationFieldType type = period.getFieldType(i);
            int value = period.getValue(i);
            checkAndUpdate(type, newValues, value);
        }
        iValues = newValues;
    }

    /**
     * Sets the eight standard the fields in one go.
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
        setPeriodInternal(years, months, weeks, days, hours, minutes, seconds, millis);
    }

    /**
     * Private method called from constructor.
     */
    private void setPeriodInternal(int years, int months, int weeks, int days,
                                   int hours, int minutes, int seconds, int millis) {
        int[] newValues = new int[size()];
        checkAndUpdate(DurationFieldType.years(), newValues, years);
        checkAndUpdate(DurationFieldType.months(), newValues, months);
        checkAndUpdate(DurationFieldType.weeks(), newValues, weeks);
        checkAndUpdate(DurationFieldType.days(), newValues, days);
        checkAndUpdate(DurationFieldType.hours(), newValues, hours);
        checkAndUpdate(DurationFieldType.minutes(), newValues, minutes);
        checkAndUpdate(DurationFieldType.seconds(), newValues, seconds);
        checkAndUpdate(DurationFieldType.millis(), newValues, millis);
        iValues = newValues;
    }

    /**
     * Sets all the fields in one go from a millisecond interval.
     * 
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     * @param chrono  the chronology to use, not null
     */
    protected void setPeriod(long startInstant, long endInstant, Chronology chrono) {
        setPeriodInternal(startInstant, endInstant, chrono);
    }

    /**
     * Private method called from constructor.
     */
    private void setPeriodInternal(long startInstant, long endInstant, Chronology chrono) {
        int[] newValues = new int[size()];
        if (startInstant == endInstant) {
            iValues = newValues;
        } else {
            for (int i = 0, isize = size(); i < isize; i++) {
                DurationField field = getFieldType(i).getField(chrono);
                int value = field.getDifference(endInstant, startInstant);
                startInstant = field.add(startInstant, value);
                newValues[i] = value;
            }
            iValues = newValues;
        }
    }

    /**
     * Sets all the fields in one go from a millisecond duration.
     * <p>
     * This calculates the period relative to 1970-01-01 but only sets those
     * fields which are precise.
     * 
     * @param duration  the duration, in milliseconds
     * @throws ArithmeticException if the set exceeds the capacity of the period
     * @param chrono  the chronology to use, not null
     */
    protected void setPeriod(long duration, Chronology chrono) {
        setPeriodInternal(duration, chrono);
    }

    /**
     * Private method called from constructor.
     */
    private void setPeriodInternal(long duration, Chronology chrono) {
        int[] newValues = new int[size()];
        if (duration == 0) {
            iValues = newValues;
        } else {
            long current = 0;
            for (int i = 0, isize = size(); i < isize; i++) {
                DurationField field = getFieldType(i).getField(chrono);
                if (field.isPrecise()) {
                    int value = field.getDifference(duration, current);
                    current = field.add(current, value);
                    newValues[i] = value;
                }
            }
            iValues = newValues;
        }
    }

    /**
     * Sets the value of a field in this period.
     * 
     * @param field  the field to set
     * @param value  the value to set
     * @throws UnsupportedOperationException if field is not supported.
     */
    protected void setField(DurationFieldType field, int value) {
        int index = indexOf(field);
        if (index == -1) {
            if (value != 0) {
                throw new UnsupportedOperationException(
                    "Period does not support field '" + field.getName() + "'");
            }
        } else {
            setValue(index, value);
        }
    }

    /**
     * Adds the fields from another period.
     * 
     * @param period  the period to add from, not null
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    protected void addPeriod(ReadablePeriod period) {
         int[] newValues = getValues(); // already cloned
         for (int i = 0, isize = period.size(); i < isize; i++) {
             DurationFieldType type = period.getFieldType(i);
             int value = period.getValue(i);
             int index = indexOf(type);
             if (index == -1) {
                 if (value != 0) {
                     throw new IllegalArgumentException(
                         "Period does not support field '" + type.getName() + "'");
                 }
             } else {
                 newValues[index] = FieldUtils.safeAdd(getValue(index), value);
             }
         }
         setValues(newValues);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the value of the field at the specifed index.
     * 
     * @param index  the index
     * @param value  the value to set
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    protected void setValue(int index, int value) {
        if (value != getValue(index)) {
            iValues[index] = value;
        }
    }

    /**
     * Sets the values of all fields.
     * 
     * @param values  the array of values
     */
    protected void setValues(int[] values) {
        iValues = values;
    }

}
