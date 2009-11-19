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
package org.joda.time.tz;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

/**
 * Entry point for all tests in this package.
 * 
 * @version $Revision: 1000 $ $Date: 2005-12-23 01:17:52 +0100 (Fri, 23 Dec 2005) $
 * 
 * @author Stephen Colebourne
 */
public class GwtTestAll extends TestSuite {

//    public TestAll(String testName) {
//        super(testName);
//    }

    public static Test suite() {
        GWTTestSuite suite = new GWTTestSuite();
        suite.addTestSuite(TestUTCProvider.class);
        suite.addTestSuite(TestBuilder.class);
//Removed for GWT        suite.addTestSuite(TestCompiler.class);
        suite.addTestSuite(TestFixedDateTimeZone.class);
        return suite;
    }

    public static void main(String args[]) {
        String[] testCaseName = {
            GwtTestAll.class.getName()
        };
        junit.textui.TestRunner.main(testCaseName);
    }

}
