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


/**
 * ISODateTimeFormat provides factory methods for the ISO8601 standard.
 * <p>
 * ISO8601 is the international standard for data interchange. It defines a
 * framework, rather than an absolute standard. As a result this provider has a
 * number of methods that represent common uses of the framework. The most common
 * formats are {@link #date() date}, {@link #time() time}, and {@link #dateTime() dateTime}.
 * <p>
 * For example, to format a date time in ISO format:
 * <pre>
 * DateTime dt = new DateTime();
 * DateTimeFormatter fmt = DateTimeFormat.getInstance().dateTime();
 * String str = fmt.print(dt);
 * </pre>
 * <p>
 * ISODateTimeFormat is thread-safe and immutable, and the formatters it
 * returns are as well.
 *
 * @author Brian S O'Neill
 * @since 1.0
 * @see DateTimeFormat
 * @see DateTimeFormatterBuilder
 */
public class ISODateTimeFormat {

    /** The singleton instance. */
    private static final ISODateTimeFormat INSTANCE = new ISODateTimeFormat();

    /**
     * Gets an instance of a the format provider.
     * 
     * @return a format provider
     */
    public static ISODateTimeFormat getInstance() {
        return INSTANCE;
    }

    //-----------------------------------------------------------------------
    private transient DateTimeFormatter
        ye,  // year element (yyyy)
        mye, // monthOfYear element (-MM)
        dme, // dayOfMonth element (-dd)
        we,  // weekyear element (xxxx)
        wwe, // weekOfWeekyear element (-ww)
        dwe, // dayOfWeek element (-ee)
        dye, // dayOfYear element (-DDD)
        hde, // hourOfDay element (HH)
        mhe, // minuteOfHour element (:mm)
        sme, // secondOfMinute element (:ss)
        lse, // millisOfSecond element (.SSS)
        fse, // fractionOfSecond element (.SSSSSSSSS)
        ze,  // zone offset element
        lte, // literal 'T' element
        
        //y,   // year (same as year element)
        ym,  // year month
        ymd, // year month day

        //w,   // weekyear (same as weekyear element)
        ww,  // weekyear week
        wwd, // weekyear week day

        //h,    // hour (same as hour element)
        hm,   // hour minute
        hms,  // hour minute second
        hmsl, // hour minute second millis
        hmsf, // hour minute second fraction

        dh,    // date hour
        dhm,   // date hour minute
        dhms,  // date hour minute second
        dhmsl, // date hour minute second millis
        dhmsf, // date hour minute second fraction

        //d,  // date (same as ymd)
        t,  // time
        tx,  // time no millis
        tt,  // Ttime
        ttx,  // Ttime no millis
        dt, // date time
        dtx, // date time no millis

        //wd,  // week date (same as wwd)
        wdt, // week date time
        wdtx, // week date time no millis

        bd,  // basic date
        bt,  // basic time
        btx,  // basic time no millis
        btt, // basic Ttime
        bttx, // basic Ttime no millis
        bdt, // basic date time
        bdtx, // basic date time no millis

        bwd,  // basic week date
        bwdt, // basic week date time
        bwdtx; // basic week date time no millis

    private transient DateTimeParser
        dpe, // date parser element
        tpe, // time parser element
        dp,  // date parser
        tp,  // time parser
        dtp; // date time parser

    /**
     * Restricted constructor.
     * 
     * @param chrono  the chronology to use, must not be null
     */
    private ISODateTimeFormat() {
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a generic ISO date parser. It accepts formats described by
     * the following syntax:
     * <pre>
     * date              = date-element ['T' offset]
     * date-element      = std-date-element | ord-date-element | week-date-element
     * std-date-element  = yyyy ['-' MM ['-' dd]]
     * ord-date-element  = yyyy ['-' DDD]
     * week-date-element = xxxx '-W' ww ['-' e]
     * offset            = 'Z' | (('+' | '-') HH [':' mm [':' ss [('.' | ',') SSS]]])
     * </pre>
     */
    public DateTimeParser dateParser() {
        if (dp == null) {
            dp = new DateTimeFormatterBuilder()
                .append(dateElementParser())
                .appendOptional
                (new DateTimeFormatterBuilder()
                 .appendLiteral('T')
                 .append(offsetElement())
                 .toParser())
                .toParser();
        }
        return dp;
    }

    /**
     * Returns a generic ISO date parser. It accepts formats described by
     * the following syntax:
     * <pre>
     * date-element      = std-date-element | ord-date-element | week-date-element
     * std-date-element  = yyyy ['-' MM ['-' dd]]
     * ord-date-element  = yyyy ['-' DDD]
     * week-date-element = xxxx '-W' ww ['-' e]
     * </pre>
     */
    public DateTimeParser dateElementParser() {
        if (dpe == null) {
            dpe = new DateTimeFormatterBuilder()
                .append(null, new DateTimeParser[] {
                    new DateTimeFormatterBuilder()
                    .append(yearElement())
                    .appendOptional
                    (new DateTimeFormatterBuilder()
                     .append(monthElement())
                     .appendOptional(dayOfMonthElement())
                     .toParser())
                    .toParser(),
                    new DateTimeFormatterBuilder()
                    .append(weekyearElement())
                    .append(weekElement())
                    .appendOptional(dayOfWeekElement())
                    .toParser(),
                    new DateTimeFormatterBuilder()
                    .append(yearElement())
                    .append(dayOfYearElement())
                    .toParser()
                })
                .toParser();
        }
        return dpe;
    }

    /**
     * Returns a generic ISO time parser. It accepts formats described by
     * the following syntax:
     * <pre>
     * time           = ['T'] time-element [offset]
     * time-element   = HH [minute-element] | [fraction]
     * minute-element = ':' mm [second-element] | [fraction]
     * second-element = ':' ss [fraction]
     * fraction       = ('.' | ',') digit+
     * offset         = 'Z' | (('+' | '-') HH [':' mm [':' ss [('.' | ',') SSS]]])
     * </pre>
     */
    public DateTimeParser timeParser() {
        if (tp == null) {
            tp = new DateTimeFormatterBuilder()
                .appendOptional
                (new DateTimeFormatterBuilder()
                 .appendLiteral('T')
                 .toParser())
                .append(timeElementParser())
                .appendOptional(offsetElement())
                .toParser();
        }
        return tp;
    }

    /**
     * Returns a generic ISO time parser. It accepts formats described by
     * the following syntax:
     * <pre>
     * time-element   = HH [minute-element] | [fraction]
     * minute-element = ':' mm [second-element] | [fraction]
     * second-element = ':' ss [fraction]
     * fraction       = ('.' | ',') digit+
     * </pre>
     */
    public DateTimeParser timeElementParser() {
        if (tpe == null) {
            // Decimal point can be either '.' or ','
            DateTimeParser decimalPoint = new DateTimeFormatterBuilder()
                .append(null, new DateTimeParser[] {
                    new DateTimeFormatterBuilder()
                    .appendLiteral('.')
                    .toParser(),
                    new DateTimeFormatterBuilder()
                    .appendLiteral(',')
                    .toParser()
                })
                .toParser();

            tpe = new DateTimeFormatterBuilder()
                // time-element
                .append(hourElement())
                .append
                (null, new DateTimeParser[] {
                    new DateTimeFormatterBuilder()
                    // minute-element
                    .append(minuteElement())
                    .append
                    (null, new DateTimeParser[] {
                        new DateTimeFormatterBuilder()
                        // second-element
                        .append(secondElement())
                        // second fraction
                        .appendOptional(new DateTimeFormatterBuilder()
                                        .append(decimalPoint)
                                        .appendFractionOfSecond(1, 9)
                                        .toParser())
                        .toParser(),
                        // minute fraction
                        new DateTimeFormatterBuilder()
                        .append(decimalPoint)
                        .appendFractionOfMinute(1, 9)
                        .toParser(),
                        null
                    })
                    .toParser(),
                    // hour fraction
                    new DateTimeFormatterBuilder()
                    .append(decimalPoint)
                    .appendFractionOfHour(1, 9)
                    .toParser(),
                    null
                })
                .toParser();
        }
        return tpe;
    }

    /**
     * Returns a generic ISO datetime parser. It accepts formats described by
     * the following syntax:
     * <pre>
     * datetime          = time | (date-element [time | ('T' offset)])
     * time              = 'T' time-element [offset]
     * date-element      = std-date-element | ord-date-element | week-date-element
     * std-date-element  = yyyy ['-' MM ['-' dd]]
     * ord-date-element  = yyyy ['-' DDD]
     * week-date-element = xxxx '-W' ww ['-' e]
     * time-element      = HH [minute-element] | [fraction]
     * minute-element    = ':' mm [second-element] | [fraction]
     * second-element    = ':' ss [fraction]
     * fraction          = ('.' | ',') digit+
     * offset            = 'Z' | (('+' | '-') HH [':' mm [':' ss [('.' | ',') SSS]]])
     * </pre>
     */
    public DateTimeParser dateTimeParser() {
        if (dtp == null) {
            // This is different from the general time parser in that the 'T'
            // is required.
            DateTimeParser time = new DateTimeFormatterBuilder()
                .appendLiteral('T')
                .append(timeElementParser())
                .appendOptional(offsetElement())
                .toParser();

            dtp = new DateTimeFormatterBuilder()
                .append(null, new DateTimeParser[] {
                    time,
                    new DateTimeFormatterBuilder()
                    .append(dateElementParser())
                    .append(null, new DateTimeParser[] {
                        time,
                        new DateTimeFormatterBuilder()
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

    //-----------------------------------------------------------------------
    /**
     * Returns a formatter for a full date as four digit year, two digit month
     * of year, and two digit day of month (yyyy-MM-dd).
     * 
     * @return a formatter for yyyy-MM-dd
     */
    public DateTimeFormatter date() {
        return yearMonthDay();
    }

    /**
     * Returns a formatter for a two digit hour of day, two digit minute of
     * hour, two digit second of minute, three digit fraction of second, and
     * time zone offset (HH:mm:ss.SSSZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for HH:mm:ss.SSSZ
     */
    public DateTimeFormatter time() {
        if (t == null) {
            t = new DateTimeFormatterBuilder()
                .append(hourMinuteSecondMillis())
                .append(offsetElement())
                .toFormatter();
        }
        return t;
    }

    /**
     * Returns a formatter for a two digit hour of day, two digit minute of
     * hour, two digit second of minute, and time zone offset (HH:mm:ssZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for HH:mm:ssZ
     */
    public DateTimeFormatter timeNoMillis() {
        if (tx == null) {
            tx = new DateTimeFormatterBuilder()
                .append(hourMinuteSecond())
                .append(offsetElement())
                .toFormatter();
        }
        return tx;
    }

    /**
     * Returns a formatter for a two digit hour of day, two digit minute of
     * hour, two digit second of minute, three digit fraction of second, and
     * time zone offset prefixed by 'T' ('T'HH:mm:ss.SSSZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for 'T'HH:mm:ss.SSSZ
     */
    public DateTimeFormatter tTime() {
        if (tt == null) {
            tt = new DateTimeFormatterBuilder()
                .append(literalTElement())
                .append(time())
                .toFormatter();
        }
        return tt;
    }

    /**
     * Returns a formatter for a two digit hour of day, two digit minute of
     * hour, two digit second of minute, and time zone offset prefixed
     * by 'T' ('T'HH:mm:ssZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for 'T'HH:mm:ssZ
     */
    public DateTimeFormatter tTimeNoMillis() {
        if (ttx == null) {
            ttx = new DateTimeFormatterBuilder()
                .append(literalTElement())
                .append(timeNoMillis())
                .toFormatter();
        }
        return ttx;
    }

    /**
     * Returns a formatter that combines a full date and time, separated by a 'T'
     * (yyyy-MM-dd'T'HH:mm:ss.SSSZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for yyyy-MM-dd'T'HH:mm:ss.SSSZ
     */
    public DateTimeFormatter dateTime() {
        if (dt == null) {
            dt = new DateTimeFormatterBuilder()
                .append(date())
                .append(tTime())
                .toFormatter();
        }
        return dt;
    }

    /**
     * Returns a formatter that combines a full date and time without millis,
     * separated by a 'T' (yyyy-MM-dd'T'HH:mm:ssZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for yyyy-MM-dd'T'HH:mm:ssZ
     */
    public DateTimeFormatter dateTimeNoMillis() {
        if (dtx == null) {
            dtx = new DateTimeFormatterBuilder()
                .append(date())
                .append(tTimeNoMillis())
                .toFormatter();
        }
        return dtx;
    }

    /**
     * Returns a formatter for a full date as four digit weekyear, two digit
     * week of weekyear, and one digit day of week (xxxx-'W'ww-e).
     * 
     * @return a formatter for xxxx-'W'ww-e
     */
    public DateTimeFormatter weekDate() {
        return weekyearWeekDay();
    }

    /**
     * Returns a formatter that combines a full weekyear date and time,
     * separated by a 'T' (xxxx-'W'ww-e'T'HH:mm:ss.SSSZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for xxxx-'W'ww-e'T'HH:mm:ss.SSSZ
     */
    public DateTimeFormatter weekDateTime() {
        if (wdt == null) {
            wdt = new DateTimeFormatterBuilder()
                .append(weekDate())
                .append(tTime())
                .toFormatter();
        }
        return wdt;
    }

    /**
     * Returns a formatter that combines a full weekyear date and time without millis,
     * separated by a 'T' (xxxx-'W'ww-e'T'HH:mm:ssZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for xxxx-'W'ww-e'T'HH:mm:ssZ
     */
    public DateTimeFormatter weekDateTimeNoMillis() {
        if (wdtx == null) {
            wdtx = new DateTimeFormatterBuilder()
                .append(weekDate())
                .append(tTimeNoMillis())
                .toFormatter();
        }
        return wdtx;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a basic formatter for a full date as four digit year, two digit
     * month of year, and two digit day of month (yyyyMMdd).
     * 
     * @return a formatter for yyyyMMdd
     */
    public DateTimeFormatter basicDate() {
        if (bd == null) {
            bd = new DateTimeFormatterBuilder()
                .appendYear(4, 4)
                .appendMonthOfYear(2)
                .appendDayOfMonth(2)
                .toFormatter();
        }
        return bd;
    }

    /**
     * Returns a basic formatter for a two digit hour of day, two digit minute
     * of hour, two digit second of minute, three digit millis, and time zone
     * offset (HHmmss.SSSZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for HHmmss.SSSZ
     */
    public DateTimeFormatter basicTime() {
        if (bt == null) {
            bt = new DateTimeFormatterBuilder()
                .appendHourOfDay(2)
                .appendMinuteOfHour(2)
                .appendSecondOfMinute(2)
                .appendLiteral('.')
                .appendMillisOfSecond(3)
                .appendTimeZoneOffset("Z", false, 2, 2)
                .toFormatter();
        }
        return bt;
    }

    /**
     * Returns a basic formatter for a two digit hour of day, two digit minute
     * of hour, two digit second of minute, and time zone offset (HHmmssZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for HHmmssZ
     */
    public DateTimeFormatter basicTimeNoMillis() {
        if (btx == null) {
            btx = new DateTimeFormatterBuilder()
                .appendHourOfDay(2)
                .appendMinuteOfHour(2)
                .appendSecondOfMinute(2)
                .appendTimeZoneOffset("Z", false, 2, 2)
                .toFormatter();
        }
        return btx;
    }

    /**
     * Returns a basic formatter for a two digit hour of day, two digit minute
     * of hour, two digit second of minute, three digit millis, and time zone
     * offset prefixed by 'T' ('T'HHmmss.SSSZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for 'T'HHmmss.SSSZ
     */
    public DateTimeFormatter basicTTime() {
        if (btt == null) {
            btt = new DateTimeFormatterBuilder()
                .append(literalTElement())
                .append(basicTime())
                .toFormatter();
        }
        return btt;
    }

    /**
     * Returns a basic formatter for a two digit hour of day, two digit minute
     * of hour, two digit second of minute, and time zone offset prefixed by 'T'
     * ('T'HHmmssZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for 'T'HHmmssZ
     */
    public DateTimeFormatter basicTTimeNoMillis() {
        if (bttx == null) {
            bttx = new DateTimeFormatterBuilder()
                .append(literalTElement())
                .append(basicTimeNoMillis())
                .toFormatter();
        }
        return bttx;
    }

    /**
     * Returns a basic formatter that combines a basic date and time, separated
     * by a 'T' (yyyyMMdd'T'HHmmss.SSSZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for yyyyMMdd'T'HHmmss.SSSZ
     */
    public DateTimeFormatter basicDateTime() {
        if (bdt == null) {
            bdt = new DateTimeFormatterBuilder()
                .append(basicDate())
                .append(basicTTime())
                .toFormatter();
        }
        return bdt;
    }

    /**
     * Returns a basic formatter that combines a basic date and time without millis,
     * separated by a 'T' (yyyyMMdd'T'HHmmssZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for yyyyMMdd'T'HHmmssZ
     */
    public DateTimeFormatter basicDateTimeNoMillis() {
        if (bdtx == null) {
            bdtx = new DateTimeFormatterBuilder()
                .append(basicDate())
                .append(basicTTimeNoMillis())
                .toFormatter();
        }
        return bdtx;
    }

    /**
     * Returns a basic formatter for a full date as four digit weekyear, two
     * digit week of weekyear, and one digit day of week (xxxx'W'wwe).
     * 
     * @return a formatter for xxxx'W'wwe
     */
    public DateTimeFormatter basicWeekDate() {
        if (bwd == null) {
            bwd = new DateTimeFormatterBuilder()
                .appendWeekyear(4, 4)
                .appendLiteral('W')
                .appendWeekOfWeekyear(2)
                .appendDayOfWeek(1)
                .toFormatter();
        }
        return bwd;
    }

    /**
     * Returns a basic formatter that combines a basic weekyear date and time,
     * separated by a 'T' (xxxx'W'wwe'T'HHmmss.SSSZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for xxxx'W'wwe'T'HHmmss.SSSZ
     */
    public DateTimeFormatter basicWeekDateTime() {
        if (bwdt == null) {
            bwdt = new DateTimeFormatterBuilder()
                .append(basicWeekDate())
                .append(basicTTime())
                .toFormatter();
        }
        return bwdt;
    }

    /**
     * Returns a basic formatter that combines a basic weekyear date and time
     * without millis, separated by a 'T' (xxxx'W'wwe'T'HHmmssZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for xxxx'W'wwe'T'HHmmssZ
     */
    public DateTimeFormatter basicWeekDateTimeNoMillis() {
        if (bwdtx == null) {
            bwdtx = new DateTimeFormatterBuilder()
                .append(basicWeekDate())
                .append(basicTTimeNoMillis())
                .toFormatter();
        }
        return bwdtx;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a formatter for a four digit year. (yyyy)
     * 
     * @return a formatter for yyyy
     */
    public DateTimeFormatter year() {
        return yearElement();
    }

    /**
     * Returns a formatter for a four digit year and two digit month of
     * year. (yyyy-MM)
     * 
     * @return a formatter for yyyy-MM
     */
    public DateTimeFormatter yearMonth() {
        if (ym == null) {
            ym = new DateTimeFormatterBuilder()
                .append(yearElement())
                .append(monthElement())
                .toFormatter();
        }
        return ym;
    }

    /**
     * Returns a formatter for a four digit year, two digit month of year, and
     * two digit day of month. (yyyy-MM-dd)
     * 
     * @return a formatter for yyyy-MM-dd
     */
    public DateTimeFormatter yearMonthDay() {
        if (ymd == null) {
            ymd = new DateTimeFormatterBuilder()
                .append(yearElement())
                .append(monthElement())
                .append(dayOfMonthElement())
                .toFormatter();
        }
        return ymd;
    }

    /**
     * Returns a formatter for a four digit weekyear. (xxxx)
     * 
     * @return a formatter for xxxx
     */
    public DateTimeFormatter weekyear() {
        return weekyearElement();
    }

    /**
     * Returns a formatter for a four digit weekyear and two digit week of
     * weekyear. (xxxx-'W'ww)
     * 
     * @return a formatter for xxxx-'W'ww
     */
    public DateTimeFormatter weekyearWeek() {
        if (ww == null) {
            ww = new DateTimeFormatterBuilder()
                .append(weekyearElement())
                .append(weekElement())
                .toFormatter();
        }
        return ww;
    }

    /**
     * Returns a formatter for a four digit weekyear, two digit week of
     * weekyear, and one digit day of week. (xxxx-'W'ww-e)
     * 
     * @return a formatter for xxxx-'W'ww-e
     */
    public DateTimeFormatter weekyearWeekDay() {
        if (wwd == null) {
            wwd = new DateTimeFormatterBuilder()
                .append(weekyearElement())
                .append(weekElement())
                .append(dayOfWeekElement())
                .toFormatter();
        }
        return wwd;
    }

    /**
     * Returns a formatter for a two digit hour of day. (HH)
     * 
     * @return a formatter for HH
     */
    public DateTimeFormatter hour() {
        return hourElement();
    }

    /**
     * Returns a formatter for a two digit hour of day and two digit minute of
     * hour. (HH:mm)
     * 
     * @return a formatter for HH:mm
     */
    public DateTimeFormatter hourMinute() {
        if (hm == null) {
            hm = new DateTimeFormatterBuilder()
                .append(hourElement())
                .append(minuteElement())
                .toFormatter();
        }
        return hm;
    }

    /**
     * Returns a formatter for a two digit hour of day, two digit minute of
     * hour, and two digit second of minute. (HH:mm:ss)
     * 
     * @return a formatter for HH:mm:ss
     */
    public DateTimeFormatter hourMinuteSecond() {
        if (hms == null) {
            hms = new DateTimeFormatterBuilder()
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
     * 
     * @return a formatter for HH:mm:ss.SSS
     */
    public DateTimeFormatter hourMinuteSecondMillis() {
        if (hmsl == null) {
            hmsl = new DateTimeFormatterBuilder()
                .append(hourElement())
                .append(minuteElement())
                .append(secondElement())
                .append(millisElement())
                .toFormatter();
        }
        return hmsl;
    }

    /**
     * Returns a formatter for a two digit hour of day, two digit minute of
     * hour, two digit second of minute, and three digit fraction of
     * second. (HH:mm:ss.SSS)
     * 
     * @return a formatter for HH:mm:ss.SSS
     */
    public DateTimeFormatter hourMinuteSecondFraction() {
        if (hmsf == null) {
            hmsf = new DateTimeFormatterBuilder()
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
     * day. (yyyy-MM-dd'T'HH)
     * 
     * @return a formatter for yyyy-MM-dd'T'HH
     */
    public DateTimeFormatter dateHour() {
        if (dh == null) {
            dh = new DateTimeFormatterBuilder()
                .append(date())
                .append(literalTElement())
                .append(hour())
                .toFormatter();
        }
        return dh;
    }

    /**
     * Returns a formatter that combines a full date, two digit hour of day,
     * and two digit minute of hour. (yyyy-MM-dd'T'HH:mm)
     * 
     * @return a formatter for yyyy-MM-dd'T'HH:mm
     */
    public DateTimeFormatter dateHourMinute() {
        if (dhm == null) {
            dhm = new DateTimeFormatterBuilder()
                .append(date())
                .append(literalTElement())
                .append(hourMinute())
                .toFormatter();
        }
        return dhm;
    }

    /**
     * Returns a formatter that combines a full date, two digit hour of day,
     * two digit minute of hour, and two digit second of
     * minute. (yyyy-MM-dd'T'HH:mm:ss)
     * 
     * @return a formatter for yyyy-MM-dd'T'HH:mm:ss
     */
    public DateTimeFormatter dateHourMinuteSecond() {
        if (dhms == null) {
            dhms = new DateTimeFormatterBuilder()
                .append(date())
                .append(literalTElement())
                .append(hourMinuteSecond())
                .toFormatter();
        }
        return dhms;
    }

    /**
     * Returns a formatter that combines a full date, two digit hour of day,
     * two digit minute of hour, two digit second of minute, and three digit
     * fraction of second. (yyyy-MM-dd'T'HH:mm:ss.SSS)
     * 
     * @return a formatter for yyyy-MM-dd'T'HH:mm:ss.SSS
     */
    public DateTimeFormatter dateHourMinuteSecondMillis() {
        if (dhmsl == null) {
            dhmsl = new DateTimeFormatterBuilder()
                .append(date())
                .append(literalTElement())
                .append(hourMinuteSecondMillis())
                .toFormatter();
        }
        return dhmsl;
    }

    /**
     * Returns a formatter that combines a full date, two digit hour of day,
     * two digit minute of hour, two digit second of minute, and three digit
     * fraction of second. (yyyy-MM-dd'T'HH:mm:ss.SSS)
     * 
     * @return a formatter for yyyy-MM-dd'T'HH:mm:ss.SSS
     */
    public DateTimeFormatter dateHourMinuteSecondFraction() {
        if (dhmsf == null) {
            dhmsf = new DateTimeFormatterBuilder()
                .append(date())
                .append(literalTElement())
                .append(hourMinuteSecondFraction())
                .toFormatter();
        }
        return dhmsf;
    }

    //-----------------------------------------------------------------------
    private DateTimeFormatter yearElement() {
        if (ye == null) {
            ye = new DateTimeFormatterBuilder()
                .appendYear(4, 9)
                .toFormatter();
        }
        return ye;
    }

    private DateTimeFormatter monthElement() {
        if (mye == null) {
            mye = new DateTimeFormatterBuilder()
                .appendLiteral('-')
                .appendMonthOfYear(2)
                .toFormatter();
        }
        return mye;
    }

    private DateTimeFormatter dayOfMonthElement() {
        if (dme == null) {
            dme = new DateTimeFormatterBuilder()
                .appendLiteral('-')
                .appendDayOfMonth(2)
                .toFormatter();
        }
        return dme;
    }

    private DateTimeFormatter weekyearElement() {
        if (we == null) {
            we = new DateTimeFormatterBuilder()
                .appendWeekyear(4, 9)
                .toFormatter();
        }
        return we;
    }

    private DateTimeFormatter weekElement() {
        if (wwe == null) {
            wwe = new DateTimeFormatterBuilder()
                .appendLiteral("-W")
                .appendWeekOfWeekyear(2)
                .toFormatter();
        }
        return wwe;
    }

    private DateTimeFormatter dayOfWeekElement() {
        if (dwe == null) {
            dwe = new DateTimeFormatterBuilder()
                .appendLiteral('-')
                .appendDayOfWeek(1)
                .toFormatter();
        }
        return dwe;
    }

    private DateTimeFormatter dayOfYearElement() {
        if (dye == null) {
            dye = new DateTimeFormatterBuilder()
                .appendLiteral('-')
                .appendDayOfYear(3)
                .toFormatter();
        }
        return dye;
    }
    
    private DateTimeFormatter literalTElement() {
        if (lte == null) {
            lte = new DateTimeFormatterBuilder()
                .appendLiteral('T')
                .toFormatter();
        }
        return lte;
    }

    private DateTimeFormatter hourElement() {
        if (hde == null) {
            hde = new DateTimeFormatterBuilder()
                .appendHourOfDay(2)
                .toFormatter();
        }
        return hde;
    }

    private DateTimeFormatter minuteElement() {
        if (mhe == null) {
            mhe = new DateTimeFormatterBuilder()
                .appendLiteral(':')
                .appendMinuteOfHour(2)
                .toFormatter();
        }
        return mhe;
    }

    private DateTimeFormatter secondElement() {
        if (sme == null) {
            sme = new DateTimeFormatterBuilder()
                .appendLiteral(':')
                .appendSecondOfMinute(2)
                .toFormatter();
        }
        return sme;
    }

    private DateTimeFormatter millisElement() {
        if (lse == null) {
            lse = new DateTimeFormatterBuilder()
                .appendLiteral('.')
                .appendMillisOfSecond(3)
                .toFormatter();
        }
        return lse;
    }

    private DateTimeFormatter fractionElement() {
        if (fse == null) {
            fse = new DateTimeFormatterBuilder()
                .appendLiteral('.')
                // Support parsing up to nanosecond precision even though
                // those extra digits will be dropped.
                .appendFractionOfSecond(3, 9)
                .toFormatter();
        }
        return fse;
    }

    private DateTimeFormatter offsetElement() {
        if (ze == null) {
            ze = new DateTimeFormatterBuilder()
                .appendTimeZoneOffset("Z", true, 2, 4)
                .toFormatter();
        }
        return ze;
    }
    
}
