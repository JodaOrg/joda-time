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
package org.joda.time;

import java.util.Locale;
import java.io.Serializable;

/**
 * DateTimeFieldProperty binds a ReadableInstant to a DateTimeField.
 * <p>
 * DateTimeFieldProperty allows the date and time manipulation code to be 
 * field based yet still easy to use.
 *
 * @see ReadableInstant
 * @see DateTimeField
 * 
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public class DateTimeFieldProperty implements Serializable {
    
    /** The instant this property is working against */
    private final ReadableInstant iInstant;
    /** The field this property is working against */
    private final DateTimeField iField;

    /**
     * Constructor.
     * 
     * @param instant  the instant to set
     * @param field  the field to use
     */
    public DateTimeFieldProperty(ReadableInstant instant, DateTimeField field) {
        super();
        iInstant = instant;
        iField = field;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the field being used.
     * 
     * @return the field
     */
    public DateTimeField getField() {
        return iField;
    }

    /**
     * Gets the name of the field.
     * 
     * @return the field name
     */
    public String getName() {
        return iField.getName();
    }

    /**
     * Gets the instant being used.
     * 
     * @return the instant
     */
    public ReadableInstant getInstant() {
        return iInstant;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a value from the instant.
     * 
     * @return the current value
     * @see DateTimeField#get
     */
    public int get() {
        return iField.get(iInstant.getMillis());
    }

    /**
     * Gets a text value from the instant.
     * 
     * @param locale  optional locale to use for selecting a text symbol
     * @return the current text value
     * @see DateTimeField#getAsText
     */
    public String getAsText(Locale locale) {
        return iField.getAsText(iInstant.getMillis(), locale);
    }

    /**
     * Gets a text value from the instant.
     * 
     * @return the current text value
     * @see DateTimeField#getAsText
     */
    public final String getAsText() {
        return iField.getAsText(iInstant.getMillis(), null);
    }

    /**
     * Gets a short text value from the instant.
     * 
     * @param locale  optional locale to use for selecting a text symbol
     * @return the current text value
     * @see DateTimeField#getAsShortText
     */
    public String getAsShortText(Locale locale) {
        return iField.getAsShortText(iInstant.getMillis(), locale);
    }

    /**
     * Gets a short text value from the instant.
     * 
     * @return the current text value
     * @see DateTimeField#getAsShortText
     */
    public final String getAsShortText() {
        return getAsShortText(null);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the difference between this field property instant and the one
     * passed in, in the units of this field. The sign of the difference
     * matches that of compareTo. In other words, this field property's instant
     * is the minuend.
     *
     * @param instant the subtrahend
     * @return the difference in the units of this field
     * @see DateTimeField#getDifference
     */
    public long getDifference(ReadableInstant instant) {
        if (instant == null) {
            throw new IllegalArgumentException("The instant must not be null");
        }
        return iField.getDifference(iInstant.getMillis(), instant.getMillis());
    }

    /**
     * Gets whether this field is leap.
     * 
     * @return true if a leap field
     * @see DateTimeField#isLeap
     */
    public boolean isLeap() {
        return iField.isLeap(iInstant.getMillis());
    }

    /**
     * Gets the amount by which this field is leap.
     * 
     * @return the amount by which the field is leap
     * @see DateTimeField#getLeapAmount
     */
    public int getLeapAmount() {
        return iField.getLeapAmount(iInstant.getMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the minimum value for the field ignoring the current time.
     * 
     * @return the minimum value
     * @see DateTimeField#getMinimumValue
     */
    public int getMinimumValueOverall() {
        return iField.getMinimumValue();
    }

    /**
     * Gets the minimum value for the field.
     * 
     * @return the minimum value
     * @see DateTimeField#getMinimumValue
     */
    public int getMinimumValue() {
        return iField.getMinimumValue(iInstant.getMillis());
    }

    /**
     * Gets the maximum value for the field ignoring the current time.
     * 
     * @return the maximum value
     * @see DateTimeField#getMaximumValue
     */
    public int getMaximumValueOverall() {
        return iField.getMaximumValue();
    }

    /**
     * Gets the maximum value for the field.
     * 
     * @return the maximum value
     * @see DateTimeField#getMaximumValue
     */
    public int getMaximumValue() {
        return iField.getMaximumValue(iInstant.getMillis());
    }

    /**
     * Gets the maximum text length for the field.
     * 
     * @param locale  optional locale to use for selecting a text symbol
     * @return the maximum length
     * @see DateTimeField#getMaximumTextLength
     */
    public int getMaximumTextLength(Locale locale) {
        return iField.getMaximumTextLength(locale);
    }

    /**
     * Gets the maximum short text length for the field.
     * 
     * @param locale  optional locale to use for selecting a text symbol
     * @return the maximum length
     * @see DateTimeField#getMaximumShortTextLength
     */
    public int getMaximumShortTextLength(Locale locale) {
        return iField.getMaximumShortTextLength(locale);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the amount of milliseconds per unit value of this field. For
     * example, if this field represents "hour of day", then the unit is the
     * amount of milliseconds per one hour.
     * <p>
     * For fields with a variable unit size, this method returns a suitable
     * average value.
     *
     * @return the unit size of this field, in milliseconds
     */
    public long getUnitMillis() {
        return iField.getUnitMillis();
    }

    /**
     * Returns the range of this field, in milliseconds. For example, if this
     * field represents "hour of day", then the range is the amount of
     * milliseconds per one day.
     * <p>
     * For fields with a variable range, this method returns a suitable average
     * value. If the range is too large to fit in a long, Long.MAX_VALUE is
     * returned.
     *
     * @return the range of this field, in milliseconds
     */
    public long getRangeMillis() {
        return iField.getRangeMillis();
    }

    //-----------------------------------------------------------------------
    /**
     * Compare this field to the same field on another instant.
     * <p>
     * The {@link #get()} method is used to obtain the value to compare for
     * this instant and the {@link ReadableInstant#get(DateTimeField)} method
     * is used for the specified instant.
     * 
     * @param instant  the instant to compare to
     * @return -1 if this is less, +1 if more and 0 if equal
     * @throws IllegalArgumentException if the instant is null
     */
    public int compareTo(ReadableInstant instant) {
        if (instant == null) {
            throw new IllegalArgumentException("The instant must not be null");
        }
        int thisValue = get();
        int otherValue = iField.get(instant.getMillis());
        if (thisValue < otherValue) {
            return -1;
        } else if (thisValue > otherValue) {
            return 1;
        } else {
            return 0;
        }
    }
    
    //-----------------------------------------------------------------------
    /**
     * Output a debugging string.
     * 
     * @return debugging string
     */
    public String toString() {
        return "DateTimeFieldProperty[" + getName() + "]";
    }

}
