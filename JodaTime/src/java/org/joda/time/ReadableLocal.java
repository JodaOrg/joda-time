/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2005 Stephen Colebourne.  
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
package org.joda.time;

/**
 * Defines a local time (no time zone) that does not fully define a single
 * instant in the datetime continuum.
 * <p>
 * A <code>ReadableLocal</code> will never have a timezone, thus is
 * always a local time. It may also only support a subset of the fields
 * on the chronology, see also {@link ReadablePartial}.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public interface ReadableLocal {

    /**
     * Gets the amount of time this instance represents.
     * <p>
     * The value returned from this method will be relative to the epoch
     * returned by {@link #getEpoch()} and in the units of {@link #getUnitDurationType()}.
     *
     * @return the amount of time this local represents
     */
    long getAmount();

    /**
     * Gets the length of each unit that this instance uses for the amount.
     * <p>
     * A <code>ReadableLocal</code> measures time in units defined by this method.
     * The result of {@link #getAmount()} must be interpretted in terms of these units.
     *
     * @return the duration of each unit of time this local represents
     */
    DurationFieldType getUnitDurationType();

    /**
     * Gets the epoch that this instance uses.
     * <p>
     * A <code>ReadableLocal</code> measures time in units whose duration is
     * defined by {@link #getUnitDurationType()}. This method returns the zero point,
     * allowing conversion between a <code>ReadableLocal</code> and a
     * <code>ReadableInstant</code>.
     *
     * @return the epoch that this instance measures against
     */
    DateTime getEpoch();

    /**
     * Gets the chronology of the ReadableLocal which is never null.
     * <p>
     * The {@link Chronology} is the calculation engine behind the ReadableLocal and
     * provides conversion and validation of the fields in a particular calendar system.
     * 
     * @return the chronology, never null
     */
    Chronology getChronology();

    //-----------------------------------------------------------------------
    /**
     * Gets the value of one of the fields.
     * <p>
     * The field type specified must be one of those that is supported by the ReadableLocal.
     *
     * @param field  a DateTimeFieldType instance that is supported by this ReadableLocal
     * @return the value of that field
     * @throws IllegalArgumentException if the field is null or not supported
     */
    int get(DateTimeFieldType field);

    /**
     * Checks whether the field type specified is supported by this ReadableLocal.
     *
     * @param field  the field to check, may be null which returns false
     * @return true if the field is supported
     */
    boolean isSupported(DateTimeFieldType field);

    //-----------------------------------------------------------------------
    /**
     * Get the value as a String in a recognisable ISO8601 format, only
     * displaying supported fields.
     * <p>
     * The string output is in ISO8601 format to enable the String
     * constructor to correctly parse it.
     *
     * @return the value as an ISO8601 string
     */
    String toString();

}
