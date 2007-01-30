package org.joda.time.contrib.hibernate;

import org.hibernate.usertype.EnhancedUserType;
import org.hibernate.HibernateException;
import org.hibernate.Hibernate;
import org.joda.time.LocalTime;

import java.sql.Types;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.io.Serializable;

/**
 * Persist {@link org.joda.time.LocalDate} via hibernate
 *
 * @author Mario Ivankovits (mario@ops.co.at)
 */
public class PersistentLocalTimeExact implements EnhancedUserType
{
	public final static PersistentLocalTimeExact INSTANCE = new PersistentLocalTimeExact();

	private static final int[] SQL_TYPES = new int[]
    {
        Types.INTEGER,
    };

    public int[] sqlTypes()
    {
        return SQL_TYPES;
    }

    public Class returnedClass()
    {
        return LocalTime.class;
    }

    public boolean equals(Object x, Object y) throws HibernateException
	{
        if (x == y)
        {
            return true;
        }
        if (x == null || y == null)
        {
            return false;
        }
        LocalTime dtx = (LocalTime) x;
        LocalTime dty = (LocalTime) y;

        return dtx.equals(dty);
    }

    public int hashCode(Object object) throws HibernateException
    {
        return object.hashCode();
    }

    public Object nullSafeGet(ResultSet resultSet, String[] strings, Object object) throws HibernateException, SQLException
	{
		return nullSafeGet(resultSet, strings[0]);

	}

	public Object nullSafeGet(ResultSet resultSet, String string) throws SQLException
	{
		Object timestamp = Hibernate.INTEGER.nullSafeGet(resultSet, string);
		if (timestamp == null)
		{
			return null;
		}

		return LocalTime.fromMillisOfDay(((Number) timestamp).intValue());
	}


	public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index) throws HibernateException, SQLException
	{
		if (value == null)
		{
			Hibernate.INTEGER.nullSafeSet(preparedStatement, null, index);
		}
		else
		{
			LocalTime lt = ((LocalTime) value);

			Hibernate.INTEGER.nullSafeSet(preparedStatement, new Integer(lt.getMillisOfDay()), index);
		}
	}

    public Object deepCopy(Object value) throws HibernateException
    {
        if (value == null)
        {
            return null;
        }

        return new LocalTime(value);
    }

    public boolean isMutable()
    {
        return false;
    }

    public Serializable disassemble(Object value) throws HibernateException
    {
        return (Serializable) value;
    }

    public Object assemble(Serializable cached, Object value) throws HibernateException
    {
        return cached;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException
    {
        return original;
    }

	public String objectToSQLString(Object object)
	{
		throw new UnsupportedOperationException();
	}

	public String toXMLString(Object object)
	{
		return object.toString();
	}

	public Object fromXMLString(String string)
	{
		return new LocalTime(string);
	}
}
