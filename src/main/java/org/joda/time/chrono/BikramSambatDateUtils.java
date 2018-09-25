/*
 *  Copyright 2001-2018 Bishwash Adhikari
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
package org.joda.time.chrono;

/**
 * Nepali date conversion utilities and database.
 */
public class BikramSambatDateUtils {
    private static final int FIRST_BIS_YEAR_SUPPORTED = 2000;
    private static final int NUMBER_OF_MONTHS = 12;

    /**
     * Date Database useful for converting from/to Nepali/English dates.
     *
     * Basically, this is an array of arrays. Each sub-array represents a year. Each year contains
     * number of days in each month as array of integers.
     *
     * Start BIS Date : 2000/1/1
     * Start AD Date : 1943/4/14
     *
     * End BIS Date : 2090/1/1
     * End AD Date : 2033/4/14
     */
    public final static int[][] data = new int[][]{
            new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31}, // 2000 Birkram Sambat
            new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31},
            new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30},
            new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30},
            new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            new int[]{31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            new int[]{31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 31, 32, 31, 32, 30, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            new int[]{30, 32, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31},
            new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30},
            new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30},
            new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            new int[]{31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            new int[]{31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 31, 32, 31, 32, 30, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            new int[]{30, 32, 31, 32, 31, 31, 29, 30, 29, 30, 29, 31},
            new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31},
            new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30},
            new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31},
            new int[]{31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31},
            new int[]{31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30},
            new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30},
            new int[]{31, 31, 32, 32, 31, 30, 30, 30, 29, 30, 30, 30},
            new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30},
            new int[]{31, 31, 32, 31, 31, 30, 30, 30, 29, 30, 30, 30},
            new int[]{31, 31, 32, 31, 31, 30, 30, 30, 29, 30, 30, 30},
            new int[]{31, 32, 31, 32, 30, 31, 30, 30, 29, 30, 30, 30},
            new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30},
            new int[]{31, 31, 32, 31, 31, 31, 30, 30, 29, 30, 30, 30},
            new int[]{30, 31, 32, 32, 30, 31, 30, 30, 29, 30, 30, 30},
            new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30},
            new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30}, // 2090 Birkram Sambat
    };

    static int getTotalDaysInYear(int year) {
        int pos = year - FIRST_BIS_YEAR_SUPPORTED;
        int days = 0;

        for (int j = 0; j < NUMBER_OF_MONTHS; j++) {
            days += data[pos][j];
        }

        return days;
    }

    static int getTotalDaysInYearTillMonth(int year, int month) {
        int pos = year - FIRST_BIS_YEAR_SUPPORTED;
        int days = 0; // 0 for first month

        for (int j = 0; j <= NUMBER_OF_MONTHS; j++) {
            if (j == month - 1) {
                break;
            }
            days += data[pos][j];
        }

        return days;
    }

    static int getTotalDaysInYearsMonth(int year, int month) {
        return data[year - FIRST_BIS_YEAR_SUPPORTED][month - 1];
    }

    static int getMonthByYearDays(int year, int day){
        int pos = year - FIRST_BIS_YEAR_SUPPORTED;
        int days = 0;

        for (int j = 0; j < NUMBER_OF_MONTHS; j++) {
            days += data[pos][j];
            if (day < days){
                return j + 1;
            }
        }
        return 1;
    }

    public static int getDayOfMonth(int year, int doy){
        int pos = year - FIRST_BIS_YEAR_SUPPORTED;
        int dayOfMonth = 1;

        for (int j = 0; j < NUMBER_OF_MONTHS; j++) {
            if (doy <= data[pos][j]){
                dayOfMonth = doy;
                break;
            }
            doy -= data[pos][j];
        }
        return dayOfMonth;
    }
}
