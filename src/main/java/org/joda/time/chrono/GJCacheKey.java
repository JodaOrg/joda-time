/*
 *  Copyright 2001-2014 Stephen Colebourne
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
package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.joda.time.Instant;

/**
 * For internal usage in GJChronology only. 
 */
class GJCacheKey {
    private final DateTimeZone zone;
    private final Instant cutoverInstant;
    private final int minDaysInFirstWeek;

    GJCacheKey(DateTimeZone zone, Instant cutoverInstant, int minDaysInFirstWeek) {
        this.zone = zone;
        this.cutoverInstant = cutoverInstant;
        this.minDaysInFirstWeek = minDaysInFirstWeek;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cutoverInstant == null) ? 0 : cutoverInstant.hashCode());
        result = prime * result + minDaysInFirstWeek;
        result = prime * result + ((zone == null) ? 0 : zone.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof GJCacheKey)) {
            return false;
        }
        GJCacheKey other = (GJCacheKey) obj;
        if (cutoverInstant == null) {
            if (other.cutoverInstant != null) {
                return false;
            }
        } else if (!cutoverInstant.equals(other.cutoverInstant)) {
            return false;
        }
        if (minDaysInFirstWeek != other.minDaysInFirstWeek) {
            return false;
        }
        if (zone == null) {
            if (other.zone != null) {
                return false;
            }
        } else if (!zone.equals(other.zone)) {
            return false;
        }
        return true;
    }

}