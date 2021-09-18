package org.joda.time.convert;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Set;

import org.joda.time.DateTimeZone;

import junit.framework.Assert;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class TestPerformance_DateConverter extends TestCase {
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite() {
		return new TestSuite(TestPerformance_DateConverter.class);
	}

	public TestPerformance_DateConverter(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	// to generate random number(index) to select random id from available timezone Ids 
	private DateTimeZone getRandomTimeZone(List<String> arr, int min, int max) {

		int p = (int) ((Math.random() * (max - min)) + min);
		return DateTimeZone.forID(arr.get(p));

	}

	public void testPerf() {

		long threshHold = 500;
		
		long executionTime = 0;

		List<String> aList = new ArrayList<String>(DateTimeZone.getAvailableIDs());
		

		for (int i = 0; i < 1000; i++) {
			
			long randomDate = new Random().nextLong();
			
			long start = System.currentTimeMillis();

		
			DateConverter.INSTANCE.getChronology(new Date(randomDate), getRandomTimeZone(aList, 0, aList.size()));

			long end = System.currentTimeMillis();
			
			executionTime+=end-start;
		}
		

		System.out.println("Time to execute in milis: " + (executionTime)); 
		Assert.assertTrue(executionTime <= threshHold);
	}

}