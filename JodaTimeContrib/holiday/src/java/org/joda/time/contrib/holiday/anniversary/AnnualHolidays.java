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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * 
 * @author Al Major
 *
 * @param <T>
 * @param <AF>
 */
public abstract class AnnualHolidays<T extends Comparable, AF extends IAnniversaryFactory<? extends T>> {
    protected final List<AF> fHolidayFactories = new ArrayList<AF>();
    
    public AnnualHolidays() {
        buildFactories();
    }

    protected abstract void buildFactories ();

    protected void addFactory(AF af) {
        fHolidayFactories.add(af);
    }

    public List<T> getHolidaysForYear(int iYear) {
        List<T> result = new ArrayList<T>(fHolidayFactories.size());
        for (AF sdf : (List<AF>) fHolidayFactories) {
            result.add((T) sdf.create(iYear));
        }
        Collections.sort(result);
        return result;
    }
}
