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
package org.joda.test.time.gj;

import java.text.DateFormatSymbols;
import java.util.Locale;

import junit.framework.TestSuite;

import org.joda.time.DateTimeField;
import org.joda.time.chrono.gj.GJChronology;
/**
 * This class is a Junit unit test for the date time field.
 *
 * @author Stephen Colebourne
 */
public class TestGJMonthOfYearDateTimeField extends AbstractTestGJDateTimeField {


    public TestGJMonthOfYearDateTimeField(String name) {
        super(name);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static TestSuite suite() {
        return new TestSuite(TestGJMonthOfYearDateTimeField.class);
    }
    protected void setUp() throws Exception {
        super.setUp();
    }
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    
    protected String getFieldName() {
        return "monthOfYear";
    }
    protected DateTimeField getField() {
        return GJChronology.getInstance(getZone()).monthOfYear();
    }
    protected int getMinimumValue() {
        return 1;
    }
    protected int getMaximumValue() {
        return 12;
    }
    protected int getCalendarValue(long millis) {
        millis = millis + getZone().getOffset(millis);
        return getDMYDS(millis)[1];
    }
    protected long getAddedResult(long millis, int add) {
        if (add == 0) {
            return millis;
        }
//        System.out.println(new Instant(millis) + "  " + add);
        
        int[] before = getDMYDS(millis + getZone().getOffset(millis));
        int currentDay = before[0];
        int currentMonth = before[1];
        long newMillis = millis + (add * getUnitSize()) + (((int) (add/2)) * 24 * 60 * 60 * 1000);
        int[] after = getDMYDS(newMillis + getZone().getOffset(newMillis));
        int newDay = after[0];
        int newMonth = after[1];
        
        // calc effective add
        int max = 12;
        int min = 1;
        int added = currentMonth + add;
        while (added > max || added < min) {
            if (add >= 0) {
                added = added - (max - min) - 1;
            } else {
                added = added + (max - min) + 1;
            }
        }
        int effectiveAdd = added - currentMonth;
        
        // ensure month correct
        while (currentMonth + effectiveAdd != newMonth) {
            if (currentMonth + effectiveAdd == 1 && newMonth == 12) {
                newMillis = newMillis + 24 * 60 * 60 * 1000;
            } else if (currentMonth + effectiveAdd == 12 && newMonth == 1) {
                newMillis = newMillis - 24 * 60 * 60 * 1000;
            } else if (currentMonth + effectiveAdd > newMonth) {
                newMillis = newMillis + 24 * 60 * 60 * 1000;
            } else {
                newMillis = newMillis - 24 * 60 * 60 * 1000;
            }
            after = getDMYDS(newMillis + getZone().getOffset(newMillis));
            newDay = after[0];
            newMonth = after[1];
        }
        
        // ensure day correct
        if (currentDay < newDay) {
            while (currentDay < newDay) {
                newMillis = newMillis - 24 * 60 * 60 * 1000;
                after = getDMYDS(newMillis + getZone().getOffset(newMillis));
                newDay = after[0];
                newMonth = after[1];
                if (currentMonth + effectiveAdd != newMonth) {
                    newMillis = newMillis + 24 * 60 * 60 * 1000;
                    break;
                }
            }
        } else {
            while (currentDay > newDay) {
                newMillis = newMillis + 24 * 60 * 60 * 1000;
                after = getDMYDS(newMillis + getZone().getOffset(newMillis));
                newDay = after[0];
                newMonth = after[1];
                if (currentMonth + effectiveAdd != newMonth) {
                    newMillis = newMillis - 24 * 60 * 60 * 1000;
                    break;
                }
            }
        }
        
//        System.out.println(new Instant(newMillis) + "  " + add);
        return newMillis;
    }
    protected long getAddWrappedResult(long millis, int addWrapped) {
        if (addWrapped == 0) {
            return millis;
        }
        int[] before = getDMYDS(millis + getZone().getOffset(millis));
        int val = before[1];
        int max = 12;
        int min = 1;
        int add = val + addWrapped;
        while (add > max || add < min) {
            if (addWrapped >= 0) {
                add = add - (max - min) - 1;
            } else {
                add = add + (max - min) + 1;
            }
        }
        int amount = add - val;
        if (amount == 0) {
            return millis;
        } else {
            return getAddedResult(millis, amount);
        }
    }
    
    protected long getUnitSize() {
        return 30L * 24 * 60 * 60 * 1000; // 30 day
    }
    protected long getIncrementSize() {
        return 8L * 60 * 60 * 1000;  // 8 hours
    }
    protected long getTestRange() {
        return 32L * 24 * 60 * 60 * 1000;  // 32 days
    }
    
    protected String getText(int value, Locale loc) {
        if (loc == null) {
            loc = Locale.getDefault();
        }
        DateFormatSymbols sym = new DateFormatSymbols(loc);
        return sym.getMonths()[value - 1];
    }
    protected String getShortText(int value, Locale loc) {
        if (loc == null) {
            loc = Locale.getDefault();
        }
        DateFormatSymbols sym = new DateFormatSymbols(loc);
        return sym.getShortMonths()[value - 1];
    }
    
    protected int getMaximumTextLength(Locale loc) {
        if (loc == null) {
            loc = Locale.getDefault();
        }
        DateFormatSymbols sym = new DateFormatSymbols(loc);
        int max = 0;
        for (int i = 0; i < sym.getMonths().length; i++) {
            max = (max >= sym.getMonths()[i].length() ? max : sym.getMonths()[i].length());
        }
        return max;
    }
    protected int getMaximumShortTextLength(Locale loc) {
        if (loc == null) {
            loc = Locale.getDefault();
        }
        DateFormatSymbols sym = new DateFormatSymbols(loc);
        int max = 0;
        for (int i = 0; i < sym.getShortMonths().length; i++) {
            max = (max >= sym.getShortMonths()[i].length() ? max : sym.getShortMonths()[i].length());
        }
        return max;
    }

}
