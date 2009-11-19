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
package org.joda.time;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

/**
 * Entry point for all tests in this package.
 * 
 * @version $Revision: 1232 $ $Date: 2007-08-04 19:23:22 +0200 (Sat, 04 Aug 2007) $
 * 
 * @author Stephen Colebourne
 */
public class GwtTestAll extends TestSuite {

//    public TestAll(String testName) {
//        super(testName);
//    }

    public static Test suite() {
        GWTTestSuite suite = new GWTTestSuite();
        suite.addTestSuite(TestChronology.class);
        suite.addTestSuite(TestDateTimeFieldType.class);
        suite.addTestSuite(TestDurationFieldType.class);
        
        suite.addTestSuite(TestInstant_Constructors.class);
        suite.addTestSuite(TestInstant_Basics.class);
        
        suite.addTestSuite(TestDateTime_Constructors.class);
        suite.addTestSuite(TestDateTime_Basics.class);
        suite.addTestSuite(TestDateTime_Properties.class);
        
        suite.addTestSuite(TestMutableDateTime_Constructors.class);
        suite.addTestSuite(TestMutableDateTime_Basics.class);
        suite.addTestSuite(TestMutableDateTime_Sets.class);
        suite.addTestSuite(TestMutableDateTime_Adds.class);
        suite.addTestSuite(TestMutableDateTime_Properties.class);
        
        suite.addTestSuite(TestDateMidnight_Constructors.class);
        suite.addTestSuite(TestDateMidnight_Basics.class);
        suite.addTestSuite(TestDateMidnight_Properties.class);
        
        suite.addTestSuite(TestDuration_Constructors.class);
        suite.addTestSuite(TestDuration_Basics.class);
        
        suite.addTestSuite(TestInterval_Constructors.class);
        suite.addTestSuite(TestInterval_Basics.class);
        
        suite.addTestSuite(TestLocalDateTime_Constructors.class);
        suite.addTestSuite(TestLocalDateTime_Basics.class);
        suite.addTestSuite(TestLocalDateTime_Properties.class);
        
        suite.addTestSuite(TestLocalDate_Constructors.class);
        suite.addTestSuite(TestLocalDate_Basics.class);
        suite.addTestSuite(TestLocalDate_Properties.class);
        
        suite.addTestSuite(TestLocalTime_Constructors.class);
        suite.addTestSuite(TestLocalTime_Basics.class);
        suite.addTestSuite(TestLocalTime_Properties.class);
        
        suite.addTestSuite(TestMutableInterval_Constructors.class);
        suite.addTestSuite(TestMutableInterval_Basics.class);
        suite.addTestSuite(TestMutableInterval_Updates.class);
        
        suite.addTestSuite(TestPeriod_Constructors.class);
        suite.addTestSuite(TestPeriod_Basics.class);
        
        suite.addTestSuite(TestMutablePeriod_Constructors.class);
        suite.addTestSuite(TestMutablePeriod_Basics.class);
        suite.addTestSuite(TestMutablePeriod_Updates.class);
        
        suite.addTestSuite(TestBaseSingleFieldPeriod.class);
        suite.addTestSuite(TestYears.class);
        suite.addTestSuite(TestMonths.class);
        suite.addTestSuite(TestWeeks.class);
        suite.addTestSuite(TestDays.class);
        suite.addTestSuite(TestHours.class);
        suite.addTestSuite(TestMinutes.class);
        suite.addTestSuite(TestSeconds.class);
        
        suite.addTestSuite(TestTimeOfDay_Basics.class);
        suite.addTestSuite(TestTimeOfDay_Constructors.class);
//Removed for GWT        suite.addTestSuite(TestTimeOfDay_Properties.class);
        
        suite.addTestSuite(TestYearMonthDay_Basics.class);
        suite.addTestSuite(TestYearMonthDay_Constructors.class);
        suite.addTestSuite(TestYearMonthDay_Properties.class);
        
        suite.addTestSuite(TestPartial_Basics.class);
        suite.addTestSuite(TestPartial_Constructors.class);
        suite.addTestSuite(TestPartial_Properties.class);
        suite.addTestSuite(TestPartial_Match.class);
        
        suite.addTestSuite(TestAbstractPartial.class);
        suite.addTestSuite(TestBasePartial.class);
        
        suite.addTestSuite(TestDateTimeComparator.class);
        suite.addTestSuite(TestDateTimeConstants.class);
        suite.addTestSuite(TestDateTimeUtils.class);
        suite.addTestSuite(TestDateTimeZone.class);
        suite.addTestSuite(TestDateTimeZoneCutover.class);
        suite.addTestSuite(TestPeriodType.class);
//        suite.addTestSuite(TestParseISO.class);
        suite.addTestSuite(TestDurationField.class);
        
//Removed for GWT        suite.addTestSuite(TestSerialization.class);
        suite.addTestSuite(TestIllegalFieldValueException.class);
        return suite;
    }

    public static void main(String args[]) {
        String[] testCaseName = {
            GwtTestAll.class.getName()
        };
        junit.textui.TestRunner.main(testCaseName);
    }

}
