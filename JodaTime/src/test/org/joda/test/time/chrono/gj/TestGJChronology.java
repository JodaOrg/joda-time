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

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.chrono.AbstractChronology;

/**
 * A reference Gregorian/Julian chronology implementation, intended for testing
 * purposes only. Correctness is favored over performance. The key functions
 * for date calculations are based on ones provided in "Calendrical
 * Calculations", ISBN 0-521-77752-6.
 *
 * <p>In theory, this class can be used to test any other Gregorian/Julian
 * chronology as long as almost all datetime fields are implemented differently
 * between the two. Fields that would most likely be implemented the same are
 * not supported by this class.
 *
 * <p>Unsupported features
 * <ul>
 * <li>time zones
 * <li>time of day
 * <li>year of era
 * <li>year of century
 * <li>century of era
 * <li>era
 * </ul>
 *
 * @author Brian S O'Neill
 */
abstract class TestGJChronology extends AbstractChronology {
    static final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000;

    /**
     * Divide with round-negative behavior.
     *
     * @param divisor must be positive
     */
    static long div(long dividend, long divisor) {
        if (divisor < 0) {
            throw new IllegalArgumentException("divisor must be positive: " + divisor);
        }
        if (dividend >= 0) {
            return dividend / divisor;
        } else {
            return (dividend + 1) / divisor - 1;
        }
    }

    /**
     * Modulus with round-negative behavior, result is always positive.
     *
     * @param divisor must be positive
     */
    static long mod(long dividend, long divisor) {
        if (divisor < 0) {
            throw new IllegalArgumentException("divisor must be positive: " + divisor);
        }
        if (dividend >= 0) {
            return dividend % divisor;
        } else {
            return (dividend + 1) % divisor - 1 + divisor;
        }
    }

    static long amod(long dividend, long divisor) {
        long mod = mod(dividend, divisor);
        return (mod == 0) ? divisor : mod;
    }

    /** Milliseconds from 0001-01-01 to the epoch. */
    private final long iEpochMillis;

    public TestGJChronology(int epochYear, int epochMonth, int epochDay) {
        iEpochMillis = fixedFromGJ(epochYear, epochMonth, epochDay) * MILLIS_PER_DAY;
    }

    public DateTimeZone getDateTimeZone() {
        return null;
    }

    public Chronology withUTC() {
        return this;
    }

    /**
     * Unsupported.
     */
    public Chronology withDateTimeZone(DateTimeZone zone) {
        throw new UnsupportedOperationException();
    }

    public long getTimeOnlyMillis(long millis) {
        return mod(millis, MILLIS_PER_DAY);
    }

    public long getDateOnlyMillis(long millis) {
        return millis - mod(millis, MILLIS_PER_DAY);
    }

    public DurationField days() {
        return dayOfWeek().getDurationField();
    }

    public DateTimeField dayOfWeek() {
        return new TestGJDayOfWeekField(this);
    }

    public DateTimeField dayOfMonth() {
        return new TestGJDayOfMonthField(this); 
    }

    public DateTimeField dayOfYear() {
        return new TestGJDayOfYearField(this);
    }

    public DurationField weeks() {
        return weekOfWeekyear().getDurationField();
    }

    public DateTimeField weekOfWeekyear() {
        return new TestGJWeekOfWeekyearField(this);
    }

    public DurationField weekyears() {
        return weekyear().getDurationField();
    }

    public DateTimeField weekyear() {
        return new TestGJWeekyearField(this);
    }

    public DurationField months() {
        return monthOfYear().getDurationField();
    }

    public DateTimeField monthOfYear() {
        return new TestGJMonthOfYearField(this);
    }

    public DurationField years() {
        return year().getDurationField();
    }

    public DateTimeField year() {
        return new TestGJYearField(this);
    }

    abstract long millisPerYear();

    abstract long millisPerMonth();

    abstract boolean isLeapYear(int year);

    /**
     * @return days from 0001-01-01
     */
    abstract long fixedFromGJ(int year, int monthOfYear, int dayOfMonth);

    /**
     * @param date days from 0001-01-01
     * @return gj year
     */
    abstract int gjYearFromFixed(long date);

    /**
     * @param date days from 0001-01-01
     * @return gj year, monthOfYear, dayOfMonth
     */
    abstract int[] gjFromFixed(long date);

    abstract long fixedFromISO(int weekyear, int weekOfWeekyear, int dayOfWeek);

    /**
     * @param date days from 0001-01-01
     * @return iso weekyear, weekOfWeekyear, dayOfWeek (1=Monday to 7)
     */
    abstract int[] isoFromFixed(long date);

    /**
     * @param millis milliseconds from epoch
     * @return days from 0001-01-01
     */
    long fixedFromMillis(long millis) {
        return div(millis + iEpochMillis, MILLIS_PER_DAY);
    }

    /**
     * @param fixed days from 0001-01-01
     * @return milliseconds from epoch
     */
    long millisFromFixed(long fixed) {
        return fixed * MILLIS_PER_DAY - iEpochMillis;
    }

    /**
     * @return milliseconds from epoch
     */
    long millisFromGJ(int year, int monthOfYear, int dayOfMonth) {
        return millisFromFixed(fixedFromGJ(year, monthOfYear, dayOfMonth));
    }

    /**
     * @param millis milliseconds from epoch
     * @return gj year
     */
    int gjYearFromMillis(long millis) {
        return gjYearFromFixed(fixedFromMillis(millis));
    }

    /**
     * @param millis milliseconds from epoch
     * @return gj year, monthOfYear, dayOfMonth
     */
    int[] gjFromMillis(long millis) {
        return gjFromFixed(fixedFromMillis(millis));
    }

    /**
     * @return milliseconds from epoch
     */
    long millisFromISO(int weekyear, int weekOfWeekyear, int dayOfWeek) {
        return millisFromFixed(fixedFromISO(weekyear, weekOfWeekyear, dayOfWeek));
    }

    /**
     * @param millis milliseconds from epoch
     * @return iso weekyear, weekOfWeekyear, dayOfWeek (1=Monday to 7)
     */
    int[] isoFromMillis(long millis) {
        return isoFromFixed(fixedFromMillis(millis));
    }

    /**
     * @param date days from 0001-01-01
     * @param weekday 0=Sunday, 1=Monday, 2=Tuesday ... 6=Saturday, 7=Sunday
     * @param date days from 0001-01-01, on or before weekday
     */
    long weekdayOnOrBefore(long date, int weekday) {
        return date - mod(date - mod(weekday, 7), 7);
    }

    long weekdayOnOrAfter(long date, int weekday) {
        return weekdayOnOrBefore(date + 6, weekday);
    }

    long weekdayNearest(long date, int weekday) {
        return weekdayOnOrBefore(date + 3, weekday);
    }

    long weekdayBefore(long date, int weekday) {
        return weekdayOnOrBefore(date - 1, weekday);
    }

    long weekdayAfter(long date, int weekday) {
        return weekdayOnOrBefore(date + 7, weekday);
    }

    long nthWeekday(int n, int weekday,
                    int year, int monthOfYear, int dayOfMonth)
    {
        if (n > 0) {
            return 7 * n + weekdayBefore
                (fixedFromGJ(year, monthOfYear, dayOfMonth), weekday);
        } else {
            return 7 * n + weekdayAfter
                (fixedFromGJ(year, monthOfYear, dayOfMonth), weekday);
        }
    }

    long firstWeekday(int weekday, int year, int monthOfYear, int dayOfMonth) {
        return nthWeekday(1, weekday, year, monthOfYear, dayOfMonth);
    }

    long lastWeekday(int weekday, int year, int monthOfYear, int dayOfMonth) {
        return nthWeekday(-1, weekday, year, monthOfYear, dayOfMonth);
    }
}
