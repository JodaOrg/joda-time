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

import org.joda.time.DurationField;

/**
 * <code>DecoratedDurationField</code> extends {@link AbstractDurationField},
 * implementing only the minimum required set of methods. These implemented
 * methods delegate to a wrapped field.
 * <p>
 * This design allows new DurationField types to be defined that piggyback on
 * top of another, inheriting all the safe method implementations from
 * AbstractDurationField. Should any method require pure delegation to the
 * wrapped field, simply override and use the provided getWrappedField method.
 * <p>
 * DecoratedDurationField is thread-safe and immutable, and its subclasses must
 * be as well.
 *
 * @author Brian S O'Neill
 * @see DelegatedDurationField
 */
public class DecoratedDurationField extends AbstractDurationField {

    static final long serialVersionUID = 8019982251647420015L;

    /** The DurationField being wrapped */
    private final DurationField iField;

    /**
     * @param name allow name to be overridden
     */
    public DecoratedDurationField(DurationField field, String name) {
        super(name);
        if (field == null) {
            throw new IllegalArgumentException("The field must not be null");
        }
        if (!field.isSupported()) {
            throw new IllegalArgumentException("The field must be supported");
        }
        iField = field;
    }

    /**
     * Gets the wrapped duration field.
     * 
     * @return the wrapped DurationField
     */
    public final DurationField getWrappedField() {
        return iField;
    }

    public boolean isPrecise() {
        return iField.isPrecise();
    }

    public long getValueAsLong(long duration, long instant) {
        return iField.getValueAsLong(duration, instant);
    }

    public long getMillis(int value, long instant) {
        return iField.getMillis(value, instant);
    }

    public long getMillis(long value, long instant) {
        return iField.getMillis(value, instant);
    }

    public long add(long instant, int value) {
        return iField.add(instant, value);
    }

    public long add(long instant, long value) {
        return iField.add(instant, value);
    }

    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        return iField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    public long getUnitMillis() {
        return iField.getUnitMillis();
    }

}
