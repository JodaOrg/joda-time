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
package org.joda.time.field;

import org.joda.time.DateTimeField;
import org.joda.time.DurationField;

/**
 * Generic offset adjusting datetime field.
 * <p>
 * OffsetDateTimeField is thread-safe and immutable.
 * 
 * @author Brian S O'Neill
 * @since 1.0
 */
public class OffsetDateTimeField extends DecoratedDateTimeField {
    static final long serialVersionUID = 3145790132623583142L;

    private final int iOffset;

    private final int iMin;
    private final int iMax;

    /**
     * Constructor
     * 
     * @param field  the field to wrap, like "year()".
     * @param name  short, descriptive name, like "offsetYear".
     * @param offset  offset to add to field values
     * @throws IllegalArgumentException if offset is zero
     */
    public OffsetDateTimeField(DateTimeField field, String name, int offset) {
        this(field, name, offset, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Constructor
     * 
     * @param field  the field to wrap, like "year()".
     * @param name  short, descriptive name, like "offsetYear".
     * @param offset  offset to add to field values
     * @param minValue  minimum allowed value
     * @param maxValue  maximum allowed value
     * @throws IllegalArgumentException if offset is zero
     */
    public OffsetDateTimeField(DateTimeField field, String name, int offset,
                               int minValue, int maxValue) {
        super(field, name);
                
        if (offset == 0) {
            throw new IllegalArgumentException("The offset cannot be zero");
        }

        iOffset = offset;

        if (minValue < (field.getMinimumValue() + offset)) {
            iMin = field.getMinimumValue() + offset;
        } else {
            iMin = minValue;
        }
        if (maxValue > (field.getMaximumValue() + offset)) {
            iMax = field.getMaximumValue() + offset;
        } else {
            iMax = maxValue;
        }
    }

    /**
     * Get the amount of offset units from the specified time instant.
     * 
     * @param instant  the time instant in millis to query.
     * @return the amount of units extracted from the input.
     */
    public int get(long instant) {
        return super.get(instant) + iOffset;
    }

    /**
     * Add the specified amount of offset units to the specified time
     * instant. The amount added may be negative.
     * 
     * @param instant  the time instant in millis to update.
     * @param amount  the amount of units to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long instant, int amount) {
        instant = super.add(instant, amount);
        FieldUtils.verifyValueBounds(this, get(instant), iMin, iMax);
        return instant;
    }

    /**
     * Add the specified amount of offset units to the specified time
     * instant. The amount added may be negative.
     * 
     * @param instant  the time instant in millis to update.
     * @param amount  the amount of units to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long instant, long amount) {
        instant = super.add(instant, amount);
        FieldUtils.verifyValueBounds(this, get(instant), iMin, iMax);
        return instant;
    }

    /**
     * Add to the offset component of the specified time instant,
     * wrapping around within that component if necessary.
     * 
     * @param instant  the time instant in millis to update.
     * @param amount  the amount of units to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapped(long instant, int amount) {
        return set(instant, FieldUtils.getWrappedValue(get(instant), amount, iMin, iMax));
    }

    /**
     * Set the specified amount of offset units to the specified time instant.
     * 
     * @param instant  the time instant in millis to update.
     * @param value  value of units to set.
     * @return the updated time instant.
     * @throws IllegalArgumentException if value is too large or too small.
     */
    public long set(long instant, int value) {
        FieldUtils.verifyValueBounds(this, value, iMin, iMax);
        return super.set(instant, value - iOffset);
    }

    public boolean isLeap(long instant) {
        return getWrappedField().isLeap(instant);
    }

    public int getLeapAmount(long instant) {
        return getWrappedField().getLeapAmount(instant);
    }

    public DurationField getLeapDurationField() {
        return getWrappedField().getLeapDurationField();
    }

    /**
     * Get the minimum value for the field.
     * 
     * @return the minimum value
     */
    public int getMinimumValue() {
        return iMin;
    }

    /**
     * Get the maximum value for the field.
     * 
     * @return the maximum value
     */
    public int getMaximumValue() {
        return iMax;
    }
    
    public long roundFloor(long instant) {
        return getWrappedField().roundFloor(instant);
    }

    public long roundCeiling(long instant) {
        return getWrappedField().roundCeiling(instant);
    }

    public long roundHalfFloor(long instant) {
        return getWrappedField().roundHalfFloor(instant);
    }

    public long roundHalfCeiling(long instant) {
        return getWrappedField().roundHalfCeiling(instant);
    }

    public long roundHalfEven(long instant) {
        return getWrappedField().roundHalfEven(instant);
    }

    public long remainder(long instant) {
        return getWrappedField().remainder(instant);
    }

    /**
     * Returns the offset added to the field values.
     * 
     * @return the offset
     */
    public int getOffset() {
        return iOffset;
    }
}
