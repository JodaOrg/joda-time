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

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.field.DecoratedDateTimeField;
import org.joda.time.field.FieldUtils;

/**
 * Provides time calculations for the year of era component of time.
 * 
 * @author Brian S O'Neill
 */
final class GJYearOfEraDateTimeField extends DecoratedDateTimeField {

    static final long serialVersionUID = -5961050944769862059L;

    private final AbstractGJChronology iChronology;

    /**
     * Restricted constructor.
     */
    GJYearOfEraDateTimeField(DateTimeField yearField, AbstractGJChronology chronology) {
        super(yearField, "yearOfEra");
        iChronology = chronology;
    }

    /**
     * Get the year of era component of the specified time instant.
     * 
     * @param instant  the time instant in millis to query.
     * @return the year of era extracted from the input.
     */
    public int get(long instant) {
        int year = getWrappedField().get(instant);
        if (year <= 0) {
            year = 1 - year;
        }
        return year;
    }

    /**
     * Add the specified year to the specified time instant.
     * The amount added may be negative.
     * 
     * @param instant  the time instant in millis to update.
     * @param years  the years to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long instant, int years) {
        return getWrappedField().add(instant, years);
    }

    public long add(long instant, long years) {
        return getWrappedField().add(instant, years);
    }

    /**
     * Add to the year component of the specified time instant
     * wrapping around within that component if necessary.
     * 
     * @param instant  the time instant in millis to update.
     * @param years  the years to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapped(long instant, int years) {
        return getWrappedField().addWrapped(instant, years);
    }

    public int getDifference(long minuendInstant, long subtrahendInstant) {
        return getWrappedField().getDifference(minuendInstant, subtrahendInstant);
    }

    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        return getWrappedField().getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    /**
     * Set the year component of the specified time instant.
     * 
     * @param instant  the time instant in millis to update.
     * @param year  the year (0,292278994) to update the time to.
     * @return the updated time instant.
     * @throws IllegalArgumentException  if year is invalid.
     */
    public long set(long instant, int year) {
        FieldUtils.verifyValueBounds(this, year, 1, getMaximumValue());
        if (iChronology.getYear(instant) <= 0) {
            year = 1 - year;
        }
        return super.set(instant, year);
    }

    public int getMinimumValue() {
        return 1;
    }

    public int getMaximumValue() {
        return getWrappedField().getMaximumValue();
    }

    public long roundFloor(long instant) {
        return getWrappedField().roundFloor(instant);
    }

    public long roundCeiling(long instant) {
        return getWrappedField().roundCeiling(instant);
    }

    public long remainder(long instant) {
        return getWrappedField().remainder(instant);
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return iChronology.yearOfEra();
    }
}
