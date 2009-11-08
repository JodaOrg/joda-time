/*
 *  Copyright 2001-2007 Stephen Colebourne
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

import java.io.File;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class TestPersistentDateTime extends HibernateTestCase
{
    private DateTime[] writeReadTimes = new DateTime[]
    {
        new DateTime(2004, 2, 25, 17, 3, 45, 760),
        new DateTime(1980, 3, 11,  2, 3, 45,   0, DateTimeZone.forOffsetHours(2))
    };

    public void testSimpleStore() throws SQLException
    {
        SessionFactory factory = getSessionFactory();

        Session session = factory.openSession();

        for (int i = 0; i<writeReadTimes.length; i++)
        {
            DateTime writeReadTime = writeReadTimes[i];

            Event event = new Event();
            event.setId(i);
            event.setDateTime(writeReadTime);

            session.save(event);
        }

        session.flush();
        session.connection().commit();
        session.close();

        for (int i = 0; i<writeReadTimes.length; i++)
        {
            DateTime writeReadTime = writeReadTimes[i];

            session = factory.openSession();
            Event eventReread = (Event) session.get(Event.class, new Integer(i));

            assertNotNull("get failed - event#'" + i + "'not found", eventReread);
            assertNotNull("get failed - returned null", eventReread.getDateTime());

            // we loose the timezone, so we have to normalize both to offset=0
            assertEquals("get failed - returned different time",
                writeReadTime.toDateTime(DateTimeZone.forOffsetHours(0)),
                eventReread.getDateTime().toDateTime(DateTimeZone.forOffsetHours(0)));

            session.close();
        }
    }

    public void testStoreWithTimezone() throws SQLException
    {
        SessionFactory factory = getSessionFactory();

        Session session = factory.openSession();

        for (int i = 0; i<writeReadTimes.length; i++)
        {
            DateTime writeReadTime = writeReadTimes[i];

            EventTZ event = new EventTZ();
            event.setId(i);
            event.setDateTime(writeReadTime);

            session.save(event);
        }

        session.flush();
        session.connection().commit();
        session.close();

        for (int i = 0; i<writeReadTimes.length; i++)
        {
            DateTime writeReadTime = writeReadTimes[i];

            session = factory.openSession();
            EventTZ eventReread = (EventTZ) session.get(EventTZ.class, new Integer(i));

            assertNotNull("get failed - event#'" + i + "'not found", eventReread);
            assertNotNull("get failed - returned null", eventReread.getDateTime());

            assertEquals("get failed - returned different time",
                writeReadTime, eventReread.getDateTime());
        }
		
		session.close();
    }

	protected void setupConfiguration(Configuration cfg)
	{
		cfg.addFile(new File("src/test/java/org/joda/time/contrib/hibernate/event.hbm.xml"));
		cfg.addFile(new File("src/test/java/org/joda/time/contrib/hibernate/eventTZ.hbm.xml"));
	}
}
