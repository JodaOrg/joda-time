package org.joda.time.format;

import java.util.List;
import java.util.Locale;
import java.util.ArrayList;

import java.util.Random;


import org.joda.time.Period;
import junit.framework.Assert;
import junit.framework.TestCase;
import junit.framework.TestSuite;



public class TestPerformance_PeriodFormat extends TestCase {
	private static final Locale FR = new Locale("fr");

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite() {
		return new TestSuite(TestPerformance_PeriodFormat.class);
	}

	public TestPerformance_PeriodFormat(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	private int[] randomIntegerGenerator(int datasetSize) {

		int randomIntegers[] = new int[datasetSize];

		for (int i = 0; i < datasetSize; i++) {
			int generatedInt = (int) (0 + (Math.random() * (31 - 0)));
			randomIntegers[i] = generatedInt;
		}
		return randomIntegers;

	}

	public void testPerfPeriodFormat() {

		int threshHold = 30;
		int datasetSize = 1000;
		int[] randomIntegers = randomIntegerGenerator(datasetSize);

		long start = System.currentTimeMillis();

		for (int k = 0; k < datasetSize; k++) {

			Period p = Period.days(randomIntegers[k]);
			PeriodFormat.getDefault().print(p);

		}

		long end = System.currentTimeMillis();

		System.out.println("Time to execute default in millis: " + (end - start));
		Assert.assertTrue(end - start <= threshHold);

	}

	public void testPerfPeriodFormatFR() {

		int threshHold = 390;
		int datasetSize = 10000;
		int[] randomIntegers = randomIntegerGenerator(datasetSize);

		long start = System.currentTimeMillis();

		for (int k = 0; k < datasetSize; k++) {

			Period p = Period.days(randomIntegers[k]);
			PeriodFormat.wordBased(FR).print(p);

		}

		long end = System.currentTimeMillis();

		System.out.println("Time to execute wordBased in millis: " + (end - start));
		Assert.assertTrue(end - start <= threshHold);

	}

}
