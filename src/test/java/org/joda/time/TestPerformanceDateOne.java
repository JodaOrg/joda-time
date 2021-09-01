package org.joda.time;

import java.util.List;
import java.util.ArrayList;

import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestPerformanceDateOne extends TestCase {
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite() {
		return new TestSuite(TestPerformanceDateOne.class);
	}

	public TestPerformanceDateOne(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	int size = 1000;
	List<DateTime> randomDates = new ArrayList<DateTime>(size);

	public void generateDates() {

		for (int i = 0; i < size; i++) {

			long generatedNumber = new Random().nextInt() / 10000;
			DateTime dateTime = new DateTime(new Long(generatedNumber));
			randomDates.add(dateTime);
		}

	}

	public void testPeriodPerf() {

		int threshHold = 12;

		generateDates();

		DateTime startTime = new DateTime();
		// System.out.println("Start Timestamp:"+startTime);

		for (int i = 0; i < size - 1; i++) {

			Period p = new Period(randomDates.get(i), randomDates.get(i + 1), PeriodType.millis());

		}

		DateTime endTime = new DateTime();
		// System.out.println("End Timestamp:"+endTime);

		System.out.println("Time to execute in millis: " + (endTime.getMillis() - startTime.getMillis()));
		Assert.assertTrue(endTime.getMillis() - startTime.getMillis() <= threshHold);
	}

	// public void test

}
