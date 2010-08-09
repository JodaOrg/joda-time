/*
 *  Copyright 2001-2009 Stephen Colebourne
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

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.field.DelegatedDateTimeField;
import org.joda.time.field.DividedDateTimeField;
import org.joda.time.field.OffsetDateTimeField;
import org.joda.time.field.RemainderDateTimeField;
import org.joda.time.field.SkipUndoDateTimeField;

/**
 * A chronology that matches the BuddhistCalendar class supplied by Sun.
 * <p>
 * The chronology is identical to the Gregorian/Julian, except that the
 * year is offset by +543 and the era is named 'BE' for Buddhist Era.
 * <p>
 * This class was intended by Sun to model the calendar used in Thailand.
 * However, the actual rules for Thailand are much more involved than
 * this class covers. (This class is accurate after 1941-01-01 ISO).
 * <p>
 * This chronlogy is being retained for those who want a same effect
 * replacement for the Sun class. It is hoped that community support will
 * enable a more accurate chronology for Thailand, to be developed.
 * <p>
 * BuddhistChronology is thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class BuddhistChronology extends AssembledChronology {
    
    /** Serialization lock */
    private static final long serialVersionUID = -3474595157769370126L;

    /**
     * Constant value for 'Buddhist Era', equivalent to the value returned
     * for AD/CE. Note that this differs from the constant in BuddhistCalendar.
     */
    public static final int BE = DateTimeConstants.CE;

    /** A singleton era field. */
    private static final DateTimeField ERA_FIELD = new BasicSingleEraDateTimeField("BE");

    /** Number of years difference in calendars. */
    private static final int BUDDHIST_OFFSET = 543;

    /** Cache of zone to chronology */
    private static final Map<DateTimeZone, BuddhistChronology> cCache = new HashMap<DateTimeZone, BuddhistChronology>();

    /** UTC instance of the chronology */
    private static final BuddhistChronology INSTANCE_UTC = getInstance(DateTimeZone.UTC);

    /**
     * Standard instance of a Buddhist Chronology, that matches
     * Sun's BuddhistCalendar class. This means that it follows the
     * GregorianJulian calendar rules with a cutover date.
     * <p>
     * The time zone of the returned instance is UTC.
     */
    public static BuddhistChronology getInstanceUTC() {
        return INSTANCE_UTC;
    }

    /**
     * Standard instance of a Buddhist Chronology, that matches
     * Sun's BuddhistCalendar class. This means that it follows the
     * GregorianJulian calendar rules with a cutover date.
     */
    public static BuddhistChronology getInstance() {
        return getInstance(DateTimeZone.getDefault());
    }

    /**
     * Standard instance of a Buddhist Chronology, that matches
     * Sun's BuddhistCalendar class. This means that it follows the
     * GregorianJulian calendar rules with a cutover date.
     *
     * @param zone  the time zone to use, null is default
     */
    public static synchronized BuddhistChronology getInstance(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        BuddhistChronology chrono = cCache.get(zone);
        if (chrono == null) {
            // First create without a lower limit.
            chrono = new BuddhistChronology(GJChronology.getInstance(zone, null), null);
            // Impose lower limit and make another BuddhistChronology.
            DateTime lowerLimit = new DateTime(1, 1, 1, 0, 0, 0, 0, chrono);
            chrono = new BuddhistChronology(LimitChronology.getInstance(chrono, lowerLimit, null), "");
            cCache.put(zone, chrono);
        }
        return chrono;
    }

    // Constructors and instance variables
    //-----------------------------------------------------------------------
    
    /**
     * Restricted constructor.
     *
     * @param param if non-null, then don't change the field set
     */
    private BuddhistChronology(Chronology base, Object param) {
        super(base, param);
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        Chronology base = getBase();
        return base == null ? getInstanceUTC() : getInstance(base.getZone());
    }

    // Conversion
    //-----------------------------------------------------------------------
    /**
     * Gets the Chronology in the UTC time zone.
     * 
     * @return the chronology in UTC
     */
    public Chronology withUTC() {
        return INSTANCE_UTC;
    }

    /**
     * Gets the Chronology in a specific time zone.
     * 
     * @param zone  the zone to get the chronology in, null is default
     * @return the chronology
     */
    public Chronology withZone(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        if (zone == getZone()) {
            return this;
        }
        return getInstance(zone);
    }

    /**
     * Checks if this chronology instance equals another.
     * 
     * @param obj  the object to compare to
     * @return true if equal
     * @since 1.6
     */
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * A suitable hash code for the chronology.
     * 
     * @return the hash code
     * @since 1.6
     */
    public int hashCode() {
        return "Buddhist".hashCode() * 11 + getZone().hashCode();
    }

    // Output
    //-----------------------------------------------------------------------
    /**
     * Gets a debugging toString.
     * 
     * @return a debugging string
     */
    public String toString() {
        String str = "BuddhistChronology";
        DateTimeZone zone = getZone();
        if (zone != null) {
            str = str + '[' + zone.getID() + ']';
        }
        return str;
    }

    protected void assemble(Fields fields) {
        if (getParam() == null) {
            // julian chrono removed zero, but we need to put it back
            DateTimeField field = fields.year;
            fields.year = new OffsetDateTimeField(
                    new SkipUndoDateTimeField(this, field), BUDDHIST_OFFSET);
            
            // one era, so yearOfEra is the same
            field = fields.yearOfEra;
            fields.yearOfEra = new DelegatedDateTimeField(
                fields.year, DateTimeFieldType.yearOfEra());
            
            // julian chrono removed zero, but we need to put it back
            field = fields.weekyear;
            fields.weekyear = new OffsetDateTimeField(
                    new SkipUndoDateTimeField(this, field), BUDDHIST_OFFSET);
            
            field = new OffsetDateTimeField(fields.yearOfEra, 99);
            fields.centuryOfEra = new DividedDateTimeField(
                field, DateTimeFieldType.centuryOfEra(), 100);
            
            field = new RemainderDateTimeField(
                (DividedDateTimeField) fields.centuryOfEra);
            fields.yearOfCentury = new OffsetDateTimeField(
                field, DateTimeFieldType.yearOfCentury(), 1);
            
            field = new RemainderDateTimeField(
                fields.weekyear, DateTimeFieldType.weekyearOfCentury(), 100);
            fields.weekyearOfCentury = new OffsetDateTimeField(
                field, DateTimeFieldType.weekyearOfCentury(), 1);
            
            fields.era = ERA_FIELD;
        }
    }
   
}
