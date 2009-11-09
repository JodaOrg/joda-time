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

import java.text.DateFormatSymbols;
import java.util.HashMap;
import java.util.Locale;

/**
 * The default name provider acquires localized names from
 * {@link DateFormatSymbols java.text.DateFormatSymbols}.
 * <p>
 * DefaultNameProvider is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public class DefaultNameProvider implements NameProvider {
    // locale -> (id -> (nameKey -> [shortName, name]))
    private HashMap iByLocaleCache = createCache();

    public DefaultNameProvider() {
    }

    public String getShortName(Locale locale, String id, String nameKey) {
        String[] nameSet = getNameSet(locale, id, nameKey);
        return nameSet == null ? null : nameSet[0];
    }
    
    public String getName(Locale locale, String id, String nameKey) {
        String[] nameSet = getNameSet(locale, id, nameKey);
        return nameSet == null ? null : nameSet[1];
    }

    private synchronized String[] getNameSet(Locale locale, String id, String nameKey) {
        if (locale == null || id == null || nameKey == null) {
            return null;
        }

        HashMap byIdCache = (HashMap)iByLocaleCache.get(locale);
        if (byIdCache == null) {
            iByLocaleCache.put(locale, byIdCache = createCache());
        }

        HashMap byNameKeyCache = (HashMap)byIdCache.get(id);
        if (byNameKeyCache == null) {
            byIdCache.put(id, byNameKeyCache = createCache());
            String[][] zoneStrings = new DateFormatSymbols(locale).getZoneStrings();
            for (int i=0; i<zoneStrings.length; i++) {
                String[] set = zoneStrings[i];
                if (set != null && set.length == 5 && id.equals(set[0])) {
                    byNameKeyCache.put(set[2], new String[] {set[2], set[1]});
                    // need to handle case where summer and winter have the same
                    // abbreviation, such as EST in Australia [1716305]
                    // we handle this by appending "-Summer", cf ZoneInfoCompiler
                    if (set[2].equals(set[4])) {
                        byNameKeyCache.put(set[4] + "-Summer", new String[] {set[4], set[3]});
                    } else {
                        byNameKeyCache.put(set[4], new String[] {set[4], set[3]});
                    }
                    break;
                }
            }
        }

        return (String[])byNameKeyCache.get(nameKey);
    }

    private HashMap createCache() {
        return new HashMap(7);
    }
}
