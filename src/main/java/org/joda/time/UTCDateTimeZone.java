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
package org.joda.time;

/**
 * A DateTimeZone implementation for UTC.
 * <p>
 * This exists instead of using FixedDateTimeZone to avoid deadlocks.
 * https://github.com/JodaOrg/joda-time/issues/171
 */
final class UTCDateTimeZone extends DateTimeZone {

    static final DateTimeZone INSTANCE = new UTCDateTimeZone();
    private static final long serialVersionUID = -3513011772763289092L;

    public UTCDateTimeZone() {
        super("UTC");
    }

    @Override
    public String getNameKey(long instant) {
        return "UTC";
    }

    @Override
    public int getOffset(long instant) {
        return 0;
    }

    @Override
    public int getStandardOffset(long instant) {
        return 0;
    }

    @Override
    public int getOffsetFromLocal(long instantLocal) {
        return 0;
    }

    @Override
    public boolean isFixed() {
        return true;
    }

    @Override
    public long nextTransition(long instant) {
        return instant;
    }

    @Override
    public long previousTransition(long instant) {
        return instant;
    }

    @Override
    public java.util.TimeZone toTimeZone() {
        return new java.util.SimpleTimeZone(0, getID());
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof UTCDateTimeZone);
    }

    @Override
    public int hashCode() {
        return getID().hashCode();
    }

}
