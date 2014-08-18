package org.joda.time.chrono;

import org.joda.time.DurationFieldType;
import org.joda.time.field.BaseDurationField;

/**
 * An implementation of months duration in Korean Lunar Chronology<br>
 * This class uses {@link KoreanLunarCalendar} for detail calculation.
 * 
 * @author Billie Yang
 */
public class KoreanLunarMonthsField extends BaseDurationField {
	private static final long serialVersionUID = -1043738311062906056L;

	public KoreanLunarMonthsField() {
		super(DurationFieldType.years());
	}

	@Override
	public boolean isPrecise() {
		return false;
	}

	@Override
	public long getUnitMillis() {
		return 0;
	}

	@Override
	public long getValueAsLong(long duration, long instant) {
		return 0;
	}

	@Override
	public long getMillis(int value, long instant) {
		return 0;
	}

	@Override
	public long getMillis(long value, long instant) {
		return 0;
	}

	@Override
	public long add(long instant, int value) {
		return KoreanLunarCalendar.getInstance().addMonths(instant, value);
	}

	@Override
	public long add(long instant, long value) {
		return 0;
	}

	@Override
	public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
		return 0;
	}
}
