/*
 *  Copyright 2001-2006,2008 Stephen Colebourne
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;

/**
 * Factory that creates instances of DateTimeFormatter for the ISO8601 standard.
 * <p>
 * Datetime formatting is performed by the {@link DateTimeFormatter} class.
 * Three classes provide factory methods to create formatters, and this is one.
 * The others are {@link DateTimeFormat} and {@link DateTimeFormatterBuilder}.
 * <p>
 * ISO8601 is the international standard for data interchange. It defines a
 * framework, rather than an absolute standard. As a result this provider has a
 * number of methods that represent common uses of the framework. The most common
 * formats are {@link #date() date}, {@link #time() time}, and {@link #dateTime() dateTime}.
 * <p>
 * For example, to format a date time in ISO format:
 * <pre>
 * DateTime dt = new DateTime();
 * DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
 * String str = fmt.print(dt);
 * </pre>
 * <p>
 * It is important to understand that these formatters are not linked to
 * the <code>ISOChronology</code>. These formatters may be used with any
 * chronology, however there may be certain side effects with more unusual
 * chronologies. For example, the ISO formatters rely on dayOfWeek being
 * single digit, dayOfMonth being two digit and dayOfYear being three digit.
 * A chronology with a ten day week would thus cause issues. However, in
 * general, it is safe to use these formatters with other chronologies.
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

    //-----------------------------------------------------------------------
    private static DateTimeFormatter
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

        od,  // ordinal date (same as yd)
        odt, // ordinal date time
        odtx, // ordinal date time no millis

        bd,  // basic date
        bt,  // basic time
        btx,  // basic time no millis
        btt, // basic Ttime
        bttx, // basic Ttime no millis
        bdt, // basic date time
        bdtx, // basic date time no millis

        bod,  // basic ordinal date
        bodt, // basic ordinal date time
        bodtx, // basic ordinal date time no millis

        bwd,  // basic week date
        bwdt, // basic week date time
        bwdtx, // basic week date time no millis

        dpe, // date parser element
        tpe, // time parser element
        dp,  // date parser
        ldp, // local date parser
        tp,  // time parser
        ltp, // local time parser
        dtp, // date time parser
        dotp, // date optional time parser
        ldotp; // local date optional time parser

    /**
     * Constructor.
     *
     * @since 1.1 (previously private)
     */
    protected ISODateTimeFormat() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a formatter that outputs only those fields specified.
     * <p>
     * This method examines the fields provided and returns an ISO-style
     * formatter that best fits. This can be useful for outputting
     * less-common ISO styles, such as YearMonth (YYYY-MM) or MonthDay (--MM-DD).
     * <p>
     * The list provided may have overlapping fields, such as dayOfWeek and
     * dayOfMonth. In this case, the style is chosen based on the following
     * list, thus in the example, the calendar style is chosen as dayOfMonth
     * is higher in priority than dayOfWeek:
     * <ul>
     * <li>monthOfYear - calendar date style
     * <li>dayOfYear - ordinal date style
     * <li>weekOfWeekYear - week date style
     * <li>dayOfMonth - calendar date style
     * <li>dayOfWeek - week date style
     * <li>year
     * <li>weekyear
     * </ul>
     * The supported formats are:
     * <pre>
     * Extended      Basic       Fields
     * 2005-03-25    20050325    year/monthOfYear/dayOfMonth
     * 2005-03       2005-03     year/monthOfYear
     * 2005--25      2005--25    year/dayOfMonth *
     * 2005          2005        year
     * --03-25       --0325      monthOfYear/dayOfMonth
     * --03          --03        monthOfYear
     * ---03         ---03       dayOfMonth
     * 2005-084      2005084     year/dayOfYear
     * -084          -084        dayOfYear
     * 2005-W12-5    2005W125    weekyear/weekOfWeekyear/dayOfWeek
     * 2005-W-5      2005W-5     weekyear/dayOfWeek *
     * 2005-W12      2005W12     weekyear/weekOfWeekyear
     * -W12-5        -W125       weekOfWeekyear/dayOfWeek
     * -W12          -W12        weekOfWeekyear
     * -W-5          -W-5        dayOfWeek
     * 10:20:30.040  102030.040  hour/minute/second/milli
     * 10:20:30      102030      hour/minute/second
     * 10:20         1020        hour/minute
     * 10            10          hour
     * -20:30.040    -2030.040   minute/second/milli
     * -20:30        -2030       minute/second
     * -20           -20         minute
     * --30.040      --30.040    second/milli
     * --30          --30        second
     * ---.040       ---.040     milli *
     * 10-30.040     10-30.040   hour/second/milli *
     * 10:20-.040    1020-.040   hour/minute/milli *
     * 10-30         10-30       hour/second *
     * 10--.040      10--.040    hour/milli *
     * -20-.040      -20-.040    minute/milli *
     *   plus datetime formats like {date}T{time}
     * </pre>
     * * indiates that this is not an official ISO format and can be excluded
     * by passing in <code>strictISO</code> as <code>true</code>.
     * <p>
     * This method can side effect the input collection of fields.
     * If the input collection is modifiable, then each field that was added to
     * the formatter will be removed from the collection, including any duplicates.
     * If the input collection is unmodifiable then no side effect occurs.
     * <p>
     * This side effect processing is useful if you need to know whether all
     * the fields were converted into the formatter or not. To achieve this,
     * pass in a modifiable list, and check that it is empty on exit.
     *
     * @param fields  the fields to get a formatter for, not null,
     *  updated by the method call unless unmodifiable,
     *  removing those fields built in the formatter
     * @param extended  true to use the extended format (with separators)
     * @param strictISO  true to stick exactly to ISO8601, false to include additional formats
     * @return a suitable formatter
     * @throws IllegalArgumentException if there is no format for the fields
     * @since 1.1
     */
    public static DateTimeFormatter forFields(
        Collection fields,
        boolean extended,
        boolean strictISO) {
        
        if (fields == null || fields.size() == 0) {
            throw new IllegalArgumentException("The fields must not be null or empty");
        }
        Set workingFields = new HashSet(fields);
        int inputSize = workingFields.size();
        boolean reducedPrec = false;
        DateTimeFormatterBuilder bld = new DateTimeFormatterBuilder();
        // date
        if (workingFields.contains(DateTimeFieldType.monthOfYear())) {
            reducedPrec = dateByMonth(bld, workingFields, extended, strictISO);
        } else if (workingFields.contains(DateTimeFieldType.dayOfYear())) {
            reducedPrec = dateByOrdinal(bld, workingFields, extended, strictISO);
        } else if (workingFields.contains(DateTimeFieldType.weekOfWeekyear())) {
            reducedPrec = dateByWeek(bld, workingFields, extended, strictISO);
        } else if (workingFields.contains(DateTimeFieldType.dayOfMonth())) {
            reducedPrec = dateByMonth(bld, workingFields, extended, strictISO);
        } else if (workingFields.contains(DateTimeFieldType.dayOfWeek())) {
            reducedPrec = dateByWeek(bld, workingFields, extended, strictISO);
        } else if (workingFields.remove(DateTimeFieldType.year())) {
            bld.append(yearElement());
            reducedPrec = true;
        } else if (workingFields.remove(DateTimeFieldType.weekyear())) {
            bld.append(weekyearElement());
            reducedPrec = true;
        }
        boolean datePresent = (workingFields.size() < inputSize);
        
        // time
        time(bld, workingFields, extended, strictISO, reducedPrec, datePresent);
        
        // result
        if (bld.canBuildFormatter() == false) {
            throw new IllegalArgumentException("No valid format for fields: " + fields);
        }
        
        // side effect the input collection to indicate the processed fields
        // handling unmodifiable collections with no side effect
        try {
            fields.retainAll(workingFields);
        } catch (UnsupportedOperationException ex) {
            // ignore, so we can handle unmodifiable collections
        }
        return bld.toFormatter();
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a date using the calendar date format.
     * Specification reference: 5.2.1.
     *
     * @param bld  the builder
     * @param fields  the fields
     * @param extended  true to use extended format
     * @param strictISO  true to only allow ISO formats
     * @return true if reduced precision
     * @since 1.1
     */
    private static boolean dateByMonth(
        DateTimeFormatterBuilder bld,
        Collection fields,
        boolean extended,
        boolean strictISO) {
        
        boolean reducedPrec = false;
        if (fields.remove(DateTimeFieldType.year())) {
            bld.append(yearElement());
            if (fields.remove(DateTimeFieldType.monthOfYear())) {
                if (fields.remove(DateTimeFieldType.dayOfMonth())) {
                    // YYYY-MM-DD/YYYYMMDD
                    appendSeparator(bld, extended);
                    bld.appendMonthOfYear(2);
                    appendSeparator(bld, extended);
                    bld.appendDayOfMonth(2);
                } else {
                    // YYYY-MM/YYYY-MM
                    bld.appendLiteral('-');
                    bld.appendMonthOfYear(2);
                    reducedPrec = true;
                }
            } else {
                if (fields.remove(DateTimeFieldType.dayOfMonth())) {
                    // YYYY--DD/YYYY--DD (non-iso)
                    checkNotStrictISO(fields, strictISO);
                    bld.appendLiteral('-');
                    bld.appendLiteral('-');
                    bld.appendDayOfMonth(2);
                } else {
                    // YYYY/YYYY
                    reducedPrec = true;
                }
            }
            
        } else if (fields.remove(DateTimeFieldType.monthOfYear())) {
            bld.appendLiteral('-');
            bld.appendLiteral('-');
            bld.appendMonthOfYear(2);
            if (fields.remove(DateTimeFieldType.dayOfMonth())) {
                // --MM-DD/--MMDD
                appendSeparator(bld, extended);
                bld.appendDayOfMonth(2);
            } else {
                // --MM/--MM
                reducedPrec = true;
            }
        } else if (fields.remove(DateTimeFieldType.dayOfMonth())) {
            // ---DD/---DD
            bld.appendLiteral('-');
            bld.appendLiteral('-');
            bld.appendLiteral('-');
            bld.appendDayOfMonth(2);
        }
        return reducedPrec;
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a date using the ordinal date format.
     * Specification reference: 5.2.2.
     *
     * @param bld  the builder
     * @param fields  the fields
     * @param extended  true to use extended format
     * @param strictISO  true to only allow ISO formats
     * @since 1.1
     */
    private static boolean dateByOrdinal(
        DateTimeFormatterBuilder bld,
        Collection fields,
        boolean extended,
        boolean strictISO) {
        
        boolean reducedPrec = false;
        if (fields.remove(DateTimeFieldType.year())) {
            bld.append(yearElement());
            if (fields.remove(DateTimeFieldType.dayOfYear())) {
                // YYYY-DDD/YYYYDDD
                appendSeparator(bld, extended);
                bld.appendDayOfYear(3);
            } else {
                // YYYY/YYYY
                reducedPrec = true;
            }
            
        } else if (fields.remove(DateTimeFieldType.dayOfYear())) {
            // -DDD/-DDD
            bld.appendLiteral('-');
            bld.appendDayOfYear(3);
        }
        return reducedPrec;
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a date using the calendar date format.
     * Specification reference: 5.2.3.
     *
     * @param bld  the builder
     * @param fields  the fields
     * @param extended  true to use extended format
     * @param strictISO  true to only allow ISO formats
     * @since 1.1
     */
    private static boolean dateByWeek(
        DateTimeFormatterBuilder bld,
        Collection fields,
        boolean extended,
        boolean strictISO) {
        
        boolean reducedPrec = false;
        if (fields.remove(DateTimeFieldType.weekyear())) {
            bld.append(weekyearElement());
            if (fields.remove(DateTimeFieldType.weekOfWeekyear())) {
                appendSeparator(bld, extended);
                bld.appendLiteral('W');
                bld.appendWeekOfWeekyear(2);
                if (fields.remove(DateTimeFieldType.dayOfWeek())) {
                    // YYYY-WWW-D/YYYYWWWD
                    appendSeparator(bld, extended);
                    bld.appendDayOfWeek(1);
                } else {
                    // YYYY-WWW/YYYY-WWW
                    reducedPrec = true;
                }
            } else {
                if (fields.remove(DateTimeFieldType.dayOfWeek())) {
                    // YYYY-W-D/YYYYW-D (non-iso)
                    checkNotStrictISO(fields, strictISO);
                    appendSeparator(bld, extended);
                    bld.appendLiteral('W');
                    bld.appendLiteral('-');
                    bld.appendDayOfWeek(1);
                } else {
                    // YYYY/YYYY
                    reducedPrec = true;
                }
            }
            
        } else if (fields.remove(DateTimeFieldType.weekOfWeekyear())) {
            bld.appendLiteral('-');
            bld.appendLiteral('W');
            bld.appendWeekOfWeekyear(2);
            if (fields.remove(DateTimeFieldType.dayOfWeek())) {
                // -WWW-D/-WWWD
                appendSeparator(bld, extended);
                bld.appendDayOfWeek(1);
            } else {
                // -WWW/-WWW
                reducedPrec = true;
            }
        } else if (fields.remove(DateTimeFieldType.dayOfWeek())) {
            // -W-D/-W-D
            bld.appendLiteral('-');
            bld.appendLiteral('W');
            bld.appendLiteral('-');
            bld.appendDayOfWeek(1);
        }
        return reducedPrec;
    }

    //-----------------------------------------------------------------------
    /**
     * Adds the time fields to the builder.
     * Specification reference: 5.3.1.
     * 
     * @param bld  the builder
     * @param fields  the fields
     * @param extended  whether to use the extended format
     * @param strictISO  whether to be strict
     * @param reducedPrec  whether the date was reduced precision
     * @param datePresent  whether there was a date
     * @since 1.1
     */
    private static void time(
        DateTimeFormatterBuilder bld,
        Collection fields,
        boolean extended,
        boolean strictISO,
        boolean reducedPrec,
        boolean datePresent) {
        
        boolean hour = fields.remove(DateTimeFieldType.hourOfDay());
        boolean minute = fields.remove(DateTimeFieldType.minuteOfHour());
        boolean second = fields.remove(DateTimeFieldType.secondOfMinute());
        boolean milli = fields.remove(DateTimeFieldType.millisOfSecond());
        if (!hour && !minute && !second && !milli) {
            return;
        }
        if (hour || minute || second || milli) {
            if (strictISO && reducedPrec) {
                throw new IllegalArgumentException("No valid ISO8601 format for fields because Date was reduced precision: " + fields);
            }
            if (datePresent) {
                bld.appendLiteral('T');
            }
        }
        if (hour && minute && second || (hour && !second && !milli)) {
            // OK - HMSm/HMS/HM/H - valid in combination with date
        } else {
            if (strictISO && datePresent) {
                throw new IllegalArgumentException("No valid ISO8601 format for fields because Time was truncated: " + fields);
            }
            if (!hour && (minute && second || (minute && !milli) || second)) {
                // OK - MSm/MS/M/Sm/S - valid ISO formats
            } else {
                if (strictISO) {
                    throw new IllegalArgumentException("No valid ISO8601 format for fields: " + fields);
                }
            }
        }
        if (hour) {
            bld.appendHourOfDay(2);
        } else if (minute || second || milli) {
            bld.appendLiteral('-');
        }
        if (extended && hour && minute) {
            bld.appendLiteral(':');
        }
        if (minute) {
            bld.appendMinuteOfHour(2);
        } else if (second || milli) {
            bld.appendLiteral('-');
        }
        if (extended && minute && second) {
            bld.appendLiteral(':');
        }
        if (second) {
            bld.appendSecondOfMinute(2);
        } else if (milli) {
            bld.appendLiteral('-');
        }
        if (milli) {
            bld.appendLiteral('.');
            bld.appendMillisOfSecond(3);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Checks that the iso only flag is not set, throwing an exception if it is.
     * 
     * @param fields  the fields
     * @param strictISO  true if only ISO formats allowed
     * @since 1.1
     */
    private static void checkNotStrictISO(Collection fields, boolean strictISO) {
        if (strictISO) {
            throw new IllegalArgumentException("No valid ISO8601 format for fields: " + fields);
        }
    }

    /**
     * Appends the separator if necessary.
     *
     * @param bld  the builder
     * @param extended  whether to append the separator
     * @param sep  the separator
     * @since 1.1
     */
    private static void appendSeparator(DateTimeFormatterBuilder bld, boolean extended) {
        if (extended) {
            bld.appendLiteral('-');
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a generic ISO date parser for parsing dates with a possible zone.
     * It accepts formats described by the following syntax:
     * <pre>
     * date              = date-element ['T' offset]
     * date-element      = std-date-element | ord-date-element | week-date-element
     * std-date-element  = yyyy ['-' MM ['-' dd]]
     * ord-date-element  = yyyy ['-' DDD]
     * week-date-element = xxxx '-W' ww ['-' e]
     * offset            = 'Z' | (('+' | '-') HH [':' mm [':' ss [('.' | ',') SSS]]])
     * </pre>
     */
    public static DateTimeFormatter dateParser() {
        if (dp == null) {
            DateTimeParser tOffset = new DateTimeFormatterBuilder()
                .appendLiteral('T')
                .append(offsetElement()).toParser();
            dp = new DateTimeFormatterBuilder()
                .append(dateElementParser())
                .appendOptional(tOffset)
                .toFormatter();
        }
        return dp;
    }

    /**
     * Returns a generic ISO date parser for parsing local dates.
     * This parser is initialised with the local (UTC) time zone.
     * <p>
     * It accepts formats described by the following syntax:
     * <pre>
     * date-element      = std-date-element | ord-date-element | week-date-element
     * std-date-element  = yyyy ['-' MM ['-' dd]]
     * ord-date-element  = yyyy ['-' DDD]
     * week-date-element = xxxx '-W' ww ['-' e]
     * </pre>
     * @since 1.3
     */
    public static DateTimeFormatter localDateParser() {
        if (ldp == null) {
            ldp = dateElementParser().withZone(DateTimeZone.UTC);
        }
        return ldp;
    }

    /**
     * Returns a generic ISO date parser for parsing dates.
     * It accepts formats described by the following syntax:
     * <pre>
     * date-element      = std-date-element | ord-date-element | week-date-element
     * std-date-element  = yyyy ['-' MM ['-' dd]]
     * ord-date-element  = yyyy ['-' DDD]
     * week-date-element = xxxx '-W' ww ['-' e]
     * </pre>
     */
    public static DateTimeFormatter dateElementParser() {
        if (dpe == null) {
            dpe = new DateTimeFormatterBuilder()
                .append(null, new DateTimeParser[] {
                    new DateTimeFormatterBuilder()
                    .append(yearElement())
                    .appendOptional
                    (new DateTimeFormatterBuilder()
                     .append(monthElement())
                     .appendOptional(dayOfMonthElement().getParser())
                     .toParser())
                    .toParser(),
                    new DateTimeFormatterBuilder()
                    .append(weekyearElement())
                    .append(weekElement())
                    .appendOptional(dayOfWeekElement().getParser())
                    .toParser(),
                    new DateTimeFormatterBuilder()
                    .append(yearElement())
                    .append(dayOfYearElement())
                    .toParser()
                })
                .toFormatter();
        }
        return dpe;
    }

    /**
     * Returns a generic ISO time parser for parsing times with a possible zone.
     * It accepts formats described by the following syntax:
     * <pre>
     * time           = ['T'] time-element [offset]
     * time-element   = HH [minute-element] | [fraction]
     * minute-element = ':' mm [second-element] | [fraction]
     * second-element = ':' ss [fraction]
     * fraction       = ('.' | ',') digit+
     * offset         = 'Z' | (('+' | '-') HH [':' mm [':' ss [('.' | ',') SSS]]])
     * </pre>
     */
    public static DateTimeFormatter timeParser() {
        if (tp == null) {
            tp = new DateTimeFormatterBuilder()
                .appendOptional(literalTElement().getParser())
                .append(timeElementParser())
                .appendOptional(offsetElement().getParser())
                .toFormatter();
        }
        return tp;
    }

    /**
     * Returns a generic ISO time parser for parsing local times.
     * This parser is initialised with the local (UTC) time zone.
     * <p>
     * It accepts formats described by the following syntax:
     * <pre>
     * time           = ['T'] time-element
     * time-element   = HH [minute-element] | [fraction]
     * minute-element = ':' mm [second-element] | [fraction]
     * second-element = ':' ss [fraction]
     * fraction       = ('.' | ',') digit+
     * </pre>
     * @since 1.3
     */
    public static DateTimeFormatter localTimeParser() {
        if (ltp == null) {
            ltp = new DateTimeFormatterBuilder()
                .appendOptional(literalTElement().getParser())
                .append(timeElementParser())
                .toFormatter().withZone(DateTimeZone.UTC);
        }
        return ltp;
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
    public static DateTimeFormatter timeElementParser() {
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
                .toFormatter();
        }
        return tpe;
    }

    /**
     * Returns a generic ISO datetime parser which parses either a date or
     * a time or both. It accepts formats described by the following syntax:
     * <pre>
     * datetime          = time | date-opt-time
     * time              = 'T' time-element [offset]
     * date-opt-time     = date-element ['T' [time-element] [offset]]
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
    public static DateTimeFormatter dateTimeParser() {
        if (dtp == null) {
            // This is different from the general time parser in that the 'T'
            // is required.
            DateTimeParser time = new DateTimeFormatterBuilder()
                .appendLiteral('T')
                .append(timeElementParser())
                .appendOptional(offsetElement().getParser())
                .toParser();
            dtp = new DateTimeFormatterBuilder()
                .append(null, new DateTimeParser[] {time, dateOptionalTimeParser().getParser()})
                .toFormatter();
        }
        return dtp;
    }

    /**
     * Returns a generic ISO datetime parser where the date is mandatory and
     * the time is optional. This parser can parse zoned datetimes.
     * It accepts formats described by the following syntax:
     * <pre>
     * date-opt-time     = date-element ['T' [time-element] [offset]]
     * date-element      = std-date-element | ord-date-element | week-date-element
     * std-date-element  = yyyy ['-' MM ['-' dd]]
     * ord-date-element  = yyyy ['-' DDD]
     * week-date-element = xxxx '-W' ww ['-' e]
     * time-element      = HH [minute-element] | [fraction]
     * minute-element    = ':' mm [second-element] | [fraction]
     * second-element    = ':' ss [fraction]
     * fraction          = ('.' | ',') digit+
     * </pre>
     * @since 1.3
     */
    public static DateTimeFormatter dateOptionalTimeParser() {
        if (dotp == null) {
            DateTimeParser timeOrOffset = new DateTimeFormatterBuilder()
                .appendLiteral('T')
                .appendOptional(timeElementParser().getParser())
                .appendOptional(offsetElement().getParser())
                .toParser();
            dotp = new DateTimeFormatterBuilder()
                .append(dateElementParser())
                .appendOptional(timeOrOffset)
                .toFormatter();
        }
        return dotp;
    }

    /**
     * Returns a generic ISO datetime parser where the date is mandatory and
     * the time is optional. This parser only parses local datetimes.
     * This parser is initialised with the local (UTC) time zone.
     * <p>
     * It accepts formats described by the following syntax:
     * <pre>
     * datetime          = date-element ['T' time-element]
     * date-element      = std-date-element | ord-date-element | week-date-element
     * std-date-element  = yyyy ['-' MM ['-' dd]]
     * ord-date-element  = yyyy ['-' DDD]
     * week-date-element = xxxx '-W' ww ['-' e]
     * time-element      = HH [minute-element] | [fraction]
     * minute-element    = ':' mm [second-element] | [fraction]
     * second-element    = ':' ss [fraction]
     * fraction          = ('.' | ',') digit+
     * </pre>
     * @since 1.3
     */
    public static DateTimeFormatter localDateOptionalTimeParser() {
        if (ldotp == null) {
            DateTimeParser time = new DateTimeFormatterBuilder()
                .appendLiteral('T')
                .append(timeElementParser())
                .toParser();
            ldotp = new DateTimeFormatterBuilder()
                .append(dateElementParser())
                .appendOptional(time)
                .toFormatter().withZone(DateTimeZone.UTC);
        }
        return ldotp;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a formatter for a full date as four digit year, two digit month
     * of year, and two digit day of month (yyyy-MM-dd).
     * 
     * @return a formatter for yyyy-MM-dd
     */
    public static DateTimeFormatter date() {
        return yearMonthDay();
    }

    /**
     * Returns a formatter for a two digit hour of day, two digit minute of
     * hour, two digit second of minute, three digit fraction of second, and
     * time zone offset (HH:mm:ss.SSSZZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for HH:mm:ss.SSSZZ
     */
    public static DateTimeFormatter time() {
        if (t == null) {
            t = new DateTimeFormatterBuilder()
                .append(hourMinuteSecondFraction())
                .append(offsetElement())
                .toFormatter();
        }
        return t;
    }

    /**
     * Returns a formatter for a two digit hour of day, two digit minute of
     * hour, two digit second of minute, and time zone offset (HH:mm:ssZZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for HH:mm:ssZZ
     */
    public static DateTimeFormatter timeNoMillis() {
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
     * time zone offset prefixed by 'T' ('T'HH:mm:ss.SSSZZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for 'T'HH:mm:ss.SSSZZ
     */
    public static DateTimeFormatter tTime() {
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
     * by 'T' ('T'HH:mm:ssZZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for 'T'HH:mm:ssZZ
     */
    public static DateTimeFormatter tTimeNoMillis() {
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
     * (yyyy-MM-dd'T'HH:mm:ss.SSSZZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for yyyy-MM-dd'T'HH:mm:ss.SSSZZ
     */
    public static DateTimeFormatter dateTime() {
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
     * separated by a 'T' (yyyy-MM-dd'T'HH:mm:ssZZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for yyyy-MM-dd'T'HH:mm:ssZZ
     */
    public static DateTimeFormatter dateTimeNoMillis() {
        if (dtx == null) {
            dtx = new DateTimeFormatterBuilder()
                .append(date())
                .append(tTimeNoMillis())
                .toFormatter();
        }
        return dtx;
    }

    /**
     * Returns a formatter for a full ordinal date, using a four
     * digit year and three digit dayOfYear (yyyy-DDD).
     * 
     * @return a formatter for yyyy-DDD
     * @since 1.1
     */
    public static DateTimeFormatter ordinalDate() {
        if (od == null) {
            od = new DateTimeFormatterBuilder()
                .append(yearElement())
                .append(dayOfYearElement())
                .toFormatter();
        }
        return od;
    }

    /**
     * Returns a formatter for a full ordinal date and time, using a four
     * digit year and three digit dayOfYear (yyyy-DDD'T'HH:mm:ss.SSSZZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for yyyy-DDD'T'HH:mm:ss.SSSZZ
     * @since 1.1
     */
    public static DateTimeFormatter ordinalDateTime() {
        if (odt == null) {
            odt = new DateTimeFormatterBuilder()
                .append(ordinalDate())
                .append(tTime())
                .toFormatter();
        }
        return odt;
    }

    /**
     * Returns a formatter for a full ordinal date and time without millis,
     * using a four digit year and three digit dayOfYear (yyyy-DDD'T'HH:mm:ssZZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for yyyy-DDD'T'HH:mm:ssZZ
     * @since 1.1
     */
    public static DateTimeFormatter ordinalDateTimeNoMillis() {
        if (odtx == null) {
            odtx = new DateTimeFormatterBuilder()
                .append(ordinalDate())
                .append(tTimeNoMillis())
                .toFormatter();
        }
        return odtx;
    }

    /**
     * Returns a formatter for a full date as four digit weekyear, two digit
     * week of weekyear, and one digit day of week (xxxx-'W'ww-e).
     * 
     * @return a formatter for xxxx-'W'ww-e
     */
    public static DateTimeFormatter weekDate() {
        return weekyearWeekDay();
    }

    /**
     * Returns a formatter that combines a full weekyear date and time,
     * separated by a 'T' (xxxx-'W'ww-e'T'HH:mm:ss.SSSZZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for xxxx-'W'ww-e'T'HH:mm:ss.SSSZZ
     */
    public static DateTimeFormatter weekDateTime() {
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
     * separated by a 'T' (xxxx-'W'ww-e'T'HH:mm:ssZZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HH:mm' for non-zero.
     * 
     * @return a formatter for xxxx-'W'ww-e'T'HH:mm:ssZZ
     */
    public static DateTimeFormatter weekDateTimeNoMillis() {
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
    public static DateTimeFormatter basicDate() {
        if (bd == null) {
            bd = new DateTimeFormatterBuilder()
                .appendYear(4, 4)
                .appendFixedDecimal(DateTimeFieldType.monthOfYear(), 2)
                .appendFixedDecimal(DateTimeFieldType.dayOfMonth(), 2)
                .toFormatter();
        }
        return bd;
    }

    /**
     * Returns a basic formatter for a two digit hour of day, two digit minute
     * of hour, two digit second of minute, three digit millis, and time zone
     * offset (HHmmss.SSSZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HHmm' for non-zero.
     * 
     * @return a formatter for HHmmss.SSSZ
     */
    public static DateTimeFormatter basicTime() {
        if (bt == null) {
            bt = new DateTimeFormatterBuilder()
                .appendFixedDecimal(DateTimeFieldType.hourOfDay(), 2)
                .appendFixedDecimal(DateTimeFieldType.minuteOfHour(), 2)
                .appendFixedDecimal(DateTimeFieldType.secondOfMinute(), 2)
                .appendLiteral('.')
                .appendFractionOfSecond(3, 9)
                .appendTimeZoneOffset("Z", false, 2, 2)
                .toFormatter();
        }
        return bt;
    }

    /**
     * Returns a basic formatter for a two digit hour of day, two digit minute
     * of hour, two digit second of minute, and time zone offset (HHmmssZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HHmm' for non-zero.
     * 
     * @return a formatter for HHmmssZ
     */
    public static DateTimeFormatter basicTimeNoMillis() {
        if (btx == null) {
            btx = new DateTimeFormatterBuilder()
                .appendFixedDecimal(DateTimeFieldType.hourOfDay(), 2)
                .appendFixedDecimal(DateTimeFieldType.minuteOfHour(), 2)
                .appendFixedDecimal(DateTimeFieldType.secondOfMinute(), 2)
                .appendTimeZoneOffset("Z", false, 2, 2)
                .toFormatter();
        }
        return btx;
    }

    /**
     * Returns a basic formatter for a two digit hour of day, two digit minute
     * of hour, two digit second of minute, three digit millis, and time zone
     * offset prefixed by 'T' ('T'HHmmss.SSSZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HHmm' for non-zero.
     * 
     * @return a formatter for 'T'HHmmss.SSSZ
     */
    public static DateTimeFormatter basicTTime() {
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
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HHmm' for non-zero.
     * 
     * @return a formatter for 'T'HHmmssZ
     */
    public static DateTimeFormatter basicTTimeNoMillis() {
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
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HHmm' for non-zero.
     * 
     * @return a formatter for yyyyMMdd'T'HHmmss.SSSZ
     */
    public static DateTimeFormatter basicDateTime() {
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
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HHmm' for non-zero.
     * 
     * @return a formatter for yyyyMMdd'T'HHmmssZ
     */
    public static DateTimeFormatter basicDateTimeNoMillis() {
        if (bdtx == null) {
            bdtx = new DateTimeFormatterBuilder()
                .append(basicDate())
                .append(basicTTimeNoMillis())
                .toFormatter();
        }
        return bdtx;
    }

    /**
     * Returns a formatter for a full ordinal date, using a four
     * digit year and three digit dayOfYear (yyyyDDD).
     * 
     * @return a formatter for yyyyDDD
     * @since 1.1
     */
    public static DateTimeFormatter basicOrdinalDate() {
        if (bod == null) {
            bod = new DateTimeFormatterBuilder()
                .appendYear(4, 4)
                .appendFixedDecimal(DateTimeFieldType.dayOfYear(), 3)
                .toFormatter();
        }
        return bod;
    }

    /**
     * Returns a formatter for a full ordinal date and time, using a four
     * digit year and three digit dayOfYear (yyyyDDD'T'HHmmss.SSSZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HHmm' for non-zero.
     * 
     * @return a formatter for yyyyDDD'T'HHmmss.SSSZ
     * @since 1.1
     */
    public static DateTimeFormatter basicOrdinalDateTime() {
        if (bodt == null) {
            bodt = new DateTimeFormatterBuilder()
                .append(basicOrdinalDate())
                .append(basicTTime())
                .toFormatter();
        }
        return bodt;
    }

    /**
     * Returns a formatter for a full ordinal date and time without millis,
     * using a four digit year and three digit dayOfYear (yyyyDDD'T'HHmmssZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HHmm' for non-zero.
     * 
     * @return a formatter for yyyyDDD'T'HHmmssZ
     * @since 1.1
     */
    public static DateTimeFormatter basicOrdinalDateTimeNoMillis() {
        if (bodtx == null) {
            bodtx = new DateTimeFormatterBuilder()
                .append(basicOrdinalDate())
                .append(basicTTimeNoMillis())
                .toFormatter();
        }
        return bodtx;
    }

    /**
     * Returns a basic formatter for a full date as four digit weekyear, two
     * digit week of weekyear, and one digit day of week (xxxx'W'wwe).
     * 
     * @return a formatter for xxxx'W'wwe
     */
    public static DateTimeFormatter basicWeekDate() {
        if (bwd == null) {
            bwd = new DateTimeFormatterBuilder()
                .appendWeekyear(4, 4)
                .appendLiteral('W')
                .appendFixedDecimal(DateTimeFieldType.weekOfWeekyear(), 2)
                .appendFixedDecimal(DateTimeFieldType.dayOfWeek(), 1)
                .toFormatter();
        }
        return bwd;
    }

    /**
     * Returns a basic formatter that combines a basic weekyear date and time,
     * separated by a 'T' (xxxx'W'wwe'T'HHmmss.SSSZ).
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HHmm' for non-zero.
     * 
     * @return a formatter for xxxx'W'wwe'T'HHmmss.SSSZ
     */
    public static DateTimeFormatter basicWeekDateTime() {
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
     * The time zone offset is 'Z' for zero, and of the form '\u00b1HHmm' for non-zero.
     * 
     * @return a formatter for xxxx'W'wwe'T'HHmmssZ
     */
    public static DateTimeFormatter basicWeekDateTimeNoMillis() {
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
    public static DateTimeFormatter year() {
        return yearElement();
    }

    /**
     * Returns a formatter for a four digit year and two digit month of
     * year. (yyyy-MM)
     * 
     * @return a formatter for yyyy-MM
     */
    public static DateTimeFormatter yearMonth() {
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
    public static DateTimeFormatter yearMonthDay() {
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
    public static DateTimeFormatter weekyear() {
        return weekyearElement();
    }

    /**
     * Returns a formatter for a four digit weekyear and two digit week of
     * weekyear. (xxxx-'W'ww)
     * 
     * @return a formatter for xxxx-'W'ww
     */
    public static DateTimeFormatter weekyearWeek() {
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
    public static DateTimeFormatter weekyearWeekDay() {
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
    public static DateTimeFormatter hour() {
        return hourElement();
    }

    /**
     * Returns a formatter for a two digit hour of day and two digit minute of
     * hour. (HH:mm)
     * 
     * @return a formatter for HH:mm
     */
    public static DateTimeFormatter hourMinute() {
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
    public static DateTimeFormatter hourMinuteSecond() {
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
     * second (HH:mm:ss.SSS). Parsing will parse up to 3 fractional second
     * digits.
     * 
     * @return a formatter for HH:mm:ss.SSS
     */
    public static DateTimeFormatter hourMinuteSecondMillis() {
        if (hmsl == null) {
            hmsl = new DateTimeFormatterBuilder()
                .append(hourElement())
                .append(minuteElement())
                .append(secondElement())
                .appendLiteral('.')
                .appendFractionOfSecond(3, 3)
                .toFormatter();
        }
        return hmsl;
    }

    /**
     * Returns a formatter for a two digit hour of day, two digit minute of
     * hour, two digit second of minute, and three digit fraction of
     * second (HH:mm:ss.SSS). Parsing will parse up to 9 fractional second
     * digits, throwing away all except the first three.
     * 
     * @return a formatter for HH:mm:ss.SSS
     */
    public static DateTimeFormatter hourMinuteSecondFraction() {
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
    public static DateTimeFormatter dateHour() {
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
    public static DateTimeFormatter dateHourMinute() {
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
    public static DateTimeFormatter dateHourMinuteSecond() {
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
     * fraction of second (yyyy-MM-dd'T'HH:mm:ss.SSS). Parsing will parse up
     * to 3 fractional second digits.
     * 
     * @return a formatter for yyyy-MM-dd'T'HH:mm:ss.SSS
     */
    public static DateTimeFormatter dateHourMinuteSecondMillis() {
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
     * fraction of second (yyyy-MM-dd'T'HH:mm:ss.SSS). Parsing will parse up
     * to 9 fractional second digits, throwing away all except the first three.
     * 
     * @return a formatter for yyyy-MM-dd'T'HH:mm:ss.SSS
     */
    public static DateTimeFormatter dateHourMinuteSecondFraction() {
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
    private static DateTimeFormatter yearElement() {
        if (ye == null) {
            ye = new DateTimeFormatterBuilder()
                .appendYear(4, 9)
                .toFormatter();
        }
        return ye;
    }

    private static DateTimeFormatter monthElement() {
        if (mye == null) {
            mye = new DateTimeFormatterBuilder()
                .appendLiteral('-')
                .appendMonthOfYear(2)
                .toFormatter();
        }
        return mye;
    }

    private static DateTimeFormatter dayOfMonthElement() {
        if (dme == null) {
            dme = new DateTimeFormatterBuilder()
                .appendLiteral('-')
                .appendDayOfMonth(2)
                .toFormatter();
        }
        return dme;
    }

    private static DateTimeFormatter weekyearElement() {
        if (we == null) {
            we = new DateTimeFormatterBuilder()
                .appendWeekyear(4, 9)
                .toFormatter();
        }
        return we;
    }

    private static DateTimeFormatter weekElement() {
        if (wwe == null) {
            wwe = new DateTimeFormatterBuilder()
                .appendLiteral("-W")
                .appendWeekOfWeekyear(2)
                .toFormatter();
        }
        return wwe;
    }

    private static DateTimeFormatter dayOfWeekElement() {
        if (dwe == null) {
            dwe = new DateTimeFormatterBuilder()
                .appendLiteral('-')
                .appendDayOfWeek(1)
                .toFormatter();
        }
        return dwe;
    }

    private static DateTimeFormatter dayOfYearElement() {
        if (dye == null) {
            dye = new DateTimeFormatterBuilder()
                .appendLiteral('-')
                .appendDayOfYear(3)
                .toFormatter();
        }
        return dye;
    }
    
    private static DateTimeFormatter literalTElement() {
        if (lte == null) {
            lte = new DateTimeFormatterBuilder()
                .appendLiteral('T')
                .toFormatter();
        }
        return lte;
    }

    private static DateTimeFormatter hourElement() {
        if (hde == null) {
            hde = new DateTimeFormatterBuilder()
                .appendHourOfDay(2)
                .toFormatter();
        }
        return hde;
    }

    private static DateTimeFormatter minuteElement() {
        if (mhe == null) {
            mhe = new DateTimeFormatterBuilder()
                .appendLiteral(':')
                .appendMinuteOfHour(2)
                .toFormatter();
        }
        return mhe;
    }

    private static DateTimeFormatter secondElement() {
        if (sme == null) {
            sme = new DateTimeFormatterBuilder()
                .appendLiteral(':')
                .appendSecondOfMinute(2)
                .toFormatter();
        }
        return sme;
    }

    private static DateTimeFormatter fractionElement() {
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

    private static DateTimeFormatter offsetElement() {
        if (ze == null) {
            ze = new DateTimeFormatterBuilder()
                .appendTimeZoneOffset("Z", true, 2, 4)
                .toFormatter();
        }
        return ze;
    }

}
