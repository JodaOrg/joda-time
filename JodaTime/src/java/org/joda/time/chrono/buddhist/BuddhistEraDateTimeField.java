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
package org.joda.time.chrono.buddhist;

import java.util.Locale;

import org.joda.time.DateTimeField;

/**
 * Provides time calculations for the buddhist era component of time.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
final class BuddhistEraDateTimeField extends DateTimeField {
    
    /**
     * Singleton instance of BuddhistYearDateTimeField
     */
    static final DateTimeField INSTANCE = new BuddhistEraDateTimeField();

    /**
     * Restricted constructor
     */
    private BuddhistEraDateTimeField() {
        super("era");
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return INSTANCE;
    }

    /**
     * Get the Era component of the specified time instant.
     * 
     * @param millis  the time instant in millis to query.
     * @return the era extracted from the input.
     */
    public int get(long millis) {
        return BuddhistChronology.BE;
    }

    /**
     * Unsupported - add the specified eras to the specified time instant.
     * 
     * @param millis  the time instant in millis to update.
     * @param years  the years to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long millis, int era) {
        throw new UnsupportedOperationException("Adding to Era field is unsupported");
    }

    /**
     * Unsupported - add the specified eras to the specified time instant.
     * 
     * @param millis  the time instant in millis to update.
     * @param years  the years to add (can be negative).
     * @return the updated time instant.
     */
    public long add(long millis, long era) {
        throw new UnsupportedOperationException("Adding to Era field is unsupported");
    }

    /**
     * Unsupported - add the specified eras to the specified time instant.
     * 
     * @param millis  the time instant in millis to update.
     * @param era  the era to add (can be negative).
     * @return the updated time instant.
     */
    public long addWrapped(long millis, int era) {
        throw new UnsupportedOperationException("Adding to Era field is unsupported");
    }

    public long getDifference(long minuendMillis, long subtrahendMillis) {
        throw new UnsupportedOperationException("Era field difference is unsupported");
    }

    /**
     * Set the Era component of the specified time instant.
     * 
     * @param millis  the time instant in millis to update.
     * @param era  the era (BuddhistChonology.BE) to update the time to.
     * @return the updated time instant.
     * @throws IllegalArgumentException  if era is invalid.
     */
    public long set(long millis, int era) {
        super.verifyValueBounds(era, getMinimumValue(), getMaximumValue());

        return millis;
    }

    public long getUnitMillis() {
        // Should actually be double this, but that is not possible since Java
        // doesn't support unsigned types.
        return Long.MAX_VALUE;
    }

    public long getRangeMillis() {
        // Should actually be double this, but that is not possible since Java
        // doesn't support unsigned types.
        return Long.MAX_VALUE;
    }

    public int getMinimumValue() {
        return BuddhistChronology.BE;
    }

    public int getMaximumValue() {
        return BuddhistChronology.BE;
    }
    
    /**
     * @see org.joda.time.DateTimeField#getAsShortText(long, Locale)
     */
    public String getAsShortText(long millis, Locale locale) {
        return "BE";
    }
    
    /**
     * @see org.joda.time.DateTimeField#getMaximumShortTextLength(Locale)
     */
    public int getMaximumShortTextLength(Locale locale) {
        return 2;
    }

    /**
     * @see org.joda.time.DateTimeField#getAsShortText(long, Locale)
     */
    public String getAsText(long millis, Locale locale) {
        return "BE";
    }

    /**
     * @see org.joda.time.DateTimeField#getMaximumShortTextLength(Locale)
     */
    public int getMaximumTextLength(Locale locale) {
        return 2;
    }

    /**
     * @see org.joda.time.DateTimeField#set(long, String, Locale)
     */
    public long set(long millis, String text, Locale locale) {
        if ("BE".equals(text) == false) {
            throw new IllegalArgumentException("Invalid era text: " + text);
        }
        return millis;    
    }
    

    /**
     * Unsupported.
     * @throws UnsupportedOperationException always
     */
    public long roundFloor(long millis) {
        throw new UnsupportedOperationException("Rounding an Era field is unsupported");
    }

    /**
     * Unsupported.
     * @throws UnsupportedOperationException always
     */
    public long remainder(long millis) {
        throw new UnsupportedOperationException("Calculating remainder from Era field is unsupported");
    }
}
