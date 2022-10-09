/*
 *  Copyright 2001-2009 Stephen Colebourne
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
package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.ReadWritableInterval;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

/**
 * StringConverter converts from a String to an instant, partial,
 * duration, period or interval..
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
class StringConverter extends AbstractConverter
        implements InstantConverter, PartialConverter, DurationConverter, PeriodConverter, IntervalConverter {

    /**
     * Singleton instance.
     */
    static final StringConverter INSTANCE = new StringConverter();

    /**
     * Restricted constructor.
     */
    protected StringConverter() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the millis, which is the ISO parsed string value.
     * 
     * @param object  the String to convert, must not be null
     * @param chrono  the chronology to use, non-null result of getChronology
     * @return the millisecond value
     * @throws IllegalArgumentException if the value if invalid
     */
    @Override
    public long getInstantMillis(Object object, Chronology chrono) {
        String str = (String) object;
        DateTimeFormatter p = ISODateTimeFormat.dateTimeParser();
        return p.withChronology(chrono).parseMillis(str);
    }

    /**
     * Extracts the values of the partial from an object of this converter's type.
     * This method checks if the parser has a zone, and uses it if present.
     * This is most useful for parsing local times with UTC.
     * 
     * @param fieldSource  a partial that provides access to the fields.
     *  This partial may be incomplete and only getFieldType(int) should be used
     * @param object  the object to convert
     * @param chrono  the chronology to use, which is the non-null result of getChronology()
     * @param parser the parser to use, may be null
     * @return the array of field values that match the fieldSource, must be non-null valid
     * @throws ClassCastException if the object is invalid
     * @throws IllegalArgumentException if the value if invalid
     * @since 1.3
     */
    @Override
    public int[] getPartialValues(ReadablePartial fieldSource, Object object, Chronology chrono, DateTimeFormatter parser) {
        if (parser.getZone() != null) {
            chrono = chrono.withZone(parser.getZone());
        }
        long millis = parser.withChronology(chrono).parseMillis((String) object);
        return chrono.get(fieldSource, millis);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the duration of the string using the standard type.
     * This matches the toString() method of ReadableDuration.
     * 
     * @param object  the String to convert, must not be null
     * @throws ClassCastException if the object is invalid
     */
    public long getDurationMillis(Object object) {
        // parse here because duration could be bigger than the int supported
        // by the period parser
        String original = (String) object;
        String str = original;
        int len = str.length();
        if (len >= 4 &&
            (str.charAt(0) == 'P' || str.charAt(0) == 'p') &&
            (str.charAt(1) == 'T' || str.charAt(1) == 't') &&
            (str.charAt(len - 1) == 'S' || str.charAt(len - 1) == 's')) {
            // ok
        } else {
            throw new IllegalArgumentException("Invalid format: \"" + original + '"');
        }
        str = str.substring(2, len - 1);
        int dot = -1;
        boolean negative = false;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                // ok
            } else if (i == 0 && str.charAt(0) == '-') {
                // ok
                negative = true;
            } else if (i > (negative ? 1 : 0) && str.charAt(i) == '.' && dot == -1) {
                // ok
                dot = i;
            } else {
                throw new IllegalArgumentException("Invalid format: \"" + original + '"');
            }
        }
        long millis = 0, seconds = 0;
        int firstDigit = negative ? 1 : 0;
        if (dot > 0) {
            seconds = Long.parseLong(str.substring(firstDigit, dot));
            str = str.substring(dot + 1);
            if (str.length() != 3) {
                str = (str + "000").substring(0, 3);
            }
            millis = Integer.parseInt(str);
        } else if (negative) {
            seconds = Long.parseLong(str.substring(firstDigit, str.length()));
        } else {
            seconds = Long.parseLong(str);
        }
        if (negative) {
            return FieldUtils.safeAdd(FieldUtils.safeMultiply(-seconds, 1000), -millis);
        } else {
            return FieldUtils.safeAdd(FieldUtils.safeMultiply(seconds, 1000), millis);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Extracts duration values from an object of this converter's type, and
     * sets them into the given ReadWritableDuration.
     *
     * @param period  period to get modified
     * @param object  the String to convert, must not be null
     * @param chrono  the chronology to use
     * @throws ClassCastException if the object is invalid
     */
    public void setInto(ReadWritablePeriod period, Object object, Chronology chrono) {
        String str = (String) object;
        PeriodFormatter parser = ISOPeriodFormat.standard();
        period.clear();
        int pos = parser.parseInto(period, str, 0);
        if (pos < str.length()) {
            if (pos < 0) {
                // Parse again to get a better exception thrown.
                parser.withParseType(period.getPeriodType()).parseMutablePeriod(str);
            }
            throw new IllegalArgumentException("Invalid format: \"" + str + '"');
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the value of the mutable interval from the string.
     * 
     * @param writableInterval  the interval to set
     * @param object  the String to convert, must not be null
     * @param chrono  the chronology to use, may be null
     */
    public void setInto(ReadWritableInterval writableInterval, Object object, Chronology chrono) {
        String str = (String) object;

        int separator = str.indexOf('/');
        if (separator < 0) {
            throw new IllegalArgumentException("Format requires a '/' separator: " + str);
        }

        String leftStr = str.substring(0, separator);
        if (leftStr.length() <= 0) {
            throw new IllegalArgumentException("Format invalid: " + str);
        }
        String rightStr = str.substring(separator + 1);
        if (rightStr.length() <= 0) {
            throw new IllegalArgumentException("Format invalid: " + str);
        }

        DateTimeFormatter dateTimeParser = ISODateTimeFormat.dateTimeParser();
        dateTimeParser = dateTimeParser.withChronology(chrono);
        PeriodFormatter periodParser = ISOPeriodFormat.standard();
        long startInstant = 0, endInstant = 0;
        Period period = null;
        Chronology parsedChrono = null;
        
        // before slash
        char c = leftStr.charAt(0);
        if (c == 'P' || c == 'p') {
            period = periodParser.withParseType(getPeriodType(leftStr)).parsePeriod(leftStr);
        } else {
            DateTime start = dateTimeParser.parseDateTime(leftStr);
            startInstant = start.getMillis();
            parsedChrono = start.getChronology();
        }
        
        // after slash
        c = rightStr.charAt(0);
        if (c == 'P' || c == 'p') {
            if (period != null) {
                throw new IllegalArgumentException("Interval composed of two durations: " + str);
            }
            period = periodParser.withParseType(getPeriodType(rightStr)).parsePeriod(rightStr);
            chrono = (chrono != null ? chrono : parsedChrono);
            endInstant = chrono.add(period, startInstant, 1);
        } else {
            DateTime end = dateTimeParser.parseDateTime(rightStr);
            endInstant = end.getMillis();
            parsedChrono = (parsedChrono != null ? parsedChrono : end.getChronology());
            chrono = (chrono != null ? chrono : parsedChrono);
            if (period != null) {
                startInstant = chrono.add(period, endInstant, -1);
            }
        }
        
        writableInterval.setInterval(startInstant, endInstant);
        writableInterval.setChronology(chrono);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns String.class.
     * 
     * @return String.class
     */
    public Class<?> getSupportedType() {
        return String.class;
    }

}
