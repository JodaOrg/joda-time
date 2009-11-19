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
package org.joda.time.convert;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

/**
 * Entry point for all tests in this package.
 * 
 * @version $Revision: 662 $ $Date: 2005-02-08 00:51:26 +0100 (Tue, 08 Feb 2005) $
 * 
 * @author Stephen Colebourne
 */
public class GwtTestAll extends TestSuite {

//    public TestAllGwt(String testName) {
//        super(testName);
//    }

    public static Test suite() {
        GWTTestSuite suite = new GWTTestSuite();
        suite.addTestSuite(TestConverterManager.class);
        suite.addTestSuite(TestConverterSet.class);
        
        suite.addTestSuite(TestCalendarConverter.class);
        suite.addTestSuite(TestDateConverter.class);
        suite.addTestSuite(TestLongConverter.class);
        suite.addTestSuite(TestNullConverter.class);
        suite.addTestSuite(TestReadableDurationConverter.class);
        suite.addTestSuite(TestReadableIntervalConverter.class);
        suite.addTestSuite(TestReadableInstantConverter.class);
        suite.addTestSuite(TestReadablePartialConverter.class);
        suite.addTestSuite(TestReadablePeriodConverter.class);
        suite.addTestSuite(TestStringConverter.class);
        return suite;
    }

    public static void main(String args[]) {
        String[] testCaseName = {
            GwtTestAll.class.getName()
        };
        junit.textui.TestRunner.main(testCaseName);
    }

}
