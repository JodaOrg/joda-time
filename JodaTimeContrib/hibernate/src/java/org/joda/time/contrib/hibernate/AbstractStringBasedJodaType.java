package org.joda.time.contrib.hibernate;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.joda.time.Period;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author gjoseph
 * @author $Author: $ (last edit)
 * @version $Revision: $
 */
public abstract class AbstractStringBasedJodaType implements UserType {
    private static final int[] SQL_TYPES = new int[]{Types.VARCHAR};

    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    public Object nullSafeGet(ResultSet resultSet, String[] strings, Object object) throws HibernateException, SQLException {
        String s = (String) Hibernate.STRING.nullSafeGet(resultSet, strings[0]);
        if (s == null) {
            return null;
        }

        return fromNonNullString(s);
    }

    protected abstract Object fromNonNullString(String s);

    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index) throws HibernateException, SQLException {
        if (value == null) {
            Hibernate.STRING.nullSafeSet(preparedStatement, null, index);
        } else {
            Hibernate.STRING.nullSafeSet(preparedStatement, toNonNullString(value), index);
        }
    }

    protected abstract String toNonNullString(Object value);

    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }
        if (x == null || y == null) {
            return false;
        }
        // why do this !? .. TODO : maybe we need to check types ?
        // Period px = (Period) x;
        // Period py = (Period) y;

        return x.equals(y);
    }

    public int hashCode(Object object) throws HibernateException {
        return object.hashCode();
    }

    public Object deepCopy(Object value) throws HibernateException {
        if (value == null) {
            return null;
        }

        return new Period(value);
    }

    public boolean isMutable() {
        return false;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public Object assemble(Serializable cached, Object value) throws HibernateException {
        return cached;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}
