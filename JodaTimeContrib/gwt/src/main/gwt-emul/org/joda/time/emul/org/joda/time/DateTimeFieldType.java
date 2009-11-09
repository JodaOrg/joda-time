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
package org.joda.time;

import java.io.Serializable;

/**
 * Identifies a field, such as year or minuteOfHour, in a chronology-neutral way.
 * <p>
 * A field type defines the type of the field, such as hourOfDay.
 * If does not directly enable any calculations, however it does provide a
 * {@link #getField(Chronology)} method that returns the actual calculation engine
 * for a particular chronology.
 * It also provides access to the related {@link DurationFieldType}s.
 * <p>
 * Instances of <code>DateTimeFieldType</code> are singletons.
 * They can be compared using <code>==</code>.
 * <p>
 * If required, you can create your own field, for example a quarterOfYear.
 * You must create a subclass of <code>DateTimeFieldType</code> that defines the field type.
 * This class returns the actual calculation engine from {@link #getField(Chronology)}.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public abstract class DateTimeFieldType implements Serializable {

    /** Serialization version */
    private static final long serialVersionUID = -42615285973990L;

    /** Ordinal values for standard field types. */
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
     * Gets a suitable field for this type from the given Chronology.
     *
     * @param chronology  the chronology to use, null means ISOChronology in default zone
     * @return a suitable field
     */
    public abstract DateTimeField getField(Chronology chronology);

    /**
     * Checks whether this field supported in the given Chronology.
     *
     * @param chronology  the chronology to use, null means ISOChronology in default zone
     * @return true if supported
     */
    public boolean isSupported(Chronology chronology) {
        return getField(chronology).isSupported();
    }

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
        private final transient DurationFieldType iUnitType;
        /** The range duration of the field. */
        private final transient DurationFieldType iRangeType;

        /**
         * Constructor.
         * 
         * @param name  the name to use
         * @param ordinal  the byte value for the oridinal index
         * @param unitType  the unit duration type
         * @param rangeType  the range duration type
         */
        StandardDateTimeFieldType(String name, byte ordinal,
                                  DurationFieldType unitType, DurationFieldType rangeType) {
            super(name);
            iOrdinal = ordinal;
            iUnitType = unitType;
            iRangeType = rangeType;
        }

        /** @inheritdoc */
        public DurationFieldType getDurationType() {
            return iUnitType;
        }

        /** @inheritdoc */
        public DurationFieldType getRangeDurationType() {
            return iRangeType;
        }

        /** @inheritdoc */
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
                    throw new InternalError();
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
