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
import org.joda.time.Period;
import org.joda.time.ReadWritableInterval;
import org.joda.time.ReadWritablePeriod;

/**
 * NullConverter converts null to milliseconds (now) in the ISOChronology.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
class NullConverter extends AbstractConverter
        implements InstantConverter, DurationConverter, PeriodConverter, IntervalConverter {
    
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
     * Gets the millisecond instant, which is now.
     * 
     * @param object  the object to convert
     * @return the millisecond instant of now
     */
    public long getInstantMillis(Object object) {
        return DateTimeUtils.currentTimeMillis();
    }
    
    //-----------------------------------------------------------------------
    /**
     * Gets the millisecond duration, which is zero.
     * 
     * @param object  the object to convert
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
     * @param object  the object to convert
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
     * @param object  the object to convert, must not be null
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
