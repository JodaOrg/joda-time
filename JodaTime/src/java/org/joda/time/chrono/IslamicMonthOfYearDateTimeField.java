/*
 *  Copyright 2001-2005 Stephen Colebourne
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
package org.joda.time.chrono;

import java.util.Locale;

/**
 * Provides month names for IslamicChronology. This implementation is crude and
 * only supports locale language of "ar". For all other locales, names are
 * numbers.
 *
 * @author Brian S O'Neill
 * @since 1.2
 */
final class IslamicMonthOfYearDateTimeField extends BasicMonthOfYearDateTimeField {

    /** Serialization version */
    private static final long serialVersionUID = -4748157875845286249L;

    private static final Locale AR = new Locale("ar", "", "");

    private static final String[] AR_MONTH_NAMES = {
        "\u0645\u062d\u0631\u0645",
        "\u0635\u0641\u0631",
        "\u0631\u0628\u064a\u0639 \u0627\u0644\u0623\u0648\u0644",
        "\u0631\u0628\u064a\u0639 \u0627\u0644\u0622\u062e\u0631",
        "\u062c\u0645\u0627\u062f\u0649 \u0627\u0644\u0623\u0648\u0644\u0649",
        "\u062c\u0645\u0627\u062f\u0649 \u0627\u0644\u0622\u062e\u0631\u0629",
        "\u0631\u062c\u0628",
        "\u0634\u0639\u0628\u0627\u0646",
        "\u0631\u0645\u0636\u0627\u0646",
        "\u0634\u0648\u0627\u0644",
        "\u0630\u0648 \u0627\u0644\u0642\u0639\u062f\u0629",
        "\u0630\u0648 \u0627\u0644\u062d\u062c\u0629"
    };

    private static final int AR_MAX_MONTH_NAME_LENGTH;

    static {
        int max = 0;
        for (int i=0; i<AR_MONTH_NAMES.length; i++) {
            int len = AR_MONTH_NAMES[i].length();
            if (len > max) {
                len = max;
            }
        }
        AR_MAX_MONTH_NAME_LENGTH = max;
    }

    /**
     * Restricted constructor
     */
    IslamicMonthOfYearDateTimeField(IslamicChronology chronology) {
        super(chronology, 12);
    }

    public String getAsText(int fieldValue, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        if (isSupported(locale) && fieldValue >= 1 && fieldValue <= 12) {
            return AR_MONTH_NAMES[fieldValue - 1];
        }
        return super.getAsText(fieldValue, locale);
    }

    protected int convertText(String text, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        if (isSupported(locale)) {
            for (int i=0; i<AR_MONTH_NAMES.length; i++) {
                if (AR_MONTH_NAMES[i].equals(text)) {
                    return i + 1;
                }
            }
        }
        return super.convertText(text, locale);
    }

    public int getMaximumTextLength(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        if (isSupported(locale)) {
            return AR_MAX_MONTH_NAME_LENGTH;
        }
        return super.getMaximumTextLength(locale);
    }

    private boolean isSupported(Locale locale) {
        return locale.getLanguage().equals(AR.getLanguage());
    }
}
