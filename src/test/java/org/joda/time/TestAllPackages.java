/*
 *  Copyright 2001-2011 Stephen Colebourne
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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.Locale;
import java.util.TimeZone;


/**
 * Entry point for all tests in Joda Time.
 * 
 * @version $Revision$ $Date$
 * 
 * @author Stephen Colebourne
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( {
        org.joda.time.TestAll.class,
        org.joda.time.chrono.TestAll.class,
        org.joda.time.chrono.gj.TestAll.class,
        org.joda.time.convert.TestAll.class,
        org.joda.time.field.TestAll.class,
        org.joda.time.format.TestAll.class,
        org.joda.time.tz.TestAll.class
}
)
public class TestAllPackages  {

    //TODO: need this?
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
