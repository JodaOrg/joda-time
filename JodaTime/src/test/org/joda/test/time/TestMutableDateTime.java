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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.util.*;
import java.text.* ;
import java.io.*;

import junit.framework.TestSuite;

import org.joda.time.MutableDateTime;
import org.joda.time.chrono.ISOChronology;
/**
 * This class is a Junit unit test for the
 * org.joda.time.MutableDateTime class.
 *
 * @author Stephen Colebourne
 * @author Guy Allard
 */
public class TestMutableDateTime
    extends BulkTest {

    private static Class cls = MutableDateTime.class;

    /**
     * main
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    /**
     * TestSuite
     */
    public static TestSuite suite() {
        return BulkTest.makeSuite(TestMutableDateTime.class);
    }
    /**
     * TestMutableDateTime constructor.  A constructor with this signature
     * is required for Junit testing.
     * @param name The human readable name of the class.
     */
    public TestMutableDateTime(String name) {
        super(name);
    }

    /**
     * Junit <code>setUp()</code> method.
     */
    protected void setUp() /* throws Exception */ {
        // TimeZone.setDefault( new SimpleTimeZone(0, "UTC") );
    }
    
    /**
     * Junit <code>tearDown()</code> method.
     */
    protected void tearDown() /* throws Exception */ {
        // super.tearDown();
    }

    //
    // No CTOR test methods are provided here!
    // All MutableDateTime CTORs map directly to AbstractDateTime
    // CTORS, and all each MDT CTOR does is call super.
    //
    // New CTOR tests are required only for:
    // 1) CTORs which do *NOT* call super(...) at all.
    // 1) CTORs which do *NOT* call super(...) with a matching signature.
    // 2) CTORs with differing method signatures (new at this level).
    //
    // Add Other BulkTests
    //
    public BulkTest bulkTestGet() {
        return new TestMDTGet("Mutable Date Time get Method Tests");
    }

    public BulkTest bulkTestSet() {
        return new TestMDTSet("Mutable Date Time set Method Tests");
    }

} // end of class TestMutableDateTime
