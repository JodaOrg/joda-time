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
package org.joda.time.chrono.gj;

import java.util.Locale;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;

/**
 * GJDayOfWeekDateTimeField provides time calculations for the
 * day of the week component of time.
 *
 * @since 1.0
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 */
final class GJDayOfWeekDateTimeField extends DateTimeField {
    
    private static final int MIN = DateTimeConstants.MONDAY;
    private static final int MAX = DateTimeConstants.SUNDAY;
    
    private final ProlepticChronology iChronology;

    /**
     * Restricted constructor.
     */
    GJDayOfWeekDateTimeField(ProlepticChronology chronology) {
        super("dayOfWeek");
        iChronology = chronology;
    }

    /**
     * Get the value of the specified time instant.
     * 
     * @param millis  the time instant in millis to query
     * @return the day of the week extracted from the input
     */
    public int get(long millis) {
        // 1970-01-01 is day of week 4, Thursday.

        long daysSince19700101;
        if (millis >= 0) {
            daysSince19700101 = millis / DateTimeConstants.MILLIS_PER_DAY;
        } else {
            daysSince19700101 = (millis - (DateTimeConstants.MILLIS_PER_DAY - 1))
                / DateTimeConstants.MILLIS_PER_DAY;
            if (daysSince19700101 < -3) {
                return 7 + (int) ((daysSince19700101 + 4) % 7);
            }
        }

        return 1 + (int) ((daysSince19700101 + 3) % 7);
    }

    /**
     * Get the textual value of the specified time instant.
     * 
     * @param millis  the time instant in millis to query
     * @param locale  the locale to use
     * @return the day of the week, such as 'Monday'
     */
    public String getAsText(long millis, Locale locale) {
        return GJLocaleSymbols.forLocale(locale).dayOfWeekValueToText(get(millis));
    }

    /**
     * Get the abbreviated textual value of the specified time instant.
     * 
     * @param millis  the time instant in millis to query
     * @param locale  the locale to use
     * @return the day of the week, such as 'Mon'
     */
    public String getAsShortText(long millis, Locale locale) {
        return GJLocaleSymbols.forLocale(locale).dayOfWeekValueToShortText(get(millis));
    }

    /**
     * Add to the value of the specified time instant.
     * The amount added may be negative.
     * 
     * @param millis  the time instant in millis to update
     * @param day  the day of the week to add (can be negative)
     * @return the updated time instant
     */
    public long add(long millis, int days) {
        return millis + days * (long)DateTimeConstants.MILLIS_PER_DAY;
    }

    public long add(long millis, long days) {
        return millis + days * DateTimeConstants.MILLIS_PER_DAY;
    }

    /**
     * Add to the value of the specified time instant wrapping around
     * within that component if necessary.
     * The amount added may be negative.
     * 
     * @param millis  the time instant in millis to update
     * @param day  the day of the week to add (can be negative)
     * @return the updated time instant.
     */
    public long addWrapped(long millis, int days) {
        int thisDow = get(millis);
        int wrappedDow = getWrappedValue(thisDow, days, MIN, MAX);
        // copy code from set() to avoid repeat call to get()
        return millis + (wrappedDow - thisDow) * (long)DateTimeConstants.MILLIS_PER_DAY;
    }

    public long getDifference(long minuendMillis, long subtrahendMillis) {
        return (minuendMillis - subtrahendMillis) / DateTimeConstants.MILLIS_PER_DAY;
    }

    /**
     * Set the value of the specified time instant.
     * 
     * @param millis  the time instant in millis to update
     * @param day  the day of the week (1,7) to update the time to
     * @return the updated time instant
     * @throws IllegalArgumentException  if day is invalid
     */
    public long set(long millis, int day) {
        verifyValueBounds(day, MIN, MAX);
        int thisDow = get(millis);
        return millis + (day - thisDow) * (long)DateTimeConstants.MILLIS_PER_DAY;
    }

    /**
     * Set the value of the specified time instant from text.
     * 
     * @param millis  the time instant in millis to update
     * @param text  the text to set from
     * @param locale  the locale to use
     * @return the updated millis
     */
    public long set(long millis, String text, Locale locale) {
        return set(millis, GJLocaleSymbols.forLocale(locale).dayOfWeekTextToValue(text));
    }

    public long getUnitMillis() {
        return DateTimeConstants.MILLIS_PER_DAY;
    }

    public long getRangeMillis() {
        return DateTimeConstants.MILLIS_PER_WEEK;
    }

    /**
     * Get the minimum value that this field can have.
     * 
     * @return the field's minimum value
     */
    public int getMinimumValue() {
        return MIN;
    }

    /**
     * Get the maximum value that this field can have.
     * 
     * @return the field's maximum value
     */
    public int getMaximumValue() {
        return MAX;
    }

    /**
     * Get the maximum length of the text returned by this field.
     * 
     * @param locale  the locale to use
     * @return the maximum textual length
     */
    public int getMaximumTextLength(Locale locale) {
        return GJLocaleSymbols.forLocale(locale).getDayOfWeekMaxTextLength();
    }

    /**
     * Get the maximum length of the abbreviated text returned by this field.
     * 
     * @param locale  the locale to use
     * @return the maximum abbreviated textual length
     */
    public int getMaximumShortTextLength(Locale locale) {
        return GJLocaleSymbols.forLocale(locale).getDayOfWeekMaxShortTextLength();
    }
    
    public long roundFloor(long millis) {
        if (millis >= 0) {
            return millis - millis % DateTimeConstants.MILLIS_PER_DAY;
        } else {
            millis += 1;
            return millis - millis % DateTimeConstants.MILLIS_PER_DAY - DateTimeConstants.MILLIS_PER_DAY;
        }
    }

    public long roundCeiling(long millis) {
        if (millis >= 0) {
            millis -= 1;
            return millis - millis % DateTimeConstants.MILLIS_PER_DAY + DateTimeConstants.MILLIS_PER_DAY;
        } else {
            return millis - millis % DateTimeConstants.MILLIS_PER_DAY;
        }
    }

    public long remainder(long millis) {
        if (millis >= 0) {
            return millis % DateTimeConstants.MILLIS_PER_DAY;
        } else {
            return (millis + 1) % DateTimeConstants.MILLIS_PER_DAY + (DateTimeConstants.MILLIS_PER_DAY - 1);
        }
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return iChronology.dayOfWeek();
    }
}
