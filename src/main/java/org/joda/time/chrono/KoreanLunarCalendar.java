package org.joda.time.chrono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.Days;
import org.joda.time.Instant;
import org.joda.time.LocalDate;

/**
 * As Korean lunar calendar cannot be composed arithmetically,<br>
 * this class defines every single days of months from 1881-01-01 to 2043-12-31
 * <p>
 * Also, this class supports utilizing methods which are used by datetime field operations such as dayOfMonth, monthOfYear, etc. 
 * 
 * @author Billie Yang
 * @since 2.4
 */
public class KoreanLunarCalendar {
	private static final int YEAR_LOWER_LIMIT = 1881;
	private static final int YEAR_UPPER_LIMIT = 2043;

	private static KoreanLunarCalendar KOREAN_LUNAR_CALENDAR = null;
	private Map<Integer, Integer> accumulatedDaysOfYearCache = new HashMap<Integer, Integer>();
	private Map<Integer, Map<Integer, Integer>> accumulatedDaysOfMonthCache = new HashMap<Integer, Map<Integer, Integer>>();
	private Map<Integer, KoreanLunarMonth> monthsOfYears = null;

	public static KoreanLunarCalendar getInstance() {
		if (KOREAN_LUNAR_CALENDAR == null) {
			KOREAN_LUNAR_CALENDAR = new KoreanLunarCalendar();
		}
		return KOREAN_LUNAR_CALENDAR;
	}

	private KoreanLunarCalendar() {
		initializeMonthsOfYears();
	}

	public Map<Integer, KoreanLunarMonth> getMonthsOfYears() {
		return monthsOfYears;
	}

	private void initializeMonthsOfYears() {
		if (monthsOfYears != null) {
			return;
		}
		monthsOfYears = new HashMap<Integer, KoreanLunarMonth>();
		monthsOfYears.put(1881, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 29, 30, 30, 30, 30, 29, 30, 29)), 7, 29));
		monthsOfYears.put(1882, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 29, 30, 29, 30, 30, 29, 30, 30))));
		monthsOfYears.put(1883, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 29, 30, 29, 29, 30, 29, 30, 30, 30, 29, 30))));
		monthsOfYears.put(1884, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 29, 30, 29, 30, 29, 30, 30, 29, 30, 30)), 5, 29));
		monthsOfYears.put(1885, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 29, 30, 29, 29, 30, 29, 30, 29, 30, 30))));
		monthsOfYears.put(1886, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 30, 29, 29, 30, 29, 30, 29, 30))));
		monthsOfYears.put(1887, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 30, 30, 29, 29, 30, 29, 30, 29, 30)), 4, 29));
		monthsOfYears.put(1888, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 30, 29, 30, 29, 29, 30, 29, 30, 29))));
		monthsOfYears.put(1889, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 30, 29, 30, 29, 30, 29, 30, 29, 30))));
		monthsOfYears.put(1890, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 30, 29, 30, 30, 29, 30, 29, 30, 29, 30)), 2, 29));
		monthsOfYears.put(1891, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 29, 30, 29, 30, 30, 29, 30, 30))));
		monthsOfYears.put(1892, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 29, 30, 29, 29, 30, 30, 30, 29, 30, 30, 30)), 6, 29));
		monthsOfYears.put(1893, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 29, 30, 29, 29, 30, 29, 30, 29, 30, 30, 30))));
		monthsOfYears.put(1894, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 29, 29, 30, 29, 30, 29, 30, 30))));
		monthsOfYears.put(1895, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 30, 29, 30, 29, 30, 29, 30, 29)), 5, 29));
		monthsOfYears.put(1896, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 30, 29, 30, 29, 29, 30, 29, 30, 29, 30))));
		monthsOfYears.put(1897, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 30, 29, 30, 29, 30, 29, 30, 29, 30, 29))));
		monthsOfYears.put(1898, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 30, 30, 29, 30, 29, 30, 29, 30, 29)), 3, 29));
		monthsOfYears.put(1899, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 30, 29, 30, 30, 29, 30, 29, 30))));
		monthsOfYears.put(1900, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 29, 30, 29, 30, 30, 30, 30, 29, 30)), 8, 29));
		monthsOfYears.put(1901, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 29, 30, 29, 30, 29, 30, 30, 30, 29))));
		monthsOfYears.put(1902, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 29, 30, 29, 30, 29, 30, 30, 30))));
		monthsOfYears.put(1903, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 29, 30, 29, 29, 30, 30, 29, 30)), 5, 29));
		monthsOfYears.put(1904, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 30, 29, 29, 30, 29, 29, 30, 30, 29))));
		monthsOfYears.put(1905, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 30, 30, 29, 29, 30, 29, 30, 29, 30))));
		monthsOfYears.put(1906, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 30, 29, 29, 30, 29, 30, 29, 30, 29, 30)), 4, 30));
		monthsOfYears.put(1907, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 29, 30, 30, 29, 30, 29, 30, 29))));
		monthsOfYears.put(1908, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 29, 30, 30, 29, 30, 29, 30, 30, 29, 30))));
		monthsOfYears.put(1909, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 29, 30, 29, 30, 30, 30, 29, 30)), 2, 29));
		monthsOfYears.put(1910, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 29, 30, 29, 30, 29, 30, 30, 30, 29))));
		monthsOfYears.put(1911, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 29, 30, 29, 30, 30, 29, 30, 30)), 6, 29));
		monthsOfYears.put(1912, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 29, 30, 29, 29, 30, 30, 29, 30))));
		monthsOfYears.put(1913, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 30, 29, 29, 30, 29, 29, 30, 29, 30))));
		monthsOfYears.put(1914, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 30, 30, 29, 30, 29, 30, 29, 29, 30)), 5, 29));
		monthsOfYears.put(1915, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 30, 29, 30, 29, 30, 29, 30, 29, 30))));
		monthsOfYears.put(1916, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 29, 30, 30, 29, 30, 29, 30, 29))));
		monthsOfYears.put(1917, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 30, 30, 29, 30, 30, 29, 30, 29)), 2, 29));
		monthsOfYears.put(1918, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 29, 30, 29, 30, 29, 30, 30, 30, 29, 30))));
		monthsOfYears.put(1919, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 29, 30, 29, 30, 30, 30, 29, 30, 30)), 7, 29));
		monthsOfYears.put(1920, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 29, 30, 29, 29, 30, 30, 29, 30, 30))));
		monthsOfYears.put(1921, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 29, 30, 29, 29, 30, 29, 30, 30))));
		monthsOfYears.put(1922, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 30, 29, 30, 29, 29, 30, 29, 30, 30)), 5, 29));
		monthsOfYears.put(1923, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 30, 29, 30, 29, 30, 29, 30, 29, 29, 30))));
		monthsOfYears.put(1924, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 30, 30, 29, 30, 29, 30, 29, 29))));
		monthsOfYears.put(1925, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 30, 30, 29, 30, 30, 29, 30, 29, 30)), 4, 29));
		monthsOfYears.put(1926, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 29, 30, 29, 30, 29, 30, 30, 29, 30, 30, 29))));
		monthsOfYears.put(1927, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 29, 30, 29, 30, 29, 30, 30, 29, 30, 30))));
		monthsOfYears.put(1928, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 29, 29, 30, 30, 29, 30, 30, 30)), 2, 29));
		monthsOfYears.put(1929, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 29, 30, 29, 29, 30, 29, 30, 30, 30))));
		monthsOfYears.put(1930, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 30, 29, 29, 30, 29, 30, 29, 30, 30, 29)), 6, 29));
		monthsOfYears.put(1931, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 30, 29, 29, 30, 29, 29, 30, 29, 30, 29))));
		monthsOfYears.put(1932, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 30, 29, 30, 29, 30, 29, 29, 30, 29, 30))));
		monthsOfYears.put(1933, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 30, 29, 30, 29, 30, 29, 30, 29, 29, 30)), 5, 30));
		monthsOfYears.put(1934, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 30, 29, 30, 30, 29, 30, 29, 30))));
		monthsOfYears.put(1935, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 29, 30, 29, 30, 29, 30, 30, 29, 30, 30, 29))));
		monthsOfYears.put(1936, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 29, 29, 30, 29, 30, 29, 30, 30, 30, 29)), 3, 30));
		monthsOfYears.put(1937, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 29, 30, 29, 29, 30, 29, 30, 30, 30, 29))));
		monthsOfYears.put(1938, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 29, 30, 29, 29, 29, 30, 30, 29, 30)), 7, 30));
		monthsOfYears.put(1939, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 29, 30, 29, 29, 30, 29, 30, 29, 30))));
		monthsOfYears.put(1940, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 30, 29, 30, 29, 29, 30, 29, 30, 29))));
		monthsOfYears.put(1941, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 30, 30, 29, 29, 29, 30, 29, 30, 29)), 6, 30));
		monthsOfYears.put(1942, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 30, 29, 30, 30, 29, 30, 29, 29, 30))));
		monthsOfYears.put(1943, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 29, 30, 30, 29, 30, 30, 29, 30))));
		monthsOfYears.put(1944, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 29, 30, 29, 29, 30, 29, 30, 30, 29, 30, 30)), 4, 30));
		monthsOfYears.put(1945, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 29, 30, 29, 29, 30, 29, 30, 30, 30, 29, 30))));
		monthsOfYears.put(1946, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 29, 30, 29, 29, 30, 29, 30, 30, 29, 30))));
		monthsOfYears.put(1947, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 30, 29, 29, 30, 29, 30, 29, 30, 30)), 2, 29));
		monthsOfYears.put(1948, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 30, 29, 29, 30, 29, 30, 29, 30))));
		monthsOfYears.put(1949, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 30, 29, 30, 29, 30, 29, 30, 29, 30)), 7, 29));
		monthsOfYears.put(1950, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 30, 29, 30, 29, 29, 30, 29, 30, 29))));
		monthsOfYears.put(1951, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 30, 29, 30, 29, 30, 29, 30, 29, 30))));
		monthsOfYears.put(1952, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 29, 30, 29, 30, 29, 30, 29, 30)), 5, 30));
		monthsOfYears.put(1953, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 29, 30, 30, 29, 30, 30, 29, 30, 30))));
		monthsOfYears.put(1954, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 29, 30, 29, 29, 30, 29, 30, 30, 29, 30, 30))));
		monthsOfYears.put(1955, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 29, 29, 29, 30, 29, 30, 29, 30, 30, 30)), 3, 30));
		monthsOfYears.put(1956, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 29, 29, 30, 29, 30, 29, 30, 30))));
		monthsOfYears.put(1957, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 30, 29, 29, 30, 30, 29, 30, 30)), 8, 29));
		monthsOfYears.put(1958, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 30, 29, 30, 29, 29, 30, 29, 30, 29, 30))));
		monthsOfYears.put(1959, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 30, 29, 30, 29, 30, 29, 30, 29, 30, 29))));
		monthsOfYears.put(1960, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 30, 30, 30, 29, 30, 29, 30, 29)), 6, 29));
		monthsOfYears.put(1961, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 30, 29, 30, 30, 29, 30, 29, 30))));
		monthsOfYears.put(1962, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 29, 30, 29, 30, 30, 29, 30, 30, 29))));
		monthsOfYears.put(1963, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 30, 29, 30, 29, 30, 30, 30, 29)), 4, 29));
		monthsOfYears.put(1964, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 29, 30, 29, 30, 29, 30, 30, 30))));
		monthsOfYears.put(1965, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 29, 29, 30, 29, 29, 30, 30, 30))));
		monthsOfYears.put(1966, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 30, 30, 29, 29, 30, 29, 29, 30, 30, 29)), 3, 29));
		monthsOfYears.put(1967, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 30, 30, 29, 29, 30, 29, 30, 29, 30))));
		monthsOfYears.put(1968, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 30, 29, 30, 29, 30, 30, 29, 30, 29, 30)), 7, 29));
		monthsOfYears.put(1969, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 29, 30, 30, 29, 30, 29, 30, 29))));
		monthsOfYears.put(1970, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 29, 30, 30, 29, 30, 29, 30, 30, 29, 30))));
		monthsOfYears.put(1971, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 29, 30, 30, 29, 30, 30, 30, 29, 30)), 5, 29));
		monthsOfYears.put(1972, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 29, 30, 29, 30, 29, 30, 30, 30, 29))));
		monthsOfYears.put(1973, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 29, 30, 29, 29, 30, 30, 30, 29))));
		monthsOfYears.put(1974, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 30, 29, 30, 29, 29, 30, 30, 29, 30)), 4, 29));
		monthsOfYears.put(1975, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 30, 29, 29, 30, 29, 29, 30, 29, 30))));
		monthsOfYears.put(1976, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 30, 29, 30, 29, 30, 30, 29, 29, 30)), 8, 29));
		monthsOfYears.put(1977, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 30, 29, 30, 29, 30, 29, 30, 29, 29))));
		monthsOfYears.put(1978, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 30, 29, 30, 30, 29, 30, 29, 30, 29))));
		monthsOfYears.put(1979, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 29, 30, 29, 30, 29, 30, 30, 29, 30, 29)), 6, 30));
		monthsOfYears.put(1980, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 29, 30, 29, 30, 29, 30, 30, 29, 30, 30))));
		monthsOfYears.put(1981, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 29, 30, 29, 29, 30, 30, 29, 30, 30))));
		monthsOfYears.put(1982, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 30, 29, 29, 30, 30, 29, 30, 30)), 4, 29));
		monthsOfYears.put(1983, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 29, 30, 29, 29, 30, 29, 30, 30))));
		monthsOfYears.put(1984, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 30, 29, 29, 30, 29, 29, 30, 30, 30)), 10, 29));
		monthsOfYears.put(1985, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 30, 29, 30, 29, 30, 29, 29, 30, 29, 30))));
		monthsOfYears.put(1986, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 30, 29, 30, 30, 29, 30, 29, 30, 29, 29))));
		monthsOfYears.put(1987, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 30, 29, 30, 30, 30, 29, 30, 29, 30)), 6, 29));
		monthsOfYears.put(1988, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 29, 30, 29, 30, 29, 30, 30, 29, 30, 30, 29))));
		monthsOfYears.put(1989, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 29, 30, 29, 30, 29, 30, 30, 29, 30, 30))));
		monthsOfYears.put(1990, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 29, 30, 29, 30, 30, 29, 30, 30, 30)), 5, 29));
		monthsOfYears.put(1991, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 29, 30, 29, 29, 30, 29, 30, 30, 30))));
		monthsOfYears.put(1992, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 30, 29, 29, 30, 29, 29, 30, 29, 30, 30))));
		monthsOfYears.put(1993, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 30, 30, 29, 30, 29, 29, 30, 29, 30, 29)), 3, 29));
		monthsOfYears.put(1994, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 30, 29, 30, 29, 30, 29, 29, 30, 29, 30))));
		monthsOfYears.put(1995, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 30, 29, 30, 30, 29, 30, 30, 29, 29, 30)), 8, 29));
		monthsOfYears.put(1996, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 30, 29, 30, 29, 30, 30, 29, 30))));
		monthsOfYears.put(1997, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 29, 30, 29, 30, 29, 30, 30, 29, 30, 30, 29))));
		monthsOfYears.put(1998, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 29, 30, 29, 30, 30, 29, 30, 30, 30, 29)), 5, 29));
		monthsOfYears.put(1999, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 29, 30, 29, 29, 30, 29, 30, 30, 30, 29))));
		monthsOfYears.put(2000, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 29, 30, 29, 29, 30, 29, 30, 30, 29))));
		monthsOfYears.put(2001, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 30, 29, 30, 29, 29, 30, 29, 30, 29, 30)), 4, 29));
		monthsOfYears.put(2002, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 30, 29, 30, 29, 29, 30, 29, 30, 29))));
		monthsOfYears.put(2003, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 30, 30, 29, 30, 29, 29, 30, 29, 30))));
		monthsOfYears.put(2004, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 30, 30, 29, 30, 29, 30, 29, 30, 29, 30)), 2, 29));
		monthsOfYears.put(2005, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 29, 30, 30, 29, 30, 30, 29, 29))));
		monthsOfYears.put(2006, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 30, 29, 30, 30, 30, 29, 30, 30)), 7, 29));
		monthsOfYears.put(2007, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 29, 30, 29, 29, 30, 29, 30, 30, 30, 29, 30))));
		monthsOfYears.put(2008, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 29, 30, 29, 29, 30, 29, 30, 30, 29, 30))));
		monthsOfYears.put(2009, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 29, 30, 29, 30, 29, 30, 29, 30, 30)), 5, 29));
		monthsOfYears.put(2010, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 30, 29, 29, 30, 29, 30, 29, 30))));
		monthsOfYears.put(2011, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 30, 29, 30, 29, 29, 30, 29, 30, 29))));
		monthsOfYears.put(2012, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 30, 29, 30, 29, 29, 30, 29, 30, 29)), 3, 30));
		monthsOfYears.put(2013, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 30, 29, 30, 29, 30, 29, 30, 29, 30))));
		monthsOfYears.put(2014, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 29, 30, 29, 30, 30, 30, 29, 30)), 9, 29));
		monthsOfYears.put(2015, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 29, 30, 29, 30, 30, 30, 29, 30, 29))));
		monthsOfYears.put(2016, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 29, 30, 29, 30, 30, 29, 30, 30))));
		monthsOfYears.put(2017, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 29, 30, 29, 30, 29, 30, 30, 30)), 5, 29));
		monthsOfYears.put(2018, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 29, 29, 30, 29, 30, 29, 30, 30))));
		monthsOfYears.put(2019, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 30, 29, 29, 30, 29, 30, 29, 30))));
		monthsOfYears.put(2020, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 30, 30, 29, 29, 30, 29, 30, 29, 30)), 4, 29));
		monthsOfYears.put(2021, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 30, 29, 30, 29, 30, 29, 30, 29, 30, 29))));
		monthsOfYears.put(2022, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 30, 30, 29, 30, 29, 30, 29, 30))));
		monthsOfYears.put(2023, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 30, 29, 30, 29, 30, 30, 29, 30, 29, 30)), 2, 29));
		monthsOfYears.put(2024, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 29, 30, 29, 30, 30, 29, 30, 30, 29))));
		monthsOfYears.put(2025, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 29, 30, 30, 29, 30, 30, 30, 29)), 6, 29));
		monthsOfYears.put(2026, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 29, 30, 29, 30, 29, 30, 30, 30))));
		monthsOfYears.put(2027, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 29, 29, 30, 29, 29, 30, 30, 30))));
		monthsOfYears.put(2028, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 30, 29, 30, 29, 30, 29, 29, 30, 30, 29)), 5, 29));
		monthsOfYears.put(2029, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 30, 30, 29, 29, 30, 29, 29, 30, 30))));
		monthsOfYears.put(2030, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 30, 29, 30, 29, 30, 29, 30, 29))));
		monthsOfYears.put(2031, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 30, 29, 30, 30, 29, 30, 29, 30, 29)), 3, 29));
		monthsOfYears.put(2032, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 29, 30, 29, 30, 30, 29, 30, 30, 29, 30))));
		monthsOfYears.put(2033, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 29, 30, 29, 30, 29, 30, 30, 30, 30)), 11, 29));
		monthsOfYears.put(2034, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 29, 30, 29, 30, 29, 30, 30, 30, 29))));
		monthsOfYears.put(2035, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 29, 29, 30, 29, 29, 30, 30, 29, 30))));
		monthsOfYears.put(2036, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 30, 29, 29, 29, 29, 30, 30, 29, 30)), 6, 30));
		monthsOfYears.put(2037, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 30, 29, 29, 30, 29, 29, 30, 29, 30))));
		monthsOfYears.put(2038, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 30, 29, 30, 29, 30, 29, 29, 30, 29))));
		monthsOfYears.put(2039, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 30, 29, 30, 30, 30, 29, 30, 29, 30, 29, 29)), 5, 29));
		monthsOfYears.put(2040, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 30, 30, 29, 30, 30, 29, 30, 29, 30, 29))));
		monthsOfYears.put(2041, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(30, 29, 29, 30, 29, 30, 30, 29, 30, 30, 29, 30))));
		monthsOfYears.put(2042, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 30, 29, 30, 29, 30, 30, 29, 30, 30)), 2, 29));
		monthsOfYears.put(2043, new KoreanLunarMonth(new ArrayList<Integer>(Arrays.asList(29, 30, 29, 29, 30, 29, 29, 30, 30, 29, 30, 30))));
	}

	/**
	 * Calculate accumulated days of years until upperboundYear (include upperboundYear)
	 * 
	 * @param upperboundYear
	 * @return
	 */
	int getAccumulatedDaysOfYears(int upperboundYear) {
		int accumulatedDaysOfYears = 0;
		for (int year = upperboundYear; year >= YEAR_LOWER_LIMIT; year--) {
			if (!accumulatedDaysOfYearCache.containsKey(year)) {
				int daysOfYear = 0;

				for (Integer daysInMonth : monthsOfYears.get(year).getDaysInMonths()) {
					daysOfYear += daysInMonth;
				}
				if (monthsOfYears.get(year).getLeapMonth() != null) {
					daysOfYear += monthsOfYears.get(year).getDaysInLeapMonth();
				}

				accumulatedDaysOfYearCache.put(year, daysOfYear);
			}
			accumulatedDaysOfYears += accumulatedDaysOfYearCache.get(year);
		}
		return accumulatedDaysOfYears;
	}

	int getAccumulatedDaysOfMonths(int year, int monthOfYear) {
		if (!accumulatedDaysOfMonthCache.containsKey(year)) {
			accumulatedDaysOfMonthCache.put(year, new HashMap<Integer, Integer>());
		}

		int accumulatedDaysOfMonths = 0;
		for (int m = monthOfYear; m >= 1; m--) {
			if (!accumulatedDaysOfMonthCache.get(year).containsKey(m)) {
				int daysOfMonth = monthsOfYears.get(year).getDaysInMonths().get(m - 1);
				if (monthsOfYears.get(year).getLeapMonth() != null && monthsOfYears.get(year).getLeapMonth() == m) {
					daysOfMonth += monthsOfYears.get(year).getDaysInLeapMonth();
				}
				accumulatedDaysOfMonthCache.get(year).put(m, daysOfMonth);
			}
			accumulatedDaysOfMonths += accumulatedDaysOfMonthCache.get(year).get(m);
		}

		return accumulatedDaysOfMonths;
	}

	public int getYear(long instant) {
		return getYear(getDaysFromLowerLimitToInstant(instant));
	}

	public int getYear(int daysFrom1881ToInstant) {
		int year = YEAR_LOWER_LIMIT;
		for (; year <= YEAR_UPPER_LIMIT; year++) {
			if (daysFrom1881ToInstant < getAccumulatedDaysOfYears(year)) {
				break;
			}
		}

		return year;
	}

	public int getMonthOfYear(long instant) {
		return getMonthOfYear(getYear(instant), getDaysFromLowerLimitToInstant(instant) - getAccumulatedDaysOfYears(getYear(instant) - 1));
	}

	public int getMonthOfYear(int year, int daysFromStartOfYear) {
		int monthOfYear = 1;
		for (; monthOfYear <= 12; monthOfYear++) {
			if (daysFromStartOfYear < getAccumulatedDaysOfMonths(year, monthOfYear)) {
				break;
			}
		}
		return monthOfYear;
	}

	public int getDayOfMonth(long instant) {
		int year = getYear(instant);
		int daysFromLowerLimitToInstant = getDaysFromLowerLimitToInstant(instant);
		int accumulatedDaysBeforeInstantYear = getAccumulatedDaysOfYears(year - 1);
		int monthOfYear = getMonthOfYear(year, daysFromLowerLimitToInstant - accumulatedDaysBeforeInstantYear);

		int dayOfMonth = daysFromLowerLimitToInstant - accumulatedDaysBeforeInstantYear - getAccumulatedDaysOfMonths(year, monthOfYear - 1);
		if (dayOfMonth >= monthsOfYears.get(year).getDaysInMonths().get(monthOfYear - 1)) {
			dayOfMonth -= monthsOfYears.get(year).getDaysInMonths().get(monthOfYear - 1);
		}
		return dayOfMonth + 1;
	}

	int getDaysFromLowerLimitToInstant(long instant) {
		return Days.daysBetween(LocalDate.parse("1881-01-30").toDateTimeAtStartOfDay(), new Instant(instant)).getDays();
	}

	public int getDayOfWeek(long instant) {
		int year = getYear(instant);
		int monthOfYear = getMonthOfYear(year, getDaysFromLowerLimitToInstant(instant) - getAccumulatedDaysOfYears(getYear(instant) - 1));
		int dayOfMonth = getDayOfMonth(instant);

		return LocalDate.parse(year + "-" + monthOfYear + "-" + dayOfMonth).getDayOfWeek();
	}

	public boolean isLeapMonth(long instant) {
		int year = getYear(instant);
		int daysFromLowerLimitToInstant = getDaysFromLowerLimitToInstant(instant);
		int accumulatedDaysBeforeInstantYear = getAccumulatedDaysOfYears(year - 1);
		int monthOfYear = getMonthOfYear(year, daysFromLowerLimitToInstant - accumulatedDaysBeforeInstantYear);

		int dayOfMonth = daysFromLowerLimitToInstant - accumulatedDaysBeforeInstantYear - getAccumulatedDaysOfMonths(year, monthOfYear - 1);
		if (dayOfMonth >= monthsOfYears.get(year).getDaysInMonths().get(monthOfYear - 1)) {
			return true;
		}
		return false;
	}

	public long addYears(long instant, int value) {
		if (value == 0) {
			return instant;
		}

		int year = getYear(instant);
		int monthOfYear = getMonthOfYear(instant);
		boolean leapMonth = isLeapMonth(instant);

		long addedInstant = addMonths(instant, value * 12 + getCountOfLeapMonthsBetweenYearMonths(year, monthOfYear, leapMonth, year + value, monthOfYear));

		return addedInstant;
	}

	public long addMonths(long instant, int value) {
		if (value == 0) {
			return instant;
		}

		int year = getYear(instant);
		int monthOfYear = getMonthOfYear(instant);
		boolean leapMonth = isLeapMonth(instant);
		int dayOfMonth = getDayOfMonth(instant);

		long daysToBeAdded = 0;

		// add remaining days of current month
		if (leapMonth) {
			daysToBeAdded += getMonthsOfYears().get(year).getDaysInLeapMonth() - dayOfMonth;
		} else {
			daysToBeAdded += getMonthsOfYears().get(year).getDaysInMonths().get(monthOfYear - 1) - dayOfMonth;
		}

		// starting from next month, add days of remaining months
		int yearOffset = year;
		int monthOffset = monthOfYear + 1;
		boolean leapOffset = !leapMonth && getMonthsOfYears().get(yearOffset).getLeapMonth() != null && (monthOffset == getMonthsOfYears().get(yearOffset).getLeapMonth());
		for (int remainingMonthsToBeAdded = value - 1; remainingMonthsToBeAdded > 0; remainingMonthsToBeAdded--) {
			if (monthOffset > 12) {
				yearOffset++;
				monthOffset = 1;
			}

			if (leapOffset) {
				leapOffset = false;
				daysToBeAdded += getMonthsOfYears().get(yearOffset).getDaysInLeapMonth();
			} else {
				daysToBeAdded += getMonthsOfYears().get(yearOffset).getDaysInMonths().get(monthOffset - 1);
				leapOffset = (getMonthsOfYears().get(yearOffset).getLeapMonth() != null) && (monthOffset == getMonthsOfYears().get(yearOffset).getLeapMonth());
				if (leapOffset) {
					monthOffset--;
				}
			}
			monthOffset++;
		}

		// calculate initial dayOfMonth value in result month
		daysToBeAdded += dayOfMonth;

		long addedInstant = instant + daysToBeAdded * 1000L * 60L * 60L * 24L;
		int resultDayOfMonth = getDayOfMonth(addedInstant);

		// the-end-day-of-month correction process
		if (dayOfMonth > resultDayOfMonth) {
			if (resultDayOfMonth < 10) {
				addedInstant -= 1000L * 60L * 60L * 24L;
			} else {
				addedInstant += 1000L * 60L * 60L * 24L;
			}
		} else if (dayOfMonth < resultDayOfMonth) {
			addedInstant -= 1000L * 60L * 60L * 24L;
		}

		return addedInstant;
	}

	int getCountOfLeapMonthsBetweenYearMonths(int startYear, int startMonth, boolean startMonthLeap, int endYear, int endMonth) {
		int countOfLeapMonths = 0;
		for (int year = startYear; year <= endYear; year++) {
			Integer leapMonthOfTheYear = getMonthsOfYears().get(year).getLeapMonth();
			if (leapMonthOfTheYear == null) {
				continue;
			}

			// startYear
			if (year == startYear) {
				if (!startMonthLeap && startMonth <= leapMonthOfTheYear || startMonthLeap && startMonth < leapMonthOfTheYear) {
					countOfLeapMonths++;
				}
			}

			// between (startYear,endYear)
			if (year > startYear && year < endYear) {
				countOfLeapMonths++;
			}

			// endYear
			if (year == endYear) {
				if (endMonth > leapMonthOfTheYear || startMonthLeap && endMonth == leapMonthOfTheYear) {
					countOfLeapMonths++;
				}
			}
		}
		return countOfLeapMonths;
	}
}
