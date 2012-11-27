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

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;

import org.joda.time.DateTime;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;

/**
 * An immutable abstract PublicHoliday
 * 
 * @author Joakim Ribier
 * @since 2.1
 *
 */
public abstract class PublicHoliday implements Serializable {

	/** Serialization lock */
	private static final long serialVersionUID = 6389705635776581657L;
	
	/** current year */
	protected final int year;

	/**
	 * Obtains a map with a public holiday to string key
	 * and a public holiday to datetime value of the current year.
	 * 
	 * @return map between string and datetime
	 */
	public abstract ImmutableMap<String, DateTime> get();
	
	/**
	 * Obtains a map with a public holiday to string key
	 * and a public holiday to datetime value of the parameter year.
	 * 
	 * @param year the year
	 * @return map between string and datetime
	 */
	public abstract ImmutableMap<String, DateTime> get(int year);

	/**
	 * Obtains set of public holiday datetime value
	 * sorted of the current year.
	 * 
	 * @return immutable set sorted
	 */
	public abstract ImmutableSortedSet<DateTime> sorted();
	
	/**
	 * Obtains set of public holiday datetime value
	 * sorted of the current year.
	 * 
	 * @param year the year
	 * @return immutable set sorted
	 */
	public abstract ImmutableSortedSet<DateTime> sorted(int year);

	protected PublicHoliday(int year) {
		this.year = year;
	}
	
	/**
	 * Order by public holidays datetime.
	 * 
	 * @param values public holidays datetime
	 * @return datetime values sorted
	 */
	protected ImmutableSortedSet<DateTime> sortedCollection(Collection<DateTime> values) {
		return ImmutableSortedSet.<DateTime>orderedBy(new Comparator<DateTime>() {
			public int compare(DateTime o1, DateTime o2) {
				return o1.compareTo(o2);
			}
		}).addAll(values).build();
	}
}