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

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;

/**
 * Wraps another chronology, ensuring all the fields are lenient.
 * <p>
 * LenientChronology is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @see LenientDateTimeField
 * @see StrictChronology
 */
public class LenientChronology extends DelegatedChronology {

    static final long serialVersionUID = -3148237568046877177L;

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

    private transient Chronology iWithUTC;

    /**
     * Create a LenientChronology for any chronology.
     *
     * @param chrono the chronology
     * @throws IllegalArgumentException if chronology is null
     */
    public LenientChronology(Chronology chrono) {
        super(chrono);
    }

    public Chronology withUTC() {
        if (iWithUTC == null) {
            iWithUTC = new LenientChronology(getWrappedChronology().withUTC());
        }
        return iWithUTC;
    }

    public Chronology withDateTimeZone(DateTimeZone zone) {
        return new LenientChronology(getWrappedChronology().withDateTimeZone(zone));
    }

    /**
     * Overridden to ensure lenient fields are used.
     */
    public long getDateOnlyMillis(int year, int monthOfYear, int dayOfMonth)
        throws IllegalArgumentException
    {
        return getDateTimeMillis(year, monthOfYear, dayOfMonth, 0);
    }

    /**
     * Overridden to ensure lenient fields are used.
     */
    public long getTimeOnlyMillis(int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        long instant = hourOfDay().set(0, hourOfDay);
        instant = minuteOfHour().set(instant, minuteOfHour);
        instant = secondOfMinute().set(instant, secondOfMinute);
        return millisOfSecond().set(instant, millisOfSecond);
    }

    /**
     * Overridden to ensure lenient fields are used.
     */
    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int millisOfDay)
        throws IllegalArgumentException
    {
        long instant = year().set(0, year);
        instant = monthOfYear().set(instant, monthOfYear);
        instant = dayOfMonth().set(instant, dayOfMonth);
        return millisOfDay().set(instant, millisOfDay);
    }

    /**
     * Overridden to ensure lenient fields are used.
     */
    public long getDateTimeMillis(long instant,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        instant = hourOfDay().set(instant, hourOfDay);
        instant = minuteOfHour().set(instant, minuteOfHour);
        instant = secondOfMinute().set(instant, secondOfMinute);
        return millisOfSecond().set(instant, millisOfSecond);
    }

    /**
     * Overridden to ensure lenient fields are used.
     */
    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        long instant = year().set(0, year);
        instant = monthOfYear().set(instant, monthOfYear);
        instant = dayOfMonth().set(instant, dayOfMonth);
        instant = hourOfDay().set(instant, hourOfDay);
        instant = minuteOfHour().set(instant, minuteOfHour);
        instant = secondOfMinute().set(instant, secondOfMinute);
        return millisOfSecond().set(instant, millisOfSecond);
    }

    // Milliseconds
    //------------------------------------------------------------

    public DateTimeField millisOfSecond() {
        if (iMillisOfDayField == null) {
            iMillisOfSecondField = LenientDateTimeField.getInstance(super.millisOfSecond());
        }
        return iMillisOfSecondField;
    }

    public DateTimeField millisOfDay() {
        if (iMillisOfDayField == null) {
            iMillisOfDayField = LenientDateTimeField.getInstance(super.millisOfDay());
        }
        return iMillisOfDayField;
    }

    // Seconds
    //------------------------------------------------------------

    public DateTimeField secondOfMinute() {
        if (iSecondOfMinuteField == null) {
            iSecondOfMinuteField = LenientDateTimeField.getInstance(super.secondOfMinute());
        }
        return iSecondOfMinuteField;
    }

    public DateTimeField secondOfDay() {
        if (iSecondOfDayField == null) {
            iSecondOfDayField = LenientDateTimeField.getInstance(super.secondOfDay());
        }
        return iSecondOfDayField;
    }

    // Minutes
    //------------------------------------------------------------

    public DateTimeField minuteOfHour() {
        if (iMinuteOfHourField == null) {
            iMinuteOfHourField = LenientDateTimeField.getInstance(super.minuteOfHour());
        }
        return iMinuteOfHourField;
    }

    public DateTimeField minuteOfDay() {
        if (iMinuteOfDayField == null) {
            iMinuteOfDayField = LenientDateTimeField.getInstance(super.minuteOfDay());
        }
        return iMinuteOfDayField;
    }

    // Hours
    //------------------------------------------------------------

    public DateTimeField hourOfDay() {
        if (iHourOfDayField == null) {
            iHourOfDayField = LenientDateTimeField.getInstance(super.hourOfDay());
        }
        return iHourOfDayField;
    }

    public DateTimeField clockhourOfDay() {
        if (iClockhourOfDayField == null) {
            iClockhourOfDayField = LenientDateTimeField.getInstance(super.clockhourOfDay());
        }
        return iClockhourOfDayField;
    }

    public DateTimeField hourOfHalfday() {
        if (iHourOfHalfdayField == null) {
            iHourOfHalfdayField = LenientDateTimeField.getInstance(super.hourOfHalfday());
        }
        return iHourOfHalfdayField;
    }

    public DateTimeField clockhourOfHalfday() {
        if (iClockhourOfHalfdayField == null) {
            iClockhourOfHalfdayField =
                LenientDateTimeField.getInstance(super.clockhourOfHalfday());
        }
        return iClockhourOfHalfdayField;
    }

    public DateTimeField halfdayOfDay() {
        if (iHalfdayOfDayField == null) {
            iHalfdayOfDayField = LenientDateTimeField.getInstance(super.halfdayOfDay());
        }
        return iHalfdayOfDayField;
    }

    // Day
    //------------------------------------------------------------

    public DateTimeField dayOfWeek() {
        if (iDayOfWeekField == null) {
            iDayOfWeekField = LenientDateTimeField.getInstance(super.dayOfWeek());
        }
        return iDayOfWeekField;
    }

    public DateTimeField dayOfMonth() {
        if (iDayOfMonthField == null) {
            iDayOfMonthField = LenientDateTimeField.getInstance(super.dayOfMonth());
        }
        return iDayOfMonthField;
    }

    public DateTimeField dayOfYear() {
        if (iDayOfYearField == null) {
            iDayOfYearField = LenientDateTimeField.getInstance(super.dayOfYear());
        }
        return iDayOfYearField;
    }

    // Week
    //------------------------------------------------------------

    public DateTimeField weekOfWeekyear() {
        if (iWeekOfWeekyearField == null) {
            iWeekOfWeekyearField = LenientDateTimeField.getInstance(super.weekOfWeekyear());
        }
        return iWeekOfWeekyearField;
    }

    public DateTimeField weekyear() {
        if (iWeekyearField == null) {
            iWeekyearField = LenientDateTimeField.getInstance(super.weekyear());
        }
        return iWeekyearField;
    }

    // Month
    //------------------------------------------------------------

    public DateTimeField monthOfYear() {
        if (iMonthOfYearField == null) {
            iMonthOfYearField = LenientDateTimeField.getInstance(super.monthOfYear());
        }
        return iMonthOfYearField;
    }

    // Year
    //------------------------------------------------------------

    public DateTimeField year() {
        if (iYearField == null) {
            iYearField = LenientDateTimeField.getInstance(super.year());
        }
        return iYearField;
    }

    public DateTimeField yearOfEra() {
        if (iYearOfEraField == null) {
            iYearOfEraField = LenientDateTimeField.getInstance(super.yearOfEra());
        }
        return iYearOfEraField;
    }

    public DateTimeField yearOfCentury() {
        if (iYearOfCenturyField == null) {
            iYearOfCenturyField = LenientDateTimeField.getInstance(super.yearOfCentury());
        }
        return iYearOfCenturyField;
    }

    public DateTimeField centuryOfEra() {
        if (iCenturyOfEraField == null) {
            iCenturyOfEraField = LenientDateTimeField.getInstance(super.centuryOfEra());
        }
        return iCenturyOfEraField;
    }

    public DateTimeField era() {
        if (iEraField == null) {
            iEraField = LenientDateTimeField.getInstance(super.era());
        }
        return iEraField;
    }

    public String toString() {
        return "LenientChronology[" + getWrappedChronology().toString() + ']';
    }

}
