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
import org.joda.time.DateOnly;
import org.joda.time.DateTimeField;
// Import for @link support
import org.joda.time.MutableDateOnly;
import org.joda.time.ReadableInstant;

/**
 * DateOnlyFieldProperty binds a DateOnly to a DateTimeField allowing powerful
 * date functionality to be easily accessed.
 * <p>
 * The simplest use of this class is as an alternative get method, here used to
 * get the year '1972' (as an int) and the month 'December' (as a String).
 * <pre>
 * DateOnly date = new DateOnly(1972, 12, 3);
 * int year = date.year().get();
 * String monthStr = date.month().getAsText();
 * </pre>
 * <p>
 * Methods are also provided that allow date modification. These return new instances
 * of DateOnly - they do not modify the original. The example below yields two
 * independent immutable date objects 20 years apart.
 * <pre>
 * DateOnly date = new DateOnly(1972, 12, 3);
 * DateOnly date20 = date.year().addToCopy(20);
 * </pre>
 * Serious modification of dates (ie. more than just changing one or two fields)
 * should use the {@link MutableDateOnly} class.
 * <p>
 * DateOnlyFieldPropery itself is thread-safe and immutable, as well as the
 * DateOnly being operated on.
 *
 * @author Brian S O'Neill
 */
public class DateOnlyFieldProperty extends AbstractReadableInstantFieldProperty {

    static final long serialVersionUID = -7764886769525930067L;

    /** The instant this property is working against */
    private final DateOnly iInstant;
    /** The field this property is working against */
    private final DateTimeField iField;

    /**
     * Constructor.
     * 
     * @param instant  the instant to set
     * @param field  the field to use
     */
    public DateOnlyFieldProperty(DateOnly instant, DateTimeField field) {
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
    public DateOnly getDateTime() {
        return iInstant;
    }

    //-----------------------------------------------------------------------
    /**
     * Adds to this field in a copy of this DateOnly.
     * <p>
     * The DateOnly attached to this property is unchanged by this call.
     * This operation is faster than converting a DateOnly to a MutableDateOnly
     * and back again when setting one field. When setting multiple fields,
     * it is generally quicker to make the conversion to MutableDateOnly.
     * 
     * @param value  the value to add to the field in the copy
     * @return a copy of the DateOnly with the field value changed
     * @throws IllegalArgumentException if the value isn't valid
     */
    public DateOnly addToCopy(int value) {
        DateOnly instant = iInstant;
        return (DateOnly)instant.toCopy(iField.add(instant.getMillis(), value));
    }

    /**
     * Adds to this field in a copy of this DateOnly.
     * <p>
     * The DateOnly attached to this property is unchanged by this call.
     * This operation is faster than converting a DateOnly to a MutableDateOnly
     * and back again when setting one field. When setting multiple fields,
     * it is generally quicker to make the conversion to MutableDateOnly.
     * 
     * @param value  the value to add to the field in the copy
     * @return a copy of the DateOnly with the field value changed
     * @throws IllegalArgumentException if the value isn't valid
     */
    public DateOnly addToCopy(long value) {
        DateOnly instant = iInstant;
        return (DateOnly)instant.toCopy(iField.add(instant.getMillis(), value));
    }

    /**
     * Adds to this field, possibly wrapped, in a copy of this DateOnly.
     * A wrapped operation only changes this field.
     * Thus 31st January addWrapped one day goes to the 1st January.
     * <p>
     * The DateOnly attached to this property is unchanged by this call.
     * This operation is faster than converting a DateOnly to a MutableDateOnly
     * and back again when setting one field. When setting multiple fields,
     * it is generally quicker to make the conversion to MutableDateOnly.
     * 
     * @param value  the value to add to the field in the copy
     * @return a copy of the DateOnly with the field value changed
     * @throws IllegalArgumentException if the value isn't valid
     */
    public DateOnly addWrappedToCopy(int value) {
        DateOnly instant = iInstant;
        return (DateOnly)instant.toCopy(iField.addWrapped(instant.getMillis(), value));
    }

    //-----------------------------------------------------------------------
    /**
     * Sets this field in a copy of the DateOnly.
     * <p>
     * The DateOnly attached to this property is unchanged by this call.
     * This operation is faster than converting a DateOnly to a MutableDateOnly
     * and back again when setting one field. When setting multiple fields,
     * it is generally quicker to make the conversion to MutableDateOnly.
     * 
     * @param value  the value to set the field in the copy to
     * @return a copy of the DateOnly with the field value changed
     * @throws IllegalArgumentException if the value isn't valid
     */
    public DateOnly setCopy(int value) {
        DateOnly instant = iInstant;
        return (DateOnly)instant.toCopy(iField.set(instant.getMillis(), value));
    }
    
    /**
     * Sets this field in a copy of the DateOnly to a parsed text value.
     * <p>
     * The DateOnly attached to this property is unchanged by this call.
     * This operation is faster than converting a DateOnly to a MutableDateOnly
     * and back again when setting one field. When setting multiple fields,
     * it is generally quicker to make the conversion to MutableDateOnly.
     * 
     * @param text  the text value to set
     * @param locale  optional locale to use for selecting a text symbol
     * @return a copy of the DateOnly with the field value changed
     * @throws IllegalArgumentException if the text value isn't valid
     */
    public DateOnly setCopy(String text, Locale locale) {
        DateOnly instant = iInstant;
        return (DateOnly)instant.toCopy(iField.set(instant.getMillis(), text, locale));
    }

    /**
     * Sets this field in a copy of the DateOnly to a parsed text value.
     * <p>
     * The DateOnly attached to this property is unchanged by this call.
     * This operation is faster than converting a DateOnly to a MutableDateOnly
     * and back again when setting one field. When setting multiple fields,
     * it is generally quicker to make the conversion to MutableDateOnly.
     * 
     * @param text  the text value to set
     * @return a copy of the DateOnly with the field value changed
     * @throws IllegalArgumentException if the text value isn't valid
     */
    public final DateOnly setCopy(String text) {
        return setCopy(text, null);
    }

    //-----------------------------------------------------------------------
    /**
     * Rounds to the lowest whole unit of this field on a copy of this DateOnly.
     *
     * @return a copy of the DateOnly with the field value changed
     */
    public DateOnly roundFloorCopy() {
        DateOnly instant = iInstant;
        return (DateOnly)instant.toCopy(iField.roundFloor(instant.getMillis()));
    }

    /**
     * Rounds to the highest whole unit of this field on a copy of this DateOnly.
     *
     * @return a copy of the DateOnly with the field value changed
     */
    public DateOnly roundCeilingCopy() {
        DateOnly instant = iInstant;
        return (DateOnly)instant.toCopy(iField.roundCeiling(instant.getMillis()));
    }

    /**
     * Rounds to the nearest whole unit of this field on a copy of this DateOnly,
     * favoring the floor if halfway.
     *
     * @return a copy of the DateOnly with the field value changed
     */
    public DateOnly roundHalfFloorCopy() {
        DateOnly instant = iInstant;
        return (DateOnly)instant.toCopy(iField.roundHalfFloor(instant.getMillis()));
    }

    /**
     * Rounds to the nearest whole unit of this field on a copy of this DateOnly,
     * favoring the ceiling if halfway.
     *
     * @return a copy of the DateOnly with the field value changed
     */
    public DateOnly roundHalfCeilingCopy() {
        DateOnly instant = iInstant;
        return (DateOnly)instant.toCopy(iField.roundHalfCeiling(instant.getMillis()));
    }

    /**
     * Rounds to the nearest whole unit of this field on a copy of this DateOnly.
     * If halfway, the ceiling is favored over the floor only if it makes this field's value even.
     *
     * @return a copy of the DateOnly with the field value changed
     */
    public DateOnly roundHalfEvenCopy() {
        DateOnly instant = iInstant;
        return (DateOnly)instant.toCopy(iField.roundHalfEven(instant.getMillis()));
    }

}
