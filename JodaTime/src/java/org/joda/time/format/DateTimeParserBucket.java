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

import java.util.Arrays;
import java.util.Locale;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;

/**
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
 * DateTimeParserBucket is mutable and not thread-safe.
 *
 * @author Brian S O'Neill
 */
public class DateTimeParserBucket {

    final long iMillis;

    // TimeZone to switch to in computeMillis. If null, use offset.
    DateTimeZone iZone;
    int iOffset;

    SavedField[] iSavedFields = new SavedField[8];
    int iSavedFieldsCount;
    boolean iSavedFieldsShared;

    private Object iSavedState;

    /**
     * @param instantLocal the initial millis from 1970-01-01T00:00:00, local time
     */
    public DateTimeParserBucket(long instantLocal) {
        iMillis = instantLocal;
    }

    /**
     * Returns the time zone used by computeMillis, or null if an offset is
     * used instead.
     */
    public DateTimeZone getDateTimeZone() {
        return iZone;
    }

    /**
     * Set a time zone to be used when computeMillis is called, which
     * overrides any set time zone offset.
     *
     * @param zone the date time zone to operate in, or null if UTC
     */
    public void setDateTimeZone(DateTimeZone zone) {
        iSavedState = null;
        iZone = zone == DateTimeZone.UTC ? null : zone;
        iOffset = 0;
    }

    /**
     * Returns the time zone offset used by computeMillis, unless
     * getDateTimeZone doesn't return null.
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

    /**
     * Saves a datetime field value.
     */
    public void saveField(DateTimeField field, int value) {
        saveField(new SavedField(field, value));
    }

    /**
     * Saves a datetime field text value.
     */
    public void saveField(DateTimeField field, String text, Locale locale) {
        saveField(new SavedField(field, text, locale));
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
            if (((SavedState)savedState).restoreState(this)) {
                iSavedState = savedState;
                return true;
            }
        }
        return false;
    }

    /**
     * Computes the parsed datetime by setting the saved fields. This method is
     * idempotent, but it is not thread-safe.
     *
     * @return milliseconds since 1970-01-01T00:00:00Z
     * @throws IllegalArgumentException if any field is out of range
     */
    public long computeMillis() {
        SavedField[] savedFields = iSavedFields;
        int count = iSavedFieldsCount;
        if (iSavedFieldsShared) {
            iSavedFields = savedFields = (SavedField[])iSavedFields.clone();
            iSavedFieldsShared = false;
        }
        sort(savedFields, count);

        long millis = iMillis;
        for (int i=0; i<count; i++) {
            millis = savedFields[i].set(millis);
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

    private static class SavedField implements Comparable {
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

        long set(long millis) {
            if (iText == null) {
                return iField.set(millis, iValue);
            } else {
                return iField.set(millis, iText, iLocale);
            }
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

    private class SavedState {
        final DateTimeZone iZone;
        final int iOffset;
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
}
