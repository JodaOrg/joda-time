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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * Private class to hold weekend rules with holiday.
 *
 * @author Scott R. Duchin
 */
class CcyHoli implements RawHolidayChoice {

    static final long serialVersionUID = 1001L;             // version 1 for serialization

    // private fields
    private RawHolidayChoice _rawHoli;   // raw holiday
    private int                         _sat;               // saturday rule (days adjustment)
    private int                         _sun;               // sunday rule (days adjustment)

    /**
     * Constructor.
     * @param raw Raw holiday.
     * @param sat Saturday adjustment.
     * @param sun Sunday adjustment.
     */
    CcyHoli(RawHolidayChoice raw, int sat, int sun) {
        _rawHoli = raw;
        _sat = sat;
        _sun = sun;
    }

    /**
     * Returns the interface associated with this choice.
     * @return The interface associated with this choice.
     */
    public Class choiceClass() {
        return _rawHoli.choiceClass();
    }

    /**
     * Returns the list containing this choice.
     * @return The list containing this choice.
     */
    public ChoiceList choiceList() {
        return _rawHoli.choiceList();
    }

    /**
     * Returns the name associated with this choice.
     * @return The name associated with this choice.
     */
    public String getLabel() {
        return _rawHoli.getLabel();
    }

    /**
     * Returns the date of the particular holiday for the specified year.
     * Note: the holiday returned is pure; not adjusted for Saturday or Sunday local behavior.
     * @param year Year the holiday is requested for.
     * @return Date of holiday in specified year; <code>null</code> if no holiday in specified year.
     */
    public Date date(/*year*/int year) {
        Date holi = _rawHoli.date(year);
        if (holi != null) {
            Weekday wkdy = holi.weekday();
            if (_sun != 0 && wkdy.equals(Weekday.SUN)) {
                holi = holi.addDays(_sun);
            } else if (_sat != 0 && wkdy.equals(Weekday.SAT)) {
                holi = holi.addDays(_sat);
            }
        }
        return holi;
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

///////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * The list of holidays per currency implementations.
 *
 * @author Scott R. Duchin
 */
public final class CurrencyHoliday {

    // static variables
    private static final Logger         _logger = Logger.getLogger("com.swvi.financial.instrument.CurrencyHoliday");

    /**
     * A map containing all currencies pointing to list of their corresponding holidays.
     */
    private static Map<String, Map<String, RawHolidayChoice>> _ccys =
      Collections.synchronizedSortedMap(new TreeMap<String, Map<String, RawHolidayChoice>>());

    /**
     * Choice list of the raw holidays.
     */
    private static ChoiceList _raws =
      ChoiceList.choiceList(RawHolidayChoiceImpl.klass());

    /**
     * Private constructor.
     */
    private CurrencyHoliday() {
    }

    /*
     * Variable representing that a Saturday holiday is observed on Saturday.
     */
    private static final int ON_SAT = 0;
    /*
     * Variable representing that a Sunday holiday is observed on Sunday.
     */
    private static final int ON_SUN = 0;
    /*
     * Variable representing that a Saturday holiday is observed on Friday.
     */
    private static final int SAT_FRI = -1;
    /*
     * Variable representing that a Saturday holiday is observed on Monday.
     */
    private static final int SAT_MON = 2;
    /*
     * Variable representing that a Sunday holiday is observed on Monday.
     */
    private static final int SUN_MON = 1;
    /*
     * Variable representing that a Sunday holiday is observed on Tuesday.
     */
    private static final int SUN_TUE = 2;

    /**
     * Static initializer.
     */
    static {
        // argentina peso
        addHoliday("ARS", "Argentina Flag Day");
        addHoliday("ARS", "Argentina Independence",         ON_SAT, ON_SUN);
        addHoliday("ARS", "Argentina May Revolution",       ON_SAT, ON_SUN);
        addHoliday("ARS", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("ARS", "Columbus Day",                   ON_SAT, ON_SUN);
        addHoliday("ARS", "Easter Monday");
        addHoliday("ARS", "General San Martin Memorial");
        addHoliday("ARS", "Good Friday");
        addHoliday("ARS", "Holy Thursday");
        addHoliday("ARS", "Immaculate Conception",          ON_SAT, ON_SUN);
        addHoliday("ARS", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("ARS", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("ARS", "New Year Eve",                   ON_SAT, ON_SUN);
//      ARS 06/14/1999 Malvinas Day
//      ARS 06/05/2000 Malvinas Day
//      ARS 06/04/2001 Malvinas Day

        // austria schilling
        addHoliday("ATS", "All Saints Day",                 ON_SAT, ON_SUN);
        addHoliday("ATS", "Ascension");
        addHoliday("ATS", "Assumption",                     ON_SAT, ON_SUN);
        addHoliday("ATS", "Austria National Day",           ON_SAT, ON_SUN);
        addHoliday("ATS", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("ATS", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("ATS", "Corpus Christi");
        addHoliday("ATS", "Easter Monday");
        addHoliday("ATS", "Epiphany",                       ON_SAT, ON_SUN);
//      addHoliday("ATS", "Good Friday");                                     // TODO: check if observed
        addHoliday("ATS", "Immaculate Conception",          ON_SAT, ON_SUN);
        addHoliday("ATS", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("ATS", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("ATS", "Pentecost");

        // australia dollar
        addHoliday("AUD", "Anzac Day",                      ON_SAT, ON_SUN);
        addHoliday("AUD", "Australia Day",                  ON_SAT, ON_SUN);
        addHoliday("AUD", "Australia Labour Day",           ON_SAT, ON_SUN);
        addHoliday("AUD", "Australia Queen Day");
        addHoliday("AUD", "Autumn Bank Holiday");
        addHoliday("AUD", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("AUD", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("AUD", "Easter Monday");
        addHoliday("AUD", "Good Friday");
//      addHoliday("AUD", "May Monday");                                      // TODO: check if observed
        addHoliday("AUD", "New Year Day",                   SAT_MON, SUN_MON);

        // barbados dollar
        addHoliday("BBD", "Barbados Heroes Day",            ON_SAT, ON_SUN);
        addHoliday("BBD", "Barbados Independence",          ON_SAT, ON_SUN);
        addHoliday("BBD", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("BBD", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("BBD", "December Bank Holiday",          ON_SAT, ON_SUN);
        addHoliday("BBD", "Errol Barrow Day",               ON_SAT, ON_SUN);
        addHoliday("BBD", "Emancipation Day",               ON_SAT, ON_SUN);
        addHoliday("BBD", "Good Friday");
        addHoliday("BBD", "Kadooment Day",                  ON_SAT, ON_SUN);
        addHoliday("BBD", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("BBD", "New Year Day",                   ON_SAT, ON_SUN);

        // belgium franc
        addHoliday("BEF", "All Saints Day",                 ON_SAT, ON_SUN);
        addHoliday("BEF", "Armistice Day",                  ON_SAT, ON_SUN);
        addHoliday("BEF", "Ascension");
        addHoliday("BEF", "Assumption",                     ON_SAT, ON_SUN);
        addHoliday("BEF", "Belgium National Day",           ON_SAT, ON_SUN);
        addHoliday("BEF", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("BEF", "Easter Monday");
        addHoliday("BEF", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("BEF", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("BEF", "Pentecost");

        // bermuda dollar
        addHoliday("BMD", "Bermuda Cup Match");
        addHoliday("BMD", "Bermuda Labour Day");
        addHoliday("BMD", "Bermuda Queen Day");
        addHoliday("BMD", "Bermuda Somers Day");
        addHoliday("BMD", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("BMD", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("BMD", "Christmas Eve",                  ON_SAT, ON_SUN);
        addHoliday("BMD", "Good Friday");
        addHoliday("BMD", "Remembrance Day",                ON_SAT, ON_SUN);

        // bolivia boliviano
        addHoliday("BOB", "All Saints Day",                 ON_SAT, ON_SUN);
        addHoliday("BOB", "Bolivia Independence",           ON_SAT, ON_SUN);
        addHoliday("BOB", "Carnival Thursday");
        addHoliday("BOB", "Carnival Wednesday");
        addHoliday("BOB", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("BOB", "Corpus Christi");
        addHoliday("BOB", "Good Friday");
        addHoliday("BOB", "La Paz",                         ON_SAT, ON_SUN);
        addHoliday("BOB", "New Year Day",                   ON_SAT, ON_SUN);

        // brazil real
        addHoliday("BRL", "All Souls Day",                  ON_SAT, ON_SUN);
        addHoliday("BRL", "Brazil Independence",            ON_SAT, ON_SUN);
        addHoliday("BRL", "Carnival Monday");
        addHoliday("BRL", "Carnival Tuesday");
        addHoliday("BRL", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("BRL", "Corpus Christi");
        addHoliday("BRL", "Good Friday");
        addHoliday("BRL", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("BRL", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("BRL", "Nossa Senhora",                  ON_SAT, ON_SUN);
        addHoliday("BRL", "Brazil Republic Day",            ON_SAT, ON_SUN);
        addHoliday("BRL", "Tiradentes",                     ON_SAT, ON_SUN);

        // bahamas dollar
        addHoliday("BSD", "Bahamas Independence",           ON_SAT, ON_SUN);
        addHoliday("BSD", "Bahamas Labour Day",             ON_SAT, ON_SUN);
        addHoliday("BSD", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("BSD", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("BSD", "Columbus Day",                   ON_SAT, ON_SUN);
        addHoliday("BSD", "Emancipation Monday");
        addHoliday("BSD", "New Year Day",                   ON_SAT, ON_SUN);

        // belize dollar
        addHoliday("BZD", "Belize Emancipation",            ON_SAT, ON_SUN);
        addHoliday("BZD", "Belize Independence",            ON_SAT, ON_SUN);
        addHoliday("BZD", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("BZD", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("BZD", "Commonwealth Day",               ON_SAT, ON_SUN);
        addHoliday("BZD", "December Bank Holiday",          ON_SAT, ON_SUN);
        addHoliday("BZD", "Garifuna Day",                   ON_SAT, ON_SUN);
        addHoliday("BZD", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("BZD", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("BZD", "St. Georges Caye Day",           ON_SAT, ON_SUN);

        // canada dollar
        addHoliday("CAD", "Boxing Day",                     SAT_MON, SUN_MON);
        addHoliday("CAD", "Canada Independence",            SAT_MON, SUN_MON);
        addHoliday("CAD", "Canada Labour Day");
        addHoliday("CAD", "Canada Thanksgiving Day");
        addHoliday("CAD", "Christmas",                      SAT_FRI, SUN_TUE); // skip over boxing day on monday
        addHoliday("CAD", "Easter Monday");
        addHoliday("CAD", "Good Friday");
        addHoliday("CAD", "New Year Day",                   ON_SAT, ON_SUN); // TODO: check this
        addHoliday("CAD", "Remembrance Day",                ON_SAT, ON_SUN);
        addHoliday("CAD", "Queen Victoria Day");

        // switzerland franc
        addHoliday("CHF", "Ascension");
        addHoliday("CHF", "Assumption",                     ON_SAT, ON_SUN);
        addHoliday("CHF", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("CHF", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("CHF", "Easter Monday");
        addHoliday("CHF", "Good Friday");
        addHoliday("CHF", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("CHF", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("CHF", "New Year Day II",                ON_SAT, ON_SUN);
        addHoliday("CHF", "New Year Eve",                   ON_SAT, ON_SUN);
        addHoliday("CHF", "Pentecost");
        addHoliday("CHF", "Swiss National Day",             ON_SAT, ON_SUN);

        // chile peso
        addHoliday("CLP", "All Saints Day",                 ON_SAT, ON_SUN);
        addHoliday("CLP", "Assumption",                     ON_SAT, ON_SUN);
        addHoliday("CLP", "Chile Army Day",                 ON_SAT, ON_SUN);
        addHoliday("CLP", "Chile Commemoration Day",        ON_SAT, ON_SUN);
        addHoliday("CLP", "Chile Marine Day",               ON_SAT, ON_SUN);
        addHoliday("CLP", "Chile National Day",             ON_SAT, ON_SUN);
        addHoliday("CLP", "Chile Unity Day",                ON_SAT, ON_SUN);
        addHoliday("CLP", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("CLP", "Columbus Day",                   ON_SAT, ON_SUN);
        addHoliday("CLP", "Corpus Christi");
        addHoliday("CLP", "Good Friday");
        addHoliday("CLP", "Immaculate Conception",          ON_SAT, ON_SUN);
        addHoliday("CLP", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("CLP", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("CLP", "New Year Eve",                   ON_SAT, ON_SUN);

        // columbia peso
        addHoliday("COP", "All Saints Day",                 ON_SAT, ON_SUN);
        addHoliday("COP", "Ascension");
        addHoliday("COP", "Assumption",                     ON_SAT, ON_SUN);
        addHoliday("COP", "Battle of Boyaca Day",           ON_SAT, ON_SUN);
        addHoliday("COP", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("COP", "Columbia Independence",          ON_SAT, ON_SUN);
        addHoliday("COP", "Columbus Day",                   ON_SAT, ON_SUN);
        addHoliday("COP", "Corpus Christi");
        addHoliday("COP", "Epiphany",                       ON_SAT, ON_SUN);
        addHoliday("COP", "Friendship Day",                 ON_SAT, ON_SUN);
        addHoliday("COP", "Good Friday");
        addHoliday("COP", "Holy Thursday");
        addHoliday("COP", "Immaculate Conception",          ON_SAT, ON_SUN);
        addHoliday("COP", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("COP", "New Year Day",                   ON_SAT, ON_SUN); // TODO: check this
        addHoliday("COP", "St. Josephs Day",                ON_SAT, ON_SUN);
        addHoliday("COP", "St. Peter & St. Paul Day",       ON_SAT, ON_SUN);

        // costa rica colon
        addHoliday("CRC", "Assumption",                     ON_SAT, ON_SUN);
        addHoliday("CRC", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("CRC", "Christmas VI",                   ON_SAT, ON_SUN);
        addHoliday("CRC", "Christmas V",                    ON_SAT, ON_SUN);
        addHoliday("CRC", "Christmas VI",                   ON_SAT, ON_SUN);
        addHoliday("CRC", "Columbus Day",                   ON_SAT, ON_SUN);
        addHoliday("CRC", "Costa Rica Independence",        ON_SAT, ON_SUN);
        addHoliday("CRC", "Good Friday");
        addHoliday("CRC", "Holy Thursday");
        addHoliday("CRC", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("CRC", "New Year Eve",                   ON_SAT, ON_SUN);
        addHoliday("CRC", "May Labour Day",                 ON_SAT, ON_SUN);

        // czech koruna
        addHoliday("CZK", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("CZK", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("CZK", "Czech Liberation",               ON_SAT, ON_SUN);
        addHoliday("CZK", "Czech National Day",             ON_SAT, ON_SUN);
        addHoliday("CZK", "Easter Monday");
        addHoliday("CZK", "Jan Hus",                        ON_SAT, ON_SUN);
        addHoliday("CZK", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("CZK", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("CZK", "St. Cyril & Methodius",          ON_SAT, ON_SUN);

        // germany mark
        addHoliday("DEM", "Ascension");
        addHoliday("DEM", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("DEM", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("DEM", "Easter Monday");
        addHoliday("DEM", "German Unity Day",               ON_SAT, ON_SUN);
        addHoliday("DEM", "Good Friday");
        addHoliday("DEM", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("DEM", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("DEM", "Pentecost");

        // denmark krone
        addHoliday("DKK", "Ascension");
        addHoliday("DKK", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("DKK", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("DKK", "Christmas Eve",                  ON_SAT, ON_SUN);
        addHoliday("DKK", "Denmark National Day",           ON_SAT, ON_SUN);
        addHoliday("DKK", "Easter Monday");
        addHoliday("DKK", "Good Friday");
        addHoliday("DKK", "Holy Thursday");
        addHoliday("DKK", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("DKK", "Pentecost");
        addHoliday("DKK", "Prayer Day");

        // ecuador sucre
        addHoliday("ECS", "All Souls Day",                  ON_SAT, ON_SUN);
        addHoliday("ECS", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("ECS", "Columbus Day",                   ON_SAT, ON_SUN);
        addHoliday("ECS", "Cuencas Independence",           ON_SAT, ON_SUN);
        addHoliday("ECS", "Ecuador Bank Holiday");
        addHoliday("ECS", "Ecuador Independence",           ON_SAT, ON_SUN);
        addHoliday("ECS", "Guayaquils Independence",        ON_SAT, ON_SUN);
        addHoliday("ECS", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("ECS", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("ECS", "Pichincha Battle",               ON_SAT, ON_SUN);
        addHoliday("ECS", "Quitos Foundation",              ON_SAT, ON_SUN);
        addHoliday("ECS", "Simon Bolivar Day",              ON_SAT, ON_SUN);

        // egypt pound
        addHoliday("EGP", "Birth of the Prophet",           ON_SAT, ON_SUN);
        addHoliday("EGP", "Egypt Army Day",                 ON_SAT, ON_SUN);
        addHoliday("EGP", "Egypt Liberation",               ON_SAT, ON_SUN);
        addHoliday("EGP", "Egypt National Day",             ON_SAT, ON_SUN);
        addHoliday("EGP", "Egypt Union Day",                ON_SAT, ON_SUN);
        addHoliday("EGP", "Egypt Victory Day",              ON_SAT, ON_SUN);
        addHoliday("EGP", "Eid Al Fitr",                    ON_SAT, ON_SUN);
        addHoliday("EGP", "Eid El Adha",                    ON_SAT, ON_SUN);
        addHoliday("EGP", "Islamic New Year",               ON_SAT, ON_SUN);
        addHoliday("EGP", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("EGP", "Mid Year Day",                   ON_SAT, ON_SUN);
        addHoliday("EGP", "Sham El Nessim",                 ON_SAT, ON_SUN);
        addHoliday("EGP", "Sinai Day",                      ON_SAT, ON_SUN);
        addHoliday("EGP", "Suez Victory Day",               ON_SAT, ON_SUN);

        // spain peseta
        addHoliday("ESP", "All Saints Day",                 ON_SAT, ON_SUN);
        addHoliday("ESP", "Assumption",                     ON_SAT, ON_SUN);
        addHoliday("ESP", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("ESP", "Easter Monday");
        addHoliday("ESP", "Epiphany",                       ON_SAT, ON_SUN);
        addHoliday("ESP", "Holy Thursday");
        addHoliday("ESP", "Immaculate Conception",          ON_SAT, ON_SUN);
        addHoliday("ESP", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("ESP", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("ESP", "Spain Constitution Day",         ON_SAT, ON_SUN);
        addHoliday("ESP", "Spain National Day",             ON_SAT, ON_SUN);

        // euro
        addHoliday("EUR", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("EUR", "New Year Day",                   ON_SAT, ON_SUN);

        // finland markka
        addHoliday("FIM", "Ascension");
        addHoliday("FIM", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("FIM", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("FIM", "Christmas Eve",                  ON_SAT, ON_SUN);
        addHoliday("FIM", "Easter Monday");
        addHoliday("FIM", "Epiphany",                       ON_SAT, ON_SUN);
        addHoliday("FIM", "Finland Independence",           ON_SAT, ON_SUN);
        addHoliday("FIM", "Good Friday");
        addHoliday("FIM", "May Day",                        ON_SAT, ON_SUN);
        addHoliday("FIM", "May Day Eve",                    ON_SAT, ON_SUN);
        addHoliday("FIM", "Midsummer",                      ON_SAT, ON_SUN);
        addHoliday("FIM", "Midsummer Eve",                  ON_SAT, ON_SUN);
        addHoliday("FIM", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("FIM", "Pentecost");

        // france franc
        addHoliday("FRF", "All Saints Day",                 ON_SAT, ON_SUN);
        addHoliday("FRF", "Armistice Day",                  ON_SAT, ON_SUN);
        addHoliday("FRF", "Ascension");
        addHoliday("FRF", "Assumption",                     ON_SAT, ON_SUN);
        addHoliday("FRF", "Bastille Day",                   ON_SAT, ON_SUN);
        addHoliday("FRF", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("FRF", "Easter Monday");
        addHoliday("FRF", "Fete De La Victoire",            ON_SAT, ON_SUN);
//      addHoliday("FRF", "Good Friday");                                    // TODO: check if observed
        addHoliday("FRF", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("FRF", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("FRF", "Pentecost");

        // great britain pound
        addHoliday("GBP", "Boxing Day",                     SAT_MON, SUN_MON);
        addHoliday("GBP", "Christmas",                      SAT_FRI, SUN_TUE); // skip over boxing day on monday
        addHoliday("GBP", "Easter Monday");
        addHoliday("GBP", "Good Friday");
        addHoliday("GBP", "May Monday");
        addHoliday("GBP", "New Year Day",                   SAT_MON, SUN_MON);
        addHoliday("GBP", "New Year Eve",                   SAT_FRI, SUN_TUE);
        addHoliday("GBP", "Spring Bank Holiday");
        addHoliday("GBP", "Summer Bank Holiday");

        // greece drachma
        addHoliday("GRD", "Assumption",                     ON_SAT, ON_SUN);
        addHoliday("GRD", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("GRD", "Epiphany",                       ON_SAT, ON_SUN);
        addHoliday("GRD", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("GRD", "Good Friday");
        addHoliday("GRD", "Fasting Day",                    ON_SAT, ON_SUN);
        addHoliday("GRD", "Greece National Day I",          ON_SAT, ON_SUN);
        addHoliday("GRD", "Greece National Day II",         ON_SAT, ON_SUN);
        addHoliday("GRD", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("GRD", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("GRD", "Pentecost");
//      GRD 04/11/1999 Orthodox Easter
//      GRD 04/12/1999 Orthodox Easter Monday
//      GRD 04/11/2000 Orthodox Easter
//      GRD 04/12/2000 Orthodox Easter Monday
//      GRD 04/15/2001 Orthodox Easter
//      GRD 04/16/2001 Orthodox Easter Monday

        // hong kong dollar
        addHoliday("HKD", "Boxing Day",                     ON_SAT, SUN_MON);
        addHoliday("KRW", "Buddha Birthday",                ON_SAT, ON_SUN);
        addHoliday("HKD", "Chinese National Day I",         ON_SAT, SUN_TUE);
        addHoliday("HKD", "Chinese National Day II",        ON_SAT, SUN_MON);
        addHoliday("HKD", "Ching Ming Festival",            ON_SAT, ON_SUN);
        addHoliday("HKD", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("HKD", "Easter Monday");
        addHoliday("HKD", "Good Friday");
        addHoliday("HKD", "Lunar New Year",                 ON_SAT, ON_SUN);
        addHoliday("HKD", "Lunar New Year II",              ON_SAT, ON_SUN);
        addHoliday("HKD", "Lunar New Year III",             ON_SAT, ON_SUN);
        addHoliday("HKD", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("TWD", "Mid Autumn Festival",            ON_SAT, ON_SUN);
        addHoliday("HKD", "Mid Year Day",                   ON_SAT, ON_SUN);
        addHoliday("HKD", "New Year Day",                   ON_SAT, ON_SUN);
//      HKD 06/18/1999 Tuen Ng Festival
//      HKD 08/16/1999 Sino-JapaneseWar Victory Day
//      HKD 10/18/1999 Chung Yeung Festival
//      HKD 06/06/2000 Tuen Ng Day
//      HKD 08/21/2000 Sino-Japanese War Victory Day
//      HKD 10/06/2000 Chung Yeung Day

        // honduras limpira
        addHoliday("HNL", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("HNL", "Columbus Day",                   ON_SAT, ON_SUN);
        addHoliday("HNL", "Honduras Army Day",              ON_SAT, ON_SUN);
        addHoliday("HNL", "Honduras Independence",          ON_SAT, ON_SUN);
        addHoliday("HNL", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("HNL", "Morazan Day",                    ON_SAT, ON_SUN);
        addHoliday("HNL", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("HNL", "Panamerican Day",                ON_SAT, ON_SUN);

        // hungary forint
        addHoliday("HUF", "Ascension");
        addHoliday("HUF", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("HUF", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("HUF", "Easter Monday");
        addHoliday("HUF", "Hungary Constitution Day",       ON_SAT, ON_SUN);
        addHoliday("HUF", "Hungary National Day",           ON_SAT, ON_SUN);
        addHoliday("HUF", "Hungary Republic Day",           ON_SAT, ON_SUN);
        addHoliday("HUF", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("HUF", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("HUF", "Pentecost");

        // indonesia rupiah
        addHoliday("IDR", "Ascension");
        addHoliday("IDR", "Birth of the Prophet",           ON_SAT, ON_SUN);
        addHoliday("IDR", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("IDR", "Good Friday");
        addHoliday("IDR", "Indonesia Independence",         ON_SAT, ON_SUN);
        addHoliday("IDR", "Islamic New Year",               ON_SAT, ON_SUN);
        addHoliday("IDR", "Muttons Feast",                  ON_SAT, ON_SUN);
        addHoliday("IDR", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("IDR", "Wesak Day",                      ON_SAT, ON_SUN);

        // ireland punt
        addHoliday("IEP", "Autumn Bank Holiday");
        addHoliday("IEP", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("IEP", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("IEP", "Easter Monday");
        addHoliday("IEP", "Good Friday");
        addHoliday("IEP", "Halloween Day");
        addHoliday("IEP", "June Bank Holiday");
        addHoliday("IEP", "May Monday");
        addHoliday("IEP", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("IEP", "St. Patrick Day",                ON_SAT, ON_SUN);

        // india rupee
        addHoliday("INR", "Buddha Purnima",                 ON_SAT, ON_SUN);
        addHoliday("INR", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("INR", "Diwali",                         ON_SAT, ON_SUN);
        addHoliday("INR", "Dussehra",                       ON_SAT, ON_SUN);
        addHoliday("INR", "Eid Al Fitr",                    ON_SAT, ON_SUN);
        addHoliday("INR", "Good Friday");
        addHoliday("INR", "Guru Nanak",                     ON_SAT, ON_SUN);
        addHoliday("INR", "India Independence",             ON_SAT, ON_SUN);
        addHoliday("INR", "India Republic Day",             ON_SAT, ON_SUN);
        addHoliday("INR", "Mahatma Gandhis Birthday",       ON_SAT, ON_SUN);
        addHoliday("INR", "Mahavir Jayanti",                ON_SAT, ON_SUN);
        addHoliday("INR", "Muharram",                       ON_SAT, ON_SUN);
        addHoliday("INR", "New Year Day",                   ON_SAT, ON_SUN);
//      INR 03/29/1999 Idul Zuha
//      INR 06/27/1999 Id-el-Milad

        // iceland krona
        addHoliday("ISK", "Ascension");
        addHoliday("ISK", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("ISK", "Easter Monday");
        addHoliday("ISK", "Good Friday");
        addHoliday("ISK", "Holy Thursday");
        addHoliday("ISK", "Iceland Commerce Day");
        addHoliday("ISK", "Iceland National Day",           ON_SAT, ON_SUN);
        addHoliday("ISK", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("ISK", "Pentecost");

        // italy lira
        addHoliday("ITL", "All Saints Day",                 ON_SAT, ON_SUN);
        addHoliday("ITL", "Assumption",                     ON_SAT, ON_SUN);
        addHoliday("ITL", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("ITL", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("ITL", "Easter Monday");
        addHoliday("ITL", "Epiphany",                       ON_SAT, ON_SUN);
        addHoliday("ITL", "Immaculate Conception",          ON_SAT, ON_SUN);
        addHoliday("ITL", "Italy Liberation Day",           ON_SAT, ON_SUN);
        addHoliday("ITL", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("ITL", "New Year Day",                   ON_SAT, ON_SUN);

        // japan yen -- since Saturday is Friday evening in the West and mostly worked, Saturdays do not roll but are taken directly
        addHoliday("JPY", "Autumnal Equinox",               ON_SAT, ON_SUN); // TODO: equinox algorithms not working yet
        addHoliday("JPY", "Japan Bank Holiday",             ON_SAT, ON_SUN);
        addHoliday("JPY", "Japan Children Day",             ON_SAT, ON_SUN);
        addHoliday("JPY", "Japan Coming of Age Day",        ON_SAT, ON_SUN);
        addHoliday("JPY", "Japan Culture Day",              ON_SAT, ON_SUN);
        addHoliday("JPY", "Japan Emperor Birthday",         ON_SAT, ON_SUN);
        addHoliday("JPY", "Japan Foundation Day",           ON_SAT, ON_SUN);
        addHoliday("JPY", "Japan Greenery Day",             ON_SAT, ON_SUN);
        addHoliday("JPY", "Japan Sports Day",               ON_SAT, SUN_MON);
        addHoliday("JPY", "Japan Marine Day",               ON_SAT, ON_SUN);
        addHoliday("JPY", "Japan Memorial Day",             ON_SAT, ON_SUN);
        addHoliday("JPY", "Japan Respect Aged Day",         ON_SAT, ON_SUN);
        addHoliday("JPY", "Japan Thanksgiving Day",         ON_SAT, ON_SUN);
        addHoliday("JPY", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("JPY", "New Year Day II",                ON_SAT, ON_SUN);
        addHoliday("JPY", "New Year Day III",               ON_SAT, ON_SUN);
        addHoliday("JPY", "New Year Eve",                   ON_SAT, ON_SUN);
        addHoliday("JPY", "Vernal Equinox",                 ON_SAT, ON_SUN); // TODO: equinox algorithms not working yet
        addHoliday("JPY", "Vernal Equinox II",              ON_SAT, ON_SUN); // TODO: equinox algorithms not working yet

        // south korea won
        addHoliday("KRW", "Buddha Birthday",                ON_SAT, ON_SUN);
        addHoliday("KRW", "Lunar New Year",                 ON_SAT, ON_SUN);
        addHoliday("KRW", "Lunar New Year II",              ON_SAT, ON_SUN);
        addHoliday("KRW", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("KRW", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("KRW", "New Year Day II",                ON_SAT, ON_SUN);
        addHoliday("KRW", "South Korean Constitution",      ON_SAT, ON_SUN);
        addHoliday("KRW", "South Korean Independence",      ON_SAT, ON_SUN);
        addHoliday("KRW", "South Korean Liberation",        ON_SAT, ON_SUN);
        addHoliday("KRW", "South Korean Memorial Day",      ON_SAT, ON_SUN);
        addHoliday("KRW", "South Korean National Day",      ON_SAT, ON_SUN);
//      KRW 09/23/1999 Harvest Moon Festival Eve
//      KRW 09/24/1999 Harvest Moon Festival Day
//      KRW 09/11/2000 Harvest Moon Festival Eve
//      KRW 09/12/2000 Harvest Moon Festival Day
//      KRW 09/13/2000 Harvest Moon Festival Holiday
//      KRW 09/30/2001 Harvest Moon Festival Eve
//      KRW 10/01/2001 Harvest Moon Festival Day
//      KRW 10/02/2001 Harvest Moon Festival Holiday

        // kuwait dinar
        addHoliday("KWD", "Birth of the Prophet",           ON_SAT, ON_SUN);
        addHoliday("KWD", "Eid Al Fitr",                    ON_SAT, ON_SUN);
        addHoliday("KWD", "Eid El Adha",                    ON_SAT, ON_SUN);
        addHoliday("KWD", "Islamic New Year",               ON_SAT, ON_SUN);
        addHoliday("KWD", "Kuwait Liberation Day",          ON_SAT, ON_SUN);
        addHoliday("KWD", "Kuwait National Day",            ON_SAT, ON_SUN);
        addHoliday("KWD", "New Year Day",                   ON_SAT, ON_SUN);

        // cayman islands dollar
        addHoliday("KYD", "Cayman Constitution Day");
        addHoliday("KYD", "Cayman Discovery Day");
        addHoliday("KYD", "Cayman Queen Day");
        addHoliday("KYD", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("KYD", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("KYD", "Christmas Eve",                  ON_SAT, ON_SUN);
        addHoliday("KYD", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("KYD", "Remembrance Day",                ON_SAT, ON_SUN);

        // luxemburg franc
        addHoliday("LUF", "All Saints Day",                 ON_SAT, ON_SUN);
        addHoliday("LUF", "Ascension");
        addHoliday("LUF", "Assumption",                     ON_SAT, ON_SUN);
        addHoliday("LUF", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("LUF", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("LUF", "Easter Monday");
        addHoliday("LUF", "Luxemburg National Day",         ON_SAT, ON_SUN);
        addHoliday("LUF", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("LUF", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("LUF", "Pentecost");

        // mexico peso
        addHoliday("MXN", "All Souls Day",                  ON_SAT, ON_SUN);
        addHoliday("MXN", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("MXN", "Cinco De Mayo",                  ON_SAT, ON_SUN);
        addHoliday("MXN", "Columbus Day",                   ON_SAT, ON_SUN);
        addHoliday("MXN", "Easter Monday");
        addHoliday("MXN", "Good Friday");
        addHoliday("MXN", "Guadalupes Day",                 ON_SAT, ON_SUN);
        addHoliday("MXN", "Holy Thursday");
        addHoliday("MXN", "Juarez Birthday",                ON_SAT, ON_SUN);
        addHoliday("MXN", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("MXN", "Mexico Constitution Day",        ON_SAT, ON_SUN);
        addHoliday("MXN", "Mexico Independence",            ON_SAT, ON_SUN);
        addHoliday("MXN", "Mexico Revolution",              ON_SAT, ON_SUN);
        addHoliday("MXN", "New Year Day",                   ON_SAT, ON_SUN); // TODO: check this

        // malaysia ringgit
        addHoliday("MYR", "Birth of the Prophet",           ON_SAT, ON_SUN);
        addHoliday("MYR", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("MYR", "Islamic New Year",               ON_SAT, ON_SUN);
        addHoliday("MYR", "Lunar New Year",                 ON_SAT, ON_SUN);
        addHoliday("MYR", "Lunar New Year II",              ON_SAT, ON_SUN);
        addHoliday("MYR", "Malaysia Federal Territory",     ON_SAT, ON_SUN);
        addHoliday("MYR", "Malaysia National Day",          ON_SAT, ON_SUN);
        addHoliday("MYR", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("MYR", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("MYR", "Wesak Day",                      ON_SAT, ON_SUN);
//      MYR 01/19/1999 Hari Raya Puasa
//      MYY 01/20/1999 Hari Raya Puasa
//      MYR 11/07/1999 Deepavally Day
//      MYR 01/08/2000 Hari Raya Puasa
//      MYR 01/09/2000 Hari Raya Puasa
//      MYR 10/26/2000 Deepavally Day
//      MYR 12/27/2000 Hari Raya Puasa
//      MYR 12/28/2000 Hari Raya Puasa
//      MYR 12/16/2001 Hari Raya Puasa
//      MYR 12/17/2001 Hari Raya Puasa

        // netherlands guilder
        addHoliday("NLG", "Ascension");
        addHoliday("NLG", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("NLG", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("NLG", "Easter Monday");
        addHoliday("NLG", "Good Friday");
        addHoliday("NLG", "Netherland Liberation",          ON_SAT, ON_SUN);
        addHoliday("NLG", "Netherland Queen Birthday",      ON_SAT, ON_SUN);
        addHoliday("NLG", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("NLG", "Pentecost");

        // norway krone
        addHoliday("NOK", "Ascension");
        addHoliday("NOK", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("NOK", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("NOK", "Easter Monday");
        addHoliday("NOK", "Good Friday");
        addHoliday("NOK", "Holy Thursday");
        addHoliday("NOK", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("NOK", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("NOK", "Norway Constitution Day",        ON_SAT, ON_SUN);
        addHoliday("NOK", "Pentecost");

        // new zealand dollar
        addHoliday("NZD", "Anzac Day",                      ON_SAT, ON_SUN);
        addHoliday("NZD", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("NZD", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("NZD", "Easter Monday");
        addHoliday("NZD", "Good Friday");
        addHoliday("NZD", "New Year Day",                   SAT_MON, SUN_TUE);
        addHoliday("NZD", "New Year Day II",                SAT_MON, SUN_TUE);
        addHoliday("NZD", "New Zealand Labour Day");
        addHoliday("NZD", "New Zealand Queen Day");
        addHoliday("NZD", "Waitangi Day",                   ON_SAT, ON_SUN);

        // panama balboa
        addHoliday("PAB", "Assumption",                     ON_SAT, ON_SUN);
        addHoliday("PAB", "Carnival Monday");
        addHoliday("PAB", "Carnival Tuesday");
        addHoliday("PAB", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("PAB", "Good Friday");
        addHoliday("PAB", "Martyrs Day",                    ON_SAT, ON_SUN);
        addHoliday("PAB", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("PAB", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("PAB", "Panama Announcement",            ON_SAT, ON_SUN);
        addHoliday("PAB", "Panama Break From Spain",        ON_SAT, ON_SUN);
        addHoliday("PAB", "Panama Independence I",          ON_SAT, ON_SUN);
        addHoliday("PAB", "Panama Independence II",         ON_SAT, ON_SUN);
        addHoliday("PAB", "Panama Mothers Day",             ON_SAT, ON_SUN);

        // peru sol
        addHoliday("PEN", "All Saints Day",                 ON_SAT, ON_SUN);
        addHoliday("PEN", "Angamos Battle",                 ON_SAT, ON_SUN);
        addHoliday("PEN", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("PEN", "Good Friday");
        addHoliday("PEN", "Holy Thursday");
        addHoliday("PEN", "Immaculate Conception",          ON_SAT, ON_SUN);
        addHoliday("PEN", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("PEN", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("PEN", "Peru Independence I",            ON_SAT, ON_SUN);
        addHoliday("PEN", "Peru Independence II",           ON_SAT, ON_SUN);
        addHoliday("PEN", "St. Peter & St. Paul Day",       ON_SAT, ON_SUN);
        addHoliday("PEN", "St. Rose of Lima",               ON_SAT, ON_SUN);

        // philippine peso
        addHoliday("PHP", "All Saints Day",                 ON_SAT, ON_SUN);
        addHoliday("PHP", "Bonifacio Day",                  ON_SAT, ON_SUN);
        addHoliday("PHP", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("PHP", "Good Friday");
        addHoliday("PHP", "Holy Thursday");
        addHoliday("PHP", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("PHP", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("PHP", "New Year Eve",                   ON_SAT, ON_SUN);
        addHoliday("PHP", "New Year Eve Eve",               ON_SAT, ON_SUN);
        addHoliday("PHP", "Philippine Heroes Day I",        ON_SAT, ON_SUN);
        addHoliday("PHP", "Philippine Heroes Day II",       ON_SAT, ON_SUN);
        addHoliday("PHP", "Philippine Heroes Day III",      ON_SAT, ON_SUN);
        addHoliday("PHP", "Philippine Independence",        ON_SAT, ON_SUN);

        // poland zloty
        addHoliday("PLN", "All Saints Day",                 ON_SAT, ON_SUN);
        addHoliday("PLN", "Assumption",                     ON_SAT, ON_SUN);
        addHoliday("PLN", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("PLN", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("PLN", "Corpus Christi");
        addHoliday("PLN", "Easter Monday");
        addHoliday("PLN", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("PLN", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("PLN", "Poland Constitution Day",        ON_SAT, ON_SUN);
        addHoliday("PLN", "Poland Independence",            ON_SAT, ON_SUN);

        // portugal escudo
        addHoliday("PTE", "All Saints Day",                 ON_SAT, ON_SUN);
        addHoliday("PTE", "Assumption",                     ON_SAT, ON_SUN);
        addHoliday("PTE", "Carnival Monday");                                // TODO: or tuesday
        addHoliday("PTE", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("PTE", "Christmas Eve",                  ON_SAT, ON_SUN);
        addHoliday("PTE", "Corpus Christi");
        addHoliday("PTE", "Good Friday");
        addHoliday("PTE", "Immaculate Conception",          ON_SAT, ON_SUN);
        addHoliday("PTE", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("PTE", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("PTE", "Portugal Day",                   ON_SAT, ON_SUN);
        addHoliday("PTE", "Portugal Independence",          ON_SAT, ON_SUN);
        addHoliday("PTE", "Portugal Liberty Day",           ON_SAT, ON_SUN);
        addHoliday("PTE", "Portugal Republic Day",          ON_SAT, ON_SUN);

        // russia ruble
        addHoliday("RUB", "Defenders of Motherland",        ON_SAT, ON_SUN);
        addHoliday("RUB", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("RUB", "May Labour Day II",              ON_SAT, ON_SUN);
        addHoliday("RUB", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("RUB", "New Year Day II",                ON_SAT, ON_SUN);
        addHoliday("RUB", "Orthodox Christmas",             ON_SAT, ON_SUN);
        addHoliday("RUB", "Russia Constitution Day",        ON_SAT, ON_SUN);
        addHoliday("RUB", "Russia National Day",            ON_SAT, ON_SUN);
        addHoliday("RUB", "Russia Revolution Day",          ON_SAT, ON_SUN);
        addHoliday("RUB", "Russia Victory Day",             ON_SAT, ON_SUN);
        addHoliday("RUB", "Russia Womans Day",              ON_SAT, ON_SUN);

        // saudi arabia riyal
        addHoliday("SAR", "Eid Al Fitr",                    ON_SAT, ON_SUN);
        addHoliday("SAR", "Eid El Adha",                    ON_SAT, ON_SUN);
        addHoliday("SAR", "Saudi Arabia Nature Day",        ON_SAT, ON_SUN);
        addHoliday("SAR", "Saudi Arabia Nature Day II",     ON_SAT, ON_SUN);
        addHoliday("SAR", "Saudi Arabia Unification",       ON_SAT, ON_SUN);

        // sweden krona
        addHoliday("SEK", "All Saints Day",                 ON_SAT, ON_SUN);
        addHoliday("SEK", "Ascension");
        addHoliday("SEK", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("SEK", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("SEK", "Christmas Eve",                  ON_SAT, ON_SUN);
        addHoliday("SEK", "Easter Monday");
        addHoliday("SEK", "Epiphany",                       ON_SAT, ON_SUN);
        addHoliday("SEK", "Good Friday");
        addHoliday("SEK", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("SEK", "Midsummer");
        addHoliday("SEK", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("SEK", "New Year Eve",                   ON_SAT, ON_SUN);
        addHoliday("SEK", "Pentecost");
        addHoliday("SEK", "St. Walpurgis Day",              ON_SAT, ON_SUN);
        addHoliday("SEK", "Sweden Flag Day",                ON_SAT, ON_SUN);

        // singapore dollar
        addHoliday("SGD", "Lunar New Year",                 ON_SAT, ON_SUN);
        addHoliday("SGD", "Lunar New Year II",              ON_SAT, ON_SUN);
        addHoliday("SGD", "Lunar New Year III",             ON_SAT, ON_SUN);
        addHoliday("SGD", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("SGD", "Good Friday");
        addHoliday("SGD", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("SGD", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("SGD", "Singapore National Day",         ON_SAT, ON_SUN);
        addHoliday("SGD", "Wesak Day",                      ON_SAT, ON_SUN);
//      SGD 01/19/1999 Hari Raya Puasa
//      SGD 03/28/1999 Hari Raya Haji
//      SGD 11/07/1999 Deepavali
//      SGD 01/08/2000 Hari Raya Puasa
//      SGD 03/16/2000 Hari Raya Haji
//      SGD 10/26/2000 Deepavali
//      SGD 12/27/2000 Hari Raya Puasa
//      SGD 03/06/2001 Hari Raya Haji
//      SGD 12/16/2001 Hari Raya Puasa

        // slovakia koruna
        addHoliday("SGD", "All Saints Day",                 ON_SAT, ON_SUN);
        addHoliday("SGD", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("SGD", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("SGD", "Christmas Eve",                  ON_SAT, ON_SUN);
        addHoliday("SGD", "Epiphany",                       ON_SAT, ON_SUN);
        addHoliday("SGD", "Good Friday");
        addHoliday("SGD", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("SGD", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("SGD", "Our Lady of Seven Sorrows",      ON_SAT, ON_SUN);
        addHoliday("SGD", "Slovakia Constitution Day",      ON_SAT, ON_SUN);
        addHoliday("SGD", "Slovakia Liberation",            ON_SAT, ON_SUN);
        addHoliday("SGD", "Slovakia National Day",          ON_SAT, ON_SUN);
        addHoliday("SGD", "St. Cyril & Methodius",          ON_SAT, ON_SUN);

        // el salvador colon
        addHoliday("SVC", "All Souls Day",                  ON_SAT, ON_SUN);
        addHoliday("SVC", "Bank Balance I",                 ON_SAT, ON_SUN);
        addHoliday("SVC", "Bank Balance II",                ON_SAT, ON_SUN);
        addHoliday("SVC", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("SVC", "Easter Monday");
        addHoliday("SVC", "El Salvador Independence",       ON_SAT, ON_SUN);
        addHoliday("SVC", "Good Friday");
        addHoliday("SVC", "Holy Thursday");
        addHoliday("SVC", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("SVC", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("SVC", "New Year Eve",                   ON_SAT, ON_SUN);
        addHoliday("SVC", "New Year Eve Eve",               ON_SAT, ON_SUN);
        addHoliday("SVC", "Transfiguration I",              ON_SAT, ON_SUN);
        addHoliday("SVC", "Transfiguration II",             ON_SAT, ON_SUN);
        addHoliday("SVC", "Transfiguration III",            ON_SAT, ON_SUN);

        // thailand baht
        addHoliday("THB", "King Chulalongkorn Memorial",    ON_SAT, ON_SUN);
        addHoliday("THB", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("THB", "Mid Year Day",                   ON_SAT, ON_SUN);
        addHoliday("THB", "New Year Day",                   SAT_MON, SUN_MON);
        addHoliday("THB", "New Year Eve",                   ON_SAT, ON_SUN);
        addHoliday("THB", "Shakri Dynasty Day",             ON_SAT, ON_SUN);
        addHoliday("THB", "Songkran Festival I",            ON_SAT, ON_SUN);
        addHoliday("THB", "Songkran Festival II",           ON_SAT, ON_SUN);
        addHoliday("THB", "Songkran Festival III",          ON_SAT, ON_SUN);
        addHoliday("THB", "Thailand Constitution Day",      SAT_MON, SUN_MON);
        addHoliday("THB", "Thailand Crowning Day",          ON_SAT, ON_SUN);
        addHoliday("THB", "Thailand King Day",              ON_SAT, ON_SUN);
        addHoliday("THB", "Thailand Queen Day",             ON_SAT, ON_SUN);
//      THB 01/01/1999 Makha Bucha Day (?)
//      THB 05/29/1999 Visakha Bucha Day
//      THB 07/28/1999 Buddisht Lent
//      THB 05/18/2000 Vishaka Bucha Day
//      THB 07/17/2000 Buddisht Lent

        // turkey lira
        addHoliday("TRL", "Eid Al Fitr",                    ON_SAT, ON_SUN);
        addHoliday("TRL", "Eid El Adha",                    ON_SAT, ON_SUN);
        addHoliday("TRL", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("TRL", "Turkey Children & Sovereign",    ON_SAT, ON_SUN);
        addHoliday("TRL", "Turkey Republic Day",            ON_SAT, ON_SUN);
        addHoliday("TRL", "Turkey Victory Day",             ON_SAT, ON_SUN);
        addHoliday("TRL", "Turkey Youth & Sports",          ON_SAT, ON_SUN);
//      TRL 03/16/2000 Day of the Sacrifice
//      TRL 03/17/2000 Day of the Sacrifice
//      TRL 03/18/2000 Day of the Sacrifice
//      TRL 03/19/2000 Day of the Sacrifice
//      TRL 03/05/2001 Day of the Sacrifice
//      TRL 03/06/2001 Day of the Sacrifice
//      TRL 03/07/2001 Day of the Sacrifice
//      TRL 03/08/2001 Day of the Sacrifice

        // taiwan dollar
        addHoliday("TWD", "Ching Ming Eve",                 ON_SAT, ON_SUN);
        addHoliday("TWD", "Confucius Birthday",             ON_SAT, ON_SUN);
        addHoliday("TWD", "Dragon Boat Day",                ON_SAT, ON_SUN);
        addHoliday("TWD", "Dr. Sun Yat Sen Birthday",       ON_SAT, ON_SUN);
        addHoliday("TWD", "Lunar New Year Eve",             ON_SAT, ON_SUN);
        addHoliday("TWD", "Lunar New Year",                 ON_SAT, ON_SUN);
        addHoliday("TWD", "Lunar New Year II",              ON_SAT, ON_SUN);
        addHoliday("TWD", "Lunar New Year III",             ON_SAT, ON_SUN);
        addHoliday("TWD", "Mid Autumn Festival",            ON_SAT, ON_SUN);
        addHoliday("TWD", "Mid Year Day",                   ON_SAT, ON_SUN);
        addHoliday("TWD", "New Year Day",                   SAT_MON, SUN_TUE);
        addHoliday("TWD", "New Year Day II",                SAT_MON, SUN_TUE);
        addHoliday("TWD", "President Tchiang Kai Sek",      ON_SAT, ON_SUN);
        addHoliday("TWD", "Taiwan Constitution Day",        ON_SAT, ON_SUN);
        addHoliday("TWD", "Taiwan National Day",            ON_SAT, ON_SUN);
        addHoliday("TWD", "Taiwan Peace Day",               ON_SAT, ON_SUN);
        addHoliday("TWD", "Taiwan Retrocession",            ON_SAT, ON_SUN);
        addHoliday("TWD", "Taiwan Woman Day",               ON_SAT, ON_SUN);
        addHoliday("TWD", "Taiwan Youth Day",               ON_SAT, ON_SUN);

        // united states dollar
        addHoliday("USD", "Christmas",                      SAT_FRI, SUN_MON);
        addHoliday("USD", "Fourth Of July",                 SAT_FRI, SUN_MON);
        addHoliday("USD", "Martin Luther King");
        addHoliday("USD", "New Year Day",                   SAT_FRI, SUN_MON);
        addHoliday("USD", "Presidents Day");
        addHoliday("USD", "Thanksgiving Day");
        addHoliday("USD", "USA Columbus Day");
        addHoliday("USD", "USA Labor Day");
        addHoliday("USD", "USA Memorial Day");
        addHoliday("USD", "USA Veterans Day",               SAT_FRI, SUN_MON); // TODO: unsure if this is a currency

        // venezuela bolivar
        addHoliday("VEB", "All Saints Day",                 ON_SAT, ON_SUN);
        addHoliday("VEB", "Ascension");
        addHoliday("VEB", "Assumption",                     ON_SAT, ON_SUN);
        addHoliday("VEB", "Battle of Carabobo",             ON_SAT, ON_SUN);
        addHoliday("VEB", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("VEB", "Columbus Day",                   ON_SAT, ON_SUN);
        addHoliday("VEB", "Epiphany Monday");
        addHoliday("VEB", "Immaculate Conception",          ON_SAT, ON_SUN);
        addHoliday("VEB", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("VEB", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("VEB", "San Jose",                       ON_SAT, ON_SUN);
        addHoliday("VEB", "St. Peter & St. Paul Day",       ON_SAT, ON_SUN);
        addHoliday("VEB", "Simon Bolivar Day",              ON_SAT, ON_SUN);
        addHoliday("VEB", "Venezuela Independence",         ON_SAT, ON_SUN);
        addHoliday("VEB", "Venezuela Independence II",      ON_SAT, ON_SUN);

        // south africa rand
        addHoliday("ZAR", "Ascension");
        addHoliday("ZAR", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("ZAR", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("ZAR", "Easter Monday");
        addHoliday("ZAR", "Good Friday");
        addHoliday("ZAR", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("ZAR", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("ZAR", "South Africa Freedom Day",       ON_SAT, ON_SUN);
        addHoliday("ZAR", "South Africa Heritage Day",      ON_SAT, ON_SUN);
        addHoliday("ZAR", "South Africa Human Rights",      ON_SAT, SUN_MON);
        addHoliday("ZAR", "South Africa Reconciliation",    ON_SAT, ON_SUN);
        addHoliday("ZAR", "South Africa Womans Day",        ON_SAT, ON_SUN);
        addHoliday("ZAR", "South Africa Youth Day",         ON_SAT, ON_SUN);

        // zimbabwe dollar
        addHoliday("ZWD", "Africa Day",                     ON_SAT, ON_SUN);
        addHoliday("ZWD", "Boxing Day",                     ON_SAT, ON_SUN);
        addHoliday("ZWD", "Christmas",                      ON_SAT, ON_SUN);
        addHoliday("ZWD", "May Labour Day",                 ON_SAT, ON_SUN);
        addHoliday("ZWD", "New Year Day",                   ON_SAT, ON_SUN);
        addHoliday("ZWD", "Zimbabwe Armed Forces Day",      ON_SAT, ON_SUN);
        addHoliday("ZWD", "Zimbabwe Heroes Day",            ON_SAT, ON_SUN);
        addHoliday("ZWD", "Zimbabwe Independence",          ON_SAT, ON_SUN);
        addHoliday("ZWD", "Zimbabwe Public Holiday",        ON_SAT, ON_SUN);
        addHoliday("ZWD", "Zimbabwe Unity Day",             ON_SAT, ON_SUN);
    }

    /**
     * Returns an iterator for all the currencies handled by this class.
     * @return Iterator of currency keys.
     */
    public static Iterator<String> currencies() {
        return _ccys.keySet().iterator();
    }

    /**
     * Returns an iterator for all the holidays for specified currency.
     * @param ccy Name of the currency.
     * @return Iterator of currency keys.
     */
    public static Iterator<RawHolidayChoice> holidays(String ccy) {
        Map<String, RawHolidayChoice> ccyMap = _ccys.get(ccy);
        return ccyMap.values().iterator();
    }

    /**
     * Prints holidays for currency between years specified, inclusive.
     * @param ccy   Name of the currency.
     * @param start Starting year.
     * @param end   Ending year.
     */
    public static void print(String ccy, /*year*/int start, /*year*/int end) {
        RawHolidayChoice holi;
        Date date;
        /*year*/int year;
        for (Iterator<RawHolidayChoice> ih = holidays(ccy); ih.hasNext();) {
            holi = ih.next();
            _logger.info("  " + holi.getLabel());
            for (year = start; year <= end; year++) {
                date = holi.date(year);
                if (date != null) {
                    _logger.info("    " + date);
                }
            }
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Stores a holiday for a currency on the currency's list.
     * @param ccy  Name of the currency.
     * @param holi Name of the holiday.
     */
    public static void addHoliday(String ccy, String holi) {
        RawHolidayChoice raw = (RawHolidayChoice) _raws.choice(holi);
        if (raw != null) {
            putHoliday(ccy, raw);
        }
    }

    /**
     * Stores a holiday for a currency on the currency's list.
     * @param ccy  Name of the currency.
     * @param holi Name of the holiday.
     * @param sat  Saturday adjustment.
     * @param sun  Sunday adjustment.
     */
    public static void addHoliday(String ccy, String holi, int sat, int sun) {
        if (sat == 0 && sun == 0) {
            addHoliday(ccy, holi);
        } else {
            RawHolidayChoice raw = (RawHolidayChoice) _raws.choice(holi);
            if (raw != null) {
                putHoliday(ccy, new CcyHoli(raw, sat, sun));
            }
        }
    }

    /**
     * Stores a holiday for a currency on the currency's list.
     * @param ccy Name of the currency.
     * @param raw Raw holiday object.
     */
    public static void putHoliday(String ccy, RawHolidayChoice raw) {
        Map<String, RawHolidayChoice> ccyMap = _ccys.get(ccy);
        if (ccyMap == null) {
            ccyMap = new TreeMap<String, RawHolidayChoice>();
            _ccys.put(ccy, ccyMap);
        }
        ccyMap.put(raw.getLabel(), raw);
    }
}
