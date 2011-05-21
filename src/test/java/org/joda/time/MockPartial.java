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
package org.joda.time;

import org.joda.time.chrono.ISOChronology;

/**
 * A basic mock testing class for a PartialInstant that doesn't extend AbstractPartialInstant.
 *
 * @author Stephen Colebourne
 */
public class MockPartial implements ReadablePartial {
    
    public static final ReadablePartial EMPTY_INSTANCE = new MockPartial();
    
    public Chronology getChronology() {
        return ISOChronology.getInstanceUTC();
    }
    public int size() {
        return getFields().length;
    }
    public DateTimeFieldType getFieldType(int index) {
        return getFields()[index].getType();
    }
    public DateTimeField getField(int index) {
        return getFields()[index];
    }
    public int getValue(int index) {
        return getValues()[index];
    }
    public int get(DateTimeFieldType field) {
        return 0;
    }
    public boolean isSupported(DateTimeFieldType field) {
        return false;
    }
    public DateTime toDateTime(DateTimeZone zone) {
        return null;
    }
    public DateTime toDateTime(ReadableInstant base) {
        return null;
    }
    public DateTimeField[] getFields() {
        return new DateTimeField[0];
    }
    public int[] getValues() {
        return new int[0];
    }
    public int compareTo(ReadablePartial partial) {
        return 0;
    }
}
