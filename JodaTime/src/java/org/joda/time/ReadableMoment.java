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
 * Represents a moment in time that can be accessed via a datetime field type.
 * <p>
 * <code>ReadableMoment</code> is the highest abstraction in the model.
 * It allows different kinds of datetime objects to be accessed in a uniform way.
 * <p>
 * The implementation class will almost always implement another interface:
 * <ul>
 * <li>{@link ReadableInstant} - for a fully specified datetime (with a time zone)
 * <li>{@link ReadableLocal} - for a local datetime, date or time (without a time zone)
 * <li>{@link ReadablePartial} - for a partially defined datetime (a collection of datetime fields)
 * </ul>
 * Thus, the implementation may not represent a full datetime where all fields
 * can be queried. That is why the {@link #isSupported(DateTimeFieldType)} method
 * is provided.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public interface ReadableMoment {

    /**
     * Gets the chronology of the instance which is never null.
     * <p>
     * The {@link Chronology} is the calculation engine and provides conversion
     * and validation of the fields in a particular calendar system.
     * 
     * @return the chronology, never null
     */
    Chronology getChronology();

    /**
     * Gets the value of one of the fields.
     * <p>
     * The field type specified must be one of those that is supported by the implementation.
     *
     * @param field  the field type to use, which must be supported by this implementation
     * @return the value of that field
     * @throws IllegalArgumentException if the field is null or not supported
     */
    int get(DateTimeFieldType field);

    /**
     * Checks whether the field type specified is supported by this implementation.
     *
     * @param field  the field type to check, may be null which returns false
     * @return true if the field is supported
     */
    boolean isSupported(DateTimeFieldType field);

}
