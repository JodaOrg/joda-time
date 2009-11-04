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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.field.DividedDateTimeField;
import org.joda.time.field.RemainderDateTimeField;

/**
 * Implements a chronology that follows the rules of the ISO8601 standard,
 * which is compatible with Gregorian for all modern dates.
 * When ISO does not define a field, but it can be determined (such as AM/PM)
 * it is included.
 * <p>
 * With the exception of century related fields, ISOChronology is exactly the
 * same as {@link GregorianChronology}. In this chronology, centuries and year
 * of century are zero based. For all years, the century is determined by
 * dropping the last two digits of the year, ignoring sign. The year of century
 * is the value of the last two year digits.
 * <p>
 * ISOChronology is thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class ISOChronology extends AssembledChronology {
    
    /** Serialization lock */
    private static final long serialVersionUID = -6212696554273812441L;

    /** Singleton instance of a UTC ISOChronology */
    private static final ISOChronology INSTANCE_UTC;
        
    private static final int FAST_CACHE_SIZE = 64;

    /** Fast cache of zone to chronology */
    private static final ISOChronology[] cFastCache;

    /** Cache of zone to chronology */
    private static final Map<DateTimeZone, ISOChronology> cCache = new HashMap<DateTimeZone, ISOChronology>();
    static {
        cFastCache = new ISOChronology[FAST_CACHE_SIZE];
        INSTANCE_UTC = new ISOChronology(GregorianChronology.getInstanceUTC());
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
        if (chrono != null && chrono.getZone() == zone) {
            return chrono;
        }
        synchronized (cCache) {
            chrono = cCache.get(zone);
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
        String str = "ISOChronology";
        DateTimeZone zone = getZone();
        if (zone != null) {
            str = str + '[' + zone.getID() + ']';
        }
        return str;
    }

    protected void assemble(Fields fields) {
        if (getBase().getZone() == DateTimeZone.UTC) {
            // Use zero based century and year of century.
            fields.centuryOfEra = new DividedDateTimeField(
                ISOYearOfEraDateTimeField.INSTANCE, DateTimeFieldType.centuryOfEra(), 100);
            fields.yearOfCentury = new RemainderDateTimeField(
                (DividedDateTimeField) fields.centuryOfEra, DateTimeFieldType.yearOfCentury());
            fields.weekyearOfCentury = new RemainderDateTimeField(
                (DividedDateTimeField) fields.centuryOfEra, DateTimeFieldType.weekyearOfCentury());

            fields.centuries = fields.centuryOfEra.getDurationField();
        }
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
        return "ISO".hashCode() * 11 + getZone().hashCode();
    }

    /**
     * Serialize ISOChronology instances using a small stub. This reduces the
     * serialized size, and deserialized instances come from the cache.
     */
    private Object writeReplace() {
        return new Stub(getZone());
    }

    private static final class Stub implements Serializable {
        private static final long serialVersionUID = -6212696554273812441L;

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
