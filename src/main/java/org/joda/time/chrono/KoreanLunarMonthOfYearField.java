package org.joda.time.chrono;

import java.util.Locale;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;

/**
 * An implementation of month of year in Korean Lunar Chronology<br>
 * This class uses {@link KoreanLunarCalendar} for detail calculation.
 * 
 * @author Billie Yang
 */
public class KoreanLunarMonthOfYearField extends DateTimeField {
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
		return KoreanLunarCalendar.getInstance().getMonthOfYear(instant);
	}

	@Override
	public String getAsText(long instant, Locale locale) {
		return null;
	}

	@Override
	public String getAsText(long instant) {
		return null;
	}

	@Override
	public String getAsText(ReadablePartial partial, int fieldValue, Locale locale) {
		return null;
	}

	@Override
	public String getAsText(ReadablePartial partial, Locale locale) {
		return null;
	}

	@Override
	public String getAsText(int fieldValue, Locale locale) {
		return null;
	}

	@Override
	public String getAsShortText(long instant, Locale locale) {
		return null;
	}

	@Override
	public String getAsShortText(long instant) {
		return null;
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
		return KoreanLunarCalendar.getInstance().isLeapMonth(instant);
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