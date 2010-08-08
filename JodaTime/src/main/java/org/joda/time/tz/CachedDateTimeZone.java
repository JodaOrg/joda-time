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

import org.joda.time.DateTimeZone;

/**
 * Improves the performance of requesting time zone offsets and name keys by
 * caching the results. Time zones that have simple rules or are fixed should
 * not be cached, as it is unlikely to improve performance.
 * <p>
 * CachedDateTimeZone is thread-safe and immutable.
 * 
 * @author Brian S O'Neill
 * @since 1.0
 */
public class CachedDateTimeZone extends DateTimeZone {

    private static final long serialVersionUID = 5472298452022250685L;

    private static final int cInfoCacheMask;

    static {
        Integer i;
        try {
            i = Integer.getInteger("org.joda.time.tz.CachedDateTimeZone.size");
        } catch (SecurityException e) {
            i = null;
        }

        int cacheSize;
        if (i == null) {
            // With a cache size of 512, dates that lie within any 69.7 year
            // period have no cache collisions.
            cacheSize = 512; // (1 << 9)
        } else {
            cacheSize = i.intValue();
            // Ensure cache size is even power of 2.
            cacheSize--;
            int shift = 0;
            while (cacheSize > 0) {
                shift++;
                cacheSize >>= 1;
            }
            cacheSize = 1 << shift;
        }

        cInfoCacheMask = cacheSize - 1;
    }

    /**
     * Returns a new CachedDateTimeZone unless given zone is already cached.
     */
    public static CachedDateTimeZone forZone(DateTimeZone zone) {
        if (zone instanceof CachedDateTimeZone) {
            return (CachedDateTimeZone)zone;
        }
        return new CachedDateTimeZone(zone);
    }

    /*
     * Caching is performed by breaking timeline down into periods of 2^32
     * milliseconds, or about 49.7 days. A year has about 7.3 periods, usually
     * with only 2 time zone offset periods. Most of the 49.7 day periods will
     * have no transition, about one quarter have one transition, and very rare
     * cases have multiple transitions.
     */

    private final DateTimeZone iZone;

    private transient Info[] iInfoCache;

    private CachedDateTimeZone(DateTimeZone zone) {
        super(zone.getID());
        iZone = zone;
        iInfoCache = new Info[cInfoCacheMask + 1];
    }

    private void readObject(java.io.ObjectInputStream in)
        throws java.io.IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        iInfoCache = new Info[cInfoCacheMask + 1];
    }

    /**
     * Returns the DateTimeZone being wrapped.
     */
    public DateTimeZone getUncachedZone() {
        return iZone;
    }

    public String getNameKey(long instant) {
        return getInfo(instant).getNameKey(instant);
    }

    public int getOffset(long instant) {
        return getInfo(instant).getOffset(instant);
    }

    public int getStandardOffset(long instant) {
        return getInfo(instant).getStandardOffset(instant);
    }

    public boolean isFixed() {
        return iZone.isFixed();
    }

    public long nextTransition(long instant) {
        return iZone.nextTransition(instant);
    }

    public long previousTransition(long instant) {
        return iZone.previousTransition(instant);
    }

    public int hashCode() {
        return iZone.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof CachedDateTimeZone) {
            return iZone.equals(((CachedDateTimeZone)obj).iZone);
        }
        return false;
    }

    // Although accessed by multiple threads, this method doesn't need to be
    // synchronized.

    private Info getInfo(long millis) {
        int period = (int)(millis >> 32);
        Info[] cache = iInfoCache;
        int index = period & cInfoCacheMask;
        Info info = cache[index];
        if (info == null || (int)((info.iPeriodStart >> 32)) != period) {
            info = createInfo(millis);
            cache[index] = info;
        }
        return info;
    }

    private Info createInfo(long millis) {
        long periodStart = millis & (0xffffffffL << 32);
        Info info = new Info(iZone, periodStart);
        
        long end = periodStart | 0xffffffffL;
        Info chain = info;
        while (true) {
            long next = iZone.nextTransition(periodStart);
            if (next == periodStart || next > end) {
                break;
            }
            periodStart = next;
            chain = (chain.iNextInfo = new Info(iZone, periodStart));
        }

        return info;
    }

    private final static class Info {
        // For first Info in chain, iPeriodStart's lower 32 bits are clear.
        public final long iPeriodStart;
        public final DateTimeZone iZoneRef;

        Info iNextInfo;

        private String iNameKey;
        private int iOffset = Integer.MIN_VALUE;
        private int iStandardOffset = Integer.MIN_VALUE;

        Info(DateTimeZone zone, long periodStart) {
            iPeriodStart = periodStart;
            iZoneRef = zone;
        }

        public String getNameKey(long millis) {
            if (iNextInfo == null || millis < iNextInfo.iPeriodStart) {
                if (iNameKey == null) {
                    iNameKey = iZoneRef.getNameKey(iPeriodStart);
                }
                return iNameKey;
            }
            return iNextInfo.getNameKey(millis);
        }

        public int getOffset(long millis) {
            if (iNextInfo == null || millis < iNextInfo.iPeriodStart) {
                if (iOffset == Integer.MIN_VALUE) {
                    iOffset = iZoneRef.getOffset(iPeriodStart);
                }
                return iOffset;
            }
            return iNextInfo.getOffset(millis);
        }

        public int getStandardOffset(long millis) {
            if (iNextInfo == null || millis < iNextInfo.iPeriodStart) {
                if (iStandardOffset == Integer.MIN_VALUE) {
                    iStandardOffset = iZoneRef.getStandardOffset(iPeriodStart);
                }
                return iStandardOffset;
            }
            return iNextInfo.getStandardOffset(millis);
        }
    }
}
