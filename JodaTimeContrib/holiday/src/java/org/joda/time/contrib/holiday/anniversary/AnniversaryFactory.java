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
package org.joda.time.contrib.holiday.anniversary;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
/**
 * 
 * @author Al Major
 *
 */
public abstract class AnniversaryFactory implements
        IAnniversaryFactory<DateTime> {
    private final String holidayID;

    protected AnniversaryFactory(String str, Chronology c) {
        if (c != Chronology.getISO()) {
            throw new IllegalArgumentException(
                    "AnniversaryFactory(String, Chronology): requires Gregorian/UTC chronologies only");
        }
        this.holidayID = str;
    }

    protected AnniversaryFactory(String str) {
        this.holidayID = str;
    }

    public abstract DateTime create(int iYear);

    public String getId() {
        return holidayID;
    }
}
