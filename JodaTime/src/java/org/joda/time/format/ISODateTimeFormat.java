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
package org.joda.time.format;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.iso.ISOChronology;

/**
 * Factory methods for many ISO8601 formats (the ISO standard is a framework
 * for outputting data, but not an absolute standard). The most common formats
 * are date, time, and dateTime.
 * 
 * @author Brian S O'Neill
 * @see DateTimeFormat
 * @see DateTimeFormatterBuilder
 */
public class ISODateTimeFormat {

    // Maps Chronology instances to instances.
    private static Map cCache = new HashMap(7);

    public static ISODateTimeFormat getInstanceUTC() {
        return getInstance(ISOChronology.getInstanceUTC());
    }

    public static ISODateTimeFormat getInstance() {
        return getInstance(ISOChronology.getInstance());
    }

    public static ISODateTimeFormat getInstance(DateTimeZone zone) {
        return getInstance(ISOChronology.getInstance(zone));
    }

    /**
     * @param chrono Chronology to use
     */
    public static synchronized ISODateTimeFormat getInstance(Chronology chrono) {
        if (chrono == null) {
            throw new IllegalArgumentException("The Chronology must not be null");
        }
        ISODateTimeFormat instance = (ISODateTimeFormat)cCache.get(chrono);
        if (instance == null) {
            instance = new ISODateTimeFormat(chrono);
            cCache.put(chrono, instance);
        }
        return instance;
    }

    private final Chronology iChrono;

    private transient DateTimeFormatter
        ye,  // year element (yyyy)
        me,  // month element (-MM)
        de,  // day element (-dd)
        he,  // hour element (HH)
        mne, // minute element (:mm)
        se,  // second element (:ss)
        fe,  // fraction element (.SSS)
        ze,  // zone offset element
        
        //y,   // year (same as year element)
        ym,  // year month
        ymd, // year month day

        //h,    // hour (same as hour element)
        hm,   // hour minute
        hms,  // hour minute second
        hmsf, // hour minute second fraction

        dh,    // date hour
        dhm,   // date hour minute
        dhms,  // date hour minute second
        dhmsf, // date hour minute second fraction

        //d,  // date (same as ymd)
        t,  // time
        dt, // date time

        bd,  // basic date
        bt,  // basic time
        bdt; // basic date time

    private transient DateTimeParser
        dpe, // date parser element
        tpe, // time parser element
        dp, // date parser
        tp, // time parser
        dtp; // date time parser

    private ISODateTimeFormat(Chronology chrono) {
        iChrono = chrono;
    }

    /**
     * Returns a generic ISO date parser that accepts formats described by
     * the following syntax:
     * <pre>
     * date         = date-element ['T' offset]
     * date-element = yyyy ['-' MM ['-' dd]]
     * offset       = 'Z' | (('+' | '-') HH ':' mm)
     * </pre>
     */
    public DateTimeParser dateParser() {
        if (dp == null) {
            dp = new DateTimeFormatterBuilder(iChrono)
                .append(dateElementParser())
                .appendOptional
                (new DateTimeFormatterBuilder(iChrono)
                 .appendLiteral('T')
                 .append(offsetElement())
                 .toParser())
                .toParser();
        }
        return dp;
    }

    /**
     * Returns a generic ISO date parser that accepts formats described by
     * the following syntax:
     * <pre>
     * date-element = yyyy ['-' MM ['-' dd]]
     * </pre>
     */
    public DateTimeParser dateElementParser() {
        if (dpe == null) {
            dpe = new DateTimeFormatterBuilder(iChrono)
                .append(yearElement())
                .appendOptional
                (new DateTimeFormatterBuilder(iChrono)
                 .append(monthElement())
                 .appendOptional(dayElement())
                 .toParser())
                .toParser();
        }
        return dpe;
    }

    /**
     * Returns a generic ISO time parser that accepts formats described by
     * the following syntax:
     * <pre>
     * time         = ['T'] time-element [offset]
     * time-element = HH [':' mm [':' ss ['.' SSS]]]
     * offset       = 'Z' | (('+' | '-') HH ':' mm)
     * </pre>
     */
    public DateTimeParser timeParser() {
        if (tp == null) {
            tp = new DateTimeFormatterBuilder(iChrono)
                .appendOptional
                (new DateTimeFormatterBuilder(iChrono)
                 .appendLiteral('T')
                 .toParser())
                .append(timeElementParser())
                .appendOptional(offsetElement())
                .toParser();
        }
        return tp;
    }

    /**
     * Returns a generic ISO time parser that accepts formats described by
     * the following syntax:
     * <pre>
     * time-element = HH [':' mm [':' ss ['.' SSS]]]
     * </pre>
     */
    public DateTimeParser timeElementParser() {
        if (tpe == null) {
            tpe = new DateTimeFormatterBuilder(iChrono)
                .append(hourElement())
                .appendOptional
                (new DateTimeFormatterBuilder(iChrono)
                 .append(minuteElement())
                 .appendOptional
                 (new DateTimeFormatterBuilder(iChrono)
                  .append(secondElement())
                  .appendOptional(fractionElement())
                  .toParser())
                 .toParser())
                .toParser();
        }
        return tpe;
    }

    /**
     * Returns a generic ISO datetime parser that accepts formats described by
     * the following syntax:
     * <pre>
     * datetime     = time | (date-element [time | ('T' offset)])
     * time         = 'T' time-element [offset]
     * date-element = yyyy ['-' MM ['-' dd]]
     * time-element = HH [':' mm [':' ss ['.' SSS]]]
     * offset       = 'Z' | (('+' | '-') HH ':' mm)
     * </pre>
     */
    public DateTimeParser dateTimeParser() {
        if (dtp == null) {
            // This is different from the general time parser in that the 'T'
            // is required.
            DateTimeParser time = new DateTimeFormatterBuilder(iChrono)
                .appendLiteral('T')
                .append(timeElementParser())
                .appendOptional(offsetElement())
                .toParser();

            dtp = new DateTimeFormatterBuilder(iChrono)
                .append(null, new DateTimeParser[] {
                    time,
                    new DateTimeFormatterBuilder(iChrono)
                    .append(dateElementParser())
                    .append(null, new DateTimeParser[] {
                        time,
                        new DateTimeFormatterBuilder(iChrono)
                        .appendLiteral('T')
                        .append(offsetElement())
                        .toParser(),
                        null
                    })
                    .toParser()
                })
                .toParser();
        }
        return dtp;
    }

    /**
     * Returns a formatter for a full date as four digit year, two digit month
     * of year, and two digit day of month. (yyyy-MM-dd)
     */
    public DateTimeFormatter date() {
        return yearMonthDay();
    }

    /**
     * Returns a formatter for a two digit hour of day, two digit minute of
     * hour, two digit second of minute, three digit fraction of second, and
     * time zone offset. (HH:mm:ss.SSSZ) The time zone offset is 'Z' for zero, and
     * of the form '\u00b1HH:mm' for non-zero.
     */
    public DateTimeFormatter time() {
        if (t == null) {
            t = new DateTimeFormatterBuilder(iChrono)
                .append(hourMinuteSecondFraction())
                .append(offsetElement())
                .toFormatter();
        }
        return t;
    }

    /**
     * Returns a formatter that combines a full date and time, separated by a 'T'.
     * (yyyy-MM-ddTHH:mm:ss.SSSZ)
     */
    public DateTimeFormatter dateTime() {
        if (dt == null) {
            dt = new DateTimeFormatterBuilder(iChrono)
                .append(date())
                .appendLiteral('T')
                .append(time())
                .toFormatter();
        }
        return dt;
    }

    /**
     * Returns a basic formatter for a full date as four digit year, two digit
     * month of year, and two digit day of month. (yyyyMMdd)
     */
    public DateTimeFormatter basicDate() {
        if (bd == null) {
            bd = new DateTimeFormatterBuilder(iChrono)
                .appendPattern("yyyyMMdd")
                .toFormatter();
        }
        return bd;
    }

    /**
     * Returns a basic formatter for a two digit hour of day, two digit minute
     * of hour, two digit second of minute, and time zone offset. (HHmmssZ) The time zone
     * offset is blank for zero, and of the form '\u00b1HHmm' for non-zero.
     */
    public DateTimeFormatter basicTime() {
        if (bt == null) {
            bt = new DateTimeFormatterBuilder(iChrono)
                .appendPattern("HHmmss")
                .appendTimeZoneOffset("", false, 1, 2)
                .toFormatter();
        }
        return bt;
    }

    /**
     * Returns a basic formatter that combines a basic date and time, separated
     * by a 'T'. (yyyyMMddTHHmmssZ)
     */
    public DateTimeFormatter basicDateTime() {
        if (bdt == null) {
            bdt = new DateTimeFormatterBuilder(iChrono)
                .append(basicDate())
                .appendLiteral('T')
                .append(basicTime())
                .toFormatter();
        }
        return bdt;
    }

    /**
     * Returns a formatter for a four digit year. (yyyy)
     */
    public DateTimeFormatter year() {
        return yearElement();
    }

    /**
     * Returns a formatter for a four digit year and two digit month of
     * year. (yyyy-MM)
     */
    public DateTimeFormatter yearMonth() {
        if (ym == null) {
            ym = new DateTimeFormatterBuilder(iChrono)
                .append(yearElement())
                .append(monthElement())
                .toFormatter();
        }
        return ym;
    }

    /**
     * Returns a formatter for a four digit year, two digit month of year, and
     * two digit day of month. (yyyy-MM-dd)
     */
    public DateTimeFormatter yearMonthDay() {
        if (ymd == null) {
            ymd = new DateTimeFormatterBuilder(iChrono)
                .append(yearElement())
                .append(monthElement())
                .append(dayElement())
                .toFormatter();
        }
        return ymd;
    }

    /**
     * Returns a formatter for a two digit hour of day. (HH)
     */
    public DateTimeFormatter hour() {
        return hourElement();
    }

    /**
     * Returns a formatter for a two digit hour of day and two digit minute of
     * hour. (HH:mm)
     */
    public DateTimeFormatter hourMinute() {
        if (hm == null) {
            hm = new DateTimeFormatterBuilder(iChrono)
                .append(hourElement())
                .append(minuteElement())
                .toFormatter();
        }
        return hm;
    }

    /**
     * Returns a formatter for a two digit hour of day, two digit minute of
     * hour, and two digit second of minute. (HH:mm:ss)
     */
    public DateTimeFormatter hourMinuteSecond() {
        if (hms == null) {
            hms = new DateTimeFormatterBuilder(iChrono)
                .append(hourElement())
                .append(minuteElement())
                .append(secondElement())
                .toFormatter();
        }
        return hms;
    }

    /**
     * Returns a formatter for a two digit hour of day, two digit minute of
     * hour, two digit second of minute, and three digit fraction of
     * second. (HH:mm:ss.SSS)
     */
    public DateTimeFormatter hourMinuteSecondFraction() {
        if (hmsf == null) {
            hmsf = new DateTimeFormatterBuilder(iChrono)
                .append(hourElement())
                .append(minuteElement())
                .append(secondElement())
                .append(fractionElement())
                .toFormatter();
        }
        return hmsf;
    }

    /**
     * Returns a formatter that combines a full date and two digit hour of
     * day. (yyyy-MM-ddTHH)
     */
    public DateTimeFormatter dateHour() {
        if (dh == null) {
            dh = new DateTimeFormatterBuilder(iChrono)
                .append(date())
                .appendLiteral('T')
                .append(hour())
                .toFormatter();
        }
        return dh;
    }

    /**
     * Returns a formatter that combines a full date, two digit hour of day,
     * and two digit minute of hour. (yyyy-MM-ddTHH:mm)
     */
    public DateTimeFormatter dateHourMinute() {
        if (dhm == null) {
            dhm = new DateTimeFormatterBuilder(iChrono)
                .append(date())
                .appendLiteral('T')
                .append(hourMinute())
                .toFormatter();
        }
        return dhm;
    }

    /**
     * Returns a formatter that combines a full date, two digit hour of day,
     * two digit minute of hour, and two digit second of
     * minute. (yyyy-MM-ddTHH:mm:ss)
     */
    public DateTimeFormatter dateHourMinuteSecond() {
        if (dhms == null) {
            dhms = new DateTimeFormatterBuilder(iChrono)
                .append(date())
                .appendLiteral('T')
                .append(hourMinuteSecond())
                .toFormatter();
        }
        return dhms;
    }

    /**
     * Returns a formatter that combines a full date, two digit hour of day,
     * two digit minute of hour, two digit second of minute, and three digit
     * fraction of second. (yyyy-MM-ddTHH:mm:ss.SSS)
     */
    public DateTimeFormatter dateHourMinuteSecondFraction() {
        if (dhmsf == null) {
            dhmsf = new DateTimeFormatterBuilder(iChrono)
                .append(date())
                .appendLiteral('T')
                .append(hourMinuteSecondFraction())
                .toFormatter();
        }
        return dhmsf;
    }

    private DateTimeFormatter yearElement() {
        if (ye == null) {
            ye = new DateTimeFormatterBuilder(iChrono)
                .appendYear(4, 9)
                .toFormatter();
        }
        return ye;
    }

    private DateTimeFormatter monthElement() {
        if (me == null) {
            me = new DateTimeFormatterBuilder(iChrono)
                .appendLiteral('-')
                .appendMonthOfYear(2)
                .toFormatter();
        }
        return me;
    }

    private DateTimeFormatter dayElement() {
        if (de == null) {
            de = new DateTimeFormatterBuilder(iChrono)
                .appendLiteral('-')
                .appendDayOfMonth(2)
                .toFormatter();
        }
        return de;
    }

    private DateTimeFormatter hourElement() {
        if (he == null) {
            he = new DateTimeFormatterBuilder(iChrono)
                .appendHourOfDay(2)
                .toFormatter();
        }
        return he;
    }

    private DateTimeFormatter minuteElement() {
        if (mne == null) {
            mne = new DateTimeFormatterBuilder(iChrono)
                .appendLiteral(':')
                .appendMinuteOfHour(2)
                .toFormatter();
        }
        return mne;
    }

    private DateTimeFormatter secondElement() {
        if (se == null) {
            se = new DateTimeFormatterBuilder(iChrono)
                .appendLiteral(':')
                .appendSecondOfMinute(2)
                .toFormatter();
        }
        return se;
    }

    private DateTimeFormatter fractionElement() {
        if (fe == null) {
            fe = new DateTimeFormatterBuilder(iChrono)
                .appendLiteral('.')
                // Support parsing up to nanosecond precision even though
                // those extra digits will be dropped.
                .appendFractionOfSecond(3, 9)
                .toFormatter();
        }
        return fe;
    }

    private DateTimeFormatter offsetElement() {
        if (ze == null) {
            ze = new DateTimeFormatterBuilder(iChrono)
                .appendTimeZoneOffset("Z", true, 2, 2)
                .toFormatter();
        }
        return ze;
    }
}
