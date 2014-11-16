/*
 *  Copyright 2001-2009 Stephen Colebourne
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
package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.ReadableDuration;

/**
 * ReadableDurationConverter extracts milliseconds and chronology from a ReadableDuration.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
class ReadableDurationConverter extends AbstractConverter
        implements DurationConverter, PeriodConverter {

    /**
     * Singleton instance.
     */
    static final ReadableDurationConverter INSTANCE = new ReadableDurationConverter();

    /**
     * Restricted constructor.
     */
    protected ReadableDurationConverter() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * Extracts the millis from an object of this converter's type.
     * 
     * @param object  the object to convert, must not be null
     * @return the millisecond value
     * @throws NullPointerException if the object is null
     * @throws ClassCastException if the object is an invalid type
     * @throws IllegalArgumentException if the object is invalid
     */
    public long getDurationMillis(Object object) {
        return ((ReadableDuration) object).getMillis();
    }

    //-----------------------------------------------------------------------
    /**
     * Extracts duration values from an object of this converter's type, and
     * sets them into the given ReadWritableDuration.
     *
     * @param writablePeriod  period to get modified
     * @param object  the object to convert, must not be null
     * @param chrono  the chronology to use, must not be null
     * @throws NullPointerException if the duration or object is null
     * @throws ClassCastException if the object is an invalid type
     * @throws IllegalArgumentException if the object is invalid
     */
    public void setInto(ReadWritablePeriod writablePeriod, Object object, Chronology chrono) {
        ReadableDuration dur = (ReadableDuration) object;
        chrono = DateTimeUtils.getChronology(chrono);
        long duration = dur.getMillis();
        int[] values = chrono.get(writablePeriod, duration);
        for (int i = 0; i < values.length; i++) {
            writablePeriod.setValue(i, values[i]);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Returns ReadableDuration.class.
     * 
     * @return ReadableDuration.class
     */
    public Class<?> getSupportedType() {
        return ReadableDuration.class;
    }

}
