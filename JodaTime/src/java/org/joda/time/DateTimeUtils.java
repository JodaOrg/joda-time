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
package org.joda.time;

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
     */
    public static final void setCurrentMillisOffset(long offsetMillis) throws SecurityException {
        checkPermission();
        cMillisProvider = new OffsetMillisProvider(offsetMillis);
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
     * Gets the chronology from the specified instant object handling null.
     * <p>
     * If the instant object is <code>null</code>, or the instant's chronology is
     * <code>null</code>, <code>nullChrono</code> will be returned.
     * Otherwise, the chronology from the object is returned.
     * 
     * @param instant  the instant to examine, null means returns use nullChrono
     * @param nullChrono  the chronology to use in the case of finding null
     * @return the chronology
     */
    public static final Chronology getInstantChronology(ReadableInstant instant, Chronology nullChrono) {
        if (instant == null) {
            return nullChrono;
        }
        Chronology chrono = instant.getChronology();
        if (chrono == null) {
            return nullChrono;
        }
        return chrono;
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
     * Base class defining a millisecond provider.
     */
    static abstract class MillisProvider {
        abstract long getMillis();
    }

    /**
     * System millis provider.
     */
    static class SystemMillisProvider extends MillisProvider {
        long getMillis() {
            return System.currentTimeMillis();
        }
    }

    /**
     * Fixed millisecond provider.
     */
    static class FixedMillisProvider extends MillisProvider {
        private final long iMillis;
        FixedMillisProvider(long fixedMillis) {
            iMillis = fixedMillis;
        }
        long getMillis() {
            return iMillis;
        }
    }

    /**
     * Offset from system millis provider.
     */
    static class OffsetMillisProvider extends MillisProvider {
        private final long iMillis;
        OffsetMillisProvider(long offsetMillis) {
            iMillis = offsetMillis;
        }
        long getMillis() {
            return System.currentTimeMillis() + iMillis;
        }
    }

}
