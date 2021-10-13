/*
 *  Copyright 2001-2012 Stephen Colebourne
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

import org.joda.time.DateTimeZone;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.time.ZoneId;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test cases for FixedDateTimeZone.
 *
 * @author Stephen Colebourne
 */
public class TestCachedDateTimeZone extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestCachedDateTimeZone.class);
    }

    private DateTimeZone originalDateTimeZone = null;

    public TestCachedDateTimeZone(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        originalDateTimeZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    protected void tearDown() throws Exception {
        DateTimeZone.setDefault(originalDateTimeZone);
    }

    public void testRangoonDateTimeZone_MappingToNewId_Asia_Yangon() throws Exception {
        DateTimeZone zoneOld = DateTimeZone.forID("Asia/Rangoon");
        assertEquals(zoneOld.getID(), "Asia/Yangon");

        DateTimeZone zoneNew = DateTimeZone.forID("Asia/Yangon");
        assertEquals(zoneNew.getID(), "Asia/Yangon");
    }

    public void testRangoonDateTimeZone_NoMapping() throws Exception {
        DateTimeZone zoneOld = DateTimeZone.forExactID("Asia/Rangoon");
        assertEquals(zoneOld.getID(), "Asia/Rangoon");

        DateTimeZone zoneNew = DateTimeZone.forID("Asia/Yangon");
        assertEquals(zoneNew.getID(), "Asia/Yangon");
    }

    public void testRangoonJavaUtilTimeZone() throws Exception {
        TimeZone zone = TimeZone.getTimeZone("Asia/Rangoon");
        assertEquals(zone.getID(), "Asia/Rangoon");

        TimeZone zoneNew = TimeZone.getTimeZone("Asia/Yangon");
        assertEquals(zoneNew.getID(), "Asia/Yangon");
    }

    public void testRangoonJavaTimeZoneId() throws Exception {
        ZoneId zone = ZoneId.of("Asia/Rangoon");
        assertEquals(zone.getId(), "Asia/Rangoon");

        ZoneId zoneNew = ZoneId.of("Asia/Yangon");
        assertEquals(zoneNew.getId(), "Asia/Yangon");
    }

    public void testRangoonCachingForMappingCode() throws Exception {
        DateTimeZone zoneYangon = DateTimeZone.forID("Asia/Rangoon");
        DateTimeZone zoneRangoon = DateTimeZone.forExactID("Asia/Rangoon");
        Field field_iZoneInfoMap = ZoneInfoProvider.class.getDeclaredField("iZoneInfoMap");
        field_iZoneInfoMap.setAccessible(true);
        Map<String, Object> iZoneInfoMap = (Map<String, Object>) field_iZoneInfoMap.get(DateTimeZone.getProvider());
        Entry<String, SoftReference<DateTimeZone>> entryRangoon = (Entry<String, SoftReference<DateTimeZone>>) iZoneInfoMap.get("Asia/Rangoon");
        assertEquals("Asia/Yangon", entryRangoon.getKey());
        SoftReference<DateTimeZone> entryYangon = (SoftReference<DateTimeZone>) iZoneInfoMap.get("Asia/Yangon");
        entryRangoon.getValue().clear();
        entryYangon.clear();

        assertEquals(DateTimeZone.forID("Asia/Rangoon"), zoneYangon);
        assertEquals(DateTimeZone.forExactID("Asia/Rangoon"), zoneRangoon);
        assertEquals(DateTimeZone.forID("Asia/Rangoon").getID(), "Asia/Yangon");
        assertEquals(DateTimeZone.forExactID("Asia/Rangoon").getID(), "Asia/Rangoon");
    }

    public void test_caching() throws Exception {
        CachedDateTimeZone zone1 = CachedDateTimeZone.forZone(DateTimeZone.forID("Europe/Paris"));
        CachedDateTimeZone zone2 = CachedDateTimeZone.forZone(DateTimeZone.forID("Europe/Paris"));
        assertSame(zone1, zone2);
    }

    //-----------------------------------------------------------------------
    public void testSerialization() throws Exception {
        CachedDateTimeZone test = CachedDateTimeZone.forZone(DateTimeZone.forID("Europe/Paris"));
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(test);
        oos.close();
        byte[] bytes = baos.toByteArray();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        CachedDateTimeZone result = (CachedDateTimeZone) ois.readObject();
        ois.close();
        
        assertEquals(test, result);
    }

}
