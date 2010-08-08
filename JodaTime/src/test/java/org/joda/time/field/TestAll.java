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
package org.joda.time.field;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Entry point for all tests in this package.
 * 
 * @version $Id$
 * 
 * @author Stephen Colebourne
 */
public class TestAll extends TestCase {

    public TestAll(String testName) {
        super(testName);
    }

    public static void main(String args[]) {
        String[] testCaseName = {
            TestAll.class.getName()
        };
        junit.textui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        
        suite.addTest(TestFieldUtils.suite());

        suite.addTest(TestBaseDateTimeField.suite());
        suite.addTest(TestOffsetDateTimeField.suite());
        suite.addTest(TestPreciseDurationDateTimeField.suite());
        suite.addTest(TestPreciseDateTimeField.suite());
        
        suite.addTest(TestMillisDurationField.suite());
        suite.addTest(TestPreciseDurationField.suite());
        suite.addTest(TestScaledDurationField.suite());
        suite.addTest(TestUnsupportedDateTimeField.suite());
        return suite;
    }

}
