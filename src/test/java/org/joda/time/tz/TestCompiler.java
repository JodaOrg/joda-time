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
package org.joda.time.tz;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.tz.ZoneInfoCompiler.DateTimeOfYear;

/**
 * Test cases for ZoneInfoCompiler.
 *
 * @author Brian S O'Neill
 */
public class TestCompiler extends TestCase {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestCompiler.class);
    }

    static final String AMERICA_LOS_ANGELES_FILE =
        "# Rules for building just America/Los_Angeles time zone.\n" + 
        "\n" + 
        "Rule    US  1918    1919    -   Mar lastSun 2:00    1:00    D\n" + 
        "Rule    US  1918    1919    -   Oct lastSun 2:00    0   S\n" + 
        "Rule    US  1942    only    -   Feb 9   2:00    1:00    W # War\n" + 
        "Rule    US  1945    only    -   Aug 14  23:00u  1:00    P # Peace\n" + 
        "Rule    US  1945    only    -   Sep 30  2:00    0   S\n" + 
        "Rule    US  1967    max -   Oct lastSun 2:00    0   S\n" + 
        "Rule    US  1967    1973    -   Apr lastSun 2:00    1:00    D\n" + 
        "Rule    US  1974    only    -   Jan 6   2:00    1:00    D\n" + 
        "Rule    US  1975    only    -   Feb 23  2:00    1:00    D\n" + 
        "Rule    US  1976    1986    -   Apr lastSun 2:00    1:00    D\n" + 
        "Rule    US  1987    max -   Apr Sun>=1  2:00    1:00    D\n" + 
        "\n" + 
        "Rule    CA  1948    only    -   Mar 14  2:00    1:00    D\n" + 
        "Rule    CA  1949    only    -   Jan  1  2:00    0   S\n" + 
        "Rule    CA  1950    1966    -   Apr lastSun 2:00    1:00    D\n" + 
        "Rule    CA  1950    1961    -   Sep lastSun 2:00    0   S\n" + 
        "Rule    CA  1962    1966    -   Oct lastSun 2:00    0   S\n" + 
        "\n" + 
        "Zone America/Los_Angeles -7:52:58 - LMT 1883 Nov 18 12:00\n" + 
        "            -8:00   US  P%sT    1946\n" + 
        "            -8:00   CA  P%sT    1967\n" + 
        "            -8:00   US  P%sT";

    static final String BROKEN_TIMEZONE_FILE =
        "# Incomplete Rules for building America/Los_Angeles time zone.\n" +
        "\n" +
        "Rule    US  1918    1919    -   Mar lastSun 2:00    1:00    D\n" +
        "Rule    \n" ; // this line is intentionally incomplete

    static final String BROKEN_TIMEZONE_FILE_2 =
        "# Incomplete Zone for building America/Los_Angeles time zone.\n" +
        "\n" +
        "Rule    CA  1948    only    -   Mar 14  2:00    1:00    D\n" +
        "Rule    CA  1949    only    -   Jan  1  2:00    0   S\n" +
        "\n" +
        "Zone "; // this line is intentionally left incomplete

    private DateTimeZone originalDateTimeZone = null;

    public TestCompiler(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        originalDateTimeZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    @Override
    protected void tearDown() throws Exception {
        DateTimeZone.setDefault(originalDateTimeZone);
    }

    //-----------------------------------------------------------------------
    public void testDateTimeZoneBuilder() throws Exception {
        // test multithreading, issue #18
        getTestDataTimeZoneBuilder().toDateTimeZone("TestDTZ1", true);
        final DateTimeZone[] zone = new DateTimeZone[1];
        Thread t = new Thread(new Runnable() {
            public void run() {
                zone[0] = getTestDataTimeZoneBuilder().toDateTimeZone("TestDTZ2", true);
            }
        });
        t.start();
        t.join();
        assertNotNull(zone[0]);
    }

    private DateTimeZoneBuilder getTestDataTimeZoneBuilder() {
         return new DateTimeZoneBuilder()
             .addCutover(1601, 'w', 1, 1, 1, false, 7200000)
             .setStandardOffset(3600000)
             .addRecurringSavings("", 3600000, 1601, Integer.MAX_VALUE, 'w', 3, -1, 1, false, 7200000)
             .addRecurringSavings("", 0, 1601, Integer.MAX_VALUE, 'w', 10, -1, 1, false, 10800000);
    }    

    //-----------------------------------------------------------------------
    public void testCompile() throws Exception {
        Provider provider = compileAndLoad(AMERICA_LOS_ANGELES_FILE);
        DateTimeZone tz = provider.getZone("America/Los_Angeles");

        assertEquals("America/Los_Angeles", tz.getID());
        assertEquals(false, tz.isFixed());
        TestBuilder.testForwardTransitions(tz, TestBuilder.AMERICA_LOS_ANGELES_DATA);
        TestBuilder.testReverseTransitions(tz, TestBuilder.AMERICA_LOS_ANGELES_DATA);
    }

    public void testCompileOnBrokenTimeZoneFile() throws Exception {
        try {
            Provider provider = compileAndLoad(BROKEN_TIMEZONE_FILE);
            fail();
        } catch(NoSuchElementException nsee) {
            // This used to be thrown in the Rule constructor
            fail("NoSuchElementException was thrown; broken timezone file?");
        } catch(IllegalArgumentException iae) {
            assertEquals("Attempting to create a Rule from an incomplete tokenizer", iae.getMessage());
        }
    }

    public void testCompileOnBrokenTimeZoneFile_2() throws Exception {
        try {
            Provider provider = compileAndLoad(BROKEN_TIMEZONE_FILE_2);
            fail();
        } catch (NoSuchElementException nsee) {
            // This thrown from the Zone constructor
            fail("NoSuchElementException was thrown; broken timezone file?");
        } catch (IllegalArgumentException iae) {
            assertEquals("Attempting to create a Zone from an incomplete tokenizer", iae.getMessage());
        }
    }

    private Provider compileAndLoad(String data) throws Exception {
        File tempDir = createDataFile(data);
        File destDir = makeTempDir();

        ZoneInfoCompiler.main(new String[] {
            "-src", tempDir.getAbsolutePath(),
            "-dst", destDir.getAbsolutePath(),
            "tzdata"
        });

        // Mark all files to be deleted on exit.
        deleteOnExit(destDir);

        return new ZoneInfoProvider(destDir);
    }

    private File createDataFile(String data) throws IOException {
        File tempDir = makeTempDir();

        File tempFile = new File(tempDir, "tzdata");
        tempFile.deleteOnExit();

        InputStream in = new ByteArrayInputStream(data.getBytes("UTF-8"));

        FileOutputStream out = new FileOutputStream(tempFile);
        byte[] buf = new byte[1000];
        int amt;
        while ((amt = in.read(buf)) > 0) {
            out.write(buf, 0, amt);
        }
        out.close();
        in.close();

        return tempDir;
    }

    private File makeTempDir() {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        tempDir = new File(tempDir, "joda-test-" + (new java.util.Random().nextInt() & 0xffffff));
        tempDir.mkdirs();
        tempDir.deleteOnExit();
        return tempDir;
    }

    private void deleteOnExit(File tempFile) {
        tempFile.deleteOnExit();
        if (tempFile.isDirectory()) {
            File[] files = tempFile.listFiles();
            for (int i=0; i<files.length; i++) {
                deleteOnExit(files[i]);
            }
        }
    }

    //-----------------------------------------------------------------------
    public void test_2400_fromDay() {
        StringTokenizer st = new StringTokenizer("Apr Sun>=1  24:00");
        DateTimeOfYear test = new DateTimeOfYear(st);
        assertEquals(4, test.iMonthOfYear);  // Apr
        assertEquals(2, test.iDayOfMonth);   // 2nd
        assertEquals(1, test.iDayOfWeek);    // Mon
        assertEquals(0, test.iMillisOfDay);  // 00:00
        assertEquals(true, test.iAdvanceDayOfWeek);
    }

    public void test_2400_last() {
        StringTokenizer st = new StringTokenizer("Mar lastSun 24:00");
        DateTimeOfYear test = new DateTimeOfYear(st);
        assertEquals(4, test.iMonthOfYear);  // Apr
        assertEquals(1, test.iDayOfMonth);   // 1st
        assertEquals(1, test.iDayOfWeek);    // Mon
        assertEquals(0, test.iMillisOfDay);  // 00:00
        assertEquals(false, test.iAdvanceDayOfWeek);
    }

    public void test_2400_specific_day() {
        StringTokenizer st = new StringTokenizer("Sep 21 24:00");
        DateTimeOfYear test = new DateTimeOfYear(st);
        assertEquals(9, test.iMonthOfYear);  // Sep
        assertEquals(22, test.iDayOfMonth);   // 22st
        assertEquals(0, test.iDayOfWeek);    // Ignored
        assertEquals(0, test.iMillisOfDay);  // 00:00
        assertEquals(false, test.iAdvanceDayOfWeek);
    }

    public void test_Amman_2003() {
        DateTimeZone zone = DateTimeZone.forID("Asia/Amman");
        DateTime dt = new DateTime(2003, 3, 1, 0, 0, zone);
        long next = zone.nextTransition(dt.getMillis());
        assertEquals(next, new DateTime(2003, 3, 28, 0, 0, DateTimeZone.forOffsetHours(2)).getMillis());
    }

    public void test_Amman_2004() {
        DateTimeZone zone = DateTimeZone.forID("Asia/Amman");
        DateTime dt = new DateTime(2004, 3, 1, 0, 0, zone);
        long next = zone.nextTransition(dt.getMillis());
        assertEquals(next, new DateTime(2004, 3, 26, 0, 0, DateTimeZone.forOffsetHours(2)).getMillis());
    }

    public void test_Amman_2005() {
        DateTimeZone zone = DateTimeZone.forID("Asia/Amman");
        DateTime dt = new DateTime(2005, 3, 1, 0, 0, zone);
        long next = zone.nextTransition(dt.getMillis());
        assertEquals(next, new DateTime(2005, 4, 1, 0, 0, DateTimeZone.forOffsetHours(2)).getMillis());
    }

    public void test_Amman_2006() {
        DateTimeZone zone = DateTimeZone.forID("Asia/Amman");
        DateTime dt = new DateTime(2006, 3, 1, 0, 0, zone);
        long next = zone.nextTransition(dt.getMillis());
        assertEquals(next, new DateTime(2006, 3, 31, 0, 0, DateTimeZone.forOffsetHours(2)).getMillis());
    }

    public void test_Tokyo_1949() {
        DateTimeZone zone = DateTimeZone.forID("Asia/Tokyo");
        DateTime dt = new DateTime(1949, 9, 7, 0, 0, zone);
        long next = zone.nextTransition(dt.getMillis());
        DateTime expected = new DateTime(1949, 9, 11, 0, 0, DateTimeZone.forOffsetHours(9));
        assertEquals(DateTimeConstants.SUNDAY, expected.getDayOfWeek());
        assertEquals(expected.getMillis(), next);
    }

}
