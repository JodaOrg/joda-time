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
 * Wraps another field such that zero values are replaced with one more than
 * it's maximum. This is particularly useful for implementing an clockhourOfDay
 * field, where the midnight value of 0 is replaced with 24.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class NonZeroDateTimeField extends DateTimeField {
    private final DateTimeField iField;

    /**
     * @param name  short, descriptive name, like "clockhourOfDay".
     * @throws IllegalArgumentException if wrapped field's minimum value is not zero
     */
    public NonZeroDateTimeField(String name, DateTimeField field) {
        super(name);
        if (field == null) {
            throw new IllegalArgumentException("The field must not be null");
        }
        if (field.getMinimumValue() != 0) {
            throw new IllegalArgumentException("Wrapped field's minumum value must be zero");
        }
        iField = field;
    }

    public int get(long millis) {
        int value = iField.get(millis);
        if (value == 0) {
            value = getMaximumValue();
        }
        return value;
    }

    public long add(long millis, int amount) {
        return iField.add(millis, amount);
    }

    public long add(long millis, long amount) {
        return iField.add(millis, amount);
    }

    public long addWrapped(long millis, int amount) {
        return iField.addWrapped(millis, amount);
    }
    
    public long getDifference(long minuendMillis, long subtrahendMillis) {
        return iField.getDifference(minuendMillis, subtrahendMillis);
    }

    public long set(long millis, int value) {
        int max = getMaximumValue();
        verifyValueBounds(value, 1, max);
        if (value == max) {
            value = 0;
        }
        return iField.set(millis, value);
    }

    public long getUnitMillis() {
        return iField.getUnitMillis();
    }

    public long getRangeMillis() {
        return iField.getRangeMillis();
    }

    /**
     * Always returns 1.
     * 
     * @return the minimum value of 1
     */
    public int getMinimumValue() {
        return 1;
    }

    /**
     * Always returns 1.
     * 
     * @return the minimum value of 1
     */
    public int getMinimumValue(long millis) {
        return 1;
    }

    /**
     * Get the maximum value for the field, which is one more than the wrapped
     * field's maximum value.
     * 
     * @return the maximum value
     */
    public int getMaximumValue() {
        return iField.getMaximumValue() + 1;
    }

    /**
     * Get the maximum value for the field, which is one more than the wrapped
     * field's maximum value.
     * 
     * @return the maximum value
     */
    public int getMaximumValue(long millis) {
        return iField.getMaximumValue(millis) + 1;
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
}
