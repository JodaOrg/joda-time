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
package org.joda.time.field;

import java.io.Serializable;
import java.util.Locale;
import org.joda.time.DateTimeField;
import org.joda.time.DurationField;
import org.joda.time.partial.PartialInstant;

/**
 * <code>DelegatedDateTimeField</code> delegates each method call to the
 * date time field it wraps.
 * <p>
 * DelegatedDateTimeField is thread-safe and immutable, and its subclasses must
 * be as well.
 *
 * @author Brian S O'Neill
 * @since 1.0
 * @see DecoratedDateTimeField
 */
public class DelegatedDateTimeField extends DateTimeField implements Serializable {

    /** Serialization version */
    private static final long serialVersionUID = -4730164440214502503L;

    /** The DateTimeField being wrapped */
    private final DateTimeField iField;
    /** A desriptive name for the field */
    private final String iName;

    /**
     * Constructor.
     * 
     * @param field  the field being decorated
     */
    protected DelegatedDateTimeField(DateTimeField field) {
        this(field, null);
    }

    /**
     * Constructor.
     * 
     * @param field  the field being decorated
     * @param name  the name of the field
     */
    protected DelegatedDateTimeField(DateTimeField field, String name) {
        super();
        if (field == null) {
            throw new IllegalArgumentException("The field must not be null");
        }
        iField = field;
        iName = name;
    }

    /**
     * Gets the wrapped date time field.
     * 
     * @return the wrapped DateTimeField
     */
    public final DateTimeField getWrappedField() {
        return iField;
    }

    public String getName() {
        return (iName == null) ? iField.getName() : iName;
    }

    public boolean isSupported() {
        return iField.isSupported();
    }

    public boolean isLenient() {
        return iField.isLenient();
    }

    public int get(long instant) {
        return iField.get(instant);
    }

    public String getAsText(long instant, Locale locale) {
        return iField.getAsText(instant, locale);
    }

    public String getAsText(long instant) {
        return iField.getAsText(instant);
    }

    public String getAsText(PartialInstant partial, int fieldValue, Locale locale) {
        return iField.getAsText(partial, fieldValue, locale);
    }

    public String getAsText(PartialInstant partial, Locale locale) {
        return iField.getAsText(partial, locale);
    }

    public String getAsShortText(long instant, Locale locale) {
        return iField.getAsShortText(instant, locale);
    }

    public String getAsShortText(long instant) {
        return iField.getAsShortText(instant);
    }

    public String getAsShortText(PartialInstant partial, int fieldValue, Locale locale) {
        return iField.getAsShortText(partial, fieldValue, locale);
    }

    public String getAsShortText(PartialInstant partial, Locale locale) {
        return iField.getAsShortText(partial, locale);
    }

    public long add(long instant, int value) {
        return iField.add(instant, value);
    }

    public long add(long instant, long value) {
        return iField.add(instant, value);
    }

    public long addWrapped(long instant, int value) {
        return iField.addWrapped(instant, value);
    }

    public int getDifference(long minuendInstant, long subtrahendInstant) {
        return iField.getDifference(minuendInstant, subtrahendInstant);
    }

    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        return iField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    public long set(long instant, int value) {
        return iField.set(instant, value);
    }

    public long set(long instant, String text, Locale locale) {
        return iField.set(instant, text, locale);
    }

    public long set(long instant, String text) {
        return iField.set(instant, text);
    }

    public DurationField getDurationField() {
        return iField.getDurationField();
    }

    public DurationField getRangeDurationField() {
        return iField.getRangeDurationField();
    }

    public boolean isLeap(long instant) {
        return iField.isLeap(instant);
    }

    public int getLeapAmount(long instant) {
        return iField.getLeapAmount(instant);
    }

    public DurationField getLeapDurationField() {
        return iField.getLeapDurationField();
    }

    public int getMinimumValue() {
        return iField.getMinimumValue();
    }

    public int getMinimumValue(long instant) {
        return iField.getMinimumValue(instant);
    }

    public int getMaximumValue() {
        return iField.getMaximumValue();
    }

    public int getMaximumValue(long instant) {
        return iField.getMaximumValue(instant);
    }

    public int getMaximumTextLength(Locale locale) {
        return iField.getMaximumTextLength(locale);
    }

    public int getMaximumShortTextLength(Locale locale) {
        return iField.getMaximumShortTextLength(locale);
    }

    public long roundFloor(long instant) {
        return iField.roundFloor(instant);
    }

    public long roundCeiling(long instant) {
        return iField.roundCeiling(instant);
    }

    public long roundHalfFloor(long instant) {
        return iField.roundHalfFloor(instant);
    }

    public long roundHalfCeiling(long instant) {
        return iField.roundHalfCeiling(instant);
    }

    public long roundHalfEven(long instant) {
        return iField.roundHalfEven(instant);
    }

    public long remainder(long instant) {
        return iField.remainder(instant);
    }

    public String toString() {
        return (iName == null) ? iField.toString() :
            ("DateTimeField[" + iName + ']');
    }

}
