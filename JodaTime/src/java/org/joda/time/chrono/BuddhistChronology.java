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
package org.joda.time.chrono;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.field.DividedDateTimeField;
import org.joda.time.field.OffsetDateTimeField;
import org.joda.time.field.RemainderDateTimeField;

/**
 * Implements the Buddhist calendar system, which is similar to Gregorian/Julian,
 * except with the year offset by 543.
 * <p>
 * The Buddhist calendar differs from the Gregorian/Julian calendar only 
 * in the year. This class is compatable with the BuddhistCalendar class 
 * supplied by Sun.
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
     * for AD/CE.
     */
    public static final int BE = DateTimeConstants.CE;

    /** Number of years difference in calendars. */
    private static final int BUDDHIST_OFFSET = 543;

    /** Cache of zone to chronology */
    private static final Map cCache = new HashMap();

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
        BuddhistChronology chrono = (BuddhistChronology) cCache.get(zone);
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
            DateTimeField field = fields.year;
            fields.year = new OffsetDateTimeField(field, BUDDHIST_OFFSET);
            
            field = fields.yearOfEra;
            fields.yearOfEra = new OffsetDateTimeField(
                fields.year, DateTimeFieldType.yearOfEra(), BUDDHIST_OFFSET);
            
            field = fields.weekyear;
            fields.weekyear = new OffsetDateTimeField(field, BUDDHIST_OFFSET);
            
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
            
            fields.era = BuddhistEraDateTimeField.INSTANCE;
        }
    }
   
}
