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
package org.joda.time.chrono.buddhist;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.DelegateChronology;
import org.joda.time.chrono.OffsetDateTimeField;
import org.joda.time.chrono.gj.GJChronology;

/**
 * <code>BuddhistChronology</code> provides access to the individual date
 * time fields for the Buddhist chronological calendar system.
 * <p>
 * The Buddhist calendar differs from the GregorianJulian calendar only 
 * in the year. This class is compatable with the BuddhistCalendar class 
 * supplied by Sun.
 * <p>
 * At present the century fields are unsupported.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class BuddhistChronology extends DelegateChronology {
    
    /**
     * Constant value for 'Buddhist Era', equivalent to the value returned
     * for AD/CE.
     */
    public static final int BE = DateTimeConstants.CE;

    /** Number of years difference in calendars. */
    private static final int BUDDHIST_OFFSET = 543;

    /** UTC instance of the chronology */
    private static final BuddhistChronology INSTANCE_UTC =
        new BuddhistChronology(GJChronology.getInstance(DateTimeZone.UTC, null, false));

    /** Cache of zone to chronology */
    private static final Map cCache = new HashMap();
    static {
        cCache.put(DateTimeZone.UTC, INSTANCE_UTC);
    }

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
     * @throws IllegalArgumentException if the zone is null
     */
    public static synchronized BuddhistChronology getInstance(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        BuddhistChronology chrono = (BuddhistChronology) cCache.get(zone);
        if (chrono == null) {
            chrono = new BuddhistChronology(GJChronology.getInstance(zone, null, false));
            cCache.put(zone, chrono);
        }
        return chrono;
    }
    
    // Constructors and instance variables
    //-----------------------------------------------------------------------
    // Fields are transient because readResolve will always return a cached instance.
    private transient DateTimeField iYearField;
    private transient DateTimeField iWeekyearField;
    
    /**
     * Restricted constructor.
     */
    private BuddhistChronology(GJChronology gjChronology) {
        super(gjChronology);
        DateTimeField field = gjChronology.year();
        iYearField = new OffsetDateTimeField(field.getName(), field, BUDDHIST_OFFSET);
        field = gjChronology.weekyear();
        iWeekyearField = new OffsetDateTimeField(field.getName(), field, BUDDHIST_OFFSET);
        // All other fields delegated to GJ
    }
    
    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return getInstance(getChronology().getDateTimeZone());
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
    public Chronology withDateTimeZone(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        if (zone == getDateTimeZone()) {
            return this;
        }
        return getInstance(zone);
    }

    // Week
    //-----------------------------------------------------------------------
    /**
     * Get the year of a week based year field for this chronology.
     *
     * @return DateTimeField
     */
    public DateTimeField weekyear() {
        return iWeekyearField;
    }

    // Year
    //-----------------------------------------------------------------------
    /**
     * Get the year field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField year() {
        // TODO block negative years
        return iYearField;
    }

    /**
     * Get the year of era field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField yearOfEra() {
        // TODO block negative years
        return iYearField;
    }

    /**
     * Get the year of century field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException always
     */
    public DateTimeField yearOfCentury() {
        // TODO
        throw new UnsupportedOperationException("yearOfCentury is unsupported for " + getClass().getName());
    }

    /**
     * Get the century of era field for this chronology.
     * 
     * @return DateTimeField
     * @throws UnsupportedOperationException always
     */
    public DateTimeField centuryOfEra() {
        // TODO
        throw new UnsupportedOperationException("centuryOfEra is unsupported for " + getClass().getName());
    }

    // Misc
    //-----------------------------------------------------------------------
    /**
     * Get the era field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField era() {
        return BuddhistEraDateTimeField.INSTANCE;
    }
    
    // Output
    //-----------------------------------------------------------------------
    /**
     * Gets a debugging toString.
     * 
     * @return a debugging string
     */
    public String toString() {
        DateTimeZone zone = getDateTimeZone();
        return "BuddhistChronology[" + (zone == null ? "" : zone.getID()) + "]";
    }
   
}
