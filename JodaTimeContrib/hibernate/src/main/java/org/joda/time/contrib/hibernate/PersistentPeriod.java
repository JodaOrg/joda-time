/*
 *  Copyright 2001-2009 Stephen Colebourne
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
package org.joda.time.contrib.hibernate;

import org.joda.time.Period;

/**
 * Converts a org.joda.time.Period to and from Sql for Hibernate.
 * It simply stores and retrieves the value as a varchar using Period.toString.
 * 
 * @author gjoseph
 */
public class PersistentPeriod extends AbstractStringBasedJodaType {

    public Class returnedClass() {
        return Period.class;
    }

    protected Object fromNonNullString(String s) {
        return new Period(s);
    }

    protected String toNonNullString(Object value) {
        return value.toString();
    }

}
