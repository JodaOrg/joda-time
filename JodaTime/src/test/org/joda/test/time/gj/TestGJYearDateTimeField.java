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
package org.joda.test.time.gj;

import java.util.Date;

import junit.framework.AssertionFailedError;
import junit.framework.TestSuite;

import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.Instant;
import org.joda.time.chrono.GJChronology;
/**
 * This class is a Junit unit test for the date time field.
 *
 * @author Stephen Colebourne
 */
public class TestGJYearDateTimeField extends AbstractTestGJDateTimeField {


    public TestGJYearDateTimeField(String name) {
        super(name);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static TestSuite suite() {
        return new TestSuite(TestGJYearDateTimeField.class);
    }
    protected void setUp() throws Exception {
        super.setUp();
    }
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    
    protected String getFieldName() {
        return "year";
    }
    protected DateTimeField getField() {
        return GJChronology.getInstance(getZone()).year();
    }
    protected int getMinimumValue() {
        return -292269053;
    }
    protected int getMaximumValue() {
        return 292278993;
    }
    protected int getCalendarValue(long millis) {
        millis = millis + getZone().getOffset(millis);
        return getDMYDS(millis)[2];
    }
    protected long getAddedResult(long millis, int add) {
        if (add == 0) {
            return millis;
        }
        int[] fields = getDMYDS(millis + getZone().getOffset(millis));
        int day = fields[0];
        int month = fields[1];
        int year = fields[2];
        int yearLength = fields[5];
        int newYear = year + add;
        if (add > 0) {
            int addDays = 0;
            if (month >= 3) {
                for (int i = year + 1; i <= newYear; i++) {
                    if (isLeap(i, 1582)) {
                        addDays += 366;
                    } else {
                        addDays += 365;
                    }
                }
            } else {
                for (int i = year; i < newYear; i++) {
                    if (isLeap(i, 1582)) {
                        addDays += 366;
                    } else {
                        addDays += 365;
                    }
                }
            }
            if (day == 29 && month == 2 && isLeap(newYear, 1582) == false) {
                addDays = addDays - 1;
            }
            millis = millis + (addDays * 24 * 60 * 60 * 1000L);
        } else {
            int addDays = 0;
            if (month >= 3) {
                for (int i = year; i > newYear; i--) {
                    if (isLeap(i, 1582)) {
                        addDays -= 366;
                    } else {
                        addDays -= 365;
                    }
                }
            } else {
                for (int i = year - 1; i >= newYear; i--) {
                    if (isLeap(i, 1582)) {
                        addDays -= 366;
                    } else {
                        addDays -= 365;
                    }
                }
            }
            if (day == 29 && month == 2 && isLeap(newYear, 1582) == false) {
                addDays = addDays - 1;
            }
            millis = millis + (addDays * 24 * 60 * 60 * 1000L);
        }
        return millis;
    }
    protected long getAddWrappedResult(long millis, int add) {
        return getAddedResult(millis, add);
    }
    
    protected long getUnitSize() {
        return 365 * 24 * 60 * 60 * 1000; // 365 day
    }
    protected long getIncrementSize() {
        return 8 * 60 * 60 * 1000;  // 8 hours
    }
    protected long getTestRange() {
        return 20L * 24 * 60 * 60 * 1000;  // 20 days
    }
    
    
    public void testIsLeapYear() throws Exception {
        DateTimeField field = getField();
        long[] var = getAllTestPositions();
        for (int i = 0; i < var.length; i++) {
            try {
                assertEquals(iCalendar.isLeapYear(field.get(var[i])), field.isLeap(var[i]));
            } catch (AssertionFailedError ex) {
                System.out.println("LEAPYEAR: i: "+i+" cal:"+getCalendarValue(var[i])+" get:"+getField().get(var[i])+
                    " time:"+new Instant(var[i])+","+new Date(var[i])+
                    " day:"+new DateTime(var[i], GJChronology.getInstance()).getDayOfMonth()+
                    " month:"+new DateTime(var[i], GJChronology.getInstance()).getMonthOfYear()+
                    " year:"+new DateTime(var[i], GJChronology.getInstance()).getYear());
                throw ex;
            }
        }
    }
    
    public void testGetLeapAmount() throws Exception {
        DateTimeField field = getField();
        long[] var = getAllTestPositions();
        for (int i = 0; i < var.length; i++) {
            assertEquals(iCalendar.isLeapYear(field.get(var[i])) ? 1 : 0, field.getLeapAmount(var[i]));
        }
    }
    
}
