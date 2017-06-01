package org.joda.time.chrono;


/**
 * <p>
 * Implements a pure International Fixed calendar (also known as the Cotsworth plan, the Eastman plan,
 * the 13 Month calendar or the Equal Month calendar) a solar calendar proposal for calendar reform designed by
 * Moses B. Cotsworth, who presented it in 1902.</p>
 * <p>
 * It provides for a year of 13 months of 28 days each, with one or two days a year belonging to no month or week.
 * It is therefore a perennial calendar, with every date fixed always on the same weekday.
 * Though it was never officially adopted in any country, it was the official calendar of the Eastman Kodak Company
 * from 1928 to 1989.</p>
 * <p>
 * InternationalFixedChronology is thread-safe and immutable.
 *
 * @see <a href='https://en.wikipedia.org/wiki/International_Fixed_Calendar'>Wikipedia</a>
 *
 * @author Carlo Dapor
 * @since 1.0
 */

import org.joda.time.*;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.PreciseDurationField;

import java.util.concurrent.ConcurrentHashMap;

public class InternationalFixedChronology extends GregorianChronology {
    /**
     * Cache of zone to chronology arrays
     */
    private static final ConcurrentHashMap<DateTimeZone, InternationalFixedChronology> cCache = new ConcurrentHashMap<DateTimeZone, InternationalFixedChronology> ();
    private static final InternationalFixedChronology INSTANCE_UTC = new InternationalFixedChronology (GregorianChronology.getInstanceUTC ());

    // These arrays are NOT public. We trust ourselves not to alter the array.
    // They use zero-based array indexes so the that valid range of months is
    // automatically checked.
    private static final int[] MIN_DAYS_PER_MONTH_ARRAY = {
            28, 28, 28, 28, 28, 28,
            28, 28, 28, 28, 28, 28, 28
    };

    private static final int[] MAX_DAYS_PER_MONTH_ARRAY = {
            28, 28, 28, 28, 28, 28 + 1,
            28, 28, 28, 28, 28, 28, 28
    };

    private static final int MONTHS_PER_YEAR = MIN_DAYS_PER_MONTH_ARRAY.length;

    private static final long[] MIN_TOTAL_MILLIS_BY_MONTH_ARRAY;
    private static final long[] MAX_TOTAL_MILLIS_BY_MONTH_ARRAY;
    private static final long LEAP_DAY = (6 * 28L) * DateTimeConstants.MILLIS_PER_DAY;

    static {
        MIN_TOTAL_MILLIS_BY_MONTH_ARRAY = new long[MONTHS_PER_YEAR];
        MAX_TOTAL_MILLIS_BY_MONTH_ARRAY = new long[MONTHS_PER_YEAR];

        long minSum = 0;
        long maxSum = 0;

        for (int i = 1; i < MONTHS_PER_YEAR; i++) {
            long millis = MIN_DAYS_PER_MONTH_ARRAY[i - 1] * (long) DateTimeConstants.MILLIS_PER_DAY;
            minSum += millis;
            MIN_TOTAL_MILLIS_BY_MONTH_ARRAY[i] = minSum;

            millis = MAX_DAYS_PER_MONTH_ARRAY[i - 1] * (long) DateTimeConstants.MILLIS_PER_DAY;
            maxSum += millis;
            MAX_TOTAL_MILLIS_BY_MONTH_ARRAY[i] = maxSum;
        }
    }

    public InternationalFixedChronology (final Chronology base) {
        super (base, null, 7);
    }

    /**
     * Gets an instance of the InternationalFixedChronology.
     * The time zone of the returned instance is UTC.
     *
     * @return a singleton UTC instance of the chronology
     */
    public static InternationalFixedChronology getInstanceUTC () {
        return INSTANCE_UTC;
    }

    //-----------------------------------------------------------------------

    /**
     * Gets an instance of the InternationalFixedChronology in the default time zone.
     *
     * @return a chronology in the default time zone
     */
    public static InternationalFixedChronology getInstance () {
        return getInstance (DateTimeZone.getDefault ());
    }

    /**
     * Gets an instance of the InternationalFixedChronology in the given time zone.
     *
     * @param zone the time zone to get the chronology in, null is default
     * @return a chronology in the specified time zone
     */
    public static InternationalFixedChronology getInstance (DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault ();
        }

        InternationalFixedChronology chronology = cCache.get (zone);

        if (chronology == null) {
            chronology = new InternationalFixedChronology (GJChronology.getInstance(zone, null));
            chronology = new InternationalFixedChronology (ZonedChronology.getInstance(chronology, zone));
            InternationalFixedChronology oldChronology = cCache.putIfAbsent (zone, chronology);

            if (oldChronology != null) {
                chronology = oldChronology;
            }
        }

        return chronology;
    }

    /**
     * Returns an instance of this Chronology that operates in the UTC time
     * zone. Chronologies that do not operate in a time zone or are already
     * UTC must return themselves.
     *
     * @return a version of this chronology that ignores time zones
     */
    @Override
    public Chronology withUTC () {
        return INSTANCE_UTC;
    }

    @Override
    int getMaxMonth () {
        return MONTHS_PER_YEAR;
    }

    @Override
    int getMaxMonth (final int year) {
        return MONTHS_PER_YEAR;
    }

    /**
     * Gets the maximum number of days in any month.
     *
     * @return 30
     */
    @Override
    int getDaysInMonthMax () {
        return 30;
    }

    /**
     * Gets the maximum number of days in the month specified by the instant.
     *
     * @param instant millis from 1970-01-01T00:00:00Z
     * @return the maximum number of days in the month
     */
    @Override
    int getDaysInMonthMax (final long instant) {
        int thisYear = getYear (instant);
        int thisMonth = getMonthOfYear (instant, thisYear);
        return getDaysInYearMonth (thisYear, thisMonth);
    }

    /**
     * Identify the month of the year.
     *
     * @param millis time stamo in milli-seconds
     * @param year the year
     *
     * @return month of the year
     */
    @Override
    int getMonthOfYear (final long millis, final int year) {
        long i = millis - getYearMillis (year);
        int j = MONTHS_PER_YEAR;
        long array[] = isLeapYear (year) ? MAX_TOTAL_MILLIS_BY_MONTH_ARRAY : MIN_TOTAL_MILLIS_BY_MONTH_ARRAY;

        while (array[j - 1] > i) {
            j--;
        }

        return j;
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the number of days in the specified month and year.
     *
     * @param year  the year
     * @param month the month
     * @return the number of days
     */
    @Override
    int getDaysInYearMonth (final int year, final int month) {
        if (isLeapYear (year)) {
            return MAX_DAYS_PER_MONTH_ARRAY[month - 1];
        } else {
            return MIN_DAYS_PER_MONTH_ARRAY[month - 1];
        }
    }

    @Override
    int getDaysInMonthMax (final int month) {
        return MAX_DAYS_PER_MONTH_ARRAY[month - 1];
    }

    @Override
    int getDaysInMonthMaxForSet (final long instant, final int value) {
        return ((value > 28 || value < 1) ? getDaysInMonthMax (instant) : 28);
    }

    @Override
    long getTotalMillisByYearMonth (final int year, final int month) {
        if (isLeapYear (year)) {
            return MAX_TOTAL_MILLIS_BY_MONTH_ARRAY[month - 1];
        } else {
            return MIN_TOTAL_MILLIS_BY_MONTH_ARRAY[month - 1];
        }
    }

    /**
     * Calculate the difference between two instants
     *
     * @param minuendInstant base instant
     * @param subtrahendInstant instant to subtract
     *
     * @return difference between 2 instants
     */
    @Override
    long getYearDifference (final long minuendInstant, final long subtrahendInstant) {
        int minuendYear = getYear (minuendInstant);
        int subtrahendYear = getYear (subtrahendInstant);

        // Inlined remainder method to avoid duplicate calls to get.
        long minuendRem = minuendInstant - getYearMillis (minuendYear);
        long subtrahendRem = subtrahendInstant - getYearMillis (subtrahendYear);

        // Balance leap year differences on remainders.
        if (subtrahendRem >= LEAP_DAY) {
            if (isLeapYear (subtrahendYear)) {
                if (!isLeapYear (minuendYear)) {
                    subtrahendRem -= DateTimeConstants.MILLIS_PER_DAY;
                }
            } else if (minuendRem >= LEAP_DAY && isLeapYear (minuendYear)) {
                minuendRem -= DateTimeConstants.MILLIS_PER_DAY;
            }
        }

        int difference = minuendYear - subtrahendYear;

        if (minuendRem < subtrahendRem) {
            difference--;
        }

        return difference;
    }

    /**
     * Assign the year, working around leap day.
     *
     * @param instant an instant
     * @param year the year to set
     *
     * @return new instant of the year
     */
    @Override
    long setYear (final long instant, final int year) {
        int thisYear = getYear (instant);
        int dayOfYear = getDayOfYear (instant, thisYear);
        int millisOfDay = getMillisOfDay (instant);

        if (dayOfYear > (6 * 28)) { // after Jun 28
            if (isLeapYear (thisYear)) {
                // Current date is Jun 28 or later.
                if (!isLeapYear (year)) {
                    // Moving to a non-leap year, Jun 29 does not exist.
                    dayOfYear--;
                }
            } else {
                // Current date is Sol 01 or later.
                if (isLeapYear (year)) {
                    // Moving to a leap year, account for Jun 29.
                    dayOfYear++;
                }
            }
        }

        return getYearMonthDayMillis (year, 1, dayOfYear) + millisOfDay;
    }

    @Override
    public long getDateTimeMillis (
            final int year, final int monthOfYear, final int dayOfMonth,
            final int hourOfDay, final int minuteOfHour, final int secondOfMinute, final int millisOfSecond)
            throws IllegalArgumentException {
        FieldUtils.verifyValueBounds (DateTimeFieldType.hourOfDay (), hourOfDay, 0, 23);
        FieldUtils.verifyValueBounds (DateTimeFieldType.minuteOfHour (), minuteOfHour, 0, 59);
        FieldUtils.verifyValueBounds (DateTimeFieldType.secondOfMinute (), secondOfMinute, 0, 59);
        FieldUtils.verifyValueBounds (DateTimeFieldType.millisOfSecond (), millisOfSecond, 0, 999);

        return getDateMidnightMillis (year, monthOfYear, dayOfMonth)
                + hourOfDay * DateTimeConstants.MILLIS_PER_HOUR
                + minuteOfHour * DateTimeConstants.MILLIS_PER_MINUTE
                + secondOfMinute * DateTimeConstants.MILLIS_PER_SECOND
                + millisOfSecond;
    }

    /**
     * Gets the milliseconds for a date at midnight.
     *
     * @param year        the year
     * @param monthOfYear the month
     * @param dayOfMonth  the day
     *
     * @return the milliseconds
     */
    @Override
    long getDateMidnightMillis (final int year, final int monthOfYear, final int dayOfMonth) {
        FieldUtils.verifyValueBounds (DateTimeFieldType.year (), year, getMinYear (), getMaxYear ());
        FieldUtils.verifyValueBounds (DateTimeFieldType.monthOfYear (), monthOfYear, 1, getMaxMonth (year));
        FieldUtils.verifyValueBounds (DateTimeFieldType.dayOfMonth (), dayOfMonth, 1, getDaysInYearMonth (year, monthOfYear));

        return getYearMonthDayMillis (year, monthOfYear, dayOfMonth);
    }

    /**
     * Invoked by the constructor and after deserialization to allow subclasses
     * to define all of its supported fields.
     *
     * All unset fields default to unsupported instances.
     *
     * @param fields container of fields
     */
    @Override
    protected void assemble (final Fields fields) {
        if (getBase () == null) {
            super.assemble (fields);
        }

        fields.monthOfYear = new GJMonthOfYearDateTimeField (this, 6);
        fields.months = fields.monthOfYear.getDurationField ();

        fields.days = new PreciseDurationField (DurationFieldType.days (), DateTimeConstants.MILLIS_PER_DAY);
        fields.dayOfMonth = new BasicDayOfMonthDateTimeField (this, fields.days);
    }
}
