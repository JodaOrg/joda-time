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
package org.joda.time;

import java.io.Serializable;
import org.joda.time.chrono.MillisDurationField;
import org.joda.time.chrono.PreciseDurationField;
import org.joda.time.chrono.ScaledDurationField;
import org.joda.time.chrono.UnsupportedDurationField;
import org.joda.time.chrono.iso.ISOChronology;

/**
 * Controls a duration implementation by specifying which duration fields are to be used.
 * <p>
 * The following implementations are provided:
 * <ul>
 * <li>Millis - the duration is defined only in terms of milliseconds, other
 *  fields are not used
 * <li>DayHour - the duration is expressed in terms of days, hours, minutes, seconds
 *  and milliseconds
 * <li>YearMonth - the duration is expressed using all fields except weeks
 * <li>YearWeek - the duration is expressed using all fields except months
 * <li>AverageYearMonth - as YearMonth, but years and months have fixed average lengths
 * <li>PreciseYearMonth - as YearMonth, but years are fixed at 365 days,
 *  and months are fixed at 30 days
 * <li>PreciseYearWeek - as YearWeek, but years are fixed at 365 days
 * </ul>
 *
 * <p>
 * DurationType is thread-safe and immutable, and all subclasses must be as
 * well.
 *
 * @author Brian S O'Neill
 */
public abstract class DurationType implements Serializable {
    static final long serialVersionUID = 2274324892792009998L;

    // TODO: Many caching opportunities

    /**
     * Returns a DurationType of only a milliseconds field.
     */
    public static DurationType getMillisType() {
        return new MillisType();
    }

    /**
     * Returns a DurationType of:
     *
     * <ul>
     * <li>days
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     */
    public static DurationType getDayHourType() {
        return getDayHourType(null);
    }

    /**
     * Returns a DurationType of:
     *
     * <ul>
     * <li>days
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     *
     * This factory method returns a DurationType that calculates using any
     * Chronology. For best results, the Chronology's time zone should
     * be UTC or have fixed offsets.
     *
     * @param chrono Chronology to use for calculations.
     */
    public static DurationType getDayHourType(Chronology chrono) {
        if (chrono == null) {
            chrono = ISOChronology.getInstanceUTC();
        }
        return new DayHourType(chrono);
    }

    /**
     * Returns a DurationType of:
     *
     * <ul>
     * <li>years
     * <li>months
     * <li>days
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     */
    public static DurationType getYearMonthType() {
        return getYearMonthType(null);
    }

    /**
     * Returns a DurationType of:
     *
     * <ul>
     * <li>years
     * <li>months
     * <li>days
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     *
     * This factory method returns a DurationType that calculates using any
     * Chronology. For best results, the Chronology's time zone should
     * be UTC or have fixed offsets.
     *
     * @param chrono Chronology to use for calculations.
     */
    public static DurationType getYearMonthType(Chronology chrono) {
        if (chrono == null) {
            chrono = ISOChronology.getInstanceUTC();
        }
        return new YearMonthType(chrono);
    }

    /**
     * Returns a DurationType of:
     *
     * <ul>
     * <li>years (weekyears)
     * <li>weeks
     * <li>days
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     */
    public static DurationType getYearWeekType() {
        return getYearWeekType(null);
    }

    /**
     * Returns a DurationType of:
     *
     * <ul>
     * <li>years (weekyears)
     * <li>weeks
     * <li>days
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     *
     * This factory method returns a DurationType that calculates using any
     * Chronology. For best results, the Chronology's time zone should
     * be UTC or have fixed offsets.
     *
     * @param chrono Chronology to use for calculations.
     */
    public static DurationType getYearWeekType(Chronology chrono) {
        if (chrono == null) {
            chrono = ISOChronology.getInstanceUTC();
        }
        return new YearWeekType(chrono);
    }

    /**
     * Returns a precise DurationType of:
     *
     * <ul>
     * <li>years (fixed at 365.2425 days)
     * <li>months (fixed at 30.436875 days)
     * <li>days
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     */
    public static DurationType getAverageYearMonthType() {
        return new AverageYearMonthType(ISOChronology.getInstanceUTC());
    }

    /**
     * Returns a precise DurationType of:
     *
     * <ul>
     * <li>years (fixed to chronology's average year)
     * <li>months (fixed to chronology's average month)
     * <li>days
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     *
     * This factory method returns a DurationType that calculates using any
     * Chronology. For best results, the Chronology's time zone should
     * be UTC or have fixed offsets.
     *
     * @param chrono Chronology to use for calculations.
     * @throws IllegalArgumentException if chronology produces an imprecise duration type
     */
    public static DurationType getAverageYearMonthType(Chronology chrono) {
        if (chrono == null || chrono.equals(ISOChronology.getInstanceUTC())) {
            return getAverageYearMonthType();
        }
        DurationType type = new AverageYearMonthType(chrono);
        if (!type.isPrecise()) {
            throw new IllegalArgumentException
                ("Chronology produced an imprecise duration type");
        }
        return type;
    }

    /**
     * Returns a precise DurationType of:
     *
     * <ul>
     * <li>years (fixed at 365 days)
     * <li>months (fixed at 30 days)
     * <li>days
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     */
    public static DurationType getPreciseYearMonthType() {
        return new PreciseYearMonthType(ISOChronology.getInstanceUTC());
    }

    /**
     * Returns a precise DurationType of:
     *
     * <ul>
     * <li>years (fixed at 365 days)
     * <li>weeks
     * <li>days
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     */
    public static DurationType getPreciseYearWeekType() {
        return new PreciseYearWeekType(ISOChronology.getInstanceUTC());
    }

    protected DurationType() {
    }

    /**
     * Returns the chronology used, or null if none.
     */
    public abstract Chronology getChronology();

    /**
     * Returns a DurationType that uses the given chronology.
     */
    public abstract DurationType withChronology(Chronology chrono);

    /**
     * Returns true if every supported field in this type is precise.
     */
    public abstract boolean isPrecise();

    /**
     * Returns a DurationField representing years.
     *
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField years() {
        return UnsupportedDurationField.INSTANCE;
    }

    /**
     * Returns a DurationField representing months.
     *
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField months() {
        return UnsupportedDurationField.INSTANCE;
    }

    /**
     * Returns a DurationField representing weeks.
     *
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField weeks() {
        return UnsupportedDurationField.INSTANCE;
    }

    /**
     * Returns a DurationField representing days.
     *
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField days() {
        return UnsupportedDurationField.INSTANCE;
    }

    /**
     * Returns a DurationField representing hours.
     *
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField hours() {
        return UnsupportedDurationField.INSTANCE;
    }

    /**
     * Returns a DurationField representing minutes.
     *
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField minutes() {
        return UnsupportedDurationField.INSTANCE;
    }

    /**
     * Returns a DurationField representing seconds.
     *
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField seconds() {
        return UnsupportedDurationField.INSTANCE;
    }

    /**
     * Returns a DurationField representing milliseconds.
     *
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField millis() {
        return UnsupportedDurationField.INSTANCE;
    }

    /**
     * Returns a version of this DurationType instance that does not support
     * years.
     */
    public DurationType withYearsRemoved() {
        if (!years().isSupported()) {
            return this;
        }
        return MaskedType.mask(this, 1 << 0);
    }

    /**
     * Returns a version of this DurationType instance that does not support
     * months.
     */
    public DurationType withMonthsRemoved() {
        if (!months().isSupported()) {
            return this;
        }
        return MaskedType.mask(this, 1 << 1);
    }

    /**
     * Returns a version of this DurationType instance that does not support
     * weeks.
     */
    public DurationType withWeeksRemoved() {
        if (!weeks().isSupported()) {
            return this;
        }
        return MaskedType.mask(this, 1 << 2);
    }

    /**
     * Returns a version of this DurationType instance that does not support
     * days.
     */
    public DurationType withDaysRemoved() {
        if (!days().isSupported()) {
            return this;
        }
        return MaskedType.mask(this, 1 << 3);
    }

    /**
     * Returns a version of this DurationType instance that does not support
     * hours.
     */
    public DurationType withHoursRemoved() {
        if (!hours().isSupported()) {
            return this;
        }
        return MaskedType.mask(this, 1 << 4);
    }

    /**
     * Returns a version of this DurationType instance that does not support
     * minutes.
     */
    public DurationType withMinutesRemoved() {
        if (!minutes().isSupported()) {
            return this;
        }
        return MaskedType.mask(this, 1 << 5);
    }

    /**
     * Returns a version of this DurationType instance that does not support
     * seconds.
     */
    public DurationType withSecondsRemoved() {
        if (!seconds().isSupported()) {
            return this;
        }
        return MaskedType.mask(this, 1 << 6);
    }

    /**
     * Returns a version of this DurationType instance that does not support
     * milliseconds.
     */
    public DurationType withMillisRemoved() {
        if (!millis().isSupported()) {
            return this;
        }
        return MaskedType.mask(this, 1 << 7);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DurationType)) {
            return false;
        }
        DurationType other = (DurationType)obj;
        Chronology chrono = getChronology();
        if (chrono == null) {
            if (other.getChronology() != null) {
                return false;
            }
        } else if (!chrono.equals(other.getChronology())) {
            return false;
        }
        return years().equals(other.years())
            && months().equals(other.months())
            && weeks().equals(other.weeks())
            && days().equals(other.days())
            && hours().equals(other.hours())
            && minutes().equals(other.minutes())
            && seconds().equals(other.seconds())
            && millis().equals(other.millis());
    }

    public int hashCode() {
        int hash = 0;
        Chronology chrono = getChronology();
        if (chrono != null) {
            hash += chrono.hashCode();
        }
        hash = hash
            + years().hashCode()
            + months().hashCode()
            + weeks().hashCode()
            + days().hashCode()
            + hours().hashCode()
            + minutes().hashCode()
            + seconds().hashCode()
            + millis().hashCode();
        return hash;
    }

    private static final class MillisType extends DurationType {
        static final long serialVersionUID = -4314867016852780422L;

        public boolean isPrecise() {
            return true;
        }

        public final DurationField millis() {
            return MillisDurationField.INSTANCE;
        }

        public Chronology getChronology() {
            return null;
        }

        public DurationType withChronology(Chronology chrono) {
            return this;
        }

        private Object readResolve() {
            return getMillisType();
        }
    }

    private static class DayHourType extends DurationType {
        static final long serialVersionUID = 1115025839896760481L;

        protected final Chronology iChronology;

        public DayHourType(Chronology chrono) {
            iChronology = chrono;
        }

        public final Chronology getChronology() {
            return iChronology;
        }

        public DurationType withChronology(Chronology chrono) {
            if (chrono == iChronology) {
                return this;
            }
            return new DayHourType(iChronology);
        }

        public boolean isPrecise() {
            return days().isPrecise()
                && hours().isPrecise()
                && minutes().isPrecise()
                && seconds().isPrecise()
                && minutes().isPrecise();
        }

        public final DurationField days() {
            return iChronology.days();
        }

        public final DurationField hours() {
            return iChronology.hours();
        }

        public final DurationField minutes() {
            return iChronology.minutes();
        }

        public final DurationField seconds() {
            return iChronology.seconds();
        }

        public final DurationField millis() {
            return iChronology.millis();
        }

        private Object readResolve() {
            return getDayHourType(iChronology);
        }
    }

    private static final class YearMonthType extends DayHourType {
        static final long serialVersionUID = -1336767257680877683L;

        public YearMonthType(Chronology chrono) {
            super(chrono);
        }

        public DurationType withChronology(Chronology chrono) {
            if (chrono == iChronology) {
                return this;
            }
            return new YearMonthType(iChronology);
        }

        public boolean isPrecise() {
            return years().isPrecise()
                && months().isPrecise()
                && super.isPrecise();
        }

        public DurationField years() {
            return iChronology.years();
        }

        public DurationField months() {
            return iChronology.months();
        }

        private Object readResolve() {
            return getYearMonthType(iChronology);
        }
    }

    private static final class YearWeekType extends DayHourType {
        static final long serialVersionUID = 1347170237843447098L;

        public YearWeekType(Chronology chrono) {
            super(chrono);
        }

        public DurationType withChronology(Chronology chrono) {
            if (chrono == iChronology) {
                return this;
            }
            return new YearWeekType(iChronology);
        }

        public boolean isPrecise() {
            return years().isPrecise()
                && weeks().isPrecise()
                && super.isPrecise();
        }

        public DurationField years() {
            return iChronology.weekyears();
        }

        public DurationField weeks() {
            return iChronology.weeks();
        }

        private Object readResolve() {
            return getYearWeekType(iChronology);
        }
    }

    private static final class AverageYearMonthType extends DayHourType {
        static final long serialVersionUID = -1629017135050918461L;

        private final DurationField iYears;
        private final DurationField iMonths;
        
        public AverageYearMonthType(Chronology chrono) {
            super(chrono);
            iYears = new PreciseDurationField("AverageYears", chrono.years().getUnitMillis());
            iMonths = new PreciseDurationField("AverageMonths", chrono.months().getUnitMillis());
        }

        public DurationType withChronology(Chronology chrono) {
            if (chrono == iChronology) {
                return this;
            }
            return getAverageYearMonthType(chrono);
        }

        public DurationField years() {
            return iYears;
        }

        public DurationField months() {
            return iMonths;
        }

        private Object readResolve() {
            return getAverageYearMonthType(iChronology);
        }
    }

    private static final class PreciseYearMonthType extends DayHourType {
        static final long serialVersionUID = 1203161678926193794L;

        private final DurationField iYears;
        private final DurationField iMonths;
        
        public PreciseYearMonthType(Chronology chrono) {
            super(chrono);
            iYears = new ScaledDurationField(chrono.days(), "PreciseYears", 365);
            iMonths = new ScaledDurationField(chrono.days(), "PreciseMonths", 30);
        }

        public DurationType withChronology(Chronology chrono) {
            return this;
        }

        public boolean isPrecise() {
            return years().isPrecise()
                && months().isPrecise()
                && super.isPrecise();
        }

        public DurationField years() {
            return iYears;
        }

        public DurationField months() {
            return iMonths;
        }

        private Object readResolve() {
            return getPreciseYearMonthType();
        }
    }

    private static final class PreciseYearWeekType extends DayHourType {
        static final long serialVersionUID = -2040324323318740267L;

        private final DurationField iYears;
        
        public PreciseYearWeekType(Chronology chrono) {
            super(chrono);
            iYears = new ScaledDurationField(chrono.days(), "PreciseYears", 365);
        }

        public DurationType withChronology(Chronology chrono) {
            return this;
        }

        public boolean isPrecise() {
            return years().isPrecise()
                && weeks().isPrecise()
                && super.isPrecise();
        }

        public DurationField years() {
            return iYears;
        }

        public DurationField weeks() {
            return iChronology.weeks();
        }

        private Object readResolve() {
            return getPreciseYearWeekType();
        }
    }

    private static final class MaskedType extends DurationType {
        static final long serialVersionUID = 940106774669244586L;

        public static DurationType mask(DurationType type, int mask) {
            if (type instanceof MaskedType) {
                MaskedType masked = (MaskedType)type;
                if ((mask |= masked.iMask) == masked.iMask) {
                    // No additional fields removed, so return original.
                    return masked;
                }
                type = masked.iType;
            }
            return new MaskedType(type, mask);
        }

        private final DurationType iType;

        // Bit 0: when set, years is unsupported
        // Bit 1: when set, months is unsupported
        // ...
        private final int iMask;

        private MaskedType(DurationType type, int mask) {
            iType = type;
            iMask = mask;
        }

        public Chronology getChronology() {
            return iType.getChronology();
        }

        public DurationType withChronology(Chronology chrono) {
            if (chrono == getChronology()) {
                return this;
            }
            return mask(iType.withChronology(chrono), iMask);
        }

        public boolean isPrecise() {
            return years().isPrecise()
                && months().isPrecise()
                && weeks().isPrecise()
                && days().isPrecise()
                && hours().isPrecise()
                && minutes().isPrecise()
                && seconds().isPrecise()
                && minutes().isPrecise();
        }

        public DurationField years() {
            if ((iMask & (1 << 0)) != 0) {
                return UnsupportedDurationField.INSTANCE;
            }
            return iType.years();
        }

        public DurationField months() {
            if ((iMask & (1 << 1)) != 0) {
                return UnsupportedDurationField.INSTANCE;
            }
            return iType.months();
        }

        public DurationField weeks() {
            if ((iMask & (1 << 2)) != 0) {
                return UnsupportedDurationField.INSTANCE;
            }
            return iType.weeks();
        }

        public DurationField days() {
            if ((iMask & (1 << 3)) != 0) {
                return UnsupportedDurationField.INSTANCE;
            }
            return iType.days();
        }

        public DurationField hours() {
            if ((iMask & (1 << 4)) != 0) {
                return UnsupportedDurationField.INSTANCE;
            }
            return iType.hours();
        }

        public DurationField minutes() {
            if ((iMask & (1 << 5)) != 0) {
                return UnsupportedDurationField.INSTANCE;
            }
            return iType.minutes();
        }

        public DurationField seconds() {
            if ((iMask & (1 << 6)) != 0) {
                return UnsupportedDurationField.INSTANCE;
            }
            return iType.seconds();
        }

        public DurationField millis() {
            if ((iMask & (1 << 7)) != 0) {
                return UnsupportedDurationField.INSTANCE;
            }
            return iType.millis();
        }
    }

}
