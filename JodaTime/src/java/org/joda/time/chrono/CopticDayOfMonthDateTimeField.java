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

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;
import org.joda.time.field.PreciseDurationDateTimeField;

/**
 * Provides time calculations for the day of the month component of time.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
final class CopticDayOfMonthDateTimeField extends PreciseDurationDateTimeField {

    private static final long serialVersionUID = -5441610762799659434L;

    private final BaseGJChronology iChronology;

    /**
     * Restricted constructor.
     */
    CopticDayOfMonthDateTimeField(BaseGJChronology chronology, DurationField days) {
        super(DateTimeFieldType.dayOfMonth(), days);
        iChronology = chronology;
    }

    public int get(long instant) {
        return (iChronology.getDayOfYear(instant) - 1) % 30 + 1;
    }

    public DurationField getRangeDurationField() {
        return iChronology.months();
    }

    public int getMinimumValue() {
        return 1;
    }

    public int getMaximumValue() {
        return 30;
    }

    public int getMaximumValue(long instant) {
		if (((iChronology.getDayOfYear(instant) - 1) / 30) < 12) {
			return 30;
		}
		return iChronology.isLeapYear(iChronology.getYear(instant)) ? 6 : 5;
    }

    public int getMaximumValue(ReadablePartial partial) {
        if (partial.isSupported(DateTimeFieldType.monthOfYear())) {
            // find month
            int month = partial.get(DateTimeFieldType.monthOfYear());
            if (month <= 12) {
                return 30;
            }
            // 13th month, so check year
            if (partial.isSupported(DateTimeFieldType.year())) {
                int year = partial.get(DateTimeFieldType.year());
                return iChronology.isLeapYear(year) ? 6 : 5;
            }
            return 6;
        }
        return 30;
    }

    public int getMaximumValue(ReadablePartial partial, int[] values) {
        int size = partial.size();
        for (int i = 0; i < size; i++) {
            // find month
            if (partial.getFieldType(i) == DateTimeFieldType.monthOfYear()) {
                int month = values[i];
                if (month <= 12) {
                    return 30;
                }
                // 13th month, so check year
                for (int j = 0; j < size; j++) {
                    if (partial.getFieldType(j) == DateTimeFieldType.year()) {
                        int year = values[j];
                        return iChronology.isLeapYear(year) ? 6 : 5;
                    }
                }
                return 6;
            }
        }
        return 30;
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return iChronology.dayOfMonth();
    }
}
