package org.joda.time.gwt;

import java.util.Date;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.gwt.util.ExceptionUtils;

import com.google.gwt.i18n.client.TimeZone;

public class GwtTimeZone extends DateTimeZone {

    private final TimeZone gwtTimeZone;

    public GwtTimeZone(TimeZone gwtTimeZone) {
        super(gwtTimeZone.getID());
        this.gwtTimeZone = gwtTimeZone;
    }
    
    public TimeZone getGwtTimeZone() {
        return gwtTimeZone;
    }

    public long previousTransition(long instant) {
        throw ExceptionUtils.unsupportedInGwt();
    }

    public long nextTransition(long instant) {
        throw ExceptionUtils.unsupportedInGwt();
    }

    public boolean isFixed() {
        throw ExceptionUtils.unsupportedInGwt();
    }
    
    public int getStandardOffset(long instant) {
        // TODO looks like in GWT standardOffset does not depend on instant
        return -1 * 1000 * DateTimeConstants.SECONDS_PER_MINUTE * gwtTimeZone.getStandardOffset();
    }

    public int getOffset(long instant) {
        return -1 * 1000 * DateTimeConstants.SECONDS_PER_MINUTE * gwtTimeZone.getOffset(new Date(instant));
    }

    public String getNameKey(long instant) {
        return gwtTimeZone.getShortName(new Date(instant));
    }

    public int hashCode() {
        return getID().hashCode();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof GwtTimeZone) {
            // TODO not sure if this is correct (also for hashCode)...
            GwtTimeZone other = (GwtTimeZone) object;
            return getID().equals(other.getID());
        }
        return false;
    }
}