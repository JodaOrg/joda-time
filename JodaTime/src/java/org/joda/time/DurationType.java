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

import java.io.Serializable;
import org.joda.time.field.MillisDurationField;
import org.joda.time.field.PreciseDurationField;
import org.joda.time.field.ScaledDurationField;
import org.joda.time.field.UnsupportedDurationField;
import org.joda.time.chrono.ISOChronology;

/**
 * Controls a duration implementation by specifying which duration fields are to be used.
 * <p>
 * The following implementations are provided:
 * <ul>
 * <li>Millis - the duration is defined only in terms of milliseconds, other
 *  fields are not used
 * <li>All - the duration is defined to permit all fields to be used
 * <li>DayHour - the duration is expressed in terms of days, hours, minutes, seconds
 *  and milliseconds
 * <li>YearMonth - the duration is expressed using all fields except weeks
 * <li>YearWeek - the duration is expressed using all fields except months
 * <li>AverageYearMonth - as YearMonth, but years and months have fixed average lengths
 * <li>PreciseAll - defines years as 365 days, months as 30 days, weeks as 7 days,
 * days as 24 hours and all the time fields
 * <li>PreciseDayHour - defines days as 24 hours and all the time fields
 * <li>PreciseYearDay - defines years as 365 days, days as 24 hours and all the time fields
 * <li>PreciseYearWeek - defines years as 365 days, weeks as 7 days,
 * days as 24 hours and all the time fields
 * <li>PreciseYearMonth - defines years as 365 days, months as 30 days,
 * days as 24 hours and all the time fields
 * </ul>
 *
 * <p>
 * DurationType is thread-safe and immutable, and all subclasses must be as well.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public abstract class DurationType implements Serializable {
    private static final long serialVersionUID = 2274324892792009998L;

    private static final DurationType MILLIS_TYPE;
    private static final DurationType DAY_HOUR_TYPE;
    private static final DurationType ALL_TYPE;
    private static final DurationType YEAR_MONTH_TYPE;
    private static final DurationType YEAR_WEEK_TYPE;
    private static final DurationType AVERAGE_YEAR_MONTH_TYPE;
    private static final DurationType PRECISE_DAY_HOUR_TYPE;
    private static final DurationType PRECISE_YEAR_DAY_TYPE;
    private static final DurationType PRECISE_YEAR_WEEK_TYPE;
    private static final DurationType PRECISE_YEAR_MONTH_TYPE;
    private static final DurationType PRECISE_ALL_TYPE;

    static {
        MILLIS_TYPE = new MillisType();
        DAY_HOUR_TYPE = new DayHourType(ISOChronology.getInstanceUTC());
        YEAR_MONTH_TYPE = new YearMonthType(ISOChronology.getInstanceUTC());
        YEAR_WEEK_TYPE = new YearWeekType(ISOChronology.getInstanceUTC());
        AVERAGE_YEAR_MONTH_TYPE = new AverageYearMonthType(ISOChronology.getInstanceUTC());
        ALL_TYPE = new AllType(ISOChronology.getInstanceUTC());
        PRECISE_DAY_HOUR_TYPE = new PreciseDayHourType(ISOChronology.getInstanceUTC());
        PRECISE_YEAR_DAY_TYPE = new PreciseYearDayType(ISOChronology.getInstanceUTC());
        PRECISE_YEAR_WEEK_TYPE = new PreciseYearWeekType(ISOChronology.getInstanceUTC());
        PRECISE_YEAR_MONTH_TYPE = new PreciseYearMonthType(ISOChronology.getInstanceUTC());
        PRECISE_ALL_TYPE = new PreciseAllType(ISOChronology.getInstanceUTC());
    }

    /**
     * Returns a DurationType of only a milliseconds field using the ISOChronology.
     * When using this type, the maximum millisecond value that can be stored is
     * typically limited by a 32 bit int.
     */
    public static DurationType getMillisType() {
        return MILLIS_TYPE;
    }

    /**
     * Returns a DurationType using the ISOChronology of:
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
        return DAY_HOUR_TYPE;
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
        if (chrono == null || chrono.equals(ISOChronology.getInstanceUTC())) {
            return getDayHourType();
        }
        return new DayHourType(chrono);
    }

    /**
     * Returns a DurationType using the ISOChronology of:
     *
     * <ul>
     * <li>years
     * <li>months
     * <li>weeks
     * <li>days
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     */
    public static DurationType getAllType() {
        return ALL_TYPE;
    }

    /**
     * Returns a DurationType of:
     *
     * <ul>
     * <li>years
     * <li>months
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
    public static DurationType getAllType(Chronology chrono) {
        if (chrono == null || chrono.equals(ISOChronology.getInstanceUTC())) {
            return getAllType();
        }
        return new AllType(chrono);
    }

    /**
     * Returns a DurationType using the ISOChronology of:
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
        return YEAR_MONTH_TYPE;
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
        if (chrono == null || chrono.equals(ISOChronology.getInstanceUTC())) {
            return getYearMonthType();
        }
        return new YearMonthType(chrono);
    }

    /**
     * Returns a DurationType using the ISOChronology of:
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
        return YEAR_WEEK_TYPE;
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
        if (chrono == null || chrono.equals(ISOChronology.getInstanceUTC())) {
            return getYearWeekType();
        }
        return new YearWeekType(chrono);
    }

    /**
     * Returns a precise DurationType using the ISOChronology of:
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
        return AVERAGE_YEAR_MONTH_TYPE;
    }

    /**
     * Returns a DurationType, normally precise, of:
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
     */
    public static DurationType getAverageYearMonthType(Chronology chrono) {
        if (chrono == null || chrono.equals(ISOChronology.getInstanceUTC())) {
            return getAverageYearMonthType();
        }
        return new AverageYearMonthType(chrono);
    }

    /**
     * Returns a precise DurationType using the ISOChronology in UTC of:
     *
     * <ul>
     * <li>days (fixed at 24 hours)
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     */
    public static DurationType getPreciseDayHourType() {
        return PRECISE_DAY_HOUR_TYPE;
    }

    /**
     * Returns a precise DurationType using the ISOChronology in UTC of:
     *
     * <ul>
     * <li>years (fixed at 365 days)
     * <li>days (fixed at 24 hours)
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     */
    public static DurationType getPreciseYearDayType() {
        return PRECISE_YEAR_DAY_TYPE;
    }

    /**
     * Returns a precise DurationType using the ISOChronology in UTC of:
     *
     * <ul>
     * <li>years (fixed at 365 days)
     * <li>months (fixed at 30 days)
     * <li>weeks (fixed at 7 days)
     * <li>days (fixed at 24 hours)
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     */
    public static DurationType getPreciseAllType() {
        return PRECISE_ALL_TYPE;
    }

    /**
     * Returns a precise DurationType using the ISOChronology in UTC of:
     *
     * <ul>
     * <li>years (fixed at 365 days)
     * <li>months (fixed at 30 days)
     * <li>days (fixed at 24 hours)
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     */
    public static DurationType getPreciseYearMonthType() {
        return PRECISE_YEAR_MONTH_TYPE;
    }

    /**
     * Returns a precise DurationType using the ISOChronology in UTC of:
     *
     * <ul>
     * <li>years (fixed at 365 days)
     * <li>weeks (fixed at 7 days)
     * <li>days (fixed at 24 hours)
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     */
    public static DurationType getPreciseYearWeekType() {
        return PRECISE_YEAR_WEEK_TYPE;
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor.
     */
    protected DurationType() {
    }

    /**
     * Gets the name of the duration type.
     * 
     * @return the name
     */
    public abstract String getName();

    /**
     * Returns the chronology used, or null if none.
     * 
     * @return the chronology
     */
    public abstract Chronology getChronology();

    /**
     * Returns a DurationType that uses the given chronology.
     * 
     * @param chrono  the new chronology, null means ISOChronology in UTC
     * @return a new duration type with the specified chronology
     */
    public abstract DurationType withChronology(Chronology chrono);

    /**
     * Returns true if every supported field in this type is precise.
     * 
     * @return true if precise
     */
    public abstract boolean isPrecise();

    //-----------------------------------------------------------------------
    /**
     * Returns a DurationField representing years.
     *
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField years() {
        return UnsupportedDurationField.getInstance("years");
    }

    /**
     * Returns a DurationField representing months.
     *
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField months() {
        return UnsupportedDurationField.getInstance("months");
    }

    /**
     * Returns a DurationField representing weeks.
     *
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField weeks() {
        return UnsupportedDurationField.getInstance("weeks");
    }

    /**
     * Returns a DurationField representing days.
     *
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField days() {
        return UnsupportedDurationField.getInstance("days");
    }

    /**
     * Returns a DurationField representing hours.
     *
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField hours() {
        return UnsupportedDurationField.getInstance("hours");
    }

    /**
     * Returns a DurationField representing minutes.
     *
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField minutes() {
        return UnsupportedDurationField.getInstance("minutes");
    }

    /**
     * Returns a DurationField representing seconds.
     *
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField seconds() {
        return UnsupportedDurationField.getInstance("seconds");
    }

    /**
     * Returns a DurationField representing milliseconds.
     *
     * @return DurationField or UnsupportedDurationField if unsupported
     */
    public DurationField millis() {
        return UnsupportedDurationField.getInstance("millis");
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a version of this DurationType instance that does not support years.
     * 
     * @return a new duration type that supports the original set of fields except years
     */
    public DurationType withYearsRemoved() {
        if (!years().isSupported()) {
            return this;
        }
        return MaskedType.mask(this, 1 << 0);
    }

    /**
     * Returns a version of this DurationType instance that does not support months.
     * 
     * @return a new duration type that supports the original set of fields except months
     */
    public DurationType withMonthsRemoved() {
        if (!months().isSupported()) {
            return this;
        }
        return MaskedType.mask(this, 1 << 1);
    }

    /**
     * Returns a version of this DurationType instance that does not support weeks.
     * 
     * @return a new duration type that supports the original set of fields except weeks
     */
    public DurationType withWeeksRemoved() {
        if (!weeks().isSupported()) {
            return this;
        }
        return MaskedType.mask(this, 1 << 2);
    }

    /**
     * Returns a version of this DurationType instance that does not support days.
     * 
     * @return a new duration type that supports the original set of fields except days
     */
    public DurationType withDaysRemoved() {
        if (!days().isSupported()) {
            return this;
        }
        return MaskedType.mask(this, 1 << 3);
    }

    /**
     * Returns a version of this DurationType instance that does not support hours.
     * 
     * @return a new duration type that supports the original set of fields except hours
     */
    public DurationType withHoursRemoved() {
        if (!hours().isSupported()) {
            return this;
        }
        return MaskedType.mask(this, 1 << 4);
    }

    /**
     * Returns a version of this DurationType instance that does not support minutes.
     * 
     * @return a new duration type that supports the original set of fields except minutes
     */
    public DurationType withMinutesRemoved() {
        if (!minutes().isSupported()) {
            return this;
        }
        return MaskedType.mask(this, 1 << 5);
    }

    /**
     * Returns a version of this DurationType instance that does not support seconds.
     * 
     * @return a new duration type that supports the original set of fields except seconds
     */
    public DurationType withSecondsRemoved() {
        if (!seconds().isSupported()) {
            return this;
        }
        return MaskedType.mask(this, 1 << 6);
    }

    /**
     * Returns a version of this DurationType instance that does not support milliseconds.
     * 
     * @return a new duration type that supports the original set of fields except milliseconds
     */
    public DurationType withMillisRemoved() {
        if (!millis().isSupported()) {
            return this;
        }
        return MaskedType.mask(this, 1 << 7);
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this type to another object.
     * To be equal, the object must be a DurationType with the same chronology
     * and same supported fields.
     * 
     * @param obj  the object to compare to
     * @return true if equal
     */
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

    /**
     * Returns a hashcode based on the chronology and supported fields.
     * 
     * @return a suitable hashcode
     */
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
    
    public String toString() {
        String name = getName();
        return "DurationType[" + (name == null ? "" : name) + "]";
    }

    //-----------------------------------------------------------------------
    private static class MillisType extends DurationType {
        private static final long serialVersionUID = -4314867016852780422L;

        MillisType() {
        }

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
        
        public String getName() {
            return "MillisType";
        }
    }

    private static class DayHourType extends DurationType {
        private static final long serialVersionUID = 1115025839896760481L;

        protected final Chronology iChronology;

        DayHourType(Chronology chrono) {
            iChronology = chrono;
        }

        public final Chronology getChronology() {
            return iChronology;
        }

        public DurationType withChronology(Chronology chrono) {
            if (chrono == iChronology) {
                return this;
            }
            return DurationType.getDayHourType(chrono);
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
        
        public String getName() {
            return "DayHourType";
        }
    }

    private static class AllType extends DayHourType {
        private static final long serialVersionUID = -359769822629866L;

        AllType(Chronology chrono) {
            super(chrono);
        }

        public DurationType withChronology(Chronology chrono) {
            if (chrono == iChronology) {
                return this;
            }
            return DurationType.getAllType(chrono);
        }

        public boolean isPrecise() {
            return years().isPrecise()
                && months().isPrecise()
                && weeks().isPrecise()
                && super.isPrecise();
        }

        public DurationField years() {
            return iChronology.years();
        }

        public DurationField months() {
            return iChronology.months();
        }

        public DurationField weeks() {
            return iChronology.weeks();
        }

        private Object readResolve() {
            return getAllType(iChronology);
        }
        
        public String getName() {
            return "AllType";
        }
    }

    private static class YearMonthType extends DayHourType {
        private static final long serialVersionUID = -1336767257680877683L;

        YearMonthType(Chronology chrono) {
            super(chrono);
        }

        public DurationType withChronology(Chronology chrono) {
            if (chrono == iChronology) {
                return this;
            }
            return DurationType.getYearMonthType(chrono);
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
        
        public String getName() {
            return "YearMonthType";
        }
    }

    private static class YearWeekType extends DayHourType {
        private static final long serialVersionUID = 1347170237843447098L;

        YearWeekType(Chronology chrono) {
            super(chrono);
        }

        public DurationType withChronology(Chronology chrono) {
            if (chrono == iChronology) {
                return this;
            }
            return DurationType.getYearWeekType(chrono);
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
        
        public String getName() {
            return "YearWeekType";
        }
    }

    private static class AverageYearMonthType extends DayHourType {
        private static final long serialVersionUID = -1629017135050918461L;

        private final DurationField iYears;
        private final DurationField iMonths;
        
        AverageYearMonthType(Chronology chrono) {
            super(chrono);
            iYears = new PreciseDurationField("AverageYears", chrono.years().getUnitMillis());
            iMonths = new PreciseDurationField("AverageMonths", chrono.months().getUnitMillis());
        }

        public DurationType withChronology(Chronology chrono) {
            if (chrono == iChronology) {
                return this;
            }
            return DurationType.getAverageYearMonthType(chrono);
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
        
        public String getName() {
            return "AverageYearMonthType";
        }
    }

    //-----------------------------------------------------------------------
    private static class PreciseDayHourType extends DurationType {
        private static final long serialVersionUID = 216528691637527857L;

        protected final Chronology iChronology;

        PreciseDayHourType(Chronology chrono) {
            iChronology = chrono;
        }

        public final Chronology getChronology() {
            return iChronology;
        }

        public DurationType withChronology(Chronology chrono) {
            return this;
        }

        public boolean isPrecise() {
            return true;
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
            return getPreciseDayHourType();
        }
        
        public String getName() {
            return "PreciseDayHourType";
        }
    }

    private static class PreciseYearDayType extends PreciseDayHourType {
        private static final long serialVersionUID = -2553285612358L;

        private final DurationField iYears;
        
        PreciseYearDayType(Chronology chrono) {
            super(chrono);
            iYears = new ScaledDurationField(chrono.days(), "PreciseYears", 365);
            // rely on days/weeks to be precise because only ISO UTC used
        }

        public DurationType withChronology(Chronology chrono) {
            return this;
        }

        public boolean isPrecise() {
            return years().isPrecise()
                && super.isPrecise();
        }

        public DurationField years() {
            return iYears;
        }

        private Object readResolve() {
            return getPreciseYearDayType();
        }
        
        public String getName() {
            return "PreciseYearDayType";
        }
    }

    private static class PreciseYearWeekType extends PreciseYearDayType {
        private static final long serialVersionUID = -2040324323318740267L;

        PreciseYearWeekType(Chronology chrono) {
            super(chrono);
        }

        public DurationType withChronology(Chronology chrono) {
            return this;
        }

        public boolean isPrecise() {
            return weeks().isPrecise()
                && super.isPrecise();
        }

        public DurationField weeks() {
            return iChronology.weeks();
        }

        private Object readResolve() {
            return getPreciseYearWeekType();
        }
        
        public String getName() {
            return "PreciseYearWeekType";
        }
    }

    private static class PreciseYearMonthType extends PreciseYearDayType {
        private static final long serialVersionUID = 1203161678926193794L;

        private final DurationField iMonths;
        
        PreciseYearMonthType(Chronology chrono) {
            super(chrono);
            iMonths = new ScaledDurationField(chrono.days(), "PreciseMonths", 30);
        }

        public DurationType withChronology(Chronology chrono) {
            return this;
        }

        public boolean isPrecise() {
            return months().isPrecise()
                && super.isPrecise();
        }

        public DurationField months() {
            return iMonths;
        }

        private Object readResolve() {
            return getPreciseYearMonthType();
        }
        
        public String getName() {
            return "PreciseYearMonthType";
        }
    }

    private static class PreciseAllType extends PreciseYearMonthType {
        private static final long serialVersionUID = 43967269280186L;

        PreciseAllType(Chronology chrono) {
            super(chrono);
        }

        public DurationType withChronology(Chronology chrono) {
            return this;
        }

        public boolean isPrecise() {
            return weeks().isPrecise()
                && super.isPrecise();
        }

        public DurationField weeks() {
            return iChronology.weeks();
        }

        private Object readResolve() {
            return getPreciseAllType();
        }
        
        public String getName() {
            return "PreciseAllType";
        }
    }

    //-----------------------------------------------------------------------
    private static class MaskedType extends DurationType {
        private static final long serialVersionUID = 940106774669244586L;

        public static DurationType mask(DurationType type, int mask) {
            if (type instanceof MaskedType) {
                MaskedType masked = (MaskedType)type;
                mask |= masked.iMask;
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
            return MaskedType.mask(iType.withChronology(chrono), iMask);
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
                return UnsupportedDurationField.getInstance("years");
            }
            return iType.years();
        }

        public DurationField months() {
            if ((iMask & (1 << 1)) != 0) {
                return UnsupportedDurationField.getInstance("months");
            }
            return iType.months();
        }

        public DurationField weeks() {
            if ((iMask & (1 << 2)) != 0) {
                return UnsupportedDurationField.getInstance("weeks");
            }
            return iType.weeks();
        }

        public DurationField days() {
            if ((iMask & (1 << 3)) != 0) {
                return UnsupportedDurationField.getInstance("days");
            }
            return iType.days();
        }

        public DurationField hours() {
            if ((iMask & (1 << 4)) != 0) {
                return UnsupportedDurationField.getInstance("hours");
            }
            return iType.hours();
        }

        public DurationField minutes() {
            if ((iMask & (1 << 5)) != 0) {
                return UnsupportedDurationField.getInstance("minutes");
            }
            return iType.minutes();
        }

        public DurationField seconds() {
            if ((iMask & (1 << 6)) != 0) {
                return UnsupportedDurationField.getInstance("seconds");
            }
            return iType.seconds();
        }

        public DurationField millis() {
            if ((iMask & (1 << 7)) != 0) {
                return UnsupportedDurationField.getInstance("millis");
            }
            return iType.millis();
        }
        
        public String getName() {
            String name = iType.getName();
            String maskStr = "";
            if ((iMask & (1 << 0)) != 0) {
                maskStr += "Years";
            }
            if ((iMask & (1 << 1)) != 0) {
                maskStr += "Months";
            }
            if ((iMask & (1 << 2)) != 0) {
                maskStr += "Weeks";
            }
            if ((iMask & (1 << 3)) != 0) {
                maskStr += "Days";
            }
            if ((iMask & (1 << 4)) != 0) {
                maskStr += "Hours";
            }
            if ((iMask & (1 << 5)) != 0) {
                maskStr += "Minutes";
            }
            if ((iMask & (1 << 6)) != 0) {
                maskStr += "Seconds";
            }
            if ((iMask & (1 << 7)) != 0) {
                maskStr += "Millis";
            }
            return "Masked" + maskStr + "[" + (name == null ? "" : name) + "]";
        }
    }

}
