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
import org.joda.time.ReadWritableInterval;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.ReadableInterval;

/**
 * Converts intervals into durations of any requested period type.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
class ReadableIntervalConverter extends AbstractConverter
        implements IntervalConverter, DurationConverter, PeriodConverter {

    /**
     * Singleton instance.
     */
    static final ReadableIntervalConverter INSTANCE = new ReadableIntervalConverter();

    /**
     * Restricted constructor.
     */
    protected ReadableIntervalConverter() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the millisecond length of the interval.
     * 
     * @param object  the interval
     */
    public long getDurationMillis(Object object) {
        return (((ReadableInterval) object)).toDurationMillis();
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the values of the mutable duration from the specified interval.
     * 
     * @param writablePeriod  the period to modify
     * @param object  the interval to set from
     * @param chrono  the chronology to use
     */
    public void setInto(ReadWritablePeriod writablePeriod, Object object, Chronology chrono) {
        ReadableInterval interval = (ReadableInterval) object;
        chrono = (chrono != null ? chrono : DateTimeUtils.getIntervalChronology(interval));
        long start = interval.getStartMillis();
        long end = interval.getEndMillis();
        int[] values = chrono.get(writablePeriod, start, end);
        for (int i = 0; i < values.length; i++) {
            writablePeriod.setValue(i, values[i]);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Checks if the input is a ReadableInterval.
     * <p>
     * If it is, then the calling code should cast and copy the fields directly.
     *
     * @param object  the object to convert, must not be null
     * @param chrono  the chronology to use, may be null
     * @return true if the input is a ReadableInterval
     * @throws ClassCastException if the object is invalid
     */
    @Override
    public boolean isReadableInterval(Object object, Chronology chrono) {
        return true;
    }

    /**
     * Extracts interval endpoint values from an object of this converter's
     * type, and sets them into the given ReadWritableInterval.
     *
     * @param writableInterval interval to get modified, not null
     * @param object  the object to convert, must not be null
     * @param chrono  the chronology to use, may be null
     * @throws ClassCastException if the object is invalid
     */
    public void setInto(ReadWritableInterval writableInterval, Object object, Chronology chrono) {
        ReadableInterval input = (ReadableInterval) object;
        writableInterval.setInterval(input);
        if (chrono != null) {
            writableInterval.setChronology(chrono);
        } else {
            writableInterval.setChronology(input.getChronology());
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Returns ReadableInterval.class.
     */
    public Class<?> getSupportedType() {
        return ReadableInterval.class;
    }

}
