/*
 *  Copyright 2001-2006 Stephen Colebourne
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
package org.joda.time.base;

import java.util.Date;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * AbstractInstant provides the common behaviour for instant classes.
 * <p>
 * This class has no concept of a chronology, all methods work on the
 * millisecond instant.
 * <p>
 * This class should generally not be used directly by API users. The 
 * {@link ReadableInstant} interface should be used when different 
 * kinds of date/time objects are to be referenced.
 * <p>
 * Whenever you want to implement <code>ReadableInstant</code> you should
 * extend this class.
 * <p>
 * AbstractInstant itself is thread-safe and immutable, but subclasses may be
 * mutable and not thread-safe.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public abstract class AbstractInstant implements ReadableInstant {

    /**
     * Constructor.
     */
    protected AbstractInstant() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the time zone of the instant from the chronology.
     * 
     * @return the DateTimeZone that the instant is using, never null
     */
    public DateTimeZone getZone() {
        return getChronology().getZone();
    }

    /**
     * Get the value of one of the fields of a datetime using the chronology of the instant.
     * <p>
     * This method uses the chronology of the instant to obtain the value.
     * For example:
     * <pre>
     * DateTime dt = new DateTime();
     * int year = dt.get(DateTimeFieldType.year());
     * </pre>
     *
     * @param type  a field type, usually obtained from DateTimeFieldType, not null
     * @return the value of that field
     * @throws IllegalArgumentException if the field type is null
     */
    public int get(DateTimeFieldType type) {
        if (type == null) {
            throw new IllegalArgumentException("The DateTimeFieldType must not be null");
        }
        return type.getField(getChronology()).get(getMillis());
    }

    /**
     * Checks if the field type specified is supported by this instant and chronology.
     * This can be used to avoid exceptions in {@link #get(DateTimeFieldType)}.
     *
     * @param type  a field type, usually obtained from DateTimeFieldType
     * @return true if the field type is supported
     */
    public boolean isSupported(DateTimeFieldType type) {
        if (type == null) {
            return false;
        }
        return type.getField(getChronology()).isSupported();
    }

    /**
     * Get the value of one of the fields of a datetime.
     * <p>
     * This could be used to get a field using a different Chronology.
     * For example:
     * <pre>
     * Instant dt = new Instant();
     * int gjYear = dt.get(Chronology.getCoptic().year());
     * </pre>
     * 
     * @param field  the DateTimeField to use, not null
     * @return the value
     * @throws IllegalArgumentException if the field is null
     */
    public int get(DateTimeField field) {
        if (field == null) {
            throw new IllegalArgumentException("The DateTimeField must not be null");
        }
        return field.get(getMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Get this object as an Instant.
     * 
     * @return an Instant using the same millis
     */
    public Instant toInstant() {
        return new Instant(getMillis());
    }

    /**
     * Get this object as a DateTime in the same zone.
     *
     * @return a DateTime using the same millis
     */
    public DateTime toDateTime() {
        return new DateTime(getMillis(), getZone());
    }

    /**
     * Get this object as a DateTime using ISOChronology in the same zone.
     *
     * @return a DateTime using the same millis with ISOChronology
     */
    public DateTime toDateTimeISO() {
        return new DateTime(getMillis(), ISOChronology.getInstance(getZone()));
    }

    /**
     * Get this object as a DateTime using the same chronology but a different zone.
     * 
     * @param zone time zone to apply, or default if null
     * @return a DateTime using the same millis
     */
    public DateTime toDateTime(DateTimeZone zone) {
        Chronology chrono = DateTimeUtils.getChronology(getChronology());
        chrono = chrono.withZone(zone);
        return new DateTime(getMillis(), chrono);
    }

    /**
     * Get this object as a DateTime using the given chronology and its zone.
     * 
     * @param chronology chronology to apply, or ISOChronology if null
     * @return a DateTime using the same millis
     */
    public DateTime toDateTime(Chronology chronology) {
        return new DateTime(getMillis(), chronology);
    }

    // NOTE: Although the toMutableDateTime methods could check to see if this
    // is already a MutableDateTime and return this casted, it makes it too
    // easy to mistakenly modify ReadableDateTime input parameters. Always
    // returning a copy prevents this.

    /**
     * Get this object as a MutableDateTime in the same zone.
     *
     * @return a MutableDateTime using the same millis
     */
    public MutableDateTime toMutableDateTime() {
        return new MutableDateTime(getMillis(), getZone());
    }

    /**
     * Get this object as a MutableDateTime using ISOChronology in the same zone.
     *
     * @return a MutableDateTime using the same millis with ISOChronology
     */
    public MutableDateTime toMutableDateTimeISO() {
        return new MutableDateTime(getMillis(), ISOChronology.getInstance(getZone()));
    }

    /**
     * Get this object as a MutableDateTime using the same chronology but a different zone.
     * 
     * @param zone time zone to apply, or default if null
     * @return a MutableDateTime using the same millis
     */
    public MutableDateTime toMutableDateTime(DateTimeZone zone) {
        Chronology chrono = DateTimeUtils.getChronology(getChronology());
        chrono = chrono.withZone(zone);
        return new MutableDateTime(getMillis(), chrono);
    }

    /**
     * Get this object as a MutableDateTime using the given chronology and its zone.
     * 
     * @param chronology chronology to apply, or ISOChronology if null
     * @return a MutableDateTime using the same millis
     */
    public MutableDateTime toMutableDateTime(Chronology chronology) {
        return new MutableDateTime(getMillis(), chronology);
    }

    //-----------------------------------------------------------------------
    /**
     * Get the date time as a <code>java.util.Date</code>.
     * <p>
     * The <code>Date</code> object created has exactly the same millisecond
     * instant as this object.
     *
     * @return a Date initialised with this datetime
     */
    public Date toDate() {
        return new Date(getMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this object with the specified object for equality based
     * on the millisecond instant, chronology and time zone.
     * <p>
     * Two objects which represent the same instant in time, but are in
     * different time zones (based on time zone id), will be considered to
     * be different. Only two objects with the same {@link DateTimeZone},
     * {@link Chronology} and instant are equal.
     * <p>
     * See {@link #isEqual(ReadableInstant)} for an equals method that
     * ignores the Chronology and time zone.
     * <p>
     * All ReadableInstant instances are accepted.
     *
     * @param readableInstant  a readable instant to check against
     * @return true if millisecond and chronology are equal, false if
     *  not or the instant is null or of an incorrect type
     */
    public boolean equals(Object readableInstant) {
        // must be to fulfil ReadableInstant contract
        if (this == readableInstant) {
            return true;
        }
        if (readableInstant instanceof ReadableInstant == false) {
            return false;
        }
        ReadableInstant otherInstant = (ReadableInstant) readableInstant;
        return
            getMillis() == otherInstant.getMillis() &&
            FieldUtils.equals(getChronology(), otherInstant.getChronology());
    }

    /**
     * Gets a hash code for the instant as defined in <code>ReadableInstant</code>.
     *
     * @return a suitable hash code
     */
    public int hashCode() {
        // must be to fulfil ReadableInstant contract
        return
            ((int) (getMillis() ^ (getMillis() >>> 32))) +
            (getChronology().hashCode());
    }

    /**
     * Compares this object with the specified object for ascending
     * millisecond instant order. This ordering is inconsistent with
     * equals, as it ignores the Chronology.
     * <p>
     * All ReadableInstant instances are accepted.
     *
     * @param instant  a readable instant to check against
     * @return negative value if this is less, 0 if equal, or positive value if greater
     * @throws NullPointerException if the object is null
     * @throws ClassCastException if the object type is not supported
     */
    public int compareTo(Object instant) {
        if (this == instant) {
            return 0;
        }
        
        ReadableInstant otherInstant = (ReadableInstant) instant;
        
        long otherMillis = otherInstant.getMillis();
        long thisMillis = getMillis();
        
        // cannot do (thisMillis - otherMillis) as can overflow
        if (thisMillis == otherMillis) {
            return 0;
        }
        if (thisMillis < otherMillis) {
            return -1;
        } else {
            return 1;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Is this instant after the millisecond instant passed in
     * comparing solely by millisecond.
     *
     * @param instant  a millisecond instant to check against
     * @return true if this instant is after the instant passed in
     */
    public boolean isAfter(long instant) {
        return (getMillis() > instant);
    }

    /**
     * Is this instant after the current instant
     * comparing solely by millisecond.
     * 
     * @return true if this instant is after the current instant
     */
    public boolean isAfterNow() {
        return isAfter(DateTimeUtils.currentTimeMillis());
    }

    /**
     * Is this instant after the instant passed in
     * comparing solely by millisecond.
     *
     * @param instant  an instant to check against, null means now
     * @return true if the instant is after the instant passed in
     */
    public boolean isAfter(ReadableInstant instant) {
        long instantMillis = DateTimeUtils.getInstantMillis(instant);
        return isAfter(instantMillis);
    }

    //-----------------------------------------------------------------------
    /**
     * Is this instant before the millisecond instant passed in
     * comparing solely by millisecond.
     *
     * @param instant  a millisecond instant to check against
     * @return true if this instant is before the instant passed in
     */
    public boolean isBefore(long instant) {
        return (getMillis() < instant);
    }

    /**
     * Is this instant before the current instant
     * comparing solely by millisecond.
     * 
     * @return true if this instant is before the current instant
     */
    public boolean isBeforeNow() {
        return isBefore(DateTimeUtils.currentTimeMillis());
    }

    /**
     * Is this instant before the instant passed in
     * comparing solely by millisecond.
     *
     * @param instant  an instant to check against, null means now
     * @return true if the instant is before the instant passed in
     */
    public boolean isBefore(ReadableInstant instant) {
        long instantMillis = DateTimeUtils.getInstantMillis(instant);
        return isBefore(instantMillis);
    }

    //-----------------------------------------------------------------------
    /**
     * Is this instant equal to the millisecond instant passed in
     * comparing solely by millisecond.
     *
     * @param instant  a millisecond instant to check against
     * @return true if this instant is before the instant passed in
     */
    public boolean isEqual(long instant) {
        return (getMillis() == instant);
    }

    /**
     * Is this instant equal to the current instant
     * comparing solely by millisecond.
     * 
     * @return true if this instant is before the current instant
     */
    public boolean isEqualNow() {
        return isEqual(DateTimeUtils.currentTimeMillis());
    }

    /**
     * Is this instant equal to the instant passed in
     * comparing solely by millisecond.
     *
     * @param instant  an instant to check against, null means now
     * @return true if the instant is equal to the instant passed in
     */
    public boolean isEqual(ReadableInstant instant) {
        long instantMillis = DateTimeUtils.getInstantMillis(instant);
        return isEqual(instantMillis);
    }

    //-----------------------------------------------------------------------
    /**
     * Output the date time in ISO8601 format (yyyy-MM-ddTHH:mm:ss.SSSZZ).
     * 
     * @return ISO8601 time formatted string.
     */
    public String toString() {
        return ISODateTimeFormat.dateTime().print(this);
    }

    //-----------------------------------------------------------------------
    /**
     * Uses the specified formatter to convert this partial to a String.
     *
     * @param formatter  the formatter to use, null means use <code>toString()</code>.
     * @return the formatted string
     * @since 1.1
     */
    public String toString(DateTimeFormatter formatter) {
        if (formatter == null) {
            return toString();
        }
        return formatter.print(this);
    }

}
