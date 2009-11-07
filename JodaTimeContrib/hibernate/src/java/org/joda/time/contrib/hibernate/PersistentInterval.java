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

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;
import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 * Persist {@link org.joda.time.Interval} via hibernate. Internally, this class
 * collaborates with {@link org.joda.time.contrib.hibernate.PersistentDateTime}
 * to convert the start and end components of an Interval to and from the
 * database correspondents. This class allows clients to execute hibernate or
 * JPA queries using the attribute names "start" and "end." For example,
 * <br />
 * <blockquote>
 * "from Foo where :date is between barInterval.start and barInterval.end"
 * </blockquote>
 * 
 * @author Christopher R. Gardner (chris_gardner76@yahoo.com)
 */
public class PersistentInterval implements CompositeUserType, Serializable {

    private static final String[] PROPERTY_NAMES = new String[] { "start", "end" };

    private static final Type[] TYPES = new Type[] { Hibernate.TIMESTAMP, Hibernate.TIMESTAMP };

    public Object assemble(Serializable cached, SessionImplementor session, Object owner) throws HibernateException {
        return cached;
    }

    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    public Serializable disassemble(Object value, SessionImplementor session) throws HibernateException {
        return (Serializable) value;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }
        if (x == null || y == null) {
            return false;
        }
        return x.equals(y);
    }

    public String[] getPropertyNames() {
        return PROPERTY_NAMES;
    }

    public Type[] getPropertyTypes() {
        return TYPES;
    }

    public Object getPropertyValue(Object component, int property) throws HibernateException {
        Interval interval = (Interval) component;
        return (property == 0) ? interval.getStart().toDate() : interval.getEnd().toDate();
    }

    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    public boolean isMutable() {
        return false;
    }

    public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor session, Object owner)
            throws HibernateException, SQLException {
        if (resultSet == null) {
            return null;
        }
        PersistentDateTime pst = new PersistentDateTime();
        DateTime start = (DateTime) pst.nullSafeGet(resultSet, names[0]);
        DateTime end = (DateTime) pst.nullSafeGet(resultSet, names[1]);
        if (start == null || end == null) {
            return null;
        }
        return new Interval(start, end);
    }

    public void nullSafeSet(PreparedStatement statement, Object value, int index, SessionImplementor session)
            throws HibernateException, SQLException {
        if (value == null) {
            statement.setNull(index, Hibernate.TIMESTAMP.sqlType());
            statement.setNull(index + 1, Hibernate.TIMESTAMP.sqlType());
            return;
        }
        Interval interval = (Interval) value;
        statement.setTimestamp(index, asTimeStamp(interval.getStart()));
        statement.setTimestamp(index + 1, asTimeStamp(interval.getEnd()));
    }

    private Timestamp asTimeStamp(DateTime time) {
        return new Timestamp(time.getMillis());
    }

    public Object replace(Object original, Object target, SessionImplementor session, Object owner)
            throws HibernateException {
        return original;
    }

    public Class returnedClass() {
        return Interval.class;
    }

    public void setPropertyValue(Object component, int property, Object value) throws HibernateException {
        throw new UnsupportedOperationException("Immutable Interval");
    }

}
