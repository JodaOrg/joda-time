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
package org.joda.time.chrono.gj;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;

/**
 * 
 * @author Brian S O'Neill
 */
class TestGJYearField extends TestGJDateTimeField {
    public TestGJYearField(TestGJChronology chrono) {
        super(DateTimeFieldType.year(), chrono.millisPerYear(), chrono);
    }

    public int get(long millis) {
        return iChronology.gjYearFromMillis(millis);
    }

    public long set(long millis, int value) {
        int[] ymd = iChronology.gjFromMillis(millis);
        millis = iChronology.getTimeOnlyMillis(millis)
            + iChronology.millisFromGJ(value, ymd[1], ymd[2]);
        if (ymd[1] == 2 && ymd[2] == 29 && !iChronology.isLeapYear(value)) {
            millis = iChronology.dayOfYear().add(millis, -1);
        }
        return millis;
    }

    public long add(long millis, long value) {
        return set(millis, (int)(get(millis) + value));
    }

    public boolean isLeap(long millis) {
        return iChronology.isLeapYear(get(millis));
    }

    public int getLeapAmount(long millis) {
        return isLeap(millis) ? 1 : 0;
    }

    public DurationField getLeapDurationField() {
        return iChronology.days();
    }

    public DurationField getRangeDurationField() {
        return null;
    }

    public int getMinimumValue() {
        return -100000000;
    }

    public int getMaximumValue() {
        return 100000000;
    }

    public long roundFloor(long millis) {
        return iChronology.millisFromGJ(get(millis), 1, 1);
    }
}
