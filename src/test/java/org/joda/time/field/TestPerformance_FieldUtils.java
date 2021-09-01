package org.joda.time.field;


import java.util.Random;


import junit.framework.Assert;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class TestPerformance_FieldUtils extends TestCase {
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite() {
		return new TestSuite(TestPerformance_FieldUtils.class);
	}

	public TestPerformance_FieldUtils(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}
	
	
	private long[] randomLongGenerator(int datasetSize) {
		long randomLongs[] = new long[datasetSize];

		for (int i = 0; i < datasetSize; i++) {
			long generatedLong = new Random().nextLong()/100000;
			randomLongs[i] = generatedLong;
		}
		return randomLongs;
		
	}
	
	public void testPerfSafeAddLong() {

		int datasetSize = 1000;
		int threshHold = 2400000;

		long[] randomLongs = randomLongGenerator(datasetSize);
		

		long start = System.nanoTime();

		for (int k = 0; k < datasetSize-1; k++) {
			
			
			FieldUtils.safeAdd(randomLongs[k], randomLongs[k+1]);
		}

		long end = System.nanoTime();

		System.out.println("Time to execute SafeAdd in nano seconds: " + (end - start));
		Assert.assertTrue(end - start <= threshHold);

	}
	
	public void testPerfSafeSubstractLong() {

		int datasetSize = 1000000;
		int threshHold = 522240;

		long[] randomLongs = randomLongGenerator(datasetSize);
		

		long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		

		for (int k = 0; k < datasetSize-1; k++) {
			
			
			FieldUtils.safeSubtract(randomLongs[k], randomLongs[k+1]);
		}
		
		long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();

		System.out.println("Memory Consumed " + (afterUsedMem - beforeUsedMem));
		Assert.assertTrue(afterUsedMem - beforeUsedMem <= threshHold);

	}
	
	public void testPerfSafeDivideLong() {

		int datasetSize = 1000;
		long threshHold =  270000;

		long[] randomLongs = randomLongGenerator(datasetSize);
		

		long start = System.nanoTime();
		
		

		for (int k = 0; k < datasetSize-1; k++) {
			
			
			FieldUtils.safeDivide(randomLongs[k], randomLongs[k+1]);
		}
		
		
		
		long end = System.nanoTime();

		System.out.println("Time to execute SafeDivide in nano seconds: " + (end - start));
		Assert.assertTrue(end - start <= threshHold);

	}
	
	
}