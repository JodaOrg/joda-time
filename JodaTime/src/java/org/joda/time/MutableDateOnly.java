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

import java.io.Serializable;

import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.DurationConverter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.property.ReadWritableInstantFieldProperty;

/**
 * MutableDateOnly is the basic implementation of a modifiable date only class.
 * It holds the date as milliseconds from the Java epoch of 1970-01-01. The
 * time component and time zone is fixed at T00:00:00Z.
 * <p>
 * This class uses a Chronology internally. The Chronology determines how the
 * millisecond instant value is converted into the date time fields.
 * The default Chronology is <code>ISOChronology</code> which is the agreed
 * international standard and compatable with the modern Gregorian calendar.
 *
 * <p>
 * Each individual field can be accessed in two ways:
 * <ul>
 * <li><code>getYear()</code>
 * <li><code>year().get()</code>
 * </ul>
 * The second technique also provides access to other useful methods on the
 * field:
 * <ul>
 * <li>get numeric value
 * <li>set numeric value
 * <li>add to numeric value
 * <li>add to numeric value wrapping with the field
 * <li>get text vlaue
 * <li>get short text value
 * <li>set text value
 * <li>field maximum value
 * <li>field minimum value
 * </ul>
 *
 * <p>
 * MutableDateOnly is mutable and not thread-safe, unless concurrent threads
 * are not invoking mutator methods.
 *
 * @author Brian S O'Neill
 * @since 1.0
 * @see DateOnly
 */
public class MutableDateOnly extends AbstractPartialInstant
    implements ReadWritableInstant, Cloneable, Serializable {

    static final long serialVersionUID = 7781405642158513308L;

    // Constructors
    //-----------------------------------------------------------------------
    /**
     * Constructs a MutableDateOnly to the current date in the default time zone.
     */
    public MutableDateOnly() {
        super();
    }

    /**
     * Constructs a MutableDateOnly to the current date in the given time zone.
     *
     * @param zone  the time zone, null means default zone
     */
    public MutableDateOnly(DateTimeZone zone) {
        super(zone);
    }

    /**
     * Constructs a MutableDateOnly to the current date in the time zone of the given
     * chronology.
     *
     * @param chronology  the chronology, null means ISOChronology in default zone
     */
    public MutableDateOnly(Chronology chronology) {
        super(chronology);
    }

    /**
     * Constructs a MutableDateOnly set to the milliseconds from 1970-01-01T00:00:00Z.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     */
    public MutableDateOnly(long instant) {
        super(instant);
    }

    /**
     * Constructs a MutableDateOnly set to the milliseconds from
     * 1970-01-01T00:00:00Z. If the time zone of the given chronology is not
     * null or UTC, then the instant is converted to local time.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @param chronology  the chronology, null means ISOChronology
     */
    public MutableDateOnly(long instant, Chronology chronology) {
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
    public MutableDateOnly(Object instant) {
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
    public MutableDateOnly(Object instant, Chronology chronology) {
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
    public MutableDateOnly(
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
    public MutableDateOnly(
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
     *
     * @param newMillis  the new millis, from 1970-01-01T00:00:00Z
     * @return a copy of this instant with different millis
     */
    public ReadableInstant withMillis(long newMillis) {
        return new MutableDateOnly(newMillis, getChronology());
    }
    
    /**
     * Gets a copy of this instant with a different chronology.
     * <p>
     * The returned object will be a new instance of the same implementation type.
     * Only the chronology will change, the millis are kept.
     *
     * @param newChronology  the new chronology
     * @return a copy of this instant with a different chronology
     */
    public ReadableInstant withChronology(Chronology newChronology) {
        newChronology = newChronology == null ? ISOChronology.getInstanceUTC()
            : newChronology.withUTC();
        return new MutableDateOnly(getMillis(), newChronology);
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

    /**
     * Set the value as the number of milliseconds since the epoch,
     * 1970-01-01T00:00:00Z.
     *
     * @param instant  the milliseconds since 1970-01-01T00:00:00Z to set the
     * instant to
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setMillis(long instant) {
        super.setMillis(instant);
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
    public void setMillis(Object instant) {
        super.setMillis(instant);
    }

    /**
     * Set the chronology of the instant.
     * 
     * @param chronology  the chronology to use, null means ISOChronology/UTC
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setChronology(Chronology chronology) {
        super.setChronology(chronology);
    }
    
    /**
     * Since time zones are ignored, this method does nothing.
     *
     * @param zone  ignored
     */
    public void setDateTimeZone(DateTimeZone zone) {
    }

    /**
     * Since time zones are ignored, this method does nothing.
     *
     * @param zone  ignored
     */
    public void moveDateTimeZone(DateTimeZone zone) {
    }

    // Add
    //-----------------------------------------------------------------------
    /**
     * Add an amount of time to the date.
     * 
     * @param duration  the millis to add
     */
    public void add(final long duration) {
        setMillis(getMillis() + duration);
    }

    /**
     * Add an amount of time to the date.
     * 
     * @param duration  duration to add.
     */
    public void add(final ReadableDuration duration) {
        duration.addInto(this, 1);
    }

    /**
     * Add an amount of time to the date.
     * 
     * @param duration  duration to add.
     * @param scalar  direction and amount to add, which may be negative
     */
    public void add(final ReadableDuration duration, final int scalar) {
        duration.addInto(this, scalar);
    }

    /**
     * Add an amount of time to the date.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableDuration, and Long.
     * 
     * @param duration  an object representing a duration
     */
    public void add(final Object duration) {
        if (duration instanceof ReadableDuration) {
            add((ReadableDuration) duration, 1);
        } else {
            DurationConverter converter = ConverterManager.getInstance().getDurationConverter(duration);
            add(converter.getDurationMillis(duration));
        }
    }

    // Field based
    //-----------------------------------------------------------------------
    /**
     * Set a value in the specified field.
     * This could be used to set a field using a different Chronology.
     * For example:
     * <pre>
     * MutableDateOnly date = new MutableDateOnly();
     * date.set(GJChronology.getInstance().year(), 2002);
     * </pre>
     * 
     * @param field  the DateTimeField to use
     * @param value the value
     * @throws NullPointerException if the field is null
     */
    public void set(final DateTimeField field, final int value) {
        setMillis(field.set(getMillis(), value));
    }

    /**
     * Add a value to the specified field.
     * This could be used to set a field using a different Chronology.
     * For example:
     * <pre>
     * MutableDateOnly date = new MutableDateOnly();
     * date.add(GJChronology.getInstance().year(), 2);
     * </pre>
     * 
     * @param field  the DateTimeField to use
     * @param value the value
     * @throws NullPointerException if the field is null
     */
    public void add(final DateTimeField field, final int value) {
        setMillis(field.add(getMillis(), value));
    }

    /**
     * Add a value to the specified field, wrapping within that field.
     * This could be used to set a field using a different Chronology.
     * For example:
     * <pre>
     * MutableDateOnly date = new MutableDateOnly();
     * date.addWrapped(GJChronology.getInstance().monthOfYear(), 6);
     * </pre>
     * 
     * @param field  the DateTimeField to use
     * @param value the value
     * @throws NullPointerException if the field is null
     */
    public void addWrapped(final DateTimeField field, final int value) {
        setMillis(field.addWrapped(getMillis(), value));
    }

    // Date methods
    //-----------------------------------------------------------------------
    /**
     * Set the year to the specified value.
     *
     * @param year  the year
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setYear(final int year) {
        setMillis(getChronology().year().set(getMillis(), year));
    }

    /**
     * Add a number of years to the date.
     *
     * @param years  the years to add
     * @throws IllegalArgumentException if the value is invalid
     */
    public void addYears(final int years) {
        setMillis(getChronology().years().add(getMillis(), years));
    }

    //-----------------------------------------------------------------------
    /**
     * Set the weekyear to the specified value.
     *
     * @param weekyear  the weekyear
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setWeekyear(final int weekyear) {
        setMillis(getChronology().weekyear().set(getMillis(), weekyear));
    }

    /**
     * Add a number of weekyears to the date.
     *
     * @param weekyears  the weekyears to add
     * @throws IllegalArgumentException if the value is invalid
     */
    public void addWeekyears(final int weekyears) {
        setMillis(getChronology().weekyears().add(getMillis(), weekyears));
    }

    //-----------------------------------------------------------------------
    /**
     * Set the month of the year to the specified value.
     *
     * @param monthOfYear  the month of the year
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setMonthOfYear(final int monthOfYear) {
        setMillis(getChronology().monthOfYear().set(getMillis(), monthOfYear));
    }

    /**
     * Add a number of months to the date.
     *
     * @param months  the months to add
     * @throws IllegalArgumentException if the value is invalid
     */
    public void addMonths(final int months) {
        setMillis(getChronology().months().add(getMillis(), months));
    }

    //-----------------------------------------------------------------------
    /**
     * Set the week of weekyear to the specified value.
     *
     * @param weekOfWeekyear the week of the weekyear
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setWeekOfWeekyear(final int weekOfWeekyear) {
        setMillis(getChronology().weekOfWeekyear().set(getMillis(), weekOfWeekyear));
    }

    /**
     * Add a number of weeks to the date.
     *
     * @param weeks  the weeks to add
     * @throws IllegalArgumentException if the value is invalid
     */
    public void addWeeks(final int weeks) {
        setMillis(getChronology().weeks().add(getMillis(), weeks));
    }

    //-----------------------------------------------------------------------
    /**
     * Set the day of year to the specified value.
     *
     * @param dayOfYear the day of the year
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setDayOfYear(final int dayOfYear) {
        setMillis(getChronology().dayOfYear().set(getMillis(), dayOfYear));
    }

    /**
     * Set the day of the month to the specified value.
     *
     * @param dayOfMonth  the day of the month
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setDayOfMonth(final int dayOfMonth) {
        setMillis(getChronology().dayOfMonth().set(getMillis(), dayOfMonth));
    }

    /**
     * Set the day of week to the specified value.
     *
     * @param dayOfWeek  the day of the week
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setDayOfWeek(final int dayOfWeek) {
        setMillis(getChronology().dayOfWeek().set(getMillis(), dayOfWeek));
    }

    /**
     * Add a number of days to the date.
     *
     * @param days  the days to add
     * @throws IllegalArgumentException if the value is invalid
     */
    public void addDays(final int days) {
        setMillis(getChronology().days().add(getMillis(), days));
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

    // Setters
    //-----------------------------------------------------------------------
    /**
     * Set the date from milliseconds.
     *
     * @param instant  milliseconds from 1970-01-01T00:00:00Z, time part ignored
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setDate(final long instant) {
        setMillis(instant);
    }

    /**
     * Set the date from an object representing an instant.
     * <p>
     * The recognised object types are defined in {@link ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  an object representing an instant, time part ignored
     * @throws IllegalArgumentException if the object is null or invalid
     */
    public void setDate(final Object instant) {
        setMillis(instant);
    }

    /**
     * Set the date from fields.
     *
     * @param year  the year
     * @param monthOfYear  the month of the year
     * @param dayOfMonth  the day of the month
     * @throws IllegalArgumentException if the value is invalid
     */
    public void setDate(
            final int year,
            final int monthOfYear,
            final int dayOfMonth) {
        setMillis(getChronology().getDateOnlyMillis(year, monthOfYear, dayOfMonth));
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
    public final ReadWritableInstantFieldProperty dayOfWeek() {
        return new ReadWritableInstantFieldProperty(this, getChronology().dayOfWeek());
    }

    /**
     * Get the day of month property.
     * 
     * @return the day of month property
     */
    public final ReadWritableInstantFieldProperty dayOfMonth() {
        return new ReadWritableInstantFieldProperty(this, getChronology().dayOfMonth());
    }

    /**
     * Get the day of year property.
     * 
     * @return the day of year property
     */
    public final ReadWritableInstantFieldProperty dayOfYear() {
        return new ReadWritableInstantFieldProperty(this, getChronology().dayOfYear());
    }

    /**
     * Get the week of a week based year property.
     * 
     * @return the week of a week based year property
     */
    public final ReadWritableInstantFieldProperty weekOfWeekyear() {
        return new ReadWritableInstantFieldProperty(this, getChronology().weekOfWeekyear());
    }

    /**
     * Get the year of a week based year property.
     * 
     * @return the year of a week based year property
     */
    public final ReadWritableInstantFieldProperty weekyear() {
        return new ReadWritableInstantFieldProperty(this, getChronology().weekyear());
    }

    /**
     * Get the month of year property.
     * 
     * @return the month of year property
     */
    public final ReadWritableInstantFieldProperty monthOfYear() {
        return new ReadWritableInstantFieldProperty(this, getChronology().monthOfYear());
    }

    /**
     * Get the year property.
     * 
     * @return the year property
     */
    public final ReadWritableInstantFieldProperty year() {
        return new ReadWritableInstantFieldProperty(this, getChronology().year());
    }

    /**
     * Get the year of era property.
     * 
     * @return the year of era property
     */
    public final ReadWritableInstantFieldProperty yearOfEra() {
        return new ReadWritableInstantFieldProperty(this, getChronology().yearOfEra());
    }

    /**
     * Get the year of century property.
     * 
     * @return the year of era property
     */
    public final ReadWritableInstantFieldProperty yearOfCentury() {
        return new ReadWritableInstantFieldProperty(this, getChronology().yearOfCentury());
    }

    /**
     * Get the century of era property.
     * 
     * @return the year of era property
     */
    public final ReadWritableInstantFieldProperty centuryOfEra() {
        return new ReadWritableInstantFieldProperty(this, getChronology().centuryOfEra());
    }

    /**
     * Get the era property.
     * 
     * @return the era property
     */
    public final ReadWritableInstantFieldProperty era() {
        return new ReadWritableInstantFieldProperty(this, getChronology().era());
    }

    // Misc
    //-----------------------------------------------------------------------
    /**
     * Clone this object without having to cast the returned object.
     *
     * @return a clone of the this object.
     */
    public MutableDateOnly copy() {
        return (MutableDateOnly)clone();
    }

    /**
     * Clone this object.
     *
     * @return a clone of this object.
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new InternalError("Clone error");
        }
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

}
