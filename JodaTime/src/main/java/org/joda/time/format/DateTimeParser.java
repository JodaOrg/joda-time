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

/**
 * Internal interface for parsing textual representations of datetimes.
 * <p>
 * Application users will rarely use this class directly. Instead, you
 * will use one of the factory classes to create a {@link DateTimeFormatter}.
 * <p>
 * The factory classes are:<br />
 * - {@link DateTimeFormatterBuilder}<br />
 * - {@link DateTimeFormat}<br />
 * - {@link ISODateTimeFormat}<br />
 *
 * @author Brian S O'Neill
 * @see DateTimeFormatter
 * @see DateTimeFormatterBuilder
 * @see DateTimeFormat
 * @since 1.0
 */
public interface DateTimeParser {

    /**
     * Returns the expected maximum number of characters consumed.
     * The actual amount should rarely exceed this estimate.
     * 
     * @return the estimated length
     */
    int estimateParsedLength();

    /**
     * Parse an element from the given text, saving any fields into the given
     * DateTimeParserBucket. If the parse succeeds, the return value is the new
     * text position. Note that the parse may succeed without fully reading the
     * text.
     * <p>
     * If it fails, the return value is negative. To determine the position
     * where the parse failed, apply the one's complement operator (~) on the
     * return value.
     *
     * @param bucket  field are saved into this, not null
     * @param text  the text to parse, not null
     * @param position  position to start parsing from
     * @return new position, negative value means parse failed -
     *  apply complement operator (~) to get position of failure
     * @throws IllegalArgumentException if any field is out of range
     */
    int parseInto(DateTimeParserBucket bucket, String text, int position);

}
