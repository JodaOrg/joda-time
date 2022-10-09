/*
 *  Copyright 2001-2005 Stephen Colebourne
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

import java.io.IOException;
import java.io.ObjectInputStream;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;

/**
 * Abstract Chronology that enables chronologies to be assembled from
 * a container of fields.
 * <p>
 * AssembledChronology is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public abstract class AssembledChronology extends BaseChronology {

    private static final long serialVersionUID = -6728465968995518215L;

    private final Chronology iBase;
    private final Object iParam;

    private transient DurationField iMillis;
    private transient DurationField iSeconds;
    private transient DurationField iMinutes;
    private transient DurationField iHours;
    private transient DurationField iHalfdays;

    private transient DurationField iDays;
    private transient DurationField iWeeks;
    private transient DurationField iWeekyears;
    private transient DurationField iMonths;
    private transient DurationField iYears;
    private transient DurationField iCenturies;
    private transient DurationField iEras;

    private transient DateTimeField iMillisOfSecond;
    private transient DateTimeField iMillisOfDay;
    private transient DateTimeField iSecondOfMinute;
    private transient DateTimeField iSecondOfDay;
    private transient DateTimeField iMinuteOfHour;
    private transient DateTimeField iMinuteOfDay;
    private transient DateTimeField iHourOfDay;
    private transient DateTimeField iClockhourOfDay;
    private transient DateTimeField iHourOfHalfday;
    private transient DateTimeField iClockhourOfHalfday;
    private transient DateTimeField iHalfdayOfDay;

    private transient DateTimeField iDayOfWeek;
    private transient DateTimeField iDayOfMonth;
    private transient DateTimeField iDayOfYear;
    private transient DateTimeField iWeekOfWeekyear;
    private transient DateTimeField iWeekyear;
    private transient DateTimeField iWeekyearOfCentury;
    private transient DateTimeField iMonthOfYear;
    private transient DateTimeField iYear;
    private transient DateTimeField iYearOfEra;
    private transient DateTimeField iYearOfCentury;
    private transient DateTimeField iCenturyOfEra;
    private transient DateTimeField iEra;

    // Bit set determines which base fields are used
    // bit 1 set: hourOfDay, minuteOfHour, secondOfMinute, and millisOfSecond fields
    // bit 2 set: millisOfDayField
    // bit 3 set: year, monthOfYear, and dayOfMonth fields
    private transient int iBaseFlags;

    /**
     * Constructor calls the assemble method, enabling subclasses to define its
     * supported fields. If a base chronology is supplied, the field set
     * initially contains references to each base chronology field.
     * <p>
     * Other methods in this class will delegate to the base chronology, if it
     * can be determined that the base chronology will produce the same results
     * as AbstractChronology.
     *
     * @param base optional base chronology to copy initial fields from
     * @param param optional param object available for assemble method
     */
    protected AssembledChronology(Chronology base, Object param) {
        iBase = base;
        iParam = param;
        setFields();
    }

    @Override
    public DateTimeZone getZone() {
        Chronology base;
        if ((base = iBase) != null) {
            return base.getZone();
        }
        return null;
    }

    @Override
    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int millisOfDay)
        throws IllegalArgumentException
    {
        Chronology base;
        if ((base = iBase) != null && (iBaseFlags & 6) == 6) {
            // Only call specialized implementation if applicable fields are the same.
            return base.getDateTimeMillis(year, monthOfYear, dayOfMonth, millisOfDay);
        }
        return super.getDateTimeMillis(year, monthOfYear, dayOfMonth, millisOfDay);
    }

    @Override
    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        Chronology base;
        if ((base = iBase) != null && (iBaseFlags & 5) == 5) {
            // Only call specialized implementation if applicable fields are the same.
            return base.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                          hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        }
        return super.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                       hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    @Override
    public long getDateTimeMillis(long instant,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        Chronology base;
        if ((base = iBase) != null && (iBaseFlags & 1) == 1) {
            // Only call specialized implementation if applicable fields are the same.
            return base.getDateTimeMillis
                (instant, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        }
        return super.getDateTimeMillis
            (instant, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    @Override
    public final DurationField millis() {
        return iMillis;
    }

    @Override
    public final DateTimeField millisOfSecond() {
        return iMillisOfSecond;
    }

    @Override
    public final DateTimeField millisOfDay() {
        return iMillisOfDay;
    }

    @Override
    public final DurationField seconds() {
        return iSeconds;
    }

    @Override
    public final DateTimeField secondOfMinute() {
        return iSecondOfMinute;
    }

    @Override
    public final DateTimeField secondOfDay() {
        return iSecondOfDay;
    }

    @Override
    public final DurationField minutes() {
        return iMinutes;
    }

    @Override
    public final DateTimeField minuteOfHour() {
        return iMinuteOfHour;
    }

    @Override
    public final DateTimeField minuteOfDay() {
        return iMinuteOfDay;
    }

    @Override
    public final DurationField hours() {
        return iHours;
    }

    @Override
    public final DateTimeField hourOfDay() {
        return iHourOfDay;
    }

    @Override
    public final DateTimeField clockhourOfDay() {
        return iClockhourOfDay;
    }

    @Override
    public final DurationField halfdays() {
        return iHalfdays;
    }

    @Override
    public final DateTimeField hourOfHalfday() {
        return iHourOfHalfday;
    }

    @Override
    public final DateTimeField clockhourOfHalfday() {
        return iClockhourOfHalfday;
    }

    @Override
    public final DateTimeField halfdayOfDay() {
        return iHalfdayOfDay;
    }

    @Override
    public final DurationField days() {
        return iDays;
    }

    @Override
    public final DateTimeField dayOfWeek() {
        return iDayOfWeek;
    }

    @Override
    public final DateTimeField dayOfMonth() {
        return iDayOfMonth;
    }

    @Override
    public final DateTimeField dayOfYear() {
        return iDayOfYear;
    }

    @Override
    public final DurationField weeks() {
        return iWeeks;
    }

    @Override
    public final DateTimeField weekOfWeekyear() {
        return iWeekOfWeekyear;
    }

    @Override
    public final DurationField weekyears() {
        return iWeekyears;
    }

    @Override
    public final DateTimeField weekyear() {
        return iWeekyear;
    }

    @Override
    public final DateTimeField weekyearOfCentury() {
        return iWeekyearOfCentury;
    }

    @Override
    public final DurationField months() {
        return iMonths;
    }

    @Override
    public final DateTimeField monthOfYear() {
        return iMonthOfYear;
    }

    @Override
    public final DurationField years() {
        return iYears;
    }

    @Override
    public final DateTimeField year() {
        return iYear;
    }

    @Override
    public final DateTimeField yearOfEra() {
        return iYearOfEra;
    }

    @Override
    public final DateTimeField yearOfCentury() {
        return iYearOfCentury;
    }

    @Override
    public final DurationField centuries() {
        return iCenturies;
    }

    @Override
    public final DateTimeField centuryOfEra() {
        return iCenturyOfEra;
    }

    @Override
    public final DurationField eras() {
        return iEras;
    }

    @Override
    public final DateTimeField era() {
        return iEra;
    }

    /**
     * Invoked by the constructor and after deserialization to allow subclasses
     * to define all of its supported fields. All unset fields default to
     * unsupported instances.
     *
     * @param fields container of fields
     */
    protected abstract void assemble(Fields fields);

    /**
     * Returns the same base chronology as passed into the constructor.
     * 
     * @return the base chronology
     */
    protected final Chronology getBase() {
        return iBase;
    }

    /**
     * Returns the same param object as passed into the constructor.
     * 
     * @return the object parameter
     */
    protected final Object getParam() {
        return iParam;
    }

    private void setFields() {
        Fields fields = new Fields();
        if (iBase != null) {
            fields.copyFieldsFrom(iBase);
        }
        assemble(fields);

        {
            DurationField f;
            iMillis    = (f = fields.millis)    != null ? f : super.millis();
            iSeconds   = (f = fields.seconds)   != null ? f : super.seconds();
            iMinutes   = (f = fields.minutes)   != null ? f : super.minutes();
            iHours     = (f = fields.hours)     != null ? f : super.hours();
            iHalfdays  = (f = fields.halfdays)  != null ? f : super.halfdays();
            iDays      = (f = fields.days)      != null ? f : super.days();
            iWeeks     = (f = fields.weeks)     != null ? f : super.weeks();
            iWeekyears = (f = fields.weekyears) != null ? f : super.weekyears();
            iMonths    = (f = fields.months)    != null ? f : super.months();
            iYears     = (f = fields.years)     != null ? f : super.years();
            iCenturies = (f = fields.centuries) != null ? f : super.centuries();
            iEras      = (f = fields.eras)      != null ? f : super.eras();
        }

        {
            DateTimeField f;
            iMillisOfSecond     = (f = fields.millisOfSecond)     != null ? f : super.millisOfSecond();
            iMillisOfDay        = (f = fields.millisOfDay)        != null ? f : super.millisOfDay();
            iSecondOfMinute     = (f = fields.secondOfMinute)     != null ? f : super.secondOfMinute();
            iSecondOfDay        = (f = fields.secondOfDay)        != null ? f : super.secondOfDay();
            iMinuteOfHour       = (f = fields.minuteOfHour)       != null ? f : super.minuteOfHour();
            iMinuteOfDay        = (f = fields.minuteOfDay)        != null ? f : super.minuteOfDay();
            iHourOfDay          = (f = fields.hourOfDay)          != null ? f : super.hourOfDay();
            iClockhourOfDay     = (f = fields.clockhourOfDay)     != null ? f : super.clockhourOfDay();
            iHourOfHalfday      = (f = fields.hourOfHalfday)      != null ? f : super.hourOfHalfday();
            iClockhourOfHalfday = (f = fields.clockhourOfHalfday) != null ? f : super.clockhourOfHalfday();
            iHalfdayOfDay       = (f = fields.halfdayOfDay)       != null ? f : super.halfdayOfDay();
            iDayOfWeek          = (f = fields.dayOfWeek)          != null ? f : super.dayOfWeek();
            iDayOfMonth         = (f = fields.dayOfMonth)         != null ? f : super.dayOfMonth();
            iDayOfYear          = (f = fields.dayOfYear)          != null ? f : super.dayOfYear();
            iWeekOfWeekyear     = (f = fields.weekOfWeekyear)     != null ? f : super.weekOfWeekyear();
            iWeekyear           = (f = fields.weekyear)           != null ? f : super.weekyear();
            iWeekyearOfCentury  = (f = fields.weekyearOfCentury)  != null ? f : super.weekyearOfCentury();
            iMonthOfYear        = (f = fields.monthOfYear)        != null ? f : super.monthOfYear();
            iYear               = (f = fields.year)               != null ? f : super.year();
            iYearOfEra          = (f = fields.yearOfEra)          != null ? f : super.yearOfEra();
            iYearOfCentury      = (f = fields.yearOfCentury)      != null ? f : super.yearOfCentury();
            iCenturyOfEra       = (f = fields.centuryOfEra)       != null ? f : super.centuryOfEra();
            iEra                = (f = fields.era)                != null ? f : super.era();
        }

        int flags;
        if (iBase == null) {
            flags = 0;
        } else {
            flags = 
                ((iHourOfDay      == iBase.hourOfDay()      &&
                  iMinuteOfHour   == iBase.minuteOfHour()   &&
                  iSecondOfMinute == iBase.secondOfMinute() &&
                  iMillisOfSecond == iBase.millisOfSecond()   ) ? 1 : 0) |

                ((iMillisOfDay == iBase.millisOfDay()) ? 2 : 0) |

                ((iYear        == iBase.year()        &&
                  iMonthOfYear == iBase.monthOfYear() &&
                  iDayOfMonth  == iBase.dayOfMonth()    ) ? 4 : 0);
        }

        iBaseFlags = flags;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        setFields();
    }

    /**
     * A container of fields used for assembling a chronology.
     */
    public static final class Fields {
        public DurationField millis;
        public DurationField seconds;
        public DurationField minutes;
        public DurationField hours;
        public DurationField halfdays;
    
        public DurationField days;
        public DurationField weeks;
        public DurationField weekyears;
        public DurationField months;
        public DurationField years;
        public DurationField centuries;
        public DurationField eras;
    
        public DateTimeField millisOfSecond;
        public DateTimeField millisOfDay;
        public DateTimeField secondOfMinute;
        public DateTimeField secondOfDay;
        public DateTimeField minuteOfHour;
        public DateTimeField minuteOfDay;
        public DateTimeField hourOfDay;
        public DateTimeField clockhourOfDay;
        public DateTimeField hourOfHalfday;
        public DateTimeField clockhourOfHalfday;
        public DateTimeField halfdayOfDay;
    
        public DateTimeField dayOfWeek;
        public DateTimeField dayOfMonth;
        public DateTimeField dayOfYear;
        public DateTimeField weekOfWeekyear;
        public DateTimeField weekyear;
        public DateTimeField weekyearOfCentury;
        public DateTimeField monthOfYear;
        public DateTimeField year;
        public DateTimeField yearOfEra;
        public DateTimeField yearOfCentury;
        public DateTimeField centuryOfEra;
        public DateTimeField era;

        Fields() {
        }

        /**
         * Copy the supported fields from a chronology into this container.
         * 
         * @param chrono  the chronology to copy from, not null
         */
        public void copyFieldsFrom(Chronology chrono) {
            {
                DurationField f;
                if (isSupported(f = chrono.millis())) {
                    millis = f;
                }
                if (isSupported(f = chrono.seconds())) {
                    seconds = f;
                }
                if (isSupported(f = chrono.minutes())) {
                    minutes = f;
                }
                if (isSupported(f = chrono.hours())) {
                    hours = f;
                }
                if (isSupported(f = chrono.halfdays())) {
                    halfdays = f;
                }
                if (isSupported(f = chrono.days())) {
                    days = f;
                }
                if (isSupported(f = chrono.weeks())) {
                    weeks = f;
                }
                if (isSupported(f = chrono.weekyears())) {
                    weekyears = f;
                }
                if (isSupported(f = chrono.months())) {
                    months = f;
                }
                if (isSupported(f = chrono.years())) {
                    years = f;
                }
                if (isSupported(f = chrono.centuries())) {
                    centuries = f;
                }
                if (isSupported(f = chrono.eras())) {
                    eras = f;
                }
            }

            {
                DateTimeField f;
                if (isSupported(f = chrono.millisOfSecond())) {
                    millisOfSecond = f;
                }
                if (isSupported(f = chrono.millisOfDay())) {
                    millisOfDay = f;
                }
                if (isSupported(f = chrono.secondOfMinute())) {
                    secondOfMinute = f;
                }
                if (isSupported(f = chrono.secondOfDay())) {
                    secondOfDay = f;
                }
                if (isSupported(f = chrono.minuteOfHour())) {
                    minuteOfHour = f;
                }
                if (isSupported(f = chrono.minuteOfDay())) {
                    minuteOfDay = f;
                }
                if (isSupported(f = chrono.hourOfDay())) {
                    hourOfDay = f;
                }
                if (isSupported(f = chrono.clockhourOfDay())) {
                    clockhourOfDay = f;
                }
                if (isSupported(f = chrono.hourOfHalfday())) {
                    hourOfHalfday = f;
                }
                if (isSupported(f = chrono.clockhourOfHalfday())) {
                    clockhourOfHalfday = f;
                }
                if (isSupported(f = chrono.halfdayOfDay())) {
                    halfdayOfDay = f;
                }
                if (isSupported(f = chrono.dayOfWeek())) {
                    dayOfWeek = f;
                }
                if (isSupported(f = chrono.dayOfMonth())) {
                    dayOfMonth = f;
                }
                if (isSupported(f = chrono.dayOfYear())) {
                    dayOfYear = f;
                }
                if (isSupported(f = chrono.weekOfWeekyear())) {
                    weekOfWeekyear = f;
                }
                if (isSupported(f = chrono.weekyear())) {
                    weekyear = f;
                }
                if (isSupported(f = chrono.weekyearOfCentury())) {
                    weekyearOfCentury = f;
                }
                if (isSupported(f = chrono.monthOfYear())) {
                    monthOfYear = f;
                }
                if (isSupported(f = chrono.year())) {
                    year = f;
                }
                if (isSupported(f = chrono.yearOfEra())) {
                    yearOfEra = f;
                }
                if (isSupported(f = chrono.yearOfCentury())) {
                    yearOfCentury = f;
                }
                if (isSupported(f = chrono.centuryOfEra())) {
                    centuryOfEra = f;
                }
                if (isSupported(f = chrono.era())) {
                    era = f;
                }
            }
        }

        private static boolean isSupported(DurationField field) {
            return field == null ? false : field.isSupported();
        }

        private static boolean isSupported(DateTimeField field) {
            return field == null ? false : field.isSupported();
        }
    }
}
