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
import java.util.Comparator;

import org.joda.time.convert.ConverterManager;

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
    private static final DateTimeComparator INSTANCE = new DateTimeComparator(null, null);

    /** The lower limit of fields to compare, null if no limit */
    private final DateTimeField iLowerLimit;
    /** The upper limit of fields to compare, null if no limit */
    private final DateTimeField iUpperLimit;

    //-----------------------------------------------------------------------
    /**
     * Returns a DateTimeComparator the compares the entire date time value.
     * 
     * @return a comparator over all fields
     */
    public static DateTimeComparator getInstance() {
        return INSTANCE;
    }

    /**
     * Returns a DateTimeComparator with a lower limit only. Fields of a
     * magnitude less than the lower limit are excluded from comparisons.
     *
     * @param lowerLimit  inclusive lower limit for fields to be compared, null means no limit
     * @return a comparator over all fields above the lower limit
     */
    public static DateTimeComparator getInstance(DateTimeField lowerLimit) {
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
     * @throws IllegalArgumentException if the lower limit is greater than the upper
     */
    public static DateTimeComparator getInstance(DateTimeField lowerLimit, DateTimeField upperLimit) {
        if (lowerLimit == null && upperLimit == null) {
            return INSTANCE;
        }
        if (lowerLimit != null && upperLimit != null) {
            if (lowerLimit.getDurationField().getUnitMillis() > upperLimit.getDurationField().getUnitMillis()) {
                throw new IllegalArgumentException("Lower limit greater than upper: " +
                    lowerLimit.getName() + " > " + upperLimit.getName());
            }
        }
        return new DateTimeComparator(lowerLimit, upperLimit);
    }

    /**
     * Returns a comparator that only considers date fields.
     * Time of day is ignored.
     * 
     * @param chrono  the chronology to use
     * @return a comparator over all date fields
     */
    public static DateTimeComparator getDateOnlyInstance(Chronology chrono) {
        chrono = DateTimeUtils.getChronology(chrono);
        return getInstance(chrono.dayOfYear(), null);
    }

    /**
     * Returns a comparator that only considers time fields.
     * Date is ignored.
     * 
     * @param chrono  the chronology to use
     * @return a comparator over all time fields
     */
    public static DateTimeComparator getTimeOnlyInstance(Chronology chrono) {
        chrono = DateTimeUtils.getChronology(chrono);
        return getInstance(null, chrono.dayOfYear());
    }

    /**
     * Restricted constructor.
     * 
     * @param lowerLimit  the lower field limit, null means no limit
     * @param upperLimit  the upper field limit, null means no limit
     */
    protected DateTimeComparator(DateTimeField lowerLimit, DateTimeField upperLimit) {
        super();
        iLowerLimit = lowerLimit;
        iUpperLimit = upperLimit;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the field that represents the lower limit of comparison.
     * 
     * @return the field, null if no upper limit
     */
    public DateTimeField getLowerLimit() {
        return iLowerLimit;
    }

    /**
     * Gets the field that represents the upper limit of comparison.
     * 
     * @return the field, null if no upper limit
     */
    public DateTimeField getUpperLimit() {
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
        long lhsMillis = getMillisFromObject(lhsObj);
        long rhsMillis = getMillisFromObject(rhsObj);

        if (iLowerLimit != null) {
            lhsMillis = iLowerLimit.roundFloor(lhsMillis);
            rhsMillis = iLowerLimit.roundFloor(rhsMillis);
        }

        if (iUpperLimit != null) {
            lhsMillis = iUpperLimit.remainder(lhsMillis);
            rhsMillis = iUpperLimit.remainder(rhsMillis);
        }

        if (lhsMillis < rhsMillis) {
            return -1;
        } else if (lhsMillis > rhsMillis) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Gets the millisecond value from an object using the converter system.
     * 
     * @param obj  the object to convert
     * @return millis since the epoch
     */
    private static long getMillisFromObject(Object obj) {
        return ConverterManager.getInstance().getInstantConverter(obj).getInstantMillis(obj);
    }

    //-----------------------------------------------------------------------
    /**
     * Support serialization singletons.
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
