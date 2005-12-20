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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.joda.time.DateTimeZone;

/**
 * Support for tag handlers for &lt;timeZone&gt;.
 * 
 * @author Jan Luehe
 * @author Jim Newsham
 */
public abstract class DateTimeZoneSupport extends BodyTagSupport {

    /** The config key for the time zone. */
    public static final String FMT_TIME_ZONE = "org.joda.time.dateTimeZone";

    /** The value attribute. */
    protected Object value;

    /** The zone. */
    private DateTimeZone dateTimeZone;

    /**
     * Constructor.
     */
    public DateTimeZoneSupport() {
        super();
        init();
    }

    private void init() {
        value = null;
    }

    public DateTimeZone getDateTimeZone() {
        return dateTimeZone;
    }

    public int doStartTag() throws JspException {
        if (value == null) {
            dateTimeZone = DateTimeZone.UTC;
        } else if (value instanceof String) {
            try {
                dateTimeZone = DateTimeZone.forID((String) value);
            } catch (IllegalArgumentException iae) {
                dateTimeZone = DateTimeZone.UTC;
            }
        } else {
            dateTimeZone = (DateTimeZone) value;
        }
        return EVAL_BODY_BUFFERED;
    }

    public int doEndTag() throws JspException {
        try {
            pageContext.getOut().print(bodyContent.getString());
        } catch (IOException ioe) {
            throw new JspTagException(ioe.toString(), ioe);
        }
        return EVAL_PAGE;
    }

    // Releases any resources we may have (or inherit)
    public void release() {
        init();
    }

    /**
     * Determines and returns the time zone to be used by the given action.
     * <p>
     * If the given action is nested inside a &lt;dateTimeZone&gt; action,
     * the time zone is taken from the enclosing &lt;dateTimeZone&gt; action.
     * <p>
     * Otherwise, the time zone configuration setting
     * <tt>org.joda.time.FMT_TIME_ZONE</tt> is used.
     * 
     * @param pc  the page containing the action for which the time zone
     *  needs to be determined
     * @param fromTag  the action for which the time zone needs to be determined
     * 
     * @return the time zone, or <tt> null </tt> if the given action is not
     * nested inside a &lt;dateTimeZone&gt; action and no time zone configuration
     * setting exists
     */
    static DateTimeZone getDateTimeZone(PageContext pc, Tag fromTag) {
        DateTimeZone tz = null;

        Tag t = findAncestorWithClass(fromTag, DateTimeZoneSupport.class);
        if (t != null) {
            // use time zone from parent <timeZone> tag
            DateTimeZoneSupport parent = (DateTimeZoneSupport) t;
            tz = parent.getDateTimeZone();
        } else {
            // get time zone from configuration setting
            Object obj = Config.find(pc, FMT_TIME_ZONE);
            if (obj != null) {
                if (obj instanceof DateTimeZone) {
                    tz = (DateTimeZone) obj;
                } else {
                    try {
                        tz = DateTimeZone.forID((String) obj);
                    } catch (IllegalArgumentException iae) {
                        tz = DateTimeZone.UTC;
                    }
                }
            }
        }

        return tz;
    }

}
