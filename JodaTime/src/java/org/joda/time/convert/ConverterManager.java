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
package org.joda.time.convert;

/**
 * ConverterManager controls the date and time converters.
 * <p>
 * This class enables additional conversion classes to be added via
 * {@link #addInstantConverter(InstantConverter)}, which may replace an
 * existing converter. Similar methods exist for duration and interval
 * converters.
 * <p>
 * This class is threadsafe, so adding/removing converters can be done at any
 * time. Updating the set of convertors is relatively expensive, and so should
 * not be performed often.
 * <p>
 * The default instant converters are:
 * <ul>
 * <li>ReadableInstant
 * <li>PartialInstant
 * <li>String
 * <li>Calendar
 * <li>Date
 * <li>Long
 * </ul>
 * The default duration converters are:
 * <ul>
 * <li>ReadableDuration
 * <li>ReadableInterval
 * <li>String
 * <li>Long
 * </ul>
 *
 * The default interval converters are:
 * <ul>
 * <li>ReadableInterval
 * <li>String
 * </ul>
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class ConverterManager {

    /**
     * Singleton instance, lazily loaded to avoid class loading.
     */
    private static ConverterManager INSTANCE;

    public static ConverterManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConverterManager();
        }
        return INSTANCE;
    }
    
    private ConverterSet iInstantConverters;
    private ConverterSet iDurationConverters;
    private ConverterSet iIntervalConverters;
    
    /**
     * Restricted constructor.
     */
    protected ConverterManager() {
        super();

        iInstantConverters = new ConverterSet(new Converter[] {
            ReadableInstantConverter.INSTANCE,
            PartialInstantConverter.INSTANCE,
            StringConverter.INSTANCE,
            CalendarConverter.INSTANCE,
            DateConverter.INSTANCE,
            LongConverter.INSTANCE,
        });

        iDurationConverters = new ConverterSet(new Converter[] {
            ReadableDurationConverter.INSTANCE,
            ReadableIntervalConverter.INSTANCE,
            StringConverter.INSTANCE,
            LongConverter.INSTANCE,
        });

        iIntervalConverters = new ConverterSet(new Converter[] {
            ReadableIntervalConverter.INSTANCE,
            StringConverter.INSTANCE,
        });
    }
    
    //-----------------------------------------------------------------------
    /**
     * Gets the best converter for the object specified.
     * 
     * @param object  the object to convert
     * @return the converter to use
     * @throws IllegalArgumentException if no suitable converter
     * @throws IllegalStateException if multiple converters match the type
     * equally well
     */
    public InstantConverter getInstantConverter(Object object) {
        InstantConverter converter =
            (InstantConverter)iInstantConverters.select(object == null ? null : object.getClass());
        if (converter != null) {
            return converter;
        }
        throw new IllegalArgumentException("No instant converter found for type: " +
            (object == null ? "null" : object.getClass().getName()));
    }
    
    //-----------------------------------------------------------------------
    /**
     * Gets a copy of the set of converters.
     * 
     * @return the converters, a copy of the real data, never null
     */
    public InstantConverter[] getInstantConverters() {
        ConverterSet set = iInstantConverters;
        InstantConverter[] converters = new InstantConverter[set.size()];
        set.copyInto(converters);
        return converters;
    }
    
    /**
     * Adds a converter to the set of converters. If a matching converter is
     * already in the set, the given converter replaces it. If the converter is
     * exactly the same as one already in the set, no changes are made.
     * <p>
     * The order in which converters are added is not relevent. The best
     * converter is selected by examining the object hierarchy.
     * 
     * @param converter  the converter to add, null ignored
     * @return replaced converter, or null
     */
    public InstantConverter addInstantConverter(InstantConverter converter) {
        if (converter == null) {
            return null;
        }
        InstantConverter[] removed = new InstantConverter[1];
        iInstantConverters = iInstantConverters.add(converter, removed);
        return removed[0];
    }
    
    /**
     * Removes a converter from the set of converters. If the converter was
     * not in the set, no changes are made.
     * 
     * @param converter  the converter to remove, null ignored
     * @return replaced converter, or null
     */
    public InstantConverter removeInstantConverter(InstantConverter converter) {
        if (converter == null) {
            return null;
        }
        InstantConverter[] removed = new InstantConverter[1];
        iInstantConverters = iInstantConverters.remove(converter, removed);
        return removed[0];
    }
    
    /**
     * Removes a converter from the set of converters, by index.
     * 
     * @param index  the index to remove
     * @return replaced converter, or null
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public InstantConverter removeInstantConverter(int index) {
        InstantConverter[] removed = new InstantConverter[1];
        iInstantConverters = iInstantConverters.remove(index, removed);
        return removed[0];
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the best converter for the object specified.
     * 
     * @param object  the object to convert
     * @return the converter to use
     * @throws IllegalArgumentException if no suitable converter
     * @throws IllegalStateException if multiple converters match the type
     * equally well
     */
    public DurationConverter getDurationConverter(Object object) {
        DurationConverter converter =
            (DurationConverter)iDurationConverters.select(object == null ? null : object.getClass());
        if (converter != null) {
            return converter;
        }
        throw new IllegalArgumentException("No duration converter found for type: " +
            (object == null ? "null" : object.getClass().getName()));
    }
    
    //-----------------------------------------------------------------------
    /**
     * Gets a copy of the list of converters.
     * 
     * @return the converters, a copy of the real data, never null
     */
    public DurationConverter[] getDurationConverters() {
        ConverterSet set = iDurationConverters;
        DurationConverter[] converters = new DurationConverter[set.size()];
        set.copyInto(converters);
        return converters;
    }
    
    /**
     * Adds a converter to the set of converters. If a matching converter is
     * already in the set, the given converter replaces it. If the converter is
     * exactly the same as one already in the set, no changes are made.
     * <p>
     * The order in which converters are added is not relevent. The best
     * converter is selected by examining the object hierarchy.
     * 
     * @param converter  the converter to add, null ignored
     * @return replaced converter, or null
     */
    public DurationConverter addDurationConverter(DurationConverter converter) {
        if (converter == null) {
            return null;
        }
        DurationConverter[] removed = new DurationConverter[1];
        iDurationConverters = iDurationConverters.add(converter, removed);
        return removed[0];
    }
    
    /**
     * Removes a converter from the set of converters. If the converter was
     * not in the set, no changes are made.
     * 
     * @param converter  the converter to remove, null ignored
     * @return replaced converter, or null
     */
    public DurationConverter removeDurationConverter(DurationConverter converter) {
        if (converter == null) {
            return null;
        }
        DurationConverter[] removed = new DurationConverter[1];
        iDurationConverters = iDurationConverters.remove(converter, removed);
        return removed[0];
    }
    
    /**
     * Removes a converter from the set of converters, by index.
     * 
     * @param index  the index to remove
     * @return replaced converter, or null
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public DurationConverter removeDurationConverter(int index) {
        DurationConverter[] removed = new DurationConverter[1];
        iDurationConverters = iDurationConverters.remove(index, removed);
        return removed[0];
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the best converter for the object specified.
     * 
     * @param object  the object to convert
     * @return the converter to use
     * @throws IllegalArgumentException if no suitable converter
     * @throws IllegalStateException if multiple converters match the type
     * equally well
     */
    public IntervalConverter getIntervalConverter(Object object) {
        IntervalConverter converter =
            (IntervalConverter)iIntervalConverters.select(object == null ? null : object.getClass());
        if (converter != null) {
            return converter;
        }
        throw new IllegalArgumentException("No interval converter found for type: " +
            (object == null ? "null" : object.getClass().getName()));
    }
    
    //-----------------------------------------------------------------------
    /**
     * Gets a copy of the list of converters.
     * 
     * @return the converters, a copy of the real data, never null
     */
    public IntervalConverter[] getIntervalConverters() {
        ConverterSet set = iIntervalConverters;
        IntervalConverter[] converters = new IntervalConverter[set.size()];
        set.copyInto(converters);
        return converters;
    }
    
    /**
     * Adds a converter to the set of converters. If a matching converter is
     * already in the set, the given converter replaces it. If the converter is
     * exactly the same as one already in the set, no changes are made.
     * <p>
     * The order in which converters are added is not relevent. The best
     * converter is selected by examining the object hierarchy.
     * 
     * @param converter  the converter to add, null ignored
     * @return replaced converter, or null
     */
    public IntervalConverter addIntervalConverter(IntervalConverter converter) {
        if (converter == null) {
            return null;
        }
        IntervalConverter[] removed = new IntervalConverter[1];
        iIntervalConverters = iIntervalConverters.add(converter, removed);
        return removed[0];
    }
    
    /**
     * Removes a converter from the set of converters. If the converter was
     * not in the set, no changes are made.
     * 
     * @param converter  the converter to remove, null ignored
     * @return replaced converter, or null
     */
    public IntervalConverter removeIntervalConverter(IntervalConverter converter) {
        if (converter == null) {
            return null;
        }
        IntervalConverter[] removed = new IntervalConverter[1];
        iIntervalConverters = iIntervalConverters.remove(converter, removed);
        return removed[0];
    }
    
    /**
     * Removes a converter from the set of converters, by index.
     * 
     * @param index  the index to remove
     * @return replaced converter, or null
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public IntervalConverter removeIntervalConverter(int index) {
        IntervalConverter[] removed = new IntervalConverter[1];
        iIntervalConverters = iIntervalConverters.remove(index, removed);
        return removed[0];
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a debug representation of the object
     */
    public String toString() {
        return "ConverterManager[" +
            iInstantConverters.size() + " instant converters," +
            iDurationConverters.size() + " duration converters," +
            iIntervalConverters.size() + " interval converters]";
    }

}
