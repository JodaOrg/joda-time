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

import java.io.File;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.joda.time.Instant;

public class TestPersistentInstant extends HibernateTestCase
{
    private Instant[] writeReadTimes = new Instant[]
    {
        new Instant(0),
		new Instant(1000),
		new Instant(1000000)
    };

    public void testSimpleStore() throws SQLException
    {
        SessionFactory factory = getSessionFactory();

        Session session = factory.openSession();

        for (int i = 0; i<writeReadTimes.length; i++)
        {
            Instant writeReadTime = writeReadTimes[i];

            ThingWithInstant thing = new ThingWithInstant();
            thing.setId(i);
            thing.setInstant(writeReadTime);

            session.save(thing);
        }

        session.flush();
        session.connection().commit();
        session.close();

        for (int i = 0; i<writeReadTimes.length; i++)
        {
            Instant writeReadTime = writeReadTimes[i];

            session = factory.openSession();
            ThingWithInstant thingReread = (ThingWithInstant)session.get(ThingWithInstant.class, new Integer(i));

            assertNotNull("get failed - thing#'" + i + "'not found", thingReread);
            assertNotNull("get failed - returned null", thingReread.getInstant());

			Instant reReadTime = thingReread.getInstant();
			if (writeReadTime.getMillis() != reReadTime.getMillis())
			{
				fail("get failed - returned different date. expected " + writeReadTime + " was " + thingReread.getInstant());
			}
		}

		session.close();
    }

	protected void setupConfiguration(Configuration cfg)
	{
		cfg.addFile(new File("src/test/java/org/joda/time/contrib/hibernate/thingWithInstant.hbm.xml"));
	}
}
