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
import org.joda.time.DurationField;

/**
 * Generic limiting datetime field.
 * <p>
 * This DateTimeField allows specific millisecond boundaries to be applied 
 * to DateTimeFields.
 * <p>
 * LimitDateTimeField is thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
// TODO: Move delete this
// TODO: Also consider ShiftedChronology, for supporting Coptic.
public class LimitDateTimeField extends DecoratedDateTimeField {

    static final long serialVersionUID = -4969295623151287856L;

    /** The lower boundary in millis */
    private final long iLowerBound;
    /** The upper boundary in millis */
    private final long iUpperBound;

    /**
     * Constructor
     * 
     * @param lowerBound  milliseconds to form the lower boundary inclusive
     * @param upperBound  milliseconds to form the upper boundary inclusive
     * @throws IllegalArgumentException if field is null or boundary is invalid
     */
    public LimitDateTimeField(DateTimeField field,
                              long lowerBound, long upperBound) {
        this(field, field.getName(), lowerBound, upperBound);
    }

    /**
     * Constructor
     * 
     * @param name  short, descriptive name, like "secondOfMinute".
     * @param lowerBound  milliseconds to form the lower boundary inclusive
     * @param upperBound  milliseconds to form the upper boundary inclusive
     * @throws IllegalArgumentException if field is null or boundary is invalid
     */
    public LimitDateTimeField(DateTimeField field, String name,
                              long lowerBound, long upperBound) {
        super(field, name);
                
        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException("The lowerBound must be less than the upperBound");
        }
        iLowerBound = lowerBound;
        iUpperBound = upperBound;
    }

    /**
     * Get the amount of fractional units from the specified time instant.
     * 
     * @param instant  the time instant in millis to query.
     * @return the amount of fractional units extracted from the input.
     */
    public int get(long instant) {
        checkBounds(instant, null);
        return getWrappedField().get(instant);
    }

    public String getAsText(long instant, Locale locale) {
        checkBounds(instant, null);
        return getWrappedField().getAsText(instant, locale);
    }

    public String getAsShortText(long instant, Locale locale) {
        checkBounds(instant, null);
        return getWrappedField().getAsShortText(instant, locale);
    }

    /**
     * Add the specified amount of fractional units to the specified time
     * instant. The amount added may be negative.
     * 
     * @param instant  the time instant in millis to update.
     * @param amount  the amount of fractional units to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long instant, int amount) {
        checkBounds(instant, null);
        long result = getWrappedField().add(instant, amount);
        checkBounds(result, "resulting");
        return result;
    }

    /**
     * Add the specified amount of fractional units to the specified time
     * instant. The amount added may be negative.
     * 
     * @param instant  the time instant in millis to update.
     * @param amount  the amount of fractional units to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long instant, long amount) {
        checkBounds(instant, null);
        long result = getWrappedField().add(instant, amount);
        System.out.println(result);
        System.out.println(iLowerBound);
        checkBounds(result, "resulting");
        return result;
    }

    /**
     * Add to the fractional component of the specified time instant,
     * wrapping around within that component if necessary.
     * 
     * @param instant  the time instant in millis to update.
     * @param amount  the amount of fractional units to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapped(long instant, int amount) {
        checkBounds(instant, null);
        long result = getWrappedField().addWrapped(instant, amount);
        checkBounds(result, "resulting");
        return result;
    }

    public int getDifference(long minuendInstant, long subtrahendInstant) {
        checkBounds(minuendInstant, "minuend");
        checkBounds(subtrahendInstant, "subtrahend");
        return getWrappedField().getDifference(minuendInstant, subtrahendInstant);
    }

    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        checkBounds(minuendInstant, "minuend");
        checkBounds(subtrahendInstant, "subtrahend");
        return getWrappedField().getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    /**
     * Set the specified amount of fractional units to the specified time instant.
     * 
     * @param instant  the time instant in millis to update.
     * @param value  value of fractional units to set.
     * @return the updated time instant.
     * @throws IllegalArgumentException if value is too large or too small.
     */
    public long set(long instant, int value) {
        checkBounds(instant, null);
        long result = getWrappedField().set(instant, value);
        checkBounds(result, "resulting");
        return result;
    }

    public long set(long instant, String text, Locale locale) {
        checkBounds(instant, null);
        long result = getWrappedField().set(instant, text, locale);
        checkBounds(result, "resulting");
        return result;
    }

    public boolean isLeap(long instant) {
        return getWrappedField().isLeap(instant);
    }

    public int getLeapAmount(long instant) {
        return getWrappedField().getLeapAmount(instant);
    }

    public DurationField getLeapDurationField() {
        return getWrappedField().getLeapDurationField();
    }

    public long roundFloor(long instant) {
        checkBounds(instant, null);
        long result = getWrappedField().roundFloor(instant);
        checkBounds(result, "resulting");
        return result;
    }

    public long roundCeiling(long instant) {
        checkBounds(instant, null);
        long result = getWrappedField().roundCeiling(instant);
        checkBounds(result, "resulting");
        return result;
    }

    public long roundHalfFloor(long instant) {
        checkBounds(instant, null);
        long result = getWrappedField().roundHalfFloor(instant);
        checkBounds(result, "resulting");
        return result;
    }

    public long roundHalfCeiling(long instant) {
        checkBounds(instant, null);
        long result = getWrappedField().roundHalfCeiling(instant);
        checkBounds(result, "resulting");
        return result;
    }

    public long roundHalfEven(long instant) {
        checkBounds(instant, null);
        long result = getWrappedField().roundHalfEven(instant);
        checkBounds(result, "resulting");
        return result;
    }

    public long remainder(long instant) {
        checkBounds(instant, null);
        long result = getWrappedField().remainder(instant);
        checkBounds(result, "resulting");
        return result;
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

    private void checkBounds(long instant, String desc) {
        if (instant < iLowerBound) {
            throw new BoundsException(desc, true);
        } else if (instant > iUpperBound) {
            throw new BoundsException(desc, false);
        }
    }

    /**
     * Extends IllegalArgumentException such that the exception message is not
     * generated unless it is actually requested.
     */
    private static class BoundsException extends IllegalArgumentException {
        private final boolean iIsLow;

        BoundsException(String desc, boolean isLow) {
            super(desc);
            iIsLow = isLow;
        }

        public String getMessage() {
            StringBuffer buf = new StringBuffer(85);
            buf.append("The");
            String desc = super.getMessage();
            if (desc != null) {
                buf.append(' ');
                buf.append(desc);
            }
            buf.append(" instant is ");
            if (iIsLow) {
                buf.append("below the supported minimum of ");
            } else {
                buf.append("above the supported maximum of ");
            }
            buf.append("TODO");
            return buf.toString();
        }

        public String toString() {
            return "IllegalArgumentException: " + getMessage();
        }
    }

}
