/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2004 Stephen Colebourne.  
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Joda project (http://www.joda.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "Joda" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact licence@joda.org.
 *
 * 5. Products derived from this software may not be called "Joda",
 *    nor may "Joda" appear in their name, without prior written
 *    permission of the Joda project.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE JODA AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Joda project and was originally 
 * created by Stephen Colebourne <scolebourne@joda.org>. For more
 * information on the Joda project, please see <http://www.joda.org/>.
 */
package org.joda.time.format;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Locale;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadWritableInstant;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;

/**
 * Abstract base class for implementing {@link DateTimePrinter}s,
 * {@link DateTimeParser}s, and {@link DateTimeFormatter}s. This class
 * intentionally does not implement any of those interfaces. You can subclass
 * and implement only the interfaces that you need to.
 * <p>
 * The print methods assume that your subclass has implemented DateTimePrinter or
 * DateTimeFormatter. If not, a ClassCastException is thrown when calling those
 * methods.
 * <p>
 * Likewise, the parse methods assume that your subclass has implemented
 * DateTimeParser or DateTimeFormatter. If not, a ClassCastException is thrown
 * when calling the parse methods.
 * <p>
 * BaseDateTimeFormatter is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public abstract class BaseDateTimeFormatter {

    public BoundDateTimePrinter bindPrinter(Chronology chrono) {
        return (BoundDateTimePrinter) this;
    }

    //-----------------------------------------------------------------------
    public void printTo(StringBuffer buf, ReadableInstant instant) {
        long millis = DateTimeUtils.getInstantMillis(instant);
        Chronology chrono = DateTimeUtils.getInstantChronology(instant);
        printTo(buf, millis, chrono);
    }

    public void printTo(Writer out, ReadableInstant instant) throws IOException {
        long millis = DateTimeUtils.getInstantMillis(instant);
        Chronology chrono = DateTimeUtils.getInstantChronology(instant);
        printTo(out, millis, chrono);
    }

    public void printTo(StringBuffer buf, long instant) {
        printTo(buf, instant, ISOChronology.getInstance());
    }

    public void printTo(Writer out, long instant) throws IOException {
        printTo(out, instant, ISOChronology.getInstance());
    }

    public void printTo(StringBuffer buf, long instant, DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        printTo(buf, instant, ISOChronology.getInstance(zone));
    }

    public void printTo(Writer out, long instant, DateTimeZone zone) throws IOException {
        zone = DateTimeUtils.getZone(zone);
        printTo(out, instant, ISOChronology.getInstance(zone));
    }

    public void printTo(StringBuffer buf, long instant, Chronology chrono) {
        chrono = DateTimeUtils.getChronology(chrono);
        printTo(buf,
                instant + chrono.getZone().getOffset(instant), chrono.withUTC(),
                instant, chrono);
    }

    public void printTo(Writer out, long instant, Chronology chrono) throws IOException {
        chrono = DateTimeUtils.getChronology(chrono);
        printTo(out,
                instant + chrono.getZone().getOffset(instant), chrono.withUTC(),
                instant, chrono);
    }

    //-----------------------------------------------------------------------
    public String print(ReadableInstant instant) {
        long millis = DateTimeUtils.getInstantMillis(instant);
        Chronology chrono = DateTimeUtils.getInstantChronology(instant);
        return print(millis, chrono);
    }

    public String print(long instant) {
        return print(instant, ISOChronology.getInstance());
    }

    public String print(long instant, DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        return print(instant, ISOChronology.getInstance(zone));
    }

    public String print(long instant, Chronology chrono) {
        chrono = DateTimeUtils.getChronology(chrono);
        return print(instant + chrono.getZone().getOffset(instant), chrono.withUTC(),
                     instant, chrono);
    }

    public String print(ReadablePartial partial) {
        StringBuffer buf = new StringBuffer(estimatePrintedLength());
        printTo(buf, partial);
        return buf.toString();
    }

    //-----------------------------------------------------------------------
    protected int estimatePrintedLength() {
        throw new UnsupportedOperationException("Printing not supported");
    }

    protected void printTo(StringBuffer buf,
                           long instantLocal, Chronology chronoLocal,
                           long instant, Chronology chrono) {
        throw new UnsupportedOperationException("Printing not supported");
    }

    protected void printTo(Writer out,
                           long instantLocal, Chronology chronoLocal,
                           long instant, Chronology chrono) throws IOException {
        throw new UnsupportedOperationException("Printing not supported");
    }

    public void printTo(StringBuffer buf, ReadablePartial partial) {
        throw new UnsupportedOperationException("Printing not supported");
    }

    public void printTo(Writer out, ReadablePartial partial) throws IOException {
        throw new UnsupportedOperationException("Printing not supported");
    }

    protected String print(long instantLocal, Chronology chronoLocal,
                           long instant, Chronology chrono) {
        StringBuffer buf = new StringBuffer(estimatePrintedLength());
        printTo(buf, instantLocal, chronoLocal, instant, chrono);
        return buf.toString();
    }

    //-----------------------------------------------------------------------
    public int parseInto(ReadWritableInstant instant, String text, int position) {
        if (instant == null) {
            throw new IllegalArgumentException("Instant must not be null");
        }

        long millis = instant.getMillis();
        Chronology chrono = instant.getChronology();
        long instantLocal = millis + chrono.getZone().getOffset(millis);

        ParseBucket bucket = new ParseBucket(instantLocal, chrono);
        int resultPos = parseInto(bucket, text, position);
        instant.setMillis(bucket.computeMillis());
        return resultPos;
    }

    public long parseMillis(String text) {
        return parseMillis(text, ISOChronology.getInstance());
    }

    public long parseMillis(String text, Chronology chrono) {
        ParseBucket bucket = new ParseBucket(0, chrono);

        int newPos = parseInto(bucket, text, 0);
        if (newPos >= 0) {
            if (newPos >= text.length()) {
                return bucket.computeMillis(true);
            }
        } else {
            newPos = ~newPos;
        }

        throw new IllegalArgumentException(FormatUtils.createErrorMessage(text, newPos));
    }

    public long parseMillis(String text, long instant) {
        return parseMillis(text, instant, ISOChronology.getInstance());
    }

    public long parseMillis(String text, long instant, Chronology chrono) {
        chrono = DateTimeUtils.getChronology(chrono);
        long instantLocal = instant + chrono.getZone().getOffset(instant);
        ParseBucket bucket = new ParseBucket(instantLocal, chrono);

        int newPos = parseInto(bucket, text, 0);
        if (newPos >= 0) {
            if (newPos >= text.length()) {
                return bucket.computeMillis();
            }
        } else {
            newPos = ~newPos;
        }

        throw new IllegalArgumentException(FormatUtils.createErrorMessage(text, newPos));
    }

    public DateTime parseDateTime(String text) {
        return parseDateTime(text, ISOChronology.getInstance());
    }

    public DateTime parseDateTime(String text, Chronology chrono) {
        return new DateTime(parseMillis(text, chrono), chrono);
    }

    public DateTime parseDateTime(String text, ReadableInstant instant) {
        Chronology chrono = DateTimeUtils.getInstantChronology(instant);
        long millis = DateTimeUtils.getInstantMillis(instant);
        return new DateTime(parseMillis(text, millis, chrono), chrono);
    }

    public MutableDateTime parseMutableDateTime(String text) {
        return parseMutableDateTime(text, ISOChronology.getInstance());
    }

    public MutableDateTime parseMutableDateTime(String text, Chronology chrono) {
        return new MutableDateTime(parseMillis(text, chrono), chrono);
    }

    public MutableDateTime parseMutableDateTime(String text, ReadableInstant instant) {
        Chronology chrono = DateTimeUtils.getInstantChronology(instant);
        long millis = DateTimeUtils.getInstantMillis(instant);
        return new MutableDateTime(parseMillis(text, millis, chrono), chrono);
    }

    //-----------------------------------------------------------------------
    protected int estimateParsedLength() {
        throw new UnsupportedOperationException("Parsing not supported");
    }

    protected int parseInto(ParseBucket bucket, String text, int position) {
        throw new UnsupportedOperationException("Parsing not supported");
    }

    //-----------------------------------------------------------------------
    /**
     * Internal class used to build the state during parsing.
     * <p>
     * Allows fields to be saved in any order, but be physically set in a
     * consistent order. This is useful for parsing against formats that allow
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
     * ParseBucket is mutable and not thread-safe.
     *
     * @author Brian S O'Neill
     * @since 1.0
     */
    public static class ParseBucket {

        private final Chronology iChrono;
        private final long iMillis;

        // TimeZone to switch to in computeMillis. If null, use offset.
        DateTimeZone iZone;
        int iOffset;

        SavedField[] iSavedFields = new SavedField[8];
        int iSavedFieldsCount;
        boolean iSavedFieldsShared;

        private Object iSavedState;

        /**
         * Constucts a bucket.
         * 
         * @param instantLocal the initial millis from 1970-01-01T00:00:00, local time
         * @param chrono  the chronology to use
         */
        public ParseBucket(long instantLocal, Chronology chrono) {
            super();
            chrono = DateTimeUtils.getChronology(chrono);
            iMillis = instantLocal;
            iChrono = chrono.withUTC();
            setZone(chrono.getZone());
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
         * Returns the time zone used by computeMillis, or null if an offset is
         * used instead.
         */
        public DateTimeZone getZone() {
            return iZone;
        }

        /**
         * Set a time zone to be used when computeMillis is called, which
         * overrides any set time zone offset.
         *
         * @param zone the date time zone to operate in, or null if UTC
         */
        public void setZone(DateTimeZone zone) {
            iSavedState = null;
            iZone = zone == DateTimeZone.UTC ? null : zone;
            iOffset = 0;
        }

        //-----------------------------------------------------------------------
        /**
         * Returns the time zone offset used by computeMillis, unless
         * getZone doesn't return null.
         */
        public int getOffset() {
            return iOffset;
        }

        /**
         * Set a time zone offset to be used when computeMillis is called, which
         * overrides the time zone.
         */
        public void setOffset(int offset) {
            iSavedState = null;
            iOffset = offset;
            iZone = null;
        }

        //-----------------------------------------------------------------------
        /**
         * Saves a datetime field value.
         * 
         * @param field  the field, whose chronology must match that of this bucket
         * @param value  the value
         */
        public void saveField(DateTimeField field, int value) {
            saveField(new SavedField(field, value));
        }

        /**
         * Saves a datetime field value.
         * 
         * @param fieldType  the field type
         * @param value  the value
         */
        public void saveField(DateTimeFieldType fieldType, int value) {
            saveField(new SavedField(fieldType.getField(iChrono), value));
        }

        /**
         * Saves a datetime field text value.
         * 
         * @param fieldType  the field type
         * @param text  the text value
         * @param locale  the locale to use
         */
        public void saveField(DateTimeFieldType fieldType, String text, Locale locale) {
            saveField(new SavedField(fieldType.getField(iChrono), text, locale));
        }

        private void saveField(SavedField field) {
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
            savedFields[savedFieldsCount] = field;
            iSavedFieldsCount = savedFieldsCount + 1;
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
            return computeMillis(false);
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
            SavedField[] savedFields = iSavedFields;
            int count = iSavedFieldsCount;
            if (iSavedFieldsShared) {
                iSavedFields = savedFields = (SavedField[])iSavedFields.clone();
                iSavedFieldsShared = false;
            }
            sort(savedFields, count);

            long millis = iMillis;
            for (int i=0; i<count; i++) {
                millis = savedFields[i].set(millis, resetFields);
            }

            if (iZone == null) {
                millis -= iOffset;
            } else {
                int offset = iZone.getOffsetFromLocal(millis);
                millis -= offset;
                if (offset != iZone.getOffset(millis)) {
                    throw new IllegalArgumentException
                        ("Illegal instant due to time zone offset transition");
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
         * The end result is much greater performace when computeMillis is called.
         * Since the amount of saved fields is small, the insertion sort is a
         * better choice. Additional performance is gained since there is no extra
         * array allocation and copying. Also, the insertion sort here does not
         * perform any casting operations. The version in java.util.Arrays performs
         * casts within the insertion sort loop.
         */
        private static void sort(Comparable[] array, int high) {
            if (high > 10) {
                Arrays.sort(array, 0, high);
            } else {
                for (int i=0; i<high; i++) {
                    for (int j=i; j>0 && (array[j-1]).compareTo(array[j])>0; j--) {
                        Comparable t = array[j];
                        array[j] = array[j-1];
                        array[j-1] = t;
                    }
                }
            }
        }

        class SavedState {
            final DateTimeZone iZone;
            final int iOffset;
            final SavedField[] iSavedFields;
            final int iSavedFieldsCount;

            SavedState() {
                this.iZone = ParseBucket.this.iZone;
                this.iOffset = ParseBucket.this.iOffset;
                this.iSavedFields = ParseBucket.this.iSavedFields;
                this.iSavedFieldsCount = ParseBucket.this.iSavedFieldsCount;
            }

            boolean restoreState(ParseBucket enclosing) {
                if (enclosing != ParseBucket.this) {
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

        static class SavedField implements Comparable {
            final DateTimeField iField;
            final int iValue;
            final String iText;
            final Locale iLocale;

            SavedField(DateTimeField field, int value) {
                iField = field;
                iValue = value;
                iText = null;
                iLocale = null;
            }

            SavedField(DateTimeField field, String text, Locale locale) {
                iField = field;
                iValue = 0;
                iText = text;
                iLocale = locale;
            }

            long set(long millis, boolean reset) {
                if (iText == null) {
                    millis = iField.set(millis, iValue);
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
            public int compareTo(Object obj) {
                DateTimeField other = ((SavedField)obj).iField;
                int result = compareReverse
                    (iField.getRangeDurationField(), other.getRangeDurationField());
                if (result != 0) {
                    return result;
                }
                return compareReverse
                    (iField.getDurationField(), other.getDurationField());
            }

            private int compareReverse(Comparable a, Comparable b) {
                if (a == null) {
                    if (b == null) {
                        return 0;
                    }
                    return -1;
                }
                if (b == null) {
                    return 1;
                }
                return -a.compareTo(b);
            }
        }
    }

}
