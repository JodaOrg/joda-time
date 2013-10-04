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

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 *
 * @author Brian S O'Neill
 */
public class TestFieldUtils extends TestCase {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestFieldUtils.class);
    }

    public TestFieldUtils(String name) {
        super(name);
    }

    public void testSafeAddInt() {
        assertEquals(0, FieldUtils.safeAdd(0, 0));

        assertEquals(5, FieldUtils.safeAdd(2, 3));
        assertEquals(-1, FieldUtils.safeAdd(2, -3));
        assertEquals(1, FieldUtils.safeAdd(-2, 3));
        assertEquals(-5, FieldUtils.safeAdd(-2, -3));

        assertEquals(Integer.MAX_VALUE - 1, FieldUtils.safeAdd(Integer.MAX_VALUE, -1));
        assertEquals(Integer.MIN_VALUE + 1, FieldUtils.safeAdd(Integer.MIN_VALUE, 1));

        assertEquals(-1, FieldUtils.safeAdd(Integer.MIN_VALUE, Integer.MAX_VALUE));
        assertEquals(-1, FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MIN_VALUE));

        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail();
        } catch (ArithmeticException e) {
        }

        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 100);
            fail();
        } catch (ArithmeticException e) {
        }

        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MAX_VALUE);
            fail();
        } catch (ArithmeticException e) {
        }

        try {
            FieldUtils.safeAdd(Integer.MIN_VALUE, -1);
            fail();
        } catch (ArithmeticException e) {
        }

        try {
            FieldUtils.safeAdd(Integer.MIN_VALUE, -100);
            fail();
        } catch (ArithmeticException e) {
        }

        try {
            FieldUtils.safeAdd(Integer.MIN_VALUE, Integer.MIN_VALUE);
            fail();
        } catch (ArithmeticException e) {
        }
    }

    public void testSafeAddLong() {
        assertEquals(0L, FieldUtils.safeAdd(0L, 0L));

        assertEquals(5L, FieldUtils.safeAdd(2L, 3L));
        assertEquals(-1L, FieldUtils.safeAdd(2L, -3L));
        assertEquals(1L, FieldUtils.safeAdd(-2L, 3L));
        assertEquals(-5L, FieldUtils.safeAdd(-2L, -3L));

        assertEquals(Long.MAX_VALUE - 1, FieldUtils.safeAdd(Long.MAX_VALUE, -1L));
        assertEquals(Long.MIN_VALUE + 1, FieldUtils.safeAdd(Long.MIN_VALUE, 1L));

        assertEquals(-1, FieldUtils.safeAdd(Long.MIN_VALUE, Long.MAX_VALUE));
        assertEquals(-1, FieldUtils.safeAdd(Long.MAX_VALUE, Long.MIN_VALUE));

        try {
            FieldUtils.safeAdd(Long.MAX_VALUE, 1L);
            fail();
        } catch (ArithmeticException e) {
        }

        try {
            FieldUtils.safeAdd(Long.MAX_VALUE, 100L);
            fail();
        } catch (ArithmeticException e) {
        }

        try {
            FieldUtils.safeAdd(Long.MAX_VALUE, Long.MAX_VALUE);
            fail();
        } catch (ArithmeticException e) {
        }

        try {
            FieldUtils.safeAdd(Long.MIN_VALUE, -1L);
            fail();
        } catch (ArithmeticException e) {
        }

        try {
            FieldUtils.safeAdd(Long.MIN_VALUE, -100L);
            fail();
        } catch (ArithmeticException e) {
        }

        try {
            FieldUtils.safeAdd(Long.MIN_VALUE, Long.MIN_VALUE);
            fail();
        } catch (ArithmeticException e) {
        }
    }

    public void testSafeSubtractLong() {
        assertEquals(0L, FieldUtils.safeSubtract(0L, 0L));

        assertEquals(-1L, FieldUtils.safeSubtract(2L, 3L));
        assertEquals(5L, FieldUtils.safeSubtract(2L, -3L));
        assertEquals(-5L, FieldUtils.safeSubtract(-2L, 3L));
        assertEquals(1L, FieldUtils.safeSubtract(-2L, -3L));

        assertEquals(Long.MAX_VALUE - 1, FieldUtils.safeSubtract(Long.MAX_VALUE, 1L));
        assertEquals(Long.MIN_VALUE + 1, FieldUtils.safeSubtract(Long.MIN_VALUE, -1L));

        assertEquals(0, FieldUtils.safeSubtract(Long.MIN_VALUE, Long.MIN_VALUE));
        assertEquals(0, FieldUtils.safeSubtract(Long.MAX_VALUE, Long.MAX_VALUE));

        try {
            FieldUtils.safeSubtract(Long.MIN_VALUE, 1L);
            fail();
        } catch (ArithmeticException e) {
        }

        try {
            FieldUtils.safeSubtract(Long.MIN_VALUE, 100L);
            fail();
        } catch (ArithmeticException e) {
        }

        try {
            FieldUtils.safeSubtract(Long.MIN_VALUE, Long.MAX_VALUE);
            fail();
        } catch (ArithmeticException e) {
        }

        try {
            FieldUtils.safeSubtract(Long.MAX_VALUE, -1L);
            fail();
        } catch (ArithmeticException e) {
        }

        try {
            FieldUtils.safeSubtract(Long.MAX_VALUE, -100L);
            fail();
        } catch (ArithmeticException e) {
        }

        try {
            FieldUtils.safeSubtract(Long.MAX_VALUE, Long.MIN_VALUE);
            fail();
        } catch (ArithmeticException e) {
        }
    }

    //-----------------------------------------------------------------------
    public void testSafeMultiplyLongLong() {
        assertEquals(0L, FieldUtils.safeMultiply(0L, 0L));
        
        assertEquals(1L, FieldUtils.safeMultiply(1L, 1L));
        assertEquals(3L, FieldUtils.safeMultiply(1L, 3L));
        assertEquals(3L, FieldUtils.safeMultiply(3L, 1L));
        
        assertEquals(6L, FieldUtils.safeMultiply(2L, 3L));
        assertEquals(-6L, FieldUtils.safeMultiply(2L, -3L));
        assertEquals(-6L, FieldUtils.safeMultiply(-2L, 3L));
        assertEquals(6L, FieldUtils.safeMultiply(-2L, -3L));
        
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1L));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, 1L));
        assertEquals(-Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, -1L));
        
        try {
            FieldUtils.safeMultiply(Long.MIN_VALUE, -1L);
            fail();
        } catch (ArithmeticException e) {
        }
        
        try {
            FieldUtils.safeMultiply(-1L, Long.MIN_VALUE);
            fail();
        } catch (ArithmeticException e) {
        }
      
        try {
            FieldUtils.safeMultiply(Long.MIN_VALUE, 100L);
            fail();
        } catch (ArithmeticException e) {
        }
        
        try {
            FieldUtils.safeMultiply(Long.MIN_VALUE, Long.MAX_VALUE);
            fail();
        } catch (ArithmeticException e) {
        }
        
        try {
            FieldUtils.safeMultiply(Long.MAX_VALUE, Long.MIN_VALUE);
            fail();
        } catch (ArithmeticException e) {
        }
    }

    //-----------------------------------------------------------------------
    public void testSafeMultiplyLongInt() {
        assertEquals(0L, FieldUtils.safeMultiply(0L, 0));
        
        assertEquals(1L, FieldUtils.safeMultiply(1L, 1));
        assertEquals(3L, FieldUtils.safeMultiply(1L, 3));
        assertEquals(3L, FieldUtils.safeMultiply(3L, 1));
        
        assertEquals(6L, FieldUtils.safeMultiply(2L, 3));
        assertEquals(-6L, FieldUtils.safeMultiply(2L, -3));
        assertEquals(-6L, FieldUtils.safeMultiply(-2L, 3));
        assertEquals(6L, FieldUtils.safeMultiply(-2L, -3));
        
        assertEquals(-1L * Integer.MIN_VALUE, FieldUtils.safeMultiply(-1L, Integer.MIN_VALUE));
        
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, 1));
        assertEquals(-Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, -1));
        
        try {
            FieldUtils.safeMultiply(Long.MIN_VALUE, -1);
            fail();
        } catch (ArithmeticException e) {
        }
        
        try {
            FieldUtils.safeMultiply(Long.MIN_VALUE, 100);
            fail();
        } catch (ArithmeticException e) {
        }
        
        try {
            FieldUtils.safeMultiply(Long.MIN_VALUE, Integer.MAX_VALUE);
            fail();
        } catch (ArithmeticException e) {
        }
        
        try {
            FieldUtils.safeMultiply(Long.MAX_VALUE, Integer.MIN_VALUE);
            fail();
        } catch (ArithmeticException e) {
        }
    }

    //-----------------------------------------------------------------------
    public void testSafeDivideLongLong() {
        assertEquals(1L, FieldUtils.safeDivide(1L, 1L));
        
        assertEquals(1L, FieldUtils.safeDivide(3L, 3L));
        assertEquals(0L, FieldUtils.safeDivide(1L, 3L));
        assertEquals(3L, FieldUtils.safeDivide(3L, 1L));
        
        assertEquals(1L, FieldUtils.safeDivide(5L, 3L));
        assertEquals(-1L, FieldUtils.safeDivide(5L, -3L));
        assertEquals(-1L, FieldUtils.safeDivide(-5L, 3L));
        assertEquals(1L, FieldUtils.safeDivide(-5L, -3L));
        
        assertEquals(2L, FieldUtils.safeDivide(6L, 3L));
        assertEquals(-2L, FieldUtils.safeDivide(6L, -3L));
        assertEquals(-2L, FieldUtils.safeDivide(-6L, 3L));
        assertEquals(2L, FieldUtils.safeDivide(-6L, -3L));
        
        assertEquals(2L, FieldUtils.safeDivide(7L, 3L));
        assertEquals(-2L, FieldUtils.safeDivide(7L, -3L));
        assertEquals(-2L, FieldUtils.safeDivide(-7L, 3L));
        assertEquals(2L, FieldUtils.safeDivide(-7L, -3L));
        
        assertEquals(Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, 1L));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeDivide(Long.MIN_VALUE, 1L));
        assertEquals(-Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, -1L));
        
        try {
            FieldUtils.safeDivide(Long.MIN_VALUE, -1L);
            fail();
        } catch (ArithmeticException e) {
        }
        
        try {
            FieldUtils.safeDivide(1L, 0L);
            fail();
        } catch (ArithmeticException e) {
        }
    }

}
