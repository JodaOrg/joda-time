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
package org.joda.time.chrono;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.field.SkipDateTimeField;

/**
 * Implements a pure proleptic Julian calendar system, which defines every
 * fourth year as leap. This implementation follows the leap year rule
 * strictly, even for dates before 8 CE, where leap years were actually
 * irregular. In the Julian calendar, year zero does not exist: 1 BCE is
 * followed by 1 CE.
 * <p>
 * Although the Julian calendar did not exist before 45 BCE, this chronology
 * assumes it did, thus it is proleptic. This implementation also fixes the
 * start of the year at January 1.
 * <p>
 * JulianChronology is thread-safe and immutable.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Julian_calendar">Wikipedia</a>
 * @see GregorianChronology
 * @see GJChronology
 *
 * @author Guy Allard
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public final class JulianChronology extends BasicGJChronology {

    /** Serialization lock */
    private static final long serialVersionUID = -8731039522547897247L;

    private static final long MILLIS_PER_YEAR =
        (long) (365.25 * DateTimeConstants.MILLIS_PER_DAY);

    private static final long MILLIS_PER_MONTH =
        (long) (365.25 * DateTimeConstants.MILLIS_PER_DAY / 12);

    /** The lowest year that can be fully supported. */
    private static final int MIN_YEAR = -292269054;

    /** The highest year that can be fully supported. */
    private static final int MAX_YEAR = 292272992;

    /** Singleton instance of a UTC JulianChronology */
    private static final JulianChronology INSTANCE_UTC;

    /** Cache of zone to chronology arrays */
    private static final Map cCache = new HashMap();

    static {
        INSTANCE_UTC = getInstance(DateTimeZone.UTC);
    }

    static int adjustYearForSet(int year) {
        if (year <= 0) {
            if (year == 0) {
                throw new IllegalFieldValueException
                    (DateTimeFieldType.year(), new Integer(year), null, null);
            }
            year++;
        }
        return year;
    }

    /**
     * Gets an instance of the JulianChronology.
     * The time zone of the returned instance is UTC.
     * 
     * @return a singleton UTC instance of the chronology
     */
    public static JulianChronology getInstanceUTC() {
        return INSTANCE_UTC;
    }

    /**
     * Gets an instance of the JulianChronology in the default time zone.
     * 
     * @return a chronology in the default time zone
     */
    public static JulianChronology getInstance() {
        return getInstance(DateTimeZone.getDefault(), 4);
    }

    /**
     * Gets an instance of the JulianChronology in the given time zone.
     * 
     * @param zone  the time zone to get the chronology in, null is default
     * @return a chronology in the specified time zone
     */
    public static JulianChronology getInstance(DateTimeZone zone) {
        return getInstance(zone, 4);
    }

    /**
     * Gets an instance of the JulianChronology in the given time zone.
     * 
     * @param zone  the time zone to get the chronology in, null is default
     * @param minDaysInFirstWeek  minimum number of days in first week of the year; default is 4
     * @return a chronology in the specified time zone
     */
    public static JulianChronology getInstance(DateTimeZone zone, int minDaysInFirstWeek) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        JulianChronology chrono;
        synchronized (cCache) {
            JulianChronology[] chronos = (JulianChronology[]) cCache.get(zone);
            if (chronos == null) {
                chronos = new JulianChronology[7];
                cCache.put(zone, chronos);
            }
            try {
                chrono = chronos[minDaysInFirstWeek - 1];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException
                    ("Invalid min days in first week: " + minDaysInFirstWeek);
            }
            if (chrono == null) {
                if (zone == DateTimeZone.UTC) {
                    chrono = new JulianChronology(null, null, minDaysInFirstWeek);
                } else {
                    chrono = getInstance(DateTimeZone.UTC, minDaysInFirstWeek);
                    chrono = new JulianChronology
                        (ZonedChronology.getInstance(chrono, zone), null, minDaysInFirstWeek);
                }
                chronos[minDaysInFirstWeek - 1] = chrono;
            }
        }
        return chrono;
    }

    // Constructors and instance variables
    //-----------------------------------------------------------------------

    /**
     * Restricted constructor
     */
    JulianChronology(Chronology base, Object param, int minDaysInFirstWeek) {
        super(base, param, minDaysInFirstWeek);
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        Chronology base = getBase();
        int minDays = getMinimumDaysInFirstWeek();
        minDays = (minDays == 0 ? 4 : minDays);  // handle rename of BaseGJChronology
        return base == null ?
                getInstance(DateTimeZone.UTC, minDays) :
                    getInstance(base.getZone(), minDays);
    }

    // Conversion
    //-----------------------------------------------------------------------
    /**
     * Gets the Chronology in the UTC time zone.
     * 
     * @return the chronology in UTC
     */
    public Chronology withUTC() {
        return INSTANCE_UTC;
    }

    /**
     * Gets the Chronology in a specific time zone.
     * 
     * @param zone  the zone to get the chronology in, null is default
     * @return the chronology
     */
    public Chronology withZone(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        if (zone == getZone()) {
            return this;
        }
        return getInstance(zone);
    }

    long getDateMidnightMillis(int year, int monthOfYear, int dayOfMonth)
        throws IllegalArgumentException
    {
        return super.getDateMidnightMillis(adjustYearForSet(year), monthOfYear, dayOfMonth);
    }

    boolean isLeapYear(int year) {
        return (year & 3) == 0;
    }

    long calculateFirstDayOfYearMillis(int year) {
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
        
        long millis = (relativeYear * 365L + leapYears) * (long)DateTimeConstants.MILLIS_PER_DAY;

        // Adjust to account for difference between 1968-01-01 and 1969-12-19.

        return millis - (366L + 352) * DateTimeConstants.MILLIS_PER_DAY;
    }

    int getMinYear() {
        return MIN_YEAR;
    }

    int getMaxYear() {
        return MAX_YEAR;
    }

    long getAverageMillisPerYear() {
        return MILLIS_PER_YEAR;
    }

    long getAverageMillisPerYearDividedByTwo() {
        return MILLIS_PER_YEAR / 2;
    }

    long getAverageMillisPerMonth() {
        return MILLIS_PER_MONTH;
    }

    long getApproxMillisAtEpochDividedByTwo() {
        return (1969L * MILLIS_PER_YEAR + 352L * DateTimeConstants.MILLIS_PER_DAY) / 2;
    }

    protected void assemble(Fields fields) {
        if (getBase() == null) {
            super.assemble(fields);
            // Julian chronology has no year zero.
            fields.year = new SkipDateTimeField(this, fields.year);
            fields.weekyear = new SkipDateTimeField(this, fields.weekyear);
        }
    }

}
