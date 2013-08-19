/*
 *  Copyright 2001-2013 Stephen Colebourne
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
package org.joda.time;

import java.util.Locale;
import java.util.TimeZone;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Entry point for all tests in Joda-Time.
 */
public class TestAllPackages extends TestCase {

    public TestAllPackages(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(org.joda.time.TestAll.suite());
        suite.addTest(org.joda.time.chrono.TestAll.suite());
        suite.addTest(org.joda.time.chrono.gj.TestAll.suite());
        suite.addTest(org.joda.time.convert.TestAll.suite());
        suite.addTest(org.joda.time.field.TestAll.suite());
        suite.addTest(org.joda.time.format.TestAll.suite());
        suite.addTest(org.joda.time.tz.TestAll.suite());
        return suite;
    }

    public static void main(String args[]) {
        // setup a time zone other than one tester is in
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        
        // setup a locale other than one the tester is in
        Locale.setDefault(new Locale("th", "TH"));
        
        // run tests
        String[] testCaseName = {
            TestAllPackages.class.getName()
        };
        junit.textui.TestRunner.main(testCaseName);
    }

}
