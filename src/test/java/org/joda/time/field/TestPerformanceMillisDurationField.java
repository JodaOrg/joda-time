package org.joda.time.field;

import java.util.Random;


import junit.framework.Assert;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class TestPerformanceMillisDurationField extends TestCase {
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite() {
		return new TestSuite(TestPerformanceMillisDurationField.class);
	}

	public TestPerformanceMillisDurationField(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}
	
	
	private long[] randomLongGenerator(int datasetSize) {
		long randomLongs[] = new long[datasetSize];

		for (int i = 0; i < datasetSize; i++) {
			long generatedLong = new Random().nextLong();
			randomLongs[i] = generatedLong;
		}
		return randomLongs;
	}

	private int[] randomIntegerGenerator(int datasetSize) {

		int randomIntegers[] = new int[datasetSize];

		for (int i = 0; i < datasetSize; i++) {
			int generatedInt = new Random().nextInt();
			randomIntegers[i] = generatedInt;
		}
		return randomIntegers;
	}
	

	public void testPerfAdd() {

		int datasetSize = 1000;
		int threshHold = 4000000;

		int[] randomIntegers = randomIntegerGenerator(datasetSize);
		long[] randomLongs = randomLongGenerator(datasetSize);

		long start = System.nanoTime();

		for (int k = 0; k < datasetSize; k++) {
			MillisDurationField.INSTANCE.add(randomLongs[k], randomIntegers[k]);
		}

		long end = System.nanoTime();

		System.out.println("Time to execute Add in nano seconds: " + (end - start));
		Assert.assertTrue(end - start <= threshHold);

	}

	public void testPerfDifference() {

		int datasetSize = 1000;
		int threshHold = 370000;

		int[] randomIntegers = randomIntegerGenerator(datasetSize);
		long[] randomLongs = randomLongGenerator(datasetSize);

		long start = System.nanoTime();

		for (int k = 0; k < datasetSize; k++) {

			MillisDurationField.INSTANCE.getDifferenceAsLong(randomLongs[k], randomIntegers[k]);
		}

		long end = System.nanoTime();

		System.out.println("Time to execute Difference in nano seconds: " + (end - start));
		Assert.assertTrue(end - start <= threshHold);

	}


}
