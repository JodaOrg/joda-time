/*
 *  Copyright 2001-2014 Stephen Colebourne
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
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Entry point for all tests in this package.
 * 
 * @version $Revision$ $Date$
 * 
 * @author Stephen Colebourne
 */
public class TestAll extends TestCase {

    public TestAll(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        
        suite.addTest(TestDateTimeFormatter.suite());
        suite.addTest(TestDateTimeFormat.suite());
        suite.addTest(TestDateTimeFormatStyle.suite());
        suite.addTest(TestDateTimeParserBucket.suite());
        suite.addTest(TestISODateTimeFormat.suite());
        suite.addTest(TestISODateTimeFormat_Fields.suite());
        suite.addTest(TestISODateTimeFormatParsing.suite());
        suite.addTest(TestDateTimeFormatterBuilder.suite());
        
        suite.addTest(TestPeriodFormatter.suite());
        suite.addTest(TestPeriodFormat.suite());
        suite.addTest(TestISOPeriodFormat.suite());
        suite.addTest(TestISOPeriodFormatParsing.suite());
        suite.addTest(TestPeriodFormatParsing.suite());
        suite.addTest(TestPeriodFormatterBuilder.suite());

        suite.addTest(TestTextFields.suite());

        return suite;
    }

    public static void main(String args[]) {
        String[] testCaseName = {
            TestAll.class.getName()
        };
        junit.textui.TestRunner.main(testCaseName);
    }

}
