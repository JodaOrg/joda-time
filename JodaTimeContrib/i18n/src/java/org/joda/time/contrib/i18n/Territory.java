/*
 *  Copyright 2006 Stephen Colebourne
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
package org.joda.time.contrib.i18n;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTimeZone;

/**
 * Provides time data for a specific territory, typically a country.
 * <p>
 * Many pieces of data used in dates and times varies by territory.
 * This class provides access to that data.
 */
public abstract class Territory {

//    /** An empty chronology array. */
//    private static final Chronology[] EMPTY_CHRONOLOGY_ARRAY = new Chronology[0];
    /** A cache of territories. */
    private static final Map cTerritoryMap = Collections.synchronizedMap(new HashMap());

    //-----------------------------------------------------------------------
    /**
     * Gets a territory instance for the specified id.
     * <p>
     * The territory id must be one of those returned by getAvailableIDs.
     *
     * @param id  the ID of the territory, not null
     * @return the territory object for the ID
     * @throws IllegalArgumentException if the ID is not recognised
     */
    public static Territory forID(String id) {
        if (id != null && id.length() == 2) {
            Territory t = (Territory) cTerritoryMap.get(id);
            if (t == null) {
                t = new CLDRTerritory(id);
                cTerritoryMap.put(id, t);
            }
            return t;
        }
        throw new IllegalArgumentException("The territory id is not recognised: " + id);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor.
     *
     * @param id  the territory id, not null
     */
    protected Territory() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the territory id.
     *
     * @return the territory id, never null
     */
    public abstract String getID();

    //-----------------------------------------------------------------------
    /**
     * Gets the time zones applicable for the territory.
     *
     * @return the array of zones, never null
     */
	public abstract DateTimeZone[] getZones();

    /**
     * Gets the time zone for the territory, selecting the zone of the most
     * important city (the capital) if there are multiple zones.
     *
     * @return the zone that best represents the territory, null if unknown
     */
    public DateTimeZone getZone() {
        DateTimeZone[] zones = getZones();
        if (zones.length == 0) {
            return null;
        }
        return zones[0];
    }

//    //-----------------------------------------------------------------------
//    /**
//     * Gets the altenate non-ISO chronologies used in the territory.
//     * For example, countries in the middle east would include
//     * IslamicChronology in the result.
//     *
//     * @return the non-ISO chronologies, empty array if none
//     */
//    public Chronology[] getChronologies() {
//        return EMPTY_CHRONOLOGY_ARRAY;
//    }

    //-----------------------------------------------------------------------
    /**
     * Gets the first day of the week.
     * The value is expressed using ISO values (1=Mon,7=Sun).
     *
     * @return the first day of the week
     */
    public abstract int getFirstDayOfWeek();

    /**
     * Gets the day of week that the business week starts.
     * The value is expressed using ISO values (1=Mon,7=Sun).
     *
     * @return the day of week that the business week starts
     */
    public abstract int getBusinessWeekStart();

    /**
     * Gets the day of week that the business week ends.
     * The value is expressed using ISO values (1=Mon,7=Sun).
     *
     * @return the day of week that the business week ends
     */
    public abstract int getBusinessWeekEnd();

    /**
     * Gets the day of week that the weekend starts.
     * The value is expressed using ISO values (1=Mon,7=Sun).
     *
     * @return the day of week that the weekend starts
     */
    public abstract int getWeekendStart();

    /**
     * Gets the day of week that the weekend ends.
     * The value is expressed using ISO values (1=Mon,7=Sun).
     *
     * @return the day of week that the weekend ends
     */
    public abstract int getWeekendEnd();

    //-----------------------------------------------------------------------
    /**
     * Is this territory equal (by id and class) to another.
     *
     * @param other  the other object to compare to
     * @return trur if equal
     */
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other.getClass() != getClass()) {
            return false;
        }
        return ((Territory) other).getID().equals(getID());
    }

    /**
     * Gets a suitable hashcode for this territory.
     *
     * @return a hashcode
     */
    public int hashCode() {
        return 19 * getClass().hashCode() + getID().hashCode();
    }

    /**
     * Outputs a string vesion of the territory.
     *
     * @return string
     */
    public String toString() {
        return "Territory[" + getID() + "]";
    }

}
