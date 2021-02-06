/*
 *  Copyright 2017 Stephen Colebourne
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
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTimeZone;
import org.joda.time.MockZone;

import junit.framework.TestCase;

/**
 * Unit tests for class {@link ZoneInfoCompiler}.
 *
 * @date 2017-07-31
 * @see ZoneInfoCompiler
 **/
public class TestZoneInfoCompiler extends TestCase {

  public void testParseDataFileWithTrue() throws IOException {
    ZoneInfoCompiler zoneInfoCompiler = new ZoneInfoCompiler();
    StringReader stringReader = new StringReader(" I  ;>- ");
    BufferedReader bufferedReader = new BufferedReader(stringReader, 2014);
    zoneInfoCompiler.parseDataFile(bufferedReader, true);
  }

  public void testParseDataFileWithFalse() throws IOException {
    ZoneInfoCompiler zoneInfoCompiler = new ZoneInfoCompiler();
    StringReader stringReader = new StringReader("XYC5w.9eA}*U#A;mu");
    BufferedReader bufferedReader = new BufferedReader(stringReader);
    zoneInfoCompiler.parseDataFile(bufferedReader, false);
  }

  public void testTestOne() {
    DateTimeZone dateTimeZone = DateTimeZone.forOffsetMillis(8);

    assertTrue(ZoneInfoCompiler.test("+02:15", dateTimeZone));
  }

  public void testTestTwo() {
    DateTimeZoneBuilder dateTimeZoneBuilder = new DateTimeZoneBuilder();
    DateTimeZone dateTimeZone = dateTimeZoneBuilder.toDateTimeZone("7M6 _n'63", true);

    assertTrue(ZoneInfoCompiler.test("7M6 _n'63", dateTimeZone));
  }

  public void testParseZoneCharFive() {
    assertEquals('s', ZoneInfoCompiler.parseZoneChar('s'));
  }

  public void testParseYearAndParseYearReturningPositiveAndParseYearWithNegative() {
    assertEquals(Integer.MAX_VALUE, ZoneInfoCompiler.parseYear("maximum", (-2512)));
  }

  public void testParseYearAndParseYearReturningNegativeOne() {
    assertEquals(Integer.MIN_VALUE, ZoneInfoCompiler.parseYear("min", (-1881443201)));
  }

  public void testParseYearAndParseYearReturningNegativeTwo() {
    assertEquals(Integer.MIN_VALUE, ZoneInfoCompiler.parseYear("minimum", (-1971487955)));
  }

  public void testWriteZoneInfoMapThrowsIllegalArgumentException() throws IOException {
    Map<String, MockZone> hashMap = new HashMap<String, MockZone>();
    MockZone mockZone = new MockZone(0L, 3022, 90);
    hashMap.put("     - ", mockZone);
    Map<String, DateTimeZone> hashMapTwo = new HashMap<String, DateTimeZone>(hashMap);
    hashMapTwo.put("H)m^v1(", mockZone);

    try {
      ZoneInfoCompiler.writeZoneInfoMap(null, hashMapTwo);
      fail("Expecting exception: IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals(ZoneInfoCompiler.class.getName(), e.getStackTrace()[0].getClassName());
    }
  }

}
