/*
 *  Copyright 2001-2013 Stephen Colebourne
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.field.BaseDateTimeField;
import org.joda.time.field.DecoratedDurationField;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Implements the Gregorian/Julian calendar system which is the calendar system
 * used in most of the world. Wherever possible, it is recommended to use the
 * {@link ISOChronology} instead.
 * <p>
 * The Gregorian calendar replaced the Julian calendar, and the point in time
 * when this chronology switches can be controlled using the second parameter
 * of the getInstance method. By default this cutover is set to the date the
 * Gregorian calendar was first instituted, October 15, 1582.
 * <p>
 * Before this date, this chronology uses the proleptic Julian calendar
 * (proleptic means extending indefinitely). The Julian calendar has leap years
 * every four years, whereas the Gregorian has special rules for 100 and 400
 * years. A meaningful result will thus be obtained for all input values.
 * However before 8 CE, Julian leap years were irregular, and before 45 BCE
 * there was no Julian calendar.
 * <p>
 * This chronology differs from
 * {@link java.util.GregorianCalendar GregorianCalendar} in that years
 * in BCE are returned correctly. Thus year 1 BCE is returned as -1 instead of 1.
 * The yearOfEra field produces results compatible with GregorianCalendar.
 * <p>
 * The Julian calendar does not have a year zero, and so year -1 is followed by
 * year 1. If the Gregorian cutover date is specified at or before year -1
 * (Julian), year zero is defined. In other words, the proleptic Gregorian
 * chronology used by this class has a year zero.
 * <p>
 * To create a pure proleptic Julian chronology, use {@link JulianChronology},
 * and to create a pure proleptic Gregorian chronology, use
 * {@link GregorianChronology}.
 * <p>
 * GJChronology is thread-safe and immutable.
 * 
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public final class GJChronology extends AssembledChronology {

    /** Serialization lock */
    private static final long serialVersionUID = -2545574827706931671L;

    /**
     * Convert a datetime from one chronology to another.
     */
    private static long convertByYear(long instant, Chronology from, Chronology to) {
        return to.getDateTimeMillis
            (from.year().get(instant),
             from.monthOfYear().get(instant),
             from.dayOfMonth().get(instant),
             from.millisOfDay().get(instant));
    }

    /**
     * Convert a datetime from one chronology to another.
     */
    private static long convertByWeekyear(final long instant, Chronology from, Chronology to) {
        long newInstant;
        newInstant = to.weekyear().set(0, from.weekyear().get(instant));
        newInstant = to.weekOfWeekyear().set(newInstant, from.weekOfWeekyear().get(instant));
        newInstant = to.dayOfWeek().set(newInstant, from.dayOfWeek().get(instant));
        newInstant = to.millisOfDay().set(newInstant, from.millisOfDay().get(instant));
        return newInstant;
    }

    /**
     * The default GregorianJulian cutover point.
     */
    static final Instant DEFAULT_CUTOVER = new Instant(-12219292800000L);

    /** Cache of zone to chronology list */
    private static final Map<DateTimeZone, ArrayList<GJChronology>> cCache = new HashMap<DateTimeZone, ArrayList<GJChronology>>();

    /**
     * Factory method returns instances of the default GJ cutover
     * chronology. This uses a cutover date of October 15, 1582 (Gregorian)
     * 00:00:00 UTC. For this value, October 4, 1582 (Julian) is followed by
     * October 15, 1582 (Gregorian).
     *
     * <p>The first day of the week is designated to be
     * {@link org.joda.time.DateTimeConstants#MONDAY Monday},
     * and the minimum days in the first week of the year is 4.
     *
     * <p>The time zone of the returned instance is UTC.
     */
    public static GJChronology getInstanceUTC() {
        return getInstance(DateTimeZone.UTC, DEFAULT_CUTOVER, 4);
    }

    /**
     * Factory method returns instances of the default GJ cutover
     * chronology. This uses a cutover date of October 15, 1582 (Gregorian)
     * 00:00:00 UTC. For this value, October 4, 1582 (Julian) is followed by
     * October 15, 1582 (Gregorian).
     *
     * <p>The first day of the week is designated to be
     * {@link org.joda.time.DateTimeConstants#MONDAY Monday},
     * and the minimum days in the first week of the year is 4.
     *
     * <p>The returned chronology is in the default time zone.
     */
    public static GJChronology getInstance() {
        return getInstance(DateTimeZone.getDefault(), DEFAULT_CUTOVER, 4);
    }

    /**
     * Factory method returns instances of the GJ cutover chronology. This uses
     * a cutover date of October 15, 1582 (Gregorian) 00:00:00 UTC. For this
     * value, October 4, 1582 (Julian) is followed by October 15, 1582
     * (Gregorian).
     *
     * <p>The first day of the week is designated to be
     * {@link org.joda.time.DateTimeConstants#MONDAY Monday},
     * and the minimum days in the first week of the year is 4.
     *
     * @param zone  the time zone to use, null is default
     */
    public static GJChronology getInstance(DateTimeZone zone) {
        return getInstance(zone, DEFAULT_CUTOVER, 4);
    }

    /**
     * Factory method returns instances of the GJ cutover chronology. Any
     * cutover date may be specified.
     *
     * <p>The first day of the week is designated to be
     * {@link org.joda.time.DateTimeConstants#MONDAY Monday},
     * and the minimum days in the first week of the year is 4.
     *
     * @param zone  the time zone to use, null is default
     * @param gregorianCutover  the cutover to use, null means default
     */
    public static GJChronology getInstance(
            DateTimeZone zone,
            ReadableInstant gregorianCutover) {
        
        return getInstance(zone, gregorianCutover, 4);
    }
    
    /**
     * Factory method returns instances of the GJ cutover chronology. Any
     * cutover date may be specified.
     *
     * @param zone  the time zone to use, null is default
     * @param gregorianCutover  the cutover to use, null means default
     * @param minDaysInFirstWeek  minimum number of days in first week of the year; default is 4
     */
    public static synchronized GJChronology getInstance(
            DateTimeZone zone,
            ReadableInstant gregorianCutover,
            int minDaysInFirstWeek) {
        
        zone = DateTimeUtils.getZone(zone);
        Instant cutoverInstant;
        if (gregorianCutover == null) {
            cutoverInstant = DEFAULT_CUTOVER;
        } else {
            cutoverInstant = gregorianCutover.toInstant();
            LocalDate cutoverDate = new LocalDate(cutoverInstant.getMillis(), GregorianChronology.getInstance(zone));
            if (cutoverDate.getYear() <= 0) {
                throw new IllegalArgumentException("Cutover too early. Must be on or after 0001-01-01.");
            }
        }

        GJChronology chrono;
        synchronized (cCache) {
            ArrayList<GJChronology> chronos = cCache.get(zone);
            if (chronos == null) {
                chronos = new ArrayList<GJChronology>(2);
                cCache.put(zone, chronos);
            } else {
                for (int i = chronos.size(); --i >= 0;) {
                    chrono = chronos.get(i);
                    if (minDaysInFirstWeek == chrono.getMinimumDaysInFirstWeek() &&
                        cutoverInstant.equals(chrono.getGregorianCutover())) {
                        
                        return chrono;
                    }
                }
            }
            if (zone == DateTimeZone.UTC) {
                chrono = new GJChronology
                    (JulianChronology.getInstance(zone, minDaysInFirstWeek),
                     GregorianChronology.getInstance(zone, minDaysInFirstWeek),
                     cutoverInstant);
            } else {
                chrono = getInstance(DateTimeZone.UTC, cutoverInstant, minDaysInFirstWeek);
                chrono = new GJChronology
                    (ZonedChronology.getInstance(chrono, zone),
                     chrono.iJulianChronology,
                     chrono.iGregorianChronology,
                     chrono.iCutoverInstant);
            }
            chronos.add(chrono);
        }
        return chrono;
    }

    /**
     * Factory method returns instances of the GJ cutover chronology. Any
     * cutover date may be specified.
     *
     * @param zone  the time zone to use, null is default
     * @param gregorianCutover  the cutover to use
     * @param minDaysInFirstWeek  minimum number of days in first week of the year; default is 4
     */
    public static GJChronology getInstance(
            DateTimeZone zone,
            long gregorianCutover,
            int minDaysInFirstWeek) {
        
        Instant cutoverInstant;
        if (gregorianCutover == DEFAULT_CUTOVER.getMillis()) {
            cutoverInstant = null;
        } else {
            cutoverInstant = new Instant(gregorianCutover);
        }
        return getInstance(zone, cutoverInstant, minDaysInFirstWeek);
    }

    //-----------------------------------------------------------------------
    private JulianChronology iJulianChronology;
    private GregorianChronology iGregorianChronology;
    private Instant iCutoverInstant;

    private long iCutoverMillis;
    private long iGapDuration;

    /**
     * @param julian chronology used before the cutover instant
     * @param gregorian chronology used at and after the cutover instant
     * @param cutoverInstant instant when the gregorian chronology began
     */
    private GJChronology(JulianChronology julian,
                         GregorianChronology gregorian,
                         Instant cutoverInstant) {
        super(null, new Object[] {julian, gregorian, cutoverInstant});
    }

    /**
     * Called when applying a time zone.
     */
    private GJChronology(Chronology base,
                         JulianChronology julian,
                         GregorianChronology gregorian,
                         Instant cutoverInstant) {
        super(base, new Object[] {julian, gregorian, cutoverInstant});
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return getInstance(getZone(), iCutoverInstant, getMinimumDaysInFirstWeek());
    }

    public DateTimeZone getZone() {
        Chronology base;
        if ((base = getBase()) != null) {
            return base.getZone();
        }
        return DateTimeZone.UTC;
    }

    // Conversion
    //-----------------------------------------------------------------------
    /**
     * Gets the Chronology in the UTC time zone.
     * 
     * @return the chronology in UTC
     */
    public Chronology withUTC() {
        return withZone(DateTimeZone.UTC);
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
        return getInstance(zone, iCutoverInstant, getMinimumDaysInFirstWeek());
    }

    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int millisOfDay)
        throws IllegalArgumentException
    {
        Chronology base;
        if ((base = getBase()) != null) {
            return base.getDateTimeMillis(year, monthOfYear, dayOfMonth, millisOfDay);
        }

        // Assume date is Gregorian.
        long instant = iGregorianChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth, millisOfDay);
        if (instant < iCutoverMillis) {
            // Maybe it's Julian.
            instant = iJulianChronology.getDateTimeMillis
                (year, monthOfYear, dayOfMonth, millisOfDay);
            if (instant >= iCutoverMillis) {
                // Okay, it's in the illegal cutover gap.
                throw new IllegalArgumentException("Specified date does not exist");
            }
        }
        return instant;
    }

    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        Chronology base;
        if ((base = getBase()) != null) {
            return base.getDateTimeMillis
                (year, monthOfYear, dayOfMonth,
                 hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        }

        // Assume date is Gregorian.
        long instant;
        try {
            instant = iGregorianChronology.getDateTimeMillis
                (year, monthOfYear, dayOfMonth,
                 hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        } catch (IllegalFieldValueException ex) {
            if (monthOfYear != 2 || dayOfMonth != 29) {
                throw ex;
            }
            instant = iGregorianChronology.getDateTimeMillis
                (year, monthOfYear, 28,
                 hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
            if (instant >= iCutoverMillis) {
                throw ex;
            }
        }
        if (instant < iCutoverMillis) {
            // Maybe it's Julian.
            instant = iJulianChronology.getDateTimeMillis
                (year, monthOfYear, dayOfMonth,
                 hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
            if (instant >= iCutoverMillis) {
                // Okay, it's in the illegal cutover gap.
                throw new IllegalArgumentException("Specified date does not exist");
            }
        }
        return instant;
    }

    /**
     * Gets the cutover instant between Gregorian and Julian chronologies.
     * @return the cutover instant
     */
    public Instant getGregorianCutover() {
        return iCutoverInstant;
    }

    /**
     * Gets the minimum days needed for a week to be the first week in a year.
     * 
     * @return the minimum days
     */
    public int getMinimumDaysInFirstWeek() {
        return iGregorianChronology.getMinimumDaysInFirstWeek();
    }

    //-----------------------------------------------------------------------
    /**
     * Checks if this chronology instance equals another.
     * 
     * @param obj  the object to compare to
     * @return true if equal
     * @since 1.6
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof GJChronology) {
            GJChronology chrono = (GJChronology) obj;
            return iCutoverMillis == chrono.iCutoverMillis &&
                    getMinimumDaysInFirstWeek() == chrono.getMinimumDaysInFirstWeek() &&
                    getZone().equals(chrono.getZone());
        }
        return false;
    }

    /**
     * A suitable hash code for the chronology.
     * 
     * @return the hash code
     * @since 1.6
     */
    public int hashCode() {
        return "GJ".hashCode() * 11 + getZone().hashCode() +
                getMinimumDaysInFirstWeek() + iCutoverInstant.hashCode();
    }

    // Output
    //-----------------------------------------------------------------------
    /**
     * Gets a debugging toString.
     * 
     * @return a debugging string
     */
    public String toString() {
        StringBuffer sb = new StringBuffer(60);
        sb.append("GJChronology");
        sb.append('[');
        sb.append(getZone().getID());
        
        if (iCutoverMillis != DEFAULT_CUTOVER.getMillis()) {
            sb.append(",cutover=");
            DateTimeFormatter printer;
            if (withUTC().dayOfYear().remainder(iCutoverMillis) == 0) {
                printer = ISODateTimeFormat.date();
            } else {
                printer = ISODateTimeFormat.dateTime();
            }
            printer.withChronology(withUTC()).printTo(sb, iCutoverMillis);
        }
        
        if (getMinimumDaysInFirstWeek() != 4) {
            sb.append(",mdfw=");
            sb.append(getMinimumDaysInFirstWeek());
        }
        sb.append(']');
        
        return sb.toString();
    }

    protected void assemble(Fields fields) {
        Object[] params = (Object[])getParam();

        JulianChronology julian = (JulianChronology)params[0];
        GregorianChronology gregorian = (GregorianChronology)params[1];
        Instant cutoverInstant = (Instant)params[2];
        iCutoverMillis = cutoverInstant.getMillis();

        iJulianChronology = julian;
        iGregorianChronology = gregorian;
        iCutoverInstant = cutoverInstant;

        if (getBase() != null) {
            return;
        }

        if (julian.getMinimumDaysInFirstWeek() != gregorian.getMinimumDaysInFirstWeek()) {
            throw new IllegalArgumentException();
        }

        // Compute difference between the chronologies at the cutover instant
        iGapDuration = iCutoverMillis - julianToGregorianByYear(iCutoverMillis);

        // Begin field definitions.

        // First just copy all the Gregorian fields and then override those
        // that need special attention.
        fields.copyFieldsFrom(gregorian);
        
        // Assuming cutover is at midnight, all time of day fields can be
        // gregorian since they are unaffected by cutover.

        // Verify assumption.
        if (gregorian.millisOfDay().get(iCutoverMillis) == 0) {
            // Cutover is sometime in the day, so cutover fields are required
            // for time of day.

            fields.millisOfSecond = new CutoverField(julian.millisOfSecond(), fields.millisOfSecond, iCutoverMillis);
            fields.millisOfDay = new CutoverField(julian.millisOfDay(), fields.millisOfDay, iCutoverMillis);
            fields.secondOfMinute = new CutoverField(julian.secondOfMinute(), fields.secondOfMinute, iCutoverMillis);
            fields.secondOfDay = new CutoverField(julian.secondOfDay(), fields.secondOfDay, iCutoverMillis);
            fields.minuteOfHour = new CutoverField(julian.minuteOfHour(), fields.minuteOfHour, iCutoverMillis);
            fields.minuteOfDay = new CutoverField(julian.minuteOfDay(), fields.minuteOfDay, iCutoverMillis);
            fields.hourOfDay = new CutoverField(julian.hourOfDay(), fields.hourOfDay, iCutoverMillis);
            fields.hourOfHalfday = new CutoverField(julian.hourOfHalfday(), fields.hourOfHalfday, iCutoverMillis);
            fields.clockhourOfDay = new CutoverField(julian.clockhourOfDay(), fields.clockhourOfDay, iCutoverMillis);
            fields.clockhourOfHalfday = new CutoverField(julian.clockhourOfHalfday(),
                                                         fields.clockhourOfHalfday, iCutoverMillis);
            fields.halfdayOfDay = new CutoverField(julian.halfdayOfDay(), fields.halfdayOfDay, iCutoverMillis);
        }

        // These fields just require basic cutover support.
        {
            fields.era = new CutoverField(julian.era(), fields.era, iCutoverMillis);
        }

        // DayOfYear and weekOfWeekyear require special handling since cutover
        // year has fewer days and weeks. Extend the cutover to the start of
        // the next year or weekyear. This keeps the sequence unbroken during
        // the cutover year.

        {
            long cutover = gregorian.year().roundCeiling(iCutoverMillis);
            fields.dayOfYear = new CutoverField(
                julian.dayOfYear(), fields.dayOfYear, cutover);
        }

        {
            long cutover = gregorian.weekyear().roundCeiling(iCutoverMillis);
            fields.weekOfWeekyear = new CutoverField(
                julian.weekOfWeekyear(), fields.weekOfWeekyear, cutover, true);
        }

        // These fields are special because they have imprecise durations. The
        // family of addition methods need special attention. Override affected
        // duration fields as well.
        {
            fields.year = new ImpreciseCutoverField(
                julian.year(), fields.year, iCutoverMillis);
            fields.years = fields.year.getDurationField();
            fields.yearOfEra = new ImpreciseCutoverField(
                julian.yearOfEra(), fields.yearOfEra, fields.years, iCutoverMillis);
            fields.yearOfCentury = new ImpreciseCutoverField(
                julian.yearOfCentury(), fields.yearOfCentury, fields.years, iCutoverMillis);
            
            fields.centuryOfEra = new ImpreciseCutoverField(
                julian.centuryOfEra(), fields.centuryOfEra, iCutoverMillis);
            fields.centuries = fields.centuryOfEra.getDurationField();
            
            fields.monthOfYear = new ImpreciseCutoverField(
                julian.monthOfYear(), fields.monthOfYear, iCutoverMillis);
            fields.months = fields.monthOfYear.getDurationField();
            
            fields.weekyear = new ImpreciseCutoverField(
                julian.weekyear(), fields.weekyear, null, iCutoverMillis, true);
            fields.weekyearOfCentury = new ImpreciseCutoverField(
                julian.weekyearOfCentury(), fields.weekyearOfCentury, fields.weekyears, iCutoverMillis);
            fields.weekyears = fields.weekyear.getDurationField();
        }

        // These fields require basic cutover support, except they must link to
        // imprecise durations.
        {
            CutoverField cf = new CutoverField
                (julian.dayOfMonth(), fields.dayOfMonth, iCutoverMillis);
            cf.iRangeDurationField = fields.months;
            fields.dayOfMonth = cf;
        }
    }

    long julianToGregorianByYear(long instant) {
        return convertByYear(instant, iJulianChronology, iGregorianChronology);
    }

    long gregorianToJulianByYear(long instant) {
        return convertByYear(instant, iGregorianChronology, iJulianChronology);
    }

    long julianToGregorianByWeekyear(long instant) {
        return convertByWeekyear(instant, iJulianChronology, iGregorianChronology);
    }

    long gregorianToJulianByWeekyear(long instant) {
        return convertByWeekyear(instant, iGregorianChronology, iJulianChronology);
    }

    //-----------------------------------------------------------------------
    /**
     * This basic cutover field adjusts calls to 'get' and 'set' methods, and
     * assumes that calls to add and addWrapField are unaffected by the cutover.
     */
    private class CutoverField extends BaseDateTimeField {
        @SuppressWarnings("unused")
        private static final long serialVersionUID = 3528501219481026402L;

        final DateTimeField iJulianField;
        final DateTimeField iGregorianField;
        final long iCutover;
        final boolean iConvertByWeekyear;

        protected DurationField iDurationField;
        protected DurationField iRangeDurationField;

        /**
         * @param julianField field from the chronology used before the cutover instant
         * @param gregorianField field from the chronology used at and after the cutover
         * @param cutoverMillis  the millis of the cutover
         */
        CutoverField(DateTimeField julianField, DateTimeField gregorianField, long cutoverMillis) {
            this(julianField, gregorianField, cutoverMillis, false);
        }

        /**
         * @param julianField field from the chronology used before the cutover instant
         * @param gregorianField field from the chronology used at and after the cutover
         * @param cutoverMillis  the millis of the cutover
         * @param convertByWeekyear
         */
        CutoverField(DateTimeField julianField, DateTimeField gregorianField,
                     long cutoverMillis, boolean convertByWeekyear) {
            super(gregorianField.getType());
            iJulianField = julianField;
            iGregorianField = gregorianField;
            iCutover = cutoverMillis;
            iConvertByWeekyear = convertByWeekyear;
            // Although average length of Julian and Gregorian years differ,
            // use the Gregorian duration field because it is more accurate.
            iDurationField = gregorianField.getDurationField();

            DurationField rangeField = gregorianField.getRangeDurationField();
            if (rangeField == null) {
                rangeField = julianField.getRangeDurationField();
            }
            iRangeDurationField = rangeField;
        }

        public boolean isLenient() {
            return false;
        }

        public int get(long instant) {
            if (instant >= iCutover) {
                return iGregorianField.get(instant);
            } else {
                return iJulianField.get(instant);
            }
        }

        public String getAsText(long instant, Locale locale) {
            if (instant >= iCutover) {
                return iGregorianField.getAsText(instant, locale);
            } else {
                return iJulianField.getAsText(instant, locale);
            }
        }

        public String getAsText(int fieldValue, Locale locale) {
            return iGregorianField.getAsText(fieldValue, locale);
        }

        public String getAsShortText(long instant, Locale locale) {
            if (instant >= iCutover) {
                return iGregorianField.getAsShortText(instant, locale);
            } else {
                return iJulianField.getAsShortText(instant, locale);
            }
        }

        public String getAsShortText(int fieldValue, Locale locale) {
            return iGregorianField.getAsShortText(fieldValue, locale);
        }

        public long add(long instant, int value) {
            return iGregorianField.add(instant, value);
        }

        public long add(long instant, long value) {
            return iGregorianField.add(instant, value);
        }

        public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
            // overridden as superclass algorithm can't handle
            // 2004-02-29 + 48 months -> 2008-02-29 type dates
            if (valueToAdd == 0) {
                return values;
            }
            if (DateTimeUtils.isContiguous(partial)) {
                long instant = 0L;
                for (int i = 0, isize = partial.size(); i < isize; i++) {
                    instant = partial.getFieldType(i).getField(GJChronology.this).set(instant, values[i]);
                }
                instant = add(instant, valueToAdd);
                return GJChronology.this.get(partial, instant);
            } else {
                return super.add(partial, fieldIndex, values, valueToAdd);
            }
        }

        public int getDifference(long minuendInstant, long subtrahendInstant) {
            return iGregorianField.getDifference(minuendInstant, subtrahendInstant);
        }

        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            return iGregorianField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        }

        public long set(long instant, int value) {
            if (instant >= iCutover) {
                instant = iGregorianField.set(instant, value);
                if (instant < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant + iGapDuration < iCutover) {
                        instant = gregorianToJulian(instant);
                    }
                    // Verify that new value stuck.
                    if (get(instant) != value) {
                        throw new IllegalFieldValueException
                            (iGregorianField.getType(), Integer.valueOf(value), null, null);
                    }
                }
            } else {
                instant = iJulianField.set(instant, value);
                if (instant >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant - iGapDuration >= iCutover) {
                        instant = julianToGregorian(instant);
                    }
                    // Verify that new value stuck.
                    if (get(instant) != value) {
                       throw new IllegalFieldValueException
                            (iJulianField.getType(), Integer.valueOf(value), null, null);
                    }
                }
            }
            return instant;
        }

        public long set(long instant, String text, Locale locale) {
            if (instant >= iCutover) {
                instant = iGregorianField.set(instant, text, locale);
                if (instant < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant + iGapDuration < iCutover) {
                        instant = gregorianToJulian(instant);
                    }
                    // Cannot verify that new value stuck because set may be lenient.
                }
            } else {
                instant = iJulianField.set(instant, text, locale);
                if (instant >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant - iGapDuration >= iCutover) {
                        instant = julianToGregorian(instant);
                    }
                    // Cannot verify that new value stuck because set may be lenient.
                }
            }
            return instant;
        }

        public DurationField getDurationField() {
            return iDurationField;
        }

        public DurationField getRangeDurationField() {
            return iRangeDurationField;
        }

        public boolean isLeap(long instant) {
            if (instant >= iCutover) {
                return iGregorianField.isLeap(instant);
            } else {
                return iJulianField.isLeap(instant);
            }
        }

        public int getLeapAmount(long instant) {
            if (instant >= iCutover) {
                return iGregorianField.getLeapAmount(instant);
            } else {
                return iJulianField.getLeapAmount(instant);
            }
        }

        public DurationField getLeapDurationField() {
            return iGregorianField.getLeapDurationField();
        }


        public int getMinimumValue() {
            // For all precise fields, the Julian and Gregorian limits are
            // identical. Choose Julian to tighten up the year limits.
            return iJulianField.getMinimumValue();
        }

        public int getMinimumValue(ReadablePartial partial) {
            return iJulianField.getMinimumValue(partial);
        }

        public int getMinimumValue(ReadablePartial partial, int[] values) {
            return iJulianField.getMinimumValue(partial, values);
        }

        public int getMinimumValue(long instant) {
            if (instant < iCutover) {
                return iJulianField.getMinimumValue(instant);
            }

            int min = iGregorianField.getMinimumValue(instant);

            // Because the cutover may reduce the length of this field, verify
            // the minimum by setting it.
            instant = iGregorianField.set(instant, min);
            if (instant < iCutover) {
                min = iGregorianField.get(iCutover);
            }

            return min;
        }

        public int getMaximumValue() {
            // For all precise fields, the Julian and Gregorian limits are
            // identical.
            return iGregorianField.getMaximumValue();
        }

        public int getMaximumValue(long instant) {
            if (instant >= iCutover) {
                return iGregorianField.getMaximumValue(instant);
            }

            int max = iJulianField.getMaximumValue(instant);

            // Because the cutover may reduce the length of this field, verify
            // the maximum by setting it.
            instant = iJulianField.set(instant, max);
            if (instant >= iCutover) {
                max = iJulianField.get(iJulianField.add(iCutover, -1));
            }

            return max;
        }

        public int getMaximumValue(ReadablePartial partial) {
            long instant = GJChronology.getInstanceUTC().set(partial, 0L);
            return getMaximumValue(instant);
        }

        public int getMaximumValue(ReadablePartial partial, int[] values) {
            Chronology chrono = GJChronology.getInstanceUTC();
            long instant = 0L;
            for (int i = 0, isize = partial.size(); i < isize; i++) {
                DateTimeField field = partial.getFieldType(i).getField(chrono);
                if (values[i] <= field.getMaximumValue(instant)) {
                    instant = field.set(instant, values[i]);
                }
            }
            return getMaximumValue(instant);
        }

        public long roundFloor(long instant) {
            if (instant >= iCutover) {
                instant = iGregorianField.roundFloor(instant);
                if (instant < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant + iGapDuration < iCutover) {
                        instant = gregorianToJulian(instant);
                    }
                }
            } else {
                instant = iJulianField.roundFloor(instant);
            }
            return instant;
        }

        public long roundCeiling(long instant) {
            if (instant >= iCutover) {
                instant = iGregorianField.roundCeiling(instant);
            } else {
                instant = iJulianField.roundCeiling(instant);
                if (instant >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant - iGapDuration >= iCutover) {
                        instant = julianToGregorian(instant);
                    }
                }
            }
            return instant;
        }

        public int getMaximumTextLength(Locale locale) {
            return Math.max(iJulianField.getMaximumTextLength(locale),
                            iGregorianField.getMaximumTextLength(locale));
        }

        public int getMaximumShortTextLength(Locale locale) {
            return Math.max(iJulianField.getMaximumShortTextLength(locale),
                            iGregorianField.getMaximumShortTextLength(locale));
        }

        protected long julianToGregorian(long instant) {
            if (iConvertByWeekyear) {
                return julianToGregorianByWeekyear(instant);
            } else {
                return julianToGregorianByYear(instant);
            }
        }

        protected long gregorianToJulian(long instant) {
            if (iConvertByWeekyear) {
                return gregorianToJulianByWeekyear(instant);
            } else {
                return gregorianToJulianByYear(instant);
            }
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Cutover field for variable length fields. These fields internally call
     * set whenever add is called. As a result, the same correction applied to
     * set must be applied to add and addWrapField. Knowing when to use this
     * field requires specific knowledge of how the GJ fields are implemented.
     */
    private final class ImpreciseCutoverField extends CutoverField {
        @SuppressWarnings("unused")
        private static final long serialVersionUID = 3410248757173576441L;

        /**
         * Creates a duration field that links back to this.
         */
        ImpreciseCutoverField(DateTimeField julianField, DateTimeField gregorianField, long cutoverMillis) {
            this(julianField, gregorianField, null, cutoverMillis, false);
        }

        /**
         * Uses a shared duration field rather than creating a new one.
         *
         * @param durationField shared duration field
         */
        ImpreciseCutoverField(DateTimeField julianField, DateTimeField gregorianField,
                              DurationField durationField, long cutoverMillis)
        {
            this(julianField, gregorianField, durationField, cutoverMillis, false);
        }

        /**
         * Uses a shared duration field rather than creating a new one.
         *
         * @param durationField shared duration field
         */
        ImpreciseCutoverField(DateTimeField julianField, DateTimeField gregorianField,
                              DurationField durationField,
                              long cutoverMillis, boolean convertByWeekyear)
        {
            super(julianField, gregorianField, cutoverMillis, convertByWeekyear);
            if (durationField == null) {
                durationField = new LinkedDurationField(iDurationField, this);
            }
            iDurationField = durationField;
        }

        public long add(long instant, int value) {
            if (instant >= iCutover) {
                instant = iGregorianField.add(instant, value);
                if (instant < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant + iGapDuration < iCutover) {
                        if (iConvertByWeekyear) {
                            int wyear = iGregorianChronology.weekyear().get(instant);
                            if (wyear <= 0) {
                                instant = iGregorianChronology.weekyear().add(instant, -1);
                            }
                        } else {
                            int year = iGregorianChronology.year().get(instant);
                            if (year <= 0) {
                                instant = iGregorianChronology.year().add(instant, -1);
                            }
                        }
                        instant = gregorianToJulian(instant);
                    }
                }
            } else {
                instant = iJulianField.add(instant, value);
                if (instant >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant - iGapDuration >= iCutover) {
                        // no special handling for year zero as cutover always after year zero
                        instant = julianToGregorian(instant);
                    }
                }
            }
            return instant;
        }
        
        public long add(long instant, long value) {
            if (instant >= iCutover) {
                instant = iGregorianField.add(instant, value);
                if (instant < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant + iGapDuration < iCutover) {
                        if (iConvertByWeekyear) {
                            int wyear = iGregorianChronology.weekyear().get(instant);
                            if (wyear <= 0) {
                                instant = iGregorianChronology.weekyear().add(instant, -1);
                            }
                        } else {
                            int year = iGregorianChronology.year().get(instant);
                            if (year <= 0) {
                                instant = iGregorianChronology.year().add(instant, -1);
                            }
                        }
                        instant = gregorianToJulian(instant);
                    }
                }
            } else {
                instant = iJulianField.add(instant, value);
                if (instant >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant - iGapDuration >= iCutover) {
                        // no special handling for year zero as cutover always after year zero
                        instant = julianToGregorian(instant);
                    }
                }
            }
            return instant;
        }

        public int getDifference(long minuendInstant, long subtrahendInstant) {
            if (minuendInstant >= iCutover) {
                if (subtrahendInstant >= iCutover) {
                    return iGregorianField.getDifference(minuendInstant, subtrahendInstant);
                }
                // Remember, the add is being reversed. Since subtrahend is
                // Julian, convert minuend to Julian to match.
                minuendInstant = gregorianToJulian(minuendInstant);
                return iJulianField.getDifference(minuendInstant, subtrahendInstant);
            } else {
                if (subtrahendInstant < iCutover) {
                    return iJulianField.getDifference(minuendInstant, subtrahendInstant);
                }
                // Remember, the add is being reversed. Since subtrahend is
                // Gregorian, convert minuend to Gregorian to match.
                minuendInstant = julianToGregorian(minuendInstant);
                return iGregorianField.getDifference(minuendInstant, subtrahendInstant);
            }
        }

        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            if (minuendInstant >= iCutover) {
                if (subtrahendInstant >= iCutover) {
                    return iGregorianField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
                }
                // Remember, the add is being reversed. Since subtrahend is
                // Julian, convert minuend to Julian to match.
                minuendInstant = gregorianToJulian(minuendInstant);
                return iJulianField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
            } else {
                if (subtrahendInstant < iCutover) {
                    return iJulianField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
                }
                // Remember, the add is being reversed. Since subtrahend is
                // Gregorian, convert minuend to Gregorian to match.
                minuendInstant = julianToGregorian(minuendInstant);
                return iGregorianField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
            }
        }

        // Since the imprecise fields have durations longer than the gap
        // duration, keep these methods simple. The inherited implementations
        // produce incorrect results.
        //
        // Degenerate case: If this field is a month, and the cutover is set
        // far into the future, then the gap duration may be so large as to
        // reduce the number of months in a year. If the missing month(s) are
        // at the beginning or end of the year, then the minimum and maximum
        // values are not 1 and 12. I don't expect this case to ever occur.

        public int getMinimumValue(long instant) {
            if (instant >= iCutover) {
                return iGregorianField.getMinimumValue(instant);
            } else {
                return iJulianField.getMinimumValue(instant);
            }
        }

        public int getMaximumValue(long instant) {
            if (instant >= iCutover) {
                return iGregorianField.getMaximumValue(instant);
            } else {
                return iJulianField.getMaximumValue(instant);
            }
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Links the duration back to a ImpreciseCutoverField.
     */
    private static class LinkedDurationField extends DecoratedDurationField {
        private static final long serialVersionUID = 4097975388007713084L;

        private final ImpreciseCutoverField iField;

        LinkedDurationField(DurationField durationField, ImpreciseCutoverField dateTimeField) {
            super(durationField, durationField.getType());
            iField = dateTimeField;
        }

        public long add(long instant, int value) {
            return iField.add(instant, value);
        }

        public long add(long instant, long value) {
            return iField.add(instant, value);
        }

        public int getDifference(long minuendInstant, long subtrahendInstant) {
            return iField.getDifference(minuendInstant, subtrahendInstant);
        }

        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            return iField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        }
    }

}
