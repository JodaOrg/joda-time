package org.joda.time.contrib.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.joda.time.LocalDate;

import java.io.File;
import java.sql.SQLException;

public class TestPersistentLocalDate extends HibernateTestCase
{
    private LocalDate[] writeReadTimes = new LocalDate[]
    {
        new LocalDate(2004, 2, 25),
        new LocalDate(1980, 3, 11)
    };

    public void testSimpleStore() throws SQLException
	{
        SessionFactory factory = getSessionFactory();

        Session session = factory.openSession();

        for (int i = 0; i<writeReadTimes.length; i++)
        {
            LocalDate writeReadTime = writeReadTimes[i];

            Event event = new Event();
            event.setId(i);
            event.setLocalDate(writeReadTime);

            session.save(event);
        }

        session.flush();
        session.connection().commit();
        session.close();

        for (int i = 0; i<writeReadTimes.length; i++)
        {
            LocalDate writeReadTime = writeReadTimes[i];

            session = factory.openSession();
            Event eventReread = (Event) session.get(Event.class, new Integer(i));

            assertNotNull("get failed - event#'" + i + "'not found", eventReread);
            assertNotNull("get failed - returned null", eventReread.getLocalDate());

            // we loose the timezone, so we have to normalize both to offset=0
            assertEquals("get failed - returned different time",
                writeReadTime,
                eventReread.getLocalDate());

            session.close();
        }
    }

	protected void setupConfiguration(Configuration cfg)
	{
		cfg.addFile(new File("src/test/org/joda/time/contrib/hibernate/event.hbm.xml"));
		cfg.addFile(new File("src/test/org/joda/time/contrib/hibernate/eventTZ.hbm.xml"));
	}
}
