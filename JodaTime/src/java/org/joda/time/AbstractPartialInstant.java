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
import java.util.Locale;

import org.joda.time.chrono.iso.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.InstantConverter;
import org.joda.time.format.DateTimeFormat;

/**
 * AbstractPartialInstant provides the common behaviour for partial instant
 * classes.
 * <p>
 * This class should generally not be used directly by API users. The 
 * {@link PartialInstant} interface should be used when different 
 * kinds of partial instants are to be referenced.
 * <p>
 * AbstractPartialInstant subclasses may be mutable and not thread-safe.
 *
 * @author Brian S O'Neill
 */
public abstract class AbstractPartialInstant extends AbstractInstant
    implements PartialInstant, Serializable {

    static final long serialVersionUID = -8202208243769478085L;

    /**
     * Converts the given instant to local time.
     *
     * @param instant instant to convert
     * @param original original chronology
     * @param chronoUTC chronology with no time zone
     */
    private static long toLocalTime(long instant, Chronology original, Chronology chronoUTC) {
        if (original != chronoUTC) {
            DateTimeZone zone = original.getDateTimeZone();
            if (zone != null) {
                instant += zone.getOffset(instant);
            }
        }
        return instant;
    }

    /**
     * Returns a chronology that is UTC, defaulting to ISO if given chronology
     * is null.
     */
    private static Chronology selectChronologyUTC(final Chronology chronology) {
        if (chronology == null) {
            return ISOChronology.getInstanceUTC();
        } else {
            Chronology utc = chronology.withUTC();
            if (utc != null) {
                return utc;
            }
            DateTimeZone zone = chronology.getDateTimeZone();
            if (zone == null || zone == DateTimeZone.UTC) {
                return chronology;
            }
            throw new IllegalArgumentException("Chronology does not support UTC: " + chronology);
        }
    }

    private Chronology iChronology;

    /** The millis from 1970-01-01T00:00:00Z */
    private long iMillis;

    // Constructors
    //-----------------------------------------------------------------------
    /**
     * Constructs with the current instant in the default time zone.
     */
    protected AbstractPartialInstant() {
        this(ISOChronology.getInstance());
    }

    /**
     * Constructs with the current instant in the given time zone.
     *
     * @param zone  the time zone, null means default zone
     */
    protected AbstractPartialInstant(DateTimeZone zone) {
        this(ISOChronology.getInstance(zone));
    }

    /**
     * Constructs with the current instant in the time zone of the given
     * chronology.
     *
     * @param chronology  the chronology, null means ISOChronology in default zone
     */
    protected AbstractPartialInstant(final Chronology chronology) {
        iChronology = selectChronologyUTC(chronology);
        iMillis = resetUnsupportedFields
            (toLocalTime(System.currentTimeMillis(), chronology, iChronology));
    }

    /**
     * Constructs with milliseconds from 1970-01-01T00:00:00Z.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     */
    protected AbstractPartialInstant(long instant) {
        iChronology = ISOChronology.getInstanceUTC();
        iMillis = resetUnsupportedFields(instant);
    }

    /**
     * Constructs with milliseconds from 1970-01-01T00:00:00Z. If the time zone
     * of the given chronology is not null or UTC, then the instant is
     * converted to local time.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @param chronology  the chronology, null means ISOChronology
     */
    protected AbstractPartialInstant(long instant, final Chronology chronology) {
        iChronology = selectChronologyUTC(chronology);
        iMillis = resetUnsupportedFields
            (toLocalTime(instant, chronology, iChronology));
    }

    /**
     * Constructs an instance from an Object that represents a date.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  the datetime object, must not be null
     * @throws IllegalArgumentException if the date is null
     */
    protected AbstractPartialInstant(Object instant) {
        InstantConverter converter = ConverterManager.getInstance().getInstantConverter(instant);
        final Chronology chronology = converter.getChronology(instant);
        iChronology = selectChronologyUTC(chronology);
        iMillis = resetUnsupportedFields
            (toLocalTime(converter.getInstantMillis(instant), chronology, iChronology));
    }

    /**
     * Constructs an instance from an Object that represents a date, using the
     * specified chronology.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  the datetime object, must not be null
     * @param chronology  the chronology, must not be null
     * @throws IllegalArgumentException if the date or chronology is null
     */
    protected AbstractPartialInstant(Object instant, final Chronology chronology) {
        iChronology = selectChronologyUTC(chronology);
        InstantConverter converter = ConverterManager.getInstance().getInstantConverter(instant);
        iMillis = resetUnsupportedFields
            (toLocalTime(converter.getInstantMillis(instant),
                         converter.getChronology(instant), iChronology));
    }

    /**
     * Get the value as the number of milliseconds since the epoch,
     * 1970-01-01T00:00:00Z, with unsupported field values reset.
     *
     * @return the value as milliseconds
     */
    public final long getMillis() {
        return iMillis;
    }

    /**
     * Get the value as the number of milliseconds since the epoch,
     * 1970-01-01T00:00:00Z, with unsupported field values and time zone filled
     * in by the given base instant.
     *
     * @param base source of missing fields
     * @return the value as milliseconds
     */
    public final long getMillis(ReadableInstant base) {
        if (base == null || isMatchingType(base)) {
            return getMillis();
        }
        return getMillis(base, base.getDateTimeZone());
    }

    /**
     * Get the value as the number of milliseconds since the epoch,
     * 1970-01-01T00:00:00Z, with unsupported field values filled in by the
     * given base instant.
     *
     * @param base source of missing fields
     * @param zone override the base time zone, null implies override with no
     * time zone
     * @return the value as milliseconds
     */
    public final long getMillis(ReadableInstant base, DateTimeZone zone) {
        if (base == null || isMatchingType(base)) {
            return getMillis();
        }

        long millis = getMillis();
        long baseMillis = base.getMillis();

        DateTimeZone baseZone = base.getDateTimeZone();
        if (baseZone != null) {
            // Strip zone from base such that sum can be performed.
            baseMillis += baseZone.getOffset(baseMillis);
        }

        millis += resetSupportedFields(baseMillis);

        if (zone != null) {
            // Apply new zone.
            millis -= zone.getOffsetFromLocal(millis);
        }

        return millis;
    }

    /**
     * Gets the chronology of the instant, null if not applicable. The time
     * zone of the chronology is either null or UTC.
     * <p>
     * The {@link Chronology} provides conversion from the millisecond
     * value to meaningful fields in a particular calendar system.
     * 
     * @return the Chronology
     */
    public final Chronology getChronology() {
        return iChronology;
    }

    /**
     * Returns the lower limiting field, where the lower limit field itself is
     * supported. In other words, for the range described by the lower and
     * upper limits, the lower limit is inclusive.
     *
     * @return lower limit or null if none
     */
    public abstract DateTimeField getLowerLimit();

    /**
     * Returns the upper limiting field, where the upper limit field itself is
     * not supported. In other words, for the range described by the lower and
     * upper limits, the upper limit is exclusive.
     *
     * @return upper limit or null if none
     */
    public abstract DateTimeField getUpperLimit();

    /**
     * Returns true if the given instant is a PartialDateTime that supports the
     * same exact set of fields. Implementations may simply do the following:
     * <pre>
     *     return instant instanceof &lt;this class&gt;;
     * </pre>
     *
     * @return true if instant is same type as this
     */
    public abstract boolean isMatchingType(ReadableInstant instant);

    /**
     * Returns the given instant, except with lower and upper limits
     * applied. Field values are reset below the lower limit and at or above
     * the upper limit.
     *
     * @param instant milliseconds from 1970-01-01T00:00:00
     */
    public final long resetUnsupportedFields(long instant) {
        DateTimeField field;
        if ((field = getLowerLimit()) != null) {
            instant = field.roundFloor(instant);
        }
        if ((field = getUpperLimit()) != null) {
            instant = field.remainder(instant);
        }
        return instant;
    }

    /**
     * Returns the given instant, except with lower and upper limits
     * applied. Field values are reset below the upper limit and at or above
     * the lower limit.
     *
     * @param instant milliseconds from 1970-01-01T00:00:00
     */
    public final long resetSupportedFields(final long instant) {
        long newInstant = 0;
        DateTimeField field;
        if ((field = getUpperLimit()) != null) {
            newInstant = field.roundFloor(instant);
        }
        if ((field = getLowerLimit()) != null) {
            newInstant += field.remainder(instant);
        }
        return newInstant;
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this object with the specified object for equality based
     * on the millisecond instant, the Chronology, and the limiting fields.
     * <p>
     * To compare two instants for absolute time (ie. UTC milliseconds ignoring
     * the chronology), use {@link #isEqual(ReadableInstant)} or
     * {@link #compareTo(Object)}.
     *
     * @param readableInstant  a readable instant to check against
     * @return true if millisecond and chronology are equal, false if
     *  not or the instant is null or of an incorrect type
     */
    public boolean equals(Object readableInstant) {
        return super.equals(readableInstant) && isMatchingType((ReadableInstant) readableInstant);
    }

    private static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    /**
     * Gets a hash code for the instant that is compatable with the 
     * equals method.
     *
     * @return a suitable hash code
     */
    public int hashCode() {
        int result = super.hashCode();
        DateTimeField field;
        if ((field = getLowerLimit()) != null) {
            result = 31 * result + field.hashCode();
        }
        if ((field = getUpperLimit()) != null) {
            result = 31 * result + field.hashCode();
        }
        return result;
    }

    //-----------------------------------------------------------------------
    /**
     * Get the value as a String in a recognisable ISO8601 format, only
     * displaying supported fields.
     * <p>
     * The string output is in ISO8601 format to enable the String
     * constructor to correctly parse it.
     *
     * @return the value as an ISO8601 string
     */
    public abstract String toString();

    /**
     * Set the value as the number of milliseconds since the epoch,
     * 1970-01-01T00:00:00Z.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     *
     * @param instant  the milliseconds since 1970-01-01T00:00:00Z to set the
     * instant to
     * @throws IllegalArgumentException if the value is invalid
     */
    protected void setMillis(long instant) {
        iMillis = resetUnsupportedFields(instant);
    }

    /**
     * Set the value from an Object representing an instant.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     * 
     * @param instant  an object representing an instant
     * @throws IllegalArgumentException if the value is invalid
     */
    protected void setMillis(Object instant) {
        // Don't set iMillis directly, as it may provide a backdoor to
        // immutable subclasses.
        if (instant instanceof ReadableInstant) {
            setMillis(((ReadableInstant) instant).getMillis());
        } else {
            InstantConverter converter = ConverterManager.getInstance().getInstantConverter(instant);
            setMillis(converter.getInstantMillis(instant));
        }
    }

    /**
     * Set the chronology of the instant.
     * <p>
     * Subclasses that wish to be immutable should override this method with an
     * empty implementation that is protected and final. This also ensures that
     * all lower subclasses are also immutable.
     * 
     * @param chronology  the chronology to use, null means ISOChronology/UTC
     * @throws IllegalArgumentException if the value is invalid
     */
    protected void setChronology(Chronology chronology) {
        iChronology = selectChronologyUTC(chronology);
    }

}
