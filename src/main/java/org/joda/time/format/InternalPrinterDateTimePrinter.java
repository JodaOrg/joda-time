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
class InternalPrinterDateTimePrinter implements DateTimePrinter, InternalPrinter {
    
    private final InternalPrinter underlying;

    static DateTimePrinter of(InternalPrinter underlying) {
        if (underlying instanceof DateTimePrinterInternalPrinter) {
            return ((DateTimePrinterInternalPrinter) underlying).getUnderlying();
        }
        if (underlying instanceof DateTimePrinter) {
            return (DateTimePrinter) underlying;
        }
        if (underlying == null) {
            return null;
        }
        return new InternalPrinterDateTimePrinter(underlying);
    }

    private InternalPrinterDateTimePrinter(InternalPrinter underlying) {
        this.underlying = underlying;
    }

    //-----------------------------------------------------------------------
    public int estimatePrintedLength() {
        return underlying.estimatePrintedLength();
    }

    public void printTo(StringBuffer buf, long instant, Chronology chrono,
                    int displayOffset, DateTimeZone displayZone, Locale locale) {
        try {
            underlying.printTo(buf, instant, chrono, displayOffset, displayZone, locale);
        } catch (IOException ex) {
            // StringBuffer does not throw IOException
        }
    }

    public void printTo(Writer out, long instant, Chronology chrono,
                    int displayOffset, DateTimeZone displayZone, Locale locale) throws IOException {
        underlying.printTo(out, instant, chrono, displayOffset, displayZone, locale);
    }

    public void printTo(Appendable appendable, long instant, Chronology chrono, int displayOffset,
                    DateTimeZone displayZone, Locale locale) throws IOException {
        underlying.printTo(appendable, instant, chrono, displayOffset, displayZone, locale);
    }

    public void printTo(StringBuffer buf, ReadablePartial partial, Locale locale) {
        try {
            underlying.printTo(buf, partial, locale);
        } catch (IOException ex) {
            // StringBuffer does not throw IOException
        }
    }

    public void printTo(Writer out, ReadablePartial partial, Locale locale) throws IOException {
        underlying.printTo(out, partial, locale);
    }

    public void printTo(Appendable appendable, ReadablePartial partial, Locale locale) throws IOException {
        underlying.printTo(appendable, partial, locale);
    }

    //-----------------------------------------------------------------------
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof InternalPrinterDateTimePrinter) {
            InternalPrinterDateTimePrinter other = (InternalPrinterDateTimePrinter) obj;
            return underlying.equals(other.underlying);
        }
        return false;
    }

}
