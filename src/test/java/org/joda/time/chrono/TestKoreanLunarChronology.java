package org.joda.time.chrono;

import java.io.BufferedReader;
import java.io.FileReader;

import junit.framework.TestCase;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

public class TestKoreanLunarChronology extends TestCase {
	public void testKoreanLunarChronology() throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(this.getClass().getResource("/SolarToLunar.txt").getFile()));
		String line = null;
		while ((line = reader.readLine()) != null) {
			String[] parts = line.split("\t");
			String solarDateString = parts[0];
			String expectedResult = parts[1];

			try {
				assertEquals(expectedResult, getLunarDate(solarDateString));
			} catch (IndexOutOfBoundsException e) {
				System.out.println(solarDateString);
			}
		}
		reader.close();
	}

	private String getLunarDate(String solarDate) {
		DateTime lunarDateTime = LocalDate.parse(solarDate).toDateTimeAtStartOfDay().withChronology(KoreanLunarChronology.getInstance(DateTimeZone.forOffsetHours(9)));

		StringBuilder sb = new StringBuilder(lunarDateTime.toString("yyyy년 MM월 dd일"));
		sb.append(" (");
		if (lunarDateTime.monthOfYear().isLeap()) {
			sb.append("윤달");
		} else {
			sb.append("평달");
		}
		sb.append(")");

		return sb.toString();
	}

	public void testKoreanLunarChronologyAddDaysTest() throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(this.getClass().getResource("/SolarToLunar.txt").getFile()));
		String line = null;

		DateTime lunarDateTime = DateTime.parse("1881-01-30").withChronology(KoreanLunarChronology.getInstanceKST());

		int i = 0;
		while ((line = reader.readLine()) != null) {
			String[] parts = line.split("\t");
			String lunarDateString = parts[1];

			DateTime lunarDateTimeDaysAdded = lunarDateTime.plusDays(i);
			StringBuilder sb = new StringBuilder();
			sb.append(lunarDateTimeDaysAdded.toString(DateTimeFormat.forPattern("yyyy년 MM월 dd일 ")));
			if (lunarDateTimeDaysAdded.monthOfYear().isLeap()) {
				sb.append("(윤달)");
			} else {
				sb.append("(평달)");
			}

			String resultString = sb.toString();
			assertEquals(lunarDateString, resultString);
			i++;
		}
		reader.close();
	}

	public void testKoreanLuanrChronologyAddMonthsTest() throws Exception {
		DateTime lunarDateTime = DateTime.parse("1881-01-30").withChronology(KoreanLunarChronology.getInstanceKST());
		assertEquals("1881년 2월 1일 (화)", lunarDateTime.plusMonths(1).toString(DateTimeFormat.forStyle("L-")));
		assertEquals("1881년 3월 1일 (화)", lunarDateTime.plusMonths(2).toString(DateTimeFormat.forStyle("L-")));
		assertEquals("1881년 4월 1일 (금)", lunarDateTime.plusMonths(3).toString(DateTimeFormat.forStyle("L-")));
		assertEquals("1881년 5월 1일 (일)", lunarDateTime.plusMonths(4).toString(DateTimeFormat.forStyle("L-")));
		assertEquals("1881년 6월 1일 (수)", lunarDateTime.plusMonths(5).toString(DateTimeFormat.forStyle("L-")));
		assertEquals("1881년 7월 1일 (금)", lunarDateTime.plusMonths(6).toString(DateTimeFormat.forStyle("L-")));
		assertEquals("1881년 7월 1일 (금)", lunarDateTime.plusMonths(7).toString(DateTimeFormat.forStyle("L-")));
		assertEquals("1881년 8월 1일 (월)", lunarDateTime.plusMonths(8).toString(DateTimeFormat.forStyle("L-")));
		assertEquals("1881년 9월 1일 (목)", lunarDateTime.plusMonths(9).toString(DateTimeFormat.forStyle("L-")));
		assertEquals("1881년 10월 1일 (토)", lunarDateTime.plusMonths(10).toString(DateTimeFormat.forStyle("L-")));
		assertEquals("1881년 11월 1일 (화)", lunarDateTime.plusMonths(11).toString(DateTimeFormat.forStyle("L-")));
		assertEquals("1881년 12월 1일 (목)", lunarDateTime.plusMonths(12).toString(DateTimeFormat.forStyle("L-")));
		assertEquals("1882년 1월 1일 (일)", lunarDateTime.plusMonths(13).toString(DateTimeFormat.forStyle("L-")));
	}
}
