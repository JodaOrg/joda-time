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

import org.joda.time.DateTimeField;

/**
 * Generic fractional datetime field.
 * <p>
 * This DateTimeField is useful for extracting a fractional part from the
 * milliseconds. This is very useful for time values.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public class FractionalDateTimeField extends DateTimeField {

    /** The fractional unit in millis */
    private final long iUnitMillis;
    /** The maximum range in the correct units */
    private final int iRange;

    /**
     * Constructor.
     * 
     * @param name  short, descriptive name, like "secondOfMinute".
     * @param unitMillis  milliseconds per unit, such as 1000 for one second
     * @param range  range in units, such as 60 seconds per minute
     * @throws IllegalArgumentException if fractional unit is less than one, or if range is
     *  less than two.
     */
    public FractionalDateTimeField(String name, long unitMillis, int range) {
        super(name);
                
        if (unitMillis < 1) {
            throw new IllegalArgumentException("The unit milliseconds must be at least 1");
        }
        if (range < 2) {
            throw new IllegalArgumentException("The range must be at least 2");
        }
        iUnitMillis = unitMillis;
        iRange = range;
    }

    /**
     * Get the amount of fractional units from the specified time instant.
     * 
     * @param millis  the time instant in millis to query.
     * @return the amount of fractional units extracted from the input.
     */
    public int get(long millis) {
        if (millis >= 0) {
            return (int) ((millis / iUnitMillis) % iRange);
        } else {
            return iRange - 1 + (int) (((millis + 1) / iUnitMillis) % iRange);
        }
    }

    /**
     * Add the specified amount of fractional units to the specified time
     * instant. The amount added may be negative.
     * 
     * @param millis  the time instant in millis to update.
     * @param amount  the amount of fractional units to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long millis, int amount) {
        return millis + amount * iUnitMillis;
    }

    /**
     * Add the specified amount of fractional units to the specified time
     * instant. The amount added may be negative.
     * 
     * @param millis  the time instant in millis to update.
     * @param amount  the amount of fractional units to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long millis, long amount) {
        return millis + amount * iUnitMillis;
    }

    /**
     * Add to the fractional component of the specified time instant,
     * wrapping around within that component if necessary.
     * 
     * @param millis  the time instant in millis to update.
     * @param amount  the amount of fractional units to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapped(long millis, int amount) {
        int thisValue = get(millis);
        int wrappedValue = getWrappedValue(thisValue, amount, getMinimumValue(), getMaximumValue());
        // copy code from set() to avoid repeat call to get()
        return millis + (wrappedValue - thisValue) * iUnitMillis;
    }

    public long getDifference(long minuendMillis, long subtrahendMillis) {
        return (minuendMillis - subtrahendMillis) / iUnitMillis;
    }

    /**
     * Set the specified amount of fractional units to the specified time instant.
     * 
     * @param millis  the time instant in millis to update.
     * @param value  value of fractional units to set.
     * @return the updated time instant.
     * @throws IllegalArgumentException if value is too large or too small.
     */
    public long set(long millis, int value) {
        verifyValueBounds(value, getMinimumValue(), getMaximumValue());
        return millis + (value - get(millis)) * iUnitMillis;
    }

    /**
     * Returns the milliseconds representing one unit of the field.
     * <p>
     * For example, 1000 for one second.
     * 
     * @return unit millis
     */
    public long getUnitMillis() {
        return iUnitMillis;
    }

    public long getRangeMillis() {
        return iRange * iUnitMillis;
    }

    /**
     * Get the minimum value for the field.
     * 
     * @return the minimum value
     */
    public int getMinimumValue() {
        return 0;
    }

    /**
     * Get the maximum value for the field.
     * 
     * @return the maximum value
     */
    public int getMaximumValue() {
        return iRange - 1;
    }
    
    public long roundFloor(long millis) {
        if (millis >= 0) {
            return millis - millis % iUnitMillis;
        } else {
            millis += 1;
            return millis - millis % iUnitMillis - iUnitMillis;
        }
    }

    public long roundCeiling(long millis) {
        if (millis >= 0) {
            millis -= 1;
            return millis - millis % iUnitMillis + iUnitMillis;
        } else {
            return millis - millis % iUnitMillis;
        }
    }

    public long remainder(long millis) {
        if (millis >= 0) {
            return millis % iUnitMillis;
        } else {
            return (millis + 1) % iUnitMillis + iUnitMillis - 1;
        }
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
