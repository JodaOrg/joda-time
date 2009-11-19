package org.joda.time.gwt;

import org.joda.time.DateTimeZone;
import org.joda.time.gwt.tz.GwtZoneInfoProvider;
import org.joda.time.tz.Provider;

import com.google.gwt.junit.client.GWTTestCase;

public class JodaGwtTestCase extends GWTTestCase {
    
    private static Provider originalProvider;

    protected void gwtSetUp() throws Exception {
        originalProvider = DateTimeZone.getProvider();
        DateTimeZone.setProvider(new GwtZoneInfoProvider());
    }
    
    protected void gwtTearDown() throws Exception {
        DateTimeZone.setProvider(originalProvider);
    }

    @Override
    public String getModuleName() {
        return "org.joda.TimeTest";
    }

}
