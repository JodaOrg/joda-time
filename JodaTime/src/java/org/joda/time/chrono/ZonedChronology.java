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
package org.joda.time.chrono;

import java.io.IOException;
import java.io.ObjectInputStream;

import java.util.Locale;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;

/**
 * Wraps another Chronology for supporting time zones.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public class ZonedChronology extends Chronology {
    private final Chronology iChronology;
    private final DateTimeZone iZone;

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
        DateTimeZone z = iZone;

        iYearField = new ZonedDateField(c.year(), z);
        iYearOfEraField = new ZonedDateField(c.yearOfEra(), z);
        iYearOfCenturyField = new ZonedDateField(c.yearOfCentury(), z);
        iCenturyOfEraField = new ZonedDateField(c.centuryOfEra(), z);
        iEraField = new ZonedDateField(c.era(), z);
        iDayOfMonthField = new ZonedDateField(c.dayOfMonth(), z);
        iDayOfWeekField = new ZonedDateField(c.dayOfWeek(), z);
        iDayOfYearField = new ZonedDateField(c.dayOfYear(), z);
        iMonthOfYearField = new ZonedDateField(c.monthOfYear(), z);
        iWeekOfWeekyearField = new ZonedDateField(c.weekOfWeekyear(), z);
        iWeekyearField = new ZonedDateField(c.weekyear(), z);
        
        iMillisOfSecondField = new ZonedTimeField(c.millisOfSecond(), z);
        iMillisOfDayField = new ZonedTimeField(c.millisOfDay(), z);
        iSecondOfMinuteField = new ZonedTimeField(c.secondOfMinute(), z);
        iSecondOfDayField = new ZonedTimeField(c.secondOfDay(), z);
        iMinuteOfHourField = new ZonedTimeField(c.minuteOfHour(), z);
        iMinuteOfDayField = new ZonedTimeField(c.minuteOfDay(), z);
        iHourOfDayField = new ZonedTimeField(c.hourOfDay(), z);
        iHourOfHalfdayField = new ZonedTimeField(c.hourOfHalfday(), z);
        iClockhourOfDayField = new ZonedTimeField(c.clockhourOfDay(), z);
        iClockhourOfHalfdayField = new ZonedTimeField(c.clockhourOfHalfday(), z);

        // Treat halfday as a date field for adds.
        iHalfdayOfDayField = new ZonedDateField(c.halfdayOfDay(), z);
    }

    public DateTimeZone getDateTimeZone() {
        return iZone;
    }

    public Chronology withUTC() {
        return iChronology;
    }

    public Chronology withDateTimeZone(DateTimeZone zone) {
        if (zone == null) {
            throw new IllegalArgumentException("The DateTimeZone must not be null");
        }
        if (zone == iZone) {
            return this;
        }
        if (zone == DateTimeZone.UTC) {
            return iChronology;
        }
        return new ZonedChronology(iChronology, zone);
    }

    // Milliseconds
    //------------------------------------------------------------

    public DateTimeField millisOfSecond() {
        return iMillisOfSecondField;
    }

    public DateTimeField millisOfDay() {
        return iMillisOfDayField;
    }

    // Seconds
    //------------------------------------------------------------

    public DateTimeField secondOfMinute() {
        return iSecondOfMinuteField;
    }

    public DateTimeField secondOfDay() {
        return iSecondOfDayField;
    }

    // Minutes
    //------------------------------------------------------------

    public DateTimeField minuteOfHour() {
        return iMinuteOfHourField;
    }

    public DateTimeField minuteOfDay() {
        return iMinuteOfDayField;
    }

    // Hours
    //------------------------------------------------------------

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
    
    public DateTimeField weekOfWeekyear() {
        return iWeekOfWeekyearField;
    }

    public DateTimeField weekyear() {
        return iWeekyearField;
    }

    // Month
    //------------------------------------------------------------
    
    public DateTimeField monthOfYear() {
        return iMonthOfYearField;
    }

    // Year
    //------------------------------------------------------------
    
    public DateTimeField year() {
        return iYearField;
    }

    public DateTimeField yearOfEra() {
        return iYearOfEraField;
    }

    public DateTimeField yearOfCentury() {
        return iYearOfCenturyField;
    }

    public DateTimeField centuryOfEra() {
        return iCenturyOfEraField;
    }

    public DateTimeField era() {
        return iEraField;
    }

    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        setFields();
    }

    /**
     * A DateTimeField that decorates another to add timezone behaviour.
     * <p>
     * This class converts passed in millis to local wall millis, and 
     * vice versa on output.
     */
    static class ZonedDateField extends DateTimeField {
        final DateTimeField iField;
        final DateTimeZone iZone;
        
        ZonedDateField(DateTimeField field, DateTimeZone zone) {
            super(field.getName());
            iField = field;
            this.iZone = zone;
        }
        
        public int get(long millis) {
            return iField.get(millis + this.iZone.getOffset(millis));
        }
        
        public String getAsText(long millis, Locale locale) {
            return iField.getAsText(millis + this.iZone.getOffset(millis), locale);
        }
        
        public String getAsShortText(long millis, Locale locale) {
            return iField.getAsShortText(millis + this.iZone.getOffset(millis), locale);
        }
        
        public long add(long millis, int value) {
            millis = iField.add(millis + this.iZone.getOffset(millis), value);
            return millis - this.iZone.getOffsetFromLocal(millis);
        }
        
        public long add(long millis, long value) {
            millis = iField.add(millis + this.iZone.getOffset(millis), value);
            return millis - this.iZone.getOffsetFromLocal(millis);
        }

        public long addWrapped(long millis, int value) {
            millis = iField.addWrapped(millis + this.iZone.getOffset(millis), value);
            return millis - this.iZone.getOffsetFromLocal(millis);
        }

        public long getDifference(long minuendMillis, long subtrahendMillis) {
            return iField.getDifference(minuendMillis + this.iZone.getOffset(minuendMillis),
                                        subtrahendMillis + this.iZone.getOffset(subtrahendMillis));
        }

        public long set(long millis, int value) {
            long offset = this.iZone.getOffset(millis);
            
            millis = iField.set(millis + offset, value);
            long offsetFromLocal = this.iZone.getOffsetFromLocal(millis);
            millis -= offsetFromLocal;
            
            if (offset != offsetFromLocal) {
                if (get(millis) != value) {
                    throw new IllegalArgumentException
                        ("Illegal value for " + iField.getName() + ": " + value);
                }
            }
            
            return millis;
        }
        
        public long set(long millis, String text, Locale locale) {
            millis = iField.set(millis + this.iZone.getOffset(millis), text, locale);
            // Cannot verify that new value stuck because set may be lenient.
            return millis - this.iZone.getOffsetFromLocal(millis);
        }
        
        public boolean isLeap(long millis) {
            return iField.isLeap(millis + this.iZone.getOffset(millis));
        }

        public int getLeapAmount(long millis) {
            return iField.getLeapAmount(millis + this.iZone.getOffset(millis));
        }
        
        public long getUnitMillis() {
            return iField.getUnitMillis();
        }

        public long getRangeMillis() {
            return iField.getRangeMillis();
        }

        public int getMinimumValue() {
            return iField.getMinimumValue();
        }
        
        public int getMinimumValue(long millis) {
            return iField.getMinimumValue(millis + this.iZone.getOffset(millis));
        }
        
        public int getMaximumValue() {
            return iField.getMaximumValue();
        }
        
        public int getMaximumValue(long millis) {
            return iField.getMaximumValue(millis + this.iZone.getOffset(millis));
        }
        
        public long roundFloor(long millis) {
            millis = iField.roundFloor(millis + this.iZone.getOffset(millis));
            return millis - this.iZone.getOffsetFromLocal(millis);
        }
        
        public long roundCeiling(long millis) {
            millis = iField.roundCeiling(millis + this.iZone.getOffset(millis));
            return millis - this.iZone.getOffsetFromLocal(millis);
        }

        public long remainder(long millis) {
            millis = iField.remainder(millis + this.iZone.getOffset(millis));
            return millis - this.iZone.getOffsetFromLocal(millis);
        }

        public int getMaximumTextLength(Locale locale) {
            return iField.getMaximumTextLength(locale);
        }
        
        public int getMaximumShortTextLength(Locale locale) {
            return iField.getMaximumShortTextLength(locale);
        }
    }

    static class ZonedTimeField extends ZonedDateField {
        ZonedTimeField(DateTimeField field, DateTimeZone zone) {
            super(field, zone);
        }

        // Because time fields are smaller than time zone offsets, override the
        // arithmetic methods to follow more expected behavior when crossing
        // time zone offset transitions. The original add method can nullify or
        // reverse an add when crossing a transition.

        public long add(long millis, int value) {
            int offset = this.iZone.getOffset(millis);
            return iField.add(millis + offset, value) - offset;
        }
        
        public long add(long millis, long value) {
            int offset = this.iZone.getOffset(millis);
            return iField.add(millis + offset, value) - offset;
        }

        public long addWrapped(long millis, int value) {
            int offset = this.iZone.getOffset(millis);
            return iField.addWrapped(millis + offset, value) - offset;
        }

        public long getDifference(long minuendMillis, long subtrahendMillis) {
            int offset = this.iZone.getOffset(subtrahendMillis);
            return iField.getDifference(minuendMillis + offset, subtrahendMillis + offset);
        }
    }
}
