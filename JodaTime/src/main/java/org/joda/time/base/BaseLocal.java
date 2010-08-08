/*
 *  Copyright 2001-2007 Stephen Colebourne
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
package org.joda.time.base;

/**
 * BaseLocal is an abstract implementation of ReadablePartial that
 * use a local milliseconds internal representation.
 * <p>
 * This class should generally not be used directly by API users.
 * The {@link org.joda.time.ReadablePartial} interface should be used when different 
 * kinds of partial objects are to be referenced.
 * <p>
 * BasePartial subclasses may be mutable and not thread-safe.
 *
 * @author Stephen Colebourne
 * @since 1.5
 */
public abstract class BaseLocal
        extends AbstractPartial {

    /** Serialization version */
    private static final long serialVersionUID = 276453175381783L;

    //-----------------------------------------------------------------------
    /**
     * Constructs a partial with the current time, using ISOChronology in
     * the default zone to extract the fields.
     * <p>
     * The constructor uses the default time zone, resulting in the local time
     * being initialised. Once the constructor is complete, all further calculations
     * are performed without reference to a timezone (by switching to UTC).
     */
    protected BaseLocal() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the local milliseconds from the Java epoch
     * of 1970-01-01T00:00:00 (not fixed to any specific time zone).
     * <p>
     * This method is useful in certain circustances for high performance
     * access to the datetime fields.
     * 
     * @return the number of milliseconds since 1970-01-01T00:00:00
     */
    protected abstract long getLocalMillis();

}
