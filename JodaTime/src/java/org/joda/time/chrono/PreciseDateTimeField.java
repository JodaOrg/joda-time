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
 * Precise datetime field, composed of two precise duration fields.
 * <p>
 * This DateTimeField is useful for defining DateTimeFields that are composed
 * of precise durations, like time of day fields. If either duration field is
 * imprecise, then an {@link ImpreciseDateTimeField} may be used instead.
 * <p>
 * PreciseDateTimeField is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 * @see ImpreciseDateTimeField
 */
public class PreciseDateTimeField extends PreciseDurationDateTimeField {

    static final long serialVersionUID = -5586801265774496376L;

    /** The maximum range in the correct units */
    private final int iRange;

    private final DurationField iRangeField;

    /**
     * Constructor.
     * 
     * @param name  short, descriptive name, like "secondOfMinute".
     * @param unit  precise unit duration, like "seconds()".
     * @param range precise range duration, preferably a multiple of the unit,
     * like "minutes()".
     * @throws IllegalArgumentException if either duration field is imprecise
     * @throws IllegalArgumentException if unit milliseconds is less than one
     * or effective value range is less than two.
     */
    public PreciseDateTimeField(String name,
                                DurationField unit, DurationField range) {
        super(name, unit);

        if (!range.isPrecise()) {
            throw new IllegalArgumentException("Range duration field must be precise");
        }

        long rangeMillis = range.getUnitMillis();
        iRange = (int)(rangeMillis / getUnitMillis());
        if (iRange < 2) {
            throw new IllegalArgumentException("The effective range must be at least 2");
        }

        iRangeField = range;
    }

    /**
     * Get the amount of fractional units from the specified time instant.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to query
     * @return the amount of fractional units extracted from the input.
     */
    public int get(long instant) {
        if (instant >= 0) {
            return (int) ((instant / getUnitMillis()) % iRange);
        } else {
            return iRange - 1 + (int) (((instant + 1) / getUnitMillis()) % iRange);
        }
    }

    /**
     * Add to the component of the specified time instant, wrapping around
     * within that component if necessary.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to add to
     * @param amount  the amount of units to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapped(long instant, int amount) {
        int thisValue = get(instant);
        int wrappedValue = Utils.getWrappedValue
            (thisValue, amount, getMinimumValue(), getMaximumValue());
        // copy code from set() to avoid repeat call to get()
        return instant + (wrappedValue - thisValue) * getUnitMillis();
    }

    /**
     * Returns the range duration of this field. For example, if this field
     * represents "minute of hour", then the range duration field is an hours.
     *
     * @return the range duration of this field, or null if field has no range
     */
    public DurationField getRangeDurationField() {
        return iRangeField;
    }

    /**
     * Get the maximum value for the field.
     * 
     * @return the maximum value
     */
    public int getMaximumValue() {
        return iRange - 1;
    }
    
    /**
     * Returns the range of the field in the field's units.
     * <p>
     * For example, 60 for seconds per minute. The field is allowed values
     * from 0 to range - 1.
     * 
     * @return unit range
     */
    public int getRange() {
        return iRange;
    }

}
