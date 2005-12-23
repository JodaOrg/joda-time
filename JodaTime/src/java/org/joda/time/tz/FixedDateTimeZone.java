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
 * Basic DateTimeZone implementation that has a fixed name key and offsets.
 * <p>
 * FixedDateTimeZone is thread-safe and immutable.
 * 
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class FixedDateTimeZone extends DateTimeZone {

    private static final long serialVersionUID = -3513011772763289092L;

    private final String iNameKey;
    private final int iWallOffset;
    private final int iStandardOffset;

    public FixedDateTimeZone(String id, String nameKey,
                             int wallOffset, int standardOffset) {
        super(id);
        iNameKey = nameKey;
        iWallOffset = wallOffset;
        iStandardOffset = standardOffset;
    }

    public String getNameKey(long instant) {
        return iNameKey;
    }

    public int getOffset(long instant) {
        return iWallOffset;
    }

    public int getStandardOffset(long instant) {
        return iStandardOffset;
    }

    public int getOffsetFromLocal(long instantLocal) {
        return iWallOffset;
    }

    public boolean isFixed() {
        return true;
    }

    public long nextTransition(long instant) {
        return instant;
    }

    public long previousTransition(long instant) {
        return instant;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof FixedDateTimeZone) {
            FixedDateTimeZone other = (FixedDateTimeZone)obj;
            return
                getID().equals(other.getID()) &&
                iStandardOffset == other.iStandardOffset &&
                iWallOffset == other.iWallOffset;
        }
        return false;
    }

    public int hashCode() {
        return getID().hashCode() + 37 * iStandardOffset + 31 * iWallOffset;
    }

}
