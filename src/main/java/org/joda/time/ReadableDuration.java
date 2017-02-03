/*
 *  Copyright 2001-2009 Stephen Colebourne
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

/**
 * Defines an exact duration of time in milliseconds.
 * <p>
 * The implementation of this interface may be mutable or immutable. This
 * interface only gives access to retrieve data, never to change it.
 * <p>
 * Methods that are passed a duration as a parameter will treat <code>null</code>
 * as a zero length duration.
 * <p>
 * The {@code compareTo} method is no longer defined in this class in version 2.0.
 * Instead, the definition is simply inherited from the {@code Comparable} interface.
 * This approach is necessary to preserve binary compatibility.
 * The definition of the comparison is ascending order by millisecond duration.
 * Implementors are recommended to extend {@code AbstractInstant} instead of this interface.
 *
 * @see ReadableInterval
 * @see ReadablePeriod
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public interface ReadableDuration extends Comparable<ReadableDuration> {

    /**
     * Gets the total length of this duration in milliseconds.
     *
     * @return the total length of the time duration in milliseconds.
     */
    long getMillis();

    //-----------------------------------------------------------------------
    /**
     * Get this duration as an immutable <code>Duration</code> object.
     * <p>
     * This will either typecast this instance, or create a new <code>Duration</code>.
     * 
     * @return a Duration created using the millisecond duration from this instance
     */
    Duration toDuration();

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
    Period toPeriod();

    //-----------------------------------------------------------------------
    // Method is no longer defined here as that would break generic backwards compatibility
//    /**
//     * Compares this duration with the specified duration based on length.
//     *
//     * @param obj  a duration to check against
//     * @return negative value if this is less, 0 if equal, or positive value if greater
//     * @throws NullPointerException if the object is null
//     * @throws ClassCastException if the given object is not supported
//     */
//    int compareTo(ReadableDuration obj);

    /**
     * Is the length of this duration equal to the duration passed in.
     *
     * @param duration  another duration to compare to, null means zero milliseconds
     * @return true if this duration is equal to the duration passed in
     */
    boolean isEqual(ReadableDuration duration);

    /**
     * Is the length of this duration longer than the duration passed in.
     *
     * @param duration  another duration to compare to, null means zero milliseconds
     * @return true if this duration is greater than the duration passed in
     */
    boolean isLongerThan(ReadableDuration duration);

    /**
     * Is the length of this duration shorter than the duration passed in.
     *
     * @param duration  another duration to compare to, null means zero milliseconds
     * @return true if this duration is less than the duration passed in
     */
    boolean isShorterThan(ReadableDuration duration);

    //-----------------------------------------------------------------------
    /**
     * Compares this object with the specified object for equality based
     * on the millisecond length. All ReadableDuration instances are accepted.
     *
     * @param readableDuration  a readable duration to check against
     * @return true if the length of the duration is equal
     */
    boolean equals(Object readableDuration);

    /**
     * Gets a hash code for the duration that is compatible with the
     * equals method.
     * The following formula must be used:
     * <pre>
     *  long len = getMillis();
     *  return (int) (len ^ (len >>> 32));
     * </pre>
     *
     * @return a hash code
     */
    int hashCode();

    //-----------------------------------------------------------------------
    /**
     * Gets the value as a String in the ISO8601 duration format using hours,
     * minutes and seconds (including fractional milliseconds).
     * <p>
     * For example, "PT6H3M7S" represents 6 hours, 3 minutes, 7 seconds.
     *
     * @return the value as an ISO8601 string
     */
    String toString();

}
