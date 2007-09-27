package org.joda.time.contrib.hibernate;

import org.joda.time.Duration;

/**
 * Converts a org.joda.time.Duration to and from Sql for Hibernate.
 * It simply stores the value as a varchar using Duration.toString.
 *
 * @author gjoseph
 * @author $Author: $ (last edit)
 * @version $Revision: $
 */
public class PersistentDuration extends AbstractStringBasedJodaType {

    public Class returnedClass() {
        return Duration.class;
    }

    protected Object fromNonNullString(String s) {
        return new Duration(s);
    }

    protected String toNonNullString(Object value) {
        return value.toString();
    }

}
