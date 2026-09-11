/*
 *  Copyright 2001-2009 Stephen Colebourne
 *  Copyright 2026 Ujjwal Nain
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

import java.util.HashSet;
import java.util.Set;

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

    //-----------------------------------------------------------------------
    public void test_equals_sameFieldDifferentType() {
        DurationField base = new PreciseDurationField(DurationFieldType.seconds(), 1000);
        DelegatedDurationField seconds = new DelegatedDurationField(base, DurationFieldType.seconds());
        DelegatedDurationField minutes = new DelegatedDurationField(base, DurationFieldType.minutes());
        assertEquals(false, seconds.equals(minutes));
        assertEquals(false, minutes.equals(seconds));
    }

    public void test_equals_equalFieldsSameType() {
        DelegatedDurationField field1 = new DelegatedDurationField(
            new PreciseDurationField(DurationFieldType.seconds(), 1000), DurationFieldType.minutes());
        DelegatedDurationField field2 = new DelegatedDurationField(
            new PreciseDurationField(DurationFieldType.seconds(), 1000), DurationFieldType.minutes());
        assertEquals(true, field1.equals(field1));
        assertEquals(true, field1.equals(field2));
        assertEquals(true, field2.equals(field1));
        assertEquals(field1.hashCode(), field2.hashCode());
        assertEquals(false, field1.equals(null));
        assertEquals(false, field1.equals(field1.getWrappedField()));
    }

    public void test_equals_differentFieldsSameType() {
        DelegatedDurationField field1 = new DelegatedDurationField(
            new PreciseDurationField(DurationFieldType.seconds(), 1000), DurationFieldType.minutes());
        DelegatedDurationField field2 = new DelegatedDurationField(
            new PreciseDurationField(DurationFieldType.seconds(), 2000), DurationFieldType.minutes());
        assertEquals(false, field1.equals(field2));
        assertEquals(false, field2.equals(field1));
    }

    public void test_equals_defaultType() {
        DurationField base = new PreciseDurationField(DurationFieldType.seconds(), 1000);
        DelegatedDurationField implicit = new DelegatedDurationField(base);
        DelegatedDurationField nullType = new DelegatedDurationField(base, null);
        DelegatedDurationField explicit = new DelegatedDurationField(base, DurationFieldType.seconds());
        assertEquals(implicit, nullType);
        assertEquals(nullType, explicit);
        assertEquals(implicit, explicit);
        assertEquals(implicit.hashCode(), nullType.hashCode());
        assertEquals(implicit.hashCode(), explicit.hashCode());
    }

    public void test_hashSet() {
        DurationField base = new PreciseDurationField(DurationFieldType.seconds(), 1000);
        Set<DelegatedDurationField> fields = new HashSet<DelegatedDurationField>();
        assertEquals(true, fields.add(new DelegatedDurationField(base)));
        assertEquals(false, fields.add(new DelegatedDurationField(
            new PreciseDurationField(DurationFieldType.seconds(), 1000), DurationFieldType.seconds())));
        assertEquals(true, fields.add(new DelegatedDurationField(base, DurationFieldType.minutes())));
        assertEquals(2, fields.size());
        assertEquals(true, fields.contains(new DelegatedDurationField(base)));
        assertEquals(true, fields.contains(new DelegatedDurationField(base, DurationFieldType.minutes())));
    }

}
