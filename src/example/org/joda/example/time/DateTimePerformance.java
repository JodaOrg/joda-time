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
package org.joda.example.time;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.chrono.GJChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * DateTimePerformance provides various comparisons between the Java supplied
 * Date classes and the Joda ones.
 *
 * @author Stephen Colebourne
 */
public class DateTimePerformance {
    
    private static class Result {
        String object = null;
        String name = null;
        long time = 0;
        long avg = 0;
        int runs = 0;
    }
    
    private static int AVERAGE = 3;
    private static int COUNT_VERY_FAST = 5000000;
    private static int COUNT_FAST = 200000;
    private static int COUNT_SLOW = 20000;
    
    private Map results = new HashMap();
    private List resultList = new ArrayList();
    
    private Result result = null;
    private long start = 0;
    private long end = 0;
    
    /**
     * Constructor
     */
    public static void main(String[] args) {
        try {
            new DateTimePerformance();
            
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
    
    /**
     * Constructor
     */
    public DateTimePerformance() throws Exception {
        checkJodaConstructor1();
        checkJISOConstructor1();
        checkGCalConstructor1();
        checkDateConstructor1();
        
        checkJodaConstructor2();
        checkJISOConstructor2();
        checkGCalConstructor2();
        checkDateConstructor2();
        
        checkJodaConstructor3();
        checkJISOConstructor3();
        checkGCalConstructor3();
        checkDateConstructor3();
        
        checkJodaGetYear();
        checkJISOGetYear();
        checkGCalGetYear();
        checkDateGetYear();
        
//        checkJodaGetMonth();
//        checkJISOGetMonth();
//        checkGCalGetMonth();
//        checkDateGetMonth();
        
//        checkJodaGetDay();
//        checkJISOGetDay();
//        checkGCalGetDay();
//        checkDateGetDay();
        
        checkJodaGetHour();
        checkJISOGetHour();
        checkGCalGetHour();
        checkDateGetHour();
        
        checkJodaSetYear();
        checkJISOSetYear();
        checkGCalSetYear();
        checkDateSetYear();
        
        checkJodaSetGetYear();
        checkJISOSetGetYear();
        checkGCalSetGetYear();
        checkDateSetGetYear();
        
        checkJodaSetHour();
        checkJISOSetHour();
        checkGCalSetHour();
        checkDateSetHour();
        
        checkJodaSetGetHour();
        checkJISOSetGetHour();
        checkGCalSetGetHour();
        checkDateSetGetHour();
        
        checkJodaToString();
        checkJISOToString();
        checkGCalToString();
        checkDateToString();
        
        System.out.println("");
        long jodaTotal = 0;
        long jisoTotal = 0;
        long gcalTotal = 0;
        long dateTotal = 0;
        for (Iterator it = resultList.iterator(); it.hasNext();) {
            Result res = (Result) it.next();
            System.out.println(res.object + "." + res.name + ": " + res.avg + "ns");
            if (res.object.equals("Joda")) {
                jodaTotal += res.avg;
            } else if (res.object.equals("JISO")) {
                jisoTotal += res.avg;
            } else if (res.object.equals("GCal")) {
                gcalTotal += res.avg;
            } else if (res.object.equals("Date")) {
                dateTotal += res.avg;
                System.out.println("");
            }
        }
        System.out.println("Joda: " + jodaTotal);
        System.out.println("JISO: " + jisoTotal);
        System.out.println("GCal: " + gcalTotal);
        System.out.println("Date: " + dateTotal);
    }

    // Constructor using currentTimeMillis()
    //------------------------------------------------------------------------
    
    private void checkJodaConstructor1() {
        int COUNT = COUNT_SLOW;
        DateTime dt = new DateTime(GJChronology.getInstance());
        int count = 0;
        for (int i = 0; i < AVERAGE; i++) {
            start("Joda", "new()");
            for (int j = 0; j < COUNT; j++) {
                dt = new DateTime(GJChronology.getInstance());
                if (count++ < 0) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkJISOConstructor1() {
        int COUNT = COUNT_SLOW;
        DateTime dt = new DateTime();
        int count = 0;
        for (int i = 0; i < AVERAGE; i++) {
            start("JISO", "new()");
            for (int j = 0; j < COUNT; j++) {
                dt = new DateTime();
                if (count++ < 0) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkGCalConstructor1() {
        int COUNT = COUNT_SLOW;
        GregorianCalendar dt = new GregorianCalendar();
        int count = 0;
        for (int i = 0; i < AVERAGE; i++) {
            start("GCal", "new()");
            for (int j = 0; j < COUNT; j++) {
                dt = new GregorianCalendar();
                if (count++ < 0) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkDateConstructor1() {
        int COUNT = COUNT_SLOW;
        Date dt = new Date();
        int count = 0;
        for (int i = 0; i < AVERAGE; i++) {
            start("Date", "new()");
            for (int j = 0; j < COUNT; j++) {
                dt = new Date();
                if (count++ < 0) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    // Constructor using long millis
    //------------------------------------------------------------------------
    
    private void checkJodaConstructor2() {
        int COUNT = COUNT_VERY_FAST;
        DateTime dt = new DateTime(12345L, GJChronology.getInstance());
        for (int i = 0; i < AVERAGE; i++) {
            start("Joda", "new(millis)");
            for (int j = 0; j < COUNT; j++) {
                dt = new DateTime(12345L, GJChronology.getInstance());
                if (dt == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkJISOConstructor2() {
        int COUNT = COUNT_VERY_FAST;
        DateTime dt = new DateTime(12345L);
        for (int i = 0; i < AVERAGE; i++) {
            start("JISO", "new(millis)");
            for (int j = 0; j < COUNT; j++) {
                dt = new DateTime(12345L);
                if (dt == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkGCalConstructor2() {
        int COUNT = COUNT_SLOW;
        GregorianCalendar dt = new GregorianCalendar();
        for (int i = 0; i < AVERAGE; i++) {
            start("GCal", "new(millis)");
            for (int j = 0; j < COUNT; j++) {
                dt = new GregorianCalendar();
                dt.setTime(new Date(12345L));
                if (dt == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkDateConstructor2() {
        int COUNT = COUNT_VERY_FAST;
        Date dt = new Date();
        for (int i = 0; i < AVERAGE; i++) {
            start("Date", "new(millis)");
            for (int j = 0; j < COUNT; j++) {
                dt = new Date(12345L);
                if (dt == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    // Constructor using year month and day
    //------------------------------------------------------------------------
    
    private void checkJodaConstructor3() {
        int COUNT = COUNT_SLOW;
        DateTime dt = new DateTime(1972, 10, 1, 0, 0, 0, 0,
                                   GJChronology.getInstance());
        for (int i = 0; i < AVERAGE; i++) {
            start("Joda", "new(YMD)");
            for (int j = 0; j < COUNT; j++) {
                dt = new DateTime(1972, 10, 1, 0, 0, 0, 0,
                                  GJChronology.getInstance());
                if (dt == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkJISOConstructor3() {
        int COUNT = COUNT_SLOW;
        DateTime dt = new DateTime(1972, 10, 1, 0, 0, 0, 0);
        for (int i = 0; i < AVERAGE; i++) {
            start("JISO", "new(YMD)");
            for (int j = 0; j < COUNT; j++) {
                dt = new DateTime(1972, 10, 1, 0, 0, 0, 0);
                if (dt == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkGCalConstructor3() {
        int COUNT = COUNT_SLOW;
        GregorianCalendar dt = new GregorianCalendar(1972, 10, 1);
        for (int i = 0; i < AVERAGE; i++) {
            start("GCal", "new(YMD)");
            for (int j = 0; j < COUNT; j++) {
                dt = new GregorianCalendar(1972, 10, 1);
                if (dt == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkDateConstructor3() {
        int COUNT = COUNT_SLOW;
        Date dt = new Date();
        for (int i = 0; i < AVERAGE; i++) {
            start("Date", "new(YMD)");
            for (int j = 0; j < COUNT; j++) {
                dt = new Date(1972, 10, 1);
                if (dt == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    // Get year
    //------------------------------------------------------------------------
    
    private void checkJodaGetYear() {
        int COUNT = COUNT_VERY_FAST;
        DateTime dt = new DateTime(GJChronology.getInstance());
        for (int i = 0; i < AVERAGE; i++) {
            start("Joda", "getYear");
            for (int j = 0; j < COUNT; j++) {
                int val = dt.getYear();
                if (val == 0) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkJISOGetYear() {
        int COUNT = COUNT_VERY_FAST;
        DateTime dt = new DateTime();
        for (int i = 0; i < AVERAGE; i++) {
            start("JISO", "getYear");
            for (int j = 0; j < COUNT; j++) {
                int val = dt.getYear();
                if (val == 0) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkGCalGetYear() {
        int COUNT = COUNT_VERY_FAST;
        GregorianCalendar dt = new GregorianCalendar();
        for (int i = 0; i < AVERAGE; i++) {
            start("GCal", "getYear");
            for (int j = 0; j < COUNT; j++) {
                int val = dt.get(GregorianCalendar.YEAR);
                if (val == 0) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkDateGetYear() {
        int COUNT = COUNT_FAST;
        Date dt = new Date();
        for (int i = 0; i < AVERAGE; i++) {
            start("Date", "getYear");
            for (int j = 0; j < COUNT; j++) {
                int val = dt.getYear();
                if (val == 0) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    // Get month
    //------------------------------------------------------------------------
    
    private void checkJodaGetMonth() {
        int COUNT = COUNT_VERY_FAST;
        DateTime dt = new DateTime(GJChronology.getInstance());
        for (int i = 0; i < AVERAGE; i++) {
            start("Joda", "getMonth");
            for (int j = 0; j < COUNT; j++) {
                int val = dt.getMonthOfYear();
                if (val == 0) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkJISOGetMonth() {
        int COUNT = COUNT_VERY_FAST;
        DateTime dt = new DateTime();
        for (int i = 0; i < AVERAGE; i++) {
            start("JISO", "getMonth");
            for (int j = 0; j < COUNT; j++) {
                int val = dt.getMonthOfYear();
                if (val == 0) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkGCalGetMonth() {
        int COUNT = COUNT_VERY_FAST;
        GregorianCalendar dt = new GregorianCalendar();
        for (int i = 0; i < AVERAGE; i++) {
            start("GCal", "getMonth");
            for (int j = 0; j < COUNT; j++) {
                int val = dt.get(GregorianCalendar.MONTH);
                if (val == 0) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkDateGetMonth() {
        int COUNT = COUNT_FAST;
        Date dt = new Date();
        for (int i = 0; i < AVERAGE; i++) {
            start("Date", "getMonth");
            for (int j = 0; j < COUNT; j++) {
                int val = dt.getMonth();
                if (val == 0) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    // Get day
    //------------------------------------------------------------------------
    
    private void checkJodaGetDay() {
        int COUNT = COUNT_VERY_FAST;
        DateTime dt = new DateTime(GJChronology.getInstance());
        for (int i = 0; i < AVERAGE; i++) {
            start("Joda", "getDay");
            for (int j = 0; j < COUNT; j++) {
                int val = dt.getDayOfMonth();
                if (val == 0) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkJISOGetDay() {
        int COUNT = COUNT_VERY_FAST;
        DateTime dt = new DateTime();
        for (int i = 0; i < AVERAGE; i++) {
            start("JISO", "getDay");
            for (int j = 0; j < COUNT; j++) {
                int val = dt.getDayOfMonth();
                if (val == 0) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkGCalGetDay() {
        int COUNT = COUNT_VERY_FAST;
        GregorianCalendar dt = new GregorianCalendar();
        for (int i = 0; i < AVERAGE; i++) {
            start("GCal", "getDay");
            for (int j = 0; j < COUNT; j++) {
                int val = dt.get(GregorianCalendar.DAY_OF_MONTH);
                if (val == 0) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkDateGetDay() {
        int COUNT = COUNT_FAST;
        Date dt = new Date();
        for (int i = 0; i < AVERAGE; i++) {
            start("Date", "getDay");
            for (int j = 0; j < COUNT; j++) {
                int val = dt.getDate();
                if (val == 0) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    // Get hour
    //------------------------------------------------------------------------
    
    private void checkJodaGetHour() {
        int COUNT = COUNT_VERY_FAST;
        DateTime dt = new DateTime(GJChronology.getInstance());
        for (int i = 0; i < AVERAGE; i++) {
            start("Joda", "getHour");
            for (int j = 0; j < COUNT; j++) {
                int val = dt.getHourOfDay();
                if (val == -1) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkJISOGetHour() {
        int COUNT = COUNT_VERY_FAST;
        DateTime dt = new DateTime();
        for (int i = 0; i < AVERAGE; i++) {
            start("JISO", "getHour");
            for (int j = 0; j < COUNT; j++) {
                int val = dt.getHourOfDay();
                if (val == -1) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkGCalGetHour() {
        int COUNT = COUNT_VERY_FAST;
        GregorianCalendar dt = new GregorianCalendar();
        for (int i = 0; i < AVERAGE; i++) {
            start("GCal", "getHour");
            for (int j = 0; j < COUNT; j++) {
                int val = dt.get(GregorianCalendar.HOUR_OF_DAY);
                if (val == -1) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkDateGetHour() {
        int COUNT = COUNT_FAST;
        Date dt = new Date();
        for (int i = 0; i < AVERAGE; i++) {
            start("Date", "getHour");
            for (int j = 0; j < COUNT; j++) {
                int val = dt.getHours();
                if (val == -1) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    // Set year
    //------------------------------------------------------------------------
    
    private void checkJodaSetYear() {
        int COUNT = COUNT_FAST;
        // Is it fair to use only MutableDateTime here? You decide.
        MutableDateTime dt = new MutableDateTime(GJChronology.getInstance());
        for (int i = 0; i < AVERAGE; i++) {
            start("Joda", "setYear");
            for (int j = 0; j < COUNT; j++) {
                dt.setYear(1972);
                if (dt == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkJISOSetYear() {
        int COUNT = COUNT_FAST;
        // Is it fair to use only MutableDateTime here? You decide.
        MutableDateTime dt = new MutableDateTime();
        for (int i = 0; i < AVERAGE; i++) {
            start("JISO", "setYear");
            for (int j = 0; j < COUNT; j++) {
                dt.setYear(1972);
                if (dt == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkGCalSetYear() {
        int COUNT = COUNT_VERY_FAST;
        GregorianCalendar dt = new GregorianCalendar();
        for (int i = 0; i < AVERAGE; i++) {
            start("GCal", "setYear");
            for (int j = 0; j < COUNT; j++) {
                dt.set(GregorianCalendar.YEAR, 1972);
                if (dt == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkDateSetYear() {
        int COUNT = COUNT_FAST;
        Date dt = new Date();
        for (int i = 0; i < AVERAGE; i++) {
            start("Date", "setYear");
            for (int j = 0; j < COUNT; j++) {
                dt.setYear(1972);
                if (dt == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    // Set then get year
    //------------------------------------------------------------------------
    
    private void checkJodaSetGetYear() {
        int COUNT = COUNT_FAST;
        // Is it fair to use only MutableDateTime here? You decide.
//        MutableDateTime dt = new MutableDateTime(GJChronology.getInstance());
//        for (int i = 0; i < AVERAGE; i++) {
//            start("Joda", "setGetYear");
//            for (int j = 0; j < COUNT; j++) {
//                dt.setYear(1972);
//                int val = dt.getYear();
//                if (val < 0) {System.out.println("Anti optimise");}
//            }
//            end(COUNT);
//        }
        DateTime dt = new DateTime(GJChronology.getInstance());
        for (int i = 0; i < AVERAGE; i++) {
            start("Joda", "setGetYear");
            for (int j = 0; j < COUNT; j++) {
                dt = dt.year().setCopy(1972);
                int val = dt.getYear();
                if (val < 0) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkJISOSetGetYear() {
        int COUNT = COUNT_FAST;
        // Is it fair to use only MutableDateTime here? You decide.
//        MutableDateTime dt = new MutableDateTime();
//        for (int i = 0; i < AVERAGE; i++) {
//            start("JISO", "setGetYear");
//            for (int j = 0; j < COUNT; j++) {
//                dt.setYear(1972);
//                int val = dt.getYear();
//                if (val < 0) {System.out.println("Anti optimise");}
//            }
//            end(COUNT);
//        }
        DateTime dt = new DateTime();
        for (int i = 0; i < AVERAGE; i++) {
            start("JISO", "setGetYear");
            for (int j = 0; j < COUNT; j++) {
                dt = dt.year().setCopy(1972);
                int val = dt.getYear();
                if (val < 0) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkGCalSetGetYear() {
        int COUNT = COUNT_FAST;
        GregorianCalendar dt = new GregorianCalendar();
        for (int i = 0; i < AVERAGE; i++) {
            start("GCal", "setGetYear");
            for (int j = 0; j < COUNT; j++) {
                dt.set(GregorianCalendar.YEAR, 1972);
                int val = dt.get(GregorianCalendar.YEAR);
                if (val < 0) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkDateSetGetYear() {
        int COUNT = COUNT_FAST;
        Date dt = new Date();
        for (int i = 0; i < AVERAGE; i++) {
            start("Date", "setGetYear");
            for (int j = 0; j < COUNT; j++) {
                dt.setYear(1972);
                int val = dt.getYear();
                if (val < 0) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    // Set hour
    //------------------------------------------------------------------------
    
    private void checkJodaSetHour() {
        int COUNT = COUNT_VERY_FAST;
        // Is it fair to use only MutableDateTime here? You decide.
        MutableDateTime dt = new MutableDateTime(GJChronology.getInstance());
        for (int i = 0; i < AVERAGE; i++) {
            start("Joda", "setHour");
            for (int j = 0; j < COUNT; j++) {
                dt.setHourOfDay(13);
                if (dt == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkJISOSetHour() {
        int COUNT = COUNT_VERY_FAST;
        // Is it fair to use only MutableDateTime here? You decide.
        MutableDateTime dt = new MutableDateTime();
        for (int i = 0; i < AVERAGE; i++) {
            start("JISO", "setHour");
            for (int j = 0; j < COUNT; j++) {
                dt.setHourOfDay(13);
                if (dt == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkGCalSetHour() {
        int COUNT = COUNT_VERY_FAST;
        GregorianCalendar dt = new GregorianCalendar();
        for (int i = 0; i < AVERAGE; i++) {
            start("GCal", "setHour");
            for (int j = 0; j < COUNT; j++) {
                dt.set(GregorianCalendar.HOUR_OF_DAY, 13);
                if (dt == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkDateSetHour() {
        int COUNT = COUNT_FAST;
        Date dt = new Date();
        for (int i = 0; i < AVERAGE; i++) {
            start("Date", "setHour");
            for (int j = 0; j < COUNT; j++) {
                dt.setHours(13);
                if (dt == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    // Set hour
    //------------------------------------------------------------------------
    
    private void checkJodaSetGetHour() {
        int COUNT = COUNT_VERY_FAST;
        // Is it fair to use only MutableDateTime here? You decide.
        MutableDateTime dt = new MutableDateTime(GJChronology.getInstance());
        for (int i = 0; i < AVERAGE; i++) {
            start("Joda", "setGetHour");
            for (int j = 0; j < COUNT; j++) {
                dt.setHourOfDay(13);
                int val = dt.getHourOfDay();
                if (dt == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkJISOSetGetHour() {
        int COUNT = COUNT_VERY_FAST;
        // Is it fair to use only MutableDateTime here? You decide.
        MutableDateTime dt = new MutableDateTime();
        for (int i = 0; i < AVERAGE; i++) {
            start("JISO", "setGetHour");
            for (int j = 0; j < COUNT; j++) {
                dt.setHourOfDay(13);
                int val = dt.getHourOfDay();
                if (dt == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkGCalSetGetHour() {
        int COUNT = COUNT_VERY_FAST;
        GregorianCalendar dt = new GregorianCalendar();
        for (int i = 0; i < AVERAGE; i++) {
            start("GCal", "setGetHour");
            for (int j = 0; j < COUNT; j++) {
                dt.set(GregorianCalendar.HOUR_OF_DAY, 13);
                int val = dt.get(GregorianCalendar.HOUR_OF_DAY);
                if (dt == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkDateSetGetHour() {
        int COUNT = COUNT_FAST;
        Date dt = new Date();
        for (int i = 0; i < AVERAGE; i++) {
            start("Date", "setGetHour");
            for (int j = 0; j < COUNT; j++) {
                dt.setHours(13);
                int val = dt.getHours();
                if (dt == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    // To formatted string
    //------------------------------------------------------------------------
    
    private void checkJodaToString() {
        int COUNT = COUNT_SLOW;
        DateTime dt = new DateTime(GJChronology.getInstance());
        DateTimeFormatter f = DateTimeFormat.forPattern("dd MMM yyyy");
        for (int i = 0; i < AVERAGE; i++) {
            start("Joda", "toString");
            for (int j = 0; j < COUNT; j++) {
                String str = dt.toString("dd MMM yyyy");
//                String str = dt.toString(f);
                if (str == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkJISOToString() {
        int COUNT = COUNT_SLOW;
        DateTime dt = new DateTime();
        DateTimeFormatter f = DateTimeFormat.forPattern("dd MMM yyyy");
        for (int i = 0; i < AVERAGE; i++) {
            start("JISO", "toString");
            for (int j = 0; j < COUNT; j++) {
                String str = dt.toString("dd MMM yyyy");
//                String str = dt.toString(f);
                if (str == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkGCalToString() {
        int COUNT = COUNT_SLOW;
        GregorianCalendar dt = new GregorianCalendar();
        for (int i = 0; i < AVERAGE; i++) {
            start("GCal", "toString");
            for (int j = 0; j < COUNT; j++) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                String str = sdf.format(dt.getTime());
                if (str == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    private void checkDateToString() {
        int COUNT = COUNT_SLOW;
        Date dt = new Date();
        for (int i = 0; i < AVERAGE; i++) {
            start("Date", "toString");
            for (int j = 0; j < COUNT; j++) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                String str = sdf.format(dt);
                if (str == null) {System.out.println("Anti optimise");}
            }
            end(COUNT);
        }
    }

    //------------------------------------------------------------------------
    
    /**
     * Start the stopwatch.
     */
    private void start(String str1, String str2) {
        result = (Result) results.get(str1 + str2);
        if (result == null) {
            result = new Result();
            result.object = str1;
            result.name = str2;
            results.put(str1 + str2, result);
            resultList.add(result);
        }
        start = System.currentTimeMillis();
    }
   
    /**
     * End the stopwatch and print the result.
     */
    private void end(int count) {
        end = System.currentTimeMillis();
        long time = (end - start);
        result.time = result.time + time;
        result.runs = result.runs + count;
        result.avg = (result.time * 1000000) / result.runs;
        System.out.print(".");
    }

}
