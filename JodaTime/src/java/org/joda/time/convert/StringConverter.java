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
import org.joda.time.DurationType;
import org.joda.time.ReadWritableDuration;
import org.joda.time.ReadWritableInterval;
import org.joda.time.ReadableDuration;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.DurationFormatter;
import org.joda.time.format.DurationParser;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.ISODurationFormat;

/**
 * StringConverter converts a String to milliseconds in the ISOChronology.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
class StringConverter extends AbstractConverter
    implements InstantConverter, DurationConverter, IntervalConverter {
    
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
     * Returns false always.
     */
    public boolean isPrecise(Object object) {
        return false;
    }

    /**
     * @throws UnsupportedOperationException always
     */
    public long getDurationMillis(Object object) {
        throw new UnsupportedOperationException();
    }

    /**
     * Extracts duration values from an object of this converter's type, and
     * sets them into the given ReadWritableDuration.
     *
     * @param duration duration to get modified
     * @param object  the object to convert, must not be null
     * @return the millisecond duration
     * @throws ClassCastException if the object is invalid
     */
    public void setInto(ReadWritableDuration duration, Object object) {
        String str = (String) object;
        DurationParser parser = ISODurationFormat.getInstance().standard();
        int pos = parser.parseInto(duration, str, 0);
        if (pos < str.length()) {
            if (pos < 0) {
                // Parse again to get a better exception thrown.
                parser.parseMutableDuration(duration.getDurationType(), str);
            }
            throw new IllegalArgumentException("Invalid format: \"" + str + '"');
        }
    }

    /**
     * Selects a suitable duration type for the given object.
     *
     * @param object  the object to examine, must not be null
     * @return the duration type, never null
     * @throws ClassCastException if the object is invalid
     */
    public DurationType getDurationType(Object object) {
        String str = (String) object;
        if (str.indexOf('W') >= 0) {
            return DurationType.getYearWeekType();
        }
        return DurationType.getYearMonthType();
    }

    /**
     * Sets the value of the mutable interval from the string.
     * 
     * @param writableInterval  the interval to set
     * @param object  the string to set from
     */
    public void setInto(ReadWritableInterval writableInterval, Object object) {
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
        DurationFormatter durationParser = ISODurationFormat.getInstance().standard();
        long startInstant;
        ReadableDuration duration;

        char c = leftStr.charAt(0);
        if (c == 'P' || c == 'p') {
            startInstant = 0;
            duration = durationParser.parseDuration(getDurationType(leftStr), leftStr);
        } else {
            startInstant = dateTimeParser.parseMillis(leftStr);
            duration = null;
        }

        c = rightStr.charAt(0);
        if (c == 'P' || c == 'p') {
            if (duration != null) {
                throw new IllegalArgumentException("Interval composed of two durations: " + str);
            }
            duration = durationParser.parseDuration(getDurationType(rightStr), rightStr);
            writableInterval.setStartMillis(startInstant);
            writableInterval.setDurationAfterStart(duration);
        } else {
            long endInstant = dateTimeParser.parseMillis(rightStr);
            writableInterval.setEndMillis(endInstant);
            if (duration == null) {
                writableInterval.setStartMillis(startInstant);
            } else {
                writableInterval.setDurationBeforeEnd(duration);
            }
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
