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
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableDateTime;
import org.joda.time.field.DecoratedDateTimeField;
import org.joda.time.field.DecoratedDurationField;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Wraps another Chronology to impose limits on the range of instants that
 * the fields within a Chronology may support. The limits are applied to both
 * DateTimeFields and DurationFields.
 * <p>
 * Methods in DateTimeField and DurationField throw an IllegalArgumentException
 * whenever given an input instant that is outside the limits or when an
 * attempt is made to move an instant outside the limits.
 * <p>
 * LimitChronology is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public final class LimitChronology extends AssembledChronology {

    /** Serialization lock */
    private static final long serialVersionUID = 7670866536893052522L;

    /**
     * Wraps another chronology, with datetime limits. When withUTC or
     * withZone is called, the returned LimitChronology instance has
     * the same limits, except they are time zone adjusted.
     *
     * @param base  base chronology to wrap
     * @param lowerLimit  inclusive lower limit, or null if none
     * @param upperLimit  exclusive upper limit, or null if none
     * @throws IllegalArgumentException if chronology is null or limits are invalid
     */
    public static LimitChronology getInstance(Chronology base,
                                              ReadableDateTime lowerLimit,
                                              ReadableDateTime upperLimit) {
        if (base == null) {
            throw new IllegalArgumentException("Must supply a chronology");
        }

        lowerLimit = lowerLimit == null ? null : lowerLimit.toDateTime();
        upperLimit = upperLimit == null ? null : upperLimit.toDateTime();

        if (lowerLimit != null && upperLimit != null && !lowerLimit.isBefore(upperLimit)) {
            throw new IllegalArgumentException
            ("The lower limit must be come before than the upper limit");
        }

        return new LimitChronology(base, (DateTime)lowerLimit, (DateTime)upperLimit);
    }

    final DateTime iLowerLimit;
    final DateTime iUpperLimit;

    private transient LimitChronology iWithUTC;

    /**
     * Wraps another chronology, with datetime limits. When withUTC or
     * withZone is called, the returned LimitChronology instance has
     * the same limits, except they are time zone adjusted.
     *
     * @param lowerLimit  inclusive lower limit, or null if none
     * @param upperLimit  exclusive upper limit, or null if none
     */
    private LimitChronology(Chronology base,
                            DateTime lowerLimit, DateTime upperLimit) {
        super(base, null);
        // These can be set after assembly.
        iLowerLimit = lowerLimit;
        iUpperLimit = upperLimit;
    }

    /**
     * Returns the inclusive lower limit instant.
     * 
     * @return lower limit
     */
    public DateTime getLowerLimit() {
        return iLowerLimit;
    }

    /**
     * Returns the inclusive upper limit instant.
     * 
     * @return upper limit
     */
    public DateTime getUpperLimit() {
        return iUpperLimit;
    }

    /**
     * If this LimitChronology is already UTC, then this is
     * returned. Otherwise, a new instance is returned, with the limits
     * adjusted to the new time zone.
     */
    public Chronology withUTC() {
        return withZone(DateTimeZone.UTC);
    }

    /**
     * If this LimitChronology has the same time zone as the one given, then
     * this is returned. Otherwise, a new instance is returned, with the limits
     * adjusted to the new time zone.
     */
    public Chronology withZone(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        if (zone == getZone()) {
            return this;
        }

        if (zone == DateTimeZone.UTC && iWithUTC != null) {
            return iWithUTC;
        }

        DateTime lowerLimit = iLowerLimit;
        if (lowerLimit != null) {
            MutableDateTime mdt = lowerLimit.toMutableDateTime();
            mdt.setZoneRetainFields(zone);
            lowerLimit = mdt.toDateTime();
        }

        DateTime upperLimit = iUpperLimit;
        if (upperLimit != null) {
            MutableDateTime mdt = upperLimit.toMutableDateTime();
            mdt.setZoneRetainFields(zone);
            upperLimit = mdt.toDateTime();
        }
        
        LimitChronology chrono = getInstance
            (getBase().withZone(zone), lowerLimit, upperLimit);

        if (zone == DateTimeZone.UTC) {
            iWithUTC = chrono;
        }

        return chrono;
    }

    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int millisOfDay)
        throws IllegalArgumentException
    {
        long instant = getBase().getDateTimeMillis(year, monthOfYear, dayOfMonth, millisOfDay);
        checkLimits(instant, "resulting");
        return instant;
    }

    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        long instant = getBase().getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        checkLimits(instant, "resulting");
        return instant;
    }

    public long getDateTimeMillis(long instant,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        checkLimits(instant, null);
        instant = getBase().getDateTimeMillis
            (instant, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        checkLimits(instant, "resulting");
        return instant;
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
        LimitDurationField limitField = new LimitDurationField(field);
        converted.put(field, limitField);
        return limitField;
    }

    private DateTimeField convertField(DateTimeField field, HashMap<Object, Object> converted) {
        if (field == null || !field.isSupported()) {
            return field;
        }
        if (converted.containsKey(field)) {
            return (DateTimeField)converted.get(field);
        }
        LimitDateTimeField limitField =
            new LimitDateTimeField(field,
                                   convertField(field.getDurationField(), converted),
                                   convertField(field.getRangeDurationField(), converted),
                                   convertField(field.getLeapDurationField(), converted));
        converted.put(field, limitField);
        return limitField;
    }

    void checkLimits(long instant, String desc) {
        DateTime limit;
        if ((limit = iLowerLimit) != null && instant < limit.getMillis()) {
            throw new LimitException(desc, true);
        }
        if ((limit = iUpperLimit) != null && instant >= limit.getMillis()) {
            throw new LimitException(desc, false);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * A limit chronology is only equal to a limit chronology with the
     * same base chronology and limits.
     * 
     * @param obj  the object to compare to
     * @return true if equal
     * @since 1.4
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof LimitChronology == false) {
            return false;
        }
        LimitChronology chrono = (LimitChronology) obj;
        return
            getBase().equals(chrono.getBase()) &&
            FieldUtils.equals(getLowerLimit(), chrono.getLowerLimit()) &&
            FieldUtils.equals(getUpperLimit(), chrono.getUpperLimit());
    }

    /**
     * A suitable hashcode for the chronology.
     * 
     * @return the hashcode
     * @since 1.4
     */
    public int hashCode() {
        int hash = 317351877;
        hash += (getLowerLimit() != null ? getLowerLimit().hashCode() : 0);
        hash += (getUpperLimit() != null ? getUpperLimit().hashCode() : 0);
        hash += getBase().hashCode() * 7;
        return hash;
    }

    /**
     * A debugging string for the chronology.
     * 
     * @return the debugging string
     */
    public String toString() {
        return "LimitChronology[" + getBase().toString() + ", " +
            (getLowerLimit() == null ? "NoLimit" : getLowerLimit().toString()) + ", " +
            (getUpperLimit() == null ? "NoLimit" : getUpperLimit().toString()) + ']';
    }

    //-----------------------------------------------------------------------
    /**
     * Extends IllegalArgumentException such that the exception message is not
     * generated unless it is actually requested.
     */
    private class LimitException extends IllegalArgumentException {
        private static final long serialVersionUID = -5924689995607498581L;

        private final boolean iIsLow;

        LimitException(String desc, boolean isLow) {
            super(desc);
            iIsLow = isLow;
        }

        public String getMessage() {
            StringBuffer buf = new StringBuffer(85);
            buf.append("The");
            String desc = super.getMessage();
            if (desc != null) {
                buf.append(' ');
                buf.append(desc);
            }
            buf.append(" instant is ");

            DateTimeFormatter p = ISODateTimeFormat.dateTime();
            p = p.withChronology(getBase());
            if (iIsLow) {
                buf.append("below the supported minimum of ");
                p.printTo(buf, getLowerLimit().getMillis());
            } else {
                buf.append("above the supported maximum of ");
                p.printTo(buf, getUpperLimit().getMillis());
            }
            
            buf.append(" (");
            buf.append(getBase());
            buf.append(')');

            return buf.toString();
        }

        public String toString() {
            return "IllegalArgumentException: " + getMessage();
        }
    }

    private class LimitDurationField extends DecoratedDurationField {
        private static final long serialVersionUID = 8049297699408782284L;

        LimitDurationField(DurationField field) {
            super(field, field.getType());
        }

        public int getValue(long duration, long instant) {
            checkLimits(instant, null);
            return getWrappedField().getValue(duration, instant);
        }

        public long getValueAsLong(long duration, long instant) {
            checkLimits(instant, null);
            return getWrappedField().getValueAsLong(duration, instant);
        }

        public long getMillis(int value, long instant) {
            checkLimits(instant, null);
            return getWrappedField().getMillis(value, instant);
        }

        public long getMillis(long value, long instant) {
            checkLimits(instant, null);
            return getWrappedField().getMillis(value, instant);
        }

        public long add(long instant, int amount) {
            checkLimits(instant, null);
            long result = getWrappedField().add(instant, amount);
            checkLimits(result, "resulting");
            return result;
        }

        public long add(long instant, long amount) {
            checkLimits(instant, null);
            long result = getWrappedField().add(instant, amount);
            checkLimits(result, "resulting");
            return result;
        }

        public int getDifference(long minuendInstant, long subtrahendInstant) {
            checkLimits(minuendInstant, "minuend");
            checkLimits(subtrahendInstant, "subtrahend");
            return getWrappedField().getDifference(minuendInstant, subtrahendInstant);
        }

        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            checkLimits(minuendInstant, "minuend");
            checkLimits(subtrahendInstant, "subtrahend");
            return getWrappedField().getDifferenceAsLong(minuendInstant, subtrahendInstant);
        }

    }

    private class LimitDateTimeField extends DecoratedDateTimeField {
        @SuppressWarnings("unused")
        private static final long serialVersionUID = -2435306746995699312L;

        private final DurationField iDurationField;
        private final DurationField iRangeDurationField;
        private final DurationField iLeapDurationField;

        LimitDateTimeField(DateTimeField field,
                           DurationField durationField,
                           DurationField rangeDurationField,
                           DurationField leapDurationField) {
            super(field, field.getType());
            iDurationField = durationField;
            iRangeDurationField = rangeDurationField;
            iLeapDurationField = leapDurationField;
        }

        public int get(long instant) {
            checkLimits(instant, null);
            return getWrappedField().get(instant);
        }
        
        public String getAsText(long instant, Locale locale) {
            checkLimits(instant, null);
            return getWrappedField().getAsText(instant, locale);
        }
        
        public String getAsShortText(long instant, Locale locale) {
            checkLimits(instant, null);
            return getWrappedField().getAsShortText(instant, locale);
        }
        
        public long add(long instant, int amount) {
            checkLimits(instant, null);
            long result = getWrappedField().add(instant, amount);
            checkLimits(result, "resulting");
            return result;
        }

        public long add(long instant, long amount) {
            checkLimits(instant, null);
            long result = getWrappedField().add(instant, amount);
            checkLimits(result, "resulting");
            return result;
        }

        public long addWrapField(long instant, int amount) {
            checkLimits(instant, null);
            long result = getWrappedField().addWrapField(instant, amount);
            checkLimits(result, "resulting");
            return result;
        }
        
        public int getDifference(long minuendInstant, long subtrahendInstant) {
            checkLimits(minuendInstant, "minuend");
            checkLimits(subtrahendInstant, "subtrahend");
            return getWrappedField().getDifference(minuendInstant, subtrahendInstant);
        }
        
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            checkLimits(minuendInstant, "minuend");
            checkLimits(subtrahendInstant, "subtrahend");
            return getWrappedField().getDifferenceAsLong(minuendInstant, subtrahendInstant);
        }
        
        public long set(long instant, int value) {
            checkLimits(instant, null);
            long result = getWrappedField().set(instant, value);
            checkLimits(result, "resulting");
            return result;
        }
        
        public long set(long instant, String text, Locale locale) {
            checkLimits(instant, null);
            long result = getWrappedField().set(instant, text, locale);
            checkLimits(result, "resulting");
            return result;
        }
        
        public final DurationField getDurationField() {
            return iDurationField;
        }

        public final DurationField getRangeDurationField() {
            return iRangeDurationField;
        }

        public boolean isLeap(long instant) {
            checkLimits(instant, null);
            return getWrappedField().isLeap(instant);
        }
        
        public int getLeapAmount(long instant) {
            checkLimits(instant, null);
            return getWrappedField().getLeapAmount(instant);
        }
        
        public final DurationField getLeapDurationField() {
            return iLeapDurationField;
        }
        
        public long roundFloor(long instant) {
            checkLimits(instant, null);
            long result = getWrappedField().roundFloor(instant);
            checkLimits(result, "resulting");
            return result;
        }
        
        public long roundCeiling(long instant) {
            checkLimits(instant, null);
            long result = getWrappedField().roundCeiling(instant);
            checkLimits(result, "resulting");
            return result;
        }
        
        public long roundHalfFloor(long instant) {
            checkLimits(instant, null);
            long result = getWrappedField().roundHalfFloor(instant);
            checkLimits(result, "resulting");
            return result;
        }
        
        public long roundHalfCeiling(long instant) {
            checkLimits(instant, null);
            long result = getWrappedField().roundHalfCeiling(instant);
            checkLimits(result, "resulting");
            return result;
        }
        
        public long roundHalfEven(long instant) {
            checkLimits(instant, null);
            long result = getWrappedField().roundHalfEven(instant);
            checkLimits(result, "resulting");
            return result;
        }
        
        public long remainder(long instant) {
            checkLimits(instant, null);
            long result = getWrappedField().remainder(instant);
            checkLimits(result, "resulting");
            return result;
        }

        public int getMinimumValue(long instant) {
            checkLimits(instant, null);
            return getWrappedField().getMinimumValue(instant);
        }

        public int getMaximumValue(long instant) {
            checkLimits(instant, null);
            return getWrappedField().getMaximumValue(instant);
        }

        public int getMaximumTextLength(Locale locale) {
            return getWrappedField().getMaximumTextLength(locale);
        }

        public int getMaximumShortTextLength(Locale locale) {
            return getWrappedField().getMaximumShortTextLength(locale);
        }

    }

}
