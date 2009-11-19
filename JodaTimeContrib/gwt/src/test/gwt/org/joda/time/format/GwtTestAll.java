/*
 *  Copyright 2001-2006 Stephen Colebourne
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
package org.joda.time.format;

import junit.framework.Test;

import com.google.gwt.junit.tools.GWTTestSuite;

import junit.framework.TestSuite;

/**
 * Entry point for all tests in this package.
 * 
 * @version $Revision: 1091 $ $Date: 2006-06-17 14:46:03 +0200 (Sat, 17 Jun 2006) $
 * 
 * @author Stephen Colebourne
 */
public class GwtTestAll extends TestSuite {

//    public TestAll(String testName) {
//        super(testName);
//    }

    public static Test suite() {
        GWTTestSuite suite = new GWTTestSuite();
        
        suite.addTestSuite(TestDateTimeFormatter.class);
        suite.addTestSuite(TestDateTimeFormat.class);
//Removed for GWT        suite.addTestSuite(TestDateTimeFormatStyle.class);
        suite.addTestSuite(TestISODateTimeFormat.class);
        suite.addTestSuite(TestISODateTimeFormat_Fields.class);
        suite.addTestSuite(TestISODateTimeFormatParsing.class);
        suite.addTestSuite(TestDateTimeFormatterBuilder.class);
        
        suite.addTestSuite(TestPeriodFormatter.class);
        suite.addTestSuite(TestPeriodFormat.class);
        suite.addTestSuite(TestISOPeriodFormat.class);
        suite.addTestSuite(TestISOPeriodFormatParsing.class);
        suite.addTestSuite(TestPeriodFormatParsing.class);
        suite.addTestSuite(TestPeriodFormatterBuilder.class);

        suite.addTestSuite(TestTextFields.class);

        return suite;
    }

    public static void main(String args[]) {
        String[] testCaseName = {
            GwtTestAll.class.getName()
        };
        junit.textui.TestRunner.main(testCaseName);
    }

}
