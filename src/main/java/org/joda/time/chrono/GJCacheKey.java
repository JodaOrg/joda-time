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