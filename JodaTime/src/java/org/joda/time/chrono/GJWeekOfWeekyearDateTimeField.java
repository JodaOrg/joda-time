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
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;
import org.joda.time.field.PreciseDurationDateTimeField;

/**
 * Provides time calculations for the week of a week based year component of time.
 *
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @version 1.0
 * @since 1.0
 */
final class GJWeekOfWeekyearDateTimeField extends PreciseDurationDateTimeField {

    private static final long serialVersionUID = -1587436826395135328L;

    private final BaseGJChronology iChronology;

    /**
     * Restricted constructor
     */
    GJWeekOfWeekyearDateTimeField(BaseGJChronology chronology, DurationField weeks) {
        super(DateTimeFieldType.weekOfWeekyear(), weeks);
        iChronology = chronology;
    }

    /**
     * Get the week of a week based year component of the specified time instant.
     * 
     * @see org.joda.time.DateTimeField#get(long)
     * @param instant  the time instant in millis to query.
     * @return the week of the year extracted from the input.
     */
    public int get(long instant) {
        return iChronology.getWeekOfWeekyear(instant);
    }

    public DurationField getRangeDurationField() {
        return iChronology.weekyears();
    }

    // 1970-01-01 is day of week 4, Thursday. The rounding methods need to
    // apply a corrective alignment since weeks begin on day of week 1, Monday.

    public long roundFloor(long instant) {
        return super.roundFloor(instant + 3 * DateTimeConstants.MILLIS_PER_DAY)
            - 3 * DateTimeConstants.MILLIS_PER_DAY;
    }

    public long roundCeiling(long instant) {
        return super.roundCeiling(instant + 3 * DateTimeConstants.MILLIS_PER_DAY)
            - 3 * DateTimeConstants.MILLIS_PER_DAY;
    }

    public long remainder(long instant) {
        return super.remainder(instant + 3 * DateTimeConstants.MILLIS_PER_DAY);
    }

    public int getMinimumValue() {
        return 1;
    }

    public int getMaximumValue() {
        return 53;
    }

    public int getMaximumValue(long instant) {
        int weekyear = iChronology.getWeekyear(instant);
        return iChronology.getWeeksInYear(weekyear);
    }

    public int getMaximumValue(ReadablePartial instant) {
        if (instant.isSupported(iChronology.weekyear())) {
            int weekyear = instant.get(iChronology.weekyear());
            return iChronology.getWeeksInYear(weekyear);
        }
        return 53;
    }

    public int getMaximumValue(ReadablePartial instant, int[] values) {
        DateTimeField[] fields = instant.getFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] == iChronology.weekyear()) {
                int weekyear = values[i];
                return iChronology.getWeeksInYear(weekyear);
            }
        }
        return 53;
    }

    protected int getMaximumValueForSet(long instant, int value) {
        return value > 52 ? getMaximumValue(instant) : 52;
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return iChronology.weekOfWeekyear();
    }
}
