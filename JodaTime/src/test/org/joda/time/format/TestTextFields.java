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
package org.joda.time.format;

import java.util.Locale;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;

/**
 * Makes sure that text fields are correct for English.
 *
 * @author Brian S O'Neill
 */
public class TestTextFields extends TestCase {

    private static final DateTimeZone[] ZONES = {
        DateTimeZone.UTC,
        DateTimeZone.getInstance("Europe/Paris"),
        DateTimeZone.getInstance("Europe/London"),
        DateTimeZone.getInstance("Asia/Tokyo"),
        DateTimeZone.getInstance("America/Los_Angeles"),
    };

    private static final String[] MONTHS = {
        null,
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };

    private static final String[] WEEKDAYS = {
        null,
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    };

    private static final String[] HALFDAYS = {
        "AM", "PM"
    };

    private DateTimeZone originalDateTimeZone = null;
    private Locale originalLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestTextFields.class);
    }

    public TestTextFields(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        originalDateTimeZone = DateTimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(ZONES[0]);
        Locale.setDefault(Locale.ENGLISH);
    }

    protected void tearDown() throws Exception {
        DateTimeZone.setDefault(originalDateTimeZone);
        Locale.setDefault(originalLocale);
        originalDateTimeZone = null;
        originalLocale = null;
    }

    //-----------------------------------------------------------------------
    public void testMonthNames_monthStart() {
        DateTimePrinter printer = DateTimeFormat.getInstance().forPattern("MMMM");
        for (int i=0; i<ZONES.length; i++) {
            for (int month=1; month<=12; month++) {
                DateTime dt = new DateTime(2004, month, 1, 1, 20, 30, 40, ZONES[i]);
                String monthText = printer.print(dt);
                assertEquals(MONTHS[month], monthText);
            }
        }
    }

    public void testMonthNames_monthMiddle() {
        DateTimePrinter printer = DateTimeFormat.getInstance().forPattern("MMMM");
        for (int i=0; i<ZONES.length; i++) {
            for (int month=1; month<=12; month++) {
                DateTime dt = new DateTime(2004, month, 15, 12, 20, 30, 40, ZONES[i]);
                String monthText = printer.print(dt);
                assertEquals(MONTHS[month], monthText);
            }
        }
    }

    public void testMonthNames_monthEnd() {
        DateTimePrinter printer = DateTimeFormat.getInstance().forPattern("MMMM");
        for (int i=0; i<ZONES.length; i++) {
            Chronology chrono = Chronology.getISO(ZONES[i]);
            for (int month=1; month<=12; month++) {
                DateTime dt = new DateTime(2004, month, 1, 23, 20, 30, 40, chrono);
                int lastDay = chrono.dayOfMonth().getMaximumValue(dt.getMillis());
                dt = new DateTime(2004, month, lastDay, 23, 20, 30, 40, chrono);
                String monthText = printer.print(dt);
                assertEquals(MONTHS[month], monthText);
            }
        }
    }

    public void testWeekdayNames() {
        DateTimePrinter printer = DateTimeFormat.getInstance().forPattern("EEEE");
        for (int i=0; i<ZONES.length; i++) {
            MutableDateTime mdt = new MutableDateTime(2004, 1, 1, 1, 20, 30, 40, ZONES[i]);
            for (int day=1; day<=366; day++) {
                mdt.setDayOfYear(day);
                int weekday = mdt.getDayOfWeek();
                String weekdayText = printer.print(mdt);
                assertEquals(WEEKDAYS[weekday], weekdayText);
            }
        }
    }

    public void testHalfdayNames() {
        DateTimePrinter printer = DateTimeFormat.getInstance().forPattern("a");
        for (int i=0; i<ZONES.length; i++) {
            Chronology chrono = Chronology.getISO(ZONES[i]);
            MutableDateTime mdt = new MutableDateTime(2004, 5, 30, 0, 20, 30, 40, chrono);
            for (int hour=0; hour<24; hour++) {
                mdt.setHourOfDay(hour);
                int halfday = mdt.get(chrono.halfdayOfDay());
                String halfdayText = printer.print(mdt);
                assertEquals(HALFDAYS[halfday], halfdayText);
            }
        }
    }
}
