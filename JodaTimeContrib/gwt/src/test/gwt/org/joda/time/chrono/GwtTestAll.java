/*
 *  Copyright 2001-2005 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.chrono;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

import junit.textui.TestRunner;

/**
 * Entry point for all tests in this package.
 * 
 * @version $Revision: 1217 $ $Date: 2007-05-08 05:49:47 +0200 (Tue, 08 May 2007) $
 * 
 * @author Stephen Colebourne
 */
public class GwtTestAll extends TestSuite {

    public static boolean FAST = false;

//    public TestAll(String testName) {
//        super(testName);
//    }

    public static Test suite() {
        GWTTestSuite suite = new GWTTestSuite();
        
        suite.addTestSuite(TestBuddhistChronology.class);
        suite.addTestSuite(TestCopticChronology.class);
        suite.addTestSuite(TestEthiopicChronology.class);
        suite.addTestSuite(TestGJChronology.class);
        suite.addTestSuite(TestGregorianChronology.class);
        suite.addTestSuite(TestIslamicChronology.class);
        suite.addTestSuite(TestJulianChronology.class);
        suite.addTestSuite(TestISOChronology.class);
        suite.addTestSuite(TestLenientChronology.class);
        
        return suite;
    }

    public static void main(String args[]) {
        FAST = false;
        TestRunner.run(GwtTestAll.class);
    }

}
