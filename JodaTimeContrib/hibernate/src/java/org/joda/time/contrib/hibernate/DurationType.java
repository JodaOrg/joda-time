package org.joda.time.contrib.hibernate;

import org.joda.time.Duration;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.DurationConverter;

/**
 * Converts a org.joda.time.Duration to and from Sql for Hibernate.
 * It simply stores the value as a varchar (using Duration.toString),
 * and retrieves it using a DurationConverter.
 * TODO : are we persisting Chronologies ? is it any relevant ?
 *
 * @author gjoseph
 * @author $Author: $ (last edit)
 * @version $Revision: $
 */
public class DurationType extends AbstractStringBasedJodaType {

    public Class returnedClass() {
        return Duration.class;
    }

    protected Object fromNonNullString(String s) {
        final DurationConverter converter = ConverterManager.getInstance().getDurationConverter(s);
        final long durationMillis = converter.getDurationMillis(s);
        return new Duration(durationMillis);
    }

    protected String toNonNullString(Object value) {
        return value.toString();
    }

}
