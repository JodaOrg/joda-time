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

import java.io.Serializable;

/**
 * MutableInterval is the standard implementation of a mutable time interval.
 * <p>
 * If performing significant calculations on an interval, it may be faster to
 * convert an Interval object to a MutableInterval one.
 * <p>
 * MutableInterval is mutable and not thread-safe, unless concurrent threads
 * are not invoking mutator methods.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class MutableInterval extends AbstractInterval
        implements ReadWritableInterval, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = -5982824024992428470L;

    /**
     * Constructs a time interval as a copy of another.
     * 
     * @param interval the time interval to copy
     * @throws IllegalArgumentException if the interval is null
     */
    public MutableInterval(ReadableInterval interval) {
        super(interval);
    }
    
    /**
     * Constructs a time interval as a copy of another.
     * 
     * @param interval the time interval to convert
     * @throws IllegalArgumentException if the interval is null
     */
    public MutableInterval(Object interval) {
        super(interval);
    }

    /**
     * Constructs an interval from a start and end instant.
     * 
     * @param startInstant  start of this interval, as milliseconds from
     *  1970-01-01T00:00:00Z.
     * @param endInstant  end of this interval, as milliseconds from
     *  1970-01-01T00:00:00Z.
     */
    public MutableInterval(long startInstant, long endInstant) {
        super(startInstant, endInstant);
    }
    
    /**
     * Constructs an interval from a start and end instant.
     * 
     * @param start  start of this interval
     * @param end  end of this interval
     * @throws IllegalArgumentException if either instant is null
     */
    public MutableInterval(ReadableInstant start, ReadableInstant end) {
        super(start, end);
    }
    
    /**
     * Constructs an interval from a start instant and a duration.
     * 
     * @param start  start of this interval
     * @param duration  duration of this interval
     * @throws IllegalArgumentException if start or duration is null
     */
    public MutableInterval(ReadableInstant start, ReadableDuration duration) {
        super(start, duration);
    }
    
    /**
     * Constructs an interval from a duration and an end instant.
     * 
     * @param duration duration of this interval
     * @param end end of this interval
     * @throws IllegalArgumentException if duration or end is null
     */
    public MutableInterval(ReadableDuration duration, ReadableInstant end) {
        super(duration, end);
    }
    
    //-----------------------------------------------------------------------
    /**
     * Sets the start of this time interval.
     *
     * @param millisInstant  the start of the time interval,
     *  millisecond instant from 1970-01-01T00:00:00Z
     */
    public void setStartMillis(long millisInstant) {
        super.setStartMillis(millisInstant);
    }

    /**
     * Sets the start of this time interval as an Instant.
     *
     * @param instant  the start of the time interval
     */
    public void setStartInstant(ReadableInstant instant) {
        if (instant == null) {
            throw new IllegalArgumentException("The instant must not be null");
        }
        super.setStartMillis(instant.getMillis());
    }

    /** 
     * Sets the end of this time interval.
     *
     * @param millisInstant  the end of the time interval,
     *  millisecond instant from 1970-01-01T00:00:00Z
     */
    public void setEndMillis(long millisInstant) {
        super.setEndMillis(millisInstant);
    }

    /** 
     * Sets the end of this time interval as an Instant.
     *
     * @param instant  the end of the time interval
     */
    public void setEndInstant(ReadableInstant instant) {
        if (instant == null) {
            throw new IllegalArgumentException("The instant must not be null");
        }
        super.setEndMillis(instant.getMillis());
    }

    /**
     * Sets the duration of this time interval, preserving the start instant.
     *
     * @param millisDuration  new duration for interval
     */
    public void setDurationAfterStart(long millisDuration) {
        super.setEndMillis(getStartMillis() + millisDuration);
    }

    /**
     * Sets the duration of this time interval, preserving the start instant.
     *
     * @param duration  new duration for interval
     */
    public void setDurationAfterStart(ReadableDuration duration) {
        super.setDurationAfterStart(duration);
    }

    /**
     * Sets the duration of this time interval, preserving the end instant.
     *
     * @param millisDuration  new duration for interval
     */
    public void setDurationBeforeEnd(long millisDuration) {
        super.setStartMillis(getEndMillis() - millisDuration);
    }

    /**
     * Sets the duration of this time interval, preserving the end instant.
     *
     * @param duration  new duration for interval
     */
    public void setDurationBeforeEnd(ReadableDuration duration) {
        super.setDurationBeforeEnd(duration);
    }

}
