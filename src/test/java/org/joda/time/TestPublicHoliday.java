/*
 *  Copyright 2001-2006 Stephen Colebourne
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

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.fest.assertions.Assertions;
import org.joda.time.publicholiday.CountrySupport;
import org.joda.time.publicholiday.FrancePublicHoliday;
import org.joda.time.publicholiday.PublicHoliday;
import org.joda.time.publicholiday.PublicHolidayBuilder;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;

/**
 * This class is a Junit unit test for DateTime.getPublicHolidays
 *
 * @author Joakim Ribier
 */
public class TestPublicHoliday extends TestCase {
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestPublicHoliday.class);
    }

    public TestPublicHoliday(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

	public void testCountrySupportBuildNPE() {
		try {
			new PublicHolidayBuilder().year(2012).build();
			fail("Should get an Exception");
		} catch (NullPointerException ex){
		}
	}
	
	public void testYearBuildIllegalStateException() {
		try {
			new PublicHolidayBuilder().country(CountrySupport.FRANCE).build();
			fail("Should get an IllegalArgumentException");
		} catch (IllegalArgumentException ex){
		}
	}
	
	public void testPublicHolidaysIsNotNull() {
		Assertions.assertThat(new PublicHolidayBuilder().
				country(CountrySupport.FRANCE).year(2012).build()).isNotNull();
	}
	
	public void testPublicHolidaysBuilderFranceIsNotNull() {
		Assertions.assertThat(new PublicHolidayBuilder().
				france().year(2012).build()).isNotNull();
	}
	
	public void testCountPublicHolidaysNumber() {
		PublicHoliday francePublicHoliday =
	    		DateTime.now().getPublicHoliday(CountrySupport.FRANCE);
		
		Set<Entry<String, DateTime>> entrySet = francePublicHoliday.get().entrySet();
		Assertions.assertThat(entrySet).hasSize(11);
	}
	
	public void testFrancePublicHolidays2012() {
		PublicHoliday francePublicHoliday =
	    		DateTime.now().getPublicHoliday(CountrySupport.FRANCE);
		
		Map<String, DateTime> publicHolidaysMap = francePublicHoliday.get(2012);
		assertPublicHolidays(publicHolidaysMap,
				FrancePublicHoliday._1_JAN, new DateTime("2012-01-01"));
		
		assertPublicHolidays(publicHolidaysMap,
				FrancePublicHoliday._EASTER_MONDAY, new DateTime("2012-04-09"));
		
		assertPublicHolidays(publicHolidaysMap,
				FrancePublicHoliday._1_MAY, new DateTime("2012-05-01"));
		
		assertPublicHolidays(publicHolidaysMap,
				FrancePublicHoliday._8_MAY_1945, new DateTime("2012-05-08"));

		assertPublicHolidays(publicHolidaysMap,
				FrancePublicHoliday._ASCENSION_DAY, new DateTime("2012-05-17"));
		
		assertPublicHolidays(publicHolidaysMap,
				FrancePublicHoliday._WHIT_MONDAY, new DateTime("2012-05-28"));

		assertPublicHolidays(publicHolidaysMap,
				FrancePublicHoliday._14_JUL, new DateTime("2012-07-14"));
		
		assertPublicHolidays(publicHolidaysMap,
				FrancePublicHoliday._15_AUG, new DateTime("2012-08-15"));
		
		assertPublicHolidays(publicHolidaysMap,
				FrancePublicHoliday._1_NOV, new DateTime("2012-11-01"));
		assertPublicHolidays(publicHolidaysMap,
				
				FrancePublicHoliday._11_NOV, new DateTime("2012-11-11"));
		
		assertPublicHolidays(publicHolidaysMap,
				FrancePublicHoliday._25_DEC, new DateTime("2012-12-25"));
	}
	
	public void testFranceMoveablePublicHolidays2013() {
		PublicHoliday francePublicHoliday =
	    		DateTime.now().getPublicHoliday(CountrySupport.FRANCE);
		
		Map<String, DateTime> publicHolidaysMap = francePublicHoliday.get(2013);
		assertPublicHolidays(publicHolidaysMap,
				FrancePublicHoliday._EASTER_MONDAY, new DateTime("2013-04-01"));
		
		assertPublicHolidays(publicHolidaysMap,
				FrancePublicHoliday._ASCENSION_DAY, new DateTime("2013-05-9"));
		
		assertPublicHolidays(publicHolidaysMap,
				FrancePublicHoliday._WHIT_MONDAY, new DateTime("2013-05-20"));
	}
	
	private void assertPublicHolidays(Map<String, DateTime> publicHolidays,
			String countrySupport, DateTime expectedDateTime) {

		Assertions.assertThat(
				publicHolidays.get(countrySupport)).isEqualTo(expectedDateTime);
	}
	
	public void testSortedFrancePublicHolidays2012() {
		PublicHoliday francePublicHoliday =
	    		DateTime.now().getPublicHoliday(CountrySupport.FRANCE);
		
		ImmutableSortedSet<DateTime> publicHolidaysSorted = francePublicHoliday.sorted(2012);
		Assertions.assertThat(
				Iterables.get(publicHolidaysSorted, 0)).isEqualTo(new DateTime("2012-01-01"));
		
		Assertions.assertThat(
				Iterables.getLast(publicHolidaysSorted)).isEqualTo(new DateTime("2012-12-25"));
	}
}
