/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2004 Stephen Colebourne.  
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Joda project (http://www.joda.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "Joda" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact licence@joda.org.
 *
 * 5. Products derived from this software may not be called "Joda",
 *    nor may "Joda" appear in their name, without prior written
 *    permission of the Joda project.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE JODA AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Joda project and was originally 
 * created by Stephen Colebourne <scolebourne@joda.org>. For more
 * information on the Joda project, please see <http://www.joda.org/>.
 */
package org.joda.time.chrono;

import java.lang.ref.WeakReference;
import java.text.DateFormatSymbols;
import java.util.WeakHashMap;
import java.util.Locale;

/**
 * Utility class used by a few of the GJDateTimeFields.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
class GJLocaleSymbols {
    private static final int FAST_CACHE_SIZE = 64;

    private static final GJLocaleSymbols[] cFastCache = new GJLocaleSymbols[FAST_CACHE_SIZE];

    private static WeakHashMap cCache = new WeakHashMap();

    public static GJLocaleSymbols forLocale(Locale locale) {
        int index = System.identityHashCode(locale) & (FAST_CACHE_SIZE - 1);
        GJLocaleSymbols symbols = cFastCache[index];
        if (symbols != null && symbols.iLocale.get() == locale) {
            return symbols;
        }
        synchronized (cCache) {
            symbols = (GJLocaleSymbols) cCache.get(locale);
            if (symbols == null) {
                symbols = new GJLocaleSymbols(locale);
                cCache.put(locale, symbols);
            }
        }
        cFastCache[index] = symbols;
        return symbols;
    }

    private static String[] realignMonths(String[] months) {
        String[] a = new String[13];
        for (int i=1; i<13; i++) {
            a[i] = months[i - 1];
        }
        return a;
    }

    private static String[] realignDaysOfWeek(String[] daysOfWeek) {
        String[] a = new String[8];
        for (int i=1; i<8; i++) {
            a[i] = daysOfWeek[(i < 7) ? i + 1 : 1];
        }
        return a;
    }

    private static int maxLength(String[] a) {
        int max = 0;
        for (int i=a.length; --i>=0; ) {
            String s = a[i];
            if (s != null) {
                int len = s.length();
                if (len > max) {
                    max = len;
                }
            }
        }
        return max;
    }

    private final WeakReference iLocale;

    private final String[] iEras;
    private final String[] iDaysOfWeek;
    private final String[] iShortDaysOfWeek;
    private final String[] iMonths;
    private final String[] iShortMonths;
    private final String[] iHalfday;

    private final int iMaxEraLength;
    private final int iMaxDayOfWeekLength;
    private final int iMaxShortDayOfWeekLength;
    private final int iMaxMonthLength;
    private final int iMaxShortMonthLength;
    private final int iMaxHalfdayLength;

    private GJLocaleSymbols(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }

        iLocale = new WeakReference(locale);

        DateFormatSymbols dfs = new DateFormatSymbols(locale);

        iEras = dfs.getEras();
        iDaysOfWeek = realignDaysOfWeek(dfs.getWeekdays());
        iShortDaysOfWeek = realignDaysOfWeek(dfs.getShortWeekdays());
        iMonths = realignMonths(dfs.getMonths());
        iShortMonths = realignMonths(dfs.getShortMonths());
        iHalfday = dfs.getAmPmStrings();

        iMaxEraLength = maxLength(iEras);
        iMaxDayOfWeekLength = maxLength(iDaysOfWeek);
        iMaxShortDayOfWeekLength = maxLength(iShortDaysOfWeek);
        iMaxMonthLength = maxLength(iMonths);
        iMaxShortMonthLength = maxLength(iShortMonths);
        iMaxHalfdayLength = maxLength(iHalfday);
    }

    public String eraValueToText(int value) {
        return iEras[value];
    }

    public int eraTextToValue(String text) {
        String[] eras = iEras;
        for (int i=eras.length; --i>=0; ) {
            if (eras[i].equalsIgnoreCase(text)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Illegal era text: " + text);
    }

    public int getEraMaxTextLength() {
        return iMaxEraLength;
    }

    public String monthOfYearValueToText(int value) {
        return iMonths[value];
    }

    public String monthOfYearValueToShortText(int value) {
        return iShortMonths[value];
    }

    public int monthOfYearTextToValue(String text) {
        String[] months = iMonths;
        for (int i=months.length; --i>=1; ) {
            if (months[i].equalsIgnoreCase(text)) {
                return i;
            }
        }
        months = iShortMonths;
        for (int i=months.length; --i>=1; ) {
            if (months[i].equalsIgnoreCase(text)) {
                return i;
            }
        }
        try {
            int month = Integer.parseInt(text);
            if (month >= 1 && month <= 12) {
                return month;
            }
        } catch (NumberFormatException ex) {
            // ignore
        }
        throw new IllegalArgumentException("Illegal monthOfYear text: " + text);
    }

    public int getMonthMaxTextLength() {
        return iMaxMonthLength;
    }

    public int getMonthMaxShortTextLength() {
        return iMaxShortMonthLength;
    }

    public String dayOfWeekValueToText(int value) {
        return iDaysOfWeek[value];
    }

    public String dayOfWeekValueToShortText(int value) {
        return iShortDaysOfWeek[value];
    }

    public int dayOfWeekTextToValue(String text) {
        String[] daysOfWeek = iDaysOfWeek;
        for (int i=daysOfWeek.length; --i>=1; ) {
            if (daysOfWeek[i].equalsIgnoreCase(text)) {
                return i;
            }
        }
        daysOfWeek = iShortDaysOfWeek;
        for (int i=daysOfWeek.length; --i>=1; ) {
            if (daysOfWeek[i].equalsIgnoreCase(text)) {
                return i;
            }
        }
        try {
            int day = Integer.parseInt(text);
            if (day >= 1 && day <= 7) {
                return day;
            }
        } catch (NumberFormatException ex) {
            // ignore
        }
        throw new IllegalArgumentException("Illegal dayOfWeek text: " + text);
    }

    public int getDayOfWeekMaxTextLength() {
        return iMaxDayOfWeekLength;
    }

    public int getDayOfWeekMaxShortTextLength() {
        return iMaxShortDayOfWeekLength;
    }

    public String halfdayValueToText(int value) {
        return iHalfday[value];
    }

    public int halfdayTextToValue(String text) {
        String[] halfday = iHalfday;
        for (int i = halfday.length; --i>=0; ) {
            if (halfday[i].equalsIgnoreCase(text)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Illegal halfday text: " + text);
    }

    public int getHalfdayMaxTextLength() {
        return iMaxHalfdayLength;
    }
}
