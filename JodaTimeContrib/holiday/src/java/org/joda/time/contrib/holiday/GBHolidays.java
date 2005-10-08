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
import org.joda.time.contrib.holiday.anniversary.GBAnniversaries;
import org.joda.time.contrib.holiday.anniversary.NamedAnniversaryFactory;
/**
 * 
 * @author Al Major
 *
 */
public class GBHolidays {
    static AnnualHolidays<NamedAnniversaryFactory.NamedAnniversary, NamedAnniversaryFactory>
        ENGLISH_BANK = new AnnualHolidays<NamedAnniversaryFactory.NamedAnniversary, NamedAnniversaryFactory> () {
        protected void buildFactories () {
            addFactory(new NamedAnniversaryFactory("New Year's Day",
                    CommonAnniversaries.NEW_YEAR));
            addFactory(new NamedAnniversaryFactory("Good Friday",
                    CommonAnniversaries.GOOD_FRIDAY));
            addFactory(new NamedAnniversaryFactory("Easter Monday",
                    CommonAnniversaries.EASTER_MONDAY));
            addFactory(new NamedAnniversaryFactory("May Day Bank Holiday",
                    GBAnniversaries.MAY_DAY_BANK_HOLIDAY));
            addFactory(new NamedAnniversaryFactory("Spring Bank Holiday",
                    GBAnniversaries.SPRING_BANK_HOLIDAY));
            addFactory(new NamedAnniversaryFactory("Battle of the Boyne",
                    GBAnniversaries.JULY_12));
            addFactory(new NamedAnniversaryFactory("Summer Bank Holiday",
                    GBAnniversaries.SUMMER_BANK_HOLIDAY));
            addFactory(new NamedAnniversaryFactory("Christmas Day",
                    CommonAnniversaries.CHRISTMAS));
            addFactory(new NamedAnniversaryFactory("Boxing Day",
                    CommonAnniversaries.BOXING_DAY));
        }
    };
}
