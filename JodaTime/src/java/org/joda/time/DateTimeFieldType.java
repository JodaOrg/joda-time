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
package org.joda.time;

import java.io.Serializable;

/**
 * Identifies a field, such as year or minuteOfHour, in a chronology-neutral way.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public abstract class DateTimeFieldType implements Serializable {

    /** Serialization version */
    private static final long serialVersionUID = -42615285973990L;

    // Ordinals for standard field types.
    static final byte
        ERA = 1,
        YEAR_OF_ERA = 2,
        CENTURY_OF_ERA = 3,
        YEAR_OF_CENTURY = 4,
        YEAR = 5,
        DAY_OF_YEAR = 6,
        MONTH_OF_YEAR = 7,
        DAY_OF_MONTH = 8,
        WEEKYEAR_OF_CENTURY = 9,
        WEEKYEAR = 10,
        WEEK_OF_WEEKYEAR = 11,
        DAY_OF_WEEK = 12,
        HALFDAY_OF_DAY = 13,
        HOUR_OF_HALFDAY = 14,
        CLOCKHOUR_OF_HALFDAY = 15,
        CLOCKHOUR_OF_DAY = 16,
        HOUR_OF_DAY = 17,
        MINUTE_OF_DAY = 18,
        MINUTE_OF_HOUR = 19,
        SECOND_OF_DAY = 20,
        SECOND_OF_MINUTE = 21,
        MILLIS_OF_DAY = 22,
        MILLIS_OF_SECOND = 23;

    /** The era field type. */
    private static final DateTimeFieldType ERA_TYPE = new StandardDateTimeFieldType(
        "era", ERA, DurationFieldType.eras(), null);
    /** The yearOfEra field type. */
    private static final DateTimeFieldType YEAR_OF_ERA_TYPE = new StandardDateTimeFieldType(
        "yearOfEra", YEAR_OF_ERA, DurationFieldType.years(), DurationFieldType.eras());
    /** The centuryOfEra field type. */
    private static final DateTimeFieldType CENTURY_OF_ERA_TYPE = new StandardDateTimeFieldType(
        "centuryOfEra", CENTURY_OF_ERA, DurationFieldType.centuries(), DurationFieldType.eras());
    /** The yearOfCentury field type. */
    private static final DateTimeFieldType YEAR_OF_CENTURY_TYPE = new StandardDateTimeFieldType(
        "yearOfCentury", YEAR_OF_CENTURY, DurationFieldType.years(), DurationFieldType.centuries());
    /** The year field type. */
    private static final DateTimeFieldType YEAR_TYPE = new StandardDateTimeFieldType(
        "year", YEAR, DurationFieldType.years(), null);
    /** The dayOfYear field type. */
    private static final DateTimeFieldType DAY_OF_YEAR_TYPE = new StandardDateTimeFieldType(
        "dayOfYear", DAY_OF_YEAR, DurationFieldType.days(), DurationFieldType.years());
    /** The monthOfYear field type. */
    private static final DateTimeFieldType MONTH_OF_YEAR_TYPE = new StandardDateTimeFieldType(
        "monthOfYear", MONTH_OF_YEAR, DurationFieldType.months(), DurationFieldType.years());
    /** The dayOfMonth field type. */
    private static final DateTimeFieldType DAY_OF_MONTH_TYPE = new StandardDateTimeFieldType(
        "dayOfMonth", DAY_OF_MONTH, DurationFieldType.days(), DurationFieldType.months());
    /** The weekyearOfCentury field type. */
    private static final DateTimeFieldType WEEKYEAR_OF_CENTURY_TYPE = new StandardDateTimeFieldType(
        "weekyearOfCentury", WEEKYEAR_OF_CENTURY, DurationFieldType.weekyears(), DurationFieldType.centuries());
    /** The weekyear field type. */
    private static final DateTimeFieldType WEEKYEAR_TYPE = new StandardDateTimeFieldType(
        "weekyear", WEEKYEAR, DurationFieldType.weekyears(), null);
    /** The weekOfWeekyear field type. */
    private static final DateTimeFieldType WEEK_OF_WEEKYEAR_TYPE = new StandardDateTimeFieldType(
        "weekOfWeekyear", WEEK_OF_WEEKYEAR, DurationFieldType.weeks(), DurationFieldType.weekyears());
    /** The dayOfWeek field type. */
    private static final DateTimeFieldType DAY_OF_WEEK_TYPE = new StandardDateTimeFieldType(
        "dayOfWeek", DAY_OF_WEEK, DurationFieldType.days(), DurationFieldType.weeks());

    /** The halfday field type. */
    private static final DateTimeFieldType HALFDAY_OF_DAY_TYPE = new StandardDateTimeFieldType(
        "halfdayOfDay", HALFDAY_OF_DAY, DurationFieldType.halfdays(), DurationFieldType.days());
    /** The hourOfHalfday field type. */
    private static final DateTimeFieldType HOUR_OF_HALFDAY_TYPE = new StandardDateTimeFieldType(
        "hourOfHalfday", HOUR_OF_HALFDAY, DurationFieldType.hours(), DurationFieldType.halfdays());
    /** The clockhourOfHalfday field type. */
    private static final DateTimeFieldType CLOCKHOUR_OF_HALFDAY_TYPE = new StandardDateTimeFieldType(
        "clockhourOfHalfday", CLOCKHOUR_OF_HALFDAY, DurationFieldType.hours(), DurationFieldType.halfdays());
    /** The clockhourOfDay field type. */
    private static final DateTimeFieldType CLOCKHOUR_OF_DAY_TYPE = new StandardDateTimeFieldType(
        "clockhourOfDay", CLOCKHOUR_OF_DAY, DurationFieldType.hours(), DurationFieldType.days());
    /** The hourOfDay field type. */
    private static final DateTimeFieldType HOUR_OF_DAY_TYPE = new StandardDateTimeFieldType(
        "hourOfDay", HOUR_OF_DAY, DurationFieldType.hours(), DurationFieldType.days());
    /** The minuteOfDay field type. */
    private static final DateTimeFieldType MINUTE_OF_DAY_TYPE = new StandardDateTimeFieldType(
        "minuteOfDay", MINUTE_OF_DAY, DurationFieldType.minutes(), DurationFieldType.days());
    /** The minuteOfHour field type. */
    private static final DateTimeFieldType MINUTE_OF_HOUR_TYPE = new StandardDateTimeFieldType(
        "minuteOfHour", MINUTE_OF_HOUR, DurationFieldType.minutes(), DurationFieldType.hours());
    /** The secondOfDay field type. */
    private static final DateTimeFieldType SECOND_OF_DAY_TYPE = new StandardDateTimeFieldType(
        "secondOfDay", SECOND_OF_DAY, DurationFieldType.seconds(), DurationFieldType.days());
    /** The secondOfMinute field type. */
    private static final DateTimeFieldType SECOND_OF_MINUTE_TYPE = new StandardDateTimeFieldType(
        "secondOfMinute", SECOND_OF_MINUTE, DurationFieldType.seconds(), DurationFieldType.minutes());
    /** The millisOfDay field type. */
    private static final DateTimeFieldType MILLIS_OF_DAY_TYPE = new StandardDateTimeFieldType(
        "millisOfDay", MILLIS_OF_DAY, DurationFieldType.millis(), DurationFieldType.days());
    /** The millisOfSecond field type. */
    private static final DateTimeFieldType MILLIS_OF_SECOND_TYPE = new StandardDateTimeFieldType(
        "millisOfSecond", MILLIS_OF_SECOND, DurationFieldType.millis(), DurationFieldType.seconds());

    /** The name of the field. */
    private final String iName;

    //-----------------------------------------------------------------------
    /**
     * Constructor.
     * 
     * @param name  the name to use
     */
    protected DateTimeFieldType(String name) {
        super();
        iName = name;
    }

    //-----------------------------------------------------------------------
    /**
     * Get the millis of second field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType millisOfSecond() {
        return MILLIS_OF_SECOND_TYPE;
    }

    /**
     * Get the millis of day field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType millisOfDay() {
        return MILLIS_OF_DAY_TYPE;
    }

    /**
     * Get the second of minute field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType secondOfMinute() {
        return SECOND_OF_MINUTE_TYPE;
    }

    /**
     * Get the second of day field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType secondOfDay() {
        return SECOND_OF_DAY_TYPE;
    }

    /**
     * Get the minute of hour field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType minuteOfHour() {
        return MINUTE_OF_HOUR_TYPE;
    }

    /**
     * Get the minute of day field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType minuteOfDay() {
        return MINUTE_OF_DAY_TYPE;
    }

    /**
     * Get the hour of day (0-23) field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType hourOfDay() {
        return HOUR_OF_DAY_TYPE;
    }

    /**
     * Get the hour of day (offset to 1-24) field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType clockhourOfDay() {
        return CLOCKHOUR_OF_DAY_TYPE;
    }

    /**
     * Get the hour of am/pm (0-11) field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType hourOfHalfday() {
        return HOUR_OF_HALFDAY_TYPE;
    }

    /**
     * Get the hour of am/pm (offset to 1-12) field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType clockhourOfHalfday() {
        return CLOCKHOUR_OF_HALFDAY_TYPE;
    }

    /**
     * Get the AM(0) PM(1) field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType halfdayOfDay() {
        return HALFDAY_OF_DAY_TYPE;
    }

    //-----------------------------------------------------------------------
    /**
     * Get the day of week field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType dayOfWeek() {
        return DAY_OF_WEEK_TYPE;
    }

    /**
     * Get the day of month field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType dayOfMonth() {
        return DAY_OF_MONTH_TYPE;
    }

    /**
     * Get the day of year field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType dayOfYear() {
        return DAY_OF_YEAR_TYPE;
    }

    /**
     * Get the week of a week based year field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType weekOfWeekyear() {
        return WEEK_OF_WEEKYEAR_TYPE;
    }

    /**
     * Get the year of a week based year field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType weekyear() {
        return WEEKYEAR_TYPE;
    }

    /**
     * Get the year of a week based year within a century field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType weekyearOfCentury() {
        return WEEKYEAR_OF_CENTURY_TYPE;
    }

    /**
     * Get the month of year field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType monthOfYear() {
        return MONTH_OF_YEAR_TYPE;
    }

    /**
     * Get the year field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType year() {
        return YEAR_TYPE;
    }

    /**
     * Get the year of era field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType yearOfEra() {
        return YEAR_OF_ERA_TYPE;
    }

    /**
     * Get the year of century field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType yearOfCentury() {
        return YEAR_OF_CENTURY_TYPE;
    }

    /**
     * Get the century of era field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType centuryOfEra() {
        return CENTURY_OF_ERA_TYPE;
    }

    /**
     * Get the era field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType era() {
        return ERA_TYPE;
    }

    //-----------------------------------------------------------------------
    /**
     * Get the name of the field.
     * <p>
     * By convention, names follow a pattern of "dddOfRrr", where "ddd" represents
     * the (singular) duration unit field name and "Rrr" represents the (singular)
     * duration range field name. If the range field is not applicable, then
     * the name of the field is simply the (singular) duration field name.
     * 
     * @return field name
     */
    public String getName() {
        return iName;
    }

    /**
     * Get the duration unit of the field.
     * 
     * @return duration unit of the field, never null
     */
    public abstract DurationFieldType getDurationType();

    /**
     * Get the duration range of the field.
     * 
     * @return duration range of the field, null if unbounded
     */
    public abstract DurationFieldType getRangeDurationType();

    /**
     * Select a suitable field for this type from the given Chronology.
     *
     * @param chronology Chronology to select a field from, null means
     * ISOChronology in default zone
     * @return a suitable field
     */
    public abstract DateTimeField getField(Chronology chronology);

    /**
     * Get a suitable debug string.
     * 
     * @return debug string
     */
    public String toString() {
        return getName();
    }

    private static class StandardDateTimeFieldType extends DateTimeFieldType {
        /** Serialization version */
        private static final long serialVersionUID = -9937958251642L;

        /** The ordinal of the standard field type, for switch statements */
        private final byte iOrdinal;

        /** The unit duration of the field. */
        private transient final DurationFieldType iUnitType;
        /** The range duration of the field. */
        private transient final DurationFieldType iRangeType;

        /**
         * Constructor.
         * 
         * @param name  the name to use
         */
        StandardDateTimeFieldType(String name, byte ordinal,
                                  DurationFieldType unitType, DurationFieldType rangeType) {
            super(name);
            iOrdinal = ordinal;
            iUnitType = unitType;
            iRangeType = rangeType;
        }

        public DurationFieldType getDurationType() {
            return iUnitType;
        }

        public DurationFieldType getRangeDurationType() {
            return iRangeType;
        }

        public DateTimeField getField(Chronology chronology) {
            chronology = DateTimeUtils.getChronology(chronology);

            switch (iOrdinal) {
            case ERA:
                return chronology.era();
            case YEAR_OF_ERA:
                return chronology.yearOfEra();
            case CENTURY_OF_ERA:
                return chronology.centuryOfEra();
            case YEAR_OF_CENTURY:
                return chronology.yearOfCentury();
            case YEAR:
                return chronology.year();
            case DAY_OF_YEAR:
                return chronology.dayOfYear();
            case MONTH_OF_YEAR:
                return chronology.monthOfYear();
            case DAY_OF_MONTH:
                return chronology.dayOfMonth();
            case WEEKYEAR_OF_CENTURY:
                return chronology.weekyearOfCentury();
            case WEEKYEAR:
                return chronology.weekyear();
            case WEEK_OF_WEEKYEAR:
                return chronology.weekOfWeekyear();
            case DAY_OF_WEEK:
                return chronology.dayOfWeek();
            case HALFDAY_OF_DAY:
                return chronology.halfdayOfDay();
            case HOUR_OF_HALFDAY:
                return chronology.hourOfHalfday();
            case CLOCKHOUR_OF_HALFDAY:
                return chronology.clockhourOfHalfday();
            case CLOCKHOUR_OF_DAY:
                return chronology.clockhourOfDay();
            case HOUR_OF_DAY:
                return chronology.hourOfDay();
            case MINUTE_OF_DAY:
                return chronology.minuteOfDay();
            case MINUTE_OF_HOUR:
                return chronology.minuteOfHour();
            case SECOND_OF_DAY:
                return chronology.secondOfDay();
            case SECOND_OF_MINUTE:
                return chronology.secondOfMinute();
            case MILLIS_OF_DAY:
                return chronology.millisOfDay();
            case MILLIS_OF_SECOND:
                return chronology.millisOfSecond();
            default:
                // Shouldn't happen.
                throw new IllegalStateException();
            }
        }

        /**
         * Ensure a singleton is returned.
         * 
         * @return the singleton type
         */
        private Object readResolve() {
            switch (iOrdinal) {
            case ERA:
                return ERA_TYPE;
            case YEAR_OF_ERA:
                return YEAR_OF_ERA_TYPE;
            case CENTURY_OF_ERA:
                return CENTURY_OF_ERA_TYPE;
            case YEAR_OF_CENTURY:
                return YEAR_OF_CENTURY_TYPE;
            case YEAR:
                return YEAR_TYPE;
            case DAY_OF_YEAR:
                return DAY_OF_YEAR_TYPE;
            case MONTH_OF_YEAR:
                return MONTH_OF_YEAR_TYPE;
            case DAY_OF_MONTH:
                return DAY_OF_MONTH_TYPE;
            case WEEKYEAR_OF_CENTURY:
                return WEEKYEAR_OF_CENTURY_TYPE;
            case WEEKYEAR:
                return WEEKYEAR_TYPE;
            case WEEK_OF_WEEKYEAR:
                return WEEK_OF_WEEKYEAR_TYPE;
            case DAY_OF_WEEK:
                return DAY_OF_WEEK_TYPE;
            case HALFDAY_OF_DAY:
                return HALFDAY_OF_DAY_TYPE;
            case HOUR_OF_HALFDAY:
                return HOUR_OF_HALFDAY_TYPE;
            case CLOCKHOUR_OF_HALFDAY:
                return CLOCKHOUR_OF_HALFDAY_TYPE;
            case CLOCKHOUR_OF_DAY:
                return CLOCKHOUR_OF_DAY_TYPE;
            case HOUR_OF_DAY:
                return HOUR_OF_DAY_TYPE;
            case MINUTE_OF_DAY:
                return MINUTE_OF_DAY_TYPE;
            case MINUTE_OF_HOUR:
                return MINUTE_OF_HOUR_TYPE;
            case SECOND_OF_DAY:
                return SECOND_OF_DAY_TYPE;
            case SECOND_OF_MINUTE:
                return SECOND_OF_MINUTE_TYPE;
            case MILLIS_OF_DAY:
                return MILLIS_OF_DAY_TYPE;
            case MILLIS_OF_SECOND:
                return MILLIS_OF_SECOND_TYPE;
            default:
                // Shouldn't happen.
                return this;
            }
        }
    }

}
