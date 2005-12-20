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

import javax.servlet.jsp.JspTagException;

/**
 * <p>
 * A handler for &lt;setDateTimeZone&gt; that supports rtexprvalue-based
 * attributes.
 * </p>
 * 
 * @author Jan Luehe
 * @author Jim Newsham
 */
public class SetDateTimeZoneTag extends SetDateTimeZoneSupport {

    /**
     * Sets the value attribute.
     * 
     * @param value  the value
     */
    public void setValue(Object value) throws JspTagException {
        this.value = value;
    }

}
