package java.util;

public class Locale {

    public static final Locale ENGLISH = new Locale();
    public static final Locale FRENCH = new Locale();
    public static final Locale UK = new Locale();
    public static final Locale FRANCE = new Locale();
    public static final Locale KOREAN = new Locale();
    public static final Locale US = new Locale();
    
    public Locale() {
        throw localeUnsupportedInGwt();
    }

    public static Locale getDefault() {
        throw localeUnsupportedInGwt();
    }

    public String getLanguage() {
        throw localeUnsupportedInGwt();
    }

    public static void setDefault(Locale locale) {
        throw localeUnsupportedInGwt();
    }

    private static UnsupportedOperationException localeUnsupportedInGwt() {
        return new UnsupportedOperationException("Locale not supported in GWT");
    }

}
