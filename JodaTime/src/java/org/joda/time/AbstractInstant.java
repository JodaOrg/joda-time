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
package org.joda.time;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import org.joda.time.chrono.ISOChronology;

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

    /**
     * Gets the time zone of the datetime from the chronology.
     * 
     * @return the DateTimeZone that the datetime is using
     */
    public final DateTimeZone getDateTimeZone() {
        Chronology chrono = getChronology();
        return (chrono != null ? chrono.getDateTimeZone() : null);
    }

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

    /**
     * Gets a copy of this instant with a different time zone.
     * <p>
     * The returned object will be a new instance of the same implementation
     * type. Only the time zone of the chronology will change, the millis are
     * kept. Immutable subclasses may return <code>this</code> if appropriate.
     *
     * @param newDateTimeZone  the new time zone
     * @return a copy of this instant with a different time zone
     */
    public ReadableInstant withDateTimeZone(DateTimeZone newDateTimeZone) {
        Chronology newChronology = getChronology();
        newChronology = newChronology == null ? ISOChronology.getInstance(newDateTimeZone)
            : newChronology.withDateTimeZone(newDateTimeZone);
        return withChronology(newChronology);
    }

    // Conversion
    //-----------------------------------------------------------------------
    /**
     * Get this object as an Instant.
     * 
     * @return an Instant using the same millis, unless partially specified
     */
    public final Instant toInstant() {
        if (this instanceof Instant) {
            return (Instant) this;
        }
        return new Instant(this);
    }

    /**
     * Get this object as an Instant, using the given base instant to supply
     * missing field values.
     * 
     * @param base source of missing fields
     * @return a new Instant
     */
    public final Instant toInstant(ReadableInstant base) {
        return new Instant(getMillis(base, null));
    }

    /**
     * Get this object as a DateTime.
     * 
     * @return a DateTime using the same millis, unless partially specified
     */
    public final DateTime toDateTime() {
        if (this instanceof DateTime) {
            return (DateTime) this;
        }
        return new DateTime(this);
    }

    /**
     * Get this object as a DateTime.
     * 
     * @param zone time zone to apply, or default if null
     * @return a DateTime using the same millis, unless partially specified
     */
    public final DateTime toDateTime(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        if (this instanceof DateTime && getDateTimeZone() == zone) {
            return (DateTime) this;
        }
        return new DateTime(this, zone);
    }

    /**
     * Get this object as a DateTime.
     * 
     * @param chronology chronology to apply, or ISOChronology if null
     * @return a DateTime using the same millis, unless partially specified
     */
    public final DateTime toDateTime(Chronology chronology) {
        if (chronology == null) {
            chronology = ISOChronology.getInstance();
        }
        if (this instanceof DateTime && getChronology() == chronology) {
            return (DateTime) this;
        }
        return new DateTime(this, chronology);
    }

    /**
     * Convert this object into a DateTime, using the given base instant to
     * supply missing field values and time zone.
     * 
     * @param base source of missing fields
     * @return a new DateTime
     */
    public final DateTime toDateTime(ReadableInstant base) {
        if (base == null) {
            return new DateTime(this);
        }
        DateTimeZone zone = base.getDateTimeZone();
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        return new DateTime(getMillis(base), getChronology().withDateTimeZone(zone));
    }

    /**
     * Convert this object into a DateTime, using the given base instant to
     * supply missing field values.
     * 
     * @param base source of missing fields
     * @param zone  time zone to apply, or default if null
     * @return a new DateTime
     */
    public final DateTime toDateTime(ReadableInstant base, DateTimeZone zone) {
        if (base == null) {
            return new DateTime(this, zone);
        }
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        return new DateTime(getMillis(base, zone), getChronology().withDateTimeZone(zone));
    }

    /**
     * Convert this object into a DateTime, using the given base instant to
     * supply missing field values.
     * 
     * @param base source of missing fields
     * @param chronology  chronology to apply, or ISOChronology if null
     * @return a new DateTime
     */
    public final DateTime toDateTime(ReadableInstant base, Chronology chronology) {
        if (base == null) {
            return new DateTime(this, chronology);
        }
        if (chronology == null) {
            chronology = ISOChronology.getInstance();
        }
        return new DateTime(getMillis(base, chronology.getDateTimeZone()), chronology);
    }

    /**
     * Get this object as a trusted ISO immutable DateTime. The purpose of
     * this method is to guarantee that an externally received DateTime
     * object does not have any backdoors that allow it to be modified.
     * <p>
     * If this object is already a DateTime, whose chronology is
     * {@link ISOChronology ISO}, and the time zone came from the default
     * {@link org.joda.time.tz.Provider provider}, then this object is cast to
     * a DateTime and returned. Otherwise, a new trusted DateTime is returned.
     * 
     * @return a trusted ISO DateTime using the same millis, unless partially specified
     * @throws IllegalArgumentException if the time zone is not trusted, and
     * no matching trusted time zone can be found.
     */
    public final DateTime toTrustedISODateTime() {
        DateTimeZone zone = getDateTimeZone();
        if (zone == null) {
            return new DateTime(this, (Chronology)null);
        }

        DateTimeZone trusted = DateTimeZone.getInstance(zone.getID());
        
        if (zone == trusted && this instanceof DateTime
            && getChronology() instanceof ISOChronology) {
            return (DateTime) this;
        }
        
        return new DateTime(this, ISOChronology.getInstance(trusted));
    }

    // NOTE: Although the toMutableDateTime methods could check to see if this
    // is already a MutableDateTime and return this casted, it makes it too
    // easy to mistakenly modify ReadableDateTime input parameters. Always
    // returning a copy prevents this.

    /**
     * Get this object as a MutableDateTime.
     * 
     * @return a MutableDateTime using the same millis, unless partially specified
     */
    public final MutableDateTime toMutableDateTime() {
        return new MutableDateTime(this);
    }

    /**
     * Get this object as a MutableDateTime.
     * 
     * @param zone time zone to apply, or default if null
     * @return a MutableDateTime using the same millis, unless partially specified
     */
    public final MutableDateTime toMutableDateTime(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        return new MutableDateTime(this, zone);
    }

    /**
     * Get this object as a MutableDateTime.
     * 
     * @param chronology chronology to apply, or ISOChronology if null
     * @return a MutableDateTime using the same millis, unless partially specified
     */
    public final MutableDateTime toMutableDateTime(Chronology chronology) {
        if (chronology == null) {
            chronology = ISOChronology.getInstance();
        }
        return new MutableDateTime(this, chronology);
    }

    /**
     * Convert this object into a MutableDateTime, using the given base instant
     * to supply missing field values and time zone.
     * 
     * @param base source of missing fields
     * @return a new MutableDateTime
     */
    public final MutableDateTime toMutableDateTime(ReadableInstant base) {
        if (base == null) {
            return new MutableDateTime(this);
        }
        DateTimeZone zone = base.getDateTimeZone();
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        return new MutableDateTime(getMillis(base), getChronology().withDateTimeZone(zone));
    }

    /**
     * Convert this object into a MutableDateTime, using the given base instant
     * to supply missing field values.
     * 
     * @param base source of missing fields
     * @param zone  time zone to apply, or default if null
     * @return a new MutableDateTime
     */
    public final MutableDateTime toMutableDateTime(ReadableInstant base, DateTimeZone zone) {
        if (base == null) {
            return new MutableDateTime(this, zone);
        }
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        return new MutableDateTime(getMillis(base, zone), getChronology().withDateTimeZone(zone));
    }

    /**
     * Convert this object into a MutableDateTime, using the given base instant
     * to supply missing field values.
     * 
     * @param base source of missing fields
     * @param chronology  chronology to apply, or ISOChronology if null
     * @return a new MutableDateTime
     */
    public final MutableDateTime toMutableDateTime(ReadableInstant base, Chronology chronology) {
        if (base == null) {
            return new MutableDateTime(this, chronology);
        }
        if (chronology == null) {
            chronology = ISOChronology.getInstance();
        }
        return new MutableDateTime(getMillis(base, chronology.getDateTimeZone()), chronology);
    }

    /**
     * Get this object as a DateOnly.
     * 
     * @return a DateOnly using the same millis, unless partially specified
     */
    public final DateOnly toDateOnly() {
        if (this instanceof DateOnly) {
            return (DateOnly) this;
        }
        return new DateOnly(this);
    }

    /**
     * Get this object as a DateOnly.
     * 
     * @param chronology chronology to apply, or ISOChronology if null
     * @return a DateOnly using the same millis, unless partially specified
     */
    public final DateOnly toDateOnly(Chronology chronology) {
        if (chronology == null) {
            chronology = ISOChronology.getInstance();
        }
        if (this instanceof DateOnly) {
            DateOnly d = (DateOnly) this;
            if (d.getChronology() == chronology.withUTC()) {
                return d;
            }
        }
        return new DateOnly(this, chronology);
    }

    /**
     * Get this object as a TimeOnly.
     * 
     * @return a TimeOnly using the same millis, unless partially specified
     */
    public final TimeOnly toTimeOnly() {
        if (this instanceof TimeOnly) {
            return (TimeOnly) this;
        }
        return new TimeOnly(this);
    }

    /**
     * Get this object as a TimeOnly.
     * 
     * @param chronology chronology to apply, or ISOChronology if null
     * @return a TimeOnly using the same millis, unless partially specified
     */
    public final TimeOnly toTimeOnly(Chronology chronology) {
        if (chronology == null) {
            chronology = ISOChronology.getInstance();
        }
        if (this instanceof TimeOnly) {
            TimeOnly t = (TimeOnly) this;
            if (t.getChronology() == chronology.withUTC()) {
                return t;
            }
        }
        return new TimeOnly(this, chronology);
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
     * @param locale  the locale to get the Calendar for, or default if null
     * @return a localized Calendar initialised with this datetime
     */
    public final Calendar toCalendar(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        DateTimeZone zone = getDateTimeZone();
        Calendar cal;
        if (zone == null) {
            cal = Calendar.getInstance(locale);
        } else {
            cal = Calendar.getInstance(zone.toTimeZone(), locale);
        }
        cal.setTime(toDate());
        return cal;
    }

    /**
     * Get the date time as a <code>java.util.GregorianCalendar</code>.
     * 
     * @return a GregorianCalendar initialised with this datetime
     */
    public final GregorianCalendar toGregorianCalendar() {
        DateTimeZone zone = getDateTimeZone();
        GregorianCalendar cal;
        if (zone == null) {
            cal = new GregorianCalendar();
        } else {
            cal = new GregorianCalendar(zone.toTimeZone());
        }
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
    public boolean equals(Object readableInstant) {
        if (this == readableInstant) {
            return true;
        }
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
    public int hashCode() {
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
     * @return negative value if this is less, 0 if equal, or positive value if greater
     * @throws NullPointerException if the object is null
     * @throws ClassCastException if the object type is not supported
     */
    public final int compareTo(Object readableInstant) {
        if (this == readableInstant) {
            return 0;
        }

        ReadableInstant otherInstant = (ReadableInstant) readableInstant;

        // If instants are partial, then they can use each other to fill in
        // missing fields.
        long otherMillis = otherInstant.getMillis(this);
        long thisMillis = getMillis(otherInstant);

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
        // If instants are partial, then they can use each other to fill in
        // missing fields.
        return (getMillis(readableInstant) > readableInstant.getMillis(this));
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
        // If instants are partial, then they can use each other to fill in
        // missing fields.
        return (getMillis(readableInstant) < readableInstant.getMillis(this));
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
        // If instants are partial, then they can use each other to fill in
        // missing fields.
        return (getMillis(readableInstant) == readableInstant.getMillis(this));
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
