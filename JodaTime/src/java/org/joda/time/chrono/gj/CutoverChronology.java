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
package org.joda.time.chrono.gj;

import java.util.Locale;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.chrono.AbstractDateTimeField;
import org.joda.time.chrono.DecoratedDurationField;

/**
 * Chronology for supporting the cutover from the Julian calendar to the
 * Gregorian calendar.
 * 
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
final class CutoverChronology extends GJChronology {

    static final long serialVersionUID = -2545574827706931671L;

    /**
     * Convert a datetime from one chronology to another.
     */
    private static long convert(long instant, Chronology from, Chronology to) {
        return to.getDateTimeMillis
            (from.year().get(instant),
             from.monthOfYear().get(instant),
             from.dayOfMonth().get(instant),
             from.millisOfDay().get(instant));
    }

    private static void checkUTC(Chronology chrono) {
        if (chrono.getDateTimeZone() != null &&
            chrono.getDateTimeZone() != DateTimeZone.UTC) {
            throw new IllegalArgumentException();
        }
    }

    private final GJChronology iJulianChronology;
    private final GJChronology iGregorianChronology;

    final long iCutoverInstant;
    transient final long iGapDuration;

    /**
     * @param julian chronology used before the cutover instant
     * @param gregorian chronology used at and after the cutover instant
     * @param cutoverInstant instant when the gregorian chronology began
     */
    CutoverChronology(JulianChronology julian, GregorianChronology gregorian, long cutoverInstant) {
        checkUTC(julian);
        checkUTC(gregorian);

        if (julian.getMinimumDaysInFirstWeek() != gregorian.getMinimumDaysInFirstWeek()) {
            throw new IllegalArgumentException();
        }
        if (julian.isCenturyISO() != gregorian.isCenturyISO()) {
            throw new IllegalArgumentException();
        }

        iJulianChronology = julian;
        iGregorianChronology = gregorian;
        iCutoverInstant = cutoverInstant;

        // Compute difference between the chronologies at the cutover instant
        iGapDuration = cutoverInstant - julianToGregorian(cutoverInstant);

        // Begin field definitions.

        // First just copy all the Gregorian fields and then override those
        // that need special attention.
        copyFields(gregorian);
        
        // Assuming cutover is at midnight, all time of day fields can be
        // gregorian since they are unaffected by cutover.

        // Verify assumption.
        if (gregorian.millisOfDay().get(cutoverInstant) == 0) {
            // Cutover is sometime in the day, so cutover fields are required
            // for time of day.

            iMillisOfSecondField = new CutoverField(julian.millisOfSecond(), iMillisOfSecondField);
            iMillisOfDayField = new CutoverField(julian.millisOfDay(), iMillisOfDayField);
            iSecondOfMinuteField = new CutoverField(julian.secondOfMinute(), iSecondOfMinuteField);
            iSecondOfDayField = new CutoverField(julian.secondOfDay(), iSecondOfDayField);
            iMinuteOfHourField = new CutoverField(julian.minuteOfHour(), iMinuteOfHourField);
            iMinuteOfDayField = new CutoverField(julian.minuteOfDay(), iMinuteOfDayField);
            iHourOfDayField = new CutoverField(julian.hourOfDay(), iHourOfDayField);
            iHourOfHalfdayField = new CutoverField(julian.hourOfHalfday(), iHourOfHalfdayField);
            iClockhourOfDayField = new CutoverField(julian.clockhourOfDay(), iClockhourOfDayField);
            iClockhourOfHalfdayField = new CutoverField(julian.clockhourOfHalfday(), iClockhourOfHalfdayField);
            iHalfdayOfDayField = new CutoverField(julian.halfdayOfDay(), iHalfdayOfDayField);
        }

        // These fields just require basic cutover support.
        {
            iEraField = new CutoverField(julian.era(), gregorian.era());
            iDayOfMonthField = new CutoverField(julian.dayOfMonth(), gregorian.dayOfMonth());
        }

        // DayOfYear and weekOfWeekyear require special handling since cutover
        // year has fewer days and weeks. Extend the cutover to the start of
        // the next year or weekyear. This keeps the sequence unbroken during
        // the cutover year.

        {
            long cutover = gregorian.year().roundCeiling(iCutoverInstant);
            iDayOfYearField = new CutoverField
                (julian.dayOfYear(), gregorian.dayOfYear(), cutover);
        }

        {
            long cutover = gregorian.weekyear().roundCeiling(iCutoverInstant);
            iWeekOfWeekyearField = new CutoverField
                (julian.weekOfWeekyear(), gregorian.weekOfWeekyear(), cutover);
        }

        // These fields are special because they have imprecise durations. The
        // family of addition methods need special attention. Override affected
        // duration fields as well.
        {
            iYearField = new ImpreciseCutoverField(julian.year(), gregorian.year());
            iYearsField = iYearField.getDurationField();
            iYearOfEraField = new ImpreciseCutoverField
                (julian.yearOfEra(), gregorian.yearOfEra(), iYearsField);
            iYearOfCenturyField = new ImpreciseCutoverField
                (julian.yearOfCentury(), gregorian.yearOfCentury(), iYearsField);
            
            iCenturyOfEraField = new ImpreciseCutoverField(julian.centuryOfEra(), gregorian.centuryOfEra());
            iCenturiesField = iCenturyOfEraField.getDurationField();
            
            iMonthOfYearField = new ImpreciseCutoverField(julian.monthOfYear(), gregorian.monthOfYear());
            iMonthsField = iMonthOfYearField.getDurationField();
            
            iWeekyearField = new ImpreciseCutoverField(julian.weekyear(), gregorian.weekyear());
            iWeekyearsField = iWeekyearField.getDurationField();
        }
    }

    public Chronology withUTC() {
        return this;
    }

    public long getDateOnlyMillis(int year, int monthOfYear, int dayOfMonth)
        throws IllegalArgumentException
    {
        return getDateTimeMillis(year, monthOfYear, dayOfMonth, 0);
    }

    public long getTimeOnlyMillis(int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        // Time fields are same for Julian and Gregorian.
        return iGregorianChronology.getTimeOnlyMillis
            (hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int millisOfDay)
        throws IllegalArgumentException
    {
        // Assume date is Gregorian.
        long instant = iGregorianChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth, millisOfDay);
        if (instant < iCutoverInstant) {
            // Maybe it's Julian.
            instant = iJulianChronology.getDateTimeMillis
                (year, monthOfYear, dayOfMonth, millisOfDay);
            if (instant >= iCutoverInstant) {
                // Okay, it's in the illegal cutover gap.
                throw new IllegalArgumentException("Specified date does not exist");
            }
        }
        return instant;
    }

    public long getDateTimeMillis(long instant,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        return getDateOnlyMillis(instant)
            + getTimeOnlyMillis(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        return getDateTimeMillis(year, monthOfYear, dayOfMonth, 0)
            + getTimeOnlyMillis(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    public long getGregorianJulianCutoverMillis() {
        return iCutoverInstant;
    }

    public boolean isCenturyISO() {
        return iGregorianChronology.isCenturyISO();
    }

    public final int getMinimumDaysInFirstWeek() {
        return iGregorianChronology.getMinimumDaysInFirstWeek();
    }

    long julianToGregorian(long instant) {
        return convert(instant, iJulianChronology, iGregorianChronology);
    }

    long gregorianToJulian(long instant) {
        return convert(instant, iGregorianChronology, iJulianChronology);
    }

    /**
     * This basic cutover field adjusts calls to 'get' and 'set' methods, and
     * assumes that calls to add and addWrapped are unaffected by the cutover.
     */
    private class CutoverField extends AbstractDateTimeField {
        static final long serialVersionUID = 3528501219481026402L;

        final DateTimeField iJulianField;
        final DateTimeField iGregorianField;
        final long iCutover;

        protected DurationField iDurationField;

        /**
         * @param julianField field from the chronology used before the cutover instant
         * @param gregorianField field from the chronology used at and after the cutover
         */
        CutoverField(DateTimeField julianField, DateTimeField gregorianField) {
            this(julianField, gregorianField, iCutoverInstant);
        }

        CutoverField(DateTimeField julianField, DateTimeField gregorianField, long cutoverInstant) {
            super(gregorianField.getName());
            iJulianField = julianField;
            iGregorianField = gregorianField;
            iCutover = cutoverInstant;
            // Although average length of Julian and Gregorian years differ,
            // use the Gregorian duration field because it is more accurate.
            iDurationField = gregorianField.getDurationField();
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

        public String getAsShortText(long instant, Locale locale) {
            if (instant >= iCutover) {
                return iGregorianField.getAsShortText(instant, locale);
            } else {
                return iJulianField.getAsShortText(instant, locale);
            }
        }

        public long add(long instant, int value) {
            return iGregorianField.add(instant, value);
        }

        public long add(long instant, long value) {
            return iGregorianField.add(instant, value);
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
                        throw new IllegalArgumentException
                            ("Illegal value for " + iGregorianField.getName() + ": " + value);
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
                        throw new IllegalArgumentException
                            ("Illegal value for " + iJulianField.getName() + ": " + value);
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
            DurationField rangeField = iGregorianField.getRangeDurationField();
            if (rangeField == null) {
                rangeField = iJulianField.getRangeDurationField();
            }
            return rangeField;
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
    }

    /**
     * Cutover field for variable length fields. These fields internally call
     * set whenever add is called. As a result, the same correction applied to
     * set must be applied to add and addWrapped. Knowing when to use this
     * field requires specific knowledge of how the GJ fields are implemented.
     */
    private final class ImpreciseCutoverField extends CutoverField {
        static final long serialVersionUID = 3410248757173576441L;

        /**
         * Creates a duration field that links back to this.
         */
        ImpreciseCutoverField(DateTimeField julianField, DateTimeField gregorianField) {
            this(julianField, gregorianField, null);
        }

        /**
         * Uses a shared duration field rather than creating a new one.
         *
         * @param durationField shared duration field
         */
        ImpreciseCutoverField(DateTimeField julianField, DateTimeField gregorianField,
                              DurationField durationField)
        {
            super(julianField, gregorianField);
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
                        instant = gregorianToJulian(instant);
                    }
                }
            } else {
                instant = iJulianField.add(instant, value);
                if (instant >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant - iGapDuration >= iCutover) {
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
                        instant = gregorianToJulian(instant);
                    }
                }
            } else {
                instant = iJulianField.add(instant, value);
                if (instant >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant - iGapDuration >= iCutover) {
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

    /**
     * Links the duration back to a ImpreciseCutoverField.
     */
    private static class LinkedDurationField extends DecoratedDurationField {
        static final long serialVersionUID = 4097975388007713084L;

        private final ImpreciseCutoverField iField;

        LinkedDurationField(DurationField durationField, ImpreciseCutoverField dateTimeField) {
            super(durationField, durationField.getName());
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
