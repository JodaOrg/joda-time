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

package org.joda.time.chrono;

import org.joda.time.DurationField;

/**
 * Precise datetime field, which has a precise unit duration field.
 * <p>
 * PreciseDurationDateTimeField is thread-safe and immutable, and its
 * subclasses must be as well.
 *
 * @author Brian S O'Neill
 */
public abstract class PreciseDurationDateTimeField extends AbstractDateTimeField {

    static final long serialVersionUID = 5004523158306266035L;

    /** The fractional unit in millis */
    private final long iUnitMillis;

    private final DurationField iUnitField;

    /**
     * Constructor.
     * 
     * @param name  short, descriptive name, like "dayOfMonth".
     * @param unit  precise unit duration, like "days()".
     * @throws IllegalArgumentException if duration field is imprecise
     * @throws IllegalArgumentException if unit milliseconds is less than one
     */
    public PreciseDurationDateTimeField(String name, DurationField unit) {
        super(name);

        if (!unit.isPrecise()) {
            throw new IllegalArgumentException("Unit duration field must be precise");
        }

        iUnitMillis = unit.getUnitMillis();
        if (iUnitMillis < 1) {
            throw new IllegalArgumentException("The unit milliseconds must be at least 1");
        }

        iUnitField = unit;
    }

    /**
     * Returns false by default.
     */
    public boolean isLenient() {
        return false;
    }

    /**
     * Add the specified amount of units to the specified time instant. The
     * amount added may be negative.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to add to
     * @param amount  the amount of units to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long instant, int amount) {
        return instant + amount * iUnitMillis;
    }

    /**
     * Add the specified amount of units to the specified time instant. The
     * amount added may be negative.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to add to
     * @param amount  the amount of units to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long instant, long amount) {
        return instant + amount * iUnitMillis;
    }

    public int getDifference(long minuendInstant, long subtrahendInstant) {
        return Utils.safeToInt(getDifferenceAsLong(minuendInstant, subtrahendInstant));
    }

    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        return (minuendInstant - subtrahendInstant) / iUnitMillis;
    }

    /**
     * Set the specified amount of units to the specified time instant.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to set in
     * @param value  value of units to set.
     * @return the updated time instant.
     * @throws IllegalArgumentException if value is too large or too small.
     */
    public long set(long instant, int value) {
        int max;
        if (getRangeDurationField().isPrecise()) {
            max = getMaximumValue();
        } else {
            max = getMaximumValueForSet(instant, value);
        }
        Utils.verifyValueBounds(this, value, getMinimumValue(), max);
        return instant + (value - get(instant)) * iUnitMillis;
    }

    /**
     * This method assumes that this field is properly rounded on
     * 1970-01-01T00:00:00. If the rounding alignment differs, override this
     * method as follows:
     * <pre>
     * return super.roundFloor(instant + ALIGNMENT_MILLIS) - ALIGNMENT_MILLIS;
     * </pre>
     */
    public long roundFloor(long instant) {
        if (instant >= 0) {
            return instant - instant % iUnitMillis;
        } else {
            instant += 1;
            return instant - instant % iUnitMillis - iUnitMillis;
        }
    }

    /**
     * This method assumes that this field is properly rounded on
     * 1970-01-01T00:00:00. If the rounding alignment differs, override this
     * method as follows:
     * <pre>
     * return super.roundCeiling(instant + ALIGNMENT_MILLIS) - ALIGNMENT_MILLIS;
     * </pre>
     */
    public long roundCeiling(long instant) {
        if (instant >= 0) {
            instant -= 1;
            return instant - instant % iUnitMillis + iUnitMillis;
        } else {
            return instant - instant % iUnitMillis;
        }
    }

    /**
     * This method assumes that this field is properly rounded on
     * 1970-01-01T00:00:00. If the rounding alignment differs, override this
     * method as follows:
     * <pre>
     * return super.remainder(instant + ALIGNMENT_MILLIS);
     * </pre>
     */
    public long remainder(long instant) {
        if (instant >= 0) {
            return instant % iUnitMillis;
        } else {
            return (instant + 1) % iUnitMillis + iUnitMillis - 1;
        }
    }

    /**
     * Returns the duration per unit value of this field. For example, if this
     * field represents "minute of hour", then the duration field is minutes.
     *
     * @return the duration of this field, or UnsupportedDurationField if field
     * has no duration
     */
    public DurationField getDurationField() {
        return iUnitField;
    }

    /**
     * Get the minimum value for the field.
     * 
     * @return the minimum value
     */
    public int getMinimumValue() {
        return 0;
    }

    public final long getUnitMillis() {
        return iUnitMillis;
    }

    /**
     * Called by the set method if the range duration field is imprecise. By
     * default, returns getMaximumValue(instant). Override to provide a faster
     * implementation.
     */
    protected int getMaximumValueForSet(long instant, int value) {
        return getMaximumValue(instant);
    }

}
