/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2004 Stephen Colebourne.  
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

import junit.framework.TestSuite;

import org.joda.time.DateTimeZone;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.ISOChronology;
/**
 * This class is a Junit unit test for the CTORs of the
 * MutableDateTime date time class.  The MutableDateTime class is passed
 * to the base class, which actually invokes the proper
 * constructors.
 *
 * @author Stephen Colebourne
 * @author Guy Allard
 */
public class TestMutableDateTimeCtors
       extends AbstractTestDateTimeCommon {

    // The class to be tested.
    private static Class cls = MutableDateTime.class;

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
        return BulkTest.makeSuite(TestMutableDateTimeCtors.class);
    }
    /**
     * Constructor.
     * @param name
     */
    public TestMutableDateTimeCtors(String name) {
        super(name, cls);
    }
    /**
     * Constructor
     * @param name The class name.
     * @param cls The class to be tested.
     */
    public TestMutableDateTimeCtors(String name, Class cls) {
        super(name, cls);
        TestMutableDateTimeCtors.cls = cls;
    }
    /**
     * Junit <code>setUp()</code> method.
     */
    protected void setUp() /* throws Exception */ {
        super.setUp();
    }

    /**
     * Junit <code>tearDown()</code> method.
     */
    protected void tearDown() /* throws Exception */ {
        super.tearDown();
    }
    
    protected ReadableInstant createSmall(boolean ofAnotherClass) {
        if (ofAnotherClass) {
            return new DateTime(-1L * 1000, GJChronology.getInstance());
        }
        return new MutableDateTime(-1L * 1000);
    }
    
    protected ReadableInstant createMid(boolean ofAnotherClass) {
        if (ofAnotherClass) {
            return new DateTime(2L * 1000, GJChronology.getInstance());
        }
        return new MutableDateTime((2L * 1000));
    }
    
    protected ReadableInstant createLarge(boolean ofAnotherClass) {
        if (ofAnotherClass) {
            return new DateTime(3L * 1000, GJChronology.getInstance());
        }
        return new MutableDateTime((3L * 1000));
    }

    protected ReadableInstant createUTC(long millis) {
        return new MutableDateTime(millis, ISOChronology.getInstance(DateTimeZone.UTC));
    }

}
