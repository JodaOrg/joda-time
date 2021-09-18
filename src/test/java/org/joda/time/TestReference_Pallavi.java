package org.joda.time;

import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class TestReference_Pallavi extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite() {
		return new TestSuite(TestReference_Pallavi.class);
	}

	public TestReference_Pallavi(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	DateTime[] dateTime1 = new DateTime[100000];
	DateTime[] dateTime2 = new DateTime[100000];

	public void randomDates() {

		for (int i = 0; i < dateTime1.length; i++) {
			long generatedLong = new Random().nextInt();
			DateTime dateTime = new DateTime(new Long(generatedLong));
			dateTime1[i] = dateTime;
			// System.out.println("Array 1: "+dateTime1[i]);
		}

		for (int j = 0; j < dateTime2.length; j++) {
			long generatedLong = new Random().nextInt();
			DateTime dateTime = new DateTime(new Long(generatedLong));
			dateTime2[j] = dateTime;
			// System.out.println("Array 2: "+dateTime2[j]);
		}
	}

	public void testPerf_yearsBetween() {

		randomDates();

		//int threshHold = 1000;

		long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		for (int k = 0; k < dateTime1.length; k++) {
			
			Years.yearsBetween(dateTime1[k], dateTime2[k]).getYears();

		}
		
		long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		//System.out.println("Memory Used for years:"+ (afterUsedMem - beforeUsedMem));
		
		Assert.assertTrue((afterUsedMem - beforeUsedMem)<= 0);

	}

	// Test case for Months
	public void testPerf_monthsBetween() {

		randomDates();
		//int threshHold = 2000;
		long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		for (int k = 0; k < dateTime1.length; k++) {
			// System.out.println(Months.monthsBetween(dateTime1[k],dateTime2[k]).getMonths());
			Months.monthsBetween(dateTime1[k], dateTime2[k]).getMonths();
		}

		long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		//System.out.println("Memory Used for months:"+ (afterUsedMem - beforeUsedMem));
		
		Assert.assertTrue((afterUsedMem - beforeUsedMem)<= 470016);

	}

	// Test case for Days
	public void testPerf_daysBetween() {
		randomDates();

		//int threshHold = 10000;

		long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		for (int k = 0; k < dateTime1.length; k++) {
			// System.out.println(Days.daysBetween(dateTime1[k], dateTime2[k]).getDays());
			Days.daysBetween(dateTime1[k], dateTime2[k]).getDays();
		}

		long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		//System.out.println("Memory Used for days:"+ (afterUsedMem - beforeUsedMem));
		
		Assert.assertTrue((afterUsedMem - beforeUsedMem)<= 1634940);

	}

	// Test case for Hours
	public void testPerf_hoursBetween() {
		randomDates();
		//int threshHold = 1000;

		LocalTime start4 = new LocalTime();
		for (int k = 0; k < dateTime1.length; k++) {
			// System.out.println( Hours.hoursBetween(dateTime1[k],dateTime2[k]).getHours());
			Hours.hoursBetween(dateTime1[k], dateTime2[k]).getHours();
		}
		LocalTime end4 = new LocalTime();
		//System.out.println("Time to execute hours in millis: " + (end4.getLocalMillis() - start4.getLocalMillis()));

		Assert.assertTrue(end4.getLocalMillis() - start4.getLocalMillis() <= 45);
	}

	// Test case for Minutes
	public void testPerf_minutesBetween() {
		randomDates();

		//int threshHold = 10000;

		LocalTime start5 = new LocalTime();
		for (int k = 0; k < dateTime1.length; k++) {
			// System.out.println( Minutes.minutesBetween(dateTime1[k],dateTime2[k]).getMinutes());
			Minutes.minutesBetween(dateTime1[k], dateTime2[k]).getMinutes();
		}
		LocalTime end5 = new LocalTime();
		//System.out.println("Time to execute minutes in millis: " + (end5.getLocalMillis() - start5.getLocalMillis()));

		Assert.assertTrue(end5.getLocalMillis() - start5.getLocalMillis() <= 21);
	}

	// Test case for Seconds
	public void testPerf_secondsBetween() {
		randomDates();

		//int threshHold = 10000;

		LocalTime start6 = new LocalTime();
		for (int k = 0; k < dateTime1.length; k++) {
			// System.out.println( Seconds.secondsBetween(dateTime1[k],dateTime2[k]).getSeconds());
			Seconds.secondsBetween(dateTime1[k], dateTime2[k]).getSeconds();

		}
		LocalTime end6 = new LocalTime();
		//System.out.println("Time to execute seconds in millis: " + (end6.getLocalMillis() - start6.getLocalMillis()));

		Assert.assertTrue(end6.getLocalMillis() - start6.getLocalMillis() <= 44);

	}

	// Test case for Week
	public void testPerf_weeksBewtween() {

		randomDates();

		//int threshHold = 10000;

		LocalTime start7 = new LocalTime();
		for (int k = 0; k < dateTime1.length; k++) {
			// System.out.println( Weeks.weeksBetween(dateTime1[k],dateTime2[k]).getWeeks());
			Weeks.weeksBetween(dateTime1[k], dateTime2[k]).getWeeks();
		}
		LocalTime end7 = new LocalTime();
		//System.out.println("Time to execute week in millis: " + (end7.getLocalMillis() - start7.getLocalMillis()));

		Assert.assertTrue(end7.getLocalMillis() - start7.getLocalMillis() <= 35);
	}
}
