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
import org.joda.time.DateTimeZone;

/**
 * InstantConverter defines how an object is converted to milliseconds/chronology.
 * <p>
 * The two methods in this interface must be called in order, as the
 * <code>getInstantMillis</code> method relies on the result of the
 * <code>getChronology</code> method being passed in.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public interface InstantConverter extends Converter {

    /**
     * Extracts the chronology from an object of this converter's type
     * where the time zone is specified.
     * 
     * @param object  the object to convert
     * @param zone  the specified zone to use, null means default zone
     * @return the chronology, never null
     * @throws ClassCastException if the object is invalid
     */
    Chronology getChronology(Object object, DateTimeZone zone);

    /**
     * Extracts the chronology from an object of this converter's type
     * where the chronology may be specified.
     * <p>
     * If the chronology is non-null it should be used. If it is null, then the
     * object should be queried, and if it has no chronology then ISO default is used.
     * 
     * @param object  the object to convert
     * @param chrono  the chronology to use, null means use object
     * @return the chronology, never null
     * @throws ClassCastException if the object is invalid
     */
    Chronology getChronology(Object object, Chronology chrono);

    //-----------------------------------------------------------------------
    /**
     * Extracts the millis from an object of this converter's type.
     * <p>
     * The chronology passed in is the result of the call to <code>getChronology</code>.
     * 
     * @param object  the object to convert
     * @param chrono  the chronology to use, which is the non-null result of getChronology()
     * @return the millisecond instant
     * @throws ClassCastException if the object is invalid
     * @throws IllegalArgumentException if object conversion fails
     */
    long getInstantMillis(Object object, Chronology chrono);

}
