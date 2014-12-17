package org.joda.time.chrono;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by carlo on 15.12.14.
 */
public class TestInternationalFixedChronology extends TestCase {
    private static final InternationalFixedChronology IFC_UTC = InternationalFixedChronology.getInstanceUTC ();
    private static final GregorianChronology GREG_UTC = GregorianChronology.getInstanceUTC ();
    private static final DateTimeZone PARIS = DateTimeZone.forID ("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID ("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID ("Asia/Tokyo");
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC ();
    private static final Chronology GJ_UTC = GJChronology.getInstanceUTC ();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC ();
    private static final DateTimeFormatter printer = DateTimeFormat.forPattern ("MMMM dd yyyy").withLocale (new Locale ("en", "US", "ifc"));
    private final static long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
            366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 +
            365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
            366 + 365;
    private static int SKIP = 1 * DateTimeConstants.MILLIS_PER_DAY;
    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;
    // 2002-06-09
    private long TEST_TIME_NOW = (y2002days + 31L + 28L + 31L + 30L + 31L + 9L - 1L) * DateTimeConstants.MILLIS_PER_DAY;

    public TestInternationalFixedChronology (final String name) {
        super (name);
    }

    public static TestSuite suite () {
        return new TestSuite (TestInternationalFixedChronology.class);
    }

    protected void setUp () throws Exception {
        DateTimeUtils.setCurrentMillisFixed (TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault ();
        originalTimeZone = TimeZone.getDefault ();
        originalLocale = Locale.getDefault ();
        DateTimeZone.setDefault (LONDON);
        TimeZone.setDefault (TimeZone.getTimeZone ("Europe/London"));
        Locale.setDefault (Locale.UK);
    }

    protected void tearDown () throws Exception {
        DateTimeUtils.setCurrentMillisSystem ();
        DateTimeZone.setDefault (originalDateTimeZone);
        TimeZone.setDefault (originalTimeZone);
        Locale.setDefault (originalLocale);
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    public void testFactoryUTC () {
        assertEquals (DateTimeZone.UTC, InternationalFixedChronology.getInstanceUTC ().getZone ());
        assertSame (InternationalFixedChronology.class, InternationalFixedChronology.getInstanceUTC ().getClass ());
    }

    public void testFactory () {
        assertEquals (LONDON, InternationalFixedChronology.getInstance ().getZone ());
        assertSame (InternationalFixedChronology.class, InternationalFixedChronology.getInstance ().getClass ());
    }

    public void testFactory_Zone () {
        assertEquals (TOKYO, InternationalFixedChronology.getInstance (TOKYO).getZone ());
        assertEquals (PARIS, InternationalFixedChronology.getInstance (PARIS).getZone ());
        assertEquals (LONDON, InternationalFixedChronology.getInstance (null).getZone ());
        assertSame (InternationalFixedChronology.class, InternationalFixedChronology.getInstance (TOKYO).getClass ());
    }

    //-----------------------------------------------------------------------
    public void testEquality () {
        assertSame (InternationalFixedChronology.getInstance (TOKYO), InternationalFixedChronology.getInstance (TOKYO));
        assertSame (InternationalFixedChronology.getInstance (LONDON), InternationalFixedChronology.getInstance (LONDON));
        assertSame (InternationalFixedChronology.getInstance (PARIS), InternationalFixedChronology.getInstance (PARIS));
        assertSame (InternationalFixedChronology.getInstanceUTC (), InternationalFixedChronology.getInstanceUTC ());
        assertSame (InternationalFixedChronology.getInstance (), InternationalFixedChronology.getInstance (LONDON));
    }

    public void testWithUTC () {
        assertSame (InternationalFixedChronology.getInstanceUTC (), InternationalFixedChronology.getInstance (LONDON).withUTC ());
        assertSame (InternationalFixedChronology.getInstanceUTC (), InternationalFixedChronology.getInstance (TOKYO).withUTC ());
        assertSame (InternationalFixedChronology.getInstanceUTC (), InternationalFixedChronology.getInstanceUTC ().withUTC ());
        assertSame (InternationalFixedChronology.getInstanceUTC (), InternationalFixedChronology.getInstance ().withUTC ());
    }

    public void testWithZone () {
        assertSame (InternationalFixedChronology.getInstance (TOKYO), InternationalFixedChronology.getInstance (TOKYO).withZone (TOKYO));
        //assertSame(InternationalFixedChronology.getInstance(LONDON), InternationalFixedChronology.getInstance(TOKYO).withZone(LONDON));
        //assertSame(InternationalFixedChronology.getInstance(PARIS), InternationalFixedChronology.getInstance(TOKYO).withZone(PARIS));
        //assertSame(InternationalFixedChronology.getInstance(LONDON), InternationalFixedChronology.getInstance(TOKYO).withZone(null));
        //assertSame(InternationalFixedChronology.getInstance(PARIS), InternationalFixedChronology.getInstance().withZone(PARIS));
        //assertSame(InternationalFixedChronology.getInstance(PARIS), InternationalFixedChronology.getInstanceUTC().withZone(PARIS));
    }

    public void testToString () {
        assertEquals ("InternationalFixedChronology[Europe/London,mdfw=7]", InternationalFixedChronology.getInstance (LONDON).toString ());
        assertEquals ("InternationalFixedChronology[Asia/Tokyo,mdfw=7]", InternationalFixedChronology.getInstance (TOKYO).toString ());
        assertEquals ("InternationalFixedChronology[Europe/London,mdfw=7]", InternationalFixedChronology.getInstance ().toString ());
        assertEquals ("InternationalFixedChronology[UTC,mdfw=7]", InternationalFixedChronology.getInstanceUTC ().toString ());
    }

    public void testConversion () {
        DateTime fixedDt     = new DateTime (2015, 1, 1, 0, 0, IFC_UTC);
        DateTime gregorianDt = new DateTime (2015, 1, 1, 0, 0, GREG_UTC);
        assertEquals (fixedDt.withChronology (GREG_UTC), gregorianDt);
        assertEquals (fixedDt, gregorianDt.withChronology (IFC_UTC));
        assertEquals (fixedDt.getYear (), 2015);
        assertEquals (fixedDt.getMonthOfYear (), 1);
        assertEquals (fixedDt.getDayOfMonth (), 1);
        assertEquals ("January 01 2015", printer.print (fixedDt));

        fixedDt     = new DateTime (2015, 7,  1, 0, 0, IFC_UTC);
        gregorianDt = new DateTime (2015, 6, 18, 0, 0, GREG_UTC);
        assertEquals (fixedDt.withChronology (GREG_UTC), gregorianDt);
        assertEquals (fixedDt, gregorianDt.withChronology (IFC_UTC));
        assertEquals (fixedDt.getYear (), 2015);
        assertEquals (fixedDt.getMonthOfYear (), 7);
        assertEquals (fixedDt.getDayOfMonth (), 1);
        assertEquals ("Sol 01 2015", printer.print (fixedDt));

        fixedDt     = new DateTime (2015, 13, 28, 0, 0, IFC_UTC);
        gregorianDt = new DateTime (2015, 12, 30, 0, 0, GREG_UTC);
        assertEquals (fixedDt.withChronology (GREG_UTC), gregorianDt);
        assertEquals (fixedDt, gregorianDt.withChronology (IFC_UTC));
        assertEquals (fixedDt.getYear (), 2015);
        assertEquals (fixedDt.getMonthOfYear (), 13);
        assertEquals (fixedDt.getDayOfMonth (), 28);
        assertEquals ("December 28 2015", printer.print (fixedDt));
    }
}
