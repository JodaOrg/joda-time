/*
 *  Copyright 2001-2005 Stephen Colebourne
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
import java.util.Comparator;

import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.InstantConverter;

/**
 * DateTimeComparator provides comparators to compare one date with another.
 * <p>
 * Dates may be specified using any object recognised by the
 * {@link org.joda.time.convert.ConverterManager ConverterManager} class.
 * <p>
 * The default objects recognised by the comparator are:
 * <ul>
 * <li>ReadableInstant
 * <li>String
 * <li>Calendar
 * <li>Date
 * <li>Long (milliseconds)
 * <li>null (now)
 * </ul>
 *
 * <p>
 * DateTimeComparator is thread-safe and immutable.
 *
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public class DateTimeComparator implements Comparator, Serializable {

    /** Serialization lock */
    private static final long serialVersionUID = -6097339773320178364L;

    /** Singleton instance */
    private static final DateTimeComparator ALL_INSTANCE = new DateTimeComparator(null, null);
    /** Singleton instance */
    private static final DateTimeComparator DATE_INSTANCE = new DateTimeComparator(DateTimeFieldType.dayOfYear(), null);
    /** Singleton instance */
    private static final DateTimeComparator TIME_INSTANCE = new DateTimeComparator(null, DateTimeFieldType.dayOfYear());

    /** The lower limit of fields to compare, null if no limit */
    private final DateTimeFieldType iLowerLimit;
    /** The upper limit of fields to compare, null if no limit */
    private final DateTimeFieldType iUpperLimit;

    //-----------------------------------------------------------------------
    /**
     * Returns a DateTimeComparator the compares the entire date time value.
     * 
     * @return a comparator over all fields
     */
    public static DateTimeComparator getInstance() {
        return ALL_INSTANCE;
    }

    /**
     * Returns a DateTimeComparator with a lower limit only. Fields of a
     * magnitude less than the lower limit are excluded from comparisons.
     *
     * @param lowerLimit  inclusive lower limit for fields to be compared, null means no limit
     * @return a comparator over all fields above the lower limit
     */
    public static DateTimeComparator getInstance(DateTimeFieldType lowerLimit) {
        return getInstance(lowerLimit, null);
    }

    /**
     * Returns a DateTimeComparator with a lower and upper limit. Fields of a
     * magnitude less than the lower limit are excluded from comparisons.
     * Fields of a magnitude greater than or equal to the upper limit are also
     * excluded from comparisons. Either limit may be specified as null, which
     * indicates an unbounded limit.
     *
     * @param lowerLimit  inclusive lower limit for fields to be compared, null means no limit
     * @param upperLimit  exclusive upper limit for fields to be compared, null means no limit
     * @return a comparator over all fields between the limits
     */
    public static DateTimeComparator getInstance(DateTimeFieldType lowerLimit, DateTimeFieldType upperLimit) {
        if (lowerLimit == null && upperLimit == null) {
            return ALL_INSTANCE;
        }
        if (lowerLimit == DateTimeFieldType.dayOfYear() && upperLimit == null) {
            return DATE_INSTANCE;
        }
        if (lowerLimit == null && upperLimit == DateTimeFieldType.dayOfYear()) {
            return TIME_INSTANCE;
        }
        return new DateTimeComparator(lowerLimit, upperLimit);
    }

    /**
     * Returns a comparator that only considers date fields.
     * Time of day is ignored.
     * 
     * @return a comparator over all date fields
     */
    public static DateTimeComparator getDateOnlyInstance() {
        return DATE_INSTANCE;
    }

    /**
     * Returns a comparator that only considers time fields.
     * Date is ignored.
     * 
     * @return a comparator over all time fields
     */
    public static DateTimeComparator getTimeOnlyInstance() {
        return TIME_INSTANCE;
    }

    /**
     * Restricted constructor.
     * 
     * @param lowerLimit  the lower field limit, null means no limit
     * @param upperLimit  the upper field limit, null means no limit
     */
    protected DateTimeComparator(DateTimeFieldType lowerLimit, DateTimeFieldType upperLimit) {
        super();
        iLowerLimit = lowerLimit;
        iUpperLimit = upperLimit;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the field type that represents the lower limit of comparison.
     * 
     * @return the field type, null if no upper limit
     */
    public DateTimeFieldType getLowerLimit() {
        return iLowerLimit;
    }

    /**
     * Gets the field type that represents the upper limit of comparison.
     * 
     * @return the field type, null if no upper limit
     */
    public DateTimeFieldType getUpperLimit() {
        return iUpperLimit;
    }

    /**
     * Compare two objects against only the range of date time fields as
     * specified in the constructor.
     * 
     * @param lhsObj  the first object,
     *      logically on the left of a &lt; comparison, null means now
     * @param rhsObj  the second object,
     *      logically on the right of a &lt; comparison, null means now
     * @return zero if order does not matter,
     *      negative value if lhsObj &lt; rhsObj, positive value otherwise.
     * @throws IllegalArgumentException if either argument is not supported
     */
    public int compare(Object lhsObj, Object rhsObj) {
        InstantConverter conv = ConverterManager.getInstance().getInstantConverter(lhsObj);
        Chronology lhsChrono = conv.getChronology(lhsObj, (Chronology) null);
        long lhsMillis = conv.getInstantMillis(lhsObj, lhsChrono);
        
        conv = ConverterManager.getInstance().getInstantConverter(rhsObj);
        Chronology rhsChrono = conv.getChronology(rhsObj, (Chronology) null);
        long rhsMillis = conv.getInstantMillis(rhsObj, rhsChrono);

        if (iLowerLimit != null) {
            lhsMillis = iLowerLimit.getField(lhsChrono).roundFloor(lhsMillis);
            rhsMillis = iLowerLimit.getField(rhsChrono).roundFloor(rhsMillis);
        }

        if (iUpperLimit != null) {
            lhsMillis = iUpperLimit.getField(lhsChrono).remainder(lhsMillis);
            rhsMillis = iUpperLimit.getField(rhsChrono).remainder(rhsMillis);
        }

        if (lhsMillis < rhsMillis) {
            return -1;
        } else if (lhsMillis > rhsMillis) {
            return 1;
        } else {
            return 0;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Support serialization singletons.
     * 
     * @return the resolved singleton instance
     */
    private Object readResolve() {
        return getInstance(iLowerLimit, iUpperLimit);
    }

    /**
     * Compares this comparator to another.
     * 
     * @param object  the object to compare to
     * @return true if equal
     */
    public boolean equals(Object object) {
        if (object instanceof DateTimeComparator) {
            DateTimeComparator other = (DateTimeComparator) object;
            return (iLowerLimit == other.getLowerLimit() ||
                    (iLowerLimit != null && iLowerLimit.equals(other.getLowerLimit()))) &&
                   (iUpperLimit == other.getUpperLimit() ||
                    (iUpperLimit != null && iUpperLimit.equals(other.getUpperLimit())));
        }
        return false;
    }

    /**
     * Gets a suitable hashcode.
     * 
     * @return the hashcode
     */
    public int hashCode() {
        return (iLowerLimit == null ? 0 : iLowerLimit.hashCode()) +
               (123 * (iUpperLimit == null ? 0 : iUpperLimit.hashCode()));
    }

    /**
     * Gets a debugging string.
     * 
     * @return a debugging string
     */
    public String toString() {
        if (iLowerLimit == iUpperLimit) {
            return "DateTimeComparator["
                + (iLowerLimit == null ? "" : iLowerLimit.getName())
                + "]";
        } else {
            return "DateTimeComparator["
                + (iLowerLimit == null ? "" : iLowerLimit.getName())
                + "-"
                + (iUpperLimit == null ? "" : iUpperLimit.getName())
                + "]";
        }
    }

}
