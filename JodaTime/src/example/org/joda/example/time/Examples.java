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
package org.joda.example.time;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Instant;

/**
 * Example code demonstrating how to use Joda-Time.
 *
 * @author Stephen Colebourne
 */
public class Examples {

    public static void main(String[] args) throws Exception {
        try {
            new Examples().run();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    private void run() {
        runInstant();
        System.out.println();
        runDateTime();
        System.out.println();
    }
    
    private void runInstant() {
        System.out.println("Instant");
        System.out.println("=======");
        System.out.println("Instant stores a point in the datetime continuum as millisecs from 1970-01-01T00:00:00Z");
        System.out.println("Instant is immutable and thread-safe");
        System.out.println("                      in = new Instant()");
        Instant in = new Instant();
        System.out.println("Millisecond time:     in.getMillis():           " + in.getMillis());
        System.out.println("ISO string version:   in.toString():            " + in.toString());
        System.out.println("ISO chronology:       in.getChronology():       " + in.getChronology());
        System.out.println("UTC time zone:        in.getDateTimeZone():     " + in.getZone());
        System.out.println("Change millis:        in.withMillis(0):         " + in.withMillis(0L));
        System.out.println("");
        System.out.println("Convert to Instant:   in.toInstant():           " + in.toInstant());
        System.out.println("Convert to DateTime:  in.toDateTime():          " + in.toDateTime());
        System.out.println("Convert to MutableDT: in.toMutableDateTime():   " + in.toMutableDateTime());
        System.out.println("Convert to Date:      in.toDate():              " + in.toDate());
        System.out.println("");
        System.out.println("                      in2 = new Instant(in.getMillis() + 10)");
        Instant in2 = new Instant(in.getMillis() + 10);
        System.out.println("Equals ms and chrono: in.equals(in2):           " + in.equals(in2));
        System.out.println("Compare millisecond:  in.compareTo(in2):        " + in.compareTo(in2));
        System.out.println("Compare millisecond:  in.isEqual(in2):          " + in.isEqual(in2));
        System.out.println("Compare millisecond:  in.isAfter(in2):          " + in.isAfter(in2));
        System.out.println("Compare millisecond:  in.isBefore(in2):         " + in.isBefore(in2));
    }

    private void runDateTime() {
        System.out.println("DateTime");
        System.out.println("=======");
        System.out.println("DateTime stores a the date and time using millisecs from 1970-01-01T00:00:00Z internally");
        System.out.println("DateTime is immutable and thread-safe");
        System.out.println("                      in = new DateTime()");
        DateTime in = new DateTime();
        System.out.println("Millisecond time:     in.getMillis():           " + in.getMillis());
        System.out.println("ISO string version:   in.toString():            " + in.toString());
        System.out.println("ISO chronology:       in.getChronology():       " + in.getChronology());
        System.out.println("Your time zone:       in.getDateTimeZone():     " + in.getZone());
        System.out.println("Change millis:        in.withMillis(0):         " + in.withMillis(0L));
        System.out.println("");
        System.out.println("Get year:             in.getYear():             " + in.getYear());
        System.out.println("Get monthOfYear:      in.getMonthOfYear():      " + in.getMonthOfYear());
        System.out.println("Get dayOfMonth:       in.getDayOfMonth():       " + in.getDayOfMonth());
        System.out.println("...");
        System.out.println("Property access:      in.dayOfWeek().get():                   " + in.dayOfWeek().get());
        System.out.println("Day of week as text:  in.dayOfWeek().getAsText():             " + in.dayOfWeek().getAsText());
        System.out.println("Day as short text:    in.dayOfWeek().getAsShortText():        " + in.dayOfWeek().getAsShortText());
        System.out.println("Day in french:        in.dayOfWeek().getAsText(Locale.FRENCH):" + in.dayOfWeek().getAsText(Locale.FRENCH));
        System.out.println("Max allowed value:    in.dayOfWeek().getMaximumValue():       " + in.dayOfWeek().getMaximumValue());
        System.out.println("Min allowed value:    in.dayOfWeek().getMinimumValue():       " + in.dayOfWeek().getMinimumValue());
        System.out.println("Copy & set to Jan:    in.monthOfYear().setCopy(1):            " + in.monthOfYear().setCopy(1));
        System.out.println("Copy & add 14 months: in.monthOfYear().addCopy(14):           " + in.monthOfYear().addToCopy(14));
        System.out.println("Add 14 mnths in field:in.monthOfYear().addWrapFieldCopy(14):  " + in.monthOfYear().addWrapFieldToCopy(14));
        System.out.println("...");
        System.out.println("Convert to Instant:   in.toInstant():           " + in.toInstant());
        System.out.println("Convert to DateTime:  in.toDateTime():          " + in.toDateTime());
        System.out.println("Convert to MutableDT: in.toMutableDateTime():   " + in.toMutableDateTime());
        System.out.println("Convert to Date:      in.toDate():              " + in.toDate());
        System.out.println("Convert to Calendar:  in.toCalendar(Locale.UK): " + in.toCalendar(Locale.UK).toString().substring(0, 46));
        System.out.println("Convert to GregCal:   in.toGregorianCalendar(): " + in.toGregorianCalendar().toString().substring(0, 46));
        System.out.println("");
        System.out.println("                      in2 = new DateTime(in.getMillis() + 10)");
        DateTime in2 = new DateTime(in.getMillis() + 10);
        System.out.println("Equals ms and chrono: in.equals(in2):           " + in.equals(in2));
        System.out.println("Compare millisecond:  in.compareTo(in2):        " + in.compareTo(in2));
        System.out.println("Compare millisecond:  in.isEqual(in2):          " + in.isEqual(in2));
        System.out.println("Compare millisecond:  in.isAfter(in2):          " + in.isAfter(in2));
        System.out.println("Compare millisecond:  in.isBefore(in2):         " + in.isBefore(in2));
    }

}
