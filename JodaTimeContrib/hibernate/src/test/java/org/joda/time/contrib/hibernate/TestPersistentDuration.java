package org.joda.time.contrib.hibernate;

import junit.framework.Assert;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.contrib.hibernate.testmodel.SomethingThatLasts;

import java.io.IOException;
import java.io.File;
import java.sql.SQLException;

/**
 * @author gjoseph
 * @author $Author: $ (last edit)
 * @version $Revision: $
 */
public class TestPersistentDuration extends HibernateTestCase {
    protected void setupConfiguration(Configuration cfg) {
        cfg.addFile(new File("src/test/java/org/joda/time/contrib/hibernate/testmodel/SomethingThatLasts.hbm.xml"));
    }

    private Duration[] durations = new Duration[]{
            Duration.ZERO, new Duration(30), Period.seconds(30).toDurationTo(new DateTime()), Period.months(3).toDurationFrom(new DateTime())
    };

    public void testSimpleStore() throws SQLException, IOException {
        Session session = getSessionFactory().openSession();

        for (int i = 0; i < durations.length; i++) {
            SomethingThatLasts thing = new SomethingThatLasts();
            thing.setId(i);
            thing.setName("test_" + i);
            thing.setTheDuration(durations[i]);
            session.save(thing);
        }

        session.flush();
        session.connection().commit();
        session.close();

        for (int i = 0; i < durations.length; i++) {
            session = getSessionFactory().openSession();
            SomethingThatLasts lastingThing = (SomethingThatLasts) session.get(SomethingThatLasts.class, new Long(i));

            Assert.assertNotNull(lastingThing);
            Assert.assertEquals(i, lastingThing.getId());
            Assert.assertEquals("test_" + i, lastingThing.getName());
            Assert.assertEquals(durations[i], lastingThing.getTheDuration());

            session.close();
        }

        // printSqlQueryResults("SELECT * FROM lasting");
    }

}
