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
import java.util.Calendar;
import java.util.Locale;

import junit.framework.TestSuite;

import org.joda.test.time.AbstractTestDateTimeField;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.gj.GJChronology;
/**
 * This class is a Junit unit test for the date time field.
 *
 * @author Stephen Colebourne
 */
public class TestGJDayOfWeekDateTimeField extends AbstractTestDateTimeField {


    public TestGJDayOfWeekDateTimeField(String name) {
        super(name);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static TestSuite suite() {
        return new TestSuite(TestGJDayOfWeekDateTimeField.class);
    }
    protected void setUp() throws Exception {
        super.setUp();
    }
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    
    protected String getFieldName() {
        return "dayOfWeek";
    }
    protected DateTimeField getField() {
        return GJChronology.getInstance(getZone()).dayOfWeek();
    }
    protected int getMinimumValue() {
        return 1;
    }
    protected int getMaximumValue() {
        return 7;
    }
    protected int getCalendarValue(long millis) {
        millis = millis + getZone().getOffset(millis);
        int val = 0;
        if (millis >= 0) {
            val = (int) ((millis / (24 * 60 * 60 * 1000)) % 7);
        } else {
            val = (int) 6 + (int) (((millis + 1) / (24 * 60 * 60 * 1000)) % 7);
        }
        val = val + 4;  // shift 1970-01-01 to Thursday
        val = val % 7;  // readjust
        return (val == 0 ? 7 : val);
    }
    protected long getUnitSize() {
        return 24 * 60 * 60 * 1000; // 1 day
    }
    protected String getText(int value, Locale loc) {
        if (loc == null) {
            loc = Locale.getDefault();
        }
        DateFormatSymbols sym = new DateFormatSymbols(loc);
        switch (value) {
            case DateTimeConstants.MONDAY :
            return sym.getWeekdays()[Calendar.MONDAY];
            case DateTimeConstants.TUESDAY :
            return sym.getWeekdays()[Calendar.TUESDAY];
            case DateTimeConstants.WEDNESDAY :
            return sym.getWeekdays()[Calendar.WEDNESDAY];
            case DateTimeConstants.THURSDAY :
            return sym.getWeekdays()[Calendar.THURSDAY];
            case DateTimeConstants.FRIDAY :
            return sym.getWeekdays()[Calendar.FRIDAY];
            case DateTimeConstants.SATURDAY :
            return sym.getWeekdays()[Calendar.SATURDAY];
            case DateTimeConstants.SUNDAY :
            return sym.getWeekdays()[Calendar.SUNDAY];
        }
        return null;
    }
    protected String getShortText(int value, Locale loc) {
        if (loc == null) {
            loc = Locale.getDefault();
        }
        DateFormatSymbols sym = new DateFormatSymbols(loc);
        switch (value) {
            case DateTimeConstants.MONDAY :
            return sym.getShortWeekdays()[Calendar.MONDAY];
            case DateTimeConstants.TUESDAY :
            return sym.getShortWeekdays()[Calendar.TUESDAY];
            case DateTimeConstants.WEDNESDAY :
            return sym.getShortWeekdays()[Calendar.WEDNESDAY];
            case DateTimeConstants.THURSDAY :
            return sym.getShortWeekdays()[Calendar.THURSDAY];
            case DateTimeConstants.FRIDAY :
            return sym.getShortWeekdays()[Calendar.FRIDAY];
            case DateTimeConstants.SATURDAY :
            return sym.getShortWeekdays()[Calendar.SATURDAY];
            case DateTimeConstants.SUNDAY :
            return sym.getShortWeekdays()[Calendar.SUNDAY];
        }
        return null;
    }

    protected int getMaximumTextLength(Locale loc) {
        if (loc == null) {
            loc = Locale.getDefault();
        }
        DateFormatSymbols sym = new DateFormatSymbols(loc);
        int max = 0;
        for (int i = 0; i < sym.getWeekdays().length; i++) {
            max = (max >= sym.getWeekdays()[i].length() ? max : sym.getWeekdays()[i].length());
        }
        return max;
    }
    protected int getMaximumShortTextLength(Locale loc) {
        if (loc == null) {
            loc = Locale.getDefault();
        }
        DateFormatSymbols sym = new DateFormatSymbols(loc);
        int max = 0;
        for (int i = 0; i < sym.getShortWeekdays().length; i++) {
            max = (max >= sym.getShortWeekdays()[i].length() ? max : sym.getShortWeekdays()[i].length());
        }
        return max;
    }

}
