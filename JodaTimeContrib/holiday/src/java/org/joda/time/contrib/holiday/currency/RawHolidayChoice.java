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
package org.joda.time.contrib.holiday.currency;

import java.util.List;

/**
 * The interface for specifying a raw holiday; the Saturday and Sunday behavior is not specified at this level.
 *
 * @author Scott R. Duchin
 */
public interface RawHolidayChoice extends Choice {

    /**
     * Returns the date of the particular holiday for the specified year.
     * Note: the holiday returned is pure; not adjusted for Saturday or Sunday local behavior.
     * @param year Year the holiday is requested for.
     * @return Date of holiday in specified year; <code>null</code> if no holiday in specified year.
     */
    Date date(/*year*/int year);

    /**
     * Returns a list of dates of the particular holiday for the specified years.
     * Note: the holiday returned is pure; not adjusted for Saturday or Sunday local behavior.
     * @param start Starting year.
     * @param end   Ending year.
     * @return List of dates of holidays in specified years, inclusive.
     */
    List<Date> dates(/*year*/int start, /*year*/int end);
}
