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

import java.io.Serializable;
import java.util.HashMap;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

/**
 * A placeholder implementation to use when a duration field is not supported.
 * <p>
 * UnsupportedDurationField is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class UnsupportedDurationField extends DurationField implements Serializable {

    /** Serialization lock. */
    private static final long serialVersionUID = -6390301302770925357L;

    /** The cache of unsupported duration field instances */
    private static HashMap cCache;

    /**
     * Gets an instance of UnsupportedDurationField for a specific named field.
     * Names should be plural, such as 'years' or 'hours'.
     * The returned instance is cached.
     * 
     * @param type  the type to obtain
     * @return the instance
     */
    public static synchronized UnsupportedDurationField getInstance(DurationFieldType type) {
        UnsupportedDurationField field;
        if (cCache == null) {
            cCache = new HashMap(7);
            field = null;
        } else {
            field = (UnsupportedDurationField) cCache.get(type);
        }
        if (field == null) {
            field = new UnsupportedDurationField(type);
            cCache.put(type, field);
        }
        return field;
    }

    /** The name of the field */
    private final DurationFieldType iType;

    /**
     * Constructor.
     * 
     * @param type  the type to use
     */
    private UnsupportedDurationField(DurationFieldType type) {
        iType = type;
    }

    //-----------------------------------------------------------------------
    // Design note: Simple Accessors return a suitable value, but methods
    // intended to perform calculations throw an UnsupportedOperationException.

    public final DurationFieldType getType() {
        return iType;
    }

    public String getName() {
        return iType.getName();
    }

    /**
     * This field is not supported.
     *
     * @return false always
     */
    public boolean isSupported() {
        return false;
    }

    /**
     * This field is precise.
     * 
     * @return true always
     */
    public boolean isPrecise() {
        return true;
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int getValue(long duration) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long getValueAsLong(long duration) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int getValue(long duration, long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long getValueAsLong(long duration, long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long getMillis(int value) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long getMillis(long value) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long getMillis(int value, long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long getMillis(long value, long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long add(long instant, int value) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long add(long instant, long value) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int getDifference(long minuendInstant, long subtrahendInstant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        throw unsupported();
    }

    /**
     * Always returns zero.
     *
     * @return zero always
     */
    public long getUnitMillis() {
        return 0;
    }

    /**
     * Always returns zero, indicating that sort order is not relevent.
     *
     * @return zero always
     */
    public int compareTo(Object durationField) {
        return 0;
    }

    //------------------------------------------------------------------------
    /**
     * Compares this duration field to another.
     * 
     * @param obj  the object to compare to
     * @return true if equal
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof UnsupportedDurationField) {
            UnsupportedDurationField other = (UnsupportedDurationField) obj;
            if (other.getName() == null) {
                return (getName() == null);
            }
            return (other.getName().equals(getName()));
        }
        return false;
    }

    /**
     * Gets a suitable hashcode.
     * 
     * @return the hashcode
     */
    public int hashCode() {
        return getName().hashCode();
    }

    /**
     * Get a suitable debug string.
     * 
     * @return debug string
     */
    public String toString() {
        return "UnsupportedDurationField[" + getName() + ']';
    }

    /**
     * Ensure proper singleton serialization
     */
    private Object readResolve() {
        return getInstance(iType);
    }

    private UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException(iType + " field is unsupported");
    }

}
