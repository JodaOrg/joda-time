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
 * Divides a DateTimeField such that the retrieved values are reduced by a
 * fixed divisor. The field's unit duration is scaled accordingly, but the
 * range duration is unchanged.
 * <p>
 * DividedDateTimeField is thread-safe and immutable.
 *
 * @see RemainderDateTimeField
 * 
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public class DividedDateTimeField extends DecoratedDateTimeField {

    static final long serialVersionUID = 8318475124230605365L;

    // Shared with RemainderDateTimeField.
    final int iDivisor;
    final DurationField iDurationField;

    private final int iMin;
    private final int iMax;

    /**
     * Constructor.
     * 
     * @param field  the field to wrap, like "year()".
     * @param name  short, descriptive name, like "century".
     * @param durationName  short, descriptive name, like "centuries".
     * @param divisor  divisor, such as 100 years in a century
     * @throws IllegalArgumentException if divisor is less than two
     */
    public DividedDateTimeField(DateTimeField field,
                                String name, String durationName, int divisor) {
        super(field, name);
                
        if (divisor < 2) {
            throw new IllegalArgumentException("The divisor must be at least 2");
        }

        DurationField unitField = field.getDurationField();
        if (unitField == null) {
            iDurationField = null;
        } else {
            iDurationField = new ScaledDurationField(unitField, durationName, divisor);
        }

        iDivisor = divisor;

        int i = field.getMinimumValue();
        int min = (i >= 0) ? i / divisor : ((i + 1) / divisor - 1);

        int j = field.getMaximumValue();
        int max = (j >= 0) ? j / divisor : ((j + 1) / divisor - 1);

        iMin = min;
        iMax = max;
    }

    /**
     * Construct a DividedDateTimeField that compliments the given
     * RemainderDateTimeField.
     *
     * @param remainderField  complimentary remainder field, like "yearOfCentury()".
     * @param name  short, descriptive name, like "century".
     */
    public DividedDateTimeField(RemainderDateTimeField remainderField, String name) {
        super(remainderField.getWrappedField(), name);
        int divisor = iDivisor = remainderField.iDivisor;
        iDurationField = remainderField.iRangeField;

        DateTimeField field = getWrappedField();
        int i = field.getMinimumValue();
        int min = (i >= 0) ? i / divisor : ((i + 1) / divisor - 1);

        int j = field.getMaximumValue();
        int max = (j >= 0) ? j / divisor : ((j + 1) / divisor - 1);

        iMin = min;
        iMax = max;
    }

    /**
     * Get the amount of scaled units from the specified time instant.
     * 
     * @param instant  the time instant in millis to query.
     * @return the amount of scaled units extracted from the input.
     */
    public int get(long instant) {
        int value = getWrappedField().get(instant);
        if (value >= 0) {
            return value / iDivisor;
        } else {
            return ((value + 1) / iDivisor) - 1;
        }
    }

    /**
     * Add the specified amount of scaled units to the specified time
     * instant. The amount added may be negative.
     * 
     * @param instant  the time instant in millis to update.
     * @param amount  the amount of scaled units to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long instant, int amount) {
        return getWrappedField().add(instant, amount * iDivisor);
    }

    /**
     * Add the specified amount of scaled units to the specified time
     * instant. The amount added may be negative.
     * 
     * @param instant  the time instant in millis to update.
     * @param amount  the amount of scaled units to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long instant, long amount) {
        return getWrappedField().add(instant, amount * iDivisor);
    }

    /**
     * Add to the scaled component of the specified time instant,
     * wrapping around within that component if necessary.
     * 
     * @param instant  the time instant in millis to update.
     * @param amount  the amount of scaled units to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapped(long instant, int amount) {
        return set(instant, Utils.getWrappedValue(get(instant), amount, iMin, iMax));
    }

    public int getDifference(long minuendInstant, long subtrahendInstant) {
        return getWrappedField().getDifference(minuendInstant, subtrahendInstant) / iDivisor;
    }

    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        return getWrappedField().getDifferenceAsLong(minuendInstant, subtrahendInstant) / iDivisor;
    }

    /**
     * Set the specified amount of scaled units to the specified time instant.
     * 
     * @param instant  the time instant in millis to update.
     * @param value  value of scaled units to set.
     * @return the updated time instant.
     * @throws IllegalArgumentException if value is too large or too small.
     */
    public long set(long instant, int value) {
        Utils.verifyValueBounds(this, value, iMin, iMax);
        int remainder = getRemainder(getWrappedField().get(instant));
        return getWrappedField().set(instant, value * iDivisor + remainder);
    }

    /**
     * Returns a scaled version of the wrapped field's unit duration field.
     */
    public DurationField getDurationField() {
        return iDurationField;
    }

    /**
     * Get the minimum value for the field.
     * 
     * @return the minimum value
     */
    public int getMinimumValue() {
        return iMin;
    }

    /**
     * Get the maximum value for the field.
     * 
     * @return the maximum value
     */
    public int getMaximumValue() {
        return iMax;
    }

    public long roundFloor(long instant) {
        DateTimeField field = getWrappedField();
        return field.roundFloor(field.set(instant, get(instant) * iDivisor));
    }

    public long remainder(long instant) {
        return set(instant, get(getWrappedField().remainder(instant)));
    }

    /**
     * Returns the divisor applied, in the field's units.
     * 
     * @return the divisor
     */
    public int getDivisor() {
        return iDivisor;
    }

    private int getRemainder(int value) {
        if (value >= 0) {
            return value % iDivisor;
        } else {
            return (iDivisor - 1) + ((value + 1) % iDivisor);
        }
    }

}
