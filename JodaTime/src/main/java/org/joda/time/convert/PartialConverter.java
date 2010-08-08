/*
 *  Copyright 2001-2006 Stephen Colebourne
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
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormatter;

/**
 * PartialConverter defines how an object is converted to a ReadablePartial.
 * <p>
 * The two methods in this interface must be called in order, as the
 * <code>getPartialValues</code> method relies on the result of the
 * <code>getChronology</code> method being passed in.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public interface PartialConverter extends Converter {

    /**
     * Extracts the chronology from an object of this converter's type
     * where the time zone is specified.
     * 
     * @param object  the object to convert
     * @param zone  the specified zone to use, null means default zone
     * @return the chronology, never null
     * @throws ClassCastException if the object is invalid
     * @since 1.3
     */
    Chronology getChronology(Object object, DateTimeZone zone);

    /**
     * Extracts the chronology from an object of this converter's type
     * where the chronology is specified.
     * 
     * @param object  the object to convert
     * @param chrono  the chronology to use, null usually means ISO
     * @return the chronology, not converted to UTC/local time zone, must be non-null valid
     * @throws ClassCastException if the object is invalid
     */
    Chronology getChronology(Object object, Chronology chrono);

    /**
     * Extracts the values of the partial from an object of this converter's type.
     * The chrono parameter is a hint to the converter, should it require a
     * chronology to aid in conversion.
     * 
     * @param fieldSource  a partial that provides access to the fields.
     *  This partial may be incomplete and only getFieldType(int) should be used
     * @param object  the object to convert
     * @param chrono  the chronology to use, which is the non-null result of getChronology()
     * @return the array of field values that match the fieldSource, must be non-null valid
     * @throws ClassCastException if the object is invalid
     */
    int[] getPartialValues(ReadablePartial fieldSource, Object object, Chronology chrono);

    /**
     * Extracts the values of the partial from an object of this converter's type.
     * The chrono parameter is a hint to the converter, should it require a
     * chronology to aid in conversion.
     * 
     * @param fieldSource  a partial that provides access to the fields.
     *  This partial may be incomplete and only getFieldType(int) should be used
     * @param object  the object to convert
     * @param chrono  the chronology to use, which is the non-null result of getChronology()
     * @param parser  if converting from a String, the given parser is preferred
     * @return the array of field values that match the fieldSource, must be non-null valid
     * @throws ClassCastException if the object is invalid
     * @since 1.3
     */
    int[] getPartialValues(ReadablePartial fieldSource, Object object, Chronology chrono,
                           DateTimeFormatter parser);

}
