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
package org.joda.time.chrono;

import java.io.Serializable;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.UnsupportedDateTimeField;
import org.joda.time.field.UnsupportedDurationField;

/**
 * BaseChronology provides a skeleton implementation for chronology
 * classes. Many utility methods are defined, but all fields are unsupported.
 * <p>
 * BaseChronology is thread-safe and immutable, and all subclasses must be
 * as well.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public abstract class BaseChronology
        extends Chronology
        implements Serializable {

    /** Serialization version. */
    private static final long serialVersionUID = -7310865996721419676L;

    /**
     * Restricted constructor.
     */
    protected BaseChronology() {
        super();
    }

    /**
     * Returns the DateTimeZone that this Chronology operates in, or null if
     * unspecified.
     *
     * @return DateTimeZone null if unspecified
     */
    public abstract DateTimeZone getZone();

    /**
     * Returns an instance of this Chronology that operates in the UTC time
     * zone. Chronologies that do not operate in a time zone or are already
     * UTC must return themself.
     *
     * @return a version of this chronology that ignores time zones
     */
    public abstract Chronology withUTC();
    
    /**
     * Returns an instance of this Chronology that operates in any time zone.
     *
     * @return a version of this chronology with a specific time zone
     * @param zone to use, or default if null
     * @see org.joda.time.chrono.ZonedChronology
     */
    public abstract Chronology withZone(DateTimeZone zone);

    /**
     * Returns a datetime millisecond instant, formed from the given year,
     * month, day, and millisecond values. The set of given values must refer
     * to a valid datetime, or else an IllegalArgumentException is thrown.
     * <p>
     * The default implementation calls upon separate DateTimeFields to
     * determine the result. Subclasses are encouraged to provide a more
     * efficient implementation.
     *
     * @param year year to use
     * @param monthOfYear month to use
     * @param dayOfMonth day of month to use
     * @param millisOfDay millisecond to use
     * @return millisecond instant from 1970-01-01T00:00:00Z
     */
    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int millisOfDay)
        throws IllegalArgumentException
    {
        long instant = year().set(0, year);
        instant = monthOfYear().set(instant, monthOfYear);
        instant = dayOfMonth().set(instant, dayOfMonth);
        return millisOfDay().set(instant, millisOfDay);
    }

    /**
     * Returns a datetime millisecond instant, formed from the given year,
     * month, day, hour, minute, second, and millisecond values. The set of
     * given values must refer to a valid datetime, or else an
     * IllegalArgumentException is thrown.
     * <p>
     * The default implementation calls upon separate DateTimeFields to
     * determine the result. Subclasses are encouraged to provide a more
     * efficient implementation.
     *
     * @param year year to use
     * @param monthOfYear month to use
     * @param dayOfMonth day of month to use
     * @param hourOfDay hour to use
     * @param minuteOfHour minute to use
     * @param secondOfMinute second to use
     * @param millisOfSecond millisecond to use
     * @return millisecond instant from 1970-01-01T00:00:00Z
     */
    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        long instant = year().set(0, year);
        instant = monthOfYear().set(instant, monthOfYear);
        instant = dayOfMonth().set(instant, dayOfMonth);
        instant = hourOfDay().set(instant, hourOfDay);
        instant = minuteOfHour().set(instant, minuteOfHour);
        instant = secondOfMinute().set(instant, secondOfMinute);
        return millisOfSecond().set(instant, millisOfSecond);
    }

    /**
     * Returns a datetime millisecond instant, from from the given instant,
     * hour, minute, second, and millisecond values. The set of given values
     * must refer to a valid datetime, or else an IllegalArgumentException is
     * thrown.
     * <p>
     * The default implementation calls upon separate DateTimeFields to
     * determine the result. Subclasses are encouraged to provide a more
     * efficient implementation.
     *
     * @param instant instant to start from
     * @param hourOfDay hour to use
     * @param minuteOfHour minute to use
     * @param secondOfMinute second to use
     * @param millisOfSecond millisecond to use
     * @return millisecond instant from 1970-01-01T00:00:00Z
     */
    public long getDateTimeMillis(long instant,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        instant = hourOfDay().set(instant, hourOfDay);
        instant = minuteOfHour().set(instant, minuteOfHour);
        instant = secondOfMinute().set(instant, secondOfMinute);
        return millisOfSecond().set(instant, millisOfSecond);
    }

    //-----------------------------------------------------------------------
    /**
     * Validates whether the fields stored in a partial instant are valid.
     * <p>
     * This implementation uses {@link DateTimeField#getMinimumValue(ReadablePartial, int[])}
     * and {@link DateTimeField#getMaximumValue(ReadablePartial, int[])}.
     *
     * @param partial  the partial instant to validate
     * @param values  the values to validate, not null unless the partial is empty
     * @throws IllegalArgumentException if the instant is invalid
     */
    public void validate(ReadablePartial partial, int[] values) {
        // check values in standard range, catching really stupid cases like -1
        // this means that the second check will not hit trouble
        int size = partial.size();
        for (int i = 0; i < size; i++) {
            int value = values[i];
            DateTimeField field = partial.getField(i);
            if (value < field.getMinimumValue()) {
                throw new IllegalFieldValueException
                    (field.getType(), Integer.valueOf(value),
                     Integer.valueOf(field.getMinimumValue()), null);
            }
            if (value > field.getMaximumValue()) {
                throw new IllegalFieldValueException
                    (field.getType(), Integer.valueOf(value),
                     null, Integer.valueOf(field.getMaximumValue()));
            }
        }
        // check values in specific range, catching really odd cases like 30th Feb
        for (int i = 0; i < size; i++) {
            int value = values[i];
            DateTimeField field = partial.getField(i);
            if (value < field.getMinimumValue(partial, values)) {
                throw new IllegalFieldValueException
                    (field.getType(), Integer.valueOf(value),
                     Integer.valueOf(field.getMinimumValue(partial, values)), null);
            }
            if (value > field.getMaximumValue(partial, values)) {
                throw new IllegalFieldValueException
                    (field.getType(), Integer.valueOf(value),
                     null, Integer.valueOf(field.getMaximumValue(partial, values)));
            }
        }
    }

    /**
     * Gets the values of a partial from an instant.
     *
     * @param partial  the partial instant to use
     * @param instant  the instant to query
     * @return the values of the partial extracted from the instant
     */
    public int[] get(ReadablePartial partial, long instant) {
        int size = partial.size();
        int[] values = new int[size];
        for (int i = 0; i < size; i++) {
            values[i] = partial.getFieldType(i).getField(this).get(instant);
        }
        return values;
    }

    /**
     * Sets the partial into the instant.
     *
     * @param partial  the partial instant to use
     * @param instant  the instant to update
     * @return the updated instant
     */
    public long set(ReadablePartial partial, long instant) {
        for (int i = 0, isize = partial.size(); i < isize; i++) {
            instant = partial.getFieldType(i).getField(this).set(instant, partial.getValue(i));
        }
        return instant;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the values of a period from an interval.
     *
     * @param period  the period instant to use
     * @param startInstant  the start instant of an interval to query
     * @param endInstant  the start instant of an interval to query
     * @return the values of the period extracted from the interval
     */
    public int[] get(ReadablePeriod period, long startInstant, long endInstant) {
        int size = period.size();
        int[] values = new int[size];
        if (startInstant != endInstant) {
            for (int i = 0; i < size; i++) {
                DurationField field = period.getFieldType(i).getField(this);
                int value = field.getDifference(endInstant, startInstant);
                if (value != 0) {
                    startInstant = field.add(startInstant, value);
                }
                values[i] = value;
            }
        }
        return values;
    }

    /**
     * Gets the values of a period from an interval.
     *
     * @param period  the period instant to use
     * @param duration  the duration to query
     * @return the values of the period extracted from the duration
     */
    public int[] get(ReadablePeriod period, long duration) {
        int size = period.size();
        int[] values = new int[size];
        if (duration != 0) {
            long current = 0;
            for (int i = 0; i < size; i++) {
                DurationField field = period.getFieldType(i).getField(this);
                if (field.isPrecise()) {
                    int value = field.getDifference(duration, current);
                    current = field.add(current, value);
                    values[i] = value;
                }
            }
        }
        return values;
    }

    /**
     * Adds the period to the instant, specifying the number of times to add.
     *
     * @param period  the period to add, null means add nothing
     * @param instant  the instant to add to
     * @param scalar  the number of times to add
     * @return the updated instant
     */
    public long add(ReadablePeriod period, long instant, int scalar) {
        if (scalar != 0 && period != null) {
            for (int i = 0, isize = period.size(); i < isize; i++) {
                long value = period.getValue(i); // use long to allow for multiplication (fits OK)
                if (value != 0) {
                    instant = period.getFieldType(i).getField(this).add(instant, value * scalar);
                }
            }
        }
        return instant;
    }

    //-----------------------------------------------------------------------
    /**
     * Adds the duration to the instant, specifying the number of times to add.
     *
     * @param instant  the instant to add to
     * @param duration  the duration to add
     * @param scalar  the number of times to add
     * @return the updated instant
     */
    public long add(long instant, long duration, int scalar) {
        if (duration == 0 || scalar == 0) {
            return instant;
        }
        long add = FieldUtils.safeMultiply(duration, scalar);
        return FieldUtils.safeAdd(instant, add);
    }

    // Millis
    //-----------------------------------------------------------------------
    /**
     * Get the millis duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField millis() {
        return UnsupportedDurationField.getInstance(DurationFieldType.millis());
    }

    /**
     * Get the millis of second field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField millisOfSecond() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.millisOfSecond(), millis());
    }

    /**
     * Get the millis of day field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField millisOfDay() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.millisOfDay(), millis());
    }

    // Second
    //-----------------------------------------------------------------------
    /**
     * Get the seconds duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField seconds() {
        return UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    }

    /**
     * Get the second of minute field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField secondOfMinute() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.secondOfMinute(), seconds());
    }

    /**
     * Get the second of day field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField secondOfDay() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.secondOfDay(), seconds());
    }

    // Minute
    //-----------------------------------------------------------------------
    /**
     * Get the minutes duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField minutes() {
        return UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    }

    /**
     * Get the minute of hour field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField minuteOfHour() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.minuteOfHour(), minutes());
    }

    /**
     * Get the minute of day field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField minuteOfDay() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.minuteOfDay(), minutes());
    }

    // Hour
    //-----------------------------------------------------------------------
    /**
     * Get the hours duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField hours() {
        return UnsupportedDurationField.getInstance(DurationFieldType.hours());
    }

    /**
     * Get the hour of day (0-23) field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField hourOfDay() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.hourOfDay(), hours());
    }

    /**
     * Get the hour of day (offset to 1-24) field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField clockhourOfDay() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.clockhourOfDay(), hours());
    }

    // Halfday
    //-----------------------------------------------------------------------
    /**
     * Get the halfdays duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField halfdays() {
        return UnsupportedDurationField.getInstance(DurationFieldType.halfdays());
    }

    /**
     * Get the hour of am/pm (0-11) field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField hourOfHalfday() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.hourOfHalfday(), hours());
    }

    /**
     * Get the hour of am/pm (offset to 1-12) field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField clockhourOfHalfday() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.clockhourOfHalfday(), hours());
    }

    /**
     * Get the AM(0) PM(1) field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField halfdayOfDay() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.halfdayOfDay(), halfdays());
    }

    // Day
    //-----------------------------------------------------------------------
    /**
     * Get the days duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField days() {
        return UnsupportedDurationField.getInstance(DurationFieldType.days());
    }

    /**
     * Get the day of week field for this chronology.
     *
     * <p>DayOfWeek values are defined in
     * {@link org.joda.time.DateTimeConstants DateTimeConstants}.
     * They use the ISO definitions, where 1 is Monday and 7 is Sunday.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField dayOfWeek() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.dayOfWeek(), days());
    }

    /**
     * Get the day of month field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField dayOfMonth() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.dayOfMonth(), days());
    }

    /**
     * Get the day of year field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField dayOfYear() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.dayOfYear(), days());
    }

    // Week
    //-----------------------------------------------------------------------
    /**
     * Get the weeks duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField weeks() {
        return UnsupportedDurationField.getInstance(DurationFieldType.weeks());
    }

    /**
     * Get the week of a week based year field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField weekOfWeekyear() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.weekOfWeekyear(), weeks());
    }

    // Weekyear
    //-----------------------------------------------------------------------
    /**
     * Get the weekyears duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField weekyears() {
        return UnsupportedDurationField.getInstance(DurationFieldType.weekyears());
    }

    /**
     * Get the year of a week based year field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField weekyear() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.weekyear(), weekyears());
    }

    /**
     * Get the year of a week based year in a century field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField weekyearOfCentury() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.weekyearOfCentury(), weekyears());
    }

    // Month
    //-----------------------------------------------------------------------
    /**
     * Get the months duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField months() {
        return UnsupportedDurationField.getInstance(DurationFieldType.months());
    }

    /**
     * Get the month of year field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField monthOfYear() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.monthOfYear(), months());
    }

    // Year
    //-----------------------------------------------------------------------
    /**
     * Get the years duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField years() {
        return UnsupportedDurationField.getInstance(DurationFieldType.years());
    }

    /**
     * Get the year field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField year() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.year(), years());
    }

    /**
     * Get the year of era field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField yearOfEra() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.yearOfEra(), years());
    }

    /**
     * Get the year of century field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField yearOfCentury() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.yearOfCentury(), years());
    }

    // Century
    //-----------------------------------------------------------------------
    /**
     * Get the centuries duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField centuries() {
        return UnsupportedDurationField.getInstance(DurationFieldType.centuries());
    }

    /**
     * Get the century of era field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField centuryOfEra() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.centuryOfEra(), centuries());
    }

    // Era
    //-----------------------------------------------------------------------
    /**
     * Get the eras duration field for this chronology.
     * 
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField eras() {
        return UnsupportedDurationField.getInstance(DurationFieldType.eras());
    }

    /**
     * Get the era field for this chronology.
     * 
     * @return DateTimeField or UnsupportedDateTimeField if unsupported
     */
    public DateTimeField era() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.era(), eras());
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a debugging toString.
     * 
     * @return a debugging string
     */
    public abstract String toString();

}
