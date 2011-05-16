/*
 *  Copyright 2001-2010 Stephen Colebourne
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

import org.joda.convert.ToString;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;
import org.joda.time.format.FormatUtils;

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
public abstract class AbstractDuration implements ReadableDuration {

    /**
     * Constructor.
     */
    protected AbstractDuration() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * Get this duration as an immutable <code>Duration</code> object.
     * 
     * @return a Duration created using the millisecond duration from this instance
     */
    public Duration toDuration() {
        return new Duration(getMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this duration to a Period instance using the standard period type
     * and the ISO chronology.
     * <p>
     * Only precise fields in the period type will be used. Thus, only the hour,
     * minute, second and millisecond fields on the period will be used.
     * The year, month, week and day fields will not be populated.
     * <p>
     * If the duration is small, less than one day, then this method will perform
     * as you might expect and split the fields evenly.
     * If the duration is larger than one day then all the remaining duration will
     * be stored in the largest available field, hours in this case.
     * <p>
     * For example, a duration effectively equal to (365 + 60 + 5) days will be
     * converted to ((365 + 60 + 5) * 24) hours by this constructor.
     * <p>
     * For more control over the conversion process, you must pair the duration with
     * an instant, see {@link Period#Period(ReadableInstant,ReadableDuration)}.
     * 
     * @return a Period created using the millisecond duration from this instance
     */
    public Period toPeriod() {
        return new Period(getMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this duration with the specified duration based on length.
     *
     * @param other  a duration to check against
     * @return negative value if this is less, 0 if equal, or positive value if greater
     * @throws NullPointerException if the object is null
     * @throws ClassCastException if the given object is not supported
     */
    public int compareTo(ReadableDuration other) {
        long thisMillis = this.getMillis();
        long otherMillis = other.getMillis();
        
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
     *
     * @param duration  another duration to compare to, null means zero milliseconds
     * @return true if this duration is equal to than the duration passed in
     */
    public boolean isEqual(ReadableDuration duration) {
        if (duration == null) {
            duration = Duration.ZERO;
        }
        return compareTo(duration) == 0;
    }

    /**
     * Is the length of this duration longer than the duration passed in.
     *
     * @param duration  another duration to compare to, null means zero milliseconds
     * @return true if this duration is equal to than the duration passed in
     */
    public boolean isLongerThan(ReadableDuration duration) {
        if (duration == null) {
            duration = Duration.ZERO;
        }
        return compareTo(duration) > 0;
    }

    /**
     * Is the length of this duration shorter than the duration passed in.
     *
     * @param duration  another duration to compare to, null means zero milliseconds
     * @return true if this duration is equal to than the duration passed in
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
     * on the millisecond length. All ReadableDuration instances are accepted.
     *
     * @param duration  a readable duration to check against
     * @return true if the length of the duration is equal
     */
    public boolean equals(Object duration) {
        if (this == duration) {
            return true;
        }
        if (duration instanceof ReadableDuration == false) {
            return false;
        }
        ReadableDuration other = (ReadableDuration) duration;
        return (getMillis() == other.getMillis());
    }

    /**
     * Gets a hash code for the duration that is compatible with the 
     * equals method.
     *
     * @return a hash code
     */
    public int hashCode() {
        long len = getMillis();
        return (int) (len ^ (len >>> 32));
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the value as a String in the ISO8601 duration format including
     * only seconds and milliseconds.
     * <p>
     * For example, "PT72.345S" represents 1 minute, 12 seconds and 345 milliseconds.
     * <p>
     * For more control over the output, see
     * {@link org.joda.time.format.PeriodFormatterBuilder PeriodFormatterBuilder}.
     *
     * @return the value as an ISO8601 string
     */
    @ToString
    public String toString() {
        long millis = getMillis();
        StringBuffer buf = new StringBuffer();
        buf.append("PT");
        boolean negative = (millis < 0);
        FormatUtils.appendUnpaddedInteger(buf, millis);
        while (buf.length() < (negative ? 7 : 6)) {
            buf.insert(negative ? 3 : 2, "0");
        }
        if ((millis / 1000) * 1000 == millis) {
            buf.setLength(buf.length() - 3);
        } else {
            buf.insert(buf.length() - 3, ".");
        }
        buf.append('S');
        return buf.toString();
    }

}
