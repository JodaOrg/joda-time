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

package org.joda.time.chrono.gj;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;

/**
 * Provides time calculations for the year of era component of time.
 * 
 * @author Brian S O'Neill
 */
final class GJYearOfEraDateTimeField extends DateTimeField {
    private final ProlepticChronology iChronology;

    /**
     * Restricted constructor
     */
    GJYearOfEraDateTimeField(ProlepticChronology chronology) {
        super("yearOfEra");
        iChronology = chronology;
    }

    /**
     * Get the year of era component of the specified time instant.
     * 
     * @param millis  the time instant in millis to query.
     * @return the year of era extracted from the input.
     */
    public int get(long millis) {
        int year = iChronology.year().get(millis);
        if (year <= 0) {
            year = 1 - year;
        }
        return year;
    }

    /**
     * Add the specified year to the specified time instant.
     * The amount added may be negative.
     * 
     * @param millis  the time instant in millis to update.
     * @param years  the years to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long millis, int years) {
        return iChronology.year().add(millis, years);
    }

    public long add(long millis, long years) {
        return addLong(millis, years);
    }

    /**
     * Add to the year component of the specified time instant
     * wrapping around within that component if necessary.
     * 
     * @param millis  the time instant in millis to update.
     * @param years  the years to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapped(long millis, int years) {
        return iChronology.year().addWrapped(millis, years);
    }

    public long getDifference(long minuendMillis, long subtrahendMillis) {
        return iChronology.year().getDifference(minuendMillis, subtrahendMillis);
    }

    /**
     * Set the year component of the specified time instant.
     * 
     * @param millis  the time instant in millis to update.
     * @param year  the year (0,292278994) to update the time to.
     * @return the updated time instant.
     * @throws IllegalArgumentException  if year is invalid.
     */
    public long set(long millis, int year) {
        super.verifyValueBounds(year, 1, iChronology.getMaxYear());
        if (iChronology.era().get(millis) == DateTimeConstants.BCE) {
            return iChronology.year().set(millis, 1 - year);
        } else {
            return iChronology.year().set(millis, year);
        }
    }

    public long getUnitMillis() {
        return iChronology.getRoughMillisPerYear();
    }

    public long getRangeMillis() {
        return Long.MAX_VALUE;
    }

    public int getMinimumValue() {
        return 1;
    }

    public int getMaximumValue() {
        return iChronology.getMaxYear();
    }

    public long roundFloor(long millis) {
        return iChronology.year().roundFloor(millis);
    }

    public long roundCeiling(long millis) {
        return iChronology.year().roundCeiling(millis);
    }

    public long remainder(long millis) {
        return iChronology.year().remainder(millis);
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return iChronology.yearOfEra();
    }
}
