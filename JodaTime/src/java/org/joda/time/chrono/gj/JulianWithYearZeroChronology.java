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
 * JulianWithZeroChronology includes year zero to simplify implementation.
 * 
 * @author Guy Allard
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 */
final class JulianWithYearZeroChronology extends ProlepticChronology {
    JulianWithYearZeroChronology(int minDaysInFirstWeek) {
        super(minDaysInFirstWeek);
    }

    public long getGregorianJulianCutoverMillis() {
        return Long.MAX_VALUE;
    }

    public boolean isLeapYear(int year) {
        return (year & 3) == 0;
    }

    protected long calculateFirstDayOfYearMillis(int year) {
        // Java epoch is 1970-01-01 Gregorian which is 1969-12-19 Julian.
        // Calculate relative to the nearest leap year and account for the
        // difference later.

        int relativeYear = year - 1968;
        int leapYears;
        if (relativeYear <= 0) {
            // Add 3 before shifting right since /4 and >>2 behave differently
            // on negative numbers.
            leapYears = (relativeYear + 3) >> 2;
        } else {
            leapYears = relativeYear >> 2;
            // For post 1968 an adjustment is needed as jan1st is before leap day
            if (!isLeapYear(year)) {
                leapYears++;
            }
        }
        
        long millis = (relativeYear * 365L + leapYears)
            * (long)DateTimeConstants.MILLIS_PER_DAY;

        // Adjust to account for difference between 1968-01-01 and 1969-12-19.

        return millis - (366L + 365 - 13) * DateTimeConstants.MILLIS_PER_DAY;
    }

    protected int getMinYear() {
        // The lowest year that can be fully supported.
        return -292269053;
    }

    protected int getMaxYear() {
        // The highest year that can be fully supported.
        return 292272992;
    }

    protected long getRoughMillisPerYear() {
        return (long) (365.25 * DateTimeConstants.MILLIS_PER_DAY);
    }

    protected long getRoughMillisPerMonth() {
        return (long) (365.25 * DateTimeConstants.MILLIS_PER_DAY / 12);
    }
}
