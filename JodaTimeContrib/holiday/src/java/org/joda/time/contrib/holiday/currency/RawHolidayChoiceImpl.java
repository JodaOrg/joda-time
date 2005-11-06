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
package org.joda.time.contrib.holiday.currency;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Holidays.
 *
 * @author Scott R. Duchin
 */
abstract class AbstractHoliday extends AbstractChoice implements RawHolidayChoice {

    static final long serialVersionUID = 1001L;             // version 1 for serialization

    // state fields
    protected /*day*/int                _day;               // day (or days from easter, etc...)
    protected /*year*/int               _end = 2100;        // ending year for holidays
    protected int                       _every = 1;         // the number of years between holidays
    protected Month                     _month;             // month of holiday
    protected /*year*/int               _start = 1;         // starting year for holidays
    protected Weekday                   _weekday;           // day of the week

    /**
     * Constructor for holiday.
     * @param label The holiday convention.
     * @param month Month holiday is in.
     * @param day   Day holiday is on.
     */
    public AbstractHoliday(String label, Month month, /*day*/int day) {
        super(RawHolidayChoiceImpl.choices(), label);
        _day = day;
        _month = month;
    }

    /**
     * Constructor for holiday.
     * @param label The holiday convention.
     * @param month Month holiday is in.
     * @param day   Day holiday is on.
     * @param start Starting year.
     * @param end   Ending year.
     * @param every Number of years between holidays.
     */
    public AbstractHoliday(String label, Month month, /*day*/int day, /*year*/int start, /*year*/int end, int every) {
        super(RawHolidayChoiceImpl.choices(), label);
        _day = day;
        _month = month;
        _start = start;
        _end = end;
        _every = every;
    }

    /**
     * Returns a list of dates of the particular holiday for the specified years.
     * Note: the holiday returned is pure; not adjusted for Saturday or Sunday local behavior.
     * @param start Starting year.
     * @param end   Ending year.
     * @return List of dates of holidays in specified years, inclusive.
     */
    public List<Date> dates(/*year*/int start, /*year*/int end) {
        List<Date> list = new ArrayList<Date>(end - start + 1);
        for (/*year*/int year = start; year <= end; year++) {
            list.add(date(year));
        }
        return list;
    }
}

/**
 * Easter Holidays.
 *
 * @author Scott R. Duchin
 */
class HolidayEaster extends AbstractHoliday {

    static final long serialVersionUID = 1001L;             // version 1 for serialization

    /**
     * Constructor for holiday.
     * @param label The holiday convention.
     * @param days  Days from Easter Sunday.
     */
    public HolidayEaster(String label, int days) {
        super(label, null, days);
    }

    private static final /*day*/int[] EASTER = {
        14, 3, 23, 11, 31, 18, 8, 28, 16, 5, 25, 13, 2, 22, 10, 30, 17, 7, 27
    };

    /**
     * Returns the date of the particular holiday for the specified year.
     * Note: the holiday returned is pure; not adjusted for Saturday or Sunday local behavior.
     * @param year Year the holiday is requested for.
     * @return Date of holiday in specified year; <code>null</code> if no holiday in specified year.
     */
    public Date date(/*year*/int year) {
        int day = EASTER[year % 19];
        Date easter = DateFactory.date(year, (day < 20) ? Month.APR : Month.MAR, day).addDays(1).nextWeekday(Weekday.SUN);
        Date holi = easter.addDays(_day);
        return holi;
    }
}

/**
 * Equinox Holidays.
 *
 * @author Scott R. Duchin
 */
class HolidayEquinox extends AbstractHoliday {

    static final long serialVersionUID = 1001L;             // version 1 for serialization

    /**
     * Constructor for holiday.
     * @param label  The holiday convention.
     * @param spring <code>true</code> for Spring; <code>false</code> for Autumn.
     * @param days   Days from Equinox.
     */
    public HolidayEquinox(String label, boolean spring, int days) {
        super(label, spring ? Month.MAR : Month.SEP, days);
    }

    /**
     * Returns the date of the particular holiday for the specified year.
     * Note: the holiday returned is pure; not adjusted for Saturday or Sunday local behavior.
     * @param year Year the holiday is requested for.
     * @return Date of holiday in specified year; <code>null</code> if no holiday in specified year.
     */
    public Date date(/*year*/int year) {
        // TODO: must get starting seconds and add seconds per year to derive actual time of equinox
        Date equinox;
        if (_month == Month.MAR) {
            equinox = DateFactory.date(year, _month, 21);
        } else {
            equinox = DateFactory.date(year, _month, 23);
        }
        Date holi = equinox.addDays(_day);
        return holi;
    }
}

/**
 * Islamic Holidays.
 *
 * @author Scott R. Duchin
 */
class HolidayIslamic extends AbstractHoliday {

    static final long serialVersionUID = 1001L;             // version 1 for serialization

    /**
     * Constructor for holiday.
     * @param label The holiday convention.
     * @param days  Days from Islamic new year.
     */
    public HolidayIslamic(String label, int days) {
        super(label, null, days);
    }

    private static final int[][] ISLAMIC = {
        {1999, 4, 18}, {2000, 4, 6}, {2001, 3, 26}, {2002, 3, 15}
    };

    /**
     * Returns the date of the particular holiday for the specified year.
     * Note: the holiday returned is pure; not adjusted for Saturday or Sunday local behavior.
     * @param year Year the holiday is requested for.
     * @return Date of holiday in specified year; <code>null</code> if no holiday in specified year.
     */
    public Date date(/*year*/int year) {
        Date holi = null;
        if (1999 <= year && year <= 2002) {
            int yr = year - 1999;
            Date islamic = DateFactory.date(year, Month.month(ISLAMIC[yr][1]), ISLAMIC[yr][2]);
            holi = islamic.addDays(_day);
        }
        return holi;
    }
}

/**
 * Lunar New Year Holidays.
 *
 * @author Scott R. Duchin
 */
class HolidayLunar extends AbstractHoliday {

    static final long serialVersionUID = 1001L;             // version 1 for serialization

    /**
     * Constructor for holiday.
     * @param label The holiday convention.
     * @param days  Days from lunar new year.
     */
    public HolidayLunar(String label, int days) {
        super(label, null, days);
    }

    private static final int[][] LUNAR = {
        {1999, 2, 16}, {2000, 2,  5}, {2001, 1, 24}, {2002, 2, 12}, {2003, 2,  1}, {2004, 1, 22},
        {2005, 2,  9}, {2006, 1, 29}, {2007, 2, 18}, {2008, 2,  7}, {2009, 1, 26}, {2010, 2, 14}
    };

    /**
     * Returns the date of the particular holiday for the specified year.
     * Note: the holiday returned is pure; not adjusted for Saturday or Sunday local behavior.
     * @param year Year the holiday is requested for.
     * @return Date of holiday in specified year; <code>null</code> if no holiday in specified year.
     */
    public Date date(/*year*/int year) {
        Date holi = null;
        if (1999 <= year && year <= 2010) {
            int yr = year - 1999;
            Date lunar = DateFactory.date(year, Month.month(LUNAR[yr][1]), LUNAR[yr][2]);
            holi = lunar.addDays(_day);
        }
        return holi;
    }
}

/**
 * Day Of Month Holidays.
 *
 * @author Scott R. Duchin
 */
class HolidayMonthDay extends AbstractHoliday {

    static final long serialVersionUID = 1001L;             // version 1 for serialization

    /**
     * Constructor for holiday.
     * @param label The holiday convention.
     * @param month Month holiday is in.
     * @param day   Day holiday is on.
     */
    public HolidayMonthDay(String label, Month month, /*day*/int day) {
        super(label, month, day);
    }

    /**
     * Constructor for holiday.
     * @param label The holiday convention.
     * @param month Month holiday is in.
     * @param day   Day holiday is on.
     * @param start Starting year.
     * @param end   Ending year.
     * @param every Number of years between holidays.
     */
    public HolidayMonthDay(String label, Month month, /*day*/int day, /*year*/int start, /*year*/int end, int every) {
        super(label, month, day, start, end, every);
    }

    /**
     * Returns the date of the particular holiday for the specified year.
     * Note: the holiday returned is pure; not adjusted for Saturday or Sunday local behavior.
     * @param year Year the holiday is requested for.
     * @return Date of holiday in specified year; <code>null</code> if no holiday in specified year.
     */
    public Date date(/*year*/int year) {
        Date holi = null;
        if (_every == 1 || (_start <= year && year <= _end && ((year - _start) % _every == 0))) {
            holi = DateFactory.date(year, _month, _day);
        }
        return holi;
    }
}

/**
 * Weekday Holidays.
 *
 * @author Scott R. Duchin
 */
class HolidayWeekday extends AbstractHoliday {

    static final long serialVersionUID = 1001L;             // version 1 for serialization

    /**
     * Constructor for holiday.
     * @param label   The holiday convention.
     * @param month   Month holiday is in.
     * @param day     First possible day for holiday.
     * @param weekday Day of the week holiday is on.
     */
    public HolidayWeekday(String label, Month month, /*day*/int day, Weekday weekday) {
        super(label, month, day);
        _weekday = weekday;
    }

    /**
     * Returns the date of the particular holiday for the specified year.
     * Note: the holiday returned is pure; not adjusted for Saturday or Sunday local behavior.
     * @param year Year the holiday is requested for.
     * @return Date of holiday in specified year; <code>null</code> if no holiday in specified year.
     */
    public Date date(/*year*/int year) {
        Date holi = DateFactory.date(year, _month, _day);
        return holi.nextWeekday(_weekday);
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * The choice list for holiday implementations.
 *
 * @author Scott R. Duchin
 */
public final class RawHolidayChoiceImpl {

///////////////////////////////////////////////////////////////////////////////////////////////////
// Static Variables & Methods

    // static variables
    private static ChoiceList _choices;

    /**
     * Static initializer to create the holiday convention list.
     */
    static {
        _choices = new ChoiceList(klass());

        new HolidayMonthDay("Africa Day",                   Month.MAY,  25);
        new HolidayMonthDay("All Saints Day",               Month.NOV,   1);
        new HolidayMonthDay("All Souls Day",                Month.NOV,   2);
        new HolidayMonthDay("Angamos Battle",               Month.OCT,   8);
        new HolidayMonthDay("Annunciation",                 Month.MAR,  25);
        new HolidayMonthDay("Anzac Day",                    Month.APR,  25);
        new HolidayWeekday("Argentina Flag Day",            Month.JUN,  18,  Weekday.MON); // TODO: starting date
        new HolidayMonthDay("Argentina Independence",       Month.JUL,   9);
        new HolidayMonthDay("Argentina May Revolution",     Month.MAY,  25);
        new HolidayMonthDay("Armistice Day",                Month.NOV,  11);
        new HolidayEaster("Ascension",                       39);
        new HolidayEaster("Ash Wednesday",                  -46);
        new HolidayMonthDay("Assumption",                   Month.AUG,  15);
        new HolidayMonthDay("Australia Day",                Month.JAN,  26);
        new HolidayMonthDay("Australia Labour Day",         Month.MAR,   1);
        new HolidayWeekday("Australia Queen Day",           Month.JUN,   8,  Weekday.MON);
        new HolidayMonthDay("Austria National Day",         Month.OCT,  26);
        new HolidayWeekday("Autumn Bank Holiday",           Month.AUG,   1,  Weekday.MON);
        new HolidayEquinox("Autumnal Equinox",              false, 0);
        new HolidayMonthDay("Bahamas Independence",         Month.JUL,  10);
        new HolidayWeekday("Bahamas Labour Day",            Month.JUN,   1,  Weekday.FRI);
        new HolidayMonthDay("Bank Balance I",               Month.JUN,  29);
        new HolidayMonthDay("Bank Balance II",              Month.JUN,  30);
        new HolidayMonthDay("Barbados Heroes Day",          Month.APR,  28);
        new HolidayMonthDay("Barbados Independence",        Month.NOV,  30);
        new HolidayMonthDay("Bastille Day",                 Month.JUL,  14);
        new HolidayMonthDay("Battle of Boyaca Day",         Month.AUG,   7);
        new HolidayMonthDay("Battle of Carabobo",           Month.JUN,  24);
        new HolidayMonthDay("Belgium National Day",         Month.JUL,  21);
        new HolidayMonthDay("Belize Emancipation",          Month.MAY,  24);
        new HolidayMonthDay("Belize Independence",          Month.SEP,  21);
        new HolidayWeekday("Bermuda Cup Match",             Month.JUL,  30,  Weekday.THU); // TODO: starting date
        new HolidayWeekday("Bermuda Labour Day",            Month.SEP,   1,  Weekday.MON);
        new HolidayWeekday("Bermuda Queen Day",             Month.JUN,  15,  Weekday.MON); // TODO: starting date
        new HolidayWeekday("Bermuda Somers Day",            Month.JUL,  31,  Weekday.FRI); // TODO: starting date
        new HolidayIslamic("Birth of the Prophet",           69);
        new HolidayMonthDay("Bolivia Independence",         Month.AUG,   6);
        new HolidayMonthDay("Bonifacio Day",                Month.NOV,  30);
        new HolidayMonthDay("Brazil Independence",          Month.SEP,   7);
        new HolidayMonthDay("Brazil Republic Day",          Month.NOV,  15);
        new HolidayMonthDay("Boxing Day",                   Month.DEC,  26);
        new HolidayMonthDay("Buddha Purnima",               Month.APR,  30);
        new HolidayLunar("Buddha Birthday",                  95);
        new HolidayWeekday("Canada Civic Day",              Month.AUG,   1,  Weekday.MON);
        new HolidayMonthDay("Canada Independence",          Month.JUL,   1);
        new HolidayWeekday("Canada Labour Day",             Month.SEP,   1,  Weekday.MON);
        new HolidayWeekday("Canada Thanksgiving Day",       Month.OCT,   8,  Weekday.MON);
        new HolidayEaster("Carnival Monday",                -48);
        new HolidayEaster("Carnival Thursday",              -52);
        new HolidayEaster("Carnival Tuesday",               -47);
        new HolidayEaster("Carnival Wednesday",             -53);
        new HolidayWeekday("Cayman Constitution Day",       Month.JUL,   1,  Weekday.MON); // TODO: starting date
        new HolidayWeekday("Cayman Discovery Day",          Month.MAY,  15,  Weekday.MON); // TODO: starting date
        new HolidayWeekday("Cayman Queen Day",              Month.JUN,   8,  Weekday.MON); // TODO: starting date
        new HolidayMonthDay("Chile Army Day",               Month.SEP,  19);
        new HolidayMonthDay("Chile Commemoration Day",      Month.SEP,  11);
        new HolidayMonthDay("Chile Marine Day",             Month.MAY,  21);
        new HolidayMonthDay("Chile National Day",           Month.SEP,  18);
        new HolidayMonthDay("Chile Unity Day",              Month.SEP,   6);
        new HolidayMonthDay("Chinese National Day I",       Month.OCT,   1);
        new HolidayMonthDay("Chinese National Day II",      Month.OCT,   2);
        new HolidayMonthDay("Ching Ming Eve",               Month.APR,   5); // TODO: check this with next
        new HolidayMonthDay("Ching Ming Festival",          Month.APR,   6);
        new HolidayMonthDay("Christmas",                    Month.DEC,  25);
        new HolidayMonthDay("Christmas Eve",                Month.DEC,  24);
        new HolidayMonthDay("Christmas IV",                 Month.DEC,  28);
        new HolidayMonthDay("Christmas V",                  Month.DEC,  29);
        new HolidayMonthDay("Christmas VI",                 Month.DEC,  30);
        new HolidayMonthDay("Cinco De Mayo",                Month.MAY,   5);
        new HolidayMonthDay("Columbia Independence",        Month.JUL,  20);
        new HolidayMonthDay("Columbus Day",                 Month.OCT,  12);
        new HolidayMonthDay("Commonwealth Day",             Month.MAR,   9);
        new HolidayMonthDay("Confucius Birthday",           Month.SEP,  28);
        new HolidayEaster("Corpus Christi",                  60);
        new HolidayMonthDay("Costa Rica Independence",      Month.SEP,  15);
        new HolidayMonthDay("Cuencas Independence",         Month.NOV,   3);
        new HolidayMonthDay("Czech Liberation",             Month.MAY,   8);
        new HolidayMonthDay("Czech National Day",           Month.OCT,  28);
        new HolidayMonthDay("December Bank Holiday",        Month.DEC,   1);
        new HolidayMonthDay("Denmark National Day",         Month.JUN,   5);
        new HolidayMonthDay("Defenders of Motherland",      Month.FEB,  23);
        new HolidayMonthDay("Diwali",                       Month.NOV,   7);
        new HolidayMonthDay("Dr. Sun Yat Sen Birthday",     Month.NOV,  12);
        new HolidayLunar("Dragon Boat Day",                 122);
        new HolidayMonthDay("Dussehra",                     Month.OCT,  19);
        new HolidayEaster("Easter Monday",                    1);
        new HolidayMonthDay("Ecuador Independence",         Month.AUG,  10);
        new HolidayWeekday("Ecuador Bank Holiday",          Month.JUN,  24,  Weekday.FRI);
        new HolidayIslamic("Eid Al Fitr",                   -89);
        new HolidayIslamic("Eid El Adha",                   -20);
        new HolidayMonthDay("Egypt Army Day",               Month.OCT,   6);
        new HolidayMonthDay("Egypt Liberation",             Month.OCT,  23);
        new HolidayMonthDay("Egypt National Day",           Month.JUL,  23);
        new HolidayMonthDay("Egypt Union Day",              Month.FEB,  22);
        new HolidayMonthDay("Egypt Victory Day",            Month.DEC,  23);
        new HolidayMonthDay("El Salvador Independence",     Month.SEP,  15);
        new HolidayWeekday("Election Day",                  Month.NOV,   2,  Weekday.TUE);
        new HolidayMonthDay("Emancipation Day",             Month.AUG,   1);
        new HolidayWeekday("Emancipation Monday",           Month.AUG,   1,  Weekday.MON);
        new HolidayMonthDay("Epiphany",                     Month.JAN,   6);
        new HolidayWeekday("Epiphany Monday",               Month.JAN,   2,  Weekday.MON);
        new HolidayMonthDay("Elevation",                    Month.SEP,  14);
        new HolidayMonthDay("Errol Barrow Day",             Month.JAN,  21);
        new HolidayMonthDay("Fasting Day",                  Month.FEB,  22);
        new HolidayMonthDay("Fete De La Victoire",          Month.MAY,   8);
        new HolidayMonthDay("Finland Independence",         Month.DEC,   6);
        new HolidayMonthDay("Foundation Day",               Month.JUN,   7);
        new HolidayMonthDay("Fourth Of July",               Month.JUL,   4);
        new HolidayMonthDay("Friendship Day",               Month.SEP,  20);
        new HolidayMonthDay("Garifuna Day",                 Month.NOV,  19);
        new HolidayWeekday("General San Martin Memorial",   Month.AUG,  13,  Weekday.MON); // TODO: starting date
        new HolidayMonthDay("German Unity Day",             Month.OCT,   3);
        new HolidayEaster("Good Friday",                    -2);
        new HolidayMonthDay("Greece National Day I",        Month.MAR,  25);
        new HolidayMonthDay("Greece National Day II",       Month.OCT,  28);
        new HolidayMonthDay("Guadalupes Day",               Month.DEC,  12);
        new HolidayMonthDay("Guayaquils Independence",      Month.OCT,   9);
        new HolidayMonthDay("Guru Nanak",                   Month.NOV,  23);
        new HolidayWeekday("Halloween Day",                 Month.OCT,  25,  Weekday.MON);
        new HolidayIslamic("Hari Raya Haji",                -20);
        new HolidayEaster("Holy Thursday",                  -3);
        new HolidayMonthDay("Honduras Army Day",            Month.OCT,  21);
        new HolidayMonthDay("Honduras Independence",        Month.SEP,  15);
        new HolidayMonthDay("Hungary Constitution Day",     Month.AUG,  20);
        new HolidayMonthDay("Hungary National Day",         Month.MAR,  15);
        new HolidayMonthDay("Hungary Republic Day",         Month.OCT,  23);
        new HolidayWeekday("Iceland Commerce Day",          Month.AUG,   1,  Weekday.MON);
        new HolidayMonthDay("Iceland National Day",         Month.JUL,  17);
        new HolidayMonthDay("Immaculate Conception",        Month.DEC,   8);
        new HolidayMonthDay("Inauguration Day",             Month.JAN,  20,  1937, 3000, 4);
        new HolidayMonthDay("India Independence",           Month.AUG,  15);
        new HolidayMonthDay("India Republic Day",           Month.JAN,  26);
        new HolidayMonthDay("Indonesia Independence",       Month.AUG,  17);
        new HolidayIslamic("Islamic New Year",               0);
        new HolidayMonthDay("Italy Liberation Day",         Month.APR,  25);
        new HolidayMonthDay("Jan Hus",                      Month.JUL,   6); // TODO: sometimes june?
        new HolidayMonthDay("Japan Bank Holiday",           Month.MAY,   4);
        new HolidayMonthDay("Japan Children Day",           Month.MAY,   5);
        new HolidayMonthDay("Japan Coming of Age Day",      Month.JAN,  15);
        new HolidayMonthDay("Japan Culture Day",            Month.NOV,   3);
        new HolidayMonthDay("Japan Emperor Birthday",       Month.DEC,  23);
        new HolidayMonthDay("Japan Foundation Day",         Month.FEB,  11);
        new HolidayMonthDay("Japan Greenery Day",           Month.APR,  29);
        new HolidayMonthDay("Japan Sports Day",             Month.OCT,  10);
        new HolidayMonthDay("Japan Marine Day",             Month.JUL,  20);
        new HolidayMonthDay("Japan Memorial Day",           Month.MAY,   3);
        new HolidayMonthDay("Japan Respect Aged Day",       Month.SEP,  15);
        new HolidayMonthDay("Japan Thanksgiving Day",       Month.NOV,  23);
        new HolidayMonthDay("Juarez Birthday",              Month.MAR,  21);
        new HolidayWeekday("June Bank Holiday",             Month.JUN,   1,  Weekday.MON);
        new HolidayWeekday("Kadooment Day",                 Month.AUG,   2,  Weekday.MON); // TODO: start date?
        new HolidayMonthDay("King Chulalongkorn Memorial",  Month.OCT,  23);
        new HolidayMonthDay("Kuwait Liberation Day",        Month.FEB,  26);
        new HolidayMonthDay("Kuwait National Day",          Month.FEB,  25);
        new HolidayMonthDay("La Paz",                       Month.JUL,  16);
        new HolidayWeekday("Labour Day",                    Month.OCT,   1,  Weekday.MON);
        new HolidayLunar("Lunar New Year Eve",              -1);
        new HolidayLunar("Lunar New Year",                   0);
        new HolidayLunar("Lunar New Year II",                1);
        new HolidayLunar("Lunar New Year III",               2);
        new HolidayMonthDay("Luxemburg National Day",       Month.JUN,  23);
        new HolidayMonthDay("Mahatma Gandhis Birthday",     Month.OCT,   2);
        new HolidayMonthDay("Mahavir Jayanti",              Month.MAR,  29);
        new HolidayMonthDay("Malaysia Federal Territory",   Month.FEB,   1);
        new HolidayMonthDay("Malaysia National Day",        Month.AUG,  31);
        new HolidayWeekday("Martin Luther King",            Month.JAN,  15,  Weekday.MON);
        new HolidayMonthDay("Martyrs Day",                  Month.JAN,   9);
        new HolidayMonthDay("May Day",                      Month.MAY,   1);
        new HolidayMonthDay("May Day Eve",                  Month.APR,  30);
        new HolidayWeekday("May Monday",                    Month.MAY,   1,  Weekday.MON);
        new HolidayMonthDay("May Labour Day",               Month.MAY,   1);
        new HolidayMonthDay("May Labour Day II",            Month.MAY,   2);
        new HolidayMonthDay("Mexico Constitution Day",      Month.FEB,   5);
        new HolidayMonthDay("Mexico Independence",          Month.SEP,  16);
        new HolidayMonthDay("Mexico Revolution",            Month.NOV,  20);
        new HolidayLunar("Mid Autumn Festival",             220);
        new HolidayMonthDay("Mid Year Day",                 Month.JUL,   1);
        new HolidayWeekday("Midsummer",                     Month.JUN,  22,  Weekday.TUE); // TODO: start - saw 15th & 23rd (SEK)
        new HolidayWeekday("Midsummer Eve",                 Month.JUN,  21,  Weekday.MON); // TODO: ditto
        new HolidayMonthDay("Morazan Day",                  Month.OCT,   3);
        new HolidayMonthDay("Muharram",                     Month.APR,  27);
        new HolidayMonthDay("Muttons Feast",                Month.MAR,  28);
        new HolidayMonthDay("Nativity",                     Month.SEP,   8);
        new HolidayMonthDay("Netherland Liberation",        Month.MAY,   5);
        new HolidayMonthDay("Netherland Queen Birthday",    Month.APR,  30);
        new HolidayMonthDay("New Year Day",                 Month.JAN,   1);
        new HolidayMonthDay("New Year Day II",              Month.JAN,   2);
        new HolidayMonthDay("New Year Day III",             Month.JAN,   3);
        new HolidayMonthDay("New Year Eve",                 Month.DEC,  31);
        new HolidayMonthDay("New Year Eve Eve",             Month.DEC,  30);
        new HolidayWeekday("New Zealand Labour Day",        Month.OCT,  22,  Weekday.MON); // TODO: starting date
        new HolidayWeekday("New Zealand Queen Day",         Month.JUN,   1,  Weekday.MON); // TODO: starting date
        new HolidayMonthDay("Norway Constitution Day",      Month.MAY,  17);
        new HolidayMonthDay("Nossa Senhora",                Month.OCT,  12);
        new HolidayMonthDay("Orthodox Christmas",           Month.JAN,   7);
        new HolidayMonthDay("Orthodox Christmas Eve",       Month.JAN,   6);
        new HolidayMonthDay("Orthodox New Year",            Month.JAN,  14);
        new HolidayMonthDay("Our Lady of Seven Sorrows",    Month.SEP,  15);
        new HolidayMonthDay("Panama Announcement",          Month.NOV,  10);
        new HolidayMonthDay("Panama Break From Spain",      Month.NOV,  28);
        new HolidayMonthDay("Panama Independence I",        Month.NOV,   3);
        new HolidayMonthDay("Panama Independence II",       Month.NOV,   4);
        new HolidayMonthDay("Panama Mothers Day",           Month.DEC,   8);
        new HolidayMonthDay("Panamerican Day",              Month.APR,  14);
        new HolidayEaster("Pentecost",                       50);
        new HolidayMonthDay("Peru Independence I",          Month.JUL,  28);
        new HolidayMonthDay("Peru Independence II",         Month.JUL,  29);
        new HolidayMonthDay("Philippine Heroes Day I",      Month.APR,   9);
        new HolidayMonthDay("Philippine Heroes Day II",     Month.AUG,  27);
        new HolidayMonthDay("Philippine Heroes Day III",    Month.AUG,  31);
        new HolidayMonthDay("Philippine Independence",      Month.JUN,  12);
        new HolidayMonthDay("Pichincha Battle",             Month.MAY,  24);
        new HolidayMonthDay("Poland Constitution Day",      Month.MAY,   3); // TODO: is this 1st wednesday of each month
        new HolidayMonthDay("Poland Independence",          Month.NOV,  11);
        new HolidayMonthDay("Portugal Day",                 Month.JUN,  10);
        new HolidayMonthDay("Portugal Independence",        Month.DEC,   1);
        new HolidayMonthDay("Portugal Liberty Day",         Month.APR,  25);
        new HolidayMonthDay("Portugal Republic Day",        Month.OCT,   5);
        new HolidayEaster("Prayer Day",                      26);
        new HolidayWeekday("Presidents Day",                Month.FEB,  15,  Weekday.MON);
        new HolidayMonthDay("President Tchiang Kai Sek",    Month.OCT,  31);
        new HolidayWeekday("Queen Victoria Day",            Month.MAY,  18,  Weekday.MON);
        new HolidayMonthDay("Quitos Foundation",            Month.DEC,   6);
        new HolidayMonthDay("Remembrance Day",              Month.NOV,  11);
        new HolidayMonthDay("Russia Constitution Day",      Month.DEC,  12);
        new HolidayMonthDay("Russia National Day",          Month.JUN,  12);
        new HolidayMonthDay("Russia Revolution Day",        Month.NOV,   7);
        new HolidayMonthDay("Russia Victory Day",           Month.MAY,   9);
        new HolidayMonthDay("Russia Womans Day",            Month.MAR,   8);
        new HolidayMonthDay("San Jose",                     Month.MAR,  19);
        new HolidayMonthDay("Saudi Arabia Nature Day",      Month.APR,   1);
        new HolidayMonthDay("Saudi Arabia Nature Day II",   Month.APR,   2);
        new HolidayMonthDay("Saudi Arabia Unification",     Month.SEP,  24);
        new HolidayMonthDay("Sham El Nessim",               Month.APR,  12);
        new HolidayMonthDay("Shakri Dynasty Day",           Month.APR,   6);
        new HolidayMonthDay("Simon Bolivar Day",            Month.JUL,  24);
        new HolidayMonthDay("Sinai Day",                    Month.APR,  25);
        new HolidayMonthDay("Singapore National Day",       Month.AUG,   9);
        new HolidayMonthDay("Slovakia Constitution Day",    Month.SEP,   1);
        new HolidayMonthDay("Slovakia Liberation",          Month.MAY,   8);
        new HolidayMonthDay("Slovakia National Day",        Month.AUG,  29);
        new HolidayMonthDay("Songkran Festival I",          Month.APR,  12);
        new HolidayMonthDay("Songkran Festival II",         Month.APR,  13);
        new HolidayMonthDay("Songkran Festival III",        Month.APR,  14);
        new HolidayMonthDay("South Africa Freedom Day",     Month.APR,  27);
        new HolidayMonthDay("South Africa Heritage Day",    Month.SEP,  24);
        new HolidayMonthDay("South Africa Human Rights",    Month.MAR,  21);
        new HolidayMonthDay("South Africa Reconciliation",  Month.DEC,  16);
        new HolidayMonthDay("South Africa Womans Day",      Month.AUG,   9);
        new HolidayMonthDay("South Africa Youth Day",       Month.JUN,  16);
        new HolidayMonthDay("South Korean Constitution",    Month.JUL,  17);
        new HolidayMonthDay("South Korean Independence",    Month.MAR,  1);
        new HolidayMonthDay("South Korean Liberation",      Month.AUG,  15);
        new HolidayMonthDay("South Korean Memorial Day",    Month.JUN,   6);
        new HolidayMonthDay("South Korean National Day",    Month.OCT,   3);
        new HolidayMonthDay("Spain Constitution Day",       Month.DEC,   6);
        new HolidayMonthDay("Spain National Day",           Month.OCT,  12);
        new HolidayWeekday("Spring Bank Holiday",           Month.MAY,  25,  Weekday.MON);
        new HolidayMonthDay("St. Cyril & Methodius",        Month.JUL,   5); // TODO: sometimes june?
        new HolidayMonthDay("St. Georges Caye Day",         Month.SEP,  10);
        new HolidayMonthDay("St. Josephs Day",              Month.MAR,  19);
        new HolidayMonthDay("St. Patrick Day",              Month.MAR,  17);
        new HolidayMonthDay("St. Peter & St. Paul Day",     Month.JUN,  29);
        new HolidayMonthDay("St. Rose of Lima",             Month.AUG,  30);
        new HolidayMonthDay("St. Walpurgis Day",            Month.APR,  30);
        new HolidayMonthDay("Suez Victory Day",             Month.OCT,  24);
        new HolidayWeekday("Summer Bank Holiday",           Month.AUG,  25,  Weekday.MON);
        new HolidayMonthDay("Sweden Flag Day",              Month.JUN,   6);
        new HolidayMonthDay("Swiss National Day",           Month.AUG,   1);
        new HolidayMonthDay("Taiwan Constitution Day",      Month.DEC,  25);
        new HolidayMonthDay("Taiwan National Day",          Month.OCT,  10);
        new HolidayMonthDay("Taiwan Peace Day",             Month.FEB,  28);
        new HolidayMonthDay("Taiwan Retrocession",          Month.OCT,  25);
        new HolidayMonthDay("Taiwan Woman Day",             Month.APR,   4);
        new HolidayMonthDay("Taiwan Youth Day",             Month.MAR,  29);
        new HolidayMonthDay("Thailand Constitution Day",    Month.DEC,  10);
        new HolidayMonthDay("Thailand Crowning Day",        Month.MAY,   5);
        new HolidayMonthDay("Thailand King Day",            Month.DEC,   5);
        new HolidayMonthDay("Thailand Queen Day",           Month.AUG,  12);
        new HolidayWeekday("Thanksgiving Day",              Month.NOV,  22,  Weekday.THU);
        new HolidayWeekday("Thanksgiving Friday",           Month.NOV,  23,  Weekday.FRI);
        new HolidayMonthDay("Tiradentes",                   Month.APR,  21);
        new HolidayMonthDay("Transfiguration I",            Month.AUG,   4);
        new HolidayMonthDay("Transfiguration II",           Month.AUG,   5);
        new HolidayMonthDay("Transfiguration III",          Month.AUG,   6);
        new HolidayMonthDay("Turkey Children & Sovereign",  Month.APR,  23);
        new HolidayMonthDay("Turkey Republic Day",          Month.OCT,  29);
        new HolidayMonthDay("Turkey Victory Day",           Month.AUG,  30);
        new HolidayMonthDay("Turkey Youth & Sports",        Month.MAY,  19);
        new HolidayMonthDay("United Nation Day",            Month.OCT,  24);
        new HolidayWeekday("USA Columbus Day",              Month.OCT,   8,  Weekday.MON);
        new HolidayMonthDay("USA Flag Day",                 Month.JUN,   1);
        new HolidayWeekday("USA Labor Day",                 Month.SEP,   1,  Weekday.MON);
        new HolidayWeekday("USA Memorial Day",              Month.MAY,  25,  Weekday.MON);
        new HolidayMonthDay("USA Veterans Day",             Month.NOV,  11);
        new HolidayMonthDay("Venezuela Independence",       Month.JUL,   5);
        new HolidayMonthDay("Venezuela Independence II",    Month.APR,  19);
        new HolidayEquinox("Vernal Equinox",                true, 0);
        new HolidayEquinox("Vernal Equinox II",             true, 1);
        new HolidayMonthDay("Waitangi Day",                 Month.FEB,   6);
        new HolidayIslamic("Wesak Day",                      42);
        new HolidayMonthDay("Zimbabwe Armed Forces Day",    Month.AUG,  12);
        new HolidayMonthDay("Zimbabwe Heroes Day",          Month.AUG,  11);
        new HolidayMonthDay("Zimbabwe Independence",        Month.APR,  18);
        new HolidayMonthDay("Zimbabwe Public Holiday",      Month.AUG,  13);
        new HolidayMonthDay("Zimbabwe Unity Day",           Month.DEC,  22);
    }

    /**
     * Returns the class used to reference choices.
     * @return Choice reference class.
     */
    public static Class klass() {
        return RawHolidayChoice.class;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
// Field Variables & Constructor

    /**
     * Invalid constructor to choice list.
     */
    private RawHolidayChoiceImpl() {
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
// Get & Set Methods

    /**
     * Returns the choice list for this implementation.
     * @return Choice list.
     */
    public static ChoiceList choices() {
        return _choices;
    }
}
