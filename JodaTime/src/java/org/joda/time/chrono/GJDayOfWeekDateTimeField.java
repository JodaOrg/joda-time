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
package org.joda.time.chrono;

import java.util.Locale;

import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.field.PreciseDurationDateTimeField;

/**
 * GJDayOfWeekDateTimeField provides time calculations for the
 * day of the week component of time.
 *
 * @since 1.0
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 */
final class GJDayOfWeekDateTimeField extends PreciseDurationDateTimeField {
    
    /** Serialization version */
    private static final long serialVersionUID = -3857947176719041436L;

    private final AbstractGJChronology iChronology;

    /**
     * Restricted constructor.
     */
    GJDayOfWeekDateTimeField(AbstractGJChronology chronology, DurationField days) {
        super("dayOfWeek", days);
        iChronology = chronology;
    }

    /**
     * Get the value of the specified time instant.
     * 
     * @param instant  the time instant in millis to query
     * @return the day of the week extracted from the input
     */
    public int get(long instant) {
        return iChronology.getDayOfWeek(instant);
    }

    /**
     * Get the textual value of the specified time instant.
     * 
     * @param fieldValue  the field value to query
     * @param locale  the locale to use
     * @return the day of the week, such as 'Monday'
     */
    protected String getAsText(int fieldValue, Locale locale) {
        return GJLocaleSymbols.forLocale(locale).dayOfWeekValueToText(fieldValue);
    }

    /**
     * Get the abbreviated textual value of the specified time instant.
     * 
     * @param instant  the time instant in millis to query
     * @param locale  the locale to use
     * @return the day of the week, such as 'Mon'
     */
    protected String getAsShortText(int fieldValue, Locale locale) {
        return GJLocaleSymbols.forLocale(locale).dayOfWeekValueToShortText(fieldValue);
    }

    /**
     * Set the value of the specified time instant from text.
     * 
     * @param instant  the time instant in millis to update
     * @param text  the text to set from
     * @param locale  the locale to use
     * @return the updated millis
     */
    public long set(long instant, String text, Locale locale) {
        return set(instant, GJLocaleSymbols.forLocale(locale).dayOfWeekTextToValue(text));
    }

    public DurationField getRangeDurationField() {
        return iChronology.weeks();
    }

    /**
     * Get the minimum value that this field can have.
     * 
     * @return the field's minimum value
     */
    public int getMinimumValue() {
        return DateTimeConstants.MONDAY;
    }

    /**
     * Get the maximum value that this field can have.
     * 
     * @return the field's maximum value
     */
    public int getMaximumValue() {
        return DateTimeConstants.SUNDAY;
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

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return iChronology.dayOfWeek();
    }
}
