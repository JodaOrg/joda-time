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

/**
 * DateTimeFieldProperty binds a DateTime to a DateTimeField allowing powerful
 * datetime functionality to be easily accessed.
 * <p>
 * The simplest use of this class is as an alternative get method, here used to
 * get the year '1972' (as an int) and the month 'December' (as a String).
 * <pre>
 * DateTime dt = new DateTime(1972, 12, 3, 0, 0, 0, 0);
 * int year = dt.year().get();
 * String monthStr = dt.month().getAsText();
 * </pre>
 * <p>
 * Methods are also provided that allow date modification. These return new instances
 * of DateTime - they do not modify the original. The example below yields two
 * independent immutable date objects 20 years apart.
 * <pre>
 * DateTime dt = new DateTime(1972, 12, 3, 0, 0, 0, 0);
 * DateTime dt20 = dt.year().addToCopy(20);
 * </pre>
 * Serious modification of dates (ie. more than just changing one or two fields)
 * should use the {@link MutableDateTime} class.
 * <p>
 * DateTimeFieldPropery itself is thread-safe and immutable, as well as the
 * DateTime being operated on.
 *
 * @see ReadableInstant
 * @see DateTimeField
 * 
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public class DateTimeFieldProperty extends AbstractDateTimeFieldProperty {
    
    static final long serialVersionUID = -6983323811635733510L;

    /** The instant this property is working against */
    private final DateTime iInstant;
    /** The field this property is working against */
    private final DateTimeField iField;

    /**
     * Constructor.
     * 
     * @param instant  the instant to set
     * @param field  the field to use
     */
    public DateTimeFieldProperty(DateTime instant, DateTimeField field) {
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
     * Gets the instant being used.
     * 
     * @return the instant
     */
    public ReadableDateTime getInstant() {
        return iInstant;
    }

    /**
     * Gets the instant being used.
     * 
     * @return the instant
     */
    public DateTime getDateTime() {
        return iInstant;
    }

    //-----------------------------------------------------------------------
    /**
     * Adds to this field in a copy of this DateTime.
     * <p>
     * The DateTime attached to this property is unchanged by this call.
     * This operation is faster than converting a DateTime to a MutableDateTime
     * and back again when setting one field. When setting multiple fields,
     * it is generally quicker to make the conversion to MutableDateTime.
     * 
     * @param value  the value to add to the field in the copy
     * @return a copy of the DateTime with the field value changed
     * @throws IllegalArgumentException if the value isn't valid
     */
    public DateTime addToCopy(int value) {
        long newMillis = iField.add(iInstant.getMillis(), value);
        return iInstant.createDateTime(newMillis, iInstant.getChronology());
    }

    /**
     * Adds to this field in a copy of this DateTime.
     * <p>
     * The DateTime attached to this property is unchanged by this call.
     * This operation is faster than converting a DateTime to a MutableDateTime
     * and back again when setting one field. When setting multiple fields,
     * it is generally quicker to make the conversion to MutableDateTime.
     * 
     * @param value  the value to add to the field in the copy
     * @return a copy of the DateTime with the field value changed
     * @throws IllegalArgumentException if the value isn't valid
     */
    public DateTime addToCopy(long value) {
        long newMillis = iField.add(iInstant.getMillis(), value);
        return iInstant.createDateTime(newMillis, iInstant.getChronology());
    }

    /**
     * Adds to this field, possibly wrapped, in a copy of this DateTime.
     * A wrapped operation only changes this field.
     * Thus 31st January addWrapped one day goes to the 1st January.
     * <p>
     * The DateTime attached to this property is unchanged by this call.
     * This operation is faster than converting a DateTime to a MutableDateTime
     * and back again when setting one field. When setting multiple fields,
     * it is generally quicker to make the conversion to MutableDateTime.
     * 
     * @param value  the value to add to the field in the copy
     * @return a copy of the DateTime with the field value changed
     * @throws IllegalArgumentException if the value isn't valid
     */
    public DateTime addWrappedToCopy(int value) {
        long newMillis = iField.addWrapped(iInstant.getMillis(), value);
        return iInstant.createDateTime(newMillis, iInstant.getChronology());
    }

    //-----------------------------------------------------------------------
    /**
     * Sets this field in a copy of the DateTime.
     * <p>
     * The DateTime attached to this property is unchanged by this call.
     * This operation is faster than converting a DateTime to a MutableDateTime
     * and back again when setting one field. When setting multiple fields,
     * it is generally quicker to make the conversion to MutableDateTime.
     * 
     * @param value  the value to set the field in the copy to
     * @return a copy of the DateTime with the field value changed
     * @throws IllegalArgumentException if the value isn't valid
     */
    public DateTime setCopy(int value) {
        long newMillis = iField.set(iInstant.getMillis(), value);
        return iInstant.createDateTime(newMillis, iInstant.getChronology());
    }
    
    /**
     * Sets this field in a copy of the DateTime to a parsed text value.
     * <p>
     * The DateTime attached to this property is unchanged by this call.
     * This operation is faster than converting a DateTime to a MutableDateTime
     * and back again when setting one field. When setting multiple fields,
     * it is generally quicker to make the conversion to MutableDateTime.
     * 
     * @param text  the text value to set
     * @param locale  optional locale to use for selecting a text symbol
     * @return a copy of the DateTime with the field value changed
     * @throws IllegalArgumentException if the text value isn't valid
     */
    public DateTime setCopy(String text, Locale locale) {
        long newMillis = iField.set(iInstant.getMillis(), text, locale);
        return iInstant.createDateTime(newMillis, iInstant.getChronology());
    }

    /**
     * Sets this field in a copy of the DateTime to a parsed text value.
     * <p>
     * The DateTime attached to this property is unchanged by this call.
     * This operation is faster than converting a DateTime to a MutableDateTime
     * and back again when setting one field. When setting multiple fields,
     * it is generally quicker to make the conversion to MutableDateTime.
     * 
     * @param text  the text value to set
     * @return a copy of the DateTime with the field value changed
     * @throws IllegalArgumentException if the text value isn't valid
     */
    public final DateTime setCopy(String text) {
        return setCopy(text, null);
    }

    //-----------------------------------------------------------------------
    /**
     * Rounds to the lowest whole unit of this field on a copy of this DateTime.
     *
     * @return a copy of the DateTime with the field value changed
     */
    public DateTime roundFloorCopy() {
        long newMillis = iField.roundFloor(iInstant.getMillis());
        return iInstant.createDateTime(newMillis, iInstant.getChronology());
    }

    /**
     * Rounds to the highest whole unit of this field on a copy of this DateTime.
     *
     * @return a copy of the DateTime with the field value changed
     */
    public DateTime roundCeilingCopy() {
        long newMillis = iField.roundCeiling(iInstant.getMillis());
        return iInstant.createDateTime(newMillis, iInstant.getChronology());
    }

    /**
     * Rounds to the nearest whole unit of this field on a copy of this DateTime,
     * favoring the floor if halfway.
     *
     * @return a copy of the DateTime with the field value changed
     */
    public DateTime roundHalfFloorCopy() {
        long newMillis = iField.roundHalfFloor(iInstant.getMillis());
        return iInstant.createDateTime(newMillis, iInstant.getChronology());
    }

    /**
     * Rounds to the nearest whole unit of this field on a copy of this DateTime,
     * favoring the ceiling if halfway.
     *
     * @return a copy of the DateTime with the field value changed
     */
    public DateTime roundHalfCeilingCopy() {
        long newMillis = iField.roundHalfCeiling(iInstant.getMillis());
        return iInstant.createDateTime(newMillis, iInstant.getChronology());
    }

    /**
     * Rounds to the nearest whole unit of this field on a copy of this DateTime.
     * If halfway, the ceiling is favored over the floor only if it makes this field's value even.
     *
     * @return a copy of the DateTime with the field value changed
     */
    public DateTime roundHalfEvenCopy() {
        long newMillis = iField.roundHalfEven(iInstant.getMillis());
        return iInstant.createDateTime(newMillis, iInstant.getChronology());
    }

}
