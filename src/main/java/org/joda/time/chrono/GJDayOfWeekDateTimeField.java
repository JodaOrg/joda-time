/*
 *  Copyright 2001-2013 Stephen Colebourne
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
package org.joda.time.chrono;

import java.util.Locale;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
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
    @SuppressWarnings("unused")
    private static final long serialVersionUID = -3857947176719041436L;

    private final BasicChronology iChronology;

    /**
     * Restricted constructor.
     */
    GJDayOfWeekDateTimeField(BasicChronology chronology, DurationField days) {
        super(DateTimeFieldType.dayOfWeek(), days);
        iChronology = chronology;
    }

    /**
     * Get the value of the specified time instant.
     * 
     * @param instant  the time instant in millis to query
     * @return the day of the week extracted from the input
     */
    @Override
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
    @Override
    public String getAsText(int fieldValue, Locale locale) {
        return GJLocaleSymbols.forLocale(locale).dayOfWeekValueToText(fieldValue);
    }

    /**
     * Get the abbreviated textual value of the specified time instant.
     * 
     * @param fieldValue  the field value to query
     * @param locale  the locale to use
     * @return the day of the week, such as 'Mon'
     */
    @Override
    public String getAsShortText(int fieldValue, Locale locale) {
        return GJLocaleSymbols.forLocale(locale).dayOfWeekValueToShortText(fieldValue);
    }

    /**
     * Convert the specified text and locale into a value.
     * 
     * @param text  the text to convert
     * @param locale  the locale to convert using
     * @return the value extracted from the text
     * @throws IllegalArgumentException if the text is invalid
     */
    @Override
    protected int convertText(String text, Locale locale) {
        return GJLocaleSymbols.forLocale(locale).dayOfWeekTextToValue(text);
    }

    @Override
    public DurationField getRangeDurationField() {
        return iChronology.weeks();
    }

    /**
     * Get the minimum value that this field can have.
     * 
     * @return the field's minimum value
     */
    @Override
    public int getMinimumValue() {
        return DateTimeConstants.MONDAY;
    }

    /**
     * Get the maximum value that this field can have.
     * 
     * @return the field's maximum value
     */
    @Override
    public int getMaximumValue() {
        return DateTimeConstants.SUNDAY;
    }

    /**
     * Get the maximum length of the text returned by this field.
     * 
     * @param locale  the locale to use
     * @return the maximum textual length
     */
    @Override
    public int getMaximumTextLength(Locale locale) {
        return GJLocaleSymbols.forLocale(locale).getDayOfWeekMaxTextLength();
    }

    /**
     * Get the maximum length of the abbreviated text returned by this field.
     * 
     * @param locale  the locale to use
     * @return the maximum abbreviated textual length
     */
    @Override
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
