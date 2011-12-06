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
 * Identifies a duration field, such as years or minutes, in a chronology-neutral way.
 * <p>
 * A duration field type defines the type of the field, such as hours.
 * If does not directly enable any calculations, however it does provide a
 * {@link #getField(Chronology)} method that returns the actual calculation engine
 * for a particular chronology.
 * <p>
 * Instances of <code>DurationFieldType</code> are singletons.
 * They can be compared using <code>==</code>.
 * <p>
 * If required, you can create your own field, for example a quarters.
 * You must create a subclass of <code>DurationFieldType</code> that defines the field type.
 * This class returns the actual calculation engine from {@link #getField(Chronology)}.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public abstract class DurationFieldType implements Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 8765135187319L;

    // Ordinals for standard field types.
    static final byte
        ERAS = 1,
        CENTURIES = 2,
        WEEKYEARS = 3,
        YEARS = 4,
        MONTHS = 5,
        WEEKS = 6,
        DAYS = 7,
        HALFDAYS = 8,
        HOURS = 9,
        MINUTES = 10,
        SECONDS = 11,
        MILLIS = 12;

    /** The eras field type. */
    static final DurationFieldType ERAS_TYPE = new StandardDurationFieldType("eras", ERAS);
    /** The centuries field type. */
    static final DurationFieldType CENTURIES_TYPE = new StandardDurationFieldType("centuries", CENTURIES);
    /** The weekyears field type. */
    static final DurationFieldType WEEKYEARS_TYPE = new StandardDurationFieldType("weekyears", WEEKYEARS);
    /** The years field type. */
    static final DurationFieldType YEARS_TYPE = new StandardDurationFieldType("years", YEARS);
    /** The months field type. */
    static final DurationFieldType MONTHS_TYPE = new StandardDurationFieldType("months", MONTHS);
    /** The weeks field type. */
    static final DurationFieldType WEEKS_TYPE = new StandardDurationFieldType("weeks", WEEKS);
    /** The days field type. */
    static final DurationFieldType DAYS_TYPE = new StandardDurationFieldType("days", DAYS);
    /** The halfdays field type. */
    static final DurationFieldType HALFDAYS_TYPE = new StandardDurationFieldType("halfdays", HALFDAYS);
    /** The hours field type. */
    static final DurationFieldType HOURS_TYPE = new StandardDurationFieldType("hours", HOURS);
    /** The minutes field type. */
    static final DurationFieldType MINUTES_TYPE = new StandardDurationFieldType("minutes", MINUTES);
    /** The seconds field type. */
    static final DurationFieldType SECONDS_TYPE = new StandardDurationFieldType("seconds", SECONDS);
    /** The millis field type. */
    static final DurationFieldType MILLIS_TYPE = new StandardDurationFieldType("millis", MILLIS);

    /** The name of the field type. */
    private final String iName;

    //-----------------------------------------------------------------------
    /**
     * Constructor.
     * 
     * @param name  the name to use, which by convention, are plural.
     */
    protected DurationFieldType(String name) {
        super();
        iName = name;
    }

    //-----------------------------------------------------------------------
    /**
     * Get the millis field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType millis() {
        return MILLIS_TYPE;
    }

    /**
     * Get the seconds field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType seconds() {
        return SECONDS_TYPE;
    }

    /**
     * Get the minutes field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType minutes() {
        return MINUTES_TYPE;
    }

    /**
     * Get the hours field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType hours() {
        return HOURS_TYPE;
    }

    /**
     * Get the halfdays field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType halfdays() {
        return HALFDAYS_TYPE;
    }

    //-----------------------------------------------------------------------
    /**
     * Get the days field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType days() {
        return DAYS_TYPE;
    }

    /**
     * Get the weeks field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType weeks() {
        return WEEKS_TYPE;
    }

    /**
     * Get the weekyears field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType weekyears() {
        return WEEKYEARS_TYPE;
    }

    /**
     * Get the months field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType months() {
        return MONTHS_TYPE;
    }

    /**
     * Get the years field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType years() {
        return YEARS_TYPE;
    }

    /**
     * Get the centuries field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType centuries() {
        return CENTURIES_TYPE;
    }

    /**
     * Get the eras field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType eras() {
        return ERAS_TYPE;
    }

    //-----------------------------------------------------------------------
    /**
     * Get the name of the field.
     * By convention, names are plural.
     * 
     * @return field name
     */
    public String getName() {
        return iName;
    }

    /**
     * Gets a suitable field for this type from the given Chronology.
     *
     * @param chronology  the chronology to use, null means ISOChronology in default zone
     * @return a suitable field
     */
    public abstract DurationField getField(Chronology chronology);

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

    private static class StandardDurationFieldType extends DurationFieldType {
        /** Serialization version */
        private static final long serialVersionUID = 31156755687123L;

        /** The ordinal of the standard field type, for switch statements */
        private final byte iOrdinal;

        /**
         * Constructor.
         * 
         * @param name  the name to use
         */
        StandardDurationFieldType(String name, byte ordinal) {
            super(name);
            iOrdinal = ordinal;
        }

        /** @inheritdoc */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof StandardDurationFieldType) {
                return iOrdinal == ((StandardDurationFieldType) obj).iOrdinal;
            }
            return false;
        }

        /** @inheritdoc */
        @Override
        public int hashCode() {
            return (1 << iOrdinal);
        }

        public DurationField getField(Chronology chronology) {
            chronology = DateTimeUtils.getChronology(chronology);
            
            switch (iOrdinal) {
                case ERAS:
                    return chronology.eras();
                case CENTURIES:
                    return chronology.centuries();
                case WEEKYEARS:
                    return chronology.weekyears();
                case YEARS:
                    return chronology.years();
                case MONTHS:
                    return chronology.months();
                case WEEKS:
                    return chronology.weeks();
                case DAYS:
                    return chronology.days();
                case HALFDAYS:
                    return chronology.halfdays();
                case HOURS:
                    return chronology.hours();
                case MINUTES:
                    return chronology.minutes();
                case SECONDS:
                    return chronology.seconds();
                case MILLIS:
                    return chronology.millis();
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
                case ERAS:
                    return ERAS_TYPE;
                case CENTURIES:
                    return CENTURIES_TYPE;
                case WEEKYEARS:
                    return WEEKYEARS_TYPE;
                case YEARS:
                    return YEARS_TYPE;
                case MONTHS:
                    return MONTHS_TYPE;
                case WEEKS:
                    return WEEKS_TYPE;
                case DAYS:
                    return DAYS_TYPE;
                case HALFDAYS:
                    return HALFDAYS_TYPE;
                case HOURS:
                    return HOURS_TYPE;
                case MINUTES:
                    return MINUTES_TYPE;
                case SECONDS:
                    return SECONDS_TYPE;
                case MILLIS:
                    return MILLIS_TYPE;
                default:
                    // Shouldn't happen.
                    return this;
            }
        }
    }
}
