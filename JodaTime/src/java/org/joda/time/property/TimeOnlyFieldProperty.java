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
package org.joda.time.property;

import java.util.Locale;
import org.joda.time.TimeOnly;
import org.joda.time.DateTimeField;
// Import for @link support
import org.joda.time.MutableTimeOnly;
import org.joda.time.ReadableInstant;

/**
 * TimeOnlyFieldProperty binds a TimeOnly to a DateTimeField allowing powerful
 * time functionality to be easily accessed.
 * <p>
 * The simplest use of this class is as an alternative get method, here used to
 * get the clockhour '8' (as an int) and the AM/PM string.
 * <pre>
 * TimeOnly time = new TimeOnly(20, 30, 0);
 * int clockhour = time.clockhourOfHalfday().get();
 * String amPmStr = time.halfdayOfDay().getAsText();
 * </pre>
 * <p>
 * Methods are also provided that allow time modification. These return new instances
 * of TimeOnly - they do not modify the original. The example below yields two
 * independent immutable time objects 20 minutes apart.
 * <pre>
 * TimeOnly time = new TimeOnly(20, 30, 0);
 * TimeOnly time20 = time.minuteOfHour().addToCopy(20);
 * </pre>
 * Serious modification of times (ie. more than just changing one or two fields)
 * should use the {@link MutableTimeOnly} class.
 * <p>
 * TimeOnlyFieldPropery itself is thread-safe and immutable, as well as the
 * TimeOnly being operated on.
 *
 * @author Brian S O'Neill
 */
public class TimeOnlyFieldProperty extends AbstractReadableInstantFieldProperty {

    static final long serialVersionUID = 8826542069120527929L;

    /** The instant this property is working against */
    private final TimeOnly iInstant;
    /** The field this property is working against */
    private final DateTimeField iField;

    /**
     * Constructor.
     * 
     * @param instant  the instant to set
     * @param field  the field to use
     */
    public TimeOnlyFieldProperty(TimeOnly instant, DateTimeField field) {
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
    public ReadableInstant getInstant() {
        return iInstant;
    }

    /**
     * Gets the instant being used.
     * 
     * @return the instant
     */
    public TimeOnly getDateTime() {
        return iInstant;
    }

    //-----------------------------------------------------------------------
    /**
     * Adds to this field in a copy of this TimeOnly.
     * <p>
     * The TimeOnly attached to this property is unchanged by this call.
     * This operation is faster than converting a TimeOnly to a MutableTimeOnly
     * and back again when setting one field. When setting multiple fields,
     * it is generally quicker to make the conversion to MutableTimeOnly.
     * 
     * @param value  the value to add to the field in the copy
     * @return a copy of the TimeOnly with the field value changed
     * @throws IllegalArgumentException if the value isn't valid
     */
    public TimeOnly addToCopy(int value) {
        TimeOnly instant = iInstant;
        return (TimeOnly)instant.withMillis(iField.add(instant.getMillis(), value));
    }

    /**
     * Adds to this field in a copy of this TimeOnly.
     * <p>
     * The TimeOnly attached to this property is unchanged by this call.
     * This operation is faster than converting a TimeOnly to a MutableTimeOnly
     * and back again when setting one field. When setting multiple fields,
     * it is generally quicker to make the conversion to MutableTimeOnly.
     * 
     * @param value  the value to add to the field in the copy
     * @return a copy of the TimeOnly with the field value changed
     * @throws IllegalArgumentException if the value isn't valid
     */
    public TimeOnly addToCopy(long value) {
        TimeOnly instant = iInstant;
        return (TimeOnly)instant.withMillis(iField.add(instant.getMillis(), value));
    }

    /**
     * Adds to this field, possibly wrapped, in a copy of this TimeOnly.
     * A wrapped operation only changes this field.
     * Thus 12:59:00 addWrapped one minute goes to 12:00:00.
     * <p>
     * The TimeOnly attached to this property is unchanged by this call.
     * This operation is faster than converting a TimeOnly to a MutableTimeOnly
     * and back again when setting one field. When setting multiple fields,
     * it is generally quicker to make the conversion to MutableTimeOnly.
     * 
     * @param value  the value to add to the field in the copy
     * @return a copy of the TimeOnly with the field value changed
     * @throws IllegalArgumentException if the value isn't valid
     */
    public TimeOnly addWrappedToCopy(int value) {
        TimeOnly instant = iInstant;
        return (TimeOnly)instant.withMillis(iField.addWrapped(instant.getMillis(), value));
    }

    //-----------------------------------------------------------------------
    /**
     * Sets this field in a copy of the TimeOnly.
     * <p>
     * The TimeOnly attached to this property is unchanged by this call.
     * This operation is faster than converting a TimeOnly to a MutableTimeOnly
     * and back again when setting one field. When setting multiple fields,
     * it is generally quicker to make the conversion to MutableTimeOnly.
     * 
     * @param value  the value to set the field in the copy to
     * @return a copy of the TimeOnly with the field value changed
     * @throws IllegalArgumentException if the value isn't valid
     */
    public TimeOnly setCopy(int value) {
        TimeOnly instant = iInstant;
        return (TimeOnly)instant.withMillis(iField.set(instant.getMillis(), value));
    }
    
    /**
     * Sets this field in a copy of the TimeOnly to a parsed text value.
     * <p>
     * The TimeOnly attached to this property is unchanged by this call.
     * This operation is faster than converting a TimeOnly to a MutableTimeOnly
     * and back again when setting one field. When setting multiple fields,
     * it is generally quicker to make the conversion to MutableTimeOnly.
     * 
     * @param text  the text value to set
     * @param locale  optional locale to use for selecting a text symbol
     * @return a copy of the TimeOnly with the field value changed
     * @throws IllegalArgumentException if the text value isn't valid
     */
    public TimeOnly setCopy(String text, Locale locale) {
        TimeOnly instant = iInstant;
        return (TimeOnly)instant.withMillis(iField.set(instant.getMillis(), text, locale));
    }

    /**
     * Sets this field in a copy of the TimeOnly to a parsed text value.
     * <p>
     * The TimeOnly attached to this property is unchanged by this call.
     * This operation is faster than converting a TimeOnly to a MutableTimeOnly
     * and back again when setting one field. When setting multiple fields,
     * it is generally quicker to make the conversion to MutableTimeOnly.
     * 
     * @param text  the text value to set
     * @return a copy of the TimeOnly with the field value changed
     * @throws IllegalArgumentException if the text value isn't valid
     */
    public final TimeOnly setCopy(String text) {
        return setCopy(text, null);
    }

    //-----------------------------------------------------------------------
    /**
     * Rounds to the lowest whole unit of this field on a copy of this TimeOnly.
     *
     * @return a copy of the TimeOnly with the field value changed
     */
    public TimeOnly roundFloorCopy() {
        TimeOnly instant = iInstant;
        return (TimeOnly)instant.withMillis(iField.roundFloor(instant.getMillis()));
    }

    /**
     * Rounds to the highest whole unit of this field on a copy of this TimeOnly.
     *
     * @return a copy of the TimeOnly with the field value changed
     */
    public TimeOnly roundCeilingCopy() {
        TimeOnly instant = iInstant;
        return (TimeOnly)instant.withMillis(iField.roundCeiling(instant.getMillis()));
    }

    /**
     * Rounds to the nearest whole unit of this field on a copy of this TimeOnly,
     * favoring the floor if halfway.
     *
     * @return a copy of the TimeOnly with the field value changed
     */
    public TimeOnly roundHalfFloorCopy() {
        TimeOnly instant = iInstant;
        return (TimeOnly)instant.withMillis(iField.roundHalfFloor(instant.getMillis()));
    }

    /**
     * Rounds to the nearest whole unit of this field on a copy of this TimeOnly,
     * favoring the ceiling if halfway.
     *
     * @return a copy of the TimeOnly with the field value changed
     */
    public TimeOnly roundHalfCeilingCopy() {
        TimeOnly instant = iInstant;
        return (TimeOnly)instant.withMillis(iField.roundHalfCeiling(instant.getMillis()));
    }

    /**
     * Rounds to the nearest whole unit of this field on a copy of this TimeOnly.
     * If halfway, the ceiling is favored over the floor only if it makes this field's value even.
     *
     * @return a copy of the TimeOnly with the field value changed
     */
    public TimeOnly roundHalfEvenCopy() {
        TimeOnly instant = iInstant;
        return (TimeOnly)instant.withMillis(iField.roundHalfEven(instant.getMillis()));
    }

}
