/* 
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-03 Stephen Colebourne.
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

/**
 * Standard immutable duration implementation split on any set of fields.
 * <p>
 * A duration can be divided into a number of fields, such as hours and seconds.
 * The way in which that divide occurs is controlled by the DurationType class.
 * <p>
 * Duration is thread-safe and immutable, provided that the DurationType is
 * as well. All standard DurationType classes supplied are thread-safe and
 * immutable.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 * @see MutableDuration
 */
public class Duration extends AbstractDuration implements ReadableDuration, Serializable {

    static final long serialVersionUID = 741052353876488155L;

    /**
     * Copies another duration to this one.
     *
     * @throws UnsupportedOperationException if an unsupported field's value is
     * non-zero
     */
    public Duration(ReadableDuration duration) {
        super(duration);
    }

    /**
     * Creates a zero length duration.
     *
     * @param type determines which set of fields this duration supports
     */
    public Duration(DurationType type) {
        super(type);
    }

    /**
     * Copies another duration to this one.
     *
     * @param type use a different DurationType
     * @param duration duration to copy
     * @throws UnsupportedOperationException if an unsupported field's value is
     * non-zero
     */
    public Duration(DurationType type, ReadableDuration duration) {
        super(type, duration);
    }

    /**
     * Copies another duration to this one.
     *
     * @param type use a different DurationType
     * @param duration duration to convert
     * @throws UnsupportedOperationException if an unsupported field's value is
     * non-zero
     */
    public Duration(DurationType type, Object duration) {
        super(type, duration);
    }

    /**
     * Create a duration from a set of field values.
     *
     * @param type determines which set of fields this duration supports
     * @param years amount of years in this duration, which must be zero if
     * unsupported.
     * @param months amount of months in this duration, which must be zero if
     * unsupported.
     * @param weeks amount of weeks in this duration, which must be zero if
     * unsupported.
     * @param days amount of days in this duration, which must be zero if
     * unsupported.
     * @param hours amount of hours in this duration, which must be zero if
     * unsupported.
     * @param minutes amount of minutes in this duration, which must be zero if
     * unsupported.
     * @param seconds amount of seconds in this duration, which must be zero if
     * unsupported.
     * @param millis amount of milliseconds in this duration, which must be
     * zero if unsupported.
     * @throws UnsupportedOperationException if an unsupported field's value is
     * non-zero
     */
    public Duration(DurationType type,
                    int years, int months, int weeks, int days,
                    int hours, int minutes, int seconds, int millis) {
        super(type, years, months, weeks, days, hours, minutes, seconds, millis);
    }

    /**
     * Creates a duration from the given interval endpoints.
     *
     * @param type determines which set of fields this duration supports
     * @param startInstant interval start, in milliseconds
     * @param endInstant interval end, in milliseconds
     */
    public Duration(DurationType type, long startInstant, long endInstant) {
        super(type, startInstant, endInstant);
    }

    /**
     * Creates a duration from the given interval endpoints.
     *
     * @param type determines which set of fields this duration supports
     * @param startInstant interval start
     * @param endInstant interval end
     */
    public Duration(DurationType type,
                    ReadableInstant startInstant, ReadableInstant endInstant) {
        super(type, startInstant, endInstant);
    }

    /**
     * Creates a duration from the given millisecond duration. If any supported
     * fields are imprecise, an UnsupportedOperationException is thrown. The
     * exception to this is when the specified duration is zero.
     *
     * @param type determines which set of fields this duration supports
     * @param duration  the duration, in milliseconds
     * @throws UnsupportedOperationException if any fields are imprecise
     */
    public Duration(DurationType type, long duration) {
        super(type, duration);
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are
     * immutable.
     */
    protected final void setDuration(ReadableDuration duration) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are
     * immutable.
     */
    protected final void setDuration(int years, int months, int weeks, int days,
                                     int hours, int minutes, int seconds, int millis) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are
     * immutable.
     */
    protected final void setTotalMillis(long startInstant, long endInstant) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are
     * immutable.
     */
    protected final void setTotalMillis(long duration) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are
     * immutable.
     */
    protected final void normalize() {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are
     * immutable.
     */
    protected final void setYears(int years) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are
     * immutable.
     */
    protected final void setMonths(int months) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are
     * immutable.
     */
    protected final void setWeeks(int weeks) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are
     * immutable.
     */
    protected final void setDays(int days) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are
     * immutable.
     */
    protected final void setHours(int hours) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are
     * immutable.
     */
    protected final void setMinutes(int minutes) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are
     * immutable.
     */
    protected final void setSeconds(int seconds) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are
     * immutable.
     */
    protected final void setMillis(int millis) {
    }

}
