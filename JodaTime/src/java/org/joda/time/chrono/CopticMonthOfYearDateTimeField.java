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
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.field.PreciseDurationDateTimeField;

/**
 * 
 *
 * @author Brian S O'Neill
 */
final class CopticMonthOfYearDateTimeField extends PreciseDurationDateTimeField {

    private static final long serialVersionUID = 7741038885247700323L;

    private final BaseGJChronology iChronology;

    /**
     * Restricted constructor.
     */
    CopticMonthOfYearDateTimeField(BaseGJChronology chronology, DurationField months) {
        super(DateTimeFieldType.monthOfYear(), months);
        iChronology = chronology;
    }

    public int get(long instant) {
        return (iChronology.getDayOfYear(instant) - 1) / 30 + 1;
    }

    public long set(long instant, int value) {
        instant = super.set(instant, value);
        if (value == 13) {
            int day = iChronology.getDayOfYear(instant);
            if (day < 30) {
                // Move back a few days to the end of the 13th "month".
                instant -= (long)DateTimeConstants.MILLIS_PER_DAY * day;
            }
        }
        return instant;
    }

    public DurationField getRangeDurationField() {
        return iChronology.years();
    }

    public boolean isLeap(long instant) {
        return get(instant) > 12 && iChronology.isLeapYear(iChronology.getYear(instant));
    }

    public int getLeapAmount(long instant) {
        return isLeap(instant) ? 1 : 0;
    }

    public DurationField getLeapDurationField() {
        return iChronology.days();
    }

    public int getMinimumValue() {
        return 1;
    }

    public int getMaximumValue() {
        return 13;
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return iChronology.monthOfYear();
    }

}
