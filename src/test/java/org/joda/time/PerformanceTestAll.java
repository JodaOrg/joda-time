package org.joda.time;


import org.joda.time.convert.TestPerformance_DateConverter;
import org.joda.time.field.TestPerformanceMillisDurationField;
import org.joda.time.field.TestPerformance_FieldUtils;
import org.joda.time.format.TestPerformance_PeriodFormat;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class PerformanceTestAll extends TestCase {

    public PerformanceTestAll (String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(TestPerformanceDate1.suite());
        suite.addTest(TestReference_Pallavi.suite());
        suite.addTest(TestPerformanceDateOne.suite());
        
        suite.addTest(TestPerformance_DateConverter.suite());
        suite.addTest(TestPerformanceMillisDurationField.suite());
        
        suite.addTest(TestPerformance_FieldUtils.suite());
        suite.addTest(TestPerformance_PeriodFormat.suite());


        return suite;
    }

    public static void main(String args[]) {
        String[] testCaseName = {
            PerformanceTestAll.class.getName()
        };
        junit.textui.TestRunner.main(testCaseName);
    }

}

