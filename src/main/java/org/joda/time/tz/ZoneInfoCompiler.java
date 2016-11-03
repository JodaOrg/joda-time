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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.MutableDateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.LenientChronology;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Compiles IANA ZoneInfo database files into binary files for each time zone
 * in the database. {@link DateTimeZoneBuilder} is used to construct and encode
 * compiled data files. {@link ZoneInfoProvider} loads the encoded files and
 * converts them back into {@link DateTimeZone} objects.
 * <p>
 * Although this tool is similar to zic, the binary formats are not
 * compatible. The latest IANA time zone database files may be obtained
 * <a href="http://www.iana.org/time-zones">here</a>.
 * <p>
 * ZoneInfoCompiler is mutable and not thread-safe, although the main method
 * may be safely invoked by multiple threads.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public class ZoneInfoCompiler {
    static DateTimeOfYear cStartOfYear;

    static Chronology cLenientISO;

    //-----------------------------------------------------------------------
    /**
     * Launches the ZoneInfoCompiler tool.
     *
     * <pre>
     * Usage: java org.joda.time.tz.ZoneInfoCompiler &lt;options&gt; &lt;source files&gt;
     * where possible options include:
     *   -src &lt;directory&gt;    Specify where to read source files
     *   -dst &lt;directory&gt;    Specify where to write generated files
     *   -verbose            Output verbosely (default false)
     * </pre>
     */
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            printUsage();
            return;
        }

        File inputDir = null;
        File outputDir = null;
        boolean verbose = false;

        int i;
        for (i=0; i<args.length; i++) {
            try {
                if ("-src".equals(args[i])) {
                    inputDir = new File(args[++i]);
                } else if ("-dst".equals(args[i])) {
                    outputDir = new File(args[++i]);
                } else if ("-verbose".equals(args[i])) {
                    verbose = true;
                } else if ("-?".equals(args[i])) {
                    printUsage();
                    return;
                } else {
                    break;
                }
            } catch (IndexOutOfBoundsException e) {
                printUsage();
                return;
            }
        }

        if (i >= args.length) {
            printUsage();
            return;
        }

        File[] sources = new File[args.length - i];
        for (int j=0; i<args.length; i++,j++) {
            sources[j] = inputDir == null ? new File(args[i]) : new File(inputDir, args[i]);
        }

        ZoneInfoLogger.set(verbose);
        ZoneInfoCompiler zic = new ZoneInfoCompiler();
        zic.compile(outputDir, sources);
    }

    private static void printUsage() {
        System.out.println("Usage: java org.joda.time.tz.ZoneInfoCompiler <options> <source files>");
        System.out.println("where possible options include:");
        System.out.println("  -src <directory>    Specify where to read source files");
        System.out.println("  -dst <directory>    Specify where to write generated files");
        System.out.println("  -verbose            Output verbosely (default false)");
    }

    static DateTimeOfYear getStartOfYear() {
        if (cStartOfYear == null) {
            cStartOfYear = new DateTimeOfYear();
        }
        return cStartOfYear;
    }

    static Chronology getLenientISOChronology() {
        if (cLenientISO == null) {
            cLenientISO = LenientChronology.getInstance(ISOChronology.getInstanceUTC());
        }
        return cLenientISO;
    }

    /**
     * @param zimap maps string ids to DateTimeZone objects.
     */
    static void writeZoneInfoMap(DataOutputStream dout, Map<String, DateTimeZone> zimap) throws IOException {
        // Build the string pool.
        Map<String, Short> idToIndex = new HashMap<String, Short>(zimap.size());
        TreeMap<Short, String> indexToId = new TreeMap<Short, String>();

        short count = 0;
        for (Entry<String, DateTimeZone> entry : zimap.entrySet()) {
            String id = (String)entry.getKey();
            if (!idToIndex.containsKey(id)) {
                Short index = Short.valueOf(count);
                idToIndex.put(id, index);
                indexToId.put(index, id);
                if (++count == 0) {
                    throw new InternalError("Too many time zone ids");
                }
            }
            id = ((DateTimeZone)entry.getValue()).getID();
            if (!idToIndex.containsKey(id)) {
                Short index = Short.valueOf(count);
                idToIndex.put(id, index);
                indexToId.put(index, id);
                if (++count == 0) {
                    throw new InternalError("Too many time zone ids");
                }
            }
        }

        // Write the string pool, ordered by index.
        dout.writeShort(indexToId.size());
        for (String id : indexToId.values()) {
            dout.writeUTF(id);
        }

        // Write the mappings.
        dout.writeShort(zimap.size());
        for (Entry<String, DateTimeZone> entry : zimap.entrySet()) {
            String id = entry.getKey();
            dout.writeShort(idToIndex.get(id).shortValue());
            id = entry.getValue().getID();
            dout.writeShort(idToIndex.get(id).shortValue());
        }
    }

    static int parseYear(String str, int def) {
        str = str.toLowerCase(Locale.ENGLISH);
        if (str.equals("minimum") || str.equals("min")) {
            return Integer.MIN_VALUE;
        } else if (str.equals("maximum") || str.equals("max")) {
            return Integer.MAX_VALUE;
        } else if (str.equals("only")) {
            return def;
        }
        return Integer.parseInt(str);
    }

    static int parseMonth(String str) {
        DateTimeField field = ISOChronology.getInstanceUTC().monthOfYear();
        return field.get(field.set(0, str, Locale.ENGLISH));
    }

    static int parseDayOfWeek(String str) {
        DateTimeField field = ISOChronology.getInstanceUTC().dayOfWeek();
        return field.get(field.set(0, str, Locale.ENGLISH));
    }
    
    static String parseOptional(String str) {
        return (str.equals("-")) ? null : str;
    }

    static int parseTime(String str) {
        DateTimeFormatter p = ISODateTimeFormat.hourMinuteSecondFraction();
        MutableDateTime mdt = new MutableDateTime(0, getLenientISOChronology());
        int pos = 0;
        if (str.startsWith("-")) {
            pos = 1;
        }
        int newPos = p.parseInto(mdt, str, pos);
        if (newPos == ~pos) {
            throw new IllegalArgumentException(str);
        }
        int millis = (int)mdt.getMillis();
        if (pos == 1) {
            millis = -millis;
        }
        return millis;
    }

    static char parseZoneChar(char c) {
        switch (c) {
        case 's': case 'S':
            // Standard time
            return 's';
        case 'u': case 'U': case 'g': case 'G': case 'z': case 'Z':
            // UTC
            return 'u';
        case 'w': case 'W': default:
            // Wall time
            return 'w';
        }
    }

    /**
     * @return false if error.
     */
    static boolean test(String id, DateTimeZone tz) {
        if (!id.equals(tz.getID())) {
            return true;
        }

        // Test to ensure that reported transitions are not duplicated.

        long millis = ISOChronology.getInstanceUTC().year().set(0, 1850);
        long end = ISOChronology.getInstanceUTC().year().set(0, 2050);

        int offset = tz.getOffset(millis);
        int stdOffset = tz.getStandardOffset(millis);
        String key = tz.getNameKey(millis);

        List<Long> transitions = new ArrayList<Long>();

        while (true) {
            long next = tz.nextTransition(millis);
            if (next == millis || next > end) {
                break;
            }

            millis = next;

            int nextOffset = tz.getOffset(millis);
            int nextStdOffset = tz.getStandardOffset(millis);
            String nextKey = tz.getNameKey(millis);

            if (offset == nextOffset && stdOffset == nextStdOffset && key.equals(nextKey)) {
                System.out.println("*d* Error in " + tz.getID() + " "
                                   + new DateTime(millis,
                                                  ISOChronology.getInstanceUTC()));
                return false;
            }

            if (nextKey == null || (nextKey.length() < 3 && !"??".equals(nextKey))) {
                System.out.println("*s* Error in " + tz.getID() + " "
                                   + new DateTime(millis,
                                                  ISOChronology.getInstanceUTC())
                                   + ", nameKey=" + nextKey);
                return false;
            }

            transitions.add(Long.valueOf(millis));

            offset = nextOffset;
            key = nextKey;
        }

        // Now verify that reverse transitions match up.

        millis = ISOChronology.getInstanceUTC().year().set(0, 2050);
        end = ISOChronology.getInstanceUTC().year().set(0, 1850);

        for (int i=transitions.size(); --i>= 0; ) {
            long prev = tz.previousTransition(millis);
            if (prev == millis || prev < end) {
                break;
            }

            millis = prev;

            long trans = transitions.get(i).longValue();
            
            if (trans - 1 != millis) {
                System.out.println("*r* Error in " + tz.getID() + " "
                                   + new DateTime(millis,
                                                  ISOChronology.getInstanceUTC()) + " != "
                                   + new DateTime(trans - 1,
                                                  ISOChronology.getInstanceUTC()));
                                   
                return false;
            }
        }

        return true;
    }

    // Maps names to RuleSets.
    private Map<String, RuleSet> iRuleSets;

    // List of Zone objects.
    private List<Zone> iZones;

    // List String pairs to link.
    private List<String> iGoodLinks;

    // List String pairs to link.
    private List<String> iBackLinks;

    public ZoneInfoCompiler() {
        iRuleSets = new HashMap<String, RuleSet>();
        iZones = new ArrayList<Zone>();
        iGoodLinks = new ArrayList<String>();
        iBackLinks = new ArrayList<String>();
    }

    /**
     * Returns a map of ids to DateTimeZones.
     *
     * @param outputDir optional directory to write compiled data files to
     * @param sources optional list of source files to parse
     */
    public Map<String, DateTimeZone> compile(File outputDir, File[] sources) throws IOException {
        if (sources != null) {
            for (int i=0; i<sources.length; i++) {
                BufferedReader in = null;
                try {
                    in = new BufferedReader(new FileReader(sources[i]));
                    parseDataFile(in, "backward".equals(sources[i].getName()));
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            }
        }

        if (outputDir != null) {
            if (!outputDir.exists() && !outputDir.mkdirs()) {
                throw new IOException("Destination directory doesn't exist and cannot be created: " + outputDir);
            }
            if (!outputDir.isDirectory()) {
                throw new IOException("Destination is not a directory: " + outputDir);
            }
        }

        Map<String, DateTimeZone> map = new TreeMap<String, DateTimeZone>();
        Map<String, Zone> sourceMap = new TreeMap<String, Zone>();

        System.out.println("Writing zoneinfo files");
        // write out the standard entries
        for (int i = 0; i < iZones.size(); i++) {
            Zone zone = iZones.get(i);
            DateTimeZoneBuilder builder = new DateTimeZoneBuilder();
            zone.addToBuilder(builder, iRuleSets);
            DateTimeZone tz = builder.toDateTimeZone(zone.iName, true);
            if (test(tz.getID(), tz)) {
                map.put(tz.getID(), tz);
                sourceMap.put(tz.getID(), zone);
                if (outputDir != null) {
                    writeZone(outputDir, builder, tz);
                }
            }
        }

        // revive zones from "good" links
        for (int i = 0; i < iGoodLinks.size(); i += 2) {
            String baseId = iGoodLinks.get(i);
            String alias = iGoodLinks.get(i + 1);
            Zone sourceZone = sourceMap.get(baseId);
            if (sourceZone == null) {
                System.out.println("Cannot find source zone '" + baseId + "' to link alias '" + alias + "' to");
            } else {
                DateTimeZoneBuilder builder = new DateTimeZoneBuilder();
                sourceZone.addToBuilder(builder, iRuleSets);
                DateTimeZone revived = builder.toDateTimeZone(alias, true);
                if (test(revived.getID(), revived)) {
                    map.put(revived.getID(), revived);
                    if (outputDir != null) {
                        writeZone(outputDir, builder, revived);
                    }
                }
                map.put(revived.getID(), revived);
                if (ZoneInfoLogger.verbose()) {
                    System.out.println("Good link: " + alias + " -> " + baseId + " revived");
                }
            }
        }

        // store "back" links as aliases (where name is permanently mapped
        for (int pass = 0; pass < 2; pass++) {
            for (int i = 0; i < iBackLinks.size(); i += 2) {
                String id = iBackLinks.get(i);
                String alias = iBackLinks.get(i + 1);
                DateTimeZone tz = map.get(id);
                if (tz == null) {
                    if (pass > 0) {
                        System.out.println("Cannot find time zone '" + id + "' to link alias '" + alias + "' to");
                    }
                } else {
                    map.put(alias, tz);
                    if (ZoneInfoLogger.verbose()) {
                        System.out.println("Back link: " + alias + " -> " + tz.getID());
                    }
                }
            }
        }

        // write map that unites the time-zone data, pointing aliases and real zones at files
        if (outputDir != null) {
            System.out.println("Writing ZoneInfoMap");
            File file = new File(outputDir, "ZoneInfoMap");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            OutputStream out = new FileOutputStream(file);
            DataOutputStream dout = new DataOutputStream(out);
            try {
                // Sort and filter out any duplicates that match case.
                Map<String, DateTimeZone> zimap = new TreeMap<String, DateTimeZone>(String.CASE_INSENSITIVE_ORDER);
                zimap.putAll(map);
                writeZoneInfoMap(dout, zimap);
            } finally {
                dout.close();
            }
        }

        return map;
    }

    private void writeZone(File outputDir, DateTimeZoneBuilder builder, DateTimeZone tz) throws IOException {
        if (ZoneInfoLogger.verbose()) {
            System.out.println("Writing " + tz.getID());
        }
        File file = new File(outputDir, tz.getID());
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        OutputStream out = new FileOutputStream(file);
        try {
            builder.writeTo(tz.getID(), out);
        } finally {
            out.close();
        }

        // Test if it can be read back.
        InputStream in = new FileInputStream(file);
        DateTimeZone tz2 = DateTimeZoneBuilder.readFrom(in, tz.getID());
        in.close();

        if (!tz.equals(tz2)) {
            System.out.println("*e* Error in " + tz.getID() +
                               ": Didn't read properly from file");
        }
    }

    public void parseDataFile(BufferedReader in, boolean backward) throws IOException {
        Zone zone = null;
        String line;
        while ((line = in.readLine()) != null) {
            String trimmed = line.trim();
            if (trimmed.length() == 0 || trimmed.charAt(0) == '#') {
                continue;
            }

            int index = line.indexOf('#');
            if (index >= 0) {
                line = line.substring(0, index);
            }

            //System.out.println(line);

            StringTokenizer st = new StringTokenizer(line, " \t");

            if (Character.isWhitespace(line.charAt(0)) && st.hasMoreTokens()) {
                if (zone != null) {
                    // Zone continuation
                    zone.chain(st);
                }
                continue;
            } else {
                if (zone != null) {
                    iZones.add(zone);
                }
                zone = null;
            }

            if (st.hasMoreTokens()) {
                String token = st.nextToken();
                if (token.equalsIgnoreCase("Rule")) {
                    Rule r = new Rule(st);
                    RuleSet rs = iRuleSets.get(r.iName);
                    if (rs == null) {
                        rs = new RuleSet(r);
                        iRuleSets.put(r.iName, rs);
                    } else {
                        rs.addRule(r);
                    }
                } else if (token.equalsIgnoreCase("Zone")) {
                    if (st.countTokens() < 4) {
                        throw new IllegalArgumentException("Attempting to create a Zone from an incomplete tokenizer");
                    }
                    zone = new Zone(st);
                } else if (token.equalsIgnoreCase("Link")) {
                    String real = st.nextToken();
                    String alias = st.nextToken();
                    // links in "backward" are deprecated names
                    // links in other files should be kept
                    // special case a few to try to repair terrible damage to tzdb
                    if (backward || alias.equals("US/Pacific-New") || alias.startsWith("Etc/") || alias.equals("GMT")) {
                        iBackLinks.add(real);
                        iBackLinks.add(alias);
                    } else {
                        iGoodLinks.add(real);
                        iGoodLinks.add(alias);
                    }
                } else {
                    System.out.println("Unknown line: " + line);
                }
            }
        }

        if (zone != null) {
            iZones.add(zone);
        }
    }

    static class DateTimeOfYear {
        public final int iMonthOfYear;
        public final int iDayOfMonth;
        public final int iDayOfWeek;
        public final boolean iAdvanceDayOfWeek;
        public final int iMillisOfDay;
        public final char iZoneChar;

        DateTimeOfYear() {
            iMonthOfYear = 1;
            iDayOfMonth = 1;
            iDayOfWeek = 0;
            iAdvanceDayOfWeek = false;
            iMillisOfDay = 0;
            iZoneChar = 'w';
        }

        DateTimeOfYear(StringTokenizer st) {
            int month = 1;
            int day = 1;
            int dayOfWeek = 0;
            int millis = 0;
            boolean advance = false;
            char zoneChar = 'w';

            if (st.hasMoreTokens()) {
                month = parseMonth(st.nextToken());

                if (st.hasMoreTokens()) {
                    String str = st.nextToken();
                    if (str.startsWith("last")) {
                        day = -1;
                        dayOfWeek = parseDayOfWeek(str.substring(4));
                        advance = false;
                    } else {
                        try {
                            day = Integer.parseInt(str);
                            dayOfWeek = 0;
                            advance = false;
                        } catch (NumberFormatException e) {
                            int index = str.indexOf(">=");
                            if (index > 0) {
                                day = Integer.parseInt(str.substring(index + 2));
                                dayOfWeek = parseDayOfWeek(str.substring(0, index));
                                advance = true;
                            } else {
                                index = str.indexOf("<=");
                                if (index > 0) {
                                    day = Integer.parseInt(str.substring(index + 2));
                                    dayOfWeek = parseDayOfWeek(str.substring(0, index));
                                    advance = false;
                                } else {
                                    throw new IllegalArgumentException(str);
                                }
                            }
                        }
                    }

                    if (st.hasMoreTokens()) {
                        str = st.nextToken();
                        zoneChar = parseZoneChar(str.charAt(str.length() - 1));
                        if (str.equals("24:00")) {
                            // handle end of year
                            if (month == 12 && day == 31) {
                                millis = parseTime("23:59:59.999");
                            } else {
                                LocalDate date = (day == -1 ?
                                        new LocalDate(2001, month, 1).plusMonths(1) :
                                        new LocalDate(2001, month, day).plusDays(1));
                                advance = (day != -1 && dayOfWeek != 0);
                                month = date.getMonthOfYear();
                                day = date.getDayOfMonth();
                                if (dayOfWeek != 0) {
                                    dayOfWeek = ((dayOfWeek - 1 + 1) % 7) + 1;
                                }
                            }
                        } else {
                            millis = parseTime(str);
                        }
                    }
                }
            }

            iMonthOfYear = month;
            iDayOfMonth = day;
            iDayOfWeek = dayOfWeek;
            iAdvanceDayOfWeek = advance;
            iMillisOfDay = millis;
            iZoneChar = zoneChar;
        }

        /**
         * Adds a recurring savings rule to the builder.
         */
        public void addRecurring(DateTimeZoneBuilder builder, String nameKey,
                                 int saveMillis, int fromYear, int toYear)
        {
            builder.addRecurringSavings(nameKey, saveMillis,
                                        fromYear, toYear,
                                        iZoneChar,
                                        iMonthOfYear,
                                        iDayOfMonth,
                                        iDayOfWeek,
                                        iAdvanceDayOfWeek,
                                        iMillisOfDay);
        }

        /**
         * Adds a cutover to the builder.
         */
        public void addCutover(DateTimeZoneBuilder builder, int year) {
            builder.addCutover(year,
                               iZoneChar,
                               iMonthOfYear,
                               iDayOfMonth,
                               iDayOfWeek,
                               iAdvanceDayOfWeek,
                               iMillisOfDay);
        }

        public String toString() {
            return
                "MonthOfYear: " + iMonthOfYear + "\n" +
                "DayOfMonth: " + iDayOfMonth + "\n" +
                "DayOfWeek: " + iDayOfWeek + "\n" +
                "AdvanceDayOfWeek: " + iAdvanceDayOfWeek + "\n" +
                "MillisOfDay: " + iMillisOfDay + "\n" +
                "ZoneChar: " + iZoneChar + "\n";
        }
    }

    private static class Rule {
        public final String iName;
        public final int iFromYear;
        public final int iToYear;
        public final String iType;
        public final DateTimeOfYear iDateTimeOfYear;
        public final int iSaveMillis;
        public final String iLetterS;

        Rule(StringTokenizer st) {
            if (st.countTokens() < 6) {
                throw new IllegalArgumentException("Attempting to create a Rule from an incomplete tokenizer");
            }
            iName = st.nextToken().intern();
            iFromYear = parseYear(st.nextToken(), 0);
            iToYear = parseYear(st.nextToken(), iFromYear);
            if (iToYear < iFromYear) {
                throw new IllegalArgumentException();
            }
            iType = parseOptional(st.nextToken());
            iDateTimeOfYear = new DateTimeOfYear(st);
            iSaveMillis = parseTime(st.nextToken());
            iLetterS = parseOptional(st.nextToken());
        }

        /**
         * Adds a recurring savings rule to the builder.
         */
        public void addRecurring(DateTimeZoneBuilder builder, String nameFormat) {
            String nameKey = formatName(nameFormat);
            iDateTimeOfYear.addRecurring
                (builder, nameKey, iSaveMillis, iFromYear, iToYear);
        }

        private String formatName(String nameFormat) {
            int index = nameFormat.indexOf('/');
            if (index > 0) {
                if (iSaveMillis == 0) {
                    // Extract standard name.
                    return nameFormat.substring(0, index).intern();
                } else {
                    return nameFormat.substring(index + 1).intern();
                }
            }
            index = nameFormat.indexOf("%s");
            if (index < 0) {
                return nameFormat;
            }
            String left = nameFormat.substring(0, index);
            String right = nameFormat.substring(index + 2);
            String name;
            if (iLetterS == null) {
                name = left.concat(right);
            } else {
                name = left + iLetterS + right;
            }
            return name.intern();
        }

        public String toString() {
            return
                "[Rule]\n" + 
                "Name: " + iName + "\n" +
                "FromYear: " + iFromYear + "\n" +
                "ToYear: " + iToYear + "\n" +
                "Type: " + iType + "\n" +
                iDateTimeOfYear +
                "SaveMillis: " + iSaveMillis + "\n" +
                "LetterS: " + iLetterS + "\n";
        }
    }

    private static class RuleSet {
        private List<Rule> iRules;

        RuleSet(Rule rule) {
            iRules = new ArrayList<Rule>();
            iRules.add(rule);
        }

        void addRule(Rule rule) {
            if (!(rule.iName.equals(iRules.get(0).iName))) {
                throw new IllegalArgumentException("Rule name mismatch");
            }
            iRules.add(rule);
        }

        /**
         * Adds recurring savings rules to the builder.
         */
        public void addRecurring(DateTimeZoneBuilder builder, String nameFormat) {
            for (int i=0; i<iRules.size(); i++) {
                Rule rule = iRules.get(i);
                rule.addRecurring(builder, nameFormat);
            }
        }
    }

    private static class Zone {
        public final String iName;
        public final int iOffsetMillis;
        public final String iRules;
        public final String iFormat;
        public final int iUntilYear;
        public final DateTimeOfYear iUntilDateTimeOfYear;

        private Zone iNext;

        Zone(StringTokenizer st) {
            this(st.nextToken(), st);
        }

        private Zone(String name, StringTokenizer st) {
            iName = name.intern();
            iOffsetMillis = parseTime(st.nextToken());
            iRules = parseOptional(st.nextToken());
            iFormat = st.nextToken().intern();

            int year = Integer.MAX_VALUE;
            DateTimeOfYear dtOfYear = getStartOfYear();

            if (st.hasMoreTokens()) {
                year = Integer.parseInt(st.nextToken());
                if (st.hasMoreTokens()) {
                    dtOfYear = new DateTimeOfYear(st);
                }
            }

            iUntilYear = year;
            iUntilDateTimeOfYear = dtOfYear;
        }

        void chain(StringTokenizer st) {
            if (iNext != null) {
                iNext.chain(st);
            } else {
                iNext = new Zone(iName, st);
            }
        }

        /*
        public DateTimeZone buildDateTimeZone(Map ruleSets) {
            DateTimeZoneBuilder builder = new DateTimeZoneBuilder();
            addToBuilder(builder, ruleSets);
            return builder.toDateTimeZone(iName);
        }
        */

        /**
         * Adds zone info to the builder.
         */
        public void addToBuilder(DateTimeZoneBuilder builder, Map<String, RuleSet> ruleSets) {
            addToBuilder(this, builder, ruleSets);
        }

        private static void addToBuilder(Zone zone,
                                         DateTimeZoneBuilder builder,
                                         Map<String, RuleSet> ruleSets)
        {
            for (; zone != null; zone = zone.iNext) {
                builder.setStandardOffset(zone.iOffsetMillis);

                if (zone.iRules == null) {
                    builder.setFixedSavings(zone.iFormat, 0);
                } else {
                    try {
                        // Check if iRules actually just refers to a savings.
                        int saveMillis = parseTime(zone.iRules);
                        builder.setFixedSavings(zone.iFormat, saveMillis);
                    }
                    catch (Exception e) {
                        RuleSet rs = ruleSets.get(zone.iRules);
                        if (rs == null) {
                            throw new IllegalArgumentException
                                ("Rules not found: " + zone.iRules);
                        }
                        rs.addRecurring(builder, zone.iFormat);
                    }
                }

                if (zone.iUntilYear == Integer.MAX_VALUE) {
                    break;
                }

                zone.iUntilDateTimeOfYear.addCutover(builder, zone.iUntilYear);
            }
        }

        public String toString() {
            String str =
                "[Zone]\n" + 
                "Name: " + iName + "\n" +
                "OffsetMillis: " + iOffsetMillis + "\n" +
                "Rules: " + iRules + "\n" +
                "Format: " + iFormat + "\n" +
                "UntilYear: " + iUntilYear + "\n" +
                iUntilDateTimeOfYear;

            if (iNext == null) {
                return str;
            }

            return str + "...\n" + iNext.toString();
        }
    }
}

