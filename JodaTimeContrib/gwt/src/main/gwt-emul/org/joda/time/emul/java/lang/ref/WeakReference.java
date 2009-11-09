package java.lang.ref;

import java.util.Locale;

public class WeakReference {

    public WeakReference(Locale locale) {
        throw weakReferenceUnsupportedInGwt();
    }

    public Object get() {
        throw weakReferenceUnsupportedInGwt();
    }

    private static UnsupportedOperationException weakReferenceUnsupportedInGwt() {
        return new UnsupportedOperationException("WeakReference not supported in GWT");
    }
}
