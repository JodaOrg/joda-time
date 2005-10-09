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
package org.joda.time.contrib.holiday;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.YearMonthDay;

/**
 * Holiday defines a specially named day and the rules for creating it.
 * The named day may be a holiday, anniversary or other day with
 * particular meaning.
 *
 * @author Al Major
 * @author Stephen Colebourne
 * @version $Id$
 */
public class Holiday {

    /** The id of the holiday. */
    private String iId;
    /** The name of the holiday. */
    private Map iNames;
    /** The list of HolidayRule objects. */
    private List iHolidayRules = new ArrayList();

    /**
     * Constructor.
     */
    Holiday() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the ID of the holiday.
     *
     * @return the id of the holiday
     */
    public String getID() {
        return iId;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the name of the holiday.
     * This method supports intelligent name lookup by locale.
     *
     * @param locale  the locale to get the name in
     * @return the name of the holiday
     */
    public String getName(Locale locale) {
        String localeStr = locale.toString();
        String name = (String) iNames.get(localeStr);
        if (name == null && locale.getVariant().length() > 0) {
            name = (String) iNames.get(localeStr.substring(0, 5));
        }
        if (name == null && locale.getCountry().length() > 0) {
            name = (String) iNames.get(localeStr.substring(0, 2));
        }
        if (name == null) {
            name = (String) iNames.get("en");
        }
        if (name == null && iNames.size() > 0) {
            name = (String) iNames.values().iterator().next();
        }
        return name;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the YearMonthDay object for the specified year.
     *
     * @param year  the year
     * @return the YearMonthDay, null if day does not exist for this year
     */
    public YearMonthDay yearMonthDayForYear(int year) {
        MutableDateTime mdt = new MutableDateTime(0L, DateTimeZone.UTC);
        for (int i = 0; i < iHolidayRules.size(); i++) {
            HolidayRule rule = (HolidayRule) iHolidayRules.get(i);
            if (rule.appliesForYear(mdt, year)) {
                return new YearMonthDay(rule.applyForYear(mdt, year));
            }
        }
        return null;
    }

}
