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
package org.joda.time.format;

import java.util.Locale;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.chrono.ISOChronology;

/**
 * Makes sure that text fields are correct for English.
 *
 * @author Brian S O'Neill
 */
public class TestTextFields extends TestCase {

    private static final DateTimeZone[] ZONES = {
        DateTimeZone.UTC,
        DateTimeZone.forID("Europe/Paris"),
        DateTimeZone.forID("Europe/London"),
        DateTimeZone.forID("Asia/Tokyo"),
        DateTimeZone.forID("America/Los_Angeles"),
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

    @Override
    protected void setUp() throws Exception {
        originalDateTimeZone = DateTimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(ZONES[0]);
        Locale.setDefault(Locale.ENGLISH);
    }

    @Override
    protected void tearDown() throws Exception {
        DateTimeZone.setDefault(originalDateTimeZone);
        Locale.setDefault(originalLocale);
        originalDateTimeZone = null;
        originalLocale = null;
    }

    //-----------------------------------------------------------------------
    public void testMonthNames_monthStart() {
        DateTimeFormatter printer = DateTimeFormat.forPattern("MMMM");
        for (int i=0; i<ZONES.length; i++) {
            for (int month=1; month<=12; month++) {
                DateTime dt = new DateTime(2004, month, 1, 1, 20, 30, 40, ZONES[i]);
                String monthText = printer.print(dt);
                assertEquals(MONTHS[month], monthText);
            }
        }
    }

    public void testMonthNames_monthMiddle() {
        DateTimeFormatter printer = DateTimeFormat.forPattern("MMMM");
        for (int i=0; i<ZONES.length; i++) {
            for (int month=1; month<=12; month++) {
                DateTime dt = new DateTime(2004, month, 15, 12, 20, 30, 40, ZONES[i]);
                String monthText = printer.print(dt);
                assertEquals(MONTHS[month], monthText);
            }
        }
    }

    public void testMonthNames_monthEnd() {
        DateTimeFormatter printer = DateTimeFormat.forPattern("MMMM");
        for (int i=0; i<ZONES.length; i++) {
            Chronology chrono = ISOChronology.getInstance(ZONES[i]);
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
        DateTimeFormatter printer = DateTimeFormat.forPattern("EEEE");
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
        DateTimeFormatter printer = DateTimeFormat.forPattern("a");
        for (int i=0; i<ZONES.length; i++) {
            Chronology chrono = ISOChronology.getInstance(ZONES[i]);
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
