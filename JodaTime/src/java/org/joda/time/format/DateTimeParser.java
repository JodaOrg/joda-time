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

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadWritableInstant;
import org.joda.time.ReadableInstant;

/**
 * Defines an interface for parsing textual representations of datetimes.
 *
 * @author Brian S O'Neill
 * @see DateTimeFormatter
 * @see DateTimeFormatterBuilder
 * @see DateTimeFormat
 * @since 1.0
 */
public interface DateTimeParser {

    /**
     * Returns the expected maximum number of characters consumed. The actual
     * amount should rarely exceed this estimate.
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
     * @param bucket  field are saved into this
     * @param text  the text to parse
     * @param position  position to start parsing from
     * @return new position, negative value means parse failed -
     *  apply complement operator (~) to get position of failure
     * @throws IllegalArgumentException if any field is out of range
     */
    int parseInto(DateTimeParserBucket bucket, String text, int position);

    /**
     * Parses a datetime from the given text, at the given position, saving the
     * result into the fields of the given ReadWritableInstant. If the parse
     * succeeds, the return value is the new text position. Note that the parse
     * may succeed without fully reading the text.
     * <p>
     * If it fails, the return value is negative, but the instant may still be
     * modified. To determine the position where the parse failed, apply the
     * one's complement operator (~) on the return value.
     * <p>
     * The parse will use the chronology of the instant.
     *
     * @param instant  an instant that will be modified
     * @param text  the text to parse
     * @param position  position to start parsing from
     * @return new position, negative value means parse failed -
     *  apply complement operator (~) to get position of failure
     * @throws IllegalArgumentException if the instant is null
     * @throws IllegalArgumentException if any field is out of range
     */
    int parseInto(ReadWritableInstant instant, String text, int position);

    //-----------------------------------------------------------------------
    /**
     * Parses a datetime from the given text, returning the number of
     * milliseconds since the epoch, 1970-01-01T00:00:00Z.
     * <p>
     * The parse will use the ISO chronology, and the default time zone.
     * If the text contains a time zone string then that will be taken into account.
     *
     * @param text  text to parse
     * @return parsed value expressed in milliseconds since the epoch
     * @throws IllegalArgumentException if the text to parse is invalid
     */
    long parseMillis(String text);

    /**
     * Parses a datetime from the given text, returning the number of
     * milliseconds since the epoch, 1970-01-01T00:00:00Z.
     * <p>
     * The parse will use the given chronology and time zone.
     * If the text contains a time zone string then that will be taken into account.
     *
     * @param text  the text to parse
     * @param chrono  the chronology to use, null means ISO default
     * @return parsed value expressed in milliseconds since the epoch
     * @throws IllegalArgumentException if the text to parse is invalid
     */
    long parseMillis(String text, Chronology chrono);

    //-----------------------------------------------------------------------
    /**
     * Parses a datetime from the given text, at the given position, returning
     * the number of milliseconds since the epoch, 1970-01-01T00:00:00Z.
     * An initial millisecond value is passed in, which is relative to the epoch,
     * local time, and which can default field values.
     * <p>
     * The parse will use the ISO chronology and default time zone.
     * If the text contains a time zone string then that will be taken into account.
     *
     * @param text  the text to parse
     * @param instant  initial value of instant, relative to the epoch, local time
     * @return parsed value expressed in milliseconds since the epoch, UTC
     * @throws IllegalArgumentException if the text to parse is invalid
     */
    long parseMillis(String text, long instant);

    /**
     * Parses a datetime from the given text, at the given position, returning
     * the number of milliseconds since the epoch, 1970-01-01T00:00:00Z.
     * An initial millisecond value is passed in, which is relative to the epoch,
     * which can default field values.
     * <p>
     * The parse will use the given chronology and time zone.
     * If the text contains a time zone string then that will be taken into account.
     *
     * @param text  the text to parse
     * @param instant  initial value of instant, relative to the epoch
     * @param chrono  the chronology to use, null means ISO default
     * @return parsed value expressed in milliseconds since the epoch, UTC
     * @throws IllegalArgumentException if the text to parse is invalid
     */
    long parseMillis(String text, long instant, Chronology chrono);

    //-----------------------------------------------------------------------
    /**
     * Parses a datetime from the given text, returning a new DateTime.
     * <p>
     * The parse will use the ISO chronology and default time zone.
     * If the text contains a time zone string then that will be taken into account.
     *
     * @param text  the text to parse
     * @return parsed value in a DateTime object
     * @throws IllegalArgumentException if the text to parse is invalid
     */
    DateTime parseDateTime(String text);

    /**
     * Parses a datetime from the given text, returning a new DateTime.
     * <p>
     * The parse will use the given chronology and time zone.
     * If the text contains a time zone string then that will be taken into account.
     *
     * @param text  the text to parse
     * @param chrono  the chronology to use, null means ISO default
     * @return parsed value in a DateTime object
     * @throws IllegalArgumentException if the text to parse is invalid
     */
    DateTime parseDateTime(String text, Chronology chrono);

    /**
     * Parses a datetime from the given text, returning a new DateTime, using
     * the given instant to supply field values that were not parsed.
     * <p>
     * The parse will use the instant's chronology and time zone.
     * If the text contains a time zone string then that will be taken into account.
     *
     * @param text  the text to parse
     * @param instant  initial value of DateTime
     * @return parsed value in a DateTime object
     * @throws IllegalArgumentException if the text to parse is invalid
     */
    DateTime parseDateTime(String text, ReadableInstant instant);

    //-----------------------------------------------------------------------
    /**
     * Parses a datetime from the given text, returning a new MutableDateTime.
     * <p>
     * The parse will use the ISO chronology and default time zone.
     * If the text contains a time zone string then that will be taken into account.
     *
     * @param text  the text to parse
     * @return parsed value in a MutableDateTime object
     * @throws IllegalArgumentException if the text to parse is invalid
     */
    MutableDateTime parseMutableDateTime(String text);

    /**
     * Parses a datetime from the given text, returning a new MutableDateTime.
     * <p>
     * The parse will use the given chronology and time zone.
     * If the text contains a time zone string then that will be taken into account.
     *
     * @param text  the text to parse
     * @param chrono  the chronology to use, null means ISO default
     * @return parsed value in a MutableDateTime object
     * @throws IllegalArgumentException if the text to parse is invalid
     */
    MutableDateTime parseMutableDateTime(String text, Chronology chrono);

    /**
     * Parses a datetime from the given text, returning a new MutableDateTime,
     * using the given instant to supply field values that were not parsed.
     * <p>
     * The parse will use the instant's chronology and time zone.
     * If the text contains a time zone string then that will be taken into account.
     *
     * @param text  the text to parse
     * @param instant  initial value of DateTime
     * @return parsed value in a MutableDateTime object
     * @throws IllegalArgumentException if the text to parse is invalid
     */
    MutableDateTime parseMutableDateTime(String text, ReadableInstant instant);

}
