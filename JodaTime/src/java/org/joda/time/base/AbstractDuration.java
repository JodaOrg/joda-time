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

import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableDuration;
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
     * <p>
     * This will either typecast this instance, or create a new <code>Duration</code>.
     * 
     * @return a Duration created using the millisecond duration from this instance
     */
    public Duration toDuration() {
        if (this instanceof Duration) {
            return (Duration) this;
        }
        return new Duration(getMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this duration to a Period instance using the All type.
     * <p>
     * Only precise fields in the period type will be used.
     * For AllType, this is the time fields only.
     * The year, month, week and day fields will not be populated.
     * The period constructed will always be precise.
     * <p>
     * If the duration is small, less than one day, then this method will perform
     * as you might expect and split the fields evenly.
     * <p>
     * If the duration is larger than one day then all the remaining duration will
     * be stored in the largest available precise field, hours in this case.
     * <p>
     * For example, a duration effectively equal to (365 + 60 + 5) days will be
     * converted to ((365 + 60 + 5) * 24) hours by this constructor.
     * <p>
     * For more control over the conversion process, you have two options:
     * <ul>
     * <li>convert the duration to an {@link org.joda.time.Interval}, and from
     * there obtain the period
     * <li>specify a period type that contains precise definitions of the day
     * and larger fields, such as the UTC or precise types.
     * </ul>
     * 
     * @return a Period created using the millisecond duration from this instance
     */
    public Period toPeriod() {
        return new Period(getMillis());
    }

    /**
     * Converts this duration to a Period instance specifying a period type
     * to control how the duration is split into fields.
     * <p>
     * Only precise fields in the period type will be used.
     * Imprecise fields will not be populated.
     * The period constructed will always be precise.
     * <p>
     * If the duration is small then this method will perform
     * as you might expect and split the fields evenly.
     * <p>
     * If the duration is large then all the remaining duration will
     * be stored in the largest available precise field.
     * For details as to which fields are precise, review the period type javadoc.
     * 
     * @param type  the period type determining how to split the duration into fields, null means All type
     * @return a Period created using the millisecond duration from this instance
     */
    public Period toPeriod(PeriodType type) {
        return new Period(getMillis(), type);
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this duration with the specified duration based on length.
     *
     * @param obj  a duration to check against
     * @return negative value if this is less, 0 if equal, or positive value if greater
     * @throws NullPointerException if the object is null
     * @throws ClassCastException if the given object is not supported
     */
    public int compareTo(Object obj) {
        // Comparable contract means we cannot handle null or other types gracefully
        ReadableDuration thisDuration = (ReadableDuration) this;
        ReadableDuration otherDuration = (ReadableDuration) obj;
        
        long thisMillis = thisDuration.getMillis();
        long otherMillis = otherDuration.getMillis();
        
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
     * Gets the value as a String in the ISO8601 duration format.
     * <p>
     * For example, "P6H3M7S" represents 6 hours, 3 minutes, 7 seconds.
     * The field values are determined using {@link #toPeriod()}.
     * <p>
     * For more control over the output, see
     * {@link org.joda.time.format.PeriodFormatterBuilder PeriodFormatterBuilder}.
     *
     * @return the value as an ISO8601 string
     */
    public String toString() {
        return ISOPeriodFormat.getInstance().standard().print(toPeriod());
    }

}
