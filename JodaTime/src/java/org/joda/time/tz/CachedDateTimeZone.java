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

package org.joda.time.tz;

import org.joda.time.DateTimeZone;

/**
 * Improves the performance of requesting time zone offsets and name keys by
 * caching the results. Time zones that have simple rules or are fixed should
 * not be cached, as it is unlikely to improve performance.
 * 
 * @author Brian S O'Neill
 */
public class CachedDateTimeZone extends DateTimeZone {
    private static final int cInfoCacheMask;

    static {
        Integer i = Integer.getInteger("org.joda.time.tz.CachedDateTimeZone.size");

        // With a cache size of 512, dates that lie within any 69.7 year period
        // have no cache collisions.
        int cacheSize = (i == null) ? 512 : i.intValue();

        // Ensure cache size is even power of 2.
        cacheSize--;
        int shift = 0;
        while (cacheSize > 0) {
            shift++;
            cacheSize >>= 1;
        }
        cacheSize = 1 << shift;

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

    public String getNameKey(long millis) {
        return getInfo(millis).getNameKey(millis);
    }

    public int getOffset(long millis) {
        return getInfo(millis).getOffset(millis);
    }

    public int getStandardOffset(long millis) {
        return getInfo(millis).getStandardOffset(millis);
    }

    public long nextTransition(long millis) {
        return iZone.nextTransition(millis);
    }

    public long previousTransition(long millis) {
        return iZone.previousTransition(millis);
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
