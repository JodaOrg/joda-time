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
package org.joda.time.chrono;

import org.joda.time.DateTimeField;
import org.joda.time.DurationField;

/**
 * Counterpart remainder datetime field to {@link DividedDateTimeField}. The
 * field's unit duration is unchanged, but the range duration is scaled
 * accordingly.
 * <p>
 * RemainderDateTimeField is thread-safe and immutable.
 *
 * @see DividedDateTimeField
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public class RemainderDateTimeField extends DecoratedDateTimeField {

    static final long serialVersionUID = 5708241235177666790L;

    // Shared with DividedDateTimeField.
    final int iDivisor;
    final DurationField iRangeField;

    /**
     * Constructor.
     * 
     * @param field  the field to wrap, like "year()".
     * @param name  short, descriptive name, like "yearOfCentury".
     * @param rangeName  short, descriptive name, like "centuries".
     * @param divisor  divisor, such as 100 years in a century
     * @throws IllegalArgumentException if divisor is less than two
     */
    public RemainderDateTimeField(DateTimeField field,
                                  String name, String rangeName, int divisor) {
        super(field, name);

        if (divisor < 2) {
            throw new IllegalArgumentException("The divisor must be at least 2");
        }

        DurationField rangeField = field.getDurationField();
        if (rangeField == null) {
            iRangeField = null;
        } else {
            iRangeField = new ScaledDurationField(rangeField, rangeName, divisor);
        }

        iDivisor = divisor;
    }

    /**
     * Construct a RemainderDateTimeField that compliments the given
     * DividedDateTimeField.
     *
     * @param dividedField  complimentary divided field, like "century()".
     * @param name  short, descriptive name, like "yearOfCentury".
     */
    public RemainderDateTimeField(DividedDateTimeField dividedField, String name) {
        super(dividedField.getWrappedField(), name);
        iDivisor = dividedField.iDivisor;
        iRangeField = dividedField.iDurationField;
    }

    /**
     * Get the remainder from the specified time instant.
     * 
     * @param instant  the time instant in millis to query.
     * @return the remainder extracted from the input.
     */
    public int get(long instant) {
        int value = getWrappedField().get(instant);
        if (value >= 0) {
            return value % iDivisor;
        } else {
            return (iDivisor - 1) + ((value + 1) % iDivisor);
        }
    }

    /**
     * Add the specified amount to the specified time instant, wrapping around
     * within the remainder range if necessary. The amount added may be
     * negative.
     * 
     * @param instant  the time instant in millis to update.
     * @param amount  the amount to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapped(long instant, int amount) {
        return set(instant, Utils.getWrappedValue(get(instant), amount, 0, iDivisor - 1));
    }

    /**
     * Set the specified amount of remainder units to the specified time instant.
     * 
     * @param instant  the time instant in millis to update.
     * @param value  value of remainder units to set.
     * @return the updated time instant.
     * @throws IllegalArgumentException if value is too large or too small.
     */
    public long set(long instant, int value) {
        Utils.verifyValueBounds(this, value, 0, iDivisor - 1);
        int divided = getDivided(getWrappedField().get(instant));
        return getWrappedField().set(instant, divided * iDivisor + value);
    }

    /**
     * Returns a scaled version of the wrapped field's unit duration field.
     */
    public DurationField getRangeDurationField() {
        return iRangeField;
    }

    /**
     * Get the minimum value for the field, which is always zero.
     * 
     * @return the minimum value of zero.
     */
    public int getMinimumValue() {
        return 0;
    }

    /**
     * Get the maximum value for the field, which is always one less than the
     * divisor.
     * 
     * @return the maximum value
     */
    public int getMaximumValue() {
        return iDivisor - 1;
    }

    public long roundFloor(long instant) {
        return getWrappedField().roundFloor(instant);
    }

    public long roundCeiling(long instant) {
        return getWrappedField().roundCeiling(instant);
    }

    public long roundHalfFloor(long instant) {
        return getWrappedField().roundHalfFloor(instant);
    }

    public long roundHalfCeiling(long instant) {
        return getWrappedField().roundHalfCeiling(instant);
    }

    public long roundHalfEven(long instant) {
        return getWrappedField().roundHalfEven(instant);
    }

    public long remainder(long instant) {
        return getWrappedField().remainder(instant);
    }

    /**
     * Returns the divisor applied, in the field's units.
     * 
     * @return the divisor
     */
    public int getDivisor() {
        return iDivisor;
    }

    private int getDivided(int value) {
        if (value >= 0) {
            return value / iDivisor;
        } else {
            return ((value + 1) / iDivisor) - 1;
        }
    }

}
