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
 * GJTimePeriod date time class.
 *
 * @author Stephen Colebourne
 * @author Guy Allard
 */
public class TestTimePeriod extends BulkTest {

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
		return BulkTest.makeSuite(TestTimePeriod.class);
	}
	/**
	 * TestDateTimeField constructor.
	 * @param name
	 */
	public TestTimePeriod(String name) {
		super(name);
	}
	// Class Name: org.joda.time.TimePeriod
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
	 * Test the <code>clone()</code> method.
	 * @see org.joda.time.TimePeriod#clone()
	 */
	public void testClone() {
		fail("TBD");
	}
	/**
	 * Test the <code>format(java.text.Format)</code> method.
	 * @see org.joda.time.TimePeriod#format(java.text.Format)
	 */
	public void testFormat() {
		fail("TBD");
	}
	/**
	 * Test the <code>getMillis()</code> method.
	 * @see org.joda.time.TimePeriod#getMillis()
	 */
	public void testGetMillis() {
		fail("TBD");
	}
	/**
	 * Test the <code>getStartInstant()</code> method.
	 * @see org.joda.time.TimePeriod#getStartInstant()
	 */
	public void testGetStartInstant() {
		fail("TBD");
	}
	/**
	 * Test the <code>getEndInstant()</code> method.
	 * @see org.joda.time.TimePeriod#getEndInstant()
	 */
	public void testGetEndInstant() {
		fail("TBD");
	}
	/**
	 * Test the <code>toTimePeriod()</code> method.
	 * @see org.joda.time.TimePeriod#toTimePeriod()
	 */
	public void testToTimePeriod() {
		fail("TBD");
	}
	/**
	 * Test the <code>getTotalSeconds()</code> method.
	 * @see org.joda.time.TimePeriod#getTotalSeconds()
	 */
	public void testGetTotalSeconds() {
		fail("TBD");
	}
	/**
	 * Test the <code>getTotalMinutes()</code> method.
	 * @see org.joda.time.TimePeriod#getTotalMinutes()
	 */
	public void testGetTotalMinutes() {
		fail("TBD");
	}
	/**
	 * Test the <code>getTotalHours()</code> method.
	 * @see org.joda.time.TimePeriod#getTotalHours()
	 */
	public void testGetTotalHours() {
		fail("TBD");
	}
	/**
	 * Test the <code>getTotalDays()</code> method.
	 * @see org.joda.time.TimePeriod#getTotalDays()
	 */
	public void testGetTotalDays() {
		fail("TBD");
	}
	/**
	 * Test the <code>getTotalMonths()</code> method.
	 * @see org.joda.time.TimePeriod#getTotalMonths()
	 */
	public void testGetTotalMonths() {
		fail("TBD");
	}
	/**
	 * Test the <code>getTotalWeeks()</code> method.
	 * @see org.joda.time.TimePeriod#getTotalWeeks()
	 */
	public void testGetTotalWeeks() {
		fail("TBD");
	}
	/**
	 * Test the <code>getSeconds()</code> method.
	 * @see org.joda.time.TimePeriod#getSeconds()
	 */
	public void testGetSeconds() {
		fail("TBD");
	}
	/**
	 * Test the <code>getMinutes()</code> method.
	 * @see org.joda.time.TimePeriod#getMinutes()
	 */
	public void testGetMinutes() {
		fail("TBD");
	}
	/**
	 * Test the <code>getHours()</code> method.
	 * @see org.joda.time.TimePeriod#getHours()
	 */
	public void testGetHours() {
		fail("TBD");
	}
	/**
	 * Test the <code>getDays()</code> method.
	 * @see org.joda.time.TimePeriod#getDays()
	 */
	public void testGetDays() {
		fail("TBD");
	}
	/**
	 * Test the <code>getMonths()</code> method.
	 * @see org.joda.time.TimePeriod#getMonths()
	 */
	public void testGetMonths() {
		fail("TBD");
	}
	/**
	 * Test the <code>getYears()</code> method.
	 * @see org.joda.time.TimePeriod#getYears()
	 */
	public void testGetYears() {
		fail("TBD");
	}
	/**
	 * Test the <code>equals(java.lang.Object)</code> method.
	 * @see org.joda.time.TimePeriod#equals(java.lang.Object)
	 */
	public void testEquals() {
		fail("TBD");
	}
	/**
	 * Test the <code>hashCode()</code> method.
	 * @see org.joda.time.TimePeriod#hashCode()
	 */
	public void testHashCode() {
		fail("TBD");
	}
	/**
	 * Test the <code>compareTo(java.lang.Object)</code> method.
	 * @see org.joda.time.TimePeriod#compareTo(java.lang.Object)
	 */
	public void testCompareTo() {
		fail("TBD");
	}
	/**
	 * Test the <code>isLongerThan(java.lang.Object)</code> method.
	 * @see org.joda.time.TimePeriod#isLongerThan(java.lang.Object)
	 */
	public void testIsLongerThan() {
		fail("TBD");
	}
	/**
	 * Test the <code>isShorterThan(java.lang.Object)</code> method.
	 * @see org.joda.time.TimePeriod#isShorterThan(java.lang.Object)
	 */
	public void testIsShorterThan() {
		fail("TBD");
	}
	/**
	 * Test the <code>toString()</code> method.
	 * @see org.joda.time.TimePeriod#toString()
	 */
	public void testToString() {
		fail("TBD");
	}

}
