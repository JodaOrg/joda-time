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
        System.out.println("No chronology:        in.getChronology():       " + in.getChronology());
        System.out.println("No time zone:         in.getDateTimeZone():     " + in.getZone());
        System.out.println("Change millis:        in.withMillis(0):         " + in.withMillis(0L));
        System.out.println("");
        System.out.println("Convert to Instant:   in.toInstant():           " + in.toInstant());
        System.out.println("Convert to DateTime:  in.toDateTime():          " + in.toDateTime());
        System.out.println("Convert to trusted:   in.toTrustedISODateTime():" + in.toTrustedISODateTime());
        System.out.println("Convert to MutableDT: in.toMutableDateTime():   " + in.toMutableDateTime());
        System.out.println("Convert to Date:      in.toDate():              " + in.toDate());
        System.out.println("Convert to Calendar:  in.toCalendar(Locale.UK): " + in.toCalendar(Locale.UK).toString().substring(0, 46));
        System.out.println("Convert to GregCal:   in.toGregorianCalendar(): " + in.toGregorianCalendar().toString().substring(0, 46));
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
        System.out.println("Convert to trusted:   in.toTrustedISODateTime():" + in.toTrustedISODateTime());
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
