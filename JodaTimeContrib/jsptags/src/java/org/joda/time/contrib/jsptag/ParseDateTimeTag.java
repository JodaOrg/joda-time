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

import java.util.Locale;

import javax.servlet.jsp.JspTagException;

import org.joda.time.DateTimeZone;

/**
 * <p>A handler for &lt;parseDate&gt; that supports rtexprvalue-based
 * attributes.</p>
 * @author Jan Luehe
 * @author Jim Newsham
 */

public class ParseDateTimeTag extends ParseDateTimeSupport {

  // 'value' attribute
  public void setValue(String value) throws JspTagException {
    this.value = value;
    this.valueSpecified = true;
  }

  // 'style' attribute
  public void setStyle(String style) throws JspTagException {
    this.style = style;
  }

  // 'pattern' attribute
  public void setPattern(String pattern) throws JspTagException {
    this.pattern = pattern;
  }

  // 'dateTimeZone' attribute
  public void setDateTimeZone(Object dtz) throws JspTagException {
    if (dtz == null || dtz instanceof String && ((String) dtz).length() == 0) {
      this.dateTimeZone = null;
    }
    else if (dtz instanceof DateTimeZone) {
      this.dateTimeZone = (DateTimeZone) dtz;
    }
    else {
      try {
        String s = (String) dtz;
        this.dateTimeZone = DateTimeZone.forID((String) dtz);
      }
      catch(IllegalArgumentException iae) {
        this.dateTimeZone = DateTimeZone.UTC;
      }
    }
  }

  // 'locale' attribute
  public void setLocale(Object loc) throws JspTagException {
    if (loc == null || 
      (loc instanceof String && ((String) loc).length() == 0)) {
      this.locale = null;
    }
    else if (loc instanceof Locale) {
      this.locale = (Locale) loc;
    } 
    else {
      locale = Util.parseLocale((String) loc);
    }
  }
}
