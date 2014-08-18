package org.joda.time.chrono;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;

/**
 * A chronology that matches the KoreanLunarCalendar class supplied by Moon.
 * <p>
 * The chronology is based on ISO, starting from 1881-01-01 (1881-01-30 in ISO), as time goes through KoreanLunarCalendar
 * <p>
 * This class was intended by Moon-based calendar used in Korea.
 * <p>
 * KoreanLunarChronology is thread-safe and immutable, singleton (only can be used by getInstance()}).
 *
 * @author Billie Yang
 * @since 2.4
 */
public class KoreanLunarChronology extends AssembledChronology {
	private static final long serialVersionUID = 1056220584903746008L;

	/** Cache of zone to chronology arrays */
	private static final Map<DateTimeZone, KoreanLunarChronology> cCache = new HashMap<DateTimeZone, KoreanLunarChronology>();

	private static final KoreanLunarChronology INSTANCE_UTC;
	static {
		// init after static fields
		INSTANCE_UTC = new KoreanLunarChronology(DateTimeZone.UTC);
	}

	KoreanLunarChronology(DateTimeZone zone) {
		super(ISOChronology.getInstance(zone), null);
	}

	public static KoreanLunarChronology getInstance(DateTimeZone zone) {
		if (!cCache.containsKey(zone)) {
			cCache.put(zone, new KoreanLunarChronology(zone));
		}
		return cCache.get(zone);
	}

	public static KoreanLunarChronology getInstanceKST() {
		return getInstance(DateTimeZone.forOffsetHours(9));
	}

	public static KoreanLunarChronology getInstanceUTC() {
		return INSTANCE_UTC;
	}

	@Override
	protected void assemble(Fields fields) {
		fields.year = new KoreanLunarYearField();
		fields.monthOfYear = new KoreanLunarMonthOfYearField();
		fields.dayOfMonth = new KoreanLunarDayOfMonthField();
		fields.dayOfWeek = new KoreanLunarDayOfWeekField();
		fields.years = new KoreanLunarYearsField();
		fields.months = new KoreanLunarMonthsField();
		fields.days = new KoreanLunarDaysField();
	}

	@Override
	public Chronology withUTC() {
		return getInstanceUTC();
	}

	@Override
	public Chronology withZone(DateTimeZone zone) {
		return getInstance(zone);
	}

	@Override
	public String toString() {
		return "KoreanLunarChronology";
	}
}
