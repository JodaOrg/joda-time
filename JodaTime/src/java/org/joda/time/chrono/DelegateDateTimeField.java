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
 * <code>DelegateDateTimeField</code> delegates each method call to the
 * date time field it wraps.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public abstract class DelegateDateTimeField extends DateTimeField {

    private static DateTimeField getField(DateTimeField field) {
        if (field == null) {
            throw new IllegalArgumentException("The field must not be null");
        }
        return field;
    }

    /** The DateTimeField being wrapped */
    private final DateTimeField iField;

    protected DelegateDateTimeField(DateTimeField field) {
        super(getField(field).getName());
        iField = field;
    }

    protected DelegateDateTimeField(String name, DateTimeField field) {
        super(name);
        iField = getField(field);
    }

    /**
     * Gets the wrapped date time field.
     * 
     * @return the wrapped DateTimeField
     */
    protected DateTimeField getDateTimeField() {
        return iField;
    }

    public int get(long millis) {
        return iField.get(millis);
    }

    public String getAsText(long millis, Locale locale) {
        return iField.getAsText(millis, locale);
    }

    public String getAsShortText(long millis, Locale locale) {
        return iField.getAsShortText(millis, locale);
    }

    public long add(long millis, int value) {
        return iField.add(millis, value);
    }

    public long add(long millis, long value) {
        return iField.add(millis, value);
    }

    public long addWrapped(long millis, int value) {
        return iField.addWrapped(millis, value);
    }

    public long getDifference(long minuendMillis, long subtrahendMillis) {
        return iField.getDifference(minuendMillis, subtrahendMillis);
    }

    public long set(long millis, int value) {
        return iField.set(millis, value);
    }

    public long set(long millis, String text, Locale locale) {
        return iField.set(millis, text, locale);
    }

    public boolean isLeap(long millis) {
        return iField.isLeap(millis);
    }

    public int getLeapAmount(long millis) {
        return iField.getLeapAmount(millis);
    }

    public long getUnitMillis() {
        return iField.getUnitMillis();
    }

    public long getRangeMillis() {
        return iField.getRangeMillis();
    }

    public int getMinimumValue() {
        return iField.getMinimumValue();
    }

    public int getMinimumValue(long millis) {
        return iField.getMinimumValue(millis);
    }

    public int getMaximumValue() {
        return iField.getMaximumValue();
    }

    public int getMaximumValue(long millis) {
        return iField.getMaximumValue(millis);
    }

    public int getMaximumTextLength(Locale locale) {
        return iField.getMaximumTextLength(locale);
    }

    public int getMaximumShortTextLength(Locale locale) {
        return iField.getMaximumShortTextLength(locale);
    }

    public long roundFloor(long millis) {
        return iField.roundFloor(millis);
    }

    public long roundCeiling(long millis) {
        return iField.roundCeiling(millis);
    }

    public long roundHalfFloor(long millis) {
        return iField.roundHalfFloor(millis);
    }

    public long roundHalfCeiling(long millis) {
        return iField.roundHalfCeiling(millis);
    }

    public long roundHalfEven(long millis) {
        return iField.roundHalfEven(millis);
    }

    public long remainder(long millis) {
        return iField.remainder(millis);
    }

    public String toString() {
        return iField.toString();
    }
}
