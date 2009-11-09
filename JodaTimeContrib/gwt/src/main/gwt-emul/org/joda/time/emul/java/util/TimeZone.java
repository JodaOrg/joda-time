package java.util;

public class TimeZone {

    public static TimeZone getDefault() {
        throw timeZoneUnsupportedInGwt();
    }

    public String getDisplayName() {
        throw timeZoneUnsupportedInGwt();
    }

    public String getID() {
        throw timeZoneUnsupportedInGwt();
    }

    public int getRawOffset() {
        throw timeZoneUnsupportedInGwt();
    }

    public static TimeZone getTimeZone(String tzName) {
        throw timeZoneUnsupportedInGwt();
    }

    public boolean inDaylightTime(Date date) {
        throw timeZoneUnsupportedInGwt();
    }

    public static void setDefault(TimeZone toSet) {
        throw timeZoneUnsupportedInGwt();
    }
    
    private static UnsupportedOperationException timeZoneUnsupportedInGwt() {
        return new UnsupportedOperationException("TimeZone not supported in GWT");
    }

}
