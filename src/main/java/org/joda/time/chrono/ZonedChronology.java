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

import java.util.HashMap;
import java.util.Locale;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.IllegalInstantException;
import org.joda.time.ReadablePartial;
import org.joda.time.field.BaseDateTimeField;
import org.joda.time.field.BaseDurationField;

/**
 * Wraps another Chronology to add support for time zones.
 * <p>
 * ZonedChronology is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public final class ZonedChronology extends AssembledChronology {

    /** Serialization lock */
    private static final long serialVersionUID = -1079258847191166848L;

    /**
     * Create a ZonedChronology for any chronology, overriding any time zone it
     * may already have.
     *
     * @param base base chronology to wrap
     * @param zone the time zone
     * @throws IllegalArgumentException if chronology or time zone is null
     */
    public static ZonedChronology getInstance(Chronology base, DateTimeZone zone) {
        if (base == null) {
            throw new IllegalArgumentException("Must supply a chronology");
        }
        base = base.withUTC();
        if (base == null) {
            throw new IllegalArgumentException("UTC chronology must not be null");
        }
        if (zone == null) {
            throw new IllegalArgumentException("DateTimeZone must not be null");
        }
        return new ZonedChronology(base, zone);
    }

    static boolean useTimeArithmetic(DurationField field) {
        // Use time of day arithmetic rules for unit durations less than
        // typical time zone offsets.
        return field != null && field.getUnitMillis() < DateTimeConstants.MILLIS_PER_HOUR * 12;
    }

    /**
     * Restricted constructor
     *
     * @param base base chronology to wrap
     * @param zone the time zone
     */
    private ZonedChronology(Chronology base, DateTimeZone zone) {
        super(base, zone);
    }

    public DateTimeZone getZone() {
        return (DateTimeZone)getParam();
    }

    public Chronology withUTC() {
        return getBase();
    }

    public Chronology withZone(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        if (zone == getParam()) {
            return this;
        }
        if (zone == DateTimeZone.UTC) {
            return getBase();
        }
        return new ZonedChronology(getBase(), zone);
    }

    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int millisOfDay)
        throws IllegalArgumentException
    {
        return localToUTC(getBase().getDateTimeMillis
                          (year, monthOfYear, dayOfMonth, millisOfDay));
    }

    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        return localToUTC(getBase().getDateTimeMillis
                          (year, monthOfYear, dayOfMonth, 
                           hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond));
    }

    public long getDateTimeMillis(long instant,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        return localToUTC(getBase().getDateTimeMillis
                          (instant + getZone().getOffset(instant),
                           hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond));
    }

    /**
     * @param localInstant  the instant from 1970-01-01T00:00:00 local time
     * @return the instant from 1970-01-01T00:00:00Z
     */
    private long localToUTC(long localInstant) {
        DateTimeZone zone = getZone();
        int offset = zone.getOffsetFromLocal(localInstant);
        localInstant -= offset;
        if (offset != zone.getOffset(localInstant)) {
            throw new IllegalInstantException(localInstant, zone.getID());
        }
        return localInstant;
    }

    protected void assemble(Fields fields) {
        // Keep a local cache of converted fields so as not to create redundant
        // objects.
        HashMap<Object, Object> converted = new HashMap<Object, Object>();

        // Convert duration fields...

        fields.eras = convertField(fields.eras, converted);
        fields.centuries = convertField(fields.centuries, converted);
        fields.years = convertField(fields.years, converted);
        fields.months = convertField(fields.months, converted);
        fields.weekyears = convertField(fields.weekyears, converted);
        fields.weeks = convertField(fields.weeks, converted);
        fields.days = convertField(fields.days, converted);

        fields.halfdays = convertField(fields.halfdays, converted);
        fields.hours = convertField(fields.hours, converted);
        fields.minutes = convertField(fields.minutes, converted);
        fields.seconds = convertField(fields.seconds, converted);
        fields.millis = convertField(fields.millis, converted);

        // Convert datetime fields...

        fields.year = convertField(fields.year, converted);
        fields.yearOfEra = convertField(fields.yearOfEra, converted);
        fields.yearOfCentury = convertField(fields.yearOfCentury, converted);
        fields.centuryOfEra = convertField(fields.centuryOfEra, converted);
        fields.era = convertField(fields.era, converted);
        fields.dayOfWeek = convertField(fields.dayOfWeek, converted);
        fields.dayOfMonth = convertField(fields.dayOfMonth, converted);
        fields.dayOfYear = convertField(fields.dayOfYear, converted);
        fields.monthOfYear = convertField(fields.monthOfYear, converted);
        fields.weekOfWeekyear = convertField(fields.weekOfWeekyear, converted);
        fields.weekyear = convertField(fields.weekyear, converted);
        fields.weekyearOfCentury = convertField(fields.weekyearOfCentury, converted);

        fields.millisOfSecond = convertField(fields.millisOfSecond, converted);
        fields.millisOfDay = convertField(fields.millisOfDay, converted);
        fields.secondOfMinute = convertField(fields.secondOfMinute, converted);
        fields.secondOfDay = convertField(fields.secondOfDay, converted);
        fields.minuteOfHour = convertField(fields.minuteOfHour, converted);
        fields.minuteOfDay = convertField(fields.minuteOfDay, converted);
        fields.hourOfDay = convertField(fields.hourOfDay, converted);
        fields.hourOfHalfday = convertField(fields.hourOfHalfday, converted);
        fields.clockhourOfDay = convertField(fields.clockhourOfDay, converted);
        fields.clockhourOfHalfday = convertField(fields.clockhourOfHalfday, converted);
        fields.halfdayOfDay = convertField(fields.halfdayOfDay, converted);
    }

    private DurationField convertField(DurationField field, HashMap<Object, Object> converted) {
        if (field == null || !field.isSupported()) {
            return field;
        }
        if (converted.containsKey(field)) {
            return (DurationField)converted.get(field);
        }
        ZonedDurationField zonedField = new ZonedDurationField(field, getZone());
        converted.put(field, zonedField);
        return zonedField;
    }

    private DateTimeField convertField(DateTimeField field, HashMap<Object, Object> converted) {
        if (field == null || !field.isSupported()) {
            return field;
        }
        if (converted.containsKey(field)) {
            return (DateTimeField)converted.get(field);
        }
        ZonedDateTimeField zonedField =
            new ZonedDateTimeField(field, getZone(),
                                   convertField(field.getDurationField(), converted),
                                   convertField(field.getRangeDurationField(), converted),
                                   convertField(field.getLeapDurationField(), converted));
        converted.put(field, zonedField);
        return zonedField;
    }

    //-----------------------------------------------------------------------
    /**
     * A zoned chronology is only equal to a zoned chronology with the
     * same base chronology and zone.
     * 
     * @param obj  the object to compare to
     * @return true if equal
     * @since 1.4
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ZonedChronology == false) {
            return false;
        }
        ZonedChronology chrono = (ZonedChronology) obj;
        return
            getBase().equals(chrono.getBase()) &&
            getZone().equals(chrono.getZone());
    }

    /**
     * A suitable hashcode for the chronology.
     * 
     * @return the hashcode
     * @since 1.4
     */
    public int hashCode() {
        return 326565 + getZone().hashCode() * 11 + getBase().hashCode() * 7;
    }

    /**
     * A debugging string for the chronology.
     * 
     * @return the debugging string
     */
    public String toString() {
        return "ZonedChronology[" + getBase() + ", " + getZone().getID() + ']';
    }

    //-----------------------------------------------------------------------
    /*
     * Because time durations are typically smaller than time zone offsets, the
     * arithmetic methods subtract the original offset. This produces a more
     * expected behavior when crossing time zone offset transitions. For dates,
     * the new offset is subtracted off. This behavior, if applied to time
     * fields, can nullify or reverse an add when crossing a transition.
     */
    static class ZonedDurationField extends BaseDurationField {
        private static final long serialVersionUID = -485345310999208286L;

        final DurationField iField;
        final boolean iTimeField;
        final DateTimeZone iZone;

        ZonedDurationField(DurationField field, DateTimeZone zone) {
            super(field.getType());
            if (!field.isSupported()) {
                throw new IllegalArgumentException();
            }
            iField = field;
            iTimeField = useTimeArithmetic(field);
            iZone = zone;
        }

        public boolean isPrecise() {
            return iTimeField ? iField.isPrecise() : iField.isPrecise() && this.iZone.isFixed();
        }

        public long getUnitMillis() {
            return iField.getUnitMillis();
        }

        public int getValue(long duration, long instant) {
            return iField.getValue(duration, addOffset(instant));
        }

        public long getValueAsLong(long duration, long instant) {
            return iField.getValueAsLong(duration, addOffset(instant));
        }

        public long getMillis(int value, long instant) {
            return iField.getMillis(value, addOffset(instant));
        }

        public long getMillis(long value, long instant) {
            return iField.getMillis(value, addOffset(instant));
        }

        public long add(long instant, int value) {
            int offset = getOffsetToAdd(instant);
            instant = iField.add(instant + offset, value);
            return instant - (iTimeField ? offset : getOffsetFromLocalToSubtract(instant));
        }

        public long add(long instant, long value) {
            int offset = getOffsetToAdd(instant);
            instant = iField.add(instant + offset, value);
            return instant - (iTimeField ? offset : getOffsetFromLocalToSubtract(instant));
        }

        public int getDifference(long minuendInstant, long subtrahendInstant) {
            int offset = getOffsetToAdd(subtrahendInstant);
            return iField.getDifference
                (minuendInstant + (iTimeField ? offset : getOffsetToAdd(minuendInstant)),
                 subtrahendInstant + offset);
        }

        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            int offset = getOffsetToAdd(subtrahendInstant);
            return iField.getDifferenceAsLong
                (minuendInstant + (iTimeField ? offset : getOffsetToAdd(minuendInstant)),
                 subtrahendInstant + offset);
        }

        private int getOffsetToAdd(long instant) {
            int offset = this.iZone.getOffset(instant);
            long sum = instant + offset;
            // If there is a sign change, but the two values have the same sign...
            if ((instant ^ sum) < 0 && (instant ^ offset) >= 0) {
                throw new ArithmeticException("Adding time zone offset caused overflow");
            }
            return offset;
        }

        private int getOffsetFromLocalToSubtract(long instant) {
            int offset = this.iZone.getOffsetFromLocal(instant);
            long diff = instant - offset;
            // If there is a sign change, but the two values have different signs...
            if ((instant ^ diff) < 0 && (instant ^ offset) < 0) {
                throw new ArithmeticException("Subtracting time zone offset caused overflow");
            }
            return offset;
        }

        private long addOffset(long instant) {
            return iZone.convertUTCToLocal(instant);
        }
    }

    /**
     * A DateTimeField that decorates another to add timezone behaviour.
     * <p>
     * This class converts passed in instants to local wall time, and vice
     * versa on output.
     */
    static final class ZonedDateTimeField extends BaseDateTimeField {
        private static final long serialVersionUID = -3968986277775529794L;

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
            super(field.getType());
            if (!field.isSupported()) {
                throw new IllegalArgumentException();
            }
            iField = field;
            iZone = zone;
            iDurationField = durationField;
            iTimeField = useTimeArithmetic(durationField);
            iRangeDurationField = rangeDurationField;
            iLeapDurationField = leapDurationField;
        }

        public boolean isLenient() {
            return iField.isLenient();
        }

        public int get(long instant) {
            long localInstant = iZone.convertUTCToLocal(instant);
            return iField.get(localInstant);
        }

        public String getAsText(long instant, Locale locale) {
            long localInstant = iZone.convertUTCToLocal(instant);
            return iField.getAsText(localInstant, locale);
        }

        public String getAsShortText(long instant, Locale locale) {
            long localInstant = iZone.convertUTCToLocal(instant);
            return iField.getAsShortText(localInstant, locale);
        }

        public String getAsText(int fieldValue, Locale locale) {
            return iField.getAsText(fieldValue, locale);
        }

        public String getAsShortText(int fieldValue, Locale locale) {
            return iField.getAsShortText(fieldValue, locale);
        }

        public long add(long instant, int value) {
            if (iTimeField) {
                int offset = getOffsetToAdd(instant);
                long localInstant = iField.add(instant + offset, value);
                return localInstant - offset;
            } else {
               long localInstant = iZone.convertUTCToLocal(instant);
               localInstant = iField.add(localInstant, value);
               return iZone.convertLocalToUTC(localInstant, false, instant);
            }
        }

        public long add(long instant, long value) {
            if (iTimeField) {
                int offset = getOffsetToAdd(instant);
                long localInstant = iField.add(instant + offset, value);
                return localInstant - offset;
            } else {
               long localInstant = iZone.convertUTCToLocal(instant);
               localInstant = iField.add(localInstant, value);
               return iZone.convertLocalToUTC(localInstant, false, instant);
            }
        }

        public long addWrapField(long instant, int value) {
            if (iTimeField) {
                int offset = getOffsetToAdd(instant);
                long localInstant = iField.addWrapField(instant + offset, value);
                return localInstant - offset;
            } else {
                long localInstant = iZone.convertUTCToLocal(instant);
                localInstant = iField.addWrapField(localInstant, value);
                return iZone.convertLocalToUTC(localInstant, false, instant);
            }
        }

        public long set(long instant, int value) {
            long localInstant = iZone.convertUTCToLocal(instant);
            localInstant = iField.set(localInstant, value);
            long result = iZone.convertLocalToUTC(localInstant, false, instant);
            if (get(result) != value) {
                IllegalInstantException cause = new IllegalInstantException(localInstant,  iZone.getID());
                IllegalFieldValueException ex = new IllegalFieldValueException(iField.getType(), Integer.valueOf(value), cause.getMessage());
                ex.initCause(cause);
                throw ex;
            }
            return result;
        }

        public long set(long instant, String text, Locale locale) {
            // cannot verify that new value stuck because set may be lenient
            long localInstant = iZone.convertUTCToLocal(instant);
            localInstant = iField.set(localInstant, text, locale);
            return iZone.convertLocalToUTC(localInstant, false, instant);
        }

        public int getDifference(long minuendInstant, long subtrahendInstant) {
            int offset = getOffsetToAdd(subtrahendInstant);
            return iField.getDifference
                (minuendInstant + (iTimeField ? offset : getOffsetToAdd(minuendInstant)),
                 subtrahendInstant + offset);
        }

        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            int offset = getOffsetToAdd(subtrahendInstant);
            return iField.getDifferenceAsLong
                (minuendInstant + (iTimeField ? offset : getOffsetToAdd(minuendInstant)),
                 subtrahendInstant + offset);
        }

        public final DurationField getDurationField() {
            return iDurationField;
        }

        public final DurationField getRangeDurationField() {
            return iRangeDurationField;
        }

        public boolean isLeap(long instant) {
            long localInstant = iZone.convertUTCToLocal(instant);
            return iField.isLeap(localInstant);
        }

        public int getLeapAmount(long instant) {
            long localInstant = iZone.convertUTCToLocal(instant);
            return iField.getLeapAmount(localInstant);
        }

        public final DurationField getLeapDurationField() {
            return iLeapDurationField;
        }

        public long roundFloor(long instant) {
            if (iTimeField) {
                int offset = getOffsetToAdd(instant);
                instant = iField.roundFloor(instant + offset);
                return instant - offset;
            } else {
                long localInstant = iZone.convertUTCToLocal(instant);
                localInstant = iField.roundFloor(localInstant);
                return iZone.convertLocalToUTC(localInstant, false, instant);
            }
        }

        public long roundCeiling(long instant) {
            if (iTimeField) {
                int offset = getOffsetToAdd(instant);
                instant = iField.roundCeiling(instant + offset);
                return instant - offset;
            } else {
                long localInstant = iZone.convertUTCToLocal(instant);
                localInstant = iField.roundCeiling(localInstant);
                return iZone.convertLocalToUTC(localInstant, false, instant);
            }
        }

        public long remainder(long instant) {
            long localInstant = iZone.convertUTCToLocal(instant);
            return iField.remainder(localInstant);
        }

        public int getMinimumValue() {
            return iField.getMinimumValue();
        }

        public int getMinimumValue(long instant) {
            long localInstant = iZone.convertUTCToLocal(instant);
            return iField.getMinimumValue(localInstant);
        }

        public int getMinimumValue(ReadablePartial instant) {
            return iField.getMinimumValue(instant);
        }

        public int getMinimumValue(ReadablePartial instant, int[] values) {
            return iField.getMinimumValue(instant, values);
        }

        public int getMaximumValue() {
            return iField.getMaximumValue();
        }

        public int getMaximumValue(long instant) {
            long localInstant = iZone.convertUTCToLocal(instant);
            return iField.getMaximumValue(localInstant);
        }

        public int getMaximumValue(ReadablePartial instant) {
            return iField.getMaximumValue(instant);
        }

        public int getMaximumValue(ReadablePartial instant, int[] values) {
            return iField.getMaximumValue(instant, values);
        }

        public int getMaximumTextLength(Locale locale) {
            return iField.getMaximumTextLength(locale);
        }

        public int getMaximumShortTextLength(Locale locale) {
            return iField.getMaximumShortTextLength(locale);
        }

        private int getOffsetToAdd(long instant) {
            int offset = this.iZone.getOffset(instant);
            long sum = instant + offset;
            // If there is a sign change, but the two values have the same sign...
            if ((instant ^ sum) < 0 && (instant ^ offset) >= 0) {
                throw new ArithmeticException("Adding time zone offset caused overflow");
            }
            return offset;
        }
    }

}
