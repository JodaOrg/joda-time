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

import java.io.IOException;
import java.io.ObjectInputStream;

import java.util.HashMap;
import java.util.Locale;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;

/**
 * Wraps another Chronology for supporting time zones.
 * <p>
 * ZonedChronology is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public class ZonedChronology extends Chronology {

    static final long serialVersionUID = -1079258847191166848L;

    static boolean useTimeArithmetic(DurationField field) {
        // Use time of day arithmetic rules for unit durations less than
        // typical time zone offsets.
        return field != null && field.getUnitMillis() < DateTimeConstants.MILLIS_PER_HOUR * 12;
    }

    private final Chronology iChronology;
    private final DateTimeZone iZone;

    private transient DurationField iErasField;
    private transient DurationField iCenturiesField;
    private transient DurationField iYearsField;
    private transient DurationField iMonthsField;
    private transient DurationField iWeekyearsField;
    private transient DurationField iWeeksField;
    private transient DurationField iDaysField;

    private transient DurationField iHoursField;
    private transient DurationField iMinutesField;
    private transient DurationField iSecondsField;
    private transient DurationField iMillisField;

    private transient DateTimeField iYearField;
    private transient DateTimeField iYearOfEraField;
    private transient DateTimeField iYearOfCenturyField;
    private transient DateTimeField iCenturyOfEraField;
    private transient DateTimeField iEraField;
    private transient DateTimeField iDayOfWeekField;
    private transient DateTimeField iDayOfMonthField;
    private transient DateTimeField iDayOfYearField;
    private transient DateTimeField iMonthOfYearField;
    private transient DateTimeField iWeekOfWeekyearField;
    private transient DateTimeField iWeekyearField;

    private transient DateTimeField iMillisOfSecondField;
    private transient DateTimeField iMillisOfDayField;
    private transient DateTimeField iSecondOfMinuteField;
    private transient DateTimeField iSecondOfDayField;
    private transient DateTimeField iMinuteOfHourField;
    private transient DateTimeField iMinuteOfDayField;
    private transient DateTimeField iHourOfDayField;
    private transient DateTimeField iHourOfHalfdayField;
    private transient DateTimeField iClockhourOfDayField;
    private transient DateTimeField iClockhourOfHalfdayField;
    private transient DateTimeField iHalfdayOfDayField;

    /**
     * Create a ZonedChronology for any chronology, overriding any time zone it
     * may already have.
     *
     * @param chrono the chronology
     * @param zone the time zone
     * @throws IllegalArgumentException if chronology or time zone is null
     */
    public ZonedChronology(Chronology chrono, DateTimeZone zone) {
        if (chrono == null) {
            throw new IllegalArgumentException("Must supply a chronology");
        }
        chrono = chrono.withUTC();
        if (chrono == null) {
            throw new IllegalArgumentException("UTC chronology must not be null");
        }
        if (zone == null) {
            throw new IllegalArgumentException("DateTimeZone must not be null");
        }

        iChronology = chrono;
        iZone = zone;
        setFields();
    }

    private void setFields() {
        Chronology c = iChronology;

        // Keep a local cache of converted fields so as not to create redundant
        // objects.
        HashMap converted = new HashMap();

        // Convert duration fields...

        iErasField = convertField(c.eras(), converted);
        iCenturiesField = convertField(c.centuries(), converted);
        iYearsField = convertField(c.years(), converted);
        iMonthsField = convertField(c.months(), converted);
        iWeekyearsField = convertField(c.weekyears(), converted);
        iWeeksField = convertField(c.weeks(), converted);
        iDaysField = convertField(c.days(), converted);

        iHoursField = convertField(c.hours(), converted);
        iMinutesField = convertField(c.minutes(), converted);
        iSecondsField = convertField(c.seconds(), converted);
        iMillisField = convertField(c.millis(), converted);

        // Convert datetime fields...

        iYearField = convertField(c.year(), converted);
        iYearOfEraField = convertField(c.yearOfEra(), converted);
        iYearOfCenturyField = convertField(c.yearOfCentury(), converted);
        iCenturyOfEraField = convertField(c.centuryOfEra(), converted);
        iEraField = convertField(c.era(), converted);
        iDayOfWeekField = convertField(c.dayOfWeek(), converted);
        iDayOfMonthField = convertField(c.dayOfMonth(), converted);
        iDayOfYearField = convertField(c.dayOfYear(), converted);
        iMonthOfYearField = convertField(c.monthOfYear(), converted);
        iWeekOfWeekyearField = convertField(c.weekOfWeekyear(), converted);
        iWeekyearField = convertField(c.weekyear(), converted);

        iMillisOfSecondField = convertField(c.millisOfSecond(), converted);
        iMillisOfDayField = convertField(c.millisOfDay(), converted);
        iSecondOfMinuteField = convertField(c.secondOfMinute(), converted);
        iSecondOfDayField = convertField(c.secondOfDay(), converted);
        iMinuteOfHourField = convertField(c.minuteOfHour(), converted);
        iMinuteOfDayField = convertField(c.minuteOfDay(), converted);
        iHourOfDayField = convertField(c.hourOfDay(), converted);
        iHourOfHalfdayField = convertField(c.hourOfHalfday(), converted);
        iClockhourOfDayField = convertField(c.clockhourOfDay(), converted);
        iClockhourOfHalfdayField = convertField(c.clockhourOfHalfday(), converted);
        iHalfdayOfDayField = convertField(c.halfdayOfDay(), converted);
    }

    private DurationField convertField(DurationField field, HashMap converted) {
        if (field == null || !field.isSupported()) {
            return field;
        }
        if (converted.containsKey(field)) {
            return (DurationField)converted.get(field);
        }
        ZonedDurationField zonedField = new ZonedDurationField(field, iZone);
        converted.put(field, zonedField);
        return zonedField;
    }

    private DateTimeField convertField(DateTimeField field, HashMap converted) {
        if (field == null || !field.isSupported()) {
            return field;
        }
        if (converted.containsKey(field)) {
            return (DateTimeField)converted.get(field);
        }
        ZonedDateTimeField zonedField =
            new ZonedDateTimeField(field, iZone,
                                   convertField(field.getDurationField(), converted),
                                   convertField(field.getRangeDurationField(), converted),
                                   convertField(field.getLeapDurationField(), converted));
        converted.put(field, zonedField);
        return zonedField;
    }

    public DateTimeZone getDateTimeZone() {
        return iZone;
    }

    public Chronology withUTC() {
        return iChronology;
    }

    public Chronology withDateTimeZone(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        if (zone == iZone) {
            return this;
        }
        if (zone == DateTimeZone.UTC) {
            return iChronology;
        }
        return new ZonedChronology(iChronology, zone);
    }

    public long getDateOnlyMillis(int year, int monthOfYear, int dayOfMonth)
        throws IllegalArgumentException
    {
        return localToUTC(iChronology.getDateOnlyMillis
                          (year, monthOfYear, dayOfMonth));
    }

    public long getTimeOnlyMillis(int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        return localToUTC(iChronology.getTimeOnlyMillis
                          (hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond));
    }

    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int millisOfDay)
        throws IllegalArgumentException
    {
        return localToUTC(iChronology.getDateTimeMillis
                          (year, monthOfYear, dayOfMonth, millisOfDay));
    }

    public long getDateTimeMillis(long instant,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        return localToUTC(iChronology.getDateTimeMillis
                          (instant + iZone.getOffset(instant),
                           hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond));
    }

    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        return localToUTC(iChronology.getDateTimeMillis
                          (year, monthOfYear, dayOfMonth, 
                           hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond));
    }

    /**
     * @param instant instant from 1970-01-01T00:00:00 local time
     */
    private long localToUTC(long instant) {
        int offset = iZone.getOffsetFromLocal(instant);
        instant -= offset;
        if (offset != iZone.getOffset(instant)) {
            throw new IllegalArgumentException
                ("Illegal instant due to time zone offset transition");
        }
        return instant;
    }

    // Milliseconds
    //------------------------------------------------------------

    public DurationField millis() {
        return iMillisField;
    }

    public DateTimeField millisOfSecond() {
        return iMillisOfSecondField;
    }

    public DateTimeField millisOfDay() {
        return iMillisOfDayField;
    }

    // Seconds
    //------------------------------------------------------------

    public DurationField seconds() {
        return iSecondsField;
    }

    public DateTimeField secondOfMinute() {
        return iSecondOfMinuteField;
    }

    public DateTimeField secondOfDay() {
        return iSecondOfDayField;
    }

    // Minutes
    //------------------------------------------------------------

    public DurationField minutes() {
        return iMinutesField;
    }

    public DateTimeField minuteOfHour() {
        return iMinuteOfHourField;
    }

    public DateTimeField minuteOfDay() {
        return iMinuteOfDayField;
    }

    // Hours
    //------------------------------------------------------------

    public DurationField hours() {
        return iHoursField;
    }

    public DateTimeField hourOfDay() {
        return iHourOfDayField;
    }

    public DateTimeField clockhourOfDay() {
        return iClockhourOfDayField;
    }

    public DateTimeField hourOfHalfday() {
        return iHourOfHalfdayField;
    }

    public DateTimeField clockhourOfHalfday() {
        return iClockhourOfHalfdayField;
    }

    public DateTimeField halfdayOfDay() {
        return iHalfdayOfDayField;
    }

    // Day
    //------------------------------------------------------------

    public DurationField days() {
        return iDaysField;
    }

    public DateTimeField dayOfWeek() {
        return iDayOfWeekField;
    }

    public DateTimeField dayOfMonth() {
        return iDayOfMonthField;
    }

    public DateTimeField dayOfYear() {
        return iDayOfYearField;
    }

    // Week
    //------------------------------------------------------------

    public DurationField weeks() {
        return iWeeksField;
    }

    public DateTimeField weekOfWeekyear() {
        return iWeekOfWeekyearField;
    }

    public DurationField weekyears() {
        return iWeekyearsField;
    }

    public DateTimeField weekyear() {
        return iWeekyearField;
    }

    // Month
    //------------------------------------------------------------

    public DurationField months() {
        return iMonthsField;
    }

    public DateTimeField monthOfYear() {
        return iMonthOfYearField;
    }

    // Year
    //------------------------------------------------------------

    public DurationField years() {
        return iYearsField;
    }

    public DateTimeField year() {
        return iYearField;
    }

    public DateTimeField yearOfEra() {
        return iYearOfEraField;
    }

    public DateTimeField yearOfCentury() {
        return iYearOfCenturyField;
    }

    public DurationField centuries() {
        return iCenturiesField;
    }

    public DateTimeField centuryOfEra() {
        return iCenturyOfEraField;
    }

    public DurationField eras() {
        return iErasField;
    }

    public DateTimeField era() {
        return iEraField;
    }

    public String toString() {
        return iChronology.toString();
    }

    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        setFields();
    }

    /*
     * Because time durations are typically smaller than time zone offsets, the
     * arithmetic methods subtract the original offset. This produces a more
     * expected behavior when crossing time zone offset transitions. For dates,
     * the new offset is subtracted off. This behavior, if applied to time
     * fields, can nullify or reverse an add when crossing a transition.
     */

    static class ZonedDurationField extends AbstractDurationField {
        static final long serialVersionUID = -485345310999208286L;

        final DurationField iField;
        final boolean iTimeField;
        final DateTimeZone iZone;

        ZonedDurationField(DurationField field, DateTimeZone zone) {
            super(field.getName());
            if (!field.isSupported()) {
                throw new IllegalArgumentException();
            }
            iField = field;
            iTimeField = useTimeArithmetic(field);
            this.iZone = zone;
        }

        public boolean isPrecise() {
            return iTimeField ? iField.isPrecise() : iZone.isFixed();
        }

        public long getUnitMillis() {
            return iField.getUnitMillis();
        }

        public int getValue(long duration, long instant) {
            return iField.getValue(duration, instant + this.iZone.getOffset(instant));
        }

        public long getValueAsLong(long duration, long instant) {
            return iField.getValueAsLong(duration, instant + this.iZone.getOffset(instant));
        }

        public long getMillis(int value, long instant) {
            return iField.getMillis(value, instant + this.iZone.getOffset(instant));
        }

        public long getMillis(long value, long instant) {
            return iField.getMillis(value, instant + this.iZone.getOffset(instant));
        }

        public long add(long instant, int value) {
            int offset = this.iZone.getOffset(instant);
            instant = iField.add(instant + offset, value);
            return instant - (iTimeField ? offset : this.iZone.getOffsetFromLocal(instant));
        }

        public long add(long instant, long value) {
            int offset = this.iZone.getOffset(instant);
            instant = iField.add(instant + offset, value);
            return instant - (iTimeField ? offset : this.iZone.getOffsetFromLocal(instant));
        }

        public int getDifference(long minuendInstant, long subtrahendInstant) {
            int offset = this.iZone.getOffset(subtrahendInstant);
            return iField.getDifference
                (minuendInstant + (iTimeField ? offset : this.iZone.getOffset(minuendInstant)),
                 subtrahendInstant + offset);
        }

        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            int offset = this.iZone.getOffset(subtrahendInstant);
            return iField.getDifferenceAsLong
                (minuendInstant + (iTimeField ? offset : this.iZone.getOffset(minuendInstant)),
                 subtrahendInstant + offset);
        }
    }

    /**
     * A DateTimeField that decorates another to add timezone behaviour.
     * <p>
     * This class converts passed in instants to local wall time, and vice
     * versa on output.
     */
    static final class ZonedDateTimeField extends AbstractDateTimeField {
        static final long serialVersionUID = -3968986277775529794L;

        final DateTimeField iField;
        final DateTimeZone iZone;
        final DurationField iDurationField;
        final boolean iTimeField;
        final DurationField iRangeDurationField;
        final DurationField iLeapDurationField;

        ZonedDateTimeField(DateTimeField field,
                           DateTimeZone zone,
                           DurationField durationField,
                           DurationField rangeDurationField,
                           DurationField leapDurationField) {
            super(field.getName());
            if (!field.isSupported()) {
                throw new IllegalArgumentException();
            }
            iField = field;
            this.iZone = zone;
            iDurationField = durationField;
            iTimeField = useTimeArithmetic(durationField);
            iRangeDurationField = rangeDurationField;
            iLeapDurationField = leapDurationField;
        }

        public boolean isLenient() {
            return iField.isLenient();
        }

        public int get(long instant) {
            return iField.get(instant + this.iZone.getOffset(instant));
        }

        public String getAsText(long instant, Locale locale) {
            return iField.getAsText(instant + this.iZone.getOffset(instant), locale);
        }

        public String getAsShortText(long instant, Locale locale) {
            return iField.getAsShortText(instant + this.iZone.getOffset(instant), locale);
        }

        public long add(long instant, int value) {
            int offset = this.iZone.getOffset(instant);
            instant = iField.add(instant + offset, value);
            return instant - (iTimeField ? offset : this.iZone.getOffsetFromLocal(instant));
        }

        public long add(long instant, long value) {
            int offset = this.iZone.getOffset(instant);
            instant = iField.add(instant + offset, value);
            return instant - (iTimeField ? offset : this.iZone.getOffsetFromLocal(instant));
        }

        public long addWrapped(long instant, int value) {
            int offset = this.iZone.getOffset(instant);
            instant = iField.addWrapped(instant + offset, value);
            return instant - (iTimeField ? offset : this.iZone.getOffsetFromLocal(instant));
        }

        public long set(long instant, int value) {
            long offset = this.iZone.getOffset(instant);

            instant = iField.set(instant + offset, value);
            long offsetFromLocal = this.iZone.getOffsetFromLocal(instant);
            instant -= offsetFromLocal;

            if (offset != offsetFromLocal) {
                if (get(instant) != value) {
                    throw new IllegalArgumentException
                        ("Illegal value for " + iField.getName() + ": " + value);
                }
            }

            return instant;
        }

        public long set(long instant, String text, Locale locale) {
            instant = iField.set(instant + this.iZone.getOffset(instant), text, locale);
            // Cannot verify that new value stuck because set may be lenient.
            return instant - this.iZone.getOffsetFromLocal(instant);
        }

        public int getDifference(long minuendInstant, long subtrahendInstant) {
            int offset = this.iZone.getOffset(subtrahendInstant);
            return iField.getDifference
                (minuendInstant + (iTimeField ? offset : this.iZone.getOffset(minuendInstant)),
                 subtrahendInstant + offset);
        }

        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            int offset = this.iZone.getOffset(subtrahendInstant);
            return iField.getDifferenceAsLong
                (minuendInstant + (iTimeField ? offset : this.iZone.getOffset(minuendInstant)),
                 subtrahendInstant + offset);
        }

        public final DurationField getDurationField() {
            return iDurationField;
        }

        public final DurationField getRangeDurationField() {
            return iRangeDurationField;
        }

        public boolean isLeap(long instant) {
            return iField.isLeap(instant + this.iZone.getOffset(instant));
        }

        public int getLeapAmount(long instant) {
            return iField.getLeapAmount(instant + this.iZone.getOffset(instant));
        }

        public final DurationField getLeapDurationField() {
            return iLeapDurationField;
        }

        public long roundFloor(long instant) {
            int offset = this.iZone.getOffset(instant);
            instant = iField.roundFloor(instant + offset);
            return instant - (iTimeField ? offset : this.iZone.getOffsetFromLocal(instant));
        }

        public long roundCeiling(long instant) {
            int offset = this.iZone.getOffset(instant);
            instant = iField.roundCeiling(instant + offset);
            return instant - (iTimeField ? offset : this.iZone.getOffsetFromLocal(instant));
        }

        public long remainder(long instant) {
            return iField.remainder(instant + this.iZone.getOffset(instant));
        }

        public int getMinimumValue() {
            return iField.getMinimumValue();
        }

        public int getMinimumValue(long instant) {
            return iField.getMinimumValue(instant + this.iZone.getOffset(instant));
        }

        public int getMaximumValue() {
            return iField.getMaximumValue();
        }

        public int getMaximumValue(long instant) {
            return iField.getMaximumValue(instant + this.iZone.getOffset(instant));
        }

        public int getMaximumTextLength(Locale locale) {
            return iField.getMaximumTextLength(locale);
        }

        public int getMaximumShortTextLength(Locale locale) {
            return iField.getMaximumShortTextLength(locale);
        }
    }

}
