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
import java.util.Arrays;

/**
 * Controls a period implementation by specifying which duration fields are to be used.
 * <p>
 * The following implementations are provided:
 * <ul>
 * <li>Standard - years, months, weeks, days, hours, minutes, seconds, millis
 * <li>YearMonthDayTime - years, months, days, hours, minutes, seconds, millis
 * <li>YearWeekDayTime - years, weeks, days, hours, minutes, seconds, millis
 * <li>YearDayTime - years, days, hours, minutes, seconds, millis
 * <li>DayTime - days, hours, minutes, seconds, millis
 * <li>Time - hours, minutes, seconds, millis
 * <li>plus one for each single type
 * </ul>
 *
 * <p>
 * PeriodType is thread-safe and immutable, and all subclasses must be as well.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public class PeriodType implements Serializable {
    /** Serialization version */
    private static final long serialVersionUID = 2274324892792009998L;

    private static PeriodType cStandard;
    private static PeriodType cYMDTime;
    private static PeriodType cYWDTime;
    private static PeriodType cYDTime;
    private static PeriodType cDTime;
    private static PeriodType cTime;
    
    private static PeriodType cYears;
    private static PeriodType cMonths;
    private static PeriodType cWeeks;
    private static PeriodType cDays;
    private static PeriodType cHours;
    private static PeriodType cMinutes;
    private static PeriodType cSeconds;
    private static PeriodType cMillis;

    /**
     * Gets a type that defines all standard fields.
     * <ul>
     * <li>years
     * <li>months
     * <li>weeks
     * <li>days
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     *
     * @return the period type
     */
    public static PeriodType standard() {
        PeriodType type = cStandard;
        if (type == null) {
            type = new PeriodType(
                "Standard",
                new DurationFieldType[] {
                    DurationFieldType.years(), DurationFieldType.months(),
                    DurationFieldType.weeks(), DurationFieldType.days(),
                    DurationFieldType.hours(), DurationFieldType.minutes(),
                    DurationFieldType.seconds(), DurationFieldType.millis(),
                },
                new int[] { 0, 1, 2, 3, 4, 5, 6, 7, }
            );
            cStandard = type;
        }
        return type;
    }

    /**
     * Gets a type that defines all standard fields except weeks.
     * <ul>
     * <li>years
     * <li>months
     * <li>days
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     *
     * @return the period type
     */
    public static PeriodType yearMonthDayTime() {
        PeriodType type = cYMDTime;
        if (type == null) {
            type = new PeriodType(
                "YearMonthDayTime",
                new DurationFieldType[] {
                    DurationFieldType.years(), DurationFieldType.months(),
                    DurationFieldType.days(),
                    DurationFieldType.hours(), DurationFieldType.minutes(),
                    DurationFieldType.seconds(), DurationFieldType.millis(),
                },
                new int[] { 0, 1, -1, 2, 3, 4, 5, 6, }
            );
            cYMDTime = type;
        }
        return type;
    }

    /**
     * Gets a type that defines all standard fields except months.
     * <ul>
     * <li>years
     * <li>weeks
     * <li>days
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     *
     * @return the period type
     */
    public static PeriodType yearWeekDayTime() {
        PeriodType type = cYWDTime;
        if (type == null) {
            type = new PeriodType(
                "YearWeekDayTime",
                new DurationFieldType[] {
                    DurationFieldType.years(),
                    DurationFieldType.weeks(), DurationFieldType.days(),
                    DurationFieldType.hours(), DurationFieldType.minutes(),
                    DurationFieldType.seconds(), DurationFieldType.millis(),
                },
                new int[] { 0, -1, 1, 2, 3, 4, 5, 6, }
            );
            cYWDTime = type;
        }
        return type;
    }

    /**
     * Gets a type that defines all standard fields except months and weeks.
     * <ul>
     * <li>years
     * <li>days
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     *
     * @return the period type
     */
    public static PeriodType yearDayTime() {
        PeriodType type = cYDTime;
        if (type == null) {
            type = new PeriodType(
                "YearDayTime",
                new DurationFieldType[] {
                    DurationFieldType.years(), DurationFieldType.days(),
                    DurationFieldType.hours(), DurationFieldType.minutes(),
                    DurationFieldType.seconds(), DurationFieldType.millis(),
                },
                new int[] { 0, -1, -1, 1, 2, 3, 4, 5, }
            );
            cYDTime = type;
        }
        return type;
    }

    /**
     * Gets a type that defines all standard fields from days downwards.
     * <ul>
     * <li>days
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     *
     * @return the period type
     */
    public static PeriodType dayTime() {
        PeriodType type = cDTime;
        if (type == null) {
            type = new PeriodType(
                "DayTime",
                new DurationFieldType[] {
                    DurationFieldType.days(),
                    DurationFieldType.hours(), DurationFieldType.minutes(),
                    DurationFieldType.seconds(), DurationFieldType.millis(),
                },
                new int[] { -1, -1, -1, 0, 1, 2, 3, 4, }
            );
            cDTime = type;
        }
        return type;
    }

    /**
     * Gets a type that defines all standard time fields.
     * <ul>
     * <li>hours
     * <li>minutes
     * <li>seconds
     * <li>milliseconds
     * </ul>
     *
     * @return the period type
     */
    public static PeriodType time() {
        PeriodType type = cTime;
        if (type == null) {
            type = new PeriodType(
                "Time",
                new DurationFieldType[] {
                    DurationFieldType.hours(), DurationFieldType.minutes(),
                    DurationFieldType.seconds(), DurationFieldType.millis(),
                },
                new int[] { -1, -1, -1, -1, 0, 1, 2, 3, }
            );
            cTime = type;
        }
        return type;
    }

    /**
     * Gets a type that defines just the years field.
     *
     * @return the period type
     */
    public static PeriodType years() {
        PeriodType type = cYears;
        if (type == null) {
            type = new PeriodType(
                "Years",
                new DurationFieldType[] { DurationFieldType.years() },
                new int[] { 0, -1, -1, -1, -1, -1, -1, -1, }
            );
            cYears = type;
        }
        return type;
    }

    /**
     * Gets a type that defines just the months field.
     *
     * @return the period type
     */
    public static PeriodType months() {
        PeriodType type = cMonths;
        if (type == null) {
            type = new PeriodType(
                "Months",
                new DurationFieldType[] { DurationFieldType.months() },
                new int[] { -1, 0, -1, -1, -1, -1, -1, -1, }
            );
            cMonths = type;
        }
        return type;
    }

    /**
     * Gets a type that defines just the weeks field.
     *
     * @return the period type
     */
    public static PeriodType weeks() {
        PeriodType type = cWeeks;
        if (type == null) {
            type = new PeriodType(
                "Weeks",
                new DurationFieldType[] { DurationFieldType.weeks() },
                new int[] { -1, -1, 0, -1, -1, -1, -1, -1, }
            );
            cWeeks = type;
        }
        return type;
    }

    /**
     * Gets a type that defines just the days field.
     *
     * @return the period type
     */
    public static PeriodType days() {
        PeriodType type = cDays;
        if (type == null) {
            type = new PeriodType(
                "Days",
                new DurationFieldType[] { DurationFieldType.days() },
                new int[] { -1, -1, -1, 0, -1, -1, -1, -1, }
            );
            cDays = type;
        }
        return type;
    }

    /**
     * Gets a type that defines just the hours field.
     *
     * @return the period type
     */
    public static PeriodType hours() {
        PeriodType type = cHours;
        if (type == null) {
            type = new PeriodType(
                "Hours",
                new DurationFieldType[] { DurationFieldType.hours() },
                new int[] { -1, -1, -1, -1, 0, -1, -1, -1, }
            );
            cHours = type;
        }
        return type;
    }

    /**
     * Gets a type that defines just the minutes field.
     *
     * @return the period type
     */
    public static PeriodType minutes() {
        PeriodType type = cMinutes;
        if (type == null) {
            type = new PeriodType(
                "Minutes",
                new DurationFieldType[] { DurationFieldType.minutes() },
                new int[] { -1, -1, -1, -1, -1, 0, -1, -1, }
            );
            cMinutes = type;
        }
        return type;
    }

    /**
     * Gets a type that defines just the seconds field.
     *
     * @return the period type
     */
    public static PeriodType seconds() {
        PeriodType type = cSeconds;
        if (type == null) {
            type = new PeriodType(
                "Seconds",
                new DurationFieldType[] { DurationFieldType.seconds() },
                new int[] { -1, -1, -1, -1, -1, -1, 0, -1, }
            );
            cSeconds = type;
        }
        return type;
    }

    /**
     * Gets a type that defines just the millis field.
     *
     * @return the period type
     */
    public static PeriodType millis() {
        PeriodType type = cMillis;
        if (type == null) {
            type = new PeriodType(
                "Millis",
                new DurationFieldType[] { DurationFieldType.millis() },
                new int[] { -1, -1, -1, -1, -1, -1, -1, 0, }
            );
            cMillis = type;
        }
        return type;
    }

    //-----------------------------------------------------------------------    
    /** The name of the type */
    private final String iName;
    /** The array of types */
    private final DurationFieldType[] iTypes;
    /** The array of indices */
    private final int[] iIndices;

    /**
     * Constructor.
     *
     * @param name  the name
     * @param types  the types
     * @param indices  the indices
     */
    protected PeriodType(String name, DurationFieldType[] types, int[] indices) {
        super();
        iName = name;
        iTypes = types;
        iIndices = indices;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the name of the period type.
     * 
     * @return the name
     */
    public String getName() {
        return iName;
    }

    /**
     * Gets the number of fields in the period type.
     * 
     * @return the number of fields
     */
    public int size() {
        return iTypes.length;
    }

    /**
     * Gets the field type by index.
     * 
     * @param index  the index to retrieve
     * @return the field type
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public DurationFieldType getFieldType(int index) {
        return iTypes[index];
    }

    /**
     * Checks whether the field specified is supported by this period.
     *
     * @param type  the type to check, may be null which returns false
     * @return true if the field is supported
     */
    public boolean isSupported(DurationFieldType type) {
        return (indexOf(type) >= 0);
    }

    /**
     * Gets the index of the field in this period.
     *
     * @param type  the type to check, may be null which returns -1
     * @return the index of -1 if not supported
     */
    public int indexOf(DurationFieldType type) {
        for (int i = 0, isize = size(); i < isize; i++) {
            if (iTypes[i] == type) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets a debugging to string.
     * 
     * @return a string
     */
    public String toString() {
        String name = getName();
        return "PeriodType[" + getName() + "]";
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the years field part of the period.
     * 
     * @param period  the period to query
     * @return the number of years in the period, zero if unsupported
     */
    public int getYears(ReadablePeriod period) {
        int index = iIndices[0];
        return (index == -1 ? 0 : period.getValue(index));
    }

    /**
     * Gets the months field part of the period.
     * 
     * @param period  the period to query
     * @return the number of months in the period, zero if unsupported
     */
    public int getMonths(ReadablePeriod period) {
        int index = iIndices[1];
        return (index == -1 ? 0 : period.getValue(index));
    }

    /**
     * Gets the weeks field part of the period.
     * 
     * @param period  the period to query
     * @return the number of weeks in the period, zero if unsupported
     */
    public int getWeeks(ReadablePeriod period) {
        int index = iIndices[2];
        return (index == -1 ? 0 : period.getValue(index));
    }

    /**
     * Gets the days field part of the period.
     * 
     * @param period  the period to query
     * @return the number of days in the period, zero if unsupported
     */
    public int getDays(ReadablePeriod period) {
        int index = iIndices[3];
        return (index == -1 ? 0 : period.getValue(index));
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the hours field part of the period.
     * 
     * @param period  the period to query
     * @return the number of hours in the period, zero if unsupported
     */
    public int getHours(ReadablePeriod period) {
        int index = iIndices[4];
        return (index == -1 ? 0 : period.getValue(index));
    }

    /**
     * Gets the minutes field part of the period.
     * 
     * @param period  the period to query
     * @return the number of minutes in the period, zero if unsupported
     */
    public int getMinutes(ReadablePeriod period) {
        int index = iIndices[5];
        return (index == -1 ? 0 : period.getValue(index));
    }

    /**
     * Gets the seconds field part of the period.
     * 
     * @param period  the period to query
     * @return the number of seconds in the period, zero if unsupported
     */
    public int getSeconds(ReadablePeriod period) {
        int index = iIndices[6];
        return (index == -1 ? 0 : period.getValue(index));
    }

    /**
     * Gets the millis field part of the period.
     * 
     * @param period  the period to query
     * @return the number of millis in the period, zero if unsupported
     */
    public int getMillis(ReadablePeriod period) {
        int index = iIndices[7];
        return (index == -1 ? 0 : period.getValue(index));
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a version of this PeriodType instance that does not support years.
     * 
     * @return a new period type that supports the original set of fields except years
     */
    public PeriodType withYearsRemoved() {
        return withFieldRemoved(0, "NoYears");
    }

    /**
     * Returns a version of this PeriodType instance that does not support months.
     * 
     * @return a new period type that supports the original set of fields except months
     */
    public PeriodType withMonthsRemoved() {
        return withFieldRemoved(1, "NoMonths");
    }

    /**
     * Returns a version of this PeriodType instance that does not support weeks.
     * 
     * @return a new period type that supports the original set of fields except weeks
     */
    public PeriodType withWeeksRemoved() {
        return withFieldRemoved(2, "NoWeeks");
    }

    /**
     * Returns a version of this PeriodType instance that does not support days.
     * 
     * @return a new period type that supports the original set of fields except days
     */
    public PeriodType withDaysRemoved() {
        return withFieldRemoved(3, "NoDays");
    }

    /**
     * Returns a version of this PeriodType instance that does not support hours.
     * 
     * @return a new period type that supports the original set of fields except hours
     */
    public PeriodType withHoursRemoved() {
        return withFieldRemoved(4, "NoHours");
    }

    /**
     * Returns a version of this PeriodType instance that does not support minutes.
     * 
     * @return a new period type that supports the original set of fields except minutes
     */
    public PeriodType withMinutesRemoved() {
        return withFieldRemoved(5, "NoMinutes");
    }

    /**
     * Returns a version of this PeriodType instance that does not support seconds.
     * 
     * @return a new period type that supports the original set of fields except seconds
     */
    public PeriodType withSecondsRemoved() {
        return withFieldRemoved(6, "NoSeconds");
    }

    /**
     * Returns a version of this PeriodType instance that does not support milliseconds.
     * 
     * @return a new period type that supports the original set of fields except milliseconds
     */
    public PeriodType withMillisRemoved() {
        return withFieldRemoved(7, "NoMillis");
    }

    /**
     * Removes the field specified by indices index.
     * 
     * @param indicesIndex  the index to remove
     * @param name  the name addition
     * @return the new type
     */
    private PeriodType withFieldRemoved(int indicesIndex, String name) {
        int fieldIndex = iIndices[indicesIndex];
        if (fieldIndex == -1) {
            return this;
        }
        
        DurationFieldType[] types = new DurationFieldType[size() - 1];
        for (int i = 0; i < iTypes.length; i++) {
            if (i < fieldIndex) {
                types[i] = iTypes[i];
            } else if (i > fieldIndex) {
                types[i - 1] = iTypes[i];
            }
        }
        
        int[] indices = new int[8];
        for (int i = 0; i < indices.length; i++) {
            if (i < indicesIndex) {
                indices[i] = iIndices[i];
            } else if (i > indicesIndex) {
                indices[i] = (iIndices[i] == -1 ? -1 : iIndices[i] - 1);
            } else {
                indices[i] = -1;
            }
        }
        return new PeriodType(getName() + name, types, indices);
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this type to another object.
     * To be equal, the object must be a PeriodType with the same set of fields.
     * 
     * @param obj  the object to compare to
     * @return true if equal
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof PeriodType == false) {
            return false;
        }
        PeriodType other = (PeriodType) obj;
        return (Arrays.equals(iTypes, other.iTypes));
    }

    /**
     * Returns a hashcode based on the field types.
     * 
     * @return a suitable hashcode
     */
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < iTypes.length; i++) {
            hash += iTypes[i].hashCode();
        }
        return hash;
    }

}
