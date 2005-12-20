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

import java.io.IOException;
import java.text.DateFormat;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Support for tag handlers for &lt;parseDate&gt;, the date and time parsing tag
 * in JSTL 1.0.
 * 
 * @author Jan Luehe
 * @author Jim Newsham
 */
public abstract class ParseDateTimeSupport extends BodyTagSupport {

    /** The value attribute. */
    protected String value;
    /** Status of the value. */
    protected boolean valueSpecified;
    /** The pattern attribute. */
    protected String pattern;
    /** The style attribute. */
    protected String style;
    /** The zone attribute. */
    protected DateTimeZone dateTimeZone;
    /** The locale attribute. */
    protected Locale locale;
    /** The var attribute. */
    private String var;
    /** The scope attribute. */
    private int scope;

    /**
     * Constructor.
     */
    public ParseDateTimeSupport() {
        super();
        init();
    }

    private void init() {
        value = null;
        valueSpecified = false;
        pattern = null;
        style = null;
        dateTimeZone = null;
        locale = null;
        scope = PageContext.PAGE_SCOPE;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setScope(String scope) {
        this.scope = Util.getScope(scope);
    }

    public int doEndTag() throws JspException {
        String input = null;

        // determine the input by...
        if (valueSpecified) {
            // ... reading 'value' attribute
            input = value;
        } else {
            // ... retrieving and trimming our body
            if (bodyContent != null && bodyContent.getString() != null) {
                input = bodyContent.getString().trim();
            }
        }

        if ((input == null) || input.equals("")) {
            if (var != null) {
                pageContext.removeAttribute(var, scope);
            }
            return EVAL_PAGE;
        }

        // Create formatter
        DateTimeFormatter formatter;
        if (pattern != null) {
            formatter = DateTimeFormat.forPattern(pattern);
        } else if (style != null) {
            formatter = DateTimeFormat.forStyle(style);
        } else {
            formatter = DateTimeFormat.fullDateTime();
        }

        // set formatter locale
        Locale locale = this.locale;
        if (locale == null) {
            locale = Util.getFormattingLocale(pageContext, this, true,
                    DateFormat.getAvailableLocales());
        }
        if (locale != null) {
            formatter = formatter.withLocale(locale);
        }

        // set formatter timezone
        DateTimeZone tz = this.dateTimeZone;
        if (tz == null) {
            tz = DateTimeZoneSupport.getDateTimeZone(pageContext, this);
        }
        if (tz != null) {
            formatter = formatter.withZone(tz);
        }

        // Parse date
        DateTime parsed = null;
        try {
            parsed = formatter.parseDateTime(input);
        } catch (IllegalArgumentException iae) {
            throw new JspException(Resources.getMessage(
                    "PARSE_DATE_PARSE_ERROR", input), iae);
        }

        if (var != null) {
            pageContext.setAttribute(var, parsed, scope);
        } else {
            try {
                pageContext.getOut().print(parsed);
            } catch (IOException ioe) {
                throw new JspTagException(ioe.toString(), ioe);
            }
        }

        return EVAL_PAGE;
    }

    // Releases any resources we may have (or inherit)
    public void release() {
        init();
    }

}
