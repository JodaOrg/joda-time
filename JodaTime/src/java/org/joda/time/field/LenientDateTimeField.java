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

import org.joda.time.DateTimeField;

/**
 * Converts a strict DateTimeField into a lenient one. By being lenient, the
 * set method accepts out of bounds values, performing an addition instead.
 * <p>
 * LenientDateTimeField is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @see org.joda.time.chrono.LenientChronology
 * @see StrictDateTimeField
 */
public class LenientDateTimeField extends DelegatedDateTimeField {

    static final long serialVersionUID = 8714085824173290599L;

    /**
     * Returns a lenient version of the given field. If it is already lenient,
     * then it is returned as-is. Otherwise, a new LenientDateTimeField is
     * returned.
     */
    public static DateTimeField getInstance(DateTimeField field) {
        if (field == null) {
            return null;
        }
        if (field instanceof StrictDateTimeField) {
            field = ((StrictDateTimeField)field).getWrappedField();
        }
        if (field.isLenient()) {
            return field;
        }
        return new LenientDateTimeField(field);
    }

    protected LenientDateTimeField(DateTimeField field) {
        super(field);
    }

    public final boolean isLenient() {
        return true;
    }

    /**
     * Set values which may be out of bounds. If the value is out of bounds,
     * the instant is first set to the minimum allowed value, and then the
     * difference is added.
     */
    public long set(long instant, int value) {
        int min = getMinimumValue(instant);
        if (value >= min && value < getMaximumValue(instant)) {
            return super.set(instant, value);
        }
        return add(super.set(instant, min), value - min);
    }
}
