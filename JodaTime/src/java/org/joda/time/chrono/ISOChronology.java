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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.field.DividedDateTimeField;
import org.joda.time.field.RemainderDateTimeField;

/**
 * ISOChronology provides access to the individual date time fields 
 * for the ISO8601 defined chronological calendar system. When ISO 
 * does not define a field, but it can be determined (such as AM/PM)
 * it is included.
 * <p>
 * ISOChronology is thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class ISOChronology extends AssembledChronology {
    
    static final long serialVersionUID = -6212696554273812441L;

    /** Singleton instance of a UTC ISOChronology */
    private static final ISOChronology INSTANCE_UTC =
        new ISOChronology(GregorianChronology.getInstanceUTC());
        
    private static final int FAST_CACHE_SIZE = 64;

    /** Fast cache of zone to chronology */
    private static final ISOChronology[] cFastCache = new ISOChronology[FAST_CACHE_SIZE];

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
    public static ISOChronology getInstance(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        int index = System.identityHashCode(zone) & (FAST_CACHE_SIZE - 1);
        ISOChronology chrono = cFastCache[index];
        if (chrono != null && chrono.getDateTimeZone() == zone) {
            return chrono;
        }
        synchronized (cCache) {
            chrono = (ISOChronology) cCache.get(zone);
            if (chrono == null) {
                chrono = new ISOChronology(ZonedChronology.getInstance(INSTANCE_UTC, zone));
                cCache.put(zone, chrono);
            }
        }
        cFastCache[index] = chrono;
        return chrono;
    }

    // Constructors and instance variables
    //-----------------------------------------------------------------------

    /**
     * Restricted constructor
     */
    private ISOChronology(Chronology base) {
        super(base, null);
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
        String str = "ISOChronology";
        DateTimeZone zone = getDateTimeZone();
        if (zone != null) {
            str = str + '[' + zone.getID() + ']';
        }
        return str;
    }

    protected void assemble(Fields fields) {
        if (getBase().getDateTimeZone() == DateTimeZone.UTC) {
            // Use zero based century and year of century.
            fields.centuryOfEra = new DividedDateTimeField
                (fields.yearOfEra, "centuryOfEra", "centuries", 100);
            fields.yearOfCentury = new RemainderDateTimeField
                ((DividedDateTimeField)fields.centuryOfEra, "yearOfCentury");

            fields.centuries = fields.centuryOfEra.getDurationField();
        }
    }

    /**
     * Serialize ISOChronology instances using a small stub. This reduces the
     * serialized size, and deserialized instances come from the cache.
     */
    private Object writeReplace() {
        return new Stub(getDateTimeZone());
    }

    private static final class Stub implements Serializable {
        static final long serialVersionUID = -6212696554273812441L;

        private transient DateTimeZone iZone;

        Stub(DateTimeZone zone) {
            iZone = zone;
        }

        private Object readResolve() {
            return ISOChronology.getInstance(iZone);
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeObject(iZone);
        }

        private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException
        {
            iZone = (DateTimeZone)in.readObject();
        }
    }

}
