/*
 *  Copyright 2006 Stephen Colebourne
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
package org.joda.time.contrib.i18n;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Generates a file that stores the territory data.
 */
public class CLDRTerritoryGenerator {

    Map firstDOWMap = new HashMap();
    Map weekendStartMap = new HashMap();
    Map weekendEndMap = new HashMap();
    Map zoneMap = new HashMap();

    //-----------------------------------------------------------------------
    /**
     * Output the CLDR data to a file.
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            CLDRTerritoryGenerator gen = new CLDRTerritoryGenerator();
            gen.readXML();
            gen.writeFile();
            
        } catch (RuntimeException ex) {
            System.err.println();
            ex.printStackTrace();
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Writes the data to a file.
     */
    private void writeFile() {
        File file = new File("src/java/org/joda/time/contrib/i18n/CLDRTerritoryData.dat");
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(
                    new BufferedOutputStream(new FileOutputStream(file)));
            oos.writeByte(1);  // version
            writeData(oos);
            oos.writeUTF("");  // end of file id
            oos.close();
            
        } catch (IOException ex) {
            ex.printStackTrace();
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException ex2) {
                    // ignore
                }
            }
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Writes the data to the stream.
     */
    private void writeData(ObjectOutputStream oos) throws IOException {
        Set territories = zoneMap.keySet();
        for (Iterator it = territories.iterator(); it.hasNext(); ) {
            String territory = (String) it.next();
            if ("001".equals(territory)) {
                continue;
            }
            List zones = (List) zoneMap.get(territory);
            if (zones.size() > 1) {
                pickPrimaryZone(territory, zones);
            }
            Integer firstDay = (Integer) firstDOWMap.get(territory);
            if (firstDay == null) {
                firstDay = (Integer) firstDOWMap.get("001");
            }
            Integer weekendStart = (Integer) weekendStartMap.get(territory);
            if (weekendStart == null) {
                weekendStart = (Integer) weekendStartMap.get("001");
            }
            Integer weekendEnd = (Integer) weekendEndMap.get(territory);
            if (weekendEnd == null) {
                weekendEnd = (Integer) weekendEndMap.get("001");
            }
            
            int weStart = weekendStart.intValue();
            int weEnd = weekendEnd.intValue();
            int busStart = (weEnd == 7 ? 1 : weEnd + 1);
            int busEnd = (weStart == 1 ? 7 : weStart - 1);
            
            oos.writeUTF(territory);
            oos.writeByte(zones.size());
            for (int i = 0; i < zones.size(); i++) {
                oos.writeUTF((String) zones.get(i));
            }
            oos.writeByte(firstDay.intValue());
            oos.writeByte(busStart);
            oos.writeByte(busEnd);
            oos.writeByte(weStart);
            oos.writeByte(weEnd);
            System.out.print(territory + " ");
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Reads the CLDR XML data.
     */
    private void readXML() {
        File file = new File("src/gen/org/joda/time/contrib/i18n/supplementalData.xml");
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            Element rootEl = (Element) document.getElementsByTagName("supplementalData").item(0);
            
            parseFirstDay(rootEl);
            parseWeekendStart(rootEl);
            parseWeekendEnd(rootEl);
            parseZoneFormatting(rootEl);
            
        } catch (SAXParseException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Parses the firstDay elements.
     *
     * @param rootEl  the root element
     */
    private void parseFirstDay(Element rootEl) {
        Element weekDataEl = (Element) rootEl.getElementsByTagName("weekData").item(0);
        NodeList els = weekDataEl.getElementsByTagName("firstDay");
        for (int i = 0; i < els.getLength(); i++) {
            Element el = (Element) els.item(i);
            if ("true".equals(el.getAttribute("draft"))) {
                continue;
            }
            String dayStr = el.getAttribute("day");
            Integer dow = convertDOW(dayStr);
            String territoriesStr = el.getAttribute("territories");
            StringTokenizer tkn = new StringTokenizer(territoriesStr);
            while (tkn.hasMoreTokens()) {
                String territory = tkn.nextToken();
                firstDOWMap.put(territory, dow);
            }
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Parses the weekendStart elements.
     *
     * @param rootEl  the root element
     */
    private void parseWeekendStart(Element rootEl) {
        Element weekDataEl = (Element) rootEl.getElementsByTagName("weekData").item(0);
        NodeList els = weekDataEl.getElementsByTagName("weekendStart");
        for (int i = 0; i < els.getLength(); i++) {
            Element el = (Element) els.item(i);
            if ("true".equals(el.getAttribute("draft"))) {
                continue;
            }
            String dayStr = el.getAttribute("day");
            Integer dow = convertDOW(dayStr);
            String territoriesStr = el.getAttribute("territories");
            StringTokenizer tkn = new StringTokenizer(territoriesStr);
            while (tkn.hasMoreTokens()) {
                String territory = tkn.nextToken();
                weekendStartMap.put(territory, dow);
            }
        }
        // fix CLDR
        weekendStartMap.put("AF", new Integer(4));
        weekendStartMap.put("IR", new Integer(4));
    }

    //-----------------------------------------------------------------------
    /**
     * Parses the weekendEnd elements.
     *
     * @param rootEl  the root element
     */
    private void parseWeekendEnd(Element rootEl) {
        Element weekDataEl = (Element) rootEl.getElementsByTagName("weekData").item(0);
        NodeList els = weekDataEl.getElementsByTagName("weekendEnd");
        for (int i = 0; i < els.getLength(); i++) {
            Element el = (Element) els.item(i);
            if ("true".equals(el.getAttribute("draft"))) {
                continue;
            }
            String dayStr = el.getAttribute("day");
            Integer dow = convertDOW(dayStr);
            String territoriesStr = el.getAttribute("territories");
            StringTokenizer tkn = new StringTokenizer(territoriesStr);
            while (tkn.hasMoreTokens()) {
                String territory = tkn.nextToken();
                weekendEndMap.put(territory, dow);
            }
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Parses the zoneFormatting element.
     *
     * @param rootEl  the root element
     */
    private void parseZoneFormatting(Element rootEl) {
        Element weekDataEl = (Element) rootEl.getElementsByTagName("zoneFormatting").item(0);
        NodeList els = weekDataEl.getElementsByTagName("zoneItem");
        for (int i = 0; i < els.getLength(); i++) {
            Element el = (Element) els.item(i);
            if ("true".equals(el.getAttribute("draft"))) {
                continue;
            }
            String zoneStr = el.getAttribute("type");
            String territory = el.getAttribute("territory");
            List list = (List) zoneMap.get(territory);
            if (list == null) {
                list = new ArrayList();
                zoneMap.put(territory, list);
            }
            list.add(zoneStr);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Converts a day string to a day of week value.
     *
     * @param dayStr  the day string, such as 'sat'
     * @return the dow
     */
    private Integer convertDOW(String dayStr) {
        Integer dowValue = (Integer) CLDR_DAYS.get(dayStr);
        if (dowValue == null) {
            throw new IllegalStateException("Unknown day of week: " + dayStr);
        }
        return dowValue;
    }

    /** The CLDR day of week values. */
    private static final Map CLDR_DAYS = new HashMap();
    static {
        CLDR_DAYS.put("mon", new Integer(1));
        CLDR_DAYS.put("tue", new Integer(2));
        CLDR_DAYS.put("wed", new Integer(3));
        CLDR_DAYS.put("thu", new Integer(4));
        CLDR_DAYS.put("fri", new Integer(5));
        CLDR_DAYS.put("sat", new Integer(6));
        CLDR_DAYS.put("sun", new Integer(7));
    }

    //-----------------------------------------------------------------------
    /**
     * Picks the primary zone
     * @param zones  the list of zones
     */
    private void pickPrimaryZone(String territory, List zones) {
        String primary = (String) PRIMARY_ZONES.get(territory);
        if (primary == null) {
            throw new IllegalStateException("Unknown primary zone for territory: " + territory + ": " + zones);
        }
        if (zones.contains(primary) == false) {
            throw new IllegalStateException("Invalid primary zone for territory: " + territory + ": " + primary);
        }
        zones.remove(primary);
        zones.add(0, primary);
    }

    /** The primary zone selections. */
    private static final Map PRIMARY_ZONES = new HashMap();
    static {
        PRIMARY_ZONES.put("US", "America/New_York");
        PRIMARY_ZONES.put("MN", "Asia/Ulaanbaatar");
        PRIMARY_ZONES.put("CD", "Africa/Kinshasa");
        PRIMARY_ZONES.put("EC", "America/Guayaquil");
        PRIMARY_ZONES.put("KZ", "Asia/Almaty");
        PRIMARY_ZONES.put("GL", "America/Godthab");
        PRIMARY_ZONES.put("CN", "Asia/Shanghai");
        PRIMARY_ZONES.put("UA", "Europe/Kiev");
        PRIMARY_ZONES.put("CL", "America/Santiago");
        PRIMARY_ZONES.put("ID", "Asia/Jakarta");
        PRIMARY_ZONES.put("PT", "Europe/Lisbon");
        PRIMARY_ZONES.put("PF", "Pacific/Tahiti");
        PRIMARY_ZONES.put("SJ", "Arctic/Longyearbyen");
        PRIMARY_ZONES.put("KI", "Pacific/Tarawa");
        PRIMARY_ZONES.put("AQ", "Antarctica/McMurdo");
        PRIMARY_ZONES.put("AU", "Australia/Sydney");
        PRIMARY_ZONES.put("MH", "Pacific/Majuro");
        PRIMARY_ZONES.put("UZ", "Asia/Tashkent");
        PRIMARY_ZONES.put("RU", "Europe/Moscow");
        PRIMARY_ZONES.put("MY", "Asia/Kuala_Lumpur");
        PRIMARY_ZONES.put("MX", "America/Mexico_City");
        PRIMARY_ZONES.put("BR", "America/Sao_Paulo");
        PRIMARY_ZONES.put("ES", "Europe/Madrid");
        PRIMARY_ZONES.put("UM", "Pacific/Midway");
        PRIMARY_ZONES.put("CA", "America/Toronto");
        PRIMARY_ZONES.put("FM", "Pacific/Ponape");
        PRIMARY_ZONES.put("AR", "America/Buenos_Aires");
        PRIMARY_ZONES.put("NZ", "Pacific/Auckland");
    }

}
