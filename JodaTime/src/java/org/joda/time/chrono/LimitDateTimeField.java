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
package org.joda.time.chrono;

import java.util.Locale;

import org.joda.time.DateTimeField;

/**
 * Generic limiting datetime field.
 * <p>
 * This DateTimeField allows specific millisecond boundaries to be applied 
 * to DateTimeFields.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public class LimitDateTimeField extends DateTimeField {

    /** The field to wrap */
    private final DateTimeField iField;
    /** The lower boundary in millis */
    private final long iLowerBound;
    /** The upper boundary in millis */
    private final long iUpperBound;

    /**
     * Constructor
     * 
     * @param name  short, descriptive name, like "secondOfMinute".
     * @param lowerBound  milliseconds to form the lower boundary inclusive
     * @param upperBound  milliseconds to form the upper boundary inclusive
     * @throws IllegalArgumentException if field is null or boundary is invalid
     */
    public LimitDateTimeField(String name, DateTimeField field, long lowerBound, long upperBound) {
        super(name);
                
        if (field == null) {
            throw new IllegalArgumentException("The field must not be null");
        }
        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException("The lowerBound must be less than the upperBound");
        }
        iField = field;
        iLowerBound = lowerBound;
        iUpperBound = upperBound;
    }

    /**
     * Get the amount of fractional units from the specified time instant.
     * 
     * @param millis  the time instant in millis to query.
     * @return the amount of fractional units extracted from the input.
     */
    public int get(long millis) {
        checkBounds(millis, "value");
        return iField.get(millis);
    }

    public String getAsText(long millis, Locale locale) {
        checkBounds(millis, "value");
        return iField.getAsText(millis, locale);
    }

    public String getAsShortText(long millis, Locale locale) {
        checkBounds(millis, "value");
        return iField.getAsShortText(millis, locale);
    }

    /**
     * Add the specified amount of fractional units to the specified time
     * instant. The amount added may be negative.
     * 
     * @param millis  the time instant in millis to update.
     * @param amount  the amount of fractional units to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long millis, int amount) {
        checkBounds(millis, "value");
        long result = iField.add(millis, amount);
        checkBounds(millis, "result");
        return result;
    }

    /**
     * Add the specified amount of fractional units to the specified time
     * instant. The amount added may be negative.
     * 
     * @param millis  the time instant in millis to update.
     * @param amount  the amount of fractional units to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long millis, long amount) {
        checkBounds(millis, "value");
        long result = iField.add(millis, amount);
        checkBounds(millis, "result");
        return result;
    }

    /**
     * Add to the fractional component of the specified time instant,
     * wrapping around within that component if necessary.
     * 
     * @param millis  the time instant in millis to update.
     * @param amount  the amount of fractional units to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapped(long millis, int amount) {
        checkBounds(millis, "value");
        long result = iField.addWrapped(millis, amount);
        checkBounds(millis, "result");
        return result;
    }

    public long getDifference(long minuendMillis, long subtrahendMillis) {
        checkBounds(minuendMillis, "minuend");
        checkBounds(subtrahendMillis, "subtrahend");
        return iField.getDifference(minuendMillis, subtrahendMillis);
    }

    /**
     * Set the specified amount of fractional units to the specified time instant.
     * 
     * @param millis  the time instant in millis to update.
     * @param value  value of fractional units to set.
     * @return the updated time instant.
     * @throws IllegalArgumentException if value is too large or too small.
     */
    public long set(long millis, int value) {
        checkBounds(millis, "value");
        long result = iField.set(millis, value);
        checkBounds(millis, "result");
        return result;
    }

    public long set(long millis, String text, Locale locale) {
        checkBounds(millis, "value");
        long result = iField.set(millis, text, locale);
        checkBounds(millis, "result");
        return result;
    }

    public long getUnitMillis() {
        return iField.getUnitMillis();
    }

    public long getRangeMillis() {
        return iField.getRangeMillis();
    }

    /**
     * Get the minimum value for the field.
     * 
     * @return the minimum value
     */
    public int getMinimumValue() {
        return iField.getMinimumValue();
    }

    /**
     * Get the maximum value for the field.
     * 
     * @return the maximum value
     */
    public int getMaximumValue() {
        return iField.getMaximumValue();
    }

    public int getMaximumTextLength(Locale locale) {
        return iField.getMaximumTextLength(locale);
    }

    public int getMaximumShortTextLength(Locale locale) {
        return iField.getMaximumShortTextLength(locale);
    }

    public long roundFloor(long millis) {
        checkBounds(millis, "value");
        long result = iField.roundFloor(millis);
        checkBounds(millis, "result");
        return result;
    }

    public long roundCeiling(long millis) {
        checkBounds(millis, "value");
        long result = iField.roundCeiling(millis);
        checkBounds(millis, "result");
        return result;
    }

    public long roundHalfFloor(long millis) {
        checkBounds(millis, "value");
        long result = iField.roundHalfFloor(millis);
        checkBounds(millis, "result");
        return result;
    }

    public long roundHalfCeiling(long millis) {
        checkBounds(millis, "value");
        long result = iField.roundHalfCeiling(millis);
        checkBounds(millis, "result");
        return result;
    }

    public long roundHalfEven(long millis) {
        checkBounds(millis, "value");
        long result = iField.roundHalfEven(millis);
        checkBounds(millis, "result");
        return result;
    }

    public long remainder(long millis) {
        checkBounds(millis, "value");
        long result = iField.remainder(millis);
        checkBounds(millis, "result");
        return result;
    }

   /**
     * Returns the DateTimeField being wrapped.
     * 
     * @return field
     */
    public DateTimeField getField() {
        return iField;
    }

    /**
     * Returns the milliseconds lower bound.
     * 
     * @return lower bound
     */
    public long getLowerBound() {
        return iLowerBound;
    }

    /**
     * Returns the milliseconds upper bound.
     * 
     * @return upper bound
     */
    public long getUpperBound() {
        return iUpperBound;
    }

    private void checkBounds(long millis, String desc) {
        if (millis < iLowerBound) {
            throw new IllegalArgumentException("The millisecond " + desc + " is below the minimum");
        }
        if (millis > iUpperBound) {
            throw new IllegalArgumentException("The millisecond " + desc + " is above the maximum");
        }
    }
}
