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

import java.util.Locale;

/**
 * Defines the calculation engine for date and time fields.
 * The interface defines a set of methods that manipulate a millisecond datetime
 * with regards to a single field, such as monthOfYear or secondOfMinute.
 * <p>
 * This design is extensible so, if you wish, you can extract a different field from
 * the milliseconds. A number of standard implementations are provided to assist.
 *
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public interface DateTimeField {

    /**
     * Get the name of the field.
     * 
     * @return field name
     */
    String getName();

    /**
     * Returns true if this field is supported.
     * 
     * @return true if this field is supported
     */
    boolean isSupported();

    /**
     * Returns true if the set method is lenient. If so, it accepts values that
     * are out of bounds. For example, a lenient day of month field accepts 32
     * for January, converting it to February 1.
     * 
     * @return true if this field is lenient
     */
    boolean isLenient();

    // Main access API
    //------------------------------------------------------------------------
    /**
     * Get the value of this field from the milliseconds.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to query
     * @return the value of the field, in the units of the field
     */
    int get(long instant);

    /**
     * Get the human-readable, text value of this field from the milliseconds.
     * If the specified locale is null, the default locale is used.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to query
     * @param locale the locale to use for selecting a text symbol, null for default
     * @return the text value of the field
     */
    String getAsText(long instant, Locale locale);

    /**
     * Get the human-readable, text value of this field from the milliseconds.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to query
     * @return the text value of the field
     */
    String getAsText(long instant);

    /**
     * Get the human-readable, short text value of this field from the
     * milliseconds.  If the specified locale is null, the default locale is used.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to query
     * @param locale the locale to use for selecting a text symbol, null for default
     * @return the short text value of the field
     */
    String getAsShortText(long instant, Locale locale);

    /**
     * Get the human-readable, short text value of this field from the
     * milliseconds.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to query
     * @return the short text value of the field
     */
    String getAsShortText(long instant);

    /**
     * Adds a value (which may be negative) to the millis value,
     * overflowing into larger fields if necessary.
     * <p>
     * The value will be added to this field. If the value is too large to be
     * added solely to this field, larger fields will increase as required.
     * Smaller fields should be unaffected, except where the result would be
     * an invalid value for a smaller field. In this case the smaller field is
     * adjusted to be in range.
     * <p>
     * For example, in the ISO chronology:<br>
     * 2000-08-20 add six months is 2001-02-20<br>
     * 2000-08-20 add twenty months is 2002-04-20<br>
     * 2000-08-20 add minus nine months is 1999-11-20<br>
     * 2001-01-31 add one month  is 2001-02-28<br>
     * 2001-01-31 add two months is 2001-03-31<br>
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to add to
     * @param value  the value to add, in the units of the field
     * @return the updated milliseconds
     */
    long add(long instant, int value);

    /**
     * Adds a value (which may be negative) to the millis value,
     * overflowing into larger fields if necessary.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to add to
     * @param value  the long value to add, in the units of the field
     * @return the updated milliseconds
     * @throws IllegalArgumentException if value is too large
     * @see #add(long,int)
     */
    long add(long instant, long value);

    /**
     * Adds a value (which may be negative) to the millis value,
     * wrapping within this field.
     * <p>
     * The value will be added to this field. If the value is too large to be
     * added solely to this field then it wraps. Larger fields are always
     * unaffected. Smaller fields should be unaffected, except where the
     * result would be an invalid value for a smaller field. In this case the
     * smaller field is adjusted to be in range.
     * <p>
     * For example, in the ISO chronology:<br>
     * 2000-08-20 addWrapped six months is 2000-02-20<br>
     * 2000-08-20 addWrapped twenty months is 2000-04-20<br>
     * 2000-08-20 addWrapped minus nine months is 2000-11-20<br>
     * 2001-01-31 addWrapped one month  is 2001-02-28<br>
     * 2001-01-31 addWrapped two months is 2001-03-31<br>
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to add to
     * @param value  the value to add, in the units of the field
     * @return the updated milliseconds
     */
    long addWrapped(long instant, int value) ;

    /**
     * Computes the difference between two instants, as measured in the units
     * of this field. Any fractional units are dropped from the result. Calling
     * getDifference reverses the effect of calling add. In the following code:
     *
     * <pre>
     * long instant = ...
     * int v = ...
     * int age = getDifference(add(instant, v), instant);
     * </pre>
     *
     * The value 'age' is the same as the value 'v'.
     *
     * @param minuendInstant the milliseconds from 1970-01-01T00:00:00Z to
     * subtract from
     * @param subtrahendInstant the milliseconds from 1970-01-01T00:00:00Z to
     * subtract off the minuend
     * @return the difference in the units of this field
     */
    int getDifference(long minuendInstant, long subtrahendInstant);

    /**
     * Computes the difference between two instants, as measured in the units
     * of this field. Any fractional units are dropped from the result. Calling
     * getDifference reverses the effect of calling add. In the following code:
     *
     * <pre>
     * long instant = ...
     * long v = ...
     * long age = getDifferenceAsLong(add(instant, v), instant);
     * </pre>
     *
     * The value 'age' is the same as the value 'v'.
     *
     * @param minuendInstant the milliseconds from 1970-01-01T00:00:00Z to
     * subtract from
     * @param subtrahendInstant the milliseconds from 1970-01-01T00:00:00Z to
     * subtract off the minuend
     * @return the difference in the units of this field
     */
    long getDifferenceAsLong(long minuendInstant, long subtrahendInstant);

    /**
     * Sets a value in the milliseconds supplied.
     * <p>
     * The value of this field will be set. If the value is invalid, an
     * exception if thrown. Other fields are always unaffected.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to set in
     * @param value  the value to set, in the units of the field
     * @return the updated milliseconds
     * @throws IllegalArgumentException if the value is invalid
     */
    long set(long instant, int value);

    /**
     * Sets a value in the milliseconds supplied from a human-readable, text
     * value. If the specified locale is null, the default locale is used.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to set in
     * @param text  the text value to set
     * @param locale the locale to use for selecting a text symbol, null for
     * default
     * @return the updated milliseconds
     * @throws IllegalArgumentException if the text value is invalid
     */
    long set(long instant, String text, Locale locale);

    /**
     * Sets a value in the milliseconds supplied from a human-readable, text
     * value.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to set in
     * @param text  the text value to set
     * @return the updated milliseconds
     * @throws IllegalArgumentException if the text value is invalid
     */
    long set(long instant, String text);

    // Extra information API
    //------------------------------------------------------------------------
    /**
     * Returns the duration per unit value of this field. For example, if this
     * field represents "hour of day", then the duration is an hour.
     *
     * @return the duration of this field, or UnsupportedDurationField if field
     * has no duration
     */
    DurationField getDurationField();

    /**
     * Returns the range duration of this field. For example, if this field
     * represents "hour of day", then the range duration is a day.
     *
     * @return the range duration of this field, or null if field has no range
     */
    DurationField getRangeDurationField();

    /**
     * Returns whether this field is 'leap' for the specified instant.
     * <p>
     * For example, a leap year would return true, a non leap year would return
     * false.
     * 
     * @return true if the field is 'leap'
     */
    boolean isLeap(long instant);

    /**
     * Gets the amount by which this field is 'leap' for the specified instant.
     * <p>
     * For example, a leap year would return one, a non leap year would return
     * zero.
     */
    int getLeapAmount(long instant);

    /**
     * If this field were to leap, then it would be in units described by the
     * returned duration. If this field doesn't ever leap, null is returned.
     */
    DurationField getLeapDurationField();

    /**
     * Get the minimum allowable value for this field.
     * 
     * @return the minimum valid value for this field, in the units of the
     * field
     */
    int getMinimumValue();

    /**
     * Get the minimum value for this field evaluated at the specified time.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to query
     * @return the minimum value for this field, in the units of the field
     */
    int getMinimumValue(long instant);

    /**
     * Get the maximum allowable value for this field.
     * 
     * @return the maximum valid value for this field, in the units of the
     * field
     */
    int getMaximumValue();

    /**
     * Get the maximum value for this field evaluated at the specified time.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to query
     * @return the maximum value for this field, in the units of the field
     */
    int getMaximumValue(long instant);

    /**
     * Get the maximum text value for this field.
     * 
     * @param locale  the locale to use for selecting a text symbol
     * @return the maximum text length
     */
    int getMaximumTextLength(Locale locale);

    /**
     * Get the maximum short text value for this field.
     * 
     * @param locale  the locale to use for selecting a text symbol
     * @return the maximum short text length
     */
    int getMaximumShortTextLength(Locale locale);

    // Calculation API
    //------------------------------------------------------------------------
    /**
     * Round to the lowest whole unit of this field. After rounding, the value
     * of this field and all fields of a higher magnitude are retained. The
     * fractional millis that cannot be expressed in whole increments of this
     * field are set to minimum.
     * <p>
     * For example, a datetime of 2002-11-02T23:34:56.789, rounded to the
     * lowest whole hour is 2002-11-02T23:00:00.000.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to round
     * @return rounded milliseconds
     */
    long roundFloor(long instant);

    /**
     * Round to the highest whole unit of this field. The value of this field
     * and all fields of a higher magnitude may be incremented in order to
     * achieve this result. The fractional millis that cannot be expressed in
     * whole increments of this field are set to minimum.
     * <p>
     * For example, a datetime of 2002-11-02T23:34:56.789, rounded to the
     * highest whole hour is 2002-11-03T00:00:00.000.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to round
     * @return rounded milliseconds
     */
    long roundCeiling(long instant);

    /**
     * Round to the nearest whole unit of this field. If the given millisecond
     * value is closer to the floor or is exactly halfway, this function
     * behaves like roundFloor. If the millisecond value is closer to the
     * ceiling, this function behaves like roundCeiling.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to round
     * @return rounded milliseconds
     */
    long roundHalfFloor(long instant);

    /**
     * Round to the nearest whole unit of this field. If the given millisecond
     * value is closer to the floor, this function behaves like roundFloor. If
     * the millisecond value is closer to the ceiling or is exactly halfway,
     * this function behaves like roundCeiling.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to round
     * @return rounded milliseconds
     */
    long roundHalfCeiling(long instant);

    /**
     * Round to the nearest whole unit of this field. If the given millisecond
     * value is closer to the floor, this function behaves like roundFloor. If
     * the millisecond value is closer to the ceiling, this function behaves
     * like roundCeiling.
     * <p>
     * If the millisecond value is exactly halfway between the floor and
     * ceiling, the ceiling is chosen over the floor only if it makes this
     * field's value even.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to round
     * @return rounded milliseconds
     */
    long roundHalfEven(long instant);

    /**
     * Returns the fractional duration milliseconds of this field. In other
     * words, calling remainder returns the duration that roundFloor would
     * subtract.
     * <p>
     * For example, on a datetime of 2002-11-02T23:34:56.789, the remainder by
     * hour is 34 minutes and 56.789 seconds.
     *
     * @param instant the milliseconds from 1970-01-01T00:00:00Z to get the
     * remainder
     * @return remainder duration, in milliseconds
     */
    long remainder(long instant);

    /**
     * Get a suitable debug string.
     * 
     * @return debug string
     */
    String toString();
    
}
