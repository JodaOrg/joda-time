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
package org.joda.time.field;

/**
 * Duration field class representing a field with a fixed unit length.
 * <p>
 * PreciseDurationField is thread-safe and immutable.
 * 
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public class PreciseDurationField extends AbstractDurationField {
    
    static final long serialVersionUID = -8346152187724495365L;

    /** The size of the unit */
    private final long iUnitMillis;

    /**
     * Constructor.
     * 
     * @param name  the name of the field, like "seconds"
     * @param unitMillis  the unit milliseconds
     */    
    public PreciseDurationField(String name, long unitMillis) {
        super(name);
        iUnitMillis = unitMillis;
    }
    
    //------------------------------------------------------------------------
    /**
     * This field is precise.
     * 
     * @return true always
     */
    public final boolean isPrecise() {
        return true;
    }
    
    /**
     * Returns the amount of milliseconds per unit value of this field.
     *
     * @return the unit size of this field, in milliseconds
     */
    public final long getUnitMillis() {
        return iUnitMillis;
    }

    //------------------------------------------------------------------------
    /**
     * Get the value of this field from the milliseconds.
     * 
     * @param duration  the milliseconds to query, which may be negative
     * @param instant  ignored
     * @return the value of the field, in the units of the field, which may be
     * negative
     */
    public long getValueAsLong(long duration, long instant) {
        return duration / iUnitMillis;
    }

    /**
     * Get the millisecond duration of this field from its value.
     * 
     * @param value  the value of the field, which may be negative
     * @param instant  ignored
     * @return the milliseconds that the field represents, which may be
     * negative
     */
    public long getMillis(int value, long instant) {
        return value * iUnitMillis;
    }

    /**
     * Get the millisecond duration of this field from its value.
     * 
     * @param value  the value of the field, which may be negative
     * @param instant  ignored
     * @return the milliseconds that the field represents, which may be
     * negative
     */
    public long getMillis(long value, long instant) {
        return value * iUnitMillis;
    }

    public long add(long instant, int value) {
        return instant + value * iUnitMillis;
    }

    public long add(long instant, long value) {
        return instant + value * iUnitMillis;
    }

    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        return (minuendInstant - subtrahendInstant) / iUnitMillis;
    }

}
