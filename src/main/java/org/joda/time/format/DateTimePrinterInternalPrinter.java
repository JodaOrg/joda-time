/*
 *  Copyright 2001-2014 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.format;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadablePartial;

/**
 * Adapter between old and new printer interface.
 *
 * @author Stephen Colebourne
 * @since 2.4
 */
class DateTimePrinterInternalPrinter implements InternalPrinter {
    
    private final DateTimePrinter underlying;

    static InternalPrinter of(DateTimePrinter underlying) {
        if (underlying instanceof InternalPrinterDateTimePrinter) {
            return (InternalPrinter) underlying;
        }
        if (underlying == null) {
            return null;
        }
        return new DateTimePrinterInternalPrinter(underlying);
    }

    private DateTimePrinterInternalPrinter(DateTimePrinter underlying) {
        this.underlying = underlying;
    }

    //-----------------------------------------------------------------------
    DateTimePrinter getUnderlying() {
        return underlying;
    }

    //-----------------------------------------------------------------------
    public int estimatePrintedLength() {
        return underlying.estimatePrintedLength();
    }

    public void printTo(Appendable appendable, long instant, Chronology chrono, int displayOffset,
                    DateTimeZone displayZone, Locale locale) throws IOException {
        if (appendable instanceof StringBuffer) {
            StringBuffer buf = (StringBuffer) appendable;
            underlying.printTo(buf, instant, chrono, displayOffset, displayZone, locale);
        }
        if (appendable instanceof Writer) {
            Writer out = (Writer) appendable;
            underlying.printTo(out, instant, chrono, displayOffset, displayZone, locale);
        }
        StringBuffer buf = new StringBuffer(estimatePrintedLength());
        underlying.printTo(buf, instant, chrono, displayOffset, displayZone, locale);
        appendable.append(buf);
    }

    public void printTo(Appendable appendable, ReadablePartial partial, Locale locale) throws IOException {
        if (appendable instanceof StringBuffer) {
            StringBuffer buf = (StringBuffer) appendable;
            underlying.printTo(buf, partial, locale);
        }
        if (appendable instanceof Writer) {
            Writer out = (Writer) appendable;
            underlying.printTo(out, partial, locale);
        }
        StringBuffer buf = new StringBuffer(estimatePrintedLength());
        underlying.printTo(buf, partial, locale);
        appendable.append(buf);
    }

}
