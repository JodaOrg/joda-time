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
                    byNameKeyCache.put(set[4], new String[] {set[4], set[3]});
                }
            }
        }

        return (String[])byNameKeyCache.get(nameKey);
    }

    private HashMap createCache() {
        return new HashMap(7);
    }
}
