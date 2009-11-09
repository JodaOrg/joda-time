package java.util;

public class Calendar {

    public final static int YEAR = 1;
    public final static int MONTH = 2;
    public final static int WEEK_OF_YEAR = 3;
    public final static int DAY_OF_MONTH = 5;
    public final static int DAY_OF_YEAR = 6;
    public final static int DAY_OF_WEEK = 7;
    public final static int HOUR_OF_DAY = 11;
    public final static int MINUTE = 12;
    public final static int SECOND = 13;
    public final static int MILLISECOND = 14;

    public int get(int field) {
        throw calendarUnsupportedInGwt();
    }

    public static Calendar getInstance(TimeZone timeZone, Locale locale) {
        throw calendarUnsupportedInGwt();
    }

    public void setTime(Date date) {
        throw calendarUnsupportedInGwt();
    }

    public Date getTime() {
        throw calendarUnsupportedInGwt();
    }

    public TimeZone getTimeZone() {
        throw calendarUnsupportedInGwt();
    }

    public void setTimeZone(TimeZone timeZone) {
        throw calendarUnsupportedInGwt();
    }

    public static Calendar getInstance() {
        throw calendarUnsupportedInGwt();
    }

    public void set(int field, int i) {
        throw calendarUnsupportedInGwt();
    }

    private static UnsupportedOperationException calendarUnsupportedInGwt() {
        return new UnsupportedOperationException("Calendar not supported in GWT");
    }

}
