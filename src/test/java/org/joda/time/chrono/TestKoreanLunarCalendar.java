package org.joda.time.chrono;

import junit.framework.TestCase;

public class TestKoreanLunarCalendar extends TestCase {
	public void testGetAccumulatedDaysOfYears() throws Exception {
		int accumulatedDaysOfYears = KoreanLunarCalendar.getInstance().getAccumulatedDaysOfYears(2043);
		assertEquals(59534, accumulatedDaysOfYears);

		accumulatedDaysOfYears = KoreanLunarCalendar.getInstance().getAccumulatedDaysOfYears(1881);
		assertEquals(384, accumulatedDaysOfYears);

		accumulatedDaysOfYears = KoreanLunarCalendar.getInstance().getAccumulatedDaysOfYears(1882);
		assertEquals(739, accumulatedDaysOfYears);
	}

	public void testGetAccumulatedDaysOfMonths() throws Exception {
		int accumulatedDaysOfMonths = KoreanLunarCalendar.getInstance().getAccumulatedDaysOfMonths(1881, 6);
		assertEquals(177, accumulatedDaysOfMonths);

		accumulatedDaysOfMonths = KoreanLunarCalendar.getInstance().getAccumulatedDaysOfMonths(1881, 7);
		assertEquals(236, accumulatedDaysOfMonths);
	}

}
