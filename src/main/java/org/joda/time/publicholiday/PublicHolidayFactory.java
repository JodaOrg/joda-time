/*
 *  Copyright 2012 Joakim Ribier
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
package org.joda.time.publicholiday;


/**
 * Factory which create a PublicHoliday
 * 
 * @author Joakim Ribier
 * @since 2.1
 *
 */
public class PublicHolidayFactory {

	/**
	 * Constructs an instance of PublicHoliday from a CountrySupport
	 * 
	 * @param countrySupport represents a country
	 * @param year 
	 * @throws IllegalArgumentException if the country is not supported
	 */
	protected PublicHoliday getPublicHoliday(CountrySupport countrySupport, int year) {
		switch (countrySupport) {
		case FRANCE:
			return new FrancePublicHoliday(year);
			
		default:
			throw new IllegalArgumentException(
					"Country " + countrySupport.getLabel() + " not supported");
			
		}
	}
}