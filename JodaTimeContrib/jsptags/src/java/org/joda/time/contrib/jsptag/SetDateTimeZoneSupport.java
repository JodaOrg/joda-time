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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.tagext.TagSupport;

import org.joda.time.DateTimeZone;

/**
 * Support for tag handlers for &lt;setDateTimeZone&gt;.
 * 
 * @author Jan Luehe
 * @author Jim Newsham
 */
public abstract class SetDateTimeZoneSupport extends TagSupport {

    /** The value attribute. */
    protected Object value;
    /** The scope attribute. */
    private int scope;
    /** The var attribute. */
    private String var;

    /**
     * Constructor.
     */
    public SetDateTimeZoneSupport() {
        super();
        init();
    }

    // resets local state
    private void init() {
        value = null;
        var = null;
        scope = PageContext.PAGE_SCOPE;
    }

    public void setScope(String scope) {
        this.scope = Util.getScope(scope);
    }

    public void setVar(String var) {
        this.var = var;
    }

    public int doEndTag() throws JspException {
        DateTimeZone dateTimeZone = null;
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

        if (var != null) {
            pageContext.setAttribute(var, dateTimeZone, scope);
        } else {
            Config.set(pageContext, DateTimeZoneSupport.FMT_TIME_ZONE,
                    dateTimeZone, scope);
        }

        return EVAL_PAGE;
    }

    // Releases any resources we may have (or inherit)
    public void release() {
        init();
    }

}
