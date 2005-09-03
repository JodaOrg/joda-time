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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;

/**
 * Identifies a type of chronology, which represents the calendar system.
 * <p>
 * Various chronologies are supported by Joda-Time, including ISO and
 * GregorianJulian. This class defines static methods to obtain the
 * different kinds of chronology that are supported. Each instance then
 * defines methods to obtain a Chronology by specifying the time zone.
 * For example, to obtain the coptic chronology in the default zone:
 * <pre>
 * Chronology c = ChronologyType.coptic().getChronology();
 * </pre>
 * <p>
 * The provided chronology types are:
 * <ul>
 * <li>ISO - Based on the ISO8601 standard and suitable for use after about 1600
 * <li>GJ - Historically accurate calendar with Julian followed by Gregorian
 * <li>Gregorian - The Gregorian calendar system used for all time (proleptic)
 * <li>Julian - The Julian calendar system used for all time (proleptic)
 * <li>Buddhist - The Buddhist calendar system which is an offset in years from GJ
 * <li>Coptic - The Coptic calendar system which defines 30 day months
 * </ul>
 * Hopefully future releases will contain more chronologies.
 *
 * @see org.joda.time.chrono.ISOChronology
 * @see org.joda.time.chrono.GJChronology
 * @see org.joda.time.chrono.GregorianChronology
 * @see org.joda.time.chrono.JulianChronology
 * @see org.joda.time.chrono.CopticChronology
 * @see org.joda.time.chrono.BuddhistChronology
 *
 * @author Stephen Colebourne
 * @since 1.2
 */
public abstract class ChronologyType implements Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 83923682285389L;

    /** The map of id to type. */
    private static final Map cTypes = Collections.synchronizedMap(new HashMap());

    /** Ordinal values for standard chronology types. */
    static final byte  // do not change - values matter for serialization
        ISO = 1,
        GJ = 2,
        GREGORIAN = 3,
        JULIAN = 4,
        BUDDHIST = 5,
        COPTIC = 6;

    /** The iso chronology type. */
    private static final ChronologyType ISO_TYPE = new StandardChronologyType("ISO", ISO);
    /** The iso chronology type. */
    private static final ChronologyType GJ_TYPE = new StandardChronologyType("GJ", GJ);
    /** The iso chronology type. */
    private static final ChronologyType GREGORIAN_TYPE = new StandardChronologyType("Gregorian", GREGORIAN);
    /** The iso chronology type. */
    private static final ChronologyType JULIAN_TYPE = new StandardChronologyType("Julian", JULIAN);
    /** The iso chronology type. */
    private static final ChronologyType BUDDHIST_TYPE = new StandardChronologyType("Buddhist", BUDDHIST);
    /** The iso chronology type. */
    private static final ChronologyType COPTIC_TYPE = new StandardChronologyType("Coptic", COPTIC);

    /** The id of the field. */
    private final String iID;

    //-----------------------------------------------------------------------
    /**
     * Constructor.
     * 
     * @param id  the id to use
     */
    protected ChronologyType(String id) {
        super();
        iID = id;
        cTypes.put(id, this);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a ChronologyType by id.
     * <p>
     * Note that this method will work with your own subclasses of
     * ChronologyType, so long as an instance of the subclass has been
     * class-loaded.
     *
     * @param id  the id to get
     * @return the chronology type, null if no known chronology
     */
    public static ChronologyType forID(String id) {
        return (ChronologyType) cTypes.get(id);
    }

    /**
     * Gets all the available ids supported.
     * 
     * @return an unmodifiable Set of String ids
     */
    public static Set getAvailableIDs() {
        return Collections.unmodifiableSet(cTypes.keySet());
    }

    //-----------------------------------------------------------------------
    /**
     * Get the ISO chronology type.
     * <p>
     * {@link ISOChronology} defines all fields in line with the ISO8601 standard.
     * This chronology is the default, and is suitable for all normal datetime processing.
     * It is <i>unsuitable</i> for historical datetimes before October 15, 1582
     * as it applies the modern Gregorian calendar rules before that date.
     *
     * @return the ChronologyType constant
     */
    public static ChronologyType iso() {
        return ISO_TYPE;
    }

    /**
     * Get the GJ chronology type.
     * <p>
     * {@link GJChronology} defines all fields using standard meanings.
     * This chronology is intended to be used as a replacement for <code>GregorianCalendar</code>.
     * The Gregorian calendar system is used after October 15, 1582, while the
     * Julian calendar system is used before.
     * <p>
     * Unlike <code>GregorianCalendar</code>, this chronology returns a year of -1
     * for 1 BCE, -2 for 2 BCE and so on. Thus there is no year zero.
     * <p>
     * This method uses the standard Julian to Gregorian cutover date of
     * October 15th 1582. If you require a cutover on a different date, then use
     * the factory methods on <code>GJChronology</code> itself.
     * <p>
     * When dealing solely with dates in the modern era, from 1600 onwards,
     * we recommend using ISOChronology, which is the default.
     *
     * @return the ChronologyType constant
     */
    public static ChronologyType gj() {
        return GJ_TYPE;
    }

    /**
     * Get the Gregorian chronology type.
     * <p>
     * {@link GregorianChronology} defines all fields using standard meanings.
     * It uses the Gregorian calendar rules <i>for all time</i> (proleptic)
     * thus it is NOT a replacement for <code>GregorianCalendar</code>.
     * For that purpose, you should use {@link #gj()}.
     * <p>
     * The Gregorian calendar system defines a leap year every four years,
     * except that every 100 years is not leap, but every 400 is leap.
     * <p>
     * Technically, this chronology is almost identical to the ISO chronology,
     * thus we recommend using ISOChronology instead, which is the default.
     *
     * @return the ChronologyType constant
     */
    public static ChronologyType gregorian() {
        return GREGORIAN_TYPE;
    }

    /**
     * Get the Julian chronology type.
     * <p>
     * {@link JulianChronology} defines all fields using standard meanings.
     * It uses the Julian calendar rules <i>for all time</i> (proleptic).
     * The Julian calendar system defines a leap year every four years.
     *
     * @return the ChronologyType constant
     */
    public static ChronologyType julian() {
        return JULIAN_TYPE;
    }

    /**
     * Get the Buddhist chronology type.
     * <p>
     * {@link BuddhistChronology} defines all fields using standard meanings,
     * however the year is offset by 543. The chronology cannot be used before
     * year 1 in the Buddhist calendar.
     *
     * @return the ChronologyType constant
     */
    public static ChronologyType buddhist() {
        return BUDDHIST_TYPE;
    }

    /**
     * Get the Coptic chronology type.
     * <p>
     * {@link CopticChronology} defines fields sensibly for the Coptic calendar system.
     * The Coptic calendar system defines every fourth year as leap.
     * The year is broken down into 12 months, each 30 days in length.
     * An extra period at the end of the year is either 5 or 6 days in length
     * and is returned as a 13th month.
     * Year 1 in the Coptic calendar began on August 29, 284 CE (Julian).
     * The chronology cannot be used before the first Coptic year.
     *
     * @return the ChronologyType constant
     */
    public static ChronologyType coptic() {
        return COPTIC_TYPE;
    }

    //-----------------------------------------------------------------------
    /**
     * Get the id of the chronology.
     * 
     * @return the id of the chronology
     */
    public String getID() {
        return iID;
    }

    /**
     * Get an instance of the Chronology in the default time zone.
     * 
     * @return the Chronology
     */
    public Chronology getChronology() {
        return getChronology(null);
    }

    /**
     * Get an instance of the Chronology in the UTC time zone.
     * 
     * @return the Chronology
     */
    public Chronology getChronologyUTC() {
        return getChronology(DateTimeZone.UTC);
    }

    /**
     * Get an instance of the Chronology in the specified time zone.
     * 
     * @param zone  the time zone, null means default
     */
    public abstract Chronology getChronology(DateTimeZone zone);

    /**
     * Gets the chronology as a string, which is simply its id.
     *
     * @return the id of the chronology
     */
    public String toString() {
        return getID();
    }

    //-----------------------------------------------------------------------
    private static class StandardChronologyType extends ChronologyType {
        /** Serialization version */
        private static final long serialVersionUID = -562289176239L;

        /** The ordinal of the standard field type, for switch statements */
        private final byte iOrdinal;

        /**
         * Constructor.
         */
        StandardChronologyType(String id, byte ordinal) {
            super(id);
            iOrdinal = ordinal;
        }

        /** @inheritdoc */
        public Chronology getChronology(DateTimeZone zone) {
            switch (iOrdinal) {
                case ISO:
                    return ISOChronology.getInstance(zone);
                case GJ:
                    return GJChronology.getInstance(zone);
                case GREGORIAN:
                    return GregorianChronology.getInstance(zone);
                case JULIAN:
                    return JulianChronology.getInstance(zone);
                case BUDDHIST:
                    return BuddhistChronology.getInstance(zone);
                case COPTIC:
                    return CopticChronology.getInstance(zone);
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
        	return (ChronologyType) forID(getID());
        }
    }

}
