package org.joda.time.contrib.hibernate;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import java.sql.SQLException;
import java.io.File;

public class TestPersistentLocalTime extends HibernateTestCase
{
    private LocalTime[] writeReadTimes = new LocalTime[]
    {
        new LocalTime(14, 2, 25),
        new LocalTime(23, 59, 59, 999),
		new LocalTime(0, 0, 0)
    };

    public void testSimpleStore() throws SQLException
	{
        SessionFactory factory = getSessionFactory();

        Session session = factory.openSession();

        for (int i = 0; i<writeReadTimes.length; i++)
        {
            LocalTime writeReadTime = writeReadTimes[i];

            Event event = new Event();
            event.setId(i);
            event.setLocalTime(writeReadTime);
			event.setLocalTime2(writeReadTime);
			event.setLocalTime3(writeReadTime);

            session.save(event);
        }

        session.flush();
        session.connection().commit();
        session.close();

        for (int i = 0; i<writeReadTimes.length; i++)
        {
            LocalTime writeReadTime = writeReadTimes[i];

            session = factory.openSession();
            Event eventReread = (Event) session.get(Event.class, new Integer(i));

            assertNotNull("get failed - event#'" + i + "'not found", eventReread);
            assertNotNull("get failed - returned null", eventReread.getLocalTime());
			assertNotNull("get failed - returned null", eventReread.getLocalTime2());
			assertNotNull("get failed - returned null", eventReread.getLocalTime3());

            // we might loose the millis, depends on database
            assertEquals("get failed - returned different time (TIME)",
                writeReadTime.getMillisOfDay()/1000,
                eventReread.getLocalTime().getMillisOfDay()/1000);

			assertEquals("get failed - returned different time (INT)",
				writeReadTime.getMillisOfDay(),
				eventReread.getLocalTime2().getMillisOfDay());

			assertEquals("get failed - returned different time (STRING)",
				writeReadTime.getMillisOfDay(),
				eventReread.getLocalTime3().getMillisOfDay());

			session.close();
        }
    }

	protected void setupConfiguration(Configuration cfg)
	{
		cfg.addFile(new File("src/test/org/joda/time/contrib/hibernate/event.hbm.xml"));
		cfg.addFile(new File("src/test/org/joda/time/contrib/hibernate/eventTZ.hbm.xml"));
	}
}
