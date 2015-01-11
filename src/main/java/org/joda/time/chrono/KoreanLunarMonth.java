package org.joda.time.chrono;

import java.util.List;

/**
 * A data structure used by {@link KoreanLunarCalendar}.<br>
 * Korean lunar calendar has several leap months which is determined by astronomical observations.
 * 
 * @author Billie Yang
 */
public class KoreanLunarMonth {
	private List<Integer> daysInMonths;
	private Integer leapMonth;
	private Integer daysInLeapMonth;

	public KoreanLunarMonth(List<Integer> daysInMonths) {
		this(daysInMonths, null, null);
	}

	public KoreanLunarMonth(List<Integer> daysInMonths, Integer leapMonth, Integer daysInLeapMonth) {
		if (daysInMonths.size() != 12) {
			throw new IllegalArgumentException("daysInMonths argument should be size of 12.");
		}
		if (leapMonth != null && (leapMonth < 0 || leapMonth > 12)) {
			throw new IllegalArgumentException("leapMonth argument should be null or in [1-12] range.");
		}

		this.daysInMonths = daysInMonths;
		this.leapMonth = leapMonth;
		if (leapMonth != null) {
			this.daysInLeapMonth = daysInLeapMonth;
		}
	}

	public Integer getLeapMonth() {
		return leapMonth;
	}

	public void setLeapMonth(Integer leapMonth) {
		this.leapMonth = leapMonth;
	}

	public Integer getDaysInLeapMonth() {
		return daysInLeapMonth;
	}

	public void setDaysInLeapMonth(Integer daysInLeapMonth) {
		this.daysInLeapMonth = daysInLeapMonth;
	}

	public List<Integer> getDaysInMonths() {
		return daysInMonths;
	}

	public void setDaysInMonths(List<Integer> daysInMonths) {
		this.daysInMonths = daysInMonths;
	}
}
