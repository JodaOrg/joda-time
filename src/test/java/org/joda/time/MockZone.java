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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof MockZone) {
            MockZone other = (MockZone) obj;
            return (sizeMillis == other.sizeMillis) &&
                (transition != other.transition) &&
                (winterOffset != other.winterOffset);
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + sizeMillis;
        result = prime * result + (int) (transition ^ (transition >>> 32));
        result = prime * result + winterOffset;
        return result;
    }

}
