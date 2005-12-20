/*
 * Copyright 1999-2004 The Apache Software Foundation.
 * Modifications, Copyright 2005 Stephen Colebourne
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.joda.time.contrib.jsptag;

import java.util.Locale;

import javax.servlet.jsp.JspTagException;

import org.joda.time.DateTimeZone;

/**
 * <p>
 * A handler for &lt;format&gt; that supports rtexprvalue-based attributes.
 * </p>
 * 
 * @author Jan Luehe
 * @author Jim Newsham
 */
public class FormatTag extends FormatSupport {

    /**
     * Sets the value attribute.
     * 
     * @param value  the value
     */
    public void setValue(Object value) throws JspTagException {
        this.value = value;
    }

    /**
     * Sets the style attribute.
     * 
     * @param style  the style
     */
    public void setStyle(String style) throws JspTagException {
        this.style = style;
    }

    /**
     * Sets the pattern attribute.
     * 
     * @param pattern  the pattern
     */
    public void setPattern(String pattern) throws JspTagException {
        this.pattern = pattern;
    }

    /**
     * Sets the zone attribute.
     * 
     * @param dtz  the zone
     */
    public void setDateTimeZone(Object dtz) throws JspTagException {
        if (dtz == null || dtz instanceof String
                && ((String) dtz).length() == 0) {
            this.dateTimeZone = null;
        } else if (dtz instanceof DateTimeZone) {
            this.dateTimeZone = (DateTimeZone) dtz;
        } else {
            try {
                this.dateTimeZone = DateTimeZone.forID((String) dtz);
            } catch (IllegalArgumentException iae) {
                this.dateTimeZone = DateTimeZone.UTC;
            }
        }
    }

    /**
     * Sets the style attribute.
     * 
     * @param loc  the locale
     */
    public void setLocale(Object loc) throws JspTagException {
        if (loc == null
                || (loc instanceof String && ((String) loc).length() == 0)) {
            this.locale = null;
        } else if (loc instanceof Locale) {
            this.locale = (Locale) loc;
        } else {
            this.locale = Util.parseLocale((String) loc);
        }
    }

}
