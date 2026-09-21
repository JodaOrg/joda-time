/*
 *  Copyright 2001-present Stephen Colebourne
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
package org.joda.time.field;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

/**
 * This class is a Junit unit test for DelegatedDurationField.
 */
public class TestDelegatedDurationField extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestDelegatedDurationField.class);
    }

    public TestDelegatedDurationField(String name) {
        super(name);
    }

    public void test_equals_includesType() {
        DurationField test1 = new TestField(MillisDurationField.INSTANCE, DurationFieldType.millis());
        DurationField test1b = new TestField(MillisDurationField.INSTANCE, DurationFieldType.millis());
        DurationField test2 = new TestField(MillisDurationField.INSTANCE, DurationFieldType.seconds());

        assertEquals(test1, test1b);
        assertEquals(test1.hashCode(), test1b.hashCode());
        assertFalse(test1.equals(test2));
        assertFalse(test2.equals(test1));
    }

    private static class TestField extends DelegatedDurationField {
        private static final long serialVersionUID = 1L;

        TestField(DurationField field, DurationFieldType type) {
            super(field, type);
        }
    }
}
