/*
 *  Copyright 2001-2005 Stephen Colebourne
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
package org.joda.time.base;

import java.io.Serializable;

import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.DurationConverter;
import org.joda.time.field.FieldUtils;

/**
 * BaseDuration is an abstract implementation of ReadableDuration that stores
 * data in a <code>long</code> duration milliseconds field.
 * <p>
 * This class should generally not be used directly by API users.
 * The {@link ReadableDuration} interface should be used when different 
 * kinds of duration objects are to be referenced.
 * <p>
 * BaseDuration subclasses may be mutable and not thread-safe.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public abstract class BaseDuration
        extends AbstractDuration
        implements ReadableDuration, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 2581698638990L;

    /** The duration length */
    private long iMillis;

    /**
     * Creates a duration from the given millisecond duration.
     *
     * @param duration  the duration, in milliseconds
     */
    protected BaseDuration(long duration) {
        super();
        iMillis = duration;
    }

    /**
     * Creates a duration from the given interval endpoints.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     * @throws ArithmeticException if the duration exceeds a 64 bit long
     */
    protected BaseDuration(long startInstant, long endInstant) {
        super();
        iMillis = FieldUtils.safeAdd(endInstant, -startInstant);
    }

    /**
     * Creates a duration from the given interval endpoints.
     *
     * @param start  interval start, null means now
     * @param end  interval end, null means now
     * @throws ArithmeticException if the duration exceeds a 64 bit long
     */
    protected BaseDuration(ReadableInstant start, ReadableInstant end) {
        super();
        if (start == end) {
            iMillis = 0L;
        } else {
            long startMillis = DateTimeUtils.getInstantMillis(start);
            long endMillis = DateTimeUtils.getInstantMillis(end);
            iMillis = FieldUtils.safeAdd(endMillis, -startMillis);
        }
    }

    /**
     * Creates a duration from the specified object using the
     * {@link org.joda.time.convert.ConverterManager ConverterManager}.
     *
     * @param duration  duration to convert
     * @throws IllegalArgumentException if duration is invalid
     */
    protected BaseDuration(Object duration) {
        super();
        DurationConverter converter = ConverterManager.getInstance().getDurationConverter(duration);
        iMillis = converter.getDurationMillis(duration);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the length of this duration in milliseconds.
     *
     * @return the length of the duration in milliseconds.
     */
    public long getMillis() {
        return iMillis;
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the length of this duration in milliseconds.
     * 
     * @param duration  the new length of the duration
     */
    protected void setMillis(long duration) {
        iMillis = duration;
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this duration to a Period instance using the specified period type
     * and the ISO chronology.
     * <p>
     * Only precise fields in the period type will be used.
     * At most these are hours, minutes, seconds and millis - the period
     * type may restrict the selection further.
     * <p>
     * For more control over the conversion process, you must pair the duration with
     * an instant, see {@link #toPeriodFrom(ReadableInstant, PeriodType)}.
     * 
     * @param type  the period type to use, null means standard
     * @return a Period created using the millisecond duration from this instance
     */
    public Period toPeriod(PeriodType type) {
        return new Period(getMillis(), type);
    }

    /**
     * Converts this duration to a Period instance using the standard period type
     * and the specified chronology.
     * <p>
     * Only precise fields in the period type will be used.
     * Exactly which fields are precise depends on the chronology.
     * Only the time fields are precise for ISO chronology with a time zone.
     * However, ISO UTC also has precise days and weeks.
     * <p>
     * For more control over the conversion process, you must pair the duration with
     * an instant, see {@link #toPeriodFrom(ReadableInstant)} and
     * {@link #toPeriodTo(ReadableInstant)}
     * 
     * @param chrono  the chronology to use, null means ISO default
     * @return a Period created using the millisecond duration from this instance
     */
    public Period toPeriod(Chronology chrono) {
        return new Period(getMillis(), chrono);
    }

    /**
     * Converts this duration to a Period instance using the specified period type
     * and chronology.
     * <p>
     * Only precise fields in the period type will be used.
     * Exactly which fields are precise depends on the chronology.
     * Only the time fields are precise for ISO chronology with a time zone.
     * However, ISO UTC also has precise days and weeks.
     * <p>
     * For more control over the conversion process, you must pair the duration with
     * an instant, see {@link #toPeriodFrom(ReadableInstant, PeriodType)} and
     * {@link #toPeriodTo(ReadableInstant, PeriodType)}
     * 
     * @param type  the period type to use, null means standard
     * @param chrono  the chronology to use, null means ISO default
     * @return a Period created using the millisecond duration from this instance
     */
    public Period toPeriod(PeriodType type, Chronology chrono) {
        return new Period(getMillis(), type, chrono);
    }

    /**
     * Converts this duration to a Period instance by adding the duration to a start
     * instant to obtain an interval using the standard period type.
     * <p>
     * This conversion will determine the fields of a period accurately.
     * The results are based on the instant millis, the chronology of the instant,
     * the standard period type and the length of this duration.
     * 
     * @param startInstant  the instant to calculate the period from, null means now
     * @return a Period created using the millisecond duration from this instance
     */
    public Period toPeriodFrom(ReadableInstant startInstant) {
        return new Period(startInstant, this);
    }

    /**
     * Converts this duration to a Period instance by adding the duration to a start
     * instant to obtain an interval.
     * <p>
     * This conversion will determine the fields of a period accurately.
     * The results are based on the instant millis, the chronology of the instant,
     * the period type and the length of this duration.
     * 
     * @param startInstant  the instant to calculate the period from, null means now
     * @param type  the period type determining how to split the duration into fields, null means All type
     * @return a Period created using the millisecond duration from this instance
     */
    public Period toPeriodFrom(ReadableInstant startInstant, PeriodType type) {
        return new Period(startInstant, this, type);
    }

    /**
     * Converts this duration to a Period instance by subtracting the duration
     * from an end instant to obtain an interval using the standard period
     * type.
     * <p>
     * This conversion will determine the fields of a period accurately.
     * The results are based on the instant millis, the chronology of the instant,
     * the standard period type and the length of this duration.
     * 
     * @param endInstant  the instant to calculate the period to, null means now
     * @return a Period created using the millisecond duration from this instance
     */
    public Period toPeriodTo(ReadableInstant endInstant) {
        return new Period(this, endInstant);
    }

    /**
     * Converts this duration to a Period instance by subtracting the duration
     * from an end instant to obtain an interval using the standard period
     * type.
     * <p>
     * This conversion will determine the fields of a period accurately.
     * The results are based on the instant millis, the chronology of the instant,
     * the period type and the length of this duration.
     * 
     * @param endInstant  the instant to calculate the period to, null means now
     * @param type  the period type determining how to split the duration into fields, null means All type
     * @return a Period created using the millisecond duration from this instance
     */
    public Period toPeriodTo(ReadableInstant endInstant, PeriodType type) {
        return new Period(this, endInstant, type);
    }

    /**
     * Converts this duration to an Interval starting at the specified instant.
     * 
     * @param startInstant  the instant to start the interval at, null means now
     * @return an Interval starting at the specified instant
     */
    public Interval toIntervalFrom(ReadableInstant startInstant) {
        return new Interval(startInstant, this);
    }

    /**
     * Converts this duration to an Interval ending at the specified instant.
     * 
     * @param endInstant  the instant to end the interval at, null means now
     * @return an Interval ending at the specified instant
     */
    public Interval toIntervalTo(ReadableInstant endInstant) {
        return new Interval(this, endInstant);
    }

}
