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
import java.io.Serializable;

import java.util.HashMap;
import java.util.Locale;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableDateTime;
import org.joda.time.format.DateTimePrinter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Imposes limits on the range of instants that the fields within a Chronology
 * may support. The limits are applied to both DateTimeFields and
 * DurationFields.
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
public class LimitChronology extends Chronology {

    static final long serialVersionUID = 7670866536893052522L;

    private final Chronology iChronology;

    final DateTime iLowerLimit;
    final DateTime iUpperLimit;

    private transient LimitChronology iWithUTC;

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
     * Wraps another chronology, with datetime limits. When withUTC or
     * withDateTimeZone is called, the returned LimitChronology instance has
     * the same limits, except they are time zone adjusted.
     *
     * @param lowerLimit  inclusive lower limit, or null if none
     * @param upperLimit  exclusive upper limit, or null if none
     * @throws IllegalArgumentException if chronology is null or limits are invalid
     */
    public LimitChronology(Chronology chrono,
                           ReadableDateTime lowerLimit, ReadableDateTime upperLimit) {
        if (chrono == null) {
            throw new IllegalArgumentException("Must supply a chronology");
        }

        iChronology = chrono;

        iLowerLimit = lowerLimit == null ? null : lowerLimit.toDateTime();
        iUpperLimit = upperLimit == null ? null : upperLimit.toDateTime();

        if (iLowerLimit != null && iUpperLimit != null) {
            if (!iLowerLimit.isBefore(iUpperLimit)) {
                throw new IllegalArgumentException
                    ("The lower limit must be come before than the upper limit");
            }
        }

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
        LimitDurationField limitField = new LimitDurationField(field);
        converted.put(field, limitField);
        return limitField;
    }

    private DateTimeField convertField(DateTimeField field, HashMap converted) {
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
    public DateTime getUpperBound() {
        return iUpperLimit;
    }

    /**
     * Gets the wrapped chronology.
     * 
     * @return the wrapped Chronology
     */
    protected Chronology getWrappedChronology() {
        return iChronology;
    }

    /**
     * If this LimitChronology is already UTC, then this is
     * returned. Otherwise, a new instance is returned, with the limits
     * adjusted to the new time zone.
     */
    public Chronology withUTC() {
        return withDateTimeZone(DateTimeZone.UTC);
    }

    /**
     * If this LimitChronology has the same time zone as the one given, then
     * this is returned. Otherwise, a new instance is returned, with the limits
     * adjusted to the new time zone.
     */
    public Chronology withDateTimeZone(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        if (zone == getDateTimeZone()) {
            return this;
        }

        if (zone == DateTimeZone.UTC && iWithUTC != null) {
            return iWithUTC;
        }

        DateTime lowerLimit = iLowerLimit;
        if (lowerLimit != null) {
            MutableDateTime mdt = lowerLimit.toMutableDateTime();
            mdt.moveDateTimeZone(zone);
            lowerLimit = mdt.toDateTime();
        }

        DateTime upperLimit = iUpperLimit;
        if (upperLimit != null) {
            MutableDateTime mdt = upperLimit.toMutableDateTime();
            mdt.moveDateTimeZone(zone);
            upperLimit = mdt.toDateTime();
        }
        
        LimitChronology chrono = new LimitChronology
            (iChronology.withDateTimeZone(zone), lowerLimit, upperLimit);

        if (zone == DateTimeZone.UTC) {
            iWithUTC = chrono;
        }

        return chrono;
    }

    public DateTimeZone getDateTimeZone() {
        return iChronology.getDateTimeZone();
    }

    public long getDateOnlyMillis(long instant) {
        checkLimits(instant, null);
        instant = iChronology.getDateOnlyMillis(instant);
        checkLimits(instant, "resulting");
        return instant;
    }

    public long getDateOnlyMillis(int year, int monthOfYear, int dayOfMonth)
        throws IllegalArgumentException
    {
        long instant = iChronology.getDateOnlyMillis(year, monthOfYear, dayOfMonth);
        checkLimits(instant, "resulting");
        return instant;
    }

    public long getTimeOnlyMillis(long instant) {
        checkLimits(instant, null);
        instant = iChronology.getTimeOnlyMillis(instant);
        checkLimits(instant, "resulting");
        return instant;
    }

    public long getTimeOnlyMillis(int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        long instant = iChronology.getTimeOnlyMillis
            (hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        checkLimits(instant, "resulting");
        return instant;
    }

    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int millisOfDay)
        throws IllegalArgumentException
    {
        long instant = iChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth, millisOfDay);
        checkLimits(instant, "resulting");
        return instant;
    }

    public long getDateTimeMillis(long instant,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        checkLimits(instant, null);
        instant = iChronology.getDateTimeMillis
            (instant, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        checkLimits(instant, "resulting");
        return instant;
    }

    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        long instant = iChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        checkLimits(instant, "resulting");
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

    void checkLimits(long instant, String desc) {
        DateTime limit;
        if ((limit = iLowerLimit) != null && instant < limit.getMillis()) {
            throw new LimitException(desc, true);
        }
        if ((limit = iUpperLimit) != null && instant >= limit.getMillis()) {
            throw new LimitException(desc, false);
        }
    }

    /**
     * Extends IllegalArgumentException such that the exception message is not
     * generated unless it is actually requested.
     */
    private class LimitException extends IllegalArgumentException {
        static final long serialVersionUID = -5924689995607498581L;

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

            DateTimePrinter p = ISODateTimeFormat.getInstance(getWrappedChronology()).dateTime();

            if (iIsLow) {
                buf.append("below the supported minimum of ");
                p.printTo(buf, iLowerLimit);
            } else {
                buf.append("above the supported maximum of ");
                p.printTo(buf, iUpperLimit);
            }
            
            buf.append(" (");
            buf.append(getWrappedChronology());
            buf.append(')');

            return buf.toString();
        }

        public String toString() {
            return "IllegalArgumentException: " + getMessage();
        }
    }

    private class LimitDurationField extends DecoratedDurationField {
        static final long serialVersionUID = 8049297699408782284L;

        LimitDurationField(DurationField field) {
            super(field, field.getName());
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
        static final long serialVersionUID = -2435306746995699312L;

        private final DurationField iDurationField;
        private final DurationField iRangeDurationField;
        private final DurationField iLeapDurationField;

        LimitDateTimeField(DateTimeField field,
                           DurationField durationField,
                           DurationField rangeDurationField,
                           DurationField leapDurationField) {
            super(field, field.getName());
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

        public long addWrapped(long instant, int amount) {
            checkLimits(instant, null);
            long result = getWrappedField().addWrapped(instant, amount);
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
