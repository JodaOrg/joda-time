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

/**
 * DateTimeField is an abstract class which allows the date and time
 * manipulation code to be field based. Each field within a datetime
 * can be accessed and manipulated using a dedicated subclass of this
 * one.
 * <p>
 * This design is extensible, so if you wish to extract a different 
 * field from the milliseconds, you can do, simply by implementing
 * your own subclass.
 *
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public abstract class DateTimeField implements Serializable {

    /** A desriptive name for the field */
    private final String iName;

    /**
     * Constructor.
     * 
     * @param name a short descriptive name for the field, such as
     * millisOfSecond
     */
    protected DateTimeField(String name) {
        super();
        
        iName = name;
    }
    
    /**
     * Get the name of the field.
     * 
     * @return field name
     */
    public String getName() {
        return iName;
    }

    // Main access API
    //------------------------------------------------------------------------
    /**
     * Get the value of this field from the milliseconds.
     * 
     * @param millis  the milliseconds from 1970-01-01T00:00:00Z to query
     * @return the value of the field, in the units of the field
     */
    public abstract int get(long millis);

    /**
     * Get the human-readable, text value of this field from the milliseconds.
     * If the specified locale is null, the default locale is used.
     * <p>
     * The default implementation returns Integer.toString(get(millis)).
     * <p>
     * Note: subclasses that override this method should also override
     * getMaximumTextLength.
     *
     * @param millis  the milliseconds from 1970-01-01T00:00:00Z to query
     * @param locale the locale to use for selecting a text symbol, null for
     * default
     * @return the text value of the field
     */
    public String getAsText(long millis, Locale locale) {
        return Integer.toString(get(millis));
    }

    /**
     * Get the human-readable, text value of this field from the milliseconds.
     * This implementation returns getAsText(millis, null).
     * 
     * @param millis  the milliseconds from 1970-01-01T00:00:00Z to query
     * @return the text value of the field
     */
    public final String getAsText(long millis) {
        return getAsText(millis, null);
    }

    /**
     * Get the human-readable, short text value of this field from the
     * milliseconds.  If the specified locale is null, the default locale is
     * used.
     * <p>
     * The default implementation returns getAsText(millis, locale).
     * <p>
     * Note: subclasses that override this method should also override
     * getMaximumShortTextLength.
     *
     * @param millis  the milliseconds from 1970-01-01T00:00:00Z to query
     * @param locale the locale to use for selecting a text symbol, null for
     * default
     * @return the short text value of the field
     */
    public String getAsShortText(long millis, Locale locale) {
        return getAsText(millis, locale);
    }

    /**
     * Get the human-readable, short text value of this field from the
     * milliseconds.  This implementation returns getAsShortText(millis, null).
     * 
     * @param millis  the milliseconds from 1970-01-01T00:00:00Z to query
     * @return the short text value of the field
     */
    public final String getAsShortText(long millis) {
        return getAsShortText(millis, null);
    }

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
     * @param millis  the milliseconds from 1970-01-01T00:00:00Z to add to
     * @param value  the value to add, in the units of the field
     * @return the updated milliseconds
     */
    public abstract long add(long millis, int value);

    /**
     * Adds a value (which may be negative) to the millis value,
     * overflowing into larger fields if necessary.
     * 
     * @param millis  the milliseconds from 1970-01-01T00:00:00Z to add to
     * @param value  the long value to add, in the units of the field
     * @return the updated milliseconds
     * @throws IllegalArgumentException if value is too large
     * @see #add(long,int)
     */
    public abstract long add(long millis, long value);

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
     * @param millis  the milliseconds from 1970-01-01T00:00:00Z to add to
     * @param value  the value to add, in the units of the field
     * @return the updated milliseconds
     */
    public long addWrapped(long millis, int value) {
        int current = get(millis);
        int wrapped = getWrappedValue
            (current, value, getMinimumValue(millis), getMaximumValue(millis));
        return set(millis, wrapped);
    }

    /**
     * Computes the difference between two instants, as measured in the units
     * of this field. Any fractional units are dropped from the result. Calling
     * getDifference reverses the effect of calling add. In the following code:
     *
     * <pre>
     * long millis = ...
     * int v = ...
     * long age = getDifference(add(millis, v), millis);
     * </pre>
     *
     * The value 'age' is the same as the value 'v'.
     * <p>
     * The default implementation performs a guess-and-check algorithm using
     * the getUnitMillis and add methods. Subclasses are encouraged to provide
     * a more efficient implementation.
     *
     * @param minuendMillis the milliseconds from 1970-01-01T00:00:00Z to
     * subtract from
     * @param subtrahendMillis the milliseconds from 1970-01-01T00:00:00Z to
     * subtract off the minuend
     * @return the difference in the units of this field
     */
    public long getDifference(long minuendMillis, long subtrahendMillis) {
        if (minuendMillis < subtrahendMillis) {
            return -getDifference(subtrahendMillis, minuendMillis);
        }

        long difference = (minuendMillis - subtrahendMillis) / getUnitMillis();
        if (add(subtrahendMillis, difference) < minuendMillis) {
            do {
                difference++;
            } while (add(subtrahendMillis, difference) <= minuendMillis);
            difference--;
        } else if (add(subtrahendMillis, difference) > minuendMillis) {
            do {
                difference--;
            } while (add(subtrahendMillis, difference) > minuendMillis);
        }
        return difference;
    }

    /**
     * Sets a value in the milliseconds supplied.
     * <p>
     * The value of this field will be set. If the value is invalid, an
     * exception if thrown. Other fields are always unaffected.
     * 
     * @param millis  the milliseconds from 1970-01-01T00:00:00Z to set in
     * @param value  the value to set, in the units of the field
     * @return the updated milliseconds
     * @throws IllegalArgumentException if the value is invalid
     */
    public abstract long set(long millis, int value);

    /**
     * Sets a value in the milliseconds supplied from a human-readable, text
     * value. If the specified locale is null, the default locale is used.
     * <p>
     * The default implementation returns set(millis,
     * Integer.parseInt(millis)).
     * <p>
     * Note: subclasses that override this method should also override
     * getAsText.
     *
     * @param millis  the milliseconds from 1970-01-01T00:00:00Z to set in
     * @param text  the text value to set
     * @param locale the locale to use for selecting a text symbol, null for
     * default
     * @return the updated milliseconds
     * @throws IllegalArgumentException if the text value is invalid
     */
    public long set(long millis, String text, Locale locale) {
        try {
            return set(millis, Integer.parseInt(text));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid " + getName() + " text: " + text);
        }
    }

    /**
     * Sets a value in the milliseconds supplied from a human-readable, text
     * value. This implementation returns set(millis, text, null).
     * 
     * @param millis  the milliseconds from 1970-01-01T00:00:00Z to set in
     * @param text  the text value to set
     * @return the updated milliseconds
     * @throws IllegalArgumentException if the text value is invalid
     */
    public final long set(long millis, String text) {
        return set(millis, text, null);
    }

    // Extra information API
    //------------------------------------------------------------------------
    /**
     * Returns whether this field is 'leap' for the specified millis.
     * <p>
     * For example, a leap year would return true, a non leap year would return
     * false.
     * <p>
     * This implementation returns false.
     * 
     * @return true if the field is 'leap'
     */
    public boolean isLeap(long millis) {
        return false;
    }

    /**
     * Gets the amount by which this field is 'leap' for the specified millis.
     * <p>
     * For example, a leap year would return one, a non leap year would return
     * zero.
     * <p>
     * This implementation returns zero.
     */
    public int getLeapAmount(long millis) {
        return 0;
    }

    /**
     * Returns the amount of milliseconds per unit value of this field. For
     * example, if this field represents "hour of day", then the unit is the
     * amount of milliseconds per one hour.
     * <p>
     * For fields with a variable unit size, this method returns a suitable
     * average value.
     *
     * @return the unit size of this field, in milliseconds
     */
    public abstract long getUnitMillis();

    /**
     * Returns the range of this field, in milliseconds. For example, if this
     * field represents "hour of day", then the range is the amount of
     * milliseconds per one day.
     * <p>
     * For fields with a variable range, this method returns a suitable average
     * value. If the range is too large to fit in a long, Long.MAX_VALUE is
     * returned.
     *
     * @return the range of this field, in milliseconds
     */
    public abstract long getRangeMillis();

    /**
     * Get the minimum allowable value for this field.
     * 
     * @return the minimum valid value for this field, in the units of the
     * field
     */
    public abstract int getMinimumValue();

    /**
     * Get the minimum value for this field evaluated at the specified time.
     * <p>
     * This implementation returns the same as {@link #getMinimumValue()}.
     * 
     * @param millis  the milliseconds from 1970-01-01T00:00:00Z to query
     * @return the minimum value for this field, in the units of the field
     */
    public int getMinimumValue(long millis) {
        return getMinimumValue();
    }

    /**
     * Get the maximum allowable value for this field.
     * 
     * @return the maximum valid value for this field, in the units of the
     * field
     */
    public abstract int getMaximumValue();

    /**
     * Get the maximum value for this field evaluated at the specified time.
     * <p>
     * This implementation returns the same as {@link #getMaximumValue()}.
     * 
     * @param millis  the milliseconds from 1970-01-01T00:00:00Z to query
     * @return the maximum value for this field, in the units of the field
     */
    public int getMaximumValue(long millis) {
        return getMaximumValue();
    }

    /**
     * Get the maximum text value for this field. The default implementation
     * returns the equivalent of Integer.toString(getMaximumValue()).length().
     * 
     * @param locale  the locale to use for selecting a text symbol
     * @return the maximum text length
     */
    public int getMaximumTextLength(Locale locale) {
        int max = getMaximumValue();
        if (max >= 0) {
            if (max < 10) {
                return 1;
            } else if (max < 100) {
                return 2;
            } else if (max < 1000) {
                return 3;
            }
        }
        return Integer.toString(max).length();
    }

    /**
     * Get the maximum short text value for this field. The default
     * implementation returns getMaximumTextLength().
     * 
     * @param locale  the locale to use for selecting a text symbol
     * @return the maximum short text length
     */
    public int getMaximumShortTextLength(Locale locale) {
        return getMaximumTextLength(locale);
    }

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
     * @param millis  the milliseconds from 1970-01-01T00:00:00Z to round
     * @return rounded milliseconds
     */
    public abstract long roundFloor(long millis);

    /**
     * Round to the highest whole unit of this field. The value of this field
     * and all fields of a higher magnitude may be incremented in order to
     * achieve this result. The fractional millis that cannot be expressed in
     * whole increments of this field are set to minimum.
     * <p>
     * For example, a datetime of 2002-11-02T23:34:56.789, rounded to the
     * highest whole hour is 2002-11-03T00:00:00.000.
     * <p>
     * The default implementation calls roundFloor, and if the millis is
     * modified as a result, adds one field unit. Subclasses are encouraged to
     * provide a more efficient implementation.
     *
     * @param millis  the milliseconds from 1970-01-01T00:00:00Z to round
     * @return rounded milliseconds
     */
    public long roundCeiling(long millis) {
        long newMillis = roundFloor(millis);
        if (newMillis != millis) {
            millis = add(newMillis, 1);
        }
        return millis;
    }

    /**
     * Round to the nearest whole unit of this field. If the given millisecond
     * value is closer to the floor or is exactly halfway, this function
     * behaves like roundFloor. If the millisecond value is closer to the
     * ceiling, this function behaves like roundCeiling.
     *
     * @param millis  the milliseconds from 1970-01-01T00:00:00Z to round
     * @return rounded milliseconds
     */
    public long roundHalfFloor(long millis) {
        long floor = roundFloor(millis);
        long ceiling = roundCeiling(millis);

        long diffFromFloor = millis - floor;
        long diffToCeiling = ceiling - millis;

        if (diffFromFloor <= diffToCeiling) {
            // Closer to the floor, or halfway - round floor
            return floor;
        } else {
            return ceiling;
        }
    }

    /**
     * Round to the nearest whole unit of this field. If the given millisecond
     * value is closer to the floor, this function behaves like roundFloor. If
     * the millisecond value is closer to the ceiling or is exactly halfway,
     * this function behaves like roundCeiling.
     *
     * @param millis  the milliseconds from 1970-01-01T00:00:00Z to round
     * @return rounded milliseconds
     */
    public long roundHalfCeiling(long millis) {
        long floor = roundFloor(millis);
        long ceiling = roundCeiling(millis);

        long diffFromFloor = millis - floor;
        long diffToCeiling = ceiling - millis;

        if (diffToCeiling <= diffFromFloor) {
            // Closer to the ceiling, or halfway - round ceiling
            return ceiling;
        } else {
            return floor;
        }
    }

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
     * @param millis  the milliseconds from 1970-01-01T00:00:00Z to round
     * @return rounded milliseconds
     */
    public long roundHalfEven(long millis) {
        long floor = roundFloor(millis);
        long ceiling = roundCeiling(millis);

        long diffFromFloor = millis - floor;
        long diffToCeiling = ceiling - millis;

        if (diffFromFloor < diffToCeiling) {
            // Closer to the floor - round floor
            return floor;
        } else if (diffToCeiling < diffFromFloor) {
            // Closer to the ceiling - round ceiling
            return ceiling;
        } else {
            // Round to the millis that makes this field even. If both values
            // make this field even (unlikely), favor the ceiling.
            if ((get(ceiling) & 1) == 0) {
                return ceiling;
            }
            return floor;
        }
    }

    /**
     * Retains only the fractional units of this field. This field value and
     * all fields of higher magnitude are reset. In other words, calling
     * remainder retains the part of the instant that roundFloor dropped.
     * <p>
     * For example, a datetime of 2002-11-02T23:34:56.789, the remainder by
     * hour is 1970-01-01T00:34:56.789.
     * <p>
     * The default implementation computes
     * <code>millis - roundFloor(millis)</code>. Subclasses are encouraged to
     * provide a more efficient implementation.
     *
     * @param millis the milliseconds from 1970-01-01T00:00:00Z to get the
     * remainder
     * @return remainder milliseconds
     */
    public long remainder(long millis) {
        return millis - roundFloor(millis);
    }

    // Implementation helper methods
    //------------------------------------------------------------------------
    /**
     * Verify that input values are within specified bounds.
     * 
     * @param value  the value to check
     * @param lowerBound  the lower bound allowed for value
     * @param upperBound  the upper bound allowed for value
     * @throws IllegalArgumentException if value is not in the specified bounds
     */
    protected void verifyValueBounds(int value, int lowerBound, int upperBound) {
        if ((value < lowerBound) || (value > upperBound)) {
            throw new IllegalArgumentException(
                "Value: "
                    + value
                    + " for "
                    + iName
                    + " must be in the range ("
                    + lowerBound
                    + ','
                    + upperBound
                    + ')');
        }
    }

    /**
     * Utility method used by addWrapped implementations to ensure the new
     * value lies within the field's legal value range.
     *
     * @param currentValue the current value of the data, which may lie outside
     * the wrapped value range
     * @param wrapValue  the value to add to current value before
     *  wrapping.  This may be negative.
     * @param minValue the wrap range minimum value.
     * @param maxValue the wrap range maximum value.  This must be
     *  greater than minValue (checked by the method).
     * @return the wrapped value
     * @throws IllegalArgumentException if minValue is greater
     *  than or equal to maxValue
     */
    protected final int getWrappedValue(int currentValue, int wrapValue,
                                        int minValue, int maxValue) {
        return getWrappedValue(currentValue + wrapValue, minValue, maxValue);
    }

    /**
     * Utility method that ensures the given value lies within the field's
     * legal value range.
     * 
     * @param value  the value to fit into the wrapped value range
     * @param minValue the wrap range minimum value.
     * @param maxValue the wrap range maximum value.  This must be
     *  greater than minValue (checked by the method).
     * @return the wrapped value
     * @throws IllegalArgumentException if minValue is greater
     *  than or equal to maxValue
     */
    protected final int getWrappedValue(int value, int minValue, int maxValue) {
        if (minValue >= maxValue) {
            throw new IllegalArgumentException("MIN > MAX");
        }

        int wrapRange = maxValue - minValue + 1;
        value -= minValue;

        if (value >= 0) {
            return (value % wrapRange) + minValue;
        }

        int remByRange = (-value) % wrapRange;

        if (remByRange == 0) {
            return 0 + minValue;
        }
        return (wrapRange - remByRange) + minValue;
    }

    /**
     * If value can be safely cast to an int, then add(long, int) is called,
     * returning its result. Otherwise, an IllegalArgumentException is thrown.
     *
     * @param millis  the milliseconds from 1970-01-01T00:00:00Z to add to
     * @param value  the value to add, in the units of the field
     * @return the updated milliseconds
     * @throws IllegalArgumentException if value is too large
     */
    protected long addLong(long millis, long value) {
        int i_value = (int)value;
        if (i_value == value) {
            return add(millis, i_value);
        }
        throw new IllegalArgumentException
            ("Magnitude of add amount is too large: " + value);
    }

    /**
     * Get a suitable debug string.
     * 
     * @return debug string
     */
    public String toString() {
        return "DateTimeField[" + iName + ']';
    }
    
}
