package java.text;

import java.util.Locale;

public class DateFormatSymbols {

    public DateFormatSymbols(Locale locale) {
        throw dateFormatSymbolsUnsupportedInGwt();
    }

    public String[] getEras() {
        throw dateFormatSymbolsUnsupportedInGwt();
    }

    public String[] getWeekdays() {
        throw dateFormatSymbolsUnsupportedInGwt();
    }

    public String[] getShortWeekdays() {
        throw dateFormatSymbolsUnsupportedInGwt();
    }

    public String[] getMonths() {
        throw dateFormatSymbolsUnsupportedInGwt();
    }

    public String[] getShortMonths() {
        throw dateFormatSymbolsUnsupportedInGwt();
    }

    public String[] getAmPmStrings() {
        throw dateFormatSymbolsUnsupportedInGwt();
    }

    public String[][] getZoneStrings() {
        throw dateFormatSymbolsUnsupportedInGwt();
    }

    private static UnsupportedOperationException dateFormatSymbolsUnsupportedInGwt() {
        return new UnsupportedOperationException("DateFormatSymbols not supported in GWT");
    }
}
