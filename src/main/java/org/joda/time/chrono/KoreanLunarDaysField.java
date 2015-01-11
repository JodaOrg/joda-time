package org.joda.time.chrono;

import org.joda.time.DurationFieldType;
import org.joda.time.field.BaseDurationField;

/**
 * An implementation of days duration in Korean Lunar Chronology<br>
 * Korean lunar calendar doesn't have daylight-saving-time, so unit-milliseconds of a day is fixed value. 
 * 
 * @author Billie Yang
 */
public class KoreanLunarDaysField extends BaseDurationField {
	private static final long serialVersionUID = -1043738311062906056L;

	public KoreanLunarDaysField() {
		super(DurationFieldType.years());
	}

	@Override
	public boolean isPrecise() {
		return false;
	}

	@Override
	public long getUnitMillis() {
		return 1000 * 60 * 60 * 24;
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
		return instant + getUnitMillis() * value;
	}

	@Override
	public long add(long instant, long value) {
		return instant + getUnitMillis() * value;
	}

	@Override
	public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
		return 0;
	}
}
