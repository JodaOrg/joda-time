package org.joda.time.chrono;

import java.util.Locale;

/**
 * Created by Bishwash Adhikari on 08/12/2016.
 */
public class BISDateTimeField extends BasicMonthOfYearDateTimeField {

    private static String FULL_MONTHS[] = {"Baishak", "Jesth", "Ashar", "Shrawan", "Bhadra", "Ashoj", "Kartik", "Mangshir", "Poush", "Magh", "Falgun", "Chaitra"};
    private static String SHORT_MONTHS[] = {"BAI", "JES", "ASHA", "SHR", "BHA", "ASHO", "KAR", "MAN", "POU", "MAG", "FAL", "CHA"};

    /**
     * Restricted constructor.
     *
     * @param leapMonth the month of year that leaps
     */
    BISDateTimeField(BasicChronology chronology, int leapMonth) {
        super(chronology, leapMonth);
    }

    @Override
    public String getAsText(long instant, Locale locale) {
        String value = super.getAsText(instant, locale);
        return FULL_MONTHS[Integer.parseInt(value) - 1 ];
    }

    @Override
    public String getAsShortText(long instant, Locale locale) {
        String value = super.getAsText(instant, locale);
        return SHORT_MONTHS[Integer.parseInt(value) - 1 ];
    }
}
