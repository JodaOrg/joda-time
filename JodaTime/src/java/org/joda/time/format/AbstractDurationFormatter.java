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
package org.joda.time.format;

import org.joda.time.Duration;
import org.joda.time.DurationType;
import org.joda.time.MutableDuration;
import org.joda.time.ReadableDuration;

/**
 * Abstract base class for implementing {@link DurationPrinter}s,
 * {@link DurationParser}s, and {@link DurationFormatter}s. This class
 * intentionally does not implement any of those interfaces. You can subclass
 * and implement only the interfaces that you need to.
 * <p>
 * The print methods assume that your subclass has implemented DurationPrinter or
 * DurationFormatter. If not, a ClassCastException is thrown when calling those
 * methods.
 * <p>
 * Likewise, the parse methods assume that your subclass has implemented
 * DurationParser or DurationFormatter. If not, a ClassCastException is thrown
 * when calling the parse methods.
 * 
 * @author Brian S O'Neill
 * @since 1.0
 */
public abstract class AbstractDurationFormatter {
    
    public int countFieldsToPrint(ReadableDuration duration) {
        return ((DurationPrinter) this).countFieldsToPrint(duration, Integer.MAX_VALUE);
    }

    public String print(ReadableDuration duration) {
        DurationPrinter p = (DurationPrinter) this;
        StringBuffer buf = new StringBuffer(p.calculatePrintedLength(duration));
        p.printTo(buf, duration);
        return buf.toString();
    }

    public Duration parseDuration(DurationType type, String text) {
        return parseMutableDuration(type, text).toDuration();
    }

    public MutableDuration parseMutableDuration(DurationType type, String text) {
        DurationParser p = (DurationParser) this;
        MutableDuration duration = new MutableDuration(0, type);

        int newPos = p.parseInto(duration, text, 0);
        if (newPos >= 0) {
            if (newPos >= text.length()) {
                return duration;
            }
        } else {
            newPos = ~newPos;
        }

        throw new IllegalArgumentException(AbstractDateTimeFormatter
                                           .createErrorMessage(text, newPos));
    }
}
