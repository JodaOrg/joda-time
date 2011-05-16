/*
 *  Copyright 2001-2010 Stephen Colebourne
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
package org.joda.example.time;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

/**
 * Prints out all available time zones to standard out in an HTML table.
 *
 * @author Brian S O'Neill
 */
public class TimeZoneTable {
    static final long cNow = System.currentTimeMillis();

    static final DateTimeFormatter cOffsetFormatter = new DateTimeFormatterBuilder()
        .appendTimeZoneOffset(null, true, 2, 4)
        .toFormatter();

    public static void main(String[] args) throws Exception {
        Set idSet = DateTimeZone.getAvailableIDs();
        ZoneData[] zones = new ZoneData[idSet.size()];
        
        {
            Iterator it = idSet.iterator();
            int i = 0;
            while (it.hasNext()) {
                String id = (String) it.next();
                zones[i++] = new ZoneData(id, DateTimeZone.forID(id));
            }
            Arrays.sort(zones);
        }

        PrintStream out = System.out;

        out.println("<table>");

        out.println("<tr>" +
                    "<th align=\"left\">Standard Offset</th>" +
                    "<th align=\"left\">Canonical ID</th>" +
                    "<th align=\"left\">Aliases</th>" +
                    "</tr>");

        ZoneData canonical = null;
        List aliases = new ArrayList();

        for (int i=0; i<zones.length; i++) {
            ZoneData zone = zones[i];

            if (!zone.isCanonical()) {
                aliases.add(zone);
                continue;
            }

            if (canonical != null) {
                printRow(out, canonical, aliases);
            }

            canonical = zone;
            aliases.clear();
        }

        if (canonical != null) {
            printRow(out, canonical, aliases);
        }

        out.println("</table>");
    }

    private static void printRow(PrintStream out, ZoneData zone, List aliases) {
        out.print("<tr>");
                
        out.print("<td align=\"left\" valign=\"top\">");
        out.print(zone.getStandardOffsetStr());
        out.print("</td>");
        
        out.print("<td align=\"left\" valign=\"top\">");
        out.print(zone.getCanonicalID());
        out.print("</td>");
        
        out.print("<td align=\"left\" valign=\"top\">");
        if (aliases.size() > 0) {
            for (int j=0; j<aliases.size(); j++) {
                if (j > 0) {
                    out.print(", ");
                }
                out.print(((ZoneData) aliases.get(j)).getID());
            }
        }
        out.print("</td>");
        
        out.println("</tr>");
    }

    private static class ZoneData implements Comparable {
        private final String iID;
        private final DateTimeZone iZone;
        
        ZoneData(String id, DateTimeZone zone) {
            iID = id;
            iZone = zone;
        }

        public String getID() {
            return iID;
        }

        public String getCanonicalID() {
            return iZone.getID();
        }

        public boolean isCanonical() {
            return getID().equals(getCanonicalID());
        }

        public String getStandardOffsetStr() {
            long millis = cNow;
            while (iZone.getOffset(millis) != iZone.getStandardOffset(millis)) {
                long next = iZone.nextTransition(millis);
                if (next == millis) {
                    break;
                }
                millis = next;
            }
            return cOffsetFormatter.withZone(iZone).print(millis);
        }

        public int compareTo(Object obj) {
            ZoneData other = (ZoneData) obj;

            int offsetA = iZone.getStandardOffset(cNow);
            int offsetB = other.iZone.getStandardOffset(cNow);

            if (offsetA < offsetB) {
                return -1;
            }
            if (offsetA > offsetB) {
                return 1;
            }

            int result = getCanonicalID().compareTo(other.getCanonicalID());

            if (result != 0) {
                return result;
            }

            if (isCanonical()) {
                if (!other.isCanonical()) {
                    return -1;
                }
            } else if (other.isCanonical()) {
                return 1;
            }

            return getID().compareTo(other.getID());
        }
    }
}
