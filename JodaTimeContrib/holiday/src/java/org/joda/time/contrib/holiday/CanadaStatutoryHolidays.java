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

import org.joda.time.contrib.holiday.anniversary.AnnualHolidays;
import org.joda.time.contrib.holiday.anniversary.CanadaAnniversaries;
import org.joda.time.contrib.holiday.anniversary.CommonAnniversaries;
import org.joda.time.contrib.holiday.anniversary.NamedAnniversaryFactory;

/**
 * 
 * @author Al Major
 *
 */
public class CanadaStatutoryHolidays {
    static AnnualHolidays<NamedAnniversaryFactory.NamedAnniversary, NamedAnniversaryFactory>
    FEDERAL = new AnnualHolidays<NamedAnniversaryFactory.NamedAnniversary, NamedAnniversaryFactory> () {
        protected void buildFactories () {
            addFactory(new NamedAnniversaryFactory("New Year's Day",
                    CanadaAnniversaries.NEW_YEAR_HOLIDAY));
            addFactory(new NamedAnniversaryFactory("Good Friday",
                    CommonAnniversaries.GOOD_FRIDAY));
            addFactory(new NamedAnniversaryFactory("Victoria Day",
                    CanadaAnniversaries.VICTORIA_DAY));
            addFactory(new NamedAnniversaryFactory("Canada Day",
                    CanadaAnniversaries.CANADA_DAY_HOLIDAY));
            addFactory(new NamedAnniversaryFactory("Labour Day",
                    CommonAnniversaries.LABOR_DAY));
            addFactory(new NamedAnniversaryFactory("Thanksgiving Day",
                    CanadaAnniversaries.THANKSGIVING));
            addFactory(new NamedAnniversaryFactory("Remembrance Day",
                    CanadaAnniversaries.REMEMBRANCE_DAY_HOLIDAY));
            addFactory(new NamedAnniversaryFactory("Christmas Day",
                    CanadaAnniversaries.CHRISTMAS_HOLIDAY));
            addFactory(new NamedAnniversaryFactory("Boxing Day",
                    CanadaAnniversaries.BOXING_DAY_HOLIDAY));
        }
    };
}
