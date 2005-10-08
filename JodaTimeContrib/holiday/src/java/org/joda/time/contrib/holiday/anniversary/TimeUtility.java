/*
 *  Copyright 2001-2005 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.contrib.holiday.anniversary;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Period;
/**
 * 
 * @author Al Major
 *
 */
public class TimeUtility {
    private static final long APPROX_MILLIS_PER_YEAR = 365L * 24L * 60L * 60L
            * 1000L;

    private static final long APPROX_MILLIS_PER_MONTH = 31L * 24L * 60L * 60L
            * 1000L;

    public static Period getApproxPeriod(long l) {
        long millis = l;

        long numYears = millis / APPROX_MILLIS_PER_YEAR;
        Period p = Period.years((int) numYears);
        millis -= numYears * APPROX_MILLIS_PER_YEAR;

        long numMonths = millis / APPROX_MILLIS_PER_MONTH;
        p = p.withMonths((int) numMonths);
        millis -= numMonths * APPROX_MILLIS_PER_MONTH;

        long numDays = millis / DateTimeConstants.MILLIS_PER_DAY;
        p = p.withDays((int) numDays);
        millis -= numDays * DateTimeConstants.MILLIS_PER_DAY;

        long numHours = millis / DateTimeConstants.MILLIS_PER_HOUR;
        p = p.withHours((int) numHours);
        millis -= numHours * DateTimeConstants.MILLIS_PER_HOUR;

        long numMinutes = millis / DateTimeConstants.MILLIS_PER_MINUTE;
        p = p.withMinutes((int) numMinutes);
        millis -= numMinutes * DateTimeConstants.MILLIS_PER_MINUTE;

        long numSeconds = millis / DateTimeConstants.MILLIS_PER_SECOND;
        p = p.withSeconds((int) numSeconds);
        millis -= numSeconds * DateTimeConstants.MILLIS_PER_SECOND;

        p = p.withMillis((int) millis);

        assert l == getApproxMillis(p);

        return p;
    }

    public static long getApproxMillis(Period p) {
        if (p.getWeeks() != 0) {
            return ((long) p.getWeeks()) * DateTimeConstants.MILLIS_PER_WEEK;
        }
        return ((long) p.getYears()) * APPROX_MILLIS_PER_YEAR
                + ((long) p.getMonths()) * APPROX_MILLIS_PER_MONTH
                + ((long) p.getDays()) * DateTimeConstants.MILLIS_PER_DAY
                + ((long) p.getHours()) * DateTimeConstants.MILLIS_PER_HOUR
                + ((long) p.getMinutes()) * DateTimeConstants.MILLIS_PER_MINUTE
                + ((long) p.getSeconds()) * DateTimeConstants.MILLIS_PER_SECOND
                + ((long) p.getMillis());
    }

    // iXDayOfWeek takes on values from DateTimeConstants.MONDAY through
    // DateTimeConstants.SUNDAY
    static DateTime GetFirstXWeekdayOfMonth(int iXDayOfWeek, int iYear,
            int iMonth) {
        DateTime dmFirstOfMonth = new DateMidnight(iYear, iMonth, 1)
                .toDateTime();
        int dayOfWeek = dmFirstOfMonth.getDayOfWeek();
        int daysToAdd = iXDayOfWeek - dayOfWeek;
        if (iXDayOfWeek < dayOfWeek) {
            daysToAdd += 7;
        }
        return dmFirstOfMonth.plusDays(daysToAdd);
    }

    // iXDayOfWeek takes on values from DateTimeConstants.MONDAY through
    // DateTimeConstants.SUNDAY
    static DateTime GetLastXWeekdayOfMonth(int iXDayOfWeek, int iYear,
            int iMonth) {
        DateTime dmFirstOfMonth = new DateMidnight(iYear, iMonth, 1)
                .toDateTime();
        DateTime dmLastOfMonth = dmFirstOfMonth.plusMonths(1).minusDays(1);
        int dayOfWeek = dmLastOfMonth.getDayOfWeek();
        int daysToSubtract = dayOfWeek - iXDayOfWeek;
        if (dayOfWeek < iXDayOfWeek) {
            daysToSubtract -= 7;
        }
        return dmLastOfMonth.minusDays(daysToSubtract);
    }

    // iXDayOfWeek takes on values from DateTimeConstants.MONDAY through
    // DateTimeConstants.SUNDAY
    static DateTime GetFirstXWeekdayOfMonthAfterYMonthday(int iXDayOfWeek,
            int iYMonthDay, int iYear, int iMonth) {
        assert 1 <= iYMonthDay && iYMonthDay <= 31;
        DateTime dmFirstXDayOfMonth = GetFirstXWeekdayOfMonth(iXDayOfWeek,
                iYear, iMonth);
        while (dmFirstXDayOfMonth.getDayOfMonth() <= iYMonthDay) {
            dmFirstXDayOfMonth.plusWeeks(1);
        }
        return dmFirstXDayOfMonth;
    }

    // iXDayOfWeek takes on values from DateTimeConstants.MONDAY through
    // DateTimeConstants.SUNDAY
    static DateTime GetLastXWeekdayOfMonthBeforeYMonthday(int iXDayOfWeek,
            int iYMonthDay, int iYear, int iMonth) {
        assert 1 <= iYMonthDay && iYMonthDay <= 31;
        DateTime dmLastXDayOfMonth = GetLastXWeekdayOfMonth(iXDayOfWeek, iYear,
                iMonth);
        while (dmLastXDayOfMonth.getDayOfMonth() >= iYMonthDay) {
            dmLastXDayOfMonth.minusWeeks(1);
        }
        return dmLastXDayOfMonth;
    }

    static DateTime AdjustAmericanHolidayForWeekend(DateTime dm) {
        switch (dm.getDayOfWeek()) {
        case DateTimeConstants.SATURDAY:
            return dm.minusDays(1);
        case DateTimeConstants.SUNDAY:
            return dm.plusDays(1);
        default:
            return dm;
        }
    }

    static DateTime AdjustCanadianHolidayForWeekend(DateTime dm) {
        switch (dm.getDayOfWeek()) {
        case DateTimeConstants.SATURDAY:
            return dm.plusDays(2);
        case DateTimeConstants.SUNDAY:
            return dm.plusDays(1);
        default:
            return dm;
        }
    }
}
