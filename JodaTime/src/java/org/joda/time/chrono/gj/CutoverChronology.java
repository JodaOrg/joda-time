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

/**
 * Chronology for supporting the cutover from the Julian calendar to the
 * Gregorian calendar.
 * 
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
final class CutoverChronology extends GJChronology {
    /**
     * Convert a datetime from one chronology to another.
     */
    private static long convert(long millis, Chronology from, Chronology to) {
        if (from == to) {
            return millis;
        }

        int year = from.year().get(millis);
        int monthOfYear = from.monthOfYear().get(millis);
        int dayOfMonth = from.dayOfMonth().get(millis);
        int millisOfDay = from.millisOfDay().get(millis);

        millis = to.year().set(0, year);
        millis = to.monthOfYear().set(millis, monthOfYear);
        millis = to.dayOfMonth().set(millis, dayOfMonth);
        millis = to.millisOfDay().set(millis, millisOfDay);

        return millis;
    }

    private static void checkUTC(Chronology chrono) {
        if (chrono.getDateTimeZone() != null &&
            chrono.getDateTimeZone() != DateTimeZone.UTC) {
            throw new IllegalArgumentException();
        }
    }

    private final GJChronology iJulianChronology;
    private final GJChronology iGregorianChronology;

    final long iCutoverMillis;
    transient final long iGapMillis;

    /**
     * @param julian chronology used before the cutover instant
     * @param gregorian chronology used at and after the cutover instant
     * @param cutoverMillis instant when the gregorian chronology began
     */
    CutoverChronology(JulianChronology julian, GregorianChronology gregorian, long cutoverMillis) {
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
        iCutoverMillis = cutoverMillis;

        // Compute difference between the chronologies at the cutover instant
        iGapMillis = cutoverMillis - julianToGregorian(cutoverMillis);

        // Begin field definitions.

        // Assuming cutover is at midnight, all time of day fields can be
        // gregorian since they are unaffected by cutover.
        iMillisOfSecondField = gregorian.millisOfSecond();
        iMillisOfDayField = gregorian.millisOfDay();
        iSecondOfMinuteField = gregorian.secondOfMinute();
        iSecondOfDayField = gregorian.secondOfDay();
        iMinuteOfHourField = gregorian.minuteOfHour();
        iMinuteOfDayField = gregorian.minuteOfDay();
        iHourOfDayField = gregorian.hourOfDay();
        iHourOfHalfdayField = gregorian.hourOfHalfday();
        iClockhourOfDayField = gregorian.clockhourOfDay();
        iClockhourOfHalfdayField = gregorian.clockhourOfHalfday();
        iHalfdayOfDayField = gregorian.halfdayOfDay();

        // Verify assumption.
        if (gregorian.millisOfDay().get(cutoverMillis) == 0) {
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
        iEraField = new CutoverField(julian.era(), gregorian.era());
        iDayOfMonthField = new CutoverField(julian.dayOfMonth(), gregorian.dayOfMonth());

        // These fields require special attention when add is called since they
        // internally call set.
        iYearField = new CutoverVarField(julian.year(), gregorian.year());
        iYearOfEraField = new CutoverVarField(julian.yearOfEra(), gregorian.yearOfEra());
        iYearOfCenturyField = new CutoverVarField(julian.yearOfCentury(), gregorian.yearOfCentury());
        iCenturyOfEraField = new CutoverVarField(julian.centuryOfEra(), gregorian.centuryOfEra());
        iMonthOfYearField = new CutoverVarField(julian.monthOfYear(), gregorian.monthOfYear());
        iWeekyearField = new CutoverVarField(julian.weekyear(), gregorian.weekyear());

        // DayOfYear and weekOfWeekyear require special handling since cutover
        // year has fewer days and weeks. Extend the cutover to the start of
        // the next year or weekyear. This keeps the sequence unbroken during
        // the cutover year.

        {
            long cutover = gregorian.year().roundCeiling(iCutoverMillis);
            iDayOfYearField = new CutoverField
                (julian.dayOfYear(), gregorian.dayOfYear(), cutover);
        }

        {
            long cutover = gregorian.weekyear().roundCeiling(iCutoverMillis);
            iWeekOfWeekyearField = new CutoverField
                (julian.weekOfWeekyear(), gregorian.weekOfWeekyear(), cutover);
        }

        // Day of week is unaffected by cutover. Either julian or gregorian will work.
        iDayOfWeekField = gregorian.dayOfWeek();
    }

    public Chronology withUTC() {
        return this;
    }

    public long getGregorianJulianCutoverMillis() {
        return iCutoverMillis;
    }

    public boolean isCenturyISO() {
        return iGregorianChronology.isCenturyISO();
    }

    public final int getMinimumDaysInFirstWeek() {
        return iGregorianChronology.getMinimumDaysInFirstWeek();
    }

    long julianToGregorian(long millis) {
        return convert(millis, iJulianChronology, iGregorianChronology);
    }

    long gregorianToJulian(long millis) {
        return convert(millis, iGregorianChronology, iJulianChronology);
    }

    /**
     * This basic cutover field adjusts calls to 'get' and 'set' methods, and
     * assumes that calls to add and addWrapped are unaffected by the cutover.
     */
    private class CutoverField extends DateTimeField {
        final DateTimeField iJulianField;
        final DateTimeField iGregorianField;
        final long iCutover;

        /**
         * @param julianField field from the chronology used before the cutover instant
         * @param gregorianField field from the chronology used at and after the cutover
         */
        CutoverField(DateTimeField julianField, DateTimeField gregorianField) {
            super(gregorianField.getName());
            iJulianField = julianField;
            iGregorianField = gregorianField;
            iCutover = iCutoverMillis;
        }

        CutoverField(DateTimeField julianField, DateTimeField gregorianField, long cutoverMillis) {
            super(gregorianField.getName());
            iJulianField = julianField;
            iGregorianField = gregorianField;
            iCutover = cutoverMillis;
        }

        public int get(long millis) {
            if (millis >= iCutover) {
                return iGregorianField.get(millis);
            } else {
                return iJulianField.get(millis);
            }
        }

        public String getAsText(long millis, Locale locale) {
            if (millis >= iCutover) {
                return iGregorianField.getAsText(millis, locale);
            } else {
                return iJulianField.getAsText(millis, locale);
            }
        }

        public String getAsShortText(long millis, Locale locale) {
            if (millis >= iCutover) {
                return iGregorianField.getAsShortText(millis, locale);
            } else {
                return iJulianField.getAsShortText(millis, locale);
            }
        }

        public long add(long millis, int value) {
            return iGregorianField.add(millis, value);
        }

        public long add(long millis, long value) {
            return iGregorianField.add(millis, value);
        }

        public long addWrapped(long millis, int value) {
            return iGregorianField.addWrapped(millis, value);
        }

        public long getDifference(long minuendMillis, long subtrahendMillis) {
            return iGregorianField.getDifference(minuendMillis, subtrahendMillis);
        }

        public long set(long millis, int value) {
            if (millis >= iCutover) {
                millis = iGregorianField.set(millis, value);
                if (millis < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (millis + iGapMillis < iCutover) {
                        millis = gregorianToJulian(millis);
                    }
                    // Verify that new value stuck.
                    if (get(millis) != value) {
                        throw new IllegalArgumentException
                            ("Illegal value for " + iGregorianField.getName() + ": " + value);
                    }
                }
            } else {
                millis = iJulianField.set(millis, value);
                if (millis >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (millis - iGapMillis >= iCutover) {
                        millis = julianToGregorian(millis);
                    }
                    // Verify that new value stuck.
                    if (get(millis) != value) {
                        throw new IllegalArgumentException
                            ("Illegal value for " + iJulianField.getName() + ": " + value);
                    }
                }
            }
            return millis;
        }

        public long set(long millis, String text, Locale locale) {
            if (millis >= iCutover) {
                millis = iGregorianField.set(millis, text, locale);
                if (millis < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (millis + iGapMillis < iCutover) {
                        millis = gregorianToJulian(millis);
                    }
                    // Cannot verify that new value stuck because set may be lenient.
                }
            } else {
                millis = iJulianField.set
                    (millis, text, locale);
                if (millis >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (millis - iGapMillis >= iCutover) {
                        millis = julianToGregorian(millis);
                    }
                    // Cannot verify that new value stuck because set may be lenient.
                }
            }
            return millis;
        }

        public boolean isLeap(long millis) {
            if (millis >= iCutover) {
                return iGregorianField.isLeap(millis);
            } else {
                return iJulianField.isLeap(millis);
            }
        }

        public int getLeapAmount(long millis) {
            if (millis >= iCutover) {
                return iGregorianField.getLeapAmount(millis);
            } else {
                return iJulianField.getLeapAmount(millis);
            }
        }

        public long getUnitMillis() {
            // Since getUnitSize doesn't accept a millis argument, return
            // Gregorian unit size because it is more accurate.
            return iGregorianField.getUnitMillis();
        }

        public long getRangeMillis() {
            return iGregorianField.getRangeMillis();
        }

        // Note on getMinimumValue and getMaximumValue: For all fields but
        // year, yearOfEra, and centuryOfEra, the Julian and Gregorian limits
        // are identical. The Julian limit is returned for getMaximumValue
        // because it is smaller than the Gregorian limit. This is to prevent
        // calling a field mutator that advances so far beyond the gap that the
        // Julian calendar overflows.

        public int getMinimumValue() {
            return iJulianField.getMinimumValue();
        }
        
        public int getMinimumValue(long millis) {
            if (millis >= iCutover) {
                return iGregorianField.getMinimumValue(millis);
            } else {
                return iJulianField.getMinimumValue(millis);
            }
        }

        public int getMaximumValue() {
            return iJulianField.getMaximumValue();
        }

        public int getMaximumValue(long millis) {
            if (millis >= iCutover) {
                return iGregorianField.getMaximumValue(millis);
            } else {
                return iJulianField.getMaximumValue(millis);
            }
        }

        public long roundFloor(long millis) {
            if (millis >= iCutover) {
                millis = iGregorianField.roundFloor(millis);
                if (millis < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (millis + iGapMillis < iCutover) {
                        millis = gregorianToJulian(millis);
                    }
                }
            } else {
                millis = iJulianField.roundFloor(millis);
            }
            return millis;
        }

        public long roundCeiling(long millis) {
            if (millis >= iCutover) {
                millis = iGregorianField.roundCeiling(millis);
            } else {
                millis = iJulianField.roundCeiling(millis);
                if (millis >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (millis - iGapMillis >= iCutover) {
                        millis = julianToGregorian(millis);
                    }
                }
            }
            return millis;
        }

        public long remainder(long millis) {
            if (millis >= iCutover) {
                millis = iGregorianField.remainder(millis);
                if (millis < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (millis + iGapMillis < iCutover) {
                        millis = gregorianToJulian(millis);
                    }
                }
            } else {
                millis = iJulianField.remainder(millis);
                if (millis >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (millis - iGapMillis >= iCutover) {
                        millis = julianToGregorian(millis);
                    }
                }
            }
            return millis;
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
    private class CutoverVarField extends CutoverField {
        CutoverVarField(DateTimeField julianField, DateTimeField gregorianField) {
            super(julianField, gregorianField);
        }

        public long add(long millis, int value) {
            if (millis >= iCutover) {
                millis = iGregorianField.add(millis, value);
                if (millis < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (millis + iGapMillis < iCutover) {
                        millis = gregorianToJulian(millis);
                    }
                }
            } else {
                millis = iJulianField.add(millis, value);
                if (millis >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (millis - iGapMillis >= iCutover) {
                        millis = julianToGregorian(millis);
                    }
                }
            }
            return millis;
        }
        
        public long add(long millis, long value) {
            if (millis >= iCutover) {
                millis = iGregorianField.add(millis, value);
                if (millis < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (millis + iGapMillis < iCutover) {
                        millis = gregorianToJulian(millis);
                    }
                }
            } else {
                millis = iJulianField.add(millis, value);
                if (millis >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (millis - iGapMillis >= iCutover) {
                        millis = julianToGregorian(millis);
                    }
                }
            }
            return millis;
        }

        public long addWrapped(long millis, int value) {
            if (millis >= iCutover) {
                millis = iGregorianField.addWrapped(millis, value);
                if (millis < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (millis + iGapMillis < iCutover) {
                        millis = gregorianToJulian(millis);
                    }
                }
            } else {
                millis = iJulianField.addWrapped(millis, value);
                if (millis >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (millis - iGapMillis >= iCutover) {
                        millis = julianToGregorian(millis);
                    }
                }
            }
            return millis;
        }

        public long getDifference(long minuendMillis, long subtrahendMillis) {
            if (minuendMillis >= iCutover) {
                if (subtrahendMillis >= iCutover) {
                    return iGregorianField.getDifference(minuendMillis, subtrahendMillis);
                }
                // Remember, the add is being reversed. Since subtrahend is
                // Julian, convert minuend to Julian to match.
                minuendMillis = gregorianToJulian(minuendMillis);
                return iJulianField.getDifference(minuendMillis, subtrahendMillis);
            } else {
                if (subtrahendMillis < iCutover) {
                    return iJulianField.getDifference(minuendMillis, subtrahendMillis);
                }
                // Remember, the add is being reversed. Since subtrahend is
                // Gregorian, convert minuend to Gregorian to match.
                minuendMillis = julianToGregorian(minuendMillis);
                return iGregorianField.getDifference(minuendMillis, subtrahendMillis);
            }
        }
    }
}
