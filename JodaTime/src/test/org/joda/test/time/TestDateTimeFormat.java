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
     * Test the <code>getInstanceUTC()</code> method.
     * @see org.joda.time.format.DateTimeFormat#getInstanceUTC()
     */
    protected void testGetInstanceUTC() {
        fail("TBD");
    }
    /**
     * Test the <code>getInstance()</code> method.
     * @see org.joda.time.format.DateTimeFormat#getInstance()
     */
    protected void testGetInstance() {
        fail("TBD");
    }
    /**
     * Test the <code>getInstance(org.joda.time.DateTimeZone)</code> method.
     * @see org.joda.time.format.DateTimeFormat#getInstance(org.joda.time.DateTimeZone)
     */
    public void testGetInstanceTZ() {
        fail("TBD");
    }
    /**
     * Test the <code>getInstance(org.joda.time.DateTimeZone, java.util.Locale)</code> method.
     * @see org.joda.time.format.DateTimeFormat#getInstance(org.joda.time.DateTimeZone, java.util.Locale)
     */
    public void testGetInstanceTZL() {
        fail("TBD");
    }
    /**
     * Test the <code>getInstance(org.joda.time.Chronology)</code> method.
     * @see org.joda.time.format.DateTimeFormat#getInstance(org.joda.time.Chronology)
     */
    public void testGetInstanceC() {
        fail("TBD");
    }
    /**
     * Test the <code>getInstance(org.joda.time.Chronology, java.util.Locale)</code> method.
     * @see org.joda.time.format.DateTimeFormat#getInstance(org.joda.time.Chronology, java.util.Locale)
     */
    public void testGetInstanceCL() {
        fail("TBD");
    }
    /**
     * Test the <code>forPattern(String)</code> method.
     * @see org.joda.time.format.DateTimeFormat#forPattern(String)
     */
    public void testForPattern() {
        fail("TBD");
    }
    /**
     * Test the <code>forStyle(String)</code> method.
     * @see org.joda.time.format.DateTimeFormat#forStyle(String)
     */
    public void testForStyle() {
        fail("TBD");
    }
    /**
     * Test the <code>getPatternForStyle()</code> method.
     * @see org.joda.time.format.DateTimeFormat#getPatternForStyle(String)
     */
    public void testGetPatternForStyle() {
        fail("TBD");
    }

}
