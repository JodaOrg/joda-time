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

/**
 * General utilities that don't fit elsewhere.
 * <p>
 * FieldUtils is thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public class FieldUtils {

    /**
     * Restricted constructor.
     */
    private FieldUtils() {
        super();
    }
    
    //------------------------------------------------------------------------
    /**
     * Add two values throwing an exception if overflow occurs.
     * 
     * @param val1  the first value
     * @param val2  the second value
     * @return the new total
     */
    public static long safeAdd(long val1, long val2) {
        long total = val1 + val2;
        if (val1 > 0 && val2 > 0 && total < 0) {
            throw new ArithmeticException("The calculation caused an overflow: " + val1 +" + " + val2);
        }
        if (val1 < 0 && val2 < 0 && total > 0) {
            throw new ArithmeticException("The calculation caused an overflow: " + val1 +" + " + val2);
        }
        return total;
    }
    
    /**
     * Multiply two values throwing an exception if overflow occurs.
     * 
     * @param val1  the first value
     * @param val2  the second value
     * @return the new total
     */
    public static long safeMultiply(long val1, long val2) {
        if (val1 == 0  || val2 == 0) {
            return 0L;
        }
        long total = val1 * val2;
        if (total / val2 != val1) {
            throw new ArithmeticException("The calculation caused an overflow: " + val1 +" * " + val2);
        }
        return total;
    }
    
    /**
     * Casts to an int throwing an exception if overflow occurs.
     * 
     * @param value  the value
     * @return the value as an int
     */
    public static int safeToInt(long value) {
        if (Integer.MIN_VALUE <= value && value <= Integer.MAX_VALUE) {
            return (int) value;
        }
        throw new ArithmeticException("Value cannot fit in an int: " + value);
    }

    /**
     * Verify that input values are within specified bounds.
     * 
     * @param value  the value to check
     * @param lowerBound  the lower bound allowed for value
     * @param upperBound  the upper bound allowed for value
     * @throws IllegalArgumentException if value is not in the specified bounds
     */
    public static void verifyValueBounds(DateTimeField field, 
                                         int value, int lowerBound, int upperBound) {
        if ((value < lowerBound) || (value > upperBound)) {
            throw new IllegalArgumentException(
                "Value "
                    + value
                    + " for "
                    + field.getName()
                    + " must be in the range ["
                    + lowerBound
                    + ','
                    + upperBound
                    + ']');
        }
    }

    /**
     * Verify that input values are within specified bounds.
     * 
     * @param value  the value to check
     * @param lowerBound  the lower bound allowed for value
     * @param upperBound  the upper bound allowed for value
     * @throws IllegalArgumentException if value is not in the specified bounds
     */
    public static void verifyValueBounds(String fieldName,
                                         int value, int lowerBound, int upperBound) {
        if ((value < lowerBound) || (value > upperBound)) {
            throw new IllegalArgumentException(
                "Value "
                    + value
                    + " for "
                    + fieldName
                    + " must be in the range ["
                    + lowerBound
                    + ','
                    + upperBound
                    + ']');
        }
    }

    /**
     * Utility method used by addWrapField implementations to ensure the new
     * value lies within the field's legal value range.
     *
     * @param currentValue the current value of the data, which may lie outside
     * the wrapped value range
     * @param wrapValue  the value to add to current value before
     *  wrapping.  This may be negative.
     * @param minValue the wrap range minimum value.
     * @param maxValue the wrap range maximum value.  This must be
     *  greater than minValue (checked by the method).
     * @return the wrapped value
     * @throws IllegalArgumentException if minValue is greater
     *  than or equal to maxValue
     */
    public static int getWrappedValue(int currentValue, int wrapValue,
                                      int minValue, int maxValue) {
        return getWrappedValue(currentValue + wrapValue, minValue, maxValue);
    }

    /**
     * Utility method that ensures the given value lies within the field's
     * legal value range.
     * 
     * @param value  the value to fit into the wrapped value range
     * @param minValue the wrap range minimum value.
     * @param maxValue the wrap range maximum value.  This must be
     *  greater than minValue (checked by the method).
     * @return the wrapped value
     * @throws IllegalArgumentException if minValue is greater
     *  than or equal to maxValue
     */
    public static int getWrappedValue(int value, int minValue, int maxValue) {
        if (minValue >= maxValue) {
            throw new IllegalArgumentException("MIN > MAX");
        }

        int wrapRange = maxValue - minValue + 1;
        value -= minValue;

        if (value >= 0) {
            return (value % wrapRange) + minValue;
        }

        int remByRange = (-value) % wrapRange;

        if (remByRange == 0) {
            return 0 + minValue;
        }
        return (wrapRange - remByRange) + minValue;
    }

}
