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
 * Identifies a field, such as year or minutes, in a chronology-neutral way.
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
     * Select a suitable field for this type from the given Chronology.
     *
     * @param chronology Chronology to select a field from, null means
     * ISOChronology in default zone
     * @return a suitable field
     */
    public abstract DurationField getField(Chronology chronology);

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
                // TODO: Add to Chronology
                //case HALFDAYS:
                //return chronology.halfdays();
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
