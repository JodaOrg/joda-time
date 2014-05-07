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
package org.joda.time.format;

import java.util.Locale;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;

/**
 * Test.
 */
public class TestDateTimeParserBucket extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final Chronology BUDDHIST_UTC = BuddhistChronology.getInstanceUTC();
    private static final Chronology BUDDHIST_PARIS = BuddhistChronology.getInstance(PARIS);
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();
    private static final DateTimeZone ZONE_0400 = DateTimeZone.forOffsetHours(4);
    private static final Chronology ISO_0400 = ISOChronology.getInstance(ZONE_0400);
    private static final int MILLIS_PER_HOUR = 3600000;
    private static final int MILLIS_PER_MINUTE = 60000;
    private static final int OFFSET_0400 = 4 * MILLIS_PER_HOUR;
    private static final Locale LOCALE = Locale.CANADA;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestDateTimeParserBucket.class);
    }

    public TestDateTimeParserBucket(String name) {
        super(name);
    }

    //-----------------------------------------------------------------------
    @SuppressWarnings("deprecation")
    public void testConstructor_3arg() {
        DateTimeParserBucket test = new DateTimeParserBucket(100, BUDDHIST_PARIS, LOCALE);
        assertEquals(BUDDHIST_UTC, test.getChronology());
        assertEquals(LOCALE, test.getLocale());
        assertEquals(null, test.getPivotYear());
        assertEquals(null, test.getOffsetInteger());
        assertEquals(PARIS, test.getZone());
    }

    @SuppressWarnings("deprecation")
    public void testConstructor_4arg() {
        DateTimeParserBucket test = new DateTimeParserBucket(100, BUDDHIST_PARIS, LOCALE, 2010);
        assertEquals(BUDDHIST_UTC, test.getChronology());
        assertEquals(LOCALE, test.getLocale());
        assertEquals((Integer) 2010, test.getPivotYear());
        assertEquals(null, test.getOffsetInteger());
        assertEquals(PARIS, test.getZone());
    }

    public void testConstructor_5arg() {
        DateTimeParserBucket test = new DateTimeParserBucket(100, BUDDHIST_PARIS, LOCALE, 2010, 2001);
        assertEquals(BUDDHIST_UTC, test.getChronology());
        assertEquals(LOCALE, test.getLocale());
        assertEquals((Integer) 2010, test.getPivotYear());
        assertEquals(null, test.getOffsetInteger());
        assertEquals(PARIS, test.getZone());
    }

    @SuppressWarnings("deprecation")
    public void testSetPivotYear() {
        DateTimeParserBucket test = new DateTimeParserBucket(100, BUDDHIST_PARIS, LOCALE, 2010, 2001);
        assertEquals((Integer) 2010, test.getPivotYear());
        test.setPivotYear(null);
        assertEquals(null, test.getPivotYear());
        test.setPivotYear(2030);
        assertEquals((Integer) 2030, test.getPivotYear());
    }

    public void testSetOffset() {
        DateTimeParserBucket test = new DateTimeParserBucket(100, BUDDHIST_PARIS, LOCALE, 2010, 2001);
        assertEquals(null, test.getOffsetInteger());
        test.setOffset((Integer) 1000);
        assertEquals((Integer) 1000, test.getOffsetInteger());
        test.setOffset(null);
        assertEquals(null, test.getOffsetInteger());
    }

    public void testSetZone() {
        DateTimeParserBucket test = new DateTimeParserBucket(100, BUDDHIST_PARIS, LOCALE, 2010, 2001);
        assertEquals(PARIS, test.getZone());
        test.setZone(LONDON);
        assertEquals(LONDON, test.getZone());
    }

    public void testCompute() {
        DateTimeParserBucket test = new DateTimeParserBucket(100, ISO_0400, LOCALE, 2000, 2000);
        assertEquals(100 - OFFSET_0400, test.computeMillis());
        assertEquals(100 - OFFSET_0400, test.computeMillis(false));
        // note that computeMillis(true) differs depending on whether fields are saved or not
        assertEquals(100 - OFFSET_0400, test.computeMillis(true));
    }

    public void testSaveCompute() {
        DateTimeParserBucket test = new DateTimeParserBucket(100, ISO_0400, LOCALE, 2000, 2000);
        test.saveField(DateTimeFieldType.hourOfDay(), 2);
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis());
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
        test.saveField(DateTimeFieldType.hourOfDay(), 5);
        assertEquals(5 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
        assertEquals(5 * MILLIS_PER_HOUR - OFFSET_0400, test.computeMillis(true));
        assertEquals(5 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
    }

    public void testSaveRestoreState() {
        DateTimeParserBucket test = new DateTimeParserBucket(100, ISO_0400, LOCALE, 2000, 2000);
        test.saveField(DateTimeFieldType.hourOfDay(), 2);
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
        Object state = test.saveState();
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
        test.saveField(DateTimeFieldType.minuteOfHour(), 6);
        assertEquals(2 * MILLIS_PER_HOUR + 6 * MILLIS_PER_MINUTE + 100 - OFFSET_0400, test.computeMillis(false));
        assertEquals(true, test.restoreState(state));
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
        test.saveField(DateTimeFieldType.minuteOfHour(), 7);
        assertEquals(2 * MILLIS_PER_HOUR + 7 * MILLIS_PER_MINUTE + 100 - OFFSET_0400, test.computeMillis(false));
        assertEquals(true, test.restoreState(state));
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
    }

    public void testSaveRestoreState_avoidSideEffects() {
        // computeMillis() has side effects, so check works without it
        DateTimeParserBucket test = new DateTimeParserBucket(100, ISO_0400, LOCALE, 2000, 2000);
        test.saveField(DateTimeFieldType.hourOfDay(), 2);
        Object state = test.saveState();
        test.saveField(DateTimeFieldType.minuteOfHour(), 6);
        assertEquals(true, test.restoreState(state));
        test.saveField(DateTimeFieldType.minuteOfHour(), 7);
        assertEquals(2 * MILLIS_PER_HOUR + 7 * MILLIS_PER_MINUTE + 100 - OFFSET_0400, test.computeMillis(false));
        assertEquals(true, test.restoreState(state));
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
    }

    public void testSaveRestoreState_offset() {
        DateTimeParserBucket test = new DateTimeParserBucket(100, ISO_0400, LOCALE, 2000, 2000);
        test.saveField(DateTimeFieldType.hourOfDay(), 2);
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
        Object state = test.saveState();
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
        test.setOffset((Integer) 0);
        assertEquals(2 * MILLIS_PER_HOUR + 100, test.computeMillis(false));
        assertEquals(true, test.restoreState(state));
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
    }

    public void testSaveRestoreState_zone() {
        DateTimeParserBucket test = new DateTimeParserBucket(100, ISO_0400, LOCALE, 2000, 2000);
        test.saveField(DateTimeFieldType.hourOfDay(), 2);
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
        Object state = test.saveState();
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
        test.setZone(DateTimeZone.UTC);
        assertEquals(2 * MILLIS_PER_HOUR + 100, test.computeMillis(false));
        assertEquals(true, test.restoreState(state));
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
    }

    public void testSaveRestoreState_text() {
        DateTimeParserBucket test = new DateTimeParserBucket(100, ISO_0400, LOCALE, 2000, 2000);
        test.saveField(DateTimeFieldType.hourOfDay(), "2", Locale.ENGLISH);
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
        Object state = test.saveState();
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
        test.saveField(DateTimeFieldType.minuteOfHour(), "6", Locale.ENGLISH);
        assertEquals(2 * MILLIS_PER_HOUR + 6 * MILLIS_PER_MINUTE + 100 - OFFSET_0400, test.computeMillis(false));
        assertEquals(true, test.restoreState(state));
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
    }

    public void testSaveRestoreState_twoStates() {
        DateTimeParserBucket test = new DateTimeParserBucket(100, ISO_0400, LOCALE, 2000, 2000);
        test.saveField(DateTimeFieldType.hourOfDay(), 2);
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
        Object state1 = test.saveState();
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
        test.saveField(DateTimeFieldType.minuteOfHour(), 6);
        assertEquals(2 * MILLIS_PER_HOUR + 6 * MILLIS_PER_MINUTE + 100 - OFFSET_0400, test.computeMillis(false));
        Object state2 = test.saveState();
        assertEquals(2 * MILLIS_PER_HOUR + 6 * MILLIS_PER_MINUTE + 100 - OFFSET_0400, test.computeMillis(false));
        test.saveField(DateTimeFieldType.secondOfMinute(), 8);
        assertEquals(2 * MILLIS_PER_HOUR + 6 * MILLIS_PER_MINUTE + 8000 + 100 - OFFSET_0400, test.computeMillis(false));
        assertEquals(true, test.restoreState(state2));
        assertEquals(2 * MILLIS_PER_HOUR + 6 * MILLIS_PER_MINUTE + 100 - OFFSET_0400, test.computeMillis(false));
        assertEquals(true, test.restoreState(state1));
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
        assertEquals(true, test.restoreState(state2));
        assertEquals(2 * MILLIS_PER_HOUR + 6 * MILLIS_PER_MINUTE + 100 - OFFSET_0400, test.computeMillis(false));
        assertEquals(true, test.restoreState(state1));
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
    }

    public void testSaveRestoreState_sameStates() {
        DateTimeParserBucket test = new DateTimeParserBucket(100, ISO_0400, LOCALE, 2000, 2000);
        test.saveField(DateTimeFieldType.hourOfDay(), 2);
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
        Object state1 = test.saveState();
        Object state2 = test.saveState();
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
        test.saveField(DateTimeFieldType.minuteOfHour(), 6);
        assertEquals(2 * MILLIS_PER_HOUR + 6 * MILLIS_PER_MINUTE + 100 - OFFSET_0400, test.computeMillis(false));
        assertEquals(true, test.restoreState(state2));
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
        test.saveField(DateTimeFieldType.minuteOfHour(), 8);
        assertEquals(2 * MILLIS_PER_HOUR + 8 * MILLIS_PER_MINUTE + 100 - OFFSET_0400, test.computeMillis(false));
        assertEquals(true, test.restoreState(state1));
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
        assertEquals(true, test.restoreState(state2));
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
        assertEquals(true, test.restoreState(state1));
        assertEquals(2 * MILLIS_PER_HOUR + 100 - OFFSET_0400, test.computeMillis(false));
    }

    public void testSaveRestoreState_badType() {
        DateTimeParserBucket bucket1 = new DateTimeParserBucket(100, ISO_0400, LOCALE, 2000, 2000);
        DateTimeParserBucket bucket2 = new DateTimeParserBucket(100, BUDDHIST_PARIS, LOCALE, 2000, 2000);
        assertEquals(false, bucket1.restoreState(null));
        assertEquals(false, bucket1.restoreState(""));
        assertEquals(false, bucket2.restoreState(bucket1.saveState()));
    }

    //-------------------------------------------------------------------------
    public void testReset() {
        DateTimeParserBucket test = new DateTimeParserBucket(100, ISO_0400, LOCALE, 2000, 2000);
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(LOCALE, test.getLocale());
        assertEquals((Integer) 2000, test.getPivotYear());
        assertEquals(null, test.getOffsetInteger());
        assertEquals(ZONE_0400, test.getZone());
        
        test.setOffset((Integer) 200);
        test.setZone(LONDON);
        test.saveField(DateTimeFieldType.hourOfDay(), 2);
        assertEquals(2 * MILLIS_PER_HOUR + 100 - 200, test.computeMillis(false));
        assertEquals((Integer) 200, test.getOffsetInteger());
        assertEquals(LONDON, test.getZone());
        
        test.reset();
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(LOCALE, test.getLocale());
        assertEquals((Integer) 2000, test.getPivotYear());
        assertEquals(null, test.getOffsetInteger());
        assertEquals(ZONE_0400, test.getZone());
        assertEquals(100 - OFFSET_0400, test.computeMillis(false));
    }

    public void testParse() {
        DateTimeParserBucket test = new DateTimeParserBucket(0, ISO_0400, LOCALE, 2000, 2000);
        DateTimeParser parser = new DateTimeParser() {
            public int parseInto(DateTimeParserBucket bucket, String text, int position) {
                bucket.saveField(DateTimeFieldType.hourOfDay(), 2);
                bucket.saveField(DateTimeFieldType.minuteOfHour(), 6);
                return position + 1;
            }
            public int estimateParsedLength() {
                return 1;
            }
        };
        long millis = test.parseMillis(parser, "A");
        assertEquals(2 * MILLIS_PER_HOUR + 6 * MILLIS_PER_MINUTE - OFFSET_0400, millis);
        millis = test.parseMillis(parser, "B");
        assertEquals(2 * MILLIS_PER_HOUR + 6 * MILLIS_PER_MINUTE - OFFSET_0400, millis);
        millis = test.parseMillis(parser, "C");
        assertEquals(2 * MILLIS_PER_HOUR + 6 * MILLIS_PER_MINUTE - OFFSET_0400, millis);
    }

}
