/*
 *  Copyright 2001-2005 Stephen Colebourne
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

import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.ReadablePeriod;

/**
 * Abstract base class for implementing {@link PeriodPrinter}s,
 * {@link PeriodParser}s, and {@link PeriodFormatter}s. This class
 * intentionally does not implement any of those interfaces. You can subclass
 * and implement only the interfaces that you need to.
 * <p>
 * The print methods assume that your subclass has implemented PeriodPrinter or
 * PeriodFormatter. If not, a ClassCastException is thrown when calling those
 * methods.
 * <p>
 * Likewise, the parse methods assume that your subclass has implemented
 * PeriodParser or PeriodFormatter. If not, a ClassCastException is thrown
 * when calling the parse methods.
 * 
 * @author Brian S O'Neill
 * @since 1.0
 */
public abstract class BasePeriodFormatter {
    
    public int countFieldsToPrint(ReadablePeriod period) {
        return ((PeriodPrinter) this).countFieldsToPrint(period, Integer.MAX_VALUE);
    }

    public String print(ReadablePeriod period) {
        PeriodPrinter printer = (PeriodPrinter) this;
        StringBuffer buf = new StringBuffer(printer.calculatePrintedLength(period));
        printer.printTo(buf, period);
        return buf.toString();
    }

    //-----------------------------------------------------------------------
    public Period parsePeriod(PeriodType type, String text) {
        return ((PeriodParser) this).parseMutablePeriod(type, text).toPeriod();
    }

    public MutablePeriod parseMutablePeriod(PeriodType type, String text) {
        PeriodParser parser = (PeriodParser) this;
        MutablePeriod period = new MutablePeriod(0, type);

        int newPos = parser.parseInto(period, text, 0);
        if (newPos >= 0) {
            if (newPos >= text.length()) {
                return period;
            }
        } else {
            newPos = ~newPos;
        }

        throw new IllegalArgumentException(FormatUtils.createErrorMessage(text, newPos));
    }

}
