/*
 *  Copyright 2001-2011 Stephen Colebourne
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
import java.util.Map;

import org.joda.time.DateTimeUtils;

/**
 * The default name provider acquires localized names from
 * {@link DateFormatSymbols java.text.DateFormatSymbols}.
 * <p>
 * DefaultNameProvider is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public class DefaultNameProvider implements NameProvider {
    // locale -> (id -> (nameKey -> [shortName, name]))
    private HashMap<Locale, Map<String, Map<String, Object>>> iByLocaleCache = createCache();

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

        Map<String, Map<String, Object>> byIdCache = iByLocaleCache.get(locale);
        if (byIdCache == null) {
            iByLocaleCache.put(locale, byIdCache = createCache());
        }

        Map<String, Object> byNameKeyCache = byIdCache.get(id);
        if (byNameKeyCache == null) {
            byIdCache.put(id, byNameKeyCache = createCache());
            
            String[][] zoneStringsEn = DateTimeUtils.getDateFormatSymbols(Locale.ENGLISH).getZoneStrings();
            String[] setEn = null;
            for (String[] strings : zoneStringsEn) {
              if (strings != null && strings.length == 5 && id.equals(strings[0])) {
                setEn = strings;
                break;
              }
            }
            String[][] zoneStringsLoc = DateTimeUtils.getDateFormatSymbols(locale).getZoneStrings();
            String[] setLoc = null;
            for (String[] strings : zoneStringsLoc) {
              if (strings != null && strings.length == 5 && id.equals(strings[0])) {
                setLoc = strings;
                break;
              }
            }
            
            if (setEn != null && setLoc != null) {
              byNameKeyCache.put(setEn[2], new String[] {setLoc[2], setLoc[1]});
              // need to handle case where summer and winter have the same
              // abbreviation, such as EST in Australia [1716305]
              // we handle this by appending "-Summer", cf ZoneInfoCompiler
              if (setEn[2].equals(setEn[4])) {
                  byNameKeyCache.put(setEn[4] + "-Summer", new String[] {setLoc[4], setLoc[3]});
              } else {
                  byNameKeyCache.put(setEn[4], new String[] {setLoc[4], setLoc[3]});
              }
            }
        }
        return (String[]) byNameKeyCache.get(nameKey);
    }

    private HashMap createCache() {
        return new HashMap(7);
    }
}
