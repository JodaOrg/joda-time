/*
 *  Copyright 2001-2018 Bishwash Adhikari
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

import java.util.Locale;

public class BikramSambatDateTimeField extends BasicMonthOfYearDateTimeField {

    private static String FULL_MONTHS[] = {
            "Baishak",
            "Jesth",
            "Ashar",
            "Shrawan",
            "Bhadra",
            "Ashoj",
            "Kartik",
            "Mangshir",
            "Poush",
            "Magh",
            "Falgun",
            "Chaitra"
    };

    private static String SHORT_MONTHS[] = {
            "BAI", "JES", "ASHA", "SHR", "BHA", "ASHO", "KAR", "MAN", "POU", "MAG", "FAL", "CHA"
    };

    /**
     * Restricted constructor.
     *
     * @param leapMonth the month of year that leaps
     */
    BikramSambatDateTimeField(BasicChronology chronology, int leapMonth) {
        super(chronology, leapMonth);
    }

    @Override
    public String getAsText(long instant, Locale locale) {
        String value = super.getAsText(instant, locale);
        return FULL_MONTHS[Integer.parseInt(value) - 1];
    }

    @Override
    public String getAsShortText(long instant, Locale locale) {
        String value = super.getAsText(instant, locale);
        return SHORT_MONTHS[Integer.parseInt(value) - 1];
    }
}
