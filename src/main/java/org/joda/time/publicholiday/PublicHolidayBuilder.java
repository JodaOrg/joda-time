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


import com.google.common.base.Preconditions;

/**
 * Builder which return a PublicHoliday object from CountrySupport
 * 
 * @author Joakim Ribier
 * @since 2.1
 *
 */
public class PublicHolidayBuilder {

	private CountrySupport countrySupport;
	private int year = -1;

	public PublicHolidayBuilder() {}
	
	/**
	 * Set CountrySupport
	 * 
	 * @param countrySupport
	 * @return this
	 */
	public PublicHolidayBuilder country(CountrySupport countrySupport) {
		this.countrySupport = countrySupport;
		return this;
	}
	
	/**
	 * Defines directly countrySupport to FRANCE
	 * 
	 * @return this
	 */
	public PublicHolidayBuilder france() {
		this.countrySupport = CountrySupport.FRANCE;
		return this;
	}
	
	/**
	 * Set default year
	 * 
	 * @param year
	 * @return this
	 */
	public PublicHolidayBuilder year(int year) {
		this.year = year;
		return this;
	}
	
	/**
	 * Obtains PublicHoliday object from countrySupport
	 * 
	 * @return PublicHoliday
	 */
	public PublicHoliday build() {
		Preconditions.checkNotNull(countrySupport, "The countrySupport field is required.");
		Preconditions.checkArgument(year != -1, "The year field is required.");
		return new PublicHolidayFactory().getPublicHoliday(countrySupport, year);
	}
}
