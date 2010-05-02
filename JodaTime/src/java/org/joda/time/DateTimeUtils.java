/*
 *  Copyright 2001-2010 Stephen Colebourne
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

import java.lang.reflect.Method;
import java.text.DateFormatSymbols;
import java.util.Locale;

import org.joda.time.chrono.ISOChronology;

/**
 * DateTimeUtils provide public utility methods for the date-time library.
 * <p>
 * DateTimeUtils is thread-safe although shared static variables are used.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public class DateTimeUtils {

    /** The singleton instance of the system millisecond provider. */
    private static final SystemMillisProvider SYSTEM_MILLIS_PROVIDER = new SystemMillisProvider();
    /** The millisecond provider currently in use. */
    private static volatile MillisProvider cMillisProvider = SYSTEM_MILLIS_PROVIDER;

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
     * Sets the provider of the current time to class specified.
     * <p>
     * This method changes the behaviour of {@link #currentTimeMillis()}.
     * Whenever the current time is queried, the specified class will be called.
     * 
     * @param millisProvider  the provider of the current time to use, not null
     * @throws SecurityException if the application does not have sufficient security rights
     * @since 2.0
     */
    public static final void setCurrentMillisProvider(MillisProvider millisProvider) throws SecurityException {
        if (millisProvider == null) {
            throw new IllegalArgumentException("The MillisProvider must not be null");
        }
        checkPermission();
        cMillisProvider = millisProvider;
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
     * For example <code>LocalDate</code> is contiguous because DayOfMonth has
     * the same range (Month) as the unit of the next field (MonthOfYear), and
     * MonthOfYear has the same range (Year) as the unit of the next field (Year).
     * <p>
     * Similarly, <code>LocalTime</code> is contiguous, as it consists of
     * MillisOfSecond, SecondOfMinute, MinuteOfHour and HourOfDay (note how
     * the names of each field 'join up').
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
     * Gets the {@link DateFormatSymbols} based on the given locale.
     * <p>
     * If JDK 6 or newer is being used, DateFormatSymbols.getInstance(locale) will
     * be used in order to allow the use of locales defined as extensions.
     * Otherwise, new DateFormatSymbols(locale) will be used.
     * See JDK 6 {@link DateFormatSymbols} for further information.
     * 
     * @param locale  the {@link Locale} used to get the correct {@link DateFormatSymbols}
     * @return the symbols
     * @since 2.0
     */
    public static final DateFormatSymbols getDateFormatSymbols(Locale locale) {
        try {        	
        	Method method = DateFormatSymbols.class.getMethod("getInstance", new Class[] {Locale.class});        	
        	return (DateFormatSymbols) method.invoke(null, new Object[] {locale});        	
        } catch (Exception ex) {
        	return new DateFormatSymbols(locale);
        } 
    }

    //-----------------------------------------------------------------------
    /**
     * A millisecond provider, allowing control of the system clock.
     * 
     * @author Stephen Colebourne
     * @since 2.0 (previously private)
     */
    public static interface MillisProvider {
        /**
         * Gets the current time.
         * <p>
         * Implementations of this method must be thread-safe.
         * 
         * @return the current time in milliseconds
         */
        long getMillis();
    }

    /**
     * System millis provider.
     */
    static class SystemMillisProvider implements MillisProvider {
        /**
         * Gets the current time.
         * @return the current time in millis
         */
        public long getMillis() {
            return System.currentTimeMillis();
        }
    }

    /**
     * Fixed millisecond provider.
     */
    static class FixedMillisProvider implements MillisProvider {
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
        public long getMillis() {
            return iMillis;
        }
    }

    /**
     * Offset from system millis provider.
     */
    static class OffsetMillisProvider implements MillisProvider {
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
        public long getMillis() {
            return System.currentTimeMillis() + iMillis;
        }
    }

}
