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
package org.joda.time.chrono;

import org.joda.time.DurationField;

/**
 * Abstract datetime field class that defines its own DurationField, which
 * delegates back into this ImpreciseDateTimeField.
 * <p>
 * This DateTimeField is useful for defining DateTimeFields that are composed
 * of imprecise durations. If both duration fields are precise, then a
 * {@link PreciseDateTimeField} should be used instead.
 * <p>
 * When defining imprecise DateTimeFields where a matching DurationField is
 * already available, just extend AbstractDateTimeField directly so as not to
 * create redundant DurationField instances.
 * <p>
 * ImpreciseDateTimeField is thread-safe and immutable, and its subclasses must
 * be as well.
 *
 * @author Brian S O'Neill
 * @see PreciseDateTimeField
 */
public abstract class ImpreciseDateTimeField extends AbstractDateTimeField {

    static final long serialVersionUID = 7190739608550251860L;

    final long iUnitMillis;
    private final DurationField iDurationField;

    /**
     * Constructor.
     * 
     * @param name  short, descriptive name, like "monthOfYear".
     * @param durationName  short, descriptive name, like "months".
     * @param unitMillis  the average duration unit milliseconds
     */
    public ImpreciseDateTimeField(String name, String durationName, long unitMillis) {
        super(name);
        iUnitMillis = unitMillis;
        iDurationField = new LinkedDurationField(durationName);
    }

    public abstract int get(long instant);

    public abstract long set(long instant, int value);

    public abstract long add(long instant, int value);

    public abstract long add(long instant, long value);

    /**
     * Computes the difference between two instants, as measured in the units
     * of this field. Any fractional units are dropped from the result. Calling
     * getDifference reverses the effect of calling add. In the following code:
     *
     * <pre>
     * long instant = ...
     * int v = ...
     * int age = getDifference(add(instant, v), instant);
     * </pre>
     *
     * The value 'age' is the same as the value 'v'.
     * <p>
     * The default implementation call getDifferenceAsLong and converts the
     * return value to an int.
     *
     * @param minuendInstant the milliseconds from 1970-01-01T00:00:00Z to
     * subtract from
     * @param subtrahendInstant the milliseconds from 1970-01-01T00:00:00Z to
     * subtract off the minuend
     * @return the difference in the units of this field
     */
    public int getDifference(long minuendInstant, long subtrahendInstant) {
        return Utils.safeToInt(getDifferenceAsLong(minuendInstant, subtrahendInstant));
    }

    /**
     * Computes the difference between two instants, as measured in the units
     * of this field. Any fractional units are dropped from the result. Calling
     * getDifference reverses the effect of calling add. In the following code:
     *
     * <pre>
     * long instant = ...
     * long v = ...
     * long age = getDifferenceAsLong(add(instant, v), instant);
     * </pre>
     *
     * The value 'age' is the same as the value 'v'.
     * <p>
     * The default implementation performs a guess-and-check algorithm using
     * getDurationField().getUnitMillis() and the add() method. Subclasses are
     * encouraged to provide a more efficient implementation.
     *
     * @param minuendInstant the milliseconds from 1970-01-01T00:00:00Z to
     * subtract from
     * @param subtrahendInstant the milliseconds from 1970-01-01T00:00:00Z to
     * subtract off the minuend
     * @return the difference in the units of this field
     */
    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        if (minuendInstant < subtrahendInstant) {
            return -getDifferenceAsLong(subtrahendInstant, minuendInstant);
        }
        
        long difference = (minuendInstant - subtrahendInstant) / iUnitMillis;
        if (add(subtrahendInstant, difference) < minuendInstant) {
            do {
                difference++;
            } while (add(subtrahendInstant, difference) <= minuendInstant);
            difference--;
        } else if (add(subtrahendInstant, difference) > minuendInstant) {
            do {
                difference--;
            } while (add(subtrahendInstant, difference) > minuendInstant);
        }
        return difference;
    }

    public final DurationField getDurationField() {
        return iDurationField;
    }

    public abstract DurationField getRangeDurationField();

    public abstract long roundFloor(long instant);

    protected final long getDurationUnitMillis() {
        return iUnitMillis;
    }

    private final class LinkedDurationField extends AbstractDurationField {
        static final long serialVersionUID = -203813474600094134L;

        LinkedDurationField(String name) {
            super(name);
        }
    
        public boolean isPrecise() {
            return false;
        }
    
        public long getUnitMillis() {
            return iUnitMillis;
        }

        public int getValue(long duration, long instant) {
            return ImpreciseDateTimeField.this
                .getDifference(instant + duration, instant);
        }

        public long getValueAsLong(long duration, long instant) {
            return ImpreciseDateTimeField.this
                .getDifferenceAsLong(instant + duration, instant);
        }
        
        public long getMillis(int value, long instant) {
            return ImpreciseDateTimeField.this.add(instant, value) - instant;
        }

        public long getMillis(long value, long instant) {
            return ImpreciseDateTimeField.this.add(instant, value) - instant;
        }

        public long add(long instant, int value) {
            return ImpreciseDateTimeField.this.add(instant, value);
        }
        
        public long add(long instant, long value) {
            return ImpreciseDateTimeField.this.add(instant, value);
        }
        
        public int getDifference(long minuendInstant, long subtrahendInstant) {
            return ImpreciseDateTimeField.this
                .getDifference(minuendInstant, subtrahendInstant);
        }
        
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            return ImpreciseDateTimeField.this
                .getDifferenceAsLong(minuendInstant, subtrahendInstant);
        }
    }

}
