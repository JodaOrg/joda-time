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
package org.joda.time.contrib.holiday.anniversary;

import org.joda.time.DateTime;
/**
 * 
 * @author Al Major
 *
 */
public class NamedAnniversaryFactory implements
        IAnniversaryFactory<NamedAnniversaryFactory.NamedAnniversary> {
    public class NamedAnniversary implements Comparable {
        final DateTime fAnniversary;

        NamedAnniversary(DateTime dm) {
            fAnniversary = dm;
        }

        public String getName() {
            return fAnniversaryName;
        }

        public DateTime getDate() {
            return fAnniversary;
        }

        public int compareTo(Object o) {
            if ( o instanceof NamedAnniversary ) {
                return fAnniversary.compareTo(((NamedAnniversary)o).fAnniversary);
            } else {
                assert false;
                return 0;
            }
        }
    }

    final String fAnniversaryName;

    final AnniversaryFactory fFactory;

    public NamedAnniversaryFactory(String strName, AnniversaryFactory af) {
        fAnniversaryName = strName;
        fFactory = af;
    }

    public NamedAnniversary create(int iYear) {
        return new NamedAnniversary(fFactory.create(iYear));
    }
}
