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
package org.joda.time.base;

import java.lang.reflect.Field;

import org.joda.time.Chronology;

/**
 * Helper to handle mutable classes that are hard to deal with now there is
 * the Java Memory Model.
 *
 * @author Stephen Colebourne
 * @since 2.0
 */
final class MutableHelper {

    private static final Field DATE_TIME_MILLIS;
    private static final Field DATE_TIME_CHRONO;
    private static final Field DURATION_MILLIS;
    private static final Field SINGLE_FIELD_PERIOD;
    private static final Field INTERVAL_START;
    private static final Field INTERVAL_END;
    private static final Field INTERVAL_CHRONO;
    static {
        try {
            DATE_TIME_MILLIS = BaseDateTime.class.getDeclaredField("iMillis");
            DATE_TIME_MILLIS.setAccessible(true);
            DATE_TIME_CHRONO = BaseDateTime.class.getDeclaredField("iChronology");
            DATE_TIME_CHRONO.setAccessible(true);
            DURATION_MILLIS = BaseDuration.class.getDeclaredField("iMillis");
            DURATION_MILLIS.setAccessible(true);
            SINGLE_FIELD_PERIOD = BaseSingleFieldPeriod.class.getDeclaredField("iPeriod");
            SINGLE_FIELD_PERIOD.setAccessible(true);
            INTERVAL_START = BaseInterval.class.getDeclaredField("iStartMillis");
            INTERVAL_START.setAccessible(true);
            INTERVAL_END = BaseInterval.class.getDeclaredField("iEndMillis");
            INTERVAL_END.setAccessible(true);
            INTERVAL_CHRONO = BaseInterval.class.getDeclaredField("iChronology");
            INTERVAL_CHRONO.setAccessible(true);
            
        } catch (Exception ex) {
            throw new RuntimeException("Joda-Time mutable classes require reflection", ex);
        }
    }

    private MutableHelper() {
    }

    static void setDateTimeMillis(BaseDateTime target, long instant) {
        try {
            DATE_TIME_MILLIS.set(target, instant);
        } catch (Exception ex) {
            throw new RuntimeException("Joda-Time mutable classes require reflection", ex);
        }
    }

    static void setDateTimeChrono(BaseDateTime target, Chronology chrono) {
        try {
            DATE_TIME_CHRONO.set(target, chrono);
        } catch (Exception ex) {
            throw new RuntimeException("Joda-Time mutable classes require reflection", ex);
        }
    }

    static void setDurationMillis(BaseDuration target, long duration) {
        try {
            DURATION_MILLIS.set(target, duration);
        } catch (Exception ex) {
            throw new RuntimeException("Joda-Time mutable classes require reflection", ex);
        }
    }

    static void setSingleFieldPeriodValue(BaseSingleFieldPeriod target, int value) {
        try {
            SINGLE_FIELD_PERIOD.set(target, value);
        } catch (Exception ex) {
            throw new RuntimeException("Joda-Time mutable classes require reflection", ex);
        }
    }

    static void setInterval(BaseInterval target, long startMillis, long endMillis, Chronology chrono) {
        try {
            INTERVAL_START.set(target, startMillis);
            INTERVAL_END.set(target, endMillis);
            INTERVAL_CHRONO.set(target, chrono);
        } catch (Exception ex) {
            throw new RuntimeException("Joda-Time mutable classes require reflection", ex);
        }
    }

}
