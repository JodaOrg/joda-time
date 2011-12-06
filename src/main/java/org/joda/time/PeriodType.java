/*
 *  Copyright 2001-2009 Stephen Colebourne
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
package org.joda.time;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.field.FieldUtils;

/**
 * Controls a period implementation by specifying which duration fields are to be used.
 * <p>
 * The following implementations are provided:
 * <ul>
 * <li>Standard - years, months, weeks, days, hours, minutes, seconds, millis
 * <li>YearMonthDayTime - years, months, days, hours, minutes, seconds, millis
 * <li>YearMonthDay - years, months, days
 * <li>YearWeekDayTime - years, weeks, days, hours, minutes, seconds, millis
 * <li>YearWeekDay - years, weeks, days
 * <li>YearDayTime - years, days, hours, minutes, seconds, millis
 * <li>YearDay - years, days, hours
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

    /** Cache of all the known types. */
    private static final Map<PeriodType, Object> cTypes = new HashMap<PeriodType, Object>(32);

    static int YEAR_INDEX = 0;
    static int MONTH_INDEX = 1;
    static int WEEK_INDEX = 2;
    static int DAY_INDEX = 3;
    static int HOUR_INDEX = 4;
    static int MINUTE_INDEX = 5;
    static int SECOND_INDEX = 6;
    static int MILLI_INDEX = 7;
    
    private static PeriodType cStandard;
    private static PeriodType cYMDTime;
    private static PeriodType cYMD;
    private static PeriodType cYWDTime;
    private static PeriodType cYWD;
    private static PeriodType cYDTime;
    private static PeriodType cYD;
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
     * Gets a type that defines the year, month and day fields.
     * <ul>
     * <li>years
     * <li>months
     * <li>days
     * </ul>
     *
     * @return the period type
     * @since 1.1
     */
    public static PeriodType yearMonthDay() {
        PeriodType type = cYMD;
        if (type == null) {
            type = new PeriodType(
                "YearMonthDay",
                new DurationFieldType[] {
                    DurationFieldType.years(), DurationFieldType.months(),
                    DurationFieldType.days(),
                },
                new int[] { 0, 1, -1, 2, -1, -1, -1, -1, }
            );
            cYMD = type;
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
     * Gets a type that defines year, week and day fields.
     * <ul>
     * <li>years
     * <li>weeks
     * <li>days
     * </ul>
     *
     * @return the period type
     * @since 1.1
     */
    public static PeriodType yearWeekDay() {
        PeriodType type = cYWD;
        if (type == null) {
            type = new PeriodType(
                "YearWeekDay",
                new DurationFieldType[] {
                    DurationFieldType.years(),
                    DurationFieldType.weeks(), DurationFieldType.days(),
                },
                new int[] { 0, -1, 1, 2, -1, -1, -1, -1, }
            );
            cYWD = type;
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
     * Gets a type that defines the year and day fields.
     * <ul>
     * <li>years
     * <li>days
     * </ul>
     *
     * @return the period type
     * @since 1.1
     */
    public static PeriodType yearDay() {
        PeriodType type = cYD;
        if (type == null) {
            type = new PeriodType(
                "YearDay",
                new DurationFieldType[] {
                    DurationFieldType.years(), DurationFieldType.days(),
                },
                new int[] { 0, -1, -1, 1, -1, -1, -1, -1, }
            );
            cYD = type;
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

    /**
     * Gets a period type that contains the duration types of the array.
     * <p>
     * Only the 8 standard duration field types are supported.
     *
     * @param types  the types to include in the array.
     * @return the period type
     * @since 1.1
     */
    public static synchronized PeriodType forFields(DurationFieldType[] types) {
        if (types == null || types.length == 0) {
            throw new IllegalArgumentException("Types array must not be null or empty");
        }
        for (int i = 0; i < types.length; i++) {
            if (types[i] == null) {
                throw new IllegalArgumentException("Types array must not contain null");
            }
        }
        Map<PeriodType, Object> cache = cTypes;
        if (cache.isEmpty()) {
            cache.put(standard(), standard());
            cache.put(yearMonthDayTime(), yearMonthDayTime());
            cache.put(yearMonthDay(), yearMonthDay());
            cache.put(yearWeekDayTime(), yearWeekDayTime());
            cache.put(yearWeekDay(), yearWeekDay());
            cache.put(yearDayTime(), yearDayTime());
            cache.put(yearDay(), yearDay());
            cache.put(dayTime(), dayTime());
            cache.put(time(), time());
            cache.put(years(), years());
            cache.put(months(), months());
            cache.put(weeks(), weeks());
            cache.put(days(), days());
            cache.put(hours(), hours());
            cache.put(minutes(), minutes());
            cache.put(seconds(), seconds());
            cache.put(millis(), millis());
        }
        PeriodType inPartType = new PeriodType(null, types, null);
        Object cached = cache.get(inPartType);
        if (cached instanceof PeriodType) {
            return (PeriodType) cached;
        }
        if (cached != null) {
            throw new IllegalArgumentException("PeriodType does not support fields: " + cached);
        }
        PeriodType type = standard();
        List<DurationFieldType> list = new ArrayList<DurationFieldType>(Arrays.asList(types));
        if (list.remove(DurationFieldType.years()) == false) {
            type = type.withYearsRemoved();
        }
        if (list.remove(DurationFieldType.months()) == false) {
            type = type.withMonthsRemoved();
        }
        if (list.remove(DurationFieldType.weeks()) == false) {
            type = type.withWeeksRemoved();
        }
        if (list.remove(DurationFieldType.days()) == false) {
            type = type.withDaysRemoved();
        }
        if (list.remove(DurationFieldType.hours()) == false) {
            type = type.withHoursRemoved();
        }
        if (list.remove(DurationFieldType.minutes()) == false) {
            type = type.withMinutesRemoved();
        }
        if (list.remove(DurationFieldType.seconds()) == false) {
            type = type.withSecondsRemoved();
        }
        if (list.remove(DurationFieldType.millis()) == false) {
            type = type.withMillisRemoved();
        }
        if (list.size() > 0) {
            cache.put(inPartType, list);
            throw new IllegalArgumentException("PeriodType does not support fields: " + list);
        }
        // recheck cache in case initial array order was wrong
        PeriodType checkPartType = new PeriodType(null, type.iTypes, null);
        PeriodType checkedType = (PeriodType) cache.get(checkPartType);
        if (checkedType != null) {
            cache.put(checkPartType, checkedType);
            return checkedType;
        }
        cache.put(checkPartType, type);
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
        return "PeriodType[" + getName() + "]";
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the indexed field part of the period.
     * 
     * @param period  the period to query
     * @param index  the index to use
     * @return the value of the field, zero if unsupported
     */
    int getIndexedField(ReadablePeriod period, int index) {
        int realIndex = iIndices[index];
        return (realIndex == -1 ? 0 : period.getValue(realIndex));
    }

    /**
     * Sets the indexed field part of the period.
     * 
     * @param period  the period to query
     * @param index  the index to use
     * @param values  the array to populate
     * @param newValue  the value to set
     * @throws UnsupportedOperationException if not supported
     */
    boolean setIndexedField(ReadablePeriod period, int index, int[] values, int newValue) {
        int realIndex = iIndices[index];
        if (realIndex == -1) {
            throw new UnsupportedOperationException("Field is not supported");
        }
        values[realIndex] = newValue;
        return true;
    }

    /**
     * Adds to the indexed field part of the period.
     * 
     * @param period  the period to query
     * @param index  the index to use
     * @param values  the array to populate
     * @param valueToAdd  the value to add
     * @return true if the array is updated
     * @throws UnsupportedOperationException if not supported
     */
    boolean addIndexedField(ReadablePeriod period, int index, int[] values, int valueToAdd) {
        if (valueToAdd == 0) {
            return false;
        }
        int realIndex = iIndices[index];
        if (realIndex == -1) {
            throw new UnsupportedOperationException("Field is not supported");
        }
        values[realIndex] = FieldUtils.safeAdd(values[realIndex], valueToAdd);
        return true;
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
