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
 * Identifies a field, such as year or minutes, in a chronology-neutral way.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public class DurationFieldType implements Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 8765135187319L;

    /** The cache of name to type used to ensure singletons. */
    private static Map cCache = new HashMap();
    /** The eras field. */
    private static final DurationFieldType ERAS = new DurationFieldType("eras");
    /** The centuries field. */
    private static final DurationFieldType CENTURIES = new DurationFieldType("centuries");
    /** The weekyears field. */
    private static final DurationFieldType WEEKYEARS = new DurationFieldType("weekyears");
    /** The years field. */
    private static final DurationFieldType YEARS = new DurationFieldType("years");
    /** The months field. */
    private static final DurationFieldType MONTHS = new DurationFieldType("months");
    /** The weeks field. */
    private static final DurationFieldType WEEKS = new DurationFieldType("weeks");
    /** The days field. */
    private static final DurationFieldType DAYS = new DurationFieldType("days");
    /** The halfdays field. */
    private static final DurationFieldType HALFDAYS = new DurationFieldType("halfdays");
    /** The hours field. */
    private static final DurationFieldType HOURS = new DurationFieldType("hours");
    /** The minutes field. */
    private static final DurationFieldType MINUTES = new DurationFieldType("minutes");
    /** The seconds field. */
    private static final DurationFieldType SECONDS = new DurationFieldType("seconds");
    /** The millis field. */
    private static final DurationFieldType MILLIS = new DurationFieldType("millis");

    /** The name of the field. */
    private final String iName;

    //-----------------------------------------------------------------------
    /**
     * Factory method to obtain a DurationFieldType by name ensuring each
     * type is a singleton.
     * <p>
     * If the name is not found, an error is thrown.
     * 
     * @param name  the name to retrieve
     * @return the singleton type
     */
    public static synchronized DurationFieldType getInstance(String name) {
        DurationFieldType type = (DurationFieldType) cCache.get(name);
        if (type == null) {
            throw new IllegalArgumentException("The field '" + name + "' is unknown");
        }
        return type;
    }

    /**
     * Factory method to define a DurationFieldType by name ensuring each
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
    public static synchronized DurationFieldType define(String name) {
        DurationFieldType type = (DurationFieldType) cCache.get(name);
        if (type == null) {
            return new DurationFieldType(name);
        }
        return type;
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor.
     * 
     * @param name  the name to use
     */
    private DurationFieldType(String name) {
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
     * Get the millis field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType millis() {
        return MILLIS;
    }

    /**
     * Get the seconds field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType seconds() {
        return SECONDS;
    }

    /**
     * Get the minutes field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType minutes() {
        return MINUTES;
    }

    /**
     * Get the hours field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType hours() {
        return HOURS;
    }

    /**
     * Get the halfdays field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType halfdays() {
        return HALFDAYS;
    }

    //-----------------------------------------------------------------------
    /**
     * Get the days field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType days() {
        return DAYS;
    }

    /**
     * Get the weeks field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType weeks() {
        return WEEKS;
    }

    /**
     * Get the weekyears field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType weekyears() {
        return WEEKYEARS;
    }

    /**
     * Get the months field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType months() {
        return MONTHS;
    }

    /**
     * Get the years field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType years() {
        return YEARS;
    }

    /**
     * Get the centuries field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType centuries() {
        return CENTURIES;
    }

    /**
     * Get the eras field type.
     * 
     * @return the DateTimeFieldType constant
     */
    public static DurationFieldType eras() {
        return ERAS;
    }

    //-----------------------------------------------------------------------
    /**
     * Get the name of the field.
     * By convention, names have a plural name.
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
