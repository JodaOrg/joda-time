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
package org.joda.test.time;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
/**
 * This class is the abstract Junit unit test for the
 * DateTimeField date time field subclasses.
 *
 * @author Stephen Colebourne
 */
public abstract class AbstractTestDateTimeField extends TestCase {
    
    protected static final TimeZone UTC = TimeZone.getTimeZone("GMT+00:00");
//    protected static final TimeZone OLD_PARIS = UTC;
//    protected static final DateTimeZone PARIS = DateTimeZone.UTC;
    protected static final TimeZone OLD_PARIS = TimeZone.getTimeZone("Europe/Paris");
    private static final DateTimeZone PARIS = DateTimeZone.getInstance("Europe/Paris");
    protected static final long MILLIS_1970;
    protected static final long MILLIS_1972_MARCH;
    protected static final long MILLIS_2000;
    protected static final long MILLIS_2000_MARCH;
    protected static final long MILLIS_1600;
    protected static final long MILLIS_1600_MARCH;
    protected static final long MILLIS_1583;
    protected static final long MILLIS_1582_OCTOBER;// = -12219292800000L;
    protected static final long MILLIS_1582;
    protected static final long MILLIS_1;
    protected static final long MILLIS_100;
    protected static final long MILLIS_100_MARCH;
    protected static final long MILLIS_400;
    protected static final long MILLIS_400_MARCH;
    protected static long[] RESULT;
    
    protected GregorianCalendar iCalendar = new GregorianCalendar(OLD_PARIS);
    protected Date iDate = new Date();
    
    static {
        GregorianCalendar cal = new GregorianCalendar(OLD_PARIS);
        cal.setTime(new Date(0L));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.YEAR, 1970);
        MILLIS_1970 = cal.getTime().getTime();
        cal.set(Calendar.YEAR, 2000);
        MILLIS_2000 = cal.getTime().getTime();
        cal.set(Calendar.YEAR, 1600);
        MILLIS_1600 = cal.getTime().getTime();
        cal.set(Calendar.YEAR, 100);
        MILLIS_100 = cal.getTime().getTime();
        cal.set(Calendar.YEAR, 400);
        MILLIS_400 = cal.getTime().getTime();
        cal.set(Calendar.YEAR, 1583);
        MILLIS_1583 = cal.getTime().getTime();
        cal.set(Calendar.YEAR, 1582);
        MILLIS_1582 = cal.getTime().getTime();
        cal.set(Calendar.YEAR, 1);
        MILLIS_1 = cal.getTime().getTime();
        
        cal.set(Calendar.YEAR, 1972);
        cal.set(Calendar.MONTH, Calendar.MARCH);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        MILLIS_1972_MARCH = cal.getTime().getTime();
        cal.set(Calendar.YEAR, 2000);
        MILLIS_2000_MARCH = cal.getTime().getTime();
        cal.set(Calendar.YEAR, 1600);
        MILLIS_1600_MARCH = cal.getTime().getTime();
        cal.set(Calendar.YEAR, 100);
        MILLIS_100_MARCH = cal.getTime().getTime();
        cal.set(Calendar.YEAR, 400);
        MILLIS_400_MARCH = cal.getTime().getTime();
        
//        System.out.println(new GJDateTime(MILLIS_1 + 4 * 366L * 24 * 60 * 60 * 1000).getLeapYear());
//        System.out.println(new GJDateTime(MILLIS_1 + 4 * 366L * 24 * 60 * 60 * 1000).getYear());
//        System.out.println(new GJDateTime(MILLIS_1 + 3 * 366L * 24 * 60 * 60 * 1000).getLeapYear());
//        System.out.println(new GJDateTime(MILLIS_1 + 3 * 366L * 24 * 60 * 60 * 1000).getYear());
//        System.out.println(new GJDateTime(MILLIS_1 + 2 * 366L * 24 * 60 * 60 * 1000).getLeapYear());
//        System.out.println(new GJDateTime(MILLIS_1 + 2 * 366L * 24 * 60 * 60 * 1000).getYear());
//        System.out.println(new GJDateTime(MILLIS_1 + 1 * 366L * 24 * 60 * 60 * 1000).getLeapYear());
//        System.out.println(new GJDateTime(MILLIS_1 + 1 * 366L * 24 * 60 * 60 * 1000).getYear());
//        System.out.println(new GJDateTime(MILLIS_1).getLeapYear());
//        System.out.println(new GJDateTime(MILLIS_1 - 1 * 360L * 24 * 60 * 60 * 1000).getLeapYear());
//        System.out.println(new GJDateTime(MILLIS_1 - 1 * 360L * 24 * 60 * 60 * 1000).getYear());
//        System.out.println(new GJDateTime(MILLIS_1 - 2 * 360L * 24 * 60 * 60 * 1000).getLeapYear());
//        System.out.println(new GJDateTime(MILLIS_1 - 2 * 360L * 24 * 60 * 60 * 1000).getYear());
//        System.out.println(new GJDateTime(MILLIS_1 - 3 * 360L * 24 * 60 * 60 * 1000).getLeapYear());
//        System.out.println(new GJDateTime(MILLIS_1 - 3 * 360L * 24 * 60 * 60 * 1000).getYear());
//        System.out.println(new GJDateTime(MILLIS_1 - 4 * 360L * 24 * 60 * 60 * 1000).getLeapYear());
//        System.out.println(new GJDateTime(MILLIS_1 - 4 * 360L * 24 * 60 * 60 * 1000).getYear());
        
        cal.set(Calendar.YEAR, 1582);
        cal.set(Calendar.MONTH, Calendar.OCTOBER);
        cal.set(Calendar.DAY_OF_MONTH, 15);
//        System.out.println(new GJDateTime(cal).getMonthOfYear());
//        System.out.println(new GJDateTime(cal).getDayOfMonth());
//        System.out.println(new GJDateTime(cal).getHourOfDay());
//        System.out.println("1 "+cal);
//        System.out.println(new Instant(cal));
//        cal.set(Calendar.DAY_OF_MONTH, 2);
//        System.out.println(cal);
//        System.out.println("2 "+new Instant(cal));
//        cal.set(Calendar.DAY_OF_MONTH, 3);
//        System.out.println(cal);
//        System.out.println("3 "+new Instant(cal));
//        cal.set(Calendar.DAY_OF_MONTH, 4);
//        System.out.println(cal);
//        System.out.println("4 "+new Instant(cal));
//        cal.set(Calendar.DAY_OF_MONTH, 5);
//        System.out.println(cal);
//        System.out.println("5 "+new Instant(cal));
//        cal.set(Calendar.DAY_OF_MONTH, 6);
//        System.out.println(cal);
//        System.out.println("6 "+new Instant(cal));
//        cal.set(Calendar.DAY_OF_MONTH, 9);
//        System.out.println(cal);
//        System.out.println("9 "+new Instant(cal));
//        cal.set(Calendar.DAY_OF_MONTH, 12);
//        System.out.println(cal);
//        System.out.println("12 "+new Instant(cal));
//        cal.set(Calendar.DAY_OF_MONTH, 14);
//        System.out.println(cal);
//        System.out.println("14 "+new Instant(cal));
//        cal.set(Calendar.DAY_OF_MONTH, 15);
//        System.out.println(cal);
//        System.out.println("15 "+new Instant(cal));
//        cal.set(Calendar.DAY_OF_MONTH, 16);
//        System.out.println(cal);
//        System.out.println("16 "+new Instant(cal));
//        cal.set(Calendar.DAY_OF_MONTH, 30);
//        System.out.println(cal);
//        System.out.println("30 "+new Instant(cal));
//        cal.set(Calendar.DAY_OF_MONTH, 31);
//        System.out.println(cal);
//        System.out.println("31 "+new Instant(cal));
//        cal.set(Calendar.DAY_OF_MONTH, 1);
//        cal.set(Calendar.MONTH, Calendar.NOVEMBER);
//        System.out.println(cal);
//        System.out.println("1 "+new Instant(cal));
        MILLIS_1582_OCTOBER = cal.getTime().getTime();
    }

    //-----------------------------------------------------------------------
    /**
     * TestDateTimeField constructor.
     * @param name
     */
    public AbstractTestDateTimeField(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    // subclass calculations
    //-----------------------------------------------------------------------
    /**
     * Override to return the field name.
     */
    protected abstract String getFieldName();
    /**
     * Override to return the unit size in millis
     */
    protected abstract long getUnitSize();
    /**
     * Override to return the field.
     */
    protected abstract DateTimeField getField();
    protected abstract int getMinimumValue();
    protected abstract int getMaximumValue();
    protected abstract int getCalendarValue(long millis);
    protected String getText(int value, Locale loc) {
        return Integer.toString(value);
    }
    protected String getShortText(int value, Locale loc) {
        return Integer.toString(value);
    }
    protected int getMaximumTextLength(Locale loc) {
        int max = getMaximumValue();
        return Integer.toString(max).length();
    }
    protected int getMaximumShortTextLength(Locale loc) {
        int max = getMaximumValue();
        return Integer.toString(max).length();
    }
    protected long getAddedResult(long millis, int add) {
        return millis + add * getUnitSize();
    }
    protected long getAddWrappedResult(long millis, int addWrapped) {
        if (addWrapped == 0) {
            return millis;
        }
        int val = getCalendarValue(millis);
        int max = getMaximumValue();
        int min = getMinimumValue();
        int add = val + addWrapped;
        while (add > max || add < min) {
            if (addWrapped >= 0) {
                add = add - (max - min) - 1;
            } else {
                add = add + (max - min) + 1;
            }
        }
        return millis + ((add - val) * getUnitSize());
    }
    
    // building of the multiple test point approach
    //-----------------------------------------------------------------------
    protected long getIncrementSize() {
        return 1 * 60 * 60 * 1000;  // 1 hour
    }
    protected long getTestRange() {
        return 2 * 24 * 60 * 60 * 1000;  // 2 days
    }
    
    protected long[] getTestPositions() {
        return new long[] {
            MILLIS_1970, MILLIS_1972_MARCH,
            MILLIS_2000, MILLIS_2000_MARCH,
            MILLIS_1600, MILLIS_1600_MARCH,
            MILLIS_1583, MILLIS_1582_OCTOBER,
            MILLIS_400, MILLIS_400_MARCH,
            MILLIS_100, MILLIS_100_MARCH,
            MILLIS_1582, MILLIS_1
        };
    }
    protected long[] getAllTestPositions() {
        if (RESULT == null) {
            long[] var = getTestPositions();
            RESULT = new long[7 * var.length * (2 * ((int) (getTestRange() / getIncrementSize())))];
            int count = 0;
            for (int i = 0; i < var.length; i++) {
                long start = var[i] - getTestRange();
                long end = var[i] + getTestRange();
//                System.out.println("START " + new Date(start));
//                System.out.println("END   " + new Date(end));
                for (long j = start; j < end ; j = j + getIncrementSize()) {
                    RESULT[count++] = j - 60000; // -1min
                    RESULT[count++] = j - 1000; // -1sec
                    RESULT[count++] = j - 1; // -1ms
                    RESULT[count++] = j;
                    RESULT[count++] = j + 1; // +1ms
                    RESULT[count++] = j + 1000; // +1sec
                    RESULT[count++] = j + 60000; // +1min
                }
            }
        }
        return RESULT;
    }

    protected DateTimeZone getZone() {
        return PARIS;
    }
    
    // test helpers for debugging
    //-----------------------------------------------------------------------
    /**
     * Special version of assert that debugs.
     */        
    private void assertEquals(long[] var, int i, int expected, int actual) {
        try {
            assertEquals(expected, actual);
        } catch (AssertionFailedError ex) {
            System.out.println(
                "\n Test:            " + getName() + " for " + getFieldName() +
                "\n loop count:      " + i +
                "\n milliseconds:    " + var[i] +
                "\n converted value: " + getCalendarValue(var[i]) +
                "\n test expects:    " + expected +
                "\n joda returned:   " + actual +
                "\n time via Instant:" + new Instant(var[i]) +
                "\n time via Date   :" + new Date(var[i]) +
                "\n time via Date   :" + DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.FRANCE).format(new Date(var[i])) +
                "\n time via Date   :" + DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.UK).format(new Date(var[i]))
            );
            throw ex;
        }
    }

    /**
     * Special version of assert that debugs.
     */        
    private void assertEquals(long[] var, int i, long expected, long actual) {
        try {
            assertEquals(expected, actual);
        } catch (AssertionFailedError ex) {
            System.out.println(
                "\n Test:            " + getName() + " for " + getFieldName() +
                "\n loop count:      " + i +
                "\n milliseconds:    " + var[i] +
                "\n converted value: " + getCalendarValue(var[i]) +
                "\n test expects:    " + expected +
                "\n joda returned:   " + actual +
                "\n test via Instant:" + new Instant(expected) +
                "\n test via Date:   " + new Date(expected) +
                "\n joda via Instant:" + new Instant(actual) +
                "\n joda via Date:   " + new Date(actual) +
                "\n time via Instant:" + new Instant(var[i]) +
                "\n time via Date   :" + new Date(var[i]) +
                "\n time via Date   :" + DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.FRANCE).format(new Date(var[i])) +
                "\n time via Date   :" + DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.UK).format(new Date(var[i]))
            );
            throw ex;
        }
    }

    /**
     * Special version of assert that debugs.
     */        
    private void assertEquals(long[] var, int i, String expected, String actual) {
        try {
            assertEquals(expected, actual);
        } catch (AssertionFailedError ex) {
            System.out.println(
                "\n Test:            " + getName() + " for " + getFieldName() +
                "\n loop count:      " + i +
                "\n milliseconds:    " + var[i] +
                "\n converted value: " + getCalendarValue(var[i]) +
                "\n test expects:    " + expected +
                "\n joda returned:   " + actual +
                "\n time via Instant:" + new Instant(var[i]) +
                "\n time via Date   :" + new Date(var[i]) +
                "\n time via Date   :" + DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.FRANCE).format(new Date(var[i])) +
                "\n time via Date   :" + DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.UK).format(new Date(var[i]))
            );
            throw ex;
        }
    }

    // the tests
    //-----------------------------------------------------------------------
    public void testGetName() {
        assertEquals(getFieldName(), getField().getName());
    }
    
    public void testGet() throws Exception {
        long[] var = getAllTestPositions();
        for (int i = 0; i < var.length; i++) {
            assertEquals(var, i, getCalendarValue(var[i]), getField().get(var[i]));
        }
    }

    public void testGetEx() throws Exception {
//        try {
//            getField().get(Long.MIN_VALUE);
//            fail();
//        } catch (IllegalArgumentException ex) {}
//        try {
//            getField().get(Long.MAX_VALUE);
//            fail();
//        } catch (IllegalArgumentException ex) {}
    }
    
    public void testSet() throws Exception {
        DateTimeField field = getField();
        long[] var = getAllTestPositions();
        int min = getMinimumValue();
        int max = getMaximumValue();
        for (int i = 0; i < var.length; i++) {
            assertEquals(var, i, min, getCalendarValue(field.set(var[i], min)));
        }
        if (min != max) {
            for (int i = 0; i < var.length; i++) {
                assertEquals(var, i, min + 1, getCalendarValue(field.set(var[i], min + 1)));
            }
        }
    }
    
    public void testSetEx() throws Exception {
        try {
            getField().set(0L, getMaximumValue() + 1);
        } catch (IllegalArgumentException ex) {}
        try {
            getField().set(0L, getMinimumValue() - 1);
        } catch (IllegalArgumentException ex) {}
    }
    
    public void testAdd() throws Exception {
        DateTimeField field = getField();
        long[] var = getAllTestPositions();
        for (int i = 0; i < var.length; i++) {
            long millis = var[i];
            assertEquals(var, i, getAddedResult(millis, 1), field.add(millis, 1));
            assertEquals(var, i, getAddedResult(millis, -1), field.add(millis, -1));
            assertEquals(var, i, getAddedResult(millis, 0), field.add(millis, 0));
            assertEquals(var, i, getAddedResult(millis, 2), field.add(millis, 2));
            assertEquals(var, i, getAddedResult(millis, 8), field.add(millis, 8));
            assertEquals(var, i, getAddedResult(millis, 9), field.add(millis, 9));
            assertEquals(var, i, getAddedResult(millis, -13), field.add(millis, -13));
        }
    }
    
    public void testAddWrapped() throws Exception {
        DateTimeField field = getField();
        long[] var = getAllTestPositions();
        for (int i = 0; i < var.length; i++) {
            long millis = var[i];
            assertEquals(var, i, getAddWrappedResult(millis, 1), field.addWrapped(millis, 1));
            assertEquals(var, i, getAddWrappedResult(millis, -1), field.addWrapped(millis, -1));
            assertEquals(var, i, getAddWrappedResult(millis, 0), field.addWrapped(millis, 0));
            assertEquals(var, i, getAddWrappedResult(millis, 2), field.addWrapped(millis, 2));
            assertEquals(var, i, getAddWrappedResult(millis, 8), field.addWrapped(millis, 8));
            assertEquals(var, i, getAddWrappedResult(millis, 9), field.addWrapped(millis, 9));
            assertEquals(var, i, getAddWrappedResult(millis, -13), field.addWrapped(millis, -13));
        }
    }

    public void testGetDifference() throws Exception {
        DateTimeField field = getField();
        long[] var = getAllTestPositions();
        for (int i = 0; i < var.length; i++) {
            long millis = var[i];
            long sum = field.add(millis, 1);
            assertEquals(var, i, 1, (int)field.getDifference(sum, millis));
            sum = field.add(millis, -1);
            assertEquals(var, i, -1, (int)field.getDifference(sum, millis));
            sum = field.add(millis, 0);
            assertEquals(var, i, 0, (int)field.getDifference(sum, millis));
            sum = field.add(millis, 2);
            assertEquals(var, i, 2, (int)field.getDifference(sum, millis));
            sum = field.add(millis, 8);
            assertEquals(var, i, 8, (int)field.getDifference(sum, millis));
            sum = field.add(millis, 9);
            assertEquals(var, i, 9, (int)field.getDifference(sum, millis));
            sum = field.add(millis, -13);
            assertEquals(var, i, -13, (int)field.getDifference(sum, millis));
        }
    }

    public void testGetAsTextLocale() throws Exception {
        DateTimeField field = getField();
        long[] var = getAllTestPositions();
        for (int i = 0; i < var.length; i++) {
            assertEquals(var, i, getText(field.get(var[i]), Locale.FRENCH), field.getAsText(var[i], Locale.FRENCH));
        }
    }

    public void testGetAsText() throws Exception {
        DateTimeField field = getField();
        long[] var = getAllTestPositions();
        for (int i = 0; i < var.length; i++) {
            assertEquals(var, i, getText(field.get(var[i]), Locale.getDefault()), field.getAsText(var[i]));
        }
    }

    public void testGetAsShortTextLocale() throws Exception {
        DateTimeField field = getField();
        long[] var = getAllTestPositions();
        for (int i = 0; i < var.length; i++) {
            assertEquals(var, i, getShortText(field.get(var[i]), Locale.FRENCH), field.getAsShortText(var[i], Locale.FRENCH));
        }
    }

    public void testGetAsShortText() throws Exception {
        DateTimeField field = getField();
        long[] var = getAllTestPositions();
        for (int i = 0; i < var.length; i++) {
            assertEquals(var, i, getShortText(field.get(var[i]), Locale.getDefault()), field.getAsShortText(var[i]));
        }
    }

    public void testSetTextLocale() throws Exception {
        DateTimeField field = getField();
        long[] var = getAllTestPositions();
        int min = getMinimumValue();
        int max = getMaximumValue();
        for (int i = 0; i < var.length; i++) {
            assertEquals(var, i, min, getCalendarValue(field.set(var[i], getText(min, Locale.FRENCH), Locale.FRENCH)));
        }
        if (min != max) {
            for (int i = 0; i < var.length; i++) {
                assertEquals(var, i, min + 1, getCalendarValue(field.set(var[i], getText(min + 1, Locale.FRENCH), Locale.FRENCH)));
            }
        }
        for (int i = 0; i < var.length; i++) {
            assertEquals(var, i, min, getCalendarValue(field.set(var[i], getShortText(min, Locale.FRENCH), Locale.FRENCH)));
        }
        if (min != max) {
            for (int i = 0; i < var.length; i++) {
                assertEquals(var, i, min + 1, getCalendarValue(field.set(var[i], getShortText(min + 1, Locale.FRENCH), Locale.FRENCH)));
            }
        }
    }

    public void testSetText() throws Exception {
        DateTimeField field = getField();
        long[] var = getAllTestPositions();
        int min = getMinimumValue();
        int max = getMaximumValue();
        for (int i = 0; i < var.length; i++) {
            assertEquals(var, i, min, getCalendarValue(field.set(var[i], getText(min, Locale.getDefault()))));
        }
        if (min != max) {
            for (int i = 0; i < var.length; i++) {
                assertEquals(var, i, min + 1, getCalendarValue(field.set(var[i], getText(min + 1, Locale.getDefault()))));
            }
        }
        for (int i = 0; i < var.length; i++) {
            assertEquals(var, i, min, getCalendarValue(field.set(var[i], getShortText(min, Locale.getDefault()))));
        }
        if (min != max) {
            for (int i = 0; i < var.length; i++) {
                assertEquals(var, i, min + 1, getCalendarValue(field.set(var[i], getShortText(min + 1, Locale.getDefault()))));
            }
        }
    }

    public void testIsLeapYear() throws Exception {
        assertEquals(false, getField().isLeap(0L));
    }
    
    public void testGetLeapAmount() throws Exception {
        assertEquals(0, getField().getLeapAmount(0L));
    }
    
    public void testGetMinimumValue() throws Exception {
        assertEquals(getMinimumValue(), getField().getMinimumValue());
    }

    public void testGetMinimumValueMillis() throws Exception {
    }

    public void testGetMaximumValue() throws Exception {
        assertEquals(getMaximumValue(), getField().getMaximumValue());
    }
    
    public void testGetMaximumValueMillis() throws Exception {
    }
    
    public void testGetMaximumTextLength() throws Exception {
        DateTimeField field = getField();
        assertEquals(getMaximumTextLength(Locale.FRENCH), field.getMaximumTextLength(Locale.FRENCH));
    }

    public void testGetMaximumShortTextLength(Locale loc) throws Exception {
        DateTimeField field = getField();
        assertEquals(getMaximumShortTextLength(Locale.FRENCH), field.getMaximumShortTextLength(Locale.FRENCH));
    }

    public void testRoundFloor() throws Exception {
    }

    public void testRoundCeiling() throws Exception {
    }

    public void testRoundHalfFloor() throws Exception {
    }

    public void testRoundHalfCeiling() throws Exception {
    }

    public void testRoundHalfEven() throws Exception {
    }

    public void testRemainder() throws Exception {
    }

}
