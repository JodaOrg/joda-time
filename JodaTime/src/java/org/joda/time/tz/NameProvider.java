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

import java.util.Locale;

/**
 * Service provider factory for localized time zone names.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public interface NameProvider {
    /**
     * Returns a localized short name, or null if not found.
     *
     * @param locale locale to use for selecting name set
     * @param id time zone id
     * @param nameKey time zone name key
     */
    String getShortName(Locale locale, String id, String nameKey);
    
    /**
     * Returns a localized name, or null if not found.
     *
     * @param locale locale to use for selecting name set
     * @param id time zone id
     * @param nameKey time zone name key
     */
    String getName(Locale locale, String id, String nameKey);
}
