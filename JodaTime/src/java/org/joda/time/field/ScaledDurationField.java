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

import org.joda.time.DurationField;

/**
 * Scales a DurationField such that it's unit millis becomes larger in
 * magnitude.
 * <p>
 * ScaledDurationField is thread-safe and immutable.
 *
 * @see PreciseDurationField
 *
 * @author Brian S O'Neill
 */
public class ScaledDurationField extends DecoratedDurationField {

    private static final long serialVersionUID = -3205227092378684157L;

    private final int iScalar;

    /**
     * Constructor
     * 
     * @param field  the field to wrap, like "year()".
     * @param name  short, descriptive name, like "centuries".
     * @param scalar  scalar, such as 100 years in a century
     * @throws IllegalArgumentException if scalar is zero or one.
     */
    public ScaledDurationField(DurationField field, String name, int scalar) {
        super(field, name);
        if (scalar == 0 || scalar == 1) {
            throw new IllegalArgumentException("The scalar must not be 0 or 1");
        }
        iScalar = scalar;
    }

    public int getValue(long duration) {
        return getWrappedField().getValue(duration) / iScalar;
    }

    public long getValueAsLong(long duration) {
        return getWrappedField().getValueAsLong(duration) / iScalar;
    }

    public int getValue(long duration, long instant) {
        return getWrappedField().getValue(duration, instant) / iScalar;
    }

    public long getValueAsLong(long duration, long instant) {
        return getWrappedField().getValueAsLong(duration, instant) / iScalar;
    }

    public long getMillis(int value) {
        return getWrappedField().getMillis(value * iScalar);
    }

    public long getMillis(long value) {
        return getWrappedField().getMillis(value * iScalar);
    }

    public long getMillis(int value, long instant) {
        return getWrappedField().getMillis(value * iScalar, instant);
    }

    public long getMillis(long value, long instant) {
        return getWrappedField().getMillis(value * iScalar, instant);
    }

    public long add(long instant, int value) {
        return getWrappedField().add(instant, value * iScalar);
    }

    public long add(long instant, long value) {
        return getWrappedField().add(instant, value * iScalar);
    }

    public int getDifference(long minuendInstant, long subtrahendInstant) {
        return getWrappedField().getDifference(minuendInstant, subtrahendInstant) / iScalar;
    }

    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        return getWrappedField().getDifferenceAsLong(minuendInstant, subtrahendInstant) / iScalar;
    }

    public long getUnitMillis() {
        return getWrappedField().getUnitMillis() * iScalar;
    }

    /**
     * Returns the scalar applied, in the field's units.
     * 
     * @return the scalar
     */
    public int getScalar() {
        return iScalar;
    }
}
