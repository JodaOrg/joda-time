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
package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.PeriodType;
import org.joda.time.MutablePeriod;
import org.joda.time.ReadWritableInterval;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.Period;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodParser;

/**
 * StringConverter converts a String to milliseconds in the ISOChronology.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
class StringConverter extends AbstractConverter
        implements InstantConverter, DurationConverter, PeriodConverter, IntervalConverter {

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
     * @param object  the object to convert, must not be null
     * @param zone  the zone to use, null means default zone
     * @return the millisecond value
     * @throws IllegalArgumentException if the value if invalid
     */
    public long getInstantMillis(Object object, DateTimeZone zone) {
        String str = (String) object;
        Chronology chrono = ISOChronology.getInstance(zone);
        DateTimeParser p = ISODateTimeFormat.getInstance(chrono).dateTimeParser();
        return p.parseMillis(str);
    }

    /**
     * Gets the millis, which is the ISO parsed string value.
     * 
     * @param object  the object to convert, must not be null
     * @param chrono  the chronology to use, null means ISOChronology
     * @return the millisecond value
     * @throws IllegalArgumentException if the value if invalid
     */
    public long getInstantMillis(Object object, Chronology chrono) {
        String str = (String) object;
        chrono = getChronology(object, chrono);
        DateTimeParser p = ISODateTimeFormat.getInstance(chrono).dateTimeParser();
        return p.parseMillis(str);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the duration of the string using the PreciseAll type.
     * This matches the toString() method of ReadableDuration.
     * 
     * @param object  the object to convert, must not be null
     * @throws ClassCastException if the object is invalid
     */
    public long getDurationMillis(Object object) {
        String str = (String) object;
        MutablePeriod period = new MutablePeriod(PeriodType.getPreciseAllType());
        PeriodParser parser = ISOPeriodFormat.getInstance().standard();
        int pos = parser.parseInto(period, str, 0);
        if (pos < str.length()) {
            if (pos < 0) {
                // Parse again to get a better exception thrown.
                parser.parseMutablePeriod(period.getPeriodType(), str);
            }
            throw new IllegalArgumentException("Invalid format: \"" + str + '"');
        }
        return period.toDurationMillis();
    }

    //-----------------------------------------------------------------------
    /**
     * Extracts duration values from an object of this converter's type, and
     * sets them into the given ReadWritableDuration.
     *
     * @param period  period to get modified
     * @param object  the object to convert, must not be null
     * @return the millisecond duration
     * @throws ClassCastException if the object is invalid
     */
    public void setInto(ReadWritablePeriod period, Object object) {
        String str = (String) object;
        PeriodParser parser = ISOPeriodFormat.getInstance().standard();
        int pos = parser.parseInto(period, str, 0);
        if (pos < str.length()) {
            if (pos < 0) {
                // Parse again to get a better exception thrown.
                parser.parseMutablePeriod(period.getPeriodType(), str);
            }
            throw new IllegalArgumentException("Invalid format: \"" + str + '"');
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Extracts the start and end millisecond instants from the object.
     *
     * @param object  the object to convert, must not be null
     * @return the start millis and end millis in an array
     * @throws ClassCastException if the object is invalid
     */
    public long[] getIntervalMillis(Object object) {
        return parseInterval(null, object);
    }

    /**
     * Sets the value of the mutable interval from the string.
     * 
     * @param writableInterval  the interval to set
     * @param object  the string to set from
     */
    public void setInto(ReadWritableInterval writableInterval, Object object) {
        parseInterval(writableInterval, object);
    }

    /**
     * Sets the value of the mutable interval from the string.
     * 
     * @param writableInterval  the interval to populate, may be null
     * @param object  the string to set from
     * @return an array of size two, containing the start and end millis if interval input is null
     */
    private long[] parseInterval(ReadWritableInterval writableInterval, Object object) {
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

        DateTimeParser dateTimeParser = ISODateTimeFormat.getInstance().dateTimeParser();
        PeriodFormatter periodParser = ISOPeriodFormat.getInstance().standard();
        long startInstant, endInstant;
        Period period;
        
        // before slash
        char c = leftStr.charAt(0);
        if (c == 'P' || c == 'p') {
            startInstant = 0;
            period = periodParser.parsePeriod(getPeriodType(leftStr, false), leftStr);
        } else {
            startInstant = dateTimeParser.parseMillis(leftStr);
            period = null;
        }
        
        // after slash
        c = rightStr.charAt(0);
        if (c == 'P' || c == 'p') {
            if (period != null) {
                throw new IllegalArgumentException("Interval composed of two durations: " + str);
            }
            period = periodParser.parsePeriod(getPeriodType(rightStr, false), rightStr);
            endInstant = period.addTo(startInstant, 1);
        } else {
            endInstant = dateTimeParser.parseMillis(rightStr);
            if (period != null) {
                startInstant = period.addTo(endInstant, -1);
            }
        }
        
        // return data avoiding object creation and code duplication
        if (writableInterval == null) {
            return new long[] {startInstant, endInstant};
        } else {
            writableInterval.setStartMillis(startInstant);
            writableInterval.setEndMillis(endInstant);
            return null;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Returns String.class.
     * 
     * @return String.class
     */
    public Class getSupportedType() {
        return String.class;
    }

}
