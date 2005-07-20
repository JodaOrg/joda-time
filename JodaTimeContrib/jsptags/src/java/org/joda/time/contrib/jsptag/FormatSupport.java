/*
 * Copyright 1999-2004 The Apache Software Foundation.
 * Modifications, Copyright 2005 Joda.org
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
import javax.servlet.jsp.tagext.TagSupport;

import org.joda.time.DateTimeZone;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Support for tag handlers for &lt;formatDate&gt;, the date and time
 * formatting tag in JSTL 1.0.
 * @author Jan Luehe
 * @author Jim Newsham
 */

public abstract class FormatSupport extends TagSupport {

  protected Object value;                      // 'value' attribute
  protected String pattern;                    // 'pattern' attribute
  protected String style;                      // 'style' attribute
  protected DateTimeZone dateTimeZone;         // 'dateTimeZone' attribute
  protected Locale locale;                     // 'locale' attribute

  private String var;                          // 'var' attribute
  private int scope;                           // 'scope' attribute


  public FormatSupport() {
    super();
    init();
  }

  private void init() {
    var = null;
    value = null;
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

  /*
   * Formats the given instant or partial.
   */
  public int doEndTag() throws JspException {
    if (value == null) {
      if (var != null) {
        pageContext.removeAttribute(var, scope);
      }
      return EVAL_PAGE;
    }

    // Create formatter
    DateTimeFormatter formatter;
    if (pattern != null) {
      formatter = DateTimeFormat.forPattern(pattern);
    }
    else if (style != null) {
      formatter = DateTimeFormat.forStyle(style);
    }
    else {
      // use a medium date (no time) style by default; same as jstl
      formatter = DateTimeFormat.mediumDate();
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

    // format value
    String formatted;
    if (value instanceof ReadableInstant) {
      formatted = formatter.print((ReadableInstant) value);
    }
    else {
      formatted = formatter.print((ReadablePartial) value);
    }

    if (var != null) {
      pageContext.setAttribute(var, formatted, scope);
    } 
    else {
      try {
        pageContext.getOut().print(formatted);
      } 
      catch (IOException ioe) {
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
