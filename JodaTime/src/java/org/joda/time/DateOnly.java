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

import org.joda.time.chrono.iso.ISOChronology;
// Import for @link support
import org.joda.time.convert.ConverterManager;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.property.DateOnlyFieldProperty;

/**
 * DateOnly is the basic implementation of a date only class supporting
 * chronologies. It holds the date as milliseconds from the Java epoch of
 * 1970-01-01. The time component and time zone is fixed at T00:00:00Z.
 * <p>
 * This class uses a Chronology internally. The Chronology determines how the
 * millisecond instant value is converted into the date time fields.
 * The default Chronology is <code>ISOChronology</code> which is the agreed
 * international standard and compatable with the modern Gregorian calendar.
 *
 * <p>Each individual field can be queried in two ways:
 * <ul>
 * <li><code>getYear()</code>
 * <li><code>year().get()</code>
 * </ul>
 * The second technique also provides access to other useful methods on the
 * field:
 * <ul>
 * <li>numeric value
 * <li>text value
 * <li>short text value
 * <li>maximum value
 * <li>minimum value
 * </ul>
 * <p>
 * DateOnly is thread-safe and immutable, provided that the Chronology is as
 * well. All standard Chronology classes supplied are thread-safe and
 * immutable.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 * @see MutableDateOnly
 * @see TimeOnly
 * @see DateTime
 */
public class DateOnly extends AbstractPartialInstant implements Serializable {

    static final long serialVersionUID = -5796551185494585279L;

    // Constructors
    //-----------------------------------------------------------------------
    /**
     * Constructs a DateOnly to the current date in the default time zone.
     */
    public DateOnly() {
        super();
    }

    /**
     * Constructs a DateOnly to the current date in the given time zone.
     *
     * @param zone  the time zone, null means default zone
     */
    public DateOnly(DateTimeZone zone) {
        super(zone);
    }

    /**
     * Constructs a DateOnly to the current date in the time zone of the given
     * chronology.
     *
     * @param chronology  the chronology, null means ISOChronology in default zone
     */
    public DateOnly(Chronology chronology) {
        super(chronology);
    }

    /**
     * Constructs a DateOnly set to the milliseconds from 1970-01-01T00:00:00Z.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     */
    public DateOnly(long instant) {
        super(instant);
    }

    /**
     * Constructs a DateOnly set to the milliseconds from
     * 1970-01-01T00:00:00Z. If the time zone of the given chronology is not
     * null or UTC, then the instant is converted to local time.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @param chronology  the chronology, null means ISOChronology
     */
    public DateOnly(long instant, Chronology chronology) {
        super(instant, chronology);
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
    public DateOnly(Object instant) {
        super(instant);
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
    public DateOnly(Object instant, Chronology chronology) {
        super(instant, chronology);
    }

    /**
     * Constructs an instance from date field values using
     * <code>ISOChronology</code>.
     *
     * @param year  the year
     * @param monthOfYear  the month of the year
     * @param dayOfMonth  the day of the month
     */
    public DateOnly(
            final int year,
            final int monthOfYear,
            final int dayOfMonth) {

        super(ISOChronology.getInstanceUTC()
              .getDateOnlyMillis(year, monthOfYear, dayOfMonth),
              ISOChronology.getInstanceUTC());
    }

    /**
     * Constructs an instance from date field values
     * using the specified chronology.
     * <p>
     * If the chronology is null, <code>ISOChronology</code>
     * is used.
     *
     * @param year  the year
     * @param monthOfYear  the month of the year
     * @param dayOfMonth  the day of the month
     * @param chronology  the chronology, null means ISOChronology
     */
    public DateOnly(
            final int year,
            final int monthOfYear,
            final int dayOfMonth,
            Chronology chronology) {

        super((chronology == null ? (chronology = ISOChronology.getInstanceUTC()) : chronology)
              .getDateOnlyMillis(year, monthOfYear, dayOfMonth),
              chronology);
    }

    /**
     * Gets a copy of this instant with different millis.
     * <p>
     * The returned object will be a new instance of the same implementation type.
     * Only the millis will change, the chronology is kept.
     * Immutable subclasses may return <code>this</code> if appropriate.
     *
     * @param newMillis  the new millis, from 1970-01-01T00:00:00Z
     * @return a copy of this instant with different millis
     */
    public ReadableInstant toCopy(long newMillis) {
        newMillis = resetUnsupportedFields(newMillis);
        return newMillis == getMillis() ? this : new DateOnly(newMillis, getChronology());
    }
    
    /**
     * Gets a copy of this instant with a different chronology.
     * <p>
     * The returned object will be a new instance of the same implementation type.
     * Only the chronology will change, the millis are kept.
     * Immutable subclasses may return <code>this</code> if appropriate.
     *
     * @param newChronology  the new chronology
     * @return a copy of this instant with a different chronology
     * @throws IllegalArgumentException if the chronology is null
     */
    public ReadableInstant toCopy(Chronology newChronology) {
        if (newChronology == null) {
            throw new IllegalArgumentException("The Chronology must not be null");
        }
        newChronology = newChronology.withUTC();
        return newChronology == getChronology() ? this : new DateOnly(getMillis(), newChronology);
    }

    /**
     * Returns the lower limiting field, dayOfYear.
     *
     * @return dayOfYear field
     */
    public final DateTimeField getLowerLimit() {
        return getChronology().dayOfYear();
    }

    /**
     * Returns the upper limiting field, null.
     *
     * @return null
     */
    public final DateTimeField getUpperLimit() {
        return null;
    }

    // Date field access
    //-----------------------------------------------------------------------
    /**
     * Get the era field value.
     * 
     * @return the era
     */
    public final int getEra() {
        return getChronology().era().get(getMillis());
    }

    /**
     * Get the year of era field value.
     * 
     * @return the year of era
     */
    public final int getCenturyOfEra() {
        return getChronology().centuryOfEra().get(getMillis());
    }

    /**
     * Get the year of era field value.
     * 
     * @return the year of era
     */
    public final int getYearOfEra() {
        return getChronology().yearOfEra().get(getMillis());
    }

    /**
     * Get the year of century field value.
     * 
     * @return the year of century
     */
    public final int getYearOfCentury() {
        return getChronology().yearOfCentury().get(getMillis());
    }

    /**
     * Get the year field value.
     * 
     * @return the year
     */
    public final int getYear() {
        return getChronology().year().get(getMillis());
    }

    /**
     * Get the weekyear field value.
     * 
     * @return the year of a week based year
     */
    public final int getWeekyear() {
        return getChronology().weekyear().get(getMillis());
    }

    /**
     * Get the month of year field value.
     * 
     * @return the month of year
     */
    public final int getMonthOfYear() {
        return getChronology().monthOfYear().get(getMillis());
    }

    /**
     * Get the week of weekyear field value.
     * 
     * @return the week of a week based year
     */
    public final int getWeekOfWeekyear() {
        return getChronology().weekOfWeekyear().get(getMillis());
    }

    /**
     * Get the day of year field value.
     * 
     * @return the day of year
     */
    public final int getDayOfYear() {
        return getChronology().dayOfYear().get(getMillis());
    }

    /**
     * Get the day of month field value.
     * <p>
     * The values for the day of month are defined in {@link DateTimeConstants}.
     * 
     * @return the day of month
     */
    public final int getDayOfMonth() {
        return getChronology().dayOfMonth().get(getMillis());
    }

    /**
     * Get the day of week field value.
     * <p>
     * The values for the day of week are defined in {@link DateTimeConstants}.
     * 
     * @return the day of week
     */
    public final int getDayOfWeek() {
        return getChronology().dayOfWeek().get(getMillis());
    }


    // Properties
    //-----------------------------------------------------------------------
    /**
     * Get the day of week property.
     * <p>
     * The values for day of week are defined in {@link DateTimeConstants}.
     * 
     * @return the day of week property
     */
    public final DateOnlyFieldProperty dayOfWeek() {
        return new DateOnlyFieldProperty(this, getChronology().dayOfWeek());
    }

    /**
     * Get the day of month property.
     * 
     * @return the day of month property
     */
    public final DateOnlyFieldProperty dayOfMonth() {
        return new DateOnlyFieldProperty(this, getChronology().dayOfMonth());
    }

    /**
     * Get the day of year property.
     * 
     * @return the day of year property
     */
    public final DateOnlyFieldProperty dayOfYear() {
        return new DateOnlyFieldProperty(this, getChronology().dayOfYear());
    }

    /**
     * Get the week of a week based year property.
     * 
     * @return the week of a week based year property
     */
    public final DateOnlyFieldProperty weekOfWeekyear() {
        return new DateOnlyFieldProperty(this, getChronology().weekOfWeekyear());
    }

    /**
     * Get the year of a week based year property.
     * 
     * @return the year of a week based year property
     */
    public final DateOnlyFieldProperty weekyear() {
        return new DateOnlyFieldProperty(this, getChronology().weekyear());
    }

    /**
     * Get the month of year property.
     * 
     * @return the month of year property
     */
    public final DateOnlyFieldProperty monthOfYear() {
        return new DateOnlyFieldProperty(this, getChronology().monthOfYear());
    }

    /**
     * Get the year property.
     * 
     * @return the year property
     */
    public final DateOnlyFieldProperty year() {
        return new DateOnlyFieldProperty(this, getChronology().year());
    }

    /**
     * Get the year of era property.
     * 
     * @return the year of era property
     */
    public final DateOnlyFieldProperty yearOfEra() {
        return new DateOnlyFieldProperty(this, getChronology().yearOfEra());
    }

    /**
     * Get the year of century property.
     * 
     * @return the year of era property
     */
    public final DateOnlyFieldProperty yearOfCentury() {
        return new DateOnlyFieldProperty(this, getChronology().yearOfCentury());
    }

    /**
     * Get the century of era property.
     * 
     * @return the year of era property
     */
    public final DateOnlyFieldProperty centuryOfEra() {
        return new DateOnlyFieldProperty(this, getChronology().centuryOfEra());
    }

    /**
     * Get the era property.
     * 
     * @return the era property
     */
    public final DateOnlyFieldProperty era() {
        return new DateOnlyFieldProperty(this, getChronology().era());
    }

    // Output
    //-----------------------------------------------------------------------
    /**
     * Output the date in ISO8601 date only format (yyyy-MM-dd).
     * 
     * @return ISO8601 date formatted string
     */
    public final String toString() {
        return ISODateTimeFormat.getInstance(getChronology()).date().print(getMillis());
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are
     * immutable.
     */
    protected final void setMillis(long millis) {
    }

    /**
     * Overridden to do nothing, ensuring this class and all subclasses are
     * immutable.
     */
    protected final void setChronology(Chronology chronology) {
    }
    
}
