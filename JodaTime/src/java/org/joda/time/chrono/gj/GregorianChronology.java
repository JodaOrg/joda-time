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

/**
 * 
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 */
final class GregorianChronology extends ProlepticChronology {

    static final long serialVersionUID = 3691407383323710523L;

    GregorianChronology(int minDaysInFirstWeek) {
        super(minDaysInFirstWeek);
    }

    public long getGregorianJulianCutoverMillis() {
        return Long.MIN_VALUE;
    }
    
    public boolean isLeapYear(int year) {
        return ((year & 3) == 0) && ((year % 100) != 0 || (year % 400) == 0);
    }

    protected long calculateFirstDayOfYearMillis(int year) {
        // Calculate relative to 2000 as that is on a 400 year boundary
        // and that makes the sum easier
        int relativeYear = year - 2000;
        // Initial value is just temporary.
        int leapYears = relativeYear / 100;
        if (relativeYear <= 0) {
            // Add 3 before shifting right since /4 and >>2 behave differently
            // on negative numbers. When the expression is written as
            // (relativeYear / 4) - (relativeYear / 100) + (relativeYear / 400),
            // it works for both positive and negative values, except this optimization
            // eliminates two divisions.
            leapYears = ((relativeYear + 3) >> 2) - leapYears + ((leapYears + 3) >> 2);
        } else {
            leapYears = (relativeYear >> 2) - leapYears + (leapYears >> 2);
            // For post 2000 an adjustment is needed as jan1st is before leap day
            if (!isLeapYear(year)) {
                leapYears++;
            }
        }
        
        long millis = (relativeYear * 365L + leapYears)
            * (long)DateTimeConstants.MILLIS_PER_DAY;
        
        // Previous line was reduced from this to eliminate a multiplication.
        // millis = ((relativeYear - leapYears) * 365L + leapYears * 366) * MILLIS_PER_DAY;
        // (x - y)*c + y*(c + 1) => x*c - y*c + y*c + y => x*c + y
        
        return millis + MILLIS_1970_TO_2000;
    }

    protected int getMinYear() {
        // The lowest year that can be fully supported.
        return -292275054;
    }

    protected int getMaxYear() {
        // The highest year that can be fully supported.
        return 292278993;
    }

    protected long getRoughMillisPerYear() {
        return (long) (365.2425 * DateTimeConstants.MILLIS_PER_DAY);
    }

    protected long getRoughMillisPerMonth() {
        return (long) (365.2425 * DateTimeConstants.MILLIS_PER_DAY / 12);
    }
}
