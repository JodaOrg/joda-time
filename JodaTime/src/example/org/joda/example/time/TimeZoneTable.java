/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2004 Stephen Colebourne.  
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
package org.joda.example.time;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.joda.time.*;
import org.joda.time.format.*;

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
                zones[i++] = new ZoneData(id, DateTimeZone.getInstance(id));
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
                millis = iZone.nextTransition(millis);
            }
            return cOffsetFormatter.print(millis, iZone);
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
