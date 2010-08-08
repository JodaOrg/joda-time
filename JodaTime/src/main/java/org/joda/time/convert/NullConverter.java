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
package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.Period;
import org.joda.time.ReadWritableInterval;
import org.joda.time.ReadWritablePeriod;

/**
 * NullConverter converts null to an instant, partial, duration, period
 * or interval. Null means now for instant/partial, zero for duration/period
 * and from now to now for interval.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
class NullConverter extends AbstractConverter
        implements InstantConverter, PartialConverter, DurationConverter, PeriodConverter, IntervalConverter {

    /**
     * Singleton instance.
     */
    static final NullConverter INSTANCE = new NullConverter();

    /**
     * Restricted constructor.
     */
    protected NullConverter() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the millisecond duration, which is zero.
     * 
     * @param object  the object to convert, which is null
     * @return the millisecond duration
     */
    public long getDurationMillis(Object object) {
        return 0L;
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the given ReadWritableDuration to zero milliseconds.
     *
     * @param duration duration to get modified
     * @param object  the object to convert, which is null
     * @param chrono  the chronology to use
     * @throws NullPointerException if the duration is null
     */
    public void setInto(ReadWritablePeriod duration, Object object, Chronology chrono) {
        duration.setPeriod((Period) null);
    }

    //-----------------------------------------------------------------------
    /**
     * Extracts interval endpoint values from an object of this converter's
     * type, and sets them into the given ReadWritableInterval.
     *
     * @param writableInterval interval to get modified, not null
     * @param object  the object to convert, which is null
     * @param chrono  the chronology to use, may be null
     * @throws NullPointerException if the interval is null
     */
    public void setInto(ReadWritableInterval writableInterval, Object object, Chronology chrono) {
        writableInterval.setChronology(chrono);
        long now = DateTimeUtils.currentTimeMillis();
        writableInterval.setInterval(now, now);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns null.
     * 
     * @return null
     */
    public Class getSupportedType() {
        return null;
    }

}
