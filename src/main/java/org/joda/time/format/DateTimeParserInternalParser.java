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
 * Adapter between old and new parser interface.
 *
 * @author Stephen Colebourne
 * @since 2.4
 */
class DateTimeParserInternalParser implements InternalParser {
    
    private final DateTimeParser underlying;

    static InternalParser of(DateTimeParser underlying) {
        if (underlying instanceof InternalParserDateTimeParser) {
            return (InternalParser) underlying;
        }
        if (underlying == null) {
            return null;
        }
        return new DateTimeParserInternalParser(underlying);
    }

    private DateTimeParserInternalParser(DateTimeParser underlying) {
        this.underlying = underlying;
    }

    //-----------------------------------------------------------------------
    DateTimeParser getUnderlying() {
        return underlying;
    }

    public int estimateParsedLength() {
        return underlying.estimateParsedLength();
    }

    public int parseInto(DateTimeParserBucket bucket, CharSequence text, int position) {
        return underlying.parseInto(bucket, text.toString(), position);
    }

}
