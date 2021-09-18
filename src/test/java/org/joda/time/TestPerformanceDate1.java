package org.joda.time;

import java.util.List;
import java.util.Locale;
import java.util.ArrayList;

import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class TestPerformanceDate1 extends TestCase {
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite() {
		return new TestSuite(TestPerformanceDate1.class);
	}

	public TestPerformanceDate1(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}
	
	int size = 1000;//100000;
	List<DateTime> randomDates = new ArrayList<DateTime>(size);

	// Method generating dates randomly

	public void generateDates() {

		for (int i = 0; i < size; i++) {	

			long generatedNumber = new Random().nextInt() / 10000;
			DateTime dateTime = new DateTime(new Long(generatedNumber));
			randomDates.add(dateTime);
		}
		 //System.out.println("Random Dates are:"+randomDates);
	}
	
	// Performance test for getting MonthOfYear as a text
	public void testMonthAsText() {
		//int threshold = 1000;
		
		generateDates();
		
		LocalTime start = new LocalTime();
		for (int i = 0; i < size; i++) 
		{
			randomDates.get(i).monthOfYear().getAsText(Locale.ENGLISH);
		}
		LocalTime end = new LocalTime();
		//System.out.println("Time to execute text method in millis: " + (end.getLocalMillis() - start.getLocalMillis()));
		Assert.assertTrue(end.getLocalMillis() - start.getLocalMillis() <= 571);
		
	}
	
	// Performance test for IsLeapYear
		public void testIsLeapPerf() {
			//int threshold = 1000;
			
			generateDates();
			
			LocalTime start = new LocalTime();
			for (int i = 0; i < size; i++) 
			{
				randomDates.get(i).year().isLeap();
			}
			LocalTime end = new LocalTime();
			//System.out.println("Time to execute isleap year in millis: " + (end.getLocalMillis() - start.getLocalMillis()));
			Assert.assertTrue(end.getLocalMillis() - start.getLocalMillis() <= 420);
			
		}
	// Performance test for getting Century of era
	public void testCentuaryOfEraPerf() {
		//int threshold = 100000;
		
		generateDates();
		
		LocalTime start = new LocalTime();
		for (int i = 0; i < size; i++) 
		{
			randomDates.get(i).getCenturyOfEra();
		}
		LocalTime end = new LocalTime();
		//System.out.println("Time to execute centuary in millis: " + (end.getLocalMillis() - start.getLocalMillis()));
		Assert.assertTrue(end.getLocalMillis() - start.getLocalMillis() <= 396);
		
	}
	
	// Performance test for getting day of Week 
		public void testDayOfWeekPerf() {
			//int threshold = 1000;
			
			generateDates();
			
			LocalTime start = new LocalTime();
			for (int i = 0; i < size; i++) 
			{
				randomDates.get(i).getDayOfWeek();
			}
			LocalTime end = new LocalTime();
			//System.out.println("Time to execute day of week in millis: " + (end.getLocalMillis() - start.getLocalMillis()));
			Assert.assertTrue(end.getLocalMillis() - start.getLocalMillis() <= 342);	
		}
		
	// Performance test for getting day of year 
				public void testDayOfYearPerf() {
					//int threshold = 1000;
					
					generateDates();
					
					LocalTime start = new LocalTime();
					for (int i = 0; i < size; i++) 
					{
						randomDates.get(i).getDayOfYear();
					}
					LocalTime end = new LocalTime();
					//System.out.println("Time to execute day of year in millis: " + (end.getLocalMillis() - start.getLocalMillis()));
					Assert.assertTrue(end.getLocalMillis() - start.getLocalMillis() <= 415);	
				}
				
		// Performance test for getting day of Month 
				public void testDayOfMonthPerf() {
					//int threshold = 1000;
					
					generateDates();
					
					long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
					for (int i = 0; i < size; i++) 
					{
						randomDates.get(i).getDayOfMonth();
					}
					long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
					//System.out.println("Memory Used to get day of month:"+ (afterUsedMem- beforeUsedMem));
					
					Assert.assertTrue((afterUsedMem - beforeUsedMem)<= 0);	
				}
		 // Performance test for getting Month of year
				public void testMonthOfYearPerf() {
					//int threshold = 1000;
					
					generateDates();
					
					long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
					for (int i = 0; i < size; i++) 
					{
						randomDates.get(i).getMonthOfYear();
					}
					long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
					//System.out.println("Memory Used to get month of year:"+ (afterUsedMem- beforeUsedMem));
					
					Assert.assertTrue((afterUsedMem - beforeUsedMem)<= 0);
					
				}
	// Performance test for getting zone
		public void testZonePerf() {
					//int threshold = 1000;
					
					generateDates();
					
					long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
					for (int i = 0; i < size; i++) 
					{
						randomDates.get(i).getZone();
					}
					long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
					//System.out.println("Memory Used to get zone :"+ (afterUsedMem- beforeUsedMem));
					
					Assert.assertTrue((afterUsedMem - beforeUsedMem)<= 0);
					
				}
				
				
	// Performance test for converting date in to string
	public void testToStringPerf() {
		//int threshold = 1000;
		
		generateDates();
		
		long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		for (int i = 0; i < size; i++) 
		{
			randomDates.get(i).toString();
		}
		long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		//System.out.println("Memory Used to get string:"+ (afterUsedMem- beforeUsedMem));
		
		Assert.assertTrue((afterUsedMem - beforeUsedMem)<= 1593940374);
	}
}
