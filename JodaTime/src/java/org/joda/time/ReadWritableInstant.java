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
package org.joda.time;

/**
 * Defines an instant in the datetime continuum that can be queried and modified.
 * This interface expresses the datetime as milliseconds from 1970-01-01T00:00:00Z.
 * <p>
 * The implementation of this interface will be mutable.
 * It may provide more advanced methods than those in the interface.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public interface ReadWritableInstant extends ReadableInstant {

    /**
     * Set the value as the number of milliseconds since
     * the epoch, 1970-01-01T00:00:00Z.
     * 
     * @param instant  the milliseconds since 1970-01-01T00:00:00Z to set the
     * instant to
     * @throws IllegalArgumentException if the value is invalid
     */
    void setMillis(long instant);

    /**
     * Set the value from an Object representing an instant.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     * 
     * @param instant  an object representing an instant
     * @throws IllegalArgumentException if the value is invalid
     */
    void setMillis(Object instant);

    /**
     * Set the chronology of the datetime, which has no effect if not
     * applicable.
     * 
     * @param chronology  the chronology to use, null means ISOChronology in default zone
     * @throws IllegalArgumentException if the value is invalid
     */
    void setChronology(Chronology chronology);

    /**
     * Sets the time zone of the datetime, changing the chronology and field values.
     * <p>
     * Changing the zone using this method retains the millisecond instant.
     * The millisecond instant is adjusted in the new zone to compensate.
     * 
     * chronology. Setting the time zone does not affect the millisecond value
     * of this instant.
     * <p>
     * If the chronology already has this time zone, no change occurs.
     *
     * @param zone  the time zone to use, null means default zone
     * @see #setZoneRetainFields
     */
    void setZone(DateTimeZone zone);

    /**
     * Sets the time zone of the datetime, changing the chronology and millisecond.
     * <p>
     * Changing the zone using this method retains the field values.
     * The millisecond instant is adjusted in the new zone to compensate.
     * <p>
     * If the chronology already has this time zone, no change occurs.
     *
     * @param zone  the time zone to use, null means default zone
     * @see #setZone
     */
    void setZoneRetainFields(DateTimeZone zone);

    //-----------------------------------------------------------------------
    /**
     * Add a number of millis to the value.
     * <p>
     * If the resulting value is too large for millis, seconds
     * will change and so on unless it is too large for the
     * implementation, when an exception is thrown.
     *
     * @param duration  the millis to add
     * @throws IllegalArgumentException if the value is invalid
     */
    void add(long duration);

    /**
     * Add an amount of time.
     * <p>
     * If the resulting value is too large for the implementation,
     * an exception is thrown.
     *
     * @param duration  duration to add.
     * @throws IllegalArgumentException if the value is invalid
     */
    void add(ReadableDuration duration);

    /**
     * Add an amount of time.
     * <p>
     * If the resulting value is too large for the implementation,
     * an exception is thrown.
     *
     * @param duration  duration to add.
     * @param scalar direction and amount to add, which may be negative
     * @throws IllegalArgumentException if the value is invalid
     */
    void add(ReadableDuration duration, int scalar);

    /**
     * Add an amount of time, either a ReadableDuration or Long (millis).
     * <p>
     * If the resulting value is too large for the implementation,
     * an exception is thrown.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableDuration, String and Long.
     *
     * @param duration  an object representing a duration
     * @throws IllegalArgumentException if the value is invalid
     */
    void add(Object duration);

    //-----------------------------------------------------------------------
    /**
     * Set the value of one of the fields of a datetime.
     * <p>
     * DateTimeField instances are generally obtained from a {@link Chronology} subclass.
     * However, an application can create its own DateTimeField to manipulate the
     * date time millis in new ways.
     *
     * @param field  a DateTimeField instance, usually obtained from a Chronology
     * @param value  the value of that field for the millis set in the implementation
     * @throws IllegalArgumentException if the value is invalid
     */
    void set(DateTimeField field, int value);
    
    /**
     * Add to the value of one of the fields of a datetime.
     * <p>
     * DateTimeField instances are generally obtained from a {@link Chronology} subclass.
     * However, an application can create its own DateTimeField to manipulate the
     * date time millis in new ways.
     *
     * @param field  a DateTimeField instance, usually obtained from a Chronology
     * @param value  the value of that field for the millis set in the implementation
     * @throws IllegalArgumentException if the value is invalid
     */
    void add(DateTimeField field, int value);
    
    /**
     * Add to the value of one of the fields of a datetime, wrapping within that field.
     * <p>
     * For example, 2002-03-01 add 14 months gives 2003-05-01. But 2002-03-01 add wrapped
     * 14 months gives 2002-05-01. This is similar to the <code>roll</code> method on Calendar.
     * <p>
     * DateTimeField instances are generally obtained from a {@link Chronology} subclass.
     * However, an application can create its own DateTimeField to manipulate the
     * date time millis in new ways.
     *
     * @param field  a DateTimeField instance, usually obtained from a Chronology
     * @param value  the value of that field for the millis set in the implementation
     * @throws IllegalArgumentException if the value is invalid
     */
    void addWrapped(DateTimeField field, int value);
    
}
