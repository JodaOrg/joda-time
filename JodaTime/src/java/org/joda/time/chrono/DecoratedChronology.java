/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-03 Stephen Colebourne.
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
package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;

/**
 * <code>DecoratedChronology</code> extends {@link Chronology}, implementing
 * only the minimum required set of methods. These implemented methods delegate
 * to a wrapped field. The withUTC, withDateTimeZone, and toString methods are
 * exceptions, and are left abstract.
 * <p>
 * This design allows new Chronology types to be defined that piggyback on top
 * of another, inheriting all the safe method implementations from
 * Chronology. Should any method require pure delegation to the wrapped field,
 * simply override and use the provided getWrappedChronology method.
 * <p>
 * DecoratedChronology is thread-safe and immutable, and its subclasses must
 * be as well.
 *
 * @author Brian S O'Neill
 * @since 1.0
 * @see DelegatedChronology
 */
public abstract class DecoratedChronology extends Chronology {

    static final long serialVersionUID = 7094038875466049631L;

    /** The Chronology being wrapped */
    private final Chronology iChronology;
    
    /**
     * Create a DecoratedChronology for any chronology.
     *
     * @param chrono the chronology
     * @throws IllegalArgumentException if chronology is null
     */
    protected DecoratedChronology(Chronology chrono) {
        if (chrono == null) {
            throw new IllegalArgumentException("The Chronology must not be null");
        }
        iChronology = chrono;
    }
    
    /**
     * Gets the wrapped chronology.
     * 
     * @return the wrapped Chronology
     */
    protected Chronology getWrappedChronology() {
        return iChronology;
    }

    /**
     * Get the Chronology in the UTC time zone.
     * 
     * @return Chronology in UTC
     */
    public abstract Chronology withUTC();

    /**
     * Get the Chronology in the any time zone.
     * 
     * @return Chronology in ant time zone
     */
    public abstract Chronology withDateTimeZone(DateTimeZone zone);

    /**
     * Gets the time zone that this chronology is using.
     * 
     * @return the DateTimeZone
     */
    public DateTimeZone getDateTimeZone() {
        return iChronology.getDateTimeZone();
    }

}
