package org.joda.time.gwt;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.MockZone;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.chrono.JulianChronology;


public class TestConstants {
    
    public static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    public static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    public static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
    public static final DateTimeZone ZONE_NEW_YORK = DateTimeZone.forID("America/New_York");
    public static final DateTimeZone ZONE_MOSCOW = DateTimeZone.forID("Europe/Moscow");
    public static final DateTimeZone ZONE_GUATEMALA = DateTimeZone.forID("America/Guatemala");
    private static long CUTOVER_TURK = 1175403600000L;
    private static int OFFSET_TURK = -18000000;  // -05:00
    public static final DateTimeZone MOCK_TURK = new MockZone(CUTOVER_TURK, OFFSET_TURK);
    public static final DateTimeZone MOSCOW = DateTimeZone.forID("Europe/Moscow");
    public static final DateTimeZone ONE_HOUR = DateTimeZone.forOffsetHours(1);
    public static final DateTimeZone SIX = DateTimeZone.forOffsetHours(6);
    public static final DateTimeZone SEVEN = DateTimeZone.forOffsetHours(7);
    public static final DateTimeZone EIGHT = DateTimeZone.forOffsetHours(8);
    public static final DateTimeZone UTC = DateTimeZone.UTC;
    public static final DateTimeZone NEWYORK = DateTimeZone.forID("America/New_York");
    public static final DateTimeZone NEW_YORK = DateTimeZone.forID("America/New_York");
    private static long CUTOVER_GAZA = 1175378400000L;
    private static int OFFSET_GAZA = 7200000;  // +02:00
    public static final DateTimeZone MOCK_GAZA = new MockZone(CUTOVER_GAZA, OFFSET_GAZA);

    public static final DateTimeZone[] ZONES = {
        DateTimeZone.UTC,
        DateTimeZone.forID("Europe/Paris"),
        DateTimeZone.forID("Europe/London"),
        DateTimeZone.forID("Asia/Tokyo"),
        DateTimeZone.forID("America/Los_Angeles"),
    };
    public static final int OFFSET_PARIS = PARIS.getOffset(0L) / DateTimeConstants.MILLIS_PER_HOUR;
    public static final int OFFSET_MOSCOW = MOSCOW.getOffset(0L) / DateTimeConstants.MILLIS_PER_HOUR;
    public static final int OFFSET_LONDON = LONDON.getOffset(0L) / DateTimeConstants.MILLIS_PER_HOUR;

    public static final Chronology COPTIC_PARIS = CopticChronology.getInstance(PARIS);
    public static final Chronology COPTIC_LONDON = CopticChronology.getInstance(LONDON);
    public static final Chronology COPTIC_TOKYO = CopticChronology.getInstance(TOKYO);
    public static final Chronology COPTIC_UTC = CopticChronology.getInstanceUTC();
    public static final Chronology ISO_PARIS = ISOChronology.getInstance(PARIS);
    public static final Chronology ISO_LONDON = ISOChronology.getInstance(LONDON);
    public static final Chronology ISO_TOKYO = ISOChronology.getInstance(TOKYO);
    public static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();
    public static final Chronology ISO_EIGHT = ISOChronology.getInstance(EIGHT);
    public static final Chronology GREGORIAN_UTC = GregorianChronology.getInstanceUTC();
    public static final Chronology GREGORIAN_PARIS = GregorianChronology.getInstance(PARIS);
    public static final Chronology BUDDHIST_PARIS = BuddhistChronology.getInstance(PARIS);
    public static final Chronology BUDDHIST_LONDON = BuddhistChronology.getInstance(LONDON);
    public static final Chronology BUDDHIST_TOKYO = BuddhistChronology.getInstance(TOKYO);
    public static final Chronology BUDDHIST_UTC = BuddhistChronology.getInstanceUTC();
    public static final Chronology ETHIOPIC_UTC = EthiopicChronology.getInstanceUTC();
    public static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();
    public static final Chronology ISLAMIC_UTC = IslamicChronology.getInstanceUTC();
    public static final Chronology GJ_UTC = GJChronology.getInstanceUTC();
    public static final Chronology GREGORIAN_MOSCOW = GregorianChronology.getInstance(MOSCOW);
    public static final ISOChronology ISO_DEFAULT = ISOChronology.getInstance(LONDON);
    public static final GJChronology GJ_DEFAULT = GJChronology.getInstance(LONDON);
    public static final GregorianChronology GREGORIAN_DEFAULT = GregorianChronology.getInstance(LONDON);
    public static final BuddhistChronology BUDDHIST_DEFAULT = BuddhistChronology.getInstance(LONDON);
    public static final CopticChronology COPTIC_DEFAULT = CopticChronology.getInstance(LONDON);
    public static final GJChronology GJ = GJChronology.getInstance();
    public static final JulianChronology JULIAN_LONDON = JulianChronology.getInstance(LONDON);
    public static final JulianChronology JULIAN_PARIS = JulianChronology.getInstance(PARIS);
    
    public static class TestAll {

        public static final boolean FAST = false;
        
    }
}
