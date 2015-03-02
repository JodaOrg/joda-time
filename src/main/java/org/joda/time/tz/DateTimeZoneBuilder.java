/*
 *  Copyright 2001-2013 Stephen Colebourne
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

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;

/**
 * DateTimeZoneBuilder allows complex DateTimeZones to be constructed. Since
 * creating a new DateTimeZone this way is a relatively expensive operation,
 * built zones can be written to a file. Reading back the encoded data is a
 * quick operation.
 * <p>
 * DateTimeZoneBuilder itself is mutable and not thread-safe, but the
 * DateTimeZone objects that it builds are thread-safe and immutable.
 * <p>
 * It is intended that {@link ZoneInfoCompiler} be used to read time zone data
 * files, indirectly calling DateTimeZoneBuilder. The following complex
 * example defines the America/Los_Angeles time zone, with all historical
 * transitions:
 * 
 * <pre>
 * DateTimeZone America_Los_Angeles = new DateTimeZoneBuilder()
 *     .addCutover(-2147483648, 'w', 1, 1, 0, false, 0)
 *     .setStandardOffset(-28378000)
 *     .setFixedSavings("LMT", 0)
 *     .addCutover(1883, 'w', 11, 18, 0, false, 43200000)
 *     .setStandardOffset(-28800000)
 *     .addRecurringSavings("PDT", 3600000, 1918, 1919, 'w',  3, -1, 7, false, 7200000)
 *     .addRecurringSavings("PST",       0, 1918, 1919, 'w', 10, -1, 7, false, 7200000)
 *     .addRecurringSavings("PWT", 3600000, 1942, 1942, 'w',  2,  9, 0, false, 7200000)
 *     .addRecurringSavings("PPT", 3600000, 1945, 1945, 'u',  8, 14, 0, false, 82800000)
 *     .addRecurringSavings("PST",       0, 1945, 1945, 'w',  9, 30, 0, false, 7200000)
 *     .addRecurringSavings("PDT", 3600000, 1948, 1948, 'w',  3, 14, 0, false, 7200000)
 *     .addRecurringSavings("PST",       0, 1949, 1949, 'w',  1,  1, 0, false, 7200000)
 *     .addRecurringSavings("PDT", 3600000, 1950, 1966, 'w',  4, -1, 7, false, 7200000)
 *     .addRecurringSavings("PST",       0, 1950, 1961, 'w',  9, -1, 7, false, 7200000)
 *     .addRecurringSavings("PST",       0, 1962, 1966, 'w', 10, -1, 7, false, 7200000)
 *     .addRecurringSavings("PST",       0, 1967, 2147483647, 'w', 10, -1, 7, false, 7200000)
 *     .addRecurringSavings("PDT", 3600000, 1967, 1973, 'w', 4, -1,  7, false, 7200000)
 *     .addRecurringSavings("PDT", 3600000, 1974, 1974, 'w', 1,  6,  0, false, 7200000)
 *     .addRecurringSavings("PDT", 3600000, 1975, 1975, 'w', 2, 23,  0, false, 7200000)
 *     .addRecurringSavings("PDT", 3600000, 1976, 1986, 'w', 4, -1,  7, false, 7200000)
 *     .addRecurringSavings("PDT", 3600000, 1987, 2147483647, 'w', 4, 1, 7, true, 7200000)
 *     .toDateTimeZone("America/Los_Angeles", true);
 * </pre>
 *
 * @author Brian S O'Neill
 * @see ZoneInfoCompiler
 * @see ZoneInfoProvider
 * @since 1.0
 */
public class DateTimeZoneBuilder {
    /**
     * Decodes a built DateTimeZone from the given stream, as encoded by
     * writeTo.
     *
     * @param in input stream to read encoded DateTimeZone from.
     * @param id time zone id to assign
     */
    public static DateTimeZone readFrom(InputStream in, String id) throws IOException {
        if (in instanceof DataInput) {
            return readFrom((DataInput)in, id);
        } else {
            return readFrom((DataInput)new DataInputStream(in), id);
        }
    }

    /**
     * Decodes a built DateTimeZone from the given stream, as encoded by
     * writeTo.
     *
     * @param in input stream to read encoded DateTimeZone from.
     * @param id time zone id to assign
     */
    public static DateTimeZone readFrom(DataInput in, String id) throws IOException {
        switch (in.readUnsignedByte()) {
        case 'F':
            DateTimeZone fixed = new FixedDateTimeZone
                (id, in.readUTF(), (int)readMillis(in), (int)readMillis(in));
            if (fixed.equals(DateTimeZone.UTC)) {
                fixed = DateTimeZone.UTC;
            }
            return fixed;
        case 'C':
            return CachedDateTimeZone.forZone(PrecalculatedZone.readFrom(in, id));
        case 'P':
            return PrecalculatedZone.readFrom(in, id);
        default:
            throw new IOException("Invalid encoding");
        }
    }

    /**
     * Millisecond encoding formats:
     *
     * upper two bits  units       field length  approximate range
     * ---------------------------------------------------------------
     * 00              30 minutes  1 byte        +/- 16 hours
     * 01              minutes     4 bytes       +/- 1020 years
     * 10              seconds     5 bytes       +/- 4355 years
     * 11              millis      9 bytes       +/- 292,000,000 years
     *
     * Remaining bits in field form signed offset from 1970-01-01T00:00:00Z.
     */
    static void writeMillis(DataOutput out, long millis) throws IOException {
        if (millis % (30 * 60000L) == 0) {
            // Try to write in 30 minute units.
            long units = millis / (30 * 60000L);
            if (((units << (64 - 6)) >> (64 - 6)) == units) {
                // Form 00 (6 bits effective precision)
                out.writeByte((int)(units & 0x3f));
                return;
            }
        }

        if (millis % 60000L == 0) {
            // Try to write minutes.
            long minutes = millis / 60000L;
            if (((minutes << (64 - 30)) >> (64 - 30)) == minutes) {
                // Form 01 (30 bits effective precision)
                out.writeInt(0x40000000 | (int)(minutes & 0x3fffffff));
                return;
            }
        }
        
        if (millis % 1000L == 0) {
            // Try to write seconds.
            long seconds = millis / 1000L;
            if (((seconds << (64 - 38)) >> (64 - 38)) == seconds) {
                // Form 10 (38 bits effective precision)
                out.writeByte(0x80 | (int)((seconds >> 32) & 0x3f));
                out.writeInt((int)(seconds & 0xffffffff));
                return;
            }
        }

        // Write milliseconds either because the additional precision is
        // required or the minutes didn't fit in the field.
        
        // Form 11 (64-bits effective precision, but write as if 70 bits)
        out.writeByte(millis < 0 ? 0xff : 0xc0);
        out.writeLong(millis);
    }

    /**
     * Reads encoding generated by writeMillis.
     */
    static long readMillis(DataInput in) throws IOException {
        int v = in.readUnsignedByte();
        switch (v >> 6) {
        case 0: default:
            // Form 00 (6 bits effective precision)
            v = (v << (32 - 6)) >> (32 - 6);
            return v * (30 * 60000L);

        case 1:
            // Form 01 (30 bits effective precision)
            v = (v << (32 - 6)) >> (32 - 30);
            v |= (in.readUnsignedByte()) << 16;
            v |= (in.readUnsignedByte()) << 8;
            v |= (in.readUnsignedByte());
            return v * 60000L;

        case 2:
            // Form 10 (38 bits effective precision)
            long w = (((long)v) << (64 - 6)) >> (64 - 38);
            w |= (in.readUnsignedByte()) << 24;
            w |= (in.readUnsignedByte()) << 16;
            w |= (in.readUnsignedByte()) << 8;
            w |= (in.readUnsignedByte());
            return w * 1000L;

        case 3:
            // Form 11 (64-bits effective precision)
            return in.readLong();
        }
    }

    private static DateTimeZone buildFixedZone(String id, String nameKey,
                                               int wallOffset, int standardOffset) {
        if ("UTC".equals(id) && id.equals(nameKey) &&
            wallOffset == 0 && standardOffset == 0) {
            return DateTimeZone.UTC;
        }
        return new FixedDateTimeZone(id, nameKey, wallOffset, standardOffset);
    }

    // List of RuleSets.
    private final ArrayList<RuleSet> iRuleSets;

    public DateTimeZoneBuilder() {
        iRuleSets = new ArrayList<RuleSet>(10);
    }

    /**
     * Adds a cutover for added rules. The standard offset at the cutover
     * defaults to 0. Call setStandardOffset afterwards to change it.
     *
     * @param year  the year of cutover
     * @param mode 'u' - cutover is measured against UTC, 'w' - against wall
     *  offset, 's' - against standard offset
     * @param monthOfYear  the month from 1 (January) to 12 (December)
     * @param dayOfMonth  if negative, set to ((last day of month) - ~dayOfMonth).
     *  For example, if -1, set to last day of month
     * @param dayOfWeek  from 1 (Monday) to 7 (Sunday), if 0 then ignore
     * @param advanceDayOfWeek  if dayOfMonth does not fall on dayOfWeek, advance to
     *  dayOfWeek when true, retreat when false.
     * @param millisOfDay  additional precision for specifying time of day of cutover
     */
    public DateTimeZoneBuilder addCutover(int year,
                                          char mode,
                                          int monthOfYear,
                                          int dayOfMonth,
                                          int dayOfWeek,
                                          boolean advanceDayOfWeek,
                                          int millisOfDay)
    {
        if (iRuleSets.size() > 0) {
            OfYear ofYear = new OfYear
                (mode, monthOfYear, dayOfMonth, dayOfWeek, advanceDayOfWeek, millisOfDay);
            RuleSet lastRuleSet = iRuleSets.get(iRuleSets.size() - 1);
            lastRuleSet.setUpperLimit(year, ofYear);
        }
        iRuleSets.add(new RuleSet());
        return this;
    }

    /**
     * Sets the standard offset to use for newly added rules until the next
     * cutover is added.
     * @param standardOffset  the standard offset in millis
     */
    public DateTimeZoneBuilder setStandardOffset(int standardOffset) {
        getLastRuleSet().setStandardOffset(standardOffset);
        return this;
    }

    /**
     * Set a fixed savings rule at the cutover.
     */
    public DateTimeZoneBuilder setFixedSavings(String nameKey, int saveMillis) {
        getLastRuleSet().setFixedSavings(nameKey, saveMillis);
        return this;
    }

    /**
     * Add a recurring daylight saving time rule.
     *
     * @param nameKey  the name key of new rule
     * @param saveMillis  the milliseconds to add to standard offset
     * @param fromYear  the first year that rule is in effect, MIN_VALUE indicates
     * beginning of time
     * @param toYear  the last year (inclusive) that rule is in effect, MAX_VALUE
     *  indicates end of time
     * @param mode  'u' - transitions are calculated against UTC, 'w' -
     *  transitions are calculated against wall offset, 's' - transitions are
     *  calculated against standard offset
     * @param monthOfYear  the month from 1 (January) to 12 (December)
     * @param dayOfMonth  if negative, set to ((last day of month) - ~dayOfMonth).
     *  For example, if -1, set to last day of month
     * @param dayOfWeek  from 1 (Monday) to 7 (Sunday), if 0 then ignore
     * @param advanceDayOfWeek  if dayOfMonth does not fall on dayOfWeek, advance to
     *  dayOfWeek when true, retreat when false.
     * @param millisOfDay  additional precision for specifying time of day of transitions
     */
    public DateTimeZoneBuilder addRecurringSavings(String nameKey, int saveMillis,
                                                   int fromYear, int toYear,
                                                   char mode,
                                                   int monthOfYear,
                                                   int dayOfMonth,
                                                   int dayOfWeek,
                                                   boolean advanceDayOfWeek,
                                                   int millisOfDay)
    {
        if (fromYear <= toYear) {
            OfYear ofYear = new OfYear
                (mode, monthOfYear, dayOfMonth, dayOfWeek, advanceDayOfWeek, millisOfDay);
            Recurrence recurrence = new Recurrence(ofYear, nameKey, saveMillis);
            Rule rule = new Rule(recurrence, fromYear, toYear);
            getLastRuleSet().addRule(rule);
        }
        return this;
    }

    private RuleSet getLastRuleSet() {
        if (iRuleSets.size() == 0) {
            addCutover(Integer.MIN_VALUE, 'w', 1, 1, 0, false, 0);
        }
        return iRuleSets.get(iRuleSets.size() - 1);
    }
    
    /**
     * Processes all the rules and builds a DateTimeZone.
     *
     * @param id  time zone id to assign
     * @param outputID  true if the zone id should be output
     */
    public DateTimeZone toDateTimeZone(String id, boolean outputID) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        // Discover where all the transitions occur and store the results in
        // these lists.
        ArrayList<Transition> transitions = new ArrayList<Transition>();

        // Tail zone picks up remaining transitions in the form of an endless
        // DST cycle.
        DSTZone tailZone = null;

        long millis = Long.MIN_VALUE;
        int saveMillis = 0;
            
        int ruleSetCount = iRuleSets.size();
        for (int i=0; i<ruleSetCount; i++) {
            RuleSet rs = iRuleSets.get(i);
            Transition next = rs.firstTransition(millis);
            if (next == null) {
                continue;
            }
            addTransition(transitions, next);
            millis = next.getMillis();
            saveMillis = next.getSaveMillis();

            // Copy it since we're going to destroy it.
            rs = new RuleSet(rs);

            while ((next = rs.nextTransition(millis, saveMillis)) != null) {
                if (addTransition(transitions, next)) {
                    if (tailZone != null) {
                        // Got the extra transition before DSTZone.
                        break;
                    }
                }
                millis = next.getMillis();
                saveMillis = next.getSaveMillis();
                if (tailZone == null && i == ruleSetCount - 1) {
                    tailZone = rs.buildTailZone(id);
                    // If tailZone is not null, don't break out of main loop until
                    // at least one more transition is calculated. This ensures a
                    // correct 'seam' to the DSTZone.
                }
            }

            millis = rs.getUpperLimit(saveMillis);
        }

        // Check if a simpler zone implementation can be returned.
        if (transitions.size() == 0) {
            if (tailZone != null) {
                // This shouldn't happen, but handle just in case.
                return tailZone;
            }
            return buildFixedZone(id, "UTC", 0, 0);
        }
        if (transitions.size() == 1 && tailZone == null) {
            Transition tr = transitions.get(0);
            return buildFixedZone(id, tr.getNameKey(),
                                  tr.getWallOffset(), tr.getStandardOffset());
        }

        PrecalculatedZone zone = PrecalculatedZone.create(id, outputID, transitions, tailZone);
        if (zone.isCachable()) {
            return CachedDateTimeZone.forZone(zone);
        }
        return zone;
    }

    private boolean addTransition(ArrayList<Transition> transitions, Transition tr) {
        int size = transitions.size();
        if (size == 0) {
            transitions.add(tr);
            return true;
        }

        Transition last = transitions.get(size - 1);
        if (!tr.isTransitionFrom(last)) {
            return false;
        }

        // If local time of new transition is same as last local time, just
        // replace last transition with new one.
        int offsetForLast = 0;
        if (size >= 2) {
            offsetForLast = transitions.get(size - 2).getWallOffset();
        }
        int offsetForNew = last.getWallOffset();

        long lastLocal = last.getMillis() + offsetForLast;
        long newLocal = tr.getMillis() + offsetForNew;

        if (newLocal != lastLocal) {
            transitions.add(tr);
            return true;
        }

        transitions.remove(size - 1);
        return addTransition(transitions, tr);
    }

    /**
     * Encodes a built DateTimeZone to the given stream. Call readFrom to
     * decode the data into a DateTimeZone object.
     *
     * @param out  the output stream to receive the encoded DateTimeZone
     * @since 1.5 (parameter added)
     */
    public void writeTo(String zoneID, OutputStream out) throws IOException {
        if (out instanceof DataOutput) {
            writeTo(zoneID, (DataOutput)out);
        } else {
            writeTo(zoneID, (DataOutput)new DataOutputStream(out));
        }
    }

    /**
     * Encodes a built DateTimeZone to the given stream. Call readFrom to
     * decode the data into a DateTimeZone object.
     *
     * @param out  the output stream to receive the encoded DateTimeZone
     * @since 1.5 (parameter added)
     */
    public void writeTo(String zoneID, DataOutput out) throws IOException {
        // pass false so zone id is not written out
        DateTimeZone zone = toDateTimeZone(zoneID, false);

        if (zone instanceof FixedDateTimeZone) {
            out.writeByte('F'); // 'F' for fixed
            out.writeUTF(zone.getNameKey(0));
            writeMillis(out, zone.getOffset(0));
            writeMillis(out, zone.getStandardOffset(0));
        } else {
            if (zone instanceof CachedDateTimeZone) {
                out.writeByte('C'); // 'C' for cached, precalculated
                zone = ((CachedDateTimeZone)zone).getUncachedZone();
            } else {
                out.writeByte('P'); // 'P' for precalculated, uncached
            }
            ((PrecalculatedZone)zone).writeTo(out);
        }
    }

    /**
     * Supports setting fields of year and moving between transitions.
     */
    private static final class OfYear {
        static OfYear readFrom(DataInput in) throws IOException {
            return new OfYear((char)in.readUnsignedByte(),
                              (int)in.readUnsignedByte(),
                              (int)in.readByte(),
                              (int)in.readUnsignedByte(),
                              in.readBoolean(),
                              (int)readMillis(in));
        }

        // Is 'u', 'w', or 's'.
        final char iMode;

        final int iMonthOfYear;
        final int iDayOfMonth;
        final int iDayOfWeek;
        final boolean iAdvance;
        final int iMillisOfDay;

        OfYear(char mode,
               int monthOfYear,
               int dayOfMonth,
               int dayOfWeek, boolean advanceDayOfWeek,
               int millisOfDay)
        {
            if (mode != 'u' && mode != 'w' && mode != 's') {
                throw new IllegalArgumentException("Unknown mode: " + mode);
            }

            iMode = mode;
            iMonthOfYear = monthOfYear;
            iDayOfMonth = dayOfMonth;
            iDayOfWeek = dayOfWeek;
            iAdvance = advanceDayOfWeek;
            iMillisOfDay = millisOfDay;
        }

        /**
         * @param standardOffset standard offset just before instant
         */
        public long setInstant(int year, int standardOffset, int saveMillis) {
            int offset;
            if (iMode == 'w') {
                offset = standardOffset + saveMillis;
            } else if (iMode == 's') {
                offset = standardOffset;
            } else {
                offset = 0;
            }

            Chronology chrono = ISOChronology.getInstanceUTC();
            long millis = chrono.year().set(0, year);
            millis = chrono.monthOfYear().set(millis, iMonthOfYear);
            millis = chrono.millisOfDay().set(millis, iMillisOfDay);
            millis = setDayOfMonth(chrono, millis);

            if (iDayOfWeek != 0) {
                millis = setDayOfWeek(chrono, millis);
            }

            // Convert from local time to UTC.
            return millis - offset;
        }

        /**
         * @param standardOffset standard offset just before next recurrence
         */
        public long next(long instant, int standardOffset, int saveMillis) {
            int offset;
            if (iMode == 'w') {
                offset = standardOffset + saveMillis;
            } else if (iMode == 's') {
                offset = standardOffset;
            } else {
                offset = 0;
            }

            // Convert from UTC to local time.
            instant += offset;

            Chronology chrono = ISOChronology.getInstanceUTC();
            long next = chrono.monthOfYear().set(instant, iMonthOfYear);
            // Be lenient with millisOfDay.
            next = chrono.millisOfDay().set(next, 0);
            next = chrono.millisOfDay().add(next, iMillisOfDay);
            next = setDayOfMonthNext(chrono, next);

            if (iDayOfWeek == 0) {
                if (next <= instant) {
                    next = chrono.year().add(next, 1);
                    next = setDayOfMonthNext(chrono, next);
                }
            } else {
                next = setDayOfWeek(chrono, next);
                if (next <= instant) {
                    next = chrono.year().add(next, 1);
                    next = chrono.monthOfYear().set(next, iMonthOfYear);
                    next = setDayOfMonthNext(chrono, next);
                    next = setDayOfWeek(chrono, next);
                }
            }

            // Convert from local time to UTC.
            return next - offset;
        }

        /**
         * @param standardOffset standard offset just before previous recurrence
         */
        public long previous(long instant, int standardOffset, int saveMillis) {
            int offset;
            if (iMode == 'w') {
                offset = standardOffset + saveMillis;
            } else if (iMode == 's') {
                offset = standardOffset;
            } else {
                offset = 0;
            }

            // Convert from UTC to local time.
            instant += offset;

            Chronology chrono = ISOChronology.getInstanceUTC();
            long prev = chrono.monthOfYear().set(instant, iMonthOfYear);
            // Be lenient with millisOfDay.
            prev = chrono.millisOfDay().set(prev, 0);
            prev = chrono.millisOfDay().add(prev, iMillisOfDay);
            prev = setDayOfMonthPrevious(chrono, prev);

            if (iDayOfWeek == 0) {
                if (prev >= instant) {
                    prev = chrono.year().add(prev, -1);
                    prev = setDayOfMonthPrevious(chrono, prev);
                }
            } else {
                prev = setDayOfWeek(chrono, prev);
                if (prev >= instant) {
                    prev = chrono.year().add(prev, -1);
                    prev = chrono.monthOfYear().set(prev, iMonthOfYear);
                    prev = setDayOfMonthPrevious(chrono, prev);
                    prev = setDayOfWeek(chrono, prev);
                }
            }

            // Convert from local time to UTC.
            return prev - offset;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof OfYear) {
                OfYear other = (OfYear)obj;
                return
                    iMode == other.iMode &&
                    iMonthOfYear == other.iMonthOfYear &&
                    iDayOfMonth == other.iDayOfMonth &&
                    iDayOfWeek == other.iDayOfWeek &&
                    iAdvance == other.iAdvance &&
                    iMillisOfDay == other.iMillisOfDay;
            }
            return false;
        }

        /*
        public String toString() {
            return
                "[OfYear]\n" + 
                "Mode: " + iMode + '\n' +
                "MonthOfYear: " + iMonthOfYear + '\n' +
                "DayOfMonth: " + iDayOfMonth + '\n' +
                "DayOfWeek: " + iDayOfWeek + '\n' +
                "AdvanceDayOfWeek: " + iAdvance + '\n' +
                "MillisOfDay: " + iMillisOfDay + '\n';
        }
        */

        public void writeTo(DataOutput out) throws IOException {
            out.writeByte(iMode);
            out.writeByte(iMonthOfYear);
            out.writeByte(iDayOfMonth);
            out.writeByte(iDayOfWeek);
            out.writeBoolean(iAdvance);
            writeMillis(out, iMillisOfDay);
        }

        /**
         * If month-day is 02-29 and year isn't leap, advances to next leap year.
         */
        private long setDayOfMonthNext(Chronology chrono, long next) {
            try {
                next = setDayOfMonth(chrono, next);
            } catch (IllegalArgumentException e) {
                if (iMonthOfYear == 2 && iDayOfMonth == 29) {
                    while (chrono.year().isLeap(next) == false) {
                        next = chrono.year().add(next, 1);
                    }
                    next = setDayOfMonth(chrono, next);
                } else {
                    throw e;
                }
            }
            return next;
        }

        /**
         * If month-day is 02-29 and year isn't leap, retreats to previous leap year.
         */
        private long setDayOfMonthPrevious(Chronology chrono, long prev) {
            try {
                prev = setDayOfMonth(chrono, prev);
            } catch (IllegalArgumentException e) {
                if (iMonthOfYear == 2 && iDayOfMonth == 29) {
                    while (chrono.year().isLeap(prev) == false) {
                        prev = chrono.year().add(prev, -1);
                    }
                    prev = setDayOfMonth(chrono, prev);
                } else {
                    throw e;
                }
            }
            return prev;
        }

        private long setDayOfMonth(Chronology chrono, long instant) {
            if (iDayOfMonth >= 0) {
                instant = chrono.dayOfMonth().set(instant, iDayOfMonth);
            } else {
                instant = chrono.dayOfMonth().set(instant, 1);
                instant = chrono.monthOfYear().add(instant, 1);
                instant = chrono.dayOfMonth().add(instant, iDayOfMonth);
            }
            return instant;
        }

        private long setDayOfWeek(Chronology chrono, long instant) {
            int dayOfWeek = chrono.dayOfWeek().get(instant);
            int daysToAdd = iDayOfWeek - dayOfWeek;
            if (daysToAdd != 0) {
                if (iAdvance) {
                    if (daysToAdd < 0) {
                        daysToAdd += 7;
                    }
                } else {
                    if (daysToAdd > 0) {
                        daysToAdd -= 7;
                    }
                }
                instant = chrono.dayOfWeek().add(instant, daysToAdd);
            }
            return instant;
        }
    }

    /**
     * Extends OfYear with a nameKey and savings.
     */
    private static final class Recurrence {
        static Recurrence readFrom(DataInput in) throws IOException {
            return new Recurrence(OfYear.readFrom(in), in.readUTF(), (int)readMillis(in));
        }

        final OfYear iOfYear;
        final String iNameKey;
        final int iSaveMillis;

        Recurrence(OfYear ofYear, String nameKey, int saveMillis) {
            iOfYear = ofYear;
            iNameKey = nameKey;
            iSaveMillis = saveMillis;
        }

        public OfYear getOfYear() {
            return iOfYear;
        }

        /**
         * @param standardOffset standard offset just before next recurrence
         */
        public long next(long instant, int standardOffset, int saveMillis) {
            return iOfYear.next(instant, standardOffset, saveMillis);
        }

        /**
         * @param standardOffset standard offset just before previous recurrence
         */
        public long previous(long instant, int standardOffset, int saveMillis) {
            return iOfYear.previous(instant, standardOffset, saveMillis);
        }

        public String getNameKey() {
            return iNameKey;
        }

        public int getSaveMillis() {
            return iSaveMillis;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof Recurrence) {
                Recurrence other = (Recurrence)obj;
                return
                    iSaveMillis == other.iSaveMillis &&
                    iNameKey.equals(other.iNameKey) &&
                    iOfYear.equals(other.iOfYear);
            }
            return false;
        }

        public void writeTo(DataOutput out) throws IOException {
            iOfYear.writeTo(out);
            out.writeUTF(iNameKey);
            writeMillis(out, iSaveMillis);
        }

        Recurrence rename(String nameKey) {
            return new Recurrence(iOfYear, nameKey, iSaveMillis);
        }

        Recurrence renameAppend(String appendNameKey) {
            return rename((iNameKey + appendNameKey).intern());
        }
    }

    /**
     * Extends Recurrence with inclusive year limits.
     */
    private static final class Rule {
        final Recurrence iRecurrence;
        final int iFromYear; // inclusive
        final int iToYear;   // inclusive

        Rule(Recurrence recurrence, int fromYear, int toYear) {
            iRecurrence = recurrence;
            iFromYear = fromYear;
            iToYear = toYear;
        }

        @SuppressWarnings("unused")
        public int getFromYear() {
            return iFromYear;
        }

        public int getToYear() {
            return iToYear;
        }

        @SuppressWarnings("unused")
        public OfYear getOfYear() {
            return iRecurrence.getOfYear();
        }

        public String getNameKey() {
            return iRecurrence.getNameKey();
        }

        public int getSaveMillis() {
            return iRecurrence.getSaveMillis();
        }

        public long next(final long instant, int standardOffset, int saveMillis) {
            Chronology chrono = ISOChronology.getInstanceUTC();

            final int wallOffset = standardOffset + saveMillis;
            long testInstant = instant;

            int year;
            if (instant == Long.MIN_VALUE) {
                year = Integer.MIN_VALUE;
            } else {
                year = chrono.year().get(instant + wallOffset);
            }

            if (year < iFromYear) {
                // First advance instant to start of from year.
                testInstant = chrono.year().set(0, iFromYear) - wallOffset;
                // Back off one millisecond to account for next recurrence
                // being exactly at the beginning of the year.
                testInstant -= 1;
            }

            long next = iRecurrence.next(testInstant, standardOffset, saveMillis);

            if (next > instant) {
                year = chrono.year().get(next + wallOffset);
                if (year > iToYear) {
                    // Out of range, return original value.
                    next = instant;
                }
            }

            return next;
        }
    }

    private static final class Transition {
        private final long iMillis;
        private final String iNameKey;
        private final int iWallOffset;
        private final int iStandardOffset;

        Transition(long millis, Transition tr) {
            iMillis = millis;
            iNameKey = tr.iNameKey;
            iWallOffset = tr.iWallOffset;
            iStandardOffset = tr.iStandardOffset;
        }

        Transition(long millis, Rule rule, int standardOffset) {
            iMillis = millis;
            iNameKey = rule.getNameKey();
            iWallOffset = standardOffset + rule.getSaveMillis();
            iStandardOffset = standardOffset;
        }

        Transition(long millis, String nameKey,
                   int wallOffset, int standardOffset) {
            iMillis = millis;
            iNameKey = nameKey;
            iWallOffset = wallOffset;
            iStandardOffset = standardOffset;
        }

        public long getMillis() {
            return iMillis;
        }

        public String getNameKey() {
            return iNameKey;
        }

        public int getWallOffset() {
            return iWallOffset;
        }

        public int getStandardOffset() {
            return iStandardOffset;
        }

        public int getSaveMillis() {
            return iWallOffset - iStandardOffset;
        }

        /**
         * There must be a change in the millis, wall offsets or name keys.
         */
        public boolean isTransitionFrom(Transition other) {
            if (other == null) {
                return true;
            }
            return iMillis > other.iMillis &&
                (iWallOffset != other.iWallOffset ||
                 //iStandardOffset != other.iStandardOffset ||
                 !(iNameKey.equals(other.iNameKey)));
        }
    }

    private static final class RuleSet {
        private static final int YEAR_LIMIT;

        static {
            // Don't pre-calculate more than 100 years into the future. Almost
            // all zones will stop pre-calculating far sooner anyhow. Either a
            // simple DST cycle is detected or the last rule is a fixed
            // offset. If a zone has a fixed offset set more than 100 years
            // into the future, then it won't be observed.
            long now = DateTimeUtils.currentTimeMillis();
            YEAR_LIMIT = ISOChronology.getInstanceUTC().year().get(now) + 100;
        }

        private int iStandardOffset;
        private ArrayList<Rule> iRules;

        // Optional.
        private String iInitialNameKey;
        private int iInitialSaveMillis;

        // Upper limit is exclusive.
        private int iUpperYear;
        private OfYear iUpperOfYear;

        RuleSet() {
            iRules = new ArrayList<Rule>(10);
            iUpperYear = Integer.MAX_VALUE;
        }

        /**
         * Copy constructor.
         */
        RuleSet(RuleSet rs) {
            iStandardOffset = rs.iStandardOffset;
            iRules = new ArrayList<Rule>(rs.iRules);
            iInitialNameKey = rs.iInitialNameKey;
            iInitialSaveMillis = rs.iInitialSaveMillis;
            iUpperYear = rs.iUpperYear;
            iUpperOfYear = rs.iUpperOfYear;
        }

        @SuppressWarnings("unused")
        public int getStandardOffset() {
            return iStandardOffset;
        }

        public void setStandardOffset(int standardOffset) {
            iStandardOffset = standardOffset;
        }

        public void setFixedSavings(String nameKey, int saveMillis) {
            iInitialNameKey = nameKey;
            iInitialSaveMillis = saveMillis;
        }

        public void addRule(Rule rule) {
            if (!iRules.contains(rule)) {
                iRules.add(rule);
            }
        }

        public void setUpperLimit(int year, OfYear ofYear) {
            iUpperYear = year;
            iUpperOfYear = ofYear;
        }

        /**
         * Returns a transition at firstMillis with the first name key and
         * offsets for this rule set. This method may return null.
         *
         * @param firstMillis millis of first transition
         */
        public Transition firstTransition(final long firstMillis) {
            if (iInitialNameKey != null) {
                // Initial zone info explicitly set, so don't search the rules.
                return new Transition(firstMillis, iInitialNameKey,
                                      iStandardOffset + iInitialSaveMillis, iStandardOffset);
            }

            // Make a copy before we destroy the rules.
            ArrayList<Rule> copy = new ArrayList<Rule>(iRules);

            // Iterate through all the transitions until firstMillis is
            // reached. Use the name key and savings for whatever rule reaches
            // the limit.

            long millis = Long.MIN_VALUE;
            int saveMillis = 0;
            Transition first = null;

            Transition next;
            while ((next = nextTransition(millis, saveMillis)) != null) {
                millis = next.getMillis();

                if (millis == firstMillis) {
                    first = new Transition(firstMillis, next);
                    break;
                }

                if (millis > firstMillis) {
                    if (first == null) {
                        // Find first rule without savings. This way a more
                        // accurate nameKey is found even though no rule
                        // extends to the RuleSet's lower limit.
                        for (Rule rule : copy) {
                            if (rule.getSaveMillis() == 0) {
                                first = new Transition(firstMillis, rule, iStandardOffset);
                                break;
                            }
                        }
                    }
                    if (first == null) {
                        // Found no rule without savings. Create a transition
                        // with no savings anyhow, and use the best available
                        // name key.
                        first = new Transition(firstMillis, next.getNameKey(),
                                               iStandardOffset, iStandardOffset);
                    }
                    break;
                }
                
                // Set first to the best transition found so far, but next
                // iteration may find something closer to lower limit.
                first = new Transition(firstMillis, next);

                saveMillis = next.getSaveMillis();
            }

            iRules = copy;
            return first;
        }

        /**
         * Returns null if RuleSet is exhausted or upper limit reached. Calling
         * this method will throw away rules as they each become
         * exhausted. Copy the RuleSet before using it to compute transitions.
         *
         * Returned transition may be a duplicate from previous
         * transition. Caller must call isTransitionFrom to filter out
         * duplicates.
         *
         * @param saveMillis savings before next transition
         */
        public Transition nextTransition(final long instant, final int saveMillis) {
            Chronology chrono = ISOChronology.getInstanceUTC();

            // Find next matching rule.
            Rule nextRule = null;
            long nextMillis = Long.MAX_VALUE;
            
            Iterator<Rule> it = iRules.iterator();
            while (it.hasNext()) {
                Rule rule = it.next();
                long next = rule.next(instant, iStandardOffset, saveMillis);
                if (next <= instant) {
                    it.remove();
                    continue;
                }
                // Even if next is same as previous next, choose the rule
                // in order for more recently added rules to override.
                if (next <= nextMillis) {
                    // Found a better match.
                    nextRule = rule;
                    nextMillis = next;
                }
            }
            
            if (nextRule == null) {
                return null;
            }
            
            // Stop precalculating if year reaches some arbitrary limit.
            if (chrono.year().get(nextMillis) >= YEAR_LIMIT) {
                return null;
            }
            
            // Check if upper limit reached or passed.
            if (iUpperYear < Integer.MAX_VALUE) {
                long upperMillis =
                    iUpperOfYear.setInstant(iUpperYear, iStandardOffset, saveMillis);
                if (nextMillis >= upperMillis) {
                    // At or after upper limit.
                    return null;
                }
            }
            
            return new Transition(nextMillis, nextRule, iStandardOffset);
        }

        /**
         * @param saveMillis savings before upper limit
         */
        public long getUpperLimit(int saveMillis) {
            if (iUpperYear == Integer.MAX_VALUE) {
                return Long.MAX_VALUE;
            }
            return iUpperOfYear.setInstant(iUpperYear, iStandardOffset, saveMillis);
        }

        /**
         * Returns null if none can be built.
         */
        public DSTZone buildTailZone(String id) {
            if (iRules.size() == 2) {
                Rule startRule = iRules.get(0);
                Rule endRule = iRules.get(1);
                if (startRule.getToYear() == Integer.MAX_VALUE &&
                    endRule.getToYear() == Integer.MAX_VALUE) {

                    // With exactly two infinitely recurring rules left, a
                    // simple DSTZone can be formed.

                    // The order of rules can come in any order, and it doesn't
                    // really matter which rule was chosen the 'start' and
                    // which is chosen the 'end'. DSTZone works properly either
                    // way.
                    return new DSTZone(id, iStandardOffset,
                                       startRule.iRecurrence, endRule.iRecurrence);
                }
            }
            return null;
        }
    }

    private static final class DSTZone extends DateTimeZone {
        private static final long serialVersionUID = 6941492635554961361L;

        static DSTZone readFrom(DataInput in, String id) throws IOException {
            return new DSTZone(id, (int)readMillis(in), 
                               Recurrence.readFrom(in), Recurrence.readFrom(in));
        }

        final int iStandardOffset;
        final Recurrence iStartRecurrence;
        final Recurrence iEndRecurrence;

        DSTZone(String id, int standardOffset,
                Recurrence startRecurrence, Recurrence endRecurrence) {
            super(id);
            iStandardOffset = standardOffset;
            iStartRecurrence = startRecurrence;
            iEndRecurrence = endRecurrence;
        }

        public String getNameKey(long instant) {
            return findMatchingRecurrence(instant).getNameKey();
        }

        public int getOffset(long instant) {
            return iStandardOffset + findMatchingRecurrence(instant).getSaveMillis();
        }

        public int getStandardOffset(long instant) {
            return iStandardOffset;
        }

        public boolean isFixed() {
            return false;
        }

        public long nextTransition(long instant) {
            int standardOffset = iStandardOffset;
            Recurrence startRecurrence = iStartRecurrence;
            Recurrence endRecurrence = iEndRecurrence;

            long start, end;

            try {
                start = startRecurrence.next
                    (instant, standardOffset, endRecurrence.getSaveMillis());
                if (instant > 0 && start < 0) {
                    // Overflowed.
                    start = instant;
                }
            } catch (IllegalArgumentException e) {
                // Overflowed.
                start = instant;
            } catch (ArithmeticException e) {
                // Overflowed.
                start = instant;
            }

            try {
                end = endRecurrence.next
                    (instant, standardOffset, startRecurrence.getSaveMillis());
                if (instant > 0 && end < 0) {
                    // Overflowed.
                    end = instant;
                }
            } catch (IllegalArgumentException e) {
                // Overflowed.
                end = instant;
            } catch (ArithmeticException e) {
                // Overflowed.
                end = instant;
            }

            return (start > end) ? end : start;
        }

        public long previousTransition(long instant) {
            // Increment in order to handle the case where instant is exactly at
            // a transition.
            instant++;

            int standardOffset = iStandardOffset;
            Recurrence startRecurrence = iStartRecurrence;
            Recurrence endRecurrence = iEndRecurrence;

            long start, end;

            try {
                start = startRecurrence.previous
                    (instant, standardOffset, endRecurrence.getSaveMillis());
                if (instant < 0 && start > 0) {
                    // Overflowed.
                    start = instant;
                }
            } catch (IllegalArgumentException e) {
                // Overflowed.
                start = instant;
            } catch (ArithmeticException e) {
                // Overflowed.
                start = instant;
            }

            try {
                end = endRecurrence.previous
                    (instant, standardOffset, startRecurrence.getSaveMillis());
                if (instant < 0 && end > 0) {
                    // Overflowed.
                    end = instant;
                }
            } catch (IllegalArgumentException e) {
                // Overflowed.
                end = instant;
            } catch (ArithmeticException e) {
                // Overflowed.
                end = instant;
            }

            return ((start > end) ? start : end) - 1;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof DSTZone) {
                DSTZone other = (DSTZone)obj;
                return
                    getID().equals(other.getID()) &&
                    iStandardOffset == other.iStandardOffset &&
                    iStartRecurrence.equals(other.iStartRecurrence) &&
                    iEndRecurrence.equals(other.iEndRecurrence);
            }
            return false;
        }

        public void writeTo(DataOutput out) throws IOException {
            writeMillis(out, iStandardOffset);
            iStartRecurrence.writeTo(out);
            iEndRecurrence.writeTo(out);
        }

        private Recurrence findMatchingRecurrence(long instant) {
            int standardOffset = iStandardOffset;
            Recurrence startRecurrence = iStartRecurrence;
            Recurrence endRecurrence = iEndRecurrence;

            long start, end;

            try {
                start = startRecurrence.next
                    (instant, standardOffset, endRecurrence.getSaveMillis());
            } catch (IllegalArgumentException e) {
                // Overflowed.
                start = instant;
            } catch (ArithmeticException e) {
                // Overflowed.
                start = instant;
            }

            try {
                end = endRecurrence.next
                    (instant, standardOffset, startRecurrence.getSaveMillis());
            } catch (IllegalArgumentException e) {
                // Overflowed.
                end = instant;
            } catch (ArithmeticException e) {
                // Overflowed.
                end = instant;
            }

            return (start > end) ? startRecurrence : endRecurrence;
        }
    }

    private static final class PrecalculatedZone extends DateTimeZone {
        private static final long serialVersionUID = 7811976468055766265L;

        static PrecalculatedZone readFrom(DataInput in, String id) throws IOException {
            // Read string pool.
            int poolSize = in.readUnsignedShort();
            String[] pool = new String[poolSize];
            for (int i=0; i<poolSize; i++) {
                pool[i] = in.readUTF();
            }

            int size = in.readInt();
            long[] transitions = new long[size];
            int[] wallOffsets = new int[size];
            int[] standardOffsets = new int[size];
            String[] nameKeys = new String[size];
            
            for (int i=0; i<size; i++) {
                transitions[i] = readMillis(in);
                wallOffsets[i] = (int)readMillis(in);
                standardOffsets[i] = (int)readMillis(in);
                try {
                    int index;
                    if (poolSize < 256) {
                        index = in.readUnsignedByte();
                    } else {
                        index = in.readUnsignedShort();
                    }
                    nameKeys[i] = pool[index];
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new IOException("Invalid encoding");
                }
            }

            DSTZone tailZone = null;
            if (in.readBoolean()) {
                tailZone = DSTZone.readFrom(in, id);
            }

            return new PrecalculatedZone
                (id, transitions, wallOffsets, standardOffsets, nameKeys, tailZone);
        }

        /**
         * Factory to create instance from builder.
         * 
         * @param id  the zone id
         * @param outputID  true if the zone id should be output
         * @param transitions  the list of Transition objects
         * @param tailZone  optional zone for getting info beyond precalculated tables
         */
        static PrecalculatedZone create(String id, boolean outputID, ArrayList<Transition> transitions,
                                        DSTZone tailZone) {
            int size = transitions.size();
            if (size == 0) {
                throw new IllegalArgumentException();
            }

            long[] trans = new long[size];
            int[] wallOffsets = new int[size];
            int[] standardOffsets = new int[size];
            String[] nameKeys = new String[size];

            Transition last = null;
            for (int i=0; i<size; i++) {
                Transition tr = transitions.get(i);

                if (!tr.isTransitionFrom(last)) {
                    throw new IllegalArgumentException(id);
                }

                trans[i] = tr.getMillis();
                wallOffsets[i] = tr.getWallOffset();
                standardOffsets[i] = tr.getStandardOffset();
                nameKeys[i] = tr.getNameKey();

                last = tr;
            }

            // Some timezones (Australia) have the same name key for
            // summer and winter which messes everything up. Fix it here.
            String[] zoneNameData = new String[5];
            String[][] zoneStrings = new DateFormatSymbols(Locale.ENGLISH).getZoneStrings();
            for (int j = 0; j < zoneStrings.length; j++) {
                String[] set = zoneStrings[j];
                if (set != null && set.length == 5 && id.equals(set[0])) {
                    zoneNameData = set;
                }
            }

            Chronology chrono = ISOChronology.getInstanceUTC();

            for (int i = 0; i < nameKeys.length - 1; i++) {
                String curNameKey = nameKeys[i];
                String nextNameKey = nameKeys[i + 1];
                long curOffset = wallOffsets[i];
                long nextOffset = wallOffsets[i + 1];
                long curStdOffset = standardOffsets[i];
                long nextStdOffset = standardOffsets[i + 1];
                Period p = new Period(trans[i], trans[i + 1], PeriodType.yearMonthDay(), chrono);
                if (curOffset != nextOffset &&
                        curStdOffset == nextStdOffset &&
                        curNameKey.equals(nextNameKey) &&
                        p.getYears() == 0 && p.getMonths() > 4 && p.getMonths() < 8 &&
                        curNameKey.equals(zoneNameData[2]) &&
                        curNameKey.equals(zoneNameData[4])) {
                    
                    if (ZoneInfoLogger.verbose()) {
                        System.out.println("Fixing duplicate name key - " + nextNameKey);
                        System.out.println("     - " + new DateTime(trans[i], chrono) +
                                           " - " + new DateTime(trans[i + 1], chrono));
                    }
                    if (curOffset > nextOffset) {
                        nameKeys[i] = (curNameKey + "-Summer").intern();
                    } else if (curOffset < nextOffset) {
                        nameKeys[i + 1] = (nextNameKey + "-Summer").intern();
                        i++;
                    }
                }
            }

            if (tailZone != null) {
                if (tailZone.iStartRecurrence.getNameKey()
                    .equals(tailZone.iEndRecurrence.getNameKey())) {
                    if (ZoneInfoLogger.verbose()) {
                        System.out.println("Fixing duplicate recurrent name key - " +
                                           tailZone.iStartRecurrence.getNameKey());
                    }
                    if (tailZone.iStartRecurrence.getSaveMillis() > 0) {
                        tailZone = new DSTZone(
                            tailZone.getID(),
                            tailZone.iStandardOffset,
                            tailZone.iStartRecurrence.renameAppend("-Summer"),
                            tailZone.iEndRecurrence);
                    } else {
                        tailZone = new DSTZone(
                            tailZone.getID(),
                            tailZone.iStandardOffset,
                            tailZone.iStartRecurrence,
                            tailZone.iEndRecurrence.renameAppend("-Summer"));
                    }
                }
            }
            
            return new PrecalculatedZone
                ((outputID ? id : ""), trans, wallOffsets, standardOffsets, nameKeys, tailZone);
        }

        // All array fields have the same length.

        private final long[] iTransitions;

        private final int[] iWallOffsets;
        private final int[] iStandardOffsets;
        private final String[] iNameKeys;

        private final DSTZone iTailZone;

        /**
         * Constructor used ONLY for valid input, loaded via static methods.
         */
        private PrecalculatedZone(String id, long[] transitions, int[] wallOffsets,
                          int[] standardOffsets, String[] nameKeys, DSTZone tailZone)
        {
            super(id);
            iTransitions = transitions;
            iWallOffsets = wallOffsets;
            iStandardOffsets = standardOffsets;
            iNameKeys = nameKeys;
            iTailZone = tailZone;
        }

        public String getNameKey(long instant) {
            long[] transitions = iTransitions;
            int i = Arrays.binarySearch(transitions, instant);
            if (i >= 0) {
                return iNameKeys[i];
            }
            i = ~i;
            if (i < transitions.length) {
                if (i > 0) {
                    return iNameKeys[i - 1];
                }
                return "UTC";
            }
            if (iTailZone == null) {
                return iNameKeys[i - 1];
            }
            return iTailZone.getNameKey(instant);
        }

        public int getOffset(long instant) {
            long[] transitions = iTransitions;
            int i = Arrays.binarySearch(transitions, instant);
            if (i >= 0) {
                return iWallOffsets[i];
            }
            i = ~i;
            if (i < transitions.length) {
                if (i > 0) {
                    return iWallOffsets[i - 1];
                }
                return 0;
            }
            if (iTailZone == null) {
                return iWallOffsets[i - 1];
            }
            return iTailZone.getOffset(instant);
        }

        public int getStandardOffset(long instant) {
            long[] transitions = iTransitions;
            int i = Arrays.binarySearch(transitions, instant);
            if (i >= 0) {
                return iStandardOffsets[i];
            }
            i = ~i;
            if (i < transitions.length) {
                if (i > 0) {
                    return iStandardOffsets[i - 1];
                }
                return 0;
            }
            if (iTailZone == null) {
                return iStandardOffsets[i - 1];
            }
            return iTailZone.getStandardOffset(instant);
        }

        public boolean isFixed() {
            return false;
        }

        public long nextTransition(long instant) {
            long[] transitions = iTransitions;
            int i = Arrays.binarySearch(transitions, instant);
            i = (i >= 0) ? (i + 1) : ~i;
            if (i < transitions.length) {
                return transitions[i];
            }
            if (iTailZone == null) {
                return instant;
            }
            long end = transitions[transitions.length - 1];
            if (instant < end) {
                instant = end;
            }
            return iTailZone.nextTransition(instant);
        }

        public long previousTransition(long instant) {
            long[] transitions = iTransitions;
            int i = Arrays.binarySearch(transitions, instant);
            if (i >= 0) {
                if (instant > Long.MIN_VALUE) {
                    return instant - 1;
                }
                return instant;
            }
            i = ~i;
            if (i < transitions.length) {
                if (i > 0) {
                    long prev = transitions[i - 1];
                    if (prev > Long.MIN_VALUE) {
                        return prev - 1;
                    }
                }
                return instant;
            }
            if (iTailZone != null) {
                long prev = iTailZone.previousTransition(instant);
                if (prev < instant) {
                    return prev;
                }
            }
            long prev = transitions[i - 1];
            if (prev > Long.MIN_VALUE) {
                return prev - 1;
            }
            return instant;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof PrecalculatedZone) {
                PrecalculatedZone other = (PrecalculatedZone)obj;
                return
                    getID().equals(other.getID()) &&
                    Arrays.equals(iTransitions, other.iTransitions) &&
                    Arrays.equals(iNameKeys, other.iNameKeys) &&
                    Arrays.equals(iWallOffsets, other.iWallOffsets) &&
                    Arrays.equals(iStandardOffsets, other.iStandardOffsets) &&
                    ((iTailZone == null)
                     ? (null == other.iTailZone)
                     : (iTailZone.equals(other.iTailZone)));
            }
            return false;
        }

        public void writeTo(DataOutput out) throws IOException {
            int size = iTransitions.length;

            // Create unique string pool.
            Set<String> poolSet = new HashSet<String>();
            for (int i=0; i<size; i++) {
                poolSet.add(iNameKeys[i]);
            }

            int poolSize = poolSet.size();
            if (poolSize > 65535) {
                throw new UnsupportedOperationException("String pool is too large");
            }
            String[] pool = new String[poolSize];
            Iterator<String> it = poolSet.iterator();
            for (int i=0; it.hasNext(); i++) {
                pool[i] = it.next();
            }

            // Write out the pool.
            out.writeShort(poolSize);
            for (int i=0; i<poolSize; i++) {
                out.writeUTF(pool[i]);
            }

            out.writeInt(size);

            for (int i=0; i<size; i++) {
                writeMillis(out, iTransitions[i]);
                writeMillis(out, iWallOffsets[i]);
                writeMillis(out, iStandardOffsets[i]);
                
                // Find pool index and write it out.
                String nameKey = iNameKeys[i];
                for (int j=0; j<poolSize; j++) {
                    if (pool[j].equals(nameKey)) {
                        if (poolSize < 256) {
                            out.writeByte(j);
                        } else {
                            out.writeShort(j);
                        }
                        break;
                    }
                }
            }

            out.writeBoolean(iTailZone != null);
            if (iTailZone != null) {
                iTailZone.writeTo(out);
            }
        }

        public boolean isCachable() {
            if (iTailZone != null) {
                return true;
            }
            long[] transitions = iTransitions;
            if (transitions.length <= 1) {
                return false;
            }

            // Add up all the distances between transitions that are less than
            // about two years.
            double distances = 0;
            int count = 0;

            for (int i=1; i<transitions.length; i++) {
                long diff = transitions[i] - transitions[i - 1];
                if (diff < ((366L + 365) * 24 * 60 * 60 * 1000)) {
                    distances += (double)diff;
                    count++;
                }
            }

            if (count > 0) {
                double avg = distances / count;
                avg /= 24 * 60 * 60 * 1000;
                if (avg >= 25) {
                    // Only bother caching if average distance between
                    // transitions is at least 25 days. Why 25?
                    // CachedDateTimeZone is more efficient if the distance
                    // between transitions is large. With an average of 25, it
                    // will on average perform about 2 tests per cache
                    // hit. (49.7 / 25) is approximately 2.
                    return true;
                }
            }

            return false;
        }
    }
}
