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
 * Generic remainder datetime field.
 * 
 * @see DividedDateTimeField
 * 
 * @author Brian S O'Neill
 * @since 1.0
 */
public class RemainderDateTimeField extends DateTimeField {
    /** The field to get the remainder */
    private final DateTimeField iField;
    /** The amount to divide by in the correct units */
    private final int iUnitDivisor;

    /**
     * Constructor
     * 
     * @param name  short, descriptive name, like "yearOfCentury".
     * @param field  the field to wrap, like "year()".
     * @param unitDivisor  divisor in units, such as 100 years in a century
     * @throws IllegalArgumentException if unit is less than two
     */
    public RemainderDateTimeField(String name, DateTimeField field, int unitDivisor) {
        super(name);
                
        if (field == null) {
            throw new IllegalArgumentException("The field must not be null");
        }
        if (unitDivisor < 2) {
            throw new IllegalArgumentException("The unit divisor must be at least 2");
        }
        iField = field;
        iUnitDivisor = unitDivisor;
    }

    /**
     * Get the remainder from the specified time instant.
     * 
     * @param millis  the time instant in millis to query.
     * @return the remainder extracted from the input.
     */
    public int get(long millis) {
        int value = iField.get(millis);
        if (value >= 0) {
            return value % iUnitDivisor;
        } else {
            return (iUnitDivisor - 1) + ((value + 1) % iUnitDivisor);
        }
    }
    
    /**
     * Add the specified amount to the specified time instant. The amount added
     * may be negative.
     * 
     * @param millis  the time instant in millis to update.
     * @param amount  the amount to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long millis, int amount) {
        return iField.add(millis, amount);
    }
    
    /**
     * Add the specified amount to the specified time instant. The amount added
     * may be negative.
     * 
     * @param millis  the time instant in millis to update.
     * @param amount  the amount to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long millis, long amount) {
        return iField.add(millis, amount);
    }

    /**
     * Add the specified amount to the specified time instant, wrapping around
     * within the remainder range if necessary. The amount added may be
     * negative.
     * 
     * @param millis  the time instant in millis to update.
     * @param amount  the amount to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapped(long millis, int amount) {
        return set(millis, getWrappedValue(get(millis), amount, 0, iUnitDivisor - 1));
    }
    
    public long getDifference(long minuendMillis, long subtrahendMillis) {
        return iField.getDifference(minuendMillis, subtrahendMillis);
    }

    /**
     * Set the specified amount of remainder units to the specified time instant.
     * 
     * @param millis  the time instant in millis to update.
     * @param value  value of remainder units to set.
     * @return the updated time instant.
     * @throws IllegalArgumentException if value is too large or too small.
     */
    public long set(long millis, int value) {
        verifyValueBounds(value, 0, iUnitDivisor - 1);
        int divided = getDivided(iField.get(millis));
        return iField.set(millis, divided * iUnitDivisor + value);
    }
    
    public long getUnitMillis() {
        return iField.getUnitMillis();
    }

    /**
     * Returns the wrapped field's unit size multiplied by the unit divisor.
     */
    public long getRangeMillis() {
        return iField.getUnitMillis() * iUnitDivisor;
    }

    /**
     * Get the minimum value for the field, which is always zero.
     * 
     * @return the minimum value of zero.
     */
    public int getMinimumValue() {
        return 0;
    }
    
    /**
     * Get the maximum value for the field, which is always one less than the
     * unit divisor.
     * 
     * @return the maximum value
     */
    public int getMaximumValue() {
        return iUnitDivisor - 1;
    }
    
    public long roundFloor(long millis) {
        return iField.roundFloor(millis);
    }

    public long roundCeiling(long millis) {
        return iField.roundCeiling(millis);
    }

    public long remainder(long millis) {
        return iField.remainder(millis);
    }

    /**
     * Returns the DateTimeField being wrapped.
     * 
     * @return field
     */
    public DateTimeField getField() {
        return iField;
    }

    /**
     * Returns the divisor to apply to the field in the field's units.
     * 
     * @return the divisor
     */
    public int getUnitDivisor() {
        return iUnitDivisor;
    }

    private int getDivided(int value) {
        if (value >= 0) {
            return value / iUnitDivisor;
        } else {
            return ((value + 1) / iUnitDivisor) - 1;
        }
    }

}
