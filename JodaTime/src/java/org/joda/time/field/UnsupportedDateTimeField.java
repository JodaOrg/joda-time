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
import java.util.HashMap;
import java.util.Locale;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;

/**
 * A placeholder implementation to use when a datetime field is not supported.
 * <p>
 * UnsupportedDateTimeField is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class UnsupportedDateTimeField extends DateTimeField implements Serializable {

    /** Serialilzation version */
    private static final long serialVersionUID = -1934618396111902255L;

    /** The cache of unsupported datetime field instances */
    private static HashMap cCache;

    /**
     * Gets an instance of UnsupportedDateTimeField for a specific named field.
     * Names should be of standard format, such as 'monthOfYear' or 'hourOfDay'.
     * The returned instance is cached.
     * 
     * @param type  the type to obtain
     * @return the instance
     * @throws IllegalArgumentException if durationField is null
     */
    public static synchronized UnsupportedDateTimeField getInstance(
            DateTimeFieldType type, DurationField durationField) {

        UnsupportedDateTimeField field;
        if (cCache == null) {
            cCache = new HashMap(7);
            field = null;
        } else {
            field = (UnsupportedDateTimeField)cCache.get(type);
            if (field != null && field.getDurationField() != durationField) {
                field = null;
            }
        }
        if (field == null) {
            field = new UnsupportedDateTimeField(type, durationField);
            cCache.put(type, field);
        }
        return field;
    }

    /** The field type */
    private final DateTimeFieldType iType;
    /** The duration of the datetime field */
    private final DurationField iDurationField;

    /**
     * Constructor.
     * 
     * @param type  the field type
     * @param durationField  the duration to use
     */
    private UnsupportedDateTimeField(DateTimeFieldType type, DurationField durationField) {
        if (type == null || durationField == null) {
            throw new IllegalArgumentException();
        }
        iType = type;
        iDurationField = durationField;
    }

    //-----------------------------------------------------------------------
    // Design note: Simple accessors return a suitable value, but methods
    // intended to perform calculations throw an UnsupportedOperationException.

    public DateTimeFieldType getType() {
        return iType;
    }

    public String getName() {
        return iType.getName();
    }

    /**
     * This field is not supported.
     *
     * @return false always
     */
    public boolean isSupported() {
        return false;
    }

    /**
     * This field is not lenient.
     *
     * @return false always
     */
    public boolean isLenient() {
        return false;
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int get(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public String getAsText(long instant, Locale locale) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public String getAsText(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public String getAsText(ReadablePartial partial, int fieldValue, Locale locale) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public String getAsText(ReadablePartial partial, Locale locale) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public String getAsText(int fieldValue, Locale locale) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public String getAsShortText(long instant, Locale locale) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public String getAsShortText(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public String getAsShortText(ReadablePartial partial, int fieldValue, Locale locale) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public String getAsShortText(ReadablePartial partial, Locale locale) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public String getAsShortText(int fieldValue, Locale locale) {
        throw unsupported();
    }

    /**
     * Delegates to the duration field.
     *
     * @throws UnsupportedOperationException if the duration is unsupported
     */
    public long add(long instant, int value) {
        return getDurationField().add(instant, value);
    }

    /**
     * Delegates to the duration field.
     *
     * @throws UnsupportedOperationException if the duration is unsupported
     */
    public long add(long instant, long value) {
        return getDurationField().add(instant, value);
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int[] add(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long addWrapField(long instant, int value) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int[] addWrapField(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
        throw unsupported();
    }

    /**
     * Delegates to the duration field.
     *
     * @throws UnsupportedOperationException if the duration is unsupported
     */
    public int getDifference(long minuendInstant, long subtrahendInstant) {
        return getDurationField().getDifference(minuendInstant, subtrahendInstant);
    }

    /**
     * Delegates to the duration field.
     *
     * @throws UnsupportedOperationException if the duration is unsupported
     */
    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        return getDurationField().getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long set(long instant, int value) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int[] set(ReadablePartial instant, int fieldIndex, int[] values, int newValue) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long set(long instant, String text, Locale locale) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long set(long instant, String text) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int[] set(ReadablePartial instant, int fieldIndex, int[] values, String text, Locale locale) {
        throw unsupported();
    }

    /**
     * Even though this DateTimeField is unsupported, the duration field might
     * be supported.
     *
     * @return a possibly supported DurationField
     */
    public DurationField getDurationField() {
        return iDurationField;
    }

    /**
     * Always returns null.
     *
     * @return null always
     */
    public DurationField getRangeDurationField() {
        return null;
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public boolean isLeap(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int getLeapAmount(long instant) {
        throw unsupported();
    }

    /**
     * Always returns null.
     *
     * @return null always
     */
    public DurationField getLeapDurationField() {
        return null;
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int getMinimumValue() {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int getMinimumValue(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int getMinimumValue(ReadablePartial instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int getMinimumValue(ReadablePartial instant, int[] values) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int getMaximumValue() {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int getMaximumValue(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int getMaximumValue(ReadablePartial instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int getMaximumValue(ReadablePartial instant, int[] values) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int getMaximumTextLength(Locale locale) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int getMaximumShortTextLength(Locale locale) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long roundFloor(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long roundCeiling(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long roundHalfFloor(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long roundHalfCeiling(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long roundHalfEven(long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long remainder(long instant) {
        throw unsupported();
    }

    //------------------------------------------------------------------------
    /**
     * Get a suitable debug string.
     * 
     * @return debug string
     */
    public String toString() {
        return "UnsupportedDateTimeField";
    }

    /**
     * Ensure proper singleton serialization
     */
    private Object readResolve() {
        return getInstance(iType, iDurationField);
    }

    private UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException(iType + " field is unsupported");
    }

}
