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
package org.joda.test.time.gj;

import org.joda.test.time.AbstractTestDateTimeField;
import org.joda.time.chrono.gj.GJChronology;
/**
 * This class is a Junit unit test for the date time field.
 *
 * @author Stephen Colebourne
 */
public abstract class AbstractTestGJDateTimeField extends AbstractTestDateTimeField {


    public AbstractTestGJDateTimeField(String name) {
        super(name);
    }

    static final int[] monthLengths = {-1, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    protected static final long GJ_CUTOVER_MILLIS = GJChronology.getInstanceUTC().getGregorianJulianCutoverMillis();
    
    protected int[] getDMYDS(long millis) {
        return getDMYDS(millis, GJ_CUTOVER_MILLIS, 1582, 10);
    }
    
    protected final int[] getDMYDS(long millis, long cutoverMillis, int cutoverYear, int cutoverAmount) {
        long days = 0;
        if (millis >= 0) {
            days = (int) ((millis / (24 * 60 * 60 * 1000)));
            int day = 1;
            int month = 1;
            int year = 1970;
            int doy = 1;
            for (int i = 0; i < days; i++) {
                boolean leap = isLeap(year, cutoverYear);
                int yearLength = (leap ? 366 : 365);
                if ((days - i) > yearLength) {
                    // shortcut
                    year++;
                    i = i + yearLength - 1;
                } else {
                    day++;
                    doy++;
                    if (day > monthLengths[month]) {
                        if (month == 2 && day == 29 && isLeap(year, cutoverYear)) {
                            // do nothing
                        } else {
                            day = 1;
                            month++;
                            if (month == 13) {
                                month = 1;
                                year++;
                                doy = 1;
                            }
                        }
                    }
                }
            }
            int monthLength = (month == 2 && isLeap(year, cutoverYear) ? 29 : monthLengths[month]);
            int yearLength = (isLeap(year, cutoverYear) ? 366 : 365);
            return new int[] {day, month, year, doy, monthLength, yearLength};
        } else {
            days = (int) (((-millis - 1) / (24 * 60 * 60 * 1000)));
            if (millis < cutoverMillis) {
                // fake the cutover (calculation will be wrong until it gets to before the cutover)
                days = days + cutoverAmount;
            }
            int day = 1;  // count in reverse!
            int month = 12;
            int year = 1969;
            int doy = 1;
            for (int i = 0; i < days; i++) {
                boolean leap = isLeap(year, cutoverYear);
                int yearLength = (leap ? 366 : 365);
                if ((days - i) > yearLength) {
                    // shortcut
                    year--;
                    i = i + yearLength - 1;
                } else {
                    day++;
                    doy++;
                    if (day > monthLengths[month]) {
                        if (month == 2 && day == 29 && leap) {
                            // do nothing
                        } else {
                            day = 1;
                            month--;
                            if (month == 0) {
                                month = 12;
                                year--;
                                doy = 1;
                            }
                        }
                    }
                }
            }
            int monthLength = (month == 2 && isLeap(year, cutoverYear) ? 29 : monthLengths[month]);
            if (year == cutoverYear && month == 10) {
                monthLength = monthLength - cutoverAmount;
            }
            if (month == 2 && isLeap(year, cutoverYear)) {
                day = monthLengths[month] + 2 - day;
            } else {
                day = monthLengths[month] + 1 - day;
            }
            int yearLength = (isLeap(year, cutoverYear) ? 366 : 365);
            yearLength = (year == cutoverYear && millis >= cutoverMillis  ? yearLength - cutoverAmount : yearLength);
            doy = (isLeap(year, cutoverYear) ? 367 - doy : 366 - doy);
            doy = (year == cutoverYear && millis >= cutoverMillis  ? doy - cutoverAmount : doy);
            return new int[] {day, month, year, doy, monthLength, yearLength};
        }
    }
    
    /**
     * @param year
     * @return true if a leap year
     */
    protected boolean isLeap(int year, int cutoverYear) {
        if (year < cutoverYear) {
            return (year % 4 == 0);
        }
        if (year % 400 == 0) {
            return true;
        }
        if (year % 100 == 0) {
            return false;
        }
        return (year % 4 == 0);
    }

}
