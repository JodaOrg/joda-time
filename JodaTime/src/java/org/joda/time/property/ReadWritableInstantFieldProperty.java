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
import org.joda.time.DateTimeField;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadWritableInstant;

/**
 * ReadWritableInstantFieldProperty binds a ReadWritableInstant to a
 * DateTimeField allowing powerful datetime functionality to be easily
 * accessed.
 * <p>
 * The example below shows how to use the property to change the value of a
 * MutableDateTime object.
 * <pre>
 * MutableDateTime dt = new MutableDateTime(1972, 12, 3, 0, 0, 0, 0);
 * dt.year().add(20);
 * dt.second().roundFloor();
 * dt.minute().set(10);
 * </pre>
 * <p>
 * ReadWritableInstantFieldPropery itself is thread-safe and immutable, but the
 * ReadWritableInstant being operated on may not be thread-safe.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public class ReadWritableInstantFieldProperty extends AbstractReadableInstantFieldProperty {

    static final long serialVersionUID = -4481126543819298617L;

    /** The instant this property is working against */
    private final ReadWritableInstant iInstant;
    /** The field this property is working against */
    private final DateTimeField iField;

    /**
     * Constructor.
     * 
     * @param instant  the instant to set
     * @param field  the field to use
     */
    public ReadWritableInstantFieldProperty(ReadWritableInstant instant, DateTimeField field) {
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

    //-----------------------------------------------------------------------
    /**
     * Adds a value to the millis value.
     * 
     * @param value  the value to add
     * @see DateTimeField#add(long,int)
     */
    public void add(int value) {
        iInstant.setMillis(getField().add(iInstant.getMillis(), value));
    }

    /**
     * Adds a value to the millis value.
     * 
     * @param value  the value to add
     * @see DateTimeField#add(long,long)
     */
    public void add(long value) {
        iInstant.setMillis(getField().add(iInstant.getMillis(), value));
    }

    /**
     * Adds a value, possibly wrapped, to the millis value.
     * 
     * @param value  the value to add
     * @see DateTimeField#addWrapped
     */
    public void addWrapped(int value) {
        iInstant.setMillis(getField().addWrapped(iInstant.getMillis(), value));
    }

    //-----------------------------------------------------------------------
    /**
     * Sets a value.
     * 
     * @param value  the value to set.
     * @see DateTimeField#set(long,int)
     */
    public void set(int value) {
        iInstant.setMillis(getField().set(iInstant.getMillis(), value));
    }

    /**
     * Sets a text value.
     * 
     * @param text  the text value to set
     * @param locale  optional locale to use for selecting a text symbol
     * @throws IllegalArgumentException if the text value isn't valid
     * @see DateTimeField#set(long,java.lang.String,java.util.Locale)
     */
    public void set(String text, Locale locale) {
        iInstant.setMillis(getField().set(iInstant.getMillis(), text, locale));
    }

    /**
     * Sets a text value.
     * 
     * @param text  the text value to set
     * @throws IllegalArgumentException if the text value isn't valid
     * @see DateTimeField#set(long,java.lang.String)
     */
    public final void set(String text) {
        set(text, null);
    }

    //-----------------------------------------------------------------------
    /**
     * Round to the lowest whole unit of this field.
     *
     * @see DateTimeField#roundFloor
     */
    public void roundFloor() {
        iInstant.setMillis(getField().roundFloor(iInstant.getMillis()));
    }

    /**
     * Round to the highest whole unit of this field.
     *
     * @see DateTimeField#roundCeiling
     */
    public void roundCeiling() {
        iInstant.setMillis(getField().roundCeiling(iInstant.getMillis()));
    }

    /**
     * Round to the nearest whole unit of this field, favoring the floor if
     * halfway.
     *
     * @see DateTimeField#roundHalfFloor
     */
    public void roundHalfFloor() {
        iInstant.setMillis(getField().roundHalfFloor(iInstant.getMillis()));
    }

    /**
     * Round to the nearest whole unit of this field, favoring the ceiling if
     * halfway.
     *
     * @see DateTimeField#roundHalfCeiling
     */
    public void roundHalfCeiling() {
        iInstant.setMillis(getField().roundHalfCeiling(iInstant.getMillis()));
    }

    /**
     * Round to the nearest whole unit of this field. If halfway, the ceiling
     * is favored over the floor only if it makes this field's value even.
     *
     * @see DateTimeField#roundHalfEven
     */
    public void roundHalfEven() {
        iInstant.setMillis(getField().roundHalfEven(iInstant.getMillis()));
    }

}
