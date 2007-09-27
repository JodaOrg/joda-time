package org.joda.time.contrib.hibernate;

import org.joda.time.Period;

/**
 * Converts a org.joda.time.Period to and from Sql for Hibernate.
 * It simply stores and retrieves the value as a varchar using Period.toString.
 *
 * @author gjoseph
 * @author $Author: $ (last edit)
 * @version $Revision: $
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
