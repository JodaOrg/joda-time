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
 * GJDateTimeFormat date time class.
 *
 * @author Stephen Colebourne
 * @author Guy Allard
 */
public class TestDateTimeFormat extends BulkTest {

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
		return BulkTest.makeSuite(TestDateTimeFormat.class);
	}
	/**
	 * TestDateTimeField constructor.
	 * @param name
	 */
	public TestDateTimeFormat(String name) {
		super(name);
	}
	// Class Name: org.joda.time.format.DateTimeFormat
	/**
	 * Junit <code>setUp()</code> method.
	 */
	public void setUp() /* throws Exception */ {
		// super.setUp();
	}
	/**
	 * Junit <code>tearDown()</code> method.
	 */
	protected void tearDown() /* throws Exception */ {
		// super.tearDown();
	}
	/**
	 * Test the <code>getInstance()</code> method.
	 * @see org.joda.time.format.DateTimeFormat#getInstance()
	 */
	protected void testGetInstance() {
		fail("TBD");
	}
	/**
	 * Test the <code>getInstance(java.lang.String)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#getInstance(java.lang.String)
	 */
	public void testGetInstanceString() {
		fail("TBD");
	}
	/**
	 * Test the <code>getInstance(java.lang.String, java.util.TimeZone)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#getInstance(java.lang.String, java.util.TimeZone)
	 */
	public void testGetInstanceSTZ() {
		fail("TBD");
	}
	/**
	 * Test the <code>getInstance(java.lang.String, java.util.TimeZone, java.util.Locale)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#getInstance(java.lang.String, java.util.TimeZone, java.util.Locale)
	 */
	public void testGetInstanceSTZL() {
		fail("TBD");
	}
	/**
	 * Test the <code>getInstance(java.lang.String, org.joda.time.Chronology)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#getInstance(java.lang.String, org.joda.time.Chronology)
	 */
	public void testGetInstanceSC() {
		fail("TBD");
	}
	/**
	 * Test the <code>getInstance(java.lang.String, org.joda.time.Chronology, java.util.TimeZone)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#getInstance(java.lang.String, org.joda.time.Chronology, java.util.TimeZone)
	 */
	public void testGetInstanceSCT() {
		fail("TBD");
	}
	/**
	 * Test the <code>getInstance(java.lang.String, org.joda.time.Chronology, java.util.TimeZone, java.util.Locale)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#getInstance(java.lang.String, org.joda.time.Chronology, java.util.TimeZone, java.util.Locale)
	 */
	public void testGetInstanceSCTL() {
		fail("TBD");
	}
	/**
	 * Test the <code>getDateInstance(java.lang.Object, org.joda.time.Chronology, java.util.TimeZone, java.util.Locale)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#getDateInstance(java.lang.Object, org.joda.time.Chronology, java.util.TimeZone, java.util.Locale)
	 */
	public void testGetDateInstance() {
		fail("TBD");
	}
	/**
	 * Test the <code>getTimeInstance(java.lang.Object, org.joda.time.Chronology, java.util.TimeZone, java.util.Locale)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#getTimeInstance(java.lang.Object, org.joda.time.Chronology, java.util.TimeZone, java.util.Locale)
	 */
	public void testGetTimeInstance() {
		fail("TBD");
	}
	/**
	 * Test the <code>getDateTimeInstance(java.lang.Object, java.lang.Object, org.joda.time.Chronology, java.util.TimeZone, java.util.Locale)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#getDateTimeInstance(java.lang.Object, java.lang.Object, org.joda.time.Chronology, java.util.TimeZone, java.util.Locale)
	 */
	public void testGetDateTimeInstance() {
		fail("TBD");
	}
	/**
	 * Test the <code>getPattern()</code> method.
	 * @see org.joda.time.format.DateTimeFormat#getPattern()
	 */
	public void testGetPattern() {
		fail("TBD");
	}
	/**
	 * Test the <code>getChronology()</code> method.
	 * @see org.joda.time.format.DateTimeFormat#getChronology()
	 */
	public void testGetChronology() {
		fail("TBD");
	}
	/**
	 * Test the <code>getTimeZone()</code> method.
	 * @see org.joda.time.format.DateTimeFormat#getTimeZone()
	 */
	public void testGetTimeZone() {
		fail("TBD");
	}
	/**
	 * Test the <code>getLocale()</code> method.
	 * @see org.joda.time.format.DateTimeFormat#getLocale()
	 */
	public void testGetLocale() {
		fail("TBD");
	}
	/**
	 * Test the <code>getPrinter()</code> method.
	 * @see org.joda.time.format.DateTimeFormat#getPrinter()
	 */
	public void testGetPrinter() {
		fail("TBD");
	}
	/**
	 * Test the <code>getParser()</code> method.
	 * @see org.joda.time.format.DateTimeFormat#getParser()
	 */
	public void testGetParser() {
		fail("TBD");
	}
	/**
	 * Test the <code>estimatePrintedLength()</code> method.
	 * @see org.joda.time.format.DateTimeFormat#estimatePrintedLength()
	 */
	public void testEstimatePrintedLength() {
		fail("TBD");
	}
	/**
	 * Test the <code>printTo(java.lang.StringBuffer, long, long)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#printTo(java.lang.StringBuffer, long, long)
	 */
	public void testPrintToSLL() {
		fail("TBD");
	}
	/**
	 * Test the <code>printTo(java.io.Writer, long, long)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#printTo(java.io.Writer, long, long)
	 */
	public void testPrintToWLL() {
		fail("TBD");
	}
	/**
	 * Test the <code>printTo(java.lang.StringBuffer, org.joda.time.ReadableInstant)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#printTo(java.lang.StringBuffer, org.joda.time.ReadableInstant)
	 */
	public void testPrintToSBRI() {
		fail("TBD");
	}
	/**
	 * Test the <code>printTo(java.lang.StringBuffer, long)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#printTo(java.lang.StringBuffer, long)
	 */
	public void testPrintToSBL() {
		fail("TBD");
	}
	/**
	 * Test the <code>printTo(java.io.Writer, org.joda.time.ReadableInstant)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#printTo(java.io.Writer, org.joda.time.ReadableInstant)
	 */
	public void testPrintToWRI() {
		fail("TBD");
	}
	/**
	 * Test the <code>printTo(java.io.Writer, long)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#printTo(java.io.Writer, long)
	 */
	public void testPrintToWL() {
		fail("TBD");
	}
	/**
	 * Test the <code>print(org.joda.time.ReadableInstant)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#print(org.joda.time.ReadableInstant)
	 */
	public void testPrintRI() {
		fail("TBD");
	}
	/**
	 * Test the <code>print(long)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#print(long)
	 */
	public void testPrintL() {
		fail("TBD");
	}
	/**
	 * Test the <code>estimateParsedLength()</code> method.
	 * @see org.joda.time.format.DateTimeFormat#estimateParsedLength()
	 */
	public void testEstimateParsedLength() {
		fail("TBD");
	}
	/**
	 * Test the <code>parseInto(org.joda.time.format.DateTimeParserBucket, java.lang.String, int)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#parseInto(org.joda.time.format.DateTimeParserBucket, java.lang.String, int)
	 */
	public void testParseIntoPBSI() {
		fail("TBD");
	}
	/**
	 * Test the <code>parseInto(org.joda.time.ReadWritableInstant, java.lang.String)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#parseInto(org.joda.time.ReadWritableInstant, java.lang.String)
	 */
	public void testParseIntoRWIS() {
		fail("TBD");
	}
	/**
	 * Test the <code>parseInto(org.joda.time.ReadWritableInstant, java.lang.String, int)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#parseInto(org.joda.time.ReadWritableInstant, java.lang.String, int)
	 */
	public void testParseIntoRWISI() {
		fail("TBD");
	}
	/**
	 * Test the <code>parse(java.lang.String)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#parse(java.lang.String)
	 */
	public void testParseS() {
		fail("TBD");
	}
	/**
	 * Test the <code>parse(java.lang.String, int, long)</code> method.
	 * @see org.joda.time.format.DateTimeFormat#parse(java.lang.String, int, long)
	 */
	public void testParseSL() {
		fail("TBD");
	}

}
