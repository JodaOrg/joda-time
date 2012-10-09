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

import java.util.Collection;
import java.util.Map;

import org.joda.time.DateTime;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;

/**
 * An immutable PublicHoliday object which return
 * list of public holidays in France
 * 
 * @author Joakim Ribier
 * @since 2.1
 *
 */
public class FrancePublicHoliday extends PublicHoliday {

	/** Serialization lock */
	private static final long serialVersionUID = -6266413058013053119L;
	
	public static final String _1_JAN = "New Year's Day";
	public static final String _EASTER_MONDAY = "Easter Monday";
	public static final String _1_MAY = "May Day/Labour Day";
	public static final String _8_MAY_1945 = "Victory in Europe Day";
	public static final String _ASCENSION_DAY = "Ascension Day";
	public static final String _WHIT_MONDAY = "Whit Monday";
	public static final String _14_JUL = "Bastille Day";
	public static final String _15_AUG = "Assumption of Mary to Heaven";
	public static final String _1_NOV = "All Saints' Day";
	public static final String _11_NOV = "Armistice Day";
	public static final String _25_DEC = "Christmas Day";

	private static final int DAYS_AFTER_PAQUE_MONDAY = 39;
	private static final int WEEKS_AFTER_PAQUE_MONDAY = 7;
	
	private final Map<String, DateTime> staticPublicHolidays = Maps.newHashMap();
	
	protected FrancePublicHoliday(int year) {
		super(year);
		
		staticPublicHolidays.put(_1_JAN, new DateTime("1970-01-01"));
    	staticPublicHolidays.put(_1_MAY, new DateTime("1970-05-01"));
    	staticPublicHolidays.put(_8_MAY_1945, new DateTime("1970-05-08"));
    	staticPublicHolidays.put(_14_JUL, new DateTime("1970-07-14"));
    	staticPublicHolidays.put(_15_AUG, new DateTime("1970-08-15"));
    	staticPublicHolidays.put(_1_NOV, new DateTime("1970-11-01"));
    	staticPublicHolidays.put(_11_NOV, new DateTime("1970-11-11"));
    	staticPublicHolidays.put(_25_DEC, new DateTime("1970-12-25"));
	}

	@Override
	public ImmutableMap<String, DateTime> get() {
		return get(year);
	}
	
	@Override
	public ImmutableMap<String, DateTime> get(int year) {
		Builder<String, DateTime> builder = ImmutableMap.<String, DateTime>builder();
		for (java.util.Map.Entry<String, DateTime> entry: staticPublicHolidays.entrySet()) {
			builder.put(
					entry.getKey(),
					replaceEpochYearByTheCurrent(year, entry.getValue()));
			
		}
		fillMoveablePublicHolidays(builder, year);
		return builder.build();
	}

	@Override
	public ImmutableSortedSet<DateTime> sorted() {
		return sorted(year);
	}

	@Override
	public ImmutableSortedSet<DateTime> sorted(int year) {
		Collection<DateTime> values = get(year).values();
		return sortedCollection(values);
	}
	
	private DateTime replaceEpochYearByTheCurrent(int year, DateTime dateTime) {
		return new DateTime(
				year + "-" + dateTime.getMonthOfYear() + "-" + dateTime.getDayOfMonth()); 
	}

	private void fillMoveablePublicHolidays(Builder<String, DateTime> builder, int year) {
		builder.put(_EASTER_MONDAY, easterSunday(year).plusDays(1));
		builder.put(_ASCENSION_DAY, ascensionDay(year));
		builder.put(_WHIT_MONDAY, whitMonday(year).plusDays(1));
	}
	
	/** 
	 * Obtains Easter Sunday datetime
	 * 
	 * @param year
	 * @see http://en.wikipedia.org/wiki/Computus
	 * @return easter sunday date
	 */
	private DateTime easterSunday(int year) {
        int g, c, c_4, e, h, k, p, q, i, b, j1, j2, r;
        g = year % 19;
        c = year / 100;
        c_4 = c / 4;
        e = (8 * c + 13) / 25;
        h = (19 * g + c - c_4 - e + 15) % 30;
        k = h / 28;
        p = 29 / (h + 1);
        q = (21 - g) / 11;
        i = (k * p * q - 1) * k + h;
        b = year / 4 + year;
        j1 = (b + i + 2 + c_4) - c;
        j2 = j1 % 7;
        r = 28 + i - j2;
        StringBuilder builder = new StringBuilder();
        if (r > 31) {
        	builder.append(year).append("-").append(4).append("-").append(r-31);
        } else {
        	builder.append(year).append("-").append(3).append("-").append(r);
        }
        return new DateTime(builder.toString());
    }
	
	/**
	 * Compute ascension day
	 * 
	 * @param year
	 * @see http://en.wikipedia.org/wiki/Computus
	 * @return ascension day datetime
	 */
	private DateTime ascensionDay(int year) {
        return easterSunday(year).plusDays(DAYS_AFTER_PAQUE_MONDAY);
    }
	
	/**
	 * Compute whit monday
	 * 
	 * @param year
	 * @see http://en.wikipedia.org/wiki/Computus
	 * @return whit monday day datetime
	 */
	private DateTime whitMonday(int year) {
        return easterSunday(year).plusWeeks(WEEKS_AFTER_PAQUE_MONDAY);
    }
}
