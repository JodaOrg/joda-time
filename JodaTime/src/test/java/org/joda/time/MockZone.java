/*
 *  Copyright 2001-2007 Stephen Colebourne
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

public class MockZone extends DateTimeZone {

    long transition;
    int winterOffset;
    int sizeMillis;

    public MockZone(long transition, int winterOffset, int sizeSecs) {
        super("MockZone");
        this.transition = transition;
        this.winterOffset = winterOffset;
        this.sizeMillis = sizeSecs * 1000;
    }

    public int getOffset(long instant) {
        return (instant < transition ? winterOffset : winterOffset + sizeMillis);
    }

    public int getStandardOffset(long instant) {
        return winterOffset;
    }

    public long nextTransition(long instant) {
        return (instant < transition ? transition : transition + 180L * DateTimeConstants.MILLIS_PER_DAY);
    }

    public long previousTransition(long instant) {
        return (instant > transition ? transition : transition - 180L * DateTimeConstants.MILLIS_PER_DAY);
    }

    public boolean isFixed() {
        return false;
    }

    public String getNameKey(long instant) {
        return null;
    }

    public boolean equals(Object object) {
        return false;
    }
}
