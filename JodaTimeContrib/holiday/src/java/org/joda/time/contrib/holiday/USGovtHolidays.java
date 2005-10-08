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
import org.joda.time.contrib.holiday.anniversary.CommonAnniversaries;
import org.joda.time.contrib.holiday.anniversary.NamedAnniversaryFactory;
import org.joda.time.contrib.holiday.anniversary.USAnniversaries;
/**
 * 
 * @author Al Major
 *
 */
public class USGovtHolidays {
    static AnnualHolidays<NamedAnniversaryFactory.NamedAnniversary, NamedAnniversaryFactory>
        FEDERAL = new AnnualHolidays<NamedAnniversaryFactory.NamedAnniversary, NamedAnniversaryFactory> () {
        protected void buildFactories () {
            addFactory(new NamedAnniversaryFactory("New Year's Day",
                    USAnniversaries.NEW_YEAR_HOLIDAY));
            addFactory(new NamedAnniversaryFactory(
                    "Birthday of Martin Luther King, Jr.",
                    USAnniversaries.MARTIN_LUTHER_KING_DAY));
            addFactory(new NamedAnniversaryFactory("Washington's Birthday",
                    USAnniversaries.WASHINGTONS_BIRTHDAY_HOLIDAY));
            addFactory(new NamedAnniversaryFactory("Memorial Day",
                    USAnniversaries.MEMORIAL_DAY));
            addFactory(new NamedAnniversaryFactory("Independence Day",
                    USAnniversaries.INDEPENDENCE_DAY_HOLIDAY));
            addFactory(new NamedAnniversaryFactory("Labor Day",
                    CommonAnniversaries.LABOR_DAY));
            addFactory(new NamedAnniversaryFactory("Columbus Day",
                    USAnniversaries.COLUMBUS_DAY));
            addFactory(new NamedAnniversaryFactory("Veterans Day",
                    USAnniversaries.VETERANS_DAY_HOLIDAY));
            addFactory(new NamedAnniversaryFactory("Thanksgiving Day",
                    USAnniversaries.THANKSGIVING));
            addFactory(new NamedAnniversaryFactory("Christmas Day",
                    USAnniversaries.CHRISTMAS_HOLIDAY));
        }
    };
}
