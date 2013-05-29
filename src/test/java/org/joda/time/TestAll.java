/*
 *  Copyright 2001-2010 Stephen Colebourne
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


/**
 * Entry point for all tests in this package.
 * 
 * @version $Revision$ $Date$
 * 
 * @author Stephen Colebourne
 */
@RunWith( Suite.class )
@Suite.SuiteClasses( {
        TestChronology.class,
        TestDateTimeFieldType.class,
        TestDurationFieldType.class,

        TestInstant_Constructors.class,
        TestInstant_Basics.class,

        TestDateTime_Constructors.class,
        TestDateTime_Basics.class,
        TestDateTime_Properties.class,

        TestMutableDateTime_Constructors.class,
        TestMutableDateTime_Basics.class,
        TestMutableDateTime_Sets.class,
        TestMutableDateTime_Adds.class,
        TestMutableDateTime_Properties.class,

        TestDateMidnight_Constructors.class,
        TestDateMidnight_Basics.class,
        TestDateMidnight_Properties.class,

        TestDuration_Constructors.class,
        TestDuration_Basics.class,

        TestInterval_Constructors.class,
        TestInterval_Basics.class,

        TestLocalDateTime_Constructors.class,
        TestLocalDateTime_Basics.class,
        TestLocalDateTime_Properties.class,

        TestLocalDate_Constructors.class,
        TestLocalDate_Basics.class,
        TestLocalDate_Properties.class,

        TestLocalTime_Constructors.class,
        TestLocalTime_Basics.class,
        TestLocalTime_Properties.class,

        TestMutableInterval_Constructors.class,
        TestMutableInterval_Basics.class,
        TestMutableInterval_Updates.class,

        TestPeriod_Constructors.class,
        TestPeriod_Basics.class,

        TestMutablePeriod_Constructors.class,
        TestMutablePeriod_Basics.class,
        TestMutablePeriod_Updates.class,

        TestBaseSingleFieldPeriod.class,
        TestYears.class,
        TestMonths.class,
        TestWeeks.class,
        TestDays.class,
        TestHours.class,
        TestMinutes.class,
        TestSeconds.class,

        TestTimeOfDay_Basics.class,
        TestTimeOfDay_Constructors.class,
        TestTimeOfDay_Properties.class,

        TestYearMonthDay_Basics.class,
        TestYearMonthDay_Constructors.class,
        TestYearMonthDay_Properties.class,

        TestYearMonth_Basics.class,
        TestYearMonth_Constructors.class,
        TestYearMonth_Properties.class,

        TestMonthDay_Basics.class,
        TestMonthDay_Constructors.class,
        TestMonthDay_Properties.class,

        TestPartial_Basics.class,
        TestPartial_Constructors.class,
        TestPartial_Properties.class,
        TestPartial_Match.class,

        TestAbstractPartial.class,
        TestBasePartial.class,

        TestDateTimeComparator.class,
        TestDateTimeConstants.class,
        TestDateTimeUtils.class,
        TestDateTimeZone.class,
        TestDateTimeZoneCutover.class,
        TestPeriodType.class,
//        TestParseISO.class,
        TestDurationField.class,

        TestStringConvert.class,
        TestSerialization.class,
        TestIllegalFieldValueException.class
} )

public class TestAll {
}
