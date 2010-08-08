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
package org.joda.time.tz;

import java.util.Set;
import org.joda.time.DateTimeZone;

/**
 * Service provider factory for time zones.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public interface Provider {
    /**
     * Retrieves a DateTimeZone for the given id. All providers must at
     * least support id "UTC".
     *
     * @return null if not found
     */
    DateTimeZone getZone(String id);

    /**
     * Returns an unmodifiable set of ids. All providers must at least
     * support id "UTC".
     */        
    Set getAvailableIDs();
}
