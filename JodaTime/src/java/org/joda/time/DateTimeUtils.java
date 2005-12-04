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
package org.joda.time;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.joda.time.chrono.ISOChronology;

/**
 * DateTimeUtils provide public utility methods for the datetime library.
 * <p>
 * DateTimeUtils is thread-safe although shared static variables are used.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public class DateTimeUtils {

    /** The singleton instance of the system millisecond provider */
    private static final SystemMillisProvider SYSTEM_MILLIS_PROVIDER = new SystemMillisProvider();
    
    /** The millisecond provider currently in use */
    private static MillisProvider cMillisProvider = SYSTEM_MILLIS_PROVIDER;

    /**
     * Restrictive constructor
     */
    protected DateTimeUtils() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the current time in milliseconds.
     * <p>
     * By default this returns <code>System.currentTimeMillis()</code>.
     * This may be changed using other methods in this class.
     * 
     * @return the current time in milliseconds from 1970-01-01T00:00:00Z
     */
    public static final long currentTimeMillis() {
        return cMillisProvider.getMillis();
    }

    /**
     * Resets the current time to return the system time.
     * <p>
     * This method changes the behaviour of {@link #currentTimeMillis()}.
     * Whenever the current time is queried, {@link System#currentTimeMillis()} is used.
     * 
     * @throws SecurityException if the application does not have sufficient security rights
     */
    public static final void setCurrentMillisSystem() throws SecurityException {
        checkPermission();
        cMillisProvider = SYSTEM_MILLIS_PROVIDER;
    }

    /**
     * Sets the current time to return a fixed millisecond time.
     * <p>
     * This method changes the behaviour of {@link #currentTimeMillis()}.
     * Whenever the current time is queried, the same millisecond time will be returned.
     * 
     * @param fixedMillis  the fixed millisecond time to use
     * @throws SecurityException if the application does not have sufficient security rights
     */
    public static final void setCurrentMillisFixed(long fixedMillis) throws SecurityException {
        checkPermission();
        cMillisProvider = new FixedMillisProvider(fixedMillis);
    }

    /**
     * Sets the current time to return the system time plus an offset.
     * <p>
     * This method changes the behaviour of {@link #currentTimeMillis()}.
     * Whenever the current time is queried, {@link System#currentTimeMillis()} is used
     * and then offset by adding the millisecond value specified here.
     * 
     * @param offsetMillis  the fixed millisecond time to use
     * @throws SecurityException if the application does not have sufficient security rights
     */
    public static final void setCurrentMillisOffset(long offsetMillis) throws SecurityException {
        checkPermission();
        if (offsetMillis == 0) {
            cMillisProvider = SYSTEM_MILLIS_PROVIDER;
        } else {
            cMillisProvider = new OffsetMillisProvider(offsetMillis);
        }
    }

    /**
     * Checks whether the provider may be changed using permission 'CurrentTime.setProvider'.
     * 
     * @throws SecurityException if the provider may not be changed
     */
    private static void checkPermission() throws SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new JodaTimePermission("CurrentTime.setProvider"));
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the millisecond instant from the specified instant object handling null.
     * <p>
     * If the instant object is <code>null</code>, the {@link #currentTimeMillis()}
     * will be returned. Otherwise, the millis from the object are returned.
     * 
     * @param instant  the instant to examine, null means now
     * @return the time in milliseconds from 1970-01-01T00:00:00Z
     */
    public static final long getInstantMillis(ReadableInstant instant) {
        if (instant == null) {
            return DateTimeUtils.currentTimeMillis();
        }
        return instant.getMillis();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the chronology from the specified instant object handling null.
     * <p>
     * If the instant object is <code>null</code>, or the instant's chronology is
     * <code>null</code>, {@link ISOChronology#getInstance()} will be returned.
     * Otherwise, the chronology from the object is returned.
     * 
     * @param instant  the instant to examine, null means ISO in the default zone
     * @return the chronology, never null
     */
    public static final Chronology getInstantChronology(ReadableInstant instant) {
        if (instant == null) {
            return ISOChronology.getInstance();
        }
        Chronology chrono = instant.getChronology();
        if (chrono == null) {
            return ISOChronology.getInstance();
        }
        return chrono;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the chronology from the specified instant based interval handling null.
     * <p>
     * The chronology is obtained from the start if that is not null, or from the
     * end if the start is null. The result is additionally checked, and if still
     * null then {@link ISOChronology#getInstance()} will be returned.
     * 
     * @param start  the instant to examine and use as the primary source of the chronology
     * @param end  the instant to examine and use as the secondary source of the chronology
     * @return the chronology, never null
     */
    public static final Chronology getIntervalChronology(ReadableInstant start, ReadableInstant end) {
        Chronology chrono = null;
        if (start != null) {
            chrono = start.getChronology();
        } else if (end != null) {
            chrono = end.getChronology();
        }
        if (chrono == null) {
            chrono = ISOChronology.getInstance();
        }
        return chrono;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the chronology from the specified interval object handling null.
     * <p>
     * If the interval object is <code>null</code>, or the interval's chronology is
     * <code>null</code>, {@link ISOChronology#getInstance()} will be returned.
     * Otherwise, the chronology from the object is returned.
     * 
     * @param interval  the interval to examine, null means ISO in the default zone
     * @return the chronology, never null
     */
    public static final Chronology getIntervalChronology(ReadableInterval interval) {
        if (interval == null) {
            return ISOChronology.getInstance();
        }
        Chronology chrono = interval.getChronology();
        if (chrono == null) {
            return ISOChronology.getInstance();
        }
        return chrono;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the interval handling null.
     * <p>
     * If the interval is <code>null</code>, an interval representing now
     * to now in the {@link ISOChronology#getInstance() ISOChronology}
     * will be returned. Otherwise, the interval specified is returned.
     * 
     * @param interval  the interval to use, null means now to now
     * @return the interval, never null
     * @since 1.1
     */
    public static final ReadableInterval getReadableInterval(ReadableInterval interval) {
        if (interval == null) {
            long now = DateTimeUtils.currentTimeMillis();
            interval = new Interval(now, now);
        }
        return interval;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the chronology handling null.
     * <p>
     * If the chronology is <code>null</code>, {@link ISOChronology#getInstance()}
     * will be returned. Otherwise, the chronology is returned.
     * 
     * @param chrono  the chronology to use, null means ISO in the default zone
     * @return the chronology, never null
     */
    public static final Chronology getChronology(Chronology chrono) {
        if (chrono == null) {
            return ISOChronology.getInstance();
        }
        return chrono;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the zone handling null.
     * <p>
     * If the zone is <code>null</code>, {@link DateTimeZone#getDefault()}
     * will be returned. Otherwise, the zone specified is returned.
     * 
     * @param zone  the time zone to use, null means the default zone
     * @return the time zone, never null
     */
    public static final DateTimeZone getZone(DateTimeZone zone) {
        if (zone == null) {
            return DateTimeZone.getDefault();
        }
        return zone;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the period type handling null.
     * <p>
     * If the zone is <code>null</code>, {@link PeriodType#standard()}
     * will be returned. Otherwise, the type specified is returned.
     * 
     * @param type  the time zone to use, null means the standard type
     * @return the type to use, never null
     */
    public static final PeriodType getPeriodType(PeriodType type) {
        if (type == null) {
            return PeriodType.standard();
        }
        return type;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the millisecond duration from the specified duration object handling null.
     * <p>
     * If the duration object is <code>null</code>, zero will be returned.
     * Otherwise, the millis from the object are returned.
     * 
     * @param duration  the duration to examine, null means zero
     * @return the duration in milliseconds
     */
    public static final long getDurationMillis(ReadableDuration duration) {
        if (duration == null) {
            return 0L;
        }
        return duration.getMillis();
    }

    //-----------------------------------------------------------------------
    /**
     * Checks whether the partial is contiguous.
     * <p>
     * A partial is contiguous if one field starts where another ends.
     * <p>
     * For example YearMonthDay is contiguous because DayOfMonth has the
     * same range (Month) as the unit of the next field (MonthOfYear), and
     * MonthOfYear has the same range (Year) as the unit of the next field
     * (Year).
     * <p>
     * Similarly, TimeOfDay is contiguous, as it consists of MillisOfSecond,
     * SecondOfMinute, MinuteOfHour and HourOfDay (note how the names of
     * each field 'join up').
     * <p>
     * However, a Year/HourOfDay partial is not contiguous because the range
     * field Day is not equal to the next field Year.
     * Similarly, a DayOfWeek/DayOfMonth partial is not contiguous because
     * the range Month is not equal to the next field Day.
     * 
     * @param partial  the partial to check
     * @return true if the partial is contiguous
     * @throws IllegalArgumentException if the partial is null
     * @since 1.1
     */
    public static final boolean isContiguous(ReadablePartial partial) {
        if (partial == null) {
            throw new IllegalArgumentException("Partial must not be null");
        }
        DurationFieldType lastType = null;
        for (int i = 0; i < partial.size(); i++) {
            DateTimeField loopField = partial.getField(i);
            if (i > 0) {
                if (loopField.getRangeDurationField().getType() != lastType) {
                    return false;
                }
            }
            lastType = loopField.getDurationField().getType();
        }
        return true;
    }

    //-----------------------------------------------------------------------
    /**
     * Converts the instant value to a <code>GregorianCalendar</code>
     * adjusting to take into account the different time zone rules.
     * <p>
     * This method is the equivalent of creating a calendar object and
     * calling set using each field in turn. The result is a calendar that
     * reflects the fields of the instant, rather than millisecond value.
     * <p>
     * This method can 'lose' data depending on the date and zone rules.
     * If a datetime exists in the specified instant that cannot be
     * represented in a calendar (due to differences in the time zone rules)
     * the JDK chooses to move the hour backwards. For example, 1972-03-26
     * 01:30  in Europe/London cannot be represented in <code>Calendar</code>
     * in JDK1.3 due to an incorrect time zone definition in the JDK.
     * Instead, the JDK sets the time to 00:30.
     *
     * @param instant  the instant to convert
     * @return the offset to add to UTC
     * @throws IllegalArgumentException if the instant is null
     * @since 1.2
     */
    public static GregorianCalendar toGregorianCalendar(ReadableInstant instant) {
        if (instant == null) {
            throw new IllegalArgumentException("The zone must not be null");
        }
        Chronology chrono = instant.getChronology();
        DateTimeZone zone = instant.getZone();
        long millis = instant.getMillis();
        TimeZone jdkZone = zone.toTimeZone();
        GregorianCalendar cal = new GregorianCalendar(jdkZone);
        cal.clear();
        cal.set(Calendar.YEAR, chrono.year().get(millis));
        cal.set(Calendar.DAY_OF_YEAR, chrono.dayOfYear().get(millis));
        cal.set(Calendar.MILLISECOND, chrono.millisOfDay().get(millis));
        cal.get(Calendar.ERA);  // force calculation
        return cal;
    }

    //-----------------------------------------------------------------------
    /**
     * Converts a <code>Calendar</code> to a <code>DateTime</code> adjusting
     * the millisecond value to preserve the local time.
     * <p>
     * <code>DateTimeZone</code> and JDK <code>TimeZone</code> have
     * different time zone rules. When converting to and from the JDK this
     * can cause unexpected and undesired consequences. This method seeks
     * to adjust for the differences in the rules by maintaining the same
     * field values in the resultant <code>DateTime</code>.
     * <p>
     * Note that this method may not be reversible if the zone rules differ
     * around a daylight savings, or other, change.
     *
     * @param calendar  the JDK calendar to convert
     * @return the converted datetime
     * @throws IllegalArgumentException if the calendar is null
     * @since 1.2
     */
    public static DateTime toDateTime(Calendar calendar) {
        if (calendar == null) {
            throw new IllegalArgumentException("The calendar must not be null");
        }
        long millisLocal = calendar.getTime().getTime() +
            calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);
        DateTimeZone zone = DateTimeZone.forTimeZone(calendar.getTimeZone());
        long millis = millisLocal - zone.getOffsetFromLocal(millisLocal);
        return new DateTime(millis, zone);
    }

    //-----------------------------------------------------------------------
    /**
     * Converts a <code>Date</code> to a <code>DateTime</code> adjusting
     * the millisecond value to preserve the local time.
     * <p>
     * <code>DateTimeZone</code> and JDK <code>TimeZone</code> have
     * different time zone rules. When converting to and from the JDK this
     * can cause unexpected and undesired consequences. This method seeks
     * to adjust for the differences in the rules by maintaining the same
     * field values in the resultant <code>DateTime</code>.
     * <p>
     * Note that this method may not be reversible if the zone rules differ
     * around a daylight savings, or other, change.
     *
     * @param date  the JDK date to convert
     * @return the converted datetime
     * @throws IllegalArgumentException if the calendar is null
     * @since 1.2
     */
    public static DateTime toDateTime(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        long millisLocal = date.getTime() - date.getTimezoneOffset() * 60000;
        DateTimeZone zone = DateTimeZone.forTimeZone(TimeZone.getDefault());
        long millis = millisLocal - zone.getOffsetFromLocal(millisLocal);
        return new DateTime(millis, zone);
    }

    //-----------------------------------------------------------------------
    /**
     * Base class defining a millisecond provider.
     */
    abstract static class MillisProvider {
        /**
         * Gets the current time.
         * @return the current time in millis
         */
        abstract long getMillis();
    }

    /**
     * System millis provider.
     */
    static class SystemMillisProvider extends MillisProvider {
        /**
         * Gets the current time.
         * @return the current time in millis
         */
        long getMillis() {
            return System.currentTimeMillis();
        }
    }

    /**
     * Fixed millisecond provider.
     */
    static class FixedMillisProvider extends MillisProvider {
        /** The fixed millis value. */
        private final long iMillis;
        
        /**
         * Constructor.
         * @param offsetMillis  the millis offset
         */
        FixedMillisProvider(long fixedMillis) {
            iMillis = fixedMillis;
        }
        
        /**
         * Gets the current time.
         * @return the current time in millis
         */
        long getMillis() {
            return iMillis;
        }
    }

    /**
     * Offset from system millis provider.
     */
    static class OffsetMillisProvider extends MillisProvider {
        /** The millis offset. */
        private final long iMillis;
        
        /**
         * Constructor.
         * @param offsetMillis  the millis offset
         */
        OffsetMillisProvider(long offsetMillis) {
            iMillis = offsetMillis;
        }
        
        /**
         * Gets the current time.
         * @return the current time in millis
         */
        long getMillis() {
            return System.currentTimeMillis() + iMillis;
        }
    }

}
