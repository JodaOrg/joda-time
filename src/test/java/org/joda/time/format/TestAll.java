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

        TestDateTimeFormatter.class,
        TestDateTimeFormat.class,
        TestDateTimeFormatStyle.class,
        TestISODateTimeFormat.class,
        TestISODateTimeFormat_Fields.class,
        TestISODateTimeFormatParsing.class,
        TestDateTimeFormatterBuilder.class,

        TestPeriodFormatter.class,
        TestPeriodFormat.class,
        TestISOPeriodFormat.class,
        TestISOPeriodFormatParsing.class,
        TestPeriodFormatParsing.class,
        TestPeriodFormatterBuilder.class,

        TestTextFields.class
} )
public class TestAll {

}
