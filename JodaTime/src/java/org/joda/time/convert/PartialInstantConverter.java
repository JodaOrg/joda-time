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
import org.joda.time.DateTimeZone;
import org.joda.time.PartialInstant;
import org.joda.time.chrono.ISOChronology;

/**
 * PartialInstantConverter extracts milliseconds and chronology from a
 * PartialInstant. Since supplying a time zone requires special attention, the
 * regular ReadableInstantConverter is not quite right.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
class PartialInstantConverter extends ReadableInstantConverter {
    /**
     * Singleton instance.
     */
    static final PartialInstantConverter INSTANCE = new PartialInstantConverter();

    /**
     * Restricted constructor.
     */
    protected PartialInstantConverter() {
        super();
    }

    /**
     * Extracts the millis from an object of this convertor's type.
     * 
     * @param object  the object to convert, must not be null
     * @return the millisecond instant
     */
    public long getInstantMillis(Object object) {
        return getInstantMillis(object, (DateTimeZone) null);
    }
    
    /**
     * Extracts the millis from an object of this convertor's type.
     * 
     * @param object  the object to convert, must not be null
     * @param zone  the zone to use, null means default zone
     * @return the millisecond instant
     */
    public long getInstantMillis(Object object, DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        long millis = ((PartialInstant) object).getMillis();
        return millis - zone.getOffsetFromLocal(millis);
    }
    
    /**
     * Extracts the millis from an object of this convertor's type.
     * 
     * @param object  the object to convert, must not be null
     * @param chrono  the chronology to use, null means ISOChronology
     * @return the millisecond instant
     */
    public long getInstantMillis(Object object, Chronology chrono) {
        long millis = ((PartialInstant) object).getMillis();
        DateTimeZone zone;
        if (chrono == null) {
            zone = DateTimeZone.getDefault();
        } else {
            zone = chrono.getDateTimeZone();
        }
        if (zone != null) {
            millis -= zone.getOffsetFromLocal(millis);
        }
        return millis;
    }
    
    //-----------------------------------------------------------------------
    /**
     * Gets the chronology, which is taken from the PartialDateTime.  If the
     * chronology on the datetime is null, the ISOChronology in the default
     * time zone is used. Otherwise, the chronology is returned in the default
     * time zone.
     * 
     * @param object  the object to convert, must not be null
     * @return the chronology, never null
     */
    public Chronology getChronology(Object object) {
        Chronology chrono = ((PartialInstant) object).getChronology();
        if (chrono == null) {
            return ISOChronology.getInstance();
        }
        return chrono.withDateTimeZone(DateTimeZone.getDefault());
    }
    
    //-----------------------------------------------------------------------
    /**
     * Returns PartialDateTime.class.
     * 
     * @return PartialDateTime.class
     */
    public Class getSupportedType() {
        return PartialInstant.class;
    }

}
