/*
 *  Copyright 2001-2015 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.format;

import java.util.Arrays;
import java.util.Locale;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.IllegalInstantException;

/**
 * DateTimeParserBucket is an advanced class, intended mainly for parser
 * implementations. It can also be used during normal parsing operations to
 * capture more information about the parse.
 * <p>
 * This class allows fields to be saved in any order, but be physically set in
 * a consistent order. This is useful for parsing against formats that allow
 * field values to contradict each other.
 * <p>
 * Field values are applied in an order where the "larger" fields are set
 * first, making their value less likely to stick.  A field is larger than
 * another when it's range duration is longer. If both ranges are the same,
 * then the larger field has the longer duration. If it cannot be determined
 * which field is larger, then the fields are set in the order they were saved.
 * <p>
 * For example, these fields were saved in this order: dayOfWeek, monthOfYear,
 * dayOfMonth, dayOfYear. When computeMillis is called, the fields are set in
 * this order: monthOfYear, dayOfYear, dayOfMonth, dayOfWeek.
 * <p>
 * DateTimeParserBucket is mutable and not thread-safe.
 *
 * @author Brian S O'Neill
 * @author Fredrik Borgh
 * @since 1.0
 */
public class DateTimeParserBucket {

    /** The chronology to use for parsing. */
    private final Chronology iChrono;
    /** The initial millis. */
    private final long iMillis;
    /** The locale to use for parsing. */
    private final Locale iLocale;
    /** Used for parsing month/day without year. */
    private final int iDefaultYear;
    /** The default zone from the constructor. */
    private final DateTimeZone iDefaultZone;
    /** The default pivot year from the constructor. */
    private final Integer iDefaultPivotYear;

    /** The parsed zone, initialised to formatter zone. */
    private DateTimeZone iZone;
    /** The parsed offset. */
    private Integer iOffset;
    /** Used for parsing two-digit years. */
    private Integer iPivotYear;

    private SavedField[] iSavedFields;
    private int iSavedFieldsCount;
    private boolean iSavedFieldsShared;
    
    private Object iSavedState;

    /**
     * Constructs a bucket.
     * 
     * @param instantLocal  the initial millis from 1970-01-01T00:00:00, local time
     * @param chrono  the chronology to use
     * @param locale  the locale to use
     * @deprecated Use longer constructor
     */
    @Deprecated
    public DateTimeParserBucket(long instantLocal, Chronology chrono, Locale locale) {
        this(instantLocal, chrono, locale, null, 2000);
    }

    /**
     * Constructs a bucket, with the option of specifying the pivot year for
     * two-digit year parsing.
     *
     * @param instantLocal  the initial millis from 1970-01-01T00:00:00, local time
     * @param chrono  the chronology to use
     * @param locale  the locale to use
     * @param pivotYear  the pivot year to use when parsing two-digit years
     * @since 1.1
     * @deprecated Use longer constructor
     */
    @Deprecated
    public DateTimeParserBucket(long instantLocal, Chronology chrono, Locale locale, Integer pivotYear) {
        this(instantLocal, chrono, locale, pivotYear, 2000);
    }

    /**
     * Constructs a bucket, with the option of specifying the pivot year for
     * two-digit year parsing.
     *
     * @param instantLocal  the initial millis from 1970-01-01T00:00:00, local time
     * @param chrono  the chronology to use
     * @param locale  the locale to use
     * @param pivotYear  the pivot year to use when parsing two-digit years
     * @param defaultYear  the default year to use when parsing month-day
     * @since 2.0
     */
    public DateTimeParserBucket(long instantLocal, Chronology chrono,
            Locale locale, Integer pivotYear, int defaultYear) {
        super();
        chrono = DateTimeUtils.getChronology(chrono);
        iMillis = instantLocal;
        iDefaultZone = chrono.getZone();
        iChrono = chrono.withUTC();
        iLocale = (locale == null ? Locale.getDefault() : locale);
        iDefaultYear = defaultYear;
        iDefaultPivotYear = pivotYear;
        // reset
        iZone = iDefaultZone;
        iPivotYear = iDefaultPivotYear;
        iSavedFields = new SavedField[8];
    }

    //-----------------------------------------------------------------------
    /**
     * Resets the state back to that when the object was constructed.
     * <p>
     * This resets the state of the bucket, allowing a single bucket to be re-used
     * for many parses. The bucket must not be shared between threads.
     * 
     * @since 2.4
     */
    public void reset() {
        iZone = iDefaultZone;
        iOffset = null;
        iPivotYear = iDefaultPivotYear;
        iSavedFieldsCount = 0;
        iSavedFieldsShared = false;
        iSavedState = null;
    }

    /**
     * Parses a datetime from the given text, returning the number of
     * milliseconds since the epoch, 1970-01-01T00:00:00Z.
     * <p>
     * This parses the text using the parser into this bucket.
     * The bucket is reset before parsing begins, allowing the bucket to be re-used.
     * The bucket must not be shared between threads.
     *
     * @param parser  the parser to use, see {@link DateTimeFormatter#getParser()}, not null
     * @param text  text to parse, not null
     * @return parsed value expressed in milliseconds since the epoch
     * @throws UnsupportedOperationException if parsing is not supported
     * @throws IllegalArgumentException if the text to parse is invalid
     * @since 2.4
     */
    public long parseMillis(DateTimeParser parser, CharSequence text) {
        reset();
        return doParseMillis(DateTimeParserInternalParser.of(parser), text);
    }

    long doParseMillis(InternalParser parser, CharSequence text) {
        int newPos = parser.parseInto(this, text, 0);
        if (newPos >= 0) {
            if (newPos >= text.length()) {
                return computeMillis(true, text);
            }
        } else {
            newPos = ~newPos;
        }
        throw new IllegalArgumentException(FormatUtils.createErrorMessage(text.toString(), newPos));
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the chronology of the bucket, which will be a local (UTC) chronology.
     */
    public Chronology getChronology() {
        return iChrono;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the locale to be used during parsing.
     * 
     * @return the locale to use
     */
    public Locale getLocale() {
        return iLocale;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the time zone used by computeMillis.
     */
    public DateTimeZone getZone() {
        return iZone;
    }

    /**
     * Set a time zone to be used when computeMillis is called.
     */
    public void setZone(DateTimeZone zone) {
        iSavedState = null;
        iZone = zone;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the time zone offset in milliseconds used by computeMillis.
     * @deprecated use Integer version
     */
    @Deprecated
    public int getOffset() {
        return (iOffset != null ? iOffset : 0);
    }

    /**
     * Returns the time zone offset in milliseconds used by computeMillis.
     */
    public Integer getOffsetInteger() {
        return iOffset;
    }

    /**
     * Set a time zone offset to be used when computeMillis is called.
     * @deprecated use Integer version
     */
    @Deprecated
    public void setOffset(int offset) {
        iSavedState = null;
        iOffset = offset;
    }

    /**
     * Set a time zone offset to be used when computeMillis is called.
     */
    public void setOffset(Integer offset) {
        iSavedState = null;
        iOffset = offset;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the default year used when information is incomplete.
     * <p>
     * This is used for two-digit years and when the largest parsed field is
     * months or days.
     * <p>
     * A null value for two-digit years means to use the value from DateTimeFormatterBuilder.
     * A null value for month/day only parsing will cause the default of 2000 to be used.
     *
     * @return Integer value of the pivot year, null if not set
     * @since 1.1
     */
    public Integer getPivotYear() {
        return iPivotYear;
    }

    /**
     * Sets the pivot year to use when parsing two digit years.
     * <p>
     * If the value is set to null, this will indicate that default
     * behaviour should be used.
     *
     * @param pivotYear  the pivot year to use
     * @since 1.1
     * @deprecated this method should never have been public
     */
    @Deprecated
    public void setPivotYear(Integer pivotYear) {
        iPivotYear = pivotYear;
    }

    //-----------------------------------------------------------------------
    /**
     * Saves a datetime field value.
     * 
     * @param field  the field, whose chronology must match that of this bucket
     * @param value  the value
     */
    public void saveField(DateTimeField field, int value) {
        obtainSaveField().init(field, value);
    }
    
    /**
     * Saves a datetime field value.
     * 
     * @param fieldType  the field type
     * @param value  the value
     */
    public void saveField(DateTimeFieldType fieldType, int value) {
        obtainSaveField().init(fieldType.getField(iChrono), value);
    }
    
    /**
     * Saves a datetime field text value.
     * 
     * @param fieldType  the field type
     * @param text  the text value
     * @param locale  the locale to use
     */
    public void saveField(DateTimeFieldType fieldType, String text, Locale locale) {
        obtainSaveField().init(fieldType.getField(iChrono), text, locale);
    }
    
    private SavedField obtainSaveField() {
        SavedField[] savedFields = iSavedFields;
        int savedFieldsCount = iSavedFieldsCount;
        
        if (savedFieldsCount == savedFields.length || iSavedFieldsShared) {
            // Expand capacity or merely copy if saved fields are shared.
            SavedField[] newArray = new SavedField
                [savedFieldsCount == savedFields.length ? savedFieldsCount * 2 : savedFields.length];
            System.arraycopy(savedFields, 0, newArray, 0, savedFieldsCount);
            iSavedFields = savedFields = newArray;
            iSavedFieldsShared = false;
        }
        
        iSavedState = null;
        SavedField saved = savedFields[savedFieldsCount];
        if (saved == null) {
            saved = savedFields[savedFieldsCount] = new SavedField();
        }
        iSavedFieldsCount = savedFieldsCount + 1;
        return saved;
    }
    
    /**
     * Saves the state of this bucket, returning it in an opaque object. Call
     * restoreState to undo any changes that were made since the state was
     * saved. Calls to saveState may be nested.
     *
     * @return opaque saved state, which may be passed to restoreState
     */
    public Object saveState() {
        if (iSavedState == null) {
            iSavedState = new SavedState();
        }
        return iSavedState;
    }
    
    /**
     * Restores the state of this bucket from a previously saved state. The
     * state object passed into this method is not consumed, and it can be used
     * later to restore to that state again.
     *
     * @param savedState opaque saved state, returned from saveState
     * @return true state object is valid and state restored
     */
    public boolean restoreState(Object savedState) {
        if (savedState instanceof SavedState) {
            if (((SavedState) savedState).restoreState(this)) {
                iSavedState = savedState;
                return true;
            }
        }
        return false;
    }
    
    /**
     * Computes the parsed datetime by setting the saved fields.
     * This method is idempotent, but it is not thread-safe.
     *
     * @return milliseconds since 1970-01-01T00:00:00Z
     * @throws IllegalArgumentException if any field is out of range
     */
    public long computeMillis() {
        return computeMillis(false, (CharSequence) null);
    }
    
    /**
     * Computes the parsed datetime by setting the saved fields.
     * This method is idempotent, but it is not thread-safe.
     *
     * @param resetFields false by default, but when true, unsaved field values are cleared
     * @return milliseconds since 1970-01-01T00:00:00Z
     * @throws IllegalArgumentException if any field is out of range
     */
    public long computeMillis(boolean resetFields) {
        return computeMillis(resetFields, (CharSequence) null);
    }

    /**
     * Computes the parsed datetime by setting the saved fields.
     * This method is idempotent, but it is not thread-safe.
     *
     * @param resetFields false by default, but when true, unsaved field values are cleared
     * @param text optional text being parsed, to be included in any error message
     * @return milliseconds since 1970-01-01T00:00:00Z
     * @throws IllegalArgumentException if any field is out of range
     * @since 1.3
     */
    public long computeMillis(boolean resetFields, String text) {
        return computeMillis(resetFields, (CharSequence) text);
    }

    /**
     * Computes the parsed datetime by setting the saved fields.
     * This method is idempotent, but it is not thread-safe.
     *
     * @param resetFields false by default, but when true, unsaved field values are cleared
     * @param text optional text being parsed, to be included in any error message
     * @return milliseconds since 1970-01-01T00:00:00Z
     * @throws IllegalArgumentException if any field is out of range
     * @since 2.4
     */
    public long computeMillis(boolean resetFields, CharSequence text) {
        SavedField[] savedFields = iSavedFields;
        int count = iSavedFieldsCount;
        if (iSavedFieldsShared) {
            // clone so that sort does not affect saved state
            iSavedFields = savedFields = (SavedField[])iSavedFields.clone();
            iSavedFieldsShared = false;
        }
        sort(savedFields, count);
        if (count > 0) {
            // alter base year for parsing if first field is month or day
            DurationField months = DurationFieldType.months().getField(iChrono);
            DurationField days = DurationFieldType.days().getField(iChrono);
            DurationField first = savedFields[0].iField.getDurationField();
            if (compareReverse(first, months) >= 0 && compareReverse(first, days) <= 0) {
                saveField(DateTimeFieldType.year(), iDefaultYear);
                return computeMillis(resetFields, text);
            }
        }

        long millis = iMillis;
        try {
            for (int i = 0; i < count; i++) {
                millis = savedFields[i].set(millis, resetFields);
            }
            if (resetFields) {
                for (int i = 0; i < count; i++) {
                    millis = savedFields[i].set(millis, i == (count - 1));
                }
            }
        } catch (IllegalFieldValueException e) {
            if (text != null) {
                e.prependMessage("Cannot parse \"" + text + '"');
            }
            throw e;
        }
        
        if (iOffset != null) {
            millis -= iOffset;
        } else if (iZone != null) {
            int offset = iZone.getOffsetFromLocal(millis);
            millis -= offset;
            if (offset != iZone.getOffset(millis)) {
                String message = "Illegal instant due to time zone offset transition (" + iZone + ')';
                if (text != null) {
                    message = "Cannot parse \"" + text + "\": " + message;
                }
                throw new IllegalInstantException(message);
            }
        }
        
        return millis;
    }
    
    /**
     * Sorts elements [0,high). Calling java.util.Arrays isn't always the right
     * choice since it always creates an internal copy of the array, even if it
     * doesn't need to. If the array slice is small enough, an insertion sort
     * is chosen instead, but it doesn't need a copy!
     * <p>
     * This method has a modified version of that insertion sort, except it
     * doesn't create an unnecessary array copy. If high is over 10, then
     * java.util.Arrays is called, which will perform a merge sort, which is
     * faster than insertion sort on large lists.
     * <p>
     * The end result is much greater performance when computeMillis is called.
     * Since the amount of saved fields is small, the insertion sort is a
     * better choice. Additional performance is gained since there is no extra
     * array allocation and copying. Also, the insertion sort here does not
     * perform any casting operations. The version in java.util.Arrays performs
     * casts within the insertion sort loop.
     */
    private static void sort(SavedField[] array, int high) {
        if (high > 10) {
            Arrays.sort(array, 0, high);
        } else {
            for (int i=0; i<high; i++) {
                for (int j=i; j>0 && (array[j-1]).compareTo(array[j])>0; j--) {
                    SavedField t = array[j];
                    array[j] = array[j-1];
                    array[j-1] = t;
                }
            }
        }
    }

    class SavedState {
        final DateTimeZone iZone;
        final Integer iOffset;
        final SavedField[] iSavedFields;
        final int iSavedFieldsCount;
        
        SavedState() {
            this.iZone = DateTimeParserBucket.this.iZone;
            this.iOffset = DateTimeParserBucket.this.iOffset;
            this.iSavedFields = DateTimeParserBucket.this.iSavedFields;
            this.iSavedFieldsCount = DateTimeParserBucket.this.iSavedFieldsCount;
        }
        
        boolean restoreState(DateTimeParserBucket enclosing) {
            if (enclosing != DateTimeParserBucket.this) {
                // block SavedState from a different bucket
                return false;
            }
            enclosing.iZone = this.iZone;
            enclosing.iOffset = this.iOffset;
            enclosing.iSavedFields = this.iSavedFields;
            if (this.iSavedFieldsCount < enclosing.iSavedFieldsCount) {
                // Since count is being restored to a lower count, the
                // potential exists for new saved fields to destroy data being
                // shared by another state. Set this flag such that the array
                // of saved fields is cloned prior to modification.
                enclosing.iSavedFieldsShared = true;
            }
            enclosing.iSavedFieldsCount = this.iSavedFieldsCount;
            return true;
        }
    }
    
    static class SavedField implements Comparable<SavedField> {
        DateTimeField iField;
        int iValue;
        String iText;
        Locale iLocale;
        
        SavedField() {
        }
        
        void init(DateTimeField field, int value) {
            iField = field;
            iValue = value;
            iText = null;
            iLocale = null;
        }
        
        void init(DateTimeField field, String text, Locale locale) {
            iField = field;
            iValue = 0;
            iText = text;
            iLocale = locale;
        }
        
        long set(long millis, boolean reset) {
            if (iText == null) {
                millis = iField.setExtended(millis, iValue);
            } else {
                millis = iField.set(millis, iText, iLocale);
            }
            if (reset) {
                millis = iField.roundFloor(millis);
            }
            return millis;
        }
        
        /**
         * The field with the longer range duration is ordered first, where
         * null is considered infinite. If the ranges match, then the field
         * with the longer duration is ordered first.
         */
        public int compareTo(SavedField obj) {
            DateTimeField other = obj.iField;
            int result = compareReverse
                (iField.getRangeDurationField(), other.getRangeDurationField());
            if (result != 0) {
                return result;
            }
            return compareReverse
                (iField.getDurationField(), other.getDurationField());
        }
    }

    static int compareReverse(DurationField a, DurationField b) {
        if (a == null || !a.isSupported()) {
            if (b == null || !b.isSupported()) {
                return 0;
            }
            return -1;
        }
        if (b == null || !b.isSupported()) {
            return 1;
        }
        return -a.compareTo(b);
    }
}
