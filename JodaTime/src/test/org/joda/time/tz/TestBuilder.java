/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2005 Stephen Colebourne.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Joda project (http://www.joda.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "Joda" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact licence@joda.org.
 *
 * 5. Products derived from this software may not be called "Joda",
 *    nor may "Joda" appear in their name, without prior written
 *    permission of the Joda project.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE JODA AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Joda project and was originally
 * created by Stephen Colebourne <scolebourne@joda.org>. For more
 * information on the Joda project, please see <http://www.joda.org/>.
 */
package org.joda.time.tz;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

/**
 * Test cases for DateTimeZoneBuilder.
 *
 * @author Brian S O'Neill
 */
public class TestBuilder extends TestCase {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestBuilder.class);
    }

    static final DateTimeFormatter OFFSET_FORMATTER = new DateTimeFormatterBuilder()
        .appendTimeZoneOffset(null, true, 2, 4)
        .toFormatter();

    // Each row is {transition, nameKey, standardOffset, offset}
    static final String[][] AMERICA_LOS_ANGELES_DATA = {
        {null,                            "LMT", "-07:52:58", "-07:52:58"},
        {"1883-11-18T19:52:58.000Z",      "PST", "-08:00", "-08:00"},
        {"1918-03-31T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1918-10-27T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1919-03-30T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1919-10-26T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1942-02-09T02:00:00.000-08:00", "PWT", "-08:00", "-07:00"},
        {"1945-08-14T23:00:00.000Z",      "PPT", "-08:00", "-07:00"},
        {"1945-09-30T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1948-03-14T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1949-01-01T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1950-04-30T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1950-09-24T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1951-04-29T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1951-09-30T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1952-04-27T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1952-09-28T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1953-04-26T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1953-09-27T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1954-04-25T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1954-09-26T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1955-04-24T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1955-09-25T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1956-04-29T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1956-09-30T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1957-04-28T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1957-09-29T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1958-04-27T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1958-09-28T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1959-04-26T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1959-09-27T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1960-04-24T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1960-09-25T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1961-04-30T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1961-09-24T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1962-04-29T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1962-10-28T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1963-04-28T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1963-10-27T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1964-04-26T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1964-10-25T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1965-04-25T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1965-10-31T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1966-04-24T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1966-10-30T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1967-04-30T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1967-10-29T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1968-04-28T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1968-10-27T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1969-04-27T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1969-10-26T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1970-04-26T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1970-10-25T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1971-04-25T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1971-10-31T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1972-04-30T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1972-10-29T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1973-04-29T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1973-10-28T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1974-01-06T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1974-10-27T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1975-02-23T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1975-10-26T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1976-04-25T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1976-10-31T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1977-04-24T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1977-10-30T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1978-04-30T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1978-10-29T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1979-04-29T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1979-10-28T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1980-04-27T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1980-10-26T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1981-04-26T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1981-10-25T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1982-04-25T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1982-10-31T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1983-04-24T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1983-10-30T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1984-04-29T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1984-10-28T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1985-04-28T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1985-10-27T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1986-04-27T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1986-10-26T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1987-04-05T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1987-10-25T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1988-04-03T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1988-10-30T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1989-04-02T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1989-10-29T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1990-04-01T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1990-10-28T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1991-04-07T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1991-10-27T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1992-04-05T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1992-10-25T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1993-04-04T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1993-10-31T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1994-04-03T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1994-10-30T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1995-04-02T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1995-10-29T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1996-04-07T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1996-10-27T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1997-04-06T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1997-10-26T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1998-04-05T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1998-10-25T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"1999-04-04T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"1999-10-31T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
        {"2000-04-02T02:00:00.000-08:00", "PDT", "-08:00", "-07:00"},
        {"2000-10-29T02:00:00.000-07:00", "PST", "-08:00", "-08:00"},
    };

    static DateTimeZone buildAmericaLosAngeles() {
        return new DateTimeZoneBuilder()
            .addCutover(-2147483648, 'w', 1, 1, 0, false, 0)
            .setStandardOffset(-28378000)
            .setFixedSavings("LMT", 0)
            .addCutover(1883, 'w', 11, 18, 0, false, 43200000)
            .setStandardOffset(-28800000)
            .addRecurringSavings("PDT", 3600000, 1918, 1919, 'w',  3, -1, 7, false, 7200000)
            .addRecurringSavings("PST",       0, 1918, 1919, 'w', 10, -1, 7, false, 7200000)
            .addRecurringSavings("PWT", 3600000, 1942, 1942, 'w',  2,  9, 0, false, 7200000)
            .addRecurringSavings("PPT", 3600000, 1945, 1945, 'u',  8, 14, 0, false, 82800000)
            .addRecurringSavings("PST",       0, 1945, 1945, 'w',  9, 30, 0, false, 7200000)
            .addRecurringSavings("PDT", 3600000, 1948, 1948, 'w',  3, 14, 0, false, 7200000)
            .addRecurringSavings("PST",       0, 1949, 1949, 'w',  1,  1, 0, false, 7200000)
            .addRecurringSavings("PDT", 3600000, 1950, 1966, 'w',  4, -1, 7, false, 7200000)
            .addRecurringSavings("PST",       0, 1950, 1961, 'w',  9, -1, 7, false, 7200000)
            .addRecurringSavings("PST",       0, 1962, 1966, 'w', 10, -1, 7, false, 7200000)
            .addRecurringSavings("PST",       0, 1967, 2147483647, 'w', 10, -1, 7, false, 7200000)
            .addRecurringSavings("PDT", 3600000, 1967, 1973, 'w', 4, -1,  7, false, 7200000)
            .addRecurringSavings("PDT", 3600000, 1974, 1974, 'w', 1,  6,  0, false, 7200000)
            .addRecurringSavings("PDT", 3600000, 1975, 1975, 'w', 2, 23,  0, false, 7200000)
            .addRecurringSavings("PDT", 3600000, 1976, 1986, 'w', 4, -1,  7, false, 7200000)
            .addRecurringSavings("PDT", 3600000, 1987, 2147483647, 'w', 4, 1, 7, true, 7200000)
            .toDateTimeZone("America/Los_Angeles");
    }

    private DateTimeZone originalDateTimeZone = null;

    public TestBuilder(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        originalDateTimeZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    protected void tearDown() throws Exception {
        DateTimeZone.setDefault(originalDateTimeZone);
    }

    public void testID() {
        DateTimeZone tz = buildAmericaLosAngeles();
        assertEquals("America/Los_Angeles", tz.getID());
    }

    public void testForwardTransitions() {
        DateTimeZone tz = buildAmericaLosAngeles();
        testForwardTransitions(tz, AMERICA_LOS_ANGELES_DATA);
    }

    private void testForwardTransitions(DateTimeZone tz, String[][] data) {
        long instant = Long.MIN_VALUE;
        for (int i=0; i<data.length; i++) {
            String[] row = data[i];
            long expectedInstant = instant;
            if (row[0] != null) {
                instant = tz.nextTransition(instant);
                expectedInstant = new DateTime(row[0]).getMillis();
            }

            String expectedKey = row[1];
            int expectedStandardOffset = -(int) OFFSET_FORMATTER.parseMillis(row[2]);
            int expectedOffset = -(int) OFFSET_FORMATTER.parseMillis(row[3]);

            assertEquals(expectedInstant, instant);
            assertEquals(expectedKey, tz.getNameKey(instant));
            assertEquals(expectedStandardOffset, tz.getStandardOffset(instant));
            assertEquals(expectedOffset, tz.getOffset(instant));

            // Sample a few instants between transitions.
            if (i < data.length - 1) {
                long nextInstant = new DateTime(data[i + 1][0]).getMillis();
                long span = (nextInstant - instant) / 10;
                for (int j=1; j<10; j++) {
                    long between = instant + j * span;
                    assertEquals(expectedKey, tz.getNameKey(between));
                    assertEquals(expectedStandardOffset, tz.getStandardOffset(between));
                    assertEquals(expectedOffset, tz.getOffset(between));
                }
            }
        }
    }

    public void testReverseTransitions() {
        DateTimeZone tz = buildAmericaLosAngeles();
        testReverseTransitions(tz, AMERICA_LOS_ANGELES_DATA);
    }

    private void testReverseTransitions(DateTimeZone tz, String[][] data) {
        long instant = new DateTime(data[data.length - 1][0]).getMillis();
        for (int i=data.length; --i>=1; ) {
            String[] row = data[i];
            String[] prevRow = data[i - 1];
            instant = tz.previousTransition(instant);

            long expectedInstant = new DateTime(row[0]).getMillis() - 1;
            String expectedKey = prevRow[1];
            int expectedStandardOffset = -(int) OFFSET_FORMATTER.parseMillis(prevRow[2]);
            int expectedOffset = -(int) OFFSET_FORMATTER.parseMillis(prevRow[3]);

            assertEquals(expectedInstant, instant);
            assertEquals(expectedKey, tz.getNameKey(instant));
            assertEquals(expectedStandardOffset, tz.getStandardOffset(instant));
            assertEquals(expectedOffset, tz.getOffset(instant));
        }
    }
}
