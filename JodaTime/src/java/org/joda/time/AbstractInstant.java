/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-03 Stephen Colebourne.
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
package org.joda.time;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * AbstractInstant provides the common behaviour for immutable time classes.
 * <p>
 * This class has no concept of a chronology, all methods work on the
 * millisecond instant.
 * <p>
 * This class should generally not be used directly by API users. The 
 * {@link ReadableInstant} interface should be used when different 
 * kinds of date/time objects are to be referenced.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public abstract class AbstractInstant
        implements ReadableInstant, Serializable {

    /**
     * Constructor.
     */
    public AbstractInstant() {
        super();
    }

    /**
     * Gets the time zone of the datetime from the chronology.
     * 
     * @return the DateTimeZone that the datetime is using
     */
    public final DateTimeZone getDateTimeZone() {
        Chronology chrono = getChronology();
        return chrono != null ? chrono.getDateTimeZone() : null;
    }

    /**
     * Creates a new instance of this class.
     * <p>
     * The returned object will be a new instance of the implementation.
     * Immutable subclasses may return <code>this</code> if appropriate.
     *
     * @param millis  the new millis, from 1970-01-01T00:00:00Z
     * @param chrono  the new chronology
     * @return a new instance of this class
     * @throws IllegalArgumentException if the chronology is null
     */
    protected abstract ReadableInstant create(long millis, Chronology chrono);
    
    // Accessors
    //-----------------------------------------------------------------------
    /**
     * Get the value of the specified field.
     * <p>
     * This could be used to get a field using a different Chronology.
     * For example:
     * <pre>
     * Instant dt = new Instant();
     * int gjYear = dt.get(ISOChronology.getInstance().year());
     * </pre>
     * 
     * @param field  the DateTimeField subclass to use
     * @return the value
     * @throws IllegalArgumentException if the field is null
     */
    public final int get(DateTimeField field) {
        if (field == null) {
            throw new IllegalArgumentException("The DateTimeField must not be null");
        }
        return field.get(getMillis());
    }

    // Updates
    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this instant with different millis.
     * <p>
     * The returned object will be a new instance of <code>DateTime</code>.
     * Only the millis will change, the chronology and time zone are kept.
     * If the millis is the same, <code>this</code> will be returned.
     *
     * @param newMillis  the new millis, from 1970-01-01T00:00:00Z
     * @return a copy of this instant with different millis
     */
    public final ReadableInstant toCopy(long newMillis) {
        return create(newMillis, getChronology());
    }
    
    /**
     * Gets a copy of this instant with a different chronology.
     * <p>
     * The returned object will be a new instance of <code>DateTime</code>.
     * Only the chronology will change, the millis are kept.
     * If the chronology is the same, <code>this</code> will be returned.
     *
     * @param newChronology  the new chronology
     * @return a copy of this instant with a different chronology
     * @throws IllegalArgumentException if the chronology is null
     */
    public final ReadableInstant toCopy(Chronology newChronology) {
        return create(getMillis(), newChronology);
    }
    
    // Conversion
    //-----------------------------------------------------------------------
    /**
     * Get this object as an Instant.
     * 
     * @return an Instant using the same millis
     */
    public final Instant toInstant() {
        if (this instanceof Instant) {
            return (Instant) this;
        }
        return new Instant(this);
    }

    /**
     * Get the date time as a <code>java.util.Date</code>.
     * 
     * @return a Date initialised with this datetime
     */
    public final Date toDate() {
        return new Date(getMillis());
    }

    /**
     * Get the date time as a <code>java.util.Calendar</code>.
     * The locale is passed in, enabling Calendar to select the correct
     * localized subclass.
     * 
     * @param locale  the locale to get the Calendar for
     * @return a localized Calendar initialised with this datetime
     * @throws IllegalArgumentException if the locale is null
     */
    public final Calendar toCalendar(Locale locale) {
        if (locale == null) {
            throw new IllegalArgumentException("The Locale must not be null");
        }
        Calendar cal = Calendar.getInstance(locale);
        cal.setTime(toDate());
        return cal;
    }

    /**
     * Get the date time as a <code>java.util.GregorianCalendar</code>.
     * 
     * @return a GregorianCalendar initialised with this datetime
     */
    public final GregorianCalendar toGregorianCalendar() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(toDate());
        return cal;
    }

    // Basics
    //-----------------------------------------------------------------------
    /**
     * Compares this object with the specified object for equality based
     * on the millisecond instant and the Chronology.
     * <p>
     * All ReadableInstant instances are accepted.
     * <p>
     * See {@link #isEqual(ReadableInstant)} for an equals method that
     * ignores the Chronology.
     *
     * @param readableInstant  a readable instant to check against
     * @return true if millisecond and chronology are equal, false if
     *  not or the instant is null or of an incorrect type
     */
    public final boolean equals(Object readableInstant) {
        if (readableInstant instanceof ReadableInstant) {
            ReadableInstant otherInstant = (ReadableInstant) readableInstant;
            if (getMillis() == otherInstant.getMillis()) {
                Chronology chrono = getChronology();
                if (chrono == otherInstant.getChronology()) {
                    return true;
                }
                if (chrono != null && chrono.equals(otherInstant.getChronology())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets a hash code for the instant that is compatable with the 
     * equals method.
     *
     * @return a suitable hash code
     */
    public final int hashCode() {
        // following rules in [Bloch02]
        int result = 317;
        result = 59 * result + ((int) (getMillis() ^ (getMillis() >>> 32)));
        result = 59 * result + (getChronology() == null ? 0 : getChronology().hashCode());
        return result;
    }

    /**
     * Compares this object with the specified object for ascending
     * millisecond instant order. This ordering is inconsistent with
     * equals, as it ignores the Chronology.
     * <p>
     * All ReadableInstant instances are accepted.
     *
     * @param readableInstant  a readable instant to check against
     * @return -1 if this is less, 0 if equal or +1 if greater
     * @throws NullPointerException if the object is null
     * @throws ClassCastException if the object type is not supported
     */
    public final int compareTo(Object readableInstant) {
        ReadableInstant otherInstant = (ReadableInstant) readableInstant;
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

    /**
     * Is the millisecond value after the millisecond passed in.
     *
     * @param readableInstant  an instant to check against
     * @return true if the instant is after the instant passed in
     * @throws IllegalArgumentException if the object is null
     */
    public final boolean isAfter(ReadableInstant readableInstant) {
        if (readableInstant == null) {
            throw new IllegalArgumentException("The instant must not be null");
        }
        return (getMillis() > readableInstant.getMillis());
    }

    /**
     * Is the millisecond value before the millisecond passed in.
     *
     * @param readableInstant  an instant to check against
     * @return true if the instant is before the instant passed in
     * @throws IllegalArgumentException if the object is null
     */
    public final boolean isBefore(ReadableInstant readableInstant) {
        if (readableInstant == null) {
            throw new IllegalArgumentException("The instant must not be null");
        }
        return (getMillis() < readableInstant.getMillis());
    }

    /**
     * Is the millisecond value equal to the millisecond passed in.
     *
     * @param readableInstant  an instant to check against
     * @return true if the instant is equal to the instant passed in
     * @throws IllegalArgumentException if the object is null
     */
    public final boolean isEqual(ReadableInstant readableInstant) {
        if (readableInstant == null) {
            throw new IllegalArgumentException("The instant must not be null");
        }
        return (getMillis() == readableInstant.getMillis());
    }

    // Output    
    //-----------------------------------------------------------------------
    /**
     * Get the value as a String in a recognisable ISO8601 format.
     * <p>
     * The string output is in ISO8601 format to enable the String
     * constructor to correctly parse it.
     *
     * @return the value as an ISO8601 string
     */
    public abstract String toString();

}
