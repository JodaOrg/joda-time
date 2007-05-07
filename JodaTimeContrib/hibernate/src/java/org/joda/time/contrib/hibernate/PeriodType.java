package org.joda.time.contrib.hibernate;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;

/**
 * Converts a org.joda.time.Period to and from Sql for Hibernate.
 * It simply stores and retrieves the value as a varchar, using
 * PeriodFormat.
 *
 * TODO : are we persisting Chronologies ? is it any relevant ?
 * TODO : how are we handling conversions that might end up too long for the column ? 
 *
 * @author gjoseph
 * @author $Author: $ (last edit)
 * @version $Revision: $
 */
public class PeriodType extends AbstractStringBasedJodaType {

    public Class returnedClass() {
        return Period.class;
    }

    protected Object fromNonNullString(String s) {
        return PeriodFormat.getDefault().parsePeriod(s);
    }

    protected String toNonNullString(Object value) {
        return PeriodFormat.getDefault().print((Period) value);
    }

}
