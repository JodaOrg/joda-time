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
package org.joda.time.chrono.gj;

import org.joda.time.DateTimeFieldType;
import org.joda.time.field.ImpreciseDateTimeField;

/**
 * 
 * @author Brian S O'Neill
 */
abstract class TestGJDateTimeField extends ImpreciseDateTimeField {
    protected final TestGJChronology iChronology;

    public TestGJDateTimeField(DateTimeFieldType type, long unitMillis, TestGJChronology chrono) {
        super(type, unitMillis);
        iChronology = chrono;
    }

    public boolean isLenient() {
        return false;
    }

    public long add(long instant, int value) {
        return add(instant, (long)value);
    }

    public abstract long add(long instant, long value);

}
