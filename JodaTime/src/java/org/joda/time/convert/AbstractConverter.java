/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2004 Stephen Colebourne.  
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Joda project (http://www.joda.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "Joda" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact licence@joda.org.
 *
 * 5. Products derived from this software may not be called "Joda",
 *    nor may "Joda" appear in their name, without prior written
 *    permission of the Joda project.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE JODA AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Joda project and was originally 
 * created by Stephen Colebourne <scolebourne@joda.org>. For more
 * information on the Joda project, please see <http://www.joda.org/>.
 */
package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.PeriodType;
import org.joda.time.ReadablePartial;

/**
 * AbstractConverter simplifies the process of implementing a converter.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public abstract class AbstractConverter implements Converter {

    /**
     * Restricted constructor.
     */
    protected AbstractConverter() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * Extracts the millis from an object of this convertor's type.
     * <p>
     * This implementation returns the current time.
     * 
     * @param object  the object to convert
     * @param chrono  the chronology to use, which is always non-null
     * @return the millisecond value
     */
    public long getInstantMillis(Object object, Chronology chrono) {
        return DateTimeUtils.currentTimeMillis();
    }

    //-----------------------------------------------------------------------
    /**
     * Extracts the chronology from an object of this convertor's type
     * where the time zone is specified.
     * <p>
     * This implementation returns the ISO chronology.
     * 
     * @param object  the object to convert
     * @param zone  the specified zone to use, null means default zone
     * @return the chronology, never null
     */
    public Chronology getChronology(Object object, DateTimeZone zone) {
        return Chronology.getISO(zone);
    }

    /**
     * Extracts the chronology from an object of this convertor's type
     * where the chronology is specified.
     * <p>
     * This implementation returns the chronology specified, or the
     * ISO chronology in the default zone if null passed in.
     * 
     * @param object  the object to convert
     * @param chrono  the chronology to use, null means ISO default
     * @return the chronology, never null
     */
    public Chronology getChronology(Object object, Chronology chrono) {
        return DateTimeUtils.getChronology(chrono);
    }

    //-----------------------------------------------------------------------
    /**
     * Extracts the values of the partial from an object of this converter's type.
     * The chrono parameter is a hint to the converter, should it require a
     * chronology to aid in conversion.
     * 
     * @param fieldSource  a partial that provides access to the fields.
     *  This partial may be incomplete and only getFieldType(int) should be used
     * @param object  the object to convert
     * @param chrono  the chronology to use, which is the non-null result of getChronology()
     * @return the array of field values that match the 
     */
    public int[] getPartialValues(ReadablePartial fieldSource, Object object, Chronology chrono) {
        long instant = getInstantMillis(object, chrono);
        return chrono.get(fieldSource, instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Selects a suitable period type for the given object.
     *
     * @param object  the object to examine
     * @return the period type, never null
     */
    public PeriodType getPeriodType(Object object) {
        return PeriodType.standard();
    }

    //-----------------------------------------------------------------------
    /**
     * Checks if the input is a ReadableInterval.
     * <p>
     * If it is, then the calling code should cast and copy the fields directly.
     *
     * @param object  the object to convert
     * @param chrono  the chronology to use, may be null
     * @return true if the input is a ReadableInterval
     */
    public boolean isReadableInterval(Object object, Chronology chrono) {
        return false;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a debugging string version of this converter.
     * 
     * @return a debugging string
     */
    public String toString() {
        return "Converter[" + (getSupportedType() == null ? "null" : getSupportedType().getName()) + "]";
    }

}
