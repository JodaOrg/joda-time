/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-03 Stephen Colebourne.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Joda project (http://www.joda.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "Joda" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact licence@joda.org.
 *
 * 5. Products derived from this software may not be called "Joda",
 *    nor may "Joda" appear in their name, without prior written
 *    permission of the Joda project.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE JODA AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Joda project and was originally
 * created by Stephen Colebourne <scolebourne@joda.org>. For more
 * information on the Joda project, please see <http://www.joda.org/>.
 */
package org.joda.test.time;

//import java.util.*;
//import java.text.* ;
import junit.framework.TestSuite;
//import org.joda.time.*;
//import org.joda.time.gj.*;
//import org.joda.time.iso.*;
/**
 * This class is a Junit unit test for the
 * GJDateTimeFormatterBuilder date time class.
 *
 * @author Stephen Colebourne
 * @author Guy Allard
 */
public class TestDateTimeFormatterBuilder extends BulkTest {

	/**
	 * This is the main class for this test suite.
	 * @param args command line arguments.
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}
	/**
	 * TestSuite is a junit required method.
	 */
	public static TestSuite suite() {
		return BulkTest.makeSuite(TestDateTimeFormatterBuilder.class);
	}
	/**
	 * TestDateTimeField constructor.
	 * @param name
	 */
	public TestDateTimeFormatterBuilder(String name) {
		super(name);
	}
	// Class Name: org.joda.time.format.DateTimeFormatterBuilder
	/**
	 * Junit <code>setUp()</code> method.
	 */
	protected void setUp() /* throws Exception */ {
		// super.setUp();
	}
	/**
	 * Junit <code>tearDown()</code> method.
	 */
	protected void tearDown() /* throws Exception */ {
		// super.tearDown();
	}
	/**
	 * Test the <code>toPrinter()</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#toPrinter()
	 */
	public void testToPrinter() {
		fail("TBD");
	}
	/**
	 * Test the <code>toParser()</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#toParser()
	 */
	public void testToParser() {
		fail("TBD");
	}
	/**
	 * Test the <code>toFormatter()</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#toFormatter()
	 */
	public void testToFormatter() {
		fail("TBD");
	}
	/**
	 * Test the <code>canBuildPrinter()</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#canBuildPrinter()
	 */
	public void testCanBuildPrinter() {
		fail("TBD");
	}
	/**
	 * Test the <code>canBuildParser()</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#canBuildParser()
	 */
	public void testCanBuildParser() {
		fail("TBD");
	}
	/**
	 * Test the <code>canBuilderFormatter()</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#canBuilderFormatter()
	 */
	public void testCanBuilderFormatter() {
		fail("TBD");
	}
	/**
	 * Test the <code>clear()</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#clear()
	 */
	public void testClear() {
		fail("TBD");
	}
	/**
	 * Test the <code>append(org.joda.time.format.DateTimeFormatter)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#append(org.joda.time.format.DateTimeFormatter)
	 */
	public void testAppendDTF() {
		fail("TBD");
	}
	/**
	 * Test the <code>append(org.joda.time.format.DateTimePrinterElement, org.joda.time.format.DateTimeParserElement)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#append(org.joda.time.format.DateTimePrinterElement, org.joda.time.format.DateTimeParserElement)
	 */
	public void testAppendDTPE() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendLiteral(char)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendLiteral(char)
	 */
	public void testAppendLiteralC() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendLiteral(java.lang.String)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendLiteral(java.lang.String)
	 */
	public void testAppendLiteralS() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendNumeric(org.joda.time.DateTimeField, int, int, int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendNumeric(org.joda.time.DateTimeField, int, int, int)
	 */
	public void testAppendNumeric() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendSignedNumeric(org.joda.time.DateTimeField, int, int, int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendSignedNumeric(org.joda.time.DateTimeField, int, int, int)
	 */
	public void testAppendSignedNumeric() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendFraction(int, int, int, int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendFraction(int, int, int, int)
	 */
	public void testAppendFraction() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendFractionOfSecond(int, int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendFractionOfSecond(int, int)
	 */
	public void testAppendFractionOfSecond() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendFractionOfMinute(int, int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendFractionOfMinute(int, int)
	 */
	public void testAppendFractionOfMinute() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendFractionOfHour(int, int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendFractionOfHour(int, int)
	 */
	public void testAppendFractionOfHour() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendFractionOfDay(int, int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendFractionOfDay(int, int)
	 */
	public void testAppendFractionOfDay() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendMillisOfSecond(int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendMillisOfSecond(int)
	 */
	public void testAppendMillisOfSecond() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendMillisOfDay(int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendMillisOfDay(int)
	 */
	public void testAppendMillisOfDay() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendSecondOfMinute(int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendSecondOfMinute(int)
	 */
	public void testAppendSecondOfMinute() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendSecondOfDay(int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendSecondOfDay(int)
	 */
	public void testAppendSecondOfDay() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendMinuteOfHour(int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendMinuteOfHour(int)
	 */
	public void testAppendMinuteOfHour() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendMinuteOfDay(int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendMinuteOfDay(int)
	 */
	public void testAppendMinuteOfDay() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendHourOfDay(int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendHourOfDay(int)
	 */
	public void testAppendHourOfDay() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendClockhourOfDay(int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendClockhourOfDay(int)
	 */
	public void testAppendClockhourOfDay() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendHourOfHalfday(int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendHourOfHalfday(int)
	 */
	public void testAppendHourOfHalfday() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendClockhourOfHalfday(int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendClockhourOfHalfday(int)
	 */
	public void testAppendClockhourOfHalfday() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendDayOfWeek(int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendDayOfWeek(int)
	 */
	public void testAppendDayOfWeek() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendDayOfMonth(int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendDayOfMonth(int)
	 */
	public void testAppendDayOfMonth() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendDayOfYear(int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendDayOfYear(int)
	 */
	public void testAppendDayOfYear() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendWeekOfYearWeek(int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendWeekOfYearWeek(int)
	 */
	public void testAppendWeekOfYearWeek() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendWeekOfYearYear(int, int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendWeekOfYearYear(int, int)
	 */
	public void testAppendWeekOfYearYear() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendMonthOfYear(int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendMonthOfYear(int)
	 */
	public void testAppendMonthOfYear() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendYear(int, int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendYear(int, int)
	 */
	public void testAppendYear() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendYearOfEra(int, int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendYearOfEra(int, int)
	 */
	public void testAppendYearOfEra() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendYearOfCentury()</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendYearOfCentury()
	 */
	public void testAppendYearOfCentury() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendCentury(int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendCentury(int)
	 */
	public void testAppendCentury() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendCenturyOfEra(int)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendCenturyOfEra(int)
	 */
	public void testAppendCenturyOfEra() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendAmPmSymbol()</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendAmPmSymbol()
	 */
	public void testAppendAmPmSymbol() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendDayOfWeekSymbol()</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendDayOfWeekSymbol()
	 */
	public void testAppendDayOfWeekSymbol() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendDayOfWeekShortSymbol()</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendDayOfWeekShortSymbol()
	 */
	public void testAppendDayOfWeekShortSymbol() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendMonthOfYearSymbol()</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendMonthOfYearSymbol()
	 */
	public void testAppendMonthOfYearSymbol() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendMonthOfYearShortSymbol()</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendMonthOfYearShortSymbol()
	 */
	public void testAppendMonthOfYearShortSymbol() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendEraSymbol()</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendEraSymbol()
	 */
	public void testAppendEraSymbol() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendTimeZoneSymbol()</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendTimeZoneSymbol()
	 */
	public void testAppendTimeZoneSymbol() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendTimeZoneShortSymbol()</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendTimeZoneShortSymbol()
	 */
	public void testAppendTimeZoneShortSymbol() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendTimeZoneOffset(java.lang.String, java.lang.String, boolean)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendTimeZoneOffset(java.lang.String, java.lang.String, boolean)
	 */
	public void testAppendTimeZoneOffset() {
		fail("TBD");
	}
	/**
	 * Test the <code>appendPattern(java.lang.String)</code> method.
	 * @see org.joda.time.format.DateTimeFormatterBuilder#appendPattern(java.lang.String)
	 */
	public void testAppendPattern() {
		fail("TBD");
	}

}
