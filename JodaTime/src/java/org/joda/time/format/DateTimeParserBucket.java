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
 * Allows fields to be saved in any order, but physically set in a consistent
 * order.
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

        if (savedFieldsCount == savedFields.length) {
            SavedField[] newArray = new SavedField[savedFieldsCount * 2];
            System.arraycopy(savedFields, 0, newArray, 0, savedFieldsCount);
            iSavedFields = savedFields = newArray;
            iSavedFieldsShared = false;
        } else if (iSavedFieldsShared) {
            iSavedFields = savedFields = (SavedField[])savedFields.clone();
            iSavedFieldsShared = false;
        }

        iSavedState = null;
        savedFields[savedFieldsCount] = field;
        iSavedFieldsCount = savedFieldsCount + 1;
    }

    /**
     * Saves the state of this bucket, returning it in an opaque object. Call
     * undoChanges to undo any changes that were made since the state was
     * saved. Calls to saveState may be nested.
     *
     * @return opaque saved state, which may be passed to undoChanges
     */
    public Object saveState() {
        if (iSavedState == null) {
            iSavedState = new SavedState();
        }
        return iSavedState;
    }

    /**
     * Undos any changes that were made to this bucket since the given state
     * was saved. Once the changes have been undone, they are lost. Any states
     * that were saved after saving the previous state are also lost.
     * <p>
     * The state object passed into this method is not lost, and it can be used
     * later to revert to that state again.
     *
     * @param savedState opaque saved state, returned from saveState
     * @return true state object is valid and changes were undone
     */
    public boolean undoChanges(Object savedState) {
        if (savedState instanceof SavedState) {
            if (((SavedState)savedState).revertState(this)) {
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
        Arrays.sort(savedFields, 0, count);

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
         * The field with the larger range is ordered first, where null is
         * considered infinite. If the ranges match, then the field with the
         * larger unit is ordered first. This ordering casues "smaller" fields
         * to be set last, and thus their value sticks. For example, dayOfMonth
         * takes precedence over monthOfYear, and dayOfWeek takes precedence
         * over dayOfMonth.
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
            DateTimeParserBucket.this.iSavedFieldsShared = true;
        }

        boolean revertState(DateTimeParserBucket enclosing) {
            if (enclosing != DateTimeParserBucket.this) {
                return false;
            }
            DateTimeParserBucket.this.iZone = this.iZone;
            DateTimeParserBucket.this.iOffset = this.iOffset;
            DateTimeParserBucket.this.iSavedFields = this.iSavedFields;
            DateTimeParserBucket.this.iSavedFieldsCount = this.iSavedFieldsCount;
            DateTimeParserBucket.this.iSavedFieldsShared = true;
            return true;
        }
    }
}
