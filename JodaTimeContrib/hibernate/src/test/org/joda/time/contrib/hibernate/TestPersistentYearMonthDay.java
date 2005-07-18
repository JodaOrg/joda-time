package org.joda.time.contrib.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.joda.time.YearMonthDay;

import java.io.File;
import java.sql.SQLException;

public class TestPersistentYearMonthDay extends HibernateTestCase
{
    private YearMonthDay[] writeReadTimes = new YearMonthDay[]
    {
        new YearMonthDay(2004, 2, 25),
        new YearMonthDay(1980, 3, 11)
    };

    public void testSimpleStore() throws SQLException
    {
        SessionFactory factory = getSessionFactory();

        Session session = factory.openSession();

        for (int i = 0; i<writeReadTimes.length; i++)
        {
            YearMonthDay writeReadTime = writeReadTimes[i];

            Schedule event = new Schedule();
            event.setId(i);
            event.setStartDate(writeReadTime);

            session.save(event);
        }

        session.flush();
        session.connection().commit();
        session.close();

        for (int i = 0; i<writeReadTimes.length; i++)
        {
            YearMonthDay writeReadTime = writeReadTimes[i];

            session = factory.openSession();
            Schedule eventReread = (Schedule) session.get(Schedule.class, new Integer(i));

            assertNotNull("get failed - event#'" + i + "'not found", eventReread);
            assertNotNull("get failed - returned null", eventReread.getStartDate());

            assertEquals("get failed - returned different date", writeReadTime, eventReread.getStartDate());

            session.close();
        }
    }

	protected void setupConfiguration(Configuration cfg)
	{
		cfg.addFile(new File("src/test/org/joda/time/contrib/hibernate/schedule.hbm.xml"));
	}
}
