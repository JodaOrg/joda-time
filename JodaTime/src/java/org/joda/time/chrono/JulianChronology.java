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

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.field.DelegatedDateTimeField;
import org.joda.time.field.FieldUtils;

/**
 *
 * @author Guy Allard
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 */
public final class JulianChronology extends AbstractGJChronology {

    static final long serialVersionUID = -8731039522547897247L;

    private static final long MILLIS_PER_YEAR =
        (long) (365.25 * DateTimeConstants.MILLIS_PER_DAY);

    private static final long MILLIS_PER_MONTH =
        (long) (365.25 * DateTimeConstants.MILLIS_PER_DAY / 12);

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
                throw new IllegalArgumentException("Invalid year: " + year);
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
        return getInstance(getBase().getDateTimeZone());
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
    public Chronology withDateTimeZone(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        if (zone == getDateTimeZone()) {
            return this;
        }
        return getInstance(zone);
    }

    public long getDateOnlyMillis(int year, int monthOfYear, int dayOfMonth)
        throws IllegalArgumentException
    {
        return super.getDateOnlyMillis(adjustYearForSet(year), monthOfYear, dayOfMonth);
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
        
        long millis = (relativeYear * 365L + leapYears)
            * (long)DateTimeConstants.MILLIS_PER_DAY;

        // Adjust to account for difference between 1968-01-01 and 1969-12-19.

        return millis - (366L + 352) * DateTimeConstants.MILLIS_PER_DAY;
    }

    int getMinYear() {
        // The lowest year that can be fully supported.
        return -292269053;
    }

    int getMaxYear() {
        // The highest year that can be fully supported.
        return 292272992;
    }

    long getAverageMillisPerYear() {
        return MILLIS_PER_YEAR;
    }

    long getAverageMillisPerMonth() {
        return MILLIS_PER_MONTH;
    }

    long getApproxMillisAtEpoch() {
        return 1969L * MILLIS_PER_YEAR + 352L * DateTimeConstants.MILLIS_PER_DAY;
    }

    protected void assemble(Fields fields) {
        if (getBase() == null) {
            super.assemble(fields);
            // Julian chronology has no year zero.
            fields.year = new NoYearZeroField(this, fields.year);
            fields.weekyear = new NoWeekyearZeroField(this, fields.weekyear);
        }
    }

    static class NoYearZeroField extends DelegatedDateTimeField {
        static final long serialVersionUID = -8869148464118507846L;

        final AbstractGJChronology iChronology;
        private transient int iMinYear;

        NoYearZeroField(AbstractGJChronology chronology, DateTimeField field) {
            super(field);
            iChronology = chronology;
            int min = super.getMinimumValue();
            if (min < 0) {
                iMinYear = min - 1;
            } else if (min == 0) {
                iMinYear = 1;
            } else {
                iMinYear = min;
            }
        }
        
        public int get(long millis) {
            int year = super.get(millis);
            if (year <= 0) {
                year--;
            }
            return year;
        }

        public long set(long millis, int year) {
            FieldUtils.verifyValueBounds(this, year, iMinYear, getMaximumValue());
            return super.set(millis, adjustYearForSet(year));
        }

        public int getMinimumValue() {
            return iMinYear;
        }

        private Object readResolve() {
            return iChronology.year();
        }
    }

    static class NoWeekyearZeroField extends NoYearZeroField {
        static final long serialVersionUID = -5013429014495501104L;

        NoWeekyearZeroField(AbstractGJChronology chronology, DateTimeField field) {
            super(chronology, field);
        }
        
        private Object readResolve() {
            return iChronology.weekyear();
        }
    }

}
