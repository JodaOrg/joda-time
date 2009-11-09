package java.util;

public class GregorianCalendar extends Calendar {

    public GregorianCalendar(TimeZone timeZone) {
        throw gregorianCalendarUnsupportedInGwt();
    }

    public GregorianCalendar() {
        throw gregorianCalendarUnsupportedInGwt();
    }

    public GregorianCalendar(int i, int j, int k) {
        throw gregorianCalendarUnsupportedInGwt();
    }

    public GregorianCalendar(int i, int j, int k, int l, int m, int n) {
        throw gregorianCalendarUnsupportedInGwt();
    }

    public Date getGregorianChange() {
        throw gregorianCalendarUnsupportedInGwt();
    }

    public void setMinimalDaysInFirstWeek(int i) {
        throw gregorianCalendarUnsupportedInGwt();
    }

    public void setGregorianChange(Date date) {
        throw gregorianCalendarUnsupportedInGwt();
    }

    private static UnsupportedOperationException gregorianCalendarUnsupportedInGwt() {
        return new UnsupportedOperationException("GregorianCalendar not supported in GWT");
    }

}
