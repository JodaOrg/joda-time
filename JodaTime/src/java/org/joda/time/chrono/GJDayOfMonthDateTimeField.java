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
import org.joda.time.field.PreciseDurationDateTimeField;
import org.joda.time.partial.PartialInstant;

/**
 * Provides time calculations for the day of the month component of time.
 *
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
final class GJDayOfMonthDateTimeField extends PreciseDurationDateTimeField {

    static final long serialVersionUID = -4677223814028011723L;

    private final AbstractGJChronology iChronology;

    /**
     * Restricted constructor.
     */
    GJDayOfMonthDateTimeField(AbstractGJChronology chronology, DurationField days) {
        super("dayOfMonth", days);
        iChronology = chronology;
    }

    /**
     * Get the day of the month component of the specified time instant.
     * 
     * @param instant  the time instant in millis to query.
     * @return the day of the month extracted from the input.
     */
    public int get(long instant) {
        return iChronology.getDayOfMonth(instant);
    }

    public DurationField getRangeDurationField() {
        return iChronology.months();
    }

    public int getMinimumValue() {
        return 1;
    }

    public int getMaximumValue() {
        return 31;
    }

    public int getMaximumValue(long instant) {
        int thisYear = iChronology.getYear(instant);
        int thisMonth = iChronology.getMonthOfYear(instant, thisYear);
        return iChronology.getDaysInYearMonth(thisYear, thisMonth);
    }

    public int getMaximumValue(PartialInstant instant) {
        if (instant.isSupported(iChronology.monthOfYear())) {
            int month = instant.get(iChronology.monthOfYear());
            if (instant.isSupported(iChronology.year())) {
                int year = instant.get(iChronology.year());
                return iChronology.getDaysInYearMonth(year, month);
            }
            return iChronology.getDaysInMonthMax(month);
        }
        return 31;
    }

    public int getMaximumValue(PartialInstant instant, int[] values) {
        DateTimeField[] fields = instant.getFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] == iChronology.monthOfYear()) {
                int month = values[i];
                for (int j = 0; j < fields.length; j++) {
                    if (fields[j] == iChronology.year()) {
                        int year = values[j];
                        return iChronology.getDaysInYearMonth(year, month);
                    }
                }
                return iChronology.getDaysInMonthMax(month);
            }
        }
        return 31;
    }

    protected int getMaximumValueForSet(long instant, int value) {
        return value > 28 ? getMaximumValue(instant) : 28;
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return iChronology.dayOfMonth();
    }
}
