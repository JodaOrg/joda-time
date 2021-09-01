/*
 *  Copyright 2001-2014 Stephen Colebourne
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import junit.framework.Assert;

import org.joda.time.DateTimeZone;

/**
 * This class is a testing class for threading.
 */
public class ThreadContention {

    public static void main(String[] args) {
        multiThreadGJLocale();
        singleThreadGJLocale2();
        singleThreadGJLocale();
        multiThreadZones();
        singleThreadZones();
    }

    //-------------------------------------------------------------------------
    private static void multiThreadGJLocale() {
        final CountDownLatch latch = new CountDownLatch(1);
        final List<Locale> locales = createLocales();
        List<Runnable> runnables = new ArrayList<Runnable>();
        for (int i = 0; i < 100; i++) { 
            Runnable r = new Runnable() {
                public void run() {
                    try {
                        latch.await();
                        List<Locale> shuffled = new ArrayList<Locale>(locales);
                        Collections.shuffle(shuffled);
                        String name = Thread.currentThread().getName();
                        for (int j = 0; j < 100; j++) { 
                            for (Locale locale : shuffled) {
                                GJLocaleSymbols symbols = GJLocaleSymbols.forLocale(locale);
                                Assert.assertEquals(GJLocaleSymbols.class, symbols.getClass());
                            }
                        }
                      System.out.println("Finished: " + name);
                        
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.exit(1);
                    }
                }
            };
            new Thread(r).start();
            runnables.add(r);
        }
        latch.countDown();
    }

    private static void singleThreadGJLocale() {
        List<Locale> locales = createLocales();
        List<Locale> shuffled = new ArrayList<Locale>(locales);
        Collections.shuffle(shuffled);
        long start = System.nanoTime();
        int count = 0;
        for (int j = 0; j < 10000; j++) { 
            for (Locale locale : shuffled) {
                GJLocaleSymbols symbols = GJLocaleSymbols.forLocale(locale);
                count = count + symbols.getDayOfWeekMaxShortTextLength();
            }
        }
        long end = System.nanoTime();
        System.out.println("Finished " + count + " " + (end - start) / 1000000);
    }

    private static void singleThreadGJLocale2() {
        List<Locale> locales = createLocales();
        List<Locale> shuffled = new ArrayList<Locale>(locales);
        Collections.shuffle(shuffled);
        long start = System.nanoTime();
        int count = 0;
        for (int j = 0; j < 1000000; j++) { 
            GJLocaleSymbols symbols = GJLocaleSymbols.forLocale(Locale.US);
            count = count + symbols.getDayOfWeekMaxShortTextLength() + symbols.hashCode();
        }
        long end = System.nanoTime();
        System.out.println("Finished " + count + " " + (end - start) / 1000000);
    }

    private static List<Locale> createLocales() {
        return Arrays.asList(Locale.getAvailableLocales());
    }

    //-------------------------------------------------------------------------
    private static void multiThreadZones() {
        final CountDownLatch latch = new CountDownLatch(1);
        final List<DateTimeZone> zones = createZones();
        List<Runnable> runnables = new ArrayList<Runnable>();
        for (int i = 0; i < 100; i++) { 
            Runnable r = new Runnable() {
                public void run() {
                    try {
                        latch.await();
                        List<DateTimeZone> shuffled = new ArrayList<DateTimeZone>(zones);
                        Collections.shuffle(shuffled);
                        String name = Thread.currentThread().getName();
                        for (int j = 0; j < 100; j++) { 
                            for (DateTimeZone zn : shuffled) {
                                ISOChronology chrono = ISOChronology.getInstance(zn);
                                Assert.assertEquals(zn, chrono.getZone());
                            }
                        }
                      System.out.println("Finished: " + name);
                        
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.exit(1);
                    }
                }
            };
            new Thread(r).start();
            runnables.add(r);
        }
        latch.countDown();
    }

    private static void singleThreadZones() {
        List<DateTimeZone> zones = createZones();
        List<DateTimeZone> shuffled = new ArrayList<DateTimeZone>(zones);
        Collections.shuffle(shuffled);
        long start = System.nanoTime();
        int count = 0;
        for (int j = 0; j < 10000; j++) { 
            for (DateTimeZone zn : shuffled) {
                ISOChronology chrono = ISOChronology.getInstance(zn);
                count = count + chrono.getZone().getID().length();
            }
        }
        long end = System.nanoTime();
        System.out.println("Finished " + count + " " + (end - start) / 1000000);
    }

    private static List<DateTimeZone> createZones() {
        final List<DateTimeZone> zones = new ArrayList<DateTimeZone>();
        for (final String zone : DateTimeZone.getAvailableIDs()) {
            zones.add(DateTimeZone.forID(zone));
        }
        return zones;
    }

}
