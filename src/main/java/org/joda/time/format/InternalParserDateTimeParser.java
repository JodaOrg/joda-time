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


/**
 * Adapter between old and new printer interface.
 *
 * @author Stephen Colebourne
 * @since 2.4
 */
class InternalParserDateTimeParser implements DateTimeParser, InternalParser {
    
    private final InternalParser underlying;

    static DateTimeParser of(InternalParser underlying) {
        if (underlying instanceof DateTimeParserInternalParser) {
            return ((DateTimeParserInternalParser) underlying).getUnderlying();
        }
        if (underlying instanceof DateTimeParser) {
            return (DateTimeParser) underlying;
        }
        if (underlying == null) {
            return null;
        }
        return new InternalParserDateTimeParser(underlying);
    }

    private InternalParserDateTimeParser(InternalParser underlying) {
        this.underlying = underlying;
    }

    //-------------------------------------------------------------------------
    public int estimateParsedLength() {
        return underlying.estimateParsedLength();
    }

    public int parseInto(DateTimeParserBucket bucket, CharSequence text, int position) {
        return underlying.parseInto(bucket, text, position);
    }

    public int parseInto(DateTimeParserBucket bucket, String text, int position) {
        return underlying.parseInto(bucket, text, position);
    }

    //-----------------------------------------------------------------------
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof InternalParserDateTimeParser) {
            InternalParserDateTimeParser other = (InternalParserDateTimeParser) obj;
            return underlying.equals(other.underlying);
        }
        return false;
    }

}
