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
import java.util.HashMap;
import java.util.Map;

/**
 * Identifies a field, such as year or minuteOfHour, in a chronology-neutral way.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public class DateTimeFieldType implements Serializable {

    /** Serialization version */
    private static final long serialVersionUID = -42615285973990L;

    /** The cache of name to type used to ensure singletons. */
    private static Map cCache = new HashMap();
    /** The era field. */
    private static final DateTimeFieldType ERA = new DateTimeFieldType("era");
    /** The yearOfEra field. */
    private static final DateTimeFieldType YEAR_OF_ERA = new DateTimeFieldType("yearOfEra");
    /** The centuryOfEra field. */
    private static final DateTimeFieldType CENTURY_OF_ERA = new DateTimeFieldType("centuryOfEra");
    /** The yearOfCentury field. */
    private static final DateTimeFieldType YEAR_OF_CENTURY = new DateTimeFieldType("yearOfCentury");
    /** The year field. */
    private static final DateTimeFieldType YEAR = new DateTimeFieldType("year");
    /** The dayOfYear field. */
    private static final DateTimeFieldType DAY_OF_YEAR = new DateTimeFieldType("dayOfYear");
    /** The monthOfYear field. */
    private static final DateTimeFieldType MONTH_OF_YEAR = new DateTimeFieldType("monthOfYear");
    /** The dayOfMonth field. */
    private static final DateTimeFieldType DAY_OF_MONTH = new DateTimeFieldType("dayOfMonth");
    /** The weekyearOfCentury field. */
    private static final DateTimeFieldType WEEKYEAR_OF_CENTURY = new DateTimeFieldType("weekyearOfCentury");
    /** The weekyear field. */
    private static final DateTimeFieldType WEEKYEAR = new DateTimeFieldType("weekyear");
    /** The weekOfWeekyear field. */
    private static final DateTimeFieldType WEEK_OF_WEEKYEAR = new DateTimeFieldType("weekOfWeekyear");
    /** The dayOfWeek field. */
    private static final DateTimeFieldType DAY_OF_WEEK = new DateTimeFieldType("dayOfWeek");

    /** The halfday field. */
    private static final DateTimeFieldType HALFDAY_OF_DAY = new DateTimeFieldType("halfdayOfDay");
    /** The hourOfHalfday field. */
    private static final DateTimeFieldType HOUR_OF_HALFDAY = new DateTimeFieldType("hourOfHalfday");
    /** The clockhourOfHalfday field. */
    private static final DateTimeFieldType CLOCKHOUR_OF_HALFDAY = new DateTimeFieldType("clockhourOfHalfday");
    /** The clockhourOfDay field. */
    private static final DateTimeFieldType CLOCKHOUR_OF_DAY = new DateTimeFieldType("clockhourOfDay");
    /** The hourOfDay field. */
    private static final DateTimeFieldType HOUR_OF_DAY = new DateTimeFieldType("hourOfDay");
    /** The minuteOfDay field. */
    private static final DateTimeFieldType MINUTE_OF_DAY = new DateTimeFieldType("minuteOfDay");
    /** The minuteOfHour field. */
    private static final DateTimeFieldType MINUTE_OF_HOUR = new DateTimeFieldType("minuteOfHour");
    /** The secondOfDay field. */
    private static final DateTimeFieldType SECOND_OF_DAY = new DateTimeFieldType("secondOfDay");
    /** The secondOfMinute field. */
    private static final DateTimeFieldType SECOND_OF_MINUTE = new DateTimeFieldType("secondOfMinute");
    /** The millisOfDay field. */
    private static final DateTimeFieldType MILLIS_OF_DAY = new DateTimeFieldType("millisOfDay");
    /** The millisOfSecond field. */
    private static final DateTimeFieldType MILLIS_OF_SECOND = new DateTimeFieldType("millisOfSecond");

    /** The name of the field. */
    private final String iName;

    //-----------------------------------------------------------------------
    /**
     * Factory method to obtain a DateTimeFieldType by name ensuring each
     * type is a singleton.
     * <p>
     * If the name is not found, an error is thrown.
     * 
     * @param name  the name to retrieve
     * @return the singleton type
     */
    public static synchronized DateTimeFieldType getInstance(String name) {
        DateTimeFieldType type = (DateTimeFieldType) cCache.get(name);
        if (type == null) {
            throw new IllegalArgumentException("The field '" + name + "' is unknown");
        }
        return type;
    }

    /**
     * Factory method to define a DateTimeFieldType by name ensuring each
     * type is a singleton.
     * <p>
     * This method checks to see if the name is previously defined, in which
     * case it returns the previously defined type object.
     * If the name is not found, a new type is created.
     * <p>
     * It is intended that this method is called once in your program startup
     * and then you use the <code>getInstance</code> method. This could be
     * achieved by subclassing this class.
     * 
     * @param name  the name to retrieve
     * @return the singleton type
     */
    public static synchronized DateTimeFieldType define(String name) {
        DateTimeFieldType type = (DateTimeFieldType) cCache.get(name);
        if (type == null) {
            return new DateTimeFieldType(name);
        }
        return type;
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor.
     * 
     * @param name  the name to use
     */
    private DateTimeFieldType(String name) {
        super();
        iName = name;
        cCache.put(name, this);
    }

    /**
     * Ensure a singleton is returned.
     * 
     * @return the singleton type
     */
    private Object readResolve() {
        return getInstance(iName);
    }

    //-----------------------------------------------------------------------
    /**
     * Get the millis of second field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType millisOfSecond() {
        return MILLIS_OF_SECOND;
    }

    /**
     * Get the millis of day field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType millisOfDay() {
        return MILLIS_OF_DAY;
    }

    /**
     * Get the second of minute field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType secondOfMinute() {
        return SECOND_OF_MINUTE;
    }

    /**
     * Get the second of day field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType secondOfDay() {
        return SECOND_OF_DAY;
    }

    /**
     * Get the minute of hour field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType minuteOfHour() {
        return MINUTE_OF_HOUR;
    }

    /**
     * Get the minute of day field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType minuteOfDay() {
        return MINUTE_OF_DAY;
    }

    /**
     * Get the hour of day (0-23) field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType hourOfDay() {
        return HOUR_OF_DAY;
    }

    /**
     * Get the hour of day (offset to 1-24) field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType clockhourOfDay() {
        return CLOCKHOUR_OF_DAY;
    }

    /**
     * Get the hour of am/pm (0-11) field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType hourOfHalfday() {
        return HOUR_OF_HALFDAY;
    }

    /**
     * Get the hour of am/pm (offset to 1-12) field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType clockhourOfHalfday() {
        return CLOCKHOUR_OF_HALFDAY;
    }

    /**
     * Get the AM(0) PM(1) field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType halfdayOfDay() {
        return HALFDAY_OF_DAY;
    }

    //-----------------------------------------------------------------------
    /**
     * Get the day of week field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType dayOfWeek() {
        return DAY_OF_WEEK;
    }

    /**
     * Get the day of month field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType dayOfMonth() {
        return DAY_OF_MONTH;
    }

    /**
     * Get the day of year field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType dayOfYear() {
        return DAY_OF_YEAR;
    }

    /**
     * Get the week of a week based year field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType weekOfWeekyear() {
        return WEEK_OF_WEEKYEAR;
    }

    /**
     * Get the year of a week based year field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType weekyear() {
        return WEEKYEAR;
    }

    /**
     * Get the year of a week based year within a century field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType weekyearOfCentury() {
        return WEEKYEAR_OF_CENTURY;
    }

    /**
     * Get the month of year field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType monthOfYear() {
        return MONTH_OF_YEAR;
    }

    /**
     * Get the year field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType year() {
        return YEAR;
    }

    /**
     * Get the year of era field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType yearOfEra() {
        return YEAR_OF_ERA;
    }

    /**
     * Get the year of century field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType yearOfCentury() {
        return YEAR_OF_CENTURY;
    }

    /**
     * Get the century of era field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType centuryOfEra() {
        return CENTURY_OF_ERA;
    }

    /**
     * Get the era field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DateTimeFieldType era() {
        return ERA;
    }

    //-----------------------------------------------------------------------
    /**
     * Get the name of the field.
     * By convention, names follow a pattern of "dddOfRrr", where "ddd" represents
     * the (singular) duration field name and "Rrr" represents the (singular)
     * range duration field name. If the range field is not applicable, then
     * the name of the field is simply the (singular) duration field name.
     * 
     * @return field name
     */
    public String getName() {
        return iName;
    }

    /**
     * Get a suitable debug string.
     * 
     * @return debug string
     */
    public String toString() {
        return getName();
    }

}
