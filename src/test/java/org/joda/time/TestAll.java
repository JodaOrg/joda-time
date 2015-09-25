/*
 *  Copyright 2001-2015 Stephen Colebourne
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
        suite.addTest(TestChronology.suite());
        suite.addTest(TestDateTimeFieldType.suite());
        suite.addTest(TestDurationFieldType.suite());
        
        suite.addTest(TestInstant_Constructors.suite());
        suite.addTest(TestInstant_Basics.suite());
        
        suite.addTest(TestDateTime_Constructors.suite());
        suite.addTest(TestDateTime_Basics.suite());
        suite.addTest(TestDateTime_Properties.suite());
        
        suite.addTest(TestMutableDateTime_Constructors.suite());
        suite.addTest(TestMutableDateTime_Basics.suite());
        suite.addTest(TestMutableDateTime_Sets.suite());
        suite.addTest(TestMutableDateTime_Adds.suite());
        suite.addTest(TestMutableDateTime_Properties.suite());
        
        suite.addTest(TestDateMidnight_Constructors.suite());
        suite.addTest(TestDateMidnight_Basics.suite());
        suite.addTest(TestDateMidnight_Properties.suite());
        
        suite.addTest(TestDuration_Constructors.suite());
        suite.addTest(TestDuration_Basics.suite());
        
        suite.addTest(TestInterval_Constructors.suite());
        suite.addTest(TestInterval_Basics.suite());
        
        suite.addTest(TestLocalDateTime_Constructors.suite());
        suite.addTest(TestLocalDateTime_Basics.suite());
        suite.addTest(TestLocalDateTime_Properties.suite());
        
        suite.addTest(TestLocalDate_Constructors.suite());
        suite.addTest(TestLocalDate_Basics.suite());
        suite.addTest(TestLocalDate_Properties.suite());
        
        suite.addTest(TestLocalTime_Constructors.suite());
        suite.addTest(TestLocalTime_Basics.suite());
        suite.addTest(TestLocalTime_Properties.suite());
        
        suite.addTest(TestMutableInterval_Constructors.suite());
        suite.addTest(TestMutableInterval_Basics.suite());
        suite.addTest(TestMutableInterval_Updates.suite());
        
        suite.addTest(TestPeriod_Constructors.suite());
        suite.addTest(TestPeriod_Basics.suite());
        
        suite.addTest(TestMutablePeriod_Constructors.suite());
        suite.addTest(TestMutablePeriod_Basics.suite());
        suite.addTest(TestMutablePeriod_Updates.suite());
        
        suite.addTest(TestBaseSingleFieldPeriod.suite());
        suite.addTest(TestYears.suite());
        suite.addTest(TestMonths.suite());
        suite.addTest(TestWeeks.suite());
        suite.addTest(TestDays.suite());
        suite.addTest(TestHours.suite());
        suite.addTest(TestMinutes.suite());
        suite.addTest(TestSeconds.suite());
        
        suite.addTest(TestTimeOfDay_Basics.suite());
        suite.addTest(TestTimeOfDay_Constructors.suite());
        suite.addTest(TestTimeOfDay_Properties.suite());
        
        suite.addTest(TestYearMonthDay_Basics.suite());
        suite.addTest(TestYearMonthDay_Constructors.suite());
        suite.addTest(TestYearMonthDay_Properties.suite());
        
        suite.addTest(TestYearMonth_Basics.suite());
        suite.addTest(TestYearMonth_Constructors.suite());
        suite.addTest(TestYearMonth_Properties.suite());
        
        suite.addTest(TestMonthDay_Basics.suite());
        suite.addTest(TestMonthDay_Constructors.suite());
        suite.addTest(TestMonthDay_Properties.suite());
        
        suite.addTest(TestPartial_Basics.suite());
        suite.addTest(TestPartial_Constructors.suite());
        suite.addTest(TestPartial_Properties.suite());
        suite.addTest(TestPartial_Match.suite());
        
        suite.addTest(TestAbstractPartial.suite());
        suite.addTest(TestBasePartial.suite());
        
        suite.addTest(TestDateTimeComparator.suite());
        suite.addTest(TestDateTimeConstants.suite());
        suite.addTest(TestDateTimeUtils.suite());
        suite.addTest(TestDateTimeZone.suite());
        suite.addTest(TestDateTimeZoneCutover.suite());
        suite.addTest(TestPeriodType.suite());
//        suite.addTest(TestParseISO.suite());
        suite.addTest(TestDurationField.suite());
        
        suite.addTest(TestStringConvert.suite());
        suite.addTest(TestSerialization.suite());
        suite.addTest(TestIllegalFieldValueException.suite());
        suite.addTest(TestMinMaxLong.suite());
        return suite;
    }

    public static void main(String args[]) {
        String[] testCaseName = {
            TestAll.class.getName()
        };
        junit.textui.TestRunner.main(testCaseName);
    }

}
