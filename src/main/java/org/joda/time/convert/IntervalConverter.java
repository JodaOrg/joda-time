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
import org.joda.time.ReadWritableInterval;

/**
 * IntervalConverter defines how an object is converted to an interval.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public interface IntervalConverter extends Converter {

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
    boolean isReadableInterval(Object object, Chronology chrono);

    /**
     * Extracts interval endpoint values from an object of this converter's
     * type, and sets them into the given ReadWritableInterval.
     *
     * @param writableInterval  interval to get modified, not null
     * @param object  the object to convert, must not be null
     * @param chrono  the chronology to use, may be null
     * @throws ClassCastException if the object is invalid
     */
    void setInto(ReadWritableInterval writableInterval, Object object, Chronology chrono);

}
