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
package org.joda.test.time.chrono.gj;

import org.joda.time.DurationField;

/**
 * 
 * @author Brian S O'Neill
 */
class TestGJMonthOfYearField extends TestGJDateTimeField {
    public TestGJMonthOfYearField(TestGJChronology chrono) {
        super("monthOfYear", "months", chrono.millisPerMonth(), chrono);
    }

    public int get(long millis) {
        return iChronology.gjFromMillis(millis)[1];
    }

    public long set(long millis, int value) {
        long timeOnlyMillis = iChronology.getTimeOnlyMillis(millis);
        int[] ymd = iChronology.gjFromMillis(millis);
        // First set to start of month...
        millis = iChronology.millisFromGJ(ymd[0], value, 1);
        // ...and use dayOfMonth field to check range.
        int maxDay = iChronology.dayOfMonth().getMaximumValue(millis);
        if (ymd[2] > maxDay) {
            ymd[2] = maxDay;
        }
        return timeOnlyMillis + iChronology.millisFromGJ(ymd[0], value, ymd[2]);
    }

    public long add(long millis, long value) {
        int newYear = iChronology.year().get(millis)
            + (int)iChronology.div(value, 12);
        int newMonth = get(millis) + (int)iChronology.mod(value, 12);
        if (newMonth > 12) {
            newYear++;
            newMonth -= 12;
        }
        int newDay = iChronology.dayOfMonth().get(millis);
        millis = iChronology.getTimeOnlyMillis(millis) 
            + iChronology.millisFromGJ(newYear, newMonth, newDay);
        while (get(millis) != newMonth) {
            millis = iChronology.dayOfYear().add(millis, -1);
        }
        return millis;
    }

    public boolean isLeap(long millis) {
        int[] ymd = iChronology.gjFromMillis(millis);
        return ymd[1] == 2 && iChronology.isLeapYear(ymd[0]);
    }

    public int getLeapAmount(long millis) {
        return isLeap(millis) ? 1 : 0;
    }

    public DurationField getLeapDurationField() {
        return iChronology.days();
    }

    public DurationField getRangeDurationField() {
        return iChronology.years();
    }

    public int getMinimumValue() {
        return 1;
    }

    public int getMaximumValue() {
        return 12;
    }

    public long roundFloor(long millis) {
        int[] ymd = iChronology.gjFromMillis(millis);
        return iChronology.millisFromGJ(ymd[0], ymd[1], 1);
    }
}
