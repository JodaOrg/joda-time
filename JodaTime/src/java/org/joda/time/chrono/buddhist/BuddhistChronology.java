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
package org.joda.time.chrono.buddhist;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.chrono.DecoratedChronology;
import org.joda.time.chrono.DividedDateTimeField;
import org.joda.time.chrono.LimitChronology;
import org.joda.time.chrono.OffsetDateTimeField;
import org.joda.time.chrono.RemainderDateTimeField;
import org.joda.time.chrono.gj.GJChronology;

/**
 * <code>BuddhistChronology</code> provides access to the individual date
 * time fields for the Buddhist chronological calendar system.
 * <p>
 * The Buddhist calendar differs from the GregorianJulian calendar only 
 * in the year. This class is compatable with the BuddhistCalendar class 
 * supplied by Sun.
 * <p>
 * BuddhistChronology is thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class BuddhistChronology extends DecoratedChronology {
    
    static final long serialVersionUID = -3474595157769370126L;

    /**
     * Constant value for 'Buddhist Era', equivalent to the value returned
     * for AD/CE.
     */
    public static final int BE = DateTimeConstants.CE;

    /** Number of years difference in calendars. */
    private static final int BUDDHIST_OFFSET = 543;

    /** Cache of zone to chronology */
    private static final Map cCache = new HashMap();

    /** UTC instance of the chronology */
    private static final BuddhistChronology INSTANCE_UTC = getInstance(DateTimeZone.UTC);

    /**
     * Standard instance of a Buddhist Chronology, that matches
     * Sun's BuddhistCalendar class. This means that it follows the
     * GregorianJulian calendar rules with a cutover date.
     * <p>
     * The time zone of the returned instance is UTC.
     */
    public static BuddhistChronology getInstanceUTC() {
        return INSTANCE_UTC;
    }

    /**
     * Standard instance of a Buddhist Chronology, that matches
     * Sun's BuddhistCalendar class. This means that it follows the
     * GregorianJulian calendar rules with a cutover date.
     */
    public static BuddhistChronology getInstance() {
        return getInstance(DateTimeZone.getDefault());
    }

    /**
     * Standard instance of a Buddhist Chronology, that matches
     * Sun's BuddhistCalendar class. This means that it follows the
     * GregorianJulian calendar rules with a cutover date.
     *
     * @param zone  the time zone to use, null is default
     */
    public static synchronized BuddhistChronology getInstance(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        BuddhistChronology chrono = (BuddhistChronology) cCache.get(zone);
        if (chrono == null) {
            chrono = new BuddhistChronology(GJChronology.getInstance(zone, null, false));
            cCache.put(zone, chrono);
        }
        return chrono;
    }

    // Constructors and instance variables
    //-----------------------------------------------------------------------
    // Fields are transient because readResolve will always return a cached instance.
    private transient DateTimeField iYearField;
    private transient DateTimeField iWeekyearField;
    private transient DateTimeField iYearOfCenturyField;
    private transient DateTimeField iCenturyOfEraField;
    
    /**
     * Restricted constructor.
     */
    private BuddhistChronology(Chronology chronology) {
        this(chronology, false);
    }

    /**
     * Restricted constructor.
     */
    private BuddhistChronology(Chronology chronology, boolean unlimited) {
        // BuddhistChronology is constructed in three magic steps:
        //
        // 1. Wrap a BuddhistChronology with proper offset, but no range limits
        // 2. Wrap a LimitChronology, which will copy and wrap all the fields
        // 3. Wrap a BuddhistChronology which purely delegates to LimitChronology
        //
        // Why is it done this way? So that the LimitChronology error message
        // shows the limit printed using BuddhistChronology fields. This extra
        // wrapping does not impose any additional overhead when accessing
        // fields because LimitChronology copies them.
        //
        // Is this a good design? No.

        super(unlimited ? chronology : limitChronology(chronology));

        DateTimeField field = getWrappedChronology().year();
        if (unlimited) {
            field = new OffsetDateTimeField(field, field.getName(), BUDDHIST_OFFSET);
        }
        iYearField = field;
            
        field = getWrappedChronology().weekyear();
        if (unlimited) {
            field = new OffsetDateTimeField(field, field.getName(), BUDDHIST_OFFSET);
        }
        iWeekyearField = field;

        // All other fields delegated to GJ
    }

    /**
     * Returns a LimitChronology that wraps an unlimited BuddhistChronology
     * that wraps the given Chronology.
     */    
    private static Chronology limitChronology(Chronology chrono) {
        chrono = new BuddhistChronology(chrono, true);
        DateTime lowerLimit = new DateTime(1, 1, 1, 0, 0, 0, 0, chrono);
        return new LimitChronology(chrono, lowerLimit, null);
    }
    
    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return getInstance(getWrappedChronology().getDateTimeZone());
    }

    // Conversion
    //-----------------------------------------------------------------------
    /**
     * Gets the Chronology in the UTC time zone.
     * 
     * @return the chronology in UTC
     */
    public Chronology withUTC() {
        return INSTANCE_UTC;
    }

    /**
     * Gets the Chronology in a specific time zone.
     * 
     * @param zone  the zone to get the chronology in, null is default
     * @return the chronology
     */
    public Chronology withDateTimeZone(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        if (zone == getDateTimeZone()) {
            return this;
        }
        return getInstance(zone);
    }

    // Millis
    //------------------------------------------------------------

    /**
     * Get the millis duration field for this chronology.
     * 
     * @return DurationField
     */
    public DurationField millis() {
        return getWrappedChronology().millis();
    }

    /**
     * Get the millis of second field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField millisOfSecond() {
        return getWrappedChronology().millisOfSecond();
    }

    /**
     * Get the millis of day field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField millisOfDay() {
        return getWrappedChronology().millisOfDay();
    }

    // Seconds
    //------------------------------------------------------------

    /**
     * Get the seconds duration field for this chronology.
     * 
     * @return DurationField
     */
    public DurationField seconds() {
        return getWrappedChronology().seconds();
    }

    /**
     * Get the second of minute field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField secondOfMinute() {
        return getWrappedChronology().secondOfMinute();
    }

    /**
     * Get the second of day field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField secondOfDay() {
        return getWrappedChronology().secondOfDay();
    }

    // Minutes
    //------------------------------------------------------------

    /**
     * Get the minutes duration field for this chronology.
     * 
     * @return DurationField
     */
    public DurationField minutes() {
        return getWrappedChronology().minutes();
    }

    /**
     * Get the minute of hour field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField minuteOfHour() {
        return getWrappedChronology().minuteOfHour();
    }

    /**
     * Get the minute of day field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField minuteOfDay() {
        return getWrappedChronology().minuteOfDay();
    }

    // Hours
    //------------------------------------------------------------

    /**
     * Get the hours duration field for this chronology.
     * 
     * @return DurationField
     */
    public DurationField hours() {
        return getWrappedChronology().hours();
    }

    /**
     * Get the hour of day (0-23) field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField hourOfDay() {
        return getWrappedChronology().hourOfDay();
    }

    /**
     * Get the hour of day (offset to 1-24) field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField clockhourOfDay() {
        return getWrappedChronology().clockhourOfDay();
    }

    /**
     * Get the hour of am/pm (0-11) field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField hourOfHalfday() {
        return getWrappedChronology().hourOfHalfday();
    }

    /**
     * Get the hour of am/pm (offset to 1-12) field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField clockhourOfHalfday() {
        return getWrappedChronology().clockhourOfHalfday();
    }

    /**
     * Get the AM(0) PM(1) field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField halfdayOfDay() {
        return getWrappedChronology().halfdayOfDay();
    }

    // Day
    //------------------------------------------------------------

    /**
     * Get the days duration field for this chronology.
     * 
     * @return DurationField
     */
    public DurationField days() {
        return getWrappedChronology().days();
    }

    /**
     * Get the day of week field for this chronology.
     *
     * @return DateTimeField
     */
    public DateTimeField dayOfWeek() {
        return getWrappedChronology().dayOfWeek();
    }

    /**
     * Get the day of month field for this chronology.
     *
     * @return DateTimeField
     */
    public DateTimeField dayOfMonth() {
        return getWrappedChronology().dayOfMonth();
    }

    /**
     * Get the day of year field for this chronology.
     *
     * @return DateTimeField
     */
    public DateTimeField dayOfYear() {
        return getWrappedChronology().dayOfYear();
    }

    // Week
    //------------------------------------------------------------

    /**
     * Get the weeks duration field for this chronology.
     * 
     * @return DurationField
     */
    public DurationField weeks() {
        return getWrappedChronology().weeks();
    }

    /**
     * Get the week of a week based year field for this chronology.
     *
     * @return DateTimeField
     */
    public DateTimeField weekOfWeekyear() {
        return getWrappedChronology().weekOfWeekyear();
    }

    /**
     * Get the weekyears duration field for this chronology.
     * 
     * @return DurationField
     */
    public DurationField weekyears() {
        return getWrappedChronology().weekyears();
    }

    /**
     * Get the year of a week based year field for this chronology.
     *
     * @return DateTimeField
     */
    public DateTimeField weekyear() {
        return iWeekyearField;
    }

    // Month
    //------------------------------------------------------------

    /**
     * Get the months duration field for this chronology.
     * 
     * @return DurationField
     */
    public DurationField months() {
        return getWrappedChronology().months();
    }

    /**
     * Get the month of year field for this chronology.
     *
     * @return DateTimeField
     */
    public DateTimeField monthOfYear() {
        return getWrappedChronology().monthOfYear();
    }

    // Year
    //------------------------------------------------------------

    /**
     * Get the years duration field for this chronology.
     * 
     * @return DurationField
     */
    public DurationField years() {
        return getWrappedChronology().years();
    }

    /**
     * Get the year field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField year() {
        return iYearField;
    }

    /**
     * Get the year of era field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField yearOfEra() {
        return iYearField;
    }

    /**
     * Get the year of century field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField yearOfCentury() {
        if (iYearOfCenturyField == null) {
            DateTimeField tempField = new RemainderDateTimeField
                ((DividedDateTimeField)centuryOfEra(), "");
            iYearOfCenturyField = new OffsetDateTimeField(tempField, "yearOfCentury", 1);
        }
        return iYearOfCenturyField;
    }

    /**
     * Get the centuries duration field for this chronology.
     * 
     * @return DurationField
     */
    public DurationField centuries() {
        return getWrappedChronology().centuries();
    }

    /**
     * Get the century of era field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField centuryOfEra() {
        if (iCenturyOfEraField == null) {
            DateTimeField tempField = new OffsetDateTimeField(yearOfEra(), "", 99);
            iCenturyOfEraField = new DividedDateTimeField
                (tempField, "centuryOfEra", "centuries", 100);
        }
        return iCenturyOfEraField;
    }

    /**
     * Get the era field for this chronology.
     * 
     * @return DateTimeField
     */
    public DateTimeField era() {
        return BuddhistEraDateTimeField.INSTANCE;
    }

    // Output
    //-----------------------------------------------------------------------
    /**
     * Gets a debugging toString.
     * 
     * @return a debugging string
     */
    public String toString() {
        String str = "BuddhistChronology";
        DateTimeZone zone = getDateTimeZone();
        if (zone != null) {
            str = str + '[' + zone.getID() + ']';
        }
        return str;
    }
   
}
