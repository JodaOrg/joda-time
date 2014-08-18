package org.joda.time.chrono;

import java.util.Locale;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;

/**
 * An implementation of day of week in Korean Lunar Chronology<br>
 * This class uses {@link KoreanLunarCalendar} for detail calculation.
 * <p>
 * Actually, Korean lunar calendar doesn't support day of week concept.<br>
 * In common sense, however, Korean often uses the day of week of corresponding solar calendar.
 * 
 * @author Billie Yang
 */
public class KoreanLunarDayOfWeekField extends DateTimeField {
	@Override
	public DateTimeFieldType getType() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean isSupported() {
		return false;
	}

	@Override
	public boolean isLenient() {
		return false;
	}

	@Override
	public int get(long instant) {
		return KoreanLunarCalendar.getInstance().getDayOfWeek(instant);
	}

	@Override
	public String getAsText(long instant, Locale locale) {
		return "X요일";
	}

	@Override
	public String getAsText(long instant) {
		return "X요일";
	}

	@Override
	public String getAsText(ReadablePartial partial, int fieldValue, Locale locale) {
		return "X요일";
	}

	@Override
	public String getAsText(ReadablePartial partial, Locale locale) {
		return "X요일";
	}

	@Override
	public String getAsText(int fieldValue, Locale locale) {
		return "X요일";
	}

	@Override
	public String getAsShortText(long instant, Locale locale) {
		int dayOfWeek = get(instant);
		if (dayOfWeek == 1) {
			return "월";
		} else if (dayOfWeek == 2) {
			return "화";
		} else if (dayOfWeek == 3) {
			return "수";
		} else if (dayOfWeek == 4) {
			return "목";
		} else if (dayOfWeek == 5) {
			return "금";
		} else if (dayOfWeek == 6) {
			return "토";
		} else if (dayOfWeek == 7) {
			return "일";
		}
		return "X";
	}

	@Override
	public String getAsShortText(long instant) {
		return "X";
	}

	@Override
	public String getAsShortText(ReadablePartial partial, int fieldValue, Locale locale) {
		return null;
	}

	@Override
	public String getAsShortText(ReadablePartial partial, Locale locale) {
		return null;
	}

	@Override
	public String getAsShortText(int fieldValue, Locale locale) {
		return null;
	}

	@Override
	public long add(long instant, int value) {
		return 0;
	}

	@Override
	public long add(long instant, long value) {
		return 0;
	}

	@Override
	public int[] add(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
		return null;
	}

	@Override
	public int[] addWrapPartial(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
		return null;
	}

	@Override
	public long addWrapField(long instant, int value) {
		return 0;
	}

	@Override
	public int[] addWrapField(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
		return null;
	}

	@Override
	public int getDifference(long minuendInstant, long subtrahendInstant) {
		return 0;
	}

	@Override
	public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
		return 0;
	}

	@Override
	public long set(long instant, int value) {
		return 0;
	}

	@Override
	public int[] set(ReadablePartial instant, int fieldIndex, int[] values, int newValue) {
		return null;
	}

	@Override
	public long set(long instant, String text, Locale locale) {
		return 0;
	}

	@Override
	public long set(long instant, String text) {
		return 0;
	}

	@Override
	public int[] set(ReadablePartial instant, int fieldIndex, int[] values, String text, Locale locale) {
		return null;
	}

	@Override
	public DurationField getDurationField() {
		return null;
	}

	@Override
	public DurationField getRangeDurationField() {
		return null;
	}

	@Override
	public boolean isLeap(long instant) {
		return false;
	}

	@Override
	public int getLeapAmount(long instant) {
		return 0;
	}

	@Override
	public DurationField getLeapDurationField() {
		return null;
	}

	@Override
	public int getMinimumValue() {
		return 0;
	}

	@Override
	public int getMinimumValue(long instant) {
		return 0;
	}

	@Override
	public int getMinimumValue(ReadablePartial instant) {
		return 0;
	}

	@Override
	public int getMinimumValue(ReadablePartial instant, int[] values) {
		return 0;
	}

	@Override
	public int getMaximumValue() {
		return 0;
	}

	@Override
	public int getMaximumValue(long instant) {
		return 0;
	}

	@Override
	public int getMaximumValue(ReadablePartial instant) {
		return 0;
	}

	@Override
	public int getMaximumValue(ReadablePartial instant, int[] values) {
		return 0;
	}

	@Override
	public int getMaximumTextLength(Locale locale) {
		return 0;
	}

	@Override
	public int getMaximumShortTextLength(Locale locale) {
		return 0;
	}

	@Override
	public long roundFloor(long instant) {
		return 0;
	}

	@Override
	public long roundCeiling(long instant) {
		return 0;
	}

	@Override
	public long roundHalfFloor(long instant) {
		return 0;
	}

	@Override
	public long roundHalfCeiling(long instant) {
		return 0;
	}

	@Override
	public long roundHalfEven(long instant) {
		return 0;
	}

	@Override
	public long remainder(long instant) {
		return 0;
	}

	@Override
	public String toString() {
		return null;
	}

}
