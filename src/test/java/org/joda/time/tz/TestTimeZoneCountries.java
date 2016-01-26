package org.joda.time.tz;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTimeZone;

public class TestTimeZoneCountries extends TestCase{
	
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestTimeZoneCountries.class);
    }

    public TestTimeZoneCountries(String name) {
        super(name);
    }

    public void testTimeZoneCountry() throws Exception {
    	DateTimeZone dt = DateTimeZone.forID("US/Pacific");
    	HashMap<String, String> countries = dt.getLocationInfo().getCountries();
    	Set<String> countryCodes = countries.keySet();
    	
        assertEquals("America/Los_Angeles", dt.getID());
        assertEquals("United States", countries.get(countryCodes.iterator().next()));
    }

}
