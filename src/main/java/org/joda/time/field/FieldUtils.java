/*
 *  Copyright 2001-2005 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.field;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

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
     * Negates the input throwing an exception if it can't negate it.
     * 
     * @param value  the value to negate
     * @return the negated value
     * @throws ArithmeticException if the value is Integer.MIN_VALUE
     * @since 1.1
     */
    public static int safeNegate(int value) {
        if (value == Integer.MIN_VALUE) {
            throw new ArithmeticException("Integer.MIN_VALUE cannot be negated");
        }
        return -value;
    }
    
    /**
     * Add two values throwing an exception if overflow occurs.
     * 
     * @param val1  the first value
     * @param val2  the second value
     * @return the new total
     * @throws ArithmeticException if the value is too big or too small
     */
    public static int safeAdd(int val1, int val2) {
        int sum = val1 + val2;
        // If there is a sign change, but the two values have the same sign...
        if ((val1 ^ sum) < 0 && (val1 ^ val2) >= 0) {
            throw new ArithmeticException
                ("The calculation caused an overflow: " + val1 + " + " + val2);
        }
        return sum;
    }
    
    /**
     * Add two values throwing an exception if overflow occurs.
     * 
     * @param val1  the first value
     * @param val2  the second value
     * @return the new total
     * @throws ArithmeticException if the value is too big or too small
     */
    public static long safeAdd(long val1, long val2) {
        long sum = val1 + val2;
        // If there is a sign change, but the two values have the same sign...
        if ((val1 ^ sum) < 0 && (val1 ^ val2) >= 0) {
            throw new ArithmeticException
                ("The calculation caused an overflow: " + val1 + " + " + val2);
        }
        return sum;
    }
    
    /**
     * Subtracts two values throwing an exception if overflow occurs.
     * 
     * @param val1  the first value, to be taken away from
     * @param val2  the second value, the amount to take away
     * @return the new total
     * @throws ArithmeticException if the value is too big or too small
     */
    public static long safeSubtract(long val1, long val2) {
        long diff = val1 - val2;
        // If there is a sign change, but the two values have different signs...
        if ((val1 ^ diff) < 0 && (val1 ^ val2) < 0) {
            throw new ArithmeticException
                ("The calculation caused an overflow: " + val1 + " - " + val2);
        }
        return diff;
    }
    
    /**
     * Multiply two values throwing an exception if overflow occurs.
     * 
     * @param val1  the first value
     * @param val2  the second value
     * @return the new total
     * @throws ArithmeticException if the value is too big or too small
     * @since 1.2
     */
    public static int safeMultiply(int val1, int val2) {
        long total = (long) val1 * (long) val2;
        if (total < Integer.MIN_VALUE || total > Integer.MAX_VALUE) {
          throw new ArithmeticException("Multiplication overflows an int: " + val1 + " * " + val2);
        }
        return (int) total;
    }

    /**
     * Multiply two values throwing an exception if overflow occurs.
     * 
     * @param val1  the first value
     * @param val2  the second value
     * @return the new total
     * @throws ArithmeticException if the value is too big or too small
     * @since 1.2
     */
    public static long safeMultiply(long val1, int val2) {
        switch (val2) {
            case -1:
                if (val1 == Long.MIN_VALUE) {
                    throw new ArithmeticException("Multiplication overflows a long: " + val1 + " * " + val2);
                }
                return -val1;
            case 0:
                return 0L;
            case 1:
                return val1;
        }
        long total = val1 * val2;
        if (total / val2 != val1) {
          throw new ArithmeticException("Multiplication overflows a long: " + val1 + " * " + val2);
        }
        return total;
    }

    /**
     * Multiply two values throwing an exception if overflow occurs.
     * 
     * @param val1  the first value
     * @param val2  the second value
     * @return the new total
     * @throws ArithmeticException if the value is too big or too small
     */
    public static long safeMultiply(long val1, long val2) {
        if (val2 == 1) {
            return val1;
        }
        if (val1 == 1) {
            return val2;
        }
        if (val1 == 0 || val2 == 0) {
            return 0;
        }
        long total = val1 * val2;
        if (total / val2 != val1 || val1 == Long.MIN_VALUE && val2 == -1 || val2 == Long.MIN_VALUE && val1 == -1) {
            throw new ArithmeticException("Multiplication overflows a long: " + val1 + " * " + val2);
        }
        return total;
    }
    
    /**
     * Casts to an int throwing an exception if overflow occurs.
     * 
     * @param value  the value
     * @return the value as an int
     * @throws ArithmeticException if the value is too big or too small
     */
    public static int safeToInt(long value) {
        if (Integer.MIN_VALUE <= value && value <= Integer.MAX_VALUE) {
            return (int) value;
        }
        throw new ArithmeticException("Value cannot fit in an int: " + value);
    }
    
    /**
     * Multiply two values to return an int throwing an exception if overflow occurs.
     * 
     * @param val1  the first value
     * @param val2  the second value
     * @return the new total
     * @throws ArithmeticException if the value is too big or too small
     */
    public static int safeMultiplyToInt(long val1, long val2) {
        long val = FieldUtils.safeMultiply(val1, val2);
        return FieldUtils.safeToInt(val);
    }

    //-----------------------------------------------------------------------
    /**
     * Verify that input values are within specified bounds.
     * 
     * @param value  the value to check
     * @param lowerBound  the lower bound allowed for value
     * @param upperBound  the upper bound allowed for value
     * @throws IllegalFieldValueException if value is not in the specified bounds
     */
    public static void verifyValueBounds(DateTimeField field, 
                                         int value, int lowerBound, int upperBound) {
        if ((value < lowerBound) || (value > upperBound)) {
            throw new IllegalFieldValueException
                (field.getType(), Integer.valueOf(value),
                 Integer.valueOf(lowerBound), Integer.valueOf(upperBound));
        }
    }

    /**
     * Verify that input values are within specified bounds.
     * 
     * @param value  the value to check
     * @param lowerBound  the lower bound allowed for value
     * @param upperBound  the upper bound allowed for value
     * @throws IllegalFieldValueException if value is not in the specified bounds
     * @since 1.1
     */
    public static void verifyValueBounds(DateTimeFieldType fieldType, 
                                         int value, int lowerBound, int upperBound) {
        if ((value < lowerBound) || (value > upperBound)) {
            throw new IllegalFieldValueException
                (fieldType, Integer.valueOf(value),
                 Integer.valueOf(lowerBound), Integer.valueOf(upperBound));
        }
    }

    /**
     * Verify that input values are within specified bounds.
     * 
     * @param value  the value to check
     * @param lowerBound  the lower bound allowed for value
     * @param upperBound  the upper bound allowed for value
     * @throws IllegalFieldValueException if value is not in the specified bounds
     */
    public static void verifyValueBounds(String fieldName,
                                         int value, int lowerBound, int upperBound) {
        if ((value < lowerBound) || (value > upperBound)) {
            throw new IllegalFieldValueException
                (fieldName, Integer.valueOf(value),
                 Integer.valueOf(lowerBound), Integer.valueOf(upperBound));
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

    //-----------------------------------------------------------------------
    /**
     * Compares two objects as equals handling null.
     * 
     * @param object1  the first object
     * @param object2  the second object
     * @return true if equal
     * @since 1.4
     */
    public static boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if (object1 == null || object2 == null) {
            return false;
        }
        return object1.equals(object2);
    }

}
