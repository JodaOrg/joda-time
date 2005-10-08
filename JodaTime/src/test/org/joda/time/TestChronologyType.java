/*
 *  Copyright 2001-2005 Stephen Colebourne
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
package org.joda.time;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.util.Set;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;

/**
 * This class is a Junit unit test for ChronologyType.
 *
 * @author Stephen Colebourne
 */
public class TestChronologyType extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestChronologyType.class);
    }

    public TestChronologyType(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    //-----------------------------------------------------------------------
    public void test_forName() throws Exception {
        assertSame(ChronologyType.iso(), ChronologyType.forID("ISO"));
        assertSame(null, ChronologyType.forID("rubbish"));
        assertSame(null, ChronologyType.forID(""));
        assertSame(null, ChronologyType.forID(null));
    }

    //-----------------------------------------------------------------------
    public void test_getAvailableIDs() throws Exception {
        Set set = ChronologyType.getAvailableIDs();
        assertEquals(7, set.size());
        assertEquals(true, set.contains("ISO"));
        assertEquals(true, set.contains("GJ"));
        assertEquals(true, set.contains("Gregorian"));
        assertEquals(true, set.contains("Julian"));
        assertEquals(true, set.contains("Buddhist"));
        assertEquals(true, set.contains("Coptic"));
        assertEquals(true, set.contains("Ethiopic"));
    }

    //-----------------------------------------------------------------------
    public void test_iso() throws Exception {
        assertEquals(ChronologyType.iso(), ChronologyType.iso());
        assertEquals("ISO", ChronologyType.iso().getID());
        assertEquals(ISOChronology.getInstance(), ChronologyType.iso().getChronology());
        assertEquals(ISOChronology.getInstanceUTC(), ChronologyType.iso().getChronologyUTC());
        assertEquals(ISOChronology.getInstance(PARIS), ChronologyType.iso().getChronology(PARIS));
        assertEquals("ISO", ChronologyType.iso().toString());
        assertSerialization(ChronologyType.iso());
    }

    public void test_gj() throws Exception {
        assertEquals(ChronologyType.gj(), ChronologyType.gj());
        assertEquals("GJ", ChronologyType.gj().getID());
        assertEquals(GJChronology.getInstance(), ChronologyType.gj().getChronology());
        assertEquals(GJChronology.getInstanceUTC(), ChronologyType.gj().getChronologyUTC());
        assertEquals(GJChronology.getInstance(PARIS), ChronologyType.gj().getChronology(PARIS));
        assertEquals("GJ", ChronologyType.gj().toString());
        assertSerialization(ChronologyType.gj());
    }

    public void test_gregorian() throws Exception {
        assertEquals(ChronologyType.gregorian(), ChronologyType.gregorian());
        assertEquals("Gregorian", ChronologyType.gregorian().getID());
        assertEquals(GregorianChronology.getInstance(), ChronologyType.gregorian().getChronology());
        assertEquals(GregorianChronology.getInstanceUTC(), ChronologyType.gregorian().getChronologyUTC());
        assertEquals(GregorianChronology.getInstance(PARIS), ChronologyType.gregorian().getChronology(PARIS));
        assertEquals("Gregorian", ChronologyType.gregorian().toString());
        assertSerialization(ChronologyType.gregorian());
    }

    public void test_julian() throws Exception {
        assertEquals(ChronologyType.julian(), ChronologyType.julian());
        assertEquals("Julian", ChronologyType.julian().getID());
        assertEquals(JulianChronology.getInstance(), ChronologyType.julian().getChronology());
        assertEquals(JulianChronology.getInstanceUTC(), ChronologyType.julian().getChronologyUTC());
        assertEquals(JulianChronology.getInstance(PARIS), ChronologyType.julian().getChronology(PARIS));
        assertEquals("Julian", ChronologyType.julian().toString());
        assertSerialization(ChronologyType.julian());
    }

    public void test_buddhist() throws Exception {
        assertEquals(ChronologyType.buddhist(), ChronologyType.buddhist());
        assertEquals("Buddhist", ChronologyType.buddhist().getID());
        assertEquals(BuddhistChronology.getInstance(), ChronologyType.buddhist().getChronology());
        assertEquals(BuddhistChronology.getInstanceUTC(), ChronologyType.buddhist().getChronologyUTC());
        assertEquals(BuddhistChronology.getInstance(PARIS), ChronologyType.buddhist().getChronology(PARIS));
        assertEquals("Buddhist", ChronologyType.buddhist().toString());
        assertSerialization(ChronologyType.buddhist());
    }

    public void test_coptic() throws Exception {
        assertEquals(ChronologyType.coptic(), ChronologyType.coptic());
        assertEquals("Coptic", ChronologyType.coptic().getID());
        assertEquals(CopticChronology.getInstance(), ChronologyType.coptic().getChronology());
        assertEquals(CopticChronology.getInstanceUTC(), ChronologyType.coptic().getChronologyUTC());
        assertEquals(CopticChronology.getInstance(PARIS), ChronologyType.coptic().getChronology(PARIS));
        assertEquals("Coptic", ChronologyType.coptic().toString());
        assertSerialization(ChronologyType.coptic());
    }

    public void test_ethiopic() throws Exception {
        assertEquals(ChronologyType.ethiopic(), ChronologyType.ethiopic());
        assertEquals("Ethiopic", ChronologyType.ethiopic().getID());
        assertEquals(EthiopicChronology.getInstance(), ChronologyType.ethiopic().getChronology());
        assertEquals(EthiopicChronology.getInstanceUTC(), ChronologyType.ethiopic().getChronologyUTC());
        assertEquals(EthiopicChronology.getInstance(PARIS), ChronologyType.ethiopic().getChronology(PARIS));
        assertEquals("Ethiopic", ChronologyType.ethiopic().toString());
        assertSerialization(ChronologyType.ethiopic());
    }

    public void test_other() throws Exception {
        assertEquals(1, ChronologyType.class.getDeclaredClasses().length);
        Class cls = ChronologyType.class.getDeclaredClasses()[0];
        assertEquals(1, cls.getDeclaredConstructors().length);
        Constructor con = cls.getDeclaredConstructors()[0];
        Object[] params = new Object[] { "other", new Byte((byte) 128) };
        ChronologyType type = (ChronologyType) con.newInstance(params);
        
        assertEquals("other", type.getID());
        try {
            type.getChronology(PARIS);
            fail();
        } catch (InternalError ex) {}
        assertSerialization(type);   // same because of ID map
    }

    //-----------------------------------------------------------------------
    private void assertSerialization(ChronologyType type) throws Exception {
        ChronologyType result = doSerialization(type);
        assertSame(type, result);
    }

    private ChronologyType doSerialization(ChronologyType type) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(type);
        byte[] bytes = baos.toByteArray();
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        ChronologyType result = (ChronologyType) ois.readObject();
        ois.close();
        return result;
    }

}
