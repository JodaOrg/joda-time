package org.joda.time.contrib.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.joda.time.TimeOfDay;

import java.io.File;
import java.sql.SQLException;

public class TestPersistentTimeOfDayExact extends HibernateTestCase
{
    private TimeOfDay[] writeReadTimes = new TimeOfDay[]
    {
        new TimeOfDay(12, 10, 31),
        new TimeOfDay(23,  7, 43, 120)
    };

    public void testSimpleStore() throws SQLException
    {
        SessionFactory factory = getSessionFactory();

        Session session = factory.openSession();

        for (int i = 0; i<writeReadTimes.length; i++)
        {
            TimeOfDay writeReadTime = writeReadTimes[i];

            Schedule event = new Schedule();
            event.setId(i);
            event.setNextTimeMillis(writeReadTime);

            session.save(event);
        }

        session.flush();
        session.connection().commit();
        session.close();

        for (int i = 0; i<writeReadTimes.length; i++)
        {
            TimeOfDay writeReadTime = writeReadTimes[i];

            session = factory.openSession();
            Schedule eventReread = (Schedule) session.get(Schedule.class, new Integer(i));

            assertNotNull("get failed - event#'" + i + "'not found", eventReread);
            assertNotNull("get failed - returned null", eventReread.getNextTimeMillis());

			assertEquals("get failed - returned different date.", writeReadTime, eventReread.getNextTimeMillis());
		}

		session.close();
    }

	protected void setupConfiguration(Configuration cfg)
	{
		cfg.addFile(new File("src/test/org/joda/time/contrib/hibernate/schedule.hbm.xml"));
	}
}
