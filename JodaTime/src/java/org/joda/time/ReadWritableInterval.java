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

/**
 * Writable interface for an interval.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public interface ReadWritableInterval extends ReadableInterval {

    /**
     * Sets this interval from two millisecond instants.
     *
     * @param startInstant  the start of the time interval
     * @param endInstant  the start of the time interval
     * @throws IllegalArgumentException if the end is before the start
     */
    void setInterval(long startInstant, long endInstant);

    /**
     * Sets this interval to be the same as another.
     *
     * @param interval  the interval to copy
     * @throws IllegalArgumentException if the end is before the start
     */
    void setInterval(ReadableInterval interval);

    /**
     * Sets this interval from two instants.
     *
     * @param startInstant  the start of the time interval
     * @param endInstant  the start of the time interval
     * @throws IllegalArgumentException if the end is before the start
     */
    void setInterval(ReadableInstant startInstant, ReadableInstant endInstant);

    //-----------------------------------------------------------------------
    /**
     * Sets the start of this time interval.
     *
     * @param millisInstant  the start of the time interval,
     *  millisecond instant from 1970-01-01T00:00:00Z
     * @throws IllegalArgumentException if the end is before the start
     */
    void setStartMillis(long millisInstant);

    /**
     * Sets the start of this time interval as an Instant.
     *
     * @param instant  the start of the time interval
     * @throws IllegalArgumentException if the end is before the start
     */
    void setStartInstant(ReadableInstant instant);

    //-----------------------------------------------------------------------
    /** 
     * Sets the end of this time interval.
     *
     * @param millisInstant  the end of the time interval,
     *  millisecond instant from 1970-01-01T00:00:00Z
     * @throws IllegalArgumentException if the end is before the start
     */
    void setEndMillis(long millisInstant);

    /** 
     * Sets the end of this time interval as an Instant.
     *
     * @param instant  the end of the time interval
     * @throws IllegalArgumentException if the end is before the start
     */
    void setEndInstant(ReadableInstant instant);

    //-----------------------------------------------------------------------
    /**
     * Sets the duration of this time interval, preserving the start instant.
     *
     * @param millisDuration  new duration for interval
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the end instant exceeds the capacity of a long
     */
    void setDurationAfterStart(long millisDuration);

    /**
     * Sets the duration of this time interval, preserving the end instant.
     *
     * @param millisDuration  new duration for interval
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the start instant exceeds the capacity of a long
     */
    void setDurationBeforeEnd(long millisDuration);

    //-----------------------------------------------------------------------
    /**
     * Sets the duration of this time interval, preserving the start instant.
     *
     * @param duration  new duration for interval
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the end instant exceeds the capacity of a long
     */
    void setDurationAfterStart(ReadableDuration duration);

    /**
     * Sets the duration of this time interval, preserving the end instant.
     *
     * @param duration  new duration for interval
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the start instant exceeds the capacity of a long
     */
    void setDurationBeforeEnd(ReadableDuration duration);

    //-----------------------------------------------------------------------
    /**
     * Sets the period of this time interval, preserving the start instant.
     *
     * @param period  new period for interval, null means zero length
     * @param chrono  the chronology to add using, null means ISO default
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the end instant exceeds the capacity of a long
     */
    void setPeriodAfterStart(ReadablePeriod period, Chronology chrono);

    /**
     * Sets the period of this time interval, preserving the end instant.
     *
     * @param period  new period for interval, null means zero length
     * @param chrono  the chronology to add using, null means ISO default
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the start instant exceeds the capacity of a long
     */
    void setPeriodBeforeEnd(ReadablePeriod period, Chronology chrono);

}
