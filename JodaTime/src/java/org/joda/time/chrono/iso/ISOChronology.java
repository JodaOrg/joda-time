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
package org.joda.time.chrono.iso;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.DelegateChronology;
import org.joda.time.chrono.gj.GJChronology;

/**
 * ISOChronology provides access to the individual date time fields 
 * for the ISO8601 defined chronological calendar system. When ISO 
 * does not define a field, but it can be determined (such as AM/PM)
 * it is included.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class ISOChronology extends DelegateChronology {
    
    /** Singleton instance of a UTC ISOChronology */
    private static final ISOChronology INSTANCE_UTC =
        new ISOChronology(GJChronology.getInstance(DateTimeZone.UTC, Long.MIN_VALUE, true));
        
    /** Cache of zone to chronology */
    private static final Map cCache = new HashMap();
    static {
        cCache.put(DateTimeZone.UTC, INSTANCE_UTC);
    }

    /**
     * Gets an instance of the ISOChronology.
     * The time zone of the returned instance is UTC.
     * 
     * @return a singleton UTC instance of the chronology
     */
    public static ISOChronology getInstanceUTC() {
        return INSTANCE_UTC;
    }

    /**
     * Gets an instance of the ISOChronology in the default time zone.
     * 
     * @return a chronology in the default time zone
     */
    public static ISOChronology getInstance() {
        return getInstance(DateTimeZone.getDefault());
    }

    /**
     * Gets an instance of the ISOChronology in the given time zone.
     * 
     * @param zone  the time zone to get the chronology in, null is default
     * @return a chronology in the specified time zone
     */
    public static synchronized ISOChronology getInstance(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        ISOChronology chrono = (ISOChronology) cCache.get(zone);
        if (chrono == null) {
            chrono = new ISOChronology(GJChronology.getInstance(zone, Long.MIN_VALUE, true));
            cCache.put(zone, chrono);
        }
        return chrono;
    }

    // Constructors and instance variables
    //-----------------------------------------------------------------------
    /**
     * Restricted constructor
     */
    private ISOChronology(GJChronology gjChronology) {
        super(gjChronology);
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

    // Output
    //-----------------------------------------------------------------------
    /**
     * Gets a debugging toString.
     * 
     * @return a debugging string
     */
    public String toString() {
        DateTimeZone zone = getDateTimeZone();
        return "ISOChronology[" + (zone == null ? "" : zone.getID()) + "]";
    }
   
}
